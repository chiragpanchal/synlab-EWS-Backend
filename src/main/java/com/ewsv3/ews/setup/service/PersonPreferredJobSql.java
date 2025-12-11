package com.ewsv3.ews.setup.service;

public class PersonPreferredJobSql {
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
                AND (lower(tkv.employee_number) like :text or lower(tkv.person_name) like :text)
                ORDER BY
                    tkv.person_name,
                    tkv.employee_number
                OFFSET nvl(
                    :page - 1,
                    1
                ) * :pageSize ROWS FETCH NEXT :pageSize ROWS ONLY""";

    public static String jobSql = """
            SELECT DISTINCT job_title_id, job_title FROM sc_timekeeper_person_v WHERE profile_id=:profileId
                                            """;

    public static String currencySql = """
            select CURRENCY_ID, CURRENCY_CODE ||' ('|| CURRENCY_NAME ||')' currency from sc_currencies
                                            """;

    public static String skillsSql = """
            SELECT
               skill_id,
               skill,
               comments,
               created_By,
               created_On,
               last_Updated_By,
               last_Update_Date,
               date_From,
               date_To
             FROM
               sc_skills
            WHERE
               nvl(date_to, SYSDATE) >= SYSDATE
            ORDER BY
               skill
                       """;
}
