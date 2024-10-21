package ai.realworld.web.rest;

import static ai.realworld.domain.AlDesireAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlDesire;
import ai.realworld.domain.AlLeandro;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.domain.enumeration.FooGameAward;
import ai.realworld.repository.AlDesireRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link AlDesireResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlDesireResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_WEIGHT = 1;
    private static final Integer UPDATED_WEIGHT = 2;
    private static final Integer SMALLER_WEIGHT = 1 - 1;

    private static final Float DEFAULT_PROBABILITY_OF_WINNING = 1F;
    private static final Float UPDATED_PROBABILITY_OF_WINNING = 2F;
    private static final Float SMALLER_PROBABILITY_OF_WINNING = 1F - 1F;

    private static final Integer DEFAULT_MAXIMUM_WINNING_TIME = 1;
    private static final Integer UPDATED_MAXIMUM_WINNING_TIME = 2;
    private static final Integer SMALLER_MAXIMUM_WINNING_TIME = 1 - 1;

    private static final Boolean DEFAULT_IS_WINNING_TIME_LIMITED = false;
    private static final Boolean UPDATED_IS_WINNING_TIME_LIMITED = true;

    private static final FooGameAward DEFAULT_AWARD_RESULT_TYPE = FooGameAward.LOST;
    private static final FooGameAward UPDATED_AWARD_RESULT_TYPE = FooGameAward.PLAY_TIMES;

    private static final String DEFAULT_AWARD_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_AWARD_REFERENCE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DEFAULT = false;
    private static final Boolean UPDATED_IS_DEFAULT = true;

    private static final String ENTITY_API_URL = "/api/al-desires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlDesireRepository alDesireRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlDesireMockMvc;

    private AlDesire alDesire;

    private AlDesire insertedAlDesire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlDesire createEntity() {
        return new AlDesire()
            .name(DEFAULT_NAME)
            .weight(DEFAULT_WEIGHT)
            .probabilityOfWinning(DEFAULT_PROBABILITY_OF_WINNING)
            .maximumWinningTime(DEFAULT_MAXIMUM_WINNING_TIME)
            .isWinningTimeLimited(DEFAULT_IS_WINNING_TIME_LIMITED)
            .awardResultType(DEFAULT_AWARD_RESULT_TYPE)
            .awardReference(DEFAULT_AWARD_REFERENCE)
            .isDefault(DEFAULT_IS_DEFAULT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlDesire createUpdatedEntity() {
        return new AlDesire()
            .name(UPDATED_NAME)
            .weight(UPDATED_WEIGHT)
            .probabilityOfWinning(UPDATED_PROBABILITY_OF_WINNING)
            .maximumWinningTime(UPDATED_MAXIMUM_WINNING_TIME)
            .isWinningTimeLimited(UPDATED_IS_WINNING_TIME_LIMITED)
            .awardResultType(UPDATED_AWARD_RESULT_TYPE)
            .awardReference(UPDATED_AWARD_REFERENCE)
            .isDefault(UPDATED_IS_DEFAULT);
    }

    @BeforeEach
    public void initTest() {
        alDesire = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlDesire != null) {
            alDesireRepository.delete(insertedAlDesire);
            insertedAlDesire = null;
        }
    }

    @Test
    @Transactional
    void createAlDesire() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlDesire
        var returnedAlDesire = om.readValue(
            restAlDesireMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alDesire)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlDesire.class
        );

        // Validate the AlDesire in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlDesireUpdatableFieldsEquals(returnedAlDesire, getPersistedAlDesire(returnedAlDesire));

        insertedAlDesire = returnedAlDesire;
    }

    @Test
    @Transactional
    void createAlDesireWithExistingId() throws Exception {
        // Create the AlDesire with an existing ID
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlDesireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alDesire)))
            .andExpect(status().isBadRequest());

        // Validate the AlDesire in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alDesire.setName(null);

        // Create the AlDesire, which fails.

        restAlDesireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alDesire)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlDesires() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList
        restAlDesireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alDesire.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)))
            .andExpect(jsonPath("$.[*].probabilityOfWinning").value(hasItem(DEFAULT_PROBABILITY_OF_WINNING.doubleValue())))
            .andExpect(jsonPath("$.[*].maximumWinningTime").value(hasItem(DEFAULT_MAXIMUM_WINNING_TIME)))
            .andExpect(jsonPath("$.[*].isWinningTimeLimited").value(hasItem(DEFAULT_IS_WINNING_TIME_LIMITED.booleanValue())))
            .andExpect(jsonPath("$.[*].awardResultType").value(hasItem(DEFAULT_AWARD_RESULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].awardReference").value(hasItem(DEFAULT_AWARD_REFERENCE)))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT.booleanValue())));
    }

    @Test
    @Transactional
    void getAlDesire() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get the alDesire
        restAlDesireMockMvc
            .perform(get(ENTITY_API_URL_ID, alDesire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alDesire.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT))
            .andExpect(jsonPath("$.probabilityOfWinning").value(DEFAULT_PROBABILITY_OF_WINNING.doubleValue()))
            .andExpect(jsonPath("$.maximumWinningTime").value(DEFAULT_MAXIMUM_WINNING_TIME))
            .andExpect(jsonPath("$.isWinningTimeLimited").value(DEFAULT_IS_WINNING_TIME_LIMITED.booleanValue()))
            .andExpect(jsonPath("$.awardResultType").value(DEFAULT_AWARD_RESULT_TYPE.toString()))
            .andExpect(jsonPath("$.awardReference").value(DEFAULT_AWARD_REFERENCE))
            .andExpect(jsonPath("$.isDefault").value(DEFAULT_IS_DEFAULT.booleanValue()));
    }

    @Test
    @Transactional
    void getAlDesiresByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        UUID id = alDesire.getId();

        defaultAlDesireFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllAlDesiresByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where name equals to
        defaultAlDesireFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlDesiresByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where name in
        defaultAlDesireFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlDesiresByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where name is not null
        defaultAlDesireFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAlDesiresByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where name contains
        defaultAlDesireFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlDesiresByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where name does not contain
        defaultAlDesireFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAlDesiresByWeightIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where weight equals to
        defaultAlDesireFiltering("weight.equals=" + DEFAULT_WEIGHT, "weight.equals=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlDesiresByWeightIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where weight in
        defaultAlDesireFiltering("weight.in=" + DEFAULT_WEIGHT + "," + UPDATED_WEIGHT, "weight.in=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlDesiresByWeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where weight is not null
        defaultAlDesireFiltering("weight.specified=true", "weight.specified=false");
    }

    @Test
    @Transactional
    void getAllAlDesiresByWeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where weight is greater than or equal to
        defaultAlDesireFiltering("weight.greaterThanOrEqual=" + DEFAULT_WEIGHT, "weight.greaterThanOrEqual=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlDesiresByWeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where weight is less than or equal to
        defaultAlDesireFiltering("weight.lessThanOrEqual=" + DEFAULT_WEIGHT, "weight.lessThanOrEqual=" + SMALLER_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlDesiresByWeightIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where weight is less than
        defaultAlDesireFiltering("weight.lessThan=" + UPDATED_WEIGHT, "weight.lessThan=" + DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlDesiresByWeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where weight is greater than
        defaultAlDesireFiltering("weight.greaterThan=" + SMALLER_WEIGHT, "weight.greaterThan=" + DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlDesiresByProbabilityOfWinningIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where probabilityOfWinning equals to
        defaultAlDesireFiltering(
            "probabilityOfWinning.equals=" + DEFAULT_PROBABILITY_OF_WINNING,
            "probabilityOfWinning.equals=" + UPDATED_PROBABILITY_OF_WINNING
        );
    }

    @Test
    @Transactional
    void getAllAlDesiresByProbabilityOfWinningIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where probabilityOfWinning in
        defaultAlDesireFiltering(
            "probabilityOfWinning.in=" + DEFAULT_PROBABILITY_OF_WINNING + "," + UPDATED_PROBABILITY_OF_WINNING,
            "probabilityOfWinning.in=" + UPDATED_PROBABILITY_OF_WINNING
        );
    }

    @Test
    @Transactional
    void getAllAlDesiresByProbabilityOfWinningIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where probabilityOfWinning is not null
        defaultAlDesireFiltering("probabilityOfWinning.specified=true", "probabilityOfWinning.specified=false");
    }

    @Test
    @Transactional
    void getAllAlDesiresByProbabilityOfWinningIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where probabilityOfWinning is greater than or equal to
        defaultAlDesireFiltering(
            "probabilityOfWinning.greaterThanOrEqual=" + DEFAULT_PROBABILITY_OF_WINNING,
            "probabilityOfWinning.greaterThanOrEqual=" + UPDATED_PROBABILITY_OF_WINNING
        );
    }

    @Test
    @Transactional
    void getAllAlDesiresByProbabilityOfWinningIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where probabilityOfWinning is less than or equal to
        defaultAlDesireFiltering(
            "probabilityOfWinning.lessThanOrEqual=" + DEFAULT_PROBABILITY_OF_WINNING,
            "probabilityOfWinning.lessThanOrEqual=" + SMALLER_PROBABILITY_OF_WINNING
        );
    }

    @Test
    @Transactional
    void getAllAlDesiresByProbabilityOfWinningIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where probabilityOfWinning is less than
        defaultAlDesireFiltering(
            "probabilityOfWinning.lessThan=" + UPDATED_PROBABILITY_OF_WINNING,
            "probabilityOfWinning.lessThan=" + DEFAULT_PROBABILITY_OF_WINNING
        );
    }

    @Test
    @Transactional
    void getAllAlDesiresByProbabilityOfWinningIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where probabilityOfWinning is greater than
        defaultAlDesireFiltering(
            "probabilityOfWinning.greaterThan=" + SMALLER_PROBABILITY_OF_WINNING,
            "probabilityOfWinning.greaterThan=" + DEFAULT_PROBABILITY_OF_WINNING
        );
    }

    @Test
    @Transactional
    void getAllAlDesiresByMaximumWinningTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where maximumWinningTime equals to
        defaultAlDesireFiltering(
            "maximumWinningTime.equals=" + DEFAULT_MAXIMUM_WINNING_TIME,
            "maximumWinningTime.equals=" + UPDATED_MAXIMUM_WINNING_TIME
        );
    }

    @Test
    @Transactional
    void getAllAlDesiresByMaximumWinningTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where maximumWinningTime in
        defaultAlDesireFiltering(
            "maximumWinningTime.in=" + DEFAULT_MAXIMUM_WINNING_TIME + "," + UPDATED_MAXIMUM_WINNING_TIME,
            "maximumWinningTime.in=" + UPDATED_MAXIMUM_WINNING_TIME
        );
    }

    @Test
    @Transactional
    void getAllAlDesiresByMaximumWinningTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where maximumWinningTime is not null
        defaultAlDesireFiltering("maximumWinningTime.specified=true", "maximumWinningTime.specified=false");
    }

    @Test
    @Transactional
    void getAllAlDesiresByMaximumWinningTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where maximumWinningTime is greater than or equal to
        defaultAlDesireFiltering(
            "maximumWinningTime.greaterThanOrEqual=" + DEFAULT_MAXIMUM_WINNING_TIME,
            "maximumWinningTime.greaterThanOrEqual=" + UPDATED_MAXIMUM_WINNING_TIME
        );
    }

    @Test
    @Transactional
    void getAllAlDesiresByMaximumWinningTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where maximumWinningTime is less than or equal to
        defaultAlDesireFiltering(
            "maximumWinningTime.lessThanOrEqual=" + DEFAULT_MAXIMUM_WINNING_TIME,
            "maximumWinningTime.lessThanOrEqual=" + SMALLER_MAXIMUM_WINNING_TIME
        );
    }

    @Test
    @Transactional
    void getAllAlDesiresByMaximumWinningTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where maximumWinningTime is less than
        defaultAlDesireFiltering(
            "maximumWinningTime.lessThan=" + UPDATED_MAXIMUM_WINNING_TIME,
            "maximumWinningTime.lessThan=" + DEFAULT_MAXIMUM_WINNING_TIME
        );
    }

    @Test
    @Transactional
    void getAllAlDesiresByMaximumWinningTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where maximumWinningTime is greater than
        defaultAlDesireFiltering(
            "maximumWinningTime.greaterThan=" + SMALLER_MAXIMUM_WINNING_TIME,
            "maximumWinningTime.greaterThan=" + DEFAULT_MAXIMUM_WINNING_TIME
        );
    }

    @Test
    @Transactional
    void getAllAlDesiresByIsWinningTimeLimitedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where isWinningTimeLimited equals to
        defaultAlDesireFiltering(
            "isWinningTimeLimited.equals=" + DEFAULT_IS_WINNING_TIME_LIMITED,
            "isWinningTimeLimited.equals=" + UPDATED_IS_WINNING_TIME_LIMITED
        );
    }

    @Test
    @Transactional
    void getAllAlDesiresByIsWinningTimeLimitedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where isWinningTimeLimited in
        defaultAlDesireFiltering(
            "isWinningTimeLimited.in=" + DEFAULT_IS_WINNING_TIME_LIMITED + "," + UPDATED_IS_WINNING_TIME_LIMITED,
            "isWinningTimeLimited.in=" + UPDATED_IS_WINNING_TIME_LIMITED
        );
    }

    @Test
    @Transactional
    void getAllAlDesiresByIsWinningTimeLimitedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where isWinningTimeLimited is not null
        defaultAlDesireFiltering("isWinningTimeLimited.specified=true", "isWinningTimeLimited.specified=false");
    }

    @Test
    @Transactional
    void getAllAlDesiresByAwardResultTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where awardResultType equals to
        defaultAlDesireFiltering(
            "awardResultType.equals=" + DEFAULT_AWARD_RESULT_TYPE,
            "awardResultType.equals=" + UPDATED_AWARD_RESULT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAlDesiresByAwardResultTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where awardResultType in
        defaultAlDesireFiltering(
            "awardResultType.in=" + DEFAULT_AWARD_RESULT_TYPE + "," + UPDATED_AWARD_RESULT_TYPE,
            "awardResultType.in=" + UPDATED_AWARD_RESULT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAlDesiresByAwardResultTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where awardResultType is not null
        defaultAlDesireFiltering("awardResultType.specified=true", "awardResultType.specified=false");
    }

    @Test
    @Transactional
    void getAllAlDesiresByAwardReferenceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where awardReference equals to
        defaultAlDesireFiltering("awardReference.equals=" + DEFAULT_AWARD_REFERENCE, "awardReference.equals=" + UPDATED_AWARD_REFERENCE);
    }

    @Test
    @Transactional
    void getAllAlDesiresByAwardReferenceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where awardReference in
        defaultAlDesireFiltering(
            "awardReference.in=" + DEFAULT_AWARD_REFERENCE + "," + UPDATED_AWARD_REFERENCE,
            "awardReference.in=" + UPDATED_AWARD_REFERENCE
        );
    }

    @Test
    @Transactional
    void getAllAlDesiresByAwardReferenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where awardReference is not null
        defaultAlDesireFiltering("awardReference.specified=true", "awardReference.specified=false");
    }

    @Test
    @Transactional
    void getAllAlDesiresByAwardReferenceContainsSomething() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where awardReference contains
        defaultAlDesireFiltering(
            "awardReference.contains=" + DEFAULT_AWARD_REFERENCE,
            "awardReference.contains=" + UPDATED_AWARD_REFERENCE
        );
    }

    @Test
    @Transactional
    void getAllAlDesiresByAwardReferenceNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where awardReference does not contain
        defaultAlDesireFiltering(
            "awardReference.doesNotContain=" + UPDATED_AWARD_REFERENCE,
            "awardReference.doesNotContain=" + DEFAULT_AWARD_REFERENCE
        );
    }

    @Test
    @Transactional
    void getAllAlDesiresByIsDefaultIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where isDefault equals to
        defaultAlDesireFiltering("isDefault.equals=" + DEFAULT_IS_DEFAULT, "isDefault.equals=" + UPDATED_IS_DEFAULT);
    }

    @Test
    @Transactional
    void getAllAlDesiresByIsDefaultIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where isDefault in
        defaultAlDesireFiltering("isDefault.in=" + DEFAULT_IS_DEFAULT + "," + UPDATED_IS_DEFAULT, "isDefault.in=" + UPDATED_IS_DEFAULT);
    }

    @Test
    @Transactional
    void getAllAlDesiresByIsDefaultIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        // Get all the alDesireList where isDefault is not null
        defaultAlDesireFiltering("isDefault.specified=true", "isDefault.specified=false");
    }

    @Test
    @Transactional
    void getAllAlDesiresByImageIsEqualToSomething() throws Exception {
        Metaverse image;
        if (TestUtil.findAll(em, Metaverse.class).isEmpty()) {
            alDesireRepository.saveAndFlush(alDesire);
            image = MetaverseResourceIT.createEntity();
        } else {
            image = TestUtil.findAll(em, Metaverse.class).get(0);
        }
        em.persist(image);
        em.flush();
        alDesire.setImage(image);
        alDesireRepository.saveAndFlush(alDesire);
        Long imageId = image.getId();
        // Get all the alDesireList where image equals to imageId
        defaultAlDesireShouldBeFound("imageId.equals=" + imageId);

        // Get all the alDesireList where image equals to (imageId + 1)
        defaultAlDesireShouldNotBeFound("imageId.equals=" + (imageId + 1));
    }

    @Test
    @Transactional
    void getAllAlDesiresByMaggiIsEqualToSomething() throws Exception {
        AlLeandro maggi;
        if (TestUtil.findAll(em, AlLeandro.class).isEmpty()) {
            alDesireRepository.saveAndFlush(alDesire);
            maggi = AlLeandroResourceIT.createEntity();
        } else {
            maggi = TestUtil.findAll(em, AlLeandro.class).get(0);
        }
        em.persist(maggi);
        em.flush();
        alDesire.setMaggi(maggi);
        alDesireRepository.saveAndFlush(alDesire);
        UUID maggiId = maggi.getId();
        // Get all the alDesireList where maggi equals to maggiId
        defaultAlDesireShouldBeFound("maggiId.equals=" + maggiId);

        // Get all the alDesireList where maggi equals to UUID.randomUUID()
        defaultAlDesireShouldNotBeFound("maggiId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlDesiresByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alDesireRepository.saveAndFlush(alDesire);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alDesire.setApplication(application);
        alDesireRepository.saveAndFlush(alDesire);
        UUID applicationId = application.getId();
        // Get all the alDesireList where application equals to applicationId
        defaultAlDesireShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alDesireList where application equals to UUID.randomUUID()
        defaultAlDesireShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlDesireFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlDesireShouldBeFound(shouldBeFound);
        defaultAlDesireShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlDesireShouldBeFound(String filter) throws Exception {
        restAlDesireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alDesire.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)))
            .andExpect(jsonPath("$.[*].probabilityOfWinning").value(hasItem(DEFAULT_PROBABILITY_OF_WINNING.doubleValue())))
            .andExpect(jsonPath("$.[*].maximumWinningTime").value(hasItem(DEFAULT_MAXIMUM_WINNING_TIME)))
            .andExpect(jsonPath("$.[*].isWinningTimeLimited").value(hasItem(DEFAULT_IS_WINNING_TIME_LIMITED.booleanValue())))
            .andExpect(jsonPath("$.[*].awardResultType").value(hasItem(DEFAULT_AWARD_RESULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].awardReference").value(hasItem(DEFAULT_AWARD_REFERENCE)))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT.booleanValue())));

        // Check, that the count call also returns 1
        restAlDesireMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlDesireShouldNotBeFound(String filter) throws Exception {
        restAlDesireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlDesireMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlDesire() throws Exception {
        // Get the alDesire
        restAlDesireMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlDesire() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alDesire
        AlDesire updatedAlDesire = alDesireRepository.findById(alDesire.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlDesire are not directly saved in db
        em.detach(updatedAlDesire);
        updatedAlDesire
            .name(UPDATED_NAME)
            .weight(UPDATED_WEIGHT)
            .probabilityOfWinning(UPDATED_PROBABILITY_OF_WINNING)
            .maximumWinningTime(UPDATED_MAXIMUM_WINNING_TIME)
            .isWinningTimeLimited(UPDATED_IS_WINNING_TIME_LIMITED)
            .awardResultType(UPDATED_AWARD_RESULT_TYPE)
            .awardReference(UPDATED_AWARD_REFERENCE)
            .isDefault(UPDATED_IS_DEFAULT);

        restAlDesireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlDesire.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlDesire))
            )
            .andExpect(status().isOk());

        // Validate the AlDesire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlDesireToMatchAllProperties(updatedAlDesire);
    }

    @Test
    @Transactional
    void putNonExistingAlDesire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alDesire.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlDesireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alDesire.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alDesire))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlDesire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlDesire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alDesire.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlDesireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alDesire))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlDesire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlDesire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alDesire.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlDesireMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alDesire)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlDesire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlDesireWithPatch() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alDesire using partial update
        AlDesire partialUpdatedAlDesire = new AlDesire();
        partialUpdatedAlDesire.setId(alDesire.getId());

        partialUpdatedAlDesire
            .name(UPDATED_NAME)
            .probabilityOfWinning(UPDATED_PROBABILITY_OF_WINNING)
            .isWinningTimeLimited(UPDATED_IS_WINNING_TIME_LIMITED)
            .awardReference(UPDATED_AWARD_REFERENCE);

        restAlDesireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlDesire.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlDesire))
            )
            .andExpect(status().isOk());

        // Validate the AlDesire in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlDesireUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAlDesire, alDesire), getPersistedAlDesire(alDesire));
    }

    @Test
    @Transactional
    void fullUpdateAlDesireWithPatch() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alDesire using partial update
        AlDesire partialUpdatedAlDesire = new AlDesire();
        partialUpdatedAlDesire.setId(alDesire.getId());

        partialUpdatedAlDesire
            .name(UPDATED_NAME)
            .weight(UPDATED_WEIGHT)
            .probabilityOfWinning(UPDATED_PROBABILITY_OF_WINNING)
            .maximumWinningTime(UPDATED_MAXIMUM_WINNING_TIME)
            .isWinningTimeLimited(UPDATED_IS_WINNING_TIME_LIMITED)
            .awardResultType(UPDATED_AWARD_RESULT_TYPE)
            .awardReference(UPDATED_AWARD_REFERENCE)
            .isDefault(UPDATED_IS_DEFAULT);

        restAlDesireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlDesire.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlDesire))
            )
            .andExpect(status().isOk());

        // Validate the AlDesire in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlDesireUpdatableFieldsEquals(partialUpdatedAlDesire, getPersistedAlDesire(partialUpdatedAlDesire));
    }

    @Test
    @Transactional
    void patchNonExistingAlDesire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alDesire.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlDesireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alDesire.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alDesire))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlDesire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlDesire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alDesire.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlDesireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alDesire))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlDesire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlDesire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alDesire.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlDesireMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alDesire)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlDesire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlDesire() throws Exception {
        // Initialize the database
        insertedAlDesire = alDesireRepository.saveAndFlush(alDesire);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alDesire
        restAlDesireMockMvc
            .perform(delete(ENTITY_API_URL_ID, alDesire.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alDesireRepository.count();
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

    protected AlDesire getPersistedAlDesire(AlDesire alDesire) {
        return alDesireRepository.findById(alDesire.getId()).orElseThrow();
    }

    protected void assertPersistedAlDesireToMatchAllProperties(AlDesire expectedAlDesire) {
        assertAlDesireAllPropertiesEquals(expectedAlDesire, getPersistedAlDesire(expectedAlDesire));
    }

    protected void assertPersistedAlDesireToMatchUpdatableProperties(AlDesire expectedAlDesire) {
        assertAlDesireAllUpdatablePropertiesEquals(expectedAlDesire, getPersistedAlDesire(expectedAlDesire));
    }
}
