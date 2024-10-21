package ai.realworld.web.rest;

import static ai.realworld.domain.AlZorroTemptationAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlZorroTemptation;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.domain.enumeration.AlphaSourceType;
import ai.realworld.domain.enumeration.KnsIction;
import ai.realworld.repository.AlZorroTemptationRepository;
import ai.realworld.service.dto.AlZorroTemptationDTO;
import ai.realworld.service.mapper.AlZorroTemptationMapper;
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
 * Integration tests for the {@link AlZorroTemptationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlZorroTemptationResourceIT {

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

    private static final String ENTITY_API_URL = "/api/al-zorro-temptations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlZorroTemptationRepository alZorroTemptationRepository;

    @Autowired
    private AlZorroTemptationMapper alZorroTemptationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlZorroTemptationMockMvc;

    private AlZorroTemptation alZorroTemptation;

    private AlZorroTemptation insertedAlZorroTemptation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlZorroTemptation createEntity() {
        return new AlZorroTemptation()
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
    public static AlZorroTemptation createUpdatedEntity() {
        return new AlZorroTemptation()
            .zipAction(UPDATED_ZIP_ACTION)
            .name(UPDATED_NAME)
            .templateId(UPDATED_TEMPLATE_ID)
            .dataSourceMappingType(UPDATED_DATA_SOURCE_MAPPING_TYPE)
            .templateDataMapping(UPDATED_TEMPLATE_DATA_MAPPING)
            .targetUrls(UPDATED_TARGET_URLS);
    }

    @BeforeEach
    public void initTest() {
        alZorroTemptation = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlZorroTemptation != null) {
            alZorroTemptationRepository.delete(insertedAlZorroTemptation);
            insertedAlZorroTemptation = null;
        }
    }

    @Test
    @Transactional
    void createAlZorroTemptation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlZorroTemptation
        AlZorroTemptationDTO alZorroTemptationDTO = alZorroTemptationMapper.toDto(alZorroTemptation);
        var returnedAlZorroTemptationDTO = om.readValue(
            restAlZorroTemptationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alZorroTemptationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlZorroTemptationDTO.class
        );

        // Validate the AlZorroTemptation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlZorroTemptation = alZorroTemptationMapper.toEntity(returnedAlZorroTemptationDTO);
        assertAlZorroTemptationUpdatableFieldsEquals(returnedAlZorroTemptation, getPersistedAlZorroTemptation(returnedAlZorroTemptation));

        insertedAlZorroTemptation = returnedAlZorroTemptation;
    }

    @Test
    @Transactional
    void createAlZorroTemptationWithExistingId() throws Exception {
        // Create the AlZorroTemptation with an existing ID
        alZorroTemptation.setId(1L);
        AlZorroTemptationDTO alZorroTemptationDTO = alZorroTemptationMapper.toDto(alZorroTemptation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlZorroTemptationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alZorroTemptationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlZorroTemptation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alZorroTemptation.setName(null);

        // Create the AlZorroTemptation, which fails.
        AlZorroTemptationDTO alZorroTemptationDTO = alZorroTemptationMapper.toDto(alZorroTemptation);

        restAlZorroTemptationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alZorroTemptationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlZorroTemptations() throws Exception {
        // Initialize the database
        insertedAlZorroTemptation = alZorroTemptationRepository.saveAndFlush(alZorroTemptation);

        // Get all the alZorroTemptationList
        restAlZorroTemptationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alZorroTemptation.getId().intValue())))
            .andExpect(jsonPath("$.[*].zipAction").value(hasItem(DEFAULT_ZIP_ACTION.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].templateId").value(hasItem(DEFAULT_TEMPLATE_ID)))
            .andExpect(jsonPath("$.[*].dataSourceMappingType").value(hasItem(DEFAULT_DATA_SOURCE_MAPPING_TYPE.toString())))
            .andExpect(jsonPath("$.[*].templateDataMapping").value(hasItem(DEFAULT_TEMPLATE_DATA_MAPPING)))
            .andExpect(jsonPath("$.[*].targetUrls").value(hasItem(DEFAULT_TARGET_URLS)));
    }

    @Test
    @Transactional
    void getAlZorroTemptation() throws Exception {
        // Initialize the database
        insertedAlZorroTemptation = alZorroTemptationRepository.saveAndFlush(alZorroTemptation);

        // Get the alZorroTemptation
        restAlZorroTemptationMockMvc
            .perform(get(ENTITY_API_URL_ID, alZorroTemptation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alZorroTemptation.getId().intValue()))
            .andExpect(jsonPath("$.zipAction").value(DEFAULT_ZIP_ACTION.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.templateId").value(DEFAULT_TEMPLATE_ID))
            .andExpect(jsonPath("$.dataSourceMappingType").value(DEFAULT_DATA_SOURCE_MAPPING_TYPE.toString()))
            .andExpect(jsonPath("$.templateDataMapping").value(DEFAULT_TEMPLATE_DATA_MAPPING))
            .andExpect(jsonPath("$.targetUrls").value(DEFAULT_TARGET_URLS));
    }

    @Test
    @Transactional
    void getAlZorroTemptationsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlZorroTemptation = alZorroTemptationRepository.saveAndFlush(alZorroTemptation);

        Long id = alZorroTemptation.getId();

        defaultAlZorroTemptationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlZorroTemptationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlZorroTemptationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationsByZipActionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlZorroTemptation = alZorroTemptationRepository.saveAndFlush(alZorroTemptation);

        // Get all the alZorroTemptationList where zipAction equals to
        defaultAlZorroTemptationFiltering("zipAction.equals=" + DEFAULT_ZIP_ACTION, "zipAction.equals=" + UPDATED_ZIP_ACTION);
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationsByZipActionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlZorroTemptation = alZorroTemptationRepository.saveAndFlush(alZorroTemptation);

        // Get all the alZorroTemptationList where zipAction in
        defaultAlZorroTemptationFiltering(
            "zipAction.in=" + DEFAULT_ZIP_ACTION + "," + UPDATED_ZIP_ACTION,
            "zipAction.in=" + UPDATED_ZIP_ACTION
        );
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationsByZipActionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlZorroTemptation = alZorroTemptationRepository.saveAndFlush(alZorroTemptation);

        // Get all the alZorroTemptationList where zipAction is not null
        defaultAlZorroTemptationFiltering("zipAction.specified=true", "zipAction.specified=false");
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlZorroTemptation = alZorroTemptationRepository.saveAndFlush(alZorroTemptation);

        // Get all the alZorroTemptationList where name equals to
        defaultAlZorroTemptationFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlZorroTemptation = alZorroTemptationRepository.saveAndFlush(alZorroTemptation);

        // Get all the alZorroTemptationList where name in
        defaultAlZorroTemptationFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlZorroTemptation = alZorroTemptationRepository.saveAndFlush(alZorroTemptation);

        // Get all the alZorroTemptationList where name is not null
        defaultAlZorroTemptationFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlZorroTemptation = alZorroTemptationRepository.saveAndFlush(alZorroTemptation);

        // Get all the alZorroTemptationList where name contains
        defaultAlZorroTemptationFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlZorroTemptation = alZorroTemptationRepository.saveAndFlush(alZorroTemptation);

        // Get all the alZorroTemptationList where name does not contain
        defaultAlZorroTemptationFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationsByTemplateIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlZorroTemptation = alZorroTemptationRepository.saveAndFlush(alZorroTemptation);

        // Get all the alZorroTemptationList where templateId equals to
        defaultAlZorroTemptationFiltering("templateId.equals=" + DEFAULT_TEMPLATE_ID, "templateId.equals=" + UPDATED_TEMPLATE_ID);
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationsByTemplateIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlZorroTemptation = alZorroTemptationRepository.saveAndFlush(alZorroTemptation);

        // Get all the alZorroTemptationList where templateId in
        defaultAlZorroTemptationFiltering(
            "templateId.in=" + DEFAULT_TEMPLATE_ID + "," + UPDATED_TEMPLATE_ID,
            "templateId.in=" + UPDATED_TEMPLATE_ID
        );
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationsByTemplateIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlZorroTemptation = alZorroTemptationRepository.saveAndFlush(alZorroTemptation);

        // Get all the alZorroTemptationList where templateId is not null
        defaultAlZorroTemptationFiltering("templateId.specified=true", "templateId.specified=false");
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationsByTemplateIdContainsSomething() throws Exception {
        // Initialize the database
        insertedAlZorroTemptation = alZorroTemptationRepository.saveAndFlush(alZorroTemptation);

        // Get all the alZorroTemptationList where templateId contains
        defaultAlZorroTemptationFiltering("templateId.contains=" + DEFAULT_TEMPLATE_ID, "templateId.contains=" + UPDATED_TEMPLATE_ID);
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationsByTemplateIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlZorroTemptation = alZorroTemptationRepository.saveAndFlush(alZorroTemptation);

        // Get all the alZorroTemptationList where templateId does not contain
        defaultAlZorroTemptationFiltering(
            "templateId.doesNotContain=" + UPDATED_TEMPLATE_ID,
            "templateId.doesNotContain=" + DEFAULT_TEMPLATE_ID
        );
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationsByDataSourceMappingTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlZorroTemptation = alZorroTemptationRepository.saveAndFlush(alZorroTemptation);

        // Get all the alZorroTemptationList where dataSourceMappingType equals to
        defaultAlZorroTemptationFiltering(
            "dataSourceMappingType.equals=" + DEFAULT_DATA_SOURCE_MAPPING_TYPE,
            "dataSourceMappingType.equals=" + UPDATED_DATA_SOURCE_MAPPING_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationsByDataSourceMappingTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlZorroTemptation = alZorroTemptationRepository.saveAndFlush(alZorroTemptation);

        // Get all the alZorroTemptationList where dataSourceMappingType in
        defaultAlZorroTemptationFiltering(
            "dataSourceMappingType.in=" + DEFAULT_DATA_SOURCE_MAPPING_TYPE + "," + UPDATED_DATA_SOURCE_MAPPING_TYPE,
            "dataSourceMappingType.in=" + UPDATED_DATA_SOURCE_MAPPING_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationsByDataSourceMappingTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlZorroTemptation = alZorroTemptationRepository.saveAndFlush(alZorroTemptation);

        // Get all the alZorroTemptationList where dataSourceMappingType is not null
        defaultAlZorroTemptationFiltering("dataSourceMappingType.specified=true", "dataSourceMappingType.specified=false");
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationsByTemplateDataMappingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlZorroTemptation = alZorroTemptationRepository.saveAndFlush(alZorroTemptation);

        // Get all the alZorroTemptationList where templateDataMapping equals to
        defaultAlZorroTemptationFiltering(
            "templateDataMapping.equals=" + DEFAULT_TEMPLATE_DATA_MAPPING,
            "templateDataMapping.equals=" + UPDATED_TEMPLATE_DATA_MAPPING
        );
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationsByTemplateDataMappingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlZorroTemptation = alZorroTemptationRepository.saveAndFlush(alZorroTemptation);

        // Get all the alZorroTemptationList where templateDataMapping in
        defaultAlZorroTemptationFiltering(
            "templateDataMapping.in=" + DEFAULT_TEMPLATE_DATA_MAPPING + "," + UPDATED_TEMPLATE_DATA_MAPPING,
            "templateDataMapping.in=" + UPDATED_TEMPLATE_DATA_MAPPING
        );
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationsByTemplateDataMappingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlZorroTemptation = alZorroTemptationRepository.saveAndFlush(alZorroTemptation);

        // Get all the alZorroTemptationList where templateDataMapping is not null
        defaultAlZorroTemptationFiltering("templateDataMapping.specified=true", "templateDataMapping.specified=false");
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationsByTemplateDataMappingContainsSomething() throws Exception {
        // Initialize the database
        insertedAlZorroTemptation = alZorroTemptationRepository.saveAndFlush(alZorroTemptation);

        // Get all the alZorroTemptationList where templateDataMapping contains
        defaultAlZorroTemptationFiltering(
            "templateDataMapping.contains=" + DEFAULT_TEMPLATE_DATA_MAPPING,
            "templateDataMapping.contains=" + UPDATED_TEMPLATE_DATA_MAPPING
        );
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationsByTemplateDataMappingNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlZorroTemptation = alZorroTemptationRepository.saveAndFlush(alZorroTemptation);

        // Get all the alZorroTemptationList where templateDataMapping does not contain
        defaultAlZorroTemptationFiltering(
            "templateDataMapping.doesNotContain=" + UPDATED_TEMPLATE_DATA_MAPPING,
            "templateDataMapping.doesNotContain=" + DEFAULT_TEMPLATE_DATA_MAPPING
        );
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationsByTargetUrlsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlZorroTemptation = alZorroTemptationRepository.saveAndFlush(alZorroTemptation);

        // Get all the alZorroTemptationList where targetUrls equals to
        defaultAlZorroTemptationFiltering("targetUrls.equals=" + DEFAULT_TARGET_URLS, "targetUrls.equals=" + UPDATED_TARGET_URLS);
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationsByTargetUrlsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlZorroTemptation = alZorroTemptationRepository.saveAndFlush(alZorroTemptation);

        // Get all the alZorroTemptationList where targetUrls in
        defaultAlZorroTemptationFiltering(
            "targetUrls.in=" + DEFAULT_TARGET_URLS + "," + UPDATED_TARGET_URLS,
            "targetUrls.in=" + UPDATED_TARGET_URLS
        );
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationsByTargetUrlsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlZorroTemptation = alZorroTemptationRepository.saveAndFlush(alZorroTemptation);

        // Get all the alZorroTemptationList where targetUrls is not null
        defaultAlZorroTemptationFiltering("targetUrls.specified=true", "targetUrls.specified=false");
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationsByTargetUrlsContainsSomething() throws Exception {
        // Initialize the database
        insertedAlZorroTemptation = alZorroTemptationRepository.saveAndFlush(alZorroTemptation);

        // Get all the alZorroTemptationList where targetUrls contains
        defaultAlZorroTemptationFiltering("targetUrls.contains=" + DEFAULT_TARGET_URLS, "targetUrls.contains=" + UPDATED_TARGET_URLS);
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationsByTargetUrlsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlZorroTemptation = alZorroTemptationRepository.saveAndFlush(alZorroTemptation);

        // Get all the alZorroTemptationList where targetUrls does not contain
        defaultAlZorroTemptationFiltering(
            "targetUrls.doesNotContain=" + UPDATED_TARGET_URLS,
            "targetUrls.doesNotContain=" + DEFAULT_TARGET_URLS
        );
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationsByThumbnailIsEqualToSomething() throws Exception {
        Metaverse thumbnail;
        if (TestUtil.findAll(em, Metaverse.class).isEmpty()) {
            alZorroTemptationRepository.saveAndFlush(alZorroTemptation);
            thumbnail = MetaverseResourceIT.createEntity();
        } else {
            thumbnail = TestUtil.findAll(em, Metaverse.class).get(0);
        }
        em.persist(thumbnail);
        em.flush();
        alZorroTemptation.setThumbnail(thumbnail);
        alZorroTemptationRepository.saveAndFlush(alZorroTemptation);
        Long thumbnailId = thumbnail.getId();
        // Get all the alZorroTemptationList where thumbnail equals to thumbnailId
        defaultAlZorroTemptationShouldBeFound("thumbnailId.equals=" + thumbnailId);

        // Get all the alZorroTemptationList where thumbnail equals to (thumbnailId + 1)
        defaultAlZorroTemptationShouldNotBeFound("thumbnailId.equals=" + (thumbnailId + 1));
    }

    @Test
    @Transactional
    void getAllAlZorroTemptationsByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alZorroTemptationRepository.saveAndFlush(alZorroTemptation);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alZorroTemptation.setApplication(application);
        alZorroTemptationRepository.saveAndFlush(alZorroTemptation);
        UUID applicationId = application.getId();
        // Get all the alZorroTemptationList where application equals to applicationId
        defaultAlZorroTemptationShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alZorroTemptationList where application equals to UUID.randomUUID()
        defaultAlZorroTemptationShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlZorroTemptationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlZorroTemptationShouldBeFound(shouldBeFound);
        defaultAlZorroTemptationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlZorroTemptationShouldBeFound(String filter) throws Exception {
        restAlZorroTemptationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alZorroTemptation.getId().intValue())))
            .andExpect(jsonPath("$.[*].zipAction").value(hasItem(DEFAULT_ZIP_ACTION.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].templateId").value(hasItem(DEFAULT_TEMPLATE_ID)))
            .andExpect(jsonPath("$.[*].dataSourceMappingType").value(hasItem(DEFAULT_DATA_SOURCE_MAPPING_TYPE.toString())))
            .andExpect(jsonPath("$.[*].templateDataMapping").value(hasItem(DEFAULT_TEMPLATE_DATA_MAPPING)))
            .andExpect(jsonPath("$.[*].targetUrls").value(hasItem(DEFAULT_TARGET_URLS)));

        // Check, that the count call also returns 1
        restAlZorroTemptationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlZorroTemptationShouldNotBeFound(String filter) throws Exception {
        restAlZorroTemptationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlZorroTemptationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlZorroTemptation() throws Exception {
        // Get the alZorroTemptation
        restAlZorroTemptationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlZorroTemptation() throws Exception {
        // Initialize the database
        insertedAlZorroTemptation = alZorroTemptationRepository.saveAndFlush(alZorroTemptation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alZorroTemptation
        AlZorroTemptation updatedAlZorroTemptation = alZorroTemptationRepository.findById(alZorroTemptation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlZorroTemptation are not directly saved in db
        em.detach(updatedAlZorroTemptation);
        updatedAlZorroTemptation
            .zipAction(UPDATED_ZIP_ACTION)
            .name(UPDATED_NAME)
            .templateId(UPDATED_TEMPLATE_ID)
            .dataSourceMappingType(UPDATED_DATA_SOURCE_MAPPING_TYPE)
            .templateDataMapping(UPDATED_TEMPLATE_DATA_MAPPING)
            .targetUrls(UPDATED_TARGET_URLS);
        AlZorroTemptationDTO alZorroTemptationDTO = alZorroTemptationMapper.toDto(updatedAlZorroTemptation);

        restAlZorroTemptationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alZorroTemptationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alZorroTemptationDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlZorroTemptation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlZorroTemptationToMatchAllProperties(updatedAlZorroTemptation);
    }

    @Test
    @Transactional
    void putNonExistingAlZorroTemptation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alZorroTemptation.setId(longCount.incrementAndGet());

        // Create the AlZorroTemptation
        AlZorroTemptationDTO alZorroTemptationDTO = alZorroTemptationMapper.toDto(alZorroTemptation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlZorroTemptationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alZorroTemptationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alZorroTemptationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlZorroTemptation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlZorroTemptation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alZorroTemptation.setId(longCount.incrementAndGet());

        // Create the AlZorroTemptation
        AlZorroTemptationDTO alZorroTemptationDTO = alZorroTemptationMapper.toDto(alZorroTemptation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlZorroTemptationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alZorroTemptationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlZorroTemptation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlZorroTemptation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alZorroTemptation.setId(longCount.incrementAndGet());

        // Create the AlZorroTemptation
        AlZorroTemptationDTO alZorroTemptationDTO = alZorroTemptationMapper.toDto(alZorroTemptation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlZorroTemptationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alZorroTemptationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlZorroTemptation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlZorroTemptationWithPatch() throws Exception {
        // Initialize the database
        insertedAlZorroTemptation = alZorroTemptationRepository.saveAndFlush(alZorroTemptation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alZorroTemptation using partial update
        AlZorroTemptation partialUpdatedAlZorroTemptation = new AlZorroTemptation();
        partialUpdatedAlZorroTemptation.setId(alZorroTemptation.getId());

        partialUpdatedAlZorroTemptation
            .name(UPDATED_NAME)
            .templateId(UPDATED_TEMPLATE_ID)
            .templateDataMapping(UPDATED_TEMPLATE_DATA_MAPPING)
            .targetUrls(UPDATED_TARGET_URLS);

        restAlZorroTemptationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlZorroTemptation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlZorroTemptation))
            )
            .andExpect(status().isOk());

        // Validate the AlZorroTemptation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlZorroTemptationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlZorroTemptation, alZorroTemptation),
            getPersistedAlZorroTemptation(alZorroTemptation)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlZorroTemptationWithPatch() throws Exception {
        // Initialize the database
        insertedAlZorroTemptation = alZorroTemptationRepository.saveAndFlush(alZorroTemptation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alZorroTemptation using partial update
        AlZorroTemptation partialUpdatedAlZorroTemptation = new AlZorroTemptation();
        partialUpdatedAlZorroTemptation.setId(alZorroTemptation.getId());

        partialUpdatedAlZorroTemptation
            .zipAction(UPDATED_ZIP_ACTION)
            .name(UPDATED_NAME)
            .templateId(UPDATED_TEMPLATE_ID)
            .dataSourceMappingType(UPDATED_DATA_SOURCE_MAPPING_TYPE)
            .templateDataMapping(UPDATED_TEMPLATE_DATA_MAPPING)
            .targetUrls(UPDATED_TARGET_URLS);

        restAlZorroTemptationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlZorroTemptation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlZorroTemptation))
            )
            .andExpect(status().isOk());

        // Validate the AlZorroTemptation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlZorroTemptationUpdatableFieldsEquals(
            partialUpdatedAlZorroTemptation,
            getPersistedAlZorroTemptation(partialUpdatedAlZorroTemptation)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAlZorroTemptation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alZorroTemptation.setId(longCount.incrementAndGet());

        // Create the AlZorroTemptation
        AlZorroTemptationDTO alZorroTemptationDTO = alZorroTemptationMapper.toDto(alZorroTemptation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlZorroTemptationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alZorroTemptationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alZorroTemptationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlZorroTemptation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlZorroTemptation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alZorroTemptation.setId(longCount.incrementAndGet());

        // Create the AlZorroTemptation
        AlZorroTemptationDTO alZorroTemptationDTO = alZorroTemptationMapper.toDto(alZorroTemptation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlZorroTemptationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alZorroTemptationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlZorroTemptation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlZorroTemptation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alZorroTemptation.setId(longCount.incrementAndGet());

        // Create the AlZorroTemptation
        AlZorroTemptationDTO alZorroTemptationDTO = alZorroTemptationMapper.toDto(alZorroTemptation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlZorroTemptationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alZorroTemptationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlZorroTemptation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlZorroTemptation() throws Exception {
        // Initialize the database
        insertedAlZorroTemptation = alZorroTemptationRepository.saveAndFlush(alZorroTemptation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alZorroTemptation
        restAlZorroTemptationMockMvc
            .perform(delete(ENTITY_API_URL_ID, alZorroTemptation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alZorroTemptationRepository.count();
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

    protected AlZorroTemptation getPersistedAlZorroTemptation(AlZorroTemptation alZorroTemptation) {
        return alZorroTemptationRepository.findById(alZorroTemptation.getId()).orElseThrow();
    }

    protected void assertPersistedAlZorroTemptationToMatchAllProperties(AlZorroTemptation expectedAlZorroTemptation) {
        assertAlZorroTemptationAllPropertiesEquals(expectedAlZorroTemptation, getPersistedAlZorroTemptation(expectedAlZorroTemptation));
    }

    protected void assertPersistedAlZorroTemptationToMatchUpdatableProperties(AlZorroTemptation expectedAlZorroTemptation) {
        assertAlZorroTemptationAllUpdatablePropertiesEquals(
            expectedAlZorroTemptation,
            getPersistedAlZorroTemptation(expectedAlZorroTemptation)
        );
    }
}
