package com.ewsv3.ews.reports.dto.timesheetReport;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TimesheetReportDetailDto(
        Long ttsTimesheetId,
        Long personId,
        LocalDate effectiveDate,
        LocalDateTime timeStart,
        LocalDateTime timeEnd,
        Double regHrs,
        String payCodeName,
        String timeHour,
        Double allwValue,
        String comments,
        String status,
        String submittedBy,
        String submittedOn,
        String pendingWith,
        String departmentName,
        String jobTitle,
        String projectName,
        String taskName,
        String expType,
        Long itemKey,
        String schDetails,
        String punchDetails
) {
}
