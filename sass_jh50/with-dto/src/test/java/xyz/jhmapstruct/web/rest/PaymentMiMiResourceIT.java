package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.PaymentMiMiAsserts.*;
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
import xyz.jhmapstruct.domain.PaymentMiMi;
import xyz.jhmapstruct.domain.enumeration.PaymentMiMiMethod;
import xyz.jhmapstruct.repository.PaymentMiMiRepository;
import xyz.jhmapstruct.service.dto.PaymentMiMiDTO;
import xyz.jhmapstruct.service.mapper.PaymentMiMiMapper;

/**
 * Integration tests for the {@link PaymentMiMiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentMiMiResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final Instant DEFAULT_PAYMENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYMENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final PaymentMiMiMethod DEFAULT_PAYMENT_METHOD = PaymentMiMiMethod.CREDIT_CARD;
    private static final PaymentMiMiMethod UPDATED_PAYMENT_METHOD = PaymentMiMiMethod.PAYPAL;

    private static final String ENTITY_API_URL = "/api/payment-mi-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaymentMiMiRepository paymentMiMiRepository;

    @Autowired
    private PaymentMiMiMapper paymentMiMiMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentMiMiMockMvc;

    private PaymentMiMi paymentMiMi;

    private PaymentMiMi insertedPaymentMiMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentMiMi createEntity() {
        return new PaymentMiMi().amount(DEFAULT_AMOUNT).paymentDate(DEFAULT_PAYMENT_DATE).paymentMethod(DEFAULT_PAYMENT_METHOD);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentMiMi createUpdatedEntity() {
        return new PaymentMiMi().amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);
    }

    @BeforeEach
    public void initTest() {
        paymentMiMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPaymentMiMi != null) {
            paymentMiMiRepository.delete(insertedPaymentMiMi);
            insertedPaymentMiMi = null;
        }
    }

    @Test
    @Transactional
    void createPaymentMiMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PaymentMiMi
        PaymentMiMiDTO paymentMiMiDTO = paymentMiMiMapper.toDto(paymentMiMi);
        var returnedPaymentMiMiDTO = om.readValue(
            restPaymentMiMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentMiMiDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PaymentMiMiDTO.class
        );

        // Validate the PaymentMiMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPaymentMiMi = paymentMiMiMapper.toEntity(returnedPaymentMiMiDTO);
        assertPaymentMiMiUpdatableFieldsEquals(returnedPaymentMiMi, getPersistedPaymentMiMi(returnedPaymentMiMi));

        insertedPaymentMiMi = returnedPaymentMiMi;
    }

    @Test
    @Transactional
    void createPaymentMiMiWithExistingId() throws Exception {
        // Create the PaymentMiMi with an existing ID
        paymentMiMi.setId(1L);
        PaymentMiMiDTO paymentMiMiDTO = paymentMiMiMapper.toDto(paymentMiMi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentMiMiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentMiMi.setAmount(null);

        // Create the PaymentMiMi, which fails.
        PaymentMiMiDTO paymentMiMiDTO = paymentMiMiMapper.toDto(paymentMiMi);

        restPaymentMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentMiMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentMiMi.setPaymentDate(null);

        // Create the PaymentMiMi, which fails.
        PaymentMiMiDTO paymentMiMiDTO = paymentMiMiMapper.toDto(paymentMiMi);

        restPaymentMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentMiMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaymentMiMis() throws Exception {
        // Initialize the database
        insertedPaymentMiMi = paymentMiMiRepository.saveAndFlush(paymentMiMi);

        // Get all the paymentMiMiList
        restPaymentMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentMiMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));
    }

    @Test
    @Transactional
    void getPaymentMiMi() throws Exception {
        // Initialize the database
        insertedPaymentMiMi = paymentMiMiRepository.saveAndFlush(paymentMiMi);

        // Get the paymentMiMi
        restPaymentMiMiMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentMiMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentMiMi.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPaymentMiMi() throws Exception {
        // Get the paymentMiMi
        restPaymentMiMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaymentMiMi() throws Exception {
        // Initialize the database
        insertedPaymentMiMi = paymentMiMiRepository.saveAndFlush(paymentMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentMiMi
        PaymentMiMi updatedPaymentMiMi = paymentMiMiRepository.findById(paymentMiMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPaymentMiMi are not directly saved in db
        em.detach(updatedPaymentMiMi);
        updatedPaymentMiMi.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);
        PaymentMiMiDTO paymentMiMiDTO = paymentMiMiMapper.toDto(updatedPaymentMiMi);

        restPaymentMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentMiMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentMiMiDTO))
            )
            .andExpect(status().isOk());

        // Validate the PaymentMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaymentMiMiToMatchAllProperties(updatedPaymentMiMi);
    }

    @Test
    @Transactional
    void putNonExistingPaymentMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentMiMi.setId(longCount.incrementAndGet());

        // Create the PaymentMiMi
        PaymentMiMiDTO paymentMiMiDTO = paymentMiMiMapper.toDto(paymentMiMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentMiMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentMiMi.setId(longCount.incrementAndGet());

        // Create the PaymentMiMi
        PaymentMiMiDTO paymentMiMiDTO = paymentMiMiMapper.toDto(paymentMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentMiMi.setId(longCount.incrementAndGet());

        // Create the PaymentMiMi
        PaymentMiMiDTO paymentMiMiDTO = paymentMiMiMapper.toDto(paymentMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMiMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentMiMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentMiMi = paymentMiMiRepository.saveAndFlush(paymentMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentMiMi using partial update
        PaymentMiMi partialUpdatedPaymentMiMi = new PaymentMiMi();
        partialUpdatedPaymentMiMi.setId(paymentMiMi.getId());

        partialUpdatedPaymentMiMi.paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restPaymentMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentMiMi))
            )
            .andExpect(status().isOk());

        // Validate the PaymentMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentMiMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPaymentMiMi, paymentMiMi),
            getPersistedPaymentMiMi(paymentMiMi)
        );
    }

    @Test
    @Transactional
    void fullUpdatePaymentMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentMiMi = paymentMiMiRepository.saveAndFlush(paymentMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentMiMi using partial update
        PaymentMiMi partialUpdatedPaymentMiMi = new PaymentMiMi();
        partialUpdatedPaymentMiMi.setId(paymentMiMi.getId());

        partialUpdatedPaymentMiMi.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restPaymentMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentMiMi))
            )
            .andExpect(status().isOk());

        // Validate the PaymentMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentMiMiUpdatableFieldsEquals(partialUpdatedPaymentMiMi, getPersistedPaymentMiMi(partialUpdatedPaymentMiMi));
    }

    @Test
    @Transactional
    void patchNonExistingPaymentMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentMiMi.setId(longCount.incrementAndGet());

        // Create the PaymentMiMi
        PaymentMiMiDTO paymentMiMiDTO = paymentMiMiMapper.toDto(paymentMiMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentMiMiDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentMiMi.setId(longCount.incrementAndGet());

        // Create the PaymentMiMi
        PaymentMiMiDTO paymentMiMiDTO = paymentMiMiMapper.toDto(paymentMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentMiMi.setId(longCount.incrementAndGet());

        // Create the PaymentMiMi
        PaymentMiMiDTO paymentMiMiDTO = paymentMiMiMapper.toDto(paymentMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMiMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paymentMiMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaymentMiMi() throws Exception {
        // Initialize the database
        insertedPaymentMiMi = paymentMiMiRepository.saveAndFlush(paymentMiMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the paymentMiMi
        restPaymentMiMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentMiMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paymentMiMiRepository.count();
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

    protected PaymentMiMi getPersistedPaymentMiMi(PaymentMiMi paymentMiMi) {
        return paymentMiMiRepository.findById(paymentMiMi.getId()).orElseThrow();
    }

    protected void assertPersistedPaymentMiMiToMatchAllProperties(PaymentMiMi expectedPaymentMiMi) {
        assertPaymentMiMiAllPropertiesEquals(expectedPaymentMiMi, getPersistedPaymentMiMi(expectedPaymentMiMi));
    }

    protected void assertPersistedPaymentMiMiToMatchUpdatableProperties(PaymentMiMi expectedPaymentMiMi) {
        assertPaymentMiMiAllUpdatablePropertiesEquals(expectedPaymentMiMi, getPersistedPaymentMiMi(expectedPaymentMiMi));
    }
}
