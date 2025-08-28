package com.ewsv3.ews.timesheets.service;

public class TimesheetUtils {


    public static String sqlSelfRecord= """
            select
                per.person_id,
                per.assignment_id,
                per.employee_number,
                per.assignment_number,
                per.full_name person_name,
                per.job_title,
                per.department_name,
                per.grade_name,
                per.user_id person_user_id
            from
                sc_person_v per
            where
                per.user_id = :userId
                and 'Y'= sc_timesheet_status_filter_f(p_person_id=> per.person_id , p_start_date=>:startDate , p_end_date=> :endDate , p_filter_flag=> :filterFlag )
                and 'Y'= sc_timesheet_pay_code_filter_f(p_person_id=> per.person_id , p_start_date=>:startDate , p_end_date=> :endDate , p_pay_code_flag=> :payCodeFlag )""";

    public static String sqlLineManagerRecords= """
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
                and 'Y'= sc_timesheet_status_filter_f(p_person_id=> per.person_id , p_start_date=>:startDate , p_end_date=> :endDate , p_filter_flag=> :filterFlag )
                and 'Y'= sc_timesheet_pay_code_filter_f(p_person_id=> per.person_id , p_start_date=>:startDate , p_end_date=> :endDate , p_pay_code_flag=> :payCodeFlag )
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

    public static String sqlTimesheetPersonList = """
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
                        and 'Y'= sc_timesheet_status_filter_f(p_person_id=> tkv.person_id , p_start_date=>:startDate , p_end_date=> :endDate , p_filter_flag=> :filterFlag )
                        and 'Y'= sc_timesheet_pay_code_filter_f(p_person_id=> tkv.person_id , p_start_date=>:startDate , p_end_date=> :endDate , p_pay_code_flag=> :payCodeFlag )
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

//    public static String sqlTimesheetDateSummary = """
//            select
//                st.person_id,
//                st.effective_date,
//                sum(st.sch_hrs) sch_hrs,
//                (
//                    select
//                        sum(st2.act_hrs)
//                    from
//                        sc_timecards st2
//                    where
//                            st2.person_id = st.person_id
//                        and st2.effective_date        = st.effective_date
//                        and nvl(
//                            st2.primary_row,
//                            'N'
//                        ) = 'N'
//                )               punch_hrs,
//                (
//                    select
//                        ( sum(swd.break_mins) / 60 )
//                    from
//                        sc_timecards     st2,
//                        sc_work_duration swd
//                    where
//                            st2.person_id = st.person_id
//                        and st2.effective_date        = st.effective_date
//                        and nvl(
//                            st2.primary_row,
//                            'N'
//                        ) = 'Y'
//                        and swd.work_duration_id      = st2.work_duration_id
//                )               break_hrs,
//                (
//                    select
//                        count(st2.timecard_id)
//                    from
//                        sc_timecards st2
//                    where
//                            st2.person_id = st.person_id
//                        and st2.effective_date        = st.effective_date
//                        and nvl(
//                            st2.primary_row,
//                            'N'
//                        ) = 'Y'
//                        and st2.violation_code is not null
//                )               violation_counts,
//                (
//                    select
//                        count(*)
//                    from
//                        sc_person_absences_t a
//                    where
//                            a.person_id = st.person_id
//                        and a.leave_date = st.effective_date
//                )               leave_counts
//            from
//                sc_timecards st
//            where
//                    st.person_id = :personId
//                and st.effective_date between :startDate and :endDate
//                and nvl(
//                    st.primary_row,
//                    'N'
//                ) = 'Y'
//            group by
//                st.person_id,
//                st.effective_date
//            order by
//                st.effective_date""";
//
//    public static String sqlTimesheetTableData = """
//            select
//                tts.person_id,
//                tts.effective_date,
//                tts.item_key,
//                spc.pay_code_name,
//                sum(tts.reg_hrs) timesheet_hrs,
//                sc_get_tts_status(
//                    tts.item_key
//                )                approval_status
//            from
//                sc_tts_timesheets tts,
//                sc_pay_codes      spc
//            where
//                    tts.person_id = :personId
//                and spc.pay_code_id  = tts.pay_code_id
//                and tts.effective_date = :effectiveDate
//            group by
//                tts.person_id,
//                tts.effective_date,
//                tts.item_key,
//                spc.pay_code_name
//            order by tts.effective_date, spc.pay_code_name""";

    public static String sqlSelfTimesheetDateSummaryBulk = """
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
                and 'Y'= sc_timesheet_status_filter_f(p_person_id=> st.person_id , p_start_date=>st.effective_date , p_end_date=> st.effective_date, p_filter_flag=> :filterFlag )
                and 'Y'= sc_timesheet_pay_code_filter_f(p_person_id=> st.person_id ,p_start_date=>st.effective_date , p_end_date=> st.effective_date , p_pay_code_flag=> :payCodeFlag )
            group by
                st.person_id,
                st.effective_date
            order by
                st.person_id, st.effective_date""";

    public static String sqlSelfTimesheetTableDataBulk = """
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
                    tts.person_id  = :personId
                and spc.pay_code_id = tts.pay_code_id
                and tts.effective_date between :startDate and :endDate
                and 'Y'= sc_timesheet_status_filter_f(p_person_id=> tts.person_id , p_start_date=>tts.effective_date , p_end_date=> tts.effective_date , p_filter_flag=> :filterFlag )
                and 'Y'= sc_timesheet_pay_code_filter_f(p_person_id=> tts.person_id , p_start_date=>tts.effective_date , p_end_date=> tts.effective_date , p_pay_code_flag=> :payCodeFlag )
            group by
                tts.person_id,
                tts.effective_date,
                tts.item_key,
                spc.pay_code_name
            order by tts.person_id, tts.effective_date, spc.pay_code_name""";

    public static String sqlLineManagerTimesheetDateSummaryBulk = """
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
                    select
                        per.person_id
                    from
                        sc_person_manager pm,
                        sc_person_v       mgr,
                        sc_person_v       per
                    where
                            mgr.person_id = pm.manager_id
                        and mgr.user_id   = :userId
                        and per.person_id = pm.person_id
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
                        ) >= :endDate)
                and st.effective_date between :startDate and :endDate
                and 'Y'= sc_timesheet_status_filter_f(p_person_id=> st.person_id , p_start_date=>st.effective_date , p_end_date=> st.effective_date , p_filter_flag=> :filterFlag )
                and 'Y'= sc_timesheet_pay_code_filter_f(p_person_id=> st.person_id , p_start_date=>st.effective_date , p_end_date=> st.effective_date , p_pay_code_flag=> :payCodeFlag )
                and nvl(
                    st.primary_row,
                    'N'
                ) = 'Y'
            group by
                st.person_id,
                st.effective_date
            order by
                st.person_id, st.effective_date""";

    public static String sqlLineManagerTimesheetTableDataBulk = """
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
                    tts.person_id  in (select
                                           per.person_id
                                       from
                                           sc_person_manager pm,
                                           sc_person_v       mgr,
                                           sc_person_v       per
                                       where
                                               mgr.person_id = pm.manager_id
                                           and mgr.user_id   = :userId
                                           and per.person_id = pm.person_id
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
                                           ) >= :endDate)
                and spc.pay_code_id = tts.pay_code_id
                and tts.effective_date between :startDate and :endDate
                and 'Y'= sc_timesheet_status_filter_f(p_person_id=> tts.person_id , p_start_date=>tts.effective_date , p_end_date=> tts.effective_date , p_filter_flag=> :filterFlag )
                and 'Y'= sc_timesheet_pay_code_filter_f(p_person_id=> tts.person_id , p_start_date=>tts.effective_date , p_end_date=> tts.effective_date , p_pay_code_flag=> :payCodeFlag )
            group by
                tts.person_id,
                tts.effective_date,
                tts.item_key,
                spc.pay_code_name
            order by tts.person_id, tts.effective_date, spc.pay_code_name""";

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
                and 'Y'= sc_timesheet_status_filter_f(p_person_id=> st.person_id , p_start_date=>st.effective_date , p_end_date=> st.effective_date , p_filter_flag=> :filterFlag )
                and 'Y'= sc_timesheet_pay_code_filter_f(p_person_id=> st.person_id , p_start_date=>st.effective_date , p_end_date=> st.effective_date , p_pay_code_flag=> :payCodeFlag )
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
                and 'Y'= sc_timesheet_status_filter_f(p_person_id=> tts.person_id , p_start_date=>tts.effective_date , p_end_date=> tts.effective_date , p_filter_flag=> :filterFlag )
                and 'Y'= sc_timesheet_pay_code_filter_f(p_person_id=> tts.person_id , p_start_date=>tts.effective_date , p_end_date=> tts.effective_date , p_pay_code_flag=> :payCodeFlag )
            group by
                tts.person_id,
                tts.effective_date,
                tts.item_key,
                spc.pay_code_name
            order by tts.person_id, tts.effective_date, spc.pay_code_name""";


    public static String sqlSelfTimesheetPayCodeHrs= """
            select
                spc.pay_code_name,
                sum(tts.reg_hrs) timesheet_hrs
            from
                sc_tts_timesheets tts,
                sc_pay_codes      spc,
                sc_person_v per
            where per.person_id = tts.person_id
                and per.user_id= :userId
                and spc.pay_code_id = tts.pay_code_id
                and tts.effective_date between :startDate and :endDate
                and 'Y'= sc_timesheet_status_filter_f(p_person_id=> tts.person_id , p_start_date=>tts.effective_date , p_end_date=> tts.effective_date , p_filter_flag=> :filterFlag )
            group by
                spc.pay_code_name
            order by
                spc.pay_code_name""";

    public static String sqlSelfTimesheetStatusCounts= """
            select
                sc_get_tts_status(
                    tts.item_key
                )                approval_status,
                count(tts.tts_timesheet_id) timesheet_counts
            from
                sc_tts_timesheets tts,
                sc_person_v per
            where
                per.person_id = tts.person_id
                and per.user_id= :userId
                and tts.effective_date between :startDate and :endDate
                and 'Y'= sc_timesheet_pay_code_filter_f(p_person_id=> tts.person_id , p_start_date=>tts.effective_date , p_end_date=> tts.effective_date , p_pay_code_flag=> :payCodeFlag )
            group by
                sc_get_tts_status(
                    tts.item_key
                )
            order by
                sc_get_tts_status(
                    tts.item_key
                )""";

    public static String sqlLineManagerTimesheetPayCodeHrs= """
            select
                spc.pay_code_name,
                sum(tts.reg_hrs) timesheet_hrs
            from
                sc_tts_timesheets tts,
                sc_pay_codes      spc
            where
                tts.person_id in (select
                                           per.person_id
                                       from
                                           sc_person_manager pm,
                                           sc_person_v       mgr,
                                           sc_person_v       per
                                       where
                                               mgr.person_id = pm.manager_id
                                           and mgr.user_id   = :userId
                                           and per.person_id = pm.person_id
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
                                           ) >= :endDate)
                and spc.pay_code_id = tts.pay_code_id
                and tts.effective_date between :startDate and :endDate
                and 'Y'= sc_timesheet_status_filter_f(p_person_id=> tts.person_id , p_start_date=>tts.effective_date , p_end_date=> tts.effective_date , p_filter_flag=> :filterFlag )
                and 'Y'= sc_timesheet_pay_code_filter_f(p_person_id=> tts.person_id , p_start_date=>tts.effective_date , p_end_date=> tts.effective_date , p_pay_code_flag=> :payCodeFlag )
            group by
                spc.pay_code_name
            order by
                spc.pay_code_name""";

    public static String sqlLineManagerTimesheetStatusCounts= """
            select
                sc_get_tts_status(
                    tts.item_key
                )                approval_status,
                count(tts.tts_timesheet_id) timesheet_counts
            from
                sc_tts_timesheets tts
            where
                tts.person_id in (select
                                           per.person_id
                                       from
                                           sc_person_manager pm,
                                           sc_person_v       mgr,
                                           sc_person_v       per
                                       where
                                               mgr.person_id = pm.manager_id
                                           and mgr.user_id   = :userId
                                           and per.person_id = pm.person_id
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
                                           ) >= :endDate)
                and tts.effective_date between :startDate and :endDate
                and 'Y'= sc_timesheet_status_filter_f(p_person_id=> tts.person_id , p_start_date=>tts.effective_date , p_end_date=> tts.effective_date , p_filter_flag=> :filterFlag )
                and 'Y'= sc_timesheet_pay_code_filter_f(p_person_id=> tts.person_id , p_start_date=>tts.effective_date , p_end_date=> tts.effective_date , p_pay_code_flag=> :payCodeFlag )
            group by
                sc_get_tts_status(
                    tts.item_key
                )
            order by
                sc_get_tts_status(
                    tts.item_key
                )""";

    public static String sqlTimesheetPayCodeHrs= """
            select
                spc.pay_code_name,
                sum(tts.reg_hrs) timesheet_hrs
            from
                sc_tts_timesheets tts,
                sc_pay_codes      spc
            where
                tts.person_id in (
                    select
                        p.person_id
                    from
                        sc_timekeeper_person_v p
                    where
                            p.timekeeper_user_id = :userId
                        and p.profile_id = :profileId
                        and ( lower(
                            p.employee_number
                        ) like lower(
                            :text
                        )
                              or lower(
                            p.person_name
                        ) like lower(
                            :text
                        ) )
                        and nvl(
                            p.hire_date,
                            :startDate
                        ) <= :startDate
                        and nvl(
                            p.termination_date,
                            :endDate
                        ) >= :endDate
                )
                and spc.pay_code_id = tts.pay_code_id
                and tts.effective_date between :startDate and :endDate
                and 'Y'= sc_timesheet_status_filter_f(p_person_id=> tts.person_id , p_start_date=>tts.effective_date , p_end_date=> tts.effective_date , p_filter_flag=> :filterFlag )
            group by
                spc.pay_code_name
            order by
                spc.pay_code_name""";

    public static String sqlTimesheetStatusCounts= """
            select
                sc_get_tts_status(
                    tts.item_key
                )                approval_status,
                count(tts.tts_timesheet_id) timesheet_counts
            from
                sc_tts_timesheets tts
            where
                tts.person_id in (
                    select
                        p.person_id
                    from
                        sc_timekeeper_person_v p
                    where
                            p.timekeeper_user_id = :userId
                        and p.profile_id = :profileId
                        and ( lower(
                            p.employee_number
                        ) like lower(
                            :text
                        )
                              or lower(
                            p.person_name
                        ) like lower(
                            :text
                        ) )
                        and nvl(
                            p.hire_date,
                            :startDate
                        ) <= :startDate
                        and nvl(
                            p.termination_date,
                            :endDate
                        ) >= :endDate
                )
                and tts.effective_date between :startDate and :endDate
                and 'Y'= sc_timesheet_pay_code_filter_f(p_person_id=> tts.person_id , p_start_date=>tts.effective_date , p_end_date=> tts.effective_date , p_pay_code_flag=> :payCodeFlag )
            group by
                sc_get_tts_status(
                    tts.item_key
                )
            order by
                sc_get_tts_status(
                    tts.item_key
                )""";

}
