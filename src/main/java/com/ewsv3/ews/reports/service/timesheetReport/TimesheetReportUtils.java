package com.ewsv3.ews.reports.service.timesheetReport;

public class TimesheetReportUtils {
    public static String timesheetReportSql = """
            select
                per.employee_number,
                per.full_name,
                per.business_unit_name,
                per.legal_entity,
                per.employee_types,
                tts.effective_date,
                to_char(
                    tts.sch_start_time,
                    'hh:mi am'
                )            schedule_start_time,
                to_char(
                    tts.sch_end_time,
                    'hh:mi am'
                )            schedule_end_time,
                tts.sch_hrs,
                decode(
                    tts.time_hour,
                    'H',
                    null,
                    to_char(
                        tts.time_start,
                        'hh:mi am'
                    )
                )            time_start,
                decode(
                    tts.time_hour,
                    'H',
                    null,
                    to_char(
                        tts.time_end,
                        'hh:mi am'
                    )
                )            time_end,
                pc.pay_code_name,
                tts.reg_hrs,
                sd.department_name,
                sj.job_title,
                tts.comments,
                tk.full_name submitted_by,
                to_char(
                    tts.created_on,
                    'dd-Mon-yyyy hh:mi am'
                )            submitted_on,
                decode(
                          tts.item_key,
                          null,
                          'Draft',
                          decode(
                              si.completion_date,
                              null,
                              'Pending Approval',
                              'Approved'
                          )
                      )           status,
                vn.pending_with,
                (
                    select
                        per.full_name
                        || ' ['
                        || per.employee_number
                        || ']'
                    from
                        sc_notif_comments wc,
                        sc_person_v       per
                    where
                        wc.notif_comment_id in (
                            select
                                min(wc2.notif_comment_id)
                            from
                                sc_notif_comments wc2,
                                sc_notifications  ns
                            where
                                    1 = 1--wc2.notification_id = wc.notification_id
                                and ns.notification_id = wc2.notification_id
                                and wc2.action_taken   = 'APPROVED'
                                and ns.item_key        = tts.item_key
                        )
                        and wc.created_by = per.user_id
                )            first_approver,
                (
                    select
                        per.full_name
                        || ' ['
                        || per.employee_number
                        || ']'
                    from
                        sc_notif_comments wc,
                        sc_person_v       per
                    where
                        wc.notif_comment_id in (
                            select
                                max(wc2.notif_comment_id)
                            from
                                sc_notif_comments wc2,
                                sc_notifications  ns
                            where
                                    wc2.notification_id = wc.notification_id
                                and ns.notification_id = wc2.notification_id
                                and wc2.action_taken   = 'APPROVED'
                                and ns.item_key        = tts.item_key
                        )
                        and wc.notif_comment_id not in (
                            select
                                min(wc2.notif_comment_id)
                            from
                                sc_notif_comments wc2,
                                sc_notifications  ns
                            where
                                    ns.notification_id = wc2.notification_id
                                and wc2.action_taken = 'APPROVED'
                                and ns.item_key      = tts.item_key
                        )
                        and wc.created_by = per.user_id
                )            second_approver
            from
                sc_tts_timesheets          tts,
                sc_items                   si,
                sc_person_v                per,
                sc_person_v                tk,
                sc_departments             sd,
                sc_jobs                    sj,
                sc_pending_notifications_v vn,
                sc_pay_codes               pc
            where
                    si.item_key (+) = tts.item_key
                and per.person_id             = tts.person_id
                and tk.user_id                = tts.created_by
                and tts.cost_center_id        = sd.department_id
                and tts.job_title_id          = sj.job_title_id
                and pc.pay_code_id            = tts.pay_code_id
                and vn.item_key (+)           = si.item_key
                and tts.effective_date between :startDate and :endDate
                and tts.pay_code_id=nvl(:payCodeId,tts.pay_code_id)
                and sd.department_id          = nvl(
                    :departmentId,
                    sd.department_id
                )
                and sj.job_title_id           = nvl(
                    :jobTitleId,
                    sj.job_title_id
                )
                and per.person_id             = nvl(
                    :personId,
                    per.person_id
                )
                and lower(nvl(vn.pending_with,'%')) like lower(:pendingWith)
                and exists (
                    select
                        'Y'
                    from
                        sc_timekeeper_person_v tkv
                    where
                            tkv.profile_id = nvl(:profileId,tkv.profile_id )
                        and tkv.timekeeper_useR_id=:userId
                        and tkv.person_id = tts.person_id
                        and ( lower(
                            tkv.employee_number
                        ) like lower(
                            :employeeText
                        )
                              or lower(
                            tkv.person_name
                        ) like lower(
                            :employeeText
                        ) )
                        and nvl(
                            tkv.hire_date,
                            :startDate
                        ) <= :startDate
                        and nvl(
                            tkv.termination_date,
                            :endDate
                        ) >= :endDate
                )
            order by
                per.full_name,
                tts.effective_date,
                pc.pay_code_name
            offset :offset * :pageSize rows
            fetch next :pageSize rows only
            """;
}
