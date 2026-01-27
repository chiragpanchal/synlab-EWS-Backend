package com.ewsv3.ews.reports.dto.rosterAuditReport;

import java.time.LocalDate;

public record RosterAuditReqDto(
        LocalDate startDate,
        LocalDate endDate,
        String str,
        String departmentIds,
        String jobTitleIds,
        String workLocationIds
) {
}
