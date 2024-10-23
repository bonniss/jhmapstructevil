package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.CustomerAlphaAsserts.*;
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
import xyz.jhmapstruct.domain.CustomerAlpha;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.repository.CustomerAlphaRepository;
import xyz.jhmapstruct.service.dto.CustomerAlphaDTO;
import xyz.jhmapstruct.service.mapper.CustomerAlphaMapper;

/**
 * Integration tests for the {@link CustomerAlphaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomerAlphaResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/customer-alphas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CustomerAlphaRepository customerAlphaRepository;

    @Autowired
    private CustomerAlphaMapper customerAlphaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerAlphaMockMvc;

    private CustomerAlpha customerAlpha;

    private CustomerAlpha insertedCustomerAlpha;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerAlpha createEntity() {
        return new CustomerAlpha()
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
    public static CustomerAlpha createUpdatedEntity() {
        return new CustomerAlpha()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        customerAlpha = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCustomerAlpha != null) {
            customerAlphaRepository.delete(insertedCustomerAlpha);
            insertedCustomerAlpha = null;
        }
    }

    @Test
    @Transactional
    void createCustomerAlpha() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CustomerAlpha
        CustomerAlphaDTO customerAlphaDTO = customerAlphaMapper.toDto(customerAlpha);
        var returnedCustomerAlphaDTO = om.readValue(
            restCustomerAlphaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerAlphaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CustomerAlphaDTO.class
        );

        // Validate the CustomerAlpha in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCustomerAlpha = customerAlphaMapper.toEntity(returnedCustomerAlphaDTO);
        assertCustomerAlphaUpdatableFieldsEquals(returnedCustomerAlpha, getPersistedCustomerAlpha(returnedCustomerAlpha));

        insertedCustomerAlpha = returnedCustomerAlpha;
    }

    @Test
    @Transactional
    void createCustomerAlphaWithExistingId() throws Exception {
        // Create the CustomerAlpha with an existing ID
        customerAlpha.setId(1L);
        CustomerAlphaDTO customerAlphaDTO = customerAlphaMapper.toDto(customerAlpha);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerAlphaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerAlpha.setFirstName(null);

        // Create the CustomerAlpha, which fails.
        CustomerAlphaDTO customerAlphaDTO = customerAlphaMapper.toDto(customerAlpha);

        restCustomerAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerAlphaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerAlpha.setLastName(null);

        // Create the CustomerAlpha, which fails.
        CustomerAlphaDTO customerAlphaDTO = customerAlphaMapper.toDto(customerAlpha);

        restCustomerAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerAlphaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerAlpha.setEmail(null);

        // Create the CustomerAlpha, which fails.
        CustomerAlphaDTO customerAlphaDTO = customerAlphaMapper.toDto(customerAlpha);

        restCustomerAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerAlphaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustomerAlphas() throws Exception {
        // Initialize the database
        insertedCustomerAlpha = customerAlphaRepository.saveAndFlush(customerAlpha);

        // Get all the customerAlphaList
        restCustomerAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @Test
    @Transactional
    void getCustomerAlpha() throws Exception {
        // Initialize the database
        insertedCustomerAlpha = customerAlphaRepository.saveAndFlush(customerAlpha);

        // Get the customerAlpha
        restCustomerAlphaMockMvc
            .perform(get(ENTITY_API_URL_ID, customerAlpha.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customerAlpha.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getCustomerAlphasByIdFiltering() throws Exception {
        // Initialize the database
        insertedCustomerAlpha = customerAlphaRepository.saveAndFlush(customerAlpha);

        Long id = customerAlpha.getId();

        defaultCustomerAlphaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCustomerAlphaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCustomerAlphaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCustomerAlphasByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerAlpha = customerAlphaRepository.saveAndFlush(customerAlpha);

        // Get all the customerAlphaList where firstName equals to
        defaultCustomerAlphaFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerAlphasByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerAlpha = customerAlphaRepository.saveAndFlush(customerAlpha);

        // Get all the customerAlphaList where firstName in
        defaultCustomerAlphaFiltering(
            "firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME,
            "firstName.in=" + UPDATED_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllCustomerAlphasByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerAlpha = customerAlphaRepository.saveAndFlush(customerAlpha);

        // Get all the customerAlphaList where firstName is not null
        defaultCustomerAlphaFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerAlphasByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerAlpha = customerAlphaRepository.saveAndFlush(customerAlpha);

        // Get all the customerAlphaList where firstName contains
        defaultCustomerAlphaFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerAlphasByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerAlpha = customerAlphaRepository.saveAndFlush(customerAlpha);

        // Get all the customerAlphaList where firstName does not contain
        defaultCustomerAlphaFiltering("firstName.doesNotContain=" + UPDATED_FIRST_NAME, "firstName.doesNotContain=" + DEFAULT_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerAlphasByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerAlpha = customerAlphaRepository.saveAndFlush(customerAlpha);

        // Get all the customerAlphaList where lastName equals to
        defaultCustomerAlphaFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerAlphasByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerAlpha = customerAlphaRepository.saveAndFlush(customerAlpha);

        // Get all the customerAlphaList where lastName in
        defaultCustomerAlphaFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerAlphasByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerAlpha = customerAlphaRepository.saveAndFlush(customerAlpha);

        // Get all the customerAlphaList where lastName is not null
        defaultCustomerAlphaFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerAlphasByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerAlpha = customerAlphaRepository.saveAndFlush(customerAlpha);

        // Get all the customerAlphaList where lastName contains
        defaultCustomerAlphaFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerAlphasByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerAlpha = customerAlphaRepository.saveAndFlush(customerAlpha);

        // Get all the customerAlphaList where lastName does not contain
        defaultCustomerAlphaFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerAlphasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerAlpha = customerAlphaRepository.saveAndFlush(customerAlpha);

        // Get all the customerAlphaList where email equals to
        defaultCustomerAlphaFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomerAlphasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerAlpha = customerAlphaRepository.saveAndFlush(customerAlpha);

        // Get all the customerAlphaList where email in
        defaultCustomerAlphaFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomerAlphasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerAlpha = customerAlphaRepository.saveAndFlush(customerAlpha);

        // Get all the customerAlphaList where email is not null
        defaultCustomerAlphaFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerAlphasByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerAlpha = customerAlphaRepository.saveAndFlush(customerAlpha);

        // Get all the customerAlphaList where email contains
        defaultCustomerAlphaFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomerAlphasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerAlpha = customerAlphaRepository.saveAndFlush(customerAlpha);

        // Get all the customerAlphaList where email does not contain
        defaultCustomerAlphaFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomerAlphasByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerAlpha = customerAlphaRepository.saveAndFlush(customerAlpha);

        // Get all the customerAlphaList where phoneNumber equals to
        defaultCustomerAlphaFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerAlphasByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerAlpha = customerAlphaRepository.saveAndFlush(customerAlpha);

        // Get all the customerAlphaList where phoneNumber in
        defaultCustomerAlphaFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCustomerAlphasByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerAlpha = customerAlphaRepository.saveAndFlush(customerAlpha);

        // Get all the customerAlphaList where phoneNumber is not null
        defaultCustomerAlphaFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerAlphasByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerAlpha = customerAlphaRepository.saveAndFlush(customerAlpha);

        // Get all the customerAlphaList where phoneNumber contains
        defaultCustomerAlphaFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerAlphasByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerAlpha = customerAlphaRepository.saveAndFlush(customerAlpha);

        // Get all the customerAlphaList where phoneNumber does not contain
        defaultCustomerAlphaFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCustomerAlphasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            customerAlphaRepository.saveAndFlush(customerAlpha);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        customerAlpha.setTenant(tenant);
        customerAlphaRepository.saveAndFlush(customerAlpha);
        Long tenantId = tenant.getId();
        // Get all the customerAlphaList where tenant equals to tenantId
        defaultCustomerAlphaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the customerAlphaList where tenant equals to (tenantId + 1)
        defaultCustomerAlphaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultCustomerAlphaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCustomerAlphaShouldBeFound(shouldBeFound);
        defaultCustomerAlphaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerAlphaShouldBeFound(String filter) throws Exception {
        restCustomerAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restCustomerAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomerAlphaShouldNotBeFound(String filter) throws Exception {
        restCustomerAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCustomerAlpha() throws Exception {
        // Get the customerAlpha
        restCustomerAlphaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomerAlpha() throws Exception {
        // Initialize the database
        insertedCustomerAlpha = customerAlphaRepository.saveAndFlush(customerAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerAlpha
        CustomerAlpha updatedCustomerAlpha = customerAlphaRepository.findById(customerAlpha.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCustomerAlpha are not directly saved in db
        em.detach(updatedCustomerAlpha);
        updatedCustomerAlpha
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        CustomerAlphaDTO customerAlphaDTO = customerAlphaMapper.toDto(updatedCustomerAlpha);

        restCustomerAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerAlphaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerAlphaDTO))
            )
            .andExpect(status().isOk());

        // Validate the CustomerAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCustomerAlphaToMatchAllProperties(updatedCustomerAlpha);
    }

    @Test
    @Transactional
    void putNonExistingCustomerAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerAlpha.setId(longCount.incrementAndGet());

        // Create the CustomerAlpha
        CustomerAlphaDTO customerAlphaDTO = customerAlphaMapper.toDto(customerAlpha);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerAlphaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomerAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerAlpha.setId(longCount.incrementAndGet());

        // Create the CustomerAlpha
        CustomerAlphaDTO customerAlphaDTO = customerAlphaMapper.toDto(customerAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomerAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerAlpha.setId(longCount.incrementAndGet());

        // Create the CustomerAlpha
        CustomerAlphaDTO customerAlphaDTO = customerAlphaMapper.toDto(customerAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerAlphaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerAlphaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedCustomerAlpha = customerAlphaRepository.saveAndFlush(customerAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerAlpha using partial update
        CustomerAlpha partialUpdatedCustomerAlpha = new CustomerAlpha();
        partialUpdatedCustomerAlpha.setId(customerAlpha.getId());

        partialUpdatedCustomerAlpha.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME).email(UPDATED_EMAIL);

        restCustomerAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomerAlpha))
            )
            .andExpect(status().isOk());

        // Validate the CustomerAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomerAlphaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCustomerAlpha, customerAlpha),
            getPersistedCustomerAlpha(customerAlpha)
        );
    }

    @Test
    @Transactional
    void fullUpdateCustomerAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedCustomerAlpha = customerAlphaRepository.saveAndFlush(customerAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerAlpha using partial update
        CustomerAlpha partialUpdatedCustomerAlpha = new CustomerAlpha();
        partialUpdatedCustomerAlpha.setId(customerAlpha.getId());

        partialUpdatedCustomerAlpha
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restCustomerAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomerAlpha))
            )
            .andExpect(status().isOk());

        // Validate the CustomerAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomerAlphaUpdatableFieldsEquals(partialUpdatedCustomerAlpha, getPersistedCustomerAlpha(partialUpdatedCustomerAlpha));
    }

    @Test
    @Transactional
    void patchNonExistingCustomerAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerAlpha.setId(longCount.incrementAndGet());

        // Create the CustomerAlpha
        CustomerAlphaDTO customerAlphaDTO = customerAlphaMapper.toDto(customerAlpha);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerAlphaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomerAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerAlpha.setId(longCount.incrementAndGet());

        // Create the CustomerAlpha
        CustomerAlphaDTO customerAlphaDTO = customerAlphaMapper.toDto(customerAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomerAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerAlpha.setId(longCount.incrementAndGet());

        // Create the CustomerAlpha
        CustomerAlphaDTO customerAlphaDTO = customerAlphaMapper.toDto(customerAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerAlphaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(customerAlphaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomerAlpha() throws Exception {
        // Initialize the database
        insertedCustomerAlpha = customerAlphaRepository.saveAndFlush(customerAlpha);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the customerAlpha
        restCustomerAlphaMockMvc
            .perform(delete(ENTITY_API_URL_ID, customerAlpha.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return customerAlphaRepository.count();
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

    protected CustomerAlpha getPersistedCustomerAlpha(CustomerAlpha customerAlpha) {
        return customerAlphaRepository.findById(customerAlpha.getId()).orElseThrow();
    }

    protected void assertPersistedCustomerAlphaToMatchAllProperties(CustomerAlpha expectedCustomerAlpha) {
        assertCustomerAlphaAllPropertiesEquals(expectedCustomerAlpha, getPersistedCustomerAlpha(expectedCustomerAlpha));
    }

    protected void assertPersistedCustomerAlphaToMatchUpdatableProperties(CustomerAlpha expectedCustomerAlpha) {
        assertCustomerAlphaAllUpdatablePropertiesEquals(expectedCustomerAlpha, getPersistedCustomerAlpha(expectedCustomerAlpha));
    }
}
