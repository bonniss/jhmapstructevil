package ai.realworld.web.rest;

import static ai.realworld.domain.OlAlmantinoMiloAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.OlAlmantinoMilo;
import ai.realworld.domain.OlMaster;
import ai.realworld.domain.enumeration.EcmaScript;
import ai.realworld.repository.OlAlmantinoMiloRepository;
import ai.realworld.service.dto.OlAlmantinoMiloDTO;
import ai.realworld.service.mapper.OlAlmantinoMiloMapper;
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
 * Integration tests for the {@link OlAlmantinoMiloResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OlAlmantinoMiloResourceIT {

    private static final EcmaScript DEFAULT_PROVIDER = EcmaScript.JELLO;
    private static final EcmaScript UPDATED_PROVIDER = EcmaScript.OTHER;

    private static final String DEFAULT_PROVIDER_APP_MANAGER_ID = "AAAAAAAAAA";
    private static final String UPDATED_PROVIDER_APP_MANAGER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PROVIDER_SECRET_KEY = "AAAAAAAAAA";
    private static final String UPDATED_PROVIDER_SECRET_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_PROVIDER_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_PROVIDER_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_PROVIDER_REFRESH_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_PROVIDER_REFRESH_TOKEN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ol-almantino-milos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OlAlmantinoMiloRepository olAlmantinoMiloRepository;

    @Autowired
    private OlAlmantinoMiloMapper olAlmantinoMiloMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOlAlmantinoMiloMockMvc;

    private OlAlmantinoMilo olAlmantinoMilo;

    private OlAlmantinoMilo insertedOlAlmantinoMilo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OlAlmantinoMilo createEntity() {
        return new OlAlmantinoMilo()
            .provider(DEFAULT_PROVIDER)
            .providerAppManagerId(DEFAULT_PROVIDER_APP_MANAGER_ID)
            .name(DEFAULT_NAME)
            .providerSecretKey(DEFAULT_PROVIDER_SECRET_KEY)
            .providerToken(DEFAULT_PROVIDER_TOKEN)
            .providerRefreshToken(DEFAULT_PROVIDER_REFRESH_TOKEN);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OlAlmantinoMilo createUpdatedEntity() {
        return new OlAlmantinoMilo()
            .provider(UPDATED_PROVIDER)
            .providerAppManagerId(UPDATED_PROVIDER_APP_MANAGER_ID)
            .name(UPDATED_NAME)
            .providerSecretKey(UPDATED_PROVIDER_SECRET_KEY)
            .providerToken(UPDATED_PROVIDER_TOKEN)
            .providerRefreshToken(UPDATED_PROVIDER_REFRESH_TOKEN);
    }

    @BeforeEach
    public void initTest() {
        olAlmantinoMilo = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedOlAlmantinoMilo != null) {
            olAlmantinoMiloRepository.delete(insertedOlAlmantinoMilo);
            insertedOlAlmantinoMilo = null;
        }
    }

    @Test
    @Transactional
    void createOlAlmantinoMilo() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OlAlmantinoMilo
        OlAlmantinoMiloDTO olAlmantinoMiloDTO = olAlmantinoMiloMapper.toDto(olAlmantinoMilo);
        var returnedOlAlmantinoMiloDTO = om.readValue(
            restOlAlmantinoMiloMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(olAlmantinoMiloDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OlAlmantinoMiloDTO.class
        );

        // Validate the OlAlmantinoMilo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOlAlmantinoMilo = olAlmantinoMiloMapper.toEntity(returnedOlAlmantinoMiloDTO);
        assertOlAlmantinoMiloUpdatableFieldsEquals(returnedOlAlmantinoMilo, getPersistedOlAlmantinoMilo(returnedOlAlmantinoMilo));

        insertedOlAlmantinoMilo = returnedOlAlmantinoMilo;
    }

    @Test
    @Transactional
    void createOlAlmantinoMiloWithExistingId() throws Exception {
        // Create the OlAlmantinoMilo with an existing ID
        insertedOlAlmantinoMilo = olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);
        OlAlmantinoMiloDTO olAlmantinoMiloDTO = olAlmantinoMiloMapper.toDto(olAlmantinoMilo);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOlAlmantinoMiloMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(olAlmantinoMiloDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OlAlmantinoMilo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProviderIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        olAlmantinoMilo.setProvider(null);

        // Create the OlAlmantinoMilo, which fails.
        OlAlmantinoMiloDTO olAlmantinoMiloDTO = olAlmantinoMiloMapper.toDto(olAlmantinoMilo);

        restOlAlmantinoMiloMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(olAlmantinoMiloDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProviderAppManagerIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        olAlmantinoMilo.setProviderAppManagerId(null);

        // Create the OlAlmantinoMilo, which fails.
        OlAlmantinoMiloDTO olAlmantinoMiloDTO = olAlmantinoMiloMapper.toDto(olAlmantinoMilo);

        restOlAlmantinoMiloMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(olAlmantinoMiloDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        olAlmantinoMilo.setName(null);

        // Create the OlAlmantinoMilo, which fails.
        OlAlmantinoMiloDTO olAlmantinoMiloDTO = olAlmantinoMiloMapper.toDto(olAlmantinoMilo);

        restOlAlmantinoMiloMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(olAlmantinoMiloDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOlAlmantinoMilos() throws Exception {
        // Initialize the database
        insertedOlAlmantinoMilo = olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);

        // Get all the olAlmantinoMiloList
        restOlAlmantinoMiloMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(olAlmantinoMilo.getId().toString())))
            .andExpect(jsonPath("$.[*].provider").value(hasItem(DEFAULT_PROVIDER.toString())))
            .andExpect(jsonPath("$.[*].providerAppManagerId").value(hasItem(DEFAULT_PROVIDER_APP_MANAGER_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].providerSecretKey").value(hasItem(DEFAULT_PROVIDER_SECRET_KEY)))
            .andExpect(jsonPath("$.[*].providerToken").value(hasItem(DEFAULT_PROVIDER_TOKEN)))
            .andExpect(jsonPath("$.[*].providerRefreshToken").value(hasItem(DEFAULT_PROVIDER_REFRESH_TOKEN)));
    }

    @Test
    @Transactional
    void getOlAlmantinoMilo() throws Exception {
        // Initialize the database
        insertedOlAlmantinoMilo = olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);

        // Get the olAlmantinoMilo
        restOlAlmantinoMiloMockMvc
            .perform(get(ENTITY_API_URL_ID, olAlmantinoMilo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(olAlmantinoMilo.getId().toString()))
            .andExpect(jsonPath("$.provider").value(DEFAULT_PROVIDER.toString()))
            .andExpect(jsonPath("$.providerAppManagerId").value(DEFAULT_PROVIDER_APP_MANAGER_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.providerSecretKey").value(DEFAULT_PROVIDER_SECRET_KEY))
            .andExpect(jsonPath("$.providerToken").value(DEFAULT_PROVIDER_TOKEN))
            .andExpect(jsonPath("$.providerRefreshToken").value(DEFAULT_PROVIDER_REFRESH_TOKEN));
    }

    @Test
    @Transactional
    void getOlAlmantinoMilosByIdFiltering() throws Exception {
        // Initialize the database
        insertedOlAlmantinoMilo = olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);

        UUID id = olAlmantinoMilo.getId();

        defaultOlAlmantinoMiloFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllOlAlmantinoMilosByProviderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOlAlmantinoMilo = olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);

        // Get all the olAlmantinoMiloList where provider equals to
        defaultOlAlmantinoMiloFiltering("provider.equals=" + DEFAULT_PROVIDER, "provider.equals=" + UPDATED_PROVIDER);
    }

    @Test
    @Transactional
    void getAllOlAlmantinoMilosByProviderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOlAlmantinoMilo = olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);

        // Get all the olAlmantinoMiloList where provider in
        defaultOlAlmantinoMiloFiltering("provider.in=" + DEFAULT_PROVIDER + "," + UPDATED_PROVIDER, "provider.in=" + UPDATED_PROVIDER);
    }

    @Test
    @Transactional
    void getAllOlAlmantinoMilosByProviderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOlAlmantinoMilo = olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);

        // Get all the olAlmantinoMiloList where provider is not null
        defaultOlAlmantinoMiloFiltering("provider.specified=true", "provider.specified=false");
    }

    @Test
    @Transactional
    void getAllOlAlmantinoMilosByProviderAppManagerIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOlAlmantinoMilo = olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);

        // Get all the olAlmantinoMiloList where providerAppManagerId equals to
        defaultOlAlmantinoMiloFiltering(
            "providerAppManagerId.equals=" + DEFAULT_PROVIDER_APP_MANAGER_ID,
            "providerAppManagerId.equals=" + UPDATED_PROVIDER_APP_MANAGER_ID
        );
    }

    @Test
    @Transactional
    void getAllOlAlmantinoMilosByProviderAppManagerIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOlAlmantinoMilo = olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);

        // Get all the olAlmantinoMiloList where providerAppManagerId in
        defaultOlAlmantinoMiloFiltering(
            "providerAppManagerId.in=" + DEFAULT_PROVIDER_APP_MANAGER_ID + "," + UPDATED_PROVIDER_APP_MANAGER_ID,
            "providerAppManagerId.in=" + UPDATED_PROVIDER_APP_MANAGER_ID
        );
    }

    @Test
    @Transactional
    void getAllOlAlmantinoMilosByProviderAppManagerIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOlAlmantinoMilo = olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);

        // Get all the olAlmantinoMiloList where providerAppManagerId is not null
        defaultOlAlmantinoMiloFiltering("providerAppManagerId.specified=true", "providerAppManagerId.specified=false");
    }

    @Test
    @Transactional
    void getAllOlAlmantinoMilosByProviderAppManagerIdContainsSomething() throws Exception {
        // Initialize the database
        insertedOlAlmantinoMilo = olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);

        // Get all the olAlmantinoMiloList where providerAppManagerId contains
        defaultOlAlmantinoMiloFiltering(
            "providerAppManagerId.contains=" + DEFAULT_PROVIDER_APP_MANAGER_ID,
            "providerAppManagerId.contains=" + UPDATED_PROVIDER_APP_MANAGER_ID
        );
    }

    @Test
    @Transactional
    void getAllOlAlmantinoMilosByProviderAppManagerIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOlAlmantinoMilo = olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);

        // Get all the olAlmantinoMiloList where providerAppManagerId does not contain
        defaultOlAlmantinoMiloFiltering(
            "providerAppManagerId.doesNotContain=" + UPDATED_PROVIDER_APP_MANAGER_ID,
            "providerAppManagerId.doesNotContain=" + DEFAULT_PROVIDER_APP_MANAGER_ID
        );
    }

    @Test
    @Transactional
    void getAllOlAlmantinoMilosByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOlAlmantinoMilo = olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);

        // Get all the olAlmantinoMiloList where name equals to
        defaultOlAlmantinoMiloFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOlAlmantinoMilosByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOlAlmantinoMilo = olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);

        // Get all the olAlmantinoMiloList where name in
        defaultOlAlmantinoMiloFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOlAlmantinoMilosByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOlAlmantinoMilo = olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);

        // Get all the olAlmantinoMiloList where name is not null
        defaultOlAlmantinoMiloFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllOlAlmantinoMilosByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedOlAlmantinoMilo = olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);

        // Get all the olAlmantinoMiloList where name contains
        defaultOlAlmantinoMiloFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOlAlmantinoMilosByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOlAlmantinoMilo = olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);

        // Get all the olAlmantinoMiloList where name does not contain
        defaultOlAlmantinoMiloFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllOlAlmantinoMilosByProviderSecretKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOlAlmantinoMilo = olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);

        // Get all the olAlmantinoMiloList where providerSecretKey equals to
        defaultOlAlmantinoMiloFiltering(
            "providerSecretKey.equals=" + DEFAULT_PROVIDER_SECRET_KEY,
            "providerSecretKey.equals=" + UPDATED_PROVIDER_SECRET_KEY
        );
    }

    @Test
    @Transactional
    void getAllOlAlmantinoMilosByProviderSecretKeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOlAlmantinoMilo = olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);

        // Get all the olAlmantinoMiloList where providerSecretKey in
        defaultOlAlmantinoMiloFiltering(
            "providerSecretKey.in=" + DEFAULT_PROVIDER_SECRET_KEY + "," + UPDATED_PROVIDER_SECRET_KEY,
            "providerSecretKey.in=" + UPDATED_PROVIDER_SECRET_KEY
        );
    }

    @Test
    @Transactional
    void getAllOlAlmantinoMilosByProviderSecretKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOlAlmantinoMilo = olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);

        // Get all the olAlmantinoMiloList where providerSecretKey is not null
        defaultOlAlmantinoMiloFiltering("providerSecretKey.specified=true", "providerSecretKey.specified=false");
    }

    @Test
    @Transactional
    void getAllOlAlmantinoMilosByProviderSecretKeyContainsSomething() throws Exception {
        // Initialize the database
        insertedOlAlmantinoMilo = olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);

        // Get all the olAlmantinoMiloList where providerSecretKey contains
        defaultOlAlmantinoMiloFiltering(
            "providerSecretKey.contains=" + DEFAULT_PROVIDER_SECRET_KEY,
            "providerSecretKey.contains=" + UPDATED_PROVIDER_SECRET_KEY
        );
    }

    @Test
    @Transactional
    void getAllOlAlmantinoMilosByProviderSecretKeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOlAlmantinoMilo = olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);

        // Get all the olAlmantinoMiloList where providerSecretKey does not contain
        defaultOlAlmantinoMiloFiltering(
            "providerSecretKey.doesNotContain=" + UPDATED_PROVIDER_SECRET_KEY,
            "providerSecretKey.doesNotContain=" + DEFAULT_PROVIDER_SECRET_KEY
        );
    }

    @Test
    @Transactional
    void getAllOlAlmantinoMilosByProviderTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOlAlmantinoMilo = olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);

        // Get all the olAlmantinoMiloList where providerToken equals to
        defaultOlAlmantinoMiloFiltering("providerToken.equals=" + DEFAULT_PROVIDER_TOKEN, "providerToken.equals=" + UPDATED_PROVIDER_TOKEN);
    }

    @Test
    @Transactional
    void getAllOlAlmantinoMilosByProviderTokenIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOlAlmantinoMilo = olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);

        // Get all the olAlmantinoMiloList where providerToken in
        defaultOlAlmantinoMiloFiltering(
            "providerToken.in=" + DEFAULT_PROVIDER_TOKEN + "," + UPDATED_PROVIDER_TOKEN,
            "providerToken.in=" + UPDATED_PROVIDER_TOKEN
        );
    }

    @Test
    @Transactional
    void getAllOlAlmantinoMilosByProviderTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOlAlmantinoMilo = olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);

        // Get all the olAlmantinoMiloList where providerToken is not null
        defaultOlAlmantinoMiloFiltering("providerToken.specified=true", "providerToken.specified=false");
    }

    @Test
    @Transactional
    void getAllOlAlmantinoMilosByProviderTokenContainsSomething() throws Exception {
        // Initialize the database
        insertedOlAlmantinoMilo = olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);

        // Get all the olAlmantinoMiloList where providerToken contains
        defaultOlAlmantinoMiloFiltering(
            "providerToken.contains=" + DEFAULT_PROVIDER_TOKEN,
            "providerToken.contains=" + UPDATED_PROVIDER_TOKEN
        );
    }

    @Test
    @Transactional
    void getAllOlAlmantinoMilosByProviderTokenNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOlAlmantinoMilo = olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);

        // Get all the olAlmantinoMiloList where providerToken does not contain
        defaultOlAlmantinoMiloFiltering(
            "providerToken.doesNotContain=" + UPDATED_PROVIDER_TOKEN,
            "providerToken.doesNotContain=" + DEFAULT_PROVIDER_TOKEN
        );
    }

    @Test
    @Transactional
    void getAllOlAlmantinoMilosByProviderRefreshTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOlAlmantinoMilo = olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);

        // Get all the olAlmantinoMiloList where providerRefreshToken equals to
        defaultOlAlmantinoMiloFiltering(
            "providerRefreshToken.equals=" + DEFAULT_PROVIDER_REFRESH_TOKEN,
            "providerRefreshToken.equals=" + UPDATED_PROVIDER_REFRESH_TOKEN
        );
    }

    @Test
    @Transactional
    void getAllOlAlmantinoMilosByProviderRefreshTokenIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOlAlmantinoMilo = olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);

        // Get all the olAlmantinoMiloList where providerRefreshToken in
        defaultOlAlmantinoMiloFiltering(
            "providerRefreshToken.in=" + DEFAULT_PROVIDER_REFRESH_TOKEN + "," + UPDATED_PROVIDER_REFRESH_TOKEN,
            "providerRefreshToken.in=" + UPDATED_PROVIDER_REFRESH_TOKEN
        );
    }

    @Test
    @Transactional
    void getAllOlAlmantinoMilosByProviderRefreshTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOlAlmantinoMilo = olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);

        // Get all the olAlmantinoMiloList where providerRefreshToken is not null
        defaultOlAlmantinoMiloFiltering("providerRefreshToken.specified=true", "providerRefreshToken.specified=false");
    }

    @Test
    @Transactional
    void getAllOlAlmantinoMilosByProviderRefreshTokenContainsSomething() throws Exception {
        // Initialize the database
        insertedOlAlmantinoMilo = olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);

        // Get all the olAlmantinoMiloList where providerRefreshToken contains
        defaultOlAlmantinoMiloFiltering(
            "providerRefreshToken.contains=" + DEFAULT_PROVIDER_REFRESH_TOKEN,
            "providerRefreshToken.contains=" + UPDATED_PROVIDER_REFRESH_TOKEN
        );
    }

    @Test
    @Transactional
    void getAllOlAlmantinoMilosByProviderRefreshTokenNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOlAlmantinoMilo = olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);

        // Get all the olAlmantinoMiloList where providerRefreshToken does not contain
        defaultOlAlmantinoMiloFiltering(
            "providerRefreshToken.doesNotContain=" + UPDATED_PROVIDER_REFRESH_TOKEN,
            "providerRefreshToken.doesNotContain=" + DEFAULT_PROVIDER_REFRESH_TOKEN
        );
    }

    @Test
    @Transactional
    void getAllOlAlmantinoMilosByOrganizationIsEqualToSomething() throws Exception {
        OlMaster organization;
        if (TestUtil.findAll(em, OlMaster.class).isEmpty()) {
            olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);
            organization = OlMasterResourceIT.createEntity();
        } else {
            organization = TestUtil.findAll(em, OlMaster.class).get(0);
        }
        em.persist(organization);
        em.flush();
        olAlmantinoMilo.setOrganization(organization);
        olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);
        UUID organizationId = organization.getId();
        // Get all the olAlmantinoMiloList where organization equals to organizationId
        defaultOlAlmantinoMiloShouldBeFound("organizationId.equals=" + organizationId);

        // Get all the olAlmantinoMiloList where organization equals to UUID.randomUUID()
        defaultOlAlmantinoMiloShouldNotBeFound("organizationId.equals=" + UUID.randomUUID());
    }

    private void defaultOlAlmantinoMiloFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOlAlmantinoMiloShouldBeFound(shouldBeFound);
        defaultOlAlmantinoMiloShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOlAlmantinoMiloShouldBeFound(String filter) throws Exception {
        restOlAlmantinoMiloMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(olAlmantinoMilo.getId().toString())))
            .andExpect(jsonPath("$.[*].provider").value(hasItem(DEFAULT_PROVIDER.toString())))
            .andExpect(jsonPath("$.[*].providerAppManagerId").value(hasItem(DEFAULT_PROVIDER_APP_MANAGER_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].providerSecretKey").value(hasItem(DEFAULT_PROVIDER_SECRET_KEY)))
            .andExpect(jsonPath("$.[*].providerToken").value(hasItem(DEFAULT_PROVIDER_TOKEN)))
            .andExpect(jsonPath("$.[*].providerRefreshToken").value(hasItem(DEFAULT_PROVIDER_REFRESH_TOKEN)));

        // Check, that the count call also returns 1
        restOlAlmantinoMiloMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOlAlmantinoMiloShouldNotBeFound(String filter) throws Exception {
        restOlAlmantinoMiloMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOlAlmantinoMiloMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOlAlmantinoMilo() throws Exception {
        // Get the olAlmantinoMilo
        restOlAlmantinoMiloMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOlAlmantinoMilo() throws Exception {
        // Initialize the database
        insertedOlAlmantinoMilo = olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the olAlmantinoMilo
        OlAlmantinoMilo updatedOlAlmantinoMilo = olAlmantinoMiloRepository.findById(olAlmantinoMilo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOlAlmantinoMilo are not directly saved in db
        em.detach(updatedOlAlmantinoMilo);
        updatedOlAlmantinoMilo
            .provider(UPDATED_PROVIDER)
            .providerAppManagerId(UPDATED_PROVIDER_APP_MANAGER_ID)
            .name(UPDATED_NAME)
            .providerSecretKey(UPDATED_PROVIDER_SECRET_KEY)
            .providerToken(UPDATED_PROVIDER_TOKEN)
            .providerRefreshToken(UPDATED_PROVIDER_REFRESH_TOKEN);
        OlAlmantinoMiloDTO olAlmantinoMiloDTO = olAlmantinoMiloMapper.toDto(updatedOlAlmantinoMilo);

        restOlAlmantinoMiloMockMvc
            .perform(
                put(ENTITY_API_URL_ID, olAlmantinoMiloDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(olAlmantinoMiloDTO))
            )
            .andExpect(status().isOk());

        // Validate the OlAlmantinoMilo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOlAlmantinoMiloToMatchAllProperties(updatedOlAlmantinoMilo);
    }

    @Test
    @Transactional
    void putNonExistingOlAlmantinoMilo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        olAlmantinoMilo.setId(UUID.randomUUID());

        // Create the OlAlmantinoMilo
        OlAlmantinoMiloDTO olAlmantinoMiloDTO = olAlmantinoMiloMapper.toDto(olAlmantinoMilo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOlAlmantinoMiloMockMvc
            .perform(
                put(ENTITY_API_URL_ID, olAlmantinoMiloDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(olAlmantinoMiloDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OlAlmantinoMilo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOlAlmantinoMilo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        olAlmantinoMilo.setId(UUID.randomUUID());

        // Create the OlAlmantinoMilo
        OlAlmantinoMiloDTO olAlmantinoMiloDTO = olAlmantinoMiloMapper.toDto(olAlmantinoMilo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOlAlmantinoMiloMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(olAlmantinoMiloDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OlAlmantinoMilo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOlAlmantinoMilo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        olAlmantinoMilo.setId(UUID.randomUUID());

        // Create the OlAlmantinoMilo
        OlAlmantinoMiloDTO olAlmantinoMiloDTO = olAlmantinoMiloMapper.toDto(olAlmantinoMilo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOlAlmantinoMiloMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(olAlmantinoMiloDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OlAlmantinoMilo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOlAlmantinoMiloWithPatch() throws Exception {
        // Initialize the database
        insertedOlAlmantinoMilo = olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the olAlmantinoMilo using partial update
        OlAlmantinoMilo partialUpdatedOlAlmantinoMilo = new OlAlmantinoMilo();
        partialUpdatedOlAlmantinoMilo.setId(olAlmantinoMilo.getId());

        partialUpdatedOlAlmantinoMilo
            .providerAppManagerId(UPDATED_PROVIDER_APP_MANAGER_ID)
            .providerSecretKey(UPDATED_PROVIDER_SECRET_KEY)
            .providerRefreshToken(UPDATED_PROVIDER_REFRESH_TOKEN);

        restOlAlmantinoMiloMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOlAlmantinoMilo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOlAlmantinoMilo))
            )
            .andExpect(status().isOk());

        // Validate the OlAlmantinoMilo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOlAlmantinoMiloUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOlAlmantinoMilo, olAlmantinoMilo),
            getPersistedOlAlmantinoMilo(olAlmantinoMilo)
        );
    }

    @Test
    @Transactional
    void fullUpdateOlAlmantinoMiloWithPatch() throws Exception {
        // Initialize the database
        insertedOlAlmantinoMilo = olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the olAlmantinoMilo using partial update
        OlAlmantinoMilo partialUpdatedOlAlmantinoMilo = new OlAlmantinoMilo();
        partialUpdatedOlAlmantinoMilo.setId(olAlmantinoMilo.getId());

        partialUpdatedOlAlmantinoMilo
            .provider(UPDATED_PROVIDER)
            .providerAppManagerId(UPDATED_PROVIDER_APP_MANAGER_ID)
            .name(UPDATED_NAME)
            .providerSecretKey(UPDATED_PROVIDER_SECRET_KEY)
            .providerToken(UPDATED_PROVIDER_TOKEN)
            .providerRefreshToken(UPDATED_PROVIDER_REFRESH_TOKEN);

        restOlAlmantinoMiloMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOlAlmantinoMilo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOlAlmantinoMilo))
            )
            .andExpect(status().isOk());

        // Validate the OlAlmantinoMilo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOlAlmantinoMiloUpdatableFieldsEquals(
            partialUpdatedOlAlmantinoMilo,
            getPersistedOlAlmantinoMilo(partialUpdatedOlAlmantinoMilo)
        );
    }

    @Test
    @Transactional
    void patchNonExistingOlAlmantinoMilo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        olAlmantinoMilo.setId(UUID.randomUUID());

        // Create the OlAlmantinoMilo
        OlAlmantinoMiloDTO olAlmantinoMiloDTO = olAlmantinoMiloMapper.toDto(olAlmantinoMilo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOlAlmantinoMiloMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, olAlmantinoMiloDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(olAlmantinoMiloDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OlAlmantinoMilo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOlAlmantinoMilo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        olAlmantinoMilo.setId(UUID.randomUUID());

        // Create the OlAlmantinoMilo
        OlAlmantinoMiloDTO olAlmantinoMiloDTO = olAlmantinoMiloMapper.toDto(olAlmantinoMilo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOlAlmantinoMiloMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(olAlmantinoMiloDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OlAlmantinoMilo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOlAlmantinoMilo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        olAlmantinoMilo.setId(UUID.randomUUID());

        // Create the OlAlmantinoMilo
        OlAlmantinoMiloDTO olAlmantinoMiloDTO = olAlmantinoMiloMapper.toDto(olAlmantinoMilo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOlAlmantinoMiloMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(olAlmantinoMiloDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OlAlmantinoMilo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOlAlmantinoMilo() throws Exception {
        // Initialize the database
        insertedOlAlmantinoMilo = olAlmantinoMiloRepository.saveAndFlush(olAlmantinoMilo);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the olAlmantinoMilo
        restOlAlmantinoMiloMockMvc
            .perform(delete(ENTITY_API_URL_ID, olAlmantinoMilo.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return olAlmantinoMiloRepository.count();
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

    protected OlAlmantinoMilo getPersistedOlAlmantinoMilo(OlAlmantinoMilo olAlmantinoMilo) {
        return olAlmantinoMiloRepository.findById(olAlmantinoMilo.getId()).orElseThrow();
    }

    protected void assertPersistedOlAlmantinoMiloToMatchAllProperties(OlAlmantinoMilo expectedOlAlmantinoMilo) {
        assertOlAlmantinoMiloAllPropertiesEquals(expectedOlAlmantinoMilo, getPersistedOlAlmantinoMilo(expectedOlAlmantinoMilo));
    }

    protected void assertPersistedOlAlmantinoMiloToMatchUpdatableProperties(OlAlmantinoMilo expectedOlAlmantinoMilo) {
        assertOlAlmantinoMiloAllUpdatablePropertiesEquals(expectedOlAlmantinoMilo, getPersistedOlAlmantinoMilo(expectedOlAlmantinoMilo));
    }
}
