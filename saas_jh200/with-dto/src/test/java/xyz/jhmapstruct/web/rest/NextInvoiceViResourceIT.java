package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextInvoiceViAsserts.*;
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
import xyz.jhmapstruct.domain.NextInvoiceVi;
import xyz.jhmapstruct.repository.NextInvoiceViRepository;
import xyz.jhmapstruct.service.dto.NextInvoiceViDTO;
import xyz.jhmapstruct.service.mapper.NextInvoiceViMapper;

/**
 * Integration tests for the {@link NextInvoiceViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextInvoiceViResourceIT {

    private static final String DEFAULT_INVOICE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_ISSUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ISSUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/next-invoice-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextInvoiceViRepository nextInvoiceViRepository;

    @Autowired
    private NextInvoiceViMapper nextInvoiceViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextInvoiceViMockMvc;

    private NextInvoiceVi nextInvoiceVi;

    private NextInvoiceVi insertedNextInvoiceVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextInvoiceVi createEntity() {
        return new NextInvoiceVi()
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
    public static NextInvoiceVi createUpdatedEntity() {
        return new NextInvoiceVi()
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);
    }

    @BeforeEach
    public void initTest() {
        nextInvoiceVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextInvoiceVi != null) {
            nextInvoiceViRepository.delete(insertedNextInvoiceVi);
            insertedNextInvoiceVi = null;
        }
    }

    @Test
    @Transactional
    void createNextInvoiceVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextInvoiceVi
        NextInvoiceViDTO nextInvoiceViDTO = nextInvoiceViMapper.toDto(nextInvoiceVi);
        var returnedNextInvoiceViDTO = om.readValue(
            restNextInvoiceViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextInvoiceViDTO.class
        );

        // Validate the NextInvoiceVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextInvoiceVi = nextInvoiceViMapper.toEntity(returnedNextInvoiceViDTO);
        assertNextInvoiceViUpdatableFieldsEquals(returnedNextInvoiceVi, getPersistedNextInvoiceVi(returnedNextInvoiceVi));

        insertedNextInvoiceVi = returnedNextInvoiceVi;
    }

    @Test
    @Transactional
    void createNextInvoiceViWithExistingId() throws Exception {
        // Create the NextInvoiceVi with an existing ID
        nextInvoiceVi.setId(1L);
        NextInvoiceViDTO nextInvoiceViDTO = nextInvoiceViMapper.toDto(nextInvoiceVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextInvoiceViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkInvoiceNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextInvoiceVi.setInvoiceNumber(null);

        // Create the NextInvoiceVi, which fails.
        NextInvoiceViDTO nextInvoiceViDTO = nextInvoiceViMapper.toDto(nextInvoiceVi);

        restNextInvoiceViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIssueDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextInvoiceVi.setIssueDate(null);

        // Create the NextInvoiceVi, which fails.
        NextInvoiceViDTO nextInvoiceViDTO = nextInvoiceViMapper.toDto(nextInvoiceVi);

        restNextInvoiceViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextInvoiceVi.setAmount(null);

        // Create the NextInvoiceVi, which fails.
        NextInvoiceViDTO nextInvoiceViDTO = nextInvoiceViMapper.toDto(nextInvoiceVi);

        restNextInvoiceViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextInvoiceVis() throws Exception {
        // Initialize the database
        insertedNextInvoiceVi = nextInvoiceViRepository.saveAndFlush(nextInvoiceVi);

        // Get all the nextInvoiceViList
        restNextInvoiceViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextInvoiceVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));
    }

    @Test
    @Transactional
    void getNextInvoiceVi() throws Exception {
        // Initialize the database
        insertedNextInvoiceVi = nextInvoiceViRepository.saveAndFlush(nextInvoiceVi);

        // Get the nextInvoiceVi
        restNextInvoiceViMockMvc
            .perform(get(ENTITY_API_URL_ID, nextInvoiceVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextInvoiceVi.getId().intValue()))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER))
            .andExpect(jsonPath("$.issueDate").value(DEFAULT_ISSUE_DATE.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)));
    }

    @Test
    @Transactional
    void getNextInvoiceVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextInvoiceVi = nextInvoiceViRepository.saveAndFlush(nextInvoiceVi);

        Long id = nextInvoiceVi.getId();

        defaultNextInvoiceViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextInvoiceViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextInvoiceViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextInvoiceVisByInvoiceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceVi = nextInvoiceViRepository.saveAndFlush(nextInvoiceVi);

        // Get all the nextInvoiceViList where invoiceNumber equals to
        defaultNextInvoiceViFiltering("invoiceNumber.equals=" + DEFAULT_INVOICE_NUMBER, "invoiceNumber.equals=" + UPDATED_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextInvoiceVisByInvoiceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoiceVi = nextInvoiceViRepository.saveAndFlush(nextInvoiceVi);

        // Get all the nextInvoiceViList where invoiceNumber in
        defaultNextInvoiceViFiltering(
            "invoiceNumber.in=" + DEFAULT_INVOICE_NUMBER + "," + UPDATED_INVOICE_NUMBER,
            "invoiceNumber.in=" + UPDATED_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceVisByInvoiceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoiceVi = nextInvoiceViRepository.saveAndFlush(nextInvoiceVi);

        // Get all the nextInvoiceViList where invoiceNumber is not null
        defaultNextInvoiceViFiltering("invoiceNumber.specified=true", "invoiceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoiceVisByInvoiceNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceVi = nextInvoiceViRepository.saveAndFlush(nextInvoiceVi);

        // Get all the nextInvoiceViList where invoiceNumber contains
        defaultNextInvoiceViFiltering(
            "invoiceNumber.contains=" + DEFAULT_INVOICE_NUMBER,
            "invoiceNumber.contains=" + UPDATED_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceVisByInvoiceNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceVi = nextInvoiceViRepository.saveAndFlush(nextInvoiceVi);

        // Get all the nextInvoiceViList where invoiceNumber does not contain
        defaultNextInvoiceViFiltering(
            "invoiceNumber.doesNotContain=" + UPDATED_INVOICE_NUMBER,
            "invoiceNumber.doesNotContain=" + DEFAULT_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceVisByIssueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceVi = nextInvoiceViRepository.saveAndFlush(nextInvoiceVi);

        // Get all the nextInvoiceViList where issueDate equals to
        defaultNextInvoiceViFiltering("issueDate.equals=" + DEFAULT_ISSUE_DATE, "issueDate.equals=" + UPDATED_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllNextInvoiceVisByIssueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoiceVi = nextInvoiceViRepository.saveAndFlush(nextInvoiceVi);

        // Get all the nextInvoiceViList where issueDate in
        defaultNextInvoiceViFiltering(
            "issueDate.in=" + DEFAULT_ISSUE_DATE + "," + UPDATED_ISSUE_DATE,
            "issueDate.in=" + UPDATED_ISSUE_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextInvoiceVisByIssueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoiceVi = nextInvoiceViRepository.saveAndFlush(nextInvoiceVi);

        // Get all the nextInvoiceViList where issueDate is not null
        defaultNextInvoiceViFiltering("issueDate.specified=true", "issueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoiceVisByDueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceVi = nextInvoiceViRepository.saveAndFlush(nextInvoiceVi);

        // Get all the nextInvoiceViList where dueDate equals to
        defaultNextInvoiceViFiltering("dueDate.equals=" + DEFAULT_DUE_DATE, "dueDate.equals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllNextInvoiceVisByDueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoiceVi = nextInvoiceViRepository.saveAndFlush(nextInvoiceVi);

        // Get all the nextInvoiceViList where dueDate in
        defaultNextInvoiceViFiltering("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE, "dueDate.in=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllNextInvoiceVisByDueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoiceVi = nextInvoiceViRepository.saveAndFlush(nextInvoiceVi);

        // Get all the nextInvoiceViList where dueDate is not null
        defaultNextInvoiceViFiltering("dueDate.specified=true", "dueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoiceVisByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceVi = nextInvoiceViRepository.saveAndFlush(nextInvoiceVi);

        // Get all the nextInvoiceViList where amount equals to
        defaultNextInvoiceViFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceVisByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoiceVi = nextInvoiceViRepository.saveAndFlush(nextInvoiceVi);

        // Get all the nextInvoiceViList where amount in
        defaultNextInvoiceViFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceVisByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoiceVi = nextInvoiceViRepository.saveAndFlush(nextInvoiceVi);

        // Get all the nextInvoiceViList where amount is not null
        defaultNextInvoiceViFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoiceVisByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceVi = nextInvoiceViRepository.saveAndFlush(nextInvoiceVi);

        // Get all the nextInvoiceViList where amount is greater than or equal to
        defaultNextInvoiceViFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceVisByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceVi = nextInvoiceViRepository.saveAndFlush(nextInvoiceVi);

        // Get all the nextInvoiceViList where amount is less than or equal to
        defaultNextInvoiceViFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceVisByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceVi = nextInvoiceViRepository.saveAndFlush(nextInvoiceVi);

        // Get all the nextInvoiceViList where amount is less than
        defaultNextInvoiceViFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceVisByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextInvoiceVi = nextInvoiceViRepository.saveAndFlush(nextInvoiceVi);

        // Get all the nextInvoiceViList where amount is greater than
        defaultNextInvoiceViFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoiceVisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextInvoiceViRepository.saveAndFlush(nextInvoiceVi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextInvoiceVi.setTenant(tenant);
        nextInvoiceViRepository.saveAndFlush(nextInvoiceVi);
        Long tenantId = tenant.getId();
        // Get all the nextInvoiceViList where tenant equals to tenantId
        defaultNextInvoiceViShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextInvoiceViList where tenant equals to (tenantId + 1)
        defaultNextInvoiceViShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextInvoiceViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextInvoiceViShouldBeFound(shouldBeFound);
        defaultNextInvoiceViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextInvoiceViShouldBeFound(String filter) throws Exception {
        restNextInvoiceViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextInvoiceVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));

        // Check, that the count call also returns 1
        restNextInvoiceViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextInvoiceViShouldNotBeFound(String filter) throws Exception {
        restNextInvoiceViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextInvoiceViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextInvoiceVi() throws Exception {
        // Get the nextInvoiceVi
        restNextInvoiceViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextInvoiceVi() throws Exception {
        // Initialize the database
        insertedNextInvoiceVi = nextInvoiceViRepository.saveAndFlush(nextInvoiceVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextInvoiceVi
        NextInvoiceVi updatedNextInvoiceVi = nextInvoiceViRepository.findById(nextInvoiceVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextInvoiceVi are not directly saved in db
        em.detach(updatedNextInvoiceVi);
        updatedNextInvoiceVi
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);
        NextInvoiceViDTO nextInvoiceViDTO = nextInvoiceViMapper.toDto(updatedNextInvoiceVi);

        restNextInvoiceViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextInvoiceViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextInvoiceViDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextInvoiceVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextInvoiceViToMatchAllProperties(updatedNextInvoiceVi);
    }

    @Test
    @Transactional
    void putNonExistingNextInvoiceVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceVi.setId(longCount.incrementAndGet());

        // Create the NextInvoiceVi
        NextInvoiceViDTO nextInvoiceViDTO = nextInvoiceViMapper.toDto(nextInvoiceVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextInvoiceViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextInvoiceViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextInvoiceViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextInvoiceVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceVi.setId(longCount.incrementAndGet());

        // Create the NextInvoiceVi
        NextInvoiceViDTO nextInvoiceViDTO = nextInvoiceViMapper.toDto(nextInvoiceVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextInvoiceViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextInvoiceVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceVi.setId(longCount.incrementAndGet());

        // Create the NextInvoiceVi
        NextInvoiceViDTO nextInvoiceViDTO = nextInvoiceViMapper.toDto(nextInvoiceVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextInvoiceVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextInvoiceViWithPatch() throws Exception {
        // Initialize the database
        insertedNextInvoiceVi = nextInvoiceViRepository.saveAndFlush(nextInvoiceVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextInvoiceVi using partial update
        NextInvoiceVi partialUpdatedNextInvoiceVi = new NextInvoiceVi();
        partialUpdatedNextInvoiceVi.setId(nextInvoiceVi.getId());

        partialUpdatedNextInvoiceVi.dueDate(UPDATED_DUE_DATE);

        restNextInvoiceViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextInvoiceVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextInvoiceVi))
            )
            .andExpect(status().isOk());

        // Validate the NextInvoiceVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextInvoiceViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextInvoiceVi, nextInvoiceVi),
            getPersistedNextInvoiceVi(nextInvoiceVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextInvoiceViWithPatch() throws Exception {
        // Initialize the database
        insertedNextInvoiceVi = nextInvoiceViRepository.saveAndFlush(nextInvoiceVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextInvoiceVi using partial update
        NextInvoiceVi partialUpdatedNextInvoiceVi = new NextInvoiceVi();
        partialUpdatedNextInvoiceVi.setId(nextInvoiceVi.getId());

        partialUpdatedNextInvoiceVi
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);

        restNextInvoiceViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextInvoiceVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextInvoiceVi))
            )
            .andExpect(status().isOk());

        // Validate the NextInvoiceVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextInvoiceViUpdatableFieldsEquals(partialUpdatedNextInvoiceVi, getPersistedNextInvoiceVi(partialUpdatedNextInvoiceVi));
    }

    @Test
    @Transactional
    void patchNonExistingNextInvoiceVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceVi.setId(longCount.incrementAndGet());

        // Create the NextInvoiceVi
        NextInvoiceViDTO nextInvoiceViDTO = nextInvoiceViMapper.toDto(nextInvoiceVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextInvoiceViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextInvoiceViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextInvoiceViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextInvoiceVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceVi.setId(longCount.incrementAndGet());

        // Create the NextInvoiceVi
        NextInvoiceViDTO nextInvoiceViDTO = nextInvoiceViMapper.toDto(nextInvoiceVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextInvoiceViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoiceVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextInvoiceVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoiceVi.setId(longCount.incrementAndGet());

        // Create the NextInvoiceVi
        NextInvoiceViDTO nextInvoiceViDTO = nextInvoiceViMapper.toDto(nextInvoiceVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextInvoiceViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextInvoiceVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextInvoiceVi() throws Exception {
        // Initialize the database
        insertedNextInvoiceVi = nextInvoiceViRepository.saveAndFlush(nextInvoiceVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextInvoiceVi
        restNextInvoiceViMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextInvoiceVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextInvoiceViRepository.count();
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

    protected NextInvoiceVi getPersistedNextInvoiceVi(NextInvoiceVi nextInvoiceVi) {
        return nextInvoiceViRepository.findById(nextInvoiceVi.getId()).orElseThrow();
    }

    protected void assertPersistedNextInvoiceViToMatchAllProperties(NextInvoiceVi expectedNextInvoiceVi) {
        assertNextInvoiceViAllPropertiesEquals(expectedNextInvoiceVi, getPersistedNextInvoiceVi(expectedNextInvoiceVi));
    }

    protected void assertPersistedNextInvoiceViToMatchUpdatableProperties(NextInvoiceVi expectedNextInvoiceVi) {
        assertNextInvoiceViAllUpdatablePropertiesEquals(expectedNextInvoiceVi, getPersistedNextInvoiceVi(expectedNextInvoiceVi));
    }
}
