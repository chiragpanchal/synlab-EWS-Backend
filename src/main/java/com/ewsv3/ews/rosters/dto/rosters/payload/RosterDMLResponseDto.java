package com.ewsv3.ews.rosters.dto.rosters.payload;


public class RosterDMLResponseDto {

    private String statusMessage;
    private String detailMessage;

    public RosterDMLResponseDto() {
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
