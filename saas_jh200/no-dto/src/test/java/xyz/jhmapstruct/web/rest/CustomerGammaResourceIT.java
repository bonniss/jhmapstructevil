package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.CustomerGammaAsserts.*;
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
import xyz.jhmapstruct.domain.CustomerGamma;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.repository.CustomerGammaRepository;

/**
 * Integration tests for the {@link CustomerGammaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomerGammaResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/customer-gammas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CustomerGammaRepository customerGammaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerGammaMockMvc;

    private CustomerGamma customerGamma;

    private CustomerGamma insertedCustomerGamma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerGamma createEntity() {
        return new CustomerGamma()
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
    public static CustomerGamma createUpdatedEntity() {
        return new CustomerGamma()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        customerGamma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCustomerGamma != null) {
            customerGammaRepository.delete(insertedCustomerGamma);
            insertedCustomerGamma = null;
        }
    }

    @Test
    @Transactional
    void createCustomerGamma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CustomerGamma
        var returnedCustomerGamma = om.readValue(
            restCustomerGammaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerGamma)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CustomerGamma.class
        );

        // Validate the CustomerGamma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCustomerGammaUpdatableFieldsEquals(returnedCustomerGamma, getPersistedCustomerGamma(returnedCustomerGamma));

        insertedCustomerGamma = returnedCustomerGamma;
    }

    @Test
    @Transactional
    void createCustomerGammaWithExistingId() throws Exception {
        // Create the CustomerGamma with an existing ID
        customerGamma.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerGamma)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerGamma.setFirstName(null);

        // Create the CustomerGamma, which fails.

        restCustomerGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerGamma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerGamma.setLastName(null);

        // Create the CustomerGamma, which fails.

        restCustomerGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerGamma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerGamma.setEmail(null);

        // Create the CustomerGamma, which fails.

        restCustomerGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerGamma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustomerGammas() throws Exception {
        // Initialize the database
        insertedCustomerGamma = customerGammaRepository.saveAndFlush(customerGamma);

        // Get all the customerGammaList
        restCustomerGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @Test
    @Transactional
    void getCustomerGamma() throws Exception {
        // Initialize the database
        insertedCustomerGamma = customerGammaRepository.saveAndFlush(customerGamma);

        // Get the customerGamma
        restCustomerGammaMockMvc
            .perform(get(ENTITY_API_URL_ID, customerGamma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customerGamma.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getCustomerGammasByIdFiltering() throws Exception {
        // Initialize the database
        insertedCustomerGamma = customerGammaRepository.saveAndFlush(customerGamma);

        Long id = customerGamma.getId();

        defaultCustomerGammaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCustomerGammaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCustomerGammaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCustomerGammasByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerGamma = customerGammaRepository.saveAndFlush(customerGamma);

        // Get all the customerGammaList where firstName equals to
        defaultCustomerGammaFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerGammasByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerGamma = customerGammaRepository.saveAndFlush(customerGamma);

        // Get all the customerGammaList where firstName in
        defaultCustomerGammaFiltering(
            "firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME,
            "firstName.in=" + UPDATED_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllCustomerGammasByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerGamma = customerGammaRepository.saveAndFlush(customerGamma);

        // Get all the customerGammaList where firstName is not null
        defaultCustomerGammaFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerGammasByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerGamma = customerGammaRepository.saveAndFlush(customerGamma);

        // Get all the customerGammaList where firstName contains
        defaultCustomerGammaFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerGammasByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerGamma = customerGammaRepository.saveAndFlush(customerGamma);

        // Get all the customerGammaList where firstName does not contain
        defaultCustomerGammaFiltering("firstName.doesNotContain=" + UPDATED_FIRST_NAME, "firstName.doesNotContain=" + DEFAULT_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerGammasByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerGamma = customerGammaRepository.saveAndFlush(customerGamma);

        // Get all the customerGammaList where lastName equals to
        defaultCustomerGammaFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerGammasByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerGamma = customerGammaRepository.saveAndFlush(customerGamma);

        // Get all the customerGammaList where lastName in
        defaultCustomerGammaFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerGammasByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerGamma = customerGammaRepository.saveAndFlush(customerGamma);

        // Get all the customerGammaList where lastName is not null
        defaultCustomerGammaFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerGammasByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerGamma = customerGammaRepository.saveAndFlush(customerGamma);

        // Get all the customerGammaList where lastName contains
        defaultCustomerGammaFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerGammasByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerGamma = customerGammaRepository.saveAndFlush(customerGamma);

        // Get all the customerGammaList where lastName does not contain
        defaultCustomerGammaFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerGammasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerGamma = customerGammaRepository.saveAndFlush(customerGamma);

        // Get all the customerGammaList where email equals to
        defaultCustomerGammaFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomerGammasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerGamma = customerGammaRepository.saveAndFlush(customerGamma);

        // Get all the customerGammaList where email in
        defaultCustomerGammaFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomerGammasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerGamma = customerGammaRepository.saveAndFlush(customerGamma);

        // Get all the customerGammaList where email is not null
        defaultCustomerGammaFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerGammasByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerGamma = customerGammaRepository.saveAndFlush(customerGamma);

        // Get all the customerGammaList where email contains
        defaultCustomerGammaFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomerGammasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerGamma = customerGammaRepository.saveAndFlush(customerGamma);

        // Get all the customerGammaList where email does not contain
        defaultCustomerGammaFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomerGammasByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerGamma = customerGammaRepository.saveAndFlush(customerGamma);

        // Get all the customerGammaList where phoneNumber equals to
        defaultCustomerGammaFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerGammasByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerGamma = customerGammaRepository.saveAndFlush(customerGamma);

        // Get all the customerGammaList where phoneNumber in
        defaultCustomerGammaFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCustomerGammasByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerGamma = customerGammaRepository.saveAndFlush(customerGamma);

        // Get all the customerGammaList where phoneNumber is not null
        defaultCustomerGammaFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerGammasByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerGamma = customerGammaRepository.saveAndFlush(customerGamma);

        // Get all the customerGammaList where phoneNumber contains
        defaultCustomerGammaFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerGammasByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerGamma = customerGammaRepository.saveAndFlush(customerGamma);

        // Get all the customerGammaList where phoneNumber does not contain
        defaultCustomerGammaFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCustomerGammasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            customerGammaRepository.saveAndFlush(customerGamma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        customerGamma.setTenant(tenant);
        customerGammaRepository.saveAndFlush(customerGamma);
        Long tenantId = tenant.getId();
        // Get all the customerGammaList where tenant equals to tenantId
        defaultCustomerGammaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the customerGammaList where tenant equals to (tenantId + 1)
        defaultCustomerGammaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultCustomerGammaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCustomerGammaShouldBeFound(shouldBeFound);
        defaultCustomerGammaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerGammaShouldBeFound(String filter) throws Exception {
        restCustomerGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restCustomerGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomerGammaShouldNotBeFound(String filter) throws Exception {
        restCustomerGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCustomerGamma() throws Exception {
        // Get the customerGamma
        restCustomerGammaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomerGamma() throws Exception {
        // Initialize the database
        insertedCustomerGamma = customerGammaRepository.saveAndFlush(customerGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerGamma
        CustomerGamma updatedCustomerGamma = customerGammaRepository.findById(customerGamma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCustomerGamma are not directly saved in db
        em.detach(updatedCustomerGamma);
        updatedCustomerGamma
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restCustomerGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCustomerGamma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCustomerGamma))
            )
            .andExpect(status().isOk());

        // Validate the CustomerGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCustomerGammaToMatchAllProperties(updatedCustomerGamma);
    }

    @Test
    @Transactional
    void putNonExistingCustomerGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerGamma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerGamma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerGamma))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomerGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerGamma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerGamma))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomerGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerGamma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerGammaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerGamma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerGammaWithPatch() throws Exception {
        // Initialize the database
        insertedCustomerGamma = customerGammaRepository.saveAndFlush(customerGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerGamma using partial update
        CustomerGamma partialUpdatedCustomerGamma = new CustomerGamma();
        partialUpdatedCustomerGamma.setId(customerGamma.getId());

        partialUpdatedCustomerGamma.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME).phoneNumber(UPDATED_PHONE_NUMBER);

        restCustomerGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomerGamma))
            )
            .andExpect(status().isOk());

        // Validate the CustomerGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomerGammaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCustomerGamma, customerGamma),
            getPersistedCustomerGamma(customerGamma)
        );
    }

    @Test
    @Transactional
    void fullUpdateCustomerGammaWithPatch() throws Exception {
        // Initialize the database
        insertedCustomerGamma = customerGammaRepository.saveAndFlush(customerGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerGamma using partial update
        CustomerGamma partialUpdatedCustomerGamma = new CustomerGamma();
        partialUpdatedCustomerGamma.setId(customerGamma.getId());

        partialUpdatedCustomerGamma
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restCustomerGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomerGamma))
            )
            .andExpect(status().isOk());

        // Validate the CustomerGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomerGammaUpdatableFieldsEquals(partialUpdatedCustomerGamma, getPersistedCustomerGamma(partialUpdatedCustomerGamma));
    }

    @Test
    @Transactional
    void patchNonExistingCustomerGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerGamma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerGamma))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomerGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerGamma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerGamma))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomerGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerGamma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerGammaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(customerGamma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomerGamma() throws Exception {
        // Initialize the database
        insertedCustomerGamma = customerGammaRepository.saveAndFlush(customerGamma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the customerGamma
        restCustomerGammaMockMvc
            .perform(delete(ENTITY_API_URL_ID, customerGamma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return customerGammaRepository.count();
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

    protected CustomerGamma getPersistedCustomerGamma(CustomerGamma customerGamma) {
        return customerGammaRepository.findById(customerGamma.getId()).orElseThrow();
    }

    protected void assertPersistedCustomerGammaToMatchAllProperties(CustomerGamma expectedCustomerGamma) {
        assertCustomerGammaAllPropertiesEquals(expectedCustomerGamma, getPersistedCustomerGamma(expectedCustomerGamma));
    }

    protected void assertPersistedCustomerGammaToMatchUpdatableProperties(CustomerGamma expectedCustomerGamma) {
        assertCustomerGammaAllUpdatablePropertiesEquals(expectedCustomerGamma, getPersistedCustomerGamma(expectedCustomerGamma));
    }
}
