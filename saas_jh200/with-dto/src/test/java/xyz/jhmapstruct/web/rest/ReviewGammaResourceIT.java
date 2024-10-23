package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.ReviewGammaAsserts.*;
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
import xyz.jhmapstruct.domain.ProductGamma;
import xyz.jhmapstruct.domain.ReviewGamma;
import xyz.jhmapstruct.repository.ReviewGammaRepository;
import xyz.jhmapstruct.service.ReviewGammaService;
import xyz.jhmapstruct.service.dto.ReviewGammaDTO;
import xyz.jhmapstruct.service.mapper.ReviewGammaMapper;

/**
 * Integration tests for the {@link ReviewGammaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ReviewGammaResourceIT {

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;
    private static final Integer SMALLER_RATING = 1 - 1;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_REVIEW_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REVIEW_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/review-gammas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReviewGammaRepository reviewGammaRepository;

    @Mock
    private ReviewGammaRepository reviewGammaRepositoryMock;

    @Autowired
    private ReviewGammaMapper reviewGammaMapper;

    @Mock
    private ReviewGammaService reviewGammaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReviewGammaMockMvc;

    private ReviewGamma reviewGamma;

    private ReviewGamma insertedReviewGamma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReviewGamma createEntity() {
        return new ReviewGamma().rating(DEFAULT_RATING).comment(DEFAULT_COMMENT).reviewDate(DEFAULT_REVIEW_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReviewGamma createUpdatedEntity() {
        return new ReviewGamma().rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);
    }

    @BeforeEach
    public void initTest() {
        reviewGamma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedReviewGamma != null) {
            reviewGammaRepository.delete(insertedReviewGamma);
            insertedReviewGamma = null;
        }
    }

    @Test
    @Transactional
    void createReviewGamma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReviewGamma
        ReviewGammaDTO reviewGammaDTO = reviewGammaMapper.toDto(reviewGamma);
        var returnedReviewGammaDTO = om.readValue(
            restReviewGammaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewGammaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReviewGammaDTO.class
        );

        // Validate the ReviewGamma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReviewGamma = reviewGammaMapper.toEntity(returnedReviewGammaDTO);
        assertReviewGammaUpdatableFieldsEquals(returnedReviewGamma, getPersistedReviewGamma(returnedReviewGamma));

        insertedReviewGamma = returnedReviewGamma;
    }

    @Test
    @Transactional
    void createReviewGammaWithExistingId() throws Exception {
        // Create the ReviewGamma with an existing ID
        reviewGamma.setId(1L);
        ReviewGammaDTO reviewGammaDTO = reviewGammaMapper.toDto(reviewGamma);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReviewGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewGammaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReviewGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRatingIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reviewGamma.setRating(null);

        // Create the ReviewGamma, which fails.
        ReviewGammaDTO reviewGammaDTO = reviewGammaMapper.toDto(reviewGamma);

        restReviewGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewGammaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReviewDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reviewGamma.setReviewDate(null);

        // Create the ReviewGamma, which fails.
        ReviewGammaDTO reviewGammaDTO = reviewGammaMapper.toDto(reviewGamma);

        restReviewGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewGammaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReviewGammas() throws Exception {
        // Initialize the database
        insertedReviewGamma = reviewGammaRepository.saveAndFlush(reviewGamma);

        // Get all the reviewGammaList
        restReviewGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reviewGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReviewGammasWithEagerRelationshipsIsEnabled() throws Exception {
        when(reviewGammaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReviewGammaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(reviewGammaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReviewGammasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(reviewGammaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReviewGammaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(reviewGammaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getReviewGamma() throws Exception {
        // Initialize the database
        insertedReviewGamma = reviewGammaRepository.saveAndFlush(reviewGamma);

        // Get the reviewGamma
        restReviewGammaMockMvc
            .perform(get(ENTITY_API_URL_ID, reviewGamma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reviewGamma.getId().intValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.reviewDate").value(DEFAULT_REVIEW_DATE.toString()));
    }

    @Test
    @Transactional
    void getReviewGammasByIdFiltering() throws Exception {
        // Initialize the database
        insertedReviewGamma = reviewGammaRepository.saveAndFlush(reviewGamma);

        Long id = reviewGamma.getId();

        defaultReviewGammaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultReviewGammaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultReviewGammaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReviewGammasByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReviewGamma = reviewGammaRepository.saveAndFlush(reviewGamma);

        // Get all the reviewGammaList where rating equals to
        defaultReviewGammaFiltering("rating.equals=" + DEFAULT_RATING, "rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllReviewGammasByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReviewGamma = reviewGammaRepository.saveAndFlush(reviewGamma);

        // Get all the reviewGammaList where rating in
        defaultReviewGammaFiltering("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING, "rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllReviewGammasByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReviewGamma = reviewGammaRepository.saveAndFlush(reviewGamma);

        // Get all the reviewGammaList where rating is not null
        defaultReviewGammaFiltering("rating.specified=true", "rating.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewGammasByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReviewGamma = reviewGammaRepository.saveAndFlush(reviewGamma);

        // Get all the reviewGammaList where rating is greater than or equal to
        defaultReviewGammaFiltering("rating.greaterThanOrEqual=" + DEFAULT_RATING, "rating.greaterThanOrEqual=" + (DEFAULT_RATING + 1));
    }

    @Test
    @Transactional
    void getAllReviewGammasByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReviewGamma = reviewGammaRepository.saveAndFlush(reviewGamma);

        // Get all the reviewGammaList where rating is less than or equal to
        defaultReviewGammaFiltering("rating.lessThanOrEqual=" + DEFAULT_RATING, "rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllReviewGammasByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReviewGamma = reviewGammaRepository.saveAndFlush(reviewGamma);

        // Get all the reviewGammaList where rating is less than
        defaultReviewGammaFiltering("rating.lessThan=" + (DEFAULT_RATING + 1), "rating.lessThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllReviewGammasByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReviewGamma = reviewGammaRepository.saveAndFlush(reviewGamma);

        // Get all the reviewGammaList where rating is greater than
        defaultReviewGammaFiltering("rating.greaterThan=" + SMALLER_RATING, "rating.greaterThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllReviewGammasByReviewDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReviewGamma = reviewGammaRepository.saveAndFlush(reviewGamma);

        // Get all the reviewGammaList where reviewDate equals to
        defaultReviewGammaFiltering("reviewDate.equals=" + DEFAULT_REVIEW_DATE, "reviewDate.equals=" + UPDATED_REVIEW_DATE);
    }

    @Test
    @Transactional
    void getAllReviewGammasByReviewDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReviewGamma = reviewGammaRepository.saveAndFlush(reviewGamma);

        // Get all the reviewGammaList where reviewDate in
        defaultReviewGammaFiltering(
            "reviewDate.in=" + DEFAULT_REVIEW_DATE + "," + UPDATED_REVIEW_DATE,
            "reviewDate.in=" + UPDATED_REVIEW_DATE
        );
    }

    @Test
    @Transactional
    void getAllReviewGammasByReviewDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReviewGamma = reviewGammaRepository.saveAndFlush(reviewGamma);

        // Get all the reviewGammaList where reviewDate is not null
        defaultReviewGammaFiltering("reviewDate.specified=true", "reviewDate.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewGammasByProductIsEqualToSomething() throws Exception {
        ProductGamma product;
        if (TestUtil.findAll(em, ProductGamma.class).isEmpty()) {
            reviewGammaRepository.saveAndFlush(reviewGamma);
            product = ProductGammaResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, ProductGamma.class).get(0);
        }
        em.persist(product);
        em.flush();
        reviewGamma.setProduct(product);
        reviewGammaRepository.saveAndFlush(reviewGamma);
        Long productId = product.getId();
        // Get all the reviewGammaList where product equals to productId
        defaultReviewGammaShouldBeFound("productId.equals=" + productId);

        // Get all the reviewGammaList where product equals to (productId + 1)
        defaultReviewGammaShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllReviewGammasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            reviewGammaRepository.saveAndFlush(reviewGamma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        reviewGamma.setTenant(tenant);
        reviewGammaRepository.saveAndFlush(reviewGamma);
        Long tenantId = tenant.getId();
        // Get all the reviewGammaList where tenant equals to tenantId
        defaultReviewGammaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the reviewGammaList where tenant equals to (tenantId + 1)
        defaultReviewGammaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultReviewGammaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultReviewGammaShouldBeFound(shouldBeFound);
        defaultReviewGammaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReviewGammaShouldBeFound(String filter) throws Exception {
        restReviewGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reviewGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));

        // Check, that the count call also returns 1
        restReviewGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReviewGammaShouldNotBeFound(String filter) throws Exception {
        restReviewGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReviewGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReviewGamma() throws Exception {
        // Get the reviewGamma
        restReviewGammaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReviewGamma() throws Exception {
        // Initialize the database
        insertedReviewGamma = reviewGammaRepository.saveAndFlush(reviewGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reviewGamma
        ReviewGamma updatedReviewGamma = reviewGammaRepository.findById(reviewGamma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReviewGamma are not directly saved in db
        em.detach(updatedReviewGamma);
        updatedReviewGamma.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);
        ReviewGammaDTO reviewGammaDTO = reviewGammaMapper.toDto(updatedReviewGamma);

        restReviewGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reviewGammaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reviewGammaDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReviewGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReviewGammaToMatchAllProperties(updatedReviewGamma);
    }

    @Test
    @Transactional
    void putNonExistingReviewGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewGamma.setId(longCount.incrementAndGet());

        // Create the ReviewGamma
        ReviewGammaDTO reviewGammaDTO = reviewGammaMapper.toDto(reviewGamma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReviewGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reviewGammaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reviewGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReviewGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReviewGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewGamma.setId(longCount.incrementAndGet());

        // Create the ReviewGamma
        ReviewGammaDTO reviewGammaDTO = reviewGammaMapper.toDto(reviewGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reviewGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReviewGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReviewGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewGamma.setId(longCount.incrementAndGet());

        // Create the ReviewGamma
        ReviewGammaDTO reviewGammaDTO = reviewGammaMapper.toDto(reviewGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewGammaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewGammaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReviewGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReviewGammaWithPatch() throws Exception {
        // Initialize the database
        insertedReviewGamma = reviewGammaRepository.saveAndFlush(reviewGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reviewGamma using partial update
        ReviewGamma partialUpdatedReviewGamma = new ReviewGamma();
        partialUpdatedReviewGamma.setId(reviewGamma.getId());

        partialUpdatedReviewGamma.comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);

        restReviewGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReviewGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReviewGamma))
            )
            .andExpect(status().isOk());

        // Validate the ReviewGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReviewGammaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReviewGamma, reviewGamma),
            getPersistedReviewGamma(reviewGamma)
        );
    }

    @Test
    @Transactional
    void fullUpdateReviewGammaWithPatch() throws Exception {
        // Initialize the database
        insertedReviewGamma = reviewGammaRepository.saveAndFlush(reviewGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reviewGamma using partial update
        ReviewGamma partialUpdatedReviewGamma = new ReviewGamma();
        partialUpdatedReviewGamma.setId(reviewGamma.getId());

        partialUpdatedReviewGamma.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);

        restReviewGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReviewGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReviewGamma))
            )
            .andExpect(status().isOk());

        // Validate the ReviewGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReviewGammaUpdatableFieldsEquals(partialUpdatedReviewGamma, getPersistedReviewGamma(partialUpdatedReviewGamma));
    }

    @Test
    @Transactional
    void patchNonExistingReviewGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewGamma.setId(longCount.incrementAndGet());

        // Create the ReviewGamma
        ReviewGammaDTO reviewGammaDTO = reviewGammaMapper.toDto(reviewGamma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReviewGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reviewGammaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reviewGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReviewGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReviewGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewGamma.setId(longCount.incrementAndGet());

        // Create the ReviewGamma
        ReviewGammaDTO reviewGammaDTO = reviewGammaMapper.toDto(reviewGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reviewGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReviewGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReviewGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewGamma.setId(longCount.incrementAndGet());

        // Create the ReviewGamma
        ReviewGammaDTO reviewGammaDTO = reviewGammaMapper.toDto(reviewGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewGammaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reviewGammaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReviewGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReviewGamma() throws Exception {
        // Initialize the database
        insertedReviewGamma = reviewGammaRepository.saveAndFlush(reviewGamma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reviewGamma
        restReviewGammaMockMvc
            .perform(delete(ENTITY_API_URL_ID, reviewGamma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reviewGammaRepository.count();
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

    protected ReviewGamma getPersistedReviewGamma(ReviewGamma reviewGamma) {
        return reviewGammaRepository.findById(reviewGamma.getId()).orElseThrow();
    }

    protected void assertPersistedReviewGammaToMatchAllProperties(ReviewGamma expectedReviewGamma) {
        assertReviewGammaAllPropertiesEquals(expectedReviewGamma, getPersistedReviewGamma(expectedReviewGamma));
    }

    protected void assertPersistedReviewGammaToMatchUpdatableProperties(ReviewGamma expectedReviewGamma) {
        assertReviewGammaAllUpdatablePropertiesEquals(expectedReviewGamma, getPersistedReviewGamma(expectedReviewGamma));
    }
}
