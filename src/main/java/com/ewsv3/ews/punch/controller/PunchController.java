package com.ewsv3.ews.punch.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ewsv3.ews.accessprofiles.controller.AccessProfileController;
import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.commons.dto.UserIdReqDto;
import com.ewsv3.ews.commons.dto.UserProfileResponse;
import com.ewsv3.ews.commons.service.CommonService;
import com.ewsv3.ews.punch.dto.Punch;
import com.ewsv3.ews.punch.dto.PunchResponse;
import com.ewsv3.ews.punch.service.PunchService;

@RestController
@RequestMapping("/api/punch")
public class PunchController {

    public final PunchService punchService;
    public final JdbcClient jdbcClient;
    public final CommonService commonService;

    private static final Logger logger = LoggerFactory.getLogger(AccessProfileController.class);

    public PunchController(PunchService punchService, JdbcClient jdbcClient, CommonService commonService) {
        this.punchService = punchService;
        this.jdbcClient = jdbcClient;
        this.commonService = commonService;
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getUserId();
        }
        throw new RuntimeException("User not authenticated or invalid token");
    }

    @PostMapping
    public ResponseEntity<DMLResponseDto> savePunch(@RequestHeader Map<String, String> headers,
            @RequestBody Punch req) {

        logger.info("SAVE PUNCH - Entry - Time: {}, punch req data: {}",
                LocalDateTime.now(), req);
        try {
            UserProfileResponse userFromUserId = this.commonService
                    .getUserFromUserId(new UserIdReqDto(getCurrentUserId()), jdbcClient);
            int savePunchCounts = this.punchService.savePunch(getCurrentUserId(), userFromUserId.personId(), req,
                    jdbcClient);
            logger.info("SAVE PUNCH - Exit - Time: {}, Request: {}, saved Count: {}", LocalDateTime.now(),
                    req, savePunchCounts);
            return new ResponseEntity<>(new DMLResponseDto("S", "Punch Saved Successfully"), HttpStatus.OK);

        } catch (Exception exception) {
            logger.error("SAVE PUNCH - Exception - Time: {}, Request: {}, Error: {}", LocalDateTime.now(),
                    req, exception.getMessage(), exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping
    public ResponseEntity<List<PunchResponse>> getPunchData(@RequestHeader Map<String, String> headers,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        UserProfileResponse userFromUserId = this.commonService
                .getUserFromUserId(new UserIdReqDto(getCurrentUserId()), jdbcClient);
        logger.info("getPunchData - Entry - Time: {}, personId: {}",
                LocalDateTime.now(), userFromUserId.personId());

        try {

            List<PunchResponse> punchData = this.punchService.getPunchData(userFromUserId.personId(), page, size,
                    jdbcClient);
            logger.info("getPunchData - Exit - Time: {}, personId: {}, Response Count: {}", LocalDateTime.now(),
                    userFromUserId.personId(), punchData.size());
            return new ResponseEntity<>(punchData, HttpStatus.OK);
        } catch (Exception exception) {
            logger.error("getPunchData - Exception - Time: {}, personId: {}, Error: {}", LocalDateTime.now(),
                    userFromUserId.personId(), exception.getMessage(), exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
