package ai.realworld.web.rest;

import static ai.realworld.domain.AlProtyAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlApple;
import ai.realworld.domain.AlProPro;
import ai.realworld.domain.AlProty;
import ai.realworld.domain.AlProty;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.domain.enumeration.PeteStatus;
import ai.realworld.repository.AlProtyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AlProtyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlProtyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION_HEITIGA = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_HEITIGA = "BBBBBBBBBB";

    private static final String DEFAULT_COORDINATE = "AAAAAAAAAA";
    private static final String UPDATED_COORDINATE = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final PeteStatus DEFAULT_STATUS = PeteStatus.ACTIVE;
    private static final PeteStatus UPDATED_STATUS = PeteStatus.INACTIVE;

    private static final Boolean DEFAULT_IS_ENABLED = false;
    private static final Boolean UPDATED_IS_ENABLED = true;

    private static final String ENTITY_API_URL = "/api/al-proties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlProtyRepository alProtyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlProtyMockMvc;

    private AlProty alProty;

    private AlProty insertedAlProty;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlProty createEntity(EntityManager em) {
        AlProty alProty = new AlProty()
            .name(DEFAULT_NAME)
            .descriptionHeitiga(DEFAULT_DESCRIPTION_HEITIGA)
            .coordinate(DEFAULT_COORDINATE)
            .code(DEFAULT_CODE)
            .status(DEFAULT_STATUS)
            .isEnabled(DEFAULT_IS_ENABLED);
        // Add required entity
        AlApple alApple;
        if (TestUtil.findAll(em, AlApple.class).isEmpty()) {
            alApple = AlAppleResourceIT.createEntity(em);
            em.persist(alApple);
            em.flush();
        } else {
            alApple = TestUtil.findAll(em, AlApple.class).get(0);
        }
        alProty.setOperator(alApple);
        return alProty;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlProty createUpdatedEntity(EntityManager em) {
        AlProty updatedAlProty = new AlProty()
            .name(UPDATED_NAME)
            .descriptionHeitiga(UPDATED_DESCRIPTION_HEITIGA)
            .coordinate(UPDATED_COORDINATE)
            .code(UPDATED_CODE)
            .status(UPDATED_STATUS)
            .isEnabled(UPDATED_IS_ENABLED);
        // Add required entity
        AlApple alApple;
        if (TestUtil.findAll(em, AlApple.class).isEmpty()) {
            alApple = AlAppleResourceIT.createUpdatedEntity(em);
            em.persist(alApple);
            em.flush();
        } else {
            alApple = TestUtil.findAll(em, AlApple.class).get(0);
        }
        updatedAlProty.setOperator(alApple);
        return updatedAlProty;
    }

    @BeforeEach
    public void initTest() {
        alProty = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlProty != null) {
            alProtyRepository.delete(insertedAlProty);
            insertedAlProty = null;
        }
    }

    @Test
    @Transactional
    void createAlProty() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlProty
        var returnedAlProty = om.readValue(
            restAlProtyMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alProty)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlProty.class
        );

        // Validate the AlProty in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlProtyUpdatableFieldsEquals(returnedAlProty, getPersistedAlProty(returnedAlProty));

        insertedAlProty = returnedAlProty;
    }

    @Test
    @Transactional
    void createAlProtyWithExistingId() throws Exception {
        // Create the AlProty with an existing ID
        insertedAlProty = alProtyRepository.saveAndFlush(alProty);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlProtyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alProty)))
            .andExpect(status().isBadRequest());

        // Validate the AlProty in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alProty.setName(null);

        // Create the AlProty, which fails.

        restAlProtyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alProty)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlProties() throws Exception {
        // Initialize the database
        insertedAlProty = alProtyRepository.saveAndFlush(alProty);

        // Get all the alProtyList
        restAlProtyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alProty.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].descriptionHeitiga").value(hasItem(DEFAULT_DESCRIPTION_HEITIGA)))
            .andExpect(jsonPath("$.[*].coordinate").value(hasItem(DEFAULT_COORDINATE)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].isEnabled").value(hasItem(DEFAULT_IS_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    void getAlProty() throws Exception {
        // Initialize the database
        insertedAlProty = alProtyRepository.saveAndFlush(alProty);

        // Get the alProty
        restAlProtyMockMvc
            .perform(get(ENTITY_API_URL_ID, alProty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alProty.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.descriptionHeitiga").value(DEFAULT_DESCRIPTION_HEITIGA))
            .andExpect(jsonPath("$.coordinate").value(DEFAULT_COORDINATE))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.isEnabled").value(DEFAULT_IS_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    void getAlProtiesByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlProty = alProtyRepository.saveAndFlush(alProty);

        UUID id = alProty.getId();

        defaultAlProtyFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllAlProtiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProty = alProtyRepository.saveAndFlush(alProty);

        // Get all the alProtyList where name equals to
        defaultAlProtyFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlProtiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlProty = alProtyRepository.saveAndFlush(alProty);

        // Get all the alProtyList where name in
        defaultAlProtyFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlProtiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlProty = alProtyRepository.saveAndFlush(alProty);

        // Get all the alProtyList where name is not null
        defaultAlProtyFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAlProtiesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlProty = alProtyRepository.saveAndFlush(alProty);

        // Get all the alProtyList where name contains
        defaultAlProtyFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlProtiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlProty = alProtyRepository.saveAndFlush(alProty);

        // Get all the alProtyList where name does not contain
        defaultAlProtyFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAlProtiesByDescriptionHeitigaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProty = alProtyRepository.saveAndFlush(alProty);

        // Get all the alProtyList where descriptionHeitiga equals to
        defaultAlProtyFiltering(
            "descriptionHeitiga.equals=" + DEFAULT_DESCRIPTION_HEITIGA,
            "descriptionHeitiga.equals=" + UPDATED_DESCRIPTION_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlProtiesByDescriptionHeitigaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlProty = alProtyRepository.saveAndFlush(alProty);

        // Get all the alProtyList where descriptionHeitiga in
        defaultAlProtyFiltering(
            "descriptionHeitiga.in=" + DEFAULT_DESCRIPTION_HEITIGA + "," + UPDATED_DESCRIPTION_HEITIGA,
            "descriptionHeitiga.in=" + UPDATED_DESCRIPTION_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlProtiesByDescriptionHeitigaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlProty = alProtyRepository.saveAndFlush(alProty);

        // Get all the alProtyList where descriptionHeitiga is not null
        defaultAlProtyFiltering("descriptionHeitiga.specified=true", "descriptionHeitiga.specified=false");
    }

    @Test
    @Transactional
    void getAllAlProtiesByDescriptionHeitigaContainsSomething() throws Exception {
        // Initialize the database
        insertedAlProty = alProtyRepository.saveAndFlush(alProty);

        // Get all the alProtyList where descriptionHeitiga contains
        defaultAlProtyFiltering(
            "descriptionHeitiga.contains=" + DEFAULT_DESCRIPTION_HEITIGA,
            "descriptionHeitiga.contains=" + UPDATED_DESCRIPTION_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlProtiesByDescriptionHeitigaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlProty = alProtyRepository.saveAndFlush(alProty);

        // Get all the alProtyList where descriptionHeitiga does not contain
        defaultAlProtyFiltering(
            "descriptionHeitiga.doesNotContain=" + UPDATED_DESCRIPTION_HEITIGA,
            "descriptionHeitiga.doesNotContain=" + DEFAULT_DESCRIPTION_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlProtiesByCoordinateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProty = alProtyRepository.saveAndFlush(alProty);

        // Get all the alProtyList where coordinate equals to
        defaultAlProtyFiltering("coordinate.equals=" + DEFAULT_COORDINATE, "coordinate.equals=" + UPDATED_COORDINATE);
    }

    @Test
    @Transactional
    void getAllAlProtiesByCoordinateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlProty = alProtyRepository.saveAndFlush(alProty);

        // Get all the alProtyList where coordinate in
        defaultAlProtyFiltering("coordinate.in=" + DEFAULT_COORDINATE + "," + UPDATED_COORDINATE, "coordinate.in=" + UPDATED_COORDINATE);
    }

    @Test
    @Transactional
    void getAllAlProtiesByCoordinateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlProty = alProtyRepository.saveAndFlush(alProty);

        // Get all the alProtyList where coordinate is not null
        defaultAlProtyFiltering("coordinate.specified=true", "coordinate.specified=false");
    }

    @Test
    @Transactional
    void getAllAlProtiesByCoordinateContainsSomething() throws Exception {
        // Initialize the database
        insertedAlProty = alProtyRepository.saveAndFlush(alProty);

        // Get all the alProtyList where coordinate contains
        defaultAlProtyFiltering("coordinate.contains=" + DEFAULT_COORDINATE, "coordinate.contains=" + UPDATED_COORDINATE);
    }

    @Test
    @Transactional
    void getAllAlProtiesByCoordinateNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlProty = alProtyRepository.saveAndFlush(alProty);

        // Get all the alProtyList where coordinate does not contain
        defaultAlProtyFiltering("coordinate.doesNotContain=" + UPDATED_COORDINATE, "coordinate.doesNotContain=" + DEFAULT_COORDINATE);
    }

    @Test
    @Transactional
    void getAllAlProtiesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProty = alProtyRepository.saveAndFlush(alProty);

        // Get all the alProtyList where code equals to
        defaultAlProtyFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAlProtiesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlProty = alProtyRepository.saveAndFlush(alProty);

        // Get all the alProtyList where code in
        defaultAlProtyFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAlProtiesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlProty = alProtyRepository.saveAndFlush(alProty);

        // Get all the alProtyList where code is not null
        defaultAlProtyFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllAlProtiesByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedAlProty = alProtyRepository.saveAndFlush(alProty);

        // Get all the alProtyList where code contains
        defaultAlProtyFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAlProtiesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlProty = alProtyRepository.saveAndFlush(alProty);

        // Get all the alProtyList where code does not contain
        defaultAlProtyFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllAlProtiesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProty = alProtyRepository.saveAndFlush(alProty);

        // Get all the alProtyList where status equals to
        defaultAlProtyFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllAlProtiesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlProty = alProtyRepository.saveAndFlush(alProty);

        // Get all the alProtyList where status in
        defaultAlProtyFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllAlProtiesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlProty = alProtyRepository.saveAndFlush(alProty);

        // Get all the alProtyList where status is not null
        defaultAlProtyFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllAlProtiesByIsEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProty = alProtyRepository.saveAndFlush(alProty);

        // Get all the alProtyList where isEnabled equals to
        defaultAlProtyFiltering("isEnabled.equals=" + DEFAULT_IS_ENABLED, "isEnabled.equals=" + UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    void getAllAlProtiesByIsEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlProty = alProtyRepository.saveAndFlush(alProty);

        // Get all the alProtyList where isEnabled in
        defaultAlProtyFiltering("isEnabled.in=" + DEFAULT_IS_ENABLED + "," + UPDATED_IS_ENABLED, "isEnabled.in=" + UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    void getAllAlProtiesByIsEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlProty = alProtyRepository.saveAndFlush(alProty);

        // Get all the alProtyList where isEnabled is not null
        defaultAlProtyFiltering("isEnabled.specified=true", "isEnabled.specified=false");
    }

    @Test
    @Transactional
    void getAllAlProtiesByParentIsEqualToSomething() throws Exception {
        AlProty parent;
        if (TestUtil.findAll(em, AlProty.class).isEmpty()) {
            alProtyRepository.saveAndFlush(alProty);
            parent = AlProtyResourceIT.createEntity(em);
        } else {
            parent = TestUtil.findAll(em, AlProty.class).get(0);
        }
        em.persist(parent);
        em.flush();
        alProty.setParent(parent);
        alProtyRepository.saveAndFlush(alProty);
        UUID parentId = parent.getId();
        // Get all the alProtyList where parent equals to parentId
        defaultAlProtyShouldBeFound("parentId.equals=" + parentId);

        // Get all the alProtyList where parent equals to UUID.randomUUID()
        defaultAlProtyShouldNotBeFound("parentId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlProtiesByOperatorIsEqualToSomething() throws Exception {
        AlApple operator;
        if (TestUtil.findAll(em, AlApple.class).isEmpty()) {
            alProtyRepository.saveAndFlush(alProty);
            operator = AlAppleResourceIT.createEntity(em);
        } else {
            operator = TestUtil.findAll(em, AlApple.class).get(0);
        }
        em.persist(operator);
        em.flush();
        alProty.setOperator(operator);
        alProtyRepository.saveAndFlush(alProty);
        UUID operatorId = operator.getId();
        // Get all the alProtyList where operator equals to operatorId
        defaultAlProtyShouldBeFound("operatorId.equals=" + operatorId);

        // Get all the alProtyList where operator equals to UUID.randomUUID()
        defaultAlProtyShouldNotBeFound("operatorId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlProtiesByPropertyProfileIsEqualToSomething() throws Exception {
        AlProPro propertyProfile;
        if (TestUtil.findAll(em, AlProPro.class).isEmpty()) {
            alProtyRepository.saveAndFlush(alProty);
            propertyProfile = AlProProResourceIT.createEntity();
        } else {
            propertyProfile = TestUtil.findAll(em, AlProPro.class).get(0);
        }
        em.persist(propertyProfile);
        em.flush();
        alProty.setPropertyProfile(propertyProfile);
        alProtyRepository.saveAndFlush(alProty);
        UUID propertyProfileId = propertyProfile.getId();
        // Get all the alProtyList where propertyProfile equals to propertyProfileId
        defaultAlProtyShouldBeFound("propertyProfileId.equals=" + propertyProfileId);

        // Get all the alProtyList where propertyProfile equals to UUID.randomUUID()
        defaultAlProtyShouldNotBeFound("propertyProfileId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlProtiesByAvatarIsEqualToSomething() throws Exception {
        Metaverse avatar;
        if (TestUtil.findAll(em, Metaverse.class).isEmpty()) {
            alProtyRepository.saveAndFlush(alProty);
            avatar = MetaverseResourceIT.createEntity();
        } else {
            avatar = TestUtil.findAll(em, Metaverse.class).get(0);
        }
        em.persist(avatar);
        em.flush();
        alProty.setAvatar(avatar);
        alProtyRepository.saveAndFlush(alProty);
        Long avatarId = avatar.getId();
        // Get all the alProtyList where avatar equals to avatarId
        defaultAlProtyShouldBeFound("avatarId.equals=" + avatarId);

        // Get all the alProtyList where avatar equals to (avatarId + 1)
        defaultAlProtyShouldNotBeFound("avatarId.equals=" + (avatarId + 1));
    }

    @Test
    @Transactional
    void getAllAlProtiesByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alProtyRepository.saveAndFlush(alProty);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alProty.setApplication(application);
        alProtyRepository.saveAndFlush(alProty);
        UUID applicationId = application.getId();
        // Get all the alProtyList where application equals to applicationId
        defaultAlProtyShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alProtyList where application equals to UUID.randomUUID()
        defaultAlProtyShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlProtyFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlProtyShouldBeFound(shouldBeFound);
        defaultAlProtyShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlProtyShouldBeFound(String filter) throws Exception {
        restAlProtyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alProty.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].descriptionHeitiga").value(hasItem(DEFAULT_DESCRIPTION_HEITIGA)))
            .andExpect(jsonPath("$.[*].coordinate").value(hasItem(DEFAULT_COORDINATE)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].isEnabled").value(hasItem(DEFAULT_IS_ENABLED.booleanValue())));

        // Check, that the count call also returns 1
        restAlProtyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlProtyShouldNotBeFound(String filter) throws Exception {
        restAlProtyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlProtyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlProty() throws Exception {
        // Get the alProty
        restAlProtyMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlProty() throws Exception {
        // Initialize the database
        insertedAlProty = alProtyRepository.saveAndFlush(alProty);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alProty
        AlProty updatedAlProty = alProtyRepository.findById(alProty.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlProty are not directly saved in db
        em.detach(updatedAlProty);
        updatedAlProty
            .name(UPDATED_NAME)
            .descriptionHeitiga(UPDATED_DESCRIPTION_HEITIGA)
            .coordinate(UPDATED_COORDINATE)
            .code(UPDATED_CODE)
            .status(UPDATED_STATUS)
            .isEnabled(UPDATED_IS_ENABLED);

        restAlProtyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlProty.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlProty))
            )
            .andExpect(status().isOk());

        // Validate the AlProty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlProtyToMatchAllProperties(updatedAlProty);
    }

    @Test
    @Transactional
    void putNonExistingAlProty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alProty.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlProtyMockMvc
            .perform(put(ENTITY_API_URL_ID, alProty.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alProty)))
            .andExpect(status().isBadRequest());

        // Validate the AlProty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlProty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alProty.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlProtyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alProty))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlProty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlProty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alProty.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlProtyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alProty)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlProty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlProtyWithPatch() throws Exception {
        // Initialize the database
        insertedAlProty = alProtyRepository.saveAndFlush(alProty);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alProty using partial update
        AlProty partialUpdatedAlProty = new AlProty();
        partialUpdatedAlProty.setId(alProty.getId());

        partialUpdatedAlProty
            .name(UPDATED_NAME)
            .coordinate(UPDATED_COORDINATE)
            .code(UPDATED_CODE)
            .status(UPDATED_STATUS)
            .isEnabled(UPDATED_IS_ENABLED);

        restAlProtyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlProty.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlProty))
            )
            .andExpect(status().isOk());

        // Validate the AlProty in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlProtyUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAlProty, alProty), getPersistedAlProty(alProty));
    }

    @Test
    @Transactional
    void fullUpdateAlProtyWithPatch() throws Exception {
        // Initialize the database
        insertedAlProty = alProtyRepository.saveAndFlush(alProty);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alProty using partial update
        AlProty partialUpdatedAlProty = new AlProty();
        partialUpdatedAlProty.setId(alProty.getId());

        partialUpdatedAlProty
            .name(UPDATED_NAME)
            .descriptionHeitiga(UPDATED_DESCRIPTION_HEITIGA)
            .coordinate(UPDATED_COORDINATE)
            .code(UPDATED_CODE)
            .status(UPDATED_STATUS)
            .isEnabled(UPDATED_IS_ENABLED);

        restAlProtyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlProty.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlProty))
            )
            .andExpect(status().isOk());

        // Validate the AlProty in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlProtyUpdatableFieldsEquals(partialUpdatedAlProty, getPersistedAlProty(partialUpdatedAlProty));
    }

    @Test
    @Transactional
    void patchNonExistingAlProty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alProty.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlProtyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alProty.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alProty))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlProty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlProty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alProty.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlProtyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alProty))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlProty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlProty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alProty.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlProtyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alProty)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlProty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlProty() throws Exception {
        // Initialize the database
        insertedAlProty = alProtyRepository.saveAndFlush(alProty);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alProty
        restAlProtyMockMvc
            .perform(delete(ENTITY_API_URL_ID, alProty.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alProtyRepository.count();
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

    protected AlProty getPersistedAlProty(AlProty alProty) {
        return alProtyRepository.findById(alProty.getId()).orElseThrow();
    }

    protected void assertPersistedAlProtyToMatchAllProperties(AlProty expectedAlProty) {
        assertAlProtyAllPropertiesEquals(expectedAlProty, getPersistedAlProty(expectedAlProty));
    }

    protected void assertPersistedAlProtyToMatchUpdatableProperties(AlProty expectedAlProty) {
        assertAlProtyAllUpdatablePropertiesEquals(expectedAlProty, getPersistedAlProty(expectedAlProty));
    }
}
