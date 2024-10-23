package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.ReviewAlphaAsserts.*;
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
import xyz.jhmapstruct.domain.ProductAlpha;
import xyz.jhmapstruct.domain.ReviewAlpha;
import xyz.jhmapstruct.repository.ReviewAlphaRepository;
import xyz.jhmapstruct.service.ReviewAlphaService;
import xyz.jhmapstruct.service.dto.ReviewAlphaDTO;
import xyz.jhmapstruct.service.mapper.ReviewAlphaMapper;

/**
 * Integration tests for the {@link ReviewAlphaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ReviewAlphaResourceIT {

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;
    private static final Integer SMALLER_RATING = 1 - 1;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_REVIEW_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REVIEW_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/review-alphas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReviewAlphaRepository reviewAlphaRepository;

    @Mock
    private ReviewAlphaRepository reviewAlphaRepositoryMock;

    @Autowired
    private ReviewAlphaMapper reviewAlphaMapper;

    @Mock
    private ReviewAlphaService reviewAlphaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReviewAlphaMockMvc;

    private ReviewAlpha reviewAlpha;

    private ReviewAlpha insertedReviewAlpha;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReviewAlpha createEntity() {
        return new ReviewAlpha().rating(DEFAULT_RATING).comment(DEFAULT_COMMENT).reviewDate(DEFAULT_REVIEW_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReviewAlpha createUpdatedEntity() {
        return new ReviewAlpha().rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);
    }

    @BeforeEach
    public void initTest() {
        reviewAlpha = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedReviewAlpha != null) {
            reviewAlphaRepository.delete(insertedReviewAlpha);
            insertedReviewAlpha = null;
        }
    }

    @Test
    @Transactional
    void createReviewAlpha() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReviewAlpha
        ReviewAlphaDTO reviewAlphaDTO = reviewAlphaMapper.toDto(reviewAlpha);
        var returnedReviewAlphaDTO = om.readValue(
            restReviewAlphaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewAlphaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReviewAlphaDTO.class
        );

        // Validate the ReviewAlpha in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReviewAlpha = reviewAlphaMapper.toEntity(returnedReviewAlphaDTO);
        assertReviewAlphaUpdatableFieldsEquals(returnedReviewAlpha, getPersistedReviewAlpha(returnedReviewAlpha));

        insertedReviewAlpha = returnedReviewAlpha;
    }

    @Test
    @Transactional
    void createReviewAlphaWithExistingId() throws Exception {
        // Create the ReviewAlpha with an existing ID
        reviewAlpha.setId(1L);
        ReviewAlphaDTO reviewAlphaDTO = reviewAlphaMapper.toDto(reviewAlpha);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReviewAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewAlphaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReviewAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRatingIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reviewAlpha.setRating(null);

        // Create the ReviewAlpha, which fails.
        ReviewAlphaDTO reviewAlphaDTO = reviewAlphaMapper.toDto(reviewAlpha);

        restReviewAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewAlphaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReviewDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reviewAlpha.setReviewDate(null);

        // Create the ReviewAlpha, which fails.
        ReviewAlphaDTO reviewAlphaDTO = reviewAlphaMapper.toDto(reviewAlpha);

        restReviewAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewAlphaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReviewAlphas() throws Exception {
        // Initialize the database
        insertedReviewAlpha = reviewAlphaRepository.saveAndFlush(reviewAlpha);

        // Get all the reviewAlphaList
        restReviewAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reviewAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReviewAlphasWithEagerRelationshipsIsEnabled() throws Exception {
        when(reviewAlphaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReviewAlphaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(reviewAlphaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReviewAlphasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(reviewAlphaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReviewAlphaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(reviewAlphaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getReviewAlpha() throws Exception {
        // Initialize the database
        insertedReviewAlpha = reviewAlphaRepository.saveAndFlush(reviewAlpha);

        // Get the reviewAlpha
        restReviewAlphaMockMvc
            .perform(get(ENTITY_API_URL_ID, reviewAlpha.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reviewAlpha.getId().intValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.reviewDate").value(DEFAULT_REVIEW_DATE.toString()));
    }

    @Test
    @Transactional
    void getReviewAlphasByIdFiltering() throws Exception {
        // Initialize the database
        insertedReviewAlpha = reviewAlphaRepository.saveAndFlush(reviewAlpha);

        Long id = reviewAlpha.getId();

        defaultReviewAlphaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultReviewAlphaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultReviewAlphaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReviewAlphasByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReviewAlpha = reviewAlphaRepository.saveAndFlush(reviewAlpha);

        // Get all the reviewAlphaList where rating equals to
        defaultReviewAlphaFiltering("rating.equals=" + DEFAULT_RATING, "rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllReviewAlphasByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReviewAlpha = reviewAlphaRepository.saveAndFlush(reviewAlpha);

        // Get all the reviewAlphaList where rating in
        defaultReviewAlphaFiltering("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING, "rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllReviewAlphasByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReviewAlpha = reviewAlphaRepository.saveAndFlush(reviewAlpha);

        // Get all the reviewAlphaList where rating is not null
        defaultReviewAlphaFiltering("rating.specified=true", "rating.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewAlphasByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReviewAlpha = reviewAlphaRepository.saveAndFlush(reviewAlpha);

        // Get all the reviewAlphaList where rating is greater than or equal to
        defaultReviewAlphaFiltering("rating.greaterThanOrEqual=" + DEFAULT_RATING, "rating.greaterThanOrEqual=" + (DEFAULT_RATING + 1));
    }

    @Test
    @Transactional
    void getAllReviewAlphasByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReviewAlpha = reviewAlphaRepository.saveAndFlush(reviewAlpha);

        // Get all the reviewAlphaList where rating is less than or equal to
        defaultReviewAlphaFiltering("rating.lessThanOrEqual=" + DEFAULT_RATING, "rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllReviewAlphasByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReviewAlpha = reviewAlphaRepository.saveAndFlush(reviewAlpha);

        // Get all the reviewAlphaList where rating is less than
        defaultReviewAlphaFiltering("rating.lessThan=" + (DEFAULT_RATING + 1), "rating.lessThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllReviewAlphasByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReviewAlpha = reviewAlphaRepository.saveAndFlush(reviewAlpha);

        // Get all the reviewAlphaList where rating is greater than
        defaultReviewAlphaFiltering("rating.greaterThan=" + SMALLER_RATING, "rating.greaterThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllReviewAlphasByReviewDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReviewAlpha = reviewAlphaRepository.saveAndFlush(reviewAlpha);

        // Get all the reviewAlphaList where reviewDate equals to
        defaultReviewAlphaFiltering("reviewDate.equals=" + DEFAULT_REVIEW_DATE, "reviewDate.equals=" + UPDATED_REVIEW_DATE);
    }

    @Test
    @Transactional
    void getAllReviewAlphasByReviewDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReviewAlpha = reviewAlphaRepository.saveAndFlush(reviewAlpha);

        // Get all the reviewAlphaList where reviewDate in
        defaultReviewAlphaFiltering(
            "reviewDate.in=" + DEFAULT_REVIEW_DATE + "," + UPDATED_REVIEW_DATE,
            "reviewDate.in=" + UPDATED_REVIEW_DATE
        );
    }

    @Test
    @Transactional
    void getAllReviewAlphasByReviewDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReviewAlpha = reviewAlphaRepository.saveAndFlush(reviewAlpha);

        // Get all the reviewAlphaList where reviewDate is not null
        defaultReviewAlphaFiltering("reviewDate.specified=true", "reviewDate.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewAlphasByProductIsEqualToSomething() throws Exception {
        ProductAlpha product;
        if (TestUtil.findAll(em, ProductAlpha.class).isEmpty()) {
            reviewAlphaRepository.saveAndFlush(reviewAlpha);
            product = ProductAlphaResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, ProductAlpha.class).get(0);
        }
        em.persist(product);
        em.flush();
        reviewAlpha.setProduct(product);
        reviewAlphaRepository.saveAndFlush(reviewAlpha);
        Long productId = product.getId();
        // Get all the reviewAlphaList where product equals to productId
        defaultReviewAlphaShouldBeFound("productId.equals=" + productId);

        // Get all the reviewAlphaList where product equals to (productId + 1)
        defaultReviewAlphaShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllReviewAlphasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            reviewAlphaRepository.saveAndFlush(reviewAlpha);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        reviewAlpha.setTenant(tenant);
        reviewAlphaRepository.saveAndFlush(reviewAlpha);
        Long tenantId = tenant.getId();
        // Get all the reviewAlphaList where tenant equals to tenantId
        defaultReviewAlphaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the reviewAlphaList where tenant equals to (tenantId + 1)
        defaultReviewAlphaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultReviewAlphaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultReviewAlphaShouldBeFound(shouldBeFound);
        defaultReviewAlphaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReviewAlphaShouldBeFound(String filter) throws Exception {
        restReviewAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reviewAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));

        // Check, that the count call also returns 1
        restReviewAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReviewAlphaShouldNotBeFound(String filter) throws Exception {
        restReviewAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReviewAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReviewAlpha() throws Exception {
        // Get the reviewAlpha
        restReviewAlphaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReviewAlpha() throws Exception {
        // Initialize the database
        insertedReviewAlpha = reviewAlphaRepository.saveAndFlush(reviewAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reviewAlpha
        ReviewAlpha updatedReviewAlpha = reviewAlphaRepository.findById(reviewAlpha.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReviewAlpha are not directly saved in db
        em.detach(updatedReviewAlpha);
        updatedReviewAlpha.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);
        ReviewAlphaDTO reviewAlphaDTO = reviewAlphaMapper.toDto(updatedReviewAlpha);

        restReviewAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reviewAlphaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reviewAlphaDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReviewAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReviewAlphaToMatchAllProperties(updatedReviewAlpha);
    }

    @Test
    @Transactional
    void putNonExistingReviewAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewAlpha.setId(longCount.incrementAndGet());

        // Create the ReviewAlpha
        ReviewAlphaDTO reviewAlphaDTO = reviewAlphaMapper.toDto(reviewAlpha);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReviewAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reviewAlphaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reviewAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReviewAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReviewAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewAlpha.setId(longCount.incrementAndGet());

        // Create the ReviewAlpha
        ReviewAlphaDTO reviewAlphaDTO = reviewAlphaMapper.toDto(reviewAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reviewAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReviewAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReviewAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewAlpha.setId(longCount.incrementAndGet());

        // Create the ReviewAlpha
        ReviewAlphaDTO reviewAlphaDTO = reviewAlphaMapper.toDto(reviewAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewAlphaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewAlphaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReviewAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReviewAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedReviewAlpha = reviewAlphaRepository.saveAndFlush(reviewAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reviewAlpha using partial update
        ReviewAlpha partialUpdatedReviewAlpha = new ReviewAlpha();
        partialUpdatedReviewAlpha.setId(reviewAlpha.getId());

        partialUpdatedReviewAlpha.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);

        restReviewAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReviewAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReviewAlpha))
            )
            .andExpect(status().isOk());

        // Validate the ReviewAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReviewAlphaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReviewAlpha, reviewAlpha),
            getPersistedReviewAlpha(reviewAlpha)
        );
    }

    @Test
    @Transactional
    void fullUpdateReviewAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedReviewAlpha = reviewAlphaRepository.saveAndFlush(reviewAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reviewAlpha using partial update
        ReviewAlpha partialUpdatedReviewAlpha = new ReviewAlpha();
        partialUpdatedReviewAlpha.setId(reviewAlpha.getId());

        partialUpdatedReviewAlpha.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);

        restReviewAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReviewAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReviewAlpha))
            )
            .andExpect(status().isOk());

        // Validate the ReviewAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReviewAlphaUpdatableFieldsEquals(partialUpdatedReviewAlpha, getPersistedReviewAlpha(partialUpdatedReviewAlpha));
    }

    @Test
    @Transactional
    void patchNonExistingReviewAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewAlpha.setId(longCount.incrementAndGet());

        // Create the ReviewAlpha
        ReviewAlphaDTO reviewAlphaDTO = reviewAlphaMapper.toDto(reviewAlpha);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReviewAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reviewAlphaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reviewAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReviewAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReviewAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewAlpha.setId(longCount.incrementAndGet());

        // Create the ReviewAlpha
        ReviewAlphaDTO reviewAlphaDTO = reviewAlphaMapper.toDto(reviewAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reviewAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReviewAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReviewAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewAlpha.setId(longCount.incrementAndGet());

        // Create the ReviewAlpha
        ReviewAlphaDTO reviewAlphaDTO = reviewAlphaMapper.toDto(reviewAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewAlphaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reviewAlphaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReviewAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReviewAlpha() throws Exception {
        // Initialize the database
        insertedReviewAlpha = reviewAlphaRepository.saveAndFlush(reviewAlpha);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reviewAlpha
        restReviewAlphaMockMvc
            .perform(delete(ENTITY_API_URL_ID, reviewAlpha.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reviewAlphaRepository.count();
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

    protected ReviewAlpha getPersistedReviewAlpha(ReviewAlpha reviewAlpha) {
        return reviewAlphaRepository.findById(reviewAlpha.getId()).orElseThrow();
    }

    protected void assertPersistedReviewAlphaToMatchAllProperties(ReviewAlpha expectedReviewAlpha) {
        assertReviewAlphaAllPropertiesEquals(expectedReviewAlpha, getPersistedReviewAlpha(expectedReviewAlpha));
    }

    protected void assertPersistedReviewAlphaToMatchUpdatableProperties(ReviewAlpha expectedReviewAlpha) {
        assertReviewAlphaAllUpdatablePropertiesEquals(expectedReviewAlpha, getPersistedReviewAlpha(expectedReviewAlpha));
    }
}
