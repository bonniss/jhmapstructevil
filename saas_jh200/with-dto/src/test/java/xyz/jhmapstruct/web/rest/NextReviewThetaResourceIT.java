package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextReviewThetaAsserts.*;
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
import xyz.jhmapstruct.domain.NextProductTheta;
import xyz.jhmapstruct.domain.NextReviewTheta;
import xyz.jhmapstruct.repository.NextReviewThetaRepository;
import xyz.jhmapstruct.service.NextReviewThetaService;
import xyz.jhmapstruct.service.dto.NextReviewThetaDTO;
import xyz.jhmapstruct.service.mapper.NextReviewThetaMapper;

/**
 * Integration tests for the {@link NextReviewThetaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextReviewThetaResourceIT {

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;
    private static final Integer SMALLER_RATING = 1 - 1;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_REVIEW_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REVIEW_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/next-review-thetas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextReviewThetaRepository nextReviewThetaRepository;

    @Mock
    private NextReviewThetaRepository nextReviewThetaRepositoryMock;

    @Autowired
    private NextReviewThetaMapper nextReviewThetaMapper;

    @Mock
    private NextReviewThetaService nextReviewThetaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextReviewThetaMockMvc;

    private NextReviewTheta nextReviewTheta;

    private NextReviewTheta insertedNextReviewTheta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextReviewTheta createEntity() {
        return new NextReviewTheta().rating(DEFAULT_RATING).comment(DEFAULT_COMMENT).reviewDate(DEFAULT_REVIEW_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextReviewTheta createUpdatedEntity() {
        return new NextReviewTheta().rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);
    }

    @BeforeEach
    public void initTest() {
        nextReviewTheta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextReviewTheta != null) {
            nextReviewThetaRepository.delete(insertedNextReviewTheta);
            insertedNextReviewTheta = null;
        }
    }

    @Test
    @Transactional
    void createNextReviewTheta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextReviewTheta
        NextReviewThetaDTO nextReviewThetaDTO = nextReviewThetaMapper.toDto(nextReviewTheta);
        var returnedNextReviewThetaDTO = om.readValue(
            restNextReviewThetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewThetaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextReviewThetaDTO.class
        );

        // Validate the NextReviewTheta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextReviewTheta = nextReviewThetaMapper.toEntity(returnedNextReviewThetaDTO);
        assertNextReviewThetaUpdatableFieldsEquals(returnedNextReviewTheta, getPersistedNextReviewTheta(returnedNextReviewTheta));

        insertedNextReviewTheta = returnedNextReviewTheta;
    }

    @Test
    @Transactional
    void createNextReviewThetaWithExistingId() throws Exception {
        // Create the NextReviewTheta with an existing ID
        nextReviewTheta.setId(1L);
        NextReviewThetaDTO nextReviewThetaDTO = nextReviewThetaMapper.toDto(nextReviewTheta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextReviewThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewThetaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextReviewTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRatingIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextReviewTheta.setRating(null);

        // Create the NextReviewTheta, which fails.
        NextReviewThetaDTO nextReviewThetaDTO = nextReviewThetaMapper.toDto(nextReviewTheta);

        restNextReviewThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewThetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReviewDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextReviewTheta.setReviewDate(null);

        // Create the NextReviewTheta, which fails.
        NextReviewThetaDTO nextReviewThetaDTO = nextReviewThetaMapper.toDto(nextReviewTheta);

        restNextReviewThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewThetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextReviewThetas() throws Exception {
        // Initialize the database
        insertedNextReviewTheta = nextReviewThetaRepository.saveAndFlush(nextReviewTheta);

        // Get all the nextReviewThetaList
        restNextReviewThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextReviewTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextReviewThetasWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextReviewThetaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextReviewThetaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextReviewThetaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextReviewThetasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextReviewThetaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextReviewThetaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextReviewThetaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextReviewTheta() throws Exception {
        // Initialize the database
        insertedNextReviewTheta = nextReviewThetaRepository.saveAndFlush(nextReviewTheta);

        // Get the nextReviewTheta
        restNextReviewThetaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextReviewTheta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextReviewTheta.getId().intValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.reviewDate").value(DEFAULT_REVIEW_DATE.toString()));
    }

    @Test
    @Transactional
    void getNextReviewThetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextReviewTheta = nextReviewThetaRepository.saveAndFlush(nextReviewTheta);

        Long id = nextReviewTheta.getId();

        defaultNextReviewThetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextReviewThetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextReviewThetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextReviewThetasByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReviewTheta = nextReviewThetaRepository.saveAndFlush(nextReviewTheta);

        // Get all the nextReviewThetaList where rating equals to
        defaultNextReviewThetaFiltering("rating.equals=" + DEFAULT_RATING, "rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewThetasByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextReviewTheta = nextReviewThetaRepository.saveAndFlush(nextReviewTheta);

        // Get all the nextReviewThetaList where rating in
        defaultNextReviewThetaFiltering("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING, "rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewThetasByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextReviewTheta = nextReviewThetaRepository.saveAndFlush(nextReviewTheta);

        // Get all the nextReviewThetaList where rating is not null
        defaultNextReviewThetaFiltering("rating.specified=true", "rating.specified=false");
    }

    @Test
    @Transactional
    void getAllNextReviewThetasByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReviewTheta = nextReviewThetaRepository.saveAndFlush(nextReviewTheta);

        // Get all the nextReviewThetaList where rating is greater than or equal to
        defaultNextReviewThetaFiltering("rating.greaterThanOrEqual=" + DEFAULT_RATING, "rating.greaterThanOrEqual=" + (DEFAULT_RATING + 1));
    }

    @Test
    @Transactional
    void getAllNextReviewThetasByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReviewTheta = nextReviewThetaRepository.saveAndFlush(nextReviewTheta);

        // Get all the nextReviewThetaList where rating is less than or equal to
        defaultNextReviewThetaFiltering("rating.lessThanOrEqual=" + DEFAULT_RATING, "rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewThetasByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextReviewTheta = nextReviewThetaRepository.saveAndFlush(nextReviewTheta);

        // Get all the nextReviewThetaList where rating is less than
        defaultNextReviewThetaFiltering("rating.lessThan=" + (DEFAULT_RATING + 1), "rating.lessThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewThetasByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextReviewTheta = nextReviewThetaRepository.saveAndFlush(nextReviewTheta);

        // Get all the nextReviewThetaList where rating is greater than
        defaultNextReviewThetaFiltering("rating.greaterThan=" + SMALLER_RATING, "rating.greaterThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewThetasByReviewDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReviewTheta = nextReviewThetaRepository.saveAndFlush(nextReviewTheta);

        // Get all the nextReviewThetaList where reviewDate equals to
        defaultNextReviewThetaFiltering("reviewDate.equals=" + DEFAULT_REVIEW_DATE, "reviewDate.equals=" + UPDATED_REVIEW_DATE);
    }

    @Test
    @Transactional
    void getAllNextReviewThetasByReviewDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextReviewTheta = nextReviewThetaRepository.saveAndFlush(nextReviewTheta);

        // Get all the nextReviewThetaList where reviewDate in
        defaultNextReviewThetaFiltering(
            "reviewDate.in=" + DEFAULT_REVIEW_DATE + "," + UPDATED_REVIEW_DATE,
            "reviewDate.in=" + UPDATED_REVIEW_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextReviewThetasByReviewDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextReviewTheta = nextReviewThetaRepository.saveAndFlush(nextReviewTheta);

        // Get all the nextReviewThetaList where reviewDate is not null
        defaultNextReviewThetaFiltering("reviewDate.specified=true", "reviewDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextReviewThetasByProductIsEqualToSomething() throws Exception {
        NextProductTheta product;
        if (TestUtil.findAll(em, NextProductTheta.class).isEmpty()) {
            nextReviewThetaRepository.saveAndFlush(nextReviewTheta);
            product = NextProductThetaResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, NextProductTheta.class).get(0);
        }
        em.persist(product);
        em.flush();
        nextReviewTheta.setProduct(product);
        nextReviewThetaRepository.saveAndFlush(nextReviewTheta);
        Long productId = product.getId();
        // Get all the nextReviewThetaList where product equals to productId
        defaultNextReviewThetaShouldBeFound("productId.equals=" + productId);

        // Get all the nextReviewThetaList where product equals to (productId + 1)
        defaultNextReviewThetaShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllNextReviewThetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextReviewThetaRepository.saveAndFlush(nextReviewTheta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextReviewTheta.setTenant(tenant);
        nextReviewThetaRepository.saveAndFlush(nextReviewTheta);
        Long tenantId = tenant.getId();
        // Get all the nextReviewThetaList where tenant equals to tenantId
        defaultNextReviewThetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextReviewThetaList where tenant equals to (tenantId + 1)
        defaultNextReviewThetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextReviewThetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextReviewThetaShouldBeFound(shouldBeFound);
        defaultNextReviewThetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextReviewThetaShouldBeFound(String filter) throws Exception {
        restNextReviewThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextReviewTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));

        // Check, that the count call also returns 1
        restNextReviewThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextReviewThetaShouldNotBeFound(String filter) throws Exception {
        restNextReviewThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextReviewThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextReviewTheta() throws Exception {
        // Get the nextReviewTheta
        restNextReviewThetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextReviewTheta() throws Exception {
        // Initialize the database
        insertedNextReviewTheta = nextReviewThetaRepository.saveAndFlush(nextReviewTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextReviewTheta
        NextReviewTheta updatedNextReviewTheta = nextReviewThetaRepository.findById(nextReviewTheta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextReviewTheta are not directly saved in db
        em.detach(updatedNextReviewTheta);
        updatedNextReviewTheta.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);
        NextReviewThetaDTO nextReviewThetaDTO = nextReviewThetaMapper.toDto(updatedNextReviewTheta);

        restNextReviewThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextReviewThetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextReviewThetaDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextReviewTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextReviewThetaToMatchAllProperties(updatedNextReviewTheta);
    }

    @Test
    @Transactional
    void putNonExistingNextReviewTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewTheta.setId(longCount.incrementAndGet());

        // Create the NextReviewTheta
        NextReviewThetaDTO nextReviewThetaDTO = nextReviewThetaMapper.toDto(nextReviewTheta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextReviewThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextReviewThetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextReviewThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReviewTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextReviewTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewTheta.setId(longCount.incrementAndGet());

        // Create the NextReviewTheta
        NextReviewThetaDTO nextReviewThetaDTO = nextReviewThetaMapper.toDto(nextReviewTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextReviewThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReviewTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextReviewTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewTheta.setId(longCount.incrementAndGet());

        // Create the NextReviewTheta
        NextReviewThetaDTO nextReviewThetaDTO = nextReviewThetaMapper.toDto(nextReviewTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewThetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewThetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextReviewTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextReviewThetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextReviewTheta = nextReviewThetaRepository.saveAndFlush(nextReviewTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextReviewTheta using partial update
        NextReviewTheta partialUpdatedNextReviewTheta = new NextReviewTheta();
        partialUpdatedNextReviewTheta.setId(nextReviewTheta.getId());

        partialUpdatedNextReviewTheta.reviewDate(UPDATED_REVIEW_DATE);

        restNextReviewThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextReviewTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextReviewTheta))
            )
            .andExpect(status().isOk());

        // Validate the NextReviewTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextReviewThetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextReviewTheta, nextReviewTheta),
            getPersistedNextReviewTheta(nextReviewTheta)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextReviewThetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextReviewTheta = nextReviewThetaRepository.saveAndFlush(nextReviewTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextReviewTheta using partial update
        NextReviewTheta partialUpdatedNextReviewTheta = new NextReviewTheta();
        partialUpdatedNextReviewTheta.setId(nextReviewTheta.getId());

        partialUpdatedNextReviewTheta.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);

        restNextReviewThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextReviewTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextReviewTheta))
            )
            .andExpect(status().isOk());

        // Validate the NextReviewTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextReviewThetaUpdatableFieldsEquals(
            partialUpdatedNextReviewTheta,
            getPersistedNextReviewTheta(partialUpdatedNextReviewTheta)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextReviewTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewTheta.setId(longCount.incrementAndGet());

        // Create the NextReviewTheta
        NextReviewThetaDTO nextReviewThetaDTO = nextReviewThetaMapper.toDto(nextReviewTheta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextReviewThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextReviewThetaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextReviewThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReviewTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextReviewTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewTheta.setId(longCount.incrementAndGet());

        // Create the NextReviewTheta
        NextReviewThetaDTO nextReviewThetaDTO = nextReviewThetaMapper.toDto(nextReviewTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextReviewThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReviewTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextReviewTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewTheta.setId(longCount.incrementAndGet());

        // Create the NextReviewTheta
        NextReviewThetaDTO nextReviewThetaDTO = nextReviewThetaMapper.toDto(nextReviewTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewThetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextReviewThetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextReviewTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextReviewTheta() throws Exception {
        // Initialize the database
        insertedNextReviewTheta = nextReviewThetaRepository.saveAndFlush(nextReviewTheta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextReviewTheta
        restNextReviewThetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextReviewTheta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextReviewThetaRepository.count();
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

    protected NextReviewTheta getPersistedNextReviewTheta(NextReviewTheta nextReviewTheta) {
        return nextReviewThetaRepository.findById(nextReviewTheta.getId()).orElseThrow();
    }

    protected void assertPersistedNextReviewThetaToMatchAllProperties(NextReviewTheta expectedNextReviewTheta) {
        assertNextReviewThetaAllPropertiesEquals(expectedNextReviewTheta, getPersistedNextReviewTheta(expectedNextReviewTheta));
    }

    protected void assertPersistedNextReviewThetaToMatchUpdatableProperties(NextReviewTheta expectedNextReviewTheta) {
        assertNextReviewThetaAllUpdatablePropertiesEquals(expectedNextReviewTheta, getPersistedNextReviewTheta(expectedNextReviewTheta));
    }
}
