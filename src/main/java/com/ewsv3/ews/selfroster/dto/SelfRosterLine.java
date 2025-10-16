package com.ewsv3.ews.selfroster.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SelfRosterLine(
        Long selfRosterLineId,
        Long selfRosterId,
        Long d_1,
        Long d_2,
        Long d_3,
        Long d_4,
        Long d_5,
        Long d_6,
        Long d_7,
        Long personRosterId_1,
        Long personRosterId_2,
        Long personRosterId_3,
        Long personRosterId_4,
        Long personRosterId_5,
        Long personRosterId_6,
        Long personRosterId_7,
        Long createdBy,
        LocalDateTime createdOn,
        Long lastUpdatedBy,
        LocalDateTime lastUpdateDate,
        LocalDate startDate,
        LocalDate endDate) {

}
