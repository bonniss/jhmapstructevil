package ai.realworld.web.rest;

import static ai.realworld.domain.AlGoogleMeetAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlApple;
import ai.realworld.domain.AlGoogleMeet;
import ai.realworld.domain.AlPacino;
import ai.realworld.domain.EdSheeran;
import ai.realworld.domain.JohnLennon;
import ai.realworld.repository.AlGoogleMeetRepository;
import ai.realworld.service.dto.AlGoogleMeetDTO;
import ai.realworld.service.mapper.AlGoogleMeetMapper;
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
 * Integration tests for the {@link AlGoogleMeetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlGoogleMeetResourceIT {

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

    private static final String ENTITY_API_URL = "/api/al-google-meets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlGoogleMeetRepository alGoogleMeetRepository;

    @Autowired
    private AlGoogleMeetMapper alGoogleMeetMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlGoogleMeetMockMvc;

    private AlGoogleMeet alGoogleMeet;

    private AlGoogleMeet insertedAlGoogleMeet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlGoogleMeet createEntity() {
        return new AlGoogleMeet()
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
    public static AlGoogleMeet createUpdatedEntity() {
        return new AlGoogleMeet()
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
        alGoogleMeet = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlGoogleMeet != null) {
            alGoogleMeetRepository.delete(insertedAlGoogleMeet);
            insertedAlGoogleMeet = null;
        }
    }

    @Test
    @Transactional
    void createAlGoogleMeet() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlGoogleMeet
        AlGoogleMeetDTO alGoogleMeetDTO = alGoogleMeetMapper.toDto(alGoogleMeet);
        var returnedAlGoogleMeetDTO = om.readValue(
            restAlGoogleMeetMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alGoogleMeetDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlGoogleMeetDTO.class
        );

        // Validate the AlGoogleMeet in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlGoogleMeet = alGoogleMeetMapper.toEntity(returnedAlGoogleMeetDTO);
        assertAlGoogleMeetUpdatableFieldsEquals(returnedAlGoogleMeet, getPersistedAlGoogleMeet(returnedAlGoogleMeet));

        insertedAlGoogleMeet = returnedAlGoogleMeet;
    }

    @Test
    @Transactional
    void createAlGoogleMeetWithExistingId() throws Exception {
        // Create the AlGoogleMeet with an existing ID
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);
        AlGoogleMeetDTO alGoogleMeetDTO = alGoogleMeetMapper.toDto(alGoogleMeet);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlGoogleMeetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alGoogleMeetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlGoogleMeet in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alGoogleMeet.setTitle(null);

        // Create the AlGoogleMeet, which fails.
        AlGoogleMeetDTO alGoogleMeetDTO = alGoogleMeetMapper.toDto(alGoogleMeet);

        restAlGoogleMeetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alGoogleMeetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlGoogleMeets() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList
        restAlGoogleMeetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alGoogleMeet.getId().toString())))
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
    void getAlGoogleMeet() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get the alGoogleMeet
        restAlGoogleMeetMockMvc
            .perform(get(ENTITY_API_URL_ID, alGoogleMeet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alGoogleMeet.getId().toString()))
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
    void getAlGoogleMeetsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        UUID id = alGoogleMeet.getId();

        defaultAlGoogleMeetFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where title equals to
        defaultAlGoogleMeetFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where title in
        defaultAlGoogleMeetFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where title is not null
        defaultAlGoogleMeetFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where title contains
        defaultAlGoogleMeetFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where title does not contain
        defaultAlGoogleMeetFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where description equals to
        defaultAlGoogleMeetFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where description in
        defaultAlGoogleMeetFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where description is not null
        defaultAlGoogleMeetFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where description contains
        defaultAlGoogleMeetFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where description does not contain
        defaultAlGoogleMeetFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByNumberOfParticipantsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where numberOfParticipants equals to
        defaultAlGoogleMeetFiltering(
            "numberOfParticipants.equals=" + DEFAULT_NUMBER_OF_PARTICIPANTS,
            "numberOfParticipants.equals=" + UPDATED_NUMBER_OF_PARTICIPANTS
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByNumberOfParticipantsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where numberOfParticipants in
        defaultAlGoogleMeetFiltering(
            "numberOfParticipants.in=" + DEFAULT_NUMBER_OF_PARTICIPANTS + "," + UPDATED_NUMBER_OF_PARTICIPANTS,
            "numberOfParticipants.in=" + UPDATED_NUMBER_OF_PARTICIPANTS
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByNumberOfParticipantsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where numberOfParticipants is not null
        defaultAlGoogleMeetFiltering("numberOfParticipants.specified=true", "numberOfParticipants.specified=false");
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByNumberOfParticipantsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where numberOfParticipants is greater than or equal to
        defaultAlGoogleMeetFiltering(
            "numberOfParticipants.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_PARTICIPANTS,
            "numberOfParticipants.greaterThanOrEqual=" + UPDATED_NUMBER_OF_PARTICIPANTS
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByNumberOfParticipantsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where numberOfParticipants is less than or equal to
        defaultAlGoogleMeetFiltering(
            "numberOfParticipants.lessThanOrEqual=" + DEFAULT_NUMBER_OF_PARTICIPANTS,
            "numberOfParticipants.lessThanOrEqual=" + SMALLER_NUMBER_OF_PARTICIPANTS
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByNumberOfParticipantsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where numberOfParticipants is less than
        defaultAlGoogleMeetFiltering(
            "numberOfParticipants.lessThan=" + UPDATED_NUMBER_OF_PARTICIPANTS,
            "numberOfParticipants.lessThan=" + DEFAULT_NUMBER_OF_PARTICIPANTS
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByNumberOfParticipantsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where numberOfParticipants is greater than
        defaultAlGoogleMeetFiltering(
            "numberOfParticipants.greaterThan=" + SMALLER_NUMBER_OF_PARTICIPANTS,
            "numberOfParticipants.greaterThan=" + DEFAULT_NUMBER_OF_PARTICIPANTS
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByPlannedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where plannedDate equals to
        defaultAlGoogleMeetFiltering("plannedDate.equals=" + DEFAULT_PLANNED_DATE, "plannedDate.equals=" + UPDATED_PLANNED_DATE);
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByPlannedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where plannedDate in
        defaultAlGoogleMeetFiltering(
            "plannedDate.in=" + DEFAULT_PLANNED_DATE + "," + UPDATED_PLANNED_DATE,
            "plannedDate.in=" + UPDATED_PLANNED_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByPlannedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where plannedDate is not null
        defaultAlGoogleMeetFiltering("plannedDate.specified=true", "plannedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByPlannedDurationInMinutesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where plannedDurationInMinutes equals to
        defaultAlGoogleMeetFiltering(
            "plannedDurationInMinutes.equals=" + DEFAULT_PLANNED_DURATION_IN_MINUTES,
            "plannedDurationInMinutes.equals=" + UPDATED_PLANNED_DURATION_IN_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByPlannedDurationInMinutesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where plannedDurationInMinutes in
        defaultAlGoogleMeetFiltering(
            "plannedDurationInMinutes.in=" + DEFAULT_PLANNED_DURATION_IN_MINUTES + "," + UPDATED_PLANNED_DURATION_IN_MINUTES,
            "plannedDurationInMinutes.in=" + UPDATED_PLANNED_DURATION_IN_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByPlannedDurationInMinutesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where plannedDurationInMinutes is not null
        defaultAlGoogleMeetFiltering("plannedDurationInMinutes.specified=true", "plannedDurationInMinutes.specified=false");
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByPlannedDurationInMinutesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where plannedDurationInMinutes is greater than or equal to
        defaultAlGoogleMeetFiltering(
            "plannedDurationInMinutes.greaterThanOrEqual=" + DEFAULT_PLANNED_DURATION_IN_MINUTES,
            "plannedDurationInMinutes.greaterThanOrEqual=" + UPDATED_PLANNED_DURATION_IN_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByPlannedDurationInMinutesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where plannedDurationInMinutes is less than or equal to
        defaultAlGoogleMeetFiltering(
            "plannedDurationInMinutes.lessThanOrEqual=" + DEFAULT_PLANNED_DURATION_IN_MINUTES,
            "plannedDurationInMinutes.lessThanOrEqual=" + SMALLER_PLANNED_DURATION_IN_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByPlannedDurationInMinutesIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where plannedDurationInMinutes is less than
        defaultAlGoogleMeetFiltering(
            "plannedDurationInMinutes.lessThan=" + UPDATED_PLANNED_DURATION_IN_MINUTES,
            "plannedDurationInMinutes.lessThan=" + DEFAULT_PLANNED_DURATION_IN_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByPlannedDurationInMinutesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where plannedDurationInMinutes is greater than
        defaultAlGoogleMeetFiltering(
            "plannedDurationInMinutes.greaterThan=" + SMALLER_PLANNED_DURATION_IN_MINUTES,
            "plannedDurationInMinutes.greaterThan=" + DEFAULT_PLANNED_DURATION_IN_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByActualDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where actualDate equals to
        defaultAlGoogleMeetFiltering("actualDate.equals=" + DEFAULT_ACTUAL_DATE, "actualDate.equals=" + UPDATED_ACTUAL_DATE);
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByActualDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where actualDate in
        defaultAlGoogleMeetFiltering(
            "actualDate.in=" + DEFAULT_ACTUAL_DATE + "," + UPDATED_ACTUAL_DATE,
            "actualDate.in=" + UPDATED_ACTUAL_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByActualDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where actualDate is not null
        defaultAlGoogleMeetFiltering("actualDate.specified=true", "actualDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByActualDurationInMinutesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where actualDurationInMinutes equals to
        defaultAlGoogleMeetFiltering(
            "actualDurationInMinutes.equals=" + DEFAULT_ACTUAL_DURATION_IN_MINUTES,
            "actualDurationInMinutes.equals=" + UPDATED_ACTUAL_DURATION_IN_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByActualDurationInMinutesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where actualDurationInMinutes in
        defaultAlGoogleMeetFiltering(
            "actualDurationInMinutes.in=" + DEFAULT_ACTUAL_DURATION_IN_MINUTES + "," + UPDATED_ACTUAL_DURATION_IN_MINUTES,
            "actualDurationInMinutes.in=" + UPDATED_ACTUAL_DURATION_IN_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByActualDurationInMinutesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where actualDurationInMinutes is not null
        defaultAlGoogleMeetFiltering("actualDurationInMinutes.specified=true", "actualDurationInMinutes.specified=false");
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByActualDurationInMinutesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where actualDurationInMinutes is greater than or equal to
        defaultAlGoogleMeetFiltering(
            "actualDurationInMinutes.greaterThanOrEqual=" + DEFAULT_ACTUAL_DURATION_IN_MINUTES,
            "actualDurationInMinutes.greaterThanOrEqual=" + UPDATED_ACTUAL_DURATION_IN_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByActualDurationInMinutesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where actualDurationInMinutes is less than or equal to
        defaultAlGoogleMeetFiltering(
            "actualDurationInMinutes.lessThanOrEqual=" + DEFAULT_ACTUAL_DURATION_IN_MINUTES,
            "actualDurationInMinutes.lessThanOrEqual=" + SMALLER_ACTUAL_DURATION_IN_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByActualDurationInMinutesIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where actualDurationInMinutes is less than
        defaultAlGoogleMeetFiltering(
            "actualDurationInMinutes.lessThan=" + UPDATED_ACTUAL_DURATION_IN_MINUTES,
            "actualDurationInMinutes.lessThan=" + DEFAULT_ACTUAL_DURATION_IN_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByActualDurationInMinutesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where actualDurationInMinutes is greater than
        defaultAlGoogleMeetFiltering(
            "actualDurationInMinutes.greaterThan=" + SMALLER_ACTUAL_DURATION_IN_MINUTES,
            "actualDurationInMinutes.greaterThan=" + DEFAULT_ACTUAL_DURATION_IN_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByContentJasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where contentJason equals to
        defaultAlGoogleMeetFiltering("contentJason.equals=" + DEFAULT_CONTENT_JASON, "contentJason.equals=" + UPDATED_CONTENT_JASON);
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByContentJasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where contentJason in
        defaultAlGoogleMeetFiltering(
            "contentJason.in=" + DEFAULT_CONTENT_JASON + "," + UPDATED_CONTENT_JASON,
            "contentJason.in=" + UPDATED_CONTENT_JASON
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByContentJasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where contentJason is not null
        defaultAlGoogleMeetFiltering("contentJason.specified=true", "contentJason.specified=false");
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByContentJasonContainsSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where contentJason contains
        defaultAlGoogleMeetFiltering("contentJason.contains=" + DEFAULT_CONTENT_JASON, "contentJason.contains=" + UPDATED_CONTENT_JASON);
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByContentJasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        // Get all the alGoogleMeetList where contentJason does not contain
        defaultAlGoogleMeetFiltering(
            "contentJason.doesNotContain=" + UPDATED_CONTENT_JASON,
            "contentJason.doesNotContain=" + DEFAULT_CONTENT_JASON
        );
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByCustomerIsEqualToSomething() throws Exception {
        AlPacino customer;
        if (TestUtil.findAll(em, AlPacino.class).isEmpty()) {
            alGoogleMeetRepository.saveAndFlush(alGoogleMeet);
            customer = AlPacinoResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, AlPacino.class).get(0);
        }
        em.persist(customer);
        em.flush();
        alGoogleMeet.setCustomer(customer);
        alGoogleMeetRepository.saveAndFlush(alGoogleMeet);
        UUID customerId = customer.getId();
        // Get all the alGoogleMeetList where customer equals to customerId
        defaultAlGoogleMeetShouldBeFound("customerId.equals=" + customerId);

        // Get all the alGoogleMeetList where customer equals to UUID.randomUUID()
        defaultAlGoogleMeetShouldNotBeFound("customerId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByAgencyIsEqualToSomething() throws Exception {
        AlApple agency;
        if (TestUtil.findAll(em, AlApple.class).isEmpty()) {
            alGoogleMeetRepository.saveAndFlush(alGoogleMeet);
            agency = AlAppleResourceIT.createEntity(em);
        } else {
            agency = TestUtil.findAll(em, AlApple.class).get(0);
        }
        em.persist(agency);
        em.flush();
        alGoogleMeet.setAgency(agency);
        alGoogleMeetRepository.saveAndFlush(alGoogleMeet);
        UUID agencyId = agency.getId();
        // Get all the alGoogleMeetList where agency equals to agencyId
        defaultAlGoogleMeetShouldBeFound("agencyId.equals=" + agencyId);

        // Get all the alGoogleMeetList where agency equals to UUID.randomUUID()
        defaultAlGoogleMeetShouldNotBeFound("agencyId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByPersonInChargeIsEqualToSomething() throws Exception {
        EdSheeran personInCharge;
        if (TestUtil.findAll(em, EdSheeran.class).isEmpty()) {
            alGoogleMeetRepository.saveAndFlush(alGoogleMeet);
            personInCharge = EdSheeranResourceIT.createEntity(em);
        } else {
            personInCharge = TestUtil.findAll(em, EdSheeran.class).get(0);
        }
        em.persist(personInCharge);
        em.flush();
        alGoogleMeet.setPersonInCharge(personInCharge);
        alGoogleMeetRepository.saveAndFlush(alGoogleMeet);
        Long personInChargeId = personInCharge.getId();
        // Get all the alGoogleMeetList where personInCharge equals to personInChargeId
        defaultAlGoogleMeetShouldBeFound("personInChargeId.equals=" + personInChargeId);

        // Get all the alGoogleMeetList where personInCharge equals to (personInChargeId + 1)
        defaultAlGoogleMeetShouldNotBeFound("personInChargeId.equals=" + (personInChargeId + 1));
    }

    @Test
    @Transactional
    void getAllAlGoogleMeetsByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alGoogleMeetRepository.saveAndFlush(alGoogleMeet);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alGoogleMeet.setApplication(application);
        alGoogleMeetRepository.saveAndFlush(alGoogleMeet);
        UUID applicationId = application.getId();
        // Get all the alGoogleMeetList where application equals to applicationId
        defaultAlGoogleMeetShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alGoogleMeetList where application equals to UUID.randomUUID()
        defaultAlGoogleMeetShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlGoogleMeetFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlGoogleMeetShouldBeFound(shouldBeFound);
        defaultAlGoogleMeetShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlGoogleMeetShouldBeFound(String filter) throws Exception {
        restAlGoogleMeetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alGoogleMeet.getId().toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].numberOfParticipants").value(hasItem(DEFAULT_NUMBER_OF_PARTICIPANTS)))
            .andExpect(jsonPath("$.[*].plannedDate").value(hasItem(DEFAULT_PLANNED_DATE.toString())))
            .andExpect(jsonPath("$.[*].plannedDurationInMinutes").value(hasItem(DEFAULT_PLANNED_DURATION_IN_MINUTES.doubleValue())))
            .andExpect(jsonPath("$.[*].actualDate").value(hasItem(DEFAULT_ACTUAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].actualDurationInMinutes").value(hasItem(DEFAULT_ACTUAL_DURATION_IN_MINUTES.doubleValue())))
            .andExpect(jsonPath("$.[*].contentJason").value(hasItem(DEFAULT_CONTENT_JASON)));

        // Check, that the count call also returns 1
        restAlGoogleMeetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlGoogleMeetShouldNotBeFound(String filter) throws Exception {
        restAlGoogleMeetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlGoogleMeetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlGoogleMeet() throws Exception {
        // Get the alGoogleMeet
        restAlGoogleMeetMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlGoogleMeet() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alGoogleMeet
        AlGoogleMeet updatedAlGoogleMeet = alGoogleMeetRepository.findById(alGoogleMeet.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlGoogleMeet are not directly saved in db
        em.detach(updatedAlGoogleMeet);
        updatedAlGoogleMeet
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .numberOfParticipants(UPDATED_NUMBER_OF_PARTICIPANTS)
            .plannedDate(UPDATED_PLANNED_DATE)
            .plannedDurationInMinutes(UPDATED_PLANNED_DURATION_IN_MINUTES)
            .actualDate(UPDATED_ACTUAL_DATE)
            .actualDurationInMinutes(UPDATED_ACTUAL_DURATION_IN_MINUTES)
            .contentJason(UPDATED_CONTENT_JASON);
        AlGoogleMeetDTO alGoogleMeetDTO = alGoogleMeetMapper.toDto(updatedAlGoogleMeet);

        restAlGoogleMeetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alGoogleMeetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alGoogleMeetDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlGoogleMeet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlGoogleMeetToMatchAllProperties(updatedAlGoogleMeet);
    }

    @Test
    @Transactional
    void putNonExistingAlGoogleMeet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alGoogleMeet.setId(UUID.randomUUID());

        // Create the AlGoogleMeet
        AlGoogleMeetDTO alGoogleMeetDTO = alGoogleMeetMapper.toDto(alGoogleMeet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlGoogleMeetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alGoogleMeetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alGoogleMeetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlGoogleMeet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlGoogleMeet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alGoogleMeet.setId(UUID.randomUUID());

        // Create the AlGoogleMeet
        AlGoogleMeetDTO alGoogleMeetDTO = alGoogleMeetMapper.toDto(alGoogleMeet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlGoogleMeetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alGoogleMeetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlGoogleMeet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlGoogleMeet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alGoogleMeet.setId(UUID.randomUUID());

        // Create the AlGoogleMeet
        AlGoogleMeetDTO alGoogleMeetDTO = alGoogleMeetMapper.toDto(alGoogleMeet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlGoogleMeetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alGoogleMeetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlGoogleMeet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlGoogleMeetWithPatch() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alGoogleMeet using partial update
        AlGoogleMeet partialUpdatedAlGoogleMeet = new AlGoogleMeet();
        partialUpdatedAlGoogleMeet.setId(alGoogleMeet.getId());

        partialUpdatedAlGoogleMeet
            .description(UPDATED_DESCRIPTION)
            .plannedDate(UPDATED_PLANNED_DATE)
            .plannedDurationInMinutes(UPDATED_PLANNED_DURATION_IN_MINUTES)
            .actualDurationInMinutes(UPDATED_ACTUAL_DURATION_IN_MINUTES);

        restAlGoogleMeetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlGoogleMeet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlGoogleMeet))
            )
            .andExpect(status().isOk());

        // Validate the AlGoogleMeet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlGoogleMeetUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlGoogleMeet, alGoogleMeet),
            getPersistedAlGoogleMeet(alGoogleMeet)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlGoogleMeetWithPatch() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alGoogleMeet using partial update
        AlGoogleMeet partialUpdatedAlGoogleMeet = new AlGoogleMeet();
        partialUpdatedAlGoogleMeet.setId(alGoogleMeet.getId());

        partialUpdatedAlGoogleMeet
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .numberOfParticipants(UPDATED_NUMBER_OF_PARTICIPANTS)
            .plannedDate(UPDATED_PLANNED_DATE)
            .plannedDurationInMinutes(UPDATED_PLANNED_DURATION_IN_MINUTES)
            .actualDate(UPDATED_ACTUAL_DATE)
            .actualDurationInMinutes(UPDATED_ACTUAL_DURATION_IN_MINUTES)
            .contentJason(UPDATED_CONTENT_JASON);

        restAlGoogleMeetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlGoogleMeet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlGoogleMeet))
            )
            .andExpect(status().isOk());

        // Validate the AlGoogleMeet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlGoogleMeetUpdatableFieldsEquals(partialUpdatedAlGoogleMeet, getPersistedAlGoogleMeet(partialUpdatedAlGoogleMeet));
    }

    @Test
    @Transactional
    void patchNonExistingAlGoogleMeet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alGoogleMeet.setId(UUID.randomUUID());

        // Create the AlGoogleMeet
        AlGoogleMeetDTO alGoogleMeetDTO = alGoogleMeetMapper.toDto(alGoogleMeet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlGoogleMeetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alGoogleMeetDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alGoogleMeetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlGoogleMeet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlGoogleMeet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alGoogleMeet.setId(UUID.randomUUID());

        // Create the AlGoogleMeet
        AlGoogleMeetDTO alGoogleMeetDTO = alGoogleMeetMapper.toDto(alGoogleMeet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlGoogleMeetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alGoogleMeetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlGoogleMeet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlGoogleMeet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alGoogleMeet.setId(UUID.randomUUID());

        // Create the AlGoogleMeet
        AlGoogleMeetDTO alGoogleMeetDTO = alGoogleMeetMapper.toDto(alGoogleMeet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlGoogleMeetMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alGoogleMeetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlGoogleMeet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlGoogleMeet() throws Exception {
        // Initialize the database
        insertedAlGoogleMeet = alGoogleMeetRepository.saveAndFlush(alGoogleMeet);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alGoogleMeet
        restAlGoogleMeetMockMvc
            .perform(delete(ENTITY_API_URL_ID, alGoogleMeet.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alGoogleMeetRepository.count();
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

    protected AlGoogleMeet getPersistedAlGoogleMeet(AlGoogleMeet alGoogleMeet) {
        return alGoogleMeetRepository.findById(alGoogleMeet.getId()).orElseThrow();
    }

    protected void assertPersistedAlGoogleMeetToMatchAllProperties(AlGoogleMeet expectedAlGoogleMeet) {
        assertAlGoogleMeetAllPropertiesEquals(expectedAlGoogleMeet, getPersistedAlGoogleMeet(expectedAlGoogleMeet));
    }

    protected void assertPersistedAlGoogleMeetToMatchUpdatableProperties(AlGoogleMeet expectedAlGoogleMeet) {
        assertAlGoogleMeetAllUpdatablePropertiesEquals(expectedAlGoogleMeet, getPersistedAlGoogleMeet(expectedAlGoogleMeet));
    }
}
