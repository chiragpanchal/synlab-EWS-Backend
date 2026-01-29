package com.ewsv3.ews.masters.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ShiftGroupShiftsDto(
        Long shiftGroupWorkShiftId,
        String onCall,
        String onCallMeaning,
        String emergency,
        String emergencyMeaning,
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
        Double duration,
        Long workDurationCategoryId,
        String exceptionEvents,
        Double minWorkHrs,
        Double maxWorkHrs,
        String workUnit,
        Long hcmScheduleId,
        String erosterCode,
        String timeHour
) {
}
