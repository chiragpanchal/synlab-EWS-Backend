package com.ewsv3.ews.openShifts.dto.allocation;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SuggestionPersonLeavesDto(
        Long personId,
        String absenceName,
        LocalDate leaveDate,
        Double absenceDays,
        Double absenceHrs,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
}
