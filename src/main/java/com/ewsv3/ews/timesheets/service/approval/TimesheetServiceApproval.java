package com.ewsv3.ews.timesheets.service.approval;

import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.timesheets.dto.*;
import com.ewsv3.ews.timesheets.dto.submission.TimesheetActionReqBody;
import com.ewsv3.ews.timesheets.dto.submission.TimesheetApprovalDates;
import com.ewsv3.ews.timesheets.dto.submission.TimesheetPeriodTypeReqBody;

import jakarta.annotation.PostConstruct;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ewsv3.ews.timesheets.service.approval.TimesheetUtilsApproval.*;

@Service
public class TimesheetServiceApproval {

        private SimpleJdbcCall simpleJdbcCall;
        private final JdbcTemplate jdbcTemplate;

        public TimesheetServiceApproval(JdbcTemplate jdbcTemplate) {
                this.jdbcTemplate = jdbcTemplate;
        }

        @PostConstruct
        public void init() {
                jdbcTemplate.setResultsMapCaseInsensitive(true);
                simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("SC_BULK_TIMESHEET_ACTIONS_P");
        }

        public List<TimesheetApprovalDates> getApprovalDates(Long userId, TimesheetPeriodTypeReqBody requestBody,
                        JdbcClient jdbcClient) {

                List<TimesheetApprovalDates> list = jdbcClient.sql(sqlGetApprovalPeriods)
                                .param("userId", userId)
                                .param("periodType", requestBody.periodType())
                                .query(TimesheetApprovalDates.class)
                                .list();

                return list;

        }

        public List<TimesheetPageResponseBody> getTimesheetApprovalData(
                        Long userId,
                        int page,
                        int size,
                        String payCodeName,
                        TimesheetPageRequestBody requestBody,
                        JdbcClient jdbcClient) {

                //System.out.println("getTimesheetApprovalData before get personList currenttime:" + LocalDateTime.now());

                List<TimesheetPerson> personList = new ArrayList<>();

                // Timekeeper Profile
                personList = jdbcClient.sql(sqlApprovalTimesheetPersonList)
                                .param("userId", userId)
                                .param("payCodeFlag", payCodeName)
                                // .param("pFilterFlag", filterFlag)
                                .param("startDate", requestBody.startDate())
                                .param("endDate", requestBody.endDate())
                                .param("offset", page)
                                .param("pageSize", size)
                                .query(TimesheetPerson.class)
                                .list();

                //System.out.println("getTimesheetApprovalData  personList.size():" + personList.size());

                //System.out.println("getTimesheetApprovalData after get personList currenttime:" + LocalDateTime.now());

                //System.out.println("getTimesheetApprovalData personList.size():" + personList.size());

                if (personList.isEmpty()) {
                        return new ArrayList<>();
                }

                List<Long> personIds = personList.stream()
                                .map(timesheetPerson -> timesheetPerson.personId())
                                .collect(Collectors.toList());

                //System.out.println("getTimesheetApprovalData personIds.size():" + personIds.size());
                //System.out.println("getTimesheetApprovalData personIds:" + personIds);

                List<TimesheetDateSummary> allDateSummaries = new ArrayList<>();
                List<TimesheetTableSummary> allTableSummaries = new ArrayList<>();

                //System.out.println("getTimesheetApprovalData before get allDateSummaries currenttime:"
                //                + LocalDateTime.now());

                // Timekeeper Profile

                //System.out.println("getTimesheetApprovalData before get allTableSummaries currenttime:"
                //                + LocalDateTime.now());
                // Bulk fetch all table summaries for all persons in one query
                allTableSummaries = jdbcClient.sql(sqlApprovalTimesheetTableDataBulk)
                                .param("userId", userId)
                                .param("payCodeFlag", payCodeName)
                                .param("startDate", requestBody.startDate())
                                .param("endDate", requestBody.endDate())
                                .param("personIds", personIds)
                                // .param("offset", page)
                                // .param("pageSize", size)
                                .query(TimesheetTableSummary.class)
                                .list();

                //System.out.println("getTimesheetApprovalData after get allTableSummaries currenttime:"
                //                + LocalDateTime.now());

                //System.out.println("getTimesheetApprovalData Line 169 currenttime:" + LocalDateTime.now());
                // Create maps for efficient lookup
                Map<Long, Map<LocalDate, TimesheetDateSummary>> dateSummaryMap = allDateSummaries.stream()
                                .collect(Collectors.groupingBy(
                                                TimesheetDateSummary::getPersonId,
                                                Collectors.toMap(TimesheetDateSummary::getEffectiveDate,
                                                                Function.identity())));

                //System.out.println("getTimesheetApprovalData Line 177 currenttime:" + LocalDate.now());
                Map<String, List<TimesheetTableSummary>> tableSummaryMap = allTableSummaries.stream()
                                .collect(Collectors.groupingBy(
                                                ts -> ts.personId() + "_" + ts.effectiveDate()));

                List<TimesheetPageResponseBody> pageResponseBody = new ArrayList<>();

                //System.out.println("getTimesheetApprovalData Line 185 currenttime:" + LocalDateTime.now());
                for (TimesheetPerson person : personList) {

                        List<TimesheetDateSummary> dateSummaries = new ArrayList<>();
                        Map<LocalDate, TimesheetDateSummary> personDateMap = dateSummaryMap.get(person.personId());

                        // Generate all dates in range and populate with existing data or nulls
                        for (var date = requestBody.startDate(); !date.isAfter(requestBody.endDate()); date = date
                                        .plusDays(1)) {
                                TimesheetDateSummary dateSummary;
                                if (personDateMap != null && personDateMap.containsKey(date)) {
                                        dateSummary = personDateMap.get(date);
                                } else {
                                        dateSummary = new TimesheetDateSummary(
                                                        person.personId(),
                                                        date,
                                                        null,
                                                        null,
                                                        null,
                                                        null,
                                                        null,
                                                        null);
                                }

                                // Attach table summaries to this date summary
                                String key = person.personId() + "_" + date;
                                List<TimesheetTableSummary> tableSummaries = tableSummaryMap.getOrDefault(key,
                                                new ArrayList<>());
                                dateSummary.setTimesheetTableSummaries(tableSummaries);

                                dateSummaries.add(dateSummary);
                        }

                        TimesheetPageResponseBody responseBody = new TimesheetPageResponseBody(person, dateSummaries);
                        pageResponseBody.add(responseBody);
                }

                //System.out.println("getTimesheetApprovalData Line 222 currenttime:" + LocalDateTime.now());

                return pageResponseBody;

        }

        public TimesheetKpi getTimesheetApprovalKpi(Long userId,
                        String payCodeName,
                        TimesheetPageRequestBody requestBody,
                        JdbcClient jdbcClient) {

                //System.out.println("getTimesheetApprovalKpi requestBody:" + requestBody);
                List<TimesheetPayCodeKpi> payCodeKpi = new ArrayList<>();
                List<TimesheetStatusKpi> statusKpi = new ArrayList<>();

                // selectedProfile
                payCodeKpi = jdbcClient.sql(sqlApprovalTimesheetPayCodeHrs)
                                .param("userId", userId)
                                .param("startDate", requestBody.startDate())
                                .param("endDate", requestBody.endDate())
                                .query(TimesheetPayCodeKpi.class)
                                .list();
                //System.out.println("getTimesheetApprovalKpi payCodeKpi:" + payCodeKpi);

                TimesheetKpi timesheetKpi = new TimesheetKpi(
                                payCodeKpi,
                                statusKpi);

                return timesheetKpi;

        }

        public DMLResponseDto actionTimesheets(Long userId, TimesheetActionReqBody reqBody) {

                final AtomicReference<String>[] errorMessage = new AtomicReference[] { new AtomicReference<>("") };
                final int[] recCounts = { 0 };

                Map<String, Object> inParamMap = new HashMap<>();
                inParamMap.put("p_user_id", userId);
                inParamMap.put("p_person_id", reqBody.personId());
                inParamMap.put("p_pay_codes", reqBody.payCodes());
                inParamMap.put("p_start_date", reqBody.startDate());
                inParamMap.put("p_end_date", reqBody.endDate());
                inParamMap.put("p_from_action", reqBody.fromAction());
                inParamMap.put("p_strperson", reqBody.strPerson());
                inParamMap.put("p_fwd_user_id", reqBody.fwdUserId());
                inParamMap.put("p_rmi_user_id", reqBody.rmiUserId());
                inParamMap.put("p_comments", reqBody.comments());

                SqlParameterSource inSource = new MapSqlParameterSource(inParamMap);
                //System.out.println(inSource);
                inParamMap.clear();
                // simpleJdbcCall = new
                // SimpleJdbcCall(jdbcTemplate).withProcedureName("SC_DELETE_PERSON_ROSTERS_P");
                simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("SC_BULK_TIMESHEET_ACTIONS_P");
                Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inSource);

                //System.out.println("actionTimesheets simpleJdbcCallResult :" + simpleJdbcCallResult);

                AtomicReference<Object> sMessage = new AtomicReference<>();

                simpleJdbcCallResult.forEach((s, o) -> {
                        //System.out.println(s);
                        //System.out.println(o);

                        if (s.equals("P_OUT")) {
                                String strMessage = o.toString();
                                //System.out.println("strMessage:" + strMessage);
                                sMessage.set(o);
                        }
                });

                if (sMessage.get() != null) {
                        //System.out.println("sMessage.get():" + sMessage.get());
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
                                        //System.out.println("recCounts:" + recCounts[0]);
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

}
