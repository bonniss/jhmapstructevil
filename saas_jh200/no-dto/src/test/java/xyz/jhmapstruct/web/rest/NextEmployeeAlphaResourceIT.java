package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextEmployeeAlphaAsserts.*;
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
import xyz.jhmapstruct.domain.NextEmployeeAlpha;
import xyz.jhmapstruct.repository.NextEmployeeAlphaRepository;

/**
 * Integration tests for the {@link NextEmployeeAlphaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextEmployeeAlphaResourceIT {

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

    private static final String ENTITY_API_URL = "/api/next-employee-alphas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextEmployeeAlphaRepository nextEmployeeAlphaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextEmployeeAlphaMockMvc;

    private NextEmployeeAlpha nextEmployeeAlpha;

    private NextEmployeeAlpha insertedNextEmployeeAlpha;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextEmployeeAlpha createEntity() {
        return new NextEmployeeAlpha()
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
    public static NextEmployeeAlpha createUpdatedEntity() {
        return new NextEmployeeAlpha()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);
    }

    @BeforeEach
    public void initTest() {
        nextEmployeeAlpha = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextEmployeeAlpha != null) {
            nextEmployeeAlphaRepository.delete(insertedNextEmployeeAlpha);
            insertedNextEmployeeAlpha = null;
        }
    }

    @Test
    @Transactional
    void createNextEmployeeAlpha() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextEmployeeAlpha
        var returnedNextEmployeeAlpha = om.readValue(
            restNextEmployeeAlphaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeAlpha)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextEmployeeAlpha.class
        );

        // Validate the NextEmployeeAlpha in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextEmployeeAlphaUpdatableFieldsEquals(returnedNextEmployeeAlpha, getPersistedNextEmployeeAlpha(returnedNextEmployeeAlpha));

        insertedNextEmployeeAlpha = returnedNextEmployeeAlpha;
    }

    @Test
    @Transactional
    void createNextEmployeeAlphaWithExistingId() throws Exception {
        // Create the NextEmployeeAlpha with an existing ID
        nextEmployeeAlpha.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextEmployeeAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeAlpha)))
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextEmployeeAlpha.setFirstName(null);

        // Create the NextEmployeeAlpha, which fails.

        restNextEmployeeAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeAlpha)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextEmployeeAlpha.setLastName(null);

        // Create the NextEmployeeAlpha, which fails.

        restNextEmployeeAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeAlpha)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextEmployeeAlpha.setEmail(null);

        // Create the NextEmployeeAlpha, which fails.

        restNextEmployeeAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeAlpha)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextEmployeeAlphas() throws Exception {
        // Initialize the database
        insertedNextEmployeeAlpha = nextEmployeeAlphaRepository.saveAndFlush(nextEmployeeAlpha);

        // Get all the nextEmployeeAlphaList
        restNextEmployeeAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextEmployeeAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));
    }

    @Test
    @Transactional
    void getNextEmployeeAlpha() throws Exception {
        // Initialize the database
        insertedNextEmployeeAlpha = nextEmployeeAlphaRepository.saveAndFlush(nextEmployeeAlpha);

        // Get the nextEmployeeAlpha
        restNextEmployeeAlphaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextEmployeeAlpha.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextEmployeeAlpha.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.hireDate").value(DEFAULT_HIRE_DATE.toString()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION));
    }

    @Test
    @Transactional
    void getNextEmployeeAlphasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextEmployeeAlpha = nextEmployeeAlphaRepository.saveAndFlush(nextEmployeeAlpha);

        Long id = nextEmployeeAlpha.getId();

        defaultNextEmployeeAlphaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextEmployeeAlphaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextEmployeeAlphaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextEmployeeAlphasByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeAlpha = nextEmployeeAlphaRepository.saveAndFlush(nextEmployeeAlpha);

        // Get all the nextEmployeeAlphaList where firstName equals to
        defaultNextEmployeeAlphaFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeAlphasByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeAlpha = nextEmployeeAlphaRepository.saveAndFlush(nextEmployeeAlpha);

        // Get all the nextEmployeeAlphaList where firstName in
        defaultNextEmployeeAlphaFiltering(
            "firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME,
            "firstName.in=" + UPDATED_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllNextEmployeeAlphasByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeAlpha = nextEmployeeAlphaRepository.saveAndFlush(nextEmployeeAlpha);

        // Get all the nextEmployeeAlphaList where firstName is not null
        defaultNextEmployeeAlphaFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeAlphasByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeAlpha = nextEmployeeAlphaRepository.saveAndFlush(nextEmployeeAlpha);

        // Get all the nextEmployeeAlphaList where firstName contains
        defaultNextEmployeeAlphaFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeAlphasByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeAlpha = nextEmployeeAlphaRepository.saveAndFlush(nextEmployeeAlpha);

        // Get all the nextEmployeeAlphaList where firstName does not contain
        defaultNextEmployeeAlphaFiltering(
            "firstName.doesNotContain=" + UPDATED_FIRST_NAME,
            "firstName.doesNotContain=" + DEFAULT_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllNextEmployeeAlphasByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeAlpha = nextEmployeeAlphaRepository.saveAndFlush(nextEmployeeAlpha);

        // Get all the nextEmployeeAlphaList where lastName equals to
        defaultNextEmployeeAlphaFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeAlphasByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeAlpha = nextEmployeeAlphaRepository.saveAndFlush(nextEmployeeAlpha);

        // Get all the nextEmployeeAlphaList where lastName in
        defaultNextEmployeeAlphaFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeAlphasByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeAlpha = nextEmployeeAlphaRepository.saveAndFlush(nextEmployeeAlpha);

        // Get all the nextEmployeeAlphaList where lastName is not null
        defaultNextEmployeeAlphaFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeAlphasByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeAlpha = nextEmployeeAlphaRepository.saveAndFlush(nextEmployeeAlpha);

        // Get all the nextEmployeeAlphaList where lastName contains
        defaultNextEmployeeAlphaFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeAlphasByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeAlpha = nextEmployeeAlphaRepository.saveAndFlush(nextEmployeeAlpha);

        // Get all the nextEmployeeAlphaList where lastName does not contain
        defaultNextEmployeeAlphaFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextEmployeeAlphasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeAlpha = nextEmployeeAlphaRepository.saveAndFlush(nextEmployeeAlpha);

        // Get all the nextEmployeeAlphaList where email equals to
        defaultNextEmployeeAlphaFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeeAlphasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeAlpha = nextEmployeeAlphaRepository.saveAndFlush(nextEmployeeAlpha);

        // Get all the nextEmployeeAlphaList where email in
        defaultNextEmployeeAlphaFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeeAlphasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeAlpha = nextEmployeeAlphaRepository.saveAndFlush(nextEmployeeAlpha);

        // Get all the nextEmployeeAlphaList where email is not null
        defaultNextEmployeeAlphaFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeAlphasByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeAlpha = nextEmployeeAlphaRepository.saveAndFlush(nextEmployeeAlpha);

        // Get all the nextEmployeeAlphaList where email contains
        defaultNextEmployeeAlphaFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeeAlphasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeAlpha = nextEmployeeAlphaRepository.saveAndFlush(nextEmployeeAlpha);

        // Get all the nextEmployeeAlphaList where email does not contain
        defaultNextEmployeeAlphaFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextEmployeeAlphasByHireDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeAlpha = nextEmployeeAlphaRepository.saveAndFlush(nextEmployeeAlpha);

        // Get all the nextEmployeeAlphaList where hireDate equals to
        defaultNextEmployeeAlphaFiltering("hireDate.equals=" + DEFAULT_HIRE_DATE, "hireDate.equals=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllNextEmployeeAlphasByHireDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeAlpha = nextEmployeeAlphaRepository.saveAndFlush(nextEmployeeAlpha);

        // Get all the nextEmployeeAlphaList where hireDate in
        defaultNextEmployeeAlphaFiltering("hireDate.in=" + DEFAULT_HIRE_DATE + "," + UPDATED_HIRE_DATE, "hireDate.in=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllNextEmployeeAlphasByHireDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeAlpha = nextEmployeeAlphaRepository.saveAndFlush(nextEmployeeAlpha);

        // Get all the nextEmployeeAlphaList where hireDate is not null
        defaultNextEmployeeAlphaFiltering("hireDate.specified=true", "hireDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeAlphasByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeAlpha = nextEmployeeAlphaRepository.saveAndFlush(nextEmployeeAlpha);

        // Get all the nextEmployeeAlphaList where position equals to
        defaultNextEmployeeAlphaFiltering("position.equals=" + DEFAULT_POSITION, "position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeeAlphasByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextEmployeeAlpha = nextEmployeeAlphaRepository.saveAndFlush(nextEmployeeAlpha);

        // Get all the nextEmployeeAlphaList where position in
        defaultNextEmployeeAlphaFiltering("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION, "position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeeAlphasByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextEmployeeAlpha = nextEmployeeAlphaRepository.saveAndFlush(nextEmployeeAlpha);

        // Get all the nextEmployeeAlphaList where position is not null
        defaultNextEmployeeAlphaFiltering("position.specified=true", "position.specified=false");
    }

    @Test
    @Transactional
    void getAllNextEmployeeAlphasByPositionContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeAlpha = nextEmployeeAlphaRepository.saveAndFlush(nextEmployeeAlpha);

        // Get all the nextEmployeeAlphaList where position contains
        defaultNextEmployeeAlphaFiltering("position.contains=" + DEFAULT_POSITION, "position.contains=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeeAlphasByPositionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextEmployeeAlpha = nextEmployeeAlphaRepository.saveAndFlush(nextEmployeeAlpha);

        // Get all the nextEmployeeAlphaList where position does not contain
        defaultNextEmployeeAlphaFiltering("position.doesNotContain=" + UPDATED_POSITION, "position.doesNotContain=" + DEFAULT_POSITION);
    }

    @Test
    @Transactional
    void getAllNextEmployeeAlphasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextEmployeeAlphaRepository.saveAndFlush(nextEmployeeAlpha);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextEmployeeAlpha.setTenant(tenant);
        nextEmployeeAlphaRepository.saveAndFlush(nextEmployeeAlpha);
        Long tenantId = tenant.getId();
        // Get all the nextEmployeeAlphaList where tenant equals to tenantId
        defaultNextEmployeeAlphaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextEmployeeAlphaList where tenant equals to (tenantId + 1)
        defaultNextEmployeeAlphaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextEmployeeAlphaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextEmployeeAlphaShouldBeFound(shouldBeFound);
        defaultNextEmployeeAlphaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextEmployeeAlphaShouldBeFound(String filter) throws Exception {
        restNextEmployeeAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextEmployeeAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));

        // Check, that the count call also returns 1
        restNextEmployeeAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextEmployeeAlphaShouldNotBeFound(String filter) throws Exception {
        restNextEmployeeAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextEmployeeAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextEmployeeAlpha() throws Exception {
        // Get the nextEmployeeAlpha
        restNextEmployeeAlphaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextEmployeeAlpha() throws Exception {
        // Initialize the database
        insertedNextEmployeeAlpha = nextEmployeeAlphaRepository.saveAndFlush(nextEmployeeAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextEmployeeAlpha
        NextEmployeeAlpha updatedNextEmployeeAlpha = nextEmployeeAlphaRepository.findById(nextEmployeeAlpha.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextEmployeeAlpha are not directly saved in db
        em.detach(updatedNextEmployeeAlpha);
        updatedNextEmployeeAlpha
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);

        restNextEmployeeAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextEmployeeAlpha.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextEmployeeAlpha))
            )
            .andExpect(status().isOk());

        // Validate the NextEmployeeAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextEmployeeAlphaToMatchAllProperties(updatedNextEmployeeAlpha);
    }

    @Test
    @Transactional
    void putNonExistingNextEmployeeAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeAlpha.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextEmployeeAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextEmployeeAlpha.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextEmployeeAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextEmployeeAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextEmployeeAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextEmployeeAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeAlphaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextEmployeeAlpha)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextEmployeeAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextEmployeeAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedNextEmployeeAlpha = nextEmployeeAlphaRepository.saveAndFlush(nextEmployeeAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextEmployeeAlpha using partial update
        NextEmployeeAlpha partialUpdatedNextEmployeeAlpha = new NextEmployeeAlpha();
        partialUpdatedNextEmployeeAlpha.setId(nextEmployeeAlpha.getId());

        partialUpdatedNextEmployeeAlpha.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME).position(UPDATED_POSITION);

        restNextEmployeeAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextEmployeeAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextEmployeeAlpha))
            )
            .andExpect(status().isOk());

        // Validate the NextEmployeeAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextEmployeeAlphaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextEmployeeAlpha, nextEmployeeAlpha),
            getPersistedNextEmployeeAlpha(nextEmployeeAlpha)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextEmployeeAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedNextEmployeeAlpha = nextEmployeeAlphaRepository.saveAndFlush(nextEmployeeAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextEmployeeAlpha using partial update
        NextEmployeeAlpha partialUpdatedNextEmployeeAlpha = new NextEmployeeAlpha();
        partialUpdatedNextEmployeeAlpha.setId(nextEmployeeAlpha.getId());

        partialUpdatedNextEmployeeAlpha
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);

        restNextEmployeeAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextEmployeeAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextEmployeeAlpha))
            )
            .andExpect(status().isOk());

        // Validate the NextEmployeeAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextEmployeeAlphaUpdatableFieldsEquals(
            partialUpdatedNextEmployeeAlpha,
            getPersistedNextEmployeeAlpha(partialUpdatedNextEmployeeAlpha)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextEmployeeAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeAlpha.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextEmployeeAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextEmployeeAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextEmployeeAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextEmployeeAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextEmployeeAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextEmployeeAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextEmployeeAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextEmployeeAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextEmployeeAlphaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextEmployeeAlpha)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextEmployeeAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextEmployeeAlpha() throws Exception {
        // Initialize the database
        insertedNextEmployeeAlpha = nextEmployeeAlphaRepository.saveAndFlush(nextEmployeeAlpha);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextEmployeeAlpha
        restNextEmployeeAlphaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextEmployeeAlpha.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextEmployeeAlphaRepository.count();
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

    protected NextEmployeeAlpha getPersistedNextEmployeeAlpha(NextEmployeeAlpha nextEmployeeAlpha) {
        return nextEmployeeAlphaRepository.findById(nextEmployeeAlpha.getId()).orElseThrow();
    }

    protected void assertPersistedNextEmployeeAlphaToMatchAllProperties(NextEmployeeAlpha expectedNextEmployeeAlpha) {
        assertNextEmployeeAlphaAllPropertiesEquals(expectedNextEmployeeAlpha, getPersistedNextEmployeeAlpha(expectedNextEmployeeAlpha));
    }

    protected void assertPersistedNextEmployeeAlphaToMatchUpdatableProperties(NextEmployeeAlpha expectedNextEmployeeAlpha) {
        assertNextEmployeeAlphaAllUpdatablePropertiesEquals(
            expectedNextEmployeeAlpha,
            getPersistedNextEmployeeAlpha(expectedNextEmployeeAlpha)
        );
    }
}
