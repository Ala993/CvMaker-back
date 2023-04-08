package com.cv.maker.repository;

import com.cv.maker.domain.FileEntry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the FileEntry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FileEntryRepository extends MongoRepository<FileEntry, String> {}
