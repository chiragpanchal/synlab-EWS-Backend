package com.ewsv3.ews.shiftbidding.service;

public class BiddingGroupUtils {

    public static final String sqlGetBiddingGroups = """
            SELECT
                bidding_group_id AS biddingGroupId,
                profile_id AS profileId,
                seq,
                open_days AS openDays,
                last_unique_bids_check AS lastUniqueBidsCheck,
                bid_pref AS bidPref,
                created_by AS createdBy,
                created_on AS createdOn,
                last_updated_by AS lastUpdatedBy,
                last_update_date AS lastUpdateDate
            FROM sc_bidding_groups
            WHERE profile_id = :profileId
            ORDER BY seq
            """;

    public static final String sqlGetBiddingGroupById = """
            SELECT
                bidding_group_id AS biddingGroupId,
                profile_id AS profileId,
                seq,
                open_days AS openDays,
                last_unique_bids_check AS lastUniqueBidsCheck,
                bid_pref AS bidPref,
                created_by AS createdBy,
                created_on AS createdOn,
                last_updated_by AS lastUpdatedBy,
                last_update_date AS lastUpdateDate
            FROM sc_bidding_groups
            WHERE bidding_group_id = :biddingGroupId
            """;

    public static final String sqlInsertBiddingGroup = """
            INSERT INTO sc_bidding_groups (
                bidding_group_id,
                profile_id,
                seq,
                open_days,
                last_unique_bids_check,
                bid_pref,
                created_by,
                created_on,
                last_updated_by,
                last_update_date
            ) VALUES (
                :biddingGroupId,
                :profileId,
                :seq,
                :openDays,
                :lastUniqueBidsCheck,
                :bidPref,
                :createdBy,
                SYSDATE,
                :lastUpdatedBy,
                SYSDATE
            )
            """;

    public static final String sqlUpdateBiddingGroup = """
            UPDATE sc_bidding_groups
            SET profile_id = :profileId,
                seq = :seq,
                open_days = :openDays,
                last_unique_bids_check = :lastUniqueBidsCheck,
                bid_pref = :bidPref,
                last_updated_by = :lastUpdatedBy,
                last_update_date = SYSDATE
            WHERE bidding_group_id = :biddingGroupId
            """;

    public static final String sqlDeleteBiddingGroup = """
            DELETE FROM sc_bidding_groups
            WHERE bidding_group_id = :biddingGroupId
            """;

    // Person Preferences queries
    public static final String sqlGetBiddingPersonPrefs = """
            SELECT
                bidding_person_pref_id AS biddingPersonPrefId,
                bidding_group_id AS biddingGroupId,
                person_id AS personId,
                priority_level AS priorityLevel,
                last_unique_bids_check AS lastUniqueBidsCheck,
                created_by AS createdBy,
                created_on AS createdOn,
                last_updated_by AS lastUpdatedBy,
                last_update_date AS lastUpdateDate
            FROM sc_bidding_person_pref
            WHERE bidding_group_id = :biddingGroupId
            ORDER BY priority_level
            """;

    public static final String sqlInsertBiddingPersonPref = """
            INSERT INTO sc_bidding_person_pref (
                bidding_person_pref_id,
                bidding_group_id,
                person_id,
                priority_level,
                last_unique_bids_check,
                created_by,
                created_on,
                last_updated_by,
                last_update_date
            ) VALUES (
                :biddingPersonPrefId,
                :biddingGroupId,
                :personId,
                :priorityLevel,
                :lastUniqueBidsCheck,
                :createdBy,
                SYSDATE,
                :lastUpdatedBy,
                SYSDATE
            )
            """;

    public static final String sqlDeleteBiddingPersonPrefs = """
            DELETE FROM sc_bidding_person_pref
            WHERE bidding_group_id = :biddingGroupId
            """;
}
