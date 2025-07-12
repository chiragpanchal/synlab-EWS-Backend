package com.ewsv3.ews.timecard.controller;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.timecard.dto.PersonDateRequestBody;
import com.ewsv3.ews.timecard.dto.TimecardObject;
import com.ewsv3.ews.timecard.service.TimecardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;

@RestController
@RequestMapping("/api/timecards")
public class TimecardController {

    private final JdbcClient jdbcClient;
    private final TimecardService timecardService;

    public TimecardController( JdbcClient jdbcClient, TimecardService timecardService) {
        this.jdbcClient = jdbcClient;
        this.timecardService = timecardService;
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

    @PostMapping("/timecard")
    @CrossOrigin
    public ResponseEntity<TimecardObject> getTimecards(@RequestHeader Map<String, String> headers,
                                                       @RequestBody PersonDateRequestBody requestBody) {

        System.out.println("getTimecards > headers" + headers);
        System.out.println("getTimecards > requestBody" + requestBody);
        Long personId = requestBody.personId();
        try {
            Long userId = getCurrentUserId();
            System.out.println("getTimecards > userId:" + userId);

            if (requestBody.startDate() == null || requestBody.endDate() == null) {
                return new ResponseEntity<>(null, new org.springframework.http.HttpHeaders(), HttpStatus.BAD_REQUEST);
            }

            if (personId == null || personId <= 0) {
                personId = getCurrentPersonId();
            }

            TimecardObject timecards = this.timecardService.getTimecards(
                    personId,
                    LocalDate.ofInstant(requestBody.startDate().toInstant(), ZoneId.systemDefault()),
                    LocalDate.ofInstant(requestBody.endDate().toInstant(), ZoneId.systemDefault()),
                    this.jdbcClient);

            return new ResponseEntity<>(timecards, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(new TimecardObject(), HttpStatus.valueOf(exception.getMessage()));
        }

    }

}
