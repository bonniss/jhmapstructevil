package ai.realworld.web.rest;

import static ai.realworld.domain.AppMessageTemplateAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AppMessageTemplate;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.domain.enumeration.AlphaSourceType;
import ai.realworld.repository.AppMessageTemplateRepository;
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
 * Integration tests for the {@link AppMessageTemplateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AppMessageTemplateResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_TOP_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_TOP_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_BOTTOM_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_BOTTOM_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_PROP_TITLE_MAPPING_JASON = "AAAAAAAAAA";
    private static final String UPDATED_PROP_TITLE_MAPPING_JASON = "BBBBBBBBBB";

    private static final AlphaSourceType DEFAULT_DATA_SOURCE_MAPPING_TYPE = AlphaSourceType.COMPLETED_ORDER;
    private static final AlphaSourceType UPDATED_DATA_SOURCE_MAPPING_TYPE = AlphaSourceType.BOOKING_INFORMATION;

    private static final String DEFAULT_TARGET_URLS = "AAAAAAAAAA";
    private static final String UPDATED_TARGET_URLS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/app-message-templates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AppMessageTemplateRepository appMessageTemplateRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppMessageTemplateMockMvc;

    private AppMessageTemplate appMessageTemplate;

    private AppMessageTemplate insertedAppMessageTemplate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppMessageTemplate createEntity() {
        return new AppMessageTemplate()
            .title(DEFAULT_TITLE)
            .topContent(DEFAULT_TOP_CONTENT)
            .content(DEFAULT_CONTENT)
            .bottomContent(DEFAULT_BOTTOM_CONTENT)
            .propTitleMappingJason(DEFAULT_PROP_TITLE_MAPPING_JASON)
            .dataSourceMappingType(DEFAULT_DATA_SOURCE_MAPPING_TYPE)
            .targetUrls(DEFAULT_TARGET_URLS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppMessageTemplate createUpdatedEntity() {
        return new AppMessageTemplate()
            .title(UPDATED_TITLE)
            .topContent(UPDATED_TOP_CONTENT)
            .content(UPDATED_CONTENT)
            .bottomContent(UPDATED_BOTTOM_CONTENT)
            .propTitleMappingJason(UPDATED_PROP_TITLE_MAPPING_JASON)
            .dataSourceMappingType(UPDATED_DATA_SOURCE_MAPPING_TYPE)
            .targetUrls(UPDATED_TARGET_URLS);
    }

    @BeforeEach
    public void initTest() {
        appMessageTemplate = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAppMessageTemplate != null) {
            appMessageTemplateRepository.delete(insertedAppMessageTemplate);
            insertedAppMessageTemplate = null;
        }
    }

    @Test
    @Transactional
    void createAppMessageTemplate() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AppMessageTemplate
        var returnedAppMessageTemplate = om.readValue(
            restAppMessageTemplateMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appMessageTemplate)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AppMessageTemplate.class
        );

        // Validate the AppMessageTemplate in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAppMessageTemplateUpdatableFieldsEquals(
            returnedAppMessageTemplate,
            getPersistedAppMessageTemplate(returnedAppMessageTemplate)
        );

        insertedAppMessageTemplate = returnedAppMessageTemplate;
    }

    @Test
    @Transactional
    void createAppMessageTemplateWithExistingId() throws Exception {
        // Create the AppMessageTemplate with an existing ID
        appMessageTemplate.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppMessageTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appMessageTemplate)))
            .andExpect(status().isBadRequest());

        // Validate the AppMessageTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appMessageTemplate.setTitle(null);

        // Create the AppMessageTemplate, which fails.

        restAppMessageTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appMessageTemplate)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAppMessageTemplates() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        // Get all the appMessageTemplateList
        restAppMessageTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appMessageTemplate.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].topContent").value(hasItem(DEFAULT_TOP_CONTENT)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].bottomContent").value(hasItem(DEFAULT_BOTTOM_CONTENT)))
            .andExpect(jsonPath("$.[*].propTitleMappingJason").value(hasItem(DEFAULT_PROP_TITLE_MAPPING_JASON)))
            .andExpect(jsonPath("$.[*].dataSourceMappingType").value(hasItem(DEFAULT_DATA_SOURCE_MAPPING_TYPE.toString())))
            .andExpect(jsonPath("$.[*].targetUrls").value(hasItem(DEFAULT_TARGET_URLS)));
    }

    @Test
    @Transactional
    void getAppMessageTemplate() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        // Get the appMessageTemplate
        restAppMessageTemplateMockMvc
            .perform(get(ENTITY_API_URL_ID, appMessageTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appMessageTemplate.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.topContent").value(DEFAULT_TOP_CONTENT))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.bottomContent").value(DEFAULT_BOTTOM_CONTENT))
            .andExpect(jsonPath("$.propTitleMappingJason").value(DEFAULT_PROP_TITLE_MAPPING_JASON))
            .andExpect(jsonPath("$.dataSourceMappingType").value(DEFAULT_DATA_SOURCE_MAPPING_TYPE.toString()))
            .andExpect(jsonPath("$.targetUrls").value(DEFAULT_TARGET_URLS));
    }

    @Test
    @Transactional
    void getAppMessageTemplatesByIdFiltering() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        Long id = appMessageTemplate.getId();

        defaultAppMessageTemplateFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAppMessageTemplateFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAppMessageTemplateFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAppMessageTemplatesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        // Get all the appMessageTemplateList where title equals to
        defaultAppMessageTemplateFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAppMessageTemplatesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        // Get all the appMessageTemplateList where title in
        defaultAppMessageTemplateFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAppMessageTemplatesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        // Get all the appMessageTemplateList where title is not null
        defaultAppMessageTemplateFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllAppMessageTemplatesByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        // Get all the appMessageTemplateList where title contains
        defaultAppMessageTemplateFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAppMessageTemplatesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        // Get all the appMessageTemplateList where title does not contain
        defaultAppMessageTemplateFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllAppMessageTemplatesByTopContentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        // Get all the appMessageTemplateList where topContent equals to
        defaultAppMessageTemplateFiltering("topContent.equals=" + DEFAULT_TOP_CONTENT, "topContent.equals=" + UPDATED_TOP_CONTENT);
    }

    @Test
    @Transactional
    void getAllAppMessageTemplatesByTopContentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        // Get all the appMessageTemplateList where topContent in
        defaultAppMessageTemplateFiltering(
            "topContent.in=" + DEFAULT_TOP_CONTENT + "," + UPDATED_TOP_CONTENT,
            "topContent.in=" + UPDATED_TOP_CONTENT
        );
    }

    @Test
    @Transactional
    void getAllAppMessageTemplatesByTopContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        // Get all the appMessageTemplateList where topContent is not null
        defaultAppMessageTemplateFiltering("topContent.specified=true", "topContent.specified=false");
    }

    @Test
    @Transactional
    void getAllAppMessageTemplatesByTopContentContainsSomething() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        // Get all the appMessageTemplateList where topContent contains
        defaultAppMessageTemplateFiltering("topContent.contains=" + DEFAULT_TOP_CONTENT, "topContent.contains=" + UPDATED_TOP_CONTENT);
    }

    @Test
    @Transactional
    void getAllAppMessageTemplatesByTopContentNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        // Get all the appMessageTemplateList where topContent does not contain
        defaultAppMessageTemplateFiltering(
            "topContent.doesNotContain=" + UPDATED_TOP_CONTENT,
            "topContent.doesNotContain=" + DEFAULT_TOP_CONTENT
        );
    }

    @Test
    @Transactional
    void getAllAppMessageTemplatesByContentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        // Get all the appMessageTemplateList where content equals to
        defaultAppMessageTemplateFiltering("content.equals=" + DEFAULT_CONTENT, "content.equals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllAppMessageTemplatesByContentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        // Get all the appMessageTemplateList where content in
        defaultAppMessageTemplateFiltering("content.in=" + DEFAULT_CONTENT + "," + UPDATED_CONTENT, "content.in=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllAppMessageTemplatesByContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        // Get all the appMessageTemplateList where content is not null
        defaultAppMessageTemplateFiltering("content.specified=true", "content.specified=false");
    }

    @Test
    @Transactional
    void getAllAppMessageTemplatesByContentContainsSomething() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        // Get all the appMessageTemplateList where content contains
        defaultAppMessageTemplateFiltering("content.contains=" + DEFAULT_CONTENT, "content.contains=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllAppMessageTemplatesByContentNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        // Get all the appMessageTemplateList where content does not contain
        defaultAppMessageTemplateFiltering("content.doesNotContain=" + UPDATED_CONTENT, "content.doesNotContain=" + DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    void getAllAppMessageTemplatesByBottomContentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        // Get all the appMessageTemplateList where bottomContent equals to
        defaultAppMessageTemplateFiltering(
            "bottomContent.equals=" + DEFAULT_BOTTOM_CONTENT,
            "bottomContent.equals=" + UPDATED_BOTTOM_CONTENT
        );
    }

    @Test
    @Transactional
    void getAllAppMessageTemplatesByBottomContentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        // Get all the appMessageTemplateList where bottomContent in
        defaultAppMessageTemplateFiltering(
            "bottomContent.in=" + DEFAULT_BOTTOM_CONTENT + "," + UPDATED_BOTTOM_CONTENT,
            "bottomContent.in=" + UPDATED_BOTTOM_CONTENT
        );
    }

    @Test
    @Transactional
    void getAllAppMessageTemplatesByBottomContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        // Get all the appMessageTemplateList where bottomContent is not null
        defaultAppMessageTemplateFiltering("bottomContent.specified=true", "bottomContent.specified=false");
    }

    @Test
    @Transactional
    void getAllAppMessageTemplatesByBottomContentContainsSomething() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        // Get all the appMessageTemplateList where bottomContent contains
        defaultAppMessageTemplateFiltering(
            "bottomContent.contains=" + DEFAULT_BOTTOM_CONTENT,
            "bottomContent.contains=" + UPDATED_BOTTOM_CONTENT
        );
    }

    @Test
    @Transactional
    void getAllAppMessageTemplatesByBottomContentNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        // Get all the appMessageTemplateList where bottomContent does not contain
        defaultAppMessageTemplateFiltering(
            "bottomContent.doesNotContain=" + UPDATED_BOTTOM_CONTENT,
            "bottomContent.doesNotContain=" + DEFAULT_BOTTOM_CONTENT
        );
    }

    @Test
    @Transactional
    void getAllAppMessageTemplatesByPropTitleMappingJasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        // Get all the appMessageTemplateList where propTitleMappingJason equals to
        defaultAppMessageTemplateFiltering(
            "propTitleMappingJason.equals=" + DEFAULT_PROP_TITLE_MAPPING_JASON,
            "propTitleMappingJason.equals=" + UPDATED_PROP_TITLE_MAPPING_JASON
        );
    }

    @Test
    @Transactional
    void getAllAppMessageTemplatesByPropTitleMappingJasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        // Get all the appMessageTemplateList where propTitleMappingJason in
        defaultAppMessageTemplateFiltering(
            "propTitleMappingJason.in=" + DEFAULT_PROP_TITLE_MAPPING_JASON + "," + UPDATED_PROP_TITLE_MAPPING_JASON,
            "propTitleMappingJason.in=" + UPDATED_PROP_TITLE_MAPPING_JASON
        );
    }

    @Test
    @Transactional
    void getAllAppMessageTemplatesByPropTitleMappingJasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        // Get all the appMessageTemplateList where propTitleMappingJason is not null
        defaultAppMessageTemplateFiltering("propTitleMappingJason.specified=true", "propTitleMappingJason.specified=false");
    }

    @Test
    @Transactional
    void getAllAppMessageTemplatesByPropTitleMappingJasonContainsSomething() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        // Get all the appMessageTemplateList where propTitleMappingJason contains
        defaultAppMessageTemplateFiltering(
            "propTitleMappingJason.contains=" + DEFAULT_PROP_TITLE_MAPPING_JASON,
            "propTitleMappingJason.contains=" + UPDATED_PROP_TITLE_MAPPING_JASON
        );
    }

    @Test
    @Transactional
    void getAllAppMessageTemplatesByPropTitleMappingJasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        // Get all the appMessageTemplateList where propTitleMappingJason does not contain
        defaultAppMessageTemplateFiltering(
            "propTitleMappingJason.doesNotContain=" + UPDATED_PROP_TITLE_MAPPING_JASON,
            "propTitleMappingJason.doesNotContain=" + DEFAULT_PROP_TITLE_MAPPING_JASON
        );
    }

    @Test
    @Transactional
    void getAllAppMessageTemplatesByDataSourceMappingTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        // Get all the appMessageTemplateList where dataSourceMappingType equals to
        defaultAppMessageTemplateFiltering(
            "dataSourceMappingType.equals=" + DEFAULT_DATA_SOURCE_MAPPING_TYPE,
            "dataSourceMappingType.equals=" + UPDATED_DATA_SOURCE_MAPPING_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAppMessageTemplatesByDataSourceMappingTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        // Get all the appMessageTemplateList where dataSourceMappingType in
        defaultAppMessageTemplateFiltering(
            "dataSourceMappingType.in=" + DEFAULT_DATA_SOURCE_MAPPING_TYPE + "," + UPDATED_DATA_SOURCE_MAPPING_TYPE,
            "dataSourceMappingType.in=" + UPDATED_DATA_SOURCE_MAPPING_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAppMessageTemplatesByDataSourceMappingTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        // Get all the appMessageTemplateList where dataSourceMappingType is not null
        defaultAppMessageTemplateFiltering("dataSourceMappingType.specified=true", "dataSourceMappingType.specified=false");
    }

    @Test
    @Transactional
    void getAllAppMessageTemplatesByTargetUrlsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        // Get all the appMessageTemplateList where targetUrls equals to
        defaultAppMessageTemplateFiltering("targetUrls.equals=" + DEFAULT_TARGET_URLS, "targetUrls.equals=" + UPDATED_TARGET_URLS);
    }

    @Test
    @Transactional
    void getAllAppMessageTemplatesByTargetUrlsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        // Get all the appMessageTemplateList where targetUrls in
        defaultAppMessageTemplateFiltering(
            "targetUrls.in=" + DEFAULT_TARGET_URLS + "," + UPDATED_TARGET_URLS,
            "targetUrls.in=" + UPDATED_TARGET_URLS
        );
    }

    @Test
    @Transactional
    void getAllAppMessageTemplatesByTargetUrlsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        // Get all the appMessageTemplateList where targetUrls is not null
        defaultAppMessageTemplateFiltering("targetUrls.specified=true", "targetUrls.specified=false");
    }

    @Test
    @Transactional
    void getAllAppMessageTemplatesByTargetUrlsContainsSomething() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        // Get all the appMessageTemplateList where targetUrls contains
        defaultAppMessageTemplateFiltering("targetUrls.contains=" + DEFAULT_TARGET_URLS, "targetUrls.contains=" + UPDATED_TARGET_URLS);
    }

    @Test
    @Transactional
    void getAllAppMessageTemplatesByTargetUrlsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        // Get all the appMessageTemplateList where targetUrls does not contain
        defaultAppMessageTemplateFiltering(
            "targetUrls.doesNotContain=" + UPDATED_TARGET_URLS,
            "targetUrls.doesNotContain=" + DEFAULT_TARGET_URLS
        );
    }

    @Test
    @Transactional
    void getAllAppMessageTemplatesByThumbnailIsEqualToSomething() throws Exception {
        Metaverse thumbnail;
        if (TestUtil.findAll(em, Metaverse.class).isEmpty()) {
            appMessageTemplateRepository.saveAndFlush(appMessageTemplate);
            thumbnail = MetaverseResourceIT.createEntity();
        } else {
            thumbnail = TestUtil.findAll(em, Metaverse.class).get(0);
        }
        em.persist(thumbnail);
        em.flush();
        appMessageTemplate.setThumbnail(thumbnail);
        appMessageTemplateRepository.saveAndFlush(appMessageTemplate);
        Long thumbnailId = thumbnail.getId();
        // Get all the appMessageTemplateList where thumbnail equals to thumbnailId
        defaultAppMessageTemplateShouldBeFound("thumbnailId.equals=" + thumbnailId);

        // Get all the appMessageTemplateList where thumbnail equals to (thumbnailId + 1)
        defaultAppMessageTemplateShouldNotBeFound("thumbnailId.equals=" + (thumbnailId + 1));
    }

    @Test
    @Transactional
    void getAllAppMessageTemplatesByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            appMessageTemplateRepository.saveAndFlush(appMessageTemplate);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        appMessageTemplate.setApplication(application);
        appMessageTemplateRepository.saveAndFlush(appMessageTemplate);
        UUID applicationId = application.getId();
        // Get all the appMessageTemplateList where application equals to applicationId
        defaultAppMessageTemplateShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the appMessageTemplateList where application equals to UUID.randomUUID()
        defaultAppMessageTemplateShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAppMessageTemplateFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAppMessageTemplateShouldBeFound(shouldBeFound);
        defaultAppMessageTemplateShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAppMessageTemplateShouldBeFound(String filter) throws Exception {
        restAppMessageTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appMessageTemplate.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].topContent").value(hasItem(DEFAULT_TOP_CONTENT)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].bottomContent").value(hasItem(DEFAULT_BOTTOM_CONTENT)))
            .andExpect(jsonPath("$.[*].propTitleMappingJason").value(hasItem(DEFAULT_PROP_TITLE_MAPPING_JASON)))
            .andExpect(jsonPath("$.[*].dataSourceMappingType").value(hasItem(DEFAULT_DATA_SOURCE_MAPPING_TYPE.toString())))
            .andExpect(jsonPath("$.[*].targetUrls").value(hasItem(DEFAULT_TARGET_URLS)));

        // Check, that the count call also returns 1
        restAppMessageTemplateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAppMessageTemplateShouldNotBeFound(String filter) throws Exception {
        restAppMessageTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAppMessageTemplateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAppMessageTemplate() throws Exception {
        // Get the appMessageTemplate
        restAppMessageTemplateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAppMessageTemplate() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appMessageTemplate
        AppMessageTemplate updatedAppMessageTemplate = appMessageTemplateRepository.findById(appMessageTemplate.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAppMessageTemplate are not directly saved in db
        em.detach(updatedAppMessageTemplate);
        updatedAppMessageTemplate
            .title(UPDATED_TITLE)
            .topContent(UPDATED_TOP_CONTENT)
            .content(UPDATED_CONTENT)
            .bottomContent(UPDATED_BOTTOM_CONTENT)
            .propTitleMappingJason(UPDATED_PROP_TITLE_MAPPING_JASON)
            .dataSourceMappingType(UPDATED_DATA_SOURCE_MAPPING_TYPE)
            .targetUrls(UPDATED_TARGET_URLS);

        restAppMessageTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAppMessageTemplate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAppMessageTemplate))
            )
            .andExpect(status().isOk());

        // Validate the AppMessageTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAppMessageTemplateToMatchAllProperties(updatedAppMessageTemplate);
    }

    @Test
    @Transactional
    void putNonExistingAppMessageTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appMessageTemplate.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppMessageTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appMessageTemplate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(appMessageTemplate))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppMessageTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppMessageTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appMessageTemplate.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppMessageTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(appMessageTemplate))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppMessageTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppMessageTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appMessageTemplate.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppMessageTemplateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appMessageTemplate)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppMessageTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAppMessageTemplateWithPatch() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appMessageTemplate using partial update
        AppMessageTemplate partialUpdatedAppMessageTemplate = new AppMessageTemplate();
        partialUpdatedAppMessageTemplate.setId(appMessageTemplate.getId());

        partialUpdatedAppMessageTemplate
            .propTitleMappingJason(UPDATED_PROP_TITLE_MAPPING_JASON)
            .dataSourceMappingType(UPDATED_DATA_SOURCE_MAPPING_TYPE);

        restAppMessageTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppMessageTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAppMessageTemplate))
            )
            .andExpect(status().isOk());

        // Validate the AppMessageTemplate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppMessageTemplateUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAppMessageTemplate, appMessageTemplate),
            getPersistedAppMessageTemplate(appMessageTemplate)
        );
    }

    @Test
    @Transactional
    void fullUpdateAppMessageTemplateWithPatch() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appMessageTemplate using partial update
        AppMessageTemplate partialUpdatedAppMessageTemplate = new AppMessageTemplate();
        partialUpdatedAppMessageTemplate.setId(appMessageTemplate.getId());

        partialUpdatedAppMessageTemplate
            .title(UPDATED_TITLE)
            .topContent(UPDATED_TOP_CONTENT)
            .content(UPDATED_CONTENT)
            .bottomContent(UPDATED_BOTTOM_CONTENT)
            .propTitleMappingJason(UPDATED_PROP_TITLE_MAPPING_JASON)
            .dataSourceMappingType(UPDATED_DATA_SOURCE_MAPPING_TYPE)
            .targetUrls(UPDATED_TARGET_URLS);

        restAppMessageTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppMessageTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAppMessageTemplate))
            )
            .andExpect(status().isOk());

        // Validate the AppMessageTemplate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppMessageTemplateUpdatableFieldsEquals(
            partialUpdatedAppMessageTemplate,
            getPersistedAppMessageTemplate(partialUpdatedAppMessageTemplate)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAppMessageTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appMessageTemplate.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppMessageTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appMessageTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(appMessageTemplate))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppMessageTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppMessageTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appMessageTemplate.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppMessageTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(appMessageTemplate))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppMessageTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppMessageTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appMessageTemplate.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppMessageTemplateMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(appMessageTemplate)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppMessageTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAppMessageTemplate() throws Exception {
        // Initialize the database
        insertedAppMessageTemplate = appMessageTemplateRepository.saveAndFlush(appMessageTemplate);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the appMessageTemplate
        restAppMessageTemplateMockMvc
            .perform(delete(ENTITY_API_URL_ID, appMessageTemplate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return appMessageTemplateRepository.count();
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

    protected AppMessageTemplate getPersistedAppMessageTemplate(AppMessageTemplate appMessageTemplate) {
        return appMessageTemplateRepository.findById(appMessageTemplate.getId()).orElseThrow();
    }

    protected void assertPersistedAppMessageTemplateToMatchAllProperties(AppMessageTemplate expectedAppMessageTemplate) {
        assertAppMessageTemplateAllPropertiesEquals(expectedAppMessageTemplate, getPersistedAppMessageTemplate(expectedAppMessageTemplate));
    }

    protected void assertPersistedAppMessageTemplateToMatchUpdatableProperties(AppMessageTemplate expectedAppMessageTemplate) {
        assertAppMessageTemplateAllUpdatablePropertiesEquals(
            expectedAppMessageTemplate,
            getPersistedAppMessageTemplate(expectedAppMessageTemplate)
        );
    }
}
