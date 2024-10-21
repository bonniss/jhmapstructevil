package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.InvoiceViAsserts.*;
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
import xyz.jhmapstruct.domain.InvoiceVi;
import xyz.jhmapstruct.repository.InvoiceViRepository;
import xyz.jhmapstruct.service.dto.InvoiceViDTO;
import xyz.jhmapstruct.service.mapper.InvoiceViMapper;

/**
 * Integration tests for the {@link InvoiceViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InvoiceViResourceIT {

    private static final String DEFAULT_INVOICE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_ISSUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ISSUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/invoice-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InvoiceViRepository invoiceViRepository;

    @Autowired
    private InvoiceViMapper invoiceViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceViMockMvc;

    private InvoiceVi invoiceVi;

    private InvoiceVi insertedInvoiceVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceVi createEntity() {
        return new InvoiceVi()
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
    public static InvoiceVi createUpdatedEntity() {
        return new InvoiceVi()
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);
    }

    @BeforeEach
    public void initTest() {
        invoiceVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedInvoiceVi != null) {
            invoiceViRepository.delete(insertedInvoiceVi);
            insertedInvoiceVi = null;
        }
    }

    @Test
    @Transactional
    void createInvoiceVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InvoiceVi
        InvoiceViDTO invoiceViDTO = invoiceViMapper.toDto(invoiceVi);
        var returnedInvoiceViDTO = om.readValue(
            restInvoiceViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InvoiceViDTO.class
        );

        // Validate the InvoiceVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedInvoiceVi = invoiceViMapper.toEntity(returnedInvoiceViDTO);
        assertInvoiceViUpdatableFieldsEquals(returnedInvoiceVi, getPersistedInvoiceVi(returnedInvoiceVi));

        insertedInvoiceVi = returnedInvoiceVi;
    }

    @Test
    @Transactional
    void createInvoiceViWithExistingId() throws Exception {
        // Create the InvoiceVi with an existing ID
        invoiceVi.setId(1L);
        InvoiceViDTO invoiceViDTO = invoiceViMapper.toDto(invoiceVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkInvoiceNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceVi.setInvoiceNumber(null);

        // Create the InvoiceVi, which fails.
        InvoiceViDTO invoiceViDTO = invoiceViMapper.toDto(invoiceVi);

        restInvoiceViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIssueDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceVi.setIssueDate(null);

        // Create the InvoiceVi, which fails.
        InvoiceViDTO invoiceViDTO = invoiceViMapper.toDto(invoiceVi);

        restInvoiceViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceVi.setAmount(null);

        // Create the InvoiceVi, which fails.
        InvoiceViDTO invoiceViDTO = invoiceViMapper.toDto(invoiceVi);

        restInvoiceViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInvoiceVis() throws Exception {
        // Initialize the database
        insertedInvoiceVi = invoiceViRepository.saveAndFlush(invoiceVi);

        // Get all the invoiceViList
        restInvoiceViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));
    }

    @Test
    @Transactional
    void getInvoiceVi() throws Exception {
        // Initialize the database
        insertedInvoiceVi = invoiceViRepository.saveAndFlush(invoiceVi);

        // Get the invoiceVi
        restInvoiceViMockMvc
            .perform(get(ENTITY_API_URL_ID, invoiceVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceVi.getId().intValue()))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER))
            .andExpect(jsonPath("$.issueDate").value(DEFAULT_ISSUE_DATE.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)));
    }

    @Test
    @Transactional
    void getNonExistingInvoiceVi() throws Exception {
        // Get the invoiceVi
        restInvoiceViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInvoiceVi() throws Exception {
        // Initialize the database
        insertedInvoiceVi = invoiceViRepository.saveAndFlush(invoiceVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceVi
        InvoiceVi updatedInvoiceVi = invoiceViRepository.findById(invoiceVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInvoiceVi are not directly saved in db
        em.detach(updatedInvoiceVi);
        updatedInvoiceVi
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);
        InvoiceViDTO invoiceViDTO = invoiceViMapper.toDto(updatedInvoiceVi);

        restInvoiceViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceViDTO))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInvoiceViToMatchAllProperties(updatedInvoiceVi);
    }

    @Test
    @Transactional
    void putNonExistingInvoiceVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceVi.setId(longCount.incrementAndGet());

        // Create the InvoiceVi
        InvoiceViDTO invoiceViDTO = invoiceViMapper.toDto(invoiceVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoiceVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceVi.setId(longCount.incrementAndGet());

        // Create the InvoiceVi
        InvoiceViDTO invoiceViDTO = invoiceViMapper.toDto(invoiceVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoiceVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceVi.setId(longCount.incrementAndGet());

        // Create the InvoiceVi
        InvoiceViDTO invoiceViDTO = invoiceViMapper.toDto(invoiceVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceViWithPatch() throws Exception {
        // Initialize the database
        insertedInvoiceVi = invoiceViRepository.saveAndFlush(invoiceVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceVi using partial update
        InvoiceVi partialUpdatedInvoiceVi = new InvoiceVi();
        partialUpdatedInvoiceVi.setId(invoiceVi.getId());

        partialUpdatedInvoiceVi
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);

        restInvoiceViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoiceVi))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInvoiceVi, invoiceVi),
            getPersistedInvoiceVi(invoiceVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateInvoiceViWithPatch() throws Exception {
        // Initialize the database
        insertedInvoiceVi = invoiceViRepository.saveAndFlush(invoiceVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceVi using partial update
        InvoiceVi partialUpdatedInvoiceVi = new InvoiceVi();
        partialUpdatedInvoiceVi.setId(invoiceVi.getId());

        partialUpdatedInvoiceVi
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);

        restInvoiceViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoiceVi))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceViUpdatableFieldsEquals(partialUpdatedInvoiceVi, getPersistedInvoiceVi(partialUpdatedInvoiceVi));
    }

    @Test
    @Transactional
    void patchNonExistingInvoiceVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceVi.setId(longCount.incrementAndGet());

        // Create the InvoiceVi
        InvoiceViDTO invoiceViDTO = invoiceViMapper.toDto(invoiceVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoiceViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoiceViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoiceVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceVi.setId(longCount.incrementAndGet());

        // Create the InvoiceVi
        InvoiceViDTO invoiceViDTO = invoiceViMapper.toDto(invoiceVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoiceViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoiceVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceVi.setId(longCount.incrementAndGet());

        // Create the InvoiceVi
        InvoiceViDTO invoiceViDTO = invoiceViMapper.toDto(invoiceVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(invoiceViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvoiceVi() throws Exception {
        // Initialize the database
        insertedInvoiceVi = invoiceViRepository.saveAndFlush(invoiceVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the invoiceVi
        restInvoiceViMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoiceVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return invoiceViRepository.count();
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

    protected InvoiceVi getPersistedInvoiceVi(InvoiceVi invoiceVi) {
        return invoiceViRepository.findById(invoiceVi.getId()).orElseThrow();
    }

    protected void assertPersistedInvoiceViToMatchAllProperties(InvoiceVi expectedInvoiceVi) {
        assertInvoiceViAllPropertiesEquals(expectedInvoiceVi, getPersistedInvoiceVi(expectedInvoiceVi));
    }

    protected void assertPersistedInvoiceViToMatchUpdatableProperties(InvoiceVi expectedInvoiceVi) {
        assertInvoiceViAllUpdatablePropertiesEquals(expectedInvoiceVi, getPersistedInvoiceVi(expectedInvoiceVi));
    }
}
