package ai.realworld.web.rest;

import static ai.realworld.domain.AlPacinoAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlMemTier;
import ai.realworld.domain.AlPacino;
import ai.realworld.domain.AlVueVueUsage;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.enumeration.AcquisitionCanal;
import ai.realworld.domain.enumeration.TyrantSex;
import ai.realworld.repository.AlPacinoRepository;
import ai.realworld.service.dto.AlPacinoDTO;
import ai.realworld.service.mapper.AlPacinoMapper;
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
 * Integration tests for the {@link AlPacinoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlPacinoResourceIT {

    private static final String DEFAULT_PLATFORM_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PLATFORM_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PLATFORM_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_PLATFORM_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PLATFORM_AVATAR_URL = "AAAAAAAAAA";
    private static final String UPDATED_PLATFORM_AVATAR_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_SENSITIVE = false;
    private static final Boolean UPDATED_IS_SENSITIVE = true;

    private static final String DEFAULT_FAMILY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FAMILY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GIVEN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GIVEN_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DOB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DOB = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DOB = LocalDate.ofEpochDay(-1L);

    private static final TyrantSex DEFAULT_GENDER = TyrantSex.MALE;
    private static final TyrantSex UPDATED_GENDER = TyrantSex.FEMALE;

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "R9@Xe)9v._=l'";
    private static final String UPDATED_EMAIL = "2?\"SY@ENc;V.5ZcB5S";

    private static final AcquisitionCanal DEFAULT_ACQUIRED_FROM = AcquisitionCanal.JELLO;
    private static final AcquisitionCanal UPDATED_ACQUIRED_FROM = AcquisitionCanal.OFFLINE;

    private static final Integer DEFAULT_CURRENT_POINTS = 1;
    private static final Integer UPDATED_CURRENT_POINTS = 2;
    private static final Integer SMALLER_CURRENT_POINTS = 1 - 1;

    private static final Integer DEFAULT_TOTAL_POINTS = 1;
    private static final Integer UPDATED_TOTAL_POINTS = 2;
    private static final Integer SMALLER_TOTAL_POINTS = 1 - 1;

    private static final Boolean DEFAULT_IS_FOLLOWING = false;
    private static final Boolean UPDATED_IS_FOLLOWING = true;

    private static final Boolean DEFAULT_IS_ENABLED = false;
    private static final Boolean UPDATED_IS_ENABLED = true;

    private static final String ENTITY_API_URL = "/api/al-pacinos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlPacinoRepository alPacinoRepository;

    @Autowired
    private AlPacinoMapper alPacinoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlPacinoMockMvc;

    private AlPacino alPacino;

    private AlPacino insertedAlPacino;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPacino createEntity() {
        return new AlPacino()
            .platformCode(DEFAULT_PLATFORM_CODE)
            .platformUsername(DEFAULT_PLATFORM_USERNAME)
            .platformAvatarUrl(DEFAULT_PLATFORM_AVATAR_URL)
            .isSensitive(DEFAULT_IS_SENSITIVE)
            .familyName(DEFAULT_FAMILY_NAME)
            .givenName(DEFAULT_GIVEN_NAME)
            .dob(DEFAULT_DOB)
            .gender(DEFAULT_GENDER)
            .phone(DEFAULT_PHONE)
            .email(DEFAULT_EMAIL)
            .acquiredFrom(DEFAULT_ACQUIRED_FROM)
            .currentPoints(DEFAULT_CURRENT_POINTS)
            .totalPoints(DEFAULT_TOTAL_POINTS)
            .isFollowing(DEFAULT_IS_FOLLOWING)
            .isEnabled(DEFAULT_IS_ENABLED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPacino createUpdatedEntity() {
        return new AlPacino()
            .platformCode(UPDATED_PLATFORM_CODE)
            .platformUsername(UPDATED_PLATFORM_USERNAME)
            .platformAvatarUrl(UPDATED_PLATFORM_AVATAR_URL)
            .isSensitive(UPDATED_IS_SENSITIVE)
            .familyName(UPDATED_FAMILY_NAME)
            .givenName(UPDATED_GIVEN_NAME)
            .dob(UPDATED_DOB)
            .gender(UPDATED_GENDER)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .acquiredFrom(UPDATED_ACQUIRED_FROM)
            .currentPoints(UPDATED_CURRENT_POINTS)
            .totalPoints(UPDATED_TOTAL_POINTS)
            .isFollowing(UPDATED_IS_FOLLOWING)
            .isEnabled(UPDATED_IS_ENABLED);
    }

    @BeforeEach
    public void initTest() {
        alPacino = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlPacino != null) {
            alPacinoRepository.delete(insertedAlPacino);
            insertedAlPacino = null;
        }
    }

    @Test
    @Transactional
    void createAlPacino() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlPacino
        AlPacinoDTO alPacinoDTO = alPacinoMapper.toDto(alPacino);
        var returnedAlPacinoDTO = om.readValue(
            restAlPacinoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPacinoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlPacinoDTO.class
        );

        // Validate the AlPacino in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlPacino = alPacinoMapper.toEntity(returnedAlPacinoDTO);
        assertAlPacinoUpdatableFieldsEquals(returnedAlPacino, getPersistedAlPacino(returnedAlPacino));

        insertedAlPacino = returnedAlPacino;
    }

    @Test
    @Transactional
    void createAlPacinoWithExistingId() throws Exception {
        // Create the AlPacino with an existing ID
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);
        AlPacinoDTO alPacinoDTO = alPacinoMapper.toDto(alPacino);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlPacinoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPacinoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlPacino in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAlPacinos() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList
        restAlPacinoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPacino.getId().toString())))
            .andExpect(jsonPath("$.[*].platformCode").value(hasItem(DEFAULT_PLATFORM_CODE)))
            .andExpect(jsonPath("$.[*].platformUsername").value(hasItem(DEFAULT_PLATFORM_USERNAME)))
            .andExpect(jsonPath("$.[*].platformAvatarUrl").value(hasItem(DEFAULT_PLATFORM_AVATAR_URL)))
            .andExpect(jsonPath("$.[*].isSensitive").value(hasItem(DEFAULT_IS_SENSITIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].familyName").value(hasItem(DEFAULT_FAMILY_NAME)))
            .andExpect(jsonPath("$.[*].givenName").value(hasItem(DEFAULT_GIVEN_NAME)))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].acquiredFrom").value(hasItem(DEFAULT_ACQUIRED_FROM.toString())))
            .andExpect(jsonPath("$.[*].currentPoints").value(hasItem(DEFAULT_CURRENT_POINTS)))
            .andExpect(jsonPath("$.[*].totalPoints").value(hasItem(DEFAULT_TOTAL_POINTS)))
            .andExpect(jsonPath("$.[*].isFollowing").value(hasItem(DEFAULT_IS_FOLLOWING.booleanValue())))
            .andExpect(jsonPath("$.[*].isEnabled").value(hasItem(DEFAULT_IS_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    void getAlPacino() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get the alPacino
        restAlPacinoMockMvc
            .perform(get(ENTITY_API_URL_ID, alPacino.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alPacino.getId().toString()))
            .andExpect(jsonPath("$.platformCode").value(DEFAULT_PLATFORM_CODE))
            .andExpect(jsonPath("$.platformUsername").value(DEFAULT_PLATFORM_USERNAME))
            .andExpect(jsonPath("$.platformAvatarUrl").value(DEFAULT_PLATFORM_AVATAR_URL))
            .andExpect(jsonPath("$.isSensitive").value(DEFAULT_IS_SENSITIVE.booleanValue()))
            .andExpect(jsonPath("$.familyName").value(DEFAULT_FAMILY_NAME))
            .andExpect(jsonPath("$.givenName").value(DEFAULT_GIVEN_NAME))
            .andExpect(jsonPath("$.dob").value(DEFAULT_DOB.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.acquiredFrom").value(DEFAULT_ACQUIRED_FROM.toString()))
            .andExpect(jsonPath("$.currentPoints").value(DEFAULT_CURRENT_POINTS))
            .andExpect(jsonPath("$.totalPoints").value(DEFAULT_TOTAL_POINTS))
            .andExpect(jsonPath("$.isFollowing").value(DEFAULT_IS_FOLLOWING.booleanValue()))
            .andExpect(jsonPath("$.isEnabled").value(DEFAULT_IS_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    void getAlPacinosByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        UUID id = alPacino.getId();

        defaultAlPacinoFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllAlPacinosByPlatformCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where platformCode equals to
        defaultAlPacinoFiltering("platformCode.equals=" + DEFAULT_PLATFORM_CODE, "platformCode.equals=" + UPDATED_PLATFORM_CODE);
    }

    @Test
    @Transactional
    void getAllAlPacinosByPlatformCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where platformCode in
        defaultAlPacinoFiltering(
            "platformCode.in=" + DEFAULT_PLATFORM_CODE + "," + UPDATED_PLATFORM_CODE,
            "platformCode.in=" + UPDATED_PLATFORM_CODE
        );
    }

    @Test
    @Transactional
    void getAllAlPacinosByPlatformCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where platformCode is not null
        defaultAlPacinoFiltering("platformCode.specified=true", "platformCode.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPacinosByPlatformCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where platformCode contains
        defaultAlPacinoFiltering("platformCode.contains=" + DEFAULT_PLATFORM_CODE, "platformCode.contains=" + UPDATED_PLATFORM_CODE);
    }

    @Test
    @Transactional
    void getAllAlPacinosByPlatformCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where platformCode does not contain
        defaultAlPacinoFiltering(
            "platformCode.doesNotContain=" + UPDATED_PLATFORM_CODE,
            "platformCode.doesNotContain=" + DEFAULT_PLATFORM_CODE
        );
    }

    @Test
    @Transactional
    void getAllAlPacinosByPlatformUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where platformUsername equals to
        defaultAlPacinoFiltering(
            "platformUsername.equals=" + DEFAULT_PLATFORM_USERNAME,
            "platformUsername.equals=" + UPDATED_PLATFORM_USERNAME
        );
    }

    @Test
    @Transactional
    void getAllAlPacinosByPlatformUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where platformUsername in
        defaultAlPacinoFiltering(
            "platformUsername.in=" + DEFAULT_PLATFORM_USERNAME + "," + UPDATED_PLATFORM_USERNAME,
            "platformUsername.in=" + UPDATED_PLATFORM_USERNAME
        );
    }

    @Test
    @Transactional
    void getAllAlPacinosByPlatformUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where platformUsername is not null
        defaultAlPacinoFiltering("platformUsername.specified=true", "platformUsername.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPacinosByPlatformUsernameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where platformUsername contains
        defaultAlPacinoFiltering(
            "platformUsername.contains=" + DEFAULT_PLATFORM_USERNAME,
            "platformUsername.contains=" + UPDATED_PLATFORM_USERNAME
        );
    }

    @Test
    @Transactional
    void getAllAlPacinosByPlatformUsernameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where platformUsername does not contain
        defaultAlPacinoFiltering(
            "platformUsername.doesNotContain=" + UPDATED_PLATFORM_USERNAME,
            "platformUsername.doesNotContain=" + DEFAULT_PLATFORM_USERNAME
        );
    }

    @Test
    @Transactional
    void getAllAlPacinosByPlatformAvatarUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where platformAvatarUrl equals to
        defaultAlPacinoFiltering(
            "platformAvatarUrl.equals=" + DEFAULT_PLATFORM_AVATAR_URL,
            "platformAvatarUrl.equals=" + UPDATED_PLATFORM_AVATAR_URL
        );
    }

    @Test
    @Transactional
    void getAllAlPacinosByPlatformAvatarUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where platformAvatarUrl in
        defaultAlPacinoFiltering(
            "platformAvatarUrl.in=" + DEFAULT_PLATFORM_AVATAR_URL + "," + UPDATED_PLATFORM_AVATAR_URL,
            "platformAvatarUrl.in=" + UPDATED_PLATFORM_AVATAR_URL
        );
    }

    @Test
    @Transactional
    void getAllAlPacinosByPlatformAvatarUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where platformAvatarUrl is not null
        defaultAlPacinoFiltering("platformAvatarUrl.specified=true", "platformAvatarUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPacinosByPlatformAvatarUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where platformAvatarUrl contains
        defaultAlPacinoFiltering(
            "platformAvatarUrl.contains=" + DEFAULT_PLATFORM_AVATAR_URL,
            "platformAvatarUrl.contains=" + UPDATED_PLATFORM_AVATAR_URL
        );
    }

    @Test
    @Transactional
    void getAllAlPacinosByPlatformAvatarUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where platformAvatarUrl does not contain
        defaultAlPacinoFiltering(
            "platformAvatarUrl.doesNotContain=" + UPDATED_PLATFORM_AVATAR_URL,
            "platformAvatarUrl.doesNotContain=" + DEFAULT_PLATFORM_AVATAR_URL
        );
    }

    @Test
    @Transactional
    void getAllAlPacinosByIsSensitiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where isSensitive equals to
        defaultAlPacinoFiltering("isSensitive.equals=" + DEFAULT_IS_SENSITIVE, "isSensitive.equals=" + UPDATED_IS_SENSITIVE);
    }

    @Test
    @Transactional
    void getAllAlPacinosByIsSensitiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where isSensitive in
        defaultAlPacinoFiltering(
            "isSensitive.in=" + DEFAULT_IS_SENSITIVE + "," + UPDATED_IS_SENSITIVE,
            "isSensitive.in=" + UPDATED_IS_SENSITIVE
        );
    }

    @Test
    @Transactional
    void getAllAlPacinosByIsSensitiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where isSensitive is not null
        defaultAlPacinoFiltering("isSensitive.specified=true", "isSensitive.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPacinosByFamilyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where familyName equals to
        defaultAlPacinoFiltering("familyName.equals=" + DEFAULT_FAMILY_NAME, "familyName.equals=" + UPDATED_FAMILY_NAME);
    }

    @Test
    @Transactional
    void getAllAlPacinosByFamilyNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where familyName in
        defaultAlPacinoFiltering(
            "familyName.in=" + DEFAULT_FAMILY_NAME + "," + UPDATED_FAMILY_NAME,
            "familyName.in=" + UPDATED_FAMILY_NAME
        );
    }

    @Test
    @Transactional
    void getAllAlPacinosByFamilyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where familyName is not null
        defaultAlPacinoFiltering("familyName.specified=true", "familyName.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPacinosByFamilyNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where familyName contains
        defaultAlPacinoFiltering("familyName.contains=" + DEFAULT_FAMILY_NAME, "familyName.contains=" + UPDATED_FAMILY_NAME);
    }

    @Test
    @Transactional
    void getAllAlPacinosByFamilyNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where familyName does not contain
        defaultAlPacinoFiltering("familyName.doesNotContain=" + UPDATED_FAMILY_NAME, "familyName.doesNotContain=" + DEFAULT_FAMILY_NAME);
    }

    @Test
    @Transactional
    void getAllAlPacinosByGivenNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where givenName equals to
        defaultAlPacinoFiltering("givenName.equals=" + DEFAULT_GIVEN_NAME, "givenName.equals=" + UPDATED_GIVEN_NAME);
    }

    @Test
    @Transactional
    void getAllAlPacinosByGivenNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where givenName in
        defaultAlPacinoFiltering("givenName.in=" + DEFAULT_GIVEN_NAME + "," + UPDATED_GIVEN_NAME, "givenName.in=" + UPDATED_GIVEN_NAME);
    }

    @Test
    @Transactional
    void getAllAlPacinosByGivenNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where givenName is not null
        defaultAlPacinoFiltering("givenName.specified=true", "givenName.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPacinosByGivenNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where givenName contains
        defaultAlPacinoFiltering("givenName.contains=" + DEFAULT_GIVEN_NAME, "givenName.contains=" + UPDATED_GIVEN_NAME);
    }

    @Test
    @Transactional
    void getAllAlPacinosByGivenNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where givenName does not contain
        defaultAlPacinoFiltering("givenName.doesNotContain=" + UPDATED_GIVEN_NAME, "givenName.doesNotContain=" + DEFAULT_GIVEN_NAME);
    }

    @Test
    @Transactional
    void getAllAlPacinosByDobIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where dob equals to
        defaultAlPacinoFiltering("dob.equals=" + DEFAULT_DOB, "dob.equals=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllAlPacinosByDobIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where dob in
        defaultAlPacinoFiltering("dob.in=" + DEFAULT_DOB + "," + UPDATED_DOB, "dob.in=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllAlPacinosByDobIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where dob is not null
        defaultAlPacinoFiltering("dob.specified=true", "dob.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPacinosByDobIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where dob is greater than or equal to
        defaultAlPacinoFiltering("dob.greaterThanOrEqual=" + DEFAULT_DOB, "dob.greaterThanOrEqual=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllAlPacinosByDobIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where dob is less than or equal to
        defaultAlPacinoFiltering("dob.lessThanOrEqual=" + DEFAULT_DOB, "dob.lessThanOrEqual=" + SMALLER_DOB);
    }

    @Test
    @Transactional
    void getAllAlPacinosByDobIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where dob is less than
        defaultAlPacinoFiltering("dob.lessThan=" + UPDATED_DOB, "dob.lessThan=" + DEFAULT_DOB);
    }

    @Test
    @Transactional
    void getAllAlPacinosByDobIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where dob is greater than
        defaultAlPacinoFiltering("dob.greaterThan=" + SMALLER_DOB, "dob.greaterThan=" + DEFAULT_DOB);
    }

    @Test
    @Transactional
    void getAllAlPacinosByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where gender equals to
        defaultAlPacinoFiltering("gender.equals=" + DEFAULT_GENDER, "gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllAlPacinosByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where gender in
        defaultAlPacinoFiltering("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER, "gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllAlPacinosByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where gender is not null
        defaultAlPacinoFiltering("gender.specified=true", "gender.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPacinosByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where phone equals to
        defaultAlPacinoFiltering("phone.equals=" + DEFAULT_PHONE, "phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllAlPacinosByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where phone in
        defaultAlPacinoFiltering("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE, "phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllAlPacinosByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where phone is not null
        defaultAlPacinoFiltering("phone.specified=true", "phone.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPacinosByPhoneContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where phone contains
        defaultAlPacinoFiltering("phone.contains=" + DEFAULT_PHONE, "phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllAlPacinosByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where phone does not contain
        defaultAlPacinoFiltering("phone.doesNotContain=" + UPDATED_PHONE, "phone.doesNotContain=" + DEFAULT_PHONE);
    }

    @Test
    @Transactional
    void getAllAlPacinosByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where email equals to
        defaultAlPacinoFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAlPacinosByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where email in
        defaultAlPacinoFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAlPacinosByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where email is not null
        defaultAlPacinoFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPacinosByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where email contains
        defaultAlPacinoFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAlPacinosByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where email does not contain
        defaultAlPacinoFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllAlPacinosByAcquiredFromIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where acquiredFrom equals to
        defaultAlPacinoFiltering("acquiredFrom.equals=" + DEFAULT_ACQUIRED_FROM, "acquiredFrom.equals=" + UPDATED_ACQUIRED_FROM);
    }

    @Test
    @Transactional
    void getAllAlPacinosByAcquiredFromIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where acquiredFrom in
        defaultAlPacinoFiltering(
            "acquiredFrom.in=" + DEFAULT_ACQUIRED_FROM + "," + UPDATED_ACQUIRED_FROM,
            "acquiredFrom.in=" + UPDATED_ACQUIRED_FROM
        );
    }

    @Test
    @Transactional
    void getAllAlPacinosByAcquiredFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where acquiredFrom is not null
        defaultAlPacinoFiltering("acquiredFrom.specified=true", "acquiredFrom.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPacinosByCurrentPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where currentPoints equals to
        defaultAlPacinoFiltering("currentPoints.equals=" + DEFAULT_CURRENT_POINTS, "currentPoints.equals=" + UPDATED_CURRENT_POINTS);
    }

    @Test
    @Transactional
    void getAllAlPacinosByCurrentPointsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where currentPoints in
        defaultAlPacinoFiltering(
            "currentPoints.in=" + DEFAULT_CURRENT_POINTS + "," + UPDATED_CURRENT_POINTS,
            "currentPoints.in=" + UPDATED_CURRENT_POINTS
        );
    }

    @Test
    @Transactional
    void getAllAlPacinosByCurrentPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where currentPoints is not null
        defaultAlPacinoFiltering("currentPoints.specified=true", "currentPoints.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPacinosByCurrentPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where currentPoints is greater than or equal to
        defaultAlPacinoFiltering(
            "currentPoints.greaterThanOrEqual=" + DEFAULT_CURRENT_POINTS,
            "currentPoints.greaterThanOrEqual=" + UPDATED_CURRENT_POINTS
        );
    }

    @Test
    @Transactional
    void getAllAlPacinosByCurrentPointsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where currentPoints is less than or equal to
        defaultAlPacinoFiltering(
            "currentPoints.lessThanOrEqual=" + DEFAULT_CURRENT_POINTS,
            "currentPoints.lessThanOrEqual=" + SMALLER_CURRENT_POINTS
        );
    }

    @Test
    @Transactional
    void getAllAlPacinosByCurrentPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where currentPoints is less than
        defaultAlPacinoFiltering("currentPoints.lessThan=" + UPDATED_CURRENT_POINTS, "currentPoints.lessThan=" + DEFAULT_CURRENT_POINTS);
    }

    @Test
    @Transactional
    void getAllAlPacinosByCurrentPointsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where currentPoints is greater than
        defaultAlPacinoFiltering(
            "currentPoints.greaterThan=" + SMALLER_CURRENT_POINTS,
            "currentPoints.greaterThan=" + DEFAULT_CURRENT_POINTS
        );
    }

    @Test
    @Transactional
    void getAllAlPacinosByTotalPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where totalPoints equals to
        defaultAlPacinoFiltering("totalPoints.equals=" + DEFAULT_TOTAL_POINTS, "totalPoints.equals=" + UPDATED_TOTAL_POINTS);
    }

    @Test
    @Transactional
    void getAllAlPacinosByTotalPointsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where totalPoints in
        defaultAlPacinoFiltering(
            "totalPoints.in=" + DEFAULT_TOTAL_POINTS + "," + UPDATED_TOTAL_POINTS,
            "totalPoints.in=" + UPDATED_TOTAL_POINTS
        );
    }

    @Test
    @Transactional
    void getAllAlPacinosByTotalPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where totalPoints is not null
        defaultAlPacinoFiltering("totalPoints.specified=true", "totalPoints.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPacinosByTotalPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where totalPoints is greater than or equal to
        defaultAlPacinoFiltering(
            "totalPoints.greaterThanOrEqual=" + DEFAULT_TOTAL_POINTS,
            "totalPoints.greaterThanOrEqual=" + UPDATED_TOTAL_POINTS
        );
    }

    @Test
    @Transactional
    void getAllAlPacinosByTotalPointsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where totalPoints is less than or equal to
        defaultAlPacinoFiltering(
            "totalPoints.lessThanOrEqual=" + DEFAULT_TOTAL_POINTS,
            "totalPoints.lessThanOrEqual=" + SMALLER_TOTAL_POINTS
        );
    }

    @Test
    @Transactional
    void getAllAlPacinosByTotalPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where totalPoints is less than
        defaultAlPacinoFiltering("totalPoints.lessThan=" + UPDATED_TOTAL_POINTS, "totalPoints.lessThan=" + DEFAULT_TOTAL_POINTS);
    }

    @Test
    @Transactional
    void getAllAlPacinosByTotalPointsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where totalPoints is greater than
        defaultAlPacinoFiltering("totalPoints.greaterThan=" + SMALLER_TOTAL_POINTS, "totalPoints.greaterThan=" + DEFAULT_TOTAL_POINTS);
    }

    @Test
    @Transactional
    void getAllAlPacinosByIsFollowingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where isFollowing equals to
        defaultAlPacinoFiltering("isFollowing.equals=" + DEFAULT_IS_FOLLOWING, "isFollowing.equals=" + UPDATED_IS_FOLLOWING);
    }

    @Test
    @Transactional
    void getAllAlPacinosByIsFollowingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where isFollowing in
        defaultAlPacinoFiltering(
            "isFollowing.in=" + DEFAULT_IS_FOLLOWING + "," + UPDATED_IS_FOLLOWING,
            "isFollowing.in=" + UPDATED_IS_FOLLOWING
        );
    }

    @Test
    @Transactional
    void getAllAlPacinosByIsFollowingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where isFollowing is not null
        defaultAlPacinoFiltering("isFollowing.specified=true", "isFollowing.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPacinosByIsEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where isEnabled equals to
        defaultAlPacinoFiltering("isEnabled.equals=" + DEFAULT_IS_ENABLED, "isEnabled.equals=" + UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    void getAllAlPacinosByIsEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where isEnabled in
        defaultAlPacinoFiltering("isEnabled.in=" + DEFAULT_IS_ENABLED + "," + UPDATED_IS_ENABLED, "isEnabled.in=" + UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    void getAllAlPacinosByIsEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        // Get all the alPacinoList where isEnabled is not null
        defaultAlPacinoFiltering("isEnabled.specified=true", "isEnabled.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPacinosByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alPacinoRepository.saveAndFlush(alPacino);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alPacino.setApplication(application);
        alPacinoRepository.saveAndFlush(alPacino);
        UUID applicationId = application.getId();
        // Get all the alPacinoList where application equals to applicationId
        defaultAlPacinoShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alPacinoList where application equals to UUID.randomUUID()
        defaultAlPacinoShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlPacinosByMembershipTierIsEqualToSomething() throws Exception {
        AlMemTier membershipTier;
        if (TestUtil.findAll(em, AlMemTier.class).isEmpty()) {
            alPacinoRepository.saveAndFlush(alPacino);
            membershipTier = AlMemTierResourceIT.createEntity();
        } else {
            membershipTier = TestUtil.findAll(em, AlMemTier.class).get(0);
        }
        em.persist(membershipTier);
        em.flush();
        alPacino.setMembershipTier(membershipTier);
        alPacinoRepository.saveAndFlush(alPacino);
        Long membershipTierId = membershipTier.getId();
        // Get all the alPacinoList where membershipTier equals to membershipTierId
        defaultAlPacinoShouldBeFound("membershipTierId.equals=" + membershipTierId);

        // Get all the alPacinoList where membershipTier equals to (membershipTierId + 1)
        defaultAlPacinoShouldNotBeFound("membershipTierId.equals=" + (membershipTierId + 1));
    }

    @Test
    @Transactional
    void getAllAlPacinosByAlVueVueUsageIsEqualToSomething() throws Exception {
        AlVueVueUsage alVueVueUsage;
        if (TestUtil.findAll(em, AlVueVueUsage.class).isEmpty()) {
            alPacinoRepository.saveAndFlush(alPacino);
            alVueVueUsage = AlVueVueUsageResourceIT.createEntity();
        } else {
            alVueVueUsage = TestUtil.findAll(em, AlVueVueUsage.class).get(0);
        }
        em.persist(alVueVueUsage);
        em.flush();
        alPacino.setAlVueVueUsage(alVueVueUsage);
        alPacinoRepository.saveAndFlush(alPacino);
        UUID alVueVueUsageId = alVueVueUsage.getId();
        // Get all the alPacinoList where alVueVueUsage equals to alVueVueUsageId
        defaultAlPacinoShouldBeFound("alVueVueUsageId.equals=" + alVueVueUsageId);

        // Get all the alPacinoList where alVueVueUsage equals to UUID.randomUUID()
        defaultAlPacinoShouldNotBeFound("alVueVueUsageId.equals=" + UUID.randomUUID());
    }

    private void defaultAlPacinoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlPacinoShouldBeFound(shouldBeFound);
        defaultAlPacinoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlPacinoShouldBeFound(String filter) throws Exception {
        restAlPacinoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPacino.getId().toString())))
            .andExpect(jsonPath("$.[*].platformCode").value(hasItem(DEFAULT_PLATFORM_CODE)))
            .andExpect(jsonPath("$.[*].platformUsername").value(hasItem(DEFAULT_PLATFORM_USERNAME)))
            .andExpect(jsonPath("$.[*].platformAvatarUrl").value(hasItem(DEFAULT_PLATFORM_AVATAR_URL)))
            .andExpect(jsonPath("$.[*].isSensitive").value(hasItem(DEFAULT_IS_SENSITIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].familyName").value(hasItem(DEFAULT_FAMILY_NAME)))
            .andExpect(jsonPath("$.[*].givenName").value(hasItem(DEFAULT_GIVEN_NAME)))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].acquiredFrom").value(hasItem(DEFAULT_ACQUIRED_FROM.toString())))
            .andExpect(jsonPath("$.[*].currentPoints").value(hasItem(DEFAULT_CURRENT_POINTS)))
            .andExpect(jsonPath("$.[*].totalPoints").value(hasItem(DEFAULT_TOTAL_POINTS)))
            .andExpect(jsonPath("$.[*].isFollowing").value(hasItem(DEFAULT_IS_FOLLOWING.booleanValue())))
            .andExpect(jsonPath("$.[*].isEnabled").value(hasItem(DEFAULT_IS_ENABLED.booleanValue())));

        // Check, that the count call also returns 1
        restAlPacinoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlPacinoShouldNotBeFound(String filter) throws Exception {
        restAlPacinoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlPacinoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlPacino() throws Exception {
        // Get the alPacino
        restAlPacinoMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlPacino() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPacino
        AlPacino updatedAlPacino = alPacinoRepository.findById(alPacino.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlPacino are not directly saved in db
        em.detach(updatedAlPacino);
        updatedAlPacino
            .platformCode(UPDATED_PLATFORM_CODE)
            .platformUsername(UPDATED_PLATFORM_USERNAME)
            .platformAvatarUrl(UPDATED_PLATFORM_AVATAR_URL)
            .isSensitive(UPDATED_IS_SENSITIVE)
            .familyName(UPDATED_FAMILY_NAME)
            .givenName(UPDATED_GIVEN_NAME)
            .dob(UPDATED_DOB)
            .gender(UPDATED_GENDER)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .acquiredFrom(UPDATED_ACQUIRED_FROM)
            .currentPoints(UPDATED_CURRENT_POINTS)
            .totalPoints(UPDATED_TOTAL_POINTS)
            .isFollowing(UPDATED_IS_FOLLOWING)
            .isEnabled(UPDATED_IS_ENABLED);
        AlPacinoDTO alPacinoDTO = alPacinoMapper.toDto(updatedAlPacino);

        restAlPacinoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alPacinoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPacinoDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlPacino in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlPacinoToMatchAllProperties(updatedAlPacino);
    }

    @Test
    @Transactional
    void putNonExistingAlPacino() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacino.setId(UUID.randomUUID());

        // Create the AlPacino
        AlPacinoDTO alPacinoDTO = alPacinoMapper.toDto(alPacino);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPacinoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alPacinoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPacinoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPacino in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlPacino() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacino.setId(UUID.randomUUID());

        // Create the AlPacino
        AlPacinoDTO alPacinoDTO = alPacinoMapper.toDto(alPacino);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPacinoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPacinoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPacino in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlPacino() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacino.setId(UUID.randomUUID());

        // Create the AlPacino
        AlPacinoDTO alPacinoDTO = alPacinoMapper.toDto(alPacino);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPacinoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPacinoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPacino in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlPacinoWithPatch() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPacino using partial update
        AlPacino partialUpdatedAlPacino = new AlPacino();
        partialUpdatedAlPacino.setId(alPacino.getId());

        partialUpdatedAlPacino
            .platformUsername(UPDATED_PLATFORM_USERNAME)
            .platformAvatarUrl(UPDATED_PLATFORM_AVATAR_URL)
            .isSensitive(UPDATED_IS_SENSITIVE)
            .familyName(UPDATED_FAMILY_NAME)
            .givenName(UPDATED_GIVEN_NAME)
            .email(UPDATED_EMAIL)
            .acquiredFrom(UPDATED_ACQUIRED_FROM)
            .totalPoints(UPDATED_TOTAL_POINTS)
            .isEnabled(UPDATED_IS_ENABLED);

        restAlPacinoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPacino.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPacino))
            )
            .andExpect(status().isOk());

        // Validate the AlPacino in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPacinoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAlPacino, alPacino), getPersistedAlPacino(alPacino));
    }

    @Test
    @Transactional
    void fullUpdateAlPacinoWithPatch() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPacino using partial update
        AlPacino partialUpdatedAlPacino = new AlPacino();
        partialUpdatedAlPacino.setId(alPacino.getId());

        partialUpdatedAlPacino
            .platformCode(UPDATED_PLATFORM_CODE)
            .platformUsername(UPDATED_PLATFORM_USERNAME)
            .platformAvatarUrl(UPDATED_PLATFORM_AVATAR_URL)
            .isSensitive(UPDATED_IS_SENSITIVE)
            .familyName(UPDATED_FAMILY_NAME)
            .givenName(UPDATED_GIVEN_NAME)
            .dob(UPDATED_DOB)
            .gender(UPDATED_GENDER)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .acquiredFrom(UPDATED_ACQUIRED_FROM)
            .currentPoints(UPDATED_CURRENT_POINTS)
            .totalPoints(UPDATED_TOTAL_POINTS)
            .isFollowing(UPDATED_IS_FOLLOWING)
            .isEnabled(UPDATED_IS_ENABLED);

        restAlPacinoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPacino.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPacino))
            )
            .andExpect(status().isOk());

        // Validate the AlPacino in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPacinoUpdatableFieldsEquals(partialUpdatedAlPacino, getPersistedAlPacino(partialUpdatedAlPacino));
    }

    @Test
    @Transactional
    void patchNonExistingAlPacino() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacino.setId(UUID.randomUUID());

        // Create the AlPacino
        AlPacinoDTO alPacinoDTO = alPacinoMapper.toDto(alPacino);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPacinoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alPacinoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPacinoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPacino in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlPacino() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacino.setId(UUID.randomUUID());

        // Create the AlPacino
        AlPacinoDTO alPacinoDTO = alPacinoMapper.toDto(alPacino);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPacinoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPacinoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPacino in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlPacino() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacino.setId(UUID.randomUUID());

        // Create the AlPacino
        AlPacinoDTO alPacinoDTO = alPacinoMapper.toDto(alPacino);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPacinoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alPacinoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPacino in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlPacino() throws Exception {
        // Initialize the database
        insertedAlPacino = alPacinoRepository.saveAndFlush(alPacino);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alPacino
        restAlPacinoMockMvc
            .perform(delete(ENTITY_API_URL_ID, alPacino.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alPacinoRepository.count();
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

    protected AlPacino getPersistedAlPacino(AlPacino alPacino) {
        return alPacinoRepository.findById(alPacino.getId()).orElseThrow();
    }

    protected void assertPersistedAlPacinoToMatchAllProperties(AlPacino expectedAlPacino) {
        assertAlPacinoAllPropertiesEquals(expectedAlPacino, getPersistedAlPacino(expectedAlPacino));
    }

    protected void assertPersistedAlPacinoToMatchUpdatableProperties(AlPacino expectedAlPacino) {
        assertAlPacinoAllUpdatablePropertiesEquals(expectedAlPacino, getPersistedAlPacino(expectedAlPacino));
    }
}
