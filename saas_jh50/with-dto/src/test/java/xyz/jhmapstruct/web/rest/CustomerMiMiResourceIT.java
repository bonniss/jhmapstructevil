package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.CustomerMiMiAsserts.*;
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
import xyz.jhmapstruct.domain.CustomerMiMi;
import xyz.jhmapstruct.repository.CustomerMiMiRepository;
import xyz.jhmapstruct.service.dto.CustomerMiMiDTO;
import xyz.jhmapstruct.service.mapper.CustomerMiMiMapper;

/**
 * Integration tests for the {@link CustomerMiMiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomerMiMiResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/customer-mi-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CustomerMiMiRepository customerMiMiRepository;

    @Autowired
    private CustomerMiMiMapper customerMiMiMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerMiMiMockMvc;

    private CustomerMiMi customerMiMi;

    private CustomerMiMi insertedCustomerMiMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerMiMi createEntity() {
        return new CustomerMiMi()
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
    public static CustomerMiMi createUpdatedEntity() {
        return new CustomerMiMi()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        customerMiMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCustomerMiMi != null) {
            customerMiMiRepository.delete(insertedCustomerMiMi);
            insertedCustomerMiMi = null;
        }
    }

    @Test
    @Transactional
    void createCustomerMiMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CustomerMiMi
        CustomerMiMiDTO customerMiMiDTO = customerMiMiMapper.toDto(customerMiMi);
        var returnedCustomerMiMiDTO = om.readValue(
            restCustomerMiMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerMiMiDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CustomerMiMiDTO.class
        );

        // Validate the CustomerMiMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCustomerMiMi = customerMiMiMapper.toEntity(returnedCustomerMiMiDTO);
        assertCustomerMiMiUpdatableFieldsEquals(returnedCustomerMiMi, getPersistedCustomerMiMi(returnedCustomerMiMi));

        insertedCustomerMiMi = returnedCustomerMiMi;
    }

    @Test
    @Transactional
    void createCustomerMiMiWithExistingId() throws Exception {
        // Create the CustomerMiMi with an existing ID
        customerMiMi.setId(1L);
        CustomerMiMiDTO customerMiMiDTO = customerMiMiMapper.toDto(customerMiMi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerMiMiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerMiMi.setFirstName(null);

        // Create the CustomerMiMi, which fails.
        CustomerMiMiDTO customerMiMiDTO = customerMiMiMapper.toDto(customerMiMi);

        restCustomerMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerMiMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerMiMi.setLastName(null);

        // Create the CustomerMiMi, which fails.
        CustomerMiMiDTO customerMiMiDTO = customerMiMiMapper.toDto(customerMiMi);

        restCustomerMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerMiMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerMiMi.setEmail(null);

        // Create the CustomerMiMi, which fails.
        CustomerMiMiDTO customerMiMiDTO = customerMiMiMapper.toDto(customerMiMi);

        restCustomerMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerMiMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustomerMiMis() throws Exception {
        // Initialize the database
        insertedCustomerMiMi = customerMiMiRepository.saveAndFlush(customerMiMi);

        // Get all the customerMiMiList
        restCustomerMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerMiMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @Test
    @Transactional
    void getCustomerMiMi() throws Exception {
        // Initialize the database
        insertedCustomerMiMi = customerMiMiRepository.saveAndFlush(customerMiMi);

        // Get the customerMiMi
        restCustomerMiMiMockMvc
            .perform(get(ENTITY_API_URL_ID, customerMiMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customerMiMi.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getNonExistingCustomerMiMi() throws Exception {
        // Get the customerMiMi
        restCustomerMiMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomerMiMi() throws Exception {
        // Initialize the database
        insertedCustomerMiMi = customerMiMiRepository.saveAndFlush(customerMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerMiMi
        CustomerMiMi updatedCustomerMiMi = customerMiMiRepository.findById(customerMiMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCustomerMiMi are not directly saved in db
        em.detach(updatedCustomerMiMi);
        updatedCustomerMiMi
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        CustomerMiMiDTO customerMiMiDTO = customerMiMiMapper.toDto(updatedCustomerMiMi);

        restCustomerMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerMiMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerMiMiDTO))
            )
            .andExpect(status().isOk());

        // Validate the CustomerMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCustomerMiMiToMatchAllProperties(updatedCustomerMiMi);
    }

    @Test
    @Transactional
    void putNonExistingCustomerMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerMiMi.setId(longCount.incrementAndGet());

        // Create the CustomerMiMi
        CustomerMiMiDTO customerMiMiDTO = customerMiMiMapper.toDto(customerMiMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerMiMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomerMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerMiMi.setId(longCount.incrementAndGet());

        // Create the CustomerMiMi
        CustomerMiMiDTO customerMiMiDTO = customerMiMiMapper.toDto(customerMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomerMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerMiMi.setId(longCount.incrementAndGet());

        // Create the CustomerMiMi
        CustomerMiMiDTO customerMiMiDTO = customerMiMiMapper.toDto(customerMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMiMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerMiMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedCustomerMiMi = customerMiMiRepository.saveAndFlush(customerMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerMiMi using partial update
        CustomerMiMi partialUpdatedCustomerMiMi = new CustomerMiMi();
        partialUpdatedCustomerMiMi.setId(customerMiMi.getId());

        partialUpdatedCustomerMiMi.email(UPDATED_EMAIL);

        restCustomerMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomerMiMi))
            )
            .andExpect(status().isOk());

        // Validate the CustomerMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomerMiMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCustomerMiMi, customerMiMi),
            getPersistedCustomerMiMi(customerMiMi)
        );
    }

    @Test
    @Transactional
    void fullUpdateCustomerMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedCustomerMiMi = customerMiMiRepository.saveAndFlush(customerMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerMiMi using partial update
        CustomerMiMi partialUpdatedCustomerMiMi = new CustomerMiMi();
        partialUpdatedCustomerMiMi.setId(customerMiMi.getId());

        partialUpdatedCustomerMiMi
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restCustomerMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomerMiMi))
            )
            .andExpect(status().isOk());

        // Validate the CustomerMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomerMiMiUpdatableFieldsEquals(partialUpdatedCustomerMiMi, getPersistedCustomerMiMi(partialUpdatedCustomerMiMi));
    }

    @Test
    @Transactional
    void patchNonExistingCustomerMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerMiMi.setId(longCount.incrementAndGet());

        // Create the CustomerMiMi
        CustomerMiMiDTO customerMiMiDTO = customerMiMiMapper.toDto(customerMiMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerMiMiDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomerMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerMiMi.setId(longCount.incrementAndGet());

        // Create the CustomerMiMi
        CustomerMiMiDTO customerMiMiDTO = customerMiMiMapper.toDto(customerMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomerMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerMiMi.setId(longCount.incrementAndGet());

        // Create the CustomerMiMi
        CustomerMiMiDTO customerMiMiDTO = customerMiMiMapper.toDto(customerMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMiMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(customerMiMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomerMiMi() throws Exception {
        // Initialize the database
        insertedCustomerMiMi = customerMiMiRepository.saveAndFlush(customerMiMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the customerMiMi
        restCustomerMiMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, customerMiMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return customerMiMiRepository.count();
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

    protected CustomerMiMi getPersistedCustomerMiMi(CustomerMiMi customerMiMi) {
        return customerMiMiRepository.findById(customerMiMi.getId()).orElseThrow();
    }

    protected void assertPersistedCustomerMiMiToMatchAllProperties(CustomerMiMi expectedCustomerMiMi) {
        assertCustomerMiMiAllPropertiesEquals(expectedCustomerMiMi, getPersistedCustomerMiMi(expectedCustomerMiMi));
    }

    protected void assertPersistedCustomerMiMiToMatchUpdatableProperties(CustomerMiMi expectedCustomerMiMi) {
        assertCustomerMiMiAllUpdatablePropertiesEquals(expectedCustomerMiMi, getPersistedCustomerMiMi(expectedCustomerMiMi));
    }
}
