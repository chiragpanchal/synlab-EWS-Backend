package com.ewsv3.ews.schedulers.dto.fusionDto.errors;

public record errorLogs(
                String batchId,
                String apiName,
                String apiLink,
                String error) {
}
