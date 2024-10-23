package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextReviewGammaAsserts.*;
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
import xyz.jhmapstruct.domain.NextProductGamma;
import xyz.jhmapstruct.domain.NextReviewGamma;
import xyz.jhmapstruct.repository.NextReviewGammaRepository;
import xyz.jhmapstruct.service.NextReviewGammaService;

/**
 * Integration tests for the {@link NextReviewGammaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextReviewGammaResourceIT {

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;
    private static final Integer SMALLER_RATING = 1 - 1;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_REVIEW_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REVIEW_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/next-review-gammas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextReviewGammaRepository nextReviewGammaRepository;

    @Mock
    private NextReviewGammaRepository nextReviewGammaRepositoryMock;

    @Mock
    private NextReviewGammaService nextReviewGammaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextReviewGammaMockMvc;

    private NextReviewGamma nextReviewGamma;

    private NextReviewGamma insertedNextReviewGamma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextReviewGamma createEntity() {
        return new NextReviewGamma().rating(DEFAULT_RATING).comment(DEFAULT_COMMENT).reviewDate(DEFAULT_REVIEW_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextReviewGamma createUpdatedEntity() {
        return new NextReviewGamma().rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);
    }

    @BeforeEach
    public void initTest() {
        nextReviewGamma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextReviewGamma != null) {
            nextReviewGammaRepository.delete(insertedNextReviewGamma);
            insertedNextReviewGamma = null;
        }
    }

    @Test
    @Transactional
    void createNextReviewGamma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextReviewGamma
        var returnedNextReviewGamma = om.readValue(
            restNextReviewGammaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewGamma)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextReviewGamma.class
        );

        // Validate the NextReviewGamma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextReviewGammaUpdatableFieldsEquals(returnedNextReviewGamma, getPersistedNextReviewGamma(returnedNextReviewGamma));

        insertedNextReviewGamma = returnedNextReviewGamma;
    }

    @Test
    @Transactional
    void createNextReviewGammaWithExistingId() throws Exception {
        // Create the NextReviewGamma with an existing ID
        nextReviewGamma.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextReviewGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewGamma)))
            .andExpect(status().isBadRequest());

        // Validate the NextReviewGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRatingIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextReviewGamma.setRating(null);

        // Create the NextReviewGamma, which fails.

        restNextReviewGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewGamma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReviewDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextReviewGamma.setReviewDate(null);

        // Create the NextReviewGamma, which fails.

        restNextReviewGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewGamma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextReviewGammas() throws Exception {
        // Initialize the database
        insertedNextReviewGamma = nextReviewGammaRepository.saveAndFlush(nextReviewGamma);

        // Get all the nextReviewGammaList
        restNextReviewGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextReviewGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextReviewGammasWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextReviewGammaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextReviewGammaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextReviewGammaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextReviewGammasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextReviewGammaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextReviewGammaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextReviewGammaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextReviewGamma() throws Exception {
        // Initialize the database
        insertedNextReviewGamma = nextReviewGammaRepository.saveAndFlush(nextReviewGamma);

        // Get the nextReviewGamma
        restNextReviewGammaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextReviewGamma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextReviewGamma.getId().intValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.reviewDate").value(DEFAULT_REVIEW_DATE.toString()));
    }

    @Test
    @Transactional
    void getNextReviewGammasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextReviewGamma = nextReviewGammaRepository.saveAndFlush(nextReviewGamma);

        Long id = nextReviewGamma.getId();

        defaultNextReviewGammaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextReviewGammaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextReviewGammaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextReviewGammasByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReviewGamma = nextReviewGammaRepository.saveAndFlush(nextReviewGamma);

        // Get all the nextReviewGammaList where rating equals to
        defaultNextReviewGammaFiltering("rating.equals=" + DEFAULT_RATING, "rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewGammasByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextReviewGamma = nextReviewGammaRepository.saveAndFlush(nextReviewGamma);

        // Get all the nextReviewGammaList where rating in
        defaultNextReviewGammaFiltering("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING, "rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewGammasByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextReviewGamma = nextReviewGammaRepository.saveAndFlush(nextReviewGamma);

        // Get all the nextReviewGammaList where rating is not null
        defaultNextReviewGammaFiltering("rating.specified=true", "rating.specified=false");
    }

    @Test
    @Transactional
    void getAllNextReviewGammasByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReviewGamma = nextReviewGammaRepository.saveAndFlush(nextReviewGamma);

        // Get all the nextReviewGammaList where rating is greater than or equal to
        defaultNextReviewGammaFiltering("rating.greaterThanOrEqual=" + DEFAULT_RATING, "rating.greaterThanOrEqual=" + (DEFAULT_RATING + 1));
    }

    @Test
    @Transactional
    void getAllNextReviewGammasByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReviewGamma = nextReviewGammaRepository.saveAndFlush(nextReviewGamma);

        // Get all the nextReviewGammaList where rating is less than or equal to
        defaultNextReviewGammaFiltering("rating.lessThanOrEqual=" + DEFAULT_RATING, "rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewGammasByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextReviewGamma = nextReviewGammaRepository.saveAndFlush(nextReviewGamma);

        // Get all the nextReviewGammaList where rating is less than
        defaultNextReviewGammaFiltering("rating.lessThan=" + (DEFAULT_RATING + 1), "rating.lessThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewGammasByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextReviewGamma = nextReviewGammaRepository.saveAndFlush(nextReviewGamma);

        // Get all the nextReviewGammaList where rating is greater than
        defaultNextReviewGammaFiltering("rating.greaterThan=" + SMALLER_RATING, "rating.greaterThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewGammasByReviewDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReviewGamma = nextReviewGammaRepository.saveAndFlush(nextReviewGamma);

        // Get all the nextReviewGammaList where reviewDate equals to
        defaultNextReviewGammaFiltering("reviewDate.equals=" + DEFAULT_REVIEW_DATE, "reviewDate.equals=" + UPDATED_REVIEW_DATE);
    }

    @Test
    @Transactional
    void getAllNextReviewGammasByReviewDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextReviewGamma = nextReviewGammaRepository.saveAndFlush(nextReviewGamma);

        // Get all the nextReviewGammaList where reviewDate in
        defaultNextReviewGammaFiltering(
            "reviewDate.in=" + DEFAULT_REVIEW_DATE + "," + UPDATED_REVIEW_DATE,
            "reviewDate.in=" + UPDATED_REVIEW_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextReviewGammasByReviewDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextReviewGamma = nextReviewGammaRepository.saveAndFlush(nextReviewGamma);

        // Get all the nextReviewGammaList where reviewDate is not null
        defaultNextReviewGammaFiltering("reviewDate.specified=true", "reviewDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextReviewGammasByProductIsEqualToSomething() throws Exception {
        NextProductGamma product;
        if (TestUtil.findAll(em, NextProductGamma.class).isEmpty()) {
            nextReviewGammaRepository.saveAndFlush(nextReviewGamma);
            product = NextProductGammaResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, NextProductGamma.class).get(0);
        }
        em.persist(product);
        em.flush();
        nextReviewGamma.setProduct(product);
        nextReviewGammaRepository.saveAndFlush(nextReviewGamma);
        Long productId = product.getId();
        // Get all the nextReviewGammaList where product equals to productId
        defaultNextReviewGammaShouldBeFound("productId.equals=" + productId);

        // Get all the nextReviewGammaList where product equals to (productId + 1)
        defaultNextReviewGammaShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllNextReviewGammasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextReviewGammaRepository.saveAndFlush(nextReviewGamma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextReviewGamma.setTenant(tenant);
        nextReviewGammaRepository.saveAndFlush(nextReviewGamma);
        Long tenantId = tenant.getId();
        // Get all the nextReviewGammaList where tenant equals to tenantId
        defaultNextReviewGammaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextReviewGammaList where tenant equals to (tenantId + 1)
        defaultNextReviewGammaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextReviewGammaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextReviewGammaShouldBeFound(shouldBeFound);
        defaultNextReviewGammaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextReviewGammaShouldBeFound(String filter) throws Exception {
        restNextReviewGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextReviewGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));

        // Check, that the count call also returns 1
        restNextReviewGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextReviewGammaShouldNotBeFound(String filter) throws Exception {
        restNextReviewGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextReviewGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextReviewGamma() throws Exception {
        // Get the nextReviewGamma
        restNextReviewGammaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextReviewGamma() throws Exception {
        // Initialize the database
        insertedNextReviewGamma = nextReviewGammaRepository.saveAndFlush(nextReviewGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextReviewGamma
        NextReviewGamma updatedNextReviewGamma = nextReviewGammaRepository.findById(nextReviewGamma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextReviewGamma are not directly saved in db
        em.detach(updatedNextReviewGamma);
        updatedNextReviewGamma.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);

        restNextReviewGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextReviewGamma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextReviewGamma))
            )
            .andExpect(status().isOk());

        // Validate the NextReviewGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextReviewGammaToMatchAllProperties(updatedNextReviewGamma);
    }

    @Test
    @Transactional
    void putNonExistingNextReviewGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewGamma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextReviewGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextReviewGamma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextReviewGamma))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReviewGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextReviewGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewGamma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextReviewGamma))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReviewGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextReviewGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewGamma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewGammaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewGamma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextReviewGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextReviewGammaWithPatch() throws Exception {
        // Initialize the database
        insertedNextReviewGamma = nextReviewGammaRepository.saveAndFlush(nextReviewGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextReviewGamma using partial update
        NextReviewGamma partialUpdatedNextReviewGamma = new NextReviewGamma();
        partialUpdatedNextReviewGamma.setId(nextReviewGamma.getId());

        restNextReviewGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextReviewGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextReviewGamma))
            )
            .andExpect(status().isOk());

        // Validate the NextReviewGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextReviewGammaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextReviewGamma, nextReviewGamma),
            getPersistedNextReviewGamma(nextReviewGamma)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextReviewGammaWithPatch() throws Exception {
        // Initialize the database
        insertedNextReviewGamma = nextReviewGammaRepository.saveAndFlush(nextReviewGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextReviewGamma using partial update
        NextReviewGamma partialUpdatedNextReviewGamma = new NextReviewGamma();
        partialUpdatedNextReviewGamma.setId(nextReviewGamma.getId());

        partialUpdatedNextReviewGamma.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);

        restNextReviewGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextReviewGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextReviewGamma))
            )
            .andExpect(status().isOk());

        // Validate the NextReviewGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextReviewGammaUpdatableFieldsEquals(
            partialUpdatedNextReviewGamma,
            getPersistedNextReviewGamma(partialUpdatedNextReviewGamma)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextReviewGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewGamma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextReviewGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextReviewGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextReviewGamma))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReviewGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextReviewGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewGamma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextReviewGamma))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReviewGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextReviewGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewGamma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewGammaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextReviewGamma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextReviewGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextReviewGamma() throws Exception {
        // Initialize the database
        insertedNextReviewGamma = nextReviewGammaRepository.saveAndFlush(nextReviewGamma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextReviewGamma
        restNextReviewGammaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextReviewGamma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextReviewGammaRepository.count();
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

    protected NextReviewGamma getPersistedNextReviewGamma(NextReviewGamma nextReviewGamma) {
        return nextReviewGammaRepository.findById(nextReviewGamma.getId()).orElseThrow();
    }

    protected void assertPersistedNextReviewGammaToMatchAllProperties(NextReviewGamma expectedNextReviewGamma) {
        assertNextReviewGammaAllPropertiesEquals(expectedNextReviewGamma, getPersistedNextReviewGamma(expectedNextReviewGamma));
    }

    protected void assertPersistedNextReviewGammaToMatchUpdatableProperties(NextReviewGamma expectedNextReviewGamma) {
        assertNextReviewGammaAllUpdatablePropertiesEquals(expectedNextReviewGamma, getPersistedNextReviewGamma(expectedNextReviewGamma));
    }
}
