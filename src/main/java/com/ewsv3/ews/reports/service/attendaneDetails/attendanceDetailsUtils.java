package com.ewsv3.ews.reports.service.attendaneDetails;

public class attendanceDetailsUtils {

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
                    SELECT distinct
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
                        round(st_a.act_hrs,2)act_hrs,
                        st_lv.time_type leave_name
                      FROM
                        sc_timekeeper_person_v tkv,
                        sc_person_rosters      spr,
                        sc_departments         sd,
                        sc_jobs                sj,
                        sc_work_locations      loc,
                        sc_timecards st,
                        (select st2.person_id, st2.effective_date, st2.in_time, st2.out_time, st2.act_hrs, st2.person_roster_id from sc_timecards st2
                        where st2.effective_date between :startDate AND :endDate
                        and st2.primary_row='N'
                        and st2.in_time is not null
                        and st2.time_type is null)st_a,
                        (select st2.person_id, st2.effective_date, st2.time_type from sc_timecards st2
                        where st2.effective_date between :startDate AND :endDate
                        and st2.primary_row='N'
                        and nvl(st2.ABSENCE_ATTENDANCES_ID,0)<>0
                        and st2.time_type is not null)st_lv
                     WHERE
                            tkv.timekeeper_user_id = :userId
                           AND tkv.profile_id           = :profileId
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
                           AND spr.person_id (+)        = tkv.person_id
                           AND spr.effective_date (+) BETWEEN :startDate AND :endDate
                           AND sd.department_id (+)     = spr.department_id
                           AND sj.job_title_id (+)      = spr.job_title_id
                           AND loc.work_location_id (+) = spr.work_location_id
                           AND (:departmentId = 0 or sd.department_id = :departmentId)
                           AND (:jobTitleId = 0 or sj.job_title_id = :jobTitleId )
                           and st.person_id= tkv.person_id
                           and st.effective_date between :startDate AND :endDate
                           and st.primary_row='Y'
                           and st_a.person_id(+)= st.person_id
                           and st_a.effective_date(+)= st.effective_date
                           and st_a.person_roster_id(+)= st.person_roster_id
                           and st_lv.person_id(+)= st.person_id
                           and st_lv.effective_date(+)= st.effective_date
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
}
