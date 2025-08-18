package com.ewsv3.ews.accessprofiles.service;

import com.ewsv3.ews.accessprofiles.dto.AccessProfileLines;
import com.ewsv3.ews.accessprofiles.dto.AccessProfileResponse;
import com.ewsv3.ews.accessprofiles.dto.AccessProfiles;
import com.ewsv3.ews.accessprofiles.dto.UserProfileAssoc;
import com.ewsv3.ews.accessprofiles.dto.req.AccessProfileReq;
import com.ewsv3.ews.accessprofiles.dto.req.AssessProfileId;
import com.ewsv3.ews.accessprofiles.dto.resp.AccessProfileResp;
import com.ewsv3.ews.commons.dto.DMLResponseDto;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.ewsv3.ews.accessprofiles.service.AccessProfileUtils.*;

@Service
public class AccessProfileService {

    public List<AccessProfileResp> getSearchedProfiles(Long userId, AccessProfileReq req, JdbcClient jdbcClient) {

        System.out.printf("getSearchedProfiles req:" + req);

        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("strPerson", req.strPerson());
        objectMap.put("strTimekeeper", req.strTimekeeper());
        objectMap.put("strProfileName", req.strProfileName());
        List<AccessProfileResp> accessProfileRespList = jdbcClient.sql(sqlSearchedProfiles)
                .params(objectMap)
                .query(AccessProfileResp.class)
                .list();

        System.out.printf("getSearchedProfiles accessProfileRespList.size():" + (long) accessProfileRespList.size());

        return accessProfileRespList;


    }

    public AccessProfileResponse getAccessProfile(Long userId, AssessProfileId req, JdbcClient jdbcClient) {

        AccessProfiles accessProfiles = jdbcClient.sql(sqlGetProfileFromProfileId)
                .param("profileId", req.profileId())
                .query(AccessProfiles.class)
                .single();

        List<AccessProfileLines> accessProfileLines = jdbcClient.sql(sqlGetProfileLinesFromProfileId)
                .param("profileId", req.profileId())
                .query(AccessProfileLines.class)
                .list();

        List<UserProfileAssoc> userProfileAssocs = jdbcClient.sql(sqlGetUserProfileAssocFromProfileId)
                .param("profileId", req.profileId())
                .query(UserProfileAssoc.class)
                .list();

        AccessProfileResponse accessProfileResponse = new AccessProfileResponse(
                accessProfiles,
                userProfileAssocs,
                accessProfileLines
        );

        return accessProfileResponse;
    }

    public void saveProfile(Long userId, AccessProfileResponse profileResponse, JdbcClient jdbcClient) {


//        save profile
        AccessProfiles profile = profileResponse.getAccessProfiles();

        long generatedAccessProfileId = 0L;

        AccessProfileResponse existingProfile = getAccessProfile(userId, new AssessProfileId(profile.profileId()), jdbcClient);

        System.out.println("saveProfile existingProfile:" + existingProfile.getAccessProfiles());
        System.out.println("saveProfile existingProfile.getAccessProfileLinesList():" + existingProfile.getAccessProfileLinesList());
        System.out.println("saveProfile existingProfile.getUserProfileAssocList():" + existingProfile.getUserProfileAssocList());

        if (profile.profileId() <= 0) {
            generatedAccessProfileId = jdbcClient
                    .sql("SELECT PROFILE_ID_SQ.NEXTVAL FROM dual")
                    .query(Long.class)
                    .single();

            int inserted = jdbcClient.sql(sqlInsertAccessProfile)
                    .param("profileId", generatedAccessProfileId)
                    .param("createdBy", userId)
                    .param("lastUpdatedBy", userId)
                    .param("skipApproval", 'N')
                    .param("profileName", profile.profileName())
                    .param("endDate", profile.endDate())
                    .param("startDate", profile.startDate())
                    .update();

            System.out.println("saveProfile existingProfile inserted:" + inserted);

        } else {
            generatedAccessProfileId = profile.profileId();

            if (!Objects.equals(existingProfile.getAccessProfiles().profileName(), profile.profileName())
                    || !Objects.equals(existingProfile.getAccessProfiles().startDate(), profile.startDate())
                    || !Objects.equals(existingProfile.getAccessProfiles().endDate(), profile.endDate())) {


                System.out.println("saveProfile existingProfile existingProfile.getAccessProfiles().profileName():" + existingProfile.getAccessProfiles().profileName());
                System.out.println("saveProfile existingProfile profile.profileName():" + profile.profileName());

                System.out.println("saveProfile existingProfile existingProfile.getAccessProfiles().startDate():" + existingProfile.getAccessProfiles().startDate());
                System.out.println("saveProfile existingProfile profile.startDate():" + profile.startDate());

                System.out.println("saveProfile existingProfile existingProfile.getAccessProfiles().endDate():" + existingProfile.getAccessProfiles().endDate());
                System.out.println("saveProfile existingProfile profile.endDate():" + profile.endDate());

                int updated = jdbcClient.sql(sqlUpdateAccessProfile)
                        .param("profileId", generatedAccessProfileId)
                        .param("lastUpdatedBy", userId)
                        .param("skipApproval", 'N')
                        .param("profileName", profile.profileName())
                        .param("endDate", profile.endDate())
                        .param("startDate", profile.startDate())
                        .update();

                System.out.println("saveProfile existingProfile updated:" + updated);
            }
        }


//        save profile lines
        List<AccessProfileLines> profileLines = profileResponse.getAccessProfileLinesList();
        long generatedAccessProfileLineId = 0L;
        for (AccessProfileLines profileLine : profileLines) {

//            if (profileLine.personId() == 0) {
            // NON PERSON lines

            if (profileLine.accessProfileLineId() == 0) {
                generatedAccessProfileLineId = jdbcClient
                        .sql("SELECT ACCESS_PROFILE_LINE_ID_SQ.NEXTVAL FROM dual")
                        .query(Long.class)
                        .single();

                jdbcClient.sql(sqlInsertAccessProfileLines)
                        .param("profileId", generatedAccessProfileId)
                        .param("createdBy", userId)
                        .param("nationality", profileLine.nationality())
                        .param("lastUpdatedBy", userId)
                        .param("jobId", profileLine.jobId())
                        .param("departmentId", profileLine.departmentId())
                        .param("shiftType", profileLine.shiftType())
                        .param("legalEntityId", profileLine.legalEntityId())
                        .param("jobFamily", profileLine.jobFamily())
                        .param("employeeCatg", profileLine.employeeCatg())
                        .param("businessUnitId", profileLine.businessUnitId())
                        .param("accessProfileLineId", generatedAccessProfileLineId)
                        .param("religion", profileLine.religion())
                        .param("personId", profileLine.personId())
                        .param("employeeTypeId", profileLine.employeeTypeId())
                        .param("gender", profileLine.gender())
                        .param("projectId", profileLine.projectId())
                        .param("gradeId", profileLine.gradeId())
                        .param("includeExcludeFlag", profileLine.includeExcludeFlag())
                        .update();
                ;


            } else {
                generatedAccessProfileLineId = profileLine.accessProfileLineId();


                long finalGeneratedAccessProfileLineId = generatedAccessProfileLineId;
                AccessProfileLines matchingLine = existingProfile.getAccessProfileLinesList()
                        .stream()
                        .filter(line -> line.accessProfileLineId() == finalGeneratedAccessProfileLineId)
                        .findFirst()
                        .orElse(null);

                assert matchingLine != null;
                System.out.println("saveProfile accessProfileLine matchingLine.employeeTypeId():" + matchingLine.employeeTypeId());
                System.out.println("saveProfile accessProfileLine profileLine.employeeTypeId():" + profileLine.employeeTypeId());
                System.out.println("saveProfile accessProfileLine generatedAccessProfileLineId:" + generatedAccessProfileLineId);


                if (!Objects.equals(matchingLine.personId(), profileLine.personId())
                        || !Objects.equals(matchingLine.jobId(), profileLine.jobId())
                        || !Objects.equals(matchingLine.departmentId(), profileLine.departmentId())
                        || !Objects.equals(matchingLine.shiftType(), profileLine.shiftType())
                        || !Objects.equals(matchingLine.legalEntityId(), profileLine.legalEntityId())
                        || !Objects.equals(matchingLine.jobFamily(), profileLine.jobFamily())
                        || !Objects.equals(matchingLine.employeeCatg(), profileLine.employeeCatg())
                        || !Objects.equals(matchingLine.businessUnitId(), profileLine.businessUnitId())
                        || !Objects.equals(matchingLine.religion(), profileLine.religion())
                        || !Objects.equals(matchingLine.employeeTypeId(), profileLine.employeeTypeId())
                        || !Objects.equals(matchingLine.gender(), profileLine.gender())
                        || !Objects.equals(matchingLine.projectId(), profileLine.projectId())
                        || !Objects.equals(matchingLine.gradeId(), profileLine.gradeId())
                        || !Objects.equals(matchingLine.includeExcludeFlag(), profileLine.includeExcludeFlag())
                ) {


                    int updated = jdbcClient.sql(sqlUpdateAccessProfileLine)
                            .param("nationality", profileLine.nationality())
                            .param("lastUpdatedBy", userId)
                            .param("jobId", profileLine.jobId())
                            .param("departmentId", profileLine.departmentId())
                            .param("shiftType", profileLine.shiftType())
                            .param("legalEntityId", profileLine.legalEntityId())
                            .param("jobFamily", profileLine.jobFamily())
                            .param("employeeCatg", profileLine.employeeCatg())
                            .param("businessUnitId", profileLine.businessUnitId())
                            .param("religion", profileLine.religion())
                            .param("personId", profileLine.personId())
                            .param("employeeTypeId", profileLine.employeeTypeId())
                            .param("gender", profileLine.gender())
                            .param("projectId", profileLine.projectId())
                            .param("gradeId", profileLine.gradeId())
                            .param("includeExcludeFlag", profileLine.includeExcludeFlag())
                            .param("accessProfileLineId", generatedAccessProfileLineId)
                            .update();

                    System.out.println("saveProfile accessProfileLine updated:" + updated);
                }
            }

        }


        List<UserProfileAssoc> profileAssocList = profileResponse.getUserProfileAssocList();
        long generatedUserProfileAssocIdId = 0L;

        for (
                UserProfileAssoc profileAssoc : profileAssocList) {
            if (profileAssoc.userProfileAssocId() == 0) {
                generatedUserProfileAssocIdId = jdbcClient
                        .sql("SELECT USER_PROFILE_ASSOC_ID_SQ.NEXTVAL FROM dual")
                        .query(Long.class)
                        .single();

                jdbcClient.sql(sqlInsertUserProfileAssoc)
                        .param("profileId", generatedAccessProfileId)
                        .param("createdBy", userId)
                        .param("lastUpdatedBy", userId)
                        .param("userId", profileAssoc.userId())
                        .param("userProfileAssocId", generatedUserProfileAssocIdId)
                        .param("canCreate", profileAssoc.canCreate())
                        .param("userType", profileAssoc.userType())
                        .update();


            } else {


                UserProfileAssoc existingProfileAssoc = existingProfile.getUserProfileAssocList()
                        .stream()
                        .filter(assoc -> Objects.equals(assoc.userProfileAssocId(), profileAssoc.userProfileAssocId()))
                        .findFirst()
                        .orElse(null);

                assert existingProfileAssoc != null;
                if (!Objects.equals(existingProfileAssoc.userId(), profileAssoc.userId())
                        || !Objects.equals(existingProfileAssoc.canCreate(), profileAssoc.canCreate())
                        || !Objects.equals(existingProfileAssoc.userType(), profileAssoc.userType())
                ) {
                    int updated = jdbcClient.sql(sqlUpdateUserProfileAssoc)
                            .param("lastUpdatedBy", userId)
                            .param("userId", profileAssoc.userId())
                            .param("userProfileAssocId", generatedUserProfileAssocIdId)
                            .param("canCreate", profileAssoc.canCreate())
                            .param("userType", profileAssoc.userType())
                            .update();

                    System.out.println("saveProfile UserProfileAssoc updated:" + updated);
                }


            }
        }

    }

    public DMLResponseDto deleteUserProfileAssoc(Long userId, UserProfileAssoc userProfileAssoc, JdbcClient jdbcClient) {
        int deleted = jdbcClient.sql(sqlDeleteUserProfileAssoc)
                .param("userProfileAssocId", userProfileAssoc.userProfileAssocId())
                .update();

        return new DMLResponseDto("S", deleted + " Profile Owner lines deleted!");
    }


    public DMLResponseDto deleteProfileLine(Long userId, AccessProfileLines profileLines, JdbcClient jdbcClient) {
        int deleted = jdbcClient.sql(sqlDeleteAccessProfileLine)
                .param("accessProfileLineId", profileLines.accessProfileLineId())
                .update();

        return new DMLResponseDto("S", deleted + " Profile Criteria deleted!");
    }

    public DMLResponseDto deleteProfile(Long userId, AccessProfiles accessProfiles, JdbcClient jdbcClient) {
        int deleted = jdbcClient.sql(sqlDeleteAccessProfile)
                .param("profileId", accessProfiles.profileId())
                .update();

        return new DMLResponseDto("S", deleted + " Profile deleted!");
    }
}
