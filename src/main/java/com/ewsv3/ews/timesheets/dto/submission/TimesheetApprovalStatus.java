package com.ewsv3.ews.timesheets.dto.submission;

import java.time.LocalDateTime;

public record TimesheetApprovalStatus(
        Long notifCommentId,
        Long notificationId,
        String actionType,
        String fromUser,
        String actionTaken,
        String toUser,
        String status,
        String roleName,
        String comments,
        LocalDateTime transactionDate
) {
}
