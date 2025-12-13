package com.ewsv3.ews.commons.service;

public class CommonUtils {

    public static String sqlGetUserFromUserId = """
            select
                su.user_id,
                su.user_name,
                per.full_name       user_person_name,
                per.employee_number user_employee_number,
                per.job_title,
                per.department_name,
                per.person_id
            from
                sc_person_v per,
                sc_users    su
            where
                    su.user_id = :userId
                and per.user_id (+) = su.user_id""";
}
