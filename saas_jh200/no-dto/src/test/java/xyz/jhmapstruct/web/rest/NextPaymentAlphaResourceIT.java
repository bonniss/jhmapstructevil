package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextPaymentAlphaAsserts.*;
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
import xyz.jhmapstruct.domain.NextPaymentAlpha;
import xyz.jhmapstruct.domain.enumeration.NextPaymentAlphaMethod;
import xyz.jhmapstruct.repository.NextPaymentAlphaRepository;

/**
 * Integration tests for the {@link NextPaymentAlphaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextPaymentAlphaResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final Instant DEFAULT_PAYMENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYMENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final NextPaymentAlphaMethod DEFAULT_PAYMENT_METHOD = NextPaymentAlphaMethod.CREDIT_CARD;
    private static final NextPaymentAlphaMethod UPDATED_PAYMENT_METHOD = NextPaymentAlphaMethod.PAYPAL;

    private static final String ENTITY_API_URL = "/api/next-payment-alphas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextPaymentAlphaRepository nextPaymentAlphaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextPaymentAlphaMockMvc;

    private NextPaymentAlpha nextPaymentAlpha;

    private NextPaymentAlpha insertedNextPaymentAlpha;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextPaymentAlpha createEntity() {
        return new NextPaymentAlpha().amount(DEFAULT_AMOUNT).paymentDate(DEFAULT_PAYMENT_DATE).paymentMethod(DEFAULT_PAYMENT_METHOD);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextPaymentAlpha createUpdatedEntity() {
        return new NextPaymentAlpha().amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);
    }

    @BeforeEach
    public void initTest() {
        nextPaymentAlpha = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextPaymentAlpha != null) {
            nextPaymentAlphaRepository.delete(insertedNextPaymentAlpha);
            insertedNextPaymentAlpha = null;
        }
    }

    @Test
    @Transactional
    void createNextPaymentAlpha() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextPaymentAlpha
        var returnedNextPaymentAlpha = om.readValue(
            restNextPaymentAlphaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentAlpha)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextPaymentAlpha.class
        );

        // Validate the NextPaymentAlpha in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextPaymentAlphaUpdatableFieldsEquals(returnedNextPaymentAlpha, getPersistedNextPaymentAlpha(returnedNextPaymentAlpha));

        insertedNextPaymentAlpha = returnedNextPaymentAlpha;
    }

    @Test
    @Transactional
    void createNextPaymentAlphaWithExistingId() throws Exception {
        // Create the NextPaymentAlpha with an existing ID
        nextPaymentAlpha.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextPaymentAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentAlpha)))
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextPaymentAlpha.setAmount(null);

        // Create the NextPaymentAlpha, which fails.

        restNextPaymentAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentAlpha)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextPaymentAlpha.setPaymentDate(null);

        // Create the NextPaymentAlpha, which fails.

        restNextPaymentAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentAlpha)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextPaymentAlphas() throws Exception {
        // Initialize the database
        insertedNextPaymentAlpha = nextPaymentAlphaRepository.saveAndFlush(nextPaymentAlpha);

        // Get all the nextPaymentAlphaList
        restNextPaymentAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextPaymentAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));
    }

    @Test
    @Transactional
    void getNextPaymentAlpha() throws Exception {
        // Initialize the database
        insertedNextPaymentAlpha = nextPaymentAlphaRepository.saveAndFlush(nextPaymentAlpha);

        // Get the nextPaymentAlpha
        restNextPaymentAlphaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextPaymentAlpha.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextPaymentAlpha.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()));
    }

    @Test
    @Transactional
    void getNextPaymentAlphasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextPaymentAlpha = nextPaymentAlphaRepository.saveAndFlush(nextPaymentAlpha);

        Long id = nextPaymentAlpha.getId();

        defaultNextPaymentAlphaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextPaymentAlphaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextPaymentAlphaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextPaymentAlphasByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentAlpha = nextPaymentAlphaRepository.saveAndFlush(nextPaymentAlpha);

        // Get all the nextPaymentAlphaList where amount equals to
        defaultNextPaymentAlphaFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentAlphasByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextPaymentAlpha = nextPaymentAlphaRepository.saveAndFlush(nextPaymentAlpha);

        // Get all the nextPaymentAlphaList where amount in
        defaultNextPaymentAlphaFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentAlphasByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextPaymentAlpha = nextPaymentAlphaRepository.saveAndFlush(nextPaymentAlpha);

        // Get all the nextPaymentAlphaList where amount is not null
        defaultNextPaymentAlphaFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllNextPaymentAlphasByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentAlpha = nextPaymentAlphaRepository.saveAndFlush(nextPaymentAlpha);

        // Get all the nextPaymentAlphaList where amount is greater than or equal to
        defaultNextPaymentAlphaFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentAlphasByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentAlpha = nextPaymentAlphaRepository.saveAndFlush(nextPaymentAlpha);

        // Get all the nextPaymentAlphaList where amount is less than or equal to
        defaultNextPaymentAlphaFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentAlphasByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentAlpha = nextPaymentAlphaRepository.saveAndFlush(nextPaymentAlpha);

        // Get all the nextPaymentAlphaList where amount is less than
        defaultNextPaymentAlphaFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentAlphasByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentAlpha = nextPaymentAlphaRepository.saveAndFlush(nextPaymentAlpha);

        // Get all the nextPaymentAlphaList where amount is greater than
        defaultNextPaymentAlphaFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentAlphasByPaymentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentAlpha = nextPaymentAlphaRepository.saveAndFlush(nextPaymentAlpha);

        // Get all the nextPaymentAlphaList where paymentDate equals to
        defaultNextPaymentAlphaFiltering("paymentDate.equals=" + DEFAULT_PAYMENT_DATE, "paymentDate.equals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllNextPaymentAlphasByPaymentDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextPaymentAlpha = nextPaymentAlphaRepository.saveAndFlush(nextPaymentAlpha);

        // Get all the nextPaymentAlphaList where paymentDate in
        defaultNextPaymentAlphaFiltering(
            "paymentDate.in=" + DEFAULT_PAYMENT_DATE + "," + UPDATED_PAYMENT_DATE,
            "paymentDate.in=" + UPDATED_PAYMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextPaymentAlphasByPaymentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextPaymentAlpha = nextPaymentAlphaRepository.saveAndFlush(nextPaymentAlpha);

        // Get all the nextPaymentAlphaList where paymentDate is not null
        defaultNextPaymentAlphaFiltering("paymentDate.specified=true", "paymentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextPaymentAlphasByPaymentMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentAlpha = nextPaymentAlphaRepository.saveAndFlush(nextPaymentAlpha);

        // Get all the nextPaymentAlphaList where paymentMethod equals to
        defaultNextPaymentAlphaFiltering(
            "paymentMethod.equals=" + DEFAULT_PAYMENT_METHOD,
            "paymentMethod.equals=" + UPDATED_PAYMENT_METHOD
        );
    }

    @Test
    @Transactional
    void getAllNextPaymentAlphasByPaymentMethodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextPaymentAlpha = nextPaymentAlphaRepository.saveAndFlush(nextPaymentAlpha);

        // Get all the nextPaymentAlphaList where paymentMethod in
        defaultNextPaymentAlphaFiltering(
            "paymentMethod.in=" + DEFAULT_PAYMENT_METHOD + "," + UPDATED_PAYMENT_METHOD,
            "paymentMethod.in=" + UPDATED_PAYMENT_METHOD
        );
    }

    @Test
    @Transactional
    void getAllNextPaymentAlphasByPaymentMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextPaymentAlpha = nextPaymentAlphaRepository.saveAndFlush(nextPaymentAlpha);

        // Get all the nextPaymentAlphaList where paymentMethod is not null
        defaultNextPaymentAlphaFiltering("paymentMethod.specified=true", "paymentMethod.specified=false");
    }

    @Test
    @Transactional
    void getAllNextPaymentAlphasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextPaymentAlphaRepository.saveAndFlush(nextPaymentAlpha);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextPaymentAlpha.setTenant(tenant);
        nextPaymentAlphaRepository.saveAndFlush(nextPaymentAlpha);
        Long tenantId = tenant.getId();
        // Get all the nextPaymentAlphaList where tenant equals to tenantId
        defaultNextPaymentAlphaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextPaymentAlphaList where tenant equals to (tenantId + 1)
        defaultNextPaymentAlphaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextPaymentAlphaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextPaymentAlphaShouldBeFound(shouldBeFound);
        defaultNextPaymentAlphaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextPaymentAlphaShouldBeFound(String filter) throws Exception {
        restNextPaymentAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextPaymentAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));

        // Check, that the count call also returns 1
        restNextPaymentAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextPaymentAlphaShouldNotBeFound(String filter) throws Exception {
        restNextPaymentAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextPaymentAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextPaymentAlpha() throws Exception {
        // Get the nextPaymentAlpha
        restNextPaymentAlphaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextPaymentAlpha() throws Exception {
        // Initialize the database
        insertedNextPaymentAlpha = nextPaymentAlphaRepository.saveAndFlush(nextPaymentAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextPaymentAlpha
        NextPaymentAlpha updatedNextPaymentAlpha = nextPaymentAlphaRepository.findById(nextPaymentAlpha.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextPaymentAlpha are not directly saved in db
        em.detach(updatedNextPaymentAlpha);
        updatedNextPaymentAlpha.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restNextPaymentAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextPaymentAlpha.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextPaymentAlpha))
            )
            .andExpect(status().isOk());

        // Validate the NextPaymentAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextPaymentAlphaToMatchAllProperties(updatedNextPaymentAlpha);
    }

    @Test
    @Transactional
    void putNonExistingNextPaymentAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentAlpha.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextPaymentAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextPaymentAlpha.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextPaymentAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextPaymentAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextPaymentAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextPaymentAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentAlphaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentAlpha)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextPaymentAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextPaymentAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedNextPaymentAlpha = nextPaymentAlphaRepository.saveAndFlush(nextPaymentAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextPaymentAlpha using partial update
        NextPaymentAlpha partialUpdatedNextPaymentAlpha = new NextPaymentAlpha();
        partialUpdatedNextPaymentAlpha.setId(nextPaymentAlpha.getId());

        partialUpdatedNextPaymentAlpha.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE);

        restNextPaymentAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextPaymentAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextPaymentAlpha))
            )
            .andExpect(status().isOk());

        // Validate the NextPaymentAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextPaymentAlphaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextPaymentAlpha, nextPaymentAlpha),
            getPersistedNextPaymentAlpha(nextPaymentAlpha)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextPaymentAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedNextPaymentAlpha = nextPaymentAlphaRepository.saveAndFlush(nextPaymentAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextPaymentAlpha using partial update
        NextPaymentAlpha partialUpdatedNextPaymentAlpha = new NextPaymentAlpha();
        partialUpdatedNextPaymentAlpha.setId(nextPaymentAlpha.getId());

        partialUpdatedNextPaymentAlpha.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restNextPaymentAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextPaymentAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextPaymentAlpha))
            )
            .andExpect(status().isOk());

        // Validate the NextPaymentAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextPaymentAlphaUpdatableFieldsEquals(
            partialUpdatedNextPaymentAlpha,
            getPersistedNextPaymentAlpha(partialUpdatedNextPaymentAlpha)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextPaymentAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentAlpha.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextPaymentAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextPaymentAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextPaymentAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextPaymentAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextPaymentAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextPaymentAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentAlphaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextPaymentAlpha)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextPaymentAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextPaymentAlpha() throws Exception {
        // Initialize the database
        insertedNextPaymentAlpha = nextPaymentAlphaRepository.saveAndFlush(nextPaymentAlpha);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextPaymentAlpha
        restNextPaymentAlphaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextPaymentAlpha.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextPaymentAlphaRepository.count();
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

    protected NextPaymentAlpha getPersistedNextPaymentAlpha(NextPaymentAlpha nextPaymentAlpha) {
        return nextPaymentAlphaRepository.findById(nextPaymentAlpha.getId()).orElseThrow();
    }

    protected void assertPersistedNextPaymentAlphaToMatchAllProperties(NextPaymentAlpha expectedNextPaymentAlpha) {
        assertNextPaymentAlphaAllPropertiesEquals(expectedNextPaymentAlpha, getPersistedNextPaymentAlpha(expectedNextPaymentAlpha));
    }

    protected void assertPersistedNextPaymentAlphaToMatchUpdatableProperties(NextPaymentAlpha expectedNextPaymentAlpha) {
        assertNextPaymentAlphaAllUpdatablePropertiesEquals(
            expectedNextPaymentAlpha,
            getPersistedNextPaymentAlpha(expectedNextPaymentAlpha)
        );
    }
}
