package com.ewsv3.ews.request.controller;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.request.dto.*;
import com.ewsv3.ews.request.service.RequestService;
import com.ewsv3.ews.timesheets.dto.submission.TimesheetActionReqBody;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/requests")
public class RequestController {
    private static final Logger logger = LoggerFactory.getLogger(RequestController.class);

    private final JdbcClient jdbcClient;
    private final RequestService requestService;

    public RequestController(JdbcClient jdbcClient, RequestService requestService) {
        this.jdbcClient = jdbcClient;
        this.requestService = requestService;
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getUserId();
        }
        throw new RuntimeException("User not authenticated or invalid token");
    }

    @GetMapping("req-master")
    @CrossOrigin
    public ResponseEntity<List<RequestMaster>> getRequestMaster(@RequestHeader Map<String, String> headers) {
        logger.info("GET_REQUEST_MASTER - Entry - Time: {}", LocalDateTime.now());
        try {
            List<RequestMaster> requestMaster = this.requestService.getRequestMaster(this.jdbcClient);
            logger.info("GET_REQUEST_MASTER - Exit - Time: {}, Response Count: {}", LocalDateTime.now(), requestMaster.size());
            return new ResponseEntity<>(requestMaster, HttpStatus.OK);
        } catch (Exception exception) {
            logger.error("GET_REQUEST_MASTER - Exception - Time: {}, Error: {}", LocalDateTime.now(), exception.getMessage(), exception);
            // return new ResponseEntity<>(null,
            // HttpStatus.valueOf(exception.getMessage()));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("person-requests")
    @CrossOrigin
    public ResponseEntity<List<RequestResp>> getRequests(@RequestHeader Map<String, String> headers,
            @RequestBody PersonRequestReqBody reqBody) {
        logger.info("GET_REQUESTS - Entry - Time: {}, Request: {}", LocalDateTime.now(), reqBody);
        try {
            Long userId = getCurrentUserId();
            //System.out.println("getRequests > userId:" + userId);
            //System.out.println("getRequests > reqBody:" + reqBody);

            List<RequestResp> requests = this.requestService.getRequests(reqBody.personId(),
                    LocalDate.ofInstant(reqBody.startDate().toInstant(), ZoneId.systemDefault()),
                    LocalDate.ofInstant(reqBody.endDate().toInstant(), ZoneId.systemDefault()), this.jdbcClient);
            //System.out.println("getRequests > requests:" + requests);

            logger.info("GET_REQUESTS - Exit - Time: {}, UserId: {}, Request: {}, Response Count: {}", LocalDateTime.now(), userId, reqBody, requests.size());
            return new ResponseEntity<>(requests, HttpStatus.OK);
        } catch (Exception exception) {
            logger.error("GET_REQUESTS - Exception - Time: {}, Request: {}, Error: {}", LocalDateTime.now(), reqBody, exception.getMessage(), exception);
            // return new ResponseEntity<>(null,
            // HttpStatus.valueOf(exception.getMessage()));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("new")
    @CrossOrigin
    public ResponseEntity<String> createRequest(@RequestHeader Map<String, String> headers,
            @RequestBody NewRequestReqBody reqBody) {
        logger.info("CREATE_REQUEST - Entry - Time: {}, Request: {}", LocalDateTime.now(), reqBody);
        //System.out.println("createRequest reqBody" + reqBody);

        try {
            Long userId = getCurrentUserId();
            //System.out.println("getRequests > userId:" + userId);
            String itemKey = this.requestService.createRequest(userId, reqBody, jdbcClient);
            //System.out.println("createRequest itemKey:" + itemKey);
            logger.info("CREATE_REQUEST - Exit - Time: {}, UserId: {}, Request: {}, ItemKey: {}", LocalDateTime.now(), userId, reqBody, itemKey);
            return new ResponseEntity<>(itemKey, HttpStatus.OK);
        } catch (Exception exception) {
            //System.out.println("getRequests > exception:" + exception.getMessage());

            String errorMsg = exception.getMessage();
            String extracted = null;
            java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("~~(.*?)~~").matcher(errorMsg);
            if (matcher.find()) {
                extracted = matcher.group(1);
                //System.out.println("Extracted error: " + extracted);
            }

            logger.error("CREATE_REQUEST - Exception - Time: {}, Request: {}, Error: {}", LocalDateTime.now(), reqBody, extracted != null ? extracted : exception.getMessage(), exception);
            // String regex = "~~(.*?)~~";
            // String errorText = "";
            // java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
            // java.util.regex.Matcher matcher = pattern.matcher(exception.getMessage());
            //
            // if (matcher.find()) {
            // // Extracting the string between `~~`
            // errorText = matcher.group(1);
            // } else {
            // errorText = exception.getMessage();
            // }

            return new ResponseEntity<>(extracted, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("req-approvals")
    @CrossOrigin
    public ResponseEntity<List<RequestApproval>> getRequestApprovals(@RequestHeader Map<String, String> headers,
            @RequestBody RequestApprovalReqBody reqBody) {
        logger.info("GET_REQUEST_APPROVALS - Entry - Time: {}, Request: {}", LocalDateTime.now(), reqBody);
        //System.out.println("getRequestApprovals reqBody:" + reqBody);

        try {
            Long userId = getCurrentUserId();
            //System.out.println("getRequests > userId:" + userId);
            List<RequestApproval> requestApprovals = this.requestService.getRequestApprovals(reqBody.itemKey(),
                    this.jdbcClient);
            logger.info("GET_REQUEST_APPROVALS - Exit - Time: {}, UserId: {}, Request: {}, Response Count: {}", LocalDateTime.now(), userId, reqBody, requestApprovals.size());
            return new ResponseEntity<>(requestApprovals, HttpStatus.OK);

        } catch (Exception exception) {
            //System.out.println("getRequests > exception:" + exception.getMessage());
            logger.error("GET_REQUEST_APPROVALS - Exception - Time: {}, Request: {}, Error: {}", LocalDateTime.now(), reqBody, exception.getMessage(), exception);
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("dest-rosters")
    @CrossOrigin
    public ResponseEntity<List<DestinationRosterResponseBody>> getDestinationRosters(
            @RequestHeader Map<String, String> headers,
            @RequestBody DestinationRosterReqBody reqBody) {
        logger.info("GET_DESTINATION_ROSTERS - Entry - Time: {}, Request: {}", LocalDateTime.now(), reqBody);
        //System.out.println("getDestinationRosters reqBody:" + reqBody);

        try {

            List<DestinationRosterResponseBody> destinationRosters = this.requestService.getDestinationRosters(reqBody,
                    this.jdbcClient);
            logger.info("GET_DESTINATION_ROSTERS - Exit - Time: {}, Request: {}, Response Count: {}", LocalDateTime.now(), reqBody, destinationRosters.size());
            return new ResponseEntity<>(destinationRosters, HttpStatus.OK);

        } catch (Exception exception) {
            //System.out.println("getRequests > exception:" + exception.getMessage());
            logger.error("GET_DESTINATION_ROSTERS - Exception - Time: {}, Request: {}, Error: {}", LocalDateTime.now(), reqBody, exception.getMessage(), exception);
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("request-notifications")
    @CrossOrigin
    public ResponseEntity<List<RequestNotificationResponse>> getRequestNotifications(
            @RequestHeader Map<String, String> headers) {
        logger.info("GET_REQUEST_NOTIFICATIONS - Entry - Time: {}", LocalDateTime.now());
        //System.out.println("start request-notifications");

        try {
            List<RequestNotificationResponse> requestNotifications = this.requestService
                    .getRequestNotifications(getCurrentUserId(), jdbcClient);
            logger.info("GET_REQUEST_NOTIFICATIONS - Exit - Time: {}, Response Count: {}", LocalDateTime.now(), requestNotifications.size());
            logger.info("GET_REQUEST_NOTIFICATIONS - Exit - Time: {}, Response :{}", LocalDateTime.now(), requestNotifications);
            return new ResponseEntity<>(requestNotifications, HttpStatus.OK);

        } catch (Exception exception) {
            //System.out.println("request-notifications > exception:" + exception.getMessage());
            logger.error("GET_REQUEST_NOTIFICATIONS - Exception - Time: {}, Error: {}", LocalDateTime.now(), exception.getMessage(), exception);
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("request-action")
    public ResponseEntity<DMLResponseDto> actionTimesheets(@RequestHeader Map<String, String> headers,
            @RequestBody RequestActionReqBody reqBody) {
        logger.info("ACTION_REQUESTS - Entry - Time: {}, Request: {}", LocalDateTime.now(), reqBody);
        try {
            //System.out.println("action reqBody:" + reqBody);
            DMLResponseDto dmlResponseDto = this.requestService.actionRequests(getCurrentUserId(), reqBody);
            logger.info("ACTION_REQUESTS - Exit - Time: {}, Request: {}, Response: {}", LocalDateTime.now(), reqBody, dmlResponseDto);
            return new ResponseEntity<>(dmlResponseDto, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("ACTION_REQUESTS - Exception - Time: {}, Request: {}, Error: {}", LocalDateTime.now(), reqBody, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

}
