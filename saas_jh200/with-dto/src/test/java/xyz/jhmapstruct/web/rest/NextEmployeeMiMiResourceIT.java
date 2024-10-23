package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextEmployeeMiMiAsserts.*;
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
import xyz.jhmapstruct.domain.NextEmployeeMiMi;
import xyz.jhmapstruct.repository.NextEmployeeMiMiRepository;
import xyz.jhmapstruct.service.dto.NextEmployeeMiMiDTO;
import xyz.jhmapstruct.service.mapper.NextEmployeeMiMiMapper;

/**
 * Integration tests for the {@link NextEmployeeMiMiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextEmployeeMiMiResourceIT {

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

    private static final String ENTITY_API_URL = "/api/next-employee-mi-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextEmployeeMiMiRepository nextEmployeeMiMiRepository;

    @Autowired
    private NextEmployeeMiMiMapper nextEmployeeMiMiMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextEmployeeMiMiMockMvc;

    private NextEmployeeMiMi nextEmployeeMiMi;

    private NextEmployeeMiMi insertedNextEmployeeMiMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextEmployeeMiMi createEntity() {
        return new NextEmployeeMiMi()
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
    public static NextEmployeeMiMi createUpdatedEntity() {
        return new NextEmployeeMiMi()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);
    }

    @BeforeEach
    public void initTest() {
        nextEmployeeMiMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextEmployeeMiMi != null) {
            nextEmployeeMiMiRepository.delete(insertedNextEmployeeMiMi);
            insertedNextEmployeeMiMi = null;
        }
    }

    @Test
    @Transactional
    void createNextEmployeeMiMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextEmployeeMiMi
        NextEmployeeMiMiDTO nextEmployeeMiMiDTO = nextEmployeeMiMiMapper.toDto(nextEmployeeMiMi);
        var returnedNextEmployeeMiMiDTO = om.readValue(
            restNextEmployeeMiMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeMiMiDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextEmployeeMiMiDTO.class
        );

        // Validate the NextEmployeeMiMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextEmployeeMiMi = nextEmployeeMiMiMapper.toEntity(returnedNextEmployeeMiMiDTO);
        assertNextEmployeeMiMiUpdatableFieldsEquals(returnedNextEmployeeMiMi, getPersistedNextEmployeeMiMi(returnedNextEmployeeMiMi));

        insertedNextEmployeeMiMi = returnedNextEmployeeMiMi;
    }

    @Test
    @Transactional
    void createNextEmployeeMiMiWithExistingId() throws Exception {
        // Create the NextEmployeeMiMi with an existing ID
        nextEmployeeMiMi.setId(1L);
        NextEmployeeMiMiDTO nextEmployeeMiMiDTO = nextEmployeeMiMiMapper.toDto(nextEmployeeMiMi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextEmployeeMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeMiMiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextEmployeeMiMi.setFirstName(null);

        // Create the NextEmployeeMiMi, which fails.
        NextEmployeeMiMiDTO nextEmployeeMiMiDTO = nextEmployeeMiMiMapper.toDto(nextEmployeeMiMi);

        restNextEmployeeMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeMiMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextEmployeeMiMi.setLastName(null);

        // Create the NextEmployeeMiMi, which fails.
        NextEmployeeMiMiDTO nextEmployeeMiMiDTO = nextEmployeeMiMiMapper.toDto(nextEmployeeMiMi);

        restNextEmployeeMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeMiMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextEmployeeMiMi.setEmail(null);

        // Create the NextEmployeeMiMi, which fails.
        NextEmployeeMiMiDTO nextEmployeeMiMiDTO = nextEmployeeMiMiMapper.toDto(nextEmployeeMiMi);

        restNextEmployeeMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeMiMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMiMis() throws Exception {
        // Initialize the database
        insertedNextEmployeeMiMi = nextEmployeeMiMiRepository.saveAndFlush(nextEmployeeMiMi);

        // Get all the nextEmployeeMiMiList
        restNextEmployeeMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextEmployeeMiMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));
    }

    @Test
    @Transactional
    void getNextEmployeeMiMi() throws Exception {
        // Initialize the database
        insertedNextEmployeeMiMi = nextEmployeeMiMiRepository.saveAndFlush(nextEmployeeMiMi);

        // Get the nextEmployeeMiMi
        restNextEmployeeMiMiMockMvc
            .perform(get(ENTITY_API_URL_ID, nextEmployeeMiMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextEmployeeMiMi.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.hireDate").value(DEFAULT_HIRE_DATE.toString()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION));
    }

    @Test
    @Transactional
    void getNextEmployeeMiMisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextEmployeeMiMi = nextEmployeeMiMiRepository.saveAndFlush(nextEmployeeMiMi);

        Long id = nextEmployeeMiMi.getId();

        defaultNextEmployeeMiMiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextEmployeeMiMiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextEmployeeMiMiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMiMisByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeMiMi = nextEmployeeMiMiRepository.saveAndFlush(nextEmployeeMiMi);

        // Get all the nextEmployeeMiMiList where firstName equals to
        defaultNextEmployeeMiMiFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMiMisByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeMiMi = nextEmployeeMiMiRepository.saveAndFlush(nextEmployeeMiMi);

        // Get all the nextEmployeeMiMiList where firstName in
        defaultNextEmployeeMiMiFiltering(
            "firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME,
            "firstName.in=" + UPDATED_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllNextEmployeeMiMisByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeMiMi = nextEmployeeMiMiRepository.saveAndFlush(nextEmployeeMiMi);

        // Get all the nextEmployeeMiMiList where firstName is not null
        defaultNextEmployeeMiMiFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeMiMisByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeMiMi = nextEmployeeMiMiRepository.saveAndFlush(nextEmployeeMiMi);

        // Get all the nextEmployeeMiMiList where firstName contains
        defaultNextEmployeeMiMiFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMiMisByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeMiMi = nextEmployeeMiMiRepository.saveAndFlush(nextEmployeeMiMi);

        // Get all the nextEmployeeMiMiList where firstName does not contain
        defaultNextEmployeeMiMiFiltering(
            "firstName.doesNotContain=" + UPDATED_FIRST_NAME,
            "firstName.doesNotContain=" + DEFAULT_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllNextEmployeeMiMisByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeMiMi = nextEmployeeMiMiRepository.saveAndFlush(nextEmployeeMiMi);

        // Get all the nextEmployeeMiMiList where lastName equals to
        defaultNextEmployeeMiMiFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMiMisByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeMiMi = nextEmployeeMiMiRepository.saveAndFlush(nextEmployeeMiMi);

        // Get all the nextEmployeeMiMiList where lastName in
        defaultNextEmployeeMiMiFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMiMisByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeMiMi = nextEmployeeMiMiRepository.saveAndFlush(nextEmployeeMiMi);

        // Get all the nextEmployeeMiMiList where lastName is not null
        defaultNextEmployeeMiMiFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeMiMisByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeMiMi = nextEmployeeMiMiRepository.saveAndFlush(nextEmployeeMiMi);

        // Get all the nextEmployeeMiMiList where lastName contains
        defaultNextEmployeeMiMiFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMiMisByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeMiMi = nextEmployeeMiMiRepository.saveAndFlush(nextEmployeeMiMi);

        // Get all the nextEmployeeMiMiList where lastName does not contain
        defaultNextEmployeeMiMiFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMiMisByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeMiMi = nextEmployeeMiMiRepository.saveAndFlush(nextEmployeeMiMi);

        // Get all the nextEmployeeMiMiList where email equals to
        defaultNextEmployeeMiMiFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMiMisByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeMiMi = nextEmployeeMiMiRepository.saveAndFlush(nextEmployeeMiMi);

        // Get all the nextEmployeeMiMiList where email in
        defaultNextEmployeeMiMiFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMiMisByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeMiMi = nextEmployeeMiMiRepository.saveAndFlush(nextEmployeeMiMi);

        // Get all the nextEmployeeMiMiList where email is not null
        defaultNextEmployeeMiMiFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeMiMisByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeMiMi = nextEmployeeMiMiRepository.saveAndFlush(nextEmployeeMiMi);

        // Get all the nextEmployeeMiMiList where email contains
        defaultNextEmployeeMiMiFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMiMisByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeMiMi = nextEmployeeMiMiRepository.saveAndFlush(nextEmployeeMiMi);

        // Get all the nextEmployeeMiMiList where email does not contain
        defaultNextEmployeeMiMiFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMiMisByHireDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeMiMi = nextEmployeeMiMiRepository.saveAndFlush(nextEmployeeMiMi);

        // Get all the nextEmployeeMiMiList where hireDate equals to
        defaultNextEmployeeMiMiFiltering("hireDate.equals=" + DEFAULT_HIRE_DATE, "hireDate.equals=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMiMisByHireDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeMiMi = nextEmployeeMiMiRepository.saveAndFlush(nextEmployeeMiMi);

        // Get all the nextEmployeeMiMiList where hireDate in
        defaultNextEmployeeMiMiFiltering("hireDate.in=" + DEFAULT_HIRE_DATE + "," + UPDATED_HIRE_DATE, "hireDate.in=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMiMisByHireDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeMiMi = nextEmployeeMiMiRepository.saveAndFlush(nextEmployeeMiMi);

        // Get all the nextEmployeeMiMiList where hireDate is not null
        defaultNextEmployeeMiMiFiltering("hireDate.specified=true", "hireDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeMiMisByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeMiMi = nextEmployeeMiMiRepository.saveAndFlush(nextEmployeeMiMi);

        // Get all the nextEmployeeMiMiList where position equals to
        defaultNextEmployeeMiMiFiltering("position.equals=" + DEFAULT_POSITION, "position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMiMisByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeMiMi = nextEmployeeMiMiRepository.saveAndFlush(nextEmployeeMiMi);

        // Get all the nextEmployeeMiMiList where position in
        defaultNextEmployeeMiMiFiltering("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION, "position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMiMisByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeMiMi = nextEmployeeMiMiRepository.saveAndFlush(nextEmployeeMiMi);

        // Get all the nextEmployeeMiMiList where position is not null
        defaultNextEmployeeMiMiFiltering("position.specified=true", "position.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeMiMisByPositionContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeMiMi = nextEmployeeMiMiRepository.saveAndFlush(nextEmployeeMiMi);

        // Get all the nextEmployeeMiMiList where position contains
        defaultNextEmployeeMiMiFiltering("position.contains=" + DEFAULT_POSITION, "position.contains=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMiMisByPositionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeMiMi = nextEmployeeMiMiRepository.saveAndFlush(nextEmployeeMiMi);

        // Get all the nextEmployeeMiMiList where position does not contain
        defaultNextEmployeeMiMiFiltering("position.doesNotContain=" + UPDATED_POSITION, "position.doesNotContain=" + DEFAULT_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMiMisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextEmployeeMiMiRepository.saveAndFlush(nextEmployeeMiMi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextEmployeeMiMi.setTenant(tenant);
        nextEmployeeMiMiRepository.saveAndFlush(nextEmployeeMiMi);
        Long tenantId = tenant.getId();
        // Get all the nextEmployeeMiMiList where tenant equals to tenantId
        defaultNextEmployeeMiMiShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextEmployeeMiMiList where tenant equals to (tenantId + 1)
        defaultNextEmployeeMiMiShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextEmployeeMiMiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextEmployeeMiMiShouldBeFound(shouldBeFound);
        defaultNextEmployeeMiMiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextEmployeeMiMiShouldBeFound(String filter) throws Exception {
        restNextEmployeeMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextEmployeeMiMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));

        // Check, that the count call also returns 1
        restNextEmployeeMiMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextEmployeeMiMiShouldNotBeFound(String filter) throws Exception {
        restNextEmployeeMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextEmployeeMiMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextEmployeeMiMi() throws Exception {
        // Get the nextEmployeeMiMi
        restNextEmployeeMiMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextEmployeeMiMi() throws Exception {
        // Initialize the database
        insertedNextEmployeeMiMi = nextEmployeeMiMiRepository.saveAndFlush(nextEmployeeMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextEmployeeMiMi
        NextEmployeeMiMi updatedNextEmployeeMiMi = nextEmployeeMiMiRepository.findById(nextEmployeeMiMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextEmployeeMiMi are not directly saved in db
        em.detach(updatedNextEmployeeMiMi);
        updatedNextEmployeeMiMi
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);
        NextEmployeeMiMiDTO nextEmployeeMiMiDTO = nextEmployeeMiMiMapper.toDto(updatedNextEmployeeMiMi);

        restNextEmployeeMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextEmployeeMiMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextEmployeeMiMiDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextEmployeeMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextEmployeeMiMiToMatchAllProperties(updatedNextEmployeeMiMi);
    }

    @Test
    @Transactional
    void putNonExistingNextEmployeeMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeMiMi.setId(longCount.incrementAndGet());

        // Create the NextEmployeeMiMi
        NextEmployeeMiMiDTO nextEmployeeMiMiDTO = nextEmployeeMiMiMapper.toDto(nextEmployeeMiMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextEmployeeMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextEmployeeMiMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextEmployeeMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextEmployeeMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeMiMi.setId(longCount.incrementAndGet());

        // Create the NextEmployeeMiMi
        NextEmployeeMiMiDTO nextEmployeeMiMiDTO = nextEmployeeMiMiMapper.toDto(nextEmployeeMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextEmployeeMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextEmployeeMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeMiMi.setId(longCount.incrementAndGet());

        // Create the NextEmployeeMiMi
        NextEmployeeMiMiDTO nextEmployeeMiMiDTO = nextEmployeeMiMiMapper.toDto(nextEmployeeMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeMiMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeMiMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextEmployeeMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextEmployeeMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextEmployeeMiMi = nextEmployeeMiMiRepository.saveAndFlush(nextEmployeeMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextEmployeeMiMi using partial update
        NextEmployeeMiMi partialUpdatedNextEmployeeMiMi = new NextEmployeeMiMi();
        partialUpdatedNextEmployeeMiMi.setId(nextEmployeeMiMi.getId());

        partialUpdatedNextEmployeeMiMi.firstName(UPDATED_FIRST_NAME);

        restNextEmployeeMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextEmployeeMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextEmployeeMiMi))
            )
            .andExpect(status().isOk());

        // Validate the NextEmployeeMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextEmployeeMiMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextEmployeeMiMi, nextEmployeeMiMi),
            getPersistedNextEmployeeMiMi(nextEmployeeMiMi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextEmployeeMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextEmployeeMiMi = nextEmployeeMiMiRepository.saveAndFlush(nextEmployeeMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextEmployeeMiMi using partial update
        NextEmployeeMiMi partialUpdatedNextEmployeeMiMi = new NextEmployeeMiMi();
        partialUpdatedNextEmployeeMiMi.setId(nextEmployeeMiMi.getId());

        partialUpdatedNextEmployeeMiMi
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);

        restNextEmployeeMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextEmployeeMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextEmployeeMiMi))
            )
            .andExpect(status().isOk());

        // Validate the NextEmployeeMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextEmployeeMiMiUpdatableFieldsEquals(
            partialUpdatedNextEmployeeMiMi,
            getPersistedNextEmployeeMiMi(partialUpdatedNextEmployeeMiMi)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextEmployeeMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeMiMi.setId(longCount.incrementAndGet());

        // Create the NextEmployeeMiMi
        NextEmployeeMiMiDTO nextEmployeeMiMiDTO = nextEmployeeMiMiMapper.toDto(nextEmployeeMiMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextEmployeeMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextEmployeeMiMiDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextEmployeeMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextEmployeeMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeMiMi.setId(longCount.incrementAndGet());

        // Create the NextEmployeeMiMi
        NextEmployeeMiMiDTO nextEmployeeMiMiDTO = nextEmployeeMiMiMapper.toDto(nextEmployeeMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextEmployeeMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextEmployeeMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeMiMi.setId(longCount.incrementAndGet());

        // Create the NextEmployeeMiMi
        NextEmployeeMiMiDTO nextEmployeeMiMiDTO = nextEmployeeMiMiMapper.toDto(nextEmployeeMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeMiMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextEmployeeMiMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextEmployeeMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextEmployeeMiMi() throws Exception {
        // Initialize the database
        insertedNextEmployeeMiMi = nextEmployeeMiMiRepository.saveAndFlush(nextEmployeeMiMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextEmployeeMiMi
        restNextEmployeeMiMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextEmployeeMiMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextEmployeeMiMiRepository.count();
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

    protected NextEmployeeMiMi getPersistedNextEmployeeMiMi(NextEmployeeMiMi nextEmployeeMiMi) {
        return nextEmployeeMiMiRepository.findById(nextEmployeeMiMi.getId()).orElseThrow();
    }

    protected void assertPersistedNextEmployeeMiMiToMatchAllProperties(NextEmployeeMiMi expectedNextEmployeeMiMi) {
        assertNextEmployeeMiMiAllPropertiesEquals(expectedNextEmployeeMiMi, getPersistedNextEmployeeMiMi(expectedNextEmployeeMiMi));
    }

    protected void assertPersistedNextEmployeeMiMiToMatchUpdatableProperties(NextEmployeeMiMi expectedNextEmployeeMiMi) {
        assertNextEmployeeMiMiAllUpdatablePropertiesEquals(
            expectedNextEmployeeMiMi,
            getPersistedNextEmployeeMiMi(expectedNextEmployeeMiMi)
        );
    }
}
