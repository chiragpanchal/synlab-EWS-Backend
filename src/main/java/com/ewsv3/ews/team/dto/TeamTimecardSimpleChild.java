package com.ewsv3.ews.team.dto;

import java.time.LocalDate;

public record TeamTimecardSimpleChild(
        LocalDate effectiveDate,
        String workDurationCode,
        Double schHrs,
        Double actHrs,
        Long leaveCount,
        Long violationCount
) {
    public TeamTimecardSimpleChild setEffectiveDate(LocalDate effectiveDate) {
        return new TeamTimecardSimpleChild(effectiveDate, workDurationCode, schHrs, actHrs, leaveCount, violationCount);
    }

}
