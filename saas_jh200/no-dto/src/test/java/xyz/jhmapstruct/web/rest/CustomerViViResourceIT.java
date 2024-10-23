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
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.repository.CustomerViViRepository;

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
        var returnedCustomerViVi = om.readValue(
            restCustomerViViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerViVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CustomerViVi.class
        );

        // Validate the CustomerViVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCustomerViViUpdatableFieldsEquals(returnedCustomerViVi, getPersistedCustomerViVi(returnedCustomerViVi));

        insertedCustomerViVi = returnedCustomerViVi;
    }

    @Test
    @Transactional
    void createCustomerViViWithExistingId() throws Exception {
        // Create the CustomerViVi with an existing ID
        customerViVi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerViVi)))
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

        restCustomerViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerViVi)))
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

        restCustomerViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerViVi)))
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

        restCustomerViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerViVi)))
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
    void getCustomerViVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedCustomerViVi = customerViViRepository.saveAndFlush(customerViVi);

        Long id = customerViVi.getId();

        defaultCustomerViViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCustomerViViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCustomerViViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCustomerViVisByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerViVi = customerViViRepository.saveAndFlush(customerViVi);

        // Get all the customerViViList where firstName equals to
        defaultCustomerViViFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerViVisByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerViVi = customerViViRepository.saveAndFlush(customerViVi);

        // Get all the customerViViList where firstName in
        defaultCustomerViViFiltering("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME, "firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerViVisByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerViVi = customerViViRepository.saveAndFlush(customerViVi);

        // Get all the customerViViList where firstName is not null
        defaultCustomerViViFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerViVisByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerViVi = customerViViRepository.saveAndFlush(customerViVi);

        // Get all the customerViViList where firstName contains
        defaultCustomerViViFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerViVisByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerViVi = customerViViRepository.saveAndFlush(customerViVi);

        // Get all the customerViViList where firstName does not contain
        defaultCustomerViViFiltering("firstName.doesNotContain=" + UPDATED_FIRST_NAME, "firstName.doesNotContain=" + DEFAULT_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerViVisByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerViVi = customerViViRepository.saveAndFlush(customerViVi);

        // Get all the customerViViList where lastName equals to
        defaultCustomerViViFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerViVisByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerViVi = customerViViRepository.saveAndFlush(customerViVi);

        // Get all the customerViViList where lastName in
        defaultCustomerViViFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerViVisByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerViVi = customerViViRepository.saveAndFlush(customerViVi);

        // Get all the customerViViList where lastName is not null
        defaultCustomerViViFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerViVisByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerViVi = customerViViRepository.saveAndFlush(customerViVi);

        // Get all the customerViViList where lastName contains
        defaultCustomerViViFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerViVisByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerViVi = customerViViRepository.saveAndFlush(customerViVi);

        // Get all the customerViViList where lastName does not contain
        defaultCustomerViViFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerViVisByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerViVi = customerViViRepository.saveAndFlush(customerViVi);

        // Get all the customerViViList where email equals to
        defaultCustomerViViFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomerViVisByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerViVi = customerViViRepository.saveAndFlush(customerViVi);

        // Get all the customerViViList where email in
        defaultCustomerViViFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomerViVisByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerViVi = customerViViRepository.saveAndFlush(customerViVi);

        // Get all the customerViViList where email is not null
        defaultCustomerViViFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerViVisByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerViVi = customerViViRepository.saveAndFlush(customerViVi);

        // Get all the customerViViList where email contains
        defaultCustomerViViFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomerViVisByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerViVi = customerViViRepository.saveAndFlush(customerViVi);

        // Get all the customerViViList where email does not contain
        defaultCustomerViViFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomerViVisByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerViVi = customerViViRepository.saveAndFlush(customerViVi);

        // Get all the customerViViList where phoneNumber equals to
        defaultCustomerViViFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerViVisByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerViVi = customerViViRepository.saveAndFlush(customerViVi);

        // Get all the customerViViList where phoneNumber in
        defaultCustomerViViFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCustomerViVisByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerViVi = customerViViRepository.saveAndFlush(customerViVi);

        // Get all the customerViViList where phoneNumber is not null
        defaultCustomerViViFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerViVisByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerViVi = customerViViRepository.saveAndFlush(customerViVi);

        // Get all the customerViViList where phoneNumber contains
        defaultCustomerViViFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerViVisByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerViVi = customerViViRepository.saveAndFlush(customerViVi);

        // Get all the customerViViList where phoneNumber does not contain
        defaultCustomerViViFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCustomerViVisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            customerViViRepository.saveAndFlush(customerViVi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        customerViVi.setTenant(tenant);
        customerViViRepository.saveAndFlush(customerViVi);
        Long tenantId = tenant.getId();
        // Get all the customerViViList where tenant equals to tenantId
        defaultCustomerViViShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the customerViViList where tenant equals to (tenantId + 1)
        defaultCustomerViViShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultCustomerViViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCustomerViViShouldBeFound(shouldBeFound);
        defaultCustomerViViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerViViShouldBeFound(String filter) throws Exception {
        restCustomerViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerViVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restCustomerViViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomerViViShouldNotBeFound(String filter) throws Exception {
        restCustomerViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerViViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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

        restCustomerViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCustomerViVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCustomerViVi))
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

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerViVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerViVi))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerViVi))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerViViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerViVi)))
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

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerViVi))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerViVi))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerViViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(customerViVi)))
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
