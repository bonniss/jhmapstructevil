package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextReviewSigmaAsserts.*;
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
import xyz.jhmapstruct.domain.NextProductSigma;
import xyz.jhmapstruct.domain.NextReviewSigma;
import xyz.jhmapstruct.repository.NextReviewSigmaRepository;
import xyz.jhmapstruct.service.NextReviewSigmaService;
import xyz.jhmapstruct.service.dto.NextReviewSigmaDTO;
import xyz.jhmapstruct.service.mapper.NextReviewSigmaMapper;

/**
 * Integration tests for the {@link NextReviewSigmaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextReviewSigmaResourceIT {

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;
    private static final Integer SMALLER_RATING = 1 - 1;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_REVIEW_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REVIEW_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/next-review-sigmas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextReviewSigmaRepository nextReviewSigmaRepository;

    @Mock
    private NextReviewSigmaRepository nextReviewSigmaRepositoryMock;

    @Autowired
    private NextReviewSigmaMapper nextReviewSigmaMapper;

    @Mock
    private NextReviewSigmaService nextReviewSigmaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextReviewSigmaMockMvc;

    private NextReviewSigma nextReviewSigma;

    private NextReviewSigma insertedNextReviewSigma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextReviewSigma createEntity() {
        return new NextReviewSigma().rating(DEFAULT_RATING).comment(DEFAULT_COMMENT).reviewDate(DEFAULT_REVIEW_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextReviewSigma createUpdatedEntity() {
        return new NextReviewSigma().rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);
    }

    @BeforeEach
    public void initTest() {
        nextReviewSigma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextReviewSigma != null) {
            nextReviewSigmaRepository.delete(insertedNextReviewSigma);
            insertedNextReviewSigma = null;
        }
    }

    @Test
    @Transactional
    void createNextReviewSigma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextReviewSigma
        NextReviewSigmaDTO nextReviewSigmaDTO = nextReviewSigmaMapper.toDto(nextReviewSigma);
        var returnedNextReviewSigmaDTO = om.readValue(
            restNextReviewSigmaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewSigmaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextReviewSigmaDTO.class
        );

        // Validate the NextReviewSigma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextReviewSigma = nextReviewSigmaMapper.toEntity(returnedNextReviewSigmaDTO);
        assertNextReviewSigmaUpdatableFieldsEquals(returnedNextReviewSigma, getPersistedNextReviewSigma(returnedNextReviewSigma));

        insertedNextReviewSigma = returnedNextReviewSigma;
    }

    @Test
    @Transactional
    void createNextReviewSigmaWithExistingId() throws Exception {
        // Create the NextReviewSigma with an existing ID
        nextReviewSigma.setId(1L);
        NextReviewSigmaDTO nextReviewSigmaDTO = nextReviewSigmaMapper.toDto(nextReviewSigma);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextReviewSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewSigmaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextReviewSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRatingIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextReviewSigma.setRating(null);

        // Create the NextReviewSigma, which fails.
        NextReviewSigmaDTO nextReviewSigmaDTO = nextReviewSigmaMapper.toDto(nextReviewSigma);

        restNextReviewSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewSigmaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReviewDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextReviewSigma.setReviewDate(null);

        // Create the NextReviewSigma, which fails.
        NextReviewSigmaDTO nextReviewSigmaDTO = nextReviewSigmaMapper.toDto(nextReviewSigma);

        restNextReviewSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewSigmaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextReviewSigmas() throws Exception {
        // Initialize the database
        insertedNextReviewSigma = nextReviewSigmaRepository.saveAndFlush(nextReviewSigma);

        // Get all the nextReviewSigmaList
        restNextReviewSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextReviewSigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextReviewSigmasWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextReviewSigmaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextReviewSigmaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextReviewSigmaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextReviewSigmasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextReviewSigmaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextReviewSigmaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextReviewSigmaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextReviewSigma() throws Exception {
        // Initialize the database
        insertedNextReviewSigma = nextReviewSigmaRepository.saveAndFlush(nextReviewSigma);

        // Get the nextReviewSigma
        restNextReviewSigmaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextReviewSigma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextReviewSigma.getId().intValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.reviewDate").value(DEFAULT_REVIEW_DATE.toString()));
    }

    @Test
    @Transactional
    void getNextReviewSigmasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextReviewSigma = nextReviewSigmaRepository.saveAndFlush(nextReviewSigma);

        Long id = nextReviewSigma.getId();

        defaultNextReviewSigmaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextReviewSigmaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextReviewSigmaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextReviewSigmasByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReviewSigma = nextReviewSigmaRepository.saveAndFlush(nextReviewSigma);

        // Get all the nextReviewSigmaList where rating equals to
        defaultNextReviewSigmaFiltering("rating.equals=" + DEFAULT_RATING, "rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewSigmasByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextReviewSigma = nextReviewSigmaRepository.saveAndFlush(nextReviewSigma);

        // Get all the nextReviewSigmaList where rating in
        defaultNextReviewSigmaFiltering("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING, "rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewSigmasByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextReviewSigma = nextReviewSigmaRepository.saveAndFlush(nextReviewSigma);

        // Get all the nextReviewSigmaList where rating is not null
        defaultNextReviewSigmaFiltering("rating.specified=true", "rating.specified=false");
    }

    @Test
    @Transactional
    void getAllNextReviewSigmasByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReviewSigma = nextReviewSigmaRepository.saveAndFlush(nextReviewSigma);

        // Get all the nextReviewSigmaList where rating is greater than or equal to
        defaultNextReviewSigmaFiltering("rating.greaterThanOrEqual=" + DEFAULT_RATING, "rating.greaterThanOrEqual=" + (DEFAULT_RATING + 1));
    }

    @Test
    @Transactional
    void getAllNextReviewSigmasByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReviewSigma = nextReviewSigmaRepository.saveAndFlush(nextReviewSigma);

        // Get all the nextReviewSigmaList where rating is less than or equal to
        defaultNextReviewSigmaFiltering("rating.lessThanOrEqual=" + DEFAULT_RATING, "rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewSigmasByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextReviewSigma = nextReviewSigmaRepository.saveAndFlush(nextReviewSigma);

        // Get all the nextReviewSigmaList where rating is less than
        defaultNextReviewSigmaFiltering("rating.lessThan=" + (DEFAULT_RATING + 1), "rating.lessThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewSigmasByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextReviewSigma = nextReviewSigmaRepository.saveAndFlush(nextReviewSigma);

        // Get all the nextReviewSigmaList where rating is greater than
        defaultNextReviewSigmaFiltering("rating.greaterThan=" + SMALLER_RATING, "rating.greaterThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewSigmasByReviewDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReviewSigma = nextReviewSigmaRepository.saveAndFlush(nextReviewSigma);

        // Get all the nextReviewSigmaList where reviewDate equals to
        defaultNextReviewSigmaFiltering("reviewDate.equals=" + DEFAULT_REVIEW_DATE, "reviewDate.equals=" + UPDATED_REVIEW_DATE);
    }

    @Test
    @Transactional
    void getAllNextReviewSigmasByReviewDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextReviewSigma = nextReviewSigmaRepository.saveAndFlush(nextReviewSigma);

        // Get all the nextReviewSigmaList where reviewDate in
        defaultNextReviewSigmaFiltering(
            "reviewDate.in=" + DEFAULT_REVIEW_DATE + "," + UPDATED_REVIEW_DATE,
            "reviewDate.in=" + UPDATED_REVIEW_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextReviewSigmasByReviewDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextReviewSigma = nextReviewSigmaRepository.saveAndFlush(nextReviewSigma);

        // Get all the nextReviewSigmaList where reviewDate is not null
        defaultNextReviewSigmaFiltering("reviewDate.specified=true", "reviewDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextReviewSigmasByProductIsEqualToSomething() throws Exception {
        NextProductSigma product;
        if (TestUtil.findAll(em, NextProductSigma.class).isEmpty()) {
            nextReviewSigmaRepository.saveAndFlush(nextReviewSigma);
            product = NextProductSigmaResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, NextProductSigma.class).get(0);
        }
        em.persist(product);
        em.flush();
        nextReviewSigma.setProduct(product);
        nextReviewSigmaRepository.saveAndFlush(nextReviewSigma);
        Long productId = product.getId();
        // Get all the nextReviewSigmaList where product equals to productId
        defaultNextReviewSigmaShouldBeFound("productId.equals=" + productId);

        // Get all the nextReviewSigmaList where product equals to (productId + 1)
        defaultNextReviewSigmaShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllNextReviewSigmasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextReviewSigmaRepository.saveAndFlush(nextReviewSigma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextReviewSigma.setTenant(tenant);
        nextReviewSigmaRepository.saveAndFlush(nextReviewSigma);
        Long tenantId = tenant.getId();
        // Get all the nextReviewSigmaList where tenant equals to tenantId
        defaultNextReviewSigmaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextReviewSigmaList where tenant equals to (tenantId + 1)
        defaultNextReviewSigmaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextReviewSigmaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextReviewSigmaShouldBeFound(shouldBeFound);
        defaultNextReviewSigmaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextReviewSigmaShouldBeFound(String filter) throws Exception {
        restNextReviewSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextReviewSigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));

        // Check, that the count call also returns 1
        restNextReviewSigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextReviewSigmaShouldNotBeFound(String filter) throws Exception {
        restNextReviewSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextReviewSigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextReviewSigma() throws Exception {
        // Get the nextReviewSigma
        restNextReviewSigmaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextReviewSigma() throws Exception {
        // Initialize the database
        insertedNextReviewSigma = nextReviewSigmaRepository.saveAndFlush(nextReviewSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextReviewSigma
        NextReviewSigma updatedNextReviewSigma = nextReviewSigmaRepository.findById(nextReviewSigma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextReviewSigma are not directly saved in db
        em.detach(updatedNextReviewSigma);
        updatedNextReviewSigma.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);
        NextReviewSigmaDTO nextReviewSigmaDTO = nextReviewSigmaMapper.toDto(updatedNextReviewSigma);

        restNextReviewSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextReviewSigmaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextReviewSigmaDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextReviewSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextReviewSigmaToMatchAllProperties(updatedNextReviewSigma);
    }

    @Test
    @Transactional
    void putNonExistingNextReviewSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewSigma.setId(longCount.incrementAndGet());

        // Create the NextReviewSigma
        NextReviewSigmaDTO nextReviewSigmaDTO = nextReviewSigmaMapper.toDto(nextReviewSigma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextReviewSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextReviewSigmaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextReviewSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReviewSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextReviewSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewSigma.setId(longCount.incrementAndGet());

        // Create the NextReviewSigma
        NextReviewSigmaDTO nextReviewSigmaDTO = nextReviewSigmaMapper.toDto(nextReviewSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextReviewSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReviewSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextReviewSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewSigma.setId(longCount.incrementAndGet());

        // Create the NextReviewSigma
        NextReviewSigmaDTO nextReviewSigmaDTO = nextReviewSigmaMapper.toDto(nextReviewSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewSigmaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewSigmaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextReviewSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextReviewSigmaWithPatch() throws Exception {
        // Initialize the database
        insertedNextReviewSigma = nextReviewSigmaRepository.saveAndFlush(nextReviewSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextReviewSigma using partial update
        NextReviewSigma partialUpdatedNextReviewSigma = new NextReviewSigma();
        partialUpdatedNextReviewSigma.setId(nextReviewSigma.getId());

        partialUpdatedNextReviewSigma.rating(UPDATED_RATING).reviewDate(UPDATED_REVIEW_DATE);

        restNextReviewSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextReviewSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextReviewSigma))
            )
            .andExpect(status().isOk());

        // Validate the NextReviewSigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextReviewSigmaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextReviewSigma, nextReviewSigma),
            getPersistedNextReviewSigma(nextReviewSigma)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextReviewSigmaWithPatch() throws Exception {
        // Initialize the database
        insertedNextReviewSigma = nextReviewSigmaRepository.saveAndFlush(nextReviewSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextReviewSigma using partial update
        NextReviewSigma partialUpdatedNextReviewSigma = new NextReviewSigma();
        partialUpdatedNextReviewSigma.setId(nextReviewSigma.getId());

        partialUpdatedNextReviewSigma.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);

        restNextReviewSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextReviewSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextReviewSigma))
            )
            .andExpect(status().isOk());

        // Validate the NextReviewSigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextReviewSigmaUpdatableFieldsEquals(
            partialUpdatedNextReviewSigma,
            getPersistedNextReviewSigma(partialUpdatedNextReviewSigma)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextReviewSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewSigma.setId(longCount.incrementAndGet());

        // Create the NextReviewSigma
        NextReviewSigmaDTO nextReviewSigmaDTO = nextReviewSigmaMapper.toDto(nextReviewSigma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextReviewSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextReviewSigmaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextReviewSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReviewSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextReviewSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewSigma.setId(longCount.incrementAndGet());

        // Create the NextReviewSigma
        NextReviewSigmaDTO nextReviewSigmaDTO = nextReviewSigmaMapper.toDto(nextReviewSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextReviewSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReviewSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextReviewSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewSigma.setId(longCount.incrementAndGet());

        // Create the NextReviewSigma
        NextReviewSigmaDTO nextReviewSigmaDTO = nextReviewSigmaMapper.toDto(nextReviewSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewSigmaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextReviewSigmaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextReviewSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextReviewSigma() throws Exception {
        // Initialize the database
        insertedNextReviewSigma = nextReviewSigmaRepository.saveAndFlush(nextReviewSigma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextReviewSigma
        restNextReviewSigmaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextReviewSigma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextReviewSigmaRepository.count();
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

    protected NextReviewSigma getPersistedNextReviewSigma(NextReviewSigma nextReviewSigma) {
        return nextReviewSigmaRepository.findById(nextReviewSigma.getId()).orElseThrow();
    }

    protected void assertPersistedNextReviewSigmaToMatchAllProperties(NextReviewSigma expectedNextReviewSigma) {
        assertNextReviewSigmaAllPropertiesEquals(expectedNextReviewSigma, getPersistedNextReviewSigma(expectedNextReviewSigma));
    }

    protected void assertPersistedNextReviewSigmaToMatchUpdatableProperties(NextReviewSigma expectedNextReviewSigma) {
        assertNextReviewSigmaAllUpdatablePropertiesEquals(expectedNextReviewSigma, getPersistedNextReviewSigma(expectedNextReviewSigma));
    }
}
