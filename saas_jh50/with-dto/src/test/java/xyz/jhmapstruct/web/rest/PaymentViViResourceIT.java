package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.PaymentViViAsserts.*;
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
import xyz.jhmapstruct.domain.PaymentViVi;
import xyz.jhmapstruct.domain.enumeration.PaymentViViMethod;
import xyz.jhmapstruct.repository.PaymentViViRepository;
import xyz.jhmapstruct.service.dto.PaymentViViDTO;
import xyz.jhmapstruct.service.mapper.PaymentViViMapper;

/**
 * Integration tests for the {@link PaymentViViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentViViResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final Instant DEFAULT_PAYMENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYMENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final PaymentViViMethod DEFAULT_PAYMENT_METHOD = PaymentViViMethod.CREDIT_CARD;
    private static final PaymentViViMethod UPDATED_PAYMENT_METHOD = PaymentViViMethod.PAYPAL;

    private static final String ENTITY_API_URL = "/api/payment-vi-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaymentViViRepository paymentViViRepository;

    @Autowired
    private PaymentViViMapper paymentViViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentViViMockMvc;

    private PaymentViVi paymentViVi;

    private PaymentViVi insertedPaymentViVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentViVi createEntity() {
        return new PaymentViVi().amount(DEFAULT_AMOUNT).paymentDate(DEFAULT_PAYMENT_DATE).paymentMethod(DEFAULT_PAYMENT_METHOD);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentViVi createUpdatedEntity() {
        return new PaymentViVi().amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);
    }

    @BeforeEach
    public void initTest() {
        paymentViVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPaymentViVi != null) {
            paymentViViRepository.delete(insertedPaymentViVi);
            insertedPaymentViVi = null;
        }
    }

    @Test
    @Transactional
    void createPaymentViVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PaymentViVi
        PaymentViViDTO paymentViViDTO = paymentViViMapper.toDto(paymentViVi);
        var returnedPaymentViViDTO = om.readValue(
            restPaymentViViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentViViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PaymentViViDTO.class
        );

        // Validate the PaymentViVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPaymentViVi = paymentViViMapper.toEntity(returnedPaymentViViDTO);
        assertPaymentViViUpdatableFieldsEquals(returnedPaymentViVi, getPersistedPaymentViVi(returnedPaymentViVi));

        insertedPaymentViVi = returnedPaymentViVi;
    }

    @Test
    @Transactional
    void createPaymentViViWithExistingId() throws Exception {
        // Create the PaymentViVi with an existing ID
        paymentViVi.setId(1L);
        PaymentViViDTO paymentViViDTO = paymentViViMapper.toDto(paymentViVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentViViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentViVi.setAmount(null);

        // Create the PaymentViVi, which fails.
        PaymentViViDTO paymentViViDTO = paymentViViMapper.toDto(paymentViVi);

        restPaymentViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentViViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentViVi.setPaymentDate(null);

        // Create the PaymentViVi, which fails.
        PaymentViViDTO paymentViViDTO = paymentViViMapper.toDto(paymentViVi);

        restPaymentViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentViViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaymentViVis() throws Exception {
        // Initialize the database
        insertedPaymentViVi = paymentViViRepository.saveAndFlush(paymentViVi);

        // Get all the paymentViViList
        restPaymentViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentViVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));
    }

    @Test
    @Transactional
    void getPaymentViVi() throws Exception {
        // Initialize the database
        insertedPaymentViVi = paymentViViRepository.saveAndFlush(paymentViVi);

        // Get the paymentViVi
        restPaymentViViMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentViVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentViVi.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPaymentViVi() throws Exception {
        // Get the paymentViVi
        restPaymentViViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaymentViVi() throws Exception {
        // Initialize the database
        insertedPaymentViVi = paymentViViRepository.saveAndFlush(paymentViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentViVi
        PaymentViVi updatedPaymentViVi = paymentViViRepository.findById(paymentViVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPaymentViVi are not directly saved in db
        em.detach(updatedPaymentViVi);
        updatedPaymentViVi.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);
        PaymentViViDTO paymentViViDTO = paymentViViMapper.toDto(updatedPaymentViVi);

        restPaymentViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentViViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentViViDTO))
            )
            .andExpect(status().isOk());

        // Validate the PaymentViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaymentViViToMatchAllProperties(updatedPaymentViVi);
    }

    @Test
    @Transactional
    void putNonExistingPaymentViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentViVi.setId(longCount.incrementAndGet());

        // Create the PaymentViVi
        PaymentViViDTO paymentViViDTO = paymentViViMapper.toDto(paymentViVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentViViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentViVi.setId(longCount.incrementAndGet());

        // Create the PaymentViVi
        PaymentViViDTO paymentViViDTO = paymentViViMapper.toDto(paymentViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentViVi.setId(longCount.incrementAndGet());

        // Create the PaymentViVi
        PaymentViViDTO paymentViViDTO = paymentViViMapper.toDto(paymentViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentViViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentViViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentViViWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentViVi = paymentViViRepository.saveAndFlush(paymentViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentViVi using partial update
        PaymentViVi partialUpdatedPaymentViVi = new PaymentViVi();
        partialUpdatedPaymentViVi.setId(paymentViVi.getId());

        partialUpdatedPaymentViVi.paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restPaymentViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentViVi))
            )
            .andExpect(status().isOk());

        // Validate the PaymentViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentViViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPaymentViVi, paymentViVi),
            getPersistedPaymentViVi(paymentViVi)
        );
    }

    @Test
    @Transactional
    void fullUpdatePaymentViViWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentViVi = paymentViViRepository.saveAndFlush(paymentViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentViVi using partial update
        PaymentViVi partialUpdatedPaymentViVi = new PaymentViVi();
        partialUpdatedPaymentViVi.setId(paymentViVi.getId());

        partialUpdatedPaymentViVi.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restPaymentViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentViVi))
            )
            .andExpect(status().isOk());

        // Validate the PaymentViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentViViUpdatableFieldsEquals(partialUpdatedPaymentViVi, getPersistedPaymentViVi(partialUpdatedPaymentViVi));
    }

    @Test
    @Transactional
    void patchNonExistingPaymentViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentViVi.setId(longCount.incrementAndGet());

        // Create the PaymentViVi
        PaymentViViDTO paymentViViDTO = paymentViViMapper.toDto(paymentViVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentViViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentViVi.setId(longCount.incrementAndGet());

        // Create the PaymentViVi
        PaymentViViDTO paymentViViDTO = paymentViViMapper.toDto(paymentViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentViVi.setId(longCount.incrementAndGet());

        // Create the PaymentViVi
        PaymentViViDTO paymentViViDTO = paymentViViMapper.toDto(paymentViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentViViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paymentViViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaymentViVi() throws Exception {
        // Initialize the database
        insertedPaymentViVi = paymentViViRepository.saveAndFlush(paymentViVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the paymentViVi
        restPaymentViViMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentViVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paymentViViRepository.count();
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

    protected PaymentViVi getPersistedPaymentViVi(PaymentViVi paymentViVi) {
        return paymentViViRepository.findById(paymentViVi.getId()).orElseThrow();
    }

    protected void assertPersistedPaymentViViToMatchAllProperties(PaymentViVi expectedPaymentViVi) {
        assertPaymentViViAllPropertiesEquals(expectedPaymentViVi, getPersistedPaymentViVi(expectedPaymentViVi));
    }

    protected void assertPersistedPaymentViViToMatchUpdatableProperties(PaymentViVi expectedPaymentViVi) {
        assertPaymentViViAllUpdatablePropertiesEquals(expectedPaymentViVi, getPersistedPaymentViVi(expectedPaymentViVi));
    }
}
