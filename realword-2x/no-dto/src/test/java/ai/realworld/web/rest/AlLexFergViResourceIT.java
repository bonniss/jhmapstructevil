package ai.realworld.web.rest;

import static ai.realworld.domain.AlLexFergViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlLexFergVi;
import ai.realworld.domain.enumeration.PaoloStatus;
import ai.realworld.repository.AlLexFergViRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
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
 * Integration tests for the {@link AlLexFergViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlLexFergViResourceIT {

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

    private static final String ENTITY_API_URL = "/api/al-lex-ferg-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlLexFergViRepository alLexFergViRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlLexFergViMockMvc;

    private AlLexFergVi alLexFergVi;

    private AlLexFergVi insertedAlLexFergVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlLexFergVi createEntity() {
        return new AlLexFergVi()
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
    public static AlLexFergVi createUpdatedEntity() {
        return new AlLexFergVi()
            .title(UPDATED_TITLE)
            .slug(UPDATED_SLUG)
            .summary(UPDATED_SUMMARY)
            .contentHeitiga(UPDATED_CONTENT_HEITIGA)
            .publicationStatus(UPDATED_PUBLICATION_STATUS)
            .publishedDate(UPDATED_PUBLISHED_DATE);
    }

    @BeforeEach
    public void initTest() {
        alLexFergVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlLexFergVi != null) {
            alLexFergViRepository.delete(insertedAlLexFergVi);
            insertedAlLexFergVi = null;
        }
    }

    @Test
    @Transactional
    void createAlLexFergVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlLexFergVi
        var returnedAlLexFergVi = om.readValue(
            restAlLexFergViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alLexFergVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlLexFergVi.class
        );

        // Validate the AlLexFergVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlLexFergViUpdatableFieldsEquals(returnedAlLexFergVi, getPersistedAlLexFergVi(returnedAlLexFergVi));

        insertedAlLexFergVi = returnedAlLexFergVi;
    }

    @Test
    @Transactional
    void createAlLexFergViWithExistingId() throws Exception {
        // Create the AlLexFergVi with an existing ID
        alLexFergVi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlLexFergViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alLexFergVi)))
            .andExpect(status().isBadRequest());

        // Validate the AlLexFergVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alLexFergVi.setTitle(null);

        // Create the AlLexFergVi, which fails.

        restAlLexFergViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alLexFergVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSlugIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alLexFergVi.setSlug(null);

        // Create the AlLexFergVi, which fails.

        restAlLexFergViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alLexFergVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlLexFergVis() throws Exception {
        // Initialize the database
        insertedAlLexFergVi = alLexFergViRepository.saveAndFlush(alLexFergVi);

        // Get all the alLexFergViList
        restAlLexFergViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alLexFergVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].summary").value(hasItem(DEFAULT_SUMMARY)))
            .andExpect(jsonPath("$.[*].contentHeitiga").value(hasItem(DEFAULT_CONTENT_HEITIGA)))
            .andExpect(jsonPath("$.[*].publicationStatus").value(hasItem(DEFAULT_PUBLICATION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].publishedDate").value(hasItem(DEFAULT_PUBLISHED_DATE.toString())));
    }

    @Test
    @Transactional
    void getAlLexFergVi() throws Exception {
        // Initialize the database
        insertedAlLexFergVi = alLexFergViRepository.saveAndFlush(alLexFergVi);

        // Get the alLexFergVi
        restAlLexFergViMockMvc
            .perform(get(ENTITY_API_URL_ID, alLexFergVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alLexFergVi.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG))
            .andExpect(jsonPath("$.summary").value(DEFAULT_SUMMARY))
            .andExpect(jsonPath("$.contentHeitiga").value(DEFAULT_CONTENT_HEITIGA))
            .andExpect(jsonPath("$.publicationStatus").value(DEFAULT_PUBLICATION_STATUS.toString()))
            .andExpect(jsonPath("$.publishedDate").value(DEFAULT_PUBLISHED_DATE.toString()));
    }

    @Test
    @Transactional
    void getAlLexFergVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlLexFergVi = alLexFergViRepository.saveAndFlush(alLexFergVi);

        Long id = alLexFergVi.getId();

        defaultAlLexFergViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlLexFergViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlLexFergViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlLexFergVisByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLexFergVi = alLexFergViRepository.saveAndFlush(alLexFergVi);

        // Get all the alLexFergViList where title equals to
        defaultAlLexFergViFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAlLexFergVisByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlLexFergVi = alLexFergViRepository.saveAndFlush(alLexFergVi);

        // Get all the alLexFergViList where title in
        defaultAlLexFergViFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAlLexFergVisByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlLexFergVi = alLexFergViRepository.saveAndFlush(alLexFergVi);

        // Get all the alLexFergViList where title is not null
        defaultAlLexFergViFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllAlLexFergVisByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedAlLexFergVi = alLexFergViRepository.saveAndFlush(alLexFergVi);

        // Get all the alLexFergViList where title contains
        defaultAlLexFergViFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAlLexFergVisByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlLexFergVi = alLexFergViRepository.saveAndFlush(alLexFergVi);

        // Get all the alLexFergViList where title does not contain
        defaultAlLexFergViFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllAlLexFergVisBySlugIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLexFergVi = alLexFergViRepository.saveAndFlush(alLexFergVi);

        // Get all the alLexFergViList where slug equals to
        defaultAlLexFergViFiltering("slug.equals=" + DEFAULT_SLUG, "slug.equals=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllAlLexFergVisBySlugIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlLexFergVi = alLexFergViRepository.saveAndFlush(alLexFergVi);

        // Get all the alLexFergViList where slug in
        defaultAlLexFergViFiltering("slug.in=" + DEFAULT_SLUG + "," + UPDATED_SLUG, "slug.in=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllAlLexFergVisBySlugIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlLexFergVi = alLexFergViRepository.saveAndFlush(alLexFergVi);

        // Get all the alLexFergViList where slug is not null
        defaultAlLexFergViFiltering("slug.specified=true", "slug.specified=false");
    }

    @Test
    @Transactional
    void getAllAlLexFergVisBySlugContainsSomething() throws Exception {
        // Initialize the database
        insertedAlLexFergVi = alLexFergViRepository.saveAndFlush(alLexFergVi);

        // Get all the alLexFergViList where slug contains
        defaultAlLexFergViFiltering("slug.contains=" + DEFAULT_SLUG, "slug.contains=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllAlLexFergVisBySlugNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlLexFergVi = alLexFergViRepository.saveAndFlush(alLexFergVi);

        // Get all the alLexFergViList where slug does not contain
        defaultAlLexFergViFiltering("slug.doesNotContain=" + UPDATED_SLUG, "slug.doesNotContain=" + DEFAULT_SLUG);
    }

    @Test
    @Transactional
    void getAllAlLexFergVisBySummaryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLexFergVi = alLexFergViRepository.saveAndFlush(alLexFergVi);

        // Get all the alLexFergViList where summary equals to
        defaultAlLexFergViFiltering("summary.equals=" + DEFAULT_SUMMARY, "summary.equals=" + UPDATED_SUMMARY);
    }

    @Test
    @Transactional
    void getAllAlLexFergVisBySummaryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlLexFergVi = alLexFergViRepository.saveAndFlush(alLexFergVi);

        // Get all the alLexFergViList where summary in
        defaultAlLexFergViFiltering("summary.in=" + DEFAULT_SUMMARY + "," + UPDATED_SUMMARY, "summary.in=" + UPDATED_SUMMARY);
    }

    @Test
    @Transactional
    void getAllAlLexFergVisBySummaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlLexFergVi = alLexFergViRepository.saveAndFlush(alLexFergVi);

        // Get all the alLexFergViList where summary is not null
        defaultAlLexFergViFiltering("summary.specified=true", "summary.specified=false");
    }

    @Test
    @Transactional
    void getAllAlLexFergVisBySummaryContainsSomething() throws Exception {
        // Initialize the database
        insertedAlLexFergVi = alLexFergViRepository.saveAndFlush(alLexFergVi);

        // Get all the alLexFergViList where summary contains
        defaultAlLexFergViFiltering("summary.contains=" + DEFAULT_SUMMARY, "summary.contains=" + UPDATED_SUMMARY);
    }

    @Test
    @Transactional
    void getAllAlLexFergVisBySummaryNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlLexFergVi = alLexFergViRepository.saveAndFlush(alLexFergVi);

        // Get all the alLexFergViList where summary does not contain
        defaultAlLexFergViFiltering("summary.doesNotContain=" + UPDATED_SUMMARY, "summary.doesNotContain=" + DEFAULT_SUMMARY);
    }

    @Test
    @Transactional
    void getAllAlLexFergVisByContentHeitigaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLexFergVi = alLexFergViRepository.saveAndFlush(alLexFergVi);

        // Get all the alLexFergViList where contentHeitiga equals to
        defaultAlLexFergViFiltering("contentHeitiga.equals=" + DEFAULT_CONTENT_HEITIGA, "contentHeitiga.equals=" + UPDATED_CONTENT_HEITIGA);
    }

    @Test
    @Transactional
    void getAllAlLexFergVisByContentHeitigaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlLexFergVi = alLexFergViRepository.saveAndFlush(alLexFergVi);

        // Get all the alLexFergViList where contentHeitiga in
        defaultAlLexFergViFiltering(
            "contentHeitiga.in=" + DEFAULT_CONTENT_HEITIGA + "," + UPDATED_CONTENT_HEITIGA,
            "contentHeitiga.in=" + UPDATED_CONTENT_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlLexFergVisByContentHeitigaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlLexFergVi = alLexFergViRepository.saveAndFlush(alLexFergVi);

        // Get all the alLexFergViList where contentHeitiga is not null
        defaultAlLexFergViFiltering("contentHeitiga.specified=true", "contentHeitiga.specified=false");
    }

    @Test
    @Transactional
    void getAllAlLexFergVisByContentHeitigaContainsSomething() throws Exception {
        // Initialize the database
        insertedAlLexFergVi = alLexFergViRepository.saveAndFlush(alLexFergVi);

        // Get all the alLexFergViList where contentHeitiga contains
        defaultAlLexFergViFiltering(
            "contentHeitiga.contains=" + DEFAULT_CONTENT_HEITIGA,
            "contentHeitiga.contains=" + UPDATED_CONTENT_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlLexFergVisByContentHeitigaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlLexFergVi = alLexFergViRepository.saveAndFlush(alLexFergVi);

        // Get all the alLexFergViList where contentHeitiga does not contain
        defaultAlLexFergViFiltering(
            "contentHeitiga.doesNotContain=" + UPDATED_CONTENT_HEITIGA,
            "contentHeitiga.doesNotContain=" + DEFAULT_CONTENT_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlLexFergVisByPublicationStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLexFergVi = alLexFergViRepository.saveAndFlush(alLexFergVi);

        // Get all the alLexFergViList where publicationStatus equals to
        defaultAlLexFergViFiltering(
            "publicationStatus.equals=" + DEFAULT_PUBLICATION_STATUS,
            "publicationStatus.equals=" + UPDATED_PUBLICATION_STATUS
        );
    }

    @Test
    @Transactional
    void getAllAlLexFergVisByPublicationStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlLexFergVi = alLexFergViRepository.saveAndFlush(alLexFergVi);

        // Get all the alLexFergViList where publicationStatus in
        defaultAlLexFergViFiltering(
            "publicationStatus.in=" + DEFAULT_PUBLICATION_STATUS + "," + UPDATED_PUBLICATION_STATUS,
            "publicationStatus.in=" + UPDATED_PUBLICATION_STATUS
        );
    }

    @Test
    @Transactional
    void getAllAlLexFergVisByPublicationStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlLexFergVi = alLexFergViRepository.saveAndFlush(alLexFergVi);

        // Get all the alLexFergViList where publicationStatus is not null
        defaultAlLexFergViFiltering("publicationStatus.specified=true", "publicationStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllAlLexFergVisByPublishedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLexFergVi = alLexFergViRepository.saveAndFlush(alLexFergVi);

        // Get all the alLexFergViList where publishedDate equals to
        defaultAlLexFergViFiltering("publishedDate.equals=" + DEFAULT_PUBLISHED_DATE, "publishedDate.equals=" + UPDATED_PUBLISHED_DATE);
    }

    @Test
    @Transactional
    void getAllAlLexFergVisByPublishedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlLexFergVi = alLexFergViRepository.saveAndFlush(alLexFergVi);

        // Get all the alLexFergViList where publishedDate in
        defaultAlLexFergViFiltering(
            "publishedDate.in=" + DEFAULT_PUBLISHED_DATE + "," + UPDATED_PUBLISHED_DATE,
            "publishedDate.in=" + UPDATED_PUBLISHED_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlLexFergVisByPublishedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlLexFergVi = alLexFergViRepository.saveAndFlush(alLexFergVi);

        // Get all the alLexFergViList where publishedDate is not null
        defaultAlLexFergViFiltering("publishedDate.specified=true", "publishedDate.specified=false");
    }

    private void defaultAlLexFergViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlLexFergViShouldBeFound(shouldBeFound);
        defaultAlLexFergViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlLexFergViShouldBeFound(String filter) throws Exception {
        restAlLexFergViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alLexFergVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].summary").value(hasItem(DEFAULT_SUMMARY)))
            .andExpect(jsonPath("$.[*].contentHeitiga").value(hasItem(DEFAULT_CONTENT_HEITIGA)))
            .andExpect(jsonPath("$.[*].publicationStatus").value(hasItem(DEFAULT_PUBLICATION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].publishedDate").value(hasItem(DEFAULT_PUBLISHED_DATE.toString())));

        // Check, that the count call also returns 1
        restAlLexFergViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlLexFergViShouldNotBeFound(String filter) throws Exception {
        restAlLexFergViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlLexFergViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlLexFergVi() throws Exception {
        // Get the alLexFergVi
        restAlLexFergViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlLexFergVi() throws Exception {
        // Initialize the database
        insertedAlLexFergVi = alLexFergViRepository.saveAndFlush(alLexFergVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alLexFergVi
        AlLexFergVi updatedAlLexFergVi = alLexFergViRepository.findById(alLexFergVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlLexFergVi are not directly saved in db
        em.detach(updatedAlLexFergVi);
        updatedAlLexFergVi
            .title(UPDATED_TITLE)
            .slug(UPDATED_SLUG)
            .summary(UPDATED_SUMMARY)
            .contentHeitiga(UPDATED_CONTENT_HEITIGA)
            .publicationStatus(UPDATED_PUBLICATION_STATUS)
            .publishedDate(UPDATED_PUBLISHED_DATE);

        restAlLexFergViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlLexFergVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlLexFergVi))
            )
            .andExpect(status().isOk());

        // Validate the AlLexFergVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlLexFergViToMatchAllProperties(updatedAlLexFergVi);
    }

    @Test
    @Transactional
    void putNonExistingAlLexFergVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLexFergVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlLexFergViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alLexFergVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alLexFergVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlLexFergVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlLexFergVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLexFergVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlLexFergViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alLexFergVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlLexFergVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlLexFergVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLexFergVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlLexFergViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alLexFergVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlLexFergVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlLexFergViWithPatch() throws Exception {
        // Initialize the database
        insertedAlLexFergVi = alLexFergViRepository.saveAndFlush(alLexFergVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alLexFergVi using partial update
        AlLexFergVi partialUpdatedAlLexFergVi = new AlLexFergVi();
        partialUpdatedAlLexFergVi.setId(alLexFergVi.getId());

        partialUpdatedAlLexFergVi
            .title(UPDATED_TITLE)
            .contentHeitiga(UPDATED_CONTENT_HEITIGA)
            .publicationStatus(UPDATED_PUBLICATION_STATUS);

        restAlLexFergViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlLexFergVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlLexFergVi))
            )
            .andExpect(status().isOk());

        // Validate the AlLexFergVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlLexFergViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlLexFergVi, alLexFergVi),
            getPersistedAlLexFergVi(alLexFergVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlLexFergViWithPatch() throws Exception {
        // Initialize the database
        insertedAlLexFergVi = alLexFergViRepository.saveAndFlush(alLexFergVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alLexFergVi using partial update
        AlLexFergVi partialUpdatedAlLexFergVi = new AlLexFergVi();
        partialUpdatedAlLexFergVi.setId(alLexFergVi.getId());

        partialUpdatedAlLexFergVi
            .title(UPDATED_TITLE)
            .slug(UPDATED_SLUG)
            .summary(UPDATED_SUMMARY)
            .contentHeitiga(UPDATED_CONTENT_HEITIGA)
            .publicationStatus(UPDATED_PUBLICATION_STATUS)
            .publishedDate(UPDATED_PUBLISHED_DATE);

        restAlLexFergViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlLexFergVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlLexFergVi))
            )
            .andExpect(status().isOk());

        // Validate the AlLexFergVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlLexFergViUpdatableFieldsEquals(partialUpdatedAlLexFergVi, getPersistedAlLexFergVi(partialUpdatedAlLexFergVi));
    }

    @Test
    @Transactional
    void patchNonExistingAlLexFergVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLexFergVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlLexFergViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alLexFergVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alLexFergVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlLexFergVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlLexFergVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLexFergVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlLexFergViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alLexFergVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlLexFergVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlLexFergVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLexFergVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlLexFergViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alLexFergVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlLexFergVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlLexFergVi() throws Exception {
        // Initialize the database
        insertedAlLexFergVi = alLexFergViRepository.saveAndFlush(alLexFergVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alLexFergVi
        restAlLexFergViMockMvc
            .perform(delete(ENTITY_API_URL_ID, alLexFergVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alLexFergViRepository.count();
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

    protected AlLexFergVi getPersistedAlLexFergVi(AlLexFergVi alLexFergVi) {
        return alLexFergViRepository.findById(alLexFergVi.getId()).orElseThrow();
    }

    protected void assertPersistedAlLexFergViToMatchAllProperties(AlLexFergVi expectedAlLexFergVi) {
        assertAlLexFergViAllPropertiesEquals(expectedAlLexFergVi, getPersistedAlLexFergVi(expectedAlLexFergVi));
    }

    protected void assertPersistedAlLexFergViToMatchUpdatableProperties(AlLexFergVi expectedAlLexFergVi) {
        assertAlLexFergViAllUpdatablePropertiesEquals(expectedAlLexFergVi, getPersistedAlLexFergVi(expectedAlLexFergVi));
    }
}
