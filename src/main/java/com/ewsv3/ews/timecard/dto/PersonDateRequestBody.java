package com.ewsv3.ews.timecard.dto;

import java.util.Date;

public record PersonDateRequestBody(
        Long personId,
        Date startDate,
        Date endDate
) {
}