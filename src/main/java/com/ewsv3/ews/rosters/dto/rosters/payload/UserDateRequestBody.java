package com.ewsv3.ews.rosters.dto.rosters.payload;


import java.util.Date;

public record UserDateRequestBody(
        Long userId,
        Date effectiveDate) {
}
