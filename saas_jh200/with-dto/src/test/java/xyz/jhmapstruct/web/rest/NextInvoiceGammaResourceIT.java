package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextInvoiceGammaAsserts.*;
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
import xyz.jhmapstruct.domain.NextInvoiceGamma;
import xyz.jhmapstruct.repository.NextInvoiceGammaRepository;
import xyz.jhmapstruct.service.dto.NextInvoiceGammaDTO;
import xyz.jhmapstruct.service.mapper.NextInvoiceGammaMapper;

/**
 * Integration tests for the {@link NextInvoiceGammaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextInvoiceGammaResourceIT {

    private static final String DEFAULT_INVOICE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_ISSUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ISSUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/next-invoice-gammas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextInvoiceGammaRepository nextInvoiceGammaRepository;

    @Autowired
    private NextInvoiceGammaMapper nextInvoiceGammaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextInvoiceGammaMockMvc;

    private NextInvoiceGamma nextInvoiceGamma;

    private NextInvoiceGamma insertedNextInvoiceGamma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextInvoiceGamma createEntity() {
        return new NextInvoiceGamma()
            .invoiceNumber(DEFAULT_INVOICE_NUMBER)
            .issueDate(DEFAULT_ISSUE_DATE)
            .dueDate(DEFAULT_DUE_DATE)
            .amount(DEFAULT_AMOUNT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextInvoiceGamma createUpdatedEntity() {
        return new NextInvoiceGamma()
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);
    }

    @BeforeEach
    public void initTest() {
        nextInvoiceGamma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextInvoiceGamma != null) {
            nextInvoiceGammaRepository.delete(insertedNextInvoiceGamma);
            insertedNextInvoiceGamma = null;
        }
    }

    @Test
    @Transactional
    void createNextInvoiceGamma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextInvoiceGamma
        NextInvoiceGammaDTO nextInvoiceGammaDTO = nextInvoiceGammaMapper.toDto(nextInvoiceGamma);
        var returnedNextInvoiceGammaDTO = om.readValue(
            restNextInvoiceGammaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceGammaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextInvoiceGammaDTO.class
        );

        // Validate the NextInvoiceGamma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextInvoiceGamma = nextInvoiceGammaMapper.toEntity(returnedNextInvoiceGammaDTO);
        assertNextInvoiceGammaUpdatableFieldsEquals(returnedNextInvoiceGamma, getPersistedNextInvoiceGamma(returnedNextInvoiceGamma));

        insertedNextInvoiceGamma = returnedNextInvoiceGamma;
    }

    @Test
    @Transactional
    void createNextInvoiceGammaWithExistingId() throws Exception {
        // Create the NextInvoiceGamma with an existing ID
        nextInvoiceGamma.setId(1L);
        NextInvoiceGammaDTO nextInvoiceGammaDTO = nextInvoiceGammaMapper.toDto(nextInvoiceGamma);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextInvoiceGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceGammaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkInvoiceNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextInvoiceGamma.setInvoiceNumber(null);

        // Create the NextInvoiceGamma, which fails.
        NextInvoiceGammaDTO nextInvoiceGammaDTO = nextInvoiceGammaMapper.toDto(nextInvoiceGamma);

        restNextInvoiceGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceGammaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIssueDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextInvoiceGamma.setIssueDate(null);

        // Create the NextInvoiceGamma, which fails.
        NextInvoiceGammaDTO nextInvoiceGammaDTO = nextInvoiceGammaMapper.toDto(nextInvoiceGamma);

        restNextInvoiceGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceGammaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextInvoiceGamma.setAmount(null);

        // Create the NextInvoiceGamma, which fails.
        NextInvoiceGammaDTO nextInvoiceGammaDTO = nextInvoiceGammaMapper.toDto(nextInvoiceGamma);

        restNextInvoiceGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceGammaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextInvoiceGammas() throws Exception {
        // Initialize the database
        insertedNextInvoiceGamma = nextInvoiceGammaRepository.saveAndFlush(nextInvoiceGamma);

        // Get all the nextInvoiceGammaList
        restNextInvoiceGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextInvoiceGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));
    }

    @Test
    @Transactional
    void getNextInvoiceGamma() throws Exception {
        // Initialize the database
        insertedNextInvoiceGamma = nextInvoiceGammaRepository.saveAndFlush(nextInvoiceGamma);

        // Get the nextInvoiceGamma
        restNextInvoiceGammaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextInvoiceGamma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextInvoiceGamma.getId().intValue()))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER))
            .andExpect(jsonPath("$.issueDate").value(DEFAULT_ISSUE_DATE.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)));
    }

    @Test
    @Transactional
    void getNextInvoiceGammasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextInvoiceGamma = nextInvoiceGammaRepository.saveAndFlush(nextInvoiceGamma);

        Long id = nextInvoiceGamma.getId();

        defaultNextInvoiceGammaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextInvoiceGammaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextInvoiceGammaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextInvoiceGammasByInvoiceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceGamma = nextInvoiceGammaRepository.saveAndFlush(nextInvoiceGamma);

        // Get all the nextInvoiceGammaList where invoiceNumber equals to
        defaultNextInvoiceGammaFiltering(
            "invoiceNumber.equals=" + DEFAULT_INVOICE_NUMBER,
            "invoiceNumber.equals=" + UPDATED_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceGammasByInvoiceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoiceGamma = nextInvoiceGammaRepository.saveAndFlush(nextInvoiceGamma);

        // Get all the nextInvoiceGammaList where invoiceNumber in
        defaultNextInvoiceGammaFiltering(
            "invoiceNumber.in=" + DEFAULT_INVOICE_NUMBER + "," + UPDATED_INVOICE_NUMBER,
            "invoiceNumber.in=" + UPDATED_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceGammasByInvoiceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoiceGamma = nextInvoiceGammaRepository.saveAndFlush(nextInvoiceGamma);

        // Get all the nextInvoiceGammaList where invoiceNumber is not null
        defaultNextInvoiceGammaFiltering("invoiceNumber.specified=true", "invoiceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoiceGammasByInvoiceNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceGamma = nextInvoiceGammaRepository.saveAndFlush(nextInvoiceGamma);

        // Get all the nextInvoiceGammaList where invoiceNumber contains
        defaultNextInvoiceGammaFiltering(
            "invoiceNumber.contains=" + DEFAULT_INVOICE_NUMBER,
            "invoiceNumber.contains=" + UPDATED_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceGammasByInvoiceNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceGamma = nextInvoiceGammaRepository.saveAndFlush(nextInvoiceGamma);

        // Get all the nextInvoiceGammaList where invoiceNumber does not contain
        defaultNextInvoiceGammaFiltering(
            "invoiceNumber.doesNotContain=" + UPDATED_INVOICE_NUMBER,
            "invoiceNumber.doesNotContain=" + DEFAULT_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceGammasByIssueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceGamma = nextInvoiceGammaRepository.saveAndFlush(nextInvoiceGamma);

        // Get all the nextInvoiceGammaList where issueDate equals to
        defaultNextInvoiceGammaFiltering("issueDate.equals=" + DEFAULT_ISSUE_DATE, "issueDate.equals=" + UPDATED_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllNextInvoiceGammasByIssueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoiceGamma = nextInvoiceGammaRepository.saveAndFlush(nextInvoiceGamma);

        // Get all the nextInvoiceGammaList where issueDate in
        defaultNextInvoiceGammaFiltering(
            "issueDate.in=" + DEFAULT_ISSUE_DATE + "," + UPDATED_ISSUE_DATE,
            "issueDate.in=" + UPDATED_ISSUE_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceGammasByIssueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoiceGamma = nextInvoiceGammaRepository.saveAndFlush(nextInvoiceGamma);

        // Get all the nextInvoiceGammaList where issueDate is not null
        defaultNextInvoiceGammaFiltering("issueDate.specified=true", "issueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoiceGammasByDueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceGamma = nextInvoiceGammaRepository.saveAndFlush(nextInvoiceGamma);

        // Get all the nextInvoiceGammaList where dueDate equals to
        defaultNextInvoiceGammaFiltering("dueDate.equals=" + DEFAULT_DUE_DATE, "dueDate.equals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllNextInvoiceGammasByDueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoiceGamma = nextInvoiceGammaRepository.saveAndFlush(nextInvoiceGamma);

        // Get all the nextInvoiceGammaList where dueDate in
        defaultNextInvoiceGammaFiltering("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE, "dueDate.in=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllNextInvoiceGammasByDueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoiceGamma = nextInvoiceGammaRepository.saveAndFlush(nextInvoiceGamma);

        // Get all the nextInvoiceGammaList where dueDate is not null
        defaultNextInvoiceGammaFiltering("dueDate.specified=true", "dueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoiceGammasByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceGamma = nextInvoiceGammaRepository.saveAndFlush(nextInvoiceGamma);

        // Get all the nextInvoiceGammaList where amount equals to
        defaultNextInvoiceGammaFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceGammasByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoiceGamma = nextInvoiceGammaRepository.saveAndFlush(nextInvoiceGamma);

        // Get all the nextInvoiceGammaList where amount in
        defaultNextInvoiceGammaFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceGammasByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoiceGamma = nextInvoiceGammaRepository.saveAndFlush(nextInvoiceGamma);

        // Get all the nextInvoiceGammaList where amount is not null
        defaultNextInvoiceGammaFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoiceGammasByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceGamma = nextInvoiceGammaRepository.saveAndFlush(nextInvoiceGamma);

        // Get all the nextInvoiceGammaList where amount is greater than or equal to
        defaultNextInvoiceGammaFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceGammasByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceGamma = nextInvoiceGammaRepository.saveAndFlush(nextInvoiceGamma);

        // Get all the nextInvoiceGammaList where amount is less than or equal to
        defaultNextInvoiceGammaFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceGammasByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceGamma = nextInvoiceGammaRepository.saveAndFlush(nextInvoiceGamma);

        // Get all the nextInvoiceGammaList where amount is less than
        defaultNextInvoiceGammaFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceGammasByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceGamma = nextInvoiceGammaRepository.saveAndFlush(nextInvoiceGamma);

        // Get all the nextInvoiceGammaList where amount is greater than
        defaultNextInvoiceGammaFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceGammasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextInvoiceGammaRepository.saveAndFlush(nextInvoiceGamma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextInvoiceGamma.setTenant(tenant);
        nextInvoiceGammaRepository.saveAndFlush(nextInvoiceGamma);
        Long tenantId = tenant.getId();
        // Get all the nextInvoiceGammaList where tenant equals to tenantId
        defaultNextInvoiceGammaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextInvoiceGammaList where tenant equals to (tenantId + 1)
        defaultNextInvoiceGammaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextInvoiceGammaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextInvoiceGammaShouldBeFound(shouldBeFound);
        defaultNextInvoiceGammaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextInvoiceGammaShouldBeFound(String filter) throws Exception {
        restNextInvoiceGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextInvoiceGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));

        // Check, that the count call also returns 1
        restNextInvoiceGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextInvoiceGammaShouldNotBeFound(String filter) throws Exception {
        restNextInvoiceGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextInvoiceGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextInvoiceGamma() throws Exception {
        // Get the nextInvoiceGamma
        restNextInvoiceGammaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextInvoiceGamma() throws Exception {
        // Initialize the database
        insertedNextInvoiceGamma = nextInvoiceGammaRepository.saveAndFlush(nextInvoiceGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextInvoiceGamma
        NextInvoiceGamma updatedNextInvoiceGamma = nextInvoiceGammaRepository.findById(nextInvoiceGamma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextInvoiceGamma are not directly saved in db
        em.detach(updatedNextInvoiceGamma);
        updatedNextInvoiceGamma
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);
        NextInvoiceGammaDTO nextInvoiceGammaDTO = nextInvoiceGammaMapper.toDto(updatedNextInvoiceGamma);

        restNextInvoiceGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextInvoiceGammaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextInvoiceGammaDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextInvoiceGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextInvoiceGammaToMatchAllProperties(updatedNextInvoiceGamma);
    }

    @Test
    @Transactional
    void putNonExistingNextInvoiceGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceGamma.setId(longCount.incrementAndGet());

        // Create the NextInvoiceGamma
        NextInvoiceGammaDTO nextInvoiceGammaDTO = nextInvoiceGammaMapper.toDto(nextInvoiceGamma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextInvoiceGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextInvoiceGammaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextInvoiceGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextInvoiceGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceGamma.setId(longCount.incrementAndGet());

        // Create the NextInvoiceGamma
        NextInvoiceGammaDTO nextInvoiceGammaDTO = nextInvoiceGammaMapper.toDto(nextInvoiceGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextInvoiceGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextInvoiceGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceGamma.setId(longCount.incrementAndGet());

        // Create the NextInvoiceGamma
        NextInvoiceGammaDTO nextInvoiceGammaDTO = nextInvoiceGammaMapper.toDto(nextInvoiceGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceGammaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceGammaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextInvoiceGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextInvoiceGammaWithPatch() throws Exception {
        // Initialize the database
        insertedNextInvoiceGamma = nextInvoiceGammaRepository.saveAndFlush(nextInvoiceGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextInvoiceGamma using partial update
        NextInvoiceGamma partialUpdatedNextInvoiceGamma = new NextInvoiceGamma();
        partialUpdatedNextInvoiceGamma.setId(nextInvoiceGamma.getId());

        partialUpdatedNextInvoiceGamma.issueDate(UPDATED_ISSUE_DATE);

        restNextInvoiceGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextInvoiceGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextInvoiceGamma))
            )
            .andExpect(status().isOk());

        // Validate the NextInvoiceGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextInvoiceGammaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextInvoiceGamma, nextInvoiceGamma),
            getPersistedNextInvoiceGamma(nextInvoiceGamma)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextInvoiceGammaWithPatch() throws Exception {
        // Initialize the database
        insertedNextInvoiceGamma = nextInvoiceGammaRepository.saveAndFlush(nextInvoiceGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextInvoiceGamma using partial update
        NextInvoiceGamma partialUpdatedNextInvoiceGamma = new NextInvoiceGamma();
        partialUpdatedNextInvoiceGamma.setId(nextInvoiceGamma.getId());

        partialUpdatedNextInvoiceGamma
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);

        restNextInvoiceGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextInvoiceGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextInvoiceGamma))
            )
            .andExpect(status().isOk());

        // Validate the NextInvoiceGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextInvoiceGammaUpdatableFieldsEquals(
            partialUpdatedNextInvoiceGamma,
            getPersistedNextInvoiceGamma(partialUpdatedNextInvoiceGamma)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextInvoiceGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceGamma.setId(longCount.incrementAndGet());

        // Create the NextInvoiceGamma
        NextInvoiceGammaDTO nextInvoiceGammaDTO = nextInvoiceGammaMapper.toDto(nextInvoiceGamma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextInvoiceGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextInvoiceGammaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextInvoiceGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextInvoiceGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceGamma.setId(longCount.incrementAndGet());

        // Create the NextInvoiceGamma
        NextInvoiceGammaDTO nextInvoiceGammaDTO = nextInvoiceGammaMapper.toDto(nextInvoiceGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextInvoiceGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextInvoiceGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceGamma.setId(longCount.incrementAndGet());

        // Create the NextInvoiceGamma
        NextInvoiceGammaDTO nextInvoiceGammaDTO = nextInvoiceGammaMapper.toDto(nextInvoiceGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceGammaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextInvoiceGammaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextInvoiceGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextInvoiceGamma() throws Exception {
        // Initialize the database
        insertedNextInvoiceGamma = nextInvoiceGammaRepository.saveAndFlush(nextInvoiceGamma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextInvoiceGamma
        restNextInvoiceGammaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextInvoiceGamma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextInvoiceGammaRepository.count();
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

    protected NextInvoiceGamma getPersistedNextInvoiceGamma(NextInvoiceGamma nextInvoiceGamma) {
        return nextInvoiceGammaRepository.findById(nextInvoiceGamma.getId()).orElseThrow();
    }

    protected void assertPersistedNextInvoiceGammaToMatchAllProperties(NextInvoiceGamma expectedNextInvoiceGamma) {
        assertNextInvoiceGammaAllPropertiesEquals(expectedNextInvoiceGamma, getPersistedNextInvoiceGamma(expectedNextInvoiceGamma));
    }

    protected void assertPersistedNextInvoiceGammaToMatchUpdatableProperties(NextInvoiceGamma expectedNextInvoiceGamma) {
        assertNextInvoiceGammaAllUpdatablePropertiesEquals(
            expectedNextInvoiceGamma,
            getPersistedNextInvoiceGamma(expectedNextInvoiceGamma)
        );
    }
}
