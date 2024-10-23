package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextCustomerViAsserts.*;
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
import xyz.jhmapstruct.domain.NextCustomerVi;
import xyz.jhmapstruct.repository.NextCustomerViRepository;

/**
 * Integration tests for the {@link NextCustomerViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextCustomerViResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/next-customer-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextCustomerViRepository nextCustomerViRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextCustomerViMockMvc;

    private NextCustomerVi nextCustomerVi;

    private NextCustomerVi insertedNextCustomerVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextCustomerVi createEntity() {
        return new NextCustomerVi()
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
    public static NextCustomerVi createUpdatedEntity() {
        return new NextCustomerVi()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        nextCustomerVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextCustomerVi != null) {
            nextCustomerViRepository.delete(insertedNextCustomerVi);
            insertedNextCustomerVi = null;
        }
    }

    @Test
    @Transactional
    void createNextCustomerVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextCustomerVi
        var returnedNextCustomerVi = om.readValue(
            restNextCustomerViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextCustomerVi.class
        );

        // Validate the NextCustomerVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextCustomerViUpdatableFieldsEquals(returnedNextCustomerVi, getPersistedNextCustomerVi(returnedNextCustomerVi));

        insertedNextCustomerVi = returnedNextCustomerVi;
    }

    @Test
    @Transactional
    void createNextCustomerViWithExistingId() throws Exception {
        // Create the NextCustomerVi with an existing ID
        nextCustomerVi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextCustomerViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerVi)))
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCustomerVi.setFirstName(null);

        // Create the NextCustomerVi, which fails.

        restNextCustomerViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCustomerVi.setLastName(null);

        // Create the NextCustomerVi, which fails.

        restNextCustomerViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCustomerVi.setEmail(null);

        // Create the NextCustomerVi, which fails.

        restNextCustomerViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextCustomerVis() throws Exception {
        // Initialize the database
        insertedNextCustomerVi = nextCustomerViRepository.saveAndFlush(nextCustomerVi);

        // Get all the nextCustomerViList
        restNextCustomerViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCustomerVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @Test
    @Transactional
    void getNextCustomerVi() throws Exception {
        // Initialize the database
        insertedNextCustomerVi = nextCustomerViRepository.saveAndFlush(nextCustomerVi);

        // Get the nextCustomerVi
        restNextCustomerViMockMvc
            .perform(get(ENTITY_API_URL_ID, nextCustomerVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextCustomerVi.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getNextCustomerVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextCustomerVi = nextCustomerViRepository.saveAndFlush(nextCustomerVi);

        Long id = nextCustomerVi.getId();

        defaultNextCustomerViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextCustomerViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextCustomerViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextCustomerVisByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerVi = nextCustomerViRepository.saveAndFlush(nextCustomerVi);

        // Get all the nextCustomerViList where firstName equals to
        defaultNextCustomerViFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerVisByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomerVi = nextCustomerViRepository.saveAndFlush(nextCustomerVi);

        // Get all the nextCustomerViList where firstName in
        defaultNextCustomerViFiltering(
            "firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME,
            "firstName.in=" + UPDATED_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllNextCustomerVisByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomerVi = nextCustomerViRepository.saveAndFlush(nextCustomerVi);

        // Get all the nextCustomerViList where firstName is not null
        defaultNextCustomerViFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomerVisByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerVi = nextCustomerViRepository.saveAndFlush(nextCustomerVi);

        // Get all the nextCustomerViList where firstName contains
        defaultNextCustomerViFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerVisByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerVi = nextCustomerViRepository.saveAndFlush(nextCustomerVi);

        // Get all the nextCustomerViList where firstName does not contain
        defaultNextCustomerViFiltering("firstName.doesNotContain=" + UPDATED_FIRST_NAME, "firstName.doesNotContain=" + DEFAULT_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerVisByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerVi = nextCustomerViRepository.saveAndFlush(nextCustomerVi);

        // Get all the nextCustomerViList where lastName equals to
        defaultNextCustomerViFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerVisByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomerVi = nextCustomerViRepository.saveAndFlush(nextCustomerVi);

        // Get all the nextCustomerViList where lastName in
        defaultNextCustomerViFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerVisByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomerVi = nextCustomerViRepository.saveAndFlush(nextCustomerVi);

        // Get all the nextCustomerViList where lastName is not null
        defaultNextCustomerViFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomerVisByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerVi = nextCustomerViRepository.saveAndFlush(nextCustomerVi);

        // Get all the nextCustomerViList where lastName contains
        defaultNextCustomerViFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerVisByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerVi = nextCustomerViRepository.saveAndFlush(nextCustomerVi);

        // Get all the nextCustomerViList where lastName does not contain
        defaultNextCustomerViFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerVisByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerVi = nextCustomerViRepository.saveAndFlush(nextCustomerVi);

        // Get all the nextCustomerViList where email equals to
        defaultNextCustomerViFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomerVisByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomerVi = nextCustomerViRepository.saveAndFlush(nextCustomerVi);

        // Get all the nextCustomerViList where email in
        defaultNextCustomerViFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomerVisByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomerVi = nextCustomerViRepository.saveAndFlush(nextCustomerVi);

        // Get all the nextCustomerViList where email is not null
        defaultNextCustomerViFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomerVisByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerVi = nextCustomerViRepository.saveAndFlush(nextCustomerVi);

        // Get all the nextCustomerViList where email contains
        defaultNextCustomerViFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomerVisByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerVi = nextCustomerViRepository.saveAndFlush(nextCustomerVi);

        // Get all the nextCustomerViList where email does not contain
        defaultNextCustomerViFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomerVisByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerVi = nextCustomerViRepository.saveAndFlush(nextCustomerVi);

        // Get all the nextCustomerViList where phoneNumber equals to
        defaultNextCustomerViFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextCustomerVisByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomerVi = nextCustomerViRepository.saveAndFlush(nextCustomerVi);

        // Get all the nextCustomerViList where phoneNumber in
        defaultNextCustomerViFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextCustomerVisByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomerVi = nextCustomerViRepository.saveAndFlush(nextCustomerVi);

        // Get all the nextCustomerViList where phoneNumber is not null
        defaultNextCustomerViFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomerVisByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerVi = nextCustomerViRepository.saveAndFlush(nextCustomerVi);

        // Get all the nextCustomerViList where phoneNumber contains
        defaultNextCustomerViFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextCustomerVisByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerVi = nextCustomerViRepository.saveAndFlush(nextCustomerVi);

        // Get all the nextCustomerViList where phoneNumber does not contain
        defaultNextCustomerViFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextCustomerVisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextCustomerViRepository.saveAndFlush(nextCustomerVi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextCustomerVi.setTenant(tenant);
        nextCustomerViRepository.saveAndFlush(nextCustomerVi);
        Long tenantId = tenant.getId();
        // Get all the nextCustomerViList where tenant equals to tenantId
        defaultNextCustomerViShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextCustomerViList where tenant equals to (tenantId + 1)
        defaultNextCustomerViShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextCustomerViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextCustomerViShouldBeFound(shouldBeFound);
        defaultNextCustomerViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextCustomerViShouldBeFound(String filter) throws Exception {
        restNextCustomerViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCustomerVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restNextCustomerViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextCustomerViShouldNotBeFound(String filter) throws Exception {
        restNextCustomerViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextCustomerViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextCustomerVi() throws Exception {
        // Get the nextCustomerVi
        restNextCustomerViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextCustomerVi() throws Exception {
        // Initialize the database
        insertedNextCustomerVi = nextCustomerViRepository.saveAndFlush(nextCustomerVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCustomerVi
        NextCustomerVi updatedNextCustomerVi = nextCustomerViRepository.findById(nextCustomerVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextCustomerVi are not directly saved in db
        em.detach(updatedNextCustomerVi);
        updatedNextCustomerVi
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restNextCustomerViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextCustomerVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextCustomerVi))
            )
            .andExpect(status().isOk());

        // Validate the NextCustomerVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextCustomerViToMatchAllProperties(updatedNextCustomerVi);
    }

    @Test
    @Transactional
    void putNonExistingNextCustomerVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCustomerViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextCustomerVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCustomerVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextCustomerVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCustomerVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextCustomerVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCustomerVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextCustomerViWithPatch() throws Exception {
        // Initialize the database
        insertedNextCustomerVi = nextCustomerViRepository.saveAndFlush(nextCustomerVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCustomerVi using partial update
        NextCustomerVi partialUpdatedNextCustomerVi = new NextCustomerVi();
        partialUpdatedNextCustomerVi.setId(nextCustomerVi.getId());

        partialUpdatedNextCustomerVi.firstName(UPDATED_FIRST_NAME).email(UPDATED_EMAIL).phoneNumber(UPDATED_PHONE_NUMBER);

        restNextCustomerViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCustomerVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCustomerVi))
            )
            .andExpect(status().isOk());

        // Validate the NextCustomerVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCustomerViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextCustomerVi, nextCustomerVi),
            getPersistedNextCustomerVi(nextCustomerVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextCustomerViWithPatch() throws Exception {
        // Initialize the database
        insertedNextCustomerVi = nextCustomerViRepository.saveAndFlush(nextCustomerVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCustomerVi using partial update
        NextCustomerVi partialUpdatedNextCustomerVi = new NextCustomerVi();
        partialUpdatedNextCustomerVi.setId(nextCustomerVi.getId());

        partialUpdatedNextCustomerVi
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restNextCustomerViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCustomerVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCustomerVi))
            )
            .andExpect(status().isOk());

        // Validate the NextCustomerVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCustomerViUpdatableFieldsEquals(partialUpdatedNextCustomerVi, getPersistedNextCustomerVi(partialUpdatedNextCustomerVi));
    }

    @Test
    @Transactional
    void patchNonExistingNextCustomerVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCustomerViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextCustomerVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCustomerVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextCustomerVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCustomerVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextCustomerVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextCustomerVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCustomerVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextCustomerVi() throws Exception {
        // Initialize the database
        insertedNextCustomerVi = nextCustomerViRepository.saveAndFlush(nextCustomerVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextCustomerVi
        restNextCustomerViMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextCustomerVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextCustomerViRepository.count();
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

    protected NextCustomerVi getPersistedNextCustomerVi(NextCustomerVi nextCustomerVi) {
        return nextCustomerViRepository.findById(nextCustomerVi.getId()).orElseThrow();
    }

    protected void assertPersistedNextCustomerViToMatchAllProperties(NextCustomerVi expectedNextCustomerVi) {
        assertNextCustomerViAllPropertiesEquals(expectedNextCustomerVi, getPersistedNextCustomerVi(expectedNextCustomerVi));
    }

    protected void assertPersistedNextCustomerViToMatchUpdatableProperties(NextCustomerVi expectedNextCustomerVi) {
        assertNextCustomerViAllUpdatablePropertiesEquals(expectedNextCustomerVi, getPersistedNextCustomerVi(expectedNextCustomerVi));
    }
}
