package com.ewsv3.ews.reports.service.attendaneDetails;

public class AttendanceDetailsUtils {


    public static String getAttendanceDetailsSQL= """
            SELECT
                person_id,
                employee_number,
                person_name,
                job_title,
                department_name,
                location_name,
                grade_name,
                effective_date,
                sch_time_start,
                sch_time_end,
                sch_hrs,
                violation_code,
                occurences,
                in_time,
                out_time,
                act_hrs,
                leave_name
              FROM
                (
                    SELECT DISTINCT
                        per.person_id,
                        per.employee_number,
                        per.full_name person_name,
                        sj.job_title,
                        sd.department_name,
                        per.grade_name,
                        loc.location_name,
                        st.effective_date,
                        st.sch_time_start,
                        st.sch_time_end,
                        st.sch_hrs,
                        st.violation_code,
                        st.occurences,
                        st_a.in_time,
                        st_a.out_time,
                        round(
                            st_a.act_hrs,
                            2
                        )               act_hrs,
                        st_lv.time_type leave_name
                      FROM
                        sc_person_v per,
                        sc_departments         sd,
                        sc_jobs                sj,
                        sc_work_locations      loc,
                        sc_timecards           st,
                        (
                            SELECT
                                st2.person_id,
                                st2.effective_date,
                                st2.in_time,
                                st2.out_time,
                                st2.act_hrs,
                                st2.person_roster_id
                              FROM
                                sc_timecards st2
                             WHERE
                                st2.effective_date BETWEEN :startDate AND :endDate
                                   AND st2.primary_row = 'N'
                                   AND st2.in_time IS NOT NULL
                                   AND st2.time_type IS NULL
                        )                      st_a,
                        (
                            SELECT
                                st2.person_id,
                                st2.effective_date,
                                st2.time_type
                              FROM
                                sc_timecards st2
                             WHERE
                                st2.effective_date BETWEEN :startDate AND :endDate
                                   AND st2.primary_row = 'N'
                                   AND nvl(
                                    st2.absence_attendances_id,
                                    0
                                ) <> 0
                                   AND st2.time_type IS NOT NULL
                        )                      st_lv
                     WHERE
                           per.person_id in ( :personIds )
                           AND st.person_id              = per.person_id
                           AND st.effective_date BETWEEN :startDate AND :endDate
                           AND st.primary_row            = 'Y'
                           AND sd.department_id (+)      = st.cost_center_id
                           AND sj.job_title_id (+)       = st.job_title_id
                           AND loc.work_location_id (+)  = st.work_location_id
                           AND ( :departmentId = 0
                            OR sd.department_id           = :departmentId )
                           AND ( :jobTitleId = 0
                            OR sj.job_title_id            = :jobTitleId )
                           AND st_a.person_id (+)        = st.person_id
                           AND st_a.effective_date (+)   = st.effective_date
                           AND st_a.person_roster_id (+) = st.person_roster_id
                           AND st_lv.person_id (+)       = st.person_id
                           AND st_lv.effective_date (+)  = st.effective_date
                     ORDER BY
                        person_name
                )""";

    public static String XX_getAttendanceDetailsTKSQL= """
            SELECT
                person_id,
                employee_number,
                person_name,
                job_title,
                department_name,
                location_name,
                grade_name,
                effective_date,
                sch_time_start,
                sch_time_end,
                sch_hrs,
                violation_code,
                occurences,
                in_time,
                out_time,
                act_hrs,
                leave_name
              FROM
                (
                    SELECT DISTINCT
                        tkv.person_id,
                        tkv.employee_number,
                        tkv.person_name,
            --                        tkv.job_title,
                        sj.job_title,
            --                        tkv.department_name,
                        sd.department_name,
                        tkv.grade_name,
            --                        tkv.location_name
                        loc.location_name,
                        st.effective_date,
                        st.sch_time_start,
                        st.sch_time_end,
                        st.sch_hrs,
                        st.violation_code,
                        st.occurences,
                        st_a.in_time,
                        st_a.out_time,
                        round(
                            st_a.act_hrs,
                            2
                        )               act_hrs,
                        st_lv.time_type leave_name
                      FROM
                        sc_timekeeper_person_v tkv,
                        sc_departments         sd,
                        sc_jobs                sj,
                        sc_work_locations      loc,
                        sc_timecards           st,
                        (
                            SELECT
                                st2.person_id,
                                st2.effective_date,
                                st2.in_time,
                                st2.out_time,
                                st2.act_hrs,
                                st2.person_roster_id
                              FROM
                                sc_timecards st2
                             WHERE
                                st2.effective_date BETWEEN :startDate AND :endDate
                                   AND st2.primary_row = 'N'
                                   AND st2.in_time IS NOT NULL
                                   AND st2.time_type IS NULL
                        )                      st_a,
                        (
                            SELECT
                                st2.person_id,
                                st2.effective_date,
                                st2.time_type
                              FROM
                                sc_timecards st2
                             WHERE
                                st2.effective_date BETWEEN :startDate AND :endDate
                                   AND st2.primary_row = 'N'
                                   AND nvl(
                                    st2.absence_attendances_id,
                                    0
                                ) <> 0
                                   AND st2.time_type IS NOT NULL
                        )                      st_lv
                     WHERE
                            tkv.timekeeper_user_id = :userId
                           AND tkv.profile_id            = :profileId
                           AND st.person_id              = tkv.person_id
                           AND st.effective_date BETWEEN :startDate AND :endDate
                           AND st.primary_row            = 'Y'
                           AND ( lower(
                            tkv.employee_number
                        ) LIKE lower(
                            :text
                        )
                            OR lower(
                            tkv.person_name
                        ) LIKE lower(
                            :text
                        ) )
                           AND sd.department_id (+)      = st.cost_center_id
                           AND sj.job_title_id (+)       = st.job_title_id
                           AND loc.work_location_id (+)  = st.work_location_id
                           AND ( :departmentId = 0
                            OR sd.department_id           = :departmentId )
                           AND ( :jobTitleId = 0
                            OR sj.job_title_id            = :jobTitleId )
                           AND st_a.person_id (+)        = st.person_id
                           AND st_a.effective_date (+)   = st.effective_date
                           AND st_a.person_roster_id (+) = st.person_roster_id
                           AND st_lv.person_id (+)       = st.person_id
                           AND st_lv.effective_date (+)  = st.effective_date
                           AND nvl(
                            tkv.hire_date,
                            :startDate
                        ) <= :startDate
                           AND nvl(
                            tkv.termination_date,
                            :endDate
                        ) >= :endDate
                     ORDER BY
                        person_name
                )
            OFFSET :offset * :pageSize ROWS
             FETCH NEXT :pageSize ROWS ONLY""";

    public static String XX_getAttendanceDetailsDirectSQL= """
            SELECT
                person_id,
                employee_number,
                person_name,
                job_title,
                department_name,
                location_name,
                grade_name,
                effective_date,
                sch_time_start,
                sch_time_end,
                sch_hrs,
                violation_code,
                occurences,
                in_time,
                out_time,
                act_hrs,
                leave_name
              FROM
                (
                    SELECT DISTINCT
                        per.person_id,
                        per.employee_number,
                        per.full_name person_name,
            --                        tkv.job_title,
                        sj.job_title,
            --                        tkv.department_name,
                        sd.department_name,
                        per.grade_name,
            --                        tkv.location_name
                        loc.location_name,
                        st.effective_date,
                        st.sch_time_start,
                        st.sch_time_end,
                        st.sch_hrs,
                        st.violation_code,
                        st.occurences,
                        st_a.in_time,
                        st_a.out_time,
                        round(
                            st_a.act_hrs,
                            2
                        )               act_hrs,
                        st_lv.time_type leave_name
                      FROM
                        sc_person_v per,
                        sc_departments         sd,
                        sc_jobs                sj,
                        sc_work_locations      loc,
                        sc_timecards           st,
                        (
                            SELECT
                                st2.person_id,
                                st2.effective_date,
                                st2.in_time,
                                st2.out_time,
                                st2.act_hrs,
                                st2.person_roster_id
                              FROM
                                sc_timecards st2
                             WHERE
                                st2.effective_date BETWEEN :startDate AND :endDate
                                   AND st2.primary_row = 'N'
                                   AND st2.in_time IS NOT NULL
                                   AND st2.time_type IS NULL
                        )                      st_a,
                        (
                            SELECT
                                st2.person_id,
                                st2.effective_date,
                                st2.time_type
                              FROM
                                sc_timecards st2
                             WHERE
                                st2.effective_date BETWEEN :startDate AND :endDate
                                   AND st2.primary_row = 'N'
                                   AND nvl(
                                    st2.absence_attendances_id,
                                    0
                                ) <> 0
                                   AND st2.time_type IS NOT NULL
                        )                      st_lv
                     WHERE\s
                        EXISTS (
                            SELECT
                                'Y'
                              FROM
                                sc_person_manager spm
                             WHERE
                                    spm.manager_id = :personId
                                   AND spm.person_id = per.person_id
                        )  AND st.person_id              = per.person_id
                           AND st.effective_date BETWEEN :startDate AND :endDate
                           AND st.primary_row            = 'Y'
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
                           AND sd.department_id (+)      = st.cost_center_id
                           AND sj.job_title_id (+)       = st.job_title_id
                           AND loc.work_location_id (+)  = st.work_location_id
                           AND ( :departmentId = 0
                            OR sd.department_id           = :departmentId )
                           AND ( :jobTitleId = 0
                            OR sj.job_title_id            = :jobTitleId )
                           AND st_a.person_id (+)        = st.person_id
                           AND st_a.effective_date (+)   = st.effective_date
                           AND st_a.person_roster_id (+) = st.person_roster_id
                           AND st_lv.person_id (+)       = st.person_id
                           AND st_lv.effective_date (+)  = st.effective_date
                           AND nvl(
                            per.hire_date,
                            :startDate
                        ) <= :startDate
                           AND nvl(
                            per.termination_date,
                            :endDate
                        ) >= :endDate
                     ORDER BY
                        person_name
                )
            OFFSET :offset * :pageSize ROWS
             FETCH NEXT :pageSize ROWS ONLY""";

    public static String XX_getAttendanceDetailsAllSQL= """
            SELECT
                person_id,
                employee_number,
                person_name,
                job_title,
                department_name,
                location_name,
                grade_name,
                effective_date,
                sch_time_start,
                sch_time_end,
                sch_hrs,
                violation_code,
                occurences,
                in_time,
                out_time,
                act_hrs,
                leave_name
              FROM
                (
                    SELECT DISTINCT
                        per.person_id,
                        per.employee_number,
                        per.full_name person_name,
            --                        tkv.job_title,
                        sj.job_title,
            --                        tkv.department_name,
                        sd.department_name,
                        per.grade_name,
            --                        tkv.location_name
                        loc.location_name,
                        st.effective_date,
                        st.sch_time_start,
                        st.sch_time_end,
                        st.sch_hrs,
                        st.violation_code,
                        st.occurences,
                        st_a.in_time,
                        st_a.out_time,
                        round(
                            st_a.act_hrs,
                            2
                        )               act_hrs,
                        st_lv.time_type leave_name
                      FROM
                        sc_person_v per,
                        sc_departments         sd,
                        sc_jobs                sj,
                        sc_work_locations      loc,
                        sc_timecards           st,
                        (
                            SELECT
                                st2.person_id,
                                st2.effective_date,
                                st2.in_time,
                                st2.out_time,
                                st2.act_hrs,
                                st2.person_roster_id
                              FROM
                                sc_timecards st2
                             WHERE
                                st2.effective_date BETWEEN :startDate AND :endDate
                                   AND st2.primary_row = 'N'
                                   AND st2.in_time IS NOT NULL
                                   AND st2.time_type IS NULL
                        )                      st_a,
                        (
                            SELECT
                                st2.person_id,
                                st2.effective_date,
                                st2.time_type
                              FROM
                                sc_timecards st2
                             WHERE
                                st2.effective_date BETWEEN :startDate AND :endDate
                                   AND st2.primary_row = 'N'
                                   AND nvl(
                                    st2.absence_attendances_id,
                                    0
                                ) <> 0
                                   AND st2.time_type IS NOT NULL
                        )                      st_lv
                     WHERE
                        EXISTS (
                             SELECT
                                 'Y'
                               FROM
                                 sc_person_manager spm
                              WHERE spm.person_id = per.person_id
                              START WITH spm.manager_id = :personId
                              CONNECT BY PRIOR spm.manager_id= spm.person_id
    
                         )  AND st.person_id              = per.person_id
                           AND st.effective_date BETWEEN :startDate AND :endDate
                           AND st.primary_row            = 'Y'
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
                           AND sd.department_id (+)      = st.cost_center_id
                           AND sj.job_title_id (+)       = st.job_title_id
                           AND loc.work_location_id (+)  = st.work_location_id
                           AND ( :departmentId = 0
                            OR sd.department_id           = :departmentId )
                           AND ( :jobTitleId = 0
                            OR sj.job_title_id            = :jobTitleId )
                           AND st_a.person_id (+)        = st.person_id
                           AND st_a.effective_date (+)   = st.effective_date
                           AND st_a.person_roster_id (+) = st.person_roster_id
                           AND st_lv.person_id (+)       = st.person_id
                           AND st_lv.effective_date (+)  = st.effective_date
                           AND nvl(
                            per.hire_date,
                            :startDate
                        ) <= :startDate
                           AND nvl(
                            per.termination_date,
                            :endDate
                        ) >= :endDate
                     ORDER BY
                        person_name
                )
            OFFSET :offset * :pageSize ROWS
             FETCH NEXT :pageSize ROWS ONLY""";
}
