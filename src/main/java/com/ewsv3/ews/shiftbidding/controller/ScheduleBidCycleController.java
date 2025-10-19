package com.ewsv3.ews.shiftbidding.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
import com.ewsv3.ews.shiftbidding.dto.ScheduleBidCycle;
import com.ewsv3.ews.shiftbidding.service.ScheduleBidCycleService;

@RestController
@RequestMapping("/api/schedule-bid-cycles")
public class ScheduleBidCycleController {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleBidCycleController.class);

    private final ScheduleBidCycleService scheduleBidCycleService;
    private final JdbcClient jdbcClient;

    public ScheduleBidCycleController(ScheduleBidCycleService scheduleBidCycleService, JdbcClient jdbcClient) {
        this.scheduleBidCycleService = scheduleBidCycleService;
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
    public ResponseEntity<List<ScheduleBidCycle>> getScheduleBidCycles(
            @RequestHeader Map<String, String> header,
            @RequestParam Long profileId) {
        try {
            logger.debug("GET /api/schedule-bid-cycles - profileId: {}", profileId);

            List<ScheduleBidCycle> cycles = this.scheduleBidCycleService.getScheduleBidCycles(profileId, jdbcClient);
            return new ResponseEntity<>(cycles, HttpStatus.OK);

        } catch (Exception exception) {
            logger.error("Error fetching schedule bid cycles: {}", exception.getMessage(), exception);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleBidCycle> getScheduleBidCycleById(
            @RequestHeader Map<String, String> header,
            @PathVariable("id") Long scheduleBidCycleId) {
        try {
            logger.debug("GET /api/schedule-bid-cycles/{} ", scheduleBidCycleId);

            ScheduleBidCycle cycle = this.scheduleBidCycleService.getScheduleBidCycleById(scheduleBidCycleId, jdbcClient);

            if (cycle != null) {
                return new ResponseEntity<>(cycle, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (Exception exception) {
            logger.error("Error fetching schedule bid cycle by id: {}", exception.getMessage(), exception);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<DMLResponseDto> saveScheduleBidCycle(
            @RequestHeader Map<String, String> header,
            @RequestBody ScheduleBidCycle scheduleBidCycle) {
        try {
            logger.debug("POST /api/schedule-bid-cycles - scheduleBidCycle: {}", scheduleBidCycle);

            DMLResponseDto dmlResponseDto = this.scheduleBidCycleService.saveScheduleBidCycle(
                    getCurrentUserId(), scheduleBidCycle, jdbcClient);

            return new ResponseEntity<>(dmlResponseDto, HttpStatus.OK);

        } catch (Exception exception) {
            logger.error("Error saving schedule bid cycle: {}", exception.getMessage(), exception);
            return new ResponseEntity<>(new DMLResponseDto("E", exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DMLResponseDto> deleteScheduleBidCycle(
            @RequestHeader Map<String, String> header,
            @PathVariable("id") Long scheduleBidCycleId) {
        try {
            logger.debug("DELETE /api/schedule-bid-cycles/{}", scheduleBidCycleId);

            DMLResponseDto dmlResponseDto = this.scheduleBidCycleService.deleteScheduleBidCycle(
                    scheduleBidCycleId, jdbcClient);

            return new ResponseEntity<>(dmlResponseDto, HttpStatus.OK);

        } catch (Exception exception) {
            logger.error("Error deleting schedule bid cycle: {}", exception.getMessage(), exception);
            return new ResponseEntity<>(new DMLResponseDto("E", exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
