package com.ewsv3.ews.schedulerules.dto.req;

import java.time.LocalDate;

public record ScheduleRuleRequest(
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
        Integer maxContRestDays
) {
    public ScheduleRuleRequest() {
        this(null, null, null, null, null, null, null, null, null, null, null, null);
    }
}