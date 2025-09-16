package com.ewsv3.ews.timesheets.dto.bulk;

public record BulkTimesheetJobDto(
        Long personId,
        Long jobTitleId,
        String jobTitle,
        Long payCodeId,
        Double perHrSal
) {
}
