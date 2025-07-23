package com.ewsv3.ews.rosters.dto.rosters;


public record WorkDurationDtoAssignment(
        long workDurationId,
        String workDurationCode,
        String workDurationName,
        String timeStart,
        String timeEnd,
        String mon,
        String tue,
        String wed,
        String thu,
        String fri,
        String sat,
        String sun,
        String duration,
        long workDurationCategoryId) {
}
