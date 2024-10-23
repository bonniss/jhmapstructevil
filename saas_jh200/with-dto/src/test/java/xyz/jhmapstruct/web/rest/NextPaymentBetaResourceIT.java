package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextPaymentBetaAsserts.*;
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
import xyz.jhmapstruct.domain.NextPaymentBeta;
import xyz.jhmapstruct.domain.enumeration.NextPaymentBetaMethod;
import xyz.jhmapstruct.repository.NextPaymentBetaRepository;
import xyz.jhmapstruct.service.dto.NextPaymentBetaDTO;
import xyz.jhmapstruct.service.mapper.NextPaymentBetaMapper;

/**
 * Integration tests for the {@link NextPaymentBetaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextPaymentBetaResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final Instant DEFAULT_PAYMENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYMENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final NextPaymentBetaMethod DEFAULT_PAYMENT_METHOD = NextPaymentBetaMethod.CREDIT_CARD;
    private static final NextPaymentBetaMethod UPDATED_PAYMENT_METHOD = NextPaymentBetaMethod.PAYPAL;

    private static final String ENTITY_API_URL = "/api/next-payment-betas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextPaymentBetaRepository nextPaymentBetaRepository;

    @Autowired
    private NextPaymentBetaMapper nextPaymentBetaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextPaymentBetaMockMvc;

    private NextPaymentBeta nextPaymentBeta;

    private NextPaymentBeta insertedNextPaymentBeta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextPaymentBeta createEntity() {
        return new NextPaymentBeta().amount(DEFAULT_AMOUNT).paymentDate(DEFAULT_PAYMENT_DATE).paymentMethod(DEFAULT_PAYMENT_METHOD);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextPaymentBeta createUpdatedEntity() {
        return new NextPaymentBeta().amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);
    }

    @BeforeEach
    public void initTest() {
        nextPaymentBeta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextPaymentBeta != null) {
            nextPaymentBetaRepository.delete(insertedNextPaymentBeta);
            insertedNextPaymentBeta = null;
        }
    }

    @Test
    @Transactional
    void createNextPaymentBeta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextPaymentBeta
        NextPaymentBetaDTO nextPaymentBetaDTO = nextPaymentBetaMapper.toDto(nextPaymentBeta);
        var returnedNextPaymentBetaDTO = om.readValue(
            restNextPaymentBetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentBetaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextPaymentBetaDTO.class
        );

        // Validate the NextPaymentBeta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextPaymentBeta = nextPaymentBetaMapper.toEntity(returnedNextPaymentBetaDTO);
        assertNextPaymentBetaUpdatableFieldsEquals(returnedNextPaymentBeta, getPersistedNextPaymentBeta(returnedNextPaymentBeta));

        insertedNextPaymentBeta = returnedNextPaymentBeta;
    }

    @Test
    @Transactional
    void createNextPaymentBetaWithExistingId() throws Exception {
        // Create the NextPaymentBeta with an existing ID
        nextPaymentBeta.setId(1L);
        NextPaymentBetaDTO nextPaymentBetaDTO = nextPaymentBetaMapper.toDto(nextPaymentBeta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextPaymentBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentBetaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextPaymentBeta.setAmount(null);

        // Create the NextPaymentBeta, which fails.
        NextPaymentBetaDTO nextPaymentBetaDTO = nextPaymentBetaMapper.toDto(nextPaymentBeta);

        restNextPaymentBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentBetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextPaymentBeta.setPaymentDate(null);

        // Create the NextPaymentBeta, which fails.
        NextPaymentBetaDTO nextPaymentBetaDTO = nextPaymentBetaMapper.toDto(nextPaymentBeta);

        restNextPaymentBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentBetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextPaymentBetas() throws Exception {
        // Initialize the database
        insertedNextPaymentBeta = nextPaymentBetaRepository.saveAndFlush(nextPaymentBeta);

        // Get all the nextPaymentBetaList
        restNextPaymentBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextPaymentBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));
    }

    @Test
    @Transactional
    void getNextPaymentBeta() throws Exception {
        // Initialize the database
        insertedNextPaymentBeta = nextPaymentBetaRepository.saveAndFlush(nextPaymentBeta);

        // Get the nextPaymentBeta
        restNextPaymentBetaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextPaymentBeta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextPaymentBeta.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()));
    }

    @Test
    @Transactional
    void getNextPaymentBetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextPaymentBeta = nextPaymentBetaRepository.saveAndFlush(nextPaymentBeta);

        Long id = nextPaymentBeta.getId();

        defaultNextPaymentBetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextPaymentBetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextPaymentBetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextPaymentBetasByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentBeta = nextPaymentBetaRepository.saveAndFlush(nextPaymentBeta);

        // Get all the nextPaymentBetaList where amount equals to
        defaultNextPaymentBetaFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentBetasByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextPaymentBeta = nextPaymentBetaRepository.saveAndFlush(nextPaymentBeta);

        // Get all the nextPaymentBetaList where amount in
        defaultNextPaymentBetaFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentBetasByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextPaymentBeta = nextPaymentBetaRepository.saveAndFlush(nextPaymentBeta);

        // Get all the nextPaymentBetaList where amount is not null
        defaultNextPaymentBetaFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllNextPaymentBetasByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentBeta = nextPaymentBetaRepository.saveAndFlush(nextPaymentBeta);

        // Get all the nextPaymentBetaList where amount is greater than or equal to
        defaultNextPaymentBetaFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentBetasByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentBeta = nextPaymentBetaRepository.saveAndFlush(nextPaymentBeta);

        // Get all the nextPaymentBetaList where amount is less than or equal to
        defaultNextPaymentBetaFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentBetasByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentBeta = nextPaymentBetaRepository.saveAndFlush(nextPaymentBeta);

        // Get all the nextPaymentBetaList where amount is less than
        defaultNextPaymentBetaFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentBetasByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentBeta = nextPaymentBetaRepository.saveAndFlush(nextPaymentBeta);

        // Get all the nextPaymentBetaList where amount is greater than
        defaultNextPaymentBetaFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextPaymentBetasByPaymentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentBeta = nextPaymentBetaRepository.saveAndFlush(nextPaymentBeta);

        // Get all the nextPaymentBetaList where paymentDate equals to
        defaultNextPaymentBetaFiltering("paymentDate.equals=" + DEFAULT_PAYMENT_DATE, "paymentDate.equals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllNextPaymentBetasByPaymentDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextPaymentBeta = nextPaymentBetaRepository.saveAndFlush(nextPaymentBeta);

        // Get all the nextPaymentBetaList where paymentDate in
        defaultNextPaymentBetaFiltering(
            "paymentDate.in=" + DEFAULT_PAYMENT_DATE + "," + UPDATED_PAYMENT_DATE,
            "paymentDate.in=" + UPDATED_PAYMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextPaymentBetasByPaymentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextPaymentBeta = nextPaymentBetaRepository.saveAndFlush(nextPaymentBeta);

        // Get all the nextPaymentBetaList where paymentDate is not null
        defaultNextPaymentBetaFiltering("paymentDate.specified=true", "paymentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextPaymentBetasByPaymentMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextPaymentBeta = nextPaymentBetaRepository.saveAndFlush(nextPaymentBeta);

        // Get all the nextPaymentBetaList where paymentMethod equals to
        defaultNextPaymentBetaFiltering("paymentMethod.equals=" + DEFAULT_PAYMENT_METHOD, "paymentMethod.equals=" + UPDATED_PAYMENT_METHOD);
    }

    @Test
    @Transactional
    void getAllNextPaymentBetasByPaymentMethodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextPaymentBeta = nextPaymentBetaRepository.saveAndFlush(nextPaymentBeta);

        // Get all the nextPaymentBetaList where paymentMethod in
        defaultNextPaymentBetaFiltering(
            "paymentMethod.in=" + DEFAULT_PAYMENT_METHOD + "," + UPDATED_PAYMENT_METHOD,
            "paymentMethod.in=" + UPDATED_PAYMENT_METHOD
        );
    }

    @Test
    @Transactional
    void getAllNextPaymentBetasByPaymentMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextPaymentBeta = nextPaymentBetaRepository.saveAndFlush(nextPaymentBeta);

        // Get all the nextPaymentBetaList where paymentMethod is not null
        defaultNextPaymentBetaFiltering("paymentMethod.specified=true", "paymentMethod.specified=false");
    }

    @Test
    @Transactional
    void getAllNextPaymentBetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextPaymentBetaRepository.saveAndFlush(nextPaymentBeta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextPaymentBeta.setTenant(tenant);
        nextPaymentBetaRepository.saveAndFlush(nextPaymentBeta);
        Long tenantId = tenant.getId();
        // Get all the nextPaymentBetaList where tenant equals to tenantId
        defaultNextPaymentBetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextPaymentBetaList where tenant equals to (tenantId + 1)
        defaultNextPaymentBetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextPaymentBetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextPaymentBetaShouldBeFound(shouldBeFound);
        defaultNextPaymentBetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextPaymentBetaShouldBeFound(String filter) throws Exception {
        restNextPaymentBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextPaymentBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));

        // Check, that the count call also returns 1
        restNextPaymentBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextPaymentBetaShouldNotBeFound(String filter) throws Exception {
        restNextPaymentBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextPaymentBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextPaymentBeta() throws Exception {
        // Get the nextPaymentBeta
        restNextPaymentBetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextPaymentBeta() throws Exception {
        // Initialize the database
        insertedNextPaymentBeta = nextPaymentBetaRepository.saveAndFlush(nextPaymentBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextPaymentBeta
        NextPaymentBeta updatedNextPaymentBeta = nextPaymentBetaRepository.findById(nextPaymentBeta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextPaymentBeta are not directly saved in db
        em.detach(updatedNextPaymentBeta);
        updatedNextPaymentBeta.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);
        NextPaymentBetaDTO nextPaymentBetaDTO = nextPaymentBetaMapper.toDto(updatedNextPaymentBeta);

        restNextPaymentBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextPaymentBetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextPaymentBetaDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextPaymentBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextPaymentBetaToMatchAllProperties(updatedNextPaymentBeta);
    }

    @Test
    @Transactional
    void putNonExistingNextPaymentBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentBeta.setId(longCount.incrementAndGet());

        // Create the NextPaymentBeta
        NextPaymentBetaDTO nextPaymentBetaDTO = nextPaymentBetaMapper.toDto(nextPaymentBeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextPaymentBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextPaymentBetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextPaymentBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextPaymentBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentBeta.setId(longCount.incrementAndGet());

        // Create the NextPaymentBeta
        NextPaymentBetaDTO nextPaymentBetaDTO = nextPaymentBetaMapper.toDto(nextPaymentBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextPaymentBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextPaymentBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentBeta.setId(longCount.incrementAndGet());

        // Create the NextPaymentBeta
        NextPaymentBetaDTO nextPaymentBetaDTO = nextPaymentBetaMapper.toDto(nextPaymentBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentBetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextPaymentBetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextPaymentBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextPaymentBetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextPaymentBeta = nextPaymentBetaRepository.saveAndFlush(nextPaymentBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextPaymentBeta using partial update
        NextPaymentBeta partialUpdatedNextPaymentBeta = new NextPaymentBeta();
        partialUpdatedNextPaymentBeta.setId(nextPaymentBeta.getId());

        partialUpdatedNextPaymentBeta.paymentMethod(UPDATED_PAYMENT_METHOD);

        restNextPaymentBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextPaymentBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextPaymentBeta))
            )
            .andExpect(status().isOk());

        // Validate the NextPaymentBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextPaymentBetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextPaymentBeta, nextPaymentBeta),
            getPersistedNextPaymentBeta(nextPaymentBeta)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextPaymentBetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextPaymentBeta = nextPaymentBetaRepository.saveAndFlush(nextPaymentBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextPaymentBeta using partial update
        NextPaymentBeta partialUpdatedNextPaymentBeta = new NextPaymentBeta();
        partialUpdatedNextPaymentBeta.setId(nextPaymentBeta.getId());

        partialUpdatedNextPaymentBeta.amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).paymentMethod(UPDATED_PAYMENT_METHOD);

        restNextPaymentBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextPaymentBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextPaymentBeta))
            )
            .andExpect(status().isOk());

        // Validate the NextPaymentBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextPaymentBetaUpdatableFieldsEquals(
            partialUpdatedNextPaymentBeta,
            getPersistedNextPaymentBeta(partialUpdatedNextPaymentBeta)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextPaymentBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentBeta.setId(longCount.incrementAndGet());

        // Create the NextPaymentBeta
        NextPaymentBetaDTO nextPaymentBetaDTO = nextPaymentBetaMapper.toDto(nextPaymentBeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextPaymentBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextPaymentBetaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextPaymentBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextPaymentBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentBeta.setId(longCount.incrementAndGet());

        // Create the NextPaymentBeta
        NextPaymentBetaDTO nextPaymentBetaDTO = nextPaymentBetaMapper.toDto(nextPaymentBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextPaymentBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextPaymentBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextPaymentBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextPaymentBeta.setId(longCount.incrementAndGet());

        // Create the NextPaymentBeta
        NextPaymentBetaDTO nextPaymentBetaDTO = nextPaymentBetaMapper.toDto(nextPaymentBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextPaymentBetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextPaymentBetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextPaymentBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextPaymentBeta() throws Exception {
        // Initialize the database
        insertedNextPaymentBeta = nextPaymentBetaRepository.saveAndFlush(nextPaymentBeta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextPaymentBeta
        restNextPaymentBetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextPaymentBeta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextPaymentBetaRepository.count();
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

    protected NextPaymentBeta getPersistedNextPaymentBeta(NextPaymentBeta nextPaymentBeta) {
        return nextPaymentBetaRepository.findById(nextPaymentBeta.getId()).orElseThrow();
    }

    protected void assertPersistedNextPaymentBetaToMatchAllProperties(NextPaymentBeta expectedNextPaymentBeta) {
        assertNextPaymentBetaAllPropertiesEquals(expectedNextPaymentBeta, getPersistedNextPaymentBeta(expectedNextPaymentBeta));
    }

    protected void assertPersistedNextPaymentBetaToMatchUpdatableProperties(NextPaymentBeta expectedNextPaymentBeta) {
        assertNextPaymentBetaAllUpdatablePropertiesEquals(expectedNextPaymentBeta, getPersistedNextPaymentBeta(expectedNextPaymentBeta));
    }
}
