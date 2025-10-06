package com.ewsv3.ews.timesheets.service.approval;

public class TimesheetUtilsApproval {

    public static String sqlGetApprovalPeriods = """
            select
                start_date,
                end_date
            from
                (
                    select distinct
                        sc_get_period_dates_f(
                                            p_period_type => :periodType,
                                            p_effective_date => tts.effective_date,
                                            p_date_type => 'SD'
                        ) start_date,
                        sc_get_period_dates_f(
                                            p_period_type => :periodType,
                                            p_effective_date => tts.effective_date,
                                            p_date_type => 'ED'
                        ) end_date
                    from
                        sc_notifications  wn,
                        sc_items          si,
                        sc_tasks          st,
                        sc_tts_timesheets tts
                    where
                            si.item_key = wn.item_key
                        and st.task_id    = si.task_id
                        and si.completion_date is null
                        and ( ( wn.more_info_user_id = :userId )
                            or ( wn.more_info_user_id is null
                                and wn.to_user_id = :userId ) )
                        and wn.status     = 'OPEN'
                        and st.task_code  = 'TimesheetsSubmission'
                        and tts.item_key  = si.item_key
                )
            order by
                start_date desc""";

    public static String sqlApprovalTimesheetPersonList = """
            select
                person_id,
                assignment_id,
                employee_number,
                assignment_number,
                person_name,
                job_title,
                department_name,
                grade_name,
                person_user_id
            from
                (
                    select distinct
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
                        sc_notifications  wn,
                        sc_items          si,
                        sc_tasks          st,
                        sc_person_v       per,
                        sc_tts_timesheets tts,
                        sc_pay_codes      spc
                    where
                            si.item_key = wn.item_key
                        and st.task_id      = si.task_id
                        and si.completion_date is null
                        and per.person_id   = si.selected_person_id
                        and st.task_name    = 'Timesheets'
                        and tts.item_key    = si.item_key
                        and ( ( wn.more_info_user_id = :userId )
                            or ( wn.more_info_user_id is null
                                and wn.to_user_id   = :userId ) )
                        and wn.status       = 'OPEN'
                        and spc.pay_code_id = tts.pay_code_id
                        and tts.effective_date between :startDate and :endDate
                        and not exists (
                            select 'Y' from SC_WORK_FLOW_ACTION_QUEUE_T t
                            where nvl(t.FROM_NOTIFICATION_ID,0) = wn.notification_id
                        )
                        and 'Y'             = sc_timesheet_pay_code_filter_f(
                                                                p_person_id => tts.person_id,
                                                                p_start_date => :startDate,
                                                                p_end_date => :endDate,
                                                                p_tts_timesheet_id => tts.tts_timesheet_id
                                                                ,
                                                                p_pay_code_flag => :payCodeFlag
                                )
                    order by
                        full_name
                )
            offset :offset * :pageSize rows
            fetch next :pageSize rows only""";

    public static String sqlApprovalTimesheetTableDataBulk = """
                select
                tts.person_id,
                tts.effective_date,
                tts.item_key,
                spc.pay_code_name,
                sum(tts.reg_hrs) timesheet_hrs,
                null             approval_status
            from
                sc_notifications  wn,
                sc_items          si,
                sc_tasks          st,
                sc_person_v       per,
                sc_tts_timesheets tts,
                sc_pay_codes      spc
            where
                    si.item_key = wn.item_key
                and st.task_id      = si.task_id
                and tts.person_id in (:personIds)
                and si.completion_date is null
                and per.person_id   = si.selected_person_id
                and st.task_name    = 'Timesheets'
                and tts.item_key    = si.item_key
                and ( ( wn.more_info_user_id = :userId )
                    or ( wn.more_info_user_id is null
                        and wn.to_user_id   = :userId ) )
                and wn.status       = 'OPEN'
                and spc.pay_code_id = tts.pay_code_id
                and tts.effective_date between :startDate and :endDate
                and not exists (
                            select 'Y' from SC_WORK_FLOW_ACTION_QUEUE_T t
                            where nvl(t.FROM_NOTIFICATION_ID,0) = wn.notification_id
                        )
                and 'Y'             = sc_timesheet_pay_code_filter_f(
                                                        p_person_id => tts.person_id,
                                                        p_start_date => :startDate,
                                                        p_end_date => :endDate,
                                                        p_tts_timesheet_id => tts.tts_timesheet_id
                                                        ,
                                                        p_pay_code_flag => :payCodeFlag
                        )
            group by
                tts.person_id,
                tts.effective_date,
                tts.item_key,
                spc.pay_code_name
            order by
                tts.person_id,
                tts.effective_date,
                spc.pay_code_name""";

    public static String sqlApprovalTimesheetPayCodeHrs = """
            select
                spc.pay_code_name,
                sum(tts.reg_hrs) timesheet_hrs
            from
                sc_notifications  wn,
                sc_items          si,
                sc_tasks          st,
                sc_person_v       per,
                sc_tts_timesheets tts,
                sc_pay_codes      spc
            where
                    si.item_key = wn.item_key
                and st.task_id      = si.task_id
                and si.completion_date is null
                and per.person_id   = si.selected_person_id
                and st.task_name    = 'Timesheets'
                and tts.item_key    = si.item_key
                and ( ( wn.more_info_user_id = :userId )
                    or ( wn.more_info_user_id is null
                        and wn.to_user_id   = :userId ) )
                and wn.status       = 'OPEN'
                and spc.pay_code_id = tts.pay_code_id
                and tts.effective_date between :startDate and :endDate
                and not exists (
                            select 'Y' from SC_WORK_FLOW_ACTION_QUEUE_T t
                            where nvl(t.FROM_NOTIFICATION_ID,0) = wn.notification_id
                        )
            group by
                spc.pay_code_name
            order by
                spc.pay_code_name""";

}
