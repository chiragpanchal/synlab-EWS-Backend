package com.ewsv3.ews.openShifts.dto.allocation;

import java.time.LocalDate;

public record SuggestionPersonHolidaysDto(
        Long personId,
        String holidayName,
        LocalDate holidayDate
) {
}
