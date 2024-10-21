package ai.realworld.web.rest;

import static ai.realworld.domain.AlPyuDjibrilViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static ai.realworld.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlProtyVi;
import ai.realworld.domain.AlPyuDjibrilVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.enumeration.BenedictRiottaType;
import ai.realworld.repository.AlPyuDjibrilViRepository;
import ai.realworld.service.dto.AlPyuDjibrilViDTO;
import ai.realworld.service.mapper.AlPyuDjibrilViMapper;
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
 * Integration tests for the {@link AlPyuDjibrilViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlPyuDjibrilViResourceIT {

    private static final BenedictRiottaType DEFAULT_RATE_TYPE = BenedictRiottaType.NIGHT_WEEKDAY;
    private static final BenedictRiottaType UPDATED_RATE_TYPE = BenedictRiottaType.NIGHT_WEEKEND;

    private static final BigDecimal DEFAULT_RATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_RATE = new BigDecimal(2);
    private static final BigDecimal SMALLER_RATE = new BigDecimal(1 - 1);

    private static final Boolean DEFAULT_IS_ENABLED = false;
    private static final Boolean UPDATED_IS_ENABLED = true;

    private static final String ENTITY_API_URL = "/api/al-pyu-djibril-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlPyuDjibrilViRepository alPyuDjibrilViRepository;

    @Autowired
    private AlPyuDjibrilViMapper alPyuDjibrilViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlPyuDjibrilViMockMvc;

    private AlPyuDjibrilVi alPyuDjibrilVi;

    private AlPyuDjibrilVi insertedAlPyuDjibrilVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPyuDjibrilVi createEntity() {
        return new AlPyuDjibrilVi().rateType(DEFAULT_RATE_TYPE).rate(DEFAULT_RATE).isEnabled(DEFAULT_IS_ENABLED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPyuDjibrilVi createUpdatedEntity() {
        return new AlPyuDjibrilVi().rateType(UPDATED_RATE_TYPE).rate(UPDATED_RATE).isEnabled(UPDATED_IS_ENABLED);
    }

    @BeforeEach
    public void initTest() {
        alPyuDjibrilVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlPyuDjibrilVi != null) {
            alPyuDjibrilViRepository.delete(insertedAlPyuDjibrilVi);
            insertedAlPyuDjibrilVi = null;
        }
    }

    @Test
    @Transactional
    void createAlPyuDjibrilVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlPyuDjibrilVi
        AlPyuDjibrilViDTO alPyuDjibrilViDTO = alPyuDjibrilViMapper.toDto(alPyuDjibrilVi);
        var returnedAlPyuDjibrilViDTO = om.readValue(
            restAlPyuDjibrilViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPyuDjibrilViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlPyuDjibrilViDTO.class
        );

        // Validate the AlPyuDjibrilVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlPyuDjibrilVi = alPyuDjibrilViMapper.toEntity(returnedAlPyuDjibrilViDTO);
        assertAlPyuDjibrilViUpdatableFieldsEquals(returnedAlPyuDjibrilVi, getPersistedAlPyuDjibrilVi(returnedAlPyuDjibrilVi));

        insertedAlPyuDjibrilVi = returnedAlPyuDjibrilVi;
    }

    @Test
    @Transactional
    void createAlPyuDjibrilViWithExistingId() throws Exception {
        // Create the AlPyuDjibrilVi with an existing ID
        alPyuDjibrilVi.setId(1L);
        AlPyuDjibrilViDTO alPyuDjibrilViDTO = alPyuDjibrilViMapper.toDto(alPyuDjibrilVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlPyuDjibrilViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPyuDjibrilViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlPyuDjibrilVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAlPyuDjibrilVis() throws Exception {
        // Initialize the database
        insertedAlPyuDjibrilVi = alPyuDjibrilViRepository.saveAndFlush(alPyuDjibrilVi);

        // Get all the alPyuDjibrilViList
        restAlPyuDjibrilViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPyuDjibrilVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].rateType").value(hasItem(DEFAULT_RATE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(sameNumber(DEFAULT_RATE))))
            .andExpect(jsonPath("$.[*].isEnabled").value(hasItem(DEFAULT_IS_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    void getAlPyuDjibrilVi() throws Exception {
        // Initialize the database
        insertedAlPyuDjibrilVi = alPyuDjibrilViRepository.saveAndFlush(alPyuDjibrilVi);

        // Get the alPyuDjibrilVi
        restAlPyuDjibrilViMockMvc
            .perform(get(ENTITY_API_URL_ID, alPyuDjibrilVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alPyuDjibrilVi.getId().intValue()))
            .andExpect(jsonPath("$.rateType").value(DEFAULT_RATE_TYPE.toString()))
            .andExpect(jsonPath("$.rate").value(sameNumber(DEFAULT_RATE)))
            .andExpect(jsonPath("$.isEnabled").value(DEFAULT_IS_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    void getAlPyuDjibrilVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlPyuDjibrilVi = alPyuDjibrilViRepository.saveAndFlush(alPyuDjibrilVi);

        Long id = alPyuDjibrilVi.getId();

        defaultAlPyuDjibrilViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlPyuDjibrilViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlPyuDjibrilViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlPyuDjibrilVisByRateTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuDjibrilVi = alPyuDjibrilViRepository.saveAndFlush(alPyuDjibrilVi);

        // Get all the alPyuDjibrilViList where rateType equals to
        defaultAlPyuDjibrilViFiltering("rateType.equals=" + DEFAULT_RATE_TYPE, "rateType.equals=" + UPDATED_RATE_TYPE);
    }

    @Test
    @Transactional
    void getAllAlPyuDjibrilVisByRateTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuDjibrilVi = alPyuDjibrilViRepository.saveAndFlush(alPyuDjibrilVi);

        // Get all the alPyuDjibrilViList where rateType in
        defaultAlPyuDjibrilViFiltering("rateType.in=" + DEFAULT_RATE_TYPE + "," + UPDATED_RATE_TYPE, "rateType.in=" + UPDATED_RATE_TYPE);
    }

    @Test
    @Transactional
    void getAllAlPyuDjibrilVisByRateTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuDjibrilVi = alPyuDjibrilViRepository.saveAndFlush(alPyuDjibrilVi);

        // Get all the alPyuDjibrilViList where rateType is not null
        defaultAlPyuDjibrilViFiltering("rateType.specified=true", "rateType.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuDjibrilVisByRateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuDjibrilVi = alPyuDjibrilViRepository.saveAndFlush(alPyuDjibrilVi);

        // Get all the alPyuDjibrilViList where rate equals to
        defaultAlPyuDjibrilViFiltering("rate.equals=" + DEFAULT_RATE, "rate.equals=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    void getAllAlPyuDjibrilVisByRateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuDjibrilVi = alPyuDjibrilViRepository.saveAndFlush(alPyuDjibrilVi);

        // Get all the alPyuDjibrilViList where rate in
        defaultAlPyuDjibrilViFiltering("rate.in=" + DEFAULT_RATE + "," + UPDATED_RATE, "rate.in=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    void getAllAlPyuDjibrilVisByRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuDjibrilVi = alPyuDjibrilViRepository.saveAndFlush(alPyuDjibrilVi);

        // Get all the alPyuDjibrilViList where rate is not null
        defaultAlPyuDjibrilViFiltering("rate.specified=true", "rate.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuDjibrilVisByRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuDjibrilVi = alPyuDjibrilViRepository.saveAndFlush(alPyuDjibrilVi);

        // Get all the alPyuDjibrilViList where rate is greater than or equal to
        defaultAlPyuDjibrilViFiltering("rate.greaterThanOrEqual=" + DEFAULT_RATE, "rate.greaterThanOrEqual=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    void getAllAlPyuDjibrilVisByRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuDjibrilVi = alPyuDjibrilViRepository.saveAndFlush(alPyuDjibrilVi);

        // Get all the alPyuDjibrilViList where rate is less than or equal to
        defaultAlPyuDjibrilViFiltering("rate.lessThanOrEqual=" + DEFAULT_RATE, "rate.lessThanOrEqual=" + SMALLER_RATE);
    }

    @Test
    @Transactional
    void getAllAlPyuDjibrilVisByRateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlPyuDjibrilVi = alPyuDjibrilViRepository.saveAndFlush(alPyuDjibrilVi);

        // Get all the alPyuDjibrilViList where rate is less than
        defaultAlPyuDjibrilViFiltering("rate.lessThan=" + UPDATED_RATE, "rate.lessThan=" + DEFAULT_RATE);
    }

    @Test
    @Transactional
    void getAllAlPyuDjibrilVisByRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlPyuDjibrilVi = alPyuDjibrilViRepository.saveAndFlush(alPyuDjibrilVi);

        // Get all the alPyuDjibrilViList where rate is greater than
        defaultAlPyuDjibrilViFiltering("rate.greaterThan=" + SMALLER_RATE, "rate.greaterThan=" + DEFAULT_RATE);
    }

    @Test
    @Transactional
    void getAllAlPyuDjibrilVisByIsEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuDjibrilVi = alPyuDjibrilViRepository.saveAndFlush(alPyuDjibrilVi);

        // Get all the alPyuDjibrilViList where isEnabled equals to
        defaultAlPyuDjibrilViFiltering("isEnabled.equals=" + DEFAULT_IS_ENABLED, "isEnabled.equals=" + UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    void getAllAlPyuDjibrilVisByIsEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuDjibrilVi = alPyuDjibrilViRepository.saveAndFlush(alPyuDjibrilVi);

        // Get all the alPyuDjibrilViList where isEnabled in
        defaultAlPyuDjibrilViFiltering(
            "isEnabled.in=" + DEFAULT_IS_ENABLED + "," + UPDATED_IS_ENABLED,
            "isEnabled.in=" + UPDATED_IS_ENABLED
        );
    }

    @Test
    @Transactional
    void getAllAlPyuDjibrilVisByIsEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuDjibrilVi = alPyuDjibrilViRepository.saveAndFlush(alPyuDjibrilVi);

        // Get all the alPyuDjibrilViList where isEnabled is not null
        defaultAlPyuDjibrilViFiltering("isEnabled.specified=true", "isEnabled.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuDjibrilVisByPropertyIsEqualToSomething() throws Exception {
        AlProtyVi property;
        if (TestUtil.findAll(em, AlProtyVi.class).isEmpty()) {
            alPyuDjibrilViRepository.saveAndFlush(alPyuDjibrilVi);
            property = AlProtyViResourceIT.createEntity(em);
        } else {
            property = TestUtil.findAll(em, AlProtyVi.class).get(0);
        }
        em.persist(property);
        em.flush();
        alPyuDjibrilVi.setProperty(property);
        alPyuDjibrilViRepository.saveAndFlush(alPyuDjibrilVi);
        UUID propertyId = property.getId();
        // Get all the alPyuDjibrilViList where property equals to propertyId
        defaultAlPyuDjibrilViShouldBeFound("propertyId.equals=" + propertyId);

        // Get all the alPyuDjibrilViList where property equals to UUID.randomUUID()
        defaultAlPyuDjibrilViShouldNotBeFound("propertyId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlPyuDjibrilVisByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alPyuDjibrilViRepository.saveAndFlush(alPyuDjibrilVi);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alPyuDjibrilVi.setApplication(application);
        alPyuDjibrilViRepository.saveAndFlush(alPyuDjibrilVi);
        UUID applicationId = application.getId();
        // Get all the alPyuDjibrilViList where application equals to applicationId
        defaultAlPyuDjibrilViShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alPyuDjibrilViList where application equals to UUID.randomUUID()
        defaultAlPyuDjibrilViShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlPyuDjibrilViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlPyuDjibrilViShouldBeFound(shouldBeFound);
        defaultAlPyuDjibrilViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlPyuDjibrilViShouldBeFound(String filter) throws Exception {
        restAlPyuDjibrilViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPyuDjibrilVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].rateType").value(hasItem(DEFAULT_RATE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(sameNumber(DEFAULT_RATE))))
            .andExpect(jsonPath("$.[*].isEnabled").value(hasItem(DEFAULT_IS_ENABLED.booleanValue())));

        // Check, that the count call also returns 1
        restAlPyuDjibrilViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlPyuDjibrilViShouldNotBeFound(String filter) throws Exception {
        restAlPyuDjibrilViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlPyuDjibrilViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlPyuDjibrilVi() throws Exception {
        // Get the alPyuDjibrilVi
        restAlPyuDjibrilViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlPyuDjibrilVi() throws Exception {
        // Initialize the database
        insertedAlPyuDjibrilVi = alPyuDjibrilViRepository.saveAndFlush(alPyuDjibrilVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPyuDjibrilVi
        AlPyuDjibrilVi updatedAlPyuDjibrilVi = alPyuDjibrilViRepository.findById(alPyuDjibrilVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlPyuDjibrilVi are not directly saved in db
        em.detach(updatedAlPyuDjibrilVi);
        updatedAlPyuDjibrilVi.rateType(UPDATED_RATE_TYPE).rate(UPDATED_RATE).isEnabled(UPDATED_IS_ENABLED);
        AlPyuDjibrilViDTO alPyuDjibrilViDTO = alPyuDjibrilViMapper.toDto(updatedAlPyuDjibrilVi);

        restAlPyuDjibrilViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alPyuDjibrilViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPyuDjibrilViDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlPyuDjibrilVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlPyuDjibrilViToMatchAllProperties(updatedAlPyuDjibrilVi);
    }

    @Test
    @Transactional
    void putNonExistingAlPyuDjibrilVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPyuDjibrilVi.setId(longCount.incrementAndGet());

        // Create the AlPyuDjibrilVi
        AlPyuDjibrilViDTO alPyuDjibrilViDTO = alPyuDjibrilViMapper.toDto(alPyuDjibrilVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPyuDjibrilViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alPyuDjibrilViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPyuDjibrilViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPyuDjibrilVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlPyuDjibrilVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPyuDjibrilVi.setId(longCount.incrementAndGet());

        // Create the AlPyuDjibrilVi
        AlPyuDjibrilViDTO alPyuDjibrilViDTO = alPyuDjibrilViMapper.toDto(alPyuDjibrilVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPyuDjibrilViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPyuDjibrilViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPyuDjibrilVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlPyuDjibrilVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPyuDjibrilVi.setId(longCount.incrementAndGet());

        // Create the AlPyuDjibrilVi
        AlPyuDjibrilViDTO alPyuDjibrilViDTO = alPyuDjibrilViMapper.toDto(alPyuDjibrilVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPyuDjibrilViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPyuDjibrilViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPyuDjibrilVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlPyuDjibrilViWithPatch() throws Exception {
        // Initialize the database
        insertedAlPyuDjibrilVi = alPyuDjibrilViRepository.saveAndFlush(alPyuDjibrilVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPyuDjibrilVi using partial update
        AlPyuDjibrilVi partialUpdatedAlPyuDjibrilVi = new AlPyuDjibrilVi();
        partialUpdatedAlPyuDjibrilVi.setId(alPyuDjibrilVi.getId());

        restAlPyuDjibrilViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPyuDjibrilVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPyuDjibrilVi))
            )
            .andExpect(status().isOk());

        // Validate the AlPyuDjibrilVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPyuDjibrilViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlPyuDjibrilVi, alPyuDjibrilVi),
            getPersistedAlPyuDjibrilVi(alPyuDjibrilVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlPyuDjibrilViWithPatch() throws Exception {
        // Initialize the database
        insertedAlPyuDjibrilVi = alPyuDjibrilViRepository.saveAndFlush(alPyuDjibrilVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPyuDjibrilVi using partial update
        AlPyuDjibrilVi partialUpdatedAlPyuDjibrilVi = new AlPyuDjibrilVi();
        partialUpdatedAlPyuDjibrilVi.setId(alPyuDjibrilVi.getId());

        partialUpdatedAlPyuDjibrilVi.rateType(UPDATED_RATE_TYPE).rate(UPDATED_RATE).isEnabled(UPDATED_IS_ENABLED);

        restAlPyuDjibrilViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPyuDjibrilVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPyuDjibrilVi))
            )
            .andExpect(status().isOk());

        // Validate the AlPyuDjibrilVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPyuDjibrilViUpdatableFieldsEquals(partialUpdatedAlPyuDjibrilVi, getPersistedAlPyuDjibrilVi(partialUpdatedAlPyuDjibrilVi));
    }

    @Test
    @Transactional
    void patchNonExistingAlPyuDjibrilVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPyuDjibrilVi.setId(longCount.incrementAndGet());

        // Create the AlPyuDjibrilVi
        AlPyuDjibrilViDTO alPyuDjibrilViDTO = alPyuDjibrilViMapper.toDto(alPyuDjibrilVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPyuDjibrilViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alPyuDjibrilViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPyuDjibrilViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPyuDjibrilVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlPyuDjibrilVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPyuDjibrilVi.setId(longCount.incrementAndGet());

        // Create the AlPyuDjibrilVi
        AlPyuDjibrilViDTO alPyuDjibrilViDTO = alPyuDjibrilViMapper.toDto(alPyuDjibrilVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPyuDjibrilViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPyuDjibrilViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPyuDjibrilVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlPyuDjibrilVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPyuDjibrilVi.setId(longCount.incrementAndGet());

        // Create the AlPyuDjibrilVi
        AlPyuDjibrilViDTO alPyuDjibrilViDTO = alPyuDjibrilViMapper.toDto(alPyuDjibrilVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPyuDjibrilViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alPyuDjibrilViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPyuDjibrilVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlPyuDjibrilVi() throws Exception {
        // Initialize the database
        insertedAlPyuDjibrilVi = alPyuDjibrilViRepository.saveAndFlush(alPyuDjibrilVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alPyuDjibrilVi
        restAlPyuDjibrilViMockMvc
            .perform(delete(ENTITY_API_URL_ID, alPyuDjibrilVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alPyuDjibrilViRepository.count();
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

    protected AlPyuDjibrilVi getPersistedAlPyuDjibrilVi(AlPyuDjibrilVi alPyuDjibrilVi) {
        return alPyuDjibrilViRepository.findById(alPyuDjibrilVi.getId()).orElseThrow();
    }

    protected void assertPersistedAlPyuDjibrilViToMatchAllProperties(AlPyuDjibrilVi expectedAlPyuDjibrilVi) {
        assertAlPyuDjibrilViAllPropertiesEquals(expectedAlPyuDjibrilVi, getPersistedAlPyuDjibrilVi(expectedAlPyuDjibrilVi));
    }

    protected void assertPersistedAlPyuDjibrilViToMatchUpdatableProperties(AlPyuDjibrilVi expectedAlPyuDjibrilVi) {
        assertAlPyuDjibrilViAllUpdatablePropertiesEquals(expectedAlPyuDjibrilVi, getPersistedAlPyuDjibrilVi(expectedAlPyuDjibrilVi));
    }
}
