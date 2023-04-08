package com.cv.maker.web.rest;

import com.cv.maker.domain.FileEntry;
import com.cv.maker.repository.FileEntryRepository;
import com.cv.maker.service.FileEntryService;
import com.cv.maker.web.rest.errors.BadRequestAlertException;

import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.cv.maker.domain.FileEntry}.
 */
@RestController
@RequestMapping("/api")
public class FileEntryResource {

    private final Logger log = LoggerFactory.getLogger(FileEntryResource.class);

    private static final String ENTITY_NAME = "fileEntry";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FileEntryService fileEntryService;

    private final FileEntryRepository fileEntryRepository;

    public FileEntryResource(FileEntryService fileEntryService, FileEntryRepository fileEntryRepository) {
        this.fileEntryService = fileEntryService;
        this.fileEntryRepository = fileEntryRepository;
    }

    /**
     * {@code POST  /file-entries} : Create a new fileEntry.
     *
     * @param fileEntry the fileEntry to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fileEntry, or with status {@code 400 (Bad Request)} if the fileEntry has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/file-entries")
    public ResponseEntity<FileEntry> createFileEntry(@RequestBody FileEntry fileEntry) throws URISyntaxException {
        log.debug("REST request to save FileEntry : {}", fileEntry);
        if (fileEntry.getId() != null) {
            throw new BadRequestAlertException("A new fileEntry cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FileEntry result = fileEntryService.save(fileEntry);
        return ResponseEntity
            .created(new URI("/api/file-entries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /file-entries/:id} : Updates an existing fileEntry.
     *
     * @param id the id of the fileEntry to save.
     * @param fileEntry the fileEntry to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileEntry,
     * or with status {@code 400 (Bad Request)} if the fileEntry is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fileEntry couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/file-entries/{id}")
    public ResponseEntity<FileEntry> updateFileEntry(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody FileEntry fileEntry
    ) throws URISyntaxException {
        log.debug("REST request to update FileEntry : {}, {}", id, fileEntry);
        if (fileEntry.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fileEntry.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fileEntryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FileEntry result = fileEntryService.save(fileEntry);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fileEntry.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /file-entries/:id} : Partial updates given fields of an existing fileEntry, field will ignore if it is null
     *
     * @param id the id of the fileEntry to save.
     * @param fileEntry the fileEntry to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileEntry,
     * or with status {@code 400 (Bad Request)} if the fileEntry is not valid,
     * or with status {@code 404 (Not Found)} if the fileEntry is not found,
     * or with status {@code 500 (Internal Server Error)} if the fileEntry couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/file-entries/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FileEntry> partialUpdateFileEntry(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody FileEntry fileEntry
    ) throws URISyntaxException {
        log.debug("REST request to partial update FileEntry partially : {}, {}", id, fileEntry);
        if (fileEntry.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fileEntry.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fileEntryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FileEntry> result = fileEntryService.partialUpdate(fileEntry);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fileEntry.getId())
        );
    }

    /**
     * {@code GET  /file-entries} : get all the fileEntries.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fileEntries in body.
     */
    @GetMapping("/file-entries")
    public ResponseEntity<List<FileEntry>> getAllFileEntries(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of FileEntries");
        Page<FileEntry> page = fileEntryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /file-entries/:id} : get the "id" fileEntry.
     *
     * @param id the id of the fileEntry to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fileEntry, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/file-entries/{id}")
    public ResponseEntity<FileEntry> getFileEntry(@PathVariable String id) {
        log.debug("REST request to get FileEntry : {}", id);
        Optional<FileEntry> fileEntry = fileEntryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fileEntry);
    }

    /**
     * {@code DELETE  /file-entries/:id} : delete the "id" fileEntry.
     *
     * @param id the id of the fileEntry to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/file-entries/{id}")
    public ResponseEntity<Void> deleteFileEntry(@PathVariable String id) {
        log.debug("REST request to delete FileEntry : {}", id);
        fileEntryService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    @PostMapping("/save-file")
    public ResponseEntity<FileEntry> saveFile(MultipartFile multipartFile) throws IOException {
        FileEntry fileEntry = fileEntryService.store(multipartFile);
        return  ResponseEntity.ok().body(fileEntry);
    }
}
