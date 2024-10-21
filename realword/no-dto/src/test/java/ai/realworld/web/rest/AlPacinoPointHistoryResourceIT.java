package ai.realworld.web.rest;

import static ai.realworld.domain.AlPacinoPointHistoryAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlPacino;
import ai.realworld.domain.AlPacinoPointHistory;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.enumeration.EeriePointSource;
import ai.realworld.repository.AlPacinoPointHistoryRepository;
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
 * Integration tests for the {@link AlPacinoPointHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlPacinoPointHistoryResourceIT {

    private static final EeriePointSource DEFAULT_SOURCE = EeriePointSource.PURCHASE;
    private static final EeriePointSource UPDATED_SOURCE = EeriePointSource.MINIGAME;

    private static final String DEFAULT_ASSOCIATED_ID = "AAAAAAAAAA";
    private static final String UPDATED_ASSOCIATED_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_POINT_AMOUNT = 1;
    private static final Integer UPDATED_POINT_AMOUNT = 2;
    private static final Integer SMALLER_POINT_AMOUNT = 1 - 1;

    private static final String ENTITY_API_URL = "/api/al-pacino-point-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlPacinoPointHistoryRepository alPacinoPointHistoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlPacinoPointHistoryMockMvc;

    private AlPacinoPointHistory alPacinoPointHistory;

    private AlPacinoPointHistory insertedAlPacinoPointHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPacinoPointHistory createEntity() {
        return new AlPacinoPointHistory().source(DEFAULT_SOURCE).associatedId(DEFAULT_ASSOCIATED_ID).pointAmount(DEFAULT_POINT_AMOUNT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPacinoPointHistory createUpdatedEntity() {
        return new AlPacinoPointHistory().source(UPDATED_SOURCE).associatedId(UPDATED_ASSOCIATED_ID).pointAmount(UPDATED_POINT_AMOUNT);
    }

    @BeforeEach
    public void initTest() {
        alPacinoPointHistory = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlPacinoPointHistory != null) {
            alPacinoPointHistoryRepository.delete(insertedAlPacinoPointHistory);
            insertedAlPacinoPointHistory = null;
        }
    }

    @Test
    @Transactional
    void createAlPacinoPointHistory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlPacinoPointHistory
        var returnedAlPacinoPointHistory = om.readValue(
            restAlPacinoPointHistoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPacinoPointHistory)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlPacinoPointHistory.class
        );

        // Validate the AlPacinoPointHistory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlPacinoPointHistoryUpdatableFieldsEquals(
            returnedAlPacinoPointHistory,
            getPersistedAlPacinoPointHistory(returnedAlPacinoPointHistory)
        );

        insertedAlPacinoPointHistory = returnedAlPacinoPointHistory;
    }

    @Test
    @Transactional
    void createAlPacinoPointHistoryWithExistingId() throws Exception {
        // Create the AlPacinoPointHistory with an existing ID
        alPacinoPointHistory.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlPacinoPointHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPacinoPointHistory)))
            .andExpect(status().isBadRequest());

        // Validate the AlPacinoPointHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAlPacinoPointHistories() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistory = alPacinoPointHistoryRepository.saveAndFlush(alPacinoPointHistory);

        // Get all the alPacinoPointHistoryList
        restAlPacinoPointHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPacinoPointHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE.toString())))
            .andExpect(jsonPath("$.[*].associatedId").value(hasItem(DEFAULT_ASSOCIATED_ID)))
            .andExpect(jsonPath("$.[*].pointAmount").value(hasItem(DEFAULT_POINT_AMOUNT)));
    }

    @Test
    @Transactional
    void getAlPacinoPointHistory() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistory = alPacinoPointHistoryRepository.saveAndFlush(alPacinoPointHistory);

        // Get the alPacinoPointHistory
        restAlPacinoPointHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, alPacinoPointHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alPacinoPointHistory.getId().intValue()))
            .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE.toString()))
            .andExpect(jsonPath("$.associatedId").value(DEFAULT_ASSOCIATED_ID))
            .andExpect(jsonPath("$.pointAmount").value(DEFAULT_POINT_AMOUNT));
    }

    @Test
    @Transactional
    void getAlPacinoPointHistoriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistory = alPacinoPointHistoryRepository.saveAndFlush(alPacinoPointHistory);

        Long id = alPacinoPointHistory.getId();

        defaultAlPacinoPointHistoryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlPacinoPointHistoryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlPacinoPointHistoryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlPacinoPointHistoriesBySourceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistory = alPacinoPointHistoryRepository.saveAndFlush(alPacinoPointHistory);

        // Get all the alPacinoPointHistoryList where source equals to
        defaultAlPacinoPointHistoryFiltering("source.equals=" + DEFAULT_SOURCE, "source.equals=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void getAllAlPacinoPointHistoriesBySourceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistory = alPacinoPointHistoryRepository.saveAndFlush(alPacinoPointHistory);

        // Get all the alPacinoPointHistoryList where source in
        defaultAlPacinoPointHistoryFiltering("source.in=" + DEFAULT_SOURCE + "," + UPDATED_SOURCE, "source.in=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void getAllAlPacinoPointHistoriesBySourceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistory = alPacinoPointHistoryRepository.saveAndFlush(alPacinoPointHistory);

        // Get all the alPacinoPointHistoryList where source is not null
        defaultAlPacinoPointHistoryFiltering("source.specified=true", "source.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPacinoPointHistoriesByAssociatedIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistory = alPacinoPointHistoryRepository.saveAndFlush(alPacinoPointHistory);

        // Get all the alPacinoPointHistoryList where associatedId equals to
        defaultAlPacinoPointHistoryFiltering(
            "associatedId.equals=" + DEFAULT_ASSOCIATED_ID,
            "associatedId.equals=" + UPDATED_ASSOCIATED_ID
        );
    }

    @Test
    @Transactional
    void getAllAlPacinoPointHistoriesByAssociatedIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistory = alPacinoPointHistoryRepository.saveAndFlush(alPacinoPointHistory);

        // Get all the alPacinoPointHistoryList where associatedId in
        defaultAlPacinoPointHistoryFiltering(
            "associatedId.in=" + DEFAULT_ASSOCIATED_ID + "," + UPDATED_ASSOCIATED_ID,
            "associatedId.in=" + UPDATED_ASSOCIATED_ID
        );
    }

    @Test
    @Transactional
    void getAllAlPacinoPointHistoriesByAssociatedIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistory = alPacinoPointHistoryRepository.saveAndFlush(alPacinoPointHistory);

        // Get all the alPacinoPointHistoryList where associatedId is not null
        defaultAlPacinoPointHistoryFiltering("associatedId.specified=true", "associatedId.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPacinoPointHistoriesByAssociatedIdContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistory = alPacinoPointHistoryRepository.saveAndFlush(alPacinoPointHistory);

        // Get all the alPacinoPointHistoryList where associatedId contains
        defaultAlPacinoPointHistoryFiltering(
            "associatedId.contains=" + DEFAULT_ASSOCIATED_ID,
            "associatedId.contains=" + UPDATED_ASSOCIATED_ID
        );
    }

    @Test
    @Transactional
    void getAllAlPacinoPointHistoriesByAssociatedIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistory = alPacinoPointHistoryRepository.saveAndFlush(alPacinoPointHistory);

        // Get all the alPacinoPointHistoryList where associatedId does not contain
        defaultAlPacinoPointHistoryFiltering(
            "associatedId.doesNotContain=" + UPDATED_ASSOCIATED_ID,
            "associatedId.doesNotContain=" + DEFAULT_ASSOCIATED_ID
        );
    }

    @Test
    @Transactional
    void getAllAlPacinoPointHistoriesByPointAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistory = alPacinoPointHistoryRepository.saveAndFlush(alPacinoPointHistory);

        // Get all the alPacinoPointHistoryList where pointAmount equals to
        defaultAlPacinoPointHistoryFiltering("pointAmount.equals=" + DEFAULT_POINT_AMOUNT, "pointAmount.equals=" + UPDATED_POINT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAlPacinoPointHistoriesByPointAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistory = alPacinoPointHistoryRepository.saveAndFlush(alPacinoPointHistory);

        // Get all the alPacinoPointHistoryList where pointAmount in
        defaultAlPacinoPointHistoryFiltering(
            "pointAmount.in=" + DEFAULT_POINT_AMOUNT + "," + UPDATED_POINT_AMOUNT,
            "pointAmount.in=" + UPDATED_POINT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllAlPacinoPointHistoriesByPointAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistory = alPacinoPointHistoryRepository.saveAndFlush(alPacinoPointHistory);

        // Get all the alPacinoPointHistoryList where pointAmount is not null
        defaultAlPacinoPointHistoryFiltering("pointAmount.specified=true", "pointAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPacinoPointHistoriesByPointAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistory = alPacinoPointHistoryRepository.saveAndFlush(alPacinoPointHistory);

        // Get all the alPacinoPointHistoryList where pointAmount is greater than or equal to
        defaultAlPacinoPointHistoryFiltering(
            "pointAmount.greaterThanOrEqual=" + DEFAULT_POINT_AMOUNT,
            "pointAmount.greaterThanOrEqual=" + UPDATED_POINT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllAlPacinoPointHistoriesByPointAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistory = alPacinoPointHistoryRepository.saveAndFlush(alPacinoPointHistory);

        // Get all the alPacinoPointHistoryList where pointAmount is less than or equal to
        defaultAlPacinoPointHistoryFiltering(
            "pointAmount.lessThanOrEqual=" + DEFAULT_POINT_AMOUNT,
            "pointAmount.lessThanOrEqual=" + SMALLER_POINT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllAlPacinoPointHistoriesByPointAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistory = alPacinoPointHistoryRepository.saveAndFlush(alPacinoPointHistory);

        // Get all the alPacinoPointHistoryList where pointAmount is less than
        defaultAlPacinoPointHistoryFiltering(
            "pointAmount.lessThan=" + UPDATED_POINT_AMOUNT,
            "pointAmount.lessThan=" + DEFAULT_POINT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllAlPacinoPointHistoriesByPointAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistory = alPacinoPointHistoryRepository.saveAndFlush(alPacinoPointHistory);

        // Get all the alPacinoPointHistoryList where pointAmount is greater than
        defaultAlPacinoPointHistoryFiltering(
            "pointAmount.greaterThan=" + SMALLER_POINT_AMOUNT,
            "pointAmount.greaterThan=" + DEFAULT_POINT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllAlPacinoPointHistoriesByCustomerIsEqualToSomething() throws Exception {
        AlPacino customer;
        if (TestUtil.findAll(em, AlPacino.class).isEmpty()) {
            alPacinoPointHistoryRepository.saveAndFlush(alPacinoPointHistory);
            customer = AlPacinoResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, AlPacino.class).get(0);
        }
        em.persist(customer);
        em.flush();
        alPacinoPointHistory.setCustomer(customer);
        alPacinoPointHistoryRepository.saveAndFlush(alPacinoPointHistory);
        UUID customerId = customer.getId();
        // Get all the alPacinoPointHistoryList where customer equals to customerId
        defaultAlPacinoPointHistoryShouldBeFound("customerId.equals=" + customerId);

        // Get all the alPacinoPointHistoryList where customer equals to UUID.randomUUID()
        defaultAlPacinoPointHistoryShouldNotBeFound("customerId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlPacinoPointHistoriesByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alPacinoPointHistoryRepository.saveAndFlush(alPacinoPointHistory);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alPacinoPointHistory.setApplication(application);
        alPacinoPointHistoryRepository.saveAndFlush(alPacinoPointHistory);
        UUID applicationId = application.getId();
        // Get all the alPacinoPointHistoryList where application equals to applicationId
        defaultAlPacinoPointHistoryShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alPacinoPointHistoryList where application equals to UUID.randomUUID()
        defaultAlPacinoPointHistoryShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlPacinoPointHistoryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlPacinoPointHistoryShouldBeFound(shouldBeFound);
        defaultAlPacinoPointHistoryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlPacinoPointHistoryShouldBeFound(String filter) throws Exception {
        restAlPacinoPointHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPacinoPointHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE.toString())))
            .andExpect(jsonPath("$.[*].associatedId").value(hasItem(DEFAULT_ASSOCIATED_ID)))
            .andExpect(jsonPath("$.[*].pointAmount").value(hasItem(DEFAULT_POINT_AMOUNT)));

        // Check, that the count call also returns 1
        restAlPacinoPointHistoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlPacinoPointHistoryShouldNotBeFound(String filter) throws Exception {
        restAlPacinoPointHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlPacinoPointHistoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlPacinoPointHistory() throws Exception {
        // Get the alPacinoPointHistory
        restAlPacinoPointHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlPacinoPointHistory() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistory = alPacinoPointHistoryRepository.saveAndFlush(alPacinoPointHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPacinoPointHistory
        AlPacinoPointHistory updatedAlPacinoPointHistory = alPacinoPointHistoryRepository
            .findById(alPacinoPointHistory.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedAlPacinoPointHistory are not directly saved in db
        em.detach(updatedAlPacinoPointHistory);
        updatedAlPacinoPointHistory.source(UPDATED_SOURCE).associatedId(UPDATED_ASSOCIATED_ID).pointAmount(UPDATED_POINT_AMOUNT);

        restAlPacinoPointHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlPacinoPointHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlPacinoPointHistory))
            )
            .andExpect(status().isOk());

        // Validate the AlPacinoPointHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlPacinoPointHistoryToMatchAllProperties(updatedAlPacinoPointHistory);
    }

    @Test
    @Transactional
    void putNonExistingAlPacinoPointHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacinoPointHistory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPacinoPointHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alPacinoPointHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPacinoPointHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPacinoPointHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlPacinoPointHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacinoPointHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPacinoPointHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPacinoPointHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPacinoPointHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlPacinoPointHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacinoPointHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPacinoPointHistoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPacinoPointHistory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPacinoPointHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlPacinoPointHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistory = alPacinoPointHistoryRepository.saveAndFlush(alPacinoPointHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPacinoPointHistory using partial update
        AlPacinoPointHistory partialUpdatedAlPacinoPointHistory = new AlPacinoPointHistory();
        partialUpdatedAlPacinoPointHistory.setId(alPacinoPointHistory.getId());

        partialUpdatedAlPacinoPointHistory.associatedId(UPDATED_ASSOCIATED_ID).pointAmount(UPDATED_POINT_AMOUNT);

        restAlPacinoPointHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPacinoPointHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPacinoPointHistory))
            )
            .andExpect(status().isOk());

        // Validate the AlPacinoPointHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPacinoPointHistoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlPacinoPointHistory, alPacinoPointHistory),
            getPersistedAlPacinoPointHistory(alPacinoPointHistory)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlPacinoPointHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistory = alPacinoPointHistoryRepository.saveAndFlush(alPacinoPointHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPacinoPointHistory using partial update
        AlPacinoPointHistory partialUpdatedAlPacinoPointHistory = new AlPacinoPointHistory();
        partialUpdatedAlPacinoPointHistory.setId(alPacinoPointHistory.getId());

        partialUpdatedAlPacinoPointHistory.source(UPDATED_SOURCE).associatedId(UPDATED_ASSOCIATED_ID).pointAmount(UPDATED_POINT_AMOUNT);

        restAlPacinoPointHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPacinoPointHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPacinoPointHistory))
            )
            .andExpect(status().isOk());

        // Validate the AlPacinoPointHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPacinoPointHistoryUpdatableFieldsEquals(
            partialUpdatedAlPacinoPointHistory,
            getPersistedAlPacinoPointHistory(partialUpdatedAlPacinoPointHistory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAlPacinoPointHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacinoPointHistory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPacinoPointHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alPacinoPointHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPacinoPointHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPacinoPointHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlPacinoPointHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacinoPointHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPacinoPointHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPacinoPointHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPacinoPointHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlPacinoPointHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacinoPointHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPacinoPointHistoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alPacinoPointHistory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPacinoPointHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlPacinoPointHistory() throws Exception {
        // Initialize the database
        insertedAlPacinoPointHistory = alPacinoPointHistoryRepository.saveAndFlush(alPacinoPointHistory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alPacinoPointHistory
        restAlPacinoPointHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, alPacinoPointHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alPacinoPointHistoryRepository.count();
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

    protected AlPacinoPointHistory getPersistedAlPacinoPointHistory(AlPacinoPointHistory alPacinoPointHistory) {
        return alPacinoPointHistoryRepository.findById(alPacinoPointHistory.getId()).orElseThrow();
    }

    protected void assertPersistedAlPacinoPointHistoryToMatchAllProperties(AlPacinoPointHistory expectedAlPacinoPointHistory) {
        assertAlPacinoPointHistoryAllPropertiesEquals(
            expectedAlPacinoPointHistory,
            getPersistedAlPacinoPointHistory(expectedAlPacinoPointHistory)
        );
    }

    protected void assertPersistedAlPacinoPointHistoryToMatchUpdatableProperties(AlPacinoPointHistory expectedAlPacinoPointHistory) {
        assertAlPacinoPointHistoryAllUpdatablePropertiesEquals(
            expectedAlPacinoPointHistory,
            getPersistedAlPacinoPointHistory(expectedAlPacinoPointHistory)
        );
    }
}
