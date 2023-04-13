package com.cv.maker.service;

import com.cv.maker.domain.Language;
import com.cv.maker.repository.LanguageRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Language}.
 */
@Service
public class LanguageService {

    private final Logger log = LoggerFactory.getLogger(LanguageService.class);

    private final LanguageRepository languageRepository;

    public LanguageService(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    /**
     * Save a language.
     *
     * @param language the entity to save.
     * @return the persisted entity.
     */
    public Language save(Language language) {
        log.debug("Request to save Language : {}", language);
        return languageRepository.save(language);
    }

    /**
     * Partially update a language.
     *
     * @param language the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Language> partialUpdate(Language language) {
        log.debug("Request to partially update Language : {}", language);

        return languageRepository
            .findById(language.getId())
            .map(existingLanguage -> {
                if (language.getName() != null) {
                    existingLanguage.setName(language.getName());
                }
                if (language.getLevel() != null) {
                    existingLanguage.setLevel(language.getLevel());
                }

                return existingLanguage;
            })
            .map(languageRepository::save);
    }

    /**
     * Get all the languages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<Language> findAll(Pageable pageable) {
        log.debug("Request to get all Languages");
        return languageRepository.findAll(pageable);
    }

    /**
     * Get one language by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<Language> findOne(String id) {
        log.debug("Request to get Language : {}", id);
        return languageRepository.findById(id);
    }

    /**
     * Delete the language by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Language : {}", id);
        languageRepository.deleteById(id);
    }
}
