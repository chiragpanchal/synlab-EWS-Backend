package com.ewsv3.ews.dashboard.dto;

public record AwaitingActionsDto(
        String taskName,
        Long personId,
        String fullName,
        String employeeNumber,
        String pendingSince,
        Integer personCounts,
        Integer notificationCounts
) {
}
