package com.ewsv3.ews.accessprofiles.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AccessProfiles(
        Long profileId,
        String profileName,
        LocalDate startDate,
        LocalDate endDate,
        Long createdBy,
        LocalDateTime createdOn,
        Long lastUpdatedBy,
        LocalDateTime lastUpdateDate,
        String skipApproval
) {
    public AccessProfiles() {
        this(null, null, null, null, null, null, null, null, null);
    }
}
