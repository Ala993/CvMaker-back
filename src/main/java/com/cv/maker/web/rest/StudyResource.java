package com.cv.maker.web.rest;

import com.cv.maker.domain.Study;
import com.cv.maker.repository.StudyRepository;
import com.cv.maker.service.StudyService;
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
 * REST controller for managing {@link com.cv.maker.domain.Study}.
 */
@RestController
@RequestMapping("/api")
public class StudyResource {

    private final Logger log = LoggerFactory.getLogger(StudyResource.class);

    private static final String ENTITY_NAME = "study";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudyService studyService;

    private final StudyRepository studyRepository;

    public StudyResource(StudyService studyService, StudyRepository studyRepository) {
        this.studyService = studyService;
        this.studyRepository = studyRepository;
    }

    /**
     * {@code POST  /studies} : Create a new study.
     *
     * @param study the study to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new study, or with status {@code 400 (Bad Request)} if the study has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/studies")
    public ResponseEntity<Study> createStudy(@RequestBody Study study) throws URISyntaxException {
        log.debug("REST request to save Study : {}", study);
        if (study.getId() != null) {
            throw new BadRequestAlertException("A new study cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Study result = studyService.save(study);
        return ResponseEntity
            .created(new URI("/api/studies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /studies/:id} : Updates an existing study.
     *
     * @param id the id of the study to save.
     * @param study the study to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated study,
     * or with status {@code 400 (Bad Request)} if the study is not valid,
     * or with status {@code 500 (Internal Server Error)} if the study couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/studies/{id}")
    public ResponseEntity<Study> updateStudy(@PathVariable(value = "id", required = false) final String id, @RequestBody Study study)
        throws URISyntaxException {
        log.debug("REST request to update Study : {}, {}", id, study);
        if (study.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, study.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Study result = studyService.save(study);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, study.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /studies/:id} : Partial updates given fields of an existing study, field will ignore if it is null
     *
     * @param id the id of the study to save.
     * @param study the study to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated study,
     * or with status {@code 400 (Bad Request)} if the study is not valid,
     * or with status {@code 404 (Not Found)} if the study is not found,
     * or with status {@code 500 (Internal Server Error)} if the study couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/studies/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Study> partialUpdateStudy(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Study study
    ) throws URISyntaxException {
        log.debug("REST request to partial update Study partially : {}, {}", id, study);
        if (study.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, study.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Study> result = studyService.partialUpdate(study);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, study.getId()));
    }

    /**
     * {@code GET  /studies} : get all the studies.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studies in body.
     */
    @GetMapping("/studies")
    public ResponseEntity<List<Study>> getAllStudies(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Studies");
        Page<Study> page = studyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /studies/:id} : get the "id" study.
     *
     * @param id the id of the study to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the study, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/studies/{id}")
    public ResponseEntity<Study> getStudy(@PathVariable String id) {
        log.debug("REST request to get Study : {}", id);
        Optional<Study> study = studyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(study);
    }

    /**
     * {@code DELETE  /studies/:id} : delete the "id" study.
     *
     * @param id the id of the study to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/studies/{id}")
    public ResponseEntity<Void> deleteStudy(@PathVariable String id) {
        log.debug("REST request to delete Study : {}", id);
        studyService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
