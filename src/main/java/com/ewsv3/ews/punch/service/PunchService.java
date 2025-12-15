package com.ewsv3.ews.punch.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import com.ewsv3.ews.punch.dto.Punch;
import com.ewsv3.ews.punch.dto.PunchResponse;
import com.ewsv3.ews.punch.dto.TimeType;
import com.ewsv3.ews.timesheets.dto.form.TimesheetFormReqDto;
import com.ewsv3.ews.timesheets.dto.form.TsDepartmentDto;
import com.ewsv3.ews.timesheets.dto.form.TsJobDto;
import com.ewsv3.ews.timesheets.service.form.TimesheetFormUtils;

@Service
public class PunchService {

    public int savePunch(Long userId, Long personId, Punch punch, JdbcClient jdbcClient) {

        // Map<String, Object> punchParamMap = new HashMap<>();

        // punchParamMap.put("userId", userId);

        int insertedCounts = jdbcClient.sql(PunchUtils.insertPunch)
                .param("personId", personId)
                .param("departmentId", punch.departmentId())
                .param("jobTitleId", punch.jobTitleId())
                .param("punchTime", LocalDateTime.now())
                .param("punchType", punch.punchType())
                .param("createdBy", userId)
                .param("lastUpdatedBy", userId)
                .param("timeType", punch.timeType())
                .update();

        return insertedCounts;
    }

    public List<PunchResponse> getPunchData(Long personId, int offset, int pageSize, JdbcClient jdbcClient) {

        List<PunchResponse> punchList = jdbcClient.sql(PunchUtils.getPunchData)
                .param("personId", personId)
                .param("offset", offset)
                .param("pageSize", pageSize)
                .query(PunchResponse.class)
                .list();

        return punchList;
    }

    public List<TimeType> getTimeTypes(JdbcClient jdbcClient) {
        List<TimeType> timeTypelist = jdbcClient.sql(PunchUtils.timeTypeSql)
                .query(TimeType.class)
                .list();

        return timeTypelist;
    }

    public List<TsDepartmentDto> getDepartments(Long userId, Long personId, JdbcClient jdbcClient) {
        List<TsDepartmentDto> tsDepartmentDtoList = jdbcClient.sql(TimesheetFormUtils.sqlDepartmentList)
                .param("personId", personId)
                .query(TsDepartmentDto.class)
                .list();

        return tsDepartmentDtoList;
    }

    public List<TsJobDto> getJobs(Long userId, Long personId, JdbcClient jdbcClient) {
        List<TsJobDto> tsJobDtoList = jdbcClient.sql(TimesheetFormUtils.sqlJobList)
                .param("personId", personId)
                .query(TsJobDto.class)
                .list();

        return tsJobDtoList;
    }
}
