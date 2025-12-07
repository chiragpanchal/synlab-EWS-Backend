package com.ewsv3.ews.selfroster.service;

import static com.ewsv3.ews.rosters.service.utils.RosterSql.sqlScheduledValidate;
import static com.ewsv3.ews.timesheets.service.approval.TimesheetUtilsApproval.sqlApprovalTimesheetTableDataBulk;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.selfroster.dto.SelfRoster;
import com.ewsv3.ews.selfroster.dto.SelfRosterLine;
import com.ewsv3.ews.selfroster.dto.SelfRosterDto;
import static com.ewsv3.ews.selfroster.service.SelfRosterUtils.*;

import jakarta.annotation.PostConstruct;

@Service
public class SelfRosterService {

    private SimpleJdbcCall simpleJdbcCall;
    private final JdbcTemplate jdbcTemplate;

    public SelfRosterService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void init() {
        jdbcTemplate.setResultsMapCaseInsensitive(true);
        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("SC_SAVE_SELF_ROSTER_P");
    }

    public List<SelfRoster> getSelfRosters(Long userId, JdbcClient jdbcClient) {

        List<SelfRoster> selfRosterList = jdbcClient.sql(sqlGetSelfRosters)
                .param("userId", userId)
                .query(SelfRoster.class)
                .list();

        for (SelfRoster selfRoster : selfRosterList) {

            List<SelfRosterLine> selfRosterLines = jdbcClient.sql(sqlGetSelfRosterLines)
                    .param("selfRosterId", selfRoster.getSelfRosterId())
                    .query(SelfRosterLine.class)
                    .list();

            selfRoster.setSelfRosterLine(selfRosterLines);
        }

        return selfRosterList;

    }

    public DMLResponseDto saveSelfRoster(Long userId, SelfRoster reqDto, JdbcClient jdbcClient) {

        Long generatedId = 0L;
        String errorMessage = "";
        int recCounts = 0;

        if (reqDto.getSelfRosterId() != null) {

            generatedId = reqDto.getSelfRosterId();

        } else {
            generatedId = jdbcClient
                    .sql("SELECT SELF_ROSTER_ID_SQ.NEXTVAL FROM dual")
                    .query(Long.class)
                    .single();
        }

        Map<String, Object> inParamMap = new HashMap<>();
        inParamMap.put("p_created_by", userId);
        inParamMap.put("p_from_date", reqDto.getFromDate());
        inParamMap.put("p_last_updated_by", userId);
        inParamMap.put("p_department_id", reqDto.getDepartmentId());
        inParamMap.put("p_to_date", reqDto.getToDate());
        inParamMap.put("p_comments", reqDto.getComments());
        inParamMap.put("p_self_roster_id", generatedId);
        inParamMap.put("p_last_update_date", LocalTime.now());
        inParamMap.put("p_status", reqDto.getStatus());
        inParamMap.put("p_job_title_id", reqDto.getJobTitleId());
        inParamMap.put("p_person_id", reqDto.getPersonId());
        inParamMap.put("p_work_location_id", reqDto.getWorkLocationId());
        inParamMap.put("p_created_on", LocalTime.now());
        inParamMap.put("p_item_key", reqDto.getItemKey());

        SqlParameterSource inSource = new MapSqlParameterSource(inParamMap);
        //System.out.println(inSource);
        // simpleJdbcCall = new
        // SimpleJdbcCall(jdbcTemplate).withProcedureName("SC_DELETE_PERSON_ROSTERS_P");
        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("SC_SAVE_SELF_ROSTER_P");
        Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inSource);

        //System.out.println("saveSelfRoster simpleJdbcCallResult:" + simpleJdbcCallResult);

        AtomicReference<Object> sMessage = new AtomicReference<>();

        simpleJdbcCallResult.forEach((s, o) -> {
            //System.out.println("s:" + s);
            //System.out.println("o:" + o);

            if (s.equals("P_OUT")) {
                String strMessage = o.toString();
                //System.out.println("strMessage:" + strMessage);
                sMessage.set(o);
            }
        });

        //System.out.println("sMessage.get():" + sMessage.get());
        String messageString = sMessage.get().toString();

        String flag = messageString.substring(0, 1);
        //System.out.println("flag:" + flag);
        if (flag.equals("E")) {
            errorMessage = messageString.substring(2);
            return new DMLResponseDto("E", "Error in saving self roster");
        } else {
            recCounts = recCounts + Integer.parseInt(messageString.substring(2));
            //System.out.println("recCounts:" + recCounts);
        }

        inParamMap.clear();
        simpleJdbcCallResult.clear();
        messageString = "";

        if (flag.equals("S")) {

            for (SelfRosterLine selfRosterLine : reqDto.getSelfRosterLine()) {

                Long generatedSelfRosterLineId = 0L;

                if (selfRosterLine.selfRosterLineId() != null) {

                    generatedSelfRosterLineId = selfRosterLine.selfRosterLineId();

                } else {
                    generatedSelfRosterLineId = jdbcClient
                            .sql("SELECT SELF_ROSTER_LINE_ID_SQ.NEXTVAL FROM dual")
                            .query(Long.class)
                            .single();
                }

                inParamMap.put("p_created_by", selfRosterLine.createdBy());
                inParamMap.put("p_d_1", selfRosterLine.d_1());
                inParamMap.put("p_d_2", selfRosterLine.d_2());
                inParamMap.put("p_d_3", selfRosterLine.d_3());
                inParamMap.put("p_d_4", selfRosterLine.d_4());
                inParamMap.put("p_d_5", selfRosterLine.d_5());
                inParamMap.put("p_d_6", selfRosterLine.d_6());
                inParamMap.put("p_d_7", selfRosterLine.d_7());
                inParamMap.put("p_last_updated_by", selfRosterLine.lastUpdatedBy());
                inParamMap.put("p_person_roster_id_1", selfRosterLine.personRosterId_1());
                inParamMap.put("p_person_roster_id_2", selfRosterLine.personRosterId_2());
                inParamMap.put("p_person_roster_id_3", selfRosterLine.personRosterId_3());
                inParamMap.put("p_person_roster_id_4", selfRosterLine.personRosterId_4());
                inParamMap.put("p_person_roster_id_5", selfRosterLine.personRosterId_5());
                inParamMap.put("p_person_roster_id_6", selfRosterLine.personRosterId_6());
                inParamMap.put("p_person_roster_id_7", selfRosterLine.personRosterId_7());
                inParamMap.put("p_self_roster_id", generatedId);
                inParamMap.put("p_self_roster_line_id", generatedSelfRosterLineId);
                inParamMap.put("p_created_on", selfRosterLine.createdOn());
                inParamMap.put("p_end_date", selfRosterLine.endDate());
                inParamMap.put("p_last_update_date", selfRosterLine.lastUpdateDate());
                inParamMap.put("p_start_date", selfRosterLine.startDate());

                inSource = new MapSqlParameterSource(inParamMap);
                //System.out.println(inSource);
                simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("SC_SAVE_SELF_ROSTER_LINE_P");
                simpleJdbcCallResult = simpleJdbcCall.execute(inSource);

                AtomicReference<Object> sMessageline = new AtomicReference<>();

                simpleJdbcCallResult.forEach((s, o) -> {
                    //System.out.println(s);
                    //System.out.println(o);

                    if (s.equals("P_OUT")) {
                        String strMessage = o.toString();
                        //System.out.println("strMessage:" + strMessage);
                        sMessageline.set(o);
                    }
                });

                //System.out.println("sMessageline.get():" + sMessageline.get());
                messageString = sMessageline.get().toString();

                flag = messageString.substring(0, 1);
                //System.out.println("flag:" + flag);
                if (flag.equals("E")) {
                    errorMessage = messageString.substring(2);
                    return new DMLResponseDto("E", "Error in saving self roster lines");
                } else {
                    recCounts = recCounts + Integer.parseInt(messageString.substring(2));
                    //System.out.println("recCounts:" + recCounts);
                }

                inParamMap.clear();
                simpleJdbcCallResult.clear();
                messageString = "";
                flag = "";

            }

        }

        return new DMLResponseDto("S", "sd");

    }

}
