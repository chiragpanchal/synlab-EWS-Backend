package com.ewsv3.ews.rosters.dto.healthcare;

import java.util.List;

public class HealthcareRosterResponseDto {
    private List<AcuityDataDto> acuityData;
    private List<JobTitleFactorDto> jobTitleFactors;
    private List<ScheduledFteDto> scheduledFte;

    public HealthcareRosterResponseDto() {}

    public HealthcareRosterResponseDto(List<AcuityDataDto> acuityData,
                                       List<JobTitleFactorDto> jobTitleFactors,
                                       List<ScheduledFteDto> scheduledFte) {
        this.acuityData = acuityData;
        this.jobTitleFactors = jobTitleFactors;
        this.scheduledFte = scheduledFte;
    }

    public List<AcuityDataDto> getAcuityData() {
        return acuityData;
    }

    public void setAcuityData(List<AcuityDataDto> acuityData) {
        this.acuityData = acuityData;
    }

    public List<JobTitleFactorDto> getJobTitleFactors() {
        return jobTitleFactors;
    }

    public void setJobTitleFactors(List<JobTitleFactorDto> jobTitleFactors) {
        this.jobTitleFactors = jobTitleFactors;
    }

    public List<ScheduledFteDto> getScheduledFte() {
        return scheduledFte;
    }

    public void setScheduledFte(List<ScheduledFteDto> scheduledFte) {
        this.scheduledFte = scheduledFte;
    }
}
