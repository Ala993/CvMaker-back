package com.cv.maker.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cv.maker.IntegrationTest;
import com.cv.maker.domain.Experience;
import com.cv.maker.repository.ExperienceRepository;
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
 * Integration tests for the {@link ExperienceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExperienceResourceIT {

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_COMPANY = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY = "BBBBBBBBBB";

    private static final String DEFAULT_POSITION = "AAAAAAAAAA";
    private static final String UPDATED_POSITION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/experiences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ExperienceRepository experienceRepository;

    @Autowired
    private MockMvc restExperienceMockMvc;

    private Experience experience;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Experience createEntity() {
        Experience experience = new Experience()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .company(DEFAULT_COMPANY)
            .position(DEFAULT_POSITION);
        return experience;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Experience createUpdatedEntity() {
        Experience experience = new Experience()
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .company(UPDATED_COMPANY)
            .position(UPDATED_POSITION);
        return experience;
    }

    @BeforeEach
    public void initTest() {
        experienceRepository.deleteAll();
        experience = createEntity();
    }

    @Test
    void createExperience() throws Exception {
        int databaseSizeBeforeCreate = experienceRepository.findAll().size();
        // Create the Experience
        restExperienceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(experience)))
            .andExpect(status().isCreated());

        // Validate the Experience in the database
        List<Experience> experienceList = experienceRepository.findAll();
        assertThat(experienceList).hasSize(databaseSizeBeforeCreate + 1);
        Experience testExperience = experienceList.get(experienceList.size() - 1);
        assertThat(testExperience.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testExperience.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testExperience.getCompany()).isEqualTo(DEFAULT_COMPANY);
        assertThat(testExperience.getPosition()).isEqualTo(DEFAULT_POSITION);
    }

    @Test
    void createExperienceWithExistingId() throws Exception {
        // Create the Experience with an existing ID
        experience.setId("existing_id");

        int databaseSizeBeforeCreate = experienceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExperienceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(experience)))
            .andExpect(status().isBadRequest());

        // Validate the Experience in the database
        List<Experience> experienceList = experienceRepository.findAll();
        assertThat(experienceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllExperiences() throws Exception {
        // Initialize the database
        experienceRepository.save(experience);

        // Get all the experienceList
        restExperienceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(experience.getId())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].company").value(hasItem(DEFAULT_COMPANY)))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));
    }

    @Test
    void getExperience() throws Exception {
        // Initialize the database
        experienceRepository.save(experience);

        // Get the experience
        restExperienceMockMvc
            .perform(get(ENTITY_API_URL_ID, experience.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(experience.getId()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.company").value(DEFAULT_COMPANY))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION));
    }

    @Test
    void getNonExistingExperience() throws Exception {
        // Get the experience
        restExperienceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewExperience() throws Exception {
        // Initialize the database
        experienceRepository.save(experience);

        int databaseSizeBeforeUpdate = experienceRepository.findAll().size();

        // Update the experience
        Experience updatedExperience = experienceRepository.findById(experience.getId()).get();
        updatedExperience.startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).company(UPDATED_COMPANY).position(UPDATED_POSITION);

        restExperienceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExperience.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExperience))
            )
            .andExpect(status().isOk());

        // Validate the Experience in the database
        List<Experience> experienceList = experienceRepository.findAll();
        assertThat(experienceList).hasSize(databaseSizeBeforeUpdate);
        Experience testExperience = experienceList.get(experienceList.size() - 1);
        assertThat(testExperience.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testExperience.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testExperience.getCompany()).isEqualTo(UPDATED_COMPANY);
        assertThat(testExperience.getPosition()).isEqualTo(UPDATED_POSITION);
    }

    @Test
    void putNonExistingExperience() throws Exception {
        int databaseSizeBeforeUpdate = experienceRepository.findAll().size();
        experience.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExperienceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, experience.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(experience))
            )
            .andExpect(status().isBadRequest());

        // Validate the Experience in the database
        List<Experience> experienceList = experienceRepository.findAll();
        assertThat(experienceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchExperience() throws Exception {
        int databaseSizeBeforeUpdate = experienceRepository.findAll().size();
        experience.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExperienceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(experience))
            )
            .andExpect(status().isBadRequest());

        // Validate the Experience in the database
        List<Experience> experienceList = experienceRepository.findAll();
        assertThat(experienceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamExperience() throws Exception {
        int databaseSizeBeforeUpdate = experienceRepository.findAll().size();
        experience.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExperienceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(experience)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Experience in the database
        List<Experience> experienceList = experienceRepository.findAll();
        assertThat(experienceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateExperienceWithPatch() throws Exception {
        // Initialize the database
        experienceRepository.save(experience);

        int databaseSizeBeforeUpdate = experienceRepository.findAll().size();

        // Update the experience using partial update
        Experience partialUpdatedExperience = new Experience();
        partialUpdatedExperience.setId(experience.getId());

        partialUpdatedExperience.endDate(UPDATED_END_DATE);

        restExperienceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExperience.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExperience))
            )
            .andExpect(status().isOk());

        // Validate the Experience in the database
        List<Experience> experienceList = experienceRepository.findAll();
        assertThat(experienceList).hasSize(databaseSizeBeforeUpdate);
        Experience testExperience = experienceList.get(experienceList.size() - 1);
        assertThat(testExperience.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testExperience.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testExperience.getCompany()).isEqualTo(DEFAULT_COMPANY);
        assertThat(testExperience.getPosition()).isEqualTo(DEFAULT_POSITION);
    }

    @Test
    void fullUpdateExperienceWithPatch() throws Exception {
        // Initialize the database
        experienceRepository.save(experience);

        int databaseSizeBeforeUpdate = experienceRepository.findAll().size();

        // Update the experience using partial update
        Experience partialUpdatedExperience = new Experience();
        partialUpdatedExperience.setId(experience.getId());

        partialUpdatedExperience
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .company(UPDATED_COMPANY)
            .position(UPDATED_POSITION);

        restExperienceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExperience.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExperience))
            )
            .andExpect(status().isOk());

        // Validate the Experience in the database
        List<Experience> experienceList = experienceRepository.findAll();
        assertThat(experienceList).hasSize(databaseSizeBeforeUpdate);
        Experience testExperience = experienceList.get(experienceList.size() - 1);
        assertThat(testExperience.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testExperience.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testExperience.getCompany()).isEqualTo(UPDATED_COMPANY);
        assertThat(testExperience.getPosition()).isEqualTo(UPDATED_POSITION);
    }

    @Test
    void patchNonExistingExperience() throws Exception {
        int databaseSizeBeforeUpdate = experienceRepository.findAll().size();
        experience.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExperienceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, experience.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(experience))
            )
            .andExpect(status().isBadRequest());

        // Validate the Experience in the database
        List<Experience> experienceList = experienceRepository.findAll();
        assertThat(experienceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchExperience() throws Exception {
        int databaseSizeBeforeUpdate = experienceRepository.findAll().size();
        experience.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExperienceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(experience))
            )
            .andExpect(status().isBadRequest());

        // Validate the Experience in the database
        List<Experience> experienceList = experienceRepository.findAll();
        assertThat(experienceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamExperience() throws Exception {
        int databaseSizeBeforeUpdate = experienceRepository.findAll().size();
        experience.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExperienceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(experience))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Experience in the database
        List<Experience> experienceList = experienceRepository.findAll();
        assertThat(experienceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteExperience() throws Exception {
        // Initialize the database
        experienceRepository.save(experience);

        int databaseSizeBeforeDelete = experienceRepository.findAll().size();

        // Delete the experience
        restExperienceMockMvc
            .perform(delete(ENTITY_API_URL_ID, experience.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Experience> experienceList = experienceRepository.findAll();
        assertThat(experienceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
