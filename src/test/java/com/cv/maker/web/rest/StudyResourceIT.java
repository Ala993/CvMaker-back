package com.cv.maker.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cv.maker.IntegrationTest;
import com.cv.maker.domain.Study;
import com.cv.maker.repository.StudyRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link StudyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StudyResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_ESTABLISHMENT = "AAAAAAAAAA";
    private static final String UPDATED_ESTABLISHMENT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/studies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private MockMvc restStudyMockMvc;

    private Study study;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Study createEntity() {
        Study study = new Study()
            .title(DEFAULT_TITLE)
            .location(DEFAULT_LOCATION)
            .establishment(DEFAULT_ESTABLISHMENT)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .description(DEFAULT_DESCRIPTION);
        return study;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Study createUpdatedEntity() {
        Study study = new Study()
            .title(UPDATED_TITLE)
            .location(UPDATED_LOCATION)
            .establishment(UPDATED_ESTABLISHMENT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .description(UPDATED_DESCRIPTION);
        return study;
    }

    @BeforeEach
    public void initTest() {
        studyRepository.deleteAll();
        study = createEntity();
    }

    @Test
    void createStudy() throws Exception {
        int databaseSizeBeforeCreate = studyRepository.findAll().size();
        // Create the Study
        restStudyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(study)))
            .andExpect(status().isCreated());

        // Validate the Study in the database
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeCreate + 1);
        Study testStudy = studyList.get(studyList.size() - 1);
        assertThat(testStudy.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testStudy.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testStudy.getEstablishment()).isEqualTo(DEFAULT_ESTABLISHMENT);
        assertThat(testStudy.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testStudy.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testStudy.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void createStudyWithExistingId() throws Exception {
        // Create the Study with an existing ID
        study.setId("existing_id");

        int databaseSizeBeforeCreate = studyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(study)))
            .andExpect(status().isBadRequest());

        // Validate the Study in the database
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllStudies() throws Exception {
        // Initialize the database
        studyRepository.save(study);

        // Get all the studyList
        restStudyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(study.getId())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].establishment").value(hasItem(DEFAULT_ESTABLISHMENT)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    void getStudy() throws Exception {
        // Initialize the database
        studyRepository.save(study);

        // Get the study
        restStudyMockMvc
            .perform(get(ENTITY_API_URL_ID, study.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(study.getId()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.establishment").value(DEFAULT_ESTABLISHMENT))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    void getNonExistingStudy() throws Exception {
        // Get the study
        restStudyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewStudy() throws Exception {
        // Initialize the database
        studyRepository.save(study);

        int databaseSizeBeforeUpdate = studyRepository.findAll().size();

        // Update the study
        Study updatedStudy = studyRepository.findById(study.getId()).get();
        updatedStudy
            .title(UPDATED_TITLE)
            .location(UPDATED_LOCATION)
            .establishment(UPDATED_ESTABLISHMENT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .description(UPDATED_DESCRIPTION);

        restStudyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStudy.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStudy))
            )
            .andExpect(status().isOk());

        // Validate the Study in the database
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeUpdate);
        Study testStudy = studyList.get(studyList.size() - 1);
        assertThat(testStudy.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testStudy.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testStudy.getEstablishment()).isEqualTo(UPDATED_ESTABLISHMENT);
        assertThat(testStudy.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testStudy.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testStudy.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void putNonExistingStudy() throws Exception {
        int databaseSizeBeforeUpdate = studyRepository.findAll().size();
        study.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, study.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(study))
            )
            .andExpect(status().isBadRequest());

        // Validate the Study in the database
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchStudy() throws Exception {
        int databaseSizeBeforeUpdate = studyRepository.findAll().size();
        study.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(study))
            )
            .andExpect(status().isBadRequest());

        // Validate the Study in the database
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamStudy() throws Exception {
        int databaseSizeBeforeUpdate = studyRepository.findAll().size();
        study.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(study)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Study in the database
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateStudyWithPatch() throws Exception {
        // Initialize the database
        studyRepository.save(study);

        int databaseSizeBeforeUpdate = studyRepository.findAll().size();

        // Update the study using partial update
        Study partialUpdatedStudy = new Study();
        partialUpdatedStudy.setId(study.getId());

        partialUpdatedStudy.title(UPDATED_TITLE).location(UPDATED_LOCATION).establishment(UPDATED_ESTABLISHMENT).endDate(UPDATED_END_DATE);

        restStudyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudy.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudy))
            )
            .andExpect(status().isOk());

        // Validate the Study in the database
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeUpdate);
        Study testStudy = studyList.get(studyList.size() - 1);
        assertThat(testStudy.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testStudy.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testStudy.getEstablishment()).isEqualTo(UPDATED_ESTABLISHMENT);
        assertThat(testStudy.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testStudy.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testStudy.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void fullUpdateStudyWithPatch() throws Exception {
        // Initialize the database
        studyRepository.save(study);

        int databaseSizeBeforeUpdate = studyRepository.findAll().size();

        // Update the study using partial update
        Study partialUpdatedStudy = new Study();
        partialUpdatedStudy.setId(study.getId());

        partialUpdatedStudy
            .title(UPDATED_TITLE)
            .location(UPDATED_LOCATION)
            .establishment(UPDATED_ESTABLISHMENT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .description(UPDATED_DESCRIPTION);

        restStudyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudy.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudy))
            )
            .andExpect(status().isOk());

        // Validate the Study in the database
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeUpdate);
        Study testStudy = studyList.get(studyList.size() - 1);
        assertThat(testStudy.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testStudy.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testStudy.getEstablishment()).isEqualTo(UPDATED_ESTABLISHMENT);
        assertThat(testStudy.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testStudy.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testStudy.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void patchNonExistingStudy() throws Exception {
        int databaseSizeBeforeUpdate = studyRepository.findAll().size();
        study.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, study.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(study))
            )
            .andExpect(status().isBadRequest());

        // Validate the Study in the database
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchStudy() throws Exception {
        int databaseSizeBeforeUpdate = studyRepository.findAll().size();
        study.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(study))
            )
            .andExpect(status().isBadRequest());

        // Validate the Study in the database
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamStudy() throws Exception {
        int databaseSizeBeforeUpdate = studyRepository.findAll().size();
        study.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(study)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Study in the database
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteStudy() throws Exception {
        // Initialize the database
        studyRepository.save(study);

        int databaseSizeBeforeDelete = studyRepository.findAll().size();

        // Delete the study
        restStudyMockMvc
            .perform(delete(ENTITY_API_URL_ID, study.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
