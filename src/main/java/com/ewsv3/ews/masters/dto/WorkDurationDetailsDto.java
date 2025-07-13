package com.ewsv3.ews.masters.dto;

import java.time.LocalDateTime;

public record WorkDurationDetailsDto(
        long workDurationDetailId,
        long workDurationId,
        String typeName,
        int effectDay,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Double duration,
        long createdBy,
        LocalDateTime createdOn,
        long lastUpdatedBy,
        LocalDateTime lastUpdateDate,
        Double graceMinutes,
        String geenrateException) {
}
