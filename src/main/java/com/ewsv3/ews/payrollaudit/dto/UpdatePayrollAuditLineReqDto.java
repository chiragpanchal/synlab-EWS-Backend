package com.ewsv3.ews.payrollaudit.dto;

import java.math.BigDecimal;

public record UpdatePayrollAuditLineReqDto(
        Long payrollAuditLineId,
        Long nPayCodeId,
        BigDecimal nHours,
        String comments
) {
}
