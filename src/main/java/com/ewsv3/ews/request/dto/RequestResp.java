package com.ewsv3.ews.request.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record RequestResp(
        Long personRequestId,
        Long personId,
        String requestName,
        String reason,
        LocalDate dateStart,
        LocalDate dateEnd,
        LocalDateTime timeStart,
        LocalDateTime timeEnd,
        Long itemKey,
        LocalDateTime createdOn,
        String status,
        String comments,
        String  pendingWith
) {
}
