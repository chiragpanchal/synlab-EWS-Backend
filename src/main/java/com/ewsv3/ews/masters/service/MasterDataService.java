package com.ewsv3.ews.masters.service;

import com.ewsv3.ews.masters.dto.TimekeeperProfiles;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.ewsv3.ews.masters.service.ServiceUtils.sqlProfiles;


@Service
public class MasterDataService {

        // private final JdbcClient jdbcClient;
        //
        // public MasterDataService(JdbcClient jdbcClient) {
        // this.jdbcClient = jdbcClient;
        // }

        public List<TimekeeperProfiles> getTimekeeperProfiles(Long userId,JdbcClient jdbcClient) {

                // ServiceUtils serviceUtils = new ServiceUtils();

                Map<String, Object> profileParamMap = new HashMap<>();

                profileParamMap.put("userId", userId);

                List<TimekeeperProfiles> timekeeperProfilesList = jdbcClient.sql(sqlProfiles)
                                .params(profileParamMap)
                                .query(TimekeeperProfiles.class)
                                .list();

                return timekeeperProfilesList;

        }



}
