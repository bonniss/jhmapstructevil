package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextReviewAlphaAsserts.*;
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
import xyz.jhmapstruct.domain.NextProductAlpha;
import xyz.jhmapstruct.domain.NextReviewAlpha;
import xyz.jhmapstruct.repository.NextReviewAlphaRepository;
import xyz.jhmapstruct.service.NextReviewAlphaService;

/**
 * Integration tests for the {@link NextReviewAlphaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextReviewAlphaResourceIT {

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;
    private static final Integer SMALLER_RATING = 1 - 1;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_REVIEW_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REVIEW_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/next-review-alphas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextReviewAlphaRepository nextReviewAlphaRepository;

    @Mock
    private NextReviewAlphaRepository nextReviewAlphaRepositoryMock;

    @Mock
    private NextReviewAlphaService nextReviewAlphaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextReviewAlphaMockMvc;

    private NextReviewAlpha nextReviewAlpha;

    private NextReviewAlpha insertedNextReviewAlpha;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextReviewAlpha createEntity() {
        return new NextReviewAlpha().rating(DEFAULT_RATING).comment(DEFAULT_COMMENT).reviewDate(DEFAULT_REVIEW_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextReviewAlpha createUpdatedEntity() {
        return new NextReviewAlpha().rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);
    }

    @BeforeEach
    public void initTest() {
        nextReviewAlpha = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextReviewAlpha != null) {
            nextReviewAlphaRepository.delete(insertedNextReviewAlpha);
            insertedNextReviewAlpha = null;
        }
    }

    @Test
    @Transactional
    void createNextReviewAlpha() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextReviewAlpha
        var returnedNextReviewAlpha = om.readValue(
            restNextReviewAlphaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewAlpha)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextReviewAlpha.class
        );

        // Validate the NextReviewAlpha in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextReviewAlphaUpdatableFieldsEquals(returnedNextReviewAlpha, getPersistedNextReviewAlpha(returnedNextReviewAlpha));

        insertedNextReviewAlpha = returnedNextReviewAlpha;
    }

    @Test
    @Transactional
    void createNextReviewAlphaWithExistingId() throws Exception {
        // Create the NextReviewAlpha with an existing ID
        nextReviewAlpha.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextReviewAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewAlpha)))
            .andExpect(status().isBadRequest());

        // Validate the NextReviewAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRatingIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextReviewAlpha.setRating(null);

        // Create the NextReviewAlpha, which fails.

        restNextReviewAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewAlpha)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReviewDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextReviewAlpha.setReviewDate(null);

        // Create the NextReviewAlpha, which fails.

        restNextReviewAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewAlpha)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextReviewAlphas() throws Exception {
        // Initialize the database
        insertedNextReviewAlpha = nextReviewAlphaRepository.saveAndFlush(nextReviewAlpha);

        // Get all the nextReviewAlphaList
        restNextReviewAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextReviewAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextReviewAlphasWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextReviewAlphaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextReviewAlphaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextReviewAlphaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextReviewAlphasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextReviewAlphaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextReviewAlphaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextReviewAlphaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextReviewAlpha() throws Exception {
        // Initialize the database
        insertedNextReviewAlpha = nextReviewAlphaRepository.saveAndFlush(nextReviewAlpha);

        // Get the nextReviewAlpha
        restNextReviewAlphaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextReviewAlpha.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextReviewAlpha.getId().intValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.reviewDate").value(DEFAULT_REVIEW_DATE.toString()));
    }

    @Test
    @Transactional
    void getNextReviewAlphasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextReviewAlpha = nextReviewAlphaRepository.saveAndFlush(nextReviewAlpha);

        Long id = nextReviewAlpha.getId();

        defaultNextReviewAlphaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextReviewAlphaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextReviewAlphaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextReviewAlphasByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReviewAlpha = nextReviewAlphaRepository.saveAndFlush(nextReviewAlpha);

        // Get all the nextReviewAlphaList where rating equals to
        defaultNextReviewAlphaFiltering("rating.equals=" + DEFAULT_RATING, "rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewAlphasByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextReviewAlpha = nextReviewAlphaRepository.saveAndFlush(nextReviewAlpha);

        // Get all the nextReviewAlphaList where rating in
        defaultNextReviewAlphaFiltering("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING, "rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewAlphasByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextReviewAlpha = nextReviewAlphaRepository.saveAndFlush(nextReviewAlpha);

        // Get all the nextReviewAlphaList where rating is not null
        defaultNextReviewAlphaFiltering("rating.specified=true", "rating.specified=false");
    }

    @Test
    @Transactional
    void getAllNextReviewAlphasByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReviewAlpha = nextReviewAlphaRepository.saveAndFlush(nextReviewAlpha);

        // Get all the nextReviewAlphaList where rating is greater than or equal to
        defaultNextReviewAlphaFiltering("rating.greaterThanOrEqual=" + DEFAULT_RATING, "rating.greaterThanOrEqual=" + (DEFAULT_RATING + 1));
    }

    @Test
    @Transactional
    void getAllNextReviewAlphasByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReviewAlpha = nextReviewAlphaRepository.saveAndFlush(nextReviewAlpha);

        // Get all the nextReviewAlphaList where rating is less than or equal to
        defaultNextReviewAlphaFiltering("rating.lessThanOrEqual=" + DEFAULT_RATING, "rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewAlphasByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextReviewAlpha = nextReviewAlphaRepository.saveAndFlush(nextReviewAlpha);

        // Get all the nextReviewAlphaList where rating is less than
        defaultNextReviewAlphaFiltering("rating.lessThan=" + (DEFAULT_RATING + 1), "rating.lessThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewAlphasByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextReviewAlpha = nextReviewAlphaRepository.saveAndFlush(nextReviewAlpha);

        // Get all the nextReviewAlphaList where rating is greater than
        defaultNextReviewAlphaFiltering("rating.greaterThan=" + SMALLER_RATING, "rating.greaterThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewAlphasByReviewDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReviewAlpha = nextReviewAlphaRepository.saveAndFlush(nextReviewAlpha);

        // Get all the nextReviewAlphaList where reviewDate equals to
        defaultNextReviewAlphaFiltering("reviewDate.equals=" + DEFAULT_REVIEW_DATE, "reviewDate.equals=" + UPDATED_REVIEW_DATE);
    }

    @Test
    @Transactional
    void getAllNextReviewAlphasByReviewDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextReviewAlpha = nextReviewAlphaRepository.saveAndFlush(nextReviewAlpha);

        // Get all the nextReviewAlphaList where reviewDate in
        defaultNextReviewAlphaFiltering(
            "reviewDate.in=" + DEFAULT_REVIEW_DATE + "," + UPDATED_REVIEW_DATE,
            "reviewDate.in=" + UPDATED_REVIEW_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextReviewAlphasByReviewDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextReviewAlpha = nextReviewAlphaRepository.saveAndFlush(nextReviewAlpha);

        // Get all the nextReviewAlphaList where reviewDate is not null
        defaultNextReviewAlphaFiltering("reviewDate.specified=true", "reviewDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextReviewAlphasByProductIsEqualToSomething() throws Exception {
        NextProductAlpha product;
        if (TestUtil.findAll(em, NextProductAlpha.class).isEmpty()) {
            nextReviewAlphaRepository.saveAndFlush(nextReviewAlpha);
            product = NextProductAlphaResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, NextProductAlpha.class).get(0);
        }
        em.persist(product);
        em.flush();
        nextReviewAlpha.setProduct(product);
        nextReviewAlphaRepository.saveAndFlush(nextReviewAlpha);
        Long productId = product.getId();
        // Get all the nextReviewAlphaList where product equals to productId
        defaultNextReviewAlphaShouldBeFound("productId.equals=" + productId);

        // Get all the nextReviewAlphaList where product equals to (productId + 1)
        defaultNextReviewAlphaShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllNextReviewAlphasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextReviewAlphaRepository.saveAndFlush(nextReviewAlpha);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextReviewAlpha.setTenant(tenant);
        nextReviewAlphaRepository.saveAndFlush(nextReviewAlpha);
        Long tenantId = tenant.getId();
        // Get all the nextReviewAlphaList where tenant equals to tenantId
        defaultNextReviewAlphaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextReviewAlphaList where tenant equals to (tenantId + 1)
        defaultNextReviewAlphaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextReviewAlphaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextReviewAlphaShouldBeFound(shouldBeFound);
        defaultNextReviewAlphaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextReviewAlphaShouldBeFound(String filter) throws Exception {
        restNextReviewAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextReviewAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));

        // Check, that the count call also returns 1
        restNextReviewAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextReviewAlphaShouldNotBeFound(String filter) throws Exception {
        restNextReviewAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextReviewAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextReviewAlpha() throws Exception {
        // Get the nextReviewAlpha
        restNextReviewAlphaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextReviewAlpha() throws Exception {
        // Initialize the database
        insertedNextReviewAlpha = nextReviewAlphaRepository.saveAndFlush(nextReviewAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextReviewAlpha
        NextReviewAlpha updatedNextReviewAlpha = nextReviewAlphaRepository.findById(nextReviewAlpha.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextReviewAlpha are not directly saved in db
        em.detach(updatedNextReviewAlpha);
        updatedNextReviewAlpha.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);

        restNextReviewAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextReviewAlpha.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextReviewAlpha))
            )
            .andExpect(status().isOk());

        // Validate the NextReviewAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextReviewAlphaToMatchAllProperties(updatedNextReviewAlpha);
    }

    @Test
    @Transactional
    void putNonExistingNextReviewAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewAlpha.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextReviewAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextReviewAlpha.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextReviewAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReviewAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextReviewAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextReviewAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReviewAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextReviewAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewAlphaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewAlpha)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextReviewAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextReviewAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedNextReviewAlpha = nextReviewAlphaRepository.saveAndFlush(nextReviewAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextReviewAlpha using partial update
        NextReviewAlpha partialUpdatedNextReviewAlpha = new NextReviewAlpha();
        partialUpdatedNextReviewAlpha.setId(nextReviewAlpha.getId());

        partialUpdatedNextReviewAlpha.comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);

        restNextReviewAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextReviewAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextReviewAlpha))
            )
            .andExpect(status().isOk());

        // Validate the NextReviewAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextReviewAlphaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextReviewAlpha, nextReviewAlpha),
            getPersistedNextReviewAlpha(nextReviewAlpha)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextReviewAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedNextReviewAlpha = nextReviewAlphaRepository.saveAndFlush(nextReviewAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextReviewAlpha using partial update
        NextReviewAlpha partialUpdatedNextReviewAlpha = new NextReviewAlpha();
        partialUpdatedNextReviewAlpha.setId(nextReviewAlpha.getId());

        partialUpdatedNextReviewAlpha.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);

        restNextReviewAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextReviewAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextReviewAlpha))
            )
            .andExpect(status().isOk());

        // Validate the NextReviewAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextReviewAlphaUpdatableFieldsEquals(
            partialUpdatedNextReviewAlpha,
            getPersistedNextReviewAlpha(partialUpdatedNextReviewAlpha)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextReviewAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewAlpha.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextReviewAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextReviewAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextReviewAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReviewAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextReviewAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextReviewAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReviewAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextReviewAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewAlphaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextReviewAlpha)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextReviewAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextReviewAlpha() throws Exception {
        // Initialize the database
        insertedNextReviewAlpha = nextReviewAlphaRepository.saveAndFlush(nextReviewAlpha);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextReviewAlpha
        restNextReviewAlphaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextReviewAlpha.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextReviewAlphaRepository.count();
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

    protected NextReviewAlpha getPersistedNextReviewAlpha(NextReviewAlpha nextReviewAlpha) {
        return nextReviewAlphaRepository.findById(nextReviewAlpha.getId()).orElseThrow();
    }

    protected void assertPersistedNextReviewAlphaToMatchAllProperties(NextReviewAlpha expectedNextReviewAlpha) {
        assertNextReviewAlphaAllPropertiesEquals(expectedNextReviewAlpha, getPersistedNextReviewAlpha(expectedNextReviewAlpha));
    }

    protected void assertPersistedNextReviewAlphaToMatchUpdatableProperties(NextReviewAlpha expectedNextReviewAlpha) {
        assertNextReviewAlphaAllUpdatablePropertiesEquals(expectedNextReviewAlpha, getPersistedNextReviewAlpha(expectedNextReviewAlpha));
    }
}
