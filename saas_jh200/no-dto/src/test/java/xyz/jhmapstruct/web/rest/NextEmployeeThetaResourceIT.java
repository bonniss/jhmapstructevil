package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextEmployeeThetaAsserts.*;
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
import xyz.jhmapstruct.domain.NextEmployeeTheta;
import xyz.jhmapstruct.repository.NextEmployeeThetaRepository;

/**
 * Integration tests for the {@link NextEmployeeThetaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextEmployeeThetaResourceIT {

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

    private static final String ENTITY_API_URL = "/api/next-employee-thetas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextEmployeeThetaRepository nextEmployeeThetaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextEmployeeThetaMockMvc;

    private NextEmployeeTheta nextEmployeeTheta;

    private NextEmployeeTheta insertedNextEmployeeTheta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextEmployeeTheta createEntity() {
        return new NextEmployeeTheta()
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
    public static NextEmployeeTheta createUpdatedEntity() {
        return new NextEmployeeTheta()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);
    }

    @BeforeEach
    public void initTest() {
        nextEmployeeTheta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextEmployeeTheta != null) {
            nextEmployeeThetaRepository.delete(insertedNextEmployeeTheta);
            insertedNextEmployeeTheta = null;
        }
    }

    @Test
    @Transactional
    void createNextEmployeeTheta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextEmployeeTheta
        var returnedNextEmployeeTheta = om.readValue(
            restNextEmployeeThetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeTheta)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextEmployeeTheta.class
        );

        // Validate the NextEmployeeTheta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextEmployeeThetaUpdatableFieldsEquals(returnedNextEmployeeTheta, getPersistedNextEmployeeTheta(returnedNextEmployeeTheta));

        insertedNextEmployeeTheta = returnedNextEmployeeTheta;
    }

    @Test
    @Transactional
    void createNextEmployeeThetaWithExistingId() throws Exception {
        // Create the NextEmployeeTheta with an existing ID
        nextEmployeeTheta.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextEmployeeThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeTheta)))
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextEmployeeTheta.setFirstName(null);

        // Create the NextEmployeeTheta, which fails.

        restNextEmployeeThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeTheta)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextEmployeeTheta.setLastName(null);

        // Create the NextEmployeeTheta, which fails.

        restNextEmployeeThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeTheta)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextEmployeeTheta.setEmail(null);

        // Create the NextEmployeeTheta, which fails.

        restNextEmployeeThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeTheta)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextEmployeeThetas() throws Exception {
        // Initialize the database
        insertedNextEmployeeTheta = nextEmployeeThetaRepository.saveAndFlush(nextEmployeeTheta);

        // Get all the nextEmployeeThetaList
        restNextEmployeeThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextEmployeeTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));
    }

    @Test
    @Transactional
    void getNextEmployeeTheta() throws Exception {
        // Initialize the database
        insertedNextEmployeeTheta = nextEmployeeThetaRepository.saveAndFlush(nextEmployeeTheta);

        // Get the nextEmployeeTheta
        restNextEmployeeThetaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextEmployeeTheta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextEmployeeTheta.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.hireDate").value(DEFAULT_HIRE_DATE.toString()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION));
    }

    @Test
    @Transactional
    void getNextEmployeeThetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextEmployeeTheta = nextEmployeeThetaRepository.saveAndFlush(nextEmployeeTheta);

        Long id = nextEmployeeTheta.getId();

        defaultNextEmployeeThetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextEmployeeThetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextEmployeeThetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextEmployeeThetasByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeTheta = nextEmployeeThetaRepository.saveAndFlush(nextEmployeeTheta);

        // Get all the nextEmployeeThetaList where firstName equals to
        defaultNextEmployeeThetaFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeThetasByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeTheta = nextEmployeeThetaRepository.saveAndFlush(nextEmployeeTheta);

        // Get all the nextEmployeeThetaList where firstName in
        defaultNextEmployeeThetaFiltering(
            "firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME,
            "firstName.in=" + UPDATED_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllNextEmployeeThetasByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeTheta = nextEmployeeThetaRepository.saveAndFlush(nextEmployeeTheta);

        // Get all the nextEmployeeThetaList where firstName is not null
        defaultNextEmployeeThetaFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeThetasByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeTheta = nextEmployeeThetaRepository.saveAndFlush(nextEmployeeTheta);

        // Get all the nextEmployeeThetaList where firstName contains
        defaultNextEmployeeThetaFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeThetasByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeTheta = nextEmployeeThetaRepository.saveAndFlush(nextEmployeeTheta);

        // Get all the nextEmployeeThetaList where firstName does not contain
        defaultNextEmployeeThetaFiltering(
            "firstName.doesNotContain=" + UPDATED_FIRST_NAME,
            "firstName.doesNotContain=" + DEFAULT_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllNextEmployeeThetasByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeTheta = nextEmployeeThetaRepository.saveAndFlush(nextEmployeeTheta);

        // Get all the nextEmployeeThetaList where lastName equals to
        defaultNextEmployeeThetaFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeThetasByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeTheta = nextEmployeeThetaRepository.saveAndFlush(nextEmployeeTheta);

        // Get all the nextEmployeeThetaList where lastName in
        defaultNextEmployeeThetaFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeThetasByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeTheta = nextEmployeeThetaRepository.saveAndFlush(nextEmployeeTheta);

        // Get all the nextEmployeeThetaList where lastName is not null
        defaultNextEmployeeThetaFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeThetasByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeTheta = nextEmployeeThetaRepository.saveAndFlush(nextEmployeeTheta);

        // Get all the nextEmployeeThetaList where lastName contains
        defaultNextEmployeeThetaFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeThetasByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeTheta = nextEmployeeThetaRepository.saveAndFlush(nextEmployeeTheta);

        // Get all the nextEmployeeThetaList where lastName does not contain
        defaultNextEmployeeThetaFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeThetasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeTheta = nextEmployeeThetaRepository.saveAndFlush(nextEmployeeTheta);

        // Get all the nextEmployeeThetaList where email equals to
        defaultNextEmployeeThetaFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeeThetasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeTheta = nextEmployeeThetaRepository.saveAndFlush(nextEmployeeTheta);

        // Get all the nextEmployeeThetaList where email in
        defaultNextEmployeeThetaFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeeThetasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeTheta = nextEmployeeThetaRepository.saveAndFlush(nextEmployeeTheta);

        // Get all the nextEmployeeThetaList where email is not null
        defaultNextEmployeeThetaFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeThetasByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeTheta = nextEmployeeThetaRepository.saveAndFlush(nextEmployeeTheta);

        // Get all the nextEmployeeThetaList where email contains
        defaultNextEmployeeThetaFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeeThetasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeTheta = nextEmployeeThetaRepository.saveAndFlush(nextEmployeeTheta);

        // Get all the nextEmployeeThetaList where email does not contain
        defaultNextEmployeeThetaFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeeThetasByHireDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeTheta = nextEmployeeThetaRepository.saveAndFlush(nextEmployeeTheta);

        // Get all the nextEmployeeThetaList where hireDate equals to
        defaultNextEmployeeThetaFiltering("hireDate.equals=" + DEFAULT_HIRE_DATE, "hireDate.equals=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllNextEmployeeThetasByHireDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeTheta = nextEmployeeThetaRepository.saveAndFlush(nextEmployeeTheta);

        // Get all the nextEmployeeThetaList where hireDate in
        defaultNextEmployeeThetaFiltering("hireDate.in=" + DEFAULT_HIRE_DATE + "," + UPDATED_HIRE_DATE, "hireDate.in=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllNextEmployeeThetasByHireDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeTheta = nextEmployeeThetaRepository.saveAndFlush(nextEmployeeTheta);

        // Get all the nextEmployeeThetaList where hireDate is not null
        defaultNextEmployeeThetaFiltering("hireDate.specified=true", "hireDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeThetasByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeTheta = nextEmployeeThetaRepository.saveAndFlush(nextEmployeeTheta);

        // Get all the nextEmployeeThetaList where position equals to
        defaultNextEmployeeThetaFiltering("position.equals=" + DEFAULT_POSITION, "position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeeThetasByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeTheta = nextEmployeeThetaRepository.saveAndFlush(nextEmployeeTheta);

        // Get all the nextEmployeeThetaList where position in
        defaultNextEmployeeThetaFiltering("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION, "position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeeThetasByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeTheta = nextEmployeeThetaRepository.saveAndFlush(nextEmployeeTheta);

        // Get all the nextEmployeeThetaList where position is not null
        defaultNextEmployeeThetaFiltering("position.specified=true", "position.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeThetasByPositionContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeTheta = nextEmployeeThetaRepository.saveAndFlush(nextEmployeeTheta);

        // Get all the nextEmployeeThetaList where position contains
        defaultNextEmployeeThetaFiltering("position.contains=" + DEFAULT_POSITION, "position.contains=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeeThetasByPositionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeTheta = nextEmployeeThetaRepository.saveAndFlush(nextEmployeeTheta);

        // Get all the nextEmployeeThetaList where position does not contain
        defaultNextEmployeeThetaFiltering("position.doesNotContain=" + UPDATED_POSITION, "position.doesNotContain=" + DEFAULT_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeeThetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextEmployeeThetaRepository.saveAndFlush(nextEmployeeTheta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextEmployeeTheta.setTenant(tenant);
        nextEmployeeThetaRepository.saveAndFlush(nextEmployeeTheta);
        Long tenantId = tenant.getId();
        // Get all the nextEmployeeThetaList where tenant equals to tenantId
        defaultNextEmployeeThetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextEmployeeThetaList where tenant equals to (tenantId + 1)
        defaultNextEmployeeThetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextEmployeeThetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextEmployeeThetaShouldBeFound(shouldBeFound);
        defaultNextEmployeeThetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextEmployeeThetaShouldBeFound(String filter) throws Exception {
        restNextEmployeeThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextEmployeeTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));

        // Check, that the count call also returns 1
        restNextEmployeeThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextEmployeeThetaShouldNotBeFound(String filter) throws Exception {
        restNextEmployeeThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextEmployeeThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextEmployeeTheta() throws Exception {
        // Get the nextEmployeeTheta
        restNextEmployeeThetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextEmployeeTheta() throws Exception {
        // Initialize the database
        insertedNextEmployeeTheta = nextEmployeeThetaRepository.saveAndFlush(nextEmployeeTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextEmployeeTheta
        NextEmployeeTheta updatedNextEmployeeTheta = nextEmployeeThetaRepository.findById(nextEmployeeTheta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextEmployeeTheta are not directly saved in db
        em.detach(updatedNextEmployeeTheta);
        updatedNextEmployeeTheta
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);

        restNextEmployeeThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextEmployeeTheta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextEmployeeTheta))
            )
            .andExpect(status().isOk());

        // Validate the NextEmployeeTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextEmployeeThetaToMatchAllProperties(updatedNextEmployeeTheta);
    }

    @Test
    @Transactional
    void putNonExistingNextEmployeeTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeTheta.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextEmployeeThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextEmployeeTheta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextEmployeeTheta))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextEmployeeTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeTheta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextEmployeeTheta))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextEmployeeTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeTheta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeThetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeTheta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextEmployeeTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextEmployeeThetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextEmployeeTheta = nextEmployeeThetaRepository.saveAndFlush(nextEmployeeTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextEmployeeTheta using partial update
        NextEmployeeTheta partialUpdatedNextEmployeeTheta = new NextEmployeeTheta();
        partialUpdatedNextEmployeeTheta.setId(nextEmployeeTheta.getId());

        partialUpdatedNextEmployeeTheta
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);

        restNextEmployeeThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextEmployeeTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextEmployeeTheta))
            )
            .andExpect(status().isOk());

        // Validate the NextEmployeeTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextEmployeeThetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextEmployeeTheta, nextEmployeeTheta),
            getPersistedNextEmployeeTheta(nextEmployeeTheta)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextEmployeeThetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextEmployeeTheta = nextEmployeeThetaRepository.saveAndFlush(nextEmployeeTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextEmployeeTheta using partial update
        NextEmployeeTheta partialUpdatedNextEmployeeTheta = new NextEmployeeTheta();
        partialUpdatedNextEmployeeTheta.setId(nextEmployeeTheta.getId());

        partialUpdatedNextEmployeeTheta
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);

        restNextEmployeeThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextEmployeeTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextEmployeeTheta))
            )
            .andExpect(status().isOk());

        // Validate the NextEmployeeTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextEmployeeThetaUpdatableFieldsEquals(
            partialUpdatedNextEmployeeTheta,
            getPersistedNextEmployeeTheta(partialUpdatedNextEmployeeTheta)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextEmployeeTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeTheta.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextEmployeeThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextEmployeeTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextEmployeeTheta))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextEmployeeTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeTheta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextEmployeeTheta))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextEmployeeTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeTheta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeThetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextEmployeeTheta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextEmployeeTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextEmployeeTheta() throws Exception {
        // Initialize the database
        insertedNextEmployeeTheta = nextEmployeeThetaRepository.saveAndFlush(nextEmployeeTheta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextEmployeeTheta
        restNextEmployeeThetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextEmployeeTheta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextEmployeeThetaRepository.count();
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

    protected NextEmployeeTheta getPersistedNextEmployeeTheta(NextEmployeeTheta nextEmployeeTheta) {
        return nextEmployeeThetaRepository.findById(nextEmployeeTheta.getId()).orElseThrow();
    }

    protected void assertPersistedNextEmployeeThetaToMatchAllProperties(NextEmployeeTheta expectedNextEmployeeTheta) {
        assertNextEmployeeThetaAllPropertiesEquals(expectedNextEmployeeTheta, getPersistedNextEmployeeTheta(expectedNextEmployeeTheta));
    }

    protected void assertPersistedNextEmployeeThetaToMatchUpdatableProperties(NextEmployeeTheta expectedNextEmployeeTheta) {
        assertNextEmployeeThetaAllUpdatablePropertiesEquals(
            expectedNextEmployeeTheta,
            getPersistedNextEmployeeTheta(expectedNextEmployeeTheta)
        );
    }
}
