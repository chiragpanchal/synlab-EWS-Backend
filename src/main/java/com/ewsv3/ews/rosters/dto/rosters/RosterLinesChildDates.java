package com.ewsv3.ews.rosters.dto.rosters;

import java.time.LocalDate;

public record RosterLinesChildDates(
        LocalDate effectiveDate,
        RosterLinesChild[] children
) {
}
