package com.ewsv3.ews.schedulerules.controller;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.schedulerules.dto.ScheduleRuleDto;
import com.ewsv3.ews.schedulerules.dto.req.ScheduleRuleRequest;
import com.ewsv3.ews.schedulerules.dto.req.ScheduleRuleSearchRequest;
import com.ewsv3.ews.schedulerules.service.ScheduleRuleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/schedule-rules/")
public class ScheduleRuleController {

    private final ScheduleRuleService scheduleRuleService;
    private final JdbcClient jdbcClient;

    public ScheduleRuleController(ScheduleRuleService scheduleRuleService, JdbcClient jdbcClient) {
        this.scheduleRuleService = scheduleRuleService;
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

    @GetMapping("all")
    public ResponseEntity<List<ScheduleRuleDto>> getAllScheduleRules(@RequestHeader Map<String, String> headers) {
        try {
            // Get schedule rules only for profiles accessible by current user
            List<ScheduleRuleDto> scheduleRules = scheduleRuleService.getScheduleRulesForUser(getCurrentUserId(), jdbcClient);
            return new ResponseEntity<>(scheduleRules, HttpStatus.OK);
        } catch (Exception exception) {
            System.out.println("getAllScheduleRules exception: " + exception.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<ScheduleRuleDto> getScheduleRuleById(@RequestHeader Map<String, String> headers, @PathVariable Long id) {
        try {
            Optional<ScheduleRuleDto> scheduleRule = scheduleRuleService.getScheduleRuleById(id);
            if (scheduleRule.isPresent()) {
                return new ResponseEntity<>(scheduleRule.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            System.out.println("getScheduleRuleById exception: " + exception.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("profile/{profileId}")
    public ResponseEntity<List<ScheduleRuleDto>> getScheduleRulesByProfileId(@RequestHeader Map<String, String> headers, @PathVariable Long profileId) {
        try {
            List<ScheduleRuleDto> scheduleRules = scheduleRuleService.getScheduleRulesByProfileId(profileId);
            return new ResponseEntity<>(scheduleRules, HttpStatus.OK);
        } catch (Exception exception) {
            System.out.println("getScheduleRulesByProfileId exception: " + exception.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("search")
    public ResponseEntity<List<ScheduleRuleDto>> searchScheduleRules(@RequestHeader Map<String, String> headers, @RequestBody ScheduleRuleSearchRequest request) {
        try {
            List<ScheduleRuleDto> scheduleRules = scheduleRuleService.searchScheduleRules(request);
            return new ResponseEntity<>(scheduleRules, HttpStatus.OK);
        } catch (Exception exception) {
            System.out.println("searchScheduleRules exception: " + exception.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("active")
    public ResponseEntity<List<ScheduleRuleDto>> getActiveScheduleRules(@RequestHeader Map<String, String> headers, @RequestParam(required = false) LocalDate date) {
        try {
            LocalDate searchDate = date != null ? date : LocalDate.now();
            List<ScheduleRuleDto> scheduleRules = scheduleRuleService.getActiveScheduleRules(searchDate);
            return new ResponseEntity<>(scheduleRules, HttpStatus.OK);
        } catch (Exception exception) {
            System.out.println("getActiveScheduleRules exception: " + exception.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("create")
    public ResponseEntity<DMLResponseDto> createScheduleRule(@RequestHeader Map<String, String> headers, @RequestBody ScheduleRuleRequest request) {
        try {
            System.out.println("controller createScheduleRule request: " + request);
            DMLResponseDto response = scheduleRuleService.createScheduleRule(getCurrentUserId(), request);
            
            if ("S".equals(response.getStatusMessage())) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            System.out.println("createScheduleRule exception: " + exception.getMessage());
            return new ResponseEntity<>(new DMLResponseDto("E", exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("update/{id}")
    public ResponseEntity<DMLResponseDto> updateScheduleRule(@RequestHeader Map<String, String> headers, @PathVariable Long id, @RequestBody ScheduleRuleRequest request) {
        try {
            System.out.println("controller updateScheduleRule id: " + id + ", request: " + request);
            DMLResponseDto response = scheduleRuleService.updateScheduleRule(getCurrentUserId(), id, request);
            
            if ("S".equals(response.getStatusMessage())) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            System.out.println("updateScheduleRule exception: " + exception.getMessage());
            return new ResponseEntity<>(new DMLResponseDto("E", exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<DMLResponseDto> deleteScheduleRule(@RequestHeader Map<String, String> headers, @PathVariable Long id) {
        try {
            System.out.println("controller deleteScheduleRule id: " + id);
            DMLResponseDto response = scheduleRuleService.deleteScheduleRule(id);
            
            if ("S".equals(response.getStatusMessage())) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            System.out.println("deleteScheduleRule exception: " + exception.getMessage());
            return new ResponseEntity<>(new DMLResponseDto("E", exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}