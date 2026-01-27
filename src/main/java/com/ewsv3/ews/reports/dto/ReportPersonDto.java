package com.ewsv3.ews.reports.dto;

public record ReportPersonDto(
        Long personId,
        Long assignmentId,
        String employeeNumber,
        String assignmentNumber,
        String personName,
        String departmentName,
        String jobTitle,
        String gradeName,
        Long personUserId
) {
}
