package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextInvoiceMiAsserts.*;
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
import xyz.jhmapstruct.domain.NextInvoiceMi;
import xyz.jhmapstruct.repository.NextInvoiceMiRepository;

/**
 * Integration tests for the {@link NextInvoiceMiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextInvoiceMiResourceIT {

    private static final String DEFAULT_INVOICE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_ISSUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ISSUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/next-invoice-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextInvoiceMiRepository nextInvoiceMiRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextInvoiceMiMockMvc;

    private NextInvoiceMi nextInvoiceMi;

    private NextInvoiceMi insertedNextInvoiceMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextInvoiceMi createEntity() {
        return new NextInvoiceMi()
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
    public static NextInvoiceMi createUpdatedEntity() {
        return new NextInvoiceMi()
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);
    }

    @BeforeEach
    public void initTest() {
        nextInvoiceMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextInvoiceMi != null) {
            nextInvoiceMiRepository.delete(insertedNextInvoiceMi);
            insertedNextInvoiceMi = null;
        }
    }

    @Test
    @Transactional
    void createNextInvoiceMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextInvoiceMi
        var returnedNextInvoiceMi = om.readValue(
            restNextInvoiceMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceMi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextInvoiceMi.class
        );

        // Validate the NextInvoiceMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextInvoiceMiUpdatableFieldsEquals(returnedNextInvoiceMi, getPersistedNextInvoiceMi(returnedNextInvoiceMi));

        insertedNextInvoiceMi = returnedNextInvoiceMi;
    }

    @Test
    @Transactional
    void createNextInvoiceMiWithExistingId() throws Exception {
        // Create the NextInvoiceMi with an existing ID
        nextInvoiceMi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextInvoiceMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceMi)))
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkInvoiceNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextInvoiceMi.setInvoiceNumber(null);

        // Create the NextInvoiceMi, which fails.

        restNextInvoiceMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIssueDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextInvoiceMi.setIssueDate(null);

        // Create the NextInvoiceMi, which fails.

        restNextInvoiceMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextInvoiceMi.setAmount(null);

        // Create the NextInvoiceMi, which fails.

        restNextInvoiceMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextInvoiceMis() throws Exception {
        // Initialize the database
        insertedNextInvoiceMi = nextInvoiceMiRepository.saveAndFlush(nextInvoiceMi);

        // Get all the nextInvoiceMiList
        restNextInvoiceMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextInvoiceMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));
    }

    @Test
    @Transactional
    void getNextInvoiceMi() throws Exception {
        // Initialize the database
        insertedNextInvoiceMi = nextInvoiceMiRepository.saveAndFlush(nextInvoiceMi);

        // Get the nextInvoiceMi
        restNextInvoiceMiMockMvc
            .perform(get(ENTITY_API_URL_ID, nextInvoiceMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextInvoiceMi.getId().intValue()))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER))
            .andExpect(jsonPath("$.issueDate").value(DEFAULT_ISSUE_DATE.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)));
    }

    @Test
    @Transactional
    void getNextInvoiceMisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextInvoiceMi = nextInvoiceMiRepository.saveAndFlush(nextInvoiceMi);

        Long id = nextInvoiceMi.getId();

        defaultNextInvoiceMiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextInvoiceMiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextInvoiceMiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextInvoiceMisByInvoiceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceMi = nextInvoiceMiRepository.saveAndFlush(nextInvoiceMi);

        // Get all the nextInvoiceMiList where invoiceNumber equals to
        defaultNextInvoiceMiFiltering("invoiceNumber.equals=" + DEFAULT_INVOICE_NUMBER, "invoiceNumber.equals=" + UPDATED_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextInvoiceMisByInvoiceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoiceMi = nextInvoiceMiRepository.saveAndFlush(nextInvoiceMi);

        // Get all the nextInvoiceMiList where invoiceNumber in
        defaultNextInvoiceMiFiltering(
            "invoiceNumber.in=" + DEFAULT_INVOICE_NUMBER + "," + UPDATED_INVOICE_NUMBER,
            "invoiceNumber.in=" + UPDATED_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceMisByInvoiceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoiceMi = nextInvoiceMiRepository.saveAndFlush(nextInvoiceMi);

        // Get all the nextInvoiceMiList where invoiceNumber is not null
        defaultNextInvoiceMiFiltering("invoiceNumber.specified=true", "invoiceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoiceMisByInvoiceNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceMi = nextInvoiceMiRepository.saveAndFlush(nextInvoiceMi);

        // Get all the nextInvoiceMiList where invoiceNumber contains
        defaultNextInvoiceMiFiltering(
            "invoiceNumber.contains=" + DEFAULT_INVOICE_NUMBER,
            "invoiceNumber.contains=" + UPDATED_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceMisByInvoiceNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceMi = nextInvoiceMiRepository.saveAndFlush(nextInvoiceMi);

        // Get all the nextInvoiceMiList where invoiceNumber does not contain
        defaultNextInvoiceMiFiltering(
            "invoiceNumber.doesNotContain=" + UPDATED_INVOICE_NUMBER,
            "invoiceNumber.doesNotContain=" + DEFAULT_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceMisByIssueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceMi = nextInvoiceMiRepository.saveAndFlush(nextInvoiceMi);

        // Get all the nextInvoiceMiList where issueDate equals to
        defaultNextInvoiceMiFiltering("issueDate.equals=" + DEFAULT_ISSUE_DATE, "issueDate.equals=" + UPDATED_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllNextInvoiceMisByIssueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoiceMi = nextInvoiceMiRepository.saveAndFlush(nextInvoiceMi);

        // Get all the nextInvoiceMiList where issueDate in
        defaultNextInvoiceMiFiltering(
            "issueDate.in=" + DEFAULT_ISSUE_DATE + "," + UPDATED_ISSUE_DATE,
            "issueDate.in=" + UPDATED_ISSUE_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceMisByIssueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoiceMi = nextInvoiceMiRepository.saveAndFlush(nextInvoiceMi);

        // Get all the nextInvoiceMiList where issueDate is not null
        defaultNextInvoiceMiFiltering("issueDate.specified=true", "issueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoiceMisByDueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceMi = nextInvoiceMiRepository.saveAndFlush(nextInvoiceMi);

        // Get all the nextInvoiceMiList where dueDate equals to
        defaultNextInvoiceMiFiltering("dueDate.equals=" + DEFAULT_DUE_DATE, "dueDate.equals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllNextInvoiceMisByDueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoiceMi = nextInvoiceMiRepository.saveAndFlush(nextInvoiceMi);

        // Get all the nextInvoiceMiList where dueDate in
        defaultNextInvoiceMiFiltering("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE, "dueDate.in=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllNextInvoiceMisByDueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoiceMi = nextInvoiceMiRepository.saveAndFlush(nextInvoiceMi);

        // Get all the nextInvoiceMiList where dueDate is not null
        defaultNextInvoiceMiFiltering("dueDate.specified=true", "dueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoiceMisByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceMi = nextInvoiceMiRepository.saveAndFlush(nextInvoiceMi);

        // Get all the nextInvoiceMiList where amount equals to
        defaultNextInvoiceMiFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceMisByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoiceMi = nextInvoiceMiRepository.saveAndFlush(nextInvoiceMi);

        // Get all the nextInvoiceMiList where amount in
        defaultNextInvoiceMiFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceMisByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoiceMi = nextInvoiceMiRepository.saveAndFlush(nextInvoiceMi);

        // Get all the nextInvoiceMiList where amount is not null
        defaultNextInvoiceMiFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoiceMisByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceMi = nextInvoiceMiRepository.saveAndFlush(nextInvoiceMi);

        // Get all the nextInvoiceMiList where amount is greater than or equal to
        defaultNextInvoiceMiFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceMisByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceMi = nextInvoiceMiRepository.saveAndFlush(nextInvoiceMi);

        // Get all the nextInvoiceMiList where amount is less than or equal to
        defaultNextInvoiceMiFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceMisByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceMi = nextInvoiceMiRepository.saveAndFlush(nextInvoiceMi);

        // Get all the nextInvoiceMiList where amount is less than
        defaultNextInvoiceMiFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceMisByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceMi = nextInvoiceMiRepository.saveAndFlush(nextInvoiceMi);

        // Get all the nextInvoiceMiList where amount is greater than
        defaultNextInvoiceMiFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceMisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextInvoiceMiRepository.saveAndFlush(nextInvoiceMi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextInvoiceMi.setTenant(tenant);
        nextInvoiceMiRepository.saveAndFlush(nextInvoiceMi);
        Long tenantId = tenant.getId();
        // Get all the nextInvoiceMiList where tenant equals to tenantId
        defaultNextInvoiceMiShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextInvoiceMiList where tenant equals to (tenantId + 1)
        defaultNextInvoiceMiShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextInvoiceMiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextInvoiceMiShouldBeFound(shouldBeFound);
        defaultNextInvoiceMiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextInvoiceMiShouldBeFound(String filter) throws Exception {
        restNextInvoiceMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextInvoiceMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));

        // Check, that the count call also returns 1
        restNextInvoiceMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextInvoiceMiShouldNotBeFound(String filter) throws Exception {
        restNextInvoiceMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextInvoiceMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextInvoiceMi() throws Exception {
        // Get the nextInvoiceMi
        restNextInvoiceMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextInvoiceMi() throws Exception {
        // Initialize the database
        insertedNextInvoiceMi = nextInvoiceMiRepository.saveAndFlush(nextInvoiceMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextInvoiceMi
        NextInvoiceMi updatedNextInvoiceMi = nextInvoiceMiRepository.findById(nextInvoiceMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextInvoiceMi are not directly saved in db
        em.detach(updatedNextInvoiceMi);
        updatedNextInvoiceMi
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);

        restNextInvoiceMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextInvoiceMi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextInvoiceMi))
            )
            .andExpect(status().isOk());

        // Validate the NextInvoiceMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextInvoiceMiToMatchAllProperties(updatedNextInvoiceMi);
    }

    @Test
    @Transactional
    void putNonExistingNextInvoiceMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceMi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextInvoiceMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextInvoiceMi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextInvoiceMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextInvoiceMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextInvoiceMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextInvoiceMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceMi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextInvoiceMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextInvoiceMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextInvoiceMi = nextInvoiceMiRepository.saveAndFlush(nextInvoiceMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextInvoiceMi using partial update
        NextInvoiceMi partialUpdatedNextInvoiceMi = new NextInvoiceMi();
        partialUpdatedNextInvoiceMi.setId(nextInvoiceMi.getId());

        partialUpdatedNextInvoiceMi.invoiceNumber(UPDATED_INVOICE_NUMBER);

        restNextInvoiceMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextInvoiceMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextInvoiceMi))
            )
            .andExpect(status().isOk());

        // Validate the NextInvoiceMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextInvoiceMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextInvoiceMi, nextInvoiceMi),
            getPersistedNextInvoiceMi(nextInvoiceMi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextInvoiceMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextInvoiceMi = nextInvoiceMiRepository.saveAndFlush(nextInvoiceMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextInvoiceMi using partial update
        NextInvoiceMi partialUpdatedNextInvoiceMi = new NextInvoiceMi();
        partialUpdatedNextInvoiceMi.setId(nextInvoiceMi.getId());

        partialUpdatedNextInvoiceMi
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);

        restNextInvoiceMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextInvoiceMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextInvoiceMi))
            )
            .andExpect(status().isOk());

        // Validate the NextInvoiceMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextInvoiceMiUpdatableFieldsEquals(partialUpdatedNextInvoiceMi, getPersistedNextInvoiceMi(partialUpdatedNextInvoiceMi));
    }

    @Test
    @Transactional
    void patchNonExistingNextInvoiceMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceMi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextInvoiceMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextInvoiceMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextInvoiceMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextInvoiceMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextInvoiceMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextInvoiceMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextInvoiceMi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextInvoiceMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextInvoiceMi() throws Exception {
        // Initialize the database
        insertedNextInvoiceMi = nextInvoiceMiRepository.saveAndFlush(nextInvoiceMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextInvoiceMi
        restNextInvoiceMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextInvoiceMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextInvoiceMiRepository.count();
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

    protected NextInvoiceMi getPersistedNextInvoiceMi(NextInvoiceMi nextInvoiceMi) {
        return nextInvoiceMiRepository.findById(nextInvoiceMi.getId()).orElseThrow();
    }

    protected void assertPersistedNextInvoiceMiToMatchAllProperties(NextInvoiceMi expectedNextInvoiceMi) {
        assertNextInvoiceMiAllPropertiesEquals(expectedNextInvoiceMi, getPersistedNextInvoiceMi(expectedNextInvoiceMi));
    }

    protected void assertPersistedNextInvoiceMiToMatchUpdatableProperties(NextInvoiceMi expectedNextInvoiceMi) {
        assertNextInvoiceMiAllUpdatablePropertiesEquals(expectedNextInvoiceMi, getPersistedNextInvoiceMi(expectedNextInvoiceMi));
    }
}
