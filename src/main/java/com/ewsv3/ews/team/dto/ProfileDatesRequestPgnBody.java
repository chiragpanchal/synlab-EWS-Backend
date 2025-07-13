package com.ewsv3.ews.team.dto;

import java.util.Date;

public record ProfileDatesRequestPgnBody(
        Long profileId,
        Date startDate,
        Date endDate,
        Integer pageNo,
        Integer pageSize
        ) {
}
