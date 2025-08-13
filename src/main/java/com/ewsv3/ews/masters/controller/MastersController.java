package com.ewsv3.ews.masters.controller;


import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.masters.dto.TimekeeperProfiles;
import com.ewsv3.ews.masters.dto.UserDateRequestBody;
import com.ewsv3.ews.masters.dto.UserProfileReqBody;
import com.ewsv3.ews.masters.dto.WorkStructureMasters;
import com.ewsv3.ews.masters.service.MasterDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/masters")
public class MastersController {


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

        try {
            System.out.println("timekeeper-profiles > headers" + header);
            List<TimekeeperProfiles> timekeeperProfiles = this.masterDataService
                    .getTimekeeperProfiles(getCurrentUserId(), jdbcClient);
            return new ResponseEntity<>(timekeeperProfiles, HttpStatus.OK);
        } catch (Error error) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @PostMapping("/work-structure-masters")
    @CrossOrigin
    public ResponseEntity<WorkStructureMasters> getTimekeeperProfiles(@RequestHeader Map<String, String> header, @RequestBody UserProfileReqBody reqBody
    ) {

        try {
            System.out.println("work-structure-masters > headers" + header);
            System.out.println("work-structure-masters > reqBody" + reqBody);
            WorkStructureMasters workStructureMasters = this.masterDataService.getWorkStructureMasters(getCurrentUserId(), reqBody, this.jdbcClient);
            return new ResponseEntity<>(workStructureMasters, HttpStatus.OK);
        } catch (Error error) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }


}
