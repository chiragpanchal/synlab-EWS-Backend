package com.ewsv3.ews.accessprofiles.controller;

import com.ewsv3.ews.accessprofiles.dto.AccessProfileLines;
import com.ewsv3.ews.accessprofiles.dto.AccessProfileResponse;
import com.ewsv3.ews.accessprofiles.dto.AccessProfiles;
import com.ewsv3.ews.accessprofiles.dto.UserProfileAssoc;
import com.ewsv3.ews.accessprofiles.dto.req.AccessProfileReq;
import com.ewsv3.ews.accessprofiles.dto.req.AssessProfileId;
import com.ewsv3.ews.accessprofiles.dto.resp.AccessProfileResp;
import com.ewsv3.ews.accessprofiles.service.AccessProfileService;
import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.commons.dto.DMLResponseDto;
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
@RequestMapping("/api/access-profiles/")
public class AccessProfileController {

    private static final Logger logger = LoggerFactory.getLogger(AccessProfileController.class);

    private final AccessProfileService accessProfileService;
    private final JdbcClient jdbcClient;

    public AccessProfileController(AccessProfileService accessProfileService, JdbcClient jdbcClient) {
        this.accessProfileService = accessProfileService;
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

    @PostMapping("searched-profiles")
    public ResponseEntity<List<AccessProfileResp>> getSearchedProfiles(@RequestHeader Map<String, String> headers, @RequestBody AccessProfileReq req) {
        logger.info("GET_SEARCHED_PROFILES - Entry - Time: {}, Request: {}", LocalDateTime.now(), req);

        try {
            List<AccessProfileResp> searchedProfiles = accessProfileService.getSearchedProfiles(getCurrentUserId(), req, this.jdbcClient);
            logger.info("GET_SEARCHED_PROFILES - Exit - Time: {}, Request: {}, Response Count: {}", LocalDateTime.now(), req, searchedProfiles.size());
            return new ResponseEntity<>(searchedProfiles, HttpStatus.OK);

        } catch (Exception exception) {
            logger.error("GET_SEARCHED_PROFILES - Exception - Time: {}, Request: {}, Error: {}", LocalDateTime.now(), req, exception.getMessage(), exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("access-profile-one")
    public ResponseEntity<AccessProfileResponse> getAccessProfile(@RequestHeader Map<String, String> headers, @RequestBody AssessProfileId req) {
        logger.info("GET_ACCESS_PROFILE - Entry - Time: {}, Request: {}", LocalDateTime.now(), req);

        try {
            AccessProfileResponse accessProfile = accessProfileService.getAccessProfile(getCurrentUserId(), req, this.jdbcClient);
            logger.info("GET_ACCESS_PROFILE - Exit - Time: {}, Request: {}, Response: {}", LocalDateTime.now(), req, accessProfile);
            return new ResponseEntity<>(accessProfile, HttpStatus.OK);

        } catch (Exception exception) {
            // System.out.println("getAccessProfile exception:" + exception.getMessage());
            logger.error("GET_ACCESS_PROFILE - Exception - Time: {}, Request: {}, Error: {}", LocalDateTime.now(), req, exception.getMessage(), exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("save-profile")
    public ResponseEntity<DMLResponseDto> saveAccessProfile(@RequestHeader Map<String, String> headers, @RequestBody AccessProfileResponse req) {
        logger.info("SAVE_ACCESS_PROFILE - Entry - Time: {}, AccessProfiles: {}, UserProfileAssocList Count: {}, AccessProfileLinesList Count: {}",
            LocalDateTime.now(), req.getAccessProfiles(),
            req.getUserProfileAssocList() != null ? req.getUserProfileAssocList().size() : 0,
            req.getAccessProfileLinesList() != null ? req.getAccessProfileLinesList().size() : 0);

        try {
            // System.out.println("controller saveAccessProfile req.getAccessProfiles():" + req.getAccessProfiles());
            // System.out.println("controller saveAccessProfile req.getUserProfileAssocList().size():" + req.getUserProfileAssocList().size());
            // System.out.println("controller saveAccessProfile req.getAccessProfileLinesList().size():" + req.getAccessProfileLinesList().size());
            accessProfileService.saveProfile(getCurrentUserId(), req, this.jdbcClient);
            DMLResponseDto response = new DMLResponseDto("S", "Profile Saved successfully!");
            logger.info("SAVE_ACCESS_PROFILE - Exit - Time: {}, Response: {}", LocalDateTime.now(), response);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception exception) {
            // System.out.println("saveAccessProfile exception:" + exception.getMessage());
            logger.error("SAVE_ACCESS_PROFILE - Exception - Time: {}, AccessProfiles: {}, Error: {}",
                LocalDateTime.now(), req.getAccessProfiles(), exception.getMessage(), exception);
            return new ResponseEntity<>(new DMLResponseDto("E", exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("delete-profile-line")
    public ResponseEntity<DMLResponseDto> deleteProfileLine(@RequestHeader Map<String, String> headers, @RequestBody AccessProfileLines req) {
        logger.info("DELETE_PROFILE_LINE - Entry - Time: {}, Request: {}", LocalDateTime.now(), req);

        try {
            DMLResponseDto dmlResponseDto = accessProfileService.deleteProfileLine(getCurrentUserId(), req, this.jdbcClient);
            logger.info("DELETE_PROFILE_LINE - Exit - Time: {}, Request: {}, Response: {}", LocalDateTime.now(), req, dmlResponseDto);
            return new ResponseEntity<>(dmlResponseDto, HttpStatus.OK);

        } catch (Exception exception) {
            // System.out.println("deleteProfileLine exception:" + exception.getMessage());
            logger.error("DELETE_PROFILE_LINE - Exception - Time: {}, Request: {}, Error: {}", LocalDateTime.now(), req, exception.getMessage(), exception);
            return new ResponseEntity<>(new DMLResponseDto("E", exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("delete-user-assoc")
    public ResponseEntity<DMLResponseDto> deleteUSerAssoc(@RequestHeader Map<String, String> headers, @RequestBody UserProfileAssoc req) {
        logger.info("DELETE_USER_ASSOC - Entry - Time: {}, Request: {}", LocalDateTime.now(), req);

        try {
            DMLResponseDto dmlResponseDto = accessProfileService.deleteUserProfileAssoc(getCurrentUserId(), req, this.jdbcClient);
            logger.info("DELETE_USER_ASSOC - Exit - Time: {}, Request: {}, Response: {}", LocalDateTime.now(), req, dmlResponseDto);
            return new ResponseEntity<>(dmlResponseDto, HttpStatus.OK);

        } catch (Exception exception) {
            // System.out.println("deleteUSerAssoc exception:" + exception.getMessage());
            logger.error("DELETE_USER_ASSOC - Exception - Time: {}, Request: {}, Error: {}", LocalDateTime.now(), req, exception.getMessage(), exception);
            return new ResponseEntity<>(new DMLResponseDto("E", exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("delete-profile")
    public ResponseEntity<DMLResponseDto> deleteProfile(@RequestHeader Map<String, String> headers, @RequestBody AccessProfiles req) {
        logger.info("DELETE_PROFILE - Entry - Time: {}, Request: {}", LocalDateTime.now(), req);

        try {
            DMLResponseDto dmlResponseDto = accessProfileService.deleteProfile(getCurrentUserId(), req, this.jdbcClient);
            logger.info("DELETE_PROFILE - Exit - Time: {}, Request: {}, Response: {}", LocalDateTime.now(), req, dmlResponseDto);
            return new ResponseEntity<>(dmlResponseDto, HttpStatus.OK);

        } catch (Exception exception) {
            // System.out.println("deleteProfile exception:" + exception.getMessage());
            logger.error("DELETE_PROFILE - Exception - Time: {}, Request: {}, Error: {}", LocalDateTime.now(), req, exception.getMessage(), exception);
            return new ResponseEntity<>(new DMLResponseDto("E", exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
