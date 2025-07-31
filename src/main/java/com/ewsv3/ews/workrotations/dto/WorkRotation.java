package com.ewsv3.ews.workrotations.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record WorkRotation(
        Long workRotationId,
        String workRotationName,
        LocalDate startDate,
        Integer iterations,
        Long createdBy,
        LocalDateTime createdOn,
        Long lastUpdatedBy,
        LocalDateTime lastUpdateDate,
        LocalDate expiryDate,
        String colorCode,
        String foreverFlag,
        String createdByName,
        String lastUpdatedByName) {

}
