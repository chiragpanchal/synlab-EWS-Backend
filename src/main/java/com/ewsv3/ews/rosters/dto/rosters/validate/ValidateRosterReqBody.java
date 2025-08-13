package com.ewsv3.ews.rosters.dto.rosters.validate;

import java.time.LocalDate;

public record ValidateRosterReqBody(
        Long profileId,
        LocalDate startDate,
        LocalDate endDate,
        Long demandTemplateId
) {
}
