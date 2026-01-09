package com.ewsv3.ews.openShifts.dto.allocation;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SuggestionPersonRostersDto(
        Long personId,
        LocalDate effectiveDate,
        LocalDateTime timeStart,
        LocalDateTime timeEnd,
        String workDurationCode
        ) {
}
