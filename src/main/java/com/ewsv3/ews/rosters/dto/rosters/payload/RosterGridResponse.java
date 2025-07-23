package com.ewsv3.ews.rosters.dto.rosters.payload;

import java.util.List;

public record RosterGridResponse(
        List<PersonRosterPivotResponse> pivotResponseList,
        String rosterGridKpi
) {
}
