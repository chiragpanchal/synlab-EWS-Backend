package com.ewsv3.ews.timesheets.dto;

import java.time.LocalDate;

public record TimesheetPageRequestBody(
        Long profileId,
        LocalDate startDate,
        LocalDate endDate
) {
}
