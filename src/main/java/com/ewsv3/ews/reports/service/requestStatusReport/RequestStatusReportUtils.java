package com.ewsv3.ews.reports.service.requestStatusReport;

public class RequestStatusReportUtils {

    public static String RequestStatusReportSql = """
            select
                per.full_name,
                per.employee_number,
                srm.request_name,
                srr.reason,
                sapr.date_start,
                sapr.date_end,
                sapr.time_start,
                sapr.time_end,
                sapr.comments,
                si.item_key,
                si.start_date      submit_date,
                si.completion_date approved_date,
                sch_time_start,
                sch_time_end,
                (
                    select
                        listagg(to_char(
                            in_time,
                            'hh:mi am'
                        )
                                || ' - '
                                || to_char(
                            out_time,
                            'hh:mi am'
                        ),
                                ', ') within group(
                        order by
                            in_time
                        )
                    from
                        sc_timecards st2
                    where
                            st2.person_id = per.person_id
                        and st2.person_roster_id = sc.person_roster_id
                        and st2.absence_attendances_id is null
                        and st2.holiday_id is null
                        and st2.person_request_id is null
                )                  punch_lines,
                sc.violation_code,
                decode(
                    si.completion_date,
                    null,
                    'Pending Approval',
                    'Approved'
                )                  status,
                (
                    select
                        listagg(mgr.full_name,
                                ', ') within group(
                        order by
                            mgr.full_name
                        )
                    from
                        sc_notifications wn,
                        sc_person_v      mgr
                    where
                            wn.item_key = si.item_key
                        and upper(
                            wn.status
                        ) = 'OPEN'
                        and wn.action_type   = 'Approval'
                        and ( ( wn.more_info_user_id = mgr.user_id )
                              or ( wn.more_info_user_id is null
                                   and wn.to_user_id    = mgr.user_id ) )
                )                  pending_on
            from
                sc_items                si,
                sc_tasks                st,
                sc_person_v             per,
                sc_person_requests_appr sapr,
                sc_requests_master      srm,
                sc_request_reasons      srr,
                sc_timecards            sc
            where
                    st.task_id = si.task_id
                and per.person_id             = si.selected_person_id
                and st.task_code              = 'Request'
                and sapr.item_key             = si.item_key
                and srm.request_master_id     = sapr.request_master_id
                and srr.request_master_id (+) = srm.request_master_id
                and srr.request_reason_id (+) = sapr.request_reason_id
                and sc.person_id              = sapr.person_id
                and sc.effective_date         = sapr.date_start
                and sapr.date_start between :startDate and :endDate
                and srm.request_name=nvl(:requestName,srm.request_name)
                and (:status ='Approved' and si.completion_date is not null
                OR  :status ='Pending Approval' and si.completion_date is null
                OR  :status is null)
                and sc.primary_row            = 'Y'
            group by
                per.full_name,
                per.employee_number,
                per.person_id,
                srm.request_name,
                srr.reason,
                sapr.date_start,
                sapr.date_end,
                sapr.time_start,
                sapr.time_end,
                sapr.comments,
                si.item_key,
                si.start_date,
                si.completion_date,
                sc.person_roster_id,
                sch_time_start,
                sch_time_end,
                sc.violation_code
            order by
                sapr.date_start,
                per.full_name""";
}
