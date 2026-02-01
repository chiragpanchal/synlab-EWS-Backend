package com.ewsv3.ews.reports.service.attendaneDetails;

import com.ewsv3.ews.reports.controller.ReportController;
import com.ewsv3.ews.reports.dto.attendaneDetails.AttendanceDetailsDto;
import com.ewsv3.ews.reports.dto.attendaneDetails.AttendanceDetailsReqDto;
import com.ewsv3.ews.reports.dto.reportMasters.ReportPersonDto;
import com.ewsv3.ews.reports.dto.reportMasters.ReportPersonReqDto;
import com.ewsv3.ews.reports.service.reportMaters.ReportMasterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.ewsv3.ews.reports.service.attendaneDetails.AttendanceDetailsUtils.getAttendanceDetailsSQL;


@Service
public class AttendanceDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    private final ReportMasterService reportMasterService;

    public AttendanceDetailsService(ReportMasterService reportMasterService) {
        this.reportMasterService = reportMasterService;
    }

    public List<AttendanceDetailsDto> getAttendanceDetails(Long userId, Long personId, int page,
                                                           int size, AttendanceDetailsReqDto reqDto, JdbcClient jdbcClient) {

        ReportPersonReqDto reportPersonReqDto = new ReportPersonReqDto(reqDto.profileId(), reqDto.startDate(), reqDto.endDate(), reqDto.text(), 0L, 0L);
        logger.info("getAttendanceDetails - Entry - reportPersonReqDto: {}", reportPersonReqDto);
        logger.info("getAttendanceDetails - Entry - userId: {}", userId);
        logger.info("getAttendanceDetails - Entry - personId: {}", personId);
        List<ReportPersonDto> personList = this.reportMasterService.getReportPerson(userId, personId, page, size, reportPersonReqDto, jdbcClient);

        if (!personList.isEmpty()) {
            List<Long> collectedPersonIds = personList.stream().map(ReportPersonDto::personId).toList();

            List<AttendanceDetailsDto> attendanceDetailsDtoList = jdbcClient.sql(getAttendanceDetailsSQL)
                    .param("startDate", reqDto.startDate())
                    .param("endDate", reqDto.endDate())
                    .param("personIds", collectedPersonIds)
                    .param("departmentId", reqDto.departmentId())
                    .param("jobTitleId", reqDto.jobTitleId())
                    .query(AttendanceDetailsDto.class)
                    .list();
            return attendanceDetailsDtoList;
        }
        return new ArrayList<>();


    }

}
