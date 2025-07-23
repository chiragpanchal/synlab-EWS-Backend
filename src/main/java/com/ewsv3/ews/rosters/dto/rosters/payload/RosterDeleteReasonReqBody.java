package com.ewsv3.ews.rosters.dto.rosters.payload;

import java.util.Date;
import java.util.List;

public record RosterDeleteReasonReqBody(
                Long userId,
                List<Long> personId,
                List<Long> deSelectPersonId,
                Date startDate,
                Date endDate,
                Long personRosterId,
                Long deleteReasonId,
                String deleteComments,
                Long profileId,
                String filterFlag) {
}
