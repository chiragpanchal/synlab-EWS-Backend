package com.ewsv3.ews.masters.controller;


import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.masters.dto.*;
import com.ewsv3.ews.masters.service.MasterDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/masters")
public class MastersController {

    private static final Logger logger = LoggerFactory.getLogger(MastersController.class);

    private final MasterDataService masterDataService;
    private final JdbcClient jdbcClient;

    public MastersController(MasterDataService masterDataService, JdbcClient jdbcClient) {
        this.masterDataService = masterDataService;
        this.jdbcClient = jdbcClient;
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getUserId();
        }
        throw new RuntimeException("User not authenticated or invalid token");
    }

    @GetMapping("/timekeeper-profiles")
    @CrossOrigin
    public ResponseEntity<List<TimekeeperProfiles>> getTimekeeperProfiles(@RequestHeader Map<String, String> header
    ) {
        logger.info("GET_TIMEKEEPER_PROFILES - Entry - Time: {}, UserId: {}", LocalDateTime.now(), getCurrentUserId());

        try {
            // System.out.println("timekeeper-profiles > headers" + header);
            List<TimekeeperProfiles> timekeeperProfiles = this.masterDataService
                    .getTimekeeperProfiles(getCurrentUserId(), jdbcClient);
            logger.info("GET_TIMEKEEPER_PROFILES - Exit - Time: {}, UserId: {}, Response Count: {}",
                LocalDateTime.now(), getCurrentUserId(), timekeeperProfiles.size());
            return new ResponseEntity<>(timekeeperProfiles, HttpStatus.OK);
        } catch (Error error) {
            logger.error("GET_TIMEKEEPER_PROFILES - Exception - Time: {}, UserId: {}, Error: {}",
                LocalDateTime.now(), getCurrentUserId(), error.getMessage(), error);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @PostMapping("/work-structure-masters")
    @CrossOrigin
    public ResponseEntity<WorkStructureMasters> getTimekeeperProfiles(@RequestHeader Map<String, String> header, @RequestBody UserProfileReqBody reqBody
    ) {
        logger.info("GET_WORK_STRUCTURE_MASTERS - Entry - Time: {}, UserId: {}, Request: {}",
            LocalDateTime.now(), getCurrentUserId(), reqBody);

        try {
            // System.out.println("work-structure-masters > headers" + header);
            // System.out.println("work-structure-masters > reqBody" + reqBody);
            WorkStructureMasters workStructureMasters = this.masterDataService.getWorkStructureMasters(getCurrentUserId(), reqBody, this.jdbcClient);
            logger.info("GET_WORK_STRUCTURE_MASTERS - Exit - Time: {}, UserId: {}, Request: {}, Response: {}",
                LocalDateTime.now(), getCurrentUserId(), reqBody, workStructureMasters);
            return new ResponseEntity<>(workStructureMasters, HttpStatus.OK);
        } catch (Error error) {
            logger.error("GET_WORK_STRUCTURE_MASTERS - Exception - Time: {}, UserId: {}, Request: {}, Error: {}",
                LocalDateTime.now(), getCurrentUserId(), reqBody, error.getMessage(), error);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @PostMapping("/shift-group-shifts-roster")
    @CrossOrigin
    public ResponseEntity<List<ShiftGroupShiftsDto>> getShiftGroupShifts(@RequestHeader Map<String, String> header, @RequestBody ShiftGroupDto reqBody
    ) {
        logger.info("shift-group-shifts-roster - Entry - Time: {}, UserId: {}, Request: {}",
                LocalDateTime.now(), getCurrentUserId(), reqBody);

        try {
            // System.out.println("work-structure-masters > headers" + header);
            // System.out.println("work-structure-masters > reqBody" + reqBody);
            List<ShiftGroupShiftsDto> shiftGroupShifts = this.masterDataService.getShiftGroupShifts(reqBody, this.jdbcClient);
            logger.info("shift-group-shifts-roster - Exit - Time: {}, UserId: {}, Request: {}, shiftGroupShifts.size(): {}",
                    LocalDateTime.now(), getCurrentUserId(), reqBody, shiftGroupShifts.size());
            return new ResponseEntity<>(shiftGroupShifts, HttpStatus.OK);
        } catch (Error error) {
            logger.error("shift-group-shifts-roster - Exception - Time: {}, UserId: {}, Request: {}, Error: {}",
                    LocalDateTime.now(), getCurrentUserId(), reqBody, error.getMessage(), error);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }




}
