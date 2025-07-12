package com.ewsv3.ews.timecard.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TimecardActuals(
        Integer timecardId,
        Integer personRosterId,
        LocalDate effectiveDate,
        LocalDateTime inTime,
        LocalDateTime outTime,
        Double actHrs,
        String timeType,
        Long absenceAttendancesId,
        Long holidayId) {
}
