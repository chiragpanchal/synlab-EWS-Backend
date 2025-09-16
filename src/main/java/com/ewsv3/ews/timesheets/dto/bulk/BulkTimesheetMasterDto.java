package com.ewsv3.ews.timesheets.dto.bulk;

import java.util.List;

public class BulkTimesheetMasterDto {

    private List<BulkTimesheetPersonDto> personDtos;
    private List<BulkTimesheetDepartmentDto> departmentDtos;
    private List<BulkTimesheetJobDto> jobDtos;
    private List<BulkTimesheetProjectDto> projectDtos;
    private List<BulkTimesheetExpTypeDto> expTypeDtos;
    private List<BulkTimesheetPaycodeDto> paycodeDtos;

    public BulkTimesheetMasterDto() {
    }

    public List<BulkTimesheetPersonDto> getPersonDtos() {
        return personDtos;
    }

    public void setPersonDtos(List<BulkTimesheetPersonDto> personDtos) {
        this.personDtos = personDtos;
    }

    public List<BulkTimesheetDepartmentDto> getDepartmentDtos() {
        return departmentDtos;
    }

    public void setDepartmentDtos(List<BulkTimesheetDepartmentDto> departmentDtos) {
        this.departmentDtos = departmentDtos;
    }

    public List<BulkTimesheetJobDto> getJobDtos() {
        return jobDtos;
    }

    public void setJobDtos(List<BulkTimesheetJobDto> jobDtos) {
        this.jobDtos = jobDtos;
    }

    public List<BulkTimesheetProjectDto> getProjectDtos() {
        return projectDtos;
    }

    public void setProjectDtos(List<BulkTimesheetProjectDto> projectDtos) {
        this.projectDtos = projectDtos;
    }

    public List<BulkTimesheetExpTypeDto> getExpTypeDtos() {
        return expTypeDtos;
    }

    public void setExpTypeDtos(List<BulkTimesheetExpTypeDto> expTypeDtos) {
        this.expTypeDtos = expTypeDtos;
    }

    public List<BulkTimesheetPaycodeDto> getPaycodeDtos() {
        return paycodeDtos;
    }

    public void setPaycodeDtos(List<BulkTimesheetPaycodeDto> paycodeDtos) {
        this.paycodeDtos = paycodeDtos;
    }
}