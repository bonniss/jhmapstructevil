package ai.realworld.web.rest;

import static ai.realworld.domain.JohnLennonAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.Initium;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.domain.OlAlmantinoMilo;
import ai.realworld.domain.OlMaster;
import ai.realworld.domain.enumeration.EcmaScript;
import ai.realworld.repository.JohnLennonRepository;
import ai.realworld.service.dto.JohnLennonDTO;
import ai.realworld.service.mapper.JohnLennonMapper;
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
 * Integration tests for the {@link JohnLennonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class JohnLennonResourceIT {

    private static final EcmaScript DEFAULT_PROVIDER = EcmaScript.JELLO;
    private static final EcmaScript UPDATED_PROVIDER = EcmaScript.OTHER;

    private static final String DEFAULT_PROVIDER_APP_ID = "AAAAAAAAAA";
    private static final String UPDATED_PROVIDER_APP_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SLUG = "AAAAAAAAAA";
    private static final String UPDATED_SLUG = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ENABLED = false;
    private static final Boolean UPDATED_IS_ENABLED = true;

    private static final String ENTITY_API_URL = "/api/john-lennons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private JohnLennonRepository johnLennonRepository;

    @Autowired
    private JohnLennonMapper johnLennonMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJohnLennonMockMvc;

    private JohnLennon johnLennon;

    private JohnLennon insertedJohnLennon;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JohnLennon createEntity() {
        return new JohnLennon()
            .provider(DEFAULT_PROVIDER)
            .providerAppId(DEFAULT_PROVIDER_APP_ID)
            .name(DEFAULT_NAME)
            .slug(DEFAULT_SLUG)
            .isEnabled(DEFAULT_IS_ENABLED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JohnLennon createUpdatedEntity() {
        return new JohnLennon()
            .provider(UPDATED_PROVIDER)
            .providerAppId(UPDATED_PROVIDER_APP_ID)
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .isEnabled(UPDATED_IS_ENABLED);
    }

    @BeforeEach
    public void initTest() {
        johnLennon = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedJohnLennon != null) {
            johnLennonRepository.delete(insertedJohnLennon);
            insertedJohnLennon = null;
        }
    }

    @Test
    @Transactional
    void createJohnLennon() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the JohnLennon
        JohnLennonDTO johnLennonDTO = johnLennonMapper.toDto(johnLennon);
        var returnedJohnLennonDTO = om.readValue(
            restJohnLennonMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(johnLennonDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            JohnLennonDTO.class
        );

        // Validate the JohnLennon in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedJohnLennon = johnLennonMapper.toEntity(returnedJohnLennonDTO);
        assertJohnLennonUpdatableFieldsEquals(returnedJohnLennon, getPersistedJohnLennon(returnedJohnLennon));

        insertedJohnLennon = returnedJohnLennon;
    }

    @Test
    @Transactional
    void createJohnLennonWithExistingId() throws Exception {
        // Create the JohnLennon with an existing ID
        insertedJohnLennon = johnLennonRepository.saveAndFlush(johnLennon);
        JohnLennonDTO johnLennonDTO = johnLennonMapper.toDto(johnLennon);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJohnLennonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(johnLennonDTO)))
            .andExpect(status().isBadRequest());

        // Validate the JohnLennon in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        johnLennon.setName(null);

        // Create the JohnLennon, which fails.
        JohnLennonDTO johnLennonDTO = johnLennonMapper.toDto(johnLennon);

        restJohnLennonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(johnLennonDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSlugIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        johnLennon.setSlug(null);

        // Create the JohnLennon, which fails.
        JohnLennonDTO johnLennonDTO = johnLennonMapper.toDto(johnLennon);

        restJohnLennonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(johnLennonDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllJohnLennons() throws Exception {
        // Initialize the database
        insertedJohnLennon = johnLennonRepository.saveAndFlush(johnLennon);

        // Get all the johnLennonList
        restJohnLennonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(johnLennon.getId().toString())))
            .andExpect(jsonPath("$.[*].provider").value(hasItem(DEFAULT_PROVIDER.toString())))
            .andExpect(jsonPath("$.[*].providerAppId").value(hasItem(DEFAULT_PROVIDER_APP_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].isEnabled").value(hasItem(DEFAULT_IS_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    void getJohnLennon() throws Exception {
        // Initialize the database
        insertedJohnLennon = johnLennonRepository.saveAndFlush(johnLennon);

        // Get the johnLennon
        restJohnLennonMockMvc
            .perform(get(ENTITY_API_URL_ID, johnLennon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(johnLennon.getId().toString()))
            .andExpect(jsonPath("$.provider").value(DEFAULT_PROVIDER.toString()))
            .andExpect(jsonPath("$.providerAppId").value(DEFAULT_PROVIDER_APP_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG))
            .andExpect(jsonPath("$.isEnabled").value(DEFAULT_IS_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    void getJohnLennonsByIdFiltering() throws Exception {
        // Initialize the database
        insertedJohnLennon = johnLennonRepository.saveAndFlush(johnLennon);

        UUID id = johnLennon.getId();

        defaultJohnLennonFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllJohnLennonsByProviderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedJohnLennon = johnLennonRepository.saveAndFlush(johnLennon);

        // Get all the johnLennonList where provider equals to
        defaultJohnLennonFiltering("provider.equals=" + DEFAULT_PROVIDER, "provider.equals=" + UPDATED_PROVIDER);
    }

    @Test
    @Transactional
    void getAllJohnLennonsByProviderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedJohnLennon = johnLennonRepository.saveAndFlush(johnLennon);

        // Get all the johnLennonList where provider in
        defaultJohnLennonFiltering("provider.in=" + DEFAULT_PROVIDER + "," + UPDATED_PROVIDER, "provider.in=" + UPDATED_PROVIDER);
    }

    @Test
    @Transactional
    void getAllJohnLennonsByProviderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedJohnLennon = johnLennonRepository.saveAndFlush(johnLennon);

        // Get all the johnLennonList where provider is not null
        defaultJohnLennonFiltering("provider.specified=true", "provider.specified=false");
    }

    @Test
    @Transactional
    void getAllJohnLennonsByProviderAppIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedJohnLennon = johnLennonRepository.saveAndFlush(johnLennon);

        // Get all the johnLennonList where providerAppId equals to
        defaultJohnLennonFiltering("providerAppId.equals=" + DEFAULT_PROVIDER_APP_ID, "providerAppId.equals=" + UPDATED_PROVIDER_APP_ID);
    }

    @Test
    @Transactional
    void getAllJohnLennonsByProviderAppIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedJohnLennon = johnLennonRepository.saveAndFlush(johnLennon);

        // Get all the johnLennonList where providerAppId in
        defaultJohnLennonFiltering(
            "providerAppId.in=" + DEFAULT_PROVIDER_APP_ID + "," + UPDATED_PROVIDER_APP_ID,
            "providerAppId.in=" + UPDATED_PROVIDER_APP_ID
        );
    }

    @Test
    @Transactional
    void getAllJohnLennonsByProviderAppIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedJohnLennon = johnLennonRepository.saveAndFlush(johnLennon);

        // Get all the johnLennonList where providerAppId is not null
        defaultJohnLennonFiltering("providerAppId.specified=true", "providerAppId.specified=false");
    }

    @Test
    @Transactional
    void getAllJohnLennonsByProviderAppIdContainsSomething() throws Exception {
        // Initialize the database
        insertedJohnLennon = johnLennonRepository.saveAndFlush(johnLennon);

        // Get all the johnLennonList where providerAppId contains
        defaultJohnLennonFiltering(
            "providerAppId.contains=" + DEFAULT_PROVIDER_APP_ID,
            "providerAppId.contains=" + UPDATED_PROVIDER_APP_ID
        );
    }

    @Test
    @Transactional
    void getAllJohnLennonsByProviderAppIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedJohnLennon = johnLennonRepository.saveAndFlush(johnLennon);

        // Get all the johnLennonList where providerAppId does not contain
        defaultJohnLennonFiltering(
            "providerAppId.doesNotContain=" + UPDATED_PROVIDER_APP_ID,
            "providerAppId.doesNotContain=" + DEFAULT_PROVIDER_APP_ID
        );
    }

    @Test
    @Transactional
    void getAllJohnLennonsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedJohnLennon = johnLennonRepository.saveAndFlush(johnLennon);

        // Get all the johnLennonList where name equals to
        defaultJohnLennonFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllJohnLennonsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedJohnLennon = johnLennonRepository.saveAndFlush(johnLennon);

        // Get all the johnLennonList where name in
        defaultJohnLennonFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllJohnLennonsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedJohnLennon = johnLennonRepository.saveAndFlush(johnLennon);

        // Get all the johnLennonList where name is not null
        defaultJohnLennonFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllJohnLennonsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedJohnLennon = johnLennonRepository.saveAndFlush(johnLennon);

        // Get all the johnLennonList where name contains
        defaultJohnLennonFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllJohnLennonsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedJohnLennon = johnLennonRepository.saveAndFlush(johnLennon);

        // Get all the johnLennonList where name does not contain
        defaultJohnLennonFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllJohnLennonsBySlugIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedJohnLennon = johnLennonRepository.saveAndFlush(johnLennon);

        // Get all the johnLennonList where slug equals to
        defaultJohnLennonFiltering("slug.equals=" + DEFAULT_SLUG, "slug.equals=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllJohnLennonsBySlugIsInShouldWork() throws Exception {
        // Initialize the database
        insertedJohnLennon = johnLennonRepository.saveAndFlush(johnLennon);

        // Get all the johnLennonList where slug in
        defaultJohnLennonFiltering("slug.in=" + DEFAULT_SLUG + "," + UPDATED_SLUG, "slug.in=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllJohnLennonsBySlugIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedJohnLennon = johnLennonRepository.saveAndFlush(johnLennon);

        // Get all the johnLennonList where slug is not null
        defaultJohnLennonFiltering("slug.specified=true", "slug.specified=false");
    }

    @Test
    @Transactional
    void getAllJohnLennonsBySlugContainsSomething() throws Exception {
        // Initialize the database
        insertedJohnLennon = johnLennonRepository.saveAndFlush(johnLennon);

        // Get all the johnLennonList where slug contains
        defaultJohnLennonFiltering("slug.contains=" + DEFAULT_SLUG, "slug.contains=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllJohnLennonsBySlugNotContainsSomething() throws Exception {
        // Initialize the database
        insertedJohnLennon = johnLennonRepository.saveAndFlush(johnLennon);

        // Get all the johnLennonList where slug does not contain
        defaultJohnLennonFiltering("slug.doesNotContain=" + UPDATED_SLUG, "slug.doesNotContain=" + DEFAULT_SLUG);
    }

    @Test
    @Transactional
    void getAllJohnLennonsByIsEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedJohnLennon = johnLennonRepository.saveAndFlush(johnLennon);

        // Get all the johnLennonList where isEnabled equals to
        defaultJohnLennonFiltering("isEnabled.equals=" + DEFAULT_IS_ENABLED, "isEnabled.equals=" + UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    void getAllJohnLennonsByIsEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        insertedJohnLennon = johnLennonRepository.saveAndFlush(johnLennon);

        // Get all the johnLennonList where isEnabled in
        defaultJohnLennonFiltering("isEnabled.in=" + DEFAULT_IS_ENABLED + "," + UPDATED_IS_ENABLED, "isEnabled.in=" + UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    void getAllJohnLennonsByIsEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedJohnLennon = johnLennonRepository.saveAndFlush(johnLennon);

        // Get all the johnLennonList where isEnabled is not null
        defaultJohnLennonFiltering("isEnabled.specified=true", "isEnabled.specified=false");
    }

    @Test
    @Transactional
    void getAllJohnLennonsByLogoIsEqualToSomething() throws Exception {
        Metaverse logo;
        if (TestUtil.findAll(em, Metaverse.class).isEmpty()) {
            johnLennonRepository.saveAndFlush(johnLennon);
            logo = MetaverseResourceIT.createEntity();
        } else {
            logo = TestUtil.findAll(em, Metaverse.class).get(0);
        }
        em.persist(logo);
        em.flush();
        johnLennon.setLogo(logo);
        johnLennonRepository.saveAndFlush(johnLennon);
        Long logoId = logo.getId();
        // Get all the johnLennonList where logo equals to logoId
        defaultJohnLennonShouldBeFound("logoId.equals=" + logoId);

        // Get all the johnLennonList where logo equals to (logoId + 1)
        defaultJohnLennonShouldNotBeFound("logoId.equals=" + (logoId + 1));
    }

    @Test
    @Transactional
    void getAllJohnLennonsByAppManagerIsEqualToSomething() throws Exception {
        OlAlmantinoMilo appManager;
        if (TestUtil.findAll(em, OlAlmantinoMilo.class).isEmpty()) {
            johnLennonRepository.saveAndFlush(johnLennon);
            appManager = OlAlmantinoMiloResourceIT.createEntity();
        } else {
            appManager = TestUtil.findAll(em, OlAlmantinoMilo.class).get(0);
        }
        em.persist(appManager);
        em.flush();
        johnLennon.setAppManager(appManager);
        johnLennonRepository.saveAndFlush(johnLennon);
        UUID appManagerId = appManager.getId();
        // Get all the johnLennonList where appManager equals to appManagerId
        defaultJohnLennonShouldBeFound("appManagerId.equals=" + appManagerId);

        // Get all the johnLennonList where appManager equals to UUID.randomUUID()
        defaultJohnLennonShouldNotBeFound("appManagerId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllJohnLennonsByOrganizationIsEqualToSomething() throws Exception {
        OlMaster organization;
        if (TestUtil.findAll(em, OlMaster.class).isEmpty()) {
            johnLennonRepository.saveAndFlush(johnLennon);
            organization = OlMasterResourceIT.createEntity();
        } else {
            organization = TestUtil.findAll(em, OlMaster.class).get(0);
        }
        em.persist(organization);
        em.flush();
        johnLennon.setOrganization(organization);
        johnLennonRepository.saveAndFlush(johnLennon);
        UUID organizationId = organization.getId();
        // Get all the johnLennonList where organization equals to organizationId
        defaultJohnLennonShouldBeFound("organizationId.equals=" + organizationId);

        // Get all the johnLennonList where organization equals to UUID.randomUUID()
        defaultJohnLennonShouldNotBeFound("organizationId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllJohnLennonsByJelloInitiumIsEqualToSomething() throws Exception {
        Initium jelloInitium;
        if (TestUtil.findAll(em, Initium.class).isEmpty()) {
            johnLennonRepository.saveAndFlush(johnLennon);
            jelloInitium = InitiumResourceIT.createEntity();
        } else {
            jelloInitium = TestUtil.findAll(em, Initium.class).get(0);
        }
        em.persist(jelloInitium);
        em.flush();
        johnLennon.setJelloInitium(jelloInitium);
        johnLennonRepository.saveAndFlush(johnLennon);
        Long jelloInitiumId = jelloInitium.getId();
        // Get all the johnLennonList where jelloInitium equals to jelloInitiumId
        defaultJohnLennonShouldBeFound("jelloInitiumId.equals=" + jelloInitiumId);

        // Get all the johnLennonList where jelloInitium equals to (jelloInitiumId + 1)
        defaultJohnLennonShouldNotBeFound("jelloInitiumId.equals=" + (jelloInitiumId + 1));
    }

    @Test
    @Transactional
    void getAllJohnLennonsByInhouseInitiumIsEqualToSomething() throws Exception {
        Initium inhouseInitium;
        if (TestUtil.findAll(em, Initium.class).isEmpty()) {
            johnLennonRepository.saveAndFlush(johnLennon);
            inhouseInitium = InitiumResourceIT.createEntity();
        } else {
            inhouseInitium = TestUtil.findAll(em, Initium.class).get(0);
        }
        em.persist(inhouseInitium);
        em.flush();
        johnLennon.setInhouseInitium(inhouseInitium);
        johnLennonRepository.saveAndFlush(johnLennon);
        Long inhouseInitiumId = inhouseInitium.getId();
        // Get all the johnLennonList where inhouseInitium equals to inhouseInitiumId
        defaultJohnLennonShouldBeFound("inhouseInitiumId.equals=" + inhouseInitiumId);

        // Get all the johnLennonList where inhouseInitium equals to (inhouseInitiumId + 1)
        defaultJohnLennonShouldNotBeFound("inhouseInitiumId.equals=" + (inhouseInitiumId + 1));
    }

    private void defaultJohnLennonFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultJohnLennonShouldBeFound(shouldBeFound);
        defaultJohnLennonShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultJohnLennonShouldBeFound(String filter) throws Exception {
        restJohnLennonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(johnLennon.getId().toString())))
            .andExpect(jsonPath("$.[*].provider").value(hasItem(DEFAULT_PROVIDER.toString())))
            .andExpect(jsonPath("$.[*].providerAppId").value(hasItem(DEFAULT_PROVIDER_APP_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].isEnabled").value(hasItem(DEFAULT_IS_ENABLED.booleanValue())));

        // Check, that the count call also returns 1
        restJohnLennonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultJohnLennonShouldNotBeFound(String filter) throws Exception {
        restJohnLennonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restJohnLennonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingJohnLennon() throws Exception {
        // Get the johnLennon
        restJohnLennonMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingJohnLennon() throws Exception {
        // Initialize the database
        insertedJohnLennon = johnLennonRepository.saveAndFlush(johnLennon);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the johnLennon
        JohnLennon updatedJohnLennon = johnLennonRepository.findById(johnLennon.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedJohnLennon are not directly saved in db
        em.detach(updatedJohnLennon);
        updatedJohnLennon
            .provider(UPDATED_PROVIDER)
            .providerAppId(UPDATED_PROVIDER_APP_ID)
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .isEnabled(UPDATED_IS_ENABLED);
        JohnLennonDTO johnLennonDTO = johnLennonMapper.toDto(updatedJohnLennon);

        restJohnLennonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, johnLennonDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(johnLennonDTO))
            )
            .andExpect(status().isOk());

        // Validate the JohnLennon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedJohnLennonToMatchAllProperties(updatedJohnLennon);
    }

    @Test
    @Transactional
    void putNonExistingJohnLennon() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        johnLennon.setId(UUID.randomUUID());

        // Create the JohnLennon
        JohnLennonDTO johnLennonDTO = johnLennonMapper.toDto(johnLennon);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJohnLennonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, johnLennonDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(johnLennonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JohnLennon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchJohnLennon() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        johnLennon.setId(UUID.randomUUID());

        // Create the JohnLennon
        JohnLennonDTO johnLennonDTO = johnLennonMapper.toDto(johnLennon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJohnLennonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(johnLennonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JohnLennon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamJohnLennon() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        johnLennon.setId(UUID.randomUUID());

        // Create the JohnLennon
        JohnLennonDTO johnLennonDTO = johnLennonMapper.toDto(johnLennon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJohnLennonMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(johnLennonDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the JohnLennon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateJohnLennonWithPatch() throws Exception {
        // Initialize the database
        insertedJohnLennon = johnLennonRepository.saveAndFlush(johnLennon);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the johnLennon using partial update
        JohnLennon partialUpdatedJohnLennon = new JohnLennon();
        partialUpdatedJohnLennon.setId(johnLennon.getId());

        partialUpdatedJohnLennon.slug(UPDATED_SLUG);

        restJohnLennonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJohnLennon.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedJohnLennon))
            )
            .andExpect(status().isOk());

        // Validate the JohnLennon in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertJohnLennonUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedJohnLennon, johnLennon),
            getPersistedJohnLennon(johnLennon)
        );
    }

    @Test
    @Transactional
    void fullUpdateJohnLennonWithPatch() throws Exception {
        // Initialize the database
        insertedJohnLennon = johnLennonRepository.saveAndFlush(johnLennon);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the johnLennon using partial update
        JohnLennon partialUpdatedJohnLennon = new JohnLennon();
        partialUpdatedJohnLennon.setId(johnLennon.getId());

        partialUpdatedJohnLennon
            .provider(UPDATED_PROVIDER)
            .providerAppId(UPDATED_PROVIDER_APP_ID)
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .isEnabled(UPDATED_IS_ENABLED);

        restJohnLennonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJohnLennon.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedJohnLennon))
            )
            .andExpect(status().isOk());

        // Validate the JohnLennon in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertJohnLennonUpdatableFieldsEquals(partialUpdatedJohnLennon, getPersistedJohnLennon(partialUpdatedJohnLennon));
    }

    @Test
    @Transactional
    void patchNonExistingJohnLennon() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        johnLennon.setId(UUID.randomUUID());

        // Create the JohnLennon
        JohnLennonDTO johnLennonDTO = johnLennonMapper.toDto(johnLennon);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJohnLennonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, johnLennonDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(johnLennonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JohnLennon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchJohnLennon() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        johnLennon.setId(UUID.randomUUID());

        // Create the JohnLennon
        JohnLennonDTO johnLennonDTO = johnLennonMapper.toDto(johnLennon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJohnLennonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(johnLennonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JohnLennon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamJohnLennon() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        johnLennon.setId(UUID.randomUUID());

        // Create the JohnLennon
        JohnLennonDTO johnLennonDTO = johnLennonMapper.toDto(johnLennon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJohnLennonMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(johnLennonDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the JohnLennon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteJohnLennon() throws Exception {
        // Initialize the database
        insertedJohnLennon = johnLennonRepository.saveAndFlush(johnLennon);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the johnLennon
        restJohnLennonMockMvc
            .perform(delete(ENTITY_API_URL_ID, johnLennon.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return johnLennonRepository.count();
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

    protected JohnLennon getPersistedJohnLennon(JohnLennon johnLennon) {
        return johnLennonRepository.findById(johnLennon.getId()).orElseThrow();
    }

    protected void assertPersistedJohnLennonToMatchAllProperties(JohnLennon expectedJohnLennon) {
        assertJohnLennonAllPropertiesEquals(expectedJohnLennon, getPersistedJohnLennon(expectedJohnLennon));
    }

    protected void assertPersistedJohnLennonToMatchUpdatableProperties(JohnLennon expectedJohnLennon) {
        assertJohnLennonAllUpdatablePropertiesEquals(expectedJohnLennon, getPersistedJohnLennon(expectedJohnLennon));
    }
}
