package ai.realworld.web.rest;

import static ai.realworld.domain.AllMassageThaiViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AllMassageThaiVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.domain.enumeration.AlphaSourceType;
import ai.realworld.repository.AllMassageThaiViRepository;
import ai.realworld.service.dto.AllMassageThaiViDTO;
import ai.realworld.service.mapper.AllMassageThaiViMapper;
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
 * Integration tests for the {@link AllMassageThaiViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AllMassageThaiViResourceIT {

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

    private static final String ENTITY_API_URL = "/api/all-massage-thai-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AllMassageThaiViRepository allMassageThaiViRepository;

    @Autowired
    private AllMassageThaiViMapper allMassageThaiViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAllMassageThaiViMockMvc;

    private AllMassageThaiVi allMassageThaiVi;

    private AllMassageThaiVi insertedAllMassageThaiVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AllMassageThaiVi createEntity() {
        return new AllMassageThaiVi()
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
    public static AllMassageThaiVi createUpdatedEntity() {
        return new AllMassageThaiVi()
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
        allMassageThaiVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAllMassageThaiVi != null) {
            allMassageThaiViRepository.delete(insertedAllMassageThaiVi);
            insertedAllMassageThaiVi = null;
        }
    }

    @Test
    @Transactional
    void createAllMassageThaiVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AllMassageThaiVi
        AllMassageThaiViDTO allMassageThaiViDTO = allMassageThaiViMapper.toDto(allMassageThaiVi);
        var returnedAllMassageThaiViDTO = om.readValue(
            restAllMassageThaiViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(allMassageThaiViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AllMassageThaiViDTO.class
        );

        // Validate the AllMassageThaiVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAllMassageThaiVi = allMassageThaiViMapper.toEntity(returnedAllMassageThaiViDTO);
        assertAllMassageThaiViUpdatableFieldsEquals(returnedAllMassageThaiVi, getPersistedAllMassageThaiVi(returnedAllMassageThaiVi));

        insertedAllMassageThaiVi = returnedAllMassageThaiVi;
    }

    @Test
    @Transactional
    void createAllMassageThaiViWithExistingId() throws Exception {
        // Create the AllMassageThaiVi with an existing ID
        allMassageThaiVi.setId(1L);
        AllMassageThaiViDTO allMassageThaiViDTO = allMassageThaiViMapper.toDto(allMassageThaiVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAllMassageThaiViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(allMassageThaiViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AllMassageThaiVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        allMassageThaiVi.setTitle(null);

        // Create the AllMassageThaiVi, which fails.
        AllMassageThaiViDTO allMassageThaiViDTO = allMassageThaiViMapper.toDto(allMassageThaiVi);

        restAllMassageThaiViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(allMassageThaiViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAllMassageThaiVis() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        // Get all the allMassageThaiViList
        restAllMassageThaiViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(allMassageThaiVi.getId().intValue())))
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
    void getAllMassageThaiVi() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        // Get the allMassageThaiVi
        restAllMassageThaiViMockMvc
            .perform(get(ENTITY_API_URL_ID, allMassageThaiVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(allMassageThaiVi.getId().intValue()))
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
    void getAllMassageThaiVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        Long id = allMassageThaiVi.getId();

        defaultAllMassageThaiViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAllMassageThaiViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAllMassageThaiViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAllMassageThaiVisByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        // Get all the allMassageThaiViList where title equals to
        defaultAllMassageThaiViFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAllMassageThaiVisByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        // Get all the allMassageThaiViList where title in
        defaultAllMassageThaiViFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAllMassageThaiVisByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        // Get all the allMassageThaiViList where title is not null
        defaultAllMassageThaiViFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllAllMassageThaiVisByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        // Get all the allMassageThaiViList where title contains
        defaultAllMassageThaiViFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAllMassageThaiVisByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        // Get all the allMassageThaiViList where title does not contain
        defaultAllMassageThaiViFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllAllMassageThaiVisByTopContentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        // Get all the allMassageThaiViList where topContent equals to
        defaultAllMassageThaiViFiltering("topContent.equals=" + DEFAULT_TOP_CONTENT, "topContent.equals=" + UPDATED_TOP_CONTENT);
    }

    @Test
    @Transactional
    void getAllAllMassageThaiVisByTopContentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        // Get all the allMassageThaiViList where topContent in
        defaultAllMassageThaiViFiltering(
            "topContent.in=" + DEFAULT_TOP_CONTENT + "," + UPDATED_TOP_CONTENT,
            "topContent.in=" + UPDATED_TOP_CONTENT
        );
    }

    @Test
    @Transactional
    void getAllAllMassageThaiVisByTopContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        // Get all the allMassageThaiViList where topContent is not null
        defaultAllMassageThaiViFiltering("topContent.specified=true", "topContent.specified=false");
    }

    @Test
    @Transactional
    void getAllAllMassageThaiVisByTopContentContainsSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        // Get all the allMassageThaiViList where topContent contains
        defaultAllMassageThaiViFiltering("topContent.contains=" + DEFAULT_TOP_CONTENT, "topContent.contains=" + UPDATED_TOP_CONTENT);
    }

    @Test
    @Transactional
    void getAllAllMassageThaiVisByTopContentNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        // Get all the allMassageThaiViList where topContent does not contain
        defaultAllMassageThaiViFiltering(
            "topContent.doesNotContain=" + UPDATED_TOP_CONTENT,
            "topContent.doesNotContain=" + DEFAULT_TOP_CONTENT
        );
    }

    @Test
    @Transactional
    void getAllAllMassageThaiVisByContentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        // Get all the allMassageThaiViList where content equals to
        defaultAllMassageThaiViFiltering("content.equals=" + DEFAULT_CONTENT, "content.equals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllAllMassageThaiVisByContentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        // Get all the allMassageThaiViList where content in
        defaultAllMassageThaiViFiltering("content.in=" + DEFAULT_CONTENT + "," + UPDATED_CONTENT, "content.in=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllAllMassageThaiVisByContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        // Get all the allMassageThaiViList where content is not null
        defaultAllMassageThaiViFiltering("content.specified=true", "content.specified=false");
    }

    @Test
    @Transactional
    void getAllAllMassageThaiVisByContentContainsSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        // Get all the allMassageThaiViList where content contains
        defaultAllMassageThaiViFiltering("content.contains=" + DEFAULT_CONTENT, "content.contains=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllAllMassageThaiVisByContentNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        // Get all the allMassageThaiViList where content does not contain
        defaultAllMassageThaiViFiltering("content.doesNotContain=" + UPDATED_CONTENT, "content.doesNotContain=" + DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    void getAllAllMassageThaiVisByBottomContentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        // Get all the allMassageThaiViList where bottomContent equals to
        defaultAllMassageThaiViFiltering(
            "bottomContent.equals=" + DEFAULT_BOTTOM_CONTENT,
            "bottomContent.equals=" + UPDATED_BOTTOM_CONTENT
        );
    }

    @Test
    @Transactional
    void getAllAllMassageThaiVisByBottomContentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        // Get all the allMassageThaiViList where bottomContent in
        defaultAllMassageThaiViFiltering(
            "bottomContent.in=" + DEFAULT_BOTTOM_CONTENT + "," + UPDATED_BOTTOM_CONTENT,
            "bottomContent.in=" + UPDATED_BOTTOM_CONTENT
        );
    }

    @Test
    @Transactional
    void getAllAllMassageThaiVisByBottomContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        // Get all the allMassageThaiViList where bottomContent is not null
        defaultAllMassageThaiViFiltering("bottomContent.specified=true", "bottomContent.specified=false");
    }

    @Test
    @Transactional
    void getAllAllMassageThaiVisByBottomContentContainsSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        // Get all the allMassageThaiViList where bottomContent contains
        defaultAllMassageThaiViFiltering(
            "bottomContent.contains=" + DEFAULT_BOTTOM_CONTENT,
            "bottomContent.contains=" + UPDATED_BOTTOM_CONTENT
        );
    }

    @Test
    @Transactional
    void getAllAllMassageThaiVisByBottomContentNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        // Get all the allMassageThaiViList where bottomContent does not contain
        defaultAllMassageThaiViFiltering(
            "bottomContent.doesNotContain=" + UPDATED_BOTTOM_CONTENT,
            "bottomContent.doesNotContain=" + DEFAULT_BOTTOM_CONTENT
        );
    }

    @Test
    @Transactional
    void getAllAllMassageThaiVisByPropTitleMappingJasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        // Get all the allMassageThaiViList where propTitleMappingJason equals to
        defaultAllMassageThaiViFiltering(
            "propTitleMappingJason.equals=" + DEFAULT_PROP_TITLE_MAPPING_JASON,
            "propTitleMappingJason.equals=" + UPDATED_PROP_TITLE_MAPPING_JASON
        );
    }

    @Test
    @Transactional
    void getAllAllMassageThaiVisByPropTitleMappingJasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        // Get all the allMassageThaiViList where propTitleMappingJason in
        defaultAllMassageThaiViFiltering(
            "propTitleMappingJason.in=" + DEFAULT_PROP_TITLE_MAPPING_JASON + "," + UPDATED_PROP_TITLE_MAPPING_JASON,
            "propTitleMappingJason.in=" + UPDATED_PROP_TITLE_MAPPING_JASON
        );
    }

    @Test
    @Transactional
    void getAllAllMassageThaiVisByPropTitleMappingJasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        // Get all the allMassageThaiViList where propTitleMappingJason is not null
        defaultAllMassageThaiViFiltering("propTitleMappingJason.specified=true", "propTitleMappingJason.specified=false");
    }

    @Test
    @Transactional
    void getAllAllMassageThaiVisByPropTitleMappingJasonContainsSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        // Get all the allMassageThaiViList where propTitleMappingJason contains
        defaultAllMassageThaiViFiltering(
            "propTitleMappingJason.contains=" + DEFAULT_PROP_TITLE_MAPPING_JASON,
            "propTitleMappingJason.contains=" + UPDATED_PROP_TITLE_MAPPING_JASON
        );
    }

    @Test
    @Transactional
    void getAllAllMassageThaiVisByPropTitleMappingJasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        // Get all the allMassageThaiViList where propTitleMappingJason does not contain
        defaultAllMassageThaiViFiltering(
            "propTitleMappingJason.doesNotContain=" + UPDATED_PROP_TITLE_MAPPING_JASON,
            "propTitleMappingJason.doesNotContain=" + DEFAULT_PROP_TITLE_MAPPING_JASON
        );
    }

    @Test
    @Transactional
    void getAllAllMassageThaiVisByDataSourceMappingTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        // Get all the allMassageThaiViList where dataSourceMappingType equals to
        defaultAllMassageThaiViFiltering(
            "dataSourceMappingType.equals=" + DEFAULT_DATA_SOURCE_MAPPING_TYPE,
            "dataSourceMappingType.equals=" + UPDATED_DATA_SOURCE_MAPPING_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAllMassageThaiVisByDataSourceMappingTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        // Get all the allMassageThaiViList where dataSourceMappingType in
        defaultAllMassageThaiViFiltering(
            "dataSourceMappingType.in=" + DEFAULT_DATA_SOURCE_MAPPING_TYPE + "," + UPDATED_DATA_SOURCE_MAPPING_TYPE,
            "dataSourceMappingType.in=" + UPDATED_DATA_SOURCE_MAPPING_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAllMassageThaiVisByDataSourceMappingTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        // Get all the allMassageThaiViList where dataSourceMappingType is not null
        defaultAllMassageThaiViFiltering("dataSourceMappingType.specified=true", "dataSourceMappingType.specified=false");
    }

    @Test
    @Transactional
    void getAllAllMassageThaiVisByTargetUrlsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        // Get all the allMassageThaiViList where targetUrls equals to
        defaultAllMassageThaiViFiltering("targetUrls.equals=" + DEFAULT_TARGET_URLS, "targetUrls.equals=" + UPDATED_TARGET_URLS);
    }

    @Test
    @Transactional
    void getAllAllMassageThaiVisByTargetUrlsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        // Get all the allMassageThaiViList where targetUrls in
        defaultAllMassageThaiViFiltering(
            "targetUrls.in=" + DEFAULT_TARGET_URLS + "," + UPDATED_TARGET_URLS,
            "targetUrls.in=" + UPDATED_TARGET_URLS
        );
    }

    @Test
    @Transactional
    void getAllAllMassageThaiVisByTargetUrlsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        // Get all the allMassageThaiViList where targetUrls is not null
        defaultAllMassageThaiViFiltering("targetUrls.specified=true", "targetUrls.specified=false");
    }

    @Test
    @Transactional
    void getAllAllMassageThaiVisByTargetUrlsContainsSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        // Get all the allMassageThaiViList where targetUrls contains
        defaultAllMassageThaiViFiltering("targetUrls.contains=" + DEFAULT_TARGET_URLS, "targetUrls.contains=" + UPDATED_TARGET_URLS);
    }

    @Test
    @Transactional
    void getAllAllMassageThaiVisByTargetUrlsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        // Get all the allMassageThaiViList where targetUrls does not contain
        defaultAllMassageThaiViFiltering(
            "targetUrls.doesNotContain=" + UPDATED_TARGET_URLS,
            "targetUrls.doesNotContain=" + DEFAULT_TARGET_URLS
        );
    }

    @Test
    @Transactional
    void getAllAllMassageThaiVisByThumbnailIsEqualToSomething() throws Exception {
        Metaverse thumbnail;
        if (TestUtil.findAll(em, Metaverse.class).isEmpty()) {
            allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);
            thumbnail = MetaverseResourceIT.createEntity();
        } else {
            thumbnail = TestUtil.findAll(em, Metaverse.class).get(0);
        }
        em.persist(thumbnail);
        em.flush();
        allMassageThaiVi.setThumbnail(thumbnail);
        allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);
        Long thumbnailId = thumbnail.getId();
        // Get all the allMassageThaiViList where thumbnail equals to thumbnailId
        defaultAllMassageThaiViShouldBeFound("thumbnailId.equals=" + thumbnailId);

        // Get all the allMassageThaiViList where thumbnail equals to (thumbnailId + 1)
        defaultAllMassageThaiViShouldNotBeFound("thumbnailId.equals=" + (thumbnailId + 1));
    }

    @Test
    @Transactional
    void getAllAllMassageThaiVisByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        allMassageThaiVi.setApplication(application);
        allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);
        UUID applicationId = application.getId();
        // Get all the allMassageThaiViList where application equals to applicationId
        defaultAllMassageThaiViShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the allMassageThaiViList where application equals to UUID.randomUUID()
        defaultAllMassageThaiViShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAllMassageThaiViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAllMassageThaiViShouldBeFound(shouldBeFound);
        defaultAllMassageThaiViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAllMassageThaiViShouldBeFound(String filter) throws Exception {
        restAllMassageThaiViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(allMassageThaiVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].topContent").value(hasItem(DEFAULT_TOP_CONTENT)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].bottomContent").value(hasItem(DEFAULT_BOTTOM_CONTENT)))
            .andExpect(jsonPath("$.[*].propTitleMappingJason").value(hasItem(DEFAULT_PROP_TITLE_MAPPING_JASON)))
            .andExpect(jsonPath("$.[*].dataSourceMappingType").value(hasItem(DEFAULT_DATA_SOURCE_MAPPING_TYPE.toString())))
            .andExpect(jsonPath("$.[*].targetUrls").value(hasItem(DEFAULT_TARGET_URLS)));

        // Check, that the count call also returns 1
        restAllMassageThaiViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAllMassageThaiViShouldNotBeFound(String filter) throws Exception {
        restAllMassageThaiViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAllMassageThaiViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAllMassageThaiVi() throws Exception {
        // Get the allMassageThaiVi
        restAllMassageThaiViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAllMassageThaiVi() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the allMassageThaiVi
        AllMassageThaiVi updatedAllMassageThaiVi = allMassageThaiViRepository.findById(allMassageThaiVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAllMassageThaiVi are not directly saved in db
        em.detach(updatedAllMassageThaiVi);
        updatedAllMassageThaiVi
            .title(UPDATED_TITLE)
            .topContent(UPDATED_TOP_CONTENT)
            .content(UPDATED_CONTENT)
            .bottomContent(UPDATED_BOTTOM_CONTENT)
            .propTitleMappingJason(UPDATED_PROP_TITLE_MAPPING_JASON)
            .dataSourceMappingType(UPDATED_DATA_SOURCE_MAPPING_TYPE)
            .targetUrls(UPDATED_TARGET_URLS);
        AllMassageThaiViDTO allMassageThaiViDTO = allMassageThaiViMapper.toDto(updatedAllMassageThaiVi);

        restAllMassageThaiViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, allMassageThaiViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(allMassageThaiViDTO))
            )
            .andExpect(status().isOk());

        // Validate the AllMassageThaiVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAllMassageThaiViToMatchAllProperties(updatedAllMassageThaiVi);
    }

    @Test
    @Transactional
    void putNonExistingAllMassageThaiVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        allMassageThaiVi.setId(longCount.incrementAndGet());

        // Create the AllMassageThaiVi
        AllMassageThaiViDTO allMassageThaiViDTO = allMassageThaiViMapper.toDto(allMassageThaiVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAllMassageThaiViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, allMassageThaiViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(allMassageThaiViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AllMassageThaiVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAllMassageThaiVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        allMassageThaiVi.setId(longCount.incrementAndGet());

        // Create the AllMassageThaiVi
        AllMassageThaiViDTO allMassageThaiViDTO = allMassageThaiViMapper.toDto(allMassageThaiVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllMassageThaiViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(allMassageThaiViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AllMassageThaiVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAllMassageThaiVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        allMassageThaiVi.setId(longCount.incrementAndGet());

        // Create the AllMassageThaiVi
        AllMassageThaiViDTO allMassageThaiViDTO = allMassageThaiViMapper.toDto(allMassageThaiVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllMassageThaiViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(allMassageThaiViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AllMassageThaiVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAllMassageThaiViWithPatch() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the allMassageThaiVi using partial update
        AllMassageThaiVi partialUpdatedAllMassageThaiVi = new AllMassageThaiVi();
        partialUpdatedAllMassageThaiVi.setId(allMassageThaiVi.getId());

        partialUpdatedAllMassageThaiVi
            .title(UPDATED_TITLE)
            .bottomContent(UPDATED_BOTTOM_CONTENT)
            .propTitleMappingJason(UPDATED_PROP_TITLE_MAPPING_JASON)
            .dataSourceMappingType(UPDATED_DATA_SOURCE_MAPPING_TYPE)
            .targetUrls(UPDATED_TARGET_URLS);

        restAllMassageThaiViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAllMassageThaiVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAllMassageThaiVi))
            )
            .andExpect(status().isOk());

        // Validate the AllMassageThaiVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAllMassageThaiViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAllMassageThaiVi, allMassageThaiVi),
            getPersistedAllMassageThaiVi(allMassageThaiVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateAllMassageThaiViWithPatch() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the allMassageThaiVi using partial update
        AllMassageThaiVi partialUpdatedAllMassageThaiVi = new AllMassageThaiVi();
        partialUpdatedAllMassageThaiVi.setId(allMassageThaiVi.getId());

        partialUpdatedAllMassageThaiVi
            .title(UPDATED_TITLE)
            .topContent(UPDATED_TOP_CONTENT)
            .content(UPDATED_CONTENT)
            .bottomContent(UPDATED_BOTTOM_CONTENT)
            .propTitleMappingJason(UPDATED_PROP_TITLE_MAPPING_JASON)
            .dataSourceMappingType(UPDATED_DATA_SOURCE_MAPPING_TYPE)
            .targetUrls(UPDATED_TARGET_URLS);

        restAllMassageThaiViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAllMassageThaiVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAllMassageThaiVi))
            )
            .andExpect(status().isOk());

        // Validate the AllMassageThaiVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAllMassageThaiViUpdatableFieldsEquals(
            partialUpdatedAllMassageThaiVi,
            getPersistedAllMassageThaiVi(partialUpdatedAllMassageThaiVi)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAllMassageThaiVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        allMassageThaiVi.setId(longCount.incrementAndGet());

        // Create the AllMassageThaiVi
        AllMassageThaiViDTO allMassageThaiViDTO = allMassageThaiViMapper.toDto(allMassageThaiVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAllMassageThaiViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, allMassageThaiViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(allMassageThaiViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AllMassageThaiVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAllMassageThaiVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        allMassageThaiVi.setId(longCount.incrementAndGet());

        // Create the AllMassageThaiVi
        AllMassageThaiViDTO allMassageThaiViDTO = allMassageThaiViMapper.toDto(allMassageThaiVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllMassageThaiViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(allMassageThaiViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AllMassageThaiVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAllMassageThaiVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        allMassageThaiVi.setId(longCount.incrementAndGet());

        // Create the AllMassageThaiVi
        AllMassageThaiViDTO allMassageThaiViDTO = allMassageThaiViMapper.toDto(allMassageThaiVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllMassageThaiViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(allMassageThaiViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AllMassageThaiVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAllMassageThaiVi() throws Exception {
        // Initialize the database
        insertedAllMassageThaiVi = allMassageThaiViRepository.saveAndFlush(allMassageThaiVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the allMassageThaiVi
        restAllMassageThaiViMockMvc
            .perform(delete(ENTITY_API_URL_ID, allMassageThaiVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return allMassageThaiViRepository.count();
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

    protected AllMassageThaiVi getPersistedAllMassageThaiVi(AllMassageThaiVi allMassageThaiVi) {
        return allMassageThaiViRepository.findById(allMassageThaiVi.getId()).orElseThrow();
    }

    protected void assertPersistedAllMassageThaiViToMatchAllProperties(AllMassageThaiVi expectedAllMassageThaiVi) {
        assertAllMassageThaiViAllPropertiesEquals(expectedAllMassageThaiVi, getPersistedAllMassageThaiVi(expectedAllMassageThaiVi));
    }

    protected void assertPersistedAllMassageThaiViToMatchUpdatableProperties(AllMassageThaiVi expectedAllMassageThaiVi) {
        assertAllMassageThaiViAllUpdatablePropertiesEquals(
            expectedAllMassageThaiVi,
            getPersistedAllMassageThaiVi(expectedAllMassageThaiVi)
        );
    }
}
