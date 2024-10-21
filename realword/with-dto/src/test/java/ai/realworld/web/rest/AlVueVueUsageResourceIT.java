package ai.realworld.web.rest;

import static ai.realworld.domain.AlVueVueUsageAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlVueVueUsage;
import ai.realworld.domain.JohnLennon;
import ai.realworld.repository.AlVueVueUsageRepository;
import ai.realworld.service.dto.AlVueVueUsageDTO;
import ai.realworld.service.mapper.AlVueVueUsageMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AlVueVueUsageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlVueVueUsageResourceIT {

    private static final String ENTITY_API_URL = "/api/al-vue-vue-usages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlVueVueUsageRepository alVueVueUsageRepository;

    @Autowired
    private AlVueVueUsageMapper alVueVueUsageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlVueVueUsageMockMvc;

    private AlVueVueUsage alVueVueUsage;

    private AlVueVueUsage insertedAlVueVueUsage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlVueVueUsage createEntity() {
        return new AlVueVueUsage();
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlVueVueUsage createUpdatedEntity() {
        return new AlVueVueUsage();
    }

    @BeforeEach
    public void initTest() {
        alVueVueUsage = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlVueVueUsage != null) {
            alVueVueUsageRepository.delete(insertedAlVueVueUsage);
            insertedAlVueVueUsage = null;
        }
    }

    @Test
    @Transactional
    void createAlVueVueUsage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlVueVueUsage
        AlVueVueUsageDTO alVueVueUsageDTO = alVueVueUsageMapper.toDto(alVueVueUsage);
        var returnedAlVueVueUsageDTO = om.readValue(
            restAlVueVueUsageMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alVueVueUsageDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlVueVueUsageDTO.class
        );

        // Validate the AlVueVueUsage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlVueVueUsage = alVueVueUsageMapper.toEntity(returnedAlVueVueUsageDTO);
        assertAlVueVueUsageUpdatableFieldsEquals(returnedAlVueVueUsage, getPersistedAlVueVueUsage(returnedAlVueVueUsage));

        insertedAlVueVueUsage = returnedAlVueVueUsage;
    }

    @Test
    @Transactional
    void createAlVueVueUsageWithExistingId() throws Exception {
        // Create the AlVueVueUsage with an existing ID
        insertedAlVueVueUsage = alVueVueUsageRepository.saveAndFlush(alVueVueUsage);
        AlVueVueUsageDTO alVueVueUsageDTO = alVueVueUsageMapper.toDto(alVueVueUsage);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlVueVueUsageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alVueVueUsageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlVueVueUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAlVueVueUsages() throws Exception {
        // Initialize the database
        insertedAlVueVueUsage = alVueVueUsageRepository.saveAndFlush(alVueVueUsage);

        // Get all the alVueVueUsageList
        restAlVueVueUsageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alVueVueUsage.getId().toString())));
    }

    @Test
    @Transactional
    void getAlVueVueUsage() throws Exception {
        // Initialize the database
        insertedAlVueVueUsage = alVueVueUsageRepository.saveAndFlush(alVueVueUsage);

        // Get the alVueVueUsage
        restAlVueVueUsageMockMvc
            .perform(get(ENTITY_API_URL_ID, alVueVueUsage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alVueVueUsage.getId().toString()));
    }

    @Test
    @Transactional
    void getAlVueVueUsagesByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlVueVueUsage = alVueVueUsageRepository.saveAndFlush(alVueVueUsage);

        UUID id = alVueVueUsage.getId();

        defaultAlVueVueUsageFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllAlVueVueUsagesByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alVueVueUsageRepository.saveAndFlush(alVueVueUsage);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alVueVueUsage.setApplication(application);
        alVueVueUsageRepository.saveAndFlush(alVueVueUsage);
        UUID applicationId = application.getId();
        // Get all the alVueVueUsageList where application equals to applicationId
        defaultAlVueVueUsageShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alVueVueUsageList where application equals to UUID.randomUUID()
        defaultAlVueVueUsageShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlVueVueUsageFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlVueVueUsageShouldBeFound(shouldBeFound);
        defaultAlVueVueUsageShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlVueVueUsageShouldBeFound(String filter) throws Exception {
        restAlVueVueUsageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alVueVueUsage.getId().toString())));

        // Check, that the count call also returns 1
        restAlVueVueUsageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlVueVueUsageShouldNotBeFound(String filter) throws Exception {
        restAlVueVueUsageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlVueVueUsageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlVueVueUsage() throws Exception {
        // Get the alVueVueUsage
        restAlVueVueUsageMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlVueVueUsage() throws Exception {
        // Initialize the database
        insertedAlVueVueUsage = alVueVueUsageRepository.saveAndFlush(alVueVueUsage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alVueVueUsage
        AlVueVueUsage updatedAlVueVueUsage = alVueVueUsageRepository.findById(alVueVueUsage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlVueVueUsage are not directly saved in db
        em.detach(updatedAlVueVueUsage);
        AlVueVueUsageDTO alVueVueUsageDTO = alVueVueUsageMapper.toDto(updatedAlVueVueUsage);

        restAlVueVueUsageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alVueVueUsageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alVueVueUsageDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlVueVueUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlVueVueUsageToMatchAllProperties(updatedAlVueVueUsage);
    }

    @Test
    @Transactional
    void putNonExistingAlVueVueUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alVueVueUsage.setId(UUID.randomUUID());

        // Create the AlVueVueUsage
        AlVueVueUsageDTO alVueVueUsageDTO = alVueVueUsageMapper.toDto(alVueVueUsage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlVueVueUsageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alVueVueUsageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alVueVueUsageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlVueVueUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlVueVueUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alVueVueUsage.setId(UUID.randomUUID());

        // Create the AlVueVueUsage
        AlVueVueUsageDTO alVueVueUsageDTO = alVueVueUsageMapper.toDto(alVueVueUsage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlVueVueUsageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alVueVueUsageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlVueVueUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlVueVueUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alVueVueUsage.setId(UUID.randomUUID());

        // Create the AlVueVueUsage
        AlVueVueUsageDTO alVueVueUsageDTO = alVueVueUsageMapper.toDto(alVueVueUsage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlVueVueUsageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alVueVueUsageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlVueVueUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlVueVueUsageWithPatch() throws Exception {
        // Initialize the database
        insertedAlVueVueUsage = alVueVueUsageRepository.saveAndFlush(alVueVueUsage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alVueVueUsage using partial update
        AlVueVueUsage partialUpdatedAlVueVueUsage = new AlVueVueUsage();
        partialUpdatedAlVueVueUsage.setId(alVueVueUsage.getId());

        restAlVueVueUsageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlVueVueUsage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlVueVueUsage))
            )
            .andExpect(status().isOk());

        // Validate the AlVueVueUsage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlVueVueUsageUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlVueVueUsage, alVueVueUsage),
            getPersistedAlVueVueUsage(alVueVueUsage)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlVueVueUsageWithPatch() throws Exception {
        // Initialize the database
        insertedAlVueVueUsage = alVueVueUsageRepository.saveAndFlush(alVueVueUsage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alVueVueUsage using partial update
        AlVueVueUsage partialUpdatedAlVueVueUsage = new AlVueVueUsage();
        partialUpdatedAlVueVueUsage.setId(alVueVueUsage.getId());

        restAlVueVueUsageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlVueVueUsage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlVueVueUsage))
            )
            .andExpect(status().isOk());

        // Validate the AlVueVueUsage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlVueVueUsageUpdatableFieldsEquals(partialUpdatedAlVueVueUsage, getPersistedAlVueVueUsage(partialUpdatedAlVueVueUsage));
    }

    @Test
    @Transactional
    void patchNonExistingAlVueVueUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alVueVueUsage.setId(UUID.randomUUID());

        // Create the AlVueVueUsage
        AlVueVueUsageDTO alVueVueUsageDTO = alVueVueUsageMapper.toDto(alVueVueUsage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlVueVueUsageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alVueVueUsageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alVueVueUsageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlVueVueUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlVueVueUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alVueVueUsage.setId(UUID.randomUUID());

        // Create the AlVueVueUsage
        AlVueVueUsageDTO alVueVueUsageDTO = alVueVueUsageMapper.toDto(alVueVueUsage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlVueVueUsageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alVueVueUsageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlVueVueUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlVueVueUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alVueVueUsage.setId(UUID.randomUUID());

        // Create the AlVueVueUsage
        AlVueVueUsageDTO alVueVueUsageDTO = alVueVueUsageMapper.toDto(alVueVueUsage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlVueVueUsageMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alVueVueUsageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlVueVueUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlVueVueUsage() throws Exception {
        // Initialize the database
        insertedAlVueVueUsage = alVueVueUsageRepository.saveAndFlush(alVueVueUsage);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alVueVueUsage
        restAlVueVueUsageMockMvc
            .perform(delete(ENTITY_API_URL_ID, alVueVueUsage.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alVueVueUsageRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected AlVueVueUsage getPersistedAlVueVueUsage(AlVueVueUsage alVueVueUsage) {
        return alVueVueUsageRepository.findById(alVueVueUsage.getId()).orElseThrow();
    }

    protected void assertPersistedAlVueVueUsageToMatchAllProperties(AlVueVueUsage expectedAlVueVueUsage) {
        assertAlVueVueUsageAllPropertiesEquals(expectedAlVueVueUsage, getPersistedAlVueVueUsage(expectedAlVueVueUsage));
    }

    protected void assertPersistedAlVueVueUsageToMatchUpdatableProperties(AlVueVueUsage expectedAlVueVueUsage) {
        assertAlVueVueUsageAllUpdatablePropertiesEquals(expectedAlVueVueUsage, getPersistedAlVueVueUsage(expectedAlVueVueUsage));
    }
}
