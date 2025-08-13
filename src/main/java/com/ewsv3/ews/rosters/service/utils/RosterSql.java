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

    public static String InsertPersonRorationAssoc= """
            insert into sc_person_rotation_assoc (
                person_id,
                work_rotation_id,
                start_date,
                end_date,
                start_seq,
                created_by,
                last_updated_by
            ) values (
                :personId,
                :workRotationId,
                :startDate,
                :endDate,
                :startSeq,
                :createdBy,
                :lastUpdatedBy
            )""";

    public static String sqlPersonRotationPlans= """
            select
                per.full_name,
                listagg(wh.work_rotation_name,
                        ', ') within group(
                order by
                    null
                ) work_rotation_name
            from
                sc_person_rotation_assoc pra,
                sc_person_v              per,
                sc_work_rotations_h      wh
            where
                    pra.person_id = :personId
                and per.person_id       = pra.person_id
                and wh.work_rotation_id = pra.work_rotation_id
                and :startDate between pra.start_date and nvl(
                    pra.end_date,
                    :startDate
                )
            group by
                per.full_name""";

    public static String sqlDemandAllocationSummary= """
            select
                t.profile_id,
                t.demand_template_line_id,
                t.effective_date,
                t.req_fte,
                t.alloc_fte,
                t.work_duration_id
            from
                sc_demand_summary_t t
            where
                login_user_id = :userId
            order by
                demand_template_line_id,
                effective_date""";

    public static String sqlDemandAllocationLines= """
            select
                per.employee_number,
                per.full_name,
                t.person_id,
                t.demand_template_line_id,
                t.effective_date,
                t.rate,
                t.alloc_flag,
                t.alloc_time,
                t.sch_hrs
            from
                sc_demand_allocation_t t,
                sc_person_v            per
            where
                    t.login_user_id = :userId
                and t.demand_template_line_id = :demandTemplateLineId
                and t.effective_date= :effectiveDate
                and per.person_id             = t.person_id
            order by
                demand_template_line_id,
                person_id,
                effective_date,
                nvl(
                    rate,
                    0
                )""";


    public static String sqlDemandLinesValidate= """
            select
                dl.department_id,
                dl.job_title_id,
                dl.location_id,
                to_char(
                    swd.time_start,
                    'HH:MI am'
                ) time_start,
                to_char(
                    swd.time_end,
                    'HH:MI am'
                ) time_end,
                decode(
                    swd.sun,
                    'Y',
                    dl.sun,
                    0
                ) sun,
                decode(
                    swd.mon,
                    'Y',
                    dl.mon,
                    0
                ) mon,
                decode(
                    swd.tue,
                    'Y',
                    dl.tue,
                    0
                ) tue,
                decode(
                    swd.wed,
                    'Y',
                    dl.wed,
                    0
                ) wed,
                decode(
                    swd.thu,
                    'Y',
                    dl.thu,
                    0
                ) thu,
                decode(
                    swd.fri,
                    'Y',
                    dl.fri,
                    0
                ) fri,
                decode(
                    swd.sat,
                    'Y',
                    dl.sat,
                    0
                ) sat
            from
                sc_demand_template_l dl,
                sc_work_duration     swd
            where
                    dl.demand_template_id = :demandTemplateId
                and swd.work_duration_id = dl.work_duration_id
            order by
                demand_template_line_id""";

    public static String sqlScheduledValidate= """
            select
                spr.department_id,
                spr.job_title_id,
                spr.work_location_id          location_id,
                spr.time_start,
                spr.time_end,
                count(distinct spr.person_id) scheduled_fte
            from
                sc_person_rosters spr,
                sc_work_duration  swd
            where
                spr.effective_date BETWEEN ( nvl(
                    trunc(:startDate),
                    spr.effective_date
                ) ) AND ( nvl(
                    trunc(:endDate),
                    spr.effective_date
                ) )
                and swd.work_duration_id = spr.work_duration_id
                and swd.work_duration_code <> 'OFF'
                and exists (
                    select
                        'Y'
                    from
                        sc_timekeeper_person_v tkv
                    where
                            tkv.timekeeper_user_id = :userId
                        and tkv.profile_id = :profileId
                        and tkv.person_id  = spr.person_id
                        AND nvl(
                            tkv.hire_date,
                            trunc(:endDate)
                        ) <= trunc(:endDate)
                        AND nvl(
                            tkv.termination_date,
                            trunc(:startDate)
                        ) >= trunc(:startDate)
                )
            group by
                spr.department_id,
                spr.job_title_id,
                spr.work_location_id,
                spr.time_start,
                spr.time_end
            order by
                spr.department_id,
                spr.job_title_id,
                spr.work_location_id,
                spr.time_start,
                spr.time_end""";

}
