package com.ewsv3.ews.rules.dto;

import java.time.LocalDateTime;

public record DemandTemplateLine(
        Long demandTemplateLineId,
        Long demandTemplateId,
        Long departmentId,
        Long jobTitleId,
        Long locationId,
        Long workDurationId,
//        LocalDateTime timeStart,
//        LocalDateTime timeEnd,
        Integer sun,
        Integer mon,
        Integer tue,
        Integer wed,
        Integer thu,
        Integer fri,
        Integer sat,
        Long createdBy,
        LocalDateTime createdOn,
        Long lastUpdatedBy,
        LocalDateTime lastUpdateDate
) {
}
