package com.ewsv3.ews.reports.dto.requestStatusReport;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record RequestStatusRespDto(
        String fullName,
        String employeeNumber,
        String departmentName,
        String jobTitle,
        String gradeName,
        String locationName,
        String requestName,
        String reason,
        LocalDate dateStart,
        String requestDate,
        LocalDateTime timeStart,
        String requestTime,
        String comments,
        String itemKey,
        LocalDateTime submitDate,
        LocalDateTime approvedDate,
        String status,
        String violationCode,
        Double dayLessMins,
        String scheduleDetails,
        String punchDetails,
        String pendingOn
) {
}
