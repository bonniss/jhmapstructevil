package ai.realworld.web.rest;

import static ai.realworld.domain.AlGoreAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static ai.realworld.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlBetonamuRelation;
import ai.realworld.domain.AlGore;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.enumeration.AlcountScopy;
import ai.realworld.domain.enumeration.AlcountTypo;
import ai.realworld.repository.AlGoreRepository;
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
 * Integration tests for the {@link AlGoreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlGoreResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final AlcountTypo DEFAULT_DISCOUNT_TYPE = AlcountTypo.BY_FIXED_AMOUNT;
    private static final AlcountTypo UPDATED_DISCOUNT_TYPE = AlcountTypo.BY_PERCENT;

    private static final BigDecimal DEFAULT_DISCOUNT_RATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_DISCOUNT_RATE = new BigDecimal(2);
    private static final BigDecimal SMALLER_DISCOUNT_RATE = new BigDecimal(1 - 1);

    private static final AlcountScopy DEFAULT_SCOPE = AlcountScopy.ALL_PRODUCTS;
    private static final AlcountScopy UPDATED_SCOPE = AlcountScopy.SPECIFIED_PRODUCTS;

    private static final String ENTITY_API_URL = "/api/al-gores";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlGoreRepository alGoreRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlGoreMockMvc;

    private AlGore alGore;

    private AlGore insertedAlGore;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlGore createEntity() {
        return new AlGore().name(DEFAULT_NAME).discountType(DEFAULT_DISCOUNT_TYPE).discountRate(DEFAULT_DISCOUNT_RATE).scope(DEFAULT_SCOPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlGore createUpdatedEntity() {
        return new AlGore().name(UPDATED_NAME).discountType(UPDATED_DISCOUNT_TYPE).discountRate(UPDATED_DISCOUNT_RATE).scope(UPDATED_SCOPE);
    }

    @BeforeEach
    public void initTest() {
        alGore = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlGore != null) {
            alGoreRepository.delete(insertedAlGore);
            insertedAlGore = null;
        }
    }

    @Test
    @Transactional
    void createAlGore() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlGore
        var returnedAlGore = om.readValue(
            restAlGoreMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alGore)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlGore.class
        );

        // Validate the AlGore in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlGoreUpdatableFieldsEquals(returnedAlGore, getPersistedAlGore(returnedAlGore));

        insertedAlGore = returnedAlGore;
    }

    @Test
    @Transactional
    void createAlGoreWithExistingId() throws Exception {
        // Create the AlGore with an existing ID
        alGore.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlGoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alGore)))
            .andExpect(status().isBadRequest());

        // Validate the AlGore in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAlGores() throws Exception {
        // Initialize the database
        insertedAlGore = alGoreRepository.saveAndFlush(alGore);

        // Get all the alGoreList
        restAlGoreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alGore.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].discountType").value(hasItem(DEFAULT_DISCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].discountRate").value(hasItem(sameNumber(DEFAULT_DISCOUNT_RATE))))
            .andExpect(jsonPath("$.[*].scope").value(hasItem(DEFAULT_SCOPE.toString())));
    }

    @Test
    @Transactional
    void getAlGore() throws Exception {
        // Initialize the database
        insertedAlGore = alGoreRepository.saveAndFlush(alGore);

        // Get the alGore
        restAlGoreMockMvc
            .perform(get(ENTITY_API_URL_ID, alGore.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alGore.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.discountType").value(DEFAULT_DISCOUNT_TYPE.toString()))
            .andExpect(jsonPath("$.discountRate").value(sameNumber(DEFAULT_DISCOUNT_RATE)))
            .andExpect(jsonPath("$.scope").value(DEFAULT_SCOPE.toString()));
    }

    @Test
    @Transactional
    void getAlGoresByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlGore = alGoreRepository.saveAndFlush(alGore);

        Long id = alGore.getId();

        defaultAlGoreFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlGoreFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlGoreFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlGoresByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGore = alGoreRepository.saveAndFlush(alGore);

        // Get all the alGoreList where name equals to
        defaultAlGoreFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlGoresByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlGore = alGoreRepository.saveAndFlush(alGore);

        // Get all the alGoreList where name in
        defaultAlGoreFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlGoresByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlGore = alGoreRepository.saveAndFlush(alGore);

        // Get all the alGoreList where name is not null
        defaultAlGoreFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAlGoresByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlGore = alGoreRepository.saveAndFlush(alGore);

        // Get all the alGoreList where name contains
        defaultAlGoreFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlGoresByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlGore = alGoreRepository.saveAndFlush(alGore);

        // Get all the alGoreList where name does not contain
        defaultAlGoreFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAlGoresByDiscountTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGore = alGoreRepository.saveAndFlush(alGore);

        // Get all the alGoreList where discountType equals to
        defaultAlGoreFiltering("discountType.equals=" + DEFAULT_DISCOUNT_TYPE, "discountType.equals=" + UPDATED_DISCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllAlGoresByDiscountTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlGore = alGoreRepository.saveAndFlush(alGore);

        // Get all the alGoreList where discountType in
        defaultAlGoreFiltering(
            "discountType.in=" + DEFAULT_DISCOUNT_TYPE + "," + UPDATED_DISCOUNT_TYPE,
            "discountType.in=" + UPDATED_DISCOUNT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAlGoresByDiscountTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlGore = alGoreRepository.saveAndFlush(alGore);

        // Get all the alGoreList where discountType is not null
        defaultAlGoreFiltering("discountType.specified=true", "discountType.specified=false");
    }

    @Test
    @Transactional
    void getAllAlGoresByDiscountRateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGore = alGoreRepository.saveAndFlush(alGore);

        // Get all the alGoreList where discountRate equals to
        defaultAlGoreFiltering("discountRate.equals=" + DEFAULT_DISCOUNT_RATE, "discountRate.equals=" + UPDATED_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllAlGoresByDiscountRateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlGore = alGoreRepository.saveAndFlush(alGore);

        // Get all the alGoreList where discountRate in
        defaultAlGoreFiltering(
            "discountRate.in=" + DEFAULT_DISCOUNT_RATE + "," + UPDATED_DISCOUNT_RATE,
            "discountRate.in=" + UPDATED_DISCOUNT_RATE
        );
    }

    @Test
    @Transactional
    void getAllAlGoresByDiscountRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlGore = alGoreRepository.saveAndFlush(alGore);

        // Get all the alGoreList where discountRate is not null
        defaultAlGoreFiltering("discountRate.specified=true", "discountRate.specified=false");
    }

    @Test
    @Transactional
    void getAllAlGoresByDiscountRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGore = alGoreRepository.saveAndFlush(alGore);

        // Get all the alGoreList where discountRate is greater than or equal to
        defaultAlGoreFiltering(
            "discountRate.greaterThanOrEqual=" + DEFAULT_DISCOUNT_RATE,
            "discountRate.greaterThanOrEqual=" + UPDATED_DISCOUNT_RATE
        );
    }

    @Test
    @Transactional
    void getAllAlGoresByDiscountRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGore = alGoreRepository.saveAndFlush(alGore);

        // Get all the alGoreList where discountRate is less than or equal to
        defaultAlGoreFiltering(
            "discountRate.lessThanOrEqual=" + DEFAULT_DISCOUNT_RATE,
            "discountRate.lessThanOrEqual=" + SMALLER_DISCOUNT_RATE
        );
    }

    @Test
    @Transactional
    void getAllAlGoresByDiscountRateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlGore = alGoreRepository.saveAndFlush(alGore);

        // Get all the alGoreList where discountRate is less than
        defaultAlGoreFiltering("discountRate.lessThan=" + UPDATED_DISCOUNT_RATE, "discountRate.lessThan=" + DEFAULT_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllAlGoresByDiscountRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlGore = alGoreRepository.saveAndFlush(alGore);

        // Get all the alGoreList where discountRate is greater than
        defaultAlGoreFiltering("discountRate.greaterThan=" + SMALLER_DISCOUNT_RATE, "discountRate.greaterThan=" + DEFAULT_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllAlGoresByScopeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGore = alGoreRepository.saveAndFlush(alGore);

        // Get all the alGoreList where scope equals to
        defaultAlGoreFiltering("scope.equals=" + DEFAULT_SCOPE, "scope.equals=" + UPDATED_SCOPE);
    }

    @Test
    @Transactional
    void getAllAlGoresByScopeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlGore = alGoreRepository.saveAndFlush(alGore);

        // Get all the alGoreList where scope in
        defaultAlGoreFiltering("scope.in=" + DEFAULT_SCOPE + "," + UPDATED_SCOPE, "scope.in=" + UPDATED_SCOPE);
    }

    @Test
    @Transactional
    void getAllAlGoresByScopeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlGore = alGoreRepository.saveAndFlush(alGore);

        // Get all the alGoreList where scope is not null
        defaultAlGoreFiltering("scope.specified=true", "scope.specified=false");
    }

    @Test
    @Transactional
    void getAllAlGoresByBizRelationIsEqualToSomething() throws Exception {
        AlBetonamuRelation bizRelation;
        if (TestUtil.findAll(em, AlBetonamuRelation.class).isEmpty()) {
            alGoreRepository.saveAndFlush(alGore);
            bizRelation = AlBetonamuRelationResourceIT.createEntity(em);
        } else {
            bizRelation = TestUtil.findAll(em, AlBetonamuRelation.class).get(0);
        }
        em.persist(bizRelation);
        em.flush();
        alGore.setBizRelation(bizRelation);
        alGoreRepository.saveAndFlush(alGore);
        Long bizRelationId = bizRelation.getId();
        // Get all the alGoreList where bizRelation equals to bizRelationId
        defaultAlGoreShouldBeFound("bizRelationId.equals=" + bizRelationId);

        // Get all the alGoreList where bizRelation equals to (bizRelationId + 1)
        defaultAlGoreShouldNotBeFound("bizRelationId.equals=" + (bizRelationId + 1));
    }

    @Test
    @Transactional
    void getAllAlGoresByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alGoreRepository.saveAndFlush(alGore);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alGore.setApplication(application);
        alGoreRepository.saveAndFlush(alGore);
        UUID applicationId = application.getId();
        // Get all the alGoreList where application equals to applicationId
        defaultAlGoreShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alGoreList where application equals to UUID.randomUUID()
        defaultAlGoreShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlGoreFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlGoreShouldBeFound(shouldBeFound);
        defaultAlGoreShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlGoreShouldBeFound(String filter) throws Exception {
        restAlGoreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alGore.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].discountType").value(hasItem(DEFAULT_DISCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].discountRate").value(hasItem(sameNumber(DEFAULT_DISCOUNT_RATE))))
            .andExpect(jsonPath("$.[*].scope").value(hasItem(DEFAULT_SCOPE.toString())));

        // Check, that the count call also returns 1
        restAlGoreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlGoreShouldNotBeFound(String filter) throws Exception {
        restAlGoreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlGoreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlGore() throws Exception {
        // Get the alGore
        restAlGoreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlGore() throws Exception {
        // Initialize the database
        insertedAlGore = alGoreRepository.saveAndFlush(alGore);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alGore
        AlGore updatedAlGore = alGoreRepository.findById(alGore.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlGore are not directly saved in db
        em.detach(updatedAlGore);
        updatedAlGore.name(UPDATED_NAME).discountType(UPDATED_DISCOUNT_TYPE).discountRate(UPDATED_DISCOUNT_RATE).scope(UPDATED_SCOPE);

        restAlGoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlGore.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlGore))
            )
            .andExpect(status().isOk());

        // Validate the AlGore in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlGoreToMatchAllProperties(updatedAlGore);
    }

    @Test
    @Transactional
    void putNonExistingAlGore() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alGore.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlGoreMockMvc
            .perform(put(ENTITY_API_URL_ID, alGore.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alGore)))
            .andExpect(status().isBadRequest());

        // Validate the AlGore in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlGore() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alGore.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlGoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alGore))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlGore in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlGore() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alGore.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlGoreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alGore)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlGore in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlGoreWithPatch() throws Exception {
        // Initialize the database
        insertedAlGore = alGoreRepository.saveAndFlush(alGore);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alGore using partial update
        AlGore partialUpdatedAlGore = new AlGore();
        partialUpdatedAlGore.setId(alGore.getId());

        partialUpdatedAlGore.name(UPDATED_NAME).discountType(UPDATED_DISCOUNT_TYPE);

        restAlGoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlGore.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlGore))
            )
            .andExpect(status().isOk());

        // Validate the AlGore in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlGoreUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAlGore, alGore), getPersistedAlGore(alGore));
    }

    @Test
    @Transactional
    void fullUpdateAlGoreWithPatch() throws Exception {
        // Initialize the database
        insertedAlGore = alGoreRepository.saveAndFlush(alGore);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alGore using partial update
        AlGore partialUpdatedAlGore = new AlGore();
        partialUpdatedAlGore.setId(alGore.getId());

        partialUpdatedAlGore
            .name(UPDATED_NAME)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .discountRate(UPDATED_DISCOUNT_RATE)
            .scope(UPDATED_SCOPE);

        restAlGoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlGore.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlGore))
            )
            .andExpect(status().isOk());

        // Validate the AlGore in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlGoreUpdatableFieldsEquals(partialUpdatedAlGore, getPersistedAlGore(partialUpdatedAlGore));
    }

    @Test
    @Transactional
    void patchNonExistingAlGore() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alGore.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlGoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alGore.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alGore))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlGore in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlGore() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alGore.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlGoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alGore))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlGore in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlGore() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alGore.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlGoreMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alGore)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlGore in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlGore() throws Exception {
        // Initialize the database
        insertedAlGore = alGoreRepository.saveAndFlush(alGore);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alGore
        restAlGoreMockMvc
            .perform(delete(ENTITY_API_URL_ID, alGore.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alGoreRepository.count();
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

    protected AlGore getPersistedAlGore(AlGore alGore) {
        return alGoreRepository.findById(alGore.getId()).orElseThrow();
    }

    protected void assertPersistedAlGoreToMatchAllProperties(AlGore expectedAlGore) {
        assertAlGoreAllPropertiesEquals(expectedAlGore, getPersistedAlGore(expectedAlGore));
    }

    protected void assertPersistedAlGoreToMatchUpdatableProperties(AlGore expectedAlGore) {
        assertAlGoreAllUpdatablePropertiesEquals(expectedAlGore, getPersistedAlGore(expectedAlGore));
    }
}
