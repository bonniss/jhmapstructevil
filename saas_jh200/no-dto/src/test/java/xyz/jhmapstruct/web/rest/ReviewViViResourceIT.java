package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.ReviewViViAsserts.*;
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
import xyz.jhmapstruct.domain.ProductViVi;
import xyz.jhmapstruct.domain.ReviewViVi;
import xyz.jhmapstruct.repository.ReviewViViRepository;
import xyz.jhmapstruct.service.ReviewViViService;

/**
 * Integration tests for the {@link ReviewViViResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ReviewViViResourceIT {

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;
    private static final Integer SMALLER_RATING = 1 - 1;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_REVIEW_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REVIEW_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/review-vi-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReviewViViRepository reviewViViRepository;

    @Mock
    private ReviewViViRepository reviewViViRepositoryMock;

    @Mock
    private ReviewViViService reviewViViServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReviewViViMockMvc;

    private ReviewViVi reviewViVi;

    private ReviewViVi insertedReviewViVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReviewViVi createEntity() {
        return new ReviewViVi().rating(DEFAULT_RATING).comment(DEFAULT_COMMENT).reviewDate(DEFAULT_REVIEW_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReviewViVi createUpdatedEntity() {
        return new ReviewViVi().rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);
    }

    @BeforeEach
    public void initTest() {
        reviewViVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedReviewViVi != null) {
            reviewViViRepository.delete(insertedReviewViVi);
            insertedReviewViVi = null;
        }
    }

    @Test
    @Transactional
    void createReviewViVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReviewViVi
        var returnedReviewViVi = om.readValue(
            restReviewViViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewViVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReviewViVi.class
        );

        // Validate the ReviewViVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertReviewViViUpdatableFieldsEquals(returnedReviewViVi, getPersistedReviewViVi(returnedReviewViVi));

        insertedReviewViVi = returnedReviewViVi;
    }

    @Test
    @Transactional
    void createReviewViViWithExistingId() throws Exception {
        // Create the ReviewViVi with an existing ID
        reviewViVi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReviewViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewViVi)))
            .andExpect(status().isBadRequest());

        // Validate the ReviewViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRatingIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reviewViVi.setRating(null);

        // Create the ReviewViVi, which fails.

        restReviewViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewViVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReviewDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reviewViVi.setReviewDate(null);

        // Create the ReviewViVi, which fails.

        restReviewViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewViVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReviewViVis() throws Exception {
        // Initialize the database
        insertedReviewViVi = reviewViViRepository.saveAndFlush(reviewViVi);

        // Get all the reviewViViList
        restReviewViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reviewViVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReviewViVisWithEagerRelationshipsIsEnabled() throws Exception {
        when(reviewViViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReviewViViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(reviewViViServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReviewViVisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(reviewViViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReviewViViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(reviewViViRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getReviewViVi() throws Exception {
        // Initialize the database
        insertedReviewViVi = reviewViViRepository.saveAndFlush(reviewViVi);

        // Get the reviewViVi
        restReviewViViMockMvc
            .perform(get(ENTITY_API_URL_ID, reviewViVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reviewViVi.getId().intValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.reviewDate").value(DEFAULT_REVIEW_DATE.toString()));
    }

    @Test
    @Transactional
    void getReviewViVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedReviewViVi = reviewViViRepository.saveAndFlush(reviewViVi);

        Long id = reviewViVi.getId();

        defaultReviewViViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultReviewViViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultReviewViViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReviewViVisByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReviewViVi = reviewViViRepository.saveAndFlush(reviewViVi);

        // Get all the reviewViViList where rating equals to
        defaultReviewViViFiltering("rating.equals=" + DEFAULT_RATING, "rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllReviewViVisByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReviewViVi = reviewViViRepository.saveAndFlush(reviewViVi);

        // Get all the reviewViViList where rating in
        defaultReviewViViFiltering("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING, "rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllReviewViVisByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReviewViVi = reviewViViRepository.saveAndFlush(reviewViVi);

        // Get all the reviewViViList where rating is not null
        defaultReviewViViFiltering("rating.specified=true", "rating.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewViVisByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReviewViVi = reviewViViRepository.saveAndFlush(reviewViVi);

        // Get all the reviewViViList where rating is greater than or equal to
        defaultReviewViViFiltering("rating.greaterThanOrEqual=" + DEFAULT_RATING, "rating.greaterThanOrEqual=" + (DEFAULT_RATING + 1));
    }

    @Test
    @Transactional
    void getAllReviewViVisByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReviewViVi = reviewViViRepository.saveAndFlush(reviewViVi);

        // Get all the reviewViViList where rating is less than or equal to
        defaultReviewViViFiltering("rating.lessThanOrEqual=" + DEFAULT_RATING, "rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllReviewViVisByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReviewViVi = reviewViViRepository.saveAndFlush(reviewViVi);

        // Get all the reviewViViList where rating is less than
        defaultReviewViViFiltering("rating.lessThan=" + (DEFAULT_RATING + 1), "rating.lessThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllReviewViVisByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReviewViVi = reviewViViRepository.saveAndFlush(reviewViVi);

        // Get all the reviewViViList where rating is greater than
        defaultReviewViViFiltering("rating.greaterThan=" + SMALLER_RATING, "rating.greaterThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllReviewViVisByReviewDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReviewViVi = reviewViViRepository.saveAndFlush(reviewViVi);

        // Get all the reviewViViList where reviewDate equals to
        defaultReviewViViFiltering("reviewDate.equals=" + DEFAULT_REVIEW_DATE, "reviewDate.equals=" + UPDATED_REVIEW_DATE);
    }

    @Test
    @Transactional
    void getAllReviewViVisByReviewDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReviewViVi = reviewViViRepository.saveAndFlush(reviewViVi);

        // Get all the reviewViViList where reviewDate in
        defaultReviewViViFiltering(
            "reviewDate.in=" + DEFAULT_REVIEW_DATE + "," + UPDATED_REVIEW_DATE,
            "reviewDate.in=" + UPDATED_REVIEW_DATE
        );
    }

    @Test
    @Transactional
    void getAllReviewViVisByReviewDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReviewViVi = reviewViViRepository.saveAndFlush(reviewViVi);

        // Get all the reviewViViList where reviewDate is not null
        defaultReviewViViFiltering("reviewDate.specified=true", "reviewDate.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewViVisByProductIsEqualToSomething() throws Exception {
        ProductViVi product;
        if (TestUtil.findAll(em, ProductViVi.class).isEmpty()) {
            reviewViViRepository.saveAndFlush(reviewViVi);
            product = ProductViViResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, ProductViVi.class).get(0);
        }
        em.persist(product);
        em.flush();
        reviewViVi.setProduct(product);
        reviewViViRepository.saveAndFlush(reviewViVi);
        Long productId = product.getId();
        // Get all the reviewViViList where product equals to productId
        defaultReviewViViShouldBeFound("productId.equals=" + productId);

        // Get all the reviewViViList where product equals to (productId + 1)
        defaultReviewViViShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllReviewViVisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            reviewViViRepository.saveAndFlush(reviewViVi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        reviewViVi.setTenant(tenant);
        reviewViViRepository.saveAndFlush(reviewViVi);
        Long tenantId = tenant.getId();
        // Get all the reviewViViList where tenant equals to tenantId
        defaultReviewViViShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the reviewViViList where tenant equals to (tenantId + 1)
        defaultReviewViViShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultReviewViViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultReviewViViShouldBeFound(shouldBeFound);
        defaultReviewViViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReviewViViShouldBeFound(String filter) throws Exception {
        restReviewViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reviewViVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));

        // Check, that the count call also returns 1
        restReviewViViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReviewViViShouldNotBeFound(String filter) throws Exception {
        restReviewViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReviewViViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReviewViVi() throws Exception {
        // Get the reviewViVi
        restReviewViViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReviewViVi() throws Exception {
        // Initialize the database
        insertedReviewViVi = reviewViViRepository.saveAndFlush(reviewViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reviewViVi
        ReviewViVi updatedReviewViVi = reviewViViRepository.findById(reviewViVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReviewViVi are not directly saved in db
        em.detach(updatedReviewViVi);
        updatedReviewViVi.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);

        restReviewViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReviewViVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedReviewViVi))
            )
            .andExpect(status().isOk());

        // Validate the ReviewViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReviewViViToMatchAllProperties(updatedReviewViVi);
    }

    @Test
    @Transactional
    void putNonExistingReviewViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewViVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReviewViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reviewViVi.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewViVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReviewViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReviewViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewViVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reviewViVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReviewViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReviewViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewViVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewViViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewViVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReviewViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReviewViViWithPatch() throws Exception {
        // Initialize the database
        insertedReviewViVi = reviewViViRepository.saveAndFlush(reviewViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reviewViVi using partial update
        ReviewViVi partialUpdatedReviewViVi = new ReviewViVi();
        partialUpdatedReviewViVi.setId(reviewViVi.getId());

        partialUpdatedReviewViVi.comment(UPDATED_COMMENT);

        restReviewViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReviewViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReviewViVi))
            )
            .andExpect(status().isOk());

        // Validate the ReviewViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReviewViViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReviewViVi, reviewViVi),
            getPersistedReviewViVi(reviewViVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateReviewViViWithPatch() throws Exception {
        // Initialize the database
        insertedReviewViVi = reviewViViRepository.saveAndFlush(reviewViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reviewViVi using partial update
        ReviewViVi partialUpdatedReviewViVi = new ReviewViVi();
        partialUpdatedReviewViVi.setId(reviewViVi.getId());

        partialUpdatedReviewViVi.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);

        restReviewViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReviewViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReviewViVi))
            )
            .andExpect(status().isOk());

        // Validate the ReviewViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReviewViViUpdatableFieldsEquals(partialUpdatedReviewViVi, getPersistedReviewViVi(partialUpdatedReviewViVi));
    }

    @Test
    @Transactional
    void patchNonExistingReviewViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewViVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReviewViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reviewViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reviewViVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReviewViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReviewViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewViVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reviewViVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReviewViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReviewViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewViVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewViViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reviewViVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReviewViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReviewViVi() throws Exception {
        // Initialize the database
        insertedReviewViVi = reviewViViRepository.saveAndFlush(reviewViVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reviewViVi
        restReviewViViMockMvc
            .perform(delete(ENTITY_API_URL_ID, reviewViVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reviewViViRepository.count();
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

    protected ReviewViVi getPersistedReviewViVi(ReviewViVi reviewViVi) {
        return reviewViViRepository.findById(reviewViVi.getId()).orElseThrow();
    }

    protected void assertPersistedReviewViViToMatchAllProperties(ReviewViVi expectedReviewViVi) {
        assertReviewViViAllPropertiesEquals(expectedReviewViVi, getPersistedReviewViVi(expectedReviewViVi));
    }

    protected void assertPersistedReviewViViToMatchUpdatableProperties(ReviewViVi expectedReviewViVi) {
        assertReviewViViAllUpdatablePropertiesEquals(expectedReviewViVi, getPersistedReviewViVi(expectedReviewViVi));
    }
}