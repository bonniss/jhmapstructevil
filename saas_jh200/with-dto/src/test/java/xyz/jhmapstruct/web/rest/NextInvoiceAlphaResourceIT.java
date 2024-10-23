package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextInvoiceAlphaAsserts.*;
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
import xyz.jhmapstruct.domain.NextInvoiceAlpha;
import xyz.jhmapstruct.repository.NextInvoiceAlphaRepository;
import xyz.jhmapstruct.service.dto.NextInvoiceAlphaDTO;
import xyz.jhmapstruct.service.mapper.NextInvoiceAlphaMapper;

/**
 * Integration tests for the {@link NextInvoiceAlphaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextInvoiceAlphaResourceIT {

    private static final String DEFAULT_INVOICE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_ISSUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ISSUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/next-invoice-alphas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextInvoiceAlphaRepository nextInvoiceAlphaRepository;

    @Autowired
    private NextInvoiceAlphaMapper nextInvoiceAlphaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextInvoiceAlphaMockMvc;

    private NextInvoiceAlpha nextInvoiceAlpha;

    private NextInvoiceAlpha insertedNextInvoiceAlpha;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextInvoiceAlpha createEntity() {
        return new NextInvoiceAlpha()
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
    public static NextInvoiceAlpha createUpdatedEntity() {
        return new NextInvoiceAlpha()
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);
    }

    @BeforeEach
    public void initTest() {
        nextInvoiceAlpha = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextInvoiceAlpha != null) {
            nextInvoiceAlphaRepository.delete(insertedNextInvoiceAlpha);
            insertedNextInvoiceAlpha = null;
        }
    }

    @Test
    @Transactional
    void createNextInvoiceAlpha() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextInvoiceAlpha
        NextInvoiceAlphaDTO nextInvoiceAlphaDTO = nextInvoiceAlphaMapper.toDto(nextInvoiceAlpha);
        var returnedNextInvoiceAlphaDTO = om.readValue(
            restNextInvoiceAlphaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceAlphaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextInvoiceAlphaDTO.class
        );

        // Validate the NextInvoiceAlpha in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextInvoiceAlpha = nextInvoiceAlphaMapper.toEntity(returnedNextInvoiceAlphaDTO);
        assertNextInvoiceAlphaUpdatableFieldsEquals(returnedNextInvoiceAlpha, getPersistedNextInvoiceAlpha(returnedNextInvoiceAlpha));

        insertedNextInvoiceAlpha = returnedNextInvoiceAlpha;
    }

    @Test
    @Transactional
    void createNextInvoiceAlphaWithExistingId() throws Exception {
        // Create the NextInvoiceAlpha with an existing ID
        nextInvoiceAlpha.setId(1L);
        NextInvoiceAlphaDTO nextInvoiceAlphaDTO = nextInvoiceAlphaMapper.toDto(nextInvoiceAlpha);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextInvoiceAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceAlphaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkInvoiceNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextInvoiceAlpha.setInvoiceNumber(null);

        // Create the NextInvoiceAlpha, which fails.
        NextInvoiceAlphaDTO nextInvoiceAlphaDTO = nextInvoiceAlphaMapper.toDto(nextInvoiceAlpha);

        restNextInvoiceAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceAlphaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIssueDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextInvoiceAlpha.setIssueDate(null);

        // Create the NextInvoiceAlpha, which fails.
        NextInvoiceAlphaDTO nextInvoiceAlphaDTO = nextInvoiceAlphaMapper.toDto(nextInvoiceAlpha);

        restNextInvoiceAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceAlphaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextInvoiceAlpha.setAmount(null);

        // Create the NextInvoiceAlpha, which fails.
        NextInvoiceAlphaDTO nextInvoiceAlphaDTO = nextInvoiceAlphaMapper.toDto(nextInvoiceAlpha);

        restNextInvoiceAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceAlphaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextInvoiceAlphas() throws Exception {
        // Initialize the database
        insertedNextInvoiceAlpha = nextInvoiceAlphaRepository.saveAndFlush(nextInvoiceAlpha);

        // Get all the nextInvoiceAlphaList
        restNextInvoiceAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextInvoiceAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));
    }

    @Test
    @Transactional
    void getNextInvoiceAlpha() throws Exception {
        // Initialize the database
        insertedNextInvoiceAlpha = nextInvoiceAlphaRepository.saveAndFlush(nextInvoiceAlpha);

        // Get the nextInvoiceAlpha
        restNextInvoiceAlphaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextInvoiceAlpha.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextInvoiceAlpha.getId().intValue()))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER))
            .andExpect(jsonPath("$.issueDate").value(DEFAULT_ISSUE_DATE.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)));
    }

    @Test
    @Transactional
    void getNextInvoiceAlphasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextInvoiceAlpha = nextInvoiceAlphaRepository.saveAndFlush(nextInvoiceAlpha);

        Long id = nextInvoiceAlpha.getId();

        defaultNextInvoiceAlphaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextInvoiceAlphaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextInvoiceAlphaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextInvoiceAlphasByInvoiceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceAlpha = nextInvoiceAlphaRepository.saveAndFlush(nextInvoiceAlpha);

        // Get all the nextInvoiceAlphaList where invoiceNumber equals to
        defaultNextInvoiceAlphaFiltering(
            "invoiceNumber.equals=" + DEFAULT_INVOICE_NUMBER,
            "invoiceNumber.equals=" + UPDATED_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceAlphasByInvoiceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoiceAlpha = nextInvoiceAlphaRepository.saveAndFlush(nextInvoiceAlpha);

        // Get all the nextInvoiceAlphaList where invoiceNumber in
        defaultNextInvoiceAlphaFiltering(
            "invoiceNumber.in=" + DEFAULT_INVOICE_NUMBER + "," + UPDATED_INVOICE_NUMBER,
            "invoiceNumber.in=" + UPDATED_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceAlphasByInvoiceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoiceAlpha = nextInvoiceAlphaRepository.saveAndFlush(nextInvoiceAlpha);

        // Get all the nextInvoiceAlphaList where invoiceNumber is not null
        defaultNextInvoiceAlphaFiltering("invoiceNumber.specified=true", "invoiceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoiceAlphasByInvoiceNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceAlpha = nextInvoiceAlphaRepository.saveAndFlush(nextInvoiceAlpha);

        // Get all the nextInvoiceAlphaList where invoiceNumber contains
        defaultNextInvoiceAlphaFiltering(
            "invoiceNumber.contains=" + DEFAULT_INVOICE_NUMBER,
            "invoiceNumber.contains=" + UPDATED_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceAlphasByInvoiceNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceAlpha = nextInvoiceAlphaRepository.saveAndFlush(nextInvoiceAlpha);

        // Get all the nextInvoiceAlphaList where invoiceNumber does not contain
        defaultNextInvoiceAlphaFiltering(
            "invoiceNumber.doesNotContain=" + UPDATED_INVOICE_NUMBER,
            "invoiceNumber.doesNotContain=" + DEFAULT_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceAlphasByIssueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceAlpha = nextInvoiceAlphaRepository.saveAndFlush(nextInvoiceAlpha);

        // Get all the nextInvoiceAlphaList where issueDate equals to
        defaultNextInvoiceAlphaFiltering("issueDate.equals=" + DEFAULT_ISSUE_DATE, "issueDate.equals=" + UPDATED_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllNextInvoiceAlphasByIssueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoiceAlpha = nextInvoiceAlphaRepository.saveAndFlush(nextInvoiceAlpha);

        // Get all the nextInvoiceAlphaList where issueDate in
        defaultNextInvoiceAlphaFiltering(
            "issueDate.in=" + DEFAULT_ISSUE_DATE + "," + UPDATED_ISSUE_DATE,
            "issueDate.in=" + UPDATED_ISSUE_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceAlphasByIssueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoiceAlpha = nextInvoiceAlphaRepository.saveAndFlush(nextInvoiceAlpha);

        // Get all the nextInvoiceAlphaList where issueDate is not null
        defaultNextInvoiceAlphaFiltering("issueDate.specified=true", "issueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoiceAlphasByDueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceAlpha = nextInvoiceAlphaRepository.saveAndFlush(nextInvoiceAlpha);

        // Get all the nextInvoiceAlphaList where dueDate equals to
        defaultNextInvoiceAlphaFiltering("dueDate.equals=" + DEFAULT_DUE_DATE, "dueDate.equals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllNextInvoiceAlphasByDueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoiceAlpha = nextInvoiceAlphaRepository.saveAndFlush(nextInvoiceAlpha);

        // Get all the nextInvoiceAlphaList where dueDate in
        defaultNextInvoiceAlphaFiltering("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE, "dueDate.in=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllNextInvoiceAlphasByDueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoiceAlpha = nextInvoiceAlphaRepository.saveAndFlush(nextInvoiceAlpha);

        // Get all the nextInvoiceAlphaList where dueDate is not null
        defaultNextInvoiceAlphaFiltering("dueDate.specified=true", "dueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoiceAlphasByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceAlpha = nextInvoiceAlphaRepository.saveAndFlush(nextInvoiceAlpha);

        // Get all the nextInvoiceAlphaList where amount equals to
        defaultNextInvoiceAlphaFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceAlphasByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoiceAlpha = nextInvoiceAlphaRepository.saveAndFlush(nextInvoiceAlpha);

        // Get all the nextInvoiceAlphaList where amount in
        defaultNextInvoiceAlphaFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceAlphasByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoiceAlpha = nextInvoiceAlphaRepository.saveAndFlush(nextInvoiceAlpha);

        // Get all the nextInvoiceAlphaList where amount is not null
        defaultNextInvoiceAlphaFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoiceAlphasByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceAlpha = nextInvoiceAlphaRepository.saveAndFlush(nextInvoiceAlpha);

        // Get all the nextInvoiceAlphaList where amount is greater than or equal to
        defaultNextInvoiceAlphaFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceAlphasByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceAlpha = nextInvoiceAlphaRepository.saveAndFlush(nextInvoiceAlpha);

        // Get all the nextInvoiceAlphaList where amount is less than or equal to
        defaultNextInvoiceAlphaFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceAlphasByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceAlpha = nextInvoiceAlphaRepository.saveAndFlush(nextInvoiceAlpha);

        // Get all the nextInvoiceAlphaList where amount is less than
        defaultNextInvoiceAlphaFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceAlphasByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceAlpha = nextInvoiceAlphaRepository.saveAndFlush(nextInvoiceAlpha);

        // Get all the nextInvoiceAlphaList where amount is greater than
        defaultNextInvoiceAlphaFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceAlphasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextInvoiceAlphaRepository.saveAndFlush(nextInvoiceAlpha);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextInvoiceAlpha.setTenant(tenant);
        nextInvoiceAlphaRepository.saveAndFlush(nextInvoiceAlpha);
        Long tenantId = tenant.getId();
        // Get all the nextInvoiceAlphaList where tenant equals to tenantId
        defaultNextInvoiceAlphaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextInvoiceAlphaList where tenant equals to (tenantId + 1)
        defaultNextInvoiceAlphaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextInvoiceAlphaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextInvoiceAlphaShouldBeFound(shouldBeFound);
        defaultNextInvoiceAlphaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextInvoiceAlphaShouldBeFound(String filter) throws Exception {
        restNextInvoiceAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextInvoiceAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));

        // Check, that the count call also returns 1
        restNextInvoiceAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextInvoiceAlphaShouldNotBeFound(String filter) throws Exception {
        restNextInvoiceAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextInvoiceAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextInvoiceAlpha() throws Exception {
        // Get the nextInvoiceAlpha
        restNextInvoiceAlphaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextInvoiceAlpha() throws Exception {
        // Initialize the database
        insertedNextInvoiceAlpha = nextInvoiceAlphaRepository.saveAndFlush(nextInvoiceAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextInvoiceAlpha
        NextInvoiceAlpha updatedNextInvoiceAlpha = nextInvoiceAlphaRepository.findById(nextInvoiceAlpha.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextInvoiceAlpha are not directly saved in db
        em.detach(updatedNextInvoiceAlpha);
        updatedNextInvoiceAlpha
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);
        NextInvoiceAlphaDTO nextInvoiceAlphaDTO = nextInvoiceAlphaMapper.toDto(updatedNextInvoiceAlpha);

        restNextInvoiceAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextInvoiceAlphaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextInvoiceAlphaDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextInvoiceAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextInvoiceAlphaToMatchAllProperties(updatedNextInvoiceAlpha);
    }

    @Test
    @Transactional
    void putNonExistingNextInvoiceAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceAlpha.setId(longCount.incrementAndGet());

        // Create the NextInvoiceAlpha
        NextInvoiceAlphaDTO nextInvoiceAlphaDTO = nextInvoiceAlphaMapper.toDto(nextInvoiceAlpha);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextInvoiceAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextInvoiceAlphaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextInvoiceAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextInvoiceAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceAlpha.setId(longCount.incrementAndGet());

        // Create the NextInvoiceAlpha
        NextInvoiceAlphaDTO nextInvoiceAlphaDTO = nextInvoiceAlphaMapper.toDto(nextInvoiceAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextInvoiceAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextInvoiceAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceAlpha.setId(longCount.incrementAndGet());

        // Create the NextInvoiceAlpha
        NextInvoiceAlphaDTO nextInvoiceAlphaDTO = nextInvoiceAlphaMapper.toDto(nextInvoiceAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceAlphaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceAlphaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextInvoiceAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextInvoiceAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedNextInvoiceAlpha = nextInvoiceAlphaRepository.saveAndFlush(nextInvoiceAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextInvoiceAlpha using partial update
        NextInvoiceAlpha partialUpdatedNextInvoiceAlpha = new NextInvoiceAlpha();
        partialUpdatedNextInvoiceAlpha.setId(nextInvoiceAlpha.getId());

        partialUpdatedNextInvoiceAlpha.invoiceNumber(UPDATED_INVOICE_NUMBER).dueDate(UPDATED_DUE_DATE).amount(UPDATED_AMOUNT);

        restNextInvoiceAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextInvoiceAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextInvoiceAlpha))
            )
            .andExpect(status().isOk());

        // Validate the NextInvoiceAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextInvoiceAlphaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextInvoiceAlpha, nextInvoiceAlpha),
            getPersistedNextInvoiceAlpha(nextInvoiceAlpha)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextInvoiceAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedNextInvoiceAlpha = nextInvoiceAlphaRepository.saveAndFlush(nextInvoiceAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextInvoiceAlpha using partial update
        NextInvoiceAlpha partialUpdatedNextInvoiceAlpha = new NextInvoiceAlpha();
        partialUpdatedNextInvoiceAlpha.setId(nextInvoiceAlpha.getId());

        partialUpdatedNextInvoiceAlpha
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);

        restNextInvoiceAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextInvoiceAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextInvoiceAlpha))
            )
            .andExpect(status().isOk());

        // Validate the NextInvoiceAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextInvoiceAlphaUpdatableFieldsEquals(
            partialUpdatedNextInvoiceAlpha,
            getPersistedNextInvoiceAlpha(partialUpdatedNextInvoiceAlpha)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextInvoiceAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceAlpha.setId(longCount.incrementAndGet());

        // Create the NextInvoiceAlpha
        NextInvoiceAlphaDTO nextInvoiceAlphaDTO = nextInvoiceAlphaMapper.toDto(nextInvoiceAlpha);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextInvoiceAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextInvoiceAlphaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextInvoiceAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextInvoiceAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceAlpha.setId(longCount.incrementAndGet());

        // Create the NextInvoiceAlpha
        NextInvoiceAlphaDTO nextInvoiceAlphaDTO = nextInvoiceAlphaMapper.toDto(nextInvoiceAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextInvoiceAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextInvoiceAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceAlpha.setId(longCount.incrementAndGet());

        // Create the NextInvoiceAlpha
        NextInvoiceAlphaDTO nextInvoiceAlphaDTO = nextInvoiceAlphaMapper.toDto(nextInvoiceAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceAlphaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextInvoiceAlphaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextInvoiceAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextInvoiceAlpha() throws Exception {
        // Initialize the database
        insertedNextInvoiceAlpha = nextInvoiceAlphaRepository.saveAndFlush(nextInvoiceAlpha);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextInvoiceAlpha
        restNextInvoiceAlphaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextInvoiceAlpha.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextInvoiceAlphaRepository.count();
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

    protected NextInvoiceAlpha getPersistedNextInvoiceAlpha(NextInvoiceAlpha nextInvoiceAlpha) {
        return nextInvoiceAlphaRepository.findById(nextInvoiceAlpha.getId()).orElseThrow();
    }

    protected void assertPersistedNextInvoiceAlphaToMatchAllProperties(NextInvoiceAlpha expectedNextInvoiceAlpha) {
        assertNextInvoiceAlphaAllPropertiesEquals(expectedNextInvoiceAlpha, getPersistedNextInvoiceAlpha(expectedNextInvoiceAlpha));
    }

    protected void assertPersistedNextInvoiceAlphaToMatchUpdatableProperties(NextInvoiceAlpha expectedNextInvoiceAlpha) {
        assertNextInvoiceAlphaAllUpdatablePropertiesEquals(
            expectedNextInvoiceAlpha,
            getPersistedNextInvoiceAlpha(expectedNextInvoiceAlpha)
        );
    }
}
