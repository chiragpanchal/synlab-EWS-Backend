package com.ewsv3.ews.reports.service.reportMaters;

import com.ewsv3.ews.reports.dto.reportMasters.*;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ewsv3.ews.reports.service.reportMaters.ReportMastersUtils.*;

@Service
public class ReportMasterService {


    public AllReportMasters getAllReportMasters(Long userId, Long personId, JdbcClient jdbcClient) {

        List<ReportDepartmentDto> departmentDtoList = jdbcClient.sql(reportDepartmentTKSQL)
                .param("userId", userId)
                .query(ReportDepartmentDto.class)
                .list();

        List<ReportDepartmentDto> departmentDtoListDirectReportee = jdbcClient.sql(reportDepartmentDirectReporteeSQL)
                .param("personId", personId)
                .query(ReportDepartmentDto.class)
                .list();

        List<ReportDepartmentDto> departmentDtoListAllReportee = jdbcClient.sql(reportDepartmentAllReporteeSQL)
                .param("personId", personId)
                .query(ReportDepartmentDto.class)
                .list();

        departmentDtoList.addAll(departmentDtoListDirectReportee);
        departmentDtoList.addAll(departmentDtoListAllReportee);

        List<ReportJobDto> jobDtoList = jdbcClient.sql(reportJobTKSQL)
                .param("userId", userId)
                .query(ReportJobDto.class)
                .list();

        List<ReportJobDto> jobDtoListDirectReportee = jdbcClient.sql(reportJobDirectReporteeSQL)
                .param("personId", personId)
                .query(ReportJobDto.class)
                .list();

        List<ReportJobDto> jobDtoListAllReportee = jdbcClient.sql(reportJobAllReporteeSQL)
                .param("personId", personId)
                .query(ReportJobDto.class)
                .list();

        jobDtoList.addAll(jobDtoListDirectReportee);
        jobDtoList.addAll(jobDtoListAllReportee);

        List<ReportStatusDto> statusDtoList = jdbcClient.sql(statusSQL)
                .query(ReportStatusDto.class)
                .list();

        List<ReportPayCodeDto> payCodeDtoList = jdbcClient.sql(payCodeSQL)
                .query(ReportPayCodeDto.class)
                .list();


        AllReportMasters allReportMaters = new AllReportMasters(
                departmentDtoList,
                jobDtoList,
                statusDtoList,
                payCodeDtoList
        );

        return allReportMaters;

    }

}
