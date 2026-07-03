package com.ewsv3.ews.payrollaudit.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PayrollAuditDetailsDto(
        Long personId,
        String employeeNumber,
        String assignmentNumber,
        String fullName,
        String departmentName,
        String jobTitle,
        String gradeName,
        String businessUnitName,
        String legalEntity,
        String employeeTypes,
        String locationName,
        LocalDate effectiveDate,
        Long payrollAuditLineId,
        Long ttsTimesheetId,
        String payCodeName,
        Long payCodeId,
        BigDecimal hours,
        String comments
) {
}
