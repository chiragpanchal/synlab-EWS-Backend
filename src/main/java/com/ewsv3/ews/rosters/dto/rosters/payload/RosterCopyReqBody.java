package com.ewsv3.ews.rosters.dto.rosters.payload;

import java.util.Date;

public record RosterCopyReqBody(
        Long userId,
        Long fromPersonId,
        String personIds,
        Date startDate,
        Date endDate,
        String filterFlag,
        String copyType,
        String groupKey
) {
}
