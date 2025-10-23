package com.ewsv3.ews.rosters.dto.rosters.payload;

import java.time.LocalDate;

public record QuickCopyPersonDateReqBody(
                Long personId,
                LocalDate effectiveDate

) {

}
