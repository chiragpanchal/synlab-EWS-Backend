package com.ewsv3.ews.shiftGroup.dto;

import com.ewsv3.ews.masters.dto.ValueSetDto;
import com.ewsv3.ews.masters.dto.WorkDurationDto;

import java.util.List;

public class ShiftGoupMasters {
    List<WorkDurationDto> workDurationDtoList;
    List<ValueSetDto> onCallDtoList;
    List<ValueSetDto> emergencyDtoList;

    public ShiftGoupMasters() {
    }

    public ShiftGoupMasters(List<WorkDurationDto> workDurationDtoList, List<ValueSetDto> onCallDtoList, List<ValueSetDto> emergencyDtoList) {
        this.workDurationDtoList = workDurationDtoList;
        this.onCallDtoList = onCallDtoList;
        this.emergencyDtoList = emergencyDtoList;
    }

    public List<WorkDurationDto> getWorkDurationDtoList() {
        return workDurationDtoList;
    }

    public List<ValueSetDto> getOnCallDtoList() {
        return onCallDtoList;
    }

    public List<ValueSetDto> getEmergencyDtoList() {
        return emergencyDtoList;
    }
}
