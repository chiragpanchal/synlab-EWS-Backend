package com.ewsv3.ews.shiftbidding.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.shiftbidding.dto.ScheduleBidPattern;
import com.ewsv3.ews.shiftbidding.service.ScheduleBidPatternService;

@RestController
@RequestMapping("/api/schedule-bid-patterns")
public class ScheduleBidPatternController {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleBidPatternController.class);

    private final ScheduleBidPatternService scheduleBidPatternService;
    private final JdbcClient jdbcClient;

    public ScheduleBidPatternController(ScheduleBidPatternService scheduleBidPatternService, JdbcClient jdbcClient) {
        this.scheduleBidPatternService = scheduleBidPatternService;
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

    @GetMapping
    public ResponseEntity<List<ScheduleBidPattern>> getScheduleBidPatterns(
            @RequestHeader Map<String, String> header,
            @RequestParam Long scheduleBidCycleId) {
        try {
            logger.info("GET_SCHEDULE_BID_PATTERNS - Entry - Time: {}, ScheduleBidCycleId: {}", LocalDateTime.now(), scheduleBidCycleId);

            List<ScheduleBidPattern> patterns = this.scheduleBidPatternService
                    .getScheduleBidPatterns(scheduleBidCycleId, jdbcClient);
            logger.info("GET_SCHEDULE_BID_PATTERNS - Exit - Time: {}, Response count: {}", LocalDateTime.now(), patterns.size());
            return new ResponseEntity<>(patterns, HttpStatus.OK);

        } catch (Exception exception) {
            logger.error("GET_SCHEDULE_BID_PATTERNS - Exception - Time: {}, ScheduleBidCycleId: {}, Error: {}",
                    LocalDateTime.now(), scheduleBidCycleId, exception.getMessage(), exception);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleBidPattern> getScheduleBidPatternById(
            @RequestHeader Map<String, String> header,
            @PathVariable("id") Long scheduleBidPatternId) {
        try {
            logger.info("GET_SCHEDULE_BID_PATTERN_BY_ID - Entry - Time: {}, ScheduleBidPatternId: {}", LocalDateTime.now(), scheduleBidPatternId);

            ScheduleBidPattern pattern = this.scheduleBidPatternService
                    .getScheduleBidPatternById(scheduleBidPatternId, jdbcClient);

            if (pattern != null) {
                logger.info("GET_SCHEDULE_BID_PATTERN_BY_ID - Exit - Time: {}, Response: {}", LocalDateTime.now(), pattern);
                return new ResponseEntity<>(pattern, HttpStatus.OK);
            } else {
                logger.info("GET_SCHEDULE_BID_PATTERN_BY_ID - Exit - Time: {}, ScheduleBidPatternId: {}, Status: NOT_FOUND", LocalDateTime.now(), scheduleBidPatternId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (Exception exception) {
            logger.error("GET_SCHEDULE_BID_PATTERN_BY_ID - Exception - Time: {}, ScheduleBidPatternId: {}, Error: {}",
                    LocalDateTime.now(), scheduleBidPatternId, exception.getMessage(), exception);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<DMLResponseDto> saveScheduleBidPattern(
            @RequestHeader Map<String, String> header,
            @RequestBody ScheduleBidPattern scheduleBidPattern) {
        try {
            logger.info("SAVE_SCHEDULE_BID_PATTERN - Entry - Time: {}, Request: {}", LocalDateTime.now(), scheduleBidPattern);

            DMLResponseDto dmlResponseDto = this.scheduleBidPatternService.saveScheduleBidPattern(
                    getCurrentUserId(), scheduleBidPattern, jdbcClient);

            logger.info("SAVE_SCHEDULE_BID_PATTERN - Exit - Time: {}, Response: {}", LocalDateTime.now(), dmlResponseDto);
            return new ResponseEntity<>(dmlResponseDto, HttpStatus.OK);

        } catch (Exception exception) {
            logger.error("SAVE_SCHEDULE_BID_PATTERN - Exception - Time: {}, Request: {}, Error: {}",
                    LocalDateTime.now(), scheduleBidPattern, exception.getMessage(), exception);
            return new ResponseEntity<>(new DMLResponseDto("E", exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DMLResponseDto> deleteScheduleBidPattern(
            @RequestHeader Map<String, String> header,
            @PathVariable("id") Long scheduleBidPatternId) {
        try {
            logger.info("DELETE_SCHEDULE_BID_PATTERN - Entry - Time: {}, ScheduleBidPatternId: {}", LocalDateTime.now(), scheduleBidPatternId);

            DMLResponseDto dmlResponseDto = this.scheduleBidPatternService.deleteScheduleBidPattern(
                    scheduleBidPatternId, jdbcClient);

            logger.info("DELETE_SCHEDULE_BID_PATTERN - Exit - Time: {}, Response: {}", LocalDateTime.now(), dmlResponseDto);
            return new ResponseEntity<>(dmlResponseDto, HttpStatus.OK);

        } catch (Exception exception) {
            logger.error("DELETE_SCHEDULE_BID_PATTERN - Exception - Time: {}, ScheduleBidPatternId: {}, Error: {}",
                    LocalDateTime.now(), scheduleBidPatternId, exception.getMessage(), exception);

            return new ResponseEntity<>(new DMLResponseDto("E", exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
