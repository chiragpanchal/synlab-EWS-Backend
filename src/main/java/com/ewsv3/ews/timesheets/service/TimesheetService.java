package com.ewsv3.ews.timesheets.service;

import com.ewsv3.ews.timesheets.dto.*;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ewsv3.ews.timesheets.service.TimesheetUtils.*;

@Service
public class TimesheetService {

        public List<TimesheetPageResponseBody> getTimesheetData(
                        Long userId,
                        int page,
                        int size,
                        String text,
                        String filterFlag,
                        String payCodeName,
                        TimesheetPageRequestBody requestBody,
                        JdbcClient jdbcClient) {

                // System.out.println("getTimesheetData before get personList currenttime:" +
                // LocalDateTime.now());

                List<TimesheetPerson> personList = new ArrayList<>();
                if (requestBody.profileId() == -1) {
                        // self timesheets
                        personList = jdbcClient.sql(sqlSelfRecord)
                                        .param("userId", userId)
                                        .param("startDate", requestBody.startDate())
                                        .param("endDate", requestBody.endDate())
                                        .param("filterFlag", filterFlag)
                                        .param("payCodeFlag", payCodeName)
                                        .query(TimesheetPerson.class)
                                        .list();
                } else if (requestBody.profileId() == 0) {
                        // line manager
                        personList = jdbcClient.sql(sqlLineManagerRecords)
                                        .param("userId", userId)
                                        .param("text", "%" + text + "%")
                                        .param("filterFlag", filterFlag)
                                        .param("payCodeFlag", payCodeName)
                                        // .param("pFilterFlag", filterFlag)
                                        .param("startDate", requestBody.startDate())
                                        .param("endDate", requestBody.endDate())
                                        .query(TimesheetPerson.class)
                                        .list();

                } else {
                        // Timekeeper Profile
                        personList = jdbcClient.sql(sqlTimesheetPersonList)
                                        .param("userId", userId)
                                        .param("profileId", requestBody.profileId())
                                        .param("text", "%" + text + "%")
                                        .param("filterFlag", filterFlag)
                                        .param("payCodeFlag", payCodeName)
                                        // .param("pFilterFlag", filterFlag)
                                        .param("startDate", requestBody.startDate())
                                        .param("endDate", requestBody.endDate())
                                        .param("offset", page)
                                        .param("pageSize", size)
                                        .query(TimesheetPerson.class)
                                        .list();

                        // System.out.println("getTimesheetData personList.size():" +
                        // personList.size());

                }

                // System.out.println("getTimesheetData after get personList currenttime:" +
                // LocalDateTime.now());

                // System.out.println("getTimesheetData getTimesheetData personList.size():" +
                // personList.size());

                if (personList.isEmpty()) {
                        return new ArrayList<>();
                }

                List<Long> personIds = personList.stream()
                                .map(timesheetPerson -> timesheetPerson.personId())
                                .collect(Collectors.toList());

                // System.out.println("personIds.size():" + personIds.size());
                // System.out.println("personIds:" + personIds);

                List<TimesheetDateSummary> allDateSummaries = new ArrayList<>();
                List<TimesheetTableSummary> allTableSummaries = new ArrayList<>();

                // System.out.println("getTimesheetData before get allDateSummaries
                // currenttime:" + LocalDateTime.now());
                if (requestBody.profileId() == -1) {
                        for (TimesheetPerson person : personList) {
                                // System.out.println("getTimesheetData person.personId():" +
                                // person.personId());
                                allDateSummaries = jdbcClient.sql(sqlSelfTimesheetDateSummaryBulk)
                                                .param("personId", person.personId())
                                                .param("startDate", requestBody.startDate())
                                                .param("endDate", requestBody.endDate())
                                                .param("filterFlag", filterFlag)
                                                .param("payCodeFlag", payCodeName)
                                                .query(TimesheetDateSummary.class)
                                                .list();
                                // System.out.println(
                                // "getTimesheetData allDateSummaries.size():" + allDateSummaries.size());

                                allTableSummaries = jdbcClient.sql(sqlSelfTimesheetTableDataBulk)
                                                .param("personId", person.personId())
                                                .param("startDate", requestBody.startDate())
                                                .param("endDate", requestBody.endDate())
                                                .param("filterFlag", filterFlag)
                                                .param("payCodeFlag", payCodeName)
                                                .query(TimesheetTableSummary.class)
                                                .list();
                                // System.out.println("getTimesheetData allTableSummaries.size():"
                                // + allTableSummaries.size());
                                // System.out.println("===============================================");

                        }
                        // self timesheets
                } else if (requestBody.profileId() == 0) {
                        // Bulk fetch all date summaries for all persons in one query
                        allDateSummaries = jdbcClient.sql(sqlLineManagerTimesheetDateSummaryBulk)
                                        .param("userId", userId)
                                        .param("text", "%" + text + "%")
                                        .param("startDate", requestBody.startDate())
                                        .param("endDate", requestBody.endDate())
                                        .param("filterFlag", filterFlag)
                                        .param("payCodeFlag", payCodeName)
                                        .query(TimesheetDateSummary.class)
                                        .list();

                        // Bulk fetch all table summaries for all persons in one query
                        allTableSummaries = jdbcClient.sql(sqlLineManagerTimesheetTableDataBulk)
                                        .param("userId", userId)
                                        .param("text", "%" + text + "%")
                                        .param("startDate", requestBody.startDate())
                                        .param("endDate", requestBody.endDate())
                                        .param("filterFlag", filterFlag)
                                        .param("payCodeFlag", payCodeName)
                                        .query(TimesheetTableSummary.class)
                                        .list();
                        // line manager
                } else {
                        // Timekeeper Profile
                        // Bulk fetch all date summaries for all persons in one query
                        allDateSummaries = jdbcClient.sql(sqlTimesheetDateSummaryBulk)
                                        .param("userId", userId)
                                        .param("profileId", requestBody.profileId())
                                        .param("text", "%" + text + "%")
                                        .param("filterFlag", filterFlag)
                                        .param("payCodeFlag", payCodeName)
                                        .param("startDate", requestBody.startDate())
                                        .param("endDate", requestBody.endDate())
                                        .param("personIds", personIds)
                                        // .param("offset", page)
                                        // .param("pageSize", size)
                                        .query(TimesheetDateSummary.class)
                                        .list();

                        // System.out.println("getTimesheetData after get allDateSummaries currenttime:"
                        // + LocalDateTime.now());
                        // System.out.println("getTimesheetData before get allTableSummaries
                        // currenttime:"
                        // + LocalDateTime.now());
                        // Bulk fetch all table summaries for all persons in one query
                        allTableSummaries = jdbcClient.sql(sqlTimesheetTableDataBulk)
                                        .param("userId", userId)
                                        .param("profileId", requestBody.profileId())
                                        .param("text", "%" + text + "%")
                                        .param("filterFlag", filterFlag)
                                        .param("payCodeFlag", payCodeName)
                                        .param("startDate", requestBody.startDate())
                                        .param("endDate", requestBody.endDate())
                                        .param("personIds", personIds)
                                        // .param("offset", page)
                                        // .param("pageSize", size)
                                        .query(TimesheetTableSummary.class)
                                        .list();

                        // System.out.println("getTimesheetData after get allTableSummaries
                        // currenttime:"
                        // + LocalDateTime.now());
                }

                // System.out.println("getTimesheetData Line 169 currenttime:" +
                // LocalDateTime.now());
                // Create maps for efficient lookup
                Map<Long, Map<LocalDate, TimesheetDateSummary>> dateSummaryMap = allDateSummaries.stream()
                                .collect(Collectors.groupingBy(
                                                TimesheetDateSummary::getPersonId,
                                                Collectors.toMap(TimesheetDateSummary::getEffectiveDate,
                                                                Function.identity())));

                // System.out.println("getTimesheetData Line 177 currenttime:" +
                // LocalDate.now());
                Map<String, List<TimesheetTableSummary>> tableSummaryMap = allTableSummaries.stream()
                                .collect(Collectors.groupingBy(
                                                ts -> ts.personId() + "_" + ts.effectiveDate()));

                List<TimesheetPageResponseBody> pageResponseBody = new ArrayList<>();

                // System.out.println("getTimesheetData Line 185 currenttime:" +
                // LocalDateTime.now());
                for (TimesheetPerson person : personList) {
                        // System.out.println("getTimesheetData 2 person.personId():" +
                        // person.personId());

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

                // System.out.println("getTimesheetData Line 222 currenttime:" +
                // LocalDateTime.now());

                return pageResponseBody;

        }

        public TimesheetKpi getTimesheetKpi(Long userId,
                        String text,
                        String filterFlag,
                        String payCodeName,
                        TimesheetPageRequestBody requestBody,
                        JdbcClient jdbcClient) {

                // System.out.println("getTimesheetKpi requestBody:" + requestBody);
                List<TimesheetPayCodeKpi> payCodeKpi = new ArrayList<>();
                List<TimesheetStatusKpi> statusKpi = new ArrayList<>();

                if (requestBody.profileId() == -1) {
                        // self
                        payCodeKpi = jdbcClient.sql(sqlSelfTimesheetPayCodeHrs)
                                        .param("userId", userId)
                                        .param("filterFlag", filterFlag)
                                        .param("startDate", requestBody.startDate())
                                        .param("endDate", requestBody.endDate())
                                        .query(TimesheetPayCodeKpi.class)
                                        .list();
                        // System.out.println("getTimesheetKpi payCodeKpi:" + payCodeKpi);

                        statusKpi = jdbcClient.sql(sqlSelfTimesheetStatusCounts)
                                        .param("userId", userId)
                                        .param("payCodeFlag", payCodeName)
                                        .param("startDate", requestBody.startDate())
                                        .param("endDate", requestBody.endDate())
                                        .query(TimesheetStatusKpi.class)
                                        .list();
                } else if (requestBody.profileId() == 0) {
                        // Line Manager
                        payCodeKpi = jdbcClient.sql(sqlLineManagerTimesheetPayCodeHrs)
                                        .param("userId", userId)
                                        .param("text", "%" + text + "%")
                                        .param("filterFlag", filterFlag)
                                        .param("startDate", requestBody.startDate())
                                        .param("endDate", requestBody.endDate())
                                        .query(TimesheetPayCodeKpi.class)
                                        .list();
                        // System.out.println("getTimesheetKpi payCodeKpi:" + payCodeKpi);

                        statusKpi = jdbcClient.sql(sqlLineManagerTimesheetStatusCounts)
                                        .param("userId", userId)
                                        .param("text", "%" + text + "%")
                                        .param("payCodeFlag", payCodeName)
                                        .param("startDate", requestBody.startDate())
                                        .param("endDate", requestBody.endDate())
                                        .query(TimesheetStatusKpi.class)
                                        .list();
                } else {
                        // selectedProfile
                        payCodeKpi = jdbcClient.sql(sqlTimesheetPayCodeHrs)
                                        .param("userId", userId)
                                        .param("profileId", requestBody.profileId())
                                        .param("text", "%" + text + "%")
                                        .param("filterFlag", filterFlag)
                                        .param("startDate", requestBody.startDate())
                                        .param("endDate", requestBody.endDate())
                                        .query(TimesheetPayCodeKpi.class)
                                        .list();
                        // System.out.println("getTimesheetKpi payCodeKpi:" + payCodeKpi);

                        statusKpi = jdbcClient.sql(sqlTimesheetStatusCounts)
                                        .param("userId", userId)
                                        .param("profileId", requestBody.profileId())
                                        .param("text", "%" + text + "%")
                                        .param("payCodeFlag", payCodeName)
                                        .param("startDate", requestBody.startDate())
                                        .param("endDate", requestBody.endDate())
                                        .query(TimesheetStatusKpi.class)
                                        .list();

                }

                // System.out.println("getTimesheetKpi statusKpi:" + statusKpi);

                TimesheetKpi timesheetKpi = new TimesheetKpi(
                                payCodeKpi,
                                statusKpi);

                return timesheetKpi;

        }

}
