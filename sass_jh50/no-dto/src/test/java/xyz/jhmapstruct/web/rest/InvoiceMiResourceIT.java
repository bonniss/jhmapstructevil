package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.InvoiceMiAsserts.*;
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
import xyz.jhmapstruct.domain.InvoiceMi;
import xyz.jhmapstruct.repository.InvoiceMiRepository;

/**
 * Integration tests for the {@link InvoiceMiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InvoiceMiResourceIT {

    private static final String DEFAULT_INVOICE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_ISSUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ISSUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/invoice-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InvoiceMiRepository invoiceMiRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceMiMockMvc;

    private InvoiceMi invoiceMi;

    private InvoiceMi insertedInvoiceMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceMi createEntity() {
        return new InvoiceMi()
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
    public static InvoiceMi createUpdatedEntity() {
        return new InvoiceMi()
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);
    }

    @BeforeEach
    public void initTest() {
        invoiceMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedInvoiceMi != null) {
            invoiceMiRepository.delete(insertedInvoiceMi);
            insertedInvoiceMi = null;
        }
    }

    @Test
    @Transactional
    void createInvoiceMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InvoiceMi
        var returnedInvoiceMi = om.readValue(
            restInvoiceMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceMi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InvoiceMi.class
        );

        // Validate the InvoiceMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInvoiceMiUpdatableFieldsEquals(returnedInvoiceMi, getPersistedInvoiceMi(returnedInvoiceMi));

        insertedInvoiceMi = returnedInvoiceMi;
    }

    @Test
    @Transactional
    void createInvoiceMiWithExistingId() throws Exception {
        // Create the InvoiceMi with an existing ID
        invoiceMi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceMi)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkInvoiceNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceMi.setInvoiceNumber(null);

        // Create the InvoiceMi, which fails.

        restInvoiceMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIssueDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceMi.setIssueDate(null);

        // Create the InvoiceMi, which fails.

        restInvoiceMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceMi.setAmount(null);

        // Create the InvoiceMi, which fails.

        restInvoiceMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInvoiceMis() throws Exception {
        // Initialize the database
        insertedInvoiceMi = invoiceMiRepository.saveAndFlush(invoiceMi);

        // Get all the invoiceMiList
        restInvoiceMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));
    }

    @Test
    @Transactional
    void getInvoiceMi() throws Exception {
        // Initialize the database
        insertedInvoiceMi = invoiceMiRepository.saveAndFlush(invoiceMi);

        // Get the invoiceMi
        restInvoiceMiMockMvc
            .perform(get(ENTITY_API_URL_ID, invoiceMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceMi.getId().intValue()))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER))
            .andExpect(jsonPath("$.issueDate").value(DEFAULT_ISSUE_DATE.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)));
    }

    @Test
    @Transactional
    void getNonExistingInvoiceMi() throws Exception {
        // Get the invoiceMi
        restInvoiceMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInvoiceMi() throws Exception {
        // Initialize the database
        insertedInvoiceMi = invoiceMiRepository.saveAndFlush(invoiceMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceMi
        InvoiceMi updatedInvoiceMi = invoiceMiRepository.findById(invoiceMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInvoiceMi are not directly saved in db
        em.detach(updatedInvoiceMi);
        updatedInvoiceMi
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);

        restInvoiceMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInvoiceMi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInvoiceMi))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInvoiceMiToMatchAllProperties(updatedInvoiceMi);
    }

    @Test
    @Transactional
    void putNonExistingInvoiceMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceMi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceMi.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoiceMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoiceMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceMi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceMiWithPatch() throws Exception {
        // Initialize the database
        insertedInvoiceMi = invoiceMiRepository.saveAndFlush(invoiceMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceMi using partial update
        InvoiceMi partialUpdatedInvoiceMi = new InvoiceMi();
        partialUpdatedInvoiceMi.setId(invoiceMi.getId());

        partialUpdatedInvoiceMi.invoiceNumber(UPDATED_INVOICE_NUMBER).issueDate(UPDATED_ISSUE_DATE).dueDate(UPDATED_DUE_DATE);

        restInvoiceMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoiceMi))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInvoiceMi, invoiceMi),
            getPersistedInvoiceMi(invoiceMi)
        );
    }

    @Test
    @Transactional
    void fullUpdateInvoiceMiWithPatch() throws Exception {
        // Initialize the database
        insertedInvoiceMi = invoiceMiRepository.saveAndFlush(invoiceMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceMi using partial update
        InvoiceMi partialUpdatedInvoiceMi = new InvoiceMi();
        partialUpdatedInvoiceMi.setId(invoiceMi.getId());

        partialUpdatedInvoiceMi
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);

        restInvoiceMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoiceMi))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceMiUpdatableFieldsEquals(partialUpdatedInvoiceMi, getPersistedInvoiceMi(partialUpdatedInvoiceMi));
    }

    @Test
    @Transactional
    void patchNonExistingInvoiceMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceMi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoiceMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoiceMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoiceMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoiceMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoiceMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(invoiceMi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvoiceMi() throws Exception {
        // Initialize the database
        insertedInvoiceMi = invoiceMiRepository.saveAndFlush(invoiceMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the invoiceMi
        restInvoiceMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoiceMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return invoiceMiRepository.count();
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

    protected InvoiceMi getPersistedInvoiceMi(InvoiceMi invoiceMi) {
        return invoiceMiRepository.findById(invoiceMi.getId()).orElseThrow();
    }

    protected void assertPersistedInvoiceMiToMatchAllProperties(InvoiceMi expectedInvoiceMi) {
        assertInvoiceMiAllPropertiesEquals(expectedInvoiceMi, getPersistedInvoiceMi(expectedInvoiceMi));
    }

    protected void assertPersistedInvoiceMiToMatchUpdatableProperties(InvoiceMi expectedInvoiceMi) {
        assertInvoiceMiAllUpdatablePropertiesEquals(expectedInvoiceMi, getPersistedInvoiceMi(expectedInvoiceMi));
    }
}