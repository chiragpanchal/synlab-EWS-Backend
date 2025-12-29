package com.ewsv3.ews.openShifts.controller;

import com.ewsv3.ews.accessprofiles.controller.AccessProfileController;
import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.commons.dto.UserIdReqDto;
import com.ewsv3.ews.commons.dto.UserProfileResponse;
import com.ewsv3.ews.openShifts.dto.OpenShiftsHeader;
import com.ewsv3.ews.openShifts.service.OpenShiftService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/open-shift")
public class OpenShiftController {

    private final JdbcClient jdbcClient;
    private final OpenShiftService openShiftService;
    private static final Logger logger = LoggerFactory.getLogger(AccessProfileController.class);

    public OpenShiftController(JdbcClient jdbcClient, OpenShiftService openShiftService) {
        this.jdbcClient = jdbcClient;
        this.openShiftService = openShiftService;
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getUserId();
        }
        throw new RuntimeException("User not authenticated or invalid token");
    }

    @PostMapping("create")
    public ResponseEntity<DMLResponseDto> createOpenShifts(@RequestHeader Map<String, String> headers, @RequestBody OpenShiftsHeader reqBody) {
        logger.info("open-shift create - Entry - Time: {}, punch req data: {}",
                LocalDateTime.now(), reqBody);
        try {
//            UserProfileResponse userFromUserId = this.commonService
//                    .getUserFromUserId(new UserIdReqDto(getCurrentUserId()), jdbcClient);
//            int savePunchCounts = this.punchService.savePunch(getCurrentUserId(), userFromUserId.personId(), req,
//                    jdbcClient);
            DMLResponseDto dmlResponseDto = openShiftService.createOpenShifts(getCurrentUserId(), reqBody, this.jdbcClient);
            logger.info("open-shift create - Exit - Time: {}, Request: {}, Response dto: {}", LocalDateTime.now(),
                    reqBody, dmlResponseDto);
            return new ResponseEntity<>(dmlResponseDto, HttpStatus.OK);

        } catch (Exception exception) {
            logger.error("open-shift create - Exception - Time: {}, Request: {}, Error: {}", LocalDateTime.now(),
                    reqBody, exception.getMessage(), exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

}
