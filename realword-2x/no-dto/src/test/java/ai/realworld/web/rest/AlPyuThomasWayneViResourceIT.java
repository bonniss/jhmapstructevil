package ai.realworld.web.rest;

import static ai.realworld.domain.AlPyuThomasWayneViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlPyuJokerVi;
import ai.realworld.domain.AlPyuThomasWayneVi;
import ai.realworld.repository.AlPyuThomasWayneViRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AlPyuThomasWayneViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlPyuThomasWayneViResourceIT {

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;
    private static final Integer SMALLER_RATING = 1 - 1;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/al-pyu-thomas-wayne-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlPyuThomasWayneViRepository alPyuThomasWayneViRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlPyuThomasWayneViMockMvc;

    private AlPyuThomasWayneVi alPyuThomasWayneVi;

    private AlPyuThomasWayneVi insertedAlPyuThomasWayneVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPyuThomasWayneVi createEntity() {
        return new AlPyuThomasWayneVi().rating(DEFAULT_RATING).comment(DEFAULT_COMMENT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPyuThomasWayneVi createUpdatedEntity() {
        return new AlPyuThomasWayneVi().rating(UPDATED_RATING).comment(UPDATED_COMMENT);
    }

    @BeforeEach
    public void initTest() {
        alPyuThomasWayneVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlPyuThomasWayneVi != null) {
            alPyuThomasWayneViRepository.delete(insertedAlPyuThomasWayneVi);
            insertedAlPyuThomasWayneVi = null;
        }
    }

    @Test
    @Transactional
    void createAlPyuThomasWayneVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlPyuThomasWayneVi
        var returnedAlPyuThomasWayneVi = om.readValue(
            restAlPyuThomasWayneViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPyuThomasWayneVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlPyuThomasWayneVi.class
        );

        // Validate the AlPyuThomasWayneVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlPyuThomasWayneViUpdatableFieldsEquals(
            returnedAlPyuThomasWayneVi,
            getPersistedAlPyuThomasWayneVi(returnedAlPyuThomasWayneVi)
        );

        insertedAlPyuThomasWayneVi = returnedAlPyuThomasWayneVi;
    }

    @Test
    @Transactional
    void createAlPyuThomasWayneViWithExistingId() throws Exception {
        // Create the AlPyuThomasWayneVi with an existing ID
        alPyuThomasWayneVi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlPyuThomasWayneViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPyuThomasWayneVi)))
            .andExpect(status().isBadRequest());

        // Validate the AlPyuThomasWayneVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAlPyuThomasWayneVis() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayneVi = alPyuThomasWayneViRepository.saveAndFlush(alPyuThomasWayneVi);

        // Get all the alPyuThomasWayneViList
        restAlPyuThomasWayneViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPyuThomasWayneVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)));
    }

    @Test
    @Transactional
    void getAlPyuThomasWayneVi() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayneVi = alPyuThomasWayneViRepository.saveAndFlush(alPyuThomasWayneVi);

        // Get the alPyuThomasWayneVi
        restAlPyuThomasWayneViMockMvc
            .perform(get(ENTITY_API_URL_ID, alPyuThomasWayneVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alPyuThomasWayneVi.getId().intValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT));
    }

    @Test
    @Transactional
    void getAlPyuThomasWayneVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayneVi = alPyuThomasWayneViRepository.saveAndFlush(alPyuThomasWayneVi);

        Long id = alPyuThomasWayneVi.getId();

        defaultAlPyuThomasWayneViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlPyuThomasWayneViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlPyuThomasWayneViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlPyuThomasWayneVisByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayneVi = alPyuThomasWayneViRepository.saveAndFlush(alPyuThomasWayneVi);

        // Get all the alPyuThomasWayneViList where rating equals to
        defaultAlPyuThomasWayneViFiltering("rating.equals=" + DEFAULT_RATING, "rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllAlPyuThomasWayneVisByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayneVi = alPyuThomasWayneViRepository.saveAndFlush(alPyuThomasWayneVi);

        // Get all the alPyuThomasWayneViList where rating in
        defaultAlPyuThomasWayneViFiltering("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING, "rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllAlPyuThomasWayneVisByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayneVi = alPyuThomasWayneViRepository.saveAndFlush(alPyuThomasWayneVi);

        // Get all the alPyuThomasWayneViList where rating is not null
        defaultAlPyuThomasWayneViFiltering("rating.specified=true", "rating.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuThomasWayneVisByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayneVi = alPyuThomasWayneViRepository.saveAndFlush(alPyuThomasWayneVi);

        // Get all the alPyuThomasWayneViList where rating is greater than or equal to
        defaultAlPyuThomasWayneViFiltering("rating.greaterThanOrEqual=" + DEFAULT_RATING, "rating.greaterThanOrEqual=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllAlPyuThomasWayneVisByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayneVi = alPyuThomasWayneViRepository.saveAndFlush(alPyuThomasWayneVi);

        // Get all the alPyuThomasWayneViList where rating is less than or equal to
        defaultAlPyuThomasWayneViFiltering("rating.lessThanOrEqual=" + DEFAULT_RATING, "rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllAlPyuThomasWayneVisByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayneVi = alPyuThomasWayneViRepository.saveAndFlush(alPyuThomasWayneVi);

        // Get all the alPyuThomasWayneViList where rating is less than
        defaultAlPyuThomasWayneViFiltering("rating.lessThan=" + UPDATED_RATING, "rating.lessThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllAlPyuThomasWayneVisByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayneVi = alPyuThomasWayneViRepository.saveAndFlush(alPyuThomasWayneVi);

        // Get all the alPyuThomasWayneViList where rating is greater than
        defaultAlPyuThomasWayneViFiltering("rating.greaterThan=" + SMALLER_RATING, "rating.greaterThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllAlPyuThomasWayneVisByCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayneVi = alPyuThomasWayneViRepository.saveAndFlush(alPyuThomasWayneVi);

        // Get all the alPyuThomasWayneViList where comment equals to
        defaultAlPyuThomasWayneViFiltering("comment.equals=" + DEFAULT_COMMENT, "comment.equals=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllAlPyuThomasWayneVisByCommentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayneVi = alPyuThomasWayneViRepository.saveAndFlush(alPyuThomasWayneVi);

        // Get all the alPyuThomasWayneViList where comment in
        defaultAlPyuThomasWayneViFiltering("comment.in=" + DEFAULT_COMMENT + "," + UPDATED_COMMENT, "comment.in=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllAlPyuThomasWayneVisByCommentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayneVi = alPyuThomasWayneViRepository.saveAndFlush(alPyuThomasWayneVi);

        // Get all the alPyuThomasWayneViList where comment is not null
        defaultAlPyuThomasWayneViFiltering("comment.specified=true", "comment.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuThomasWayneVisByCommentContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayneVi = alPyuThomasWayneViRepository.saveAndFlush(alPyuThomasWayneVi);

        // Get all the alPyuThomasWayneViList where comment contains
        defaultAlPyuThomasWayneViFiltering("comment.contains=" + DEFAULT_COMMENT, "comment.contains=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllAlPyuThomasWayneVisByCommentNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayneVi = alPyuThomasWayneViRepository.saveAndFlush(alPyuThomasWayneVi);

        // Get all the alPyuThomasWayneViList where comment does not contain
        defaultAlPyuThomasWayneViFiltering("comment.doesNotContain=" + UPDATED_COMMENT, "comment.doesNotContain=" + DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    void getAllAlPyuThomasWayneVisByBookingIsEqualToSomething() throws Exception {
        AlPyuJokerVi booking;
        if (TestUtil.findAll(em, AlPyuJokerVi.class).isEmpty()) {
            alPyuThomasWayneViRepository.saveAndFlush(alPyuThomasWayneVi);
            booking = AlPyuJokerViResourceIT.createEntity();
        } else {
            booking = TestUtil.findAll(em, AlPyuJokerVi.class).get(0);
        }
        em.persist(booking);
        em.flush();
        alPyuThomasWayneVi.setBooking(booking);
        alPyuThomasWayneViRepository.saveAndFlush(alPyuThomasWayneVi);
        UUID bookingId = booking.getId();
        // Get all the alPyuThomasWayneViList where booking equals to bookingId
        defaultAlPyuThomasWayneViShouldBeFound("bookingId.equals=" + bookingId);

        // Get all the alPyuThomasWayneViList where booking equals to UUID.randomUUID()
        defaultAlPyuThomasWayneViShouldNotBeFound("bookingId.equals=" + UUID.randomUUID());
    }

    private void defaultAlPyuThomasWayneViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlPyuThomasWayneViShouldBeFound(shouldBeFound);
        defaultAlPyuThomasWayneViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlPyuThomasWayneViShouldBeFound(String filter) throws Exception {
        restAlPyuThomasWayneViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPyuThomasWayneVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)));

        // Check, that the count call also returns 1
        restAlPyuThomasWayneViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlPyuThomasWayneViShouldNotBeFound(String filter) throws Exception {
        restAlPyuThomasWayneViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlPyuThomasWayneViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlPyuThomasWayneVi() throws Exception {
        // Get the alPyuThomasWayneVi
        restAlPyuThomasWayneViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlPyuThomasWayneVi() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayneVi = alPyuThomasWayneViRepository.saveAndFlush(alPyuThomasWayneVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPyuThomasWayneVi
        AlPyuThomasWayneVi updatedAlPyuThomasWayneVi = alPyuThomasWayneViRepository.findById(alPyuThomasWayneVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlPyuThomasWayneVi are not directly saved in db
        em.detach(updatedAlPyuThomasWayneVi);
        updatedAlPyuThomasWayneVi.rating(UPDATED_RATING).comment(UPDATED_COMMENT);

        restAlPyuThomasWayneViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlPyuThomasWayneVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlPyuThomasWayneVi))
            )
            .andExpect(status().isOk());

        // Validate the AlPyuThomasWayneVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlPyuThomasWayneViToMatchAllProperties(updatedAlPyuThomasWayneVi);
    }

    @Test
    @Transactional
    void putNonExistingAlPyuThomasWayneVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPyuThomasWayneVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPyuThomasWayneViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alPyuThomasWayneVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPyuThomasWayneVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPyuThomasWayneVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlPyuThomasWayneVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPyuThomasWayneVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPyuThomasWayneViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPyuThomasWayneVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPyuThomasWayneVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlPyuThomasWayneVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPyuThomasWayneVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPyuThomasWayneViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPyuThomasWayneVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPyuThomasWayneVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlPyuThomasWayneViWithPatch() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayneVi = alPyuThomasWayneViRepository.saveAndFlush(alPyuThomasWayneVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPyuThomasWayneVi using partial update
        AlPyuThomasWayneVi partialUpdatedAlPyuThomasWayneVi = new AlPyuThomasWayneVi();
        partialUpdatedAlPyuThomasWayneVi.setId(alPyuThomasWayneVi.getId());

        partialUpdatedAlPyuThomasWayneVi.rating(UPDATED_RATING).comment(UPDATED_COMMENT);

        restAlPyuThomasWayneViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPyuThomasWayneVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPyuThomasWayneVi))
            )
            .andExpect(status().isOk());

        // Validate the AlPyuThomasWayneVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPyuThomasWayneViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlPyuThomasWayneVi, alPyuThomasWayneVi),
            getPersistedAlPyuThomasWayneVi(alPyuThomasWayneVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlPyuThomasWayneViWithPatch() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayneVi = alPyuThomasWayneViRepository.saveAndFlush(alPyuThomasWayneVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPyuThomasWayneVi using partial update
        AlPyuThomasWayneVi partialUpdatedAlPyuThomasWayneVi = new AlPyuThomasWayneVi();
        partialUpdatedAlPyuThomasWayneVi.setId(alPyuThomasWayneVi.getId());

        partialUpdatedAlPyuThomasWayneVi.rating(UPDATED_RATING).comment(UPDATED_COMMENT);

        restAlPyuThomasWayneViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPyuThomasWayneVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPyuThomasWayneVi))
            )
            .andExpect(status().isOk());

        // Validate the AlPyuThomasWayneVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPyuThomasWayneViUpdatableFieldsEquals(
            partialUpdatedAlPyuThomasWayneVi,
            getPersistedAlPyuThomasWayneVi(partialUpdatedAlPyuThomasWayneVi)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAlPyuThomasWayneVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPyuThomasWayneVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPyuThomasWayneViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alPyuThomasWayneVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPyuThomasWayneVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPyuThomasWayneVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlPyuThomasWayneVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPyuThomasWayneVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPyuThomasWayneViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPyuThomasWayneVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPyuThomasWayneVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlPyuThomasWayneVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPyuThomasWayneVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPyuThomasWayneViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alPyuThomasWayneVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPyuThomasWayneVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlPyuThomasWayneVi() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayneVi = alPyuThomasWayneViRepository.saveAndFlush(alPyuThomasWayneVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alPyuThomasWayneVi
        restAlPyuThomasWayneViMockMvc
            .perform(delete(ENTITY_API_URL_ID, alPyuThomasWayneVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alPyuThomasWayneViRepository.count();
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

    protected AlPyuThomasWayneVi getPersistedAlPyuThomasWayneVi(AlPyuThomasWayneVi alPyuThomasWayneVi) {
        return alPyuThomasWayneViRepository.findById(alPyuThomasWayneVi.getId()).orElseThrow();
    }

    protected void assertPersistedAlPyuThomasWayneViToMatchAllProperties(AlPyuThomasWayneVi expectedAlPyuThomasWayneVi) {
        assertAlPyuThomasWayneViAllPropertiesEquals(expectedAlPyuThomasWayneVi, getPersistedAlPyuThomasWayneVi(expectedAlPyuThomasWayneVi));
    }

    protected void assertPersistedAlPyuThomasWayneViToMatchUpdatableProperties(AlPyuThomasWayneVi expectedAlPyuThomasWayneVi) {
        assertAlPyuThomasWayneViAllUpdatablePropertiesEquals(
            expectedAlPyuThomasWayneVi,
            getPersistedAlPyuThomasWayneVi(expectedAlPyuThomasWayneVi)
        );
    }
}
