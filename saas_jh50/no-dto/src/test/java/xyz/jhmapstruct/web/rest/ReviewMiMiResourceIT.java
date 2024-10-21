package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.ReviewMiMiAsserts.*;
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
import xyz.jhmapstruct.domain.ReviewMiMi;
import xyz.jhmapstruct.repository.ReviewMiMiRepository;
import xyz.jhmapstruct.service.ReviewMiMiService;

/**
 * Integration tests for the {@link ReviewMiMiResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ReviewMiMiResourceIT {

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_REVIEW_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REVIEW_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/review-mi-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReviewMiMiRepository reviewMiMiRepository;

    @Mock
    private ReviewMiMiRepository reviewMiMiRepositoryMock;

    @Mock
    private ReviewMiMiService reviewMiMiServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReviewMiMiMockMvc;

    private ReviewMiMi reviewMiMi;

    private ReviewMiMi insertedReviewMiMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReviewMiMi createEntity() {
        return new ReviewMiMi().rating(DEFAULT_RATING).comment(DEFAULT_COMMENT).reviewDate(DEFAULT_REVIEW_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReviewMiMi createUpdatedEntity() {
        return new ReviewMiMi().rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);
    }

    @BeforeEach
    public void initTest() {
        reviewMiMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedReviewMiMi != null) {
            reviewMiMiRepository.delete(insertedReviewMiMi);
            insertedReviewMiMi = null;
        }
    }

    @Test
    @Transactional
    void createReviewMiMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReviewMiMi
        var returnedReviewMiMi = om.readValue(
            restReviewMiMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewMiMi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReviewMiMi.class
        );

        // Validate the ReviewMiMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertReviewMiMiUpdatableFieldsEquals(returnedReviewMiMi, getPersistedReviewMiMi(returnedReviewMiMi));

        insertedReviewMiMi = returnedReviewMiMi;
    }

    @Test
    @Transactional
    void createReviewMiMiWithExistingId() throws Exception {
        // Create the ReviewMiMi with an existing ID
        reviewMiMi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReviewMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewMiMi)))
            .andExpect(status().isBadRequest());

        // Validate the ReviewMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRatingIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reviewMiMi.setRating(null);

        // Create the ReviewMiMi, which fails.

        restReviewMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewMiMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReviewDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reviewMiMi.setReviewDate(null);

        // Create the ReviewMiMi, which fails.

        restReviewMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewMiMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReviewMiMis() throws Exception {
        // Initialize the database
        insertedReviewMiMi = reviewMiMiRepository.saveAndFlush(reviewMiMi);

        // Get all the reviewMiMiList
        restReviewMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reviewMiMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReviewMiMisWithEagerRelationshipsIsEnabled() throws Exception {
        when(reviewMiMiServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReviewMiMiMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(reviewMiMiServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReviewMiMisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(reviewMiMiServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReviewMiMiMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(reviewMiMiRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getReviewMiMi() throws Exception {
        // Initialize the database
        insertedReviewMiMi = reviewMiMiRepository.saveAndFlush(reviewMiMi);

        // Get the reviewMiMi
        restReviewMiMiMockMvc
            .perform(get(ENTITY_API_URL_ID, reviewMiMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reviewMiMi.getId().intValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.reviewDate").value(DEFAULT_REVIEW_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingReviewMiMi() throws Exception {
        // Get the reviewMiMi
        restReviewMiMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReviewMiMi() throws Exception {
        // Initialize the database
        insertedReviewMiMi = reviewMiMiRepository.saveAndFlush(reviewMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reviewMiMi
        ReviewMiMi updatedReviewMiMi = reviewMiMiRepository.findById(reviewMiMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReviewMiMi are not directly saved in db
        em.detach(updatedReviewMiMi);
        updatedReviewMiMi.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);

        restReviewMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReviewMiMi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedReviewMiMi))
            )
            .andExpect(status().isOk());

        // Validate the ReviewMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReviewMiMiToMatchAllProperties(updatedReviewMiMi);
    }

    @Test
    @Transactional
    void putNonExistingReviewMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewMiMi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReviewMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reviewMiMi.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewMiMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReviewMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReviewMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewMiMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reviewMiMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReviewMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReviewMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewMiMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewMiMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewMiMi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReviewMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReviewMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedReviewMiMi = reviewMiMiRepository.saveAndFlush(reviewMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reviewMiMi using partial update
        ReviewMiMi partialUpdatedReviewMiMi = new ReviewMiMi();
        partialUpdatedReviewMiMi.setId(reviewMiMi.getId());

        partialUpdatedReviewMiMi.rating(UPDATED_RATING).reviewDate(UPDATED_REVIEW_DATE);

        restReviewMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReviewMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReviewMiMi))
            )
            .andExpect(status().isOk());

        // Validate the ReviewMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReviewMiMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReviewMiMi, reviewMiMi),
            getPersistedReviewMiMi(reviewMiMi)
        );
    }

    @Test
    @Transactional
    void fullUpdateReviewMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedReviewMiMi = reviewMiMiRepository.saveAndFlush(reviewMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reviewMiMi using partial update
        ReviewMiMi partialUpdatedReviewMiMi = new ReviewMiMi();
        partialUpdatedReviewMiMi.setId(reviewMiMi.getId());

        partialUpdatedReviewMiMi.rating(UPDATED_RATING).comment(UPDATED_COMMENT).reviewDate(UPDATED_REVIEW_DATE);

        restReviewMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReviewMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReviewMiMi))
            )
            .andExpect(status().isOk());

        // Validate the ReviewMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReviewMiMiUpdatableFieldsEquals(partialUpdatedReviewMiMi, getPersistedReviewMiMi(partialUpdatedReviewMiMi));
    }

    @Test
    @Transactional
    void patchNonExistingReviewMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewMiMi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReviewMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reviewMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reviewMiMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReviewMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReviewMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewMiMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reviewMiMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReviewMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReviewMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewMiMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewMiMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reviewMiMi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReviewMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReviewMiMi() throws Exception {
        // Initialize the database
        insertedReviewMiMi = reviewMiMiRepository.saveAndFlush(reviewMiMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reviewMiMi
        restReviewMiMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, reviewMiMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reviewMiMiRepository.count();
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

    protected ReviewMiMi getPersistedReviewMiMi(ReviewMiMi reviewMiMi) {
        return reviewMiMiRepository.findById(reviewMiMi.getId()).orElseThrow();
    }

    protected void assertPersistedReviewMiMiToMatchAllProperties(ReviewMiMi expectedReviewMiMi) {
        assertReviewMiMiAllPropertiesEquals(expectedReviewMiMi, getPersistedReviewMiMi(expectedReviewMiMi));
    }

    protected void assertPersistedReviewMiMiToMatchUpdatableProperties(ReviewMiMi expectedReviewMiMi) {
        assertReviewMiMiAllUpdatablePropertiesEquals(expectedReviewMiMi, getPersistedReviewMiMi(expectedReviewMiMi));
    }
}
