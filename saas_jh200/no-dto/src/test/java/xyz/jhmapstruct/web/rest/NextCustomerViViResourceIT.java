package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextCustomerViViAsserts.*;
import static xyz.jhmapstruct.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
import xyz.jhmapstruct.domain.NextCustomerViVi;
import xyz.jhmapstruct.repository.NextCustomerViViRepository;

/**
 * Integration tests for the {@link NextCustomerViViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextCustomerViViResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/next-customer-vi-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextCustomerViViRepository nextCustomerViViRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextCustomerViViMockMvc;

    private NextCustomerViVi nextCustomerViVi;

    private NextCustomerViVi insertedNextCustomerViVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextCustomerViVi createEntity() {
        return new NextCustomerViVi()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextCustomerViVi createUpdatedEntity() {
        return new NextCustomerViVi()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        nextCustomerViVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextCustomerViVi != null) {
            nextCustomerViViRepository.delete(insertedNextCustomerViVi);
            insertedNextCustomerViVi = null;
        }
    }

    @Test
    @Transactional
    void createNextCustomerViVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextCustomerViVi
        var returnedNextCustomerViVi = om.readValue(
            restNextCustomerViViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerViVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextCustomerViVi.class
        );

        // Validate the NextCustomerViVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextCustomerViViUpdatableFieldsEquals(returnedNextCustomerViVi, getPersistedNextCustomerViVi(returnedNextCustomerViVi));

        insertedNextCustomerViVi = returnedNextCustomerViVi;
    }

    @Test
    @Transactional
    void createNextCustomerViViWithExistingId() throws Exception {
        // Create the NextCustomerViVi with an existing ID
        nextCustomerViVi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextCustomerViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerViVi)))
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCustomerViVi.setFirstName(null);

        // Create the NextCustomerViVi, which fails.

        restNextCustomerViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerViVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCustomerViVi.setLastName(null);

        // Create the NextCustomerViVi, which fails.

        restNextCustomerViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerViVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCustomerViVi.setEmail(null);

        // Create the NextCustomerViVi, which fails.

        restNextCustomerViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerViVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextCustomerViVis() throws Exception {
        // Initialize the database
        insertedNextCustomerViVi = nextCustomerViViRepository.saveAndFlush(nextCustomerViVi);

        // Get all the nextCustomerViViList
        restNextCustomerViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCustomerViVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @Test
    @Transactional
    void getNextCustomerViVi() throws Exception {
        // Initialize the database
        insertedNextCustomerViVi = nextCustomerViViRepository.saveAndFlush(nextCustomerViVi);

        // Get the nextCustomerViVi
        restNextCustomerViViMockMvc
            .perform(get(ENTITY_API_URL_ID, nextCustomerViVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextCustomerViVi.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getNextCustomerViVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextCustomerViVi = nextCustomerViViRepository.saveAndFlush(nextCustomerViVi);

        Long id = nextCustomerViVi.getId();

        defaultNextCustomerViViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextCustomerViViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextCustomerViViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextCustomerViVisByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerViVi = nextCustomerViViRepository.saveAndFlush(nextCustomerViVi);

        // Get all the nextCustomerViViList where firstName equals to
        defaultNextCustomerViViFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerViVisByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomerViVi = nextCustomerViViRepository.saveAndFlush(nextCustomerViVi);

        // Get all the nextCustomerViViList where firstName in
        defaultNextCustomerViViFiltering(
            "firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME,
            "firstName.in=" + UPDATED_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllNextCustomerViVisByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomerViVi = nextCustomerViViRepository.saveAndFlush(nextCustomerViVi);

        // Get all the nextCustomerViViList where firstName is not null
        defaultNextCustomerViViFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomerViVisByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerViVi = nextCustomerViViRepository.saveAndFlush(nextCustomerViVi);

        // Get all the nextCustomerViViList where firstName contains
        defaultNextCustomerViViFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerViVisByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerViVi = nextCustomerViViRepository.saveAndFlush(nextCustomerViVi);

        // Get all the nextCustomerViViList where firstName does not contain
        defaultNextCustomerViViFiltering(
            "firstName.doesNotContain=" + UPDATED_FIRST_NAME,
            "firstName.doesNotContain=" + DEFAULT_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllNextCustomerViVisByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerViVi = nextCustomerViViRepository.saveAndFlush(nextCustomerViVi);

        // Get all the nextCustomerViViList where lastName equals to
        defaultNextCustomerViViFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerViVisByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomerViVi = nextCustomerViViRepository.saveAndFlush(nextCustomerViVi);

        // Get all the nextCustomerViViList where lastName in
        defaultNextCustomerViViFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerViVisByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomerViVi = nextCustomerViViRepository.saveAndFlush(nextCustomerViVi);

        // Get all the nextCustomerViViList where lastName is not null
        defaultNextCustomerViViFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomerViVisByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerViVi = nextCustomerViViRepository.saveAndFlush(nextCustomerViVi);

        // Get all the nextCustomerViViList where lastName contains
        defaultNextCustomerViViFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerViVisByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerViVi = nextCustomerViViRepository.saveAndFlush(nextCustomerViVi);

        // Get all the nextCustomerViViList where lastName does not contain
        defaultNextCustomerViViFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerViVisByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerViVi = nextCustomerViViRepository.saveAndFlush(nextCustomerViVi);

        // Get all the nextCustomerViViList where email equals to
        defaultNextCustomerViViFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomerViVisByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomerViVi = nextCustomerViViRepository.saveAndFlush(nextCustomerViVi);

        // Get all the nextCustomerViViList where email in
        defaultNextCustomerViViFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomerViVisByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomerViVi = nextCustomerViViRepository.saveAndFlush(nextCustomerViVi);

        // Get all the nextCustomerViViList where email is not null
        defaultNextCustomerViViFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomerViVisByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerViVi = nextCustomerViViRepository.saveAndFlush(nextCustomerViVi);

        // Get all the nextCustomerViViList where email contains
        defaultNextCustomerViViFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomerViVisByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerViVi = nextCustomerViViRepository.saveAndFlush(nextCustomerViVi);

        // Get all the nextCustomerViViList where email does not contain
        defaultNextCustomerViViFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomerViVisByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerViVi = nextCustomerViViRepository.saveAndFlush(nextCustomerViVi);

        // Get all the nextCustomerViViList where phoneNumber equals to
        defaultNextCustomerViViFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextCustomerViVisByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomerViVi = nextCustomerViViRepository.saveAndFlush(nextCustomerViVi);

        // Get all the nextCustomerViViList where phoneNumber in
        defaultNextCustomerViViFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextCustomerViVisByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomerViVi = nextCustomerViViRepository.saveAndFlush(nextCustomerViVi);

        // Get all the nextCustomerViViList where phoneNumber is not null
        defaultNextCustomerViViFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomerViVisByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerViVi = nextCustomerViViRepository.saveAndFlush(nextCustomerViVi);

        // Get all the nextCustomerViViList where phoneNumber contains
        defaultNextCustomerViViFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextCustomerViVisByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerViVi = nextCustomerViViRepository.saveAndFlush(nextCustomerViVi);

        // Get all the nextCustomerViViList where phoneNumber does not contain
        defaultNextCustomerViViFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextCustomerViVisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextCustomerViViRepository.saveAndFlush(nextCustomerViVi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextCustomerViVi.setTenant(tenant);
        nextCustomerViViRepository.saveAndFlush(nextCustomerViVi);
        Long tenantId = tenant.getId();
        // Get all the nextCustomerViViList where tenant equals to tenantId
        defaultNextCustomerViViShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextCustomerViViList where tenant equals to (tenantId + 1)
        defaultNextCustomerViViShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextCustomerViViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextCustomerViViShouldBeFound(shouldBeFound);
        defaultNextCustomerViViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextCustomerViViShouldBeFound(String filter) throws Exception {
        restNextCustomerViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCustomerViVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restNextCustomerViViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextCustomerViViShouldNotBeFound(String filter) throws Exception {
        restNextCustomerViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextCustomerViViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextCustomerViVi() throws Exception {
        // Get the nextCustomerViVi
        restNextCustomerViViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextCustomerViVi() throws Exception {
        // Initialize the database
        insertedNextCustomerViVi = nextCustomerViViRepository.saveAndFlush(nextCustomerViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCustomerViVi
        NextCustomerViVi updatedNextCustomerViVi = nextCustomerViViRepository.findById(nextCustomerViVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextCustomerViVi are not directly saved in db
        em.detach(updatedNextCustomerViVi);
        updatedNextCustomerViVi
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restNextCustomerViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextCustomerViVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextCustomerViVi))
            )
            .andExpect(status().isOk());

        // Validate the NextCustomerViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextCustomerViViToMatchAllProperties(updatedNextCustomerViVi);
    }

    @Test
    @Transactional
    void putNonExistingNextCustomerViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerViVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCustomerViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextCustomerViVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCustomerViVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextCustomerViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerViVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCustomerViVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextCustomerViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerViVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerViViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerViVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCustomerViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextCustomerViViWithPatch() throws Exception {
        // Initialize the database
        insertedNextCustomerViVi = nextCustomerViViRepository.saveAndFlush(nextCustomerViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCustomerViVi using partial update
        NextCustomerViVi partialUpdatedNextCustomerViVi = new NextCustomerViVi();
        partialUpdatedNextCustomerViVi.setId(nextCustomerViVi.getId());

        partialUpdatedNextCustomerViVi.lastName(UPDATED_LAST_NAME).phoneNumber(UPDATED_PHONE_NUMBER);

        restNextCustomerViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCustomerViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCustomerViVi))
            )
            .andExpect(status().isOk());

        // Validate the NextCustomerViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCustomerViViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextCustomerViVi, nextCustomerViVi),
            getPersistedNextCustomerViVi(nextCustomerViVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextCustomerViViWithPatch() throws Exception {
        // Initialize the database
        insertedNextCustomerViVi = nextCustomerViViRepository.saveAndFlush(nextCustomerViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCustomerViVi using partial update
        NextCustomerViVi partialUpdatedNextCustomerViVi = new NextCustomerViVi();
        partialUpdatedNextCustomerViVi.setId(nextCustomerViVi.getId());

        partialUpdatedNextCustomerViVi
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restNextCustomerViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCustomerViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCustomerViVi))
            )
            .andExpect(status().isOk());

        // Validate the NextCustomerViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCustomerViViUpdatableFieldsEquals(
            partialUpdatedNextCustomerViVi,
            getPersistedNextCustomerViVi(partialUpdatedNextCustomerViVi)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextCustomerViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerViVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCustomerViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextCustomerViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCustomerViVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextCustomerViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerViVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCustomerViVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextCustomerViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerViVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerViViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextCustomerViVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCustomerViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextCustomerViVi() throws Exception {
        // Initialize the database
        insertedNextCustomerViVi = nextCustomerViViRepository.saveAndFlush(nextCustomerViVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextCustomerViVi
        restNextCustomerViViMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextCustomerViVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextCustomerViViRepository.count();
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

    protected NextCustomerViVi getPersistedNextCustomerViVi(NextCustomerViVi nextCustomerViVi) {
        return nextCustomerViViRepository.findById(nextCustomerViVi.getId()).orElseThrow();
    }

    protected void assertPersistedNextCustomerViViToMatchAllProperties(NextCustomerViVi expectedNextCustomerViVi) {
        assertNextCustomerViViAllPropertiesEquals(expectedNextCustomerViVi, getPersistedNextCustomerViVi(expectedNextCustomerViVi));
    }

    protected void assertPersistedNextCustomerViViToMatchUpdatableProperties(NextCustomerViVi expectedNextCustomerViVi) {
        assertNextCustomerViViAllUpdatablePropertiesEquals(
            expectedNextCustomerViVi,
            getPersistedNextCustomerViVi(expectedNextCustomerViVi)
        );
    }
}
