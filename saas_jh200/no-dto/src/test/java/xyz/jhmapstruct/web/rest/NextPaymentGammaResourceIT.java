package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextPaymentGammaAsserts.*;
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
import xyz.jhmapstruct.domain.NextPaymentGamma;
import xyz.jhmapstruct.domain.enumeration.NextPaymentGammaMethod;
import xyz.jhmapstruct.repository.NextPaymentGammaRepository;

/**
 * Integration tests for the {@link NextPaymentGammaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextPaymentGammaResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final Instant DEFAULT_PAYMENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYMENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final NextPaymentGammaMethod DEFAULT_PAYMENT_METHOD = NextPaymentGammaMethod.CREDIT_CARD;
    private static final NextPaymentGammaMethod UPDATED_PAYMENT_METHOD = NextPaymentGammaMethod.PAYPAL;

    private static final String ENTITY_API_URL = "/api/next-payment-gammas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextPaymentGammaRepository nextPaymentGammaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextPaymentGammaMockMvc;

    private NextPaymentGamma nextPaymentGamma;

    private NextPaymentGamma insertedNextPaymentGamma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextPaymentGamma createEntity() {
        return new NextPaymentGamma().amount(DEFAULT_AMOUNT).paymentDate(DEFAULT_PAYMENT_DATE).paymentMethod(DEFAULT_PAYMENT_METHOD);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextPaymentGamma createUpdatedEntity() {
        return new NextPaymentGamma().amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);
    }

    @BeforeEach
    public void initTest() {
        nextPaymentGamma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextPaymentGamma != null) {
            nextPaymentGammaRepository.delete(insertedNextPaymentGamma);
            insertedNextPaymentGamma = null;
        }
    }

    @Test
    @Transactional
    void createNextPaymentGamma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextPaymentGamma
        var returnedNextPaymentGamma = om.readValue(
            restNextPaymentGammaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentGamma)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextPaymentGamma.class
        );

        // Validate the NextPaymentGamma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextPaymentGammaUpdatableFieldsEquals(returnedNextPaymentGamma, getPersistedNextPaymentGamma(returnedNextPaymentGamma));

        insertedNextPaymentGamma = returnedNextPaymentGamma;
    }

    @Test
    @Transactional
    void createNextPaymentGammaWithExistingId() throws Exception {
        // Create the NextPaymentGamma with an existing ID
        nextPaymentGamma.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextPaymentGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentGamma)))
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextPaymentGamma.setAmount(null);

        // Create the NextPaymentGamma, which fails.

        restNextPaymentGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentGamma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextPaymentGamma.setPaymentDate(null);

        // Create the NextPaymentGamma, which fails.

        restNextPaymentGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentGamma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextPaymentGammas() throws Exception {
        // Initialize the database
        insertedNextPaymentGamma = nextPaymentGammaRepository.saveAndFlush(nextPaymentGamma);

        // Get all the nextPaymentGammaList
        restNextPaymentGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextPaymentGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));
    }

    @Test
    @Transactional
    void getNextPaymentGamma() throws Exception {
        // Initialize the database
        insertedNextPaymentGamma = nextPaymentGammaRepository.saveAndFlush(nextPaymentGamma);

        // Get the nextPaymentGamma
        restNextPaymentGammaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextPaymentGamma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextPaymentGamma.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()));
    }

    @Test
    @Transactional
    void getNextPaymentGammasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextPaymentGamma = nextPaymentGammaRepository.saveAndFlush(nextPaymentGamma);

        Long id = nextPaymentGamma.getId();

        defaultNextPaymentGammaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextPaymentGammaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextPaymentGammaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextPaymentGammasByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentGamma = nextPaymentGammaRepository.saveAndFlush(nextPaymentGamma);

        // Get all the nextPaymentGammaList where amount equals to
        defaultNextPaymentGammaFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentGammasByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextPaymentGamma = nextPaymentGammaRepository.saveAndFlush(nextPaymentGamma);

        // Get all the nextPaymentGammaList where amount in
        defaultNextPaymentGammaFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentGammasByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextPaymentGamma = nextPaymentGammaRepository.saveAndFlush(nextPaymentGamma);

        // Get all the nextPaymentGammaList where amount is not null
        defaultNextPaymentGammaFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllNextPaymentGammasByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentGamma = nextPaymentGammaRepository.saveAndFlush(nextPaymentGamma);

        // Get all the nextPaymentGammaList where amount is greater than or equal to
        defaultNextPaymentGammaFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentGammasByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentGamma = nextPaymentGammaRepository.saveAndFlush(nextPaymentGamma);

        // Get all the nextPaymentGammaList where amount is less than or equal to
        defaultNextPaymentGammaFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentGammasByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentGamma = nextPaymentGammaRepository.saveAndFlush(nextPaymentGamma);

        // Get all the nextPaymentGammaList where amount is less than
        defaultNextPaymentGammaFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentGammasByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentGamma = nextPaymentGammaRepository.saveAndFlush(nextPaymentGamma);

        // Get all the nextPaymentGammaList where amount is greater than
        defaultNextPaymentGammaFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentGammasByPaymentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentGamma = nextPaymentGammaRepository.saveAndFlush(nextPaymentGamma);

        // Get all the nextPaymentGammaList where paymentDate equals to
        defaultNextPaymentGammaFiltering("paymentDate.equals=" + DEFAULT_PAYMENT_DATE, "paymentDate.equals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllNextPaymentGammasByPaymentDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextPaymentGamma = nextPaymentGammaRepository.saveAndFlush(nextPaymentGamma);

        // Get all the nextPaymentGammaList where paymentDate in
        defaultNextPaymentGammaFiltering(
            "paymentDate.in=" + DEFAULT_PAYMENT_DATE + "," + UPDATED_PAYMENT_DATE,
            "paymentDate.in=" + UPDATED_PAYMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextPaymentGammasByPaymentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextPaymentGamma = nextPaymentGammaRepository.saveAndFlush(nextPaymentGamma);

        // Get all the nextPaymentGammaList where paymentDate is not null
        defaultNextPaymentGammaFiltering("paymentDate.specified=true", "paymentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextPaymentGammasByPaymentMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentGamma = nextPaymentGammaRepository.saveAndFlush(nextPaymentGamma);

        // Get all the nextPaymentGammaList where paymentMethod equals to
        defaultNextPaymentGammaFiltering(
            "paymentMethod.equals=" + DEFAULT_PAYMENT_METHOD,
            "paymentMethod.equals=" + UPDATED_PAYMENT_METHOD
        );
    }

    @Test
    @Transactional
    void getAllNextPaymentGammasByPaymentMethodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextPaymentGamma = nextPaymentGammaRepository.saveAndFlush(nextPaymentGamma);

        // Get all the nextPaymentGammaList where paymentMethod in
        defaultNextPaymentGammaFiltering(
            "paymentMethod.in=" + DEFAULT_PAYMENT_METHOD + "," + UPDATED_PAYMENT_METHOD,
            "paymentMethod.in=" + UPDATED_PAYMENT_METHOD
        );
    }

    @Test
    @Transactional
    void getAllNextPaymentGammasByPaymentMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextPaymentGamma = nextPaymentGammaRepository.saveAndFlush(nextPaymentGamma);

        // Get all the nextPaymentGammaList where paymentMethod is not null
        defaultNextPaymentGammaFiltering("paymentMethod.specified=true", "paymentMethod.specified=false");
    }

    @Test
    @Transactional
    void getAllNextPaymentGammasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextPaymentGammaRepository.saveAndFlush(nextPaymentGamma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextPaymentGamma.setTenant(tenant);
        nextPaymentGammaRepository.saveAndFlush(nextPaymentGamma);
        Long tenantId = tenant.getId();
        // Get all the nextPaymentGammaList where tenant equals to tenantId
        defaultNextPaymentGammaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextPaymentGammaList where tenant equals to (tenantId + 1)
        defaultNextPaymentGammaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextPaymentGammaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextPaymentGammaShouldBeFound(shouldBeFound);
        defaultNextPaymentGammaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextPaymentGammaShouldBeFound(String filter) throws Exception {
        restNextPaymentGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextPaymentGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));

        // Check, that the count call also returns 1
        restNextPaymentGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextPaymentGammaShouldNotBeFound(String filter) throws Exception {
        restNextPaymentGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextPaymentGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextPaymentGamma() throws Exception {
        // Get the nextPaymentGamma
        restNextPaymentGammaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextPaymentGamma() throws Exception {
        // Initialize the database
        insertedNextPaymentGamma = nextPaymentGammaRepository.saveAndFlush(nextPaymentGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextPaymentGamma
        NextPaymentGamma updatedNextPaymentGamma = nextPaymentGammaRepository.findById(nextPaymentGamma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextPaymentGamma are not directly saved in db
        em.detach(updatedNextPaymentGamma);
        updatedNextPaymentGamma.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restNextPaymentGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextPaymentGamma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextPaymentGamma))
            )
            .andExpect(status().isOk());

        // Validate the NextPaymentGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextPaymentGammaToMatchAllProperties(updatedNextPaymentGamma);
    }

    @Test
    @Transactional
    void putNonExistingNextPaymentGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentGamma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextPaymentGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextPaymentGamma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextPaymentGamma))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextPaymentGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentGamma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextPaymentGamma))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextPaymentGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentGamma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentGammaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentGamma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextPaymentGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextPaymentGammaWithPatch() throws Exception {
        // Initialize the database
        insertedNextPaymentGamma = nextPaymentGammaRepository.saveAndFlush(nextPaymentGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextPaymentGamma using partial update
        NextPaymentGamma partialUpdatedNextPaymentGamma = new NextPaymentGamma();
        partialUpdatedNextPaymentGamma.setId(nextPaymentGamma.getId());

        partialUpdatedNextPaymentGamma.paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restNextPaymentGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextPaymentGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextPaymentGamma))
            )
            .andExpect(status().isOk());

        // Validate the NextPaymentGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextPaymentGammaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextPaymentGamma, nextPaymentGamma),
            getPersistedNextPaymentGamma(nextPaymentGamma)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextPaymentGammaWithPatch() throws Exception {
        // Initialize the database
        insertedNextPaymentGamma = nextPaymentGammaRepository.saveAndFlush(nextPaymentGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextPaymentGamma using partial update
        NextPaymentGamma partialUpdatedNextPaymentGamma = new NextPaymentGamma();
        partialUpdatedNextPaymentGamma.setId(nextPaymentGamma.getId());

        partialUpdatedNextPaymentGamma.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restNextPaymentGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextPaymentGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextPaymentGamma))
            )
            .andExpect(status().isOk());

        // Validate the NextPaymentGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextPaymentGammaUpdatableFieldsEquals(
            partialUpdatedNextPaymentGamma,
            getPersistedNextPaymentGamma(partialUpdatedNextPaymentGamma)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextPaymentGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentGamma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextPaymentGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextPaymentGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextPaymentGamma))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextPaymentGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentGamma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextPaymentGamma))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextPaymentGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentGamma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentGammaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextPaymentGamma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextPaymentGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextPaymentGamma() throws Exception {
        // Initialize the database
        insertedNextPaymentGamma = nextPaymentGammaRepository.saveAndFlush(nextPaymentGamma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextPaymentGamma
        restNextPaymentGammaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextPaymentGamma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextPaymentGammaRepository.count();
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

    protected NextPaymentGamma getPersistedNextPaymentGamma(NextPaymentGamma nextPaymentGamma) {
        return nextPaymentGammaRepository.findById(nextPaymentGamma.getId()).orElseThrow();
    }

    protected void assertPersistedNextPaymentGammaToMatchAllProperties(NextPaymentGamma expectedNextPaymentGamma) {
        assertNextPaymentGammaAllPropertiesEquals(expectedNextPaymentGamma, getPersistedNextPaymentGamma(expectedNextPaymentGamma));
    }

    protected void assertPersistedNextPaymentGammaToMatchUpdatableProperties(NextPaymentGamma expectedNextPaymentGamma) {
        assertNextPaymentGammaAllUpdatablePropertiesEquals(
            expectedNextPaymentGamma,
            getPersistedNextPaymentGamma(expectedNextPaymentGamma)
        );
    }
}
