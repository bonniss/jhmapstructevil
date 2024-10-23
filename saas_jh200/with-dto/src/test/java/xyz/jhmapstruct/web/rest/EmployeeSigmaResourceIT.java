package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.EmployeeSigmaAsserts.*;
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
import xyz.jhmapstruct.domain.EmployeeSigma;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.repository.EmployeeSigmaRepository;
import xyz.jhmapstruct.service.dto.EmployeeSigmaDTO;
import xyz.jhmapstruct.service.mapper.EmployeeSigmaMapper;

/**
 * Integration tests for the {@link EmployeeSigmaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmployeeSigmaResourceIT {

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

    private static final String ENTITY_API_URL = "/api/employee-sigmas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmployeeSigmaRepository employeeSigmaRepository;

    @Autowired
    private EmployeeSigmaMapper employeeSigmaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeSigmaMockMvc;

    private EmployeeSigma employeeSigma;

    private EmployeeSigma insertedEmployeeSigma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeSigma createEntity() {
        return new EmployeeSigma()
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
    public static EmployeeSigma createUpdatedEntity() {
        return new EmployeeSigma()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);
    }

    @BeforeEach
    public void initTest() {
        employeeSigma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedEmployeeSigma != null) {
            employeeSigmaRepository.delete(insertedEmployeeSigma);
            insertedEmployeeSigma = null;
        }
    }

    @Test
    @Transactional
    void createEmployeeSigma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EmployeeSigma
        EmployeeSigmaDTO employeeSigmaDTO = employeeSigmaMapper.toDto(employeeSigma);
        var returnedEmployeeSigmaDTO = om.readValue(
            restEmployeeSigmaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeSigmaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EmployeeSigmaDTO.class
        );

        // Validate the EmployeeSigma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEmployeeSigma = employeeSigmaMapper.toEntity(returnedEmployeeSigmaDTO);
        assertEmployeeSigmaUpdatableFieldsEquals(returnedEmployeeSigma, getPersistedEmployeeSigma(returnedEmployeeSigma));

        insertedEmployeeSigma = returnedEmployeeSigma;
    }

    @Test
    @Transactional
    void createEmployeeSigmaWithExistingId() throws Exception {
        // Create the EmployeeSigma with an existing ID
        employeeSigma.setId(1L);
        EmployeeSigmaDTO employeeSigmaDTO = employeeSigmaMapper.toDto(employeeSigma);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeSigmaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EmployeeSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employeeSigma.setFirstName(null);

        // Create the EmployeeSigma, which fails.
        EmployeeSigmaDTO employeeSigmaDTO = employeeSigmaMapper.toDto(employeeSigma);

        restEmployeeSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeSigmaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employeeSigma.setLastName(null);

        // Create the EmployeeSigma, which fails.
        EmployeeSigmaDTO employeeSigmaDTO = employeeSigmaMapper.toDto(employeeSigma);

        restEmployeeSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeSigmaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employeeSigma.setEmail(null);

        // Create the EmployeeSigma, which fails.
        EmployeeSigmaDTO employeeSigmaDTO = employeeSigmaMapper.toDto(employeeSigma);

        restEmployeeSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeSigmaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmployeeSigmas() throws Exception {
        // Initialize the database
        insertedEmployeeSigma = employeeSigmaRepository.saveAndFlush(employeeSigma);

        // Get all the employeeSigmaList
        restEmployeeSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeSigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));
    }

    @Test
    @Transactional
    void getEmployeeSigma() throws Exception {
        // Initialize the database
        insertedEmployeeSigma = employeeSigmaRepository.saveAndFlush(employeeSigma);

        // Get the employeeSigma
        restEmployeeSigmaMockMvc
            .perform(get(ENTITY_API_URL_ID, employeeSigma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employeeSigma.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.hireDate").value(DEFAULT_HIRE_DATE.toString()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION));
    }

    @Test
    @Transactional
    void getEmployeeSigmasByIdFiltering() throws Exception {
        // Initialize the database
        insertedEmployeeSigma = employeeSigmaRepository.saveAndFlush(employeeSigma);

        Long id = employeeSigma.getId();

        defaultEmployeeSigmaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEmployeeSigmaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEmployeeSigmaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmployeeSigmasByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployeeSigma = employeeSigmaRepository.saveAndFlush(employeeSigma);

        // Get all the employeeSigmaList where firstName equals to
        defaultEmployeeSigmaFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeSigmasByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployeeSigma = employeeSigmaRepository.saveAndFlush(employeeSigma);

        // Get all the employeeSigmaList where firstName in
        defaultEmployeeSigmaFiltering(
            "firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME,
            "firstName.in=" + UPDATED_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllEmployeeSigmasByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployeeSigma = employeeSigmaRepository.saveAndFlush(employeeSigma);

        // Get all the employeeSigmaList where firstName is not null
        defaultEmployeeSigmaFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeSigmasByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeSigma = employeeSigmaRepository.saveAndFlush(employeeSigma);

        // Get all the employeeSigmaList where firstName contains
        defaultEmployeeSigmaFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeSigmasByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeSigma = employeeSigmaRepository.saveAndFlush(employeeSigma);

        // Get all the employeeSigmaList where firstName does not contain
        defaultEmployeeSigmaFiltering("firstName.doesNotContain=" + UPDATED_FIRST_NAME, "firstName.doesNotContain=" + DEFAULT_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeSigmasByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployeeSigma = employeeSigmaRepository.saveAndFlush(employeeSigma);

        // Get all the employeeSigmaList where lastName equals to
        defaultEmployeeSigmaFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeSigmasByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployeeSigma = employeeSigmaRepository.saveAndFlush(employeeSigma);

        // Get all the employeeSigmaList where lastName in
        defaultEmployeeSigmaFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeSigmasByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployeeSigma = employeeSigmaRepository.saveAndFlush(employeeSigma);

        // Get all the employeeSigmaList where lastName is not null
        defaultEmployeeSigmaFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeSigmasByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeSigma = employeeSigmaRepository.saveAndFlush(employeeSigma);

        // Get all the employeeSigmaList where lastName contains
        defaultEmployeeSigmaFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeSigmasByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeSigma = employeeSigmaRepository.saveAndFlush(employeeSigma);

        // Get all the employeeSigmaList where lastName does not contain
        defaultEmployeeSigmaFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeSigmasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployeeSigma = employeeSigmaRepository.saveAndFlush(employeeSigma);

        // Get all the employeeSigmaList where email equals to
        defaultEmployeeSigmaFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeeSigmasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployeeSigma = employeeSigmaRepository.saveAndFlush(employeeSigma);

        // Get all the employeeSigmaList where email in
        defaultEmployeeSigmaFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeeSigmasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployeeSigma = employeeSigmaRepository.saveAndFlush(employeeSigma);

        // Get all the employeeSigmaList where email is not null
        defaultEmployeeSigmaFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeSigmasByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeSigma = employeeSigmaRepository.saveAndFlush(employeeSigma);

        // Get all the employeeSigmaList where email contains
        defaultEmployeeSigmaFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeeSigmasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeSigma = employeeSigmaRepository.saveAndFlush(employeeSigma);

        // Get all the employeeSigmaList where email does not contain
        defaultEmployeeSigmaFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeeSigmasByHireDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployeeSigma = employeeSigmaRepository.saveAndFlush(employeeSigma);

        // Get all the employeeSigmaList where hireDate equals to
        defaultEmployeeSigmaFiltering("hireDate.equals=" + DEFAULT_HIRE_DATE, "hireDate.equals=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeeSigmasByHireDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployeeSigma = employeeSigmaRepository.saveAndFlush(employeeSigma);

        // Get all the employeeSigmaList where hireDate in
        defaultEmployeeSigmaFiltering("hireDate.in=" + DEFAULT_HIRE_DATE + "," + UPDATED_HIRE_DATE, "hireDate.in=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeeSigmasByHireDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployeeSigma = employeeSigmaRepository.saveAndFlush(employeeSigma);

        // Get all the employeeSigmaList where hireDate is not null
        defaultEmployeeSigmaFiltering("hireDate.specified=true", "hireDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeSigmasByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployeeSigma = employeeSigmaRepository.saveAndFlush(employeeSigma);

        // Get all the employeeSigmaList where position equals to
        defaultEmployeeSigmaFiltering("position.equals=" + DEFAULT_POSITION, "position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllEmployeeSigmasByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployeeSigma = employeeSigmaRepository.saveAndFlush(employeeSigma);

        // Get all the employeeSigmaList where position in
        defaultEmployeeSigmaFiltering("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION, "position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllEmployeeSigmasByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployeeSigma = employeeSigmaRepository.saveAndFlush(employeeSigma);

        // Get all the employeeSigmaList where position is not null
        defaultEmployeeSigmaFiltering("position.specified=true", "position.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeSigmasByPositionContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeSigma = employeeSigmaRepository.saveAndFlush(employeeSigma);

        // Get all the employeeSigmaList where position contains
        defaultEmployeeSigmaFiltering("position.contains=" + DEFAULT_POSITION, "position.contains=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllEmployeeSigmasByPositionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeSigma = employeeSigmaRepository.saveAndFlush(employeeSigma);

        // Get all the employeeSigmaList where position does not contain
        defaultEmployeeSigmaFiltering("position.doesNotContain=" + UPDATED_POSITION, "position.doesNotContain=" + DEFAULT_POSITION);
    }

    @Test
    @Transactional
    void getAllEmployeeSigmasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            employeeSigmaRepository.saveAndFlush(employeeSigma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        employeeSigma.setTenant(tenant);
        employeeSigmaRepository.saveAndFlush(employeeSigma);
        Long tenantId = tenant.getId();
        // Get all the employeeSigmaList where tenant equals to tenantId
        defaultEmployeeSigmaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the employeeSigmaList where tenant equals to (tenantId + 1)
        defaultEmployeeSigmaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultEmployeeSigmaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEmployeeSigmaShouldBeFound(shouldBeFound);
        defaultEmployeeSigmaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmployeeSigmaShouldBeFound(String filter) throws Exception {
        restEmployeeSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeSigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));

        // Check, that the count call also returns 1
        restEmployeeSigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmployeeSigmaShouldNotBeFound(String filter) throws Exception {
        restEmployeeSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmployeeSigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEmployeeSigma() throws Exception {
        // Get the employeeSigma
        restEmployeeSigmaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmployeeSigma() throws Exception {
        // Initialize the database
        insertedEmployeeSigma = employeeSigmaRepository.saveAndFlush(employeeSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeSigma
        EmployeeSigma updatedEmployeeSigma = employeeSigmaRepository.findById(employeeSigma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmployeeSigma are not directly saved in db
        em.detach(updatedEmployeeSigma);
        updatedEmployeeSigma
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);
        EmployeeSigmaDTO employeeSigmaDTO = employeeSigmaMapper.toDto(updatedEmployeeSigma);

        restEmployeeSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeSigmaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employeeSigmaDTO))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmployeeSigmaToMatchAllProperties(updatedEmployeeSigma);
    }

    @Test
    @Transactional
    void putNonExistingEmployeeSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeSigma.setId(longCount.incrementAndGet());

        // Create the EmployeeSigma
        EmployeeSigmaDTO employeeSigmaDTO = employeeSigmaMapper.toDto(employeeSigma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeSigmaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employeeSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployeeSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeSigma.setId(longCount.incrementAndGet());

        // Create the EmployeeSigma
        EmployeeSigmaDTO employeeSigmaDTO = employeeSigmaMapper.toDto(employeeSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employeeSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployeeSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeSigma.setId(longCount.incrementAndGet());

        // Create the EmployeeSigma
        EmployeeSigmaDTO employeeSigmaDTO = employeeSigmaMapper.toDto(employeeSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeSigmaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeSigmaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeSigmaWithPatch() throws Exception {
        // Initialize the database
        insertedEmployeeSigma = employeeSigmaRepository.saveAndFlush(employeeSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeSigma using partial update
        EmployeeSigma partialUpdatedEmployeeSigma = new EmployeeSigma();
        partialUpdatedEmployeeSigma.setId(employeeSigma.getId());

        partialUpdatedEmployeeSigma.firstName(UPDATED_FIRST_NAME).position(UPDATED_POSITION);

        restEmployeeSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmployeeSigma))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeSigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployeeSigmaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEmployeeSigma, employeeSigma),
            getPersistedEmployeeSigma(employeeSigma)
        );
    }

    @Test
    @Transactional
    void fullUpdateEmployeeSigmaWithPatch() throws Exception {
        // Initialize the database
        insertedEmployeeSigma = employeeSigmaRepository.saveAndFlush(employeeSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeSigma using partial update
        EmployeeSigma partialUpdatedEmployeeSigma = new EmployeeSigma();
        partialUpdatedEmployeeSigma.setId(employeeSigma.getId());

        partialUpdatedEmployeeSigma
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);

        restEmployeeSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmployeeSigma))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeSigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployeeSigmaUpdatableFieldsEquals(partialUpdatedEmployeeSigma, getPersistedEmployeeSigma(partialUpdatedEmployeeSigma));
    }

    @Test
    @Transactional
    void patchNonExistingEmployeeSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeSigma.setId(longCount.incrementAndGet());

        // Create the EmployeeSigma
        EmployeeSigmaDTO employeeSigmaDTO = employeeSigmaMapper.toDto(employeeSigma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeeSigmaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employeeSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployeeSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeSigma.setId(longCount.incrementAndGet());

        // Create the EmployeeSigma
        EmployeeSigmaDTO employeeSigmaDTO = employeeSigmaMapper.toDto(employeeSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employeeSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployeeSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeSigma.setId(longCount.incrementAndGet());

        // Create the EmployeeSigma
        EmployeeSigmaDTO employeeSigmaDTO = employeeSigmaMapper.toDto(employeeSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeSigmaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(employeeSigmaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployeeSigma() throws Exception {
        // Initialize the database
        insertedEmployeeSigma = employeeSigmaRepository.saveAndFlush(employeeSigma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the employeeSigma
        restEmployeeSigmaMockMvc
            .perform(delete(ENTITY_API_URL_ID, employeeSigma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return employeeSigmaRepository.count();
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

    protected EmployeeSigma getPersistedEmployeeSigma(EmployeeSigma employeeSigma) {
        return employeeSigmaRepository.findById(employeeSigma.getId()).orElseThrow();
    }

    protected void assertPersistedEmployeeSigmaToMatchAllProperties(EmployeeSigma expectedEmployeeSigma) {
        assertEmployeeSigmaAllPropertiesEquals(expectedEmployeeSigma, getPersistedEmployeeSigma(expectedEmployeeSigma));
    }

    protected void assertPersistedEmployeeSigmaToMatchUpdatableProperties(EmployeeSigma expectedEmployeeSigma) {
        assertEmployeeSigmaAllUpdatablePropertiesEquals(expectedEmployeeSigma, getPersistedEmployeeSigma(expectedEmployeeSigma));
    }
}
