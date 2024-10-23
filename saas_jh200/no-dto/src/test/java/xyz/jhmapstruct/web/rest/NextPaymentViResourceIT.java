package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextPaymentViAsserts.*;
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
import xyz.jhmapstruct.domain.NextPaymentVi;
import xyz.jhmapstruct.domain.enumeration.NextPaymentViMethod;
import xyz.jhmapstruct.repository.NextPaymentViRepository;

/**
 * Integration tests for the {@link NextPaymentViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextPaymentViResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final Instant DEFAULT_PAYMENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYMENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final NextPaymentViMethod DEFAULT_PAYMENT_METHOD = NextPaymentViMethod.CREDIT_CARD;
    private static final NextPaymentViMethod UPDATED_PAYMENT_METHOD = NextPaymentViMethod.PAYPAL;

    private static final String ENTITY_API_URL = "/api/next-payment-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextPaymentViRepository nextPaymentViRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextPaymentViMockMvc;

    private NextPaymentVi nextPaymentVi;

    private NextPaymentVi insertedNextPaymentVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextPaymentVi createEntity() {
        return new NextPaymentVi().amount(DEFAULT_AMOUNT).paymentDate(DEFAULT_PAYMENT_DATE).paymentMethod(DEFAULT_PAYMENT_METHOD);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextPaymentVi createUpdatedEntity() {
        return new NextPaymentVi().amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);
    }

    @BeforeEach
    public void initTest() {
        nextPaymentVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextPaymentVi != null) {
            nextPaymentViRepository.delete(insertedNextPaymentVi);
            insertedNextPaymentVi = null;
        }
    }

    @Test
    @Transactional
    void createNextPaymentVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextPaymentVi
        var returnedNextPaymentVi = om.readValue(
            restNextPaymentViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextPaymentVi.class
        );

        // Validate the NextPaymentVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextPaymentViUpdatableFieldsEquals(returnedNextPaymentVi, getPersistedNextPaymentVi(returnedNextPaymentVi));

        insertedNextPaymentVi = returnedNextPaymentVi;
    }

    @Test
    @Transactional
    void createNextPaymentViWithExistingId() throws Exception {
        // Create the NextPaymentVi with an existing ID
        nextPaymentVi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextPaymentViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentVi)))
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextPaymentVi.setAmount(null);

        // Create the NextPaymentVi, which fails.

        restNextPaymentViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextPaymentVi.setPaymentDate(null);

        // Create the NextPaymentVi, which fails.

        restNextPaymentViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextPaymentVis() throws Exception {
        // Initialize the database
        insertedNextPaymentVi = nextPaymentViRepository.saveAndFlush(nextPaymentVi);

        // Get all the nextPaymentViList
        restNextPaymentViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextPaymentVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));
    }

    @Test
    @Transactional
    void getNextPaymentVi() throws Exception {
        // Initialize the database
        insertedNextPaymentVi = nextPaymentViRepository.saveAndFlush(nextPaymentVi);

        // Get the nextPaymentVi
        restNextPaymentViMockMvc
            .perform(get(ENTITY_API_URL_ID, nextPaymentVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextPaymentVi.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()));
    }

    @Test
    @Transactional
    void getNextPaymentVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextPaymentVi = nextPaymentViRepository.saveAndFlush(nextPaymentVi);

        Long id = nextPaymentVi.getId();

        defaultNextPaymentViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextPaymentViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextPaymentViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextPaymentVisByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentVi = nextPaymentViRepository.saveAndFlush(nextPaymentVi);

        // Get all the nextPaymentViList where amount equals to
        defaultNextPaymentViFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentVisByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextPaymentVi = nextPaymentViRepository.saveAndFlush(nextPaymentVi);

        // Get all the nextPaymentViList where amount in
        defaultNextPaymentViFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentVisByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextPaymentVi = nextPaymentViRepository.saveAndFlush(nextPaymentVi);

        // Get all the nextPaymentViList where amount is not null
        defaultNextPaymentViFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllNextPaymentVisByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentVi = nextPaymentViRepository.saveAndFlush(nextPaymentVi);

        // Get all the nextPaymentViList where amount is greater than or equal to
        defaultNextPaymentViFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentVisByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentVi = nextPaymentViRepository.saveAndFlush(nextPaymentVi);

        // Get all the nextPaymentViList where amount is less than or equal to
        defaultNextPaymentViFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentVisByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentVi = nextPaymentViRepository.saveAndFlush(nextPaymentVi);

        // Get all the nextPaymentViList where amount is less than
        defaultNextPaymentViFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentVisByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentVi = nextPaymentViRepository.saveAndFlush(nextPaymentVi);

        // Get all the nextPaymentViList where amount is greater than
        defaultNextPaymentViFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentVisByPaymentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentVi = nextPaymentViRepository.saveAndFlush(nextPaymentVi);

        // Get all the nextPaymentViList where paymentDate equals to
        defaultNextPaymentViFiltering("paymentDate.equals=" + DEFAULT_PAYMENT_DATE, "paymentDate.equals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllNextPaymentVisByPaymentDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextPaymentVi = nextPaymentViRepository.saveAndFlush(nextPaymentVi);

        // Get all the nextPaymentViList where paymentDate in
        defaultNextPaymentViFiltering(
            "paymentDate.in=" + DEFAULT_PAYMENT_DATE + "," + UPDATED_PAYMENT_DATE,
            "paymentDate.in=" + UPDATED_PAYMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextPaymentVisByPaymentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextPaymentVi = nextPaymentViRepository.saveAndFlush(nextPaymentVi);

        // Get all the nextPaymentViList where paymentDate is not null
        defaultNextPaymentViFiltering("paymentDate.specified=true", "paymentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextPaymentVisByPaymentMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentVi = nextPaymentViRepository.saveAndFlush(nextPaymentVi);

        // Get all the nextPaymentViList where paymentMethod equals to
        defaultNextPaymentViFiltering("paymentMethod.equals=" + DEFAULT_PAYMENT_METHOD, "paymentMethod.equals=" + UPDATED_PAYMENT_METHOD);
    }

    @Test
    @Transactional
    void getAllNextPaymentVisByPaymentMethodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextPaymentVi = nextPaymentViRepository.saveAndFlush(nextPaymentVi);

        // Get all the nextPaymentViList where paymentMethod in
        defaultNextPaymentViFiltering(
            "paymentMethod.in=" + DEFAULT_PAYMENT_METHOD + "," + UPDATED_PAYMENT_METHOD,
            "paymentMethod.in=" + UPDATED_PAYMENT_METHOD
        );
    }

    @Test
    @Transactional
    void getAllNextPaymentVisByPaymentMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextPaymentVi = nextPaymentViRepository.saveAndFlush(nextPaymentVi);

        // Get all the nextPaymentViList where paymentMethod is not null
        defaultNextPaymentViFiltering("paymentMethod.specified=true", "paymentMethod.specified=false");
    }

    @Test
    @Transactional
    void getAllNextPaymentVisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextPaymentViRepository.saveAndFlush(nextPaymentVi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextPaymentVi.setTenant(tenant);
        nextPaymentViRepository.saveAndFlush(nextPaymentVi);
        Long tenantId = tenant.getId();
        // Get all the nextPaymentViList where tenant equals to tenantId
        defaultNextPaymentViShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextPaymentViList where tenant equals to (tenantId + 1)
        defaultNextPaymentViShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextPaymentViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextPaymentViShouldBeFound(shouldBeFound);
        defaultNextPaymentViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextPaymentViShouldBeFound(String filter) throws Exception {
        restNextPaymentViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextPaymentVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));

        // Check, that the count call also returns 1
        restNextPaymentViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextPaymentViShouldNotBeFound(String filter) throws Exception {
        restNextPaymentViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextPaymentViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextPaymentVi() throws Exception {
        // Get the nextPaymentVi
        restNextPaymentViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextPaymentVi() throws Exception {
        // Initialize the database
        insertedNextPaymentVi = nextPaymentViRepository.saveAndFlush(nextPaymentVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextPaymentVi
        NextPaymentVi updatedNextPaymentVi = nextPaymentViRepository.findById(nextPaymentVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextPaymentVi are not directly saved in db
        em.detach(updatedNextPaymentVi);
        updatedNextPaymentVi.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restNextPaymentViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextPaymentVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextPaymentVi))
            )
            .andExpect(status().isOk());

        // Validate the NextPaymentVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextPaymentViToMatchAllProperties(updatedNextPaymentVi);
    }

    @Test
    @Transactional
    void putNonExistingNextPaymentVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextPaymentViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextPaymentVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextPaymentVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextPaymentVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextPaymentVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextPaymentVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextPaymentVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextPaymentViWithPatch() throws Exception {
        // Initialize the database
        insertedNextPaymentVi = nextPaymentViRepository.saveAndFlush(nextPaymentVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextPaymentVi using partial update
        NextPaymentVi partialUpdatedNextPaymentVi = new NextPaymentVi();
        partialUpdatedNextPaymentVi.setId(nextPaymentVi.getId());

        partialUpdatedNextPaymentVi.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE);

        restNextPaymentViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextPaymentVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextPaymentVi))
            )
            .andExpect(status().isOk());

        // Validate the NextPaymentVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextPaymentViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextPaymentVi, nextPaymentVi),
            getPersistedNextPaymentVi(nextPaymentVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextPaymentViWithPatch() throws Exception {
        // Initialize the database
        insertedNextPaymentVi = nextPaymentViRepository.saveAndFlush(nextPaymentVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextPaymentVi using partial update
        NextPaymentVi partialUpdatedNextPaymentVi = new NextPaymentVi();
        partialUpdatedNextPaymentVi.setId(nextPaymentVi.getId());

        partialUpdatedNextPaymentVi.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restNextPaymentViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextPaymentVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextPaymentVi))
            )
            .andExpect(status().isOk());

        // Validate the NextPaymentVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextPaymentViUpdatableFieldsEquals(partialUpdatedNextPaymentVi, getPersistedNextPaymentVi(partialUpdatedNextPaymentVi));
    }

    @Test
    @Transactional
    void patchNonExistingNextPaymentVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextPaymentViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextPaymentVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextPaymentVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextPaymentVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextPaymentVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextPaymentVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextPaymentVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextPaymentVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextPaymentVi() throws Exception {
        // Initialize the database
        insertedNextPaymentVi = nextPaymentViRepository.saveAndFlush(nextPaymentVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextPaymentVi
        restNextPaymentViMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextPaymentVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextPaymentViRepository.count();
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

    protected NextPaymentVi getPersistedNextPaymentVi(NextPaymentVi nextPaymentVi) {
        return nextPaymentViRepository.findById(nextPaymentVi.getId()).orElseThrow();
    }

    protected void assertPersistedNextPaymentViToMatchAllProperties(NextPaymentVi expectedNextPaymentVi) {
        assertNextPaymentViAllPropertiesEquals(expectedNextPaymentVi, getPersistedNextPaymentVi(expectedNextPaymentVi));
    }

    protected void assertPersistedNextPaymentViToMatchUpdatableProperties(NextPaymentVi expectedNextPaymentVi) {
        assertNextPaymentViAllUpdatablePropertiesEquals(expectedNextPaymentVi, getPersistedNextPaymentVi(expectedNextPaymentVi));
    }
}
