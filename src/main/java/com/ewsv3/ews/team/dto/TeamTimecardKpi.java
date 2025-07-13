package com.ewsv3.ews.team.dto;

public record TeamTimecardKpi(
        Integer personCounts,
        Double schHrs,
        Double actHrs,
        Double leaveHrs,
        Double holidayHrs,
        Integer violationCounts
) {
}
