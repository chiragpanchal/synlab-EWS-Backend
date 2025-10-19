package com.ewsv3.ews.shiftbidding.service;

public class ScheduleBidPatternUtils {

    public static final String sqlGetScheduleBidPatterns = """
            SELECT
                schedule_bid_pattern_id AS scheduleBidPatternId,
                schedule_bid_cycle_id AS scheduleBidCycleId,
                department_id AS departmentId,
                job_title_id AS jobTitleId,
                d_1 AS d1,
                d_2 AS d2,
                d_3 AS d3,
                d_4 AS d4,
                d_5 AS d5,
                d_6 AS d6,
                d_7 AS d7,
                created_by AS createdBy,
                created_on AS createdOn,
                last_updated_by AS lastUpdatedBy,
                last_update_date AS lastUpdateDate
            FROM sc_schedule_bid_patterns
            WHERE schedule_bid_cycle_id = :scheduleBidCycleId
            ORDER BY schedule_bid_pattern_id
            """;

    public static final String sqlGetScheduleBidPatternById = """
            SELECT
                schedule_bid_pattern_id AS scheduleBidPatternId,
                schedule_bid_cycle_id AS scheduleBidCycleId,
                department_id AS departmentId,
                job_title_id AS jobTitleId,
                d_1 AS d1,
                d_2 AS d2,
                d_3 AS d3,
                d_4 AS d4,
                d_5 AS d5,
                d_6 AS d6,
                d_7 AS d7,
                created_by AS createdBy,
                created_on AS createdOn,
                last_updated_by AS lastUpdatedBy,
                last_update_date AS lastUpdateDate
            FROM sc_schedule_bid_patterns
            WHERE schedule_bid_pattern_id = :scheduleBidPatternId
            """;

    public static final String sqlInsertScheduleBidPattern = """
            INSERT INTO sc_schedule_bid_patterns (
                schedule_bid_pattern_id,
                schedule_bid_cycle_id,
                department_id,
                job_title_id,
                d_1,
                d_2,
                d_3,
                d_4,
                d_5,
                d_6,
                d_7,
                created_by,
                created_on,
                last_updated_by,
                last_update_date
            ) VALUES (
                :scheduleBidPatternId,
                :scheduleBidCycleId,
                :departmentId,
                :jobTitleId,
                :d1,
                :d2,
                :d3,
                :d4,
                :d5,
                :d6,
                :d7,
                :createdBy,
                SYSDATE,
                :lastUpdatedBy,
                SYSDATE
            )
            """;

    public static final String sqlUpdateScheduleBidPattern = """
            UPDATE sc_schedule_bid_patterns
            SET schedule_bid_cycle_id = :scheduleBidCycleId,
                department_id = :departmentId,
                job_title_id = :jobTitleId,
                d_1 = :d1,
                d_2 = :d2,
                d_3 = :d3,
                d_4 = :d4,
                d_5 = :d5,
                d_6 = :d6,
                d_7 = :d7,
                last_updated_by = :lastUpdatedBy,
                last_update_date = SYSDATE
            WHERE schedule_bid_pattern_id = :scheduleBidPatternId
            """;

    public static final String sqlDeleteScheduleBidPattern = """
            DELETE FROM sc_schedule_bid_patterns
            WHERE schedule_bid_pattern_id = :scheduleBidPatternId
            """;

    // Pattern Skills queries
    public static final String sqlGetScheduleBidPatternSkills = """
            SELECT
                schedule_bid_pattern_skill_id AS scheduleBidPatternSkillId,
                schedule_bid_pattern_id AS scheduleBidPatternId,
                skill_id AS skillId,
                created_by AS createdBy,
                created_on AS createdOn,
                last_updated_by AS lastUpdatedBy,
                last_update_date AS lastUpdateDate
            FROM sc_schedule_bid_pattern_skills
            WHERE schedule_bid_pattern_id = :scheduleBidPatternId
            ORDER BY skill_id
            """;

    public static final String sqlInsertScheduleBidPatternSkill = """
            INSERT INTO sc_schedule_bid_pattern_skills (
                schedule_bid_pattern_skill_id,
                schedule_bid_pattern_id,
                skill_id,
                created_by,
                created_on,
                last_updated_by,
                last_update_date
            ) VALUES (
                :scheduleBidPatternSkillId,
                :scheduleBidPatternId,
                :skillId,
                :createdBy,
                SYSDATE,
                :lastUpdatedBy,
                SYSDATE
            )
            """;

    public static final String sqlDeleteScheduleBidPatternSkills = """
            DELETE FROM sc_schedule_bid_pattern_skills
            WHERE schedule_bid_pattern_id = :scheduleBidPatternId
            """;
}
