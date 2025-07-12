package com.ewsv3.ews.request.service;

import com.ewsv3.ews.request.dto.NewRequestReqBody;
import com.ewsv3.ews.request.dto.RequestApproval;
import com.ewsv3.ews.request.dto.RequestMaster;
import com.ewsv3.ews.request.dto.RequestResp;
import org.springframework.jdbc.core.JdbcTemplate;
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
        this.simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                // .withCatalogName("EWS_SGP")
                .withProcedureName("SC_PERSON_REQUESTS_P")
                .declareParameters(
                        new SqlParameter("p_person_request_id", Types.NUMERIC),
                        new SqlParameter("p_request_master_id", Types.NUMERIC),
                        new SqlParameter("p_person_id", Types.NUMERIC),
                        new SqlParameter("p_time_hour", Types.VARCHAR),
                        new SqlParameter("p_date_start", Types.DATE),
                        new SqlParameter("p_date_end", Types.DATE),
                        new SqlParameter("p_time_start", Types.DATE),
                        new SqlParameter("p_time_end", Types.DATE),
                        new SqlParameter("p_specific_days", Types.VARCHAR),
                        new SqlParameter("p_mon", Types.VARCHAR),
                        new SqlParameter("p_tue", Types.VARCHAR),
                        new SqlParameter("p_wed", Types.VARCHAR),
                        new SqlParameter("p_thu", Types.VARCHAR),
                        new SqlParameter("p_fri", Types.VARCHAR),
                        new SqlParameter("p_sat", Types.VARCHAR),
                        new SqlParameter("p_sun", Types.VARCHAR),
                        new SqlParameter("p_created_by", Types.NUMERIC),
                        new SqlParameter("p_created_on", Types.TIMESTAMP),
                        new SqlParameter("p_last_updated_by", Types.NUMERIC),
                        new SqlParameter("p_last_update_date", Types.TIMESTAMP),
                        new SqlParameter("p_status", Types.VARCHAR),
                        new SqlParameter("p_comments", Types.VARCHAR),
                        new SqlParameter("p_dml_mode", Types.VARCHAR),
                        new SqlParameter("p_request_reason_id", Types.NUMERIC),
                        new SqlParameter("p_new_time_start", Types.TIMESTAMP),
                        new SqlParameter("p_new_time_end", Types.TIMESTAMP),
                        new SqlParameter("p_s_person_roster_id", Types.NUMERIC),
                        new SqlParameter("p_d_person_roster_id", Types.NUMERIC),
                        new SqlOutParameter("p_item_key", Types.NUMERIC),
                        new SqlOutParameter("p_approval_user_id", Types.VARCHAR),
                        new SqlOutParameter("p_message", Types.VARCHAR));
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
        // DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
        // DateTimeFormatter timestampFormatter =
        // DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDate sDate = LocalDate.parse(reqBody.dateStart(), dateFormatter);
        LocalDate eDate = LocalDate.parse(reqBody.dateEnd(), dateFormatter);

        System.out.println("createRequest sDate:" + sDate);
        System.out.println("createRequest eDate:" + eDate);
        System.out.println("createRequest userId:" + userId);

        LocalTime sTime = LocalDateTime.parse(reqBody.timeStart(), DateTimeFormatter.ofPattern("MM/dd/yyyy, hh:mm a"))
                .toLocalTime();
        LocalTime eTime = LocalDateTime.parse(reqBody.timeEnd(), DateTimeFormatter.ofPattern("MM/dd/yyyy, hh:mm a"))
                .toLocalTime();

        // LocalTime sTime = LocalTime.parse(reqBody.timeStart(), timeFormatter);
        // LocalTime eTime = LocalTime.parse(reqBody.timeEnd(), timeFormatter);

        // Combine LocalDate and LocalTime to create LocalDateTime
        LocalDateTime startTime = sDate.atTime(sTime);
        LocalDateTime endTime = sDate.atTime(eTime);

        System.out.println("createRequest startTime" + startTime);
        System.out.println("createRequest endTime" + endTime);

        // Timestamp startTimeTs = Timestamp.valueOf(startTime);
        // Timestamp endTimeTs = Timestamp.valueOf(endTime);
        // Timestamp createdOnTs = new Timestamp(System.currentTimeMillis());
        // Timestamp lastUpdateTs = new Timestamp(System.currentTimeMillis());

        SqlParameterSource inSource = new MapSqlParameterSource()
                .addValue("p_person_request_id", null, Types.NUMERIC)
                .addValue("p_request_master_id", reqBody.requestMasterId(), Types.NUMERIC)
                .addValue("p_person_id", reqBody.personId(), Types.NUMERIC)
                .addValue("p_time_hour", reqBody.timeHour(), Types.VARCHAR)
                .addValue("p_date_start", java.sql.Date.valueOf(sDate), Types.DATE)
                .addValue("p_date_end", java.sql.Date.valueOf(eDate), Types.DATE)
                // .addValue("p_time_start", new Date(), Types.TIMESTAMP)
                // .addValue("p_time_start", LocalDateTime.of(2025, 5, 11, 9, 0), Types.DATE)
                .addValue("p_time_start", startTime, Types.DATE)
                // .addValue("p_time_end", new Date(), Types.TIMESTAMP)
                .addValue("p_time_end", endTime, Types.DATE)
                .addValue("p_specific_days", "A", Types.VARCHAR)
                .addValue("p_mon", reqBody.mon(), Types.VARCHAR)
                .addValue("p_tue", reqBody.tue(), Types.VARCHAR)
                .addValue("p_wed", reqBody.wed(), Types.VARCHAR)
                .addValue("p_thu", reqBody.thu(), Types.VARCHAR)
                .addValue("p_fri", reqBody.fri(), Types.VARCHAR)
                .addValue("p_sat", reqBody.sat(), Types.VARCHAR)
                .addValue("p_sun", reqBody.sun(), Types.VARCHAR)
                .addValue("p_created_by", userId, Types.NUMERIC)
                .addValue("p_created_on", new Date(), Types.TIMESTAMP)
                .addValue("p_last_updated_by", userId, Types.NUMERIC)
                .addValue("p_last_update_date", new Date(), Types.TIMESTAMP)
                .addValue("p_status", "SUBMIT", Types.VARCHAR)
                .addValue("p_comments", reqBody.comments(), Types.VARCHAR)
                .addValue("p_dml_mode", "INS", Types.VARCHAR)
                .addValue("p_request_reason_id", reqBody.requestReasonId(), Types.NUMERIC)
                .addValue("p_new_time_start", null, Types.TIMESTAMP)
                .addValue("p_new_time_end", null, Types.TIMESTAMP)
                .addValue("p_s_person_roster_id", null, Types.NUMERIC)
                .addValue("p_d_person_roster_id", null, Types.NUMERIC);
        System.out.println("inSource:" + inSource);

        // OUT parameters are not included in the input source

        // simpleJdbcCall
        // .withProcedureName("SC_PERSON_REQUESTS_P")
        // .declareParameters(
        // new SqlOutParameter("p_person_request_id", Types.NUMERIC),
        // new SqlOutParameter("p_item_key", Types.NUMERIC),
        // new SqlOutParameter("p_approval_user_id", Types.VARCHAR),
        // new SqlOutParameter("p_message", Types.VARCHAR)
        // );

        Map<String, Object> result = simpleJdbcCall.execute(inSource);
        System.out.println("result:" + result);

        // SqlParameterSource inSource = new MapSqlParameterSource(inParamMap);
        // System.out.println(inSource);
        // Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inSource);
        // System.out.println("simpleJdbcCallResult" + simpleJdbcCallResult);
        //

        AtomicReference<Object> sMessage = new AtomicReference<>();
        AtomicReference<Object> personRequestId = new AtomicReference<>();
        AtomicReference<Object> itemKey = new AtomicReference<>();

        result.forEach((s, o) -> {
            System.out.println(s);
            System.out.println(o);

            if (s.equalsIgnoreCase("P_MESSAGE")) {
                String strMessage = o.toString();
                System.out.println("strMessage:" + strMessage);
                sMessage.set(o);
            }

            if (s.equalsIgnoreCase("P_PERSON_REQUEST_ID")) {
                String strMessage = o.toString();
                System.out.println("strMessage:" + strMessage);
                personRequestId.set(o);
            }

            if (s.equalsIgnoreCase("P_ITEM_KEY")) {
                String strMessage = o.toString();
                System.out.println("strMessage:" + strMessage);
                // itemKeyResult.set(o.toString());
                itemKey.set(o);
            }

        });

        System.out.println("sMessage:" + sMessage);
        System.out.println("itemKey:" + itemKey);
        System.out.println("personRequestId:" + personRequestId);

        // System.out.println("sMessage.get().toString():" + sMessage.get().toString());
        // System.out.println("itemKey.get().toString():" + itemKey.get().toString());

        Object resultItemKey = itemKey.get();
        System.out.println("resultItemKey:" + resultItemKey);
        String resultStr = (resultItemKey != null) ? resultItemKey.toString() : null;

        System.out.println("resultStr:" + resultStr);

        if (Objects.equals(sMessage.get().toString(), "SUCCESS")) {
            return itemKey.get().toString();

        } else {
            throw new RuntimeException(sMessage.get().toString());
        }

    }

    public List<RequestApproval> getRequestApprovals(Long itemKey, JdbcClient jdbcClient) {
        Map<String, Object> reqApprovalMap = new HashMap<>();
        reqApprovalMap.put("itemKey", itemKey);
        List<RequestApproval> requestApprovalList = jdbcClient.sql(RequestsApprovalSql).params(reqApprovalMap)
                .query(RequestApproval.class).list();
        return requestApprovalList;

    }

}
