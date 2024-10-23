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
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.repository.CustomerMiMiRepository;

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
        var returnedCustomerMiMi = om.readValue(
            restCustomerMiMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerMiMi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CustomerMiMi.class
        );

        // Validate the CustomerMiMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCustomerMiMiUpdatableFieldsEquals(returnedCustomerMiMi, getPersistedCustomerMiMi(returnedCustomerMiMi));

        insertedCustomerMiMi = returnedCustomerMiMi;
    }

    @Test
    @Transactional
    void createCustomerMiMiWithExistingId() throws Exception {
        // Create the CustomerMiMi with an existing ID
        customerMiMi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerMiMi)))
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

        restCustomerMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerMiMi)))
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

        restCustomerMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerMiMi)))
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

        restCustomerMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerMiMi)))
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
    void getCustomerMiMisByIdFiltering() throws Exception {
        // Initialize the database
        insertedCustomerMiMi = customerMiMiRepository.saveAndFlush(customerMiMi);

        Long id = customerMiMi.getId();

        defaultCustomerMiMiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCustomerMiMiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCustomerMiMiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCustomerMiMisByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerMiMi = customerMiMiRepository.saveAndFlush(customerMiMi);

        // Get all the customerMiMiList where firstName equals to
        defaultCustomerMiMiFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerMiMisByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerMiMi = customerMiMiRepository.saveAndFlush(customerMiMi);

        // Get all the customerMiMiList where firstName in
        defaultCustomerMiMiFiltering("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME, "firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerMiMisByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerMiMi = customerMiMiRepository.saveAndFlush(customerMiMi);

        // Get all the customerMiMiList where firstName is not null
        defaultCustomerMiMiFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerMiMisByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerMiMi = customerMiMiRepository.saveAndFlush(customerMiMi);

        // Get all the customerMiMiList where firstName contains
        defaultCustomerMiMiFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerMiMisByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerMiMi = customerMiMiRepository.saveAndFlush(customerMiMi);

        // Get all the customerMiMiList where firstName does not contain
        defaultCustomerMiMiFiltering("firstName.doesNotContain=" + UPDATED_FIRST_NAME, "firstName.doesNotContain=" + DEFAULT_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerMiMisByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerMiMi = customerMiMiRepository.saveAndFlush(customerMiMi);

        // Get all the customerMiMiList where lastName equals to
        defaultCustomerMiMiFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerMiMisByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerMiMi = customerMiMiRepository.saveAndFlush(customerMiMi);

        // Get all the customerMiMiList where lastName in
        defaultCustomerMiMiFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerMiMisByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerMiMi = customerMiMiRepository.saveAndFlush(customerMiMi);

        // Get all the customerMiMiList where lastName is not null
        defaultCustomerMiMiFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerMiMisByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerMiMi = customerMiMiRepository.saveAndFlush(customerMiMi);

        // Get all the customerMiMiList where lastName contains
        defaultCustomerMiMiFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerMiMisByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerMiMi = customerMiMiRepository.saveAndFlush(customerMiMi);

        // Get all the customerMiMiList where lastName does not contain
        defaultCustomerMiMiFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerMiMisByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerMiMi = customerMiMiRepository.saveAndFlush(customerMiMi);

        // Get all the customerMiMiList where email equals to
        defaultCustomerMiMiFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomerMiMisByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerMiMi = customerMiMiRepository.saveAndFlush(customerMiMi);

        // Get all the customerMiMiList where email in
        defaultCustomerMiMiFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomerMiMisByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerMiMi = customerMiMiRepository.saveAndFlush(customerMiMi);

        // Get all the customerMiMiList where email is not null
        defaultCustomerMiMiFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerMiMisByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerMiMi = customerMiMiRepository.saveAndFlush(customerMiMi);

        // Get all the customerMiMiList where email contains
        defaultCustomerMiMiFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomerMiMisByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerMiMi = customerMiMiRepository.saveAndFlush(customerMiMi);

        // Get all the customerMiMiList where email does not contain
        defaultCustomerMiMiFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomerMiMisByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerMiMi = customerMiMiRepository.saveAndFlush(customerMiMi);

        // Get all the customerMiMiList where phoneNumber equals to
        defaultCustomerMiMiFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerMiMisByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerMiMi = customerMiMiRepository.saveAndFlush(customerMiMi);

        // Get all the customerMiMiList where phoneNumber in
        defaultCustomerMiMiFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCustomerMiMisByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerMiMi = customerMiMiRepository.saveAndFlush(customerMiMi);

        // Get all the customerMiMiList where phoneNumber is not null
        defaultCustomerMiMiFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerMiMisByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerMiMi = customerMiMiRepository.saveAndFlush(customerMiMi);

        // Get all the customerMiMiList where phoneNumber contains
        defaultCustomerMiMiFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerMiMisByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerMiMi = customerMiMiRepository.saveAndFlush(customerMiMi);

        // Get all the customerMiMiList where phoneNumber does not contain
        defaultCustomerMiMiFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCustomerMiMisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            customerMiMiRepository.saveAndFlush(customerMiMi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        customerMiMi.setTenant(tenant);
        customerMiMiRepository.saveAndFlush(customerMiMi);
        Long tenantId = tenant.getId();
        // Get all the customerMiMiList where tenant equals to tenantId
        defaultCustomerMiMiShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the customerMiMiList where tenant equals to (tenantId + 1)
        defaultCustomerMiMiShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultCustomerMiMiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCustomerMiMiShouldBeFound(shouldBeFound);
        defaultCustomerMiMiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerMiMiShouldBeFound(String filter) throws Exception {
        restCustomerMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerMiMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restCustomerMiMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomerMiMiShouldNotBeFound(String filter) throws Exception {
        restCustomerMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerMiMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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

        restCustomerMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCustomerMiMi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCustomerMiMi))
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

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerMiMi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerMiMi))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerMiMi))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMiMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerMiMi)))
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

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerMiMi))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerMiMi))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMiMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(customerMiMi)))
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
