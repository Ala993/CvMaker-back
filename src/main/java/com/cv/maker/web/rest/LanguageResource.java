package com.cv.maker.web.rest;

import com.cv.maker.domain.Language;
import com.cv.maker.repository.LanguageRepository;
import com.cv.maker.service.LanguageService;
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
 * REST controller for managing {@link com.cv.maker.domain.Language}.
 */
@RestController
@RequestMapping("/api")
public class LanguageResource {

    private final Logger log = LoggerFactory.getLogger(LanguageResource.class);

    private static final String ENTITY_NAME = "language";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LanguageService languageService;

    private final LanguageRepository languageRepository;

    public LanguageResource(LanguageService languageService, LanguageRepository languageRepository) {
        this.languageService = languageService;
        this.languageRepository = languageRepository;
    }

    /**
     * {@code POST  /languages} : Create a new language.
     *
     * @param language the language to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new language, or with status {@code 400 (Bad Request)} if the language has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/languages")
    public ResponseEntity<Language> createLanguage(@RequestBody Language language) throws URISyntaxException {
        log.debug("REST request to save Language : {}", language);
        if (language.getId() != null) {
            throw new BadRequestAlertException("A new language cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Language result = languageService.save(language);
        return ResponseEntity
            .created(new URI("/api/languages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /languages/:id} : Updates an existing language.
     *
     * @param id the id of the language to save.
     * @param language the language to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated language,
     * or with status {@code 400 (Bad Request)} if the language is not valid,
     * or with status {@code 500 (Internal Server Error)} if the language couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/languages/{id}")
    public ResponseEntity<Language> updateLanguage(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Language language
    ) throws URISyntaxException {
        log.debug("REST request to update Language : {}, {}", id, language);
        if (language.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, language.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!languageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Language result = languageService.save(language);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, language.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /languages/:id} : Partial updates given fields of an existing language, field will ignore if it is null
     *
     * @param id the id of the language to save.
     * @param language the language to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated language,
     * or with status {@code 400 (Bad Request)} if the language is not valid,
     * or with status {@code 404 (Not Found)} if the language is not found,
     * or with status {@code 500 (Internal Server Error)} if the language couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/languages/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Language> partialUpdateLanguage(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Language language
    ) throws URISyntaxException {
        log.debug("REST request to partial update Language partially : {}, {}", id, language);
        if (language.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, language.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!languageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Language> result = languageService.partialUpdate(language);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, language.getId())
        );
    }

    /**
     * {@code GET  /languages} : get all the languages.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of languages in body.
     */
    @GetMapping("/languages")
    public ResponseEntity<List<Language>> getAllLanguages(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Languages");
        Page<Language> page = languageService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /languages/:id} : get the "id" language.
     *
     * @param id the id of the language to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the language, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/languages/{id}")
    public ResponseEntity<Language> getLanguage(@PathVariable String id) {
        log.debug("REST request to get Language : {}", id);
        Optional<Language> language = languageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(language);
    }

    /**
     * {@code DELETE  /languages/:id} : delete the "id" language.
     *
     * @param id the id of the language to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/languages/{id}")
    public ResponseEntity<Void> deleteLanguage(@PathVariable String id) {
        log.debug("REST request to delete Language : {}", id);
        languageService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
