package com.ewsv3.ews.request.dto;

import java.time.LocalDateTime;

public record RequestApproval(
        Long itemKey,
        Long notificationId,
        String actionType,
        String fromAction,
        LocalDateTime createdOn,
        String status,
        String roleName,
        Long userId,
        String fullName,
        String emailAddress,
        String fromUser
) {
}
