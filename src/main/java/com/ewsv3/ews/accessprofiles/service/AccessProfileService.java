package com.ewsv3.ews.accessprofiles.service;

import com.ewsv3.ews.accessprofiles.dto.AccessProfileLines;
import com.ewsv3.ews.accessprofiles.dto.AccessProfileResponse;
import com.ewsv3.ews.accessprofiles.dto.AccessProfiles;
import com.ewsv3.ews.accessprofiles.dto.UserProfileAssoc;
import com.ewsv3.ews.accessprofiles.dto.req.AccessProfileReq;
import com.ewsv3.ews.accessprofiles.dto.req.AssessProfileId;
import com.ewsv3.ews.accessprofiles.dto.resp.AccessProfileResp;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


}
