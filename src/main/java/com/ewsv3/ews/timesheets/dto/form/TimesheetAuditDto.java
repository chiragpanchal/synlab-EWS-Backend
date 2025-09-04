package com.ewsv3.ews.timesheets.dto.form;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TimesheetAuditDto(
        LocalDate effectiveDate,
        LocalDateTime auditDate,
        String auditUser,
        String operation,
        String deleteComments,
        Double regHrs,
        String payCodeName,
        LocalDateTime timeStart,
        LocalDateTime timeEnd,
        String department,
        String jobTitle,
        String projectName,
        String taskName,
        String expenditureType,
        String timesheetComments,
        String timeHour
) {
}
