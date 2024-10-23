package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.PaymentSigmaAsserts.*;
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
import xyz.jhmapstruct.domain.PaymentSigma;
import xyz.jhmapstruct.domain.enumeration.PaymentSigmaMethod;
import xyz.jhmapstruct.repository.PaymentSigmaRepository;

/**
 * Integration tests for the {@link PaymentSigmaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentSigmaResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final Instant DEFAULT_PAYMENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYMENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final PaymentSigmaMethod DEFAULT_PAYMENT_METHOD = PaymentSigmaMethod.CREDIT_CARD;
    private static final PaymentSigmaMethod UPDATED_PAYMENT_METHOD = PaymentSigmaMethod.PAYPAL;

    private static final String ENTITY_API_URL = "/api/payment-sigmas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaymentSigmaRepository paymentSigmaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentSigmaMockMvc;

    private PaymentSigma paymentSigma;

    private PaymentSigma insertedPaymentSigma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentSigma createEntity() {
        return new PaymentSigma().amount(DEFAULT_AMOUNT).paymentDate(DEFAULT_PAYMENT_DATE).paymentMethod(DEFAULT_PAYMENT_METHOD);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentSigma createUpdatedEntity() {
        return new PaymentSigma().amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);
    }

    @BeforeEach
    public void initTest() {
        paymentSigma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPaymentSigma != null) {
            paymentSigmaRepository.delete(insertedPaymentSigma);
            insertedPaymentSigma = null;
        }
    }

    @Test
    @Transactional
    void createPaymentSigma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PaymentSigma
        var returnedPaymentSigma = om.readValue(
            restPaymentSigmaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentSigma)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PaymentSigma.class
        );

        // Validate the PaymentSigma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPaymentSigmaUpdatableFieldsEquals(returnedPaymentSigma, getPersistedPaymentSigma(returnedPaymentSigma));

        insertedPaymentSigma = returnedPaymentSigma;
    }

    @Test
    @Transactional
    void createPaymentSigmaWithExistingId() throws Exception {
        // Create the PaymentSigma with an existing ID
        paymentSigma.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentSigma)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentSigma.setAmount(null);

        // Create the PaymentSigma, which fails.

        restPaymentSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentSigma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentSigma.setPaymentDate(null);

        // Create the PaymentSigma, which fails.

        restPaymentSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentSigma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaymentSigmas() throws Exception {
        // Initialize the database
        insertedPaymentSigma = paymentSigmaRepository.saveAndFlush(paymentSigma);

        // Get all the paymentSigmaList
        restPaymentSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentSigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));
    }

    @Test
    @Transactional
    void getPaymentSigma() throws Exception {
        // Initialize the database
        insertedPaymentSigma = paymentSigmaRepository.saveAndFlush(paymentSigma);

        // Get the paymentSigma
        restPaymentSigmaMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentSigma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentSigma.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()));
    }

    @Test
    @Transactional
    void getPaymentSigmasByIdFiltering() throws Exception {
        // Initialize the database
        insertedPaymentSigma = paymentSigmaRepository.saveAndFlush(paymentSigma);

        Long id = paymentSigma.getId();

        defaultPaymentSigmaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPaymentSigmaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPaymentSigmaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaymentSigmasByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentSigma = paymentSigmaRepository.saveAndFlush(paymentSigma);

        // Get all the paymentSigmaList where amount equals to
        defaultPaymentSigmaFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentSigmasByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentSigma = paymentSigmaRepository.saveAndFlush(paymentSigma);

        // Get all the paymentSigmaList where amount in
        defaultPaymentSigmaFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentSigmasByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentSigma = paymentSigmaRepository.saveAndFlush(paymentSigma);

        // Get all the paymentSigmaList where amount is not null
        defaultPaymentSigmaFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentSigmasByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentSigma = paymentSigmaRepository.saveAndFlush(paymentSigma);

        // Get all the paymentSigmaList where amount is greater than or equal to
        defaultPaymentSigmaFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentSigmasByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentSigma = paymentSigmaRepository.saveAndFlush(paymentSigma);

        // Get all the paymentSigmaList where amount is less than or equal to
        defaultPaymentSigmaFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentSigmasByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPaymentSigma = paymentSigmaRepository.saveAndFlush(paymentSigma);

        // Get all the paymentSigmaList where amount is less than
        defaultPaymentSigmaFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentSigmasByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPaymentSigma = paymentSigmaRepository.saveAndFlush(paymentSigma);

        // Get all the paymentSigmaList where amount is greater than
        defaultPaymentSigmaFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentSigmasByPaymentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentSigma = paymentSigmaRepository.saveAndFlush(paymentSigma);

        // Get all the paymentSigmaList where paymentDate equals to
        defaultPaymentSigmaFiltering("paymentDate.equals=" + DEFAULT_PAYMENT_DATE, "paymentDate.equals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentSigmasByPaymentDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentSigma = paymentSigmaRepository.saveAndFlush(paymentSigma);

        // Get all the paymentSigmaList where paymentDate in
        defaultPaymentSigmaFiltering(
            "paymentDate.in=" + DEFAULT_PAYMENT_DATE + "," + UPDATED_PAYMENT_DATE,
            "paymentDate.in=" + UPDATED_PAYMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllPaymentSigmasByPaymentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentSigma = paymentSigmaRepository.saveAndFlush(paymentSigma);

        // Get all the paymentSigmaList where paymentDate is not null
        defaultPaymentSigmaFiltering("paymentDate.specified=true", "paymentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentSigmasByPaymentMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentSigma = paymentSigmaRepository.saveAndFlush(paymentSigma);

        // Get all the paymentSigmaList where paymentMethod equals to
        defaultPaymentSigmaFiltering("paymentMethod.equals=" + DEFAULT_PAYMENT_METHOD, "paymentMethod.equals=" + UPDATED_PAYMENT_METHOD);
    }

    @Test
    @Transactional
    void getAllPaymentSigmasByPaymentMethodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentSigma = paymentSigmaRepository.saveAndFlush(paymentSigma);

        // Get all the paymentSigmaList where paymentMethod in
        defaultPaymentSigmaFiltering(
            "paymentMethod.in=" + DEFAULT_PAYMENT_METHOD + "," + UPDATED_PAYMENT_METHOD,
            "paymentMethod.in=" + UPDATED_PAYMENT_METHOD
        );
    }

    @Test
    @Transactional
    void getAllPaymentSigmasByPaymentMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentSigma = paymentSigmaRepository.saveAndFlush(paymentSigma);

        // Get all the paymentSigmaList where paymentMethod is not null
        defaultPaymentSigmaFiltering("paymentMethod.specified=true", "paymentMethod.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentSigmasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            paymentSigmaRepository.saveAndFlush(paymentSigma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        paymentSigma.setTenant(tenant);
        paymentSigmaRepository.saveAndFlush(paymentSigma);
        Long tenantId = tenant.getId();
        // Get all the paymentSigmaList where tenant equals to tenantId
        defaultPaymentSigmaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the paymentSigmaList where tenant equals to (tenantId + 1)
        defaultPaymentSigmaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultPaymentSigmaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPaymentSigmaShouldBeFound(shouldBeFound);
        defaultPaymentSigmaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentSigmaShouldBeFound(String filter) throws Exception {
        restPaymentSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentSigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));

        // Check, that the count call also returns 1
        restPaymentSigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentSigmaShouldNotBeFound(String filter) throws Exception {
        restPaymentSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentSigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPaymentSigma() throws Exception {
        // Get the paymentSigma
        restPaymentSigmaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaymentSigma() throws Exception {
        // Initialize the database
        insertedPaymentSigma = paymentSigmaRepository.saveAndFlush(paymentSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentSigma
        PaymentSigma updatedPaymentSigma = paymentSigmaRepository.findById(paymentSigma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPaymentSigma are not directly saved in db
        em.detach(updatedPaymentSigma);
        updatedPaymentSigma.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restPaymentSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPaymentSigma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPaymentSigma))
            )
            .andExpect(status().isOk());

        // Validate the PaymentSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaymentSigmaToMatchAllProperties(updatedPaymentSigma);
    }

    @Test
    @Transactional
    void putNonExistingPaymentSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentSigma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentSigma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentSigma))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentSigma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentSigma))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentSigma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentSigmaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentSigma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentSigmaWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentSigma = paymentSigmaRepository.saveAndFlush(paymentSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentSigma using partial update
        PaymentSigma partialUpdatedPaymentSigma = new PaymentSigma();
        partialUpdatedPaymentSigma.setId(paymentSigma.getId());

        partialUpdatedPaymentSigma.paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restPaymentSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentSigma))
            )
            .andExpect(status().isOk());

        // Validate the PaymentSigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentSigmaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPaymentSigma, paymentSigma),
            getPersistedPaymentSigma(paymentSigma)
        );
    }

    @Test
    @Transactional
    void fullUpdatePaymentSigmaWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentSigma = paymentSigmaRepository.saveAndFlush(paymentSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentSigma using partial update
        PaymentSigma partialUpdatedPaymentSigma = new PaymentSigma();
        partialUpdatedPaymentSigma.setId(paymentSigma.getId());

        partialUpdatedPaymentSigma.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restPaymentSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentSigma))
            )
            .andExpect(status().isOk());

        // Validate the PaymentSigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentSigmaUpdatableFieldsEquals(partialUpdatedPaymentSigma, getPersistedPaymentSigma(partialUpdatedPaymentSigma));
    }

    @Test
    @Transactional
    void patchNonExistingPaymentSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentSigma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentSigma))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentSigma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentSigma))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentSigma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentSigmaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paymentSigma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaymentSigma() throws Exception {
        // Initialize the database
        insertedPaymentSigma = paymentSigmaRepository.saveAndFlush(paymentSigma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the paymentSigma
        restPaymentSigmaMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentSigma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paymentSigmaRepository.count();
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

    protected PaymentSigma getPersistedPaymentSigma(PaymentSigma paymentSigma) {
        return paymentSigmaRepository.findById(paymentSigma.getId()).orElseThrow();
    }

    protected void assertPersistedPaymentSigmaToMatchAllProperties(PaymentSigma expectedPaymentSigma) {
        assertPaymentSigmaAllPropertiesEquals(expectedPaymentSigma, getPersistedPaymentSigma(expectedPaymentSigma));
    }

    protected void assertPersistedPaymentSigmaToMatchUpdatableProperties(PaymentSigma expectedPaymentSigma) {
        assertPaymentSigmaAllUpdatablePropertiesEquals(expectedPaymentSigma, getPersistedPaymentSigma(expectedPaymentSigma));
    }
}
