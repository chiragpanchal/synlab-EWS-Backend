package com.ewsv3.ews.reports.service.attendanceConsolidation;

import com.ewsv3.ews.reports.dto.attendanceConsolidation.*;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ewsv3.ews.reports.service.attendanceConsolidation.AttendConsSqlUtils.*;

@Service
public class AttendConsService {


    public List<AttendanceConsolidationResp> getAttendanceConsData(long userId, Integer pageNo, Integer pageSize, AttendConsReqBody reqBody, JdbcClient jdbcClient) {

        List<AttendanceConsolidationResp> resp = new ArrayList<>();

        List<AttendTimecardPersonRespBody> personRespBodies = new ArrayList<>();
        //Get persons
        if (reqBody.profileId() == -1)//direct/immediate reportees
        {
            personRespBodies = jdbcClient.sql(getPersonDirectReportees)
                    .param("userId", userId)
                    .param("startDate", reqBody.startDate())
                    .param("endDate", reqBody.endDate())
                    .param("offset", pageNo)
                    .param("pageSize", pageSize)
                    .query(AttendTimecardPersonRespBody.class)
                    .list();

        } else if (reqBody.profileId() == -2)//all reportees
        {
            personRespBodies = jdbcClient.sql(getPersonAllReportees)
                    .param("userId", userId)
                    .param("startDate", reqBody.startDate())
                    .param("endDate", reqBody.endDate())
                    .param("offset", pageNo)
                    .param("pageSize", pageSize)
                    .query(AttendTimecardPersonRespBody.class)
                    .list();

        } else {
            personRespBodies = jdbcClient.sql(getPersonsProfile)
                    .param("userId", userId)
                    .param("profileId", reqBody.profileId())
                    .param("startDate", reqBody.startDate())
                    .param("endDate", reqBody.endDate())
                    .param("offset", pageNo)
                    .param("pageSize", pageSize)
                    .query(AttendTimecardPersonRespBody.class)
                    .list();

        }


        List<Long> personList = personRespBodies.stream().map(AttendTimecardPersonRespBody::personId).collect(Collectors.toList());

        System.out.println("personList.size():"+ personList.size());

//        AttendanceConsolidationResp resp = null;
        if (!personRespBodies.isEmpty()) {
            List<AttendSchViolRespBody> schViolRespBodies = jdbcClient.sql(getSchViolations)
                    .param("personIds", personList)
                    .param("startDate", reqBody.startDate())
                    .param("endDate", reqBody.endDate())
                    .query(AttendSchViolRespBody.class)
                    .list();

            System.out.println("schViolRespBodies.size():"+schViolRespBodies.size());


            List<AttendActualLinesRespBody> actualLinesRespBodies = jdbcClient.sql(getActuals)
                    .param("personIds", personList)
                    .param("startDate", reqBody.startDate())
                    .param("endDate", reqBody.endDate())
                    .query(AttendActualLinesRespBody.class)
                    .list();

            System.out.println("actualLinesRespBodies.size():"+actualLinesRespBodies.size());


            List<AttendLeavesRespBody> leavesRespBodies = jdbcClient.sql(getLeaves)
                    .param("personIds", personList)
                    .param("startDate", reqBody.startDate())
                    .param("endDate", reqBody.endDate())
                    .query(AttendLeavesRespBody.class)
                    .list();

            System.out.println("leavesRespBodies.size():"+leavesRespBodies.size());


            List<AttendHolidaysRespBody> holidaysRespBodies = jdbcClient.sql(getHolidays)
                    .param("personIds", personList)
                    .param("startDate", reqBody.startDate())
                    .param("endDate", reqBody.endDate())
                    .query(AttendHolidaysRespBody.class)
                    .list();
            System.out.println("holidaysRespBodies.size():"+holidaysRespBodies.size());
//            resp = new AttendanceConsolidationResp();



            for (AttendTimecardPersonRespBody personRespBody : personRespBodies) {

                List<AttendSchViolRespBody> schViolResp = schViolRespBodies.stream().filter(attendSchViolRespBody -> Objects.equals(attendSchViolRespBody.personId(), personRespBody.personId())).collect(Collectors.toList());
                List<AttendActualLinesRespBody> actResp = actualLinesRespBodies.stream().filter(attendSchViolRespBody -> Objects.equals(attendSchViolRespBody.personId(), personRespBody.personId())).collect(Collectors.toList());
                List<AttendLeavesRespBody> leaveResp = leavesRespBodies.stream().filter(attendSchViolRespBody -> Objects.equals(attendSchViolRespBody.personId(), personRespBody.personId())).collect(Collectors.toList());
                List<AttendHolidaysRespBody> holidayResp = holidaysRespBodies.stream().filter(attendSchViolRespBody -> Objects.equals(attendSchViolRespBody.personId(), personRespBody.personId())).collect(Collectors.toList());




                AttendanceConsolidationResp attendanceConsolidationResp = new AttendanceConsolidationResp();

                attendanceConsolidationResp.setPersonRespBody(personRespBody);
                attendanceConsolidationResp.setSchViolRespBodies(schViolResp);
                attendanceConsolidationResp.setActualLinesRespBodies(actResp);
                attendanceConsolidationResp.setLeavesRespBodies(leaveResp);
                attendanceConsolidationResp.setHolidaysRespBodies(holidayResp);

                resp.add(
                        attendanceConsolidationResp
                );

            }


        }

        return resp;

    }


}
