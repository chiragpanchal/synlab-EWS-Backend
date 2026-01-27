package com.ewsv3.ews.request.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record RequestNotificationResponse(
                String requestName,
                String reason,
                LocalDate dateStart,
                LocalDate dateEnd,
                LocalDateTime timeStart,
                LocalDateTime timeEnd,
                Long personId,
                String fullName,
                String employeeNumber,
                LocalDateTime pendingSince,
                Long itemKey,
                Long notificationId,
                Long toUserId,
                String comments,
                String schedules,
                String punches,
                String violation_code,
                String swapDetails) {

}
