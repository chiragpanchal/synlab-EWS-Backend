package com.ewsv3.ews.reports.dto.timesheetReport;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TimesheetReportRespDto(
        String employeeNumber,
        String fullName,
        String businessUnitName,
        String legalEntity,
        String employeeTypes,
        LocalDate effectiveDate,
        String scheduleStartTime,
        String scheduleEndTime,
        Double schHrs,
        String timeStart,
        String timeEnd,
        String payCodeName,
        Double regHrs,
        String departmentName,
        String jobTitle,
        String comments,
        String submittedBy,
        String submittedOn,
        String status,
        String pendingWith,
        String firstApprover,
        String secondApprover
) {
}
