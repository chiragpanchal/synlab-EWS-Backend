package com.ewsv3.ews.setup.service;

import com.ewsv3.ews.setup.entity.PersonPreferredSkill;
import com.ewsv3.ews.setup.repos.PersonPreferredSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PersonPreferredSkillService {

    @Autowired
    private PersonPreferredSkillRepository personPreferredSkillRepository;

    /**
     * Get person preferred skill by ID
     */
    public Optional<PersonPreferredSkill> findById(Long id) {
        return personPreferredSkillRepository.findById(id);
    }

    /**
     * Get all skills by person preferred job ID
     */
    public List<PersonPreferredSkill> findByPersonPreferredJobId(Long personPreferredJobId) {
        return personPreferredSkillRepository.findByPersonPreferredJobId(personPreferredJobId);
    }

    /**
     * Save or update person preferred skill
     */
    public PersonPreferredSkill save(PersonPreferredSkill personPreferredSkill) {
        return personPreferredSkillRepository.save(personPreferredSkill);
    }

    /**
     * Delete person preferred skill by ID
     */
    public void deleteById(Long id) {
        personPreferredSkillRepository.deleteById(id);
    }

    /**
     * Check if person preferred skill exists
     */
    public boolean existsById(Long id) {
        return personPreferredSkillRepository.existsById(id);
    }
}
