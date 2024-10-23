package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextCustomerAsserts.*;
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
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCustomer;
import xyz.jhmapstruct.repository.NextCustomerRepository;
import xyz.jhmapstruct.service.dto.NextCustomerDTO;
import xyz.jhmapstruct.service.mapper.NextCustomerMapper;

/**
 * Integration tests for the {@link NextCustomerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextCustomerResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/next-customers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextCustomerRepository nextCustomerRepository;

    @Autowired
    private NextCustomerMapper nextCustomerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextCustomerMockMvc;

    private NextCustomer nextCustomer;

    private NextCustomer insertedNextCustomer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextCustomer createEntity() {
        return new NextCustomer()
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
    public static NextCustomer createUpdatedEntity() {
        return new NextCustomer()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        nextCustomer = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextCustomer != null) {
            nextCustomerRepository.delete(insertedNextCustomer);
            insertedNextCustomer = null;
        }
    }

    @Test
    @Transactional
    void createNextCustomer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextCustomer
        NextCustomerDTO nextCustomerDTO = nextCustomerMapper.toDto(nextCustomer);
        var returnedNextCustomerDTO = om.readValue(
            restNextCustomerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextCustomerDTO.class
        );

        // Validate the NextCustomer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextCustomer = nextCustomerMapper.toEntity(returnedNextCustomerDTO);
        assertNextCustomerUpdatableFieldsEquals(returnedNextCustomer, getPersistedNextCustomer(returnedNextCustomer));

        insertedNextCustomer = returnedNextCustomer;
    }

    @Test
    @Transactional
    void createNextCustomerWithExistingId() throws Exception {
        // Create the NextCustomer with an existing ID
        nextCustomer.setId(1L);
        NextCustomerDTO nextCustomerDTO = nextCustomerMapper.toDto(nextCustomer);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextCustomer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCustomer.setFirstName(null);

        // Create the NextCustomer, which fails.
        NextCustomerDTO nextCustomerDTO = nextCustomerMapper.toDto(nextCustomer);

        restNextCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCustomer.setLastName(null);

        // Create the NextCustomer, which fails.
        NextCustomerDTO nextCustomerDTO = nextCustomerMapper.toDto(nextCustomer);

        restNextCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCustomer.setEmail(null);

        // Create the NextCustomer, which fails.
        NextCustomerDTO nextCustomerDTO = nextCustomerMapper.toDto(nextCustomer);

        restNextCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextCustomers() throws Exception {
        // Initialize the database
        insertedNextCustomer = nextCustomerRepository.saveAndFlush(nextCustomer);

        // Get all the nextCustomerList
        restNextCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCustomer.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @Test
    @Transactional
    void getNextCustomer() throws Exception {
        // Initialize the database
        insertedNextCustomer = nextCustomerRepository.saveAndFlush(nextCustomer);

        // Get the nextCustomer
        restNextCustomerMockMvc
            .perform(get(ENTITY_API_URL_ID, nextCustomer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextCustomer.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getNextCustomersByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextCustomer = nextCustomerRepository.saveAndFlush(nextCustomer);

        Long id = nextCustomer.getId();

        defaultNextCustomerFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextCustomerFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextCustomerFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextCustomersByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomer = nextCustomerRepository.saveAndFlush(nextCustomer);

        // Get all the nextCustomerList where firstName equals to
        defaultNextCustomerFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomersByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomer = nextCustomerRepository.saveAndFlush(nextCustomer);

        // Get all the nextCustomerList where firstName in
        defaultNextCustomerFiltering("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME, "firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomersByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomer = nextCustomerRepository.saveAndFlush(nextCustomer);

        // Get all the nextCustomerList where firstName is not null
        defaultNextCustomerFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomersByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomer = nextCustomerRepository.saveAndFlush(nextCustomer);

        // Get all the nextCustomerList where firstName contains
        defaultNextCustomerFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomersByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomer = nextCustomerRepository.saveAndFlush(nextCustomer);

        // Get all the nextCustomerList where firstName does not contain
        defaultNextCustomerFiltering("firstName.doesNotContain=" + UPDATED_FIRST_NAME, "firstName.doesNotContain=" + DEFAULT_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomersByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomer = nextCustomerRepository.saveAndFlush(nextCustomer);

        // Get all the nextCustomerList where lastName equals to
        defaultNextCustomerFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomersByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomer = nextCustomerRepository.saveAndFlush(nextCustomer);

        // Get all the nextCustomerList where lastName in
        defaultNextCustomerFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomersByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomer = nextCustomerRepository.saveAndFlush(nextCustomer);

        // Get all the nextCustomerList where lastName is not null
        defaultNextCustomerFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomersByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomer = nextCustomerRepository.saveAndFlush(nextCustomer);

        // Get all the nextCustomerList where lastName contains
        defaultNextCustomerFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomersByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomer = nextCustomerRepository.saveAndFlush(nextCustomer);

        // Get all the nextCustomerList where lastName does not contain
        defaultNextCustomerFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomer = nextCustomerRepository.saveAndFlush(nextCustomer);

        // Get all the nextCustomerList where email equals to
        defaultNextCustomerFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomer = nextCustomerRepository.saveAndFlush(nextCustomer);

        // Get all the nextCustomerList where email in
        defaultNextCustomerFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomer = nextCustomerRepository.saveAndFlush(nextCustomer);

        // Get all the nextCustomerList where email is not null
        defaultNextCustomerFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomersByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomer = nextCustomerRepository.saveAndFlush(nextCustomer);

        // Get all the nextCustomerList where email contains
        defaultNextCustomerFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomer = nextCustomerRepository.saveAndFlush(nextCustomer);

        // Get all the nextCustomerList where email does not contain
        defaultNextCustomerFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomersByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomer = nextCustomerRepository.saveAndFlush(nextCustomer);

        // Get all the nextCustomerList where phoneNumber equals to
        defaultNextCustomerFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextCustomersByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomer = nextCustomerRepository.saveAndFlush(nextCustomer);

        // Get all the nextCustomerList where phoneNumber in
        defaultNextCustomerFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextCustomersByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomer = nextCustomerRepository.saveAndFlush(nextCustomer);

        // Get all the nextCustomerList where phoneNumber is not null
        defaultNextCustomerFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomersByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomer = nextCustomerRepository.saveAndFlush(nextCustomer);

        // Get all the nextCustomerList where phoneNumber contains
        defaultNextCustomerFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextCustomersByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomer = nextCustomerRepository.saveAndFlush(nextCustomer);

        // Get all the nextCustomerList where phoneNumber does not contain
        defaultNextCustomerFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextCustomersByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextCustomerRepository.saveAndFlush(nextCustomer);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextCustomer.setTenant(tenant);
        nextCustomerRepository.saveAndFlush(nextCustomer);
        Long tenantId = tenant.getId();
        // Get all the nextCustomerList where tenant equals to tenantId
        defaultNextCustomerShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextCustomerList where tenant equals to (tenantId + 1)
        defaultNextCustomerShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextCustomerFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextCustomerShouldBeFound(shouldBeFound);
        defaultNextCustomerShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextCustomerShouldBeFound(String filter) throws Exception {
        restNextCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCustomer.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restNextCustomerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextCustomerShouldNotBeFound(String filter) throws Exception {
        restNextCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextCustomerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextCustomer() throws Exception {
        // Get the nextCustomer
        restNextCustomerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextCustomer() throws Exception {
        // Initialize the database
        insertedNextCustomer = nextCustomerRepository.saveAndFlush(nextCustomer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCustomer
        NextCustomer updatedNextCustomer = nextCustomerRepository.findById(nextCustomer.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextCustomer are not directly saved in db
        em.detach(updatedNextCustomer);
        updatedNextCustomer
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        NextCustomerDTO nextCustomerDTO = nextCustomerMapper.toDto(updatedNextCustomer);

        restNextCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextCustomerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCustomerDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextCustomer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextCustomerToMatchAllProperties(updatedNextCustomer);
    }

    @Test
    @Transactional
    void putNonExistingNextCustomer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomer.setId(longCount.incrementAndGet());

        // Create the NextCustomer
        NextCustomerDTO nextCustomerDTO = nextCustomerMapper.toDto(nextCustomer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextCustomerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCustomerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextCustomer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomer.setId(longCount.incrementAndGet());

        // Create the NextCustomer
        NextCustomerDTO nextCustomerDTO = nextCustomerMapper.toDto(nextCustomer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCustomerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextCustomer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomer.setId(longCount.incrementAndGet());

        // Create the NextCustomer
        NextCustomerDTO nextCustomerDTO = nextCustomerMapper.toDto(nextCustomer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCustomer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextCustomerWithPatch() throws Exception {
        // Initialize the database
        insertedNextCustomer = nextCustomerRepository.saveAndFlush(nextCustomer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCustomer using partial update
        NextCustomer partialUpdatedNextCustomer = new NextCustomer();
        partialUpdatedNextCustomer.setId(nextCustomer.getId());

        partialUpdatedNextCustomer.phoneNumber(UPDATED_PHONE_NUMBER);

        restNextCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCustomer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCustomer))
            )
            .andExpect(status().isOk());

        // Validate the NextCustomer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCustomerUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextCustomer, nextCustomer),
            getPersistedNextCustomer(nextCustomer)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextCustomerWithPatch() throws Exception {
        // Initialize the database
        insertedNextCustomer = nextCustomerRepository.saveAndFlush(nextCustomer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCustomer using partial update
        NextCustomer partialUpdatedNextCustomer = new NextCustomer();
        partialUpdatedNextCustomer.setId(nextCustomer.getId());

        partialUpdatedNextCustomer
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restNextCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCustomer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCustomer))
            )
            .andExpect(status().isOk());

        // Validate the NextCustomer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCustomerUpdatableFieldsEquals(partialUpdatedNextCustomer, getPersistedNextCustomer(partialUpdatedNextCustomer));
    }

    @Test
    @Transactional
    void patchNonExistingNextCustomer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomer.setId(longCount.incrementAndGet());

        // Create the NextCustomer
        NextCustomerDTO nextCustomerDTO = nextCustomerMapper.toDto(nextCustomer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextCustomerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCustomerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextCustomer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomer.setId(longCount.incrementAndGet());

        // Create the NextCustomer
        NextCustomerDTO nextCustomerDTO = nextCustomerMapper.toDto(nextCustomer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCustomerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextCustomer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomer.setId(longCount.incrementAndGet());

        // Create the NextCustomer
        NextCustomerDTO nextCustomerDTO = nextCustomerMapper.toDto(nextCustomer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextCustomerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCustomer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextCustomer() throws Exception {
        // Initialize the database
        insertedNextCustomer = nextCustomerRepository.saveAndFlush(nextCustomer);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextCustomer
        restNextCustomerMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextCustomer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextCustomerRepository.count();
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

    protected NextCustomer getPersistedNextCustomer(NextCustomer nextCustomer) {
        return nextCustomerRepository.findById(nextCustomer.getId()).orElseThrow();
    }

    protected void assertPersistedNextCustomerToMatchAllProperties(NextCustomer expectedNextCustomer) {
        assertNextCustomerAllPropertiesEquals(expectedNextCustomer, getPersistedNextCustomer(expectedNextCustomer));
    }

    protected void assertPersistedNextCustomerToMatchUpdatableProperties(NextCustomer expectedNextCustomer) {
        assertNextCustomerAllUpdatablePropertiesEquals(expectedNextCustomer, getPersistedNextCustomer(expectedNextCustomer));
    }
}
