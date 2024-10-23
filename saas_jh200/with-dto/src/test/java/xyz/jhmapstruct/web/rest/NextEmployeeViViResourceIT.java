package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextEmployeeViViAsserts.*;
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
import xyz.jhmapstruct.domain.NextEmployeeViVi;
import xyz.jhmapstruct.repository.NextEmployeeViViRepository;
import xyz.jhmapstruct.service.dto.NextEmployeeViViDTO;
import xyz.jhmapstruct.service.mapper.NextEmployeeViViMapper;

/**
 * Integration tests for the {@link NextEmployeeViViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextEmployeeViViResourceIT {

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

    private static final String ENTITY_API_URL = "/api/next-employee-vi-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextEmployeeViViRepository nextEmployeeViViRepository;

    @Autowired
    private NextEmployeeViViMapper nextEmployeeViViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextEmployeeViViMockMvc;

    private NextEmployeeViVi nextEmployeeViVi;

    private NextEmployeeViVi insertedNextEmployeeViVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextEmployeeViVi createEntity() {
        return new NextEmployeeViVi()
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
    public static NextEmployeeViVi createUpdatedEntity() {
        return new NextEmployeeViVi()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);
    }

    @BeforeEach
    public void initTest() {
        nextEmployeeViVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextEmployeeViVi != null) {
            nextEmployeeViViRepository.delete(insertedNextEmployeeViVi);
            insertedNextEmployeeViVi = null;
        }
    }

    @Test
    @Transactional
    void createNextEmployeeViVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextEmployeeViVi
        NextEmployeeViViDTO nextEmployeeViViDTO = nextEmployeeViViMapper.toDto(nextEmployeeViVi);
        var returnedNextEmployeeViViDTO = om.readValue(
            restNextEmployeeViViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeViViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextEmployeeViViDTO.class
        );

        // Validate the NextEmployeeViVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextEmployeeViVi = nextEmployeeViViMapper.toEntity(returnedNextEmployeeViViDTO);
        assertNextEmployeeViViUpdatableFieldsEquals(returnedNextEmployeeViVi, getPersistedNextEmployeeViVi(returnedNextEmployeeViVi));

        insertedNextEmployeeViVi = returnedNextEmployeeViVi;
    }

    @Test
    @Transactional
    void createNextEmployeeViViWithExistingId() throws Exception {
        // Create the NextEmployeeViVi with an existing ID
        nextEmployeeViVi.setId(1L);
        NextEmployeeViViDTO nextEmployeeViViDTO = nextEmployeeViViMapper.toDto(nextEmployeeViVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextEmployeeViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeViViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextEmployeeViVi.setFirstName(null);

        // Create the NextEmployeeViVi, which fails.
        NextEmployeeViViDTO nextEmployeeViViDTO = nextEmployeeViViMapper.toDto(nextEmployeeViVi);

        restNextEmployeeViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeViViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextEmployeeViVi.setLastName(null);

        // Create the NextEmployeeViVi, which fails.
        NextEmployeeViViDTO nextEmployeeViViDTO = nextEmployeeViViMapper.toDto(nextEmployeeViVi);

        restNextEmployeeViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeViViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextEmployeeViVi.setEmail(null);

        // Create the NextEmployeeViVi, which fails.
        NextEmployeeViViDTO nextEmployeeViViDTO = nextEmployeeViViMapper.toDto(nextEmployeeViVi);

        restNextEmployeeViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeViViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextEmployeeViVis() throws Exception {
        // Initialize the database
        insertedNextEmployeeViVi = nextEmployeeViViRepository.saveAndFlush(nextEmployeeViVi);

        // Get all the nextEmployeeViViList
        restNextEmployeeViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextEmployeeViVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));
    }

    @Test
    @Transactional
    void getNextEmployeeViVi() throws Exception {
        // Initialize the database
        insertedNextEmployeeViVi = nextEmployeeViViRepository.saveAndFlush(nextEmployeeViVi);

        // Get the nextEmployeeViVi
        restNextEmployeeViViMockMvc
            .perform(get(ENTITY_API_URL_ID, nextEmployeeViVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextEmployeeViVi.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.hireDate").value(DEFAULT_HIRE_DATE.toString()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION));
    }

    @Test
    @Transactional
    void getNextEmployeeViVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextEmployeeViVi = nextEmployeeViViRepository.saveAndFlush(nextEmployeeViVi);

        Long id = nextEmployeeViVi.getId();

        defaultNextEmployeeViViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextEmployeeViViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextEmployeeViViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextEmployeeViVisByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeViVi = nextEmployeeViViRepository.saveAndFlush(nextEmployeeViVi);

        // Get all the nextEmployeeViViList where firstName equals to
        defaultNextEmployeeViViFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeViVisByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeViVi = nextEmployeeViViRepository.saveAndFlush(nextEmployeeViVi);

        // Get all the nextEmployeeViViList where firstName in
        defaultNextEmployeeViViFiltering(
            "firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME,
            "firstName.in=" + UPDATED_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllNextEmployeeViVisByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeViVi = nextEmployeeViViRepository.saveAndFlush(nextEmployeeViVi);

        // Get all the nextEmployeeViViList where firstName is not null
        defaultNextEmployeeViViFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeViVisByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeViVi = nextEmployeeViViRepository.saveAndFlush(nextEmployeeViVi);

        // Get all the nextEmployeeViViList where firstName contains
        defaultNextEmployeeViViFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeViVisByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeViVi = nextEmployeeViViRepository.saveAndFlush(nextEmployeeViVi);

        // Get all the nextEmployeeViViList where firstName does not contain
        defaultNextEmployeeViViFiltering(
            "firstName.doesNotContain=" + UPDATED_FIRST_NAME,
            "firstName.doesNotContain=" + DEFAULT_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllNextEmployeeViVisByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeViVi = nextEmployeeViViRepository.saveAndFlush(nextEmployeeViVi);

        // Get all the nextEmployeeViViList where lastName equals to
        defaultNextEmployeeViViFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeViVisByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeViVi = nextEmployeeViViRepository.saveAndFlush(nextEmployeeViVi);

        // Get all the nextEmployeeViViList where lastName in
        defaultNextEmployeeViViFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeViVisByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeViVi = nextEmployeeViViRepository.saveAndFlush(nextEmployeeViVi);

        // Get all the nextEmployeeViViList where lastName is not null
        defaultNextEmployeeViViFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeViVisByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeViVi = nextEmployeeViViRepository.saveAndFlush(nextEmployeeViVi);

        // Get all the nextEmployeeViViList where lastName contains
        defaultNextEmployeeViViFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeViVisByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeViVi = nextEmployeeViViRepository.saveAndFlush(nextEmployeeViVi);

        // Get all the nextEmployeeViViList where lastName does not contain
        defaultNextEmployeeViViFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeViVisByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeViVi = nextEmployeeViViRepository.saveAndFlush(nextEmployeeViVi);

        // Get all the nextEmployeeViViList where email equals to
        defaultNextEmployeeViViFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeeViVisByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeViVi = nextEmployeeViViRepository.saveAndFlush(nextEmployeeViVi);

        // Get all the nextEmployeeViViList where email in
        defaultNextEmployeeViViFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeeViVisByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeViVi = nextEmployeeViViRepository.saveAndFlush(nextEmployeeViVi);

        // Get all the nextEmployeeViViList where email is not null
        defaultNextEmployeeViViFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeViVisByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeViVi = nextEmployeeViViRepository.saveAndFlush(nextEmployeeViVi);

        // Get all the nextEmployeeViViList where email contains
        defaultNextEmployeeViViFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeeViVisByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeViVi = nextEmployeeViViRepository.saveAndFlush(nextEmployeeViVi);

        // Get all the nextEmployeeViViList where email does not contain
        defaultNextEmployeeViViFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeeViVisByHireDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeViVi = nextEmployeeViViRepository.saveAndFlush(nextEmployeeViVi);

        // Get all the nextEmployeeViViList where hireDate equals to
        defaultNextEmployeeViViFiltering("hireDate.equals=" + DEFAULT_HIRE_DATE, "hireDate.equals=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllNextEmployeeViVisByHireDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeViVi = nextEmployeeViViRepository.saveAndFlush(nextEmployeeViVi);

        // Get all the nextEmployeeViViList where hireDate in
        defaultNextEmployeeViViFiltering("hireDate.in=" + DEFAULT_HIRE_DATE + "," + UPDATED_HIRE_DATE, "hireDate.in=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllNextEmployeeViVisByHireDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeViVi = nextEmployeeViViRepository.saveAndFlush(nextEmployeeViVi);

        // Get all the nextEmployeeViViList where hireDate is not null
        defaultNextEmployeeViViFiltering("hireDate.specified=true", "hireDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeViVisByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeViVi = nextEmployeeViViRepository.saveAndFlush(nextEmployeeViVi);

        // Get all the nextEmployeeViViList where position equals to
        defaultNextEmployeeViViFiltering("position.equals=" + DEFAULT_POSITION, "position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeeViVisByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeViVi = nextEmployeeViViRepository.saveAndFlush(nextEmployeeViVi);

        // Get all the nextEmployeeViViList where position in
        defaultNextEmployeeViViFiltering("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION, "position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeeViVisByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeViVi = nextEmployeeViViRepository.saveAndFlush(nextEmployeeViVi);

        // Get all the nextEmployeeViViList where position is not null
        defaultNextEmployeeViViFiltering("position.specified=true", "position.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeViVisByPositionContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeViVi = nextEmployeeViViRepository.saveAndFlush(nextEmployeeViVi);

        // Get all the nextEmployeeViViList where position contains
        defaultNextEmployeeViViFiltering("position.contains=" + DEFAULT_POSITION, "position.contains=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeeViVisByPositionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeViVi = nextEmployeeViViRepository.saveAndFlush(nextEmployeeViVi);

        // Get all the nextEmployeeViViList where position does not contain
        defaultNextEmployeeViViFiltering("position.doesNotContain=" + UPDATED_POSITION, "position.doesNotContain=" + DEFAULT_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeeViVisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextEmployeeViViRepository.saveAndFlush(nextEmployeeViVi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextEmployeeViVi.setTenant(tenant);
        nextEmployeeViViRepository.saveAndFlush(nextEmployeeViVi);
        Long tenantId = tenant.getId();
        // Get all the nextEmployeeViViList where tenant equals to tenantId
        defaultNextEmployeeViViShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextEmployeeViViList where tenant equals to (tenantId + 1)
        defaultNextEmployeeViViShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextEmployeeViViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextEmployeeViViShouldBeFound(shouldBeFound);
        defaultNextEmployeeViViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextEmployeeViViShouldBeFound(String filter) throws Exception {
        restNextEmployeeViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextEmployeeViVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));

        // Check, that the count call also returns 1
        restNextEmployeeViViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextEmployeeViViShouldNotBeFound(String filter) throws Exception {
        restNextEmployeeViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextEmployeeViViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextEmployeeViVi() throws Exception {
        // Get the nextEmployeeViVi
        restNextEmployeeViViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextEmployeeViVi() throws Exception {
        // Initialize the database
        insertedNextEmployeeViVi = nextEmployeeViViRepository.saveAndFlush(nextEmployeeViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextEmployeeViVi
        NextEmployeeViVi updatedNextEmployeeViVi = nextEmployeeViViRepository.findById(nextEmployeeViVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextEmployeeViVi are not directly saved in db
        em.detach(updatedNextEmployeeViVi);
        updatedNextEmployeeViVi
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);
        NextEmployeeViViDTO nextEmployeeViViDTO = nextEmployeeViViMapper.toDto(updatedNextEmployeeViVi);

        restNextEmployeeViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextEmployeeViViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextEmployeeViViDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextEmployeeViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextEmployeeViViToMatchAllProperties(updatedNextEmployeeViVi);
    }

    @Test
    @Transactional
    void putNonExistingNextEmployeeViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeViVi.setId(longCount.incrementAndGet());

        // Create the NextEmployeeViVi
        NextEmployeeViViDTO nextEmployeeViViDTO = nextEmployeeViViMapper.toDto(nextEmployeeViVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextEmployeeViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextEmployeeViViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextEmployeeViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextEmployeeViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeViVi.setId(longCount.incrementAndGet());

        // Create the NextEmployeeViVi
        NextEmployeeViViDTO nextEmployeeViViDTO = nextEmployeeViViMapper.toDto(nextEmployeeViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextEmployeeViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextEmployeeViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeViVi.setId(longCount.incrementAndGet());

        // Create the NextEmployeeViVi
        NextEmployeeViViDTO nextEmployeeViViDTO = nextEmployeeViViMapper.toDto(nextEmployeeViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeViViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeViViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextEmployeeViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextEmployeeViViWithPatch() throws Exception {
        // Initialize the database
        insertedNextEmployeeViVi = nextEmployeeViViRepository.saveAndFlush(nextEmployeeViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextEmployeeViVi using partial update
        NextEmployeeViVi partialUpdatedNextEmployeeViVi = new NextEmployeeViVi();
        partialUpdatedNextEmployeeViVi.setId(nextEmployeeViVi.getId());

        partialUpdatedNextEmployeeViVi.hireDate(UPDATED_HIRE_DATE).position(UPDATED_POSITION);

        restNextEmployeeViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextEmployeeViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextEmployeeViVi))
            )
            .andExpect(status().isOk());

        // Validate the NextEmployeeViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextEmployeeViViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextEmployeeViVi, nextEmployeeViVi),
            getPersistedNextEmployeeViVi(nextEmployeeViVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextEmployeeViViWithPatch() throws Exception {
        // Initialize the database
        insertedNextEmployeeViVi = nextEmployeeViViRepository.saveAndFlush(nextEmployeeViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextEmployeeViVi using partial update
        NextEmployeeViVi partialUpdatedNextEmployeeViVi = new NextEmployeeViVi();
        partialUpdatedNextEmployeeViVi.setId(nextEmployeeViVi.getId());

        partialUpdatedNextEmployeeViVi
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);

        restNextEmployeeViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextEmployeeViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextEmployeeViVi))
            )
            .andExpect(status().isOk());

        // Validate the NextEmployeeViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextEmployeeViViUpdatableFieldsEquals(
            partialUpdatedNextEmployeeViVi,
            getPersistedNextEmployeeViVi(partialUpdatedNextEmployeeViVi)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextEmployeeViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeViVi.setId(longCount.incrementAndGet());

        // Create the NextEmployeeViVi
        NextEmployeeViViDTO nextEmployeeViViDTO = nextEmployeeViViMapper.toDto(nextEmployeeViVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextEmployeeViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextEmployeeViViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextEmployeeViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextEmployeeViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeViVi.setId(longCount.incrementAndGet());

        // Create the NextEmployeeViVi
        NextEmployeeViViDTO nextEmployeeViViDTO = nextEmployeeViViMapper.toDto(nextEmployeeViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextEmployeeViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextEmployeeViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeViVi.setId(longCount.incrementAndGet());

        // Create the NextEmployeeViVi
        NextEmployeeViViDTO nextEmployeeViViDTO = nextEmployeeViViMapper.toDto(nextEmployeeViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeViViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextEmployeeViViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextEmployeeViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextEmployeeViVi() throws Exception {
        // Initialize the database
        insertedNextEmployeeViVi = nextEmployeeViViRepository.saveAndFlush(nextEmployeeViVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextEmployeeViVi
        restNextEmployeeViViMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextEmployeeViVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextEmployeeViViRepository.count();
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

    protected NextEmployeeViVi getPersistedNextEmployeeViVi(NextEmployeeViVi nextEmployeeViVi) {
        return nextEmployeeViViRepository.findById(nextEmployeeViVi.getId()).orElseThrow();
    }

    protected void assertPersistedNextEmployeeViViToMatchAllProperties(NextEmployeeViVi expectedNextEmployeeViVi) {
        assertNextEmployeeViViAllPropertiesEquals(expectedNextEmployeeViVi, getPersistedNextEmployeeViVi(expectedNextEmployeeViVi));
    }

    protected void assertPersistedNextEmployeeViViToMatchUpdatableProperties(NextEmployeeViVi expectedNextEmployeeViVi) {
        assertNextEmployeeViViAllUpdatablePropertiesEquals(
            expectedNextEmployeeViVi,
            getPersistedNextEmployeeViVi(expectedNextEmployeeViVi)
        );
    }
}
