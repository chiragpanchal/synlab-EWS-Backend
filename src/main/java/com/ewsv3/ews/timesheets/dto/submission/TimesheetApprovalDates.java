package com.ewsv3.ews.timesheets.dto.submission;

import java.time.LocalDate;

public record TimesheetApprovalDates(
        LocalDate startDate,
        LocalDate endDate

) {

}
