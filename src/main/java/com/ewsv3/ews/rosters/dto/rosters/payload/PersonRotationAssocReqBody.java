package com.ewsv3.ews.rosters.dto.rosters.payload;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PersonRotationAssocReqBody(
        Long personRotationAssocId,
        Long personId,
        Long workRotationId,
        LocalDate startDate,
        LocalDate endDate,
        Integer startSeq,
        Long createdBy,
        LocalDateTime createdOn,
        Long last_updatedBy,
        LocalDateTime lastUpdateDate
) {
}
