package ai.realworld.web.rest;

import static ai.realworld.domain.AlVueVueViUsageAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlVueVueViUsage;
import ai.realworld.domain.JohnLennon;
import ai.realworld.repository.AlVueVueViUsageRepository;
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
 * Integration tests for the {@link AlVueVueViUsageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlVueVueViUsageResourceIT {

    private static final String ENTITY_API_URL = "/api/al-vue-vue-vi-usages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlVueVueViUsageRepository alVueVueViUsageRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlVueVueViUsageMockMvc;

    private AlVueVueViUsage alVueVueViUsage;

    private AlVueVueViUsage insertedAlVueVueViUsage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlVueVueViUsage createEntity() {
        return new AlVueVueViUsage();
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlVueVueViUsage createUpdatedEntity() {
        return new AlVueVueViUsage();
    }

    @BeforeEach
    public void initTest() {
        alVueVueViUsage = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlVueVueViUsage != null) {
            alVueVueViUsageRepository.delete(insertedAlVueVueViUsage);
            insertedAlVueVueViUsage = null;
        }
    }

    @Test
    @Transactional
    void createAlVueVueViUsage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlVueVueViUsage
        var returnedAlVueVueViUsage = om.readValue(
            restAlVueVueViUsageMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alVueVueViUsage)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlVueVueViUsage.class
        );

        // Validate the AlVueVueViUsage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlVueVueViUsageUpdatableFieldsEquals(returnedAlVueVueViUsage, getPersistedAlVueVueViUsage(returnedAlVueVueViUsage));

        insertedAlVueVueViUsage = returnedAlVueVueViUsage;
    }

    @Test
    @Transactional
    void createAlVueVueViUsageWithExistingId() throws Exception {
        // Create the AlVueVueViUsage with an existing ID
        insertedAlVueVueViUsage = alVueVueViUsageRepository.saveAndFlush(alVueVueViUsage);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlVueVueViUsageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alVueVueViUsage)))
            .andExpect(status().isBadRequest());

        // Validate the AlVueVueViUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAlVueVueViUsages() throws Exception {
        // Initialize the database
        insertedAlVueVueViUsage = alVueVueViUsageRepository.saveAndFlush(alVueVueViUsage);

        // Get all the alVueVueViUsageList
        restAlVueVueViUsageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alVueVueViUsage.getId().toString())));
    }

    @Test
    @Transactional
    void getAlVueVueViUsage() throws Exception {
        // Initialize the database
        insertedAlVueVueViUsage = alVueVueViUsageRepository.saveAndFlush(alVueVueViUsage);

        // Get the alVueVueViUsage
        restAlVueVueViUsageMockMvc
            .perform(get(ENTITY_API_URL_ID, alVueVueViUsage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alVueVueViUsage.getId().toString()));
    }

    @Test
    @Transactional
    void getAlVueVueViUsagesByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlVueVueViUsage = alVueVueViUsageRepository.saveAndFlush(alVueVueViUsage);

        UUID id = alVueVueViUsage.getId();

        defaultAlVueVueViUsageFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllAlVueVueViUsagesByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alVueVueViUsageRepository.saveAndFlush(alVueVueViUsage);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alVueVueViUsage.setApplication(application);
        alVueVueViUsageRepository.saveAndFlush(alVueVueViUsage);
        UUID applicationId = application.getId();
        // Get all the alVueVueViUsageList where application equals to applicationId
        defaultAlVueVueViUsageShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alVueVueViUsageList where application equals to UUID.randomUUID()
        defaultAlVueVueViUsageShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlVueVueViUsageFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlVueVueViUsageShouldBeFound(shouldBeFound);
        defaultAlVueVueViUsageShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlVueVueViUsageShouldBeFound(String filter) throws Exception {
        restAlVueVueViUsageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alVueVueViUsage.getId().toString())));

        // Check, that the count call also returns 1
        restAlVueVueViUsageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlVueVueViUsageShouldNotBeFound(String filter) throws Exception {
        restAlVueVueViUsageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlVueVueViUsageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlVueVueViUsage() throws Exception {
        // Get the alVueVueViUsage
        restAlVueVueViUsageMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlVueVueViUsage() throws Exception {
        // Initialize the database
        insertedAlVueVueViUsage = alVueVueViUsageRepository.saveAndFlush(alVueVueViUsage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alVueVueViUsage
        AlVueVueViUsage updatedAlVueVueViUsage = alVueVueViUsageRepository.findById(alVueVueViUsage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlVueVueViUsage are not directly saved in db
        em.detach(updatedAlVueVueViUsage);

        restAlVueVueViUsageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlVueVueViUsage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlVueVueViUsage))
            )
            .andExpect(status().isOk());

        // Validate the AlVueVueViUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlVueVueViUsageToMatchAllProperties(updatedAlVueVueViUsage);
    }

    @Test
    @Transactional
    void putNonExistingAlVueVueViUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alVueVueViUsage.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlVueVueViUsageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alVueVueViUsage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alVueVueViUsage))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlVueVueViUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlVueVueViUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alVueVueViUsage.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlVueVueViUsageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alVueVueViUsage))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlVueVueViUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlVueVueViUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alVueVueViUsage.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlVueVueViUsageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alVueVueViUsage)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlVueVueViUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlVueVueViUsageWithPatch() throws Exception {
        // Initialize the database
        insertedAlVueVueViUsage = alVueVueViUsageRepository.saveAndFlush(alVueVueViUsage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alVueVueViUsage using partial update
        AlVueVueViUsage partialUpdatedAlVueVueViUsage = new AlVueVueViUsage();
        partialUpdatedAlVueVueViUsage.setId(alVueVueViUsage.getId());

        restAlVueVueViUsageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlVueVueViUsage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlVueVueViUsage))
            )
            .andExpect(status().isOk());

        // Validate the AlVueVueViUsage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlVueVueViUsageUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlVueVueViUsage, alVueVueViUsage),
            getPersistedAlVueVueViUsage(alVueVueViUsage)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlVueVueViUsageWithPatch() throws Exception {
        // Initialize the database
        insertedAlVueVueViUsage = alVueVueViUsageRepository.saveAndFlush(alVueVueViUsage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alVueVueViUsage using partial update
        AlVueVueViUsage partialUpdatedAlVueVueViUsage = new AlVueVueViUsage();
        partialUpdatedAlVueVueViUsage.setId(alVueVueViUsage.getId());

        restAlVueVueViUsageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlVueVueViUsage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlVueVueViUsage))
            )
            .andExpect(status().isOk());

        // Validate the AlVueVueViUsage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlVueVueViUsageUpdatableFieldsEquals(
            partialUpdatedAlVueVueViUsage,
            getPersistedAlVueVueViUsage(partialUpdatedAlVueVueViUsage)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAlVueVueViUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alVueVueViUsage.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlVueVueViUsageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alVueVueViUsage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alVueVueViUsage))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlVueVueViUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlVueVueViUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alVueVueViUsage.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlVueVueViUsageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alVueVueViUsage))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlVueVueViUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlVueVueViUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alVueVueViUsage.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlVueVueViUsageMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alVueVueViUsage)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlVueVueViUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlVueVueViUsage() throws Exception {
        // Initialize the database
        insertedAlVueVueViUsage = alVueVueViUsageRepository.saveAndFlush(alVueVueViUsage);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alVueVueViUsage
        restAlVueVueViUsageMockMvc
            .perform(delete(ENTITY_API_URL_ID, alVueVueViUsage.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alVueVueViUsageRepository.count();
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

    protected AlVueVueViUsage getPersistedAlVueVueViUsage(AlVueVueViUsage alVueVueViUsage) {
        return alVueVueViUsageRepository.findById(alVueVueViUsage.getId()).orElseThrow();
    }

    protected void assertPersistedAlVueVueViUsageToMatchAllProperties(AlVueVueViUsage expectedAlVueVueViUsage) {
        assertAlVueVueViUsageAllPropertiesEquals(expectedAlVueVueViUsage, getPersistedAlVueVueViUsage(expectedAlVueVueViUsage));
    }

    protected void assertPersistedAlVueVueViUsageToMatchUpdatableProperties(AlVueVueViUsage expectedAlVueVueViUsage) {
        assertAlVueVueViUsageAllUpdatablePropertiesEquals(expectedAlVueVueViUsage, getPersistedAlVueVueViUsage(expectedAlVueVueViUsage));
    }
}
