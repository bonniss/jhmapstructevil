package ai.realworld.web.rest;

import static ai.realworld.domain.AlLexFergAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlCatalina;
import ai.realworld.domain.AlLexFerg;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.domain.enumeration.PaoloStatus;
import ai.realworld.repository.AlLexFergRepository;
import ai.realworld.service.dto.AlLexFergDTO;
import ai.realworld.service.mapper.AlLexFergMapper;
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
 * Integration tests for the {@link AlLexFergResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlLexFergResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SLUG = "AAAAAAAAAA";
    private static final String UPDATED_SLUG = "BBBBBBBBBB";

    private static final String DEFAULT_SUMMARY = "AAAAAAAAAA";
    private static final String UPDATED_SUMMARY = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_HEITIGA = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_HEITIGA = "BBBBBBBBBB";

    private static final PaoloStatus DEFAULT_PUBLICATION_STATUS = PaoloStatus.DRAFT;
    private static final PaoloStatus UPDATED_PUBLICATION_STATUS = PaoloStatus.PUBLISHED;

    private static final Instant DEFAULT_PUBLISHED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PUBLISHED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/al-lex-fergs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlLexFergRepository alLexFergRepository;

    @Autowired
    private AlLexFergMapper alLexFergMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlLexFergMockMvc;

    private AlLexFerg alLexFerg;

    private AlLexFerg insertedAlLexFerg;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlLexFerg createEntity() {
        return new AlLexFerg()
            .title(DEFAULT_TITLE)
            .slug(DEFAULT_SLUG)
            .summary(DEFAULT_SUMMARY)
            .contentHeitiga(DEFAULT_CONTENT_HEITIGA)
            .publicationStatus(DEFAULT_PUBLICATION_STATUS)
            .publishedDate(DEFAULT_PUBLISHED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlLexFerg createUpdatedEntity() {
        return new AlLexFerg()
            .title(UPDATED_TITLE)
            .slug(UPDATED_SLUG)
            .summary(UPDATED_SUMMARY)
            .contentHeitiga(UPDATED_CONTENT_HEITIGA)
            .publicationStatus(UPDATED_PUBLICATION_STATUS)
            .publishedDate(UPDATED_PUBLISHED_DATE);
    }

    @BeforeEach
    public void initTest() {
        alLexFerg = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlLexFerg != null) {
            alLexFergRepository.delete(insertedAlLexFerg);
            insertedAlLexFerg = null;
        }
    }

    @Test
    @Transactional
    void createAlLexFerg() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlLexFerg
        AlLexFergDTO alLexFergDTO = alLexFergMapper.toDto(alLexFerg);
        var returnedAlLexFergDTO = om.readValue(
            restAlLexFergMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alLexFergDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlLexFergDTO.class
        );

        // Validate the AlLexFerg in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlLexFerg = alLexFergMapper.toEntity(returnedAlLexFergDTO);
        assertAlLexFergUpdatableFieldsEquals(returnedAlLexFerg, getPersistedAlLexFerg(returnedAlLexFerg));

        insertedAlLexFerg = returnedAlLexFerg;
    }

    @Test
    @Transactional
    void createAlLexFergWithExistingId() throws Exception {
        // Create the AlLexFerg with an existing ID
        alLexFerg.setId(1L);
        AlLexFergDTO alLexFergDTO = alLexFergMapper.toDto(alLexFerg);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlLexFergMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alLexFergDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlLexFerg in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alLexFerg.setTitle(null);

        // Create the AlLexFerg, which fails.
        AlLexFergDTO alLexFergDTO = alLexFergMapper.toDto(alLexFerg);

        restAlLexFergMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alLexFergDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSlugIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alLexFerg.setSlug(null);

        // Create the AlLexFerg, which fails.
        AlLexFergDTO alLexFergDTO = alLexFergMapper.toDto(alLexFerg);

        restAlLexFergMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alLexFergDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlLexFergs() throws Exception {
        // Initialize the database
        insertedAlLexFerg = alLexFergRepository.saveAndFlush(alLexFerg);

        // Get all the alLexFergList
        restAlLexFergMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alLexFerg.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].summary").value(hasItem(DEFAULT_SUMMARY)))
            .andExpect(jsonPath("$.[*].contentHeitiga").value(hasItem(DEFAULT_CONTENT_HEITIGA)))
            .andExpect(jsonPath("$.[*].publicationStatus").value(hasItem(DEFAULT_PUBLICATION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].publishedDate").value(hasItem(DEFAULT_PUBLISHED_DATE.toString())));
    }

    @Test
    @Transactional
    void getAlLexFerg() throws Exception {
        // Initialize the database
        insertedAlLexFerg = alLexFergRepository.saveAndFlush(alLexFerg);

        // Get the alLexFerg
        restAlLexFergMockMvc
            .perform(get(ENTITY_API_URL_ID, alLexFerg.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alLexFerg.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG))
            .andExpect(jsonPath("$.summary").value(DEFAULT_SUMMARY))
            .andExpect(jsonPath("$.contentHeitiga").value(DEFAULT_CONTENT_HEITIGA))
            .andExpect(jsonPath("$.publicationStatus").value(DEFAULT_PUBLICATION_STATUS.toString()))
            .andExpect(jsonPath("$.publishedDate").value(DEFAULT_PUBLISHED_DATE.toString()));
    }

    @Test
    @Transactional
    void getAlLexFergsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlLexFerg = alLexFergRepository.saveAndFlush(alLexFerg);

        Long id = alLexFerg.getId();

        defaultAlLexFergFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlLexFergFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlLexFergFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlLexFergsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLexFerg = alLexFergRepository.saveAndFlush(alLexFerg);

        // Get all the alLexFergList where title equals to
        defaultAlLexFergFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAlLexFergsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlLexFerg = alLexFergRepository.saveAndFlush(alLexFerg);

        // Get all the alLexFergList where title in
        defaultAlLexFergFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAlLexFergsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlLexFerg = alLexFergRepository.saveAndFlush(alLexFerg);

        // Get all the alLexFergList where title is not null
        defaultAlLexFergFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllAlLexFergsByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedAlLexFerg = alLexFergRepository.saveAndFlush(alLexFerg);

        // Get all the alLexFergList where title contains
        defaultAlLexFergFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAlLexFergsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlLexFerg = alLexFergRepository.saveAndFlush(alLexFerg);

        // Get all the alLexFergList where title does not contain
        defaultAlLexFergFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllAlLexFergsBySlugIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLexFerg = alLexFergRepository.saveAndFlush(alLexFerg);

        // Get all the alLexFergList where slug equals to
        defaultAlLexFergFiltering("slug.equals=" + DEFAULT_SLUG, "slug.equals=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllAlLexFergsBySlugIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlLexFerg = alLexFergRepository.saveAndFlush(alLexFerg);

        // Get all the alLexFergList where slug in
        defaultAlLexFergFiltering("slug.in=" + DEFAULT_SLUG + "," + UPDATED_SLUG, "slug.in=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllAlLexFergsBySlugIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlLexFerg = alLexFergRepository.saveAndFlush(alLexFerg);

        // Get all the alLexFergList where slug is not null
        defaultAlLexFergFiltering("slug.specified=true", "slug.specified=false");
    }

    @Test
    @Transactional
    void getAllAlLexFergsBySlugContainsSomething() throws Exception {
        // Initialize the database
        insertedAlLexFerg = alLexFergRepository.saveAndFlush(alLexFerg);

        // Get all the alLexFergList where slug contains
        defaultAlLexFergFiltering("slug.contains=" + DEFAULT_SLUG, "slug.contains=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllAlLexFergsBySlugNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlLexFerg = alLexFergRepository.saveAndFlush(alLexFerg);

        // Get all the alLexFergList where slug does not contain
        defaultAlLexFergFiltering("slug.doesNotContain=" + UPDATED_SLUG, "slug.doesNotContain=" + DEFAULT_SLUG);
    }

    @Test
    @Transactional
    void getAllAlLexFergsBySummaryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLexFerg = alLexFergRepository.saveAndFlush(alLexFerg);

        // Get all the alLexFergList where summary equals to
        defaultAlLexFergFiltering("summary.equals=" + DEFAULT_SUMMARY, "summary.equals=" + UPDATED_SUMMARY);
    }

    @Test
    @Transactional
    void getAllAlLexFergsBySummaryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlLexFerg = alLexFergRepository.saveAndFlush(alLexFerg);

        // Get all the alLexFergList where summary in
        defaultAlLexFergFiltering("summary.in=" + DEFAULT_SUMMARY + "," + UPDATED_SUMMARY, "summary.in=" + UPDATED_SUMMARY);
    }

    @Test
    @Transactional
    void getAllAlLexFergsBySummaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlLexFerg = alLexFergRepository.saveAndFlush(alLexFerg);

        // Get all the alLexFergList where summary is not null
        defaultAlLexFergFiltering("summary.specified=true", "summary.specified=false");
    }

    @Test
    @Transactional
    void getAllAlLexFergsBySummaryContainsSomething() throws Exception {
        // Initialize the database
        insertedAlLexFerg = alLexFergRepository.saveAndFlush(alLexFerg);

        // Get all the alLexFergList where summary contains
        defaultAlLexFergFiltering("summary.contains=" + DEFAULT_SUMMARY, "summary.contains=" + UPDATED_SUMMARY);
    }

    @Test
    @Transactional
    void getAllAlLexFergsBySummaryNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlLexFerg = alLexFergRepository.saveAndFlush(alLexFerg);

        // Get all the alLexFergList where summary does not contain
        defaultAlLexFergFiltering("summary.doesNotContain=" + UPDATED_SUMMARY, "summary.doesNotContain=" + DEFAULT_SUMMARY);
    }

    @Test
    @Transactional
    void getAllAlLexFergsByContentHeitigaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLexFerg = alLexFergRepository.saveAndFlush(alLexFerg);

        // Get all the alLexFergList where contentHeitiga equals to
        defaultAlLexFergFiltering("contentHeitiga.equals=" + DEFAULT_CONTENT_HEITIGA, "contentHeitiga.equals=" + UPDATED_CONTENT_HEITIGA);
    }

    @Test
    @Transactional
    void getAllAlLexFergsByContentHeitigaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlLexFerg = alLexFergRepository.saveAndFlush(alLexFerg);

        // Get all the alLexFergList where contentHeitiga in
        defaultAlLexFergFiltering(
            "contentHeitiga.in=" + DEFAULT_CONTENT_HEITIGA + "," + UPDATED_CONTENT_HEITIGA,
            "contentHeitiga.in=" + UPDATED_CONTENT_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlLexFergsByContentHeitigaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlLexFerg = alLexFergRepository.saveAndFlush(alLexFerg);

        // Get all the alLexFergList where contentHeitiga is not null
        defaultAlLexFergFiltering("contentHeitiga.specified=true", "contentHeitiga.specified=false");
    }

    @Test
    @Transactional
    void getAllAlLexFergsByContentHeitigaContainsSomething() throws Exception {
        // Initialize the database
        insertedAlLexFerg = alLexFergRepository.saveAndFlush(alLexFerg);

        // Get all the alLexFergList where contentHeitiga contains
        defaultAlLexFergFiltering(
            "contentHeitiga.contains=" + DEFAULT_CONTENT_HEITIGA,
            "contentHeitiga.contains=" + UPDATED_CONTENT_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlLexFergsByContentHeitigaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlLexFerg = alLexFergRepository.saveAndFlush(alLexFerg);

        // Get all the alLexFergList where contentHeitiga does not contain
        defaultAlLexFergFiltering(
            "contentHeitiga.doesNotContain=" + UPDATED_CONTENT_HEITIGA,
            "contentHeitiga.doesNotContain=" + DEFAULT_CONTENT_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlLexFergsByPublicationStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLexFerg = alLexFergRepository.saveAndFlush(alLexFerg);

        // Get all the alLexFergList where publicationStatus equals to
        defaultAlLexFergFiltering(
            "publicationStatus.equals=" + DEFAULT_PUBLICATION_STATUS,
            "publicationStatus.equals=" + UPDATED_PUBLICATION_STATUS
        );
    }

    @Test
    @Transactional
    void getAllAlLexFergsByPublicationStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlLexFerg = alLexFergRepository.saveAndFlush(alLexFerg);

        // Get all the alLexFergList where publicationStatus in
        defaultAlLexFergFiltering(
            "publicationStatus.in=" + DEFAULT_PUBLICATION_STATUS + "," + UPDATED_PUBLICATION_STATUS,
            "publicationStatus.in=" + UPDATED_PUBLICATION_STATUS
        );
    }

    @Test
    @Transactional
    void getAllAlLexFergsByPublicationStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlLexFerg = alLexFergRepository.saveAndFlush(alLexFerg);

        // Get all the alLexFergList where publicationStatus is not null
        defaultAlLexFergFiltering("publicationStatus.specified=true", "publicationStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllAlLexFergsByPublishedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLexFerg = alLexFergRepository.saveAndFlush(alLexFerg);

        // Get all the alLexFergList where publishedDate equals to
        defaultAlLexFergFiltering("publishedDate.equals=" + DEFAULT_PUBLISHED_DATE, "publishedDate.equals=" + UPDATED_PUBLISHED_DATE);
    }

    @Test
    @Transactional
    void getAllAlLexFergsByPublishedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlLexFerg = alLexFergRepository.saveAndFlush(alLexFerg);

        // Get all the alLexFergList where publishedDate in
        defaultAlLexFergFiltering(
            "publishedDate.in=" + DEFAULT_PUBLISHED_DATE + "," + UPDATED_PUBLISHED_DATE,
            "publishedDate.in=" + UPDATED_PUBLISHED_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlLexFergsByPublishedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlLexFerg = alLexFergRepository.saveAndFlush(alLexFerg);

        // Get all the alLexFergList where publishedDate is not null
        defaultAlLexFergFiltering("publishedDate.specified=true", "publishedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAlLexFergsByAvatarIsEqualToSomething() throws Exception {
        Metaverse avatar;
        if (TestUtil.findAll(em, Metaverse.class).isEmpty()) {
            alLexFergRepository.saveAndFlush(alLexFerg);
            avatar = MetaverseResourceIT.createEntity();
        } else {
            avatar = TestUtil.findAll(em, Metaverse.class).get(0);
        }
        em.persist(avatar);
        em.flush();
        alLexFerg.setAvatar(avatar);
        alLexFergRepository.saveAndFlush(alLexFerg);
        Long avatarId = avatar.getId();
        // Get all the alLexFergList where avatar equals to avatarId
        defaultAlLexFergShouldBeFound("avatarId.equals=" + avatarId);

        // Get all the alLexFergList where avatar equals to (avatarId + 1)
        defaultAlLexFergShouldNotBeFound("avatarId.equals=" + (avatarId + 1));
    }

    @Test
    @Transactional
    void getAllAlLexFergsByCategoryIsEqualToSomething() throws Exception {
        AlCatalina category;
        if (TestUtil.findAll(em, AlCatalina.class).isEmpty()) {
            alLexFergRepository.saveAndFlush(alLexFerg);
            category = AlCatalinaResourceIT.createEntity();
        } else {
            category = TestUtil.findAll(em, AlCatalina.class).get(0);
        }
        em.persist(category);
        em.flush();
        alLexFerg.setCategory(category);
        alLexFergRepository.saveAndFlush(alLexFerg);
        Long categoryId = category.getId();
        // Get all the alLexFergList where category equals to categoryId
        defaultAlLexFergShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the alLexFergList where category equals to (categoryId + 1)
        defaultAlLexFergShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllAlLexFergsByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alLexFergRepository.saveAndFlush(alLexFerg);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alLexFerg.setApplication(application);
        alLexFergRepository.saveAndFlush(alLexFerg);
        UUID applicationId = application.getId();
        // Get all the alLexFergList where application equals to applicationId
        defaultAlLexFergShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alLexFergList where application equals to UUID.randomUUID()
        defaultAlLexFergShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlLexFergFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlLexFergShouldBeFound(shouldBeFound);
        defaultAlLexFergShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlLexFergShouldBeFound(String filter) throws Exception {
        restAlLexFergMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alLexFerg.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].summary").value(hasItem(DEFAULT_SUMMARY)))
            .andExpect(jsonPath("$.[*].contentHeitiga").value(hasItem(DEFAULT_CONTENT_HEITIGA)))
            .andExpect(jsonPath("$.[*].publicationStatus").value(hasItem(DEFAULT_PUBLICATION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].publishedDate").value(hasItem(DEFAULT_PUBLISHED_DATE.toString())));

        // Check, that the count call also returns 1
        restAlLexFergMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlLexFergShouldNotBeFound(String filter) throws Exception {
        restAlLexFergMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlLexFergMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlLexFerg() throws Exception {
        // Get the alLexFerg
        restAlLexFergMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlLexFerg() throws Exception {
        // Initialize the database
        insertedAlLexFerg = alLexFergRepository.saveAndFlush(alLexFerg);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alLexFerg
        AlLexFerg updatedAlLexFerg = alLexFergRepository.findById(alLexFerg.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlLexFerg are not directly saved in db
        em.detach(updatedAlLexFerg);
        updatedAlLexFerg
            .title(UPDATED_TITLE)
            .slug(UPDATED_SLUG)
            .summary(UPDATED_SUMMARY)
            .contentHeitiga(UPDATED_CONTENT_HEITIGA)
            .publicationStatus(UPDATED_PUBLICATION_STATUS)
            .publishedDate(UPDATED_PUBLISHED_DATE);
        AlLexFergDTO alLexFergDTO = alLexFergMapper.toDto(updatedAlLexFerg);

        restAlLexFergMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alLexFergDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alLexFergDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlLexFerg in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlLexFergToMatchAllProperties(updatedAlLexFerg);
    }

    @Test
    @Transactional
    void putNonExistingAlLexFerg() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLexFerg.setId(longCount.incrementAndGet());

        // Create the AlLexFerg
        AlLexFergDTO alLexFergDTO = alLexFergMapper.toDto(alLexFerg);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlLexFergMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alLexFergDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alLexFergDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlLexFerg in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlLexFerg() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLexFerg.setId(longCount.incrementAndGet());

        // Create the AlLexFerg
        AlLexFergDTO alLexFergDTO = alLexFergMapper.toDto(alLexFerg);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlLexFergMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alLexFergDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlLexFerg in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlLexFerg() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLexFerg.setId(longCount.incrementAndGet());

        // Create the AlLexFerg
        AlLexFergDTO alLexFergDTO = alLexFergMapper.toDto(alLexFerg);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlLexFergMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alLexFergDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlLexFerg in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlLexFergWithPatch() throws Exception {
        // Initialize the database
        insertedAlLexFerg = alLexFergRepository.saveAndFlush(alLexFerg);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alLexFerg using partial update
        AlLexFerg partialUpdatedAlLexFerg = new AlLexFerg();
        partialUpdatedAlLexFerg.setId(alLexFerg.getId());

        partialUpdatedAlLexFerg.title(UPDATED_TITLE).summary(UPDATED_SUMMARY).publicationStatus(UPDATED_PUBLICATION_STATUS);

        restAlLexFergMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlLexFerg.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlLexFerg))
            )
            .andExpect(status().isOk());

        // Validate the AlLexFerg in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlLexFergUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlLexFerg, alLexFerg),
            getPersistedAlLexFerg(alLexFerg)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlLexFergWithPatch() throws Exception {
        // Initialize the database
        insertedAlLexFerg = alLexFergRepository.saveAndFlush(alLexFerg);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alLexFerg using partial update
        AlLexFerg partialUpdatedAlLexFerg = new AlLexFerg();
        partialUpdatedAlLexFerg.setId(alLexFerg.getId());

        partialUpdatedAlLexFerg
            .title(UPDATED_TITLE)
            .slug(UPDATED_SLUG)
            .summary(UPDATED_SUMMARY)
            .contentHeitiga(UPDATED_CONTENT_HEITIGA)
            .publicationStatus(UPDATED_PUBLICATION_STATUS)
            .publishedDate(UPDATED_PUBLISHED_DATE);

        restAlLexFergMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlLexFerg.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlLexFerg))
            )
            .andExpect(status().isOk());

        // Validate the AlLexFerg in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlLexFergUpdatableFieldsEquals(partialUpdatedAlLexFerg, getPersistedAlLexFerg(partialUpdatedAlLexFerg));
    }

    @Test
    @Transactional
    void patchNonExistingAlLexFerg() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLexFerg.setId(longCount.incrementAndGet());

        // Create the AlLexFerg
        AlLexFergDTO alLexFergDTO = alLexFergMapper.toDto(alLexFerg);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlLexFergMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alLexFergDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alLexFergDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlLexFerg in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlLexFerg() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLexFerg.setId(longCount.incrementAndGet());

        // Create the AlLexFerg
        AlLexFergDTO alLexFergDTO = alLexFergMapper.toDto(alLexFerg);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlLexFergMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alLexFergDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlLexFerg in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlLexFerg() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLexFerg.setId(longCount.incrementAndGet());

        // Create the AlLexFerg
        AlLexFergDTO alLexFergDTO = alLexFergMapper.toDto(alLexFerg);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlLexFergMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alLexFergDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlLexFerg in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlLexFerg() throws Exception {
        // Initialize the database
        insertedAlLexFerg = alLexFergRepository.saveAndFlush(alLexFerg);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alLexFerg
        restAlLexFergMockMvc
            .perform(delete(ENTITY_API_URL_ID, alLexFerg.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alLexFergRepository.count();
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

    protected AlLexFerg getPersistedAlLexFerg(AlLexFerg alLexFerg) {
        return alLexFergRepository.findById(alLexFerg.getId()).orElseThrow();
    }

    protected void assertPersistedAlLexFergToMatchAllProperties(AlLexFerg expectedAlLexFerg) {
        assertAlLexFergAllPropertiesEquals(expectedAlLexFerg, getPersistedAlLexFerg(expectedAlLexFerg));
    }

    protected void assertPersistedAlLexFergToMatchUpdatableProperties(AlLexFerg expectedAlLexFerg) {
        assertAlLexFergAllUpdatablePropertiesEquals(expectedAlLexFerg, getPersistedAlLexFerg(expectedAlLexFerg));
    }
}
