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
import com.ewsv3.ews.shiftbidding.dto.BiddingGroup;
import com.ewsv3.ews.shiftbidding.service.BiddingGroupService;

@RestController
@RequestMapping("/api/bidding-groups")
public class BiddingGroupController {

    private static final Logger logger = LoggerFactory.getLogger(BiddingGroupController.class);

    private final BiddingGroupService biddingGroupService;
    private final JdbcClient jdbcClient;

    public BiddingGroupController(BiddingGroupService biddingGroupService, JdbcClient jdbcClient) {
        this.biddingGroupService = biddingGroupService;
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
    public ResponseEntity<List<BiddingGroup>> getBiddingGroups(
            @RequestHeader Map<String, String> header,
            @RequestParam Long profileId) {
        try {
            logger.debug("GET /api/bidding-groups - profileId: {}", profileId);

            List<BiddingGroup> groups = this.biddingGroupService.getBiddingGroups(profileId, jdbcClient);
            return new ResponseEntity<>(groups, HttpStatus.OK);

        } catch (Exception exception) {
            logger.error("Error fetching bidding groups: {}", exception.getMessage(), exception);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BiddingGroup> getBiddingGroupById(
            @RequestHeader Map<String, String> header,
            @PathVariable("id") Long biddingGroupId) {
        try {
            logger.debug("GET /api/bidding-groups/{}", biddingGroupId);

            BiddingGroup group = this.biddingGroupService.getBiddingGroupById(biddingGroupId, jdbcClient);

            if (group != null) {
                return new ResponseEntity<>(group, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (Exception exception) {
            logger.error("Error fetching bidding group by id: {}", exception.getMessage(), exception);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<DMLResponseDto> saveBiddingGroup(
            @RequestHeader Map<String, String> header,
            @RequestBody BiddingGroup biddingGroup) {
        try {
            logger.debug("POST /api/bidding-groups - biddingGroup: {}", biddingGroup);

            DMLResponseDto dmlResponseDto = this.biddingGroupService.saveBiddingGroup(
                    getCurrentUserId(), biddingGroup, jdbcClient);

            return new ResponseEntity<>(dmlResponseDto, HttpStatus.OK);

        } catch (Exception exception) {
            logger.error("Error saving bidding group: {}", exception.getMessage(), exception);
            return new ResponseEntity<>(new DMLResponseDto("E", exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DMLResponseDto> deleteBiddingGroup(
            @RequestHeader Map<String, String> header,
            @PathVariable("id") Long biddingGroupId) {
        try {
            logger.debug("DELETE /api/bidding-groups/{}", biddingGroupId);

            DMLResponseDto dmlResponseDto = this.biddingGroupService.deleteBiddingGroup(
                    biddingGroupId, jdbcClient);

            return new ResponseEntity<>(dmlResponseDto, HttpStatus.OK);

        } catch (Exception exception) {
            logger.error("Error deleting bidding group: {}", exception.getMessage(), exception);
            return new ResponseEntity<>(new DMLResponseDto("E", exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
