package com.ewsv3.ews.timesheets.service.form;

import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.timesheets.dto.form.*;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ewsv3.ews.timesheets.service.form.TimesheetFormUtils.*;

@Service
public class TimesheetFormService {

    public List<PayCodeDto> getPayCodes(Long userId, TimesheetFormReqDto reqDto, JdbcClient jdbcClient) {

        List<PayCodeDto> payCodeDtoList = jdbcClient.sql(sqlPayCodeList)
                .query(PayCodeDto.class)
                .list();

        return payCodeDtoList;
    }

    public List<TsDepartmentDto> getDepartments(Long userId, TimesheetFormReqDto reqDto, JdbcClient jdbcClient) {
        List<TsDepartmentDto> tsDepartmentDtoList = jdbcClient.sql(sqlDepartmentList)
                .param("personId", reqDto.personId())
                .query(TsDepartmentDto.class)
                .list();

        return tsDepartmentDtoList;
    }

    public List<TsJobDto> getJobs(Long userId, TimesheetFormReqDto reqDto, JdbcClient jdbcClient) {
        List<TsJobDto> tsJobDtoList = jdbcClient.sql(sqlJobList)
                .param("personId", reqDto.personId())
                .query(TsJobDto.class)
                .list();

        return tsJobDtoList;
    }

    public List<ProjectTaskDto> getProjectTasks(Long userId, TimesheetFormReqDto reqDto, JdbcClient jdbcClient) {
        List<ProjectTaskDto> projectTaskDtoList = jdbcClient.sql(sqlProjectTaskList)
                .param("personId", reqDto.personId())
                .query(ProjectTaskDto.class)
                .list();

        return projectTaskDtoList;
    }

    public List<ExpTypeDto> getExpenditureTypes(Long userId, TimesheetFormReqDto reqDto, JdbcClient jdbcClient) {
        List<ExpTypeDto> expTypeDtoList = jdbcClient.sql(sqlExpenditureList)
                .param("personId", reqDto.personId())
                .query(ExpTypeDto.class)
                .list();

        return expTypeDtoList;
    }

    public TsMasters getTimesheetMasters(Long userId, TimesheetFormReqDto reqDto, JdbcClient jdbcClient) {

        TsMasters tsMasters = new TsMasters(
                getPayCodes(userId, reqDto, jdbcClient),
                getDepartments(userId, reqDto, jdbcClient),
                getJobs(userId, reqDto, jdbcClient),
                getProjectTasks(userId, reqDto, jdbcClient),
                getExpenditureTypes(userId, reqDto, jdbcClient)
        );

        return tsMasters;


    }

    public List<TimesheetDetails> getTimesheetdetails (Long userId, TimesheetDetailsReqDto reqDto, JdbcClient jdbcClient){

        List<TimesheetDetails> timesheetDetails = jdbcClient.sql(sqlTimesheetDetails)
                .param("personId", reqDto.personId())
                .param("startDate", reqDto.startDate())
                .param("endDate", reqDto.endDate())
                .query(TimesheetDetails.class)
                .list();

        return timesheetDetails;
    }


    public List<TsTimecardData> getTimecardData (Long userId, TimesheetDetailsReqDto reqDto, JdbcClient jdbcClient){

        List<TsTimecardData> tsTimecardDataList = jdbcClient.sql(sqlTsTimecardData)
                .param("personId", reqDto.personId())
                .param("startDate", reqDto.startDate())
                .param("endDate", reqDto.endDate())
                .query(TsTimecardData.class)
                .list();

        return tsTimecardDataList;
    }

    public DMLResponseDto saveTimesheets(){


        return new DMLResponseDto("S","Timesheet Saved Successfully");
    }

}
