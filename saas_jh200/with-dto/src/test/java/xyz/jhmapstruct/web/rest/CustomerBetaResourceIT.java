package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.CustomerBetaAsserts.*;
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
import xyz.jhmapstruct.domain.CustomerBeta;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.repository.CustomerBetaRepository;
import xyz.jhmapstruct.service.dto.CustomerBetaDTO;
import xyz.jhmapstruct.service.mapper.CustomerBetaMapper;

/**
 * Integration tests for the {@link CustomerBetaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomerBetaResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/customer-betas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CustomerBetaRepository customerBetaRepository;

    @Autowired
    private CustomerBetaMapper customerBetaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerBetaMockMvc;

    private CustomerBeta customerBeta;

    private CustomerBeta insertedCustomerBeta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerBeta createEntity() {
        return new CustomerBeta()
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
    public static CustomerBeta createUpdatedEntity() {
        return new CustomerBeta()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        customerBeta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCustomerBeta != null) {
            customerBetaRepository.delete(insertedCustomerBeta);
            insertedCustomerBeta = null;
        }
    }

    @Test
    @Transactional
    void createCustomerBeta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CustomerBeta
        CustomerBetaDTO customerBetaDTO = customerBetaMapper.toDto(customerBeta);
        var returnedCustomerBetaDTO = om.readValue(
            restCustomerBetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerBetaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CustomerBetaDTO.class
        );

        // Validate the CustomerBeta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCustomerBeta = customerBetaMapper.toEntity(returnedCustomerBetaDTO);
        assertCustomerBetaUpdatableFieldsEquals(returnedCustomerBeta, getPersistedCustomerBeta(returnedCustomerBeta));

        insertedCustomerBeta = returnedCustomerBeta;
    }

    @Test
    @Transactional
    void createCustomerBetaWithExistingId() throws Exception {
        // Create the CustomerBeta with an existing ID
        customerBeta.setId(1L);
        CustomerBetaDTO customerBetaDTO = customerBetaMapper.toDto(customerBeta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerBetaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerBeta.setFirstName(null);

        // Create the CustomerBeta, which fails.
        CustomerBetaDTO customerBetaDTO = customerBetaMapper.toDto(customerBeta);

        restCustomerBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerBetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerBeta.setLastName(null);

        // Create the CustomerBeta, which fails.
        CustomerBetaDTO customerBetaDTO = customerBetaMapper.toDto(customerBeta);

        restCustomerBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerBetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerBeta.setEmail(null);

        // Create the CustomerBeta, which fails.
        CustomerBetaDTO customerBetaDTO = customerBetaMapper.toDto(customerBeta);

        restCustomerBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerBetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustomerBetas() throws Exception {
        // Initialize the database
        insertedCustomerBeta = customerBetaRepository.saveAndFlush(customerBeta);

        // Get all the customerBetaList
        restCustomerBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @Test
    @Transactional
    void getCustomerBeta() throws Exception {
        // Initialize the database
        insertedCustomerBeta = customerBetaRepository.saveAndFlush(customerBeta);

        // Get the customerBeta
        restCustomerBetaMockMvc
            .perform(get(ENTITY_API_URL_ID, customerBeta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customerBeta.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getCustomerBetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedCustomerBeta = customerBetaRepository.saveAndFlush(customerBeta);

        Long id = customerBeta.getId();

        defaultCustomerBetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCustomerBetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCustomerBetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCustomerBetasByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerBeta = customerBetaRepository.saveAndFlush(customerBeta);

        // Get all the customerBetaList where firstName equals to
        defaultCustomerBetaFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerBetasByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerBeta = customerBetaRepository.saveAndFlush(customerBeta);

        // Get all the customerBetaList where firstName in
        defaultCustomerBetaFiltering("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME, "firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerBetasByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerBeta = customerBetaRepository.saveAndFlush(customerBeta);

        // Get all the customerBetaList where firstName is not null
        defaultCustomerBetaFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerBetasByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerBeta = customerBetaRepository.saveAndFlush(customerBeta);

        // Get all the customerBetaList where firstName contains
        defaultCustomerBetaFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerBetasByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerBeta = customerBetaRepository.saveAndFlush(customerBeta);

        // Get all the customerBetaList where firstName does not contain
        defaultCustomerBetaFiltering("firstName.doesNotContain=" + UPDATED_FIRST_NAME, "firstName.doesNotContain=" + DEFAULT_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerBetasByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerBeta = customerBetaRepository.saveAndFlush(customerBeta);

        // Get all the customerBetaList where lastName equals to
        defaultCustomerBetaFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerBetasByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerBeta = customerBetaRepository.saveAndFlush(customerBeta);

        // Get all the customerBetaList where lastName in
        defaultCustomerBetaFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerBetasByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerBeta = customerBetaRepository.saveAndFlush(customerBeta);

        // Get all the customerBetaList where lastName is not null
        defaultCustomerBetaFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerBetasByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerBeta = customerBetaRepository.saveAndFlush(customerBeta);

        // Get all the customerBetaList where lastName contains
        defaultCustomerBetaFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerBetasByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerBeta = customerBetaRepository.saveAndFlush(customerBeta);

        // Get all the customerBetaList where lastName does not contain
        defaultCustomerBetaFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerBetasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerBeta = customerBetaRepository.saveAndFlush(customerBeta);

        // Get all the customerBetaList where email equals to
        defaultCustomerBetaFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomerBetasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerBeta = customerBetaRepository.saveAndFlush(customerBeta);

        // Get all the customerBetaList where email in
        defaultCustomerBetaFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomerBetasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerBeta = customerBetaRepository.saveAndFlush(customerBeta);

        // Get all the customerBetaList where email is not null
        defaultCustomerBetaFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerBetasByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerBeta = customerBetaRepository.saveAndFlush(customerBeta);

        // Get all the customerBetaList where email contains
        defaultCustomerBetaFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomerBetasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerBeta = customerBetaRepository.saveAndFlush(customerBeta);

        // Get all the customerBetaList where email does not contain
        defaultCustomerBetaFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomerBetasByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerBeta = customerBetaRepository.saveAndFlush(customerBeta);

        // Get all the customerBetaList where phoneNumber equals to
        defaultCustomerBetaFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerBetasByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerBeta = customerBetaRepository.saveAndFlush(customerBeta);

        // Get all the customerBetaList where phoneNumber in
        defaultCustomerBetaFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCustomerBetasByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerBeta = customerBetaRepository.saveAndFlush(customerBeta);

        // Get all the customerBetaList where phoneNumber is not null
        defaultCustomerBetaFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerBetasByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerBeta = customerBetaRepository.saveAndFlush(customerBeta);

        // Get all the customerBetaList where phoneNumber contains
        defaultCustomerBetaFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerBetasByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerBeta = customerBetaRepository.saveAndFlush(customerBeta);

        // Get all the customerBetaList where phoneNumber does not contain
        defaultCustomerBetaFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCustomerBetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            customerBetaRepository.saveAndFlush(customerBeta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        customerBeta.setTenant(tenant);
        customerBetaRepository.saveAndFlush(customerBeta);
        Long tenantId = tenant.getId();
        // Get all the customerBetaList where tenant equals to tenantId
        defaultCustomerBetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the customerBetaList where tenant equals to (tenantId + 1)
        defaultCustomerBetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultCustomerBetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCustomerBetaShouldBeFound(shouldBeFound);
        defaultCustomerBetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerBetaShouldBeFound(String filter) throws Exception {
        restCustomerBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restCustomerBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomerBetaShouldNotBeFound(String filter) throws Exception {
        restCustomerBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCustomerBeta() throws Exception {
        // Get the customerBeta
        restCustomerBetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomerBeta() throws Exception {
        // Initialize the database
        insertedCustomerBeta = customerBetaRepository.saveAndFlush(customerBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerBeta
        CustomerBeta updatedCustomerBeta = customerBetaRepository.findById(customerBeta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCustomerBeta are not directly saved in db
        em.detach(updatedCustomerBeta);
        updatedCustomerBeta
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        CustomerBetaDTO customerBetaDTO = customerBetaMapper.toDto(updatedCustomerBeta);

        restCustomerBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerBetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerBetaDTO))
            )
            .andExpect(status().isOk());

        // Validate the CustomerBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCustomerBetaToMatchAllProperties(updatedCustomerBeta);
    }

    @Test
    @Transactional
    void putNonExistingCustomerBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerBeta.setId(longCount.incrementAndGet());

        // Create the CustomerBeta
        CustomerBetaDTO customerBetaDTO = customerBetaMapper.toDto(customerBeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerBetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomerBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerBeta.setId(longCount.incrementAndGet());

        // Create the CustomerBeta
        CustomerBetaDTO customerBetaDTO = customerBetaMapper.toDto(customerBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomerBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerBeta.setId(longCount.incrementAndGet());

        // Create the CustomerBeta
        CustomerBetaDTO customerBetaDTO = customerBetaMapper.toDto(customerBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerBetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerBetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerBetaWithPatch() throws Exception {
        // Initialize the database
        insertedCustomerBeta = customerBetaRepository.saveAndFlush(customerBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerBeta using partial update
        CustomerBeta partialUpdatedCustomerBeta = new CustomerBeta();
        partialUpdatedCustomerBeta.setId(customerBeta.getId());

        partialUpdatedCustomerBeta.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME);

        restCustomerBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomerBeta))
            )
            .andExpect(status().isOk());

        // Validate the CustomerBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomerBetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCustomerBeta, customerBeta),
            getPersistedCustomerBeta(customerBeta)
        );
    }

    @Test
    @Transactional
    void fullUpdateCustomerBetaWithPatch() throws Exception {
        // Initialize the database
        insertedCustomerBeta = customerBetaRepository.saveAndFlush(customerBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerBeta using partial update
        CustomerBeta partialUpdatedCustomerBeta = new CustomerBeta();
        partialUpdatedCustomerBeta.setId(customerBeta.getId());

        partialUpdatedCustomerBeta
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restCustomerBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomerBeta))
            )
            .andExpect(status().isOk());

        // Validate the CustomerBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomerBetaUpdatableFieldsEquals(partialUpdatedCustomerBeta, getPersistedCustomerBeta(partialUpdatedCustomerBeta));
    }

    @Test
    @Transactional
    void patchNonExistingCustomerBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerBeta.setId(longCount.incrementAndGet());

        // Create the CustomerBeta
        CustomerBetaDTO customerBetaDTO = customerBetaMapper.toDto(customerBeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerBetaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomerBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerBeta.setId(longCount.incrementAndGet());

        // Create the CustomerBeta
        CustomerBetaDTO customerBetaDTO = customerBetaMapper.toDto(customerBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomerBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerBeta.setId(longCount.incrementAndGet());

        // Create the CustomerBeta
        CustomerBetaDTO customerBetaDTO = customerBetaMapper.toDto(customerBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerBetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(customerBetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomerBeta() throws Exception {
        // Initialize the database
        insertedCustomerBeta = customerBetaRepository.saveAndFlush(customerBeta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the customerBeta
        restCustomerBetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, customerBeta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return customerBetaRepository.count();
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

    protected CustomerBeta getPersistedCustomerBeta(CustomerBeta customerBeta) {
        return customerBetaRepository.findById(customerBeta.getId()).orElseThrow();
    }

    protected void assertPersistedCustomerBetaToMatchAllProperties(CustomerBeta expectedCustomerBeta) {
        assertCustomerBetaAllPropertiesEquals(expectedCustomerBeta, getPersistedCustomerBeta(expectedCustomerBeta));
    }

    protected void assertPersistedCustomerBetaToMatchUpdatableProperties(CustomerBeta expectedCustomerBeta) {
        assertCustomerBetaAllUpdatablePropertiesEquals(expectedCustomerBeta, getPersistedCustomerBeta(expectedCustomerBeta));
    }
}
