package com.ewsv3.ews.timesheets.dto;

public record TimesheetStatusKpi(
        String approvalStatus,
        Integer timesheetCounts
) {
}
