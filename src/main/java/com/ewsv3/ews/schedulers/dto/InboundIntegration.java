package com.ewsv3.ews.schedulers.dto;

public record InboundIntegration(
                long integrationId,
                String name,
                String endPoint) {
}
