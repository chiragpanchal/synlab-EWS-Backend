package com.ewsv3.ews.masters.service;

import com.ewsv3.ews.masters.dto.*;
import com.ewsv3.ews.rosters.dto.rosters.RosterMasters;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.ewsv3.ews.masters.service.ServiceUtils.*;


@Service
public class MasterDataService {

    // private final JdbcClient jdbcClient;
    //
    // public MasterDataService(JdbcClient jdbcClient) {
    // this.jdbcClient = jdbcClient;
    // }

    public List<TimekeeperProfiles> getTimekeeperProfiles(Long userId, JdbcClient jdbcClient) {

        // ServiceUtils serviceUtils = new ServiceUtils();

        Map<String, Object> profileParamMap = new HashMap<>();

        profileParamMap.put("userId", userId);

        List<TimekeeperProfiles> timekeeperProfilesList = jdbcClient.sql(sqlProfiles)
                .params(profileParamMap)
                .query(TimekeeperProfiles.class)
                .list();

        return timekeeperProfilesList;

    }

    public RosterMasters getRosterMasters(Long userId, Long profileId, Date startDate, Date endDate,
                                          JdbcClient jdbcClient) {

        // ServiceUtils serviceUtils = new ServiceUtils();

        Map<String, Object> objectMap = new HashMap<>();

        objectMap.put("userId", userId);
        objectMap.put("profileId", profileId);
        objectMap.put("startDate", startDate);
        objectMap.put("endDate", endDate);

        Map<String, Long> userProfileMap = new HashMap<>();
        userProfileMap.put("userId", userId);
        userProfileMap.put("profileId", profileId);

        System.out.println("objectMap:%s\n" + objectMap);

        // getting person list
        List<PersonDto> personDtoList = jdbcClient.sql(personMasterSql)
                .params(objectMap)
                .query(PersonDto.class)
                .list();
        // System.out.printf("\npersonDtoList completed");
        // System.out.printf("personDtoList
        // ---------------------------------------%s\n", personDtoList);

        // getting department list
        List<DepartmentDto> departmentDtoList = jdbcClient.sql(departmentMasterSql)
                .params(userProfileMap)
                .query(DepartmentDto.class)
                .list();
        // System.out.printf("\ndepartmentDtoList completed");
        // System.out.printf("departmentDtoList
        // ---------------------------------------%s\n", departmentDtoList);
        // getting job title list
        List<JobDto> jobDtoList = jdbcClient.sql(jobTitleMasterSql)
                .params(userProfileMap)
                .query(JobDto.class)
                .list();
        // System.out.printf("\njobDtoList completed");
        // System.out.printf("jobDtoList ---------------------------------------%s\n",
        // jobDtoList);
        // getting location list
        List<LocationDto> locationDtoList = jdbcClient.sql(locationMasterSql)
                .params(userProfileMap)
                .query(LocationDto.class)
                .list();
        // System.out.printf("\nlocationDtoList completed");
        // System.out.printf("locationDtoList
        // ---------------------------------------%s\n", locationDtoList);
        // getting on-call type list
        List<ValueSetDto> oncallDtoList = jdbcClient.sql(onCallTypeMasterSql)
                .query(ValueSetDto.class)
                .list();

        List<ValueSetDto> deleteRosterReasonList = jdbcClient.sql(deleteRosterReasonSql)
                .query(ValueSetDto.class)
                .list();

        // System.out.printf("\noncallDtoList completed");
        // System.out.printf("oncallDtoList
        // ---------------------------------------%s\n", oncallDtoList);
        // getting emergency type list
        List<ValueSetDto> emergencyTypeDtoList = jdbcClient.sql(emergencyTypeMasterSql)
                .query(ValueSetDto.class)
                .list();
        // System.out.printf("\nemergencyTypeDtoList completed");
        // System.out.printf("emergencyTypeDtoList
        // ---------------------------------------%s\n", emergencyTypeDtoList);
        // getting work duration type list
        List<WorkDurationDto> workDurationDtoList = jdbcClient.sql(workDurationSql)
                .query(WorkDurationDto.class)
                .list();
        // System.out.printf("\nworkDurationDtoList completed");
        System.out.println("\nworkDurationDtoList:\n" + workDurationDtoList);
        // System.out.printf("workDurationDtoList
        // initial---------------------------------------%s\n", workDurationDtoList);
        // getting work duration details type list
        // List<WorkDurationDetailsDto> workDurationDetailsDtoList =
        // this.jdbcClient.sql(serviceUtils.workDurationDetailsSql)
        // .query(WorkDurationDetailsDto.class)
        // .list();
        // System.out.printf("\nworkDurationDetailsDtoList completed");
        // System.out.printf("workDurationDetailsDtoList
        // ---------------------------------------%s\n", workDurationDetailsDtoList);
        // for (WorkDurationDto workDurationDto : workDurationDtoList) {
        //
        // List<WorkDurationDetailsDto> detailsDtos =
        // workDurationDetailsDtoList.stream().filter(detailsDto ->
        // detailsDto.workDurationId() == workDurationDto.getWorkDurationId()).toList();
        //
        // int indexOfWorkDuration = workDurationDtoList.indexOf(workDurationDto);
        // workDurationDtoList.get(indexOfWorkDuration).setDetailsDtoList(detailsDtos);
        //
        // }
        // System.out.printf("workDurationDtoList (with detail)
        // ---------------------------------------%s\n", workDurationDtoList);
        // getting work department-job details type list
        List<DepartmentJobDto> departmentJobDtoList = jdbcClient.sql(departmentJobSql)
                .params(userProfileMap)
                .query(DepartmentJobDto.class)
                .list();
        // System.out.printf("\ndepartmentJobDtoList completed");
        // System.out.printf("\ndepartmentJobDtoList
        // ---------------------------------------%s\n", departmentJobDtoList);
        RosterMasters rosterMasters = new RosterMasters(userId,
                new ArrayList<PersonDto>(personDtoList),
                new ArrayList<DepartmentDto>(departmentDtoList),
                new ArrayList<JobDto>(jobDtoList),
                new ArrayList<LocationDto>(locationDtoList),
                workDurationDtoList,
                new ArrayList<ValueSetDto>(oncallDtoList),
                new ArrayList<ValueSetDto>(emergencyTypeDtoList),
                new ArrayList<DepartmentJobDto>(departmentJobDtoList),
                new ArrayList<ValueSetDto>(deleteRosterReasonList));
        System.out.println("\n---------------------------------------rosterMasters %s\n" + rosterMasters);

        return rosterMasters;
    }

    public WorkStructureMasters getWorkStructureMasters(Long userId,UserProfileReqBody reqBody,
                                                 JdbcClient jdbcClient) {

        // ServiceUtils serviceUtils = new ServiceUtils();

        Map<String, Long> userProfileMap = new HashMap<>();
        userProfileMap.put("userId", userId);
        userProfileMap.put("profileId", reqBody.profileId());


        // getting department list
        List<DepartmentDto> departmentDtoList = jdbcClient.sql(departmentMasterSql)
                .params(userProfileMap)
                .query(DepartmentDto.class)
                .list();
        // System.out.printf("\ndepartmentDtoList completed");
        // System.out.printf("departmentDtoList
        // ---------------------------------------%s\n", departmentDtoList);
        // getting job title list
        List<JobDto> jobDtoList = jdbcClient.sql(jobTitleMasterSql)
                .params(userProfileMap)
                .query(JobDto.class)
                .list();
        // System.out.printf("\njobDtoList completed");
        // System.out.printf("jobDtoList ---------------------------------------%s\n",
        // jobDtoList);
        // getting location list
        List<LocationDto> locationDtoList = jdbcClient.sql(locationMasterSql)
                .params(userProfileMap)
                .query(LocationDto.class)
                .list();
        // System.out.printf("\nlocationDtoList completed");
        // System.out.printf("locationDtoList
        // ---------------------------------------%s\n", locationDtoList);
        // getting on-call type list

        List<DepartmentJobDto> departmentJobDtoList = jdbcClient.sql(departmentJobSql)
                .params(userProfileMap)
                .query(DepartmentJobDto.class)
                .list();
        // System.out.printf("\ndepartmentJobDtoList completed");
        // System.out.printf("\ndepartmentJobDtoList
        // ---------------------------------------%s\n", departmentJobDtoList);

        List<WorkDurationDto> workDurationDtoList = jdbcClient.sql(workDurationSql)
                .query(WorkDurationDto.class)
                .list();
        // System.out.printf("\nworkDurationDtoList completed");
        System.out.println("\nworkDurationDtoList:\n" + workDurationDtoList);

        WorkStructureMasters workStructureMasters = new WorkStructureMasters();
        workStructureMasters.setDepartmentDtoList((ArrayList<DepartmentDto>) departmentDtoList);
        workStructureMasters.setJobDtoList((ArrayList<JobDto>) jobDtoList);
        workStructureMasters.setLocationDtoList((ArrayList<LocationDto>) (locationDtoList));
        workStructureMasters.setDepartmentJobDtoList((ArrayList<DepartmentJobDto>) (departmentJobDtoList));
        workStructureMasters.setWorkDurationDtoList((ArrayList<WorkDurationDto>) (workDurationDtoList));

        System.out.println("work-structure-masters > departmentDtoList" + departmentDtoList);
        System.out.println("work-structure-masters > departmentJobDtoList" + departmentJobDtoList);
        System.out.println("work-structure-masters > workDurationDtoList" + workDurationDtoList);
        System.out.println("work-structure-masters > work-structure-masters" + workStructureMasters);


        System.out.println("\n---------------------------------------workStructureMasters %s\n" + workStructureMasters);

        return workStructureMasters;
    }


}
