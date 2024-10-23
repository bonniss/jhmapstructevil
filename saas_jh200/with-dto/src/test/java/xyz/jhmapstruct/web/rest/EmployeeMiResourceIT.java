package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.EmployeeMiAsserts.*;
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
import xyz.jhmapstruct.domain.EmployeeMi;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.repository.EmployeeMiRepository;
import xyz.jhmapstruct.service.dto.EmployeeMiDTO;
import xyz.jhmapstruct.service.mapper.EmployeeMiMapper;

/**
 * Integration tests for the {@link EmployeeMiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmployeeMiResourceIT {

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

    private static final String ENTITY_API_URL = "/api/employee-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmployeeMiRepository employeeMiRepository;

    @Autowired
    private EmployeeMiMapper employeeMiMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeMiMockMvc;

    private EmployeeMi employeeMi;

    private EmployeeMi insertedEmployeeMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeMi createEntity() {
        return new EmployeeMi()
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
    public static EmployeeMi createUpdatedEntity() {
        return new EmployeeMi()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);
    }

    @BeforeEach
    public void initTest() {
        employeeMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedEmployeeMi != null) {
            employeeMiRepository.delete(insertedEmployeeMi);
            insertedEmployeeMi = null;
        }
    }

    @Test
    @Transactional
    void createEmployeeMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EmployeeMi
        EmployeeMiDTO employeeMiDTO = employeeMiMapper.toDto(employeeMi);
        var returnedEmployeeMiDTO = om.readValue(
            restEmployeeMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeMiDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EmployeeMiDTO.class
        );

        // Validate the EmployeeMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEmployeeMi = employeeMiMapper.toEntity(returnedEmployeeMiDTO);
        assertEmployeeMiUpdatableFieldsEquals(returnedEmployeeMi, getPersistedEmployeeMi(returnedEmployeeMi));

        insertedEmployeeMi = returnedEmployeeMi;
    }

    @Test
    @Transactional
    void createEmployeeMiWithExistingId() throws Exception {
        // Create the EmployeeMi with an existing ID
        employeeMi.setId(1L);
        EmployeeMiDTO employeeMiDTO = employeeMiMapper.toDto(employeeMi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeMiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EmployeeMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employeeMi.setFirstName(null);

        // Create the EmployeeMi, which fails.
        EmployeeMiDTO employeeMiDTO = employeeMiMapper.toDto(employeeMi);

        restEmployeeMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employeeMi.setLastName(null);

        // Create the EmployeeMi, which fails.
        EmployeeMiDTO employeeMiDTO = employeeMiMapper.toDto(employeeMi);

        restEmployeeMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employeeMi.setEmail(null);

        // Create the EmployeeMi, which fails.
        EmployeeMiDTO employeeMiDTO = employeeMiMapper.toDto(employeeMi);

        restEmployeeMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmployeeMis() throws Exception {
        // Initialize the database
        insertedEmployeeMi = employeeMiRepository.saveAndFlush(employeeMi);

        // Get all the employeeMiList
        restEmployeeMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));
    }

    @Test
    @Transactional
    void getEmployeeMi() throws Exception {
        // Initialize the database
        insertedEmployeeMi = employeeMiRepository.saveAndFlush(employeeMi);

        // Get the employeeMi
        restEmployeeMiMockMvc
            .perform(get(ENTITY_API_URL_ID, employeeMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employeeMi.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.hireDate").value(DEFAULT_HIRE_DATE.toString()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION));
    }

    @Test
    @Transactional
    void getEmployeeMisByIdFiltering() throws Exception {
        // Initialize the database
        insertedEmployeeMi = employeeMiRepository.saveAndFlush(employeeMi);

        Long id = employeeMi.getId();

        defaultEmployeeMiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEmployeeMiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEmployeeMiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmployeeMisByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployeeMi = employeeMiRepository.saveAndFlush(employeeMi);

        // Get all the employeeMiList where firstName equals to
        defaultEmployeeMiFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeMisByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployeeMi = employeeMiRepository.saveAndFlush(employeeMi);

        // Get all the employeeMiList where firstName in
        defaultEmployeeMiFiltering("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME, "firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeMisByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployeeMi = employeeMiRepository.saveAndFlush(employeeMi);

        // Get all the employeeMiList where firstName is not null
        defaultEmployeeMiFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeMisByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeMi = employeeMiRepository.saveAndFlush(employeeMi);

        // Get all the employeeMiList where firstName contains
        defaultEmployeeMiFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeMisByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeMi = employeeMiRepository.saveAndFlush(employeeMi);

        // Get all the employeeMiList where firstName does not contain
        defaultEmployeeMiFiltering("firstName.doesNotContain=" + UPDATED_FIRST_NAME, "firstName.doesNotContain=" + DEFAULT_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeMisByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployeeMi = employeeMiRepository.saveAndFlush(employeeMi);

        // Get all the employeeMiList where lastName equals to
        defaultEmployeeMiFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeMisByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployeeMi = employeeMiRepository.saveAndFlush(employeeMi);

        // Get all the employeeMiList where lastName in
        defaultEmployeeMiFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeMisByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployeeMi = employeeMiRepository.saveAndFlush(employeeMi);

        // Get all the employeeMiList where lastName is not null
        defaultEmployeeMiFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeMisByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeMi = employeeMiRepository.saveAndFlush(employeeMi);

        // Get all the employeeMiList where lastName contains
        defaultEmployeeMiFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeMisByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeMi = employeeMiRepository.saveAndFlush(employeeMi);

        // Get all the employeeMiList where lastName does not contain
        defaultEmployeeMiFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeMisByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployeeMi = employeeMiRepository.saveAndFlush(employeeMi);

        // Get all the employeeMiList where email equals to
        defaultEmployeeMiFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeeMisByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployeeMi = employeeMiRepository.saveAndFlush(employeeMi);

        // Get all the employeeMiList where email in
        defaultEmployeeMiFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeeMisByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployeeMi = employeeMiRepository.saveAndFlush(employeeMi);

        // Get all the employeeMiList where email is not null
        defaultEmployeeMiFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeMisByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeMi = employeeMiRepository.saveAndFlush(employeeMi);

        // Get all the employeeMiList where email contains
        defaultEmployeeMiFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeeMisByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeMi = employeeMiRepository.saveAndFlush(employeeMi);

        // Get all the employeeMiList where email does not contain
        defaultEmployeeMiFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeeMisByHireDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployeeMi = employeeMiRepository.saveAndFlush(employeeMi);

        // Get all the employeeMiList where hireDate equals to
        defaultEmployeeMiFiltering("hireDate.equals=" + DEFAULT_HIRE_DATE, "hireDate.equals=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeeMisByHireDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployeeMi = employeeMiRepository.saveAndFlush(employeeMi);

        // Get all the employeeMiList where hireDate in
        defaultEmployeeMiFiltering("hireDate.in=" + DEFAULT_HIRE_DATE + "," + UPDATED_HIRE_DATE, "hireDate.in=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeeMisByHireDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployeeMi = employeeMiRepository.saveAndFlush(employeeMi);

        // Get all the employeeMiList where hireDate is not null
        defaultEmployeeMiFiltering("hireDate.specified=true", "hireDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeMisByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployeeMi = employeeMiRepository.saveAndFlush(employeeMi);

        // Get all the employeeMiList where position equals to
        defaultEmployeeMiFiltering("position.equals=" + DEFAULT_POSITION, "position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllEmployeeMisByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployeeMi = employeeMiRepository.saveAndFlush(employeeMi);

        // Get all the employeeMiList where position in
        defaultEmployeeMiFiltering("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION, "position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllEmployeeMisByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployeeMi = employeeMiRepository.saveAndFlush(employeeMi);

        // Get all the employeeMiList where position is not null
        defaultEmployeeMiFiltering("position.specified=true", "position.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeMisByPositionContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeMi = employeeMiRepository.saveAndFlush(employeeMi);

        // Get all the employeeMiList where position contains
        defaultEmployeeMiFiltering("position.contains=" + DEFAULT_POSITION, "position.contains=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllEmployeeMisByPositionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeMi = employeeMiRepository.saveAndFlush(employeeMi);

        // Get all the employeeMiList where position does not contain
        defaultEmployeeMiFiltering("position.doesNotContain=" + UPDATED_POSITION, "position.doesNotContain=" + DEFAULT_POSITION);
    }

    @Test
    @Transactional
    void getAllEmployeeMisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            employeeMiRepository.saveAndFlush(employeeMi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        employeeMi.setTenant(tenant);
        employeeMiRepository.saveAndFlush(employeeMi);
        Long tenantId = tenant.getId();
        // Get all the employeeMiList where tenant equals to tenantId
        defaultEmployeeMiShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the employeeMiList where tenant equals to (tenantId + 1)
        defaultEmployeeMiShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultEmployeeMiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEmployeeMiShouldBeFound(shouldBeFound);
        defaultEmployeeMiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmployeeMiShouldBeFound(String filter) throws Exception {
        restEmployeeMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));

        // Check, that the count call also returns 1
        restEmployeeMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmployeeMiShouldNotBeFound(String filter) throws Exception {
        restEmployeeMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmployeeMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEmployeeMi() throws Exception {
        // Get the employeeMi
        restEmployeeMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmployeeMi() throws Exception {
        // Initialize the database
        insertedEmployeeMi = employeeMiRepository.saveAndFlush(employeeMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeMi
        EmployeeMi updatedEmployeeMi = employeeMiRepository.findById(employeeMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmployeeMi are not directly saved in db
        em.detach(updatedEmployeeMi);
        updatedEmployeeMi
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);
        EmployeeMiDTO employeeMiDTO = employeeMiMapper.toDto(updatedEmployeeMi);

        restEmployeeMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employeeMiDTO))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmployeeMiToMatchAllProperties(updatedEmployeeMi);
    }

    @Test
    @Transactional
    void putNonExistingEmployeeMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeMi.setId(longCount.incrementAndGet());

        // Create the EmployeeMi
        EmployeeMiDTO employeeMiDTO = employeeMiMapper.toDto(employeeMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employeeMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployeeMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeMi.setId(longCount.incrementAndGet());

        // Create the EmployeeMi
        EmployeeMiDTO employeeMiDTO = employeeMiMapper.toDto(employeeMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employeeMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployeeMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeMi.setId(longCount.incrementAndGet());

        // Create the EmployeeMi
        EmployeeMiDTO employeeMiDTO = employeeMiMapper.toDto(employeeMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeMiWithPatch() throws Exception {
        // Initialize the database
        insertedEmployeeMi = employeeMiRepository.saveAndFlush(employeeMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeMi using partial update
        EmployeeMi partialUpdatedEmployeeMi = new EmployeeMi();
        partialUpdatedEmployeeMi.setId(employeeMi.getId());

        partialUpdatedEmployeeMi.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME).email(UPDATED_EMAIL).hireDate(UPDATED_HIRE_DATE);

        restEmployeeMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmployeeMi))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployeeMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEmployeeMi, employeeMi),
            getPersistedEmployeeMi(employeeMi)
        );
    }

    @Test
    @Transactional
    void fullUpdateEmployeeMiWithPatch() throws Exception {
        // Initialize the database
        insertedEmployeeMi = employeeMiRepository.saveAndFlush(employeeMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeMi using partial update
        EmployeeMi partialUpdatedEmployeeMi = new EmployeeMi();
        partialUpdatedEmployeeMi.setId(employeeMi.getId());

        partialUpdatedEmployeeMi
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);

        restEmployeeMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmployeeMi))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployeeMiUpdatableFieldsEquals(partialUpdatedEmployeeMi, getPersistedEmployeeMi(partialUpdatedEmployeeMi));
    }

    @Test
    @Transactional
    void patchNonExistingEmployeeMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeMi.setId(longCount.incrementAndGet());

        // Create the EmployeeMi
        EmployeeMiDTO employeeMiDTO = employeeMiMapper.toDto(employeeMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeeMiDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employeeMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployeeMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeMi.setId(longCount.incrementAndGet());

        // Create the EmployeeMi
        EmployeeMiDTO employeeMiDTO = employeeMiMapper.toDto(employeeMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employeeMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployeeMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeMi.setId(longCount.incrementAndGet());

        // Create the EmployeeMi
        EmployeeMiDTO employeeMiDTO = employeeMiMapper.toDto(employeeMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(employeeMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployeeMi() throws Exception {
        // Initialize the database
        insertedEmployeeMi = employeeMiRepository.saveAndFlush(employeeMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the employeeMi
        restEmployeeMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, employeeMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return employeeMiRepository.count();
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

    protected EmployeeMi getPersistedEmployeeMi(EmployeeMi employeeMi) {
        return employeeMiRepository.findById(employeeMi.getId()).orElseThrow();
    }

    protected void assertPersistedEmployeeMiToMatchAllProperties(EmployeeMi expectedEmployeeMi) {
        assertEmployeeMiAllPropertiesEquals(expectedEmployeeMi, getPersistedEmployeeMi(expectedEmployeeMi));
    }

    protected void assertPersistedEmployeeMiToMatchUpdatableProperties(EmployeeMi expectedEmployeeMi) {
        assertEmployeeMiAllUpdatablePropertiesEquals(expectedEmployeeMi, getPersistedEmployeeMi(expectedEmployeeMi));
    }
}
