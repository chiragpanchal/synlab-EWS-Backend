package com.ewsv3.ews.team.controller;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.team.dto.*;
import com.ewsv3.ews.team.service.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/team")
public class TeamController {

    private final JdbcClient jdbcClient;
    private final TeamService teamService;

    public TeamController(JdbcClient jdbcClient, TeamService teamService) {
        this.jdbcClient = jdbcClient;
        this.teamService = teamService;
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getUserId();
        }
        throw new RuntimeException("User not authenticated or invalid token");
    }


    @GetMapping("team-timecards-simple")
    public ResponseEntity<List<TeamTimecardSimple>> getTeamTimecardsSimple(@RequestHeader Map<String, String> headers,
                                                                           @RequestParam(defaultValue = "0") int page,
                                                                           @RequestParam(defaultValue = "100") int size,
                                                                           @RequestParam(defaultValue = "") String text,
                                                                           @RequestParam(defaultValue = "") String filterFlag,
                                                                           @RequestBody ProfileDatesRequestBody requestBody) {

        System.out.println("getTeamTimecardsSimple > page:" + page);
        System.out.println("getTeamTimecardsSimple > size:" + size);
        System.out.println("getTeamTimecardsSimple > text:" + text);
        System.out.println("getTeamTimecardsSimple > filterFlag:" + filterFlag);

        try {
            System.out.println("getTeamTimecardsSimple > requestBody:" + requestBody);

            List<TeamTimecardSimple> teamTimecardsSimple = this.teamService.getTeamTimecardsSimpleV2(
                    getCurrentUserId(),
                    requestBody.profileId(),
                    LocalDate.ofInstant(requestBody.startDate().toInstant(), ZoneId.systemDefault()),
                    LocalDate.ofInstant(requestBody.endDate().toInstant(), ZoneId.systemDefault()),
                    page,
                    size,
                    text,
                    filterFlag,
                    this.jdbcClient);

            System.out.println("getTeamTimecardsSimple > teamTimecardsSimple:" + teamTimecardsSimple);

            return new ResponseEntity<>(teamTimecardsSimple, HttpStatus.OK);

        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("timecards")
    public ResponseEntity<TeamMembersResponse> getTeamTimecards(@RequestHeader Map<String, String> headers,
                                                                @RequestBody ProfileDatesRequestBody requestBody) {

        try {
            System.out.println("getTeamTimecards > requestBody:" + requestBody);

            CompletableFuture<List<TeamMembers>> teamTimecards = this.teamService.getTeamTimecards2(
                    getCurrentUserId(),
                    requestBody.profileId(),
                    LocalDate.ofInstant(requestBody.startDate().toInstant(), ZoneId.systemDefault()),
                    LocalDate.ofInstant(requestBody.endDate().toInstant(), ZoneId.systemDefault()),
                    this.jdbcClient);

            System.out.println("getTeamTimecards > teamTimecards:" + teamTimecards.get());

            // TeamTimecardKpi teamTimecardKpi = this.teamService.getTeamTimecardKpi(
            // userInfo.userId(),
            // requestBody.profileId(),
            // LocalDate.ofInstant(requestBody.startDate().toInstant(),
            // ZoneId.systemDefault()),
            // LocalDate.ofInstant(requestBody.endDate().toInstant(),
            // ZoneId.systemDefault()),
            // this.jdbcClient);
            TeamTimecardKpi teamTimecardKpi = null;

            return new ResponseEntity<>(new TeamMembersResponse(teamTimecards.get(), teamTimecardKpi), HttpStatus.OK);

        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("timecards-pgn")
    public ResponseEntity<TeamMembersResponse> getTeamTimecardsPagination(@RequestHeader Map<String, String> headers,
            @RequestBody ProfileDatesRequestPgnBody requestBody) {

        try {

            // System.out.println("getTeamTimecards > requestBody:" + requestBody);

            // List<TeamMembers> teamTimecards = this.teamService.getTeamTimecards(
            // userInfo.userId(),
            // requestBody.profileId(),
            // LocalDate.ofInstant(requestBody.startDate().toInstant(),
            // ZoneId.systemDefault()),
            // LocalDate.ofInstant(requestBody.endDate().toInstant(),
            // ZoneId.systemDefault()),
            // requestBody.pageNo(),
            // requestBody.pageSize(),
            // this.jdbcClient);

            // System.out.println("getTeamTimecards > teamTimecards:" + teamTimecards);

            CompletableFuture<List<TeamMembers>> timecards5 = this.teamService.getTeamTimecards5(
                    getCurrentUserId(),
                    requestBody.profileId(),
                    LocalDate.ofInstant(requestBody.startDate().toInstant(), ZoneId.systemDefault()),
                    LocalDate.ofInstant(requestBody.endDate().toInstant(), ZoneId.systemDefault()),
                    this.jdbcClient);

            // TeamTimecardKpi teamTimecardKpi = this.teamService.getTeamTimecardKpi(
            // userInfo.userId(),
            // requestBody.profileId(),
            // LocalDate.ofInstant(requestBody.startDate().toInstant(),
            // ZoneId.systemDefault()),
            // LocalDate.ofInstant(requestBody.endDate().toInstant(),
            // ZoneId.systemDefault()),
            // this.jdbcClient);
            TeamTimecardKpi teamTimecardKpi = null;

            return new ResponseEntity<>(new TeamMembersResponse(timecards5.get(), teamTimecardKpi), HttpStatus.OK);

        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("timecards-single")
    public ResponseEntity<List<TeamTimecardObject>> getTeamTimecardsNew(@RequestHeader Map<String, String> headers,
            @RequestBody ProfileDatesRequestBody requestBody) {

        try {

            System.out.println("getTeamTimecardsNew > requestBody:" + requestBody);

            List<TeamTimecardObject> teamTimecardObjects = this.teamService.getTeamTimecards3(
                    getCurrentUserId(),
                    requestBody.profileId(),
                    LocalDate.ofInstant(requestBody.startDate().toInstant(), ZoneId.systemDefault()),
                    LocalDate.ofInstant(requestBody.endDate().toInstant(), ZoneId.systemDefault()),
                    this.jdbcClient);

            // System.out.println("getTeamTimecardsNew > teamTimecardObjects:" +
            // teamTimecardObjects);

            return new ResponseEntity<>(teamTimecardObjects, HttpStatus.OK);

        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
