package com.ewsv3.ews.rosters.dto.rosters;

import java.time.LocalDate;

public record DemandAllocationReqBody(
        Long userId,
        LocalDate startDate,
        LocalDate endDate,
        Long demandTemplateId
) {
}
