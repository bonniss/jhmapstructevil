package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextEmployeeBetaAsserts.*;
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
import xyz.jhmapstruct.domain.NextEmployeeBeta;
import xyz.jhmapstruct.repository.NextEmployeeBetaRepository;
import xyz.jhmapstruct.service.dto.NextEmployeeBetaDTO;
import xyz.jhmapstruct.service.mapper.NextEmployeeBetaMapper;

/**
 * Integration tests for the {@link NextEmployeeBetaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextEmployeeBetaResourceIT {

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

    private static final String ENTITY_API_URL = "/api/next-employee-betas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextEmployeeBetaRepository nextEmployeeBetaRepository;

    @Autowired
    private NextEmployeeBetaMapper nextEmployeeBetaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextEmployeeBetaMockMvc;

    private NextEmployeeBeta nextEmployeeBeta;

    private NextEmployeeBeta insertedNextEmployeeBeta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextEmployeeBeta createEntity() {
        return new NextEmployeeBeta()
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
    public static NextEmployeeBeta createUpdatedEntity() {
        return new NextEmployeeBeta()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);
    }

    @BeforeEach
    public void initTest() {
        nextEmployeeBeta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextEmployeeBeta != null) {
            nextEmployeeBetaRepository.delete(insertedNextEmployeeBeta);
            insertedNextEmployeeBeta = null;
        }
    }

    @Test
    @Transactional
    void createNextEmployeeBeta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextEmployeeBeta
        NextEmployeeBetaDTO nextEmployeeBetaDTO = nextEmployeeBetaMapper.toDto(nextEmployeeBeta);
        var returnedNextEmployeeBetaDTO = om.readValue(
            restNextEmployeeBetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeBetaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextEmployeeBetaDTO.class
        );

        // Validate the NextEmployeeBeta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextEmployeeBeta = nextEmployeeBetaMapper.toEntity(returnedNextEmployeeBetaDTO);
        assertNextEmployeeBetaUpdatableFieldsEquals(returnedNextEmployeeBeta, getPersistedNextEmployeeBeta(returnedNextEmployeeBeta));

        insertedNextEmployeeBeta = returnedNextEmployeeBeta;
    }

    @Test
    @Transactional
    void createNextEmployeeBetaWithExistingId() throws Exception {
        // Create the NextEmployeeBeta with an existing ID
        nextEmployeeBeta.setId(1L);
        NextEmployeeBetaDTO nextEmployeeBetaDTO = nextEmployeeBetaMapper.toDto(nextEmployeeBeta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextEmployeeBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeBetaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextEmployeeBeta.setFirstName(null);

        // Create the NextEmployeeBeta, which fails.
        NextEmployeeBetaDTO nextEmployeeBetaDTO = nextEmployeeBetaMapper.toDto(nextEmployeeBeta);

        restNextEmployeeBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeBetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextEmployeeBeta.setLastName(null);

        // Create the NextEmployeeBeta, which fails.
        NextEmployeeBetaDTO nextEmployeeBetaDTO = nextEmployeeBetaMapper.toDto(nextEmployeeBeta);

        restNextEmployeeBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeBetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextEmployeeBeta.setEmail(null);

        // Create the NextEmployeeBeta, which fails.
        NextEmployeeBetaDTO nextEmployeeBetaDTO = nextEmployeeBetaMapper.toDto(nextEmployeeBeta);

        restNextEmployeeBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeBetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextEmployeeBetas() throws Exception {
        // Initialize the database
        insertedNextEmployeeBeta = nextEmployeeBetaRepository.saveAndFlush(nextEmployeeBeta);

        // Get all the nextEmployeeBetaList
        restNextEmployeeBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextEmployeeBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));
    }

    @Test
    @Transactional
    void getNextEmployeeBeta() throws Exception {
        // Initialize the database
        insertedNextEmployeeBeta = nextEmployeeBetaRepository.saveAndFlush(nextEmployeeBeta);

        // Get the nextEmployeeBeta
        restNextEmployeeBetaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextEmployeeBeta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextEmployeeBeta.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.hireDate").value(DEFAULT_HIRE_DATE.toString()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION));
    }

    @Test
    @Transactional
    void getNextEmployeeBetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextEmployeeBeta = nextEmployeeBetaRepository.saveAndFlush(nextEmployeeBeta);

        Long id = nextEmployeeBeta.getId();

        defaultNextEmployeeBetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextEmployeeBetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextEmployeeBetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextEmployeeBetasByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeBeta = nextEmployeeBetaRepository.saveAndFlush(nextEmployeeBeta);

        // Get all the nextEmployeeBetaList where firstName equals to
        defaultNextEmployeeBetaFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeBetasByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeBeta = nextEmployeeBetaRepository.saveAndFlush(nextEmployeeBeta);

        // Get all the nextEmployeeBetaList where firstName in
        defaultNextEmployeeBetaFiltering(
            "firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME,
            "firstName.in=" + UPDATED_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllNextEmployeeBetasByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeBeta = nextEmployeeBetaRepository.saveAndFlush(nextEmployeeBeta);

        // Get all the nextEmployeeBetaList where firstName is not null
        defaultNextEmployeeBetaFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeBetasByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeBeta = nextEmployeeBetaRepository.saveAndFlush(nextEmployeeBeta);

        // Get all the nextEmployeeBetaList where firstName contains
        defaultNextEmployeeBetaFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeBetasByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeBeta = nextEmployeeBetaRepository.saveAndFlush(nextEmployeeBeta);

        // Get all the nextEmployeeBetaList where firstName does not contain
        defaultNextEmployeeBetaFiltering(
            "firstName.doesNotContain=" + UPDATED_FIRST_NAME,
            "firstName.doesNotContain=" + DEFAULT_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllNextEmployeeBetasByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeBeta = nextEmployeeBetaRepository.saveAndFlush(nextEmployeeBeta);

        // Get all the nextEmployeeBetaList where lastName equals to
        defaultNextEmployeeBetaFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeBetasByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeBeta = nextEmployeeBetaRepository.saveAndFlush(nextEmployeeBeta);

        // Get all the nextEmployeeBetaList where lastName in
        defaultNextEmployeeBetaFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeBetasByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeBeta = nextEmployeeBetaRepository.saveAndFlush(nextEmployeeBeta);

        // Get all the nextEmployeeBetaList where lastName is not null
        defaultNextEmployeeBetaFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeBetasByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeBeta = nextEmployeeBetaRepository.saveAndFlush(nextEmployeeBeta);

        // Get all the nextEmployeeBetaList where lastName contains
        defaultNextEmployeeBetaFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeBetasByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeBeta = nextEmployeeBetaRepository.saveAndFlush(nextEmployeeBeta);

        // Get all the nextEmployeeBetaList where lastName does not contain
        defaultNextEmployeeBetaFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeBetasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeBeta = nextEmployeeBetaRepository.saveAndFlush(nextEmployeeBeta);

        // Get all the nextEmployeeBetaList where email equals to
        defaultNextEmployeeBetaFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeeBetasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeBeta = nextEmployeeBetaRepository.saveAndFlush(nextEmployeeBeta);

        // Get all the nextEmployeeBetaList where email in
        defaultNextEmployeeBetaFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeeBetasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeBeta = nextEmployeeBetaRepository.saveAndFlush(nextEmployeeBeta);

        // Get all the nextEmployeeBetaList where email is not null
        defaultNextEmployeeBetaFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeBetasByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeBeta = nextEmployeeBetaRepository.saveAndFlush(nextEmployeeBeta);

        // Get all the nextEmployeeBetaList where email contains
        defaultNextEmployeeBetaFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeeBetasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeBeta = nextEmployeeBetaRepository.saveAndFlush(nextEmployeeBeta);

        // Get all the nextEmployeeBetaList where email does not contain
        defaultNextEmployeeBetaFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeeBetasByHireDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeBeta = nextEmployeeBetaRepository.saveAndFlush(nextEmployeeBeta);

        // Get all the nextEmployeeBetaList where hireDate equals to
        defaultNextEmployeeBetaFiltering("hireDate.equals=" + DEFAULT_HIRE_DATE, "hireDate.equals=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllNextEmployeeBetasByHireDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeBeta = nextEmployeeBetaRepository.saveAndFlush(nextEmployeeBeta);

        // Get all the nextEmployeeBetaList where hireDate in
        defaultNextEmployeeBetaFiltering("hireDate.in=" + DEFAULT_HIRE_DATE + "," + UPDATED_HIRE_DATE, "hireDate.in=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllNextEmployeeBetasByHireDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeBeta = nextEmployeeBetaRepository.saveAndFlush(nextEmployeeBeta);

        // Get all the nextEmployeeBetaList where hireDate is not null
        defaultNextEmployeeBetaFiltering("hireDate.specified=true", "hireDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeBetasByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeBeta = nextEmployeeBetaRepository.saveAndFlush(nextEmployeeBeta);

        // Get all the nextEmployeeBetaList where position equals to
        defaultNextEmployeeBetaFiltering("position.equals=" + DEFAULT_POSITION, "position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeeBetasByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeBeta = nextEmployeeBetaRepository.saveAndFlush(nextEmployeeBeta);

        // Get all the nextEmployeeBetaList where position in
        defaultNextEmployeeBetaFiltering("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION, "position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeeBetasByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeBeta = nextEmployeeBetaRepository.saveAndFlush(nextEmployeeBeta);

        // Get all the nextEmployeeBetaList where position is not null
        defaultNextEmployeeBetaFiltering("position.specified=true", "position.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeBetasByPositionContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeBeta = nextEmployeeBetaRepository.saveAndFlush(nextEmployeeBeta);

        // Get all the nextEmployeeBetaList where position contains
        defaultNextEmployeeBetaFiltering("position.contains=" + DEFAULT_POSITION, "position.contains=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeeBetasByPositionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeBeta = nextEmployeeBetaRepository.saveAndFlush(nextEmployeeBeta);

        // Get all the nextEmployeeBetaList where position does not contain
        defaultNextEmployeeBetaFiltering("position.doesNotContain=" + UPDATED_POSITION, "position.doesNotContain=" + DEFAULT_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeeBetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextEmployeeBetaRepository.saveAndFlush(nextEmployeeBeta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextEmployeeBeta.setTenant(tenant);
        nextEmployeeBetaRepository.saveAndFlush(nextEmployeeBeta);
        Long tenantId = tenant.getId();
        // Get all the nextEmployeeBetaList where tenant equals to tenantId
        defaultNextEmployeeBetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextEmployeeBetaList where tenant equals to (tenantId + 1)
        defaultNextEmployeeBetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextEmployeeBetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextEmployeeBetaShouldBeFound(shouldBeFound);
        defaultNextEmployeeBetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextEmployeeBetaShouldBeFound(String filter) throws Exception {
        restNextEmployeeBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextEmployeeBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));

        // Check, that the count call also returns 1
        restNextEmployeeBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextEmployeeBetaShouldNotBeFound(String filter) throws Exception {
        restNextEmployeeBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextEmployeeBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextEmployeeBeta() throws Exception {
        // Get the nextEmployeeBeta
        restNextEmployeeBetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextEmployeeBeta() throws Exception {
        // Initialize the database
        insertedNextEmployeeBeta = nextEmployeeBetaRepository.saveAndFlush(nextEmployeeBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextEmployeeBeta
        NextEmployeeBeta updatedNextEmployeeBeta = nextEmployeeBetaRepository.findById(nextEmployeeBeta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextEmployeeBeta are not directly saved in db
        em.detach(updatedNextEmployeeBeta);
        updatedNextEmployeeBeta
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);
        NextEmployeeBetaDTO nextEmployeeBetaDTO = nextEmployeeBetaMapper.toDto(updatedNextEmployeeBeta);

        restNextEmployeeBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextEmployeeBetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextEmployeeBetaDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextEmployeeBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextEmployeeBetaToMatchAllProperties(updatedNextEmployeeBeta);
    }

    @Test
    @Transactional
    void putNonExistingNextEmployeeBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeBeta.setId(longCount.incrementAndGet());

        // Create the NextEmployeeBeta
        NextEmployeeBetaDTO nextEmployeeBetaDTO = nextEmployeeBetaMapper.toDto(nextEmployeeBeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextEmployeeBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextEmployeeBetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextEmployeeBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextEmployeeBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeBeta.setId(longCount.incrementAndGet());

        // Create the NextEmployeeBeta
        NextEmployeeBetaDTO nextEmployeeBetaDTO = nextEmployeeBetaMapper.toDto(nextEmployeeBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextEmployeeBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextEmployeeBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeBeta.setId(longCount.incrementAndGet());

        // Create the NextEmployeeBeta
        NextEmployeeBetaDTO nextEmployeeBetaDTO = nextEmployeeBetaMapper.toDto(nextEmployeeBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeBetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeBetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextEmployeeBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextEmployeeBetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextEmployeeBeta = nextEmployeeBetaRepository.saveAndFlush(nextEmployeeBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextEmployeeBeta using partial update
        NextEmployeeBeta partialUpdatedNextEmployeeBeta = new NextEmployeeBeta();
        partialUpdatedNextEmployeeBeta.setId(nextEmployeeBeta.getId());

        partialUpdatedNextEmployeeBeta.lastName(UPDATED_LAST_NAME).hireDate(UPDATED_HIRE_DATE).position(UPDATED_POSITION);

        restNextEmployeeBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextEmployeeBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextEmployeeBeta))
            )
            .andExpect(status().isOk());

        // Validate the NextEmployeeBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextEmployeeBetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextEmployeeBeta, nextEmployeeBeta),
            getPersistedNextEmployeeBeta(nextEmployeeBeta)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextEmployeeBetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextEmployeeBeta = nextEmployeeBetaRepository.saveAndFlush(nextEmployeeBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextEmployeeBeta using partial update
        NextEmployeeBeta partialUpdatedNextEmployeeBeta = new NextEmployeeBeta();
        partialUpdatedNextEmployeeBeta.setId(nextEmployeeBeta.getId());

        partialUpdatedNextEmployeeBeta
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);

        restNextEmployeeBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextEmployeeBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextEmployeeBeta))
            )
            .andExpect(status().isOk());

        // Validate the NextEmployeeBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextEmployeeBetaUpdatableFieldsEquals(
            partialUpdatedNextEmployeeBeta,
            getPersistedNextEmployeeBeta(partialUpdatedNextEmployeeBeta)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextEmployeeBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeBeta.setId(longCount.incrementAndGet());

        // Create the NextEmployeeBeta
        NextEmployeeBetaDTO nextEmployeeBetaDTO = nextEmployeeBetaMapper.toDto(nextEmployeeBeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextEmployeeBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextEmployeeBetaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextEmployeeBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextEmployeeBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeBeta.setId(longCount.incrementAndGet());

        // Create the NextEmployeeBeta
        NextEmployeeBetaDTO nextEmployeeBetaDTO = nextEmployeeBetaMapper.toDto(nextEmployeeBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextEmployeeBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextEmployeeBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeBeta.setId(longCount.incrementAndGet());

        // Create the NextEmployeeBeta
        NextEmployeeBetaDTO nextEmployeeBetaDTO = nextEmployeeBetaMapper.toDto(nextEmployeeBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeBetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextEmployeeBetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextEmployeeBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextEmployeeBeta() throws Exception {
        // Initialize the database
        insertedNextEmployeeBeta = nextEmployeeBetaRepository.saveAndFlush(nextEmployeeBeta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextEmployeeBeta
        restNextEmployeeBetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextEmployeeBeta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextEmployeeBetaRepository.count();
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

    protected NextEmployeeBeta getPersistedNextEmployeeBeta(NextEmployeeBeta nextEmployeeBeta) {
        return nextEmployeeBetaRepository.findById(nextEmployeeBeta.getId()).orElseThrow();
    }

    protected void assertPersistedNextEmployeeBetaToMatchAllProperties(NextEmployeeBeta expectedNextEmployeeBeta) {
        assertNextEmployeeBetaAllPropertiesEquals(expectedNextEmployeeBeta, getPersistedNextEmployeeBeta(expectedNextEmployeeBeta));
    }

    protected void assertPersistedNextEmployeeBetaToMatchUpdatableProperties(NextEmployeeBeta expectedNextEmployeeBeta) {
        assertNextEmployeeBetaAllUpdatablePropertiesEquals(
            expectedNextEmployeeBeta,
            getPersistedNextEmployeeBeta(expectedNextEmployeeBeta)
        );
    }
}
