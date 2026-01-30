package com.ewsv3.ews.timecard.service;


import com.ewsv3.ews.request.dto.RequestResp;
import com.ewsv3.ews.request.service.RequestService;
import com.ewsv3.ews.timecard.dto.TimecardActuals;
import com.ewsv3.ews.timecard.dto.TimecardLine;
import com.ewsv3.ews.timecard.dto.TimecardObject;
import com.ewsv3.ews.timecard.dto.TimecardSummary;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.ewsv3.ews.timecard.service.TimecardUtils.*;

@Service
public class TimecardService {

    private final RequestService requestService;

    public TimecardService(RequestService requestService) {
        this.requestService = requestService;
    }

    public TimecardObject getTimecards(long personId, LocalDate startDate, LocalDate endDate, JdbcClient jdbcClient) {

        Map<String, Object> personDateMap = new HashMap<>();

        //System.out.println("-------SelfTiemcardImpl");
        System.out.println("-------personId" + personId);
        //System.out.println("-------startDate" + startDate);
        //System.out.println("-------endDate" + endDate);

        personDateMap.put("person_id", personId);
        personDateMap.put("start_date", startDate);
        personDateMap.put("end_date", endDate);

        List<TimecardLine> timecardLines = jdbcClient.sql(TimecardLineSql).params(personDateMap)
                .query(TimecardLine.class).list();

        for (TimecardLine line : timecardLines) {

            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("person_id", personId);
//            objectMap.put("person_roster_id", line.getPersonRosterId());
            Object personRosterId = line.getPersonRosterId();
            objectMap.put("person_roster_id", Objects.requireNonNullElse(personRosterId, 0L));
            objectMap.put("effective_date", line.getEffectiveDate());

            //System.out.println("person_roster_id:" + line.getPersonRosterId());
            //System.out.println("effective_date:" + line.getEffectiveDate());

            List<TimecardActuals> timecardActuals = jdbcClient.sql(TimecardActualsSql).params(objectMap)
                    .query(TimecardActuals.class).list();

            line.setTimecardActuals(timecardActuals);

            // Get request info

            List<RequestResp> requests = requestService.getRequests(personId, line.getEffectiveDate(),
                    line.getEffectiveDate(), jdbcClient);

            line.setRequestRespList(requests);

            // Long personId,
            // Date startDate,
            // Date endDate

        }

        TimecardSummary timecardSummary = jdbcClient.sql(TimecardSummarySql).params(personDateMap)
                .query(TimecardSummary.class).single();

        TimecardObject timecardObject = new TimecardObject();
        timecardObject.setTimecardLines(timecardLines);
        timecardObject.setTimecardSummary(timecardSummary);

        return timecardObject;

    }
}
