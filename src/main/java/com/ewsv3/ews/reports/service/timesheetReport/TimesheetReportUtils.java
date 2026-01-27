package com.ewsv3.ews.reports.service.timesheetReport;

public class TimesheetReportUtils {
    public static String timesheetReportSql = """
            SELECT
                per.person_id,
                per.employee_number,
                per.full_name,
                per.business_unit_name,
                per.legal_entity,
                per.employee_types,
                per.grade_name,
                per.location_name,
                tts.effective_date,
                decode(
                    tts.time_hour,
                    'H',
                    NULL,
                    to_char(
                        tts.time_start,
                        'hh:mi am'
                    )
                )            time_start,
                decode(
                    tts.time_hour,
                    'H',
                    NULL,
                    to_char(
                        tts.time_end,
                        'hh:mi am'
                    )
                )            time_end,
                tts.reg_hrs,
                tts.time_hour,
                pc.pay_code_name,
                tts.allw_value,
                sd.department_name,
                sj.job_title,
                NULL         project_name,
                NULL         task_name,
                NULL         exp_type,
                tts.comments,
                sub_per.full_name submitted_by,
                to_char(
                    si.start_date,
                    'dd-Mon-yyyy hh:mi am'
                )            submitted_on,
                decode(
                    tts.item_key,
                    NULL,
                    'Draft',
                    decode(
                        si.completion_date,
                        NULL,
                        'Pending Approval',
                        'Approved'
                    )
                )            status,
                vn.pending_with,
                (
                    SELECT
                        per.full_name
                        || ' ['
                        || per.employee_number
                        || ']'
                      FROM
                        sc_notif_comments wc,
                        sc_person_v       per
                     WHERE
                        wc.notif_comment_id IN (
                            SELECT
                                MIN(wc2.notif_comment_id)
                              FROM
                                sc_notif_comments wc2,
                                sc_notifications  ns
                             WHERE
                                    1 = 1
                --wc2.notification_id = wc.notification_id
                                   AND ns.notification_id = wc2.notification_id
                                   AND wc2.action_taken   = 'APPROVED'
                                   AND ns.item_key        = tts.item_key
                        )
                           AND wc.created_by = per.user_id
                )            first_approver,
                (
                    SELECT
                        per.full_name
                        || ' ['
                        || per.employee_number
                        || ']'
                      FROM
                        sc_notif_comments wc,
                        sc_person_v       per
                     WHERE
                        wc.notif_comment_id IN (
                            SELECT
                                MAX(wc2.notif_comment_id)
                              FROM
                                sc_notif_comments wc2,
                                sc_notifications  ns
                             WHERE
                                    wc2.notification_id = wc.notification_id
                                   AND ns.notification_id = wc2.notification_id
                                   AND wc2.action_taken   = 'APPROVED'
                                   AND ns.item_key        = tts.item_key
                        )
                           AND wc.notif_comment_id NOT IN (
                            SELECT
                                MIN(wc2.notif_comment_id)
                              FROM
                                sc_notif_comments wc2,
                                sc_notifications  ns
                             WHERE
                                    ns.notification_id = wc2.notification_id
                                   AND wc2.action_taken = 'APPROVED'
                                   AND ns.item_key      = tts.item_key
                        )
                           AND wc.created_by = per.user_id
                )            second_approver,
                to_char(
                    tts.sch_start_time,
                    'hh:mi am'
                )            schedule_start_time,
                to_char(
                    tts.sch_end_time,
                    'hh:mi am'
                )            schedule_end_time,
                tts.sch_hrs  sch_hrs,
                to_char(
                    tts.in_time,
                    'hh:mi am'
                )            punch_in_time,
                to_char(
                    tts.out_time,
                    'hh:mi am'
                )            punch_out_time,
                round(
                    (tts.out_time - tts.in_time) * 24,
                    2
                )            punch_hrs,
                decode(
                    rule_code,
                    NULL,
                    'Manual',
                    'Auto'
                )            generation_mode
              FROM
                sc_tts_timesheets          tts,
                sc_items                   si,
                sc_person_v                per,
                sc_person_v                tk,
                sc_departments             sd,
                sc_jobs                    sj,
                sc_pending_notifications_v vn,
                sc_pay_codes               pc,
                sc_person_v                sub_per
             WHERE
                    si.item_key (+) = tts.item_key
                   AND per.person_id      = tts.person_id
                   AND tk.user_id         = tts.created_by
                   AND tts.cost_center_id = sd.department_id
                   AND tts.job_title_id   = sj.job_title_id
                   AND pc.pay_code_id     = tts.pay_code_id
                   AND vn.item_key (+)    = si.item_key
                   and sub_per.person_id(+) = si.CREATED_BY_PERSON_ID
                   AND tts.effective_date BETWEEN :startDate AND :endDate
                   and (:status is null 
                   OR decode(
                                tts.item_key,
                                NULL,
                                'Draft',
                                decode(
                                    si.completion_date,
                                    NULL,
                                    'Pending Approval',
                                    'Approved'
                                )
                            ) = :status)
                   AND (:payCodeName is null 
                   OR pc.pay_code_name   =  :payCodeName)                
                and (:departmentId = 0 OR sd.department_id = :departmentId )
                and (:jobTitleId = 0 OR sj.job_title_id = :jobTitleId )
                   AND lower(
                    nvl(
                        vn.pending_with,
                        '%'
                    )
                ) LIKE lower(
                    :pendingWith
                )
                and tts.person_Id in (:personIds)
             ORDER BY
                per.full_name,
                tts.effective_date,
                pc.pay_code_name""";

    public static String sqlLineManagerRecordsReportSQL= """
            select
                per.person_id,
                per.assignment_id,
                per.employee_number,
                per.assignment_number,
                per.full_name person_name,
                per.job_title,
                per.department_name,
                per.grade_name,
                per.user_id   person_user_id
            from
                sc_person_manager pm,
                sc_person_v       mgr,
                sc_person_v       per
            where
                    mgr.person_id = pm.manager_id
                and mgr.user_id   = :userId
                and per.person_id = pm.person_id
                and (:departmentId = 0 OR per.department_id= :departmentId )
                and (:jobTitleId = 0 OR per.job_Title_Id= :jobTitleId )
                and 'Y'= sc_timesheet_status_filter_f(p_person_id=> pm.person_id , p_start_date=>:startDate , p_end_date=> :endDate ,p_tts_timesheet_id => null, p_filter_flag=> :status )
                and 'Y'= sc_timesheet_pay_code_filter_f(p_person_id=> pm.person_id , p_start_date=>:startDate , p_end_date=> :endDate , p_tts_timesheet_id => null, p_pay_code_flag=> :payCodeName )
                and ( lower(
                            per.employee_number
                        ) like lower(
                            :text
                        )
                              or lower(
                            per.full_name
                        ) like lower(
                            :text
                        ) )
                and nvl(
                        per.hire_date,
                        :startDate
                    ) <= :startDate
                    and nvl(
                        per.termination_date,
                        :endDate
                    ) >= :endDate
            order by per.full_name""";

    public static String sqlTimesheetPersonListReportSQL = """
            select
                person_id,
                assignment_Id,
                employee_number,
                assignment_number,
                person_name,
                job_title,
                department_name,
                grade_name,
                person_user_id
            from
                (
                    select
                        tkv.person_id,
                        tkv.assignment_Id,
                        tkv.employee_number,
                        tkv.assignment_number,
                        tkv.person_name,
                        tkv.job_title,
                        tkv.department_name,
                        tkv.grade_name,
                        tkv.person_user_id
                    from
                        sc_timekeeper_person_v tkv
                    where
                            tkv.timekeeper_user_id = :userId
                        and tkv.profile_id = :profileId
                        and ( lower(
                            tkv.employee_number
                        ) like lower(
                            :text
                        )
                              or lower(
                            tkv.person_name
                        ) like lower(
                            :text
                        ) )
                        and (:departmentId =0 OR tkv.department_id = :departmentId )
                        and (:jobTitleId =0 OR tkv.job_Title_Id = :jobTitleId )
                        and 'Y'= sc_timesheet_status_filter_f(p_person_id=> tkv.person_id , p_start_date=>:startDate , p_end_date=> :endDate ,p_tts_timesheet_id => null, p_filter_flag=> :status )
                        and 'Y'= sc_timesheet_pay_code_filter_f(p_person_id=> tkv.person_id , p_start_date=>:startDate , p_end_date=> :endDate , p_tts_timesheet_id => null, p_pay_code_flag=> :payCodeName )
                        and nvl(
                            tkv.hire_date,
                            :startDate
                        ) <= :startDate
                        and nvl(
                            tkv.termination_date,
                            :endDate
                        ) >= :endDate
                    order by
                        person_name
                )
            offset :offset * :pageSize rows
            fetch next :pageSize rows only""";
}
