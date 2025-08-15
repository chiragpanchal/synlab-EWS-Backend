package com.ewsv3.ews.commons.service;

import com.ewsv3.ews.commons.dto.masters.*;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.*;


import static com.ewsv3.ews.commons.utils.CommonUtils.*;
import static com.ewsv3.ews.masters.service.ServiceUtils.*;
import static com.ewsv3.ews.masters.service.ServiceUtils.deleteRosterReasonSql;
import static com.ewsv3.ews.masters.service.ServiceUtils.departmentJobSql;
import static com.ewsv3.ews.masters.service.ServiceUtils.emergencyTypeMasterSql;
import static com.ewsv3.ews.masters.service.ServiceUtils.locationMasterSql;
import static com.ewsv3.ews.masters.service.ServiceUtils.onCallTypeMasterSql;
import static com.ewsv3.ews.masters.service.ServiceUtils.workDurationSql;

@Service
public class CommonService {

    public List<PersonDtoLov> getPerson(PersonRequestDto dto, JdbcClient jdbcClient) {

        return jdbcClient.sql(sqlActivePerson)
                .param("strPerson", dto.strPerson())
                .query(PersonDtoLov.class)
                .list();

    }

    public List<DepartmentDtoLov> getDepartments(JdbcClient jdbcClient) {


        List<DepartmentDtoLov> departmentDtoLovs = jdbcClient.sql(sqlDepartments)
                .query(DepartmentDtoLov.class)
                .list();
        return departmentDtoLovs;

    }

    public List<JobDtoLov> getJobs(JdbcClient jdbcClient) {


        return jdbcClient.sql(sqlJobs)
                .query(JobDtoLov.class)
                .list();

    }

    public List<ProjectsDtoLov> getProjects(ProjectRequestDto dto, JdbcClient jdbcClient) {

        return jdbcClient.sql(sqlProjects)
                .param("strProject", dto.strProject())
                .query(ProjectsDtoLov.class)
                .list();

    }

    public List<JobFamilyDtoLov> getJobFamily(JdbcClient jdbcClient) {
        return jdbcClient.sql(sqlJobFamily)
                .query(JobFamilyDtoLov.class)
                .list();

    }

    public List<BusinessUnitsDtoLov> getBusinessUnits(JdbcClient jdbcClient) {
        return jdbcClient.sql(sqlBusinessUnits)
                .query(BusinessUnitsDtoLov.class)
                .list();
    }

    public List<LegalEntityDtoLov> getLegalEntity(JdbcClient jdbcClient) {
        return jdbcClient.sql(sqlLegalEntity)
                .query(LegalEntityDtoLov.class)
                .list();
    }

    public List<EmployeeTypesDtoLov> getEmployeeTypes(JdbcClient jdbcClient) {
        return jdbcClient.sql(sqlEmployeeTypes)
                .query(EmployeeTypesDtoLov.class)
                .list();
    }

    public List<EmploymentTypesDtoLov> getEmploymentTypes(JdbcClient jdbcClient) {
        return jdbcClient.sql(sqlEmploymentTypes)
                .query(EmploymentTypesDtoLov.class)
                .list();
    }

    public List<GenderDtoLov> getGenders(JdbcClient jdbcClient) {
        return jdbcClient.sql(sqlGender)
                .query(GenderDtoLov.class)
                .list();
    }

    public List<NationalityDtoLov> getNationality(JdbcClient jdbcClient) {
        return jdbcClient.sql(sqlNationality)
                .query(NationalityDtoLov.class)
                .list();
    }

    public List<ReligionDtoLov> getReligion(JdbcClient jdbcClient) {
        return jdbcClient.sql(sqlReligion)
                .query(ReligionDtoLov.class)
                .list();
    }

    public List<ShiftTypeDtoLov> getShiftTypes(JdbcClient jdbcClient) {
        return jdbcClient.sql(sqlShiftType)
                .query(ShiftTypeDtoLov.class)
                .list();
    }

    public AllMasterDtoLov allMasterDtoLov(JdbcClient jdbcClient) {

        List<DepartmentDtoLov> departments = getDepartments(jdbcClient);
        System.out.println("allMasterDtoLov departments:" + departments);

        for (DepartmentDtoLov department : departments) {
            System.out.println("allMasterDtoLov department:" + department);
        }

        return new AllMasterDtoLov(
                departments,
                getJobs(jdbcClient),
                getJobFamily(jdbcClient),
                getBusinessUnits(jdbcClient),
                getLegalEntity(jdbcClient),
                getEmployeeTypes(jdbcClient),
                getEmploymentTypes(jdbcClient),
                getGenders(jdbcClient),
                getNationality(jdbcClient),
                getReligion(jdbcClient),
                getShiftTypes(jdbcClient)
        );
    }


}
