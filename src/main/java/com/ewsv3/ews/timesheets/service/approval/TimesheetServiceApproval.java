package com.ewsv3.ews.timesheets.service.approval;

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

import static com.ewsv3.ews.timesheets.service.approval.TimesheetUtilsApproval.*;

@Service
public class TimesheetServiceApproval {

        public List<TimesheetPageResponseBody> getTimesheetApprovalData(
                        Long userId,
                        int page,
                        int size,
                        String payCodeName,
                        TimesheetPageRequestBody requestBody,
                        JdbcClient jdbcClient) {

                System.out.println("getTimesheetApprovalData before get personList currenttime:" + LocalDateTime.now());

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

                System.out.println("getTimesheetApprovalData  personList.size():" + personList.size());

                System.out.println("getTimesheetApprovalData after get personList currenttime:" + LocalDateTime.now());

                System.out.println("getTimesheetApprovalData personList.size():" + personList.size());

                if (personList.isEmpty()) {
                        return new ArrayList<>();
                }

                List<Long> personIds = personList.stream()
                                .map(timesheetPerson -> timesheetPerson.personId())
                                .collect(Collectors.toList());

                System.out.println("getTimesheetApprovalData personIds.size():" + personIds.size());
                System.out.println("getTimesheetApprovalData personIds:" + personIds);

                List<TimesheetDateSummary> allDateSummaries = new ArrayList<>();
                List<TimesheetTableSummary> allTableSummaries = new ArrayList<>();

                System.out.println("getTimesheetApprovalData before get allDateSummaries currenttime:"
                                + LocalDateTime.now());

                // Timekeeper Profile

                System.out.println("getTimesheetApprovalData before get allTableSummaries currenttime:"
                                + LocalDateTime.now());
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

                System.out.println("getTimesheetApprovalData after get allTableSummaries currenttime:"
                                + LocalDateTime.now());

                System.out.println("getTimesheetApprovalData Line 169 currenttime:" + LocalDateTime.now());
                // Create maps for efficient lookup
                Map<Long, Map<LocalDate, TimesheetDateSummary>> dateSummaryMap = allDateSummaries.stream()
                                .collect(Collectors.groupingBy(
                                                TimesheetDateSummary::getPersonId,
                                                Collectors.toMap(TimesheetDateSummary::getEffectiveDate,
                                                                Function.identity())));

                System.out.println("getTimesheetApprovalData Line 177 currenttime:" + LocalDate.now());
                Map<String, List<TimesheetTableSummary>> tableSummaryMap = allTableSummaries.stream()
                                .collect(Collectors.groupingBy(
                                                ts -> ts.personId() + "_" + ts.effectiveDate()));

                List<TimesheetPageResponseBody> pageResponseBody = new ArrayList<>();

                System.out.println("getTimesheetApprovalData Line 185 currenttime:" + LocalDateTime.now());
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

                System.out.println("getTimesheetApprovalData Line 222 currenttime:" + LocalDateTime.now());

                return pageResponseBody;

        }

        public TimesheetKpi getTimesheetApprovalKpi(Long userId,
                        String payCodeName,
                        TimesheetPageRequestBody requestBody,
                        JdbcClient jdbcClient) {

                System.out.println("getTimesheetApprovalKpi requestBody:" + requestBody);
                List<TimesheetPayCodeKpi> payCodeKpi = new ArrayList<>();
                List<TimesheetStatusKpi> statusKpi = new ArrayList<>();

                // selectedProfile
                payCodeKpi = jdbcClient.sql(sqlApprovalTimesheetPayCodeHrs)
                                .param("userId", userId)
                                .param("startDate", requestBody.startDate())
                                .param("endDate", requestBody.endDate())
                                .query(TimesheetPayCodeKpi.class)
                                .list();
                System.out.println("getTimesheetApprovalKpi payCodeKpi:" + payCodeKpi);

                TimesheetKpi timesheetKpi = new TimesheetKpi(
                                payCodeKpi,
                                statusKpi);

                return timesheetKpi;

        }

}
