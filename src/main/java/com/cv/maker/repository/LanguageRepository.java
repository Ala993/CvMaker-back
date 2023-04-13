package com.cv.maker.repository;

import com.cv.maker.domain.Language;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Language entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LanguageRepository extends MongoRepository<Language, String> {}
