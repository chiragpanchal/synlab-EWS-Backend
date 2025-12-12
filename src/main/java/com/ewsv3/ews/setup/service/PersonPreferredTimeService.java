package com.ewsv3.ews.setup.service;

import com.ewsv3.ews.setup.entity.PersonPreferredTime;
import com.ewsv3.ews.setup.repos.PersonPreferredTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class PersonPreferredTimeService {

    @Autowired
    private PersonPreferredTimeRepository personPreferredTimeRepository;

    /**
     * Get all person preferred times by person ID with pagination
     */
    public Page<PersonPreferredTime> findByPersonId(Long personId, Pageable pageable) {
        return personPreferredTimeRepository.findByPersonId(personId, pageable);
    }

    /**
     * Get person preferred time by ID
     */
    public Optional<PersonPreferredTime> findById(Long id) {
        return personPreferredTimeRepository.findById(id);
    }

    /**
     * Save or update person preferred time
     */
    public PersonPreferredTime save(PersonPreferredTime personPreferredTime) {
        return personPreferredTimeRepository.save(personPreferredTime);
    }

    /**
     * Delete person preferred time by ID
     */
    public void deleteById(Long id) {
        personPreferredTimeRepository.deleteById(id);
    }

    /**
     * Check if person preferred time exists
     */
    public boolean existsById(Long id) {
        return personPreferredTimeRepository.existsById(id);
    }
}
