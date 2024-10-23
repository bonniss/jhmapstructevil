package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextEmployeeSigmaAsserts.*;
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
import xyz.jhmapstruct.domain.NextEmployeeSigma;
import xyz.jhmapstruct.repository.NextEmployeeSigmaRepository;
import xyz.jhmapstruct.service.dto.NextEmployeeSigmaDTO;
import xyz.jhmapstruct.service.mapper.NextEmployeeSigmaMapper;

/**
 * Integration tests for the {@link NextEmployeeSigmaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextEmployeeSigmaResourceIT {

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

    private static final String ENTITY_API_URL = "/api/next-employee-sigmas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextEmployeeSigmaRepository nextEmployeeSigmaRepository;

    @Autowired
    private NextEmployeeSigmaMapper nextEmployeeSigmaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextEmployeeSigmaMockMvc;

    private NextEmployeeSigma nextEmployeeSigma;

    private NextEmployeeSigma insertedNextEmployeeSigma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextEmployeeSigma createEntity() {
        return new NextEmployeeSigma()
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
    public static NextEmployeeSigma createUpdatedEntity() {
        return new NextEmployeeSigma()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);
    }

    @BeforeEach
    public void initTest() {
        nextEmployeeSigma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextEmployeeSigma != null) {
            nextEmployeeSigmaRepository.delete(insertedNextEmployeeSigma);
            insertedNextEmployeeSigma = null;
        }
    }

    @Test
    @Transactional
    void createNextEmployeeSigma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextEmployeeSigma
        NextEmployeeSigmaDTO nextEmployeeSigmaDTO = nextEmployeeSigmaMapper.toDto(nextEmployeeSigma);
        var returnedNextEmployeeSigmaDTO = om.readValue(
            restNextEmployeeSigmaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeSigmaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextEmployeeSigmaDTO.class
        );

        // Validate the NextEmployeeSigma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextEmployeeSigma = nextEmployeeSigmaMapper.toEntity(returnedNextEmployeeSigmaDTO);
        assertNextEmployeeSigmaUpdatableFieldsEquals(returnedNextEmployeeSigma, getPersistedNextEmployeeSigma(returnedNextEmployeeSigma));

        insertedNextEmployeeSigma = returnedNextEmployeeSigma;
    }

    @Test
    @Transactional
    void createNextEmployeeSigmaWithExistingId() throws Exception {
        // Create the NextEmployeeSigma with an existing ID
        nextEmployeeSigma.setId(1L);
        NextEmployeeSigmaDTO nextEmployeeSigmaDTO = nextEmployeeSigmaMapper.toDto(nextEmployeeSigma);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextEmployeeSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeSigmaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextEmployeeSigma.setFirstName(null);

        // Create the NextEmployeeSigma, which fails.
        NextEmployeeSigmaDTO nextEmployeeSigmaDTO = nextEmployeeSigmaMapper.toDto(nextEmployeeSigma);

        restNextEmployeeSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeSigmaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextEmployeeSigma.setLastName(null);

        // Create the NextEmployeeSigma, which fails.
        NextEmployeeSigmaDTO nextEmployeeSigmaDTO = nextEmployeeSigmaMapper.toDto(nextEmployeeSigma);

        restNextEmployeeSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeSigmaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextEmployeeSigma.setEmail(null);

        // Create the NextEmployeeSigma, which fails.
        NextEmployeeSigmaDTO nextEmployeeSigmaDTO = nextEmployeeSigmaMapper.toDto(nextEmployeeSigma);

        restNextEmployeeSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeSigmaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextEmployeeSigmas() throws Exception {
        // Initialize the database
        insertedNextEmployeeSigma = nextEmployeeSigmaRepository.saveAndFlush(nextEmployeeSigma);

        // Get all the nextEmployeeSigmaList
        restNextEmployeeSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextEmployeeSigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));
    }

    @Test
    @Transactional
    void getNextEmployeeSigma() throws Exception {
        // Initialize the database
        insertedNextEmployeeSigma = nextEmployeeSigmaRepository.saveAndFlush(nextEmployeeSigma);

        // Get the nextEmployeeSigma
        restNextEmployeeSigmaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextEmployeeSigma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextEmployeeSigma.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.hireDate").value(DEFAULT_HIRE_DATE.toString()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION));
    }

    @Test
    @Transactional
    void getNextEmployeeSigmasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextEmployeeSigma = nextEmployeeSigmaRepository.saveAndFlush(nextEmployeeSigma);

        Long id = nextEmployeeSigma.getId();

        defaultNextEmployeeSigmaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextEmployeeSigmaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextEmployeeSigmaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextEmployeeSigmasByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeSigma = nextEmployeeSigmaRepository.saveAndFlush(nextEmployeeSigma);

        // Get all the nextEmployeeSigmaList where firstName equals to
        defaultNextEmployeeSigmaFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeSigmasByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeSigma = nextEmployeeSigmaRepository.saveAndFlush(nextEmployeeSigma);

        // Get all the nextEmployeeSigmaList where firstName in
        defaultNextEmployeeSigmaFiltering(
            "firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME,
            "firstName.in=" + UPDATED_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllNextEmployeeSigmasByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeSigma = nextEmployeeSigmaRepository.saveAndFlush(nextEmployeeSigma);

        // Get all the nextEmployeeSigmaList where firstName is not null
        defaultNextEmployeeSigmaFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeSigmasByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeSigma = nextEmployeeSigmaRepository.saveAndFlush(nextEmployeeSigma);

        // Get all the nextEmployeeSigmaList where firstName contains
        defaultNextEmployeeSigmaFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeSigmasByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeSigma = nextEmployeeSigmaRepository.saveAndFlush(nextEmployeeSigma);

        // Get all the nextEmployeeSigmaList where firstName does not contain
        defaultNextEmployeeSigmaFiltering(
            "firstName.doesNotContain=" + UPDATED_FIRST_NAME,
            "firstName.doesNotContain=" + DEFAULT_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllNextEmployeeSigmasByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeSigma = nextEmployeeSigmaRepository.saveAndFlush(nextEmployeeSigma);

        // Get all the nextEmployeeSigmaList where lastName equals to
        defaultNextEmployeeSigmaFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeSigmasByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeSigma = nextEmployeeSigmaRepository.saveAndFlush(nextEmployeeSigma);

        // Get all the nextEmployeeSigmaList where lastName in
        defaultNextEmployeeSigmaFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeSigmasByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeSigma = nextEmployeeSigmaRepository.saveAndFlush(nextEmployeeSigma);

        // Get all the nextEmployeeSigmaList where lastName is not null
        defaultNextEmployeeSigmaFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeSigmasByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeSigma = nextEmployeeSigmaRepository.saveAndFlush(nextEmployeeSigma);

        // Get all the nextEmployeeSigmaList where lastName contains
        defaultNextEmployeeSigmaFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeSigmasByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeSigma = nextEmployeeSigmaRepository.saveAndFlush(nextEmployeeSigma);

        // Get all the nextEmployeeSigmaList where lastName does not contain
        defaultNextEmployeeSigmaFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeSigmasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeSigma = nextEmployeeSigmaRepository.saveAndFlush(nextEmployeeSigma);

        // Get all the nextEmployeeSigmaList where email equals to
        defaultNextEmployeeSigmaFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeeSigmasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeSigma = nextEmployeeSigmaRepository.saveAndFlush(nextEmployeeSigma);

        // Get all the nextEmployeeSigmaList where email in
        defaultNextEmployeeSigmaFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeeSigmasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeSigma = nextEmployeeSigmaRepository.saveAndFlush(nextEmployeeSigma);

        // Get all the nextEmployeeSigmaList where email is not null
        defaultNextEmployeeSigmaFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeSigmasByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeSigma = nextEmployeeSigmaRepository.saveAndFlush(nextEmployeeSigma);

        // Get all the nextEmployeeSigmaList where email contains
        defaultNextEmployeeSigmaFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeeSigmasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeSigma = nextEmployeeSigmaRepository.saveAndFlush(nextEmployeeSigma);

        // Get all the nextEmployeeSigmaList where email does not contain
        defaultNextEmployeeSigmaFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeeSigmasByHireDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeSigma = nextEmployeeSigmaRepository.saveAndFlush(nextEmployeeSigma);

        // Get all the nextEmployeeSigmaList where hireDate equals to
        defaultNextEmployeeSigmaFiltering("hireDate.equals=" + DEFAULT_HIRE_DATE, "hireDate.equals=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllNextEmployeeSigmasByHireDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeSigma = nextEmployeeSigmaRepository.saveAndFlush(nextEmployeeSigma);

        // Get all the nextEmployeeSigmaList where hireDate in
        defaultNextEmployeeSigmaFiltering("hireDate.in=" + DEFAULT_HIRE_DATE + "," + UPDATED_HIRE_DATE, "hireDate.in=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllNextEmployeeSigmasByHireDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeSigma = nextEmployeeSigmaRepository.saveAndFlush(nextEmployeeSigma);

        // Get all the nextEmployeeSigmaList where hireDate is not null
        defaultNextEmployeeSigmaFiltering("hireDate.specified=true", "hireDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeSigmasByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeSigma = nextEmployeeSigmaRepository.saveAndFlush(nextEmployeeSigma);

        // Get all the nextEmployeeSigmaList where position equals to
        defaultNextEmployeeSigmaFiltering("position.equals=" + DEFAULT_POSITION, "position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeeSigmasByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeSigma = nextEmployeeSigmaRepository.saveAndFlush(nextEmployeeSigma);

        // Get all the nextEmployeeSigmaList where position in
        defaultNextEmployeeSigmaFiltering("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION, "position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeeSigmasByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeSigma = nextEmployeeSigmaRepository.saveAndFlush(nextEmployeeSigma);

        // Get all the nextEmployeeSigmaList where position is not null
        defaultNextEmployeeSigmaFiltering("position.specified=true", "position.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeSigmasByPositionContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeSigma = nextEmployeeSigmaRepository.saveAndFlush(nextEmployeeSigma);

        // Get all the nextEmployeeSigmaList where position contains
        defaultNextEmployeeSigmaFiltering("position.contains=" + DEFAULT_POSITION, "position.contains=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeeSigmasByPositionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeSigma = nextEmployeeSigmaRepository.saveAndFlush(nextEmployeeSigma);

        // Get all the nextEmployeeSigmaList where position does not contain
        defaultNextEmployeeSigmaFiltering("position.doesNotContain=" + UPDATED_POSITION, "position.doesNotContain=" + DEFAULT_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeeSigmasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextEmployeeSigmaRepository.saveAndFlush(nextEmployeeSigma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextEmployeeSigma.setTenant(tenant);
        nextEmployeeSigmaRepository.saveAndFlush(nextEmployeeSigma);
        Long tenantId = tenant.getId();
        // Get all the nextEmployeeSigmaList where tenant equals to tenantId
        defaultNextEmployeeSigmaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextEmployeeSigmaList where tenant equals to (tenantId + 1)
        defaultNextEmployeeSigmaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextEmployeeSigmaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextEmployeeSigmaShouldBeFound(shouldBeFound);
        defaultNextEmployeeSigmaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextEmployeeSigmaShouldBeFound(String filter) throws Exception {
        restNextEmployeeSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextEmployeeSigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));

        // Check, that the count call also returns 1
        restNextEmployeeSigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextEmployeeSigmaShouldNotBeFound(String filter) throws Exception {
        restNextEmployeeSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextEmployeeSigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextEmployeeSigma() throws Exception {
        // Get the nextEmployeeSigma
        restNextEmployeeSigmaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextEmployeeSigma() throws Exception {
        // Initialize the database
        insertedNextEmployeeSigma = nextEmployeeSigmaRepository.saveAndFlush(nextEmployeeSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextEmployeeSigma
        NextEmployeeSigma updatedNextEmployeeSigma = nextEmployeeSigmaRepository.findById(nextEmployeeSigma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextEmployeeSigma are not directly saved in db
        em.detach(updatedNextEmployeeSigma);
        updatedNextEmployeeSigma
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);
        NextEmployeeSigmaDTO nextEmployeeSigmaDTO = nextEmployeeSigmaMapper.toDto(updatedNextEmployeeSigma);

        restNextEmployeeSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextEmployeeSigmaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextEmployeeSigmaDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextEmployeeSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextEmployeeSigmaToMatchAllProperties(updatedNextEmployeeSigma);
    }

    @Test
    @Transactional
    void putNonExistingNextEmployeeSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeSigma.setId(longCount.incrementAndGet());

        // Create the NextEmployeeSigma
        NextEmployeeSigmaDTO nextEmployeeSigmaDTO = nextEmployeeSigmaMapper.toDto(nextEmployeeSigma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextEmployeeSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextEmployeeSigmaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextEmployeeSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextEmployeeSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeSigma.setId(longCount.incrementAndGet());

        // Create the NextEmployeeSigma
        NextEmployeeSigmaDTO nextEmployeeSigmaDTO = nextEmployeeSigmaMapper.toDto(nextEmployeeSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextEmployeeSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextEmployeeSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeSigma.setId(longCount.incrementAndGet());

        // Create the NextEmployeeSigma
        NextEmployeeSigmaDTO nextEmployeeSigmaDTO = nextEmployeeSigmaMapper.toDto(nextEmployeeSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeSigmaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeSigmaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextEmployeeSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextEmployeeSigmaWithPatch() throws Exception {
        // Initialize the database
        insertedNextEmployeeSigma = nextEmployeeSigmaRepository.saveAndFlush(nextEmployeeSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextEmployeeSigma using partial update
        NextEmployeeSigma partialUpdatedNextEmployeeSigma = new NextEmployeeSigma();
        partialUpdatedNextEmployeeSigma.setId(nextEmployeeSigma.getId());

        partialUpdatedNextEmployeeSigma.email(UPDATED_EMAIL).hireDate(UPDATED_HIRE_DATE).position(UPDATED_POSITION);

        restNextEmployeeSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextEmployeeSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextEmployeeSigma))
            )
            .andExpect(status().isOk());

        // Validate the NextEmployeeSigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextEmployeeSigmaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextEmployeeSigma, nextEmployeeSigma),
            getPersistedNextEmployeeSigma(nextEmployeeSigma)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextEmployeeSigmaWithPatch() throws Exception {
        // Initialize the database
        insertedNextEmployeeSigma = nextEmployeeSigmaRepository.saveAndFlush(nextEmployeeSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextEmployeeSigma using partial update
        NextEmployeeSigma partialUpdatedNextEmployeeSigma = new NextEmployeeSigma();
        partialUpdatedNextEmployeeSigma.setId(nextEmployeeSigma.getId());

        partialUpdatedNextEmployeeSigma
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);

        restNextEmployeeSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextEmployeeSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextEmployeeSigma))
            )
            .andExpect(status().isOk());

        // Validate the NextEmployeeSigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextEmployeeSigmaUpdatableFieldsEquals(
            partialUpdatedNextEmployeeSigma,
            getPersistedNextEmployeeSigma(partialUpdatedNextEmployeeSigma)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextEmployeeSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeSigma.setId(longCount.incrementAndGet());

        // Create the NextEmployeeSigma
        NextEmployeeSigmaDTO nextEmployeeSigmaDTO = nextEmployeeSigmaMapper.toDto(nextEmployeeSigma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextEmployeeSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextEmployeeSigmaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextEmployeeSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextEmployeeSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeSigma.setId(longCount.incrementAndGet());

        // Create the NextEmployeeSigma
        NextEmployeeSigmaDTO nextEmployeeSigmaDTO = nextEmployeeSigmaMapper.toDto(nextEmployeeSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextEmployeeSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextEmployeeSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeSigma.setId(longCount.incrementAndGet());

        // Create the NextEmployeeSigma
        NextEmployeeSigmaDTO nextEmployeeSigmaDTO = nextEmployeeSigmaMapper.toDto(nextEmployeeSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeSigmaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextEmployeeSigmaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextEmployeeSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextEmployeeSigma() throws Exception {
        // Initialize the database
        insertedNextEmployeeSigma = nextEmployeeSigmaRepository.saveAndFlush(nextEmployeeSigma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextEmployeeSigma
        restNextEmployeeSigmaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextEmployeeSigma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextEmployeeSigmaRepository.count();
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

    protected NextEmployeeSigma getPersistedNextEmployeeSigma(NextEmployeeSigma nextEmployeeSigma) {
        return nextEmployeeSigmaRepository.findById(nextEmployeeSigma.getId()).orElseThrow();
    }

    protected void assertPersistedNextEmployeeSigmaToMatchAllProperties(NextEmployeeSigma expectedNextEmployeeSigma) {
        assertNextEmployeeSigmaAllPropertiesEquals(expectedNextEmployeeSigma, getPersistedNextEmployeeSigma(expectedNextEmployeeSigma));
    }

    protected void assertPersistedNextEmployeeSigmaToMatchUpdatableProperties(NextEmployeeSigma expectedNextEmployeeSigma) {
        assertNextEmployeeSigmaAllUpdatablePropertiesEquals(
            expectedNextEmployeeSigma,
            getPersistedNextEmployeeSigma(expectedNextEmployeeSigma)
        );
    }
}
