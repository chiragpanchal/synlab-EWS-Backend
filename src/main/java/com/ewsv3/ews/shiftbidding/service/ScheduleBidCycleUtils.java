package com.ewsv3.ews.shiftbidding.service;

public class ScheduleBidCycleUtils {

    public static final String sqlGetScheduleBidCycles = """
            SELECT
                schedule_bid_cycle_id AS scheduleBidCycleId,
                profile_id AS profileId,
                validity_start_date AS validityStartDate,
                validity_end_date AS validityEndDate,
                bid_duration AS bidDuration,
                bid_open_days AS bidOpenDays,
                created_by AS createdBy,
                created_on AS createdOn,
                last_updated_by AS lastUpdatedBy,
                last_update_date AS lastUpdateDate
            FROM sc_schedule_bid_cycles
            WHERE profile_id = :profileId
            ORDER BY validity_start_date DESC
            """;

    public static final String sqlGetScheduleBidCycleById = """
            SELECT
                schedule_bid_cycle_id AS scheduleBidCycleId,
                profile_id AS profileId,
                validity_start_date AS validityStartDate,
                validity_end_date AS validityEndDate,
                bid_duration AS bidDuration,
                bid_open_days AS bidOpenDays,
                created_by AS createdBy,
                created_on AS createdOn,
                last_updated_by AS lastUpdatedBy,
                last_update_date AS lastUpdateDate
            FROM sc_schedule_bid_cycles
            WHERE schedule_bid_cycle_id = :scheduleBidCycleId
            """;

    public static final String sqlInsertScheduleBidCycle = """
            INSERT INTO sc_schedule_bid_cycles (
                schedule_bid_cycle_id,
                profile_id,
                validity_start_date,
                validity_end_date,
                bid_duration,
                bid_open_days,
                created_by,
                created_on,
                last_updated_by,
                last_update_date
            ) VALUES (
                :scheduleBidCycleId,
                :profileId,
                :validityStartDate,
                :validityEndDate,
                :bidDuration,
                :bidOpenDays,
                :createdBy,
                SYSDATE,
                :lastUpdatedBy,
                SYSDATE
            )
            """;

    public static final String sqlUpdateScheduleBidCycle = """
            UPDATE sc_schedule_bid_cycles
            SET profile_id = :profileId,
                validity_start_date = :validityStartDate,
                validity_end_date = :validityEndDate,
                bid_duration = :bidDuration,
                bid_open_days = :bidOpenDays,
                last_updated_by = :lastUpdatedBy,
                last_update_date = SYSDATE
            WHERE schedule_bid_cycle_id = :scheduleBidCycleId
            """;

    public static final String sqlDeleteScheduleBidCycle = """
            DELETE FROM sc_schedule_bid_cycles
            WHERE schedule_bid_cycle_id = :scheduleBidCycleId
            """;
}
