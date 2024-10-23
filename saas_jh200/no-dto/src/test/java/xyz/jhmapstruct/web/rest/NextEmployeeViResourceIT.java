package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextEmployeeViAsserts.*;
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
import xyz.jhmapstruct.domain.NextEmployeeVi;
import xyz.jhmapstruct.repository.NextEmployeeViRepository;

/**
 * Integration tests for the {@link NextEmployeeViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextEmployeeViResourceIT {

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

    private static final String ENTITY_API_URL = "/api/next-employee-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextEmployeeViRepository nextEmployeeViRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextEmployeeViMockMvc;

    private NextEmployeeVi nextEmployeeVi;

    private NextEmployeeVi insertedNextEmployeeVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextEmployeeVi createEntity() {
        return new NextEmployeeVi()
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
    public static NextEmployeeVi createUpdatedEntity() {
        return new NextEmployeeVi()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);
    }

    @BeforeEach
    public void initTest() {
        nextEmployeeVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextEmployeeVi != null) {
            nextEmployeeViRepository.delete(insertedNextEmployeeVi);
            insertedNextEmployeeVi = null;
        }
    }

    @Test
    @Transactional
    void createNextEmployeeVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextEmployeeVi
        var returnedNextEmployeeVi = om.readValue(
            restNextEmployeeViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextEmployeeVi.class
        );

        // Validate the NextEmployeeVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextEmployeeViUpdatableFieldsEquals(returnedNextEmployeeVi, getPersistedNextEmployeeVi(returnedNextEmployeeVi));

        insertedNextEmployeeVi = returnedNextEmployeeVi;
    }

    @Test
    @Transactional
    void createNextEmployeeViWithExistingId() throws Exception {
        // Create the NextEmployeeVi with an existing ID
        nextEmployeeVi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextEmployeeViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeVi)))
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextEmployeeVi.setFirstName(null);

        // Create the NextEmployeeVi, which fails.

        restNextEmployeeViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextEmployeeVi.setLastName(null);

        // Create the NextEmployeeVi, which fails.

        restNextEmployeeViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextEmployeeVi.setEmail(null);

        // Create the NextEmployeeVi, which fails.

        restNextEmployeeViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextEmployeeVis() throws Exception {
        // Initialize the database
        insertedNextEmployeeVi = nextEmployeeViRepository.saveAndFlush(nextEmployeeVi);

        // Get all the nextEmployeeViList
        restNextEmployeeViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextEmployeeVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));
    }

    @Test
    @Transactional
    void getNextEmployeeVi() throws Exception {
        // Initialize the database
        insertedNextEmployeeVi = nextEmployeeViRepository.saveAndFlush(nextEmployeeVi);

        // Get the nextEmployeeVi
        restNextEmployeeViMockMvc
            .perform(get(ENTITY_API_URL_ID, nextEmployeeVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextEmployeeVi.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.hireDate").value(DEFAULT_HIRE_DATE.toString()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION));
    }

    @Test
    @Transactional
    void getNextEmployeeVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextEmployeeVi = nextEmployeeViRepository.saveAndFlush(nextEmployeeVi);

        Long id = nextEmployeeVi.getId();

        defaultNextEmployeeViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextEmployeeViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextEmployeeViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextEmployeeVisByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeVi = nextEmployeeViRepository.saveAndFlush(nextEmployeeVi);

        // Get all the nextEmployeeViList where firstName equals to
        defaultNextEmployeeViFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeVisByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeVi = nextEmployeeViRepository.saveAndFlush(nextEmployeeVi);

        // Get all the nextEmployeeViList where firstName in
        defaultNextEmployeeViFiltering(
            "firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME,
            "firstName.in=" + UPDATED_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllNextEmployeeVisByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeVi = nextEmployeeViRepository.saveAndFlush(nextEmployeeVi);

        // Get all the nextEmployeeViList where firstName is not null
        defaultNextEmployeeViFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeVisByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeVi = nextEmployeeViRepository.saveAndFlush(nextEmployeeVi);

        // Get all the nextEmployeeViList where firstName contains
        defaultNextEmployeeViFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeVisByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeVi = nextEmployeeViRepository.saveAndFlush(nextEmployeeVi);

        // Get all the nextEmployeeViList where firstName does not contain
        defaultNextEmployeeViFiltering("firstName.doesNotContain=" + UPDATED_FIRST_NAME, "firstName.doesNotContain=" + DEFAULT_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeVisByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeVi = nextEmployeeViRepository.saveAndFlush(nextEmployeeVi);

        // Get all the nextEmployeeViList where lastName equals to
        defaultNextEmployeeViFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeVisByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeVi = nextEmployeeViRepository.saveAndFlush(nextEmployeeVi);

        // Get all the nextEmployeeViList where lastName in
        defaultNextEmployeeViFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeVisByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeVi = nextEmployeeViRepository.saveAndFlush(nextEmployeeVi);

        // Get all the nextEmployeeViList where lastName is not null
        defaultNextEmployeeViFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeVisByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeVi = nextEmployeeViRepository.saveAndFlush(nextEmployeeVi);

        // Get all the nextEmployeeViList where lastName contains
        defaultNextEmployeeViFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeVisByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeVi = nextEmployeeViRepository.saveAndFlush(nextEmployeeVi);

        // Get all the nextEmployeeViList where lastName does not contain
        defaultNextEmployeeViFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeVisByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeVi = nextEmployeeViRepository.saveAndFlush(nextEmployeeVi);

        // Get all the nextEmployeeViList where email equals to
        defaultNextEmployeeViFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeeVisByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeVi = nextEmployeeViRepository.saveAndFlush(nextEmployeeVi);

        // Get all the nextEmployeeViList where email in
        defaultNextEmployeeViFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeeVisByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeVi = nextEmployeeViRepository.saveAndFlush(nextEmployeeVi);

        // Get all the nextEmployeeViList where email is not null
        defaultNextEmployeeViFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeVisByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeVi = nextEmployeeViRepository.saveAndFlush(nextEmployeeVi);

        // Get all the nextEmployeeViList where email contains
        defaultNextEmployeeViFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeeVisByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeVi = nextEmployeeViRepository.saveAndFlush(nextEmployeeVi);

        // Get all the nextEmployeeViList where email does not contain
        defaultNextEmployeeViFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeeVisByHireDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeVi = nextEmployeeViRepository.saveAndFlush(nextEmployeeVi);

        // Get all the nextEmployeeViList where hireDate equals to
        defaultNextEmployeeViFiltering("hireDate.equals=" + DEFAULT_HIRE_DATE, "hireDate.equals=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllNextEmployeeVisByHireDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeVi = nextEmployeeViRepository.saveAndFlush(nextEmployeeVi);

        // Get all the nextEmployeeViList where hireDate in
        defaultNextEmployeeViFiltering("hireDate.in=" + DEFAULT_HIRE_DATE + "," + UPDATED_HIRE_DATE, "hireDate.in=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllNextEmployeeVisByHireDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeVi = nextEmployeeViRepository.saveAndFlush(nextEmployeeVi);

        // Get all the nextEmployeeViList where hireDate is not null
        defaultNextEmployeeViFiltering("hireDate.specified=true", "hireDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeVisByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeVi = nextEmployeeViRepository.saveAndFlush(nextEmployeeVi);

        // Get all the nextEmployeeViList where position equals to
        defaultNextEmployeeViFiltering("position.equals=" + DEFAULT_POSITION, "position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeeVisByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeVi = nextEmployeeViRepository.saveAndFlush(nextEmployeeVi);

        // Get all the nextEmployeeViList where position in
        defaultNextEmployeeViFiltering("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION, "position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeeVisByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeVi = nextEmployeeViRepository.saveAndFlush(nextEmployeeVi);

        // Get all the nextEmployeeViList where position is not null
        defaultNextEmployeeViFiltering("position.specified=true", "position.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeVisByPositionContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeVi = nextEmployeeViRepository.saveAndFlush(nextEmployeeVi);

        // Get all the nextEmployeeViList where position contains
        defaultNextEmployeeViFiltering("position.contains=" + DEFAULT_POSITION, "position.contains=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeeVisByPositionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeVi = nextEmployeeViRepository.saveAndFlush(nextEmployeeVi);

        // Get all the nextEmployeeViList where position does not contain
        defaultNextEmployeeViFiltering("position.doesNotContain=" + UPDATED_POSITION, "position.doesNotContain=" + DEFAULT_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeeVisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextEmployeeViRepository.saveAndFlush(nextEmployeeVi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextEmployeeVi.setTenant(tenant);
        nextEmployeeViRepository.saveAndFlush(nextEmployeeVi);
        Long tenantId = tenant.getId();
        // Get all the nextEmployeeViList where tenant equals to tenantId
        defaultNextEmployeeViShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextEmployeeViList where tenant equals to (tenantId + 1)
        defaultNextEmployeeViShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextEmployeeViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextEmployeeViShouldBeFound(shouldBeFound);
        defaultNextEmployeeViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextEmployeeViShouldBeFound(String filter) throws Exception {
        restNextEmployeeViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextEmployeeVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));

        // Check, that the count call also returns 1
        restNextEmployeeViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextEmployeeViShouldNotBeFound(String filter) throws Exception {
        restNextEmployeeViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextEmployeeViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextEmployeeVi() throws Exception {
        // Get the nextEmployeeVi
        restNextEmployeeViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextEmployeeVi() throws Exception {
        // Initialize the database
        insertedNextEmployeeVi = nextEmployeeViRepository.saveAndFlush(nextEmployeeVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextEmployeeVi
        NextEmployeeVi updatedNextEmployeeVi = nextEmployeeViRepository.findById(nextEmployeeVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextEmployeeVi are not directly saved in db
        em.detach(updatedNextEmployeeVi);
        updatedNextEmployeeVi
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);

        restNextEmployeeViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextEmployeeVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextEmployeeVi))
            )
            .andExpect(status().isOk());

        // Validate the NextEmployeeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextEmployeeViToMatchAllProperties(updatedNextEmployeeVi);
    }

    @Test
    @Transactional
    void putNonExistingNextEmployeeVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextEmployeeViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextEmployeeVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextEmployeeVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextEmployeeVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextEmployeeVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextEmployeeVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextEmployeeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextEmployeeViWithPatch() throws Exception {
        // Initialize the database
        insertedNextEmployeeVi = nextEmployeeViRepository.saveAndFlush(nextEmployeeVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextEmployeeVi using partial update
        NextEmployeeVi partialUpdatedNextEmployeeVi = new NextEmployeeVi();
        partialUpdatedNextEmployeeVi.setId(nextEmployeeVi.getId());

        partialUpdatedNextEmployeeVi.firstName(UPDATED_FIRST_NAME);

        restNextEmployeeViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextEmployeeVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextEmployeeVi))
            )
            .andExpect(status().isOk());

        // Validate the NextEmployeeVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextEmployeeViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextEmployeeVi, nextEmployeeVi),
            getPersistedNextEmployeeVi(nextEmployeeVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextEmployeeViWithPatch() throws Exception {
        // Initialize the database
        insertedNextEmployeeVi = nextEmployeeViRepository.saveAndFlush(nextEmployeeVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextEmployeeVi using partial update
        NextEmployeeVi partialUpdatedNextEmployeeVi = new NextEmployeeVi();
        partialUpdatedNextEmployeeVi.setId(nextEmployeeVi.getId());

        partialUpdatedNextEmployeeVi
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);

        restNextEmployeeViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextEmployeeVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextEmployeeVi))
            )
            .andExpect(status().isOk());

        // Validate the NextEmployeeVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextEmployeeViUpdatableFieldsEquals(partialUpdatedNextEmployeeVi, getPersistedNextEmployeeVi(partialUpdatedNextEmployeeVi));
    }

    @Test
    @Transactional
    void patchNonExistingNextEmployeeVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextEmployeeViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextEmployeeVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextEmployeeVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextEmployeeVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextEmployeeVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextEmployeeVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextEmployeeVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextEmployeeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextEmployeeVi() throws Exception {
        // Initialize the database
        insertedNextEmployeeVi = nextEmployeeViRepository.saveAndFlush(nextEmployeeVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextEmployeeVi
        restNextEmployeeViMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextEmployeeVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextEmployeeViRepository.count();
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

    protected NextEmployeeVi getPersistedNextEmployeeVi(NextEmployeeVi nextEmployeeVi) {
        return nextEmployeeViRepository.findById(nextEmployeeVi.getId()).orElseThrow();
    }

    protected void assertPersistedNextEmployeeViToMatchAllProperties(NextEmployeeVi expectedNextEmployeeVi) {
        assertNextEmployeeViAllPropertiesEquals(expectedNextEmployeeVi, getPersistedNextEmployeeVi(expectedNextEmployeeVi));
    }

    protected void assertPersistedNextEmployeeViToMatchUpdatableProperties(NextEmployeeVi expectedNextEmployeeVi) {
        assertNextEmployeeViAllUpdatablePropertiesEquals(expectedNextEmployeeVi, getPersistedNextEmployeeVi(expectedNextEmployeeVi));
    }
}
