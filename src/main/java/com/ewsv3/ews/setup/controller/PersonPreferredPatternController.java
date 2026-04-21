package com.ewsv3.ews.setup.controller;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.setup.entity.PersonPreferredPattern;
import com.ewsv3.ews.setup.entity.PersonPreferredPatternDto;
import com.ewsv3.ews.setup.service.PersonPreferredPatternService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/setup/person-preferred-patterns")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PersonPreferredPatternController {

    private static final Logger logger = LoggerFactory.getLogger(PersonPreferredPatternController.class);

    @Autowired
    private PersonPreferredPatternService personPreferredPatternService;

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getUserId();
        }
        throw new RuntimeException("User not authenticated or invalid token");
    }

    @GetMapping
    public ResponseEntity<List<PersonPreferredPatternDto>> getByPersonId(@RequestParam Long personId) {
        logger.info("GET_PERSON_PREFERRED_PATTERNS_BY_PERSON_ID - Entry - Time: {}, PersonId: {}",
                LocalDateTime.now(), personId);
        try {
            List<PersonPreferredPatternDto> patterns = personPreferredPatternService.findByPersonId(personId);
            logger.info("GET_PERSON_PREFERRED_PATTERNS_BY_PERSON_ID - Exit - Time: {}, Count: {}",
                    LocalDateTime.now(), patterns.size());
            return ResponseEntity.ok(patterns);
        } catch (Exception e) {
            logger.error("GET_PERSON_PREFERRED_PATTERNS_BY_PERSON_ID - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonPreferredPatternDto> getById(@PathVariable Long id) {
        logger.info("GET_PERSON_PREFERRED_PATTERN_BY_ID - Entry - Time: {}, ID: {}", LocalDateTime.now(), id);
        try {
            Optional<PersonPreferredPatternDto> pattern = personPreferredPatternService.findByIdDto(id);
            if (pattern.isPresent()) {
                logger.info("GET_PERSON_PREFERRED_PATTERN_BY_ID - Exit - Time: {}, Found: true", LocalDateTime.now());
                return ResponseEntity.ok(pattern.get());
            } else {
                logger.info("GET_PERSON_PREFERRED_PATTERN_BY_ID - Exit - Time: {}, Found: false", LocalDateTime.now());
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("GET_PERSON_PREFERRED_PATTERN_BY_ID - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<PersonPreferredPattern> create(@RequestBody PersonPreferredPattern personPreferredPattern) {
        logger.info("CREATE_PERSON_PREFERRED_PATTERN - Entry - Time: {}", LocalDateTime.now());
        try {
            Long currentUserId = getCurrentUserId();
            java.util.Date currentDate = new java.util.Date();

            personPreferredPattern.setCreatedBy(currentUserId);
            personPreferredPattern.setCreatedOn(currentDate);
            personPreferredPattern.setLastUpdatedBy(currentUserId);
            personPreferredPattern.setLastUpdateDate(currentDate);

            PersonPreferredPattern saved = personPreferredPatternService.save(personPreferredPattern);
            logger.info("CREATE_PERSON_PREFERRED_PATTERN - Exit - Time: {}, ID: {}",
                    LocalDateTime.now(), saved.getPersonPreferredPatternId());
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            logger.error("CREATE_PERSON_PREFERRED_PATTERN - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonPreferredPattern> update(@PathVariable Long id,
            @RequestBody PersonPreferredPattern personPreferredPattern) {
        logger.info("UPDATE_PERSON_PREFERRED_PATTERN - Entry - Time: {}, ID: {}", LocalDateTime.now(), id);
        try {
            PersonPreferredPattern existing = personPreferredPatternService.findById(id).orElse(null);
            if (existing == null) {
                logger.info("UPDATE_PERSON_PREFERRED_PATTERN - Exit - Time: {}, Found: false", LocalDateTime.now());
                return ResponseEntity.notFound().build();
            }

            Long currentUserId = getCurrentUserId();
            java.util.Date currentDate = new java.util.Date();

            personPreferredPattern.setPersonPreferredPatternId(id);
            personPreferredPattern.setCreatedBy(existing.getCreatedBy());
            personPreferredPattern.setCreatedOn(existing.getCreatedOn());
            personPreferredPattern.setLastUpdatedBy(currentUserId);
            personPreferredPattern.setLastUpdateDate(currentDate);

            PersonPreferredPattern updated = personPreferredPatternService.save(personPreferredPattern);
            logger.info("UPDATE_PERSON_PREFERRED_PATTERN - Exit - Time: {}, Updated: true", LocalDateTime.now());
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            logger.error("UPDATE_PERSON_PREFERRED_PATTERN - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("DELETE_PERSON_PREFERRED_PATTERN - Entry - Time: {}, ID: {}", LocalDateTime.now(), id);
        try {
            if (!personPreferredPatternService.existsById(id)) {
                logger.info("DELETE_PERSON_PREFERRED_PATTERN - Exit - Time: {}, Found: false", LocalDateTime.now());
                return ResponseEntity.notFound().build();
            }
            personPreferredPatternService.deleteById(id);
            logger.info("DELETE_PERSON_PREFERRED_PATTERN - Exit - Time: {}, Deleted: true", LocalDateTime.now());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("DELETE_PERSON_PREFERRED_PATTERN - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
