package ai.realworld.web.rest;

import static ai.realworld.domain.AndreiRightHandAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AndreiRightHand;
import ai.realworld.domain.AntonioBanderas;
import ai.realworld.repository.AndreiRightHandRepository;
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
 * Integration tests for the {@link AndreiRightHandResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AndreiRightHandResourceIT {

    private static final String DEFAULT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_DETAILS = "BBBBBBBBBB";

    private static final Float DEFAULT_LAT = 1F;
    private static final Float UPDATED_LAT = 2F;
    private static final Float SMALLER_LAT = 1F - 1F;

    private static final Float DEFAULT_LON = 1F;
    private static final Float UPDATED_LON = 2F;
    private static final Float SMALLER_LON = 1F - 1F;

    private static final String ENTITY_API_URL = "/api/andrei-right-hands";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AndreiRightHandRepository andreiRightHandRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAndreiRightHandMockMvc;

    private AndreiRightHand andreiRightHand;

    private AndreiRightHand insertedAndreiRightHand;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AndreiRightHand createEntity() {
        return new AndreiRightHand().details(DEFAULT_DETAILS).lat(DEFAULT_LAT).lon(DEFAULT_LON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AndreiRightHand createUpdatedEntity() {
        return new AndreiRightHand().details(UPDATED_DETAILS).lat(UPDATED_LAT).lon(UPDATED_LON);
    }

    @BeforeEach
    public void initTest() {
        andreiRightHand = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAndreiRightHand != null) {
            andreiRightHandRepository.delete(insertedAndreiRightHand);
            insertedAndreiRightHand = null;
        }
    }

    @Test
    @Transactional
    void createAndreiRightHand() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AndreiRightHand
        var returnedAndreiRightHand = om.readValue(
            restAndreiRightHandMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(andreiRightHand)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AndreiRightHand.class
        );

        // Validate the AndreiRightHand in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAndreiRightHandUpdatableFieldsEquals(returnedAndreiRightHand, getPersistedAndreiRightHand(returnedAndreiRightHand));

        insertedAndreiRightHand = returnedAndreiRightHand;
    }

    @Test
    @Transactional
    void createAndreiRightHandWithExistingId() throws Exception {
        // Create the AndreiRightHand with an existing ID
        andreiRightHand.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAndreiRightHandMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(andreiRightHand)))
            .andExpect(status().isBadRequest());

        // Validate the AndreiRightHand in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAndreiRightHands() throws Exception {
        // Initialize the database
        insertedAndreiRightHand = andreiRightHandRepository.saveAndFlush(andreiRightHand);

        // Get all the andreiRightHandList
        restAndreiRightHandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(andreiRightHand.getId().intValue())))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS)))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.doubleValue())))
            .andExpect(jsonPath("$.[*].lon").value(hasItem(DEFAULT_LON.doubleValue())));
    }

    @Test
    @Transactional
    void getAndreiRightHand() throws Exception {
        // Initialize the database
        insertedAndreiRightHand = andreiRightHandRepository.saveAndFlush(andreiRightHand);

        // Get the andreiRightHand
        restAndreiRightHandMockMvc
            .perform(get(ENTITY_API_URL_ID, andreiRightHand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(andreiRightHand.getId().intValue()))
            .andExpect(jsonPath("$.details").value(DEFAULT_DETAILS))
            .andExpect(jsonPath("$.lat").value(DEFAULT_LAT.doubleValue()))
            .andExpect(jsonPath("$.lon").value(DEFAULT_LON.doubleValue()));
    }

    @Test
    @Transactional
    void getAndreiRightHandsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAndreiRightHand = andreiRightHandRepository.saveAndFlush(andreiRightHand);

        Long id = andreiRightHand.getId();

        defaultAndreiRightHandFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAndreiRightHandFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAndreiRightHandFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAndreiRightHandsByDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAndreiRightHand = andreiRightHandRepository.saveAndFlush(andreiRightHand);

        // Get all the andreiRightHandList where details equals to
        defaultAndreiRightHandFiltering("details.equals=" + DEFAULT_DETAILS, "details.equals=" + UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void getAllAndreiRightHandsByDetailsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAndreiRightHand = andreiRightHandRepository.saveAndFlush(andreiRightHand);

        // Get all the andreiRightHandList where details in
        defaultAndreiRightHandFiltering("details.in=" + DEFAULT_DETAILS + "," + UPDATED_DETAILS, "details.in=" + UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void getAllAndreiRightHandsByDetailsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAndreiRightHand = andreiRightHandRepository.saveAndFlush(andreiRightHand);

        // Get all the andreiRightHandList where details is not null
        defaultAndreiRightHandFiltering("details.specified=true", "details.specified=false");
    }

    @Test
    @Transactional
    void getAllAndreiRightHandsByDetailsContainsSomething() throws Exception {
        // Initialize the database
        insertedAndreiRightHand = andreiRightHandRepository.saveAndFlush(andreiRightHand);

        // Get all the andreiRightHandList where details contains
        defaultAndreiRightHandFiltering("details.contains=" + DEFAULT_DETAILS, "details.contains=" + UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void getAllAndreiRightHandsByDetailsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAndreiRightHand = andreiRightHandRepository.saveAndFlush(andreiRightHand);

        // Get all the andreiRightHandList where details does not contain
        defaultAndreiRightHandFiltering("details.doesNotContain=" + UPDATED_DETAILS, "details.doesNotContain=" + DEFAULT_DETAILS);
    }

    @Test
    @Transactional
    void getAllAndreiRightHandsByLatIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAndreiRightHand = andreiRightHandRepository.saveAndFlush(andreiRightHand);

        // Get all the andreiRightHandList where lat equals to
        defaultAndreiRightHandFiltering("lat.equals=" + DEFAULT_LAT, "lat.equals=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllAndreiRightHandsByLatIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAndreiRightHand = andreiRightHandRepository.saveAndFlush(andreiRightHand);

        // Get all the andreiRightHandList where lat in
        defaultAndreiRightHandFiltering("lat.in=" + DEFAULT_LAT + "," + UPDATED_LAT, "lat.in=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllAndreiRightHandsByLatIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAndreiRightHand = andreiRightHandRepository.saveAndFlush(andreiRightHand);

        // Get all the andreiRightHandList where lat is not null
        defaultAndreiRightHandFiltering("lat.specified=true", "lat.specified=false");
    }

    @Test
    @Transactional
    void getAllAndreiRightHandsByLatIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAndreiRightHand = andreiRightHandRepository.saveAndFlush(andreiRightHand);

        // Get all the andreiRightHandList where lat is greater than or equal to
        defaultAndreiRightHandFiltering("lat.greaterThanOrEqual=" + DEFAULT_LAT, "lat.greaterThanOrEqual=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllAndreiRightHandsByLatIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAndreiRightHand = andreiRightHandRepository.saveAndFlush(andreiRightHand);

        // Get all the andreiRightHandList where lat is less than or equal to
        defaultAndreiRightHandFiltering("lat.lessThanOrEqual=" + DEFAULT_LAT, "lat.lessThanOrEqual=" + SMALLER_LAT);
    }

    @Test
    @Transactional
    void getAllAndreiRightHandsByLatIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAndreiRightHand = andreiRightHandRepository.saveAndFlush(andreiRightHand);

        // Get all the andreiRightHandList where lat is less than
        defaultAndreiRightHandFiltering("lat.lessThan=" + UPDATED_LAT, "lat.lessThan=" + DEFAULT_LAT);
    }

    @Test
    @Transactional
    void getAllAndreiRightHandsByLatIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAndreiRightHand = andreiRightHandRepository.saveAndFlush(andreiRightHand);

        // Get all the andreiRightHandList where lat is greater than
        defaultAndreiRightHandFiltering("lat.greaterThan=" + SMALLER_LAT, "lat.greaterThan=" + DEFAULT_LAT);
    }

    @Test
    @Transactional
    void getAllAndreiRightHandsByLonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAndreiRightHand = andreiRightHandRepository.saveAndFlush(andreiRightHand);

        // Get all the andreiRightHandList where lon equals to
        defaultAndreiRightHandFiltering("lon.equals=" + DEFAULT_LON, "lon.equals=" + UPDATED_LON);
    }

    @Test
    @Transactional
    void getAllAndreiRightHandsByLonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAndreiRightHand = andreiRightHandRepository.saveAndFlush(andreiRightHand);

        // Get all the andreiRightHandList where lon in
        defaultAndreiRightHandFiltering("lon.in=" + DEFAULT_LON + "," + UPDATED_LON, "lon.in=" + UPDATED_LON);
    }

    @Test
    @Transactional
    void getAllAndreiRightHandsByLonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAndreiRightHand = andreiRightHandRepository.saveAndFlush(andreiRightHand);

        // Get all the andreiRightHandList where lon is not null
        defaultAndreiRightHandFiltering("lon.specified=true", "lon.specified=false");
    }

    @Test
    @Transactional
    void getAllAndreiRightHandsByLonIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAndreiRightHand = andreiRightHandRepository.saveAndFlush(andreiRightHand);

        // Get all the andreiRightHandList where lon is greater than or equal to
        defaultAndreiRightHandFiltering("lon.greaterThanOrEqual=" + DEFAULT_LON, "lon.greaterThanOrEqual=" + UPDATED_LON);
    }

    @Test
    @Transactional
    void getAllAndreiRightHandsByLonIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAndreiRightHand = andreiRightHandRepository.saveAndFlush(andreiRightHand);

        // Get all the andreiRightHandList where lon is less than or equal to
        defaultAndreiRightHandFiltering("lon.lessThanOrEqual=" + DEFAULT_LON, "lon.lessThanOrEqual=" + SMALLER_LON);
    }

    @Test
    @Transactional
    void getAllAndreiRightHandsByLonIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAndreiRightHand = andreiRightHandRepository.saveAndFlush(andreiRightHand);

        // Get all the andreiRightHandList where lon is less than
        defaultAndreiRightHandFiltering("lon.lessThan=" + UPDATED_LON, "lon.lessThan=" + DEFAULT_LON);
    }

    @Test
    @Transactional
    void getAllAndreiRightHandsByLonIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAndreiRightHand = andreiRightHandRepository.saveAndFlush(andreiRightHand);

        // Get all the andreiRightHandList where lon is greater than
        defaultAndreiRightHandFiltering("lon.greaterThan=" + SMALLER_LON, "lon.greaterThan=" + DEFAULT_LON);
    }

    @Test
    @Transactional
    void getAllAndreiRightHandsByCountryIsEqualToSomething() throws Exception {
        AntonioBanderas country;
        if (TestUtil.findAll(em, AntonioBanderas.class).isEmpty()) {
            andreiRightHandRepository.saveAndFlush(andreiRightHand);
            country = AntonioBanderasResourceIT.createEntity();
        } else {
            country = TestUtil.findAll(em, AntonioBanderas.class).get(0);
        }
        em.persist(country);
        em.flush();
        andreiRightHand.setCountry(country);
        andreiRightHandRepository.saveAndFlush(andreiRightHand);
        Long countryId = country.getId();
        // Get all the andreiRightHandList where country equals to countryId
        defaultAndreiRightHandShouldBeFound("countryId.equals=" + countryId);

        // Get all the andreiRightHandList where country equals to (countryId + 1)
        defaultAndreiRightHandShouldNotBeFound("countryId.equals=" + (countryId + 1));
    }

    @Test
    @Transactional
    void getAllAndreiRightHandsByProvinceIsEqualToSomething() throws Exception {
        AntonioBanderas province;
        if (TestUtil.findAll(em, AntonioBanderas.class).isEmpty()) {
            andreiRightHandRepository.saveAndFlush(andreiRightHand);
            province = AntonioBanderasResourceIT.createEntity();
        } else {
            province = TestUtil.findAll(em, AntonioBanderas.class).get(0);
        }
        em.persist(province);
        em.flush();
        andreiRightHand.setProvince(province);
        andreiRightHandRepository.saveAndFlush(andreiRightHand);
        Long provinceId = province.getId();
        // Get all the andreiRightHandList where province equals to provinceId
        defaultAndreiRightHandShouldBeFound("provinceId.equals=" + provinceId);

        // Get all the andreiRightHandList where province equals to (provinceId + 1)
        defaultAndreiRightHandShouldNotBeFound("provinceId.equals=" + (provinceId + 1));
    }

    @Test
    @Transactional
    void getAllAndreiRightHandsByDistrictIsEqualToSomething() throws Exception {
        AntonioBanderas district;
        if (TestUtil.findAll(em, AntonioBanderas.class).isEmpty()) {
            andreiRightHandRepository.saveAndFlush(andreiRightHand);
            district = AntonioBanderasResourceIT.createEntity();
        } else {
            district = TestUtil.findAll(em, AntonioBanderas.class).get(0);
        }
        em.persist(district);
        em.flush();
        andreiRightHand.setDistrict(district);
        andreiRightHandRepository.saveAndFlush(andreiRightHand);
        Long districtId = district.getId();
        // Get all the andreiRightHandList where district equals to districtId
        defaultAndreiRightHandShouldBeFound("districtId.equals=" + districtId);

        // Get all the andreiRightHandList where district equals to (districtId + 1)
        defaultAndreiRightHandShouldNotBeFound("districtId.equals=" + (districtId + 1));
    }

    @Test
    @Transactional
    void getAllAndreiRightHandsByWardIsEqualToSomething() throws Exception {
        AntonioBanderas ward;
        if (TestUtil.findAll(em, AntonioBanderas.class).isEmpty()) {
            andreiRightHandRepository.saveAndFlush(andreiRightHand);
            ward = AntonioBanderasResourceIT.createEntity();
        } else {
            ward = TestUtil.findAll(em, AntonioBanderas.class).get(0);
        }
        em.persist(ward);
        em.flush();
        andreiRightHand.setWard(ward);
        andreiRightHandRepository.saveAndFlush(andreiRightHand);
        Long wardId = ward.getId();
        // Get all the andreiRightHandList where ward equals to wardId
        defaultAndreiRightHandShouldBeFound("wardId.equals=" + wardId);

        // Get all the andreiRightHandList where ward equals to (wardId + 1)
        defaultAndreiRightHandShouldNotBeFound("wardId.equals=" + (wardId + 1));
    }

    private void defaultAndreiRightHandFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAndreiRightHandShouldBeFound(shouldBeFound);
        defaultAndreiRightHandShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAndreiRightHandShouldBeFound(String filter) throws Exception {
        restAndreiRightHandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(andreiRightHand.getId().intValue())))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS)))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.doubleValue())))
            .andExpect(jsonPath("$.[*].lon").value(hasItem(DEFAULT_LON.doubleValue())));

        // Check, that the count call also returns 1
        restAndreiRightHandMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAndreiRightHandShouldNotBeFound(String filter) throws Exception {
        restAndreiRightHandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAndreiRightHandMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAndreiRightHand() throws Exception {
        // Get the andreiRightHand
        restAndreiRightHandMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAndreiRightHand() throws Exception {
        // Initialize the database
        insertedAndreiRightHand = andreiRightHandRepository.saveAndFlush(andreiRightHand);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the andreiRightHand
        AndreiRightHand updatedAndreiRightHand = andreiRightHandRepository.findById(andreiRightHand.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAndreiRightHand are not directly saved in db
        em.detach(updatedAndreiRightHand);
        updatedAndreiRightHand.details(UPDATED_DETAILS).lat(UPDATED_LAT).lon(UPDATED_LON);

        restAndreiRightHandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAndreiRightHand.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAndreiRightHand))
            )
            .andExpect(status().isOk());

        // Validate the AndreiRightHand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAndreiRightHandToMatchAllProperties(updatedAndreiRightHand);
    }

    @Test
    @Transactional
    void putNonExistingAndreiRightHand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        andreiRightHand.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAndreiRightHandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, andreiRightHand.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(andreiRightHand))
            )
            .andExpect(status().isBadRequest());

        // Validate the AndreiRightHand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAndreiRightHand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        andreiRightHand.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAndreiRightHandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(andreiRightHand))
            )
            .andExpect(status().isBadRequest());

        // Validate the AndreiRightHand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAndreiRightHand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        andreiRightHand.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAndreiRightHandMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(andreiRightHand)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AndreiRightHand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAndreiRightHandWithPatch() throws Exception {
        // Initialize the database
        insertedAndreiRightHand = andreiRightHandRepository.saveAndFlush(andreiRightHand);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the andreiRightHand using partial update
        AndreiRightHand partialUpdatedAndreiRightHand = new AndreiRightHand();
        partialUpdatedAndreiRightHand.setId(andreiRightHand.getId());

        partialUpdatedAndreiRightHand.details(UPDATED_DETAILS).lon(UPDATED_LON);

        restAndreiRightHandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAndreiRightHand.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAndreiRightHand))
            )
            .andExpect(status().isOk());

        // Validate the AndreiRightHand in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAndreiRightHandUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAndreiRightHand, andreiRightHand),
            getPersistedAndreiRightHand(andreiRightHand)
        );
    }

    @Test
    @Transactional
    void fullUpdateAndreiRightHandWithPatch() throws Exception {
        // Initialize the database
        insertedAndreiRightHand = andreiRightHandRepository.saveAndFlush(andreiRightHand);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the andreiRightHand using partial update
        AndreiRightHand partialUpdatedAndreiRightHand = new AndreiRightHand();
        partialUpdatedAndreiRightHand.setId(andreiRightHand.getId());

        partialUpdatedAndreiRightHand.details(UPDATED_DETAILS).lat(UPDATED_LAT).lon(UPDATED_LON);

        restAndreiRightHandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAndreiRightHand.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAndreiRightHand))
            )
            .andExpect(status().isOk());

        // Validate the AndreiRightHand in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAndreiRightHandUpdatableFieldsEquals(
            partialUpdatedAndreiRightHand,
            getPersistedAndreiRightHand(partialUpdatedAndreiRightHand)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAndreiRightHand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        andreiRightHand.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAndreiRightHandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, andreiRightHand.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(andreiRightHand))
            )
            .andExpect(status().isBadRequest());

        // Validate the AndreiRightHand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAndreiRightHand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        andreiRightHand.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAndreiRightHandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(andreiRightHand))
            )
            .andExpect(status().isBadRequest());

        // Validate the AndreiRightHand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAndreiRightHand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        andreiRightHand.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAndreiRightHandMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(andreiRightHand)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AndreiRightHand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAndreiRightHand() throws Exception {
        // Initialize the database
        insertedAndreiRightHand = andreiRightHandRepository.saveAndFlush(andreiRightHand);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the andreiRightHand
        restAndreiRightHandMockMvc
            .perform(delete(ENTITY_API_URL_ID, andreiRightHand.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return andreiRightHandRepository.count();
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

    protected AndreiRightHand getPersistedAndreiRightHand(AndreiRightHand andreiRightHand) {
        return andreiRightHandRepository.findById(andreiRightHand.getId()).orElseThrow();
    }

    protected void assertPersistedAndreiRightHandToMatchAllProperties(AndreiRightHand expectedAndreiRightHand) {
        assertAndreiRightHandAllPropertiesEquals(expectedAndreiRightHand, getPersistedAndreiRightHand(expectedAndreiRightHand));
    }

    protected void assertPersistedAndreiRightHandToMatchUpdatableProperties(AndreiRightHand expectedAndreiRightHand) {
        assertAndreiRightHandAllUpdatablePropertiesEquals(expectedAndreiRightHand, getPersistedAndreiRightHand(expectedAndreiRightHand));
    }
}
