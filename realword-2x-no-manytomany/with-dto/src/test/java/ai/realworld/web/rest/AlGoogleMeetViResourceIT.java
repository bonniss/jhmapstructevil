package ai.realworld.web.rest;

import static ai.realworld.domain.AlGoogleMeetViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlAppleVi;
import ai.realworld.domain.AlGoogleMeetVi;
import ai.realworld.domain.AlPacino;
import ai.realworld.domain.EdSheeranVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.repository.AlGoogleMeetViRepository;
import ai.realworld.service.dto.AlGoogleMeetViDTO;
import ai.realworld.service.mapper.AlGoogleMeetViMapper;
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
 * Integration tests for the {@link AlGoogleMeetViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlGoogleMeetViResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER_OF_PARTICIPANTS = 1;
    private static final Integer UPDATED_NUMBER_OF_PARTICIPANTS = 2;
    private static final Integer SMALLER_NUMBER_OF_PARTICIPANTS = 1 - 1;

    private static final Instant DEFAULT_PLANNED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PLANNED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_PLANNED_DURATION_IN_MINUTES = 1D;
    private static final Double UPDATED_PLANNED_DURATION_IN_MINUTES = 2D;
    private static final Double SMALLER_PLANNED_DURATION_IN_MINUTES = 1D - 1D;

    private static final Instant DEFAULT_ACTUAL_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ACTUAL_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_ACTUAL_DURATION_IN_MINUTES = 1D;
    private static final Double UPDATED_ACTUAL_DURATION_IN_MINUTES = 2D;
    private static final Double SMALLER_ACTUAL_DURATION_IN_MINUTES = 1D - 1D;

    private static final String DEFAULT_CONTENT_JASON = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_JASON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/al-google-meet-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlGoogleMeetViRepository alGoogleMeetViRepository;

    @Autowired
    private AlGoogleMeetViMapper alGoogleMeetViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlGoogleMeetViMockMvc;

    private AlGoogleMeetVi alGoogleMeetVi;

    private AlGoogleMeetVi insertedAlGoogleMeetVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlGoogleMeetVi createEntity() {
        return new AlGoogleMeetVi()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .numberOfParticipants(DEFAULT_NUMBER_OF_PARTICIPANTS)
            .plannedDate(DEFAULT_PLANNED_DATE)
            .plannedDurationInMinutes(DEFAULT_PLANNED_DURATION_IN_MINUTES)
            .actualDate(DEFAULT_ACTUAL_DATE)
            .actualDurationInMinutes(DEFAULT_ACTUAL_DURATION_IN_MINUTES)
            .contentJason(DEFAULT_CONTENT_JASON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlGoogleMeetVi createUpdatedEntity() {
        return new AlGoogleMeetVi()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .numberOfParticipants(UPDATED_NUMBER_OF_PARTICIPANTS)
            .plannedDate(UPDATED_PLANNED_DATE)
            .plannedDurationInMinutes(UPDATED_PLANNED_DURATION_IN_MINUTES)
            .actualDate(UPDATED_ACTUAL_DATE)
            .actualDurationInMinutes(UPDATED_ACTUAL_DURATION_IN_MINUTES)
            .contentJason(UPDATED_CONTENT_JASON);
    }

    @BeforeEach
    public void initTest() {
        alGoogleMeetVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlGoogleMeetVi != null) {
            alGoogleMeetViRepository.delete(insertedAlGoogleMeetVi);
            insertedAlGoogleMeetVi = null;
        }
    }

    @Test
    @Transactional
    void createAlGoogleMeetVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlGoogleMeetVi
        AlGoogleMeetViDTO alGoogleMeetViDTO = alGoogleMeetViMapper.toDto(alGoogleMeetVi);
        var returnedAlGoogleMeetViDTO = om.readValue(
            restAlGoogleMeetViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alGoogleMeetViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlGoogleMeetViDTO.class
        );

        // Validate the AlGoogleMeetVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlGoogleMeetVi = alGoogleMeetViMapper.toEntity(returnedAlGoogleMeetViDTO);
        assertAlGoogleMeetViUpdatableFieldsEquals(returnedAlGoogleMeetVi, getPersistedAlGoogleMeetVi(returnedAlGoogleMeetVi));

        insertedAlGoogleMeetVi = returnedAlGoogleMeetVi;
    }

    @Test
    @Transactional
    void createAlGoogleMeetViWithExistingId() throws Exception {
        // Create the AlGoogleMeetVi with an existing ID
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);
        AlGoogleMeetViDTO alGoogleMeetViDTO = alGoogleMeetViMapper.toDto(alGoogleMeetVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlGoogleMeetViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alGoogleMeetViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlGoogleMeetVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alGoogleMeetVi.setTitle(null);

        // Create the AlGoogleMeetVi, which fails.
        AlGoogleMeetViDTO alGoogleMeetViDTO = alGoogleMeetViMapper.toDto(alGoogleMeetVi);

        restAlGoogleMeetViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alGoogleMeetViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVis() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList
        restAlGoogleMeetViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alGoogleMeetVi.getId().toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].numberOfParticipants").value(hasItem(DEFAULT_NUMBER_OF_PARTICIPANTS)))
            .andExpect(jsonPath("$.[*].plannedDate").value(hasItem(DEFAULT_PLANNED_DATE.toString())))
            .andExpect(jsonPath("$.[*].plannedDurationInMinutes").value(hasItem(DEFAULT_PLANNED_DURATION_IN_MINUTES.doubleValue())))
            .andExpect(jsonPath("$.[*].actualDate").value(hasItem(DEFAULT_ACTUAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].actualDurationInMinutes").value(hasItem(DEFAULT_ACTUAL_DURATION_IN_MINUTES.doubleValue())))
            .andExpect(jsonPath("$.[*].contentJason").value(hasItem(DEFAULT_CONTENT_JASON)));
    }

    @Test
    @Transactional
    void getAlGoogleMeetVi() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get the alGoogleMeetVi
        restAlGoogleMeetViMockMvc
            .perform(get(ENTITY_API_URL_ID, alGoogleMeetVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alGoogleMeetVi.getId().toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.numberOfParticipants").value(DEFAULT_NUMBER_OF_PARTICIPANTS))
            .andExpect(jsonPath("$.plannedDate").value(DEFAULT_PLANNED_DATE.toString()))
            .andExpect(jsonPath("$.plannedDurationInMinutes").value(DEFAULT_PLANNED_DURATION_IN_MINUTES.doubleValue()))
            .andExpect(jsonPath("$.actualDate").value(DEFAULT_ACTUAL_DATE.toString()))
            .andExpect(jsonPath("$.actualDurationInMinutes").value(DEFAULT_ACTUAL_DURATION_IN_MINUTES.doubleValue()))
            .andExpect(jsonPath("$.contentJason").value(DEFAULT_CONTENT_JASON));
    }

    @Test
    @Transactional
    void getAlGoogleMeetVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        UUID id = alGoogleMeetVi.getId();

        defaultAlGoogleMeetViFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where title equals to
        defaultAlGoogleMeetViFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where title in
        defaultAlGoogleMeetViFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where title is not null
        defaultAlGoogleMeetViFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where title contains
        defaultAlGoogleMeetViFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where title does not contain
        defaultAlGoogleMeetViFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where description equals to
        defaultAlGoogleMeetViFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where description in
        defaultAlGoogleMeetViFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where description is not null
        defaultAlGoogleMeetViFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where description contains
        defaultAlGoogleMeetViFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where description does not contain
        defaultAlGoogleMeetViFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByNumberOfParticipantsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where numberOfParticipants equals to
        defaultAlGoogleMeetViFiltering(
            "numberOfParticipants.equals=" + DEFAULT_NUMBER_OF_PARTICIPANTS,
            "numberOfParticipants.equals=" + UPDATED_NUMBER_OF_PARTICIPANTS
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByNumberOfParticipantsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where numberOfParticipants in
        defaultAlGoogleMeetViFiltering(
            "numberOfParticipants.in=" + DEFAULT_NUMBER_OF_PARTICIPANTS + "," + UPDATED_NUMBER_OF_PARTICIPANTS,
            "numberOfParticipants.in=" + UPDATED_NUMBER_OF_PARTICIPANTS
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByNumberOfParticipantsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where numberOfParticipants is not null
        defaultAlGoogleMeetViFiltering("numberOfParticipants.specified=true", "numberOfParticipants.specified=false");
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByNumberOfParticipantsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where numberOfParticipants is greater than or equal to
        defaultAlGoogleMeetViFiltering(
            "numberOfParticipants.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_PARTICIPANTS,
            "numberOfParticipants.greaterThanOrEqual=" + UPDATED_NUMBER_OF_PARTICIPANTS
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByNumberOfParticipantsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where numberOfParticipants is less than or equal to
        defaultAlGoogleMeetViFiltering(
            "numberOfParticipants.lessThanOrEqual=" + DEFAULT_NUMBER_OF_PARTICIPANTS,
            "numberOfParticipants.lessThanOrEqual=" + SMALLER_NUMBER_OF_PARTICIPANTS
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByNumberOfParticipantsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where numberOfParticipants is less than
        defaultAlGoogleMeetViFiltering(
            "numberOfParticipants.lessThan=" + UPDATED_NUMBER_OF_PARTICIPANTS,
            "numberOfParticipants.lessThan=" + DEFAULT_NUMBER_OF_PARTICIPANTS
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByNumberOfParticipantsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where numberOfParticipants is greater than
        defaultAlGoogleMeetViFiltering(
            "numberOfParticipants.greaterThan=" + SMALLER_NUMBER_OF_PARTICIPANTS,
            "numberOfParticipants.greaterThan=" + DEFAULT_NUMBER_OF_PARTICIPANTS
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByPlannedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where plannedDate equals to
        defaultAlGoogleMeetViFiltering("plannedDate.equals=" + DEFAULT_PLANNED_DATE, "plannedDate.equals=" + UPDATED_PLANNED_DATE);
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByPlannedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where plannedDate in
        defaultAlGoogleMeetViFiltering(
            "plannedDate.in=" + DEFAULT_PLANNED_DATE + "," + UPDATED_PLANNED_DATE,
            "plannedDate.in=" + UPDATED_PLANNED_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByPlannedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where plannedDate is not null
        defaultAlGoogleMeetViFiltering("plannedDate.specified=true", "plannedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByPlannedDurationInMinutesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where plannedDurationInMinutes equals to
        defaultAlGoogleMeetViFiltering(
            "plannedDurationInMinutes.equals=" + DEFAULT_PLANNED_DURATION_IN_MINUTES,
            "plannedDurationInMinutes.equals=" + UPDATED_PLANNED_DURATION_IN_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByPlannedDurationInMinutesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where plannedDurationInMinutes in
        defaultAlGoogleMeetViFiltering(
            "plannedDurationInMinutes.in=" + DEFAULT_PLANNED_DURATION_IN_MINUTES + "," + UPDATED_PLANNED_DURATION_IN_MINUTES,
            "plannedDurationInMinutes.in=" + UPDATED_PLANNED_DURATION_IN_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByPlannedDurationInMinutesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where plannedDurationInMinutes is not null
        defaultAlGoogleMeetViFiltering("plannedDurationInMinutes.specified=true", "plannedDurationInMinutes.specified=false");
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByPlannedDurationInMinutesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where plannedDurationInMinutes is greater than or equal to
        defaultAlGoogleMeetViFiltering(
            "plannedDurationInMinutes.greaterThanOrEqual=" + DEFAULT_PLANNED_DURATION_IN_MINUTES,
            "plannedDurationInMinutes.greaterThanOrEqual=" + UPDATED_PLANNED_DURATION_IN_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByPlannedDurationInMinutesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where plannedDurationInMinutes is less than or equal to
        defaultAlGoogleMeetViFiltering(
            "plannedDurationInMinutes.lessThanOrEqual=" + DEFAULT_PLANNED_DURATION_IN_MINUTES,
            "plannedDurationInMinutes.lessThanOrEqual=" + SMALLER_PLANNED_DURATION_IN_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByPlannedDurationInMinutesIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where plannedDurationInMinutes is less than
        defaultAlGoogleMeetViFiltering(
            "plannedDurationInMinutes.lessThan=" + UPDATED_PLANNED_DURATION_IN_MINUTES,
            "plannedDurationInMinutes.lessThan=" + DEFAULT_PLANNED_DURATION_IN_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByPlannedDurationInMinutesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where plannedDurationInMinutes is greater than
        defaultAlGoogleMeetViFiltering(
            "plannedDurationInMinutes.greaterThan=" + SMALLER_PLANNED_DURATION_IN_MINUTES,
            "plannedDurationInMinutes.greaterThan=" + DEFAULT_PLANNED_DURATION_IN_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByActualDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where actualDate equals to
        defaultAlGoogleMeetViFiltering("actualDate.equals=" + DEFAULT_ACTUAL_DATE, "actualDate.equals=" + UPDATED_ACTUAL_DATE);
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByActualDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where actualDate in
        defaultAlGoogleMeetViFiltering(
            "actualDate.in=" + DEFAULT_ACTUAL_DATE + "," + UPDATED_ACTUAL_DATE,
            "actualDate.in=" + UPDATED_ACTUAL_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByActualDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where actualDate is not null
        defaultAlGoogleMeetViFiltering("actualDate.specified=true", "actualDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByActualDurationInMinutesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where actualDurationInMinutes equals to
        defaultAlGoogleMeetViFiltering(
            "actualDurationInMinutes.equals=" + DEFAULT_ACTUAL_DURATION_IN_MINUTES,
            "actualDurationInMinutes.equals=" + UPDATED_ACTUAL_DURATION_IN_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByActualDurationInMinutesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where actualDurationInMinutes in
        defaultAlGoogleMeetViFiltering(
            "actualDurationInMinutes.in=" + DEFAULT_ACTUAL_DURATION_IN_MINUTES + "," + UPDATED_ACTUAL_DURATION_IN_MINUTES,
            "actualDurationInMinutes.in=" + UPDATED_ACTUAL_DURATION_IN_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByActualDurationInMinutesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where actualDurationInMinutes is not null
        defaultAlGoogleMeetViFiltering("actualDurationInMinutes.specified=true", "actualDurationInMinutes.specified=false");
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByActualDurationInMinutesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where actualDurationInMinutes is greater than or equal to
        defaultAlGoogleMeetViFiltering(
            "actualDurationInMinutes.greaterThanOrEqual=" + DEFAULT_ACTUAL_DURATION_IN_MINUTES,
            "actualDurationInMinutes.greaterThanOrEqual=" + UPDATED_ACTUAL_DURATION_IN_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByActualDurationInMinutesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where actualDurationInMinutes is less than or equal to
        defaultAlGoogleMeetViFiltering(
            "actualDurationInMinutes.lessThanOrEqual=" + DEFAULT_ACTUAL_DURATION_IN_MINUTES,
            "actualDurationInMinutes.lessThanOrEqual=" + SMALLER_ACTUAL_DURATION_IN_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByActualDurationInMinutesIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where actualDurationInMinutes is less than
        defaultAlGoogleMeetViFiltering(
            "actualDurationInMinutes.lessThan=" + UPDATED_ACTUAL_DURATION_IN_MINUTES,
            "actualDurationInMinutes.lessThan=" + DEFAULT_ACTUAL_DURATION_IN_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByActualDurationInMinutesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where actualDurationInMinutes is greater than
        defaultAlGoogleMeetViFiltering(
            "actualDurationInMinutes.greaterThan=" + SMALLER_ACTUAL_DURATION_IN_MINUTES,
            "actualDurationInMinutes.greaterThan=" + DEFAULT_ACTUAL_DURATION_IN_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByContentJasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where contentJason equals to
        defaultAlGoogleMeetViFiltering("contentJason.equals=" + DEFAULT_CONTENT_JASON, "contentJason.equals=" + UPDATED_CONTENT_JASON);
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByContentJasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where contentJason in
        defaultAlGoogleMeetViFiltering(
            "contentJason.in=" + DEFAULT_CONTENT_JASON + "," + UPDATED_CONTENT_JASON,
            "contentJason.in=" + UPDATED_CONTENT_JASON
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByContentJasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where contentJason is not null
        defaultAlGoogleMeetViFiltering("contentJason.specified=true", "contentJason.specified=false");
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByContentJasonContainsSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where contentJason contains
        defaultAlGoogleMeetViFiltering("contentJason.contains=" + DEFAULT_CONTENT_JASON, "contentJason.contains=" + UPDATED_CONTENT_JASON);
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByContentJasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        // Get all the alGoogleMeetViList where contentJason does not contain
        defaultAlGoogleMeetViFiltering(
            "contentJason.doesNotContain=" + UPDATED_CONTENT_JASON,
            "contentJason.doesNotContain=" + DEFAULT_CONTENT_JASON
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByCustomerIsEqualToSomething() throws Exception {
        AlPacino customer;
        if (TestUtil.findAll(em, AlPacino.class).isEmpty()) {
            alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);
            customer = AlPacinoResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, AlPacino.class).get(0);
        }
        em.persist(customer);
        em.flush();
        alGoogleMeetVi.setCustomer(customer);
        alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);
        UUID customerId = customer.getId();
        // Get all the alGoogleMeetViList where customer equals to customerId
        defaultAlGoogleMeetViShouldBeFound("customerId.equals=" + customerId);

        // Get all the alGoogleMeetViList where customer equals to UUID.randomUUID()
        defaultAlGoogleMeetViShouldNotBeFound("customerId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByAgencyIsEqualToSomething() throws Exception {
        AlAppleVi agency;
        if (TestUtil.findAll(em, AlAppleVi.class).isEmpty()) {
            alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);
            agency = AlAppleViResourceIT.createEntity(em);
        } else {
            agency = TestUtil.findAll(em, AlAppleVi.class).get(0);
        }
        em.persist(agency);
        em.flush();
        alGoogleMeetVi.setAgency(agency);
        alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);
        UUID agencyId = agency.getId();
        // Get all the alGoogleMeetViList where agency equals to agencyId
        defaultAlGoogleMeetViShouldBeFound("agencyId.equals=" + agencyId);

        // Get all the alGoogleMeetViList where agency equals to UUID.randomUUID()
        defaultAlGoogleMeetViShouldNotBeFound("agencyId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByPersonInChargeIsEqualToSomething() throws Exception {
        EdSheeranVi personInCharge;
        if (TestUtil.findAll(em, EdSheeranVi.class).isEmpty()) {
            alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);
            personInCharge = EdSheeranViResourceIT.createEntity(em);
        } else {
            personInCharge = TestUtil.findAll(em, EdSheeranVi.class).get(0);
        }
        em.persist(personInCharge);
        em.flush();
        alGoogleMeetVi.setPersonInCharge(personInCharge);
        alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);
        Long personInChargeId = personInCharge.getId();
        // Get all the alGoogleMeetViList where personInCharge equals to personInChargeId
        defaultAlGoogleMeetViShouldBeFound("personInChargeId.equals=" + personInChargeId);

        // Get all the alGoogleMeetViList where personInCharge equals to (personInChargeId + 1)
        defaultAlGoogleMeetViShouldNotBeFound("personInChargeId.equals=" + (personInChargeId + 1));
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetVisByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alGoogleMeetVi.setApplication(application);
        alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);
        UUID applicationId = application.getId();
        // Get all the alGoogleMeetViList where application equals to applicationId
        defaultAlGoogleMeetViShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alGoogleMeetViList where application equals to UUID.randomUUID()
        defaultAlGoogleMeetViShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlGoogleMeetViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlGoogleMeetViShouldBeFound(shouldBeFound);
        defaultAlGoogleMeetViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlGoogleMeetViShouldBeFound(String filter) throws Exception {
        restAlGoogleMeetViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alGoogleMeetVi.getId().toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].numberOfParticipants").value(hasItem(DEFAULT_NUMBER_OF_PARTICIPANTS)))
            .andExpect(jsonPath("$.[*].plannedDate").value(hasItem(DEFAULT_PLANNED_DATE.toString())))
            .andExpect(jsonPath("$.[*].plannedDurationInMinutes").value(hasItem(DEFAULT_PLANNED_DURATION_IN_MINUTES.doubleValue())))
            .andExpect(jsonPath("$.[*].actualDate").value(hasItem(DEFAULT_ACTUAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].actualDurationInMinutes").value(hasItem(DEFAULT_ACTUAL_DURATION_IN_MINUTES.doubleValue())))
            .andExpect(jsonPath("$.[*].contentJason").value(hasItem(DEFAULT_CONTENT_JASON)));

        // Check, that the count call also returns 1
        restAlGoogleMeetViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlGoogleMeetViShouldNotBeFound(String filter) throws Exception {
        restAlGoogleMeetViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlGoogleMeetViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlGoogleMeetVi() throws Exception {
        // Get the alGoogleMeetVi
        restAlGoogleMeetViMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlGoogleMeetVi() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alGoogleMeetVi
        AlGoogleMeetVi updatedAlGoogleMeetVi = alGoogleMeetViRepository.findById(alGoogleMeetVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlGoogleMeetVi are not directly saved in db
        em.detach(updatedAlGoogleMeetVi);
        updatedAlGoogleMeetVi
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .numberOfParticipants(UPDATED_NUMBER_OF_PARTICIPANTS)
            .plannedDate(UPDATED_PLANNED_DATE)
            .plannedDurationInMinutes(UPDATED_PLANNED_DURATION_IN_MINUTES)
            .actualDate(UPDATED_ACTUAL_DATE)
            .actualDurationInMinutes(UPDATED_ACTUAL_DURATION_IN_MINUTES)
            .contentJason(UPDATED_CONTENT_JASON);
        AlGoogleMeetViDTO alGoogleMeetViDTO = alGoogleMeetViMapper.toDto(updatedAlGoogleMeetVi);

        restAlGoogleMeetViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alGoogleMeetViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alGoogleMeetViDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlGoogleMeetVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlGoogleMeetViToMatchAllProperties(updatedAlGoogleMeetVi);
    }

    @Test
    @Transactional
    void putNonExistingAlGoogleMeetVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alGoogleMeetVi.setId(UUID.randomUUID());

        // Create the AlGoogleMeetVi
        AlGoogleMeetViDTO alGoogleMeetViDTO = alGoogleMeetViMapper.toDto(alGoogleMeetVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlGoogleMeetViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alGoogleMeetViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alGoogleMeetViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlGoogleMeetVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlGoogleMeetVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alGoogleMeetVi.setId(UUID.randomUUID());

        // Create the AlGoogleMeetVi
        AlGoogleMeetViDTO alGoogleMeetViDTO = alGoogleMeetViMapper.toDto(alGoogleMeetVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlGoogleMeetViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alGoogleMeetViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlGoogleMeetVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlGoogleMeetVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alGoogleMeetVi.setId(UUID.randomUUID());

        // Create the AlGoogleMeetVi
        AlGoogleMeetViDTO alGoogleMeetViDTO = alGoogleMeetViMapper.toDto(alGoogleMeetVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlGoogleMeetViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alGoogleMeetViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlGoogleMeetVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlGoogleMeetViWithPatch() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alGoogleMeetVi using partial update
        AlGoogleMeetVi partialUpdatedAlGoogleMeetVi = new AlGoogleMeetVi();
        partialUpdatedAlGoogleMeetVi.setId(alGoogleMeetVi.getId());

        partialUpdatedAlGoogleMeetVi
            .description(UPDATED_DESCRIPTION)
            .numberOfParticipants(UPDATED_NUMBER_OF_PARTICIPANTS)
            .plannedDurationInMinutes(UPDATED_PLANNED_DURATION_IN_MINUTES)
            .actualDate(UPDATED_ACTUAL_DATE)
            .actualDurationInMinutes(UPDATED_ACTUAL_DURATION_IN_MINUTES)
            .contentJason(UPDATED_CONTENT_JASON);

        restAlGoogleMeetViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlGoogleMeetVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlGoogleMeetVi))
            )
            .andExpect(status().isOk());

        // Validate the AlGoogleMeetVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlGoogleMeetViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlGoogleMeetVi, alGoogleMeetVi),
            getPersistedAlGoogleMeetVi(alGoogleMeetVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlGoogleMeetViWithPatch() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alGoogleMeetVi using partial update
        AlGoogleMeetVi partialUpdatedAlGoogleMeetVi = new AlGoogleMeetVi();
        partialUpdatedAlGoogleMeetVi.setId(alGoogleMeetVi.getId());

        partialUpdatedAlGoogleMeetVi
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .numberOfParticipants(UPDATED_NUMBER_OF_PARTICIPANTS)
            .plannedDate(UPDATED_PLANNED_DATE)
            .plannedDurationInMinutes(UPDATED_PLANNED_DURATION_IN_MINUTES)
            .actualDate(UPDATED_ACTUAL_DATE)
            .actualDurationInMinutes(UPDATED_ACTUAL_DURATION_IN_MINUTES)
            .contentJason(UPDATED_CONTENT_JASON);

        restAlGoogleMeetViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlGoogleMeetVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlGoogleMeetVi))
            )
            .andExpect(status().isOk());

        // Validate the AlGoogleMeetVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlGoogleMeetViUpdatableFieldsEquals(partialUpdatedAlGoogleMeetVi, getPersistedAlGoogleMeetVi(partialUpdatedAlGoogleMeetVi));
    }

    @Test
    @Transactional
    void patchNonExistingAlGoogleMeetVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alGoogleMeetVi.setId(UUID.randomUUID());

        // Create the AlGoogleMeetVi
        AlGoogleMeetViDTO alGoogleMeetViDTO = alGoogleMeetViMapper.toDto(alGoogleMeetVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlGoogleMeetViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alGoogleMeetViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alGoogleMeetViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlGoogleMeetVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlGoogleMeetVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alGoogleMeetVi.setId(UUID.randomUUID());

        // Create the AlGoogleMeetVi
        AlGoogleMeetViDTO alGoogleMeetViDTO = alGoogleMeetViMapper.toDto(alGoogleMeetVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlGoogleMeetViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alGoogleMeetViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlGoogleMeetVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlGoogleMeetVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alGoogleMeetVi.setId(UUID.randomUUID());

        // Create the AlGoogleMeetVi
        AlGoogleMeetViDTO alGoogleMeetViDTO = alGoogleMeetViMapper.toDto(alGoogleMeetVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlGoogleMeetViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alGoogleMeetViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlGoogleMeetVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlGoogleMeetVi() throws Exception {
        // Initialize the database
        insertedAlGoogleMeetVi = alGoogleMeetViRepository.saveAndFlush(alGoogleMeetVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alGoogleMeetVi
        restAlGoogleMeetViMockMvc
            .perform(delete(ENTITY_API_URL_ID, alGoogleMeetVi.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alGoogleMeetViRepository.count();
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

    protected AlGoogleMeetVi getPersistedAlGoogleMeetVi(AlGoogleMeetVi alGoogleMeetVi) {
        return alGoogleMeetViRepository.findById(alGoogleMeetVi.getId()).orElseThrow();
    }

    protected void assertPersistedAlGoogleMeetViToMatchAllProperties(AlGoogleMeetVi expectedAlGoogleMeetVi) {
        assertAlGoogleMeetViAllPropertiesEquals(expectedAlGoogleMeetVi, getPersistedAlGoogleMeetVi(expectedAlGoogleMeetVi));
    }

    protected void assertPersistedAlGoogleMeetViToMatchUpdatableProperties(AlGoogleMeetVi expectedAlGoogleMeetVi) {
        assertAlGoogleMeetViAllUpdatablePropertiesEquals(expectedAlGoogleMeetVi, getPersistedAlGoogleMeetVi(expectedAlGoogleMeetVi));
    }
}
