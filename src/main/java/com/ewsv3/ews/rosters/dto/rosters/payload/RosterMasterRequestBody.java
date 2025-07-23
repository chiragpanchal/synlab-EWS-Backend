package com.ewsv3.ews.rosters.dto.rosters.payload;

import java.util.Date;

public record RosterMasterRequestBody(
        Long userId,
        Long profileId,
        Date startDate,
        Date endDate) {
}
