package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.CustomerViAsserts.*;
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
import xyz.jhmapstruct.domain.CustomerVi;
import xyz.jhmapstruct.repository.CustomerViRepository;
import xyz.jhmapstruct.service.dto.CustomerViDTO;
import xyz.jhmapstruct.service.mapper.CustomerViMapper;

/**
 * Integration tests for the {@link CustomerViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomerViResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/customer-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CustomerViRepository customerViRepository;

    @Autowired
    private CustomerViMapper customerViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerViMockMvc;

    private CustomerVi customerVi;

    private CustomerVi insertedCustomerVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerVi createEntity() {
        return new CustomerVi()
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
    public static CustomerVi createUpdatedEntity() {
        return new CustomerVi()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        customerVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCustomerVi != null) {
            customerViRepository.delete(insertedCustomerVi);
            insertedCustomerVi = null;
        }
    }

    @Test
    @Transactional
    void createCustomerVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CustomerVi
        CustomerViDTO customerViDTO = customerViMapper.toDto(customerVi);
        var returnedCustomerViDTO = om.readValue(
            restCustomerViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CustomerViDTO.class
        );

        // Validate the CustomerVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCustomerVi = customerViMapper.toEntity(returnedCustomerViDTO);
        assertCustomerViUpdatableFieldsEquals(returnedCustomerVi, getPersistedCustomerVi(returnedCustomerVi));

        insertedCustomerVi = returnedCustomerVi;
    }

    @Test
    @Transactional
    void createCustomerViWithExistingId() throws Exception {
        // Create the CustomerVi with an existing ID
        customerVi.setId(1L);
        CustomerViDTO customerViDTO = customerViMapper.toDto(customerVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerVi.setFirstName(null);

        // Create the CustomerVi, which fails.
        CustomerViDTO customerViDTO = customerViMapper.toDto(customerVi);

        restCustomerViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerVi.setLastName(null);

        // Create the CustomerVi, which fails.
        CustomerViDTO customerViDTO = customerViMapper.toDto(customerVi);

        restCustomerViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerVi.setEmail(null);

        // Create the CustomerVi, which fails.
        CustomerViDTO customerViDTO = customerViMapper.toDto(customerVi);

        restCustomerViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustomerVis() throws Exception {
        // Initialize the database
        insertedCustomerVi = customerViRepository.saveAndFlush(customerVi);

        // Get all the customerViList
        restCustomerViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @Test
    @Transactional
    void getCustomerVi() throws Exception {
        // Initialize the database
        insertedCustomerVi = customerViRepository.saveAndFlush(customerVi);

        // Get the customerVi
        restCustomerViMockMvc
            .perform(get(ENTITY_API_URL_ID, customerVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customerVi.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getNonExistingCustomerVi() throws Exception {
        // Get the customerVi
        restCustomerViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomerVi() throws Exception {
        // Initialize the database
        insertedCustomerVi = customerViRepository.saveAndFlush(customerVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerVi
        CustomerVi updatedCustomerVi = customerViRepository.findById(customerVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCustomerVi are not directly saved in db
        em.detach(updatedCustomerVi);
        updatedCustomerVi.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME).email(UPDATED_EMAIL).phoneNumber(UPDATED_PHONE_NUMBER);
        CustomerViDTO customerViDTO = customerViMapper.toDto(updatedCustomerVi);

        restCustomerViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerViDTO))
            )
            .andExpect(status().isOk());

        // Validate the CustomerVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCustomerViToMatchAllProperties(updatedCustomerVi);
    }

    @Test
    @Transactional
    void putNonExistingCustomerVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerVi.setId(longCount.incrementAndGet());

        // Create the CustomerVi
        CustomerViDTO customerViDTO = customerViMapper.toDto(customerVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomerVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerVi.setId(longCount.incrementAndGet());

        // Create the CustomerVi
        CustomerViDTO customerViDTO = customerViMapper.toDto(customerVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomerVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerVi.setId(longCount.incrementAndGet());

        // Create the CustomerVi
        CustomerViDTO customerViDTO = customerViMapper.toDto(customerVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerViWithPatch() throws Exception {
        // Initialize the database
        insertedCustomerVi = customerViRepository.saveAndFlush(customerVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerVi using partial update
        CustomerVi partialUpdatedCustomerVi = new CustomerVi();
        partialUpdatedCustomerVi.setId(customerVi.getId());

        partialUpdatedCustomerVi
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restCustomerViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomerVi))
            )
            .andExpect(status().isOk());

        // Validate the CustomerVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomerViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCustomerVi, customerVi),
            getPersistedCustomerVi(customerVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateCustomerViWithPatch() throws Exception {
        // Initialize the database
        insertedCustomerVi = customerViRepository.saveAndFlush(customerVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerVi using partial update
        CustomerVi partialUpdatedCustomerVi = new CustomerVi();
        partialUpdatedCustomerVi.setId(customerVi.getId());

        partialUpdatedCustomerVi
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restCustomerViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomerVi))
            )
            .andExpect(status().isOk());

        // Validate the CustomerVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomerViUpdatableFieldsEquals(partialUpdatedCustomerVi, getPersistedCustomerVi(partialUpdatedCustomerVi));
    }

    @Test
    @Transactional
    void patchNonExistingCustomerVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerVi.setId(longCount.incrementAndGet());

        // Create the CustomerVi
        CustomerViDTO customerViDTO = customerViMapper.toDto(customerVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomerVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerVi.setId(longCount.incrementAndGet());

        // Create the CustomerVi
        CustomerViDTO customerViDTO = customerViMapper.toDto(customerVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomerVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerVi.setId(longCount.incrementAndGet());

        // Create the CustomerVi
        CustomerViDTO customerViDTO = customerViMapper.toDto(customerVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(customerViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomerVi() throws Exception {
        // Initialize the database
        insertedCustomerVi = customerViRepository.saveAndFlush(customerVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the customerVi
        restCustomerViMockMvc
            .perform(delete(ENTITY_API_URL_ID, customerVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return customerViRepository.count();
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

    protected CustomerVi getPersistedCustomerVi(CustomerVi customerVi) {
        return customerViRepository.findById(customerVi.getId()).orElseThrow();
    }

    protected void assertPersistedCustomerViToMatchAllProperties(CustomerVi expectedCustomerVi) {
        assertCustomerViAllPropertiesEquals(expectedCustomerVi, getPersistedCustomerVi(expectedCustomerVi));
    }

    protected void assertPersistedCustomerViToMatchUpdatableProperties(CustomerVi expectedCustomerVi) {
        assertCustomerViAllUpdatablePropertiesEquals(expectedCustomerVi, getPersistedCustomerVi(expectedCustomerVi));
    }
}
