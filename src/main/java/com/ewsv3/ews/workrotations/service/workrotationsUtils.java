package com.ewsv3.ews.workrotations.service;

public class workrotationsUtils {

    public static String GetWorkDurationSql = """
            SELECT
                   d.work_duration_id,
                   d.work_duration_code,
                   d.work_duration_name,
                   d.valid_from,
                   d.valid_to,
                   d.time_start,
                   d.break_start,
                   d.break_end,
                   d.time_end,
                   d.enterprise_id,
                   d.mon,
                   d.tue,
                   d.wed,
                   d.thu,
                   d.fri,
                   d.sat,
                   d.sun,
                   d.color_code,
                   d.duration,
                   d.work_duration_category_id,
                   d.exception_events,
                   d.min_work_hrs,
                   d.max_work_hrs,
                   d.work_unit,
                   d.hcm_schedule_id,
                   d.eroster_code,
                   d.break_mins,
                   d.created_by,
                   d.created_on,
                   d.last_updated_by,
                   d.last_update_date,
                   c.full_name created_by_name,
                   u.full_name last_updated_by_name,
                   d.time_Hour
                 FROM
                    sc_work_duration d,
                    sc_person_v c,
                    sc_person_v u
                WHERE (work_duration_id = NVL(:workDurationId, work_duration_id)
                    or :workDurationId=0)
                    and c.user_id=d.created_by
                    and u.user_id=d.last_updated_by
                    AND ( lower(work_duration_code) LIKE '%'
                                             || lower(:searchText)
                                             || '%'
                       OR lower(work_duration_name) LIKE '%'
                                                  || lower(:searchText)
                                                  || '%' )
                      AND ( nvl(
                       :activeOnly,
                       'N'
                   ) = 'N'
                       OR ( nvl(
                           :activeOnly,
                           'N'
                       ) = 'Y'
                      AND trunc(sysdate) BETWEEN nvl(
                       valid_from,
                       trunc(sysdate)
                   ) AND nvl(
                       valid_to,
                       trunc(sysdate)
                   ) ) )
                ORDER BY
                   work_duration_name
            """;

    static String GetWorkRotationSql = """
            SELECT
                    h.work_rotation_id,
                    h.work_rotation_name,
                    h.start_date,
                    h.iterations,
                    h.created_by,
                    h.created_on,
                    h.last_updated_by,
                    h.last_update_date,
                    h.expiry_date,
                    h.color_code,
                    h.forever_flag,
                    c.full_name created_by_name,
                    u.full_name last_updated_by_name
                FROM
                    sc_work_rotations_h h,
                    sc_person_v c,
                    sc_person_v u
                 where (:workRotationId=0 OR  work_rotation_id= :workRotationId)
                 and c.user_id=h.created_by
                 and u.user_id=h.last_updated_by
                 order by work_rotation_name
            """;

    static String GetWorkRotationLineSql = """
            SELECT
                    l.work_rotation_line_id,
                    l.work_rotation_id,
                    l.seq,
                    l.iterations,
                    l.created_by,
                    l.created_on,
                    l.last_updated_by,
                    l.last_update_date,
                    l.d_1,
                    l.d_2,
                    l.d_3,
                    l.d_4,
                    l.d_5,
                    l.d_6,
                    l.d_7,
                    c.full_name created_by_name,
                    u.full_name last_updated_by_name
                FROM
                    sc_work_rotation_lines l,
                    sc_person_v c,
                    sc_person_v u
                WHERE
                    work_rotation_id = :workRotationId
                 and c.user_id=l.created_by
                 and u.user_id=l.last_updated_by
                ORDER BY
                    seq
             """;

    static String InsertWorkDurationSql = """
            INSERT INTO sc_work_duration (
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
                eroster_code,
                break_mins,
                created_by,
                created_on,
                last_updated_by,
                last_update_date,
                time_hour
            ) VALUES (
                :work_duration_id,
                :work_duration_code,
                :work_duration_name,
                :valid_from,
                :valid_to,
                :time_start,
                :break_start,
                :break_end,
                :time_end,
                :enterprise_id,
                :mon,
                :tue,
                :wed,
                :thu,
                :fri,
                :sat,
                :sun,
                :color_code,
                :duration,
                :work_duration_category_id,
                :exception_events,
                :min_work_hrs,
                :max_work_hrs,
                :work_unit,
                :hcm_schedule_id,
                :eroster_code,
                :break_mins,
                :created_by,
                :created_on,
                :last_updated_by,
                :last_update_date.
                :time_hour
            )
            """;

    static String updateWorkDurationSql = """
            UPDATE sc_work_duration SET
                work_duration_code = :work_duration_code,
                work_duration_name = :work_duration_name,
                valid_from = :valid_from,
                valid_to = :valid_to,
                time_start = :time_start,
                break_start = :break_start,
                break_end = :break_end,
                time_end = :time_end,
                enterprise_id = :enterprise_id,
                mon = :mon,
                tue = :tue,
                wed = :wed,
                thu = :thu,
                fri = :fri,
                sat = :sat,
                sun = :sun,
                color_code = :color_code,
                duration = :duration,
                work_duration_category_id = :work_duration_category_id,
                exception_events = :exception_events,
                min_work_hrs = :min_work_hrs,
                max_work_hrs = :max_work_hrs,
                work_unit = :work_unit,
                hcm_schedule_id = :hcm_schedule_id,
                eroster_code = :eroster_code,
                break_mins = :break_mins,
                last_updated_by = :last_updated_by,
                last_update_date = :last_update_date,
                time_hour = :timeHour
            WHERE
                work_duration_id = :work_duration_id
            """;

    static String deleteWorkDurationSql = """
            DELETE FROM sc_work_duration
            WHERE work_duration_id = :work_duration_id
            """;

    static String InsertWorkRotationSql = """
            INSERT INTO sc_work_rotations_h (
                work_rotation_id,
                work_rotation_name,
                start_date,
                iterations,
                created_by,
                created_on,
                last_updated_by,
                last_update_date,
                expiry_date,
                color_code,
                forever_flag
            ) VALUES (
                :work_rotation_id,
                :work_rotation_name,
                :start_date,
                :iterations,
                :created_by,
                :created_on,
                :last_updated_by,
                :last_update_date,
                :expiry_date,
                :color_code,
                :forever_flag
            )
            """;

    static String updateWorkRotationSql = """
            UPDATE sc_work_rotations_h SET
                work_rotation_name = :work_rotation_name,
                start_date = :start_date,
                iterations = :iterations,
                expiry_date = :expiry_date,
                color_code = :color_code,
                forever_flag = :forever_flag,
                last_updated_by = :last_updated_by,
                last_update_date = :last_update_date
            WHERE
                work_rotation_id = :work_rotation_id
            """;

    static String deleteWorkRotationSql = """
            DELETE FROM sc_work_rotations_h
            WHERE work_rotation_id = :work_rotation_id
            """;

    static String InsertWorkRotationLineSql = """
            INSERT INTO sc_work_rotation_lines (
                work_rotation_line_id,
                work_rotation_id,
                seq,
                iterations,
                created_by,
                created_on,
                last_updated_by,
                last_update_date,
                d_1,
                d_2,
                d_3,
                d_4,
                d_5,
                d_6,
                d_7
            ) VALUES (
                :work_rotation_line_id,
                :work_rotation_id,
                :seq,
                :iterations,
                :created_by,
                :created_on,
                :last_updated_by,
                :last_update_date,
                :d_1,
                :d_2,
                :d_3,
                :d_4,
                :d_5,
                :d_6,
                :d_7
            )
            """;

    static String updateWorkRotationLineSql = """
            UPDATE sc_work_rotation_lines SET
                work_rotation_id = :work_rotation_id,
                seq = :seq,
                iterations = :iterations,
                d_1 = :d_1,
                d_2 = :d_2,
                d_3 = :d_3,
                d_4 = :d_4,
                d_5 = :d_5,
                d_6 = :d_6,
                d_7 = :d_7,
                last_updated_by = :last_updated_by,
                last_update_date = :last_update_date
            WHERE
                work_rotation_line_id = :work_rotation_line_id
            """;

    static String deleteWorkRotationLineSql = """
            DELETE FROM sc_work_rotation_lines
            WHERE work_rotation_line_id = :work_rotation_line_id
            """;
}