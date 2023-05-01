package com.cv.maker.service;

import com.cv.maker.domain.*;
import com.cv.maker.repository.HumanResourceRepository;

import java.time.Instant;
import java.util.Optional;

import com.cv.maker.repository.UserRepository;
import com.cv.maker.security.AuthoritiesConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link HumanResource}.
 */
@Service
public class HumanResourceService {

    private final Logger log = LoggerFactory.getLogger(HumanResourceService.class);

    private final HumanResourceRepository humanResourceRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;


    public HumanResourceService(HumanResourceRepository humanResourceRepository, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.humanResourceRepository = humanResourceRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    /**
     * Save a humanResource.
     *
     * @param humanResource the entity to save.
     * @return the persisted entity.
     */
    public HumanResource save(HumanResource humanResource) {
        log.debug("Request to save HumanResource : {}", humanResource);
        return humanResourceRepository.save(humanResource);
    }

    /**
     * Partially update a humanResource.
     *
     * @param humanResource the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HumanResource> partialUpdate(HumanResource humanResource) {
        log.debug("Request to partially update HumanResource : {}", humanResource);

        return humanResourceRepository
            .findById(humanResource.getId())
            .map(existingHumanResource -> {
                return existingHumanResource;
            })
            .map(humanResourceRepository::save);
    }

    /**
     * Get all the humanResources.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<HumanResource> findAll(Pageable pageable) {
        log.debug("Request to get all HumanResources");
        return humanResourceRepository.findAll(pageable);
    }

    /**
     * Get one humanResource by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<HumanResource> findOne(String id) {
        log.debug("Request to get HumanResource : {}", id);
        return humanResourceRepository.findById(id);
    }

    /**
     * Delete the humanResource by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete HumanResource : {}", id);
        humanResourceRepository.deleteById(id);
    }


    public HumanResource createHumanResource(HumanResource humanResource) {
        log.debug("Request to save HumanResource : {}", humanResource);
        User newUser = new User();
        newUser.setLogin(humanResource.getUser().getEmail());
        newUser.setPassword(passwordEncoder.encode(humanResource.getUser().getPassword()));
        newUser.setFirstName(humanResource.getUser().getFirstName());
        newUser.setLastName(humanResource.getUser().getLastName());
        newUser.setEmail(humanResource.getUser().getEmail());
        newUser.setActivated(true);
        newUser.setLangKey("fr");
        newUser.setCreatedDate(Instant.now());
        Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.USER);
        newUser.getAuthorities().add(authority);
        newUser = userRepository.save(newUser);
        humanResource.setUser(newUser);

        return humanResourceRepository.save(humanResource);
    }
    public HumanResource updateHumanResource(HumanResource humanResource) {
        User user = userRepository.findById(humanResource.getUser().getId()).orElseThrow();
        user.setFirstName(humanResource.getUser().getFirstName());
        user.setLastName(humanResource.getUser().getLastName());
        user = userRepository.save(user);
        humanResource.setUser(user);

        return humanResourceRepository.save(humanResource);

    }
}
