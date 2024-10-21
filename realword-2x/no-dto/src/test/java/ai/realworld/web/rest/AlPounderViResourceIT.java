package ai.realworld.web.rest;

import static ai.realworld.domain.AlPounderViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlPedroTaxVi;
import ai.realworld.domain.AlPounderVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.repository.AlPounderViRepository;
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
 * Integration tests for the {@link AlPounderViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlPounderViResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_WEIGHT = 1;
    private static final Integer UPDATED_WEIGHT = 2;
    private static final Integer SMALLER_WEIGHT = 1 - 1;

    private static final String ENTITY_API_URL = "/api/al-pounder-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlPounderViRepository alPounderViRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlPounderViMockMvc;

    private AlPounderVi alPounderVi;

    private AlPounderVi insertedAlPounderVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPounderVi createEntity(EntityManager em) {
        AlPounderVi alPounderVi = new AlPounderVi().name(DEFAULT_NAME).weight(DEFAULT_WEIGHT);
        // Add required entity
        AlPedroTaxVi alPedroTaxVi;
        if (TestUtil.findAll(em, AlPedroTaxVi.class).isEmpty()) {
            alPedroTaxVi = AlPedroTaxViResourceIT.createEntity();
            em.persist(alPedroTaxVi);
            em.flush();
        } else {
            alPedroTaxVi = TestUtil.findAll(em, AlPedroTaxVi.class).get(0);
        }
        alPounderVi.setAttributeTaxonomy(alPedroTaxVi);
        return alPounderVi;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPounderVi createUpdatedEntity(EntityManager em) {
        AlPounderVi updatedAlPounderVi = new AlPounderVi().name(UPDATED_NAME).weight(UPDATED_WEIGHT);
        // Add required entity
        AlPedroTaxVi alPedroTaxVi;
        if (TestUtil.findAll(em, AlPedroTaxVi.class).isEmpty()) {
            alPedroTaxVi = AlPedroTaxViResourceIT.createUpdatedEntity();
            em.persist(alPedroTaxVi);
            em.flush();
        } else {
            alPedroTaxVi = TestUtil.findAll(em, AlPedroTaxVi.class).get(0);
        }
        updatedAlPounderVi.setAttributeTaxonomy(alPedroTaxVi);
        return updatedAlPounderVi;
    }

    @BeforeEach
    public void initTest() {
        alPounderVi = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlPounderVi != null) {
            alPounderViRepository.delete(insertedAlPounderVi);
            insertedAlPounderVi = null;
        }
    }

    @Test
    @Transactional
    void createAlPounderVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlPounderVi
        var returnedAlPounderVi = om.readValue(
            restAlPounderViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPounderVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlPounderVi.class
        );

        // Validate the AlPounderVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlPounderViUpdatableFieldsEquals(returnedAlPounderVi, getPersistedAlPounderVi(returnedAlPounderVi));

        insertedAlPounderVi = returnedAlPounderVi;
    }

    @Test
    @Transactional
    void createAlPounderViWithExistingId() throws Exception {
        // Create the AlPounderVi with an existing ID
        alPounderVi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlPounderViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPounderVi)))
            .andExpect(status().isBadRequest());

        // Validate the AlPounderVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alPounderVi.setName(null);

        // Create the AlPounderVi, which fails.

        restAlPounderViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPounderVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlPounderVis() throws Exception {
        // Initialize the database
        insertedAlPounderVi = alPounderViRepository.saveAndFlush(alPounderVi);

        // Get all the alPounderViList
        restAlPounderViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPounderVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)));
    }

    @Test
    @Transactional
    void getAlPounderVi() throws Exception {
        // Initialize the database
        insertedAlPounderVi = alPounderViRepository.saveAndFlush(alPounderVi);

        // Get the alPounderVi
        restAlPounderViMockMvc
            .perform(get(ENTITY_API_URL_ID, alPounderVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alPounderVi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT));
    }

    @Test
    @Transactional
    void getAlPounderVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlPounderVi = alPounderViRepository.saveAndFlush(alPounderVi);

        Long id = alPounderVi.getId();

        defaultAlPounderViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlPounderViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlPounderViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlPounderVisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPounderVi = alPounderViRepository.saveAndFlush(alPounderVi);

        // Get all the alPounderViList where name equals to
        defaultAlPounderViFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlPounderVisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPounderVi = alPounderViRepository.saveAndFlush(alPounderVi);

        // Get all the alPounderViList where name in
        defaultAlPounderViFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlPounderVisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPounderVi = alPounderViRepository.saveAndFlush(alPounderVi);

        // Get all the alPounderViList where name is not null
        defaultAlPounderViFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPounderVisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPounderVi = alPounderViRepository.saveAndFlush(alPounderVi);

        // Get all the alPounderViList where name contains
        defaultAlPounderViFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlPounderVisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPounderVi = alPounderViRepository.saveAndFlush(alPounderVi);

        // Get all the alPounderViList where name does not contain
        defaultAlPounderViFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAlPounderVisByWeightIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPounderVi = alPounderViRepository.saveAndFlush(alPounderVi);

        // Get all the alPounderViList where weight equals to
        defaultAlPounderViFiltering("weight.equals=" + DEFAULT_WEIGHT, "weight.equals=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlPounderVisByWeightIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPounderVi = alPounderViRepository.saveAndFlush(alPounderVi);

        // Get all the alPounderViList where weight in
        defaultAlPounderViFiltering("weight.in=" + DEFAULT_WEIGHT + "," + UPDATED_WEIGHT, "weight.in=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlPounderVisByWeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPounderVi = alPounderViRepository.saveAndFlush(alPounderVi);

        // Get all the alPounderViList where weight is not null
        defaultAlPounderViFiltering("weight.specified=true", "weight.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPounderVisByWeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPounderVi = alPounderViRepository.saveAndFlush(alPounderVi);

        // Get all the alPounderViList where weight is greater than or equal to
        defaultAlPounderViFiltering("weight.greaterThanOrEqual=" + DEFAULT_WEIGHT, "weight.greaterThanOrEqual=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlPounderVisByWeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPounderVi = alPounderViRepository.saveAndFlush(alPounderVi);

        // Get all the alPounderViList where weight is less than or equal to
        defaultAlPounderViFiltering("weight.lessThanOrEqual=" + DEFAULT_WEIGHT, "weight.lessThanOrEqual=" + SMALLER_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlPounderVisByWeightIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlPounderVi = alPounderViRepository.saveAndFlush(alPounderVi);

        // Get all the alPounderViList where weight is less than
        defaultAlPounderViFiltering("weight.lessThan=" + UPDATED_WEIGHT, "weight.lessThan=" + DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlPounderVisByWeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlPounderVi = alPounderViRepository.saveAndFlush(alPounderVi);

        // Get all the alPounderViList where weight is greater than
        defaultAlPounderViFiltering("weight.greaterThan=" + SMALLER_WEIGHT, "weight.greaterThan=" + DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlPounderVisByAttributeTaxonomyIsEqualToSomething() throws Exception {
        AlPedroTaxVi attributeTaxonomy;
        if (TestUtil.findAll(em, AlPedroTaxVi.class).isEmpty()) {
            alPounderViRepository.saveAndFlush(alPounderVi);
            attributeTaxonomy = AlPedroTaxViResourceIT.createEntity();
        } else {
            attributeTaxonomy = TestUtil.findAll(em, AlPedroTaxVi.class).get(0);
        }
        em.persist(attributeTaxonomy);
        em.flush();
        alPounderVi.setAttributeTaxonomy(attributeTaxonomy);
        alPounderViRepository.saveAndFlush(alPounderVi);
        Long attributeTaxonomyId = attributeTaxonomy.getId();
        // Get all the alPounderViList where attributeTaxonomy equals to attributeTaxonomyId
        defaultAlPounderViShouldBeFound("attributeTaxonomyId.equals=" + attributeTaxonomyId);

        // Get all the alPounderViList where attributeTaxonomy equals to (attributeTaxonomyId + 1)
        defaultAlPounderViShouldNotBeFound("attributeTaxonomyId.equals=" + (attributeTaxonomyId + 1));
    }

    @Test
    @Transactional
    void getAllAlPounderVisByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alPounderViRepository.saveAndFlush(alPounderVi);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alPounderVi.setApplication(application);
        alPounderViRepository.saveAndFlush(alPounderVi);
        UUID applicationId = application.getId();
        // Get all the alPounderViList where application equals to applicationId
        defaultAlPounderViShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alPounderViList where application equals to UUID.randomUUID()
        defaultAlPounderViShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlPounderViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlPounderViShouldBeFound(shouldBeFound);
        defaultAlPounderViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlPounderViShouldBeFound(String filter) throws Exception {
        restAlPounderViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPounderVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)));

        // Check, that the count call also returns 1
        restAlPounderViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlPounderViShouldNotBeFound(String filter) throws Exception {
        restAlPounderViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlPounderViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlPounderVi() throws Exception {
        // Get the alPounderVi
        restAlPounderViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlPounderVi() throws Exception {
        // Initialize the database
        insertedAlPounderVi = alPounderViRepository.saveAndFlush(alPounderVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPounderVi
        AlPounderVi updatedAlPounderVi = alPounderViRepository.findById(alPounderVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlPounderVi are not directly saved in db
        em.detach(updatedAlPounderVi);
        updatedAlPounderVi.name(UPDATED_NAME).weight(UPDATED_WEIGHT);

        restAlPounderViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlPounderVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlPounderVi))
            )
            .andExpect(status().isOk());

        // Validate the AlPounderVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlPounderViToMatchAllProperties(updatedAlPounderVi);
    }

    @Test
    @Transactional
    void putNonExistingAlPounderVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPounderVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPounderViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alPounderVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPounderVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPounderVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlPounderVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPounderVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPounderViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPounderVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPounderVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlPounderVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPounderVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPounderViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPounderVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPounderVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlPounderViWithPatch() throws Exception {
        // Initialize the database
        insertedAlPounderVi = alPounderViRepository.saveAndFlush(alPounderVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPounderVi using partial update
        AlPounderVi partialUpdatedAlPounderVi = new AlPounderVi();
        partialUpdatedAlPounderVi.setId(alPounderVi.getId());

        partialUpdatedAlPounderVi.name(UPDATED_NAME);

        restAlPounderViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPounderVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPounderVi))
            )
            .andExpect(status().isOk());

        // Validate the AlPounderVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPounderViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlPounderVi, alPounderVi),
            getPersistedAlPounderVi(alPounderVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlPounderViWithPatch() throws Exception {
        // Initialize the database
        insertedAlPounderVi = alPounderViRepository.saveAndFlush(alPounderVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPounderVi using partial update
        AlPounderVi partialUpdatedAlPounderVi = new AlPounderVi();
        partialUpdatedAlPounderVi.setId(alPounderVi.getId());

        partialUpdatedAlPounderVi.name(UPDATED_NAME).weight(UPDATED_WEIGHT);

        restAlPounderViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPounderVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPounderVi))
            )
            .andExpect(status().isOk());

        // Validate the AlPounderVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPounderViUpdatableFieldsEquals(partialUpdatedAlPounderVi, getPersistedAlPounderVi(partialUpdatedAlPounderVi));
    }

    @Test
    @Transactional
    void patchNonExistingAlPounderVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPounderVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPounderViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alPounderVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPounderVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPounderVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlPounderVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPounderVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPounderViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPounderVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPounderVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlPounderVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPounderVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPounderViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alPounderVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPounderVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlPounderVi() throws Exception {
        // Initialize the database
        insertedAlPounderVi = alPounderViRepository.saveAndFlush(alPounderVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alPounderVi
        restAlPounderViMockMvc
            .perform(delete(ENTITY_API_URL_ID, alPounderVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alPounderViRepository.count();
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

    protected AlPounderVi getPersistedAlPounderVi(AlPounderVi alPounderVi) {
        return alPounderViRepository.findById(alPounderVi.getId()).orElseThrow();
    }

    protected void assertPersistedAlPounderViToMatchAllProperties(AlPounderVi expectedAlPounderVi) {
        assertAlPounderViAllPropertiesEquals(expectedAlPounderVi, getPersistedAlPounderVi(expectedAlPounderVi));
    }

    protected void assertPersistedAlPounderViToMatchUpdatableProperties(AlPounderVi expectedAlPounderVi) {
        assertAlPounderViAllUpdatablePropertiesEquals(expectedAlPounderVi, getPersistedAlPounderVi(expectedAlPounderVi));
    }
}
