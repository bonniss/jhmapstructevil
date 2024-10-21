package ai.realworld.web.rest;

import static ai.realworld.domain.AlMemTierAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlMemTier;
import ai.realworld.domain.JohnLennon;
import ai.realworld.repository.AlMemTierRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
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
 * Integration tests for the {@link AlMemTierResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlMemTierResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_MIN_POINT = 1;
    private static final Integer UPDATED_MIN_POINT = 2;
    private static final Integer SMALLER_MIN_POINT = 1 - 1;

    private static final String ENTITY_API_URL = "/api/al-mem-tiers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlMemTierRepository alMemTierRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlMemTierMockMvc;

    private AlMemTier alMemTier;

    private AlMemTier insertedAlMemTier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlMemTier createEntity() {
        return new AlMemTier().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).minPoint(DEFAULT_MIN_POINT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlMemTier createUpdatedEntity() {
        return new AlMemTier().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).minPoint(UPDATED_MIN_POINT);
    }

    @BeforeEach
    public void initTest() {
        alMemTier = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlMemTier != null) {
            alMemTierRepository.delete(insertedAlMemTier);
            insertedAlMemTier = null;
        }
    }

    @Test
    @Transactional
    void createAlMemTier() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlMemTier
        var returnedAlMemTier = om.readValue(
            restAlMemTierMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alMemTier)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlMemTier.class
        );

        // Validate the AlMemTier in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlMemTierUpdatableFieldsEquals(returnedAlMemTier, getPersistedAlMemTier(returnedAlMemTier));

        insertedAlMemTier = returnedAlMemTier;
    }

    @Test
    @Transactional
    void createAlMemTierWithExistingId() throws Exception {
        // Create the AlMemTier with an existing ID
        alMemTier.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlMemTierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alMemTier)))
            .andExpect(status().isBadRequest());

        // Validate the AlMemTier in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alMemTier.setName(null);

        // Create the AlMemTier, which fails.

        restAlMemTierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alMemTier)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlMemTiers() throws Exception {
        // Initialize the database
        insertedAlMemTier = alMemTierRepository.saveAndFlush(alMemTier);

        // Get all the alMemTierList
        restAlMemTierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alMemTier.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].minPoint").value(hasItem(DEFAULT_MIN_POINT)));
    }

    @Test
    @Transactional
    void getAlMemTier() throws Exception {
        // Initialize the database
        insertedAlMemTier = alMemTierRepository.saveAndFlush(alMemTier);

        // Get the alMemTier
        restAlMemTierMockMvc
            .perform(get(ENTITY_API_URL_ID, alMemTier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alMemTier.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.minPoint").value(DEFAULT_MIN_POINT));
    }

    @Test
    @Transactional
    void getAlMemTiersByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlMemTier = alMemTierRepository.saveAndFlush(alMemTier);

        Long id = alMemTier.getId();

        defaultAlMemTierFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlMemTierFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlMemTierFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlMemTiersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlMemTier = alMemTierRepository.saveAndFlush(alMemTier);

        // Get all the alMemTierList where name equals to
        defaultAlMemTierFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlMemTiersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlMemTier = alMemTierRepository.saveAndFlush(alMemTier);

        // Get all the alMemTierList where name in
        defaultAlMemTierFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlMemTiersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlMemTier = alMemTierRepository.saveAndFlush(alMemTier);

        // Get all the alMemTierList where name is not null
        defaultAlMemTierFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAlMemTiersByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlMemTier = alMemTierRepository.saveAndFlush(alMemTier);

        // Get all the alMemTierList where name contains
        defaultAlMemTierFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlMemTiersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlMemTier = alMemTierRepository.saveAndFlush(alMemTier);

        // Get all the alMemTierList where name does not contain
        defaultAlMemTierFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAlMemTiersByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlMemTier = alMemTierRepository.saveAndFlush(alMemTier);

        // Get all the alMemTierList where description equals to
        defaultAlMemTierFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAlMemTiersByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlMemTier = alMemTierRepository.saveAndFlush(alMemTier);

        // Get all the alMemTierList where description in
        defaultAlMemTierFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllAlMemTiersByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlMemTier = alMemTierRepository.saveAndFlush(alMemTier);

        // Get all the alMemTierList where description is not null
        defaultAlMemTierFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllAlMemTiersByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedAlMemTier = alMemTierRepository.saveAndFlush(alMemTier);

        // Get all the alMemTierList where description contains
        defaultAlMemTierFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAlMemTiersByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlMemTier = alMemTierRepository.saveAndFlush(alMemTier);

        // Get all the alMemTierList where description does not contain
        defaultAlMemTierFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAlMemTiersByMinPointIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlMemTier = alMemTierRepository.saveAndFlush(alMemTier);

        // Get all the alMemTierList where minPoint equals to
        defaultAlMemTierFiltering("minPoint.equals=" + DEFAULT_MIN_POINT, "minPoint.equals=" + UPDATED_MIN_POINT);
    }

    @Test
    @Transactional
    void getAllAlMemTiersByMinPointIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlMemTier = alMemTierRepository.saveAndFlush(alMemTier);

        // Get all the alMemTierList where minPoint in
        defaultAlMemTierFiltering("minPoint.in=" + DEFAULT_MIN_POINT + "," + UPDATED_MIN_POINT, "minPoint.in=" + UPDATED_MIN_POINT);
    }

    @Test
    @Transactional
    void getAllAlMemTiersByMinPointIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlMemTier = alMemTierRepository.saveAndFlush(alMemTier);

        // Get all the alMemTierList where minPoint is not null
        defaultAlMemTierFiltering("minPoint.specified=true", "minPoint.specified=false");
    }

    @Test
    @Transactional
    void getAllAlMemTiersByMinPointIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlMemTier = alMemTierRepository.saveAndFlush(alMemTier);

        // Get all the alMemTierList where minPoint is greater than or equal to
        defaultAlMemTierFiltering("minPoint.greaterThanOrEqual=" + DEFAULT_MIN_POINT, "minPoint.greaterThanOrEqual=" + UPDATED_MIN_POINT);
    }

    @Test
    @Transactional
    void getAllAlMemTiersByMinPointIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlMemTier = alMemTierRepository.saveAndFlush(alMemTier);

        // Get all the alMemTierList where minPoint is less than or equal to
        defaultAlMemTierFiltering("minPoint.lessThanOrEqual=" + DEFAULT_MIN_POINT, "minPoint.lessThanOrEqual=" + SMALLER_MIN_POINT);
    }

    @Test
    @Transactional
    void getAllAlMemTiersByMinPointIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlMemTier = alMemTierRepository.saveAndFlush(alMemTier);

        // Get all the alMemTierList where minPoint is less than
        defaultAlMemTierFiltering("minPoint.lessThan=" + UPDATED_MIN_POINT, "minPoint.lessThan=" + DEFAULT_MIN_POINT);
    }

    @Test
    @Transactional
    void getAllAlMemTiersByMinPointIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlMemTier = alMemTierRepository.saveAndFlush(alMemTier);

        // Get all the alMemTierList where minPoint is greater than
        defaultAlMemTierFiltering("minPoint.greaterThan=" + SMALLER_MIN_POINT, "minPoint.greaterThan=" + DEFAULT_MIN_POINT);
    }

    @Test
    @Transactional
    void getAllAlMemTiersByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alMemTierRepository.saveAndFlush(alMemTier);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alMemTier.setApplication(application);
        alMemTierRepository.saveAndFlush(alMemTier);
        UUID applicationId = application.getId();
        // Get all the alMemTierList where application equals to applicationId
        defaultAlMemTierShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alMemTierList where application equals to UUID.randomUUID()
        defaultAlMemTierShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlMemTierFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlMemTierShouldBeFound(shouldBeFound);
        defaultAlMemTierShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlMemTierShouldBeFound(String filter) throws Exception {
        restAlMemTierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alMemTier.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].minPoint").value(hasItem(DEFAULT_MIN_POINT)));

        // Check, that the count call also returns 1
        restAlMemTierMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlMemTierShouldNotBeFound(String filter) throws Exception {
        restAlMemTierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlMemTierMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlMemTier() throws Exception {
        // Get the alMemTier
        restAlMemTierMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlMemTier() throws Exception {
        // Initialize the database
        insertedAlMemTier = alMemTierRepository.saveAndFlush(alMemTier);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alMemTier
        AlMemTier updatedAlMemTier = alMemTierRepository.findById(alMemTier.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlMemTier are not directly saved in db
        em.detach(updatedAlMemTier);
        updatedAlMemTier.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).minPoint(UPDATED_MIN_POINT);

        restAlMemTierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlMemTier.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlMemTier))
            )
            .andExpect(status().isOk());

        // Validate the AlMemTier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlMemTierToMatchAllProperties(updatedAlMemTier);
    }

    @Test
    @Transactional
    void putNonExistingAlMemTier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alMemTier.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlMemTierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alMemTier.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alMemTier))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlMemTier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlMemTier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alMemTier.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlMemTierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alMemTier))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlMemTier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlMemTier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alMemTier.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlMemTierMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alMemTier)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlMemTier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlMemTierWithPatch() throws Exception {
        // Initialize the database
        insertedAlMemTier = alMemTierRepository.saveAndFlush(alMemTier);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alMemTier using partial update
        AlMemTier partialUpdatedAlMemTier = new AlMemTier();
        partialUpdatedAlMemTier.setId(alMemTier.getId());

        partialUpdatedAlMemTier.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restAlMemTierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlMemTier.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlMemTier))
            )
            .andExpect(status().isOk());

        // Validate the AlMemTier in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlMemTierUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlMemTier, alMemTier),
            getPersistedAlMemTier(alMemTier)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlMemTierWithPatch() throws Exception {
        // Initialize the database
        insertedAlMemTier = alMemTierRepository.saveAndFlush(alMemTier);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alMemTier using partial update
        AlMemTier partialUpdatedAlMemTier = new AlMemTier();
        partialUpdatedAlMemTier.setId(alMemTier.getId());

        partialUpdatedAlMemTier.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).minPoint(UPDATED_MIN_POINT);

        restAlMemTierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlMemTier.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlMemTier))
            )
            .andExpect(status().isOk());

        // Validate the AlMemTier in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlMemTierUpdatableFieldsEquals(partialUpdatedAlMemTier, getPersistedAlMemTier(partialUpdatedAlMemTier));
    }

    @Test
    @Transactional
    void patchNonExistingAlMemTier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alMemTier.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlMemTierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alMemTier.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alMemTier))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlMemTier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlMemTier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alMemTier.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlMemTierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alMemTier))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlMemTier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlMemTier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alMemTier.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlMemTierMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alMemTier)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlMemTier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlMemTier() throws Exception {
        // Initialize the database
        insertedAlMemTier = alMemTierRepository.saveAndFlush(alMemTier);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alMemTier
        restAlMemTierMockMvc
            .perform(delete(ENTITY_API_URL_ID, alMemTier.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alMemTierRepository.count();
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

    protected AlMemTier getPersistedAlMemTier(AlMemTier alMemTier) {
        return alMemTierRepository.findById(alMemTier.getId()).orElseThrow();
    }

    protected void assertPersistedAlMemTierToMatchAllProperties(AlMemTier expectedAlMemTier) {
        assertAlMemTierAllPropertiesEquals(expectedAlMemTier, getPersistedAlMemTier(expectedAlMemTier));
    }

    protected void assertPersistedAlMemTierToMatchUpdatableProperties(AlMemTier expectedAlMemTier) {
        assertAlMemTierAllUpdatablePropertiesEquals(expectedAlMemTier, getPersistedAlMemTier(expectedAlMemTier));
    }
}
