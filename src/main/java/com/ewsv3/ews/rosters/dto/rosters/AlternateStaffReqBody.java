package com.ewsv3.ews.rosters.dto.rosters;

import java.util.Date;

public record AlternateStaffReqBody(
        Long profileId,
        Date startDate,
        Date endDate,
        Long personId,
        Long personRosterId
) {
}
