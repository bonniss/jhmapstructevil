package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextPaymentViViAsserts.*;
import static xyz.jhmapstruct.web.rest.TestUtil.createUpdateProxyForBean;
import static xyz.jhmapstruct.web.rest.TestUtil.sameNumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import xyz.jhmapstruct.IntegrationTest;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextPaymentViVi;
import xyz.jhmapstruct.domain.enumeration.NextPaymentViViMethod;
import xyz.jhmapstruct.repository.NextPaymentViViRepository;

/**
 * Integration tests for the {@link NextPaymentViViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextPaymentViViResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final Instant DEFAULT_PAYMENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYMENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final NextPaymentViViMethod DEFAULT_PAYMENT_METHOD = NextPaymentViViMethod.CREDIT_CARD;
    private static final NextPaymentViViMethod UPDATED_PAYMENT_METHOD = NextPaymentViViMethod.PAYPAL;

    private static final String ENTITY_API_URL = "/api/next-payment-vi-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextPaymentViViRepository nextPaymentViViRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextPaymentViViMockMvc;

    private NextPaymentViVi nextPaymentViVi;

    private NextPaymentViVi insertedNextPaymentViVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextPaymentViVi createEntity() {
        return new NextPaymentViVi().amount(DEFAULT_AMOUNT).paymentDate(DEFAULT_PAYMENT_DATE).paymentMethod(DEFAULT_PAYMENT_METHOD);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextPaymentViVi createUpdatedEntity() {
        return new NextPaymentViVi().amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);
    }

    @BeforeEach
    public void initTest() {
        nextPaymentViVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextPaymentViVi != null) {
            nextPaymentViViRepository.delete(insertedNextPaymentViVi);
            insertedNextPaymentViVi = null;
        }
    }

    @Test
    @Transactional
    void createNextPaymentViVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextPaymentViVi
        var returnedNextPaymentViVi = om.readValue(
            restNextPaymentViViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentViVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextPaymentViVi.class
        );

        // Validate the NextPaymentViVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextPaymentViViUpdatableFieldsEquals(returnedNextPaymentViVi, getPersistedNextPaymentViVi(returnedNextPaymentViVi));

        insertedNextPaymentViVi = returnedNextPaymentViVi;
    }

    @Test
    @Transactional
    void createNextPaymentViViWithExistingId() throws Exception {
        // Create the NextPaymentViVi with an existing ID
        nextPaymentViVi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextPaymentViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentViVi)))
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextPaymentViVi.setAmount(null);

        // Create the NextPaymentViVi, which fails.

        restNextPaymentViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentViVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextPaymentViVi.setPaymentDate(null);

        // Create the NextPaymentViVi, which fails.

        restNextPaymentViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentViVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextPaymentViVis() throws Exception {
        // Initialize the database
        insertedNextPaymentViVi = nextPaymentViViRepository.saveAndFlush(nextPaymentViVi);

        // Get all the nextPaymentViViList
        restNextPaymentViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextPaymentViVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));
    }

    @Test
    @Transactional
    void getNextPaymentViVi() throws Exception {
        // Initialize the database
        insertedNextPaymentViVi = nextPaymentViViRepository.saveAndFlush(nextPaymentViVi);

        // Get the nextPaymentViVi
        restNextPaymentViViMockMvc
            .perform(get(ENTITY_API_URL_ID, nextPaymentViVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextPaymentViVi.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()));
    }

    @Test
    @Transactional
    void getNextPaymentViVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextPaymentViVi = nextPaymentViViRepository.saveAndFlush(nextPaymentViVi);

        Long id = nextPaymentViVi.getId();

        defaultNextPaymentViViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextPaymentViViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextPaymentViViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextPaymentViVisByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentViVi = nextPaymentViViRepository.saveAndFlush(nextPaymentViVi);

        // Get all the nextPaymentViViList where amount equals to
        defaultNextPaymentViViFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentViVisByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextPaymentViVi = nextPaymentViViRepository.saveAndFlush(nextPaymentViVi);

        // Get all the nextPaymentViViList where amount in
        defaultNextPaymentViViFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentViVisByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextPaymentViVi = nextPaymentViViRepository.saveAndFlush(nextPaymentViVi);

        // Get all the nextPaymentViViList where amount is not null
        defaultNextPaymentViViFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllNextPaymentViVisByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentViVi = nextPaymentViViRepository.saveAndFlush(nextPaymentViVi);

        // Get all the nextPaymentViViList where amount is greater than or equal to
        defaultNextPaymentViViFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentViVisByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentViVi = nextPaymentViViRepository.saveAndFlush(nextPaymentViVi);

        // Get all the nextPaymentViViList where amount is less than or equal to
        defaultNextPaymentViViFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentViVisByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentViVi = nextPaymentViViRepository.saveAndFlush(nextPaymentViVi);

        // Get all the nextPaymentViViList where amount is less than
        defaultNextPaymentViViFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentViVisByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentViVi = nextPaymentViViRepository.saveAndFlush(nextPaymentViVi);

        // Get all the nextPaymentViViList where amount is greater than
        defaultNextPaymentViViFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentViVisByPaymentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentViVi = nextPaymentViViRepository.saveAndFlush(nextPaymentViVi);

        // Get all the nextPaymentViViList where paymentDate equals to
        defaultNextPaymentViViFiltering("paymentDate.equals=" + DEFAULT_PAYMENT_DATE, "paymentDate.equals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllNextPaymentViVisByPaymentDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextPaymentViVi = nextPaymentViViRepository.saveAndFlush(nextPaymentViVi);

        // Get all the nextPaymentViViList where paymentDate in
        defaultNextPaymentViViFiltering(
            "paymentDate.in=" + DEFAULT_PAYMENT_DATE + "," + UPDATED_PAYMENT_DATE,
            "paymentDate.in=" + UPDATED_PAYMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextPaymentViVisByPaymentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextPaymentViVi = nextPaymentViViRepository.saveAndFlush(nextPaymentViVi);

        // Get all the nextPaymentViViList where paymentDate is not null
        defaultNextPaymentViViFiltering("paymentDate.specified=true", "paymentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextPaymentViVisByPaymentMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentViVi = nextPaymentViViRepository.saveAndFlush(nextPaymentViVi);

        // Get all the nextPaymentViViList where paymentMethod equals to
        defaultNextPaymentViViFiltering("paymentMethod.equals=" + DEFAULT_PAYMENT_METHOD, "paymentMethod.equals=" + UPDATED_PAYMENT_METHOD);
    }

    @Test
    @Transactional
    void getAllNextPaymentViVisByPaymentMethodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextPaymentViVi = nextPaymentViViRepository.saveAndFlush(nextPaymentViVi);

        // Get all the nextPaymentViViList where paymentMethod in
        defaultNextPaymentViViFiltering(
            "paymentMethod.in=" + DEFAULT_PAYMENT_METHOD + "," + UPDATED_PAYMENT_METHOD,
            "paymentMethod.in=" + UPDATED_PAYMENT_METHOD
        );
    }

    @Test
    @Transactional
    void getAllNextPaymentViVisByPaymentMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextPaymentViVi = nextPaymentViViRepository.saveAndFlush(nextPaymentViVi);

        // Get all the nextPaymentViViList where paymentMethod is not null
        defaultNextPaymentViViFiltering("paymentMethod.specified=true", "paymentMethod.specified=false");
    }

    @Test
    @Transactional
    void getAllNextPaymentViVisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextPaymentViViRepository.saveAndFlush(nextPaymentViVi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextPaymentViVi.setTenant(tenant);
        nextPaymentViViRepository.saveAndFlush(nextPaymentViVi);
        Long tenantId = tenant.getId();
        // Get all the nextPaymentViViList where tenant equals to tenantId
        defaultNextPaymentViViShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextPaymentViViList where tenant equals to (tenantId + 1)
        defaultNextPaymentViViShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextPaymentViViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextPaymentViViShouldBeFound(shouldBeFound);
        defaultNextPaymentViViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextPaymentViViShouldBeFound(String filter) throws Exception {
        restNextPaymentViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextPaymentViVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));

        // Check, that the count call also returns 1
        restNextPaymentViViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextPaymentViViShouldNotBeFound(String filter) throws Exception {
        restNextPaymentViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextPaymentViViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextPaymentViVi() throws Exception {
        // Get the nextPaymentViVi
        restNextPaymentViViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextPaymentViVi() throws Exception {
        // Initialize the database
        insertedNextPaymentViVi = nextPaymentViViRepository.saveAndFlush(nextPaymentViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextPaymentViVi
        NextPaymentViVi updatedNextPaymentViVi = nextPaymentViViRepository.findById(nextPaymentViVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextPaymentViVi are not directly saved in db
        em.detach(updatedNextPaymentViVi);
        updatedNextPaymentViVi.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restNextPaymentViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextPaymentViVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextPaymentViVi))
            )
            .andExpect(status().isOk());

        // Validate the NextPaymentViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextPaymentViViToMatchAllProperties(updatedNextPaymentViVi);
    }

    @Test
    @Transactional
    void putNonExistingNextPaymentViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentViVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextPaymentViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextPaymentViVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextPaymentViVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextPaymentViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentViVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextPaymentViVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextPaymentViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentViVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentViViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentViVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextPaymentViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextPaymentViViWithPatch() throws Exception {
        // Initialize the database
        insertedNextPaymentViVi = nextPaymentViViRepository.saveAndFlush(nextPaymentViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextPaymentViVi using partial update
        NextPaymentViVi partialUpdatedNextPaymentViVi = new NextPaymentViVi();
        partialUpdatedNextPaymentViVi.setId(nextPaymentViVi.getId());

        partialUpdatedNextPaymentViVi.paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restNextPaymentViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextPaymentViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextPaymentViVi))
            )
            .andExpect(status().isOk());

        // Validate the NextPaymentViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextPaymentViViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextPaymentViVi, nextPaymentViVi),
            getPersistedNextPaymentViVi(nextPaymentViVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextPaymentViViWithPatch() throws Exception {
        // Initialize the database
        insertedNextPaymentViVi = nextPaymentViViRepository.saveAndFlush(nextPaymentViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextPaymentViVi using partial update
        NextPaymentViVi partialUpdatedNextPaymentViVi = new NextPaymentViVi();
        partialUpdatedNextPaymentViVi.setId(nextPaymentViVi.getId());

        partialUpdatedNextPaymentViVi.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restNextPaymentViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextPaymentViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextPaymentViVi))
            )
            .andExpect(status().isOk());

        // Validate the NextPaymentViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextPaymentViViUpdatableFieldsEquals(
            partialUpdatedNextPaymentViVi,
            getPersistedNextPaymentViVi(partialUpdatedNextPaymentViVi)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextPaymentViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentViVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextPaymentViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextPaymentViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextPaymentViVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextPaymentViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentViVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextPaymentViVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextPaymentViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentViVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentViViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextPaymentViVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextPaymentViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextPaymentViVi() throws Exception {
        // Initialize the database
        insertedNextPaymentViVi = nextPaymentViViRepository.saveAndFlush(nextPaymentViVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextPaymentViVi
        restNextPaymentViViMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextPaymentViVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextPaymentViViRepository.count();
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

    protected NextPaymentViVi getPersistedNextPaymentViVi(NextPaymentViVi nextPaymentViVi) {
        return nextPaymentViViRepository.findById(nextPaymentViVi.getId()).orElseThrow();
    }

    protected void assertPersistedNextPaymentViViToMatchAllProperties(NextPaymentViVi expectedNextPaymentViVi) {
        assertNextPaymentViViAllPropertiesEquals(expectedNextPaymentViVi, getPersistedNextPaymentViVi(expectedNextPaymentViVi));
    }

    protected void assertPersistedNextPaymentViViToMatchUpdatableProperties(NextPaymentViVi expectedNextPaymentViVi) {
        assertNextPaymentViViAllUpdatablePropertiesEquals(expectedNextPaymentViVi, getPersistedNextPaymentViVi(expectedNextPaymentViVi));
    }
}
