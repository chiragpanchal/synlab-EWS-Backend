package com.ewsv3.ews.openShifts.dto.allocation;

import java.util.List;

public class SuggestionPersonDto {
    Long personId;
    String personName;
    String employeeNumber;
    String gradeName;
    Double rate;
    Double schHrs;
    List<SuggestionPersonRostersDto> personRostersDtoList;
    List<SuggestionPersonLeavesDto> personLeavesDtos;
    List<SuggestionPersonHolidaysDto> personHolidaysDtos;
    List<PersonSelfApplicationsDto> personSelfApplicationsDtos;

    public SuggestionPersonDto() {
    }

    public SuggestionPersonDto(Long personId, String personName, String employeeNumber, String gradeName, Double rate, Double schHrs) {
        this.personId = personId;
        this.personName = personName;
        this.employeeNumber = employeeNumber;
        this.gradeName = gradeName;
        this.rate = rate;
        this.schHrs = schHrs;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Double getSchHrs() {
        return schHrs;
    }

    public void setSchHrs(Double schHrs) {
        this.schHrs = schHrs;
    }

    public List<SuggestionPersonRostersDto> getPersonRostersDtoList() {
        return personRostersDtoList;
    }

    public void setPersonRostersDtoList(List<SuggestionPersonRostersDto> personRostersDtoList) {
        this.personRostersDtoList = personRostersDtoList;
    }

    public List<SuggestionPersonLeavesDto> getPersonLeavesDtos() {
        return personLeavesDtos;
    }

    public void setPersonLeavesDtos(List<SuggestionPersonLeavesDto> personLeavesDtos) {
        this.personLeavesDtos = personLeavesDtos;
    }

    public List<SuggestionPersonHolidaysDto> getPersonHolidaysDtos() {
        return personHolidaysDtos;
    }

    public void setPersonHolidaysDtos(List<SuggestionPersonHolidaysDto> personHolidaysDtos) {
        this.personHolidaysDtos = personHolidaysDtos;
    }

    public List<PersonSelfApplicationsDto> getPersonSelfApplicationsDtos() {
        return personSelfApplicationsDtos;
    }

    public void setPersonSelfApplicationsDtos(List<PersonSelfApplicationsDto> personSelfApplicationsDtos) {
        this.personSelfApplicationsDtos = personSelfApplicationsDtos;
    }
}
