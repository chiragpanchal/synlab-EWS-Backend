package com.ewsv3.ews.commons;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.commons.dto.masters.*;
import com.ewsv3.ews.commons.service.CommonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/common/")
public class CommonController {


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

        try {
            AllMasterDtoLov allMasterDtoLov = this.commonService.allMasterDtoLov(this.jdbcClient);
            return new ResponseEntity<>(allMasterDtoLov, HttpStatus.OK);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("all-person")
    public ResponseEntity<List<PersonDtoLov>> getAllPerson(@RequestHeader Map<String, String> header, @RequestBody PersonRequestDto requestDto) {

        try {
            List<PersonDtoLov> personDtoLovs = this.commonService.getPerson(requestDto, this.jdbcClient);
            return new ResponseEntity<>(personDtoLovs, HttpStatus.OK);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("all-projects")
    public ResponseEntity<List<ProjectsDtoLov>> getAllProjects(@RequestHeader Map<String, String> header, @RequestBody ProjectRequestDto requestDto) {

        try {
            List<ProjectsDtoLov> projectsDtoLovs = this.commonService.getProjects(requestDto, this.jdbcClient);
            return new ResponseEntity<>(projectsDtoLovs, HttpStatus.OK);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
