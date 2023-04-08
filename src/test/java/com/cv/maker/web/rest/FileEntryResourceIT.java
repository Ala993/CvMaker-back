package com.cv.maker.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cv.maker.IntegrationTest;
import com.cv.maker.domain.FileEntry;
import com.cv.maker.repository.FileEntryRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link FileEntryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FileEntryResourceIT {

    private static final String ENTITY_API_URL = "/api/file-entries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private FileEntryRepository fileEntryRepository;

    @Autowired
    private MockMvc restFileEntryMockMvc;

    private FileEntry fileEntry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileEntry createEntity() {
        FileEntry fileEntry = new FileEntry();
        return fileEntry;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileEntry createUpdatedEntity() {
        FileEntry fileEntry = new FileEntry();
        return fileEntry;
    }

    @BeforeEach
    public void initTest() {
        fileEntryRepository.deleteAll();
        fileEntry = createEntity();
    }

    @Test
    void createFileEntry() throws Exception {
        int databaseSizeBeforeCreate = fileEntryRepository.findAll().size();
        // Create the FileEntry
        restFileEntryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileEntry)))
            .andExpect(status().isCreated());

        // Validate the FileEntry in the database
        List<FileEntry> fileEntryList = fileEntryRepository.findAll();
        assertThat(fileEntryList).hasSize(databaseSizeBeforeCreate + 1);
        FileEntry testFileEntry = fileEntryList.get(fileEntryList.size() - 1);
    }

    @Test
    void createFileEntryWithExistingId() throws Exception {
        // Create the FileEntry with an existing ID
        fileEntry.setId("existing_id");

        int databaseSizeBeforeCreate = fileEntryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFileEntryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileEntry)))
            .andExpect(status().isBadRequest());

        // Validate the FileEntry in the database
        List<FileEntry> fileEntryList = fileEntryRepository.findAll();
        assertThat(fileEntryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllFileEntries() throws Exception {
        // Initialize the database
        fileEntryRepository.save(fileEntry);

        // Get all the fileEntryList
        restFileEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileEntry.getId())));
    }

    @Test
    void getFileEntry() throws Exception {
        // Initialize the database
        fileEntryRepository.save(fileEntry);

        // Get the fileEntry
        restFileEntryMockMvc
            .perform(get(ENTITY_API_URL_ID, fileEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fileEntry.getId()));
    }

    @Test
    void getNonExistingFileEntry() throws Exception {
        // Get the fileEntry
        restFileEntryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewFileEntry() throws Exception {
        // Initialize the database
        fileEntryRepository.save(fileEntry);

        int databaseSizeBeforeUpdate = fileEntryRepository.findAll().size();

        // Update the fileEntry
        FileEntry updatedFileEntry = fileEntryRepository.findById(fileEntry.getId()).get();

        restFileEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFileEntry.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFileEntry))
            )
            .andExpect(status().isOk());

        // Validate the FileEntry in the database
        List<FileEntry> fileEntryList = fileEntryRepository.findAll();
        assertThat(fileEntryList).hasSize(databaseSizeBeforeUpdate);
        FileEntry testFileEntry = fileEntryList.get(fileEntryList.size() - 1);
    }

    @Test
    void putNonExistingFileEntry() throws Exception {
        int databaseSizeBeforeUpdate = fileEntryRepository.findAll().size();
        fileEntry.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fileEntry.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fileEntry))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileEntry in the database
        List<FileEntry> fileEntryList = fileEntryRepository.findAll();
        assertThat(fileEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFileEntry() throws Exception {
        int databaseSizeBeforeUpdate = fileEntryRepository.findAll().size();
        fileEntry.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fileEntry))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileEntry in the database
        List<FileEntry> fileEntryList = fileEntryRepository.findAll();
        assertThat(fileEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFileEntry() throws Exception {
        int databaseSizeBeforeUpdate = fileEntryRepository.findAll().size();
        fileEntry.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileEntryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileEntry)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FileEntry in the database
        List<FileEntry> fileEntryList = fileEntryRepository.findAll();
        assertThat(fileEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFileEntryWithPatch() throws Exception {
        // Initialize the database
        fileEntryRepository.save(fileEntry);

        int databaseSizeBeforeUpdate = fileEntryRepository.findAll().size();

        // Update the fileEntry using partial update
        FileEntry partialUpdatedFileEntry = new FileEntry();
        partialUpdatedFileEntry.setId(fileEntry.getId());

        restFileEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFileEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFileEntry))
            )
            .andExpect(status().isOk());

        // Validate the FileEntry in the database
        List<FileEntry> fileEntryList = fileEntryRepository.findAll();
        assertThat(fileEntryList).hasSize(databaseSizeBeforeUpdate);
        FileEntry testFileEntry = fileEntryList.get(fileEntryList.size() - 1);
    }

    @Test
    void fullUpdateFileEntryWithPatch() throws Exception {
        // Initialize the database
        fileEntryRepository.save(fileEntry);

        int databaseSizeBeforeUpdate = fileEntryRepository.findAll().size();

        // Update the fileEntry using partial update
        FileEntry partialUpdatedFileEntry = new FileEntry();
        partialUpdatedFileEntry.setId(fileEntry.getId());

        restFileEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFileEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFileEntry))
            )
            .andExpect(status().isOk());

        // Validate the FileEntry in the database
        List<FileEntry> fileEntryList = fileEntryRepository.findAll();
        assertThat(fileEntryList).hasSize(databaseSizeBeforeUpdate);
        FileEntry testFileEntry = fileEntryList.get(fileEntryList.size() - 1);
    }

    @Test
    void patchNonExistingFileEntry() throws Exception {
        int databaseSizeBeforeUpdate = fileEntryRepository.findAll().size();
        fileEntry.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fileEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fileEntry))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileEntry in the database
        List<FileEntry> fileEntryList = fileEntryRepository.findAll();
        assertThat(fileEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFileEntry() throws Exception {
        int databaseSizeBeforeUpdate = fileEntryRepository.findAll().size();
        fileEntry.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fileEntry))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileEntry in the database
        List<FileEntry> fileEntryList = fileEntryRepository.findAll();
        assertThat(fileEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFileEntry() throws Exception {
        int databaseSizeBeforeUpdate = fileEntryRepository.findAll().size();
        fileEntry.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileEntryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fileEntry))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FileEntry in the database
        List<FileEntry> fileEntryList = fileEntryRepository.findAll();
        assertThat(fileEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFileEntry() throws Exception {
        // Initialize the database
        fileEntryRepository.save(fileEntry);

        int databaseSizeBeforeDelete = fileEntryRepository.findAll().size();

        // Delete the fileEntry
        restFileEntryMockMvc
            .perform(delete(ENTITY_API_URL_ID, fileEntry.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FileEntry> fileEntryList = fileEntryRepository.findAll();
        assertThat(fileEntryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
