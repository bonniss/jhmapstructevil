package ai.realworld.web.rest;

import static ai.realworld.domain.AntonioBanderasViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AntonioBanderasVi;
import ai.realworld.domain.AntonioBanderasVi;
import ai.realworld.repository.AntonioBanderasViRepository;
import ai.realworld.service.AntonioBanderasViService;
import ai.realworld.service.dto.AntonioBanderasViDTO;
import ai.realworld.service.mapper.AntonioBanderasViMapper;
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
 * Integration tests for the {@link AntonioBanderasViResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AntonioBanderasViResourceIT {

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

    private static final String ENTITY_API_URL = "/api/antonio-banderas-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AntonioBanderasViRepository antonioBanderasViRepository;

    @Mock
    private AntonioBanderasViRepository antonioBanderasViRepositoryMock;

    @Autowired
    private AntonioBanderasViMapper antonioBanderasViMapper;

    @Mock
    private AntonioBanderasViService antonioBanderasViServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAntonioBanderasViMockMvc;

    private AntonioBanderasVi antonioBanderasVi;

    private AntonioBanderasVi insertedAntonioBanderasVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AntonioBanderasVi createEntity() {
        return new AntonioBanderasVi()
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
    public static AntonioBanderasVi createUpdatedEntity() {
        return new AntonioBanderasVi()
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
        antonioBanderasVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAntonioBanderasVi != null) {
            antonioBanderasViRepository.delete(insertedAntonioBanderasVi);
            insertedAntonioBanderasVi = null;
        }
    }

    @Test
    @Transactional
    void createAntonioBanderasVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AntonioBanderasVi
        AntonioBanderasViDTO antonioBanderasViDTO = antonioBanderasViMapper.toDto(antonioBanderasVi);
        var returnedAntonioBanderasViDTO = om.readValue(
            restAntonioBanderasViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(antonioBanderasViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AntonioBanderasViDTO.class
        );

        // Validate the AntonioBanderasVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAntonioBanderasVi = antonioBanderasViMapper.toEntity(returnedAntonioBanderasViDTO);
        assertAntonioBanderasViUpdatableFieldsEquals(returnedAntonioBanderasVi, getPersistedAntonioBanderasVi(returnedAntonioBanderasVi));

        insertedAntonioBanderasVi = returnedAntonioBanderasVi;
    }

    @Test
    @Transactional
    void createAntonioBanderasViWithExistingId() throws Exception {
        // Create the AntonioBanderasVi with an existing ID
        antonioBanderasVi.setId(1L);
        AntonioBanderasViDTO antonioBanderasViDTO = antonioBanderasViMapper.toDto(antonioBanderasVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAntonioBanderasViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(antonioBanderasViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AntonioBanderasVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        antonioBanderasVi.setCode(null);

        // Create the AntonioBanderasVi, which fails.
        AntonioBanderasViDTO antonioBanderasViDTO = antonioBanderasViMapper.toDto(antonioBanderasVi);

        restAntonioBanderasViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(antonioBanderasViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVis() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList
        restAntonioBanderasViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(antonioBanderasVi.getId().intValue())))
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
    void getAllAntonioBanderasVisWithEagerRelationshipsIsEnabled() throws Exception {
        when(antonioBanderasViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAntonioBanderasViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(antonioBanderasViServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAntonioBanderasVisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(antonioBanderasViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAntonioBanderasViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(antonioBanderasViRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAntonioBanderasVi() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get the antonioBanderasVi
        restAntonioBanderasViMockMvc
            .perform(get(ENTITY_API_URL_ID, antonioBanderasVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(antonioBanderasVi.getId().intValue()))
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
    void getAntonioBanderasVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        Long id = antonioBanderasVi.getId();

        defaultAntonioBanderasViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAntonioBanderasViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAntonioBanderasViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where level equals to
        defaultAntonioBanderasViFiltering("level.equals=" + DEFAULT_LEVEL, "level.equals=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByLevelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where level in
        defaultAntonioBanderasViFiltering("level.in=" + DEFAULT_LEVEL + "," + UPDATED_LEVEL, "level.in=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where level is not null
        defaultAntonioBanderasViFiltering("level.specified=true", "level.specified=false");
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByLevelIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where level is greater than or equal to
        defaultAntonioBanderasViFiltering("level.greaterThanOrEqual=" + DEFAULT_LEVEL, "level.greaterThanOrEqual=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByLevelIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where level is less than or equal to
        defaultAntonioBanderasViFiltering("level.lessThanOrEqual=" + DEFAULT_LEVEL, "level.lessThanOrEqual=" + SMALLER_LEVEL);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByLevelIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where level is less than
        defaultAntonioBanderasViFiltering("level.lessThan=" + UPDATED_LEVEL, "level.lessThan=" + DEFAULT_LEVEL);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByLevelIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where level is greater than
        defaultAntonioBanderasViFiltering("level.greaterThan=" + SMALLER_LEVEL, "level.greaterThan=" + DEFAULT_LEVEL);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where code equals to
        defaultAntonioBanderasViFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where code in
        defaultAntonioBanderasViFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where code is not null
        defaultAntonioBanderasViFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where code contains
        defaultAntonioBanderasViFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where code does not contain
        defaultAntonioBanderasViFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where name equals to
        defaultAntonioBanderasViFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where name in
        defaultAntonioBanderasViFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where name is not null
        defaultAntonioBanderasViFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where name contains
        defaultAntonioBanderasViFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where name does not contain
        defaultAntonioBanderasViFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByFullNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where fullName equals to
        defaultAntonioBanderasViFiltering("fullName.equals=" + DEFAULT_FULL_NAME, "fullName.equals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByFullNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where fullName in
        defaultAntonioBanderasViFiltering("fullName.in=" + DEFAULT_FULL_NAME + "," + UPDATED_FULL_NAME, "fullName.in=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByFullNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where fullName is not null
        defaultAntonioBanderasViFiltering("fullName.specified=true", "fullName.specified=false");
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByFullNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where fullName contains
        defaultAntonioBanderasViFiltering("fullName.contains=" + DEFAULT_FULL_NAME, "fullName.contains=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByFullNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where fullName does not contain
        defaultAntonioBanderasViFiltering("fullName.doesNotContain=" + UPDATED_FULL_NAME, "fullName.doesNotContain=" + DEFAULT_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByNativeNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where nativeName equals to
        defaultAntonioBanderasViFiltering("nativeName.equals=" + DEFAULT_NATIVE_NAME, "nativeName.equals=" + UPDATED_NATIVE_NAME);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByNativeNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where nativeName in
        defaultAntonioBanderasViFiltering(
            "nativeName.in=" + DEFAULT_NATIVE_NAME + "," + UPDATED_NATIVE_NAME,
            "nativeName.in=" + UPDATED_NATIVE_NAME
        );
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByNativeNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where nativeName is not null
        defaultAntonioBanderasViFiltering("nativeName.specified=true", "nativeName.specified=false");
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByNativeNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where nativeName contains
        defaultAntonioBanderasViFiltering("nativeName.contains=" + DEFAULT_NATIVE_NAME, "nativeName.contains=" + UPDATED_NATIVE_NAME);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByNativeNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where nativeName does not contain
        defaultAntonioBanderasViFiltering(
            "nativeName.doesNotContain=" + UPDATED_NATIVE_NAME,
            "nativeName.doesNotContain=" + DEFAULT_NATIVE_NAME
        );
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByOfficialCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where officialCode equals to
        defaultAntonioBanderasViFiltering("officialCode.equals=" + DEFAULT_OFFICIAL_CODE, "officialCode.equals=" + UPDATED_OFFICIAL_CODE);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByOfficialCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where officialCode in
        defaultAntonioBanderasViFiltering(
            "officialCode.in=" + DEFAULT_OFFICIAL_CODE + "," + UPDATED_OFFICIAL_CODE,
            "officialCode.in=" + UPDATED_OFFICIAL_CODE
        );
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByOfficialCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where officialCode is not null
        defaultAntonioBanderasViFiltering("officialCode.specified=true", "officialCode.specified=false");
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByOfficialCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where officialCode contains
        defaultAntonioBanderasViFiltering(
            "officialCode.contains=" + DEFAULT_OFFICIAL_CODE,
            "officialCode.contains=" + UPDATED_OFFICIAL_CODE
        );
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByOfficialCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where officialCode does not contain
        defaultAntonioBanderasViFiltering(
            "officialCode.doesNotContain=" + UPDATED_OFFICIAL_CODE,
            "officialCode.doesNotContain=" + DEFAULT_OFFICIAL_CODE
        );
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByDivisionTermIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where divisionTerm equals to
        defaultAntonioBanderasViFiltering("divisionTerm.equals=" + DEFAULT_DIVISION_TERM, "divisionTerm.equals=" + UPDATED_DIVISION_TERM);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByDivisionTermIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where divisionTerm in
        defaultAntonioBanderasViFiltering(
            "divisionTerm.in=" + DEFAULT_DIVISION_TERM + "," + UPDATED_DIVISION_TERM,
            "divisionTerm.in=" + UPDATED_DIVISION_TERM
        );
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByDivisionTermIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where divisionTerm is not null
        defaultAntonioBanderasViFiltering("divisionTerm.specified=true", "divisionTerm.specified=false");
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByDivisionTermContainsSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where divisionTerm contains
        defaultAntonioBanderasViFiltering(
            "divisionTerm.contains=" + DEFAULT_DIVISION_TERM,
            "divisionTerm.contains=" + UPDATED_DIVISION_TERM
        );
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByDivisionTermNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where divisionTerm does not contain
        defaultAntonioBanderasViFiltering(
            "divisionTerm.doesNotContain=" + UPDATED_DIVISION_TERM,
            "divisionTerm.doesNotContain=" + DEFAULT_DIVISION_TERM
        );
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByIsDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where isDeleted equals to
        defaultAntonioBanderasViFiltering("isDeleted.equals=" + DEFAULT_IS_DELETED, "isDeleted.equals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByIsDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where isDeleted in
        defaultAntonioBanderasViFiltering(
            "isDeleted.in=" + DEFAULT_IS_DELETED + "," + UPDATED_IS_DELETED,
            "isDeleted.in=" + UPDATED_IS_DELETED
        );
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByIsDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        // Get all the antonioBanderasViList where isDeleted is not null
        defaultAntonioBanderasViFiltering("isDeleted.specified=true", "isDeleted.specified=false");
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByCurrentIsEqualToSomething() throws Exception {
        AntonioBanderasVi current;
        if (TestUtil.findAll(em, AntonioBanderasVi.class).isEmpty()) {
            antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);
            current = AntonioBanderasViResourceIT.createEntity();
        } else {
            current = TestUtil.findAll(em, AntonioBanderasVi.class).get(0);
        }
        em.persist(current);
        em.flush();
        antonioBanderasVi.setCurrent(current);
        antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);
        Long currentId = current.getId();
        // Get all the antonioBanderasViList where current equals to currentId
        defaultAntonioBanderasViShouldBeFound("currentId.equals=" + currentId);

        // Get all the antonioBanderasViList where current equals to (currentId + 1)
        defaultAntonioBanderasViShouldNotBeFound("currentId.equals=" + (currentId + 1));
    }

    @Test
    @Transactional
    void getAllAntonioBanderasVisByParentIsEqualToSomething() throws Exception {
        AntonioBanderasVi parent;
        if (TestUtil.findAll(em, AntonioBanderasVi.class).isEmpty()) {
            antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);
            parent = AntonioBanderasViResourceIT.createEntity();
        } else {
            parent = TestUtil.findAll(em, AntonioBanderasVi.class).get(0);
        }
        em.persist(parent);
        em.flush();
        antonioBanderasVi.setParent(parent);
        antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);
        Long parentId = parent.getId();
        // Get all the antonioBanderasViList where parent equals to parentId
        defaultAntonioBanderasViShouldBeFound("parentId.equals=" + parentId);

        // Get all the antonioBanderasViList where parent equals to (parentId + 1)
        defaultAntonioBanderasViShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    private void defaultAntonioBanderasViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAntonioBanderasViShouldBeFound(shouldBeFound);
        defaultAntonioBanderasViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAntonioBanderasViShouldBeFound(String filter) throws Exception {
        restAntonioBanderasViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(antonioBanderasVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].nativeName").value(hasItem(DEFAULT_NATIVE_NAME)))
            .andExpect(jsonPath("$.[*].officialCode").value(hasItem(DEFAULT_OFFICIAL_CODE)))
            .andExpect(jsonPath("$.[*].divisionTerm").value(hasItem(DEFAULT_DIVISION_TERM)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));

        // Check, that the count call also returns 1
        restAntonioBanderasViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAntonioBanderasViShouldNotBeFound(String filter) throws Exception {
        restAntonioBanderasViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAntonioBanderasViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAntonioBanderasVi() throws Exception {
        // Get the antonioBanderasVi
        restAntonioBanderasViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAntonioBanderasVi() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the antonioBanderasVi
        AntonioBanderasVi updatedAntonioBanderasVi = antonioBanderasViRepository.findById(antonioBanderasVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAntonioBanderasVi are not directly saved in db
        em.detach(updatedAntonioBanderasVi);
        updatedAntonioBanderasVi
            .level(UPDATED_LEVEL)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .fullName(UPDATED_FULL_NAME)
            .nativeName(UPDATED_NATIVE_NAME)
            .officialCode(UPDATED_OFFICIAL_CODE)
            .divisionTerm(UPDATED_DIVISION_TERM)
            .isDeleted(UPDATED_IS_DELETED);
        AntonioBanderasViDTO antonioBanderasViDTO = antonioBanderasViMapper.toDto(updatedAntonioBanderasVi);

        restAntonioBanderasViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, antonioBanderasViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(antonioBanderasViDTO))
            )
            .andExpect(status().isOk());

        // Validate the AntonioBanderasVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAntonioBanderasViToMatchAllProperties(updatedAntonioBanderasVi);
    }

    @Test
    @Transactional
    void putNonExistingAntonioBanderasVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        antonioBanderasVi.setId(longCount.incrementAndGet());

        // Create the AntonioBanderasVi
        AntonioBanderasViDTO antonioBanderasViDTO = antonioBanderasViMapper.toDto(antonioBanderasVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAntonioBanderasViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, antonioBanderasViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(antonioBanderasViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AntonioBanderasVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAntonioBanderasVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        antonioBanderasVi.setId(longCount.incrementAndGet());

        // Create the AntonioBanderasVi
        AntonioBanderasViDTO antonioBanderasViDTO = antonioBanderasViMapper.toDto(antonioBanderasVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAntonioBanderasViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(antonioBanderasViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AntonioBanderasVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAntonioBanderasVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        antonioBanderasVi.setId(longCount.incrementAndGet());

        // Create the AntonioBanderasVi
        AntonioBanderasViDTO antonioBanderasViDTO = antonioBanderasViMapper.toDto(antonioBanderasVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAntonioBanderasViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(antonioBanderasViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AntonioBanderasVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAntonioBanderasViWithPatch() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the antonioBanderasVi using partial update
        AntonioBanderasVi partialUpdatedAntonioBanderasVi = new AntonioBanderasVi();
        partialUpdatedAntonioBanderasVi.setId(antonioBanderasVi.getId());

        partialUpdatedAntonioBanderasVi
            .fullName(UPDATED_FULL_NAME)
            .officialCode(UPDATED_OFFICIAL_CODE)
            .divisionTerm(UPDATED_DIVISION_TERM)
            .isDeleted(UPDATED_IS_DELETED);

        restAntonioBanderasViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAntonioBanderasVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAntonioBanderasVi))
            )
            .andExpect(status().isOk());

        // Validate the AntonioBanderasVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAntonioBanderasViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAntonioBanderasVi, antonioBanderasVi),
            getPersistedAntonioBanderasVi(antonioBanderasVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateAntonioBanderasViWithPatch() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the antonioBanderasVi using partial update
        AntonioBanderasVi partialUpdatedAntonioBanderasVi = new AntonioBanderasVi();
        partialUpdatedAntonioBanderasVi.setId(antonioBanderasVi.getId());

        partialUpdatedAntonioBanderasVi
            .level(UPDATED_LEVEL)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .fullName(UPDATED_FULL_NAME)
            .nativeName(UPDATED_NATIVE_NAME)
            .officialCode(UPDATED_OFFICIAL_CODE)
            .divisionTerm(UPDATED_DIVISION_TERM)
            .isDeleted(UPDATED_IS_DELETED);

        restAntonioBanderasViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAntonioBanderasVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAntonioBanderasVi))
            )
            .andExpect(status().isOk());

        // Validate the AntonioBanderasVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAntonioBanderasViUpdatableFieldsEquals(
            partialUpdatedAntonioBanderasVi,
            getPersistedAntonioBanderasVi(partialUpdatedAntonioBanderasVi)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAntonioBanderasVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        antonioBanderasVi.setId(longCount.incrementAndGet());

        // Create the AntonioBanderasVi
        AntonioBanderasViDTO antonioBanderasViDTO = antonioBanderasViMapper.toDto(antonioBanderasVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAntonioBanderasViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, antonioBanderasViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(antonioBanderasViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AntonioBanderasVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAntonioBanderasVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        antonioBanderasVi.setId(longCount.incrementAndGet());

        // Create the AntonioBanderasVi
        AntonioBanderasViDTO antonioBanderasViDTO = antonioBanderasViMapper.toDto(antonioBanderasVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAntonioBanderasViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(antonioBanderasViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AntonioBanderasVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAntonioBanderasVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        antonioBanderasVi.setId(longCount.incrementAndGet());

        // Create the AntonioBanderasVi
        AntonioBanderasViDTO antonioBanderasViDTO = antonioBanderasViMapper.toDto(antonioBanderasVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAntonioBanderasViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(antonioBanderasViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AntonioBanderasVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAntonioBanderasVi() throws Exception {
        // Initialize the database
        insertedAntonioBanderasVi = antonioBanderasViRepository.saveAndFlush(antonioBanderasVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the antonioBanderasVi
        restAntonioBanderasViMockMvc
            .perform(delete(ENTITY_API_URL_ID, antonioBanderasVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return antonioBanderasViRepository.count();
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

    protected AntonioBanderasVi getPersistedAntonioBanderasVi(AntonioBanderasVi antonioBanderasVi) {
        return antonioBanderasViRepository.findById(antonioBanderasVi.getId()).orElseThrow();
    }

    protected void assertPersistedAntonioBanderasViToMatchAllProperties(AntonioBanderasVi expectedAntonioBanderasVi) {
        assertAntonioBanderasViAllPropertiesEquals(expectedAntonioBanderasVi, getPersistedAntonioBanderasVi(expectedAntonioBanderasVi));
    }

    protected void assertPersistedAntonioBanderasViToMatchUpdatableProperties(AntonioBanderasVi expectedAntonioBanderasVi) {
        assertAntonioBanderasViAllUpdatablePropertiesEquals(
            expectedAntonioBanderasVi,
            getPersistedAntonioBanderasVi(expectedAntonioBanderasVi)
        );
    }
}
