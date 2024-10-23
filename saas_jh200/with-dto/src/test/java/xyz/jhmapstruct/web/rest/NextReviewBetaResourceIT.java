package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextReviewBetaAsserts.*;
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
import xyz.jhmapstruct.domain.NextProductBeta;
import xyz.jhmapstruct.domain.NextReviewBeta;
import xyz.jhmapstruct.repository.NextReviewBetaRepository;
import xyz.jhmapstruct.service.NextReviewBetaService;
import xyz.jhmapstruct.service.dto.NextReviewBetaDTO;
import xyz.jhmapstruct.service.mapper.NextReviewBetaMapper;

/**
 * Integration tests for the {@link NextReviewBetaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextReviewBetaResourceIT {

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;
    private static final Integer SMALLER_RATING = 1 - 1;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_REVIEW_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REVIEW_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/next-review-betas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextReviewBetaRepository nextReviewBetaRepository;

    @Mock
    private NextReviewBetaRepository nextReviewBetaRepositoryMock;

    @Autowired
    private NextReviewBetaMapper nextReviewBetaMapper;

    @Mock
    private NextReviewBetaService nextReviewBetaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextReviewBetaMockMvc;

    private NextReviewBeta nextReviewBeta;

    private NextReviewBeta insertedNextReviewBeta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextReviewBeta createEntity() {
        return new NextReviewBeta().rating(DEFAULT_RATING).comment(DEFAULT_COMMENT).reviewDate(DEFAULT_REVIEW_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextReviewBeta createUpdatedEntity() {
        return new NextReviewBeta().rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);
    }

    @BeforeEach
    public void initTest() {
        nextReviewBeta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextReviewBeta != null) {
            nextReviewBetaRepository.delete(insertedNextReviewBeta);
            insertedNextReviewBeta = null;
        }
    }

    @Test
    @Transactional
    void createNextReviewBeta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextReviewBeta
        NextReviewBetaDTO nextReviewBetaDTO = nextReviewBetaMapper.toDto(nextReviewBeta);
        var returnedNextReviewBetaDTO = om.readValue(
            restNextReviewBetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewBetaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextReviewBetaDTO.class
        );

        // Validate the NextReviewBeta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextReviewBeta = nextReviewBetaMapper.toEntity(returnedNextReviewBetaDTO);
        assertNextReviewBetaUpdatableFieldsEquals(returnedNextReviewBeta, getPersistedNextReviewBeta(returnedNextReviewBeta));

        insertedNextReviewBeta = returnedNextReviewBeta;
    }

    @Test
    @Transactional
    void createNextReviewBetaWithExistingId() throws Exception {
        // Create the NextReviewBeta with an existing ID
        nextReviewBeta.setId(1L);
        NextReviewBetaDTO nextReviewBetaDTO = nextReviewBetaMapper.toDto(nextReviewBeta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextReviewBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewBetaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextReviewBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRatingIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextReviewBeta.setRating(null);

        // Create the NextReviewBeta, which fails.
        NextReviewBetaDTO nextReviewBetaDTO = nextReviewBetaMapper.toDto(nextReviewBeta);

        restNextReviewBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewBetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReviewDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextReviewBeta.setReviewDate(null);

        // Create the NextReviewBeta, which fails.
        NextReviewBetaDTO nextReviewBetaDTO = nextReviewBetaMapper.toDto(nextReviewBeta);

        restNextReviewBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewBetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextReviewBetas() throws Exception {
        // Initialize the database
        insertedNextReviewBeta = nextReviewBetaRepository.saveAndFlush(nextReviewBeta);

        // Get all the nextReviewBetaList
        restNextReviewBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextReviewBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextReviewBetasWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextReviewBetaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextReviewBetaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextReviewBetaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextReviewBetasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextReviewBetaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextReviewBetaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextReviewBetaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextReviewBeta() throws Exception {
        // Initialize the database
        insertedNextReviewBeta = nextReviewBetaRepository.saveAndFlush(nextReviewBeta);

        // Get the nextReviewBeta
        restNextReviewBetaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextReviewBeta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextReviewBeta.getId().intValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.reviewDate").value(DEFAULT_REVIEW_DATE.toString()));
    }

    @Test
    @Transactional
    void getNextReviewBetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextReviewBeta = nextReviewBetaRepository.saveAndFlush(nextReviewBeta);

        Long id = nextReviewBeta.getId();

        defaultNextReviewBetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextReviewBetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextReviewBetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextReviewBetasByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReviewBeta = nextReviewBetaRepository.saveAndFlush(nextReviewBeta);

        // Get all the nextReviewBetaList where rating equals to
        defaultNextReviewBetaFiltering("rating.equals=" + DEFAULT_RATING, "rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewBetasByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextReviewBeta = nextReviewBetaRepository.saveAndFlush(nextReviewBeta);

        // Get all the nextReviewBetaList where rating in
        defaultNextReviewBetaFiltering("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING, "rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewBetasByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextReviewBeta = nextReviewBetaRepository.saveAndFlush(nextReviewBeta);

        // Get all the nextReviewBetaList where rating is not null
        defaultNextReviewBetaFiltering("rating.specified=true", "rating.specified=false");
    }

    @Test
    @Transactional
    void getAllNextReviewBetasByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReviewBeta = nextReviewBetaRepository.saveAndFlush(nextReviewBeta);

        // Get all the nextReviewBetaList where rating is greater than or equal to
        defaultNextReviewBetaFiltering("rating.greaterThanOrEqual=" + DEFAULT_RATING, "rating.greaterThanOrEqual=" + (DEFAULT_RATING + 1));
    }

    @Test
    @Transactional
    void getAllNextReviewBetasByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReviewBeta = nextReviewBetaRepository.saveAndFlush(nextReviewBeta);

        // Get all the nextReviewBetaList where rating is less than or equal to
        defaultNextReviewBetaFiltering("rating.lessThanOrEqual=" + DEFAULT_RATING, "rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewBetasByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextReviewBeta = nextReviewBetaRepository.saveAndFlush(nextReviewBeta);

        // Get all the nextReviewBetaList where rating is less than
        defaultNextReviewBetaFiltering("rating.lessThan=" + (DEFAULT_RATING + 1), "rating.lessThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewBetasByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextReviewBeta = nextReviewBetaRepository.saveAndFlush(nextReviewBeta);

        // Get all the nextReviewBetaList where rating is greater than
        defaultNextReviewBetaFiltering("rating.greaterThan=" + SMALLER_RATING, "rating.greaterThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewBetasByReviewDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReviewBeta = nextReviewBetaRepository.saveAndFlush(nextReviewBeta);

        // Get all the nextReviewBetaList where reviewDate equals to
        defaultNextReviewBetaFiltering("reviewDate.equals=" + DEFAULT_REVIEW_DATE, "reviewDate.equals=" + UPDATED_REVIEW_DATE);
    }

    @Test
    @Transactional
    void getAllNextReviewBetasByReviewDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextReviewBeta = nextReviewBetaRepository.saveAndFlush(nextReviewBeta);

        // Get all the nextReviewBetaList where reviewDate in
        defaultNextReviewBetaFiltering(
            "reviewDate.in=" + DEFAULT_REVIEW_DATE + "," + UPDATED_REVIEW_DATE,
            "reviewDate.in=" + UPDATED_REVIEW_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextReviewBetasByReviewDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextReviewBeta = nextReviewBetaRepository.saveAndFlush(nextReviewBeta);

        // Get all the nextReviewBetaList where reviewDate is not null
        defaultNextReviewBetaFiltering("reviewDate.specified=true", "reviewDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextReviewBetasByProductIsEqualToSomething() throws Exception {
        NextProductBeta product;
        if (TestUtil.findAll(em, NextProductBeta.class).isEmpty()) {
            nextReviewBetaRepository.saveAndFlush(nextReviewBeta);
            product = NextProductBetaResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, NextProductBeta.class).get(0);
        }
        em.persist(product);
        em.flush();
        nextReviewBeta.setProduct(product);
        nextReviewBetaRepository.saveAndFlush(nextReviewBeta);
        Long productId = product.getId();
        // Get all the nextReviewBetaList where product equals to productId
        defaultNextReviewBetaShouldBeFound("productId.equals=" + productId);

        // Get all the nextReviewBetaList where product equals to (productId + 1)
        defaultNextReviewBetaShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllNextReviewBetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextReviewBetaRepository.saveAndFlush(nextReviewBeta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextReviewBeta.setTenant(tenant);
        nextReviewBetaRepository.saveAndFlush(nextReviewBeta);
        Long tenantId = tenant.getId();
        // Get all the nextReviewBetaList where tenant equals to tenantId
        defaultNextReviewBetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextReviewBetaList where tenant equals to (tenantId + 1)
        defaultNextReviewBetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextReviewBetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextReviewBetaShouldBeFound(shouldBeFound);
        defaultNextReviewBetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextReviewBetaShouldBeFound(String filter) throws Exception {
        restNextReviewBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextReviewBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));

        // Check, that the count call also returns 1
        restNextReviewBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextReviewBetaShouldNotBeFound(String filter) throws Exception {
        restNextReviewBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextReviewBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextReviewBeta() throws Exception {
        // Get the nextReviewBeta
        restNextReviewBetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextReviewBeta() throws Exception {
        // Initialize the database
        insertedNextReviewBeta = nextReviewBetaRepository.saveAndFlush(nextReviewBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextReviewBeta
        NextReviewBeta updatedNextReviewBeta = nextReviewBetaRepository.findById(nextReviewBeta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextReviewBeta are not directly saved in db
        em.detach(updatedNextReviewBeta);
        updatedNextReviewBeta.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);
        NextReviewBetaDTO nextReviewBetaDTO = nextReviewBetaMapper.toDto(updatedNextReviewBeta);

        restNextReviewBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextReviewBetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextReviewBetaDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextReviewBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextReviewBetaToMatchAllProperties(updatedNextReviewBeta);
    }

    @Test
    @Transactional
    void putNonExistingNextReviewBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewBeta.setId(longCount.incrementAndGet());

        // Create the NextReviewBeta
        NextReviewBetaDTO nextReviewBetaDTO = nextReviewBetaMapper.toDto(nextReviewBeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextReviewBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextReviewBetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextReviewBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReviewBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextReviewBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewBeta.setId(longCount.incrementAndGet());

        // Create the NextReviewBeta
        NextReviewBetaDTO nextReviewBetaDTO = nextReviewBetaMapper.toDto(nextReviewBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextReviewBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReviewBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextReviewBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewBeta.setId(longCount.incrementAndGet());

        // Create the NextReviewBeta
        NextReviewBetaDTO nextReviewBetaDTO = nextReviewBetaMapper.toDto(nextReviewBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewBetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewBetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextReviewBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextReviewBetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextReviewBeta = nextReviewBetaRepository.saveAndFlush(nextReviewBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextReviewBeta using partial update
        NextReviewBeta partialUpdatedNextReviewBeta = new NextReviewBeta();
        partialUpdatedNextReviewBeta.setId(nextReviewBeta.getId());

        partialUpdatedNextReviewBeta.rating(UPDATED_RATING).reviewDate(UPDATED_REVIEW_DATE);

        restNextReviewBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextReviewBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextReviewBeta))
            )
            .andExpect(status().isOk());

        // Validate the NextReviewBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextReviewBetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextReviewBeta, nextReviewBeta),
            getPersistedNextReviewBeta(nextReviewBeta)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextReviewBetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextReviewBeta = nextReviewBetaRepository.saveAndFlush(nextReviewBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextReviewBeta using partial update
        NextReviewBeta partialUpdatedNextReviewBeta = new NextReviewBeta();
        partialUpdatedNextReviewBeta.setId(nextReviewBeta.getId());

        partialUpdatedNextReviewBeta.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);

        restNextReviewBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextReviewBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextReviewBeta))
            )
            .andExpect(status().isOk());

        // Validate the NextReviewBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextReviewBetaUpdatableFieldsEquals(partialUpdatedNextReviewBeta, getPersistedNextReviewBeta(partialUpdatedNextReviewBeta));
    }

    @Test
    @Transactional
    void patchNonExistingNextReviewBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewBeta.setId(longCount.incrementAndGet());

        // Create the NextReviewBeta
        NextReviewBetaDTO nextReviewBetaDTO = nextReviewBetaMapper.toDto(nextReviewBeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextReviewBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextReviewBetaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextReviewBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReviewBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextReviewBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewBeta.setId(longCount.incrementAndGet());

        // Create the NextReviewBeta
        NextReviewBetaDTO nextReviewBetaDTO = nextReviewBetaMapper.toDto(nextReviewBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextReviewBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReviewBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextReviewBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewBeta.setId(longCount.incrementAndGet());

        // Create the NextReviewBeta
        NextReviewBetaDTO nextReviewBetaDTO = nextReviewBetaMapper.toDto(nextReviewBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewBetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextReviewBetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextReviewBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextReviewBeta() throws Exception {
        // Initialize the database
        insertedNextReviewBeta = nextReviewBetaRepository.saveAndFlush(nextReviewBeta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextReviewBeta
        restNextReviewBetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextReviewBeta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextReviewBetaRepository.count();
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

    protected NextReviewBeta getPersistedNextReviewBeta(NextReviewBeta nextReviewBeta) {
        return nextReviewBetaRepository.findById(nextReviewBeta.getId()).orElseThrow();
    }

    protected void assertPersistedNextReviewBetaToMatchAllProperties(NextReviewBeta expectedNextReviewBeta) {
        assertNextReviewBetaAllPropertiesEquals(expectedNextReviewBeta, getPersistedNextReviewBeta(expectedNextReviewBeta));
    }

    protected void assertPersistedNextReviewBetaToMatchUpdatableProperties(NextReviewBeta expectedNextReviewBeta) {
        assertNextReviewBetaAllUpdatablePropertiesEquals(expectedNextReviewBeta, getPersistedNextReviewBeta(expectedNextReviewBeta));
    }
}
