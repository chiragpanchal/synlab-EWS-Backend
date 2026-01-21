package com.ewsv3.ews.commons.utils;

public class CommonUtils {

    public static String sqlActivePerson = """
            select
                user_id,
                person_id,
                full_name,
                employee_number,
                job_title
            from
                sc_person_v
            where
                termination_date is null
                and user_id is not null
                and nvl(termination_date, trunc(sysdate))>= trunc(sysdate)
                and (lower(FULL_NAME) like '%'||lower(ltrim(rtrim(:strPerson)))||'%'
                or lower(employee_number) like '%'||lower(ltrim(rtrim(:strPerson)))||'%')
            order by
                full_name""";

    public static String sqlDepartments = """
            select
                department_id,
                department_name
            from
                sc_departments
            order by
                department_name""";

    public static String sqlJobs = """
            select
                job_title_id,
                job_title
            from
                sc_jobs
            order by
                job_title""";

    public static String sqlProjects = """
            select
                project_id,
                project_name,
                project_number
            from
                sc_projects
            where (lower(project_name) like '%'||lower(ltrim(rtrim(:strProject)))||'%'
                or lower(project_number) like '%'||lower(ltrim(rtrim(:strProject)))||'%')
            order by project_name""";

    public static String sqlJobFamily = """
            select distinct
                job_family
            from
                sc_jobs
            order by
                job_family""";

    public static String sqlBusinessUnits = """
            select
                business_unit_id,
                business_unit_name
            from
                sc_business_units""";

    public static String sqlLegalEntity = """
            select
                legal_entity_id,
                name
            from
                sc_legal_entities
            order by
                name""";

    public static String sqlEmployeeTypes = """
            select
                lookup_code,
                meaning
            from
                sc_lookups
            where
                    lookup_type = 'EMPLOYEE_TYPES'
                and enabled = 'Y'
            order by
                meaning""";

    public static String sqlEmploymentTypes = """
            select
                lookup_code,
                meaning
            from
                sc_lookups
            where
                    lookup_type = 'EMPLOYMENT_TYPE'
                and enabled = 'Y'
            order by
                meaning""";

    public static String sqlGender = """
            select
                lookup_code,
                meaning
            from
                sc_lookups
            where
                    lookup_type = 'GENDER'
                and enabled = 'Y'
            order by
                meaning""";

    public static String sqlNationality = """
            select
                lookup_code,
                meaning
            from
                sc_lookups
            where
                    lookup_type = 'NATIONALITY'
                and enabled = 'Y'
            order by
                meaning""";

    public static String sqlReligion = """
            select
                lookup_code,
                meaning
            from
                sc_lookups
            where
                    lookup_type = 'RELIGION'
                and enabled = 'Y'
            order by
                meaning""";

    public static String sqlShiftType = """
            select
                lookup_code,
                meaning
            from
                sc_lookups
            where
                    lookup_type = 'SHIFT_TYPE'
                and enabled = 'Y'
            order by
                meaning""";

    public static String sqlGrades = """
            select
                grade_id,
                grade_name
            from
                sc_grades
            where
                enabled = 'Y'
            order by
                grade_name""";

    public static String personSkillsSQL= """
            SELECT
                pps.person_preferred_skill_id,
                ppj.person_id,
                job.job_title,
                ppj.job_title_id,
                ppj.per_hr_sal rate,
                sc.currency_code,
                ss.skill_id,
                ss.skill
              FROM
                sc_person_preferred_jobs   ppj,
                sc_currencies              sc,
                sc_jobs                    job,
                sc_person_preferred_skills pps,
                sc_skills                  ss
             WHERE
                    job.job_title_id = ppj.job_title_id
                   AND sc.currency_id (+)          = ppj.currency_id
                   AND pps.person_preferred_job_id = ppj.person_preferred_job_id
                   AND ss.skill_id                 = pps.skill_id
                   AND ppj.person_id               = :personId
             ORDER BY
                job.job_title,
                ss.skill""";

}
