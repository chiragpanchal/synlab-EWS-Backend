package com.ewsv3.ews.team.service;

public class TeamUtilsPaginationSql {
    
    
    //    Pagination Team SQLs
        static String TeamSql = """
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
                    tkv.band
                FROM
                    sc_timekeeper_person_v tkv
                WHERE
                        tkv.timekeeper_user_id = :userId
                    AND tkv.profile_id = :profileId
                    AND nvl(tkv.hire_date, :startDate) <= :startDate
                    AND nvl(tkv.termination_date, :endDate) >= :endDate
                    ORDER BY
                        tkv.person_name,
                        tkv.employee_number
                    OFFSET nvl(
                        :pageNo - 1,
                        1
                    ) * :pageSize ROWS FETCH NEXT :pageSize ROWS ONLY""";
    
        static String MemberTimecardLineSql = """
                SELECT
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
                    st.violation_code
                FROM
                    sc_timecards      st,
                    sc_departments    sd,
                    sc_jobs           sj,
                    sc_work_locations sl
                WHERE
                        st.person_id = :person_id
                    AND effective_date BETWEEN :start_date AND :end_date
                    AND sd.department_id (+) = st.COST_CENTER_ID
                    AND sj.job_title_id (+) = st.job_title_id
                    AND sl.work_location_id (+) = st.work_location_id
                    AND nvl(st.primary_row,'N') = 'Y'
                ORDER BY
                    st.effective_date, st.sch_time_start, st.in_time""";
    
        static String MemberTimecardActualsSql = """
                SELECT
                    st.timecard_id,
                    st.person_roster_id,
                    st.effective_date,
                    st.in_time,
                    st.out_time,
                    st.act_hrs,
                    st.time_type
                  FROM
                    sc_timecards st
                 WHERE
                        st.person_id = :person_id
                       AND nvl(
                        st.person_roster_id,
                        0
                    ) = nvl(
                        :person_roster_id,
                        nvl(
                                st.person_roster_id,
                                0
                            )
                    )
                       AND effective_date              = :effective_date
                       AND ( st.in_time IS NOT NULL
                        OR st.time_type IS NOT NULL )
                       AND nvl(
                        st.primary_row,
                        'N'
                    )    = 'N'
                 ORDER BY
                    st.effective_date,
                    st.in_time,
                    nvl(
                        st.act_hrs,
                        99
                    )""";
    }