package com.ewsv3.ews.accessprofiles.service;

public class AccessProfileUtils {

    public static String sqlSearchedProfiles = """
            WITH profile_person AS (
                SELECT DISTINCT tkv.profile_id
                FROM sc_timekeeper_person_v tkv
                WHERE (:strPerson IS NULL
                       OR tkv.person_name LIKE LOWER('%' || LTRIM(RTRIM(:strPerson)) || '%')
                       OR tkv.employee_number LIKE LOWER('%' || LTRIM(RTRIM(:strPerson)) || '%'))
            ),
            timekeepers AS (
                SELECT supa.profile_id,
                       LISTAGG(mgr.full_name || ' (' || mgr.employee_number || ')', ', ')
                       WITHIN GROUP (ORDER BY mgr.full_name) AS timekeepers
                FROM sc_user_profile_assoc supa
                     LEFT JOIN sc_person_v mgr ON mgr.user_id = supa.user_id
                WHERE (:strTimekeeper IS NULL
                       OR mgr.full_name LIKE LOWER('%' || LTRIM(RTRIM(:strTimekeeper)) || '%')
                       OR mgr.employee_number LIKE LOWER('%' || LTRIM(RTRIM(:strTimekeeper)) || '%'))
                GROUP BY supa.profile_id
            )
            SELECT sap.profile_id,sap.profile_name, sap.start_date, sap.end_date, tk.timekeepers
            FROM sc_access_profiles sap
                  JOIN profile_person pp ON sap.profile_id = pp.profile_id
                  JOIN timekeepers tk ON sap.profile_id = tk.profile_id
            WHERE (:strProfileName IS NULL
                   OR LOWER(sap.profile_name) LIKE LOWER('%' || LTRIM(RTRIM(:strProfileName)) || '%'))""";

    public static String sqlGetProfileFromProfileId = """
            select
                profile_id,
                enterprise_id,
                profile_name,
                start_date,
                end_date,
                created_by,
                created_on,
                last_updated_by,
                last_update_date,
                skip_approval
            from
                sc_access_profiles sap
            where
                sap.profile_id = :profileId""";

    public static String sqlGetProfileLinesFromProfileId = """
            select
                access_profile_line_id,
                profile_id,
                person_id,
                job_id,
                department_id,
                grade_id,
                employee_type_id,
                include_exclude_flag,
                business_unit_id,
                job_family,
                legal_entity_id,
                gender,
                nationality,
                religion,
                sub_department_id,
                employee_catg,
                shift_type,
                project_id,
                created_by,
                created_on,
                last_updated_by,
                last_update_date
            from
                sc_access_profile_lines sapl
            where
                sapl.profile_id = :profileId""";

    public static String sqlGetUserProfileAssocFromProfileId = """
            select
                user_profile_assoc_id,
                user_id,
                profile_id,
                can_create,
                can_edit,
                can_delete,
                can_view,
                user_type,
                vacation_rule_id,
                created_by,
                created_on,
                last_updated_by,
                last_update_date
            from
                sc_user_profile_assoc supa
            where
                supa.profile_id = :profileId""";
}
