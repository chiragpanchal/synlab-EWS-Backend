package com.ewsv3.ews.timesheets.dto.bulk;

public record BulkTimesheetPersonDto(
        Long personId,
        String personName,
        String employeeNumber,
        String jobTitle,
        String departmentName,
        String gradeName,
        Long personUserId
) {
}
