package com.ewsv3.ews.punch.service;

public class PunchUtils {

    public static String timeTypeSql = """
            SELECT
                vl.value_set_value_id time_type_id,
                vl.value_description time_type_value
            FROM
                sc_value_set_values vl,
                sc_value_sets       vs
            WHERE
                    vl.value_set_id = vs.value_set_id
                AND vl.enabled  = 'Y'
                AND vs.value_set_name = 'Time Type'""";

    public static String insertPunch = """
            INSERT INTO sc_person_punches (
                person_id,
                department_id,
                job_title_id,
                punch_time,
                punch_type,
                created_by,
                last_updated_by,
                time_type
            ) VALUES (
                :personId,
                :departmentId,
                :jobTitleId,
                :punchTime,
                :punchType,
                :createdBy,
                :lastUpdatedBy,
                :timeType
            )
            """;

    public static String getPunchData = """
            SELECT
                person_punch_id,
                punch_time,
                punch_type,
                department_name,
                job_title,
                time_type
              FROM
                (
                    SELECT
                        spp.person_punch_id,
                        spp.punch_time,
                        spp.punch_type,
                        sd.department_name,
                        sj.job_title,
                        vl.value_description time_type
                      FROM
                        sc_person_punches   spp,
                        sc_departments      sd,
                        sc_jobs             sj,
                        sc_value_set_values vl
                     WHERE
                            person_id = :personId
                           AND sd.department_id          = spp.department_id
                           AND sj.job_title_id           = spp.job_title_id
                           AND vl.value_set_value_id (+) = spp.time_type
                     ORDER BY
                        punch_time DESC
                )
            OFFSET nvl(
                :offset,
                1
            ) * :pageSize ROWS
             FETCH NEXT :pageSize ROWS ONLY
                                """;

}
