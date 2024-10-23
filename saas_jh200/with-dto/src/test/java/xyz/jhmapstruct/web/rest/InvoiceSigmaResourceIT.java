package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.InvoiceSigmaAsserts.*;
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
import xyz.jhmapstruct.domain.InvoiceSigma;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.repository.InvoiceSigmaRepository;
import xyz.jhmapstruct.service.dto.InvoiceSigmaDTO;
import xyz.jhmapstruct.service.mapper.InvoiceSigmaMapper;

/**
 * Integration tests for the {@link InvoiceSigmaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InvoiceSigmaResourceIT {

    private static final String DEFAULT_INVOICE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_ISSUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ISSUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/invoice-sigmas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InvoiceSigmaRepository invoiceSigmaRepository;

    @Autowired
    private InvoiceSigmaMapper invoiceSigmaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceSigmaMockMvc;

    private InvoiceSigma invoiceSigma;

    private InvoiceSigma insertedInvoiceSigma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceSigma createEntity() {
        return new InvoiceSigma()
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
    public static InvoiceSigma createUpdatedEntity() {
        return new InvoiceSigma()
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);
    }

    @BeforeEach
    public void initTest() {
        invoiceSigma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedInvoiceSigma != null) {
            invoiceSigmaRepository.delete(insertedInvoiceSigma);
            insertedInvoiceSigma = null;
        }
    }

    @Test
    @Transactional
    void createInvoiceSigma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InvoiceSigma
        InvoiceSigmaDTO invoiceSigmaDTO = invoiceSigmaMapper.toDto(invoiceSigma);
        var returnedInvoiceSigmaDTO = om.readValue(
            restInvoiceSigmaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceSigmaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InvoiceSigmaDTO.class
        );

        // Validate the InvoiceSigma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedInvoiceSigma = invoiceSigmaMapper.toEntity(returnedInvoiceSigmaDTO);
        assertInvoiceSigmaUpdatableFieldsEquals(returnedInvoiceSigma, getPersistedInvoiceSigma(returnedInvoiceSigma));

        insertedInvoiceSigma = returnedInvoiceSigma;
    }

    @Test
    @Transactional
    void createInvoiceSigmaWithExistingId() throws Exception {
        // Create the InvoiceSigma with an existing ID
        invoiceSigma.setId(1L);
        InvoiceSigmaDTO invoiceSigmaDTO = invoiceSigmaMapper.toDto(invoiceSigma);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceSigmaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkInvoiceNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceSigma.setInvoiceNumber(null);

        // Create the InvoiceSigma, which fails.
        InvoiceSigmaDTO invoiceSigmaDTO = invoiceSigmaMapper.toDto(invoiceSigma);

        restInvoiceSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceSigmaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIssueDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceSigma.setIssueDate(null);

        // Create the InvoiceSigma, which fails.
        InvoiceSigmaDTO invoiceSigmaDTO = invoiceSigmaMapper.toDto(invoiceSigma);

        restInvoiceSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceSigmaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceSigma.setAmount(null);

        // Create the InvoiceSigma, which fails.
        InvoiceSigmaDTO invoiceSigmaDTO = invoiceSigmaMapper.toDto(invoiceSigma);

        restInvoiceSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceSigmaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInvoiceSigmas() throws Exception {
        // Initialize the database
        insertedInvoiceSigma = invoiceSigmaRepository.saveAndFlush(invoiceSigma);

        // Get all the invoiceSigmaList
        restInvoiceSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceSigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));
    }

    @Test
    @Transactional
    void getInvoiceSigma() throws Exception {
        // Initialize the database
        insertedInvoiceSigma = invoiceSigmaRepository.saveAndFlush(invoiceSigma);

        // Get the invoiceSigma
        restInvoiceSigmaMockMvc
            .perform(get(ENTITY_API_URL_ID, invoiceSigma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceSigma.getId().intValue()))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER))
            .andExpect(jsonPath("$.issueDate").value(DEFAULT_ISSUE_DATE.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)));
    }

    @Test
    @Transactional
    void getInvoiceSigmasByIdFiltering() throws Exception {
        // Initialize the database
        insertedInvoiceSigma = invoiceSigmaRepository.saveAndFlush(invoiceSigma);

        Long id = invoiceSigma.getId();

        defaultInvoiceSigmaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInvoiceSigmaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInvoiceSigmaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInvoiceSigmasByInvoiceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceSigma = invoiceSigmaRepository.saveAndFlush(invoiceSigma);

        // Get all the invoiceSigmaList where invoiceNumber equals to
        defaultInvoiceSigmaFiltering("invoiceNumber.equals=" + DEFAULT_INVOICE_NUMBER, "invoiceNumber.equals=" + UPDATED_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    void getAllInvoiceSigmasByInvoiceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceSigma = invoiceSigmaRepository.saveAndFlush(invoiceSigma);

        // Get all the invoiceSigmaList where invoiceNumber in
        defaultInvoiceSigmaFiltering(
            "invoiceNumber.in=" + DEFAULT_INVOICE_NUMBER + "," + UPDATED_INVOICE_NUMBER,
            "invoiceNumber.in=" + UPDATED_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllInvoiceSigmasByInvoiceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceSigma = invoiceSigmaRepository.saveAndFlush(invoiceSigma);

        // Get all the invoiceSigmaList where invoiceNumber is not null
        defaultInvoiceSigmaFiltering("invoiceNumber.specified=true", "invoiceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceSigmasByInvoiceNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoiceSigma = invoiceSigmaRepository.saveAndFlush(invoiceSigma);

        // Get all the invoiceSigmaList where invoiceNumber contains
        defaultInvoiceSigmaFiltering(
            "invoiceNumber.contains=" + DEFAULT_INVOICE_NUMBER,
            "invoiceNumber.contains=" + UPDATED_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllInvoiceSigmasByInvoiceNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoiceSigma = invoiceSigmaRepository.saveAndFlush(invoiceSigma);

        // Get all the invoiceSigmaList where invoiceNumber does not contain
        defaultInvoiceSigmaFiltering(
            "invoiceNumber.doesNotContain=" + UPDATED_INVOICE_NUMBER,
            "invoiceNumber.doesNotContain=" + DEFAULT_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllInvoiceSigmasByIssueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceSigma = invoiceSigmaRepository.saveAndFlush(invoiceSigma);

        // Get all the invoiceSigmaList where issueDate equals to
        defaultInvoiceSigmaFiltering("issueDate.equals=" + DEFAULT_ISSUE_DATE, "issueDate.equals=" + UPDATED_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllInvoiceSigmasByIssueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceSigma = invoiceSigmaRepository.saveAndFlush(invoiceSigma);

        // Get all the invoiceSigmaList where issueDate in
        defaultInvoiceSigmaFiltering("issueDate.in=" + DEFAULT_ISSUE_DATE + "," + UPDATED_ISSUE_DATE, "issueDate.in=" + UPDATED_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllInvoiceSigmasByIssueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceSigma = invoiceSigmaRepository.saveAndFlush(invoiceSigma);

        // Get all the invoiceSigmaList where issueDate is not null
        defaultInvoiceSigmaFiltering("issueDate.specified=true", "issueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceSigmasByDueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceSigma = invoiceSigmaRepository.saveAndFlush(invoiceSigma);

        // Get all the invoiceSigmaList where dueDate equals to
        defaultInvoiceSigmaFiltering("dueDate.equals=" + DEFAULT_DUE_DATE, "dueDate.equals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllInvoiceSigmasByDueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceSigma = invoiceSigmaRepository.saveAndFlush(invoiceSigma);

        // Get all the invoiceSigmaList where dueDate in
        defaultInvoiceSigmaFiltering("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE, "dueDate.in=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllInvoiceSigmasByDueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceSigma = invoiceSigmaRepository.saveAndFlush(invoiceSigma);

        // Get all the invoiceSigmaList where dueDate is not null
        defaultInvoiceSigmaFiltering("dueDate.specified=true", "dueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceSigmasByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceSigma = invoiceSigmaRepository.saveAndFlush(invoiceSigma);

        // Get all the invoiceSigmaList where amount equals to
        defaultInvoiceSigmaFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceSigmasByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceSigma = invoiceSigmaRepository.saveAndFlush(invoiceSigma);

        // Get all the invoiceSigmaList where amount in
        defaultInvoiceSigmaFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceSigmasByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceSigma = invoiceSigmaRepository.saveAndFlush(invoiceSigma);

        // Get all the invoiceSigmaList where amount is not null
        defaultInvoiceSigmaFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceSigmasByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceSigma = invoiceSigmaRepository.saveAndFlush(invoiceSigma);

        // Get all the invoiceSigmaList where amount is greater than or equal to
        defaultInvoiceSigmaFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceSigmasByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceSigma = invoiceSigmaRepository.saveAndFlush(invoiceSigma);

        // Get all the invoiceSigmaList where amount is less than or equal to
        defaultInvoiceSigmaFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceSigmasByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInvoiceSigma = invoiceSigmaRepository.saveAndFlush(invoiceSigma);

        // Get all the invoiceSigmaList where amount is less than
        defaultInvoiceSigmaFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceSigmasByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInvoiceSigma = invoiceSigmaRepository.saveAndFlush(invoiceSigma);

        // Get all the invoiceSigmaList where amount is greater than
        defaultInvoiceSigmaFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceSigmasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            invoiceSigmaRepository.saveAndFlush(invoiceSigma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        invoiceSigma.setTenant(tenant);
        invoiceSigmaRepository.saveAndFlush(invoiceSigma);
        Long tenantId = tenant.getId();
        // Get all the invoiceSigmaList where tenant equals to tenantId
        defaultInvoiceSigmaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the invoiceSigmaList where tenant equals to (tenantId + 1)
        defaultInvoiceSigmaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultInvoiceSigmaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInvoiceSigmaShouldBeFound(shouldBeFound);
        defaultInvoiceSigmaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInvoiceSigmaShouldBeFound(String filter) throws Exception {
        restInvoiceSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceSigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));

        // Check, that the count call also returns 1
        restInvoiceSigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInvoiceSigmaShouldNotBeFound(String filter) throws Exception {
        restInvoiceSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInvoiceSigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInvoiceSigma() throws Exception {
        // Get the invoiceSigma
        restInvoiceSigmaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInvoiceSigma() throws Exception {
        // Initialize the database
        insertedInvoiceSigma = invoiceSigmaRepository.saveAndFlush(invoiceSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceSigma
        InvoiceSigma updatedInvoiceSigma = invoiceSigmaRepository.findById(invoiceSigma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInvoiceSigma are not directly saved in db
        em.detach(updatedInvoiceSigma);
        updatedInvoiceSigma
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);
        InvoiceSigmaDTO invoiceSigmaDTO = invoiceSigmaMapper.toDto(updatedInvoiceSigma);

        restInvoiceSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceSigmaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceSigmaDTO))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInvoiceSigmaToMatchAllProperties(updatedInvoiceSigma);
    }

    @Test
    @Transactional
    void putNonExistingInvoiceSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceSigma.setId(longCount.incrementAndGet());

        // Create the InvoiceSigma
        InvoiceSigmaDTO invoiceSigmaDTO = invoiceSigmaMapper.toDto(invoiceSigma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceSigmaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoiceSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceSigma.setId(longCount.incrementAndGet());

        // Create the InvoiceSigma
        InvoiceSigmaDTO invoiceSigmaDTO = invoiceSigmaMapper.toDto(invoiceSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoiceSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceSigma.setId(longCount.incrementAndGet());

        // Create the InvoiceSigma
        InvoiceSigmaDTO invoiceSigmaDTO = invoiceSigmaMapper.toDto(invoiceSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceSigmaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceSigmaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceSigmaWithPatch() throws Exception {
        // Initialize the database
        insertedInvoiceSigma = invoiceSigmaRepository.saveAndFlush(invoiceSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceSigma using partial update
        InvoiceSigma partialUpdatedInvoiceSigma = new InvoiceSigma();
        partialUpdatedInvoiceSigma.setId(invoiceSigma.getId());

        partialUpdatedInvoiceSigma.issueDate(UPDATED_ISSUE_DATE).dueDate(UPDATED_DUE_DATE);

        restInvoiceSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoiceSigma))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceSigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceSigmaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInvoiceSigma, invoiceSigma),
            getPersistedInvoiceSigma(invoiceSigma)
        );
    }

    @Test
    @Transactional
    void fullUpdateInvoiceSigmaWithPatch() throws Exception {
        // Initialize the database
        insertedInvoiceSigma = invoiceSigmaRepository.saveAndFlush(invoiceSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceSigma using partial update
        InvoiceSigma partialUpdatedInvoiceSigma = new InvoiceSigma();
        partialUpdatedInvoiceSigma.setId(invoiceSigma.getId());

        partialUpdatedInvoiceSigma
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);

        restInvoiceSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoiceSigma))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceSigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceSigmaUpdatableFieldsEquals(partialUpdatedInvoiceSigma, getPersistedInvoiceSigma(partialUpdatedInvoiceSigma));
    }

    @Test
    @Transactional
    void patchNonExistingInvoiceSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceSigma.setId(longCount.incrementAndGet());

        // Create the InvoiceSigma
        InvoiceSigmaDTO invoiceSigmaDTO = invoiceSigmaMapper.toDto(invoiceSigma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoiceSigmaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoiceSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoiceSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceSigma.setId(longCount.incrementAndGet());

        // Create the InvoiceSigma
        InvoiceSigmaDTO invoiceSigmaDTO = invoiceSigmaMapper.toDto(invoiceSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoiceSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoiceSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceSigma.setId(longCount.incrementAndGet());

        // Create the InvoiceSigma
        InvoiceSigmaDTO invoiceSigmaDTO = invoiceSigmaMapper.toDto(invoiceSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceSigmaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(invoiceSigmaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvoiceSigma() throws Exception {
        // Initialize the database
        insertedInvoiceSigma = invoiceSigmaRepository.saveAndFlush(invoiceSigma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the invoiceSigma
        restInvoiceSigmaMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoiceSigma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return invoiceSigmaRepository.count();
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

    protected InvoiceSigma getPersistedInvoiceSigma(InvoiceSigma invoiceSigma) {
        return invoiceSigmaRepository.findById(invoiceSigma.getId()).orElseThrow();
    }

    protected void assertPersistedInvoiceSigmaToMatchAllProperties(InvoiceSigma expectedInvoiceSigma) {
        assertInvoiceSigmaAllPropertiesEquals(expectedInvoiceSigma, getPersistedInvoiceSigma(expectedInvoiceSigma));
    }

    protected void assertPersistedInvoiceSigmaToMatchUpdatableProperties(InvoiceSigma expectedInvoiceSigma) {
        assertInvoiceSigmaAllUpdatablePropertiesEquals(expectedInvoiceSigma, getPersistedInvoiceSigma(expectedInvoiceSigma));
    }
}
