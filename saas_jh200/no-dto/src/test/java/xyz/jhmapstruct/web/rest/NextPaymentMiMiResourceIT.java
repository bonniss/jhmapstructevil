package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextPaymentMiMiAsserts.*;
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
import xyz.jhmapstruct.domain.NextPaymentMiMi;
import xyz.jhmapstruct.domain.enumeration.NextPaymentMiMiMethod;
import xyz.jhmapstruct.repository.NextPaymentMiMiRepository;

/**
 * Integration tests for the {@link NextPaymentMiMiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextPaymentMiMiResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final Instant DEFAULT_PAYMENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYMENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final NextPaymentMiMiMethod DEFAULT_PAYMENT_METHOD = NextPaymentMiMiMethod.CREDIT_CARD;
    private static final NextPaymentMiMiMethod UPDATED_PAYMENT_METHOD = NextPaymentMiMiMethod.PAYPAL;

    private static final String ENTITY_API_URL = "/api/next-payment-mi-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextPaymentMiMiRepository nextPaymentMiMiRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextPaymentMiMiMockMvc;

    private NextPaymentMiMi nextPaymentMiMi;

    private NextPaymentMiMi insertedNextPaymentMiMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextPaymentMiMi createEntity() {
        return new NextPaymentMiMi().amount(DEFAULT_AMOUNT).paymentDate(DEFAULT_PAYMENT_DATE).paymentMethod(DEFAULT_PAYMENT_METHOD);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextPaymentMiMi createUpdatedEntity() {
        return new NextPaymentMiMi().amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);
    }

    @BeforeEach
    public void initTest() {
        nextPaymentMiMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextPaymentMiMi != null) {
            nextPaymentMiMiRepository.delete(insertedNextPaymentMiMi);
            insertedNextPaymentMiMi = null;
        }
    }

    @Test
    @Transactional
    void createNextPaymentMiMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextPaymentMiMi
        var returnedNextPaymentMiMi = om.readValue(
            restNextPaymentMiMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentMiMi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextPaymentMiMi.class
        );

        // Validate the NextPaymentMiMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextPaymentMiMiUpdatableFieldsEquals(returnedNextPaymentMiMi, getPersistedNextPaymentMiMi(returnedNextPaymentMiMi));

        insertedNextPaymentMiMi = returnedNextPaymentMiMi;
    }

    @Test
    @Transactional
    void createNextPaymentMiMiWithExistingId() throws Exception {
        // Create the NextPaymentMiMi with an existing ID
        nextPaymentMiMi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextPaymentMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentMiMi)))
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextPaymentMiMi.setAmount(null);

        // Create the NextPaymentMiMi, which fails.

        restNextPaymentMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentMiMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextPaymentMiMi.setPaymentDate(null);

        // Create the NextPaymentMiMi, which fails.

        restNextPaymentMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentMiMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextPaymentMiMis() throws Exception {
        // Initialize the database
        insertedNextPaymentMiMi = nextPaymentMiMiRepository.saveAndFlush(nextPaymentMiMi);

        // Get all the nextPaymentMiMiList
        restNextPaymentMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextPaymentMiMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));
    }

    @Test
    @Transactional
    void getNextPaymentMiMi() throws Exception {
        // Initialize the database
        insertedNextPaymentMiMi = nextPaymentMiMiRepository.saveAndFlush(nextPaymentMiMi);

        // Get the nextPaymentMiMi
        restNextPaymentMiMiMockMvc
            .perform(get(ENTITY_API_URL_ID, nextPaymentMiMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextPaymentMiMi.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()));
    }

    @Test
    @Transactional
    void getNextPaymentMiMisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextPaymentMiMi = nextPaymentMiMiRepository.saveAndFlush(nextPaymentMiMi);

        Long id = nextPaymentMiMi.getId();

        defaultNextPaymentMiMiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextPaymentMiMiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextPaymentMiMiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextPaymentMiMisByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentMiMi = nextPaymentMiMiRepository.saveAndFlush(nextPaymentMiMi);

        // Get all the nextPaymentMiMiList where amount equals to
        defaultNextPaymentMiMiFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentMiMisByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextPaymentMiMi = nextPaymentMiMiRepository.saveAndFlush(nextPaymentMiMi);

        // Get all the nextPaymentMiMiList where amount in
        defaultNextPaymentMiMiFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentMiMisByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextPaymentMiMi = nextPaymentMiMiRepository.saveAndFlush(nextPaymentMiMi);

        // Get all the nextPaymentMiMiList where amount is not null
        defaultNextPaymentMiMiFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllNextPaymentMiMisByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentMiMi = nextPaymentMiMiRepository.saveAndFlush(nextPaymentMiMi);

        // Get all the nextPaymentMiMiList where amount is greater than or equal to
        defaultNextPaymentMiMiFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentMiMisByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentMiMi = nextPaymentMiMiRepository.saveAndFlush(nextPaymentMiMi);

        // Get all the nextPaymentMiMiList where amount is less than or equal to
        defaultNextPaymentMiMiFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentMiMisByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentMiMi = nextPaymentMiMiRepository.saveAndFlush(nextPaymentMiMi);

        // Get all the nextPaymentMiMiList where amount is less than
        defaultNextPaymentMiMiFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentMiMisByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentMiMi = nextPaymentMiMiRepository.saveAndFlush(nextPaymentMiMi);

        // Get all the nextPaymentMiMiList where amount is greater than
        defaultNextPaymentMiMiFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentMiMisByPaymentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentMiMi = nextPaymentMiMiRepository.saveAndFlush(nextPaymentMiMi);

        // Get all the nextPaymentMiMiList where paymentDate equals to
        defaultNextPaymentMiMiFiltering("paymentDate.equals=" + DEFAULT_PAYMENT_DATE, "paymentDate.equals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllNextPaymentMiMisByPaymentDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextPaymentMiMi = nextPaymentMiMiRepository.saveAndFlush(nextPaymentMiMi);

        // Get all the nextPaymentMiMiList where paymentDate in
        defaultNextPaymentMiMiFiltering(
            "paymentDate.in=" + DEFAULT_PAYMENT_DATE + "," + UPDATED_PAYMENT_DATE,
            "paymentDate.in=" + UPDATED_PAYMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextPaymentMiMisByPaymentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextPaymentMiMi = nextPaymentMiMiRepository.saveAndFlush(nextPaymentMiMi);

        // Get all the nextPaymentMiMiList where paymentDate is not null
        defaultNextPaymentMiMiFiltering("paymentDate.specified=true", "paymentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextPaymentMiMisByPaymentMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentMiMi = nextPaymentMiMiRepository.saveAndFlush(nextPaymentMiMi);

        // Get all the nextPaymentMiMiList where paymentMethod equals to
        defaultNextPaymentMiMiFiltering("paymentMethod.equals=" + DEFAULT_PAYMENT_METHOD, "paymentMethod.equals=" + UPDATED_PAYMENT_METHOD);
    }

    @Test
    @Transactional
    void getAllNextPaymentMiMisByPaymentMethodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextPaymentMiMi = nextPaymentMiMiRepository.saveAndFlush(nextPaymentMiMi);

        // Get all the nextPaymentMiMiList where paymentMethod in
        defaultNextPaymentMiMiFiltering(
            "paymentMethod.in=" + DEFAULT_PAYMENT_METHOD + "," + UPDATED_PAYMENT_METHOD,
            "paymentMethod.in=" + UPDATED_PAYMENT_METHOD
        );
    }

    @Test
    @Transactional
    void getAllNextPaymentMiMisByPaymentMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextPaymentMiMi = nextPaymentMiMiRepository.saveAndFlush(nextPaymentMiMi);

        // Get all the nextPaymentMiMiList where paymentMethod is not null
        defaultNextPaymentMiMiFiltering("paymentMethod.specified=true", "paymentMethod.specified=false");
    }

    @Test
    @Transactional
    void getAllNextPaymentMiMisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextPaymentMiMiRepository.saveAndFlush(nextPaymentMiMi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextPaymentMiMi.setTenant(tenant);
        nextPaymentMiMiRepository.saveAndFlush(nextPaymentMiMi);
        Long tenantId = tenant.getId();
        // Get all the nextPaymentMiMiList where tenant equals to tenantId
        defaultNextPaymentMiMiShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextPaymentMiMiList where tenant equals to (tenantId + 1)
        defaultNextPaymentMiMiShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextPaymentMiMiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextPaymentMiMiShouldBeFound(shouldBeFound);
        defaultNextPaymentMiMiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextPaymentMiMiShouldBeFound(String filter) throws Exception {
        restNextPaymentMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextPaymentMiMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));

        // Check, that the count call also returns 1
        restNextPaymentMiMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextPaymentMiMiShouldNotBeFound(String filter) throws Exception {
        restNextPaymentMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextPaymentMiMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextPaymentMiMi() throws Exception {
        // Get the nextPaymentMiMi
        restNextPaymentMiMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextPaymentMiMi() throws Exception {
        // Initialize the database
        insertedNextPaymentMiMi = nextPaymentMiMiRepository.saveAndFlush(nextPaymentMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextPaymentMiMi
        NextPaymentMiMi updatedNextPaymentMiMi = nextPaymentMiMiRepository.findById(nextPaymentMiMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextPaymentMiMi are not directly saved in db
        em.detach(updatedNextPaymentMiMi);
        updatedNextPaymentMiMi.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restNextPaymentMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextPaymentMiMi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextPaymentMiMi))
            )
            .andExpect(status().isOk());

        // Validate the NextPaymentMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextPaymentMiMiToMatchAllProperties(updatedNextPaymentMiMi);
    }

    @Test
    @Transactional
    void putNonExistingNextPaymentMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentMiMi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextPaymentMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextPaymentMiMi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextPaymentMiMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextPaymentMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentMiMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextPaymentMiMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextPaymentMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentMiMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentMiMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentMiMi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextPaymentMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextPaymentMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextPaymentMiMi = nextPaymentMiMiRepository.saveAndFlush(nextPaymentMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextPaymentMiMi using partial update
        NextPaymentMiMi partialUpdatedNextPaymentMiMi = new NextPaymentMiMi();
        partialUpdatedNextPaymentMiMi.setId(nextPaymentMiMi.getId());

        partialUpdatedNextPaymentMiMi.paymentMethod(UPDATED_PAYMENT_METHOD);

        restNextPaymentMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextPaymentMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextPaymentMiMi))
            )
            .andExpect(status().isOk());

        // Validate the NextPaymentMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextPaymentMiMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextPaymentMiMi, nextPaymentMiMi),
            getPersistedNextPaymentMiMi(nextPaymentMiMi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextPaymentMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextPaymentMiMi = nextPaymentMiMiRepository.saveAndFlush(nextPaymentMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextPaymentMiMi using partial update
        NextPaymentMiMi partialUpdatedNextPaymentMiMi = new NextPaymentMiMi();
        partialUpdatedNextPaymentMiMi.setId(nextPaymentMiMi.getId());

        partialUpdatedNextPaymentMiMi.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restNextPaymentMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextPaymentMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextPaymentMiMi))
            )
            .andExpect(status().isOk());

        // Validate the NextPaymentMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextPaymentMiMiUpdatableFieldsEquals(
            partialUpdatedNextPaymentMiMi,
            getPersistedNextPaymentMiMi(partialUpdatedNextPaymentMiMi)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextPaymentMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentMiMi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextPaymentMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextPaymentMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextPaymentMiMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextPaymentMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentMiMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextPaymentMiMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextPaymentMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentMiMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentMiMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextPaymentMiMi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextPaymentMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextPaymentMiMi() throws Exception {
        // Initialize the database
        insertedNextPaymentMiMi = nextPaymentMiMiRepository.saveAndFlush(nextPaymentMiMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextPaymentMiMi
        restNextPaymentMiMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextPaymentMiMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextPaymentMiMiRepository.count();
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

    protected NextPaymentMiMi getPersistedNextPaymentMiMi(NextPaymentMiMi nextPaymentMiMi) {
        return nextPaymentMiMiRepository.findById(nextPaymentMiMi.getId()).orElseThrow();
    }

    protected void assertPersistedNextPaymentMiMiToMatchAllProperties(NextPaymentMiMi expectedNextPaymentMiMi) {
        assertNextPaymentMiMiAllPropertiesEquals(expectedNextPaymentMiMi, getPersistedNextPaymentMiMi(expectedNextPaymentMiMi));
    }

    protected void assertPersistedNextPaymentMiMiToMatchUpdatableProperties(NextPaymentMiMi expectedNextPaymentMiMi) {
        assertNextPaymentMiMiAllUpdatablePropertiesEquals(expectedNextPaymentMiMi, getPersistedNextPaymentMiMi(expectedNextPaymentMiMi));
    }
}
