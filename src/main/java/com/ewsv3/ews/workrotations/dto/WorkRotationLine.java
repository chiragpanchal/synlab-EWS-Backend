package com.ewsv3.ews.workrotations.dto;

import java.time.LocalDateTime;

public record WorkRotationLine(
        Long workRotationLineId,
        Long workRotationId,
        Integer seq,
        Integer iterations,
        Long createdBy,
        LocalDateTime createdOn,
        Long lastUpdatedBy,
        LocalDateTime lastUpdateDate,
        Integer d_1,
        Integer d_2,
        Integer d_3,
        Integer d_4,
        Integer d_5,
        Integer d_6,
        Integer d_7,
        String createdByName,
        String lastUpdatedByName) {

}
