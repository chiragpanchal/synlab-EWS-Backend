package com.ewsv3.ews.request.controller;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.request.dto.*;
import com.ewsv3.ews.request.service.RequestService;
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

@RestController
@RequestMapping("/api/requests")
public class RequestController {

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
        try {
            List<RequestMaster> requestMaster = this.requestService.getRequestMaster(this.jdbcClient);
            return new ResponseEntity<>(requestMaster, HttpStatus.OK);
        } catch (Exception exception) {
            // return new ResponseEntity<>(null,
            // HttpStatus.valueOf(exception.getMessage()));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("person-requests")
    @CrossOrigin
    public ResponseEntity<List<RequestResp>> getRequests(@RequestHeader Map<String, String> headers,
                                                         @RequestBody PersonRequestReqBody reqBody) {

        try {
            Long userId = getCurrentUserId();
            System.out.println("getRequests > userId:" + userId);
            System.out.println("getRequests > reqBody:" + reqBody);

            List<RequestResp> requests = this.requestService.getRequests(reqBody.personId(),
                    LocalDate.ofInstant(reqBody.startDate().toInstant(), ZoneId.systemDefault()),
                    LocalDate.ofInstant(reqBody.endDate().toInstant(), ZoneId.systemDefault()), this.jdbcClient);
            System.out.println("getRequests > requests:" + requests);

            return new ResponseEntity<>(requests, HttpStatus.OK);
        } catch (Exception exception) {
            // return new ResponseEntity<>(null,
            // HttpStatus.valueOf(exception.getMessage()));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("new")
    @CrossOrigin
    public ResponseEntity<String> createRequest(@RequestHeader Map<String, String> headers,
            @RequestBody NewRequestReqBody reqBody) {

        System.out.println("createRequest reqBody" + reqBody);

        try {
            Long userId = getCurrentUserId();
            System.out.println("getRequests > userId:" + userId);
            String itemKey = this.requestService.createRequest(userId, reqBody, jdbcClient);
            System.out.println("createRequest itemKey:" + itemKey);
            return new ResponseEntity<>(itemKey, HttpStatus.OK);
        } catch (Exception exception) {
            System.out.println("getRequests > exception:" + exception.getMessage());

            String regex = "~~(.*?)~~";
            String errorText = "";
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
            java.util.regex.Matcher matcher = pattern.matcher(exception.getMessage());

            if (matcher.find()) {
                // Extracting the string between `~~`
                errorText = matcher.group(1);
            } else {
                errorText = exception.getMessage();
            }

            return new ResponseEntity<>(errorText, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("req-approvals")
    @CrossOrigin
    public ResponseEntity<List<RequestApproval>> getRequestApprovals(@RequestHeader Map<String, String> headers,
            @RequestBody RequestApprovalReqBody reqBody) {

        System.out.println("getRequestApprovals reqBody:" + reqBody);

        try {
            Long userId = getCurrentUserId();
            System.out.println("getRequests > userId:" + userId);
            List<RequestApproval> requestApprovals = this.requestService.getRequestApprovals(reqBody.itemKey(),
                    this.jdbcClient);
            return new ResponseEntity<>(requestApprovals, HttpStatus.OK);

        } catch (Exception exception) {
            System.out.println("getRequests > exception:" + exception.getMessage());
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("dest-rosters")
    @CrossOrigin
    public ResponseEntity<List<DestinationRosterResponseBody>> getDestinationRosters(@RequestHeader Map<String, String> headers,
                                                                     @RequestBody DestinationRosterReqBody reqBody) {

        System.out.println("getDestinationRosters reqBody:" + reqBody);

        try {

            List<DestinationRosterResponseBody> destinationRosters = this.requestService.getDestinationRosters(reqBody, this.jdbcClient);
            return new ResponseEntity<>(destinationRosters, HttpStatus.OK);

        } catch (Exception exception) {
            System.out.println("getRequests > exception:" + exception.getMessage());
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
