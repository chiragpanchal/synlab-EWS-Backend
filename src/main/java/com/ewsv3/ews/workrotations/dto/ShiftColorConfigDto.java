package com.ewsv3.ews.workrotations.dto;

import java.time.LocalDateTime;

public record ShiftColorConfigDto(
        Long shiftColorConfigId,
        Double startTime,
        Double endTime,
        String color,
        Long createdBy,
        LocalDateTime createdOn,
        Long lastUpdatedBy,
        LocalDateTime lastUpdateDate
) {
}
