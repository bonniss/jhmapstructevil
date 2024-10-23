package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.InvoiceThetaAsserts.*;
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
import xyz.jhmapstruct.domain.InvoiceTheta;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.repository.InvoiceThetaRepository;
import xyz.jhmapstruct.service.dto.InvoiceThetaDTO;
import xyz.jhmapstruct.service.mapper.InvoiceThetaMapper;

/**
 * Integration tests for the {@link InvoiceThetaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InvoiceThetaResourceIT {

    private static final String DEFAULT_INVOICE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_ISSUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ISSUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/invoice-thetas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InvoiceThetaRepository invoiceThetaRepository;

    @Autowired
    private InvoiceThetaMapper invoiceThetaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceThetaMockMvc;

    private InvoiceTheta invoiceTheta;

    private InvoiceTheta insertedInvoiceTheta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceTheta createEntity() {
        return new InvoiceTheta()
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
    public static InvoiceTheta createUpdatedEntity() {
        return new InvoiceTheta()
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);
    }

    @BeforeEach
    public void initTest() {
        invoiceTheta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedInvoiceTheta != null) {
            invoiceThetaRepository.delete(insertedInvoiceTheta);
            insertedInvoiceTheta = null;
        }
    }

    @Test
    @Transactional
    void createInvoiceTheta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InvoiceTheta
        InvoiceThetaDTO invoiceThetaDTO = invoiceThetaMapper.toDto(invoiceTheta);
        var returnedInvoiceThetaDTO = om.readValue(
            restInvoiceThetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceThetaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InvoiceThetaDTO.class
        );

        // Validate the InvoiceTheta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedInvoiceTheta = invoiceThetaMapper.toEntity(returnedInvoiceThetaDTO);
        assertInvoiceThetaUpdatableFieldsEquals(returnedInvoiceTheta, getPersistedInvoiceTheta(returnedInvoiceTheta));

        insertedInvoiceTheta = returnedInvoiceTheta;
    }

    @Test
    @Transactional
    void createInvoiceThetaWithExistingId() throws Exception {
        // Create the InvoiceTheta with an existing ID
        invoiceTheta.setId(1L);
        InvoiceThetaDTO invoiceThetaDTO = invoiceThetaMapper.toDto(invoiceTheta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceThetaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkInvoiceNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceTheta.setInvoiceNumber(null);

        // Create the InvoiceTheta, which fails.
        InvoiceThetaDTO invoiceThetaDTO = invoiceThetaMapper.toDto(invoiceTheta);

        restInvoiceThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceThetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIssueDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceTheta.setIssueDate(null);

        // Create the InvoiceTheta, which fails.
        InvoiceThetaDTO invoiceThetaDTO = invoiceThetaMapper.toDto(invoiceTheta);

        restInvoiceThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceThetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceTheta.setAmount(null);

        // Create the InvoiceTheta, which fails.
        InvoiceThetaDTO invoiceThetaDTO = invoiceThetaMapper.toDto(invoiceTheta);

        restInvoiceThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceThetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInvoiceThetas() throws Exception {
        // Initialize the database
        insertedInvoiceTheta = invoiceThetaRepository.saveAndFlush(invoiceTheta);

        // Get all the invoiceThetaList
        restInvoiceThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));
    }

    @Test
    @Transactional
    void getInvoiceTheta() throws Exception {
        // Initialize the database
        insertedInvoiceTheta = invoiceThetaRepository.saveAndFlush(invoiceTheta);

        // Get the invoiceTheta
        restInvoiceThetaMockMvc
            .perform(get(ENTITY_API_URL_ID, invoiceTheta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceTheta.getId().intValue()))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER))
            .andExpect(jsonPath("$.issueDate").value(DEFAULT_ISSUE_DATE.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)));
    }

    @Test
    @Transactional
    void getInvoiceThetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedInvoiceTheta = invoiceThetaRepository.saveAndFlush(invoiceTheta);

        Long id = invoiceTheta.getId();

        defaultInvoiceThetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInvoiceThetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInvoiceThetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInvoiceThetasByInvoiceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceTheta = invoiceThetaRepository.saveAndFlush(invoiceTheta);

        // Get all the invoiceThetaList where invoiceNumber equals to
        defaultInvoiceThetaFiltering("invoiceNumber.equals=" + DEFAULT_INVOICE_NUMBER, "invoiceNumber.equals=" + UPDATED_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    void getAllInvoiceThetasByInvoiceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceTheta = invoiceThetaRepository.saveAndFlush(invoiceTheta);

        // Get all the invoiceThetaList where invoiceNumber in
        defaultInvoiceThetaFiltering(
            "invoiceNumber.in=" + DEFAULT_INVOICE_NUMBER + "," + UPDATED_INVOICE_NUMBER,
            "invoiceNumber.in=" + UPDATED_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllInvoiceThetasByInvoiceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceTheta = invoiceThetaRepository.saveAndFlush(invoiceTheta);

        // Get all the invoiceThetaList where invoiceNumber is not null
        defaultInvoiceThetaFiltering("invoiceNumber.specified=true", "invoiceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceThetasByInvoiceNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoiceTheta = invoiceThetaRepository.saveAndFlush(invoiceTheta);

        // Get all the invoiceThetaList where invoiceNumber contains
        defaultInvoiceThetaFiltering(
            "invoiceNumber.contains=" + DEFAULT_INVOICE_NUMBER,
            "invoiceNumber.contains=" + UPDATED_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllInvoiceThetasByInvoiceNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoiceTheta = invoiceThetaRepository.saveAndFlush(invoiceTheta);

        // Get all the invoiceThetaList where invoiceNumber does not contain
        defaultInvoiceThetaFiltering(
            "invoiceNumber.doesNotContain=" + UPDATED_INVOICE_NUMBER,
            "invoiceNumber.doesNotContain=" + DEFAULT_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllInvoiceThetasByIssueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceTheta = invoiceThetaRepository.saveAndFlush(invoiceTheta);

        // Get all the invoiceThetaList where issueDate equals to
        defaultInvoiceThetaFiltering("issueDate.equals=" + DEFAULT_ISSUE_DATE, "issueDate.equals=" + UPDATED_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllInvoiceThetasByIssueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceTheta = invoiceThetaRepository.saveAndFlush(invoiceTheta);

        // Get all the invoiceThetaList where issueDate in
        defaultInvoiceThetaFiltering("issueDate.in=" + DEFAULT_ISSUE_DATE + "," + UPDATED_ISSUE_DATE, "issueDate.in=" + UPDATED_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllInvoiceThetasByIssueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceTheta = invoiceThetaRepository.saveAndFlush(invoiceTheta);

        // Get all the invoiceThetaList where issueDate is not null
        defaultInvoiceThetaFiltering("issueDate.specified=true", "issueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceThetasByDueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceTheta = invoiceThetaRepository.saveAndFlush(invoiceTheta);

        // Get all the invoiceThetaList where dueDate equals to
        defaultInvoiceThetaFiltering("dueDate.equals=" + DEFAULT_DUE_DATE, "dueDate.equals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllInvoiceThetasByDueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceTheta = invoiceThetaRepository.saveAndFlush(invoiceTheta);

        // Get all the invoiceThetaList where dueDate in
        defaultInvoiceThetaFiltering("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE, "dueDate.in=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllInvoiceThetasByDueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceTheta = invoiceThetaRepository.saveAndFlush(invoiceTheta);

        // Get all the invoiceThetaList where dueDate is not null
        defaultInvoiceThetaFiltering("dueDate.specified=true", "dueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceThetasByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceTheta = invoiceThetaRepository.saveAndFlush(invoiceTheta);

        // Get all the invoiceThetaList where amount equals to
        defaultInvoiceThetaFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceThetasByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceTheta = invoiceThetaRepository.saveAndFlush(invoiceTheta);

        // Get all the invoiceThetaList where amount in
        defaultInvoiceThetaFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceThetasByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceTheta = invoiceThetaRepository.saveAndFlush(invoiceTheta);

        // Get all the invoiceThetaList where amount is not null
        defaultInvoiceThetaFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceThetasByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceTheta = invoiceThetaRepository.saveAndFlush(invoiceTheta);

        // Get all the invoiceThetaList where amount is greater than or equal to
        defaultInvoiceThetaFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceThetasByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceTheta = invoiceThetaRepository.saveAndFlush(invoiceTheta);

        // Get all the invoiceThetaList where amount is less than or equal to
        defaultInvoiceThetaFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceThetasByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInvoiceTheta = invoiceThetaRepository.saveAndFlush(invoiceTheta);

        // Get all the invoiceThetaList where amount is less than
        defaultInvoiceThetaFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceThetasByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInvoiceTheta = invoiceThetaRepository.saveAndFlush(invoiceTheta);

        // Get all the invoiceThetaList where amount is greater than
        defaultInvoiceThetaFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceThetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            invoiceThetaRepository.saveAndFlush(invoiceTheta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        invoiceTheta.setTenant(tenant);
        invoiceThetaRepository.saveAndFlush(invoiceTheta);
        Long tenantId = tenant.getId();
        // Get all the invoiceThetaList where tenant equals to tenantId
        defaultInvoiceThetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the invoiceThetaList where tenant equals to (tenantId + 1)
        defaultInvoiceThetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultInvoiceThetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInvoiceThetaShouldBeFound(shouldBeFound);
        defaultInvoiceThetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInvoiceThetaShouldBeFound(String filter) throws Exception {
        restInvoiceThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));

        // Check, that the count call also returns 1
        restInvoiceThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInvoiceThetaShouldNotBeFound(String filter) throws Exception {
        restInvoiceThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInvoiceThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInvoiceTheta() throws Exception {
        // Get the invoiceTheta
        restInvoiceThetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInvoiceTheta() throws Exception {
        // Initialize the database
        insertedInvoiceTheta = invoiceThetaRepository.saveAndFlush(invoiceTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceTheta
        InvoiceTheta updatedInvoiceTheta = invoiceThetaRepository.findById(invoiceTheta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInvoiceTheta are not directly saved in db
        em.detach(updatedInvoiceTheta);
        updatedInvoiceTheta
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);
        InvoiceThetaDTO invoiceThetaDTO = invoiceThetaMapper.toDto(updatedInvoiceTheta);

        restInvoiceThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceThetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceThetaDTO))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInvoiceThetaToMatchAllProperties(updatedInvoiceTheta);
    }

    @Test
    @Transactional
    void putNonExistingInvoiceTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceTheta.setId(longCount.incrementAndGet());

        // Create the InvoiceTheta
        InvoiceThetaDTO invoiceThetaDTO = invoiceThetaMapper.toDto(invoiceTheta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceThetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoiceTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceTheta.setId(longCount.incrementAndGet());

        // Create the InvoiceTheta
        InvoiceThetaDTO invoiceThetaDTO = invoiceThetaMapper.toDto(invoiceTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoiceTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceTheta.setId(longCount.incrementAndGet());

        // Create the InvoiceTheta
        InvoiceThetaDTO invoiceThetaDTO = invoiceThetaMapper.toDto(invoiceTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceThetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceThetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceThetaWithPatch() throws Exception {
        // Initialize the database
        insertedInvoiceTheta = invoiceThetaRepository.saveAndFlush(invoiceTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceTheta using partial update
        InvoiceTheta partialUpdatedInvoiceTheta = new InvoiceTheta();
        partialUpdatedInvoiceTheta.setId(invoiceTheta.getId());

        partialUpdatedInvoiceTheta.invoiceNumber(UPDATED_INVOICE_NUMBER).issueDate(UPDATED_ISSUE_DATE).dueDate(UPDATED_DUE_DATE);

        restInvoiceThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoiceTheta))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceThetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInvoiceTheta, invoiceTheta),
            getPersistedInvoiceTheta(invoiceTheta)
        );
    }

    @Test
    @Transactional
    void fullUpdateInvoiceThetaWithPatch() throws Exception {
        // Initialize the database
        insertedInvoiceTheta = invoiceThetaRepository.saveAndFlush(invoiceTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceTheta using partial update
        InvoiceTheta partialUpdatedInvoiceTheta = new InvoiceTheta();
        partialUpdatedInvoiceTheta.setId(invoiceTheta.getId());

        partialUpdatedInvoiceTheta
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);

        restInvoiceThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoiceTheta))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceThetaUpdatableFieldsEquals(partialUpdatedInvoiceTheta, getPersistedInvoiceTheta(partialUpdatedInvoiceTheta));
    }

    @Test
    @Transactional
    void patchNonExistingInvoiceTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceTheta.setId(longCount.incrementAndGet());

        // Create the InvoiceTheta
        InvoiceThetaDTO invoiceThetaDTO = invoiceThetaMapper.toDto(invoiceTheta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoiceThetaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoiceThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoiceTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceTheta.setId(longCount.incrementAndGet());

        // Create the InvoiceTheta
        InvoiceThetaDTO invoiceThetaDTO = invoiceThetaMapper.toDto(invoiceTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoiceThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoiceTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceTheta.setId(longCount.incrementAndGet());

        // Create the InvoiceTheta
        InvoiceThetaDTO invoiceThetaDTO = invoiceThetaMapper.toDto(invoiceTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceThetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(invoiceThetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvoiceTheta() throws Exception {
        // Initialize the database
        insertedInvoiceTheta = invoiceThetaRepository.saveAndFlush(invoiceTheta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the invoiceTheta
        restInvoiceThetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoiceTheta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return invoiceThetaRepository.count();
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

    protected InvoiceTheta getPersistedInvoiceTheta(InvoiceTheta invoiceTheta) {
        return invoiceThetaRepository.findById(invoiceTheta.getId()).orElseThrow();
    }

    protected void assertPersistedInvoiceThetaToMatchAllProperties(InvoiceTheta expectedInvoiceTheta) {
        assertInvoiceThetaAllPropertiesEquals(expectedInvoiceTheta, getPersistedInvoiceTheta(expectedInvoiceTheta));
    }

    protected void assertPersistedInvoiceThetaToMatchUpdatableProperties(InvoiceTheta expectedInvoiceTheta) {
        assertInvoiceThetaAllUpdatablePropertiesEquals(expectedInvoiceTheta, getPersistedInvoiceTheta(expectedInvoiceTheta));
    }
}
