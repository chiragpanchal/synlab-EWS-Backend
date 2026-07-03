package com.ewsv3.ews.payrollaudit.service;

import com.ewsv3.ews.payrollaudit.dto.*;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class PayrollAuditService {

    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(PayrollAuditService.class);

    public PayrollAuditService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void init() {
        jdbcTemplate.setResultsMapCaseInsensitive(true);
    }

    public List<PayrollAuditSummaryDto> getPayrollAuditSummary(
            Long userId,
            Long payPeriodId,
            Long departmentId,
            Long jobTitleId,
            String payCodes,
            Long locationId,
            Long gradeId,
            String assignmentNumber,
            String status) {

        Map<String, Object> inParamMap = new java.util.HashMap<>();
        inParamMap.put("p_user_id", userId);
        inParamMap.put("p_pay_period_id", payPeriodId);
        inParamMap.put("p_department_id", departmentId);
        inParamMap.put("p_job_title_id", jobTitleId);
        inParamMap.put("p_pay_codes", payCodes);
        inParamMap.put("p_location_id", locationId);
        inParamMap.put("p_grade_id", gradeId);
        inParamMap.put("p_assignment_number", assignmentNumber);
        inParamMap.put("p_status", status);

        System.out.println("getPayrollAuditSummary inParamMap:"+inParamMap);

        SqlParameterSource inSource = new MapSqlParameterSource(inParamMap);

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("SC_MANAGE_PAYROLL_AUDIT_PKG")
                .withProcedureName("get_audit_summ_p")
                .returningResultSet("p_list", (rs, rowNum) -> mapToSummaryDto(rs));

        try {
            Map<String, Object> result = call.execute(inSource);
            List<PayrollAuditSummaryDto> summaryList = (List<PayrollAuditSummaryDto>) result.get("p_list");
            return summaryList != null ? summaryList : java.util.Collections.emptyList();
        } catch (Exception e) {
            logger.error("Error calling getPayrollAuditSummary procedure", e);
            return java.util.Collections.emptyList();
        }
    }

    public List<PayrollAuditDetailsDto> getPayrollAuditDetails(
            Long userId,
            Long payPeriodId,
            Long departmentId,
            Long jobTitleId,
            String payCodes,
            Long locationId,
            Long gradeId,
            String assignmentNumber,
            String status) {

        Map<String, Object> inParamMap = new java.util.HashMap<>();
        inParamMap.put("p_user_id", userId);
        inParamMap.put("p_pay_period_id", payPeriodId);
        inParamMap.put("p_department_id", departmentId);
        inParamMap.put("p_job_title_id", jobTitleId);
        inParamMap.put("p_pay_codes", payCodes);
        inParamMap.put("p_location_id", locationId);
        inParamMap.put("p_grade_id", gradeId);
        inParamMap.put("p_assignment_number", assignmentNumber);
        inParamMap.put("p_status", status);

        SqlParameterSource inSource = new MapSqlParameterSource(inParamMap);

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("SC_MANAGE_PAYROLL_AUDIT_PKG")
                .withProcedureName("get_audit_detail_p")
                .returningResultSet("p_list", (rs, rowNum) -> mapToDetailsDto(rs));

        try {
            Map<String, Object> result = call.execute(inSource);
            List<PayrollAuditDetailsDto> detailsList = (List<PayrollAuditDetailsDto>) result.get("p_list");
            return detailsList != null ? detailsList : java.util.Collections.emptyList();
        } catch (Exception e) {
            logger.error("Error calling getPayrollAuditDetails procedure", e);
            return java.util.Collections.emptyList();
        }
    }

    public List<PayPeriodDto> getPayPeriods(JdbcClient jdbcClient) {
        String sql = "SELECT " +
                "spp.pay_period_id, " +
                "spp.pay_calendar_id, " +
                "spp.pay_period_name, " +
                "spp.start_date, " +
                "spp.end_date, " +
                "spp.process_date, " +
                "spp.created_by, " +
                "spp.created_on, " +
                "spp.last_updated_by, " +
                "spp.last_update_date, " +
                "spp.cutoff_date " +
                "FROM sc_pay_periods spp " +
                "ORDER BY spp.start_date";

        try {
            return jdbcClient.sql(sql)
                    .query(PayPeriodDto.class)
                    .list();
        } catch (Exception e) {
            logger.error("Error fetching pay periods", e);
            return java.util.Collections.emptyList();
        }
    }

    public PayrollAuditMastersDto getPayrollAuditMasters(JdbcClient jdbcClient) {
        try {
            List<PayCodeDto> payCodes = getPayCodes(jdbcClient);
            List<DepartmentMasterDto> departments = getDepartments(jdbcClient);
            List<JobTitleMasterDto> jobTitles = getJobTitles(jdbcClient);
            List<LocationMasterDto> locations = getLocations(jdbcClient);
            List<GradeMasterDto> grades = getGrades(jdbcClient);

            return new PayrollAuditMastersDto(payCodes, departments, jobTitles, locations, grades);
        } catch (Exception e) {
            logger.error("Error fetching payroll audit masters", e);
            return new PayrollAuditMastersDto(
                    java.util.Collections.emptyList(),
                    java.util.Collections.emptyList(),
                    java.util.Collections.emptyList(),
                    java.util.Collections.emptyList(),
                    java.util.Collections.emptyList()
            );
        }
    }

    private List<PayCodeDto> getPayCodes(JdbcClient jdbcClient) {
        String sql = "SELECT pay_code_id, pay_code, pay_code_name " +
                "FROM sc_pay_codes spc " +
                "WHERE enabled = 'Y' AND consider_in_total = 'Y' AND payroll_audit = 'Y' " +
                "ORDER BY pay_code_name";
        try {
            return jdbcClient.sql(sql).query(PayCodeDto.class).list();
        } catch (Exception e) {
            logger.error("Error fetching pay codes", e);
            return java.util.Collections.emptyList();
        }
    }

    private List<DepartmentMasterDto> getDepartments(JdbcClient jdbcClient) {
        String sql = "SELECT department_id, department_name FROM sc_departments " +
                "WHERE enabled = 'Y' ORDER BY department_name";
        try {
            return jdbcClient.sql(sql).query(DepartmentMasterDto.class).list();
        } catch (Exception e) {
            logger.error("Error fetching departments", e);
            return java.util.Collections.emptyList();
        }
    }

    private List<JobTitleMasterDto> getJobTitles(JdbcClient jdbcClient) {
        String sql = "SELECT job_title_id, job_title FROM sc_jobs " +
                "WHERE enabled = 'Y' ORDER BY job_title";
        try {
            return jdbcClient.sql(sql).query(JobTitleMasterDto.class).list();
        } catch (Exception e) {
            logger.error("Error fetching job titles", e);
            return java.util.Collections.emptyList();
        }
    }

    private List<LocationMasterDto> getLocations(JdbcClient jdbcClient) {
        String sql = "SELECT work_location_id, location_name FROM sc_work_locations " +
                "WHERE enabled = 'Y' ORDER BY location_name";
        try {
            return jdbcClient.sql(sql).query(LocationMasterDto.class).list();
        } catch (Exception e) {
            logger.error("Error fetching locations", e);
            return java.util.Collections.emptyList();
        }
    }

    private List<GradeMasterDto> getGrades(JdbcClient jdbcClient) {
        String sql = "SELECT grade_id, grade_name FROM sc_grades " +
                "WHERE enabled = 'Y' ORDER BY grade_name";
        try {
            return jdbcClient.sql(sql).query(GradeMasterDto.class).list();
        } catch (Exception e) {
            logger.error("Error fetching grades", e);
            return java.util.Collections.emptyList();
        }
    }

    private PayrollAuditSummaryDto mapToSummaryDto(ResultSet rs) throws SQLException {
        return new PayrollAuditSummaryDto(
                rs.getLong("person_id"),
                rs.getString("employee_number"),
                rs.getString("assignment_number"),
                rs.getString("full_name"),
                rs.getString("department_name"),
                rs.getString("job_title"),
                rs.getString("grade_name"),
                rs.getString("business_unit_name"),
                rs.getString("legal_entity"),
                rs.getString("employee_types"),
                rs.getString("location_name"),
                rs.getString("pay_code_name"),
                rs.getBigDecimal("hours")
        );
    }

    private PayrollAuditDetailsDto mapToDetailsDto(ResultSet rs) throws SQLException {
        return new PayrollAuditDetailsDto(
                rs.getLong("person_id"),
                rs.getString("employee_number"),
                rs.getString("assignment_number"),
                rs.getString("full_name"),
                rs.getString("department_name"),
                rs.getString("job_title"),
                rs.getString("grade_name"),
                rs.getString("business_unit_name"),
                rs.getString("legal_entity"),
                rs.getString("employee_types"),
                rs.getString("location_name"),
                rs.getDate("effective_date") != null ? rs.getDate("effective_date").toLocalDate() : null,
                rs.getLong("payroll_audit_line_id"),
                rs.getLong("tts_timesheet_id"),
                rs.getString("pay_code_name"),
                rs.getLong("n_pay_code_id"),
                rs.getBigDecimal("n_hours"),
                rs.getString("n_comments")
        );
    }
}
