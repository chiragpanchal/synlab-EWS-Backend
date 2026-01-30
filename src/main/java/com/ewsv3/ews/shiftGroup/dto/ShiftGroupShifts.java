package com.ewsv3.ews.shiftGroup.dto;

import java.time.LocalDateTime;

public record ShiftGroupShifts(
        Long shiftGroupWorkShiftId,
        Long shiftGroupId,
        Long workDurationId,
        Long onCall,
        Long emergency,
        Long createdBy,
        LocalDateTime createdOn,
        Long lastUpdatedBy,
        LocalDateTime lastUpdateDate,
        String lastUpdatedByUserName
) {
}
