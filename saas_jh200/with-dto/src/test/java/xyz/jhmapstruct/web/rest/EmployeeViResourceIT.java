package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.EmployeeViAsserts.*;
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
import xyz.jhmapstruct.domain.EmployeeVi;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.repository.EmployeeViRepository;
import xyz.jhmapstruct.service.dto.EmployeeViDTO;
import xyz.jhmapstruct.service.mapper.EmployeeViMapper;

/**
 * Integration tests for the {@link EmployeeViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmployeeViResourceIT {

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

    private static final String ENTITY_API_URL = "/api/employee-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmployeeViRepository employeeViRepository;

    @Autowired
    private EmployeeViMapper employeeViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeViMockMvc;

    private EmployeeVi employeeVi;

    private EmployeeVi insertedEmployeeVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeVi createEntity() {
        return new EmployeeVi()
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
    public static EmployeeVi createUpdatedEntity() {
        return new EmployeeVi()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);
    }

    @BeforeEach
    public void initTest() {
        employeeVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedEmployeeVi != null) {
            employeeViRepository.delete(insertedEmployeeVi);
            insertedEmployeeVi = null;
        }
    }

    @Test
    @Transactional
    void createEmployeeVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EmployeeVi
        EmployeeViDTO employeeViDTO = employeeViMapper.toDto(employeeVi);
        var returnedEmployeeViDTO = om.readValue(
            restEmployeeViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EmployeeViDTO.class
        );

        // Validate the EmployeeVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEmployeeVi = employeeViMapper.toEntity(returnedEmployeeViDTO);
        assertEmployeeViUpdatableFieldsEquals(returnedEmployeeVi, getPersistedEmployeeVi(returnedEmployeeVi));

        insertedEmployeeVi = returnedEmployeeVi;
    }

    @Test
    @Transactional
    void createEmployeeViWithExistingId() throws Exception {
        // Create the EmployeeVi with an existing ID
        employeeVi.setId(1L);
        EmployeeViDTO employeeViDTO = employeeViMapper.toDto(employeeVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EmployeeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employeeVi.setFirstName(null);

        // Create the EmployeeVi, which fails.
        EmployeeViDTO employeeViDTO = employeeViMapper.toDto(employeeVi);

        restEmployeeViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employeeVi.setLastName(null);

        // Create the EmployeeVi, which fails.
        EmployeeViDTO employeeViDTO = employeeViMapper.toDto(employeeVi);

        restEmployeeViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employeeVi.setEmail(null);

        // Create the EmployeeVi, which fails.
        EmployeeViDTO employeeViDTO = employeeViMapper.toDto(employeeVi);

        restEmployeeViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmployeeVis() throws Exception {
        // Initialize the database
        insertedEmployeeVi = employeeViRepository.saveAndFlush(employeeVi);

        // Get all the employeeViList
        restEmployeeViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));
    }

    @Test
    @Transactional
    void getEmployeeVi() throws Exception {
        // Initialize the database
        insertedEmployeeVi = employeeViRepository.saveAndFlush(employeeVi);

        // Get the employeeVi
        restEmployeeViMockMvc
            .perform(get(ENTITY_API_URL_ID, employeeVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employeeVi.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.hireDate").value(DEFAULT_HIRE_DATE.toString()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION));
    }

    @Test
    @Transactional
    void getEmployeeVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedEmployeeVi = employeeViRepository.saveAndFlush(employeeVi);

        Long id = employeeVi.getId();

        defaultEmployeeViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEmployeeViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEmployeeViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmployeeVisByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployeeVi = employeeViRepository.saveAndFlush(employeeVi);

        // Get all the employeeViList where firstName equals to
        defaultEmployeeViFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeVisByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployeeVi = employeeViRepository.saveAndFlush(employeeVi);

        // Get all the employeeViList where firstName in
        defaultEmployeeViFiltering("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME, "firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeVisByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployeeVi = employeeViRepository.saveAndFlush(employeeVi);

        // Get all the employeeViList where firstName is not null
        defaultEmployeeViFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeVisByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeVi = employeeViRepository.saveAndFlush(employeeVi);

        // Get all the employeeViList where firstName contains
        defaultEmployeeViFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeVisByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeVi = employeeViRepository.saveAndFlush(employeeVi);

        // Get all the employeeViList where firstName does not contain
        defaultEmployeeViFiltering("firstName.doesNotContain=" + UPDATED_FIRST_NAME, "firstName.doesNotContain=" + DEFAULT_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeVisByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployeeVi = employeeViRepository.saveAndFlush(employeeVi);

        // Get all the employeeViList where lastName equals to
        defaultEmployeeViFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeVisByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployeeVi = employeeViRepository.saveAndFlush(employeeVi);

        // Get all the employeeViList where lastName in
        defaultEmployeeViFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeVisByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployeeVi = employeeViRepository.saveAndFlush(employeeVi);

        // Get all the employeeViList where lastName is not null
        defaultEmployeeViFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeVisByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeVi = employeeViRepository.saveAndFlush(employeeVi);

        // Get all the employeeViList where lastName contains
        defaultEmployeeViFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeVisByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeVi = employeeViRepository.saveAndFlush(employeeVi);

        // Get all the employeeViList where lastName does not contain
        defaultEmployeeViFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeVisByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployeeVi = employeeViRepository.saveAndFlush(employeeVi);

        // Get all the employeeViList where email equals to
        defaultEmployeeViFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeeVisByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployeeVi = employeeViRepository.saveAndFlush(employeeVi);

        // Get all the employeeViList where email in
        defaultEmployeeViFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeeVisByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployeeVi = employeeViRepository.saveAndFlush(employeeVi);

        // Get all the employeeViList where email is not null
        defaultEmployeeViFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeVisByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeVi = employeeViRepository.saveAndFlush(employeeVi);

        // Get all the employeeViList where email contains
        defaultEmployeeViFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeeVisByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeVi = employeeViRepository.saveAndFlush(employeeVi);

        // Get all the employeeViList where email does not contain
        defaultEmployeeViFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeeVisByHireDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployeeVi = employeeViRepository.saveAndFlush(employeeVi);

        // Get all the employeeViList where hireDate equals to
        defaultEmployeeViFiltering("hireDate.equals=" + DEFAULT_HIRE_DATE, "hireDate.equals=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeeVisByHireDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployeeVi = employeeViRepository.saveAndFlush(employeeVi);

        // Get all the employeeViList where hireDate in
        defaultEmployeeViFiltering("hireDate.in=" + DEFAULT_HIRE_DATE + "," + UPDATED_HIRE_DATE, "hireDate.in=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeeVisByHireDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployeeVi = employeeViRepository.saveAndFlush(employeeVi);

        // Get all the employeeViList where hireDate is not null
        defaultEmployeeViFiltering("hireDate.specified=true", "hireDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeVisByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployeeVi = employeeViRepository.saveAndFlush(employeeVi);

        // Get all the employeeViList where position equals to
        defaultEmployeeViFiltering("position.equals=" + DEFAULT_POSITION, "position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllEmployeeVisByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployeeVi = employeeViRepository.saveAndFlush(employeeVi);

        // Get all the employeeViList where position in
        defaultEmployeeViFiltering("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION, "position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllEmployeeVisByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployeeVi = employeeViRepository.saveAndFlush(employeeVi);

        // Get all the employeeViList where position is not null
        defaultEmployeeViFiltering("position.specified=true", "position.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeVisByPositionContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeVi = employeeViRepository.saveAndFlush(employeeVi);

        // Get all the employeeViList where position contains
        defaultEmployeeViFiltering("position.contains=" + DEFAULT_POSITION, "position.contains=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllEmployeeVisByPositionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeVi = employeeViRepository.saveAndFlush(employeeVi);

        // Get all the employeeViList where position does not contain
        defaultEmployeeViFiltering("position.doesNotContain=" + UPDATED_POSITION, "position.doesNotContain=" + DEFAULT_POSITION);
    }

    @Test
    @Transactional
    void getAllEmployeeVisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            employeeViRepository.saveAndFlush(employeeVi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        employeeVi.setTenant(tenant);
        employeeViRepository.saveAndFlush(employeeVi);
        Long tenantId = tenant.getId();
        // Get all the employeeViList where tenant equals to tenantId
        defaultEmployeeViShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the employeeViList where tenant equals to (tenantId + 1)
        defaultEmployeeViShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultEmployeeViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEmployeeViShouldBeFound(shouldBeFound);
        defaultEmployeeViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmployeeViShouldBeFound(String filter) throws Exception {
        restEmployeeViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));

        // Check, that the count call also returns 1
        restEmployeeViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmployeeViShouldNotBeFound(String filter) throws Exception {
        restEmployeeViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmployeeViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEmployeeVi() throws Exception {
        // Get the employeeVi
        restEmployeeViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmployeeVi() throws Exception {
        // Initialize the database
        insertedEmployeeVi = employeeViRepository.saveAndFlush(employeeVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeVi
        EmployeeVi updatedEmployeeVi = employeeViRepository.findById(employeeVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmployeeVi are not directly saved in db
        em.detach(updatedEmployeeVi);
        updatedEmployeeVi
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);
        EmployeeViDTO employeeViDTO = employeeViMapper.toDto(updatedEmployeeVi);

        restEmployeeViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employeeViDTO))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmployeeViToMatchAllProperties(updatedEmployeeVi);
    }

    @Test
    @Transactional
    void putNonExistingEmployeeVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeVi.setId(longCount.incrementAndGet());

        // Create the EmployeeVi
        EmployeeViDTO employeeViDTO = employeeViMapper.toDto(employeeVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employeeViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployeeVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeVi.setId(longCount.incrementAndGet());

        // Create the EmployeeVi
        EmployeeViDTO employeeViDTO = employeeViMapper.toDto(employeeVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employeeViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployeeVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeVi.setId(longCount.incrementAndGet());

        // Create the EmployeeVi
        EmployeeViDTO employeeViDTO = employeeViMapper.toDto(employeeVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeViWithPatch() throws Exception {
        // Initialize the database
        insertedEmployeeVi = employeeViRepository.saveAndFlush(employeeVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeVi using partial update
        EmployeeVi partialUpdatedEmployeeVi = new EmployeeVi();
        partialUpdatedEmployeeVi.setId(employeeVi.getId());

        partialUpdatedEmployeeVi.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME).position(UPDATED_POSITION);

        restEmployeeViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmployeeVi))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployeeViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEmployeeVi, employeeVi),
            getPersistedEmployeeVi(employeeVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateEmployeeViWithPatch() throws Exception {
        // Initialize the database
        insertedEmployeeVi = employeeViRepository.saveAndFlush(employeeVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeVi using partial update
        EmployeeVi partialUpdatedEmployeeVi = new EmployeeVi();
        partialUpdatedEmployeeVi.setId(employeeVi.getId());

        partialUpdatedEmployeeVi
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);

        restEmployeeViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmployeeVi))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployeeViUpdatableFieldsEquals(partialUpdatedEmployeeVi, getPersistedEmployeeVi(partialUpdatedEmployeeVi));
    }

    @Test
    @Transactional
    void patchNonExistingEmployeeVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeVi.setId(longCount.incrementAndGet());

        // Create the EmployeeVi
        EmployeeViDTO employeeViDTO = employeeViMapper.toDto(employeeVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeeViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employeeViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployeeVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeVi.setId(longCount.incrementAndGet());

        // Create the EmployeeVi
        EmployeeViDTO employeeViDTO = employeeViMapper.toDto(employeeVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employeeViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployeeVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeVi.setId(longCount.incrementAndGet());

        // Create the EmployeeVi
        EmployeeViDTO employeeViDTO = employeeViMapper.toDto(employeeVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(employeeViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployeeVi() throws Exception {
        // Initialize the database
        insertedEmployeeVi = employeeViRepository.saveAndFlush(employeeVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the employeeVi
        restEmployeeViMockMvc
            .perform(delete(ENTITY_API_URL_ID, employeeVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return employeeViRepository.count();
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

    protected EmployeeVi getPersistedEmployeeVi(EmployeeVi employeeVi) {
        return employeeViRepository.findById(employeeVi.getId()).orElseThrow();
    }

    protected void assertPersistedEmployeeViToMatchAllProperties(EmployeeVi expectedEmployeeVi) {
        assertEmployeeViAllPropertiesEquals(expectedEmployeeVi, getPersistedEmployeeVi(expectedEmployeeVi));
    }

    protected void assertPersistedEmployeeViToMatchUpdatableProperties(EmployeeVi expectedEmployeeVi) {
        assertEmployeeViAllUpdatablePropertiesEquals(expectedEmployeeVi, getPersistedEmployeeVi(expectedEmployeeVi));
    }
}
