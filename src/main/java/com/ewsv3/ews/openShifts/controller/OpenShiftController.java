package com.ewsv3.ews.openShifts.controller;

import com.ewsv3.ews.accessprofiles.controller.AccessProfileController;
import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.commons.dto.UserIdReqDto;
import com.ewsv3.ews.commons.dto.UserProfileResponse;
import com.ewsv3.ews.commons.service.CommonService;
import com.ewsv3.ews.openShifts.dto.OpenShifListTkRespDto;
import com.ewsv3.ews.openShifts.dto.OpenShiftCountsRespDto;
import com.ewsv3.ews.openShifts.dto.OpenShiftProfileDatesReqDto;
import com.ewsv3.ews.openShifts.dto.OpenShiftsHeader;
import com.ewsv3.ews.openShifts.dto.allocation.*;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/open-shift")
public class OpenShiftController {

    private final JdbcClient jdbcClient;
    private final OpenShiftService openShiftService;
    private final CommonService commonService;
    private static final Logger logger = LoggerFactory.getLogger(AccessProfileController.class);

    public OpenShiftController(JdbcClient jdbcClient, OpenShiftService openShiftService, CommonService commonService) {
        this.jdbcClient = jdbcClient;
        this.openShiftService = openShiftService;
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

    @PostMapping("create")
    public ResponseEntity<DMLResponseDto> createOpenShifts(@RequestHeader Map<String, String> headers, @RequestBody OpenShiftsHeader reqBody) {
        logger.info("open-shift create - Entry - Time: {}, reqBody: {}",
                LocalDateTime.now(), reqBody);
        try {
//            UserProfileResponse userFromUserId = this.commonService
//                    .getUserFromUserId(new UserIdReqDto(getCurrentUserId()), jdbcClient);
//            int savePunchCounts = this.punchService.savePunch(getCurrentUserId(), userFromUserId.personId(), req,
//                    jdbcClient);
            DMLResponseDto dmlResponseDto = openShiftService.createOpenShifts(getCurrentUserId(), reqBody, this.jdbcClient);
            logger.info("open-shift create - Exit - Time: {}, reqBody: {}, Response dto: {}", LocalDateTime.now(),
                    reqBody, dmlResponseDto);
            return new ResponseEntity<>(dmlResponseDto, HttpStatus.OK);

        } catch (Exception exception) {
            logger.error("open-shift create - Exception - Time: {}, reqBody: {}, Error: {}", LocalDateTime.now(),
                    reqBody, exception.getMessage(), exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @PostMapping("get-open-shift-counts-tk")
    public ResponseEntity<OpenShiftCountsRespDto> getOpenshiftsCountsTk(@RequestHeader Map<String, String> headers, @RequestBody OpenShiftProfileDatesReqDto reqBody) {
        logger.info("get-open-shift-counts-tk - Entry - Time: {}, reqBody: {}",
                LocalDateTime.now(), reqBody);
        try {

            Long countTk = this.openShiftService.getOpenshiftCountTk(getCurrentUserId(), reqBody, this.jdbcClient);

            logger.info("get-open-shift-counts-tk - Exit - Time: {}, reqBody: {}",
                    LocalDateTime.now(), reqBody);
            return new ResponseEntity<>(new OpenShiftCountsRespDto(countTk), HttpStatus.OK);

        } catch (Exception exception) {
            logger.error("get-open-shift-counts-tk - Exception - Time: {}, reqBody: {}, Error: {}", LocalDateTime.now(),
                    reqBody, exception.getMessage(), exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("get-open-shift-list-tk")
    public ResponseEntity<List<OpenShifListTkRespDto>> getOpenshiftListTk(@RequestHeader Map<String, String> headers, @RequestBody OpenShiftProfileDatesReqDto reqBody) {
        logger.info("get-open-shift-list-tk - Entry - Time: {}, reqBody: {}",
                LocalDateTime.now(), reqBody);
        try {

            List<OpenShifListTkRespDto> openshiftListTk = this.openShiftService.getOpenshiftListTk(getCurrentUserId(), reqBody, this.jdbcClient);

            logger.info("get-open-shift-list-tk - Exit - Time: {}, reqBody: {}",
                    LocalDateTime.now(), reqBody);
            return new ResponseEntity<>(openshiftListTk, HttpStatus.OK);

        } catch (Exception exception) {
            logger.error("get-open-shift-list-tk - Exception - Time: {}, reqBody: {}, Error: {}", LocalDateTime.now(),
                    reqBody, exception.getMessage(), exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("get-open-shift-suggestions")
    public ResponseEntity<List<SuggestionPersonDto>> getSuggestPersonList(@RequestHeader Map<String, String> headers, @RequestBody OpenShiftProfileDatesReqDto reqBody) {

        logger.info("get-open-shift-suggest-person - Entry - Time: {}, reqBody: {}",
                LocalDateTime.now(), reqBody);
        try {

            List<SuggestionPersonDto> suggestPersonList = this.openShiftService.getSuggestPersonList(getCurrentUserId(), reqBody, this.jdbcClient);

            logger.info("get-open-shift-suggest-person - Exit - Time: {}, reqBody: {} , response counts {}",
                    LocalDateTime.now(), reqBody, suggestPersonList.size());
            return new ResponseEntity<>(suggestPersonList, HttpStatus.OK);

        } catch (Exception exception) {
            logger.error("get-open-shift-suggest-person - Exception - Time: {}, reqBody: {}, Error: {}", LocalDateTime.now(),
                    reqBody, exception.getMessage(), exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @GetMapping("my-open-shifts")
    public ResponseEntity<List<OpenShifListTkRespDto>> getEmployeeOpenShifts(@RequestHeader Map<String, String> headers) {

        logger.info("my-open-shifts - Entry - Time: {}",
                LocalDateTime.now());
        try {

            List<OpenShifListTkRespDto> employeeOpenShifts = this.openShiftService.getEmployeeOpenShifts(getCurrentUserId(), this.jdbcClient);

            logger.info("my-open-shifts - Exit - Time: {} , response counts {}",
                    LocalDateTime.now(), employeeOpenShifts.size());
            return new ResponseEntity<>(employeeOpenShifts, HttpStatus.OK);

        } catch (Exception exception) {
            logger.error("my-open-shifts - Exception - Time: {},  Error: {}", LocalDateTime.now(),
                    exception.getMessage(), exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("bid-open-shift")
    public ResponseEntity<DMLResponseDto> bidOpenShift(@RequestHeader Map<String, String> headers, @RequestBody PersonOpenShiftBidReqDto reqDto) {

        logger.info("bid-open-shift - Entry -  Time: {}, req {} ,",
                LocalDateTime.now(), reqDto);
        try {

            DMLResponseDto dmlResponseDto = this.openShiftService.bidOpenShift(getCurrentUserId(), reqDto, this.jdbcClient);

            logger.info("bid-open-shift - Exit - Time: {} , response  {}",
                    LocalDateTime.now(), dmlResponseDto);
            return new ResponseEntity<>(dmlResponseDto, HttpStatus.OK);

        } catch (Exception exception) {
            logger.error("bid-open-shift - Exception -  Time: {}, req {},  Error: {}", LocalDateTime.now(),
                    exception.getMessage(), reqDto, exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("open-shift-total-appl")
    public ResponseEntity<TotalApplictionCountsDto> getTotalApplications(@RequestHeader Map<String, String> headers, @RequestBody PersonOpenShiftBidReqDto reqDto){
        logger.info("open-shift-total-appl - Entry -  Time: {}, req {} ,",
                LocalDateTime.now(), reqDto);
        try {

            TotalApplictionCountsDto totalApplications = this.openShiftService.getTotalApplications(getCurrentUserId(), reqDto, this.jdbcClient);

            logger.info("open-shift-total-appl - Exit - Time: {} , response  {}",
                    LocalDateTime.now(), totalApplications);
            return new ResponseEntity<>(totalApplications, HttpStatus.OK);

        } catch (Exception exception) {
            logger.error("open-shift-total-appl - Exception -  Time: {}, req {},  Error: {}", LocalDateTime.now(),
                    exception.getMessage(), reqDto, exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("self-appl-list")
    public ResponseEntity<SelfApplicationRespSto> getSelfApplications(@RequestHeader Map<String, String> headers, @RequestBody PersonOpenShiftBidReqDto reqDto){
        logger.info("self-appl-list - Entry -  Time: {}, req {} ,",
                LocalDateTime.now(), reqDto);
        try {

            UserProfileResponse userFromUserId = this.commonService
                    .getUserFromUserId(new UserIdReqDto(getCurrentUserId()), jdbcClient);
            SelfApplicationRespSto selfApplications = this.openShiftService.getSelfApplications(getCurrentUserId(), userFromUserId.personId(), reqDto, this.jdbcClient);

            logger.info("self-appl-list - Exit - Time: {} , response  {}",
                    LocalDateTime.now(), selfApplications);
            return new ResponseEntity<>(selfApplications, HttpStatus.OK);

        } catch (Exception exception) {
            logger.error("self-appl-list - Exception -  Time: {}, req {},  Error: {}", LocalDateTime.now(),
                    exception.getMessage(), reqDto, exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
