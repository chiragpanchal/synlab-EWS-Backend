package com.ewsv3.ews.rosters.dto.rosters.payload;

import java.util.List;

public record QuickCopyReqBody(
                Long personRosterId,
                List<QuickCopyPersonDateReqBody> quickCopyPersonDateReqBodies) {

}
