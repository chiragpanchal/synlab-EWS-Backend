package com.ewsv3.ews.reports.dto.reportMasters;

public record ReportPersonDto(
        Long personId,
        String employeeNumber,
        String personName,
        String departmentName,
        String jobTitle,
        String gradeName,
        String locationName
) {
}
