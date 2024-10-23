package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextCustomerAlphaAsserts.*;
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
import xyz.jhmapstruct.domain.NextCustomerAlpha;
import xyz.jhmapstruct.repository.NextCustomerAlphaRepository;
import xyz.jhmapstruct.service.dto.NextCustomerAlphaDTO;
import xyz.jhmapstruct.service.mapper.NextCustomerAlphaMapper;

/**
 * Integration tests for the {@link NextCustomerAlphaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextCustomerAlphaResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/next-customer-alphas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextCustomerAlphaRepository nextCustomerAlphaRepository;

    @Autowired
    private NextCustomerAlphaMapper nextCustomerAlphaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextCustomerAlphaMockMvc;

    private NextCustomerAlpha nextCustomerAlpha;

    private NextCustomerAlpha insertedNextCustomerAlpha;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextCustomerAlpha createEntity() {
        return new NextCustomerAlpha()
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
    public static NextCustomerAlpha createUpdatedEntity() {
        return new NextCustomerAlpha()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        nextCustomerAlpha = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextCustomerAlpha != null) {
            nextCustomerAlphaRepository.delete(insertedNextCustomerAlpha);
            insertedNextCustomerAlpha = null;
        }
    }

    @Test
    @Transactional
    void createNextCustomerAlpha() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextCustomerAlpha
        NextCustomerAlphaDTO nextCustomerAlphaDTO = nextCustomerAlphaMapper.toDto(nextCustomerAlpha);
        var returnedNextCustomerAlphaDTO = om.readValue(
            restNextCustomerAlphaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerAlphaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextCustomerAlphaDTO.class
        );

        // Validate the NextCustomerAlpha in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextCustomerAlpha = nextCustomerAlphaMapper.toEntity(returnedNextCustomerAlphaDTO);
        assertNextCustomerAlphaUpdatableFieldsEquals(returnedNextCustomerAlpha, getPersistedNextCustomerAlpha(returnedNextCustomerAlpha));

        insertedNextCustomerAlpha = returnedNextCustomerAlpha;
    }

    @Test
    @Transactional
    void createNextCustomerAlphaWithExistingId() throws Exception {
        // Create the NextCustomerAlpha with an existing ID
        nextCustomerAlpha.setId(1L);
        NextCustomerAlphaDTO nextCustomerAlphaDTO = nextCustomerAlphaMapper.toDto(nextCustomerAlpha);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextCustomerAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerAlphaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCustomerAlpha.setFirstName(null);

        // Create the NextCustomerAlpha, which fails.
        NextCustomerAlphaDTO nextCustomerAlphaDTO = nextCustomerAlphaMapper.toDto(nextCustomerAlpha);

        restNextCustomerAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerAlphaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCustomerAlpha.setLastName(null);

        // Create the NextCustomerAlpha, which fails.
        NextCustomerAlphaDTO nextCustomerAlphaDTO = nextCustomerAlphaMapper.toDto(nextCustomerAlpha);

        restNextCustomerAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerAlphaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCustomerAlpha.setEmail(null);

        // Create the NextCustomerAlpha, which fails.
        NextCustomerAlphaDTO nextCustomerAlphaDTO = nextCustomerAlphaMapper.toDto(nextCustomerAlpha);

        restNextCustomerAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerAlphaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextCustomerAlphas() throws Exception {
        // Initialize the database
        insertedNextCustomerAlpha = nextCustomerAlphaRepository.saveAndFlush(nextCustomerAlpha);

        // Get all the nextCustomerAlphaList
        restNextCustomerAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCustomerAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @Test
    @Transactional
    void getNextCustomerAlpha() throws Exception {
        // Initialize the database
        insertedNextCustomerAlpha = nextCustomerAlphaRepository.saveAndFlush(nextCustomerAlpha);

        // Get the nextCustomerAlpha
        restNextCustomerAlphaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextCustomerAlpha.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextCustomerAlpha.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getNextCustomerAlphasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextCustomerAlpha = nextCustomerAlphaRepository.saveAndFlush(nextCustomerAlpha);

        Long id = nextCustomerAlpha.getId();

        defaultNextCustomerAlphaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextCustomerAlphaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextCustomerAlphaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextCustomerAlphasByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerAlpha = nextCustomerAlphaRepository.saveAndFlush(nextCustomerAlpha);

        // Get all the nextCustomerAlphaList where firstName equals to
        defaultNextCustomerAlphaFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerAlphasByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomerAlpha = nextCustomerAlphaRepository.saveAndFlush(nextCustomerAlpha);

        // Get all the nextCustomerAlphaList where firstName in
        defaultNextCustomerAlphaFiltering(
            "firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME,
            "firstName.in=" + UPDATED_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllNextCustomerAlphasByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomerAlpha = nextCustomerAlphaRepository.saveAndFlush(nextCustomerAlpha);

        // Get all the nextCustomerAlphaList where firstName is not null
        defaultNextCustomerAlphaFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomerAlphasByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerAlpha = nextCustomerAlphaRepository.saveAndFlush(nextCustomerAlpha);

        // Get all the nextCustomerAlphaList where firstName contains
        defaultNextCustomerAlphaFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerAlphasByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerAlpha = nextCustomerAlphaRepository.saveAndFlush(nextCustomerAlpha);

        // Get all the nextCustomerAlphaList where firstName does not contain
        defaultNextCustomerAlphaFiltering(
            "firstName.doesNotContain=" + UPDATED_FIRST_NAME,
            "firstName.doesNotContain=" + DEFAULT_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllNextCustomerAlphasByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerAlpha = nextCustomerAlphaRepository.saveAndFlush(nextCustomerAlpha);

        // Get all the nextCustomerAlphaList where lastName equals to
        defaultNextCustomerAlphaFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerAlphasByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomerAlpha = nextCustomerAlphaRepository.saveAndFlush(nextCustomerAlpha);

        // Get all the nextCustomerAlphaList where lastName in
        defaultNextCustomerAlphaFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerAlphasByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomerAlpha = nextCustomerAlphaRepository.saveAndFlush(nextCustomerAlpha);

        // Get all the nextCustomerAlphaList where lastName is not null
        defaultNextCustomerAlphaFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomerAlphasByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerAlpha = nextCustomerAlphaRepository.saveAndFlush(nextCustomerAlpha);

        // Get all the nextCustomerAlphaList where lastName contains
        defaultNextCustomerAlphaFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerAlphasByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerAlpha = nextCustomerAlphaRepository.saveAndFlush(nextCustomerAlpha);

        // Get all the nextCustomerAlphaList where lastName does not contain
        defaultNextCustomerAlphaFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerAlphasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerAlpha = nextCustomerAlphaRepository.saveAndFlush(nextCustomerAlpha);

        // Get all the nextCustomerAlphaList where email equals to
        defaultNextCustomerAlphaFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomerAlphasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomerAlpha = nextCustomerAlphaRepository.saveAndFlush(nextCustomerAlpha);

        // Get all the nextCustomerAlphaList where email in
        defaultNextCustomerAlphaFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomerAlphasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomerAlpha = nextCustomerAlphaRepository.saveAndFlush(nextCustomerAlpha);

        // Get all the nextCustomerAlphaList where email is not null
        defaultNextCustomerAlphaFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomerAlphasByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerAlpha = nextCustomerAlphaRepository.saveAndFlush(nextCustomerAlpha);

        // Get all the nextCustomerAlphaList where email contains
        defaultNextCustomerAlphaFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomerAlphasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerAlpha = nextCustomerAlphaRepository.saveAndFlush(nextCustomerAlpha);

        // Get all the nextCustomerAlphaList where email does not contain
        defaultNextCustomerAlphaFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomerAlphasByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerAlpha = nextCustomerAlphaRepository.saveAndFlush(nextCustomerAlpha);

        // Get all the nextCustomerAlphaList where phoneNumber equals to
        defaultNextCustomerAlphaFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextCustomerAlphasByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomerAlpha = nextCustomerAlphaRepository.saveAndFlush(nextCustomerAlpha);

        // Get all the nextCustomerAlphaList where phoneNumber in
        defaultNextCustomerAlphaFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextCustomerAlphasByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomerAlpha = nextCustomerAlphaRepository.saveAndFlush(nextCustomerAlpha);

        // Get all the nextCustomerAlphaList where phoneNumber is not null
        defaultNextCustomerAlphaFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomerAlphasByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerAlpha = nextCustomerAlphaRepository.saveAndFlush(nextCustomerAlpha);

        // Get all the nextCustomerAlphaList where phoneNumber contains
        defaultNextCustomerAlphaFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextCustomerAlphasByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerAlpha = nextCustomerAlphaRepository.saveAndFlush(nextCustomerAlpha);

        // Get all the nextCustomerAlphaList where phoneNumber does not contain
        defaultNextCustomerAlphaFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextCustomerAlphasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextCustomerAlphaRepository.saveAndFlush(nextCustomerAlpha);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextCustomerAlpha.setTenant(tenant);
        nextCustomerAlphaRepository.saveAndFlush(nextCustomerAlpha);
        Long tenantId = tenant.getId();
        // Get all the nextCustomerAlphaList where tenant equals to tenantId
        defaultNextCustomerAlphaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextCustomerAlphaList where tenant equals to (tenantId + 1)
        defaultNextCustomerAlphaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextCustomerAlphaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextCustomerAlphaShouldBeFound(shouldBeFound);
        defaultNextCustomerAlphaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextCustomerAlphaShouldBeFound(String filter) throws Exception {
        restNextCustomerAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCustomerAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restNextCustomerAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextCustomerAlphaShouldNotBeFound(String filter) throws Exception {
        restNextCustomerAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextCustomerAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextCustomerAlpha() throws Exception {
        // Get the nextCustomerAlpha
        restNextCustomerAlphaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextCustomerAlpha() throws Exception {
        // Initialize the database
        insertedNextCustomerAlpha = nextCustomerAlphaRepository.saveAndFlush(nextCustomerAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCustomerAlpha
        NextCustomerAlpha updatedNextCustomerAlpha = nextCustomerAlphaRepository.findById(nextCustomerAlpha.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextCustomerAlpha are not directly saved in db
        em.detach(updatedNextCustomerAlpha);
        updatedNextCustomerAlpha
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        NextCustomerAlphaDTO nextCustomerAlphaDTO = nextCustomerAlphaMapper.toDto(updatedNextCustomerAlpha);

        restNextCustomerAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextCustomerAlphaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCustomerAlphaDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextCustomerAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextCustomerAlphaToMatchAllProperties(updatedNextCustomerAlpha);
    }

    @Test
    @Transactional
    void putNonExistingNextCustomerAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerAlpha.setId(longCount.incrementAndGet());

        // Create the NextCustomerAlpha
        NextCustomerAlphaDTO nextCustomerAlphaDTO = nextCustomerAlphaMapper.toDto(nextCustomerAlpha);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCustomerAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextCustomerAlphaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCustomerAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextCustomerAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerAlpha.setId(longCount.incrementAndGet());

        // Create the NextCustomerAlpha
        NextCustomerAlphaDTO nextCustomerAlphaDTO = nextCustomerAlphaMapper.toDto(nextCustomerAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCustomerAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextCustomerAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerAlpha.setId(longCount.incrementAndGet());

        // Create the NextCustomerAlpha
        NextCustomerAlphaDTO nextCustomerAlphaDTO = nextCustomerAlphaMapper.toDto(nextCustomerAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerAlphaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerAlphaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCustomerAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextCustomerAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedNextCustomerAlpha = nextCustomerAlphaRepository.saveAndFlush(nextCustomerAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCustomerAlpha using partial update
        NextCustomerAlpha partialUpdatedNextCustomerAlpha = new NextCustomerAlpha();
        partialUpdatedNextCustomerAlpha.setId(nextCustomerAlpha.getId());

        partialUpdatedNextCustomerAlpha
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restNextCustomerAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCustomerAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCustomerAlpha))
            )
            .andExpect(status().isOk());

        // Validate the NextCustomerAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCustomerAlphaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextCustomerAlpha, nextCustomerAlpha),
            getPersistedNextCustomerAlpha(nextCustomerAlpha)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextCustomerAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedNextCustomerAlpha = nextCustomerAlphaRepository.saveAndFlush(nextCustomerAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCustomerAlpha using partial update
        NextCustomerAlpha partialUpdatedNextCustomerAlpha = new NextCustomerAlpha();
        partialUpdatedNextCustomerAlpha.setId(nextCustomerAlpha.getId());

        partialUpdatedNextCustomerAlpha
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restNextCustomerAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCustomerAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCustomerAlpha))
            )
            .andExpect(status().isOk());

        // Validate the NextCustomerAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCustomerAlphaUpdatableFieldsEquals(
            partialUpdatedNextCustomerAlpha,
            getPersistedNextCustomerAlpha(partialUpdatedNextCustomerAlpha)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextCustomerAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerAlpha.setId(longCount.incrementAndGet());

        // Create the NextCustomerAlpha
        NextCustomerAlphaDTO nextCustomerAlphaDTO = nextCustomerAlphaMapper.toDto(nextCustomerAlpha);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCustomerAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextCustomerAlphaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCustomerAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextCustomerAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerAlpha.setId(longCount.incrementAndGet());

        // Create the NextCustomerAlpha
        NextCustomerAlphaDTO nextCustomerAlphaDTO = nextCustomerAlphaMapper.toDto(nextCustomerAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCustomerAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextCustomerAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerAlpha.setId(longCount.incrementAndGet());

        // Create the NextCustomerAlpha
        NextCustomerAlphaDTO nextCustomerAlphaDTO = nextCustomerAlphaMapper.toDto(nextCustomerAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerAlphaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextCustomerAlphaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCustomerAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextCustomerAlpha() throws Exception {
        // Initialize the database
        insertedNextCustomerAlpha = nextCustomerAlphaRepository.saveAndFlush(nextCustomerAlpha);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextCustomerAlpha
        restNextCustomerAlphaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextCustomerAlpha.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextCustomerAlphaRepository.count();
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

    protected NextCustomerAlpha getPersistedNextCustomerAlpha(NextCustomerAlpha nextCustomerAlpha) {
        return nextCustomerAlphaRepository.findById(nextCustomerAlpha.getId()).orElseThrow();
    }

    protected void assertPersistedNextCustomerAlphaToMatchAllProperties(NextCustomerAlpha expectedNextCustomerAlpha) {
        assertNextCustomerAlphaAllPropertiesEquals(expectedNextCustomerAlpha, getPersistedNextCustomerAlpha(expectedNextCustomerAlpha));
    }

    protected void assertPersistedNextCustomerAlphaToMatchUpdatableProperties(NextCustomerAlpha expectedNextCustomerAlpha) {
        assertNextCustomerAlphaAllUpdatablePropertiesEquals(
            expectedNextCustomerAlpha,
            getPersistedNextCustomerAlpha(expectedNextCustomerAlpha)
        );
    }
}
