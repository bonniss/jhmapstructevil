package ai.realworld.web.rest;

import static ai.realworld.domain.AlPacinoPointHistoryViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlPacino;
import ai.realworld.domain.AlPacinoPointHistoryVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.enumeration.EeriePointSource;
import ai.realworld.repository.AlPacinoPointHistoryViRepository;
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
 * Integration tests for the {@link AlPacinoPointHistoryViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlPacinoPointHistoryViResourceIT {

    private static final EeriePointSource DEFAULT_SOURCE = EeriePointSource.PURCHASE;
    private static final EeriePointSource UPDATED_SOURCE = EeriePointSource.MENTI;

    private static final String DEFAULT_ASSOCIATED_ID = "AAAAAAAAAA";
    private static final String UPDATED_ASSOCIATED_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_POINT_AMOUNT = 1;
    private static final Integer UPDATED_POINT_AMOUNT = 2;
    private static final Integer SMALLER_POINT_AMOUNT = 1 - 1;

    private static final String ENTITY_API_URL = "/api/al-pacino-point-history-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlPacinoPointHistoryViRepository alPacinoPointHistoryViRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlPacinoPointHistoryViMockMvc;

    private AlPacinoPointHistoryVi alPacinoPointHistoryVi;

    private AlPacinoPointHistoryVi insertedAlPacinoPointHistoryVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPacinoPointHistoryVi createEntity() {
        return new AlPacinoPointHistoryVi().source(DEFAULT_SOURCE).associatedId(DEFAULT_ASSOCIATED_ID).pointAmount(DEFAULT_POINT_AMOUNT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPacinoPointHistoryVi createUpdatedEntity() {
        return new AlPacinoPointHistoryVi().source(UPDATED_SOURCE).associatedId(UPDATED_ASSOCIATED_ID).pointAmount(UPDATED_POINT_AMOUNT);
    }

    @BeforeEach
    public void initTest() {
        alPacinoPointHistoryVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlPacinoPointHistoryVi != null) {
            alPacinoPointHistoryViRepository.delete(insertedAlPacinoPointHistoryVi);
            insertedAlPacinoPointHistoryVi = null;
        }
    }

    @Test
    @Transactional
    void createAlPacinoPointHistoryVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlPacinoPointHistoryVi
        var returnedAlPacinoPointHistoryVi = om.readValue(
            restAlPacinoPointHistoryViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPacinoPointHistoryVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlPacinoPointHistoryVi.class
        );

        // Validate the AlPacinoPointHistoryVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlPacinoPointHistoryViUpdatableFieldsEquals(
            returnedAlPacinoPointHistoryVi,
            getPersistedAlPacinoPointHistoryVi(returnedAlPacinoPointHistoryVi)
        );

        insertedAlPacinoPointHistoryVi = returnedAlPacinoPointHistoryVi;
    }

    @Test
    @Transactional
    void createAlPacinoPointHistoryViWithExistingId() throws Exception {
        // Create the AlPacinoPointHistoryVi with an existing ID
        alPacinoPointHistoryVi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlPacinoPointHistoryViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPacinoPointHistoryVi)))
            .andExpect(status().isBadRequest());

        // Validate the AlPacinoPointHistoryVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAlPacinoPointHistoryVis() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistoryVi = alPacinoPointHistoryViRepository.saveAndFlush(alPacinoPointHistoryVi);

        // Get all the alPacinoPointHistoryViList
        restAlPacinoPointHistoryViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPacinoPointHistoryVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE.toString())))
            .andExpect(jsonPath("$.[*].associatedId").value(hasItem(DEFAULT_ASSOCIATED_ID)))
            .andExpect(jsonPath("$.[*].pointAmount").value(hasItem(DEFAULT_POINT_AMOUNT)));
    }

    @Test
    @Transactional
    void getAlPacinoPointHistoryVi() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistoryVi = alPacinoPointHistoryViRepository.saveAndFlush(alPacinoPointHistoryVi);

        // Get the alPacinoPointHistoryVi
        restAlPacinoPointHistoryViMockMvc
            .perform(get(ENTITY_API_URL_ID, alPacinoPointHistoryVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alPacinoPointHistoryVi.getId().intValue()))
            .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE.toString()))
            .andExpect(jsonPath("$.associatedId").value(DEFAULT_ASSOCIATED_ID))
            .andExpect(jsonPath("$.pointAmount").value(DEFAULT_POINT_AMOUNT));
    }

    @Test
    @Transactional
    void getAlPacinoPointHistoryVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistoryVi = alPacinoPointHistoryViRepository.saveAndFlush(alPacinoPointHistoryVi);

        Long id = alPacinoPointHistoryVi.getId();

        defaultAlPacinoPointHistoryViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlPacinoPointHistoryViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlPacinoPointHistoryViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlPacinoPointHistoryVisBySourceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistoryVi = alPacinoPointHistoryViRepository.saveAndFlush(alPacinoPointHistoryVi);

        // Get all the alPacinoPointHistoryViList where source equals to
        defaultAlPacinoPointHistoryViFiltering("source.equals=" + DEFAULT_SOURCE, "source.equals=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void getAllAlPacinoPointHistoryVisBySourceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistoryVi = alPacinoPointHistoryViRepository.saveAndFlush(alPacinoPointHistoryVi);

        // Get all the alPacinoPointHistoryViList where source in
        defaultAlPacinoPointHistoryViFiltering("source.in=" + DEFAULT_SOURCE + "," + UPDATED_SOURCE, "source.in=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void getAllAlPacinoPointHistoryVisBySourceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistoryVi = alPacinoPointHistoryViRepository.saveAndFlush(alPacinoPointHistoryVi);

        // Get all the alPacinoPointHistoryViList where source is not null
        defaultAlPacinoPointHistoryViFiltering("source.specified=true", "source.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPacinoPointHistoryVisByAssociatedIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistoryVi = alPacinoPointHistoryViRepository.saveAndFlush(alPacinoPointHistoryVi);

        // Get all the alPacinoPointHistoryViList where associatedId equals to
        defaultAlPacinoPointHistoryViFiltering(
            "associatedId.equals=" + DEFAULT_ASSOCIATED_ID,
            "associatedId.equals=" + UPDATED_ASSOCIATED_ID
        );
    }

    @Test
    @Transactional
    void getAllAlPacinoPointHistoryVisByAssociatedIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistoryVi = alPacinoPointHistoryViRepository.saveAndFlush(alPacinoPointHistoryVi);

        // Get all the alPacinoPointHistoryViList where associatedId in
        defaultAlPacinoPointHistoryViFiltering(
            "associatedId.in=" + DEFAULT_ASSOCIATED_ID + "," + UPDATED_ASSOCIATED_ID,
            "associatedId.in=" + UPDATED_ASSOCIATED_ID
        );
    }

    @Test
    @Transactional
    void getAllAlPacinoPointHistoryVisByAssociatedIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistoryVi = alPacinoPointHistoryViRepository.saveAndFlush(alPacinoPointHistoryVi);

        // Get all the alPacinoPointHistoryViList where associatedId is not null
        defaultAlPacinoPointHistoryViFiltering("associatedId.specified=true", "associatedId.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPacinoPointHistoryVisByAssociatedIdContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistoryVi = alPacinoPointHistoryViRepository.saveAndFlush(alPacinoPointHistoryVi);

        // Get all the alPacinoPointHistoryViList where associatedId contains
        defaultAlPacinoPointHistoryViFiltering(
            "associatedId.contains=" + DEFAULT_ASSOCIATED_ID,
            "associatedId.contains=" + UPDATED_ASSOCIATED_ID
        );
    }

    @Test
    @Transactional
    void getAllAlPacinoPointHistoryVisByAssociatedIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistoryVi = alPacinoPointHistoryViRepository.saveAndFlush(alPacinoPointHistoryVi);

        // Get all the alPacinoPointHistoryViList where associatedId does not contain
        defaultAlPacinoPointHistoryViFiltering(
            "associatedId.doesNotContain=" + UPDATED_ASSOCIATED_ID,
            "associatedId.doesNotContain=" + DEFAULT_ASSOCIATED_ID
        );
    }

    @Test
    @Transactional
    void getAllAlPacinoPointHistoryVisByPointAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistoryVi = alPacinoPointHistoryViRepository.saveAndFlush(alPacinoPointHistoryVi);

        // Get all the alPacinoPointHistoryViList where pointAmount equals to
        defaultAlPacinoPointHistoryViFiltering("pointAmount.equals=" + DEFAULT_POINT_AMOUNT, "pointAmount.equals=" + UPDATED_POINT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAlPacinoPointHistoryVisByPointAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistoryVi = alPacinoPointHistoryViRepository.saveAndFlush(alPacinoPointHistoryVi);

        // Get all the alPacinoPointHistoryViList where pointAmount in
        defaultAlPacinoPointHistoryViFiltering(
            "pointAmount.in=" + DEFAULT_POINT_AMOUNT + "," + UPDATED_POINT_AMOUNT,
            "pointAmount.in=" + UPDATED_POINT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllAlPacinoPointHistoryVisByPointAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistoryVi = alPacinoPointHistoryViRepository.saveAndFlush(alPacinoPointHistoryVi);

        // Get all the alPacinoPointHistoryViList where pointAmount is not null
        defaultAlPacinoPointHistoryViFiltering("pointAmount.specified=true", "pointAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPacinoPointHistoryVisByPointAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistoryVi = alPacinoPointHistoryViRepository.saveAndFlush(alPacinoPointHistoryVi);

        // Get all the alPacinoPointHistoryViList where pointAmount is greater than or equal to
        defaultAlPacinoPointHistoryViFiltering(
            "pointAmount.greaterThanOrEqual=" + DEFAULT_POINT_AMOUNT,
            "pointAmount.greaterThanOrEqual=" + UPDATED_POINT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllAlPacinoPointHistoryVisByPointAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistoryVi = alPacinoPointHistoryViRepository.saveAndFlush(alPacinoPointHistoryVi);

        // Get all the alPacinoPointHistoryViList where pointAmount is less than or equal to
        defaultAlPacinoPointHistoryViFiltering(
            "pointAmount.lessThanOrEqual=" + DEFAULT_POINT_AMOUNT,
            "pointAmount.lessThanOrEqual=" + SMALLER_POINT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllAlPacinoPointHistoryVisByPointAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistoryVi = alPacinoPointHistoryViRepository.saveAndFlush(alPacinoPointHistoryVi);

        // Get all the alPacinoPointHistoryViList where pointAmount is less than
        defaultAlPacinoPointHistoryViFiltering(
            "pointAmount.lessThan=" + UPDATED_POINT_AMOUNT,
            "pointAmount.lessThan=" + DEFAULT_POINT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllAlPacinoPointHistoryVisByPointAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistoryVi = alPacinoPointHistoryViRepository.saveAndFlush(alPacinoPointHistoryVi);

        // Get all the alPacinoPointHistoryViList where pointAmount is greater than
        defaultAlPacinoPointHistoryViFiltering(
            "pointAmount.greaterThan=" + SMALLER_POINT_AMOUNT,
            "pointAmount.greaterThan=" + DEFAULT_POINT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllAlPacinoPointHistoryVisByCustomerIsEqualToSomething() throws Exception {
        AlPacino customer;
        if (TestUtil.findAll(em, AlPacino.class).isEmpty()) {
            alPacinoPointHistoryViRepository.saveAndFlush(alPacinoPointHistoryVi);
            customer = AlPacinoResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, AlPacino.class).get(0);
        }
        em.persist(customer);
        em.flush();
        alPacinoPointHistoryVi.setCustomer(customer);
        alPacinoPointHistoryViRepository.saveAndFlush(alPacinoPointHistoryVi);
        UUID customerId = customer.getId();
        // Get all the alPacinoPointHistoryViList where customer equals to customerId
        defaultAlPacinoPointHistoryViShouldBeFound("customerId.equals=" + customerId);

        // Get all the alPacinoPointHistoryViList where customer equals to UUID.randomUUID()
        defaultAlPacinoPointHistoryViShouldNotBeFound("customerId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlPacinoPointHistoryVisByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alPacinoPointHistoryViRepository.saveAndFlush(alPacinoPointHistoryVi);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alPacinoPointHistoryVi.setApplication(application);
        alPacinoPointHistoryViRepository.saveAndFlush(alPacinoPointHistoryVi);
        UUID applicationId = application.getId();
        // Get all the alPacinoPointHistoryViList where application equals to applicationId
        defaultAlPacinoPointHistoryViShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alPacinoPointHistoryViList where application equals to UUID.randomUUID()
        defaultAlPacinoPointHistoryViShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlPacinoPointHistoryViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlPacinoPointHistoryViShouldBeFound(shouldBeFound);
        defaultAlPacinoPointHistoryViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlPacinoPointHistoryViShouldBeFound(String filter) throws Exception {
        restAlPacinoPointHistoryViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPacinoPointHistoryVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE.toString())))
            .andExpect(jsonPath("$.[*].associatedId").value(hasItem(DEFAULT_ASSOCIATED_ID)))
            .andExpect(jsonPath("$.[*].pointAmount").value(hasItem(DEFAULT_POINT_AMOUNT)));

        // Check, that the count call also returns 1
        restAlPacinoPointHistoryViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlPacinoPointHistoryViShouldNotBeFound(String filter) throws Exception {
        restAlPacinoPointHistoryViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlPacinoPointHistoryViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlPacinoPointHistoryVi() throws Exception {
        // Get the alPacinoPointHistoryVi
        restAlPacinoPointHistoryViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlPacinoPointHistoryVi() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistoryVi = alPacinoPointHistoryViRepository.saveAndFlush(alPacinoPointHistoryVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPacinoPointHistoryVi
        AlPacinoPointHistoryVi updatedAlPacinoPointHistoryVi = alPacinoPointHistoryViRepository
            .findById(alPacinoPointHistoryVi.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedAlPacinoPointHistoryVi are not directly saved in db
        em.detach(updatedAlPacinoPointHistoryVi);
        updatedAlPacinoPointHistoryVi.source(UPDATED_SOURCE).associatedId(UPDATED_ASSOCIATED_ID).pointAmount(UPDATED_POINT_AMOUNT);

        restAlPacinoPointHistoryViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlPacinoPointHistoryVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlPacinoPointHistoryVi))
            )
            .andExpect(status().isOk());

        // Validate the AlPacinoPointHistoryVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlPacinoPointHistoryViToMatchAllProperties(updatedAlPacinoPointHistoryVi);
    }

    @Test
    @Transactional
    void putNonExistingAlPacinoPointHistoryVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacinoPointHistoryVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPacinoPointHistoryViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alPacinoPointHistoryVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPacinoPointHistoryVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPacinoPointHistoryVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlPacinoPointHistoryVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacinoPointHistoryVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPacinoPointHistoryViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPacinoPointHistoryVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPacinoPointHistoryVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlPacinoPointHistoryVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacinoPointHistoryVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPacinoPointHistoryViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPacinoPointHistoryVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPacinoPointHistoryVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlPacinoPointHistoryViWithPatch() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistoryVi = alPacinoPointHistoryViRepository.saveAndFlush(alPacinoPointHistoryVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPacinoPointHistoryVi using partial update
        AlPacinoPointHistoryVi partialUpdatedAlPacinoPointHistoryVi = new AlPacinoPointHistoryVi();
        partialUpdatedAlPacinoPointHistoryVi.setId(alPacinoPointHistoryVi.getId());

        partialUpdatedAlPacinoPointHistoryVi.associatedId(UPDATED_ASSOCIATED_ID).pointAmount(UPDATED_POINT_AMOUNT);

        restAlPacinoPointHistoryViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPacinoPointHistoryVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPacinoPointHistoryVi))
            )
            .andExpect(status().isOk());

        // Validate the AlPacinoPointHistoryVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPacinoPointHistoryViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlPacinoPointHistoryVi, alPacinoPointHistoryVi),
            getPersistedAlPacinoPointHistoryVi(alPacinoPointHistoryVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlPacinoPointHistoryViWithPatch() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistoryVi = alPacinoPointHistoryViRepository.saveAndFlush(alPacinoPointHistoryVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPacinoPointHistoryVi using partial update
        AlPacinoPointHistoryVi partialUpdatedAlPacinoPointHistoryVi = new AlPacinoPointHistoryVi();
        partialUpdatedAlPacinoPointHistoryVi.setId(alPacinoPointHistoryVi.getId());

        partialUpdatedAlPacinoPointHistoryVi.source(UPDATED_SOURCE).associatedId(UPDATED_ASSOCIATED_ID).pointAmount(UPDATED_POINT_AMOUNT);

        restAlPacinoPointHistoryViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPacinoPointHistoryVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPacinoPointHistoryVi))
            )
            .andExpect(status().isOk());

        // Validate the AlPacinoPointHistoryVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPacinoPointHistoryViUpdatableFieldsEquals(
            partialUpdatedAlPacinoPointHistoryVi,
            getPersistedAlPacinoPointHistoryVi(partialUpdatedAlPacinoPointHistoryVi)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAlPacinoPointHistoryVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacinoPointHistoryVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPacinoPointHistoryViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alPacinoPointHistoryVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPacinoPointHistoryVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPacinoPointHistoryVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlPacinoPointHistoryVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacinoPointHistoryVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPacinoPointHistoryViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPacinoPointHistoryVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPacinoPointHistoryVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlPacinoPointHistoryVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacinoPointHistoryVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPacinoPointHistoryViMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alPacinoPointHistoryVi))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPacinoPointHistoryVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlPacinoPointHistoryVi() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistoryVi = alPacinoPointHistoryViRepository.saveAndFlush(alPacinoPointHistoryVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alPacinoPointHistoryVi
        restAlPacinoPointHistoryViMockMvc
            .perform(delete(ENTITY_API_URL_ID, alPacinoPointHistoryVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alPacinoPointHistoryViRepository.count();
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

    protected AlPacinoPointHistoryVi getPersistedAlPacinoPointHistoryVi(AlPacinoPointHistoryVi alPacinoPointHistoryVi) {
        return alPacinoPointHistoryViRepository.findById(alPacinoPointHistoryVi.getId()).orElseThrow();
    }

    protected void assertPersistedAlPacinoPointHistoryViToMatchAllProperties(AlPacinoPointHistoryVi expectedAlPacinoPointHistoryVi) {
        assertAlPacinoPointHistoryViAllPropertiesEquals(
            expectedAlPacinoPointHistoryVi,
            getPersistedAlPacinoPointHistoryVi(expectedAlPacinoPointHistoryVi)
        );
    }

    protected void assertPersistedAlPacinoPointHistoryViToMatchUpdatableProperties(AlPacinoPointHistoryVi expectedAlPacinoPointHistoryVi) {
        assertAlPacinoPointHistoryViAllUpdatablePropertiesEquals(
            expectedAlPacinoPointHistoryVi,
            getPersistedAlPacinoPointHistoryVi(expectedAlPacinoPointHistoryVi)
        );
    }
}
