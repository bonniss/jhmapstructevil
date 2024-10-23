package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.ReviewSigmaAsserts.*;
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
import xyz.jhmapstruct.domain.ProductSigma;
import xyz.jhmapstruct.domain.ReviewSigma;
import xyz.jhmapstruct.repository.ReviewSigmaRepository;
import xyz.jhmapstruct.service.ReviewSigmaService;
import xyz.jhmapstruct.service.dto.ReviewSigmaDTO;
import xyz.jhmapstruct.service.mapper.ReviewSigmaMapper;

/**
 * Integration tests for the {@link ReviewSigmaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ReviewSigmaResourceIT {

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;
    private static final Integer SMALLER_RATING = 1 - 1;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_REVIEW_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REVIEW_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/review-sigmas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReviewSigmaRepository reviewSigmaRepository;

    @Mock
    private ReviewSigmaRepository reviewSigmaRepositoryMock;

    @Autowired
    private ReviewSigmaMapper reviewSigmaMapper;

    @Mock
    private ReviewSigmaService reviewSigmaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReviewSigmaMockMvc;

    private ReviewSigma reviewSigma;

    private ReviewSigma insertedReviewSigma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReviewSigma createEntity() {
        return new ReviewSigma().rating(DEFAULT_RATING).comment(DEFAULT_COMMENT).reviewDate(DEFAULT_REVIEW_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReviewSigma createUpdatedEntity() {
        return new ReviewSigma().rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);
    }

    @BeforeEach
    public void initTest() {
        reviewSigma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedReviewSigma != null) {
            reviewSigmaRepository.delete(insertedReviewSigma);
            insertedReviewSigma = null;
        }
    }

    @Test
    @Transactional
    void createReviewSigma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReviewSigma
        ReviewSigmaDTO reviewSigmaDTO = reviewSigmaMapper.toDto(reviewSigma);
        var returnedReviewSigmaDTO = om.readValue(
            restReviewSigmaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewSigmaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReviewSigmaDTO.class
        );

        // Validate the ReviewSigma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReviewSigma = reviewSigmaMapper.toEntity(returnedReviewSigmaDTO);
        assertReviewSigmaUpdatableFieldsEquals(returnedReviewSigma, getPersistedReviewSigma(returnedReviewSigma));

        insertedReviewSigma = returnedReviewSigma;
    }

    @Test
    @Transactional
    void createReviewSigmaWithExistingId() throws Exception {
        // Create the ReviewSigma with an existing ID
        reviewSigma.setId(1L);
        ReviewSigmaDTO reviewSigmaDTO = reviewSigmaMapper.toDto(reviewSigma);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReviewSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewSigmaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReviewSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRatingIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reviewSigma.setRating(null);

        // Create the ReviewSigma, which fails.
        ReviewSigmaDTO reviewSigmaDTO = reviewSigmaMapper.toDto(reviewSigma);

        restReviewSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewSigmaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReviewDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reviewSigma.setReviewDate(null);

        // Create the ReviewSigma, which fails.
        ReviewSigmaDTO reviewSigmaDTO = reviewSigmaMapper.toDto(reviewSigma);

        restReviewSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewSigmaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReviewSigmas() throws Exception {
        // Initialize the database
        insertedReviewSigma = reviewSigmaRepository.saveAndFlush(reviewSigma);

        // Get all the reviewSigmaList
        restReviewSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reviewSigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReviewSigmasWithEagerRelationshipsIsEnabled() throws Exception {
        when(reviewSigmaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReviewSigmaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(reviewSigmaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReviewSigmasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(reviewSigmaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReviewSigmaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(reviewSigmaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getReviewSigma() throws Exception {
        // Initialize the database
        insertedReviewSigma = reviewSigmaRepository.saveAndFlush(reviewSigma);

        // Get the reviewSigma
        restReviewSigmaMockMvc
            .perform(get(ENTITY_API_URL_ID, reviewSigma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reviewSigma.getId().intValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.reviewDate").value(DEFAULT_REVIEW_DATE.toString()));
    }

    @Test
    @Transactional
    void getReviewSigmasByIdFiltering() throws Exception {
        // Initialize the database
        insertedReviewSigma = reviewSigmaRepository.saveAndFlush(reviewSigma);

        Long id = reviewSigma.getId();

        defaultReviewSigmaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultReviewSigmaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultReviewSigmaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReviewSigmasByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReviewSigma = reviewSigmaRepository.saveAndFlush(reviewSigma);

        // Get all the reviewSigmaList where rating equals to
        defaultReviewSigmaFiltering("rating.equals=" + DEFAULT_RATING, "rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllReviewSigmasByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReviewSigma = reviewSigmaRepository.saveAndFlush(reviewSigma);

        // Get all the reviewSigmaList where rating in
        defaultReviewSigmaFiltering("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING, "rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllReviewSigmasByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReviewSigma = reviewSigmaRepository.saveAndFlush(reviewSigma);

        // Get all the reviewSigmaList where rating is not null
        defaultReviewSigmaFiltering("rating.specified=true", "rating.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewSigmasByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReviewSigma = reviewSigmaRepository.saveAndFlush(reviewSigma);

        // Get all the reviewSigmaList where rating is greater than or equal to
        defaultReviewSigmaFiltering("rating.greaterThanOrEqual=" + DEFAULT_RATING, "rating.greaterThanOrEqual=" + (DEFAULT_RATING + 1));
    }

    @Test
    @Transactional
    void getAllReviewSigmasByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReviewSigma = reviewSigmaRepository.saveAndFlush(reviewSigma);

        // Get all the reviewSigmaList where rating is less than or equal to
        defaultReviewSigmaFiltering("rating.lessThanOrEqual=" + DEFAULT_RATING, "rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllReviewSigmasByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReviewSigma = reviewSigmaRepository.saveAndFlush(reviewSigma);

        // Get all the reviewSigmaList where rating is less than
        defaultReviewSigmaFiltering("rating.lessThan=" + (DEFAULT_RATING + 1), "rating.lessThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllReviewSigmasByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReviewSigma = reviewSigmaRepository.saveAndFlush(reviewSigma);

        // Get all the reviewSigmaList where rating is greater than
        defaultReviewSigmaFiltering("rating.greaterThan=" + SMALLER_RATING, "rating.greaterThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllReviewSigmasByReviewDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReviewSigma = reviewSigmaRepository.saveAndFlush(reviewSigma);

        // Get all the reviewSigmaList where reviewDate equals to
        defaultReviewSigmaFiltering("reviewDate.equals=" + DEFAULT_REVIEW_DATE, "reviewDate.equals=" + UPDATED_REVIEW_DATE);
    }

    @Test
    @Transactional
    void getAllReviewSigmasByReviewDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReviewSigma = reviewSigmaRepository.saveAndFlush(reviewSigma);

        // Get all the reviewSigmaList where reviewDate in
        defaultReviewSigmaFiltering(
            "reviewDate.in=" + DEFAULT_REVIEW_DATE + "," + UPDATED_REVIEW_DATE,
            "reviewDate.in=" + UPDATED_REVIEW_DATE
        );
    }

    @Test
    @Transactional
    void getAllReviewSigmasByReviewDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReviewSigma = reviewSigmaRepository.saveAndFlush(reviewSigma);

        // Get all the reviewSigmaList where reviewDate is not null
        defaultReviewSigmaFiltering("reviewDate.specified=true", "reviewDate.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewSigmasByProductIsEqualToSomething() throws Exception {
        ProductSigma product;
        if (TestUtil.findAll(em, ProductSigma.class).isEmpty()) {
            reviewSigmaRepository.saveAndFlush(reviewSigma);
            product = ProductSigmaResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, ProductSigma.class).get(0);
        }
        em.persist(product);
        em.flush();
        reviewSigma.setProduct(product);
        reviewSigmaRepository.saveAndFlush(reviewSigma);
        Long productId = product.getId();
        // Get all the reviewSigmaList where product equals to productId
        defaultReviewSigmaShouldBeFound("productId.equals=" + productId);

        // Get all the reviewSigmaList where product equals to (productId + 1)
        defaultReviewSigmaShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllReviewSigmasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            reviewSigmaRepository.saveAndFlush(reviewSigma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        reviewSigma.setTenant(tenant);
        reviewSigmaRepository.saveAndFlush(reviewSigma);
        Long tenantId = tenant.getId();
        // Get all the reviewSigmaList where tenant equals to tenantId
        defaultReviewSigmaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the reviewSigmaList where tenant equals to (tenantId + 1)
        defaultReviewSigmaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultReviewSigmaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultReviewSigmaShouldBeFound(shouldBeFound);
        defaultReviewSigmaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReviewSigmaShouldBeFound(String filter) throws Exception {
        restReviewSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reviewSigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));

        // Check, that the count call also returns 1
        restReviewSigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReviewSigmaShouldNotBeFound(String filter) throws Exception {
        restReviewSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReviewSigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReviewSigma() throws Exception {
        // Get the reviewSigma
        restReviewSigmaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReviewSigma() throws Exception {
        // Initialize the database
        insertedReviewSigma = reviewSigmaRepository.saveAndFlush(reviewSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reviewSigma
        ReviewSigma updatedReviewSigma = reviewSigmaRepository.findById(reviewSigma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReviewSigma are not directly saved in db
        em.detach(updatedReviewSigma);
        updatedReviewSigma.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);
        ReviewSigmaDTO reviewSigmaDTO = reviewSigmaMapper.toDto(updatedReviewSigma);

        restReviewSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reviewSigmaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reviewSigmaDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReviewSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReviewSigmaToMatchAllProperties(updatedReviewSigma);
    }

    @Test
    @Transactional
    void putNonExistingReviewSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewSigma.setId(longCount.incrementAndGet());

        // Create the ReviewSigma
        ReviewSigmaDTO reviewSigmaDTO = reviewSigmaMapper.toDto(reviewSigma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReviewSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reviewSigmaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reviewSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReviewSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReviewSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewSigma.setId(longCount.incrementAndGet());

        // Create the ReviewSigma
        ReviewSigmaDTO reviewSigmaDTO = reviewSigmaMapper.toDto(reviewSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reviewSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReviewSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReviewSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewSigma.setId(longCount.incrementAndGet());

        // Create the ReviewSigma
        ReviewSigmaDTO reviewSigmaDTO = reviewSigmaMapper.toDto(reviewSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewSigmaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewSigmaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReviewSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReviewSigmaWithPatch() throws Exception {
        // Initialize the database
        insertedReviewSigma = reviewSigmaRepository.saveAndFlush(reviewSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reviewSigma using partial update
        ReviewSigma partialUpdatedReviewSigma = new ReviewSigma();
        partialUpdatedReviewSigma.setId(reviewSigma.getId());

        partialUpdatedReviewSigma.rating(UPDATED_RATING);

        restReviewSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReviewSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReviewSigma))
            )
            .andExpect(status().isOk());

        // Validate the ReviewSigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReviewSigmaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReviewSigma, reviewSigma),
            getPersistedReviewSigma(reviewSigma)
        );
    }

    @Test
    @Transactional
    void fullUpdateReviewSigmaWithPatch() throws Exception {
        // Initialize the database
        insertedReviewSigma = reviewSigmaRepository.saveAndFlush(reviewSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reviewSigma using partial update
        ReviewSigma partialUpdatedReviewSigma = new ReviewSigma();
        partialUpdatedReviewSigma.setId(reviewSigma.getId());

        partialUpdatedReviewSigma.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);

        restReviewSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReviewSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReviewSigma))
            )
            .andExpect(status().isOk());

        // Validate the ReviewSigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReviewSigmaUpdatableFieldsEquals(partialUpdatedReviewSigma, getPersistedReviewSigma(partialUpdatedReviewSigma));
    }

    @Test
    @Transactional
    void patchNonExistingReviewSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewSigma.setId(longCount.incrementAndGet());

        // Create the ReviewSigma
        ReviewSigmaDTO reviewSigmaDTO = reviewSigmaMapper.toDto(reviewSigma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReviewSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reviewSigmaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reviewSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReviewSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReviewSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewSigma.setId(longCount.incrementAndGet());

        // Create the ReviewSigma
        ReviewSigmaDTO reviewSigmaDTO = reviewSigmaMapper.toDto(reviewSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reviewSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReviewSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReviewSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewSigma.setId(longCount.incrementAndGet());

        // Create the ReviewSigma
        ReviewSigmaDTO reviewSigmaDTO = reviewSigmaMapper.toDto(reviewSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewSigmaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reviewSigmaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReviewSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReviewSigma() throws Exception {
        // Initialize the database
        insertedReviewSigma = reviewSigmaRepository.saveAndFlush(reviewSigma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reviewSigma
        restReviewSigmaMockMvc
            .perform(delete(ENTITY_API_URL_ID, reviewSigma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reviewSigmaRepository.count();
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

    protected ReviewSigma getPersistedReviewSigma(ReviewSigma reviewSigma) {
        return reviewSigmaRepository.findById(reviewSigma.getId()).orElseThrow();
    }

    protected void assertPersistedReviewSigmaToMatchAllProperties(ReviewSigma expectedReviewSigma) {
        assertReviewSigmaAllPropertiesEquals(expectedReviewSigma, getPersistedReviewSigma(expectedReviewSigma));
    }

    protected void assertPersistedReviewSigmaToMatchUpdatableProperties(ReviewSigma expectedReviewSigma) {
        assertReviewSigmaAllUpdatablePropertiesEquals(expectedReviewSigma, getPersistedReviewSigma(expectedReviewSigma));
    }
}
