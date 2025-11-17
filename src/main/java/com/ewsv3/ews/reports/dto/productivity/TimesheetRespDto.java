package com.ewsv3.ews.reports.dto.productivity;

import java.time.LocalDate;

public record TimesheetRespDto(
        Long personId,
        LocalDate effectiveDate,
        String payCodeName,
        Double regHrs
) {
}
