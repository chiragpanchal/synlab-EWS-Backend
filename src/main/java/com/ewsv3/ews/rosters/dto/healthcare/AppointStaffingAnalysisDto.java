package com.ewsv3.ews.rosters.dto.healthcare;

import java.util.List;

public class AppointStaffingAnalysisDto {
    private List<AppointFteDto> appointData;
    private List<ScheduledFteWithShiftDto> scheduledFte;

    public AppointStaffingAnalysisDto() {}

    public AppointStaffingAnalysisDto(List<AppointFteDto> appointData, List<ScheduledFteWithShiftDto> scheduledFte) {
        this.appointData = appointData;
        this.scheduledFte = scheduledFte;
    }

    public List<AppointFteDto> getAppointData() {
        return appointData;
    }

    public void setAppointData(List<AppointFteDto> appointData) {
        this.appointData = appointData;
    }

    public List<ScheduledFteWithShiftDto> getScheduledFte() {
        return scheduledFte;
    }

    public void setScheduledFte(List<ScheduledFteWithShiftDto> scheduledFte) {
        this.scheduledFte = scheduledFte;
    }
}
