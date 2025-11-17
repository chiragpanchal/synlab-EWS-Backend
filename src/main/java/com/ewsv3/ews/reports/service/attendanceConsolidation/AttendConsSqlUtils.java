package com.ewsv3.ews.reports.service.attendanceConsolidation;

public class AttendConsSqlUtils {


    public static String getPersonDirectReportees= """
            select
                person_id,
                employee_number,
                person_name,
                job_title,
                department_name,
                grade_name
            from
                (
                    select
                        per.person_id,
                        per.employee_number,
                        per.full_name person_name,
                        per.job_title,
                        per.department_name,
                        per.grade_name
                    from
                        sc_person_manager spm,
                        sc_person_v mgr,
                        sc_person_v per
                    where
                            mgr.person_id= spm.manager_id
                        and mgr.user_id=:user_id
                        and spm.person_id=per.person_id
                        and nvl(
                            per.hire_date,
                            :startDate
                        ) <= :startDate
                        and nvl(
                            per.termination_date,
                            :endDate
                        ) >= :endDate
                    order by
                        per.full_name,
                        per.employee_number
                )
            offset :offset * :pageSize rows
            fetch next :pageSize rows only""";

    public static String getPersonAllReportees= """
            select
                person_id,
                employee_number,
                person_name,
                job_title,
                department_name,
                grade_name
            from
                (
                    select
                        level         as hierarchy_level,
                        per.person_id,
                        per.employee_number,
                        per.full_name person_name,
                        per.job_title,
                        per.department_name,
                        per.grade_name,
                        pm.manager_id,
                        pm.manager_type_id
                    from
                        sc_person_manager pm,
                        sc_person_v       mgr,
                        sc_person_v       per
                    where
                            mgr.person_id = pm.person_id
                        and per.person_id = pm.person_id
                        and nvl(
                            per.hire_date,
                            :startDate
                        ) <= :startDate
                        and nvl(
                            per.termination_date,
                            :endDate
                        ) >= :endDate
                    start with
                        mgr.user_id = :user_id
                    connect by
                        prior pm.person_id = pm.manager_id
                    order siblings by
                        pm.person_id
                )
            offset :offset * :pageSize rows
            fetch next :pageSize rows only""";

    public static String getPersonsProfile = """
            select
                person_id,
                employee_number,
                person_name,
                job_title,
                department_name,
                grade_name
            from
                (
                    select
                        tkv.person_id,
                        tkv.employee_number,
                        tkv.person_name,
                        tkv.job_title,
                        tkv.department_name,
                        tkv.grade_name
                    from
                        sc_timekeeper_person_v tkv
                    where
                            tkv.timekeeper_user_id = :userId
                        and tkv.profile_id = :profileId
                        and nvl(
                            tkv.hire_date,
                            :startDate
                        ) <= :startDate
                        and nvl(
                            tkv.termination_date,
                            :endDate
                        ) >= :endDate
                    order by
                        tkv.person_name,
                        tkv.employee_number
                )
            offset :offset * :pageSize rows
            fetch next :pageSize rows only""";

    public static String getSchViolations = """
            select
                st.person_id,
                st.effective_date,
                round(
                    decode(
                        st.work_duration_code,
                        'OFF',
                        0,
                        sum(st.sch_hrs)
                    ),
                    2
                ) sch_hrs,
                listagg(st.violation_code,
                        ', ') within group(
                order by
                    st.violation_code
                ) violation_code
            from
                sc_timecards st,
                sc_person_v  per
            where
                    per.person_id = st.person_id
                and st.person_id in (:personIds)
                and st.effective_date between :startDate and :endDate
                and st.primary_row = 'Y'
                and nvl(
                    per.hire_date,
                    :startDate
                ) <= :startDate
                and nvl(
                    per.termination_date,
                    :endDate
                ) >= :endDate
            group by
                st.person_id,
                st.effective_date,
                st.work_duration_code
            order by
                st.person_id,
                st.effective_date""";

    public static String getActuals = """
            select
                st.person_id,
                st.effective_date,
                round(sum(st.act_hrs),2) act_hrs
            from
                sc_timecards           st,
                sc_person_v per
            where  per.person_id=st.person_id
                and st.person_id in (:personIds)
                and st.effective_date between :startDate and :endDate
                and st.primary_row = 'N'
                and nvl(st.act_hrs(+),0) <>0
                and nvl(st.absence_attendances_id(+),0) =0
                and nvl(st.holiday_id(+),0) =0
                and nvl(
                    per.hire_date,
                    :startDate
                ) <= :startDate
                and nvl(
                    per.termination_date,
                    :endDate
                ) >= :endDate
            group by
                st.person_id,
                st.effective_date
            order by
                st.person_id,
                st.effective_date""";

    public static String getLeaves = """
            select
                lv.person_id,
                lv.leave_date effective_date,
                listagg(lv.absence_name,', ') within group (order by lv.absence_name)  absence_name
            from
                sc_person_absences_t   lv,
                sc_person_v per
            where  per.person_id=lv.person_id
                and per.person_id in (:personIds)
                and lv.leave_date between :startDate and :endDate
                and nvl(
                    per.hire_date,
                    :startDate
                ) <= :startDate
                and nvl(
                    per.termination_date,
                    :endDate
                ) >= :endDate
            group by
                lv.person_id,
                lv.leave_date
            order by
                lv.person_id,
                lv.leave_date""";

    public static String getHolidays = """
            select
                hol.person_id,
                hol.holiday_date effective_date,
                listagg(hol.holiday_name,', ') within group (order by hol.holiday_name)  holioday_name
            from
                sc_person_holidays   hol,
                sc_person_v per
            where  per.person_id=hol.person_id
                and per.person_id in (:personIds)
                and hol.holiday_date between :startDate and :endDate
                and nvl(
                    per.hire_date,
                    :startDate
                ) <= :startDate
                and nvl(
                    per.termination_date,
                    :endDate
                ) >= :endDate
            group by
                hol.person_id,
                hol.holiday_date
            order by
                hol.person_id,
                hol.holiday_date""";
}



