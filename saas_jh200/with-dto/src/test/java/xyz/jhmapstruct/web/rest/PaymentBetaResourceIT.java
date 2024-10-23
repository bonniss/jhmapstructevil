package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.PaymentBetaAsserts.*;
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
import xyz.jhmapstruct.domain.PaymentBeta;
import xyz.jhmapstruct.domain.enumeration.PaymentBetaMethod;
import xyz.jhmapstruct.repository.PaymentBetaRepository;
import xyz.jhmapstruct.service.dto.PaymentBetaDTO;
import xyz.jhmapstruct.service.mapper.PaymentBetaMapper;

/**
 * Integration tests for the {@link PaymentBetaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentBetaResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final Instant DEFAULT_PAYMENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYMENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final PaymentBetaMethod DEFAULT_PAYMENT_METHOD = PaymentBetaMethod.CREDIT_CARD;
    private static final PaymentBetaMethod UPDATED_PAYMENT_METHOD = PaymentBetaMethod.PAYPAL;

    private static final String ENTITY_API_URL = "/api/payment-betas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaymentBetaRepository paymentBetaRepository;

    @Autowired
    private PaymentBetaMapper paymentBetaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentBetaMockMvc;

    private PaymentBeta paymentBeta;

    private PaymentBeta insertedPaymentBeta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentBeta createEntity() {
        return new PaymentBeta().amount(DEFAULT_AMOUNT).paymentDate(DEFAULT_PAYMENT_DATE).paymentMethod(DEFAULT_PAYMENT_METHOD);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentBeta createUpdatedEntity() {
        return new PaymentBeta().amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);
    }

    @BeforeEach
    public void initTest() {
        paymentBeta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPaymentBeta != null) {
            paymentBetaRepository.delete(insertedPaymentBeta);
            insertedPaymentBeta = null;
        }
    }

    @Test
    @Transactional
    void createPaymentBeta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PaymentBeta
        PaymentBetaDTO paymentBetaDTO = paymentBetaMapper.toDto(paymentBeta);
        var returnedPaymentBetaDTO = om.readValue(
            restPaymentBetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentBetaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PaymentBetaDTO.class
        );

        // Validate the PaymentBeta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPaymentBeta = paymentBetaMapper.toEntity(returnedPaymentBetaDTO);
        assertPaymentBetaUpdatableFieldsEquals(returnedPaymentBeta, getPersistedPaymentBeta(returnedPaymentBeta));

        insertedPaymentBeta = returnedPaymentBeta;
    }

    @Test
    @Transactional
    void createPaymentBetaWithExistingId() throws Exception {
        // Create the PaymentBeta with an existing ID
        paymentBeta.setId(1L);
        PaymentBetaDTO paymentBetaDTO = paymentBetaMapper.toDto(paymentBeta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentBetaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentBeta.setAmount(null);

        // Create the PaymentBeta, which fails.
        PaymentBetaDTO paymentBetaDTO = paymentBetaMapper.toDto(paymentBeta);

        restPaymentBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentBetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentBeta.setPaymentDate(null);

        // Create the PaymentBeta, which fails.
        PaymentBetaDTO paymentBetaDTO = paymentBetaMapper.toDto(paymentBeta);

        restPaymentBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentBetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaymentBetas() throws Exception {
        // Initialize the database
        insertedPaymentBeta = paymentBetaRepository.saveAndFlush(paymentBeta);

        // Get all the paymentBetaList
        restPaymentBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));
    }

    @Test
    @Transactional
    void getPaymentBeta() throws Exception {
        // Initialize the database
        insertedPaymentBeta = paymentBetaRepository.saveAndFlush(paymentBeta);

        // Get the paymentBeta
        restPaymentBetaMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentBeta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentBeta.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()));
    }

    @Test
    @Transactional
    void getPaymentBetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedPaymentBeta = paymentBetaRepository.saveAndFlush(paymentBeta);

        Long id = paymentBeta.getId();

        defaultPaymentBetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPaymentBetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPaymentBetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaymentBetasByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentBeta = paymentBetaRepository.saveAndFlush(paymentBeta);

        // Get all the paymentBetaList where amount equals to
        defaultPaymentBetaFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentBetasByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentBeta = paymentBetaRepository.saveAndFlush(paymentBeta);

        // Get all the paymentBetaList where amount in
        defaultPaymentBetaFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentBetasByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentBeta = paymentBetaRepository.saveAndFlush(paymentBeta);

        // Get all the paymentBetaList where amount is not null
        defaultPaymentBetaFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentBetasByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentBeta = paymentBetaRepository.saveAndFlush(paymentBeta);

        // Get all the paymentBetaList where amount is greater than or equal to
        defaultPaymentBetaFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentBetasByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentBeta = paymentBetaRepository.saveAndFlush(paymentBeta);

        // Get all the paymentBetaList where amount is less than or equal to
        defaultPaymentBetaFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentBetasByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPaymentBeta = paymentBetaRepository.saveAndFlush(paymentBeta);

        // Get all the paymentBetaList where amount is less than
        defaultPaymentBetaFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentBetasByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPaymentBeta = paymentBetaRepository.saveAndFlush(paymentBeta);

        // Get all the paymentBetaList where amount is greater than
        defaultPaymentBetaFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentBetasByPaymentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentBeta = paymentBetaRepository.saveAndFlush(paymentBeta);

        // Get all the paymentBetaList where paymentDate equals to
        defaultPaymentBetaFiltering("paymentDate.equals=" + DEFAULT_PAYMENT_DATE, "paymentDate.equals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentBetasByPaymentDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentBeta = paymentBetaRepository.saveAndFlush(paymentBeta);

        // Get all the paymentBetaList where paymentDate in
        defaultPaymentBetaFiltering(
            "paymentDate.in=" + DEFAULT_PAYMENT_DATE + "," + UPDATED_PAYMENT_DATE,
            "paymentDate.in=" + UPDATED_PAYMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllPaymentBetasByPaymentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentBeta = paymentBetaRepository.saveAndFlush(paymentBeta);

        // Get all the paymentBetaList where paymentDate is not null
        defaultPaymentBetaFiltering("paymentDate.specified=true", "paymentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentBetasByPaymentMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentBeta = paymentBetaRepository.saveAndFlush(paymentBeta);

        // Get all the paymentBetaList where paymentMethod equals to
        defaultPaymentBetaFiltering("paymentMethod.equals=" + DEFAULT_PAYMENT_METHOD, "paymentMethod.equals=" + UPDATED_PAYMENT_METHOD);
    }

    @Test
    @Transactional
    void getAllPaymentBetasByPaymentMethodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentBeta = paymentBetaRepository.saveAndFlush(paymentBeta);

        // Get all the paymentBetaList where paymentMethod in
        defaultPaymentBetaFiltering(
            "paymentMethod.in=" + DEFAULT_PAYMENT_METHOD + "," + UPDATED_PAYMENT_METHOD,
            "paymentMethod.in=" + UPDATED_PAYMENT_METHOD
        );
    }

    @Test
    @Transactional
    void getAllPaymentBetasByPaymentMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentBeta = paymentBetaRepository.saveAndFlush(paymentBeta);

        // Get all the paymentBetaList where paymentMethod is not null
        defaultPaymentBetaFiltering("paymentMethod.specified=true", "paymentMethod.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentBetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            paymentBetaRepository.saveAndFlush(paymentBeta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        paymentBeta.setTenant(tenant);
        paymentBetaRepository.saveAndFlush(paymentBeta);
        Long tenantId = tenant.getId();
        // Get all the paymentBetaList where tenant equals to tenantId
        defaultPaymentBetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the paymentBetaList where tenant equals to (tenantId + 1)
        defaultPaymentBetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultPaymentBetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPaymentBetaShouldBeFound(shouldBeFound);
        defaultPaymentBetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentBetaShouldBeFound(String filter) throws Exception {
        restPaymentBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));

        // Check, that the count call also returns 1
        restPaymentBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentBetaShouldNotBeFound(String filter) throws Exception {
        restPaymentBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPaymentBeta() throws Exception {
        // Get the paymentBeta
        restPaymentBetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaymentBeta() throws Exception {
        // Initialize the database
        insertedPaymentBeta = paymentBetaRepository.saveAndFlush(paymentBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentBeta
        PaymentBeta updatedPaymentBeta = paymentBetaRepository.findById(paymentBeta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPaymentBeta are not directly saved in db
        em.detach(updatedPaymentBeta);
        updatedPaymentBeta.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);
        PaymentBetaDTO paymentBetaDTO = paymentBetaMapper.toDto(updatedPaymentBeta);

        restPaymentBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentBetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentBetaDTO))
            )
            .andExpect(status().isOk());

        // Validate the PaymentBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaymentBetaToMatchAllProperties(updatedPaymentBeta);
    }

    @Test
    @Transactional
    void putNonExistingPaymentBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentBeta.setId(longCount.incrementAndGet());

        // Create the PaymentBeta
        PaymentBetaDTO paymentBetaDTO = paymentBetaMapper.toDto(paymentBeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentBetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentBeta.setId(longCount.incrementAndGet());

        // Create the PaymentBeta
        PaymentBetaDTO paymentBetaDTO = paymentBetaMapper.toDto(paymentBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentBeta.setId(longCount.incrementAndGet());

        // Create the PaymentBeta
        PaymentBetaDTO paymentBetaDTO = paymentBetaMapper.toDto(paymentBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentBetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentBetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentBetaWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentBeta = paymentBetaRepository.saveAndFlush(paymentBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentBeta using partial update
        PaymentBeta partialUpdatedPaymentBeta = new PaymentBeta();
        partialUpdatedPaymentBeta.setId(paymentBeta.getId());

        restPaymentBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentBeta))
            )
            .andExpect(status().isOk());

        // Validate the PaymentBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentBetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPaymentBeta, paymentBeta),
            getPersistedPaymentBeta(paymentBeta)
        );
    }

    @Test
    @Transactional
    void fullUpdatePaymentBetaWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentBeta = paymentBetaRepository.saveAndFlush(paymentBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentBeta using partial update
        PaymentBeta partialUpdatedPaymentBeta = new PaymentBeta();
        partialUpdatedPaymentBeta.setId(paymentBeta.getId());

        partialUpdatedPaymentBeta.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restPaymentBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentBeta))
            )
            .andExpect(status().isOk());

        // Validate the PaymentBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentBetaUpdatableFieldsEquals(partialUpdatedPaymentBeta, getPersistedPaymentBeta(partialUpdatedPaymentBeta));
    }

    @Test
    @Transactional
    void patchNonExistingPaymentBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentBeta.setId(longCount.incrementAndGet());

        // Create the PaymentBeta
        PaymentBetaDTO paymentBetaDTO = paymentBetaMapper.toDto(paymentBeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentBetaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentBeta.setId(longCount.incrementAndGet());

        // Create the PaymentBeta
        PaymentBetaDTO paymentBetaDTO = paymentBetaMapper.toDto(paymentBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentBeta.setId(longCount.incrementAndGet());

        // Create the PaymentBeta
        PaymentBetaDTO paymentBetaDTO = paymentBetaMapper.toDto(paymentBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentBetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paymentBetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaymentBeta() throws Exception {
        // Initialize the database
        insertedPaymentBeta = paymentBetaRepository.saveAndFlush(paymentBeta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the paymentBeta
        restPaymentBetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentBeta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paymentBetaRepository.count();
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

    protected PaymentBeta getPersistedPaymentBeta(PaymentBeta paymentBeta) {
        return paymentBetaRepository.findById(paymentBeta.getId()).orElseThrow();
    }

    protected void assertPersistedPaymentBetaToMatchAllProperties(PaymentBeta expectedPaymentBeta) {
        assertPaymentBetaAllPropertiesEquals(expectedPaymentBeta, getPersistedPaymentBeta(expectedPaymentBeta));
    }

    protected void assertPersistedPaymentBetaToMatchUpdatableProperties(PaymentBeta expectedPaymentBeta) {
        assertPaymentBetaAllUpdatablePropertiesEquals(expectedPaymentBeta, getPersistedPaymentBeta(expectedPaymentBeta));
    }
}
