package com.ewsv3.ews.reports.dto.attendanceConsolidation;

import java.time.LocalDate;

public record AttendActualLinesRespBody(
        Long personId,
        LocalDate effectiveDate,
        Double actHrs

) {
}
