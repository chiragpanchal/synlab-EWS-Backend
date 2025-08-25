package com.ewsv3.ews.schedulerules.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ScheduleRuleDto(
        Long scheduleRuleId,
        Long profileId,
        LocalDate validFrom,
        LocalDate validTo,
        Double maxHrsPerDay,
        Double minHrsPerWeek,
        Double maxHrsPerWeek,
        Double minHrsPerMonth,
        Double maxHrsPerMonth,
        Double shiftGap,
        Integer minRestDaysPerWeek,
        Integer maxContShiftDays,
        Integer maxContRestDays,
        Long createdBy,
        LocalDateTime createdOn,
        Long lastUpdatedBy,
        LocalDateTime lastUpdateDate
) {
    public ScheduleRuleDto() {
        this(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }
}