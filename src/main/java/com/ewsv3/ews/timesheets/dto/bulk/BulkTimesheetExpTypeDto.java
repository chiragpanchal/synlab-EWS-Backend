package com.ewsv3.ews.timesheets.dto.bulk;

public record BulkTimesheetExpTypeDto(
        Long expTypeId,
        String expType,
        Long projectId
) {
}
