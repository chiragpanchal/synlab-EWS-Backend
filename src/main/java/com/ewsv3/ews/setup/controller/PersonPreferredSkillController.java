package com.ewsv3.ews.setup.controller;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.setup.entity.PersonPreferredJob;
import com.ewsv3.ews.setup.entity.PersonPreferredSkill;
import com.ewsv3.ews.setup.service.PersonPreferredJobService;
import com.ewsv3.ews.setup.service.PersonPreferredSkillService;
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
@RequestMapping("/api/setup/person-preferred-skills")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PersonPreferredSkillController {

    private static final Logger logger = LoggerFactory.getLogger(PersonPreferredSkillController.class);

    @Autowired
    private PersonPreferredSkillService personPreferredSkillService;

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

    /**
     * Get all skills by person preferred job ID
     */
    @GetMapping("/by-job")
    public ResponseEntity<List<PersonPreferredSkill>> getSkillsByPersonPreferredJobId(
            @RequestParam Long personPreferredJobId) {
        logger.info("GET_SKILLS_BY_PERSON_PREFERRED_JOB_ID - Entry - Time: {}, JobID: {}", LocalDateTime.now(),
                personPreferredJobId);
        try {
            List<PersonPreferredSkill> skills = personPreferredSkillService
                    .findByPersonPreferredJobId(personPreferredJobId);
            logger.info("GET_SKILLS_BY_PERSON_PREFERRED_JOB_ID - Exit - Time: {}, Count: {}", LocalDateTime.now(),
                    skills.size());
            return ResponseEntity.ok(skills);
        } catch (Exception e) {
            logger.error("GET_SKILLS_BY_PERSON_PREFERRED_JOB_ID - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get person preferred skill by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<PersonPreferredSkill> getPersonPreferredSkillById(@PathVariable Long id) {
        logger.info("GET_PERSON_PREFERRED_SKILL_BY_ID - Entry - Time: {}, ID: {}", LocalDateTime.now(), id);
        try {
            Optional<PersonPreferredSkill> skill = personPreferredSkillService.findById(id);
            if (skill.isPresent()) {
                logger.info("GET_PERSON_PREFERRED_SKILL_BY_ID - Exit - Time: {}, Found: true", LocalDateTime.now());
                return ResponseEntity.ok(skill.get());
            } else {
                logger.info("GET_PERSON_PREFERRED_SKILL_BY_ID - Exit - Time: {}, Found: false", LocalDateTime.now());
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("GET_PERSON_PREFERRED_SKILL_BY_ID - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Create a new person preferred skill
     */
    @PostMapping("/job/{personPreferredJobId}")
    public ResponseEntity<PersonPreferredSkill> createPersonPreferredSkill(
            @PathVariable Long personPreferredJobId,
            @RequestBody PersonPreferredSkill personPreferredSkill) {
        logger.info("CREATE_PERSON_PREFERRED_SKILL - Entry - Time: {}, JobID: {}, Request: {}",
                LocalDateTime.now(), personPreferredJobId, personPreferredSkill);
        try {
            // Validate required fields
            if (personPreferredSkill.getSkillId() == null) {
                logger.error("CREATE_PERSON_PREFERRED_SKILL - Error: skillId is required");
                return ResponseEntity.badRequest().build();
            }
            if (personPreferredSkill.getRating() == null) {
                logger.error("CREATE_PERSON_PREFERRED_SKILL - Error: rating is required");
                return ResponseEntity.badRequest().build();
            }

            // Get parent job to retrieve personId
            PersonPreferredJob parentJob = personPreferredJobService.findById(personPreferredJobId)
                    .orElseThrow(() -> new RuntimeException("PersonPreferredJob not found with ID: " + personPreferredJobId));

            Long currentUserId = getCurrentUserId();
            java.util.Date currentDate = new java.util.Date();

            // Set fields from parent job
            personPreferredSkill.setPersonPreferredJobId(personPreferredJobId);
            personPreferredSkill.setPersonId(parentJob.getPersonId());

            // Set audit fields
            personPreferredSkill.setCreatedBy(currentUserId);
            personPreferredSkill.setCreatedOn(currentDate);
            personPreferredSkill.setLastUpdatedBy(currentUserId);
            personPreferredSkill.setLastUpdateDate(currentDate);

            PersonPreferredSkill savedSkill = personPreferredSkillService.save(personPreferredSkill);
            logger.info("CREATE_PERSON_PREFERRED_SKILL - Exit - Time: {}, ID: {}", LocalDateTime.now(),
                    savedSkill.getPersonPreferredSkillId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedSkill);
        } catch (Exception e) {
            logger.error("CREATE_PERSON_PREFERRED_SKILL - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update an existing person preferred skill
     */
    @PutMapping("/{id}")
    public ResponseEntity<PersonPreferredSkill> updatePersonPreferredSkill(@PathVariable Long id,
            @RequestBody PersonPreferredSkill personPreferredSkill) {
        logger.info("UPDATE_PERSON_PREFERRED_SKILL - Entry - Time: {}, ID: {}", LocalDateTime.now(), id);
        try {
            // Fetch existing record to preserve creation audit fields
            PersonPreferredSkill existingSkill = personPreferredSkillService.findById(id).orElse(null);
            if (existingSkill == null) {
                logger.info("UPDATE_PERSON_PREFERRED_SKILL - Exit - Time: {}, Found: false", LocalDateTime.now());
                return ResponseEntity.notFound().build();
            }

            Long currentUserId = getCurrentUserId();
            java.util.Date currentDate = new java.util.Date();

            // Set ID and update audit fields
            personPreferredSkill.setPersonPreferredSkillId(id);

            // Preserve original creation audit fields
            personPreferredSkill.setCreatedBy(existingSkill.getCreatedBy());
            personPreferredSkill.setCreatedOn(existingSkill.getCreatedOn());

            // Update modification audit fields
            personPreferredSkill.setLastUpdatedBy(currentUserId);
            personPreferredSkill.setLastUpdateDate(currentDate);

            PersonPreferredSkill updatedSkill = personPreferredSkillService.save(personPreferredSkill);
            logger.info("UPDATE_PERSON_PREFERRED_SKILL - Exit - Time: {}, Updated: true", LocalDateTime.now());
            return ResponseEntity.ok(updatedSkill);
        } catch (Exception e) {
            logger.error("UPDATE_PERSON_PREFERRED_SKILL - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Delete person preferred skill by ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersonPreferredSkill(@PathVariable Long id) {
        logger.info("DELETE_PERSON_PREFERRED_SKILL - Entry - Time: {}, ID: {}", LocalDateTime.now(), id);
        try {
            if (!personPreferredSkillService.existsById(id)) {
                logger.info("DELETE_PERSON_PREFERRED_SKILL - Exit - Time: {}, Found: false", LocalDateTime.now());
                return ResponseEntity.notFound().build();
            }
            personPreferredSkillService.deleteById(id);
            logger.info("DELETE_PERSON_PREFERRED_SKILL - Exit - Time: {}, Deleted: true", LocalDateTime.now());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("DELETE_PERSON_PREFERRED_SKILL - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
