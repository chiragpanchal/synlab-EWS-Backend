package com.ewsv3.ews.timesheets.dto.bulk;

public record BulkTimesheetPaycodeDto(
        Long payCodeId,
        String payCode,
        String payCodeName,
        String allwHourCode
) {
}
