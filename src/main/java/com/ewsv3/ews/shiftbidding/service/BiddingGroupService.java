package com.ewsv3.ews.shiftbidding.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.shiftbidding.dto.BiddingGroup;
import com.ewsv3.ews.shiftbidding.dto.BiddingPersonPref;

import static com.ewsv3.ews.shiftbidding.service.BiddingGroupUtils.*;

@Service
public class BiddingGroupService {

    private static final Logger logger = LoggerFactory.getLogger(BiddingGroupService.class);

    public List<BiddingGroup> getBiddingGroups(Long profileId, JdbcClient jdbcClient) {
        logger.debug("Fetching bidding groups for profileId: {}", profileId);

        List<BiddingGroup> groups = jdbcClient.sql(sqlGetBiddingGroups)
                .param("profileId", profileId)
                .query(BiddingGroup.class)
                .list();

        // Load person preferences for each group
        for (BiddingGroup group : groups) {
            List<BiddingPersonPref> personPrefs = jdbcClient.sql(sqlGetBiddingPersonPrefs)
                    .param("biddingGroupId", group.getBiddingGroupId())
                    .query(BiddingPersonPref.class)
                    .list();
            group.setPersonPreferences(personPrefs);
        }

        logger.debug("Retrieved {} bidding groups", groups.size());
        return groups;
    }

    public BiddingGroup getBiddingGroupById(Long biddingGroupId, JdbcClient jdbcClient) {
        logger.debug("Fetching bidding group by id: {}", biddingGroupId);

        BiddingGroup group = jdbcClient.sql(sqlGetBiddingGroupById)
                .param("biddingGroupId", biddingGroupId)
                .query(BiddingGroup.class)
                .optional()
                .orElse(null);

        if (group != null) {
            // Load person preferences for the group
            List<BiddingPersonPref> personPrefs = jdbcClient.sql(sqlGetBiddingPersonPrefs)
                    .param("biddingGroupId", group.getBiddingGroupId())
                    .query(BiddingPersonPref.class)
                    .list();
            group.setPersonPreferences(personPrefs);
        }

        logger.debug("Retrieved bidding group: {}", group);
        return group;
    }

    @Transactional
    public DMLResponseDto saveBiddingGroup(Long userId, BiddingGroup biddingGroup, JdbcClient jdbcClient) {
        try {
            Long biddingGroupId = biddingGroup.getBiddingGroupId();

            if (biddingGroupId == null) {
                // Insert new record
                biddingGroupId = jdbcClient
                        .sql("SELECT bidding_group_id_sq.NEXTVAL FROM dual")
                        .query(Long.class)
                        .single();

                logger.debug("Inserting new bidding group with id: {}", biddingGroupId);

                int rowsInserted = jdbcClient.sql(sqlInsertBiddingGroup)
                        .param("biddingGroupId", biddingGroupId)
                        .param("profileId", biddingGroup.getProfileId())
                        .param("seq", biddingGroup.getSeq())
                        .param("openDays", biddingGroup.getOpenDays())
                        .param("lastUniqueBidsCheck", biddingGroup.getLastUniqueBidsCheck())
                        .param("bidPref", biddingGroup.getBidPref())
                        .param("createdBy", userId)
                        .param("lastUpdatedBy", userId)
                        .update();

                logger.debug("Inserted {} bidding group record(s)", rowsInserted);

                if (rowsInserted <= 0) {
                    return new DMLResponseDto("E", "Failed to create bidding group");
                }

            } else {
                // Update existing record
                logger.debug("Updating bidding group with id: {}", biddingGroupId);

                int rowsUpdated = jdbcClient.sql(sqlUpdateBiddingGroup)
                        .param("biddingGroupId", biddingGroupId)
                        .param("profileId", biddingGroup.getProfileId())
                        .param("seq", biddingGroup.getSeq())
                        .param("openDays", biddingGroup.getOpenDays())
                        .param("lastUniqueBidsCheck", biddingGroup.getLastUniqueBidsCheck())
                        .param("bidPref", biddingGroup.getBidPref())
                        .param("lastUpdatedBy", userId)
                        .update();

                logger.debug("Updated {} bidding group record(s)", rowsUpdated);

                if (rowsUpdated <= 0) {
                    return new DMLResponseDto("E", "Bidding group not found or not updated");
                }

                // Delete existing person preferences before inserting new ones
                jdbcClient.sql(sqlDeleteBiddingPersonPrefs)
                        .param("biddingGroupId", biddingGroupId)
                        .update();
            }

            // Insert person preferences if provided
            if (biddingGroup.getPersonPreferences() != null && !biddingGroup.getPersonPreferences().isEmpty()) {
                logger.debug("Inserting {} person preferences for group id: {}",
                           biddingGroup.getPersonPreferences().size(), biddingGroupId);

                for (BiddingPersonPref pref : biddingGroup.getPersonPreferences()) {
                    Long prefId = jdbcClient
                            .sql("SELECT bidding_person_pref_id_sq.NEXTVAL FROM dual")
                            .query(Long.class)
                            .single();

                    jdbcClient.sql(sqlInsertBiddingPersonPref)
                            .param("biddingPersonPrefId", prefId)
                            .param("biddingGroupId", biddingGroupId)
                            .param("personId", pref.getPersonId())
                            .param("priorityLevel", pref.getPriorityLevel())
                            .param("lastUniqueBidsCheck", pref.getLastUniqueBidsCheck())
                            .param("createdBy", userId)
                            .param("lastUpdatedBy", userId)
                            .update();
                }
            }

            return new DMLResponseDto("S", "Bidding group saved successfully with ID: " + biddingGroupId);

        } catch (Exception e) {
            logger.error("Error saving bidding group: {}", e.getMessage(), e);
            return new DMLResponseDto("E", "Error saving bidding group: " + e.getMessage());
        }
    }

    @Transactional
    public DMLResponseDto deleteBiddingGroup(Long biddingGroupId, JdbcClient jdbcClient) {
        try {
            logger.debug("Deleting bidding group with id: {}", biddingGroupId);

            // Delete associated person preferences first (due to foreign key constraint)
            jdbcClient.sql(sqlDeleteBiddingPersonPrefs)
                    .param("biddingGroupId", biddingGroupId)
                    .update();

            // Delete the group
            int rowsDeleted = jdbcClient.sql(sqlDeleteBiddingGroup)
                    .param("biddingGroupId", biddingGroupId)
                    .update();

            logger.debug("Deleted {} bidding group record(s)", rowsDeleted);

            if (rowsDeleted > 0) {
                return new DMLResponseDto("S", "Bidding group deleted successfully");
            } else {
                return new DMLResponseDto("E", "Bidding group not found");
            }

        } catch (Exception e) {
            logger.error("Error deleting bidding group: {}", e.getMessage(), e);
            return new DMLResponseDto("E", "Error deleting bidding group: " + e.getMessage());
        }
    }
}
