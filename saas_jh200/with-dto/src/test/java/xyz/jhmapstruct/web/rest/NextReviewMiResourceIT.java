package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextReviewMiAsserts.*;
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
import xyz.jhmapstruct.domain.NextReviewMi;
import xyz.jhmapstruct.domain.ProductMi;
import xyz.jhmapstruct.repository.NextReviewMiRepository;
import xyz.jhmapstruct.service.NextReviewMiService;
import xyz.jhmapstruct.service.dto.NextReviewMiDTO;
import xyz.jhmapstruct.service.mapper.NextReviewMiMapper;

/**
 * Integration tests for the {@link NextReviewMiResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextReviewMiResourceIT {

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;
    private static final Integer SMALLER_RATING = 1 - 1;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_REVIEW_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REVIEW_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/next-review-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextReviewMiRepository nextReviewMiRepository;

    @Mock
    private NextReviewMiRepository nextReviewMiRepositoryMock;

    @Autowired
    private NextReviewMiMapper nextReviewMiMapper;

    @Mock
    private NextReviewMiService nextReviewMiServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextReviewMiMockMvc;

    private NextReviewMi nextReviewMi;

    private NextReviewMi insertedNextReviewMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextReviewMi createEntity() {
        return new NextReviewMi().rating(DEFAULT_RATING).comment(DEFAULT_COMMENT).reviewDate(DEFAULT_REVIEW_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextReviewMi createUpdatedEntity() {
        return new NextReviewMi().rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);
    }

    @BeforeEach
    public void initTest() {
        nextReviewMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextReviewMi != null) {
            nextReviewMiRepository.delete(insertedNextReviewMi);
            insertedNextReviewMi = null;
        }
    }

    @Test
    @Transactional
    void createNextReviewMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextReviewMi
        NextReviewMiDTO nextReviewMiDTO = nextReviewMiMapper.toDto(nextReviewMi);
        var returnedNextReviewMiDTO = om.readValue(
            restNextReviewMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewMiDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextReviewMiDTO.class
        );

        // Validate the NextReviewMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextReviewMi = nextReviewMiMapper.toEntity(returnedNextReviewMiDTO);
        assertNextReviewMiUpdatableFieldsEquals(returnedNextReviewMi, getPersistedNextReviewMi(returnedNextReviewMi));

        insertedNextReviewMi = returnedNextReviewMi;
    }

    @Test
    @Transactional
    void createNextReviewMiWithExistingId() throws Exception {
        // Create the NextReviewMi with an existing ID
        nextReviewMi.setId(1L);
        NextReviewMiDTO nextReviewMiDTO = nextReviewMiMapper.toDto(nextReviewMi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextReviewMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewMiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextReviewMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRatingIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextReviewMi.setRating(null);

        // Create the NextReviewMi, which fails.
        NextReviewMiDTO nextReviewMiDTO = nextReviewMiMapper.toDto(nextReviewMi);

        restNextReviewMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReviewDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextReviewMi.setReviewDate(null);

        // Create the NextReviewMi, which fails.
        NextReviewMiDTO nextReviewMiDTO = nextReviewMiMapper.toDto(nextReviewMi);

        restNextReviewMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextReviewMis() throws Exception {
        // Initialize the database
        insertedNextReviewMi = nextReviewMiRepository.saveAndFlush(nextReviewMi);

        // Get all the nextReviewMiList
        restNextReviewMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextReviewMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextReviewMisWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextReviewMiServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextReviewMiMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextReviewMiServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextReviewMisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextReviewMiServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextReviewMiMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextReviewMiRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextReviewMi() throws Exception {
        // Initialize the database
        insertedNextReviewMi = nextReviewMiRepository.saveAndFlush(nextReviewMi);

        // Get the nextReviewMi
        restNextReviewMiMockMvc
            .perform(get(ENTITY_API_URL_ID, nextReviewMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextReviewMi.getId().intValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.reviewDate").value(DEFAULT_REVIEW_DATE.toString()));
    }

    @Test
    @Transactional
    void getNextReviewMisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextReviewMi = nextReviewMiRepository.saveAndFlush(nextReviewMi);

        Long id = nextReviewMi.getId();

        defaultNextReviewMiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextReviewMiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextReviewMiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextReviewMisByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReviewMi = nextReviewMiRepository.saveAndFlush(nextReviewMi);

        // Get all the nextReviewMiList where rating equals to
        defaultNextReviewMiFiltering("rating.equals=" + DEFAULT_RATING, "rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewMisByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextReviewMi = nextReviewMiRepository.saveAndFlush(nextReviewMi);

        // Get all the nextReviewMiList where rating in
        defaultNextReviewMiFiltering("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING, "rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewMisByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextReviewMi = nextReviewMiRepository.saveAndFlush(nextReviewMi);

        // Get all the nextReviewMiList where rating is not null
        defaultNextReviewMiFiltering("rating.specified=true", "rating.specified=false");
    }

    @Test
    @Transactional
    void getAllNextReviewMisByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReviewMi = nextReviewMiRepository.saveAndFlush(nextReviewMi);

        // Get all the nextReviewMiList where rating is greater than or equal to
        defaultNextReviewMiFiltering("rating.greaterThanOrEqual=" + DEFAULT_RATING, "rating.greaterThanOrEqual=" + (DEFAULT_RATING + 1));
    }

    @Test
    @Transactional
    void getAllNextReviewMisByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReviewMi = nextReviewMiRepository.saveAndFlush(nextReviewMi);

        // Get all the nextReviewMiList where rating is less than or equal to
        defaultNextReviewMiFiltering("rating.lessThanOrEqual=" + DEFAULT_RATING, "rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewMisByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextReviewMi = nextReviewMiRepository.saveAndFlush(nextReviewMi);

        // Get all the nextReviewMiList where rating is less than
        defaultNextReviewMiFiltering("rating.lessThan=" + (DEFAULT_RATING + 1), "rating.lessThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewMisByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextReviewMi = nextReviewMiRepository.saveAndFlush(nextReviewMi);

        // Get all the nextReviewMiList where rating is greater than
        defaultNextReviewMiFiltering("rating.greaterThan=" + SMALLER_RATING, "rating.greaterThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllNextReviewMisByReviewDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextReviewMi = nextReviewMiRepository.saveAndFlush(nextReviewMi);

        // Get all the nextReviewMiList where reviewDate equals to
        defaultNextReviewMiFiltering("reviewDate.equals=" + DEFAULT_REVIEW_DATE, "reviewDate.equals=" + UPDATED_REVIEW_DATE);
    }

    @Test
    @Transactional
    void getAllNextReviewMisByReviewDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextReviewMi = nextReviewMiRepository.saveAndFlush(nextReviewMi);

        // Get all the nextReviewMiList where reviewDate in
        defaultNextReviewMiFiltering(
            "reviewDate.in=" + DEFAULT_REVIEW_DATE + "," + UPDATED_REVIEW_DATE,
            "reviewDate.in=" + UPDATED_REVIEW_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextReviewMisByReviewDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextReviewMi = nextReviewMiRepository.saveAndFlush(nextReviewMi);

        // Get all the nextReviewMiList where reviewDate is not null
        defaultNextReviewMiFiltering("reviewDate.specified=true", "reviewDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextReviewMisByProductIsEqualToSomething() throws Exception {
        ProductMi product;
        if (TestUtil.findAll(em, ProductMi.class).isEmpty()) {
            nextReviewMiRepository.saveAndFlush(nextReviewMi);
            product = ProductMiResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, ProductMi.class).get(0);
        }
        em.persist(product);
        em.flush();
        nextReviewMi.setProduct(product);
        nextReviewMiRepository.saveAndFlush(nextReviewMi);
        Long productId = product.getId();
        // Get all the nextReviewMiList where product equals to productId
        defaultNextReviewMiShouldBeFound("productId.equals=" + productId);

        // Get all the nextReviewMiList where product equals to (productId + 1)
        defaultNextReviewMiShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllNextReviewMisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextReviewMiRepository.saveAndFlush(nextReviewMi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextReviewMi.setTenant(tenant);
        nextReviewMiRepository.saveAndFlush(nextReviewMi);
        Long tenantId = tenant.getId();
        // Get all the nextReviewMiList where tenant equals to tenantId
        defaultNextReviewMiShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextReviewMiList where tenant equals to (tenantId + 1)
        defaultNextReviewMiShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextReviewMiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextReviewMiShouldBeFound(shouldBeFound);
        defaultNextReviewMiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextReviewMiShouldBeFound(String filter) throws Exception {
        restNextReviewMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextReviewMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));

        // Check, that the count call also returns 1
        restNextReviewMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextReviewMiShouldNotBeFound(String filter) throws Exception {
        restNextReviewMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextReviewMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextReviewMi() throws Exception {
        // Get the nextReviewMi
        restNextReviewMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextReviewMi() throws Exception {
        // Initialize the database
        insertedNextReviewMi = nextReviewMiRepository.saveAndFlush(nextReviewMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextReviewMi
        NextReviewMi updatedNextReviewMi = nextReviewMiRepository.findById(nextReviewMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextReviewMi are not directly saved in db
        em.detach(updatedNextReviewMi);
        updatedNextReviewMi.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);
        NextReviewMiDTO nextReviewMiDTO = nextReviewMiMapper.toDto(updatedNextReviewMi);

        restNextReviewMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextReviewMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextReviewMiDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextReviewMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextReviewMiToMatchAllProperties(updatedNextReviewMi);
    }

    @Test
    @Transactional
    void putNonExistingNextReviewMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewMi.setId(longCount.incrementAndGet());

        // Create the NextReviewMi
        NextReviewMiDTO nextReviewMiDTO = nextReviewMiMapper.toDto(nextReviewMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextReviewMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextReviewMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextReviewMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReviewMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextReviewMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewMi.setId(longCount.incrementAndGet());

        // Create the NextReviewMi
        NextReviewMiDTO nextReviewMiDTO = nextReviewMiMapper.toDto(nextReviewMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextReviewMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReviewMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextReviewMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewMi.setId(longCount.incrementAndGet());

        // Create the NextReviewMi
        NextReviewMiDTO nextReviewMiDTO = nextReviewMiMapper.toDto(nextReviewMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextReviewMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextReviewMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextReviewMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextReviewMi = nextReviewMiRepository.saveAndFlush(nextReviewMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextReviewMi using partial update
        NextReviewMi partialUpdatedNextReviewMi = new NextReviewMi();
        partialUpdatedNextReviewMi.setId(nextReviewMi.getId());

        partialUpdatedNextReviewMi.rating(UPDATED_RATING).comment(UPDATED_COMMENT);

        restNextReviewMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextReviewMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextReviewMi))
            )
            .andExpect(status().isOk());

        // Validate the NextReviewMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextReviewMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextReviewMi, nextReviewMi),
            getPersistedNextReviewMi(nextReviewMi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextReviewMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextReviewMi = nextReviewMiRepository.saveAndFlush(nextReviewMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextReviewMi using partial update
        NextReviewMi partialUpdatedNextReviewMi = new NextReviewMi();
        partialUpdatedNextReviewMi.setId(nextReviewMi.getId());

        partialUpdatedNextReviewMi.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);

        restNextReviewMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextReviewMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextReviewMi))
            )
            .andExpect(status().isOk());

        // Validate the NextReviewMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextReviewMiUpdatableFieldsEquals(partialUpdatedNextReviewMi, getPersistedNextReviewMi(partialUpdatedNextReviewMi));
    }

    @Test
    @Transactional
    void patchNonExistingNextReviewMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewMi.setId(longCount.incrementAndGet());

        // Create the NextReviewMi
        NextReviewMiDTO nextReviewMiDTO = nextReviewMiMapper.toDto(nextReviewMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextReviewMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextReviewMiDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextReviewMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReviewMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextReviewMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewMi.setId(longCount.incrementAndGet());

        // Create the NextReviewMi
        NextReviewMiDTO nextReviewMiDTO = nextReviewMiMapper.toDto(nextReviewMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextReviewMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextReviewMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextReviewMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextReviewMi.setId(longCount.incrementAndGet());

        // Create the NextReviewMi
        NextReviewMiDTO nextReviewMiDTO = nextReviewMiMapper.toDto(nextReviewMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextReviewMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextReviewMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextReviewMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextReviewMi() throws Exception {
        // Initialize the database
        insertedNextReviewMi = nextReviewMiRepository.saveAndFlush(nextReviewMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextReviewMi
        restNextReviewMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextReviewMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextReviewMiRepository.count();
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

    protected NextReviewMi getPersistedNextReviewMi(NextReviewMi nextReviewMi) {
        return nextReviewMiRepository.findById(nextReviewMi.getId()).orElseThrow();
    }

    protected void assertPersistedNextReviewMiToMatchAllProperties(NextReviewMi expectedNextReviewMi) {
        assertNextReviewMiAllPropertiesEquals(expectedNextReviewMi, getPersistedNextReviewMi(expectedNextReviewMi));
    }

    protected void assertPersistedNextReviewMiToMatchUpdatableProperties(NextReviewMi expectedNextReviewMi) {
        assertNextReviewMiAllUpdatablePropertiesEquals(expectedNextReviewMi, getPersistedNextReviewMi(expectedNextReviewMi));
    }
}
