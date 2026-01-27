package com.ewsv3.ews.reports.dto.rosterAuditReport;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record RosterAuditResponseDto(
        String employeeNumber,
        String fullName,
        String departmentName,
        String jobTitle,
        String gradeName,
        String locationName,
        String legalEntity,
        LocalDate effectiveDate,
        String auditFlag,
        LocalDateTime auditTime,
        String auditUser,
        String changedValues,
        String deleteLogs,
        String oldShift,
        String newShift,
        String oldDepartmentName,
        String newDepartmentName,
        String oldJobTitle,
        String newJobTitle,
        String oldLocationName,
        String newLocationName,
        String deleteReason,
        String deleteComments
) {
}
