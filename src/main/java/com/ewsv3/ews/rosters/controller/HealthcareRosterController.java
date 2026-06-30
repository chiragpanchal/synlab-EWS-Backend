package com.ewsv3.ews.rosters.controller;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.rosters.dto.healthcare.AcuityDataDto;
import com.ewsv3.ews.rosters.dto.healthcare.AppointFteDto;
import com.ewsv3.ews.rosters.dto.healthcare.AppointStaffingAnalysisDto;
import com.ewsv3.ews.rosters.dto.healthcare.HealthcareRosterResponseDto;
import com.ewsv3.ews.rosters.dto.healthcare.JobTitleFactorDto;
import com.ewsv3.ews.rosters.dto.healthcare.OccupyFteDto;
import com.ewsv3.ews.rosters.dto.healthcare.OccupyStaffingAnalysisDto;
import com.ewsv3.ews.rosters.dto.healthcare.PersonMatchingDto;
import com.ewsv3.ews.rosters.dto.healthcare.ProfileRulesCountDto;
import com.ewsv3.ews.rosters.dto.healthcare.ScheduledFteDto;
import com.ewsv3.ews.rosters.dto.healthcare.ScheduledFteWithShiftDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/healthcare-roster")
public class HealthcareRosterController {
    private static final Logger logger = LoggerFactory.getLogger(HealthcareRosterController.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public HealthcareRosterController(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
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

    @GetMapping("/staffing-analysis-acuity")
    public ResponseEntity<HealthcareRosterResponseDto> getStaffingAnalysis(
            @RequestParam Long profileId,
            @RequestParam LocalDate effectiveDate) {
        try {
            logger.info("Fetching staffing analysis for profileId: {}, effectiveDate: {}", profileId, effectiveDate);

            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("profileId", profileId)
                    .addValue("effectiveDate", effectiveDate);

            List<AcuityDataDto> acuityData = getAcuityData(params);
            List<JobTitleFactorDto> jobTitleFactors = getJobTitleFactors(params);
            List<ScheduledFteDto> scheduledFte = getScheduledFte(params);

            HealthcareRosterResponseDto response = new HealthcareRosterResponseDto(
                    acuityData,
                    jobTitleFactors,
                    scheduledFte
            );

            logger.info("Successfully fetched staffing analysis with {} acuity records, {} job titles, {} scheduled FTE",
                    acuityData.size(), jobTitleFactors.size(), scheduledFte.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching staffing analysis for profileId: {}, effectiveDate: {}",
                    profileId, effectiveDate, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private List<AcuityDataDto> getAcuityData(MapSqlParameterSource params) {
        String sql = "SELECT " +
                "t.effective_date, " +
                "l.level_name, " +
                "t.patient_counts, " +
                "ar.ratio_name derived_ratio, " +
                "ceil(t.patient_counts / ar.beds) req_fte " +
                "FROM " +
                "sc_cerner_acuity_data t, " +
                "sc_acuity_levels l, " +
                "sc_acuity_rules r, " +
                "sc_acuity_ratio ar " +
                "WHERE " +
                "l.acuity_level_id = t.acuity_level_id " +
                "AND r.profile_id = t.profile_id " +
                "AND r.acuity_level_id = t.acuity_level_id " +
                "AND ar.ratio_id = r.ratio_id " +
                "AND r.profile_id = :profileId " +
                "AND effective_date = :effectiveDate " +
                "ORDER BY l.level_name";

        return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) ->
                new AcuityDataDto(
                        rs.getObject("effective_date", LocalDate.class),
                        rs.getString("level_name"),
                        rs.getInt("patient_counts"),
                        rs.getString("derived_ratio"),
                        rs.getBigDecimal("req_fte")
                )
        );
    }

    private List<JobTitleFactorDto> getJobTitleFactors(MapSqlParameterSource params) {
        String sql = "SELECT " +
                "job_title_id, " +
                "job_title, " +
                "(req_fte / for_fte) factor " +
                "FROM (" +
                "SELECT DISTINCT " +
                "j.job_title_id, " +
                "j.job_title, " +
                "19 for_fte, " +
                "(SELECT SUM(ceil(t.patient_counts / ar.beds)) " +
                "FROM sc_cerner_acuity_data t, " +
                "sc_acuity_levels l, " +
                "sc_acuity_rules r, " +
                "sc_acuity_ratio ar " +
                "WHERE l.acuity_level_id = t.acuity_level_id " +
                "AND r.profile_id = t.profile_id " +
                "AND r.acuity_level_id = t.acuity_level_id " +
                "AND ar.ratio_id = r.ratio_id " +
                "AND r.profile_id = m.profile_id " +
                "AND effective_date = :effectiveDate) req_fte " +
                "FROM sc_acuity_job_mappings m, " +
                "sc_jobs j " +
                "WHERE j.job_title_id = m.for_job_title_id " +
                "AND m.profile_id = :profileId " +
                "UNION " +
                "SELECT DISTINCT " +
                "j.job_title_id, " +
                "j.job_title, " +
                "for_fte, " +
                "req_fte " +
                "FROM sc_acuity_job_mappings m, " +
                "sc_jobs j " +
                "WHERE j.job_title_id = m.req_joib_title_id " +
                "AND m.profile_id = :profileId " +
                ")";

        return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) ->
                new JobTitleFactorDto(
                        rs.getLong("job_title_id"),
                        rs.getString("job_title"),
                        rs.getBigDecimal("factor")
                )
        );
    }

    private List<ScheduledFteDto> getScheduledFte(MapSqlParameterSource params) {
        String sql = "SELECT " +
                "spr.job_title_id, " +
                "COUNT(spr.person_id) scheduled_fte " +
                "FROM sc_person_rosters spr " +
                "WHERE spr.effective_date = :effectiveDate " +
                "AND spr.person_id IN (SELECT tkv.person_id FROM sc_timekeeper_person_v tkv WHERE tkv.profile_id = :profileId) " +
                "GROUP BY spr.job_title_id";

        return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) ->
                new ScheduledFteDto(
                        rs.getLong("job_title_id"),
                        rs.getInt("scheduled_fte")
                )
        );
    }

    @GetMapping("/rules-count")
    public ResponseEntity<ProfileRulesCountDto> getRulesCount(
            @RequestParam Long profileId) {
        try {
            logger.info("Fetching rules count for profileId: {}", profileId);

            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("profileId", profileId);

            ProfileRulesCountDto response = getRulesCountData(params);

            logger.info("Successfully fetched rules count - ACUITY_RULES: {}, OCCUPY_RULES: {}, APPOINT_RULES: {}",
                    response.getACUITY_RULES(), response.getOCCUPY_RULES(), response.getAPPOINT_RULES());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching rules count for profileId: {}", profileId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private ProfileRulesCountDto getRulesCountData(MapSqlParameterSource params) {
        String acuitySql = "SELECT COUNT(*) FROM SC_ACUITY_RULES WHERE profile_id = :profileId";
        String occupySql = "SELECT COUNT(*) FROM SC_OCCUPY_RULES WHERE profile_id = :profileId";
        String appointSql = "SELECT COUNT(*) FROM SC_APPOINT_RULES WHERE profile_id = :profileId";

        Long acuityCount = namedParameterJdbcTemplate.queryForObject(acuitySql, params, Long.class);
        Long occupyCount = namedParameterJdbcTemplate.queryForObject(occupySql, params, Long.class);
        Long appointCount = namedParameterJdbcTemplate.queryForObject(appointSql, params, Long.class);

        return new ProfileRulesCountDto(acuityCount, occupyCount, appointCount);
    }

    @GetMapping("/staffing-analysis-occupy")
    public ResponseEntity<OccupyStaffingAnalysisDto> getStaffingAnalysisOccupy(
            @RequestParam Long profileId,
            @RequestParam LocalDate effectiveDate) {
        try {
            logger.info("Fetching occupy staffing analysis for profileId: {}, effectiveDate: {}", profileId, effectiveDate);

            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("profileId", profileId)
                    .addValue("effectiveDate", effectiveDate);

            List<OccupyFteDto> occupyData = getOccupyData(params);
            List<ScheduledFteWithShiftDto> scheduledFte = getScheduledFteWithShift(params);

            OccupyStaffingAnalysisDto response = new OccupyStaffingAnalysisDto(
                    occupyData,
                    scheduledFte
            );

            logger.info("Successfully fetched occupy staffing analysis with {} occupy records, {} scheduled FTE",
                    occupyData.size(), scheduledFte.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching occupy staffing analysis for profileId: {}, effectiveDate: {}",
                    profileId, effectiveDate, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private List<OccupyFteDto> getOccupyData(MapSqlParameterSource params) {
        String sql = "SELECT " +
                "ocd.profile_id, " +
                "ocd.effective_date, " +
                "ocd.occupied_beds, " +
                "ocr.job_title_id, " +
                "ocr.work_duration_id, " +
                "swd.work_duration_name ||' ('||to_char(swd.time_start,'hh:mi am')||' - '||to_char(swd.time_end,'hh:mi am') shift, " +
                "sj.job_title, " +
                "ceil((ocd.occupied_beds/ greatest(ocr.OCC_BEDS,1))* ocr.REQ_FTE) req_fte " +
                "FROM " +
                "sc_cerner_occupy_data ocd, " +
                "sc_occupy_rules ocr, " +
                "sc_jobs sj, " +
                "sc_work_duration swd " +
                "WHERE " +
                "ocr.profile_id = ocd.profile_id " +
                "AND sj.job_title_id = ocr.job_title_id " +
                "AND swd.work_duration_id = ocr.work_duration_id " +
                "AND ocd.profile_id = :profileId " +
                "AND ocd.effective_date = :effectiveDate " +
                "ORDER BY sj.job_title, swd.time_start";

        return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) ->
                new OccupyFteDto(
                        rs.getLong("profile_id"),
                        rs.getObject("effective_date", LocalDate.class),
                        rs.getInt("occupied_beds"),
                        rs.getLong("job_title_id"),
                        rs.getLong("work_duration_id"),
                        rs.getString("shift"),
                        rs.getString("job_title"),
                        rs.getBigDecimal("req_fte")
                )
        );
    }

    private List<ScheduledFteWithShiftDto> getScheduledFteWithShift(MapSqlParameterSource params) {
        String sql = "SELECT " +
                "spr.job_title_id, " +
                "spr.work_duration_id, " +
                "COUNT(spr.person_id) scheduled_fte " +
                "FROM " +
                "sc_person_rosters spr " +
                "WHERE " +
                "spr.effective_date = :effectiveDate " +
                "AND spr.person_id IN (SELECT tkv.person_id FROM sc_timekeeper_person_v tkv WHERE tkv.profile_id = :profileId) " +
                "GROUP BY " +
                "spr.job_title_id, " +
                "spr.work_duration_id";

        return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) ->
                new ScheduledFteWithShiftDto(
                        rs.getLong("job_title_id"),
                        rs.getLong("work_duration_id"),
                        rs.getInt("scheduled_fte")
                )
        );
    }

    @GetMapping("/staffing-analysis-appoint")
    public ResponseEntity<AppointStaffingAnalysisDto> getStaffingAnalysisAppoint(
            @RequestParam Long profileId,
            @RequestParam LocalDate effectiveDate) {
        try {
            logger.info("Fetching appoint staffing analysis for profileId: {}, effectiveDate: {}", profileId, effectiveDate);

            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("profileId", profileId)
                    .addValue("effectiveDate", effectiveDate);

            List<AppointFteDto> appointData = getAppointData(params);
            List<ScheduledFteWithShiftDto> scheduledFte = getScheduledFteWithShift(params);

            AppointStaffingAnalysisDto response = new AppointStaffingAnalysisDto(
                    appointData,
                    scheduledFte
            );

            logger.info("Successfully fetched appoint staffing analysis with {} appoint records, {} scheduled FTE",
                    appointData.size(), scheduledFte.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching appoint staffing analysis for profileId: {}, effectiveDate: {}",
                    profileId, effectiveDate, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private List<AppointFteDto> getAppointData(MapSqlParameterSource params) {
        String sql = "SELECT " +
                "ocd.profile_id, " +
                "ocd.effective_date, " +
                "ocd.APPOINTMENTS, " +
                "ocr.job_title_id, " +
                "ocr.work_duration_id, " +
                "swd.work_duration_name ||' ('||to_char(swd.time_start,'hh:mi am')||' - '||to_char(swd.time_end,'hh:mi am') shift, " +
                "sj.job_title, " +
                "ceil((ocd.APPOINTMENTS/ greatest(ocr.NOS_APPOINTMENTS,1))* ocr.REQ_FTE) req_fte " +
                "FROM " +
                "sc_cerner_appoint_data ocd, " +
                "sc_appoint_rules ocr, " +
                "sc_jobs sj, " +
                "sc_work_duration swd " +
                "WHERE " +
                "ocr.profile_id = ocd.profile_id " +
                "AND sj.job_title_id = ocr.job_title_id " +
                "AND swd.work_duration_id = ocr.work_duration_id " +
                "AND ocd.profile_id = :profileId " +
                "AND ocd.effective_date = :effectiveDate " +
                "ORDER BY sj.job_title, swd.time_start";

        return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) ->
                new AppointFteDto(
                        rs.getLong("profile_id"),
                        rs.getObject("effective_date", LocalDate.class),
                        rs.getInt("APPOINTMENTS"),
                        rs.getLong("job_title_id"),
                        rs.getLong("work_duration_id"),
                        rs.getString("shift"),
                        rs.getString("job_title"),
                        rs.getBigDecimal("req_fte")
                )
        );
    }

    @GetMapping("/person-matching")
    public ResponseEntity<List<PersonMatchingDto>> getPersonMatching(
            @RequestParam Long profileId,
            @RequestParam Long jobTitleId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam LocalDate effectiveDate) {
        try {
            logger.info("Fetching person matching for userId: {}, profileId: {}, jobTitleId: {}, startDate: {}, endDate: {}",
                    getCurrentUserId(), profileId, jobTitleId, startDate, endDate);

            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("userId", getCurrentUserId())
                    .addValue("profileId", profileId)
                    .addValue("jobTitleId", jobTitleId)
                    .addValue("startDate", startDate)
                    .addValue("endDate", endDate)
                    .addValue("effectiveDate", effectiveDate);

            List<PersonMatchingDto> results = getPersonMatchers(params);

            logger.info("Successfully fetched {} matching persons for jobTitleId: {}", results.size(), jobTitleId);

            return ResponseEntity.ok(results);
        } catch (Exception e) {
            logger.error("Error fetching person matching for userId: {}, jobTitleId: {}", getCurrentUserId(), jobTitleId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private List<PersonMatchingDto> getPersonMatchers(MapSqlParameterSource params) {
        String sql = "SELECT " +
                "person_id, " +
                "assignment_id, " +
                "department_id, " +
                "job_title_id, " +
                "work_location_id, " +
                "person_name, " +
                "employee_number, " +
                "grade_name, " +
                "round(sch_hrs, 2) sch_hrs, " +
                "rate, " +
                "matched_perc " +
                "FROM (" +
                "SELECT " +
                "tkv.person_id, " +
                "tkv.department_id, " +
                "tkv.job_title_id, " +
                "tkv.assignment_id, " +
                "tkv.work_location_id, " +
                "tkv.person_name, " +
                "tkv.employee_number, " +
                "tkv.grade_name, " +
                "pj.per_hr_sal rate, " +
                "(SELECT round(SUM((spr.time_end - spr.time_start) * 24), 2) hrs " +
                "FROM sc_person_rosters spr, " +
                "sc_work_duration swd " +
                "WHERE spr.person_id = tkv.person_id " +
                "AND swd.work_duration_id = spr.work_duration_id " +
                "AND swd.work_duration_code <> 'OFF' " +
                "AND trunc(spr.effective_date) BETWEEN :startDate AND :endDate) sch_hrs, " +
                "sc_get_skill_match_pers_f(p_person_id => tkv.person_id, " +
                "p_person_preferred_job_id => pj.person_preferred_job_id, " +
                "p_open_shift_line_id => NULL, " +
                "p_person_roster_id => 0) matched_perc " +
                "FROM sc_timekeeper_person_v tkv, " +
                "sc_person_preferred_jobs pj " +
                "WHERE tkv.timekeeper_user_id = :userId " +
                "AND tkv.profile_id = :profileId " +
                "AND pj.person_id = tkv.person_id " +
                "AND pj.job_title_id = :jobTitleId " +
                "AND 0 = (SELECT COUNT(spr.person_roster_id) FROM sc_person_rosters spr " +
                "WHERE spr.person_id = tkv.person_id AND spr.effective_date = :effectiveDate) " +
                "AND 0 = (SELECT COUNT(lv.absence_attendances_id) FROM sc_person_absences_t lv " +
                "WHERE lv.person_id = tkv.person_id AND lv.leave_date BETWEEN :startDate AND :endDate) " +
                "AND 0 = (SELECT COUNT(ph.holiday_id) FROM sc_person_holidays ph " +
                "WHERE ph.person_id = tkv.person_id AND ph.holiday_date BETWEEN :startDate AND :endDate) " +
                "AND nvl(tkv.hire_date, :startDate) <= :startDate " +
                "AND nvl(tkv.termination_date, :endDate) >= :endDate " +
                ") " +
                "ORDER BY nvl(matched_perc, 0) DESC, nvl(rate, 0), person_name ASC";

        return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) ->
                new PersonMatchingDto(
                        rs.getLong("person_id"),
                        rs.getLong("assignment_id"),
                        rs.getLong("department_id"),
                        rs.getLong("job_title_id"),
                        rs.getLong("work_location_id"),
                        rs.getString("person_name"),
                        rs.getString("employee_number"),
                        rs.getString("grade_name"),
                        rs.getBigDecimal("sch_hrs"),
                        rs.getBigDecimal("rate"),
                        rs.getBigDecimal("matched_perc")
                )
        );
    }
}
