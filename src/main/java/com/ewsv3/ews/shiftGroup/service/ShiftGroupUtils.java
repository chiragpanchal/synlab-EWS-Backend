package com.ewsv3.ews.shiftGroup.service;

public class ShiftGroupUtils {

    public static String getShiftGroupSQL = """
            SELECT
                scg.shift_group_id,
                scg.shift_group_name,
                scg.enable,
                scg.created_by,
                scg.created_on,
                scg.last_updated_by,
                scg.last_update_date,
                usr.full_name last_Updated_By_User_Name
              FROM
                sc_shift_group scg,
                sc_person_v usr
              WHERE
                usr.user_id =   scg.last_updated_by
                AND (:shiftGroupId = 0
                OR scg.shift_group_id = :shiftGroupId)
             ORDER BY
                scg.shift_group_name""";

    public static String insertShiftGroupSQL = """
            INSERT INTO sc_shift_group (
                    shift_group_id,
                    shift_group_name,
                    enable,
                    created_by,
                    last_updated_by
                ) VALUES (
                    :shiftGroupId,
                    :shiftGroupName,
                    :enable,
                    :createdBy,
                    :lastUpdatedBy
                )""";

    public static String updateShiftGroupSQL = """
            UPDATE sc_shift_group
               SET
                shift_group_name = :shiftGroupName,
                enable = :enable,
                last_updated_by = :lastUpdatedBy
             WHERE
                shift_group_id = :shiftGroupId""";

    public static String deleteShiftGroupSQL = "";

    public static String getShiftGroupShiftsSQL = """
            SELECT
                sgs.shift_group_work_shift_id,
                sgs.shift_group_id,
                sgs.work_duration_id,
                sgs.on_call,
                sgs.emergency,
                sgs.created_by,
                sgs.created_on,
                sgs.last_updated_by,
                sgs.last_update_date,
                usr.full_name last_Updated_By_User_Name
              FROM
                sc_shift_group_shifts sgs,
                sc_work_duration      swd,
                sc_person_v           usr
             WHERE
                    sgs.work_duration_id = swd.work_duration_id
                   AND sgs.shift_group_id = :shiftGroupId
                   AND (:shiftGroupWorkShiftId =0
                    OR sgs.shift_group_work_shift_id = :shiftGroupWorkShiftId)
                   AND usr.user_id    = sgs.last_updated_by
             ORDER BY
                swd.time_start""";

    public static String insertShiftGroupShiftsSQL = """
            INSERT INTO sc_shift_group_shifts (
                    shift_group_work_shift_id,
                    shift_group_id,
                    work_duration_id,
                    on_call,
                    emergency,
                    created_by,
                    last_updated_by
                ) VALUES (
                    :shiftGroupWorkShiftId,
                    :shiftGroupId,
                    :workDurationId,
                    :onCall,
                    :emergency,
                    :createdBy,
                    :lastUpdatedBy
                )""";

    public static String updateShiftGroupShiftsSQL = """
            UPDATE sc_shift_group_shifts sgs
               SET
                sgs.work_duration_id = :workDurationId,
                sgs.on_call = :onCall,
                sgs.emergency = :emergency,
                sgs.last_updated_by = :lastUpdatedBy
             WHERE
                sgs.shift_group_work_shift_id = :shiftGroupWorkShiftId""";

    public static String deleteShiftGroupShiftsSQL = """
            DELETE sc_shift_group_shifts
             WHERE
                shift_group_work_shift_id = :shiftGroupWorkShiftId""";


}
