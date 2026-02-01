package com.ewsv3.ews.reports.service.reportMaters;

public class ReportMastersUtils {

    /*PERSON*/
    public static String reportPersonTKSQL= """
            SELECT DISTINCT
                tkv.person_id,
                tkv.employee_number,
                tkv.person_name,
                tkv.department_name,
                tkv.job_title,
                tkv.grade_name,
                tkv.location_name
              FROM
                sc_timekeeper_person_v tkv
             WHERE
                    tkv.timekeeper_user_id = :userId
                   AND tkv.profile_id = :profileId
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
                   AND nvl(
                    tkv.hire_date,
                    :startDate
                ) <= :startDate
                   AND nvl(
                    tkv.termination_date,
                    :endDate
                ) >= :endDate
             ORDER BY
                tkv.person_name
            OFFSET :offset * :pageSize ROWS
             FETCH NEXT :pageSize ROWS ONLY""";

    public static String reportPersonDirectReporteesSQL= """
            SELECT DISTINCT
                per.person_id,
                per.employee_number,
                per.full_name person_name,
                per.department_name,
                per.job_title,
                per.grade_name,
                per.location_name
              FROM
                sc_person_v per
             WHERE
                EXISTS (
                    SELECT
                        'Y'
                      FROM
                        sc_person_manager spm
                     WHERE
                            spm.manager_id = :personId
                           AND spm.person_id = per.person_id
                )
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
                   AND nvl(
                    per.hire_date,
                    :startDate
                ) <= :startDate
                   AND nvl(
                    per.termination_date,
                    :endDate
                ) >= :endDate
             ORDER BY
                per.full_name
            OFFSET :offset * :pageSize ROWS
             FETCH NEXT :pageSize ROWS ONLY""";

    public static String reportPersonAllReporteesSQL= """
            SELECT DISTINCT
                per.person_id,
                per.employee_number,
                per.full_name person_name,
                per.department_name,
                per.job_title,
                per.grade_name,
                per.location_name
              FROM
                sc_person_v per
             WHERE
                EXISTS (
                    SELECT
                        'Y'
                      FROM
                        sc_person_manager spm
                     WHERE
                        spm.person_id = per.person_id
                    START WITH
                        spm.manager_id = :personId
                    CONNECT BY
                        PRIOR spm.manager_id = spm.person_id
                )
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
                   AND nvl(
                    per.hire_date,
                    :startDate
                ) <= :startDate
                   AND nvl(
                    per.termination_date,
                    :endDate
                ) >= :endDate
             ORDER BY
                per.full_name
            OFFSET :offset * :pageSize ROWS
             FETCH NEXT :pageSize ROWS ONLY""";


    /*DEPARTMENT*/
    public static String reportDepartmentTKSQL= """
            SELECT DISTINCT
                profile_id,
                department_id,
                department_name
              FROM
                sc_timekeeper_person_v tkv
             WHERE
                tkv.timekeeper_user_id = :userId""";

    public static String reportDepartmentDirectReporteeSQL= """
            SELECT DISTINCT
                0 profile_id,
                department_id,
                department_name
              FROM
                sc_person_v per
             WHERE
                per.person_id IN (
                    SELECT
                        pm.person_id
                      FROM
                        sc_person_manager pm
                     WHERE
                        pm.manager_id = :personId
                )""";

    public static String reportDepartmentAllReporteeSQL= """
            SELECT DISTINCT
                 1 profile_id,
                 department_id,
                 department_name
               FROM
                 sc_person_v per
              WHERE
                 per.person_id IN (
                     SELECT
                         pm.person_id
                       FROM
                         sc_person_manager pm
                     START WITH
                         pm.manager_id = :personId
                     CONNECT BY
                         PRIOR pm.person_id = pm.manager_id
                 )""";

    /*JOB*/
    public static String reportJobTKSQL= """
            SELECT DISTINCT
                profile_id,
                job_title_id,
                job_title
              FROM
                sc_timekeeper_person_v tkv
             WHERE
                tkv.timekeeper_user_id = :userId""";

    public static String reportJobDirectReporteeSQL= """
            SELECT DISTINCT
                0 profile_id,
                job_title_id,
                job_title
              FROM
                sc_person_v per
             WHERE
                per.person_id IN (
                    SELECT
                        pm.person_id
                      FROM
                        sc_person_manager pm
                     WHERE
                        pm.manager_id = :personId
                )""";

    public static String reportJobAllReporteeSQL= """
            SELECT DISTINCT
                 1 profile_id,
                 job_title_id,
                job_title
               FROM
                 sc_person_v per
              WHERE
                 per.person_id IN (
                     SELECT
                         pm.person_id
                       FROM
                         sc_person_manager pm
                     START WITH
                         pm.manager_id = :personId
                     CONNECT BY
                         PRIOR pm.person_id = pm.manager_id
                 )""";

    /*STATUS*/
    public static String statusSQL= """
            SELECT TRIM(REGEXP_SUBSTR('Pending Approval,Approved,Draft', '[^,]+', 1, LEVEL)) AS status
            FROM dual
            CONNECT BY REGEXP_SUBSTR('Pending Approval,Approved,Draft', '[^,]+', 1, LEVEL) IS NOT NULL
            order by status""";

    /*PAY CODES*/
    public static String payCodeSQL= """
            SELECT
                spc.pay_code_id,
                spc.pay_code_name
              FROM
                sc_pay_codes spc
             WHERE
                enabled = 'Y'
             ORDER BY
                pay_code_name""";
}
