package com.ewsv3.ews.rosters.service.rosterService;

import com.ewsv3.ews.masters.service.ServiceUtils;
import com.ewsv3.ews.rosters.controller.RosterController;
import com.ewsv3.ews.rosters.dto.rosters.*;
import com.ewsv3.ews.rosters.dto.rosters.payload.*;
import com.ewsv3.ews.rosters.dto.rosters.payload.pivot.PersonRosterSqlResp;
import com.ewsv3.ews.rosters.dto.rosters.validate.DemandLineResponse;
import com.ewsv3.ews.rosters.dto.rosters.validate.ScheduleLineResponse;
import com.ewsv3.ews.rosters.dto.rosters.validate.ValidateRosterReqBody;
import com.ewsv3.ews.rosters.dto.rosters.validate.ValidateRosterResponse;
import com.ewsv3.ews.team.dto.ProfileDatesRequestBody;
import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ewsv3.ews.rosters.service.utils.RosterSql.*;

@Service
public class RosterService {

    // private final JdbcClient jdbcClient;
    //
    // public RosterService(JdbcClient jdbcClient) {
    // this.jdbcClient = jdbcClient;
    // }

    private SimpleJdbcCall simpleJdbcCall;
    private final JdbcTemplate jdbcTemplate;

    private static final Logger logger = LoggerFactory.getLogger(RosterController.class);

    public RosterService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void init() {
        jdbcTemplate.setResultsMapCaseInsensitive(true);
        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("SC_CREATE_SPOT_ROSTER_P");
    }

    public List<PersonRosters> getPersonRosters(Long userId, Long profileId, Date startDate, Date endDate,
                                                JdbcClient jdbcClient) {

        // ServiceUtils serviceUtils = new ServiceUtils();

        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.printf("\nInside getPersonRosters: %s========================> ", LocalTime.now());
        Map<String, Object> objectMap = new HashMap<>();

        objectMap.put("userId", userId);
        objectMap.put("profileId", profileId);
        objectMap.put("startDate", startDate);
        objectMap.put("endDate", endDate);

        System.out.printf("\nInside getPersonRosters objectMap:%s\n", objectMap);

        List<PersonRosters> personRostersList = jdbcClient.sql(ServiceUtils.getPersonRosterDataSql)
                .params(objectMap)
                .query(PersonRosters.class)
                .list();

        System.out.printf("Completed getPersonRosters: %s========================> \n", LocalTime.now());

        return personRostersList;

    }

    public PersonRosterSqlResp getPersonRosterSql_old(Long userId, PersonRosterPivotReq personRosterPivotReq,
                                                      NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcClient jdbcClient) {

        // ServiceUtils serviceUtils = new ServiceUtils();

        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.printf("\ngetPersonRosterSql: BEGIN%s========================> \n", LocalTime.now());
        Map<String, Object> objectMap = new HashMap<>();

        objectMap.put("userId", userId);
        objectMap.put("startDate", LocalDate.parse(personRosterPivotReq.startDate()));
        objectMap.put("endDate", LocalDate.parse(personRosterPivotReq.endDate()));
        objectMap.put("profileId", personRosterPivotReq.profileId());

        System.out.printf("\ngetPersonRosterSql objectMap:%s\n", objectMap);

        // part 1: Getting Roster lines date

        // List<PersonRosterLines> personRostersList =
        // jdbcClient.sql(ServiceUtils.personRosterSql)
        // .params(objectMap)
        // .query(PersonRosterLines.class)
        // .list();

        // System.out.printf("getPersonRosterSql personRostersList: %s\n",
        // personRostersList);

        // part 2: Getting KPI String
        System.out.printf("\n\t\tgetPersonRosterSql: KPI Procedure call BEGIN%s========================> \n",
                LocalTime.now());
        String kpiString = "";
        Map<String, Object> procParamMap = new HashMap<>();
        procParamMap.put("p_user_id", userId);
        procParamMap.put("p_start_date", LocalDate.parse(personRosterPivotReq.startDate()));
        procParamMap.put("p_end_date", LocalDate.parse(personRosterPivotReq.endDate()));
        procParamMap.put("p_profile_id", personRosterPivotReq.profileId());

        JdbcTemplate template = namedParameterJdbcTemplate.getJdbcTemplate();
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(template).withProcedureName("SC_GET_ROSTER_SQL_KPI");
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(procParamMap);
        Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(sqlParameterSource);
        System.out.printf("\n\t\tgetPersonRosterSql: KPI Procedure call END%s========================> \n",
                LocalTime.now());

        kpiString = (String) simpleJdbcCallResult.get("P_KPI_STRING");
        System.out.printf("kpiString: %s\n", kpiString);

        // part 3: Preparing final response

        // PersonRosterSqlResp rosterSqlResp = new
        // PersonRosterSqlResp(personRostersList, kpiString);
        System.out.printf("\ngetPersonRosterSql: END%s========================> \n", LocalTime.now());

        // return rosterSqlResp;
        return null;

    }

    public PersonRosterSqlResp getPersonRosterSql(long userId, Long profileId, Long personId, LocalDate startDate,
                                                  LocalDate endDate, int page, int size, String text, String filterFlag, JdbcClient jdbcClient,
                                                  NamedParameterJdbcTemplate namedParameterJdbcTemplate) {

        // System.out.println("getPersonRosterSql userId:" + userId);
        Map<String, Object> objectMap = null;
        String searchText = Objects.equals(text, "") ? "%" : "%" + text.trim() + "%";
        try {
            objectMap = Map.of(
                    "userId", userId,
                    "profileId", profileId,
                    "personId", personId == null ? 0L : personId,
                    "startDate", startDate,
                    "endDate", endDate,
                    "offset", page,
                    "pageSize", size,
                    "text", searchText,
                    "pFilterFlag", filterFlag == null ? "Y" : filterFlag);
        } catch (Exception e) {
            // System.out.println("Exception in creating objectMap:" + e);
            throw new RuntimeException(e);
        }

        // System.out.println("getPersonRosterSql objectMap:" + objectMap);

        // Step 1: Get the roster team members (with pagination)
        List<RosterLines> rosterLines = jdbcClient.sql(RosterTeamSql).params(objectMap).query(RosterLines.class).list();
        System.out.printf("\ngetPersonRosterSql rosterLines:%s\n", rosterLines);

        if (rosterLines.isEmpty()) {
            // If no roster lines found, return empty response
            String kpiString = getKpiString(userId, profileId, startDate, endDate, searchText,
                    namedParameterJdbcTemplate);
            return new PersonRosterSqlResp(rosterLines, kpiString);
        }

        // step 1.5: Get Roster Error String for schedule rule
        List<RosterErrorString> rosterErrorStrings = jdbcClient.sql(errorStringSQL)
                .param("userId", userId)
                .param("profileId", profileId)
                .param("startDate", startDate)
                .param("endDate", endDate)
                .param("offset", page)
                .param("pageSize", size)
                .param("text", searchText)
                .param("pFilterFlag", filterFlag == null ? "Y" : filterFlag)
                .query(RosterErrorString.class)
                .list();

        // Step 2: Get all roster children in one batch query (PERFORMANCE OPTIMIZATION)
        // System.out.println("Fetching all roster children in batch - Performance
        // Optimization");
        long batchQueryStart = System.currentTimeMillis();

        Map<String, Object> batchQueryMap = Map.of(
                "userId", userId,
                "profileId", profileId,
                "startDate", startDate,
                "endDate", endDate,
                "text", searchText,
                "pFilterFlag", filterFlag == null ? "Y" : filterFlag);

        List<RosterLinesChild> allChildren = jdbcClient.sql(RosterMemberChildBatchSql)
                .params(batchQueryMap)
                .query(RosterLinesChild.class)
                .list();

        long batchQueryEnd = System.currentTimeMillis();
        System.out.printf("Batch query completed in %d ms, fetched %d roster children records\n",
                (batchQueryEnd - batchQueryStart), allChildren.size());

        // Step 3: Group children by person_id for efficient lookup
        Map<Long, List<RosterLinesChild>> childrenByPersonId = allChildren.stream()
                .collect(Collectors.groupingBy(RosterLinesChild::personId));

        // Step 4: Process each roster line and assign its children
        for (RosterLines rosterLine : rosterLines) {
            // System.out.println("Processing rosterLine.getPersonId(): " +
            // rosterLine.getPersonId());

            // Get children for this person from the batch result
            List<RosterLinesChild> children = childrenByPersonId.getOrDefault(rosterLine.getPersonId(),
                    new ArrayList<>());

            // Ensure at least one record per date between startDate and endDate
            Set<LocalDate> existingDates = children.stream()
                    .map(RosterLinesChild::effectiveDate)
                    .collect(Collectors.toSet());

            List<RosterLinesChild> filledChildren = new ArrayList<>(children);
            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                if (!existingDates.contains(date)) {
                    RosterLinesChild emptyChild = getLinesChild(rosterLine, date);
                    filledChildren.add(emptyChild);
                }
            }

            filledChildren.sort(Comparator.comparing(RosterLinesChild::effectiveDate));

            // Group filledChildren by effectiveDate and create RosterLinesChildDates
            Map<LocalDate, List<RosterLinesChild>> groupedByDate = filledChildren.stream()
                    .collect(Collectors.groupingBy(RosterLinesChild::effectiveDate));
            List<RosterLinesChildDates> childDatesList = groupedByDate.entrySet().stream()
                    .map(entry -> new RosterLinesChildDates(entry.getKey(),
                            entry.getValue().toArray(new RosterLinesChild[0])))
                    .sorted(Comparator.comparing(RosterLinesChildDates::effectiveDate))
                    .collect(Collectors.toList());

            // for (RosterLinesChildDates dates : childDatesList) {
            // System.out.println("childDatesList: " + dates.effectiveDate() + ":" +
            // dates.children().length);
            // }
            rosterLine.setChildren(childDatesList);

            RosterErrorString errorString = rosterErrorStrings.stream().filter(str -> str.personId() == rosterLine.getPersonId()).findFirst().orElse(null);

//            assert errorString != null;
            if (errorString != null) {
                rosterLine.setErrorString(errorString.errorString());
            } else {
                rosterLine.setErrorString(null);
            }
        }

        // for (RosterLines line : rosterLines) {
        // line.getChildren().forEach(rosterLinesChildDates -> {
        // System.out
        // .println("rosterLines:" + line.getPersonName() + ":" +
        // rosterLinesChildDates.children().length);
        // });
        // }

        // Step 5: Get KPI String
        String kpiString = getKpiString(userId, profileId, startDate, endDate, searchText, namedParameterJdbcTemplate);

        // Step 6: Prepare final response
        PersonRosterSqlResp rosterSqlResp = new PersonRosterSqlResp(rosterLines, kpiString);
        System.out.printf("\ngetPersonRosterSql: END%s========================> \n", LocalTime.now());

        return rosterSqlResp;
    }

    /**
     * Helper method to get KPI string - extracted for reusability and cleaner code
     */
    private String getKpiString(long userId, Long profileId, LocalDate startDate, LocalDate endDate,
                                String searchText, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        System.out.printf("\n\t\tgetPersonRosterSql: KPI Procedure call BEGIN%s========================> \n",
                LocalTime.now());

        Map<String, Object> procParamMap = new HashMap<>();
        procParamMap.put("p_user_id", userId);
        procParamMap.put("p_start_date", startDate);
        procParamMap.put("p_end_date", endDate);
        procParamMap.put("p_profile_id", profileId);
        procParamMap.put("p_text", searchText);

        JdbcTemplate template = namedParameterJdbcTemplate.getJdbcTemplate();
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(template).withProcedureName("SC_GET_ROSTER_SQL_KPI");
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(procParamMap);
        Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(sqlParameterSource);

        System.out.printf("\n\t\tgetPersonRosterSql: KPI Procedure call END%s========================> \n",
                LocalTime.now());

        String kpiString = (String) simpleJdbcCallResult.get("P_KPI_STRING");
        System.out.printf("kpiString: %s\n", kpiString);

        return kpiString;
    }

    private static RosterLinesChild getLinesChild(RosterLines rosterLine, LocalDate date) {
        RosterLinesChild emptyChild = new RosterLinesChild(
                rosterLine.getPersonId(),
                rosterLine.getAssignmentId(),
                null, // personRosterId
                date,
                null, // timeStart
                null, // timeEnd
                0.0, // schHrs
                null, // schDepartmentId
                null, // schJobTitleId
                null, // schWorkLocationId
                null, // schDepartment
                null, // schJobTitle
                null, // schLocation
                null, // onCall
                null, // emergency
                null, // published
                null, // workDurationId
                null, // workDurationCode
                null, // workDurationName
                null, // timeHour
                null,
                null);
        emptyChild.setEffectiveDate(date);
        return emptyChild;
    }

    public List<PersonRosterPivotResponse> getPersonRostersProcedure(Long userId,
                                                                     PersonRosterPivotReq personRosterPivotReq, NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                                                     JdbcClient jdbcClient) {

        // System.out.println("inside getPersonRostersProcedure");
        // System.out.println("personRosterPivotReq:%s\n" + personRosterPivotReq);

        // JdbcTemplate template = namedParameterJdbcTemplate.getJdbcTemplate();
        // SimpleJdbcCall simpleJdbcCall = new
        // SimpleJdbcCall(template).withProcedureName("SC_PERSON_ROSTER_PIVOT_P5");

        Map<String, Object> procParamMap = new HashMap<>();
        String p_kpi_string = "";
        procParamMap.put("p_user_id", userId);
        procParamMap.put("p_start_date", LocalDate.parse(personRosterPivotReq.startDate()));
        procParamMap.put("p_end_date", LocalDate.parse(personRosterPivotReq.endDate()));
        procParamMap.put("p_profile_id", personRosterPivotReq.profileId());
        procParamMap.put("p_person_ids", personRosterPivotReq.personIds());
        procParamMap.put("p_department_ids", personRosterPivotReq.departmentIds());
        procParamMap.put("p_job_title_ids", personRosterPivotReq.jobTitleTds());
        procParamMap.put("p_work_location_ids", personRosterPivotReq.workLocationIds());
        procParamMap.put("p_business_unit_ids", personRosterPivotReq.businessUnitIds());
        procParamMap.put("p_legal_entity_ids", personRosterPivotReq.legalEntityIds());
        procParamMap.put("p_duty_manager_ids", personRosterPivotReq.dutyManagerIds());
        procParamMap.put("p_appr_status", personRosterPivotReq.apprStatus());
        procParamMap.put("p_filter_by", personRosterPivotReq.filterBy());
        procParamMap.put("p_kpi_string", p_kpi_string);

        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.printf("\ngetPersonRosters BEGIN PROCEDURE CALL: %s========================> ", LocalTime.now());
        // SqlParameterSource sqlParameterSource = new
        // MapSqlParameterSource(procParamMap);
        // Map<String, Object> simpleJdbcCallResult =
        // simpleJdbcCall.execute(sqlParameterSource);
        System.out.printf("p_kpi_string:%s\n", p_kpi_string);

        System.out.printf("\ngetPersonRosters END PROCEDURE CALL: %s========================> ", LocalTime.now());

        System.out.printf("\ngetPersonRosters BEGIN gathering data: %s========================> ", LocalTime.now());
        List<PersonRosterPivotResponse> pivotResponseList = jdbcClient.sql(ServiceUtils.personRosterPivotSql)
                .param("userId", userId)
                // param("id", id)
                .query(PersonRosterPivotResponse.class)
                .list();

        System.out.printf("\ngetPersonRosters END gathering data: %s========================> ", LocalTime.now());

        return new ArrayList<>(pivotResponseList);

    }

    public List<PersonRostersSmall> getPersonRostersSmall(Long userId, Long profileId, Date startDate, Date endDate,
                                                          JdbcClient jdbcClient) {

        // ServiceUtils serviceUtils = new ServiceUtils();

        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // System.out.println("Inside getPersonRosters ========================> ");
        Map<String, Object> objectMap = new HashMap<>();

        objectMap.put("userId", userId);
        objectMap.put("profileId", profileId);
        objectMap.put("startDate", startDate);
        objectMap.put("endDate", endDate);
        // objectMap.put("startDate", sdf.format(startDate));
        // objectMap.put("endDate", sdf.format(endDate));
        // List<PersonRosters> personRosters = new ArrayList<>();

        System.out.printf("\nInside getPersonRosters objectMap:%s\n", objectMap);

        List<PersonRostersSmall> personRostersList = jdbcClient.sql(ServiceUtils.getPersonRosterDataSqlSmall)
                .params(objectMap)
                .query(PersonRostersSmall.class)
                .list();

        // System.out.println("Completed getPersonRosters ========================> ");

        return personRostersList;

    }

    public RosterDMLResponseDto createSpotRoster(long userId, SpotRequestBody requestBody) {

        // System.out.println("createSpotRoster :requestBody:" + requestBody);
        List<PersonDtoSelected> personDtoSelected = requestBody.personDtoSelected();

        RosterDMLResponseDto responseDto = new RosterDMLResponseDto();
        String errorMessage = "";
        int recCounts = 0;

        try {

            for (PersonDtoSelected selected : personDtoSelected) {

                for (WorkDurationDtoAssignment workDurationDtoAssignment : requestBody.workDurationDtoAssignment()) {
                    // List<WorkDurationDtoAssignment> workDurationDtoAssignment =
                    // requestBody.getWorkDurationDtoAssignment();

                    Map<String, Object> inParamMap = new HashMap<>();
                    inParamMap.put("p_creator_user_id", userId);
                    inParamMap.put("p_start_date", requestBody.startDate());
                    inParamMap.put("p_end_date", requestBody.endDate());
                    inParamMap.put("p_person_id", selected.personId());
                    inParamMap.put("p_assignment_id", selected.assignmentId());
                    inParamMap.put("p_job_title_id", selected.jobTitleId());
                    inParamMap.put("p_department_id", selected.departmentId());
                    inParamMap.put("p_work_location_id", selected.locationId());
                    inParamMap.put("p_work_duration_id", workDurationDtoAssignment.workDurationId());
                    inParamMap.put("p_on_call", selected.onCallType());
                    inParamMap.put("p_emergency", selected.emergencyType());
                    inParamMap.put("p_sun", workDurationDtoAssignment.sun());
                    inParamMap.put("p_mon", workDurationDtoAssignment.mon());
                    inParamMap.put("p_tue", workDurationDtoAssignment.tue());
                    inParamMap.put("p_wed", workDurationDtoAssignment.wed());
                    inParamMap.put("p_thu", workDurationDtoAssignment.thu());
                    inParamMap.put("p_fri", workDurationDtoAssignment.fri());
                    inParamMap.put("p_sat", workDurationDtoAssignment.sat());
                    inParamMap.put("p_person_roster_id", requestBody.personRosterId());

                    SqlParameterSource inSource = new MapSqlParameterSource(inParamMap);
                    // System.out.println("createSpotRoster requestBody.personRosterId()" +
                    // requestBody.personRosterId());
                    // System.out.println("createSpotRoster inSource" + inSource);
                    // simpleJdbcCall = new
                    // SimpleJdbcCall(jdbcTemplate).withProcedureName("SC_DELETE_PERSON_ROSTERS_P");
                    simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("SC_CREATE_SPOT_ROSTER_P");
                    Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inSource);

                    AtomicReference<Object> sMessage = new AtomicReference<>();

                    simpleJdbcCallResult.forEach((s, o) -> {
                        // System.out.println(s);
                        // System.out.println(o);

                        if (s.equals("P_OUT")) {
                            String strMessage = o.toString();
                            // System.out.println("strMessage:" + strMessage);
                            sMessage.set(o);
                        }
                    });

                    // System.out.println("sMessage.get():" + sMessage.get());
                    String messageString = sMessage.get().toString();

                    String flag = messageString.substring(0, 1);
                    // System.out.println("flag:" + flag);
                    if (flag.equals("E")) {
                        errorMessage = messageString.substring(2);
                        break;
                    } else {
                        recCounts = recCounts + Integer.parseInt(messageString.substring(2));
                        // System.out.println("recCounts:" + recCounts);
                    }

                }

                if (!errorMessage.isEmpty()) {
                    break;
                }

            }

            if (!errorMessage.isEmpty()) {
                responseDto.setStatusMessage("E");
                responseDto.setDetailMessage(errorMessage);
            } else {
                responseDto.setStatusMessage("S");
                responseDto.setDetailMessage(String.valueOf(recCounts) + " schedule(s) created successfully!");
            }
            // System.out.println("responseDto:" + responseDto);

            return responseDto;

        } catch (Exception exception) {
            // System.out.println("error:" + exception.getMessage());
            responseDto.setStatusMessage("E");
            responseDto.setDetailMessage(exception.getMessage());
        }

        return null;

    }

    public List<PersonRosters> getSinglePersonRosters(Long userId, Long personId, Long personRosterId, Date startDate,
                                                      Date endDate, JdbcClient jdbcClient) {
        Map<String, Object> objectMap = new HashMap<>();

        objectMap.put("personId", personId);
        objectMap.put("personRosterId", personRosterId);
        objectMap.put("startDate", startDate);
        objectMap.put("endDate", endDate);

        // System.out.println("getSinglePersonRosters objectMap:" + objectMap);

        List<PersonRosters> personRostersList = jdbcClient.sql(getSinglePersonRosterDataSql).params(objectMap)
                .query(PersonRosters.class).list();

        // System.out.println("RESULTS personRostersList: ");

        for (PersonRosters personRosters : personRostersList) {
            // System.out.println("personRosters.personRosterId()" +
            // personRosters.personRosterId());
        }

        // jdbcClient.sql(ServiceUtils.getPersonRosterDataSqlSmall)
        // .params(objectMap)
        // .query(PersonRostersSmall.class)
        // .list();
        // List<PersonRosters> personRosters =
        // namedParameterJdbcTemplate.query(getSinglePersonRosterDataSql, objectMap,
        // personRostersRowMapper);

        return personRostersList;

    }

    public RosterDMLResponseDto deletePersonRoster(Long userId, RosterDeleteReasonReqBody reqBody,
                                                   JdbcClient jdbcClient) {

        RosterDMLResponseDto responseDto = new RosterDMLResponseDto();
        AtomicReference<String> errorMessage = new AtomicReference<>("");
        AtomicInteger deleteCounts = new AtomicInteger();

        // System.out.println("deletePersonRoster: reqBody:" + reqBody);

        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("SC_DELETE_PERSON_ROSTERS_P");

        Map<String, Object> inParamMap = new HashMap<>();

        // System.out.println("deletePersonRoster: reqBody.personIds:" +
        // reqBody.personIds());

        inParamMap.put("p_user_id", userId);
        inParamMap.put("p_person_ids", reqBody.personIds());
        inParamMap.put("p_deselect_person_ids", reqBody.deSelectPersonIds());
        inParamMap.put("p_start_date", reqBody.startDate());
        inParamMap.put("p_end_date", reqBody.endDate());
        inParamMap.put("p_person_roster_id", reqBody.personRosterId());
        inParamMap.put("p_profile_id", reqBody.profileId());
        inParamMap.put("p_filter_flag", reqBody.filterFlag());
        inParamMap.put("p_delete_reason_id", reqBody.deleteReasonId());
        inParamMap.put("p_remarks", reqBody.deleteComments());
        inParamMap.put("p_delete_type", reqBody.deleteType());
        inParamMap.put("p_group_key", reqBody.groupKey());

        SqlParameterSource inSource = new MapSqlParameterSource(inParamMap);
        // System.out.println(inSource);
        Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inSource);

        AtomicReference<Object> sMessage = new AtomicReference<>();

        simpleJdbcCallResult.forEach((s, o) -> {
            // System.out.println(s);
            // System.out.println(o);

            if (s.equals("P_OUT")) {
                String strMessage = o.toString();
                // System.out.println("strMessage:" + strMessage);
                sMessage.set(o);
            }
        });

        // System.out.println("sMessage.get():" + sMessage.get());
        String messageString = sMessage.get().toString();

        String flag = messageString.substring(0, 1);
        // System.out.println("flag:" + flag);
        if (flag.equals("E")) {
            errorMessage.set(messageString.substring(2));
        } else {
            // deleteCounts.set(Integer.parseInt(messageString.substring(2)));
            deleteCounts.addAndGet(Integer.parseInt(messageString.substring(2)));
            // System.out.println("deleteCounts:" + deleteCounts);
        }

        if (!errorMessage.get().isEmpty()) {
            responseDto.setStatusMessage("E");
            responseDto.setDetailMessage(errorMessage.get());
        } else {
            responseDto.setStatusMessage("S");
            responseDto.setDetailMessage(String.valueOf(deleteCounts.get()) + " schedule(s) deleted successfully!");
        }

        inParamMap.clear();

        return responseDto;

    }

    public RosterDMLResponseDto copyPersonRoster(Long userId, RosterCopyReqBody reqBody, JdbcClient jdbcClient) {

        RosterDMLResponseDto responseDto = new RosterDMLResponseDto();
        AtomicReference<String> errorMessage = new AtomicReference<>("");
        AtomicInteger transCounts = new AtomicInteger();

        // System.out.println("copyPersonRoster: reqBody:" + reqBody);

        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("SC_COPY_ROSTERS_P");

        Map<String, Object> inParamMap = new HashMap<>();
        inParamMap.put("p_user_id", userId);
        inParamMap.put("p_from_person_id", reqBody.fromPersonId());
        inParamMap.put("p_person_ids", reqBody.personIds());
        inParamMap.put("p_start_date", reqBody.startDate());
        inParamMap.put("p_end_date", reqBody.endDate());
        inParamMap.put("p_filter_flag", reqBody.filterFlag());
        inParamMap.put("p_copy_type", reqBody.copyType());
        inParamMap.put("p_group_key", reqBody.groupKey());

        SqlParameterSource inSource = new MapSqlParameterSource(inParamMap);
        // System.out.println(inSource);
        Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inSource);

        AtomicReference<Object> sMessage = new AtomicReference<>();

        simpleJdbcCallResult.forEach((s, o) -> {
            // System.out.println(s);
            // System.out.println(o);

            if (s.equals("P_OUT")) {
                String strMessage = o.toString();
                // System.out.println("strMessage:" + strMessage);
                sMessage.set(o);
            }
        });

        // System.out.println("sMessage.get():" + sMessage.get());
        String messageString = sMessage.get().toString();

        String flag = messageString.substring(0, 1);
        // System.out.println("flag:" + flag);
        if (flag.equals("E")) {
            errorMessage.set(messageString.substring(2));
        } else {
            // deleteCounts.set(Integer.parseInt(messageString.substring(2)));
            transCounts.addAndGet(Integer.parseInt(messageString.substring(2)));
            // System.out.println("transCounts:" + transCounts);
        }

        if (!errorMessage.get().isEmpty()) {
            responseDto.setStatusMessage("E");
            responseDto.setDetailMessage(errorMessage.get());
        } else {
            responseDto.setStatusMessage("S");
            responseDto.setDetailMessage(String.valueOf(transCounts.get()) + " schedule(s) created successfully!");
        }

        inParamMap.clear();

        return responseDto;

    }

    public RosterDMLResponseDto createRota(Long userId, List<RotaCreationReqBody> reqBody,
                                           JdbcClient jdbcClient) {

        RosterDMLResponseDto responseDto = new RosterDMLResponseDto();

        // System.out.println("createRota: reqBody:" + reqBody);
        AtomicInteger recCounts = new AtomicInteger(0);

        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("SC_GENERATE_ROTA_SHIFTS_P");

        // If reqBody.persons() is a comma-separated String, split and convert to
        // List<Long>
        // List<Long> persons = Arrays.stream(reqBody.persons().split(","))
        // .map(String::trim)
        // .filter(s -> !s.isEmpty())
        // .map(Long::valueOf)
        // .collect(Collectors.toList());

        reqBody.forEach(reqBodyItem -> {

            try {
                // System.out.println("createRota: reqBodyItem:" + reqBodyItem);

                Map<String, Object> inParamMap = new HashMap<>();

                inParamMap.put("p_user_id", userId);
                inParamMap.put("p_work_rotation_id", reqBodyItem.workRotationId());
                inParamMap.put("p_rota_start_date", reqBodyItem.rotaStartDate());
                inParamMap.put("p_rota_end_date", reqBodyItem.rotaEndDate());
                inParamMap.put("p_line_seq", reqBodyItem.lineSeq());
                inParamMap.put("p_department_id", reqBodyItem.departmentId());
                inParamMap.put("p_job_title_id", reqBodyItem.jobTitleId());
                inParamMap.put("p_work_location_id", reqBodyItem.workLocationId());
                inParamMap.put("p_mode", reqBodyItem.mode());
                inParamMap.put("p_persons", reqBodyItem.persons() == null ? "" : reqBodyItem.persons());

                SqlParameterSource inSource = new MapSqlParameterSource(inParamMap);
                // System.out.println(inSource);
                Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inSource);

                simpleJdbcCallResult.forEach((s, o) -> {
                    // System.out.println(s);
                    // System.out.println(o);

                    if (s.equals("P_OUT")) {
                        String strMessage = o.toString();
                        // System.out.println("strMessage:" + strMessage);
                        if (strMessage.startsWith("E")) {
                            responseDto.setStatusMessage("E");
                            responseDto.setDetailMessage(strMessage.substring(2));
                        } else {
                            recCounts.addAndGet(Integer.parseInt(strMessage.substring(2)));
                        }
                    }
                });

                inParamMap.clear();

                recCounts.incrementAndGet();

            } catch (Exception e) {
                // System.out.println("Exception in createRota: " + e.getMessage());
            }

        });

        if (recCounts.get() == 0) {
            responseDto.setStatusMessage("E");
            responseDto.setDetailMessage("No records created!");
            return responseDto;
        }

        responseDto.setStatusMessage("S");
        responseDto.setDetailMessage(
                "Rotation plan for " + String.valueOf(recCounts.get()) + " staff created successfully!");

        return responseDto;

    }

    public RosterDMLResponseDto createPersonRota(Long userId, List<PersonRotationAssocReqBody> assocReqBodies,
                                                 JdbcClient jdbcClient) {

        int dmlCounts = 0;
        int errCounts = 0;
        RosterDMLResponseDto responseDto = new RosterDMLResponseDto();

        Map<String, Object> inParamMap = new HashMap<>();
        Map<String, Object> inProcParamMap = new HashMap<>();
        AtomicInteger recCounts = new AtomicInteger(0);

        for (PersonRotationAssocReqBody assocReqBody : assocReqBodies) {

            try {

                PersonRotationPlanResp rotationPlanResp = jdbcClient.sql(sqlPersonRotationPlans)
                        .param("personId", assocReqBody.personId())
                        .param("startDate", assocReqBody.startDate())
                        .query(PersonRotationPlanResp.class)
                        .optional()
                        .orElse(null);

                if (rotationPlanResp != null && rotationPlanResp.fullName() != null
                        && rotationPlanResp.workRotationName() != null) {
                    responseDto.setStatusMessage("E");
                    responseDto.setDetailMessage(rotationPlanResp.fullName() + " already assigned to "
                            + rotationPlanResp.workRotationName() + " plan during this period");
                    return responseDto;
                }

                inParamMap.put("personId", assocReqBody.personId());
                inParamMap.put("workRotationId", assocReqBody.workRotationId());
                inParamMap.put("startDate", assocReqBody.startDate());
                inParamMap.put("endDate", assocReqBody.endDate());
                inParamMap.put("startSeq", assocReqBody.startSeq());
                inParamMap.put("createdBy", userId);
                inParamMap.put("lastUpdatedBy", userId);

                int updated = jdbcClient.sql(InsertPersonRorationAssoc).params(inParamMap).update();

                dmlCounts = dmlCounts + updated;

                inParamMap.clear();

                // calling procedure to create rotation plan based schedules..
                simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("sc_process_rota_p");

                inProcParamMap.put("p_person_id", assocReqBody.personId());
                inProcParamMap.put("p_work_rotation_id", assocReqBody.workRotationId());

                SqlParameterSource inSource = new MapSqlParameterSource(inProcParamMap);
                // System.out.println(inSource);
                Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inSource);

                simpleJdbcCallResult.forEach((s, o) -> {
                    // System.out.println(s);
                    // System.out.println(o);

                    if (s.equals("P_OUT")) {
                        String strMessage = o.toString();
                        // System.out.println("strMessage:" + strMessage);
                        if (strMessage.startsWith("E")) {
                            responseDto.setStatusMessage("E");
                            responseDto.setDetailMessage(strMessage.substring(2));
                        } else {
                            recCounts.addAndGet(Integer.parseInt(strMessage.substring(2)));
                        }
                    }
                });

            } catch (Exception e) {
                errCounts = errCounts + 1;
                // System.out.println("createPersonRota exception , error:" + e.getMessage());
                responseDto.setStatusMessage("E");
                responseDto.setDetailMessage(e.getMessage());
                return responseDto;
            } finally {
                inParamMap.clear();
                inProcParamMap.clear();
            }

        }

        // responseDto.setStatusMessage("S");
        // responseDto.setDetailMessage(String.valueOf(dmlCounts) + " rotations created
        // successfully, " + String.valueOf(errCounts) + " went in error");
        // return responseDto;
        if (recCounts.get() == 0) {
            responseDto.setStatusMessage("E");
            responseDto.setDetailMessage("No records created!");
            return responseDto;
        }

        responseDto.setStatusMessage("S");
        // responseDto.setDetailMessage(
        // "Rotation plan for " + String.valueOf(recCounts.get()) + " staff created
        // successfully!");
        responseDto.setDetailMessage(
                "Rotation plan created successfully!");

        return responseDto;

    }

    public RosterDMLResponseDto rosterActions(Long userId, RosterActionsReqBody reqBody, JdbcClient jdbcClient) {

        int dmlCounts = 0;
        int errCounts = 0;
        RosterDMLResponseDto responseDto = new RosterDMLResponseDto();

        Map<String, Object> inProcParamMap = new HashMap<>();
        AtomicInteger recCounts = new AtomicInteger(0);

        try {

            // calling procedure to create rotation plan based schedules..
            simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withCatalogName("SC_MANAGE_ROSTER_PKG")
                    .withProcedureName("sc_action_rosters_p");

            inProcParamMap.put("p_profile_id", reqBody.profileId());
            inProcParamMap.put("p_user_id", userId);
            inProcParamMap.put("p_start_date", reqBody.startDate());
            inProcParamMap.put("p_end_date", reqBody.endDate());
            inProcParamMap.put("p_persons", reqBody.persons());
            inProcParamMap.put("p_action", reqBody.action());
            inProcParamMap.put("p_comments", reqBody.comments());

            SqlParameterSource inSource = new MapSqlParameterSource(inProcParamMap);
            // System.out.println("rosterActions inSource" + inSource);
            Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inSource);
            // System.out.println("rosterActions simpleJdbcCallResult: " +
            // simpleJdbcCallResult);

            String actionMessage = switch (reqBody.action()) {
                case "INIT" -> "Selected schedules are submitted for approval";
                case "APPROVED" -> "Selected schedules are approved";
                case "RMI" -> "Selected schedules are returned for more information";
                case "SUBMIT" -> "Selected schedules are submitted";
                case "WITHD" -> "Selected schedules are withdrawn";
                default -> "Action completed";
            };

            responseDto.setStatusMessage("S");
            responseDto.setDetailMessage(actionMessage);

            return responseDto;

        } catch (Exception e) {
            errCounts = errCounts + 1;
            // System.out.println("rosterActions exception , error:" + e.getMessage());
            responseDto.setStatusMessage("E");
            responseDto.setDetailMessage(e.getMessage());
            return responseDto;
        } finally {
            inProcParamMap.clear();
        }

        // responseDto.setStatusMessage("S");
        // responseDto.setDetailMessage(String.valueOf(dmlCounts) + " rotations created
        // successfully, " + String.valueOf(errCounts) + " went in error");
        // return responseDto;

    }

    public DemandAllocationRespBody getDemandAllocations(Long userId, DemandAllocationReqBody reqBody,
                                                         JdbcClient jdbcClient) {

        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("sc_generate_demand_rosters_p");
        Map<String, Object> inProcParamMap = new HashMap<>();

        inProcParamMap.put("p_user_id", userId);
        inProcParamMap.put("p_start_date", reqBody.startDate());
        inProcParamMap.put("p_end_date", reqBody.endDate());
        inProcParamMap.put("p_demand_template_id", reqBody.demandTemplateId());

        SqlParameterSource inSource = new MapSqlParameterSource(inProcParamMap);
        // System.out.println("getDemandAllocations inSource" + inSource);
        Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inSource);
        // System.out.println("getDemandAllocations simpleJdbcCallResult: " +
        // simpleJdbcCallResult);

        // System.out.println("getDemandAllocations procedure completed
        // ================");

        DemandAllocationRespBody respBody = new DemandAllocationRespBody();

        List<DemandAllocationSummary> summaryList = jdbcClient.sql(sqlDemandAllocationSummary)
                .param("userId", userId)
                .query(DemandAllocationSummary.class)
                .list();

        for (DemandAllocationSummary summary : summaryList) {

            // System.out.println("getDemandAllocations summary:" + summary);

            List<DemandAllocationLines> lines = jdbcClient.sql(sqlDemandAllocationLines)
                    .param("userId", userId)
                    .param("demandTemplateLineId", summary.getDemandTemplateLineId())
                    .param("effectiveDate", summary.getEffectiveDate())
                    .query(DemandAllocationLines.class)
                    .list();
            // System.out.println("getDemandAllocations lines:" + lines);

            summary.setDemandAllocationLines(lines);

        }

        respBody.setAllocationSummaryList(summaryList);
        return respBody;

    }

    public DemandAllocationRespBody getDemandAllocationsNew(Long userId, DemandAllocationReqBody reqBody,
                                                            JdbcClient jdbcClient) {
        logger.info("getDemandAllocationsNew - Entry - Time: {}, Request: {}", LocalDateTime.now(), reqBody);
        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("sp_generate_staff_schedule");
        Map<String, Object> inProcParamMap = new HashMap<>();

        inProcParamMap.put("p_demand_template_id", reqBody.demandTemplateId());
        inProcParamMap.put("p_user_id", userId);
        inProcParamMap.put("p_start_date", reqBody.startDate());
        inProcParamMap.put("p_end_date", reqBody.endDate());

        SqlParameterSource inSource = new MapSqlParameterSource(inProcParamMap);
        // System.out.println("getDemandAllocations inSource" + inSource);
        Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inSource);

        // Extract OUT parameters
        Number suggestionId = (Number) simpleJdbcCallResult.get("p_suggestion_id");
        String status = (String) simpleJdbcCallResult.get("p_status");
        String message = (String) simpleJdbcCallResult.get("p_message");

        logger.info(
                "getDemandAllocationsNew - PROCEDURE CALL FINISH - Time: {}, suggestionId: {} , status:{}, message:{} ",
                LocalDateTime.now(), suggestionId, status, message);

        DemandAllocationRespBody respBody = new DemandAllocationRespBody();

        List<DemandAllocationSummary> summaryList = jdbcClient.sql(sqlDemandAllocationSummaryNEW)
                .param("demandTemplateId", reqBody.demandTemplateId())
                .param("suggestionId", suggestionId)
                .query(DemandAllocationSummary.class)
                .list();

        for (DemandAllocationSummary summary : summaryList) {

            // System.out.println("getDemandAllocations summary:" + summary);

            List<DemandAllocationLines> lines = jdbcClient.sql(sqlDemandAllocationLinesNEW)
                    .param("demandTemplateId", reqBody.demandTemplateId())
                    .param("demandTemplateLineId", summary.getDemandTemplateLineId())
                    .param("suggestionId", suggestionId)
                    .param("effectiveDate", summary.getEffectiveDate())
                    .query(DemandAllocationLines.class)
                    .list();
            // System.out.println("getDemandAllocations lines:" + lines);

            summary.setDemandAllocationLines(lines);

        }

        respBody.setAllocationSummaryList(summaryList);
        return respBody;

    }

    public ValidateRosterResponse getValidateRosterResponse(Long userId, ValidateRosterReqBody reqBody,
                                                            JdbcClient jdbcClient) {
        Map<String, Object> objectMap = new HashMap<>();

        objectMap.put("userId", userId);
        objectMap.put("demandTemplateId", reqBody.demandTemplateId());

        List<DemandLineResponse> demandLineResponses = jdbcClient.sql(sqlDemandLinesValidate)
                .params(objectMap)
                .query(DemandLineResponse.class)
                .list();

        // System.out.println("getValidateRosterResponse demandLineResponses:" +
        // demandLineResponses);

        objectMap.clear();

        objectMap.put("startDate", reqBody.startDate());
        objectMap.put("endDate", reqBody.endDate());
        objectMap.put("userId", userId);
        objectMap.put("profileId", reqBody.profileId());

        List<ScheduleLineResponse> scheduleLineResponses = jdbcClient.sql(sqlScheduledValidate)
                .params(objectMap)
                .query(ScheduleLineResponse.class)
                .list();

        // System.out.println("getValidateRosterResponse scheduleLineResponses:" +
        // scheduleLineResponses);

        ValidateRosterResponse response = new ValidateRosterResponse(demandLineResponses, scheduleLineResponses);
        objectMap.clear();

        return response;

    }

    public RosterDMLResponseDto dragDropPersonRoster(Long userId, DragDropReqBody reqBody, JdbcClient jdbcClient) {

        RosterDMLResponseDto responseDto = new RosterDMLResponseDto();
        AtomicReference<String> errorMessage = new AtomicReference<>("");
        AtomicInteger transCounts = new AtomicInteger();

        // System.out.println("dragDropPersonRoster: reqBody:" + reqBody);

        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("SC_DRAG_DROP_ROSTERS_P");

        Map<String, Object> inParamMap = new HashMap<>();
        inParamMap.put("p_user_id", userId);
        inParamMap.put("p_person_roster_id", reqBody.personRosterId());
        inParamMap.put("p_person_id", reqBody.personId());
        inParamMap.put("p_effective_date", reqBody.effectiveDate());
//        inParamMap.put("p_mode", "DRAG");
        inParamMap.put("p_mode", reqBody.mode());

        SqlParameterSource inSource = new MapSqlParameterSource(inParamMap);
        // System.out.println(inSource);
        Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inSource);

        AtomicReference<Object> sMessage = new AtomicReference<>();

        simpleJdbcCallResult.forEach((s, o) -> {
            // System.out.println(s);
            // System.out.println(o);

            if (s.equals("P_OUT")) {
                String strMessage = o.toString();
                // System.out.println("strMessage:" + strMessage);
                sMessage.set(o);
            }
        });

        // System.out.println("sMessage.get():" + sMessage.get());
        String messageString = sMessage.get().toString();

        String flag = messageString.substring(0, 1);
        // System.out.println("flag:" + flag);
        if (flag.equals("E")) {
            errorMessage.set(messageString.substring(2));
        } else {
            // deleteCounts.set(Integer.parseInt(messageString.substring(2)));
            transCounts.addAndGet(Integer.parseInt(messageString.substring(2)));
            // System.out.println("transCounts:" + transCounts);
        }

        if (!errorMessage.get().isEmpty()) {
            responseDto.setStatusMessage("E");
            responseDto.setDetailMessage(errorMessage.get());
        } else {
            responseDto.setStatusMessage("S");
            if (transCounts.get() == 0) {
                responseDto.setDetailMessage("No schedule replaced.");
            } else {
                responseDto.setDetailMessage(String.valueOf(transCounts.get()) + " schedule replaced successfully!");
            }

        }

        inParamMap.clear();

        return responseDto;

    }

    public RosterDMLResponseDto quickCopyPersonRoster(Long userId, QuickCopyReqBody reqBody, JdbcClient jdbcClient) {

        RosterDMLResponseDto responseDto = new RosterDMLResponseDto();
        AtomicReference<String> errorMessage = new AtomicReference<>("");
        AtomicInteger transCounts = new AtomicInteger();

        // System.out.println("quickCopyPersonRoster: reqBody:" + reqBody);

        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("SC_QUICK_COPY_ROSTERS_P");
        Map<String, Object> inParamMap = new HashMap<>();

        List<QuickCopyPersonDateReqBody> collepersonDateReqBodiesct = reqBody.quickCopyPersonDateReqBodies().stream()
                .collect(Collectors.toList());

        for (QuickCopyPersonDateReqBody personDate : collepersonDateReqBodiesct) {
            inParamMap.put("p_user_id", userId);
            inParamMap.put("p_person_roster_id", reqBody.personRosterId());
            inParamMap.put("p_person_id", personDate.personId());
            inParamMap.put("p_effective_date", personDate.effectiveDate());

            SqlParameterSource inSource = new MapSqlParameterSource(inParamMap);
            // System.out.println(inSource);
            Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inSource);

            AtomicReference<Object> sMessage = new AtomicReference<>();

            simpleJdbcCallResult.forEach((s, o) -> {
                // System.out.println(s);
                // System.out.println(o);

                if (s.equals("P_OUT")) {
                    String strMessage = o.toString();
                    // System.out.println("strMessage:" + strMessage);
                    sMessage.set(o);
                }
            });

            // System.out.println("sMessage.get():" + sMessage.get());
            String messageString = sMessage.get().toString();

            String flag = messageString.substring(0, 1);
            // System.out.println("flag:" + flag);
            if (flag.equals("E")) {
                errorMessage.set(messageString.substring(2));
            } else {
                // deleteCounts.set(Integer.parseInt(messageString.substring(2)));
                transCounts.addAndGet(Integer.parseInt(messageString.substring(2)));
                // System.out.println("transCounts:" + transCounts);
            }

            if (!errorMessage.get().isEmpty()) {
                responseDto.setStatusMessage("E");
                responseDto.setDetailMessage(errorMessage.get());
                return responseDto;
            }

            inParamMap.clear();
        }

        responseDto.setStatusMessage("S");
        if (transCounts.get() == 0) {
            responseDto.setDetailMessage("No schedule copied.");
        } else {
            responseDto.setDetailMessage(String.valueOf(transCounts.get()) + " schedule copied successfully!");
        }

        return responseDto;

    }


    public List<AlternatePersonDto> getAlternatePersonList(Long userId,
                                                           AlternateStaffReqBody requestBody,
                                                           JdbcClient jdbcClient) {

        List<AlternatePersonDto> personDtoList = jdbcClient.sql(reAssignmentSQL)
                .param("userId", userId)
                .param("profileId", requestBody.profileId())
                .param("startDate", requestBody.startDate())
                .param("endDate", requestBody.endDate())
                .param("personId", requestBody.personId())
                .param("personRosterId", requestBody.personRosterId())
                .query(AlternatePersonDto.class)
                .list();

        return personDtoList;
    }
}
