package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.EmployeeBetaAsserts.*;
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
import xyz.jhmapstruct.domain.EmployeeBeta;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.repository.EmployeeBetaRepository;

/**
 * Integration tests for the {@link EmployeeBetaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmployeeBetaResourceIT {

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

    private static final String ENTITY_API_URL = "/api/employee-betas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmployeeBetaRepository employeeBetaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeBetaMockMvc;

    private EmployeeBeta employeeBeta;

    private EmployeeBeta insertedEmployeeBeta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeBeta createEntity() {
        return new EmployeeBeta()
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
    public static EmployeeBeta createUpdatedEntity() {
        return new EmployeeBeta()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);
    }

    @BeforeEach
    public void initTest() {
        employeeBeta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedEmployeeBeta != null) {
            employeeBetaRepository.delete(insertedEmployeeBeta);
            insertedEmployeeBeta = null;
        }
    }

    @Test
    @Transactional
    void createEmployeeBeta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EmployeeBeta
        var returnedEmployeeBeta = om.readValue(
            restEmployeeBetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeBeta)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EmployeeBeta.class
        );

        // Validate the EmployeeBeta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEmployeeBetaUpdatableFieldsEquals(returnedEmployeeBeta, getPersistedEmployeeBeta(returnedEmployeeBeta));

        insertedEmployeeBeta = returnedEmployeeBeta;
    }

    @Test
    @Transactional
    void createEmployeeBetaWithExistingId() throws Exception {
        // Create the EmployeeBeta with an existing ID
        employeeBeta.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeBeta)))
            .andExpect(status().isBadRequest());

        // Validate the EmployeeBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employeeBeta.setFirstName(null);

        // Create the EmployeeBeta, which fails.

        restEmployeeBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeBeta)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employeeBeta.setLastName(null);

        // Create the EmployeeBeta, which fails.

        restEmployeeBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeBeta)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employeeBeta.setEmail(null);

        // Create the EmployeeBeta, which fails.

        restEmployeeBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeBeta)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmployeeBetas() throws Exception {
        // Initialize the database
        insertedEmployeeBeta = employeeBetaRepository.saveAndFlush(employeeBeta);

        // Get all the employeeBetaList
        restEmployeeBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));
    }

    @Test
    @Transactional
    void getEmployeeBeta() throws Exception {
        // Initialize the database
        insertedEmployeeBeta = employeeBetaRepository.saveAndFlush(employeeBeta);

        // Get the employeeBeta
        restEmployeeBetaMockMvc
            .perform(get(ENTITY_API_URL_ID, employeeBeta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employeeBeta.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.hireDate").value(DEFAULT_HIRE_DATE.toString()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION));
    }

    @Test
    @Transactional
    void getEmployeeBetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedEmployeeBeta = employeeBetaRepository.saveAndFlush(employeeBeta);

        Long id = employeeBeta.getId();

        defaultEmployeeBetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEmployeeBetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEmployeeBetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmployeeBetasByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployeeBeta = employeeBetaRepository.saveAndFlush(employeeBeta);

        // Get all the employeeBetaList where firstName equals to
        defaultEmployeeBetaFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeBetasByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployeeBeta = employeeBetaRepository.saveAndFlush(employeeBeta);

        // Get all the employeeBetaList where firstName in
        defaultEmployeeBetaFiltering("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME, "firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeBetasByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployeeBeta = employeeBetaRepository.saveAndFlush(employeeBeta);

        // Get all the employeeBetaList where firstName is not null
        defaultEmployeeBetaFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeBetasByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeBeta = employeeBetaRepository.saveAndFlush(employeeBeta);

        // Get all the employeeBetaList where firstName contains
        defaultEmployeeBetaFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeBetasByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeBeta = employeeBetaRepository.saveAndFlush(employeeBeta);

        // Get all the employeeBetaList where firstName does not contain
        defaultEmployeeBetaFiltering("firstName.doesNotContain=" + UPDATED_FIRST_NAME, "firstName.doesNotContain=" + DEFAULT_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeBetasByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployeeBeta = employeeBetaRepository.saveAndFlush(employeeBeta);

        // Get all the employeeBetaList where lastName equals to
        defaultEmployeeBetaFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeBetasByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployeeBeta = employeeBetaRepository.saveAndFlush(employeeBeta);

        // Get all the employeeBetaList where lastName in
        defaultEmployeeBetaFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeBetasByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployeeBeta = employeeBetaRepository.saveAndFlush(employeeBeta);

        // Get all the employeeBetaList where lastName is not null
        defaultEmployeeBetaFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeBetasByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeBeta = employeeBetaRepository.saveAndFlush(employeeBeta);

        // Get all the employeeBetaList where lastName contains
        defaultEmployeeBetaFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeBetasByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeBeta = employeeBetaRepository.saveAndFlush(employeeBeta);

        // Get all the employeeBetaList where lastName does not contain
        defaultEmployeeBetaFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeBetasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployeeBeta = employeeBetaRepository.saveAndFlush(employeeBeta);

        // Get all the employeeBetaList where email equals to
        defaultEmployeeBetaFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeeBetasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployeeBeta = employeeBetaRepository.saveAndFlush(employeeBeta);

        // Get all the employeeBetaList where email in
        defaultEmployeeBetaFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeeBetasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployeeBeta = employeeBetaRepository.saveAndFlush(employeeBeta);

        // Get all the employeeBetaList where email is not null
        defaultEmployeeBetaFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeBetasByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeBeta = employeeBetaRepository.saveAndFlush(employeeBeta);

        // Get all the employeeBetaList where email contains
        defaultEmployeeBetaFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeeBetasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeBeta = employeeBetaRepository.saveAndFlush(employeeBeta);

        // Get all the employeeBetaList where email does not contain
        defaultEmployeeBetaFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeeBetasByHireDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployeeBeta = employeeBetaRepository.saveAndFlush(employeeBeta);

        // Get all the employeeBetaList where hireDate equals to
        defaultEmployeeBetaFiltering("hireDate.equals=" + DEFAULT_HIRE_DATE, "hireDate.equals=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeeBetasByHireDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployeeBeta = employeeBetaRepository.saveAndFlush(employeeBeta);

        // Get all the employeeBetaList where hireDate in
        defaultEmployeeBetaFiltering("hireDate.in=" + DEFAULT_HIRE_DATE + "," + UPDATED_HIRE_DATE, "hireDate.in=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeeBetasByHireDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployeeBeta = employeeBetaRepository.saveAndFlush(employeeBeta);

        // Get all the employeeBetaList where hireDate is not null
        defaultEmployeeBetaFiltering("hireDate.specified=true", "hireDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeBetasByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployeeBeta = employeeBetaRepository.saveAndFlush(employeeBeta);

        // Get all the employeeBetaList where position equals to
        defaultEmployeeBetaFiltering("position.equals=" + DEFAULT_POSITION, "position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllEmployeeBetasByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployeeBeta = employeeBetaRepository.saveAndFlush(employeeBeta);

        // Get all the employeeBetaList where position in
        defaultEmployeeBetaFiltering("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION, "position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllEmployeeBetasByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployeeBeta = employeeBetaRepository.saveAndFlush(employeeBeta);

        // Get all the employeeBetaList where position is not null
        defaultEmployeeBetaFiltering("position.specified=true", "position.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeBetasByPositionContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeBeta = employeeBetaRepository.saveAndFlush(employeeBeta);

        // Get all the employeeBetaList where position contains
        defaultEmployeeBetaFiltering("position.contains=" + DEFAULT_POSITION, "position.contains=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllEmployeeBetasByPositionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeBeta = employeeBetaRepository.saveAndFlush(employeeBeta);

        // Get all the employeeBetaList where position does not contain
        defaultEmployeeBetaFiltering("position.doesNotContain=" + UPDATED_POSITION, "position.doesNotContain=" + DEFAULT_POSITION);
    }

    @Test
    @Transactional
    void getAllEmployeeBetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            employeeBetaRepository.saveAndFlush(employeeBeta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        employeeBeta.setTenant(tenant);
        employeeBetaRepository.saveAndFlush(employeeBeta);
        Long tenantId = tenant.getId();
        // Get all the employeeBetaList where tenant equals to tenantId
        defaultEmployeeBetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the employeeBetaList where tenant equals to (tenantId + 1)
        defaultEmployeeBetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultEmployeeBetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEmployeeBetaShouldBeFound(shouldBeFound);
        defaultEmployeeBetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmployeeBetaShouldBeFound(String filter) throws Exception {
        restEmployeeBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));

        // Check, that the count call also returns 1
        restEmployeeBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmployeeBetaShouldNotBeFound(String filter) throws Exception {
        restEmployeeBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmployeeBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEmployeeBeta() throws Exception {
        // Get the employeeBeta
        restEmployeeBetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmployeeBeta() throws Exception {
        // Initialize the database
        insertedEmployeeBeta = employeeBetaRepository.saveAndFlush(employeeBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeBeta
        EmployeeBeta updatedEmployeeBeta = employeeBetaRepository.findById(employeeBeta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmployeeBeta are not directly saved in db
        em.detach(updatedEmployeeBeta);
        updatedEmployeeBeta
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);

        restEmployeeBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmployeeBeta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedEmployeeBeta))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmployeeBetaToMatchAllProperties(updatedEmployeeBeta);
    }

    @Test
    @Transactional
    void putNonExistingEmployeeBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeBeta.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeBeta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employeeBeta))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployeeBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeBeta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employeeBeta))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployeeBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeBeta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeBetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeBeta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeBetaWithPatch() throws Exception {
        // Initialize the database
        insertedEmployeeBeta = employeeBetaRepository.saveAndFlush(employeeBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeBeta using partial update
        EmployeeBeta partialUpdatedEmployeeBeta = new EmployeeBeta();
        partialUpdatedEmployeeBeta.setId(employeeBeta.getId());

        partialUpdatedEmployeeBeta.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME).email(UPDATED_EMAIL);

        restEmployeeBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmployeeBeta))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployeeBetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEmployeeBeta, employeeBeta),
            getPersistedEmployeeBeta(employeeBeta)
        );
    }

    @Test
    @Transactional
    void fullUpdateEmployeeBetaWithPatch() throws Exception {
        // Initialize the database
        insertedEmployeeBeta = employeeBetaRepository.saveAndFlush(employeeBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeBeta using partial update
        EmployeeBeta partialUpdatedEmployeeBeta = new EmployeeBeta();
        partialUpdatedEmployeeBeta.setId(employeeBeta.getId());

        partialUpdatedEmployeeBeta
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);

        restEmployeeBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmployeeBeta))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployeeBetaUpdatableFieldsEquals(partialUpdatedEmployeeBeta, getPersistedEmployeeBeta(partialUpdatedEmployeeBeta));
    }

    @Test
    @Transactional
    void patchNonExistingEmployeeBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeBeta.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeeBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employeeBeta))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployeeBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeBeta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employeeBeta))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployeeBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeBeta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeBetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(employeeBeta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployeeBeta() throws Exception {
        // Initialize the database
        insertedEmployeeBeta = employeeBetaRepository.saveAndFlush(employeeBeta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the employeeBeta
        restEmployeeBetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, employeeBeta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return employeeBetaRepository.count();
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

    protected EmployeeBeta getPersistedEmployeeBeta(EmployeeBeta employeeBeta) {
        return employeeBetaRepository.findById(employeeBeta.getId()).orElseThrow();
    }

    protected void assertPersistedEmployeeBetaToMatchAllProperties(EmployeeBeta expectedEmployeeBeta) {
        assertEmployeeBetaAllPropertiesEquals(expectedEmployeeBeta, getPersistedEmployeeBeta(expectedEmployeeBeta));
    }

    protected void assertPersistedEmployeeBetaToMatchUpdatableProperties(EmployeeBeta expectedEmployeeBeta) {
        assertEmployeeBetaAllUpdatablePropertiesEquals(expectedEmployeeBeta, getPersistedEmployeeBeta(expectedEmployeeBeta));
    }
}
