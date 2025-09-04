package com.ewsv3.ews.timesheets.service.form;

import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.rosters.dto.rosters.payload.RosterDMLResponseDto;
import com.ewsv3.ews.timesheets.dto.form.*;
import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static com.ewsv3.ews.timesheets.service.form.TimesheetFormUtils.*;

@Service
public class TimesheetFormService {

    private SimpleJdbcCall simpleJdbcCall;
    private final JdbcTemplate jdbcTemplate;

    public TimesheetFormService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @PostConstruct
    public void init() {
        jdbcTemplate.setResultsMapCaseInsensitive(true);
        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("SC_TTS_SAVE_P");
    }

    public List<PayCodeDto> getPayCodes(Long userId, TimesheetFormReqDto reqDto, JdbcClient jdbcClient) {

        List<PayCodeDto> payCodeDtoList = jdbcClient.sql(sqlPayCodeList)
                .query(PayCodeDto.class)
                .list();

        return payCodeDtoList;
    }

    public List<TsDepartmentDto> getDepartments(Long userId, TimesheetFormReqDto reqDto, JdbcClient jdbcClient) {
        List<TsDepartmentDto> tsDepartmentDtoList = jdbcClient.sql(sqlDepartmentList)
                .param("personId", reqDto.personId())
                .query(TsDepartmentDto.class)
                .list();

        return tsDepartmentDtoList;
    }

    public List<TsJobDto> getJobs(Long userId, TimesheetFormReqDto reqDto, JdbcClient jdbcClient) {
        List<TsJobDto> tsJobDtoList = jdbcClient.sql(sqlJobList)
                .param("personId", reqDto.personId())
                .query(TsJobDto.class)
                .list();

        return tsJobDtoList;
    }

    public List<ProjectTaskDto> getProjectTasks(Long userId, TimesheetFormReqDto reqDto, JdbcClient jdbcClient) {
        List<ProjectTaskDto> projectTaskDtoList = jdbcClient.sql(sqlProjectTaskList)
                .param("personId", reqDto.personId())
                .query(ProjectTaskDto.class)
                .list();

        return projectTaskDtoList;
    }

    public List<ExpTypeDto> getExpenditureTypes(Long userId, TimesheetFormReqDto reqDto, JdbcClient jdbcClient) {
        List<ExpTypeDto> expTypeDtoList = jdbcClient.sql(sqlExpenditureList)
                .param("personId", reqDto.personId())
                .query(ExpTypeDto.class)
                .list();

        return expTypeDtoList;
    }

    public TsMasters getTimesheetMasters(Long userId, TimesheetFormReqDto reqDto, JdbcClient jdbcClient) {

        TsMasters tsMasters = new TsMasters(
                getPayCodes(userId, reqDto, jdbcClient),
                getDepartments(userId, reqDto, jdbcClient),
                getJobs(userId, reqDto, jdbcClient),
                getProjectTasks(userId, reqDto, jdbcClient),
                getExpenditureTypes(userId, reqDto, jdbcClient)
        );

        return tsMasters;


    }

    public List<TimesheetDetails> getTimesheetdetails(Long userId, TimesheetDetailsReqDto reqDto, JdbcClient jdbcClient) {

        List<TimesheetDetails> timesheetDetails = jdbcClient.sql(sqlTimesheetDetails)
                .param("personId", reqDto.personId())
                .param("startDate", reqDto.startDate())
                .param("endDate", reqDto.endDate())
                .query(TimesheetDetails.class)
                .list();

        return timesheetDetails;
    }


    public List<TsTimecardData> getTimecardData(Long userId, TimesheetDetailsReqDto reqDto, JdbcClient jdbcClient) {

        List<TsTimecardData> tsTimecardDataList = jdbcClient.sql(sqlTsTimecardData)
                .param("personId", reqDto.personId())
                .param("startDate", reqDto.startDate())
                .param("endDate", reqDto.endDate())
                .query(TsTimecardData.class)
                .list();

        return tsTimecardDataList;
    }

    public DMLResponseDto saveTimesheets(Long userId, List<TimesheetDetails> detailsList, JdbcClient jdbcClient) {

        DMLResponseDto responseDto = new DMLResponseDto();
        String errorMessage = "";
        int recCounts = 0;

        for (TimesheetDetails details : detailsList) {
            Map<String, Object> inParamMap = new HashMap<>();
            inParamMap.put("p_tts_timesheet_id", details.ttsTimesheetId());
            inParamMap.put("p_person_id", details.personId());
            inParamMap.put("p_effective_date", details.effectiveDate());
            inParamMap.put("p_time_hour", details.timeHour());
            inParamMap.put("p_time_start", details.timeStart());
            inParamMap.put("p_time_end", details.timeEnd());
            inParamMap.put("p_reg_hrs", details.regHrs());
            inParamMap.put("p_department_id", details.departmentId());
            inParamMap.put("p_job_title_id", details.jobTitleId());
            inParamMap.put("p_project_id", details.projectId());
            inParamMap.put("p_task_id", details.taskId());
            inParamMap.put("p_exp_type_id", details.expTypeId());
            inParamMap.put("p_allw_value", details.allwValue());
            inParamMap.put("p_item_key", details.itemKey());
            inParamMap.put("p_pay_code_id", details.payCodeId());
            inParamMap.put("p_comments", details.comments());
            inParamMap.put("p_user_id", userId);
            inParamMap.put("p_person_roster_id", details.personRosterId());

            SqlParameterSource inSource = new MapSqlParameterSource(inParamMap);
            System.out.println(inSource);
            inParamMap.clear();
            // simpleJdbcCall = new
            // SimpleJdbcCall(jdbcTemplate).withProcedureName("SC_DELETE_PERSON_ROSTERS_P");
            simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("SC_TTS_SAVE_P");
            Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inSource);

            AtomicReference<Object> sMessage = new AtomicReference<>();

            simpleJdbcCallResult.forEach((s, o) -> {
                System.out.println(s);
                System.out.println(o);

                if (s.equals("P_OUT")) {
                    String strMessage = o.toString();
                    System.out.println("strMessage:" + strMessage);
                    sMessage.set(o);
                }
            });

            System.out.println("sMessage.get():" + sMessage.get());
            String messageString = sMessage.get().toString();

            String flag = messageString.substring(0, 1);
            System.out.println("flag:" + flag);
            if (flag.equals("E")) {
                errorMessage = messageString.substring(2);
                break;
            } else {
//                recCounts = recCounts + Integer.parseInt(messageString.substring(2));
                recCounts = recCounts + Integer.parseInt(messageString.split("#")[1]);
                System.out.println("recCounts:" + recCounts);
            }

        }

        if (errorMessage.isEmpty()) {
            return new DMLResponseDto("S", recCounts + " timesheets saved successfully");
        } else {
            return new DMLResponseDto("E", recCounts + errorMessage);
        }


    }

    public DMLResponseDto deleteTimesheets(Long userId, List<TimesheetDetails> detailsList, JdbcClient jdbcClient) {

        DMLResponseDto responseDto = new DMLResponseDto();
        String errorMessage = "";
        int recCounts = 0;

        for (TimesheetDetails details : detailsList) {
            Map<String, Object> inParamMap = new HashMap<>();
            inParamMap.put("p_user_id", userId);
            inParamMap.put("p_person_id", details.personId());
            inParamMap.put("p_tts_timesheet_id", details.ttsTimesheetId());
            inParamMap.put("p_start_date", details.effectiveDate());
            inParamMap.put("p_end_date", details.effectiveDate());
            inParamMap.put("p_delete_comments", details.comments());


            SqlParameterSource inSource = new MapSqlParameterSource(inParamMap);
            System.out.println(inSource);
            inParamMap.clear();
            // simpleJdbcCall = new
            // SimpleJdbcCall(jdbcTemplate).withProcedureName("SC_DELETE_PERSON_ROSTERS_P");
            simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("SC_TTS_DELETE_P");
            Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inSource);

            AtomicReference<Object> sMessage = new AtomicReference<>();

            simpleJdbcCallResult.forEach((s, o) -> {
                System.out.println(s);
                System.out.println(o);

                if (s.equals("P_OUT")) {
                    String strMessage = o.toString();
                    System.out.println("strMessage:" + strMessage);
                    sMessage.set(o);
                }
            });

            System.out.println("sMessage.get():" + sMessage.get());
            String messageString = sMessage.get().toString();

            String flag = messageString.substring(0, 1);
            System.out.println("flag:" + flag);
            if (flag.equals("E")) {
                errorMessage = messageString.substring(2);
                break;
            } else {
                recCounts = recCounts + Integer.parseInt(messageString.split("#")[1]);
                System.out.println("recCounts:" + recCounts);
            }
        }


        if (errorMessage.isEmpty()) {
            return new DMLResponseDto("S", recCounts + " timesheets deleted successfully");
        } else {
            return new DMLResponseDto("E", recCounts + errorMessage);
        }


    }

    public List<TimesheetAuditDto> getTimesheetAudit(Long userId, TimesheetDetailsReqDto reqDto, JdbcClient jdbcClient) {

        List<TimesheetAuditDto> timesheetAuditDtoList = jdbcClient.sql(sqlTimesheetAudit)
                .param("personId", reqDto.personId())
                .param("startDate", reqDto.startDate())
                .param("endDate", reqDto.endDate())
                .query(TimesheetAuditDto.class)
                .list();

        return timesheetAuditDtoList;

    }

}
