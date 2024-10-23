package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextPaymentAsserts.*;
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
import xyz.jhmapstruct.domain.NextPayment;
import xyz.jhmapstruct.domain.enumeration.NextPaymentMethod;
import xyz.jhmapstruct.repository.NextPaymentRepository;

/**
 * Integration tests for the {@link NextPaymentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextPaymentResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final Instant DEFAULT_PAYMENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYMENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final NextPaymentMethod DEFAULT_PAYMENT_METHOD = NextPaymentMethod.CREDIT_CARD;
    private static final NextPaymentMethod UPDATED_PAYMENT_METHOD = NextPaymentMethod.PAYPAL;

    private static final String ENTITY_API_URL = "/api/next-payments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextPaymentRepository nextPaymentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextPaymentMockMvc;

    private NextPayment nextPayment;

    private NextPayment insertedNextPayment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextPayment createEntity() {
        return new NextPayment().amount(DEFAULT_AMOUNT).paymentDate(DEFAULT_PAYMENT_DATE).paymentMethod(DEFAULT_PAYMENT_METHOD);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextPayment createUpdatedEntity() {
        return new NextPayment().amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);
    }

    @BeforeEach
    public void initTest() {
        nextPayment = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextPayment != null) {
            nextPaymentRepository.delete(insertedNextPayment);
            insertedNextPayment = null;
        }
    }

    @Test
    @Transactional
    void createNextPayment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextPayment
        var returnedNextPayment = om.readValue(
            restNextPaymentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPayment)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextPayment.class
        );

        // Validate the NextPayment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextPaymentUpdatableFieldsEquals(returnedNextPayment, getPersistedNextPayment(returnedNextPayment));

        insertedNextPayment = returnedNextPayment;
    }

    @Test
    @Transactional
    void createNextPaymentWithExistingId() throws Exception {
        // Create the NextPayment with an existing ID
        nextPayment.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPayment)))
            .andExpect(status().isBadRequest());

        // Validate the NextPayment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextPayment.setAmount(null);

        // Create the NextPayment, which fails.

        restNextPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPayment)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextPayment.setPaymentDate(null);

        // Create the NextPayment, which fails.

        restNextPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPayment)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextPayments() throws Exception {
        // Initialize the database
        insertedNextPayment = nextPaymentRepository.saveAndFlush(nextPayment);

        // Get all the nextPaymentList
        restNextPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextPayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));
    }

    @Test
    @Transactional
    void getNextPayment() throws Exception {
        // Initialize the database
        insertedNextPayment = nextPaymentRepository.saveAndFlush(nextPayment);

        // Get the nextPayment
        restNextPaymentMockMvc
            .perform(get(ENTITY_API_URL_ID, nextPayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextPayment.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()));
    }

    @Test
    @Transactional
    void getNextPaymentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextPayment = nextPaymentRepository.saveAndFlush(nextPayment);

        Long id = nextPayment.getId();

        defaultNextPaymentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextPaymentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextPaymentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextPaymentsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPayment = nextPaymentRepository.saveAndFlush(nextPayment);

        // Get all the nextPaymentList where amount equals to
        defaultNextPaymentFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextPayment = nextPaymentRepository.saveAndFlush(nextPayment);

        // Get all the nextPaymentList where amount in
        defaultNextPaymentFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextPayment = nextPaymentRepository.saveAndFlush(nextPayment);

        // Get all the nextPaymentList where amount is not null
        defaultNextPaymentFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllNextPaymentsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPayment = nextPaymentRepository.saveAndFlush(nextPayment);

        // Get all the nextPaymentList where amount is greater than or equal to
        defaultNextPaymentFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPayment = nextPaymentRepository.saveAndFlush(nextPayment);

        // Get all the nextPaymentList where amount is less than or equal to
        defaultNextPaymentFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextPayment = nextPaymentRepository.saveAndFlush(nextPayment);

        // Get all the nextPaymentList where amount is less than
        defaultNextPaymentFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextPayment = nextPaymentRepository.saveAndFlush(nextPayment);

        // Get all the nextPaymentList where amount is greater than
        defaultNextPaymentFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentsByPaymentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPayment = nextPaymentRepository.saveAndFlush(nextPayment);

        // Get all the nextPaymentList where paymentDate equals to
        defaultNextPaymentFiltering("paymentDate.equals=" + DEFAULT_PAYMENT_DATE, "paymentDate.equals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllNextPaymentsByPaymentDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextPayment = nextPaymentRepository.saveAndFlush(nextPayment);

        // Get all the nextPaymentList where paymentDate in
        defaultNextPaymentFiltering(
            "paymentDate.in=" + DEFAULT_PAYMENT_DATE + "," + UPDATED_PAYMENT_DATE,
            "paymentDate.in=" + UPDATED_PAYMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextPaymentsByPaymentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextPayment = nextPaymentRepository.saveAndFlush(nextPayment);

        // Get all the nextPaymentList where paymentDate is not null
        defaultNextPaymentFiltering("paymentDate.specified=true", "paymentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextPaymentsByPaymentMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPayment = nextPaymentRepository.saveAndFlush(nextPayment);

        // Get all the nextPaymentList where paymentMethod equals to
        defaultNextPaymentFiltering("paymentMethod.equals=" + DEFAULT_PAYMENT_METHOD, "paymentMethod.equals=" + UPDATED_PAYMENT_METHOD);
    }

    @Test
    @Transactional
    void getAllNextPaymentsByPaymentMethodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextPayment = nextPaymentRepository.saveAndFlush(nextPayment);

        // Get all the nextPaymentList where paymentMethod in
        defaultNextPaymentFiltering(
            "paymentMethod.in=" + DEFAULT_PAYMENT_METHOD + "," + UPDATED_PAYMENT_METHOD,
            "paymentMethod.in=" + UPDATED_PAYMENT_METHOD
        );
    }

    @Test
    @Transactional
    void getAllNextPaymentsByPaymentMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextPayment = nextPaymentRepository.saveAndFlush(nextPayment);

        // Get all the nextPaymentList where paymentMethod is not null
        defaultNextPaymentFiltering("paymentMethod.specified=true", "paymentMethod.specified=false");
    }

    @Test
    @Transactional
    void getAllNextPaymentsByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextPaymentRepository.saveAndFlush(nextPayment);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextPayment.setTenant(tenant);
        nextPaymentRepository.saveAndFlush(nextPayment);
        Long tenantId = tenant.getId();
        // Get all the nextPaymentList where tenant equals to tenantId
        defaultNextPaymentShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextPaymentList where tenant equals to (tenantId + 1)
        defaultNextPaymentShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextPaymentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextPaymentShouldBeFound(shouldBeFound);
        defaultNextPaymentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextPaymentShouldBeFound(String filter) throws Exception {
        restNextPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextPayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));

        // Check, that the count call also returns 1
        restNextPaymentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextPaymentShouldNotBeFound(String filter) throws Exception {
        restNextPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextPaymentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextPayment() throws Exception {
        // Get the nextPayment
        restNextPaymentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextPayment() throws Exception {
        // Initialize the database
        insertedNextPayment = nextPaymentRepository.saveAndFlush(nextPayment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextPayment
        NextPayment updatedNextPayment = nextPaymentRepository.findById(nextPayment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextPayment are not directly saved in db
        em.detach(updatedNextPayment);
        updatedNextPayment.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restNextPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextPayment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextPayment))
            )
            .andExpect(status().isOk());

        // Validate the NextPayment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextPaymentToMatchAllProperties(updatedNextPayment);
    }

    @Test
    @Transactional
    void putNonExistingNextPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPayment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextPayment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextPayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPayment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPayment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextPayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPayment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPayment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPayment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextPayment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextPaymentWithPatch() throws Exception {
        // Initialize the database
        insertedNextPayment = nextPaymentRepository.saveAndFlush(nextPayment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextPayment using partial update
        NextPayment partialUpdatedNextPayment = new NextPayment();
        partialUpdatedNextPayment.setId(nextPayment.getId());

        partialUpdatedNextPayment.paymentMethod(UPDATED_PAYMENT_METHOD);

        restNextPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextPayment))
            )
            .andExpect(status().isOk());

        // Validate the NextPayment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextPaymentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextPayment, nextPayment),
            getPersistedNextPayment(nextPayment)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextPaymentWithPatch() throws Exception {
        // Initialize the database
        insertedNextPayment = nextPaymentRepository.saveAndFlush(nextPayment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextPayment using partial update
        NextPayment partialUpdatedNextPayment = new NextPayment();
        partialUpdatedNextPayment.setId(nextPayment.getId());

        partialUpdatedNextPayment.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restNextPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextPayment))
            )
            .andExpect(status().isOk());

        // Validate the NextPayment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextPaymentUpdatableFieldsEquals(partialUpdatedNextPayment, getPersistedNextPayment(partialUpdatedNextPayment));
    }

    @Test
    @Transactional
    void patchNonExistingNextPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPayment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextPayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPayment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPayment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextPayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPayment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPayment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextPayment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextPayment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextPayment() throws Exception {
        // Initialize the database
        insertedNextPayment = nextPaymentRepository.saveAndFlush(nextPayment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextPayment
        restNextPaymentMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextPayment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextPaymentRepository.count();
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

    protected NextPayment getPersistedNextPayment(NextPayment nextPayment) {
        return nextPaymentRepository.findById(nextPayment.getId()).orElseThrow();
    }

    protected void assertPersistedNextPaymentToMatchAllProperties(NextPayment expectedNextPayment) {
        assertNextPaymentAllPropertiesEquals(expectedNextPayment, getPersistedNextPayment(expectedNextPayment));
    }

    protected void assertPersistedNextPaymentToMatchUpdatableProperties(NextPayment expectedNextPayment) {
        assertNextPaymentAllUpdatablePropertiesEquals(expectedNextPayment, getPersistedNextPayment(expectedNextPayment));
    }
}
