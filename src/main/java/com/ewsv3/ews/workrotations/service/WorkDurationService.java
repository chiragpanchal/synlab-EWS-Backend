package com.ewsv3.ews.workrotations.service;

import com.ewsv3.ews.workrotations.dto.*;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WorkDurationService {

    private final JdbcClient jdbcClient;

    public WorkDurationService(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }


//    public List<WorkDuration> getWorkDurations(Long userId, WorkDurationRequestBody requestBody) {
//        try {
//            System.out.println("getWorkDurations > requestBody: " + requestBody);
//            String searchText = requestBody.searchText() != null ? requestBody.searchText() : "";
//            String activeOnly = requestBody.activeOnly() != null ? requestBody.activeOnly() : "N";
//
//            System.out.println("getWorkDurations service > searchText: " + searchText);
//            System.out.println("getWorkDurations service > activeOnly: " + activeOnly);
//            Long workDurationId = (requestBody.workDurationId() != null && requestBody.workDurationId() == 0) ? null
//                    : requestBody.workDurationId();
//
//            System.out.println("getWorkDurations service > workDurationId: " + workDurationId);
//
//            List<WorkDuration> list = jdbcClient
//                    .sql(workrotationsUtils.GetWorkDurationSql)
//                    .param("workDurationId", workDurationId)
//                    .param("searchText", "%" + searchText.toLowerCase() + "%")
//                    .param("searchText", "%" + searchText.toLowerCase() + "%")
//                    .param("activeOnly", activeOnly)
//                    .param("activeOnly", activeOnly)
//                    .query(WorkDuration.class)
//                    .list();
//
//            System.out.println("getWorkDurations service > result count: " + list.size());
//            return list;
//        } catch (Exception e) {
//            System.out.println("Error getWorkDurations service : " + e.getMessage());
//            e.printStackTrace();
//            return null;
//        }
//    }


    public List<WorkDuration> getWorkDurations(Long userId, WorkDurationRequestBody requestBody) {

        try {
            System.out.println("getWorkDurations > requestBody: " + requestBody);
            String searchText = requestBody.searchText() != null ? requestBody.searchText() : "";
            String activeOnly = requestBody.activeOnly() != null ? requestBody.activeOnly() : "N";

            System.out.println("getWorkDurations service > searchText: " + searchText);
            System.out.println("getWorkDurations service > activeOnly: " + activeOnly);
//            Long workDurationId = (requestBody.workDurationId() != null && requestBody.workDurationId() == 0) ? null
//                    : requestBody.workDurationId();

            System.out.println("getWorkDurations service > workDurationId: " + requestBody.workDurationId());

             Map<String, Object> params = Map.of(
             "workDurationId",requestBody.workDurationId(),
             "searchText", "%" + searchText + "%",
             "activeOnly", activeOnly);

            System.out.println("getWorkDurations service > params: " + params);

            List<WorkDuration> list = jdbcClient
                    .sql(workrotationsUtils.GetWorkDurationSql)
                    .params(params)
                    .query(WorkDuration.class)
                    .list();

            return list;
        } catch (Exception e) {
            System.out.println("Error getWorkDurations service : " + e.getMessage());
            System.out.println("Error getWorkDurations service : " + e);
            return null;
        }

    }

    public List<WorkRotation> getWorkRotations(Long userId, WorkRotationRequestBody requestBody) {

        System.out.println("getWorkRotations service > requestBody: " + requestBody);
        List<WorkRotation> list = jdbcClient
                .sql(workrotationsUtils.GetWorkRotationSql)
//                .param("workRotationId", requestBody.workRotationId())
                .param("workRotationId", requestBody.workRotationId() != null ? requestBody.workRotationId() : 0)
                .query(WorkRotation.class)
                .list();

        return list;

    }

    public List<WorkRotationLine> getWorkRotationLines(Long userId, Long workRotationId) {

        System.out.println("getWorkRotationLines service > workRotationId: " + workRotationId);

        // Map<String, Object> params = Map.of("workRotationId", workRotationId);

        List<WorkRotationLine> list = jdbcClient
                .sql(workrotationsUtils.GetWorkRotationLineSql)
                .param("workRotationId", workRotationId)
                .query(WorkRotationLine.class)
                .list();

        return list;

    }

    public WorkDuration saveWorkDuration(Long userId, WorkDuration workDuration) {
        if (workDuration == null) {
            throw new IllegalArgumentException("WorkDuration cannot be null");
        }

        if (workDuration.workDurationId() == null) { // inserting
            // Logic to insert a new WorkDuration
            System.out.println("Saving new WorkDuration: " + workDuration);

            // Retrieve the generated workDurationId from the sequence
            Long generatedId = this.jdbcClient
                    .sql("SELECT WORK_DURATION_ID_SQ.NEXTVAL FROM dual")
                    .query(Long.class)
                    .single();

            Integer insCounts = this.jdbcClient
                    .sql(workrotationsUtils.InsertWorkDurationSql)
                    .param("work_duration_id", generatedId)
                    .param("work_duration_code", workDuration.workDurationCode())
                    .param("work_duration_name", workDuration.workDurationName())
                    .param("valid_from", workDuration.validFrom())
                    .param("valid_to", workDuration.validTo())
                    .param("time_start", workDuration.timeStart())
                    .param("break_start", workDuration.breakStart())
                    .param("break_end", workDuration.breakEnd())
                    .param("time_end", workDuration.timeEnd())
                    .param("enterprise_id", workDuration.enterpriseId())
                    .param("mon", workDuration.mon())
                    .param("tue", workDuration.tue())
                    .param("wed", workDuration.wed())
                    .param("thu", workDuration.thu())
                    .param("fri", workDuration.fri())
                    .param("sat", workDuration.sat())
                    .param("sun", workDuration.sun())
                    .param("color_code", workDuration.colorCode())
                    .param("duration", workDuration.duration())
                    .param("work_duration_category_id", workDuration.workDurationCategoryId())
                    .param("exception_events", workDuration.exceptionEvents())
                    .param("min_work_hrs", workDuration.minWorkHrs())
                    .param("max_work_hrs", workDuration.maxWorkHrs())
                    .param("work_unit", workDuration.workUnit())
                    .param("hcm_schedule_id", workDuration.hcmScheduleId())
                    .param("eroster_code", workDuration.erosterCode())
                    .param("break_mins", workDuration.breakMins())
                    .param("created_by", userId)
                    .param("created_on", new java.sql.Timestamp(System.currentTimeMillis()))
                    .param("last_updated_by", userId)
                    .param("last_update_date", new java.sql.Timestamp(System.currentTimeMillis()))
                    .update();

            // Set the generated ID back to the workDuration object (if possible)
            workDuration = getWorkDurations(userId, new WorkDurationRequestBody(generatedId, null, null)).get(0);
            System.out.println("Inserted WorkDuration,  insCounts: " + insCounts);

            System.out.println("Inserted WorkDuration with ID: " + workDuration.workDurationId());
        } else { // updating
            System.out.println("Updating WorkDuration: " + workDuration);

            Integer updCounts = this.jdbcClient
                    .sql(workrotationsUtils.updateWorkDurationSql)
                    .param("work_duration_id", workDuration.workDurationId())
                    .param("work_duration_code", workDuration.workDurationCode())
                    .param("work_duration_name", workDuration.workDurationName())
                    .param("valid_from", workDuration.validFrom())
                    .param("valid_to", workDuration.validTo())
                    .param("time_start", workDuration.timeStart())
                    .param("break_start", workDuration.breakStart())
                    .param("break_end", workDuration.breakEnd())
                    .param("time_end", workDuration.timeEnd())
                    .param("enterprise_id", workDuration.enterpriseId())
                    .param("mon", workDuration.mon())
                    .param("tue", workDuration.tue())
                    .param("wed", workDuration.wed())
                    .param("thu", workDuration.thu())
                    .param("fri", workDuration.fri())
                    .param("sat", workDuration.sat())
                    .param("sun", workDuration.sun())
                    .param("color_code", workDuration.colorCode())
                    .param("duration", workDuration.duration())
                    .param("work_duration_category_id", workDuration.workDurationCategoryId())
                    .param("exception_events", workDuration.exceptionEvents())
                    .param("min_work_hrs", workDuration.minWorkHrs())
                    .param("max_work_hrs", workDuration.maxWorkHrs())
                    .param("work_unit", workDuration.workUnit())
                    .param("hcm_schedule_id", workDuration.hcmScheduleId())
                    .param("eroster_code", workDuration.erosterCode())
                    .param("break_mins", workDuration.breakMins())
                    .param("last_updated_by", userId)
                    .param("last_update_date", new java.sql.Timestamp(System.currentTimeMillis()))
                    .update();

            System.out.println("Updated WorkDuration, updCounts: " + updCounts);

            // Logic to update an existing WorkDuration
            System.out.println("Updating WorkDuration with ID: " + workDuration.workDurationId());
        }
        return workDuration; // Return the saved WorkDuration object
    }

    public Integer deleteWorkDuration(Long userId, WorkDuration workDuration) {
        if (workDuration == null || workDuration.workDurationId() == null) {
            throw new IllegalArgumentException("WorkDuration or workDurationId cannot be null");
        }

        System.out.println("Deleting WorkDuration with ID: " + workDuration.workDurationId());

        Integer delCounts = this.jdbcClient
                .sql(workrotationsUtils.deleteWorkDurationSql)
                .param("work_duration_id", workDuration.workDurationId())
                .update();

        System.out.println("Deleted WorkDuration, delCounts: " + delCounts);

        return delCounts; // Return the deleted WorkDuration object
    }

    public WorkRotation saveWorkRotation(Long userId, WorkRotation workRotation) {

        if (workRotation == null) {
            throw new IllegalArgumentException("WorkRotation cannot be null");
        }

        Integer iterations = workRotation.iterations();

        // 2. Apply your business logic: if the value is 0, treat it as null for the
        // database.
        // This now correctly handles the case where 'iterations' is already null.
        Integer iterationForDb = (iterations != null && iterations.equals(0)) ? null : iterations;

        System.out.println("Saving new WorkRotation: iteration: " + iterationForDb);

        System.out.println("Saving new WorkRotation: iterationForDb: " + iterationForDb);
        if (workRotation.workRotationId() == null || workRotation.workRotationId() == 0 ) { // inserting
            // Logic to insert a new WorkRotation
            System.out.println("Saving new WorkRotation: " + workRotation);

            // Retrieve the generated workRotationId from the sequence
            Long generatedId = this.jdbcClient
                    .sql("SELECT WORK_ROTATION_ID_SQ.NEXTVAL FROM dual")
                    .query(Long.class)
                    .single();

            Integer insCounts = this.jdbcClient
                    .sql(workrotationsUtils.InsertWorkRotationSql)
                    .param("work_rotation_id", generatedId)
                    .param("work_rotation_name", workRotation.workRotationName())
                    .param("start_date", workRotation.startDate())
                    .param("iterations", iterationForDb)
                    .param("expiry_date", workRotation.expiryDate())
                    .param("color_code", workRotation.colorCode())
                    .param("forever_flag", workRotation.foreverFlag())
                    .param("created_by", userId)
                    .param("created_on", new java.sql.Timestamp(System.currentTimeMillis()))
                    .param("last_updated_by", userId)
                    .param("last_update_date", new java.sql.Timestamp(System.currentTimeMillis()))
                    .update();

            // Set the generated ID back to the workRotation object (if possible)
            workRotation = getWorkRotations(userId, new WorkRotationRequestBody(generatedId)).stream()
                    .filter(wr -> wr.workRotationId().equals(generatedId))
                    .findFirst()
                    .orElse(workRotation);
            System.out.println("Inserted WorkRotation, insCounts: " + insCounts);

            System.out.println("Inserted WorkRotation with ID: " + workRotation.workRotationId());
        } else { // updating
            System.out.println("Updating WorkRotation: " + workRotation);

            Integer updCounts = this.jdbcClient
                    .sql(workrotationsUtils.updateWorkRotationSql)
                    .param("work_rotation_id", workRotation.workRotationId())
                    .param("work_rotation_name", workRotation.workRotationName())
                    .param("start_date", workRotation.startDate())
                    .param("iterations", iterationForDb)
                    .param("expiry_date", workRotation.expiryDate())
                    .param("color_code", workRotation.colorCode())
                    .param("forever_flag", workRotation.foreverFlag())
                    .param("last_updated_by", userId)
                    .param("last_update_date", new java.sql.Timestamp(System.currentTimeMillis()))
                    .update();

            System.out.println("Updated WorkDuration, updCounts: " + updCounts);

        }

        return workRotation; // Return the saved WorkRotation object
    }

    public Integer deleteWorkRotation(Long userId, WorkRotation workRotation) {
        if (workRotation == null || workRotation.workRotationId() == null) {
            throw new IllegalArgumentException("WorkRotation or workRotationId cannot be null");
        }

        System.out.println("Deleting WorkRotation with ID: " + workRotation.workRotationId());

        Integer delCounts = this.jdbcClient
                .sql(workrotationsUtils.deleteWorkRotationSql)
                .param("work_rotation_id", workRotation.workRotationId())
                .update();

        System.out.println("Deleted WorkRotation, delCounts: " + delCounts);

        return delCounts; // Return the deleted WorkRotation object
    }

    public WorkRotationLine saveWorkRotationLine(Long userId, WorkRotationLine workRotationLine) {
        if (workRotationLine == null) {
            throw new IllegalArgumentException("WorkRotationLine cannot be null");
        }

        if (workRotationLine.workRotationLineId() == null) { // inserting
            // Logic to insert a new WorkRotationLine
            System.out.println("Saving new WorkRotationLine: " + workRotationLine);

            // Retrieve the generated workRotationLineId from the sequence
            Long generatedId = this.jdbcClient
                    .sql("SELECT WORK_ROTATION_LINE_ID_SQ.NEXTVAL FROM dual")
                    .query(Long.class)
                    .single();

            Integer insCounts = this.jdbcClient
                    .sql(workrotationsUtils.InsertWorkRotationLineSql)
                    .param("work_rotation_line_id", generatedId)
                    .param("work_rotation_id", workRotationLine.workRotationId())
                    .param("seq", workRotationLine.seq())
                    .param("iterations", workRotationLine.iterations())
                    .param("d_1", workRotationLine.d_1())
                    .param("d_2", workRotationLine.d_2())
                    .param("d_3", workRotationLine.d_3())
                    .param("d_4", workRotationLine.d_4())
                    .param("d_5", workRotationLine.d_5())
                    .param("d_6", workRotationLine.d_6())
                    .param("d_7", workRotationLine.d_7())
                    .param("created_by", userId)
                    .param("created_on", new java.sql.Timestamp(System.currentTimeMillis()))
                    .param("last_updated_by", userId)
                    .param("last_update_date", new java.sql.Timestamp(System.currentTimeMillis()))
                    .update();

            // Set the generated ID back to the workRotationLine object (if possible)
            workRotationLine = getWorkRotationLines(userId, workRotationLine.workRotationId()).stream()
                    .filter(wrl -> wrl.workRotationLineId().equals(generatedId))
                    .findFirst()
                    .orElse(workRotationLine);
            System.out.println("Inserted WorkRotationLine, insCounts: " + insCounts);

            System.out.println("Inserted WorkRotationLine with ID: " + workRotationLine.workRotationLineId());
        } else { // updating
            System.out.println("Updating WorkRotationLine: " + workRotationLine);

            Integer updCounts = this.jdbcClient
                    .sql(workrotationsUtils.updateWorkRotationLineSql)
                    .param("work_rotation_line_id", workRotationLine.workRotationLineId())
                    .param("work_rotation_id", workRotationLine.workRotationId())
                    .param("seq", workRotationLine.seq())
                    .param("iterations", workRotationLine.iterations())
                    .param("d_1", workRotationLine.d_1())
                    .param("d_2", workRotationLine.d_2())
                    .param("d_3", workRotationLine.d_3())
                    .param("d_4", workRotationLine.d_4())
                    .param("d_5", workRotationLine.d_5())
                    .param("d_6", workRotationLine.d_6())
                    .param("d_7", workRotationLine.d_7())
                    .param("last_updated_by", userId)
                    .param("last_update_date", new java.sql.Timestamp(System.currentTimeMillis()))
                    .update();
            System.out.println("Updated WorkRotationLine, updCounts: " + updCounts);
            // Logic to update an existing WorkRotationLine
            System.out.println("Updating WorkRotationLine with ID: " + workRotationLine.workRotationLineId());
        }
        return workRotationLine; // Return the saved WorkRotationLine object
    }

    public Integer deleteWorkRotationLine(Long userId, WorkRotationLine workRotationLine) {
        if (workRotationLine == null || workRotationLine.workRotationLineId() == null) {
            throw new IllegalArgumentException("WorkRotationLine or workRotationLineId cannot be null");
        }

        System.out.println("Deleting WorkRotationLine with ID: " + workRotationLine.workRotationLineId());

        Integer delCounts = this.jdbcClient
                .sql(workrotationsUtils.deleteWorkRotationLineSql)
                .param("work_rotation_line_id", workRotationLine.workRotationLineId())
                .update();

        System.out.println("Deleted WorkRotationLine, delCounts: " + delCounts);

        return delCounts; // Return the deleted WorkRotationLine object
    }

}
