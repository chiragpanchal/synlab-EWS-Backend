package com.ewsv3.ews.team.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TimecardActuals(
        Long personId,
        Long timecardId,
        Long personRosterId,
        LocalDate effectiveDate,
        LocalDateTime inTime,
        LocalDateTime outTime,
        Double actHrs,
        String timeType,
        Long absenceAttendancesId,
        Long holidayId
        ) {
}
