package com.ewsv3.ews.reports.dto.attendanceConsolidation;

import java.time.LocalDate;

public record AttendSchViolRespBody(
        Long personId,
        LocalDate effectiveDate,
        Double schHrs,
        String violationCode
) {
}
