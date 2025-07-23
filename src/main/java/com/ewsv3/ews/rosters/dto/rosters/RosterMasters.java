package com.ewsv3.ews.rosters.dto.rosters;

import com.ewsv3.ews.masters.dto.*;

import java.util.ArrayList;
import java.util.List;


public record RosterMasters(

        long userId,
        ArrayList<PersonDto> personDtoList,
        ArrayList<DepartmentDto> departmentDtoList,
        ArrayList<JobDto> jobDtoList,
        ArrayList<LocationDto> locationDtoList,
        List<WorkDurationDto> workDurationDtoList,
        ArrayList<ValueSetDto> onCallDtoList,
        ArrayList<ValueSetDto> emergencyDtoList,
        ArrayList<DepartmentJobDto> departmentJobDtoList,
        ArrayList<ValueSetDto> deleteRosterReasonList
) {

    public RosterMasters {
//        new RosterMasters();
    }

    public RosterMasters(ArrayList<PersonDto> personDtoList) {
        this(0, personDtoList, null, null, null, null, null, null, null,null);
    }

    @Override
    public String toString() {
        return "RosterMasters{" +
                "userId=" + userId +
                ", personDtoList=" + personDtoList +
                ", departmentDtoList=" + departmentDtoList +
                ", jobDtoList=" + jobDtoList +
                ", locationDtoList=" + locationDtoList +
                ", workDurationDtoList=" + workDurationDtoList +
                ", onCallDtoList=" + onCallDtoList +
                ", emergencyDtoList=" + emergencyDtoList +
                ", departmentJobDtoList=" + departmentJobDtoList +
                ", deleteRosterReasonList=" + deleteRosterReasonList +
                '}';
    }
}
