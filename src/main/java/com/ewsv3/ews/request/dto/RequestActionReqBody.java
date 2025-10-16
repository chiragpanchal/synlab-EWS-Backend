package com.ewsv3.ews.request.dto;

public record RequestActionReqBody(
        Long personId,
        Long itemKey,
        String requestName,
        String reason,
        String fromAction,
        Long fwdUserId,
        Long rmiUserId,
        String comments) {

}
