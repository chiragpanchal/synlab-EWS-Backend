package com.ewsv3.ews.setup.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record Skills(
        Long skillId,
        String skill,
        String comments,
        Long createdBy,
        LocalDateTime createdOn,
        Long lastUpdatedBy,
        LocalDateTime lastUpdateDate,
        LocalDate dateFrom,
        LocalDate dateTo) {

}
