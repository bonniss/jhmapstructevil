package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextInvoiceMiMiAsserts.*;
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
import xyz.jhmapstruct.domain.NextInvoiceMiMi;
import xyz.jhmapstruct.repository.NextInvoiceMiMiRepository;
import xyz.jhmapstruct.service.dto.NextInvoiceMiMiDTO;
import xyz.jhmapstruct.service.mapper.NextInvoiceMiMiMapper;

/**
 * Integration tests for the {@link NextInvoiceMiMiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextInvoiceMiMiResourceIT {

    private static final String DEFAULT_INVOICE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_ISSUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ISSUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/next-invoice-mi-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextInvoiceMiMiRepository nextInvoiceMiMiRepository;

    @Autowired
    private NextInvoiceMiMiMapper nextInvoiceMiMiMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextInvoiceMiMiMockMvc;

    private NextInvoiceMiMi nextInvoiceMiMi;

    private NextInvoiceMiMi insertedNextInvoiceMiMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextInvoiceMiMi createEntity() {
        return new NextInvoiceMiMi()
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
    public static NextInvoiceMiMi createUpdatedEntity() {
        return new NextInvoiceMiMi()
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);
    }

    @BeforeEach
    public void initTest() {
        nextInvoiceMiMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextInvoiceMiMi != null) {
            nextInvoiceMiMiRepository.delete(insertedNextInvoiceMiMi);
            insertedNextInvoiceMiMi = null;
        }
    }

    @Test
    @Transactional
    void createNextInvoiceMiMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextInvoiceMiMi
        NextInvoiceMiMiDTO nextInvoiceMiMiDTO = nextInvoiceMiMiMapper.toDto(nextInvoiceMiMi);
        var returnedNextInvoiceMiMiDTO = om.readValue(
            restNextInvoiceMiMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceMiMiDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextInvoiceMiMiDTO.class
        );

        // Validate the NextInvoiceMiMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextInvoiceMiMi = nextInvoiceMiMiMapper.toEntity(returnedNextInvoiceMiMiDTO);
        assertNextInvoiceMiMiUpdatableFieldsEquals(returnedNextInvoiceMiMi, getPersistedNextInvoiceMiMi(returnedNextInvoiceMiMi));

        insertedNextInvoiceMiMi = returnedNextInvoiceMiMi;
    }

    @Test
    @Transactional
    void createNextInvoiceMiMiWithExistingId() throws Exception {
        // Create the NextInvoiceMiMi with an existing ID
        nextInvoiceMiMi.setId(1L);
        NextInvoiceMiMiDTO nextInvoiceMiMiDTO = nextInvoiceMiMiMapper.toDto(nextInvoiceMiMi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextInvoiceMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceMiMiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkInvoiceNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextInvoiceMiMi.setInvoiceNumber(null);

        // Create the NextInvoiceMiMi, which fails.
        NextInvoiceMiMiDTO nextInvoiceMiMiDTO = nextInvoiceMiMiMapper.toDto(nextInvoiceMiMi);

        restNextInvoiceMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceMiMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIssueDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextInvoiceMiMi.setIssueDate(null);

        // Create the NextInvoiceMiMi, which fails.
        NextInvoiceMiMiDTO nextInvoiceMiMiDTO = nextInvoiceMiMiMapper.toDto(nextInvoiceMiMi);

        restNextInvoiceMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceMiMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextInvoiceMiMi.setAmount(null);

        // Create the NextInvoiceMiMi, which fails.
        NextInvoiceMiMiDTO nextInvoiceMiMiDTO = nextInvoiceMiMiMapper.toDto(nextInvoiceMiMi);

        restNextInvoiceMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceMiMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextInvoiceMiMis() throws Exception {
        // Initialize the database
        insertedNextInvoiceMiMi = nextInvoiceMiMiRepository.saveAndFlush(nextInvoiceMiMi);

        // Get all the nextInvoiceMiMiList
        restNextInvoiceMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextInvoiceMiMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));
    }

    @Test
    @Transactional
    void getNextInvoiceMiMi() throws Exception {
        // Initialize the database
        insertedNextInvoiceMiMi = nextInvoiceMiMiRepository.saveAndFlush(nextInvoiceMiMi);

        // Get the nextInvoiceMiMi
        restNextInvoiceMiMiMockMvc
            .perform(get(ENTITY_API_URL_ID, nextInvoiceMiMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextInvoiceMiMi.getId().intValue()))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER))
            .andExpect(jsonPath("$.issueDate").value(DEFAULT_ISSUE_DATE.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)));
    }

    @Test
    @Transactional
    void getNextInvoiceMiMisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextInvoiceMiMi = nextInvoiceMiMiRepository.saveAndFlush(nextInvoiceMiMi);

        Long id = nextInvoiceMiMi.getId();

        defaultNextInvoiceMiMiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextInvoiceMiMiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextInvoiceMiMiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextInvoiceMiMisByInvoiceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceMiMi = nextInvoiceMiMiRepository.saveAndFlush(nextInvoiceMiMi);

        // Get all the nextInvoiceMiMiList where invoiceNumber equals to
        defaultNextInvoiceMiMiFiltering("invoiceNumber.equals=" + DEFAULT_INVOICE_NUMBER, "invoiceNumber.equals=" + UPDATED_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextInvoiceMiMisByInvoiceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoiceMiMi = nextInvoiceMiMiRepository.saveAndFlush(nextInvoiceMiMi);

        // Get all the nextInvoiceMiMiList where invoiceNumber in
        defaultNextInvoiceMiMiFiltering(
            "invoiceNumber.in=" + DEFAULT_INVOICE_NUMBER + "," + UPDATED_INVOICE_NUMBER,
            "invoiceNumber.in=" + UPDATED_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceMiMisByInvoiceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoiceMiMi = nextInvoiceMiMiRepository.saveAndFlush(nextInvoiceMiMi);

        // Get all the nextInvoiceMiMiList where invoiceNumber is not null
        defaultNextInvoiceMiMiFiltering("invoiceNumber.specified=true", "invoiceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoiceMiMisByInvoiceNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceMiMi = nextInvoiceMiMiRepository.saveAndFlush(nextInvoiceMiMi);

        // Get all the nextInvoiceMiMiList where invoiceNumber contains
        defaultNextInvoiceMiMiFiltering(
            "invoiceNumber.contains=" + DEFAULT_INVOICE_NUMBER,
            "invoiceNumber.contains=" + UPDATED_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceMiMisByInvoiceNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceMiMi = nextInvoiceMiMiRepository.saveAndFlush(nextInvoiceMiMi);

        // Get all the nextInvoiceMiMiList where invoiceNumber does not contain
        defaultNextInvoiceMiMiFiltering(
            "invoiceNumber.doesNotContain=" + UPDATED_INVOICE_NUMBER,
            "invoiceNumber.doesNotContain=" + DEFAULT_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceMiMisByIssueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceMiMi = nextInvoiceMiMiRepository.saveAndFlush(nextInvoiceMiMi);

        // Get all the nextInvoiceMiMiList where issueDate equals to
        defaultNextInvoiceMiMiFiltering("issueDate.equals=" + DEFAULT_ISSUE_DATE, "issueDate.equals=" + UPDATED_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllNextInvoiceMiMisByIssueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoiceMiMi = nextInvoiceMiMiRepository.saveAndFlush(nextInvoiceMiMi);

        // Get all the nextInvoiceMiMiList where issueDate in
        defaultNextInvoiceMiMiFiltering(
            "issueDate.in=" + DEFAULT_ISSUE_DATE + "," + UPDATED_ISSUE_DATE,
            "issueDate.in=" + UPDATED_ISSUE_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceMiMisByIssueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoiceMiMi = nextInvoiceMiMiRepository.saveAndFlush(nextInvoiceMiMi);

        // Get all the nextInvoiceMiMiList where issueDate is not null
        defaultNextInvoiceMiMiFiltering("issueDate.specified=true", "issueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoiceMiMisByDueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceMiMi = nextInvoiceMiMiRepository.saveAndFlush(nextInvoiceMiMi);

        // Get all the nextInvoiceMiMiList where dueDate equals to
        defaultNextInvoiceMiMiFiltering("dueDate.equals=" + DEFAULT_DUE_DATE, "dueDate.equals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllNextInvoiceMiMisByDueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoiceMiMi = nextInvoiceMiMiRepository.saveAndFlush(nextInvoiceMiMi);

        // Get all the nextInvoiceMiMiList where dueDate in
        defaultNextInvoiceMiMiFiltering("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE, "dueDate.in=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllNextInvoiceMiMisByDueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoiceMiMi = nextInvoiceMiMiRepository.saveAndFlush(nextInvoiceMiMi);

        // Get all the nextInvoiceMiMiList where dueDate is not null
        defaultNextInvoiceMiMiFiltering("dueDate.specified=true", "dueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoiceMiMisByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceMiMi = nextInvoiceMiMiRepository.saveAndFlush(nextInvoiceMiMi);

        // Get all the nextInvoiceMiMiList where amount equals to
        defaultNextInvoiceMiMiFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceMiMisByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoiceMiMi = nextInvoiceMiMiRepository.saveAndFlush(nextInvoiceMiMi);

        // Get all the nextInvoiceMiMiList where amount in
        defaultNextInvoiceMiMiFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceMiMisByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoiceMiMi = nextInvoiceMiMiRepository.saveAndFlush(nextInvoiceMiMi);

        // Get all the nextInvoiceMiMiList where amount is not null
        defaultNextInvoiceMiMiFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoiceMiMisByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceMiMi = nextInvoiceMiMiRepository.saveAndFlush(nextInvoiceMiMi);

        // Get all the nextInvoiceMiMiList where amount is greater than or equal to
        defaultNextInvoiceMiMiFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceMiMisByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceMiMi = nextInvoiceMiMiRepository.saveAndFlush(nextInvoiceMiMi);

        // Get all the nextInvoiceMiMiList where amount is less than or equal to
        defaultNextInvoiceMiMiFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceMiMisByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceMiMi = nextInvoiceMiMiRepository.saveAndFlush(nextInvoiceMiMi);

        // Get all the nextInvoiceMiMiList where amount is less than
        defaultNextInvoiceMiMiFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceMiMisByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceMiMi = nextInvoiceMiMiRepository.saveAndFlush(nextInvoiceMiMi);

        // Get all the nextInvoiceMiMiList where amount is greater than
        defaultNextInvoiceMiMiFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceMiMisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextInvoiceMiMiRepository.saveAndFlush(nextInvoiceMiMi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextInvoiceMiMi.setTenant(tenant);
        nextInvoiceMiMiRepository.saveAndFlush(nextInvoiceMiMi);
        Long tenantId = tenant.getId();
        // Get all the nextInvoiceMiMiList where tenant equals to tenantId
        defaultNextInvoiceMiMiShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextInvoiceMiMiList where tenant equals to (tenantId + 1)
        defaultNextInvoiceMiMiShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextInvoiceMiMiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextInvoiceMiMiShouldBeFound(shouldBeFound);
        defaultNextInvoiceMiMiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextInvoiceMiMiShouldBeFound(String filter) throws Exception {
        restNextInvoiceMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextInvoiceMiMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));

        // Check, that the count call also returns 1
        restNextInvoiceMiMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextInvoiceMiMiShouldNotBeFound(String filter) throws Exception {
        restNextInvoiceMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextInvoiceMiMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextInvoiceMiMi() throws Exception {
        // Get the nextInvoiceMiMi
        restNextInvoiceMiMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextInvoiceMiMi() throws Exception {
        // Initialize the database
        insertedNextInvoiceMiMi = nextInvoiceMiMiRepository.saveAndFlush(nextInvoiceMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextInvoiceMiMi
        NextInvoiceMiMi updatedNextInvoiceMiMi = nextInvoiceMiMiRepository.findById(nextInvoiceMiMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextInvoiceMiMi are not directly saved in db
        em.detach(updatedNextInvoiceMiMi);
        updatedNextInvoiceMiMi
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);
        NextInvoiceMiMiDTO nextInvoiceMiMiDTO = nextInvoiceMiMiMapper.toDto(updatedNextInvoiceMiMi);

        restNextInvoiceMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextInvoiceMiMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextInvoiceMiMiDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextInvoiceMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextInvoiceMiMiToMatchAllProperties(updatedNextInvoiceMiMi);
    }

    @Test
    @Transactional
    void putNonExistingNextInvoiceMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceMiMi.setId(longCount.incrementAndGet());

        // Create the NextInvoiceMiMi
        NextInvoiceMiMiDTO nextInvoiceMiMiDTO = nextInvoiceMiMiMapper.toDto(nextInvoiceMiMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextInvoiceMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextInvoiceMiMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextInvoiceMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextInvoiceMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceMiMi.setId(longCount.incrementAndGet());

        // Create the NextInvoiceMiMi
        NextInvoiceMiMiDTO nextInvoiceMiMiDTO = nextInvoiceMiMiMapper.toDto(nextInvoiceMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextInvoiceMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextInvoiceMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceMiMi.setId(longCount.incrementAndGet());

        // Create the NextInvoiceMiMi
        NextInvoiceMiMiDTO nextInvoiceMiMiDTO = nextInvoiceMiMiMapper.toDto(nextInvoiceMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceMiMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceMiMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextInvoiceMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextInvoiceMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextInvoiceMiMi = nextInvoiceMiMiRepository.saveAndFlush(nextInvoiceMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextInvoiceMiMi using partial update
        NextInvoiceMiMi partialUpdatedNextInvoiceMiMi = new NextInvoiceMiMi();
        partialUpdatedNextInvoiceMiMi.setId(nextInvoiceMiMi.getId());

        partialUpdatedNextInvoiceMiMi.invoiceNumber(UPDATED_INVOICE_NUMBER).amount(UPDATED_AMOUNT);

        restNextInvoiceMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextInvoiceMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextInvoiceMiMi))
            )
            .andExpect(status().isOk());

        // Validate the NextInvoiceMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextInvoiceMiMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextInvoiceMiMi, nextInvoiceMiMi),
            getPersistedNextInvoiceMiMi(nextInvoiceMiMi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextInvoiceMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextInvoiceMiMi = nextInvoiceMiMiRepository.saveAndFlush(nextInvoiceMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextInvoiceMiMi using partial update
        NextInvoiceMiMi partialUpdatedNextInvoiceMiMi = new NextInvoiceMiMi();
        partialUpdatedNextInvoiceMiMi.setId(nextInvoiceMiMi.getId());

        partialUpdatedNextInvoiceMiMi
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);

        restNextInvoiceMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextInvoiceMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextInvoiceMiMi))
            )
            .andExpect(status().isOk());

        // Validate the NextInvoiceMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextInvoiceMiMiUpdatableFieldsEquals(
            partialUpdatedNextInvoiceMiMi,
            getPersistedNextInvoiceMiMi(partialUpdatedNextInvoiceMiMi)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextInvoiceMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceMiMi.setId(longCount.incrementAndGet());

        // Create the NextInvoiceMiMi
        NextInvoiceMiMiDTO nextInvoiceMiMiDTO = nextInvoiceMiMiMapper.toDto(nextInvoiceMiMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextInvoiceMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextInvoiceMiMiDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextInvoiceMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextInvoiceMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceMiMi.setId(longCount.incrementAndGet());

        // Create the NextInvoiceMiMi
        NextInvoiceMiMiDTO nextInvoiceMiMiDTO = nextInvoiceMiMiMapper.toDto(nextInvoiceMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextInvoiceMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextInvoiceMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceMiMi.setId(longCount.incrementAndGet());

        // Create the NextInvoiceMiMi
        NextInvoiceMiMiDTO nextInvoiceMiMiDTO = nextInvoiceMiMiMapper.toDto(nextInvoiceMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceMiMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextInvoiceMiMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextInvoiceMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextInvoiceMiMi() throws Exception {
        // Initialize the database
        insertedNextInvoiceMiMi = nextInvoiceMiMiRepository.saveAndFlush(nextInvoiceMiMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextInvoiceMiMi
        restNextInvoiceMiMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextInvoiceMiMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextInvoiceMiMiRepository.count();
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

    protected NextInvoiceMiMi getPersistedNextInvoiceMiMi(NextInvoiceMiMi nextInvoiceMiMi) {
        return nextInvoiceMiMiRepository.findById(nextInvoiceMiMi.getId()).orElseThrow();
    }

    protected void assertPersistedNextInvoiceMiMiToMatchAllProperties(NextInvoiceMiMi expectedNextInvoiceMiMi) {
        assertNextInvoiceMiMiAllPropertiesEquals(expectedNextInvoiceMiMi, getPersistedNextInvoiceMiMi(expectedNextInvoiceMiMi));
    }

    protected void assertPersistedNextInvoiceMiMiToMatchUpdatableProperties(NextInvoiceMiMi expectedNextInvoiceMiMi) {
        assertNextInvoiceMiMiAllUpdatablePropertiesEquals(expectedNextInvoiceMiMi, getPersistedNextInvoiceMiMi(expectedNextInvoiceMiMi));
    }
}
