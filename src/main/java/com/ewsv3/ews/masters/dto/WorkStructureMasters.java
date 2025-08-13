package com.ewsv3.ews.masters.dto;

import java.util.ArrayList;
import java.util.List;

public class WorkStructureMasters {
    ArrayList<DepartmentDto> departmentDtoList;
    ArrayList<JobDto> jobDtoList;
    ArrayList<LocationDto> locationDtoList;
    ArrayList<DepartmentJobDto> departmentJobDtoList;
    List<WorkDurationDto> workDurationDtoList;

    public List<WorkDurationDto> getWorkDurationDtoList() {
        return workDurationDtoList;
    }

    public void setWorkDurationDtoList(List<WorkDurationDto> workDurationDtoList) {
        this.workDurationDtoList = workDurationDtoList;
    }

    public WorkStructureMasters() {
    }

    public ArrayList<DepartmentDto> getDepartmentDtoList() {
        return departmentDtoList;
    }

    public void setDepartmentDtoList(ArrayList<DepartmentDto> departmentDtoList) {
        this.departmentDtoList = departmentDtoList;
    }

    public ArrayList<JobDto> getJobDtoList() {
        return jobDtoList;
    }

    public void setJobDtoList(ArrayList<JobDto> jobDtoList) {
        this.jobDtoList = jobDtoList;
    }

    public ArrayList<LocationDto> getLocationDtoList() {
        return locationDtoList;
    }

    public void setLocationDtoList(ArrayList<LocationDto> locationDtoList) {
        this.locationDtoList = locationDtoList;
    }

    public ArrayList<DepartmentJobDto> getDepartmentJobDtoList() {
        return departmentJobDtoList;
    }

    public void setDepartmentJobDtoList(ArrayList<DepartmentJobDto> departmentJobDtoList) {
        this.departmentJobDtoList = departmentJobDtoList;
    }
}
