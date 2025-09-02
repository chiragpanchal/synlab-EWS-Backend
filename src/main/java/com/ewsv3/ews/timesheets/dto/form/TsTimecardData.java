package com.ewsv3.ews.timesheets.dto.form;

import java.time.LocalDate;

public record TsTimecardData(
        Long personId,
        LocalDate effectiveDate,
        String scheduleLine,
        String punchLine,
        String LeaveLine,
        String holidayLine,
        String violationLine
) {
}
