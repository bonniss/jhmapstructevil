package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.PaymentViAsserts.*;
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
import xyz.jhmapstruct.domain.PaymentVi;
import xyz.jhmapstruct.domain.enumeration.PaymentViMethod;
import xyz.jhmapstruct.repository.PaymentViRepository;

/**
 * Integration tests for the {@link PaymentViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentViResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final Instant DEFAULT_PAYMENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYMENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final PaymentViMethod DEFAULT_PAYMENT_METHOD = PaymentViMethod.CREDIT_CARD;
    private static final PaymentViMethod UPDATED_PAYMENT_METHOD = PaymentViMethod.PAYPAL;

    private static final String ENTITY_API_URL = "/api/payment-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaymentViRepository paymentViRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentViMockMvc;

    private PaymentVi paymentVi;

    private PaymentVi insertedPaymentVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentVi createEntity() {
        return new PaymentVi().amount(DEFAULT_AMOUNT).paymentDate(DEFAULT_PAYMENT_DATE).paymentMethod(DEFAULT_PAYMENT_METHOD);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentVi createUpdatedEntity() {
        return new PaymentVi().amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);
    }

    @BeforeEach
    public void initTest() {
        paymentVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPaymentVi != null) {
            paymentViRepository.delete(insertedPaymentVi);
            insertedPaymentVi = null;
        }
    }

    @Test
    @Transactional
    void createPaymentVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PaymentVi
        var returnedPaymentVi = om.readValue(
            restPaymentViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PaymentVi.class
        );

        // Validate the PaymentVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPaymentViUpdatableFieldsEquals(returnedPaymentVi, getPersistedPaymentVi(returnedPaymentVi));

        insertedPaymentVi = returnedPaymentVi;
    }

    @Test
    @Transactional
    void createPaymentViWithExistingId() throws Exception {
        // Create the PaymentVi with an existing ID
        paymentVi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentVi)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentVi.setAmount(null);

        // Create the PaymentVi, which fails.

        restPaymentViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentVi.setPaymentDate(null);

        // Create the PaymentVi, which fails.

        restPaymentViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaymentVis() throws Exception {
        // Initialize the database
        insertedPaymentVi = paymentViRepository.saveAndFlush(paymentVi);

        // Get all the paymentViList
        restPaymentViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));
    }

    @Test
    @Transactional
    void getPaymentVi() throws Exception {
        // Initialize the database
        insertedPaymentVi = paymentViRepository.saveAndFlush(paymentVi);

        // Get the paymentVi
        restPaymentViMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentVi.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()));
    }

    @Test
    @Transactional
    void getPaymentVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedPaymentVi = paymentViRepository.saveAndFlush(paymentVi);

        Long id = paymentVi.getId();

        defaultPaymentViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPaymentViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPaymentViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaymentVisByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentVi = paymentViRepository.saveAndFlush(paymentVi);

        // Get all the paymentViList where amount equals to
        defaultPaymentViFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentVisByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentVi = paymentViRepository.saveAndFlush(paymentVi);

        // Get all the paymentViList where amount in
        defaultPaymentViFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentVisByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentVi = paymentViRepository.saveAndFlush(paymentVi);

        // Get all the paymentViList where amount is not null
        defaultPaymentViFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentVisByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentVi = paymentViRepository.saveAndFlush(paymentVi);

        // Get all the paymentViList where amount is greater than or equal to
        defaultPaymentViFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentVisByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentVi = paymentViRepository.saveAndFlush(paymentVi);

        // Get all the paymentViList where amount is less than or equal to
        defaultPaymentViFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentVisByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPaymentVi = paymentViRepository.saveAndFlush(paymentVi);

        // Get all the paymentViList where amount is less than
        defaultPaymentViFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentVisByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPaymentVi = paymentViRepository.saveAndFlush(paymentVi);

        // Get all the paymentViList where amount is greater than
        defaultPaymentViFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentVisByPaymentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentVi = paymentViRepository.saveAndFlush(paymentVi);

        // Get all the paymentViList where paymentDate equals to
        defaultPaymentViFiltering("paymentDate.equals=" + DEFAULT_PAYMENT_DATE, "paymentDate.equals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentVisByPaymentDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentVi = paymentViRepository.saveAndFlush(paymentVi);

        // Get all the paymentViList where paymentDate in
        defaultPaymentViFiltering(
            "paymentDate.in=" + DEFAULT_PAYMENT_DATE + "," + UPDATED_PAYMENT_DATE,
            "paymentDate.in=" + UPDATED_PAYMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllPaymentVisByPaymentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentVi = paymentViRepository.saveAndFlush(paymentVi);

        // Get all the paymentViList where paymentDate is not null
        defaultPaymentViFiltering("paymentDate.specified=true", "paymentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentVisByPaymentMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentVi = paymentViRepository.saveAndFlush(paymentVi);

        // Get all the paymentViList where paymentMethod equals to
        defaultPaymentViFiltering("paymentMethod.equals=" + DEFAULT_PAYMENT_METHOD, "paymentMethod.equals=" + UPDATED_PAYMENT_METHOD);
    }

    @Test
    @Transactional
    void getAllPaymentVisByPaymentMethodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentVi = paymentViRepository.saveAndFlush(paymentVi);

        // Get all the paymentViList where paymentMethod in
        defaultPaymentViFiltering(
            "paymentMethod.in=" + DEFAULT_PAYMENT_METHOD + "," + UPDATED_PAYMENT_METHOD,
            "paymentMethod.in=" + UPDATED_PAYMENT_METHOD
        );
    }

    @Test
    @Transactional
    void getAllPaymentVisByPaymentMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentVi = paymentViRepository.saveAndFlush(paymentVi);

        // Get all the paymentViList where paymentMethod is not null
        defaultPaymentViFiltering("paymentMethod.specified=true", "paymentMethod.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentVisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            paymentViRepository.saveAndFlush(paymentVi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        paymentVi.setTenant(tenant);
        paymentViRepository.saveAndFlush(paymentVi);
        Long tenantId = tenant.getId();
        // Get all the paymentViList where tenant equals to tenantId
        defaultPaymentViShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the paymentViList where tenant equals to (tenantId + 1)
        defaultPaymentViShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultPaymentViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPaymentViShouldBeFound(shouldBeFound);
        defaultPaymentViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentViShouldBeFound(String filter) throws Exception {
        restPaymentViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));

        // Check, that the count call also returns 1
        restPaymentViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentViShouldNotBeFound(String filter) throws Exception {
        restPaymentViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPaymentVi() throws Exception {
        // Get the paymentVi
        restPaymentViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaymentVi() throws Exception {
        // Initialize the database
        insertedPaymentVi = paymentViRepository.saveAndFlush(paymentVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentVi
        PaymentVi updatedPaymentVi = paymentViRepository.findById(paymentVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPaymentVi are not directly saved in db
        em.detach(updatedPaymentVi);
        updatedPaymentVi.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restPaymentViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPaymentVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPaymentVi))
            )
            .andExpect(status().isOk());

        // Validate the PaymentVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaymentViToMatchAllProperties(updatedPaymentVi);
    }

    @Test
    @Transactional
    void putNonExistingPaymentVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentVi.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentViWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentVi = paymentViRepository.saveAndFlush(paymentVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentVi using partial update
        PaymentVi partialUpdatedPaymentVi = new PaymentVi();
        partialUpdatedPaymentVi.setId(paymentVi.getId());

        restPaymentViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentVi))
            )
            .andExpect(status().isOk());

        // Validate the PaymentVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPaymentVi, paymentVi),
            getPersistedPaymentVi(paymentVi)
        );
    }

    @Test
    @Transactional
    void fullUpdatePaymentViWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentVi = paymentViRepository.saveAndFlush(paymentVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentVi using partial update
        PaymentVi partialUpdatedPaymentVi = new PaymentVi();
        partialUpdatedPaymentVi.setId(paymentVi.getId());

        partialUpdatedPaymentVi.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restPaymentViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentVi))
            )
            .andExpect(status().isOk());

        // Validate the PaymentVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentViUpdatableFieldsEquals(partialUpdatedPaymentVi, getPersistedPaymentVi(partialUpdatedPaymentVi));
    }

    @Test
    @Transactional
    void patchNonExistingPaymentVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paymentVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaymentVi() throws Exception {
        // Initialize the database
        insertedPaymentVi = paymentViRepository.saveAndFlush(paymentVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the paymentVi
        restPaymentViMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paymentViRepository.count();
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

    protected PaymentVi getPersistedPaymentVi(PaymentVi paymentVi) {
        return paymentViRepository.findById(paymentVi.getId()).orElseThrow();
    }

    protected void assertPersistedPaymentViToMatchAllProperties(PaymentVi expectedPaymentVi) {
        assertPaymentViAllPropertiesEquals(expectedPaymentVi, getPersistedPaymentVi(expectedPaymentVi));
    }

    protected void assertPersistedPaymentViToMatchUpdatableProperties(PaymentVi expectedPaymentVi) {
        assertPaymentViAllUpdatablePropertiesEquals(expectedPaymentVi, getPersistedPaymentVi(expectedPaymentVi));
    }
}
