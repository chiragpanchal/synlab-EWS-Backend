package com.ewsv3.ews.rosters.controller;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.rosters.dto.healthcare.AcuityDataDto;
import com.ewsv3.ews.rosters.dto.healthcare.HealthcareRosterResponseDto;
import com.ewsv3.ews.rosters.dto.healthcare.JobTitleFactorDto;
import com.ewsv3.ews.rosters.dto.healthcare.PersonMatchingDto;
import com.ewsv3.ews.rosters.dto.healthcare.ScheduledFteDto;
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

    @GetMapping("/staffing-analysis")
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
