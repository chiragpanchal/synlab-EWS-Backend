package com.ewsv3.ews.accessprofiles.controller;

import com.ewsv3.ews.accessprofiles.dto.AccessProfileResponse;
import com.ewsv3.ews.accessprofiles.dto.req.AccessProfileReq;
import com.ewsv3.ews.accessprofiles.dto.req.AssessProfileId;
import com.ewsv3.ews.accessprofiles.dto.resp.AccessProfileResp;
import com.ewsv3.ews.accessprofiles.service.AccessProfileService;
import com.ewsv3.ews.auth.dto.UserPrincipal;
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

}
