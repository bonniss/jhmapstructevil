package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.InvoiceAlphaAsserts.*;
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
import xyz.jhmapstruct.domain.InvoiceAlpha;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.repository.InvoiceAlphaRepository;

/**
 * Integration tests for the {@link InvoiceAlphaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InvoiceAlphaResourceIT {

    private static final String DEFAULT_INVOICE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_ISSUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ISSUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/invoice-alphas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InvoiceAlphaRepository invoiceAlphaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceAlphaMockMvc;

    private InvoiceAlpha invoiceAlpha;

    private InvoiceAlpha insertedInvoiceAlpha;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceAlpha createEntity() {
        return new InvoiceAlpha()
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
    public static InvoiceAlpha createUpdatedEntity() {
        return new InvoiceAlpha()
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);
    }

    @BeforeEach
    public void initTest() {
        invoiceAlpha = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedInvoiceAlpha != null) {
            invoiceAlphaRepository.delete(insertedInvoiceAlpha);
            insertedInvoiceAlpha = null;
        }
    }

    @Test
    @Transactional
    void createInvoiceAlpha() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InvoiceAlpha
        var returnedInvoiceAlpha = om.readValue(
            restInvoiceAlphaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceAlpha)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InvoiceAlpha.class
        );

        // Validate the InvoiceAlpha in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInvoiceAlphaUpdatableFieldsEquals(returnedInvoiceAlpha, getPersistedInvoiceAlpha(returnedInvoiceAlpha));

        insertedInvoiceAlpha = returnedInvoiceAlpha;
    }

    @Test
    @Transactional
    void createInvoiceAlphaWithExistingId() throws Exception {
        // Create the InvoiceAlpha with an existing ID
        invoiceAlpha.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceAlpha)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkInvoiceNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceAlpha.setInvoiceNumber(null);

        // Create the InvoiceAlpha, which fails.

        restInvoiceAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceAlpha)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIssueDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceAlpha.setIssueDate(null);

        // Create the InvoiceAlpha, which fails.

        restInvoiceAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceAlpha)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceAlpha.setAmount(null);

        // Create the InvoiceAlpha, which fails.

        restInvoiceAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceAlpha)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInvoiceAlphas() throws Exception {
        // Initialize the database
        insertedInvoiceAlpha = invoiceAlphaRepository.saveAndFlush(invoiceAlpha);

        // Get all the invoiceAlphaList
        restInvoiceAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));
    }

    @Test
    @Transactional
    void getInvoiceAlpha() throws Exception {
        // Initialize the database
        insertedInvoiceAlpha = invoiceAlphaRepository.saveAndFlush(invoiceAlpha);

        // Get the invoiceAlpha
        restInvoiceAlphaMockMvc
            .perform(get(ENTITY_API_URL_ID, invoiceAlpha.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceAlpha.getId().intValue()))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER))
            .andExpect(jsonPath("$.issueDate").value(DEFAULT_ISSUE_DATE.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)));
    }

    @Test
    @Transactional
    void getInvoiceAlphasByIdFiltering() throws Exception {
        // Initialize the database
        insertedInvoiceAlpha = invoiceAlphaRepository.saveAndFlush(invoiceAlpha);

        Long id = invoiceAlpha.getId();

        defaultInvoiceAlphaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInvoiceAlphaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInvoiceAlphaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInvoiceAlphasByInvoiceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceAlpha = invoiceAlphaRepository.saveAndFlush(invoiceAlpha);

        // Get all the invoiceAlphaList where invoiceNumber equals to
        defaultInvoiceAlphaFiltering("invoiceNumber.equals=" + DEFAULT_INVOICE_NUMBER, "invoiceNumber.equals=" + UPDATED_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    void getAllInvoiceAlphasByInvoiceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceAlpha = invoiceAlphaRepository.saveAndFlush(invoiceAlpha);

        // Get all the invoiceAlphaList where invoiceNumber in
        defaultInvoiceAlphaFiltering(
            "invoiceNumber.in=" + DEFAULT_INVOICE_NUMBER + "," + UPDATED_INVOICE_NUMBER,
            "invoiceNumber.in=" + UPDATED_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllInvoiceAlphasByInvoiceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceAlpha = invoiceAlphaRepository.saveAndFlush(invoiceAlpha);

        // Get all the invoiceAlphaList where invoiceNumber is not null
        defaultInvoiceAlphaFiltering("invoiceNumber.specified=true", "invoiceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceAlphasByInvoiceNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoiceAlpha = invoiceAlphaRepository.saveAndFlush(invoiceAlpha);

        // Get all the invoiceAlphaList where invoiceNumber contains
        defaultInvoiceAlphaFiltering(
            "invoiceNumber.contains=" + DEFAULT_INVOICE_NUMBER,
            "invoiceNumber.contains=" + UPDATED_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllInvoiceAlphasByInvoiceNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoiceAlpha = invoiceAlphaRepository.saveAndFlush(invoiceAlpha);

        // Get all the invoiceAlphaList where invoiceNumber does not contain
        defaultInvoiceAlphaFiltering(
            "invoiceNumber.doesNotContain=" + UPDATED_INVOICE_NUMBER,
            "invoiceNumber.doesNotContain=" + DEFAULT_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllInvoiceAlphasByIssueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceAlpha = invoiceAlphaRepository.saveAndFlush(invoiceAlpha);

        // Get all the invoiceAlphaList where issueDate equals to
        defaultInvoiceAlphaFiltering("issueDate.equals=" + DEFAULT_ISSUE_DATE, "issueDate.equals=" + UPDATED_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllInvoiceAlphasByIssueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceAlpha = invoiceAlphaRepository.saveAndFlush(invoiceAlpha);

        // Get all the invoiceAlphaList where issueDate in
        defaultInvoiceAlphaFiltering("issueDate.in=" + DEFAULT_ISSUE_DATE + "," + UPDATED_ISSUE_DATE, "issueDate.in=" + UPDATED_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllInvoiceAlphasByIssueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceAlpha = invoiceAlphaRepository.saveAndFlush(invoiceAlpha);

        // Get all the invoiceAlphaList where issueDate is not null
        defaultInvoiceAlphaFiltering("issueDate.specified=true", "issueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceAlphasByDueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceAlpha = invoiceAlphaRepository.saveAndFlush(invoiceAlpha);

        // Get all the invoiceAlphaList where dueDate equals to
        defaultInvoiceAlphaFiltering("dueDate.equals=" + DEFAULT_DUE_DATE, "dueDate.equals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllInvoiceAlphasByDueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceAlpha = invoiceAlphaRepository.saveAndFlush(invoiceAlpha);

        // Get all the invoiceAlphaList where dueDate in
        defaultInvoiceAlphaFiltering("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE, "dueDate.in=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllInvoiceAlphasByDueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceAlpha = invoiceAlphaRepository.saveAndFlush(invoiceAlpha);

        // Get all the invoiceAlphaList where dueDate is not null
        defaultInvoiceAlphaFiltering("dueDate.specified=true", "dueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceAlphasByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceAlpha = invoiceAlphaRepository.saveAndFlush(invoiceAlpha);

        // Get all the invoiceAlphaList where amount equals to
        defaultInvoiceAlphaFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceAlphasByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceAlpha = invoiceAlphaRepository.saveAndFlush(invoiceAlpha);

        // Get all the invoiceAlphaList where amount in
        defaultInvoiceAlphaFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceAlphasByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceAlpha = invoiceAlphaRepository.saveAndFlush(invoiceAlpha);

        // Get all the invoiceAlphaList where amount is not null
        defaultInvoiceAlphaFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceAlphasByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceAlpha = invoiceAlphaRepository.saveAndFlush(invoiceAlpha);

        // Get all the invoiceAlphaList where amount is greater than or equal to
        defaultInvoiceAlphaFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceAlphasByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceAlpha = invoiceAlphaRepository.saveAndFlush(invoiceAlpha);

        // Get all the invoiceAlphaList where amount is less than or equal to
        defaultInvoiceAlphaFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceAlphasByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInvoiceAlpha = invoiceAlphaRepository.saveAndFlush(invoiceAlpha);

        // Get all the invoiceAlphaList where amount is less than
        defaultInvoiceAlphaFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceAlphasByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInvoiceAlpha = invoiceAlphaRepository.saveAndFlush(invoiceAlpha);

        // Get all the invoiceAlphaList where amount is greater than
        defaultInvoiceAlphaFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceAlphasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            invoiceAlphaRepository.saveAndFlush(invoiceAlpha);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        invoiceAlpha.setTenant(tenant);
        invoiceAlphaRepository.saveAndFlush(invoiceAlpha);
        Long tenantId = tenant.getId();
        // Get all the invoiceAlphaList where tenant equals to tenantId
        defaultInvoiceAlphaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the invoiceAlphaList where tenant equals to (tenantId + 1)
        defaultInvoiceAlphaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultInvoiceAlphaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInvoiceAlphaShouldBeFound(shouldBeFound);
        defaultInvoiceAlphaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInvoiceAlphaShouldBeFound(String filter) throws Exception {
        restInvoiceAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));

        // Check, that the count call also returns 1
        restInvoiceAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInvoiceAlphaShouldNotBeFound(String filter) throws Exception {
        restInvoiceAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInvoiceAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInvoiceAlpha() throws Exception {
        // Get the invoiceAlpha
        restInvoiceAlphaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInvoiceAlpha() throws Exception {
        // Initialize the database
        insertedInvoiceAlpha = invoiceAlphaRepository.saveAndFlush(invoiceAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceAlpha
        InvoiceAlpha updatedInvoiceAlpha = invoiceAlphaRepository.findById(invoiceAlpha.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInvoiceAlpha are not directly saved in db
        em.detach(updatedInvoiceAlpha);
        updatedInvoiceAlpha
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);

        restInvoiceAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInvoiceAlpha.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInvoiceAlpha))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInvoiceAlphaToMatchAllProperties(updatedInvoiceAlpha);
    }

    @Test
    @Transactional
    void putNonExistingInvoiceAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceAlpha.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceAlpha.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoiceAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoiceAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceAlphaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceAlpha)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedInvoiceAlpha = invoiceAlphaRepository.saveAndFlush(invoiceAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceAlpha using partial update
        InvoiceAlpha partialUpdatedInvoiceAlpha = new InvoiceAlpha();
        partialUpdatedInvoiceAlpha.setId(invoiceAlpha.getId());

        restInvoiceAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoiceAlpha))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceAlphaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInvoiceAlpha, invoiceAlpha),
            getPersistedInvoiceAlpha(invoiceAlpha)
        );
    }

    @Test
    @Transactional
    void fullUpdateInvoiceAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedInvoiceAlpha = invoiceAlphaRepository.saveAndFlush(invoiceAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceAlpha using partial update
        InvoiceAlpha partialUpdatedInvoiceAlpha = new InvoiceAlpha();
        partialUpdatedInvoiceAlpha.setId(invoiceAlpha.getId());

        partialUpdatedInvoiceAlpha
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);

        restInvoiceAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoiceAlpha))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceAlphaUpdatableFieldsEquals(partialUpdatedInvoiceAlpha, getPersistedInvoiceAlpha(partialUpdatedInvoiceAlpha));
    }

    @Test
    @Transactional
    void patchNonExistingInvoiceAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceAlpha.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoiceAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoiceAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoiceAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoiceAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoiceAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceAlphaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(invoiceAlpha)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvoiceAlpha() throws Exception {
        // Initialize the database
        insertedInvoiceAlpha = invoiceAlphaRepository.saveAndFlush(invoiceAlpha);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the invoiceAlpha
        restInvoiceAlphaMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoiceAlpha.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return invoiceAlphaRepository.count();
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

    protected InvoiceAlpha getPersistedInvoiceAlpha(InvoiceAlpha invoiceAlpha) {
        return invoiceAlphaRepository.findById(invoiceAlpha.getId()).orElseThrow();
    }

    protected void assertPersistedInvoiceAlphaToMatchAllProperties(InvoiceAlpha expectedInvoiceAlpha) {
        assertInvoiceAlphaAllPropertiesEquals(expectedInvoiceAlpha, getPersistedInvoiceAlpha(expectedInvoiceAlpha));
    }

    protected void assertPersistedInvoiceAlphaToMatchUpdatableProperties(InvoiceAlpha expectedInvoiceAlpha) {
        assertInvoiceAlphaAllUpdatablePropertiesEquals(expectedInvoiceAlpha, getPersistedInvoiceAlpha(expectedInvoiceAlpha));
    }
}
