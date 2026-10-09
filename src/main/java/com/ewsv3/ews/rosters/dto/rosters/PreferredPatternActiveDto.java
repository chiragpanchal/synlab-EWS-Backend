package com.ewsv3.ews.rosters.dto.rosters;

import java.time.LocalDate;

public record PreferredPatternActiveDto(
        String employeeNumber,
        String personName,
        String workRotationName,
        LocalDate startDate,
        LocalDate endDate,
        Long weekCounts,
        LocalDate currentActiveStartDate
) {
}
