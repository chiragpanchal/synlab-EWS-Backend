package com.ewsv3.ews.accessprofiles.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AccessProfiles(
        Long profileId,
        String profileName,
        LocalDate startDate,
        LocalDate endDate,
        String readOnly,
        String allowOverlapShifts,
        String allowOvertimeShifts,
        String allowOncallShifts,
        Long weekStartsOn,
        String allowOpenShifts,
        Long rosterUpdateNotAllow,
        Long createdBy,
        LocalDateTime createdOn,
        Long lastUpdatedBy,
        LocalDateTime lastUpdateDate,
        String skipApproval
) {
    public AccessProfiles() {
        this(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }
}
