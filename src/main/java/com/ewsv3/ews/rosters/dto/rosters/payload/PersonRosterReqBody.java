package com.ewsv3.ews.rosters.dto.rosters.payload;

import java.util.Date;

public record PersonRosterReqBody(
        Long userId,
        Long personId,
        Long personRosterId,
        Date startDate,
        Date endDate) {
}
