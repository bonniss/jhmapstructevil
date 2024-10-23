package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextEmployeeMiAsserts.*;
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
import xyz.jhmapstruct.domain.NextEmployeeMi;
import xyz.jhmapstruct.repository.NextEmployeeMiRepository;

/**
 * Integration tests for the {@link NextEmployeeMiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextEmployeeMiResourceIT {

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

    private static final String ENTITY_API_URL = "/api/next-employee-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextEmployeeMiRepository nextEmployeeMiRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextEmployeeMiMockMvc;

    private NextEmployeeMi nextEmployeeMi;

    private NextEmployeeMi insertedNextEmployeeMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextEmployeeMi createEntity() {
        return new NextEmployeeMi()
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
    public static NextEmployeeMi createUpdatedEntity() {
        return new NextEmployeeMi()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);
    }

    @BeforeEach
    public void initTest() {
        nextEmployeeMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextEmployeeMi != null) {
            nextEmployeeMiRepository.delete(insertedNextEmployeeMi);
            insertedNextEmployeeMi = null;
        }
    }

    @Test
    @Transactional
    void createNextEmployeeMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextEmployeeMi
        var returnedNextEmployeeMi = om.readValue(
            restNextEmployeeMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeMi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextEmployeeMi.class
        );

        // Validate the NextEmployeeMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextEmployeeMiUpdatableFieldsEquals(returnedNextEmployeeMi, getPersistedNextEmployeeMi(returnedNextEmployeeMi));

        insertedNextEmployeeMi = returnedNextEmployeeMi;
    }

    @Test
    @Transactional
    void createNextEmployeeMiWithExistingId() throws Exception {
        // Create the NextEmployeeMi with an existing ID
        nextEmployeeMi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextEmployeeMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeMi)))
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextEmployeeMi.setFirstName(null);

        // Create the NextEmployeeMi, which fails.

        restNextEmployeeMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextEmployeeMi.setLastName(null);

        // Create the NextEmployeeMi, which fails.

        restNextEmployeeMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextEmployeeMi.setEmail(null);

        // Create the NextEmployeeMi, which fails.

        restNextEmployeeMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMis() throws Exception {
        // Initialize the database
        insertedNextEmployeeMi = nextEmployeeMiRepository.saveAndFlush(nextEmployeeMi);

        // Get all the nextEmployeeMiList
        restNextEmployeeMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextEmployeeMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));
    }

    @Test
    @Transactional
    void getNextEmployeeMi() throws Exception {
        // Initialize the database
        insertedNextEmployeeMi = nextEmployeeMiRepository.saveAndFlush(nextEmployeeMi);

        // Get the nextEmployeeMi
        restNextEmployeeMiMockMvc
            .perform(get(ENTITY_API_URL_ID, nextEmployeeMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextEmployeeMi.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.hireDate").value(DEFAULT_HIRE_DATE.toString()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION));
    }

    @Test
    @Transactional
    void getNextEmployeeMisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextEmployeeMi = nextEmployeeMiRepository.saveAndFlush(nextEmployeeMi);

        Long id = nextEmployeeMi.getId();

        defaultNextEmployeeMiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextEmployeeMiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextEmployeeMiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMisByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeMi = nextEmployeeMiRepository.saveAndFlush(nextEmployeeMi);

        // Get all the nextEmployeeMiList where firstName equals to
        defaultNextEmployeeMiFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMisByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeMi = nextEmployeeMiRepository.saveAndFlush(nextEmployeeMi);

        // Get all the nextEmployeeMiList where firstName in
        defaultNextEmployeeMiFiltering(
            "firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME,
            "firstName.in=" + UPDATED_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllNextEmployeeMisByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeMi = nextEmployeeMiRepository.saveAndFlush(nextEmployeeMi);

        // Get all the nextEmployeeMiList where firstName is not null
        defaultNextEmployeeMiFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeMisByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeMi = nextEmployeeMiRepository.saveAndFlush(nextEmployeeMi);

        // Get all the nextEmployeeMiList where firstName contains
        defaultNextEmployeeMiFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMisByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeMi = nextEmployeeMiRepository.saveAndFlush(nextEmployeeMi);

        // Get all the nextEmployeeMiList where firstName does not contain
        defaultNextEmployeeMiFiltering("firstName.doesNotContain=" + UPDATED_FIRST_NAME, "firstName.doesNotContain=" + DEFAULT_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMisByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeMi = nextEmployeeMiRepository.saveAndFlush(nextEmployeeMi);

        // Get all the nextEmployeeMiList where lastName equals to
        defaultNextEmployeeMiFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMisByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeMi = nextEmployeeMiRepository.saveAndFlush(nextEmployeeMi);

        // Get all the nextEmployeeMiList where lastName in
        defaultNextEmployeeMiFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMisByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeMi = nextEmployeeMiRepository.saveAndFlush(nextEmployeeMi);

        // Get all the nextEmployeeMiList where lastName is not null
        defaultNextEmployeeMiFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeMisByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeMi = nextEmployeeMiRepository.saveAndFlush(nextEmployeeMi);

        // Get all the nextEmployeeMiList where lastName contains
        defaultNextEmployeeMiFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMisByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeMi = nextEmployeeMiRepository.saveAndFlush(nextEmployeeMi);

        // Get all the nextEmployeeMiList where lastName does not contain
        defaultNextEmployeeMiFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMisByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeMi = nextEmployeeMiRepository.saveAndFlush(nextEmployeeMi);

        // Get all the nextEmployeeMiList where email equals to
        defaultNextEmployeeMiFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMisByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeMi = nextEmployeeMiRepository.saveAndFlush(nextEmployeeMi);

        // Get all the nextEmployeeMiList where email in
        defaultNextEmployeeMiFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMisByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeMi = nextEmployeeMiRepository.saveAndFlush(nextEmployeeMi);

        // Get all the nextEmployeeMiList where email is not null
        defaultNextEmployeeMiFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeMisByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeMi = nextEmployeeMiRepository.saveAndFlush(nextEmployeeMi);

        // Get all the nextEmployeeMiList where email contains
        defaultNextEmployeeMiFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMisByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeMi = nextEmployeeMiRepository.saveAndFlush(nextEmployeeMi);

        // Get all the nextEmployeeMiList where email does not contain
        defaultNextEmployeeMiFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMisByHireDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeMi = nextEmployeeMiRepository.saveAndFlush(nextEmployeeMi);

        // Get all the nextEmployeeMiList where hireDate equals to
        defaultNextEmployeeMiFiltering("hireDate.equals=" + DEFAULT_HIRE_DATE, "hireDate.equals=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMisByHireDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeMi = nextEmployeeMiRepository.saveAndFlush(nextEmployeeMi);

        // Get all the nextEmployeeMiList where hireDate in
        defaultNextEmployeeMiFiltering("hireDate.in=" + DEFAULT_HIRE_DATE + "," + UPDATED_HIRE_DATE, "hireDate.in=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMisByHireDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeMi = nextEmployeeMiRepository.saveAndFlush(nextEmployeeMi);

        // Get all the nextEmployeeMiList where hireDate is not null
        defaultNextEmployeeMiFiltering("hireDate.specified=true", "hireDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeMisByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeMi = nextEmployeeMiRepository.saveAndFlush(nextEmployeeMi);

        // Get all the nextEmployeeMiList where position equals to
        defaultNextEmployeeMiFiltering("position.equals=" + DEFAULT_POSITION, "position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMisByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeMi = nextEmployeeMiRepository.saveAndFlush(nextEmployeeMi);

        // Get all the nextEmployeeMiList where position in
        defaultNextEmployeeMiFiltering("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION, "position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMisByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeMi = nextEmployeeMiRepository.saveAndFlush(nextEmployeeMi);

        // Get all the nextEmployeeMiList where position is not null
        defaultNextEmployeeMiFiltering("position.specified=true", "position.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeMisByPositionContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeMi = nextEmployeeMiRepository.saveAndFlush(nextEmployeeMi);

        // Get all the nextEmployeeMiList where position contains
        defaultNextEmployeeMiFiltering("position.contains=" + DEFAULT_POSITION, "position.contains=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMisByPositionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeMi = nextEmployeeMiRepository.saveAndFlush(nextEmployeeMi);

        // Get all the nextEmployeeMiList where position does not contain
        defaultNextEmployeeMiFiltering("position.doesNotContain=" + UPDATED_POSITION, "position.doesNotContain=" + DEFAULT_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeeMisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextEmployeeMiRepository.saveAndFlush(nextEmployeeMi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextEmployeeMi.setTenant(tenant);
        nextEmployeeMiRepository.saveAndFlush(nextEmployeeMi);
        Long tenantId = tenant.getId();
        // Get all the nextEmployeeMiList where tenant equals to tenantId
        defaultNextEmployeeMiShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextEmployeeMiList where tenant equals to (tenantId + 1)
        defaultNextEmployeeMiShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextEmployeeMiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextEmployeeMiShouldBeFound(shouldBeFound);
        defaultNextEmployeeMiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextEmployeeMiShouldBeFound(String filter) throws Exception {
        restNextEmployeeMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextEmployeeMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));

        // Check, that the count call also returns 1
        restNextEmployeeMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextEmployeeMiShouldNotBeFound(String filter) throws Exception {
        restNextEmployeeMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextEmployeeMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextEmployeeMi() throws Exception {
        // Get the nextEmployeeMi
        restNextEmployeeMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextEmployeeMi() throws Exception {
        // Initialize the database
        insertedNextEmployeeMi = nextEmployeeMiRepository.saveAndFlush(nextEmployeeMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextEmployeeMi
        NextEmployeeMi updatedNextEmployeeMi = nextEmployeeMiRepository.findById(nextEmployeeMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextEmployeeMi are not directly saved in db
        em.detach(updatedNextEmployeeMi);
        updatedNextEmployeeMi
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);

        restNextEmployeeMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextEmployeeMi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextEmployeeMi))
            )
            .andExpect(status().isOk());

        // Validate the NextEmployeeMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextEmployeeMiToMatchAllProperties(updatedNextEmployeeMi);
    }

    @Test
    @Transactional
    void putNonExistingNextEmployeeMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeMi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextEmployeeMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextEmployeeMi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextEmployeeMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextEmployeeMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextEmployeeMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextEmployeeMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeMi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextEmployeeMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextEmployeeMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextEmployeeMi = nextEmployeeMiRepository.saveAndFlush(nextEmployeeMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextEmployeeMi using partial update
        NextEmployeeMi partialUpdatedNextEmployeeMi = new NextEmployeeMi();
        partialUpdatedNextEmployeeMi.setId(nextEmployeeMi.getId());

        partialUpdatedNextEmployeeMi.position(UPDATED_POSITION);

        restNextEmployeeMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextEmployeeMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextEmployeeMi))
            )
            .andExpect(status().isOk());

        // Validate the NextEmployeeMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextEmployeeMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextEmployeeMi, nextEmployeeMi),
            getPersistedNextEmployeeMi(nextEmployeeMi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextEmployeeMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextEmployeeMi = nextEmployeeMiRepository.saveAndFlush(nextEmployeeMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextEmployeeMi using partial update
        NextEmployeeMi partialUpdatedNextEmployeeMi = new NextEmployeeMi();
        partialUpdatedNextEmployeeMi.setId(nextEmployeeMi.getId());

        partialUpdatedNextEmployeeMi
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);

        restNextEmployeeMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextEmployeeMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextEmployeeMi))
            )
            .andExpect(status().isOk());

        // Validate the NextEmployeeMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextEmployeeMiUpdatableFieldsEquals(partialUpdatedNextEmployeeMi, getPersistedNextEmployeeMi(partialUpdatedNextEmployeeMi));
    }

    @Test
    @Transactional
    void patchNonExistingNextEmployeeMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeMi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextEmployeeMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextEmployeeMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextEmployeeMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextEmployeeMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextEmployeeMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextEmployeeMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextEmployeeMi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextEmployeeMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextEmployeeMi() throws Exception {
        // Initialize the database
        insertedNextEmployeeMi = nextEmployeeMiRepository.saveAndFlush(nextEmployeeMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextEmployeeMi
        restNextEmployeeMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextEmployeeMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextEmployeeMiRepository.count();
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

    protected NextEmployeeMi getPersistedNextEmployeeMi(NextEmployeeMi nextEmployeeMi) {
        return nextEmployeeMiRepository.findById(nextEmployeeMi.getId()).orElseThrow();
    }

    protected void assertPersistedNextEmployeeMiToMatchAllProperties(NextEmployeeMi expectedNextEmployeeMi) {
        assertNextEmployeeMiAllPropertiesEquals(expectedNextEmployeeMi, getPersistedNextEmployeeMi(expectedNextEmployeeMi));
    }

    protected void assertPersistedNextEmployeeMiToMatchUpdatableProperties(NextEmployeeMi expectedNextEmployeeMi) {
        assertNextEmployeeMiAllUpdatablePropertiesEquals(expectedNextEmployeeMi, getPersistedNextEmployeeMi(expectedNextEmployeeMi));
    }
}
