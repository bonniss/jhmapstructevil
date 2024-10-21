package ai.realworld.web.rest;

import static ai.realworld.domain.AlPyuThomasWayneAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlPyuJoker;
import ai.realworld.domain.AlPyuThomasWayne;
import ai.realworld.repository.AlPyuThomasWayneRepository;
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
 * Integration tests for the {@link AlPyuThomasWayneResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlPyuThomasWayneResourceIT {

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;
    private static final Integer SMALLER_RATING = 1 - 1;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/al-pyu-thomas-waynes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlPyuThomasWayneRepository alPyuThomasWayneRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlPyuThomasWayneMockMvc;

    private AlPyuThomasWayne alPyuThomasWayne;

    private AlPyuThomasWayne insertedAlPyuThomasWayne;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPyuThomasWayne createEntity() {
        return new AlPyuThomasWayne().rating(DEFAULT_RATING).comment(DEFAULT_COMMENT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPyuThomasWayne createUpdatedEntity() {
        return new AlPyuThomasWayne().rating(UPDATED_RATING).comment(UPDATED_COMMENT);
    }

    @BeforeEach
    public void initTest() {
        alPyuThomasWayne = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlPyuThomasWayne != null) {
            alPyuThomasWayneRepository.delete(insertedAlPyuThomasWayne);
            insertedAlPyuThomasWayne = null;
        }
    }

    @Test
    @Transactional
    void createAlPyuThomasWayne() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlPyuThomasWayne
        var returnedAlPyuThomasWayne = om.readValue(
            restAlPyuThomasWayneMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPyuThomasWayne)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlPyuThomasWayne.class
        );

        // Validate the AlPyuThomasWayne in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlPyuThomasWayneUpdatableFieldsEquals(returnedAlPyuThomasWayne, getPersistedAlPyuThomasWayne(returnedAlPyuThomasWayne));

        insertedAlPyuThomasWayne = returnedAlPyuThomasWayne;
    }

    @Test
    @Transactional
    void createAlPyuThomasWayneWithExistingId() throws Exception {
        // Create the AlPyuThomasWayne with an existing ID
        alPyuThomasWayne.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlPyuThomasWayneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPyuThomasWayne)))
            .andExpect(status().isBadRequest());

        // Validate the AlPyuThomasWayne in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAlPyuThomasWaynes() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayne = alPyuThomasWayneRepository.saveAndFlush(alPyuThomasWayne);

        // Get all the alPyuThomasWayneList
        restAlPyuThomasWayneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPyuThomasWayne.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)));
    }

    @Test
    @Transactional
    void getAlPyuThomasWayne() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayne = alPyuThomasWayneRepository.saveAndFlush(alPyuThomasWayne);

        // Get the alPyuThomasWayne
        restAlPyuThomasWayneMockMvc
            .perform(get(ENTITY_API_URL_ID, alPyuThomasWayne.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alPyuThomasWayne.getId().intValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT));
    }

    @Test
    @Transactional
    void getAlPyuThomasWaynesByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayne = alPyuThomasWayneRepository.saveAndFlush(alPyuThomasWayne);

        Long id = alPyuThomasWayne.getId();

        defaultAlPyuThomasWayneFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlPyuThomasWayneFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlPyuThomasWayneFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlPyuThomasWaynesByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayne = alPyuThomasWayneRepository.saveAndFlush(alPyuThomasWayne);

        // Get all the alPyuThomasWayneList where rating equals to
        defaultAlPyuThomasWayneFiltering("rating.equals=" + DEFAULT_RATING, "rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllAlPyuThomasWaynesByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayne = alPyuThomasWayneRepository.saveAndFlush(alPyuThomasWayne);

        // Get all the alPyuThomasWayneList where rating in
        defaultAlPyuThomasWayneFiltering("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING, "rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllAlPyuThomasWaynesByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayne = alPyuThomasWayneRepository.saveAndFlush(alPyuThomasWayne);

        // Get all the alPyuThomasWayneList where rating is not null
        defaultAlPyuThomasWayneFiltering("rating.specified=true", "rating.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuThomasWaynesByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayne = alPyuThomasWayneRepository.saveAndFlush(alPyuThomasWayne);

        // Get all the alPyuThomasWayneList where rating is greater than or equal to
        defaultAlPyuThomasWayneFiltering("rating.greaterThanOrEqual=" + DEFAULT_RATING, "rating.greaterThanOrEqual=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllAlPyuThomasWaynesByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayne = alPyuThomasWayneRepository.saveAndFlush(alPyuThomasWayne);

        // Get all the alPyuThomasWayneList where rating is less than or equal to
        defaultAlPyuThomasWayneFiltering("rating.lessThanOrEqual=" + DEFAULT_RATING, "rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllAlPyuThomasWaynesByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayne = alPyuThomasWayneRepository.saveAndFlush(alPyuThomasWayne);

        // Get all the alPyuThomasWayneList where rating is less than
        defaultAlPyuThomasWayneFiltering("rating.lessThan=" + UPDATED_RATING, "rating.lessThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllAlPyuThomasWaynesByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayne = alPyuThomasWayneRepository.saveAndFlush(alPyuThomasWayne);

        // Get all the alPyuThomasWayneList where rating is greater than
        defaultAlPyuThomasWayneFiltering("rating.greaterThan=" + SMALLER_RATING, "rating.greaterThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllAlPyuThomasWaynesByCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayne = alPyuThomasWayneRepository.saveAndFlush(alPyuThomasWayne);

        // Get all the alPyuThomasWayneList where comment equals to
        defaultAlPyuThomasWayneFiltering("comment.equals=" + DEFAULT_COMMENT, "comment.equals=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllAlPyuThomasWaynesByCommentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayne = alPyuThomasWayneRepository.saveAndFlush(alPyuThomasWayne);

        // Get all the alPyuThomasWayneList where comment in
        defaultAlPyuThomasWayneFiltering("comment.in=" + DEFAULT_COMMENT + "," + UPDATED_COMMENT, "comment.in=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllAlPyuThomasWaynesByCommentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayne = alPyuThomasWayneRepository.saveAndFlush(alPyuThomasWayne);

        // Get all the alPyuThomasWayneList where comment is not null
        defaultAlPyuThomasWayneFiltering("comment.specified=true", "comment.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuThomasWaynesByCommentContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayne = alPyuThomasWayneRepository.saveAndFlush(alPyuThomasWayne);

        // Get all the alPyuThomasWayneList where comment contains
        defaultAlPyuThomasWayneFiltering("comment.contains=" + DEFAULT_COMMENT, "comment.contains=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllAlPyuThomasWaynesByCommentNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayne = alPyuThomasWayneRepository.saveAndFlush(alPyuThomasWayne);

        // Get all the alPyuThomasWayneList where comment does not contain
        defaultAlPyuThomasWayneFiltering("comment.doesNotContain=" + UPDATED_COMMENT, "comment.doesNotContain=" + DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    void getAllAlPyuThomasWaynesByBookingIsEqualToSomething() throws Exception {
        AlPyuJoker booking;
        if (TestUtil.findAll(em, AlPyuJoker.class).isEmpty()) {
            alPyuThomasWayneRepository.saveAndFlush(alPyuThomasWayne);
            booking = AlPyuJokerResourceIT.createEntity();
        } else {
            booking = TestUtil.findAll(em, AlPyuJoker.class).get(0);
        }
        em.persist(booking);
        em.flush();
        alPyuThomasWayne.setBooking(booking);
        alPyuThomasWayneRepository.saveAndFlush(alPyuThomasWayne);
        UUID bookingId = booking.getId();
        // Get all the alPyuThomasWayneList where booking equals to bookingId
        defaultAlPyuThomasWayneShouldBeFound("bookingId.equals=" + bookingId);

        // Get all the alPyuThomasWayneList where booking equals to UUID.randomUUID()
        defaultAlPyuThomasWayneShouldNotBeFound("bookingId.equals=" + UUID.randomUUID());
    }

    private void defaultAlPyuThomasWayneFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlPyuThomasWayneShouldBeFound(shouldBeFound);
        defaultAlPyuThomasWayneShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlPyuThomasWayneShouldBeFound(String filter) throws Exception {
        restAlPyuThomasWayneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPyuThomasWayne.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)));

        // Check, that the count call also returns 1
        restAlPyuThomasWayneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlPyuThomasWayneShouldNotBeFound(String filter) throws Exception {
        restAlPyuThomasWayneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlPyuThomasWayneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlPyuThomasWayne() throws Exception {
        // Get the alPyuThomasWayne
        restAlPyuThomasWayneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlPyuThomasWayne() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayne = alPyuThomasWayneRepository.saveAndFlush(alPyuThomasWayne);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPyuThomasWayne
        AlPyuThomasWayne updatedAlPyuThomasWayne = alPyuThomasWayneRepository.findById(alPyuThomasWayne.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlPyuThomasWayne are not directly saved in db
        em.detach(updatedAlPyuThomasWayne);
        updatedAlPyuThomasWayne.rating(UPDATED_RATING).comment(UPDATED_COMMENT);

        restAlPyuThomasWayneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlPyuThomasWayne.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlPyuThomasWayne))
            )
            .andExpect(status().isOk());

        // Validate the AlPyuThomasWayne in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlPyuThomasWayneToMatchAllProperties(updatedAlPyuThomasWayne);
    }

    @Test
    @Transactional
    void putNonExistingAlPyuThomasWayne() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPyuThomasWayne.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPyuThomasWayneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alPyuThomasWayne.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPyuThomasWayne))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPyuThomasWayne in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlPyuThomasWayne() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPyuThomasWayne.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPyuThomasWayneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPyuThomasWayne))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPyuThomasWayne in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlPyuThomasWayne() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPyuThomasWayne.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPyuThomasWayneMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPyuThomasWayne)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPyuThomasWayne in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlPyuThomasWayneWithPatch() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayne = alPyuThomasWayneRepository.saveAndFlush(alPyuThomasWayne);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPyuThomasWayne using partial update
        AlPyuThomasWayne partialUpdatedAlPyuThomasWayne = new AlPyuThomasWayne();
        partialUpdatedAlPyuThomasWayne.setId(alPyuThomasWayne.getId());

        partialUpdatedAlPyuThomasWayne.comment(UPDATED_COMMENT);

        restAlPyuThomasWayneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPyuThomasWayne.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPyuThomasWayne))
            )
            .andExpect(status().isOk());

        // Validate the AlPyuThomasWayne in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPyuThomasWayneUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlPyuThomasWayne, alPyuThomasWayne),
            getPersistedAlPyuThomasWayne(alPyuThomasWayne)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlPyuThomasWayneWithPatch() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayne = alPyuThomasWayneRepository.saveAndFlush(alPyuThomasWayne);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPyuThomasWayne using partial update
        AlPyuThomasWayne partialUpdatedAlPyuThomasWayne = new AlPyuThomasWayne();
        partialUpdatedAlPyuThomasWayne.setId(alPyuThomasWayne.getId());

        partialUpdatedAlPyuThomasWayne.rating(UPDATED_RATING).comment(UPDATED_COMMENT);

        restAlPyuThomasWayneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPyuThomasWayne.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPyuThomasWayne))
            )
            .andExpect(status().isOk());

        // Validate the AlPyuThomasWayne in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPyuThomasWayneUpdatableFieldsEquals(
            partialUpdatedAlPyuThomasWayne,
            getPersistedAlPyuThomasWayne(partialUpdatedAlPyuThomasWayne)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAlPyuThomasWayne() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPyuThomasWayne.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPyuThomasWayneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alPyuThomasWayne.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPyuThomasWayne))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPyuThomasWayne in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlPyuThomasWayne() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPyuThomasWayne.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPyuThomasWayneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPyuThomasWayne))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPyuThomasWayne in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlPyuThomasWayne() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPyuThomasWayne.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPyuThomasWayneMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alPyuThomasWayne)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPyuThomasWayne in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlPyuThomasWayne() throws Exception {
        // Initialize the database
        insertedAlPyuThomasWayne = alPyuThomasWayneRepository.saveAndFlush(alPyuThomasWayne);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alPyuThomasWayne
        restAlPyuThomasWayneMockMvc
            .perform(delete(ENTITY_API_URL_ID, alPyuThomasWayne.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alPyuThomasWayneRepository.count();
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

    protected AlPyuThomasWayne getPersistedAlPyuThomasWayne(AlPyuThomasWayne alPyuThomasWayne) {
        return alPyuThomasWayneRepository.findById(alPyuThomasWayne.getId()).orElseThrow();
    }

    protected void assertPersistedAlPyuThomasWayneToMatchAllProperties(AlPyuThomasWayne expectedAlPyuThomasWayne) {
        assertAlPyuThomasWayneAllPropertiesEquals(expectedAlPyuThomasWayne, getPersistedAlPyuThomasWayne(expectedAlPyuThomasWayne));
    }

    protected void assertPersistedAlPyuThomasWayneToMatchUpdatableProperties(AlPyuThomasWayne expectedAlPyuThomasWayne) {
        assertAlPyuThomasWayneAllUpdatablePropertiesEquals(
            expectedAlPyuThomasWayne,
            getPersistedAlPyuThomasWayne(expectedAlPyuThomasWayne)
        );
    }
}
