package com.ewsv3.ews.timesheets.dto.bulk;

public record BulkTimesheetDepartmentDto(
        Long personId,
        Long departmentId,
        String departmentName
) {
}
