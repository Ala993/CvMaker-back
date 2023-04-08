package com.cv.maker.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cv.maker.IntegrationTest;
import com.cv.maker.domain.HumanResource;
import com.cv.maker.repository.HumanResourceRepository;
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
 * Integration tests for the {@link HumanResourceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HumanResourceResourceIT {

    private static final String ENTITY_API_URL = "/api/human-resources";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private HumanResourceRepository humanResourceRepository;

    @Autowired
    private MockMvc restHumanResourceMockMvc;

    private HumanResource humanResource;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HumanResource createEntity() {
        HumanResource humanResource = new HumanResource();
        return humanResource;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HumanResource createUpdatedEntity() {
        HumanResource humanResource = new HumanResource();
        return humanResource;
    }

    @BeforeEach
    public void initTest() {
        humanResourceRepository.deleteAll();
        humanResource = createEntity();
    }

    @Test
    void createHumanResource() throws Exception {
        int databaseSizeBeforeCreate = humanResourceRepository.findAll().size();
        // Create the HumanResource
        restHumanResourceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(humanResource)))
            .andExpect(status().isCreated());

        // Validate the HumanResource in the database
        List<HumanResource> humanResourceList = humanResourceRepository.findAll();
        assertThat(humanResourceList).hasSize(databaseSizeBeforeCreate + 1);
        HumanResource testHumanResource = humanResourceList.get(humanResourceList.size() - 1);
    }

    @Test
    void createHumanResourceWithExistingId() throws Exception {
        // Create the HumanResource with an existing ID
        humanResource.setId("existing_id");

        int databaseSizeBeforeCreate = humanResourceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHumanResourceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(humanResource)))
            .andExpect(status().isBadRequest());

        // Validate the HumanResource in the database
        List<HumanResource> humanResourceList = humanResourceRepository.findAll();
        assertThat(humanResourceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllHumanResources() throws Exception {
        // Initialize the database
        humanResourceRepository.save(humanResource);

        // Get all the humanResourceList
        restHumanResourceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(humanResource.getId())));
    }

    @Test
    void getHumanResource() throws Exception {
        // Initialize the database
        humanResourceRepository.save(humanResource);

        // Get the humanResource
        restHumanResourceMockMvc
            .perform(get(ENTITY_API_URL_ID, humanResource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(humanResource.getId()));
    }

    @Test
    void getNonExistingHumanResource() throws Exception {
        // Get the humanResource
        restHumanResourceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewHumanResource() throws Exception {
        // Initialize the database
        humanResourceRepository.save(humanResource);

        int databaseSizeBeforeUpdate = humanResourceRepository.findAll().size();

        // Update the humanResource
        HumanResource updatedHumanResource = humanResourceRepository.findById(humanResource.getId()).get();

        restHumanResourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHumanResource.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHumanResource))
            )
            .andExpect(status().isOk());

        // Validate the HumanResource in the database
        List<HumanResource> humanResourceList = humanResourceRepository.findAll();
        assertThat(humanResourceList).hasSize(databaseSizeBeforeUpdate);
        HumanResource testHumanResource = humanResourceList.get(humanResourceList.size() - 1);
    }

    @Test
    void putNonExistingHumanResource() throws Exception {
        int databaseSizeBeforeUpdate = humanResourceRepository.findAll().size();
        humanResource.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHumanResourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, humanResource.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(humanResource))
            )
            .andExpect(status().isBadRequest());

        // Validate the HumanResource in the database
        List<HumanResource> humanResourceList = humanResourceRepository.findAll();
        assertThat(humanResourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchHumanResource() throws Exception {
        int databaseSizeBeforeUpdate = humanResourceRepository.findAll().size();
        humanResource.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHumanResourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(humanResource))
            )
            .andExpect(status().isBadRequest());

        // Validate the HumanResource in the database
        List<HumanResource> humanResourceList = humanResourceRepository.findAll();
        assertThat(humanResourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamHumanResource() throws Exception {
        int databaseSizeBeforeUpdate = humanResourceRepository.findAll().size();
        humanResource.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHumanResourceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(humanResource)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HumanResource in the database
        List<HumanResource> humanResourceList = humanResourceRepository.findAll();
        assertThat(humanResourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateHumanResourceWithPatch() throws Exception {
        // Initialize the database
        humanResourceRepository.save(humanResource);

        int databaseSizeBeforeUpdate = humanResourceRepository.findAll().size();

        // Update the humanResource using partial update
        HumanResource partialUpdatedHumanResource = new HumanResource();
        partialUpdatedHumanResource.setId(humanResource.getId());

        restHumanResourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHumanResource.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHumanResource))
            )
            .andExpect(status().isOk());

        // Validate the HumanResource in the database
        List<HumanResource> humanResourceList = humanResourceRepository.findAll();
        assertThat(humanResourceList).hasSize(databaseSizeBeforeUpdate);
        HumanResource testHumanResource = humanResourceList.get(humanResourceList.size() - 1);
    }

    @Test
    void fullUpdateHumanResourceWithPatch() throws Exception {
        // Initialize the database
        humanResourceRepository.save(humanResource);

        int databaseSizeBeforeUpdate = humanResourceRepository.findAll().size();

        // Update the humanResource using partial update
        HumanResource partialUpdatedHumanResource = new HumanResource();
        partialUpdatedHumanResource.setId(humanResource.getId());

        restHumanResourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHumanResource.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHumanResource))
            )
            .andExpect(status().isOk());

        // Validate the HumanResource in the database
        List<HumanResource> humanResourceList = humanResourceRepository.findAll();
        assertThat(humanResourceList).hasSize(databaseSizeBeforeUpdate);
        HumanResource testHumanResource = humanResourceList.get(humanResourceList.size() - 1);
    }

    @Test
    void patchNonExistingHumanResource() throws Exception {
        int databaseSizeBeforeUpdate = humanResourceRepository.findAll().size();
        humanResource.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHumanResourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, humanResource.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(humanResource))
            )
            .andExpect(status().isBadRequest());

        // Validate the HumanResource in the database
        List<HumanResource> humanResourceList = humanResourceRepository.findAll();
        assertThat(humanResourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchHumanResource() throws Exception {
        int databaseSizeBeforeUpdate = humanResourceRepository.findAll().size();
        humanResource.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHumanResourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(humanResource))
            )
            .andExpect(status().isBadRequest());

        // Validate the HumanResource in the database
        List<HumanResource> humanResourceList = humanResourceRepository.findAll();
        assertThat(humanResourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamHumanResource() throws Exception {
        int databaseSizeBeforeUpdate = humanResourceRepository.findAll().size();
        humanResource.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHumanResourceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(humanResource))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HumanResource in the database
        List<HumanResource> humanResourceList = humanResourceRepository.findAll();
        assertThat(humanResourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteHumanResource() throws Exception {
        // Initialize the database
        humanResourceRepository.save(humanResource);

        int databaseSizeBeforeDelete = humanResourceRepository.findAll().size();

        // Delete the humanResource
        restHumanResourceMockMvc
            .perform(delete(ENTITY_API_URL_ID, humanResource.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HumanResource> humanResourceList = humanResourceRepository.findAll();
        assertThat(humanResourceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
