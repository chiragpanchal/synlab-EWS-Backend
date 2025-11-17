package com.ewsv3.ews.reports.dto.attendanceConsolidation;

import java.time.LocalDate;

public record AttendTimecardPersonRespBody(
        Long personId,
        String employeeNumber,
        String personName,
        String jobTitle,
        String departmentName,
        String gradeName
) {
}
