package com.ewsv3.ews.rosters.dto.rosters.payload;

import java.util.Date;
import java.util.List;

public record RosterDeleteReasonReqBody(
        Long userId,
        String personIds,
        String deSelectPersonIds,
        Date startDate,
        Date endDate,
        Long personRosterId,
        Long deleteReasonId,
        String deleteComments,
        Long profileId,
        String filterFlag,
        String deleteType,
        String groupKey) {
}
