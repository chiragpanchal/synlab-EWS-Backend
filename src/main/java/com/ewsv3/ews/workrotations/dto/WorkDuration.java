package com.ewsv3.ews.workrotations.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record WorkDuration(
        Long workDurationId,
        String workDurationCode,
        String workDurationName,
        LocalDate validFrom,
        LocalDate validTo,
        LocalDateTime timeStart,
        LocalDateTime breakStart,
        LocalDateTime breakEnd,
        LocalDateTime timeEnd,
        Long enterpriseId,
        String mon,
        String tue,
        String wed,
        String thu,
        String fri,
        String sat,
        String sun,
        String colorCode,
        Long duration,
        Long workDurationCategoryId,
        String exceptionEvents,
        Long minWorkHrs,
        Long maxWorkHrs,
        String workUnit,
        Long hcmScheduleId,
        String erosterCode,
        Long breakMins,
        Long createdBy,
        LocalDateTime createdOn,
        Long lastUpdatedBy,
        LocalDateTime lastUpdateDate,
        String createdByName,
        String lastUpdatedByName,
        String timeHour
) {
}
