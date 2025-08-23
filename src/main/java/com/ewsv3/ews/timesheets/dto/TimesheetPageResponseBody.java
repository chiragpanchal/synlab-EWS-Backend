package com.ewsv3.ews.timesheets.dto;

import java.time.LocalDate;
import java.util.List;

public record TimesheetPageResponseBody(
        TimesheetPerson timesheetPerson,
        List<TimesheetDateSummary> timesheetDateSummaries

) {
}
