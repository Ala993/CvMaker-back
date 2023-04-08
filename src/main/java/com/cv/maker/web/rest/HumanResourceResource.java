package com.cv.maker.web.rest;

import com.cv.maker.domain.HumanResource;
import com.cv.maker.repository.HumanResourceRepository;
import com.cv.maker.service.HumanResourceService;
import com.cv.maker.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.cv.maker.domain.HumanResource}.
 */
@RestController
@RequestMapping("/api")
public class HumanResourceResource {

    private final Logger log = LoggerFactory.getLogger(HumanResourceResource.class);

    private static final String ENTITY_NAME = "humanResource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HumanResourceService humanResourceService;

    private final HumanResourceRepository humanResourceRepository;

    public HumanResourceResource(HumanResourceService humanResourceService, HumanResourceRepository humanResourceRepository) {
        this.humanResourceService = humanResourceService;
        this.humanResourceRepository = humanResourceRepository;
    }

    /**
     * {@code POST  /human-resources} : Create a new humanResource.
     *
     * @param humanResource the humanResource to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new humanResource, or with status {@code 400 (Bad Request)} if the humanResource has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/human-resources")
    public ResponseEntity<HumanResource> createHumanResource(@RequestBody HumanResource humanResource) throws URISyntaxException {
        log.debug("REST request to save HumanResource : {}", humanResource);
        if (humanResource.getId() != null) {
            throw new BadRequestAlertException("A new humanResource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HumanResource result = humanResourceService.save(humanResource);
        return ResponseEntity
            .created(new URI("/api/human-resources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /human-resources/:id} : Updates an existing humanResource.
     *
     * @param id the id of the humanResource to save.
     * @param humanResource the humanResource to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated humanResource,
     * or with status {@code 400 (Bad Request)} if the humanResource is not valid,
     * or with status {@code 500 (Internal Server Error)} if the humanResource couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/human-resources/{id}")
    public ResponseEntity<HumanResource> updateHumanResource(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody HumanResource humanResource
    ) throws URISyntaxException {
        log.debug("REST request to update HumanResource : {}, {}", id, humanResource);
        if (humanResource.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, humanResource.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!humanResourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HumanResource result = humanResourceService.save(humanResource);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, humanResource.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /human-resources/:id} : Partial updates given fields of an existing humanResource, field will ignore if it is null
     *
     * @param id the id of the humanResource to save.
     * @param humanResource the humanResource to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated humanResource,
     * or with status {@code 400 (Bad Request)} if the humanResource is not valid,
     * or with status {@code 404 (Not Found)} if the humanResource is not found,
     * or with status {@code 500 (Internal Server Error)} if the humanResource couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/human-resources/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HumanResource> partialUpdateHumanResource(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody HumanResource humanResource
    ) throws URISyntaxException {
        log.debug("REST request to partial update HumanResource partially : {}, {}", id, humanResource);
        if (humanResource.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, humanResource.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!humanResourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HumanResource> result = humanResourceService.partialUpdate(humanResource);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, humanResource.getId())
        );
    }

    /**
     * {@code GET  /human-resources} : get all the humanResources.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of humanResources in body.
     */
    @GetMapping("/human-resources")
    public ResponseEntity<List<HumanResource>> getAllHumanResources(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of HumanResources");
        Page<HumanResource> page = humanResourceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /human-resources/:id} : get the "id" humanResource.
     *
     * @param id the id of the humanResource to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the humanResource, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/human-resources/{id}")
    public ResponseEntity<HumanResource> getHumanResource(@PathVariable String id) {
        log.debug("REST request to get HumanResource : {}", id);
        Optional<HumanResource> humanResource = humanResourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(humanResource);
    }

    /**
     * {@code DELETE  /human-resources/:id} : delete the "id" humanResource.
     *
     * @param id the id of the humanResource to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/human-resources/{id}")
    public ResponseEntity<Void> deleteHumanResource(@PathVariable String id) {
        log.debug("REST request to delete HumanResource : {}", id);
        humanResourceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
