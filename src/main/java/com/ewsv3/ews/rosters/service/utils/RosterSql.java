package com.ewsv3.ews.rosters.service.utils;

public class RosterSql {
    public static String RosterTeamSql = """
            SELECT
                            person_id,
                            assignment_id,
                            employee_number,
                            person_Name,
                            job_title,
                            department_name
                          FROM
                            (
                                SELECT
                                    tkv.person_id,
                                    per.assignment_id,
                                    tkv.employee_number,
                                    tkv.person_Name,
                                    tkv.job_title,
                                    tkv.department_name
                                  FROM
                                    sc_timekeeper_person_v tkv,
                                    sc_person_v per
                                 WHERE
                                        tkv.timekeeper_user_id = :userId
                                       AND tkv.profile_id=:profileId
                                       AND (lower(tkv.employee_number) LIKE lower(:text)
                                       OR lower(tkv.person_name) LIKE lower(:text))
                                       AND per.person_id=tkv.person_id
                                       AND (:personId = 0 OR per.person_id = :personId)
                                       AND 'Y'= sc_person_rosters_filter_f(p_person_id =>tkv.person_id ,p_person_roster_id => null, p_start_date=> trunc(:startDate) , p_end_date=> trunc(:endDate) ,p_filter_flag=>:pFilterFlag)
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
                        OFFSET nvl(:offset ,1) * :pageSize ROWS
                         FETCH NEXT :pageSize ROWS ONLY""";

    public static String RosterMemberChildSql = """
            SELECT
                per.person_id,
                per.assignment_id,
                spr.person_roster_id,
                spr.effective_date,
                spr.time_start,
                spr.time_end,
                ( spr.time_end - spr.time_start ) * 24 sch_hrs,
                spr.department_id                      sch_department_id,
                spr.job_title_id                       sch_job_title_id,
                spr.work_location_id                   sch_work_location_id,
                sd.department_name                     sch_department,
                sj.job_title                           sch_job_title,
                loc.location_name                      sch_location,
                spr.on_call,
                spr.emergency,
                spr.published,
                swd.work_duration_id,
                swd.work_duration_code,
                swd.work_duration_name,
                ( ( spr.time_end - spr.time_start ) * 24 * pj.per_hr_sal ) sch_cost,
                fc.currency_code
              FROM
                sc_person_rosters spr,
                sc_person_v       per,
                sc_work_duration  swd,
                sc_departments    sd,
                sc_jobs           sj,
                sc_work_locations loc,
                sc_person_preferred_jobs pj,
                sc_currencies            fc
             WHERE
                    per.person_id = spr.person_id
                   AND spr.person_id=:personId
                   AND swd.work_duration_id (+) = spr.work_duration_id
                   AND spr.effective_date BETWEEN :startDate AND :endDate
                   AND 'Y'=  sc_person_rosters_filter_f(p_person_id =>spr.person_id , p_person_roster_id => spr.person_roster_id, p_start_date=> trunc(:startDate) , p_end_date=> trunc(:endDate) ,p_filter_flag=>:pFilterFlag)
                   AND spr.department_id        = sd.department_id (+)
                   AND spr.job_title_id         = sj.job_title_id (+)
                   AND spr.work_location_id     = loc.work_location_id (+)
                   and pj.person_id (+)         = spr.person_id
                   and pj.job_title_id (+)      = spr.job_title_id
                   and fc.currency_id (+)       = pj.currency_id
             ORDER BY
                spr.effective_date,
                spr.time_start""";

    public static String getPersonRosterDataSql = """
            SELECT
                spr.person_roster_id,
                spr.effective_date,
                spr.person_id,
                spr.work_duration_id,
                spr.time_start,
                spr.break_start,
                spr.break_end,
                spr.time_end,
                spr.created_by,
                spr.created_on,
                spr.last_updated_by,
                spr.last_update_date,
                spr.department_id,
                spr.section_id,
                spr.wages,
                spr.currency_id,
                spr.comments,
                spr.job_title_id,
                spr.work_location_id,
                spr.published,
                spr.manager_id,
                spr.work_rotation_id,
                spr.project_id,
                spr.task_id,
                spr.project_address,
                spr.group_identifier_key,
                spr.person_start_time,
                spr.crew_id,
                spr.skill_1,
                spr.skill_2,
                spr.skill_3,
                spr.moving_asset_id,
                spr.per_diem,
                spr.patient_id,
                spr.appr_status,
                spr.dml_flag,
                spr.on_call,
                spr.emergency,
                spr.object_version_number,
                spr.open_shift,
                spr.skill_id,
                 swd.work_duration_code,
                swd.work_duration_name,
                swd.color_code,
                swc.category_name,
                 sd.department_name,
                 sj.job_title,
                 (spr.time_end -spr.time_start)*24 sch_hrs,
                 to_char(spr.time_start,'hh:mi am')time_start_short,
                 to_char(spr.time_end,'hh:mi am')time_end_short
             FROM
                sc_person_rosters      spr,
                sc_timekeeper_person_v tkv,
                sc_work_duration swd,
                sc_work_duration_category swc,
                sc_departments sd,
                sc_jobs sj
            WHERE
                    tkv.timekeeper_user_id = :userId
                AND spr.effective_date BETWEEN trunc(:startDate) AND trunc(:endDate)
                AND spr.person_id = tkv.person_id
                AND tkv.profile_id = :profileId
                AND swd.work_duration_id=spr.work_duration_id
                AND sd.department_id =spr.department_id
                AND sj.job_title_id=spr.job_title_id
                and swc.work_duration_category_id = swd.work_duration_category_id
                AND nvl(
                    tkv.hire_date,
                    trunc(:endDate)
                ) <= trunc(:endDate)
                AND nvl(
                    tkv.termination_date,
                    trunc(:startDate)
                ) >= trunc(:startDate)
                OFFSET 1 ROWS FETCH NEXT 1000 ROWS ONLY""";


    public static String getSinglePersonRosterDataSql = """
            SELECT
                spr.person_roster_id,
                spr.effective_date,
                spr.person_id,
                spr.work_duration_id,
                spr.time_start,
                spr.break_start,
                spr.break_end,
                spr.time_end,
                spr.created_by,
                spr.created_on,
                spr.last_updated_by,
                spr.last_update_date,
                spr.department_id,
                spr.section_id,
                spr.wages,
                spr.currency_id,
                spr.comments,
                spr.job_title_id,
                spr.work_location_id,
                spr.published,
                spr.manager_id,
                spr.work_rotation_id,
                spr.project_id,
                spr.task_id,
                spr.project_address,
                spr.group_identifier_key,
                spr.person_start_time,
                spr.crew_id,
                spr.skill_1,
                spr.skill_2,
                spr.skill_3,
                spr.moving_asset_id,
                spr.per_diem,
                spr.patient_id,
                spr.appr_status,
                spr.dml_flag,
                spr.on_call,
                spr.emergency,
                spr.object_version_number,
                spr.open_shift,
                spr.skill_id,
                swd.work_duration_code,
                swd.work_duration_name,
                swd.color_code,
                swc.category_name,
                sd.department_name,
                sj.job_title,
                ( spr.time_end - spr.time_start ) * 24 sch_hrs,
                to_char(
                    spr.time_start,
                    'hh:mi am'
                )                                      time_start_short,
                to_char(
                    spr.time_end,
                    'hh:mi am'
                )                                      time_end_short
            FROM
                sc_person_rosters         spr,
                sc_work_duration          swd,
                sc_work_duration_category swc,
                sc_departments            sd,
                sc_jobs                   sj
            WHERE
                    spr.person_id = nvl(
                        :personId,
                        spr.person_id
                    )
                AND spr.person_roster_id = nvl(
                    :personRosterId,
                    spr.person_roster_id
                )
                AND spr.effective_date BETWEEN ( nvl(
                    trunc(:startDate),
                    spr.effective_date
                ) ) AND ( nvl(
                    trunc(:endDate),
                    spr.effective_date
                ) )
                AND swd.work_duration_id = spr.work_duration_id
                AND sd.department_id = spr.department_id
                AND sj.job_title_id = spr.job_title_id
                AND swc.work_duration_category_id (+) = swd.work_duration_category_id
            ORDER BY
                spr.person_id,
                spr.effective_date,
                spr.time_start""";

//    public static String deletePersonRosterSingleSql = """
//            delete sc_person_rosters spr
//            where spr.person_roster_id=:personRosterId""";
//
//    public static String deletePersonRosterManySql = """
//            delete sc_person_rosters spr
//            where spr.person_id=:personId
//            and trunc(spr.effective_date) between trunc(:startDate) and trunc(:endDate)""";

}
