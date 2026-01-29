package com.ewsv3.ews.reports.dto.reportMasters;

import java.util.List;

public class AllReportMasters {
    List<ReportDepartmentDto> reportDepartmentDtoList;
    List<ReportJobDto> reportJobDtoList;
    List<ReportStatusDto> reportStatusDtoList;
    List<ReportPayCodeDto> reportPayCodeDtoList;

    public AllReportMasters() {
    }

    public AllReportMasters(List<ReportDepartmentDto> reportDepartmentDtoList, List<ReportJobDto> reportJobDtoList, List<ReportStatusDto> reportStatusDtoList, List<ReportPayCodeDto> reportPayCodeDtoList) {
        this.reportDepartmentDtoList = reportDepartmentDtoList;
        this.reportJobDtoList = reportJobDtoList;
        this.reportStatusDtoList = reportStatusDtoList;
        this.reportPayCodeDtoList = reportPayCodeDtoList;
    }

    public List<ReportDepartmentDto> getReportDepartmentDtoList() {
        return reportDepartmentDtoList;
    }

    public List<ReportJobDto> getReportJobDtoList() {
        return reportJobDtoList;
    }

    public List<ReportStatusDto> getReportStatusDtoList() {
        return reportStatusDtoList;
    }

    public List<ReportPayCodeDto> getReportPayCodeDtoList() {
        return reportPayCodeDtoList;
    }
}
