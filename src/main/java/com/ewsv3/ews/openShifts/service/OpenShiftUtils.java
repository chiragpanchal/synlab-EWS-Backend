package com.ewsv3.ews.openShifts.service;

public class OpenShiftUtils {

    public static String GetOpenShiftsByProfile= """
            SELECT
                open_shift_id,
                start_date,
                end_date,
                demand_template_id,
                created_by,
                created_on,
                usr.full_name last_updated_by,
                last_update_date,
                profile_id,
                recalled
              FROM
                sc_open_shifts_h h,
                sc_person_v      usr
             WHERE
                    h.profile_id = :profileId
                   AND ( ( h.start_date BETWEEN :startDate AND :endDate
                           OR h.end_date BETWEEN :startDate AND :endDate )
                           OR :startDate BETWEEN h.start_date AND h.end_date )
                   AND usr.user_id = h.last_updated_by
             ORDER BY
                start_date DESC""";

    public static String GetOpenShiftsByOpenShiftId= """
            SELECT
                open_shift_id,
                start_date,
                end_date,
                demand_template_id,
                created_by,
                created_on,
                usr.full_name last_updated_by,
                last_update_date,
                profile_id,
                recalled
              FROM
                sc_open_shifts_h h,
                sc_person_v      usr
             WHERE
                    h.openShiftId = :openShiftId
                   AND usr.user_id = h.last_updated_by
             ORDER BY
                start_date DESC""";

    public static String GetOpenShiftLines= """
            SELECT
                l.open_shift_line_id,
                l.open_shift_id,
                l.demand_template_line_id,
                l.department_id,
                l.job_title_id,
                l.location_id,
                l.work_duration_id,
                l.sun,
                l.mon,
                l.tue,
                l.wed,
                l.thu,
                l.fri,
                l.sat,
                l.created_by,
                l.created_on,
                usr.full_name last_updated_by,
                l.last_update_date
              FROM
                sc_open_shifts_l l,
                sc_person_v      usr
             WHERE
                    l.open_shift_id = :openShiftId
                   AND usr.user_id = l.last_updated_by
             ORDER BY
                open_shift_line_id""";

    public static String GetOpenShiftDetails= """
            SELECT
                l.open_shift_line_id,
                l.open_shift_id,
                l.demand_template_line_id,
                l.department_id,
                l.job_title_id,
                l.location_id,
                l.work_duration_id,
                l.effective_date,
                l.fte_requested,
                l.created_by,
                l.created_on,
                usr.full_name last_updated_by,
                l.last_update_date
              FROM
                sc_open_shifts_d l,
                sc_person_v      usr
             WHERE
                    l.open_shift_id = :openShiftId
                   AND usr.user_id = l.last_updated_by
             ORDER BY
                open_shift_line_id""";

    public static String CreateOpenShiftHeader= """
            INSERT INTO sc_open_shifts_h (
                open_shift_id,
                start_date,
                end_date,
                demand_template_id,
                created_by,
                created_on,
                last_updated_by,
                last_update_date,
                profile_id,
                recalled
            ) VALUES (
                :openShiftId,
                :startDate,
                :endDate,
                :demandTemplateId,
                :createdBy,
                :createdOn,
                :lastUpdatedBy,
                :lastUpdateDate,
                :profileId,
                :recalled
            )""";

    public static String UpdateOpenShiftHeader= """
            UPDATE sc_open_shifts_h h
               SET
                h.recalled = 'Y',
                h.last_updated_by = :lastUpdatedBy,
                h.last_update_date = :lastUpdateDate
             WHERE
                open_shift_id = :openShiftId""";

    public static String CreateOpenShiftLine= """
            INSERT INTO sc_open_shifts_l (
                open_shift_id,
                demand_template_line_id,
                department_id,
                job_title_id,
                location_id,
                work_duration_id,
                sun,
                mon,
                tue,
                wed,
                thu,
                fri,
                sat,
                created_by,
                created_on,
                last_updated_by,
                last_update_date
            ) VALUES (
                :openShiftId,
                :demandTemplateLineId,
                :departmentId,
                :jobTitleId,
                :locationId,
                :workDurationId,
                :sun,
                :mon,
                :tue,
                :wed,
                :thu,
                :fri,
                :sat,
                :createdBy,
                :createdOn,
                :lastUpdatedBy,
                :lastUpdateDate
            )""";

    public static String CreateOpenShiftDetail= """
            INSERT INTO sc_open_shifts_d (
                open_shift_id,
                demand_template_line_id,
                department_id,
                job_title_id,
                location_id,
                work_duration_id,
                effective_date,
                fte_requested,
                created_by,
                created_on,
                last_updated_by,
                last_update_date
            ) VALUES (
                :openShiftId,
                :demandTemplateLineId,
                :departmentId,
                :jobTitleId,
                :locationId,
                :workDurationId,
                :effectiveDate,
                :fteRequested,
                :createdBy,
                :createdOn,
                :lastUpdatedBy,
                :lastUpdateDate
            )""";

    public static String UpdateOpenShiftDetail= """
            UPDATE sc_open_shifts_d l
               SET
                department_id = :departmentId,
                job_title_id = :jobTitleId,
                location_id = :locationId,
                work_duration_id = :workDurationId,
                effective_date = :effectiveDate,
                fteRequested = :fteRequested
                last_updated_by = :lastUpdatedBy,
                last_update_date = :lastUpdateDate
             WHERE
                l.open_shift_line_id = :openShiftLineId""";

    public static String UpdateOpenShiftLine= """
            UPDATE sc_open_shifts_l l
               SET
                department_id = :departmentId,
                job_title_id = :jobTitleId,
                location_id = :locationId,
                work_duration_id = :workDurationId,
                sun = :sun,
                mon = :mon,
                tue = :tue,
                wed = :wed,
                thu = :thu,
                fri = :fri,
                sat = :sat,
                last_updated_by = :lastUpdatedBy,
                last_update_date = :lastUpdateDate
             WHERE
                l.open_shift_line_id = :openShiftLineId""";

    public static String DeleteOpenShiftLine= """
            DELETE sc_open_shifts_l l
             WHERE
                l.open_shift_line_id = :openShiftLineId;""";

    public static String DeleteOpenShiftdetail= """
            DELETE sc_open_shifts_d l
             WHERE
                l.open_shift_line_id = :openShiftLineId;""";


}
