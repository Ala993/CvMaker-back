package com.cv.maker.service;

import com.cv.maker.domain.Study;
import com.cv.maker.repository.StudyRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Study}.
 */
@Service
public class StudyService {

    private final Logger log = LoggerFactory.getLogger(StudyService.class);

    private final StudyRepository studyRepository;

    public StudyService(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

    /**
     * Save a study.
     *
     * @param study the entity to save.
     * @return the persisted entity.
     */
    public Study save(Study study) {
        log.debug("Request to save Study : {}", study);
        return studyRepository.save(study);
    }

    /**
     * Partially update a study.
     *
     * @param study the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Study> partialUpdate(Study study) {
        log.debug("Request to partially update Study : {}", study);

        return studyRepository
            .findById(study.getId())
            .map(existingStudy -> {
                if (study.getTitle() != null) {
                    existingStudy.setTitle(study.getTitle());
                }
                if (study.getLocation() != null) {
                    existingStudy.setLocation(study.getLocation());
                }
                if (study.getEstablishment() != null) {
                    existingStudy.setEstablishment(study.getEstablishment());
                }
                if (study.getStartDate() != null) {
                    existingStudy.setStartDate(study.getStartDate());
                }
                if (study.getEndDate() != null) {
                    existingStudy.setEndDate(study.getEndDate());
                }
                if (study.getDescription() != null) {
                    existingStudy.setDescription(study.getDescription());
                }

                return existingStudy;
            })
            .map(studyRepository::save);
    }

    /**
     * Get all the studies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<Study> findAll(Pageable pageable) {
        log.debug("Request to get all Studies");
        return studyRepository.findAll(pageable);
    }

    /**
     * Get one study by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<Study> findOne(String id) {
        log.debug("Request to get Study : {}", id);
        return studyRepository.findById(id);
    }

    /**
     * Delete the study by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Study : {}", id);
        studyRepository.deleteById(id);
    }
}
