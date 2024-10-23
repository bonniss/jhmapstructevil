package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextEmployeeGammaAsserts.*;
import static xyz.jhmapstruct.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import xyz.jhmapstruct.domain.NextEmployeeGamma;
import xyz.jhmapstruct.repository.NextEmployeeGammaRepository;
import xyz.jhmapstruct.service.dto.NextEmployeeGammaDTO;
import xyz.jhmapstruct.service.mapper.NextEmployeeGammaMapper;

/**
 * Integration tests for the {@link NextEmployeeGammaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextEmployeeGammaResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Instant DEFAULT_HIRE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_HIRE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_POSITION = "AAAAAAAAAA";
    private static final String UPDATED_POSITION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/next-employee-gammas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextEmployeeGammaRepository nextEmployeeGammaRepository;

    @Autowired
    private NextEmployeeGammaMapper nextEmployeeGammaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextEmployeeGammaMockMvc;

    private NextEmployeeGamma nextEmployeeGamma;

    private NextEmployeeGamma insertedNextEmployeeGamma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextEmployeeGamma createEntity() {
        return new NextEmployeeGamma()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .hireDate(DEFAULT_HIRE_DATE)
            .position(DEFAULT_POSITION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextEmployeeGamma createUpdatedEntity() {
        return new NextEmployeeGamma()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);
    }

    @BeforeEach
    public void initTest() {
        nextEmployeeGamma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextEmployeeGamma != null) {
            nextEmployeeGammaRepository.delete(insertedNextEmployeeGamma);
            insertedNextEmployeeGamma = null;
        }
    }

    @Test
    @Transactional
    void createNextEmployeeGamma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextEmployeeGamma
        NextEmployeeGammaDTO nextEmployeeGammaDTO = nextEmployeeGammaMapper.toDto(nextEmployeeGamma);
        var returnedNextEmployeeGammaDTO = om.readValue(
            restNextEmployeeGammaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeGammaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextEmployeeGammaDTO.class
        );

        // Validate the NextEmployeeGamma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextEmployeeGamma = nextEmployeeGammaMapper.toEntity(returnedNextEmployeeGammaDTO);
        assertNextEmployeeGammaUpdatableFieldsEquals(returnedNextEmployeeGamma, getPersistedNextEmployeeGamma(returnedNextEmployeeGamma));

        insertedNextEmployeeGamma = returnedNextEmployeeGamma;
    }

    @Test
    @Transactional
    void createNextEmployeeGammaWithExistingId() throws Exception {
        // Create the NextEmployeeGamma with an existing ID
        nextEmployeeGamma.setId(1L);
        NextEmployeeGammaDTO nextEmployeeGammaDTO = nextEmployeeGammaMapper.toDto(nextEmployeeGamma);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextEmployeeGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeGammaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextEmployeeGamma.setFirstName(null);

        // Create the NextEmployeeGamma, which fails.
        NextEmployeeGammaDTO nextEmployeeGammaDTO = nextEmployeeGammaMapper.toDto(nextEmployeeGamma);

        restNextEmployeeGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeGammaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextEmployeeGamma.setLastName(null);

        // Create the NextEmployeeGamma, which fails.
        NextEmployeeGammaDTO nextEmployeeGammaDTO = nextEmployeeGammaMapper.toDto(nextEmployeeGamma);

        restNextEmployeeGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeGammaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextEmployeeGamma.setEmail(null);

        // Create the NextEmployeeGamma, which fails.
        NextEmployeeGammaDTO nextEmployeeGammaDTO = nextEmployeeGammaMapper.toDto(nextEmployeeGamma);

        restNextEmployeeGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeGammaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextEmployeeGammas() throws Exception {
        // Initialize the database
        insertedNextEmployeeGamma = nextEmployeeGammaRepository.saveAndFlush(nextEmployeeGamma);

        // Get all the nextEmployeeGammaList
        restNextEmployeeGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextEmployeeGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));
    }

    @Test
    @Transactional
    void getNextEmployeeGamma() throws Exception {
        // Initialize the database
        insertedNextEmployeeGamma = nextEmployeeGammaRepository.saveAndFlush(nextEmployeeGamma);

        // Get the nextEmployeeGamma
        restNextEmployeeGammaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextEmployeeGamma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextEmployeeGamma.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.hireDate").value(DEFAULT_HIRE_DATE.toString()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION));
    }

    @Test
    @Transactional
    void getNextEmployeeGammasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextEmployeeGamma = nextEmployeeGammaRepository.saveAndFlush(nextEmployeeGamma);

        Long id = nextEmployeeGamma.getId();

        defaultNextEmployeeGammaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextEmployeeGammaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextEmployeeGammaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextEmployeeGammasByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeGamma = nextEmployeeGammaRepository.saveAndFlush(nextEmployeeGamma);

        // Get all the nextEmployeeGammaList where firstName equals to
        defaultNextEmployeeGammaFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeGammasByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeGamma = nextEmployeeGammaRepository.saveAndFlush(nextEmployeeGamma);

        // Get all the nextEmployeeGammaList where firstName in
        defaultNextEmployeeGammaFiltering(
            "firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME,
            "firstName.in=" + UPDATED_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllNextEmployeeGammasByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeGamma = nextEmployeeGammaRepository.saveAndFlush(nextEmployeeGamma);

        // Get all the nextEmployeeGammaList where firstName is not null
        defaultNextEmployeeGammaFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeGammasByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeGamma = nextEmployeeGammaRepository.saveAndFlush(nextEmployeeGamma);

        // Get all the nextEmployeeGammaList where firstName contains
        defaultNextEmployeeGammaFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeGammasByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeGamma = nextEmployeeGammaRepository.saveAndFlush(nextEmployeeGamma);

        // Get all the nextEmployeeGammaList where firstName does not contain
        defaultNextEmployeeGammaFiltering(
            "firstName.doesNotContain=" + UPDATED_FIRST_NAME,
            "firstName.doesNotContain=" + DEFAULT_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllNextEmployeeGammasByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeGamma = nextEmployeeGammaRepository.saveAndFlush(nextEmployeeGamma);

        // Get all the nextEmployeeGammaList where lastName equals to
        defaultNextEmployeeGammaFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeGammasByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeGamma = nextEmployeeGammaRepository.saveAndFlush(nextEmployeeGamma);

        // Get all the nextEmployeeGammaList where lastName in
        defaultNextEmployeeGammaFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeGammasByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeGamma = nextEmployeeGammaRepository.saveAndFlush(nextEmployeeGamma);

        // Get all the nextEmployeeGammaList where lastName is not null
        defaultNextEmployeeGammaFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeGammasByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeGamma = nextEmployeeGammaRepository.saveAndFlush(nextEmployeeGamma);

        // Get all the nextEmployeeGammaList where lastName contains
        defaultNextEmployeeGammaFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeGammasByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeGamma = nextEmployeeGammaRepository.saveAndFlush(nextEmployeeGamma);

        // Get all the nextEmployeeGammaList where lastName does not contain
        defaultNextEmployeeGammaFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeGammasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeGamma = nextEmployeeGammaRepository.saveAndFlush(nextEmployeeGamma);

        // Get all the nextEmployeeGammaList where email equals to
        defaultNextEmployeeGammaFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeeGammasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeGamma = nextEmployeeGammaRepository.saveAndFlush(nextEmployeeGamma);

        // Get all the nextEmployeeGammaList where email in
        defaultNextEmployeeGammaFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeeGammasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeGamma = nextEmployeeGammaRepository.saveAndFlush(nextEmployeeGamma);

        // Get all the nextEmployeeGammaList where email is not null
        defaultNextEmployeeGammaFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeGammasByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeGamma = nextEmployeeGammaRepository.saveAndFlush(nextEmployeeGamma);

        // Get all the nextEmployeeGammaList where email contains
        defaultNextEmployeeGammaFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeeGammasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeGamma = nextEmployeeGammaRepository.saveAndFlush(nextEmployeeGamma);

        // Get all the nextEmployeeGammaList where email does not contain
        defaultNextEmployeeGammaFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeeGammasByHireDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeGamma = nextEmployeeGammaRepository.saveAndFlush(nextEmployeeGamma);

        // Get all the nextEmployeeGammaList where hireDate equals to
        defaultNextEmployeeGammaFiltering("hireDate.equals=" + DEFAULT_HIRE_DATE, "hireDate.equals=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllNextEmployeeGammasByHireDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeGamma = nextEmployeeGammaRepository.saveAndFlush(nextEmployeeGamma);

        // Get all the nextEmployeeGammaList where hireDate in
        defaultNextEmployeeGammaFiltering("hireDate.in=" + DEFAULT_HIRE_DATE + "," + UPDATED_HIRE_DATE, "hireDate.in=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllNextEmployeeGammasByHireDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeGamma = nextEmployeeGammaRepository.saveAndFlush(nextEmployeeGamma);

        // Get all the nextEmployeeGammaList where hireDate is not null
        defaultNextEmployeeGammaFiltering("hireDate.specified=true", "hireDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeGammasByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeGamma = nextEmployeeGammaRepository.saveAndFlush(nextEmployeeGamma);

        // Get all the nextEmployeeGammaList where position equals to
        defaultNextEmployeeGammaFiltering("position.equals=" + DEFAULT_POSITION, "position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeeGammasByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeGamma = nextEmployeeGammaRepository.saveAndFlush(nextEmployeeGamma);

        // Get all the nextEmployeeGammaList where position in
        defaultNextEmployeeGammaFiltering("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION, "position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeeGammasByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeGamma = nextEmployeeGammaRepository.saveAndFlush(nextEmployeeGamma);

        // Get all the nextEmployeeGammaList where position is not null
        defaultNextEmployeeGammaFiltering("position.specified=true", "position.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeGammasByPositionContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeGamma = nextEmployeeGammaRepository.saveAndFlush(nextEmployeeGamma);

        // Get all the nextEmployeeGammaList where position contains
        defaultNextEmployeeGammaFiltering("position.contains=" + DEFAULT_POSITION, "position.contains=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeeGammasByPositionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeGamma = nextEmployeeGammaRepository.saveAndFlush(nextEmployeeGamma);

        // Get all the nextEmployeeGammaList where position does not contain
        defaultNextEmployeeGammaFiltering("position.doesNotContain=" + UPDATED_POSITION, "position.doesNotContain=" + DEFAULT_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeeGammasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextEmployeeGammaRepository.saveAndFlush(nextEmployeeGamma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextEmployeeGamma.setTenant(tenant);
        nextEmployeeGammaRepository.saveAndFlush(nextEmployeeGamma);
        Long tenantId = tenant.getId();
        // Get all the nextEmployeeGammaList where tenant equals to tenantId
        defaultNextEmployeeGammaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextEmployeeGammaList where tenant equals to (tenantId + 1)
        defaultNextEmployeeGammaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextEmployeeGammaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextEmployeeGammaShouldBeFound(shouldBeFound);
        defaultNextEmployeeGammaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextEmployeeGammaShouldBeFound(String filter) throws Exception {
        restNextEmployeeGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextEmployeeGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));

        // Check, that the count call also returns 1
        restNextEmployeeGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextEmployeeGammaShouldNotBeFound(String filter) throws Exception {
        restNextEmployeeGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextEmployeeGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextEmployeeGamma() throws Exception {
        // Get the nextEmployeeGamma
        restNextEmployeeGammaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextEmployeeGamma() throws Exception {
        // Initialize the database
        insertedNextEmployeeGamma = nextEmployeeGammaRepository.saveAndFlush(nextEmployeeGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextEmployeeGamma
        NextEmployeeGamma updatedNextEmployeeGamma = nextEmployeeGammaRepository.findById(nextEmployeeGamma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextEmployeeGamma are not directly saved in db
        em.detach(updatedNextEmployeeGamma);
        updatedNextEmployeeGamma
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);
        NextEmployeeGammaDTO nextEmployeeGammaDTO = nextEmployeeGammaMapper.toDto(updatedNextEmployeeGamma);

        restNextEmployeeGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextEmployeeGammaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextEmployeeGammaDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextEmployeeGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextEmployeeGammaToMatchAllProperties(updatedNextEmployeeGamma);
    }

    @Test
    @Transactional
    void putNonExistingNextEmployeeGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeGamma.setId(longCount.incrementAndGet());

        // Create the NextEmployeeGamma
        NextEmployeeGammaDTO nextEmployeeGammaDTO = nextEmployeeGammaMapper.toDto(nextEmployeeGamma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextEmployeeGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextEmployeeGammaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextEmployeeGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextEmployeeGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeGamma.setId(longCount.incrementAndGet());

        // Create the NextEmployeeGamma
        NextEmployeeGammaDTO nextEmployeeGammaDTO = nextEmployeeGammaMapper.toDto(nextEmployeeGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextEmployeeGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextEmployeeGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeGamma.setId(longCount.incrementAndGet());

        // Create the NextEmployeeGamma
        NextEmployeeGammaDTO nextEmployeeGammaDTO = nextEmployeeGammaMapper.toDto(nextEmployeeGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeGammaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeGammaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextEmployeeGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextEmployeeGammaWithPatch() throws Exception {
        // Initialize the database
        insertedNextEmployeeGamma = nextEmployeeGammaRepository.saveAndFlush(nextEmployeeGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextEmployeeGamma using partial update
        NextEmployeeGamma partialUpdatedNextEmployeeGamma = new NextEmployeeGamma();
        partialUpdatedNextEmployeeGamma.setId(nextEmployeeGamma.getId());

        partialUpdatedNextEmployeeGamma
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .position(UPDATED_POSITION);

        restNextEmployeeGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextEmployeeGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextEmployeeGamma))
            )
            .andExpect(status().isOk());

        // Validate the NextEmployeeGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextEmployeeGammaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextEmployeeGamma, nextEmployeeGamma),
            getPersistedNextEmployeeGamma(nextEmployeeGamma)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextEmployeeGammaWithPatch() throws Exception {
        // Initialize the database
        insertedNextEmployeeGamma = nextEmployeeGammaRepository.saveAndFlush(nextEmployeeGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextEmployeeGamma using partial update
        NextEmployeeGamma partialUpdatedNextEmployeeGamma = new NextEmployeeGamma();
        partialUpdatedNextEmployeeGamma.setId(nextEmployeeGamma.getId());

        partialUpdatedNextEmployeeGamma
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);

        restNextEmployeeGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextEmployeeGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextEmployeeGamma))
            )
            .andExpect(status().isOk());

        // Validate the NextEmployeeGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextEmployeeGammaUpdatableFieldsEquals(
            partialUpdatedNextEmployeeGamma,
            getPersistedNextEmployeeGamma(partialUpdatedNextEmployeeGamma)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextEmployeeGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeGamma.setId(longCount.incrementAndGet());

        // Create the NextEmployeeGamma
        NextEmployeeGammaDTO nextEmployeeGammaDTO = nextEmployeeGammaMapper.toDto(nextEmployeeGamma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextEmployeeGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextEmployeeGammaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextEmployeeGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextEmployeeGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeGamma.setId(longCount.incrementAndGet());

        // Create the NextEmployeeGamma
        NextEmployeeGammaDTO nextEmployeeGammaDTO = nextEmployeeGammaMapper.toDto(nextEmployeeGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextEmployeeGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextEmployeeGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeGamma.setId(longCount.incrementAndGet());

        // Create the NextEmployeeGamma
        NextEmployeeGammaDTO nextEmployeeGammaDTO = nextEmployeeGammaMapper.toDto(nextEmployeeGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeGammaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextEmployeeGammaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextEmployeeGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextEmployeeGamma() throws Exception {
        // Initialize the database
        insertedNextEmployeeGamma = nextEmployeeGammaRepository.saveAndFlush(nextEmployeeGamma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextEmployeeGamma
        restNextEmployeeGammaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextEmployeeGamma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextEmployeeGammaRepository.count();
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

    protected NextEmployeeGamma getPersistedNextEmployeeGamma(NextEmployeeGamma nextEmployeeGamma) {
        return nextEmployeeGammaRepository.findById(nextEmployeeGamma.getId()).orElseThrow();
    }

    protected void assertPersistedNextEmployeeGammaToMatchAllProperties(NextEmployeeGamma expectedNextEmployeeGamma) {
        assertNextEmployeeGammaAllPropertiesEquals(expectedNextEmployeeGamma, getPersistedNextEmployeeGamma(expectedNextEmployeeGamma));
    }

    protected void assertPersistedNextEmployeeGammaToMatchUpdatableProperties(NextEmployeeGamma expectedNextEmployeeGamma) {
        assertNextEmployeeGammaAllUpdatablePropertiesEquals(
            expectedNextEmployeeGamma,
            getPersistedNextEmployeeGamma(expectedNextEmployeeGamma)
        );
    }
}
