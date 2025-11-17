package com.ewsv3.ews.reports.dto.attendanceConsolidation;

import java.time.LocalDate;

public record AttendHolidaysRespBody(
        Long personId,
        LocalDate effectiveDate,
        String holidayName
) {
}
