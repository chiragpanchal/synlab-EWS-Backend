package com.ewsv3.ews.hcmintegrations.dto;

public class SyncResultDto {

    private String status;
    private int recordsSynced;
    private String message;

    public SyncResultDto() {}

    public SyncResultDto(String status, int recordsSynced, String message) {
        this.status = status;
        this.recordsSynced = recordsSynced;
        this.message = message;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getRecordsSynced() { return recordsSynced; }
    public void setRecordsSynced(int recordsSynced) { this.recordsSynced = recordsSynced; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
