package com.ewsv3.ews.shiftGroup.service;

import com.ewsv3.ews.accessprofiles.controller.AccessProfileController;
import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.masters.dto.ValueSetDto;
import com.ewsv3.ews.masters.dto.WorkDurationDto;
import com.ewsv3.ews.shiftGroup.dto.ShiftGoupMasters;
import com.ewsv3.ews.shiftGroup.dto.ShiftGroup;
import com.ewsv3.ews.shiftGroup.dto.ShiftGroupShifts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.ewsv3.ews.masters.service.ServiceUtils.*;
import static com.ewsv3.ews.shiftGroup.service.ShiftGroupUtils.*;

@Service
public class ShiftGroupService {

    private static final Logger logger = LoggerFactory.getLogger(AccessProfileController.class);

    public List<ShiftGroup> getShiftGroups(Long userId, JdbcClient jdbcClient) {

        List<ShiftGroup> shiftGroupList = jdbcClient.sql(getShiftGroupSQL)
                .param("shiftGroupId", 0)
                .query(ShiftGroup.class)
                .list();

        return shiftGroupList;

    }

    public ShiftGroup getShiftGroupFromId(Long userId, ShiftGroup shiftGroup, JdbcClient jdbcClient) {

        ShiftGroup group = jdbcClient.sql(getShiftGroupSQL)
                .param("shiftGroupId", shiftGroup.getShiftGroupId())
                .query(ShiftGroup.class)
                .optional()
                .orElse(null);

        if (group.getShiftGroupId() != null) {
            List<ShiftGroupShifts> groupShifts = jdbcClient.sql(getShiftGroupShiftsSQL)
                    .param("shiftGroupId", group.getShiftGroupId())
                    .param("shiftGroupWorkShiftId", 0)
                    .query(ShiftGroupShifts.class)
                    .list();

            group.setShiftGroupShifts(groupShifts);
        }

        return group;

    }


    public DMLResponseDto saveShiftGroup(Long userId, ShiftGroup shiftGroup, JdbcClient jdbcClient) {

        int updatedCount = 0;
        int insetedCount = 0;
        Long generatedShiftGroupId;

        logger.info("saveShiftGroup - shiftGroup.getShiftGroupId(): {}",
                shiftGroup.getShiftGroupId());

        if (shiftGroup.getShiftGroupId() == null || shiftGroup.getShiftGroupId() == 0) {
            generatedShiftGroupId = jdbcClient
                    .sql("SELECT shift_group_id_sq.NEXTVAL FROM dual")
                    .query(Long.class)
                    .single();

            logger.info("saveShiftGroup - generatedShiftGroupId: {}",
                    generatedShiftGroupId);

            insetedCount = jdbcClient.sql(insertShiftGroupSQL)
                    .param("shiftGroupId", generatedShiftGroupId)
                    .param("shiftGroupName", shiftGroup.getShiftGroupName())
                    .param("enable", shiftGroup.getEnable())
                    .param("createdBy", userId)
                    .param("lastUpdatedBy", userId)
                    .update();

            logger.info("saveShiftGroup - insetedCount: {}",
                    insetedCount);
        } else {
            generatedShiftGroupId = shiftGroup.getShiftGroupId();
            updatedCount = jdbcClient.sql(updateShiftGroupSQL)
                    .param("shiftGroupId", shiftGroup.getShiftGroupId())
                    .param("shiftGroupName", shiftGroup.getShiftGroupName())
                    .param("enable", shiftGroup.getEnable())
                    .param("lastUpdatedBy", userId)
                    .update();

            logger.info("saveShiftGroup - updatedCount: {}",
                    updatedCount);
        }


        if (insetedCount > 0 || updatedCount > 0) {
            for (ShiftGroupShifts shiftGroupShift : shiftGroup.getShiftGroupShifts()) {
                Long savedShiftGroupWorkShiftId = saveShiftGroupShift(userId, shiftGroupShift, generatedShiftGroupId, jdbcClient);
                logger.info("saveShiftGroup - savedShiftGroupWorkShiftId: {}",
                        savedShiftGroupWorkShiftId);
            }
            return new DMLResponseDto("S", "Shift Group updated successfully");
        } else {
            return new DMLResponseDto("S", "No record updated");
        }

    }

    public Long saveShiftGroupShift(Long userId, ShiftGroupShifts shiftGroupShifts, Long generatedShiftGroupId, JdbcClient jdbcClient) {

        if (shiftGroupShifts.shiftGroupWorkShiftId() == null || shiftGroupShifts.shiftGroupWorkShiftId() == 0) {
            Long generatedShiftGroupWorkShiftId = jdbcClient
                    .sql("SELECT shift_group_work_shift_id_sq.NEXTVAL FROM dual")
                    .query(Long.class)
                    .single();

            int inserted = jdbcClient.sql(insertShiftGroupShiftsSQL)
                    .param("shiftGroupWorkShiftId", generatedShiftGroupWorkShiftId)
                    .param("shiftGroupId", generatedShiftGroupId)
                    .param("workDurationId", shiftGroupShifts.workDurationId())
                    .param("onCall", shiftGroupShifts.onCall())
                    .param("emergency", shiftGroupShifts.emergency())
                    .param("createdBy", userId)
                    .param("lastUpdatedBy", userId)
                    .update();

            if (inserted > 0) {
                return generatedShiftGroupWorkShiftId;
            } else {
                return 0L;
            }


        } else {

            int updated = jdbcClient.sql(updateShiftGroupShiftsSQL)
                    .param("shiftGroupWorkShiftId", shiftGroupShifts.shiftGroupWorkShiftId())
                    .param("workDurationId", shiftGroupShifts.workDurationId())
                    .param("onCall", shiftGroupShifts.onCall())
                    .param("emergency", shiftGroupShifts.emergency())
                    .param("lastUpdatedBy", userId)
                    .update();

            if (updated > 0) {
                return shiftGroupShifts.shiftGroupWorkShiftId();
            } else {
                return 0L;
            }

        }

    }

    public DMLResponseDto deleteShiftGroupShift(Long userId, ShiftGroupShifts shiftGroupShifts, JdbcClient jdbcClient) {

        int deleted = jdbcClient.sql(deleteShiftGroupShiftsSQL)
                .param("shiftGroupWorkShiftId", shiftGroupShifts.shiftGroupWorkShiftId())
                .update();

        if (deleted > 0) {
            return new DMLResponseDto("S", "Shift Line deleted successfully");
        } else {
            return new DMLResponseDto("S", "No record updated");
        }
    }

    public List<ShiftGroupShifts> getShiftGroupShifts(Long userId, ShiftGroup shiftGroup, JdbcClient jdbcClient) {

        List<ShiftGroupShifts> shiftGroupShiftsList = jdbcClient.sql(getShiftGroupShiftsSQL)
                .param("shiftGroupId", shiftGroup.getShiftGroupId())
                .param("shiftGroupWorkShiftId", 0)
                .query(ShiftGroupShifts.class)
                .list();

        return shiftGroupShiftsList;

    }

    public ShiftGroupShifts getShiftGroupShiftFromID(Long userId, Long siftGroupId, Long shiftGroupShiftId, JdbcClient jdbcClient) {

        ShiftGroupShifts groupShifts = jdbcClient.sql(getShiftGroupShiftsSQL)
                .param("shiftGroupId", siftGroupId)
                .param("shiftGroupWorkShiftId", shiftGroupShiftId)
                .query(ShiftGroupShifts.class)
                .optional()
                .orElse(null);

        return groupShifts;

    }

    public ShiftGoupMasters getMasters(JdbcClient jdbcClient) {

        List<WorkDurationDto> workDurationDtoList = jdbcClient.sql(workDurationSql)
                .query(WorkDurationDto.class)
                .list();

        List<ValueSetDto> oncallDtoList = jdbcClient.sql(onCallTypeMasterSql)
                .query(ValueSetDto.class)
                .list();

        List<ValueSetDto> emergencyTypeDtoList = jdbcClient.sql(emergencyTypeMasterSql)
                .query(ValueSetDto.class)
                .list();

        return new ShiftGoupMasters(workDurationDtoList, oncallDtoList, emergencyTypeDtoList);


    }


}
