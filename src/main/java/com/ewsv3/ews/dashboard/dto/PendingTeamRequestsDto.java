package com.ewsv3.ews.dashboard.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PendingTeamRequestsDto(
        String request_name,
        Long personId,
        Long userId,
        String fullName,
        String employeeNumber,
        LocalDate dateStart,
        LocalDate dateEnd,
        LocalDateTime timeStart,
        LocalDateTime timeEnd,
        LocalDateTime startDate,
        Long personRequestId,
        Long itemKey,
        String reason,
        String comments,
        String schedules,
        String punches,
        String violation_code) {
}
