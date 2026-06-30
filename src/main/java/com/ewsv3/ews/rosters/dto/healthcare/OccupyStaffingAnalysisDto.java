package com.ewsv3.ews.rosters.dto.healthcare;

import java.util.List;

public class OccupyStaffingAnalysisDto {
    private List<OccupyFteDto> occupyData;
    private List<ScheduledFteWithShiftDto> scheduledFte;

    public OccupyStaffingAnalysisDto() {}

    public OccupyStaffingAnalysisDto(List<OccupyFteDto> occupyData, List<ScheduledFteWithShiftDto> scheduledFte) {
        this.occupyData = occupyData;
        this.scheduledFte = scheduledFte;
    }

    public List<OccupyFteDto> getOccupyData() {
        return occupyData;
    }

    public void setOccupyData(List<OccupyFteDto> occupyData) {
        this.occupyData = occupyData;
    }

    public List<ScheduledFteWithShiftDto> getScheduledFte() {
        return scheduledFte;
    }

    public void setScheduledFte(List<ScheduledFteWithShiftDto> scheduledFte) {
        this.scheduledFte = scheduledFte;
    }
}
