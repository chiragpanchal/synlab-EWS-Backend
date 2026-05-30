package com.ewsv3.ews.rotaDemands.dto;

public class JobTitleLookupDto {

    private Long jobTitleId;
    private String jobTitle;

    public JobTitleLookupDto() {}

    public Long getJobTitleId() {
        return jobTitleId;
    }

    public void setJobTitleId(Long jobTitleId) {
        this.jobTitleId = jobTitleId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
}
