package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.ReviewBetaAsserts.*;
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
import xyz.jhmapstruct.domain.ProductBeta;
import xyz.jhmapstruct.domain.ReviewBeta;
import xyz.jhmapstruct.repository.ReviewBetaRepository;
import xyz.jhmapstruct.service.ReviewBetaService;
import xyz.jhmapstruct.service.dto.ReviewBetaDTO;
import xyz.jhmapstruct.service.mapper.ReviewBetaMapper;

/**
 * Integration tests for the {@link ReviewBetaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ReviewBetaResourceIT {

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;
    private static final Integer SMALLER_RATING = 1 - 1;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_REVIEW_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REVIEW_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/review-betas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReviewBetaRepository reviewBetaRepository;

    @Mock
    private ReviewBetaRepository reviewBetaRepositoryMock;

    @Autowired
    private ReviewBetaMapper reviewBetaMapper;

    @Mock
    private ReviewBetaService reviewBetaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReviewBetaMockMvc;

    private ReviewBeta reviewBeta;

    private ReviewBeta insertedReviewBeta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReviewBeta createEntity() {
        return new ReviewBeta().rating(DEFAULT_RATING).comment(DEFAULT_COMMENT).reviewDate(DEFAULT_REVIEW_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReviewBeta createUpdatedEntity() {
        return new ReviewBeta().rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);
    }

    @BeforeEach
    public void initTest() {
        reviewBeta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedReviewBeta != null) {
            reviewBetaRepository.delete(insertedReviewBeta);
            insertedReviewBeta = null;
        }
    }

    @Test
    @Transactional
    void createReviewBeta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReviewBeta
        ReviewBetaDTO reviewBetaDTO = reviewBetaMapper.toDto(reviewBeta);
        var returnedReviewBetaDTO = om.readValue(
            restReviewBetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewBetaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReviewBetaDTO.class
        );

        // Validate the ReviewBeta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReviewBeta = reviewBetaMapper.toEntity(returnedReviewBetaDTO);
        assertReviewBetaUpdatableFieldsEquals(returnedReviewBeta, getPersistedReviewBeta(returnedReviewBeta));

        insertedReviewBeta = returnedReviewBeta;
    }

    @Test
    @Transactional
    void createReviewBetaWithExistingId() throws Exception {
        // Create the ReviewBeta with an existing ID
        reviewBeta.setId(1L);
        ReviewBetaDTO reviewBetaDTO = reviewBetaMapper.toDto(reviewBeta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReviewBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewBetaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReviewBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRatingIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reviewBeta.setRating(null);

        // Create the ReviewBeta, which fails.
        ReviewBetaDTO reviewBetaDTO = reviewBetaMapper.toDto(reviewBeta);

        restReviewBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewBetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReviewDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reviewBeta.setReviewDate(null);

        // Create the ReviewBeta, which fails.
        ReviewBetaDTO reviewBetaDTO = reviewBetaMapper.toDto(reviewBeta);

        restReviewBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewBetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReviewBetas() throws Exception {
        // Initialize the database
        insertedReviewBeta = reviewBetaRepository.saveAndFlush(reviewBeta);

        // Get all the reviewBetaList
        restReviewBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reviewBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReviewBetasWithEagerRelationshipsIsEnabled() throws Exception {
        when(reviewBetaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReviewBetaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(reviewBetaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReviewBetasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(reviewBetaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReviewBetaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(reviewBetaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getReviewBeta() throws Exception {
        // Initialize the database
        insertedReviewBeta = reviewBetaRepository.saveAndFlush(reviewBeta);

        // Get the reviewBeta
        restReviewBetaMockMvc
            .perform(get(ENTITY_API_URL_ID, reviewBeta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reviewBeta.getId().intValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.reviewDate").value(DEFAULT_REVIEW_DATE.toString()));
    }

    @Test
    @Transactional
    void getReviewBetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedReviewBeta = reviewBetaRepository.saveAndFlush(reviewBeta);

        Long id = reviewBeta.getId();

        defaultReviewBetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultReviewBetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultReviewBetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReviewBetasByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReviewBeta = reviewBetaRepository.saveAndFlush(reviewBeta);

        // Get all the reviewBetaList where rating equals to
        defaultReviewBetaFiltering("rating.equals=" + DEFAULT_RATING, "rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllReviewBetasByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReviewBeta = reviewBetaRepository.saveAndFlush(reviewBeta);

        // Get all the reviewBetaList where rating in
        defaultReviewBetaFiltering("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING, "rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllReviewBetasByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReviewBeta = reviewBetaRepository.saveAndFlush(reviewBeta);

        // Get all the reviewBetaList where rating is not null
        defaultReviewBetaFiltering("rating.specified=true", "rating.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewBetasByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReviewBeta = reviewBetaRepository.saveAndFlush(reviewBeta);

        // Get all the reviewBetaList where rating is greater than or equal to
        defaultReviewBetaFiltering("rating.greaterThanOrEqual=" + DEFAULT_RATING, "rating.greaterThanOrEqual=" + (DEFAULT_RATING + 1));
    }

    @Test
    @Transactional
    void getAllReviewBetasByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReviewBeta = reviewBetaRepository.saveAndFlush(reviewBeta);

        // Get all the reviewBetaList where rating is less than or equal to
        defaultReviewBetaFiltering("rating.lessThanOrEqual=" + DEFAULT_RATING, "rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllReviewBetasByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReviewBeta = reviewBetaRepository.saveAndFlush(reviewBeta);

        // Get all the reviewBetaList where rating is less than
        defaultReviewBetaFiltering("rating.lessThan=" + (DEFAULT_RATING + 1), "rating.lessThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllReviewBetasByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReviewBeta = reviewBetaRepository.saveAndFlush(reviewBeta);

        // Get all the reviewBetaList where rating is greater than
        defaultReviewBetaFiltering("rating.greaterThan=" + SMALLER_RATING, "rating.greaterThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllReviewBetasByReviewDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReviewBeta = reviewBetaRepository.saveAndFlush(reviewBeta);

        // Get all the reviewBetaList where reviewDate equals to
        defaultReviewBetaFiltering("reviewDate.equals=" + DEFAULT_REVIEW_DATE, "reviewDate.equals=" + UPDATED_REVIEW_DATE);
    }

    @Test
    @Transactional
    void getAllReviewBetasByReviewDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReviewBeta = reviewBetaRepository.saveAndFlush(reviewBeta);

        // Get all the reviewBetaList where reviewDate in
        defaultReviewBetaFiltering(
            "reviewDate.in=" + DEFAULT_REVIEW_DATE + "," + UPDATED_REVIEW_DATE,
            "reviewDate.in=" + UPDATED_REVIEW_DATE
        );
    }

    @Test
    @Transactional
    void getAllReviewBetasByReviewDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReviewBeta = reviewBetaRepository.saveAndFlush(reviewBeta);

        // Get all the reviewBetaList where reviewDate is not null
        defaultReviewBetaFiltering("reviewDate.specified=true", "reviewDate.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewBetasByProductIsEqualToSomething() throws Exception {
        ProductBeta product;
        if (TestUtil.findAll(em, ProductBeta.class).isEmpty()) {
            reviewBetaRepository.saveAndFlush(reviewBeta);
            product = ProductBetaResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, ProductBeta.class).get(0);
        }
        em.persist(product);
        em.flush();
        reviewBeta.setProduct(product);
        reviewBetaRepository.saveAndFlush(reviewBeta);
        Long productId = product.getId();
        // Get all the reviewBetaList where product equals to productId
        defaultReviewBetaShouldBeFound("productId.equals=" + productId);

        // Get all the reviewBetaList where product equals to (productId + 1)
        defaultReviewBetaShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllReviewBetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            reviewBetaRepository.saveAndFlush(reviewBeta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        reviewBeta.setTenant(tenant);
        reviewBetaRepository.saveAndFlush(reviewBeta);
        Long tenantId = tenant.getId();
        // Get all the reviewBetaList where tenant equals to tenantId
        defaultReviewBetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the reviewBetaList where tenant equals to (tenantId + 1)
        defaultReviewBetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultReviewBetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultReviewBetaShouldBeFound(shouldBeFound);
        defaultReviewBetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReviewBetaShouldBeFound(String filter) throws Exception {
        restReviewBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reviewBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));

        // Check, that the count call also returns 1
        restReviewBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReviewBetaShouldNotBeFound(String filter) throws Exception {
        restReviewBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReviewBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReviewBeta() throws Exception {
        // Get the reviewBeta
        restReviewBetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReviewBeta() throws Exception {
        // Initialize the database
        insertedReviewBeta = reviewBetaRepository.saveAndFlush(reviewBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reviewBeta
        ReviewBeta updatedReviewBeta = reviewBetaRepository.findById(reviewBeta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReviewBeta are not directly saved in db
        em.detach(updatedReviewBeta);
        updatedReviewBeta.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);
        ReviewBetaDTO reviewBetaDTO = reviewBetaMapper.toDto(updatedReviewBeta);

        restReviewBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reviewBetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reviewBetaDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReviewBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReviewBetaToMatchAllProperties(updatedReviewBeta);
    }

    @Test
    @Transactional
    void putNonExistingReviewBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewBeta.setId(longCount.incrementAndGet());

        // Create the ReviewBeta
        ReviewBetaDTO reviewBetaDTO = reviewBetaMapper.toDto(reviewBeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReviewBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reviewBetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reviewBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReviewBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReviewBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewBeta.setId(longCount.incrementAndGet());

        // Create the ReviewBeta
        ReviewBetaDTO reviewBetaDTO = reviewBetaMapper.toDto(reviewBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reviewBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReviewBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReviewBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewBeta.setId(longCount.incrementAndGet());

        // Create the ReviewBeta
        ReviewBetaDTO reviewBetaDTO = reviewBetaMapper.toDto(reviewBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewBetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewBetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReviewBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReviewBetaWithPatch() throws Exception {
        // Initialize the database
        insertedReviewBeta = reviewBetaRepository.saveAndFlush(reviewBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reviewBeta using partial update
        ReviewBeta partialUpdatedReviewBeta = new ReviewBeta();
        partialUpdatedReviewBeta.setId(reviewBeta.getId());

        partialUpdatedReviewBeta.rating(UPDATED_RATING).reviewDate(UPDATED_REVIEW_DATE);

        restReviewBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReviewBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReviewBeta))
            )
            .andExpect(status().isOk());

        // Validate the ReviewBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReviewBetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReviewBeta, reviewBeta),
            getPersistedReviewBeta(reviewBeta)
        );
    }

    @Test
    @Transactional
    void fullUpdateReviewBetaWithPatch() throws Exception {
        // Initialize the database
        insertedReviewBeta = reviewBetaRepository.saveAndFlush(reviewBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reviewBeta using partial update
        ReviewBeta partialUpdatedReviewBeta = new ReviewBeta();
        partialUpdatedReviewBeta.setId(reviewBeta.getId());

        partialUpdatedReviewBeta.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);

        restReviewBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReviewBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReviewBeta))
            )
            .andExpect(status().isOk());

        // Validate the ReviewBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReviewBetaUpdatableFieldsEquals(partialUpdatedReviewBeta, getPersistedReviewBeta(partialUpdatedReviewBeta));
    }

    @Test
    @Transactional
    void patchNonExistingReviewBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewBeta.setId(longCount.incrementAndGet());

        // Create the ReviewBeta
        ReviewBetaDTO reviewBetaDTO = reviewBetaMapper.toDto(reviewBeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReviewBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reviewBetaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reviewBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReviewBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReviewBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewBeta.setId(longCount.incrementAndGet());

        // Create the ReviewBeta
        ReviewBetaDTO reviewBetaDTO = reviewBetaMapper.toDto(reviewBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reviewBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReviewBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReviewBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewBeta.setId(longCount.incrementAndGet());

        // Create the ReviewBeta
        ReviewBetaDTO reviewBetaDTO = reviewBetaMapper.toDto(reviewBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewBetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reviewBetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReviewBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReviewBeta() throws Exception {
        // Initialize the database
        insertedReviewBeta = reviewBetaRepository.saveAndFlush(reviewBeta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reviewBeta
        restReviewBetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, reviewBeta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reviewBetaRepository.count();
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

    protected ReviewBeta getPersistedReviewBeta(ReviewBeta reviewBeta) {
        return reviewBetaRepository.findById(reviewBeta.getId()).orElseThrow();
    }

    protected void assertPersistedReviewBetaToMatchAllProperties(ReviewBeta expectedReviewBeta) {
        assertReviewBetaAllPropertiesEquals(expectedReviewBeta, getPersistedReviewBeta(expectedReviewBeta));
    }

    protected void assertPersistedReviewBetaToMatchUpdatableProperties(ReviewBeta expectedReviewBeta) {
        assertReviewBetaAllUpdatablePropertiesEquals(expectedReviewBeta, getPersistedReviewBeta(expectedReviewBeta));
    }
}
