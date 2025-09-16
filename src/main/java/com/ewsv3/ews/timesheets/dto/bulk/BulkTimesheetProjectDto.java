package com.ewsv3.ews.timesheets.dto.bulk;

import java.time.LocalDate;

public record BulkTimesheetProjectDto(
        Long personId,
        Long taskId,
        String taskName,
        Long projectId,
        String projectNumber,
        String projectName,
        LocalDate startDate,
        LocalDate endDate,
        Double budgetHrs,
        Double budgetCost,
        Double balancedHrs,
        Double balancedCost
) {
}
