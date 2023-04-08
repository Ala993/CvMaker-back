package com.cv.maker.service;

import com.cv.maker.domain.FileEntry;
import com.cv.maker.repository.FileEntryRepository;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing {@link FileEntry}.
 */
@Service
public class FileEntryService {

    private final Logger log = LoggerFactory.getLogger(FileEntryService.class);

    private final FileEntryRepository fileEntryRepository;

    public FileEntryService(FileEntryRepository fileEntryRepository) {
        this.fileEntryRepository = fileEntryRepository;
    }

    /**
     * Save a fileEntry.
     *
     * @param fileEntry the entity to save.
     * @return the persisted entity.
     */
    public FileEntry save(FileEntry fileEntry) {
        log.debug("Request to save FileEntry : {}", fileEntry);
        return fileEntryRepository.save(fileEntry);
    }

    /**
     * Partially update a fileEntry.
     *
     * @param fileEntry the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FileEntry> partialUpdate(FileEntry fileEntry) {
        log.debug("Request to partially update FileEntry : {}", fileEntry);

        return fileEntryRepository
            .findById(fileEntry.getId())
            .map(existingFileEntry -> {
                return existingFileEntry;
            })
            .map(fileEntryRepository::save);
    }

    /**
     * Get all the fileEntries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<FileEntry> findAll(Pageable pageable) {
        log.debug("Request to get all FileEntries");
        return fileEntryRepository.findAll(pageable);
    }

    /**
     * Get one fileEntry by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<FileEntry> findOne(String id) {
        log.debug("Request to get FileEntry : {}", id);
        return fileEntryRepository.findById(id);
    }

    /**
     * Delete the fileEntry by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete FileEntry : {}", id);
        fileEntryRepository.deleteById(id);
    }

    public FileEntry store (MultipartFile multipartFile) throws IOException {
        FileEntry fileEntry = new FileEntry();
        fileEntry.setData(multipartFile.getBytes());
        return fileEntryRepository.save(fileEntry);
    }
}
