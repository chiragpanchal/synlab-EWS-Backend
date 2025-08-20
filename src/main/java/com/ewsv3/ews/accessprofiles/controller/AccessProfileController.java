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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/access-profiles/")
public class AccessProfileController {

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

        try {
            List<AccessProfileResp> searchedProfiles = accessProfileService.getSearchedProfiles(getCurrentUserId(), req, this.jdbcClient);
            return new ResponseEntity<>(searchedProfiles, HttpStatus.OK);

        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("access-profile-one")
    public ResponseEntity<AccessProfileResponse> getAccessProfile(@RequestHeader Map<String, String> headers, @RequestBody AssessProfileId req) {

        try {
            AccessProfileResponse accessProfile = accessProfileService.getAccessProfile(getCurrentUserId(), req, this.jdbcClient);
            return new ResponseEntity<>(accessProfile, HttpStatus.OK);

        } catch (Exception exception) {
            System.out.println("getAccessProfile exception:" + exception.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("save-profile")
    public ResponseEntity<DMLResponseDto> saveAccessProfile(@RequestHeader Map<String, String> headers, @RequestBody AccessProfileResponse req) {

        try {
            System.out.println("controller saveAccessProfile req.getAccessProfiles():" + req.getAccessProfiles());
            System.out.println("controller saveAccessProfile req.getUserProfileAssocList().size():" + req.getUserProfileAssocList().size());
            System.out.println("controller saveAccessProfile req.getAccessProfileLinesList().size():" + req.getAccessProfileLinesList().size());
            accessProfileService.saveProfile(getCurrentUserId(), req, this.jdbcClient);
            return new ResponseEntity<>(new DMLResponseDto("S", "Profile Saved successfully!"), HttpStatus.OK);

        } catch (Exception exception) {
            System.out.println("saveAccessProfile exception:" + exception.getMessage());
            return new ResponseEntity<>(new DMLResponseDto("E", exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("delete-profile-line")
    public ResponseEntity<DMLResponseDto> deleteProfileLine(@RequestHeader Map<String, String> headers, @RequestBody AccessProfileLines req) {

        try {
            DMLResponseDto dmlResponseDto = accessProfileService.deleteProfileLine(getCurrentUserId(), req, this.jdbcClient);
            return new ResponseEntity<>(dmlResponseDto, HttpStatus.OK);

        } catch (Exception exception) {
            System.out.println("deleteProfileLine exception:" + exception.getMessage());
            return new ResponseEntity<>(new DMLResponseDto("E", exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("delete-user-assoc")
    public ResponseEntity<DMLResponseDto> deleteUSerAssoc(@RequestHeader Map<String, String> headers, @RequestBody UserProfileAssoc req) {

        try {
            DMLResponseDto dmlResponseDto = accessProfileService.deleteUserProfileAssoc(getCurrentUserId(), req, this.jdbcClient);
            return new ResponseEntity<>(dmlResponseDto, HttpStatus.OK);

        } catch (Exception exception) {
            System.out.println("deleteUSerAssoc exception:" + exception.getMessage());
            return new ResponseEntity<>(new DMLResponseDto("E", exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("delete-profile")
    public ResponseEntity<DMLResponseDto> deleteProfile(@RequestHeader Map<String, String> headers, @RequestBody AccessProfiles req) {

        try {
            DMLResponseDto dmlResponseDto = accessProfileService.deleteProfile(getCurrentUserId(), req, this.jdbcClient);
            return new ResponseEntity<>(dmlResponseDto, HttpStatus.OK);

        } catch (Exception exception) {
            System.out.println("deleteProfile exception:" + exception.getMessage());
            return new ResponseEntity<>(new DMLResponseDto("E", exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
