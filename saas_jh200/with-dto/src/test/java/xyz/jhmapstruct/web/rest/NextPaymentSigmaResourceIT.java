package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextPaymentSigmaAsserts.*;
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
import xyz.jhmapstruct.domain.NextPaymentSigma;
import xyz.jhmapstruct.domain.enumeration.NextPaymentSigmaMethod;
import xyz.jhmapstruct.repository.NextPaymentSigmaRepository;
import xyz.jhmapstruct.service.dto.NextPaymentSigmaDTO;
import xyz.jhmapstruct.service.mapper.NextPaymentSigmaMapper;

/**
 * Integration tests for the {@link NextPaymentSigmaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextPaymentSigmaResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final Instant DEFAULT_PAYMENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYMENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final NextPaymentSigmaMethod DEFAULT_PAYMENT_METHOD = NextPaymentSigmaMethod.CREDIT_CARD;
    private static final NextPaymentSigmaMethod UPDATED_PAYMENT_METHOD = NextPaymentSigmaMethod.PAYPAL;

    private static final String ENTITY_API_URL = "/api/next-payment-sigmas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextPaymentSigmaRepository nextPaymentSigmaRepository;

    @Autowired
    private NextPaymentSigmaMapper nextPaymentSigmaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextPaymentSigmaMockMvc;

    private NextPaymentSigma nextPaymentSigma;

    private NextPaymentSigma insertedNextPaymentSigma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextPaymentSigma createEntity() {
        return new NextPaymentSigma().amount(DEFAULT_AMOUNT).paymentDate(DEFAULT_PAYMENT_DATE).paymentMethod(DEFAULT_PAYMENT_METHOD);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextPaymentSigma createUpdatedEntity() {
        return new NextPaymentSigma().amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);
    }

    @BeforeEach
    public void initTest() {
        nextPaymentSigma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextPaymentSigma != null) {
            nextPaymentSigmaRepository.delete(insertedNextPaymentSigma);
            insertedNextPaymentSigma = null;
        }
    }

    @Test
    @Transactional
    void createNextPaymentSigma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextPaymentSigma
        NextPaymentSigmaDTO nextPaymentSigmaDTO = nextPaymentSigmaMapper.toDto(nextPaymentSigma);
        var returnedNextPaymentSigmaDTO = om.readValue(
            restNextPaymentSigmaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentSigmaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextPaymentSigmaDTO.class
        );

        // Validate the NextPaymentSigma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextPaymentSigma = nextPaymentSigmaMapper.toEntity(returnedNextPaymentSigmaDTO);
        assertNextPaymentSigmaUpdatableFieldsEquals(returnedNextPaymentSigma, getPersistedNextPaymentSigma(returnedNextPaymentSigma));

        insertedNextPaymentSigma = returnedNextPaymentSigma;
    }

    @Test
    @Transactional
    void createNextPaymentSigmaWithExistingId() throws Exception {
        // Create the NextPaymentSigma with an existing ID
        nextPaymentSigma.setId(1L);
        NextPaymentSigmaDTO nextPaymentSigmaDTO = nextPaymentSigmaMapper.toDto(nextPaymentSigma);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextPaymentSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentSigmaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextPaymentSigma.setAmount(null);

        // Create the NextPaymentSigma, which fails.
        NextPaymentSigmaDTO nextPaymentSigmaDTO = nextPaymentSigmaMapper.toDto(nextPaymentSigma);

        restNextPaymentSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentSigmaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextPaymentSigma.setPaymentDate(null);

        // Create the NextPaymentSigma, which fails.
        NextPaymentSigmaDTO nextPaymentSigmaDTO = nextPaymentSigmaMapper.toDto(nextPaymentSigma);

        restNextPaymentSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentSigmaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextPaymentSigmas() throws Exception {
        // Initialize the database
        insertedNextPaymentSigma = nextPaymentSigmaRepository.saveAndFlush(nextPaymentSigma);

        // Get all the nextPaymentSigmaList
        restNextPaymentSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextPaymentSigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));
    }

    @Test
    @Transactional
    void getNextPaymentSigma() throws Exception {
        // Initialize the database
        insertedNextPaymentSigma = nextPaymentSigmaRepository.saveAndFlush(nextPaymentSigma);

        // Get the nextPaymentSigma
        restNextPaymentSigmaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextPaymentSigma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextPaymentSigma.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()));
    }

    @Test
    @Transactional
    void getNextPaymentSigmasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextPaymentSigma = nextPaymentSigmaRepository.saveAndFlush(nextPaymentSigma);

        Long id = nextPaymentSigma.getId();

        defaultNextPaymentSigmaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextPaymentSigmaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextPaymentSigmaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextPaymentSigmasByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentSigma = nextPaymentSigmaRepository.saveAndFlush(nextPaymentSigma);

        // Get all the nextPaymentSigmaList where amount equals to
        defaultNextPaymentSigmaFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentSigmasByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextPaymentSigma = nextPaymentSigmaRepository.saveAndFlush(nextPaymentSigma);

        // Get all the nextPaymentSigmaList where amount in
        defaultNextPaymentSigmaFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentSigmasByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextPaymentSigma = nextPaymentSigmaRepository.saveAndFlush(nextPaymentSigma);

        // Get all the nextPaymentSigmaList where amount is not null
        defaultNextPaymentSigmaFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllNextPaymentSigmasByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentSigma = nextPaymentSigmaRepository.saveAndFlush(nextPaymentSigma);

        // Get all the nextPaymentSigmaList where amount is greater than or equal to
        defaultNextPaymentSigmaFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentSigmasByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentSigma = nextPaymentSigmaRepository.saveAndFlush(nextPaymentSigma);

        // Get all the nextPaymentSigmaList where amount is less than or equal to
        defaultNextPaymentSigmaFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentSigmasByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentSigma = nextPaymentSigmaRepository.saveAndFlush(nextPaymentSigma);

        // Get all the nextPaymentSigmaList where amount is less than
        defaultNextPaymentSigmaFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentSigmasByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentSigma = nextPaymentSigmaRepository.saveAndFlush(nextPaymentSigma);

        // Get all the nextPaymentSigmaList where amount is greater than
        defaultNextPaymentSigmaFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentSigmasByPaymentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentSigma = nextPaymentSigmaRepository.saveAndFlush(nextPaymentSigma);

        // Get all the nextPaymentSigmaList where paymentDate equals to
        defaultNextPaymentSigmaFiltering("paymentDate.equals=" + DEFAULT_PAYMENT_DATE, "paymentDate.equals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllNextPaymentSigmasByPaymentDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextPaymentSigma = nextPaymentSigmaRepository.saveAndFlush(nextPaymentSigma);

        // Get all the nextPaymentSigmaList where paymentDate in
        defaultNextPaymentSigmaFiltering(
            "paymentDate.in=" + DEFAULT_PAYMENT_DATE + "," + UPDATED_PAYMENT_DATE,
            "paymentDate.in=" + UPDATED_PAYMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextPaymentSigmasByPaymentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextPaymentSigma = nextPaymentSigmaRepository.saveAndFlush(nextPaymentSigma);

        // Get all the nextPaymentSigmaList where paymentDate is not null
        defaultNextPaymentSigmaFiltering("paymentDate.specified=true", "paymentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextPaymentSigmasByPaymentMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentSigma = nextPaymentSigmaRepository.saveAndFlush(nextPaymentSigma);

        // Get all the nextPaymentSigmaList where paymentMethod equals to
        defaultNextPaymentSigmaFiltering(
            "paymentMethod.equals=" + DEFAULT_PAYMENT_METHOD,
            "paymentMethod.equals=" + UPDATED_PAYMENT_METHOD
        );
    }

    @Test
    @Transactional
    void getAllNextPaymentSigmasByPaymentMethodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextPaymentSigma = nextPaymentSigmaRepository.saveAndFlush(nextPaymentSigma);

        // Get all the nextPaymentSigmaList where paymentMethod in
        defaultNextPaymentSigmaFiltering(
            "paymentMethod.in=" + DEFAULT_PAYMENT_METHOD + "," + UPDATED_PAYMENT_METHOD,
            "paymentMethod.in=" + UPDATED_PAYMENT_METHOD
        );
    }

    @Test
    @Transactional
    void getAllNextPaymentSigmasByPaymentMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextPaymentSigma = nextPaymentSigmaRepository.saveAndFlush(nextPaymentSigma);

        // Get all the nextPaymentSigmaList where paymentMethod is not null
        defaultNextPaymentSigmaFiltering("paymentMethod.specified=true", "paymentMethod.specified=false");
    }

    @Test
    @Transactional
    void getAllNextPaymentSigmasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextPaymentSigmaRepository.saveAndFlush(nextPaymentSigma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextPaymentSigma.setTenant(tenant);
        nextPaymentSigmaRepository.saveAndFlush(nextPaymentSigma);
        Long tenantId = tenant.getId();
        // Get all the nextPaymentSigmaList where tenant equals to tenantId
        defaultNextPaymentSigmaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextPaymentSigmaList where tenant equals to (tenantId + 1)
        defaultNextPaymentSigmaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextPaymentSigmaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextPaymentSigmaShouldBeFound(shouldBeFound);
        defaultNextPaymentSigmaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextPaymentSigmaShouldBeFound(String filter) throws Exception {
        restNextPaymentSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextPaymentSigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));

        // Check, that the count call also returns 1
        restNextPaymentSigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextPaymentSigmaShouldNotBeFound(String filter) throws Exception {
        restNextPaymentSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextPaymentSigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextPaymentSigma() throws Exception {
        // Get the nextPaymentSigma
        restNextPaymentSigmaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextPaymentSigma() throws Exception {
        // Initialize the database
        insertedNextPaymentSigma = nextPaymentSigmaRepository.saveAndFlush(nextPaymentSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextPaymentSigma
        NextPaymentSigma updatedNextPaymentSigma = nextPaymentSigmaRepository.findById(nextPaymentSigma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextPaymentSigma are not directly saved in db
        em.detach(updatedNextPaymentSigma);
        updatedNextPaymentSigma.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);
        NextPaymentSigmaDTO nextPaymentSigmaDTO = nextPaymentSigmaMapper.toDto(updatedNextPaymentSigma);

        restNextPaymentSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextPaymentSigmaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextPaymentSigmaDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextPaymentSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextPaymentSigmaToMatchAllProperties(updatedNextPaymentSigma);
    }

    @Test
    @Transactional
    void putNonExistingNextPaymentSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentSigma.setId(longCount.incrementAndGet());

        // Create the NextPaymentSigma
        NextPaymentSigmaDTO nextPaymentSigmaDTO = nextPaymentSigmaMapper.toDto(nextPaymentSigma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextPaymentSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextPaymentSigmaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextPaymentSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextPaymentSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentSigma.setId(longCount.incrementAndGet());

        // Create the NextPaymentSigma
        NextPaymentSigmaDTO nextPaymentSigmaDTO = nextPaymentSigmaMapper.toDto(nextPaymentSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextPaymentSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextPaymentSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentSigma.setId(longCount.incrementAndGet());

        // Create the NextPaymentSigma
        NextPaymentSigmaDTO nextPaymentSigmaDTO = nextPaymentSigmaMapper.toDto(nextPaymentSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentSigmaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentSigmaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextPaymentSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextPaymentSigmaWithPatch() throws Exception {
        // Initialize the database
        insertedNextPaymentSigma = nextPaymentSigmaRepository.saveAndFlush(nextPaymentSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextPaymentSigma using partial update
        NextPaymentSigma partialUpdatedNextPaymentSigma = new NextPaymentSigma();
        partialUpdatedNextPaymentSigma.setId(nextPaymentSigma.getId());

        partialUpdatedNextPaymentSigma.amount(UPDATED_AMOUNT);

        restNextPaymentSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextPaymentSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextPaymentSigma))
            )
            .andExpect(status().isOk());

        // Validate the NextPaymentSigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextPaymentSigmaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextPaymentSigma, nextPaymentSigma),
            getPersistedNextPaymentSigma(nextPaymentSigma)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextPaymentSigmaWithPatch() throws Exception {
        // Initialize the database
        insertedNextPaymentSigma = nextPaymentSigmaRepository.saveAndFlush(nextPaymentSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextPaymentSigma using partial update
        NextPaymentSigma partialUpdatedNextPaymentSigma = new NextPaymentSigma();
        partialUpdatedNextPaymentSigma.setId(nextPaymentSigma.getId());

        partialUpdatedNextPaymentSigma.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restNextPaymentSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextPaymentSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextPaymentSigma))
            )
            .andExpect(status().isOk());

        // Validate the NextPaymentSigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextPaymentSigmaUpdatableFieldsEquals(
            partialUpdatedNextPaymentSigma,
            getPersistedNextPaymentSigma(partialUpdatedNextPaymentSigma)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextPaymentSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentSigma.setId(longCount.incrementAndGet());

        // Create the NextPaymentSigma
        NextPaymentSigmaDTO nextPaymentSigmaDTO = nextPaymentSigmaMapper.toDto(nextPaymentSigma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextPaymentSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextPaymentSigmaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextPaymentSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextPaymentSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentSigma.setId(longCount.incrementAndGet());

        // Create the NextPaymentSigma
        NextPaymentSigmaDTO nextPaymentSigmaDTO = nextPaymentSigmaMapper.toDto(nextPaymentSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextPaymentSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextPaymentSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentSigma.setId(longCount.incrementAndGet());

        // Create the NextPaymentSigma
        NextPaymentSigmaDTO nextPaymentSigmaDTO = nextPaymentSigmaMapper.toDto(nextPaymentSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentSigmaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextPaymentSigmaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextPaymentSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextPaymentSigma() throws Exception {
        // Initialize the database
        insertedNextPaymentSigma = nextPaymentSigmaRepository.saveAndFlush(nextPaymentSigma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextPaymentSigma
        restNextPaymentSigmaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextPaymentSigma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextPaymentSigmaRepository.count();
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

    protected NextPaymentSigma getPersistedNextPaymentSigma(NextPaymentSigma nextPaymentSigma) {
        return nextPaymentSigmaRepository.findById(nextPaymentSigma.getId()).orElseThrow();
    }

    protected void assertPersistedNextPaymentSigmaToMatchAllProperties(NextPaymentSigma expectedNextPaymentSigma) {
        assertNextPaymentSigmaAllPropertiesEquals(expectedNextPaymentSigma, getPersistedNextPaymentSigma(expectedNextPaymentSigma));
    }

    protected void assertPersistedNextPaymentSigmaToMatchUpdatableProperties(NextPaymentSigma expectedNextPaymentSigma) {
        assertNextPaymentSigmaAllUpdatablePropertiesEquals(
            expectedNextPaymentSigma,
            getPersistedNextPaymentSigma(expectedNextPaymentSigma)
        );
    }
}
