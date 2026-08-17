package com.ewsv3.ews.rosters.dto.rosters.payload;

import java.time.LocalDate;

public record DemandRosterPersonReqBody(
        Long profileId,
        Long demandTemplateLineId,
        LocalDate startDate,
        LocalDate endDate,
        String personIds
) {
}
