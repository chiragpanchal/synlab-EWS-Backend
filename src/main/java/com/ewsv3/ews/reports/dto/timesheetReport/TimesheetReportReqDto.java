package com.ewsv3.ews.reports.dto.timesheetReport;

import java.time.LocalDate;

public record TimesheetReportReqDto(
        LocalDate startDate,
        LocalDate endDate,
        Long departmentId,
        Long jobTitleId,
        Long personId,
        String pendingWith,
        String employeeText,
        String profileId,
        Long payCodeId,
        String status
) {
}
