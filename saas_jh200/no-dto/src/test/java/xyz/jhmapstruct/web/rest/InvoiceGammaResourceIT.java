package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.InvoiceGammaAsserts.*;
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
import xyz.jhmapstruct.domain.InvoiceGamma;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.repository.InvoiceGammaRepository;

/**
 * Integration tests for the {@link InvoiceGammaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InvoiceGammaResourceIT {

    private static final String DEFAULT_INVOICE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_ISSUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ISSUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/invoice-gammas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InvoiceGammaRepository invoiceGammaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceGammaMockMvc;

    private InvoiceGamma invoiceGamma;

    private InvoiceGamma insertedInvoiceGamma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceGamma createEntity() {
        return new InvoiceGamma()
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
    public static InvoiceGamma createUpdatedEntity() {
        return new InvoiceGamma()
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);
    }

    @BeforeEach
    public void initTest() {
        invoiceGamma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedInvoiceGamma != null) {
            invoiceGammaRepository.delete(insertedInvoiceGamma);
            insertedInvoiceGamma = null;
        }
    }

    @Test
    @Transactional
    void createInvoiceGamma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InvoiceGamma
        var returnedInvoiceGamma = om.readValue(
            restInvoiceGammaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceGamma)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InvoiceGamma.class
        );

        // Validate the InvoiceGamma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInvoiceGammaUpdatableFieldsEquals(returnedInvoiceGamma, getPersistedInvoiceGamma(returnedInvoiceGamma));

        insertedInvoiceGamma = returnedInvoiceGamma;
    }

    @Test
    @Transactional
    void createInvoiceGammaWithExistingId() throws Exception {
        // Create the InvoiceGamma with an existing ID
        invoiceGamma.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceGamma)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkInvoiceNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceGamma.setInvoiceNumber(null);

        // Create the InvoiceGamma, which fails.

        restInvoiceGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceGamma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIssueDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceGamma.setIssueDate(null);

        // Create the InvoiceGamma, which fails.

        restInvoiceGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceGamma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceGamma.setAmount(null);

        // Create the InvoiceGamma, which fails.

        restInvoiceGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceGamma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInvoiceGammas() throws Exception {
        // Initialize the database
        insertedInvoiceGamma = invoiceGammaRepository.saveAndFlush(invoiceGamma);

        // Get all the invoiceGammaList
        restInvoiceGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));
    }

    @Test
    @Transactional
    void getInvoiceGamma() throws Exception {
        // Initialize the database
        insertedInvoiceGamma = invoiceGammaRepository.saveAndFlush(invoiceGamma);

        // Get the invoiceGamma
        restInvoiceGammaMockMvc
            .perform(get(ENTITY_API_URL_ID, invoiceGamma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceGamma.getId().intValue()))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER))
            .andExpect(jsonPath("$.issueDate").value(DEFAULT_ISSUE_DATE.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)));
    }

    @Test
    @Transactional
    void getInvoiceGammasByIdFiltering() throws Exception {
        // Initialize the database
        insertedInvoiceGamma = invoiceGammaRepository.saveAndFlush(invoiceGamma);

        Long id = invoiceGamma.getId();

        defaultInvoiceGammaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInvoiceGammaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInvoiceGammaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInvoiceGammasByInvoiceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceGamma = invoiceGammaRepository.saveAndFlush(invoiceGamma);

        // Get all the invoiceGammaList where invoiceNumber equals to
        defaultInvoiceGammaFiltering("invoiceNumber.equals=" + DEFAULT_INVOICE_NUMBER, "invoiceNumber.equals=" + UPDATED_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    void getAllInvoiceGammasByInvoiceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceGamma = invoiceGammaRepository.saveAndFlush(invoiceGamma);

        // Get all the invoiceGammaList where invoiceNumber in
        defaultInvoiceGammaFiltering(
            "invoiceNumber.in=" + DEFAULT_INVOICE_NUMBER + "," + UPDATED_INVOICE_NUMBER,
            "invoiceNumber.in=" + UPDATED_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllInvoiceGammasByInvoiceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceGamma = invoiceGammaRepository.saveAndFlush(invoiceGamma);

        // Get all the invoiceGammaList where invoiceNumber is not null
        defaultInvoiceGammaFiltering("invoiceNumber.specified=true", "invoiceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceGammasByInvoiceNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoiceGamma = invoiceGammaRepository.saveAndFlush(invoiceGamma);

        // Get all the invoiceGammaList where invoiceNumber contains
        defaultInvoiceGammaFiltering(
            "invoiceNumber.contains=" + DEFAULT_INVOICE_NUMBER,
            "invoiceNumber.contains=" + UPDATED_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllInvoiceGammasByInvoiceNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoiceGamma = invoiceGammaRepository.saveAndFlush(invoiceGamma);

        // Get all the invoiceGammaList where invoiceNumber does not contain
        defaultInvoiceGammaFiltering(
            "invoiceNumber.doesNotContain=" + UPDATED_INVOICE_NUMBER,
            "invoiceNumber.doesNotContain=" + DEFAULT_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllInvoiceGammasByIssueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceGamma = invoiceGammaRepository.saveAndFlush(invoiceGamma);

        // Get all the invoiceGammaList where issueDate equals to
        defaultInvoiceGammaFiltering("issueDate.equals=" + DEFAULT_ISSUE_DATE, "issueDate.equals=" + UPDATED_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllInvoiceGammasByIssueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceGamma = invoiceGammaRepository.saveAndFlush(invoiceGamma);

        // Get all the invoiceGammaList where issueDate in
        defaultInvoiceGammaFiltering("issueDate.in=" + DEFAULT_ISSUE_DATE + "," + UPDATED_ISSUE_DATE, "issueDate.in=" + UPDATED_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllInvoiceGammasByIssueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceGamma = invoiceGammaRepository.saveAndFlush(invoiceGamma);

        // Get all the invoiceGammaList where issueDate is not null
        defaultInvoiceGammaFiltering("issueDate.specified=true", "issueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceGammasByDueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceGamma = invoiceGammaRepository.saveAndFlush(invoiceGamma);

        // Get all the invoiceGammaList where dueDate equals to
        defaultInvoiceGammaFiltering("dueDate.equals=" + DEFAULT_DUE_DATE, "dueDate.equals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllInvoiceGammasByDueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceGamma = invoiceGammaRepository.saveAndFlush(invoiceGamma);

        // Get all the invoiceGammaList where dueDate in
        defaultInvoiceGammaFiltering("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE, "dueDate.in=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllInvoiceGammasByDueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceGamma = invoiceGammaRepository.saveAndFlush(invoiceGamma);

        // Get all the invoiceGammaList where dueDate is not null
        defaultInvoiceGammaFiltering("dueDate.specified=true", "dueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceGammasByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceGamma = invoiceGammaRepository.saveAndFlush(invoiceGamma);

        // Get all the invoiceGammaList where amount equals to
        defaultInvoiceGammaFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceGammasByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceGamma = invoiceGammaRepository.saveAndFlush(invoiceGamma);

        // Get all the invoiceGammaList where amount in
        defaultInvoiceGammaFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceGammasByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceGamma = invoiceGammaRepository.saveAndFlush(invoiceGamma);

        // Get all the invoiceGammaList where amount is not null
        defaultInvoiceGammaFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceGammasByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceGamma = invoiceGammaRepository.saveAndFlush(invoiceGamma);

        // Get all the invoiceGammaList where amount is greater than or equal to
        defaultInvoiceGammaFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceGammasByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceGamma = invoiceGammaRepository.saveAndFlush(invoiceGamma);

        // Get all the invoiceGammaList where amount is less than or equal to
        defaultInvoiceGammaFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceGammasByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInvoiceGamma = invoiceGammaRepository.saveAndFlush(invoiceGamma);

        // Get all the invoiceGammaList where amount is less than
        defaultInvoiceGammaFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceGammasByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInvoiceGamma = invoiceGammaRepository.saveAndFlush(invoiceGamma);

        // Get all the invoiceGammaList where amount is greater than
        defaultInvoiceGammaFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceGammasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            invoiceGammaRepository.saveAndFlush(invoiceGamma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        invoiceGamma.setTenant(tenant);
        invoiceGammaRepository.saveAndFlush(invoiceGamma);
        Long tenantId = tenant.getId();
        // Get all the invoiceGammaList where tenant equals to tenantId
        defaultInvoiceGammaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the invoiceGammaList where tenant equals to (tenantId + 1)
        defaultInvoiceGammaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultInvoiceGammaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInvoiceGammaShouldBeFound(shouldBeFound);
        defaultInvoiceGammaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInvoiceGammaShouldBeFound(String filter) throws Exception {
        restInvoiceGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));

        // Check, that the count call also returns 1
        restInvoiceGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInvoiceGammaShouldNotBeFound(String filter) throws Exception {
        restInvoiceGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInvoiceGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInvoiceGamma() throws Exception {
        // Get the invoiceGamma
        restInvoiceGammaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInvoiceGamma() throws Exception {
        // Initialize the database
        insertedInvoiceGamma = invoiceGammaRepository.saveAndFlush(invoiceGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceGamma
        InvoiceGamma updatedInvoiceGamma = invoiceGammaRepository.findById(invoiceGamma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInvoiceGamma are not directly saved in db
        em.detach(updatedInvoiceGamma);
        updatedInvoiceGamma
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);

        restInvoiceGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInvoiceGamma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInvoiceGamma))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInvoiceGammaToMatchAllProperties(updatedInvoiceGamma);
    }

    @Test
    @Transactional
    void putNonExistingInvoiceGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceGamma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceGamma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceGamma))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoiceGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceGamma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceGamma))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoiceGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceGamma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceGammaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceGamma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceGammaWithPatch() throws Exception {
        // Initialize the database
        insertedInvoiceGamma = invoiceGammaRepository.saveAndFlush(invoiceGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceGamma using partial update
        InvoiceGamma partialUpdatedInvoiceGamma = new InvoiceGamma();
        partialUpdatedInvoiceGamma.setId(invoiceGamma.getId());

        partialUpdatedInvoiceGamma.dueDate(UPDATED_DUE_DATE);

        restInvoiceGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoiceGamma))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceGammaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInvoiceGamma, invoiceGamma),
            getPersistedInvoiceGamma(invoiceGamma)
        );
    }

    @Test
    @Transactional
    void fullUpdateInvoiceGammaWithPatch() throws Exception {
        // Initialize the database
        insertedInvoiceGamma = invoiceGammaRepository.saveAndFlush(invoiceGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceGamma using partial update
        InvoiceGamma partialUpdatedInvoiceGamma = new InvoiceGamma();
        partialUpdatedInvoiceGamma.setId(invoiceGamma.getId());

        partialUpdatedInvoiceGamma
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);

        restInvoiceGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoiceGamma))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceGammaUpdatableFieldsEquals(partialUpdatedInvoiceGamma, getPersistedInvoiceGamma(partialUpdatedInvoiceGamma));
    }

    @Test
    @Transactional
    void patchNonExistingInvoiceGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceGamma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoiceGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoiceGamma))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoiceGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceGamma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoiceGamma))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoiceGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceGamma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceGammaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(invoiceGamma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvoiceGamma() throws Exception {
        // Initialize the database
        insertedInvoiceGamma = invoiceGammaRepository.saveAndFlush(invoiceGamma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the invoiceGamma
        restInvoiceGammaMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoiceGamma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return invoiceGammaRepository.count();
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

    protected InvoiceGamma getPersistedInvoiceGamma(InvoiceGamma invoiceGamma) {
        return invoiceGammaRepository.findById(invoiceGamma.getId()).orElseThrow();
    }

    protected void assertPersistedInvoiceGammaToMatchAllProperties(InvoiceGamma expectedInvoiceGamma) {
        assertInvoiceGammaAllPropertiesEquals(expectedInvoiceGamma, getPersistedInvoiceGamma(expectedInvoiceGamma));
    }

    protected void assertPersistedInvoiceGammaToMatchUpdatableProperties(InvoiceGamma expectedInvoiceGamma) {
        assertInvoiceGammaAllUpdatablePropertiesEquals(expectedInvoiceGamma, getPersistedInvoiceGamma(expectedInvoiceGamma));
    }
}
