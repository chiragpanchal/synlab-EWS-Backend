package com.ewsv3.ews.commons;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.commons.dto.UserIdReqDto;
import com.ewsv3.ews.commons.dto.UserProfileResponse;
import com.ewsv3.ews.commons.dto.masters.*;
import com.ewsv3.ews.commons.service.CommonService;
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
@RequestMapping("/api/common/")
public class CommonController {

    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);

    private final CommonService commonService;
    private final JdbcClient jdbcClient;

    public CommonController(CommonService commonService, JdbcClient jdbcClient) {
        this.commonService = commonService;
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

    @GetMapping("all-masters")
    public ResponseEntity<AllMasterDtoLov> getAllMasters(@RequestHeader Map<String, String> header) {
        logger.info("GET_ALL_MASTERS - Entry - Time: {}", LocalDateTime.now());

        try {
            AllMasterDtoLov allMasterDtoLov = this.commonService.allMasterDtoLov(this.jdbcClient);
            logger.info("GET_ALL_MASTERS - Exit - Time: {}, Response: {}", LocalDateTime.now(), allMasterDtoLov);
            return new ResponseEntity<>(allMasterDtoLov, HttpStatus.OK);
        } catch (Exception exception) {
            // System.out.println(exception.getMessage());
            logger.error("GET_ALL_MASTERS - Exception - Time: {}, Error: {}", LocalDateTime.now(), exception.getMessage(), exception);
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("all-person")
    public ResponseEntity<List<PersonDtoLov>> getAllPerson(@RequestHeader Map<String, String> header,
            @RequestBody PersonRequestDto requestDto) {
        logger.info("GET_ALL_PERSON - Entry - Time: {}, Request strPerson: {}", LocalDateTime.now(), requestDto.strPerson());

        try {
            // System.out.println("all-person requestDto.strPerson():" + requestDto.strPerson());
            List<PersonDtoLov> personDtoLovs = this.commonService.getPerson(requestDto, this.jdbcClient);
            logger.info("GET_ALL_PERSON - Exit - Time: {}, Request strPerson: {}, Response Count: {}",
                LocalDateTime.now(), requestDto.strPerson(), personDtoLovs.size());
            return new ResponseEntity<>(personDtoLovs, HttpStatus.OK);
        } catch (Exception exception) {
            // System.out.println(exception.getMessage());
            logger.error("GET_ALL_PERSON - Exception - Time: {}, Request: {}, Error: {}",
                LocalDateTime.now(), requestDto, exception.getMessage(), exception);
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("all-projects")
    public ResponseEntity<List<ProjectsDtoLov>> getAllProjects(@RequestHeader Map<String, String> header,
            @RequestBody ProjectRequestDto requestDto) {
        logger.info("GET_ALL_PROJECTS - Entry - Time: {}, Request: {}", LocalDateTime.now(), requestDto);

        try {
            List<ProjectsDtoLov> projectsDtoLovs = this.commonService.getProjects(requestDto, this.jdbcClient);
            logger.info("GET_ALL_PROJECTS - Exit - Time: {}, Request: {}, Response Count: {}",
                LocalDateTime.now(), requestDto, projectsDtoLovs.size());
            return new ResponseEntity<>(projectsDtoLovs, HttpStatus.OK);
        } catch (Exception exception) {
            // System.out.println(exception.getMessage());
            logger.error("GET_ALL_PROJECTS - Exception - Time: {}, Request: {}, Error: {}",
                LocalDateTime.now(), requestDto, exception.getMessage(), exception);
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("user-from-user-id")
    public ResponseEntity<UserProfileResponse> getUserFromUserId(@RequestHeader Map<String, String> header,
            @RequestBody UserIdReqDto dto) {
        logger.info("GET_USER_FROM_USER_ID - Entry - Time: {}, Request: {}", LocalDateTime.now(), dto);

        try {
            UserProfileResponse userProfileResponse = this.commonService.getUserFromUserId(dto, this.jdbcClient);
            logger.info("GET_USER_FROM_USER_ID - Exit - Time: {}, Request: {}, Response: {}",
                LocalDateTime.now(), dto, userProfileResponse);
            return new ResponseEntity<>(userProfileResponse, HttpStatus.OK);
        } catch (Exception exception) {
            // System.out.println(exception.getMessage());
            logger.error("GET_USER_FROM_USER_ID - Exception - Time: {}, Request: {}, Error: {}",
                LocalDateTime.now(), dto, exception.getMessage(), exception);
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
