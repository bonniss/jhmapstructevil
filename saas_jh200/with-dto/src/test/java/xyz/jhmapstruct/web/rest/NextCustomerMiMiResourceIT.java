package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextCustomerMiMiAsserts.*;
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
import xyz.jhmapstruct.domain.NextCustomerMiMi;
import xyz.jhmapstruct.repository.NextCustomerMiMiRepository;
import xyz.jhmapstruct.service.dto.NextCustomerMiMiDTO;
import xyz.jhmapstruct.service.mapper.NextCustomerMiMiMapper;

/**
 * Integration tests for the {@link NextCustomerMiMiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextCustomerMiMiResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/next-customer-mi-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextCustomerMiMiRepository nextCustomerMiMiRepository;

    @Autowired
    private NextCustomerMiMiMapper nextCustomerMiMiMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextCustomerMiMiMockMvc;

    private NextCustomerMiMi nextCustomerMiMi;

    private NextCustomerMiMi insertedNextCustomerMiMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextCustomerMiMi createEntity() {
        return new NextCustomerMiMi()
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
    public static NextCustomerMiMi createUpdatedEntity() {
        return new NextCustomerMiMi()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        nextCustomerMiMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextCustomerMiMi != null) {
            nextCustomerMiMiRepository.delete(insertedNextCustomerMiMi);
            insertedNextCustomerMiMi = null;
        }
    }

    @Test
    @Transactional
    void createNextCustomerMiMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextCustomerMiMi
        NextCustomerMiMiDTO nextCustomerMiMiDTO = nextCustomerMiMiMapper.toDto(nextCustomerMiMi);
        var returnedNextCustomerMiMiDTO = om.readValue(
            restNextCustomerMiMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerMiMiDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextCustomerMiMiDTO.class
        );

        // Validate the NextCustomerMiMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextCustomerMiMi = nextCustomerMiMiMapper.toEntity(returnedNextCustomerMiMiDTO);
        assertNextCustomerMiMiUpdatableFieldsEquals(returnedNextCustomerMiMi, getPersistedNextCustomerMiMi(returnedNextCustomerMiMi));

        insertedNextCustomerMiMi = returnedNextCustomerMiMi;
    }

    @Test
    @Transactional
    void createNextCustomerMiMiWithExistingId() throws Exception {
        // Create the NextCustomerMiMi with an existing ID
        nextCustomerMiMi.setId(1L);
        NextCustomerMiMiDTO nextCustomerMiMiDTO = nextCustomerMiMiMapper.toDto(nextCustomerMiMi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextCustomerMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerMiMiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCustomerMiMi.setFirstName(null);

        // Create the NextCustomerMiMi, which fails.
        NextCustomerMiMiDTO nextCustomerMiMiDTO = nextCustomerMiMiMapper.toDto(nextCustomerMiMi);

        restNextCustomerMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerMiMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCustomerMiMi.setLastName(null);

        // Create the NextCustomerMiMi, which fails.
        NextCustomerMiMiDTO nextCustomerMiMiDTO = nextCustomerMiMiMapper.toDto(nextCustomerMiMi);

        restNextCustomerMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerMiMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCustomerMiMi.setEmail(null);

        // Create the NextCustomerMiMi, which fails.
        NextCustomerMiMiDTO nextCustomerMiMiDTO = nextCustomerMiMiMapper.toDto(nextCustomerMiMi);

        restNextCustomerMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerMiMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextCustomerMiMis() throws Exception {
        // Initialize the database
        insertedNextCustomerMiMi = nextCustomerMiMiRepository.saveAndFlush(nextCustomerMiMi);

        // Get all the nextCustomerMiMiList
        restNextCustomerMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCustomerMiMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @Test
    @Transactional
    void getNextCustomerMiMi() throws Exception {
        // Initialize the database
        insertedNextCustomerMiMi = nextCustomerMiMiRepository.saveAndFlush(nextCustomerMiMi);

        // Get the nextCustomerMiMi
        restNextCustomerMiMiMockMvc
            .perform(get(ENTITY_API_URL_ID, nextCustomerMiMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextCustomerMiMi.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getNextCustomerMiMisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextCustomerMiMi = nextCustomerMiMiRepository.saveAndFlush(nextCustomerMiMi);

        Long id = nextCustomerMiMi.getId();

        defaultNextCustomerMiMiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextCustomerMiMiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextCustomerMiMiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextCustomerMiMisByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerMiMi = nextCustomerMiMiRepository.saveAndFlush(nextCustomerMiMi);

        // Get all the nextCustomerMiMiList where firstName equals to
        defaultNextCustomerMiMiFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerMiMisByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomerMiMi = nextCustomerMiMiRepository.saveAndFlush(nextCustomerMiMi);

        // Get all the nextCustomerMiMiList where firstName in
        defaultNextCustomerMiMiFiltering(
            "firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME,
            "firstName.in=" + UPDATED_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllNextCustomerMiMisByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomerMiMi = nextCustomerMiMiRepository.saveAndFlush(nextCustomerMiMi);

        // Get all the nextCustomerMiMiList where firstName is not null
        defaultNextCustomerMiMiFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomerMiMisByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerMiMi = nextCustomerMiMiRepository.saveAndFlush(nextCustomerMiMi);

        // Get all the nextCustomerMiMiList where firstName contains
        defaultNextCustomerMiMiFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerMiMisByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerMiMi = nextCustomerMiMiRepository.saveAndFlush(nextCustomerMiMi);

        // Get all the nextCustomerMiMiList where firstName does not contain
        defaultNextCustomerMiMiFiltering(
            "firstName.doesNotContain=" + UPDATED_FIRST_NAME,
            "firstName.doesNotContain=" + DEFAULT_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllNextCustomerMiMisByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerMiMi = nextCustomerMiMiRepository.saveAndFlush(nextCustomerMiMi);

        // Get all the nextCustomerMiMiList where lastName equals to
        defaultNextCustomerMiMiFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerMiMisByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomerMiMi = nextCustomerMiMiRepository.saveAndFlush(nextCustomerMiMi);

        // Get all the nextCustomerMiMiList where lastName in
        defaultNextCustomerMiMiFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerMiMisByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomerMiMi = nextCustomerMiMiRepository.saveAndFlush(nextCustomerMiMi);

        // Get all the nextCustomerMiMiList where lastName is not null
        defaultNextCustomerMiMiFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomerMiMisByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerMiMi = nextCustomerMiMiRepository.saveAndFlush(nextCustomerMiMi);

        // Get all the nextCustomerMiMiList where lastName contains
        defaultNextCustomerMiMiFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerMiMisByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerMiMi = nextCustomerMiMiRepository.saveAndFlush(nextCustomerMiMi);

        // Get all the nextCustomerMiMiList where lastName does not contain
        defaultNextCustomerMiMiFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllNextCustomerMiMisByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerMiMi = nextCustomerMiMiRepository.saveAndFlush(nextCustomerMiMi);

        // Get all the nextCustomerMiMiList where email equals to
        defaultNextCustomerMiMiFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomerMiMisByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomerMiMi = nextCustomerMiMiRepository.saveAndFlush(nextCustomerMiMi);

        // Get all the nextCustomerMiMiList where email in
        defaultNextCustomerMiMiFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomerMiMisByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomerMiMi = nextCustomerMiMiRepository.saveAndFlush(nextCustomerMiMi);

        // Get all the nextCustomerMiMiList where email is not null
        defaultNextCustomerMiMiFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomerMiMisByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerMiMi = nextCustomerMiMiRepository.saveAndFlush(nextCustomerMiMi);

        // Get all the nextCustomerMiMiList where email contains
        defaultNextCustomerMiMiFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomerMiMisByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerMiMi = nextCustomerMiMiRepository.saveAndFlush(nextCustomerMiMi);

        // Get all the nextCustomerMiMiList where email does not contain
        defaultNextCustomerMiMiFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextCustomerMiMisByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerMiMi = nextCustomerMiMiRepository.saveAndFlush(nextCustomerMiMi);

        // Get all the nextCustomerMiMiList where phoneNumber equals to
        defaultNextCustomerMiMiFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextCustomerMiMisByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCustomerMiMi = nextCustomerMiMiRepository.saveAndFlush(nextCustomerMiMi);

        // Get all the nextCustomerMiMiList where phoneNumber in
        defaultNextCustomerMiMiFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextCustomerMiMisByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCustomerMiMi = nextCustomerMiMiRepository.saveAndFlush(nextCustomerMiMi);

        // Get all the nextCustomerMiMiList where phoneNumber is not null
        defaultNextCustomerMiMiFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCustomerMiMisByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerMiMi = nextCustomerMiMiRepository.saveAndFlush(nextCustomerMiMi);

        // Get all the nextCustomerMiMiList where phoneNumber contains
        defaultNextCustomerMiMiFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextCustomerMiMisByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCustomerMiMi = nextCustomerMiMiRepository.saveAndFlush(nextCustomerMiMi);

        // Get all the nextCustomerMiMiList where phoneNumber does not contain
        defaultNextCustomerMiMiFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextCustomerMiMisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextCustomerMiMiRepository.saveAndFlush(nextCustomerMiMi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextCustomerMiMi.setTenant(tenant);
        nextCustomerMiMiRepository.saveAndFlush(nextCustomerMiMi);
        Long tenantId = tenant.getId();
        // Get all the nextCustomerMiMiList where tenant equals to tenantId
        defaultNextCustomerMiMiShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextCustomerMiMiList where tenant equals to (tenantId + 1)
        defaultNextCustomerMiMiShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextCustomerMiMiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextCustomerMiMiShouldBeFound(shouldBeFound);
        defaultNextCustomerMiMiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextCustomerMiMiShouldBeFound(String filter) throws Exception {
        restNextCustomerMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCustomerMiMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restNextCustomerMiMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextCustomerMiMiShouldNotBeFound(String filter) throws Exception {
        restNextCustomerMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextCustomerMiMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextCustomerMiMi() throws Exception {
        // Get the nextCustomerMiMi
        restNextCustomerMiMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextCustomerMiMi() throws Exception {
        // Initialize the database
        insertedNextCustomerMiMi = nextCustomerMiMiRepository.saveAndFlush(nextCustomerMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCustomerMiMi
        NextCustomerMiMi updatedNextCustomerMiMi = nextCustomerMiMiRepository.findById(nextCustomerMiMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextCustomerMiMi are not directly saved in db
        em.detach(updatedNextCustomerMiMi);
        updatedNextCustomerMiMi
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        NextCustomerMiMiDTO nextCustomerMiMiDTO = nextCustomerMiMiMapper.toDto(updatedNextCustomerMiMi);

        restNextCustomerMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextCustomerMiMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCustomerMiMiDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextCustomerMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextCustomerMiMiToMatchAllProperties(updatedNextCustomerMiMi);
    }

    @Test
    @Transactional
    void putNonExistingNextCustomerMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerMiMi.setId(longCount.incrementAndGet());

        // Create the NextCustomerMiMi
        NextCustomerMiMiDTO nextCustomerMiMiDTO = nextCustomerMiMiMapper.toDto(nextCustomerMiMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCustomerMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextCustomerMiMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCustomerMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextCustomerMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerMiMi.setId(longCount.incrementAndGet());

        // Create the NextCustomerMiMi
        NextCustomerMiMiDTO nextCustomerMiMiDTO = nextCustomerMiMiMapper.toDto(nextCustomerMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCustomerMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextCustomerMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerMiMi.setId(longCount.incrementAndGet());

        // Create the NextCustomerMiMi
        NextCustomerMiMiDTO nextCustomerMiMiDTO = nextCustomerMiMiMapper.toDto(nextCustomerMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerMiMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCustomerMiMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCustomerMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextCustomerMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextCustomerMiMi = nextCustomerMiMiRepository.saveAndFlush(nextCustomerMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCustomerMiMi using partial update
        NextCustomerMiMi partialUpdatedNextCustomerMiMi = new NextCustomerMiMi();
        partialUpdatedNextCustomerMiMi.setId(nextCustomerMiMi.getId());

        partialUpdatedNextCustomerMiMi.phoneNumber(UPDATED_PHONE_NUMBER);

        restNextCustomerMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCustomerMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCustomerMiMi))
            )
            .andExpect(status().isOk());

        // Validate the NextCustomerMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCustomerMiMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextCustomerMiMi, nextCustomerMiMi),
            getPersistedNextCustomerMiMi(nextCustomerMiMi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextCustomerMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextCustomerMiMi = nextCustomerMiMiRepository.saveAndFlush(nextCustomerMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCustomerMiMi using partial update
        NextCustomerMiMi partialUpdatedNextCustomerMiMi = new NextCustomerMiMi();
        partialUpdatedNextCustomerMiMi.setId(nextCustomerMiMi.getId());

        partialUpdatedNextCustomerMiMi
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restNextCustomerMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCustomerMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCustomerMiMi))
            )
            .andExpect(status().isOk());

        // Validate the NextCustomerMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCustomerMiMiUpdatableFieldsEquals(
            partialUpdatedNextCustomerMiMi,
            getPersistedNextCustomerMiMi(partialUpdatedNextCustomerMiMi)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextCustomerMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerMiMi.setId(longCount.incrementAndGet());

        // Create the NextCustomerMiMi
        NextCustomerMiMiDTO nextCustomerMiMiDTO = nextCustomerMiMiMapper.toDto(nextCustomerMiMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCustomerMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextCustomerMiMiDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCustomerMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextCustomerMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerMiMi.setId(longCount.incrementAndGet());

        // Create the NextCustomerMiMi
        NextCustomerMiMiDTO nextCustomerMiMiDTO = nextCustomerMiMiMapper.toDto(nextCustomerMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCustomerMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCustomerMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextCustomerMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCustomerMiMi.setId(longCount.incrementAndGet());

        // Create the NextCustomerMiMi
        NextCustomerMiMiDTO nextCustomerMiMiDTO = nextCustomerMiMiMapper.toDto(nextCustomerMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCustomerMiMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextCustomerMiMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCustomerMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextCustomerMiMi() throws Exception {
        // Initialize the database
        insertedNextCustomerMiMi = nextCustomerMiMiRepository.saveAndFlush(nextCustomerMiMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextCustomerMiMi
        restNextCustomerMiMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextCustomerMiMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextCustomerMiMiRepository.count();
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

    protected NextCustomerMiMi getPersistedNextCustomerMiMi(NextCustomerMiMi nextCustomerMiMi) {
        return nextCustomerMiMiRepository.findById(nextCustomerMiMi.getId()).orElseThrow();
    }

    protected void assertPersistedNextCustomerMiMiToMatchAllProperties(NextCustomerMiMi expectedNextCustomerMiMi) {
        assertNextCustomerMiMiAllPropertiesEquals(expectedNextCustomerMiMi, getPersistedNextCustomerMiMi(expectedNextCustomerMiMi));
    }

    protected void assertPersistedNextCustomerMiMiToMatchUpdatableProperties(NextCustomerMiMi expectedNextCustomerMiMi) {
        assertNextCustomerMiMiAllUpdatablePropertiesEquals(
            expectedNextCustomerMiMi,
            getPersistedNextCustomerMiMi(expectedNextCustomerMiMi)
        );
    }
}
