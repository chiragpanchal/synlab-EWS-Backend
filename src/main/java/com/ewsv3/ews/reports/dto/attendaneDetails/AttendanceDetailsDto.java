package com.ewsv3.ews.reports.dto.attendaneDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AttendanceDetailsDto(
        String employeeNumber,
        String personName,
        String jobTitle,
        String departmentName,
        String locationName,
        String gradeName,
        LocalDate effectiveDate,
        LocalDateTime schTimeStart,
        LocalDateTime schTimeEnd,
        Double schHrs,
        String violationCode,
        Integer occurences,
        LocalDateTime inTime,
        LocalDateTime outTime,
        Double actHrs,
        String leaveName
) {
}
