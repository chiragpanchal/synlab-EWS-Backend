package com.ewsv3.ews.shiftbidding.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.shiftbidding.dto.ScheduleBidPattern;
import com.ewsv3.ews.shiftbidding.dto.ScheduleBidPatternSkill;

import static com.ewsv3.ews.shiftbidding.service.ScheduleBidPatternUtils.*;

@Service
public class ScheduleBidPatternService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleBidPatternService.class);

    public List<ScheduleBidPattern> getScheduleBidPatterns(Long scheduleBidCycleId, JdbcClient jdbcClient) {
        logger.debug("Fetching schedule bid patterns for scheduleBidCycleId: {}", scheduleBidCycleId);

        List<ScheduleBidPattern> patterns = jdbcClient.sql(sqlGetScheduleBidPatterns)
                .param("scheduleBidCycleId", scheduleBidCycleId)
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

        logger.debug("Retrieved {} schedule bid patterns", patterns.size());
        return patterns;
    }

    public ScheduleBidPattern getScheduleBidPatternById(Long scheduleBidPatternId, JdbcClient jdbcClient) {
        logger.debug("Fetching schedule bid pattern by id: {}", scheduleBidPatternId);

        ScheduleBidPattern pattern = jdbcClient.sql(sqlGetScheduleBidPatternById)
                .param("scheduleBidPatternId", scheduleBidPatternId)
                .query(ScheduleBidPattern.class)
                .optional()
                .orElse(null);

        if (pattern != null) {
            // Load skills for the pattern
            List<ScheduleBidPatternSkill> skills = jdbcClient.sql(sqlGetScheduleBidPatternSkills)
                    .param("scheduleBidPatternId", pattern.getScheduleBidPatternId())
                    .query(ScheduleBidPatternSkill.class)
                    .list();
            pattern.setSkills(skills);
        }

        logger.debug("Retrieved schedule bid pattern: {}", pattern);
        return pattern;
    }

    @Transactional
    public DMLResponseDto saveScheduleBidPattern(Long userId, ScheduleBidPattern scheduleBidPattern,
                                                 JdbcClient jdbcClient) {
        try {
            Long scheduleBidPatternId = scheduleBidPattern.getScheduleBidPatternId();

            if (scheduleBidPatternId == null) {
                // Insert new record
                scheduleBidPatternId = jdbcClient
                        .sql("SELECT schedule_bid_pattern_id_sq.NEXTVAL FROM dual")
                        .query(Long.class)
                        .single();

                logger.debug("Inserting new schedule bid pattern with id: {}", scheduleBidPatternId);

                int rowsInserted = jdbcClient.sql(sqlInsertScheduleBidPattern)
                        .param("scheduleBidPatternId", scheduleBidPatternId)
                        .param("scheduleBidCycleId", scheduleBidPattern.getScheduleBidCycleId())
                        .param("departmentId", scheduleBidPattern.getDepartmentId())
                        .param("jobTitleId", scheduleBidPattern.getJobTitleId())
                        .param("d1", scheduleBidPattern.getD1())
                        .param("d2", scheduleBidPattern.getD2())
                        .param("d3", scheduleBidPattern.getD3())
                        .param("d4", scheduleBidPattern.getD4())
                        .param("d5", scheduleBidPattern.getD5())
                        .param("d6", scheduleBidPattern.getD6())
                        .param("d7", scheduleBidPattern.getD7())
                        .param("createdBy", userId)
                        .param("lastUpdatedBy", userId)
                        .update();

                logger.debug("Inserted {} schedule bid pattern record(s)", rowsInserted);

                if (rowsInserted <= 0) {
                    return new DMLResponseDto("E", "Failed to create schedule bid pattern");
                }

            } else {
                // Update existing record
                logger.debug("Updating schedule bid pattern with id: {}", scheduleBidPatternId);

                int rowsUpdated = jdbcClient.sql(sqlUpdateScheduleBidPattern)
                        .param("scheduleBidPatternId", scheduleBidPatternId)
                        .param("scheduleBidCycleId", scheduleBidPattern.getScheduleBidCycleId())
                        .param("departmentId", scheduleBidPattern.getDepartmentId())
                        .param("jobTitleId", scheduleBidPattern.getJobTitleId())
                        .param("d1", scheduleBidPattern.getD1())
                        .param("d2", scheduleBidPattern.getD2())
                        .param("d3", scheduleBidPattern.getD3())
                        .param("d4", scheduleBidPattern.getD4())
                        .param("d5", scheduleBidPattern.getD5())
                        .param("d6", scheduleBidPattern.getD6())
                        .param("d7", scheduleBidPattern.getD7())
                        .param("lastUpdatedBy", userId)
                        .update();

                logger.debug("Updated {} schedule bid pattern record(s)", rowsUpdated);

                if (rowsUpdated <= 0) {
                    return new DMLResponseDto("E", "Schedule bid pattern not found or not updated");
                }

                // Delete existing skills before inserting new ones
                jdbcClient.sql(sqlDeleteScheduleBidPatternSkills)
                        .param("scheduleBidPatternId", scheduleBidPatternId)
                        .update();
            }

            // Insert skills if provided
            if (scheduleBidPattern.getSkills() != null && !scheduleBidPattern.getSkills().isEmpty()) {
                logger.debug("Inserting {} skills for pattern id: {}",
                           scheduleBidPattern.getSkills().size(), scheduleBidPatternId);

                for (ScheduleBidPatternSkill skill : scheduleBidPattern.getSkills()) {
                    Long skillId = jdbcClient
                            .sql("SELECT schedule_bid_pattern_skill_id_sq.NEXTVAL FROM dual")
                            .query(Long.class)
                            .single();

                    jdbcClient.sql(sqlInsertScheduleBidPatternSkill)
                            .param("scheduleBidPatternSkillId", skillId)
                            .param("scheduleBidPatternId", scheduleBidPatternId)
                            .param("skillId", skill.getSkillId())
                            .param("createdBy", userId)
                            .param("lastUpdatedBy", userId)
                            .update();
                }
            }

            return new DMLResponseDto("S",
                    "Schedule bid pattern saved successfully with ID: " + scheduleBidPatternId);

        } catch (Exception e) {
            logger.error("Error saving schedule bid pattern: {}", e.getMessage(), e);
            return new DMLResponseDto("E", "Error saving schedule bid pattern: " + e.getMessage());
        }
    }

    @Transactional
    public DMLResponseDto deleteScheduleBidPattern(Long scheduleBidPatternId, JdbcClient jdbcClient) {
        try {
            logger.debug("Deleting schedule bid pattern with id: {}", scheduleBidPatternId);

            // Delete associated skills first (due to foreign key constraint)
            jdbcClient.sql(sqlDeleteScheduleBidPatternSkills)
                    .param("scheduleBidPatternId", scheduleBidPatternId)
                    .update();

            // Delete the pattern
            int rowsDeleted = jdbcClient.sql(sqlDeleteScheduleBidPattern)
                    .param("scheduleBidPatternId", scheduleBidPatternId)
                    .update();

            logger.debug("Deleted {} schedule bid pattern record(s)", rowsDeleted);

            if (rowsDeleted > 0) {
                return new DMLResponseDto("S", "Schedule bid pattern deleted successfully");
            } else {
                return new DMLResponseDto("E", "Schedule bid pattern not found");
            }

        } catch (Exception e) {
            logger.error("Error deleting schedule bid pattern: {}", e.getMessage(), e);
            return new DMLResponseDto("E", "Error deleting schedule bid pattern: " + e.getMessage());
        }
    }
}
