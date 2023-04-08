package com.cv.maker.repository;

import com.cv.maker.domain.Cv;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Cv entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CvRepository extends MongoRepository<Cv, String> {}
