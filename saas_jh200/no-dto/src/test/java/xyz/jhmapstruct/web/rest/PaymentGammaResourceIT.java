package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.PaymentGammaAsserts.*;
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
import xyz.jhmapstruct.domain.PaymentGamma;
import xyz.jhmapstruct.domain.enumeration.PaymentGammaMethod;
import xyz.jhmapstruct.repository.PaymentGammaRepository;

/**
 * Integration tests for the {@link PaymentGammaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentGammaResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final Instant DEFAULT_PAYMENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYMENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final PaymentGammaMethod DEFAULT_PAYMENT_METHOD = PaymentGammaMethod.CREDIT_CARD;
    private static final PaymentGammaMethod UPDATED_PAYMENT_METHOD = PaymentGammaMethod.PAYPAL;

    private static final String ENTITY_API_URL = "/api/payment-gammas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaymentGammaRepository paymentGammaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentGammaMockMvc;

    private PaymentGamma paymentGamma;

    private PaymentGamma insertedPaymentGamma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentGamma createEntity() {
        return new PaymentGamma().amount(DEFAULT_AMOUNT).paymentDate(DEFAULT_PAYMENT_DATE).paymentMethod(DEFAULT_PAYMENT_METHOD);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentGamma createUpdatedEntity() {
        return new PaymentGamma().amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);
    }

    @BeforeEach
    public void initTest() {
        paymentGamma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPaymentGamma != null) {
            paymentGammaRepository.delete(insertedPaymentGamma);
            insertedPaymentGamma = null;
        }
    }

    @Test
    @Transactional
    void createPaymentGamma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PaymentGamma
        var returnedPaymentGamma = om.readValue(
            restPaymentGammaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentGamma)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PaymentGamma.class
        );

        // Validate the PaymentGamma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPaymentGammaUpdatableFieldsEquals(returnedPaymentGamma, getPersistedPaymentGamma(returnedPaymentGamma));

        insertedPaymentGamma = returnedPaymentGamma;
    }

    @Test
    @Transactional
    void createPaymentGammaWithExistingId() throws Exception {
        // Create the PaymentGamma with an existing ID
        paymentGamma.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentGamma)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentGamma.setAmount(null);

        // Create the PaymentGamma, which fails.

        restPaymentGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentGamma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentGamma.setPaymentDate(null);

        // Create the PaymentGamma, which fails.

        restPaymentGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentGamma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaymentGammas() throws Exception {
        // Initialize the database
        insertedPaymentGamma = paymentGammaRepository.saveAndFlush(paymentGamma);

        // Get all the paymentGammaList
        restPaymentGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));
    }

    @Test
    @Transactional
    void getPaymentGamma() throws Exception {
        // Initialize the database
        insertedPaymentGamma = paymentGammaRepository.saveAndFlush(paymentGamma);

        // Get the paymentGamma
        restPaymentGammaMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentGamma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentGamma.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()));
    }

    @Test
    @Transactional
    void getPaymentGammasByIdFiltering() throws Exception {
        // Initialize the database
        insertedPaymentGamma = paymentGammaRepository.saveAndFlush(paymentGamma);

        Long id = paymentGamma.getId();

        defaultPaymentGammaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPaymentGammaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPaymentGammaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaymentGammasByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentGamma = paymentGammaRepository.saveAndFlush(paymentGamma);

        // Get all the paymentGammaList where amount equals to
        defaultPaymentGammaFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentGammasByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentGamma = paymentGammaRepository.saveAndFlush(paymentGamma);

        // Get all the paymentGammaList where amount in
        defaultPaymentGammaFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentGammasByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentGamma = paymentGammaRepository.saveAndFlush(paymentGamma);

        // Get all the paymentGammaList where amount is not null
        defaultPaymentGammaFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentGammasByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentGamma = paymentGammaRepository.saveAndFlush(paymentGamma);

        // Get all the paymentGammaList where amount is greater than or equal to
        defaultPaymentGammaFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentGammasByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentGamma = paymentGammaRepository.saveAndFlush(paymentGamma);

        // Get all the paymentGammaList where amount is less than or equal to
        defaultPaymentGammaFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentGammasByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPaymentGamma = paymentGammaRepository.saveAndFlush(paymentGamma);

        // Get all the paymentGammaList where amount is less than
        defaultPaymentGammaFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentGammasByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPaymentGamma = paymentGammaRepository.saveAndFlush(paymentGamma);

        // Get all the paymentGammaList where amount is greater than
        defaultPaymentGammaFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentGammasByPaymentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentGamma = paymentGammaRepository.saveAndFlush(paymentGamma);

        // Get all the paymentGammaList where paymentDate equals to
        defaultPaymentGammaFiltering("paymentDate.equals=" + DEFAULT_PAYMENT_DATE, "paymentDate.equals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentGammasByPaymentDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentGamma = paymentGammaRepository.saveAndFlush(paymentGamma);

        // Get all the paymentGammaList where paymentDate in
        defaultPaymentGammaFiltering(
            "paymentDate.in=" + DEFAULT_PAYMENT_DATE + "," + UPDATED_PAYMENT_DATE,
            "paymentDate.in=" + UPDATED_PAYMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllPaymentGammasByPaymentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentGamma = paymentGammaRepository.saveAndFlush(paymentGamma);

        // Get all the paymentGammaList where paymentDate is not null
        defaultPaymentGammaFiltering("paymentDate.specified=true", "paymentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentGammasByPaymentMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentGamma = paymentGammaRepository.saveAndFlush(paymentGamma);

        // Get all the paymentGammaList where paymentMethod equals to
        defaultPaymentGammaFiltering("paymentMethod.equals=" + DEFAULT_PAYMENT_METHOD, "paymentMethod.equals=" + UPDATED_PAYMENT_METHOD);
    }

    @Test
    @Transactional
    void getAllPaymentGammasByPaymentMethodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentGamma = paymentGammaRepository.saveAndFlush(paymentGamma);

        // Get all the paymentGammaList where paymentMethod in
        defaultPaymentGammaFiltering(
            "paymentMethod.in=" + DEFAULT_PAYMENT_METHOD + "," + UPDATED_PAYMENT_METHOD,
            "paymentMethod.in=" + UPDATED_PAYMENT_METHOD
        );
    }

    @Test
    @Transactional
    void getAllPaymentGammasByPaymentMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentGamma = paymentGammaRepository.saveAndFlush(paymentGamma);

        // Get all the paymentGammaList where paymentMethod is not null
        defaultPaymentGammaFiltering("paymentMethod.specified=true", "paymentMethod.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentGammasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            paymentGammaRepository.saveAndFlush(paymentGamma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        paymentGamma.setTenant(tenant);
        paymentGammaRepository.saveAndFlush(paymentGamma);
        Long tenantId = tenant.getId();
        // Get all the paymentGammaList where tenant equals to tenantId
        defaultPaymentGammaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the paymentGammaList where tenant equals to (tenantId + 1)
        defaultPaymentGammaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultPaymentGammaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPaymentGammaShouldBeFound(shouldBeFound);
        defaultPaymentGammaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentGammaShouldBeFound(String filter) throws Exception {
        restPaymentGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));

        // Check, that the count call also returns 1
        restPaymentGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentGammaShouldNotBeFound(String filter) throws Exception {
        restPaymentGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPaymentGamma() throws Exception {
        // Get the paymentGamma
        restPaymentGammaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaymentGamma() throws Exception {
        // Initialize the database
        insertedPaymentGamma = paymentGammaRepository.saveAndFlush(paymentGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentGamma
        PaymentGamma updatedPaymentGamma = paymentGammaRepository.findById(paymentGamma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPaymentGamma are not directly saved in db
        em.detach(updatedPaymentGamma);
        updatedPaymentGamma.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restPaymentGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPaymentGamma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPaymentGamma))
            )
            .andExpect(status().isOk());

        // Validate the PaymentGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaymentGammaToMatchAllProperties(updatedPaymentGamma);
    }

    @Test
    @Transactional
    void putNonExistingPaymentGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentGamma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentGamma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentGamma))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentGamma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentGamma))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentGamma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentGammaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentGamma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentGammaWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentGamma = paymentGammaRepository.saveAndFlush(paymentGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentGamma using partial update
        PaymentGamma partialUpdatedPaymentGamma = new PaymentGamma();
        partialUpdatedPaymentGamma.setId(paymentGamma.getId());

        partialUpdatedPaymentGamma.amount(UPDATED_AMOUNT).paymentMethod(UPDATED_PAYMENT_METHOD);

        restPaymentGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentGamma))
            )
            .andExpect(status().isOk());

        // Validate the PaymentGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentGammaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPaymentGamma, paymentGamma),
            getPersistedPaymentGamma(paymentGamma)
        );
    }

    @Test
    @Transactional
    void fullUpdatePaymentGammaWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentGamma = paymentGammaRepository.saveAndFlush(paymentGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentGamma using partial update
        PaymentGamma partialUpdatedPaymentGamma = new PaymentGamma();
        partialUpdatedPaymentGamma.setId(paymentGamma.getId());

        partialUpdatedPaymentGamma.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restPaymentGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentGamma))
            )
            .andExpect(status().isOk());

        // Validate the PaymentGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentGammaUpdatableFieldsEquals(partialUpdatedPaymentGamma, getPersistedPaymentGamma(partialUpdatedPaymentGamma));
    }

    @Test
    @Transactional
    void patchNonExistingPaymentGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentGamma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentGamma))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentGamma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentGamma))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentGamma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentGammaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paymentGamma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaymentGamma() throws Exception {
        // Initialize the database
        insertedPaymentGamma = paymentGammaRepository.saveAndFlush(paymentGamma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the paymentGamma
        restPaymentGammaMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentGamma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paymentGammaRepository.count();
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

    protected PaymentGamma getPersistedPaymentGamma(PaymentGamma paymentGamma) {
        return paymentGammaRepository.findById(paymentGamma.getId()).orElseThrow();
    }

    protected void assertPersistedPaymentGammaToMatchAllProperties(PaymentGamma expectedPaymentGamma) {
        assertPaymentGammaAllPropertiesEquals(expectedPaymentGamma, getPersistedPaymentGamma(expectedPaymentGamma));
    }

    protected void assertPersistedPaymentGammaToMatchUpdatableProperties(PaymentGamma expectedPaymentGamma) {
        assertPaymentGammaAllUpdatablePropertiesEquals(expectedPaymentGamma, getPersistedPaymentGamma(expectedPaymentGamma));
    }
}
