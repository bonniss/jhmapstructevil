package ai.realworld.web.rest;

import static ai.realworld.domain.AlDesireViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlDesireVi;
import ai.realworld.domain.AlLeandro;
import ai.realworld.domain.Metaverse;
import ai.realworld.domain.enumeration.FooGameAward;
import ai.realworld.repository.AlDesireViRepository;
import ai.realworld.service.dto.AlDesireViDTO;
import ai.realworld.service.mapper.AlDesireViMapper;
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
 * Integration tests for the {@link AlDesireViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlDesireViResourceIT {

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

    private static final String ENTITY_API_URL = "/api/al-desire-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlDesireViRepository alDesireViRepository;

    @Autowired
    private AlDesireViMapper alDesireViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlDesireViMockMvc;

    private AlDesireVi alDesireVi;

    private AlDesireVi insertedAlDesireVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlDesireVi createEntity() {
        return new AlDesireVi()
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
    public static AlDesireVi createUpdatedEntity() {
        return new AlDesireVi()
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
        alDesireVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlDesireVi != null) {
            alDesireViRepository.delete(insertedAlDesireVi);
            insertedAlDesireVi = null;
        }
    }

    @Test
    @Transactional
    void createAlDesireVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlDesireVi
        AlDesireViDTO alDesireViDTO = alDesireViMapper.toDto(alDesireVi);
        var returnedAlDesireViDTO = om.readValue(
            restAlDesireViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alDesireViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlDesireViDTO.class
        );

        // Validate the AlDesireVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlDesireVi = alDesireViMapper.toEntity(returnedAlDesireViDTO);
        assertAlDesireViUpdatableFieldsEquals(returnedAlDesireVi, getPersistedAlDesireVi(returnedAlDesireVi));

        insertedAlDesireVi = returnedAlDesireVi;
    }

    @Test
    @Transactional
    void createAlDesireViWithExistingId() throws Exception {
        // Create the AlDesireVi with an existing ID
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);
        AlDesireViDTO alDesireViDTO = alDesireViMapper.toDto(alDesireVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlDesireViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alDesireViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlDesireVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alDesireVi.setName(null);

        // Create the AlDesireVi, which fails.
        AlDesireViDTO alDesireViDTO = alDesireViMapper.toDto(alDesireVi);

        restAlDesireViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alDesireViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlDesireVis() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList
        restAlDesireViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alDesireVi.getId().toString())))
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
    void getAlDesireVi() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get the alDesireVi
        restAlDesireViMockMvc
            .perform(get(ENTITY_API_URL_ID, alDesireVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alDesireVi.getId().toString()))
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
    void getAlDesireVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        UUID id = alDesireVi.getId();

        defaultAlDesireViFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllAlDesireVisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where name equals to
        defaultAlDesireViFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlDesireVisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where name in
        defaultAlDesireViFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlDesireVisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where name is not null
        defaultAlDesireViFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAlDesireVisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where name contains
        defaultAlDesireViFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlDesireVisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where name does not contain
        defaultAlDesireViFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAlDesireVisByWeightIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where weight equals to
        defaultAlDesireViFiltering("weight.equals=" + DEFAULT_WEIGHT, "weight.equals=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlDesireVisByWeightIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where weight in
        defaultAlDesireViFiltering("weight.in=" + DEFAULT_WEIGHT + "," + UPDATED_WEIGHT, "weight.in=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlDesireVisByWeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where weight is not null
        defaultAlDesireViFiltering("weight.specified=true", "weight.specified=false");
    }

    @Test
    @Transactional
    void getAllAlDesireVisByWeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where weight is greater than or equal to
        defaultAlDesireViFiltering("weight.greaterThanOrEqual=" + DEFAULT_WEIGHT, "weight.greaterThanOrEqual=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlDesireVisByWeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where weight is less than or equal to
        defaultAlDesireViFiltering("weight.lessThanOrEqual=" + DEFAULT_WEIGHT, "weight.lessThanOrEqual=" + SMALLER_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlDesireVisByWeightIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where weight is less than
        defaultAlDesireViFiltering("weight.lessThan=" + UPDATED_WEIGHT, "weight.lessThan=" + DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlDesireVisByWeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where weight is greater than
        defaultAlDesireViFiltering("weight.greaterThan=" + SMALLER_WEIGHT, "weight.greaterThan=" + DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlDesireVisByProbabilityOfWinningIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where probabilityOfWinning equals to
        defaultAlDesireViFiltering(
            "probabilityOfWinning.equals=" + DEFAULT_PROBABILITY_OF_WINNING,
            "probabilityOfWinning.equals=" + UPDATED_PROBABILITY_OF_WINNING
        );
    }

    @Test
    @Transactional
    void getAllAlDesireVisByProbabilityOfWinningIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where probabilityOfWinning in
        defaultAlDesireViFiltering(
            "probabilityOfWinning.in=" + DEFAULT_PROBABILITY_OF_WINNING + "," + UPDATED_PROBABILITY_OF_WINNING,
            "probabilityOfWinning.in=" + UPDATED_PROBABILITY_OF_WINNING
        );
    }

    @Test
    @Transactional
    void getAllAlDesireVisByProbabilityOfWinningIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where probabilityOfWinning is not null
        defaultAlDesireViFiltering("probabilityOfWinning.specified=true", "probabilityOfWinning.specified=false");
    }

    @Test
    @Transactional
    void getAllAlDesireVisByProbabilityOfWinningIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where probabilityOfWinning is greater than or equal to
        defaultAlDesireViFiltering(
            "probabilityOfWinning.greaterThanOrEqual=" + DEFAULT_PROBABILITY_OF_WINNING,
            "probabilityOfWinning.greaterThanOrEqual=" + UPDATED_PROBABILITY_OF_WINNING
        );
    }

    @Test
    @Transactional
    void getAllAlDesireVisByProbabilityOfWinningIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where probabilityOfWinning is less than or equal to
        defaultAlDesireViFiltering(
            "probabilityOfWinning.lessThanOrEqual=" + DEFAULT_PROBABILITY_OF_WINNING,
            "probabilityOfWinning.lessThanOrEqual=" + SMALLER_PROBABILITY_OF_WINNING
        );
    }

    @Test
    @Transactional
    void getAllAlDesireVisByProbabilityOfWinningIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where probabilityOfWinning is less than
        defaultAlDesireViFiltering(
            "probabilityOfWinning.lessThan=" + UPDATED_PROBABILITY_OF_WINNING,
            "probabilityOfWinning.lessThan=" + DEFAULT_PROBABILITY_OF_WINNING
        );
    }

    @Test
    @Transactional
    void getAllAlDesireVisByProbabilityOfWinningIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where probabilityOfWinning is greater than
        defaultAlDesireViFiltering(
            "probabilityOfWinning.greaterThan=" + SMALLER_PROBABILITY_OF_WINNING,
            "probabilityOfWinning.greaterThan=" + DEFAULT_PROBABILITY_OF_WINNING
        );
    }

    @Test
    @Transactional
    void getAllAlDesireVisByMaximumWinningTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where maximumWinningTime equals to
        defaultAlDesireViFiltering(
            "maximumWinningTime.equals=" + DEFAULT_MAXIMUM_WINNING_TIME,
            "maximumWinningTime.equals=" + UPDATED_MAXIMUM_WINNING_TIME
        );
    }

    @Test
    @Transactional
    void getAllAlDesireVisByMaximumWinningTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where maximumWinningTime in
        defaultAlDesireViFiltering(
            "maximumWinningTime.in=" + DEFAULT_MAXIMUM_WINNING_TIME + "," + UPDATED_MAXIMUM_WINNING_TIME,
            "maximumWinningTime.in=" + UPDATED_MAXIMUM_WINNING_TIME
        );
    }

    @Test
    @Transactional
    void getAllAlDesireVisByMaximumWinningTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where maximumWinningTime is not null
        defaultAlDesireViFiltering("maximumWinningTime.specified=true", "maximumWinningTime.specified=false");
    }

    @Test
    @Transactional
    void getAllAlDesireVisByMaximumWinningTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where maximumWinningTime is greater than or equal to
        defaultAlDesireViFiltering(
            "maximumWinningTime.greaterThanOrEqual=" + DEFAULT_MAXIMUM_WINNING_TIME,
            "maximumWinningTime.greaterThanOrEqual=" + UPDATED_MAXIMUM_WINNING_TIME
        );
    }

    @Test
    @Transactional
    void getAllAlDesireVisByMaximumWinningTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where maximumWinningTime is less than or equal to
        defaultAlDesireViFiltering(
            "maximumWinningTime.lessThanOrEqual=" + DEFAULT_MAXIMUM_WINNING_TIME,
            "maximumWinningTime.lessThanOrEqual=" + SMALLER_MAXIMUM_WINNING_TIME
        );
    }

    @Test
    @Transactional
    void getAllAlDesireVisByMaximumWinningTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where maximumWinningTime is less than
        defaultAlDesireViFiltering(
            "maximumWinningTime.lessThan=" + UPDATED_MAXIMUM_WINNING_TIME,
            "maximumWinningTime.lessThan=" + DEFAULT_MAXIMUM_WINNING_TIME
        );
    }

    @Test
    @Transactional
    void getAllAlDesireVisByMaximumWinningTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where maximumWinningTime is greater than
        defaultAlDesireViFiltering(
            "maximumWinningTime.greaterThan=" + SMALLER_MAXIMUM_WINNING_TIME,
            "maximumWinningTime.greaterThan=" + DEFAULT_MAXIMUM_WINNING_TIME
        );
    }

    @Test
    @Transactional
    void getAllAlDesireVisByIsWinningTimeLimitedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where isWinningTimeLimited equals to
        defaultAlDesireViFiltering(
            "isWinningTimeLimited.equals=" + DEFAULT_IS_WINNING_TIME_LIMITED,
            "isWinningTimeLimited.equals=" + UPDATED_IS_WINNING_TIME_LIMITED
        );
    }

    @Test
    @Transactional
    void getAllAlDesireVisByIsWinningTimeLimitedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where isWinningTimeLimited in
        defaultAlDesireViFiltering(
            "isWinningTimeLimited.in=" + DEFAULT_IS_WINNING_TIME_LIMITED + "," + UPDATED_IS_WINNING_TIME_LIMITED,
            "isWinningTimeLimited.in=" + UPDATED_IS_WINNING_TIME_LIMITED
        );
    }

    @Test
    @Transactional
    void getAllAlDesireVisByIsWinningTimeLimitedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where isWinningTimeLimited is not null
        defaultAlDesireViFiltering("isWinningTimeLimited.specified=true", "isWinningTimeLimited.specified=false");
    }

    @Test
    @Transactional
    void getAllAlDesireVisByAwardResultTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where awardResultType equals to
        defaultAlDesireViFiltering(
            "awardResultType.equals=" + DEFAULT_AWARD_RESULT_TYPE,
            "awardResultType.equals=" + UPDATED_AWARD_RESULT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAlDesireVisByAwardResultTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where awardResultType in
        defaultAlDesireViFiltering(
            "awardResultType.in=" + DEFAULT_AWARD_RESULT_TYPE + "," + UPDATED_AWARD_RESULT_TYPE,
            "awardResultType.in=" + UPDATED_AWARD_RESULT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAlDesireVisByAwardResultTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where awardResultType is not null
        defaultAlDesireViFiltering("awardResultType.specified=true", "awardResultType.specified=false");
    }

    @Test
    @Transactional
    void getAllAlDesireVisByAwardReferenceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where awardReference equals to
        defaultAlDesireViFiltering("awardReference.equals=" + DEFAULT_AWARD_REFERENCE, "awardReference.equals=" + UPDATED_AWARD_REFERENCE);
    }

    @Test
    @Transactional
    void getAllAlDesireVisByAwardReferenceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where awardReference in
        defaultAlDesireViFiltering(
            "awardReference.in=" + DEFAULT_AWARD_REFERENCE + "," + UPDATED_AWARD_REFERENCE,
            "awardReference.in=" + UPDATED_AWARD_REFERENCE
        );
    }

    @Test
    @Transactional
    void getAllAlDesireVisByAwardReferenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where awardReference is not null
        defaultAlDesireViFiltering("awardReference.specified=true", "awardReference.specified=false");
    }

    @Test
    @Transactional
    void getAllAlDesireVisByAwardReferenceContainsSomething() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where awardReference contains
        defaultAlDesireViFiltering(
            "awardReference.contains=" + DEFAULT_AWARD_REFERENCE,
            "awardReference.contains=" + UPDATED_AWARD_REFERENCE
        );
    }

    @Test
    @Transactional
    void getAllAlDesireVisByAwardReferenceNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where awardReference does not contain
        defaultAlDesireViFiltering(
            "awardReference.doesNotContain=" + UPDATED_AWARD_REFERENCE,
            "awardReference.doesNotContain=" + DEFAULT_AWARD_REFERENCE
        );
    }

    @Test
    @Transactional
    void getAllAlDesireVisByIsDefaultIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where isDefault equals to
        defaultAlDesireViFiltering("isDefault.equals=" + DEFAULT_IS_DEFAULT, "isDefault.equals=" + UPDATED_IS_DEFAULT);
    }

    @Test
    @Transactional
    void getAllAlDesireVisByIsDefaultIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where isDefault in
        defaultAlDesireViFiltering("isDefault.in=" + DEFAULT_IS_DEFAULT + "," + UPDATED_IS_DEFAULT, "isDefault.in=" + UPDATED_IS_DEFAULT);
    }

    @Test
    @Transactional
    void getAllAlDesireVisByIsDefaultIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        // Get all the alDesireViList where isDefault is not null
        defaultAlDesireViFiltering("isDefault.specified=true", "isDefault.specified=false");
    }

    @Test
    @Transactional
    void getAllAlDesireVisByImageIsEqualToSomething() throws Exception {
        Metaverse image;
        if (TestUtil.findAll(em, Metaverse.class).isEmpty()) {
            alDesireViRepository.saveAndFlush(alDesireVi);
            image = MetaverseResourceIT.createEntity();
        } else {
            image = TestUtil.findAll(em, Metaverse.class).get(0);
        }
        em.persist(image);
        em.flush();
        alDesireVi.setImage(image);
        alDesireViRepository.saveAndFlush(alDesireVi);
        Long imageId = image.getId();
        // Get all the alDesireViList where image equals to imageId
        defaultAlDesireViShouldBeFound("imageId.equals=" + imageId);

        // Get all the alDesireViList where image equals to (imageId + 1)
        defaultAlDesireViShouldNotBeFound("imageId.equals=" + (imageId + 1));
    }

    @Test
    @Transactional
    void getAllAlDesireVisByMaggiIsEqualToSomething() throws Exception {
        AlLeandro maggi;
        if (TestUtil.findAll(em, AlLeandro.class).isEmpty()) {
            alDesireViRepository.saveAndFlush(alDesireVi);
            maggi = AlLeandroResourceIT.createEntity();
        } else {
            maggi = TestUtil.findAll(em, AlLeandro.class).get(0);
        }
        em.persist(maggi);
        em.flush();
        alDesireVi.setMaggi(maggi);
        alDesireViRepository.saveAndFlush(alDesireVi);
        UUID maggiId = maggi.getId();
        // Get all the alDesireViList where maggi equals to maggiId
        defaultAlDesireViShouldBeFound("maggiId.equals=" + maggiId);

        // Get all the alDesireViList where maggi equals to UUID.randomUUID()
        defaultAlDesireViShouldNotBeFound("maggiId.equals=" + UUID.randomUUID());
    }

    private void defaultAlDesireViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlDesireViShouldBeFound(shouldBeFound);
        defaultAlDesireViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlDesireViShouldBeFound(String filter) throws Exception {
        restAlDesireViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alDesireVi.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)))
            .andExpect(jsonPath("$.[*].probabilityOfWinning").value(hasItem(DEFAULT_PROBABILITY_OF_WINNING.doubleValue())))
            .andExpect(jsonPath("$.[*].maximumWinningTime").value(hasItem(DEFAULT_MAXIMUM_WINNING_TIME)))
            .andExpect(jsonPath("$.[*].isWinningTimeLimited").value(hasItem(DEFAULT_IS_WINNING_TIME_LIMITED.booleanValue())))
            .andExpect(jsonPath("$.[*].awardResultType").value(hasItem(DEFAULT_AWARD_RESULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].awardReference").value(hasItem(DEFAULT_AWARD_REFERENCE)))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT.booleanValue())));

        // Check, that the count call also returns 1
        restAlDesireViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlDesireViShouldNotBeFound(String filter) throws Exception {
        restAlDesireViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlDesireViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlDesireVi() throws Exception {
        // Get the alDesireVi
        restAlDesireViMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlDesireVi() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alDesireVi
        AlDesireVi updatedAlDesireVi = alDesireViRepository.findById(alDesireVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlDesireVi are not directly saved in db
        em.detach(updatedAlDesireVi);
        updatedAlDesireVi
            .name(UPDATED_NAME)
            .weight(UPDATED_WEIGHT)
            .probabilityOfWinning(UPDATED_PROBABILITY_OF_WINNING)
            .maximumWinningTime(UPDATED_MAXIMUM_WINNING_TIME)
            .isWinningTimeLimited(UPDATED_IS_WINNING_TIME_LIMITED)
            .awardResultType(UPDATED_AWARD_RESULT_TYPE)
            .awardReference(UPDATED_AWARD_REFERENCE)
            .isDefault(UPDATED_IS_DEFAULT);
        AlDesireViDTO alDesireViDTO = alDesireViMapper.toDto(updatedAlDesireVi);

        restAlDesireViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alDesireViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alDesireViDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlDesireVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlDesireViToMatchAllProperties(updatedAlDesireVi);
    }

    @Test
    @Transactional
    void putNonExistingAlDesireVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alDesireVi.setId(UUID.randomUUID());

        // Create the AlDesireVi
        AlDesireViDTO alDesireViDTO = alDesireViMapper.toDto(alDesireVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlDesireViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alDesireViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alDesireViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlDesireVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlDesireVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alDesireVi.setId(UUID.randomUUID());

        // Create the AlDesireVi
        AlDesireViDTO alDesireViDTO = alDesireViMapper.toDto(alDesireVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlDesireViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alDesireViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlDesireVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlDesireVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alDesireVi.setId(UUID.randomUUID());

        // Create the AlDesireVi
        AlDesireViDTO alDesireViDTO = alDesireViMapper.toDto(alDesireVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlDesireViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alDesireViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlDesireVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlDesireViWithPatch() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alDesireVi using partial update
        AlDesireVi partialUpdatedAlDesireVi = new AlDesireVi();
        partialUpdatedAlDesireVi.setId(alDesireVi.getId());

        partialUpdatedAlDesireVi.weight(UPDATED_WEIGHT).awardResultType(UPDATED_AWARD_RESULT_TYPE).isDefault(UPDATED_IS_DEFAULT);

        restAlDesireViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlDesireVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlDesireVi))
            )
            .andExpect(status().isOk());

        // Validate the AlDesireVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlDesireViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlDesireVi, alDesireVi),
            getPersistedAlDesireVi(alDesireVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlDesireViWithPatch() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alDesireVi using partial update
        AlDesireVi partialUpdatedAlDesireVi = new AlDesireVi();
        partialUpdatedAlDesireVi.setId(alDesireVi.getId());

        partialUpdatedAlDesireVi
            .name(UPDATED_NAME)
            .weight(UPDATED_WEIGHT)
            .probabilityOfWinning(UPDATED_PROBABILITY_OF_WINNING)
            .maximumWinningTime(UPDATED_MAXIMUM_WINNING_TIME)
            .isWinningTimeLimited(UPDATED_IS_WINNING_TIME_LIMITED)
            .awardResultType(UPDATED_AWARD_RESULT_TYPE)
            .awardReference(UPDATED_AWARD_REFERENCE)
            .isDefault(UPDATED_IS_DEFAULT);

        restAlDesireViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlDesireVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlDesireVi))
            )
            .andExpect(status().isOk());

        // Validate the AlDesireVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlDesireViUpdatableFieldsEquals(partialUpdatedAlDesireVi, getPersistedAlDesireVi(partialUpdatedAlDesireVi));
    }

    @Test
    @Transactional
    void patchNonExistingAlDesireVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alDesireVi.setId(UUID.randomUUID());

        // Create the AlDesireVi
        AlDesireViDTO alDesireViDTO = alDesireViMapper.toDto(alDesireVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlDesireViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alDesireViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alDesireViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlDesireVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlDesireVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alDesireVi.setId(UUID.randomUUID());

        // Create the AlDesireVi
        AlDesireViDTO alDesireViDTO = alDesireViMapper.toDto(alDesireVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlDesireViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alDesireViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlDesireVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlDesireVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alDesireVi.setId(UUID.randomUUID());

        // Create the AlDesireVi
        AlDesireViDTO alDesireViDTO = alDesireViMapper.toDto(alDesireVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlDesireViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alDesireViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlDesireVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlDesireVi() throws Exception {
        // Initialize the database
        insertedAlDesireVi = alDesireViRepository.saveAndFlush(alDesireVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alDesireVi
        restAlDesireViMockMvc
            .perform(delete(ENTITY_API_URL_ID, alDesireVi.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alDesireViRepository.count();
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

    protected AlDesireVi getPersistedAlDesireVi(AlDesireVi alDesireVi) {
        return alDesireViRepository.findById(alDesireVi.getId()).orElseThrow();
    }

    protected void assertPersistedAlDesireViToMatchAllProperties(AlDesireVi expectedAlDesireVi) {
        assertAlDesireViAllPropertiesEquals(expectedAlDesireVi, getPersistedAlDesireVi(expectedAlDesireVi));
    }

    protected void assertPersistedAlDesireViToMatchUpdatableProperties(AlDesireVi expectedAlDesireVi) {
        assertAlDesireViAllUpdatablePropertiesEquals(expectedAlDesireVi, getPersistedAlDesireVi(expectedAlDesireVi));
    }
}
