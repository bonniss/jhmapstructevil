package ai.realworld.web.rest;

import static ai.realworld.domain.AlProProViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlLadyGagaVi;
import ai.realworld.domain.AlMenityVi;
import ai.realworld.domain.AlProProVi;
import ai.realworld.domain.AlProProVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.domain.enumeration.BenedictToy;
import ai.realworld.domain.enumeration.PeteType;
import ai.realworld.repository.AlProProViRepository;
import ai.realworld.service.AlProProViService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.UUID;
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
 * Integration tests for the {@link AlProProViResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AlProProViResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION_HEITIGA = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_HEITIGA = "BBBBBBBBBB";

    private static final PeteType DEFAULT_PROPERTY_TYPE = PeteType.VILLA;
    private static final PeteType UPDATED_PROPERTY_TYPE = PeteType.ROOM;

    private static final Float DEFAULT_AREA_IN_SQUARE_METER = 1F;
    private static final Float UPDATED_AREA_IN_SQUARE_METER = 2F;
    private static final Float SMALLER_AREA_IN_SQUARE_METER = 1F - 1F;

    private static final Integer DEFAULT_NUMBER_OF_ADULTS = 1;
    private static final Integer UPDATED_NUMBER_OF_ADULTS = 2;
    private static final Integer SMALLER_NUMBER_OF_ADULTS = 1 - 1;

    private static final Integer DEFAULT_NUMBER_OF_PRESCHOOLERS = 0;
    private static final Integer UPDATED_NUMBER_OF_PRESCHOOLERS = 1;
    private static final Integer SMALLER_NUMBER_OF_PRESCHOOLERS = 0 - 1;

    private static final Integer DEFAULT_NUMBER_OF_CHILDREN = 0;
    private static final Integer UPDATED_NUMBER_OF_CHILDREN = 1;
    private static final Integer SMALLER_NUMBER_OF_CHILDREN = 0 - 1;

    private static final Integer DEFAULT_NUMBER_OF_ROOMS = 1;
    private static final Integer UPDATED_NUMBER_OF_ROOMS = 2;
    private static final Integer SMALLER_NUMBER_OF_ROOMS = 1 - 1;

    private static final Integer DEFAULT_NUMBER_OF_FLOORS = 1;
    private static final Integer UPDATED_NUMBER_OF_FLOORS = 2;
    private static final Integer SMALLER_NUMBER_OF_FLOORS = 1 - 1;

    private static final BenedictToy DEFAULT_BED_SIZE = BenedictToy.SINGLE;
    private static final BenedictToy UPDATED_BED_SIZE = BenedictToy.DOUBLE;

    private static final Boolean DEFAULT_IS_ENABLED = false;
    private static final Boolean UPDATED_IS_ENABLED = true;

    private static final String ENTITY_API_URL = "/api/al-pro-pro-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlProProViRepository alProProViRepository;

    @Mock
    private AlProProViRepository alProProViRepositoryMock;

    @Mock
    private AlProProViService alProProViServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlProProViMockMvc;

    private AlProProVi alProProVi;

    private AlProProVi insertedAlProProVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlProProVi createEntity() {
        return new AlProProVi()
            .name(DEFAULT_NAME)
            .descriptionHeitiga(DEFAULT_DESCRIPTION_HEITIGA)
            .propertyType(DEFAULT_PROPERTY_TYPE)
            .areaInSquareMeter(DEFAULT_AREA_IN_SQUARE_METER)
            .numberOfAdults(DEFAULT_NUMBER_OF_ADULTS)
            .numberOfPreschoolers(DEFAULT_NUMBER_OF_PRESCHOOLERS)
            .numberOfChildren(DEFAULT_NUMBER_OF_CHILDREN)
            .numberOfRooms(DEFAULT_NUMBER_OF_ROOMS)
            .numberOfFloors(DEFAULT_NUMBER_OF_FLOORS)
            .bedSize(DEFAULT_BED_SIZE)
            .isEnabled(DEFAULT_IS_ENABLED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlProProVi createUpdatedEntity() {
        return new AlProProVi()
            .name(UPDATED_NAME)
            .descriptionHeitiga(UPDATED_DESCRIPTION_HEITIGA)
            .propertyType(UPDATED_PROPERTY_TYPE)
            .areaInSquareMeter(UPDATED_AREA_IN_SQUARE_METER)
            .numberOfAdults(UPDATED_NUMBER_OF_ADULTS)
            .numberOfPreschoolers(UPDATED_NUMBER_OF_PRESCHOOLERS)
            .numberOfChildren(UPDATED_NUMBER_OF_CHILDREN)
            .numberOfRooms(UPDATED_NUMBER_OF_ROOMS)
            .numberOfFloors(UPDATED_NUMBER_OF_FLOORS)
            .bedSize(UPDATED_BED_SIZE)
            .isEnabled(UPDATED_IS_ENABLED);
    }

    @BeforeEach
    public void initTest() {
        alProProVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlProProVi != null) {
            alProProViRepository.delete(insertedAlProProVi);
            insertedAlProProVi = null;
        }
    }

    @Test
    @Transactional
    void createAlProProVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlProProVi
        var returnedAlProProVi = om.readValue(
            restAlProProViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alProProVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlProProVi.class
        );

        // Validate the AlProProVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlProProViUpdatableFieldsEquals(returnedAlProProVi, getPersistedAlProProVi(returnedAlProProVi));

        insertedAlProProVi = returnedAlProProVi;
    }

    @Test
    @Transactional
    void createAlProProViWithExistingId() throws Exception {
        // Create the AlProProVi with an existing ID
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlProProViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alProProVi)))
            .andExpect(status().isBadRequest());

        // Validate the AlProProVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alProProVi.setName(null);

        // Create the AlProProVi, which fails.

        restAlProProViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alProProVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlProProVis() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList
        restAlProProViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alProProVi.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].descriptionHeitiga").value(hasItem(DEFAULT_DESCRIPTION_HEITIGA)))
            .andExpect(jsonPath("$.[*].propertyType").value(hasItem(DEFAULT_PROPERTY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].areaInSquareMeter").value(hasItem(DEFAULT_AREA_IN_SQUARE_METER.doubleValue())))
            .andExpect(jsonPath("$.[*].numberOfAdults").value(hasItem(DEFAULT_NUMBER_OF_ADULTS)))
            .andExpect(jsonPath("$.[*].numberOfPreschoolers").value(hasItem(DEFAULT_NUMBER_OF_PRESCHOOLERS)))
            .andExpect(jsonPath("$.[*].numberOfChildren").value(hasItem(DEFAULT_NUMBER_OF_CHILDREN)))
            .andExpect(jsonPath("$.[*].numberOfRooms").value(hasItem(DEFAULT_NUMBER_OF_ROOMS)))
            .andExpect(jsonPath("$.[*].numberOfFloors").value(hasItem(DEFAULT_NUMBER_OF_FLOORS)))
            .andExpect(jsonPath("$.[*].bedSize").value(hasItem(DEFAULT_BED_SIZE.toString())))
            .andExpect(jsonPath("$.[*].isEnabled").value(hasItem(DEFAULT_IS_ENABLED.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAlProProVisWithEagerRelationshipsIsEnabled() throws Exception {
        when(alProProViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAlProProViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(alProProViServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAlProProVisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(alProProViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAlProProViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(alProProViRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAlProProVi() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get the alProProVi
        restAlProProViMockMvc
            .perform(get(ENTITY_API_URL_ID, alProProVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alProProVi.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.descriptionHeitiga").value(DEFAULT_DESCRIPTION_HEITIGA))
            .andExpect(jsonPath("$.propertyType").value(DEFAULT_PROPERTY_TYPE.toString()))
            .andExpect(jsonPath("$.areaInSquareMeter").value(DEFAULT_AREA_IN_SQUARE_METER.doubleValue()))
            .andExpect(jsonPath("$.numberOfAdults").value(DEFAULT_NUMBER_OF_ADULTS))
            .andExpect(jsonPath("$.numberOfPreschoolers").value(DEFAULT_NUMBER_OF_PRESCHOOLERS))
            .andExpect(jsonPath("$.numberOfChildren").value(DEFAULT_NUMBER_OF_CHILDREN))
            .andExpect(jsonPath("$.numberOfRooms").value(DEFAULT_NUMBER_OF_ROOMS))
            .andExpect(jsonPath("$.numberOfFloors").value(DEFAULT_NUMBER_OF_FLOORS))
            .andExpect(jsonPath("$.bedSize").value(DEFAULT_BED_SIZE.toString()))
            .andExpect(jsonPath("$.isEnabled").value(DEFAULT_IS_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    void getAlProProVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        UUID id = alProProVi.getId();

        defaultAlProProViFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllAlProProVisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where name equals to
        defaultAlProProViFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlProProVisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where name in
        defaultAlProProViFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlProProVisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where name is not null
        defaultAlProProViFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAlProProVisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where name contains
        defaultAlProProViFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlProProVisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where name does not contain
        defaultAlProProViFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAlProProVisByDescriptionHeitigaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where descriptionHeitiga equals to
        defaultAlProProViFiltering(
            "descriptionHeitiga.equals=" + DEFAULT_DESCRIPTION_HEITIGA,
            "descriptionHeitiga.equals=" + UPDATED_DESCRIPTION_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByDescriptionHeitigaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where descriptionHeitiga in
        defaultAlProProViFiltering(
            "descriptionHeitiga.in=" + DEFAULT_DESCRIPTION_HEITIGA + "," + UPDATED_DESCRIPTION_HEITIGA,
            "descriptionHeitiga.in=" + UPDATED_DESCRIPTION_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByDescriptionHeitigaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where descriptionHeitiga is not null
        defaultAlProProViFiltering("descriptionHeitiga.specified=true", "descriptionHeitiga.specified=false");
    }

    @Test
    @Transactional
    void getAllAlProProVisByDescriptionHeitigaContainsSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where descriptionHeitiga contains
        defaultAlProProViFiltering(
            "descriptionHeitiga.contains=" + DEFAULT_DESCRIPTION_HEITIGA,
            "descriptionHeitiga.contains=" + UPDATED_DESCRIPTION_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByDescriptionHeitigaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where descriptionHeitiga does not contain
        defaultAlProProViFiltering(
            "descriptionHeitiga.doesNotContain=" + UPDATED_DESCRIPTION_HEITIGA,
            "descriptionHeitiga.doesNotContain=" + DEFAULT_DESCRIPTION_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByPropertyTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where propertyType equals to
        defaultAlProProViFiltering("propertyType.equals=" + DEFAULT_PROPERTY_TYPE, "propertyType.equals=" + UPDATED_PROPERTY_TYPE);
    }

    @Test
    @Transactional
    void getAllAlProProVisByPropertyTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where propertyType in
        defaultAlProProViFiltering(
            "propertyType.in=" + DEFAULT_PROPERTY_TYPE + "," + UPDATED_PROPERTY_TYPE,
            "propertyType.in=" + UPDATED_PROPERTY_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByPropertyTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where propertyType is not null
        defaultAlProProViFiltering("propertyType.specified=true", "propertyType.specified=false");
    }

    @Test
    @Transactional
    void getAllAlProProVisByAreaInSquareMeterIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where areaInSquareMeter equals to
        defaultAlProProViFiltering(
            "areaInSquareMeter.equals=" + DEFAULT_AREA_IN_SQUARE_METER,
            "areaInSquareMeter.equals=" + UPDATED_AREA_IN_SQUARE_METER
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByAreaInSquareMeterIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where areaInSquareMeter in
        defaultAlProProViFiltering(
            "areaInSquareMeter.in=" + DEFAULT_AREA_IN_SQUARE_METER + "," + UPDATED_AREA_IN_SQUARE_METER,
            "areaInSquareMeter.in=" + UPDATED_AREA_IN_SQUARE_METER
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByAreaInSquareMeterIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where areaInSquareMeter is not null
        defaultAlProProViFiltering("areaInSquareMeter.specified=true", "areaInSquareMeter.specified=false");
    }

    @Test
    @Transactional
    void getAllAlProProVisByAreaInSquareMeterIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where areaInSquareMeter is greater than or equal to
        defaultAlProProViFiltering(
            "areaInSquareMeter.greaterThanOrEqual=" + DEFAULT_AREA_IN_SQUARE_METER,
            "areaInSquareMeter.greaterThanOrEqual=" + UPDATED_AREA_IN_SQUARE_METER
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByAreaInSquareMeterIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where areaInSquareMeter is less than or equal to
        defaultAlProProViFiltering(
            "areaInSquareMeter.lessThanOrEqual=" + DEFAULT_AREA_IN_SQUARE_METER,
            "areaInSquareMeter.lessThanOrEqual=" + SMALLER_AREA_IN_SQUARE_METER
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByAreaInSquareMeterIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where areaInSquareMeter is less than
        defaultAlProProViFiltering(
            "areaInSquareMeter.lessThan=" + UPDATED_AREA_IN_SQUARE_METER,
            "areaInSquareMeter.lessThan=" + DEFAULT_AREA_IN_SQUARE_METER
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByAreaInSquareMeterIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where areaInSquareMeter is greater than
        defaultAlProProViFiltering(
            "areaInSquareMeter.greaterThan=" + SMALLER_AREA_IN_SQUARE_METER,
            "areaInSquareMeter.greaterThan=" + DEFAULT_AREA_IN_SQUARE_METER
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByNumberOfAdultsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where numberOfAdults equals to
        defaultAlProProViFiltering(
            "numberOfAdults.equals=" + DEFAULT_NUMBER_OF_ADULTS,
            "numberOfAdults.equals=" + UPDATED_NUMBER_OF_ADULTS
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByNumberOfAdultsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where numberOfAdults in
        defaultAlProProViFiltering(
            "numberOfAdults.in=" + DEFAULT_NUMBER_OF_ADULTS + "," + UPDATED_NUMBER_OF_ADULTS,
            "numberOfAdults.in=" + UPDATED_NUMBER_OF_ADULTS
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByNumberOfAdultsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where numberOfAdults is not null
        defaultAlProProViFiltering("numberOfAdults.specified=true", "numberOfAdults.specified=false");
    }

    @Test
    @Transactional
    void getAllAlProProVisByNumberOfAdultsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where numberOfAdults is greater than or equal to
        defaultAlProProViFiltering(
            "numberOfAdults.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_ADULTS,
            "numberOfAdults.greaterThanOrEqual=" + UPDATED_NUMBER_OF_ADULTS
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByNumberOfAdultsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where numberOfAdults is less than or equal to
        defaultAlProProViFiltering(
            "numberOfAdults.lessThanOrEqual=" + DEFAULT_NUMBER_OF_ADULTS,
            "numberOfAdults.lessThanOrEqual=" + SMALLER_NUMBER_OF_ADULTS
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByNumberOfAdultsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where numberOfAdults is less than
        defaultAlProProViFiltering(
            "numberOfAdults.lessThan=" + UPDATED_NUMBER_OF_ADULTS,
            "numberOfAdults.lessThan=" + DEFAULT_NUMBER_OF_ADULTS
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByNumberOfAdultsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where numberOfAdults is greater than
        defaultAlProProViFiltering(
            "numberOfAdults.greaterThan=" + SMALLER_NUMBER_OF_ADULTS,
            "numberOfAdults.greaterThan=" + DEFAULT_NUMBER_OF_ADULTS
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByNumberOfPreschoolersIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where numberOfPreschoolers equals to
        defaultAlProProViFiltering(
            "numberOfPreschoolers.equals=" + DEFAULT_NUMBER_OF_PRESCHOOLERS,
            "numberOfPreschoolers.equals=" + UPDATED_NUMBER_OF_PRESCHOOLERS
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByNumberOfPreschoolersIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where numberOfPreschoolers in
        defaultAlProProViFiltering(
            "numberOfPreschoolers.in=" + DEFAULT_NUMBER_OF_PRESCHOOLERS + "," + UPDATED_NUMBER_OF_PRESCHOOLERS,
            "numberOfPreschoolers.in=" + UPDATED_NUMBER_OF_PRESCHOOLERS
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByNumberOfPreschoolersIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where numberOfPreschoolers is not null
        defaultAlProProViFiltering("numberOfPreschoolers.specified=true", "numberOfPreschoolers.specified=false");
    }

    @Test
    @Transactional
    void getAllAlProProVisByNumberOfPreschoolersIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where numberOfPreschoolers is greater than or equal to
        defaultAlProProViFiltering(
            "numberOfPreschoolers.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_PRESCHOOLERS,
            "numberOfPreschoolers.greaterThanOrEqual=" + UPDATED_NUMBER_OF_PRESCHOOLERS
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByNumberOfPreschoolersIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where numberOfPreschoolers is less than or equal to
        defaultAlProProViFiltering(
            "numberOfPreschoolers.lessThanOrEqual=" + DEFAULT_NUMBER_OF_PRESCHOOLERS,
            "numberOfPreschoolers.lessThanOrEqual=" + SMALLER_NUMBER_OF_PRESCHOOLERS
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByNumberOfPreschoolersIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where numberOfPreschoolers is less than
        defaultAlProProViFiltering(
            "numberOfPreschoolers.lessThan=" + UPDATED_NUMBER_OF_PRESCHOOLERS,
            "numberOfPreschoolers.lessThan=" + DEFAULT_NUMBER_OF_PRESCHOOLERS
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByNumberOfPreschoolersIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where numberOfPreschoolers is greater than
        defaultAlProProViFiltering(
            "numberOfPreschoolers.greaterThan=" + SMALLER_NUMBER_OF_PRESCHOOLERS,
            "numberOfPreschoolers.greaterThan=" + DEFAULT_NUMBER_OF_PRESCHOOLERS
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByNumberOfChildrenIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where numberOfChildren equals to
        defaultAlProProViFiltering(
            "numberOfChildren.equals=" + DEFAULT_NUMBER_OF_CHILDREN,
            "numberOfChildren.equals=" + UPDATED_NUMBER_OF_CHILDREN
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByNumberOfChildrenIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where numberOfChildren in
        defaultAlProProViFiltering(
            "numberOfChildren.in=" + DEFAULT_NUMBER_OF_CHILDREN + "," + UPDATED_NUMBER_OF_CHILDREN,
            "numberOfChildren.in=" + UPDATED_NUMBER_OF_CHILDREN
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByNumberOfChildrenIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where numberOfChildren is not null
        defaultAlProProViFiltering("numberOfChildren.specified=true", "numberOfChildren.specified=false");
    }

    @Test
    @Transactional
    void getAllAlProProVisByNumberOfChildrenIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where numberOfChildren is greater than or equal to
        defaultAlProProViFiltering(
            "numberOfChildren.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_CHILDREN,
            "numberOfChildren.greaterThanOrEqual=" + UPDATED_NUMBER_OF_CHILDREN
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByNumberOfChildrenIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where numberOfChildren is less than or equal to
        defaultAlProProViFiltering(
            "numberOfChildren.lessThanOrEqual=" + DEFAULT_NUMBER_OF_CHILDREN,
            "numberOfChildren.lessThanOrEqual=" + SMALLER_NUMBER_OF_CHILDREN
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByNumberOfChildrenIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where numberOfChildren is less than
        defaultAlProProViFiltering(
            "numberOfChildren.lessThan=" + UPDATED_NUMBER_OF_CHILDREN,
            "numberOfChildren.lessThan=" + DEFAULT_NUMBER_OF_CHILDREN
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByNumberOfChildrenIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where numberOfChildren is greater than
        defaultAlProProViFiltering(
            "numberOfChildren.greaterThan=" + SMALLER_NUMBER_OF_CHILDREN,
            "numberOfChildren.greaterThan=" + DEFAULT_NUMBER_OF_CHILDREN
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByNumberOfRoomsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where numberOfRooms equals to
        defaultAlProProViFiltering("numberOfRooms.equals=" + DEFAULT_NUMBER_OF_ROOMS, "numberOfRooms.equals=" + UPDATED_NUMBER_OF_ROOMS);
    }

    @Test
    @Transactional
    void getAllAlProProVisByNumberOfRoomsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where numberOfRooms in
        defaultAlProProViFiltering(
            "numberOfRooms.in=" + DEFAULT_NUMBER_OF_ROOMS + "," + UPDATED_NUMBER_OF_ROOMS,
            "numberOfRooms.in=" + UPDATED_NUMBER_OF_ROOMS
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByNumberOfRoomsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where numberOfRooms is not null
        defaultAlProProViFiltering("numberOfRooms.specified=true", "numberOfRooms.specified=false");
    }

    @Test
    @Transactional
    void getAllAlProProVisByNumberOfRoomsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where numberOfRooms is greater than or equal to
        defaultAlProProViFiltering(
            "numberOfRooms.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_ROOMS,
            "numberOfRooms.greaterThanOrEqual=" + UPDATED_NUMBER_OF_ROOMS
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByNumberOfRoomsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where numberOfRooms is less than or equal to
        defaultAlProProViFiltering(
            "numberOfRooms.lessThanOrEqual=" + DEFAULT_NUMBER_OF_ROOMS,
            "numberOfRooms.lessThanOrEqual=" + SMALLER_NUMBER_OF_ROOMS
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByNumberOfRoomsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where numberOfRooms is less than
        defaultAlProProViFiltering(
            "numberOfRooms.lessThan=" + UPDATED_NUMBER_OF_ROOMS,
            "numberOfRooms.lessThan=" + DEFAULT_NUMBER_OF_ROOMS
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByNumberOfRoomsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where numberOfRooms is greater than
        defaultAlProProViFiltering(
            "numberOfRooms.greaterThan=" + SMALLER_NUMBER_OF_ROOMS,
            "numberOfRooms.greaterThan=" + DEFAULT_NUMBER_OF_ROOMS
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByNumberOfFloorsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where numberOfFloors equals to
        defaultAlProProViFiltering(
            "numberOfFloors.equals=" + DEFAULT_NUMBER_OF_FLOORS,
            "numberOfFloors.equals=" + UPDATED_NUMBER_OF_FLOORS
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByNumberOfFloorsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where numberOfFloors in
        defaultAlProProViFiltering(
            "numberOfFloors.in=" + DEFAULT_NUMBER_OF_FLOORS + "," + UPDATED_NUMBER_OF_FLOORS,
            "numberOfFloors.in=" + UPDATED_NUMBER_OF_FLOORS
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByNumberOfFloorsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where numberOfFloors is not null
        defaultAlProProViFiltering("numberOfFloors.specified=true", "numberOfFloors.specified=false");
    }

    @Test
    @Transactional
    void getAllAlProProVisByNumberOfFloorsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where numberOfFloors is greater than or equal to
        defaultAlProProViFiltering(
            "numberOfFloors.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_FLOORS,
            "numberOfFloors.greaterThanOrEqual=" + UPDATED_NUMBER_OF_FLOORS
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByNumberOfFloorsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where numberOfFloors is less than or equal to
        defaultAlProProViFiltering(
            "numberOfFloors.lessThanOrEqual=" + DEFAULT_NUMBER_OF_FLOORS,
            "numberOfFloors.lessThanOrEqual=" + SMALLER_NUMBER_OF_FLOORS
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByNumberOfFloorsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where numberOfFloors is less than
        defaultAlProProViFiltering(
            "numberOfFloors.lessThan=" + UPDATED_NUMBER_OF_FLOORS,
            "numberOfFloors.lessThan=" + DEFAULT_NUMBER_OF_FLOORS
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByNumberOfFloorsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where numberOfFloors is greater than
        defaultAlProProViFiltering(
            "numberOfFloors.greaterThan=" + SMALLER_NUMBER_OF_FLOORS,
            "numberOfFloors.greaterThan=" + DEFAULT_NUMBER_OF_FLOORS
        );
    }

    @Test
    @Transactional
    void getAllAlProProVisByBedSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where bedSize equals to
        defaultAlProProViFiltering("bedSize.equals=" + DEFAULT_BED_SIZE, "bedSize.equals=" + UPDATED_BED_SIZE);
    }

    @Test
    @Transactional
    void getAllAlProProVisByBedSizeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where bedSize in
        defaultAlProProViFiltering("bedSize.in=" + DEFAULT_BED_SIZE + "," + UPDATED_BED_SIZE, "bedSize.in=" + UPDATED_BED_SIZE);
    }

    @Test
    @Transactional
    void getAllAlProProVisByBedSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where bedSize is not null
        defaultAlProProViFiltering("bedSize.specified=true", "bedSize.specified=false");
    }

    @Test
    @Transactional
    void getAllAlProProVisByIsEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where isEnabled equals to
        defaultAlProProViFiltering("isEnabled.equals=" + DEFAULT_IS_ENABLED, "isEnabled.equals=" + UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    void getAllAlProProVisByIsEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where isEnabled in
        defaultAlProProViFiltering("isEnabled.in=" + DEFAULT_IS_ENABLED + "," + UPDATED_IS_ENABLED, "isEnabled.in=" + UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    void getAllAlProProVisByIsEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        // Get all the alProProViList where isEnabled is not null
        defaultAlProProViFiltering("isEnabled.specified=true", "isEnabled.specified=false");
    }

    @Test
    @Transactional
    void getAllAlProProVisByParentIsEqualToSomething() throws Exception {
        AlProProVi parent;
        if (TestUtil.findAll(em, AlProProVi.class).isEmpty()) {
            alProProViRepository.saveAndFlush(alProProVi);
            parent = AlProProViResourceIT.createEntity();
        } else {
            parent = TestUtil.findAll(em, AlProProVi.class).get(0);
        }
        em.persist(parent);
        em.flush();
        alProProVi.setParent(parent);
        alProProViRepository.saveAndFlush(alProProVi);
        UUID parentId = parent.getId();
        // Get all the alProProViList where parent equals to parentId
        defaultAlProProViShouldBeFound("parentId.equals=" + parentId);

        // Get all the alProProViList where parent equals to UUID.randomUUID()
        defaultAlProProViShouldNotBeFound("parentId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlProProVisByProjectIsEqualToSomething() throws Exception {
        AlLadyGagaVi project;
        if (TestUtil.findAll(em, AlLadyGagaVi.class).isEmpty()) {
            alProProViRepository.saveAndFlush(alProProVi);
            project = AlLadyGagaViResourceIT.createEntity();
        } else {
            project = TestUtil.findAll(em, AlLadyGagaVi.class).get(0);
        }
        em.persist(project);
        em.flush();
        alProProVi.setProject(project);
        alProProViRepository.saveAndFlush(alProProVi);
        UUID projectId = project.getId();
        // Get all the alProProViList where project equals to projectId
        defaultAlProProViShouldBeFound("projectId.equals=" + projectId);

        // Get all the alProProViList where project equals to UUID.randomUUID()
        defaultAlProProViShouldNotBeFound("projectId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlProProVisByAvatarIsEqualToSomething() throws Exception {
        Metaverse avatar;
        if (TestUtil.findAll(em, Metaverse.class).isEmpty()) {
            alProProViRepository.saveAndFlush(alProProVi);
            avatar = MetaverseResourceIT.createEntity();
        } else {
            avatar = TestUtil.findAll(em, Metaverse.class).get(0);
        }
        em.persist(avatar);
        em.flush();
        alProProVi.setAvatar(avatar);
        alProProViRepository.saveAndFlush(alProProVi);
        Long avatarId = avatar.getId();
        // Get all the alProProViList where avatar equals to avatarId
        defaultAlProProViShouldBeFound("avatarId.equals=" + avatarId);

        // Get all the alProProViList where avatar equals to (avatarId + 1)
        defaultAlProProViShouldNotBeFound("avatarId.equals=" + (avatarId + 1));
    }

    @Test
    @Transactional
    void getAllAlProProVisByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alProProViRepository.saveAndFlush(alProProVi);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alProProVi.setApplication(application);
        alProProViRepository.saveAndFlush(alProProVi);
        UUID applicationId = application.getId();
        // Get all the alProProViList where application equals to applicationId
        defaultAlProProViShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alProProViList where application equals to UUID.randomUUID()
        defaultAlProProViShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlProProVisByAmenityIsEqualToSomething() throws Exception {
        AlMenityVi amenity;
        if (TestUtil.findAll(em, AlMenityVi.class).isEmpty()) {
            alProProViRepository.saveAndFlush(alProProVi);
            amenity = AlMenityViResourceIT.createEntity();
        } else {
            amenity = TestUtil.findAll(em, AlMenityVi.class).get(0);
        }
        em.persist(amenity);
        em.flush();
        alProProVi.addAmenity(amenity);
        alProProViRepository.saveAndFlush(alProProVi);
        Long amenityId = amenity.getId();
        // Get all the alProProViList where amenity equals to amenityId
        defaultAlProProViShouldBeFound("amenityId.equals=" + amenityId);

        // Get all the alProProViList where amenity equals to (amenityId + 1)
        defaultAlProProViShouldNotBeFound("amenityId.equals=" + (amenityId + 1));
    }

    @Test
    @Transactional
    void getAllAlProProVisByImageIsEqualToSomething() throws Exception {
        Metaverse image;
        if (TestUtil.findAll(em, Metaverse.class).isEmpty()) {
            alProProViRepository.saveAndFlush(alProProVi);
            image = MetaverseResourceIT.createEntity();
        } else {
            image = TestUtil.findAll(em, Metaverse.class).get(0);
        }
        em.persist(image);
        em.flush();
        alProProVi.addImage(image);
        alProProViRepository.saveAndFlush(alProProVi);
        Long imageId = image.getId();
        // Get all the alProProViList where image equals to imageId
        defaultAlProProViShouldBeFound("imageId.equals=" + imageId);

        // Get all the alProProViList where image equals to (imageId + 1)
        defaultAlProProViShouldNotBeFound("imageId.equals=" + (imageId + 1));
    }

    private void defaultAlProProViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlProProViShouldBeFound(shouldBeFound);
        defaultAlProProViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlProProViShouldBeFound(String filter) throws Exception {
        restAlProProViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alProProVi.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].descriptionHeitiga").value(hasItem(DEFAULT_DESCRIPTION_HEITIGA)))
            .andExpect(jsonPath("$.[*].propertyType").value(hasItem(DEFAULT_PROPERTY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].areaInSquareMeter").value(hasItem(DEFAULT_AREA_IN_SQUARE_METER.doubleValue())))
            .andExpect(jsonPath("$.[*].numberOfAdults").value(hasItem(DEFAULT_NUMBER_OF_ADULTS)))
            .andExpect(jsonPath("$.[*].numberOfPreschoolers").value(hasItem(DEFAULT_NUMBER_OF_PRESCHOOLERS)))
            .andExpect(jsonPath("$.[*].numberOfChildren").value(hasItem(DEFAULT_NUMBER_OF_CHILDREN)))
            .andExpect(jsonPath("$.[*].numberOfRooms").value(hasItem(DEFAULT_NUMBER_OF_ROOMS)))
            .andExpect(jsonPath("$.[*].numberOfFloors").value(hasItem(DEFAULT_NUMBER_OF_FLOORS)))
            .andExpect(jsonPath("$.[*].bedSize").value(hasItem(DEFAULT_BED_SIZE.toString())))
            .andExpect(jsonPath("$.[*].isEnabled").value(hasItem(DEFAULT_IS_ENABLED.booleanValue())));

        // Check, that the count call also returns 1
        restAlProProViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlProProViShouldNotBeFound(String filter) throws Exception {
        restAlProProViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlProProViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlProProVi() throws Exception {
        // Get the alProProVi
        restAlProProViMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlProProVi() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alProProVi
        AlProProVi updatedAlProProVi = alProProViRepository.findById(alProProVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlProProVi are not directly saved in db
        em.detach(updatedAlProProVi);
        updatedAlProProVi
            .name(UPDATED_NAME)
            .descriptionHeitiga(UPDATED_DESCRIPTION_HEITIGA)
            .propertyType(UPDATED_PROPERTY_TYPE)
            .areaInSquareMeter(UPDATED_AREA_IN_SQUARE_METER)
            .numberOfAdults(UPDATED_NUMBER_OF_ADULTS)
            .numberOfPreschoolers(UPDATED_NUMBER_OF_PRESCHOOLERS)
            .numberOfChildren(UPDATED_NUMBER_OF_CHILDREN)
            .numberOfRooms(UPDATED_NUMBER_OF_ROOMS)
            .numberOfFloors(UPDATED_NUMBER_OF_FLOORS)
            .bedSize(UPDATED_BED_SIZE)
            .isEnabled(UPDATED_IS_ENABLED);

        restAlProProViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlProProVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlProProVi))
            )
            .andExpect(status().isOk());

        // Validate the AlProProVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlProProViToMatchAllProperties(updatedAlProProVi);
    }

    @Test
    @Transactional
    void putNonExistingAlProProVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alProProVi.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlProProViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alProProVi.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alProProVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlProProVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlProProVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alProProVi.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlProProViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alProProVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlProProVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlProProVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alProProVi.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlProProViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alProProVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlProProVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlProProViWithPatch() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alProProVi using partial update
        AlProProVi partialUpdatedAlProProVi = new AlProProVi();
        partialUpdatedAlProProVi.setId(alProProVi.getId());

        partialUpdatedAlProProVi.descriptionHeitiga(UPDATED_DESCRIPTION_HEITIGA).numberOfRooms(UPDATED_NUMBER_OF_ROOMS);

        restAlProProViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlProProVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlProProVi))
            )
            .andExpect(status().isOk());

        // Validate the AlProProVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlProProViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlProProVi, alProProVi),
            getPersistedAlProProVi(alProProVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlProProViWithPatch() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alProProVi using partial update
        AlProProVi partialUpdatedAlProProVi = new AlProProVi();
        partialUpdatedAlProProVi.setId(alProProVi.getId());

        partialUpdatedAlProProVi
            .name(UPDATED_NAME)
            .descriptionHeitiga(UPDATED_DESCRIPTION_HEITIGA)
            .propertyType(UPDATED_PROPERTY_TYPE)
            .areaInSquareMeter(UPDATED_AREA_IN_SQUARE_METER)
            .numberOfAdults(UPDATED_NUMBER_OF_ADULTS)
            .numberOfPreschoolers(UPDATED_NUMBER_OF_PRESCHOOLERS)
            .numberOfChildren(UPDATED_NUMBER_OF_CHILDREN)
            .numberOfRooms(UPDATED_NUMBER_OF_ROOMS)
            .numberOfFloors(UPDATED_NUMBER_OF_FLOORS)
            .bedSize(UPDATED_BED_SIZE)
            .isEnabled(UPDATED_IS_ENABLED);

        restAlProProViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlProProVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlProProVi))
            )
            .andExpect(status().isOk());

        // Validate the AlProProVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlProProViUpdatableFieldsEquals(partialUpdatedAlProProVi, getPersistedAlProProVi(partialUpdatedAlProProVi));
    }

    @Test
    @Transactional
    void patchNonExistingAlProProVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alProProVi.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlProProViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alProProVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alProProVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlProProVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlProProVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alProProVi.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlProProViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alProProVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlProProVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlProProVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alProProVi.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlProProViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alProProVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlProProVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlProProVi() throws Exception {
        // Initialize the database
        insertedAlProProVi = alProProViRepository.saveAndFlush(alProProVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alProProVi
        restAlProProViMockMvc
            .perform(delete(ENTITY_API_URL_ID, alProProVi.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alProProViRepository.count();
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

    protected AlProProVi getPersistedAlProProVi(AlProProVi alProProVi) {
        return alProProViRepository.findById(alProProVi.getId()).orElseThrow();
    }

    protected void assertPersistedAlProProViToMatchAllProperties(AlProProVi expectedAlProProVi) {
        assertAlProProViAllPropertiesEquals(expectedAlProProVi, getPersistedAlProProVi(expectedAlProProVi));
    }

    protected void assertPersistedAlProProViToMatchUpdatableProperties(AlProProVi expectedAlProProVi) {
        assertAlProProViAllUpdatablePropertiesEquals(expectedAlProProVi, getPersistedAlProProVi(expectedAlProProVi));
    }
}
