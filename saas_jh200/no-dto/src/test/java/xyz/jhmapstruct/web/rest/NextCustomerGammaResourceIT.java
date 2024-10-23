package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextCustomerGammaAsserts.*;
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
import xyz.jhmapstruct.domain.NextCustomerGamma;
import xyz.jhmapstruct.repository.NextCustomerGammaRepository;

/**
 * Integration tests for the {@link NextCustomerGammaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextCustomerGammaResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/next-customer-gammas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextCustomerGammaRepository nextCustomerGammaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextCustomerGammaMockMvc;

    private NextCustomerGamma nextCustomerGamma;

    private NextCustomerGamma insertedNextCustomerGamma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextCustomerGamma createEntity() {
        return new NextCustomerGamma()
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
    public static NextCustomerGamma createUpdatedEntity() {
        return new NextCustomerGamma()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        nextCustomerGamma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextCustomerGamma != null) {
            nextCustomerGammaRepository.delete(insertedNextCustomerGamma);
            insertedNextCustomerGamma = null;
        }
    }

    @Test
    @Transactional
    void createNextCustomerGamma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextCustomerGamma
        var returnedNextCustomerGamma = om.readValue(
            restNextCustomerGammaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerGamma)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextCustomerGamma.class
        );

        // Validate the NextCustomerGamma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextCustomerGammaUpdatableFieldsEquals(returnedNextCustomerGamma, getPersistedNextCustomerGamma(returnedNextCustomerGamma));

        insertedNextCustomerGamma = returnedNextCustomerGamma;
    }

    @Test
    @Transactional
    void createNextCustomerGammaWithExistingId() throws Exception {
        // Create the NextCustomerGamma with an existing ID
        nextCustomerGamma.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextCustomerGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerGamma)))
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCustomerGamma.setFirstName(null);

        // Create the NextCustomerGamma, which fails.

        restNextCustomerGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerGamma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCustomerGamma.setLastName(null);

        // Create the NextCustomerGamma, which fails.

        restNextCustomerGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerGamma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCustomerGamma.setEmail(null);

        // Create the NextCustomerGamma, which fails.

        restNextCustomerGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerGamma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextCustomerGammas() throws Exception {
        // Initialize the database
        insertedNextCustomerGamma = nextCustomerGammaRepository.saveAndFlush(nextCustomerGamma);

        // Get all the nextCustomerGammaList
        restNextCustomerGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCustomerGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @Test
    @Transactional
    void getNextCustomerGamma() throws Exception {
        // Initialize the database
        insertedNextCustomerGamma = nextCustomerGammaRepository.saveAndFlush(nextCustomerGamma);

        // Get the nextCustomerGamma
        restNextCustomerGammaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextCustomerGamma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextCustomerGamma.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getNextCustomerGammasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextCustomerGamma = nextCustomerGammaRepository.saveAndFlush(nextCustomerGamma);

        Long id = nextCustomerGamma.getId();

        defaultNextCustomerGammaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextCustomerGammaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextCustomerGammaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextCustomerGammasByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerGamma = nextCustomerGammaRepository.saveAndFlush(nextCustomerGamma);

        // Get all the nextCustomerGammaList where firstName equals to
        defaultNextCustomerGammaFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerGammasByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomerGamma = nextCustomerGammaRepository.saveAndFlush(nextCustomerGamma);

        // Get all the nextCustomerGammaList where firstName in
        defaultNextCustomerGammaFiltering(
            "firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME,
            "firstName.in=" + UPDATED_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllNextCustomerGammasByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomerGamma = nextCustomerGammaRepository.saveAndFlush(nextCustomerGamma);

        // Get all the nextCustomerGammaList where firstName is not null
        defaultNextCustomerGammaFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomerGammasByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerGamma = nextCustomerGammaRepository.saveAndFlush(nextCustomerGamma);

        // Get all the nextCustomerGammaList where firstName contains
        defaultNextCustomerGammaFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerGammasByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerGamma = nextCustomerGammaRepository.saveAndFlush(nextCustomerGamma);

        // Get all the nextCustomerGammaList where firstName does not contain
        defaultNextCustomerGammaFiltering(
            "firstName.doesNotContain=" + UPDATED_FIRST_NAME,
            "firstName.doesNotContain=" + DEFAULT_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllNextCustomerGammasByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerGamma = nextCustomerGammaRepository.saveAndFlush(nextCustomerGamma);

        // Get all the nextCustomerGammaList where lastName equals to
        defaultNextCustomerGammaFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerGammasByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomerGamma = nextCustomerGammaRepository.saveAndFlush(nextCustomerGamma);

        // Get all the nextCustomerGammaList where lastName in
        defaultNextCustomerGammaFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerGammasByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomerGamma = nextCustomerGammaRepository.saveAndFlush(nextCustomerGamma);

        // Get all the nextCustomerGammaList where lastName is not null
        defaultNextCustomerGammaFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomerGammasByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerGamma = nextCustomerGammaRepository.saveAndFlush(nextCustomerGamma);

        // Get all the nextCustomerGammaList where lastName contains
        defaultNextCustomerGammaFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerGammasByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerGamma = nextCustomerGammaRepository.saveAndFlush(nextCustomerGamma);

        // Get all the nextCustomerGammaList where lastName does not contain
        defaultNextCustomerGammaFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerGammasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerGamma = nextCustomerGammaRepository.saveAndFlush(nextCustomerGamma);

        // Get all the nextCustomerGammaList where email equals to
        defaultNextCustomerGammaFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomerGammasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomerGamma = nextCustomerGammaRepository.saveAndFlush(nextCustomerGamma);

        // Get all the nextCustomerGammaList where email in
        defaultNextCustomerGammaFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomerGammasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomerGamma = nextCustomerGammaRepository.saveAndFlush(nextCustomerGamma);

        // Get all the nextCustomerGammaList where email is not null
        defaultNextCustomerGammaFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomerGammasByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerGamma = nextCustomerGammaRepository.saveAndFlush(nextCustomerGamma);

        // Get all the nextCustomerGammaList where email contains
        defaultNextCustomerGammaFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomerGammasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerGamma = nextCustomerGammaRepository.saveAndFlush(nextCustomerGamma);

        // Get all the nextCustomerGammaList where email does not contain
        defaultNextCustomerGammaFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomerGammasByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerGamma = nextCustomerGammaRepository.saveAndFlush(nextCustomerGamma);

        // Get all the nextCustomerGammaList where phoneNumber equals to
        defaultNextCustomerGammaFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextCustomerGammasByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomerGamma = nextCustomerGammaRepository.saveAndFlush(nextCustomerGamma);

        // Get all the nextCustomerGammaList where phoneNumber in
        defaultNextCustomerGammaFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextCustomerGammasByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomerGamma = nextCustomerGammaRepository.saveAndFlush(nextCustomerGamma);

        // Get all the nextCustomerGammaList where phoneNumber is not null
        defaultNextCustomerGammaFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomerGammasByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerGamma = nextCustomerGammaRepository.saveAndFlush(nextCustomerGamma);

        // Get all the nextCustomerGammaList where phoneNumber contains
        defaultNextCustomerGammaFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextCustomerGammasByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerGamma = nextCustomerGammaRepository.saveAndFlush(nextCustomerGamma);

        // Get all the nextCustomerGammaList where phoneNumber does not contain
        defaultNextCustomerGammaFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextCustomerGammasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextCustomerGammaRepository.saveAndFlush(nextCustomerGamma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextCustomerGamma.setTenant(tenant);
        nextCustomerGammaRepository.saveAndFlush(nextCustomerGamma);
        Long tenantId = tenant.getId();
        // Get all the nextCustomerGammaList where tenant equals to tenantId
        defaultNextCustomerGammaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextCustomerGammaList where tenant equals to (tenantId + 1)
        defaultNextCustomerGammaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextCustomerGammaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextCustomerGammaShouldBeFound(shouldBeFound);
        defaultNextCustomerGammaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextCustomerGammaShouldBeFound(String filter) throws Exception {
        restNextCustomerGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCustomerGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restNextCustomerGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextCustomerGammaShouldNotBeFound(String filter) throws Exception {
        restNextCustomerGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextCustomerGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextCustomerGamma() throws Exception {
        // Get the nextCustomerGamma
        restNextCustomerGammaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextCustomerGamma() throws Exception {
        // Initialize the database
        insertedNextCustomerGamma = nextCustomerGammaRepository.saveAndFlush(nextCustomerGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCustomerGamma
        NextCustomerGamma updatedNextCustomerGamma = nextCustomerGammaRepository.findById(nextCustomerGamma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextCustomerGamma are not directly saved in db
        em.detach(updatedNextCustomerGamma);
        updatedNextCustomerGamma
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restNextCustomerGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextCustomerGamma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextCustomerGamma))
            )
            .andExpect(status().isOk());

        // Validate the NextCustomerGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextCustomerGammaToMatchAllProperties(updatedNextCustomerGamma);
    }

    @Test
    @Transactional
    void putNonExistingNextCustomerGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerGamma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCustomerGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextCustomerGamma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCustomerGamma))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextCustomerGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerGamma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCustomerGamma))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextCustomerGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerGamma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerGammaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerGamma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCustomerGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextCustomerGammaWithPatch() throws Exception {
        // Initialize the database
        insertedNextCustomerGamma = nextCustomerGammaRepository.saveAndFlush(nextCustomerGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCustomerGamma using partial update
        NextCustomerGamma partialUpdatedNextCustomerGamma = new NextCustomerGamma();
        partialUpdatedNextCustomerGamma.setId(nextCustomerGamma.getId());

        partialUpdatedNextCustomerGamma.lastName(UPDATED_LAST_NAME).phoneNumber(UPDATED_PHONE_NUMBER);

        restNextCustomerGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCustomerGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCustomerGamma))
            )
            .andExpect(status().isOk());

        // Validate the NextCustomerGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCustomerGammaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextCustomerGamma, nextCustomerGamma),
            getPersistedNextCustomerGamma(nextCustomerGamma)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextCustomerGammaWithPatch() throws Exception {
        // Initialize the database
        insertedNextCustomerGamma = nextCustomerGammaRepository.saveAndFlush(nextCustomerGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCustomerGamma using partial update
        NextCustomerGamma partialUpdatedNextCustomerGamma = new NextCustomerGamma();
        partialUpdatedNextCustomerGamma.setId(nextCustomerGamma.getId());

        partialUpdatedNextCustomerGamma
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restNextCustomerGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCustomerGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCustomerGamma))
            )
            .andExpect(status().isOk());

        // Validate the NextCustomerGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCustomerGammaUpdatableFieldsEquals(
            partialUpdatedNextCustomerGamma,
            getPersistedNextCustomerGamma(partialUpdatedNextCustomerGamma)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextCustomerGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerGamma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCustomerGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextCustomerGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCustomerGamma))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextCustomerGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerGamma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCustomerGamma))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextCustomerGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerGamma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerGammaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextCustomerGamma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCustomerGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextCustomerGamma() throws Exception {
        // Initialize the database
        insertedNextCustomerGamma = nextCustomerGammaRepository.saveAndFlush(nextCustomerGamma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextCustomerGamma
        restNextCustomerGammaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextCustomerGamma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextCustomerGammaRepository.count();
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

    protected NextCustomerGamma getPersistedNextCustomerGamma(NextCustomerGamma nextCustomerGamma) {
        return nextCustomerGammaRepository.findById(nextCustomerGamma.getId()).orElseThrow();
    }

    protected void assertPersistedNextCustomerGammaToMatchAllProperties(NextCustomerGamma expectedNextCustomerGamma) {
        assertNextCustomerGammaAllPropertiesEquals(expectedNextCustomerGamma, getPersistedNextCustomerGamma(expectedNextCustomerGamma));
    }

    protected void assertPersistedNextCustomerGammaToMatchUpdatableProperties(NextCustomerGamma expectedNextCustomerGamma) {
        assertNextCustomerGammaAllUpdatablePropertiesEquals(
            expectedNextCustomerGamma,
            getPersistedNextCustomerGamma(expectedNextCustomerGamma)
        );
    }
}
