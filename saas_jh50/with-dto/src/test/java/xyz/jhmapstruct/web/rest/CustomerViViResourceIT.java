package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.CustomerViViAsserts.*;
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
import xyz.jhmapstruct.domain.CustomerViVi;
import xyz.jhmapstruct.repository.CustomerViViRepository;
import xyz.jhmapstruct.service.dto.CustomerViViDTO;
import xyz.jhmapstruct.service.mapper.CustomerViViMapper;

/**
 * Integration tests for the {@link CustomerViViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomerViViResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/customer-vi-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CustomerViViRepository customerViViRepository;

    @Autowired
    private CustomerViViMapper customerViViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerViViMockMvc;

    private CustomerViVi customerViVi;

    private CustomerViVi insertedCustomerViVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerViVi createEntity() {
        return new CustomerViVi()
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
    public static CustomerViVi createUpdatedEntity() {
        return new CustomerViVi()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        customerViVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCustomerViVi != null) {
            customerViViRepository.delete(insertedCustomerViVi);
            insertedCustomerViVi = null;
        }
    }

    @Test
    @Transactional
    void createCustomerViVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CustomerViVi
        CustomerViViDTO customerViViDTO = customerViViMapper.toDto(customerViVi);
        var returnedCustomerViViDTO = om.readValue(
            restCustomerViViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerViViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CustomerViViDTO.class
        );

        // Validate the CustomerViVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCustomerViVi = customerViViMapper.toEntity(returnedCustomerViViDTO);
        assertCustomerViViUpdatableFieldsEquals(returnedCustomerViVi, getPersistedCustomerViVi(returnedCustomerViVi));

        insertedCustomerViVi = returnedCustomerViVi;
    }

    @Test
    @Transactional
    void createCustomerViViWithExistingId() throws Exception {
        // Create the CustomerViVi with an existing ID
        customerViVi.setId(1L);
        CustomerViViDTO customerViViDTO = customerViViMapper.toDto(customerViVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerViViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerViVi.setFirstName(null);

        // Create the CustomerViVi, which fails.
        CustomerViViDTO customerViViDTO = customerViViMapper.toDto(customerViVi);

        restCustomerViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerViViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerViVi.setLastName(null);

        // Create the CustomerViVi, which fails.
        CustomerViViDTO customerViViDTO = customerViViMapper.toDto(customerViVi);

        restCustomerViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerViViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerViVi.setEmail(null);

        // Create the CustomerViVi, which fails.
        CustomerViViDTO customerViViDTO = customerViViMapper.toDto(customerViVi);

        restCustomerViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerViViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustomerViVis() throws Exception {
        // Initialize the database
        insertedCustomerViVi = customerViViRepository.saveAndFlush(customerViVi);

        // Get all the customerViViList
        restCustomerViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerViVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @Test
    @Transactional
    void getCustomerViVi() throws Exception {
        // Initialize the database
        insertedCustomerViVi = customerViViRepository.saveAndFlush(customerViVi);

        // Get the customerViVi
        restCustomerViViMockMvc
            .perform(get(ENTITY_API_URL_ID, customerViVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customerViVi.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getNonExistingCustomerViVi() throws Exception {
        // Get the customerViVi
        restCustomerViViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomerViVi() throws Exception {
        // Initialize the database
        insertedCustomerViVi = customerViViRepository.saveAndFlush(customerViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerViVi
        CustomerViVi updatedCustomerViVi = customerViViRepository.findById(customerViVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCustomerViVi are not directly saved in db
        em.detach(updatedCustomerViVi);
        updatedCustomerViVi
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        CustomerViViDTO customerViViDTO = customerViViMapper.toDto(updatedCustomerViVi);

        restCustomerViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerViViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerViViDTO))
            )
            .andExpect(status().isOk());

        // Validate the CustomerViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCustomerViViToMatchAllProperties(updatedCustomerViVi);
    }

    @Test
    @Transactional
    void putNonExistingCustomerViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerViVi.setId(longCount.incrementAndGet());

        // Create the CustomerViVi
        CustomerViViDTO customerViViDTO = customerViViMapper.toDto(customerViVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerViViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomerViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerViVi.setId(longCount.incrementAndGet());

        // Create the CustomerViVi
        CustomerViViDTO customerViViDTO = customerViViMapper.toDto(customerViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomerViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerViVi.setId(longCount.incrementAndGet());

        // Create the CustomerViVi
        CustomerViViDTO customerViViDTO = customerViViMapper.toDto(customerViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerViViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerViViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerViViWithPatch() throws Exception {
        // Initialize the database
        insertedCustomerViVi = customerViViRepository.saveAndFlush(customerViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerViVi using partial update
        CustomerViVi partialUpdatedCustomerViVi = new CustomerViVi();
        partialUpdatedCustomerViVi.setId(customerViVi.getId());

        partialUpdatedCustomerViVi.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME).email(UPDATED_EMAIL);

        restCustomerViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomerViVi))
            )
            .andExpect(status().isOk());

        // Validate the CustomerViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomerViViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCustomerViVi, customerViVi),
            getPersistedCustomerViVi(customerViVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateCustomerViViWithPatch() throws Exception {
        // Initialize the database
        insertedCustomerViVi = customerViViRepository.saveAndFlush(customerViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerViVi using partial update
        CustomerViVi partialUpdatedCustomerViVi = new CustomerViVi();
        partialUpdatedCustomerViVi.setId(customerViVi.getId());

        partialUpdatedCustomerViVi
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restCustomerViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomerViVi))
            )
            .andExpect(status().isOk());

        // Validate the CustomerViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomerViViUpdatableFieldsEquals(partialUpdatedCustomerViVi, getPersistedCustomerViVi(partialUpdatedCustomerViVi));
    }

    @Test
    @Transactional
    void patchNonExistingCustomerViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerViVi.setId(longCount.incrementAndGet());

        // Create the CustomerViVi
        CustomerViViDTO customerViViDTO = customerViViMapper.toDto(customerViVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerViViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomerViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerViVi.setId(longCount.incrementAndGet());

        // Create the CustomerViVi
        CustomerViViDTO customerViViDTO = customerViViMapper.toDto(customerViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomerViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerViVi.setId(longCount.incrementAndGet());

        // Create the CustomerViVi
        CustomerViViDTO customerViViDTO = customerViViMapper.toDto(customerViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerViViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(customerViViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomerViVi() throws Exception {
        // Initialize the database
        insertedCustomerViVi = customerViViRepository.saveAndFlush(customerViVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the customerViVi
        restCustomerViViMockMvc
            .perform(delete(ENTITY_API_URL_ID, customerViVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return customerViViRepository.count();
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

    protected CustomerViVi getPersistedCustomerViVi(CustomerViVi customerViVi) {
        return customerViViRepository.findById(customerViVi.getId()).orElseThrow();
    }

    protected void assertPersistedCustomerViViToMatchAllProperties(CustomerViVi expectedCustomerViVi) {
        assertCustomerViViAllPropertiesEquals(expectedCustomerViVi, getPersistedCustomerViVi(expectedCustomerViVi));
    }

    protected void assertPersistedCustomerViViToMatchUpdatableProperties(CustomerViVi expectedCustomerViVi) {
        assertCustomerViViAllUpdatablePropertiesEquals(expectedCustomerViVi, getPersistedCustomerViVi(expectedCustomerViVi));
    }
}
