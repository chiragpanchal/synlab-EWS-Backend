package com.ewsv3.ews.dashboard.dto;

import java.time.LocalDateTime;

public record AwaitingActionsSummaryDto(
        String taskName,
        LocalDateTime pendingSince,
        Long personCounts,
        Long notificationCounts) {

}
