package com.ewsv3.ews.reports.service.requestStatusReport;

public class RequestStatusReportUtils {

    public static String RequestStatusReportSql = """
            SELECT
                q.full_name,
                q.employee_number,
                q.department_name,
                q.job_title,
                q.grade_name,
                q.location_name,
                q.request_name,
                q.reason,
                q.date_start,
                q.request_date,
                q.time_start,
                q.request_time,
                q.comments,
                q.item_key,
                q.submit_date,
                q.approved_date,
                q.status,
                q.violation_code,
                q.day_less_mins,
                q.schedule_details,
                q.punch_details,
                q.pending_on
              FROM
                (
                    SELECT
                        per.full_name,
                        per.employee_number,
                        per.department_name,
                        per.job_title,
                        per.grade_name,
                        per.location_name,
                        srm.request_name,
                        srr.reason,
                        date_start,
                        decode(
                            sapr.date_start,
                            sapr.date_end,
                            to_char(
                                sapr.date_start,
                                'dd-Mon-rrrr'
                            ),
                            to_char(
                                sapr.date_start,
                                'dd-Mon-rrrr'
                            )
                            || ' - '
                            || to_char(
                                sapr.date_end,
                                'dd-Mon-rrrr'
                            )
                        )                  request_date,
                        sapr.time_start,
                        ( to_char(
                            sapr.time_start,
                            'hh:mi am'
                        )
                          || ' - '
                          || to_char(
                            sapr.time_end,
                            'hh:mi am'
                        ) )                request_time,
                        --            sapr.time_end,
                        sapr.comments,
                        si.item_key,
                        si.start_date      submit_date,
                        si.completion_date approved_date,
                        decode(
                            si.completion_date,
                            NULL,
                            'Pending Approval',
                            'Approved'
                        )                  status,
                        sapr.violation_code,
                        sapr.day_less_mins,
                        decode(
                            swd.work_duration_code,
                            'OFF',
                            swd.work_duration_code,
                            (swd.time_start
                             || ' - '
                             || swd.time_start)
                        )                  schedule_details,
                        sapr.punch_details,
                        vn.pending_with    pending_on
                      FROM
                        sc_items                   si,
                        sc_tasks                   st,
                        sc_person_v                per,
                        sc_person_requests_appr    sapr,
                        sc_requests_master         srm,
                        sc_request_reasons         srr,
                        sc_timecards               sc,
                        sc_work_duration           swd,
                        sc_pending_notifications_v vn
                     WHERE
                            st.task_id = si.task_id
                           AND per.person_id             = si.selected_person_id
                           AND st.task_code              = 'Request'
                           AND sapr.item_key             = si.item_key
                           AND srm.request_master_id     = sapr.request_master_id
                           AND srr.request_master_id (+) = srm.request_master_id
                           AND srr.request_reason_id (+) = sapr.request_reason_id
                           AND sc.person_id              = sapr.person_id
                           AND sc.effective_date         = sapr.date_start
                           AND sapr.date_start BETWEEN :startDate AND :endDate
                           AND ( lower(
                            per.employee_number
                        ) LIKE lower(
                            :text
                        )
                            OR lower(
                            per.full_name
                        ) LIKE lower(
                            :text
                        ) )
                           AND vn.item_key (+)           = sapr.item_key
                           AND ( :departmentId = 0
                            OR per.department_id          = :departmentId )
                           AND ( :jobTitleId = 0
                            OR per.job_title_id           = :jobTitleId )
                           AND lower(
                            nvl(
                                vn.pending_with,
                                '%'
                            )
                        ) LIKE lower(
                            :pendingWith
                        )
                           AND ( :requestName IS NULL
                            OR srm.request_name           = :requestName )
                           AND ( :status = 'Approved'
                           AND si.completion_date IS NOT NULL
                            OR :status                    = 'Pending Approval'
                           AND si.completion_date IS NULL
                            OR :status IS NULL )
                           AND sc.primary_row            = 'Y'
                           AND swd.work_duration_id (+)  = sapr.work_duration_id
                ) q
             ORDER BY
                q.date_start,
                q.time_start,
                q.full_name
            OFFSET :offset * :pageSize ROWS FETCH NEXT :pageSize ROWS ONLY""";
}
