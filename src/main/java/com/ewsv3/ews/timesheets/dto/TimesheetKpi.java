package com.ewsv3.ews.timesheets.dto;

import java.util.List;

public record TimesheetKpi(
        List<TimesheetPayCodeKpi> timesheetPayCodeKpi,
        List<TimesheetStatusKpi> timesheetStatusKpi
) {
}
