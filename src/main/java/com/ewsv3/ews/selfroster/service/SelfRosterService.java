package com.ewsv3.ews.selfroster.service;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.selfroster.dto.SelfRosterReqDto;

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
                .withCatalogName("SC_SELF_ROSTER_TAPI")
                .withProcedureName("INS");
    }

    public DMLResponseDto createSelfRoster(Long userId, SelfRosterReqDto reqDto, JdbcClient jdbcClient) {

        Long generatedId = jdbcClient
                .sql("SELECT SELF_ROSTER_ID_SQ.NEXTVAL FROM dual")
                .query(Long.class)
                .single();

        Map<String, Object> inParamMap = new HashMap<>();
        inParamMap.put("p_created_by", userId);
        inParamMap.put("p_from_date", reqDto.selfRoster().fromDate());
        inParamMap.put("p_last_updated_by", userId);
        inParamMap.put("p_department_id", reqDto.selfRoster().departmentId());
        inParamMap.put("p_to_date", reqDto.selfRoster().toDate());
        inParamMap.put("p_comments", reqDto.selfRoster().comments());
        inParamMap.put("p_self_roster_id", generatedId);
        inParamMap.put("p_last_update_date", LocalTime.now());
        inParamMap.put("p_status", reqDto.selfRoster().status());
        inParamMap.put("p_job_title_id", reqDto.selfRoster().jobTitleId());
        inParamMap.put("p_person_id", reqDto.selfRoster().personId());
        inParamMap.put("p_work_location_id", reqDto.selfRoster().workLocationId());
        inParamMap.put("p_created_on", LocalTime.now());
        inParamMap.put("p_item_key", reqDto.selfRoster().itemKey());

        return new DMLResponseDto("S", "sd");

    }

}
