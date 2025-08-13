package com.ewsv3.ews.commons.dto;

public class DMLResponseDto {
    private String statusMessage;
    private String detailMessage;

    public DMLResponseDto() {
    }

    public DMLResponseDto(String statusMessage, String detailMessage) {
        this.statusMessage = statusMessage;
        this.detailMessage = detailMessage;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public void setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
    }
}
