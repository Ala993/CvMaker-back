package com.cv.maker.service;

import com.cv.maker.domain.Experience;
import com.cv.maker.repository.ExperienceRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Experience}.
 */
@Service
public class ExperienceService {

    private final Logger log = LoggerFactory.getLogger(ExperienceService.class);

    private final ExperienceRepository experienceRepository;

    public ExperienceService(ExperienceRepository experienceRepository) {
        this.experienceRepository = experienceRepository;
    }

    /**
     * Save a experience.
     *
     * @param experience the entity to save.
     * @return the persisted entity.
     */
    public Experience save(Experience experience) {
        log.debug("Request to save Experience : {}", experience);
        return experienceRepository.save(experience);
    }

    /**
     * Partially update a experience.
     *
     * @param experience the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Experience> partialUpdate(Experience experience) {
        log.debug("Request to partially update Experience : {}", experience);

        return experienceRepository
            .findById(experience.getId())
            .map(existingExperience -> {
                if (experience.getStartDate() != null) {
                    existingExperience.setStartDate(experience.getStartDate());
                }
                if (experience.getEndDate() != null) {
                    existingExperience.setEndDate(experience.getEndDate());
                }
                if (experience.getCompany() != null) {
                    existingExperience.setCompany(experience.getCompany());
                }
                if (experience.getPosition() != null) {
                    existingExperience.setPosition(experience.getPosition());
                }

                return existingExperience;
            })
            .map(experienceRepository::save);
    }

    /**
     * Get all the experiences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<Experience> findAll(Pageable pageable) {
        log.debug("Request to get all Experiences");
        return experienceRepository.findAll(pageable);
    }

    /**
     * Get one experience by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<Experience> findOne(String id) {
        log.debug("Request to get Experience : {}", id);
        return experienceRepository.findById(id);
    }

    /**
     * Delete the experience by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Experience : {}", id);
        experienceRepository.deleteById(id);
    }
}
