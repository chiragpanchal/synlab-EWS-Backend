package com.ewsv3.ews.commons.dto;

public class MessageDto {

    private String statusCode;
    private String messageDetails;

    public MessageDto() {}

    public MessageDto(String statusCode, String messageDetails) {
        this.statusCode = statusCode;
        this.messageDetails = messageDetails;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessageDetails() {
        return messageDetails;
    }

    public void setMessageDetails(String messageDetails) {
        this.messageDetails = messageDetails;
    }
}
