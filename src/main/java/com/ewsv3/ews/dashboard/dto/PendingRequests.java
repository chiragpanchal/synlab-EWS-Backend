package com.ewsv3.ews.dashboard.dto;

public record PendingRequests(
        String requestType,
        Integer counts
) {
}
