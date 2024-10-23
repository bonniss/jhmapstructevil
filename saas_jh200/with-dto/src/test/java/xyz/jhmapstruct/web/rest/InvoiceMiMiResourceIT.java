package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.InvoiceMiMiAsserts.*;
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
import xyz.jhmapstruct.domain.InvoiceMiMi;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.repository.InvoiceMiMiRepository;
import xyz.jhmapstruct.service.dto.InvoiceMiMiDTO;
import xyz.jhmapstruct.service.mapper.InvoiceMiMiMapper;

/**
 * Integration tests for the {@link InvoiceMiMiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InvoiceMiMiResourceIT {

    private static final String DEFAULT_INVOICE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_ISSUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ISSUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/invoice-mi-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InvoiceMiMiRepository invoiceMiMiRepository;

    @Autowired
    private InvoiceMiMiMapper invoiceMiMiMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceMiMiMockMvc;

    private InvoiceMiMi invoiceMiMi;

    private InvoiceMiMi insertedInvoiceMiMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceMiMi createEntity() {
        return new InvoiceMiMi()
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
    public static InvoiceMiMi createUpdatedEntity() {
        return new InvoiceMiMi()
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);
    }

    @BeforeEach
    public void initTest() {
        invoiceMiMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedInvoiceMiMi != null) {
            invoiceMiMiRepository.delete(insertedInvoiceMiMi);
            insertedInvoiceMiMi = null;
        }
    }

    @Test
    @Transactional
    void createInvoiceMiMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InvoiceMiMi
        InvoiceMiMiDTO invoiceMiMiDTO = invoiceMiMiMapper.toDto(invoiceMiMi);
        var returnedInvoiceMiMiDTO = om.readValue(
            restInvoiceMiMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceMiMiDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InvoiceMiMiDTO.class
        );

        // Validate the InvoiceMiMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedInvoiceMiMi = invoiceMiMiMapper.toEntity(returnedInvoiceMiMiDTO);
        assertInvoiceMiMiUpdatableFieldsEquals(returnedInvoiceMiMi, getPersistedInvoiceMiMi(returnedInvoiceMiMi));

        insertedInvoiceMiMi = returnedInvoiceMiMi;
    }

    @Test
    @Transactional
    void createInvoiceMiMiWithExistingId() throws Exception {
        // Create the InvoiceMiMi with an existing ID
        invoiceMiMi.setId(1L);
        InvoiceMiMiDTO invoiceMiMiDTO = invoiceMiMiMapper.toDto(invoiceMiMi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceMiMiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkInvoiceNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceMiMi.setInvoiceNumber(null);

        // Create the InvoiceMiMi, which fails.
        InvoiceMiMiDTO invoiceMiMiDTO = invoiceMiMiMapper.toDto(invoiceMiMi);

        restInvoiceMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceMiMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIssueDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceMiMi.setIssueDate(null);

        // Create the InvoiceMiMi, which fails.
        InvoiceMiMiDTO invoiceMiMiDTO = invoiceMiMiMapper.toDto(invoiceMiMi);

        restInvoiceMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceMiMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceMiMi.setAmount(null);

        // Create the InvoiceMiMi, which fails.
        InvoiceMiMiDTO invoiceMiMiDTO = invoiceMiMiMapper.toDto(invoiceMiMi);

        restInvoiceMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceMiMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInvoiceMiMis() throws Exception {
        // Initialize the database
        insertedInvoiceMiMi = invoiceMiMiRepository.saveAndFlush(invoiceMiMi);

        // Get all the invoiceMiMiList
        restInvoiceMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceMiMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));
    }

    @Test
    @Transactional
    void getInvoiceMiMi() throws Exception {
        // Initialize the database
        insertedInvoiceMiMi = invoiceMiMiRepository.saveAndFlush(invoiceMiMi);

        // Get the invoiceMiMi
        restInvoiceMiMiMockMvc
            .perform(get(ENTITY_API_URL_ID, invoiceMiMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceMiMi.getId().intValue()))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER))
            .andExpect(jsonPath("$.issueDate").value(DEFAULT_ISSUE_DATE.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)));
    }

    @Test
    @Transactional
    void getInvoiceMiMisByIdFiltering() throws Exception {
        // Initialize the database
        insertedInvoiceMiMi = invoiceMiMiRepository.saveAndFlush(invoiceMiMi);

        Long id = invoiceMiMi.getId();

        defaultInvoiceMiMiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInvoiceMiMiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInvoiceMiMiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInvoiceMiMisByInvoiceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceMiMi = invoiceMiMiRepository.saveAndFlush(invoiceMiMi);

        // Get all the invoiceMiMiList where invoiceNumber equals to
        defaultInvoiceMiMiFiltering("invoiceNumber.equals=" + DEFAULT_INVOICE_NUMBER, "invoiceNumber.equals=" + UPDATED_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    void getAllInvoiceMiMisByInvoiceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceMiMi = invoiceMiMiRepository.saveAndFlush(invoiceMiMi);

        // Get all the invoiceMiMiList where invoiceNumber in
        defaultInvoiceMiMiFiltering(
            "invoiceNumber.in=" + DEFAULT_INVOICE_NUMBER + "," + UPDATED_INVOICE_NUMBER,
            "invoiceNumber.in=" + UPDATED_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllInvoiceMiMisByInvoiceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceMiMi = invoiceMiMiRepository.saveAndFlush(invoiceMiMi);

        // Get all the invoiceMiMiList where invoiceNumber is not null
        defaultInvoiceMiMiFiltering("invoiceNumber.specified=true", "invoiceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceMiMisByInvoiceNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoiceMiMi = invoiceMiMiRepository.saveAndFlush(invoiceMiMi);

        // Get all the invoiceMiMiList where invoiceNumber contains
        defaultInvoiceMiMiFiltering("invoiceNumber.contains=" + DEFAULT_INVOICE_NUMBER, "invoiceNumber.contains=" + UPDATED_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    void getAllInvoiceMiMisByInvoiceNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoiceMiMi = invoiceMiMiRepository.saveAndFlush(invoiceMiMi);

        // Get all the invoiceMiMiList where invoiceNumber does not contain
        defaultInvoiceMiMiFiltering(
            "invoiceNumber.doesNotContain=" + UPDATED_INVOICE_NUMBER,
            "invoiceNumber.doesNotContain=" + DEFAULT_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllInvoiceMiMisByIssueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceMiMi = invoiceMiMiRepository.saveAndFlush(invoiceMiMi);

        // Get all the invoiceMiMiList where issueDate equals to
        defaultInvoiceMiMiFiltering("issueDate.equals=" + DEFAULT_ISSUE_DATE, "issueDate.equals=" + UPDATED_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllInvoiceMiMisByIssueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceMiMi = invoiceMiMiRepository.saveAndFlush(invoiceMiMi);

        // Get all the invoiceMiMiList where issueDate in
        defaultInvoiceMiMiFiltering("issueDate.in=" + DEFAULT_ISSUE_DATE + "," + UPDATED_ISSUE_DATE, "issueDate.in=" + UPDATED_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllInvoiceMiMisByIssueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceMiMi = invoiceMiMiRepository.saveAndFlush(invoiceMiMi);

        // Get all the invoiceMiMiList where issueDate is not null
        defaultInvoiceMiMiFiltering("issueDate.specified=true", "issueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceMiMisByDueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceMiMi = invoiceMiMiRepository.saveAndFlush(invoiceMiMi);

        // Get all the invoiceMiMiList where dueDate equals to
        defaultInvoiceMiMiFiltering("dueDate.equals=" + DEFAULT_DUE_DATE, "dueDate.equals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllInvoiceMiMisByDueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceMiMi = invoiceMiMiRepository.saveAndFlush(invoiceMiMi);

        // Get all the invoiceMiMiList where dueDate in
        defaultInvoiceMiMiFiltering("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE, "dueDate.in=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllInvoiceMiMisByDueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceMiMi = invoiceMiMiRepository.saveAndFlush(invoiceMiMi);

        // Get all the invoiceMiMiList where dueDate is not null
        defaultInvoiceMiMiFiltering("dueDate.specified=true", "dueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceMiMisByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceMiMi = invoiceMiMiRepository.saveAndFlush(invoiceMiMi);

        // Get all the invoiceMiMiList where amount equals to
        defaultInvoiceMiMiFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceMiMisByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceMiMi = invoiceMiMiRepository.saveAndFlush(invoiceMiMi);

        // Get all the invoiceMiMiList where amount in
        defaultInvoiceMiMiFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceMiMisByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceMiMi = invoiceMiMiRepository.saveAndFlush(invoiceMiMi);

        // Get all the invoiceMiMiList where amount is not null
        defaultInvoiceMiMiFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceMiMisByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceMiMi = invoiceMiMiRepository.saveAndFlush(invoiceMiMi);

        // Get all the invoiceMiMiList where amount is greater than or equal to
        defaultInvoiceMiMiFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceMiMisByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceMiMi = invoiceMiMiRepository.saveAndFlush(invoiceMiMi);

        // Get all the invoiceMiMiList where amount is less than or equal to
        defaultInvoiceMiMiFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceMiMisByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInvoiceMiMi = invoiceMiMiRepository.saveAndFlush(invoiceMiMi);

        // Get all the invoiceMiMiList where amount is less than
        defaultInvoiceMiMiFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceMiMisByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInvoiceMiMi = invoiceMiMiRepository.saveAndFlush(invoiceMiMi);

        // Get all the invoiceMiMiList where amount is greater than
        defaultInvoiceMiMiFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceMiMisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            invoiceMiMiRepository.saveAndFlush(invoiceMiMi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        invoiceMiMi.setTenant(tenant);
        invoiceMiMiRepository.saveAndFlush(invoiceMiMi);
        Long tenantId = tenant.getId();
        // Get all the invoiceMiMiList where tenant equals to tenantId
        defaultInvoiceMiMiShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the invoiceMiMiList where tenant equals to (tenantId + 1)
        defaultInvoiceMiMiShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultInvoiceMiMiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInvoiceMiMiShouldBeFound(shouldBeFound);
        defaultInvoiceMiMiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInvoiceMiMiShouldBeFound(String filter) throws Exception {
        restInvoiceMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceMiMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));

        // Check, that the count call also returns 1
        restInvoiceMiMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInvoiceMiMiShouldNotBeFound(String filter) throws Exception {
        restInvoiceMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInvoiceMiMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInvoiceMiMi() throws Exception {
        // Get the invoiceMiMi
        restInvoiceMiMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInvoiceMiMi() throws Exception {
        // Initialize the database
        insertedInvoiceMiMi = invoiceMiMiRepository.saveAndFlush(invoiceMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceMiMi
        InvoiceMiMi updatedInvoiceMiMi = invoiceMiMiRepository.findById(invoiceMiMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInvoiceMiMi are not directly saved in db
        em.detach(updatedInvoiceMiMi);
        updatedInvoiceMiMi
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);
        InvoiceMiMiDTO invoiceMiMiDTO = invoiceMiMiMapper.toDto(updatedInvoiceMiMi);

        restInvoiceMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceMiMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceMiMiDTO))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInvoiceMiMiToMatchAllProperties(updatedInvoiceMiMi);
    }

    @Test
    @Transactional
    void putNonExistingInvoiceMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceMiMi.setId(longCount.incrementAndGet());

        // Create the InvoiceMiMi
        InvoiceMiMiDTO invoiceMiMiDTO = invoiceMiMiMapper.toDto(invoiceMiMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceMiMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoiceMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceMiMi.setId(longCount.incrementAndGet());

        // Create the InvoiceMiMi
        InvoiceMiMiDTO invoiceMiMiDTO = invoiceMiMiMapper.toDto(invoiceMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoiceMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceMiMi.setId(longCount.incrementAndGet());

        // Create the InvoiceMiMi
        InvoiceMiMiDTO invoiceMiMiDTO = invoiceMiMiMapper.toDto(invoiceMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMiMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceMiMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedInvoiceMiMi = invoiceMiMiRepository.saveAndFlush(invoiceMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceMiMi using partial update
        InvoiceMiMi partialUpdatedInvoiceMiMi = new InvoiceMiMi();
        partialUpdatedInvoiceMiMi.setId(invoiceMiMi.getId());

        partialUpdatedInvoiceMiMi.issueDate(UPDATED_ISSUE_DATE);

        restInvoiceMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoiceMiMi))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceMiMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInvoiceMiMi, invoiceMiMi),
            getPersistedInvoiceMiMi(invoiceMiMi)
        );
    }

    @Test
    @Transactional
    void fullUpdateInvoiceMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedInvoiceMiMi = invoiceMiMiRepository.saveAndFlush(invoiceMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceMiMi using partial update
        InvoiceMiMi partialUpdatedInvoiceMiMi = new InvoiceMiMi();
        partialUpdatedInvoiceMiMi.setId(invoiceMiMi.getId());

        partialUpdatedInvoiceMiMi
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);

        restInvoiceMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoiceMiMi))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceMiMiUpdatableFieldsEquals(partialUpdatedInvoiceMiMi, getPersistedInvoiceMiMi(partialUpdatedInvoiceMiMi));
    }

    @Test
    @Transactional
    void patchNonExistingInvoiceMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceMiMi.setId(longCount.incrementAndGet());

        // Create the InvoiceMiMi
        InvoiceMiMiDTO invoiceMiMiDTO = invoiceMiMiMapper.toDto(invoiceMiMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoiceMiMiDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoiceMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoiceMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceMiMi.setId(longCount.incrementAndGet());

        // Create the InvoiceMiMi
        InvoiceMiMiDTO invoiceMiMiDTO = invoiceMiMiMapper.toDto(invoiceMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoiceMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoiceMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceMiMi.setId(longCount.incrementAndGet());

        // Create the InvoiceMiMi
        InvoiceMiMiDTO invoiceMiMiDTO = invoiceMiMiMapper.toDto(invoiceMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMiMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(invoiceMiMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvoiceMiMi() throws Exception {
        // Initialize the database
        insertedInvoiceMiMi = invoiceMiMiRepository.saveAndFlush(invoiceMiMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the invoiceMiMi
        restInvoiceMiMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoiceMiMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return invoiceMiMiRepository.count();
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

    protected InvoiceMiMi getPersistedInvoiceMiMi(InvoiceMiMi invoiceMiMi) {
        return invoiceMiMiRepository.findById(invoiceMiMi.getId()).orElseThrow();
    }

    protected void assertPersistedInvoiceMiMiToMatchAllProperties(InvoiceMiMi expectedInvoiceMiMi) {
        assertInvoiceMiMiAllPropertiesEquals(expectedInvoiceMiMi, getPersistedInvoiceMiMi(expectedInvoiceMiMi));
    }

    protected void assertPersistedInvoiceMiMiToMatchUpdatableProperties(InvoiceMiMi expectedInvoiceMiMi) {
        assertInvoiceMiMiAllUpdatablePropertiesEquals(expectedInvoiceMiMi, getPersistedInvoiceMiMi(expectedInvoiceMiMi));
    }
}
