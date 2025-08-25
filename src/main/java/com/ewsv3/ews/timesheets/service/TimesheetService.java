package com.ewsv3.ews.timesheets.service;

import com.ewsv3.ews.timesheets.dto.*;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
            TimesheetPageRequestBody requestBody,
            JdbcClient jdbcClient) {

        List<TimesheetPerson> personList = jdbcClient.sql(sqlTimesheetPersonList)
                .param("userId", userId)
                .param("profileId", requestBody.profileId())
                .param("text", "%" + text + "%")
//                .param("pFilterFlag", filterFlag)
                .param("startDate", requestBody.startDate())
                .param("endDate", requestBody.endDate())
                .param("offset", page)
                .param("pageSize", size)
                .query(TimesheetPerson.class)
                .list();


        TimesheetPerson selfPersonRecord = jdbcClient.sql(sqlSelfRecord)
                .param("userId", userId)
                .query(TimesheetPerson.class)
                .single();

        if (personList.isEmpty()) {
            return new ArrayList<>();
        }


        personList.add(selfPersonRecord);

        // Bulk fetch all date summaries for all persons in one query
        List<TimesheetDateSummary> allDateSummaries = jdbcClient.sql(sqlTimesheetDateSummaryBulk)
                .param("userId", userId)
                .param("profileId", requestBody.profileId())
                .param("text", "%" + text + "%")
                .param("startDate", requestBody.startDate())
                .param("endDate", requestBody.endDate())
                .query(TimesheetDateSummary.class)
                .list();

        // Bulk fetch all table summaries for all persons in one query
        List<TimesheetTableSummary> allTableSummaries = jdbcClient.sql(sqlTimesheetTableDataBulk)
                .param("userId", userId)
                .param("profileId", requestBody.profileId())
                .param("text", "%" + text + "%")
                .param("startDate", requestBody.startDate())
                .param("endDate", requestBody.endDate())
                .query(TimesheetTableSummary.class)
                .list();

        // Create maps for efficient lookup
        Map<Long, Map<LocalDate, TimesheetDateSummary>> dateSummaryMap = allDateSummaries.stream()
                .collect(Collectors.groupingBy(
                        TimesheetDateSummary::getPersonId,
                        Collectors.toMap(TimesheetDateSummary::getEffectiveDate, Function.identity())
                ));

        Map<String, List<TimesheetTableSummary>> tableSummaryMap = allTableSummaries.stream()
                .collect(Collectors.groupingBy(
                        ts -> ts.personId() + "_" + ts.effectiveDate()
                ));

        List<TimesheetPageResponseBody> pageResponseBody = new ArrayList<>();

        for (TimesheetPerson person : personList) {
            System.out.println("getTimesheetData person.personId():" + person.personId());

            List<TimesheetDateSummary> dateSummaries = new ArrayList<>();
            Map<LocalDate, TimesheetDateSummary> personDateMap = dateSummaryMap.get(person.personId());

            // Generate all dates in range and populate with existing data or nulls
            for (var date = requestBody.startDate(); !date.isAfter(requestBody.endDate()); date = date.plusDays(1)) {
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
                            null
                    );
                }

                // Attach table summaries to this date summary
                String key = person.personId() + "_" + date;
                List<TimesheetTableSummary> tableSummaries = tableSummaryMap.getOrDefault(key, new ArrayList<>());
                dateSummary.setTimesheetTableSummaries(tableSummaries);

                dateSummaries.add(dateSummary);
            }

            TimesheetPageResponseBody responseBody = new TimesheetPageResponseBody(person, dateSummaries);
            pageResponseBody.add(responseBody);
        }

        return pageResponseBody;


    }

    public TimesheetKpi getTimesheetKpi(Long userId,
                                        String text,
                                        TimesheetPageRequestBody requestBody,
                                        JdbcClient jdbcClient) {


        System.out.println("getTimesheetKpi requestBody:" + requestBody);

        List<TimesheetPayCodeKpi> payCodeKpi = jdbcClient.sql(sqlTimesheetPayCodeHrs)
                .param("userId", userId)
                .param("profileId", requestBody.profileId())
                .param("text", "%" + text + "%")
                .param("startDate", requestBody.startDate())
                .param("endDate", requestBody.endDate())
                .query(TimesheetPayCodeKpi.class)
                .list();
        System.out.println("getTimesheetKpi payCodeKpi:" + payCodeKpi);

        List<TimesheetStatusKpi> statusKpi = jdbcClient.sql(sqlTimesheetStatusCounts)
                .param("userId", userId)
                .param("profileId", requestBody.profileId())
                .param("text", "%" + text + "%")
                .param("startDate", requestBody.startDate())
                .param("endDate", requestBody.endDate())
                .query(TimesheetStatusKpi.class)
                .list();

        System.out.println("getTimesheetKpi statusKpi:" + statusKpi);

        TimesheetKpi timesheetKpi = new TimesheetKpi(
                payCodeKpi,
                statusKpi
        );

        return timesheetKpi;

    }

}
