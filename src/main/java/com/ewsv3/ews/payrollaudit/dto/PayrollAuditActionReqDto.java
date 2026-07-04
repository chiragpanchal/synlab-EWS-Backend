package com.ewsv3.ews.payrollaudit.dto;

public record PayrollAuditActionReqDto(
        Long payPeriodId,
        Long departmentId,
        Long jobTitleId,
        String payCodes,
        Long locationId,
        Long gradeId,
        String assignmentNumber,
        String status,
        String action,
        String comments
) {
}
