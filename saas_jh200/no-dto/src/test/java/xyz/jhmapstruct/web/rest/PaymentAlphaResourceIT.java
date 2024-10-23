package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.PaymentAlphaAsserts.*;
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
import xyz.jhmapstruct.domain.PaymentAlpha;
import xyz.jhmapstruct.domain.enumeration.PaymentAlphaMethod;
import xyz.jhmapstruct.repository.PaymentAlphaRepository;

/**
 * Integration tests for the {@link PaymentAlphaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentAlphaResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final Instant DEFAULT_PAYMENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYMENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final PaymentAlphaMethod DEFAULT_PAYMENT_METHOD = PaymentAlphaMethod.CREDIT_CARD;
    private static final PaymentAlphaMethod UPDATED_PAYMENT_METHOD = PaymentAlphaMethod.PAYPAL;

    private static final String ENTITY_API_URL = "/api/payment-alphas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaymentAlphaRepository paymentAlphaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentAlphaMockMvc;

    private PaymentAlpha paymentAlpha;

    private PaymentAlpha insertedPaymentAlpha;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentAlpha createEntity() {
        return new PaymentAlpha().amount(DEFAULT_AMOUNT).paymentDate(DEFAULT_PAYMENT_DATE).paymentMethod(DEFAULT_PAYMENT_METHOD);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentAlpha createUpdatedEntity() {
        return new PaymentAlpha().amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);
    }

    @BeforeEach
    public void initTest() {
        paymentAlpha = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPaymentAlpha != null) {
            paymentAlphaRepository.delete(insertedPaymentAlpha);
            insertedPaymentAlpha = null;
        }
    }

    @Test
    @Transactional
    void createPaymentAlpha() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PaymentAlpha
        var returnedPaymentAlpha = om.readValue(
            restPaymentAlphaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentAlpha)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PaymentAlpha.class
        );

        // Validate the PaymentAlpha in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPaymentAlphaUpdatableFieldsEquals(returnedPaymentAlpha, getPersistedPaymentAlpha(returnedPaymentAlpha));

        insertedPaymentAlpha = returnedPaymentAlpha;
    }

    @Test
    @Transactional
    void createPaymentAlphaWithExistingId() throws Exception {
        // Create the PaymentAlpha with an existing ID
        paymentAlpha.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentAlpha)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentAlpha.setAmount(null);

        // Create the PaymentAlpha, which fails.

        restPaymentAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentAlpha)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentAlpha.setPaymentDate(null);

        // Create the PaymentAlpha, which fails.

        restPaymentAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentAlpha)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaymentAlphas() throws Exception {
        // Initialize the database
        insertedPaymentAlpha = paymentAlphaRepository.saveAndFlush(paymentAlpha);

        // Get all the paymentAlphaList
        restPaymentAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));
    }

    @Test
    @Transactional
    void getPaymentAlpha() throws Exception {
        // Initialize the database
        insertedPaymentAlpha = paymentAlphaRepository.saveAndFlush(paymentAlpha);

        // Get the paymentAlpha
        restPaymentAlphaMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentAlpha.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentAlpha.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()));
    }

    @Test
    @Transactional
    void getPaymentAlphasByIdFiltering() throws Exception {
        // Initialize the database
        insertedPaymentAlpha = paymentAlphaRepository.saveAndFlush(paymentAlpha);

        Long id = paymentAlpha.getId();

        defaultPaymentAlphaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPaymentAlphaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPaymentAlphaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaymentAlphasByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentAlpha = paymentAlphaRepository.saveAndFlush(paymentAlpha);

        // Get all the paymentAlphaList where amount equals to
        defaultPaymentAlphaFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentAlphasByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentAlpha = paymentAlphaRepository.saveAndFlush(paymentAlpha);

        // Get all the paymentAlphaList where amount in
        defaultPaymentAlphaFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentAlphasByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentAlpha = paymentAlphaRepository.saveAndFlush(paymentAlpha);

        // Get all the paymentAlphaList where amount is not null
        defaultPaymentAlphaFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentAlphasByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentAlpha = paymentAlphaRepository.saveAndFlush(paymentAlpha);

        // Get all the paymentAlphaList where amount is greater than or equal to
        defaultPaymentAlphaFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentAlphasByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentAlpha = paymentAlphaRepository.saveAndFlush(paymentAlpha);

        // Get all the paymentAlphaList where amount is less than or equal to
        defaultPaymentAlphaFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentAlphasByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPaymentAlpha = paymentAlphaRepository.saveAndFlush(paymentAlpha);

        // Get all the paymentAlphaList where amount is less than
        defaultPaymentAlphaFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentAlphasByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPaymentAlpha = paymentAlphaRepository.saveAndFlush(paymentAlpha);

        // Get all the paymentAlphaList where amount is greater than
        defaultPaymentAlphaFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentAlphasByPaymentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentAlpha = paymentAlphaRepository.saveAndFlush(paymentAlpha);

        // Get all the paymentAlphaList where paymentDate equals to
        defaultPaymentAlphaFiltering("paymentDate.equals=" + DEFAULT_PAYMENT_DATE, "paymentDate.equals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentAlphasByPaymentDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentAlpha = paymentAlphaRepository.saveAndFlush(paymentAlpha);

        // Get all the paymentAlphaList where paymentDate in
        defaultPaymentAlphaFiltering(
            "paymentDate.in=" + DEFAULT_PAYMENT_DATE + "," + UPDATED_PAYMENT_DATE,
            "paymentDate.in=" + UPDATED_PAYMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllPaymentAlphasByPaymentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentAlpha = paymentAlphaRepository.saveAndFlush(paymentAlpha);

        // Get all the paymentAlphaList where paymentDate is not null
        defaultPaymentAlphaFiltering("paymentDate.specified=true", "paymentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentAlphasByPaymentMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentAlpha = paymentAlphaRepository.saveAndFlush(paymentAlpha);

        // Get all the paymentAlphaList where paymentMethod equals to
        defaultPaymentAlphaFiltering("paymentMethod.equals=" + DEFAULT_PAYMENT_METHOD, "paymentMethod.equals=" + UPDATED_PAYMENT_METHOD);
    }

    @Test
    @Transactional
    void getAllPaymentAlphasByPaymentMethodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentAlpha = paymentAlphaRepository.saveAndFlush(paymentAlpha);

        // Get all the paymentAlphaList where paymentMethod in
        defaultPaymentAlphaFiltering(
            "paymentMethod.in=" + DEFAULT_PAYMENT_METHOD + "," + UPDATED_PAYMENT_METHOD,
            "paymentMethod.in=" + UPDATED_PAYMENT_METHOD
        );
    }

    @Test
    @Transactional
    void getAllPaymentAlphasByPaymentMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentAlpha = paymentAlphaRepository.saveAndFlush(paymentAlpha);

        // Get all the paymentAlphaList where paymentMethod is not null
        defaultPaymentAlphaFiltering("paymentMethod.specified=true", "paymentMethod.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentAlphasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            paymentAlphaRepository.saveAndFlush(paymentAlpha);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        paymentAlpha.setTenant(tenant);
        paymentAlphaRepository.saveAndFlush(paymentAlpha);
        Long tenantId = tenant.getId();
        // Get all the paymentAlphaList where tenant equals to tenantId
        defaultPaymentAlphaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the paymentAlphaList where tenant equals to (tenantId + 1)
        defaultPaymentAlphaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultPaymentAlphaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPaymentAlphaShouldBeFound(shouldBeFound);
        defaultPaymentAlphaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentAlphaShouldBeFound(String filter) throws Exception {
        restPaymentAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));

        // Check, that the count call also returns 1
        restPaymentAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentAlphaShouldNotBeFound(String filter) throws Exception {
        restPaymentAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPaymentAlpha() throws Exception {
        // Get the paymentAlpha
        restPaymentAlphaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaymentAlpha() throws Exception {
        // Initialize the database
        insertedPaymentAlpha = paymentAlphaRepository.saveAndFlush(paymentAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentAlpha
        PaymentAlpha updatedPaymentAlpha = paymentAlphaRepository.findById(paymentAlpha.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPaymentAlpha are not directly saved in db
        em.detach(updatedPaymentAlpha);
        updatedPaymentAlpha.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restPaymentAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPaymentAlpha.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPaymentAlpha))
            )
            .andExpect(status().isOk());

        // Validate the PaymentAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaymentAlphaToMatchAllProperties(updatedPaymentAlpha);
    }

    @Test
    @Transactional
    void putNonExistingPaymentAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentAlpha.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentAlpha.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentAlphaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentAlpha)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentAlpha = paymentAlphaRepository.saveAndFlush(paymentAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentAlpha using partial update
        PaymentAlpha partialUpdatedPaymentAlpha = new PaymentAlpha();
        partialUpdatedPaymentAlpha.setId(paymentAlpha.getId());

        partialUpdatedPaymentAlpha.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restPaymentAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentAlpha))
            )
            .andExpect(status().isOk());

        // Validate the PaymentAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentAlphaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPaymentAlpha, paymentAlpha),
            getPersistedPaymentAlpha(paymentAlpha)
        );
    }

    @Test
    @Transactional
    void fullUpdatePaymentAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentAlpha = paymentAlphaRepository.saveAndFlush(paymentAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentAlpha using partial update
        PaymentAlpha partialUpdatedPaymentAlpha = new PaymentAlpha();
        partialUpdatedPaymentAlpha.setId(paymentAlpha.getId());

        partialUpdatedPaymentAlpha.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restPaymentAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentAlpha))
            )
            .andExpect(status().isOk());

        // Validate the PaymentAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentAlphaUpdatableFieldsEquals(partialUpdatedPaymentAlpha, getPersistedPaymentAlpha(partialUpdatedPaymentAlpha));
    }

    @Test
    @Transactional
    void patchNonExistingPaymentAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentAlpha.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentAlphaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paymentAlpha)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaymentAlpha() throws Exception {
        // Initialize the database
        insertedPaymentAlpha = paymentAlphaRepository.saveAndFlush(paymentAlpha);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the paymentAlpha
        restPaymentAlphaMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentAlpha.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paymentAlphaRepository.count();
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

    protected PaymentAlpha getPersistedPaymentAlpha(PaymentAlpha paymentAlpha) {
        return paymentAlphaRepository.findById(paymentAlpha.getId()).orElseThrow();
    }

    protected void assertPersistedPaymentAlphaToMatchAllProperties(PaymentAlpha expectedPaymentAlpha) {
        assertPaymentAlphaAllPropertiesEquals(expectedPaymentAlpha, getPersistedPaymentAlpha(expectedPaymentAlpha));
    }

    protected void assertPersistedPaymentAlphaToMatchUpdatableProperties(PaymentAlpha expectedPaymentAlpha) {
        assertPaymentAlphaAllUpdatablePropertiesEquals(expectedPaymentAlpha, getPersistedPaymentAlpha(expectedPaymentAlpha));
    }
}
