package com.ewsv3.ews.workrotations.controller;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.workrotations.dto.*;
import com.ewsv3.ews.workrotations.service.WorkDurationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rostersettings/")
public class WorkDurationController {

    private final WorkDurationService workDurationService;

    public WorkDurationController(WorkDurationService workDurationService) {
        this.workDurationService = workDurationService;
    }


    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getUserId();
        }
        throw new RuntimeException("User not authenticated or invalid token");
    }

    @PostMapping("/workdurations")
    public ResponseEntity<?> getWorkDurations(@RequestHeader Map<String, String> header, @RequestBody WorkDurationRequestBody requestBody) {

        try {


            System.out.println("getWorkDurations > getCurrentUserId:" + getCurrentUserId());
            List<WorkDuration> list = workDurationService.getWorkDurations(getCurrentUserId(), requestBody);

            return ResponseEntity.ok(list);
        } catch (Exception e) {
            // Handle the exception, log it, or rethrow it as needed
            System.err.println("Error fetching work durations: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error fetching work durations: " + e.getMessage());
        }

    }

    @PostMapping("/workrotations")
    public ResponseEntity<?> getWorkRotations(@RequestHeader Map<String, String> header, @RequestBody WorkRotationRequestBody requestBody) {
        try {
            System.out.println("getWorkRotations requestBody: " + requestBody);
            List<WorkRotation> list = workDurationService.getWorkRotations(getCurrentUserId(), requestBody);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            // Handle the exception, log it, or rethrow it as needed
            System.out.println("Error fetching work rotations: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error fetching work rotation: " + e.getMessage());
        }
    }

    @PostMapping("/workrotationlines")
    public ResponseEntity<?> getWorkRotationLines(
            @RequestHeader Map<String, String> header,
            @RequestBody WorkRotationLineRequestDto requestBody) {
        try {
            List<WorkRotationLine> list = workDurationService.getWorkRotationLines(getCurrentUserId(),
                    requestBody.workRotationId());
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            // Handle the exception, log it, or rethrow it as needed
            System.out.println("Error fetching work rotation lines: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error fetching work rotation lines: " + e.getMessage());
        }

    }

    @PostMapping("/saveworkduration")
    public ResponseEntity<?> saveWorkDuration(@RequestHeader Map<String, String> header, @RequestBody WorkDuration workDuration) {
        try {
            WorkDuration savedWorkDuration = workDurationService.saveWorkDuration(getCurrentUserId(), workDuration);
            return ResponseEntity.ok(savedWorkDuration);
        } catch (Exception e) {
            System.out.println("Error deleting work duration: " + e.getMessage());
            if (e.getMessage() != null && e.getMessage().contains("ORA-02292")) {
                return ResponseEntity.badRequest()
                        .body("Cannot save work duration");
            }
            return ResponseEntity.badRequest().body("Error saving work duration: " + e.getMessage());
        }
    }

    @PostMapping("/saveworkrotation")
    public ResponseEntity<?> saveWorkRotation(@RequestHeader Map<String, String> header, @RequestBody WorkRotation workRotation) {
        try {
            System.out.println("Saving work rotation: " + workRotation);
            WorkRotation savedWorkRotation = workDurationService.saveWorkRotation(getCurrentUserId(), workRotation);
            return ResponseEntity.ok(savedWorkRotation);
        } catch (Exception e) {
            System.out.println("Error saving work rotation: " + e.getMessage());
            if (e.getMessage() != null && e.getMessage().contains("ORA-02292")) {
                return ResponseEntity.badRequest()
                        .body("Cannot save work rotation");
            }
            return ResponseEntity.badRequest().body("Error saving work rotation: " + e.getMessage());
        }
    }

    @PostMapping("/deleteworkrotation")
    public ResponseEntity<?> deleteWorkRotation(@RequestHeader Map<String, String> header, @RequestBody WorkRotation workRotation) {
        try {
            Integer delCounts = workDurationService.deleteWorkRotation(getCurrentUserId(), workRotation);
            return ResponseEntity.ok(delCounts);
        } catch (Exception e) {
            System.out.println("Error deleting work rotation: " + e.getMessage());
            if (e.getMessage() != null && e.getMessage().contains("ORA-02292")) {
                return ResponseEntity.badRequest()
                        .body("Cannot delete work rotation: child records exist (ORA-02292).");
            }
            return ResponseEntity.badRequest().body("Error deleting work rotation: " + e.getMessage());
        }
    }

    @PostMapping("/saveworkrotationline")
    public ResponseEntity<?> saveWorkRotationLine(@RequestHeader Map<String, String> header, @RequestBody WorkRotationLine workRotationLine) {
        try {

            System.out.println("Saving work rotation line: " + workRotationLine);
            WorkRotationLine savedWorkRotationLine = workDurationService.saveWorkRotationLine(getCurrentUserId(),
                    workRotationLine);
            return ResponseEntity.ok(savedWorkRotationLine);
        } catch (Exception e) {
            // Handle the exception, log it, or rethrow it as needed
            System.out.println("Error deleting work rotation: " + e.getMessage());
            if (e.getMessage() != null && e.getMessage().contains("ORA-02292")) {
                return ResponseEntity.badRequest()
                        .body("Cannot save work rotation line");
            }
            return ResponseEntity.badRequest().body("Error saving work rotation line: " + e.getMessage());
        }
    }

    @PostMapping("/deleteworkrotationline")
    public ResponseEntity<?> deleteWorkRotationLine(@RequestHeader Map<String, String> header, @RequestBody WorkRotationLine workRotationLine) {
        try {
            Integer delCounts = workDurationService.deleteWorkRotationLine(getCurrentUserId(),
                    workRotationLine);
            DMLResponseDto dto = new DMLResponseDto("S", delCounts + " rotation lines deleted successfully");
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            System.out.println("Error deleting work rotation: " + e.getMessage());
            if (e.getMessage() != null && e.getMessage().contains("ORA-02292")) {
                return ResponseEntity.badRequest()
                        .body("Cannot delete work rotation line: child records exist (ORA-02292).");
            }
            return ResponseEntity.badRequest().body("Error deleting work rotation line: " + e.getMessage());
        }
    }

}