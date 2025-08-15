package com.ewsv3.ews.accessprofiles.dto;

import java.time.LocalDateTime;

public record UserProfileAssoc(
        Long userProfileAssocId,    // USER_PROFILE_ASSOC_ID
        Long userId,                // USER_ID
        Long profileId,             // PROFILE_ID
        String canCreate,           // CAN_CREATE (VARCHAR2(1))
        String canEdit,             // CAN_EDIT (VARCHAR2(1))
        String canDelete,           // CAN_DELETE (VARCHAR2(1))
        String canView,             // CAN_VIEW (VARCHAR2(1))
        String userType,            // USER_TYPE (VARCHAR2(1000))
        Long vacationRuleId,        // VACATION_RULE_ID (nullable)
        Long createdBy,             // CREATED_BY
        LocalDateTime createdOn,    // CREATED_ON (DATE)
        Long lastUpdatedBy,         // LAST_UPDATED_BY
        LocalDateTime lastUpdateDate // LAST_UPDATE_DATE (DATE)
) {
}
