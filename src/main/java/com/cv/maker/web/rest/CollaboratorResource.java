package com.cv.maker.web.rest;

import com.cv.maker.domain.Collaborator;
import com.cv.maker.repository.CollaboratorRepository;
import com.cv.maker.service.CollaboratorService;
import com.cv.maker.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.cv.maker.domain.Collaborator}.
 */
@RestController
@RequestMapping("/api")
public class CollaboratorResource {

    private static final String ENTITY_NAME = "collaborator";
    private final Logger log = LoggerFactory.getLogger(CollaboratorResource.class);
    private final CollaboratorService collaboratorService;
    private final CollaboratorRepository collaboratorRepository;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public CollaboratorResource(CollaboratorService collaboratorService, CollaboratorRepository collaboratorRepository) {
        this.collaboratorService = collaboratorService;
        this.collaboratorRepository = collaboratorRepository;
    }

    /**
     * {@code POST  /collaborators} : Create a new collaborator.
     *
     * @param collaborator the collaborator to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new collaborator, or with status {@code 400 (Bad Request)} if the collaborator has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/collaborators")
    public ResponseEntity<Collaborator> createCollaborator(@RequestBody Collaborator collaborator) throws URISyntaxException {
        log.debug("REST request to save Collaborator : {}", collaborator);
        if (collaborator.getId() != null) {
            throw new BadRequestAlertException("A new collaborator cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Collaborator result = collaboratorService.createCollaborator(collaborator);
        return ResponseEntity
            .created(new URI("/api/collaborators/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /collaborators/:id} : Updates an existing collaborator.
     *
     * @param id           the id of the collaborator to save.
     * @param collaborator the collaborator to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated collaborator,
     * or with status {@code 400 (Bad Request)} if the collaborator is not valid,
     * or with status {@code 500 (Internal Server Error)} if the collaborator couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/collaborators/{id}")
    public ResponseEntity<Collaborator> updateCollaborator(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Collaborator collaborator
    ) throws URISyntaxException {
        log.debug("REST request to update Collaborator : {}, {}", id, collaborator);
        if (collaborator.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, collaborator.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!collaboratorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Collaborator result = collaboratorService.updateCollaborator(collaborator);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, collaborator.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /collaborators/:id} : Partial updates given fields of an existing collaborator, field will ignore if it is null
     *
     * @param id           the id of the collaborator to save.
     * @param collaborator the collaborator to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated collaborator,
     * or with status {@code 400 (Bad Request)} if the collaborator is not valid,
     * or with status {@code 404 (Not Found)} if the collaborator is not found,
     * or with status {@code 500 (Internal Server Error)} if the collaborator couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/collaborators/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<Collaborator> partialUpdateCollaborator(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Collaborator collaborator
    ) throws URISyntaxException {
        log.debug("REST request to partial update Collaborator partially : {}, {}", id, collaborator);
        if (collaborator.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, collaborator.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!collaboratorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Collaborator> result = collaboratorService.partialUpdate(collaborator);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, collaborator.getId())
        );
    }

    /**
     * {@code GET  /collaborators} : get all the collaborators.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of collaborators in body.
     */
    @GetMapping("/collaborators")
    public ResponseEntity<List<Collaborator>> getAllCollaborators(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Collaborators");
        Page<Collaborator> page = collaboratorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /collaborators/:id} : get the "id" collaborator.
     *
     * @param id the id of the collaborator to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the collaborator, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/collaborators/{id}")
    public ResponseEntity<Collaborator> getCollaborator(@PathVariable String id) {
        log.debug("REST request to get Collaborator : {}", id);
        Optional<Collaborator> collaborator = collaboratorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(collaborator);
    }

    /**
     * {@code DELETE  /collaborators/:id} : delete the "id" collaborator.
     *
     * @param id the id of the collaborator to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/collaborators/{id}")
    public ResponseEntity<Void> deleteCollaborator(@PathVariable String id) {
        log.debug("REST request to delete Collaborator : {}", id);
        collaboratorService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }


    @GetMapping("/collaborator-by-current-user")
    public ResponseEntity<Collaborator> getCollaboratorByCurrentUser() {
        Optional<Collaborator> result = collaboratorService.collaboratorByCurrentUser();
        return ResponseUtil.wrapOrNotFound(
            result);
    }

}
