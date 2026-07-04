package com.ewsv3.ews.payrollaudit.dto;

import java.math.BigDecimal;

public record PayrollAuditSummaryDto(
        Long personId,
        Long payrollAuditId,
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
        String payCodeName,
        String status,
        BigDecimal hours
) {
}
