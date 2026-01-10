package com.ewsv3.ews.openShifts.service;

import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.openShifts.dto.*;
import com.ewsv3.ews.openShifts.dto.allocation.*;
import com.ewsv3.ews.rosters.controller.RosterController;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.ewsv3.ews.openShifts.service.OpenShiftUtils.*;

@Service
public class OpenShiftService {

    private SimpleJdbcCall simpleJdbcCall;
    private final JdbcTemplate jdbcTemplate;

    private static final Logger logger = LoggerFactory.getLogger(RosterController.class);

    public OpenShiftService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void init() {
        jdbcTemplate.setResultsMapCaseInsensitive(true);
        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("SC_PERSON_OPEN_SHIFT_BIDS_P");
    }

    public OpenShiftsHeader getOpenShifts(Long openShiftId, JdbcClient jdbcClient) {
        logger.info("getOpenShifts - Entry - Time: {}, openShiftId: {}", LocalDateTime.now(), openShiftId);
        OpenShiftsHeader shiftsHeader = jdbcClient.sql(GetOpenShiftsByOpenShiftId)
                .param("openShiftId", openShiftId)
                .query(OpenShiftsHeader.class)
                .single();

        List<OpenShiftLines> shiftLinesList = jdbcClient.sql(GetOpenShiftLines)
                .param("openShiftId", openShiftId)
                .query(OpenShiftLines.class)
                .list();

        shiftsHeader.setOpenShiftLines(shiftLinesList);

        return shiftsHeader;


    }

    public DMLResponseDto createOpenShifts(Long userId, OpenShiftsHeader reqBody, JdbcClient jdbcClient) {

        Long generatedOpenShiftId = jdbcClient
                .sql("SELECT OPEN_SHIFT_ID_SQ.NEXTVAL FROM dual")
                .query(Long.class)
                .single();


        int createdCount = jdbcClient.sql(CreateOpenShiftHeader)
                .param("openShiftId", generatedOpenShiftId)
                .param("startDate", reqBody.getStartDate())
                .param("endDate", reqBody.getEndDate())
                .param("demandTemplateId", reqBody.getDemandTemplateId())
                .param("profileId", reqBody.getProfileId())
                .param("recalled", reqBody.getRecalled())
                .param("createdBy", userId)
                .param("createdOn", new Date())
                .param("lastUpdatedBy", userId)
                .param("lastUpdateDate", new Date())
                .update();

        if (createdCount == 1) {

            for (OpenShiftLines openShiftLine : reqBody.getOpenShiftLines()) {
                int createdLioneCount = jdbcClient.sql(CreateOpenShiftLine)
                        .param("openShiftId", generatedOpenShiftId)
                        .param("demandTemplateLineId", openShiftLine.getDemandTemplateLineId())
                        .param("departmentId", openShiftLine.getDepartmentId())
                        .param("jobTitleId", openShiftLine.getJobTitleId())
                        .param("locationId", openShiftLine.getLocationId())
                        .param("workDurationId", openShiftLine.getWorkDurationId())
                        .param("sun", openShiftLine.getSun())
                        .param("mon", openShiftLine.getMon())
                        .param("tue", openShiftLine.getTue())
                        .param("wed", openShiftLine.getWed())
                        .param("thu", openShiftLine.getThu())
                        .param("fri", openShiftLine.getFri())
                        .param("sat", openShiftLine.getSat())
                        .param("createdBy", userId)
                        .param("createdOn", new Date())
                        .param("lastUpdatedBy", userId)
                        .param("lastUpdateDate", new Date())
                        .update();
            }


        } else {
            return new DMLResponseDto("E", "No open-shifts are generated");
        }

        logger.info("createOpenShifts - Entry - Time: {}, generatedOpenShiftId: {}", LocalDateTime.now(), generatedOpenShiftId);
        OpenShiftsHeader shiftsHeader = getOpenShifts(generatedOpenShiftId, jdbcClient);

        populateOpenShiftDetails(userId, shiftsHeader, generatedOpenShiftId, jdbcClient);

        return new

                DMLResponseDto("S", "Open-shifts are generated successfully");

    }

    public DMLResponseDto updateOpenShifts(Long userId, OpenShiftsHeader reqBody, JdbcClient jdbcClient) {


        OpenShiftsHeader openShiftsHeader = jdbcClient.sql(GetOpenShiftsByOpenShiftId)
                .param("openShiftId", reqBody.getOpenShiftId())
                .query(OpenShiftsHeader.class)
                .single();

        int updatedHEaderCount = 0;

        if (!Objects.equals(openShiftsHeader.getRecalled(), reqBody.getRecalled())) {
            updatedHEaderCount = jdbcClient.sql(UpdateOpenShiftHeader)
                    .param("openShiftId", reqBody.getOpenShiftId())
                    .param("recalled", reqBody.getRecalled())
                    .param("lastUpdatedBy", userId)
                    .param("lastUpdateDate", new Date())
                    .update();
        }

        Long generatedOpenShiftLineId = 0L;
        int createdLineCount = 0;
        int updatedLineCount = 0;

        for (OpenShiftLines openShiftLine : reqBody.getOpenShiftLines()) {

            if (openShiftLine.getOpenShiftLineId() == null || openShiftLine.getOpenShiftLineId() == 0) {

                generatedOpenShiftLineId = jdbcClient
                        .sql("SELECT OPEN_SHIFT_ID_SQ.NEXTVAL FROM dual")
                        .query(Long.class)
                        .single();

                createdLineCount += jdbcClient.sql(CreateOpenShiftLine)
                        .param("openShiftId", reqBody.getOpenShiftId())
                        .param("demandTemplateLineId", openShiftLine.getDemandTemplateLineId())
                        .param("departmentId", openShiftLine.getDepartmentId())
                        .param("jobTitleId", openShiftLine.getJobTitleId())
                        .param("locationId", openShiftLine.getLocationId())
                        .param("workDurationId", openShiftLine.getWorkDurationId())
                        .param("sun", openShiftLine.getSun())
                        .param("mon", openShiftLine.getMon())
                        .param("tue", openShiftLine.getTue())
                        .param("wed", openShiftLine.getWed())
                        .param("thu", openShiftLine.getThu())
                        .param("fri", openShiftLine.getFri())
                        .param("sat", openShiftLine.getSat())
                        .param("createdBy", userId)
                        .param("createdOn", new Date())
                        .param("lastUpdatedBy", userId)
                        .param("lastUpdateDate", new Date())
                        .update();
            } else {
                generatedOpenShiftLineId = openShiftLine.getOpenShiftLineId();

                updatedLineCount += jdbcClient.sql(UpdateOpenShiftLine)
                        .param("openShiftLineId", generatedOpenShiftLineId)
                        .param("departmentId", openShiftLine.getDepartmentId())
                        .param("jobTitleId", openShiftLine.getJobTitleId())
                        .param("locationId", openShiftLine.getLocationId())
                        .param("workDurationId", openShiftLine.getWorkDurationId())
                        .param("sun", openShiftLine.getSun())
                        .param("mon", openShiftLine.getMon())
                        .param("tue", openShiftLine.getTue())
                        .param("wed", openShiftLine.getWed())
                        .param("thu", openShiftLine.getThu())
                        .param("fri", openShiftLine.getFri())
                        .param("sat", openShiftLine.getSat())
                        .param("lastUpdatedBy", userId)
                        .param("lastUpdateDate", new Date())
                        .update();

            }

        }


        if (updatedHEaderCount == 0 && createdLineCount == 0 && updatedLineCount == 0) {
            return new DMLResponseDto("E", "No open-shifts are updated");
        }

        OpenShiftsHeader shiftsHeader = getOpenShifts(reqBody.getOpenShiftId(), jdbcClient);

        populateOpenShiftDetails(userId, shiftsHeader, reqBody.getOpenShiftId(), jdbcClient);

        return new
                DMLResponseDto("S", "Open-shifts are updated successfully");

    }

    public void populateOpenShiftDetails(Long userId, OpenShiftsHeader reqBody, Long generatedOpenShiftId, JdbcClient jdbcClient) {

        LocalDate startDate = reqBody.getStartDate();
//        LocalDate endDate = reqBody.getEndDate();
        for (OpenShiftLines openShiftLine : reqBody.getOpenShiftLines()) {

            if (Objects.nonNull(openShiftLine.getSun()) && openShiftLine.getSun() > 0) {
                insertOpenShiftDetails(userId, openShiftLine, generatedOpenShiftId, startDate, openShiftLine.getSun(), jdbcClient);
            }

            if (Objects.nonNull(openShiftLine.getMon()) && openShiftLine.getMon() > 0) {
                insertOpenShiftDetails(userId, openShiftLine, generatedOpenShiftId, startDate.plusDays(1), openShiftLine.getMon(), jdbcClient);
            }

            if (Objects.nonNull(openShiftLine.getTue()) && openShiftLine.getTue() > 0) {
                insertOpenShiftDetails(userId, openShiftLine, generatedOpenShiftId, startDate.plusDays(2), openShiftLine.getTue(), jdbcClient);
            }

            if (Objects.nonNull(openShiftLine.getWed()) && openShiftLine.getWed() > 0) {
                insertOpenShiftDetails(userId, openShiftLine, generatedOpenShiftId, startDate.plusDays(3), openShiftLine.getWed(), jdbcClient);
            }

            if (Objects.nonNull(openShiftLine.getThu()) && openShiftLine.getThu() > 0) {
                insertOpenShiftDetails(userId, openShiftLine, generatedOpenShiftId, startDate.plusDays(4), openShiftLine.getThu(), jdbcClient);
            }

            if (Objects.nonNull(openShiftLine.getFri()) && openShiftLine.getFri() > 0) {
                insertOpenShiftDetails(userId, openShiftLine, generatedOpenShiftId, startDate.plusDays(5), openShiftLine.getFri(), jdbcClient);
            }

            if (Objects.nonNull(openShiftLine.getSat()) && openShiftLine.getSat() > 0) {
                insertOpenShiftDetails(userId, openShiftLine, generatedOpenShiftId, startDate.plusDays(6), openShiftLine.getSat(), jdbcClient);
            }
        }

    }

    public void insertOpenShiftDetails(Long userId, OpenShiftLines openShiftLine, Long generatedOpenShiftId, LocalDate effectiveDate, Long requestedFte, JdbcClient jdbcClient) {

        int insertedCounts = jdbcClient.sql(CreateOpenShiftDetail)
                .param("openShiftId", generatedOpenShiftId)
                .param("openShiftLineId", openShiftLine.getOpenShiftLineId())
                .param("departmentId", openShiftLine.getDepartmentId())
                .param("jobTitleId", openShiftLine.getJobTitleId())
                .param("locationId", openShiftLine.getLocationId())
                .param("workDurationId", openShiftLine.getWorkDurationId())
                .param("effectiveDate", effectiveDate)
                .param("fteRequested", requestedFte)
                .param("createdBy", userId)
                .param("createdOn", LocalDateTime.now())
                .param("lastUpdatedBy", userId)
                .param("lastUpdateDate", LocalDateTime.now())
                .update();

    }

    public Long getOpenshiftCountTk(Long userId, OpenShiftProfileDatesReqDto reqDto, JdbcClient jdbcClient) {
        Long openShiftounts = jdbcClient.sql(getOpenShiftsCountsTimekeeper)
                .param("profileId", reqDto.profileId())
                .param("startDate", reqDto.startDate())
                .param("endDate", reqDto.endDate())
                .query(Long.class)
                .optional()
                .orElse(0L);

        return openShiftounts;

    }

    public List<OpenShifListTkRespDto> getOpenshiftListTk(Long userId, OpenShiftProfileDatesReqDto reqDto, JdbcClient jdbcClient) {
        List<OpenShifListTkRespDto> openShifListTkRespDtos = jdbcClient.sql(getOpenShiftListTimekeeper)
                .param("profileId", reqDto.profileId())
                .param("startDate", reqDto.startDate())
                .param("endDate", reqDto.endDate())
                .query(OpenShifListTkRespDto.class)
                .list();

        return openShifListTkRespDtos;

    }

    public List<SuggestionPersonDto> getSuggestPersonList(Long userId, OpenShiftProfileDatesReqDto reqDto, JdbcClient jdbcClient) {

        OpenShiftLines shiftLines = jdbcClient.sql(GetOpenShiftLinesFromId)
                .param("openShiftLineId", reqDto.openShiftLineId())
                .query(OpenShiftLines.class)
                .optional()
                .orElse(null);

        List<SuggestionPersonDto> personDtoList = jdbcClient.sql(getOpenShiftSuggestionPerson)
                .param("userId", userId)
                .param("profileId", reqDto.profileId())
                .param("departmentId", shiftLines.getDepartmentId())
                .param("locationId", shiftLines.getLocationId())
                .param("jobTitleId", shiftLines.getJobTitleId())
                .param("startDate", reqDto.startDate())
                .param("endDate", reqDto.endDate())
                .query(SuggestionPersonDto.class)
                .list();

        List<Long> collectedPersonIds = personDtoList.stream().map(SuggestionPersonDto::getPersonId).collect(Collectors.toList());


        if (collectedPersonIds.isEmpty()) {
            return null;
        }


        List<SuggestionPersonRostersDto> rostersDtos = jdbcClient.sql(getOpenShiftSuggestionPersonRosters)
                .param("personId", collectedPersonIds)
                .param("startDate", reqDto.startDate())
                .param("endDate", reqDto.endDate())
                .query(SuggestionPersonRostersDto.class)
                .list();

        List<SuggestionPersonLeavesDto> leavesDtos = jdbcClient.sql(getOpenShiftSuggestionPersonLeaves)
                .param("personId", collectedPersonIds)
                .param("startDate", reqDto.startDate())
                .param("endDate", reqDto.endDate())
                .query(SuggestionPersonLeavesDto.class)
                .list();

        List<SuggestionPersonHolidaysDto> holidaysDtos = jdbcClient.sql(getOpenShiftSuggestionPersonHolidays)
                .param("personId", collectedPersonIds)
                .param("startDate", reqDto.startDate())
                .param("endDate", reqDto.endDate())
                .query(SuggestionPersonHolidaysDto.class)
                .list();

        List<PersonSelfApplicationsDto> personSelfApplicationsDtos = jdbcClient.sql(getSelfApplicationsSQL)
                .param("personId", collectedPersonIds)
                .param("openShiftLineId", reqDto.openShiftLineId())
                .query(PersonSelfApplicationsDto.class)
                .list();

        Map<Long, List<SuggestionPersonRostersDto>> rostersByPersonId =
                rostersDtos.stream()
                        .collect(Collectors.groupingBy(SuggestionPersonRostersDto::personId));

        Map<Long, List<SuggestionPersonLeavesDto>> leavesByPersonId =
                leavesDtos.stream()
                        .collect(Collectors.groupingBy(SuggestionPersonLeavesDto::personId));

        Map<Long, List<SuggestionPersonHolidaysDto>> holidaysByPersonId =
                holidaysDtos.stream()
                        .collect(Collectors.groupingBy(SuggestionPersonHolidaysDto::personId));

        Map<Long, List<PersonSelfApplicationsDto>> selfApplicationPersonId =
                personSelfApplicationsDtos.stream()
                        .collect(Collectors.groupingBy(PersonSelfApplicationsDto::personId));

        personDtoList.forEach(person -> {
            Long personId = person.getPersonId();

            person.setPersonRostersDtoList(
                    rostersByPersonId.getOrDefault(personId, List.of())
            );

            person.setPersonLeavesDtos(
                    leavesByPersonId.getOrDefault(personId, List.of())
            );

            person.setPersonHolidaysDtos(
                    holidaysByPersonId.getOrDefault(personId, List.of())
            );

            person.setPersonSelfApplicationsDtos(
                    selfApplicationPersonId.getOrDefault(personId, List.of())
            );
        });

        return personDtoList;

    }

    public List<OpenShifListTkRespDto> getEmployeeOpenShifts(Long userId, JdbcClient jdbcClient) {

        List<OpenShifListTkRespDto> openShifList = jdbcClient.sql(getEmployeeOpenShifts)
                .param("userId", userId)
                .query(OpenShifListTkRespDto.class)
                .list();
        return openShifList;

    }

    public DMLResponseDto bidOpenShift(Long userId, PersonOpenShiftBidReqDto reqDto, JdbcClient jdbcClient){


        DMLResponseDto dmlResponseDto= new DMLResponseDto();

        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("SC_PERSON_OPEN_SHIFT_BIDS_P");
        Map<String, Object> inParamMap = new HashMap<>();

        AtomicReference<String> errorMessage = new AtomicReference<>("");
        AtomicInteger transCounts = new AtomicInteger();


        inParamMap.put("p_person_open_shift_id", reqDto.personOpenShiftId());
        inParamMap.put("p_person_id", reqDto.personId());
        inParamMap.put("p_open_shift_line_id", reqDto.openShiftLineId());
        inParamMap.put("p_sun", reqDto.sun());
        inParamMap.put("p_mon", reqDto.mon());
        inParamMap.put("p_tue", reqDto.tue());
        inParamMap.put("p_wed", reqDto.wed());
        inParamMap.put("p_thu", reqDto.thu());
        inParamMap.put("p_fri", reqDto.fri());
        inParamMap.put("p_sat", reqDto.sat());
        inParamMap.put("p_user_id", userId);

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
            dmlResponseDto.setStatusMessage("E");
            dmlResponseDto.setDetailMessage(errorMessage.get());
            return dmlResponseDto;
        }

        dmlResponseDto.setStatusMessage("S");
        dmlResponseDto.setDetailMessage(messageString.substring(2));

        return dmlResponseDto;
    }

    public TotalApplictionCountsDto getTotalApplications(Long userId, PersonOpenShiftBidReqDto reqDto, JdbcClient jdbcClient){

        TotalApplictionCountsDto countsDto = jdbcClient.sql(getTotalApplicationCountsSQL)
                .param("openShiftLineId", reqDto.openShiftLineId())
                .query(TotalApplictionCountsDto.class)
                .optional()
                .orElse(null);

        return countsDto;

    }

    public ApprovedApplicationCountsDto getApprovedApplications(Long userId, PersonOpenShiftBidReqDto reqDto, JdbcClient jdbcClient){

        ApprovedApplicationCountsDto countsDto = jdbcClient.sql(getApprovedApplicationCountsSQL)
                .param("openShiftLineId", reqDto.openShiftLineId())
                .query(ApprovedApplicationCountsDto.class)
                .optional()
                .orElse(null);

        return countsDto;

    }

    public SelfApplicationRespSto getSelfApplications(Long userId, Long personId, PersonOpenShiftBidReqDto reqDto, JdbcClient jdbcClient){

        OpenShiftLines shiftLines = jdbcClient.sql(GetOpenShiftLinesFromId)
                .param("openShiftLineId", reqDto.openShiftLineId())
                .query(OpenShiftLines.class)
                .optional()
                .orElse(null);

        OpenShiftsHeader shiftsHeader = jdbcClient.sql(GetOpenShiftsByOpenShiftId)
                .param("openShiftId", shiftLines.getOpenShiftId())
                .query(OpenShiftsHeader.class)
                .single();

        PersonSelfApplicationsDto selfApplicationsDto = jdbcClient.sql(getSelfApplicationsSQL)
                .param("personId", personId)
                .param("openShiftLineId", reqDto.openShiftLineId())
                .query(PersonSelfApplicationsDto.class)
                .optional()
                .orElse(null);

        List<SuggestionPersonRostersDto> rostersDtos = jdbcClient.sql(getOpenShiftSuggestionPersonRosters)
                .param("personId", personId)
                .param("startDate", shiftsHeader.getStartDate())
                .param("endDate", shiftsHeader.getEndDate())
                .query(SuggestionPersonRostersDto.class)
                .list();

        List<SuggestionPersonLeavesDto> leavesDtos = jdbcClient.sql(getOpenShiftSuggestionPersonLeaves)
                .param("personId", personId)
                .param("startDate", shiftsHeader.getStartDate())
                .param("endDate", shiftsHeader.getEndDate())
                .query(SuggestionPersonLeavesDto.class)
                .list();

        List<SuggestionPersonHolidaysDto> holidaysDtos = jdbcClient.sql(getOpenShiftSuggestionPersonHolidays)
                .param("personId", personId)
                .param("startDate", shiftsHeader.getStartDate())
                .param("endDate", shiftsHeader.getEndDate())
                .query(SuggestionPersonHolidaysDto.class)
                .list();

        SelfApplicationRespSto selfApplicationRespSto= new SelfApplicationRespSto();
        selfApplicationRespSto.setPersonSelfApplicationsDto(selfApplicationsDto);
        selfApplicationRespSto.setPersonRostersDtoList(rostersDtos);
        selfApplicationRespSto.setPersonLeavesDtos(leavesDtos);
        selfApplicationRespSto.setPersonHolidaysDtos(holidaysDtos);

        return selfApplicationRespSto;

    }


}
