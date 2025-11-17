package com.ewsv3.ews.reports.dto.attendanceConsolidation;

import java.time.LocalDate;

public record AttendLeavesRespBody(
        Long personId,
        LocalDate effectiveDate,
        String absenceName
) {
}
