package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextReviewViAsserts.*;
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
import xyz.jhmapstruct.domain.NextProductVi;
import xyz.jhmapstruct.domain.NextReviewVi;
import xyz.jhmapstruct.repository.NextReviewViRepository;
import xyz.jhmapstruct.service.NextReviewViService;
import xyz.jhmapstruct.service.dto.NextReviewViDTO;
import xyz.jhmapstruct.service.mapper.NextReviewViMapper;

/**
 * Integration tests for the {@link NextReviewViResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextReviewViResourceIT {

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;
    private static final Integer SMALLER_RATING = 1 - 1;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_REVIEW_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REVIEW_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/next-review-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextReviewViRepository nextReviewViRepository;

    @Mock
    private NextReviewViRepository nextReviewViRepositoryMock;

    @Autowired
    private NextReviewViMapper nextReviewViMapper;

    @Mock
    private NextReviewViService nextReviewViServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextReviewViMockMvc;

    private NextReviewVi nextReviewVi;

    private NextReviewVi insertedNextReviewVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextReviewVi createEntity() {
        return new NextReviewVi().rating(DEFAULT_RATING).comment(DEFAULT_COMMENT).reviewDate(DEFAULT_REVIEW_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextReviewVi createUpdatedEntity() {
        return new NextReviewVi().rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);
    }

    @BeforeEach
    public void initTest() {
        nextReviewVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextReviewVi != null) {
            nextReviewViRepository.delete(insertedNextReviewVi);
            insertedNextReviewVi = null;
        }
    }

    @Test
    @Transactional
    void createNextReviewVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextReviewVi
        NextReviewViDTO nextReviewViDTO = nextReviewViMapper.toDto(nextReviewVi);
        var returnedNextReviewViDTO = om.readValue(
            restNextReviewViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextReviewViDTO.class
        );

        // Validate the NextReviewVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextReviewVi = nextReviewViMapper.toEntity(returnedNextReviewViDTO);
        assertNextReviewViUpdatableFieldsEquals(returnedNextReviewVi, getPersistedNextReviewVi(returnedNextReviewVi));

        insertedNextReviewVi = returnedNextReviewVi;
    }

    @Test
    @Transactional
    void createNextReviewViWithExistingId() throws Exception {
        // Create the NextReviewVi with an existing ID
        nextReviewVi.setId(1L);
        NextReviewViDTO nextReviewViDTO = nextReviewViMapper.toDto(nextReviewVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextReviewViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextReviewVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRatingIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextReviewVi.setRating(null);

        // Create the NextReviewVi, which fails.
        NextReviewViDTO nextReviewViDTO = nextReviewViMapper.toDto(nextReviewVi);

        restNextReviewViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReviewDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextReviewVi.setReviewDate(null);

        // Create the NextReviewVi, which fails.
        NextReviewViDTO nextReviewViDTO = nextReviewViMapper.toDto(nextReviewVi);

        restNextReviewViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextReviewVis() throws Exception {
        // Initialize the database
        insertedNextReviewVi = nextReviewViRepository.saveAndFlush(nextReviewVi);

        // Get all the nextReviewViList
        restNextReviewViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextReviewVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextReviewVisWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextReviewViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextReviewViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextReviewViServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextReviewVisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextReviewViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextReviewViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextReviewViRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextReviewVi() throws Exception {
        // Initialize the database
        insertedNextReviewVi = nextReviewViRepository.saveAndFlush(nextReviewVi);

        // Get the nextReviewVi
        restNextReviewViMockMvc
            .perform(get(ENTITY_API_URL_ID, nextReviewVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextReviewVi.getId().intValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.reviewDate").value(DEFAULT_REVIEW_DATE.toString()));
    }

    @Test
    @Transactional
    void getNextReviewVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextReviewVi = nextReviewViRepository.saveAndFlush(nextReviewVi);

        Long id = nextReviewVi.getId();

        defaultNextReviewViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextReviewViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextReviewViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextReviewVisByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReviewVi = nextReviewViRepository.saveAndFlush(nextReviewVi);

        // Get all the nextReviewViList where rating equals to
        defaultNextReviewViFiltering("rating.equals=" + DEFAULT_RATING, "rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewVisByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextReviewVi = nextReviewViRepository.saveAndFlush(nextReviewVi);

        // Get all the nextReviewViList where rating in
        defaultNextReviewViFiltering("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING, "rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewVisByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextReviewVi = nextReviewViRepository.saveAndFlush(nextReviewVi);

        // Get all the nextReviewViList where rating is not null
        defaultNextReviewViFiltering("rating.specified=true", "rating.specified=false");
    }

    @Test
    @Transactional
    void getAllNextReviewVisByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReviewVi = nextReviewViRepository.saveAndFlush(nextReviewVi);

        // Get all the nextReviewViList where rating is greater than or equal to
        defaultNextReviewViFiltering("rating.greaterThanOrEqual=" + DEFAULT_RATING, "rating.greaterThanOrEqual=" + (DEFAULT_RATING + 1));
    }

    @Test
    @Transactional
    void getAllNextReviewVisByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReviewVi = nextReviewViRepository.saveAndFlush(nextReviewVi);

        // Get all the nextReviewViList where rating is less than or equal to
        defaultNextReviewViFiltering("rating.lessThanOrEqual=" + DEFAULT_RATING, "rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewVisByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextReviewVi = nextReviewViRepository.saveAndFlush(nextReviewVi);

        // Get all the nextReviewViList where rating is less than
        defaultNextReviewViFiltering("rating.lessThan=" + (DEFAULT_RATING + 1), "rating.lessThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewVisByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextReviewVi = nextReviewViRepository.saveAndFlush(nextReviewVi);

        // Get all the nextReviewViList where rating is greater than
        defaultNextReviewViFiltering("rating.greaterThan=" + SMALLER_RATING, "rating.greaterThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewVisByReviewDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReviewVi = nextReviewViRepository.saveAndFlush(nextReviewVi);

        // Get all the nextReviewViList where reviewDate equals to
        defaultNextReviewViFiltering("reviewDate.equals=" + DEFAULT_REVIEW_DATE, "reviewDate.equals=" + UPDATED_REVIEW_DATE);
    }

    @Test
    @Transactional
    void getAllNextReviewVisByReviewDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextReviewVi = nextReviewViRepository.saveAndFlush(nextReviewVi);

        // Get all the nextReviewViList where reviewDate in
        defaultNextReviewViFiltering(
            "reviewDate.in=" + DEFAULT_REVIEW_DATE + "," + UPDATED_REVIEW_DATE,
            "reviewDate.in=" + UPDATED_REVIEW_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextReviewVisByReviewDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextReviewVi = nextReviewViRepository.saveAndFlush(nextReviewVi);

        // Get all the nextReviewViList where reviewDate is not null
        defaultNextReviewViFiltering("reviewDate.specified=true", "reviewDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextReviewVisByProductIsEqualToSomething() throws Exception {
        NextProductVi product;
        if (TestUtil.findAll(em, NextProductVi.class).isEmpty()) {
            nextReviewViRepository.saveAndFlush(nextReviewVi);
            product = NextProductViResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, NextProductVi.class).get(0);
        }
        em.persist(product);
        em.flush();
        nextReviewVi.setProduct(product);
        nextReviewViRepository.saveAndFlush(nextReviewVi);
        Long productId = product.getId();
        // Get all the nextReviewViList where product equals to productId
        defaultNextReviewViShouldBeFound("productId.equals=" + productId);

        // Get all the nextReviewViList where product equals to (productId + 1)
        defaultNextReviewViShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllNextReviewVisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextReviewViRepository.saveAndFlush(nextReviewVi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextReviewVi.setTenant(tenant);
        nextReviewViRepository.saveAndFlush(nextReviewVi);
        Long tenantId = tenant.getId();
        // Get all the nextReviewViList where tenant equals to tenantId
        defaultNextReviewViShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextReviewViList where tenant equals to (tenantId + 1)
        defaultNextReviewViShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextReviewViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextReviewViShouldBeFound(shouldBeFound);
        defaultNextReviewViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextReviewViShouldBeFound(String filter) throws Exception {
        restNextReviewViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextReviewVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));

        // Check, that the count call also returns 1
        restNextReviewViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextReviewViShouldNotBeFound(String filter) throws Exception {
        restNextReviewViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextReviewViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextReviewVi() throws Exception {
        // Get the nextReviewVi
        restNextReviewViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextReviewVi() throws Exception {
        // Initialize the database
        insertedNextReviewVi = nextReviewViRepository.saveAndFlush(nextReviewVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextReviewVi
        NextReviewVi updatedNextReviewVi = nextReviewViRepository.findById(nextReviewVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextReviewVi are not directly saved in db
        em.detach(updatedNextReviewVi);
        updatedNextReviewVi.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);
        NextReviewViDTO nextReviewViDTO = nextReviewViMapper.toDto(updatedNextReviewVi);

        restNextReviewViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextReviewViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextReviewViDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextReviewVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextReviewViToMatchAllProperties(updatedNextReviewVi);
    }

    @Test
    @Transactional
    void putNonExistingNextReviewVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewVi.setId(longCount.incrementAndGet());

        // Create the NextReviewVi
        NextReviewViDTO nextReviewViDTO = nextReviewViMapper.toDto(nextReviewVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextReviewViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextReviewViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextReviewViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReviewVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextReviewVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewVi.setId(longCount.incrementAndGet());

        // Create the NextReviewVi
        NextReviewViDTO nextReviewViDTO = nextReviewViMapper.toDto(nextReviewVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextReviewViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReviewVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextReviewVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewVi.setId(longCount.incrementAndGet());

        // Create the NextReviewVi
        NextReviewViDTO nextReviewViDTO = nextReviewViMapper.toDto(nextReviewVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextReviewVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextReviewViWithPatch() throws Exception {
        // Initialize the database
        insertedNextReviewVi = nextReviewViRepository.saveAndFlush(nextReviewVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextReviewVi using partial update
        NextReviewVi partialUpdatedNextReviewVi = new NextReviewVi();
        partialUpdatedNextReviewVi.setId(nextReviewVi.getId());

        restNextReviewViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextReviewVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextReviewVi))
            )
            .andExpect(status().isOk());

        // Validate the NextReviewVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextReviewViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextReviewVi, nextReviewVi),
            getPersistedNextReviewVi(nextReviewVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextReviewViWithPatch() throws Exception {
        // Initialize the database
        insertedNextReviewVi = nextReviewViRepository.saveAndFlush(nextReviewVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextReviewVi using partial update
        NextReviewVi partialUpdatedNextReviewVi = new NextReviewVi();
        partialUpdatedNextReviewVi.setId(nextReviewVi.getId());

        partialUpdatedNextReviewVi.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);

        restNextReviewViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextReviewVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextReviewVi))
            )
            .andExpect(status().isOk());

        // Validate the NextReviewVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextReviewViUpdatableFieldsEquals(partialUpdatedNextReviewVi, getPersistedNextReviewVi(partialUpdatedNextReviewVi));
    }

    @Test
    @Transactional
    void patchNonExistingNextReviewVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewVi.setId(longCount.incrementAndGet());

        // Create the NextReviewVi
        NextReviewViDTO nextReviewViDTO = nextReviewViMapper.toDto(nextReviewVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextReviewViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextReviewViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextReviewViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReviewVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextReviewVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewVi.setId(longCount.incrementAndGet());

        // Create the NextReviewVi
        NextReviewViDTO nextReviewViDTO = nextReviewViMapper.toDto(nextReviewVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextReviewViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReviewVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextReviewVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewVi.setId(longCount.incrementAndGet());

        // Create the NextReviewVi
        NextReviewViDTO nextReviewViDTO = nextReviewViMapper.toDto(nextReviewVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextReviewViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextReviewVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextReviewVi() throws Exception {
        // Initialize the database
        insertedNextReviewVi = nextReviewViRepository.saveAndFlush(nextReviewVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextReviewVi
        restNextReviewViMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextReviewVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextReviewViRepository.count();
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

    protected NextReviewVi getPersistedNextReviewVi(NextReviewVi nextReviewVi) {
        return nextReviewViRepository.findById(nextReviewVi.getId()).orElseThrow();
    }

    protected void assertPersistedNextReviewViToMatchAllProperties(NextReviewVi expectedNextReviewVi) {
        assertNextReviewViAllPropertiesEquals(expectedNextReviewVi, getPersistedNextReviewVi(expectedNextReviewVi));
    }

    protected void assertPersistedNextReviewViToMatchUpdatableProperties(NextReviewVi expectedNextReviewVi) {
        assertNextReviewViAllUpdatablePropertiesEquals(expectedNextReviewVi, getPersistedNextReviewVi(expectedNextReviewVi));
    }
}
