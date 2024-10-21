package ai.realworld.web.rest;

import static ai.realworld.domain.AlBetonamuRelationAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlAlexType;
import ai.realworld.domain.AlBetonamuRelation;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.enumeration.ReductionType;
import ai.realworld.repository.AlBetonamuRelationRepository;
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
 * Integration tests for the {@link AlBetonamuRelationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlBetonamuRelationResourceIT {

    private static final ReductionType DEFAULT_TYPE = ReductionType.SUPPLIER_CUSTOMER_B2B;
    private static final ReductionType UPDATED_TYPE = ReductionType.SUPPLIER_CUSTOMER_B2C;

    private static final String ENTITY_API_URL = "/api/al-betonamu-relations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlBetonamuRelationRepository alBetonamuRelationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlBetonamuRelationMockMvc;

    private AlBetonamuRelation alBetonamuRelation;

    private AlBetonamuRelation insertedAlBetonamuRelation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlBetonamuRelation createEntity(EntityManager em) {
        AlBetonamuRelation alBetonamuRelation = new AlBetonamuRelation().type(DEFAULT_TYPE);
        // Add required entity
        AlAlexType alAlexType;
        if (TestUtil.findAll(em, AlAlexType.class).isEmpty()) {
            alAlexType = AlAlexTypeResourceIT.createEntity();
            em.persist(alAlexType);
            em.flush();
        } else {
            alAlexType = TestUtil.findAll(em, AlAlexType.class).get(0);
        }
        alBetonamuRelation.setSupplier(alAlexType);
        // Add required entity
        alBetonamuRelation.setCustomer(alAlexType);
        return alBetonamuRelation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlBetonamuRelation createUpdatedEntity(EntityManager em) {
        AlBetonamuRelation updatedAlBetonamuRelation = new AlBetonamuRelation().type(UPDATED_TYPE);
        // Add required entity
        AlAlexType alAlexType;
        if (TestUtil.findAll(em, AlAlexType.class).isEmpty()) {
            alAlexType = AlAlexTypeResourceIT.createUpdatedEntity();
            em.persist(alAlexType);
            em.flush();
        } else {
            alAlexType = TestUtil.findAll(em, AlAlexType.class).get(0);
        }
        updatedAlBetonamuRelation.setSupplier(alAlexType);
        // Add required entity
        updatedAlBetonamuRelation.setCustomer(alAlexType);
        return updatedAlBetonamuRelation;
    }

    @BeforeEach
    public void initTest() {
        alBetonamuRelation = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlBetonamuRelation != null) {
            alBetonamuRelationRepository.delete(insertedAlBetonamuRelation);
            insertedAlBetonamuRelation = null;
        }
    }

    @Test
    @Transactional
    void createAlBetonamuRelation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlBetonamuRelation
        var returnedAlBetonamuRelation = om.readValue(
            restAlBetonamuRelationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alBetonamuRelation)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlBetonamuRelation.class
        );

        // Validate the AlBetonamuRelation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlBetonamuRelationUpdatableFieldsEquals(
            returnedAlBetonamuRelation,
            getPersistedAlBetonamuRelation(returnedAlBetonamuRelation)
        );

        insertedAlBetonamuRelation = returnedAlBetonamuRelation;
    }

    @Test
    @Transactional
    void createAlBetonamuRelationWithExistingId() throws Exception {
        // Create the AlBetonamuRelation with an existing ID
        alBetonamuRelation.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlBetonamuRelationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alBetonamuRelation)))
            .andExpect(status().isBadRequest());

        // Validate the AlBetonamuRelation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alBetonamuRelation.setType(null);

        // Create the AlBetonamuRelation, which fails.

        restAlBetonamuRelationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alBetonamuRelation)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlBetonamuRelations() throws Exception {
        // Initialize the database
        insertedAlBetonamuRelation = alBetonamuRelationRepository.saveAndFlush(alBetonamuRelation);

        // Get all the alBetonamuRelationList
        restAlBetonamuRelationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alBetonamuRelation.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    void getAlBetonamuRelation() throws Exception {
        // Initialize the database
        insertedAlBetonamuRelation = alBetonamuRelationRepository.saveAndFlush(alBetonamuRelation);

        // Get the alBetonamuRelation
        restAlBetonamuRelationMockMvc
            .perform(get(ENTITY_API_URL_ID, alBetonamuRelation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alBetonamuRelation.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getAlBetonamuRelationsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlBetonamuRelation = alBetonamuRelationRepository.saveAndFlush(alBetonamuRelation);

        Long id = alBetonamuRelation.getId();

        defaultAlBetonamuRelationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlBetonamuRelationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlBetonamuRelationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlBetonamuRelationsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlBetonamuRelation = alBetonamuRelationRepository.saveAndFlush(alBetonamuRelation);

        // Get all the alBetonamuRelationList where type equals to
        defaultAlBetonamuRelationFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAlBetonamuRelationsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlBetonamuRelation = alBetonamuRelationRepository.saveAndFlush(alBetonamuRelation);

        // Get all the alBetonamuRelationList where type in
        defaultAlBetonamuRelationFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAlBetonamuRelationsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlBetonamuRelation = alBetonamuRelationRepository.saveAndFlush(alBetonamuRelation);

        // Get all the alBetonamuRelationList where type is not null
        defaultAlBetonamuRelationFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllAlBetonamuRelationsBySupplierIsEqualToSomething() throws Exception {
        AlAlexType supplier;
        if (TestUtil.findAll(em, AlAlexType.class).isEmpty()) {
            alBetonamuRelationRepository.saveAndFlush(alBetonamuRelation);
            supplier = AlAlexTypeResourceIT.createEntity();
        } else {
            supplier = TestUtil.findAll(em, AlAlexType.class).get(0);
        }
        em.persist(supplier);
        em.flush();
        alBetonamuRelation.setSupplier(supplier);
        alBetonamuRelationRepository.saveAndFlush(alBetonamuRelation);
        UUID supplierId = supplier.getId();
        // Get all the alBetonamuRelationList where supplier equals to supplierId
        defaultAlBetonamuRelationShouldBeFound("supplierId.equals=" + supplierId);

        // Get all the alBetonamuRelationList where supplier equals to UUID.randomUUID()
        defaultAlBetonamuRelationShouldNotBeFound("supplierId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlBetonamuRelationsByCustomerIsEqualToSomething() throws Exception {
        AlAlexType customer;
        if (TestUtil.findAll(em, AlAlexType.class).isEmpty()) {
            alBetonamuRelationRepository.saveAndFlush(alBetonamuRelation);
            customer = AlAlexTypeResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, AlAlexType.class).get(0);
        }
        em.persist(customer);
        em.flush();
        alBetonamuRelation.setCustomer(customer);
        alBetonamuRelationRepository.saveAndFlush(alBetonamuRelation);
        UUID customerId = customer.getId();
        // Get all the alBetonamuRelationList where customer equals to customerId
        defaultAlBetonamuRelationShouldBeFound("customerId.equals=" + customerId);

        // Get all the alBetonamuRelationList where customer equals to UUID.randomUUID()
        defaultAlBetonamuRelationShouldNotBeFound("customerId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlBetonamuRelationsByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alBetonamuRelationRepository.saveAndFlush(alBetonamuRelation);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alBetonamuRelation.setApplication(application);
        alBetonamuRelationRepository.saveAndFlush(alBetonamuRelation);
        UUID applicationId = application.getId();
        // Get all the alBetonamuRelationList where application equals to applicationId
        defaultAlBetonamuRelationShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alBetonamuRelationList where application equals to UUID.randomUUID()
        defaultAlBetonamuRelationShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlBetonamuRelationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlBetonamuRelationShouldBeFound(shouldBeFound);
        defaultAlBetonamuRelationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlBetonamuRelationShouldBeFound(String filter) throws Exception {
        restAlBetonamuRelationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alBetonamuRelation.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));

        // Check, that the count call also returns 1
        restAlBetonamuRelationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlBetonamuRelationShouldNotBeFound(String filter) throws Exception {
        restAlBetonamuRelationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlBetonamuRelationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlBetonamuRelation() throws Exception {
        // Get the alBetonamuRelation
        restAlBetonamuRelationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlBetonamuRelation() throws Exception {
        // Initialize the database
        insertedAlBetonamuRelation = alBetonamuRelationRepository.saveAndFlush(alBetonamuRelation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alBetonamuRelation
        AlBetonamuRelation updatedAlBetonamuRelation = alBetonamuRelationRepository.findById(alBetonamuRelation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlBetonamuRelation are not directly saved in db
        em.detach(updatedAlBetonamuRelation);
        updatedAlBetonamuRelation.type(UPDATED_TYPE);

        restAlBetonamuRelationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlBetonamuRelation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlBetonamuRelation))
            )
            .andExpect(status().isOk());

        // Validate the AlBetonamuRelation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlBetonamuRelationToMatchAllProperties(updatedAlBetonamuRelation);
    }

    @Test
    @Transactional
    void putNonExistingAlBetonamuRelation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alBetonamuRelation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlBetonamuRelationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alBetonamuRelation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alBetonamuRelation))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlBetonamuRelation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlBetonamuRelation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alBetonamuRelation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlBetonamuRelationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alBetonamuRelation))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlBetonamuRelation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlBetonamuRelation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alBetonamuRelation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlBetonamuRelationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alBetonamuRelation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlBetonamuRelation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlBetonamuRelationWithPatch() throws Exception {
        // Initialize the database
        insertedAlBetonamuRelation = alBetonamuRelationRepository.saveAndFlush(alBetonamuRelation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alBetonamuRelation using partial update
        AlBetonamuRelation partialUpdatedAlBetonamuRelation = new AlBetonamuRelation();
        partialUpdatedAlBetonamuRelation.setId(alBetonamuRelation.getId());

        partialUpdatedAlBetonamuRelation.type(UPDATED_TYPE);

        restAlBetonamuRelationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlBetonamuRelation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlBetonamuRelation))
            )
            .andExpect(status().isOk());

        // Validate the AlBetonamuRelation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlBetonamuRelationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlBetonamuRelation, alBetonamuRelation),
            getPersistedAlBetonamuRelation(alBetonamuRelation)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlBetonamuRelationWithPatch() throws Exception {
        // Initialize the database
        insertedAlBetonamuRelation = alBetonamuRelationRepository.saveAndFlush(alBetonamuRelation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alBetonamuRelation using partial update
        AlBetonamuRelation partialUpdatedAlBetonamuRelation = new AlBetonamuRelation();
        partialUpdatedAlBetonamuRelation.setId(alBetonamuRelation.getId());

        partialUpdatedAlBetonamuRelation.type(UPDATED_TYPE);

        restAlBetonamuRelationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlBetonamuRelation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlBetonamuRelation))
            )
            .andExpect(status().isOk());

        // Validate the AlBetonamuRelation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlBetonamuRelationUpdatableFieldsEquals(
            partialUpdatedAlBetonamuRelation,
            getPersistedAlBetonamuRelation(partialUpdatedAlBetonamuRelation)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAlBetonamuRelation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alBetonamuRelation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlBetonamuRelationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alBetonamuRelation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alBetonamuRelation))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlBetonamuRelation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlBetonamuRelation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alBetonamuRelation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlBetonamuRelationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alBetonamuRelation))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlBetonamuRelation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlBetonamuRelation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alBetonamuRelation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlBetonamuRelationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alBetonamuRelation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlBetonamuRelation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlBetonamuRelation() throws Exception {
        // Initialize the database
        insertedAlBetonamuRelation = alBetonamuRelationRepository.saveAndFlush(alBetonamuRelation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alBetonamuRelation
        restAlBetonamuRelationMockMvc
            .perform(delete(ENTITY_API_URL_ID, alBetonamuRelation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alBetonamuRelationRepository.count();
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

    protected AlBetonamuRelation getPersistedAlBetonamuRelation(AlBetonamuRelation alBetonamuRelation) {
        return alBetonamuRelationRepository.findById(alBetonamuRelation.getId()).orElseThrow();
    }

    protected void assertPersistedAlBetonamuRelationToMatchAllProperties(AlBetonamuRelation expectedAlBetonamuRelation) {
        assertAlBetonamuRelationAllPropertiesEquals(expectedAlBetonamuRelation, getPersistedAlBetonamuRelation(expectedAlBetonamuRelation));
    }

    protected void assertPersistedAlBetonamuRelationToMatchUpdatableProperties(AlBetonamuRelation expectedAlBetonamuRelation) {
        assertAlBetonamuRelationAllUpdatablePropertiesEquals(
            expectedAlBetonamuRelation,
            getPersistedAlBetonamuRelation(expectedAlBetonamuRelation)
        );
    }
}
