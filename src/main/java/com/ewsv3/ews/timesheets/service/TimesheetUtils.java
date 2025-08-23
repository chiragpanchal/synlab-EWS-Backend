package com.ewsv3.ews.timesheets.service;

public class TimesheetUtils {

    public static String sqlTimesheetPersonList = """
            select
                person_id,
                assignment_Id,
                employee_number,
                assignment_number,
                person_name,
                job_title,
                department_name,
                grade_name
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
                        tkv.grade_name
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
            --            and 'Y'            = sc_team_timecards_filter_f(
            --                tkv.person_id,
            --                :startDate,
            --                :endDate,
            --                :pFilterFlag
            --            )
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

    public static String sqlTimesheetDateSummary = """
            select
                st.person_id,
                st.effective_date,
                sum(st.sch_hrs) sch_hrs,
                (
                    select
                        sum(st2.act_hrs)
                    from
                        sc_timecards st2
                    where
                            st2.person_id = st.person_id
                        and st2.effective_date        = st.effective_date
                        and nvl(
                            st2.primary_row,
                            'N'
                        ) = 'N'
                )               punch_hrs,
                (
                    select
                        ( sum(swd.break_mins) / 60 )
                    from
                        sc_timecards     st2,
                        sc_work_duration swd
                    where
                            st2.person_id = st.person_id
                        and st2.effective_date        = st.effective_date
                        and nvl(
                            st2.primary_row,
                            'N'
                        ) = 'Y'
                        and swd.work_duration_id      = st2.work_duration_id
                )               break_hrs,
                (
                    select
                        count(st2.timecard_id)
                    from
                        sc_timecards st2
                    where
                            st2.person_id = st.person_id
                        and st2.effective_date        = st.effective_date
                        and nvl(
                            st2.primary_row,
                            'N'
                        ) = 'Y'
                        and st2.violation_code is not null
                )               violation_counts,
                (
                    select
                        count(*)
                    from
                        sc_person_absences_t a
                    where
                            a.person_id = st.person_id
                        and a.leave_date = st.effective_date
                )               leave_counts
            from
                sc_timecards st
            where
                    st.person_id = :personId
                and st.effective_date between :startDate and :endDate
                and nvl(
                    st.primary_row,
                    'N'
                ) = 'Y'
            group by
                st.person_id,
                st.effective_date
            order by
                st.effective_date""";

    public static String sqlTimesheetTableData = """
            select
                tts.person_id,
                tts.effective_date,
                tts.item_key,
                spc.pay_code_name,
                sum(tts.reg_hrs) timesheet_hrs,
                sc_get_tts_status(
                    tts.item_key
                )                approval_status
            from
                sc_tts_timesheets tts,
                sc_pay_codes      spc
            where
                    tts.person_id = :personId
                and spc.pay_code_id  = tts.pay_code_id
                and tts.effective_date = :effectiveDate
            group by
                tts.person_id,
                tts.effective_date,
                tts.item_key,
                spc.pay_code_name
            order by tts.effective_date, spc.pay_code_name""";

    // Bulk queries for performance optimization
    public static String sqlTimesheetDateSummaryBulk = """
            select
                st.person_id,
                st.effective_date,
                sum(st.sch_hrs) sch_hrs,
                (
                    select
                        sum(st2.act_hrs)
                    from
                        sc_timecards st2
                    where
                            st2.person_id = st.person_id
                        and st2.effective_date        = st.effective_date
                        and nvl(
                            st2.primary_row,
                            'N'
                        ) = 'N'
                )               punch_hrs,
                (
                    select
                        ( sum(swd.break_mins) / 60 )
                    from
                        sc_timecards     st2,
                        sc_work_duration swd
                    where
                            st2.person_id = st.person_id
                        and st2.effective_date        = st.effective_date
                        and nvl(
                            st2.primary_row,
                            'N'
                        ) = 'Y'
                        and swd.work_duration_id      = st2.work_duration_id
                )               break_hrs,
                (
                    select
                        count(st2.timecard_id)
                    from
                        sc_timecards st2
                    where
                            st2.person_id = st.person_id
                        and st2.effective_date        = st.effective_date
                        and nvl(
                            st2.primary_row,
                            'N'
                        ) = 'Y'
                        and st2.violation_code is not null
                )               violation_counts,
                (
                    select
                        count(*)
                    from
                        sc_person_absences_t a
                    where
                            a.person_id = st.person_id
                        and a.leave_date = st.effective_date
                )               leave_counts
            from
                sc_timecards st
            where
                    st.person_id in (
                        select p.person_id 
                        from sc_timekeeper_person_v p 
                        where p.timekeeper_user_id = :userId 
                        and p.profile_id = :profileId
                        and ( lower(p.employee_number) like lower(:text) 
                              or lower(p.person_name) like lower(:text) )
                        and nvl(p.hire_date, :startDate) <= :startDate
                        and nvl(p.termination_date, :endDate) >= :endDate
                    )
                and st.effective_date between :startDate and :endDate
                and nvl(
                    st.primary_row,
                    'N'
                ) = 'Y'
            group by
                st.person_id,
                st.effective_date
            order by
                st.person_id, st.effective_date""";

    public static String sqlTimesheetTableDataBulk = """
            select
                tts.person_id,
                tts.effective_date,
                tts.item_key,
                spc.pay_code_name,
                sum(tts.reg_hrs) timesheet_hrs,
                sc_get_tts_status(
                    tts.item_key
                )                approval_status
            from
                sc_tts_timesheets tts,
                sc_pay_codes      spc
            where
                    tts.person_id in (
                        select p.person_id 
                        from sc_timekeeper_person_v p 
                        where p.timekeeper_user_id = :userId 
                        and p.profile_id = :profileId
                        and ( lower(p.employee_number) like lower(:text) 
                              or lower(p.person_name) like lower(:text) )
                        and nvl(p.hire_date, :startDate) <= :startDate
                        and nvl(p.termination_date, :endDate) >= :endDate
                    )
                and spc.pay_code_id = tts.pay_code_id
                and tts.effective_date between :startDate and :endDate
            group by
                tts.person_id,
                tts.effective_date,
                tts.item_key,
                spc.pay_code_name
            order by tts.person_id, tts.effective_date, spc.pay_code_name""";

}
