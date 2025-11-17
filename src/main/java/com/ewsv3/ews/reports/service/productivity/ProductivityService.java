package com.ewsv3.ews.reports.service.productivity;

import com.ewsv3.ews.reports.dto.UserProfileDateReqDto;
import com.ewsv3.ews.reports.dto.productivity.TimecardRespDto;
import com.ewsv3.ews.reports.dto.productivity.TimesheetRespDto;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ewsv3.ews.reports.service.productivity.ProductivityUtils.timecardInfoSql;
import static com.ewsv3.ews.reports.service.productivity.ProductivityUtils.timesheetInfoSql;

@Service
public class ProductivityService {

    public List<TimecardRespDto> getProductivityTimecardInfo(Long userId, UserProfileDateReqDto reqDto, JdbcClient jdbcClient){

        List<TimecardRespDto> timecardRespDtos = jdbcClient.sql(timecardInfoSql)
                .param("userId", userId)
                .param("profileId", reqDto.profileId())
                .param("startDate", reqDto.startDate())
                .param("endDate", reqDto.endDate())
                .query(TimecardRespDto.class)
                .list();

        return timecardRespDtos;
    }

    public List<TimesheetRespDto> getProductivityTimesheetInfo(Long userId, UserProfileDateReqDto reqDto, JdbcClient jdbcClient){

        List<TimesheetRespDto> timesheetRespDtos = jdbcClient.sql(timesheetInfoSql)
                .param("userId", userId)
                .param("profileId", reqDto.profileId())
                .param("startDate", reqDto.startDate())
                .param("endDate", reqDto.endDate())
                .query(TimesheetRespDto.class)
                .list();

        return timesheetRespDtos;
    }
}
