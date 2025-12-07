package com.ewsv3.ews.schedulers.controller;

import com.ewsv3.ews.schedulers.service.SchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/scheduler")
public class ScherduletController {
    private static final Logger logger = LoggerFactory.getLogger(ScherduletController.class);

    // private final JdbcClient jdbcClient;
    private final SchedulerService schedulerService;

    public ScherduletController(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @PostMapping("start")
    public void startInboundIntegrationScheduler() {
        logger.info("START_INBOUND_INTEGRATION_SCHEDULER - Entry - Time: {}", LocalDateTime.now());
        //System.out.println("scheduler starts.");

        this.schedulerService.startInboundintegration();
        //System.out.println("scheduler ends.");
        logger.info("START_INBOUND_INTEGRATION_SCHEDULER - Exit - Time: {}", LocalDateTime.now());
    }

}
