package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.InvoiceBetaAsserts.*;
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
import xyz.jhmapstruct.domain.InvoiceBeta;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.repository.InvoiceBetaRepository;
import xyz.jhmapstruct.service.dto.InvoiceBetaDTO;
import xyz.jhmapstruct.service.mapper.InvoiceBetaMapper;

/**
 * Integration tests for the {@link InvoiceBetaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InvoiceBetaResourceIT {

    private static final String DEFAULT_INVOICE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_ISSUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ISSUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/invoice-betas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InvoiceBetaRepository invoiceBetaRepository;

    @Autowired
    private InvoiceBetaMapper invoiceBetaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceBetaMockMvc;

    private InvoiceBeta invoiceBeta;

    private InvoiceBeta insertedInvoiceBeta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceBeta createEntity() {
        return new InvoiceBeta()
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
    public static InvoiceBeta createUpdatedEntity() {
        return new InvoiceBeta()
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);
    }

    @BeforeEach
    public void initTest() {
        invoiceBeta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedInvoiceBeta != null) {
            invoiceBetaRepository.delete(insertedInvoiceBeta);
            insertedInvoiceBeta = null;
        }
    }

    @Test
    @Transactional
    void createInvoiceBeta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InvoiceBeta
        InvoiceBetaDTO invoiceBetaDTO = invoiceBetaMapper.toDto(invoiceBeta);
        var returnedInvoiceBetaDTO = om.readValue(
            restInvoiceBetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceBetaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InvoiceBetaDTO.class
        );

        // Validate the InvoiceBeta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedInvoiceBeta = invoiceBetaMapper.toEntity(returnedInvoiceBetaDTO);
        assertInvoiceBetaUpdatableFieldsEquals(returnedInvoiceBeta, getPersistedInvoiceBeta(returnedInvoiceBeta));

        insertedInvoiceBeta = returnedInvoiceBeta;
    }

    @Test
    @Transactional
    void createInvoiceBetaWithExistingId() throws Exception {
        // Create the InvoiceBeta with an existing ID
        invoiceBeta.setId(1L);
        InvoiceBetaDTO invoiceBetaDTO = invoiceBetaMapper.toDto(invoiceBeta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceBetaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkInvoiceNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceBeta.setInvoiceNumber(null);

        // Create the InvoiceBeta, which fails.
        InvoiceBetaDTO invoiceBetaDTO = invoiceBetaMapper.toDto(invoiceBeta);

        restInvoiceBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceBetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIssueDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceBeta.setIssueDate(null);

        // Create the InvoiceBeta, which fails.
        InvoiceBetaDTO invoiceBetaDTO = invoiceBetaMapper.toDto(invoiceBeta);

        restInvoiceBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceBetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceBeta.setAmount(null);

        // Create the InvoiceBeta, which fails.
        InvoiceBetaDTO invoiceBetaDTO = invoiceBetaMapper.toDto(invoiceBeta);

        restInvoiceBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceBetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInvoiceBetas() throws Exception {
        // Initialize the database
        insertedInvoiceBeta = invoiceBetaRepository.saveAndFlush(invoiceBeta);

        // Get all the invoiceBetaList
        restInvoiceBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));
    }

    @Test
    @Transactional
    void getInvoiceBeta() throws Exception {
        // Initialize the database
        insertedInvoiceBeta = invoiceBetaRepository.saveAndFlush(invoiceBeta);

        // Get the invoiceBeta
        restInvoiceBetaMockMvc
            .perform(get(ENTITY_API_URL_ID, invoiceBeta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceBeta.getId().intValue()))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER))
            .andExpect(jsonPath("$.issueDate").value(DEFAULT_ISSUE_DATE.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)));
    }

    @Test
    @Transactional
    void getInvoiceBetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedInvoiceBeta = invoiceBetaRepository.saveAndFlush(invoiceBeta);

        Long id = invoiceBeta.getId();

        defaultInvoiceBetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInvoiceBetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInvoiceBetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInvoiceBetasByInvoiceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceBeta = invoiceBetaRepository.saveAndFlush(invoiceBeta);

        // Get all the invoiceBetaList where invoiceNumber equals to
        defaultInvoiceBetaFiltering("invoiceNumber.equals=" + DEFAULT_INVOICE_NUMBER, "invoiceNumber.equals=" + UPDATED_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    void getAllInvoiceBetasByInvoiceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceBeta = invoiceBetaRepository.saveAndFlush(invoiceBeta);

        // Get all the invoiceBetaList where invoiceNumber in
        defaultInvoiceBetaFiltering(
            "invoiceNumber.in=" + DEFAULT_INVOICE_NUMBER + "," + UPDATED_INVOICE_NUMBER,
            "invoiceNumber.in=" + UPDATED_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllInvoiceBetasByInvoiceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceBeta = invoiceBetaRepository.saveAndFlush(invoiceBeta);

        // Get all the invoiceBetaList where invoiceNumber is not null
        defaultInvoiceBetaFiltering("invoiceNumber.specified=true", "invoiceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceBetasByInvoiceNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoiceBeta = invoiceBetaRepository.saveAndFlush(invoiceBeta);

        // Get all the invoiceBetaList where invoiceNumber contains
        defaultInvoiceBetaFiltering("invoiceNumber.contains=" + DEFAULT_INVOICE_NUMBER, "invoiceNumber.contains=" + UPDATED_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    void getAllInvoiceBetasByInvoiceNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoiceBeta = invoiceBetaRepository.saveAndFlush(invoiceBeta);

        // Get all the invoiceBetaList where invoiceNumber does not contain
        defaultInvoiceBetaFiltering(
            "invoiceNumber.doesNotContain=" + UPDATED_INVOICE_NUMBER,
            "invoiceNumber.doesNotContain=" + DEFAULT_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllInvoiceBetasByIssueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceBeta = invoiceBetaRepository.saveAndFlush(invoiceBeta);

        // Get all the invoiceBetaList where issueDate equals to
        defaultInvoiceBetaFiltering("issueDate.equals=" + DEFAULT_ISSUE_DATE, "issueDate.equals=" + UPDATED_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllInvoiceBetasByIssueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceBeta = invoiceBetaRepository.saveAndFlush(invoiceBeta);

        // Get all the invoiceBetaList where issueDate in
        defaultInvoiceBetaFiltering("issueDate.in=" + DEFAULT_ISSUE_DATE + "," + UPDATED_ISSUE_DATE, "issueDate.in=" + UPDATED_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllInvoiceBetasByIssueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceBeta = invoiceBetaRepository.saveAndFlush(invoiceBeta);

        // Get all the invoiceBetaList where issueDate is not null
        defaultInvoiceBetaFiltering("issueDate.specified=true", "issueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceBetasByDueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceBeta = invoiceBetaRepository.saveAndFlush(invoiceBeta);

        // Get all the invoiceBetaList where dueDate equals to
        defaultInvoiceBetaFiltering("dueDate.equals=" + DEFAULT_DUE_DATE, "dueDate.equals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllInvoiceBetasByDueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceBeta = invoiceBetaRepository.saveAndFlush(invoiceBeta);

        // Get all the invoiceBetaList where dueDate in
        defaultInvoiceBetaFiltering("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE, "dueDate.in=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllInvoiceBetasByDueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceBeta = invoiceBetaRepository.saveAndFlush(invoiceBeta);

        // Get all the invoiceBetaList where dueDate is not null
        defaultInvoiceBetaFiltering("dueDate.specified=true", "dueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceBetasByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceBeta = invoiceBetaRepository.saveAndFlush(invoiceBeta);

        // Get all the invoiceBetaList where amount equals to
        defaultInvoiceBetaFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceBetasByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceBeta = invoiceBetaRepository.saveAndFlush(invoiceBeta);

        // Get all the invoiceBetaList where amount in
        defaultInvoiceBetaFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceBetasByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceBeta = invoiceBetaRepository.saveAndFlush(invoiceBeta);

        // Get all the invoiceBetaList where amount is not null
        defaultInvoiceBetaFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceBetasByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceBeta = invoiceBetaRepository.saveAndFlush(invoiceBeta);

        // Get all the invoiceBetaList where amount is greater than or equal to
        defaultInvoiceBetaFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceBetasByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceBeta = invoiceBetaRepository.saveAndFlush(invoiceBeta);

        // Get all the invoiceBetaList where amount is less than or equal to
        defaultInvoiceBetaFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceBetasByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInvoiceBeta = invoiceBetaRepository.saveAndFlush(invoiceBeta);

        // Get all the invoiceBetaList where amount is less than
        defaultInvoiceBetaFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceBetasByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInvoiceBeta = invoiceBetaRepository.saveAndFlush(invoiceBeta);

        // Get all the invoiceBetaList where amount is greater than
        defaultInvoiceBetaFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceBetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            invoiceBetaRepository.saveAndFlush(invoiceBeta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        invoiceBeta.setTenant(tenant);
        invoiceBetaRepository.saveAndFlush(invoiceBeta);
        Long tenantId = tenant.getId();
        // Get all the invoiceBetaList where tenant equals to tenantId
        defaultInvoiceBetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the invoiceBetaList where tenant equals to (tenantId + 1)
        defaultInvoiceBetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultInvoiceBetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInvoiceBetaShouldBeFound(shouldBeFound);
        defaultInvoiceBetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInvoiceBetaShouldBeFound(String filter) throws Exception {
        restInvoiceBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));

        // Check, that the count call also returns 1
        restInvoiceBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInvoiceBetaShouldNotBeFound(String filter) throws Exception {
        restInvoiceBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInvoiceBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInvoiceBeta() throws Exception {
        // Get the invoiceBeta
        restInvoiceBetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInvoiceBeta() throws Exception {
        // Initialize the database
        insertedInvoiceBeta = invoiceBetaRepository.saveAndFlush(invoiceBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceBeta
        InvoiceBeta updatedInvoiceBeta = invoiceBetaRepository.findById(invoiceBeta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInvoiceBeta are not directly saved in db
        em.detach(updatedInvoiceBeta);
        updatedInvoiceBeta
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);
        InvoiceBetaDTO invoiceBetaDTO = invoiceBetaMapper.toDto(updatedInvoiceBeta);

        restInvoiceBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceBetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceBetaDTO))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInvoiceBetaToMatchAllProperties(updatedInvoiceBeta);
    }

    @Test
    @Transactional
    void putNonExistingInvoiceBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceBeta.setId(longCount.incrementAndGet());

        // Create the InvoiceBeta
        InvoiceBetaDTO invoiceBetaDTO = invoiceBetaMapper.toDto(invoiceBeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceBetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoiceBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceBeta.setId(longCount.incrementAndGet());

        // Create the InvoiceBeta
        InvoiceBetaDTO invoiceBetaDTO = invoiceBetaMapper.toDto(invoiceBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoiceBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceBeta.setId(longCount.incrementAndGet());

        // Create the InvoiceBeta
        InvoiceBetaDTO invoiceBetaDTO = invoiceBetaMapper.toDto(invoiceBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceBetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceBetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceBetaWithPatch() throws Exception {
        // Initialize the database
        insertedInvoiceBeta = invoiceBetaRepository.saveAndFlush(invoiceBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceBeta using partial update
        InvoiceBeta partialUpdatedInvoiceBeta = new InvoiceBeta();
        partialUpdatedInvoiceBeta.setId(invoiceBeta.getId());

        partialUpdatedInvoiceBeta.invoiceNumber(UPDATED_INVOICE_NUMBER).issueDate(UPDATED_ISSUE_DATE).dueDate(UPDATED_DUE_DATE);

        restInvoiceBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoiceBeta))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceBetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInvoiceBeta, invoiceBeta),
            getPersistedInvoiceBeta(invoiceBeta)
        );
    }

    @Test
    @Transactional
    void fullUpdateInvoiceBetaWithPatch() throws Exception {
        // Initialize the database
        insertedInvoiceBeta = invoiceBetaRepository.saveAndFlush(invoiceBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceBeta using partial update
        InvoiceBeta partialUpdatedInvoiceBeta = new InvoiceBeta();
        partialUpdatedInvoiceBeta.setId(invoiceBeta.getId());

        partialUpdatedInvoiceBeta
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);

        restInvoiceBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoiceBeta))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceBetaUpdatableFieldsEquals(partialUpdatedInvoiceBeta, getPersistedInvoiceBeta(partialUpdatedInvoiceBeta));
    }

    @Test
    @Transactional
    void patchNonExistingInvoiceBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceBeta.setId(longCount.incrementAndGet());

        // Create the InvoiceBeta
        InvoiceBetaDTO invoiceBetaDTO = invoiceBetaMapper.toDto(invoiceBeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoiceBetaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoiceBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoiceBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceBeta.setId(longCount.incrementAndGet());

        // Create the InvoiceBeta
        InvoiceBetaDTO invoiceBetaDTO = invoiceBetaMapper.toDto(invoiceBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoiceBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoiceBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceBeta.setId(longCount.incrementAndGet());

        // Create the InvoiceBeta
        InvoiceBetaDTO invoiceBetaDTO = invoiceBetaMapper.toDto(invoiceBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceBetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(invoiceBetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvoiceBeta() throws Exception {
        // Initialize the database
        insertedInvoiceBeta = invoiceBetaRepository.saveAndFlush(invoiceBeta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the invoiceBeta
        restInvoiceBetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoiceBeta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return invoiceBetaRepository.count();
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

    protected InvoiceBeta getPersistedInvoiceBeta(InvoiceBeta invoiceBeta) {
        return invoiceBetaRepository.findById(invoiceBeta.getId()).orElseThrow();
    }

    protected void assertPersistedInvoiceBetaToMatchAllProperties(InvoiceBeta expectedInvoiceBeta) {
        assertInvoiceBetaAllPropertiesEquals(expectedInvoiceBeta, getPersistedInvoiceBeta(expectedInvoiceBeta));
    }

    protected void assertPersistedInvoiceBetaToMatchUpdatableProperties(InvoiceBeta expectedInvoiceBeta) {
        assertInvoiceBetaAllUpdatablePropertiesEquals(expectedInvoiceBeta, getPersistedInvoiceBeta(expectedInvoiceBeta));
    }
}
