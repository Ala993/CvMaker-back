package com.cv.maker.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cv.maker.IntegrationTest;
import com.cv.maker.domain.Cv;
import com.cv.maker.repository.CvRepository;
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
 * Integration tests for the {@link CvResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CvResourceIT {

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final Long DEFAULT_PHONE_NUMBER = 1L;
    private static final Long UPDATED_PHONE_NUMBER = 2L;

    private static final Integer DEFAULT_POSTAL_CODE = 1;
    private static final Integer UPDATED_POSTAL_CODE = 2;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cvs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private CvRepository cvRepository;

    @Autowired
    private MockMvc restCvMockMvc;

    private Cv cv;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cv createEntity() {
        Cv cv = new Cv().address(DEFAULT_ADDRESS).phoneNumber(DEFAULT_PHONE_NUMBER).postalCode(DEFAULT_POSTAL_CODE).email(DEFAULT_EMAIL);
        return cv;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cv createUpdatedEntity() {
        Cv cv = new Cv().address(UPDATED_ADDRESS).phoneNumber(UPDATED_PHONE_NUMBER).postalCode(UPDATED_POSTAL_CODE).email(UPDATED_EMAIL);
        return cv;
    }

    @BeforeEach
    public void initTest() {
        cvRepository.deleteAll();
        cv = createEntity();
    }

    @Test
    void createCv() throws Exception {
        int databaseSizeBeforeCreate = cvRepository.findAll().size();
        // Create the Cv
        restCvMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cv)))
            .andExpect(status().isCreated());

        // Validate the Cv in the database
        List<Cv> cvList = cvRepository.findAll();
        assertThat(cvList).hasSize(databaseSizeBeforeCreate + 1);
        Cv testCv = cvList.get(cvList.size() - 1);
        assertThat(testCv.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testCv.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testCv.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testCv.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    void createCvWithExistingId() throws Exception {
        // Create the Cv with an existing ID
        cv.setId("existing_id");

        int databaseSizeBeforeCreate = cvRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCvMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cv)))
            .andExpect(status().isBadRequest());

        // Validate the Cv in the database
        List<Cv> cvList = cvRepository.findAll();
        assertThat(cvList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllCvs() throws Exception {
        // Initialize the database
        cvRepository.save(cv);

        // Get all the cvList
        restCvMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cv.getId())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    void getCv() throws Exception {
        // Initialize the database
        cvRepository.save(cv);

        // Get the cv
        restCvMockMvc
            .perform(get(ENTITY_API_URL_ID, cv.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cv.getId()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.intValue()))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    void getNonExistingCv() throws Exception {
        // Get the cv
        restCvMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewCv() throws Exception {
        // Initialize the database
        cvRepository.save(cv);

        int databaseSizeBeforeUpdate = cvRepository.findAll().size();

        // Update the cv
        Cv updatedCv = cvRepository.findById(cv.getId()).get();
        updatedCv.address(UPDATED_ADDRESS).phoneNumber(UPDATED_PHONE_NUMBER).postalCode(UPDATED_POSTAL_CODE).email(UPDATED_EMAIL);

        restCvMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCv.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCv))
            )
            .andExpect(status().isOk());

        // Validate the Cv in the database
        List<Cv> cvList = cvRepository.findAll();
        assertThat(cvList).hasSize(databaseSizeBeforeUpdate);
        Cv testCv = cvList.get(cvList.size() - 1);
        assertThat(testCv.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCv.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testCv.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testCv.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    void putNonExistingCv() throws Exception {
        int databaseSizeBeforeUpdate = cvRepository.findAll().size();
        cv.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCvMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cv.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cv))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cv in the database
        List<Cv> cvList = cvRepository.findAll();
        assertThat(cvList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCv() throws Exception {
        int databaseSizeBeforeUpdate = cvRepository.findAll().size();
        cv.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCvMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cv))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cv in the database
        List<Cv> cvList = cvRepository.findAll();
        assertThat(cvList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCv() throws Exception {
        int databaseSizeBeforeUpdate = cvRepository.findAll().size();
        cv.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCvMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cv)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cv in the database
        List<Cv> cvList = cvRepository.findAll();
        assertThat(cvList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCvWithPatch() throws Exception {
        // Initialize the database
        cvRepository.save(cv);

        int databaseSizeBeforeUpdate = cvRepository.findAll().size();

        // Update the cv using partial update
        Cv partialUpdatedCv = new Cv();
        partialUpdatedCv.setId(cv.getId());

        partialUpdatedCv.address(UPDATED_ADDRESS).postalCode(UPDATED_POSTAL_CODE);

        restCvMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCv.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCv))
            )
            .andExpect(status().isOk());

        // Validate the Cv in the database
        List<Cv> cvList = cvRepository.findAll();
        assertThat(cvList).hasSize(databaseSizeBeforeUpdate);
        Cv testCv = cvList.get(cvList.size() - 1);
        assertThat(testCv.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCv.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testCv.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testCv.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    void fullUpdateCvWithPatch() throws Exception {
        // Initialize the database
        cvRepository.save(cv);

        int databaseSizeBeforeUpdate = cvRepository.findAll().size();

        // Update the cv using partial update
        Cv partialUpdatedCv = new Cv();
        partialUpdatedCv.setId(cv.getId());

        partialUpdatedCv.address(UPDATED_ADDRESS).phoneNumber(UPDATED_PHONE_NUMBER).postalCode(UPDATED_POSTAL_CODE).email(UPDATED_EMAIL);

        restCvMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCv.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCv))
            )
            .andExpect(status().isOk());

        // Validate the Cv in the database
        List<Cv> cvList = cvRepository.findAll();
        assertThat(cvList).hasSize(databaseSizeBeforeUpdate);
        Cv testCv = cvList.get(cvList.size() - 1);
        assertThat(testCv.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCv.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testCv.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testCv.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    void patchNonExistingCv() throws Exception {
        int databaseSizeBeforeUpdate = cvRepository.findAll().size();
        cv.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCvMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cv.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cv))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cv in the database
        List<Cv> cvList = cvRepository.findAll();
        assertThat(cvList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCv() throws Exception {
        int databaseSizeBeforeUpdate = cvRepository.findAll().size();
        cv.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCvMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cv))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cv in the database
        List<Cv> cvList = cvRepository.findAll();
        assertThat(cvList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCv() throws Exception {
        int databaseSizeBeforeUpdate = cvRepository.findAll().size();
        cv.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCvMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cv)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cv in the database
        List<Cv> cvList = cvRepository.findAll();
        assertThat(cvList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCv() throws Exception {
        // Initialize the database
        cvRepository.save(cv);

        int databaseSizeBeforeDelete = cvRepository.findAll().size();

        // Delete the cv
        restCvMockMvc.perform(delete(ENTITY_API_URL_ID, cv.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cv> cvList = cvRepository.findAll();
        assertThat(cvList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
