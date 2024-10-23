package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.PaymentMiAsserts.*;
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
import xyz.jhmapstruct.domain.PaymentMi;
import xyz.jhmapstruct.domain.enumeration.PaymentMiMethod;
import xyz.jhmapstruct.repository.PaymentMiRepository;
import xyz.jhmapstruct.service.dto.PaymentMiDTO;
import xyz.jhmapstruct.service.mapper.PaymentMiMapper;

/**
 * Integration tests for the {@link PaymentMiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentMiResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final Instant DEFAULT_PAYMENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYMENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final PaymentMiMethod DEFAULT_PAYMENT_METHOD = PaymentMiMethod.CREDIT_CARD;
    private static final PaymentMiMethod UPDATED_PAYMENT_METHOD = PaymentMiMethod.PAYPAL;

    private static final String ENTITY_API_URL = "/api/payment-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaymentMiRepository paymentMiRepository;

    @Autowired
    private PaymentMiMapper paymentMiMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentMiMockMvc;

    private PaymentMi paymentMi;

    private PaymentMi insertedPaymentMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentMi createEntity() {
        return new PaymentMi().amount(DEFAULT_AMOUNT).paymentDate(DEFAULT_PAYMENT_DATE).paymentMethod(DEFAULT_PAYMENT_METHOD);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentMi createUpdatedEntity() {
        return new PaymentMi().amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);
    }

    @BeforeEach
    public void initTest() {
        paymentMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPaymentMi != null) {
            paymentMiRepository.delete(insertedPaymentMi);
            insertedPaymentMi = null;
        }
    }

    @Test
    @Transactional
    void createPaymentMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PaymentMi
        PaymentMiDTO paymentMiDTO = paymentMiMapper.toDto(paymentMi);
        var returnedPaymentMiDTO = om.readValue(
            restPaymentMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentMiDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PaymentMiDTO.class
        );

        // Validate the PaymentMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPaymentMi = paymentMiMapper.toEntity(returnedPaymentMiDTO);
        assertPaymentMiUpdatableFieldsEquals(returnedPaymentMi, getPersistedPaymentMi(returnedPaymentMi));

        insertedPaymentMi = returnedPaymentMi;
    }

    @Test
    @Transactional
    void createPaymentMiWithExistingId() throws Exception {
        // Create the PaymentMi with an existing ID
        paymentMi.setId(1L);
        PaymentMiDTO paymentMiDTO = paymentMiMapper.toDto(paymentMi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentMiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentMi.setAmount(null);

        // Create the PaymentMi, which fails.
        PaymentMiDTO paymentMiDTO = paymentMiMapper.toDto(paymentMi);

        restPaymentMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentMi.setPaymentDate(null);

        // Create the PaymentMi, which fails.
        PaymentMiDTO paymentMiDTO = paymentMiMapper.toDto(paymentMi);

        restPaymentMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaymentMis() throws Exception {
        // Initialize the database
        insertedPaymentMi = paymentMiRepository.saveAndFlush(paymentMi);

        // Get all the paymentMiList
        restPaymentMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));
    }

    @Test
    @Transactional
    void getPaymentMi() throws Exception {
        // Initialize the database
        insertedPaymentMi = paymentMiRepository.saveAndFlush(paymentMi);

        // Get the paymentMi
        restPaymentMiMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentMi.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()));
    }

    @Test
    @Transactional
    void getPaymentMisByIdFiltering() throws Exception {
        // Initialize the database
        insertedPaymentMi = paymentMiRepository.saveAndFlush(paymentMi);

        Long id = paymentMi.getId();

        defaultPaymentMiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPaymentMiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPaymentMiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaymentMisByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentMi = paymentMiRepository.saveAndFlush(paymentMi);

        // Get all the paymentMiList where amount equals to
        defaultPaymentMiFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentMisByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentMi = paymentMiRepository.saveAndFlush(paymentMi);

        // Get all the paymentMiList where amount in
        defaultPaymentMiFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentMisByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentMi = paymentMiRepository.saveAndFlush(paymentMi);

        // Get all the paymentMiList where amount is not null
        defaultPaymentMiFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentMisByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentMi = paymentMiRepository.saveAndFlush(paymentMi);

        // Get all the paymentMiList where amount is greater than or equal to
        defaultPaymentMiFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentMisByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentMi = paymentMiRepository.saveAndFlush(paymentMi);

        // Get all the paymentMiList where amount is less than or equal to
        defaultPaymentMiFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentMisByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPaymentMi = paymentMiRepository.saveAndFlush(paymentMi);

        // Get all the paymentMiList where amount is less than
        defaultPaymentMiFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentMisByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPaymentMi = paymentMiRepository.saveAndFlush(paymentMi);

        // Get all the paymentMiList where amount is greater than
        defaultPaymentMiFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentMisByPaymentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentMi = paymentMiRepository.saveAndFlush(paymentMi);

        // Get all the paymentMiList where paymentDate equals to
        defaultPaymentMiFiltering("paymentDate.equals=" + DEFAULT_PAYMENT_DATE, "paymentDate.equals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentMisByPaymentDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentMi = paymentMiRepository.saveAndFlush(paymentMi);

        // Get all the paymentMiList where paymentDate in
        defaultPaymentMiFiltering(
            "paymentDate.in=" + DEFAULT_PAYMENT_DATE + "," + UPDATED_PAYMENT_DATE,
            "paymentDate.in=" + UPDATED_PAYMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllPaymentMisByPaymentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentMi = paymentMiRepository.saveAndFlush(paymentMi);

        // Get all the paymentMiList where paymentDate is not null
        defaultPaymentMiFiltering("paymentDate.specified=true", "paymentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentMisByPaymentMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentMi = paymentMiRepository.saveAndFlush(paymentMi);

        // Get all the paymentMiList where paymentMethod equals to
        defaultPaymentMiFiltering("paymentMethod.equals=" + DEFAULT_PAYMENT_METHOD, "paymentMethod.equals=" + UPDATED_PAYMENT_METHOD);
    }

    @Test
    @Transactional
    void getAllPaymentMisByPaymentMethodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentMi = paymentMiRepository.saveAndFlush(paymentMi);

        // Get all the paymentMiList where paymentMethod in
        defaultPaymentMiFiltering(
            "paymentMethod.in=" + DEFAULT_PAYMENT_METHOD + "," + UPDATED_PAYMENT_METHOD,
            "paymentMethod.in=" + UPDATED_PAYMENT_METHOD
        );
    }

    @Test
    @Transactional
    void getAllPaymentMisByPaymentMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentMi = paymentMiRepository.saveAndFlush(paymentMi);

        // Get all the paymentMiList where paymentMethod is not null
        defaultPaymentMiFiltering("paymentMethod.specified=true", "paymentMethod.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentMisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            paymentMiRepository.saveAndFlush(paymentMi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        paymentMi.setTenant(tenant);
        paymentMiRepository.saveAndFlush(paymentMi);
        Long tenantId = tenant.getId();
        // Get all the paymentMiList where tenant equals to tenantId
        defaultPaymentMiShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the paymentMiList where tenant equals to (tenantId + 1)
        defaultPaymentMiShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultPaymentMiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPaymentMiShouldBeFound(shouldBeFound);
        defaultPaymentMiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentMiShouldBeFound(String filter) throws Exception {
        restPaymentMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));

        // Check, that the count call also returns 1
        restPaymentMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentMiShouldNotBeFound(String filter) throws Exception {
        restPaymentMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPaymentMi() throws Exception {
        // Get the paymentMi
        restPaymentMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaymentMi() throws Exception {
        // Initialize the database
        insertedPaymentMi = paymentMiRepository.saveAndFlush(paymentMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentMi
        PaymentMi updatedPaymentMi = paymentMiRepository.findById(paymentMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPaymentMi are not directly saved in db
        em.detach(updatedPaymentMi);
        updatedPaymentMi.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);
        PaymentMiDTO paymentMiDTO = paymentMiMapper.toDto(updatedPaymentMi);

        restPaymentMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentMiDTO))
            )
            .andExpect(status().isOk());

        // Validate the PaymentMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaymentMiToMatchAllProperties(updatedPaymentMi);
    }

    @Test
    @Transactional
    void putNonExistingPaymentMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentMi.setId(longCount.incrementAndGet());

        // Create the PaymentMi
        PaymentMiDTO paymentMiDTO = paymentMiMapper.toDto(paymentMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentMi.setId(longCount.incrementAndGet());

        // Create the PaymentMi
        PaymentMiDTO paymentMiDTO = paymentMiMapper.toDto(paymentMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentMi.setId(longCount.incrementAndGet());

        // Create the PaymentMi
        PaymentMiDTO paymentMiDTO = paymentMiMapper.toDto(paymentMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentMiWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentMi = paymentMiRepository.saveAndFlush(paymentMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentMi using partial update
        PaymentMi partialUpdatedPaymentMi = new PaymentMi();
        partialUpdatedPaymentMi.setId(paymentMi.getId());

        restPaymentMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentMi))
            )
            .andExpect(status().isOk());

        // Validate the PaymentMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPaymentMi, paymentMi),
            getPersistedPaymentMi(paymentMi)
        );
    }

    @Test
    @Transactional
    void fullUpdatePaymentMiWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentMi = paymentMiRepository.saveAndFlush(paymentMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentMi using partial update
        PaymentMi partialUpdatedPaymentMi = new PaymentMi();
        partialUpdatedPaymentMi.setId(paymentMi.getId());

        partialUpdatedPaymentMi.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restPaymentMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentMi))
            )
            .andExpect(status().isOk());

        // Validate the PaymentMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentMiUpdatableFieldsEquals(partialUpdatedPaymentMi, getPersistedPaymentMi(partialUpdatedPaymentMi));
    }

    @Test
    @Transactional
    void patchNonExistingPaymentMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentMi.setId(longCount.incrementAndGet());

        // Create the PaymentMi
        PaymentMiDTO paymentMiDTO = paymentMiMapper.toDto(paymentMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentMiDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentMi.setId(longCount.incrementAndGet());

        // Create the PaymentMi
        PaymentMiDTO paymentMiDTO = paymentMiMapper.toDto(paymentMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentMi.setId(longCount.incrementAndGet());

        // Create the PaymentMi
        PaymentMiDTO paymentMiDTO = paymentMiMapper.toDto(paymentMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paymentMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaymentMi() throws Exception {
        // Initialize the database
        insertedPaymentMi = paymentMiRepository.saveAndFlush(paymentMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the paymentMi
        restPaymentMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paymentMiRepository.count();
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

    protected PaymentMi getPersistedPaymentMi(PaymentMi paymentMi) {
        return paymentMiRepository.findById(paymentMi.getId()).orElseThrow();
    }

    protected void assertPersistedPaymentMiToMatchAllProperties(PaymentMi expectedPaymentMi) {
        assertPaymentMiAllPropertiesEquals(expectedPaymentMi, getPersistedPaymentMi(expectedPaymentMi));
    }

    protected void assertPersistedPaymentMiToMatchUpdatableProperties(PaymentMi expectedPaymentMi) {
        assertPaymentMiAllUpdatablePropertiesEquals(expectedPaymentMi, getPersistedPaymentMi(expectedPaymentMi));
    }
}
