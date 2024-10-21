package ai.realworld.web.rest;

import static ai.realworld.domain.AllMassageThaiAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AllMassageThai;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.domain.enumeration.AlphaSourceType;
import ai.realworld.repository.AllMassageThaiRepository;
import ai.realworld.service.dto.AllMassageThaiDTO;
import ai.realworld.service.mapper.AllMassageThaiMapper;
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
 * Integration tests for the {@link AllMassageThaiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AllMassageThaiResourceIT {

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

    private static final String ENTITY_API_URL = "/api/all-massage-thais";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AllMassageThaiRepository allMassageThaiRepository;

    @Autowired
    private AllMassageThaiMapper allMassageThaiMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAllMassageThaiMockMvc;

    private AllMassageThai allMassageThai;

    private AllMassageThai insertedAllMassageThai;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AllMassageThai createEntity() {
        return new AllMassageThai()
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
    public static AllMassageThai createUpdatedEntity() {
        return new AllMassageThai()
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
        allMassageThai = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAllMassageThai != null) {
            allMassageThaiRepository.delete(insertedAllMassageThai);
            insertedAllMassageThai = null;
        }
    }

    @Test
    @Transactional
    void createAllMassageThai() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AllMassageThai
        AllMassageThaiDTO allMassageThaiDTO = allMassageThaiMapper.toDto(allMassageThai);
        var returnedAllMassageThaiDTO = om.readValue(
            restAllMassageThaiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(allMassageThaiDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AllMassageThaiDTO.class
        );

        // Validate the AllMassageThai in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAllMassageThai = allMassageThaiMapper.toEntity(returnedAllMassageThaiDTO);
        assertAllMassageThaiUpdatableFieldsEquals(returnedAllMassageThai, getPersistedAllMassageThai(returnedAllMassageThai));

        insertedAllMassageThai = returnedAllMassageThai;
    }

    @Test
    @Transactional
    void createAllMassageThaiWithExistingId() throws Exception {
        // Create the AllMassageThai with an existing ID
        allMassageThai.setId(1L);
        AllMassageThaiDTO allMassageThaiDTO = allMassageThaiMapper.toDto(allMassageThai);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAllMassageThaiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(allMassageThaiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AllMassageThai in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        allMassageThai.setTitle(null);

        // Create the AllMassageThai, which fails.
        AllMassageThaiDTO allMassageThaiDTO = allMassageThaiMapper.toDto(allMassageThai);

        restAllMassageThaiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(allMassageThaiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAllMassageThais() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        // Get all the allMassageThaiList
        restAllMassageThaiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(allMassageThai.getId().intValue())))
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
    void getAllMassageThai() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        // Get the allMassageThai
        restAllMassageThaiMockMvc
            .perform(get(ENTITY_API_URL_ID, allMassageThai.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(allMassageThai.getId().intValue()))
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
    void getAllMassageThaisByIdFiltering() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        Long id = allMassageThai.getId();

        defaultAllMassageThaiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAllMassageThaiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAllMassageThaiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAllMassageThaisByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        // Get all the allMassageThaiList where title equals to
        defaultAllMassageThaiFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAllMassageThaisByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        // Get all the allMassageThaiList where title in
        defaultAllMassageThaiFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAllMassageThaisByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        // Get all the allMassageThaiList where title is not null
        defaultAllMassageThaiFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllAllMassageThaisByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        // Get all the allMassageThaiList where title contains
        defaultAllMassageThaiFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAllMassageThaisByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        // Get all the allMassageThaiList where title does not contain
        defaultAllMassageThaiFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllAllMassageThaisByTopContentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        // Get all the allMassageThaiList where topContent equals to
        defaultAllMassageThaiFiltering("topContent.equals=" + DEFAULT_TOP_CONTENT, "topContent.equals=" + UPDATED_TOP_CONTENT);
    }

    @Test
    @Transactional
    void getAllAllMassageThaisByTopContentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        // Get all the allMassageThaiList where topContent in
        defaultAllMassageThaiFiltering(
            "topContent.in=" + DEFAULT_TOP_CONTENT + "," + UPDATED_TOP_CONTENT,
            "topContent.in=" + UPDATED_TOP_CONTENT
        );
    }

    @Test
    @Transactional
    void getAllAllMassageThaisByTopContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        // Get all the allMassageThaiList where topContent is not null
        defaultAllMassageThaiFiltering("topContent.specified=true", "topContent.specified=false");
    }

    @Test
    @Transactional
    void getAllAllMassageThaisByTopContentContainsSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        // Get all the allMassageThaiList where topContent contains
        defaultAllMassageThaiFiltering("topContent.contains=" + DEFAULT_TOP_CONTENT, "topContent.contains=" + UPDATED_TOP_CONTENT);
    }

    @Test
    @Transactional
    void getAllAllMassageThaisByTopContentNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        // Get all the allMassageThaiList where topContent does not contain
        defaultAllMassageThaiFiltering(
            "topContent.doesNotContain=" + UPDATED_TOP_CONTENT,
            "topContent.doesNotContain=" + DEFAULT_TOP_CONTENT
        );
    }

    @Test
    @Transactional
    void getAllAllMassageThaisByContentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        // Get all the allMassageThaiList where content equals to
        defaultAllMassageThaiFiltering("content.equals=" + DEFAULT_CONTENT, "content.equals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllAllMassageThaisByContentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        // Get all the allMassageThaiList where content in
        defaultAllMassageThaiFiltering("content.in=" + DEFAULT_CONTENT + "," + UPDATED_CONTENT, "content.in=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllAllMassageThaisByContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        // Get all the allMassageThaiList where content is not null
        defaultAllMassageThaiFiltering("content.specified=true", "content.specified=false");
    }

    @Test
    @Transactional
    void getAllAllMassageThaisByContentContainsSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        // Get all the allMassageThaiList where content contains
        defaultAllMassageThaiFiltering("content.contains=" + DEFAULT_CONTENT, "content.contains=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllAllMassageThaisByContentNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        // Get all the allMassageThaiList where content does not contain
        defaultAllMassageThaiFiltering("content.doesNotContain=" + UPDATED_CONTENT, "content.doesNotContain=" + DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    void getAllAllMassageThaisByBottomContentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        // Get all the allMassageThaiList where bottomContent equals to
        defaultAllMassageThaiFiltering("bottomContent.equals=" + DEFAULT_BOTTOM_CONTENT, "bottomContent.equals=" + UPDATED_BOTTOM_CONTENT);
    }

    @Test
    @Transactional
    void getAllAllMassageThaisByBottomContentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        // Get all the allMassageThaiList where bottomContent in
        defaultAllMassageThaiFiltering(
            "bottomContent.in=" + DEFAULT_BOTTOM_CONTENT + "," + UPDATED_BOTTOM_CONTENT,
            "bottomContent.in=" + UPDATED_BOTTOM_CONTENT
        );
    }

    @Test
    @Transactional
    void getAllAllMassageThaisByBottomContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        // Get all the allMassageThaiList where bottomContent is not null
        defaultAllMassageThaiFiltering("bottomContent.specified=true", "bottomContent.specified=false");
    }

    @Test
    @Transactional
    void getAllAllMassageThaisByBottomContentContainsSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        // Get all the allMassageThaiList where bottomContent contains
        defaultAllMassageThaiFiltering(
            "bottomContent.contains=" + DEFAULT_BOTTOM_CONTENT,
            "bottomContent.contains=" + UPDATED_BOTTOM_CONTENT
        );
    }

    @Test
    @Transactional
    void getAllAllMassageThaisByBottomContentNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        // Get all the allMassageThaiList where bottomContent does not contain
        defaultAllMassageThaiFiltering(
            "bottomContent.doesNotContain=" + UPDATED_BOTTOM_CONTENT,
            "bottomContent.doesNotContain=" + DEFAULT_BOTTOM_CONTENT
        );
    }

    @Test
    @Transactional
    void getAllAllMassageThaisByPropTitleMappingJasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        // Get all the allMassageThaiList where propTitleMappingJason equals to
        defaultAllMassageThaiFiltering(
            "propTitleMappingJason.equals=" + DEFAULT_PROP_TITLE_MAPPING_JASON,
            "propTitleMappingJason.equals=" + UPDATED_PROP_TITLE_MAPPING_JASON
        );
    }

    @Test
    @Transactional
    void getAllAllMassageThaisByPropTitleMappingJasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        // Get all the allMassageThaiList where propTitleMappingJason in
        defaultAllMassageThaiFiltering(
            "propTitleMappingJason.in=" + DEFAULT_PROP_TITLE_MAPPING_JASON + "," + UPDATED_PROP_TITLE_MAPPING_JASON,
            "propTitleMappingJason.in=" + UPDATED_PROP_TITLE_MAPPING_JASON
        );
    }

    @Test
    @Transactional
    void getAllAllMassageThaisByPropTitleMappingJasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        // Get all the allMassageThaiList where propTitleMappingJason is not null
        defaultAllMassageThaiFiltering("propTitleMappingJason.specified=true", "propTitleMappingJason.specified=false");
    }

    @Test
    @Transactional
    void getAllAllMassageThaisByPropTitleMappingJasonContainsSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        // Get all the allMassageThaiList where propTitleMappingJason contains
        defaultAllMassageThaiFiltering(
            "propTitleMappingJason.contains=" + DEFAULT_PROP_TITLE_MAPPING_JASON,
            "propTitleMappingJason.contains=" + UPDATED_PROP_TITLE_MAPPING_JASON
        );
    }

    @Test
    @Transactional
    void getAllAllMassageThaisByPropTitleMappingJasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        // Get all the allMassageThaiList where propTitleMappingJason does not contain
        defaultAllMassageThaiFiltering(
            "propTitleMappingJason.doesNotContain=" + UPDATED_PROP_TITLE_MAPPING_JASON,
            "propTitleMappingJason.doesNotContain=" + DEFAULT_PROP_TITLE_MAPPING_JASON
        );
    }

    @Test
    @Transactional
    void getAllAllMassageThaisByDataSourceMappingTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        // Get all the allMassageThaiList where dataSourceMappingType equals to
        defaultAllMassageThaiFiltering(
            "dataSourceMappingType.equals=" + DEFAULT_DATA_SOURCE_MAPPING_TYPE,
            "dataSourceMappingType.equals=" + UPDATED_DATA_SOURCE_MAPPING_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAllMassageThaisByDataSourceMappingTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        // Get all the allMassageThaiList where dataSourceMappingType in
        defaultAllMassageThaiFiltering(
            "dataSourceMappingType.in=" + DEFAULT_DATA_SOURCE_MAPPING_TYPE + "," + UPDATED_DATA_SOURCE_MAPPING_TYPE,
            "dataSourceMappingType.in=" + UPDATED_DATA_SOURCE_MAPPING_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAllMassageThaisByDataSourceMappingTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        // Get all the allMassageThaiList where dataSourceMappingType is not null
        defaultAllMassageThaiFiltering("dataSourceMappingType.specified=true", "dataSourceMappingType.specified=false");
    }

    @Test
    @Transactional
    void getAllAllMassageThaisByTargetUrlsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        // Get all the allMassageThaiList where targetUrls equals to
        defaultAllMassageThaiFiltering("targetUrls.equals=" + DEFAULT_TARGET_URLS, "targetUrls.equals=" + UPDATED_TARGET_URLS);
    }

    @Test
    @Transactional
    void getAllAllMassageThaisByTargetUrlsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        // Get all the allMassageThaiList where targetUrls in
        defaultAllMassageThaiFiltering(
            "targetUrls.in=" + DEFAULT_TARGET_URLS + "," + UPDATED_TARGET_URLS,
            "targetUrls.in=" + UPDATED_TARGET_URLS
        );
    }

    @Test
    @Transactional
    void getAllAllMassageThaisByTargetUrlsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        // Get all the allMassageThaiList where targetUrls is not null
        defaultAllMassageThaiFiltering("targetUrls.specified=true", "targetUrls.specified=false");
    }

    @Test
    @Transactional
    void getAllAllMassageThaisByTargetUrlsContainsSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        // Get all the allMassageThaiList where targetUrls contains
        defaultAllMassageThaiFiltering("targetUrls.contains=" + DEFAULT_TARGET_URLS, "targetUrls.contains=" + UPDATED_TARGET_URLS);
    }

    @Test
    @Transactional
    void getAllAllMassageThaisByTargetUrlsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        // Get all the allMassageThaiList where targetUrls does not contain
        defaultAllMassageThaiFiltering(
            "targetUrls.doesNotContain=" + UPDATED_TARGET_URLS,
            "targetUrls.doesNotContain=" + DEFAULT_TARGET_URLS
        );
    }

    @Test
    @Transactional
    void getAllAllMassageThaisByThumbnailIsEqualToSomething() throws Exception {
        Metaverse thumbnail;
        if (TestUtil.findAll(em, Metaverse.class).isEmpty()) {
            allMassageThaiRepository.saveAndFlush(allMassageThai);
            thumbnail = MetaverseResourceIT.createEntity();
        } else {
            thumbnail = TestUtil.findAll(em, Metaverse.class).get(0);
        }
        em.persist(thumbnail);
        em.flush();
        allMassageThai.setThumbnail(thumbnail);
        allMassageThaiRepository.saveAndFlush(allMassageThai);
        Long thumbnailId = thumbnail.getId();
        // Get all the allMassageThaiList where thumbnail equals to thumbnailId
        defaultAllMassageThaiShouldBeFound("thumbnailId.equals=" + thumbnailId);

        // Get all the allMassageThaiList where thumbnail equals to (thumbnailId + 1)
        defaultAllMassageThaiShouldNotBeFound("thumbnailId.equals=" + (thumbnailId + 1));
    }

    @Test
    @Transactional
    void getAllAllMassageThaisByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            allMassageThaiRepository.saveAndFlush(allMassageThai);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        allMassageThai.setApplication(application);
        allMassageThaiRepository.saveAndFlush(allMassageThai);
        UUID applicationId = application.getId();
        // Get all the allMassageThaiList where application equals to applicationId
        defaultAllMassageThaiShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the allMassageThaiList where application equals to UUID.randomUUID()
        defaultAllMassageThaiShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAllMassageThaiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAllMassageThaiShouldBeFound(shouldBeFound);
        defaultAllMassageThaiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAllMassageThaiShouldBeFound(String filter) throws Exception {
        restAllMassageThaiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(allMassageThai.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].topContent").value(hasItem(DEFAULT_TOP_CONTENT)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].bottomContent").value(hasItem(DEFAULT_BOTTOM_CONTENT)))
            .andExpect(jsonPath("$.[*].propTitleMappingJason").value(hasItem(DEFAULT_PROP_TITLE_MAPPING_JASON)))
            .andExpect(jsonPath("$.[*].dataSourceMappingType").value(hasItem(DEFAULT_DATA_SOURCE_MAPPING_TYPE.toString())))
            .andExpect(jsonPath("$.[*].targetUrls").value(hasItem(DEFAULT_TARGET_URLS)));

        // Check, that the count call also returns 1
        restAllMassageThaiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAllMassageThaiShouldNotBeFound(String filter) throws Exception {
        restAllMassageThaiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAllMassageThaiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAllMassageThai() throws Exception {
        // Get the allMassageThai
        restAllMassageThaiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAllMassageThai() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the allMassageThai
        AllMassageThai updatedAllMassageThai = allMassageThaiRepository.findById(allMassageThai.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAllMassageThai are not directly saved in db
        em.detach(updatedAllMassageThai);
        updatedAllMassageThai
            .title(UPDATED_TITLE)
            .topContent(UPDATED_TOP_CONTENT)
            .content(UPDATED_CONTENT)
            .bottomContent(UPDATED_BOTTOM_CONTENT)
            .propTitleMappingJason(UPDATED_PROP_TITLE_MAPPING_JASON)
            .dataSourceMappingType(UPDATED_DATA_SOURCE_MAPPING_TYPE)
            .targetUrls(UPDATED_TARGET_URLS);
        AllMassageThaiDTO allMassageThaiDTO = allMassageThaiMapper.toDto(updatedAllMassageThai);

        restAllMassageThaiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, allMassageThaiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(allMassageThaiDTO))
            )
            .andExpect(status().isOk());

        // Validate the AllMassageThai in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAllMassageThaiToMatchAllProperties(updatedAllMassageThai);
    }

    @Test
    @Transactional
    void putNonExistingAllMassageThai() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        allMassageThai.setId(longCount.incrementAndGet());

        // Create the AllMassageThai
        AllMassageThaiDTO allMassageThaiDTO = allMassageThaiMapper.toDto(allMassageThai);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAllMassageThaiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, allMassageThaiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(allMassageThaiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AllMassageThai in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAllMassageThai() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        allMassageThai.setId(longCount.incrementAndGet());

        // Create the AllMassageThai
        AllMassageThaiDTO allMassageThaiDTO = allMassageThaiMapper.toDto(allMassageThai);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllMassageThaiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(allMassageThaiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AllMassageThai in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAllMassageThai() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        allMassageThai.setId(longCount.incrementAndGet());

        // Create the AllMassageThai
        AllMassageThaiDTO allMassageThaiDTO = allMassageThaiMapper.toDto(allMassageThai);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllMassageThaiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(allMassageThaiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AllMassageThai in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAllMassageThaiWithPatch() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the allMassageThai using partial update
        AllMassageThai partialUpdatedAllMassageThai = new AllMassageThai();
        partialUpdatedAllMassageThai.setId(allMassageThai.getId());

        partialUpdatedAllMassageThai
            .title(UPDATED_TITLE)
            .topContent(UPDATED_TOP_CONTENT)
            .content(UPDATED_CONTENT)
            .bottomContent(UPDATED_BOTTOM_CONTENT)
            .propTitleMappingJason(UPDATED_PROP_TITLE_MAPPING_JASON);

        restAllMassageThaiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAllMassageThai.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAllMassageThai))
            )
            .andExpect(status().isOk());

        // Validate the AllMassageThai in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAllMassageThaiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAllMassageThai, allMassageThai),
            getPersistedAllMassageThai(allMassageThai)
        );
    }

    @Test
    @Transactional
    void fullUpdateAllMassageThaiWithPatch() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the allMassageThai using partial update
        AllMassageThai partialUpdatedAllMassageThai = new AllMassageThai();
        partialUpdatedAllMassageThai.setId(allMassageThai.getId());

        partialUpdatedAllMassageThai
            .title(UPDATED_TITLE)
            .topContent(UPDATED_TOP_CONTENT)
            .content(UPDATED_CONTENT)
            .bottomContent(UPDATED_BOTTOM_CONTENT)
            .propTitleMappingJason(UPDATED_PROP_TITLE_MAPPING_JASON)
            .dataSourceMappingType(UPDATED_DATA_SOURCE_MAPPING_TYPE)
            .targetUrls(UPDATED_TARGET_URLS);

        restAllMassageThaiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAllMassageThai.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAllMassageThai))
            )
            .andExpect(status().isOk());

        // Validate the AllMassageThai in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAllMassageThaiUpdatableFieldsEquals(partialUpdatedAllMassageThai, getPersistedAllMassageThai(partialUpdatedAllMassageThai));
    }

    @Test
    @Transactional
    void patchNonExistingAllMassageThai() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        allMassageThai.setId(longCount.incrementAndGet());

        // Create the AllMassageThai
        AllMassageThaiDTO allMassageThaiDTO = allMassageThaiMapper.toDto(allMassageThai);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAllMassageThaiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, allMassageThaiDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(allMassageThaiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AllMassageThai in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAllMassageThai() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        allMassageThai.setId(longCount.incrementAndGet());

        // Create the AllMassageThai
        AllMassageThaiDTO allMassageThaiDTO = allMassageThaiMapper.toDto(allMassageThai);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllMassageThaiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(allMassageThaiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AllMassageThai in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAllMassageThai() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        allMassageThai.setId(longCount.incrementAndGet());

        // Create the AllMassageThai
        AllMassageThaiDTO allMassageThaiDTO = allMassageThaiMapper.toDto(allMassageThai);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllMassageThaiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(allMassageThaiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AllMassageThai in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAllMassageThai() throws Exception {
        // Initialize the database
        insertedAllMassageThai = allMassageThaiRepository.saveAndFlush(allMassageThai);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the allMassageThai
        restAllMassageThaiMockMvc
            .perform(delete(ENTITY_API_URL_ID, allMassageThai.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return allMassageThaiRepository.count();
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

    protected AllMassageThai getPersistedAllMassageThai(AllMassageThai allMassageThai) {
        return allMassageThaiRepository.findById(allMassageThai.getId()).orElseThrow();
    }

    protected void assertPersistedAllMassageThaiToMatchAllProperties(AllMassageThai expectedAllMassageThai) {
        assertAllMassageThaiAllPropertiesEquals(expectedAllMassageThai, getPersistedAllMassageThai(expectedAllMassageThai));
    }

    protected void assertPersistedAllMassageThaiToMatchUpdatableProperties(AllMassageThai expectedAllMassageThai) {
        assertAllMassageThaiAllUpdatablePropertiesEquals(expectedAllMassageThai, getPersistedAllMassageThai(expectedAllMassageThai));
    }
}
