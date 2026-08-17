package com.ewsv3.ews.rosters.dto.rosters.payload;

import java.time.LocalDate;

public record OptimizedRosterReqBody(
        Long personId,
        LocalDate startDate,
        LocalDate endDate,
        String days,
        Long workDurationId
) {
}
