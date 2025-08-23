package com.ewsv3.ews.timesheets.dto;

import java.time.LocalDate;

public record TimesheetTableSummary(
        Long personId,
        LocalDate effectiveDate,
        Long itemKey,
        String payCodeName,
        Double timesheetHrs,
        String approvalStatus
) {
}
