package com.ewsv3.ews.rosters.dto.healthcare;

import java.math.BigDecimal;

public class JobTitleFactorDto {
    private Long jobTitleId;
    private String jobTitle;
    private BigDecimal factor;

    public JobTitleFactorDto() {}

    public JobTitleFactorDto(Long jobTitleId, String jobTitle, BigDecimal factor) {
        this.jobTitleId = jobTitleId;
        this.jobTitle = jobTitle;
        this.factor = factor;
    }

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

    public BigDecimal getFactor() {
        return factor;
    }

    public void setFactor(BigDecimal factor) {
        this.factor = factor;
    }
}
