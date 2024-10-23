package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextReviewAsserts.*;
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
import xyz.jhmapstruct.domain.NextProduct;
import xyz.jhmapstruct.domain.NextReview;
import xyz.jhmapstruct.repository.NextReviewRepository;
import xyz.jhmapstruct.service.NextReviewService;
import xyz.jhmapstruct.service.dto.NextReviewDTO;
import xyz.jhmapstruct.service.mapper.NextReviewMapper;

/**
 * Integration tests for the {@link NextReviewResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextReviewResourceIT {

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;
    private static final Integer SMALLER_RATING = 1 - 1;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_REVIEW_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REVIEW_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/next-reviews";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextReviewRepository nextReviewRepository;

    @Mock
    private NextReviewRepository nextReviewRepositoryMock;

    @Autowired
    private NextReviewMapper nextReviewMapper;

    @Mock
    private NextReviewService nextReviewServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextReviewMockMvc;

    private NextReview nextReview;

    private NextReview insertedNextReview;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextReview createEntity() {
        return new NextReview().rating(DEFAULT_RATING).comment(DEFAULT_COMMENT).reviewDate(DEFAULT_REVIEW_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextReview createUpdatedEntity() {
        return new NextReview().rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);
    }

    @BeforeEach
    public void initTest() {
        nextReview = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextReview != null) {
            nextReviewRepository.delete(insertedNextReview);
            insertedNextReview = null;
        }
    }

    @Test
    @Transactional
    void createNextReview() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextReview
        NextReviewDTO nextReviewDTO = nextReviewMapper.toDto(nextReview);
        var returnedNextReviewDTO = om.readValue(
            restNextReviewMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextReviewDTO.class
        );

        // Validate the NextReview in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextReview = nextReviewMapper.toEntity(returnedNextReviewDTO);
        assertNextReviewUpdatableFieldsEquals(returnedNextReview, getPersistedNextReview(returnedNextReview));

        insertedNextReview = returnedNextReview;
    }

    @Test
    @Transactional
    void createNextReviewWithExistingId() throws Exception {
        // Create the NextReview with an existing ID
        nextReview.setId(1L);
        NextReviewDTO nextReviewDTO = nextReviewMapper.toDto(nextReview);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextReviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextReview in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRatingIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextReview.setRating(null);

        // Create the NextReview, which fails.
        NextReviewDTO nextReviewDTO = nextReviewMapper.toDto(nextReview);

        restNextReviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReviewDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextReview.setReviewDate(null);

        // Create the NextReview, which fails.
        NextReviewDTO nextReviewDTO = nextReviewMapper.toDto(nextReview);

        restNextReviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextReviews() throws Exception {
        // Initialize the database
        insertedNextReview = nextReviewRepository.saveAndFlush(nextReview);

        // Get all the nextReviewList
        restNextReviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextReview.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextReviewsWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextReviewServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextReviewMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextReviewServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextReviewsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextReviewServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextReviewMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextReviewRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextReview() throws Exception {
        // Initialize the database
        insertedNextReview = nextReviewRepository.saveAndFlush(nextReview);

        // Get the nextReview
        restNextReviewMockMvc
            .perform(get(ENTITY_API_URL_ID, nextReview.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextReview.getId().intValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.reviewDate").value(DEFAULT_REVIEW_DATE.toString()));
    }

    @Test
    @Transactional
    void getNextReviewsByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextReview = nextReviewRepository.saveAndFlush(nextReview);

        Long id = nextReview.getId();

        defaultNextReviewFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextReviewFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextReviewFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextReviewsByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReview = nextReviewRepository.saveAndFlush(nextReview);

        // Get all the nextReviewList where rating equals to
        defaultNextReviewFiltering("rating.equals=" + DEFAULT_RATING, "rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewsByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextReview = nextReviewRepository.saveAndFlush(nextReview);

        // Get all the nextReviewList where rating in
        defaultNextReviewFiltering("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING, "rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewsByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextReview = nextReviewRepository.saveAndFlush(nextReview);

        // Get all the nextReviewList where rating is not null
        defaultNextReviewFiltering("rating.specified=true", "rating.specified=false");
    }

    @Test
    @Transactional
    void getAllNextReviewsByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReview = nextReviewRepository.saveAndFlush(nextReview);

        // Get all the nextReviewList where rating is greater than or equal to
        defaultNextReviewFiltering("rating.greaterThanOrEqual=" + DEFAULT_RATING, "rating.greaterThanOrEqual=" + (DEFAULT_RATING + 1));
    }

    @Test
    @Transactional
    void getAllNextReviewsByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReview = nextReviewRepository.saveAndFlush(nextReview);

        // Get all the nextReviewList where rating is less than or equal to
        defaultNextReviewFiltering("rating.lessThanOrEqual=" + DEFAULT_RATING, "rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewsByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextReview = nextReviewRepository.saveAndFlush(nextReview);

        // Get all the nextReviewList where rating is less than
        defaultNextReviewFiltering("rating.lessThan=" + (DEFAULT_RATING + 1), "rating.lessThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewsByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextReview = nextReviewRepository.saveAndFlush(nextReview);

        // Get all the nextReviewList where rating is greater than
        defaultNextReviewFiltering("rating.greaterThan=" + SMALLER_RATING, "rating.greaterThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewsByReviewDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReview = nextReviewRepository.saveAndFlush(nextReview);

        // Get all the nextReviewList where reviewDate equals to
        defaultNextReviewFiltering("reviewDate.equals=" + DEFAULT_REVIEW_DATE, "reviewDate.equals=" + UPDATED_REVIEW_DATE);
    }

    @Test
    @Transactional
    void getAllNextReviewsByReviewDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextReview = nextReviewRepository.saveAndFlush(nextReview);

        // Get all the nextReviewList where reviewDate in
        defaultNextReviewFiltering(
            "reviewDate.in=" + DEFAULT_REVIEW_DATE + "," + UPDATED_REVIEW_DATE,
            "reviewDate.in=" + UPDATED_REVIEW_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextReviewsByReviewDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextReview = nextReviewRepository.saveAndFlush(nextReview);

        // Get all the nextReviewList where reviewDate is not null
        defaultNextReviewFiltering("reviewDate.specified=true", "reviewDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextReviewsByProductIsEqualToSomething() throws Exception {
        NextProduct product;
        if (TestUtil.findAll(em, NextProduct.class).isEmpty()) {
            nextReviewRepository.saveAndFlush(nextReview);
            product = NextProductResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, NextProduct.class).get(0);
        }
        em.persist(product);
        em.flush();
        nextReview.setProduct(product);
        nextReviewRepository.saveAndFlush(nextReview);
        Long productId = product.getId();
        // Get all the nextReviewList where product equals to productId
        defaultNextReviewShouldBeFound("productId.equals=" + productId);

        // Get all the nextReviewList where product equals to (productId + 1)
        defaultNextReviewShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllNextReviewsByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextReviewRepository.saveAndFlush(nextReview);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextReview.setTenant(tenant);
        nextReviewRepository.saveAndFlush(nextReview);
        Long tenantId = tenant.getId();
        // Get all the nextReviewList where tenant equals to tenantId
        defaultNextReviewShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextReviewList where tenant equals to (tenantId + 1)
        defaultNextReviewShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextReviewFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextReviewShouldBeFound(shouldBeFound);
        defaultNextReviewShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextReviewShouldBeFound(String filter) throws Exception {
        restNextReviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextReview.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));

        // Check, that the count call also returns 1
        restNextReviewMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextReviewShouldNotBeFound(String filter) throws Exception {
        restNextReviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextReviewMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextReview() throws Exception {
        // Get the nextReview
        restNextReviewMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextReview() throws Exception {
        // Initialize the database
        insertedNextReview = nextReviewRepository.saveAndFlush(nextReview);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextReview
        NextReview updatedNextReview = nextReviewRepository.findById(nextReview.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextReview are not directly saved in db
        em.detach(updatedNextReview);
        updatedNextReview.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);
        NextReviewDTO nextReviewDTO = nextReviewMapper.toDto(updatedNextReview);

        restNextReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextReviewDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextReviewDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextReview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextReviewToMatchAllProperties(updatedNextReview);
    }

    @Test
    @Transactional
    void putNonExistingNextReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReview.setId(longCount.incrementAndGet());

        // Create the NextReview
        NextReviewDTO nextReviewDTO = nextReviewMapper.toDto(nextReview);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextReviewDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextReviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReview.setId(longCount.incrementAndGet());

        // Create the NextReview
        NextReviewDTO nextReviewDTO = nextReviewMapper.toDto(nextReview);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextReviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReview.setId(longCount.incrementAndGet());

        // Create the NextReview
        NextReviewDTO nextReviewDTO = nextReviewMapper.toDto(nextReview);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextReview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextReviewWithPatch() throws Exception {
        // Initialize the database
        insertedNextReview = nextReviewRepository.saveAndFlush(nextReview);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextReview using partial update
        NextReview partialUpdatedNextReview = new NextReview();
        partialUpdatedNextReview.setId(nextReview.getId());

        partialUpdatedNextReview.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);

        restNextReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextReview.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextReview))
            )
            .andExpect(status().isOk());

        // Validate the NextReview in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextReviewUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextReview, nextReview),
            getPersistedNextReview(nextReview)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextReviewWithPatch() throws Exception {
        // Initialize the database
        insertedNextReview = nextReviewRepository.saveAndFlush(nextReview);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextReview using partial update
        NextReview partialUpdatedNextReview = new NextReview();
        partialUpdatedNextReview.setId(nextReview.getId());

        partialUpdatedNextReview.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);

        restNextReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextReview.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextReview))
            )
            .andExpect(status().isOk());

        // Validate the NextReview in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextReviewUpdatableFieldsEquals(partialUpdatedNextReview, getPersistedNextReview(partialUpdatedNextReview));
    }

    @Test
    @Transactional
    void patchNonExistingNextReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReview.setId(longCount.incrementAndGet());

        // Create the NextReview
        NextReviewDTO nextReviewDTO = nextReviewMapper.toDto(nextReview);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextReviewDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextReviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReview.setId(longCount.incrementAndGet());

        // Create the NextReview
        NextReviewDTO nextReviewDTO = nextReviewMapper.toDto(nextReview);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextReviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReview.setId(longCount.incrementAndGet());

        // Create the NextReview
        NextReviewDTO nextReviewDTO = nextReviewMapper.toDto(nextReview);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextReviewDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextReview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextReview() throws Exception {
        // Initialize the database
        insertedNextReview = nextReviewRepository.saveAndFlush(nextReview);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextReview
        restNextReviewMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextReview.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextReviewRepository.count();
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

    protected NextReview getPersistedNextReview(NextReview nextReview) {
        return nextReviewRepository.findById(nextReview.getId()).orElseThrow();
    }

    protected void assertPersistedNextReviewToMatchAllProperties(NextReview expectedNextReview) {
        assertNextReviewAllPropertiesEquals(expectedNextReview, getPersistedNextReview(expectedNextReview));
    }

    protected void assertPersistedNextReviewToMatchUpdatableProperties(NextReview expectedNextReview) {
        assertNextReviewAllUpdatablePropertiesEquals(expectedNextReview, getPersistedNextReview(expectedNextReview));
    }
}
