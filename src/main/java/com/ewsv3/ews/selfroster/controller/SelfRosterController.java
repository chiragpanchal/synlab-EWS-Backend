package com.ewsv3.ews.selfroster.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.selfroster.dto.SelfRoster;
import com.ewsv3.ews.selfroster.service.SelfRosterService;

@RestController
@RequestMapping("/api/self-rosters/")
public class SelfRosterController {
    private static final Logger logger = LoggerFactory.getLogger(SelfRosterController.class);

    private final SelfRosterService selfRosterService;
    private final JdbcClient jdbcClient;

    public SelfRosterController(SelfRosterService selfRosterService, JdbcClient jdbcClient) {
        this.selfRosterService = selfRosterService;
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

    @GetMapping("get-self-rosters")
    public ResponseEntity<List<SelfRoster>> getSelfRosters(@RequestHeader Map<String, String> header) {
        logger.info("GET_SELF_ROSTERS - Entry - Time: {}, UserId: {}", LocalDateTime.now(), getCurrentUserId());
        try {
            //System.out.println("get-self-rosters");

            List<SelfRoster> selfRosters = this.selfRosterService.getSelfRosters(getCurrentUserId(), jdbcClient);
            logger.info("GET_SELF_ROSTERS - Exit - Time: {}, Response count: {}", LocalDateTime.now(), selfRosters.size());
            return new ResponseEntity<>(selfRosters, HttpStatus.OK);

        } catch (Exception exception) {
            //System.out.println(exception.getMessage());
            logger.error("GET_SELF_ROSTERS - Exception - Time: {}, UserId: {}, Error: {}",
                    LocalDateTime.now(), getCurrentUserId(), exception.getMessage(), exception);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("save-self-rosters")
    public ResponseEntity<DMLResponseDto> saveSelfRosters(
            @RequestHeader Map<String, String> header,
            @RequestBody SelfRoster reqDto) {
        logger.info("SAVE_SELF_ROSTERS - Entry - Time: {}, Request: {}", LocalDateTime.now(), reqDto);

        try {
            //System.out.println("save-self-rosters reqDto:" + reqDto);

            DMLResponseDto dmlResponseDto = this.selfRosterService.saveSelfRoster(getCurrentUserId(), reqDto,
                    jdbcClient);
            logger.info("SAVE_SELF_ROSTERS - Exit - Time: {}, Response: {}", LocalDateTime.now(), dmlResponseDto);
            return new ResponseEntity<>(dmlResponseDto, HttpStatus.OK);

        } catch (Exception exception) {
            //System.out.println(exception.getMessage());
            logger.error("SAVE_SELF_ROSTERS - Exception - Time: {}, Request: {}, Error: {}",
                    LocalDateTime.now(), reqDto, exception.getMessage(), exception);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

}
