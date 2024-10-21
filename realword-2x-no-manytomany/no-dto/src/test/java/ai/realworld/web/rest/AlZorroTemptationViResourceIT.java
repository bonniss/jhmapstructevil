package ai.realworld.web.rest;

import static ai.realworld.domain.AlZorroTemptationViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlZorroTemptationVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.domain.enumeration.AlphaSourceType;
import ai.realworld.domain.enumeration.KnsIction;
import ai.realworld.repository.AlZorroTemptationViRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.UUID;
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

/**
 * Integration tests for the {@link AlZorroTemptationViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlZorroTemptationViResourceIT {

    private static final KnsIction DEFAULT_ZIP_ACTION = KnsIction.SEND_ZIP_TO_CUSTOMER_AFTER_BOOKING;
    private static final KnsIction UPDATED_ZIP_ACTION = KnsIction.SEND_ZIP_TO_CUSTOMER_AFTER_BOOKING;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TEMPLATE_ID = "AAAAAAAAAA";
    private static final String UPDATED_TEMPLATE_ID = "BBBBBBBBBB";

    private static final AlphaSourceType DEFAULT_DATA_SOURCE_MAPPING_TYPE = AlphaSourceType.COMPLETED_ORDER;
    private static final AlphaSourceType UPDATED_DATA_SOURCE_MAPPING_TYPE = AlphaSourceType.BOOKING_INFORMATION;

    private static final String DEFAULT_TEMPLATE_DATA_MAPPING = "AAAAAAAAAA";
    private static final String UPDATED_TEMPLATE_DATA_MAPPING = "BBBBBBBBBB";

    private static final String DEFAULT_TARGET_URLS = "AAAAAAAAAA";
    private static final String UPDATED_TARGET_URLS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/al-zorro-temptation-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlZorroTemptationViRepository alZorroTemptationViRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlZorroTemptationViMockMvc;

    private AlZorroTemptationVi alZorroTemptationVi;

    private AlZorroTemptationVi insertedAlZorroTemptationVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlZorroTemptationVi createEntity() {
        return new AlZorroTemptationVi()
            .zipAction(DEFAULT_ZIP_ACTION)
            .name(DEFAULT_NAME)
            .templateId(DEFAULT_TEMPLATE_ID)
            .dataSourceMappingType(DEFAULT_DATA_SOURCE_MAPPING_TYPE)
            .templateDataMapping(DEFAULT_TEMPLATE_DATA_MAPPING)
            .targetUrls(DEFAULT_TARGET_URLS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlZorroTemptationVi createUpdatedEntity() {
        return new AlZorroTemptationVi()
            .zipAction(UPDATED_ZIP_ACTION)
            .name(UPDATED_NAME)
            .templateId(UPDATED_TEMPLATE_ID)
            .dataSourceMappingType(UPDATED_DATA_SOURCE_MAPPING_TYPE)
            .templateDataMapping(UPDATED_TEMPLATE_DATA_MAPPING)
            .targetUrls(UPDATED_TARGET_URLS);
    }

    @BeforeEach
    public void initTest() {
        alZorroTemptationVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlZorroTemptationVi != null) {
            alZorroTemptationViRepository.delete(insertedAlZorroTemptationVi);
            insertedAlZorroTemptationVi = null;
        }
    }

    @Test
    @Transactional
    void createAlZorroTemptationVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlZorroTemptationVi
        var returnedAlZorroTemptationVi = om.readValue(
            restAlZorroTemptationViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alZorroTemptationVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlZorroTemptationVi.class
        );

        // Validate the AlZorroTemptationVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlZorroTemptationViUpdatableFieldsEquals(
            returnedAlZorroTemptationVi,
            getPersistedAlZorroTemptationVi(returnedAlZorroTemptationVi)
        );

        insertedAlZorroTemptationVi = returnedAlZorroTemptationVi;
    }

    @Test
    @Transactional
    void createAlZorroTemptationViWithExistingId() throws Exception {
        // Create the AlZorroTemptationVi with an existing ID
        alZorroTemptationVi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlZorroTemptationViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alZorroTemptationVi)))
            .andExpect(status().isBadRequest());

        // Validate the AlZorroTemptationVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alZorroTemptationVi.setName(null);

        // Create the AlZorroTemptationVi, which fails.

        restAlZorroTemptationViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alZorroTemptationVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationVis() throws Exception {
        // Initialize the database
        insertedAlZorroTemptationVi = alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);

        // Get all the alZorroTemptationViList
        restAlZorroTemptationViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alZorroTemptationVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].zipAction").value(hasItem(DEFAULT_ZIP_ACTION.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].templateId").value(hasItem(DEFAULT_TEMPLATE_ID)))
            .andExpect(jsonPath("$.[*].dataSourceMappingType").value(hasItem(DEFAULT_DATA_SOURCE_MAPPING_TYPE.toString())))
            .andExpect(jsonPath("$.[*].templateDataMapping").value(hasItem(DEFAULT_TEMPLATE_DATA_MAPPING)))
            .andExpect(jsonPath("$.[*].targetUrls").value(hasItem(DEFAULT_TARGET_URLS)));
    }

    @Test
    @Transactional
    void getAlZorroTemptationVi() throws Exception {
        // Initialize the database
        insertedAlZorroTemptationVi = alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);

        // Get the alZorroTemptationVi
        restAlZorroTemptationViMockMvc
            .perform(get(ENTITY_API_URL_ID, alZorroTemptationVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alZorroTemptationVi.getId().intValue()))
            .andExpect(jsonPath("$.zipAction").value(DEFAULT_ZIP_ACTION.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.templateId").value(DEFAULT_TEMPLATE_ID))
            .andExpect(jsonPath("$.dataSourceMappingType").value(DEFAULT_DATA_SOURCE_MAPPING_TYPE.toString()))
            .andExpect(jsonPath("$.templateDataMapping").value(DEFAULT_TEMPLATE_DATA_MAPPING))
            .andExpect(jsonPath("$.targetUrls").value(DEFAULT_TARGET_URLS));
    }

    @Test
    @Transactional
    void getAlZorroTemptationVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlZorroTemptationVi = alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);

        Long id = alZorroTemptationVi.getId();

        defaultAlZorroTemptationViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlZorroTemptationViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlZorroTemptationViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationVisByZipActionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlZorroTemptationVi = alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);

        // Get all the alZorroTemptationViList where zipAction equals to
        defaultAlZorroTemptationViFiltering("zipAction.equals=" + DEFAULT_ZIP_ACTION, "zipAction.equals=" + UPDATED_ZIP_ACTION);
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationVisByZipActionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlZorroTemptationVi = alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);

        // Get all the alZorroTemptationViList where zipAction in
        defaultAlZorroTemptationViFiltering(
            "zipAction.in=" + DEFAULT_ZIP_ACTION + "," + UPDATED_ZIP_ACTION,
            "zipAction.in=" + UPDATED_ZIP_ACTION
        );
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationVisByZipActionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlZorroTemptationVi = alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);

        // Get all the alZorroTemptationViList where zipAction is not null
        defaultAlZorroTemptationViFiltering("zipAction.specified=true", "zipAction.specified=false");
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationVisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlZorroTemptationVi = alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);

        // Get all the alZorroTemptationViList where name equals to
        defaultAlZorroTemptationViFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationVisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlZorroTemptationVi = alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);

        // Get all the alZorroTemptationViList where name in
        defaultAlZorroTemptationViFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationVisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlZorroTemptationVi = alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);

        // Get all the alZorroTemptationViList where name is not null
        defaultAlZorroTemptationViFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationVisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlZorroTemptationVi = alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);

        // Get all the alZorroTemptationViList where name contains
        defaultAlZorroTemptationViFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationVisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlZorroTemptationVi = alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);

        // Get all the alZorroTemptationViList where name does not contain
        defaultAlZorroTemptationViFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationVisByTemplateIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlZorroTemptationVi = alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);

        // Get all the alZorroTemptationViList where templateId equals to
        defaultAlZorroTemptationViFiltering("templateId.equals=" + DEFAULT_TEMPLATE_ID, "templateId.equals=" + UPDATED_TEMPLATE_ID);
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationVisByTemplateIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlZorroTemptationVi = alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);

        // Get all the alZorroTemptationViList where templateId in
        defaultAlZorroTemptationViFiltering(
            "templateId.in=" + DEFAULT_TEMPLATE_ID + "," + UPDATED_TEMPLATE_ID,
            "templateId.in=" + UPDATED_TEMPLATE_ID
        );
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationVisByTemplateIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlZorroTemptationVi = alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);

        // Get all the alZorroTemptationViList where templateId is not null
        defaultAlZorroTemptationViFiltering("templateId.specified=true", "templateId.specified=false");
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationVisByTemplateIdContainsSomething() throws Exception {
        // Initialize the database
        insertedAlZorroTemptationVi = alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);

        // Get all the alZorroTemptationViList where templateId contains
        defaultAlZorroTemptationViFiltering("templateId.contains=" + DEFAULT_TEMPLATE_ID, "templateId.contains=" + UPDATED_TEMPLATE_ID);
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationVisByTemplateIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlZorroTemptationVi = alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);

        // Get all the alZorroTemptationViList where templateId does not contain
        defaultAlZorroTemptationViFiltering(
            "templateId.doesNotContain=" + UPDATED_TEMPLATE_ID,
            "templateId.doesNotContain=" + DEFAULT_TEMPLATE_ID
        );
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationVisByDataSourceMappingTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlZorroTemptationVi = alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);

        // Get all the alZorroTemptationViList where dataSourceMappingType equals to
        defaultAlZorroTemptationViFiltering(
            "dataSourceMappingType.equals=" + DEFAULT_DATA_SOURCE_MAPPING_TYPE,
            "dataSourceMappingType.equals=" + UPDATED_DATA_SOURCE_MAPPING_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationVisByDataSourceMappingTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlZorroTemptationVi = alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);

        // Get all the alZorroTemptationViList where dataSourceMappingType in
        defaultAlZorroTemptationViFiltering(
            "dataSourceMappingType.in=" + DEFAULT_DATA_SOURCE_MAPPING_TYPE + "," + UPDATED_DATA_SOURCE_MAPPING_TYPE,
            "dataSourceMappingType.in=" + UPDATED_DATA_SOURCE_MAPPING_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationVisByDataSourceMappingTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlZorroTemptationVi = alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);

        // Get all the alZorroTemptationViList where dataSourceMappingType is not null
        defaultAlZorroTemptationViFiltering("dataSourceMappingType.specified=true", "dataSourceMappingType.specified=false");
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationVisByTemplateDataMappingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlZorroTemptationVi = alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);

        // Get all the alZorroTemptationViList where templateDataMapping equals to
        defaultAlZorroTemptationViFiltering(
            "templateDataMapping.equals=" + DEFAULT_TEMPLATE_DATA_MAPPING,
            "templateDataMapping.equals=" + UPDATED_TEMPLATE_DATA_MAPPING
        );
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationVisByTemplateDataMappingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlZorroTemptationVi = alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);

        // Get all the alZorroTemptationViList where templateDataMapping in
        defaultAlZorroTemptationViFiltering(
            "templateDataMapping.in=" + DEFAULT_TEMPLATE_DATA_MAPPING + "," + UPDATED_TEMPLATE_DATA_MAPPING,
            "templateDataMapping.in=" + UPDATED_TEMPLATE_DATA_MAPPING
        );
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationVisByTemplateDataMappingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlZorroTemptationVi = alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);

        // Get all the alZorroTemptationViList where templateDataMapping is not null
        defaultAlZorroTemptationViFiltering("templateDataMapping.specified=true", "templateDataMapping.specified=false");
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationVisByTemplateDataMappingContainsSomething() throws Exception {
        // Initialize the database
        insertedAlZorroTemptationVi = alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);

        // Get all the alZorroTemptationViList where templateDataMapping contains
        defaultAlZorroTemptationViFiltering(
            "templateDataMapping.contains=" + DEFAULT_TEMPLATE_DATA_MAPPING,
            "templateDataMapping.contains=" + UPDATED_TEMPLATE_DATA_MAPPING
        );
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationVisByTemplateDataMappingNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlZorroTemptationVi = alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);

        // Get all the alZorroTemptationViList where templateDataMapping does not contain
        defaultAlZorroTemptationViFiltering(
            "templateDataMapping.doesNotContain=" + UPDATED_TEMPLATE_DATA_MAPPING,
            "templateDataMapping.doesNotContain=" + DEFAULT_TEMPLATE_DATA_MAPPING
        );
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationVisByTargetUrlsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlZorroTemptationVi = alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);

        // Get all the alZorroTemptationViList where targetUrls equals to
        defaultAlZorroTemptationViFiltering("targetUrls.equals=" + DEFAULT_TARGET_URLS, "targetUrls.equals=" + UPDATED_TARGET_URLS);
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationVisByTargetUrlsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlZorroTemptationVi = alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);

        // Get all the alZorroTemptationViList where targetUrls in
        defaultAlZorroTemptationViFiltering(
            "targetUrls.in=" + DEFAULT_TARGET_URLS + "," + UPDATED_TARGET_URLS,
            "targetUrls.in=" + UPDATED_TARGET_URLS
        );
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationVisByTargetUrlsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlZorroTemptationVi = alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);

        // Get all the alZorroTemptationViList where targetUrls is not null
        defaultAlZorroTemptationViFiltering("targetUrls.specified=true", "targetUrls.specified=false");
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationVisByTargetUrlsContainsSomething() throws Exception {
        // Initialize the database
        insertedAlZorroTemptationVi = alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);

        // Get all the alZorroTemptationViList where targetUrls contains
        defaultAlZorroTemptationViFiltering("targetUrls.contains=" + DEFAULT_TARGET_URLS, "targetUrls.contains=" + UPDATED_TARGET_URLS);
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationVisByTargetUrlsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlZorroTemptationVi = alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);

        // Get all the alZorroTemptationViList where targetUrls does not contain
        defaultAlZorroTemptationViFiltering(
            "targetUrls.doesNotContain=" + UPDATED_TARGET_URLS,
            "targetUrls.doesNotContain=" + DEFAULT_TARGET_URLS
        );
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationVisByThumbnailIsEqualToSomething() throws Exception {
        Metaverse thumbnail;
        if (TestUtil.findAll(em, Metaverse.class).isEmpty()) {
            alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);
            thumbnail = MetaverseResourceIT.createEntity();
        } else {
            thumbnail = TestUtil.findAll(em, Metaverse.class).get(0);
        }
        em.persist(thumbnail);
        em.flush();
        alZorroTemptationVi.setThumbnail(thumbnail);
        alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);
        Long thumbnailId = thumbnail.getId();
        // Get all the alZorroTemptationViList where thumbnail equals to thumbnailId
        defaultAlZorroTemptationViShouldBeFound("thumbnailId.equals=" + thumbnailId);

        // Get all the alZorroTemptationViList where thumbnail equals to (thumbnailId + 1)
        defaultAlZorroTemptationViShouldNotBeFound("thumbnailId.equals=" + (thumbnailId + 1));
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationVisByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alZorroTemptationVi.setApplication(application);
        alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);
        UUID applicationId = application.getId();
        // Get all the alZorroTemptationViList where application equals to applicationId
        defaultAlZorroTemptationViShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alZorroTemptationViList where application equals to UUID.randomUUID()
        defaultAlZorroTemptationViShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlZorroTemptationViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlZorroTemptationViShouldBeFound(shouldBeFound);
        defaultAlZorroTemptationViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlZorroTemptationViShouldBeFound(String filter) throws Exception {
        restAlZorroTemptationViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alZorroTemptationVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].zipAction").value(hasItem(DEFAULT_ZIP_ACTION.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].templateId").value(hasItem(DEFAULT_TEMPLATE_ID)))
            .andExpect(jsonPath("$.[*].dataSourceMappingType").value(hasItem(DEFAULT_DATA_SOURCE_MAPPING_TYPE.toString())))
            .andExpect(jsonPath("$.[*].templateDataMapping").value(hasItem(DEFAULT_TEMPLATE_DATA_MAPPING)))
            .andExpect(jsonPath("$.[*].targetUrls").value(hasItem(DEFAULT_TARGET_URLS)));

        // Check, that the count call also returns 1
        restAlZorroTemptationViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlZorroTemptationViShouldNotBeFound(String filter) throws Exception {
        restAlZorroTemptationViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlZorroTemptationViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlZorroTemptationVi() throws Exception {
        // Get the alZorroTemptationVi
        restAlZorroTemptationViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlZorroTemptationVi() throws Exception {
        // Initialize the database
        insertedAlZorroTemptationVi = alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alZorroTemptationVi
        AlZorroTemptationVi updatedAlZorroTemptationVi = alZorroTemptationViRepository.findById(alZorroTemptationVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlZorroTemptationVi are not directly saved in db
        em.detach(updatedAlZorroTemptationVi);
        updatedAlZorroTemptationVi
            .zipAction(UPDATED_ZIP_ACTION)
            .name(UPDATED_NAME)
            .templateId(UPDATED_TEMPLATE_ID)
            .dataSourceMappingType(UPDATED_DATA_SOURCE_MAPPING_TYPE)
            .templateDataMapping(UPDATED_TEMPLATE_DATA_MAPPING)
            .targetUrls(UPDATED_TARGET_URLS);

        restAlZorroTemptationViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlZorroTemptationVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlZorroTemptationVi))
            )
            .andExpect(status().isOk());

        // Validate the AlZorroTemptationVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlZorroTemptationViToMatchAllProperties(updatedAlZorroTemptationVi);
    }

    @Test
    @Transactional
    void putNonExistingAlZorroTemptationVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alZorroTemptationVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlZorroTemptationViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alZorroTemptationVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alZorroTemptationVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlZorroTemptationVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlZorroTemptationVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alZorroTemptationVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlZorroTemptationViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alZorroTemptationVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlZorroTemptationVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlZorroTemptationVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alZorroTemptationVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlZorroTemptationViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alZorroTemptationVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlZorroTemptationVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlZorroTemptationViWithPatch() throws Exception {
        // Initialize the database
        insertedAlZorroTemptationVi = alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alZorroTemptationVi using partial update
        AlZorroTemptationVi partialUpdatedAlZorroTemptationVi = new AlZorroTemptationVi();
        partialUpdatedAlZorroTemptationVi.setId(alZorroTemptationVi.getId());

        partialUpdatedAlZorroTemptationVi.templateDataMapping(UPDATED_TEMPLATE_DATA_MAPPING).targetUrls(UPDATED_TARGET_URLS);

        restAlZorroTemptationViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlZorroTemptationVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlZorroTemptationVi))
            )
            .andExpect(status().isOk());

        // Validate the AlZorroTemptationVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlZorroTemptationViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlZorroTemptationVi, alZorroTemptationVi),
            getPersistedAlZorroTemptationVi(alZorroTemptationVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlZorroTemptationViWithPatch() throws Exception {
        // Initialize the database
        insertedAlZorroTemptationVi = alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alZorroTemptationVi using partial update
        AlZorroTemptationVi partialUpdatedAlZorroTemptationVi = new AlZorroTemptationVi();
        partialUpdatedAlZorroTemptationVi.setId(alZorroTemptationVi.getId());

        partialUpdatedAlZorroTemptationVi
            .zipAction(UPDATED_ZIP_ACTION)
            .name(UPDATED_NAME)
            .templateId(UPDATED_TEMPLATE_ID)
            .dataSourceMappingType(UPDATED_DATA_SOURCE_MAPPING_TYPE)
            .templateDataMapping(UPDATED_TEMPLATE_DATA_MAPPING)
            .targetUrls(UPDATED_TARGET_URLS);

        restAlZorroTemptationViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlZorroTemptationVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlZorroTemptationVi))
            )
            .andExpect(status().isOk());

        // Validate the AlZorroTemptationVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlZorroTemptationViUpdatableFieldsEquals(
            partialUpdatedAlZorroTemptationVi,
            getPersistedAlZorroTemptationVi(partialUpdatedAlZorroTemptationVi)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAlZorroTemptationVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alZorroTemptationVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlZorroTemptationViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alZorroTemptationVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alZorroTemptationVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlZorroTemptationVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlZorroTemptationVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alZorroTemptationVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlZorroTemptationViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alZorroTemptationVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlZorroTemptationVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlZorroTemptationVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alZorroTemptationVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlZorroTemptationViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alZorroTemptationVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlZorroTemptationVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlZorroTemptationVi() throws Exception {
        // Initialize the database
        insertedAlZorroTemptationVi = alZorroTemptationViRepository.saveAndFlush(alZorroTemptationVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alZorroTemptationVi
        restAlZorroTemptationViMockMvc
            .perform(delete(ENTITY_API_URL_ID, alZorroTemptationVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alZorroTemptationViRepository.count();
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

    protected AlZorroTemptationVi getPersistedAlZorroTemptationVi(AlZorroTemptationVi alZorroTemptationVi) {
        return alZorroTemptationViRepository.findById(alZorroTemptationVi.getId()).orElseThrow();
    }

    protected void assertPersistedAlZorroTemptationViToMatchAllProperties(AlZorroTemptationVi expectedAlZorroTemptationVi) {
        assertAlZorroTemptationViAllPropertiesEquals(
            expectedAlZorroTemptationVi,
            getPersistedAlZorroTemptationVi(expectedAlZorroTemptationVi)
        );
    }

    protected void assertPersistedAlZorroTemptationViToMatchUpdatableProperties(AlZorroTemptationVi expectedAlZorroTemptationVi) {
        assertAlZorroTemptationViAllUpdatablePropertiesEquals(
            expectedAlZorroTemptationVi,
            getPersistedAlZorroTemptationVi(expectedAlZorroTemptationVi)
        );
    }
}
