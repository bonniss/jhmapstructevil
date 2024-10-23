package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextInvoiceSigmaAsserts.*;
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
import xyz.jhmapstruct.domain.NextInvoiceSigma;
import xyz.jhmapstruct.repository.NextInvoiceSigmaRepository;
import xyz.jhmapstruct.service.dto.NextInvoiceSigmaDTO;
import xyz.jhmapstruct.service.mapper.NextInvoiceSigmaMapper;

/**
 * Integration tests for the {@link NextInvoiceSigmaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextInvoiceSigmaResourceIT {

    private static final String DEFAULT_INVOICE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_ISSUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ISSUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/next-invoice-sigmas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextInvoiceSigmaRepository nextInvoiceSigmaRepository;

    @Autowired
    private NextInvoiceSigmaMapper nextInvoiceSigmaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextInvoiceSigmaMockMvc;

    private NextInvoiceSigma nextInvoiceSigma;

    private NextInvoiceSigma insertedNextInvoiceSigma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextInvoiceSigma createEntity() {
        return new NextInvoiceSigma()
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
    public static NextInvoiceSigma createUpdatedEntity() {
        return new NextInvoiceSigma()
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);
    }

    @BeforeEach
    public void initTest() {
        nextInvoiceSigma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextInvoiceSigma != null) {
            nextInvoiceSigmaRepository.delete(insertedNextInvoiceSigma);
            insertedNextInvoiceSigma = null;
        }
    }

    @Test
    @Transactional
    void createNextInvoiceSigma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextInvoiceSigma
        NextInvoiceSigmaDTO nextInvoiceSigmaDTO = nextInvoiceSigmaMapper.toDto(nextInvoiceSigma);
        var returnedNextInvoiceSigmaDTO = om.readValue(
            restNextInvoiceSigmaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceSigmaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextInvoiceSigmaDTO.class
        );

        // Validate the NextInvoiceSigma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextInvoiceSigma = nextInvoiceSigmaMapper.toEntity(returnedNextInvoiceSigmaDTO);
        assertNextInvoiceSigmaUpdatableFieldsEquals(returnedNextInvoiceSigma, getPersistedNextInvoiceSigma(returnedNextInvoiceSigma));

        insertedNextInvoiceSigma = returnedNextInvoiceSigma;
    }

    @Test
    @Transactional
    void createNextInvoiceSigmaWithExistingId() throws Exception {
        // Create the NextInvoiceSigma with an existing ID
        nextInvoiceSigma.setId(1L);
        NextInvoiceSigmaDTO nextInvoiceSigmaDTO = nextInvoiceSigmaMapper.toDto(nextInvoiceSigma);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextInvoiceSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceSigmaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkInvoiceNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextInvoiceSigma.setInvoiceNumber(null);

        // Create the NextInvoiceSigma, which fails.
        NextInvoiceSigmaDTO nextInvoiceSigmaDTO = nextInvoiceSigmaMapper.toDto(nextInvoiceSigma);

        restNextInvoiceSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceSigmaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIssueDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextInvoiceSigma.setIssueDate(null);

        // Create the NextInvoiceSigma, which fails.
        NextInvoiceSigmaDTO nextInvoiceSigmaDTO = nextInvoiceSigmaMapper.toDto(nextInvoiceSigma);

        restNextInvoiceSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceSigmaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextInvoiceSigma.setAmount(null);

        // Create the NextInvoiceSigma, which fails.
        NextInvoiceSigmaDTO nextInvoiceSigmaDTO = nextInvoiceSigmaMapper.toDto(nextInvoiceSigma);

        restNextInvoiceSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceSigmaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextInvoiceSigmas() throws Exception {
        // Initialize the database
        insertedNextInvoiceSigma = nextInvoiceSigmaRepository.saveAndFlush(nextInvoiceSigma);

        // Get all the nextInvoiceSigmaList
        restNextInvoiceSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextInvoiceSigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));
    }

    @Test
    @Transactional
    void getNextInvoiceSigma() throws Exception {
        // Initialize the database
        insertedNextInvoiceSigma = nextInvoiceSigmaRepository.saveAndFlush(nextInvoiceSigma);

        // Get the nextInvoiceSigma
        restNextInvoiceSigmaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextInvoiceSigma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextInvoiceSigma.getId().intValue()))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER))
            .andExpect(jsonPath("$.issueDate").value(DEFAULT_ISSUE_DATE.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)));
    }

    @Test
    @Transactional
    void getNextInvoiceSigmasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextInvoiceSigma = nextInvoiceSigmaRepository.saveAndFlush(nextInvoiceSigma);

        Long id = nextInvoiceSigma.getId();

        defaultNextInvoiceSigmaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextInvoiceSigmaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextInvoiceSigmaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextInvoiceSigmasByInvoiceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceSigma = nextInvoiceSigmaRepository.saveAndFlush(nextInvoiceSigma);

        // Get all the nextInvoiceSigmaList where invoiceNumber equals to
        defaultNextInvoiceSigmaFiltering(
            "invoiceNumber.equals=" + DEFAULT_INVOICE_NUMBER,
            "invoiceNumber.equals=" + UPDATED_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceSigmasByInvoiceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoiceSigma = nextInvoiceSigmaRepository.saveAndFlush(nextInvoiceSigma);

        // Get all the nextInvoiceSigmaList where invoiceNumber in
        defaultNextInvoiceSigmaFiltering(
            "invoiceNumber.in=" + DEFAULT_INVOICE_NUMBER + "," + UPDATED_INVOICE_NUMBER,
            "invoiceNumber.in=" + UPDATED_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceSigmasByInvoiceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoiceSigma = nextInvoiceSigmaRepository.saveAndFlush(nextInvoiceSigma);

        // Get all the nextInvoiceSigmaList where invoiceNumber is not null
        defaultNextInvoiceSigmaFiltering("invoiceNumber.specified=true", "invoiceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoiceSigmasByInvoiceNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceSigma = nextInvoiceSigmaRepository.saveAndFlush(nextInvoiceSigma);

        // Get all the nextInvoiceSigmaList where invoiceNumber contains
        defaultNextInvoiceSigmaFiltering(
            "invoiceNumber.contains=" + DEFAULT_INVOICE_NUMBER,
            "invoiceNumber.contains=" + UPDATED_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceSigmasByInvoiceNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceSigma = nextInvoiceSigmaRepository.saveAndFlush(nextInvoiceSigma);

        // Get all the nextInvoiceSigmaList where invoiceNumber does not contain
        defaultNextInvoiceSigmaFiltering(
            "invoiceNumber.doesNotContain=" + UPDATED_INVOICE_NUMBER,
            "invoiceNumber.doesNotContain=" + DEFAULT_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceSigmasByIssueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceSigma = nextInvoiceSigmaRepository.saveAndFlush(nextInvoiceSigma);

        // Get all the nextInvoiceSigmaList where issueDate equals to
        defaultNextInvoiceSigmaFiltering("issueDate.equals=" + DEFAULT_ISSUE_DATE, "issueDate.equals=" + UPDATED_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllNextInvoiceSigmasByIssueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoiceSigma = nextInvoiceSigmaRepository.saveAndFlush(nextInvoiceSigma);

        // Get all the nextInvoiceSigmaList where issueDate in
        defaultNextInvoiceSigmaFiltering(
            "issueDate.in=" + DEFAULT_ISSUE_DATE + "," + UPDATED_ISSUE_DATE,
            "issueDate.in=" + UPDATED_ISSUE_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceSigmasByIssueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoiceSigma = nextInvoiceSigmaRepository.saveAndFlush(nextInvoiceSigma);

        // Get all the nextInvoiceSigmaList where issueDate is not null
        defaultNextInvoiceSigmaFiltering("issueDate.specified=true", "issueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoiceSigmasByDueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceSigma = nextInvoiceSigmaRepository.saveAndFlush(nextInvoiceSigma);

        // Get all the nextInvoiceSigmaList where dueDate equals to
        defaultNextInvoiceSigmaFiltering("dueDate.equals=" + DEFAULT_DUE_DATE, "dueDate.equals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllNextInvoiceSigmasByDueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoiceSigma = nextInvoiceSigmaRepository.saveAndFlush(nextInvoiceSigma);

        // Get all the nextInvoiceSigmaList where dueDate in
        defaultNextInvoiceSigmaFiltering("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE, "dueDate.in=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllNextInvoiceSigmasByDueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoiceSigma = nextInvoiceSigmaRepository.saveAndFlush(nextInvoiceSigma);

        // Get all the nextInvoiceSigmaList where dueDate is not null
        defaultNextInvoiceSigmaFiltering("dueDate.specified=true", "dueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoiceSigmasByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceSigma = nextInvoiceSigmaRepository.saveAndFlush(nextInvoiceSigma);

        // Get all the nextInvoiceSigmaList where amount equals to
        defaultNextInvoiceSigmaFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceSigmasByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoiceSigma = nextInvoiceSigmaRepository.saveAndFlush(nextInvoiceSigma);

        // Get all the nextInvoiceSigmaList where amount in
        defaultNextInvoiceSigmaFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceSigmasByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoiceSigma = nextInvoiceSigmaRepository.saveAndFlush(nextInvoiceSigma);

        // Get all the nextInvoiceSigmaList where amount is not null
        defaultNextInvoiceSigmaFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoiceSigmasByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceSigma = nextInvoiceSigmaRepository.saveAndFlush(nextInvoiceSigma);

        // Get all the nextInvoiceSigmaList where amount is greater than or equal to
        defaultNextInvoiceSigmaFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceSigmasByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceSigma = nextInvoiceSigmaRepository.saveAndFlush(nextInvoiceSigma);

        // Get all the nextInvoiceSigmaList where amount is less than or equal to
        defaultNextInvoiceSigmaFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceSigmasByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceSigma = nextInvoiceSigmaRepository.saveAndFlush(nextInvoiceSigma);

        // Get all the nextInvoiceSigmaList where amount is less than
        defaultNextInvoiceSigmaFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceSigmasByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceSigma = nextInvoiceSigmaRepository.saveAndFlush(nextInvoiceSigma);

        // Get all the nextInvoiceSigmaList where amount is greater than
        defaultNextInvoiceSigmaFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceSigmasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextInvoiceSigmaRepository.saveAndFlush(nextInvoiceSigma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextInvoiceSigma.setTenant(tenant);
        nextInvoiceSigmaRepository.saveAndFlush(nextInvoiceSigma);
        Long tenantId = tenant.getId();
        // Get all the nextInvoiceSigmaList where tenant equals to tenantId
        defaultNextInvoiceSigmaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextInvoiceSigmaList where tenant equals to (tenantId + 1)
        defaultNextInvoiceSigmaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextInvoiceSigmaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextInvoiceSigmaShouldBeFound(shouldBeFound);
        defaultNextInvoiceSigmaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextInvoiceSigmaShouldBeFound(String filter) throws Exception {
        restNextInvoiceSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextInvoiceSigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));

        // Check, that the count call also returns 1
        restNextInvoiceSigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextInvoiceSigmaShouldNotBeFound(String filter) throws Exception {
        restNextInvoiceSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextInvoiceSigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextInvoiceSigma() throws Exception {
        // Get the nextInvoiceSigma
        restNextInvoiceSigmaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextInvoiceSigma() throws Exception {
        // Initialize the database
        insertedNextInvoiceSigma = nextInvoiceSigmaRepository.saveAndFlush(nextInvoiceSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextInvoiceSigma
        NextInvoiceSigma updatedNextInvoiceSigma = nextInvoiceSigmaRepository.findById(nextInvoiceSigma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextInvoiceSigma are not directly saved in db
        em.detach(updatedNextInvoiceSigma);
        updatedNextInvoiceSigma
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);
        NextInvoiceSigmaDTO nextInvoiceSigmaDTO = nextInvoiceSigmaMapper.toDto(updatedNextInvoiceSigma);

        restNextInvoiceSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextInvoiceSigmaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextInvoiceSigmaDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextInvoiceSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextInvoiceSigmaToMatchAllProperties(updatedNextInvoiceSigma);
    }

    @Test
    @Transactional
    void putNonExistingNextInvoiceSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceSigma.setId(longCount.incrementAndGet());

        // Create the NextInvoiceSigma
        NextInvoiceSigmaDTO nextInvoiceSigmaDTO = nextInvoiceSigmaMapper.toDto(nextInvoiceSigma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextInvoiceSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextInvoiceSigmaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextInvoiceSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextInvoiceSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceSigma.setId(longCount.incrementAndGet());

        // Create the NextInvoiceSigma
        NextInvoiceSigmaDTO nextInvoiceSigmaDTO = nextInvoiceSigmaMapper.toDto(nextInvoiceSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextInvoiceSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextInvoiceSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceSigma.setId(longCount.incrementAndGet());

        // Create the NextInvoiceSigma
        NextInvoiceSigmaDTO nextInvoiceSigmaDTO = nextInvoiceSigmaMapper.toDto(nextInvoiceSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceSigmaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceSigmaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextInvoiceSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextInvoiceSigmaWithPatch() throws Exception {
        // Initialize the database
        insertedNextInvoiceSigma = nextInvoiceSigmaRepository.saveAndFlush(nextInvoiceSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextInvoiceSigma using partial update
        NextInvoiceSigma partialUpdatedNextInvoiceSigma = new NextInvoiceSigma();
        partialUpdatedNextInvoiceSigma.setId(nextInvoiceSigma.getId());

        restNextInvoiceSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextInvoiceSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextInvoiceSigma))
            )
            .andExpect(status().isOk());

        // Validate the NextInvoiceSigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextInvoiceSigmaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextInvoiceSigma, nextInvoiceSigma),
            getPersistedNextInvoiceSigma(nextInvoiceSigma)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextInvoiceSigmaWithPatch() throws Exception {
        // Initialize the database
        insertedNextInvoiceSigma = nextInvoiceSigmaRepository.saveAndFlush(nextInvoiceSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextInvoiceSigma using partial update
        NextInvoiceSigma partialUpdatedNextInvoiceSigma = new NextInvoiceSigma();
        partialUpdatedNextInvoiceSigma.setId(nextInvoiceSigma.getId());

        partialUpdatedNextInvoiceSigma
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);

        restNextInvoiceSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextInvoiceSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextInvoiceSigma))
            )
            .andExpect(status().isOk());

        // Validate the NextInvoiceSigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextInvoiceSigmaUpdatableFieldsEquals(
            partialUpdatedNextInvoiceSigma,
            getPersistedNextInvoiceSigma(partialUpdatedNextInvoiceSigma)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextInvoiceSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceSigma.setId(longCount.incrementAndGet());

        // Create the NextInvoiceSigma
        NextInvoiceSigmaDTO nextInvoiceSigmaDTO = nextInvoiceSigmaMapper.toDto(nextInvoiceSigma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextInvoiceSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextInvoiceSigmaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextInvoiceSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextInvoiceSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceSigma.setId(longCount.incrementAndGet());

        // Create the NextInvoiceSigma
        NextInvoiceSigmaDTO nextInvoiceSigmaDTO = nextInvoiceSigmaMapper.toDto(nextInvoiceSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextInvoiceSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextInvoiceSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceSigma.setId(longCount.incrementAndGet());

        // Create the NextInvoiceSigma
        NextInvoiceSigmaDTO nextInvoiceSigmaDTO = nextInvoiceSigmaMapper.toDto(nextInvoiceSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceSigmaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextInvoiceSigmaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextInvoiceSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextInvoiceSigma() throws Exception {
        // Initialize the database
        insertedNextInvoiceSigma = nextInvoiceSigmaRepository.saveAndFlush(nextInvoiceSigma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextInvoiceSigma
        restNextInvoiceSigmaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextInvoiceSigma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextInvoiceSigmaRepository.count();
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

    protected NextInvoiceSigma getPersistedNextInvoiceSigma(NextInvoiceSigma nextInvoiceSigma) {
        return nextInvoiceSigmaRepository.findById(nextInvoiceSigma.getId()).orElseThrow();
    }

    protected void assertPersistedNextInvoiceSigmaToMatchAllProperties(NextInvoiceSigma expectedNextInvoiceSigma) {
        assertNextInvoiceSigmaAllPropertiesEquals(expectedNextInvoiceSigma, getPersistedNextInvoiceSigma(expectedNextInvoiceSigma));
    }

    protected void assertPersistedNextInvoiceSigmaToMatchUpdatableProperties(NextInvoiceSigma expectedNextInvoiceSigma) {
        assertNextInvoiceSigmaAllUpdatablePropertiesEquals(
            expectedNextInvoiceSigma,
            getPersistedNextInvoiceSigma(expectedNextInvoiceSigma)
        );
    }
}
