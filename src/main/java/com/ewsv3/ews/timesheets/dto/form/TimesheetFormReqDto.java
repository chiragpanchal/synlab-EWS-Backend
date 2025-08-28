package com.ewsv3.ews.timesheets.dto.form;

import java.time.LocalDate;

public record TimesheetFormReqDto(
        Long personId,
        Long projectId,
        LocalDate effectiveDate
) {
}
