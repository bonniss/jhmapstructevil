package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextInvoiceThetaAsserts.*;
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
import xyz.jhmapstruct.domain.NextInvoiceTheta;
import xyz.jhmapstruct.repository.NextInvoiceThetaRepository;
import xyz.jhmapstruct.service.dto.NextInvoiceThetaDTO;
import xyz.jhmapstruct.service.mapper.NextInvoiceThetaMapper;

/**
 * Integration tests for the {@link NextInvoiceThetaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextInvoiceThetaResourceIT {

    private static final String DEFAULT_INVOICE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_ISSUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ISSUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/next-invoice-thetas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextInvoiceThetaRepository nextInvoiceThetaRepository;

    @Autowired
    private NextInvoiceThetaMapper nextInvoiceThetaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextInvoiceThetaMockMvc;

    private NextInvoiceTheta nextInvoiceTheta;

    private NextInvoiceTheta insertedNextInvoiceTheta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextInvoiceTheta createEntity() {
        return new NextInvoiceTheta()
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
    public static NextInvoiceTheta createUpdatedEntity() {
        return new NextInvoiceTheta()
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);
    }

    @BeforeEach
    public void initTest() {
        nextInvoiceTheta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextInvoiceTheta != null) {
            nextInvoiceThetaRepository.delete(insertedNextInvoiceTheta);
            insertedNextInvoiceTheta = null;
        }
    }

    @Test
    @Transactional
    void createNextInvoiceTheta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextInvoiceTheta
        NextInvoiceThetaDTO nextInvoiceThetaDTO = nextInvoiceThetaMapper.toDto(nextInvoiceTheta);
        var returnedNextInvoiceThetaDTO = om.readValue(
            restNextInvoiceThetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceThetaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextInvoiceThetaDTO.class
        );

        // Validate the NextInvoiceTheta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextInvoiceTheta = nextInvoiceThetaMapper.toEntity(returnedNextInvoiceThetaDTO);
        assertNextInvoiceThetaUpdatableFieldsEquals(returnedNextInvoiceTheta, getPersistedNextInvoiceTheta(returnedNextInvoiceTheta));

        insertedNextInvoiceTheta = returnedNextInvoiceTheta;
    }

    @Test
    @Transactional
    void createNextInvoiceThetaWithExistingId() throws Exception {
        // Create the NextInvoiceTheta with an existing ID
        nextInvoiceTheta.setId(1L);
        NextInvoiceThetaDTO nextInvoiceThetaDTO = nextInvoiceThetaMapper.toDto(nextInvoiceTheta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextInvoiceThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceThetaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkInvoiceNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextInvoiceTheta.setInvoiceNumber(null);

        // Create the NextInvoiceTheta, which fails.
        NextInvoiceThetaDTO nextInvoiceThetaDTO = nextInvoiceThetaMapper.toDto(nextInvoiceTheta);

        restNextInvoiceThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceThetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIssueDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextInvoiceTheta.setIssueDate(null);

        // Create the NextInvoiceTheta, which fails.
        NextInvoiceThetaDTO nextInvoiceThetaDTO = nextInvoiceThetaMapper.toDto(nextInvoiceTheta);

        restNextInvoiceThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceThetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextInvoiceTheta.setAmount(null);

        // Create the NextInvoiceTheta, which fails.
        NextInvoiceThetaDTO nextInvoiceThetaDTO = nextInvoiceThetaMapper.toDto(nextInvoiceTheta);

        restNextInvoiceThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceThetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextInvoiceThetas() throws Exception {
        // Initialize the database
        insertedNextInvoiceTheta = nextInvoiceThetaRepository.saveAndFlush(nextInvoiceTheta);

        // Get all the nextInvoiceThetaList
        restNextInvoiceThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextInvoiceTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));
    }

    @Test
    @Transactional
    void getNextInvoiceTheta() throws Exception {
        // Initialize the database
        insertedNextInvoiceTheta = nextInvoiceThetaRepository.saveAndFlush(nextInvoiceTheta);

        // Get the nextInvoiceTheta
        restNextInvoiceThetaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextInvoiceTheta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextInvoiceTheta.getId().intValue()))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER))
            .andExpect(jsonPath("$.issueDate").value(DEFAULT_ISSUE_DATE.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)));
    }

    @Test
    @Transactional
    void getNextInvoiceThetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextInvoiceTheta = nextInvoiceThetaRepository.saveAndFlush(nextInvoiceTheta);

        Long id = nextInvoiceTheta.getId();

        defaultNextInvoiceThetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextInvoiceThetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextInvoiceThetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextInvoiceThetasByInvoiceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceTheta = nextInvoiceThetaRepository.saveAndFlush(nextInvoiceTheta);

        // Get all the nextInvoiceThetaList where invoiceNumber equals to
        defaultNextInvoiceThetaFiltering(
            "invoiceNumber.equals=" + DEFAULT_INVOICE_NUMBER,
            "invoiceNumber.equals=" + UPDATED_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceThetasByInvoiceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoiceTheta = nextInvoiceThetaRepository.saveAndFlush(nextInvoiceTheta);

        // Get all the nextInvoiceThetaList where invoiceNumber in
        defaultNextInvoiceThetaFiltering(
            "invoiceNumber.in=" + DEFAULT_INVOICE_NUMBER + "," + UPDATED_INVOICE_NUMBER,
            "invoiceNumber.in=" + UPDATED_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceThetasByInvoiceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoiceTheta = nextInvoiceThetaRepository.saveAndFlush(nextInvoiceTheta);

        // Get all the nextInvoiceThetaList where invoiceNumber is not null
        defaultNextInvoiceThetaFiltering("invoiceNumber.specified=true", "invoiceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoiceThetasByInvoiceNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceTheta = nextInvoiceThetaRepository.saveAndFlush(nextInvoiceTheta);

        // Get all the nextInvoiceThetaList where invoiceNumber contains
        defaultNextInvoiceThetaFiltering(
            "invoiceNumber.contains=" + DEFAULT_INVOICE_NUMBER,
            "invoiceNumber.contains=" + UPDATED_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceThetasByInvoiceNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceTheta = nextInvoiceThetaRepository.saveAndFlush(nextInvoiceTheta);

        // Get all the nextInvoiceThetaList where invoiceNumber does not contain
        defaultNextInvoiceThetaFiltering(
            "invoiceNumber.doesNotContain=" + UPDATED_INVOICE_NUMBER,
            "invoiceNumber.doesNotContain=" + DEFAULT_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceThetasByIssueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceTheta = nextInvoiceThetaRepository.saveAndFlush(nextInvoiceTheta);

        // Get all the nextInvoiceThetaList where issueDate equals to
        defaultNextInvoiceThetaFiltering("issueDate.equals=" + DEFAULT_ISSUE_DATE, "issueDate.equals=" + UPDATED_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllNextInvoiceThetasByIssueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoiceTheta = nextInvoiceThetaRepository.saveAndFlush(nextInvoiceTheta);

        // Get all the nextInvoiceThetaList where issueDate in
        defaultNextInvoiceThetaFiltering(
            "issueDate.in=" + DEFAULT_ISSUE_DATE + "," + UPDATED_ISSUE_DATE,
            "issueDate.in=" + UPDATED_ISSUE_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceThetasByIssueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoiceTheta = nextInvoiceThetaRepository.saveAndFlush(nextInvoiceTheta);

        // Get all the nextInvoiceThetaList where issueDate is not null
        defaultNextInvoiceThetaFiltering("issueDate.specified=true", "issueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoiceThetasByDueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceTheta = nextInvoiceThetaRepository.saveAndFlush(nextInvoiceTheta);

        // Get all the nextInvoiceThetaList where dueDate equals to
        defaultNextInvoiceThetaFiltering("dueDate.equals=" + DEFAULT_DUE_DATE, "dueDate.equals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllNextInvoiceThetasByDueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoiceTheta = nextInvoiceThetaRepository.saveAndFlush(nextInvoiceTheta);

        // Get all the nextInvoiceThetaList where dueDate in
        defaultNextInvoiceThetaFiltering("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE, "dueDate.in=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllNextInvoiceThetasByDueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoiceTheta = nextInvoiceThetaRepository.saveAndFlush(nextInvoiceTheta);

        // Get all the nextInvoiceThetaList where dueDate is not null
        defaultNextInvoiceThetaFiltering("dueDate.specified=true", "dueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoiceThetasByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceTheta = nextInvoiceThetaRepository.saveAndFlush(nextInvoiceTheta);

        // Get all the nextInvoiceThetaList where amount equals to
        defaultNextInvoiceThetaFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceThetasByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoiceTheta = nextInvoiceThetaRepository.saveAndFlush(nextInvoiceTheta);

        // Get all the nextInvoiceThetaList where amount in
        defaultNextInvoiceThetaFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceThetasByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoiceTheta = nextInvoiceThetaRepository.saveAndFlush(nextInvoiceTheta);

        // Get all the nextInvoiceThetaList where amount is not null
        defaultNextInvoiceThetaFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoiceThetasByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceTheta = nextInvoiceThetaRepository.saveAndFlush(nextInvoiceTheta);

        // Get all the nextInvoiceThetaList where amount is greater than or equal to
        defaultNextInvoiceThetaFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceThetasByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceTheta = nextInvoiceThetaRepository.saveAndFlush(nextInvoiceTheta);

        // Get all the nextInvoiceThetaList where amount is less than or equal to
        defaultNextInvoiceThetaFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceThetasByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceTheta = nextInvoiceThetaRepository.saveAndFlush(nextInvoiceTheta);

        // Get all the nextInvoiceThetaList where amount is less than
        defaultNextInvoiceThetaFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceThetasByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceTheta = nextInvoiceThetaRepository.saveAndFlush(nextInvoiceTheta);

        // Get all the nextInvoiceThetaList where amount is greater than
        defaultNextInvoiceThetaFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceThetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextInvoiceThetaRepository.saveAndFlush(nextInvoiceTheta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextInvoiceTheta.setTenant(tenant);
        nextInvoiceThetaRepository.saveAndFlush(nextInvoiceTheta);
        Long tenantId = tenant.getId();
        // Get all the nextInvoiceThetaList where tenant equals to tenantId
        defaultNextInvoiceThetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextInvoiceThetaList where tenant equals to (tenantId + 1)
        defaultNextInvoiceThetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextInvoiceThetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextInvoiceThetaShouldBeFound(shouldBeFound);
        defaultNextInvoiceThetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextInvoiceThetaShouldBeFound(String filter) throws Exception {
        restNextInvoiceThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextInvoiceTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));

        // Check, that the count call also returns 1
        restNextInvoiceThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextInvoiceThetaShouldNotBeFound(String filter) throws Exception {
        restNextInvoiceThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextInvoiceThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextInvoiceTheta() throws Exception {
        // Get the nextInvoiceTheta
        restNextInvoiceThetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextInvoiceTheta() throws Exception {
        // Initialize the database
        insertedNextInvoiceTheta = nextInvoiceThetaRepository.saveAndFlush(nextInvoiceTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextInvoiceTheta
        NextInvoiceTheta updatedNextInvoiceTheta = nextInvoiceThetaRepository.findById(nextInvoiceTheta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextInvoiceTheta are not directly saved in db
        em.detach(updatedNextInvoiceTheta);
        updatedNextInvoiceTheta
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);
        NextInvoiceThetaDTO nextInvoiceThetaDTO = nextInvoiceThetaMapper.toDto(updatedNextInvoiceTheta);

        restNextInvoiceThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextInvoiceThetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextInvoiceThetaDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextInvoiceTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextInvoiceThetaToMatchAllProperties(updatedNextInvoiceTheta);
    }

    @Test
    @Transactional
    void putNonExistingNextInvoiceTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceTheta.setId(longCount.incrementAndGet());

        // Create the NextInvoiceTheta
        NextInvoiceThetaDTO nextInvoiceThetaDTO = nextInvoiceThetaMapper.toDto(nextInvoiceTheta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextInvoiceThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextInvoiceThetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextInvoiceThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextInvoiceTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceTheta.setId(longCount.incrementAndGet());

        // Create the NextInvoiceTheta
        NextInvoiceThetaDTO nextInvoiceThetaDTO = nextInvoiceThetaMapper.toDto(nextInvoiceTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextInvoiceThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextInvoiceTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceTheta.setId(longCount.incrementAndGet());

        // Create the NextInvoiceTheta
        NextInvoiceThetaDTO nextInvoiceThetaDTO = nextInvoiceThetaMapper.toDto(nextInvoiceTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceThetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceThetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextInvoiceTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextInvoiceThetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextInvoiceTheta = nextInvoiceThetaRepository.saveAndFlush(nextInvoiceTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextInvoiceTheta using partial update
        NextInvoiceTheta partialUpdatedNextInvoiceTheta = new NextInvoiceTheta();
        partialUpdatedNextInvoiceTheta.setId(nextInvoiceTheta.getId());

        partialUpdatedNextInvoiceTheta.issueDate(UPDATED_ISSUE_DATE).amount(UPDATED_AMOUNT);

        restNextInvoiceThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextInvoiceTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextInvoiceTheta))
            )
            .andExpect(status().isOk());

        // Validate the NextInvoiceTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextInvoiceThetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextInvoiceTheta, nextInvoiceTheta),
            getPersistedNextInvoiceTheta(nextInvoiceTheta)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextInvoiceThetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextInvoiceTheta = nextInvoiceThetaRepository.saveAndFlush(nextInvoiceTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextInvoiceTheta using partial update
        NextInvoiceTheta partialUpdatedNextInvoiceTheta = new NextInvoiceTheta();
        partialUpdatedNextInvoiceTheta.setId(nextInvoiceTheta.getId());

        partialUpdatedNextInvoiceTheta
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);

        restNextInvoiceThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextInvoiceTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextInvoiceTheta))
            )
            .andExpect(status().isOk());

        // Validate the NextInvoiceTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextInvoiceThetaUpdatableFieldsEquals(
            partialUpdatedNextInvoiceTheta,
            getPersistedNextInvoiceTheta(partialUpdatedNextInvoiceTheta)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextInvoiceTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceTheta.setId(longCount.incrementAndGet());

        // Create the NextInvoiceTheta
        NextInvoiceThetaDTO nextInvoiceThetaDTO = nextInvoiceThetaMapper.toDto(nextInvoiceTheta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextInvoiceThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextInvoiceThetaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextInvoiceThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextInvoiceTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceTheta.setId(longCount.incrementAndGet());

        // Create the NextInvoiceTheta
        NextInvoiceThetaDTO nextInvoiceThetaDTO = nextInvoiceThetaMapper.toDto(nextInvoiceTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextInvoiceThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextInvoiceTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceTheta.setId(longCount.incrementAndGet());

        // Create the NextInvoiceTheta
        NextInvoiceThetaDTO nextInvoiceThetaDTO = nextInvoiceThetaMapper.toDto(nextInvoiceTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceThetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextInvoiceThetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextInvoiceTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextInvoiceTheta() throws Exception {
        // Initialize the database
        insertedNextInvoiceTheta = nextInvoiceThetaRepository.saveAndFlush(nextInvoiceTheta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextInvoiceTheta
        restNextInvoiceThetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextInvoiceTheta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextInvoiceThetaRepository.count();
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

    protected NextInvoiceTheta getPersistedNextInvoiceTheta(NextInvoiceTheta nextInvoiceTheta) {
        return nextInvoiceThetaRepository.findById(nextInvoiceTheta.getId()).orElseThrow();
    }

    protected void assertPersistedNextInvoiceThetaToMatchAllProperties(NextInvoiceTheta expectedNextInvoiceTheta) {
        assertNextInvoiceThetaAllPropertiesEquals(expectedNextInvoiceTheta, getPersistedNextInvoiceTheta(expectedNextInvoiceTheta));
    }

    protected void assertPersistedNextInvoiceThetaToMatchUpdatableProperties(NextInvoiceTheta expectedNextInvoiceTheta) {
        assertNextInvoiceThetaAllUpdatablePropertiesEquals(
            expectedNextInvoiceTheta,
            getPersistedNextInvoiceTheta(expectedNextInvoiceTheta)
        );
    }
}
