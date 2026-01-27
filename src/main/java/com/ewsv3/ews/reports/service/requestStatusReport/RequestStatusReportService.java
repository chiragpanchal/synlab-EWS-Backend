package com.ewsv3.ews.reports.service.requestStatusReport;

import com.ewsv3.ews.reports.dto.requestStatusReport.RequestStatusReportReqBody;
import com.ewsv3.ews.reports.dto.requestStatusReport.RequestStatusRespDto;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import javax.swing.plaf.PanelUI;
import java.util.List;

import static com.ewsv3.ews.reports.service.requestStatusReport.RequestStatusReportUtils.RequestStatusReportSql;

@Service
public class RequestStatusReportService {

    public List<RequestStatusRespDto> getRequestStatusReport(Long userId,
                                                             int page,
                                                             int size,
                                                             RequestStatusReportReqBody reqBody,
                                                             JdbcClient jdbcClient
                                                             ){

        List<RequestStatusRespDto> statusRespDtoList = jdbcClient.sql(RequestStatusReportSql)
                .param("startDate", reqBody.startDate())
                .param("endDate", reqBody.endDate())
                .param("requestName", reqBody.requestName())
                .param("status", reqBody.status())
                .param("offset", page)
                .param("pageSize", size)
                .query(RequestStatusRespDto.class)
                .list();

        return statusRespDtoList;

    }

}
