package com.ewsv3.ews.reports.dto.productivity;

import java.time.LocalDate;

public record TimecardRespDto(
        Long personId,
        String employeeNumber,
        String fullName,
        String jobTitle,
        String departmentName,
        LocalDate effectiveDate,
        Double schHrs,
        Double punchHrs,
        Long violationCount,
        Long leaveCount
) {
}
