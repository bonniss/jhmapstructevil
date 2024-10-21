package ai.realworld.web.rest;

import static ai.realworld.domain.AlBetonamuRelationViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlAlexTypeVi;
import ai.realworld.domain.AlBetonamuRelationVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.enumeration.ReductionType;
import ai.realworld.repository.AlBetonamuRelationViRepository;
import ai.realworld.service.dto.AlBetonamuRelationViDTO;
import ai.realworld.service.mapper.AlBetonamuRelationViMapper;
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
 * Integration tests for the {@link AlBetonamuRelationViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlBetonamuRelationViResourceIT {

    private static final ReductionType DEFAULT_TYPE = ReductionType.SUPPLIER_CUSTOMER_B2B;
    private static final ReductionType UPDATED_TYPE = ReductionType.SUPPLIER_CUSTOMER_B2C;

    private static final String ENTITY_API_URL = "/api/al-betonamu-relation-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlBetonamuRelationViRepository alBetonamuRelationViRepository;

    @Autowired
    private AlBetonamuRelationViMapper alBetonamuRelationViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlBetonamuRelationViMockMvc;

    private AlBetonamuRelationVi alBetonamuRelationVi;

    private AlBetonamuRelationVi insertedAlBetonamuRelationVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlBetonamuRelationVi createEntity(EntityManager em) {
        AlBetonamuRelationVi alBetonamuRelationVi = new AlBetonamuRelationVi().type(DEFAULT_TYPE);
        // Add required entity
        AlAlexTypeVi alAlexTypeVi;
        if (TestUtil.findAll(em, AlAlexTypeVi.class).isEmpty()) {
            alAlexTypeVi = AlAlexTypeViResourceIT.createEntity();
            em.persist(alAlexTypeVi);
            em.flush();
        } else {
            alAlexTypeVi = TestUtil.findAll(em, AlAlexTypeVi.class).get(0);
        }
        alBetonamuRelationVi.setSupplier(alAlexTypeVi);
        // Add required entity
        alBetonamuRelationVi.setCustomer(alAlexTypeVi);
        return alBetonamuRelationVi;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlBetonamuRelationVi createUpdatedEntity(EntityManager em) {
        AlBetonamuRelationVi updatedAlBetonamuRelationVi = new AlBetonamuRelationVi().type(UPDATED_TYPE);
        // Add required entity
        AlAlexTypeVi alAlexTypeVi;
        if (TestUtil.findAll(em, AlAlexTypeVi.class).isEmpty()) {
            alAlexTypeVi = AlAlexTypeViResourceIT.createUpdatedEntity();
            em.persist(alAlexTypeVi);
            em.flush();
        } else {
            alAlexTypeVi = TestUtil.findAll(em, AlAlexTypeVi.class).get(0);
        }
        updatedAlBetonamuRelationVi.setSupplier(alAlexTypeVi);
        // Add required entity
        updatedAlBetonamuRelationVi.setCustomer(alAlexTypeVi);
        return updatedAlBetonamuRelationVi;
    }

    @BeforeEach
    public void initTest() {
        alBetonamuRelationVi = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlBetonamuRelationVi != null) {
            alBetonamuRelationViRepository.delete(insertedAlBetonamuRelationVi);
            insertedAlBetonamuRelationVi = null;
        }
    }

    @Test
    @Transactional
    void createAlBetonamuRelationVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlBetonamuRelationVi
        AlBetonamuRelationViDTO alBetonamuRelationViDTO = alBetonamuRelationViMapper.toDto(alBetonamuRelationVi);
        var returnedAlBetonamuRelationViDTO = om.readValue(
            restAlBetonamuRelationViMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alBetonamuRelationViDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlBetonamuRelationViDTO.class
        );

        // Validate the AlBetonamuRelationVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlBetonamuRelationVi = alBetonamuRelationViMapper.toEntity(returnedAlBetonamuRelationViDTO);
        assertAlBetonamuRelationViUpdatableFieldsEquals(
            returnedAlBetonamuRelationVi,
            getPersistedAlBetonamuRelationVi(returnedAlBetonamuRelationVi)
        );

        insertedAlBetonamuRelationVi = returnedAlBetonamuRelationVi;
    }

    @Test
    @Transactional
    void createAlBetonamuRelationViWithExistingId() throws Exception {
        // Create the AlBetonamuRelationVi with an existing ID
        alBetonamuRelationVi.setId(1L);
        AlBetonamuRelationViDTO alBetonamuRelationViDTO = alBetonamuRelationViMapper.toDto(alBetonamuRelationVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlBetonamuRelationViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alBetonamuRelationViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlBetonamuRelationVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alBetonamuRelationVi.setType(null);

        // Create the AlBetonamuRelationVi, which fails.
        AlBetonamuRelationViDTO alBetonamuRelationViDTO = alBetonamuRelationViMapper.toDto(alBetonamuRelationVi);

        restAlBetonamuRelationViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alBetonamuRelationViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlBetonamuRelationVis() throws Exception {
        // Initialize the database
        insertedAlBetonamuRelationVi = alBetonamuRelationViRepository.saveAndFlush(alBetonamuRelationVi);

        // Get all the alBetonamuRelationViList
        restAlBetonamuRelationViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alBetonamuRelationVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    void getAlBetonamuRelationVi() throws Exception {
        // Initialize the database
        insertedAlBetonamuRelationVi = alBetonamuRelationViRepository.saveAndFlush(alBetonamuRelationVi);

        // Get the alBetonamuRelationVi
        restAlBetonamuRelationViMockMvc
            .perform(get(ENTITY_API_URL_ID, alBetonamuRelationVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alBetonamuRelationVi.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getAlBetonamuRelationVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlBetonamuRelationVi = alBetonamuRelationViRepository.saveAndFlush(alBetonamuRelationVi);

        Long id = alBetonamuRelationVi.getId();

        defaultAlBetonamuRelationViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlBetonamuRelationViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlBetonamuRelationViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlBetonamuRelationVisByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlBetonamuRelationVi = alBetonamuRelationViRepository.saveAndFlush(alBetonamuRelationVi);

        // Get all the alBetonamuRelationViList where type equals to
        defaultAlBetonamuRelationViFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAlBetonamuRelationVisByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlBetonamuRelationVi = alBetonamuRelationViRepository.saveAndFlush(alBetonamuRelationVi);

        // Get all the alBetonamuRelationViList where type in
        defaultAlBetonamuRelationViFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAlBetonamuRelationVisByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlBetonamuRelationVi = alBetonamuRelationViRepository.saveAndFlush(alBetonamuRelationVi);

        // Get all the alBetonamuRelationViList where type is not null
        defaultAlBetonamuRelationViFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllAlBetonamuRelationVisBySupplierIsEqualToSomething() throws Exception {
        AlAlexTypeVi supplier;
        if (TestUtil.findAll(em, AlAlexTypeVi.class).isEmpty()) {
            alBetonamuRelationViRepository.saveAndFlush(alBetonamuRelationVi);
            supplier = AlAlexTypeViResourceIT.createEntity();
        } else {
            supplier = TestUtil.findAll(em, AlAlexTypeVi.class).get(0);
        }
        em.persist(supplier);
        em.flush();
        alBetonamuRelationVi.setSupplier(supplier);
        alBetonamuRelationViRepository.saveAndFlush(alBetonamuRelationVi);
        UUID supplierId = supplier.getId();
        // Get all the alBetonamuRelationViList where supplier equals to supplierId
        defaultAlBetonamuRelationViShouldBeFound("supplierId.equals=" + supplierId);

        // Get all the alBetonamuRelationViList where supplier equals to UUID.randomUUID()
        defaultAlBetonamuRelationViShouldNotBeFound("supplierId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlBetonamuRelationVisByCustomerIsEqualToSomething() throws Exception {
        AlAlexTypeVi customer;
        if (TestUtil.findAll(em, AlAlexTypeVi.class).isEmpty()) {
            alBetonamuRelationViRepository.saveAndFlush(alBetonamuRelationVi);
            customer = AlAlexTypeViResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, AlAlexTypeVi.class).get(0);
        }
        em.persist(customer);
        em.flush();
        alBetonamuRelationVi.setCustomer(customer);
        alBetonamuRelationViRepository.saveAndFlush(alBetonamuRelationVi);
        UUID customerId = customer.getId();
        // Get all the alBetonamuRelationViList where customer equals to customerId
        defaultAlBetonamuRelationViShouldBeFound("customerId.equals=" + customerId);

        // Get all the alBetonamuRelationViList where customer equals to UUID.randomUUID()
        defaultAlBetonamuRelationViShouldNotBeFound("customerId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlBetonamuRelationVisByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alBetonamuRelationViRepository.saveAndFlush(alBetonamuRelationVi);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alBetonamuRelationVi.setApplication(application);
        alBetonamuRelationViRepository.saveAndFlush(alBetonamuRelationVi);
        UUID applicationId = application.getId();
        // Get all the alBetonamuRelationViList where application equals to applicationId
        defaultAlBetonamuRelationViShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alBetonamuRelationViList where application equals to UUID.randomUUID()
        defaultAlBetonamuRelationViShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlBetonamuRelationViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlBetonamuRelationViShouldBeFound(shouldBeFound);
        defaultAlBetonamuRelationViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlBetonamuRelationViShouldBeFound(String filter) throws Exception {
        restAlBetonamuRelationViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alBetonamuRelationVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));

        // Check, that the count call also returns 1
        restAlBetonamuRelationViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlBetonamuRelationViShouldNotBeFound(String filter) throws Exception {
        restAlBetonamuRelationViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlBetonamuRelationViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlBetonamuRelationVi() throws Exception {
        // Get the alBetonamuRelationVi
        restAlBetonamuRelationViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlBetonamuRelationVi() throws Exception {
        // Initialize the database
        insertedAlBetonamuRelationVi = alBetonamuRelationViRepository.saveAndFlush(alBetonamuRelationVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alBetonamuRelationVi
        AlBetonamuRelationVi updatedAlBetonamuRelationVi = alBetonamuRelationViRepository
            .findById(alBetonamuRelationVi.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedAlBetonamuRelationVi are not directly saved in db
        em.detach(updatedAlBetonamuRelationVi);
        updatedAlBetonamuRelationVi.type(UPDATED_TYPE);
        AlBetonamuRelationViDTO alBetonamuRelationViDTO = alBetonamuRelationViMapper.toDto(updatedAlBetonamuRelationVi);

        restAlBetonamuRelationViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alBetonamuRelationViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alBetonamuRelationViDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlBetonamuRelationVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlBetonamuRelationViToMatchAllProperties(updatedAlBetonamuRelationVi);
    }

    @Test
    @Transactional
    void putNonExistingAlBetonamuRelationVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alBetonamuRelationVi.setId(longCount.incrementAndGet());

        // Create the AlBetonamuRelationVi
        AlBetonamuRelationViDTO alBetonamuRelationViDTO = alBetonamuRelationViMapper.toDto(alBetonamuRelationVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlBetonamuRelationViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alBetonamuRelationViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alBetonamuRelationViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlBetonamuRelationVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlBetonamuRelationVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alBetonamuRelationVi.setId(longCount.incrementAndGet());

        // Create the AlBetonamuRelationVi
        AlBetonamuRelationViDTO alBetonamuRelationViDTO = alBetonamuRelationViMapper.toDto(alBetonamuRelationVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlBetonamuRelationViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alBetonamuRelationViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlBetonamuRelationVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlBetonamuRelationVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alBetonamuRelationVi.setId(longCount.incrementAndGet());

        // Create the AlBetonamuRelationVi
        AlBetonamuRelationViDTO alBetonamuRelationViDTO = alBetonamuRelationViMapper.toDto(alBetonamuRelationVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlBetonamuRelationViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alBetonamuRelationViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlBetonamuRelationVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlBetonamuRelationViWithPatch() throws Exception {
        // Initialize the database
        insertedAlBetonamuRelationVi = alBetonamuRelationViRepository.saveAndFlush(alBetonamuRelationVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alBetonamuRelationVi using partial update
        AlBetonamuRelationVi partialUpdatedAlBetonamuRelationVi = new AlBetonamuRelationVi();
        partialUpdatedAlBetonamuRelationVi.setId(alBetonamuRelationVi.getId());

        restAlBetonamuRelationViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlBetonamuRelationVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlBetonamuRelationVi))
            )
            .andExpect(status().isOk());

        // Validate the AlBetonamuRelationVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlBetonamuRelationViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlBetonamuRelationVi, alBetonamuRelationVi),
            getPersistedAlBetonamuRelationVi(alBetonamuRelationVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlBetonamuRelationViWithPatch() throws Exception {
        // Initialize the database
        insertedAlBetonamuRelationVi = alBetonamuRelationViRepository.saveAndFlush(alBetonamuRelationVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alBetonamuRelationVi using partial update
        AlBetonamuRelationVi partialUpdatedAlBetonamuRelationVi = new AlBetonamuRelationVi();
        partialUpdatedAlBetonamuRelationVi.setId(alBetonamuRelationVi.getId());

        partialUpdatedAlBetonamuRelationVi.type(UPDATED_TYPE);

        restAlBetonamuRelationViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlBetonamuRelationVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlBetonamuRelationVi))
            )
            .andExpect(status().isOk());

        // Validate the AlBetonamuRelationVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlBetonamuRelationViUpdatableFieldsEquals(
            partialUpdatedAlBetonamuRelationVi,
            getPersistedAlBetonamuRelationVi(partialUpdatedAlBetonamuRelationVi)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAlBetonamuRelationVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alBetonamuRelationVi.setId(longCount.incrementAndGet());

        // Create the AlBetonamuRelationVi
        AlBetonamuRelationViDTO alBetonamuRelationViDTO = alBetonamuRelationViMapper.toDto(alBetonamuRelationVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlBetonamuRelationViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alBetonamuRelationViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alBetonamuRelationViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlBetonamuRelationVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlBetonamuRelationVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alBetonamuRelationVi.setId(longCount.incrementAndGet());

        // Create the AlBetonamuRelationVi
        AlBetonamuRelationViDTO alBetonamuRelationViDTO = alBetonamuRelationViMapper.toDto(alBetonamuRelationVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlBetonamuRelationViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alBetonamuRelationViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlBetonamuRelationVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlBetonamuRelationVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alBetonamuRelationVi.setId(longCount.incrementAndGet());

        // Create the AlBetonamuRelationVi
        AlBetonamuRelationViDTO alBetonamuRelationViDTO = alBetonamuRelationViMapper.toDto(alBetonamuRelationVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlBetonamuRelationViMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alBetonamuRelationViDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlBetonamuRelationVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlBetonamuRelationVi() throws Exception {
        // Initialize the database
        insertedAlBetonamuRelationVi = alBetonamuRelationViRepository.saveAndFlush(alBetonamuRelationVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alBetonamuRelationVi
        restAlBetonamuRelationViMockMvc
            .perform(delete(ENTITY_API_URL_ID, alBetonamuRelationVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alBetonamuRelationViRepository.count();
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

    protected AlBetonamuRelationVi getPersistedAlBetonamuRelationVi(AlBetonamuRelationVi alBetonamuRelationVi) {
        return alBetonamuRelationViRepository.findById(alBetonamuRelationVi.getId()).orElseThrow();
    }

    protected void assertPersistedAlBetonamuRelationViToMatchAllProperties(AlBetonamuRelationVi expectedAlBetonamuRelationVi) {
        assertAlBetonamuRelationViAllPropertiesEquals(
            expectedAlBetonamuRelationVi,
            getPersistedAlBetonamuRelationVi(expectedAlBetonamuRelationVi)
        );
    }

    protected void assertPersistedAlBetonamuRelationViToMatchUpdatableProperties(AlBetonamuRelationVi expectedAlBetonamuRelationVi) {
        assertAlBetonamuRelationViAllUpdatablePropertiesEquals(
            expectedAlBetonamuRelationVi,
            getPersistedAlBetonamuRelationVi(expectedAlBetonamuRelationVi)
        );
    }
}
