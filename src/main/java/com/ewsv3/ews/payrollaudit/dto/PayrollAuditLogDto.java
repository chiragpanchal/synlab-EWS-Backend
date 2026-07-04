package com.ewsv3.ews.payrollaudit.dto;

import java.time.LocalDateTime;

public record PayrollAuditLogDto(
        Long payrollAuditId,
        String fullName,
        LocalDateTime logDate,
        String action,
        String comments,
        String lineObject
) {
}
