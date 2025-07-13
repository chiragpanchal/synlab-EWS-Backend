package com.ewsv3.ews.dashboard.controller;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.dashboard.dto.AwaitingActionsDto;
import com.ewsv3.ews.dashboard.dto.PendingRequests;
import com.ewsv3.ews.dashboard.dto.PendingTeamRequestsDto;
import com.ewsv3.ews.dashboard.dto.TeamViolations;
import com.ewsv3.ews.dashboard.service.DashboardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final JdbcClient jdbcClient;
    private final DashboardService dashboardService;

    public DashboardController( JdbcClient jdbcClient, DashboardService dashboardService) {
        this.jdbcClient = jdbcClient;
        this.dashboardService = dashboardService;
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getUserId();
        }
        throw new RuntimeException("User not authenticated or invalid token");
    }

    private Long getCurrentPersonId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getEmployeeId();
        }
        throw new RuntimeException("User not authenticated or invalid token");
    }

    @PostMapping("/requests")
    public ResponseEntity<List<PendingRequests>> getPendingRequestsCounts(@RequestHeader Map<String, String> headers) {

        try {

            List<PendingRequests> pendingRequests = this.dashboardService.getPendingRequests(getCurrentUserId(),
                    this.jdbcClient);
            return new ResponseEntity<>(pendingRequests, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @PostMapping("/team-violations")
    public ResponseEntity<List<TeamViolations>> getTeamViolations(@RequestHeader Map<String, String> headers) {
        try {


            List<TeamViolations> teamViolations = this.dashboardService.getTeamViolations(getCurrentUserId(),
                    this.jdbcClient);
            return new ResponseEntity<>(teamViolations, HttpStatus.OK);

        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/pending-team-requests")
    public ResponseEntity<List<PendingTeamRequestsDto>> getPendingTeamRequests(
            @RequestHeader Map<String, String> headers) {

        try {

            List<PendingTeamRequestsDto> pendingTeamRequests = this.dashboardService
                    .getPendingTeamRequests(getCurrentUserId(), this.jdbcClient);
            return new ResponseEntity<>(pendingTeamRequests, HttpStatus.OK);
        } catch (Exception exception) {
            // return new ResponseEntity<>(null,
            // HttpStatus.valueOf(exception.getMessage()));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/awaiting-actions")
    public ResponseEntity<List<AwaitingActionsDto>> getAwaitingActions(@RequestHeader Map<String, String> headers) {

        try {

            List<AwaitingActionsDto> awaitingActions = this.dashboardService.getAwaitingActions(getCurrentUserId(),
                    this.jdbcClient);
            return new ResponseEntity<>(awaitingActions, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
