package ai.realworld.web.rest;

import static ai.realworld.domain.AlLeandroAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlLeandro;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.repository.AlLeandroRepository;
import ai.realworld.service.dto.AlLeandroDTO;
import ai.realworld.service.mapper.AlLeandroMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link AlLeandroResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlLeandroResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_WEIGHT = 1;
    private static final Integer UPDATED_WEIGHT = 2;
    private static final Integer SMALLER_WEIGHT = 1 - 1;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FROM_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FROM_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FROM_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_TO_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TO_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TO_DATE = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_IS_ENABLED = false;
    private static final Boolean UPDATED_IS_ENABLED = true;

    private static final Boolean DEFAULT_SEPARATE_WINNING_BY_PERIODS = false;
    private static final Boolean UPDATED_SEPARATE_WINNING_BY_PERIODS = true;

    private static final String ENTITY_API_URL = "/api/al-leandros";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlLeandroRepository alLeandroRepository;

    @Autowired
    private AlLeandroMapper alLeandroMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlLeandroMockMvc;

    private AlLeandro alLeandro;

    private AlLeandro insertedAlLeandro;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlLeandro createEntity() {
        return new AlLeandro()
            .name(DEFAULT_NAME)
            .weight(DEFAULT_WEIGHT)
            .description(DEFAULT_DESCRIPTION)
            .fromDate(DEFAULT_FROM_DATE)
            .toDate(DEFAULT_TO_DATE)
            .isEnabled(DEFAULT_IS_ENABLED)
            .separateWinningByPeriods(DEFAULT_SEPARATE_WINNING_BY_PERIODS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlLeandro createUpdatedEntity() {
        return new AlLeandro()
            .name(UPDATED_NAME)
            .weight(UPDATED_WEIGHT)
            .description(UPDATED_DESCRIPTION)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .isEnabled(UPDATED_IS_ENABLED)
            .separateWinningByPeriods(UPDATED_SEPARATE_WINNING_BY_PERIODS);
    }

    @BeforeEach
    public void initTest() {
        alLeandro = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlLeandro != null) {
            alLeandroRepository.delete(insertedAlLeandro);
            insertedAlLeandro = null;
        }
    }

    @Test
    @Transactional
    void createAlLeandro() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlLeandro
        AlLeandroDTO alLeandroDTO = alLeandroMapper.toDto(alLeandro);
        var returnedAlLeandroDTO = om.readValue(
            restAlLeandroMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alLeandroDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlLeandroDTO.class
        );

        // Validate the AlLeandro in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlLeandro = alLeandroMapper.toEntity(returnedAlLeandroDTO);
        assertAlLeandroUpdatableFieldsEquals(returnedAlLeandro, getPersistedAlLeandro(returnedAlLeandro));

        insertedAlLeandro = returnedAlLeandro;
    }

    @Test
    @Transactional
    void createAlLeandroWithExistingId() throws Exception {
        // Create the AlLeandro with an existing ID
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);
        AlLeandroDTO alLeandroDTO = alLeandroMapper.toDto(alLeandro);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlLeandroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alLeandroDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlLeandro in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alLeandro.setName(null);

        // Create the AlLeandro, which fails.
        AlLeandroDTO alLeandroDTO = alLeandroMapper.toDto(alLeandro);

        restAlLeandroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alLeandroDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlLeandros() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList
        restAlLeandroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alLeandro.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE.toString())))
            .andExpect(jsonPath("$.[*].isEnabled").value(hasItem(DEFAULT_IS_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].separateWinningByPeriods").value(hasItem(DEFAULT_SEPARATE_WINNING_BY_PERIODS.booleanValue())));
    }

    @Test
    @Transactional
    void getAlLeandro() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get the alLeandro
        restAlLeandroMockMvc
            .perform(get(ENTITY_API_URL_ID, alLeandro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alLeandro.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.toDate").value(DEFAULT_TO_DATE.toString()))
            .andExpect(jsonPath("$.isEnabled").value(DEFAULT_IS_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.separateWinningByPeriods").value(DEFAULT_SEPARATE_WINNING_BY_PERIODS.booleanValue()));
    }

    @Test
    @Transactional
    void getAlLeandrosByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        UUID id = alLeandro.getId();

        defaultAlLeandroFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllAlLeandrosByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where name equals to
        defaultAlLeandroFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlLeandrosByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where name in
        defaultAlLeandroFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlLeandrosByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where name is not null
        defaultAlLeandroFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAlLeandrosByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where name contains
        defaultAlLeandroFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlLeandrosByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where name does not contain
        defaultAlLeandroFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAlLeandrosByWeightIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where weight equals to
        defaultAlLeandroFiltering("weight.equals=" + DEFAULT_WEIGHT, "weight.equals=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlLeandrosByWeightIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where weight in
        defaultAlLeandroFiltering("weight.in=" + DEFAULT_WEIGHT + "," + UPDATED_WEIGHT, "weight.in=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlLeandrosByWeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where weight is not null
        defaultAlLeandroFiltering("weight.specified=true", "weight.specified=false");
    }

    @Test
    @Transactional
    void getAllAlLeandrosByWeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where weight is greater than or equal to
        defaultAlLeandroFiltering("weight.greaterThanOrEqual=" + DEFAULT_WEIGHT, "weight.greaterThanOrEqual=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlLeandrosByWeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where weight is less than or equal to
        defaultAlLeandroFiltering("weight.lessThanOrEqual=" + DEFAULT_WEIGHT, "weight.lessThanOrEqual=" + SMALLER_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlLeandrosByWeightIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where weight is less than
        defaultAlLeandroFiltering("weight.lessThan=" + UPDATED_WEIGHT, "weight.lessThan=" + DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlLeandrosByWeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where weight is greater than
        defaultAlLeandroFiltering("weight.greaterThan=" + SMALLER_WEIGHT, "weight.greaterThan=" + DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlLeandrosByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where description equals to
        defaultAlLeandroFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAlLeandrosByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where description in
        defaultAlLeandroFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllAlLeandrosByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where description is not null
        defaultAlLeandroFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllAlLeandrosByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where description contains
        defaultAlLeandroFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAlLeandrosByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where description does not contain
        defaultAlLeandroFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAlLeandrosByFromDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where fromDate equals to
        defaultAlLeandroFiltering("fromDate.equals=" + DEFAULT_FROM_DATE, "fromDate.equals=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllAlLeandrosByFromDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where fromDate in
        defaultAlLeandroFiltering("fromDate.in=" + DEFAULT_FROM_DATE + "," + UPDATED_FROM_DATE, "fromDate.in=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllAlLeandrosByFromDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where fromDate is not null
        defaultAlLeandroFiltering("fromDate.specified=true", "fromDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAlLeandrosByFromDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where fromDate is greater than or equal to
        defaultAlLeandroFiltering("fromDate.greaterThanOrEqual=" + DEFAULT_FROM_DATE, "fromDate.greaterThanOrEqual=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllAlLeandrosByFromDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where fromDate is less than or equal to
        defaultAlLeandroFiltering("fromDate.lessThanOrEqual=" + DEFAULT_FROM_DATE, "fromDate.lessThanOrEqual=" + SMALLER_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllAlLeandrosByFromDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where fromDate is less than
        defaultAlLeandroFiltering("fromDate.lessThan=" + UPDATED_FROM_DATE, "fromDate.lessThan=" + DEFAULT_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllAlLeandrosByFromDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where fromDate is greater than
        defaultAlLeandroFiltering("fromDate.greaterThan=" + SMALLER_FROM_DATE, "fromDate.greaterThan=" + DEFAULT_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllAlLeandrosByToDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where toDate equals to
        defaultAlLeandroFiltering("toDate.equals=" + DEFAULT_TO_DATE, "toDate.equals=" + UPDATED_TO_DATE);
    }

    @Test
    @Transactional
    void getAllAlLeandrosByToDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where toDate in
        defaultAlLeandroFiltering("toDate.in=" + DEFAULT_TO_DATE + "," + UPDATED_TO_DATE, "toDate.in=" + UPDATED_TO_DATE);
    }

    @Test
    @Transactional
    void getAllAlLeandrosByToDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where toDate is not null
        defaultAlLeandroFiltering("toDate.specified=true", "toDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAlLeandrosByToDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where toDate is greater than or equal to
        defaultAlLeandroFiltering("toDate.greaterThanOrEqual=" + DEFAULT_TO_DATE, "toDate.greaterThanOrEqual=" + UPDATED_TO_DATE);
    }

    @Test
    @Transactional
    void getAllAlLeandrosByToDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where toDate is less than or equal to
        defaultAlLeandroFiltering("toDate.lessThanOrEqual=" + DEFAULT_TO_DATE, "toDate.lessThanOrEqual=" + SMALLER_TO_DATE);
    }

    @Test
    @Transactional
    void getAllAlLeandrosByToDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where toDate is less than
        defaultAlLeandroFiltering("toDate.lessThan=" + UPDATED_TO_DATE, "toDate.lessThan=" + DEFAULT_TO_DATE);
    }

    @Test
    @Transactional
    void getAllAlLeandrosByToDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where toDate is greater than
        defaultAlLeandroFiltering("toDate.greaterThan=" + SMALLER_TO_DATE, "toDate.greaterThan=" + DEFAULT_TO_DATE);
    }

    @Test
    @Transactional
    void getAllAlLeandrosByIsEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where isEnabled equals to
        defaultAlLeandroFiltering("isEnabled.equals=" + DEFAULT_IS_ENABLED, "isEnabled.equals=" + UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    void getAllAlLeandrosByIsEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where isEnabled in
        defaultAlLeandroFiltering("isEnabled.in=" + DEFAULT_IS_ENABLED + "," + UPDATED_IS_ENABLED, "isEnabled.in=" + UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    void getAllAlLeandrosByIsEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where isEnabled is not null
        defaultAlLeandroFiltering("isEnabled.specified=true", "isEnabled.specified=false");
    }

    @Test
    @Transactional
    void getAllAlLeandrosBySeparateWinningByPeriodsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where separateWinningByPeriods equals to
        defaultAlLeandroFiltering(
            "separateWinningByPeriods.equals=" + DEFAULT_SEPARATE_WINNING_BY_PERIODS,
            "separateWinningByPeriods.equals=" + UPDATED_SEPARATE_WINNING_BY_PERIODS
        );
    }

    @Test
    @Transactional
    void getAllAlLeandrosBySeparateWinningByPeriodsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where separateWinningByPeriods in
        defaultAlLeandroFiltering(
            "separateWinningByPeriods.in=" + DEFAULT_SEPARATE_WINNING_BY_PERIODS + "," + UPDATED_SEPARATE_WINNING_BY_PERIODS,
            "separateWinningByPeriods.in=" + UPDATED_SEPARATE_WINNING_BY_PERIODS
        );
    }

    @Test
    @Transactional
    void getAllAlLeandrosBySeparateWinningByPeriodsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        // Get all the alLeandroList where separateWinningByPeriods is not null
        defaultAlLeandroFiltering("separateWinningByPeriods.specified=true", "separateWinningByPeriods.specified=false");
    }

    @Test
    @Transactional
    void getAllAlLeandrosByProgramBackgroundIsEqualToSomething() throws Exception {
        Metaverse programBackground;
        if (TestUtil.findAll(em, Metaverse.class).isEmpty()) {
            alLeandroRepository.saveAndFlush(alLeandro);
            programBackground = MetaverseResourceIT.createEntity();
        } else {
            programBackground = TestUtil.findAll(em, Metaverse.class).get(0);
        }
        em.persist(programBackground);
        em.flush();
        alLeandro.setProgramBackground(programBackground);
        alLeandroRepository.saveAndFlush(alLeandro);
        Long programBackgroundId = programBackground.getId();
        // Get all the alLeandroList where programBackground equals to programBackgroundId
        defaultAlLeandroShouldBeFound("programBackgroundId.equals=" + programBackgroundId);

        // Get all the alLeandroList where programBackground equals to (programBackgroundId + 1)
        defaultAlLeandroShouldNotBeFound("programBackgroundId.equals=" + (programBackgroundId + 1));
    }

    @Test
    @Transactional
    void getAllAlLeandrosByWheelBackgroundIsEqualToSomething() throws Exception {
        Metaverse wheelBackground;
        if (TestUtil.findAll(em, Metaverse.class).isEmpty()) {
            alLeandroRepository.saveAndFlush(alLeandro);
            wheelBackground = MetaverseResourceIT.createEntity();
        } else {
            wheelBackground = TestUtil.findAll(em, Metaverse.class).get(0);
        }
        em.persist(wheelBackground);
        em.flush();
        alLeandro.setWheelBackground(wheelBackground);
        alLeandroRepository.saveAndFlush(alLeandro);
        Long wheelBackgroundId = wheelBackground.getId();
        // Get all the alLeandroList where wheelBackground equals to wheelBackgroundId
        defaultAlLeandroShouldBeFound("wheelBackgroundId.equals=" + wheelBackgroundId);

        // Get all the alLeandroList where wheelBackground equals to (wheelBackgroundId + 1)
        defaultAlLeandroShouldNotBeFound("wheelBackgroundId.equals=" + (wheelBackgroundId + 1));
    }

    @Test
    @Transactional
    void getAllAlLeandrosByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alLeandroRepository.saveAndFlush(alLeandro);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alLeandro.setApplication(application);
        alLeandroRepository.saveAndFlush(alLeandro);
        UUID applicationId = application.getId();
        // Get all the alLeandroList where application equals to applicationId
        defaultAlLeandroShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alLeandroList where application equals to UUID.randomUUID()
        defaultAlLeandroShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlLeandroFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlLeandroShouldBeFound(shouldBeFound);
        defaultAlLeandroShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlLeandroShouldBeFound(String filter) throws Exception {
        restAlLeandroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alLeandro.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE.toString())))
            .andExpect(jsonPath("$.[*].isEnabled").value(hasItem(DEFAULT_IS_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].separateWinningByPeriods").value(hasItem(DEFAULT_SEPARATE_WINNING_BY_PERIODS.booleanValue())));

        // Check, that the count call also returns 1
        restAlLeandroMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlLeandroShouldNotBeFound(String filter) throws Exception {
        restAlLeandroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlLeandroMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlLeandro() throws Exception {
        // Get the alLeandro
        restAlLeandroMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlLeandro() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alLeandro
        AlLeandro updatedAlLeandro = alLeandroRepository.findById(alLeandro.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlLeandro are not directly saved in db
        em.detach(updatedAlLeandro);
        updatedAlLeandro
            .name(UPDATED_NAME)
            .weight(UPDATED_WEIGHT)
            .description(UPDATED_DESCRIPTION)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .isEnabled(UPDATED_IS_ENABLED)
            .separateWinningByPeriods(UPDATED_SEPARATE_WINNING_BY_PERIODS);
        AlLeandroDTO alLeandroDTO = alLeandroMapper.toDto(updatedAlLeandro);

        restAlLeandroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alLeandroDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alLeandroDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlLeandro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlLeandroToMatchAllProperties(updatedAlLeandro);
    }

    @Test
    @Transactional
    void putNonExistingAlLeandro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLeandro.setId(UUID.randomUUID());

        // Create the AlLeandro
        AlLeandroDTO alLeandroDTO = alLeandroMapper.toDto(alLeandro);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlLeandroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alLeandroDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alLeandroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlLeandro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlLeandro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLeandro.setId(UUID.randomUUID());

        // Create the AlLeandro
        AlLeandroDTO alLeandroDTO = alLeandroMapper.toDto(alLeandro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlLeandroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alLeandroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlLeandro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlLeandro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLeandro.setId(UUID.randomUUID());

        // Create the AlLeandro
        AlLeandroDTO alLeandroDTO = alLeandroMapper.toDto(alLeandro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlLeandroMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alLeandroDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlLeandro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlLeandroWithPatch() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alLeandro using partial update
        AlLeandro partialUpdatedAlLeandro = new AlLeandro();
        partialUpdatedAlLeandro.setId(alLeandro.getId());

        partialUpdatedAlLeandro
            .name(UPDATED_NAME)
            .weight(UPDATED_WEIGHT)
            .description(UPDATED_DESCRIPTION)
            .toDate(UPDATED_TO_DATE)
            .isEnabled(UPDATED_IS_ENABLED);

        restAlLeandroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlLeandro.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlLeandro))
            )
            .andExpect(status().isOk());

        // Validate the AlLeandro in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlLeandroUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlLeandro, alLeandro),
            getPersistedAlLeandro(alLeandro)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlLeandroWithPatch() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alLeandro using partial update
        AlLeandro partialUpdatedAlLeandro = new AlLeandro();
        partialUpdatedAlLeandro.setId(alLeandro.getId());

        partialUpdatedAlLeandro
            .name(UPDATED_NAME)
            .weight(UPDATED_WEIGHT)
            .description(UPDATED_DESCRIPTION)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .isEnabled(UPDATED_IS_ENABLED)
            .separateWinningByPeriods(UPDATED_SEPARATE_WINNING_BY_PERIODS);

        restAlLeandroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlLeandro.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlLeandro))
            )
            .andExpect(status().isOk());

        // Validate the AlLeandro in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlLeandroUpdatableFieldsEquals(partialUpdatedAlLeandro, getPersistedAlLeandro(partialUpdatedAlLeandro));
    }

    @Test
    @Transactional
    void patchNonExistingAlLeandro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLeandro.setId(UUID.randomUUID());

        // Create the AlLeandro
        AlLeandroDTO alLeandroDTO = alLeandroMapper.toDto(alLeandro);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlLeandroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alLeandroDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alLeandroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlLeandro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlLeandro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLeandro.setId(UUID.randomUUID());

        // Create the AlLeandro
        AlLeandroDTO alLeandroDTO = alLeandroMapper.toDto(alLeandro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlLeandroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alLeandroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlLeandro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlLeandro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLeandro.setId(UUID.randomUUID());

        // Create the AlLeandro
        AlLeandroDTO alLeandroDTO = alLeandroMapper.toDto(alLeandro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlLeandroMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alLeandroDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlLeandro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlLeandro() throws Exception {
        // Initialize the database
        insertedAlLeandro = alLeandroRepository.saveAndFlush(alLeandro);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alLeandro
        restAlLeandroMockMvc
            .perform(delete(ENTITY_API_URL_ID, alLeandro.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alLeandroRepository.count();
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

    protected AlLeandro getPersistedAlLeandro(AlLeandro alLeandro) {
        return alLeandroRepository.findById(alLeandro.getId()).orElseThrow();
    }

    protected void assertPersistedAlLeandroToMatchAllProperties(AlLeandro expectedAlLeandro) {
        assertAlLeandroAllPropertiesEquals(expectedAlLeandro, getPersistedAlLeandro(expectedAlLeandro));
    }

    protected void assertPersistedAlLeandroToMatchUpdatableProperties(AlLeandro expectedAlLeandro) {
        assertAlLeandroAllUpdatablePropertiesEquals(expectedAlLeandro, getPersistedAlLeandro(expectedAlLeandro));
    }
}
