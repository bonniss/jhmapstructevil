package ai.realworld.web.rest;

import static ai.realworld.domain.AlPounderAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlPedroTax;
import ai.realworld.domain.AlPounder;
import ai.realworld.domain.JohnLennon;
import ai.realworld.repository.AlPounderRepository;
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
 * Integration tests for the {@link AlPounderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlPounderResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_WEIGHT = 1;
    private static final Integer UPDATED_WEIGHT = 2;
    private static final Integer SMALLER_WEIGHT = 1 - 1;

    private static final String ENTITY_API_URL = "/api/al-pounders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlPounderRepository alPounderRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlPounderMockMvc;

    private AlPounder alPounder;

    private AlPounder insertedAlPounder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPounder createEntity(EntityManager em) {
        AlPounder alPounder = new AlPounder().name(DEFAULT_NAME).weight(DEFAULT_WEIGHT);
        // Add required entity
        AlPedroTax alPedroTax;
        if (TestUtil.findAll(em, AlPedroTax.class).isEmpty()) {
            alPedroTax = AlPedroTaxResourceIT.createEntity();
            em.persist(alPedroTax);
            em.flush();
        } else {
            alPedroTax = TestUtil.findAll(em, AlPedroTax.class).get(0);
        }
        alPounder.setAttributeTaxonomy(alPedroTax);
        return alPounder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPounder createUpdatedEntity(EntityManager em) {
        AlPounder updatedAlPounder = new AlPounder().name(UPDATED_NAME).weight(UPDATED_WEIGHT);
        // Add required entity
        AlPedroTax alPedroTax;
        if (TestUtil.findAll(em, AlPedroTax.class).isEmpty()) {
            alPedroTax = AlPedroTaxResourceIT.createUpdatedEntity();
            em.persist(alPedroTax);
            em.flush();
        } else {
            alPedroTax = TestUtil.findAll(em, AlPedroTax.class).get(0);
        }
        updatedAlPounder.setAttributeTaxonomy(alPedroTax);
        return updatedAlPounder;
    }

    @BeforeEach
    public void initTest() {
        alPounder = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlPounder != null) {
            alPounderRepository.delete(insertedAlPounder);
            insertedAlPounder = null;
        }
    }

    @Test
    @Transactional
    void createAlPounder() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlPounder
        var returnedAlPounder = om.readValue(
            restAlPounderMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPounder)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlPounder.class
        );

        // Validate the AlPounder in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlPounderUpdatableFieldsEquals(returnedAlPounder, getPersistedAlPounder(returnedAlPounder));

        insertedAlPounder = returnedAlPounder;
    }

    @Test
    @Transactional
    void createAlPounderWithExistingId() throws Exception {
        // Create the AlPounder with an existing ID
        alPounder.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlPounderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPounder)))
            .andExpect(status().isBadRequest());

        // Validate the AlPounder in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alPounder.setName(null);

        // Create the AlPounder, which fails.

        restAlPounderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPounder)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlPounders() throws Exception {
        // Initialize the database
        insertedAlPounder = alPounderRepository.saveAndFlush(alPounder);

        // Get all the alPounderList
        restAlPounderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPounder.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)));
    }

    @Test
    @Transactional
    void getAlPounder() throws Exception {
        // Initialize the database
        insertedAlPounder = alPounderRepository.saveAndFlush(alPounder);

        // Get the alPounder
        restAlPounderMockMvc
            .perform(get(ENTITY_API_URL_ID, alPounder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alPounder.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT));
    }

    @Test
    @Transactional
    void getAlPoundersByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlPounder = alPounderRepository.saveAndFlush(alPounder);

        Long id = alPounder.getId();

        defaultAlPounderFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlPounderFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlPounderFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlPoundersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPounder = alPounderRepository.saveAndFlush(alPounder);

        // Get all the alPounderList where name equals to
        defaultAlPounderFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlPoundersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPounder = alPounderRepository.saveAndFlush(alPounder);

        // Get all the alPounderList where name in
        defaultAlPounderFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlPoundersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPounder = alPounderRepository.saveAndFlush(alPounder);

        // Get all the alPounderList where name is not null
        defaultAlPounderFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPoundersByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPounder = alPounderRepository.saveAndFlush(alPounder);

        // Get all the alPounderList where name contains
        defaultAlPounderFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlPoundersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPounder = alPounderRepository.saveAndFlush(alPounder);

        // Get all the alPounderList where name does not contain
        defaultAlPounderFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAlPoundersByWeightIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPounder = alPounderRepository.saveAndFlush(alPounder);

        // Get all the alPounderList where weight equals to
        defaultAlPounderFiltering("weight.equals=" + DEFAULT_WEIGHT, "weight.equals=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlPoundersByWeightIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPounder = alPounderRepository.saveAndFlush(alPounder);

        // Get all the alPounderList where weight in
        defaultAlPounderFiltering("weight.in=" + DEFAULT_WEIGHT + "," + UPDATED_WEIGHT, "weight.in=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlPoundersByWeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPounder = alPounderRepository.saveAndFlush(alPounder);

        // Get all the alPounderList where weight is not null
        defaultAlPounderFiltering("weight.specified=true", "weight.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPoundersByWeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPounder = alPounderRepository.saveAndFlush(alPounder);

        // Get all the alPounderList where weight is greater than or equal to
        defaultAlPounderFiltering("weight.greaterThanOrEqual=" + DEFAULT_WEIGHT, "weight.greaterThanOrEqual=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlPoundersByWeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPounder = alPounderRepository.saveAndFlush(alPounder);

        // Get all the alPounderList where weight is less than or equal to
        defaultAlPounderFiltering("weight.lessThanOrEqual=" + DEFAULT_WEIGHT, "weight.lessThanOrEqual=" + SMALLER_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlPoundersByWeightIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlPounder = alPounderRepository.saveAndFlush(alPounder);

        // Get all the alPounderList where weight is less than
        defaultAlPounderFiltering("weight.lessThan=" + UPDATED_WEIGHT, "weight.lessThan=" + DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlPoundersByWeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlPounder = alPounderRepository.saveAndFlush(alPounder);

        // Get all the alPounderList where weight is greater than
        defaultAlPounderFiltering("weight.greaterThan=" + SMALLER_WEIGHT, "weight.greaterThan=" + DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlPoundersByAttributeTaxonomyIsEqualToSomething() throws Exception {
        AlPedroTax attributeTaxonomy;
        if (TestUtil.findAll(em, AlPedroTax.class).isEmpty()) {
            alPounderRepository.saveAndFlush(alPounder);
            attributeTaxonomy = AlPedroTaxResourceIT.createEntity();
        } else {
            attributeTaxonomy = TestUtil.findAll(em, AlPedroTax.class).get(0);
        }
        em.persist(attributeTaxonomy);
        em.flush();
        alPounder.setAttributeTaxonomy(attributeTaxonomy);
        alPounderRepository.saveAndFlush(alPounder);
        Long attributeTaxonomyId = attributeTaxonomy.getId();
        // Get all the alPounderList where attributeTaxonomy equals to attributeTaxonomyId
        defaultAlPounderShouldBeFound("attributeTaxonomyId.equals=" + attributeTaxonomyId);

        // Get all the alPounderList where attributeTaxonomy equals to (attributeTaxonomyId + 1)
        defaultAlPounderShouldNotBeFound("attributeTaxonomyId.equals=" + (attributeTaxonomyId + 1));
    }

    @Test
    @Transactional
    void getAllAlPoundersByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alPounderRepository.saveAndFlush(alPounder);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alPounder.setApplication(application);
        alPounderRepository.saveAndFlush(alPounder);
        UUID applicationId = application.getId();
        // Get all the alPounderList where application equals to applicationId
        defaultAlPounderShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alPounderList where application equals to UUID.randomUUID()
        defaultAlPounderShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlPounderFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlPounderShouldBeFound(shouldBeFound);
        defaultAlPounderShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlPounderShouldBeFound(String filter) throws Exception {
        restAlPounderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPounder.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)));

        // Check, that the count call also returns 1
        restAlPounderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlPounderShouldNotBeFound(String filter) throws Exception {
        restAlPounderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlPounderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlPounder() throws Exception {
        // Get the alPounder
        restAlPounderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlPounder() throws Exception {
        // Initialize the database
        insertedAlPounder = alPounderRepository.saveAndFlush(alPounder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPounder
        AlPounder updatedAlPounder = alPounderRepository.findById(alPounder.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlPounder are not directly saved in db
        em.detach(updatedAlPounder);
        updatedAlPounder.name(UPDATED_NAME).weight(UPDATED_WEIGHT);

        restAlPounderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlPounder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlPounder))
            )
            .andExpect(status().isOk());

        // Validate the AlPounder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlPounderToMatchAllProperties(updatedAlPounder);
    }

    @Test
    @Transactional
    void putNonExistingAlPounder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPounder.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPounderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alPounder.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPounder))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPounder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlPounder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPounder.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPounderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPounder))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPounder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlPounder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPounder.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPounderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPounder)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPounder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlPounderWithPatch() throws Exception {
        // Initialize the database
        insertedAlPounder = alPounderRepository.saveAndFlush(alPounder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPounder using partial update
        AlPounder partialUpdatedAlPounder = new AlPounder();
        partialUpdatedAlPounder.setId(alPounder.getId());

        restAlPounderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPounder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPounder))
            )
            .andExpect(status().isOk());

        // Validate the AlPounder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPounderUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlPounder, alPounder),
            getPersistedAlPounder(alPounder)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlPounderWithPatch() throws Exception {
        // Initialize the database
        insertedAlPounder = alPounderRepository.saveAndFlush(alPounder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPounder using partial update
        AlPounder partialUpdatedAlPounder = new AlPounder();
        partialUpdatedAlPounder.setId(alPounder.getId());

        partialUpdatedAlPounder.name(UPDATED_NAME).weight(UPDATED_WEIGHT);

        restAlPounderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPounder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPounder))
            )
            .andExpect(status().isOk());

        // Validate the AlPounder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPounderUpdatableFieldsEquals(partialUpdatedAlPounder, getPersistedAlPounder(partialUpdatedAlPounder));
    }

    @Test
    @Transactional
    void patchNonExistingAlPounder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPounder.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPounderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alPounder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPounder))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPounder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlPounder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPounder.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPounderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPounder))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPounder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlPounder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPounder.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPounderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alPounder)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPounder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlPounder() throws Exception {
        // Initialize the database
        insertedAlPounder = alPounderRepository.saveAndFlush(alPounder);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alPounder
        restAlPounderMockMvc
            .perform(delete(ENTITY_API_URL_ID, alPounder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alPounderRepository.count();
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

    protected AlPounder getPersistedAlPounder(AlPounder alPounder) {
        return alPounderRepository.findById(alPounder.getId()).orElseThrow();
    }

    protected void assertPersistedAlPounderToMatchAllProperties(AlPounder expectedAlPounder) {
        assertAlPounderAllPropertiesEquals(expectedAlPounder, getPersistedAlPounder(expectedAlPounder));
    }

    protected void assertPersistedAlPounderToMatchUpdatableProperties(AlPounder expectedAlPounder) {
        assertAlPounderAllUpdatablePropertiesEquals(expectedAlPounder, getPersistedAlPounder(expectedAlPounder));
    }
}
