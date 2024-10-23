package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextInvoiceBetaAsserts.*;
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
import xyz.jhmapstruct.domain.NextInvoiceBeta;
import xyz.jhmapstruct.repository.NextInvoiceBetaRepository;
import xyz.jhmapstruct.service.dto.NextInvoiceBetaDTO;
import xyz.jhmapstruct.service.mapper.NextInvoiceBetaMapper;

/**
 * Integration tests for the {@link NextInvoiceBetaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextInvoiceBetaResourceIT {

    private static final String DEFAULT_INVOICE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_ISSUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ISSUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/next-invoice-betas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextInvoiceBetaRepository nextInvoiceBetaRepository;

    @Autowired
    private NextInvoiceBetaMapper nextInvoiceBetaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextInvoiceBetaMockMvc;

    private NextInvoiceBeta nextInvoiceBeta;

    private NextInvoiceBeta insertedNextInvoiceBeta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextInvoiceBeta createEntity() {
        return new NextInvoiceBeta()
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
    public static NextInvoiceBeta createUpdatedEntity() {
        return new NextInvoiceBeta()
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);
    }

    @BeforeEach
    public void initTest() {
        nextInvoiceBeta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextInvoiceBeta != null) {
            nextInvoiceBetaRepository.delete(insertedNextInvoiceBeta);
            insertedNextInvoiceBeta = null;
        }
    }

    @Test
    @Transactional
    void createNextInvoiceBeta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextInvoiceBeta
        NextInvoiceBetaDTO nextInvoiceBetaDTO = nextInvoiceBetaMapper.toDto(nextInvoiceBeta);
        var returnedNextInvoiceBetaDTO = om.readValue(
            restNextInvoiceBetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceBetaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextInvoiceBetaDTO.class
        );

        // Validate the NextInvoiceBeta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextInvoiceBeta = nextInvoiceBetaMapper.toEntity(returnedNextInvoiceBetaDTO);
        assertNextInvoiceBetaUpdatableFieldsEquals(returnedNextInvoiceBeta, getPersistedNextInvoiceBeta(returnedNextInvoiceBeta));

        insertedNextInvoiceBeta = returnedNextInvoiceBeta;
    }

    @Test
    @Transactional
    void createNextInvoiceBetaWithExistingId() throws Exception {
        // Create the NextInvoiceBeta with an existing ID
        nextInvoiceBeta.setId(1L);
        NextInvoiceBetaDTO nextInvoiceBetaDTO = nextInvoiceBetaMapper.toDto(nextInvoiceBeta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextInvoiceBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceBetaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkInvoiceNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextInvoiceBeta.setInvoiceNumber(null);

        // Create the NextInvoiceBeta, which fails.
        NextInvoiceBetaDTO nextInvoiceBetaDTO = nextInvoiceBetaMapper.toDto(nextInvoiceBeta);

        restNextInvoiceBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceBetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIssueDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextInvoiceBeta.setIssueDate(null);

        // Create the NextInvoiceBeta, which fails.
        NextInvoiceBetaDTO nextInvoiceBetaDTO = nextInvoiceBetaMapper.toDto(nextInvoiceBeta);

        restNextInvoiceBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceBetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextInvoiceBeta.setAmount(null);

        // Create the NextInvoiceBeta, which fails.
        NextInvoiceBetaDTO nextInvoiceBetaDTO = nextInvoiceBetaMapper.toDto(nextInvoiceBeta);

        restNextInvoiceBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceBetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextInvoiceBetas() throws Exception {
        // Initialize the database
        insertedNextInvoiceBeta = nextInvoiceBetaRepository.saveAndFlush(nextInvoiceBeta);

        // Get all the nextInvoiceBetaList
        restNextInvoiceBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextInvoiceBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));
    }

    @Test
    @Transactional
    void getNextInvoiceBeta() throws Exception {
        // Initialize the database
        insertedNextInvoiceBeta = nextInvoiceBetaRepository.saveAndFlush(nextInvoiceBeta);

        // Get the nextInvoiceBeta
        restNextInvoiceBetaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextInvoiceBeta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextInvoiceBeta.getId().intValue()))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER))
            .andExpect(jsonPath("$.issueDate").value(DEFAULT_ISSUE_DATE.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)));
    }

    @Test
    @Transactional
    void getNextInvoiceBetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextInvoiceBeta = nextInvoiceBetaRepository.saveAndFlush(nextInvoiceBeta);

        Long id = nextInvoiceBeta.getId();

        defaultNextInvoiceBetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextInvoiceBetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextInvoiceBetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextInvoiceBetasByInvoiceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceBeta = nextInvoiceBetaRepository.saveAndFlush(nextInvoiceBeta);

        // Get all the nextInvoiceBetaList where invoiceNumber equals to
        defaultNextInvoiceBetaFiltering("invoiceNumber.equals=" + DEFAULT_INVOICE_NUMBER, "invoiceNumber.equals=" + UPDATED_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextInvoiceBetasByInvoiceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoiceBeta = nextInvoiceBetaRepository.saveAndFlush(nextInvoiceBeta);

        // Get all the nextInvoiceBetaList where invoiceNumber in
        defaultNextInvoiceBetaFiltering(
            "invoiceNumber.in=" + DEFAULT_INVOICE_NUMBER + "," + UPDATED_INVOICE_NUMBER,
            "invoiceNumber.in=" + UPDATED_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceBetasByInvoiceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoiceBeta = nextInvoiceBetaRepository.saveAndFlush(nextInvoiceBeta);

        // Get all the nextInvoiceBetaList where invoiceNumber is not null
        defaultNextInvoiceBetaFiltering("invoiceNumber.specified=true", "invoiceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoiceBetasByInvoiceNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceBeta = nextInvoiceBetaRepository.saveAndFlush(nextInvoiceBeta);

        // Get all the nextInvoiceBetaList where invoiceNumber contains
        defaultNextInvoiceBetaFiltering(
            "invoiceNumber.contains=" + DEFAULT_INVOICE_NUMBER,
            "invoiceNumber.contains=" + UPDATED_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceBetasByInvoiceNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceBeta = nextInvoiceBetaRepository.saveAndFlush(nextInvoiceBeta);

        // Get all the nextInvoiceBetaList where invoiceNumber does not contain
        defaultNextInvoiceBetaFiltering(
            "invoiceNumber.doesNotContain=" + UPDATED_INVOICE_NUMBER,
            "invoiceNumber.doesNotContain=" + DEFAULT_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceBetasByIssueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceBeta = nextInvoiceBetaRepository.saveAndFlush(nextInvoiceBeta);

        // Get all the nextInvoiceBetaList where issueDate equals to
        defaultNextInvoiceBetaFiltering("issueDate.equals=" + DEFAULT_ISSUE_DATE, "issueDate.equals=" + UPDATED_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllNextInvoiceBetasByIssueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoiceBeta = nextInvoiceBetaRepository.saveAndFlush(nextInvoiceBeta);

        // Get all the nextInvoiceBetaList where issueDate in
        defaultNextInvoiceBetaFiltering(
            "issueDate.in=" + DEFAULT_ISSUE_DATE + "," + UPDATED_ISSUE_DATE,
            "issueDate.in=" + UPDATED_ISSUE_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceBetasByIssueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoiceBeta = nextInvoiceBetaRepository.saveAndFlush(nextInvoiceBeta);

        // Get all the nextInvoiceBetaList where issueDate is not null
        defaultNextInvoiceBetaFiltering("issueDate.specified=true", "issueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoiceBetasByDueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceBeta = nextInvoiceBetaRepository.saveAndFlush(nextInvoiceBeta);

        // Get all the nextInvoiceBetaList where dueDate equals to
        defaultNextInvoiceBetaFiltering("dueDate.equals=" + DEFAULT_DUE_DATE, "dueDate.equals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllNextInvoiceBetasByDueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoiceBeta = nextInvoiceBetaRepository.saveAndFlush(nextInvoiceBeta);

        // Get all the nextInvoiceBetaList where dueDate in
        defaultNextInvoiceBetaFiltering("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE, "dueDate.in=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllNextInvoiceBetasByDueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoiceBeta = nextInvoiceBetaRepository.saveAndFlush(nextInvoiceBeta);

        // Get all the nextInvoiceBetaList where dueDate is not null
        defaultNextInvoiceBetaFiltering("dueDate.specified=true", "dueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoiceBetasByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceBeta = nextInvoiceBetaRepository.saveAndFlush(nextInvoiceBeta);

        // Get all the nextInvoiceBetaList where amount equals to
        defaultNextInvoiceBetaFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceBetasByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoiceBeta = nextInvoiceBetaRepository.saveAndFlush(nextInvoiceBeta);

        // Get all the nextInvoiceBetaList where amount in
        defaultNextInvoiceBetaFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceBetasByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoiceBeta = nextInvoiceBetaRepository.saveAndFlush(nextInvoiceBeta);

        // Get all the nextInvoiceBetaList where amount is not null
        defaultNextInvoiceBetaFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoiceBetasByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceBeta = nextInvoiceBetaRepository.saveAndFlush(nextInvoiceBeta);

        // Get all the nextInvoiceBetaList where amount is greater than or equal to
        defaultNextInvoiceBetaFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceBetasByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceBeta = nextInvoiceBetaRepository.saveAndFlush(nextInvoiceBeta);

        // Get all the nextInvoiceBetaList where amount is less than or equal to
        defaultNextInvoiceBetaFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceBetasByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceBeta = nextInvoiceBetaRepository.saveAndFlush(nextInvoiceBeta);

        // Get all the nextInvoiceBetaList where amount is less than
        defaultNextInvoiceBetaFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceBetasByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceBeta = nextInvoiceBetaRepository.saveAndFlush(nextInvoiceBeta);

        // Get all the nextInvoiceBetaList where amount is greater than
        defaultNextInvoiceBetaFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceBetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextInvoiceBetaRepository.saveAndFlush(nextInvoiceBeta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextInvoiceBeta.setTenant(tenant);
        nextInvoiceBetaRepository.saveAndFlush(nextInvoiceBeta);
        Long tenantId = tenant.getId();
        // Get all the nextInvoiceBetaList where tenant equals to tenantId
        defaultNextInvoiceBetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextInvoiceBetaList where tenant equals to (tenantId + 1)
        defaultNextInvoiceBetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextInvoiceBetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextInvoiceBetaShouldBeFound(shouldBeFound);
        defaultNextInvoiceBetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextInvoiceBetaShouldBeFound(String filter) throws Exception {
        restNextInvoiceBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextInvoiceBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));

        // Check, that the count call also returns 1
        restNextInvoiceBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextInvoiceBetaShouldNotBeFound(String filter) throws Exception {
        restNextInvoiceBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextInvoiceBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextInvoiceBeta() throws Exception {
        // Get the nextInvoiceBeta
        restNextInvoiceBetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextInvoiceBeta() throws Exception {
        // Initialize the database
        insertedNextInvoiceBeta = nextInvoiceBetaRepository.saveAndFlush(nextInvoiceBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextInvoiceBeta
        NextInvoiceBeta updatedNextInvoiceBeta = nextInvoiceBetaRepository.findById(nextInvoiceBeta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextInvoiceBeta are not directly saved in db
        em.detach(updatedNextInvoiceBeta);
        updatedNextInvoiceBeta
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);
        NextInvoiceBetaDTO nextInvoiceBetaDTO = nextInvoiceBetaMapper.toDto(updatedNextInvoiceBeta);

        restNextInvoiceBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextInvoiceBetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextInvoiceBetaDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextInvoiceBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextInvoiceBetaToMatchAllProperties(updatedNextInvoiceBeta);
    }

    @Test
    @Transactional
    void putNonExistingNextInvoiceBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceBeta.setId(longCount.incrementAndGet());

        // Create the NextInvoiceBeta
        NextInvoiceBetaDTO nextInvoiceBetaDTO = nextInvoiceBetaMapper.toDto(nextInvoiceBeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextInvoiceBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextInvoiceBetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextInvoiceBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextInvoiceBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceBeta.setId(longCount.incrementAndGet());

        // Create the NextInvoiceBeta
        NextInvoiceBetaDTO nextInvoiceBetaDTO = nextInvoiceBetaMapper.toDto(nextInvoiceBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextInvoiceBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextInvoiceBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceBeta.setId(longCount.incrementAndGet());

        // Create the NextInvoiceBeta
        NextInvoiceBetaDTO nextInvoiceBetaDTO = nextInvoiceBetaMapper.toDto(nextInvoiceBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceBetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceBetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextInvoiceBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextInvoiceBetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextInvoiceBeta = nextInvoiceBetaRepository.saveAndFlush(nextInvoiceBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextInvoiceBeta using partial update
        NextInvoiceBeta partialUpdatedNextInvoiceBeta = new NextInvoiceBeta();
        partialUpdatedNextInvoiceBeta.setId(nextInvoiceBeta.getId());

        partialUpdatedNextInvoiceBeta.issueDate(UPDATED_ISSUE_DATE).amount(UPDATED_AMOUNT);

        restNextInvoiceBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextInvoiceBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextInvoiceBeta))
            )
            .andExpect(status().isOk());

        // Validate the NextInvoiceBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextInvoiceBetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextInvoiceBeta, nextInvoiceBeta),
            getPersistedNextInvoiceBeta(nextInvoiceBeta)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextInvoiceBetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextInvoiceBeta = nextInvoiceBetaRepository.saveAndFlush(nextInvoiceBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextInvoiceBeta using partial update
        NextInvoiceBeta partialUpdatedNextInvoiceBeta = new NextInvoiceBeta();
        partialUpdatedNextInvoiceBeta.setId(nextInvoiceBeta.getId());

        partialUpdatedNextInvoiceBeta
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);

        restNextInvoiceBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextInvoiceBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextInvoiceBeta))
            )
            .andExpect(status().isOk());

        // Validate the NextInvoiceBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextInvoiceBetaUpdatableFieldsEquals(
            partialUpdatedNextInvoiceBeta,
            getPersistedNextInvoiceBeta(partialUpdatedNextInvoiceBeta)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextInvoiceBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceBeta.setId(longCount.incrementAndGet());

        // Create the NextInvoiceBeta
        NextInvoiceBetaDTO nextInvoiceBetaDTO = nextInvoiceBetaMapper.toDto(nextInvoiceBeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextInvoiceBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextInvoiceBetaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextInvoiceBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextInvoiceBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceBeta.setId(longCount.incrementAndGet());

        // Create the NextInvoiceBeta
        NextInvoiceBetaDTO nextInvoiceBetaDTO = nextInvoiceBetaMapper.toDto(nextInvoiceBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextInvoiceBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextInvoiceBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceBeta.setId(longCount.incrementAndGet());

        // Create the NextInvoiceBeta
        NextInvoiceBetaDTO nextInvoiceBetaDTO = nextInvoiceBetaMapper.toDto(nextInvoiceBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceBetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextInvoiceBetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextInvoiceBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextInvoiceBeta() throws Exception {
        // Initialize the database
        insertedNextInvoiceBeta = nextInvoiceBetaRepository.saveAndFlush(nextInvoiceBeta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextInvoiceBeta
        restNextInvoiceBetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextInvoiceBeta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextInvoiceBetaRepository.count();
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

    protected NextInvoiceBeta getPersistedNextInvoiceBeta(NextInvoiceBeta nextInvoiceBeta) {
        return nextInvoiceBetaRepository.findById(nextInvoiceBeta.getId()).orElseThrow();
    }

    protected void assertPersistedNextInvoiceBetaToMatchAllProperties(NextInvoiceBeta expectedNextInvoiceBeta) {
        assertNextInvoiceBetaAllPropertiesEquals(expectedNextInvoiceBeta, getPersistedNextInvoiceBeta(expectedNextInvoiceBeta));
    }

    protected void assertPersistedNextInvoiceBetaToMatchUpdatableProperties(NextInvoiceBeta expectedNextInvoiceBeta) {
        assertNextInvoiceBetaAllUpdatablePropertiesEquals(expectedNextInvoiceBeta, getPersistedNextInvoiceBeta(expectedNextInvoiceBeta));
    }
}
