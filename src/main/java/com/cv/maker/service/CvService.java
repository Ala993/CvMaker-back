package com.cv.maker.service;

import com.cv.maker.domain.Collaborator;
import com.cv.maker.domain.Cv;
import com.cv.maker.repository.CollaboratorRepository;
import com.cv.maker.repository.CvRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Cv}.
 */
@Service
public class CvService {

    private final Logger log = LoggerFactory.getLogger(CvService.class);

    private final CvRepository cvRepository;

    private final CollaboratorRepository collaboratorRepository;


    public CvService(CvRepository cvRepository, CollaboratorRepository collaboratorRepository) {
        this.cvRepository = cvRepository;
        this.collaboratorRepository = collaboratorRepository;
    }

    /**
     * Save a cv.
     *
     * @param cv the entity to save.
     * @return the persisted entity.
     */
    public Cv save(Cv cv) {
        log.debug("Request to save Cv : {}", cv);
        if(cv.getId() != null && cv.getCollaborator() == null){
            Optional<Collaborator> optionalCollaborator =collaboratorRepository.findOneByCvId(cv.getId());
            optionalCollaborator.ifPresent(cv::setCollaborator);
        }
        return cvRepository.save(cv);
    }

    /**
     * Partially update a cv.
     *
     * @param cv the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Cv> partialUpdate(Cv cv) {
        log.debug("Request to partially update Cv : {}", cv);

        return cvRepository
            .findById(cv.getId())
            .map(existingCv -> {
                if (cv.getAddress() != null) {
                    existingCv.setAddress(cv.getAddress());
                }
                if (cv.getPhoneNumber() != null) {
                    existingCv.setPhoneNumber(cv.getPhoneNumber());
                }
                if (cv.getPostalCode() != null) {
                    existingCv.setPostalCode(cv.getPostalCode());
                }
                if (cv.getEmail() != null) {
                    existingCv.setEmail(cv.getEmail());
                }

                return existingCv;
            })
            .map(cvRepository::save);
    }

    /**
     * Get all the cvs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<Cv> findAll(Pageable pageable) {
        log.debug("Request to get all Cvs");
        return cvRepository.findAll(pageable);
    }

    /**
     * Get one cv by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<Cv> findOne(String id) {
        log.debug("Request to get Cv : {}", id);
        return cvRepository.findById(id);
    }

    /**
     * Delete the cv by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Cv : {}", id);
        cvRepository.deleteById(id);
    }
}
