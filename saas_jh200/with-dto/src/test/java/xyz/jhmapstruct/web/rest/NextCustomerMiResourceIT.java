package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextCustomerMiAsserts.*;
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
import xyz.jhmapstruct.domain.NextCustomerMi;
import xyz.jhmapstruct.repository.NextCustomerMiRepository;
import xyz.jhmapstruct.service.dto.NextCustomerMiDTO;
import xyz.jhmapstruct.service.mapper.NextCustomerMiMapper;

/**
 * Integration tests for the {@link NextCustomerMiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextCustomerMiResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/next-customer-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextCustomerMiRepository nextCustomerMiRepository;

    @Autowired
    private NextCustomerMiMapper nextCustomerMiMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextCustomerMiMockMvc;

    private NextCustomerMi nextCustomerMi;

    private NextCustomerMi insertedNextCustomerMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextCustomerMi createEntity() {
        return new NextCustomerMi()
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
    public static NextCustomerMi createUpdatedEntity() {
        return new NextCustomerMi()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        nextCustomerMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextCustomerMi != null) {
            nextCustomerMiRepository.delete(insertedNextCustomerMi);
            insertedNextCustomerMi = null;
        }
    }

    @Test
    @Transactional
    void createNextCustomerMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextCustomerMi
        NextCustomerMiDTO nextCustomerMiDTO = nextCustomerMiMapper.toDto(nextCustomerMi);
        var returnedNextCustomerMiDTO = om.readValue(
            restNextCustomerMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerMiDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextCustomerMiDTO.class
        );

        // Validate the NextCustomerMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextCustomerMi = nextCustomerMiMapper.toEntity(returnedNextCustomerMiDTO);
        assertNextCustomerMiUpdatableFieldsEquals(returnedNextCustomerMi, getPersistedNextCustomerMi(returnedNextCustomerMi));

        insertedNextCustomerMi = returnedNextCustomerMi;
    }

    @Test
    @Transactional
    void createNextCustomerMiWithExistingId() throws Exception {
        // Create the NextCustomerMi with an existing ID
        nextCustomerMi.setId(1L);
        NextCustomerMiDTO nextCustomerMiDTO = nextCustomerMiMapper.toDto(nextCustomerMi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextCustomerMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerMiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCustomerMi.setFirstName(null);

        // Create the NextCustomerMi, which fails.
        NextCustomerMiDTO nextCustomerMiDTO = nextCustomerMiMapper.toDto(nextCustomerMi);

        restNextCustomerMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCustomerMi.setLastName(null);

        // Create the NextCustomerMi, which fails.
        NextCustomerMiDTO nextCustomerMiDTO = nextCustomerMiMapper.toDto(nextCustomerMi);

        restNextCustomerMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCustomerMi.setEmail(null);

        // Create the NextCustomerMi, which fails.
        NextCustomerMiDTO nextCustomerMiDTO = nextCustomerMiMapper.toDto(nextCustomerMi);

        restNextCustomerMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextCustomerMis() throws Exception {
        // Initialize the database
        insertedNextCustomerMi = nextCustomerMiRepository.saveAndFlush(nextCustomerMi);

        // Get all the nextCustomerMiList
        restNextCustomerMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCustomerMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @Test
    @Transactional
    void getNextCustomerMi() throws Exception {
        // Initialize the database
        insertedNextCustomerMi = nextCustomerMiRepository.saveAndFlush(nextCustomerMi);

        // Get the nextCustomerMi
        restNextCustomerMiMockMvc
            .perform(get(ENTITY_API_URL_ID, nextCustomerMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextCustomerMi.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getNextCustomerMisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextCustomerMi = nextCustomerMiRepository.saveAndFlush(nextCustomerMi);

        Long id = nextCustomerMi.getId();

        defaultNextCustomerMiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextCustomerMiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextCustomerMiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextCustomerMisByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerMi = nextCustomerMiRepository.saveAndFlush(nextCustomerMi);

        // Get all the nextCustomerMiList where firstName equals to
        defaultNextCustomerMiFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerMisByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomerMi = nextCustomerMiRepository.saveAndFlush(nextCustomerMi);

        // Get all the nextCustomerMiList where firstName in
        defaultNextCustomerMiFiltering(
            "firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME,
            "firstName.in=" + UPDATED_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllNextCustomerMisByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomerMi = nextCustomerMiRepository.saveAndFlush(nextCustomerMi);

        // Get all the nextCustomerMiList where firstName is not null
        defaultNextCustomerMiFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomerMisByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerMi = nextCustomerMiRepository.saveAndFlush(nextCustomerMi);

        // Get all the nextCustomerMiList where firstName contains
        defaultNextCustomerMiFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerMisByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerMi = nextCustomerMiRepository.saveAndFlush(nextCustomerMi);

        // Get all the nextCustomerMiList where firstName does not contain
        defaultNextCustomerMiFiltering("firstName.doesNotContain=" + UPDATED_FIRST_NAME, "firstName.doesNotContain=" + DEFAULT_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerMisByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerMi = nextCustomerMiRepository.saveAndFlush(nextCustomerMi);

        // Get all the nextCustomerMiList where lastName equals to
        defaultNextCustomerMiFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerMisByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomerMi = nextCustomerMiRepository.saveAndFlush(nextCustomerMi);

        // Get all the nextCustomerMiList where lastName in
        defaultNextCustomerMiFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerMisByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomerMi = nextCustomerMiRepository.saveAndFlush(nextCustomerMi);

        // Get all the nextCustomerMiList where lastName is not null
        defaultNextCustomerMiFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomerMisByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerMi = nextCustomerMiRepository.saveAndFlush(nextCustomerMi);

        // Get all the nextCustomerMiList where lastName contains
        defaultNextCustomerMiFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerMisByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerMi = nextCustomerMiRepository.saveAndFlush(nextCustomerMi);

        // Get all the nextCustomerMiList where lastName does not contain
        defaultNextCustomerMiFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerMisByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerMi = nextCustomerMiRepository.saveAndFlush(nextCustomerMi);

        // Get all the nextCustomerMiList where email equals to
        defaultNextCustomerMiFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomerMisByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomerMi = nextCustomerMiRepository.saveAndFlush(nextCustomerMi);

        // Get all the nextCustomerMiList where email in
        defaultNextCustomerMiFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomerMisByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomerMi = nextCustomerMiRepository.saveAndFlush(nextCustomerMi);

        // Get all the nextCustomerMiList where email is not null
        defaultNextCustomerMiFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomerMisByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerMi = nextCustomerMiRepository.saveAndFlush(nextCustomerMi);

        // Get all the nextCustomerMiList where email contains
        defaultNextCustomerMiFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomerMisByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerMi = nextCustomerMiRepository.saveAndFlush(nextCustomerMi);

        // Get all the nextCustomerMiList where email does not contain
        defaultNextCustomerMiFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomerMisByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerMi = nextCustomerMiRepository.saveAndFlush(nextCustomerMi);

        // Get all the nextCustomerMiList where phoneNumber equals to
        defaultNextCustomerMiFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextCustomerMisByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomerMi = nextCustomerMiRepository.saveAndFlush(nextCustomerMi);

        // Get all the nextCustomerMiList where phoneNumber in
        defaultNextCustomerMiFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextCustomerMisByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomerMi = nextCustomerMiRepository.saveAndFlush(nextCustomerMi);

        // Get all the nextCustomerMiList where phoneNumber is not null
        defaultNextCustomerMiFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomerMisByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerMi = nextCustomerMiRepository.saveAndFlush(nextCustomerMi);

        // Get all the nextCustomerMiList where phoneNumber contains
        defaultNextCustomerMiFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextCustomerMisByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerMi = nextCustomerMiRepository.saveAndFlush(nextCustomerMi);

        // Get all the nextCustomerMiList where phoneNumber does not contain
        defaultNextCustomerMiFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextCustomerMisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextCustomerMiRepository.saveAndFlush(nextCustomerMi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextCustomerMi.setTenant(tenant);
        nextCustomerMiRepository.saveAndFlush(nextCustomerMi);
        Long tenantId = tenant.getId();
        // Get all the nextCustomerMiList where tenant equals to tenantId
        defaultNextCustomerMiShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextCustomerMiList where tenant equals to (tenantId + 1)
        defaultNextCustomerMiShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextCustomerMiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextCustomerMiShouldBeFound(shouldBeFound);
        defaultNextCustomerMiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextCustomerMiShouldBeFound(String filter) throws Exception {
        restNextCustomerMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCustomerMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restNextCustomerMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextCustomerMiShouldNotBeFound(String filter) throws Exception {
        restNextCustomerMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextCustomerMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextCustomerMi() throws Exception {
        // Get the nextCustomerMi
        restNextCustomerMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextCustomerMi() throws Exception {
        // Initialize the database
        insertedNextCustomerMi = nextCustomerMiRepository.saveAndFlush(nextCustomerMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCustomerMi
        NextCustomerMi updatedNextCustomerMi = nextCustomerMiRepository.findById(nextCustomerMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextCustomerMi are not directly saved in db
        em.detach(updatedNextCustomerMi);
        updatedNextCustomerMi
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        NextCustomerMiDTO nextCustomerMiDTO = nextCustomerMiMapper.toDto(updatedNextCustomerMi);

        restNextCustomerMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextCustomerMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCustomerMiDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextCustomerMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextCustomerMiToMatchAllProperties(updatedNextCustomerMi);
    }

    @Test
    @Transactional
    void putNonExistingNextCustomerMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerMi.setId(longCount.incrementAndGet());

        // Create the NextCustomerMi
        NextCustomerMiDTO nextCustomerMiDTO = nextCustomerMiMapper.toDto(nextCustomerMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCustomerMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextCustomerMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCustomerMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextCustomerMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerMi.setId(longCount.incrementAndGet());

        // Create the NextCustomerMi
        NextCustomerMiDTO nextCustomerMiDTO = nextCustomerMiMapper.toDto(nextCustomerMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCustomerMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextCustomerMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerMi.setId(longCount.incrementAndGet());

        // Create the NextCustomerMi
        NextCustomerMiDTO nextCustomerMiDTO = nextCustomerMiMapper.toDto(nextCustomerMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCustomerMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextCustomerMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextCustomerMi = nextCustomerMiRepository.saveAndFlush(nextCustomerMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCustomerMi using partial update
        NextCustomerMi partialUpdatedNextCustomerMi = new NextCustomerMi();
        partialUpdatedNextCustomerMi.setId(nextCustomerMi.getId());

        partialUpdatedNextCustomerMi.lastName(UPDATED_LAST_NAME).email(UPDATED_EMAIL);

        restNextCustomerMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCustomerMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCustomerMi))
            )
            .andExpect(status().isOk());

        // Validate the NextCustomerMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCustomerMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextCustomerMi, nextCustomerMi),
            getPersistedNextCustomerMi(nextCustomerMi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextCustomerMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextCustomerMi = nextCustomerMiRepository.saveAndFlush(nextCustomerMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCustomerMi using partial update
        NextCustomerMi partialUpdatedNextCustomerMi = new NextCustomerMi();
        partialUpdatedNextCustomerMi.setId(nextCustomerMi.getId());

        partialUpdatedNextCustomerMi
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restNextCustomerMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCustomerMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCustomerMi))
            )
            .andExpect(status().isOk());

        // Validate the NextCustomerMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCustomerMiUpdatableFieldsEquals(partialUpdatedNextCustomerMi, getPersistedNextCustomerMi(partialUpdatedNextCustomerMi));
    }

    @Test
    @Transactional
    void patchNonExistingNextCustomerMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerMi.setId(longCount.incrementAndGet());

        // Create the NextCustomerMi
        NextCustomerMiDTO nextCustomerMiDTO = nextCustomerMiMapper.toDto(nextCustomerMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCustomerMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextCustomerMiDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCustomerMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextCustomerMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerMi.setId(longCount.incrementAndGet());

        // Create the NextCustomerMi
        NextCustomerMiDTO nextCustomerMiDTO = nextCustomerMiMapper.toDto(nextCustomerMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCustomerMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextCustomerMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerMi.setId(longCount.incrementAndGet());

        // Create the NextCustomerMi
        NextCustomerMiDTO nextCustomerMiDTO = nextCustomerMiMapper.toDto(nextCustomerMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextCustomerMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCustomerMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextCustomerMi() throws Exception {
        // Initialize the database
        insertedNextCustomerMi = nextCustomerMiRepository.saveAndFlush(nextCustomerMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextCustomerMi
        restNextCustomerMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextCustomerMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextCustomerMiRepository.count();
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

    protected NextCustomerMi getPersistedNextCustomerMi(NextCustomerMi nextCustomerMi) {
        return nextCustomerMiRepository.findById(nextCustomerMi.getId()).orElseThrow();
    }

    protected void assertPersistedNextCustomerMiToMatchAllProperties(NextCustomerMi expectedNextCustomerMi) {
        assertNextCustomerMiAllPropertiesEquals(expectedNextCustomerMi, getPersistedNextCustomerMi(expectedNextCustomerMi));
    }

    protected void assertPersistedNextCustomerMiToMatchUpdatableProperties(NextCustomerMi expectedNextCustomerMi) {
        assertNextCustomerMiAllUpdatablePropertiesEquals(expectedNextCustomerMi, getPersistedNextCustomerMi(expectedNextCustomerMi));
    }
}
