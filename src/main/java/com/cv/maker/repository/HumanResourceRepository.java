package com.cv.maker.repository;

import com.cv.maker.domain.HumanResource;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the HumanResource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HumanResourceRepository extends MongoRepository<HumanResource, String> {}
