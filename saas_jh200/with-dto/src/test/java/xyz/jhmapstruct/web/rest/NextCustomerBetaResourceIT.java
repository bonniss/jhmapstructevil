package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextCustomerBetaAsserts.*;
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
import xyz.jhmapstruct.domain.NextCustomerBeta;
import xyz.jhmapstruct.repository.NextCustomerBetaRepository;
import xyz.jhmapstruct.service.dto.NextCustomerBetaDTO;
import xyz.jhmapstruct.service.mapper.NextCustomerBetaMapper;

/**
 * Integration tests for the {@link NextCustomerBetaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextCustomerBetaResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/next-customer-betas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextCustomerBetaRepository nextCustomerBetaRepository;

    @Autowired
    private NextCustomerBetaMapper nextCustomerBetaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextCustomerBetaMockMvc;

    private NextCustomerBeta nextCustomerBeta;

    private NextCustomerBeta insertedNextCustomerBeta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextCustomerBeta createEntity() {
        return new NextCustomerBeta()
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
    public static NextCustomerBeta createUpdatedEntity() {
        return new NextCustomerBeta()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        nextCustomerBeta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextCustomerBeta != null) {
            nextCustomerBetaRepository.delete(insertedNextCustomerBeta);
            insertedNextCustomerBeta = null;
        }
    }

    @Test
    @Transactional
    void createNextCustomerBeta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextCustomerBeta
        NextCustomerBetaDTO nextCustomerBetaDTO = nextCustomerBetaMapper.toDto(nextCustomerBeta);
        var returnedNextCustomerBetaDTO = om.readValue(
            restNextCustomerBetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerBetaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextCustomerBetaDTO.class
        );

        // Validate the NextCustomerBeta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextCustomerBeta = nextCustomerBetaMapper.toEntity(returnedNextCustomerBetaDTO);
        assertNextCustomerBetaUpdatableFieldsEquals(returnedNextCustomerBeta, getPersistedNextCustomerBeta(returnedNextCustomerBeta));

        insertedNextCustomerBeta = returnedNextCustomerBeta;
    }

    @Test
    @Transactional
    void createNextCustomerBetaWithExistingId() throws Exception {
        // Create the NextCustomerBeta with an existing ID
        nextCustomerBeta.setId(1L);
        NextCustomerBetaDTO nextCustomerBetaDTO = nextCustomerBetaMapper.toDto(nextCustomerBeta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextCustomerBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerBetaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCustomerBeta.setFirstName(null);

        // Create the NextCustomerBeta, which fails.
        NextCustomerBetaDTO nextCustomerBetaDTO = nextCustomerBetaMapper.toDto(nextCustomerBeta);

        restNextCustomerBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerBetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCustomerBeta.setLastName(null);

        // Create the NextCustomerBeta, which fails.
        NextCustomerBetaDTO nextCustomerBetaDTO = nextCustomerBetaMapper.toDto(nextCustomerBeta);

        restNextCustomerBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerBetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCustomerBeta.setEmail(null);

        // Create the NextCustomerBeta, which fails.
        NextCustomerBetaDTO nextCustomerBetaDTO = nextCustomerBetaMapper.toDto(nextCustomerBeta);

        restNextCustomerBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerBetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextCustomerBetas() throws Exception {
        // Initialize the database
        insertedNextCustomerBeta = nextCustomerBetaRepository.saveAndFlush(nextCustomerBeta);

        // Get all the nextCustomerBetaList
        restNextCustomerBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCustomerBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @Test
    @Transactional
    void getNextCustomerBeta() throws Exception {
        // Initialize the database
        insertedNextCustomerBeta = nextCustomerBetaRepository.saveAndFlush(nextCustomerBeta);

        // Get the nextCustomerBeta
        restNextCustomerBetaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextCustomerBeta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextCustomerBeta.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getNextCustomerBetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextCustomerBeta = nextCustomerBetaRepository.saveAndFlush(nextCustomerBeta);

        Long id = nextCustomerBeta.getId();

        defaultNextCustomerBetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextCustomerBetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextCustomerBetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextCustomerBetasByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerBeta = nextCustomerBetaRepository.saveAndFlush(nextCustomerBeta);

        // Get all the nextCustomerBetaList where firstName equals to
        defaultNextCustomerBetaFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerBetasByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomerBeta = nextCustomerBetaRepository.saveAndFlush(nextCustomerBeta);

        // Get all the nextCustomerBetaList where firstName in
        defaultNextCustomerBetaFiltering(
            "firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME,
            "firstName.in=" + UPDATED_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllNextCustomerBetasByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomerBeta = nextCustomerBetaRepository.saveAndFlush(nextCustomerBeta);

        // Get all the nextCustomerBetaList where firstName is not null
        defaultNextCustomerBetaFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomerBetasByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerBeta = nextCustomerBetaRepository.saveAndFlush(nextCustomerBeta);

        // Get all the nextCustomerBetaList where firstName contains
        defaultNextCustomerBetaFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerBetasByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerBeta = nextCustomerBetaRepository.saveAndFlush(nextCustomerBeta);

        // Get all the nextCustomerBetaList where firstName does not contain
        defaultNextCustomerBetaFiltering(
            "firstName.doesNotContain=" + UPDATED_FIRST_NAME,
            "firstName.doesNotContain=" + DEFAULT_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllNextCustomerBetasByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerBeta = nextCustomerBetaRepository.saveAndFlush(nextCustomerBeta);

        // Get all the nextCustomerBetaList where lastName equals to
        defaultNextCustomerBetaFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerBetasByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomerBeta = nextCustomerBetaRepository.saveAndFlush(nextCustomerBeta);

        // Get all the nextCustomerBetaList where lastName in
        defaultNextCustomerBetaFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerBetasByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomerBeta = nextCustomerBetaRepository.saveAndFlush(nextCustomerBeta);

        // Get all the nextCustomerBetaList where lastName is not null
        defaultNextCustomerBetaFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomerBetasByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerBeta = nextCustomerBetaRepository.saveAndFlush(nextCustomerBeta);

        // Get all the nextCustomerBetaList where lastName contains
        defaultNextCustomerBetaFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerBetasByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerBeta = nextCustomerBetaRepository.saveAndFlush(nextCustomerBeta);

        // Get all the nextCustomerBetaList where lastName does not contain
        defaultNextCustomerBetaFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerBetasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerBeta = nextCustomerBetaRepository.saveAndFlush(nextCustomerBeta);

        // Get all the nextCustomerBetaList where email equals to
        defaultNextCustomerBetaFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomerBetasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomerBeta = nextCustomerBetaRepository.saveAndFlush(nextCustomerBeta);

        // Get all the nextCustomerBetaList where email in
        defaultNextCustomerBetaFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomerBetasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomerBeta = nextCustomerBetaRepository.saveAndFlush(nextCustomerBeta);

        // Get all the nextCustomerBetaList where email is not null
        defaultNextCustomerBetaFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomerBetasByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerBeta = nextCustomerBetaRepository.saveAndFlush(nextCustomerBeta);

        // Get all the nextCustomerBetaList where email contains
        defaultNextCustomerBetaFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomerBetasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerBeta = nextCustomerBetaRepository.saveAndFlush(nextCustomerBeta);

        // Get all the nextCustomerBetaList where email does not contain
        defaultNextCustomerBetaFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomerBetasByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerBeta = nextCustomerBetaRepository.saveAndFlush(nextCustomerBeta);

        // Get all the nextCustomerBetaList where phoneNumber equals to
        defaultNextCustomerBetaFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextCustomerBetasByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomerBeta = nextCustomerBetaRepository.saveAndFlush(nextCustomerBeta);

        // Get all the nextCustomerBetaList where phoneNumber in
        defaultNextCustomerBetaFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextCustomerBetasByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomerBeta = nextCustomerBetaRepository.saveAndFlush(nextCustomerBeta);

        // Get all the nextCustomerBetaList where phoneNumber is not null
        defaultNextCustomerBetaFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomerBetasByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerBeta = nextCustomerBetaRepository.saveAndFlush(nextCustomerBeta);

        // Get all the nextCustomerBetaList where phoneNumber contains
        defaultNextCustomerBetaFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextCustomerBetasByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerBeta = nextCustomerBetaRepository.saveAndFlush(nextCustomerBeta);

        // Get all the nextCustomerBetaList where phoneNumber does not contain
        defaultNextCustomerBetaFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextCustomerBetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextCustomerBetaRepository.saveAndFlush(nextCustomerBeta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextCustomerBeta.setTenant(tenant);
        nextCustomerBetaRepository.saveAndFlush(nextCustomerBeta);
        Long tenantId = tenant.getId();
        // Get all the nextCustomerBetaList where tenant equals to tenantId
        defaultNextCustomerBetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextCustomerBetaList where tenant equals to (tenantId + 1)
        defaultNextCustomerBetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextCustomerBetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextCustomerBetaShouldBeFound(shouldBeFound);
        defaultNextCustomerBetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextCustomerBetaShouldBeFound(String filter) throws Exception {
        restNextCustomerBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCustomerBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restNextCustomerBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextCustomerBetaShouldNotBeFound(String filter) throws Exception {
        restNextCustomerBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextCustomerBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextCustomerBeta() throws Exception {
        // Get the nextCustomerBeta
        restNextCustomerBetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextCustomerBeta() throws Exception {
        // Initialize the database
        insertedNextCustomerBeta = nextCustomerBetaRepository.saveAndFlush(nextCustomerBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCustomerBeta
        NextCustomerBeta updatedNextCustomerBeta = nextCustomerBetaRepository.findById(nextCustomerBeta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextCustomerBeta are not directly saved in db
        em.detach(updatedNextCustomerBeta);
        updatedNextCustomerBeta
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        NextCustomerBetaDTO nextCustomerBetaDTO = nextCustomerBetaMapper.toDto(updatedNextCustomerBeta);

        restNextCustomerBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextCustomerBetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCustomerBetaDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextCustomerBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextCustomerBetaToMatchAllProperties(updatedNextCustomerBeta);
    }

    @Test
    @Transactional
    void putNonExistingNextCustomerBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerBeta.setId(longCount.incrementAndGet());

        // Create the NextCustomerBeta
        NextCustomerBetaDTO nextCustomerBetaDTO = nextCustomerBetaMapper.toDto(nextCustomerBeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCustomerBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextCustomerBetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCustomerBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextCustomerBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerBeta.setId(longCount.incrementAndGet());

        // Create the NextCustomerBeta
        NextCustomerBetaDTO nextCustomerBetaDTO = nextCustomerBetaMapper.toDto(nextCustomerBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCustomerBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextCustomerBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerBeta.setId(longCount.incrementAndGet());

        // Create the NextCustomerBeta
        NextCustomerBetaDTO nextCustomerBetaDTO = nextCustomerBetaMapper.toDto(nextCustomerBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerBetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerBetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCustomerBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextCustomerBetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextCustomerBeta = nextCustomerBetaRepository.saveAndFlush(nextCustomerBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCustomerBeta using partial update
        NextCustomerBeta partialUpdatedNextCustomerBeta = new NextCustomerBeta();
        partialUpdatedNextCustomerBeta.setId(nextCustomerBeta.getId());

        partialUpdatedNextCustomerBeta.firstName(UPDATED_FIRST_NAME).phoneNumber(UPDATED_PHONE_NUMBER);

        restNextCustomerBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCustomerBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCustomerBeta))
            )
            .andExpect(status().isOk());

        // Validate the NextCustomerBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCustomerBetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextCustomerBeta, nextCustomerBeta),
            getPersistedNextCustomerBeta(nextCustomerBeta)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextCustomerBetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextCustomerBeta = nextCustomerBetaRepository.saveAndFlush(nextCustomerBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCustomerBeta using partial update
        NextCustomerBeta partialUpdatedNextCustomerBeta = new NextCustomerBeta();
        partialUpdatedNextCustomerBeta.setId(nextCustomerBeta.getId());

        partialUpdatedNextCustomerBeta
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restNextCustomerBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCustomerBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCustomerBeta))
            )
            .andExpect(status().isOk());

        // Validate the NextCustomerBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCustomerBetaUpdatableFieldsEquals(
            partialUpdatedNextCustomerBeta,
            getPersistedNextCustomerBeta(partialUpdatedNextCustomerBeta)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextCustomerBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerBeta.setId(longCount.incrementAndGet());

        // Create the NextCustomerBeta
        NextCustomerBetaDTO nextCustomerBetaDTO = nextCustomerBetaMapper.toDto(nextCustomerBeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCustomerBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextCustomerBetaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCustomerBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextCustomerBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerBeta.setId(longCount.incrementAndGet());

        // Create the NextCustomerBeta
        NextCustomerBetaDTO nextCustomerBetaDTO = nextCustomerBetaMapper.toDto(nextCustomerBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCustomerBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextCustomerBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerBeta.setId(longCount.incrementAndGet());

        // Create the NextCustomerBeta
        NextCustomerBetaDTO nextCustomerBetaDTO = nextCustomerBetaMapper.toDto(nextCustomerBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerBetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextCustomerBetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCustomerBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextCustomerBeta() throws Exception {
        // Initialize the database
        insertedNextCustomerBeta = nextCustomerBetaRepository.saveAndFlush(nextCustomerBeta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextCustomerBeta
        restNextCustomerBetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextCustomerBeta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextCustomerBetaRepository.count();
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

    protected NextCustomerBeta getPersistedNextCustomerBeta(NextCustomerBeta nextCustomerBeta) {
        return nextCustomerBetaRepository.findById(nextCustomerBeta.getId()).orElseThrow();
    }

    protected void assertPersistedNextCustomerBetaToMatchAllProperties(NextCustomerBeta expectedNextCustomerBeta) {
        assertNextCustomerBetaAllPropertiesEquals(expectedNextCustomerBeta, getPersistedNextCustomerBeta(expectedNextCustomerBeta));
    }

    protected void assertPersistedNextCustomerBetaToMatchUpdatableProperties(NextCustomerBeta expectedNextCustomerBeta) {
        assertNextCustomerBetaAllUpdatablePropertiesEquals(
            expectedNextCustomerBeta,
            getPersistedNextCustomerBeta(expectedNextCustomerBeta)
        );
    }
}
