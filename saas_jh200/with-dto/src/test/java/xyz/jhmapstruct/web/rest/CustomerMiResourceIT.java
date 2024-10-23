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
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.repository.CustomerMiRepository;
import xyz.jhmapstruct.service.dto.CustomerMiDTO;
import xyz.jhmapstruct.service.mapper.CustomerMiMapper;

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
    private CustomerMiMapper customerMiMapper;

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
        CustomerMiDTO customerMiDTO = customerMiMapper.toDto(customerMi);
        var returnedCustomerMiDTO = om.readValue(
            restCustomerMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerMiDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CustomerMiDTO.class
        );

        // Validate the CustomerMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCustomerMi = customerMiMapper.toEntity(returnedCustomerMiDTO);
        assertCustomerMiUpdatableFieldsEquals(returnedCustomerMi, getPersistedCustomerMi(returnedCustomerMi));

        insertedCustomerMi = returnedCustomerMi;
    }

    @Test
    @Transactional
    void createCustomerMiWithExistingId() throws Exception {
        // Create the CustomerMi with an existing ID
        customerMi.setId(1L);
        CustomerMiDTO customerMiDTO = customerMiMapper.toDto(customerMi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerMiDTO)))
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
        CustomerMiDTO customerMiDTO = customerMiMapper.toDto(customerMi);

        restCustomerMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerMiDTO)))
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
        CustomerMiDTO customerMiDTO = customerMiMapper.toDto(customerMi);

        restCustomerMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerMiDTO)))
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
        CustomerMiDTO customerMiDTO = customerMiMapper.toDto(customerMi);

        restCustomerMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerMiDTO)))
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
    void getCustomerMisByIdFiltering() throws Exception {
        // Initialize the database
        insertedCustomerMi = customerMiRepository.saveAndFlush(customerMi);

        Long id = customerMi.getId();

        defaultCustomerMiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCustomerMiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCustomerMiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCustomerMisByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerMi = customerMiRepository.saveAndFlush(customerMi);

        // Get all the customerMiList where firstName equals to
        defaultCustomerMiFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerMisByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerMi = customerMiRepository.saveAndFlush(customerMi);

        // Get all the customerMiList where firstName in
        defaultCustomerMiFiltering("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME, "firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerMisByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerMi = customerMiRepository.saveAndFlush(customerMi);

        // Get all the customerMiList where firstName is not null
        defaultCustomerMiFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerMisByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerMi = customerMiRepository.saveAndFlush(customerMi);

        // Get all the customerMiList where firstName contains
        defaultCustomerMiFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerMisByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerMi = customerMiRepository.saveAndFlush(customerMi);

        // Get all the customerMiList where firstName does not contain
        defaultCustomerMiFiltering("firstName.doesNotContain=" + UPDATED_FIRST_NAME, "firstName.doesNotContain=" + DEFAULT_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerMisByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerMi = customerMiRepository.saveAndFlush(customerMi);

        // Get all the customerMiList where lastName equals to
        defaultCustomerMiFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerMisByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerMi = customerMiRepository.saveAndFlush(customerMi);

        // Get all the customerMiList where lastName in
        defaultCustomerMiFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerMisByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerMi = customerMiRepository.saveAndFlush(customerMi);

        // Get all the customerMiList where lastName is not null
        defaultCustomerMiFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerMisByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerMi = customerMiRepository.saveAndFlush(customerMi);

        // Get all the customerMiList where lastName contains
        defaultCustomerMiFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerMisByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerMi = customerMiRepository.saveAndFlush(customerMi);

        // Get all the customerMiList where lastName does not contain
        defaultCustomerMiFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerMisByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerMi = customerMiRepository.saveAndFlush(customerMi);

        // Get all the customerMiList where email equals to
        defaultCustomerMiFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomerMisByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerMi = customerMiRepository.saveAndFlush(customerMi);

        // Get all the customerMiList where email in
        defaultCustomerMiFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomerMisByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerMi = customerMiRepository.saveAndFlush(customerMi);

        // Get all the customerMiList where email is not null
        defaultCustomerMiFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerMisByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerMi = customerMiRepository.saveAndFlush(customerMi);

        // Get all the customerMiList where email contains
        defaultCustomerMiFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomerMisByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerMi = customerMiRepository.saveAndFlush(customerMi);

        // Get all the customerMiList where email does not contain
        defaultCustomerMiFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomerMisByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerMi = customerMiRepository.saveAndFlush(customerMi);

        // Get all the customerMiList where phoneNumber equals to
        defaultCustomerMiFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerMisByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerMi = customerMiRepository.saveAndFlush(customerMi);

        // Get all the customerMiList where phoneNumber in
        defaultCustomerMiFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCustomerMisByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerMi = customerMiRepository.saveAndFlush(customerMi);

        // Get all the customerMiList where phoneNumber is not null
        defaultCustomerMiFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerMisByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerMi = customerMiRepository.saveAndFlush(customerMi);

        // Get all the customerMiList where phoneNumber contains
        defaultCustomerMiFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerMisByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerMi = customerMiRepository.saveAndFlush(customerMi);

        // Get all the customerMiList where phoneNumber does not contain
        defaultCustomerMiFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCustomerMisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            customerMiRepository.saveAndFlush(customerMi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        customerMi.setTenant(tenant);
        customerMiRepository.saveAndFlush(customerMi);
        Long tenantId = tenant.getId();
        // Get all the customerMiList where tenant equals to tenantId
        defaultCustomerMiShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the customerMiList where tenant equals to (tenantId + 1)
        defaultCustomerMiShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultCustomerMiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCustomerMiShouldBeFound(shouldBeFound);
        defaultCustomerMiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerMiShouldBeFound(String filter) throws Exception {
        restCustomerMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restCustomerMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomerMiShouldNotBeFound(String filter) throws Exception {
        restCustomerMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
        CustomerMiDTO customerMiDTO = customerMiMapper.toDto(updatedCustomerMi);

        restCustomerMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerMiDTO))
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

        // Create the CustomerMi
        CustomerMiDTO customerMiDTO = customerMiMapper.toDto(customerMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerMiDTO))
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

        // Create the CustomerMi
        CustomerMiDTO customerMiDTO = customerMiMapper.toDto(customerMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerMiDTO))
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

        // Create the CustomerMi
        CustomerMiDTO customerMiDTO = customerMiMapper.toDto(customerMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerMiDTO)))
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

        // Create the CustomerMi
        CustomerMiDTO customerMiDTO = customerMiMapper.toDto(customerMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerMiDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerMiDTO))
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

        // Create the CustomerMi
        CustomerMiDTO customerMiDTO = customerMiMapper.toDto(customerMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerMiDTO))
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

        // Create the CustomerMi
        CustomerMiDTO customerMiDTO = customerMiMapper.toDto(customerMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(customerMiDTO)))
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
