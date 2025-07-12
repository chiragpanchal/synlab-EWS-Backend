package com.ewsv3.ews.timecard.dto;

public record TimecardSummary(
        Double totSchHrs,
        Double totActHrs,
        Double totViolationCount,
        Double totAbsenceHrs,
        Double totHolidayHrs
) {
}
