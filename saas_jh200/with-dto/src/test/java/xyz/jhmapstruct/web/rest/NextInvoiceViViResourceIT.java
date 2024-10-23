package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextInvoiceViViAsserts.*;
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
import xyz.jhmapstruct.domain.NextInvoiceViVi;
import xyz.jhmapstruct.repository.NextInvoiceViViRepository;
import xyz.jhmapstruct.service.dto.NextInvoiceViViDTO;
import xyz.jhmapstruct.service.mapper.NextInvoiceViViMapper;

/**
 * Integration tests for the {@link NextInvoiceViViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextInvoiceViViResourceIT {

    private static final String DEFAULT_INVOICE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_ISSUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ISSUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/next-invoice-vi-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextInvoiceViViRepository nextInvoiceViViRepository;

    @Autowired
    private NextInvoiceViViMapper nextInvoiceViViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextInvoiceViViMockMvc;

    private NextInvoiceViVi nextInvoiceViVi;

    private NextInvoiceViVi insertedNextInvoiceViVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextInvoiceViVi createEntity() {
        return new NextInvoiceViVi()
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
    public static NextInvoiceViVi createUpdatedEntity() {
        return new NextInvoiceViVi()
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);
    }

    @BeforeEach
    public void initTest() {
        nextInvoiceViVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextInvoiceViVi != null) {
            nextInvoiceViViRepository.delete(insertedNextInvoiceViVi);
            insertedNextInvoiceViVi = null;
        }
    }

    @Test
    @Transactional
    void createNextInvoiceViVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextInvoiceViVi
        NextInvoiceViViDTO nextInvoiceViViDTO = nextInvoiceViViMapper.toDto(nextInvoiceViVi);
        var returnedNextInvoiceViViDTO = om.readValue(
            restNextInvoiceViViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceViViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextInvoiceViViDTO.class
        );

        // Validate the NextInvoiceViVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextInvoiceViVi = nextInvoiceViViMapper.toEntity(returnedNextInvoiceViViDTO);
        assertNextInvoiceViViUpdatableFieldsEquals(returnedNextInvoiceViVi, getPersistedNextInvoiceViVi(returnedNextInvoiceViVi));

        insertedNextInvoiceViVi = returnedNextInvoiceViVi;
    }

    @Test
    @Transactional
    void createNextInvoiceViViWithExistingId() throws Exception {
        // Create the NextInvoiceViVi with an existing ID
        nextInvoiceViVi.setId(1L);
        NextInvoiceViViDTO nextInvoiceViViDTO = nextInvoiceViViMapper.toDto(nextInvoiceViVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextInvoiceViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceViViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkInvoiceNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextInvoiceViVi.setInvoiceNumber(null);

        // Create the NextInvoiceViVi, which fails.
        NextInvoiceViViDTO nextInvoiceViViDTO = nextInvoiceViViMapper.toDto(nextInvoiceViVi);

        restNextInvoiceViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceViViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIssueDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextInvoiceViVi.setIssueDate(null);

        // Create the NextInvoiceViVi, which fails.
        NextInvoiceViViDTO nextInvoiceViViDTO = nextInvoiceViViMapper.toDto(nextInvoiceViVi);

        restNextInvoiceViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceViViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextInvoiceViVi.setAmount(null);

        // Create the NextInvoiceViVi, which fails.
        NextInvoiceViViDTO nextInvoiceViViDTO = nextInvoiceViViMapper.toDto(nextInvoiceViVi);

        restNextInvoiceViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceViViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextInvoiceViVis() throws Exception {
        // Initialize the database
        insertedNextInvoiceViVi = nextInvoiceViViRepository.saveAndFlush(nextInvoiceViVi);

        // Get all the nextInvoiceViViList
        restNextInvoiceViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextInvoiceViVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));
    }

    @Test
    @Transactional
    void getNextInvoiceViVi() throws Exception {
        // Initialize the database
        insertedNextInvoiceViVi = nextInvoiceViViRepository.saveAndFlush(nextInvoiceViVi);

        // Get the nextInvoiceViVi
        restNextInvoiceViViMockMvc
            .perform(get(ENTITY_API_URL_ID, nextInvoiceViVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextInvoiceViVi.getId().intValue()))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER))
            .andExpect(jsonPath("$.issueDate").value(DEFAULT_ISSUE_DATE.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)));
    }

    @Test
    @Transactional
    void getNextInvoiceViVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextInvoiceViVi = nextInvoiceViViRepository.saveAndFlush(nextInvoiceViVi);

        Long id = nextInvoiceViVi.getId();

        defaultNextInvoiceViViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextInvoiceViViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextInvoiceViViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextInvoiceViVisByInvoiceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceViVi = nextInvoiceViViRepository.saveAndFlush(nextInvoiceViVi);

        // Get all the nextInvoiceViViList where invoiceNumber equals to
        defaultNextInvoiceViViFiltering("invoiceNumber.equals=" + DEFAULT_INVOICE_NUMBER, "invoiceNumber.equals=" + UPDATED_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextInvoiceViVisByInvoiceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoiceViVi = nextInvoiceViViRepository.saveAndFlush(nextInvoiceViVi);

        // Get all the nextInvoiceViViList where invoiceNumber in
        defaultNextInvoiceViViFiltering(
            "invoiceNumber.in=" + DEFAULT_INVOICE_NUMBER + "," + UPDATED_INVOICE_NUMBER,
            "invoiceNumber.in=" + UPDATED_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceViVisByInvoiceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoiceViVi = nextInvoiceViViRepository.saveAndFlush(nextInvoiceViVi);

        // Get all the nextInvoiceViViList where invoiceNumber is not null
        defaultNextInvoiceViViFiltering("invoiceNumber.specified=true", "invoiceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoiceViVisByInvoiceNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceViVi = nextInvoiceViViRepository.saveAndFlush(nextInvoiceViVi);

        // Get all the nextInvoiceViViList where invoiceNumber contains
        defaultNextInvoiceViViFiltering(
            "invoiceNumber.contains=" + DEFAULT_INVOICE_NUMBER,
            "invoiceNumber.contains=" + UPDATED_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceViVisByInvoiceNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceViVi = nextInvoiceViViRepository.saveAndFlush(nextInvoiceViVi);

        // Get all the nextInvoiceViViList where invoiceNumber does not contain
        defaultNextInvoiceViViFiltering(
            "invoiceNumber.doesNotContain=" + UPDATED_INVOICE_NUMBER,
            "invoiceNumber.doesNotContain=" + DEFAULT_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceViVisByIssueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceViVi = nextInvoiceViViRepository.saveAndFlush(nextInvoiceViVi);

        // Get all the nextInvoiceViViList where issueDate equals to
        defaultNextInvoiceViViFiltering("issueDate.equals=" + DEFAULT_ISSUE_DATE, "issueDate.equals=" + UPDATED_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllNextInvoiceViVisByIssueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoiceViVi = nextInvoiceViViRepository.saveAndFlush(nextInvoiceViVi);

        // Get all the nextInvoiceViViList where issueDate in
        defaultNextInvoiceViViFiltering(
            "issueDate.in=" + DEFAULT_ISSUE_DATE + "," + UPDATED_ISSUE_DATE,
            "issueDate.in=" + UPDATED_ISSUE_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceViVisByIssueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoiceViVi = nextInvoiceViViRepository.saveAndFlush(nextInvoiceViVi);

        // Get all the nextInvoiceViViList where issueDate is not null
        defaultNextInvoiceViViFiltering("issueDate.specified=true", "issueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoiceViVisByDueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceViVi = nextInvoiceViViRepository.saveAndFlush(nextInvoiceViVi);

        // Get all the nextInvoiceViViList where dueDate equals to
        defaultNextInvoiceViViFiltering("dueDate.equals=" + DEFAULT_DUE_DATE, "dueDate.equals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllNextInvoiceViVisByDueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoiceViVi = nextInvoiceViViRepository.saveAndFlush(nextInvoiceViVi);

        // Get all the nextInvoiceViViList where dueDate in
        defaultNextInvoiceViViFiltering("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE, "dueDate.in=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllNextInvoiceViVisByDueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoiceViVi = nextInvoiceViViRepository.saveAndFlush(nextInvoiceViVi);

        // Get all the nextInvoiceViViList where dueDate is not null
        defaultNextInvoiceViViFiltering("dueDate.specified=true", "dueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoiceViVisByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceViVi = nextInvoiceViViRepository.saveAndFlush(nextInvoiceViVi);

        // Get all the nextInvoiceViViList where amount equals to
        defaultNextInvoiceViViFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceViVisByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoiceViVi = nextInvoiceViViRepository.saveAndFlush(nextInvoiceViVi);

        // Get all the nextInvoiceViViList where amount in
        defaultNextInvoiceViViFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceViVisByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoiceViVi = nextInvoiceViViRepository.saveAndFlush(nextInvoiceViVi);

        // Get all the nextInvoiceViViList where amount is not null
        defaultNextInvoiceViViFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoiceViVisByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceViVi = nextInvoiceViViRepository.saveAndFlush(nextInvoiceViVi);

        // Get all the nextInvoiceViViList where amount is greater than or equal to
        defaultNextInvoiceViViFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceViVisByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceViVi = nextInvoiceViViRepository.saveAndFlush(nextInvoiceViVi);

        // Get all the nextInvoiceViViList where amount is less than or equal to
        defaultNextInvoiceViViFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceViVisByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceViVi = nextInvoiceViViRepository.saveAndFlush(nextInvoiceViVi);

        // Get all the nextInvoiceViViList where amount is less than
        defaultNextInvoiceViViFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceViVisByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceViVi = nextInvoiceViViRepository.saveAndFlush(nextInvoiceViVi);

        // Get all the nextInvoiceViViList where amount is greater than
        defaultNextInvoiceViViFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceViVisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextInvoiceViViRepository.saveAndFlush(nextInvoiceViVi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextInvoiceViVi.setTenant(tenant);
        nextInvoiceViViRepository.saveAndFlush(nextInvoiceViVi);
        Long tenantId = tenant.getId();
        // Get all the nextInvoiceViViList where tenant equals to tenantId
        defaultNextInvoiceViViShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextInvoiceViViList where tenant equals to (tenantId + 1)
        defaultNextInvoiceViViShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextInvoiceViViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextInvoiceViViShouldBeFound(shouldBeFound);
        defaultNextInvoiceViViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextInvoiceViViShouldBeFound(String filter) throws Exception {
        restNextInvoiceViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextInvoiceViVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));

        // Check, that the count call also returns 1
        restNextInvoiceViViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextInvoiceViViShouldNotBeFound(String filter) throws Exception {
        restNextInvoiceViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextInvoiceViViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextInvoiceViVi() throws Exception {
        // Get the nextInvoiceViVi
        restNextInvoiceViViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextInvoiceViVi() throws Exception {
        // Initialize the database
        insertedNextInvoiceViVi = nextInvoiceViViRepository.saveAndFlush(nextInvoiceViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextInvoiceViVi
        NextInvoiceViVi updatedNextInvoiceViVi = nextInvoiceViViRepository.findById(nextInvoiceViVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextInvoiceViVi are not directly saved in db
        em.detach(updatedNextInvoiceViVi);
        updatedNextInvoiceViVi
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);
        NextInvoiceViViDTO nextInvoiceViViDTO = nextInvoiceViViMapper.toDto(updatedNextInvoiceViVi);

        restNextInvoiceViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextInvoiceViViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextInvoiceViViDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextInvoiceViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextInvoiceViViToMatchAllProperties(updatedNextInvoiceViVi);
    }

    @Test
    @Transactional
    void putNonExistingNextInvoiceViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceViVi.setId(longCount.incrementAndGet());

        // Create the NextInvoiceViVi
        NextInvoiceViViDTO nextInvoiceViViDTO = nextInvoiceViViMapper.toDto(nextInvoiceViVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextInvoiceViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextInvoiceViViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextInvoiceViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextInvoiceViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceViVi.setId(longCount.incrementAndGet());

        // Create the NextInvoiceViVi
        NextInvoiceViViDTO nextInvoiceViViDTO = nextInvoiceViViMapper.toDto(nextInvoiceViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextInvoiceViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextInvoiceViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceViVi.setId(longCount.incrementAndGet());

        // Create the NextInvoiceViVi
        NextInvoiceViViDTO nextInvoiceViViDTO = nextInvoiceViViMapper.toDto(nextInvoiceViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceViViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceViViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextInvoiceViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextInvoiceViViWithPatch() throws Exception {
        // Initialize the database
        insertedNextInvoiceViVi = nextInvoiceViViRepository.saveAndFlush(nextInvoiceViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextInvoiceViVi using partial update
        NextInvoiceViVi partialUpdatedNextInvoiceViVi = new NextInvoiceViVi();
        partialUpdatedNextInvoiceViVi.setId(nextInvoiceViVi.getId());

        partialUpdatedNextInvoiceViVi
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);

        restNextInvoiceViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextInvoiceViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextInvoiceViVi))
            )
            .andExpect(status().isOk());

        // Validate the NextInvoiceViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextInvoiceViViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextInvoiceViVi, nextInvoiceViVi),
            getPersistedNextInvoiceViVi(nextInvoiceViVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextInvoiceViViWithPatch() throws Exception {
        // Initialize the database
        insertedNextInvoiceViVi = nextInvoiceViViRepository.saveAndFlush(nextInvoiceViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextInvoiceViVi using partial update
        NextInvoiceViVi partialUpdatedNextInvoiceViVi = new NextInvoiceViVi();
        partialUpdatedNextInvoiceViVi.setId(nextInvoiceViVi.getId());

        partialUpdatedNextInvoiceViVi
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);

        restNextInvoiceViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextInvoiceViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextInvoiceViVi))
            )
            .andExpect(status().isOk());

        // Validate the NextInvoiceViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextInvoiceViViUpdatableFieldsEquals(
            partialUpdatedNextInvoiceViVi,
            getPersistedNextInvoiceViVi(partialUpdatedNextInvoiceViVi)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextInvoiceViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceViVi.setId(longCount.incrementAndGet());

        // Create the NextInvoiceViVi
        NextInvoiceViViDTO nextInvoiceViViDTO = nextInvoiceViViMapper.toDto(nextInvoiceViVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextInvoiceViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextInvoiceViViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextInvoiceViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextInvoiceViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceViVi.setId(longCount.incrementAndGet());

        // Create the NextInvoiceViVi
        NextInvoiceViViDTO nextInvoiceViViDTO = nextInvoiceViViMapper.toDto(nextInvoiceViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextInvoiceViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextInvoiceViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceViVi.setId(longCount.incrementAndGet());

        // Create the NextInvoiceViVi
        NextInvoiceViViDTO nextInvoiceViViDTO = nextInvoiceViViMapper.toDto(nextInvoiceViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceViViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextInvoiceViViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextInvoiceViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextInvoiceViVi() throws Exception {
        // Initialize the database
        insertedNextInvoiceViVi = nextInvoiceViViRepository.saveAndFlush(nextInvoiceViVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextInvoiceViVi
        restNextInvoiceViViMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextInvoiceViVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextInvoiceViViRepository.count();
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

    protected NextInvoiceViVi getPersistedNextInvoiceViVi(NextInvoiceViVi nextInvoiceViVi) {
        return nextInvoiceViViRepository.findById(nextInvoiceViVi.getId()).orElseThrow();
    }

    protected void assertPersistedNextInvoiceViViToMatchAllProperties(NextInvoiceViVi expectedNextInvoiceViVi) {
        assertNextInvoiceViViAllPropertiesEquals(expectedNextInvoiceViVi, getPersistedNextInvoiceViVi(expectedNextInvoiceViVi));
    }

    protected void assertPersistedNextInvoiceViViToMatchUpdatableProperties(NextInvoiceViVi expectedNextInvoiceViVi) {
        assertNextInvoiceViViAllUpdatablePropertiesEquals(expectedNextInvoiceViVi, getPersistedNextInvoiceViVi(expectedNextInvoiceViVi));
    }
}
