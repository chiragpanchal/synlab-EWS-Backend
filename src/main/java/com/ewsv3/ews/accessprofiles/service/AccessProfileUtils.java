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

    public static String sqlInsertAccessProfile = """
            insert into sc_access_profiles (
                profile_id,
                created_by,
                last_updated_by,
                skip_approval,
                profile_name,
                end_date,
                start_date,
                enterprise_id
            ) values (
                :profileId,
                :createdBy,
                :lastUpdatedBy,
                :skipApproval,
                :profileName,
                :endDate,
                :startDate,
                1
            )""";

    public static String sqlUpdateAccessProfile = """
            update sc_access_profiles
            set
                last_updated_by = :lastUpdatedBy,
                skip_approval = :skipApproval,
                profile_name = :profileName,
                end_date = :endDate,
                start_date = :startDate
            where
                profile_id = :profileId""";

    public static String sqlDeleteAccessProfile = """
            delete sc_access_profiles
            where
                profile_id = :profileId""";

    public static String sqlInsertAccessProfileLines = """
                insert into sc_access_profile_lines (
                    profile_id,
                    created_by,
                    nationality,
                    last_updated_by,
                    job_id,
                    department_id,
                    shift_type,
                    legal_entity_id,
                    job_family,
                    employee_catg,
                    business_unit_id,
                    access_profile_line_id,
                    religion,
                    person_id,
                    employee_type_id,
                    gender,
                    project_id,
                    grade_id,
                    include_exclude_flag
                ) values (
                    :profileId,
                    :createdBy,
                    :nationality,
                    :lastUpdatedBy,
                    :jobId,
                    :departmentId,
                    :shiftType,
                    :legalEntityId,
                    :jobFamily,
                    :employeeCatg,
                    :businessUnitId,
                    :accessProfileLineId,
                    :religion,
                    :personId,
                    :employeeTypeId,
                    :gender,
                    :projectId,
                    :gradeId,
                    :includeExcludeFlag
                )
            """;

    public static String sqlUpdateAccessProfileLine = """
            update sc_access_profile_lines
            set
                nationality = :nationality,
                last_updated_by = :lastUpdatedBy,
                job_id = :jobId,
                department_id = :departmentId,
                shift_type = :shiftType,
                legal_entity_id = :legalEntityId,
                job_family = :jobFamily,
                employee_catg = :employeeCatg,
                business_unit_id = :businessUnitId,
                religion = :religion,
                person_id = :personId,
                employee_type_id = :employeeTypeId,
                gender = :gender,
                project_id = :projectId,
                grade_id = :gradeId,
                include_exclude_flag = :includeExcludeFlag
            where
                access_profile_line_id = :accessProfileLineId""";

    public static String sqlDeleteAccessProfileLine = """
            delete sc_access_profile_lines
            where
                access_profile_line_id = :accessProfileLineId""";

    public static String sqlInsertUserProfileAssoc = """
            insert into sc_user_profile_assoc (
                profile_id,
                created_by,
                last_updated_by,
                user_id,
                user_profile_assoc_id,
                can_create,
                user_type
            ) values (
                :profileId,
                :createdBy,
                :lastUpdatedBy,
                :userId,
                :userProfileAssocId,
                :canCreate,
                :userType
            )""";

    public static String sqlUpdateUserProfileAssoc = """
            update sc_user_profile_assoc
            set
                last_updated_by = :lastUpdatedBy,
                user_id = :userId,
                can_create = :canCreate,
                user_type = :userType
            where
                user_profile_assoc_id = :userProfileAssocId""";

    public static String sqlDeleteUserProfileAssoc = """
            delete sc_user_profile_assoc
            where
                  user_profile_assoc_id = :userProfileAssocId""";
}
