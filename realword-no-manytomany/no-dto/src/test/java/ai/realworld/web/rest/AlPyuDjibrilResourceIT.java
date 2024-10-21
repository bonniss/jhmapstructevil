package ai.realworld.web.rest;

import static ai.realworld.domain.AlPyuDjibrilAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static ai.realworld.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlProty;
import ai.realworld.domain.AlPyuDjibril;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.enumeration.BenedictRiottaType;
import ai.realworld.repository.AlPyuDjibrilRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link AlPyuDjibrilResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlPyuDjibrilResourceIT {

    private static final BenedictRiottaType DEFAULT_RATE_TYPE = BenedictRiottaType.NIGHT_WEEKDAY;
    private static final BenedictRiottaType UPDATED_RATE_TYPE = BenedictRiottaType.NIGHT_WEEKEND;

    private static final BigDecimal DEFAULT_RATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_RATE = new BigDecimal(2);
    private static final BigDecimal SMALLER_RATE = new BigDecimal(1 - 1);

    private static final Boolean DEFAULT_IS_ENABLED = false;
    private static final Boolean UPDATED_IS_ENABLED = true;

    private static final String ENTITY_API_URL = "/api/al-pyu-djibrils";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlPyuDjibrilRepository alPyuDjibrilRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlPyuDjibrilMockMvc;

    private AlPyuDjibril alPyuDjibril;

    private AlPyuDjibril insertedAlPyuDjibril;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPyuDjibril createEntity() {
        return new AlPyuDjibril().rateType(DEFAULT_RATE_TYPE).rate(DEFAULT_RATE).isEnabled(DEFAULT_IS_ENABLED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPyuDjibril createUpdatedEntity() {
        return new AlPyuDjibril().rateType(UPDATED_RATE_TYPE).rate(UPDATED_RATE).isEnabled(UPDATED_IS_ENABLED);
    }

    @BeforeEach
    public void initTest() {
        alPyuDjibril = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlPyuDjibril != null) {
            alPyuDjibrilRepository.delete(insertedAlPyuDjibril);
            insertedAlPyuDjibril = null;
        }
    }

    @Test
    @Transactional
    void createAlPyuDjibril() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlPyuDjibril
        var returnedAlPyuDjibril = om.readValue(
            restAlPyuDjibrilMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPyuDjibril)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlPyuDjibril.class
        );

        // Validate the AlPyuDjibril in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlPyuDjibrilUpdatableFieldsEquals(returnedAlPyuDjibril, getPersistedAlPyuDjibril(returnedAlPyuDjibril));

        insertedAlPyuDjibril = returnedAlPyuDjibril;
    }

    @Test
    @Transactional
    void createAlPyuDjibrilWithExistingId() throws Exception {
        // Create the AlPyuDjibril with an existing ID
        alPyuDjibril.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlPyuDjibrilMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPyuDjibril)))
            .andExpect(status().isBadRequest());

        // Validate the AlPyuDjibril in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAlPyuDjibrils() throws Exception {
        // Initialize the database
        insertedAlPyuDjibril = alPyuDjibrilRepository.saveAndFlush(alPyuDjibril);

        // Get all the alPyuDjibrilList
        restAlPyuDjibrilMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPyuDjibril.getId().intValue())))
            .andExpect(jsonPath("$.[*].rateType").value(hasItem(DEFAULT_RATE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(sameNumber(DEFAULT_RATE))))
            .andExpect(jsonPath("$.[*].isEnabled").value(hasItem(DEFAULT_IS_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    void getAlPyuDjibril() throws Exception {
        // Initialize the database
        insertedAlPyuDjibril = alPyuDjibrilRepository.saveAndFlush(alPyuDjibril);

        // Get the alPyuDjibril
        restAlPyuDjibrilMockMvc
            .perform(get(ENTITY_API_URL_ID, alPyuDjibril.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alPyuDjibril.getId().intValue()))
            .andExpect(jsonPath("$.rateType").value(DEFAULT_RATE_TYPE.toString()))
            .andExpect(jsonPath("$.rate").value(sameNumber(DEFAULT_RATE)))
            .andExpect(jsonPath("$.isEnabled").value(DEFAULT_IS_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    void getAlPyuDjibrilsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlPyuDjibril = alPyuDjibrilRepository.saveAndFlush(alPyuDjibril);

        Long id = alPyuDjibril.getId();

        defaultAlPyuDjibrilFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlPyuDjibrilFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlPyuDjibrilFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlPyuDjibrilsByRateTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuDjibril = alPyuDjibrilRepository.saveAndFlush(alPyuDjibril);

        // Get all the alPyuDjibrilList where rateType equals to
        defaultAlPyuDjibrilFiltering("rateType.equals=" + DEFAULT_RATE_TYPE, "rateType.equals=" + UPDATED_RATE_TYPE);
    }

    @Test
    @Transactional
    void getAllAlPyuDjibrilsByRateTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuDjibril = alPyuDjibrilRepository.saveAndFlush(alPyuDjibril);

        // Get all the alPyuDjibrilList where rateType in
        defaultAlPyuDjibrilFiltering("rateType.in=" + DEFAULT_RATE_TYPE + "," + UPDATED_RATE_TYPE, "rateType.in=" + UPDATED_RATE_TYPE);
    }

    @Test
    @Transactional
    void getAllAlPyuDjibrilsByRateTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuDjibril = alPyuDjibrilRepository.saveAndFlush(alPyuDjibril);

        // Get all the alPyuDjibrilList where rateType is not null
        defaultAlPyuDjibrilFiltering("rateType.specified=true", "rateType.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuDjibrilsByRateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuDjibril = alPyuDjibrilRepository.saveAndFlush(alPyuDjibril);

        // Get all the alPyuDjibrilList where rate equals to
        defaultAlPyuDjibrilFiltering("rate.equals=" + DEFAULT_RATE, "rate.equals=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    void getAllAlPyuDjibrilsByRateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuDjibril = alPyuDjibrilRepository.saveAndFlush(alPyuDjibril);

        // Get all the alPyuDjibrilList where rate in
        defaultAlPyuDjibrilFiltering("rate.in=" + DEFAULT_RATE + "," + UPDATED_RATE, "rate.in=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    void getAllAlPyuDjibrilsByRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuDjibril = alPyuDjibrilRepository.saveAndFlush(alPyuDjibril);

        // Get all the alPyuDjibrilList where rate is not null
        defaultAlPyuDjibrilFiltering("rate.specified=true", "rate.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuDjibrilsByRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuDjibril = alPyuDjibrilRepository.saveAndFlush(alPyuDjibril);

        // Get all the alPyuDjibrilList where rate is greater than or equal to
        defaultAlPyuDjibrilFiltering("rate.greaterThanOrEqual=" + DEFAULT_RATE, "rate.greaterThanOrEqual=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    void getAllAlPyuDjibrilsByRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuDjibril = alPyuDjibrilRepository.saveAndFlush(alPyuDjibril);

        // Get all the alPyuDjibrilList where rate is less than or equal to
        defaultAlPyuDjibrilFiltering("rate.lessThanOrEqual=" + DEFAULT_RATE, "rate.lessThanOrEqual=" + SMALLER_RATE);
    }

    @Test
    @Transactional
    void getAllAlPyuDjibrilsByRateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlPyuDjibril = alPyuDjibrilRepository.saveAndFlush(alPyuDjibril);

        // Get all the alPyuDjibrilList where rate is less than
        defaultAlPyuDjibrilFiltering("rate.lessThan=" + UPDATED_RATE, "rate.lessThan=" + DEFAULT_RATE);
    }

    @Test
    @Transactional
    void getAllAlPyuDjibrilsByRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlPyuDjibril = alPyuDjibrilRepository.saveAndFlush(alPyuDjibril);

        // Get all the alPyuDjibrilList where rate is greater than
        defaultAlPyuDjibrilFiltering("rate.greaterThan=" + SMALLER_RATE, "rate.greaterThan=" + DEFAULT_RATE);
    }

    @Test
    @Transactional
    void getAllAlPyuDjibrilsByIsEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuDjibril = alPyuDjibrilRepository.saveAndFlush(alPyuDjibril);

        // Get all the alPyuDjibrilList where isEnabled equals to
        defaultAlPyuDjibrilFiltering("isEnabled.equals=" + DEFAULT_IS_ENABLED, "isEnabled.equals=" + UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    void getAllAlPyuDjibrilsByIsEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuDjibril = alPyuDjibrilRepository.saveAndFlush(alPyuDjibril);

        // Get all the alPyuDjibrilList where isEnabled in
        defaultAlPyuDjibrilFiltering("isEnabled.in=" + DEFAULT_IS_ENABLED + "," + UPDATED_IS_ENABLED, "isEnabled.in=" + UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    void getAllAlPyuDjibrilsByIsEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuDjibril = alPyuDjibrilRepository.saveAndFlush(alPyuDjibril);

        // Get all the alPyuDjibrilList where isEnabled is not null
        defaultAlPyuDjibrilFiltering("isEnabled.specified=true", "isEnabled.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuDjibrilsByPropertyIsEqualToSomething() throws Exception {
        AlProty property;
        if (TestUtil.findAll(em, AlProty.class).isEmpty()) {
            alPyuDjibrilRepository.saveAndFlush(alPyuDjibril);
            property = AlProtyResourceIT.createEntity(em);
        } else {
            property = TestUtil.findAll(em, AlProty.class).get(0);
        }
        em.persist(property);
        em.flush();
        alPyuDjibril.setProperty(property);
        alPyuDjibrilRepository.saveAndFlush(alPyuDjibril);
        UUID propertyId = property.getId();
        // Get all the alPyuDjibrilList where property equals to propertyId
        defaultAlPyuDjibrilShouldBeFound("propertyId.equals=" + propertyId);

        // Get all the alPyuDjibrilList where property equals to UUID.randomUUID()
        defaultAlPyuDjibrilShouldNotBeFound("propertyId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlPyuDjibrilsByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alPyuDjibrilRepository.saveAndFlush(alPyuDjibril);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alPyuDjibril.setApplication(application);
        alPyuDjibrilRepository.saveAndFlush(alPyuDjibril);
        UUID applicationId = application.getId();
        // Get all the alPyuDjibrilList where application equals to applicationId
        defaultAlPyuDjibrilShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alPyuDjibrilList where application equals to UUID.randomUUID()
        defaultAlPyuDjibrilShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlPyuDjibrilFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlPyuDjibrilShouldBeFound(shouldBeFound);
        defaultAlPyuDjibrilShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlPyuDjibrilShouldBeFound(String filter) throws Exception {
        restAlPyuDjibrilMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPyuDjibril.getId().intValue())))
            .andExpect(jsonPath("$.[*].rateType").value(hasItem(DEFAULT_RATE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(sameNumber(DEFAULT_RATE))))
            .andExpect(jsonPath("$.[*].isEnabled").value(hasItem(DEFAULT_IS_ENABLED.booleanValue())));

        // Check, that the count call also returns 1
        restAlPyuDjibrilMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlPyuDjibrilShouldNotBeFound(String filter) throws Exception {
        restAlPyuDjibrilMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlPyuDjibrilMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlPyuDjibril() throws Exception {
        // Get the alPyuDjibril
        restAlPyuDjibrilMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlPyuDjibril() throws Exception {
        // Initialize the database
        insertedAlPyuDjibril = alPyuDjibrilRepository.saveAndFlush(alPyuDjibril);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPyuDjibril
        AlPyuDjibril updatedAlPyuDjibril = alPyuDjibrilRepository.findById(alPyuDjibril.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlPyuDjibril are not directly saved in db
        em.detach(updatedAlPyuDjibril);
        updatedAlPyuDjibril.rateType(UPDATED_RATE_TYPE).rate(UPDATED_RATE).isEnabled(UPDATED_IS_ENABLED);

        restAlPyuDjibrilMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlPyuDjibril.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlPyuDjibril))
            )
            .andExpect(status().isOk());

        // Validate the AlPyuDjibril in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlPyuDjibrilToMatchAllProperties(updatedAlPyuDjibril);
    }

    @Test
    @Transactional
    void putNonExistingAlPyuDjibril() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPyuDjibril.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPyuDjibrilMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alPyuDjibril.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPyuDjibril))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPyuDjibril in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlPyuDjibril() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPyuDjibril.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPyuDjibrilMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPyuDjibril))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPyuDjibril in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlPyuDjibril() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPyuDjibril.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPyuDjibrilMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPyuDjibril)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPyuDjibril in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlPyuDjibrilWithPatch() throws Exception {
        // Initialize the database
        insertedAlPyuDjibril = alPyuDjibrilRepository.saveAndFlush(alPyuDjibril);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPyuDjibril using partial update
        AlPyuDjibril partialUpdatedAlPyuDjibril = new AlPyuDjibril();
        partialUpdatedAlPyuDjibril.setId(alPyuDjibril.getId());

        restAlPyuDjibrilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPyuDjibril.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPyuDjibril))
            )
            .andExpect(status().isOk());

        // Validate the AlPyuDjibril in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPyuDjibrilUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlPyuDjibril, alPyuDjibril),
            getPersistedAlPyuDjibril(alPyuDjibril)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlPyuDjibrilWithPatch() throws Exception {
        // Initialize the database
        insertedAlPyuDjibril = alPyuDjibrilRepository.saveAndFlush(alPyuDjibril);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPyuDjibril using partial update
        AlPyuDjibril partialUpdatedAlPyuDjibril = new AlPyuDjibril();
        partialUpdatedAlPyuDjibril.setId(alPyuDjibril.getId());

        partialUpdatedAlPyuDjibril.rateType(UPDATED_RATE_TYPE).rate(UPDATED_RATE).isEnabled(UPDATED_IS_ENABLED);

        restAlPyuDjibrilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPyuDjibril.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPyuDjibril))
            )
            .andExpect(status().isOk());

        // Validate the AlPyuDjibril in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPyuDjibrilUpdatableFieldsEquals(partialUpdatedAlPyuDjibril, getPersistedAlPyuDjibril(partialUpdatedAlPyuDjibril));
    }

    @Test
    @Transactional
    void patchNonExistingAlPyuDjibril() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPyuDjibril.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPyuDjibrilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alPyuDjibril.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPyuDjibril))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPyuDjibril in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlPyuDjibril() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPyuDjibril.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPyuDjibrilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPyuDjibril))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPyuDjibril in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlPyuDjibril() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPyuDjibril.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPyuDjibrilMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alPyuDjibril)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPyuDjibril in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlPyuDjibril() throws Exception {
        // Initialize the database
        insertedAlPyuDjibril = alPyuDjibrilRepository.saveAndFlush(alPyuDjibril);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alPyuDjibril
        restAlPyuDjibrilMockMvc
            .perform(delete(ENTITY_API_URL_ID, alPyuDjibril.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alPyuDjibrilRepository.count();
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

    protected AlPyuDjibril getPersistedAlPyuDjibril(AlPyuDjibril alPyuDjibril) {
        return alPyuDjibrilRepository.findById(alPyuDjibril.getId()).orElseThrow();
    }

    protected void assertPersistedAlPyuDjibrilToMatchAllProperties(AlPyuDjibril expectedAlPyuDjibril) {
        assertAlPyuDjibrilAllPropertiesEquals(expectedAlPyuDjibril, getPersistedAlPyuDjibril(expectedAlPyuDjibril));
    }

    protected void assertPersistedAlPyuDjibrilToMatchUpdatableProperties(AlPyuDjibril expectedAlPyuDjibril) {
        assertAlPyuDjibrilAllUpdatablePropertiesEquals(expectedAlPyuDjibril, getPersistedAlPyuDjibril(expectedAlPyuDjibril));
    }
}
