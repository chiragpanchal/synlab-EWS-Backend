package com.ewsv3.ews.team.dto;

import java.util.Date;

public record ProfileDatesRequestBody(
        Long profileId,
        Long personId,
        Date startDate,
        Date endDate) {
}
