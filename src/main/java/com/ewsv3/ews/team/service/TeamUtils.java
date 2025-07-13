package com.ewsv3.ews.team.service;

public class TeamUtils {

    static String TramSimpleSql = """
            SELECT
                person_id,
                employee_number,
                person_Name,
                job_title,
                department_name
              FROM
                (
                    SELECT
                        tkv.person_id,
                        tkv.employee_number,
                        tkv.person_Name,
                        tkv.job_title,
                        tkv.department_name
                      FROM
                        sc_timekeeper_person_v tkv
                     WHERE
                            tkv.timekeeper_user_id = :userId
                           AND tkv.profile_id=:profileId
                           AND (lower(tkv.employee_number) LIKE lower(:text)
                           OR lower(tkv.person_name) LIKE lower(:text))
                           AND 'Y' = sc_team_timecards_filter_f(tkv.person_id, :startDate,  :endDate, :pFilterFlag)
                           AND nvl(
                            tkv.hire_date,
                            :startDate
                        ) <= :startDate
                           AND nvl(
                            tkv.termination_date,
                            :endDate
                        ) >= :endDate
                     ORDER BY
                        person_Name
                )
            OFFSET :offset*:pageSize ROWS
             FETCH NEXT :pageSize ROWS ONLY""";

    static String TeamSimpleChildSql = """
            SELECT
                effective_date,
                work_duration_code,
                SUM(sch_hrs)                  sch_hrs,
                SUM(act_hrs)                  act_hrs,
                COUNT(absence_attendances_id) leave_count,
                COUNT(violation_code)         violation_count
              FROM
                (
                    SELECT
                        st.effective_date,
                        decode(
                            instr(
                                nvl(st.work_duration_code,'X'),
                                'OFF'
                            ),
                            0,
                            st.sch_hrs,
                            0
                        ) sch_hrs,
                        decode(
                            st.absence_attendances_id,
                            NULL,
                            st.act_hrs,
                            0
                        ) act_hrs,
                        st.absence_attendances_id,
                        st.violation_code,
                        st.work_duration_code
                      FROM
                        sc_timecards st
                     WHERE
                            st.person_id = :personId
                           AND st.effective_date BETWEEN :startDate AND  :endDate
                           AND 'Y' = sc_team_timecards_filter_f(st.person_id, st.effective_date,  st.effective_date, :pFilterFlag)
                )
             GROUP BY
                effective_date,
                work_duration_code
             ORDER BY
                effective_date""";

    static String TeamAllSql = """
            SELECT
                tkv.person_id,
                tkv.person_user_id,
                tkv.employee_number,
                tkv.person_name,
                tkv.email_address,
                tkv.department_name,
                tkv.position_name,
                tkv.job_title,
                tkv.location_name,
                tkv.shift_type,
                tkv.band,
                tkv.department_id,
                tkv.job_title_id,
                tkv.work_location_id,
                tkv.business_unit_id,
                tkv.legal_entity_id,
                tkv.employee_type_id,
                tkv.job_family,
                per.citizenship_lookup,
                per.gender_lookup,
                per.religion_lookup,
                per.grade_id
              FROM
                sc_timekeeper_person_v tkv,
                sc_person_v            per
             WHERE
                    tkv.timekeeper_user_id = :userId
                   AND tkv.profile_id = :profileId
                   AND nvl(
                    tkv.hire_date,
                    :startDate
                ) <= :startDate
                   AND nvl(
                    tkv.termination_date,
                    :endDate
                ) >= :endDate
                   AND per.person_id  = tkv.person_id""";

    static String MemberAllTimecardLineSql = """
            SELECT
                st.person_id,
                st.timecard_id,
                st.person_roster_id,
                st.effective_date,
                st.job_title_id,
                sj.job_title,
                sd.department_id,
                sd.department_name,
                st.work_location_id,
                sl.location_name,
                st.on_call,
                st.emergency,
                null project,
                null task,
                st.sch_time_start,
                st.sch_time_end,
                st.sch_hrs,
                st.in_time,
                st.out_time,
                st.act_hrs,
                st.time_type,
                nvl(st.primary_row,'N') primary_row,
                st.violation_code,
                swd.work_duration_code
            FROM
                sc_timecards      st,
                sc_departments    sd,
                sc_jobs           sj,
                sc_work_locations sl,
                sc_timekeeper_person_v tkv,
                sc_person_rosters spr,
                sc_work_duration  swd
            WHERE  tkv.timekeeper_user_id = :userId
                AND tkv.profile_id = :profileId
                AND st.person_id = tkv.person_id
                AND st.effective_date BETWEEN :startDate AND :endDate
                AND sd.department_id (+) = st.COST_CENTER_ID
                AND sj.job_title_id (+) = st.job_title_id
                AND sl.work_location_id (+) = st.work_location_id
                AND nvl(st.primary_row,'N') = 'Y'
                AND spr.person_roster_id (+) = st.person_roster_id
                AND swd.work_duration_id (+) = spr.work_duration_id
            """;

    static String MemberAllTimecardActualsSql =
            """
                        SELECT
                        st.person_id,
                        st.timecard_id,
                        st.person_roster_id,
                        st.effective_date,
                        st.in_time,
                        st.out_time,
                        st.act_hrs,
                        st.time_type,
                        st.absence_attendances_id,
                        st.holiday_id
                    FROM
                        sc_timecards      st,
                        sc_timekeeper_person_v tkv
                    WHERE tkv.timekeeper_user_id = :userId
                        AND tkv.profile_id = :profileId
                        AND st.person_id = tkv.person_id
                        AND effective_date BETWEEN :startDate AND :endDate
                        AND ( st.in_time IS NOT NULL
                              OR st.time_type IS NOT NULL )
                        AND nvl(
                            st.primary_row,
                            'N'
                        ) = 'N'""";

    static String TeamTimecardOneSql = """
            SELECT
                spr.person_id,
                spr.effective_date,
                spr.department_id ,
                spr.job_title_id,
                spr.work_location_id,
                sj.job_title,
                sd.department_name,
                wl.location_name,
                spr.on_call,
                spr.emergency,
                spr.work_duration_id,
                swd.work_duration_code,
                sum(st.sch_hrs) sch_hrs,
                sum(st.act_hrs) act_hrs,
                count(st.ABSENCE_ATTENDANCES_ID) leave_counts,
                count(st.holiday_id) holiday_counts,
                count(distinct st.violation_code) violation_counts
            FROM
                sc_timekeeper_person_v tkv,
                sc_person_rosters      spr,
                sc_work_duration swd,
                sc_timecards st,
                sc_jobs sj,
                sc_departments sd,
                sc_work_locations wl
            WHERE tkv.timekeeper_user_id = :userId
                AND tkv.profile_id = :profileId
                AND spr.person_id = tkv.person_id\s
                AND spr.effective_date BETWEEN :startDate AND :endDate
                and sj.job_title_id(+)=spr.job_title_id
                and sd.department_id(+)=spr.department_id
                and wl.work_location_id(+)=spr.work_location_id
                AND swd.work_duration_id=spr.work_duration_id
                and st.person_id=spr.person_id
                and st.effective_date=spr.effective_date
            --    and nvl(st.primary_row,'N')='Y'
                and st.effective_date= spr.effective_date
                and nvl(st.person_roster_id(+),0)= spr.person_roster_id
            --    AND spr.person_id = 103605
                group by spr.person_id,
                spr.effective_date,
                spr.department_id ,
                spr.job_title_id,
                spr.work_location_id,
                sj.job_title,
                sd.department_name,
                wl.location_name,
                spr.on_call,
                spr.emergency,
                spr.work_duration_id,
                swd.work_duration_code
            order by spr.person_id, spr.effective_date""";

    static String TeamTimecardKpiSql = """
            SELECT
                COUNT(DISTINCT person_id) person_counts,
                SUM(sch_hrs)              sch_hrs,
                SUM(act_hrs)              act_hrs,
                SUM(leave_hrs)            leave_hrs,
                SUM(holiday_hrs)          holiday_hrs,
                SUM(violation_counts)     violation_counts
              FROM
                (
                    SELECT
                        tkv.person_id,
                        (
                            SELECT
                                SUM((spr.time_end - spr.time_start) * 24)
                              FROM
                                sc_person_rosters spr,
                                sc_work_duration  swd
                             WHERE
                                    spr.person_id = tkv.person_id
                                   AND spr.effective_date BETWEEN :startDate AND :endDate
                                   AND swd.work_duration_id    = spr.work_duration_id
                                   AND swd.work_duration_code <> 'OFF'
                                   AND nvl(
                                    spr.published,
                                    'N'
                                ) = 'Y'
                        ) sch_hrs,
                        (
                            SELECT
                                SUM(act_hrs)
                              FROM
                                sc_timecards st2
                             WHERE
                                    st2.person_id = tkv.person_id
                                   AND st2.effective_date BETWEEN :startDate AND :endDate
                                   AND st2.time_type IS NULL
                        ) act_hrs,
                        (
                            SELECT
                                SUM(act_hrs)
                              FROM
                                sc_timecards st2
                             WHERE
                                    st2.person_id = tkv.person_id
                                   AND st2.effective_date BETWEEN :startDate AND :endDate
                                   AND st2.absence_attendances_id IS NOT NULL
                        ) leave_hrs,
                        (
                            SELECT
                                SUM(act_hrs)
                              FROM
                                sc_timecards st2
                             WHERE
                                    st2.person_id = tkv.person_id
                                   AND st2.effective_date BETWEEN :startDate AND :endDate
                                   AND st2.holiday_id IS NOT NULL
                        ) holiday_hrs,
                        (
                            SELECT
                                COUNT(DISTINCT violation_code)
                              FROM
                                sc_timecards st2
                             WHERE
                                    st2.person_id = tkv.person_id
                                   AND st2.effective_date BETWEEN :startDate AND :endDate
                                   AND st2.violation_code IS NOT NULL
                                   AND nvl(
                                    st2.primary_row,
                                    'N'
                                ) = 'Y'
                        ) violation_counts
                      FROM
                        sc_timekeeper_person_v tkv
                     WHERE
                            tkv.timekeeper_user_id = :userId
                           AND tkv.profile_id = :profileId
                )""";


}
