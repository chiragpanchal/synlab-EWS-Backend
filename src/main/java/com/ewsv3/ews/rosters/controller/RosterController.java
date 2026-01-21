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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/roster")
public class RosterController {
    private static final Logger logger = LoggerFactory.getLogger(RosterController.class);

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
        logger.info("GET_ROSTER_MASTERS - Entry - Time: {}, Request: {}", LocalDateTime.now(), requestBody);

        // System.out.println("requestBody:" + requestBody);
        // UserResponseDto userResponseDto = new UserResponseDto(1, "a", 1, "f", "e01",
        // "jobTitle", "dept", 101);

        try {
            // UserResponseDto userInfo = getUserInfo(header);
            // System.out.println("getRosterMasters > getCurrentUserId():" +
            // getCurrentUserId());

            RosterMasters rosterMasters = this.masterDataService.getRosterMasters(getCurrentUserId(),
                    requestBody.profileId(), requestBody.startDate(), requestBody.endDate(), jdbcClient);

            // System.out.println("\ngetRosterMasters >>
            // rosterMasters.workDurationDtoList():\n"
            // + rosterMasters.workDurationDtoList());
            // System.out
            // .println("\ngetRosterMasters >> rosterMasters.onCallDtoList():\n" +
            // rosterMasters.onCallDtoList());
            // System.out
            // .println("\ngetRosterMasters >> rosterMasters:\n" +
            // rosterMasters.workDurationDtoList().toString());

            logger.info(
                    "GET_ROSTER_MASTERS - Exit - Time: {}, Response: RosterMasters with {} work durations and {} on-call entries",
                    LocalDateTime.now(), rosterMasters.workDurationDtoList().size(),
                    rosterMasters.onCallDtoList().size());
            return new ResponseEntity<>(rosterMasters, HttpStatus.OK);
        } catch (Exception exception) {
            // System.out.println(exception.getMessage());
            logger.error("GET_ROSTER_MASTERS - Exception - Time: {}, Request: {}, Error: {}",
                    LocalDateTime.now(), requestBody, exception.getMessage(), exception);
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/rosters")
    @CrossOrigin
    public ResponseEntity<List<PersonRosters>> getRosters(@RequestHeader Map<String, String> header,
            @RequestBody RosterMasterRequestBody requestBody) {
        logger.info("GET_ROSTERS - Entry - Time: {}, Request: {}", LocalDateTime.now(), requestBody);
        try {
            // UserResponseDto userInfo = getUserInfo(header);
            // System.out.println("getRosters > getCurrentUserId():" + getCurrentUserId());
            // System.out.println("getRosters > requestBody:" + requestBody);
            List<PersonRosters> personRosters = this.rosterService.getPersonRosters(getCurrentUserId(),
                    requestBody.profileId(), requestBody.startDate(), requestBody.endDate(), jdbcClient);
            logger.info("GET_ROSTERS - Exit - Time: {}, Response count: {}", LocalDateTime.now(), personRosters.size());
            return new ResponseEntity<>(personRosters, HttpStatus.OK);
        } catch (Exception exception) {
            // System.out.println(exception.getMessage());
            logger.error("GET_ROSTERS - Exception - Time: {}, Request: {}, Error: {}",
                    LocalDateTime.now(), requestBody, exception.getMessage(), exception);
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/rosters2")
    @CrossOrigin
    public ResponseEntity<List<PersonRosterPivotResponse>> getRostersProcedure(
            @RequestHeader Map<String, String> header, @RequestBody PersonRosterPivotReq requestBody) {
        logger.info("GET_ROSTERS_PROCEDURE - Entry - Time: {}, Request: {}", LocalDateTime.now(), requestBody);
        try {
            // UserResponseDto userInfo = getUserInfo(header);
            // System.out.println("getRostersProcedure > getCurrentUserId():" +
            // getCurrentUserId());
            // System.out.println("getRostersProcedure > requestBody:" + requestBody);
            List<PersonRosterPivotResponse> personRosters = this.rosterService
                    .getPersonRostersProcedure(getCurrentUserId(), requestBody, namedParameterJdbcTemplate, jdbcClient);
            logger.info("GET_ROSTERS_PROCEDURE - Exit - Time: {}, Response count: {}", LocalDateTime.now(),
                    personRosters.size());
            return new ResponseEntity<>(personRosters, HttpStatus.OK);
        } catch (Exception exception) {
            // System.out.println(exception.getMessage());
            logger.error("GET_ROSTERS_PROCEDURE - Exception - Time: {}, Request: {}, Error: {}",
                    LocalDateTime.now(), requestBody, exception.getMessage(), exception);
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/rosters-sm")
    @CrossOrigin
    public ResponseEntity<List<PersonRostersSmall>> getRostersSmall(@RequestHeader Map<String, String> header,
            @RequestBody RosterMasterRequestBody requestBody) {
        logger.info("GET_ROSTERS_SMALL - Entry - Time: {}, Request: {}", LocalDateTime.now(), requestBody);
        try {
            // UserResponseDto userInfo = getUserInfo(header);
            // System.out.println("getRostersSmall > getCurrentUserId():" +
            // getCurrentUserId());
            // System.out.println("getRostersSmall > requestBody:" + requestBody);
            List<PersonRostersSmall> personRosters = this.rosterService.getPersonRostersSmall(getCurrentUserId(),
                    requestBody.profileId(), requestBody.startDate(), requestBody.endDate(), jdbcClient);
            logger.info("GET_ROSTERS_SMALL - Exit - Time: {}, Response count: {}", LocalDateTime.now(),
                    personRosters.size());
            return new ResponseEntity<>(personRosters, HttpStatus.OK);
        } catch (Exception exception) {
            // System.out.println(exception.getMessage());
            logger.error("GET_ROSTERS_SMALL - Exception - Time: {}, Request: {}, Error: {}",
                    LocalDateTime.now(), requestBody, exception.getMessage(), exception);
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
        logger.info(
                "GET_ROSTERS_SQL - Entry - Time: {}, ProfileId: {}, PersonId: {}, Page: {}, Size: {}, Text: {}, FilterFlag: {}",
                LocalDateTime.now(), requestBody.profileId(), requestBody.personId(), page, size, text, filterFlag);
        try {
            // System.out.printf("getRostersSQL header: %s", header);
            // UserResponseDto userInfo = getUserInfo(header);
            // System.out.println("getRostersSQL > getCurrentUserId():" +
            // getCurrentUserId());
            // System.out.println("getRostersSQL > requestBody:" + requestBody);
            // System.out.println("getRostersSQL > text:" + text);
            // System.out.println("getRostersSQL > filterFlag:" + filterFlag);

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
            logger.info("GET_ROSTERS_SQL - Exit - Time: {}, Response: {}", LocalDateTime.now(), personRosterSqlResp);
            return new ResponseEntity<>(personRosterSqlResp, HttpStatus.OK);
        } catch (Exception exception) {
            // System.out.println(exception.getMessage());
            logger.error("GET_ROSTERS_SQL - Exception - Time: {}, Request: {}, Error: {}",
                    LocalDateTime.now(), requestBody, exception.getMessage(), exception);
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/spotone")
    @CrossOrigin
    public ResponseEntity<RosterDMLResponseDto> createSpotRosters(@RequestHeader Map<String, String> header,
            @RequestBody SpotRequestBody requestBody) {
        logger.info("CREATE_SPOT_ROSTERS - Entry - Time: {}, Request: {}", LocalDateTime.now(), requestBody);

        // System.out.println("spotone requestBody:" + requestBody);
        // RosterCreateResponseDto responseDto = new RosterCreateResponseDto();
        try {
            // UserResponseDto userInfo = getUserInfo(header);
            // System.out.println("createSpotRosters > getCurrentUserId():" +
            // getCurrentUserId());
            RosterDMLResponseDto spotRoster = this.rosterService.createSpotRoster(getCurrentUserId(), requestBody);
            // System.out.println("createSpotRosters spotRoster:" + spotRoster);
            logger.info("CREATE_SPOT_ROSTERS - Exit - Time: {}, Response: {}", LocalDateTime.now(), spotRoster);
            return new ResponseEntity<>(spotRoster, HttpStatus.OK);
        } catch (Exception exception) {
            logger.error("CREATE_SPOT_ROSTERS - Exception - Time: {}, Request: {}, Error: {}",
                    LocalDateTime.now(), requestBody, exception.getMessage(), exception);
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/person-rosters")
    @CrossOrigin
    public ResponseEntity<List<PersonRosters>> getPersonRosters(@RequestHeader Map<String, String> header,
            @RequestBody PersonRosterReqBody requestBody) {
        logger.info("GET_PERSON_ROSTERS - Entry - Time: {}, Request: {}", LocalDateTime.now(), requestBody);
        try {
            // System.out.println("getPersonRosters > getCurrentUserId():" +
            // getCurrentUserId());
            // System.out.println("person-rosters > requestBody:" + requestBody);

            List<PersonRosters> personRosters = this.rosterService.getSinglePersonRosters(
                    getCurrentUserId(),
                    requestBody.personId(),
                    requestBody.personRosterId(),
                    requestBody.startDate(),
                    requestBody.endDate(),
                    jdbcClient);
            logger.info("GET_PERSON_ROSTERS - Exit - Time: {}, Response count: {}", LocalDateTime.now(),
                    personRosters.size());
            return new ResponseEntity<>(personRosters, HttpStatus.OK);
        } catch (Exception exception) {
            // System.out.println(exception.getMessage());
            logger.error("GET_PERSON_ROSTERS - Exception - Time: {}, Request: {}, Error: {}",
                    LocalDateTime.now(), requestBody, exception.getMessage(), exception);
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/delete-rosters")
    @CrossOrigin
    public ResponseEntity<RosterDMLResponseDto> deleteRosters(@RequestHeader Map<String, String> header,
            @RequestBody RosterDeleteReasonReqBody requestBody) {
        logger.info("DELETE_ROSTERS - Entry - Time: {}, Request: {}", LocalDateTime.now(), requestBody);
        try {
            // System.out.println("deleteRosters > getCurrentUserId():" +
            // getCurrentUserId());
            // System.out.println("person-rosters > requestBody:" + requestBody);

            RosterDMLResponseDto rosterDMLResponseDto = this.rosterService.deletePersonRoster(
                    getCurrentUserId(),
                    requestBody,
                    jdbcClient);
            logger.info("DELETE_ROSTERS - Exit - Time: {}, Response: {}", LocalDateTime.now(), rosterDMLResponseDto);
            return new ResponseEntity<>(rosterDMLResponseDto, HttpStatus.OK);
        } catch (Exception exception) {
            // System.out.println(exception.getMessage());
            logger.error("DELETE_ROSTERS - Exception - Time: {}, Request: {}, Error: {}",
                    LocalDateTime.now(), requestBody, exception.getMessage(), exception);
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/copy-rosters")
    @CrossOrigin
    public ResponseEntity<RosterDMLResponseDto> copyPersonRoster(@RequestHeader Map<String, String> header,
            @RequestBody RosterCopyReqBody requestBody) {
        logger.info("COPY_ROSTERS - Entry - Time: {}, Request: {}", LocalDateTime.now(), requestBody);
        try {
            // System.out.println("copyPersonRoster > getCurrentUserId():" +
            // getCurrentUserId());
            // System.out.println("copy-rosters> requestBody:" + requestBody);

            RosterDMLResponseDto rosterDMLResponseDto = this.rosterService.copyPersonRoster(
                    getCurrentUserId(),
                    requestBody,
                    jdbcClient);
            logger.info("COPY_ROSTERS - Exit - Time: {}, Response: {}", LocalDateTime.now(), rosterDMLResponseDto);
            return new ResponseEntity<>(rosterDMLResponseDto, HttpStatus.OK);
        } catch (Exception exception) {
            // System.out.println(exception.getMessage());
            logger.error("COPY_ROSTERS - Exception - Time: {}, Request: {}, Error: {}",
                    LocalDateTime.now(), requestBody, exception.getMessage(), exception);
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/rota")
    @CrossOrigin
    public ResponseEntity<RosterDMLResponseDto> rotaRoster(@RequestHeader Map<String, String> header,
            @RequestBody List<PersonRotationAssocReqBody> requestBody) {
        logger.info("ROTA_ROSTER - Entry - Time: {}, Request count: {}", LocalDateTime.now(), requestBody.size());
        try {
            // System.out.println("rotaRoster > getCurrentUserId():" + getCurrentUserId());
            // System.out.println("rota > requestBody:" + requestBody);

            RosterDMLResponseDto rosterDMLResponseDto = this.rosterService.createPersonRota(
                    getCurrentUserId(),
                    requestBody,
                    jdbcClient);
            logger.info("ROTA_ROSTER - Exit - Time: {}, Response: {}", LocalDateTime.now(), rosterDMLResponseDto);
            return new ResponseEntity<>(rosterDMLResponseDto, HttpStatus.OK);
        } catch (Exception exception) {
            // System.out.println(exception.getMessage());
            logger.error("ROTA_ROSTER - Exception - Time: {}, Request count: {}, Error: {}",
                    LocalDateTime.now(), requestBody.size(), exception.getMessage(), exception);
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/roster-actions")
    @CrossOrigin
    public ResponseEntity<RosterDMLResponseDto> rosterActions(@RequestHeader Map<String, String> header,
            @RequestBody RosterActionsReqBody requestBody) {
        logger.info("ROSTER_ACTIONS - Entry - Time: {}, Request: {}", LocalDateTime.now(), requestBody);
        try {
            // System.out.println("rosterActions > getCurrentUserId():" +
            // getCurrentUserId());
            // System.out.println("rosterActions > requestBody:" + requestBody);

            RosterDMLResponseDto rosterDMLResponseDto = this.rosterService.rosterActions(
                    getCurrentUserId(),
                    requestBody,
                    jdbcClient);
            logger.info("ROSTER_ACTIONS - Exit - Time: {}, Response: {}", LocalDateTime.now(), rosterDMLResponseDto);
            return new ResponseEntity<>(rosterDMLResponseDto, HttpStatus.OK);
        } catch (Exception exception) {
            // System.out.println(exception.getMessage());
            logger.error("ROSTER_ACTIONS - Exception - Time: {}, Request: {}, Error: {}",
                    LocalDateTime.now(), requestBody, exception.getMessage(), exception);
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/demand-rosters")
    @CrossOrigin
    public ResponseEntity<DemandAllocationRespBody> resterDemands(@RequestHeader Map<String, String> header,
            @RequestBody DemandAllocationReqBody requestBody) {
        logger.info("DEMAND_ROSTERS - Entry - Time: {}, Request: {}", LocalDateTime.now(), requestBody);
        try {
            // System.out.println("resterDemands > getCurrentUserId():" +
            // getCurrentUserId());
            // System.out.println("resterDemands > requestBody:" + requestBody);

            DemandAllocationRespBody allocations = this.rosterService.getDemandAllocationsNew(
                    getCurrentUserId(),
                    requestBody,
                    jdbcClient);
            logger.info("DEMAND_ROSTERS - Exit - Time: {}, Response: {}", LocalDateTime.now(), allocations);
            return new ResponseEntity<>(allocations, HttpStatus.OK);
        } catch (Exception exception) {
            // System.out.println(exception.getMessage());
            logger.error("DEMAND_ROSTERS - Exception - Time: {}, Request: {}, Error: {}",
                    LocalDateTime.now(), requestBody, exception.getMessage(), exception);
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/validate-rosters")
    @CrossOrigin
    public ResponseEntity<ValidateRosterResponse> getValidateRosterResponse(@RequestHeader Map<String, String> header,
            @RequestBody ValidateRosterReqBody reqBody) {
        logger.info("VALIDATE_ROSTERS - Entry - Time: {}, Request: {}", LocalDateTime.now(), reqBody);
        try {
            // System.out.println("getValidateRosterResponse > getCurrentUserId():" +
            // getCurrentUserId());
            // System.out.println("getValidateRosterResponse > reqBody:" + reqBody);

            ValidateRosterResponse rosterResponse = this.rosterService.getValidateRosterResponse(
                    getCurrentUserId(),
                    reqBody,
                    jdbcClient);
            logger.info("VALIDATE_ROSTERS - Exit - Time: {}, Response: {}", LocalDateTime.now(), rosterResponse);
            return new ResponseEntity<>(rosterResponse, HttpStatus.OK);
        } catch (Exception exception) {
            // System.out.println(exception.getMessage());
            logger.error("VALIDATE_ROSTERS - Exception - Time: {}, Request: {}, Error: {}",
                    LocalDateTime.now(), reqBody, exception.getMessage(), exception);
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/validate-rosters-details")
    @CrossOrigin
    public ResponseEntity<ValidateRosterResponse> getValidateRosterDetailsResponse(@RequestHeader Map<String, String> header,
                                                                            @RequestBody ValidateRosterReqBody reqBody) {
        logger.info("VALIDATE_ROSTERS DETAILS- Entry - Time: {}, Request: {}", LocalDateTime.now(), reqBody);
        try {
            // System.out.println("getValidateRosterResponse > getCurrentUserId():" +
            // getCurrentUserId());
            // System.out.println("getValidateRosterResponse > reqBody:" + reqBody);

            ValidateRosterResponse rosterResponse = this.rosterService.getValidateRosterResponse(
                    getCurrentUserId(),
                    reqBody,
                    jdbcClient);
            logger.info("VALIDATE_ROSTERS DETAILS- Exit - Time: {}, Response: {}", LocalDateTime.now(), rosterResponse);
            return new ResponseEntity<>(rosterResponse, HttpStatus.OK);
        } catch (Exception exception) {
            // System.out.println(exception.getMessage());
            logger.error("VALIDATE_ROSTERS DETAILS- Exception - Time: {}, Request: {}, Error: {}",
                    LocalDateTime.now(), reqBody, exception.getMessage(), exception);
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/drag-drop")
    @CrossOrigin
    public ResponseEntity<RosterDMLResponseDto> dragDrapPersonRoster(@RequestHeader Map<String, String> header,
            @RequestBody DragDropReqBody requestBody) {
        logger.info("DRAG_DROP_ROSTER - Entry - Time: {}, Request: {}", LocalDateTime.now(), requestBody);
        try {
            // System.out.println("drag-drop > getCurrentUserId():" + getCurrentUserId());
            // System.out.println("drag-drop > requestBody:" + requestBody);

            RosterDMLResponseDto rosterDMLResponseDto = this.rosterService.dragDropPersonRoster(
                    getCurrentUserId(),
                    requestBody,
                    jdbcClient);
            logger.info("DRAG_DROP_ROSTER - Exit - Time: {}, Response: {}", LocalDateTime.now(), rosterDMLResponseDto);
            return new ResponseEntity<>(rosterDMLResponseDto, HttpStatus.OK);
        } catch (Exception exception) {
            // System.out.println(exception.getMessage());
            logger.error("DRAG_DROP_ROSTER - Exception - Time: {}, Request: {}, Error: {}",
                    LocalDateTime.now(), requestBody, exception.getMessage(), exception);
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/quick-copy")
    @CrossOrigin
    public ResponseEntity<RosterDMLResponseDto> quickCopyPersonRoster(@RequestHeader Map<String, String> header,
            @RequestBody QuickCopyReqBody requestBody) {
        logger.info("QUICK_COPY_ROSTER - Entry - Time: {}, Request: {}", LocalDateTime.now(), requestBody);
        try {
            // System.out.println("quick-copy > getCurrentUserId():" + getCurrentUserId());
            // System.out.println("quick-copy > requestBody:" + requestBody);

            RosterDMLResponseDto rosterDMLResponseDto = this.rosterService.quickCopyPersonRoster(
                    getCurrentUserId(),
                    requestBody,
                    jdbcClient);
            logger.info("QUICK_COPY_ROSTER - Exit - Time: {}, Response: {}", LocalDateTime.now(), rosterDMLResponseDto);
            return new ResponseEntity<>(rosterDMLResponseDto, HttpStatus.OK);
        } catch (Exception exception) {
            // System.out.println(exception.getMessage());
            logger.error("QUICK_COPY_ROSTER - Exception - Time: {}, Request: {}, Error: {}",
                    LocalDateTime.now(), requestBody, exception.getMessage(), exception);
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("alternate-staff")
    @CrossOrigin
    public ResponseEntity<List<AlternatePersonDto>> getAlternatePersonList(
            @RequestHeader Map<String, String> header,
            @RequestBody AlternateStaffReqBody requestBody
    ){

        logger.info("alternate-staff - Entry - Time: {}, Request: {}", LocalDateTime.now(), requestBody);
        try {
            // System.out.println("quick-copy > getCurrentUserId():" + getCurrentUserId());
            // System.out.println("quick-copy > requestBody:" + requestBody);

            List<AlternatePersonDto> alternatePersonList = this.rosterService.getAlternatePersonList(
                    getCurrentUserId(),
                    requestBody,
                    jdbcClient);
            logger.info("alternate-staff - Exit - Time: {}, alternatePersonList Counts: {}", LocalDateTime.now(), alternatePersonList.size());
            return new ResponseEntity<>(alternatePersonList, HttpStatus.OK);
        } catch (Exception exception) {
            // System.out.println(exception.getMessage());
            logger.error("alternate-staff - Exception - Time: {}, Request: {}, Error: {}",
                    LocalDateTime.now(), requestBody, exception.getMessage(), exception);
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
