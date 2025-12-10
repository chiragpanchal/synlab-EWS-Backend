package com.ewsv3.ews.setup.controller;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.setup.entity.PersonPreferredJob;
import com.ewsv3.ews.setup.entity.PrefCurrency;
import com.ewsv3.ews.setup.entity.PrefJobs;
import com.ewsv3.ews.setup.service.PersonPreferredJobService;
import com.ewsv3.ews.team.dto.ProfileDatesRequestBody;
import com.ewsv3.ews.team.dto.TeamTimecardSimple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/setup/person-preferred-jobs")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PersonPreferredJobController {

    private static final Logger logger = LoggerFactory.getLogger(PersonPreferredJobController.class);

    private final JdbcClient jdbcClient;

    public PersonPreferredJobController(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Autowired
    private PersonPreferredJobService personPreferredJobService;

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getUserId();
        }
        throw new RuntimeException("User not authenticated or invalid token");
    }

    @GetMapping("team-list")
    public ResponseEntity<List<TeamTimecardSimple>> getTeamList(@RequestHeader Map<String, String> headers,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "") String text,
            @RequestBody ProfileDatesRequestBody requestBody) {

        logger.info("getTeamList - Entry - Time: {}, Request: {}", LocalDateTime.now(), requestBody);

        try {
            // System.out.println("getTeamTimecardsSimple > requestBody:" + requestBody);

            List<TeamTimecardSimple> teamTimecardsSimple = this.personPreferredJobService.getTeamList(
                    getCurrentUserId(),
                    requestBody.profileId(),
                    LocalDate.ofInstant(requestBody.startDate().toInstant(), ZoneId.systemDefault()),
                    LocalDate.ofInstant(requestBody.endDate().toInstant(), ZoneId.systemDefault()),
                    page,
                    size,
                    text,
                    this.jdbcClient);

            // System.out.println("getTeamTimecardsSimple > teamTimecardsSimple:" +
            // teamTimecardsSimple);
            logger.info("getTeamList - Exit - Time: {}, Response Count: {}", LocalDateTime.now(),
                    teamTimecardsSimple.size());

            return new ResponseEntity<>(teamTimecardsSimple, HttpStatus.OK);

        } catch (Exception exception) {
            logger.error("getTeamList - Exception - Time: {}, Request: {}, Error: {}", LocalDateTime.now(),
                    requestBody, exception.getMessage(), exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Get person preferred jobs by person ID with pagination
     */
    @GetMapping
    public ResponseEntity<Page<PersonPreferredJob>> getPersonPreferredJobsByPersonId(
            @RequestParam Long personId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        logger.info("GET_PERSON_PREFERRED_JOBS_BY_PERSON_ID - Entry - Time: {}, PersonId: {}, Page: {}, Size: {}",
                LocalDateTime.now(), personId, page, size);
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<PersonPreferredJob> jobs = personPreferredJobService.findByPersonId(personId, pageable);
            logger.info("GET_PERSON_PREFERRED_JOBS_BY_PERSON_ID - Exit - Time: {}, TotalElements: {}, TotalPages: {}",
                    LocalDateTime.now(), jobs.getTotalElements(), jobs.getTotalPages());
            return ResponseEntity.ok(jobs);
        } catch (Exception e) {
            logger.error("GET_PERSON_PREFERRED_JOBS_BY_PERSON_ID - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get person preferred job by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<PersonPreferredJob> getPersonPreferredJobById(@PathVariable Long id) {
        logger.info("GET_PERSON_PREFERRED_JOB_BY_ID - Entry - Time: {}, ID: {}", LocalDateTime.now(), id);
        try {
            Optional<PersonPreferredJob> job = personPreferredJobService.findById(id);
            if (job.isPresent()) {
                logger.info("GET_PERSON_PREFERRED_JOB_BY_ID - Exit - Time: {}, Found: true", LocalDateTime.now());
                return ResponseEntity.ok(job.get());
            } else {
                logger.info("GET_PERSON_PREFERRED_JOB_BY_ID - Exit - Time: {}, Found: false", LocalDateTime.now());
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("GET_PERSON_PREFERRED_JOB_BY_ID - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Create a new person preferred job
     */
    @PostMapping
    public ResponseEntity<PersonPreferredJob> createPersonPreferredJob(
            @RequestBody PersonPreferredJob personPreferredJob) {
        logger.info("CREATE_PERSON_PREFERRED_JOB - Entry - Time: {}", LocalDateTime.now());
        try {
            Long currentUserId = getCurrentUserId();
            java.util.Date currentDate = new java.util.Date();

            personPreferredJob.setCreatedBy(currentUserId);
            personPreferredJob.setCreatedOn(currentDate);
            personPreferredJob.setLastUpdatedBy(currentUserId);
            personPreferredJob.setLastUpdateDate(currentDate);

            PersonPreferredJob savedJob = personPreferredJobService.save(personPreferredJob);
            logger.info("CREATE_PERSON_PREFERRED_JOB - Exit - Time: {}, ID: {}", LocalDateTime.now(),
                    savedJob.getPersonPreferredJobId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedJob);
        } catch (Exception e) {
            logger.error("CREATE_PERSON_PREFERRED_JOB - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update an existing person preferred job
     */
    @PutMapping("/{id}")
    public ResponseEntity<PersonPreferredJob> updatePersonPreferredJob(@PathVariable Long id,
            @RequestBody PersonPreferredJob personPreferredJob) {
        logger.info("UPDATE_PERSON_PREFERRED_JOB - Entry - Time: {}, ID: {}", LocalDateTime.now(), id);
        try {
            if (!personPreferredJobService.existsById(id)) {
                logger.info("UPDATE_PERSON_PREFERRED_JOB - Exit - Time: {}, Found: false", LocalDateTime.now());
                return ResponseEntity.notFound().build();
            }
            Long currentUserId = getCurrentUserId();
            java.util.Date currentDate = new java.util.Date();

            personPreferredJob.setPersonPreferredJobId(id);
            personPreferredJob.setLastUpdatedBy(currentUserId);
            personPreferredJob.setLastUpdateDate(currentDate);

            PersonPreferredJob updatedJob = personPreferredJobService.save(personPreferredJob);
            logger.info("UPDATE_PERSON_PREFERRED_JOB - Exit - Time: {}, Updated: true", LocalDateTime.now());
            return ResponseEntity.ok(updatedJob);
        } catch (Exception e) {
            logger.error("UPDATE_PERSON_PREFERRED_JOB - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Delete person preferred job by ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersonPreferredJob(@PathVariable Long id) {
        logger.info("DELETE_PERSON_PREFERRED_JOB - Entry - Time: {}, ID: {}", LocalDateTime.now(), id);
        try {
            if (!personPreferredJobService.existsById(id)) {
                logger.info("DELETE_PERSON_PREFERRED_JOB - Exit - Time: {}, Found: false", LocalDateTime.now());
                return ResponseEntity.notFound().build();
            }
            personPreferredJobService.deleteById(id);
            logger.info("DELETE_PERSON_PREFERRED_JOB - Exit - Time: {}, Deleted: true", LocalDateTime.now());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("DELETE_PERSON_PREFERRED_JOB - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("job-list")
    public ResponseEntity<List<PrefJobs>> getPrefJobs(@RequestHeader Map<String, String> headers,
            @RequestBody ProfileDatesRequestBody requestBody) {

        try {
            logger.info("getPrefJobs - Entry - Time: {}, Request: {}", LocalDateTime.now(), requestBody);
            List<PrefJobs> prefJobList = this.personPreferredJobService.getPrefJobList(requestBody.profileId(),
                    jdbcClient);
            logger.info("getPrefJobs - Exit - Time: {}, Response Count: {}", LocalDateTime.now(),
                    prefJobList.size());
            return ResponseEntity.ok(prefJobList);
        } catch (Exception exception) {
            logger.error("getPrefJobs - Exception - Time: {}, Request: {}, Error: {}", LocalDateTime.now(),
                    requestBody, exception.getMessage(), exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("currency-list")
    public ResponseEntity<List<PrefCurrency>> getPrefCurrencies(@RequestHeader Map<String, String> headers) {

        try {
            logger.info("getPrefCurrencies - Entry - Time: {}", LocalDateTime.now());
            List<PrefCurrency> prefCurrencyList = this.personPreferredJobService.getPrefCurrencyList(jdbcClient);
            return ResponseEntity.ok(prefCurrencyList);
        } catch (Exception exception) {
            logger.error("getPrefCurrencies - Exception - Time: {}, Error: {}", LocalDateTime.now(),
                    exception.getMessage(), exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
