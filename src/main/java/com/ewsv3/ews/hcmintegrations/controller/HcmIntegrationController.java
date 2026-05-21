package com.ewsv3.ews.hcmintegrations.controller;

import com.ewsv3.ews.hcmintegrations.dto.PersonScheduleDto;
import com.ewsv3.ews.hcmintegrations.dto.ScheduleReportDto;
import com.ewsv3.ews.hcmintegrations.dto.SyncResultDto;
import com.ewsv3.ews.hcmintegrations.service.HcmIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/hcm")
public class HcmIntegrationController {

    @Autowired
    private HcmIntegrationService hcmIntegrationService;

    @PostMapping("/sync/person-schedules")
    public ResponseEntity<SyncResultDto> syncPersonSchedules() {
        try {
            int count = hcmIntegrationService.syncPersonSchedules();
            return ResponseEntity.ok(new SyncResultDto("SUCCESS", count,
                    count + " person schedule records synced from Oracle HCM"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new SyncResultDto("ERROR", 0, e.getMessage()));
        }
    }

    @GetMapping("/person-schedules")
    public ResponseEntity<List<PersonScheduleDto>> getPersonSchedules() {
        return ResponseEntity.ok(hcmIntegrationService.getStoredPersonSchedules());
    }

    @GetMapping("/person-schedules/preview")
    public ResponseEntity<?> previewPersonSchedules() {
        try {
            return ResponseEntity.ok(hcmIntegrationService.fetchPersonSchedules());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new SyncResultDto("ERROR", 0, e.getMessage()));
        }
    }

    @PostMapping("/sync/schedules")
    public ResponseEntity<SyncResultDto> syncSchedules() {
        try {
            int count = hcmIntegrationService.syncSchedules();
            return ResponseEntity.ok(new SyncResultDto("SUCCESS", count,
                    count + " schedule records synced from Oracle HCM"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new SyncResultDto("ERROR", 0, e.getMessage()));
        }
    }

    @GetMapping("/schedules")
    public ResponseEntity<List<ScheduleReportDto>> getSchedules() {
        return ResponseEntity.ok(hcmIntegrationService.getStoredSchedules());
    }

    @GetMapping("/schedules/preview")
    public ResponseEntity<?> previewSchedules() {
        try {
            return ResponseEntity.ok(hcmIntegrationService.fetchSchedulesFromHcm());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new SyncResultDto("ERROR", 0, e.getMessage()));
        }
    }
}
