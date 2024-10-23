package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.CustomerThetaAsserts.*;
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
import xyz.jhmapstruct.domain.CustomerTheta;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.repository.CustomerThetaRepository;
import xyz.jhmapstruct.service.dto.CustomerThetaDTO;
import xyz.jhmapstruct.service.mapper.CustomerThetaMapper;

/**
 * Integration tests for the {@link CustomerThetaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomerThetaResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/customer-thetas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CustomerThetaRepository customerThetaRepository;

    @Autowired
    private CustomerThetaMapper customerThetaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerThetaMockMvc;

    private CustomerTheta customerTheta;

    private CustomerTheta insertedCustomerTheta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerTheta createEntity() {
        return new CustomerTheta()
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
    public static CustomerTheta createUpdatedEntity() {
        return new CustomerTheta()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        customerTheta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCustomerTheta != null) {
            customerThetaRepository.delete(insertedCustomerTheta);
            insertedCustomerTheta = null;
        }
    }

    @Test
    @Transactional
    void createCustomerTheta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CustomerTheta
        CustomerThetaDTO customerThetaDTO = customerThetaMapper.toDto(customerTheta);
        var returnedCustomerThetaDTO = om.readValue(
            restCustomerThetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerThetaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CustomerThetaDTO.class
        );

        // Validate the CustomerTheta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCustomerTheta = customerThetaMapper.toEntity(returnedCustomerThetaDTO);
        assertCustomerThetaUpdatableFieldsEquals(returnedCustomerTheta, getPersistedCustomerTheta(returnedCustomerTheta));

        insertedCustomerTheta = returnedCustomerTheta;
    }

    @Test
    @Transactional
    void createCustomerThetaWithExistingId() throws Exception {
        // Create the CustomerTheta with an existing ID
        customerTheta.setId(1L);
        CustomerThetaDTO customerThetaDTO = customerThetaMapper.toDto(customerTheta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerThetaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerTheta.setFirstName(null);

        // Create the CustomerTheta, which fails.
        CustomerThetaDTO customerThetaDTO = customerThetaMapper.toDto(customerTheta);

        restCustomerThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerThetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerTheta.setLastName(null);

        // Create the CustomerTheta, which fails.
        CustomerThetaDTO customerThetaDTO = customerThetaMapper.toDto(customerTheta);

        restCustomerThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerThetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerTheta.setEmail(null);

        // Create the CustomerTheta, which fails.
        CustomerThetaDTO customerThetaDTO = customerThetaMapper.toDto(customerTheta);

        restCustomerThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerThetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustomerThetas() throws Exception {
        // Initialize the database
        insertedCustomerTheta = customerThetaRepository.saveAndFlush(customerTheta);

        // Get all the customerThetaList
        restCustomerThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @Test
    @Transactional
    void getCustomerTheta() throws Exception {
        // Initialize the database
        insertedCustomerTheta = customerThetaRepository.saveAndFlush(customerTheta);

        // Get the customerTheta
        restCustomerThetaMockMvc
            .perform(get(ENTITY_API_URL_ID, customerTheta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customerTheta.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getCustomerThetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedCustomerTheta = customerThetaRepository.saveAndFlush(customerTheta);

        Long id = customerTheta.getId();

        defaultCustomerThetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCustomerThetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCustomerThetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCustomerThetasByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerTheta = customerThetaRepository.saveAndFlush(customerTheta);

        // Get all the customerThetaList where firstName equals to
        defaultCustomerThetaFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerThetasByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerTheta = customerThetaRepository.saveAndFlush(customerTheta);

        // Get all the customerThetaList where firstName in
        defaultCustomerThetaFiltering(
            "firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME,
            "firstName.in=" + UPDATED_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllCustomerThetasByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerTheta = customerThetaRepository.saveAndFlush(customerTheta);

        // Get all the customerThetaList where firstName is not null
        defaultCustomerThetaFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerThetasByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerTheta = customerThetaRepository.saveAndFlush(customerTheta);

        // Get all the customerThetaList where firstName contains
        defaultCustomerThetaFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerThetasByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerTheta = customerThetaRepository.saveAndFlush(customerTheta);

        // Get all the customerThetaList where firstName does not contain
        defaultCustomerThetaFiltering("firstName.doesNotContain=" + UPDATED_FIRST_NAME, "firstName.doesNotContain=" + DEFAULT_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerThetasByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerTheta = customerThetaRepository.saveAndFlush(customerTheta);

        // Get all the customerThetaList where lastName equals to
        defaultCustomerThetaFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerThetasByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerTheta = customerThetaRepository.saveAndFlush(customerTheta);

        // Get all the customerThetaList where lastName in
        defaultCustomerThetaFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerThetasByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerTheta = customerThetaRepository.saveAndFlush(customerTheta);

        // Get all the customerThetaList where lastName is not null
        defaultCustomerThetaFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerThetasByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerTheta = customerThetaRepository.saveAndFlush(customerTheta);

        // Get all the customerThetaList where lastName contains
        defaultCustomerThetaFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerThetasByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerTheta = customerThetaRepository.saveAndFlush(customerTheta);

        // Get all the customerThetaList where lastName does not contain
        defaultCustomerThetaFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerThetasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerTheta = customerThetaRepository.saveAndFlush(customerTheta);

        // Get all the customerThetaList where email equals to
        defaultCustomerThetaFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomerThetasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerTheta = customerThetaRepository.saveAndFlush(customerTheta);

        // Get all the customerThetaList where email in
        defaultCustomerThetaFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomerThetasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerTheta = customerThetaRepository.saveAndFlush(customerTheta);

        // Get all the customerThetaList where email is not null
        defaultCustomerThetaFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerThetasByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerTheta = customerThetaRepository.saveAndFlush(customerTheta);

        // Get all the customerThetaList where email contains
        defaultCustomerThetaFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomerThetasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerTheta = customerThetaRepository.saveAndFlush(customerTheta);

        // Get all the customerThetaList where email does not contain
        defaultCustomerThetaFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomerThetasByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerTheta = customerThetaRepository.saveAndFlush(customerTheta);

        // Get all the customerThetaList where phoneNumber equals to
        defaultCustomerThetaFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerThetasByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerTheta = customerThetaRepository.saveAndFlush(customerTheta);

        // Get all the customerThetaList where phoneNumber in
        defaultCustomerThetaFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCustomerThetasByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerTheta = customerThetaRepository.saveAndFlush(customerTheta);

        // Get all the customerThetaList where phoneNumber is not null
        defaultCustomerThetaFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerThetasByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerTheta = customerThetaRepository.saveAndFlush(customerTheta);

        // Get all the customerThetaList where phoneNumber contains
        defaultCustomerThetaFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomerThetasByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerTheta = customerThetaRepository.saveAndFlush(customerTheta);

        // Get all the customerThetaList where phoneNumber does not contain
        defaultCustomerThetaFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCustomerThetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            customerThetaRepository.saveAndFlush(customerTheta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        customerTheta.setTenant(tenant);
        customerThetaRepository.saveAndFlush(customerTheta);
        Long tenantId = tenant.getId();
        // Get all the customerThetaList where tenant equals to tenantId
        defaultCustomerThetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the customerThetaList where tenant equals to (tenantId + 1)
        defaultCustomerThetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultCustomerThetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCustomerThetaShouldBeFound(shouldBeFound);
        defaultCustomerThetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerThetaShouldBeFound(String filter) throws Exception {
        restCustomerThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restCustomerThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomerThetaShouldNotBeFound(String filter) throws Exception {
        restCustomerThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCustomerTheta() throws Exception {
        // Get the customerTheta
        restCustomerThetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomerTheta() throws Exception {
        // Initialize the database
        insertedCustomerTheta = customerThetaRepository.saveAndFlush(customerTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerTheta
        CustomerTheta updatedCustomerTheta = customerThetaRepository.findById(customerTheta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCustomerTheta are not directly saved in db
        em.detach(updatedCustomerTheta);
        updatedCustomerTheta
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        CustomerThetaDTO customerThetaDTO = customerThetaMapper.toDto(updatedCustomerTheta);

        restCustomerThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerThetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerThetaDTO))
            )
            .andExpect(status().isOk());

        // Validate the CustomerTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCustomerThetaToMatchAllProperties(updatedCustomerTheta);
    }

    @Test
    @Transactional
    void putNonExistingCustomerTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerTheta.setId(longCount.incrementAndGet());

        // Create the CustomerTheta
        CustomerThetaDTO customerThetaDTO = customerThetaMapper.toDto(customerTheta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerThetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomerTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerTheta.setId(longCount.incrementAndGet());

        // Create the CustomerTheta
        CustomerThetaDTO customerThetaDTO = customerThetaMapper.toDto(customerTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomerTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerTheta.setId(longCount.incrementAndGet());

        // Create the CustomerTheta
        CustomerThetaDTO customerThetaDTO = customerThetaMapper.toDto(customerTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerThetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerThetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerThetaWithPatch() throws Exception {
        // Initialize the database
        insertedCustomerTheta = customerThetaRepository.saveAndFlush(customerTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerTheta using partial update
        CustomerTheta partialUpdatedCustomerTheta = new CustomerTheta();
        partialUpdatedCustomerTheta.setId(customerTheta.getId());

        partialUpdatedCustomerTheta.lastName(UPDATED_LAST_NAME).phoneNumber(UPDATED_PHONE_NUMBER);

        restCustomerThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomerTheta))
            )
            .andExpect(status().isOk());

        // Validate the CustomerTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomerThetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCustomerTheta, customerTheta),
            getPersistedCustomerTheta(customerTheta)
        );
    }

    @Test
    @Transactional
    void fullUpdateCustomerThetaWithPatch() throws Exception {
        // Initialize the database
        insertedCustomerTheta = customerThetaRepository.saveAndFlush(customerTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerTheta using partial update
        CustomerTheta partialUpdatedCustomerTheta = new CustomerTheta();
        partialUpdatedCustomerTheta.setId(customerTheta.getId());

        partialUpdatedCustomerTheta
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restCustomerThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomerTheta))
            )
            .andExpect(status().isOk());

        // Validate the CustomerTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomerThetaUpdatableFieldsEquals(partialUpdatedCustomerTheta, getPersistedCustomerTheta(partialUpdatedCustomerTheta));
    }

    @Test
    @Transactional
    void patchNonExistingCustomerTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerTheta.setId(longCount.incrementAndGet());

        // Create the CustomerTheta
        CustomerThetaDTO customerThetaDTO = customerThetaMapper.toDto(customerTheta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerThetaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomerTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerTheta.setId(longCount.incrementAndGet());

        // Create the CustomerTheta
        CustomerThetaDTO customerThetaDTO = customerThetaMapper.toDto(customerTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomerTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerTheta.setId(longCount.incrementAndGet());

        // Create the CustomerTheta
        CustomerThetaDTO customerThetaDTO = customerThetaMapper.toDto(customerTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerThetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(customerThetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomerTheta() throws Exception {
        // Initialize the database
        insertedCustomerTheta = customerThetaRepository.saveAndFlush(customerTheta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the customerTheta
        restCustomerThetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, customerTheta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return customerThetaRepository.count();
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

    protected CustomerTheta getPersistedCustomerTheta(CustomerTheta customerTheta) {
        return customerThetaRepository.findById(customerTheta.getId()).orElseThrow();
    }

    protected void assertPersistedCustomerThetaToMatchAllProperties(CustomerTheta expectedCustomerTheta) {
        assertCustomerThetaAllPropertiesEquals(expectedCustomerTheta, getPersistedCustomerTheta(expectedCustomerTheta));
    }

    protected void assertPersistedCustomerThetaToMatchUpdatableProperties(CustomerTheta expectedCustomerTheta) {
        assertCustomerThetaAllUpdatablePropertiesEquals(expectedCustomerTheta, getPersistedCustomerTheta(expectedCustomerTheta));
    }
}
