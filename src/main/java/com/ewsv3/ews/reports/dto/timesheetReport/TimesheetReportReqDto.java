package com.ewsv3.ews.reports.dto.timesheetReport;

import java.time.LocalDate;

public record TimesheetReportReqDto(
        LocalDate startDate,
        LocalDate endDate,
        Long departmentId,
        Long jobTitleId,
        String status,//status filter
        String payCodeName,//payCode filter
        String employeeText,
        String pendingWith,
        Long profileId
) {
}
