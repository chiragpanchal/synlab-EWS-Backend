package com.ewsv3.ews.reports.service.timesheetReport;

import com.ewsv3.ews.reports.dto.timesheetReport.TimesheetReportReqDto;
import com.ewsv3.ews.reports.dto.timesheetReport.TimesheetReportRespDto;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.List;

import static com.ewsv3.ews.reports.service.timesheetReport.TimesheetReportUtils.timesheetReportSql;

@Service
public class TimesheetReportService {

    public List<TimesheetReportRespDto> getTimesheetReport(Long userId, int page,
                                                           int size,TimesheetReportReqDto reqDto, JdbcClient jdbcClient) {

        System.out.println("timesheet-report getTimesheetReport userId:" + userId);
        System.out.println("timesheet-report getTimesheetReport reqDto:" + reqDto);

        String pendingWithParam = "%" + (reqDto.pendingWith() == null ? "" : reqDto.pendingWith()) + "%";
        String employeeTextParam = "%" + (reqDto.employeeText() == null ? "" : reqDto.employeeText()) + "%";

        List<TimesheetReportRespDto> list = jdbcClient.sql(timesheetReportSql)
                .param("startDate", reqDto.startDate())
                .param("endDate", reqDto.endDate())
                .param("departmentId", reqDto.departmentId(), Types.NUMERIC)
                .param("jobTitleId", reqDto.jobTitleId(),Types.NUMERIC)
                .param("personId", reqDto.personId(),Types.NUMERIC)
                .param("pendingWith",  pendingWithParam ,Types.VARCHAR)
                .param("profileId",reqDto.profileId(),Types.NUMERIC)
                .param("employeeText", employeeTextParam, Types.VARCHAR)
                .param("userId",userId,Types.NUMERIC)
                .param("payCodeId", reqDto.payCodeId(), Types.NUMERIC)
                .param("offset", page)
                .param("pageSize", size)
                .query(TimesheetReportRespDto.class)
                .list();
        System.out.println("timesheet-report getTimesheetReport list.size():" + list.size());
        return list;
    }

}
