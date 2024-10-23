package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.ReviewViAsserts.*;
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
import xyz.jhmapstruct.domain.ProductVi;
import xyz.jhmapstruct.domain.ReviewVi;
import xyz.jhmapstruct.repository.ReviewViRepository;
import xyz.jhmapstruct.service.ReviewViService;

/**
 * Integration tests for the {@link ReviewViResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ReviewViResourceIT {

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;
    private static final Integer SMALLER_RATING = 1 - 1;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_REVIEW_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REVIEW_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/review-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReviewViRepository reviewViRepository;

    @Mock
    private ReviewViRepository reviewViRepositoryMock;

    @Mock
    private ReviewViService reviewViServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReviewViMockMvc;

    private ReviewVi reviewVi;

    private ReviewVi insertedReviewVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReviewVi createEntity() {
        return new ReviewVi().rating(DEFAULT_RATING).comment(DEFAULT_COMMENT).reviewDate(DEFAULT_REVIEW_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReviewVi createUpdatedEntity() {
        return new ReviewVi().rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);
    }

    @BeforeEach
    public void initTest() {
        reviewVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedReviewVi != null) {
            reviewViRepository.delete(insertedReviewVi);
            insertedReviewVi = null;
        }
    }

    @Test
    @Transactional
    void createReviewVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReviewVi
        var returnedReviewVi = om.readValue(
            restReviewViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReviewVi.class
        );

        // Validate the ReviewVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertReviewViUpdatableFieldsEquals(returnedReviewVi, getPersistedReviewVi(returnedReviewVi));

        insertedReviewVi = returnedReviewVi;
    }

    @Test
    @Transactional
    void createReviewViWithExistingId() throws Exception {
        // Create the ReviewVi with an existing ID
        reviewVi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReviewViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewVi)))
            .andExpect(status().isBadRequest());

        // Validate the ReviewVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRatingIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reviewVi.setRating(null);

        // Create the ReviewVi, which fails.

        restReviewViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReviewDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reviewVi.setReviewDate(null);

        // Create the ReviewVi, which fails.

        restReviewViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReviewVis() throws Exception {
        // Initialize the database
        insertedReviewVi = reviewViRepository.saveAndFlush(reviewVi);

        // Get all the reviewViList
        restReviewViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reviewVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReviewVisWithEagerRelationshipsIsEnabled() throws Exception {
        when(reviewViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReviewViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(reviewViServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReviewVisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(reviewViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReviewViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(reviewViRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getReviewVi() throws Exception {
        // Initialize the database
        insertedReviewVi = reviewViRepository.saveAndFlush(reviewVi);

        // Get the reviewVi
        restReviewViMockMvc
            .perform(get(ENTITY_API_URL_ID, reviewVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reviewVi.getId().intValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.reviewDate").value(DEFAULT_REVIEW_DATE.toString()));
    }

    @Test
    @Transactional
    void getReviewVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedReviewVi = reviewViRepository.saveAndFlush(reviewVi);

        Long id = reviewVi.getId();

        defaultReviewViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultReviewViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultReviewViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReviewVisByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReviewVi = reviewViRepository.saveAndFlush(reviewVi);

        // Get all the reviewViList where rating equals to
        defaultReviewViFiltering("rating.equals=" + DEFAULT_RATING, "rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllReviewVisByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReviewVi = reviewViRepository.saveAndFlush(reviewVi);

        // Get all the reviewViList where rating in
        defaultReviewViFiltering("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING, "rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllReviewVisByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReviewVi = reviewViRepository.saveAndFlush(reviewVi);

        // Get all the reviewViList where rating is not null
        defaultReviewViFiltering("rating.specified=true", "rating.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewVisByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReviewVi = reviewViRepository.saveAndFlush(reviewVi);

        // Get all the reviewViList where rating is greater than or equal to
        defaultReviewViFiltering("rating.greaterThanOrEqual=" + DEFAULT_RATING, "rating.greaterThanOrEqual=" + (DEFAULT_RATING + 1));
    }

    @Test
    @Transactional
    void getAllReviewVisByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReviewVi = reviewViRepository.saveAndFlush(reviewVi);

        // Get all the reviewViList where rating is less than or equal to
        defaultReviewViFiltering("rating.lessThanOrEqual=" + DEFAULT_RATING, "rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllReviewVisByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReviewVi = reviewViRepository.saveAndFlush(reviewVi);

        // Get all the reviewViList where rating is less than
        defaultReviewViFiltering("rating.lessThan=" + (DEFAULT_RATING + 1), "rating.lessThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllReviewVisByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReviewVi = reviewViRepository.saveAndFlush(reviewVi);

        // Get all the reviewViList where rating is greater than
        defaultReviewViFiltering("rating.greaterThan=" + SMALLER_RATING, "rating.greaterThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllReviewVisByReviewDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReviewVi = reviewViRepository.saveAndFlush(reviewVi);

        // Get all the reviewViList where reviewDate equals to
        defaultReviewViFiltering("reviewDate.equals=" + DEFAULT_REVIEW_DATE, "reviewDate.equals=" + UPDATED_REVIEW_DATE);
    }

    @Test
    @Transactional
    void getAllReviewVisByReviewDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReviewVi = reviewViRepository.saveAndFlush(reviewVi);

        // Get all the reviewViList where reviewDate in
        defaultReviewViFiltering(
            "reviewDate.in=" + DEFAULT_REVIEW_DATE + "," + UPDATED_REVIEW_DATE,
            "reviewDate.in=" + UPDATED_REVIEW_DATE
        );
    }

    @Test
    @Transactional
    void getAllReviewVisByReviewDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReviewVi = reviewViRepository.saveAndFlush(reviewVi);

        // Get all the reviewViList where reviewDate is not null
        defaultReviewViFiltering("reviewDate.specified=true", "reviewDate.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewVisByProductIsEqualToSomething() throws Exception {
        ProductVi product;
        if (TestUtil.findAll(em, ProductVi.class).isEmpty()) {
            reviewViRepository.saveAndFlush(reviewVi);
            product = ProductViResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, ProductVi.class).get(0);
        }
        em.persist(product);
        em.flush();
        reviewVi.setProduct(product);
        reviewViRepository.saveAndFlush(reviewVi);
        Long productId = product.getId();
        // Get all the reviewViList where product equals to productId
        defaultReviewViShouldBeFound("productId.equals=" + productId);

        // Get all the reviewViList where product equals to (productId + 1)
        defaultReviewViShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllReviewVisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            reviewViRepository.saveAndFlush(reviewVi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        reviewVi.setTenant(tenant);
        reviewViRepository.saveAndFlush(reviewVi);
        Long tenantId = tenant.getId();
        // Get all the reviewViList where tenant equals to tenantId
        defaultReviewViShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the reviewViList where tenant equals to (tenantId + 1)
        defaultReviewViShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultReviewViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultReviewViShouldBeFound(shouldBeFound);
        defaultReviewViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReviewViShouldBeFound(String filter) throws Exception {
        restReviewViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reviewVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));

        // Check, that the count call also returns 1
        restReviewViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReviewViShouldNotBeFound(String filter) throws Exception {
        restReviewViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReviewViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReviewVi() throws Exception {
        // Get the reviewVi
        restReviewViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReviewVi() throws Exception {
        // Initialize the database
        insertedReviewVi = reviewViRepository.saveAndFlush(reviewVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reviewVi
        ReviewVi updatedReviewVi = reviewViRepository.findById(reviewVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReviewVi are not directly saved in db
        em.detach(updatedReviewVi);
        updatedReviewVi.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);

        restReviewViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReviewVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedReviewVi))
            )
            .andExpect(status().isOk());

        // Validate the ReviewVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReviewViToMatchAllProperties(updatedReviewVi);
    }

    @Test
    @Transactional
    void putNonExistingReviewVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReviewViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reviewVi.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReviewVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReviewVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reviewVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReviewVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReviewVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReviewVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReviewViWithPatch() throws Exception {
        // Initialize the database
        insertedReviewVi = reviewViRepository.saveAndFlush(reviewVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reviewVi using partial update
        ReviewVi partialUpdatedReviewVi = new ReviewVi();
        partialUpdatedReviewVi.setId(reviewVi.getId());

        partialUpdatedReviewVi.rating(UPDATED_RATING).reviewDate(UPDATED_REVIEW_DATE);

        restReviewViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReviewVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReviewVi))
            )
            .andExpect(status().isOk());

        // Validate the ReviewVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReviewViUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedReviewVi, reviewVi), getPersistedReviewVi(reviewVi));
    }

    @Test
    @Transactional
    void fullUpdateReviewViWithPatch() throws Exception {
        // Initialize the database
        insertedReviewVi = reviewViRepository.saveAndFlush(reviewVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reviewVi using partial update
        ReviewVi partialUpdatedReviewVi = new ReviewVi();
        partialUpdatedReviewVi.setId(reviewVi.getId());

        partialUpdatedReviewVi.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);

        restReviewViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReviewVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReviewVi))
            )
            .andExpect(status().isOk());

        // Validate the ReviewVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReviewViUpdatableFieldsEquals(partialUpdatedReviewVi, getPersistedReviewVi(partialUpdatedReviewVi));
    }

    @Test
    @Transactional
    void patchNonExistingReviewVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReviewViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reviewVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reviewVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReviewVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReviewVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reviewVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReviewVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReviewVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reviewVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReviewVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReviewVi() throws Exception {
        // Initialize the database
        insertedReviewVi = reviewViRepository.saveAndFlush(reviewVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reviewVi
        restReviewViMockMvc
            .perform(delete(ENTITY_API_URL_ID, reviewVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reviewViRepository.count();
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

    protected ReviewVi getPersistedReviewVi(ReviewVi reviewVi) {
        return reviewViRepository.findById(reviewVi.getId()).orElseThrow();
    }

    protected void assertPersistedReviewViToMatchAllProperties(ReviewVi expectedReviewVi) {
        assertReviewViAllPropertiesEquals(expectedReviewVi, getPersistedReviewVi(expectedReviewVi));
    }

    protected void assertPersistedReviewViToMatchUpdatableProperties(ReviewVi expectedReviewVi) {
        assertReviewViAllUpdatablePropertiesEquals(expectedReviewVi, getPersistedReviewVi(expectedReviewVi));
    }
}
