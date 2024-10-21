package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.CustomerMiAsserts.*;
import static xyz.jhmapstruct.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
import xyz.jhmapstruct.domain.CustomerMi;
import xyz.jhmapstruct.repository.CustomerMiRepository;

/**
 * Integration tests for the {@link CustomerMiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomerMiResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/customer-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CustomerMiRepository customerMiRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerMiMockMvc;

    private CustomerMi customerMi;

    private CustomerMi insertedCustomerMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerMi createEntity() {
        return new CustomerMi()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerMi createUpdatedEntity() {
        return new CustomerMi()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        customerMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCustomerMi != null) {
            customerMiRepository.delete(insertedCustomerMi);
            insertedCustomerMi = null;
        }
    }

    @Test
    @Transactional
    void createCustomerMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CustomerMi
        var returnedCustomerMi = om.readValue(
            restCustomerMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerMi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CustomerMi.class
        );

        // Validate the CustomerMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCustomerMiUpdatableFieldsEquals(returnedCustomerMi, getPersistedCustomerMi(returnedCustomerMi));

        insertedCustomerMi = returnedCustomerMi;
    }

    @Test
    @Transactional
    void createCustomerMiWithExistingId() throws Exception {
        // Create the CustomerMi with an existing ID
        customerMi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerMi)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerMi.setFirstName(null);

        // Create the CustomerMi, which fails.

        restCustomerMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerMi.setLastName(null);

        // Create the CustomerMi, which fails.

        restCustomerMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerMi.setEmail(null);

        // Create the CustomerMi, which fails.

        restCustomerMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustomerMis() throws Exception {
        // Initialize the database
        insertedCustomerMi = customerMiRepository.saveAndFlush(customerMi);

        // Get all the customerMiList
        restCustomerMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @Test
    @Transactional
    void getCustomerMi() throws Exception {
        // Initialize the database
        insertedCustomerMi = customerMiRepository.saveAndFlush(customerMi);

        // Get the customerMi
        restCustomerMiMockMvc
            .perform(get(ENTITY_API_URL_ID, customerMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customerMi.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getNonExistingCustomerMi() throws Exception {
        // Get the customerMi
        restCustomerMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomerMi() throws Exception {
        // Initialize the database
        insertedCustomerMi = customerMiRepository.saveAndFlush(customerMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerMi
        CustomerMi updatedCustomerMi = customerMiRepository.findById(customerMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCustomerMi are not directly saved in db
        em.detach(updatedCustomerMi);
        updatedCustomerMi.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME).email(UPDATED_EMAIL).phoneNumber(UPDATED_PHONE_NUMBER);

        restCustomerMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCustomerMi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCustomerMi))
            )
            .andExpect(status().isOk());

        // Validate the CustomerMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCustomerMiToMatchAllProperties(updatedCustomerMi);
    }

    @Test
    @Transactional
    void putNonExistingCustomerMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerMi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerMi.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomerMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomerMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerMi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerMiWithPatch() throws Exception {
        // Initialize the database
        insertedCustomerMi = customerMiRepository.saveAndFlush(customerMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerMi using partial update
        CustomerMi partialUpdatedCustomerMi = new CustomerMi();
        partialUpdatedCustomerMi.setId(customerMi.getId());

        partialUpdatedCustomerMi.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME);

        restCustomerMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomerMi))
            )
            .andExpect(status().isOk());

        // Validate the CustomerMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomerMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCustomerMi, customerMi),
            getPersistedCustomerMi(customerMi)
        );
    }

    @Test
    @Transactional
    void fullUpdateCustomerMiWithPatch() throws Exception {
        // Initialize the database
        insertedCustomerMi = customerMiRepository.saveAndFlush(customerMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerMi using partial update
        CustomerMi partialUpdatedCustomerMi = new CustomerMi();
        partialUpdatedCustomerMi.setId(customerMi.getId());

        partialUpdatedCustomerMi
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restCustomerMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomerMi))
            )
            .andExpect(status().isOk());

        // Validate the CustomerMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomerMiUpdatableFieldsEquals(partialUpdatedCustomerMi, getPersistedCustomerMi(partialUpdatedCustomerMi));
    }

    @Test
    @Transactional
    void patchNonExistingCustomerMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerMi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomerMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomerMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(customerMi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomerMi() throws Exception {
        // Initialize the database
        insertedCustomerMi = customerMiRepository.saveAndFlush(customerMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the customerMi
        restCustomerMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, customerMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return customerMiRepository.count();
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

    protected CustomerMi getPersistedCustomerMi(CustomerMi customerMi) {
        return customerMiRepository.findById(customerMi.getId()).orElseThrow();
    }

    protected void assertPersistedCustomerMiToMatchAllProperties(CustomerMi expectedCustomerMi) {
        assertCustomerMiAllPropertiesEquals(expectedCustomerMi, getPersistedCustomerMi(expectedCustomerMi));
    }

    protected void assertPersistedCustomerMiToMatchUpdatableProperties(CustomerMi expectedCustomerMi) {
        assertCustomerMiAllUpdatablePropertiesEquals(expectedCustomerMi, getPersistedCustomerMi(expectedCustomerMi));
    }
}
