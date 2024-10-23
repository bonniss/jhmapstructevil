package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.ReviewMiAsserts.*;
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
import xyz.jhmapstruct.domain.NextProductMi;
import xyz.jhmapstruct.domain.ReviewMi;
import xyz.jhmapstruct.repository.ReviewMiRepository;
import xyz.jhmapstruct.service.ReviewMiService;

/**
 * Integration tests for the {@link ReviewMiResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ReviewMiResourceIT {

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;
    private static final Integer SMALLER_RATING = 1 - 1;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_REVIEW_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REVIEW_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/review-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReviewMiRepository reviewMiRepository;

    @Mock
    private ReviewMiRepository reviewMiRepositoryMock;

    @Mock
    private ReviewMiService reviewMiServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReviewMiMockMvc;

    private ReviewMi reviewMi;

    private ReviewMi insertedReviewMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReviewMi createEntity() {
        return new ReviewMi().rating(DEFAULT_RATING).comment(DEFAULT_COMMENT).reviewDate(DEFAULT_REVIEW_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReviewMi createUpdatedEntity() {
        return new ReviewMi().rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);
    }

    @BeforeEach
    public void initTest() {
        reviewMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedReviewMi != null) {
            reviewMiRepository.delete(insertedReviewMi);
            insertedReviewMi = null;
        }
    }

    @Test
    @Transactional
    void createReviewMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReviewMi
        var returnedReviewMi = om.readValue(
            restReviewMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewMi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReviewMi.class
        );

        // Validate the ReviewMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertReviewMiUpdatableFieldsEquals(returnedReviewMi, getPersistedReviewMi(returnedReviewMi));

        insertedReviewMi = returnedReviewMi;
    }

    @Test
    @Transactional
    void createReviewMiWithExistingId() throws Exception {
        // Create the ReviewMi with an existing ID
        reviewMi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReviewMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewMi)))
            .andExpect(status().isBadRequest());

        // Validate the ReviewMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRatingIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reviewMi.setRating(null);

        // Create the ReviewMi, which fails.

        restReviewMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReviewDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reviewMi.setReviewDate(null);

        // Create the ReviewMi, which fails.

        restReviewMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReviewMis() throws Exception {
        // Initialize the database
        insertedReviewMi = reviewMiRepository.saveAndFlush(reviewMi);

        // Get all the reviewMiList
        restReviewMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reviewMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReviewMisWithEagerRelationshipsIsEnabled() throws Exception {
        when(reviewMiServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReviewMiMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(reviewMiServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReviewMisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(reviewMiServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReviewMiMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(reviewMiRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getReviewMi() throws Exception {
        // Initialize the database
        insertedReviewMi = reviewMiRepository.saveAndFlush(reviewMi);

        // Get the reviewMi
        restReviewMiMockMvc
            .perform(get(ENTITY_API_URL_ID, reviewMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reviewMi.getId().intValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.reviewDate").value(DEFAULT_REVIEW_DATE.toString()));
    }

    @Test
    @Transactional
    void getReviewMisByIdFiltering() throws Exception {
        // Initialize the database
        insertedReviewMi = reviewMiRepository.saveAndFlush(reviewMi);

        Long id = reviewMi.getId();

        defaultReviewMiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultReviewMiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultReviewMiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReviewMisByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReviewMi = reviewMiRepository.saveAndFlush(reviewMi);

        // Get all the reviewMiList where rating equals to
        defaultReviewMiFiltering("rating.equals=" + DEFAULT_RATING, "rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllReviewMisByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReviewMi = reviewMiRepository.saveAndFlush(reviewMi);

        // Get all the reviewMiList where rating in
        defaultReviewMiFiltering("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING, "rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllReviewMisByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReviewMi = reviewMiRepository.saveAndFlush(reviewMi);

        // Get all the reviewMiList where rating is not null
        defaultReviewMiFiltering("rating.specified=true", "rating.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewMisByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReviewMi = reviewMiRepository.saveAndFlush(reviewMi);

        // Get all the reviewMiList where rating is greater than or equal to
        defaultReviewMiFiltering("rating.greaterThanOrEqual=" + DEFAULT_RATING, "rating.greaterThanOrEqual=" + (DEFAULT_RATING + 1));
    }

    @Test
    @Transactional
    void getAllReviewMisByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReviewMi = reviewMiRepository.saveAndFlush(reviewMi);

        // Get all the reviewMiList where rating is less than or equal to
        defaultReviewMiFiltering("rating.lessThanOrEqual=" + DEFAULT_RATING, "rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllReviewMisByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReviewMi = reviewMiRepository.saveAndFlush(reviewMi);

        // Get all the reviewMiList where rating is less than
        defaultReviewMiFiltering("rating.lessThan=" + (DEFAULT_RATING + 1), "rating.lessThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllReviewMisByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReviewMi = reviewMiRepository.saveAndFlush(reviewMi);

        // Get all the reviewMiList where rating is greater than
        defaultReviewMiFiltering("rating.greaterThan=" + SMALLER_RATING, "rating.greaterThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllReviewMisByReviewDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReviewMi = reviewMiRepository.saveAndFlush(reviewMi);

        // Get all the reviewMiList where reviewDate equals to
        defaultReviewMiFiltering("reviewDate.equals=" + DEFAULT_REVIEW_DATE, "reviewDate.equals=" + UPDATED_REVIEW_DATE);
    }

    @Test
    @Transactional
    void getAllReviewMisByReviewDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReviewMi = reviewMiRepository.saveAndFlush(reviewMi);

        // Get all the reviewMiList where reviewDate in
        defaultReviewMiFiltering(
            "reviewDate.in=" + DEFAULT_REVIEW_DATE + "," + UPDATED_REVIEW_DATE,
            "reviewDate.in=" + UPDATED_REVIEW_DATE
        );
    }

    @Test
    @Transactional
    void getAllReviewMisByReviewDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReviewMi = reviewMiRepository.saveAndFlush(reviewMi);

        // Get all the reviewMiList where reviewDate is not null
        defaultReviewMiFiltering("reviewDate.specified=true", "reviewDate.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewMisByProductIsEqualToSomething() throws Exception {
        NextProductMi product;
        if (TestUtil.findAll(em, NextProductMi.class).isEmpty()) {
            reviewMiRepository.saveAndFlush(reviewMi);
            product = NextProductMiResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, NextProductMi.class).get(0);
        }
        em.persist(product);
        em.flush();
        reviewMi.setProduct(product);
        reviewMiRepository.saveAndFlush(reviewMi);
        Long productId = product.getId();
        // Get all the reviewMiList where product equals to productId
        defaultReviewMiShouldBeFound("productId.equals=" + productId);

        // Get all the reviewMiList where product equals to (productId + 1)
        defaultReviewMiShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllReviewMisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            reviewMiRepository.saveAndFlush(reviewMi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        reviewMi.setTenant(tenant);
        reviewMiRepository.saveAndFlush(reviewMi);
        Long tenantId = tenant.getId();
        // Get all the reviewMiList where tenant equals to tenantId
        defaultReviewMiShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the reviewMiList where tenant equals to (tenantId + 1)
        defaultReviewMiShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultReviewMiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultReviewMiShouldBeFound(shouldBeFound);
        defaultReviewMiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReviewMiShouldBeFound(String filter) throws Exception {
        restReviewMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reviewMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));

        // Check, that the count call also returns 1
        restReviewMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReviewMiShouldNotBeFound(String filter) throws Exception {
        restReviewMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReviewMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReviewMi() throws Exception {
        // Get the reviewMi
        restReviewMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReviewMi() throws Exception {
        // Initialize the database
        insertedReviewMi = reviewMiRepository.saveAndFlush(reviewMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reviewMi
        ReviewMi updatedReviewMi = reviewMiRepository.findById(reviewMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReviewMi are not directly saved in db
        em.detach(updatedReviewMi);
        updatedReviewMi.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);

        restReviewMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReviewMi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedReviewMi))
            )
            .andExpect(status().isOk());

        // Validate the ReviewMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReviewMiToMatchAllProperties(updatedReviewMi);
    }

    @Test
    @Transactional
    void putNonExistingReviewMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewMi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReviewMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reviewMi.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReviewMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReviewMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reviewMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReviewMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReviewMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewMi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReviewMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReviewMiWithPatch() throws Exception {
        // Initialize the database
        insertedReviewMi = reviewMiRepository.saveAndFlush(reviewMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reviewMi using partial update
        ReviewMi partialUpdatedReviewMi = new ReviewMi();
        partialUpdatedReviewMi.setId(reviewMi.getId());

        partialUpdatedReviewMi.comment(UPDATED_COMMENT);

        restReviewMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReviewMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReviewMi))
            )
            .andExpect(status().isOk());

        // Validate the ReviewMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReviewMiUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedReviewMi, reviewMi), getPersistedReviewMi(reviewMi));
    }

    @Test
    @Transactional
    void fullUpdateReviewMiWithPatch() throws Exception {
        // Initialize the database
        insertedReviewMi = reviewMiRepository.saveAndFlush(reviewMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reviewMi using partial update
        ReviewMi partialUpdatedReviewMi = new ReviewMi();
        partialUpdatedReviewMi.setId(reviewMi.getId());

        partialUpdatedReviewMi.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);

        restReviewMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReviewMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReviewMi))
            )
            .andExpect(status().isOk());

        // Validate the ReviewMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReviewMiUpdatableFieldsEquals(partialUpdatedReviewMi, getPersistedReviewMi(partialUpdatedReviewMi));
    }

    @Test
    @Transactional
    void patchNonExistingReviewMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewMi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReviewMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reviewMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reviewMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReviewMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReviewMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reviewMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReviewMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReviewMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reviewMi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReviewMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReviewMi() throws Exception {
        // Initialize the database
        insertedReviewMi = reviewMiRepository.saveAndFlush(reviewMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reviewMi
        restReviewMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, reviewMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reviewMiRepository.count();
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

    protected ReviewMi getPersistedReviewMi(ReviewMi reviewMi) {
        return reviewMiRepository.findById(reviewMi.getId()).orElseThrow();
    }

    protected void assertPersistedReviewMiToMatchAllProperties(ReviewMi expectedReviewMi) {
        assertReviewMiAllPropertiesEquals(expectedReviewMi, getPersistedReviewMi(expectedReviewMi));
    }

    protected void assertPersistedReviewMiToMatchUpdatableProperties(ReviewMi expectedReviewMi) {
        assertReviewMiAllUpdatablePropertiesEquals(expectedReviewMi, getPersistedReviewMi(expectedReviewMi));
    }
}
