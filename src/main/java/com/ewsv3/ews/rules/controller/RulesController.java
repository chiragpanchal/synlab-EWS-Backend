package com.ewsv3.ews.rules.controller;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.rules.dto.DemandTemplate;
import com.ewsv3.ews.rules.dto.DemandTemplateLine;
import com.ewsv3.ews.rules.dto.DemandTemplateSaveReqBody;
import com.ewsv3.ews.rules.dto.DemandTemplateSkills;
import com.ewsv3.ews.rules.service.DemandTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/rules/")
public class RulesController {
    private static final Logger logger = LoggerFactory.getLogger(RulesController.class);

    private final JdbcClient jdbcClient;
    private final DemandTemplateService demandTemplateService;

    public RulesController(JdbcClient jdbcClient, DemandTemplateService demandTemplateService) {
        this.jdbcClient = jdbcClient;
        this.demandTemplateService = demandTemplateService;
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getUserId();
        }
        throw new RuntimeException("User not authenticated or invalid token");
    }

    @PostMapping("demand-template")
    public ResponseEntity<List<DemandTemplate>> getDemandTempaltes(@RequestHeader Map<String, String> header,
            @RequestBody DemandTemplate template) {
        logger.info("GET_DEMAND_TEMPLATES - Entry - Time: {}, Request: {}", LocalDateTime.now(), template);
        try {

            List<DemandTemplate> demandTemplates = this.demandTemplateService.getDemandTemplates(getCurrentUserId(),
                    jdbcClient, template);
            logger.info("GET_DEMAND_TEMPLATES - Exit - Time: {}, Response count: {}", LocalDateTime.now(),
                    demandTemplates.size());
            return new ResponseEntity<>(demandTemplates, HttpStatus.OK);

        } catch (Exception exception) {
            // System.out.println(exception.getMessage());
            logger.error("GET_DEMAND_TEMPLATES - Exception - Time: {}, Request: {}, Error: {}",
                    LocalDateTime.now(), template, exception.getMessage(), exception);
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("demand-template-lines")
    public ResponseEntity<List<DemandTemplateLine>> getDemandTempalteLines(@RequestHeader Map<String, String> header,
            @RequestBody DemandTemplate reqBody) {
        logger.info("GET_DEMAND_TEMPLATE_LINES - Entry - Time: {}, DemandTemplateId: {}", LocalDateTime.now(),
                reqBody.demandTemplateId());
        try {

            List<DemandTemplateLine> demandTemplateLines = this.demandTemplateService
                    .getDemandTemplatesLines(reqBody.demandTemplateId(), jdbcClient);
            logger.info("GET_DEMAND_TEMPLATE_LINES - Exit - Time: {}, Response count: {}", LocalDateTime.now(),
                    demandTemplateLines.size());
            return new ResponseEntity<>(demandTemplateLines, HttpStatus.OK);

        } catch (Exception exception) {
            // System.out.println(exception.getMessage());
            logger.error("GET_DEMAND_TEMPLATE_LINES - Exception - Time: {}, DemandTemplateId: {}, Error: {}",
                    LocalDateTime.now(), reqBody.demandTemplateId(), exception.getMessage(), exception);
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("demand-template-save")
    public ResponseEntity<DMLResponseDto> saveDemandTemplate(@RequestHeader Map<String, String> header,
            @RequestBody DemandTemplateSaveReqBody reqBody) {
        logger.info("SAVE_DEMAND_TEMPLATE - Entry - Time: {}, Request: {}", LocalDateTime.now(), reqBody);

        // System.out.println("saveDemandTemplate reqBody:" + reqBody);
        try {
            DMLResponseDto responseDto = demandTemplateService.saveDemandTempalte(reqBody, getCurrentUserId(),
                    jdbcClient);
            logger.info("SAVE_DEMAND_TEMPLATE - Exit - Time: {}, Response: {}", LocalDateTime.now(), responseDto);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (Exception exception) {
            // System.out.println(exception.getMessage());
            logger.error("SAVE_DEMAND_TEMPLATE - Exception - Time: {}, Request: {}, Error: {}",
                    LocalDateTime.now(), reqBody, exception.getMessage(), exception);
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("demand-template-line-delete")
    public ResponseEntity<DMLResponseDto> deleteDemandTemplateLine(@RequestHeader Map<String, String> header,
            @RequestBody DemandTemplateLine reqBody) {
        logger.info("DELETE_DEMAND_TEMPLATE_LINE - Entry - Time: {}, Request: {}", LocalDateTime.now(), reqBody);

        // System.out.println("deleteDemandTemplateLine reqBody:" + reqBody);
        try {
            DMLResponseDto responseDto = demandTemplateService.deleteDemandTemplateLine(getCurrentUserId(), reqBody,
                    jdbcClient);
            logger.info("DELETE_DEMAND_TEMPLATE_LINE - Exit - Time: {}, Response: {}", LocalDateTime.now(),
                    responseDto);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (Exception exception) {
            // System.out.println(exception.getMessage());
            logger.error("DELETE_DEMAND_TEMPLATE_LINE - Exception - Time: {}, Request: {}, Error: {}",
                    LocalDateTime.now(), reqBody, exception.getMessage(), exception);
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("demand-template-delete")
    public ResponseEntity<DMLResponseDto> deleteDemandTemplate(@RequestHeader Map<String, String> header,
            @RequestBody DemandTemplate reqBody) {
        logger.info("DELETE_DEMAND_TEMPLATE - Entry - Time: {}, Request: {}", LocalDateTime.now(), reqBody);

        // System.out.println("deleteDemandTemplateLine reqBody:" + reqBody);
        try {
            DMLResponseDto responseDto = demandTemplateService.deleteDemandTemplate(getCurrentUserId(), reqBody,
                    jdbcClient);
            logger.info("DELETE_DEMAND_TEMPLATE - Exit - Time: {}, Response: {}", LocalDateTime.now(), responseDto);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (Exception exception) {
            // System.out.println(exception.getMessage());
            logger.error("DELETE_DEMAND_TEMPLATE - Exception - Time: {}, Request: {}, Error: {}",
                    LocalDateTime.now(), reqBody, exception.getMessage(), exception);
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // @GetMapping("getDemandTemplateSkills/{}")
    // public String getMethodName(@RequestParam String param) {
    // return new String();
    // }

    @GetMapping("demand-skills/{demandTemplateLineId}")
    public ResponseEntity<List<DemandTemplateSkills>> getDemandTemplateSkills(
            @RequestHeader Map<String, String> header,
            @PathVariable Long demandTemplateLineId) {
        logger.info("getDemandTemplateSkills - Entry - Time: {}, demandTemplateLineId: {}", LocalDateTime.now(),
                demandTemplateLineId);

        try {
            List<DemandTemplateSkills> demandTemplateSkills = demandTemplateService.getDemandTemplateSkills(
                    getCurrentUserId(),
                    demandTemplateLineId,
                    jdbcClient);
            logger.info("getDemandTemplateSkills - Exit - Time: {}, Response counts: {}", LocalDateTime.now(),
                    demandTemplateSkills.size());
            return new ResponseEntity<>(demandTemplateSkills, HttpStatus.OK);
        } catch (Exception exception) {
            logger.error("getDemandTemplateSkills - Exception - Time: {}, demandTemplateLineId: {}, Error: {}",
                    LocalDateTime.now(), demandTemplateLineId, exception.getMessage(), exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("demand-skills")
    public ResponseEntity<Void> saveDemandTemplateSkills(
            @RequestHeader Map<String, String> header,
            @RequestBody DemandTemplateSkills reqBody) {
        logger.info("saveDemandTemplateSkills - Entry - Time: {}, reqBody: {}", LocalDateTime.now(),
                reqBody);

        try {
            int saveCounts = demandTemplateService.saveDemandTemplateSkills(
                    getCurrentUserId(),
                    reqBody,
                    jdbcClient);
            logger.info("saveDemandTemplateSkills - Exit - Time: {}, Response counts: {}", LocalDateTime.now(),
                    saveCounts);

            return ResponseEntity.noContent().build();
        } catch (Exception exception) {
            logger.error("saveDemandTemplateSkills - Exception - Time: {}, reqBody: {}, Error: {}",
                    LocalDateTime.now(), reqBody, exception.getMessage(), exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @DeleteMapping("demand-skills/{demandTemplateSkillId}")
    public ResponseEntity<Void> deleteDemandTemplateSkills(
            @RequestHeader Map<String, String> header,
            @PathVariable Long demandTemplateSkillId) {
        logger.info("deleteDemandTemplateSkills - Entry - Time: {}, demandTemplateSkillId: {}", LocalDateTime.now(),
                demandTemplateSkillId);

        try {
            int saveCounts = demandTemplateService.deleteDemandTemplateSkills(
                    getCurrentUserId(),
                    demandTemplateSkillId,
                    jdbcClient);
            logger.info("deleteDemandTemplateSkills - Exit - Time: {}, Response counts: {}", LocalDateTime.now(),
                    saveCounts);

            return ResponseEntity.noContent().build();
        } catch (Exception exception) {
            logger.error("deleteDemandTemplateSkills - Exception - Time: {}, demandTemplateSkillId: {}, Error: {}",
                    LocalDateTime.now(), demandTemplateSkillId, exception.getMessage(), exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

}
