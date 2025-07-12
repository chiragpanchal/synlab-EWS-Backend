package com.ewsv3.ews.dashboard.service;

import com.ewsv3.ews.dashboard.dto.AwaitingActionsDto;
import com.ewsv3.ews.dashboard.dto.PendingRequests;
import com.ewsv3.ews.dashboard.dto.PendingTeamRequestsDto;
import com.ewsv3.ews.dashboard.dto.TeamViolations;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ewsv3.ews.dashboard.service.DashboardUtils.*;


@Service
public class DashboardService {

    public List<PendingRequests> getPendingRequests(long personId, JdbcClient jdbcClient) {

        Map<String, Object> personDateMap = new HashMap<>();
        personDateMap.put("personId", personId);

        // get permission requests counts;
        List<PendingRequests> pendingRequests = jdbcClient.sql(RequestPendingSql).params(personDateMap)
                .query(PendingRequests.class).list();

        // get self roster requests counts;
        Integer selfRosterCounts = jdbcClient.sql(SelfRosterPendingSql).params(personDateMap).query(Integer.class)
                .single();
        pendingRequests.add(new PendingRequests("Self Roster", selfRosterCounts));

        // get timesheet counts;
        Integer timesheetCounts = jdbcClient.sql(TimesheetPendingSql).params(personDateMap).query(Integer.class)
                .single();
        pendingRequests.add(new PendingRequests("Timesheets", timesheetCounts));

        System.out.println("getPendingRequests pendingRequests:" + pendingRequests);

        return pendingRequests;

    }

    public List<TeamViolations> getTeamViolations(long userId, JdbcClient jdbcClient) {
        Map<String, Object> objectMap = new HashMap<>();

        objectMap.put("userId", userId);
        // Get current week violations
        LocalDate currentDate = LocalDate.now();
        LocalDate startOfWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate startOfPrevious5thWeek = startOfWeek.minus(5, ChronoUnit.WEEKS);
        System.out.println("Start of the week: " + startOfWeek);
        System.out.println("Start of the previous 5th week: " + startOfPrevious5thWeek);

        objectMap.put("startDate", startOfPrevious5thWeek);
        objectMap.put("endDate", currentDate);

        List<TeamViolations> prevWeekViolationsList = jdbcClient.sql(ViolationCountsSql).params(objectMap)
                .query(TeamViolations.class).list();
        System.out.println("getTeamViolations prevWeekViolationsList:" + prevWeekViolationsList);

        return prevWeekViolationsList;

    }



    public List<PendingTeamRequestsDto> getPendingTeamRequests(long userId, JdbcClient jdbcClient) {

        Map<String, Object> personDateMap = new HashMap<>();
        personDateMap.put("userId", userId);

        // get open requests counts;
        List<PendingTeamRequestsDto> pendingTeamRequestsDtos = jdbcClient.sql(PendingTeamRequestsSql)
                .params(personDateMap).query(PendingTeamRequestsDto.class).list();

        System.out.println("getPendingTeamRequests pendingTeamRequestsDtos:" + pendingTeamRequestsDtos);

        return pendingTeamRequestsDtos;

    }

    public List<AwaitingActionsDto> getAwaitingActions(long userId, JdbcClient jdbcClient) {

        Map<String, Object> personDateMap = new HashMap<>();
        personDateMap.put("userId", userId);

        // get open requests counts;
        List<AwaitingActionsDto> awaitingActionsDtos = jdbcClient.sql(AwaitingActionsSql).params(personDateMap)
                .query(AwaitingActionsDto.class).list();

        System.out.println("getAwaitingActions awaitingActionsDtos:" + awaitingActionsDtos);

        return awaitingActionsDtos;

    }

}
