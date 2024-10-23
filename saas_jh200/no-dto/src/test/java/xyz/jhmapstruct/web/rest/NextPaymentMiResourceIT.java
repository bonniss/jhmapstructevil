package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextPaymentMiAsserts.*;
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
import xyz.jhmapstruct.domain.NextPaymentMi;
import xyz.jhmapstruct.domain.enumeration.NextPaymentMiMethod;
import xyz.jhmapstruct.repository.NextPaymentMiRepository;

/**
 * Integration tests for the {@link NextPaymentMiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextPaymentMiResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final Instant DEFAULT_PAYMENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYMENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final NextPaymentMiMethod DEFAULT_PAYMENT_METHOD = NextPaymentMiMethod.CREDIT_CARD;
    private static final NextPaymentMiMethod UPDATED_PAYMENT_METHOD = NextPaymentMiMethod.PAYPAL;

    private static final String ENTITY_API_URL = "/api/next-payment-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextPaymentMiRepository nextPaymentMiRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextPaymentMiMockMvc;

    private NextPaymentMi nextPaymentMi;

    private NextPaymentMi insertedNextPaymentMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextPaymentMi createEntity() {
        return new NextPaymentMi().amount(DEFAULT_AMOUNT).paymentDate(DEFAULT_PAYMENT_DATE).paymentMethod(DEFAULT_PAYMENT_METHOD);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextPaymentMi createUpdatedEntity() {
        return new NextPaymentMi().amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);
    }

    @BeforeEach
    public void initTest() {
        nextPaymentMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextPaymentMi != null) {
            nextPaymentMiRepository.delete(insertedNextPaymentMi);
            insertedNextPaymentMi = null;
        }
    }

    @Test
    @Transactional
    void createNextPaymentMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextPaymentMi
        var returnedNextPaymentMi = om.readValue(
            restNextPaymentMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentMi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextPaymentMi.class
        );

        // Validate the NextPaymentMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextPaymentMiUpdatableFieldsEquals(returnedNextPaymentMi, getPersistedNextPaymentMi(returnedNextPaymentMi));

        insertedNextPaymentMi = returnedNextPaymentMi;
    }

    @Test
    @Transactional
    void createNextPaymentMiWithExistingId() throws Exception {
        // Create the NextPaymentMi with an existing ID
        nextPaymentMi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextPaymentMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentMi)))
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextPaymentMi.setAmount(null);

        // Create the NextPaymentMi, which fails.

        restNextPaymentMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextPaymentMi.setPaymentDate(null);

        // Create the NextPaymentMi, which fails.

        restNextPaymentMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextPaymentMis() throws Exception {
        // Initialize the database
        insertedNextPaymentMi = nextPaymentMiRepository.saveAndFlush(nextPaymentMi);

        // Get all the nextPaymentMiList
        restNextPaymentMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextPaymentMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));
    }

    @Test
    @Transactional
    void getNextPaymentMi() throws Exception {
        // Initialize the database
        insertedNextPaymentMi = nextPaymentMiRepository.saveAndFlush(nextPaymentMi);

        // Get the nextPaymentMi
        restNextPaymentMiMockMvc
            .perform(get(ENTITY_API_URL_ID, nextPaymentMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextPaymentMi.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()));
    }

    @Test
    @Transactional
    void getNextPaymentMisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextPaymentMi = nextPaymentMiRepository.saveAndFlush(nextPaymentMi);

        Long id = nextPaymentMi.getId();

        defaultNextPaymentMiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextPaymentMiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextPaymentMiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextPaymentMisByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentMi = nextPaymentMiRepository.saveAndFlush(nextPaymentMi);

        // Get all the nextPaymentMiList where amount equals to
        defaultNextPaymentMiFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentMisByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextPaymentMi = nextPaymentMiRepository.saveAndFlush(nextPaymentMi);

        // Get all the nextPaymentMiList where amount in
        defaultNextPaymentMiFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentMisByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextPaymentMi = nextPaymentMiRepository.saveAndFlush(nextPaymentMi);

        // Get all the nextPaymentMiList where amount is not null
        defaultNextPaymentMiFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllNextPaymentMisByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentMi = nextPaymentMiRepository.saveAndFlush(nextPaymentMi);

        // Get all the nextPaymentMiList where amount is greater than or equal to
        defaultNextPaymentMiFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentMisByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentMi = nextPaymentMiRepository.saveAndFlush(nextPaymentMi);

        // Get all the nextPaymentMiList where amount is less than or equal to
        defaultNextPaymentMiFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentMisByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentMi = nextPaymentMiRepository.saveAndFlush(nextPaymentMi);

        // Get all the nextPaymentMiList where amount is less than
        defaultNextPaymentMiFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentMisByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentMi = nextPaymentMiRepository.saveAndFlush(nextPaymentMi);

        // Get all the nextPaymentMiList where amount is greater than
        defaultNextPaymentMiFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentMisByPaymentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentMi = nextPaymentMiRepository.saveAndFlush(nextPaymentMi);

        // Get all the nextPaymentMiList where paymentDate equals to
        defaultNextPaymentMiFiltering("paymentDate.equals=" + DEFAULT_PAYMENT_DATE, "paymentDate.equals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllNextPaymentMisByPaymentDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextPaymentMi = nextPaymentMiRepository.saveAndFlush(nextPaymentMi);

        // Get all the nextPaymentMiList where paymentDate in
        defaultNextPaymentMiFiltering(
            "paymentDate.in=" + DEFAULT_PAYMENT_DATE + "," + UPDATED_PAYMENT_DATE,
            "paymentDate.in=" + UPDATED_PAYMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextPaymentMisByPaymentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextPaymentMi = nextPaymentMiRepository.saveAndFlush(nextPaymentMi);

        // Get all the nextPaymentMiList where paymentDate is not null
        defaultNextPaymentMiFiltering("paymentDate.specified=true", "paymentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextPaymentMisByPaymentMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentMi = nextPaymentMiRepository.saveAndFlush(nextPaymentMi);

        // Get all the nextPaymentMiList where paymentMethod equals to
        defaultNextPaymentMiFiltering("paymentMethod.equals=" + DEFAULT_PAYMENT_METHOD, "paymentMethod.equals=" + UPDATED_PAYMENT_METHOD);
    }

    @Test
    @Transactional
    void getAllNextPaymentMisByPaymentMethodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextPaymentMi = nextPaymentMiRepository.saveAndFlush(nextPaymentMi);

        // Get all the nextPaymentMiList where paymentMethod in
        defaultNextPaymentMiFiltering(
            "paymentMethod.in=" + DEFAULT_PAYMENT_METHOD + "," + UPDATED_PAYMENT_METHOD,
            "paymentMethod.in=" + UPDATED_PAYMENT_METHOD
        );
    }

    @Test
    @Transactional
    void getAllNextPaymentMisByPaymentMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextPaymentMi = nextPaymentMiRepository.saveAndFlush(nextPaymentMi);

        // Get all the nextPaymentMiList where paymentMethod is not null
        defaultNextPaymentMiFiltering("paymentMethod.specified=true", "paymentMethod.specified=false");
    }

    @Test
    @Transactional
    void getAllNextPaymentMisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextPaymentMiRepository.saveAndFlush(nextPaymentMi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextPaymentMi.setTenant(tenant);
        nextPaymentMiRepository.saveAndFlush(nextPaymentMi);
        Long tenantId = tenant.getId();
        // Get all the nextPaymentMiList where tenant equals to tenantId
        defaultNextPaymentMiShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextPaymentMiList where tenant equals to (tenantId + 1)
        defaultNextPaymentMiShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextPaymentMiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextPaymentMiShouldBeFound(shouldBeFound);
        defaultNextPaymentMiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextPaymentMiShouldBeFound(String filter) throws Exception {
        restNextPaymentMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextPaymentMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));

        // Check, that the count call also returns 1
        restNextPaymentMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextPaymentMiShouldNotBeFound(String filter) throws Exception {
        restNextPaymentMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextPaymentMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextPaymentMi() throws Exception {
        // Get the nextPaymentMi
        restNextPaymentMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextPaymentMi() throws Exception {
        // Initialize the database
        insertedNextPaymentMi = nextPaymentMiRepository.saveAndFlush(nextPaymentMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextPaymentMi
        NextPaymentMi updatedNextPaymentMi = nextPaymentMiRepository.findById(nextPaymentMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextPaymentMi are not directly saved in db
        em.detach(updatedNextPaymentMi);
        updatedNextPaymentMi.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restNextPaymentMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextPaymentMi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextPaymentMi))
            )
            .andExpect(status().isOk());

        // Validate the NextPaymentMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextPaymentMiToMatchAllProperties(updatedNextPaymentMi);
    }

    @Test
    @Transactional
    void putNonExistingNextPaymentMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentMi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextPaymentMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextPaymentMi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextPaymentMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextPaymentMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextPaymentMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextPaymentMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentMi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextPaymentMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextPaymentMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextPaymentMi = nextPaymentMiRepository.saveAndFlush(nextPaymentMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextPaymentMi using partial update
        NextPaymentMi partialUpdatedNextPaymentMi = new NextPaymentMi();
        partialUpdatedNextPaymentMi.setId(nextPaymentMi.getId());

        partialUpdatedNextPaymentMi.amount(UPDATED_AMOUNT).paymentMethod(UPDATED_PAYMENT_METHOD);

        restNextPaymentMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextPaymentMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextPaymentMi))
            )
            .andExpect(status().isOk());

        // Validate the NextPaymentMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextPaymentMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextPaymentMi, nextPaymentMi),
            getPersistedNextPaymentMi(nextPaymentMi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextPaymentMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextPaymentMi = nextPaymentMiRepository.saveAndFlush(nextPaymentMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextPaymentMi using partial update
        NextPaymentMi partialUpdatedNextPaymentMi = new NextPaymentMi();
        partialUpdatedNextPaymentMi.setId(nextPaymentMi.getId());

        partialUpdatedNextPaymentMi.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restNextPaymentMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextPaymentMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextPaymentMi))
            )
            .andExpect(status().isOk());

        // Validate the NextPaymentMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextPaymentMiUpdatableFieldsEquals(partialUpdatedNextPaymentMi, getPersistedNextPaymentMi(partialUpdatedNextPaymentMi));
    }

    @Test
    @Transactional
    void patchNonExistingNextPaymentMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentMi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextPaymentMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextPaymentMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextPaymentMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextPaymentMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextPaymentMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextPaymentMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextPaymentMi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextPaymentMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextPaymentMi() throws Exception {
        // Initialize the database
        insertedNextPaymentMi = nextPaymentMiRepository.saveAndFlush(nextPaymentMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextPaymentMi
        restNextPaymentMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextPaymentMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextPaymentMiRepository.count();
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

    protected NextPaymentMi getPersistedNextPaymentMi(NextPaymentMi nextPaymentMi) {
        return nextPaymentMiRepository.findById(nextPaymentMi.getId()).orElseThrow();
    }

    protected void assertPersistedNextPaymentMiToMatchAllProperties(NextPaymentMi expectedNextPaymentMi) {
        assertNextPaymentMiAllPropertiesEquals(expectedNextPaymentMi, getPersistedNextPaymentMi(expectedNextPaymentMi));
    }

    protected void assertPersistedNextPaymentMiToMatchUpdatableProperties(NextPaymentMi expectedNextPaymentMi) {
        assertNextPaymentMiAllUpdatablePropertiesEquals(expectedNextPaymentMi, getPersistedNextPaymentMi(expectedNextPaymentMi));
    }
}
