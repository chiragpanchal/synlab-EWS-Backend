package com.ewsv3.ews.rules.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DemandTemplate(
        Long demandTemplateId,
        String templateName,
        LocalDate validTo,
        Long profileId,
        Long createdBy,
        LocalDateTime createdOn,
        Long lastUpdatedBy,
        LocalDateTime lastUpdateDate
) {
}
