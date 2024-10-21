package ai.realworld.web.rest;

import static ai.realworld.domain.AlLeandroPlayingTimeAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlDesire;
import ai.realworld.domain.AlLeandro;
import ai.realworld.domain.AlLeandroPlayingTime;
import ai.realworld.domain.AlPacino;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.enumeration.MaBooDragonBall;
import ai.realworld.repository.AlLeandroPlayingTimeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
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
 * Integration tests for the {@link AlLeandroPlayingTimeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlLeandroPlayingTimeResourceIT {

    private static final MaBooDragonBall DEFAULT_STATUS = MaBooDragonBall.READY_TO_PLAY;
    private static final MaBooDragonBall UPDATED_STATUS = MaBooDragonBall.PLAYING;

    private static final Instant DEFAULT_WON_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_WON_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_SENT_AWARD_TO_PLAYER_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SENT_AWARD_TO_PLAYER_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_SENT_AWARD_TO_PLAYER_BY = "AAAAAAAAAA";
    private static final String UPDATED_SENT_AWARD_TO_PLAYER_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_PLAYER_RECEIVED_THE_AWARD_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PLAYER_RECEIVED_THE_AWARD_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_PLAY_SOURCE_TIME = "AAAAAAAAAA";
    private static final String UPDATED_PLAY_SOURCE_TIME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/al-leandro-playing-times";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlLeandroPlayingTimeRepository alLeandroPlayingTimeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlLeandroPlayingTimeMockMvc;

    private AlLeandroPlayingTime alLeandroPlayingTime;

    private AlLeandroPlayingTime insertedAlLeandroPlayingTime;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlLeandroPlayingTime createEntity() {
        return new AlLeandroPlayingTime()
            .status(DEFAULT_STATUS)
            .wonDate(DEFAULT_WON_DATE)
            .sentAwardToPlayerAt(DEFAULT_SENT_AWARD_TO_PLAYER_AT)
            .sentAwardToPlayerBy(DEFAULT_SENT_AWARD_TO_PLAYER_BY)
            .playerReceivedTheAwardAt(DEFAULT_PLAYER_RECEIVED_THE_AWARD_AT)
            .playSourceTime(DEFAULT_PLAY_SOURCE_TIME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlLeandroPlayingTime createUpdatedEntity() {
        return new AlLeandroPlayingTime()
            .status(UPDATED_STATUS)
            .wonDate(UPDATED_WON_DATE)
            .sentAwardToPlayerAt(UPDATED_SENT_AWARD_TO_PLAYER_AT)
            .sentAwardToPlayerBy(UPDATED_SENT_AWARD_TO_PLAYER_BY)
            .playerReceivedTheAwardAt(UPDATED_PLAYER_RECEIVED_THE_AWARD_AT)
            .playSourceTime(UPDATED_PLAY_SOURCE_TIME);
    }

    @BeforeEach
    public void initTest() {
        alLeandroPlayingTime = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlLeandroPlayingTime != null) {
            alLeandroPlayingTimeRepository.delete(insertedAlLeandroPlayingTime);
            insertedAlLeandroPlayingTime = null;
        }
    }

    @Test
    @Transactional
    void createAlLeandroPlayingTime() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlLeandroPlayingTime
        var returnedAlLeandroPlayingTime = om.readValue(
            restAlLeandroPlayingTimeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alLeandroPlayingTime)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlLeandroPlayingTime.class
        );

        // Validate the AlLeandroPlayingTime in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlLeandroPlayingTimeUpdatableFieldsEquals(
            returnedAlLeandroPlayingTime,
            getPersistedAlLeandroPlayingTime(returnedAlLeandroPlayingTime)
        );

        insertedAlLeandroPlayingTime = returnedAlLeandroPlayingTime;
    }

    @Test
    @Transactional
    void createAlLeandroPlayingTimeWithExistingId() throws Exception {
        // Create the AlLeandroPlayingTime with an existing ID
        insertedAlLeandroPlayingTime = alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlLeandroPlayingTimeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alLeandroPlayingTime)))
            .andExpect(status().isBadRequest());

        // Validate the AlLeandroPlayingTime in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimes() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTime = alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);

        // Get all the alLeandroPlayingTimeList
        restAlLeandroPlayingTimeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alLeandroPlayingTime.getId().toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].wonDate").value(hasItem(DEFAULT_WON_DATE.toString())))
            .andExpect(jsonPath("$.[*].sentAwardToPlayerAt").value(hasItem(DEFAULT_SENT_AWARD_TO_PLAYER_AT.toString())))
            .andExpect(jsonPath("$.[*].sentAwardToPlayerBy").value(hasItem(DEFAULT_SENT_AWARD_TO_PLAYER_BY)))
            .andExpect(jsonPath("$.[*].playerReceivedTheAwardAt").value(hasItem(DEFAULT_PLAYER_RECEIVED_THE_AWARD_AT.toString())))
            .andExpect(jsonPath("$.[*].playSourceTime").value(hasItem(DEFAULT_PLAY_SOURCE_TIME)));
    }

    @Test
    @Transactional
    void getAlLeandroPlayingTime() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTime = alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);

        // Get the alLeandroPlayingTime
        restAlLeandroPlayingTimeMockMvc
            .perform(get(ENTITY_API_URL_ID, alLeandroPlayingTime.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alLeandroPlayingTime.getId().toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.wonDate").value(DEFAULT_WON_DATE.toString()))
            .andExpect(jsonPath("$.sentAwardToPlayerAt").value(DEFAULT_SENT_AWARD_TO_PLAYER_AT.toString()))
            .andExpect(jsonPath("$.sentAwardToPlayerBy").value(DEFAULT_SENT_AWARD_TO_PLAYER_BY))
            .andExpect(jsonPath("$.playerReceivedTheAwardAt").value(DEFAULT_PLAYER_RECEIVED_THE_AWARD_AT.toString()))
            .andExpect(jsonPath("$.playSourceTime").value(DEFAULT_PLAY_SOURCE_TIME));
    }

    @Test
    @Transactional
    void getAlLeandroPlayingTimesByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTime = alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);

        UUID id = alLeandroPlayingTime.getId();

        defaultAlLeandroPlayingTimeFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTime = alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);

        // Get all the alLeandroPlayingTimeList where status equals to
        defaultAlLeandroPlayingTimeFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTime = alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);

        // Get all the alLeandroPlayingTimeList where status in
        defaultAlLeandroPlayingTimeFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTime = alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);

        // Get all the alLeandroPlayingTimeList where status is not null
        defaultAlLeandroPlayingTimeFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimesByWonDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTime = alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);

        // Get all the alLeandroPlayingTimeList where wonDate equals to
        defaultAlLeandroPlayingTimeFiltering("wonDate.equals=" + DEFAULT_WON_DATE, "wonDate.equals=" + UPDATED_WON_DATE);
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimesByWonDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTime = alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);

        // Get all the alLeandroPlayingTimeList where wonDate in
        defaultAlLeandroPlayingTimeFiltering("wonDate.in=" + DEFAULT_WON_DATE + "," + UPDATED_WON_DATE, "wonDate.in=" + UPDATED_WON_DATE);
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimesByWonDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTime = alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);

        // Get all the alLeandroPlayingTimeList where wonDate is not null
        defaultAlLeandroPlayingTimeFiltering("wonDate.specified=true", "wonDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimesBySentAwardToPlayerAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTime = alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);

        // Get all the alLeandroPlayingTimeList where sentAwardToPlayerAt equals to
        defaultAlLeandroPlayingTimeFiltering(
            "sentAwardToPlayerAt.equals=" + DEFAULT_SENT_AWARD_TO_PLAYER_AT,
            "sentAwardToPlayerAt.equals=" + UPDATED_SENT_AWARD_TO_PLAYER_AT
        );
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimesBySentAwardToPlayerAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTime = alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);

        // Get all the alLeandroPlayingTimeList where sentAwardToPlayerAt in
        defaultAlLeandroPlayingTimeFiltering(
            "sentAwardToPlayerAt.in=" + DEFAULT_SENT_AWARD_TO_PLAYER_AT + "," + UPDATED_SENT_AWARD_TO_PLAYER_AT,
            "sentAwardToPlayerAt.in=" + UPDATED_SENT_AWARD_TO_PLAYER_AT
        );
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimesBySentAwardToPlayerAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTime = alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);

        // Get all the alLeandroPlayingTimeList where sentAwardToPlayerAt is not null
        defaultAlLeandroPlayingTimeFiltering("sentAwardToPlayerAt.specified=true", "sentAwardToPlayerAt.specified=false");
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimesBySentAwardToPlayerByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTime = alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);

        // Get all the alLeandroPlayingTimeList where sentAwardToPlayerBy equals to
        defaultAlLeandroPlayingTimeFiltering(
            "sentAwardToPlayerBy.equals=" + DEFAULT_SENT_AWARD_TO_PLAYER_BY,
            "sentAwardToPlayerBy.equals=" + UPDATED_SENT_AWARD_TO_PLAYER_BY
        );
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimesBySentAwardToPlayerByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTime = alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);

        // Get all the alLeandroPlayingTimeList where sentAwardToPlayerBy in
        defaultAlLeandroPlayingTimeFiltering(
            "sentAwardToPlayerBy.in=" + DEFAULT_SENT_AWARD_TO_PLAYER_BY + "," + UPDATED_SENT_AWARD_TO_PLAYER_BY,
            "sentAwardToPlayerBy.in=" + UPDATED_SENT_AWARD_TO_PLAYER_BY
        );
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimesBySentAwardToPlayerByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTime = alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);

        // Get all the alLeandroPlayingTimeList where sentAwardToPlayerBy is not null
        defaultAlLeandroPlayingTimeFiltering("sentAwardToPlayerBy.specified=true", "sentAwardToPlayerBy.specified=false");
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimesBySentAwardToPlayerByContainsSomething() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTime = alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);

        // Get all the alLeandroPlayingTimeList where sentAwardToPlayerBy contains
        defaultAlLeandroPlayingTimeFiltering(
            "sentAwardToPlayerBy.contains=" + DEFAULT_SENT_AWARD_TO_PLAYER_BY,
            "sentAwardToPlayerBy.contains=" + UPDATED_SENT_AWARD_TO_PLAYER_BY
        );
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimesBySentAwardToPlayerByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTime = alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);

        // Get all the alLeandroPlayingTimeList where sentAwardToPlayerBy does not contain
        defaultAlLeandroPlayingTimeFiltering(
            "sentAwardToPlayerBy.doesNotContain=" + UPDATED_SENT_AWARD_TO_PLAYER_BY,
            "sentAwardToPlayerBy.doesNotContain=" + DEFAULT_SENT_AWARD_TO_PLAYER_BY
        );
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimesByPlayerReceivedTheAwardAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTime = alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);

        // Get all the alLeandroPlayingTimeList where playerReceivedTheAwardAt equals to
        defaultAlLeandroPlayingTimeFiltering(
            "playerReceivedTheAwardAt.equals=" + DEFAULT_PLAYER_RECEIVED_THE_AWARD_AT,
            "playerReceivedTheAwardAt.equals=" + UPDATED_PLAYER_RECEIVED_THE_AWARD_AT
        );
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimesByPlayerReceivedTheAwardAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTime = alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);

        // Get all the alLeandroPlayingTimeList where playerReceivedTheAwardAt in
        defaultAlLeandroPlayingTimeFiltering(
            "playerReceivedTheAwardAt.in=" + DEFAULT_PLAYER_RECEIVED_THE_AWARD_AT + "," + UPDATED_PLAYER_RECEIVED_THE_AWARD_AT,
            "playerReceivedTheAwardAt.in=" + UPDATED_PLAYER_RECEIVED_THE_AWARD_AT
        );
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimesByPlayerReceivedTheAwardAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTime = alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);

        // Get all the alLeandroPlayingTimeList where playerReceivedTheAwardAt is not null
        defaultAlLeandroPlayingTimeFiltering("playerReceivedTheAwardAt.specified=true", "playerReceivedTheAwardAt.specified=false");
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimesByPlaySourceTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTime = alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);

        // Get all the alLeandroPlayingTimeList where playSourceTime equals to
        defaultAlLeandroPlayingTimeFiltering(
            "playSourceTime.equals=" + DEFAULT_PLAY_SOURCE_TIME,
            "playSourceTime.equals=" + UPDATED_PLAY_SOURCE_TIME
        );
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimesByPlaySourceTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTime = alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);

        // Get all the alLeandroPlayingTimeList where playSourceTime in
        defaultAlLeandroPlayingTimeFiltering(
            "playSourceTime.in=" + DEFAULT_PLAY_SOURCE_TIME + "," + UPDATED_PLAY_SOURCE_TIME,
            "playSourceTime.in=" + UPDATED_PLAY_SOURCE_TIME
        );
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimesByPlaySourceTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTime = alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);

        // Get all the alLeandroPlayingTimeList where playSourceTime is not null
        defaultAlLeandroPlayingTimeFiltering("playSourceTime.specified=true", "playSourceTime.specified=false");
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimesByPlaySourceTimeContainsSomething() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTime = alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);

        // Get all the alLeandroPlayingTimeList where playSourceTime contains
        defaultAlLeandroPlayingTimeFiltering(
            "playSourceTime.contains=" + DEFAULT_PLAY_SOURCE_TIME,
            "playSourceTime.contains=" + UPDATED_PLAY_SOURCE_TIME
        );
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimesByPlaySourceTimeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTime = alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);

        // Get all the alLeandroPlayingTimeList where playSourceTime does not contain
        defaultAlLeandroPlayingTimeFiltering(
            "playSourceTime.doesNotContain=" + UPDATED_PLAY_SOURCE_TIME,
            "playSourceTime.doesNotContain=" + DEFAULT_PLAY_SOURCE_TIME
        );
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimesByMaggiIsEqualToSomething() throws Exception {
        AlLeandro maggi;
        if (TestUtil.findAll(em, AlLeandro.class).isEmpty()) {
            alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);
            maggi = AlLeandroResourceIT.createEntity();
        } else {
            maggi = TestUtil.findAll(em, AlLeandro.class).get(0);
        }
        em.persist(maggi);
        em.flush();
        alLeandroPlayingTime.setMaggi(maggi);
        alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);
        UUID maggiId = maggi.getId();
        // Get all the alLeandroPlayingTimeList where maggi equals to maggiId
        defaultAlLeandroPlayingTimeShouldBeFound("maggiId.equals=" + maggiId);

        // Get all the alLeandroPlayingTimeList where maggi equals to UUID.randomUUID()
        defaultAlLeandroPlayingTimeShouldNotBeFound("maggiId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimesByUserIsEqualToSomething() throws Exception {
        AlPacino user;
        if (TestUtil.findAll(em, AlPacino.class).isEmpty()) {
            alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);
            user = AlPacinoResourceIT.createEntity();
        } else {
            user = TestUtil.findAll(em, AlPacino.class).get(0);
        }
        em.persist(user);
        em.flush();
        alLeandroPlayingTime.setUser(user);
        alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);
        UUID userId = user.getId();
        // Get all the alLeandroPlayingTimeList where user equals to userId
        defaultAlLeandroPlayingTimeShouldBeFound("userId.equals=" + userId);

        // Get all the alLeandroPlayingTimeList where user equals to UUID.randomUUID()
        defaultAlLeandroPlayingTimeShouldNotBeFound("userId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimesByAwardIsEqualToSomething() throws Exception {
        AlDesire award;
        if (TestUtil.findAll(em, AlDesire.class).isEmpty()) {
            alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);
            award = AlDesireResourceIT.createEntity();
        } else {
            award = TestUtil.findAll(em, AlDesire.class).get(0);
        }
        em.persist(award);
        em.flush();
        alLeandroPlayingTime.setAward(award);
        alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);
        UUID awardId = award.getId();
        // Get all the alLeandroPlayingTimeList where award equals to awardId
        defaultAlLeandroPlayingTimeShouldBeFound("awardId.equals=" + awardId);

        // Get all the alLeandroPlayingTimeList where award equals to UUID.randomUUID()
        defaultAlLeandroPlayingTimeShouldNotBeFound("awardId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimesByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alLeandroPlayingTime.setApplication(application);
        alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);
        UUID applicationId = application.getId();
        // Get all the alLeandroPlayingTimeList where application equals to applicationId
        defaultAlLeandroPlayingTimeShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alLeandroPlayingTimeList where application equals to UUID.randomUUID()
        defaultAlLeandroPlayingTimeShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlLeandroPlayingTimeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlLeandroPlayingTimeShouldBeFound(shouldBeFound);
        defaultAlLeandroPlayingTimeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlLeandroPlayingTimeShouldBeFound(String filter) throws Exception {
        restAlLeandroPlayingTimeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alLeandroPlayingTime.getId().toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].wonDate").value(hasItem(DEFAULT_WON_DATE.toString())))
            .andExpect(jsonPath("$.[*].sentAwardToPlayerAt").value(hasItem(DEFAULT_SENT_AWARD_TO_PLAYER_AT.toString())))
            .andExpect(jsonPath("$.[*].sentAwardToPlayerBy").value(hasItem(DEFAULT_SENT_AWARD_TO_PLAYER_BY)))
            .andExpect(jsonPath("$.[*].playerReceivedTheAwardAt").value(hasItem(DEFAULT_PLAYER_RECEIVED_THE_AWARD_AT.toString())))
            .andExpect(jsonPath("$.[*].playSourceTime").value(hasItem(DEFAULT_PLAY_SOURCE_TIME)));

        // Check, that the count call also returns 1
        restAlLeandroPlayingTimeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlLeandroPlayingTimeShouldNotBeFound(String filter) throws Exception {
        restAlLeandroPlayingTimeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlLeandroPlayingTimeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlLeandroPlayingTime() throws Exception {
        // Get the alLeandroPlayingTime
        restAlLeandroPlayingTimeMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlLeandroPlayingTime() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTime = alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alLeandroPlayingTime
        AlLeandroPlayingTime updatedAlLeandroPlayingTime = alLeandroPlayingTimeRepository
            .findById(alLeandroPlayingTime.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedAlLeandroPlayingTime are not directly saved in db
        em.detach(updatedAlLeandroPlayingTime);
        updatedAlLeandroPlayingTime
            .status(UPDATED_STATUS)
            .wonDate(UPDATED_WON_DATE)
            .sentAwardToPlayerAt(UPDATED_SENT_AWARD_TO_PLAYER_AT)
            .sentAwardToPlayerBy(UPDATED_SENT_AWARD_TO_PLAYER_BY)
            .playerReceivedTheAwardAt(UPDATED_PLAYER_RECEIVED_THE_AWARD_AT)
            .playSourceTime(UPDATED_PLAY_SOURCE_TIME);

        restAlLeandroPlayingTimeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlLeandroPlayingTime.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlLeandroPlayingTime))
            )
            .andExpect(status().isOk());

        // Validate the AlLeandroPlayingTime in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlLeandroPlayingTimeToMatchAllProperties(updatedAlLeandroPlayingTime);
    }

    @Test
    @Transactional
    void putNonExistingAlLeandroPlayingTime() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLeandroPlayingTime.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlLeandroPlayingTimeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alLeandroPlayingTime.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alLeandroPlayingTime))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlLeandroPlayingTime in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlLeandroPlayingTime() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLeandroPlayingTime.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlLeandroPlayingTimeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alLeandroPlayingTime))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlLeandroPlayingTime in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlLeandroPlayingTime() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLeandroPlayingTime.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlLeandroPlayingTimeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alLeandroPlayingTime)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlLeandroPlayingTime in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlLeandroPlayingTimeWithPatch() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTime = alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alLeandroPlayingTime using partial update
        AlLeandroPlayingTime partialUpdatedAlLeandroPlayingTime = new AlLeandroPlayingTime();
        partialUpdatedAlLeandroPlayingTime.setId(alLeandroPlayingTime.getId());

        partialUpdatedAlLeandroPlayingTime
            .wonDate(UPDATED_WON_DATE)
            .sentAwardToPlayerBy(UPDATED_SENT_AWARD_TO_PLAYER_BY)
            .playerReceivedTheAwardAt(UPDATED_PLAYER_RECEIVED_THE_AWARD_AT)
            .playSourceTime(UPDATED_PLAY_SOURCE_TIME);

        restAlLeandroPlayingTimeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlLeandroPlayingTime.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlLeandroPlayingTime))
            )
            .andExpect(status().isOk());

        // Validate the AlLeandroPlayingTime in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlLeandroPlayingTimeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlLeandroPlayingTime, alLeandroPlayingTime),
            getPersistedAlLeandroPlayingTime(alLeandroPlayingTime)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlLeandroPlayingTimeWithPatch() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTime = alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alLeandroPlayingTime using partial update
        AlLeandroPlayingTime partialUpdatedAlLeandroPlayingTime = new AlLeandroPlayingTime();
        partialUpdatedAlLeandroPlayingTime.setId(alLeandroPlayingTime.getId());

        partialUpdatedAlLeandroPlayingTime
            .status(UPDATED_STATUS)
            .wonDate(UPDATED_WON_DATE)
            .sentAwardToPlayerAt(UPDATED_SENT_AWARD_TO_PLAYER_AT)
            .sentAwardToPlayerBy(UPDATED_SENT_AWARD_TO_PLAYER_BY)
            .playerReceivedTheAwardAt(UPDATED_PLAYER_RECEIVED_THE_AWARD_AT)
            .playSourceTime(UPDATED_PLAY_SOURCE_TIME);

        restAlLeandroPlayingTimeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlLeandroPlayingTime.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlLeandroPlayingTime))
            )
            .andExpect(status().isOk());

        // Validate the AlLeandroPlayingTime in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlLeandroPlayingTimeUpdatableFieldsEquals(
            partialUpdatedAlLeandroPlayingTime,
            getPersistedAlLeandroPlayingTime(partialUpdatedAlLeandroPlayingTime)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAlLeandroPlayingTime() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLeandroPlayingTime.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlLeandroPlayingTimeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alLeandroPlayingTime.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alLeandroPlayingTime))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlLeandroPlayingTime in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlLeandroPlayingTime() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLeandroPlayingTime.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlLeandroPlayingTimeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alLeandroPlayingTime))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlLeandroPlayingTime in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlLeandroPlayingTime() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLeandroPlayingTime.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlLeandroPlayingTimeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alLeandroPlayingTime)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlLeandroPlayingTime in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlLeandroPlayingTime() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTime = alLeandroPlayingTimeRepository.saveAndFlush(alLeandroPlayingTime);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alLeandroPlayingTime
        restAlLeandroPlayingTimeMockMvc
            .perform(delete(ENTITY_API_URL_ID, alLeandroPlayingTime.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alLeandroPlayingTimeRepository.count();
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

    protected AlLeandroPlayingTime getPersistedAlLeandroPlayingTime(AlLeandroPlayingTime alLeandroPlayingTime) {
        return alLeandroPlayingTimeRepository.findById(alLeandroPlayingTime.getId()).orElseThrow();
    }

    protected void assertPersistedAlLeandroPlayingTimeToMatchAllProperties(AlLeandroPlayingTime expectedAlLeandroPlayingTime) {
        assertAlLeandroPlayingTimeAllPropertiesEquals(
            expectedAlLeandroPlayingTime,
            getPersistedAlLeandroPlayingTime(expectedAlLeandroPlayingTime)
        );
    }

    protected void assertPersistedAlLeandroPlayingTimeToMatchUpdatableProperties(AlLeandroPlayingTime expectedAlLeandroPlayingTime) {
        assertAlLeandroPlayingTimeAllUpdatablePropertiesEquals(
            expectedAlLeandroPlayingTime,
            getPersistedAlLeandroPlayingTime(expectedAlLeandroPlayingTime)
        );
    }
}
