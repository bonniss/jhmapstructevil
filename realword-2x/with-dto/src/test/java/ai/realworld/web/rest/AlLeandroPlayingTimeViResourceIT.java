package ai.realworld.web.rest;

import static ai.realworld.domain.AlLeandroPlayingTimeViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlDesire;
import ai.realworld.domain.AlLeandro;
import ai.realworld.domain.AlLeandroPlayingTimeVi;
import ai.realworld.domain.AlPacino;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.enumeration.MaBooDragonBall;
import ai.realworld.repository.AlLeandroPlayingTimeViRepository;
import ai.realworld.service.dto.AlLeandroPlayingTimeViDTO;
import ai.realworld.service.mapper.AlLeandroPlayingTimeViMapper;
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
 * Integration tests for the {@link AlLeandroPlayingTimeViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlLeandroPlayingTimeViResourceIT {

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

    private static final String ENTITY_API_URL = "/api/al-leandro-playing-time-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlLeandroPlayingTimeViRepository alLeandroPlayingTimeViRepository;

    @Autowired
    private AlLeandroPlayingTimeViMapper alLeandroPlayingTimeViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlLeandroPlayingTimeViMockMvc;

    private AlLeandroPlayingTimeVi alLeandroPlayingTimeVi;

    private AlLeandroPlayingTimeVi insertedAlLeandroPlayingTimeVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlLeandroPlayingTimeVi createEntity() {
        return new AlLeandroPlayingTimeVi()
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
    public static AlLeandroPlayingTimeVi createUpdatedEntity() {
        return new AlLeandroPlayingTimeVi()
            .status(UPDATED_STATUS)
            .wonDate(UPDATED_WON_DATE)
            .sentAwardToPlayerAt(UPDATED_SENT_AWARD_TO_PLAYER_AT)
            .sentAwardToPlayerBy(UPDATED_SENT_AWARD_TO_PLAYER_BY)
            .playerReceivedTheAwardAt(UPDATED_PLAYER_RECEIVED_THE_AWARD_AT)
            .playSourceTime(UPDATED_PLAY_SOURCE_TIME);
    }

    @BeforeEach
    public void initTest() {
        alLeandroPlayingTimeVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlLeandroPlayingTimeVi != null) {
            alLeandroPlayingTimeViRepository.delete(insertedAlLeandroPlayingTimeVi);
            insertedAlLeandroPlayingTimeVi = null;
        }
    }

    @Test
    @Transactional
    void createAlLeandroPlayingTimeVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlLeandroPlayingTimeVi
        AlLeandroPlayingTimeViDTO alLeandroPlayingTimeViDTO = alLeandroPlayingTimeViMapper.toDto(alLeandroPlayingTimeVi);
        var returnedAlLeandroPlayingTimeViDTO = om.readValue(
            restAlLeandroPlayingTimeViMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alLeandroPlayingTimeViDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlLeandroPlayingTimeViDTO.class
        );

        // Validate the AlLeandroPlayingTimeVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlLeandroPlayingTimeVi = alLeandroPlayingTimeViMapper.toEntity(returnedAlLeandroPlayingTimeViDTO);
        assertAlLeandroPlayingTimeViUpdatableFieldsEquals(
            returnedAlLeandroPlayingTimeVi,
            getPersistedAlLeandroPlayingTimeVi(returnedAlLeandroPlayingTimeVi)
        );

        insertedAlLeandroPlayingTimeVi = returnedAlLeandroPlayingTimeVi;
    }

    @Test
    @Transactional
    void createAlLeandroPlayingTimeViWithExistingId() throws Exception {
        // Create the AlLeandroPlayingTimeVi with an existing ID
        insertedAlLeandroPlayingTimeVi = alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);
        AlLeandroPlayingTimeViDTO alLeandroPlayingTimeViDTO = alLeandroPlayingTimeViMapper.toDto(alLeandroPlayingTimeVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlLeandroPlayingTimeViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alLeandroPlayingTimeViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlLeandroPlayingTimeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimeVis() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTimeVi = alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);

        // Get all the alLeandroPlayingTimeViList
        restAlLeandroPlayingTimeViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alLeandroPlayingTimeVi.getId().toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].wonDate").value(hasItem(DEFAULT_WON_DATE.toString())))
            .andExpect(jsonPath("$.[*].sentAwardToPlayerAt").value(hasItem(DEFAULT_SENT_AWARD_TO_PLAYER_AT.toString())))
            .andExpect(jsonPath("$.[*].sentAwardToPlayerBy").value(hasItem(DEFAULT_SENT_AWARD_TO_PLAYER_BY)))
            .andExpect(jsonPath("$.[*].playerReceivedTheAwardAt").value(hasItem(DEFAULT_PLAYER_RECEIVED_THE_AWARD_AT.toString())))
            .andExpect(jsonPath("$.[*].playSourceTime").value(hasItem(DEFAULT_PLAY_SOURCE_TIME)));
    }

    @Test
    @Transactional
    void getAlLeandroPlayingTimeVi() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTimeVi = alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);

        // Get the alLeandroPlayingTimeVi
        restAlLeandroPlayingTimeViMockMvc
            .perform(get(ENTITY_API_URL_ID, alLeandroPlayingTimeVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alLeandroPlayingTimeVi.getId().toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.wonDate").value(DEFAULT_WON_DATE.toString()))
            .andExpect(jsonPath("$.sentAwardToPlayerAt").value(DEFAULT_SENT_AWARD_TO_PLAYER_AT.toString()))
            .andExpect(jsonPath("$.sentAwardToPlayerBy").value(DEFAULT_SENT_AWARD_TO_PLAYER_BY))
            .andExpect(jsonPath("$.playerReceivedTheAwardAt").value(DEFAULT_PLAYER_RECEIVED_THE_AWARD_AT.toString()))
            .andExpect(jsonPath("$.playSourceTime").value(DEFAULT_PLAY_SOURCE_TIME));
    }

    @Test
    @Transactional
    void getAlLeandroPlayingTimeVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTimeVi = alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);

        UUID id = alLeandroPlayingTimeVi.getId();

        defaultAlLeandroPlayingTimeViFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimeVisByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTimeVi = alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);

        // Get all the alLeandroPlayingTimeViList where status equals to
        defaultAlLeandroPlayingTimeViFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimeVisByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTimeVi = alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);

        // Get all the alLeandroPlayingTimeViList where status in
        defaultAlLeandroPlayingTimeViFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimeVisByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTimeVi = alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);

        // Get all the alLeandroPlayingTimeViList where status is not null
        defaultAlLeandroPlayingTimeViFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimeVisByWonDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTimeVi = alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);

        // Get all the alLeandroPlayingTimeViList where wonDate equals to
        defaultAlLeandroPlayingTimeViFiltering("wonDate.equals=" + DEFAULT_WON_DATE, "wonDate.equals=" + UPDATED_WON_DATE);
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimeVisByWonDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTimeVi = alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);

        // Get all the alLeandroPlayingTimeViList where wonDate in
        defaultAlLeandroPlayingTimeViFiltering("wonDate.in=" + DEFAULT_WON_DATE + "," + UPDATED_WON_DATE, "wonDate.in=" + UPDATED_WON_DATE);
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimeVisByWonDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTimeVi = alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);

        // Get all the alLeandroPlayingTimeViList where wonDate is not null
        defaultAlLeandroPlayingTimeViFiltering("wonDate.specified=true", "wonDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimeVisBySentAwardToPlayerAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTimeVi = alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);

        // Get all the alLeandroPlayingTimeViList where sentAwardToPlayerAt equals to
        defaultAlLeandroPlayingTimeViFiltering(
            "sentAwardToPlayerAt.equals=" + DEFAULT_SENT_AWARD_TO_PLAYER_AT,
            "sentAwardToPlayerAt.equals=" + UPDATED_SENT_AWARD_TO_PLAYER_AT
        );
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimeVisBySentAwardToPlayerAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTimeVi = alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);

        // Get all the alLeandroPlayingTimeViList where sentAwardToPlayerAt in
        defaultAlLeandroPlayingTimeViFiltering(
            "sentAwardToPlayerAt.in=" + DEFAULT_SENT_AWARD_TO_PLAYER_AT + "," + UPDATED_SENT_AWARD_TO_PLAYER_AT,
            "sentAwardToPlayerAt.in=" + UPDATED_SENT_AWARD_TO_PLAYER_AT
        );
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimeVisBySentAwardToPlayerAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTimeVi = alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);

        // Get all the alLeandroPlayingTimeViList where sentAwardToPlayerAt is not null
        defaultAlLeandroPlayingTimeViFiltering("sentAwardToPlayerAt.specified=true", "sentAwardToPlayerAt.specified=false");
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimeVisBySentAwardToPlayerByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTimeVi = alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);

        // Get all the alLeandroPlayingTimeViList where sentAwardToPlayerBy equals to
        defaultAlLeandroPlayingTimeViFiltering(
            "sentAwardToPlayerBy.equals=" + DEFAULT_SENT_AWARD_TO_PLAYER_BY,
            "sentAwardToPlayerBy.equals=" + UPDATED_SENT_AWARD_TO_PLAYER_BY
        );
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimeVisBySentAwardToPlayerByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTimeVi = alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);

        // Get all the alLeandroPlayingTimeViList where sentAwardToPlayerBy in
        defaultAlLeandroPlayingTimeViFiltering(
            "sentAwardToPlayerBy.in=" + DEFAULT_SENT_AWARD_TO_PLAYER_BY + "," + UPDATED_SENT_AWARD_TO_PLAYER_BY,
            "sentAwardToPlayerBy.in=" + UPDATED_SENT_AWARD_TO_PLAYER_BY
        );
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimeVisBySentAwardToPlayerByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTimeVi = alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);

        // Get all the alLeandroPlayingTimeViList where sentAwardToPlayerBy is not null
        defaultAlLeandroPlayingTimeViFiltering("sentAwardToPlayerBy.specified=true", "sentAwardToPlayerBy.specified=false");
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimeVisBySentAwardToPlayerByContainsSomething() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTimeVi = alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);

        // Get all the alLeandroPlayingTimeViList where sentAwardToPlayerBy contains
        defaultAlLeandroPlayingTimeViFiltering(
            "sentAwardToPlayerBy.contains=" + DEFAULT_SENT_AWARD_TO_PLAYER_BY,
            "sentAwardToPlayerBy.contains=" + UPDATED_SENT_AWARD_TO_PLAYER_BY
        );
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimeVisBySentAwardToPlayerByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTimeVi = alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);

        // Get all the alLeandroPlayingTimeViList where sentAwardToPlayerBy does not contain
        defaultAlLeandroPlayingTimeViFiltering(
            "sentAwardToPlayerBy.doesNotContain=" + UPDATED_SENT_AWARD_TO_PLAYER_BY,
            "sentAwardToPlayerBy.doesNotContain=" + DEFAULT_SENT_AWARD_TO_PLAYER_BY
        );
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimeVisByPlayerReceivedTheAwardAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTimeVi = alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);

        // Get all the alLeandroPlayingTimeViList where playerReceivedTheAwardAt equals to
        defaultAlLeandroPlayingTimeViFiltering(
            "playerReceivedTheAwardAt.equals=" + DEFAULT_PLAYER_RECEIVED_THE_AWARD_AT,
            "playerReceivedTheAwardAt.equals=" + UPDATED_PLAYER_RECEIVED_THE_AWARD_AT
        );
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimeVisByPlayerReceivedTheAwardAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTimeVi = alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);

        // Get all the alLeandroPlayingTimeViList where playerReceivedTheAwardAt in
        defaultAlLeandroPlayingTimeViFiltering(
            "playerReceivedTheAwardAt.in=" + DEFAULT_PLAYER_RECEIVED_THE_AWARD_AT + "," + UPDATED_PLAYER_RECEIVED_THE_AWARD_AT,
            "playerReceivedTheAwardAt.in=" + UPDATED_PLAYER_RECEIVED_THE_AWARD_AT
        );
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimeVisByPlayerReceivedTheAwardAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTimeVi = alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);

        // Get all the alLeandroPlayingTimeViList where playerReceivedTheAwardAt is not null
        defaultAlLeandroPlayingTimeViFiltering("playerReceivedTheAwardAt.specified=true", "playerReceivedTheAwardAt.specified=false");
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimeVisByPlaySourceTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTimeVi = alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);

        // Get all the alLeandroPlayingTimeViList where playSourceTime equals to
        defaultAlLeandroPlayingTimeViFiltering(
            "playSourceTime.equals=" + DEFAULT_PLAY_SOURCE_TIME,
            "playSourceTime.equals=" + UPDATED_PLAY_SOURCE_TIME
        );
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimeVisByPlaySourceTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTimeVi = alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);

        // Get all the alLeandroPlayingTimeViList where playSourceTime in
        defaultAlLeandroPlayingTimeViFiltering(
            "playSourceTime.in=" + DEFAULT_PLAY_SOURCE_TIME + "," + UPDATED_PLAY_SOURCE_TIME,
            "playSourceTime.in=" + UPDATED_PLAY_SOURCE_TIME
        );
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimeVisByPlaySourceTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTimeVi = alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);

        // Get all the alLeandroPlayingTimeViList where playSourceTime is not null
        defaultAlLeandroPlayingTimeViFiltering("playSourceTime.specified=true", "playSourceTime.specified=false");
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimeVisByPlaySourceTimeContainsSomething() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTimeVi = alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);

        // Get all the alLeandroPlayingTimeViList where playSourceTime contains
        defaultAlLeandroPlayingTimeViFiltering(
            "playSourceTime.contains=" + DEFAULT_PLAY_SOURCE_TIME,
            "playSourceTime.contains=" + UPDATED_PLAY_SOURCE_TIME
        );
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimeVisByPlaySourceTimeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTimeVi = alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);

        // Get all the alLeandroPlayingTimeViList where playSourceTime does not contain
        defaultAlLeandroPlayingTimeViFiltering(
            "playSourceTime.doesNotContain=" + UPDATED_PLAY_SOURCE_TIME,
            "playSourceTime.doesNotContain=" + DEFAULT_PLAY_SOURCE_TIME
        );
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimeVisByMaggiIsEqualToSomething() throws Exception {
        AlLeandro maggi;
        if (TestUtil.findAll(em, AlLeandro.class).isEmpty()) {
            alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);
            maggi = AlLeandroResourceIT.createEntity();
        } else {
            maggi = TestUtil.findAll(em, AlLeandro.class).get(0);
        }
        em.persist(maggi);
        em.flush();
        alLeandroPlayingTimeVi.setMaggi(maggi);
        alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);
        UUID maggiId = maggi.getId();
        // Get all the alLeandroPlayingTimeViList where maggi equals to maggiId
        defaultAlLeandroPlayingTimeViShouldBeFound("maggiId.equals=" + maggiId);

        // Get all the alLeandroPlayingTimeViList where maggi equals to UUID.randomUUID()
        defaultAlLeandroPlayingTimeViShouldNotBeFound("maggiId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimeVisByUserIsEqualToSomething() throws Exception {
        AlPacino user;
        if (TestUtil.findAll(em, AlPacino.class).isEmpty()) {
            alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);
            user = AlPacinoResourceIT.createEntity();
        } else {
            user = TestUtil.findAll(em, AlPacino.class).get(0);
        }
        em.persist(user);
        em.flush();
        alLeandroPlayingTimeVi.setUser(user);
        alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);
        UUID userId = user.getId();
        // Get all the alLeandroPlayingTimeViList where user equals to userId
        defaultAlLeandroPlayingTimeViShouldBeFound("userId.equals=" + userId);

        // Get all the alLeandroPlayingTimeViList where user equals to UUID.randomUUID()
        defaultAlLeandroPlayingTimeViShouldNotBeFound("userId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimeVisByAwardIsEqualToSomething() throws Exception {
        AlDesire award;
        if (TestUtil.findAll(em, AlDesire.class).isEmpty()) {
            alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);
            award = AlDesireResourceIT.createEntity();
        } else {
            award = TestUtil.findAll(em, AlDesire.class).get(0);
        }
        em.persist(award);
        em.flush();
        alLeandroPlayingTimeVi.setAward(award);
        alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);
        UUID awardId = award.getId();
        // Get all the alLeandroPlayingTimeViList where award equals to awardId
        defaultAlLeandroPlayingTimeViShouldBeFound("awardId.equals=" + awardId);

        // Get all the alLeandroPlayingTimeViList where award equals to UUID.randomUUID()
        defaultAlLeandroPlayingTimeViShouldNotBeFound("awardId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlLeandroPlayingTimeVisByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alLeandroPlayingTimeVi.setApplication(application);
        alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);
        UUID applicationId = application.getId();
        // Get all the alLeandroPlayingTimeViList where application equals to applicationId
        defaultAlLeandroPlayingTimeViShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alLeandroPlayingTimeViList where application equals to UUID.randomUUID()
        defaultAlLeandroPlayingTimeViShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlLeandroPlayingTimeViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlLeandroPlayingTimeViShouldBeFound(shouldBeFound);
        defaultAlLeandroPlayingTimeViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlLeandroPlayingTimeViShouldBeFound(String filter) throws Exception {
        restAlLeandroPlayingTimeViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alLeandroPlayingTimeVi.getId().toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].wonDate").value(hasItem(DEFAULT_WON_DATE.toString())))
            .andExpect(jsonPath("$.[*].sentAwardToPlayerAt").value(hasItem(DEFAULT_SENT_AWARD_TO_PLAYER_AT.toString())))
            .andExpect(jsonPath("$.[*].sentAwardToPlayerBy").value(hasItem(DEFAULT_SENT_AWARD_TO_PLAYER_BY)))
            .andExpect(jsonPath("$.[*].playerReceivedTheAwardAt").value(hasItem(DEFAULT_PLAYER_RECEIVED_THE_AWARD_AT.toString())))
            .andExpect(jsonPath("$.[*].playSourceTime").value(hasItem(DEFAULT_PLAY_SOURCE_TIME)));

        // Check, that the count call also returns 1
        restAlLeandroPlayingTimeViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlLeandroPlayingTimeViShouldNotBeFound(String filter) throws Exception {
        restAlLeandroPlayingTimeViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlLeandroPlayingTimeViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlLeandroPlayingTimeVi() throws Exception {
        // Get the alLeandroPlayingTimeVi
        restAlLeandroPlayingTimeViMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlLeandroPlayingTimeVi() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTimeVi = alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alLeandroPlayingTimeVi
        AlLeandroPlayingTimeVi updatedAlLeandroPlayingTimeVi = alLeandroPlayingTimeViRepository
            .findById(alLeandroPlayingTimeVi.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedAlLeandroPlayingTimeVi are not directly saved in db
        em.detach(updatedAlLeandroPlayingTimeVi);
        updatedAlLeandroPlayingTimeVi
            .status(UPDATED_STATUS)
            .wonDate(UPDATED_WON_DATE)
            .sentAwardToPlayerAt(UPDATED_SENT_AWARD_TO_PLAYER_AT)
            .sentAwardToPlayerBy(UPDATED_SENT_AWARD_TO_PLAYER_BY)
            .playerReceivedTheAwardAt(UPDATED_PLAYER_RECEIVED_THE_AWARD_AT)
            .playSourceTime(UPDATED_PLAY_SOURCE_TIME);
        AlLeandroPlayingTimeViDTO alLeandroPlayingTimeViDTO = alLeandroPlayingTimeViMapper.toDto(updatedAlLeandroPlayingTimeVi);

        restAlLeandroPlayingTimeViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alLeandroPlayingTimeViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alLeandroPlayingTimeViDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlLeandroPlayingTimeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlLeandroPlayingTimeViToMatchAllProperties(updatedAlLeandroPlayingTimeVi);
    }

    @Test
    @Transactional
    void putNonExistingAlLeandroPlayingTimeVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLeandroPlayingTimeVi.setId(UUID.randomUUID());

        // Create the AlLeandroPlayingTimeVi
        AlLeandroPlayingTimeViDTO alLeandroPlayingTimeViDTO = alLeandroPlayingTimeViMapper.toDto(alLeandroPlayingTimeVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlLeandroPlayingTimeViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alLeandroPlayingTimeViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alLeandroPlayingTimeViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlLeandroPlayingTimeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlLeandroPlayingTimeVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLeandroPlayingTimeVi.setId(UUID.randomUUID());

        // Create the AlLeandroPlayingTimeVi
        AlLeandroPlayingTimeViDTO alLeandroPlayingTimeViDTO = alLeandroPlayingTimeViMapper.toDto(alLeandroPlayingTimeVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlLeandroPlayingTimeViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alLeandroPlayingTimeViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlLeandroPlayingTimeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlLeandroPlayingTimeVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLeandroPlayingTimeVi.setId(UUID.randomUUID());

        // Create the AlLeandroPlayingTimeVi
        AlLeandroPlayingTimeViDTO alLeandroPlayingTimeViDTO = alLeandroPlayingTimeViMapper.toDto(alLeandroPlayingTimeVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlLeandroPlayingTimeViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alLeandroPlayingTimeViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlLeandroPlayingTimeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlLeandroPlayingTimeViWithPatch() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTimeVi = alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alLeandroPlayingTimeVi using partial update
        AlLeandroPlayingTimeVi partialUpdatedAlLeandroPlayingTimeVi = new AlLeandroPlayingTimeVi();
        partialUpdatedAlLeandroPlayingTimeVi.setId(alLeandroPlayingTimeVi.getId());

        partialUpdatedAlLeandroPlayingTimeVi
            .wonDate(UPDATED_WON_DATE)
            .sentAwardToPlayerAt(UPDATED_SENT_AWARD_TO_PLAYER_AT)
            .playSourceTime(UPDATED_PLAY_SOURCE_TIME);

        restAlLeandroPlayingTimeViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlLeandroPlayingTimeVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlLeandroPlayingTimeVi))
            )
            .andExpect(status().isOk());

        // Validate the AlLeandroPlayingTimeVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlLeandroPlayingTimeViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlLeandroPlayingTimeVi, alLeandroPlayingTimeVi),
            getPersistedAlLeandroPlayingTimeVi(alLeandroPlayingTimeVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlLeandroPlayingTimeViWithPatch() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTimeVi = alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alLeandroPlayingTimeVi using partial update
        AlLeandroPlayingTimeVi partialUpdatedAlLeandroPlayingTimeVi = new AlLeandroPlayingTimeVi();
        partialUpdatedAlLeandroPlayingTimeVi.setId(alLeandroPlayingTimeVi.getId());

        partialUpdatedAlLeandroPlayingTimeVi
            .status(UPDATED_STATUS)
            .wonDate(UPDATED_WON_DATE)
            .sentAwardToPlayerAt(UPDATED_SENT_AWARD_TO_PLAYER_AT)
            .sentAwardToPlayerBy(UPDATED_SENT_AWARD_TO_PLAYER_BY)
            .playerReceivedTheAwardAt(UPDATED_PLAYER_RECEIVED_THE_AWARD_AT)
            .playSourceTime(UPDATED_PLAY_SOURCE_TIME);

        restAlLeandroPlayingTimeViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlLeandroPlayingTimeVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlLeandroPlayingTimeVi))
            )
            .andExpect(status().isOk());

        // Validate the AlLeandroPlayingTimeVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlLeandroPlayingTimeViUpdatableFieldsEquals(
            partialUpdatedAlLeandroPlayingTimeVi,
            getPersistedAlLeandroPlayingTimeVi(partialUpdatedAlLeandroPlayingTimeVi)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAlLeandroPlayingTimeVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLeandroPlayingTimeVi.setId(UUID.randomUUID());

        // Create the AlLeandroPlayingTimeVi
        AlLeandroPlayingTimeViDTO alLeandroPlayingTimeViDTO = alLeandroPlayingTimeViMapper.toDto(alLeandroPlayingTimeVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlLeandroPlayingTimeViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alLeandroPlayingTimeViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alLeandroPlayingTimeViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlLeandroPlayingTimeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlLeandroPlayingTimeVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLeandroPlayingTimeVi.setId(UUID.randomUUID());

        // Create the AlLeandroPlayingTimeVi
        AlLeandroPlayingTimeViDTO alLeandroPlayingTimeViDTO = alLeandroPlayingTimeViMapper.toDto(alLeandroPlayingTimeVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlLeandroPlayingTimeViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alLeandroPlayingTimeViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlLeandroPlayingTimeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlLeandroPlayingTimeVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLeandroPlayingTimeVi.setId(UUID.randomUUID());

        // Create the AlLeandroPlayingTimeVi
        AlLeandroPlayingTimeViDTO alLeandroPlayingTimeViDTO = alLeandroPlayingTimeViMapper.toDto(alLeandroPlayingTimeVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlLeandroPlayingTimeViMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alLeandroPlayingTimeViDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlLeandroPlayingTimeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlLeandroPlayingTimeVi() throws Exception {
        // Initialize the database
        insertedAlLeandroPlayingTimeVi = alLeandroPlayingTimeViRepository.saveAndFlush(alLeandroPlayingTimeVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alLeandroPlayingTimeVi
        restAlLeandroPlayingTimeViMockMvc
            .perform(delete(ENTITY_API_URL_ID, alLeandroPlayingTimeVi.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alLeandroPlayingTimeViRepository.count();
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

    protected AlLeandroPlayingTimeVi getPersistedAlLeandroPlayingTimeVi(AlLeandroPlayingTimeVi alLeandroPlayingTimeVi) {
        return alLeandroPlayingTimeViRepository.findById(alLeandroPlayingTimeVi.getId()).orElseThrow();
    }

    protected void assertPersistedAlLeandroPlayingTimeViToMatchAllProperties(AlLeandroPlayingTimeVi expectedAlLeandroPlayingTimeVi) {
        assertAlLeandroPlayingTimeViAllPropertiesEquals(
            expectedAlLeandroPlayingTimeVi,
            getPersistedAlLeandroPlayingTimeVi(expectedAlLeandroPlayingTimeVi)
        );
    }

    protected void assertPersistedAlLeandroPlayingTimeViToMatchUpdatableProperties(AlLeandroPlayingTimeVi expectedAlLeandroPlayingTimeVi) {
        assertAlLeandroPlayingTimeViAllUpdatablePropertiesEquals(
            expectedAlLeandroPlayingTimeVi,
            getPersistedAlLeandroPlayingTimeVi(expectedAlLeandroPlayingTimeVi)
        );
    }
}
