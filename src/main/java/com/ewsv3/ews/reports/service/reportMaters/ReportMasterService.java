package com.ewsv3.ews.reports.service.reportMaters;

import com.ewsv3.ews.reports.dto.reportMasters.*;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<ReportPersonDto> getReportPerson(Long userId, Long personId, int page,
                                                 int size, ReportPersonReqDto reqDto, JdbcClient jdbcClient) {


        String employeeTextParam = "%" + (reqDto.text() == null ? "" : reqDto.text()) + "%";
        List<ReportPersonDto> personList = new ArrayList<>();

//        System.out.println("getReportPerson employeeTextParam:"+employeeTextParam);
//        System.out.println("getReportPerson reqDto:"+reqDto);
//        System.out.println("getReportPerson userId:"+userId);
//        System.out.println("getReportPerson personId:"+personId);

        if (reqDto.profileId() == 0) {
            //Direct Reportees
            personList = jdbcClient.sql(reportPersonDirectReporteesSQL)
                    .param("personId", personId)
                    .param("startDate", reqDto.startDate())
                    .param("endDate", reqDto.endDate())
                    .param("text", employeeTextParam)
                    .param("offset", page)
                    .param("pageSize", size)
                    .query(ReportPersonDto.class)
                    .list();
        } else if (reqDto.profileId() == 1) {
            //All Reportees
            personList = jdbcClient.sql(reportPersonAllReporteesSQL)
                    .param("personId", personId)
                    .param("startDate", reqDto.startDate())
                    .param("endDate", reqDto.endDate())
                    .param("text", employeeTextParam)
                    .param("offset", page)
                    .param("pageSize", size)
                    .query(ReportPersonDto.class)
                    .list();
        } else {
            personList = jdbcClient.sql(reportPersonTKSQL)
                    .param("userId", userId)
                    .param("profileId", reqDto.profileId())
                    .param("startDate", reqDto.startDate())
                    .param("endDate", reqDto.endDate())
                    .param("text", employeeTextParam)
                    .param("offset", page)
                    .param("pageSize", size)
                    .query(ReportPersonDto.class)
                    .list();

        }

        return personList;

    }

}
