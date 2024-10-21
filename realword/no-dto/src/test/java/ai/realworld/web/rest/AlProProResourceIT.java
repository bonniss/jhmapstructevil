package ai.realworld.web.rest;

import static ai.realworld.domain.AlProProAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlLadyGaga;
import ai.realworld.domain.AlMenity;
import ai.realworld.domain.AlProPro;
import ai.realworld.domain.AlProPro;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.domain.enumeration.BenedictToy;
import ai.realworld.domain.enumeration.PeteType;
import ai.realworld.repository.AlProProRepository;
import ai.realworld.service.AlProProService;
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
 * Integration tests for the {@link AlProProResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AlProProResourceIT {

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

    private static final String ENTITY_API_URL = "/api/al-pro-pros";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlProProRepository alProProRepository;

    @Mock
    private AlProProRepository alProProRepositoryMock;

    @Mock
    private AlProProService alProProServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlProProMockMvc;

    private AlProPro alProPro;

    private AlProPro insertedAlProPro;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlProPro createEntity() {
        return new AlProPro()
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
    public static AlProPro createUpdatedEntity() {
        return new AlProPro()
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
        alProPro = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlProPro != null) {
            alProProRepository.delete(insertedAlProPro);
            insertedAlProPro = null;
        }
    }

    @Test
    @Transactional
    void createAlProPro() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlProPro
        var returnedAlProPro = om.readValue(
            restAlProProMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alProPro)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlProPro.class
        );

        // Validate the AlProPro in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlProProUpdatableFieldsEquals(returnedAlProPro, getPersistedAlProPro(returnedAlProPro));

        insertedAlProPro = returnedAlProPro;
    }

    @Test
    @Transactional
    void createAlProProWithExistingId() throws Exception {
        // Create the AlProPro with an existing ID
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlProProMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alProPro)))
            .andExpect(status().isBadRequest());

        // Validate the AlProPro in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alProPro.setName(null);

        // Create the AlProPro, which fails.

        restAlProProMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alProPro)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlProPros() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList
        restAlProProMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alProPro.getId().toString())))
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
    void getAllAlProProsWithEagerRelationshipsIsEnabled() throws Exception {
        when(alProProServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAlProProMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(alProProServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAlProProsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(alProProServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAlProProMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(alProProRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAlProPro() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get the alProPro
        restAlProProMockMvc
            .perform(get(ENTITY_API_URL_ID, alProPro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alProPro.getId().toString()))
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
    void getAlProProsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        UUID id = alProPro.getId();

        defaultAlProProFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllAlProProsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where name equals to
        defaultAlProProFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlProProsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where name in
        defaultAlProProFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlProProsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where name is not null
        defaultAlProProFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAlProProsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where name contains
        defaultAlProProFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlProProsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where name does not contain
        defaultAlProProFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAlProProsByDescriptionHeitigaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where descriptionHeitiga equals to
        defaultAlProProFiltering(
            "descriptionHeitiga.equals=" + DEFAULT_DESCRIPTION_HEITIGA,
            "descriptionHeitiga.equals=" + UPDATED_DESCRIPTION_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByDescriptionHeitigaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where descriptionHeitiga in
        defaultAlProProFiltering(
            "descriptionHeitiga.in=" + DEFAULT_DESCRIPTION_HEITIGA + "," + UPDATED_DESCRIPTION_HEITIGA,
            "descriptionHeitiga.in=" + UPDATED_DESCRIPTION_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByDescriptionHeitigaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where descriptionHeitiga is not null
        defaultAlProProFiltering("descriptionHeitiga.specified=true", "descriptionHeitiga.specified=false");
    }

    @Test
    @Transactional
    void getAllAlProProsByDescriptionHeitigaContainsSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where descriptionHeitiga contains
        defaultAlProProFiltering(
            "descriptionHeitiga.contains=" + DEFAULT_DESCRIPTION_HEITIGA,
            "descriptionHeitiga.contains=" + UPDATED_DESCRIPTION_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByDescriptionHeitigaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where descriptionHeitiga does not contain
        defaultAlProProFiltering(
            "descriptionHeitiga.doesNotContain=" + UPDATED_DESCRIPTION_HEITIGA,
            "descriptionHeitiga.doesNotContain=" + DEFAULT_DESCRIPTION_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByPropertyTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where propertyType equals to
        defaultAlProProFiltering("propertyType.equals=" + DEFAULT_PROPERTY_TYPE, "propertyType.equals=" + UPDATED_PROPERTY_TYPE);
    }

    @Test
    @Transactional
    void getAllAlProProsByPropertyTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where propertyType in
        defaultAlProProFiltering(
            "propertyType.in=" + DEFAULT_PROPERTY_TYPE + "," + UPDATED_PROPERTY_TYPE,
            "propertyType.in=" + UPDATED_PROPERTY_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByPropertyTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where propertyType is not null
        defaultAlProProFiltering("propertyType.specified=true", "propertyType.specified=false");
    }

    @Test
    @Transactional
    void getAllAlProProsByAreaInSquareMeterIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where areaInSquareMeter equals to
        defaultAlProProFiltering(
            "areaInSquareMeter.equals=" + DEFAULT_AREA_IN_SQUARE_METER,
            "areaInSquareMeter.equals=" + UPDATED_AREA_IN_SQUARE_METER
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByAreaInSquareMeterIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where areaInSquareMeter in
        defaultAlProProFiltering(
            "areaInSquareMeter.in=" + DEFAULT_AREA_IN_SQUARE_METER + "," + UPDATED_AREA_IN_SQUARE_METER,
            "areaInSquareMeter.in=" + UPDATED_AREA_IN_SQUARE_METER
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByAreaInSquareMeterIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where areaInSquareMeter is not null
        defaultAlProProFiltering("areaInSquareMeter.specified=true", "areaInSquareMeter.specified=false");
    }

    @Test
    @Transactional
    void getAllAlProProsByAreaInSquareMeterIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where areaInSquareMeter is greater than or equal to
        defaultAlProProFiltering(
            "areaInSquareMeter.greaterThanOrEqual=" + DEFAULT_AREA_IN_SQUARE_METER,
            "areaInSquareMeter.greaterThanOrEqual=" + UPDATED_AREA_IN_SQUARE_METER
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByAreaInSquareMeterIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where areaInSquareMeter is less than or equal to
        defaultAlProProFiltering(
            "areaInSquareMeter.lessThanOrEqual=" + DEFAULT_AREA_IN_SQUARE_METER,
            "areaInSquareMeter.lessThanOrEqual=" + SMALLER_AREA_IN_SQUARE_METER
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByAreaInSquareMeterIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where areaInSquareMeter is less than
        defaultAlProProFiltering(
            "areaInSquareMeter.lessThan=" + UPDATED_AREA_IN_SQUARE_METER,
            "areaInSquareMeter.lessThan=" + DEFAULT_AREA_IN_SQUARE_METER
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByAreaInSquareMeterIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where areaInSquareMeter is greater than
        defaultAlProProFiltering(
            "areaInSquareMeter.greaterThan=" + SMALLER_AREA_IN_SQUARE_METER,
            "areaInSquareMeter.greaterThan=" + DEFAULT_AREA_IN_SQUARE_METER
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByNumberOfAdultsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where numberOfAdults equals to
        defaultAlProProFiltering("numberOfAdults.equals=" + DEFAULT_NUMBER_OF_ADULTS, "numberOfAdults.equals=" + UPDATED_NUMBER_OF_ADULTS);
    }

    @Test
    @Transactional
    void getAllAlProProsByNumberOfAdultsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where numberOfAdults in
        defaultAlProProFiltering(
            "numberOfAdults.in=" + DEFAULT_NUMBER_OF_ADULTS + "," + UPDATED_NUMBER_OF_ADULTS,
            "numberOfAdults.in=" + UPDATED_NUMBER_OF_ADULTS
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByNumberOfAdultsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where numberOfAdults is not null
        defaultAlProProFiltering("numberOfAdults.specified=true", "numberOfAdults.specified=false");
    }

    @Test
    @Transactional
    void getAllAlProProsByNumberOfAdultsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where numberOfAdults is greater than or equal to
        defaultAlProProFiltering(
            "numberOfAdults.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_ADULTS,
            "numberOfAdults.greaterThanOrEqual=" + UPDATED_NUMBER_OF_ADULTS
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByNumberOfAdultsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where numberOfAdults is less than or equal to
        defaultAlProProFiltering(
            "numberOfAdults.lessThanOrEqual=" + DEFAULT_NUMBER_OF_ADULTS,
            "numberOfAdults.lessThanOrEqual=" + SMALLER_NUMBER_OF_ADULTS
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByNumberOfAdultsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where numberOfAdults is less than
        defaultAlProProFiltering(
            "numberOfAdults.lessThan=" + UPDATED_NUMBER_OF_ADULTS,
            "numberOfAdults.lessThan=" + DEFAULT_NUMBER_OF_ADULTS
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByNumberOfAdultsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where numberOfAdults is greater than
        defaultAlProProFiltering(
            "numberOfAdults.greaterThan=" + SMALLER_NUMBER_OF_ADULTS,
            "numberOfAdults.greaterThan=" + DEFAULT_NUMBER_OF_ADULTS
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByNumberOfPreschoolersIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where numberOfPreschoolers equals to
        defaultAlProProFiltering(
            "numberOfPreschoolers.equals=" + DEFAULT_NUMBER_OF_PRESCHOOLERS,
            "numberOfPreschoolers.equals=" + UPDATED_NUMBER_OF_PRESCHOOLERS
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByNumberOfPreschoolersIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where numberOfPreschoolers in
        defaultAlProProFiltering(
            "numberOfPreschoolers.in=" + DEFAULT_NUMBER_OF_PRESCHOOLERS + "," + UPDATED_NUMBER_OF_PRESCHOOLERS,
            "numberOfPreschoolers.in=" + UPDATED_NUMBER_OF_PRESCHOOLERS
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByNumberOfPreschoolersIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where numberOfPreschoolers is not null
        defaultAlProProFiltering("numberOfPreschoolers.specified=true", "numberOfPreschoolers.specified=false");
    }

    @Test
    @Transactional
    void getAllAlProProsByNumberOfPreschoolersIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where numberOfPreschoolers is greater than or equal to
        defaultAlProProFiltering(
            "numberOfPreschoolers.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_PRESCHOOLERS,
            "numberOfPreschoolers.greaterThanOrEqual=" + UPDATED_NUMBER_OF_PRESCHOOLERS
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByNumberOfPreschoolersIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where numberOfPreschoolers is less than or equal to
        defaultAlProProFiltering(
            "numberOfPreschoolers.lessThanOrEqual=" + DEFAULT_NUMBER_OF_PRESCHOOLERS,
            "numberOfPreschoolers.lessThanOrEqual=" + SMALLER_NUMBER_OF_PRESCHOOLERS
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByNumberOfPreschoolersIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where numberOfPreschoolers is less than
        defaultAlProProFiltering(
            "numberOfPreschoolers.lessThan=" + UPDATED_NUMBER_OF_PRESCHOOLERS,
            "numberOfPreschoolers.lessThan=" + DEFAULT_NUMBER_OF_PRESCHOOLERS
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByNumberOfPreschoolersIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where numberOfPreschoolers is greater than
        defaultAlProProFiltering(
            "numberOfPreschoolers.greaterThan=" + SMALLER_NUMBER_OF_PRESCHOOLERS,
            "numberOfPreschoolers.greaterThan=" + DEFAULT_NUMBER_OF_PRESCHOOLERS
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByNumberOfChildrenIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where numberOfChildren equals to
        defaultAlProProFiltering(
            "numberOfChildren.equals=" + DEFAULT_NUMBER_OF_CHILDREN,
            "numberOfChildren.equals=" + UPDATED_NUMBER_OF_CHILDREN
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByNumberOfChildrenIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where numberOfChildren in
        defaultAlProProFiltering(
            "numberOfChildren.in=" + DEFAULT_NUMBER_OF_CHILDREN + "," + UPDATED_NUMBER_OF_CHILDREN,
            "numberOfChildren.in=" + UPDATED_NUMBER_OF_CHILDREN
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByNumberOfChildrenIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where numberOfChildren is not null
        defaultAlProProFiltering("numberOfChildren.specified=true", "numberOfChildren.specified=false");
    }

    @Test
    @Transactional
    void getAllAlProProsByNumberOfChildrenIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where numberOfChildren is greater than or equal to
        defaultAlProProFiltering(
            "numberOfChildren.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_CHILDREN,
            "numberOfChildren.greaterThanOrEqual=" + UPDATED_NUMBER_OF_CHILDREN
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByNumberOfChildrenIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where numberOfChildren is less than or equal to
        defaultAlProProFiltering(
            "numberOfChildren.lessThanOrEqual=" + DEFAULT_NUMBER_OF_CHILDREN,
            "numberOfChildren.lessThanOrEqual=" + SMALLER_NUMBER_OF_CHILDREN
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByNumberOfChildrenIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where numberOfChildren is less than
        defaultAlProProFiltering(
            "numberOfChildren.lessThan=" + UPDATED_NUMBER_OF_CHILDREN,
            "numberOfChildren.lessThan=" + DEFAULT_NUMBER_OF_CHILDREN
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByNumberOfChildrenIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where numberOfChildren is greater than
        defaultAlProProFiltering(
            "numberOfChildren.greaterThan=" + SMALLER_NUMBER_OF_CHILDREN,
            "numberOfChildren.greaterThan=" + DEFAULT_NUMBER_OF_CHILDREN
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByNumberOfRoomsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where numberOfRooms equals to
        defaultAlProProFiltering("numberOfRooms.equals=" + DEFAULT_NUMBER_OF_ROOMS, "numberOfRooms.equals=" + UPDATED_NUMBER_OF_ROOMS);
    }

    @Test
    @Transactional
    void getAllAlProProsByNumberOfRoomsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where numberOfRooms in
        defaultAlProProFiltering(
            "numberOfRooms.in=" + DEFAULT_NUMBER_OF_ROOMS + "," + UPDATED_NUMBER_OF_ROOMS,
            "numberOfRooms.in=" + UPDATED_NUMBER_OF_ROOMS
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByNumberOfRoomsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where numberOfRooms is not null
        defaultAlProProFiltering("numberOfRooms.specified=true", "numberOfRooms.specified=false");
    }

    @Test
    @Transactional
    void getAllAlProProsByNumberOfRoomsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where numberOfRooms is greater than or equal to
        defaultAlProProFiltering(
            "numberOfRooms.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_ROOMS,
            "numberOfRooms.greaterThanOrEqual=" + UPDATED_NUMBER_OF_ROOMS
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByNumberOfRoomsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where numberOfRooms is less than or equal to
        defaultAlProProFiltering(
            "numberOfRooms.lessThanOrEqual=" + DEFAULT_NUMBER_OF_ROOMS,
            "numberOfRooms.lessThanOrEqual=" + SMALLER_NUMBER_OF_ROOMS
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByNumberOfRoomsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where numberOfRooms is less than
        defaultAlProProFiltering("numberOfRooms.lessThan=" + UPDATED_NUMBER_OF_ROOMS, "numberOfRooms.lessThan=" + DEFAULT_NUMBER_OF_ROOMS);
    }

    @Test
    @Transactional
    void getAllAlProProsByNumberOfRoomsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where numberOfRooms is greater than
        defaultAlProProFiltering(
            "numberOfRooms.greaterThan=" + SMALLER_NUMBER_OF_ROOMS,
            "numberOfRooms.greaterThan=" + DEFAULT_NUMBER_OF_ROOMS
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByNumberOfFloorsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where numberOfFloors equals to
        defaultAlProProFiltering("numberOfFloors.equals=" + DEFAULT_NUMBER_OF_FLOORS, "numberOfFloors.equals=" + UPDATED_NUMBER_OF_FLOORS);
    }

    @Test
    @Transactional
    void getAllAlProProsByNumberOfFloorsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where numberOfFloors in
        defaultAlProProFiltering(
            "numberOfFloors.in=" + DEFAULT_NUMBER_OF_FLOORS + "," + UPDATED_NUMBER_OF_FLOORS,
            "numberOfFloors.in=" + UPDATED_NUMBER_OF_FLOORS
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByNumberOfFloorsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where numberOfFloors is not null
        defaultAlProProFiltering("numberOfFloors.specified=true", "numberOfFloors.specified=false");
    }

    @Test
    @Transactional
    void getAllAlProProsByNumberOfFloorsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where numberOfFloors is greater than or equal to
        defaultAlProProFiltering(
            "numberOfFloors.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_FLOORS,
            "numberOfFloors.greaterThanOrEqual=" + UPDATED_NUMBER_OF_FLOORS
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByNumberOfFloorsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where numberOfFloors is less than or equal to
        defaultAlProProFiltering(
            "numberOfFloors.lessThanOrEqual=" + DEFAULT_NUMBER_OF_FLOORS,
            "numberOfFloors.lessThanOrEqual=" + SMALLER_NUMBER_OF_FLOORS
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByNumberOfFloorsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where numberOfFloors is less than
        defaultAlProProFiltering(
            "numberOfFloors.lessThan=" + UPDATED_NUMBER_OF_FLOORS,
            "numberOfFloors.lessThan=" + DEFAULT_NUMBER_OF_FLOORS
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByNumberOfFloorsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where numberOfFloors is greater than
        defaultAlProProFiltering(
            "numberOfFloors.greaterThan=" + SMALLER_NUMBER_OF_FLOORS,
            "numberOfFloors.greaterThan=" + DEFAULT_NUMBER_OF_FLOORS
        );
    }

    @Test
    @Transactional
    void getAllAlProProsByBedSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where bedSize equals to
        defaultAlProProFiltering("bedSize.equals=" + DEFAULT_BED_SIZE, "bedSize.equals=" + UPDATED_BED_SIZE);
    }

    @Test
    @Transactional
    void getAllAlProProsByBedSizeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where bedSize in
        defaultAlProProFiltering("bedSize.in=" + DEFAULT_BED_SIZE + "," + UPDATED_BED_SIZE, "bedSize.in=" + UPDATED_BED_SIZE);
    }

    @Test
    @Transactional
    void getAllAlProProsByBedSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where bedSize is not null
        defaultAlProProFiltering("bedSize.specified=true", "bedSize.specified=false");
    }

    @Test
    @Transactional
    void getAllAlProProsByIsEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where isEnabled equals to
        defaultAlProProFiltering("isEnabled.equals=" + DEFAULT_IS_ENABLED, "isEnabled.equals=" + UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    void getAllAlProProsByIsEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where isEnabled in
        defaultAlProProFiltering("isEnabled.in=" + DEFAULT_IS_ENABLED + "," + UPDATED_IS_ENABLED, "isEnabled.in=" + UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    void getAllAlProProsByIsEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        // Get all the alProProList where isEnabled is not null
        defaultAlProProFiltering("isEnabled.specified=true", "isEnabled.specified=false");
    }

    @Test
    @Transactional
    void getAllAlProProsByParentIsEqualToSomething() throws Exception {
        AlProPro parent;
        if (TestUtil.findAll(em, AlProPro.class).isEmpty()) {
            alProProRepository.saveAndFlush(alProPro);
            parent = AlProProResourceIT.createEntity();
        } else {
            parent = TestUtil.findAll(em, AlProPro.class).get(0);
        }
        em.persist(parent);
        em.flush();
        alProPro.setParent(parent);
        alProProRepository.saveAndFlush(alProPro);
        UUID parentId = parent.getId();
        // Get all the alProProList where parent equals to parentId
        defaultAlProProShouldBeFound("parentId.equals=" + parentId);

        // Get all the alProProList where parent equals to UUID.randomUUID()
        defaultAlProProShouldNotBeFound("parentId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlProProsByProjectIsEqualToSomething() throws Exception {
        AlLadyGaga project;
        if (TestUtil.findAll(em, AlLadyGaga.class).isEmpty()) {
            alProProRepository.saveAndFlush(alProPro);
            project = AlLadyGagaResourceIT.createEntity();
        } else {
            project = TestUtil.findAll(em, AlLadyGaga.class).get(0);
        }
        em.persist(project);
        em.flush();
        alProPro.setProject(project);
        alProProRepository.saveAndFlush(alProPro);
        UUID projectId = project.getId();
        // Get all the alProProList where project equals to projectId
        defaultAlProProShouldBeFound("projectId.equals=" + projectId);

        // Get all the alProProList where project equals to UUID.randomUUID()
        defaultAlProProShouldNotBeFound("projectId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlProProsByAvatarIsEqualToSomething() throws Exception {
        Metaverse avatar;
        if (TestUtil.findAll(em, Metaverse.class).isEmpty()) {
            alProProRepository.saveAndFlush(alProPro);
            avatar = MetaverseResourceIT.createEntity();
        } else {
            avatar = TestUtil.findAll(em, Metaverse.class).get(0);
        }
        em.persist(avatar);
        em.flush();
        alProPro.setAvatar(avatar);
        alProProRepository.saveAndFlush(alProPro);
        Long avatarId = avatar.getId();
        // Get all the alProProList where avatar equals to avatarId
        defaultAlProProShouldBeFound("avatarId.equals=" + avatarId);

        // Get all the alProProList where avatar equals to (avatarId + 1)
        defaultAlProProShouldNotBeFound("avatarId.equals=" + (avatarId + 1));
    }

    @Test
    @Transactional
    void getAllAlProProsByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alProProRepository.saveAndFlush(alProPro);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alProPro.setApplication(application);
        alProProRepository.saveAndFlush(alProPro);
        UUID applicationId = application.getId();
        // Get all the alProProList where application equals to applicationId
        defaultAlProProShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alProProList where application equals to UUID.randomUUID()
        defaultAlProProShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlProProsByAmenityIsEqualToSomething() throws Exception {
        AlMenity amenity;
        if (TestUtil.findAll(em, AlMenity.class).isEmpty()) {
            alProProRepository.saveAndFlush(alProPro);
            amenity = AlMenityResourceIT.createEntity();
        } else {
            amenity = TestUtil.findAll(em, AlMenity.class).get(0);
        }
        em.persist(amenity);
        em.flush();
        alProPro.addAmenity(amenity);
        alProProRepository.saveAndFlush(alProPro);
        Long amenityId = amenity.getId();
        // Get all the alProProList where amenity equals to amenityId
        defaultAlProProShouldBeFound("amenityId.equals=" + amenityId);

        // Get all the alProProList where amenity equals to (amenityId + 1)
        defaultAlProProShouldNotBeFound("amenityId.equals=" + (amenityId + 1));
    }

    @Test
    @Transactional
    void getAllAlProProsByImageIsEqualToSomething() throws Exception {
        Metaverse image;
        if (TestUtil.findAll(em, Metaverse.class).isEmpty()) {
            alProProRepository.saveAndFlush(alProPro);
            image = MetaverseResourceIT.createEntity();
        } else {
            image = TestUtil.findAll(em, Metaverse.class).get(0);
        }
        em.persist(image);
        em.flush();
        alProPro.addImage(image);
        alProProRepository.saveAndFlush(alProPro);
        Long imageId = image.getId();
        // Get all the alProProList where image equals to imageId
        defaultAlProProShouldBeFound("imageId.equals=" + imageId);

        // Get all the alProProList where image equals to (imageId + 1)
        defaultAlProProShouldNotBeFound("imageId.equals=" + (imageId + 1));
    }

    private void defaultAlProProFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlProProShouldBeFound(shouldBeFound);
        defaultAlProProShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlProProShouldBeFound(String filter) throws Exception {
        restAlProProMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alProPro.getId().toString())))
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
        restAlProProMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlProProShouldNotBeFound(String filter) throws Exception {
        restAlProProMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlProProMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlProPro() throws Exception {
        // Get the alProPro
        restAlProProMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlProPro() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alProPro
        AlProPro updatedAlProPro = alProProRepository.findById(alProPro.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlProPro are not directly saved in db
        em.detach(updatedAlProPro);
        updatedAlProPro
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

        restAlProProMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlProPro.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlProPro))
            )
            .andExpect(status().isOk());

        // Validate the AlProPro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlProProToMatchAllProperties(updatedAlProPro);
    }

    @Test
    @Transactional
    void putNonExistingAlProPro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alProPro.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlProProMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alProPro.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alProPro))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlProPro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlProPro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alProPro.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlProProMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alProPro))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlProPro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlProPro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alProPro.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlProProMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alProPro)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlProPro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlProProWithPatch() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alProPro using partial update
        AlProPro partialUpdatedAlProPro = new AlProPro();
        partialUpdatedAlProPro.setId(alProPro.getId());

        partialUpdatedAlProPro
            .name(UPDATED_NAME)
            .descriptionHeitiga(UPDATED_DESCRIPTION_HEITIGA)
            .numberOfAdults(UPDATED_NUMBER_OF_ADULTS)
            .numberOfChildren(UPDATED_NUMBER_OF_CHILDREN)
            .numberOfRooms(UPDATED_NUMBER_OF_ROOMS)
            .numberOfFloors(UPDATED_NUMBER_OF_FLOORS)
            .isEnabled(UPDATED_IS_ENABLED);

        restAlProProMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlProPro.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlProPro))
            )
            .andExpect(status().isOk());

        // Validate the AlProPro in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlProProUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAlProPro, alProPro), getPersistedAlProPro(alProPro));
    }

    @Test
    @Transactional
    void fullUpdateAlProProWithPatch() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alProPro using partial update
        AlProPro partialUpdatedAlProPro = new AlProPro();
        partialUpdatedAlProPro.setId(alProPro.getId());

        partialUpdatedAlProPro
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

        restAlProProMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlProPro.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlProPro))
            )
            .andExpect(status().isOk());

        // Validate the AlProPro in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlProProUpdatableFieldsEquals(partialUpdatedAlProPro, getPersistedAlProPro(partialUpdatedAlProPro));
    }

    @Test
    @Transactional
    void patchNonExistingAlProPro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alProPro.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlProProMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alProPro.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alProPro))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlProPro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlProPro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alProPro.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlProProMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alProPro))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlProPro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlProPro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alProPro.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlProProMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alProPro)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlProPro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlProPro() throws Exception {
        // Initialize the database
        insertedAlProPro = alProProRepository.saveAndFlush(alProPro);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alProPro
        restAlProProMockMvc
            .perform(delete(ENTITY_API_URL_ID, alProPro.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alProProRepository.count();
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

    protected AlProPro getPersistedAlProPro(AlProPro alProPro) {
        return alProProRepository.findById(alProPro.getId()).orElseThrow();
    }

    protected void assertPersistedAlProProToMatchAllProperties(AlProPro expectedAlProPro) {
        assertAlProProAllPropertiesEquals(expectedAlProPro, getPersistedAlProPro(expectedAlProPro));
    }

    protected void assertPersistedAlProProToMatchUpdatableProperties(AlProPro expectedAlProPro) {
        assertAlProProAllUpdatablePropertiesEquals(expectedAlProPro, getPersistedAlProPro(expectedAlProPro));
    }
}
