package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextInvoiceAsserts.*;
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
import xyz.jhmapstruct.domain.NextInvoice;
import xyz.jhmapstruct.repository.NextInvoiceRepository;
import xyz.jhmapstruct.service.dto.NextInvoiceDTO;
import xyz.jhmapstruct.service.mapper.NextInvoiceMapper;

/**
 * Integration tests for the {@link NextInvoiceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextInvoiceResourceIT {

    private static final String DEFAULT_INVOICE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_ISSUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ISSUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/next-invoices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextInvoiceRepository nextInvoiceRepository;

    @Autowired
    private NextInvoiceMapper nextInvoiceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextInvoiceMockMvc;

    private NextInvoice nextInvoice;

    private NextInvoice insertedNextInvoice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextInvoice createEntity() {
        return new NextInvoice()
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
    public static NextInvoice createUpdatedEntity() {
        return new NextInvoice()
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);
    }

    @BeforeEach
    public void initTest() {
        nextInvoice = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextInvoice != null) {
            nextInvoiceRepository.delete(insertedNextInvoice);
            insertedNextInvoice = null;
        }
    }

    @Test
    @Transactional
    void createNextInvoice() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextInvoice
        NextInvoiceDTO nextInvoiceDTO = nextInvoiceMapper.toDto(nextInvoice);
        var returnedNextInvoiceDTO = om.readValue(
            restNextInvoiceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextInvoiceDTO.class
        );

        // Validate the NextInvoice in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextInvoice = nextInvoiceMapper.toEntity(returnedNextInvoiceDTO);
        assertNextInvoiceUpdatableFieldsEquals(returnedNextInvoice, getPersistedNextInvoice(returnedNextInvoice));

        insertedNextInvoice = returnedNextInvoice;
    }

    @Test
    @Transactional
    void createNextInvoiceWithExistingId() throws Exception {
        // Create the NextInvoice with an existing ID
        nextInvoice.setId(1L);
        NextInvoiceDTO nextInvoiceDTO = nextInvoiceMapper.toDto(nextInvoice);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextInvoice in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkInvoiceNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextInvoice.setInvoiceNumber(null);

        // Create the NextInvoice, which fails.
        NextInvoiceDTO nextInvoiceDTO = nextInvoiceMapper.toDto(nextInvoice);

        restNextInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIssueDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextInvoice.setIssueDate(null);

        // Create the NextInvoice, which fails.
        NextInvoiceDTO nextInvoiceDTO = nextInvoiceMapper.toDto(nextInvoice);

        restNextInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextInvoice.setAmount(null);

        // Create the NextInvoice, which fails.
        NextInvoiceDTO nextInvoiceDTO = nextInvoiceMapper.toDto(nextInvoice);

        restNextInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextInvoices() throws Exception {
        // Initialize the database
        insertedNextInvoice = nextInvoiceRepository.saveAndFlush(nextInvoice);

        // Get all the nextInvoiceList
        restNextInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextInvoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));
    }

    @Test
    @Transactional
    void getNextInvoice() throws Exception {
        // Initialize the database
        insertedNextInvoice = nextInvoiceRepository.saveAndFlush(nextInvoice);

        // Get the nextInvoice
        restNextInvoiceMockMvc
            .perform(get(ENTITY_API_URL_ID, nextInvoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextInvoice.getId().intValue()))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER))
            .andExpect(jsonPath("$.issueDate").value(DEFAULT_ISSUE_DATE.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)));
    }

    @Test
    @Transactional
    void getNextInvoicesByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextInvoice = nextInvoiceRepository.saveAndFlush(nextInvoice);

        Long id = nextInvoice.getId();

        defaultNextInvoiceFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextInvoiceFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextInvoiceFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextInvoicesByInvoiceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoice = nextInvoiceRepository.saveAndFlush(nextInvoice);

        // Get all the nextInvoiceList where invoiceNumber equals to
        defaultNextInvoiceFiltering("invoiceNumber.equals=" + DEFAULT_INVOICE_NUMBER, "invoiceNumber.equals=" + UPDATED_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextInvoicesByInvoiceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoice = nextInvoiceRepository.saveAndFlush(nextInvoice);

        // Get all the nextInvoiceList where invoiceNumber in
        defaultNextInvoiceFiltering(
            "invoiceNumber.in=" + DEFAULT_INVOICE_NUMBER + "," + UPDATED_INVOICE_NUMBER,
            "invoiceNumber.in=" + UPDATED_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextInvoicesByInvoiceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoice = nextInvoiceRepository.saveAndFlush(nextInvoice);

        // Get all the nextInvoiceList where invoiceNumber is not null
        defaultNextInvoiceFiltering("invoiceNumber.specified=true", "invoiceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoicesByInvoiceNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextInvoice = nextInvoiceRepository.saveAndFlush(nextInvoice);

        // Get all the nextInvoiceList where invoiceNumber contains
        defaultNextInvoiceFiltering("invoiceNumber.contains=" + DEFAULT_INVOICE_NUMBER, "invoiceNumber.contains=" + UPDATED_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextInvoicesByInvoiceNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextInvoice = nextInvoiceRepository.saveAndFlush(nextInvoice);

        // Get all the nextInvoiceList where invoiceNumber does not contain
        defaultNextInvoiceFiltering(
            "invoiceNumber.doesNotContain=" + UPDATED_INVOICE_NUMBER,
            "invoiceNumber.doesNotContain=" + DEFAULT_INVOICE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextInvoicesByIssueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoice = nextInvoiceRepository.saveAndFlush(nextInvoice);

        // Get all the nextInvoiceList where issueDate equals to
        defaultNextInvoiceFiltering("issueDate.equals=" + DEFAULT_ISSUE_DATE, "issueDate.equals=" + UPDATED_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllNextInvoicesByIssueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoice = nextInvoiceRepository.saveAndFlush(nextInvoice);

        // Get all the nextInvoiceList where issueDate in
        defaultNextInvoiceFiltering("issueDate.in=" + DEFAULT_ISSUE_DATE + "," + UPDATED_ISSUE_DATE, "issueDate.in=" + UPDATED_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllNextInvoicesByIssueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoice = nextInvoiceRepository.saveAndFlush(nextInvoice);

        // Get all the nextInvoiceList where issueDate is not null
        defaultNextInvoiceFiltering("issueDate.specified=true", "issueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoicesByDueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoice = nextInvoiceRepository.saveAndFlush(nextInvoice);

        // Get all the nextInvoiceList where dueDate equals to
        defaultNextInvoiceFiltering("dueDate.equals=" + DEFAULT_DUE_DATE, "dueDate.equals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllNextInvoicesByDueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoice = nextInvoiceRepository.saveAndFlush(nextInvoice);

        // Get all the nextInvoiceList where dueDate in
        defaultNextInvoiceFiltering("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE, "dueDate.in=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllNextInvoicesByDueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoice = nextInvoiceRepository.saveAndFlush(nextInvoice);

        // Get all the nextInvoiceList where dueDate is not null
        defaultNextInvoiceFiltering("dueDate.specified=true", "dueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoicesByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoice = nextInvoiceRepository.saveAndFlush(nextInvoice);

        // Get all the nextInvoiceList where amount equals to
        defaultNextInvoiceFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoicesByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextInvoice = nextInvoiceRepository.saveAndFlush(nextInvoice);

        // Get all the nextInvoiceList where amount in
        defaultNextInvoiceFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoicesByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextInvoice = nextInvoiceRepository.saveAndFlush(nextInvoice);

        // Get all the nextInvoiceList where amount is not null
        defaultNextInvoiceFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllNextInvoicesByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoice = nextInvoiceRepository.saveAndFlush(nextInvoice);

        // Get all the nextInvoiceList where amount is greater than or equal to
        defaultNextInvoiceFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoicesByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextInvoice = nextInvoiceRepository.saveAndFlush(nextInvoice);

        // Get all the nextInvoiceList where amount is less than or equal to
        defaultNextInvoiceFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoicesByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextInvoice = nextInvoiceRepository.saveAndFlush(nextInvoice);

        // Get all the nextInvoiceList where amount is less than
        defaultNextInvoiceFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoicesByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextInvoice = nextInvoiceRepository.saveAndFlush(nextInvoice);

        // Get all the nextInvoiceList where amount is greater than
        defaultNextInvoiceFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNextInvoicesByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextInvoiceRepository.saveAndFlush(nextInvoice);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextInvoice.setTenant(tenant);
        nextInvoiceRepository.saveAndFlush(nextInvoice);
        Long tenantId = tenant.getId();
        // Get all the nextInvoiceList where tenant equals to tenantId
        defaultNextInvoiceShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextInvoiceList where tenant equals to (tenantId + 1)
        defaultNextInvoiceShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextInvoiceFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextInvoiceShouldBeFound(shouldBeFound);
        defaultNextInvoiceShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextInvoiceShouldBeFound(String filter) throws Exception {
        restNextInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextInvoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));

        // Check, that the count call also returns 1
        restNextInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextInvoiceShouldNotBeFound(String filter) throws Exception {
        restNextInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextInvoice() throws Exception {
        // Get the nextInvoice
        restNextInvoiceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextInvoice() throws Exception {
        // Initialize the database
        insertedNextInvoice = nextInvoiceRepository.saveAndFlush(nextInvoice);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextInvoice
        NextInvoice updatedNextInvoice = nextInvoiceRepository.findById(nextInvoice.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextInvoice are not directly saved in db
        em.detach(updatedNextInvoice);
        updatedNextInvoice
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);
        NextInvoiceDTO nextInvoiceDTO = nextInvoiceMapper.toDto(updatedNextInvoice);

        restNextInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextInvoiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextInvoiceDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextInvoice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextInvoiceToMatchAllProperties(updatedNextInvoice);
    }

    @Test
    @Transactional
    void putNonExistingNextInvoice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoice.setId(longCount.incrementAndGet());

        // Create the NextInvoice
        NextInvoiceDTO nextInvoiceDTO = nextInvoiceMapper.toDto(nextInvoice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextInvoiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextInvoiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextInvoice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoice.setId(longCount.incrementAndGet());

        // Create the NextInvoice
        NextInvoiceDTO nextInvoiceDTO = nextInvoiceMapper.toDto(nextInvoice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextInvoiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextInvoice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoice.setId(longCount.incrementAndGet());

        // Create the NextInvoice
        NextInvoiceDTO nextInvoiceDTO = nextInvoiceMapper.toDto(nextInvoice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextInvoiceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextInvoice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextInvoiceWithPatch() throws Exception {
        // Initialize the database
        insertedNextInvoice = nextInvoiceRepository.saveAndFlush(nextInvoice);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextInvoice using partial update
        NextInvoice partialUpdatedNextInvoice = new NextInvoice();
        partialUpdatedNextInvoice.setId(nextInvoice.getId());

        partialUpdatedNextInvoice.invoiceNumber(UPDATED_INVOICE_NUMBER).issueDate(UPDATED_ISSUE_DATE).amount(UPDATED_AMOUNT);

        restNextInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextInvoice.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextInvoice))
            )
            .andExpect(status().isOk());

        // Validate the NextInvoice in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextInvoiceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextInvoice, nextInvoice),
            getPersistedNextInvoice(nextInvoice)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextInvoiceWithPatch() throws Exception {
        // Initialize the database
        insertedNextInvoice = nextInvoiceRepository.saveAndFlush(nextInvoice);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextInvoice using partial update
        NextInvoice partialUpdatedNextInvoice = new NextInvoice();
        partialUpdatedNextInvoice.setId(nextInvoice.getId());

        partialUpdatedNextInvoice
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);

        restNextInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextInvoice.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextInvoice))
            )
            .andExpect(status().isOk());

        // Validate the NextInvoice in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextInvoiceUpdatableFieldsEquals(partialUpdatedNextInvoice, getPersistedNextInvoice(partialUpdatedNextInvoice));
    }

    @Test
    @Transactional
    void patchNonExistingNextInvoice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoice.setId(longCount.incrementAndGet());

        // Create the NextInvoice
        NextInvoiceDTO nextInvoiceDTO = nextInvoiceMapper.toDto(nextInvoice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextInvoiceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextInvoiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextInvoice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoice.setId(longCount.incrementAndGet());

        // Create the NextInvoice
        NextInvoiceDTO nextInvoiceDTO = nextInvoiceMapper.toDto(nextInvoice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextInvoiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextInvoice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextInvoice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextInvoice.setId(longCount.incrementAndGet());

        // Create the NextInvoice
        NextInvoiceDTO nextInvoiceDTO = nextInvoiceMapper.toDto(nextInvoice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextInvoiceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextInvoiceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextInvoice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextInvoice() throws Exception {
        // Initialize the database
        insertedNextInvoice = nextInvoiceRepository.saveAndFlush(nextInvoice);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextInvoice
        restNextInvoiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextInvoice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextInvoiceRepository.count();
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

    protected NextInvoice getPersistedNextInvoice(NextInvoice nextInvoice) {
        return nextInvoiceRepository.findById(nextInvoice.getId()).orElseThrow();
    }

    protected void assertPersistedNextInvoiceToMatchAllProperties(NextInvoice expectedNextInvoice) {
        assertNextInvoiceAllPropertiesEquals(expectedNextInvoice, getPersistedNextInvoice(expectedNextInvoice));
    }

    protected void assertPersistedNextInvoiceToMatchUpdatableProperties(NextInvoice expectedNextInvoice) {
        assertNextInvoiceAllUpdatablePropertiesEquals(expectedNextInvoice, getPersistedNextInvoice(expectedNextInvoice));
    }
}
