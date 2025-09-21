package com.ewsv3.ews.timesheets.service.submission;

import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.timesheets.dto.submission.TimesheetApprovalStatus;
import com.ewsv3.ews.timesheets.dto.submission.TimesheetSubmitReqBody;
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

import static com.ewsv3.ews.timesheets.service.submission.TimesheetSubmissionUtils.sqlGetTimesheetApprovals;

@Service
public class TimesheetSubmissionService {

    private SimpleJdbcCall simpleJdbcCall;
    private final JdbcTemplate jdbcTemplate;

    public TimesheetSubmissionService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void init() {
        jdbcTemplate.setResultsMapCaseInsensitive(true);
        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("SC_BULK_TIMESHEET_SUBMISSION_P");
    }

    public DMLResponseDto submitTimesheets(Long userId, TimesheetSubmitReqBody reqBody) {

        DMLResponseDto responseDto = new DMLResponseDto();
        final AtomicReference<String>[] errorMessage = new AtomicReference[]{new AtomicReference<>("")};
        final int[] recCounts = {0};

        Map<String, Object> inParamMap = new HashMap<>();
        inParamMap.put("p_user_id", userId);
        inParamMap.put("p_person_id", reqBody.personId());
        inParamMap.put("p_profile_id", reqBody.profileId());
        inParamMap.put("p_pay_codes", reqBody.payCodes());
        inParamMap.put("p_start_date", reqBody.startDate());
        inParamMap.put("p_end_date", reqBody.endDate());
        inParamMap.put("p_comments", reqBody.comments());


        SqlParameterSource inSource = new MapSqlParameterSource(inParamMap);
        System.out.println(inSource);
        inParamMap.clear();
        // simpleJdbcCall = new
        // SimpleJdbcCall(jdbcTemplate).withProcedureName("SC_DELETE_PERSON_ROSTERS_P");
        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("SC_BULK_TIMESHEET_SUBMISSION_P");
        Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inSource);

        System.out.println("submitTimesheets simpleJdbcCallResult :" + simpleJdbcCallResult);

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

        if (sMessage.get() != null) {
            System.out.println("sMessage.get():" + sMessage.get());
            String messageString = sMessage.get().toString();

            String flag = messageString.substring(0, 1);
            System.out.println("flag:" + flag);
            if (flag.equals("E")) {
                errorMessage[0].set(messageString.length() > 1000 ? messageString.substring(0, 1000) : messageString);
            } else {
                String[] parts = messageString.split("#");
                if (parts.length > 1) {
                    recCounts[0] = recCounts[0] + Integer.parseInt(parts[1]);
                    System.out.println("recCounts:" + recCounts[0]);
                }
            }
        }

        if (errorMessage[0].get().isEmpty()) {
            if (recCounts[0] == 0) {
                return new DMLResponseDto("S", "No timesheets submitted");
            } else {
                return new DMLResponseDto("S", recCounts[0] + " timesheets submitted successfully");
            }

        } else {
            return new DMLResponseDto("E", recCounts[0] + errorMessage[0].get());
        }


    }

    public List<TimesheetApprovalStatus> getTimesheetApprovals(Long itemKey, JdbcClient jdbcClient) {
        List<TimesheetApprovalStatus> approvalStatuses = jdbcClient.sql(sqlGetTimesheetApprovals)
                .param("itemKey", itemKey)
                .query(TimesheetApprovalStatus.class)
                .list();

        return approvalStatuses;
    }


}
