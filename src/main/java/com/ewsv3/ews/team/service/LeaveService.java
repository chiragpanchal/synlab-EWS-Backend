package com.ewsv3.ews.team.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import com.ewsv3.ews.team.dto.PersonLeaveDto;
import com.ewsv3.ews.team.dto.ProfileDatesRequestBody;

@Service
public class LeaveService {

    public List<PersonLeaveDto> getPersonLeaves(Long userId, ProfileDatesRequestBody requestBody,
            JdbcClient jdbcClient) {

        List<PersonLeaveDto> list = new ArrayList<PersonLeaveDto>();

        //System.out.println("getPersonLeaves: requestBody:" + requestBody);
        if (requestBody.profileId() == -1) {
            // self timesheets
            list = jdbcClient.sql(LeaveServiceUtils.sqlSelfLeaveData)
                    .param("personId", requestBody.personId())
                    .param("startDate", requestBody.startDate())
                    .param("endDate", requestBody.endDate())
                    .query(PersonLeaveDto.class)
                    .list();
        } else if (requestBody.profileId() == 0) {
            // line manager
            list = jdbcClient.sql(LeaveServiceUtils.sqlLineManagerLeaveData)
                    .param("userId", userId)
                    .param("startDate", requestBody.startDate())
                    .param("endDate", requestBody.endDate())
                    .query(PersonLeaveDto.class)
                    .list();

        } else {
            // Timekeeper Profile
            list = jdbcClient.sql(LeaveServiceUtils.sqlTimekeeperLeaveData)
                    .param("userId", userId)
                    .param("profileId", requestBody.profileId())
                    .param("startDate", requestBody.startDate())
                    .param("endDate", requestBody.endDate())
                    .query(PersonLeaveDto.class)
                    .list();

        }

        //System.out.println("getPersonLeaves  list.size():" + list.size());

        return list;

    }

}
