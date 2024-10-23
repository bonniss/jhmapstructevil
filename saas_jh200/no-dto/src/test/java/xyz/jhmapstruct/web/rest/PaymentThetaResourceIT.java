package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.PaymentThetaAsserts.*;
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
import xyz.jhmapstruct.domain.PaymentTheta;
import xyz.jhmapstruct.domain.enumeration.PaymentThetaMethod;
import xyz.jhmapstruct.repository.PaymentThetaRepository;

/**
 * Integration tests for the {@link PaymentThetaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentThetaResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final Instant DEFAULT_PAYMENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYMENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final PaymentThetaMethod DEFAULT_PAYMENT_METHOD = PaymentThetaMethod.CREDIT_CARD;
    private static final PaymentThetaMethod UPDATED_PAYMENT_METHOD = PaymentThetaMethod.PAYPAL;

    private static final String ENTITY_API_URL = "/api/payment-thetas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaymentThetaRepository paymentThetaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentThetaMockMvc;

    private PaymentTheta paymentTheta;

    private PaymentTheta insertedPaymentTheta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentTheta createEntity() {
        return new PaymentTheta().amount(DEFAULT_AMOUNT).paymentDate(DEFAULT_PAYMENT_DATE).paymentMethod(DEFAULT_PAYMENT_METHOD);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentTheta createUpdatedEntity() {
        return new PaymentTheta().amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);
    }

    @BeforeEach
    public void initTest() {
        paymentTheta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPaymentTheta != null) {
            paymentThetaRepository.delete(insertedPaymentTheta);
            insertedPaymentTheta = null;
        }
    }

    @Test
    @Transactional
    void createPaymentTheta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PaymentTheta
        var returnedPaymentTheta = om.readValue(
            restPaymentThetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentTheta)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PaymentTheta.class
        );

        // Validate the PaymentTheta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPaymentThetaUpdatableFieldsEquals(returnedPaymentTheta, getPersistedPaymentTheta(returnedPaymentTheta));

        insertedPaymentTheta = returnedPaymentTheta;
    }

    @Test
    @Transactional
    void createPaymentThetaWithExistingId() throws Exception {
        // Create the PaymentTheta with an existing ID
        paymentTheta.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentTheta)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentTheta.setAmount(null);

        // Create the PaymentTheta, which fails.

        restPaymentThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentTheta)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentTheta.setPaymentDate(null);

        // Create the PaymentTheta, which fails.

        restPaymentThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentTheta)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaymentThetas() throws Exception {
        // Initialize the database
        insertedPaymentTheta = paymentThetaRepository.saveAndFlush(paymentTheta);

        // Get all the paymentThetaList
        restPaymentThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));
    }

    @Test
    @Transactional
    void getPaymentTheta() throws Exception {
        // Initialize the database
        insertedPaymentTheta = paymentThetaRepository.saveAndFlush(paymentTheta);

        // Get the paymentTheta
        restPaymentThetaMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentTheta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentTheta.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()));
    }

    @Test
    @Transactional
    void getPaymentThetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedPaymentTheta = paymentThetaRepository.saveAndFlush(paymentTheta);

        Long id = paymentTheta.getId();

        defaultPaymentThetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPaymentThetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPaymentThetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaymentThetasByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentTheta = paymentThetaRepository.saveAndFlush(paymentTheta);

        // Get all the paymentThetaList where amount equals to
        defaultPaymentThetaFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentThetasByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentTheta = paymentThetaRepository.saveAndFlush(paymentTheta);

        // Get all the paymentThetaList where amount in
        defaultPaymentThetaFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentThetasByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentTheta = paymentThetaRepository.saveAndFlush(paymentTheta);

        // Get all the paymentThetaList where amount is not null
        defaultPaymentThetaFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentThetasByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentTheta = paymentThetaRepository.saveAndFlush(paymentTheta);

        // Get all the paymentThetaList where amount is greater than or equal to
        defaultPaymentThetaFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentThetasByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentTheta = paymentThetaRepository.saveAndFlush(paymentTheta);

        // Get all the paymentThetaList where amount is less than or equal to
        defaultPaymentThetaFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentThetasByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPaymentTheta = paymentThetaRepository.saveAndFlush(paymentTheta);

        // Get all the paymentThetaList where amount is less than
        defaultPaymentThetaFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentThetasByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPaymentTheta = paymentThetaRepository.saveAndFlush(paymentTheta);

        // Get all the paymentThetaList where amount is greater than
        defaultPaymentThetaFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentThetasByPaymentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentTheta = paymentThetaRepository.saveAndFlush(paymentTheta);

        // Get all the paymentThetaList where paymentDate equals to
        defaultPaymentThetaFiltering("paymentDate.equals=" + DEFAULT_PAYMENT_DATE, "paymentDate.equals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentThetasByPaymentDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentTheta = paymentThetaRepository.saveAndFlush(paymentTheta);

        // Get all the paymentThetaList where paymentDate in
        defaultPaymentThetaFiltering(
            "paymentDate.in=" + DEFAULT_PAYMENT_DATE + "," + UPDATED_PAYMENT_DATE,
            "paymentDate.in=" + UPDATED_PAYMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllPaymentThetasByPaymentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentTheta = paymentThetaRepository.saveAndFlush(paymentTheta);

        // Get all the paymentThetaList where paymentDate is not null
        defaultPaymentThetaFiltering("paymentDate.specified=true", "paymentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentThetasByPaymentMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentTheta = paymentThetaRepository.saveAndFlush(paymentTheta);

        // Get all the paymentThetaList where paymentMethod equals to
        defaultPaymentThetaFiltering("paymentMethod.equals=" + DEFAULT_PAYMENT_METHOD, "paymentMethod.equals=" + UPDATED_PAYMENT_METHOD);
    }

    @Test
    @Transactional
    void getAllPaymentThetasByPaymentMethodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentTheta = paymentThetaRepository.saveAndFlush(paymentTheta);

        // Get all the paymentThetaList where paymentMethod in
        defaultPaymentThetaFiltering(
            "paymentMethod.in=" + DEFAULT_PAYMENT_METHOD + "," + UPDATED_PAYMENT_METHOD,
            "paymentMethod.in=" + UPDATED_PAYMENT_METHOD
        );
    }

    @Test
    @Transactional
    void getAllPaymentThetasByPaymentMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentTheta = paymentThetaRepository.saveAndFlush(paymentTheta);

        // Get all the paymentThetaList where paymentMethod is not null
        defaultPaymentThetaFiltering("paymentMethod.specified=true", "paymentMethod.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentThetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            paymentThetaRepository.saveAndFlush(paymentTheta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        paymentTheta.setTenant(tenant);
        paymentThetaRepository.saveAndFlush(paymentTheta);
        Long tenantId = tenant.getId();
        // Get all the paymentThetaList where tenant equals to tenantId
        defaultPaymentThetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the paymentThetaList where tenant equals to (tenantId + 1)
        defaultPaymentThetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultPaymentThetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPaymentThetaShouldBeFound(shouldBeFound);
        defaultPaymentThetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentThetaShouldBeFound(String filter) throws Exception {
        restPaymentThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));

        // Check, that the count call also returns 1
        restPaymentThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentThetaShouldNotBeFound(String filter) throws Exception {
        restPaymentThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPaymentTheta() throws Exception {
        // Get the paymentTheta
        restPaymentThetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaymentTheta() throws Exception {
        // Initialize the database
        insertedPaymentTheta = paymentThetaRepository.saveAndFlush(paymentTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentTheta
        PaymentTheta updatedPaymentTheta = paymentThetaRepository.findById(paymentTheta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPaymentTheta are not directly saved in db
        em.detach(updatedPaymentTheta);
        updatedPaymentTheta.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restPaymentThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPaymentTheta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPaymentTheta))
            )
            .andExpect(status().isOk());

        // Validate the PaymentTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaymentThetaToMatchAllProperties(updatedPaymentTheta);
    }

    @Test
    @Transactional
    void putNonExistingPaymentTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentTheta.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentTheta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentTheta))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentTheta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentTheta))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentTheta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentThetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentTheta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentThetaWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentTheta = paymentThetaRepository.saveAndFlush(paymentTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentTheta using partial update
        PaymentTheta partialUpdatedPaymentTheta = new PaymentTheta();
        partialUpdatedPaymentTheta.setId(paymentTheta.getId());

        partialUpdatedPaymentTheta.paymentDate(UPDATED_PAYMENT_DATE);

        restPaymentThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentTheta))
            )
            .andExpect(status().isOk());

        // Validate the PaymentTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentThetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPaymentTheta, paymentTheta),
            getPersistedPaymentTheta(paymentTheta)
        );
    }

    @Test
    @Transactional
    void fullUpdatePaymentThetaWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentTheta = paymentThetaRepository.saveAndFlush(paymentTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentTheta using partial update
        PaymentTheta partialUpdatedPaymentTheta = new PaymentTheta();
        partialUpdatedPaymentTheta.setId(paymentTheta.getId());

        partialUpdatedPaymentTheta.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restPaymentThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentTheta))
            )
            .andExpect(status().isOk());

        // Validate the PaymentTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentThetaUpdatableFieldsEquals(partialUpdatedPaymentTheta, getPersistedPaymentTheta(partialUpdatedPaymentTheta));
    }

    @Test
    @Transactional
    void patchNonExistingPaymentTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentTheta.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentTheta))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentTheta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentTheta))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentTheta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentThetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paymentTheta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaymentTheta() throws Exception {
        // Initialize the database
        insertedPaymentTheta = paymentThetaRepository.saveAndFlush(paymentTheta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the paymentTheta
        restPaymentThetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentTheta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paymentThetaRepository.count();
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

    protected PaymentTheta getPersistedPaymentTheta(PaymentTheta paymentTheta) {
        return paymentThetaRepository.findById(paymentTheta.getId()).orElseThrow();
    }

    protected void assertPersistedPaymentThetaToMatchAllProperties(PaymentTheta expectedPaymentTheta) {
        assertPaymentThetaAllPropertiesEquals(expectedPaymentTheta, getPersistedPaymentTheta(expectedPaymentTheta));
    }

    protected void assertPersistedPaymentThetaToMatchUpdatableProperties(PaymentTheta expectedPaymentTheta) {
        assertPaymentThetaAllUpdatablePropertiesEquals(expectedPaymentTheta, getPersistedPaymentTheta(expectedPaymentTheta));
    }
}
