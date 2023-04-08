package com.cv.maker.repository;

import com.cv.maker.domain.Study;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Study entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudyRepository extends MongoRepository<Study, String> {}
