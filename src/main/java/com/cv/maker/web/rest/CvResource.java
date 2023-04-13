package com.cv.maker.web.rest;

import com.cv.maker.domain.Cv;
import com.cv.maker.repository.CollaboratorRepository;
import com.cv.maker.repository.CvRepository;
import com.cv.maker.service.CvService;
import com.cv.maker.service.dto.CvFilter;
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
 * REST controller for managing {@link com.cv.maker.domain.Cv}.
 */
@RestController
@RequestMapping("/api")
public class CvResource {

    private final Logger log = LoggerFactory.getLogger(CvResource.class);

    private static final String ENTITY_NAME = "cv";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CvService cvService;

    private final CvRepository cvRepository;


    public CvResource(CvService cvService, CvRepository cvRepository) {
        this.cvService = cvService;
        this.cvRepository = cvRepository;
    }

    /**
     * {@code POST  /cvs} : Create a new cv.
     *
     * @param cv the cv to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cv, or with status {@code 400 (Bad Request)} if the cv has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cvs")
    public ResponseEntity<Cv> createCv(@RequestBody Cv cv) throws URISyntaxException {
        log.debug("REST request to save Cv : {}", cv);
        if (cv.getId() != null) {
            throw new BadRequestAlertException("A new cv cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cv result = cvService.save(cv);
        return ResponseEntity
            .created(new URI("/api/cvs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /cvs/:id} : Updates an existing cv.
     *
     * @param id the id of the cv to save.
     * @param cv the cv to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cv,
     * or with status {@code 400 (Bad Request)} if the cv is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cv couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cvs/{id}")
    public ResponseEntity<Cv> updateCv(@PathVariable(value = "id", required = false) final String id, @RequestBody Cv cv)
        throws URISyntaxException {
        log.debug("REST request to update Cv : {}, {}", id, cv);
        if (cv.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cv.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cvRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Cv result = cvService.save(cv);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cv.getId())).body(result);
    }

    /**
     * {@code PATCH  /cvs/:id} : Partial updates given fields of an existing cv, field will ignore if it is null
     *
     * @param id the id of the cv to save.
     * @param cv the cv to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cv,
     * or with status {@code 400 (Bad Request)} if the cv is not valid,
     * or with status {@code 404 (Not Found)} if the cv is not found,
     * or with status {@code 500 (Internal Server Error)} if the cv couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cvs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Cv> partialUpdateCv(@PathVariable(value = "id", required = false) final String id, @RequestBody Cv cv)
        throws URISyntaxException {
        log.debug("REST request to partial update Cv partially : {}, {}", id, cv);
        if (cv.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cv.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cvRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Cv> result = cvService.partialUpdate(cv);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cv.getId()));
    }

    /**
     * {@code GET  /cvs} : get all the cvs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cvs in body.
     */
    @GetMapping("/cvs")
    public ResponseEntity<List<Cv>> getAllCvs(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Cvs");
        Page<Cv> page = cvService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cvs/:id} : get the "id" cv.
     *
     * @param id the id of the cv to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cv, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cvs/{id}")
    public ResponseEntity<Cv> getCv(@PathVariable String id) {
        log.debug("REST request to get Cv : {}", id);
        Optional<Cv> cv = cvService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cv);
    }

    /**
     * {@code DELETE  /cvs/:id} : delete the "id" cv.
     *
     * @param id the id of the cv to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cvs/{id}")
    public ResponseEntity<Void> deleteCv(@PathVariable String id) {
        log.debug("REST request to delete Cv : {}", id);
        cvService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    @PostMapping("/cvs-filtered")
    public ResponseEntity<List<Cv>> findCvsByFilter(@RequestBody CvFilter cvFilter) {
        List<Cv> cvs = cvService.findCvsByFilter(cvFilter);
        return ResponseEntity.ok().body(cvs);
    }
}
