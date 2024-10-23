package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.EmployeeAlphaAsserts.*;
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
import xyz.jhmapstruct.domain.EmployeeAlpha;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.repository.EmployeeAlphaRepository;

/**
 * Integration tests for the {@link EmployeeAlphaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmployeeAlphaResourceIT {

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

    private static final String ENTITY_API_URL = "/api/employee-alphas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmployeeAlphaRepository employeeAlphaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeAlphaMockMvc;

    private EmployeeAlpha employeeAlpha;

    private EmployeeAlpha insertedEmployeeAlpha;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeAlpha createEntity() {
        return new EmployeeAlpha()
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
    public static EmployeeAlpha createUpdatedEntity() {
        return new EmployeeAlpha()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);
    }

    @BeforeEach
    public void initTest() {
        employeeAlpha = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedEmployeeAlpha != null) {
            employeeAlphaRepository.delete(insertedEmployeeAlpha);
            insertedEmployeeAlpha = null;
        }
    }

    @Test
    @Transactional
    void createEmployeeAlpha() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EmployeeAlpha
        var returnedEmployeeAlpha = om.readValue(
            restEmployeeAlphaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeAlpha)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EmployeeAlpha.class
        );

        // Validate the EmployeeAlpha in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEmployeeAlphaUpdatableFieldsEquals(returnedEmployeeAlpha, getPersistedEmployeeAlpha(returnedEmployeeAlpha));

        insertedEmployeeAlpha = returnedEmployeeAlpha;
    }

    @Test
    @Transactional
    void createEmployeeAlphaWithExistingId() throws Exception {
        // Create the EmployeeAlpha with an existing ID
        employeeAlpha.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeAlpha)))
            .andExpect(status().isBadRequest());

        // Validate the EmployeeAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employeeAlpha.setFirstName(null);

        // Create the EmployeeAlpha, which fails.

        restEmployeeAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeAlpha)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employeeAlpha.setLastName(null);

        // Create the EmployeeAlpha, which fails.

        restEmployeeAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeAlpha)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employeeAlpha.setEmail(null);

        // Create the EmployeeAlpha, which fails.

        restEmployeeAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeAlpha)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmployeeAlphas() throws Exception {
        // Initialize the database
        insertedEmployeeAlpha = employeeAlphaRepository.saveAndFlush(employeeAlpha);

        // Get all the employeeAlphaList
        restEmployeeAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));
    }

    @Test
    @Transactional
    void getEmployeeAlpha() throws Exception {
        // Initialize the database
        insertedEmployeeAlpha = employeeAlphaRepository.saveAndFlush(employeeAlpha);

        // Get the employeeAlpha
        restEmployeeAlphaMockMvc
            .perform(get(ENTITY_API_URL_ID, employeeAlpha.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employeeAlpha.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.hireDate").value(DEFAULT_HIRE_DATE.toString()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION));
    }

    @Test
    @Transactional
    void getEmployeeAlphasByIdFiltering() throws Exception {
        // Initialize the database
        insertedEmployeeAlpha = employeeAlphaRepository.saveAndFlush(employeeAlpha);

        Long id = employeeAlpha.getId();

        defaultEmployeeAlphaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEmployeeAlphaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEmployeeAlphaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmployeeAlphasByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployeeAlpha = employeeAlphaRepository.saveAndFlush(employeeAlpha);

        // Get all the employeeAlphaList where firstName equals to
        defaultEmployeeAlphaFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeAlphasByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployeeAlpha = employeeAlphaRepository.saveAndFlush(employeeAlpha);

        // Get all the employeeAlphaList where firstName in
        defaultEmployeeAlphaFiltering(
            "firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME,
            "firstName.in=" + UPDATED_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllEmployeeAlphasByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployeeAlpha = employeeAlphaRepository.saveAndFlush(employeeAlpha);

        // Get all the employeeAlphaList where firstName is not null
        defaultEmployeeAlphaFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeAlphasByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeAlpha = employeeAlphaRepository.saveAndFlush(employeeAlpha);

        // Get all the employeeAlphaList where firstName contains
        defaultEmployeeAlphaFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeAlphasByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeAlpha = employeeAlphaRepository.saveAndFlush(employeeAlpha);

        // Get all the employeeAlphaList where firstName does not contain
        defaultEmployeeAlphaFiltering("firstName.doesNotContain=" + UPDATED_FIRST_NAME, "firstName.doesNotContain=" + DEFAULT_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeAlphasByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployeeAlpha = employeeAlphaRepository.saveAndFlush(employeeAlpha);

        // Get all the employeeAlphaList where lastName equals to
        defaultEmployeeAlphaFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeAlphasByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployeeAlpha = employeeAlphaRepository.saveAndFlush(employeeAlpha);

        // Get all the employeeAlphaList where lastName in
        defaultEmployeeAlphaFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeAlphasByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployeeAlpha = employeeAlphaRepository.saveAndFlush(employeeAlpha);

        // Get all the employeeAlphaList where lastName is not null
        defaultEmployeeAlphaFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeAlphasByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeAlpha = employeeAlphaRepository.saveAndFlush(employeeAlpha);

        // Get all the employeeAlphaList where lastName contains
        defaultEmployeeAlphaFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeAlphasByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeAlpha = employeeAlphaRepository.saveAndFlush(employeeAlpha);

        // Get all the employeeAlphaList where lastName does not contain
        defaultEmployeeAlphaFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeAlphasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployeeAlpha = employeeAlphaRepository.saveAndFlush(employeeAlpha);

        // Get all the employeeAlphaList where email equals to
        defaultEmployeeAlphaFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeeAlphasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployeeAlpha = employeeAlphaRepository.saveAndFlush(employeeAlpha);

        // Get all the employeeAlphaList where email in
        defaultEmployeeAlphaFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeeAlphasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployeeAlpha = employeeAlphaRepository.saveAndFlush(employeeAlpha);

        // Get all the employeeAlphaList where email is not null
        defaultEmployeeAlphaFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeAlphasByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeAlpha = employeeAlphaRepository.saveAndFlush(employeeAlpha);

        // Get all the employeeAlphaList where email contains
        defaultEmployeeAlphaFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeeAlphasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeAlpha = employeeAlphaRepository.saveAndFlush(employeeAlpha);

        // Get all the employeeAlphaList where email does not contain
        defaultEmployeeAlphaFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeeAlphasByHireDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployeeAlpha = employeeAlphaRepository.saveAndFlush(employeeAlpha);

        // Get all the employeeAlphaList where hireDate equals to
        defaultEmployeeAlphaFiltering("hireDate.equals=" + DEFAULT_HIRE_DATE, "hireDate.equals=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeeAlphasByHireDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployeeAlpha = employeeAlphaRepository.saveAndFlush(employeeAlpha);

        // Get all the employeeAlphaList where hireDate in
        defaultEmployeeAlphaFiltering("hireDate.in=" + DEFAULT_HIRE_DATE + "," + UPDATED_HIRE_DATE, "hireDate.in=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeeAlphasByHireDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployeeAlpha = employeeAlphaRepository.saveAndFlush(employeeAlpha);

        // Get all the employeeAlphaList where hireDate is not null
        defaultEmployeeAlphaFiltering("hireDate.specified=true", "hireDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeAlphasByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployeeAlpha = employeeAlphaRepository.saveAndFlush(employeeAlpha);

        // Get all the employeeAlphaList where position equals to
        defaultEmployeeAlphaFiltering("position.equals=" + DEFAULT_POSITION, "position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllEmployeeAlphasByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployeeAlpha = employeeAlphaRepository.saveAndFlush(employeeAlpha);

        // Get all the employeeAlphaList where position in
        defaultEmployeeAlphaFiltering("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION, "position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllEmployeeAlphasByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployeeAlpha = employeeAlphaRepository.saveAndFlush(employeeAlpha);

        // Get all the employeeAlphaList where position is not null
        defaultEmployeeAlphaFiltering("position.specified=true", "position.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeAlphasByPositionContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeAlpha = employeeAlphaRepository.saveAndFlush(employeeAlpha);

        // Get all the employeeAlphaList where position contains
        defaultEmployeeAlphaFiltering("position.contains=" + DEFAULT_POSITION, "position.contains=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllEmployeeAlphasByPositionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployeeAlpha = employeeAlphaRepository.saveAndFlush(employeeAlpha);

        // Get all the employeeAlphaList where position does not contain
        defaultEmployeeAlphaFiltering("position.doesNotContain=" + UPDATED_POSITION, "position.doesNotContain=" + DEFAULT_POSITION);
    }

    @Test
    @Transactional
    void getAllEmployeeAlphasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            employeeAlphaRepository.saveAndFlush(employeeAlpha);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        employeeAlpha.setTenant(tenant);
        employeeAlphaRepository.saveAndFlush(employeeAlpha);
        Long tenantId = tenant.getId();
        // Get all the employeeAlphaList where tenant equals to tenantId
        defaultEmployeeAlphaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the employeeAlphaList where tenant equals to (tenantId + 1)
        defaultEmployeeAlphaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultEmployeeAlphaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEmployeeAlphaShouldBeFound(shouldBeFound);
        defaultEmployeeAlphaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmployeeAlphaShouldBeFound(String filter) throws Exception {
        restEmployeeAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));

        // Check, that the count call also returns 1
        restEmployeeAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmployeeAlphaShouldNotBeFound(String filter) throws Exception {
        restEmployeeAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmployeeAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEmployeeAlpha() throws Exception {
        // Get the employeeAlpha
        restEmployeeAlphaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmployeeAlpha() throws Exception {
        // Initialize the database
        insertedEmployeeAlpha = employeeAlphaRepository.saveAndFlush(employeeAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeAlpha
        EmployeeAlpha updatedEmployeeAlpha = employeeAlphaRepository.findById(employeeAlpha.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmployeeAlpha are not directly saved in db
        em.detach(updatedEmployeeAlpha);
        updatedEmployeeAlpha
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);

        restEmployeeAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmployeeAlpha.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedEmployeeAlpha))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmployeeAlphaToMatchAllProperties(updatedEmployeeAlpha);
    }

    @Test
    @Transactional
    void putNonExistingEmployeeAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeAlpha.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeAlpha.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employeeAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployeeAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employeeAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployeeAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeAlphaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeAlpha)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedEmployeeAlpha = employeeAlphaRepository.saveAndFlush(employeeAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeAlpha using partial update
        EmployeeAlpha partialUpdatedEmployeeAlpha = new EmployeeAlpha();
        partialUpdatedEmployeeAlpha.setId(employeeAlpha.getId());

        partialUpdatedEmployeeAlpha.lastName(UPDATED_LAST_NAME).email(UPDATED_EMAIL);

        restEmployeeAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmployeeAlpha))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployeeAlphaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEmployeeAlpha, employeeAlpha),
            getPersistedEmployeeAlpha(employeeAlpha)
        );
    }

    @Test
    @Transactional
    void fullUpdateEmployeeAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedEmployeeAlpha = employeeAlphaRepository.saveAndFlush(employeeAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeAlpha using partial update
        EmployeeAlpha partialUpdatedEmployeeAlpha = new EmployeeAlpha();
        partialUpdatedEmployeeAlpha.setId(employeeAlpha.getId());

        partialUpdatedEmployeeAlpha
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);

        restEmployeeAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmployeeAlpha))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployeeAlphaUpdatableFieldsEquals(partialUpdatedEmployeeAlpha, getPersistedEmployeeAlpha(partialUpdatedEmployeeAlpha));
    }

    @Test
    @Transactional
    void patchNonExistingEmployeeAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeAlpha.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeeAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employeeAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployeeAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employeeAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployeeAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeAlphaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(employeeAlpha)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployeeAlpha() throws Exception {
        // Initialize the database
        insertedEmployeeAlpha = employeeAlphaRepository.saveAndFlush(employeeAlpha);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the employeeAlpha
        restEmployeeAlphaMockMvc
            .perform(delete(ENTITY_API_URL_ID, employeeAlpha.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return employeeAlphaRepository.count();
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

    protected EmployeeAlpha getPersistedEmployeeAlpha(EmployeeAlpha employeeAlpha) {
        return employeeAlphaRepository.findById(employeeAlpha.getId()).orElseThrow();
    }

    protected void assertPersistedEmployeeAlphaToMatchAllProperties(EmployeeAlpha expectedEmployeeAlpha) {
        assertEmployeeAlphaAllPropertiesEquals(expectedEmployeeAlpha, getPersistedEmployeeAlpha(expectedEmployeeAlpha));
    }

    protected void assertPersistedEmployeeAlphaToMatchUpdatableProperties(EmployeeAlpha expectedEmployeeAlpha) {
        assertEmployeeAlphaAllUpdatablePropertiesEquals(expectedEmployeeAlpha, getPersistedEmployeeAlpha(expectedEmployeeAlpha));
    }
}
