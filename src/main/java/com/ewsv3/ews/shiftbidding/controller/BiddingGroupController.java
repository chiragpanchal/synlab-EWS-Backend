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
        logger.info("GET_BIDDING_GROUPS - Entry - Time: {}, ProfileId: {}", LocalDateTime.now(), profileId);
        try {
            //logger.debug("GET /api/bidding-groups - profileId: {}", profileId);

            List<BiddingGroup> groups = this.biddingGroupService.getBiddingGroups(profileId, jdbcClient);
            logger.info("GET_BIDDING_GROUPS - Exit - Time: {}, Response count: {}", LocalDateTime.now(), groups.size());
            return new ResponseEntity<>(groups, HttpStatus.OK);

        } catch (Exception exception) {
            //logger.error("Error fetching bidding groups: {}", exception.getMessage(), exception);
            logger.error("GET_BIDDING_GROUPS - Exception - Time: {}, ProfileId: {}, Error: {}",
                    LocalDateTime.now(), profileId, exception.getMessage(), exception);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BiddingGroup> getBiddingGroupById(
            @RequestHeader Map<String, String> header,
            @PathVariable("id") Long biddingGroupId) {
        logger.info("GET_BIDDING_GROUP_BY_ID - Entry - Time: {}, BiddingGroupId: {}", LocalDateTime.now(), biddingGroupId);
        try {
            //logger.debug("GET /api/bidding-groups/{}", biddingGroupId);

            BiddingGroup group = this.biddingGroupService.getBiddingGroupById(biddingGroupId, jdbcClient);

            if (group != null) {
                logger.info("GET_BIDDING_GROUP_BY_ID - Exit - Time: {}, Response: {}", LocalDateTime.now(), group);
                return new ResponseEntity<>(group, HttpStatus.OK);
            } else {
                logger.info("GET_BIDDING_GROUP_BY_ID - Exit - Time: {}, BiddingGroupId: {}, Status: NOT_FOUND", LocalDateTime.now(), biddingGroupId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (Exception exception) {
            //logger.error("Error fetching bidding group by id: {}", exception.getMessage(), exception);
            logger.error("GET_BIDDING_GROUP_BY_ID - Exception - Time: {}, BiddingGroupId: {}, Error: {}",
                    LocalDateTime.now(), biddingGroupId, exception.getMessage(), exception);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<DMLResponseDto> saveBiddingGroup(
            @RequestHeader Map<String, String> header,
            @RequestBody BiddingGroup biddingGroup) {
        logger.info("SAVE_BIDDING_GROUP - Entry - Time: {}, Request: {}", LocalDateTime.now(), biddingGroup);
        try {
            //logger.debug("POST /api/bidding-groups - biddingGroup: {}", biddingGroup);

            DMLResponseDto dmlResponseDto = this.biddingGroupService.saveBiddingGroup(
                    getCurrentUserId(), biddingGroup, jdbcClient);

            logger.info("SAVE_BIDDING_GROUP - Exit - Time: {}, Response: {}", LocalDateTime.now(), dmlResponseDto);
            return new ResponseEntity<>(dmlResponseDto, HttpStatus.OK);

        } catch (Exception exception) {
            //logger.error("Error saving bidding group: {}", exception.getMessage(), exception);
            logger.error("SAVE_BIDDING_GROUP - Exception - Time: {}, Request: {}, Error: {}",
                    LocalDateTime.now(), biddingGroup, exception.getMessage(), exception);
            return new ResponseEntity<>(new DMLResponseDto("E", exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DMLResponseDto> deleteBiddingGroup(
            @RequestHeader Map<String, String> header,
            @PathVariable("id") Long biddingGroupId) {
        logger.info("DELETE_BIDDING_GROUP - Entry - Time: {}, BiddingGroupId: {}", LocalDateTime.now(), biddingGroupId);
        try {
            //logger.debug("DELETE /api/bidding-groups/{}", biddingGroupId);

            DMLResponseDto dmlResponseDto = this.biddingGroupService.deleteBiddingGroup(
                    biddingGroupId, jdbcClient);

            logger.info("DELETE_BIDDING_GROUP - Exit - Time: {}, Response: {}", LocalDateTime.now(), dmlResponseDto);
            return new ResponseEntity<>(dmlResponseDto, HttpStatus.OK);

        } catch (Exception exception) {
            //logger.error("Error deleting bidding group: {}", exception.getMessage(), exception);
            logger.error("DELETE_BIDDING_GROUP - Exception - Time: {}, BiddingGroupId: {}, Error: {}",
                    LocalDateTime.now(), biddingGroupId, exception.getMessage(), exception);
            return new ResponseEntity<>(new DMLResponseDto("E", exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
