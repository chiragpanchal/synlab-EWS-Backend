package com.ewsv3.ews.shiftbidding.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.shiftbidding.dto.ScheduleBidCycle;
import com.ewsv3.ews.shiftbidding.dto.ScheduleBidPattern;
import com.ewsv3.ews.shiftbidding.dto.ScheduleBidPatternSkill;

import static com.ewsv3.ews.shiftbidding.service.ScheduleBidCycleUtils.*;
import static com.ewsv3.ews.shiftbidding.service.ScheduleBidPatternUtils.*;

@Service
public class ScheduleBidCycleService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleBidCycleService.class);

    public List<ScheduleBidCycle> getScheduleBidCycles(Long profileId, JdbcClient jdbcClient) {
        logger.debug("Fetching schedule bid cycles for profileId: {}", profileId);

        List<ScheduleBidCycle> cycles = jdbcClient.sql(sqlGetScheduleBidCycles)
                .param("profileId", profileId)
                .query(ScheduleBidCycle.class)
                .list();

        // Load patterns for each cycle
        for (ScheduleBidCycle cycle : cycles) {
            List<ScheduleBidPattern> patterns = jdbcClient.sql(sqlGetScheduleBidPatterns)
                    .param("scheduleBidCycleId", cycle.getScheduleBidCycleId())
                    .query(ScheduleBidPattern.class)
                    .list();

            // Load skills for each pattern
            for (ScheduleBidPattern pattern : patterns) {
                List<ScheduleBidPatternSkill> skills = jdbcClient.sql(sqlGetScheduleBidPatternSkills)
                        .param("scheduleBidPatternId", pattern.getScheduleBidPatternId())
                        .query(ScheduleBidPatternSkill.class)
                        .list();
                pattern.setSkills(skills);
            }

            cycle.setPatterns(patterns);
        }

        logger.debug("Retrieved {} schedule bid cycles", cycles.size());
        return cycles;
    }

    public ScheduleBidCycle getScheduleBidCycleById(Long scheduleBidCycleId, JdbcClient jdbcClient) {
        logger.debug("Fetching schedule bid cycle by id: {}", scheduleBidCycleId);

        ScheduleBidCycle cycle = jdbcClient.sql(sqlGetScheduleBidCycleById)
                .param("scheduleBidCycleId", scheduleBidCycleId)
                .query(ScheduleBidCycle.class)
                .optional()
                .orElse(null);

        if (cycle != null) {
            // Load patterns for the cycle
            List<ScheduleBidPattern> patterns = jdbcClient.sql(sqlGetScheduleBidPatterns)
                    .param("scheduleBidCycleId", cycle.getScheduleBidCycleId())
                    .query(ScheduleBidPattern.class)
                    .list();

            // Load skills for each pattern
            for (ScheduleBidPattern pattern : patterns) {
                List<ScheduleBidPatternSkill> skills = jdbcClient.sql(sqlGetScheduleBidPatternSkills)
                        .param("scheduleBidPatternId", pattern.getScheduleBidPatternId())
                        .query(ScheduleBidPatternSkill.class)
                        .list();
                pattern.setSkills(skills);
            }

            cycle.setPatterns(patterns);
        }

        logger.debug("Retrieved schedule bid cycle: {}", cycle);
        return cycle;
    }

    @Transactional
    public DMLResponseDto saveScheduleBidCycle(Long userId, ScheduleBidCycle scheduleBidCycle,
            JdbcClient jdbcClient) {
        try {
            Long scheduleBidCycleId = scheduleBidCycle.getScheduleBidCycleId();

            if (scheduleBidCycleId == null) {
                // Insert new record
                scheduleBidCycleId = jdbcClient
                        .sql("SELECT schedule_bid_cycle_id_sq.NEXTVAL FROM dual")
                        .query(Long.class)
                        .single();

                logger.debug("Inserting new schedule bid cycle with id: {}", scheduleBidCycleId);

                int rowsInserted = jdbcClient.sql(sqlInsertScheduleBidCycle)
                        .param("scheduleBidCycleId", scheduleBidCycleId)
                        .param("profileId", scheduleBidCycle.getProfileId())
                        .param("validityStartDate", scheduleBidCycle.getValidityStartDate())
                        .param("validityEndDate", scheduleBidCycle.getValidityEndDate())
                        .param("bidDuration", scheduleBidCycle.getBidDuration())
                        .param("bidOpenDays", scheduleBidCycle.getBidOpenDays())
                        .param("createdBy", userId)
                        .param("lastUpdatedBy", userId)
                        .update();

                logger.debug("Inserted {} schedule bid cycle record(s)", rowsInserted);

                if (rowsInserted <= 0) {
                    return new DMLResponseDto("E", "Failed to create schedule bid cycle");
                }

            } else {
                // Update existing record
                logger.debug("Updating schedule bid cycle with id: {}", scheduleBidCycleId);

                int rowsUpdated = jdbcClient.sql(sqlUpdateScheduleBidCycle)
                        .param("scheduleBidCycleId", scheduleBidCycleId)
                        .param("profileId", scheduleBidCycle.getProfileId())
                        .param("validityStartDate", scheduleBidCycle.getValidityStartDate())
                        .param("validityEndDate", scheduleBidCycle.getValidityEndDate())
                        .param("bidDuration", scheduleBidCycle.getBidDuration())
                        .param("bidOpenDays", scheduleBidCycle.getBidOpenDays())
                        .param("lastUpdatedBy", userId)
                        .update();

                logger.debug("Updated {} schedule bid cycle record(s)", rowsUpdated);

                if (rowsUpdated <= 0) {
                    return new DMLResponseDto("E", "Schedule bid cycle not found or not updated");
                }
            }

            // Save patterns if provided
            if (scheduleBidCycle.getPatterns() != null && !scheduleBidCycle.getPatterns().isEmpty()) {
                logger.debug("Saving {} patterns for cycle id: {}",
                           scheduleBidCycle.getPatterns().size(), scheduleBidCycleId);

                for (ScheduleBidPattern pattern : scheduleBidCycle.getPatterns()) {
                    Long patternId = pattern.getScheduleBidPatternId();

                    // Set the cycle ID for the pattern
                    pattern.setScheduleBidCycleId(scheduleBidCycleId);

                    if (patternId == null) {
                        // Insert new pattern
                        patternId = jdbcClient
                                .sql("SELECT schedule_bid_pattern_id_sq.NEXTVAL FROM dual")
                                .query(Long.class)
                                .single();

                        jdbcClient.sql(sqlInsertScheduleBidPattern)
                                .param("scheduleBidPatternId", patternId)
                                .param("scheduleBidCycleId", scheduleBidCycleId)
                                .param("departmentId", pattern.getDepartmentId())
                                .param("jobTitleId", pattern.getJobTitleId())
                                .param("d1", pattern.getD1())
                                .param("d2", pattern.getD2())
                                .param("d3", pattern.getD3())
                                .param("d4", pattern.getD4())
                                .param("d5", pattern.getD5())
                                .param("d6", pattern.getD6())
                                .param("d7", pattern.getD7())
                                .param("createdBy", userId)
                                .param("lastUpdatedBy", userId)
                                .update();
                    } else {
                        // Update existing pattern
                        jdbcClient.sql(sqlUpdateScheduleBidPattern)
                                .param("scheduleBidPatternId", patternId)
                                .param("scheduleBidCycleId", scheduleBidCycleId)
                                .param("departmentId", pattern.getDepartmentId())
                                .param("jobTitleId", pattern.getJobTitleId())
                                .param("d1", pattern.getD1())
                                .param("d2", pattern.getD2())
                                .param("d3", pattern.getD3())
                                .param("d4", pattern.getD4())
                                .param("d5", pattern.getD5())
                                .param("d6", pattern.getD6())
                                .param("d7", pattern.getD7())
                                .param("lastUpdatedBy", userId)
                                .update();

                        // Delete existing skills
                        jdbcClient.sql(sqlDeleteScheduleBidPatternSkills)
                                .param("scheduleBidPatternId", patternId)
                                .update();
                    }

                    // Insert skills for the pattern
                    if (pattern.getSkills() != null && !pattern.getSkills().isEmpty()) {
                        for (ScheduleBidPatternSkill skill : pattern.getSkills()) {
                            Long skillId = jdbcClient
                                    .sql("SELECT schedule_bid_pattern_skill_id_sq.NEXTVAL FROM dual")
                                    .query(Long.class)
                                    .single();

                            jdbcClient.sql(sqlInsertScheduleBidPatternSkill)
                                    .param("scheduleBidPatternSkillId", skillId)
                                    .param("scheduleBidPatternId", patternId)
                                    .param("skillId", skill.getSkillId())
                                    .param("createdBy", userId)
                                    .param("lastUpdatedBy", userId)
                                    .update();
                        }
                    }
                }
            }

            return new DMLResponseDto("S",
                    "Schedule bid cycle saved successfully with ID: " + scheduleBidCycleId);

        } catch (Exception e) {
            logger.error("Error saving schedule bid cycle: {}", e.getMessage(), e);
            return new DMLResponseDto("E", "Error saving schedule bid cycle: " + e.getMessage());
        }
    }

    @Transactional
    public DMLResponseDto deleteScheduleBidCycle(Long scheduleBidCycleId, JdbcClient jdbcClient) {
        try {
            logger.debug("Deleting schedule bid cycle with id: {}", scheduleBidCycleId);

            // First, get all patterns for this cycle
            List<ScheduleBidPattern> patterns = jdbcClient.sql(sqlGetScheduleBidPatterns)
                    .param("scheduleBidCycleId", scheduleBidCycleId)
                    .query(ScheduleBidPattern.class)
                    .list();

            // Delete skills for each pattern
            for (ScheduleBidPattern pattern : patterns) {
                jdbcClient.sql(sqlDeleteScheduleBidPatternSkills)
                        .param("scheduleBidPatternId", pattern.getScheduleBidPatternId())
                        .update();
            }

            // Delete all patterns for this cycle
            jdbcClient.sql("DELETE FROM sc_schedule_bid_patterns WHERE schedule_bid_cycle_id = :scheduleBidCycleId")
                    .param("scheduleBidCycleId", scheduleBidCycleId)
                    .update();

            // Delete the cycle
            int rowsDeleted = jdbcClient.sql(sqlDeleteScheduleBidCycle)
                    .param("scheduleBidCycleId", scheduleBidCycleId)
                    .update();

            logger.debug("Deleted {} schedule bid cycle record(s)", rowsDeleted);

            if (rowsDeleted > 0) {
                return new DMLResponseDto("S", "Schedule bid cycle deleted successfully");
            } else {
                return new DMLResponseDto("E", "Schedule bid cycle not found");
            }

        } catch (Exception e) {
            logger.error("Error deleting schedule bid cycle: {}", e.getMessage(), e);
            return new DMLResponseDto("E", "Error deleting schedule bid cycle: " + e.getMessage());
        }
    }
}
