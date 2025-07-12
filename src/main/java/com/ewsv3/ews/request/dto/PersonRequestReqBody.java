package com.ewsv3.ews.request.dto;

import java.util.Date;

public record PersonRequestReqBody(
                Long personId,
                Date startDate,
                Date endDate) {
}
