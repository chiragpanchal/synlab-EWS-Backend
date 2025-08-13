package com.ewsv3.ews.rosters.controller;


import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.masters.service.MasterDataService;
import com.ewsv3.ews.rosters.dto.rosters.*;
import com.ewsv3.ews.rosters.dto.rosters.payload.*;
import com.ewsv3.ews.rosters.dto.rosters.payload.pivot.PersonRosterSqlResp;
import com.ewsv3.ews.rosters.dto.rosters.validate.ValidateRosterReqBody;
import com.ewsv3.ews.rosters.dto.rosters.validate.ValidateRosterResponse;
import com.ewsv3.ews.rosters.service.rosterService.RosterService;
import com.ewsv3.ews.team.dto.ProfileDatesRequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/roster")
public class RosterController {

    private final MasterDataService masterDataService;
    private final RosterService rosterService;
    private final JdbcClient jdbcClient;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public RosterController(MasterDataService masterDataService, RosterService rosterService,
                            JdbcClient jdbcClient, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.masterDataService = masterDataService;
        this.rosterService = rosterService;
        this.jdbcClient = jdbcClient;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getUserId();
        }
        throw new RuntimeException("User not authenticated or invalid token");
    }

    @PostMapping("/masters")
    @CrossOrigin
    public ResponseEntity<RosterMasters> getRosterMasters(@RequestHeader Map<String, String> header,
                                                          @RequestBody RosterMasterRequestBody requestBody) {

        System.out.println("requestBody:" + requestBody);
        // UserResponseDto userResponseDto = new UserResponseDto(1, "a", 1, "f", "e01",
        // "jobTitle", "dept", 101);

        try {
            // UserResponseDto userInfo = getUserInfo(header);
            System.out.println("getRosterMasters > getCurrentUserId():" + getCurrentUserId());

            RosterMasters rosterMasters = this.masterDataService.getRosterMasters(getCurrentUserId(),
                    requestBody.profileId(), requestBody.startDate(), requestBody.endDate(), jdbcClient);

            System.out.println("\ngetRosterMasters >> rosterMasters.workDurationDtoList():\n"
                    + rosterMasters.workDurationDtoList());
            System.out
                    .println("\ngetRosterMasters >> rosterMasters.onCallDtoList():\n" + rosterMasters.onCallDtoList());
            System.out
                    .println("\ngetRosterMasters >> rosterMasters:\n" + rosterMasters.workDurationDtoList().toString());

            return new ResponseEntity<>(rosterMasters, HttpStatus.OK);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @PostMapping("/rosters")
    @CrossOrigin
    public ResponseEntity<List<PersonRosters>> getRosters(@RequestHeader Map<String, String> header,
                                                          @RequestBody RosterMasterRequestBody requestBody) {
        try {
            // UserResponseDto userInfo = getUserInfo(header);
            System.out.println("getRosters > getCurrentUserId():" + getCurrentUserId());
            System.out.println("getRosters > requestBody:" + requestBody);
            List<PersonRosters> personRosters = this.rosterService.getPersonRosters(getCurrentUserId(),
                    requestBody.profileId(), requestBody.startDate(), requestBody.endDate(), jdbcClient);
            return new ResponseEntity<>(personRosters, HttpStatus.OK);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/rosters2")
    @CrossOrigin
    public ResponseEntity<List<PersonRosterPivotResponse>> getRostersProcedure(
            @RequestHeader Map<String, String> header, @RequestBody PersonRosterPivotReq requestBody) {
        try {
            // UserResponseDto userInfo = getUserInfo(header);
            System.out.println("getRostersProcedure > getCurrentUserId():" + getCurrentUserId());
            System.out.println("getRostersProcedure > requestBody:" + requestBody);
            List<PersonRosterPivotResponse> personRosters = this.rosterService
                    .getPersonRostersProcedure(getCurrentUserId(), requestBody, namedParameterJdbcTemplate, jdbcClient);
            return new ResponseEntity<>(personRosters, HttpStatus.OK);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/rosters-sm")
    @CrossOrigin
    public ResponseEntity<List<PersonRostersSmall>> getRostersSmall(@RequestHeader Map<String, String> header,
                                                                    @RequestBody RosterMasterRequestBody requestBody) {
        try {
            // UserResponseDto userInfo = getUserInfo(header);
            System.out.println("getRostersSmall > getCurrentUserId():" + getCurrentUserId());
            System.out.println("getRostersSmall > requestBody:" + requestBody);
            List<PersonRostersSmall> personRosters = this.rosterService.getPersonRostersSmall(getCurrentUserId(),
                    requestBody.profileId(), requestBody.startDate(), requestBody.endDate(), jdbcClient);
            return new ResponseEntity<>(personRosters, HttpStatus.OK);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // (long userId, Long profileId, LocalDate startDate, LocalDate endDate,int
    // page,int size,String text, String filterFlag, JdbcClient jdbcClient,
    // NamedParameterJdbcTemplate namedParameterJdbcTemplate) {

    @PostMapping("/rosters-sql")
    @CrossOrigin
    public ResponseEntity<PersonRosterSqlResp> getRostersSQL(@RequestHeader Map<String, String> header,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "100") int size,
                                                             @RequestParam(defaultValue = "") String text,
                                                             @RequestParam(defaultValue = "") String filterFlag,
                                                             @RequestBody ProfileDatesRequestBody requestBody) {
        try {
            System.out.printf("getRostersSQL header: %s", header);
            // UserResponseDto userInfo = getUserInfo(header);
            System.out.println("getRostersSQL > getCurrentUserId():" + getCurrentUserId());
            System.out.println("getRostersSQL > requestBody:" + requestBody);
            System.out.println("getRostersSQL > text:" + text);
            System.out.println("getRostersSQL > filterFlag:" + filterFlag);

            // PersonRosterSqlResp personRosterSqlResp =
            // this.rosterService.getPersonRosterSql(userInfo.userId(), requestBody,
            // namedParameterJdbcTemplate, jdbcClient);
            PersonRosterSqlResp personRosterSqlResp = this.rosterService.getPersonRosterSql(
                    getCurrentUserId(),
                    requestBody.profileId(),
                    requestBody.personId(),
                    LocalDate.ofInstant(requestBody.startDate().toInstant(), ZoneId.systemDefault()),
                    LocalDate.ofInstant(requestBody.endDate().toInstant(), ZoneId.systemDefault()),
                    page,
                    size,
                    text,
                    filterFlag,
                    jdbcClient,
                    namedParameterJdbcTemplate);
            return new ResponseEntity<>(personRosterSqlResp, HttpStatus.OK);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/spotone")
    @CrossOrigin
    public ResponseEntity<RosterDMLResponseDto> createSpotRosters(@RequestHeader Map<String, String> header,
                                                                  @RequestBody SpotRequestBody requestBody) {

        System.out.println("spotone requestBody:" + requestBody);
        // RosterCreateResponseDto responseDto = new RosterCreateResponseDto();
        try {
            // UserResponseDto userInfo = getUserInfo(header);
            System.out.println("createSpotRosters > getCurrentUserId():" + getCurrentUserId());
            RosterDMLResponseDto spotRoster = this.rosterService.createSpotRoster(getCurrentUserId(), requestBody);
            System.out.println("createSpotRosters spotRoster:" + spotRoster);
            return new ResponseEntity<>(spotRoster, HttpStatus.OK);
        } catch (Exception exception) {
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/person-rosters")
    @CrossOrigin
    public ResponseEntity<List<PersonRosters>> getPersonRosters(@RequestHeader Map<String, String> header,
                                                                @RequestBody PersonRosterReqBody requestBody) {
        try {
            System.out.println("getPersonRosters > getCurrentUserId():" + getCurrentUserId());
            System.out.println("person-rosters > requestBody:" + requestBody);

            List<PersonRosters> personRosters = this.rosterService.getSinglePersonRosters(
                    getCurrentUserId(),
                    requestBody.personId(),
                    requestBody.personRosterId(),
                    requestBody.startDate(),
                    requestBody.endDate(),
                    jdbcClient);
            return new ResponseEntity<>(personRosters, HttpStatus.OK);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/delete-rosters")
    @CrossOrigin
    public ResponseEntity<RosterDMLResponseDto> deleteRosters(@RequestHeader Map<String, String> header,
                                                              @RequestBody RosterDeleteReasonReqBody requestBody) {
        try {
            System.out.println("deleteRosters > getCurrentUserId():" + getCurrentUserId());
            System.out.println("person-rosters > requestBody:" + requestBody);

            RosterDMLResponseDto rosterDMLResponseDto = this.rosterService.deletePersonRoster(
                    getCurrentUserId(),
                    requestBody,
                    jdbcClient);
            return new ResponseEntity<>(rosterDMLResponseDto, HttpStatus.OK);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/copy-rosters")
    @CrossOrigin
    public ResponseEntity<RosterDMLResponseDto> copyPersonRoster(@RequestHeader Map<String, String> header,
                                                                 @RequestBody RosterCopyReqBody requestBody) {
        try {
            System.out.println("copyPersonRoster > getCurrentUserId():" + getCurrentUserId());
            System.out.println("copy-rosters> requestBody:" + requestBody);

            RosterDMLResponseDto rosterDMLResponseDto = this.rosterService.copyPersonRoster(
                    getCurrentUserId(),
                    requestBody,
                    jdbcClient);
            return new ResponseEntity<>(rosterDMLResponseDto, HttpStatus.OK);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/rota")
    @CrossOrigin
    public ResponseEntity<RosterDMLResponseDto> rotaRoster(@RequestHeader Map<String, String> header,
                                                           @RequestBody List<PersonRotationAssocReqBody> requestBody) {
        try {
            System.out.println("rotaRoster > getCurrentUserId():" + getCurrentUserId());
            System.out.println("rota > requestBody:" + requestBody);

            RosterDMLResponseDto rosterDMLResponseDto = this.rosterService.createPersonRota(
                    getCurrentUserId(),
                    requestBody,
                    jdbcClient);
            return new ResponseEntity<>(rosterDMLResponseDto, HttpStatus.OK);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/roster-actions")
    @CrossOrigin
    public ResponseEntity<RosterDMLResponseDto> rosterActions(@RequestHeader Map<String, String> header,
                                                              @RequestBody RosterActionsReqBody requestBody) {
        try {
            System.out.println("rosterActions > getCurrentUserId():" + getCurrentUserId());
            System.out.println("rosterActions > requestBody:" + requestBody);

            RosterDMLResponseDto rosterDMLResponseDto = this.rosterService.rosterActions(
                    getCurrentUserId(),
                    requestBody,
                    jdbcClient);
            return new ResponseEntity<>(rosterDMLResponseDto, HttpStatus.OK);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/demand-rosters")
    @CrossOrigin
    public ResponseEntity<DemandAllocationRespBody> resterDemands(@RequestHeader Map<String, String> header,
                                                                  @RequestBody DemandAllocationReqBody requestBody) {
        try {
            System.out.println("resterDemands > getCurrentUserId():" + getCurrentUserId());
            System.out.println("resterDemands > requestBody:" + requestBody);

            DemandAllocationRespBody allocations = this.rosterService.getDemandAllocations(
                    getCurrentUserId(),
                    requestBody,
                    jdbcClient);
            return new ResponseEntity<>(allocations, HttpStatus.OK);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/validate-rosters")
    @CrossOrigin
    public ResponseEntity<ValidateRosterResponse> getValidateRosterResponse(@RequestHeader Map<String, String> header, @RequestBody ValidateRosterReqBody reqBody) {
        try {
            System.out.println("getValidateRosterResponse > getCurrentUserId():" + getCurrentUserId());
            System.out.println("getValidateRosterResponse > reqBody:" + reqBody);

            ValidateRosterResponse rosterResponse = this.rosterService.getValidateRosterResponse(
                    getCurrentUserId(),
                    reqBody,
                    jdbcClient);
            return new ResponseEntity<>(rosterResponse, HttpStatus.OK);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
