package com.ewsv3.ews.shiftGroup.controller;

import com.ewsv3.ews.accessprofiles.controller.AccessProfileController;
import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.openShifts.dto.OpenShiftDetailSkills;
import com.ewsv3.ews.openShifts.dto.OpenShiftLines;
import com.ewsv3.ews.openShifts.dto.OpenShiftsHeader;
import com.ewsv3.ews.shiftGroup.dto.ShiftGoupMasters;
import com.ewsv3.ews.shiftGroup.dto.ShiftGroup;
import com.ewsv3.ews.shiftGroup.dto.ShiftGroupShifts;
import com.ewsv3.ews.shiftGroup.service.ShiftGroupService;
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
@RequestMapping("/api/shift-group")
public class ShiftGroupController {

    private static final Logger logger = LoggerFactory.getLogger(AccessProfileController.class);

    private final JdbcClient jdbcClient;
    private final ShiftGroupService shiftGroupService;

    public ShiftGroupController(JdbcClient jdbcClient, ShiftGroupService shiftGroupService) {
        this.jdbcClient = jdbcClient;
        this.shiftGroupService = shiftGroupService;
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getUserId();
        }
        throw new RuntimeException("User not authenticated or invalid token");
    }

    @PostMapping("shift-group-save")
    public ResponseEntity<DMLResponseDto> saveShiftGroup(@RequestHeader Map<String, String> headers, @RequestBody ShiftGroup reqBody) {
        logger.info("shift-group-save - Entry - Time: {}, reqBody.getOpenShiftLines(): {}",
                LocalDateTime.now(), reqBody.getShiftGroupShifts());
        logger.info("shift-group-save - Entry - Time: {}, reqBody.getOpenShiftLines().size(): {}",
                LocalDateTime.now(), reqBody.getShiftGroupShifts().size());

        try {

            DMLResponseDto dmlResponseDto = shiftGroupService.saveShiftGroup(getCurrentUserId(), reqBody, jdbcClient);
            logger.info("shift-group-save create - Exit - Time: {}, reqBody: {}, Response dto: {}", LocalDateTime.now(),
                    reqBody, dmlResponseDto);
            return new ResponseEntity<>(dmlResponseDto, HttpStatus.OK);

        } catch (Exception exception) {
            logger.error("shift-group-save create - Exception - Time: {}, reqBody: {}, Error: {}", LocalDateTime.now(),
                    reqBody, exception.getMessage());
            return new ResponseEntity<>(new DMLResponseDto("E", exception.getMessage()), HttpStatus.BAD_REQUEST);
        }


    }

    @GetMapping("get-shift-group-list")
    public ResponseEntity<List<ShiftGroup>> getShiftGroupList(@RequestHeader Map<String, String> headers) {
        logger.info("get-shift-group-list - Entry - Time: {}, ",
                LocalDateTime.now());

        try {
            List<ShiftGroup> shiftGroups = shiftGroupService.getShiftGroups(getCurrentUserId(), jdbcClient);
            logger.info("get-shift-group-list create - Exit - Time: {} Response count: {}", LocalDateTime.now(),
                    shiftGroups.size());
            return new ResponseEntity<>(shiftGroups, HttpStatus.OK);

        } catch (Exception exception) {
            logger.error("get-shift-group-list create - Exception - Time: {},  Error: {}", LocalDateTime.now(),
                    exception.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @PostMapping("delete-shift-group-shift")
    public ResponseEntity<DMLResponseDto> deleteShiftGroupShift(@RequestHeader Map<String, String> headers, @RequestBody ShiftGroupShifts shiftGroupShift) {
        logger.info("delete-shift-group-shift - Entry - Time: {}, reqBody {} ",
                LocalDateTime.now(), shiftGroupShift);

        try {
            DMLResponseDto responseDto = shiftGroupService.deleteShiftGroupShift(getCurrentUserId(), shiftGroupShift, jdbcClient);
            logger.info("delete-shift-group-shift - Exit - Time: {} , reqBody {}, Response : {}", LocalDateTime.now(),
                    shiftGroupShift, responseDto);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);

        } catch (Exception exception) {
            logger.error("delete-shift-group-shift- Exception - Time: {}, reqBody {}, Error: {}", LocalDateTime.now(),
                    shiftGroupShift, exception.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @PostMapping("get-shift-group-shifts")
    public ResponseEntity<List<ShiftGroupShifts>> getShiftGroupShifts(@RequestHeader Map<String, String> headers, @RequestBody ShiftGroup reqBody) {
        logger.info("get-shift-group-shifts - Entry - Time: {}, reqBody {} ",
                LocalDateTime.now(), reqBody);

        try {
            List<ShiftGroupShifts> shiftGroupShifts = shiftGroupService.getShiftGroupShifts(getCurrentUserId(), reqBody, jdbcClient);
            logger.info("get-shift-group-shifts - Exit - Time: {} , reqBody {}, Response count : {}", LocalDateTime.now(),
                    reqBody, shiftGroupShifts.size());
            return new ResponseEntity<>(shiftGroupShifts, HttpStatus.OK);

        } catch (Exception exception) {
            logger.error("get-shift-group-shifts- Exception - Time: {}, reqBody {}, Error: {}", LocalDateTime.now(),
                    reqBody, exception.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @PostMapping("get-shift-group-byid")
    public ResponseEntity<ShiftGroup> getShiftGroupById(@RequestHeader Map<String, String> headers, @RequestBody ShiftGroup reqBody) {
        logger.info("get-shift-group-byid - Entry - Time: {}, reqBody {} ",
                LocalDateTime.now(), reqBody);

        try {
            ShiftGroup group = shiftGroupService.getShiftGroupFromId(getCurrentUserId(), reqBody, jdbcClient);
            logger.info("get-shift-group-byid - Exit - Time: {} , reqBody {}, Response : {}", LocalDateTime.now(),
                    reqBody, group);
            return new ResponseEntity<>(group, HttpStatus.OK);

        } catch (Exception exception) {
            logger.error("get-shift-group-byid- Exception - Time: {}, reqBody {}, Error: {}", LocalDateTime.now(),
                    reqBody, exception.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @GetMapping("shift-group-masters")
    public ResponseEntity<ShiftGoupMasters> getShiftGroupMasters(@RequestHeader Map<String, String> headers) {
        logger.info("shift-group-masters - Entry - Time: {} ",
                LocalDateTime.now());
        try {
            ShiftGoupMasters goupMasters = shiftGroupService.getMasters(jdbcClient);
            logger.info("shift-group-masters - Exit - Time: {} , Response : {}", LocalDateTime.now(),
                    goupMasters);
            return new ResponseEntity<>(goupMasters, HttpStatus.OK);

        } catch (Exception exception) {
            logger.error("shift-group-masters Exception - Time: {}, Error: {}", LocalDateTime.now(),
                    exception.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
