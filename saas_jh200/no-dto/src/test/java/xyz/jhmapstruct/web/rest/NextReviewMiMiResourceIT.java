package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextReviewMiMiAsserts.*;
import static xyz.jhmapstruct.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.IntegrationTest;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextProductMiMi;
import xyz.jhmapstruct.domain.NextReviewMiMi;
import xyz.jhmapstruct.repository.NextReviewMiMiRepository;
import xyz.jhmapstruct.service.NextReviewMiMiService;

/**
 * Integration tests for the {@link NextReviewMiMiResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextReviewMiMiResourceIT {

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;
    private static final Integer SMALLER_RATING = 1 - 1;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_REVIEW_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REVIEW_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/next-review-mi-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextReviewMiMiRepository nextReviewMiMiRepository;

    @Mock
    private NextReviewMiMiRepository nextReviewMiMiRepositoryMock;

    @Mock
    private NextReviewMiMiService nextReviewMiMiServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextReviewMiMiMockMvc;

    private NextReviewMiMi nextReviewMiMi;

    private NextReviewMiMi insertedNextReviewMiMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextReviewMiMi createEntity() {
        return new NextReviewMiMi().rating(DEFAULT_RATING).comment(DEFAULT_COMMENT).reviewDate(DEFAULT_REVIEW_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextReviewMiMi createUpdatedEntity() {
        return new NextReviewMiMi().rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);
    }

    @BeforeEach
    public void initTest() {
        nextReviewMiMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextReviewMiMi != null) {
            nextReviewMiMiRepository.delete(insertedNextReviewMiMi);
            insertedNextReviewMiMi = null;
        }
    }

    @Test
    @Transactional
    void createNextReviewMiMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextReviewMiMi
        var returnedNextReviewMiMi = om.readValue(
            restNextReviewMiMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewMiMi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextReviewMiMi.class
        );

        // Validate the NextReviewMiMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextReviewMiMiUpdatableFieldsEquals(returnedNextReviewMiMi, getPersistedNextReviewMiMi(returnedNextReviewMiMi));

        insertedNextReviewMiMi = returnedNextReviewMiMi;
    }

    @Test
    @Transactional
    void createNextReviewMiMiWithExistingId() throws Exception {
        // Create the NextReviewMiMi with an existing ID
        nextReviewMiMi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextReviewMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewMiMi)))
            .andExpect(status().isBadRequest());

        // Validate the NextReviewMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRatingIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextReviewMiMi.setRating(null);

        // Create the NextReviewMiMi, which fails.

        restNextReviewMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewMiMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReviewDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextReviewMiMi.setReviewDate(null);

        // Create the NextReviewMiMi, which fails.

        restNextReviewMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewMiMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextReviewMiMis() throws Exception {
        // Initialize the database
        insertedNextReviewMiMi = nextReviewMiMiRepository.saveAndFlush(nextReviewMiMi);

        // Get all the nextReviewMiMiList
        restNextReviewMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextReviewMiMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextReviewMiMisWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextReviewMiMiServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextReviewMiMiMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextReviewMiMiServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextReviewMiMisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextReviewMiMiServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextReviewMiMiMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextReviewMiMiRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextReviewMiMi() throws Exception {
        // Initialize the database
        insertedNextReviewMiMi = nextReviewMiMiRepository.saveAndFlush(nextReviewMiMi);

        // Get the nextReviewMiMi
        restNextReviewMiMiMockMvc
            .perform(get(ENTITY_API_URL_ID, nextReviewMiMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextReviewMiMi.getId().intValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.reviewDate").value(DEFAULT_REVIEW_DATE.toString()));
    }

    @Test
    @Transactional
    void getNextReviewMiMisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextReviewMiMi = nextReviewMiMiRepository.saveAndFlush(nextReviewMiMi);

        Long id = nextReviewMiMi.getId();

        defaultNextReviewMiMiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextReviewMiMiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextReviewMiMiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextReviewMiMisByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReviewMiMi = nextReviewMiMiRepository.saveAndFlush(nextReviewMiMi);

        // Get all the nextReviewMiMiList where rating equals to
        defaultNextReviewMiMiFiltering("rating.equals=" + DEFAULT_RATING, "rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewMiMisByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextReviewMiMi = nextReviewMiMiRepository.saveAndFlush(nextReviewMiMi);

        // Get all the nextReviewMiMiList where rating in
        defaultNextReviewMiMiFiltering("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING, "rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewMiMisByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextReviewMiMi = nextReviewMiMiRepository.saveAndFlush(nextReviewMiMi);

        // Get all the nextReviewMiMiList where rating is not null
        defaultNextReviewMiMiFiltering("rating.specified=true", "rating.specified=false");
    }

    @Test
    @Transactional
    void getAllNextReviewMiMisByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReviewMiMi = nextReviewMiMiRepository.saveAndFlush(nextReviewMiMi);

        // Get all the nextReviewMiMiList where rating is greater than or equal to
        defaultNextReviewMiMiFiltering("rating.greaterThanOrEqual=" + DEFAULT_RATING, "rating.greaterThanOrEqual=" + (DEFAULT_RATING + 1));
    }

    @Test
    @Transactional
    void getAllNextReviewMiMisByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReviewMiMi = nextReviewMiMiRepository.saveAndFlush(nextReviewMiMi);

        // Get all the nextReviewMiMiList where rating is less than or equal to
        defaultNextReviewMiMiFiltering("rating.lessThanOrEqual=" + DEFAULT_RATING, "rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewMiMisByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextReviewMiMi = nextReviewMiMiRepository.saveAndFlush(nextReviewMiMi);

        // Get all the nextReviewMiMiList where rating is less than
        defaultNextReviewMiMiFiltering("rating.lessThan=" + (DEFAULT_RATING + 1), "rating.lessThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewMiMisByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextReviewMiMi = nextReviewMiMiRepository.saveAndFlush(nextReviewMiMi);

        // Get all the nextReviewMiMiList where rating is greater than
        defaultNextReviewMiMiFiltering("rating.greaterThan=" + SMALLER_RATING, "rating.greaterThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewMiMisByReviewDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReviewMiMi = nextReviewMiMiRepository.saveAndFlush(nextReviewMiMi);

        // Get all the nextReviewMiMiList where reviewDate equals to
        defaultNextReviewMiMiFiltering("reviewDate.equals=" + DEFAULT_REVIEW_DATE, "reviewDate.equals=" + UPDATED_REVIEW_DATE);
    }

    @Test
    @Transactional
    void getAllNextReviewMiMisByReviewDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextReviewMiMi = nextReviewMiMiRepository.saveAndFlush(nextReviewMiMi);

        // Get all the nextReviewMiMiList where reviewDate in
        defaultNextReviewMiMiFiltering(
            "reviewDate.in=" + DEFAULT_REVIEW_DATE + "," + UPDATED_REVIEW_DATE,
            "reviewDate.in=" + UPDATED_REVIEW_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextReviewMiMisByReviewDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextReviewMiMi = nextReviewMiMiRepository.saveAndFlush(nextReviewMiMi);

        // Get all the nextReviewMiMiList where reviewDate is not null
        defaultNextReviewMiMiFiltering("reviewDate.specified=true", "reviewDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextReviewMiMisByProductIsEqualToSomething() throws Exception {
        NextProductMiMi product;
        if (TestUtil.findAll(em, NextProductMiMi.class).isEmpty()) {
            nextReviewMiMiRepository.saveAndFlush(nextReviewMiMi);
            product = NextProductMiMiResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, NextProductMiMi.class).get(0);
        }
        em.persist(product);
        em.flush();
        nextReviewMiMi.setProduct(product);
        nextReviewMiMiRepository.saveAndFlush(nextReviewMiMi);
        Long productId = product.getId();
        // Get all the nextReviewMiMiList where product equals to productId
        defaultNextReviewMiMiShouldBeFound("productId.equals=" + productId);

        // Get all the nextReviewMiMiList where product equals to (productId + 1)
        defaultNextReviewMiMiShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllNextReviewMiMisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextReviewMiMiRepository.saveAndFlush(nextReviewMiMi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextReviewMiMi.setTenant(tenant);
        nextReviewMiMiRepository.saveAndFlush(nextReviewMiMi);
        Long tenantId = tenant.getId();
        // Get all the nextReviewMiMiList where tenant equals to tenantId
        defaultNextReviewMiMiShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextReviewMiMiList where tenant equals to (tenantId + 1)
        defaultNextReviewMiMiShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextReviewMiMiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextReviewMiMiShouldBeFound(shouldBeFound);
        defaultNextReviewMiMiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextReviewMiMiShouldBeFound(String filter) throws Exception {
        restNextReviewMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextReviewMiMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));

        // Check, that the count call also returns 1
        restNextReviewMiMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextReviewMiMiShouldNotBeFound(String filter) throws Exception {
        restNextReviewMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextReviewMiMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextReviewMiMi() throws Exception {
        // Get the nextReviewMiMi
        restNextReviewMiMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextReviewMiMi() throws Exception {
        // Initialize the database
        insertedNextReviewMiMi = nextReviewMiMiRepository.saveAndFlush(nextReviewMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextReviewMiMi
        NextReviewMiMi updatedNextReviewMiMi = nextReviewMiMiRepository.findById(nextReviewMiMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextReviewMiMi are not directly saved in db
        em.detach(updatedNextReviewMiMi);
        updatedNextReviewMiMi.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);

        restNextReviewMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextReviewMiMi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextReviewMiMi))
            )
            .andExpect(status().isOk());

        // Validate the NextReviewMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextReviewMiMiToMatchAllProperties(updatedNextReviewMiMi);
    }

    @Test
    @Transactional
    void putNonExistingNextReviewMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewMiMi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextReviewMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextReviewMiMi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextReviewMiMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReviewMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextReviewMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewMiMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextReviewMiMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReviewMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextReviewMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewMiMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewMiMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewMiMi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextReviewMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextReviewMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextReviewMiMi = nextReviewMiMiRepository.saveAndFlush(nextReviewMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextReviewMiMi using partial update
        NextReviewMiMi partialUpdatedNextReviewMiMi = new NextReviewMiMi();
        partialUpdatedNextReviewMiMi.setId(nextReviewMiMi.getId());

        partialUpdatedNextReviewMiMi.rating(UPDATED_RATING).reviewDate(UPDATED_REVIEW_DATE);

        restNextReviewMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextReviewMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextReviewMiMi))
            )
            .andExpect(status().isOk());

        // Validate the NextReviewMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextReviewMiMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextReviewMiMi, nextReviewMiMi),
            getPersistedNextReviewMiMi(nextReviewMiMi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextReviewMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextReviewMiMi = nextReviewMiMiRepository.saveAndFlush(nextReviewMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextReviewMiMi using partial update
        NextReviewMiMi partialUpdatedNextReviewMiMi = new NextReviewMiMi();
        partialUpdatedNextReviewMiMi.setId(nextReviewMiMi.getId());

        partialUpdatedNextReviewMiMi.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);

        restNextReviewMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextReviewMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextReviewMiMi))
            )
            .andExpect(status().isOk());

        // Validate the NextReviewMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextReviewMiMiUpdatableFieldsEquals(partialUpdatedNextReviewMiMi, getPersistedNextReviewMiMi(partialUpdatedNextReviewMiMi));
    }

    @Test
    @Transactional
    void patchNonExistingNextReviewMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewMiMi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextReviewMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextReviewMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextReviewMiMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReviewMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextReviewMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewMiMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextReviewMiMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReviewMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextReviewMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewMiMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewMiMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextReviewMiMi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextReviewMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextReviewMiMi() throws Exception {
        // Initialize the database
        insertedNextReviewMiMi = nextReviewMiMiRepository.saveAndFlush(nextReviewMiMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextReviewMiMi
        restNextReviewMiMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextReviewMiMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextReviewMiMiRepository.count();
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

    protected NextReviewMiMi getPersistedNextReviewMiMi(NextReviewMiMi nextReviewMiMi) {
        return nextReviewMiMiRepository.findById(nextReviewMiMi.getId()).orElseThrow();
    }

    protected void assertPersistedNextReviewMiMiToMatchAllProperties(NextReviewMiMi expectedNextReviewMiMi) {
        assertNextReviewMiMiAllPropertiesEquals(expectedNextReviewMiMi, getPersistedNextReviewMiMi(expectedNextReviewMiMi));
    }

    protected void assertPersistedNextReviewMiMiToMatchUpdatableProperties(NextReviewMiMi expectedNextReviewMiMi) {
        assertNextReviewMiMiAllUpdatablePropertiesEquals(expectedNextReviewMiMi, getPersistedNextReviewMiMi(expectedNextReviewMiMi));
    }
}
