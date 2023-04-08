package com.cv.maker.service;

import com.cv.maker.domain.Authority;
import com.cv.maker.domain.Collaborator;
import com.cv.maker.domain.Cv;
import com.cv.maker.domain.User;
import com.cv.maker.repository.CollaboratorRepository;
import com.cv.maker.repository.CvRepository;
import com.cv.maker.repository.UserRepository;
import com.cv.maker.security.AuthoritiesConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Collaborator}.
 */
@Service
public class CollaboratorService {

    private final Logger log = LoggerFactory.getLogger(CollaboratorService.class);

    private final CollaboratorRepository collaboratorRepository;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final CvRepository cvRepository;

    public CollaboratorService(CollaboratorRepository collaboratorRepository, UserService userService, PasswordEncoder passwordEncoder, UserRepository userRepository, CvRepository cvRepository) {
        this.collaboratorRepository = collaboratorRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.cvRepository = cvRepository;
    }

    /**
     * Save a collaborator.
     *
     * @param collaborator the entity to save.
     * @return the persisted entity.
     */
    public Collaborator createCollaborator(Collaborator collaborator) {
        log.debug("Request to save Collaborator : {}", collaborator);
        User newUser = new User();
        newUser.setLogin(collaborator.getUser().getEmail());
        newUser.setPassword(passwordEncoder.encode(collaborator.getUser().getPassword()));
        newUser.setFirstName(collaborator.getUser().getFirstName());
        newUser.setLastName(collaborator.getUser().getLastName());
        newUser.setEmail(collaborator.getUser().getEmail());
        newUser.setActivated(true);
        newUser.setLangKey("fr");
        newUser.setCreatedDate(Instant.now());
        Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.USER);
        newUser.getAuthorities().add(authority);
        newUser = userRepository.save(newUser);
        collaborator.setUser(newUser);
        if (collaborator.getCv() != null) {
            if (collaborator.getCv().getId() == null) {
                collaborator.setCv(null);
            }
        }

        return collaboratorRepository.save(collaborator);

    }

    public Collaborator updateCollaborator(Collaborator collaborator) {
        User user = userRepository.findById(collaborator.getUser().getId()).orElseThrow();
        user.setFirstName(collaborator.getUser().getFirstName());
        user.setLastName(collaborator.getUser().getLastName());
        user = userRepository.save(user);
        collaborator.setUser(user);
        if (collaborator.getCv() != null) {
            if (collaborator.getCv().getId() == null) {
                collaborator.setCv(null);
            } else {
                Cv cv = collaborator.getCv();
                cv.setCollaborator(collaborator);
                cvRepository.save(cv);
            }
        }
        return collaboratorRepository.save(collaborator);

    }


    /**
     * Partially update a collaborator.
     *
     * @param collaborator the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Collaborator> partialUpdate(Collaborator collaborator) {
        log.debug("Request to partially update Collaborator : {}", collaborator);

        return collaboratorRepository
            .findById(collaborator.getId())
            .map(existingCollaborator -> {
                return existingCollaborator;
            })
            .map(collaboratorRepository::save);
    }

    /**
     * Get all the collaborators.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<Collaborator> findAll(Pageable pageable) {
        log.debug("Request to get all Collaborators");
        return collaboratorRepository.findAll(pageable);
    }

    /**
     * Get one collaborator by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<Collaborator> findOne(String id) {
        log.debug("Request to get Collaborator : {}", id);
        return collaboratorRepository.findById(id);
    }

    /**
     * Delete the collaborator by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Collaborator : {}", id);
        collaboratorRepository.deleteById(id);
    }

    public Optional<Collaborator> collaboratorByCurrentUser() {
        User user = userService.getCurrentUser();
        return collaboratorRepository.findOneByUserId(user.getId());
    }
}
