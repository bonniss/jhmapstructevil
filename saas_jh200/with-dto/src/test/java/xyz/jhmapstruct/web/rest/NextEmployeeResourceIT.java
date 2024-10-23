package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextEmployeeAsserts.*;
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
import xyz.jhmapstruct.domain.NextEmployee;
import xyz.jhmapstruct.repository.NextEmployeeRepository;
import xyz.jhmapstruct.service.dto.NextEmployeeDTO;
import xyz.jhmapstruct.service.mapper.NextEmployeeMapper;

/**
 * Integration tests for the {@link NextEmployeeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextEmployeeResourceIT {

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

    private static final String ENTITY_API_URL = "/api/next-employees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextEmployeeRepository nextEmployeeRepository;

    @Autowired
    private NextEmployeeMapper nextEmployeeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextEmployeeMockMvc;

    private NextEmployee nextEmployee;

    private NextEmployee insertedNextEmployee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextEmployee createEntity() {
        return new NextEmployee()
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
    public static NextEmployee createUpdatedEntity() {
        return new NextEmployee()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);
    }

    @BeforeEach
    public void initTest() {
        nextEmployee = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextEmployee != null) {
            nextEmployeeRepository.delete(insertedNextEmployee);
            insertedNextEmployee = null;
        }
    }

    @Test
    @Transactional
    void createNextEmployee() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextEmployee
        NextEmployeeDTO nextEmployeeDTO = nextEmployeeMapper.toDto(nextEmployee);
        var returnedNextEmployeeDTO = om.readValue(
            restNextEmployeeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextEmployeeDTO.class
        );

        // Validate the NextEmployee in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextEmployee = nextEmployeeMapper.toEntity(returnedNextEmployeeDTO);
        assertNextEmployeeUpdatableFieldsEquals(returnedNextEmployee, getPersistedNextEmployee(returnedNextEmployee));

        insertedNextEmployee = returnedNextEmployee;
    }

    @Test
    @Transactional
    void createNextEmployeeWithExistingId() throws Exception {
        // Create the NextEmployee with an existing ID
        nextEmployee.setId(1L);
        NextEmployeeDTO nextEmployeeDTO = nextEmployeeMapper.toDto(nextEmployee);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextEmployee in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextEmployee.setFirstName(null);

        // Create the NextEmployee, which fails.
        NextEmployeeDTO nextEmployeeDTO = nextEmployeeMapper.toDto(nextEmployee);

        restNextEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextEmployee.setLastName(null);

        // Create the NextEmployee, which fails.
        NextEmployeeDTO nextEmployeeDTO = nextEmployeeMapper.toDto(nextEmployee);

        restNextEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextEmployee.setEmail(null);

        // Create the NextEmployee, which fails.
        NextEmployeeDTO nextEmployeeDTO = nextEmployeeMapper.toDto(nextEmployee);

        restNextEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextEmployees() throws Exception {
        // Initialize the database
        insertedNextEmployee = nextEmployeeRepository.saveAndFlush(nextEmployee);

        // Get all the nextEmployeeList
        restNextEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextEmployee.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));
    }

    @Test
    @Transactional
    void getNextEmployee() throws Exception {
        // Initialize the database
        insertedNextEmployee = nextEmployeeRepository.saveAndFlush(nextEmployee);

        // Get the nextEmployee
        restNextEmployeeMockMvc
            .perform(get(ENTITY_API_URL_ID, nextEmployee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextEmployee.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.hireDate").value(DEFAULT_HIRE_DATE.toString()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION));
    }

    @Test
    @Transactional
    void getNextEmployeesByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextEmployee = nextEmployeeRepository.saveAndFlush(nextEmployee);

        Long id = nextEmployee.getId();

        defaultNextEmployeeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextEmployeeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextEmployeeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextEmployeesByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployee = nextEmployeeRepository.saveAndFlush(nextEmployee);

        // Get all the nextEmployeeList where firstName equals to
        defaultNextEmployeeFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeesByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployee = nextEmployeeRepository.saveAndFlush(nextEmployee);

        // Get all the nextEmployeeList where firstName in
        defaultNextEmployeeFiltering("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME, "firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeesByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployee = nextEmployeeRepository.saveAndFlush(nextEmployee);

        // Get all the nextEmployeeList where firstName is not null
        defaultNextEmployeeFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeesByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployee = nextEmployeeRepository.saveAndFlush(nextEmployee);

        // Get all the nextEmployeeList where firstName contains
        defaultNextEmployeeFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeesByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployee = nextEmployeeRepository.saveAndFlush(nextEmployee);

        // Get all the nextEmployeeList where firstName does not contain
        defaultNextEmployeeFiltering("firstName.doesNotContain=" + UPDATED_FIRST_NAME, "firstName.doesNotContain=" + DEFAULT_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeesByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployee = nextEmployeeRepository.saveAndFlush(nextEmployee);

        // Get all the nextEmployeeList where lastName equals to
        defaultNextEmployeeFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeesByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployee = nextEmployeeRepository.saveAndFlush(nextEmployee);

        // Get all the nextEmployeeList where lastName in
        defaultNextEmployeeFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeesByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployee = nextEmployeeRepository.saveAndFlush(nextEmployee);

        // Get all the nextEmployeeList where lastName is not null
        defaultNextEmployeeFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeesByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployee = nextEmployeeRepository.saveAndFlush(nextEmployee);

        // Get all the nextEmployeeList where lastName contains
        defaultNextEmployeeFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeesByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployee = nextEmployeeRepository.saveAndFlush(nextEmployee);

        // Get all the nextEmployeeList where lastName does not contain
        defaultNextEmployeeFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployee = nextEmployeeRepository.saveAndFlush(nextEmployee);

        // Get all the nextEmployeeList where email equals to
        defaultNextEmployeeFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployee = nextEmployeeRepository.saveAndFlush(nextEmployee);

        // Get all the nextEmployeeList where email in
        defaultNextEmployeeFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployee = nextEmployeeRepository.saveAndFlush(nextEmployee);

        // Get all the nextEmployeeList where email is not null
        defaultNextEmployeeFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeesByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployee = nextEmployeeRepository.saveAndFlush(nextEmployee);

        // Get all the nextEmployeeList where email contains
        defaultNextEmployeeFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeesByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployee = nextEmployeeRepository.saveAndFlush(nextEmployee);

        // Get all the nextEmployeeList where email does not contain
        defaultNextEmployeeFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeesByHireDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployee = nextEmployeeRepository.saveAndFlush(nextEmployee);

        // Get all the nextEmployeeList where hireDate equals to
        defaultNextEmployeeFiltering("hireDate.equals=" + DEFAULT_HIRE_DATE, "hireDate.equals=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllNextEmployeesByHireDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployee = nextEmployeeRepository.saveAndFlush(nextEmployee);

        // Get all the nextEmployeeList where hireDate in
        defaultNextEmployeeFiltering("hireDate.in=" + DEFAULT_HIRE_DATE + "," + UPDATED_HIRE_DATE, "hireDate.in=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllNextEmployeesByHireDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployee = nextEmployeeRepository.saveAndFlush(nextEmployee);

        // Get all the nextEmployeeList where hireDate is not null
        defaultNextEmployeeFiltering("hireDate.specified=true", "hireDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeesByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployee = nextEmployeeRepository.saveAndFlush(nextEmployee);

        // Get all the nextEmployeeList where position equals to
        defaultNextEmployeeFiltering("position.equals=" + DEFAULT_POSITION, "position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeesByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployee = nextEmployeeRepository.saveAndFlush(nextEmployee);

        // Get all the nextEmployeeList where position in
        defaultNextEmployeeFiltering("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION, "position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeesByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployee = nextEmployeeRepository.saveAndFlush(nextEmployee);

        // Get all the nextEmployeeList where position is not null
        defaultNextEmployeeFiltering("position.specified=true", "position.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeesByPositionContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployee = nextEmployeeRepository.saveAndFlush(nextEmployee);

        // Get all the nextEmployeeList where position contains
        defaultNextEmployeeFiltering("position.contains=" + DEFAULT_POSITION, "position.contains=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeesByPositionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployee = nextEmployeeRepository.saveAndFlush(nextEmployee);

        // Get all the nextEmployeeList where position does not contain
        defaultNextEmployeeFiltering("position.doesNotContain=" + UPDATED_POSITION, "position.doesNotContain=" + DEFAULT_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeesByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextEmployeeRepository.saveAndFlush(nextEmployee);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextEmployee.setTenant(tenant);
        nextEmployeeRepository.saveAndFlush(nextEmployee);
        Long tenantId = tenant.getId();
        // Get all the nextEmployeeList where tenant equals to tenantId
        defaultNextEmployeeShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextEmployeeList where tenant equals to (tenantId + 1)
        defaultNextEmployeeShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextEmployeeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextEmployeeShouldBeFound(shouldBeFound);
        defaultNextEmployeeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextEmployeeShouldBeFound(String filter) throws Exception {
        restNextEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextEmployee.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));

        // Check, that the count call also returns 1
        restNextEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextEmployeeShouldNotBeFound(String filter) throws Exception {
        restNextEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextEmployee() throws Exception {
        // Get the nextEmployee
        restNextEmployeeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextEmployee() throws Exception {
        // Initialize the database
        insertedNextEmployee = nextEmployeeRepository.saveAndFlush(nextEmployee);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextEmployee
        NextEmployee updatedNextEmployee = nextEmployeeRepository.findById(nextEmployee.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextEmployee are not directly saved in db
        em.detach(updatedNextEmployee);
        updatedNextEmployee
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);
        NextEmployeeDTO nextEmployeeDTO = nextEmployeeMapper.toDto(updatedNextEmployee);

        restNextEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextEmployeeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextEmployeeDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextEmployee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextEmployeeToMatchAllProperties(updatedNextEmployee);
    }

    @Test
    @Transactional
    void putNonExistingNextEmployee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployee.setId(longCount.incrementAndGet());

        // Create the NextEmployee
        NextEmployeeDTO nextEmployeeDTO = nextEmployeeMapper.toDto(nextEmployee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextEmployeeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextEmployeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextEmployee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployee.setId(longCount.incrementAndGet());

        // Create the NextEmployee
        NextEmployeeDTO nextEmployeeDTO = nextEmployeeMapper.toDto(nextEmployee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextEmployeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextEmployee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployee.setId(longCount.incrementAndGet());

        // Create the NextEmployee
        NextEmployeeDTO nextEmployeeDTO = nextEmployeeMapper.toDto(nextEmployee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextEmployee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextEmployeeWithPatch() throws Exception {
        // Initialize the database
        insertedNextEmployee = nextEmployeeRepository.saveAndFlush(nextEmployee);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextEmployee using partial update
        NextEmployee partialUpdatedNextEmployee = new NextEmployee();
        partialUpdatedNextEmployee.setId(nextEmployee.getId());

        partialUpdatedNextEmployee.email(UPDATED_EMAIL).hireDate(UPDATED_HIRE_DATE);

        restNextEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextEmployee.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextEmployee))
            )
            .andExpect(status().isOk());

        // Validate the NextEmployee in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextEmployeeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextEmployee, nextEmployee),
            getPersistedNextEmployee(nextEmployee)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextEmployeeWithPatch() throws Exception {
        // Initialize the database
        insertedNextEmployee = nextEmployeeRepository.saveAndFlush(nextEmployee);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextEmployee using partial update
        NextEmployee partialUpdatedNextEmployee = new NextEmployee();
        partialUpdatedNextEmployee.setId(nextEmployee.getId());

        partialUpdatedNextEmployee
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);

        restNextEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextEmployee.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextEmployee))
            )
            .andExpect(status().isOk());

        // Validate the NextEmployee in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextEmployeeUpdatableFieldsEquals(partialUpdatedNextEmployee, getPersistedNextEmployee(partialUpdatedNextEmployee));
    }

    @Test
    @Transactional
    void patchNonExistingNextEmployee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployee.setId(longCount.incrementAndGet());

        // Create the NextEmployee
        NextEmployeeDTO nextEmployeeDTO = nextEmployeeMapper.toDto(nextEmployee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextEmployeeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextEmployeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextEmployee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployee.setId(longCount.incrementAndGet());

        // Create the NextEmployee
        NextEmployeeDTO nextEmployeeDTO = nextEmployeeMapper.toDto(nextEmployee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextEmployeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextEmployee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployee.setId(longCount.incrementAndGet());

        // Create the NextEmployee
        NextEmployeeDTO nextEmployeeDTO = nextEmployeeMapper.toDto(nextEmployee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextEmployeeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextEmployee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextEmployee() throws Exception {
        // Initialize the database
        insertedNextEmployee = nextEmployeeRepository.saveAndFlush(nextEmployee);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextEmployee
        restNextEmployeeMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextEmployee.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextEmployeeRepository.count();
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

    protected NextEmployee getPersistedNextEmployee(NextEmployee nextEmployee) {
        return nextEmployeeRepository.findById(nextEmployee.getId()).orElseThrow();
    }

    protected void assertPersistedNextEmployeeToMatchAllProperties(NextEmployee expectedNextEmployee) {
        assertNextEmployeeAllPropertiesEquals(expectedNextEmployee, getPersistedNextEmployee(expectedNextEmployee));
    }

    protected void assertPersistedNextEmployeeToMatchUpdatableProperties(NextEmployee expectedNextEmployee) {
        assertNextEmployeeAllUpdatablePropertiesEquals(expectedNextEmployee, getPersistedNextEmployee(expectedNextEmployee));
    }
}
