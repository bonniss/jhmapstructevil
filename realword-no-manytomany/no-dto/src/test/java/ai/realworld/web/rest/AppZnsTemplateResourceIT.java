package ai.realworld.web.rest;

import static ai.realworld.domain.AppZnsTemplateAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AppZnsTemplate;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.domain.enumeration.AlphaSourceType;
import ai.realworld.domain.enumeration.KnsIction;
import ai.realworld.repository.AppZnsTemplateRepository;
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
 * Integration tests for the {@link AppZnsTemplateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AppZnsTemplateResourceIT {

    private static final KnsIction DEFAULT_ZNS_ACTION = KnsIction.SEND_ZNS_TO_CUSTOMER_AFTER_BOOKING;
    private static final KnsIction UPDATED_ZNS_ACTION = KnsIction.SEND_ZNS_TO_HOUSEKEEPING_AFTER_BOOKING;

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

    private static final String ENTITY_API_URL = "/api/app-zns-templates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AppZnsTemplateRepository appZnsTemplateRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppZnsTemplateMockMvc;

    private AppZnsTemplate appZnsTemplate;

    private AppZnsTemplate insertedAppZnsTemplate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppZnsTemplate createEntity() {
        return new AppZnsTemplate()
            .znsAction(DEFAULT_ZNS_ACTION)
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
    public static AppZnsTemplate createUpdatedEntity() {
        return new AppZnsTemplate()
            .znsAction(UPDATED_ZNS_ACTION)
            .name(UPDATED_NAME)
            .templateId(UPDATED_TEMPLATE_ID)
            .dataSourceMappingType(UPDATED_DATA_SOURCE_MAPPING_TYPE)
            .templateDataMapping(UPDATED_TEMPLATE_DATA_MAPPING)
            .targetUrls(UPDATED_TARGET_URLS);
    }

    @BeforeEach
    public void initTest() {
        appZnsTemplate = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAppZnsTemplate != null) {
            appZnsTemplateRepository.delete(insertedAppZnsTemplate);
            insertedAppZnsTemplate = null;
        }
    }

    @Test
    @Transactional
    void createAppZnsTemplate() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AppZnsTemplate
        var returnedAppZnsTemplate = om.readValue(
            restAppZnsTemplateMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appZnsTemplate)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AppZnsTemplate.class
        );

        // Validate the AppZnsTemplate in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAppZnsTemplateUpdatableFieldsEquals(returnedAppZnsTemplate, getPersistedAppZnsTemplate(returnedAppZnsTemplate));

        insertedAppZnsTemplate = returnedAppZnsTemplate;
    }

    @Test
    @Transactional
    void createAppZnsTemplateWithExistingId() throws Exception {
        // Create the AppZnsTemplate with an existing ID
        appZnsTemplate.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppZnsTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appZnsTemplate)))
            .andExpect(status().isBadRequest());

        // Validate the AppZnsTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appZnsTemplate.setName(null);

        // Create the AppZnsTemplate, which fails.

        restAppZnsTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appZnsTemplate)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAppZnsTemplates() throws Exception {
        // Initialize the database
        insertedAppZnsTemplate = appZnsTemplateRepository.saveAndFlush(appZnsTemplate);

        // Get all the appZnsTemplateList
        restAppZnsTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appZnsTemplate.getId().intValue())))
            .andExpect(jsonPath("$.[*].znsAction").value(hasItem(DEFAULT_ZNS_ACTION.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].templateId").value(hasItem(DEFAULT_TEMPLATE_ID)))
            .andExpect(jsonPath("$.[*].dataSourceMappingType").value(hasItem(DEFAULT_DATA_SOURCE_MAPPING_TYPE.toString())))
            .andExpect(jsonPath("$.[*].templateDataMapping").value(hasItem(DEFAULT_TEMPLATE_DATA_MAPPING)))
            .andExpect(jsonPath("$.[*].targetUrls").value(hasItem(DEFAULT_TARGET_URLS)));
    }

    @Test
    @Transactional
    void getAppZnsTemplate() throws Exception {
        // Initialize the database
        insertedAppZnsTemplate = appZnsTemplateRepository.saveAndFlush(appZnsTemplate);

        // Get the appZnsTemplate
        restAppZnsTemplateMockMvc
            .perform(get(ENTITY_API_URL_ID, appZnsTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appZnsTemplate.getId().intValue()))
            .andExpect(jsonPath("$.znsAction").value(DEFAULT_ZNS_ACTION.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.templateId").value(DEFAULT_TEMPLATE_ID))
            .andExpect(jsonPath("$.dataSourceMappingType").value(DEFAULT_DATA_SOURCE_MAPPING_TYPE.toString()))
            .andExpect(jsonPath("$.templateDataMapping").value(DEFAULT_TEMPLATE_DATA_MAPPING))
            .andExpect(jsonPath("$.targetUrls").value(DEFAULT_TARGET_URLS));
    }

    @Test
    @Transactional
    void getAppZnsTemplatesByIdFiltering() throws Exception {
        // Initialize the database
        insertedAppZnsTemplate = appZnsTemplateRepository.saveAndFlush(appZnsTemplate);

        Long id = appZnsTemplate.getId();

        defaultAppZnsTemplateFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAppZnsTemplateFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAppZnsTemplateFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAppZnsTemplatesByZnsActionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppZnsTemplate = appZnsTemplateRepository.saveAndFlush(appZnsTemplate);

        // Get all the appZnsTemplateList where znsAction equals to
        defaultAppZnsTemplateFiltering("znsAction.equals=" + DEFAULT_ZNS_ACTION, "znsAction.equals=" + UPDATED_ZNS_ACTION);
    }

    @Test
    @Transactional
    void getAllAppZnsTemplatesByZnsActionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppZnsTemplate = appZnsTemplateRepository.saveAndFlush(appZnsTemplate);

        // Get all the appZnsTemplateList where znsAction in
        defaultAppZnsTemplateFiltering(
            "znsAction.in=" + DEFAULT_ZNS_ACTION + "," + UPDATED_ZNS_ACTION,
            "znsAction.in=" + UPDATED_ZNS_ACTION
        );
    }

    @Test
    @Transactional
    void getAllAppZnsTemplatesByZnsActionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppZnsTemplate = appZnsTemplateRepository.saveAndFlush(appZnsTemplate);

        // Get all the appZnsTemplateList where znsAction is not null
        defaultAppZnsTemplateFiltering("znsAction.specified=true", "znsAction.specified=false");
    }

    @Test
    @Transactional
    void getAllAppZnsTemplatesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppZnsTemplate = appZnsTemplateRepository.saveAndFlush(appZnsTemplate);

        // Get all the appZnsTemplateList where name equals to
        defaultAppZnsTemplateFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAppZnsTemplatesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppZnsTemplate = appZnsTemplateRepository.saveAndFlush(appZnsTemplate);

        // Get all the appZnsTemplateList where name in
        defaultAppZnsTemplateFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAppZnsTemplatesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppZnsTemplate = appZnsTemplateRepository.saveAndFlush(appZnsTemplate);

        // Get all the appZnsTemplateList where name is not null
        defaultAppZnsTemplateFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAppZnsTemplatesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAppZnsTemplate = appZnsTemplateRepository.saveAndFlush(appZnsTemplate);

        // Get all the appZnsTemplateList where name contains
        defaultAppZnsTemplateFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAppZnsTemplatesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAppZnsTemplate = appZnsTemplateRepository.saveAndFlush(appZnsTemplate);

        // Get all the appZnsTemplateList where name does not contain
        defaultAppZnsTemplateFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAppZnsTemplatesByTemplateIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppZnsTemplate = appZnsTemplateRepository.saveAndFlush(appZnsTemplate);

        // Get all the appZnsTemplateList where templateId equals to
        defaultAppZnsTemplateFiltering("templateId.equals=" + DEFAULT_TEMPLATE_ID, "templateId.equals=" + UPDATED_TEMPLATE_ID);
    }

    @Test
    @Transactional
    void getAllAppZnsTemplatesByTemplateIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppZnsTemplate = appZnsTemplateRepository.saveAndFlush(appZnsTemplate);

        // Get all the appZnsTemplateList where templateId in
        defaultAppZnsTemplateFiltering(
            "templateId.in=" + DEFAULT_TEMPLATE_ID + "," + UPDATED_TEMPLATE_ID,
            "templateId.in=" + UPDATED_TEMPLATE_ID
        );
    }

    @Test
    @Transactional
    void getAllAppZnsTemplatesByTemplateIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppZnsTemplate = appZnsTemplateRepository.saveAndFlush(appZnsTemplate);

        // Get all the appZnsTemplateList where templateId is not null
        defaultAppZnsTemplateFiltering("templateId.specified=true", "templateId.specified=false");
    }

    @Test
    @Transactional
    void getAllAppZnsTemplatesByTemplateIdContainsSomething() throws Exception {
        // Initialize the database
        insertedAppZnsTemplate = appZnsTemplateRepository.saveAndFlush(appZnsTemplate);

        // Get all the appZnsTemplateList where templateId contains
        defaultAppZnsTemplateFiltering("templateId.contains=" + DEFAULT_TEMPLATE_ID, "templateId.contains=" + UPDATED_TEMPLATE_ID);
    }

    @Test
    @Transactional
    void getAllAppZnsTemplatesByTemplateIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAppZnsTemplate = appZnsTemplateRepository.saveAndFlush(appZnsTemplate);

        // Get all the appZnsTemplateList where templateId does not contain
        defaultAppZnsTemplateFiltering(
            "templateId.doesNotContain=" + UPDATED_TEMPLATE_ID,
            "templateId.doesNotContain=" + DEFAULT_TEMPLATE_ID
        );
    }

    @Test
    @Transactional
    void getAllAppZnsTemplatesByDataSourceMappingTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppZnsTemplate = appZnsTemplateRepository.saveAndFlush(appZnsTemplate);

        // Get all the appZnsTemplateList where dataSourceMappingType equals to
        defaultAppZnsTemplateFiltering(
            "dataSourceMappingType.equals=" + DEFAULT_DATA_SOURCE_MAPPING_TYPE,
            "dataSourceMappingType.equals=" + UPDATED_DATA_SOURCE_MAPPING_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAppZnsTemplatesByDataSourceMappingTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppZnsTemplate = appZnsTemplateRepository.saveAndFlush(appZnsTemplate);

        // Get all the appZnsTemplateList where dataSourceMappingType in
        defaultAppZnsTemplateFiltering(
            "dataSourceMappingType.in=" + DEFAULT_DATA_SOURCE_MAPPING_TYPE + "," + UPDATED_DATA_SOURCE_MAPPING_TYPE,
            "dataSourceMappingType.in=" + UPDATED_DATA_SOURCE_MAPPING_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAppZnsTemplatesByDataSourceMappingTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppZnsTemplate = appZnsTemplateRepository.saveAndFlush(appZnsTemplate);

        // Get all the appZnsTemplateList where dataSourceMappingType is not null
        defaultAppZnsTemplateFiltering("dataSourceMappingType.specified=true", "dataSourceMappingType.specified=false");
    }

    @Test
    @Transactional
    void getAllAppZnsTemplatesByTemplateDataMappingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppZnsTemplate = appZnsTemplateRepository.saveAndFlush(appZnsTemplate);

        // Get all the appZnsTemplateList where templateDataMapping equals to
        defaultAppZnsTemplateFiltering(
            "templateDataMapping.equals=" + DEFAULT_TEMPLATE_DATA_MAPPING,
            "templateDataMapping.equals=" + UPDATED_TEMPLATE_DATA_MAPPING
        );
    }

    @Test
    @Transactional
    void getAllAppZnsTemplatesByTemplateDataMappingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppZnsTemplate = appZnsTemplateRepository.saveAndFlush(appZnsTemplate);

        // Get all the appZnsTemplateList where templateDataMapping in
        defaultAppZnsTemplateFiltering(
            "templateDataMapping.in=" + DEFAULT_TEMPLATE_DATA_MAPPING + "," + UPDATED_TEMPLATE_DATA_MAPPING,
            "templateDataMapping.in=" + UPDATED_TEMPLATE_DATA_MAPPING
        );
    }

    @Test
    @Transactional
    void getAllAppZnsTemplatesByTemplateDataMappingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppZnsTemplate = appZnsTemplateRepository.saveAndFlush(appZnsTemplate);

        // Get all the appZnsTemplateList where templateDataMapping is not null
        defaultAppZnsTemplateFiltering("templateDataMapping.specified=true", "templateDataMapping.specified=false");
    }

    @Test
    @Transactional
    void getAllAppZnsTemplatesByTemplateDataMappingContainsSomething() throws Exception {
        // Initialize the database
        insertedAppZnsTemplate = appZnsTemplateRepository.saveAndFlush(appZnsTemplate);

        // Get all the appZnsTemplateList where templateDataMapping contains
        defaultAppZnsTemplateFiltering(
            "templateDataMapping.contains=" + DEFAULT_TEMPLATE_DATA_MAPPING,
            "templateDataMapping.contains=" + UPDATED_TEMPLATE_DATA_MAPPING
        );
    }

    @Test
    @Transactional
    void getAllAppZnsTemplatesByTemplateDataMappingNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAppZnsTemplate = appZnsTemplateRepository.saveAndFlush(appZnsTemplate);

        // Get all the appZnsTemplateList where templateDataMapping does not contain
        defaultAppZnsTemplateFiltering(
            "templateDataMapping.doesNotContain=" + UPDATED_TEMPLATE_DATA_MAPPING,
            "templateDataMapping.doesNotContain=" + DEFAULT_TEMPLATE_DATA_MAPPING
        );
    }

    @Test
    @Transactional
    void getAllAppZnsTemplatesByTargetUrlsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppZnsTemplate = appZnsTemplateRepository.saveAndFlush(appZnsTemplate);

        // Get all the appZnsTemplateList where targetUrls equals to
        defaultAppZnsTemplateFiltering("targetUrls.equals=" + DEFAULT_TARGET_URLS, "targetUrls.equals=" + UPDATED_TARGET_URLS);
    }

    @Test
    @Transactional
    void getAllAppZnsTemplatesByTargetUrlsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppZnsTemplate = appZnsTemplateRepository.saveAndFlush(appZnsTemplate);

        // Get all the appZnsTemplateList where targetUrls in
        defaultAppZnsTemplateFiltering(
            "targetUrls.in=" + DEFAULT_TARGET_URLS + "," + UPDATED_TARGET_URLS,
            "targetUrls.in=" + UPDATED_TARGET_URLS
        );
    }

    @Test
    @Transactional
    void getAllAppZnsTemplatesByTargetUrlsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppZnsTemplate = appZnsTemplateRepository.saveAndFlush(appZnsTemplate);

        // Get all the appZnsTemplateList where targetUrls is not null
        defaultAppZnsTemplateFiltering("targetUrls.specified=true", "targetUrls.specified=false");
    }

    @Test
    @Transactional
    void getAllAppZnsTemplatesByTargetUrlsContainsSomething() throws Exception {
        // Initialize the database
        insertedAppZnsTemplate = appZnsTemplateRepository.saveAndFlush(appZnsTemplate);

        // Get all the appZnsTemplateList where targetUrls contains
        defaultAppZnsTemplateFiltering("targetUrls.contains=" + DEFAULT_TARGET_URLS, "targetUrls.contains=" + UPDATED_TARGET_URLS);
    }

    @Test
    @Transactional
    void getAllAppZnsTemplatesByTargetUrlsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAppZnsTemplate = appZnsTemplateRepository.saveAndFlush(appZnsTemplate);

        // Get all the appZnsTemplateList where targetUrls does not contain
        defaultAppZnsTemplateFiltering(
            "targetUrls.doesNotContain=" + UPDATED_TARGET_URLS,
            "targetUrls.doesNotContain=" + DEFAULT_TARGET_URLS
        );
    }

    @Test
    @Transactional
    void getAllAppZnsTemplatesByThumbnailIsEqualToSomething() throws Exception {
        Metaverse thumbnail;
        if (TestUtil.findAll(em, Metaverse.class).isEmpty()) {
            appZnsTemplateRepository.saveAndFlush(appZnsTemplate);
            thumbnail = MetaverseResourceIT.createEntity();
        } else {
            thumbnail = TestUtil.findAll(em, Metaverse.class).get(0);
        }
        em.persist(thumbnail);
        em.flush();
        appZnsTemplate.setThumbnail(thumbnail);
        appZnsTemplateRepository.saveAndFlush(appZnsTemplate);
        Long thumbnailId = thumbnail.getId();
        // Get all the appZnsTemplateList where thumbnail equals to thumbnailId
        defaultAppZnsTemplateShouldBeFound("thumbnailId.equals=" + thumbnailId);

        // Get all the appZnsTemplateList where thumbnail equals to (thumbnailId + 1)
        defaultAppZnsTemplateShouldNotBeFound("thumbnailId.equals=" + (thumbnailId + 1));
    }

    @Test
    @Transactional
    void getAllAppZnsTemplatesByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            appZnsTemplateRepository.saveAndFlush(appZnsTemplate);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        appZnsTemplate.setApplication(application);
        appZnsTemplateRepository.saveAndFlush(appZnsTemplate);
        UUID applicationId = application.getId();
        // Get all the appZnsTemplateList where application equals to applicationId
        defaultAppZnsTemplateShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the appZnsTemplateList where application equals to UUID.randomUUID()
        defaultAppZnsTemplateShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAppZnsTemplateFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAppZnsTemplateShouldBeFound(shouldBeFound);
        defaultAppZnsTemplateShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAppZnsTemplateShouldBeFound(String filter) throws Exception {
        restAppZnsTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appZnsTemplate.getId().intValue())))
            .andExpect(jsonPath("$.[*].znsAction").value(hasItem(DEFAULT_ZNS_ACTION.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].templateId").value(hasItem(DEFAULT_TEMPLATE_ID)))
            .andExpect(jsonPath("$.[*].dataSourceMappingType").value(hasItem(DEFAULT_DATA_SOURCE_MAPPING_TYPE.toString())))
            .andExpect(jsonPath("$.[*].templateDataMapping").value(hasItem(DEFAULT_TEMPLATE_DATA_MAPPING)))
            .andExpect(jsonPath("$.[*].targetUrls").value(hasItem(DEFAULT_TARGET_URLS)));

        // Check, that the count call also returns 1
        restAppZnsTemplateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAppZnsTemplateShouldNotBeFound(String filter) throws Exception {
        restAppZnsTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAppZnsTemplateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAppZnsTemplate() throws Exception {
        // Get the appZnsTemplate
        restAppZnsTemplateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAppZnsTemplate() throws Exception {
        // Initialize the database
        insertedAppZnsTemplate = appZnsTemplateRepository.saveAndFlush(appZnsTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appZnsTemplate
        AppZnsTemplate updatedAppZnsTemplate = appZnsTemplateRepository.findById(appZnsTemplate.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAppZnsTemplate are not directly saved in db
        em.detach(updatedAppZnsTemplate);
        updatedAppZnsTemplate
            .znsAction(UPDATED_ZNS_ACTION)
            .name(UPDATED_NAME)
            .templateId(UPDATED_TEMPLATE_ID)
            .dataSourceMappingType(UPDATED_DATA_SOURCE_MAPPING_TYPE)
            .templateDataMapping(UPDATED_TEMPLATE_DATA_MAPPING)
            .targetUrls(UPDATED_TARGET_URLS);

        restAppZnsTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAppZnsTemplate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAppZnsTemplate))
            )
            .andExpect(status().isOk());

        // Validate the AppZnsTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAppZnsTemplateToMatchAllProperties(updatedAppZnsTemplate);
    }

    @Test
    @Transactional
    void putNonExistingAppZnsTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appZnsTemplate.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppZnsTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appZnsTemplate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(appZnsTemplate))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppZnsTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppZnsTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appZnsTemplate.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppZnsTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(appZnsTemplate))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppZnsTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppZnsTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appZnsTemplate.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppZnsTemplateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appZnsTemplate)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppZnsTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAppZnsTemplateWithPatch() throws Exception {
        // Initialize the database
        insertedAppZnsTemplate = appZnsTemplateRepository.saveAndFlush(appZnsTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appZnsTemplate using partial update
        AppZnsTemplate partialUpdatedAppZnsTemplate = new AppZnsTemplate();
        partialUpdatedAppZnsTemplate.setId(appZnsTemplate.getId());

        partialUpdatedAppZnsTemplate
            .znsAction(UPDATED_ZNS_ACTION)
            .name(UPDATED_NAME)
            .templateDataMapping(UPDATED_TEMPLATE_DATA_MAPPING)
            .targetUrls(UPDATED_TARGET_URLS);

        restAppZnsTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppZnsTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAppZnsTemplate))
            )
            .andExpect(status().isOk());

        // Validate the AppZnsTemplate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppZnsTemplateUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAppZnsTemplate, appZnsTemplate),
            getPersistedAppZnsTemplate(appZnsTemplate)
        );
    }

    @Test
    @Transactional
    void fullUpdateAppZnsTemplateWithPatch() throws Exception {
        // Initialize the database
        insertedAppZnsTemplate = appZnsTemplateRepository.saveAndFlush(appZnsTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appZnsTemplate using partial update
        AppZnsTemplate partialUpdatedAppZnsTemplate = new AppZnsTemplate();
        partialUpdatedAppZnsTemplate.setId(appZnsTemplate.getId());

        partialUpdatedAppZnsTemplate
            .znsAction(UPDATED_ZNS_ACTION)
            .name(UPDATED_NAME)
            .templateId(UPDATED_TEMPLATE_ID)
            .dataSourceMappingType(UPDATED_DATA_SOURCE_MAPPING_TYPE)
            .templateDataMapping(UPDATED_TEMPLATE_DATA_MAPPING)
            .targetUrls(UPDATED_TARGET_URLS);

        restAppZnsTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppZnsTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAppZnsTemplate))
            )
            .andExpect(status().isOk());

        // Validate the AppZnsTemplate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppZnsTemplateUpdatableFieldsEquals(partialUpdatedAppZnsTemplate, getPersistedAppZnsTemplate(partialUpdatedAppZnsTemplate));
    }

    @Test
    @Transactional
    void patchNonExistingAppZnsTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appZnsTemplate.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppZnsTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appZnsTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(appZnsTemplate))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppZnsTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppZnsTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appZnsTemplate.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppZnsTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(appZnsTemplate))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppZnsTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppZnsTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appZnsTemplate.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppZnsTemplateMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(appZnsTemplate)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppZnsTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAppZnsTemplate() throws Exception {
        // Initialize the database
        insertedAppZnsTemplate = appZnsTemplateRepository.saveAndFlush(appZnsTemplate);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the appZnsTemplate
        restAppZnsTemplateMockMvc
            .perform(delete(ENTITY_API_URL_ID, appZnsTemplate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return appZnsTemplateRepository.count();
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

    protected AppZnsTemplate getPersistedAppZnsTemplate(AppZnsTemplate appZnsTemplate) {
        return appZnsTemplateRepository.findById(appZnsTemplate.getId()).orElseThrow();
    }

    protected void assertPersistedAppZnsTemplateToMatchAllProperties(AppZnsTemplate expectedAppZnsTemplate) {
        assertAppZnsTemplateAllPropertiesEquals(expectedAppZnsTemplate, getPersistedAppZnsTemplate(expectedAppZnsTemplate));
    }

    protected void assertPersistedAppZnsTemplateToMatchUpdatableProperties(AppZnsTemplate expectedAppZnsTemplate) {
        assertAppZnsTemplateAllUpdatablePropertiesEquals(expectedAppZnsTemplate, getPersistedAppZnsTemplate(expectedAppZnsTemplate));
    }
}
