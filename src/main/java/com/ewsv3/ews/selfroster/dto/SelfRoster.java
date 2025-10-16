package com.ewsv3.ews.selfroster.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SelfRoster(
        Long selfRosterId,
        LocalDate fromDate,
        LocalDate toDate,
        Long personId,
        Long departmentId,
        Long jobTitleId,
        Long workLocationId,
        String comments,
        String status,
        Long itemKey,
        Long createdBy,
        LocalDateTime createdOn,
        Long lastUpdatedBy,
        LocalDateTime lastUpdateDate) {

}
