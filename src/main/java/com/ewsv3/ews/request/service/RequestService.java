package com.ewsv3.ews.request.service;

import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.request.dto.*;
import com.ewsv3.ews.timesheets.dto.submission.TimesheetActionReqBody;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.ewsv3.ews.request.service.RequestUtils.*;

@Service
public class RequestService {

    private SimpleJdbcCall simpleJdbcCall;
    private JdbcTemplate jdbcTemplate;

    // Remove @PostConstruct and move initialization to the constructor
    public RequestService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcTemplate.setResultsMapCaseInsensitive(true);
        // Try with schema specification and enable debugging
        this.simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("SC_PERSON_REQUESTS_P")
                .withoutProcedureColumnMetaDataAccess(); // Disable metadata access to avoid discovery issues
    }

    // @PostConstruct
    // public void init() {
    // jdbcTemplate.setResultsMapCaseInsensitive(true);
    // simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
    // .withProcedureName("SC_PERSON_REQUESTS_P")
    // .declareParameters(
    // new SqlInOutParameter("p_person_request_id", Types.NUMERIC),
    // new SqlParameter("p_request_master_id", Types.NUMERIC),
    // new SqlParameter("p_person_id", Types.NUMERIC),
    // new SqlParameter("p_time_hour", Types.VARCHAR),
    // new SqlParameter("p_date_start", Types.DATE),
    // new SqlParameter("p_date_end", Types.DATE),
    // new SqlParameter("p_time_start", Types.TIMESTAMP), // <-- changed
    // new SqlParameter("p_time_end", Types.TIMESTAMP), // <-- changed
    // new SqlParameter("p_specific_days", Types.VARCHAR),
    // new SqlParameter("p_mon", Types.VARCHAR),
    // new SqlParameter("p_tue", Types.VARCHAR),
    // new SqlParameter("p_wed", Types.VARCHAR),
    // new SqlParameter("p_thu", Types.VARCHAR),
    // new SqlParameter("p_fri", Types.VARCHAR),
    // new SqlParameter("p_sat", Types.VARCHAR),
    // new SqlParameter("p_sun", Types.VARCHAR),
    // new SqlParameter("p_created_by", Types.NUMERIC),
    // new SqlParameter("p_created_on", Types.TIMESTAMP), // <-- changed
    // new SqlParameter("p_last_updated_by", Types.NUMERIC),
    // new SqlParameter("p_last_update_date", Types.TIMESTAMP), // <-- changed
    // new SqlParameter("p_status", Types.VARCHAR),
    // new SqlParameter("p_comments", Types.VARCHAR),
    // new SqlParameter("p_dml_mode", Types.VARCHAR),
    // new SqlParameter("p_request_reason_id", Types.NUMERIC),
    // new SqlParameter("p_new_time_start", Types.TIMESTAMP), // <-- fix here
    // new SqlParameter("p_new_time_end", Types.TIMESTAMP),
    // new SqlParameter("p_s_person_roster_id", Types.NUMERIC),
    // new SqlParameter("p_d_person_roster_id", Types.NUMERIC),
    // new SqlOutParameter("p_item_key", Types.NUMERIC),
    // new SqlOutParameter("p_approval_user_id", Types.VARCHAR),
    // new SqlOutParameter("p_message", Types.VARCHAR)
    // );
    // }

    // public RequestService(JdbcTemplate jdbcTemplate) {
    // this.jdbcTemplate = jdbcTemplate;
    // }

    public List<RequestMaster> getRequestMaster(JdbcClient jdbcClient) {

        List<RequestMaster> requestMasters = jdbcClient.sql(RequestsMasterSql).query(RequestMaster.class).list();
        return requestMasters;

    }

    public List<RequestResp> getRequests(Long personId, LocalDate startDate, LocalDate endDate, JdbcClient jdbcClient) {

        Map<String, Object> personDateMap = new HashMap<>();
        personDateMap.put("personId", personId);
        personDateMap.put("startDate", startDate);
        personDateMap.put("endDate", endDate);

        List<RequestResp> requestRespList = jdbcClient.sql(RequestLinesSql).params(personDateMap)
                .query(RequestResp.class).list();

        return requestRespList;
    }

    public String createRequest(Long userId, NewRequestReqBody reqBody, JdbcClient jdbcClient) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        LocalDate sDate = LocalDate.parse(reqBody.dateStart(), dateFormatter);
        LocalDate eDate = LocalDate.parse(reqBody.dateEnd(), dateFormatter);

        //System.out.println("createRequest sDate:" + sDate);
        //System.out.println("createRequest eDate:" + eDate);
        //System.out.println("createRequest userId:" + userId);

        // Sanitize time strings to remove any non-standard whitespace
        String sanitizedTimeStart = reqBody.timeStart().replaceAll("\\s+", " ").trim();
        String sanitizedTimeEnd = reqBody.timeEnd().replaceAll("\\s+", " ").trim();
        //System.out.println("Sanitized timeStart: '" + sanitizedTimeStart + "'");
        //System.out.println("Sanitized timeEnd: '" + sanitizedTimeEnd + "'");

        // Try both 12-hour and 24-hour patterns for time parsing
        LocalTime sTime = null;
        LocalTime eTime = null;
        DateTimeFormatter formatter12 = DateTimeFormatter.ofPattern("hh:mm a", java.util.Locale.ENGLISH);
        DateTimeFormatter formatter24 = DateTimeFormatter.ofPattern("HH:mm", java.util.Locale.ENGLISH);
        try {
            sTime = LocalTime.parse(sanitizedTimeStart, formatter12);
        } catch (Exception ex1) {
            try {
                sTime = LocalTime.parse(sanitizedTimeStart, formatter24);
            } catch (Exception ex2) {
                throw new RuntimeException("Could not parse timeStart: '" + sanitizedTimeStart + "'", ex2);
            }
        }
        //System.out.println("createRequest sTime:" + sTime);
        try {
            eTime = LocalTime.parse(sanitizedTimeEnd, formatter12);
        } catch (Exception ex1) {
            try {
                eTime = LocalTime.parse(sanitizedTimeEnd, formatter24);
            } catch (Exception ex2) {
                throw new RuntimeException("Could not parse timeEnd: '" + sanitizedTimeEnd + "'", ex2);
            }
        }
        //System.out.println("createRequest eTime:" + eTime);

        // Combine LocalDate and LocalTime to create LocalDateTime
        LocalDateTime startTime = sDate.atTime(sTime);
        LocalDateTime endTime = sDate.atTime(eTime);

        //System.out.println("createRequest startTime" + startTime);
        //System.out.println("createRequest endTime" + endTime);

        // Use direct CallableStatement for more control
        try {
            String sql = "{call SC_PERSON_REQUESTS_P(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

            return jdbcTemplate.execute(sql, (java.sql.CallableStatement cs) -> {
                // Set IN and IN OUT parameters
                cs.setInt(1, -1); // p_person_request_id (IN OUT)
                cs.setLong(2, reqBody.requestMasterId()); // p_request_master_id
                cs.setLong(3, reqBody.personId()); // p_person_id
                cs.setString(4, reqBody.timeHour()); // p_time_hour
                cs.setDate(5, java.sql.Date.valueOf(sDate)); // p_date_start
                cs.setDate(6, java.sql.Date.valueOf(eDate)); // p_date_end
                cs.setTimestamp(7, java.sql.Timestamp.valueOf(startTime)); // p_time_start
                cs.setTimestamp(8, java.sql.Timestamp.valueOf(endTime)); // p_time_end
                if (reqBody.hours() != null) {
                    cs.setDouble(9, reqBody.hours()); // p_hours
                } else {
                    cs.setNull(9, Types.NUMERIC);
                }
                cs.setString(10, "A"); // p_specific_days
                cs.setString(11, reqBody.mon()); // p_mon
                cs.setString(12, reqBody.tue()); // p_tue
                cs.setString(13, reqBody.wed()); // p_wed
                cs.setString(14, reqBody.thu()); // p_thu
                cs.setString(15, reqBody.fri()); // p_fri
                cs.setString(16, reqBody.sat()); // p_sat
                cs.setString(17, reqBody.sun()); // p_sun
                cs.setLong(18, userId); // p_created_by
                cs.setTimestamp(19, new java.sql.Timestamp(System.currentTimeMillis())); // p_created_on
                cs.setLong(20, userId); // p_last_updated_by
                cs.setTimestamp(21, new java.sql.Timestamp(System.currentTimeMillis())); // p_last_update_date
                cs.setString(22, "SUBMIT"); // p_status
                cs.setString(23, reqBody.comments()); // p_comments
                cs.setString(24, "INS"); // p_dml_mode
                cs.setObject(25, reqBody.requestReasonId(), Types.NUMERIC);
                // if (reqBody.requestReasonId() != null) {
                // cs.setLong(25, reqBody.requestReasonId()); // p_request_reason_id
                // } else {
                // cs.setNull(25, Types.NUMERIC);
                // }
                cs.setNull(26, Types.TIMESTAMP); // p_new_time_start
                cs.setNull(27, Types.TIMESTAMP); // p_new_time_end
                cs.setObject(28, reqBody.sPersonRosterId(), Types.NUMERIC);
                // cs.setNull(28, Types.NUMERIC); // p_s_person_roster_id
                cs.setNull(29, Types.NUMERIC); // p_d_person_roster_id

                // Register OUT parameters
                cs.registerOutParameter(1, Types.NUMERIC); // p_person_request_id (IN OUT)
                cs.registerOutParameter(30, Types.NUMERIC); // p_item_key
                cs.registerOutParameter(31, Types.VARCHAR); // p_approval_user_id
                cs.registerOutParameter(32, Types.VARCHAR); // p_message

                // Execute the call
                cs.execute();

                // Get OUT parameter values
                Long personRequestId = cs.getLong(1);
                Long itemKey = cs.getLong(30);
                String approvalUserId = cs.getString(31);
                String message = cs.getString(32);

                //System.out.println("personRequestId: " + personRequestId);
                //System.out.println("itemKey: " + itemKey);
                //System.out.println("approvalUserId: " + approvalUserId);
                //System.out.println("message: " + message);

                if ("SUCCESS".equals(message)) {
                    return String.valueOf(itemKey);
                } else {
                    throw new RuntimeException(message);
                }
            });

        } catch (Exception e) {

            //System.out.println("Error calling stored procedure: " + e.getMessage());
            throw new RuntimeException("Failed to create request:" + e.getMessage(), e);
        }
    }

    public List<RequestApproval> getRequestApprovals(Long itemKey, JdbcClient jdbcClient) {
        Map<String, Object> reqApprovalMap = new HashMap<>();
        reqApprovalMap.put("itemKey", itemKey);
        List<RequestApproval> requestApprovalList = jdbcClient.sql(RequestsApprovalSql).params(reqApprovalMap)
                .query(RequestApproval.class).list();
        return requestApprovalList;

    }

    public List<DestinationRosterResponseBody> getDestinationRosters(DestinationRosterReqBody reqBody,
            JdbcClient jdbcClient) {
        Map<String, Object> reqApprovalMap = new HashMap<>();
        reqApprovalMap.put("personRosterId", reqBody.personRosterId());
        List<DestinationRosterResponseBody> rosterResponseBodyList = jdbcClient.sql(DestinationRostersSql)
                .params(reqApprovalMap)
                .query(DestinationRosterResponseBody.class).list();

        return rosterResponseBodyList;
    }

    public List<RequestNotificationResponse> getRequestNotifications(Long userId, JdbcClient jdbcClient) {

        List<RequestNotificationResponse> list = jdbcClient.sql(UserPendingNotifications)
                .param("userId", userId)
                .query(RequestNotificationResponse.class)
                .list();

        return list;
    }

    public DMLResponseDto actionRequests(Long userId, RequestActionReqBody reqBody) {

        final AtomicReference<String>[] errorMessage = new AtomicReference[] { new AtomicReference<>("") };
        final int[] recCounts = { 0 };

        Map<String, Object> inParamMap = new HashMap<>();
        inParamMap.put("p_user_id", userId);
        inParamMap.put("p_person_id", reqBody.personId());
        inParamMap.put("p_item_key", reqBody.itemKey());
        inParamMap.put("p_request_name", reqBody.requestName());
        inParamMap.put("p_reason", reqBody.reason());
        inParamMap.put("p_from_action", reqBody.fromAction());
        inParamMap.put("p_fwd_user_id", reqBody.fwdUserId());
        inParamMap.put("p_rmi_user_id", reqBody.rmiUserId());
        inParamMap.put("p_comments", reqBody.comments());

        SqlParameterSource inSource = new MapSqlParameterSource(inParamMap);
        //System.out.println(inSource);
        inParamMap.clear();
        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("SC_BULK_REQUEST_ACTIONS_P");
        Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inSource);

        //System.out.println("actionRequests simpleJdbcCallResult :" + simpleJdbcCallResult);

        AtomicReference<Object> sMessage = new AtomicReference<>();

        simpleJdbcCallResult.forEach((s, o) -> {
            //System.out.println(s);
            //System.out.println(o);

            if (s.equals("P_OUT")) {
                String strMessage = o.toString();
                //System.out.println("actionRequests strMessage:" + strMessage);
                sMessage.set(o);
            }
        });

        if (sMessage.get() != null) {
            //System.out.println("actionRequests sMessage.get():" + sMessage.get());
            String messageString = sMessage.get().toString();

            String flag = messageString.substring(0, 1);
            //System.out.println("flag:" + flag);
            if (flag.equals("E")) {
                errorMessage[0].set(messageString.length() > 1000 ? messageString.substring(0, 1000)
                        : messageString);
            } else {
                String[] parts = messageString.split("#");
                if (parts.length > 1) {
                    recCounts[0] = recCounts[0] + Integer.parseInt(parts[1]);
                    //System.out.println("actionRequests recCounts:" + recCounts[0]);
                }
            }
        }

        if (errorMessage[0].get().isEmpty()) {
            if (recCounts[0] == 0) {
                return new DMLResponseDto("S", "No Requests submitted");
            } else {
                return new DMLResponseDto("S", recCounts[0] + " requests submitted successfully");
            }

        } else {
            return new DMLResponseDto("E", recCounts[0] + errorMessage[0].get());
        }

    }

}
