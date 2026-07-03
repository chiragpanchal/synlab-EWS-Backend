package com.ewsv3.ews.payrollaudit.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PayPeriodDto(
        Long payPeriodId,
        Long payCalendarId,
        String payPeriodName,
        LocalDate startDate,
        LocalDate endDate,
        LocalDate processDate,
        Long createdBy,
        LocalDateTime createdOn,
        Long lastUpdatedBy,
        LocalDateTime lastUpdateDate,
        LocalDate cutoffDate
) {
}
