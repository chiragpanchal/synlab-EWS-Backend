package com.ewsv3.ews.rosters.dto.rosters.payload;

import java.time.LocalDate;

public record DragDropReqBody(
        Long personRosterId,
        Long personId,
        LocalDate effectiveDate) {

}