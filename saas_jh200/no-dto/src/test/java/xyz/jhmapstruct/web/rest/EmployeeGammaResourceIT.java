package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.EmployeeGammaAsserts.*;
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
import xyz.jhmapstruct.domain.EmployeeGamma;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.repository.EmployeeGammaRepository;

/**
 * Integration tests for the {@link EmployeeGammaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmployeeGammaResourceIT {

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

    private static final String ENTITY_API_URL = "/api/employee-gammas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmployeeGammaRepository employeeGammaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeGammaMockMvc;

    private EmployeeGamma employeeGamma;

    private EmployeeGamma insertedEmployeeGamma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeGamma createEntity() {
        return new EmployeeGamma()
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
    public static EmployeeGamma createUpdatedEntity() {
        return new EmployeeGamma()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);
    }

    @BeforeEach
    public void initTest() {
        employeeGamma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedEmployeeGamma != null) {
            employeeGammaRepository.delete(insertedEmployeeGamma);
            insertedEmployeeGamma = null;
        }
    }

    @Test
    @Transactional
    void createEmployeeGamma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EmployeeGamma
        var returnedEmployeeGamma = om.readValue(
            restEmployeeGammaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeGamma)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EmployeeGamma.class
        );

        // Validate the EmployeeGamma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEmployeeGammaUpdatableFieldsEquals(returnedEmployeeGamma, getPersistedEmployeeGamma(returnedEmployeeGamma));

        insertedEmployeeGamma = returnedEmployeeGamma;
    }

    @Test
    @Transactional
    void createEmployeeGammaWithExistingId() throws Exception {
        // Create the EmployeeGamma with an existing ID
        employeeGamma.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeGamma)))
            .andExpect(status().isBadRequest());

        // Validate the EmployeeGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employeeGamma.setFirstName(null);

        // Create the EmployeeGamma, which fails.

        restEmployeeGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeGamma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employeeGamma.setLastName(null);

        // Create the EmployeeGamma, which fails.

        restEmployeeGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeGamma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employeeGamma.setEmail(null);

        // Create the EmployeeGamma, which fails.

        restEmployeeGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeGamma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmployeeGammas() throws Exception {
        // Initialize the database
        insertedEmployeeGamma = employeeGammaRepository.saveAndFlush(employeeGamma);

        // Get all the employeeGammaList
        restEmployeeGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));
    }

    @Test
    @Transactional
    void getEmployeeGamma() throws Exception {
        // Initialize the database
        insertedEmployeeGamma = employeeGammaRepository.saveAndFlush(employeeGamma);

        // Get the employeeGamma
        restEmployeeGammaMockMvc
            .perform(get(ENTITY_API_URL_ID, employeeGamma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employeeGamma.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.hireDate").value(DEFAULT_HIRE_DATE.toString()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION));
    }

    @Test
    @Transactional
    void getEmployeeGammasByIdFiltering() throws Exception {
        // Initialize the database
        insertedEmployeeGamma = employeeGammaRepository.saveAndFlush(employeeGamma);

        Long id = employeeGamma.getId();

        defaultEmployeeGammaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEmployeeGammaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEmployeeGammaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmployeeGammasByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployeeGamma = employeeGammaRepository.saveAndFlush(employeeGamma);

        // Get all the employeeGammaList where firstName equals to
        defaultEmployeeGammaFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeGammasByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployeeGamma = employeeGammaRepository.saveAndFlush(employeeGamma);

        // Get all the employeeGammaList where firstName in
        defaultEmployeeGammaFiltering(
            "firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME,
            "firstName.in=" + UPDATED_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllEmployeeGammasByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployeeGamma = employeeGammaRepository.saveAndFlush(employeeGamma);

        // Get all the employeeGammaList where firstName is not null
        defaultEmployeeGammaFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeGammasByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeGamma = employeeGammaRepository.saveAndFlush(employeeGamma);

        // Get all the employeeGammaList where firstName contains
        defaultEmployeeGammaFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeGammasByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeGamma = employeeGammaRepository.saveAndFlush(employeeGamma);

        // Get all the employeeGammaList where firstName does not contain
        defaultEmployeeGammaFiltering("firstName.doesNotContain=" + UPDATED_FIRST_NAME, "firstName.doesNotContain=" + DEFAULT_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeGammasByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployeeGamma = employeeGammaRepository.saveAndFlush(employeeGamma);

        // Get all the employeeGammaList where lastName equals to
        defaultEmployeeGammaFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeGammasByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployeeGamma = employeeGammaRepository.saveAndFlush(employeeGamma);

        // Get all the employeeGammaList where lastName in
        defaultEmployeeGammaFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeGammasByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployeeGamma = employeeGammaRepository.saveAndFlush(employeeGamma);

        // Get all the employeeGammaList where lastName is not null
        defaultEmployeeGammaFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeGammasByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeGamma = employeeGammaRepository.saveAndFlush(employeeGamma);

        // Get all the employeeGammaList where lastName contains
        defaultEmployeeGammaFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeGammasByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeGamma = employeeGammaRepository.saveAndFlush(employeeGamma);

        // Get all the employeeGammaList where lastName does not contain
        defaultEmployeeGammaFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeGammasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployeeGamma = employeeGammaRepository.saveAndFlush(employeeGamma);

        // Get all the employeeGammaList where email equals to
        defaultEmployeeGammaFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeeGammasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployeeGamma = employeeGammaRepository.saveAndFlush(employeeGamma);

        // Get all the employeeGammaList where email in
        defaultEmployeeGammaFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeeGammasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployeeGamma = employeeGammaRepository.saveAndFlush(employeeGamma);

        // Get all the employeeGammaList where email is not null
        defaultEmployeeGammaFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeGammasByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeGamma = employeeGammaRepository.saveAndFlush(employeeGamma);

        // Get all the employeeGammaList where email contains
        defaultEmployeeGammaFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeeGammasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeGamma = employeeGammaRepository.saveAndFlush(employeeGamma);

        // Get all the employeeGammaList where email does not contain
        defaultEmployeeGammaFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeeGammasByHireDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployeeGamma = employeeGammaRepository.saveAndFlush(employeeGamma);

        // Get all the employeeGammaList where hireDate equals to
        defaultEmployeeGammaFiltering("hireDate.equals=" + DEFAULT_HIRE_DATE, "hireDate.equals=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeeGammasByHireDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployeeGamma = employeeGammaRepository.saveAndFlush(employeeGamma);

        // Get all the employeeGammaList where hireDate in
        defaultEmployeeGammaFiltering("hireDate.in=" + DEFAULT_HIRE_DATE + "," + UPDATED_HIRE_DATE, "hireDate.in=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeeGammasByHireDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployeeGamma = employeeGammaRepository.saveAndFlush(employeeGamma);

        // Get all the employeeGammaList where hireDate is not null
        defaultEmployeeGammaFiltering("hireDate.specified=true", "hireDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeGammasByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployeeGamma = employeeGammaRepository.saveAndFlush(employeeGamma);

        // Get all the employeeGammaList where position equals to
        defaultEmployeeGammaFiltering("position.equals=" + DEFAULT_POSITION, "position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllEmployeeGammasByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployeeGamma = employeeGammaRepository.saveAndFlush(employeeGamma);

        // Get all the employeeGammaList where position in
        defaultEmployeeGammaFiltering("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION, "position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllEmployeeGammasByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployeeGamma = employeeGammaRepository.saveAndFlush(employeeGamma);

        // Get all the employeeGammaList where position is not null
        defaultEmployeeGammaFiltering("position.specified=true", "position.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeGammasByPositionContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeGamma = employeeGammaRepository.saveAndFlush(employeeGamma);

        // Get all the employeeGammaList where position contains
        defaultEmployeeGammaFiltering("position.contains=" + DEFAULT_POSITION, "position.contains=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllEmployeeGammasByPositionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeGamma = employeeGammaRepository.saveAndFlush(employeeGamma);

        // Get all the employeeGammaList where position does not contain
        defaultEmployeeGammaFiltering("position.doesNotContain=" + UPDATED_POSITION, "position.doesNotContain=" + DEFAULT_POSITION);
    }

    @Test
    @Transactional
    void getAllEmployeeGammasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            employeeGammaRepository.saveAndFlush(employeeGamma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        employeeGamma.setTenant(tenant);
        employeeGammaRepository.saveAndFlush(employeeGamma);
        Long tenantId = tenant.getId();
        // Get all the employeeGammaList where tenant equals to tenantId
        defaultEmployeeGammaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the employeeGammaList where tenant equals to (tenantId + 1)
        defaultEmployeeGammaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultEmployeeGammaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEmployeeGammaShouldBeFound(shouldBeFound);
        defaultEmployeeGammaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmployeeGammaShouldBeFound(String filter) throws Exception {
        restEmployeeGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));

        // Check, that the count call also returns 1
        restEmployeeGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmployeeGammaShouldNotBeFound(String filter) throws Exception {
        restEmployeeGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmployeeGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEmployeeGamma() throws Exception {
        // Get the employeeGamma
        restEmployeeGammaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmployeeGamma() throws Exception {
        // Initialize the database
        insertedEmployeeGamma = employeeGammaRepository.saveAndFlush(employeeGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeGamma
        EmployeeGamma updatedEmployeeGamma = employeeGammaRepository.findById(employeeGamma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmployeeGamma are not directly saved in db
        em.detach(updatedEmployeeGamma);
        updatedEmployeeGamma
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);

        restEmployeeGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmployeeGamma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedEmployeeGamma))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmployeeGammaToMatchAllProperties(updatedEmployeeGamma);
    }

    @Test
    @Transactional
    void putNonExistingEmployeeGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeGamma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeGamma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employeeGamma))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployeeGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeGamma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employeeGamma))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployeeGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeGamma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeGammaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeGamma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeGammaWithPatch() throws Exception {
        // Initialize the database
        insertedEmployeeGamma = employeeGammaRepository.saveAndFlush(employeeGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeGamma using partial update
        EmployeeGamma partialUpdatedEmployeeGamma = new EmployeeGamma();
        partialUpdatedEmployeeGamma.setId(employeeGamma.getId());

        partialUpdatedEmployeeGamma.email(UPDATED_EMAIL).position(UPDATED_POSITION);

        restEmployeeGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmployeeGamma))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployeeGammaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEmployeeGamma, employeeGamma),
            getPersistedEmployeeGamma(employeeGamma)
        );
    }

    @Test
    @Transactional
    void fullUpdateEmployeeGammaWithPatch() throws Exception {
        // Initialize the database
        insertedEmployeeGamma = employeeGammaRepository.saveAndFlush(employeeGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeGamma using partial update
        EmployeeGamma partialUpdatedEmployeeGamma = new EmployeeGamma();
        partialUpdatedEmployeeGamma.setId(employeeGamma.getId());

        partialUpdatedEmployeeGamma
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);

        restEmployeeGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmployeeGamma))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployeeGammaUpdatableFieldsEquals(partialUpdatedEmployeeGamma, getPersistedEmployeeGamma(partialUpdatedEmployeeGamma));
    }

    @Test
    @Transactional
    void patchNonExistingEmployeeGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeGamma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeeGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employeeGamma))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployeeGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeGamma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employeeGamma))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployeeGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeGamma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeGammaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(employeeGamma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployeeGamma() throws Exception {
        // Initialize the database
        insertedEmployeeGamma = employeeGammaRepository.saveAndFlush(employeeGamma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the employeeGamma
        restEmployeeGammaMockMvc
            .perform(delete(ENTITY_API_URL_ID, employeeGamma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return employeeGammaRepository.count();
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

    protected EmployeeGamma getPersistedEmployeeGamma(EmployeeGamma employeeGamma) {
        return employeeGammaRepository.findById(employeeGamma.getId()).orElseThrow();
    }

    protected void assertPersistedEmployeeGammaToMatchAllProperties(EmployeeGamma expectedEmployeeGamma) {
        assertEmployeeGammaAllPropertiesEquals(expectedEmployeeGamma, getPersistedEmployeeGamma(expectedEmployeeGamma));
    }

    protected void assertPersistedEmployeeGammaToMatchUpdatableProperties(EmployeeGamma expectedEmployeeGamma) {
        assertEmployeeGammaAllUpdatablePropertiesEquals(expectedEmployeeGamma, getPersistedEmployeeGamma(expectedEmployeeGamma));
    }
}
