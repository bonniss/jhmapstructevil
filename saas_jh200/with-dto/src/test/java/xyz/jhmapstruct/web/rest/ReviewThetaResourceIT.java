package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.ReviewThetaAsserts.*;
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
import xyz.jhmapstruct.domain.ProductTheta;
import xyz.jhmapstruct.domain.ReviewTheta;
import xyz.jhmapstruct.repository.ReviewThetaRepository;
import xyz.jhmapstruct.service.ReviewThetaService;
import xyz.jhmapstruct.service.dto.ReviewThetaDTO;
import xyz.jhmapstruct.service.mapper.ReviewThetaMapper;

/**
 * Integration tests for the {@link ReviewThetaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ReviewThetaResourceIT {

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;
    private static final Integer SMALLER_RATING = 1 - 1;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_REVIEW_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REVIEW_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/review-thetas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReviewThetaRepository reviewThetaRepository;

    @Mock
    private ReviewThetaRepository reviewThetaRepositoryMock;

    @Autowired
    private ReviewThetaMapper reviewThetaMapper;

    @Mock
    private ReviewThetaService reviewThetaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReviewThetaMockMvc;

    private ReviewTheta reviewTheta;

    private ReviewTheta insertedReviewTheta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReviewTheta createEntity() {
        return new ReviewTheta().rating(DEFAULT_RATING).comment(DEFAULT_COMMENT).reviewDate(DEFAULT_REVIEW_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReviewTheta createUpdatedEntity() {
        return new ReviewTheta().rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);
    }

    @BeforeEach
    public void initTest() {
        reviewTheta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedReviewTheta != null) {
            reviewThetaRepository.delete(insertedReviewTheta);
            insertedReviewTheta = null;
        }
    }

    @Test
    @Transactional
    void createReviewTheta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReviewTheta
        ReviewThetaDTO reviewThetaDTO = reviewThetaMapper.toDto(reviewTheta);
        var returnedReviewThetaDTO = om.readValue(
            restReviewThetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewThetaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReviewThetaDTO.class
        );

        // Validate the ReviewTheta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReviewTheta = reviewThetaMapper.toEntity(returnedReviewThetaDTO);
        assertReviewThetaUpdatableFieldsEquals(returnedReviewTheta, getPersistedReviewTheta(returnedReviewTheta));

        insertedReviewTheta = returnedReviewTheta;
    }

    @Test
    @Transactional
    void createReviewThetaWithExistingId() throws Exception {
        // Create the ReviewTheta with an existing ID
        reviewTheta.setId(1L);
        ReviewThetaDTO reviewThetaDTO = reviewThetaMapper.toDto(reviewTheta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReviewThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewThetaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReviewTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRatingIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reviewTheta.setRating(null);

        // Create the ReviewTheta, which fails.
        ReviewThetaDTO reviewThetaDTO = reviewThetaMapper.toDto(reviewTheta);

        restReviewThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewThetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReviewDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reviewTheta.setReviewDate(null);

        // Create the ReviewTheta, which fails.
        ReviewThetaDTO reviewThetaDTO = reviewThetaMapper.toDto(reviewTheta);

        restReviewThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewThetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReviewThetas() throws Exception {
        // Initialize the database
        insertedReviewTheta = reviewThetaRepository.saveAndFlush(reviewTheta);

        // Get all the reviewThetaList
        restReviewThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reviewTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReviewThetasWithEagerRelationshipsIsEnabled() throws Exception {
        when(reviewThetaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReviewThetaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(reviewThetaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReviewThetasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(reviewThetaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReviewThetaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(reviewThetaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getReviewTheta() throws Exception {
        // Initialize the database
        insertedReviewTheta = reviewThetaRepository.saveAndFlush(reviewTheta);

        // Get the reviewTheta
        restReviewThetaMockMvc
            .perform(get(ENTITY_API_URL_ID, reviewTheta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reviewTheta.getId().intValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.reviewDate").value(DEFAULT_REVIEW_DATE.toString()));
    }

    @Test
    @Transactional
    void getReviewThetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedReviewTheta = reviewThetaRepository.saveAndFlush(reviewTheta);

        Long id = reviewTheta.getId();

        defaultReviewThetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultReviewThetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultReviewThetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReviewThetasByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReviewTheta = reviewThetaRepository.saveAndFlush(reviewTheta);

        // Get all the reviewThetaList where rating equals to
        defaultReviewThetaFiltering("rating.equals=" + DEFAULT_RATING, "rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllReviewThetasByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReviewTheta = reviewThetaRepository.saveAndFlush(reviewTheta);

        // Get all the reviewThetaList where rating in
        defaultReviewThetaFiltering("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING, "rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllReviewThetasByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReviewTheta = reviewThetaRepository.saveAndFlush(reviewTheta);

        // Get all the reviewThetaList where rating is not null
        defaultReviewThetaFiltering("rating.specified=true", "rating.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewThetasByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReviewTheta = reviewThetaRepository.saveAndFlush(reviewTheta);

        // Get all the reviewThetaList where rating is greater than or equal to
        defaultReviewThetaFiltering("rating.greaterThanOrEqual=" + DEFAULT_RATING, "rating.greaterThanOrEqual=" + (DEFAULT_RATING + 1));
    }

    @Test
    @Transactional
    void getAllReviewThetasByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReviewTheta = reviewThetaRepository.saveAndFlush(reviewTheta);

        // Get all the reviewThetaList where rating is less than or equal to
        defaultReviewThetaFiltering("rating.lessThanOrEqual=" + DEFAULT_RATING, "rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllReviewThetasByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReviewTheta = reviewThetaRepository.saveAndFlush(reviewTheta);

        // Get all the reviewThetaList where rating is less than
        defaultReviewThetaFiltering("rating.lessThan=" + (DEFAULT_RATING + 1), "rating.lessThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllReviewThetasByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReviewTheta = reviewThetaRepository.saveAndFlush(reviewTheta);

        // Get all the reviewThetaList where rating is greater than
        defaultReviewThetaFiltering("rating.greaterThan=" + SMALLER_RATING, "rating.greaterThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllReviewThetasByReviewDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReviewTheta = reviewThetaRepository.saveAndFlush(reviewTheta);

        // Get all the reviewThetaList where reviewDate equals to
        defaultReviewThetaFiltering("reviewDate.equals=" + DEFAULT_REVIEW_DATE, "reviewDate.equals=" + UPDATED_REVIEW_DATE);
    }

    @Test
    @Transactional
    void getAllReviewThetasByReviewDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReviewTheta = reviewThetaRepository.saveAndFlush(reviewTheta);

        // Get all the reviewThetaList where reviewDate in
        defaultReviewThetaFiltering(
            "reviewDate.in=" + DEFAULT_REVIEW_DATE + "," + UPDATED_REVIEW_DATE,
            "reviewDate.in=" + UPDATED_REVIEW_DATE
        );
    }

    @Test
    @Transactional
    void getAllReviewThetasByReviewDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReviewTheta = reviewThetaRepository.saveAndFlush(reviewTheta);

        // Get all the reviewThetaList where reviewDate is not null
        defaultReviewThetaFiltering("reviewDate.specified=true", "reviewDate.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewThetasByProductIsEqualToSomething() throws Exception {
        ProductTheta product;
        if (TestUtil.findAll(em, ProductTheta.class).isEmpty()) {
            reviewThetaRepository.saveAndFlush(reviewTheta);
            product = ProductThetaResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, ProductTheta.class).get(0);
        }
        em.persist(product);
        em.flush();
        reviewTheta.setProduct(product);
        reviewThetaRepository.saveAndFlush(reviewTheta);
        Long productId = product.getId();
        // Get all the reviewThetaList where product equals to productId
        defaultReviewThetaShouldBeFound("productId.equals=" + productId);

        // Get all the reviewThetaList where product equals to (productId + 1)
        defaultReviewThetaShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllReviewThetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            reviewThetaRepository.saveAndFlush(reviewTheta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        reviewTheta.setTenant(tenant);
        reviewThetaRepository.saveAndFlush(reviewTheta);
        Long tenantId = tenant.getId();
        // Get all the reviewThetaList where tenant equals to tenantId
        defaultReviewThetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the reviewThetaList where tenant equals to (tenantId + 1)
        defaultReviewThetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultReviewThetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultReviewThetaShouldBeFound(shouldBeFound);
        defaultReviewThetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReviewThetaShouldBeFound(String filter) throws Exception {
        restReviewThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reviewTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));

        // Check, that the count call also returns 1
        restReviewThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReviewThetaShouldNotBeFound(String filter) throws Exception {
        restReviewThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReviewThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReviewTheta() throws Exception {
        // Get the reviewTheta
        restReviewThetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReviewTheta() throws Exception {
        // Initialize the database
        insertedReviewTheta = reviewThetaRepository.saveAndFlush(reviewTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reviewTheta
        ReviewTheta updatedReviewTheta = reviewThetaRepository.findById(reviewTheta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReviewTheta are not directly saved in db
        em.detach(updatedReviewTheta);
        updatedReviewTheta.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);
        ReviewThetaDTO reviewThetaDTO = reviewThetaMapper.toDto(updatedReviewTheta);

        restReviewThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reviewThetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reviewThetaDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReviewTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReviewThetaToMatchAllProperties(updatedReviewTheta);
    }

    @Test
    @Transactional
    void putNonExistingReviewTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewTheta.setId(longCount.incrementAndGet());

        // Create the ReviewTheta
        ReviewThetaDTO reviewThetaDTO = reviewThetaMapper.toDto(reviewTheta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReviewThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reviewThetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reviewThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReviewTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReviewTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewTheta.setId(longCount.incrementAndGet());

        // Create the ReviewTheta
        ReviewThetaDTO reviewThetaDTO = reviewThetaMapper.toDto(reviewTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reviewThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReviewTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReviewTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewTheta.setId(longCount.incrementAndGet());

        // Create the ReviewTheta
        ReviewThetaDTO reviewThetaDTO = reviewThetaMapper.toDto(reviewTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewThetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewThetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReviewTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReviewThetaWithPatch() throws Exception {
        // Initialize the database
        insertedReviewTheta = reviewThetaRepository.saveAndFlush(reviewTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reviewTheta using partial update
        ReviewTheta partialUpdatedReviewTheta = new ReviewTheta();
        partialUpdatedReviewTheta.setId(reviewTheta.getId());

        partialUpdatedReviewTheta.comment(UPDATED_COMMENT);

        restReviewThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReviewTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReviewTheta))
            )
            .andExpect(status().isOk());

        // Validate the ReviewTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReviewThetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReviewTheta, reviewTheta),
            getPersistedReviewTheta(reviewTheta)
        );
    }

    @Test
    @Transactional
    void fullUpdateReviewThetaWithPatch() throws Exception {
        // Initialize the database
        insertedReviewTheta = reviewThetaRepository.saveAndFlush(reviewTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reviewTheta using partial update
        ReviewTheta partialUpdatedReviewTheta = new ReviewTheta();
        partialUpdatedReviewTheta.setId(reviewTheta.getId());

        partialUpdatedReviewTheta.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);

        restReviewThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReviewTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReviewTheta))
            )
            .andExpect(status().isOk());

        // Validate the ReviewTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReviewThetaUpdatableFieldsEquals(partialUpdatedReviewTheta, getPersistedReviewTheta(partialUpdatedReviewTheta));
    }

    @Test
    @Transactional
    void patchNonExistingReviewTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewTheta.setId(longCount.incrementAndGet());

        // Create the ReviewTheta
        ReviewThetaDTO reviewThetaDTO = reviewThetaMapper.toDto(reviewTheta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReviewThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reviewThetaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reviewThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReviewTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReviewTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewTheta.setId(longCount.incrementAndGet());

        // Create the ReviewTheta
        ReviewThetaDTO reviewThetaDTO = reviewThetaMapper.toDto(reviewTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reviewThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReviewTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReviewTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewTheta.setId(longCount.incrementAndGet());

        // Create the ReviewTheta
        ReviewThetaDTO reviewThetaDTO = reviewThetaMapper.toDto(reviewTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewThetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reviewThetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReviewTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReviewTheta() throws Exception {
        // Initialize the database
        insertedReviewTheta = reviewThetaRepository.saveAndFlush(reviewTheta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reviewTheta
        restReviewThetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, reviewTheta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reviewThetaRepository.count();
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

    protected ReviewTheta getPersistedReviewTheta(ReviewTheta reviewTheta) {
        return reviewThetaRepository.findById(reviewTheta.getId()).orElseThrow();
    }

    protected void assertPersistedReviewThetaToMatchAllProperties(ReviewTheta expectedReviewTheta) {
        assertReviewThetaAllPropertiesEquals(expectedReviewTheta, getPersistedReviewTheta(expectedReviewTheta));
    }

    protected void assertPersistedReviewThetaToMatchUpdatableProperties(ReviewTheta expectedReviewTheta) {
        assertReviewThetaAllUpdatablePropertiesEquals(expectedReviewTheta, getPersistedReviewTheta(expectedReviewTheta));
    }
}
