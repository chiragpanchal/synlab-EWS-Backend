package com.ewsv3.ews.rosters.dto.rosters;

import java.util.Date;

public record RotaDemandSuggestionsReqBody(
        Long profileId,
        Long rotaDemandId,
        Date startDate
) {
}
