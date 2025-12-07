package com.ewsv3.ews.timesheets.service.bulk;

import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.timesheets.dto.TimesheetPageRequestBody;
import com.ewsv3.ews.timesheets.dto.bulk.*;
import com.ewsv3.ews.timesheets.dto.form.TimesheetDetails;
import com.ewsv3.ews.timesheets.service.form.TimesheetFormService;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ewsv3.ews.timesheets.service.bulk.TimesheetBulkUtils.*;

@Service
public class TimesheetBulkService {


    private final TimesheetFormService timesheetFormService;

    public TimesheetBulkService(TimesheetFormService timesheetFormService) {
        this.timesheetFormService = timesheetFormService;
    }


    public BulkTimesheetMasterDto getBulkMasters(Long userId, int page,
                                                 int size, TimesheetPageRequestBody requestBody, JdbcClient jdbcClient) {


        List<BulkTimesheetPersonDto> personDtos = jdbcClient.sql(sqlBulkPerson)
                .param("profileId", requestBody.profileId())
                .param("userId", userId)
                .param("startDate", requestBody.startDate())
                .param("endDate", requestBody.endDate())
                .param("offset", page)
                .param("pageSize", size)
                .query(BulkTimesheetPersonDto.class)
                .list();

        List<BulkTimesheetDepartmentDto> departmentDtos = jdbcClient.sql(sqlBulkDepartments)
                .param("profileId", requestBody.profileId())
                .param("userId", userId)
                .param("startDate", requestBody.startDate())
                .param("endDate", requestBody.endDate())
                .param("offset", page)
                .param("pageSize", size)
                .query(BulkTimesheetDepartmentDto.class)
                .list();

        List<BulkTimesheetJobDto> jobDtos = jdbcClient.sql(sqlBulkJobs)
                .param("profileId", requestBody.profileId())
                .param("userId", userId)
                .param("startDate", requestBody.startDate())
                .param("endDate", requestBody.endDate())
                .param("offset", page)
                .param("pageSize", size)
                .query(BulkTimesheetJobDto.class)
                .list();

        List<BulkTimesheetProjectDto> projectDtos = jdbcClient.sql(sqlBulkProjects)
                .param("profileId", requestBody.profileId())
                .param("userId", userId)
                .param("startDate", requestBody.startDate())
                .param("endDate", requestBody.endDate())
                .param("offset", page)
                .param("pageSize", size)
                .query(BulkTimesheetProjectDto.class)
                .list();

        List<BulkTimesheetExpTypeDto> expTypeDtos = jdbcClient.sql(sqlBulkExpTypes)
                .param("profileId", requestBody.profileId())
                .param("userId", userId)
                .param("startDate", requestBody.startDate())
                .param("endDate", requestBody.endDate())
                .param("offset", page)
                .param("pageSize", size)
                .query(BulkTimesheetExpTypeDto.class)
                .list();

        List<BulkTimesheetPaycodeDto> paycodeDtos = jdbcClient.sql(sqlBulkPayCodes)
                .param("profileId", requestBody.profileId())
                .param("userId", userId)
                .param("startDate", requestBody.startDate())
                .param("endDate", requestBody.endDate())
                .query(BulkTimesheetPaycodeDto.class)
                .list();

        BulkTimesheetMasterDto bulkTimesheetMasterDto = new BulkTimesheetMasterDto();

        //System.out.println("getBulkMasters personDtos.size() :" + personDtos.size());
        //System.out.println("getBulkMasters departmentDtos.size() :" + departmentDtos.size());
        //System.out.println("getBulkMasters jobDtos.size() :" + jobDtos.size());
        //System.out.println("getBulkMasters projectDtos.size() :" + projectDtos.size());
        //System.out.println("getBulkMasters expTypeDtos.size() :" + expTypeDtos.size());
        //System.out.println("getBulkMasters paycodeDtos.size() :" + paycodeDtos.size());

        bulkTimesheetMasterDto.setPersonDtos(personDtos);
        bulkTimesheetMasterDto.setDepartmentDtos(departmentDtos);
        bulkTimesheetMasterDto.setJobDtos(jobDtos);
        bulkTimesheetMasterDto.setProjectDtos(projectDtos);
        bulkTimesheetMasterDto.setExpTypeDtos(expTypeDtos);
        bulkTimesheetMasterDto.setPaycodeDtos(paycodeDtos);

        return bulkTimesheetMasterDto;


    }

    public List<BulkTimesheetDetails> getBulkTimesheets(Long userId, TimesheetPageRequestBody requestBody, int page,
                                                        int size, JdbcClient jdbcClient) {

        //System.out.println("getBulkTimesheets requestBody :" + requestBody);
        //System.out.println("getBulkTimesheets page :" + page);
        //System.out.println("getBulkTimesheets size :" + size);

        List<BulkTimesheetDetails> detailsList = jdbcClient.sql(sqlBulkTimesheetDetails)
                .param("profileId", requestBody.profileId())
                .param("userId", userId)
                .param("startDate", requestBody.startDate())
                .param("endDate", requestBody.endDate())
                .param("offset", page)
                .param("pageSize", size)
                .query(BulkTimesheetDetails.class)
                .list();

        //System.out.println("getBulkTimesheets detailsList.size():" + detailsList.size());

        return detailsList;


    }

    public DMLResponseDto saveBulkTimesheets(Long currentUserId, List<TimesheetDetails> requestBody, JdbcClient jdbcClient) {


        for (TimesheetDetails details : requestBody) {
            //System.out.println("saveBulkTimesheets details:" + details);
        }
        DMLResponseDto dmlResponseDto = this.timesheetFormService.saveTimesheets(currentUserId, requestBody, jdbcClient);

        return dmlResponseDto;
//        return new DMLResponseDto("S", "Saved successfully");
    }
}
