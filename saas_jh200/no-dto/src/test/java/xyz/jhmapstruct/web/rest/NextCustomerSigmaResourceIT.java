package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextCustomerSigmaAsserts.*;
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
import xyz.jhmapstruct.domain.NextCustomerSigma;
import xyz.jhmapstruct.repository.NextCustomerSigmaRepository;

/**
 * Integration tests for the {@link NextCustomerSigmaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextCustomerSigmaResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/next-customer-sigmas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextCustomerSigmaRepository nextCustomerSigmaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextCustomerSigmaMockMvc;

    private NextCustomerSigma nextCustomerSigma;

    private NextCustomerSigma insertedNextCustomerSigma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextCustomerSigma createEntity() {
        return new NextCustomerSigma()
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
    public static NextCustomerSigma createUpdatedEntity() {
        return new NextCustomerSigma()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        nextCustomerSigma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextCustomerSigma != null) {
            nextCustomerSigmaRepository.delete(insertedNextCustomerSigma);
            insertedNextCustomerSigma = null;
        }
    }

    @Test
    @Transactional
    void createNextCustomerSigma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextCustomerSigma
        var returnedNextCustomerSigma = om.readValue(
            restNextCustomerSigmaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerSigma)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextCustomerSigma.class
        );

        // Validate the NextCustomerSigma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextCustomerSigmaUpdatableFieldsEquals(returnedNextCustomerSigma, getPersistedNextCustomerSigma(returnedNextCustomerSigma));

        insertedNextCustomerSigma = returnedNextCustomerSigma;
    }

    @Test
    @Transactional
    void createNextCustomerSigmaWithExistingId() throws Exception {
        // Create the NextCustomerSigma with an existing ID
        nextCustomerSigma.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextCustomerSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerSigma)))
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCustomerSigma.setFirstName(null);

        // Create the NextCustomerSigma, which fails.

        restNextCustomerSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerSigma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCustomerSigma.setLastName(null);

        // Create the NextCustomerSigma, which fails.

        restNextCustomerSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerSigma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCustomerSigma.setEmail(null);

        // Create the NextCustomerSigma, which fails.

        restNextCustomerSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerSigma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextCustomerSigmas() throws Exception {
        // Initialize the database
        insertedNextCustomerSigma = nextCustomerSigmaRepository.saveAndFlush(nextCustomerSigma);

        // Get all the nextCustomerSigmaList
        restNextCustomerSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCustomerSigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @Test
    @Transactional
    void getNextCustomerSigma() throws Exception {
        // Initialize the database
        insertedNextCustomerSigma = nextCustomerSigmaRepository.saveAndFlush(nextCustomerSigma);

        // Get the nextCustomerSigma
        restNextCustomerSigmaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextCustomerSigma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextCustomerSigma.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getNextCustomerSigmasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextCustomerSigma = nextCustomerSigmaRepository.saveAndFlush(nextCustomerSigma);

        Long id = nextCustomerSigma.getId();

        defaultNextCustomerSigmaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextCustomerSigmaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextCustomerSigmaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextCustomerSigmasByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerSigma = nextCustomerSigmaRepository.saveAndFlush(nextCustomerSigma);

        // Get all the nextCustomerSigmaList where firstName equals to
        defaultNextCustomerSigmaFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerSigmasByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomerSigma = nextCustomerSigmaRepository.saveAndFlush(nextCustomerSigma);

        // Get all the nextCustomerSigmaList where firstName in
        defaultNextCustomerSigmaFiltering(
            "firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME,
            "firstName.in=" + UPDATED_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllNextCustomerSigmasByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomerSigma = nextCustomerSigmaRepository.saveAndFlush(nextCustomerSigma);

        // Get all the nextCustomerSigmaList where firstName is not null
        defaultNextCustomerSigmaFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomerSigmasByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerSigma = nextCustomerSigmaRepository.saveAndFlush(nextCustomerSigma);

        // Get all the nextCustomerSigmaList where firstName contains
        defaultNextCustomerSigmaFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerSigmasByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerSigma = nextCustomerSigmaRepository.saveAndFlush(nextCustomerSigma);

        // Get all the nextCustomerSigmaList where firstName does not contain
        defaultNextCustomerSigmaFiltering(
            "firstName.doesNotContain=" + UPDATED_FIRST_NAME,
            "firstName.doesNotContain=" + DEFAULT_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllNextCustomerSigmasByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerSigma = nextCustomerSigmaRepository.saveAndFlush(nextCustomerSigma);

        // Get all the nextCustomerSigmaList where lastName equals to
        defaultNextCustomerSigmaFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerSigmasByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomerSigma = nextCustomerSigmaRepository.saveAndFlush(nextCustomerSigma);

        // Get all the nextCustomerSigmaList where lastName in
        defaultNextCustomerSigmaFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerSigmasByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomerSigma = nextCustomerSigmaRepository.saveAndFlush(nextCustomerSigma);

        // Get all the nextCustomerSigmaList where lastName is not null
        defaultNextCustomerSigmaFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomerSigmasByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerSigma = nextCustomerSigmaRepository.saveAndFlush(nextCustomerSigma);

        // Get all the nextCustomerSigmaList where lastName contains
        defaultNextCustomerSigmaFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerSigmasByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerSigma = nextCustomerSigmaRepository.saveAndFlush(nextCustomerSigma);

        // Get all the nextCustomerSigmaList where lastName does not contain
        defaultNextCustomerSigmaFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerSigmasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerSigma = nextCustomerSigmaRepository.saveAndFlush(nextCustomerSigma);

        // Get all the nextCustomerSigmaList where email equals to
        defaultNextCustomerSigmaFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomerSigmasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomerSigma = nextCustomerSigmaRepository.saveAndFlush(nextCustomerSigma);

        // Get all the nextCustomerSigmaList where email in
        defaultNextCustomerSigmaFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomerSigmasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomerSigma = nextCustomerSigmaRepository.saveAndFlush(nextCustomerSigma);

        // Get all the nextCustomerSigmaList where email is not null
        defaultNextCustomerSigmaFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomerSigmasByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerSigma = nextCustomerSigmaRepository.saveAndFlush(nextCustomerSigma);

        // Get all the nextCustomerSigmaList where email contains
        defaultNextCustomerSigmaFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomerSigmasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerSigma = nextCustomerSigmaRepository.saveAndFlush(nextCustomerSigma);

        // Get all the nextCustomerSigmaList where email does not contain
        defaultNextCustomerSigmaFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomerSigmasByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerSigma = nextCustomerSigmaRepository.saveAndFlush(nextCustomerSigma);

        // Get all the nextCustomerSigmaList where phoneNumber equals to
        defaultNextCustomerSigmaFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextCustomerSigmasByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomerSigma = nextCustomerSigmaRepository.saveAndFlush(nextCustomerSigma);

        // Get all the nextCustomerSigmaList where phoneNumber in
        defaultNextCustomerSigmaFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextCustomerSigmasByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomerSigma = nextCustomerSigmaRepository.saveAndFlush(nextCustomerSigma);

        // Get all the nextCustomerSigmaList where phoneNumber is not null
        defaultNextCustomerSigmaFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomerSigmasByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerSigma = nextCustomerSigmaRepository.saveAndFlush(nextCustomerSigma);

        // Get all the nextCustomerSigmaList where phoneNumber contains
        defaultNextCustomerSigmaFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextCustomerSigmasByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerSigma = nextCustomerSigmaRepository.saveAndFlush(nextCustomerSigma);

        // Get all the nextCustomerSigmaList where phoneNumber does not contain
        defaultNextCustomerSigmaFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextCustomerSigmasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextCustomerSigmaRepository.saveAndFlush(nextCustomerSigma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextCustomerSigma.setTenant(tenant);
        nextCustomerSigmaRepository.saveAndFlush(nextCustomerSigma);
        Long tenantId = tenant.getId();
        // Get all the nextCustomerSigmaList where tenant equals to tenantId
        defaultNextCustomerSigmaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextCustomerSigmaList where tenant equals to (tenantId + 1)
        defaultNextCustomerSigmaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextCustomerSigmaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextCustomerSigmaShouldBeFound(shouldBeFound);
        defaultNextCustomerSigmaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextCustomerSigmaShouldBeFound(String filter) throws Exception {
        restNextCustomerSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCustomerSigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restNextCustomerSigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextCustomerSigmaShouldNotBeFound(String filter) throws Exception {
        restNextCustomerSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextCustomerSigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextCustomerSigma() throws Exception {
        // Get the nextCustomerSigma
        restNextCustomerSigmaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextCustomerSigma() throws Exception {
        // Initialize the database
        insertedNextCustomerSigma = nextCustomerSigmaRepository.saveAndFlush(nextCustomerSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCustomerSigma
        NextCustomerSigma updatedNextCustomerSigma = nextCustomerSigmaRepository.findById(nextCustomerSigma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextCustomerSigma are not directly saved in db
        em.detach(updatedNextCustomerSigma);
        updatedNextCustomerSigma
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restNextCustomerSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextCustomerSigma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextCustomerSigma))
            )
            .andExpect(status().isOk());

        // Validate the NextCustomerSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextCustomerSigmaToMatchAllProperties(updatedNextCustomerSigma);
    }

    @Test
    @Transactional
    void putNonExistingNextCustomerSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerSigma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCustomerSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextCustomerSigma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCustomerSigma))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextCustomerSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerSigma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCustomerSigma))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextCustomerSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerSigma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerSigmaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerSigma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCustomerSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextCustomerSigmaWithPatch() throws Exception {
        // Initialize the database
        insertedNextCustomerSigma = nextCustomerSigmaRepository.saveAndFlush(nextCustomerSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCustomerSigma using partial update
        NextCustomerSigma partialUpdatedNextCustomerSigma = new NextCustomerSigma();
        partialUpdatedNextCustomerSigma.setId(nextCustomerSigma.getId());

        partialUpdatedNextCustomerSigma.phoneNumber(UPDATED_PHONE_NUMBER);

        restNextCustomerSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCustomerSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCustomerSigma))
            )
            .andExpect(status().isOk());

        // Validate the NextCustomerSigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCustomerSigmaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextCustomerSigma, nextCustomerSigma),
            getPersistedNextCustomerSigma(nextCustomerSigma)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextCustomerSigmaWithPatch() throws Exception {
        // Initialize the database
        insertedNextCustomerSigma = nextCustomerSigmaRepository.saveAndFlush(nextCustomerSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCustomerSigma using partial update
        NextCustomerSigma partialUpdatedNextCustomerSigma = new NextCustomerSigma();
        partialUpdatedNextCustomerSigma.setId(nextCustomerSigma.getId());

        partialUpdatedNextCustomerSigma
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restNextCustomerSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCustomerSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCustomerSigma))
            )
            .andExpect(status().isOk());

        // Validate the NextCustomerSigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCustomerSigmaUpdatableFieldsEquals(
            partialUpdatedNextCustomerSigma,
            getPersistedNextCustomerSigma(partialUpdatedNextCustomerSigma)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextCustomerSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerSigma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCustomerSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextCustomerSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCustomerSigma))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextCustomerSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerSigma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCustomerSigma))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextCustomerSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerSigma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerSigmaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextCustomerSigma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCustomerSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextCustomerSigma() throws Exception {
        // Initialize the database
        insertedNextCustomerSigma = nextCustomerSigmaRepository.saveAndFlush(nextCustomerSigma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextCustomerSigma
        restNextCustomerSigmaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextCustomerSigma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextCustomerSigmaRepository.count();
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

    protected NextCustomerSigma getPersistedNextCustomerSigma(NextCustomerSigma nextCustomerSigma) {
        return nextCustomerSigmaRepository.findById(nextCustomerSigma.getId()).orElseThrow();
    }

    protected void assertPersistedNextCustomerSigmaToMatchAllProperties(NextCustomerSigma expectedNextCustomerSigma) {
        assertNextCustomerSigmaAllPropertiesEquals(expectedNextCustomerSigma, getPersistedNextCustomerSigma(expectedNextCustomerSigma));
    }

    protected void assertPersistedNextCustomerSigmaToMatchUpdatableProperties(NextCustomerSigma expectedNextCustomerSigma) {
        assertNextCustomerSigmaAllUpdatablePropertiesEquals(
            expectedNextCustomerSigma,
            getPersistedNextCustomerSigma(expectedNextCustomerSigma)
        );
    }
}
