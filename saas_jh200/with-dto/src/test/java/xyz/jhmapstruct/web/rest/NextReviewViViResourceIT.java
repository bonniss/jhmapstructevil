package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextReviewViViAsserts.*;
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
import xyz.jhmapstruct.domain.NextProductViVi;
import xyz.jhmapstruct.domain.NextReviewViVi;
import xyz.jhmapstruct.repository.NextReviewViViRepository;
import xyz.jhmapstruct.service.NextReviewViViService;
import xyz.jhmapstruct.service.dto.NextReviewViViDTO;
import xyz.jhmapstruct.service.mapper.NextReviewViViMapper;

/**
 * Integration tests for the {@link NextReviewViViResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextReviewViViResourceIT {

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;
    private static final Integer SMALLER_RATING = 1 - 1;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_REVIEW_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REVIEW_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/next-review-vi-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextReviewViViRepository nextReviewViViRepository;

    @Mock
    private NextReviewViViRepository nextReviewViViRepositoryMock;

    @Autowired
    private NextReviewViViMapper nextReviewViViMapper;

    @Mock
    private NextReviewViViService nextReviewViViServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextReviewViViMockMvc;

    private NextReviewViVi nextReviewViVi;

    private NextReviewViVi insertedNextReviewViVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextReviewViVi createEntity() {
        return new NextReviewViVi().rating(DEFAULT_RATING).comment(DEFAULT_COMMENT).reviewDate(DEFAULT_REVIEW_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextReviewViVi createUpdatedEntity() {
        return new NextReviewViVi().rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);
    }

    @BeforeEach
    public void initTest() {
        nextReviewViVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextReviewViVi != null) {
            nextReviewViViRepository.delete(insertedNextReviewViVi);
            insertedNextReviewViVi = null;
        }
    }

    @Test
    @Transactional
    void createNextReviewViVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextReviewViVi
        NextReviewViViDTO nextReviewViViDTO = nextReviewViViMapper.toDto(nextReviewViVi);
        var returnedNextReviewViViDTO = om.readValue(
            restNextReviewViViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewViViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextReviewViViDTO.class
        );

        // Validate the NextReviewViVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextReviewViVi = nextReviewViViMapper.toEntity(returnedNextReviewViViDTO);
        assertNextReviewViViUpdatableFieldsEquals(returnedNextReviewViVi, getPersistedNextReviewViVi(returnedNextReviewViVi));

        insertedNextReviewViVi = returnedNextReviewViVi;
    }

    @Test
    @Transactional
    void createNextReviewViViWithExistingId() throws Exception {
        // Create the NextReviewViVi with an existing ID
        nextReviewViVi.setId(1L);
        NextReviewViViDTO nextReviewViViDTO = nextReviewViViMapper.toDto(nextReviewViVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextReviewViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewViViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextReviewViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRatingIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextReviewViVi.setRating(null);

        // Create the NextReviewViVi, which fails.
        NextReviewViViDTO nextReviewViViDTO = nextReviewViViMapper.toDto(nextReviewViVi);

        restNextReviewViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewViViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReviewDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextReviewViVi.setReviewDate(null);

        // Create the NextReviewViVi, which fails.
        NextReviewViViDTO nextReviewViViDTO = nextReviewViViMapper.toDto(nextReviewViVi);

        restNextReviewViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewViViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextReviewViVis() throws Exception {
        // Initialize the database
        insertedNextReviewViVi = nextReviewViViRepository.saveAndFlush(nextReviewViVi);

        // Get all the nextReviewViViList
        restNextReviewViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextReviewViVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextReviewViVisWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextReviewViViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextReviewViViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextReviewViViServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextReviewViVisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextReviewViViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextReviewViViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextReviewViViRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextReviewViVi() throws Exception {
        // Initialize the database
        insertedNextReviewViVi = nextReviewViViRepository.saveAndFlush(nextReviewViVi);

        // Get the nextReviewViVi
        restNextReviewViViMockMvc
            .perform(get(ENTITY_API_URL_ID, nextReviewViVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextReviewViVi.getId().intValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.reviewDate").value(DEFAULT_REVIEW_DATE.toString()));
    }

    @Test
    @Transactional
    void getNextReviewViVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextReviewViVi = nextReviewViViRepository.saveAndFlush(nextReviewViVi);

        Long id = nextReviewViVi.getId();

        defaultNextReviewViViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextReviewViViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextReviewViViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextReviewViVisByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReviewViVi = nextReviewViViRepository.saveAndFlush(nextReviewViVi);

        // Get all the nextReviewViViList where rating equals to
        defaultNextReviewViViFiltering("rating.equals=" + DEFAULT_RATING, "rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewViVisByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextReviewViVi = nextReviewViViRepository.saveAndFlush(nextReviewViVi);

        // Get all the nextReviewViViList where rating in
        defaultNextReviewViViFiltering("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING, "rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewViVisByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextReviewViVi = nextReviewViViRepository.saveAndFlush(nextReviewViVi);

        // Get all the nextReviewViViList where rating is not null
        defaultNextReviewViViFiltering("rating.specified=true", "rating.specified=false");
    }

    @Test
    @Transactional
    void getAllNextReviewViVisByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReviewViVi = nextReviewViViRepository.saveAndFlush(nextReviewViVi);

        // Get all the nextReviewViViList where rating is greater than or equal to
        defaultNextReviewViViFiltering("rating.greaterThanOrEqual=" + DEFAULT_RATING, "rating.greaterThanOrEqual=" + (DEFAULT_RATING + 1));
    }

    @Test
    @Transactional
    void getAllNextReviewViVisByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReviewViVi = nextReviewViViRepository.saveAndFlush(nextReviewViVi);

        // Get all the nextReviewViViList where rating is less than or equal to
        defaultNextReviewViViFiltering("rating.lessThanOrEqual=" + DEFAULT_RATING, "rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewViVisByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextReviewViVi = nextReviewViViRepository.saveAndFlush(nextReviewViVi);

        // Get all the nextReviewViViList where rating is less than
        defaultNextReviewViViFiltering("rating.lessThan=" + (DEFAULT_RATING + 1), "rating.lessThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewViVisByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextReviewViVi = nextReviewViViRepository.saveAndFlush(nextReviewViVi);

        // Get all the nextReviewViViList where rating is greater than
        defaultNextReviewViViFiltering("rating.greaterThan=" + SMALLER_RATING, "rating.greaterThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewViVisByReviewDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReviewViVi = nextReviewViViRepository.saveAndFlush(nextReviewViVi);

        // Get all the nextReviewViViList where reviewDate equals to
        defaultNextReviewViViFiltering("reviewDate.equals=" + DEFAULT_REVIEW_DATE, "reviewDate.equals=" + UPDATED_REVIEW_DATE);
    }

    @Test
    @Transactional
    void getAllNextReviewViVisByReviewDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextReviewViVi = nextReviewViViRepository.saveAndFlush(nextReviewViVi);

        // Get all the nextReviewViViList where reviewDate in
        defaultNextReviewViViFiltering(
            "reviewDate.in=" + DEFAULT_REVIEW_DATE + "," + UPDATED_REVIEW_DATE,
            "reviewDate.in=" + UPDATED_REVIEW_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextReviewViVisByReviewDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextReviewViVi = nextReviewViViRepository.saveAndFlush(nextReviewViVi);

        // Get all the nextReviewViViList where reviewDate is not null
        defaultNextReviewViViFiltering("reviewDate.specified=true", "reviewDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextReviewViVisByProductIsEqualToSomething() throws Exception {
        NextProductViVi product;
        if (TestUtil.findAll(em, NextProductViVi.class).isEmpty()) {
            nextReviewViViRepository.saveAndFlush(nextReviewViVi);
            product = NextProductViViResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, NextProductViVi.class).get(0);
        }
        em.persist(product);
        em.flush();
        nextReviewViVi.setProduct(product);
        nextReviewViViRepository.saveAndFlush(nextReviewViVi);
        Long productId = product.getId();
        // Get all the nextReviewViViList where product equals to productId
        defaultNextReviewViViShouldBeFound("productId.equals=" + productId);

        // Get all the nextReviewViViList where product equals to (productId + 1)
        defaultNextReviewViViShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllNextReviewViVisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextReviewViViRepository.saveAndFlush(nextReviewViVi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextReviewViVi.setTenant(tenant);
        nextReviewViViRepository.saveAndFlush(nextReviewViVi);
        Long tenantId = tenant.getId();
        // Get all the nextReviewViViList where tenant equals to tenantId
        defaultNextReviewViViShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextReviewViViList where tenant equals to (tenantId + 1)
        defaultNextReviewViViShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextReviewViViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextReviewViViShouldBeFound(shouldBeFound);
        defaultNextReviewViViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextReviewViViShouldBeFound(String filter) throws Exception {
        restNextReviewViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextReviewViVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));

        // Check, that the count call also returns 1
        restNextReviewViViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextReviewViViShouldNotBeFound(String filter) throws Exception {
        restNextReviewViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextReviewViViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextReviewViVi() throws Exception {
        // Get the nextReviewViVi
        restNextReviewViViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextReviewViVi() throws Exception {
        // Initialize the database
        insertedNextReviewViVi = nextReviewViViRepository.saveAndFlush(nextReviewViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextReviewViVi
        NextReviewViVi updatedNextReviewViVi = nextReviewViViRepository.findById(nextReviewViVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextReviewViVi are not directly saved in db
        em.detach(updatedNextReviewViVi);
        updatedNextReviewViVi.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);
        NextReviewViViDTO nextReviewViViDTO = nextReviewViViMapper.toDto(updatedNextReviewViVi);

        restNextReviewViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextReviewViViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextReviewViViDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextReviewViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextReviewViViToMatchAllProperties(updatedNextReviewViVi);
    }

    @Test
    @Transactional
    void putNonExistingNextReviewViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewViVi.setId(longCount.incrementAndGet());

        // Create the NextReviewViVi
        NextReviewViViDTO nextReviewViViDTO = nextReviewViViMapper.toDto(nextReviewViVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextReviewViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextReviewViViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextReviewViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReviewViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextReviewViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewViVi.setId(longCount.incrementAndGet());

        // Create the NextReviewViVi
        NextReviewViViDTO nextReviewViViDTO = nextReviewViViMapper.toDto(nextReviewViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextReviewViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReviewViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextReviewViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewViVi.setId(longCount.incrementAndGet());

        // Create the NextReviewViVi
        NextReviewViViDTO nextReviewViViDTO = nextReviewViViMapper.toDto(nextReviewViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewViViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewViViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextReviewViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextReviewViViWithPatch() throws Exception {
        // Initialize the database
        insertedNextReviewViVi = nextReviewViViRepository.saveAndFlush(nextReviewViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextReviewViVi using partial update
        NextReviewViVi partialUpdatedNextReviewViVi = new NextReviewViVi();
        partialUpdatedNextReviewViVi.setId(nextReviewViVi.getId());

        partialUpdatedNextReviewViVi.comment(UPDATED_COMMENT);

        restNextReviewViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextReviewViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextReviewViVi))
            )
            .andExpect(status().isOk());

        // Validate the NextReviewViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextReviewViViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextReviewViVi, nextReviewViVi),
            getPersistedNextReviewViVi(nextReviewViVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextReviewViViWithPatch() throws Exception {
        // Initialize the database
        insertedNextReviewViVi = nextReviewViViRepository.saveAndFlush(nextReviewViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextReviewViVi using partial update
        NextReviewViVi partialUpdatedNextReviewViVi = new NextReviewViVi();
        partialUpdatedNextReviewViVi.setId(nextReviewViVi.getId());

        partialUpdatedNextReviewViVi.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);

        restNextReviewViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextReviewViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextReviewViVi))
            )
            .andExpect(status().isOk());

        // Validate the NextReviewViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextReviewViViUpdatableFieldsEquals(partialUpdatedNextReviewViVi, getPersistedNextReviewViVi(partialUpdatedNextReviewViVi));
    }

    @Test
    @Transactional
    void patchNonExistingNextReviewViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewViVi.setId(longCount.incrementAndGet());

        // Create the NextReviewViVi
        NextReviewViViDTO nextReviewViViDTO = nextReviewViViMapper.toDto(nextReviewViVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextReviewViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextReviewViViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextReviewViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReviewViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextReviewViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewViVi.setId(longCount.incrementAndGet());

        // Create the NextReviewViVi
        NextReviewViViDTO nextReviewViViDTO = nextReviewViViMapper.toDto(nextReviewViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextReviewViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReviewViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextReviewViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewViVi.setId(longCount.incrementAndGet());

        // Create the NextReviewViVi
        NextReviewViViDTO nextReviewViViDTO = nextReviewViViMapper.toDto(nextReviewViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewViViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextReviewViViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextReviewViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextReviewViVi() throws Exception {
        // Initialize the database
        insertedNextReviewViVi = nextReviewViViRepository.saveAndFlush(nextReviewViVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextReviewViVi
        restNextReviewViViMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextReviewViVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextReviewViViRepository.count();
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

    protected NextReviewViVi getPersistedNextReviewViVi(NextReviewViVi nextReviewViVi) {
        return nextReviewViViRepository.findById(nextReviewViVi.getId()).orElseThrow();
    }

    protected void assertPersistedNextReviewViViToMatchAllProperties(NextReviewViVi expectedNextReviewViVi) {
        assertNextReviewViViAllPropertiesEquals(expectedNextReviewViVi, getPersistedNextReviewViVi(expectedNextReviewViVi));
    }

    protected void assertPersistedNextReviewViViToMatchUpdatableProperties(NextReviewViVi expectedNextReviewViVi) {
        assertNextReviewViViAllUpdatablePropertiesEquals(expectedNextReviewViVi, getPersistedNextReviewViVi(expectedNextReviewViVi));
    }
}
