package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.CustomerSigmaAsserts.*;
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
import xyz.jhmapstruct.domain.CustomerSigma;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.repository.CustomerSigmaRepository;

/**
 * Integration tests for the {@link CustomerSigmaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomerSigmaResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/customer-sigmas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CustomerSigmaRepository customerSigmaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerSigmaMockMvc;

    private CustomerSigma customerSigma;

    private CustomerSigma insertedCustomerSigma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerSigma createEntity() {
        return new CustomerSigma()
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
    public static CustomerSigma createUpdatedEntity() {
        return new CustomerSigma()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        customerSigma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCustomerSigma != null) {
            customerSigmaRepository.delete(insertedCustomerSigma);
            insertedCustomerSigma = null;
        }
    }

    @Test
    @Transactional
    void createCustomerSigma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CustomerSigma
        var returnedCustomerSigma = om.readValue(
            restCustomerSigmaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerSigma)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CustomerSigma.class
        );

        // Validate the CustomerSigma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCustomerSigmaUpdatableFieldsEquals(returnedCustomerSigma, getPersistedCustomerSigma(returnedCustomerSigma));

        insertedCustomerSigma = returnedCustomerSigma;
    }

    @Test
    @Transactional
    void createCustomerSigmaWithExistingId() throws Exception {
        // Create the CustomerSigma with an existing ID
        customerSigma.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerSigma)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerSigma.setFirstName(null);

        // Create the CustomerSigma, which fails.

        restCustomerSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerSigma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerSigma.setLastName(null);

        // Create the CustomerSigma, which fails.

        restCustomerSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerSigma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerSigma.setEmail(null);

        // Create the CustomerSigma, which fails.

        restCustomerSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerSigma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustomerSigmas() throws Exception {
        // Initialize the database
        insertedCustomerSigma = customerSigmaRepository.saveAndFlush(customerSigma);

        // Get all the customerSigmaList
        restCustomerSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerSigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @Test
    @Transactional
    void getCustomerSigma() throws Exception {
        // Initialize the database
        insertedCustomerSigma = customerSigmaRepository.saveAndFlush(customerSigma);

        // Get the customerSigma
        restCustomerSigmaMockMvc
            .perform(get(ENTITY_API_URL_ID, customerSigma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customerSigma.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getCustomerSigmasByIdFiltering() throws Exception {
        // Initialize the database
        insertedCustomerSigma = customerSigmaRepository.saveAndFlush(customerSigma);

        Long id = customerSigma.getId();

        defaultCustomerSigmaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCustomerSigmaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCustomerSigmaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCustomerSigmasByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerSigma = customerSigmaRepository.saveAndFlush(customerSigma);

        // Get all the customerSigmaList where firstName equals to
        defaultCustomerSigmaFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerSigmasByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerSigma = customerSigmaRepository.saveAndFlush(customerSigma);

        // Get all the customerSigmaList where firstName in
        defaultCustomerSigmaFiltering(
            "firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME,
            "firstName.in=" + UPDATED_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllCustomerSigmasByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerSigma = customerSigmaRepository.saveAndFlush(customerSigma);

        // Get all the customerSigmaList where firstName is not null
        defaultCustomerSigmaFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerSigmasByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerSigma = customerSigmaRepository.saveAndFlush(customerSigma);

        // Get all the customerSigmaList where firstName contains
        defaultCustomerSigmaFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerSigmasByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerSigma = customerSigmaRepository.saveAndFlush(customerSigma);

        // Get all the customerSigmaList where firstName does not contain
        defaultCustomerSigmaFiltering("firstName.doesNotContain=" + UPDATED_FIRST_NAME, "firstName.doesNotContain=" + DEFAULT_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerSigmasByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerSigma = customerSigmaRepository.saveAndFlush(customerSigma);

        // Get all the customerSigmaList where lastName equals to
        defaultCustomerSigmaFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerSigmasByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerSigma = customerSigmaRepository.saveAndFlush(customerSigma);

        // Get all the customerSigmaList where lastName in
        defaultCustomerSigmaFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerSigmasByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerSigma = customerSigmaRepository.saveAndFlush(customerSigma);

        // Get all the customerSigmaList where lastName is not null
        defaultCustomerSigmaFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerSigmasByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerSigma = customerSigmaRepository.saveAndFlush(customerSigma);

        // Get all the customerSigmaList where lastName contains
        defaultCustomerSigmaFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerSigmasByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerSigma = customerSigmaRepository.saveAndFlush(customerSigma);

        // Get all the customerSigmaList where lastName does not contain
        defaultCustomerSigmaFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerSigmasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerSigma = customerSigmaRepository.saveAndFlush(customerSigma);

        // Get all the customerSigmaList where email equals to
        defaultCustomerSigmaFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomerSigmasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerSigma = customerSigmaRepository.saveAndFlush(customerSigma);

        // Get all the customerSigmaList where email in
        defaultCustomerSigmaFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomerSigmasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerSigma = customerSigmaRepository.saveAndFlush(customerSigma);

        // Get all the customerSigmaList where email is not null
        defaultCustomerSigmaFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerSigmasByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerSigma = customerSigmaRepository.saveAndFlush(customerSigma);

        // Get all the customerSigmaList where email contains
        defaultCustomerSigmaFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomerSigmasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerSigma = customerSigmaRepository.saveAndFlush(customerSigma);

        // Get all the customerSigmaList where email does not contain
        defaultCustomerSigmaFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomerSigmasByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerSigma = customerSigmaRepository.saveAndFlush(customerSigma);

        // Get all the customerSigmaList where phoneNumber equals to
        defaultCustomerSigmaFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerSigmasByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerSigma = customerSigmaRepository.saveAndFlush(customerSigma);

        // Get all the customerSigmaList where phoneNumber in
        defaultCustomerSigmaFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCustomerSigmasByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerSigma = customerSigmaRepository.saveAndFlush(customerSigma);

        // Get all the customerSigmaList where phoneNumber is not null
        defaultCustomerSigmaFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerSigmasByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerSigma = customerSigmaRepository.saveAndFlush(customerSigma);

        // Get all the customerSigmaList where phoneNumber contains
        defaultCustomerSigmaFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerSigmasByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerSigma = customerSigmaRepository.saveAndFlush(customerSigma);

        // Get all the customerSigmaList where phoneNumber does not contain
        defaultCustomerSigmaFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCustomerSigmasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            customerSigmaRepository.saveAndFlush(customerSigma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        customerSigma.setTenant(tenant);
        customerSigmaRepository.saveAndFlush(customerSigma);
        Long tenantId = tenant.getId();
        // Get all the customerSigmaList where tenant equals to tenantId
        defaultCustomerSigmaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the customerSigmaList where tenant equals to (tenantId + 1)
        defaultCustomerSigmaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultCustomerSigmaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCustomerSigmaShouldBeFound(shouldBeFound);
        defaultCustomerSigmaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerSigmaShouldBeFound(String filter) throws Exception {
        restCustomerSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerSigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restCustomerSigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomerSigmaShouldNotBeFound(String filter) throws Exception {
        restCustomerSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerSigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCustomerSigma() throws Exception {
        // Get the customerSigma
        restCustomerSigmaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomerSigma() throws Exception {
        // Initialize the database
        insertedCustomerSigma = customerSigmaRepository.saveAndFlush(customerSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerSigma
        CustomerSigma updatedCustomerSigma = customerSigmaRepository.findById(customerSigma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCustomerSigma are not directly saved in db
        em.detach(updatedCustomerSigma);
        updatedCustomerSigma
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restCustomerSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCustomerSigma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCustomerSigma))
            )
            .andExpect(status().isOk());

        // Validate the CustomerSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCustomerSigmaToMatchAllProperties(updatedCustomerSigma);
    }

    @Test
    @Transactional
    void putNonExistingCustomerSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerSigma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerSigma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerSigma))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomerSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerSigma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerSigma))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomerSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerSigma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerSigmaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerSigma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerSigmaWithPatch() throws Exception {
        // Initialize the database
        insertedCustomerSigma = customerSigmaRepository.saveAndFlush(customerSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerSigma using partial update
        CustomerSigma partialUpdatedCustomerSigma = new CustomerSigma();
        partialUpdatedCustomerSigma.setId(customerSigma.getId());

        partialUpdatedCustomerSigma.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME).phoneNumber(UPDATED_PHONE_NUMBER);

        restCustomerSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomerSigma))
            )
            .andExpect(status().isOk());

        // Validate the CustomerSigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomerSigmaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCustomerSigma, customerSigma),
            getPersistedCustomerSigma(customerSigma)
        );
    }

    @Test
    @Transactional
    void fullUpdateCustomerSigmaWithPatch() throws Exception {
        // Initialize the database
        insertedCustomerSigma = customerSigmaRepository.saveAndFlush(customerSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerSigma using partial update
        CustomerSigma partialUpdatedCustomerSigma = new CustomerSigma();
        partialUpdatedCustomerSigma.setId(customerSigma.getId());

        partialUpdatedCustomerSigma
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restCustomerSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomerSigma))
            )
            .andExpect(status().isOk());

        // Validate the CustomerSigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomerSigmaUpdatableFieldsEquals(partialUpdatedCustomerSigma, getPersistedCustomerSigma(partialUpdatedCustomerSigma));
    }

    @Test
    @Transactional
    void patchNonExistingCustomerSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerSigma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerSigma))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomerSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerSigma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerSigma))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomerSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerSigma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerSigmaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(customerSigma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomerSigma() throws Exception {
        // Initialize the database
        insertedCustomerSigma = customerSigmaRepository.saveAndFlush(customerSigma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the customerSigma
        restCustomerSigmaMockMvc
            .perform(delete(ENTITY_API_URL_ID, customerSigma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return customerSigmaRepository.count();
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

    protected CustomerSigma getPersistedCustomerSigma(CustomerSigma customerSigma) {
        return customerSigmaRepository.findById(customerSigma.getId()).orElseThrow();
    }

    protected void assertPersistedCustomerSigmaToMatchAllProperties(CustomerSigma expectedCustomerSigma) {
        assertCustomerSigmaAllPropertiesEquals(expectedCustomerSigma, getPersistedCustomerSigma(expectedCustomerSigma));
    }

    protected void assertPersistedCustomerSigmaToMatchUpdatableProperties(CustomerSigma expectedCustomerSigma) {
        assertCustomerSigmaAllUpdatablePropertiesEquals(expectedCustomerSigma, getPersistedCustomerSigma(expectedCustomerSigma));
    }
}
