package com.ewsv3.ews.openShifts.dto.allocation;

import java.util.List;

public class SelfApplicationRespSto {

    PersonSelfApplicationsDto personSelfApplicationsDto;
    List<SuggestionPersonRostersDto> personRostersDtoList;
    List<SuggestionPersonLeavesDto> personLeavesDtos;
    List<SuggestionPersonHolidaysDto> personHolidaysDtos;

    public SelfApplicationRespSto() {
    }

    public SelfApplicationRespSto(PersonSelfApplicationsDto personSelfApplicationsDto, List<SuggestionPersonRostersDto> personRostersDtoList, List<SuggestionPersonLeavesDto> personLeavesDtos, List<SuggestionPersonHolidaysDto> personHolidaysDtos) {
        this.personSelfApplicationsDto = personSelfApplicationsDto;
        this.personRostersDtoList = personRostersDtoList;
        this.personLeavesDtos = personLeavesDtos;
        this.personHolidaysDtos = personHolidaysDtos;
    }

    public PersonSelfApplicationsDto getPersonSelfApplicationsDto() {
        return personSelfApplicationsDto;
    }

    public void setPersonSelfApplicationsDto(PersonSelfApplicationsDto personSelfApplicationsDto) {
        this.personSelfApplicationsDto = personSelfApplicationsDto;
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
}
