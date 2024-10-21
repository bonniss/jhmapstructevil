package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.InvoiceViViAsserts.*;
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
import xyz.jhmapstruct.domain.InvoiceViVi;
import xyz.jhmapstruct.repository.InvoiceViViRepository;
import xyz.jhmapstruct.service.dto.InvoiceViViDTO;
import xyz.jhmapstruct.service.mapper.InvoiceViViMapper;

/**
 * Integration tests for the {@link InvoiceViViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InvoiceViViResourceIT {

    private static final String DEFAULT_INVOICE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_ISSUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ISSUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/invoice-vi-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InvoiceViViRepository invoiceViViRepository;

    @Autowired
    private InvoiceViViMapper invoiceViViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceViViMockMvc;

    private InvoiceViVi invoiceViVi;

    private InvoiceViVi insertedInvoiceViVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceViVi createEntity() {
        return new InvoiceViVi()
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
    public static InvoiceViVi createUpdatedEntity() {
        return new InvoiceViVi()
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);
    }

    @BeforeEach
    public void initTest() {
        invoiceViVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedInvoiceViVi != null) {
            invoiceViViRepository.delete(insertedInvoiceViVi);
            insertedInvoiceViVi = null;
        }
    }

    @Test
    @Transactional
    void createInvoiceViVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InvoiceViVi
        InvoiceViViDTO invoiceViViDTO = invoiceViViMapper.toDto(invoiceViVi);
        var returnedInvoiceViViDTO = om.readValue(
            restInvoiceViViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceViViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InvoiceViViDTO.class
        );

        // Validate the InvoiceViVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedInvoiceViVi = invoiceViViMapper.toEntity(returnedInvoiceViViDTO);
        assertInvoiceViViUpdatableFieldsEquals(returnedInvoiceViVi, getPersistedInvoiceViVi(returnedInvoiceViVi));

        insertedInvoiceViVi = returnedInvoiceViVi;
    }

    @Test
    @Transactional
    void createInvoiceViViWithExistingId() throws Exception {
        // Create the InvoiceViVi with an existing ID
        invoiceViVi.setId(1L);
        InvoiceViViDTO invoiceViViDTO = invoiceViViMapper.toDto(invoiceViVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceViViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkInvoiceNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceViVi.setInvoiceNumber(null);

        // Create the InvoiceViVi, which fails.
        InvoiceViViDTO invoiceViViDTO = invoiceViViMapper.toDto(invoiceViVi);

        restInvoiceViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceViViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIssueDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceViVi.setIssueDate(null);

        // Create the InvoiceViVi, which fails.
        InvoiceViViDTO invoiceViViDTO = invoiceViViMapper.toDto(invoiceViVi);

        restInvoiceViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceViViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceViVi.setAmount(null);

        // Create the InvoiceViVi, which fails.
        InvoiceViViDTO invoiceViViDTO = invoiceViViMapper.toDto(invoiceViVi);

        restInvoiceViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceViViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInvoiceViVis() throws Exception {
        // Initialize the database
        insertedInvoiceViVi = invoiceViViRepository.saveAndFlush(invoiceViVi);

        // Get all the invoiceViViList
        restInvoiceViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceViVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));
    }

    @Test
    @Transactional
    void getInvoiceViVi() throws Exception {
        // Initialize the database
        insertedInvoiceViVi = invoiceViViRepository.saveAndFlush(invoiceViVi);

        // Get the invoiceViVi
        restInvoiceViViMockMvc
            .perform(get(ENTITY_API_URL_ID, invoiceViVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceViVi.getId().intValue()))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER))
            .andExpect(jsonPath("$.issueDate").value(DEFAULT_ISSUE_DATE.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)));
    }

    @Test
    @Transactional
    void getNonExistingInvoiceViVi() throws Exception {
        // Get the invoiceViVi
        restInvoiceViViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInvoiceViVi() throws Exception {
        // Initialize the database
        insertedInvoiceViVi = invoiceViViRepository.saveAndFlush(invoiceViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceViVi
        InvoiceViVi updatedInvoiceViVi = invoiceViViRepository.findById(invoiceViVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInvoiceViVi are not directly saved in db
        em.detach(updatedInvoiceViVi);
        updatedInvoiceViVi
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);
        InvoiceViViDTO invoiceViViDTO = invoiceViViMapper.toDto(updatedInvoiceViVi);

        restInvoiceViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceViViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceViViDTO))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInvoiceViViToMatchAllProperties(updatedInvoiceViVi);
    }

    @Test
    @Transactional
    void putNonExistingInvoiceViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceViVi.setId(longCount.incrementAndGet());

        // Create the InvoiceViVi
        InvoiceViViDTO invoiceViViDTO = invoiceViViMapper.toDto(invoiceViVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceViViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoiceViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceViVi.setId(longCount.incrementAndGet());

        // Create the InvoiceViVi
        InvoiceViViDTO invoiceViViDTO = invoiceViViMapper.toDto(invoiceViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoiceViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceViVi.setId(longCount.incrementAndGet());

        // Create the InvoiceViVi
        InvoiceViViDTO invoiceViViDTO = invoiceViViMapper.toDto(invoiceViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceViViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceViViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceViViWithPatch() throws Exception {
        // Initialize the database
        insertedInvoiceViVi = invoiceViViRepository.saveAndFlush(invoiceViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceViVi using partial update
        InvoiceViVi partialUpdatedInvoiceViVi = new InvoiceViVi();
        partialUpdatedInvoiceViVi.setId(invoiceViVi.getId());

        partialUpdatedInvoiceViVi.dueDate(UPDATED_DUE_DATE).amount(UPDATED_AMOUNT);

        restInvoiceViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoiceViVi))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceViViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInvoiceViVi, invoiceViVi),
            getPersistedInvoiceViVi(invoiceViVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateInvoiceViViWithPatch() throws Exception {
        // Initialize the database
        insertedInvoiceViVi = invoiceViViRepository.saveAndFlush(invoiceViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceViVi using partial update
        InvoiceViVi partialUpdatedInvoiceViVi = new InvoiceViVi();
        partialUpdatedInvoiceViVi.setId(invoiceViVi.getId());

        partialUpdatedInvoiceViVi
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);

        restInvoiceViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoiceViVi))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceViViUpdatableFieldsEquals(partialUpdatedInvoiceViVi, getPersistedInvoiceViVi(partialUpdatedInvoiceViVi));
    }

    @Test
    @Transactional
    void patchNonExistingInvoiceViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceViVi.setId(longCount.incrementAndGet());

        // Create the InvoiceViVi
        InvoiceViViDTO invoiceViViDTO = invoiceViViMapper.toDto(invoiceViVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoiceViViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoiceViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoiceViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceViVi.setId(longCount.incrementAndGet());

        // Create the InvoiceViVi
        InvoiceViViDTO invoiceViViDTO = invoiceViViMapper.toDto(invoiceViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoiceViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoiceViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceViVi.setId(longCount.incrementAndGet());

        // Create the InvoiceViVi
        InvoiceViViDTO invoiceViViDTO = invoiceViViMapper.toDto(invoiceViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceViViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(invoiceViViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvoiceViVi() throws Exception {
        // Initialize the database
        insertedInvoiceViVi = invoiceViViRepository.saveAndFlush(invoiceViVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the invoiceViVi
        restInvoiceViViMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoiceViVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return invoiceViViRepository.count();
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

    protected InvoiceViVi getPersistedInvoiceViVi(InvoiceViVi invoiceViVi) {
        return invoiceViViRepository.findById(invoiceViVi.getId()).orElseThrow();
    }

    protected void assertPersistedInvoiceViViToMatchAllProperties(InvoiceViVi expectedInvoiceViVi) {
        assertInvoiceViViAllPropertiesEquals(expectedInvoiceViVi, getPersistedInvoiceViVi(expectedInvoiceViVi));
    }

    protected void assertPersistedInvoiceViViToMatchUpdatableProperties(InvoiceViVi expectedInvoiceViVi) {
        assertInvoiceViViAllUpdatablePropertiesEquals(expectedInvoiceViVi, getPersistedInvoiceViVi(expectedInvoiceViVi));
    }
}
