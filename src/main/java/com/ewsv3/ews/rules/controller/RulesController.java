package com.ewsv3.ews.rules.controller;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.rules.dto.DemandTemplate;
import com.ewsv3.ews.rules.dto.DemandTemplateLine;
import com.ewsv3.ews.rules.dto.DemandTemplateSaveReqBody;
import com.ewsv3.ews.rules.service.DemandTemplateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/rules/")
public class RulesController {

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
    public ResponseEntity<List<DemandTemplate>> getDemandTempaltes(@RequestHeader Map<String, String> header, @RequestBody DemandTemplate template) {
        try {

            List<DemandTemplate> demandTemplates = this.demandTemplateService.getDemandTemplates(getCurrentUserId(), jdbcClient, template);
            return new ResponseEntity<>(demandTemplates, HttpStatus.OK);

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("demand-template-lines")
    public ResponseEntity<List<DemandTemplateLine>> getDemandTempalteLines(@RequestHeader Map<String, String> header, @RequestBody DemandTemplate reqBody) {
        try {

            List<DemandTemplateLine> demandTemplateLines = this.demandTemplateService.getDemandTemplatesLines(reqBody.demandTemplateId(), jdbcClient);
            return new ResponseEntity<>(demandTemplateLines, HttpStatus.OK);

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("demand-template-save")
    public ResponseEntity<DMLResponseDto> saveDemandTemplate(@RequestHeader Map<String, String> header, @RequestBody DemandTemplateSaveReqBody reqBody) {

        System.out.println("saveDemandTemplate reqBody:" + reqBody);
        try {
            DMLResponseDto responseDto = demandTemplateService.saveDemandTempalte(reqBody, getCurrentUserId(), jdbcClient);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @PostMapping("demand-template-line-delete")
    public ResponseEntity<DMLResponseDto> deleteDemandTemplateLine(@RequestHeader Map<String, String> header, @RequestBody DemandTemplateLine reqBody) {

        System.out.println("deleteDemandTemplateLine reqBody:" + reqBody);
        try {
            DMLResponseDto responseDto = demandTemplateService.deleteDemandTemplateLine(getCurrentUserId(), reqBody, jdbcClient);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @PostMapping("demand-template-delete")
    public ResponseEntity<DMLResponseDto> deleteDemandTemplate(@RequestHeader Map<String, String> header, @RequestBody DemandTemplate reqBody) {

        System.out.println("deleteDemandTemplateLine reqBody:" + reqBody);
        try {
            DMLResponseDto responseDto = demandTemplateService.deleteDemandTemplate(getCurrentUserId(), reqBody, jdbcClient);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            // return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

}
