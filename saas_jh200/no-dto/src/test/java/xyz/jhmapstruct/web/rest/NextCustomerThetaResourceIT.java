package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextCustomerThetaAsserts.*;
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
import xyz.jhmapstruct.domain.NextCustomerTheta;
import xyz.jhmapstruct.repository.NextCustomerThetaRepository;

/**
 * Integration tests for the {@link NextCustomerThetaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextCustomerThetaResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/next-customer-thetas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextCustomerThetaRepository nextCustomerThetaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextCustomerThetaMockMvc;

    private NextCustomerTheta nextCustomerTheta;

    private NextCustomerTheta insertedNextCustomerTheta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextCustomerTheta createEntity() {
        return new NextCustomerTheta()
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
    public static NextCustomerTheta createUpdatedEntity() {
        return new NextCustomerTheta()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        nextCustomerTheta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextCustomerTheta != null) {
            nextCustomerThetaRepository.delete(insertedNextCustomerTheta);
            insertedNextCustomerTheta = null;
        }
    }

    @Test
    @Transactional
    void createNextCustomerTheta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextCustomerTheta
        var returnedNextCustomerTheta = om.readValue(
            restNextCustomerThetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerTheta)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextCustomerTheta.class
        );

        // Validate the NextCustomerTheta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextCustomerThetaUpdatableFieldsEquals(returnedNextCustomerTheta, getPersistedNextCustomerTheta(returnedNextCustomerTheta));

        insertedNextCustomerTheta = returnedNextCustomerTheta;
    }

    @Test
    @Transactional
    void createNextCustomerThetaWithExistingId() throws Exception {
        // Create the NextCustomerTheta with an existing ID
        nextCustomerTheta.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextCustomerThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerTheta)))
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCustomerTheta.setFirstName(null);

        // Create the NextCustomerTheta, which fails.

        restNextCustomerThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerTheta)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCustomerTheta.setLastName(null);

        // Create the NextCustomerTheta, which fails.

        restNextCustomerThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerTheta)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCustomerTheta.setEmail(null);

        // Create the NextCustomerTheta, which fails.

        restNextCustomerThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerTheta)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextCustomerThetas() throws Exception {
        // Initialize the database
        insertedNextCustomerTheta = nextCustomerThetaRepository.saveAndFlush(nextCustomerTheta);

        // Get all the nextCustomerThetaList
        restNextCustomerThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCustomerTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @Test
    @Transactional
    void getNextCustomerTheta() throws Exception {
        // Initialize the database
        insertedNextCustomerTheta = nextCustomerThetaRepository.saveAndFlush(nextCustomerTheta);

        // Get the nextCustomerTheta
        restNextCustomerThetaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextCustomerTheta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextCustomerTheta.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getNextCustomerThetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextCustomerTheta = nextCustomerThetaRepository.saveAndFlush(nextCustomerTheta);

        Long id = nextCustomerTheta.getId();

        defaultNextCustomerThetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextCustomerThetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextCustomerThetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextCustomerThetasByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerTheta = nextCustomerThetaRepository.saveAndFlush(nextCustomerTheta);

        // Get all the nextCustomerThetaList where firstName equals to
        defaultNextCustomerThetaFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerThetasByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomerTheta = nextCustomerThetaRepository.saveAndFlush(nextCustomerTheta);

        // Get all the nextCustomerThetaList where firstName in
        defaultNextCustomerThetaFiltering(
            "firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME,
            "firstName.in=" + UPDATED_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllNextCustomerThetasByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomerTheta = nextCustomerThetaRepository.saveAndFlush(nextCustomerTheta);

        // Get all the nextCustomerThetaList where firstName is not null
        defaultNextCustomerThetaFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomerThetasByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerTheta = nextCustomerThetaRepository.saveAndFlush(nextCustomerTheta);

        // Get all the nextCustomerThetaList where firstName contains
        defaultNextCustomerThetaFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerThetasByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerTheta = nextCustomerThetaRepository.saveAndFlush(nextCustomerTheta);

        // Get all the nextCustomerThetaList where firstName does not contain
        defaultNextCustomerThetaFiltering(
            "firstName.doesNotContain=" + UPDATED_FIRST_NAME,
            "firstName.doesNotContain=" + DEFAULT_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllNextCustomerThetasByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerTheta = nextCustomerThetaRepository.saveAndFlush(nextCustomerTheta);

        // Get all the nextCustomerThetaList where lastName equals to
        defaultNextCustomerThetaFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerThetasByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomerTheta = nextCustomerThetaRepository.saveAndFlush(nextCustomerTheta);

        // Get all the nextCustomerThetaList where lastName in
        defaultNextCustomerThetaFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerThetasByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomerTheta = nextCustomerThetaRepository.saveAndFlush(nextCustomerTheta);

        // Get all the nextCustomerThetaList where lastName is not null
        defaultNextCustomerThetaFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomerThetasByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerTheta = nextCustomerThetaRepository.saveAndFlush(nextCustomerTheta);

        // Get all the nextCustomerThetaList where lastName contains
        defaultNextCustomerThetaFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerThetasByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerTheta = nextCustomerThetaRepository.saveAndFlush(nextCustomerTheta);

        // Get all the nextCustomerThetaList where lastName does not contain
        defaultNextCustomerThetaFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerThetasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerTheta = nextCustomerThetaRepository.saveAndFlush(nextCustomerTheta);

        // Get all the nextCustomerThetaList where email equals to
        defaultNextCustomerThetaFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomerThetasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomerTheta = nextCustomerThetaRepository.saveAndFlush(nextCustomerTheta);

        // Get all the nextCustomerThetaList where email in
        defaultNextCustomerThetaFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomerThetasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomerTheta = nextCustomerThetaRepository.saveAndFlush(nextCustomerTheta);

        // Get all the nextCustomerThetaList where email is not null
        defaultNextCustomerThetaFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomerThetasByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerTheta = nextCustomerThetaRepository.saveAndFlush(nextCustomerTheta);

        // Get all the nextCustomerThetaList where email contains
        defaultNextCustomerThetaFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomerThetasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerTheta = nextCustomerThetaRepository.saveAndFlush(nextCustomerTheta);

        // Get all the nextCustomerThetaList where email does not contain
        defaultNextCustomerThetaFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomerThetasByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerTheta = nextCustomerThetaRepository.saveAndFlush(nextCustomerTheta);

        // Get all the nextCustomerThetaList where phoneNumber equals to
        defaultNextCustomerThetaFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextCustomerThetasByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomerTheta = nextCustomerThetaRepository.saveAndFlush(nextCustomerTheta);

        // Get all the nextCustomerThetaList where phoneNumber in
        defaultNextCustomerThetaFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextCustomerThetasByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomerTheta = nextCustomerThetaRepository.saveAndFlush(nextCustomerTheta);

        // Get all the nextCustomerThetaList where phoneNumber is not null
        defaultNextCustomerThetaFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomerThetasByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerTheta = nextCustomerThetaRepository.saveAndFlush(nextCustomerTheta);

        // Get all the nextCustomerThetaList where phoneNumber contains
        defaultNextCustomerThetaFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextCustomerThetasByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerTheta = nextCustomerThetaRepository.saveAndFlush(nextCustomerTheta);

        // Get all the nextCustomerThetaList where phoneNumber does not contain
        defaultNextCustomerThetaFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextCustomerThetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextCustomerThetaRepository.saveAndFlush(nextCustomerTheta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextCustomerTheta.setTenant(tenant);
        nextCustomerThetaRepository.saveAndFlush(nextCustomerTheta);
        Long tenantId = tenant.getId();
        // Get all the nextCustomerThetaList where tenant equals to tenantId
        defaultNextCustomerThetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextCustomerThetaList where tenant equals to (tenantId + 1)
        defaultNextCustomerThetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextCustomerThetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextCustomerThetaShouldBeFound(shouldBeFound);
        defaultNextCustomerThetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextCustomerThetaShouldBeFound(String filter) throws Exception {
        restNextCustomerThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCustomerTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restNextCustomerThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextCustomerThetaShouldNotBeFound(String filter) throws Exception {
        restNextCustomerThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextCustomerThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextCustomerTheta() throws Exception {
        // Get the nextCustomerTheta
        restNextCustomerThetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextCustomerTheta() throws Exception {
        // Initialize the database
        insertedNextCustomerTheta = nextCustomerThetaRepository.saveAndFlush(nextCustomerTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCustomerTheta
        NextCustomerTheta updatedNextCustomerTheta = nextCustomerThetaRepository.findById(nextCustomerTheta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextCustomerTheta are not directly saved in db
        em.detach(updatedNextCustomerTheta);
        updatedNextCustomerTheta
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restNextCustomerThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextCustomerTheta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextCustomerTheta))
            )
            .andExpect(status().isOk());

        // Validate the NextCustomerTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextCustomerThetaToMatchAllProperties(updatedNextCustomerTheta);
    }

    @Test
    @Transactional
    void putNonExistingNextCustomerTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerTheta.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCustomerThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextCustomerTheta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCustomerTheta))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextCustomerTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerTheta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCustomerTheta))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextCustomerTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerTheta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerThetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerTheta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCustomerTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextCustomerThetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextCustomerTheta = nextCustomerThetaRepository.saveAndFlush(nextCustomerTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCustomerTheta using partial update
        NextCustomerTheta partialUpdatedNextCustomerTheta = new NextCustomerTheta();
        partialUpdatedNextCustomerTheta.setId(nextCustomerTheta.getId());

        partialUpdatedNextCustomerTheta.firstName(UPDATED_FIRST_NAME).phoneNumber(UPDATED_PHONE_NUMBER);

        restNextCustomerThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCustomerTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCustomerTheta))
            )
            .andExpect(status().isOk());

        // Validate the NextCustomerTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCustomerThetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextCustomerTheta, nextCustomerTheta),
            getPersistedNextCustomerTheta(nextCustomerTheta)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextCustomerThetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextCustomerTheta = nextCustomerThetaRepository.saveAndFlush(nextCustomerTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCustomerTheta using partial update
        NextCustomerTheta partialUpdatedNextCustomerTheta = new NextCustomerTheta();
        partialUpdatedNextCustomerTheta.setId(nextCustomerTheta.getId());

        partialUpdatedNextCustomerTheta
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restNextCustomerThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCustomerTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCustomerTheta))
            )
            .andExpect(status().isOk());

        // Validate the NextCustomerTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCustomerThetaUpdatableFieldsEquals(
            partialUpdatedNextCustomerTheta,
            getPersistedNextCustomerTheta(partialUpdatedNextCustomerTheta)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextCustomerTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerTheta.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCustomerThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextCustomerTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCustomerTheta))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextCustomerTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerTheta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCustomerTheta))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextCustomerTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerTheta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerThetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextCustomerTheta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCustomerTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextCustomerTheta() throws Exception {
        // Initialize the database
        insertedNextCustomerTheta = nextCustomerThetaRepository.saveAndFlush(nextCustomerTheta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextCustomerTheta
        restNextCustomerThetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextCustomerTheta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextCustomerThetaRepository.count();
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

    protected NextCustomerTheta getPersistedNextCustomerTheta(NextCustomerTheta nextCustomerTheta) {
        return nextCustomerThetaRepository.findById(nextCustomerTheta.getId()).orElseThrow();
    }

    protected void assertPersistedNextCustomerThetaToMatchAllProperties(NextCustomerTheta expectedNextCustomerTheta) {
        assertNextCustomerThetaAllPropertiesEquals(expectedNextCustomerTheta, getPersistedNextCustomerTheta(expectedNextCustomerTheta));
    }

    protected void assertPersistedNextCustomerThetaToMatchUpdatableProperties(NextCustomerTheta expectedNextCustomerTheta) {
        assertNextCustomerThetaAllUpdatablePropertiesEquals(
            expectedNextCustomerTheta,
            getPersistedNextCustomerTheta(expectedNextCustomerTheta)
        );
    }
}
