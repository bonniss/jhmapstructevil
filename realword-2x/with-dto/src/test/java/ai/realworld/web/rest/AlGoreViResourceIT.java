package ai.realworld.web.rest;

import static ai.realworld.domain.AlGoreViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static ai.realworld.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlGoreVi;
import ai.realworld.domain.enumeration.AlcountScopy;
import ai.realworld.domain.enumeration.AlcountTypo;
import ai.realworld.repository.AlGoreViRepository;
import ai.realworld.service.dto.AlGoreViDTO;
import ai.realworld.service.mapper.AlGoreViMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Random;
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
 * Integration tests for the {@link AlGoreViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlGoreViResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final AlcountTypo DEFAULT_DISCOUNT_TYPE = AlcountTypo.BY_FIXED_AMOUNT;
    private static final AlcountTypo UPDATED_DISCOUNT_TYPE = AlcountTypo.BY_PERCENT;

    private static final BigDecimal DEFAULT_DISCOUNT_RATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_DISCOUNT_RATE = new BigDecimal(2);
    private static final BigDecimal SMALLER_DISCOUNT_RATE = new BigDecimal(1 - 1);

    private static final AlcountScopy DEFAULT_SCOPE = AlcountScopy.ALL_PRODUCTS;
    private static final AlcountScopy UPDATED_SCOPE = AlcountScopy.SPECIFIED_PRODUCTS;

    private static final String ENTITY_API_URL = "/api/al-gore-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlGoreViRepository alGoreViRepository;

    @Autowired
    private AlGoreViMapper alGoreViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlGoreViMockMvc;

    private AlGoreVi alGoreVi;

    private AlGoreVi insertedAlGoreVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlGoreVi createEntity() {
        return new AlGoreVi()
            .name(DEFAULT_NAME)
            .discountType(DEFAULT_DISCOUNT_TYPE)
            .discountRate(DEFAULT_DISCOUNT_RATE)
            .scope(DEFAULT_SCOPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlGoreVi createUpdatedEntity() {
        return new AlGoreVi()
            .name(UPDATED_NAME)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .discountRate(UPDATED_DISCOUNT_RATE)
            .scope(UPDATED_SCOPE);
    }

    @BeforeEach
    public void initTest() {
        alGoreVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlGoreVi != null) {
            alGoreViRepository.delete(insertedAlGoreVi);
            insertedAlGoreVi = null;
        }
    }

    @Test
    @Transactional
    void createAlGoreVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlGoreVi
        AlGoreViDTO alGoreViDTO = alGoreViMapper.toDto(alGoreVi);
        var returnedAlGoreViDTO = om.readValue(
            restAlGoreViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alGoreViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlGoreViDTO.class
        );

        // Validate the AlGoreVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlGoreVi = alGoreViMapper.toEntity(returnedAlGoreViDTO);
        assertAlGoreViUpdatableFieldsEquals(returnedAlGoreVi, getPersistedAlGoreVi(returnedAlGoreVi));

        insertedAlGoreVi = returnedAlGoreVi;
    }

    @Test
    @Transactional
    void createAlGoreViWithExistingId() throws Exception {
        // Create the AlGoreVi with an existing ID
        alGoreVi.setId(1L);
        AlGoreViDTO alGoreViDTO = alGoreViMapper.toDto(alGoreVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlGoreViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alGoreViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlGoreVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAlGoreVis() throws Exception {
        // Initialize the database
        insertedAlGoreVi = alGoreViRepository.saveAndFlush(alGoreVi);

        // Get all the alGoreViList
        restAlGoreViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alGoreVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].discountType").value(hasItem(DEFAULT_DISCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].discountRate").value(hasItem(sameNumber(DEFAULT_DISCOUNT_RATE))))
            .andExpect(jsonPath("$.[*].scope").value(hasItem(DEFAULT_SCOPE.toString())));
    }

    @Test
    @Transactional
    void getAlGoreVi() throws Exception {
        // Initialize the database
        insertedAlGoreVi = alGoreViRepository.saveAndFlush(alGoreVi);

        // Get the alGoreVi
        restAlGoreViMockMvc
            .perform(get(ENTITY_API_URL_ID, alGoreVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alGoreVi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.discountType").value(DEFAULT_DISCOUNT_TYPE.toString()))
            .andExpect(jsonPath("$.discountRate").value(sameNumber(DEFAULT_DISCOUNT_RATE)))
            .andExpect(jsonPath("$.scope").value(DEFAULT_SCOPE.toString()));
    }

    @Test
    @Transactional
    void getAlGoreVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlGoreVi = alGoreViRepository.saveAndFlush(alGoreVi);

        Long id = alGoreVi.getId();

        defaultAlGoreViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlGoreViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlGoreViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlGoreVisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoreVi = alGoreViRepository.saveAndFlush(alGoreVi);

        // Get all the alGoreViList where name equals to
        defaultAlGoreViFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlGoreVisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlGoreVi = alGoreViRepository.saveAndFlush(alGoreVi);

        // Get all the alGoreViList where name in
        defaultAlGoreViFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlGoreVisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlGoreVi = alGoreViRepository.saveAndFlush(alGoreVi);

        // Get all the alGoreViList where name is not null
        defaultAlGoreViFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAlGoreVisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlGoreVi = alGoreViRepository.saveAndFlush(alGoreVi);

        // Get all the alGoreViList where name contains
        defaultAlGoreViFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlGoreVisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlGoreVi = alGoreViRepository.saveAndFlush(alGoreVi);

        // Get all the alGoreViList where name does not contain
        defaultAlGoreViFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAlGoreVisByDiscountTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoreVi = alGoreViRepository.saveAndFlush(alGoreVi);

        // Get all the alGoreViList where discountType equals to
        defaultAlGoreViFiltering("discountType.equals=" + DEFAULT_DISCOUNT_TYPE, "discountType.equals=" + UPDATED_DISCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllAlGoreVisByDiscountTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlGoreVi = alGoreViRepository.saveAndFlush(alGoreVi);

        // Get all the alGoreViList where discountType in
        defaultAlGoreViFiltering(
            "discountType.in=" + DEFAULT_DISCOUNT_TYPE + "," + UPDATED_DISCOUNT_TYPE,
            "discountType.in=" + UPDATED_DISCOUNT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAlGoreVisByDiscountTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlGoreVi = alGoreViRepository.saveAndFlush(alGoreVi);

        // Get all the alGoreViList where discountType is not null
        defaultAlGoreViFiltering("discountType.specified=true", "discountType.specified=false");
    }

    @Test
    @Transactional
    void getAllAlGoreVisByDiscountRateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoreVi = alGoreViRepository.saveAndFlush(alGoreVi);

        // Get all the alGoreViList where discountRate equals to
        defaultAlGoreViFiltering("discountRate.equals=" + DEFAULT_DISCOUNT_RATE, "discountRate.equals=" + UPDATED_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllAlGoreVisByDiscountRateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlGoreVi = alGoreViRepository.saveAndFlush(alGoreVi);

        // Get all the alGoreViList where discountRate in
        defaultAlGoreViFiltering(
            "discountRate.in=" + DEFAULT_DISCOUNT_RATE + "," + UPDATED_DISCOUNT_RATE,
            "discountRate.in=" + UPDATED_DISCOUNT_RATE
        );
    }

    @Test
    @Transactional
    void getAllAlGoreVisByDiscountRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlGoreVi = alGoreViRepository.saveAndFlush(alGoreVi);

        // Get all the alGoreViList where discountRate is not null
        defaultAlGoreViFiltering("discountRate.specified=true", "discountRate.specified=false");
    }

    @Test
    @Transactional
    void getAllAlGoreVisByDiscountRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoreVi = alGoreViRepository.saveAndFlush(alGoreVi);

        // Get all the alGoreViList where discountRate is greater than or equal to
        defaultAlGoreViFiltering(
            "discountRate.greaterThanOrEqual=" + DEFAULT_DISCOUNT_RATE,
            "discountRate.greaterThanOrEqual=" + UPDATED_DISCOUNT_RATE
        );
    }

    @Test
    @Transactional
    void getAllAlGoreVisByDiscountRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoreVi = alGoreViRepository.saveAndFlush(alGoreVi);

        // Get all the alGoreViList where discountRate is less than or equal to
        defaultAlGoreViFiltering(
            "discountRate.lessThanOrEqual=" + DEFAULT_DISCOUNT_RATE,
            "discountRate.lessThanOrEqual=" + SMALLER_DISCOUNT_RATE
        );
    }

    @Test
    @Transactional
    void getAllAlGoreVisByDiscountRateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlGoreVi = alGoreViRepository.saveAndFlush(alGoreVi);

        // Get all the alGoreViList where discountRate is less than
        defaultAlGoreViFiltering("discountRate.lessThan=" + UPDATED_DISCOUNT_RATE, "discountRate.lessThan=" + DEFAULT_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllAlGoreVisByDiscountRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlGoreVi = alGoreViRepository.saveAndFlush(alGoreVi);

        // Get all the alGoreViList where discountRate is greater than
        defaultAlGoreViFiltering("discountRate.greaterThan=" + SMALLER_DISCOUNT_RATE, "discountRate.greaterThan=" + DEFAULT_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllAlGoreVisByScopeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoreVi = alGoreViRepository.saveAndFlush(alGoreVi);

        // Get all the alGoreViList where scope equals to
        defaultAlGoreViFiltering("scope.equals=" + DEFAULT_SCOPE, "scope.equals=" + UPDATED_SCOPE);
    }

    @Test
    @Transactional
    void getAllAlGoreVisByScopeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlGoreVi = alGoreViRepository.saveAndFlush(alGoreVi);

        // Get all the alGoreViList where scope in
        defaultAlGoreViFiltering("scope.in=" + DEFAULT_SCOPE + "," + UPDATED_SCOPE, "scope.in=" + UPDATED_SCOPE);
    }

    @Test
    @Transactional
    void getAllAlGoreVisByScopeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlGoreVi = alGoreViRepository.saveAndFlush(alGoreVi);

        // Get all the alGoreViList where scope is not null
        defaultAlGoreViFiltering("scope.specified=true", "scope.specified=false");
    }

    private void defaultAlGoreViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlGoreViShouldBeFound(shouldBeFound);
        defaultAlGoreViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlGoreViShouldBeFound(String filter) throws Exception {
        restAlGoreViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alGoreVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].discountType").value(hasItem(DEFAULT_DISCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].discountRate").value(hasItem(sameNumber(DEFAULT_DISCOUNT_RATE))))
            .andExpect(jsonPath("$.[*].scope").value(hasItem(DEFAULT_SCOPE.toString())));

        // Check, that the count call also returns 1
        restAlGoreViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlGoreViShouldNotBeFound(String filter) throws Exception {
        restAlGoreViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlGoreViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlGoreVi() throws Exception {
        // Get the alGoreVi
        restAlGoreViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlGoreVi() throws Exception {
        // Initialize the database
        insertedAlGoreVi = alGoreViRepository.saveAndFlush(alGoreVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alGoreVi
        AlGoreVi updatedAlGoreVi = alGoreViRepository.findById(alGoreVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlGoreVi are not directly saved in db
        em.detach(updatedAlGoreVi);
        updatedAlGoreVi.name(UPDATED_NAME).discountType(UPDATED_DISCOUNT_TYPE).discountRate(UPDATED_DISCOUNT_RATE).scope(UPDATED_SCOPE);
        AlGoreViDTO alGoreViDTO = alGoreViMapper.toDto(updatedAlGoreVi);

        restAlGoreViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alGoreViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alGoreViDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlGoreVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlGoreViToMatchAllProperties(updatedAlGoreVi);
    }

    @Test
    @Transactional
    void putNonExistingAlGoreVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alGoreVi.setId(longCount.incrementAndGet());

        // Create the AlGoreVi
        AlGoreViDTO alGoreViDTO = alGoreViMapper.toDto(alGoreVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlGoreViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alGoreViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alGoreViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlGoreVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlGoreVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alGoreVi.setId(longCount.incrementAndGet());

        // Create the AlGoreVi
        AlGoreViDTO alGoreViDTO = alGoreViMapper.toDto(alGoreVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlGoreViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alGoreViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlGoreVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlGoreVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alGoreVi.setId(longCount.incrementAndGet());

        // Create the AlGoreVi
        AlGoreViDTO alGoreViDTO = alGoreViMapper.toDto(alGoreVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlGoreViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alGoreViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlGoreVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlGoreViWithPatch() throws Exception {
        // Initialize the database
        insertedAlGoreVi = alGoreViRepository.saveAndFlush(alGoreVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alGoreVi using partial update
        AlGoreVi partialUpdatedAlGoreVi = new AlGoreVi();
        partialUpdatedAlGoreVi.setId(alGoreVi.getId());

        partialUpdatedAlGoreVi.name(UPDATED_NAME).discountType(UPDATED_DISCOUNT_TYPE).scope(UPDATED_SCOPE);

        restAlGoreViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlGoreVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlGoreVi))
            )
            .andExpect(status().isOk());

        // Validate the AlGoreVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlGoreViUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAlGoreVi, alGoreVi), getPersistedAlGoreVi(alGoreVi));
    }

    @Test
    @Transactional
    void fullUpdateAlGoreViWithPatch() throws Exception {
        // Initialize the database
        insertedAlGoreVi = alGoreViRepository.saveAndFlush(alGoreVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alGoreVi using partial update
        AlGoreVi partialUpdatedAlGoreVi = new AlGoreVi();
        partialUpdatedAlGoreVi.setId(alGoreVi.getId());

        partialUpdatedAlGoreVi
            .name(UPDATED_NAME)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .discountRate(UPDATED_DISCOUNT_RATE)
            .scope(UPDATED_SCOPE);

        restAlGoreViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlGoreVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlGoreVi))
            )
            .andExpect(status().isOk());

        // Validate the AlGoreVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlGoreViUpdatableFieldsEquals(partialUpdatedAlGoreVi, getPersistedAlGoreVi(partialUpdatedAlGoreVi));
    }

    @Test
    @Transactional
    void patchNonExistingAlGoreVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alGoreVi.setId(longCount.incrementAndGet());

        // Create the AlGoreVi
        AlGoreViDTO alGoreViDTO = alGoreViMapper.toDto(alGoreVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlGoreViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alGoreViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alGoreViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlGoreVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlGoreVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alGoreVi.setId(longCount.incrementAndGet());

        // Create the AlGoreVi
        AlGoreViDTO alGoreViDTO = alGoreViMapper.toDto(alGoreVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlGoreViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alGoreViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlGoreVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlGoreVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alGoreVi.setId(longCount.incrementAndGet());

        // Create the AlGoreVi
        AlGoreViDTO alGoreViDTO = alGoreViMapper.toDto(alGoreVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlGoreViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alGoreViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlGoreVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlGoreVi() throws Exception {
        // Initialize the database
        insertedAlGoreVi = alGoreViRepository.saveAndFlush(alGoreVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alGoreVi
        restAlGoreViMockMvc
            .perform(delete(ENTITY_API_URL_ID, alGoreVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alGoreViRepository.count();
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

    protected AlGoreVi getPersistedAlGoreVi(AlGoreVi alGoreVi) {
        return alGoreViRepository.findById(alGoreVi.getId()).orElseThrow();
    }

    protected void assertPersistedAlGoreViToMatchAllProperties(AlGoreVi expectedAlGoreVi) {
        assertAlGoreViAllPropertiesEquals(expectedAlGoreVi, getPersistedAlGoreVi(expectedAlGoreVi));
    }

    protected void assertPersistedAlGoreViToMatchUpdatableProperties(AlGoreVi expectedAlGoreVi) {
        assertAlGoreViAllUpdatablePropertiesEquals(expectedAlGoreVi, getPersistedAlGoreVi(expectedAlGoreVi));
    }
}
