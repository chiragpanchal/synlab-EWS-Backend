package com.ewsv3.ews.openShifts.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record OpenShifListTkRespDto(
        Long openShiftId,
        LocalDate startDate,
        LocalDate endDate,
        String templateName,
        Long openShiftLineId,
        LocalDateTime createdOn,
        String createdBy,
        String departmentName,
        String jobTitle,
        String locationName,
        Long departmentId,
        Long jobTitleId,
        Long locationId,
        Long sun,
        Long mon,
        Long tue,
        Long wed,
        Long thu,
        Long fri,
        Long sat,
        String workDurationName,
        LocalDateTime timeStart,
        LocalDateTime timeEnd,
        String isApplied,
        Integer requestedCount,
        Integer appliedCount,
        Integer approvedCount,
        String recalled,
        String skills

) {
}
