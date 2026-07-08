package com.ewsv3.ews.userpref.controller;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.userpref.dto.UserPrefDto;
import com.ewsv3.ews.userpref.dto.UserPrefRequestDto;
import com.ewsv3.ews.userpref.service.UserPrefService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user-preferences/")
public class UserPrefController {
    private static final Logger logger = LoggerFactory.getLogger(UserPrefController.class);

    private final UserPrefService userPrefService;

    public UserPrefController(UserPrefService userPrefService) {
        this.userPrefService = userPrefService;
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getUserId();
        }
        throw new RuntimeException("User not authenticated or invalid token");
    }

    @GetMapping("")
    public ResponseEntity<UserPrefDto> getUserPreferenceForCurrentUser(@RequestHeader Map<String, String> headers) {
        logger.info("GET_USER_PREFERENCE_FOR_CURRENT_USER - Entry - Time: {}, UserId: {}", LocalDateTime.now(), getCurrentUserId());
        try {
            Optional<UserPrefDto> userPreference = userPrefService.getUserPreferenceForCurrentUser();
            if (userPreference.isPresent()) {
                logger.info("GET_USER_PREFERENCE_FOR_CURRENT_USER - Exit - Time: {}, Response: {}", LocalDateTime.now(), userPreference.get());
                return new ResponseEntity<>(userPreference.get(), HttpStatus.OK);
            } else {
                logger.info("GET_USER_PREFERENCE_FOR_CURRENT_USER - Exit - Time: {}, UserId: {}, Status: NOT_FOUND", LocalDateTime.now(), getCurrentUserId());
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            logger.error("GET_USER_PREFERENCE_FOR_CURRENT_USER - Exception - Time: {}, UserId: {}, Error: {}",
                    LocalDateTime.now(), getCurrentUserId(), exception.getMessage(), exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<UserPrefDto> getUserPreferenceById(@RequestHeader Map<String, String> headers, @PathVariable Long id) {
        logger.info("GET_USER_PREFERENCE_BY_ID - Entry - Time: {}, Id: {}", LocalDateTime.now(), id);
        try {
            Optional<UserPrefDto> userPreference = userPrefService.getUserPreferenceById(id);
            if (userPreference.isPresent()) {
                logger.info("GET_USER_PREFERENCE_BY_ID - Exit - Time: {}, Response: {}", LocalDateTime.now(), userPreference.get());
                return new ResponseEntity<>(userPreference.get(), HttpStatus.OK);
            } else {
                logger.info("GET_USER_PREFERENCE_BY_ID - Exit - Time: {}, Id: {}, Status: NOT_FOUND", LocalDateTime.now(), id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            logger.error("GET_USER_PREFERENCE_BY_ID - Exception - Time: {}, Id: {}, Error: {}",
                    LocalDateTime.now(), id, exception.getMessage(), exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("create")
    public ResponseEntity<DMLResponseDto> createUserPreference(@RequestHeader Map<String, String> headers,
                                                                @RequestBody UserPrefRequestDto request) {
        logger.info("CREATE_USER_PREFERENCE - Entry - Time: {}, UserId: {}, Request: {}", LocalDateTime.now(), getCurrentUserId(), request);
        try {
            DMLResponseDto response = userPrefService.createUserPreference(request);
            if ("Success".equals(response.getStatusMessage())) {
                logger.info("CREATE_USER_PREFERENCE - Exit - Time: {}, Status: SUCCESS, Message: {}", LocalDateTime.now(), response.getDetailMessage());
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else {
                logger.warn("CREATE_USER_PREFERENCE - Exit - Time: {}, Status: FAILED, Message: {}", LocalDateTime.now(), response.getDetailMessage());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            logger.error("CREATE_USER_PREFERENCE - Exception - Time: {}, UserId: {}, Error: {}",
                    LocalDateTime.now(), getCurrentUserId(), exception.getMessage(), exception);
            return new ResponseEntity<>(new DMLResponseDto("Error", "Error creating user preference"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("update/{id}")
    public ResponseEntity<DMLResponseDto> updateUserPreference(@RequestHeader Map<String, String> headers,
                                                                @PathVariable Long id,
                                                                @RequestBody UserPrefRequestDto request) {
        logger.info("UPDATE_USER_PREFERENCE - Entry - Time: {}, Id: {}, UserId: {}, Request: {}", LocalDateTime.now(), id, getCurrentUserId(), request);
        try {
            DMLResponseDto response = userPrefService.updateUserPreference(id, request);
            if ("Success".equals(response.getStatusMessage())) {
                logger.info("UPDATE_USER_PREFERENCE - Exit - Time: {}, Status: SUCCESS, Id: {}", LocalDateTime.now(), id);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                logger.warn("UPDATE_USER_PREFERENCE - Exit - Time: {}, Status: FAILED, Message: {}", LocalDateTime.now(), response.getDetailMessage());
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            logger.error("UPDATE_USER_PREFERENCE - Exception - Time: {}, Id: {}, UserId: {}, Error: {}",
                    LocalDateTime.now(), id, getCurrentUserId(), exception.getMessage(), exception);
            return new ResponseEntity<>(new DMLResponseDto("Error", "Error updating user preference"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<DMLResponseDto> deleteUserPreference(@RequestHeader Map<String, String> headers, @PathVariable Long id) {
        logger.info("DELETE_USER_PREFERENCE - Entry - Time: {}, Id: {}, UserId: {}", LocalDateTime.now(), id, getCurrentUserId());
        try {
            DMLResponseDto response = userPrefService.deleteUserPreference(id);
            if ("Success".equals(response.getStatusMessage())) {
                logger.info("DELETE_USER_PREFERENCE - Exit - Time: {}, Status: SUCCESS, Id: {}", LocalDateTime.now(), id);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                logger.warn("DELETE_USER_PREFERENCE - Exit - Time: {}, Status: FAILED, Message: {}", LocalDateTime.now(), response.getDetailMessage());
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            logger.error("DELETE_USER_PREFERENCE - Exception - Time: {}, Id: {}, UserId: {}, Error: {}",
                    LocalDateTime.now(), id, getCurrentUserId(), exception.getMessage(), exception);
            return new ResponseEntity<>(new DMLResponseDto("Error", "Error deleting user preference"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
