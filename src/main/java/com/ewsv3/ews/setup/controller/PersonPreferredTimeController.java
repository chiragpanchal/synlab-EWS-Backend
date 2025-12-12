package com.ewsv3.ews.setup.controller;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.setup.entity.PersonPreferredTime;
import com.ewsv3.ews.setup.service.PersonPreferredTimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/setup/person-preferred-times")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PersonPreferredTimeController {

    private static final Logger logger = LoggerFactory.getLogger(PersonPreferredTimeController.class);

    @Autowired
    private PersonPreferredTimeService personPreferredTimeService;

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getUserId();
        }
        throw new RuntimeException("User not authenticated or invalid token");
    }

    /**
     * Get person preferred times by person ID with pagination
     */
    @GetMapping
    public ResponseEntity<Page<PersonPreferredTime>> getPersonPreferredTimesByPersonId(
            @RequestParam Long personId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        logger.info("GET_PERSON_PREFERRED_TIMES_BY_PERSON_ID - Entry - Time: {}, PersonId: {}, Page: {}, Size: {}",
                LocalDateTime.now(), personId, page, size);
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<PersonPreferredTime> times = personPreferredTimeService.findByPersonId(personId, pageable);
            logger.info("GET_PERSON_PREFERRED_TIMES_BY_PERSON_ID - Exit - Time: {}, TotalElements: {}, TotalPages: {}",
                    LocalDateTime.now(), times.getTotalElements(), times.getTotalPages());
            return ResponseEntity.ok(times);
        } catch (Exception e) {
            logger.error("GET_PERSON_PREFERRED_TIMES_BY_PERSON_ID - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get person preferred time by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<PersonPreferredTime> getPersonPreferredTimeById(@PathVariable Long id) {
        logger.info("GET_PERSON_PREFERRED_TIME_BY_ID - Entry - Time: {}, ID: {}", LocalDateTime.now(), id);
        try {
            Optional<PersonPreferredTime> time = personPreferredTimeService.findById(id);
            if (time.isPresent()) {
                logger.info("GET_PERSON_PREFERRED_TIME_BY_ID - Exit - Time: {}, Found: true", LocalDateTime.now());
                return ResponseEntity.ok(time.get());
            } else {
                logger.info("GET_PERSON_PREFERRED_TIME_BY_ID - Exit - Time: {}, Found: false", LocalDateTime.now());
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("GET_PERSON_PREFERRED_TIME_BY_ID - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Create a new person preferred time
     */
    @PostMapping
    public ResponseEntity<PersonPreferredTime> createPersonPreferredTime(
            @RequestBody PersonPreferredTime personPreferredTime) {
        logger.info("CREATE_PERSON_PREFERRED_TIME - Entry - Time: {}", LocalDateTime.now());
        try {
            Long currentUserId = getCurrentUserId();
            java.util.Date currentDate = new java.util.Date();

            personPreferredTime.setCreatedBy(currentUserId);
            personPreferredTime.setCreatedOn(currentDate);
            personPreferredTime.setLastUpdatedBy(currentUserId);
            personPreferredTime.setLastUpdateDate(currentDate);

            PersonPreferredTime savedTime = personPreferredTimeService.save(personPreferredTime);
            logger.info("CREATE_PERSON_PREFERRED_TIME - Exit - Time: {}, ID: {}", LocalDateTime.now(),
                    savedTime.getPersonPreferredTimeId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTime);
        } catch (Exception e) {
            logger.error("CREATE_PERSON_PREFERRED_TIME - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update an existing person preferred time
     */
    @PutMapping("/{id}")
    public ResponseEntity<PersonPreferredTime> updatePersonPreferredTime(@PathVariable Long id,
            @RequestBody PersonPreferredTime personPreferredTime) {
        logger.info("UPDATE_PERSON_PREFERRED_TIME - Entry - Time: {}, ID: {}", LocalDateTime.now(), id);
        try {
            // Fetch existing record to preserve creation audit fields
            PersonPreferredTime existingTime = personPreferredTimeService.findById(id).orElse(null);
            if (existingTime == null) {
                logger.info("UPDATE_PERSON_PREFERRED_TIME - Exit - Time: {}, Found: false", LocalDateTime.now());
                return ResponseEntity.notFound().build();
            }

            Long currentUserId = getCurrentUserId();
            java.util.Date currentDate = new java.util.Date();

            // Set ID and update audit fields
            personPreferredTime.setPersonPreferredTimeId(id);

            // Preserve original creation audit fields
            personPreferredTime.setCreatedBy(existingTime.getCreatedBy());
            personPreferredTime.setCreatedOn(existingTime.getCreatedOn());

            // Update modification audit fields
            personPreferredTime.setLastUpdatedBy(currentUserId);
            personPreferredTime.setLastUpdateDate(currentDate);

            PersonPreferredTime updatedTime = personPreferredTimeService.save(personPreferredTime);
            logger.info("UPDATE_PERSON_PREFERRED_TIME - Exit - Time: {}, Updated: true", LocalDateTime.now());
            return ResponseEntity.ok(updatedTime);
        } catch (Exception e) {
            logger.error("UPDATE_PERSON_PREFERRED_TIME - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Delete person preferred time by ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersonPreferredTime(@PathVariable Long id) {
        logger.info("DELETE_PERSON_PREFERRED_TIME - Entry - Time: {}, ID: {}", LocalDateTime.now(), id);
        try {
            if (!personPreferredTimeService.existsById(id)) {
                logger.info("DELETE_PERSON_PREFERRED_TIME - Exit - Time: {}, Found: false", LocalDateTime.now());
                return ResponseEntity.notFound().build();
            }
            personPreferredTimeService.deleteById(id);
            logger.info("DELETE_PERSON_PREFERRED_TIME - Exit - Time: {}, Deleted: true", LocalDateTime.now());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("DELETE_PERSON_PREFERRED_TIME - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
