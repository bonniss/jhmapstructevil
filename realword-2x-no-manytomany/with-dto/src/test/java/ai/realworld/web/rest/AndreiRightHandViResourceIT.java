package ai.realworld.web.rest;

import static ai.realworld.domain.AndreiRightHandViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AndreiRightHandVi;
import ai.realworld.domain.AntonioBanderasVi;
import ai.realworld.repository.AndreiRightHandViRepository;
import ai.realworld.service.dto.AndreiRightHandViDTO;
import ai.realworld.service.mapper.AndreiRightHandViMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
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
 * Integration tests for the {@link AndreiRightHandViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AndreiRightHandViResourceIT {

    private static final String DEFAULT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_DETAILS = "BBBBBBBBBB";

    private static final Float DEFAULT_LAT = 1F;
    private static final Float UPDATED_LAT = 2F;
    private static final Float SMALLER_LAT = 1F - 1F;

    private static final Float DEFAULT_LON = 1F;
    private static final Float UPDATED_LON = 2F;
    private static final Float SMALLER_LON = 1F - 1F;

    private static final String ENTITY_API_URL = "/api/andrei-right-hand-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AndreiRightHandViRepository andreiRightHandViRepository;

    @Autowired
    private AndreiRightHandViMapper andreiRightHandViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAndreiRightHandViMockMvc;

    private AndreiRightHandVi andreiRightHandVi;

    private AndreiRightHandVi insertedAndreiRightHandVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AndreiRightHandVi createEntity() {
        return new AndreiRightHandVi().details(DEFAULT_DETAILS).lat(DEFAULT_LAT).lon(DEFAULT_LON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AndreiRightHandVi createUpdatedEntity() {
        return new AndreiRightHandVi().details(UPDATED_DETAILS).lat(UPDATED_LAT).lon(UPDATED_LON);
    }

    @BeforeEach
    public void initTest() {
        andreiRightHandVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAndreiRightHandVi != null) {
            andreiRightHandViRepository.delete(insertedAndreiRightHandVi);
            insertedAndreiRightHandVi = null;
        }
    }

    @Test
    @Transactional
    void createAndreiRightHandVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AndreiRightHandVi
        AndreiRightHandViDTO andreiRightHandViDTO = andreiRightHandViMapper.toDto(andreiRightHandVi);
        var returnedAndreiRightHandViDTO = om.readValue(
            restAndreiRightHandViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(andreiRightHandViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AndreiRightHandViDTO.class
        );

        // Validate the AndreiRightHandVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAndreiRightHandVi = andreiRightHandViMapper.toEntity(returnedAndreiRightHandViDTO);
        assertAndreiRightHandViUpdatableFieldsEquals(returnedAndreiRightHandVi, getPersistedAndreiRightHandVi(returnedAndreiRightHandVi));

        insertedAndreiRightHandVi = returnedAndreiRightHandVi;
    }

    @Test
    @Transactional
    void createAndreiRightHandViWithExistingId() throws Exception {
        // Create the AndreiRightHandVi with an existing ID
        andreiRightHandVi.setId(1L);
        AndreiRightHandViDTO andreiRightHandViDTO = andreiRightHandViMapper.toDto(andreiRightHandVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAndreiRightHandViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(andreiRightHandViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AndreiRightHandVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAndreiRightHandVis() throws Exception {
        // Initialize the database
        insertedAndreiRightHandVi = andreiRightHandViRepository.saveAndFlush(andreiRightHandVi);

        // Get all the andreiRightHandViList
        restAndreiRightHandViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(andreiRightHandVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS)))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.doubleValue())))
            .andExpect(jsonPath("$.[*].lon").value(hasItem(DEFAULT_LON.doubleValue())));
    }

    @Test
    @Transactional
    void getAndreiRightHandVi() throws Exception {
        // Initialize the database
        insertedAndreiRightHandVi = andreiRightHandViRepository.saveAndFlush(andreiRightHandVi);

        // Get the andreiRightHandVi
        restAndreiRightHandViMockMvc
            .perform(get(ENTITY_API_URL_ID, andreiRightHandVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(andreiRightHandVi.getId().intValue()))
            .andExpect(jsonPath("$.details").value(DEFAULT_DETAILS))
            .andExpect(jsonPath("$.lat").value(DEFAULT_LAT.doubleValue()))
            .andExpect(jsonPath("$.lon").value(DEFAULT_LON.doubleValue()));
    }

    @Test
    @Transactional
    void getAndreiRightHandVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedAndreiRightHandVi = andreiRightHandViRepository.saveAndFlush(andreiRightHandVi);

        Long id = andreiRightHandVi.getId();

        defaultAndreiRightHandViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAndreiRightHandViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAndreiRightHandViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAndreiRightHandVisByDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAndreiRightHandVi = andreiRightHandViRepository.saveAndFlush(andreiRightHandVi);

        // Get all the andreiRightHandViList where details equals to
        defaultAndreiRightHandViFiltering("details.equals=" + DEFAULT_DETAILS, "details.equals=" + UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void getAllAndreiRightHandVisByDetailsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAndreiRightHandVi = andreiRightHandViRepository.saveAndFlush(andreiRightHandVi);

        // Get all the andreiRightHandViList where details in
        defaultAndreiRightHandViFiltering("details.in=" + DEFAULT_DETAILS + "," + UPDATED_DETAILS, "details.in=" + UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void getAllAndreiRightHandVisByDetailsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAndreiRightHandVi = andreiRightHandViRepository.saveAndFlush(andreiRightHandVi);

        // Get all the andreiRightHandViList where details is not null
        defaultAndreiRightHandViFiltering("details.specified=true", "details.specified=false");
    }

    @Test
    @Transactional
    void getAllAndreiRightHandVisByDetailsContainsSomething() throws Exception {
        // Initialize the database
        insertedAndreiRightHandVi = andreiRightHandViRepository.saveAndFlush(andreiRightHandVi);

        // Get all the andreiRightHandViList where details contains
        defaultAndreiRightHandViFiltering("details.contains=" + DEFAULT_DETAILS, "details.contains=" + UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void getAllAndreiRightHandVisByDetailsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAndreiRightHandVi = andreiRightHandViRepository.saveAndFlush(andreiRightHandVi);

        // Get all the andreiRightHandViList where details does not contain
        defaultAndreiRightHandViFiltering("details.doesNotContain=" + UPDATED_DETAILS, "details.doesNotContain=" + DEFAULT_DETAILS);
    }

    @Test
    @Transactional
    void getAllAndreiRightHandVisByLatIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAndreiRightHandVi = andreiRightHandViRepository.saveAndFlush(andreiRightHandVi);

        // Get all the andreiRightHandViList where lat equals to
        defaultAndreiRightHandViFiltering("lat.equals=" + DEFAULT_LAT, "lat.equals=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllAndreiRightHandVisByLatIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAndreiRightHandVi = andreiRightHandViRepository.saveAndFlush(andreiRightHandVi);

        // Get all the andreiRightHandViList where lat in
        defaultAndreiRightHandViFiltering("lat.in=" + DEFAULT_LAT + "," + UPDATED_LAT, "lat.in=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllAndreiRightHandVisByLatIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAndreiRightHandVi = andreiRightHandViRepository.saveAndFlush(andreiRightHandVi);

        // Get all the andreiRightHandViList where lat is not null
        defaultAndreiRightHandViFiltering("lat.specified=true", "lat.specified=false");
    }

    @Test
    @Transactional
    void getAllAndreiRightHandVisByLatIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAndreiRightHandVi = andreiRightHandViRepository.saveAndFlush(andreiRightHandVi);

        // Get all the andreiRightHandViList where lat is greater than or equal to
        defaultAndreiRightHandViFiltering("lat.greaterThanOrEqual=" + DEFAULT_LAT, "lat.greaterThanOrEqual=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllAndreiRightHandVisByLatIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAndreiRightHandVi = andreiRightHandViRepository.saveAndFlush(andreiRightHandVi);

        // Get all the andreiRightHandViList where lat is less than or equal to
        defaultAndreiRightHandViFiltering("lat.lessThanOrEqual=" + DEFAULT_LAT, "lat.lessThanOrEqual=" + SMALLER_LAT);
    }

    @Test
    @Transactional
    void getAllAndreiRightHandVisByLatIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAndreiRightHandVi = andreiRightHandViRepository.saveAndFlush(andreiRightHandVi);

        // Get all the andreiRightHandViList where lat is less than
        defaultAndreiRightHandViFiltering("lat.lessThan=" + UPDATED_LAT, "lat.lessThan=" + DEFAULT_LAT);
    }

    @Test
    @Transactional
    void getAllAndreiRightHandVisByLatIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAndreiRightHandVi = andreiRightHandViRepository.saveAndFlush(andreiRightHandVi);

        // Get all the andreiRightHandViList where lat is greater than
        defaultAndreiRightHandViFiltering("lat.greaterThan=" + SMALLER_LAT, "lat.greaterThan=" + DEFAULT_LAT);
    }

    @Test
    @Transactional
    void getAllAndreiRightHandVisByLonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAndreiRightHandVi = andreiRightHandViRepository.saveAndFlush(andreiRightHandVi);

        // Get all the andreiRightHandViList where lon equals to
        defaultAndreiRightHandViFiltering("lon.equals=" + DEFAULT_LON, "lon.equals=" + UPDATED_LON);
    }

    @Test
    @Transactional
    void getAllAndreiRightHandVisByLonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAndreiRightHandVi = andreiRightHandViRepository.saveAndFlush(andreiRightHandVi);

        // Get all the andreiRightHandViList where lon in
        defaultAndreiRightHandViFiltering("lon.in=" + DEFAULT_LON + "," + UPDATED_LON, "lon.in=" + UPDATED_LON);
    }

    @Test
    @Transactional
    void getAllAndreiRightHandVisByLonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAndreiRightHandVi = andreiRightHandViRepository.saveAndFlush(andreiRightHandVi);

        // Get all the andreiRightHandViList where lon is not null
        defaultAndreiRightHandViFiltering("lon.specified=true", "lon.specified=false");
    }

    @Test
    @Transactional
    void getAllAndreiRightHandVisByLonIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAndreiRightHandVi = andreiRightHandViRepository.saveAndFlush(andreiRightHandVi);

        // Get all the andreiRightHandViList where lon is greater than or equal to
        defaultAndreiRightHandViFiltering("lon.greaterThanOrEqual=" + DEFAULT_LON, "lon.greaterThanOrEqual=" + UPDATED_LON);
    }

    @Test
    @Transactional
    void getAllAndreiRightHandVisByLonIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAndreiRightHandVi = andreiRightHandViRepository.saveAndFlush(andreiRightHandVi);

        // Get all the andreiRightHandViList where lon is less than or equal to
        defaultAndreiRightHandViFiltering("lon.lessThanOrEqual=" + DEFAULT_LON, "lon.lessThanOrEqual=" + SMALLER_LON);
    }

    @Test
    @Transactional
    void getAllAndreiRightHandVisByLonIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAndreiRightHandVi = andreiRightHandViRepository.saveAndFlush(andreiRightHandVi);

        // Get all the andreiRightHandViList where lon is less than
        defaultAndreiRightHandViFiltering("lon.lessThan=" + UPDATED_LON, "lon.lessThan=" + DEFAULT_LON);
    }

    @Test
    @Transactional
    void getAllAndreiRightHandVisByLonIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAndreiRightHandVi = andreiRightHandViRepository.saveAndFlush(andreiRightHandVi);

        // Get all the andreiRightHandViList where lon is greater than
        defaultAndreiRightHandViFiltering("lon.greaterThan=" + SMALLER_LON, "lon.greaterThan=" + DEFAULT_LON);
    }

    @Test
    @Transactional
    void getAllAndreiRightHandVisByCountryIsEqualToSomething() throws Exception {
        AntonioBanderasVi country;
        if (TestUtil.findAll(em, AntonioBanderasVi.class).isEmpty()) {
            andreiRightHandViRepository.saveAndFlush(andreiRightHandVi);
            country = AntonioBanderasViResourceIT.createEntity();
        } else {
            country = TestUtil.findAll(em, AntonioBanderasVi.class).get(0);
        }
        em.persist(country);
        em.flush();
        andreiRightHandVi.setCountry(country);
        andreiRightHandViRepository.saveAndFlush(andreiRightHandVi);
        Long countryId = country.getId();
        // Get all the andreiRightHandViList where country equals to countryId
        defaultAndreiRightHandViShouldBeFound("countryId.equals=" + countryId);

        // Get all the andreiRightHandViList where country equals to (countryId + 1)
        defaultAndreiRightHandViShouldNotBeFound("countryId.equals=" + (countryId + 1));
    }

    @Test
    @Transactional
    void getAllAndreiRightHandVisByProvinceIsEqualToSomething() throws Exception {
        AntonioBanderasVi province;
        if (TestUtil.findAll(em, AntonioBanderasVi.class).isEmpty()) {
            andreiRightHandViRepository.saveAndFlush(andreiRightHandVi);
            province = AntonioBanderasViResourceIT.createEntity();
        } else {
            province = TestUtil.findAll(em, AntonioBanderasVi.class).get(0);
        }
        em.persist(province);
        em.flush();
        andreiRightHandVi.setProvince(province);
        andreiRightHandViRepository.saveAndFlush(andreiRightHandVi);
        Long provinceId = province.getId();
        // Get all the andreiRightHandViList where province equals to provinceId
        defaultAndreiRightHandViShouldBeFound("provinceId.equals=" + provinceId);

        // Get all the andreiRightHandViList where province equals to (provinceId + 1)
        defaultAndreiRightHandViShouldNotBeFound("provinceId.equals=" + (provinceId + 1));
    }

    @Test
    @Transactional
    void getAllAndreiRightHandVisByDistrictIsEqualToSomething() throws Exception {
        AntonioBanderasVi district;
        if (TestUtil.findAll(em, AntonioBanderasVi.class).isEmpty()) {
            andreiRightHandViRepository.saveAndFlush(andreiRightHandVi);
            district = AntonioBanderasViResourceIT.createEntity();
        } else {
            district = TestUtil.findAll(em, AntonioBanderasVi.class).get(0);
        }
        em.persist(district);
        em.flush();
        andreiRightHandVi.setDistrict(district);
        andreiRightHandViRepository.saveAndFlush(andreiRightHandVi);
        Long districtId = district.getId();
        // Get all the andreiRightHandViList where district equals to districtId
        defaultAndreiRightHandViShouldBeFound("districtId.equals=" + districtId);

        // Get all the andreiRightHandViList where district equals to (districtId + 1)
        defaultAndreiRightHandViShouldNotBeFound("districtId.equals=" + (districtId + 1));
    }

    @Test
    @Transactional
    void getAllAndreiRightHandVisByWardIsEqualToSomething() throws Exception {
        AntonioBanderasVi ward;
        if (TestUtil.findAll(em, AntonioBanderasVi.class).isEmpty()) {
            andreiRightHandViRepository.saveAndFlush(andreiRightHandVi);
            ward = AntonioBanderasViResourceIT.createEntity();
        } else {
            ward = TestUtil.findAll(em, AntonioBanderasVi.class).get(0);
        }
        em.persist(ward);
        em.flush();
        andreiRightHandVi.setWard(ward);
        andreiRightHandViRepository.saveAndFlush(andreiRightHandVi);
        Long wardId = ward.getId();
        // Get all the andreiRightHandViList where ward equals to wardId
        defaultAndreiRightHandViShouldBeFound("wardId.equals=" + wardId);

        // Get all the andreiRightHandViList where ward equals to (wardId + 1)
        defaultAndreiRightHandViShouldNotBeFound("wardId.equals=" + (wardId + 1));
    }

    private void defaultAndreiRightHandViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAndreiRightHandViShouldBeFound(shouldBeFound);
        defaultAndreiRightHandViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAndreiRightHandViShouldBeFound(String filter) throws Exception {
        restAndreiRightHandViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(andreiRightHandVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS)))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.doubleValue())))
            .andExpect(jsonPath("$.[*].lon").value(hasItem(DEFAULT_LON.doubleValue())));

        // Check, that the count call also returns 1
        restAndreiRightHandViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAndreiRightHandViShouldNotBeFound(String filter) throws Exception {
        restAndreiRightHandViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAndreiRightHandViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAndreiRightHandVi() throws Exception {
        // Get the andreiRightHandVi
        restAndreiRightHandViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAndreiRightHandVi() throws Exception {
        // Initialize the database
        insertedAndreiRightHandVi = andreiRightHandViRepository.saveAndFlush(andreiRightHandVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the andreiRightHandVi
        AndreiRightHandVi updatedAndreiRightHandVi = andreiRightHandViRepository.findById(andreiRightHandVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAndreiRightHandVi are not directly saved in db
        em.detach(updatedAndreiRightHandVi);
        updatedAndreiRightHandVi.details(UPDATED_DETAILS).lat(UPDATED_LAT).lon(UPDATED_LON);
        AndreiRightHandViDTO andreiRightHandViDTO = andreiRightHandViMapper.toDto(updatedAndreiRightHandVi);

        restAndreiRightHandViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, andreiRightHandViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(andreiRightHandViDTO))
            )
            .andExpect(status().isOk());

        // Validate the AndreiRightHandVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAndreiRightHandViToMatchAllProperties(updatedAndreiRightHandVi);
    }

    @Test
    @Transactional
    void putNonExistingAndreiRightHandVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        andreiRightHandVi.setId(longCount.incrementAndGet());

        // Create the AndreiRightHandVi
        AndreiRightHandViDTO andreiRightHandViDTO = andreiRightHandViMapper.toDto(andreiRightHandVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAndreiRightHandViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, andreiRightHandViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(andreiRightHandViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AndreiRightHandVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAndreiRightHandVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        andreiRightHandVi.setId(longCount.incrementAndGet());

        // Create the AndreiRightHandVi
        AndreiRightHandViDTO andreiRightHandViDTO = andreiRightHandViMapper.toDto(andreiRightHandVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAndreiRightHandViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(andreiRightHandViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AndreiRightHandVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAndreiRightHandVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        andreiRightHandVi.setId(longCount.incrementAndGet());

        // Create the AndreiRightHandVi
        AndreiRightHandViDTO andreiRightHandViDTO = andreiRightHandViMapper.toDto(andreiRightHandVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAndreiRightHandViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(andreiRightHandViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AndreiRightHandVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAndreiRightHandViWithPatch() throws Exception {
        // Initialize the database
        insertedAndreiRightHandVi = andreiRightHandViRepository.saveAndFlush(andreiRightHandVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the andreiRightHandVi using partial update
        AndreiRightHandVi partialUpdatedAndreiRightHandVi = new AndreiRightHandVi();
        partialUpdatedAndreiRightHandVi.setId(andreiRightHandVi.getId());

        partialUpdatedAndreiRightHandVi.details(UPDATED_DETAILS).lon(UPDATED_LON);

        restAndreiRightHandViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAndreiRightHandVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAndreiRightHandVi))
            )
            .andExpect(status().isOk());

        // Validate the AndreiRightHandVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAndreiRightHandViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAndreiRightHandVi, andreiRightHandVi),
            getPersistedAndreiRightHandVi(andreiRightHandVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateAndreiRightHandViWithPatch() throws Exception {
        // Initialize the database
        insertedAndreiRightHandVi = andreiRightHandViRepository.saveAndFlush(andreiRightHandVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the andreiRightHandVi using partial update
        AndreiRightHandVi partialUpdatedAndreiRightHandVi = new AndreiRightHandVi();
        partialUpdatedAndreiRightHandVi.setId(andreiRightHandVi.getId());

        partialUpdatedAndreiRightHandVi.details(UPDATED_DETAILS).lat(UPDATED_LAT).lon(UPDATED_LON);

        restAndreiRightHandViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAndreiRightHandVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAndreiRightHandVi))
            )
            .andExpect(status().isOk());

        // Validate the AndreiRightHandVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAndreiRightHandViUpdatableFieldsEquals(
            partialUpdatedAndreiRightHandVi,
            getPersistedAndreiRightHandVi(partialUpdatedAndreiRightHandVi)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAndreiRightHandVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        andreiRightHandVi.setId(longCount.incrementAndGet());

        // Create the AndreiRightHandVi
        AndreiRightHandViDTO andreiRightHandViDTO = andreiRightHandViMapper.toDto(andreiRightHandVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAndreiRightHandViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, andreiRightHandViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(andreiRightHandViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AndreiRightHandVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAndreiRightHandVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        andreiRightHandVi.setId(longCount.incrementAndGet());

        // Create the AndreiRightHandVi
        AndreiRightHandViDTO andreiRightHandViDTO = andreiRightHandViMapper.toDto(andreiRightHandVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAndreiRightHandViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(andreiRightHandViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AndreiRightHandVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAndreiRightHandVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        andreiRightHandVi.setId(longCount.incrementAndGet());

        // Create the AndreiRightHandVi
        AndreiRightHandViDTO andreiRightHandViDTO = andreiRightHandViMapper.toDto(andreiRightHandVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAndreiRightHandViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(andreiRightHandViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AndreiRightHandVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAndreiRightHandVi() throws Exception {
        // Initialize the database
        insertedAndreiRightHandVi = andreiRightHandViRepository.saveAndFlush(andreiRightHandVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the andreiRightHandVi
        restAndreiRightHandViMockMvc
            .perform(delete(ENTITY_API_URL_ID, andreiRightHandVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return andreiRightHandViRepository.count();
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

    protected AndreiRightHandVi getPersistedAndreiRightHandVi(AndreiRightHandVi andreiRightHandVi) {
        return andreiRightHandViRepository.findById(andreiRightHandVi.getId()).orElseThrow();
    }

    protected void assertPersistedAndreiRightHandViToMatchAllProperties(AndreiRightHandVi expectedAndreiRightHandVi) {
        assertAndreiRightHandViAllPropertiesEquals(expectedAndreiRightHandVi, getPersistedAndreiRightHandVi(expectedAndreiRightHandVi));
    }

    protected void assertPersistedAndreiRightHandViToMatchUpdatableProperties(AndreiRightHandVi expectedAndreiRightHandVi) {
        assertAndreiRightHandViAllUpdatablePropertiesEquals(
            expectedAndreiRightHandVi,
            getPersistedAndreiRightHandVi(expectedAndreiRightHandVi)
        );
    }
}
