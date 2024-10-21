package ai.realworld.web.rest;

import static ai.realworld.domain.AlMemTierViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlMemTierVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.repository.AlMemTierViRepository;
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
 * Integration tests for the {@link AlMemTierViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlMemTierViResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_MIN_POINT = 1;
    private static final Integer UPDATED_MIN_POINT = 2;
    private static final Integer SMALLER_MIN_POINT = 1 - 1;

    private static final String ENTITY_API_URL = "/api/al-mem-tier-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlMemTierViRepository alMemTierViRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlMemTierViMockMvc;

    private AlMemTierVi alMemTierVi;

    private AlMemTierVi insertedAlMemTierVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlMemTierVi createEntity() {
        return new AlMemTierVi().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).minPoint(DEFAULT_MIN_POINT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlMemTierVi createUpdatedEntity() {
        return new AlMemTierVi().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).minPoint(UPDATED_MIN_POINT);
    }

    @BeforeEach
    public void initTest() {
        alMemTierVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlMemTierVi != null) {
            alMemTierViRepository.delete(insertedAlMemTierVi);
            insertedAlMemTierVi = null;
        }
    }

    @Test
    @Transactional
    void createAlMemTierVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlMemTierVi
        var returnedAlMemTierVi = om.readValue(
            restAlMemTierViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alMemTierVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlMemTierVi.class
        );

        // Validate the AlMemTierVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlMemTierViUpdatableFieldsEquals(returnedAlMemTierVi, getPersistedAlMemTierVi(returnedAlMemTierVi));

        insertedAlMemTierVi = returnedAlMemTierVi;
    }

    @Test
    @Transactional
    void createAlMemTierViWithExistingId() throws Exception {
        // Create the AlMemTierVi with an existing ID
        alMemTierVi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlMemTierViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alMemTierVi)))
            .andExpect(status().isBadRequest());

        // Validate the AlMemTierVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alMemTierVi.setName(null);

        // Create the AlMemTierVi, which fails.

        restAlMemTierViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alMemTierVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlMemTierVis() throws Exception {
        // Initialize the database
        insertedAlMemTierVi = alMemTierViRepository.saveAndFlush(alMemTierVi);

        // Get all the alMemTierViList
        restAlMemTierViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alMemTierVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].minPoint").value(hasItem(DEFAULT_MIN_POINT)));
    }

    @Test
    @Transactional
    void getAlMemTierVi() throws Exception {
        // Initialize the database
        insertedAlMemTierVi = alMemTierViRepository.saveAndFlush(alMemTierVi);

        // Get the alMemTierVi
        restAlMemTierViMockMvc
            .perform(get(ENTITY_API_URL_ID, alMemTierVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alMemTierVi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.minPoint").value(DEFAULT_MIN_POINT));
    }

    @Test
    @Transactional
    void getAlMemTierVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlMemTierVi = alMemTierViRepository.saveAndFlush(alMemTierVi);

        Long id = alMemTierVi.getId();

        defaultAlMemTierViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlMemTierViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlMemTierViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlMemTierVisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlMemTierVi = alMemTierViRepository.saveAndFlush(alMemTierVi);

        // Get all the alMemTierViList where name equals to
        defaultAlMemTierViFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlMemTierVisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlMemTierVi = alMemTierViRepository.saveAndFlush(alMemTierVi);

        // Get all the alMemTierViList where name in
        defaultAlMemTierViFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlMemTierVisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlMemTierVi = alMemTierViRepository.saveAndFlush(alMemTierVi);

        // Get all the alMemTierViList where name is not null
        defaultAlMemTierViFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAlMemTierVisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlMemTierVi = alMemTierViRepository.saveAndFlush(alMemTierVi);

        // Get all the alMemTierViList where name contains
        defaultAlMemTierViFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlMemTierVisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlMemTierVi = alMemTierViRepository.saveAndFlush(alMemTierVi);

        // Get all the alMemTierViList where name does not contain
        defaultAlMemTierViFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAlMemTierVisByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlMemTierVi = alMemTierViRepository.saveAndFlush(alMemTierVi);

        // Get all the alMemTierViList where description equals to
        defaultAlMemTierViFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAlMemTierVisByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlMemTierVi = alMemTierViRepository.saveAndFlush(alMemTierVi);

        // Get all the alMemTierViList where description in
        defaultAlMemTierViFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllAlMemTierVisByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlMemTierVi = alMemTierViRepository.saveAndFlush(alMemTierVi);

        // Get all the alMemTierViList where description is not null
        defaultAlMemTierViFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllAlMemTierVisByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedAlMemTierVi = alMemTierViRepository.saveAndFlush(alMemTierVi);

        // Get all the alMemTierViList where description contains
        defaultAlMemTierViFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAlMemTierVisByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlMemTierVi = alMemTierViRepository.saveAndFlush(alMemTierVi);

        // Get all the alMemTierViList where description does not contain
        defaultAlMemTierViFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllAlMemTierVisByMinPointIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlMemTierVi = alMemTierViRepository.saveAndFlush(alMemTierVi);

        // Get all the alMemTierViList where minPoint equals to
        defaultAlMemTierViFiltering("minPoint.equals=" + DEFAULT_MIN_POINT, "minPoint.equals=" + UPDATED_MIN_POINT);
    }

    @Test
    @Transactional
    void getAllAlMemTierVisByMinPointIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlMemTierVi = alMemTierViRepository.saveAndFlush(alMemTierVi);

        // Get all the alMemTierViList where minPoint in
        defaultAlMemTierViFiltering("minPoint.in=" + DEFAULT_MIN_POINT + "," + UPDATED_MIN_POINT, "minPoint.in=" + UPDATED_MIN_POINT);
    }

    @Test
    @Transactional
    void getAllAlMemTierVisByMinPointIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlMemTierVi = alMemTierViRepository.saveAndFlush(alMemTierVi);

        // Get all the alMemTierViList where minPoint is not null
        defaultAlMemTierViFiltering("minPoint.specified=true", "minPoint.specified=false");
    }

    @Test
    @Transactional
    void getAllAlMemTierVisByMinPointIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlMemTierVi = alMemTierViRepository.saveAndFlush(alMemTierVi);

        // Get all the alMemTierViList where minPoint is greater than or equal to
        defaultAlMemTierViFiltering("minPoint.greaterThanOrEqual=" + DEFAULT_MIN_POINT, "minPoint.greaterThanOrEqual=" + UPDATED_MIN_POINT);
    }

    @Test
    @Transactional
    void getAllAlMemTierVisByMinPointIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlMemTierVi = alMemTierViRepository.saveAndFlush(alMemTierVi);

        // Get all the alMemTierViList where minPoint is less than or equal to
        defaultAlMemTierViFiltering("minPoint.lessThanOrEqual=" + DEFAULT_MIN_POINT, "minPoint.lessThanOrEqual=" + SMALLER_MIN_POINT);
    }

    @Test
    @Transactional
    void getAllAlMemTierVisByMinPointIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlMemTierVi = alMemTierViRepository.saveAndFlush(alMemTierVi);

        // Get all the alMemTierViList where minPoint is less than
        defaultAlMemTierViFiltering("minPoint.lessThan=" + UPDATED_MIN_POINT, "minPoint.lessThan=" + DEFAULT_MIN_POINT);
    }

    @Test
    @Transactional
    void getAllAlMemTierVisByMinPointIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlMemTierVi = alMemTierViRepository.saveAndFlush(alMemTierVi);

        // Get all the alMemTierViList where minPoint is greater than
        defaultAlMemTierViFiltering("minPoint.greaterThan=" + SMALLER_MIN_POINT, "minPoint.greaterThan=" + DEFAULT_MIN_POINT);
    }

    @Test
    @Transactional
    void getAllAlMemTierVisByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alMemTierViRepository.saveAndFlush(alMemTierVi);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alMemTierVi.setApplication(application);
        alMemTierViRepository.saveAndFlush(alMemTierVi);
        UUID applicationId = application.getId();
        // Get all the alMemTierViList where application equals to applicationId
        defaultAlMemTierViShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alMemTierViList where application equals to UUID.randomUUID()
        defaultAlMemTierViShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlMemTierViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlMemTierViShouldBeFound(shouldBeFound);
        defaultAlMemTierViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlMemTierViShouldBeFound(String filter) throws Exception {
        restAlMemTierViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alMemTierVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].minPoint").value(hasItem(DEFAULT_MIN_POINT)));

        // Check, that the count call also returns 1
        restAlMemTierViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlMemTierViShouldNotBeFound(String filter) throws Exception {
        restAlMemTierViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlMemTierViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlMemTierVi() throws Exception {
        // Get the alMemTierVi
        restAlMemTierViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlMemTierVi() throws Exception {
        // Initialize the database
        insertedAlMemTierVi = alMemTierViRepository.saveAndFlush(alMemTierVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alMemTierVi
        AlMemTierVi updatedAlMemTierVi = alMemTierViRepository.findById(alMemTierVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlMemTierVi are not directly saved in db
        em.detach(updatedAlMemTierVi);
        updatedAlMemTierVi.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).minPoint(UPDATED_MIN_POINT);

        restAlMemTierViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlMemTierVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlMemTierVi))
            )
            .andExpect(status().isOk());

        // Validate the AlMemTierVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlMemTierViToMatchAllProperties(updatedAlMemTierVi);
    }

    @Test
    @Transactional
    void putNonExistingAlMemTierVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alMemTierVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlMemTierViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alMemTierVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alMemTierVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlMemTierVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlMemTierVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alMemTierVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlMemTierViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alMemTierVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlMemTierVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlMemTierVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alMemTierVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlMemTierViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alMemTierVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlMemTierVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlMemTierViWithPatch() throws Exception {
        // Initialize the database
        insertedAlMemTierVi = alMemTierViRepository.saveAndFlush(alMemTierVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alMemTierVi using partial update
        AlMemTierVi partialUpdatedAlMemTierVi = new AlMemTierVi();
        partialUpdatedAlMemTierVi.setId(alMemTierVi.getId());

        restAlMemTierViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlMemTierVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlMemTierVi))
            )
            .andExpect(status().isOk());

        // Validate the AlMemTierVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlMemTierViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlMemTierVi, alMemTierVi),
            getPersistedAlMemTierVi(alMemTierVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlMemTierViWithPatch() throws Exception {
        // Initialize the database
        insertedAlMemTierVi = alMemTierViRepository.saveAndFlush(alMemTierVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alMemTierVi using partial update
        AlMemTierVi partialUpdatedAlMemTierVi = new AlMemTierVi();
        partialUpdatedAlMemTierVi.setId(alMemTierVi.getId());

        partialUpdatedAlMemTierVi.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).minPoint(UPDATED_MIN_POINT);

        restAlMemTierViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlMemTierVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlMemTierVi))
            )
            .andExpect(status().isOk());

        // Validate the AlMemTierVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlMemTierViUpdatableFieldsEquals(partialUpdatedAlMemTierVi, getPersistedAlMemTierVi(partialUpdatedAlMemTierVi));
    }

    @Test
    @Transactional
    void patchNonExistingAlMemTierVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alMemTierVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlMemTierViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alMemTierVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alMemTierVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlMemTierVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlMemTierVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alMemTierVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlMemTierViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alMemTierVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlMemTierVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlMemTierVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alMemTierVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlMemTierViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alMemTierVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlMemTierVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlMemTierVi() throws Exception {
        // Initialize the database
        insertedAlMemTierVi = alMemTierViRepository.saveAndFlush(alMemTierVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alMemTierVi
        restAlMemTierViMockMvc
            .perform(delete(ENTITY_API_URL_ID, alMemTierVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alMemTierViRepository.count();
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

    protected AlMemTierVi getPersistedAlMemTierVi(AlMemTierVi alMemTierVi) {
        return alMemTierViRepository.findById(alMemTierVi.getId()).orElseThrow();
    }

    protected void assertPersistedAlMemTierViToMatchAllProperties(AlMemTierVi expectedAlMemTierVi) {
        assertAlMemTierViAllPropertiesEquals(expectedAlMemTierVi, getPersistedAlMemTierVi(expectedAlMemTierVi));
    }

    protected void assertPersistedAlMemTierViToMatchUpdatableProperties(AlMemTierVi expectedAlMemTierVi) {
        assertAlMemTierViAllUpdatablePropertiesEquals(expectedAlMemTierVi, getPersistedAlMemTierVi(expectedAlMemTierVi));
    }
}
