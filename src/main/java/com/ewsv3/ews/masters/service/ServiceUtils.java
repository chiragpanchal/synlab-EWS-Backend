package com.ewsv3.ews.masters.service;


public class ServiceUtils {


    public static String personMasterSql = """
            SELECT
                per.person_id,
                per.user_id,
                per.assignment_id,
                per.employee_number,
                per.assignment_number,
                per.full_name person_name,
                per.department_id,
                per.department_name,
                per.job_title_id,
                per.job_title,
                per.work_location_id,
                per.location_name,
                per.shift_type,
                per.band,
                per.email_address,
                per.position_id,
                per.position_name,
                per.hire_date,
                per.termination_date
            FROM
                sc_timekeeper_person_v tkv,
                sc_person_v            per
            WHERE
                    tkv.timekeeper_user_id = :userId
                AND tkv.profile_id = :profileId
                AND per.person_id = tkv.person_id
                AND nvl(
                    per.hire_date,
                    trunc(:endDate)
                ) <= trunc(:endDate)
                AND nvl(
                    per.termination_date,
                    trunc(:startDate)
                ) >= trunc(:startDate)""";

    public static String departmentMasterSql = """
            SELECT
                sd.department_id,
                sd.department_name
            FROM
                sc_departments sd
            WHERE
                sd.department_id IN (
                    SELECT
                        tkv.department_id
                    FROM
                        sc_timekeeper_person_v tkv
                    WHERE
                            tkv.timekeeper_user_id = :userId
                        AND tkv.profile_id = :profileId
                )""";

    public static String jobTitleMasterSql = """
            SELECT
                sj.job_title_id,
                sj.job_title
            FROM
                sc_jobs sj
            WHERE
                sj.job_title_id IN (
                    SELECT
                        tkv.job_title_id
                    FROM
                        sc_timekeeper_person_v tkv
                    WHERE
                            tkv.timekeeper_user_id = :userId
                        AND tkv.profile_id = :profileId
                )""";

    public static String locationMasterSql = """
            SELECT
                sl.work_location_id,
                sl.location_name
            FROM
                sc_work_locations sl
            WHERE
                sl.work_location_id IN (
                    SELECT
                        tkv.work_location_id
                    FROM
                        sc_timekeeper_person_v tkv
                    WHERE
                            tkv.timekeeper_user_id = :userId
                        AND tkv.profile_id = :profileId
                )""";

    public static String emergencyTypeMasterSql = """
            SELECT
                vl.value_set_value_id,
                vl.value_meaning,
                vl.enabled
            FROM
                sc_value_set_values vl,
                sc_value_sets       vs
            WHERE
                    vl.value_set_id = vs.value_set_id
                AND vl.enabled  = 'Y'
                AND vs.value_set_name = 'Emergency Type'""";

    public static String onCallTypeMasterSql = """
            SELECT
                vl.value_set_value_id,
                vl.value_meaning,
                vl.enabled
            FROM
                sc_value_set_values vl,
                sc_value_sets       vs
            WHERE
                    vl.value_set_id = vs.value_set_id
                AND vl.enabled  = 'Y'
                AND vs.value_set_name = 'On Call Type'""";

    public static String deleteRosterReasonSql = """
            SELECT
                vl.value_set_value_id,
                vl.value_meaning,
                vl.enabled
            FROM
                sc_value_set_values vl,
                sc_value_sets       vs
            WHERE
                    vl.value_set_id = vs.value_set_id
                AND vl.enabled  = 'Y'
                AND vs.value_set_name = 'Delete Roster Reasons'""";

    public static String workDurationSql = """
            SELECT
                work_duration_id,
                work_duration_code,
                work_duration_name,
                valid_from,
                valid_to,
                time_start,
                break_start,
                break_end,
                time_end,
                enterprise_id,
                mon,
                tue,
                wed,
                thu,
                fri,
                sat,
                sun,
                color_code,
                duration,
                work_duration_category_id,
                exception_events,
                min_work_hrs,
                max_work_hrs,
                work_unit,
                hcm_schedule_id,
                null eroster_code,
                time_hour
            FROM
                sc_work_duration""";

    public static String workDurationDetailsSql = """
            SELECT
                work_duration_detail_id,
                work_duration_id,
                type_name,
                effect_day,
                start_time,
                end_time,
                duration,
                created_by,
                created_on,
                last_updated_by,
                last_update_date,
                grace_minutes,
                geenrate_exception
            FROM
                sc_work_duration_details""";
//            " where d.work_duration_id = :workDurationId";

//    String departmentJobSql = "SELECT \n" +
//            "    department_id,\n" +
//            "    department_name,\n" +
//            "    job_title_id,\n" +
//            "    job_title\n" +
//            "FROM\n" +
//            "    sc_positions   pos,\n" +
//            "    sc_departments sd,\n" +
//            "    sc_jobs        sj\n" +
//            "WHERE\n" +
//            "        sd.department_id = pos.organization_id\n" +
//            "    AND sj.job_title_id = pos.job_id ";

//    public static String departmentJobSql = """
//            SELECT DISTINCT
//                sd.department_id,
//                sd.department_name,
//                sj.job_title_id,
//                sj.job_title,
//                pos.position_id,
//                pos.name position_name
//            FROM
//                sc_departments sd,
//                sc_jobs        sj,
//                sc_positions   pos
//            WHERE
//                    pos.organization_id = sd.department_id
//                AND pos.job_id = sj.job_title_id
//                AND pos.organization_id IN (
//                    SELECT
//                        tkv.department_id
//                    FROM
//                        sc_timekeeper_person_v tkv
//                    WHERE
//                            tkv.timekeeper_user_id = :userId
//                        AND tkv.profile_id = :profileId
//                )""";

    public static String departmentJobSql = """
            select distinct
                tkv.department_id,
                tkv.department_name,
                job_title_id,
                job_title,
                0     position_id,
                'n/a' position_name
            from
                sc_timekeeper_person_v tkv
            where
                    tkv.timekeeper_user_id = :userId
                and tkv.profile_id = :profileId
                and tkv.department_id is not null
                and tkv.job_title_id is not null""";

    public static String sqlProfiles = """
            SELECT
                sap.profile_id,
                ltrim(
                    rtrim(
                        sap.profile_name
                    )
                ) profile_name,
                supa.user_type,
                (
                    SELECT
                        LISTAGG(appr.full_name
                                || ' ['
                                || appr.email_address
                                || ']',
                                ', ') WITHIN GROUP(
                        ORDER BY
                            appr.full_name
                        )
                    FROM
                        sc_user_profile_assoc supa2,
                        sc_person_v           appr
                    WHERE
                            supa2.profile_id = supa.profile_id
                        AND supa2.user_type = 'Approver'
                        AND appr.user_id = supa2.user_id
                ) approvers
            FROM
                sc_user_profile_assoc supa,
                sc_access_profiles    sap
            WHERE
                    supa.user_id = :userId
                AND sap.profile_id = supa.profile_id
                AND trunc(sysdate) BETWEEN sap.start_date AND sap.end_date
            ORDER BY
                ltrim(
                    rtrim(
                        sap.profile_name
                    )
                )""";

    public static String getPersonRosterDataSql = """
            SELECT spr.person_roster_id,
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
             FROM sc_person_rosters      spr,
                sc_timekeeper_person_v tkv,
                sc_work_duration swd,
                sc_work_duration_category swc,
                sc_departments sd,
                sc_jobs sj
            WHERE tkv.timekeeper_user_id = :userId
                AND spr.effective_date BETWEEN trunc(:startDate) AND trunc(:endDate)
                AND spr.person_id = tkv.person_id
                AND tkv.profile_id = :profileId
                AND swd.work_duration_id=spr.work_duration_id
                AND sd.department_id =spr.department_id
                AND sj.job_title_id=spr.job_title_id
                AND nvl(
                    tkv.hire_date,
                    trunc(:endDate)
                ) <= trunc(:endDate)
                AND nvl(
                    tkv.termination_date,
                    trunc(:startDate)
                ) >= trunc(:startDate)
                OFFSET 1 ROWS FETCH NEXT 1000 ROWS ONLY""";


    public static String getPersonRosterDataSqlSmall = """
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
                spr.skill_1
             FROM
                sc_person_rosters      spr,
                sc_timekeeper_person_v tkv,
                sc_work_duration swd,
                sc_work_duration_category swc,
                sc_departments sd,
                sc_jobs sj
            WHERE tkv.timekeeper_user_id = :userId
                AND spr.effective_date BETWEEN trunc(:startDate) AND trunc(:endDate)
                AND spr.person_id = tkv.person_id
                AND tkv.profile_id = :profileId
                AND swd.work_duration_id=spr.work_duration_id
                AND sd.department_id =spr.department_id
                AND sj.job_title_id=spr.job_title_id
                AND nvl(
                    tkv.hire_date,
                    trunc(:endDate)
                ) <= trunc(:endDate)
                AND nvl(
                    tkv.termination_date,
                    trunc(:startDate)
                ) >= trunc(:startDate)
                OFFSET 1 ROWS FETCH NEXT 1000 ROWS ONLY""";

    public static String personRosterPivotSql = """
            SELECT
                t.person_roster_pivot_id,
                t.person_id,
                per.full_name,
                per.employee_number,
                per.assignment_id,
                per.assignment_number,
                per.department_name,
                per.job_title,
                per.location_name,
                per.band,
                per.department_Id,
                per.job_title_id,
                per.work_location_id,
                per.position_id,
                t.start_date,
                t.end_date,
                t.d_1,
                t.d_2,
                t.d_3,
                t.d_4,
                t.d_5,
                t.d_6,
                t.d_7,
                t.d_8,
                t.d_9,
                t.d_10,
                t.d_11,
                t.d_12,
                t.d_13,
                t.d_14,
                t.d_15,
                t.d_16,
                t.d_17,
                t.d_18,
                t.d_19,
                t.d_20,
                t.d_21,
                t.d_22,
                t.d_23,
                t.d_24,
                t.d_25,
                t.d_26,
                t.d_27,
                t.d_28,
                t.d_29,
                t.d_30,
                t.d_31,
                t.d_32,
                t.d_33,
                t.d_34,
                t.d_35,
                t.d_36,
                t.d_37,
                t.d_38,
                t.d_39,
                t.d_40,
                t.d_41,
                t.d_42,
                t.d_43,
                t.d_44,
                t.d_45,
                t.d_46,
                t.d_47,
                t.d_48,
                t.d_49,
                t.d_50,
                t.d_51,
                t.d_52,
                t.d_53,
                t.d_54,
                t.d_55,
                t.d_56,
                t.d_57,
                t.d_58,
                t.d_59,
                t.d_60,
                t.d_61,
                t.d_62,
                t.d_63,
                t.d_64,
                t.d_65,
                t.d_66,
                t.d_67,
                t.d_68,
                t.d_69,
                t.d_70,
                t.d_71,
                t.d_72,
                t.d_73,
                t.d_74,
                t.d_75,
                t.d_76,
                t.d_77,
                t.d_78,
                t.d_79,
                t.d_80,
                t.d_81,
                t.d_82,
                t.d_83,
                t.d_84,
                t.d_85,
                t.d_86,
                t.d_87,
                t.d_88,
                t.d_89,
                t.d_90,
                t.d_91,
                t.d_92,
                t.d_93,
                t.d_94,
                t.d_95,
                t.d_96,
                t.d_97,
                t.d_98,
                t.d_99,
                t.d_100,
                t.d_101,
                t.d_102,
                t.d_103,
                t.d_104,
                t.d_105,
                t.d_106,
                t.d_107,
                t.d_108,
                t.d_109,
                t.d_110,
                t.d_111,
                t.d_112,
                t.d_113,
                t.d_114,
                t.d_115,
                t.d_116,
                t.d_117,
                t.d_118,
                t.d_119,
                t.d_120,
                t.d_121,
                t.d_122,
                t.d_123,
                t.d_124,
                t.d_125,
                t.d_126,
                t.prev_sch_hrs,
                t.curr_sch_hrs,
                t.prev_act_hrs,
                t.curr_act_hrs,
                t.seq_id,
                t.login_user_id,
                t.dml_flag,
                t.mandate_shifts,
                t.open_shifts,
                t.services,
                t.roster_err_flag,
                t.transfer_flag
            FROM
                sc_person_roster_pivot_t t,
                sc_person_v              per
            WHERE
                    t.login_user_id = :userId
                AND per.person_id = t.person_id
                order by per.full_name
            """;

    public static String personRosterSql = """
            SELECT
                per.person_id,
                per.assignment_id,
                per.employee_number,
                per.full_name,
                per.department_id,
                per.department_name,
                per.job_title_id,
                per.job_title,
                per.work_location_id,
                per.location_name,
                per.employee_types,
                per.email_address,
                per.position_name,
                per.hire_date,
                per.grade_name,
                spr.effective_date,
                spr.time_start,
                spr.time_end,
                ( spr.time_end - spr.time_start ) * 24 sch_hrs,
                spr.department_id                      sch_department_id,
                spr.job_title_id                       sch_job_title_id,
                spr.work_location_id                   sch_work_location_id,
                sd.department_name                     sch_department,
                sj.job_title                           scj_job_title,
                loc.location_name                      sch_location,
                spr.on_call,
                spr.emergency,
                spr.published,
                swd.work_duration_id,
                swd.work_duration_code,
                swd.work_duration_name
            FROM
                sc_person_rosters spr,
                sc_person_v       per,
                sc_work_duration  swd,
                sc_departments    sd,
                sc_jobs           sj,
                sc_work_locations loc
            WHERE
                    per.person_id = spr.person_id(+)
                AND swd.work_duration_id(+) = spr.work_duration_id
                AND spr.effective_date(+) BETWEEN trunc(:startDate) AND trunc(:endDate)
                AND spr.department_id = sd.department_id(+)
                AND spr.job_title_id = sj.job_title_id(+)
                AND spr.work_location_id = loc.work_location_id(+)
                AND EXISTS (
                    SELECT
                        tkv.person_id
                    FROM
                        sc_timekeeper_person_v tkv
                    WHERE
                            tkv.timekeeper_user_id = :userId
                        AND tkv.profile_id = :profileId
                        AND tkv.person_id=per.person_id
                ) order by per.full_name, per.assignment_id""";

//    public String personRosterSql = """
//            SELECT
//                per.person_id,
//                per.assignment_id,
//                per.employee_number,
//                per.full_name,
//                per.department_id,
//                per.department_name,
//                per.job_title_id,
//                per.job_title,
//                per.work_location_id,
//                per.location_name,
//                per.employee_types,
//                per.email_address,
//                per.position_name,
//                per.hire_date,
//                per.grade_name,
//                cal.dt effective_date,
//                spr.time_start,
//                spr.time_end,
//                ( spr.time_end - spr.time_start ) * 24 sch_hrs,
//                spr.department_id                      sch_department_id,
//                spr.job_title_id                       sch_job_title_id,
//                spr.work_location_id                   sch_work_location_id,
//                sd.department_name                     sch_department,
//                sj.job_title                           scj_job_title,
//                loc.location_name                      sch_location,
//                spr.on_call,
//                spr.emergency,
//                spr.published,
//                swd.work_duration_id,
//                swd.work_duration_code,
//                swd.work_duration_name
//            FROM
//                sc_person_rosters spr,
//                sc_person_v       per,
//                sc_work_duration  swd,
//                sc_departments    sd,
//                sc_jobs           sj,
//                sc_work_locations loc,
//                (
//                                SELECT
//                                    trunc(:startDate) + level - 1 dt
//                                FROM
//                                    dual
//                                CONNECT BY
//                                    level <= ( trunc(:endDate) - trunc(:startDate) + 1 )
//                            )                 cal
//            WHERE
//                    per.person_id = spr.person_id(+)
//                AND swd.work_duration_id(+) = spr.work_duration_id
//                AND spr.effective_date(+) = cal.dt
//                AND spr.department_id = sd.department_id(+)
//                AND spr.job_title_id = sj.job_title_id(+)
//                AND spr.work_location_id = loc.work_location_id(+)
//                AND EXISTS (
//                    SELECT
//                        tkv.person_id
//                    FROM
//                        sc_timekeeper_person_v tkv
//                    WHERE
//                            tkv.timekeeper_user_id = :userId
//                        AND tkv.profile_id = :profileId
//                        AND tkv.person_id=per.person_id
//                ) order by per.person_id, per.assignment_id, cal.dt""";


    public static String userTaskSql = """
            SELECT
                v.group_name,
                v.group_seq,
                v.task_id,
                v.task_name,
                v.task_code,
                v.can_create,
                v.can_edit,
                v.can_delete,
                v.can_view,
                v.user_id,
                v.seq,
                1             enterprise_id,
                per.person_id employee_id
              FROM
                sc_user_tasks_v v,
                sc_person_v     per
             WHERE
                    v.user_id = :userId
                   AND per.user_id = v.user_id
             ORDER BY
                v.group_seq,
                v.group_name,
                v.seq,
                v.task_name""";

    public static String userTaskTanstackSql = """
            SELECT
                v.group_name,
                v.group_seq,
                v.task_id menu_id,
                v.task_name menu_name,
                v.task_code menu_code,
                v.can_create,
                v.seq,
                v.icon
            FROM
                sc_user_tasks_v v
            WHERE
                v.user_id = :userId
            ORDER BY
                v.group_seq,
                v.group_name,
                v.seq,
                v.task_name""";
}
