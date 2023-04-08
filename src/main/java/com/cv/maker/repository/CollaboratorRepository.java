package com.cv.maker.repository;

import com.cv.maker.domain.Collaborator;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data MongoDB repository for the Collaborator entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CollaboratorRepository extends MongoRepository<Collaborator, String> {

    Optional<Collaborator> findOneByUserId(String id);

    Optional<Collaborator> findOneByCvId(String id);
}
