package com.ewsv3.ews.reports.dto.timesheetReport;

import java.time.LocalDate;

public record TimesheetReportRespDto(
        String employeeNumber,
        String fullName,
        String businessUnitName,
        String legalEntity,
        String employeeTypes,
        String gradeName,
        String locationName,
        LocalDate effectiveDate,
        String timeStart,
        String timeEnd,
        Double regHrs,
        String timeHour,
        String payCodeName,
        Double allwValue,
        String departmentName,
        String jobTitle,
        String projectName,
        String taskName,
        String expType,
        String comments,
        String submittedBy,
        String submittedOn,
        String status,
        String pendingWith,
        String firstApprover,
        String secondApprover,
        String scheduleStartTime,
        String scheduleEndTime,
        Double schHrs,
        String punchInTime,
        String punchOutTime,
        Double punchHrs
) {
}
