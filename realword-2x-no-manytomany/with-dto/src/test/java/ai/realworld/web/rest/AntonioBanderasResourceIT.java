package ai.realworld.web.rest;

import static ai.realworld.domain.AntonioBanderasAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AntonioBanderas;
import ai.realworld.domain.AntonioBanderas;
import ai.realworld.repository.AntonioBanderasRepository;
import ai.realworld.service.AntonioBanderasService;
import ai.realworld.service.dto.AntonioBanderasDTO;
import ai.realworld.service.mapper.AntonioBanderasMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AntonioBanderasResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AntonioBanderasResourceIT {

    private static final Integer DEFAULT_LEVEL = 1;
    private static final Integer UPDATED_LEVEL = 2;
    private static final Integer SMALLER_LEVEL = 1 - 1;

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NATIVE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NATIVE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_OFFICIAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_OFFICIAL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DIVISION_TERM = "AAAAAAAAAA";
    private static final String UPDATED_DIVISION_TERM = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/antonio-banderas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AntonioBanderasRepository antonioBanderasRepository;

    @Mock
    private AntonioBanderasRepository antonioBanderasRepositoryMock;

    @Autowired
    private AntonioBanderasMapper antonioBanderasMapper;

    @Mock
    private AntonioBanderasService antonioBanderasServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAntonioBanderasMockMvc;

    private AntonioBanderas antonioBanderas;

    private AntonioBanderas insertedAntonioBanderas;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AntonioBanderas createEntity() {
        return new AntonioBanderas()
            .level(DEFAULT_LEVEL)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .fullName(DEFAULT_FULL_NAME)
            .nativeName(DEFAULT_NATIVE_NAME)
            .officialCode(DEFAULT_OFFICIAL_CODE)
            .divisionTerm(DEFAULT_DIVISION_TERM)
            .isDeleted(DEFAULT_IS_DELETED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AntonioBanderas createUpdatedEntity() {
        return new AntonioBanderas()
            .level(UPDATED_LEVEL)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .fullName(UPDATED_FULL_NAME)
            .nativeName(UPDATED_NATIVE_NAME)
            .officialCode(UPDATED_OFFICIAL_CODE)
            .divisionTerm(UPDATED_DIVISION_TERM)
            .isDeleted(UPDATED_IS_DELETED);
    }

    @BeforeEach
    public void initTest() {
        antonioBanderas = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAntonioBanderas != null) {
            antonioBanderasRepository.delete(insertedAntonioBanderas);
            insertedAntonioBanderas = null;
        }
    }

    @Test
    @Transactional
    void createAntonioBanderas() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AntonioBanderas
        AntonioBanderasDTO antonioBanderasDTO = antonioBanderasMapper.toDto(antonioBanderas);
        var returnedAntonioBanderasDTO = om.readValue(
            restAntonioBanderasMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(antonioBanderasDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AntonioBanderasDTO.class
        );

        // Validate the AntonioBanderas in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAntonioBanderas = antonioBanderasMapper.toEntity(returnedAntonioBanderasDTO);
        assertAntonioBanderasUpdatableFieldsEquals(returnedAntonioBanderas, getPersistedAntonioBanderas(returnedAntonioBanderas));

        insertedAntonioBanderas = returnedAntonioBanderas;
    }

    @Test
    @Transactional
    void createAntonioBanderasWithExistingId() throws Exception {
        // Create the AntonioBanderas with an existing ID
        antonioBanderas.setId(1L);
        AntonioBanderasDTO antonioBanderasDTO = antonioBanderasMapper.toDto(antonioBanderas);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAntonioBanderasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(antonioBanderasDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AntonioBanderas in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        antonioBanderas.setCode(null);

        // Create the AntonioBanderas, which fails.
        AntonioBanderasDTO antonioBanderasDTO = antonioBanderasMapper.toDto(antonioBanderas);

        restAntonioBanderasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(antonioBanderasDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAntonioBanderas() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList
        restAntonioBanderasMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(antonioBanderas.getId().intValue())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].nativeName").value(hasItem(DEFAULT_NATIVE_NAME)))
            .andExpect(jsonPath("$.[*].officialCode").value(hasItem(DEFAULT_OFFICIAL_CODE)))
            .andExpect(jsonPath("$.[*].divisionTerm").value(hasItem(DEFAULT_DIVISION_TERM)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAntonioBanderasWithEagerRelationshipsIsEnabled() throws Exception {
        when(antonioBanderasServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAntonioBanderasMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(antonioBanderasServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAntonioBanderasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(antonioBanderasServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAntonioBanderasMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(antonioBanderasRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAntonioBanderas() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get the antonioBanderas
        restAntonioBanderasMockMvc
            .perform(get(ENTITY_API_URL_ID, antonioBanderas.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(antonioBanderas.getId().intValue()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.nativeName").value(DEFAULT_NATIVE_NAME))
            .andExpect(jsonPath("$.officialCode").value(DEFAULT_OFFICIAL_CODE))
            .andExpect(jsonPath("$.divisionTerm").value(DEFAULT_DIVISION_TERM))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getAntonioBanderasByIdFiltering() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        Long id = antonioBanderas.getId();

        defaultAntonioBanderasFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAntonioBanderasFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAntonioBanderasFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where level equals to
        defaultAntonioBanderasFiltering("level.equals=" + DEFAULT_LEVEL, "level.equals=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByLevelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where level in
        defaultAntonioBanderasFiltering("level.in=" + DEFAULT_LEVEL + "," + UPDATED_LEVEL, "level.in=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where level is not null
        defaultAntonioBanderasFiltering("level.specified=true", "level.specified=false");
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByLevelIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where level is greater than or equal to
        defaultAntonioBanderasFiltering("level.greaterThanOrEqual=" + DEFAULT_LEVEL, "level.greaterThanOrEqual=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByLevelIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where level is less than or equal to
        defaultAntonioBanderasFiltering("level.lessThanOrEqual=" + DEFAULT_LEVEL, "level.lessThanOrEqual=" + SMALLER_LEVEL);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByLevelIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where level is less than
        defaultAntonioBanderasFiltering("level.lessThan=" + UPDATED_LEVEL, "level.lessThan=" + DEFAULT_LEVEL);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByLevelIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where level is greater than
        defaultAntonioBanderasFiltering("level.greaterThan=" + SMALLER_LEVEL, "level.greaterThan=" + DEFAULT_LEVEL);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where code equals to
        defaultAntonioBanderasFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where code in
        defaultAntonioBanderasFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where code is not null
        defaultAntonioBanderasFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where code contains
        defaultAntonioBanderasFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where code does not contain
        defaultAntonioBanderasFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where name equals to
        defaultAntonioBanderasFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where name in
        defaultAntonioBanderasFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where name is not null
        defaultAntonioBanderasFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where name contains
        defaultAntonioBanderasFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where name does not contain
        defaultAntonioBanderasFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByFullNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where fullName equals to
        defaultAntonioBanderasFiltering("fullName.equals=" + DEFAULT_FULL_NAME, "fullName.equals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByFullNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where fullName in
        defaultAntonioBanderasFiltering("fullName.in=" + DEFAULT_FULL_NAME + "," + UPDATED_FULL_NAME, "fullName.in=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByFullNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where fullName is not null
        defaultAntonioBanderasFiltering("fullName.specified=true", "fullName.specified=false");
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByFullNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where fullName contains
        defaultAntonioBanderasFiltering("fullName.contains=" + DEFAULT_FULL_NAME, "fullName.contains=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByFullNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where fullName does not contain
        defaultAntonioBanderasFiltering("fullName.doesNotContain=" + UPDATED_FULL_NAME, "fullName.doesNotContain=" + DEFAULT_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByNativeNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where nativeName equals to
        defaultAntonioBanderasFiltering("nativeName.equals=" + DEFAULT_NATIVE_NAME, "nativeName.equals=" + UPDATED_NATIVE_NAME);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByNativeNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where nativeName in
        defaultAntonioBanderasFiltering(
            "nativeName.in=" + DEFAULT_NATIVE_NAME + "," + UPDATED_NATIVE_NAME,
            "nativeName.in=" + UPDATED_NATIVE_NAME
        );
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByNativeNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where nativeName is not null
        defaultAntonioBanderasFiltering("nativeName.specified=true", "nativeName.specified=false");
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByNativeNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where nativeName contains
        defaultAntonioBanderasFiltering("nativeName.contains=" + DEFAULT_NATIVE_NAME, "nativeName.contains=" + UPDATED_NATIVE_NAME);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByNativeNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where nativeName does not contain
        defaultAntonioBanderasFiltering(
            "nativeName.doesNotContain=" + UPDATED_NATIVE_NAME,
            "nativeName.doesNotContain=" + DEFAULT_NATIVE_NAME
        );
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByOfficialCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where officialCode equals to
        defaultAntonioBanderasFiltering("officialCode.equals=" + DEFAULT_OFFICIAL_CODE, "officialCode.equals=" + UPDATED_OFFICIAL_CODE);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByOfficialCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where officialCode in
        defaultAntonioBanderasFiltering(
            "officialCode.in=" + DEFAULT_OFFICIAL_CODE + "," + UPDATED_OFFICIAL_CODE,
            "officialCode.in=" + UPDATED_OFFICIAL_CODE
        );
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByOfficialCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where officialCode is not null
        defaultAntonioBanderasFiltering("officialCode.specified=true", "officialCode.specified=false");
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByOfficialCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where officialCode contains
        defaultAntonioBanderasFiltering("officialCode.contains=" + DEFAULT_OFFICIAL_CODE, "officialCode.contains=" + UPDATED_OFFICIAL_CODE);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByOfficialCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where officialCode does not contain
        defaultAntonioBanderasFiltering(
            "officialCode.doesNotContain=" + UPDATED_OFFICIAL_CODE,
            "officialCode.doesNotContain=" + DEFAULT_OFFICIAL_CODE
        );
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByDivisionTermIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where divisionTerm equals to
        defaultAntonioBanderasFiltering("divisionTerm.equals=" + DEFAULT_DIVISION_TERM, "divisionTerm.equals=" + UPDATED_DIVISION_TERM);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByDivisionTermIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where divisionTerm in
        defaultAntonioBanderasFiltering(
            "divisionTerm.in=" + DEFAULT_DIVISION_TERM + "," + UPDATED_DIVISION_TERM,
            "divisionTerm.in=" + UPDATED_DIVISION_TERM
        );
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByDivisionTermIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where divisionTerm is not null
        defaultAntonioBanderasFiltering("divisionTerm.specified=true", "divisionTerm.specified=false");
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByDivisionTermContainsSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where divisionTerm contains
        defaultAntonioBanderasFiltering("divisionTerm.contains=" + DEFAULT_DIVISION_TERM, "divisionTerm.contains=" + UPDATED_DIVISION_TERM);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByDivisionTermNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where divisionTerm does not contain
        defaultAntonioBanderasFiltering(
            "divisionTerm.doesNotContain=" + UPDATED_DIVISION_TERM,
            "divisionTerm.doesNotContain=" + DEFAULT_DIVISION_TERM
        );
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByIsDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where isDeleted equals to
        defaultAntonioBanderasFiltering("isDeleted.equals=" + DEFAULT_IS_DELETED, "isDeleted.equals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByIsDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where isDeleted in
        defaultAntonioBanderasFiltering(
            "isDeleted.in=" + DEFAULT_IS_DELETED + "," + UPDATED_IS_DELETED,
            "isDeleted.in=" + UPDATED_IS_DELETED
        );
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByIsDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        // Get all the antonioBanderasList where isDeleted is not null
        defaultAntonioBanderasFiltering("isDeleted.specified=true", "isDeleted.specified=false");
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByCurrentIsEqualToSomething() throws Exception {
        AntonioBanderas current;
        if (TestUtil.findAll(em, AntonioBanderas.class).isEmpty()) {
            antonioBanderasRepository.saveAndFlush(antonioBanderas);
            current = AntonioBanderasResourceIT.createEntity();
        } else {
            current = TestUtil.findAll(em, AntonioBanderas.class).get(0);
        }
        em.persist(current);
        em.flush();
        antonioBanderas.setCurrent(current);
        antonioBanderasRepository.saveAndFlush(antonioBanderas);
        Long currentId = current.getId();
        // Get all the antonioBanderasList where current equals to currentId
        defaultAntonioBanderasShouldBeFound("currentId.equals=" + currentId);

        // Get all the antonioBanderasList where current equals to (currentId + 1)
        defaultAntonioBanderasShouldNotBeFound("currentId.equals=" + (currentId + 1));
    }

    @Test
    @Transactional
    void getAllAntonioBanderasByParentIsEqualToSomething() throws Exception {
        AntonioBanderas parent;
        if (TestUtil.findAll(em, AntonioBanderas.class).isEmpty()) {
            antonioBanderasRepository.saveAndFlush(antonioBanderas);
            parent = AntonioBanderasResourceIT.createEntity();
        } else {
            parent = TestUtil.findAll(em, AntonioBanderas.class).get(0);
        }
        em.persist(parent);
        em.flush();
        antonioBanderas.setParent(parent);
        antonioBanderasRepository.saveAndFlush(antonioBanderas);
        Long parentId = parent.getId();
        // Get all the antonioBanderasList where parent equals to parentId
        defaultAntonioBanderasShouldBeFound("parentId.equals=" + parentId);

        // Get all the antonioBanderasList where parent equals to (parentId + 1)
        defaultAntonioBanderasShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    private void defaultAntonioBanderasFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAntonioBanderasShouldBeFound(shouldBeFound);
        defaultAntonioBanderasShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAntonioBanderasShouldBeFound(String filter) throws Exception {
        restAntonioBanderasMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(antonioBanderas.getId().intValue())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].nativeName").value(hasItem(DEFAULT_NATIVE_NAME)))
            .andExpect(jsonPath("$.[*].officialCode").value(hasItem(DEFAULT_OFFICIAL_CODE)))
            .andExpect(jsonPath("$.[*].divisionTerm").value(hasItem(DEFAULT_DIVISION_TERM)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));

        // Check, that the count call also returns 1
        restAntonioBanderasMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAntonioBanderasShouldNotBeFound(String filter) throws Exception {
        restAntonioBanderasMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAntonioBanderasMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAntonioBanderas() throws Exception {
        // Get the antonioBanderas
        restAntonioBanderasMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAntonioBanderas() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the antonioBanderas
        AntonioBanderas updatedAntonioBanderas = antonioBanderasRepository.findById(antonioBanderas.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAntonioBanderas are not directly saved in db
        em.detach(updatedAntonioBanderas);
        updatedAntonioBanderas
            .level(UPDATED_LEVEL)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .fullName(UPDATED_FULL_NAME)
            .nativeName(UPDATED_NATIVE_NAME)
            .officialCode(UPDATED_OFFICIAL_CODE)
            .divisionTerm(UPDATED_DIVISION_TERM)
            .isDeleted(UPDATED_IS_DELETED);
        AntonioBanderasDTO antonioBanderasDTO = antonioBanderasMapper.toDto(updatedAntonioBanderas);

        restAntonioBanderasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, antonioBanderasDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(antonioBanderasDTO))
            )
            .andExpect(status().isOk());

        // Validate the AntonioBanderas in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAntonioBanderasToMatchAllProperties(updatedAntonioBanderas);
    }

    @Test
    @Transactional
    void putNonExistingAntonioBanderas() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        antonioBanderas.setId(longCount.incrementAndGet());

        // Create the AntonioBanderas
        AntonioBanderasDTO antonioBanderasDTO = antonioBanderasMapper.toDto(antonioBanderas);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAntonioBanderasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, antonioBanderasDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(antonioBanderasDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AntonioBanderas in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAntonioBanderas() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        antonioBanderas.setId(longCount.incrementAndGet());

        // Create the AntonioBanderas
        AntonioBanderasDTO antonioBanderasDTO = antonioBanderasMapper.toDto(antonioBanderas);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAntonioBanderasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(antonioBanderasDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AntonioBanderas in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAntonioBanderas() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        antonioBanderas.setId(longCount.incrementAndGet());

        // Create the AntonioBanderas
        AntonioBanderasDTO antonioBanderasDTO = antonioBanderasMapper.toDto(antonioBanderas);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAntonioBanderasMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(antonioBanderasDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AntonioBanderas in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAntonioBanderasWithPatch() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the antonioBanderas using partial update
        AntonioBanderas partialUpdatedAntonioBanderas = new AntonioBanderas();
        partialUpdatedAntonioBanderas.setId(antonioBanderas.getId());

        partialUpdatedAntonioBanderas.code(UPDATED_CODE).name(UPDATED_NAME).fullName(UPDATED_FULL_NAME).officialCode(UPDATED_OFFICIAL_CODE);

        restAntonioBanderasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAntonioBanderas.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAntonioBanderas))
            )
            .andExpect(status().isOk());

        // Validate the AntonioBanderas in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAntonioBanderasUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAntonioBanderas, antonioBanderas),
            getPersistedAntonioBanderas(antonioBanderas)
        );
    }

    @Test
    @Transactional
    void fullUpdateAntonioBanderasWithPatch() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the antonioBanderas using partial update
        AntonioBanderas partialUpdatedAntonioBanderas = new AntonioBanderas();
        partialUpdatedAntonioBanderas.setId(antonioBanderas.getId());

        partialUpdatedAntonioBanderas
            .level(UPDATED_LEVEL)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .fullName(UPDATED_FULL_NAME)
            .nativeName(UPDATED_NATIVE_NAME)
            .officialCode(UPDATED_OFFICIAL_CODE)
            .divisionTerm(UPDATED_DIVISION_TERM)
            .isDeleted(UPDATED_IS_DELETED);

        restAntonioBanderasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAntonioBanderas.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAntonioBanderas))
            )
            .andExpect(status().isOk());

        // Validate the AntonioBanderas in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAntonioBanderasUpdatableFieldsEquals(
            partialUpdatedAntonioBanderas,
            getPersistedAntonioBanderas(partialUpdatedAntonioBanderas)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAntonioBanderas() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        antonioBanderas.setId(longCount.incrementAndGet());

        // Create the AntonioBanderas
        AntonioBanderasDTO antonioBanderasDTO = antonioBanderasMapper.toDto(antonioBanderas);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAntonioBanderasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, antonioBanderasDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(antonioBanderasDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AntonioBanderas in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAntonioBanderas() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        antonioBanderas.setId(longCount.incrementAndGet());

        // Create the AntonioBanderas
        AntonioBanderasDTO antonioBanderasDTO = antonioBanderasMapper.toDto(antonioBanderas);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAntonioBanderasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(antonioBanderasDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AntonioBanderas in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAntonioBanderas() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        antonioBanderas.setId(longCount.incrementAndGet());

        // Create the AntonioBanderas
        AntonioBanderasDTO antonioBanderasDTO = antonioBanderasMapper.toDto(antonioBanderas);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAntonioBanderasMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(antonioBanderasDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AntonioBanderas in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAntonioBanderas() throws Exception {
        // Initialize the database
        insertedAntonioBanderas = antonioBanderasRepository.saveAndFlush(antonioBanderas);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the antonioBanderas
        restAntonioBanderasMockMvc
            .perform(delete(ENTITY_API_URL_ID, antonioBanderas.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return antonioBanderasRepository.count();
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

    protected AntonioBanderas getPersistedAntonioBanderas(AntonioBanderas antonioBanderas) {
        return antonioBanderasRepository.findById(antonioBanderas.getId()).orElseThrow();
    }

    protected void assertPersistedAntonioBanderasToMatchAllProperties(AntonioBanderas expectedAntonioBanderas) {
        assertAntonioBanderasAllPropertiesEquals(expectedAntonioBanderas, getPersistedAntonioBanderas(expectedAntonioBanderas));
    }

    protected void assertPersistedAntonioBanderasToMatchUpdatableProperties(AntonioBanderas expectedAntonioBanderas) {
        assertAntonioBanderasAllUpdatablePropertiesEquals(expectedAntonioBanderas, getPersistedAntonioBanderas(expectedAntonioBanderas));
    }
}
