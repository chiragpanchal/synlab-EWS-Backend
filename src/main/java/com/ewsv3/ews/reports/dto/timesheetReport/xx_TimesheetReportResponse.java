package com.ewsv3.ews.reports.dto.timesheetReport;

import com.ewsv3.ews.reports.dto.ReportPersonDto;

import java.util.List;

public class xx_TimesheetReportResponse {

    ReportPersonDto personDto;
    List<TimesheetReportDetailDto> detailDtoList;

    public xx_TimesheetReportResponse() {
    }

    public xx_TimesheetReportResponse(ReportPersonDto personDto, List<TimesheetReportDetailDto> detailDtoList) {
        this.personDto = personDto;
        this.detailDtoList = detailDtoList;
    }

    public ReportPersonDto getPersonDto() {
        return personDto;
    }

    public void setPersonDto(ReportPersonDto personDto) {
        this.personDto = personDto;
    }

    public List<TimesheetReportDetailDto> getDetailDtoList() {
        return detailDtoList;
    }

    public void setDetailDtoList(List<TimesheetReportDetailDto> detailDtoList) {
        this.detailDtoList = detailDtoList;
    }
}
