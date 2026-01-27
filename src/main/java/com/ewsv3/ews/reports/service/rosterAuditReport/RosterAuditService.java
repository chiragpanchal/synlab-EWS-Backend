package com.ewsv3.ews.reports.service.rosterAuditReport;

import com.ewsv3.ews.reports.dto.rosterAuditReport.RosterAuditReqDto;
import com.ewsv3.ews.reports.dto.rosterAuditReport.RosterAuditResponseDto;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ewsv3.ews.reports.service.rosterAuditReport.RosterAuditUtils.rosterAuditSQL;

@Service
public class RosterAuditService {


    public List<RosterAuditResponseDto> getRosterAuditReport(Long userid, int page, int size, RosterAuditReqDto reqDto, JdbcClient jdbcClient) {

        List<RosterAuditResponseDto> rosterAuditResponseDtos = jdbcClient.sql(rosterAuditSQL)
                .param("userId",userid)
                .param("startDate",reqDto.startDate())
                .param("endDate",reqDto.endDate())
                .param("str",reqDto.str())
                .param("departmentIds",reqDto.departmentIds())
                .param("jobTitleIds",reqDto.jobTitleIds())
                .param("workLocationIds",reqDto.workLocationIds())
                .param("offset",page)
                .param("pageSize",size)
                .query(RosterAuditResponseDto.class)
                .list();

        return rosterAuditResponseDtos;
    }


}
