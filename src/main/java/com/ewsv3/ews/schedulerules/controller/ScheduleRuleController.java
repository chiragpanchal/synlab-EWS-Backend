package com.ewsv3.ews.schedulerules.controller;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.schedulerules.dto.ScheduleRuleDto;
import com.ewsv3.ews.schedulerules.dto.req.ScheduleRuleRequest;
import com.ewsv3.ews.schedulerules.dto.req.ScheduleRuleSearchRequest;
import com.ewsv3.ews.schedulerules.service.ScheduleRuleService;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/schedule-rules/")
public class ScheduleRuleController {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleRuleController.class);

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
        logger.info("GET_ALL_SCHEDULE_RULES - Entry - Time: {}, UserId: {}", LocalDateTime.now(), getCurrentUserId());
        try {
            // Get schedule rules only for profiles accessible by current user
            List<ScheduleRuleDto> scheduleRules = scheduleRuleService.getScheduleRulesForUser(getCurrentUserId(), jdbcClient);
            logger.info("GET_ALL_SCHEDULE_RULES - Exit - Time: {}, Response count: {}", LocalDateTime.now(), scheduleRules.size());
            return new ResponseEntity<>(scheduleRules, HttpStatus.OK);
        } catch (Exception exception) {
            //System.out.println("getAllScheduleRules exception: " + exception.getMessage());
            logger.error("GET_ALL_SCHEDULE_RULES - Exception - Time: {}, UserId: {}, Error: {}",
                    LocalDateTime.now(), getCurrentUserId(), exception.getMessage(), exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<ScheduleRuleDto> getScheduleRuleById(@RequestHeader Map<String, String> headers, @PathVariable Long id) {
        logger.info("GET_SCHEDULE_RULE_BY_ID - Entry - Time: {}, Id: {}", LocalDateTime.now(), id);
        try {
            Optional<ScheduleRuleDto> scheduleRule = scheduleRuleService.getScheduleRuleById(id);
            if (scheduleRule.isPresent()) {
                logger.info("GET_SCHEDULE_RULE_BY_ID - Exit - Time: {}, Response: {}", LocalDateTime.now(), scheduleRule.get());
                return new ResponseEntity<>(scheduleRule.get(), HttpStatus.OK);
            } else {
                logger.info("GET_SCHEDULE_RULE_BY_ID - Exit - Time: {}, Id: {}, Status: NOT_FOUND", LocalDateTime.now(), id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            //System.out.println("getScheduleRuleById exception: " + exception.getMessage());
            logger.error("GET_SCHEDULE_RULE_BY_ID - Exception - Time: {}, Id: {}, Error: {}",
                    LocalDateTime.now(), id, exception.getMessage(), exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("profile/{profileId}")
    public ResponseEntity<List<ScheduleRuleDto>> getScheduleRulesByProfileId(@RequestHeader Map<String, String> headers, @PathVariable Long profileId) {
        logger.info("GET_SCHEDULE_RULES_BY_PROFILE - Entry - Time: {}, ProfileId: {}", LocalDateTime.now(), profileId);
        try {
            List<ScheduleRuleDto> scheduleRules = scheduleRuleService.getScheduleRulesByProfileId(profileId);
            logger.info("GET_SCHEDULE_RULES_BY_PROFILE - Exit - Time: {}, Response count: {}", LocalDateTime.now(), scheduleRules.size());
            return new ResponseEntity<>(scheduleRules, HttpStatus.OK);
        } catch (Exception exception) {
            //System.out.println("getScheduleRulesByProfileId exception: " + exception.getMessage());
            logger.error("GET_SCHEDULE_RULES_BY_PROFILE - Exception - Time: {}, ProfileId: {}, Error: {}",
                    LocalDateTime.now(), profileId, exception.getMessage(), exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("search")
    public ResponseEntity<List<ScheduleRuleDto>> searchScheduleRules(@RequestHeader Map<String, String> headers, @RequestBody ScheduleRuleSearchRequest request) {
        logger.info("SEARCH_SCHEDULE_RULES - Entry - Time: {}, Request: {}", LocalDateTime.now(), request);
        try {
            List<ScheduleRuleDto> scheduleRules = scheduleRuleService.searchScheduleRules(request);
            logger.info("SEARCH_SCHEDULE_RULES - Exit - Time: {}, Response count: {}", LocalDateTime.now(), scheduleRules.size());
            return new ResponseEntity<>(scheduleRules, HttpStatus.OK);
        } catch (Exception exception) {
            //System.out.println("searchScheduleRules exception: " + exception.getMessage());
            logger.error("SEARCH_SCHEDULE_RULES - Exception - Time: {}, Request: {}, Error: {}",
                    LocalDateTime.now(), request, exception.getMessage(), exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("active")
    public ResponseEntity<List<ScheduleRuleDto>> getActiveScheduleRules(@RequestHeader Map<String, String> headers, @RequestParam(required = false) LocalDate date) {
        LocalDate searchDate = date != null ? date : LocalDate.now();
        logger.info("GET_ACTIVE_SCHEDULE_RULES - Entry - Time: {}, SearchDate: {}", LocalDateTime.now(), searchDate);
        try {
            List<ScheduleRuleDto> scheduleRules = scheduleRuleService.getActiveScheduleRules(searchDate);
            logger.info("GET_ACTIVE_SCHEDULE_RULES - Exit - Time: {}, Response count: {}", LocalDateTime.now(), scheduleRules.size());
            return new ResponseEntity<>(scheduleRules, HttpStatus.OK);
        } catch (Exception exception) {
            //System.out.println("getActiveScheduleRules exception: " + exception.getMessage());
            logger.error("GET_ACTIVE_SCHEDULE_RULES - Exception - Time: {}, SearchDate: {}, Error: {}",
                    LocalDateTime.now(), searchDate, exception.getMessage(), exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("create")
    public ResponseEntity<DMLResponseDto> createScheduleRule(@RequestHeader Map<String, String> headers, @RequestBody ScheduleRuleRequest request) {
        logger.info("CREATE_SCHEDULE_RULE - Entry - Time: {}, Request: {}", LocalDateTime.now(), request);
        try {
            //System.out.println("controller createScheduleRule request: " + request);
            DMLResponseDto response = scheduleRuleService.createScheduleRule(getCurrentUserId(), request);
            logger.info("CREATE_SCHEDULE_RULE - Exit - Time: {}, Response: {}", LocalDateTime.now(), response);

            if ("S".equals(response.getStatusMessage())) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            //System.out.println("createScheduleRule exception: " + exception.getMessage());
            logger.error("CREATE_SCHEDULE_RULE - Exception - Time: {}, Request: {}, Error: {}",
                    LocalDateTime.now(), request, exception.getMessage(), exception);
            return new ResponseEntity<>(new DMLResponseDto("E", exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("update/{id}")
    public ResponseEntity<DMLResponseDto> updateScheduleRule(@RequestHeader Map<String, String> headers, @PathVariable Long id, @RequestBody ScheduleRuleRequest request) {
        logger.info("UPDATE_SCHEDULE_RULE - Entry - Time: {}, Id: {}, Request: {}", LocalDateTime.now(), id, request);
        try {
            //System.out.println("controller updateScheduleRule id: " + id + ", request: " + request);
            DMLResponseDto response = scheduleRuleService.updateScheduleRule(getCurrentUserId(), id, request);
            logger.info("UPDATE_SCHEDULE_RULE - Exit - Time: {}, Response: {}", LocalDateTime.now(), response);

            if ("S".equals(response.getStatusMessage())) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            //System.out.println("updateScheduleRule exception: " + exception.getMessage());
            logger.error("UPDATE_SCHEDULE_RULE - Exception - Time: {}, Id: {}, Request: {}, Error: {}",
                    LocalDateTime.now(), id, request, exception.getMessage(), exception);
            return new ResponseEntity<>(new DMLResponseDto("E", exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<DMLResponseDto> deleteScheduleRule(@RequestHeader Map<String, String> headers, @PathVariable Long id) {
        logger.info("DELETE_SCHEDULE_RULE - Entry - Time: {}, Id: {}", LocalDateTime.now(), id);
        try {
            //System.out.println("controller deleteScheduleRule id: " + id);
            DMLResponseDto response = scheduleRuleService.deleteScheduleRule(id);
            logger.info("DELETE_SCHEDULE_RULE - Exit - Time: {}, Response: {}", LocalDateTime.now(), response);

            if ("S".equals(response.getStatusMessage())) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            //System.out.println("deleteScheduleRule exception: " + exception.getMessage());
            logger.error("DELETE_SCHEDULE_RULE - Exception - Time: {}, Id: {}, Error: {}",
                    LocalDateTime.now(), id, exception.getMessage(), exception);
            return new ResponseEntity<>(new DMLResponseDto("E", exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
