package com.ewsv3.ews.reports.service.reportMaters;

public class ReportMastersUtils {

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
