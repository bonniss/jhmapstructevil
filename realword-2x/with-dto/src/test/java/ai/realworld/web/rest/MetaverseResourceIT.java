package ai.realworld.web.rest;

import static ai.realworld.domain.MetaverseAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlProPro;
import ai.realworld.domain.AlProProVi;
import ai.realworld.domain.AlProty;
import ai.realworld.domain.AlProtyVi;
import ai.realworld.domain.Metaverse;
import ai.realworld.repository.MetaverseRepository;
import ai.realworld.service.dto.MetaverseDTO;
import ai.realworld.service.mapper.MetaverseMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link MetaverseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MetaverseResourceIT {

    private static final String DEFAULT_FILENAME = "AAAAAAAAAA";
    private static final String UPDATED_FILENAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_EXT = "AAAAAAAAAA";
    private static final String UPDATED_FILE_EXT = "BBBBBBBBBB";

    private static final Long DEFAULT_FILE_SIZE = 1L;
    private static final Long UPDATED_FILE_SIZE = 2L;
    private static final Long SMALLER_FILE_SIZE = 1L - 1L;

    private static final String DEFAULT_FILE_URL = "AAAAAAAAAA";
    private static final String UPDATED_FILE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_THUMBNAIL_URL = "AAAAAAAAAA";
    private static final String UPDATED_THUMBNAIL_URL = "BBBBBBBBBB";

    private static final String DEFAULT_BLURHASH = "AAAAAAAAAA";
    private static final String UPDATED_BLURHASH = "BBBBBBBBBB";

    private static final String DEFAULT_OBJECT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_OBJECT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_OBJECT_META_JASON = "AAAAAAAAAA";
    private static final String UPDATED_OBJECT_META_JASON = "BBBBBBBBBB";

    private static final Double DEFAULT_URL_LIFESPAN_IN_SECONDS = 1D;
    private static final Double UPDATED_URL_LIFESPAN_IN_SECONDS = 2D;
    private static final Double SMALLER_URL_LIFESPAN_IN_SECONDS = 1D - 1D;

    private static final Instant DEFAULT_URL_EXPIRED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_URL_EXPIRED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_AUTO_RENEW_URL = false;
    private static final Boolean UPDATED_AUTO_RENEW_URL = true;

    private static final Boolean DEFAULT_IS_ENABLED = false;
    private static final Boolean UPDATED_IS_ENABLED = true;

    private static final String ENTITY_API_URL = "/api/metaverses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MetaverseRepository metaverseRepository;

    @Autowired
    private MetaverseMapper metaverseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMetaverseMockMvc;

    private Metaverse metaverse;

    private Metaverse insertedMetaverse;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Metaverse createEntity() {
        return new Metaverse()
            .filename(DEFAULT_FILENAME)
            .contentType(DEFAULT_CONTENT_TYPE)
            .fileExt(DEFAULT_FILE_EXT)
            .fileSize(DEFAULT_FILE_SIZE)
            .fileUrl(DEFAULT_FILE_URL)
            .thumbnailUrl(DEFAULT_THUMBNAIL_URL)
            .blurhash(DEFAULT_BLURHASH)
            .objectName(DEFAULT_OBJECT_NAME)
            .objectMetaJason(DEFAULT_OBJECT_META_JASON)
            .urlLifespanInSeconds(DEFAULT_URL_LIFESPAN_IN_SECONDS)
            .urlExpiredDate(DEFAULT_URL_EXPIRED_DATE)
            .autoRenewUrl(DEFAULT_AUTO_RENEW_URL)
            .isEnabled(DEFAULT_IS_ENABLED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Metaverse createUpdatedEntity() {
        return new Metaverse()
            .filename(UPDATED_FILENAME)
            .contentType(UPDATED_CONTENT_TYPE)
            .fileExt(UPDATED_FILE_EXT)
            .fileSize(UPDATED_FILE_SIZE)
            .fileUrl(UPDATED_FILE_URL)
            .thumbnailUrl(UPDATED_THUMBNAIL_URL)
            .blurhash(UPDATED_BLURHASH)
            .objectName(UPDATED_OBJECT_NAME)
            .objectMetaJason(UPDATED_OBJECT_META_JASON)
            .urlLifespanInSeconds(UPDATED_URL_LIFESPAN_IN_SECONDS)
            .urlExpiredDate(UPDATED_URL_EXPIRED_DATE)
            .autoRenewUrl(UPDATED_AUTO_RENEW_URL)
            .isEnabled(UPDATED_IS_ENABLED);
    }

    @BeforeEach
    public void initTest() {
        metaverse = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMetaverse != null) {
            metaverseRepository.delete(insertedMetaverse);
            insertedMetaverse = null;
        }
    }

    @Test
    @Transactional
    void createMetaverse() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Metaverse
        MetaverseDTO metaverseDTO = metaverseMapper.toDto(metaverse);
        var returnedMetaverseDTO = om.readValue(
            restMetaverseMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaverseDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MetaverseDTO.class
        );

        // Validate the Metaverse in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMetaverse = metaverseMapper.toEntity(returnedMetaverseDTO);
        assertMetaverseUpdatableFieldsEquals(returnedMetaverse, getPersistedMetaverse(returnedMetaverse));

        insertedMetaverse = returnedMetaverse;
    }

    @Test
    @Transactional
    void createMetaverseWithExistingId() throws Exception {
        // Create the Metaverse with an existing ID
        metaverse.setId(1L);
        MetaverseDTO metaverseDTO = metaverseMapper.toDto(metaverse);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMetaverseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaverseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Metaverse in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMetaverses() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList
        restMetaverseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metaverse.getId().intValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME)))
            .andExpect(jsonPath("$.[*].contentType").value(hasItem(DEFAULT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileExt").value(hasItem(DEFAULT_FILE_EXT)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].fileUrl").value(hasItem(DEFAULT_FILE_URL)))
            .andExpect(jsonPath("$.[*].thumbnailUrl").value(hasItem(DEFAULT_THUMBNAIL_URL)))
            .andExpect(jsonPath("$.[*].blurhash").value(hasItem(DEFAULT_BLURHASH)))
            .andExpect(jsonPath("$.[*].objectName").value(hasItem(DEFAULT_OBJECT_NAME)))
            .andExpect(jsonPath("$.[*].objectMetaJason").value(hasItem(DEFAULT_OBJECT_META_JASON)))
            .andExpect(jsonPath("$.[*].urlLifespanInSeconds").value(hasItem(DEFAULT_URL_LIFESPAN_IN_SECONDS.doubleValue())))
            .andExpect(jsonPath("$.[*].urlExpiredDate").value(hasItem(DEFAULT_URL_EXPIRED_DATE.toString())))
            .andExpect(jsonPath("$.[*].autoRenewUrl").value(hasItem(DEFAULT_AUTO_RENEW_URL.booleanValue())))
            .andExpect(jsonPath("$.[*].isEnabled").value(hasItem(DEFAULT_IS_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    void getMetaverse() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get the metaverse
        restMetaverseMockMvc
            .perform(get(ENTITY_API_URL_ID, metaverse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(metaverse.getId().intValue()))
            .andExpect(jsonPath("$.filename").value(DEFAULT_FILENAME))
            .andExpect(jsonPath("$.contentType").value(DEFAULT_CONTENT_TYPE))
            .andExpect(jsonPath("$.fileExt").value(DEFAULT_FILE_EXT))
            .andExpect(jsonPath("$.fileSize").value(DEFAULT_FILE_SIZE.intValue()))
            .andExpect(jsonPath("$.fileUrl").value(DEFAULT_FILE_URL))
            .andExpect(jsonPath("$.thumbnailUrl").value(DEFAULT_THUMBNAIL_URL))
            .andExpect(jsonPath("$.blurhash").value(DEFAULT_BLURHASH))
            .andExpect(jsonPath("$.objectName").value(DEFAULT_OBJECT_NAME))
            .andExpect(jsonPath("$.objectMetaJason").value(DEFAULT_OBJECT_META_JASON))
            .andExpect(jsonPath("$.urlLifespanInSeconds").value(DEFAULT_URL_LIFESPAN_IN_SECONDS.doubleValue()))
            .andExpect(jsonPath("$.urlExpiredDate").value(DEFAULT_URL_EXPIRED_DATE.toString()))
            .andExpect(jsonPath("$.autoRenewUrl").value(DEFAULT_AUTO_RENEW_URL.booleanValue()))
            .andExpect(jsonPath("$.isEnabled").value(DEFAULT_IS_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    void getMetaversesByIdFiltering() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        Long id = metaverse.getId();

        defaultMetaverseFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMetaverseFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMetaverseFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMetaversesByFilenameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where filename equals to
        defaultMetaverseFiltering("filename.equals=" + DEFAULT_FILENAME, "filename.equals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllMetaversesByFilenameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where filename in
        defaultMetaverseFiltering("filename.in=" + DEFAULT_FILENAME + "," + UPDATED_FILENAME, "filename.in=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllMetaversesByFilenameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where filename is not null
        defaultMetaverseFiltering("filename.specified=true", "filename.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaversesByFilenameContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where filename contains
        defaultMetaverseFiltering("filename.contains=" + DEFAULT_FILENAME, "filename.contains=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllMetaversesByFilenameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where filename does not contain
        defaultMetaverseFiltering("filename.doesNotContain=" + UPDATED_FILENAME, "filename.doesNotContain=" + DEFAULT_FILENAME);
    }

    @Test
    @Transactional
    void getAllMetaversesByContentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where contentType equals to
        defaultMetaverseFiltering("contentType.equals=" + DEFAULT_CONTENT_TYPE, "contentType.equals=" + UPDATED_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void getAllMetaversesByContentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where contentType in
        defaultMetaverseFiltering(
            "contentType.in=" + DEFAULT_CONTENT_TYPE + "," + UPDATED_CONTENT_TYPE,
            "contentType.in=" + UPDATED_CONTENT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllMetaversesByContentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where contentType is not null
        defaultMetaverseFiltering("contentType.specified=true", "contentType.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaversesByContentTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where contentType contains
        defaultMetaverseFiltering("contentType.contains=" + DEFAULT_CONTENT_TYPE, "contentType.contains=" + UPDATED_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void getAllMetaversesByContentTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where contentType does not contain
        defaultMetaverseFiltering(
            "contentType.doesNotContain=" + UPDATED_CONTENT_TYPE,
            "contentType.doesNotContain=" + DEFAULT_CONTENT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllMetaversesByFileExtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where fileExt equals to
        defaultMetaverseFiltering("fileExt.equals=" + DEFAULT_FILE_EXT, "fileExt.equals=" + UPDATED_FILE_EXT);
    }

    @Test
    @Transactional
    void getAllMetaversesByFileExtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where fileExt in
        defaultMetaverseFiltering("fileExt.in=" + DEFAULT_FILE_EXT + "," + UPDATED_FILE_EXT, "fileExt.in=" + UPDATED_FILE_EXT);
    }

    @Test
    @Transactional
    void getAllMetaversesByFileExtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where fileExt is not null
        defaultMetaverseFiltering("fileExt.specified=true", "fileExt.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaversesByFileExtContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where fileExt contains
        defaultMetaverseFiltering("fileExt.contains=" + DEFAULT_FILE_EXT, "fileExt.contains=" + UPDATED_FILE_EXT);
    }

    @Test
    @Transactional
    void getAllMetaversesByFileExtNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where fileExt does not contain
        defaultMetaverseFiltering("fileExt.doesNotContain=" + UPDATED_FILE_EXT, "fileExt.doesNotContain=" + DEFAULT_FILE_EXT);
    }

    @Test
    @Transactional
    void getAllMetaversesByFileSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where fileSize equals to
        defaultMetaverseFiltering("fileSize.equals=" + DEFAULT_FILE_SIZE, "fileSize.equals=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllMetaversesByFileSizeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where fileSize in
        defaultMetaverseFiltering("fileSize.in=" + DEFAULT_FILE_SIZE + "," + UPDATED_FILE_SIZE, "fileSize.in=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllMetaversesByFileSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where fileSize is not null
        defaultMetaverseFiltering("fileSize.specified=true", "fileSize.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaversesByFileSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where fileSize is greater than or equal to
        defaultMetaverseFiltering("fileSize.greaterThanOrEqual=" + DEFAULT_FILE_SIZE, "fileSize.greaterThanOrEqual=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllMetaversesByFileSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where fileSize is less than or equal to
        defaultMetaverseFiltering("fileSize.lessThanOrEqual=" + DEFAULT_FILE_SIZE, "fileSize.lessThanOrEqual=" + SMALLER_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllMetaversesByFileSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where fileSize is less than
        defaultMetaverseFiltering("fileSize.lessThan=" + UPDATED_FILE_SIZE, "fileSize.lessThan=" + DEFAULT_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllMetaversesByFileSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where fileSize is greater than
        defaultMetaverseFiltering("fileSize.greaterThan=" + SMALLER_FILE_SIZE, "fileSize.greaterThan=" + DEFAULT_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllMetaversesByFileUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where fileUrl equals to
        defaultMetaverseFiltering("fileUrl.equals=" + DEFAULT_FILE_URL, "fileUrl.equals=" + UPDATED_FILE_URL);
    }

    @Test
    @Transactional
    void getAllMetaversesByFileUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where fileUrl in
        defaultMetaverseFiltering("fileUrl.in=" + DEFAULT_FILE_URL + "," + UPDATED_FILE_URL, "fileUrl.in=" + UPDATED_FILE_URL);
    }

    @Test
    @Transactional
    void getAllMetaversesByFileUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where fileUrl is not null
        defaultMetaverseFiltering("fileUrl.specified=true", "fileUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaversesByFileUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where fileUrl contains
        defaultMetaverseFiltering("fileUrl.contains=" + DEFAULT_FILE_URL, "fileUrl.contains=" + UPDATED_FILE_URL);
    }

    @Test
    @Transactional
    void getAllMetaversesByFileUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where fileUrl does not contain
        defaultMetaverseFiltering("fileUrl.doesNotContain=" + UPDATED_FILE_URL, "fileUrl.doesNotContain=" + DEFAULT_FILE_URL);
    }

    @Test
    @Transactional
    void getAllMetaversesByThumbnailUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where thumbnailUrl equals to
        defaultMetaverseFiltering("thumbnailUrl.equals=" + DEFAULT_THUMBNAIL_URL, "thumbnailUrl.equals=" + UPDATED_THUMBNAIL_URL);
    }

    @Test
    @Transactional
    void getAllMetaversesByThumbnailUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where thumbnailUrl in
        defaultMetaverseFiltering(
            "thumbnailUrl.in=" + DEFAULT_THUMBNAIL_URL + "," + UPDATED_THUMBNAIL_URL,
            "thumbnailUrl.in=" + UPDATED_THUMBNAIL_URL
        );
    }

    @Test
    @Transactional
    void getAllMetaversesByThumbnailUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where thumbnailUrl is not null
        defaultMetaverseFiltering("thumbnailUrl.specified=true", "thumbnailUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaversesByThumbnailUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where thumbnailUrl contains
        defaultMetaverseFiltering("thumbnailUrl.contains=" + DEFAULT_THUMBNAIL_URL, "thumbnailUrl.contains=" + UPDATED_THUMBNAIL_URL);
    }

    @Test
    @Transactional
    void getAllMetaversesByThumbnailUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where thumbnailUrl does not contain
        defaultMetaverseFiltering(
            "thumbnailUrl.doesNotContain=" + UPDATED_THUMBNAIL_URL,
            "thumbnailUrl.doesNotContain=" + DEFAULT_THUMBNAIL_URL
        );
    }

    @Test
    @Transactional
    void getAllMetaversesByBlurhashIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where blurhash equals to
        defaultMetaverseFiltering("blurhash.equals=" + DEFAULT_BLURHASH, "blurhash.equals=" + UPDATED_BLURHASH);
    }

    @Test
    @Transactional
    void getAllMetaversesByBlurhashIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where blurhash in
        defaultMetaverseFiltering("blurhash.in=" + DEFAULT_BLURHASH + "," + UPDATED_BLURHASH, "blurhash.in=" + UPDATED_BLURHASH);
    }

    @Test
    @Transactional
    void getAllMetaversesByBlurhashIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where blurhash is not null
        defaultMetaverseFiltering("blurhash.specified=true", "blurhash.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaversesByBlurhashContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where blurhash contains
        defaultMetaverseFiltering("blurhash.contains=" + DEFAULT_BLURHASH, "blurhash.contains=" + UPDATED_BLURHASH);
    }

    @Test
    @Transactional
    void getAllMetaversesByBlurhashNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where blurhash does not contain
        defaultMetaverseFiltering("blurhash.doesNotContain=" + UPDATED_BLURHASH, "blurhash.doesNotContain=" + DEFAULT_BLURHASH);
    }

    @Test
    @Transactional
    void getAllMetaversesByObjectNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where objectName equals to
        defaultMetaverseFiltering("objectName.equals=" + DEFAULT_OBJECT_NAME, "objectName.equals=" + UPDATED_OBJECT_NAME);
    }

    @Test
    @Transactional
    void getAllMetaversesByObjectNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where objectName in
        defaultMetaverseFiltering(
            "objectName.in=" + DEFAULT_OBJECT_NAME + "," + UPDATED_OBJECT_NAME,
            "objectName.in=" + UPDATED_OBJECT_NAME
        );
    }

    @Test
    @Transactional
    void getAllMetaversesByObjectNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where objectName is not null
        defaultMetaverseFiltering("objectName.specified=true", "objectName.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaversesByObjectNameContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where objectName contains
        defaultMetaverseFiltering("objectName.contains=" + DEFAULT_OBJECT_NAME, "objectName.contains=" + UPDATED_OBJECT_NAME);
    }

    @Test
    @Transactional
    void getAllMetaversesByObjectNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where objectName does not contain
        defaultMetaverseFiltering("objectName.doesNotContain=" + UPDATED_OBJECT_NAME, "objectName.doesNotContain=" + DEFAULT_OBJECT_NAME);
    }

    @Test
    @Transactional
    void getAllMetaversesByObjectMetaJasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where objectMetaJason equals to
        defaultMetaverseFiltering(
            "objectMetaJason.equals=" + DEFAULT_OBJECT_META_JASON,
            "objectMetaJason.equals=" + UPDATED_OBJECT_META_JASON
        );
    }

    @Test
    @Transactional
    void getAllMetaversesByObjectMetaJasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where objectMetaJason in
        defaultMetaverseFiltering(
            "objectMetaJason.in=" + DEFAULT_OBJECT_META_JASON + "," + UPDATED_OBJECT_META_JASON,
            "objectMetaJason.in=" + UPDATED_OBJECT_META_JASON
        );
    }

    @Test
    @Transactional
    void getAllMetaversesByObjectMetaJasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where objectMetaJason is not null
        defaultMetaverseFiltering("objectMetaJason.specified=true", "objectMetaJason.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaversesByObjectMetaJasonContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where objectMetaJason contains
        defaultMetaverseFiltering(
            "objectMetaJason.contains=" + DEFAULT_OBJECT_META_JASON,
            "objectMetaJason.contains=" + UPDATED_OBJECT_META_JASON
        );
    }

    @Test
    @Transactional
    void getAllMetaversesByObjectMetaJasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where objectMetaJason does not contain
        defaultMetaverseFiltering(
            "objectMetaJason.doesNotContain=" + UPDATED_OBJECT_META_JASON,
            "objectMetaJason.doesNotContain=" + DEFAULT_OBJECT_META_JASON
        );
    }

    @Test
    @Transactional
    void getAllMetaversesByUrlLifespanInSecondsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where urlLifespanInSeconds equals to
        defaultMetaverseFiltering(
            "urlLifespanInSeconds.equals=" + DEFAULT_URL_LIFESPAN_IN_SECONDS,
            "urlLifespanInSeconds.equals=" + UPDATED_URL_LIFESPAN_IN_SECONDS
        );
    }

    @Test
    @Transactional
    void getAllMetaversesByUrlLifespanInSecondsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where urlLifespanInSeconds in
        defaultMetaverseFiltering(
            "urlLifespanInSeconds.in=" + DEFAULT_URL_LIFESPAN_IN_SECONDS + "," + UPDATED_URL_LIFESPAN_IN_SECONDS,
            "urlLifespanInSeconds.in=" + UPDATED_URL_LIFESPAN_IN_SECONDS
        );
    }

    @Test
    @Transactional
    void getAllMetaversesByUrlLifespanInSecondsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where urlLifespanInSeconds is not null
        defaultMetaverseFiltering("urlLifespanInSeconds.specified=true", "urlLifespanInSeconds.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaversesByUrlLifespanInSecondsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where urlLifespanInSeconds is greater than or equal to
        defaultMetaverseFiltering(
            "urlLifespanInSeconds.greaterThanOrEqual=" + DEFAULT_URL_LIFESPAN_IN_SECONDS,
            "urlLifespanInSeconds.greaterThanOrEqual=" + UPDATED_URL_LIFESPAN_IN_SECONDS
        );
    }

    @Test
    @Transactional
    void getAllMetaversesByUrlLifespanInSecondsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where urlLifespanInSeconds is less than or equal to
        defaultMetaverseFiltering(
            "urlLifespanInSeconds.lessThanOrEqual=" + DEFAULT_URL_LIFESPAN_IN_SECONDS,
            "urlLifespanInSeconds.lessThanOrEqual=" + SMALLER_URL_LIFESPAN_IN_SECONDS
        );
    }

    @Test
    @Transactional
    void getAllMetaversesByUrlLifespanInSecondsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where urlLifespanInSeconds is less than
        defaultMetaverseFiltering(
            "urlLifespanInSeconds.lessThan=" + UPDATED_URL_LIFESPAN_IN_SECONDS,
            "urlLifespanInSeconds.lessThan=" + DEFAULT_URL_LIFESPAN_IN_SECONDS
        );
    }

    @Test
    @Transactional
    void getAllMetaversesByUrlLifespanInSecondsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where urlLifespanInSeconds is greater than
        defaultMetaverseFiltering(
            "urlLifespanInSeconds.greaterThan=" + SMALLER_URL_LIFESPAN_IN_SECONDS,
            "urlLifespanInSeconds.greaterThan=" + DEFAULT_URL_LIFESPAN_IN_SECONDS
        );
    }

    @Test
    @Transactional
    void getAllMetaversesByUrlExpiredDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where urlExpiredDate equals to
        defaultMetaverseFiltering("urlExpiredDate.equals=" + DEFAULT_URL_EXPIRED_DATE, "urlExpiredDate.equals=" + UPDATED_URL_EXPIRED_DATE);
    }

    @Test
    @Transactional
    void getAllMetaversesByUrlExpiredDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where urlExpiredDate in
        defaultMetaverseFiltering(
            "urlExpiredDate.in=" + DEFAULT_URL_EXPIRED_DATE + "," + UPDATED_URL_EXPIRED_DATE,
            "urlExpiredDate.in=" + UPDATED_URL_EXPIRED_DATE
        );
    }

    @Test
    @Transactional
    void getAllMetaversesByUrlExpiredDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where urlExpiredDate is not null
        defaultMetaverseFiltering("urlExpiredDate.specified=true", "urlExpiredDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaversesByAutoRenewUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where autoRenewUrl equals to
        defaultMetaverseFiltering("autoRenewUrl.equals=" + DEFAULT_AUTO_RENEW_URL, "autoRenewUrl.equals=" + UPDATED_AUTO_RENEW_URL);
    }

    @Test
    @Transactional
    void getAllMetaversesByAutoRenewUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where autoRenewUrl in
        defaultMetaverseFiltering(
            "autoRenewUrl.in=" + DEFAULT_AUTO_RENEW_URL + "," + UPDATED_AUTO_RENEW_URL,
            "autoRenewUrl.in=" + UPDATED_AUTO_RENEW_URL
        );
    }

    @Test
    @Transactional
    void getAllMetaversesByAutoRenewUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where autoRenewUrl is not null
        defaultMetaverseFiltering("autoRenewUrl.specified=true", "autoRenewUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaversesByIsEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where isEnabled equals to
        defaultMetaverseFiltering("isEnabled.equals=" + DEFAULT_IS_ENABLED, "isEnabled.equals=" + UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    void getAllMetaversesByIsEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where isEnabled in
        defaultMetaverseFiltering("isEnabled.in=" + DEFAULT_IS_ENABLED + "," + UPDATED_IS_ENABLED, "isEnabled.in=" + UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    void getAllMetaversesByIsEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        // Get all the metaverseList where isEnabled is not null
        defaultMetaverseFiltering("isEnabled.specified=true", "isEnabled.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaversesByAlProProIsEqualToSomething() throws Exception {
        AlProPro alProPro;
        if (TestUtil.findAll(em, AlProPro.class).isEmpty()) {
            metaverseRepository.saveAndFlush(metaverse);
            alProPro = AlProProResourceIT.createEntity();
        } else {
            alProPro = TestUtil.findAll(em, AlProPro.class).get(0);
        }
        em.persist(alProPro);
        em.flush();
        metaverse.addAlProPro(alProPro);
        metaverseRepository.saveAndFlush(metaverse);
        UUID alProProId = alProPro.getId();
        // Get all the metaverseList where alProPro equals to alProProId
        defaultMetaverseShouldBeFound("alProProId.equals=" + alProProId);

        // Get all the metaverseList where alProPro equals to UUID.randomUUID()
        defaultMetaverseShouldNotBeFound("alProProId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllMetaversesByAlProtyIsEqualToSomething() throws Exception {
        AlProty alProty;
        if (TestUtil.findAll(em, AlProty.class).isEmpty()) {
            metaverseRepository.saveAndFlush(metaverse);
            alProty = AlProtyResourceIT.createEntity(em);
        } else {
            alProty = TestUtil.findAll(em, AlProty.class).get(0);
        }
        em.persist(alProty);
        em.flush();
        metaverse.addAlProty(alProty);
        metaverseRepository.saveAndFlush(metaverse);
        UUID alProtyId = alProty.getId();
        // Get all the metaverseList where alProty equals to alProtyId
        defaultMetaverseShouldBeFound("alProtyId.equals=" + alProtyId);

        // Get all the metaverseList where alProty equals to UUID.randomUUID()
        defaultMetaverseShouldNotBeFound("alProtyId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllMetaversesByAlProProViIsEqualToSomething() throws Exception {
        AlProProVi alProProVi;
        if (TestUtil.findAll(em, AlProProVi.class).isEmpty()) {
            metaverseRepository.saveAndFlush(metaverse);
            alProProVi = AlProProViResourceIT.createEntity();
        } else {
            alProProVi = TestUtil.findAll(em, AlProProVi.class).get(0);
        }
        em.persist(alProProVi);
        em.flush();
        metaverse.addAlProProVi(alProProVi);
        metaverseRepository.saveAndFlush(metaverse);
        UUID alProProViId = alProProVi.getId();
        // Get all the metaverseList where alProProVi equals to alProProViId
        defaultMetaverseShouldBeFound("alProProViId.equals=" + alProProViId);

        // Get all the metaverseList where alProProVi equals to UUID.randomUUID()
        defaultMetaverseShouldNotBeFound("alProProViId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllMetaversesByAlProtyViIsEqualToSomething() throws Exception {
        AlProtyVi alProtyVi;
        if (TestUtil.findAll(em, AlProtyVi.class).isEmpty()) {
            metaverseRepository.saveAndFlush(metaverse);
            alProtyVi = AlProtyViResourceIT.createEntity(em);
        } else {
            alProtyVi = TestUtil.findAll(em, AlProtyVi.class).get(0);
        }
        em.persist(alProtyVi);
        em.flush();
        metaverse.addAlProtyVi(alProtyVi);
        metaverseRepository.saveAndFlush(metaverse);
        UUID alProtyViId = alProtyVi.getId();
        // Get all the metaverseList where alProtyVi equals to alProtyViId
        defaultMetaverseShouldBeFound("alProtyViId.equals=" + alProtyViId);

        // Get all the metaverseList where alProtyVi equals to UUID.randomUUID()
        defaultMetaverseShouldNotBeFound("alProtyViId.equals=" + UUID.randomUUID());
    }

    private void defaultMetaverseFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMetaverseShouldBeFound(shouldBeFound);
        defaultMetaverseShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMetaverseShouldBeFound(String filter) throws Exception {
        restMetaverseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metaverse.getId().intValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME)))
            .andExpect(jsonPath("$.[*].contentType").value(hasItem(DEFAULT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileExt").value(hasItem(DEFAULT_FILE_EXT)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].fileUrl").value(hasItem(DEFAULT_FILE_URL)))
            .andExpect(jsonPath("$.[*].thumbnailUrl").value(hasItem(DEFAULT_THUMBNAIL_URL)))
            .andExpect(jsonPath("$.[*].blurhash").value(hasItem(DEFAULT_BLURHASH)))
            .andExpect(jsonPath("$.[*].objectName").value(hasItem(DEFAULT_OBJECT_NAME)))
            .andExpect(jsonPath("$.[*].objectMetaJason").value(hasItem(DEFAULT_OBJECT_META_JASON)))
            .andExpect(jsonPath("$.[*].urlLifespanInSeconds").value(hasItem(DEFAULT_URL_LIFESPAN_IN_SECONDS.doubleValue())))
            .andExpect(jsonPath("$.[*].urlExpiredDate").value(hasItem(DEFAULT_URL_EXPIRED_DATE.toString())))
            .andExpect(jsonPath("$.[*].autoRenewUrl").value(hasItem(DEFAULT_AUTO_RENEW_URL.booleanValue())))
            .andExpect(jsonPath("$.[*].isEnabled").value(hasItem(DEFAULT_IS_ENABLED.booleanValue())));

        // Check, that the count call also returns 1
        restMetaverseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMetaverseShouldNotBeFound(String filter) throws Exception {
        restMetaverseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMetaverseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMetaverse() throws Exception {
        // Get the metaverse
        restMetaverseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMetaverse() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the metaverse
        Metaverse updatedMetaverse = metaverseRepository.findById(metaverse.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMetaverse are not directly saved in db
        em.detach(updatedMetaverse);
        updatedMetaverse
            .filename(UPDATED_FILENAME)
            .contentType(UPDATED_CONTENT_TYPE)
            .fileExt(UPDATED_FILE_EXT)
            .fileSize(UPDATED_FILE_SIZE)
            .fileUrl(UPDATED_FILE_URL)
            .thumbnailUrl(UPDATED_THUMBNAIL_URL)
            .blurhash(UPDATED_BLURHASH)
            .objectName(UPDATED_OBJECT_NAME)
            .objectMetaJason(UPDATED_OBJECT_META_JASON)
            .urlLifespanInSeconds(UPDATED_URL_LIFESPAN_IN_SECONDS)
            .urlExpiredDate(UPDATED_URL_EXPIRED_DATE)
            .autoRenewUrl(UPDATED_AUTO_RENEW_URL)
            .isEnabled(UPDATED_IS_ENABLED);
        MetaverseDTO metaverseDTO = metaverseMapper.toDto(updatedMetaverse);

        restMetaverseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, metaverseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(metaverseDTO))
            )
            .andExpect(status().isOk());

        // Validate the Metaverse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMetaverseToMatchAllProperties(updatedMetaverse);
    }

    @Test
    @Transactional
    void putNonExistingMetaverse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        metaverse.setId(longCount.incrementAndGet());

        // Create the Metaverse
        MetaverseDTO metaverseDTO = metaverseMapper.toDto(metaverse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMetaverseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, metaverseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(metaverseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Metaverse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMetaverse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        metaverse.setId(longCount.incrementAndGet());

        // Create the Metaverse
        MetaverseDTO metaverseDTO = metaverseMapper.toDto(metaverse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaverseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(metaverseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Metaverse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMetaverse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        metaverse.setId(longCount.incrementAndGet());

        // Create the Metaverse
        MetaverseDTO metaverseDTO = metaverseMapper.toDto(metaverse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaverseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaverseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Metaverse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMetaverseWithPatch() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the metaverse using partial update
        Metaverse partialUpdatedMetaverse = new Metaverse();
        partialUpdatedMetaverse.setId(metaverse.getId());

        partialUpdatedMetaverse
            .fileSize(UPDATED_FILE_SIZE)
            .fileUrl(UPDATED_FILE_URL)
            .blurhash(UPDATED_BLURHASH)
            .objectName(UPDATED_OBJECT_NAME)
            .urlLifespanInSeconds(UPDATED_URL_LIFESPAN_IN_SECONDS)
            .urlExpiredDate(UPDATED_URL_EXPIRED_DATE)
            .isEnabled(UPDATED_IS_ENABLED);

        restMetaverseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMetaverse.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMetaverse))
            )
            .andExpect(status().isOk());

        // Validate the Metaverse in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMetaverseUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMetaverse, metaverse),
            getPersistedMetaverse(metaverse)
        );
    }

    @Test
    @Transactional
    void fullUpdateMetaverseWithPatch() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the metaverse using partial update
        Metaverse partialUpdatedMetaverse = new Metaverse();
        partialUpdatedMetaverse.setId(metaverse.getId());

        partialUpdatedMetaverse
            .filename(UPDATED_FILENAME)
            .contentType(UPDATED_CONTENT_TYPE)
            .fileExt(UPDATED_FILE_EXT)
            .fileSize(UPDATED_FILE_SIZE)
            .fileUrl(UPDATED_FILE_URL)
            .thumbnailUrl(UPDATED_THUMBNAIL_URL)
            .blurhash(UPDATED_BLURHASH)
            .objectName(UPDATED_OBJECT_NAME)
            .objectMetaJason(UPDATED_OBJECT_META_JASON)
            .urlLifespanInSeconds(UPDATED_URL_LIFESPAN_IN_SECONDS)
            .urlExpiredDate(UPDATED_URL_EXPIRED_DATE)
            .autoRenewUrl(UPDATED_AUTO_RENEW_URL)
            .isEnabled(UPDATED_IS_ENABLED);

        restMetaverseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMetaverse.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMetaverse))
            )
            .andExpect(status().isOk());

        // Validate the Metaverse in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMetaverseUpdatableFieldsEquals(partialUpdatedMetaverse, getPersistedMetaverse(partialUpdatedMetaverse));
    }

    @Test
    @Transactional
    void patchNonExistingMetaverse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        metaverse.setId(longCount.incrementAndGet());

        // Create the Metaverse
        MetaverseDTO metaverseDTO = metaverseMapper.toDto(metaverse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMetaverseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, metaverseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(metaverseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Metaverse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMetaverse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        metaverse.setId(longCount.incrementAndGet());

        // Create the Metaverse
        MetaverseDTO metaverseDTO = metaverseMapper.toDto(metaverse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaverseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(metaverseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Metaverse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMetaverse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        metaverse.setId(longCount.incrementAndGet());

        // Create the Metaverse
        MetaverseDTO metaverseDTO = metaverseMapper.toDto(metaverse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaverseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(metaverseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Metaverse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMetaverse() throws Exception {
        // Initialize the database
        insertedMetaverse = metaverseRepository.saveAndFlush(metaverse);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the metaverse
        restMetaverseMockMvc
            .perform(delete(ENTITY_API_URL_ID, metaverse.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return metaverseRepository.count();
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

    protected Metaverse getPersistedMetaverse(Metaverse metaverse) {
        return metaverseRepository.findById(metaverse.getId()).orElseThrow();
    }

    protected void assertPersistedMetaverseToMatchAllProperties(Metaverse expectedMetaverse) {
        assertMetaverseAllPropertiesEquals(expectedMetaverse, getPersistedMetaverse(expectedMetaverse));
    }

    protected void assertPersistedMetaverseToMatchUpdatableProperties(Metaverse expectedMetaverse) {
        assertMetaverseAllUpdatablePropertiesEquals(expectedMetaverse, getPersistedMetaverse(expectedMetaverse));
    }
}
