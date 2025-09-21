package com.ewsv3.ews.timesheets.dto.submission;

import java.time.LocalDate;

public record TimesheetSubmitReqBody(
        Long personId,
        Long profileId,
        String payCodes,
        LocalDate startDate,
        LocalDate endDate,
        String comments
) {
}
