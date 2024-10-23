package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextPaymentThetaAsserts.*;
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
import xyz.jhmapstruct.domain.NextPaymentTheta;
import xyz.jhmapstruct.domain.enumeration.NextPaymentThetaMethod;
import xyz.jhmapstruct.repository.NextPaymentThetaRepository;

/**
 * Integration tests for the {@link NextPaymentThetaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextPaymentThetaResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final Instant DEFAULT_PAYMENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYMENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final NextPaymentThetaMethod DEFAULT_PAYMENT_METHOD = NextPaymentThetaMethod.CREDIT_CARD;
    private static final NextPaymentThetaMethod UPDATED_PAYMENT_METHOD = NextPaymentThetaMethod.PAYPAL;

    private static final String ENTITY_API_URL = "/api/next-payment-thetas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextPaymentThetaRepository nextPaymentThetaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextPaymentThetaMockMvc;

    private NextPaymentTheta nextPaymentTheta;

    private NextPaymentTheta insertedNextPaymentTheta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextPaymentTheta createEntity() {
        return new NextPaymentTheta().amount(DEFAULT_AMOUNT).paymentDate(DEFAULT_PAYMENT_DATE).paymentMethod(DEFAULT_PAYMENT_METHOD);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextPaymentTheta createUpdatedEntity() {
        return new NextPaymentTheta().amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);
    }

    @BeforeEach
    public void initTest() {
        nextPaymentTheta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextPaymentTheta != null) {
            nextPaymentThetaRepository.delete(insertedNextPaymentTheta);
            insertedNextPaymentTheta = null;
        }
    }

    @Test
    @Transactional
    void createNextPaymentTheta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextPaymentTheta
        var returnedNextPaymentTheta = om.readValue(
            restNextPaymentThetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentTheta)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextPaymentTheta.class
        );

        // Validate the NextPaymentTheta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextPaymentThetaUpdatableFieldsEquals(returnedNextPaymentTheta, getPersistedNextPaymentTheta(returnedNextPaymentTheta));

        insertedNextPaymentTheta = returnedNextPaymentTheta;
    }

    @Test
    @Transactional
    void createNextPaymentThetaWithExistingId() throws Exception {
        // Create the NextPaymentTheta with an existing ID
        nextPaymentTheta.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextPaymentThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentTheta)))
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextPaymentTheta.setAmount(null);

        // Create the NextPaymentTheta, which fails.

        restNextPaymentThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentTheta)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextPaymentTheta.setPaymentDate(null);

        // Create the NextPaymentTheta, which fails.

        restNextPaymentThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentTheta)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextPaymentThetas() throws Exception {
        // Initialize the database
        insertedNextPaymentTheta = nextPaymentThetaRepository.saveAndFlush(nextPaymentTheta);

        // Get all the nextPaymentThetaList
        restNextPaymentThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextPaymentTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));
    }

    @Test
    @Transactional
    void getNextPaymentTheta() throws Exception {
        // Initialize the database
        insertedNextPaymentTheta = nextPaymentThetaRepository.saveAndFlush(nextPaymentTheta);

        // Get the nextPaymentTheta
        restNextPaymentThetaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextPaymentTheta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextPaymentTheta.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()));
    }

    @Test
    @Transactional
    void getNextPaymentThetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextPaymentTheta = nextPaymentThetaRepository.saveAndFlush(nextPaymentTheta);

        Long id = nextPaymentTheta.getId();

        defaultNextPaymentThetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextPaymentThetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextPaymentThetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextPaymentThetasByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentTheta = nextPaymentThetaRepository.saveAndFlush(nextPaymentTheta);

        // Get all the nextPaymentThetaList where amount equals to
        defaultNextPaymentThetaFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentThetasByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextPaymentTheta = nextPaymentThetaRepository.saveAndFlush(nextPaymentTheta);

        // Get all the nextPaymentThetaList where amount in
        defaultNextPaymentThetaFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentThetasByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextPaymentTheta = nextPaymentThetaRepository.saveAndFlush(nextPaymentTheta);

        // Get all the nextPaymentThetaList where amount is not null
        defaultNextPaymentThetaFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllNextPaymentThetasByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentTheta = nextPaymentThetaRepository.saveAndFlush(nextPaymentTheta);

        // Get all the nextPaymentThetaList where amount is greater than or equal to
        defaultNextPaymentThetaFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentThetasByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentTheta = nextPaymentThetaRepository.saveAndFlush(nextPaymentTheta);

        // Get all the nextPaymentThetaList where amount is less than or equal to
        defaultNextPaymentThetaFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentThetasByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentTheta = nextPaymentThetaRepository.saveAndFlush(nextPaymentTheta);

        // Get all the nextPaymentThetaList where amount is less than
        defaultNextPaymentThetaFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentThetasByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentTheta = nextPaymentThetaRepository.saveAndFlush(nextPaymentTheta);

        // Get all the nextPaymentThetaList where amount is greater than
        defaultNextPaymentThetaFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentThetasByPaymentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentTheta = nextPaymentThetaRepository.saveAndFlush(nextPaymentTheta);

        // Get all the nextPaymentThetaList where paymentDate equals to
        defaultNextPaymentThetaFiltering("paymentDate.equals=" + DEFAULT_PAYMENT_DATE, "paymentDate.equals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllNextPaymentThetasByPaymentDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextPaymentTheta = nextPaymentThetaRepository.saveAndFlush(nextPaymentTheta);

        // Get all the nextPaymentThetaList where paymentDate in
        defaultNextPaymentThetaFiltering(
            "paymentDate.in=" + DEFAULT_PAYMENT_DATE + "," + UPDATED_PAYMENT_DATE,
            "paymentDate.in=" + UPDATED_PAYMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextPaymentThetasByPaymentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextPaymentTheta = nextPaymentThetaRepository.saveAndFlush(nextPaymentTheta);

        // Get all the nextPaymentThetaList where paymentDate is not null
        defaultNextPaymentThetaFiltering("paymentDate.specified=true", "paymentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextPaymentThetasByPaymentMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentTheta = nextPaymentThetaRepository.saveAndFlush(nextPaymentTheta);

        // Get all the nextPaymentThetaList where paymentMethod equals to
        defaultNextPaymentThetaFiltering(
            "paymentMethod.equals=" + DEFAULT_PAYMENT_METHOD,
            "paymentMethod.equals=" + UPDATED_PAYMENT_METHOD
        );
    }

    @Test
    @Transactional
    void getAllNextPaymentThetasByPaymentMethodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextPaymentTheta = nextPaymentThetaRepository.saveAndFlush(nextPaymentTheta);

        // Get all the nextPaymentThetaList where paymentMethod in
        defaultNextPaymentThetaFiltering(
            "paymentMethod.in=" + DEFAULT_PAYMENT_METHOD + "," + UPDATED_PAYMENT_METHOD,
            "paymentMethod.in=" + UPDATED_PAYMENT_METHOD
        );
    }

    @Test
    @Transactional
    void getAllNextPaymentThetasByPaymentMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextPaymentTheta = nextPaymentThetaRepository.saveAndFlush(nextPaymentTheta);

        // Get all the nextPaymentThetaList where paymentMethod is not null
        defaultNextPaymentThetaFiltering("paymentMethod.specified=true", "paymentMethod.specified=false");
    }

    @Test
    @Transactional
    void getAllNextPaymentThetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextPaymentThetaRepository.saveAndFlush(nextPaymentTheta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextPaymentTheta.setTenant(tenant);
        nextPaymentThetaRepository.saveAndFlush(nextPaymentTheta);
        Long tenantId = tenant.getId();
        // Get all the nextPaymentThetaList where tenant equals to tenantId
        defaultNextPaymentThetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextPaymentThetaList where tenant equals to (tenantId + 1)
        defaultNextPaymentThetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextPaymentThetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextPaymentThetaShouldBeFound(shouldBeFound);
        defaultNextPaymentThetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextPaymentThetaShouldBeFound(String filter) throws Exception {
        restNextPaymentThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextPaymentTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));

        // Check, that the count call also returns 1
        restNextPaymentThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextPaymentThetaShouldNotBeFound(String filter) throws Exception {
        restNextPaymentThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextPaymentThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextPaymentTheta() throws Exception {
        // Get the nextPaymentTheta
        restNextPaymentThetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextPaymentTheta() throws Exception {
        // Initialize the database
        insertedNextPaymentTheta = nextPaymentThetaRepository.saveAndFlush(nextPaymentTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextPaymentTheta
        NextPaymentTheta updatedNextPaymentTheta = nextPaymentThetaRepository.findById(nextPaymentTheta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextPaymentTheta are not directly saved in db
        em.detach(updatedNextPaymentTheta);
        updatedNextPaymentTheta.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restNextPaymentThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextPaymentTheta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextPaymentTheta))
            )
            .andExpect(status().isOk());

        // Validate the NextPaymentTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextPaymentThetaToMatchAllProperties(updatedNextPaymentTheta);
    }

    @Test
    @Transactional
    void putNonExistingNextPaymentTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentTheta.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextPaymentThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextPaymentTheta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextPaymentTheta))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextPaymentTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentTheta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextPaymentTheta))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextPaymentTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentTheta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentThetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentTheta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextPaymentTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextPaymentThetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextPaymentTheta = nextPaymentThetaRepository.saveAndFlush(nextPaymentTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextPaymentTheta using partial update
        NextPaymentTheta partialUpdatedNextPaymentTheta = new NextPaymentTheta();
        partialUpdatedNextPaymentTheta.setId(nextPaymentTheta.getId());

        partialUpdatedNextPaymentTheta.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restNextPaymentThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextPaymentTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextPaymentTheta))
            )
            .andExpect(status().isOk());

        // Validate the NextPaymentTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextPaymentThetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextPaymentTheta, nextPaymentTheta),
            getPersistedNextPaymentTheta(nextPaymentTheta)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextPaymentThetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextPaymentTheta = nextPaymentThetaRepository.saveAndFlush(nextPaymentTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextPaymentTheta using partial update
        NextPaymentTheta partialUpdatedNextPaymentTheta = new NextPaymentTheta();
        partialUpdatedNextPaymentTheta.setId(nextPaymentTheta.getId());

        partialUpdatedNextPaymentTheta.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restNextPaymentThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextPaymentTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextPaymentTheta))
            )
            .andExpect(status().isOk());

        // Validate the NextPaymentTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextPaymentThetaUpdatableFieldsEquals(
            partialUpdatedNextPaymentTheta,
            getPersistedNextPaymentTheta(partialUpdatedNextPaymentTheta)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextPaymentTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentTheta.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextPaymentThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextPaymentTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextPaymentTheta))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextPaymentTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentTheta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextPaymentTheta))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextPaymentTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentTheta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentThetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextPaymentTheta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextPaymentTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextPaymentTheta() throws Exception {
        // Initialize the database
        insertedNextPaymentTheta = nextPaymentThetaRepository.saveAndFlush(nextPaymentTheta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextPaymentTheta
        restNextPaymentThetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextPaymentTheta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextPaymentThetaRepository.count();
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

    protected NextPaymentTheta getPersistedNextPaymentTheta(NextPaymentTheta nextPaymentTheta) {
        return nextPaymentThetaRepository.findById(nextPaymentTheta.getId()).orElseThrow();
    }

    protected void assertPersistedNextPaymentThetaToMatchAllProperties(NextPaymentTheta expectedNextPaymentTheta) {
        assertNextPaymentThetaAllPropertiesEquals(expectedNextPaymentTheta, getPersistedNextPaymentTheta(expectedNextPaymentTheta));
    }

    protected void assertPersistedNextPaymentThetaToMatchUpdatableProperties(NextPaymentTheta expectedNextPaymentTheta) {
        assertNextPaymentThetaAllUpdatablePropertiesEquals(
            expectedNextPaymentTheta,
            getPersistedNextPaymentTheta(expectedNextPaymentTheta)
        );
    }
}
