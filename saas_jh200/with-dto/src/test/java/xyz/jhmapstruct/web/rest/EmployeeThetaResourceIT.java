package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.EmployeeThetaAsserts.*;
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
import xyz.jhmapstruct.domain.EmployeeTheta;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.repository.EmployeeThetaRepository;
import xyz.jhmapstruct.service.dto.EmployeeThetaDTO;
import xyz.jhmapstruct.service.mapper.EmployeeThetaMapper;

/**
 * Integration tests for the {@link EmployeeThetaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmployeeThetaResourceIT {

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

    private static final String ENTITY_API_URL = "/api/employee-thetas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmployeeThetaRepository employeeThetaRepository;

    @Autowired
    private EmployeeThetaMapper employeeThetaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeThetaMockMvc;

    private EmployeeTheta employeeTheta;

    private EmployeeTheta insertedEmployeeTheta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeTheta createEntity() {
        return new EmployeeTheta()
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
    public static EmployeeTheta createUpdatedEntity() {
        return new EmployeeTheta()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);
    }

    @BeforeEach
    public void initTest() {
        employeeTheta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedEmployeeTheta != null) {
            employeeThetaRepository.delete(insertedEmployeeTheta);
            insertedEmployeeTheta = null;
        }
    }

    @Test
    @Transactional
    void createEmployeeTheta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EmployeeTheta
        EmployeeThetaDTO employeeThetaDTO = employeeThetaMapper.toDto(employeeTheta);
        var returnedEmployeeThetaDTO = om.readValue(
            restEmployeeThetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeThetaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EmployeeThetaDTO.class
        );

        // Validate the EmployeeTheta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEmployeeTheta = employeeThetaMapper.toEntity(returnedEmployeeThetaDTO);
        assertEmployeeThetaUpdatableFieldsEquals(returnedEmployeeTheta, getPersistedEmployeeTheta(returnedEmployeeTheta));

        insertedEmployeeTheta = returnedEmployeeTheta;
    }

    @Test
    @Transactional
    void createEmployeeThetaWithExistingId() throws Exception {
        // Create the EmployeeTheta with an existing ID
        employeeTheta.setId(1L);
        EmployeeThetaDTO employeeThetaDTO = employeeThetaMapper.toDto(employeeTheta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeThetaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EmployeeTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employeeTheta.setFirstName(null);

        // Create the EmployeeTheta, which fails.
        EmployeeThetaDTO employeeThetaDTO = employeeThetaMapper.toDto(employeeTheta);

        restEmployeeThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeThetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employeeTheta.setLastName(null);

        // Create the EmployeeTheta, which fails.
        EmployeeThetaDTO employeeThetaDTO = employeeThetaMapper.toDto(employeeTheta);

        restEmployeeThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeThetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employeeTheta.setEmail(null);

        // Create the EmployeeTheta, which fails.
        EmployeeThetaDTO employeeThetaDTO = employeeThetaMapper.toDto(employeeTheta);

        restEmployeeThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeThetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmployeeThetas() throws Exception {
        // Initialize the database
        insertedEmployeeTheta = employeeThetaRepository.saveAndFlush(employeeTheta);

        // Get all the employeeThetaList
        restEmployeeThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));
    }

    @Test
    @Transactional
    void getEmployeeTheta() throws Exception {
        // Initialize the database
        insertedEmployeeTheta = employeeThetaRepository.saveAndFlush(employeeTheta);

        // Get the employeeTheta
        restEmployeeThetaMockMvc
            .perform(get(ENTITY_API_URL_ID, employeeTheta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employeeTheta.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.hireDate").value(DEFAULT_HIRE_DATE.toString()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION));
    }

    @Test
    @Transactional
    void getEmployeeThetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedEmployeeTheta = employeeThetaRepository.saveAndFlush(employeeTheta);

        Long id = employeeTheta.getId();

        defaultEmployeeThetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEmployeeThetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEmployeeThetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmployeeThetasByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployeeTheta = employeeThetaRepository.saveAndFlush(employeeTheta);

        // Get all the employeeThetaList where firstName equals to
        defaultEmployeeThetaFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeThetasByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployeeTheta = employeeThetaRepository.saveAndFlush(employeeTheta);

        // Get all the employeeThetaList where firstName in
        defaultEmployeeThetaFiltering(
            "firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME,
            "firstName.in=" + UPDATED_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllEmployeeThetasByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployeeTheta = employeeThetaRepository.saveAndFlush(employeeTheta);

        // Get all the employeeThetaList where firstName is not null
        defaultEmployeeThetaFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeThetasByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeTheta = employeeThetaRepository.saveAndFlush(employeeTheta);

        // Get all the employeeThetaList where firstName contains
        defaultEmployeeThetaFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeThetasByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeTheta = employeeThetaRepository.saveAndFlush(employeeTheta);

        // Get all the employeeThetaList where firstName does not contain
        defaultEmployeeThetaFiltering("firstName.doesNotContain=" + UPDATED_FIRST_NAME, "firstName.doesNotContain=" + DEFAULT_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeThetasByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployeeTheta = employeeThetaRepository.saveAndFlush(employeeTheta);

        // Get all the employeeThetaList where lastName equals to
        defaultEmployeeThetaFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeThetasByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployeeTheta = employeeThetaRepository.saveAndFlush(employeeTheta);

        // Get all the employeeThetaList where lastName in
        defaultEmployeeThetaFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeThetasByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployeeTheta = employeeThetaRepository.saveAndFlush(employeeTheta);

        // Get all the employeeThetaList where lastName is not null
        defaultEmployeeThetaFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeThetasByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeTheta = employeeThetaRepository.saveAndFlush(employeeTheta);

        // Get all the employeeThetaList where lastName contains
        defaultEmployeeThetaFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeThetasByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeTheta = employeeThetaRepository.saveAndFlush(employeeTheta);

        // Get all the employeeThetaList where lastName does not contain
        defaultEmployeeThetaFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeThetasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployeeTheta = employeeThetaRepository.saveAndFlush(employeeTheta);

        // Get all the employeeThetaList where email equals to
        defaultEmployeeThetaFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeeThetasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployeeTheta = employeeThetaRepository.saveAndFlush(employeeTheta);

        // Get all the employeeThetaList where email in
        defaultEmployeeThetaFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeeThetasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployeeTheta = employeeThetaRepository.saveAndFlush(employeeTheta);

        // Get all the employeeThetaList where email is not null
        defaultEmployeeThetaFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeThetasByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeTheta = employeeThetaRepository.saveAndFlush(employeeTheta);

        // Get all the employeeThetaList where email contains
        defaultEmployeeThetaFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeeThetasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeTheta = employeeThetaRepository.saveAndFlush(employeeTheta);

        // Get all the employeeThetaList where email does not contain
        defaultEmployeeThetaFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeeThetasByHireDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployeeTheta = employeeThetaRepository.saveAndFlush(employeeTheta);

        // Get all the employeeThetaList where hireDate equals to
        defaultEmployeeThetaFiltering("hireDate.equals=" + DEFAULT_HIRE_DATE, "hireDate.equals=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeeThetasByHireDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployeeTheta = employeeThetaRepository.saveAndFlush(employeeTheta);

        // Get all the employeeThetaList where hireDate in
        defaultEmployeeThetaFiltering("hireDate.in=" + DEFAULT_HIRE_DATE + "," + UPDATED_HIRE_DATE, "hireDate.in=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeeThetasByHireDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployeeTheta = employeeThetaRepository.saveAndFlush(employeeTheta);

        // Get all the employeeThetaList where hireDate is not null
        defaultEmployeeThetaFiltering("hireDate.specified=true", "hireDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeThetasByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployeeTheta = employeeThetaRepository.saveAndFlush(employeeTheta);

        // Get all the employeeThetaList where position equals to
        defaultEmployeeThetaFiltering("position.equals=" + DEFAULT_POSITION, "position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllEmployeeThetasByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployeeTheta = employeeThetaRepository.saveAndFlush(employeeTheta);

        // Get all the employeeThetaList where position in
        defaultEmployeeThetaFiltering("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION, "position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllEmployeeThetasByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployeeTheta = employeeThetaRepository.saveAndFlush(employeeTheta);

        // Get all the employeeThetaList where position is not null
        defaultEmployeeThetaFiltering("position.specified=true", "position.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeThetasByPositionContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeTheta = employeeThetaRepository.saveAndFlush(employeeTheta);

        // Get all the employeeThetaList where position contains
        defaultEmployeeThetaFiltering("position.contains=" + DEFAULT_POSITION, "position.contains=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllEmployeeThetasByPositionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeTheta = employeeThetaRepository.saveAndFlush(employeeTheta);

        // Get all the employeeThetaList where position does not contain
        defaultEmployeeThetaFiltering("position.doesNotContain=" + UPDATED_POSITION, "position.doesNotContain=" + DEFAULT_POSITION);
    }

    @Test
    @Transactional
    void getAllEmployeeThetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            employeeThetaRepository.saveAndFlush(employeeTheta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        employeeTheta.setTenant(tenant);
        employeeThetaRepository.saveAndFlush(employeeTheta);
        Long tenantId = tenant.getId();
        // Get all the employeeThetaList where tenant equals to tenantId
        defaultEmployeeThetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the employeeThetaList where tenant equals to (tenantId + 1)
        defaultEmployeeThetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultEmployeeThetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEmployeeThetaShouldBeFound(shouldBeFound);
        defaultEmployeeThetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmployeeThetaShouldBeFound(String filter) throws Exception {
        restEmployeeThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));

        // Check, that the count call also returns 1
        restEmployeeThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmployeeThetaShouldNotBeFound(String filter) throws Exception {
        restEmployeeThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmployeeThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEmployeeTheta() throws Exception {
        // Get the employeeTheta
        restEmployeeThetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmployeeTheta() throws Exception {
        // Initialize the database
        insertedEmployeeTheta = employeeThetaRepository.saveAndFlush(employeeTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeTheta
        EmployeeTheta updatedEmployeeTheta = employeeThetaRepository.findById(employeeTheta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmployeeTheta are not directly saved in db
        em.detach(updatedEmployeeTheta);
        updatedEmployeeTheta
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);
        EmployeeThetaDTO employeeThetaDTO = employeeThetaMapper.toDto(updatedEmployeeTheta);

        restEmployeeThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeThetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employeeThetaDTO))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmployeeThetaToMatchAllProperties(updatedEmployeeTheta);
    }

    @Test
    @Transactional
    void putNonExistingEmployeeTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeTheta.setId(longCount.incrementAndGet());

        // Create the EmployeeTheta
        EmployeeThetaDTO employeeThetaDTO = employeeThetaMapper.toDto(employeeTheta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeThetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employeeThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployeeTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeTheta.setId(longCount.incrementAndGet());

        // Create the EmployeeTheta
        EmployeeThetaDTO employeeThetaDTO = employeeThetaMapper.toDto(employeeTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employeeThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployeeTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeTheta.setId(longCount.incrementAndGet());

        // Create the EmployeeTheta
        EmployeeThetaDTO employeeThetaDTO = employeeThetaMapper.toDto(employeeTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeThetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeThetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeThetaWithPatch() throws Exception {
        // Initialize the database
        insertedEmployeeTheta = employeeThetaRepository.saveAndFlush(employeeTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeTheta using partial update
        EmployeeTheta partialUpdatedEmployeeTheta = new EmployeeTheta();
        partialUpdatedEmployeeTheta.setId(employeeTheta.getId());

        partialUpdatedEmployeeTheta.firstName(UPDATED_FIRST_NAME).position(UPDATED_POSITION);

        restEmployeeThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmployeeTheta))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployeeThetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEmployeeTheta, employeeTheta),
            getPersistedEmployeeTheta(employeeTheta)
        );
    }

    @Test
    @Transactional
    void fullUpdateEmployeeThetaWithPatch() throws Exception {
        // Initialize the database
        insertedEmployeeTheta = employeeThetaRepository.saveAndFlush(employeeTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeTheta using partial update
        EmployeeTheta partialUpdatedEmployeeTheta = new EmployeeTheta();
        partialUpdatedEmployeeTheta.setId(employeeTheta.getId());

        partialUpdatedEmployeeTheta
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);

        restEmployeeThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmployeeTheta))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployeeThetaUpdatableFieldsEquals(partialUpdatedEmployeeTheta, getPersistedEmployeeTheta(partialUpdatedEmployeeTheta));
    }

    @Test
    @Transactional
    void patchNonExistingEmployeeTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeTheta.setId(longCount.incrementAndGet());

        // Create the EmployeeTheta
        EmployeeThetaDTO employeeThetaDTO = employeeThetaMapper.toDto(employeeTheta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeeThetaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employeeThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployeeTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeTheta.setId(longCount.incrementAndGet());

        // Create the EmployeeTheta
        EmployeeThetaDTO employeeThetaDTO = employeeThetaMapper.toDto(employeeTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employeeThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployeeTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeTheta.setId(longCount.incrementAndGet());

        // Create the EmployeeTheta
        EmployeeThetaDTO employeeThetaDTO = employeeThetaMapper.toDto(employeeTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeThetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(employeeThetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployeeTheta() throws Exception {
        // Initialize the database
        insertedEmployeeTheta = employeeThetaRepository.saveAndFlush(employeeTheta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the employeeTheta
        restEmployeeThetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, employeeTheta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return employeeThetaRepository.count();
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

    protected EmployeeTheta getPersistedEmployeeTheta(EmployeeTheta employeeTheta) {
        return employeeThetaRepository.findById(employeeTheta.getId()).orElseThrow();
    }

    protected void assertPersistedEmployeeThetaToMatchAllProperties(EmployeeTheta expectedEmployeeTheta) {
        assertEmployeeThetaAllPropertiesEquals(expectedEmployeeTheta, getPersistedEmployeeTheta(expectedEmployeeTheta));
    }

    protected void assertPersistedEmployeeThetaToMatchUpdatableProperties(EmployeeTheta expectedEmployeeTheta) {
        assertEmployeeThetaAllUpdatablePropertiesEquals(expectedEmployeeTheta, getPersistedEmployeeTheta(expectedEmployeeTheta));
    }
}
