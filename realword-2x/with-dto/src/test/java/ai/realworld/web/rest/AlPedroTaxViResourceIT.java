package ai.realworld.web.rest;

import static ai.realworld.domain.AlPedroTaxViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlPedroTaxVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.enumeration.PeteType;
import ai.realworld.repository.AlPedroTaxViRepository;
import ai.realworld.service.dto.AlPedroTaxViDTO;
import ai.realworld.service.mapper.AlPedroTaxViMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.UUID;
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
 * Integration tests for the {@link AlPedroTaxViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlPedroTaxViResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_WEIGHT = 1;
    private static final Integer UPDATED_WEIGHT = 2;
    private static final Integer SMALLER_WEIGHT = 1 - 1;

    private static final PeteType DEFAULT_PROPERTY_TYPE = PeteType.VILLA;
    private static final PeteType UPDATED_PROPERTY_TYPE = PeteType.ROOM;

    private static final String ENTITY_API_URL = "/api/al-pedro-tax-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlPedroTaxViRepository alPedroTaxViRepository;

    @Autowired
    private AlPedroTaxViMapper alPedroTaxViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlPedroTaxViMockMvc;

    private AlPedroTaxVi alPedroTaxVi;

    private AlPedroTaxVi insertedAlPedroTaxVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPedroTaxVi createEntity() {
        return new AlPedroTaxVi()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .weight(DEFAULT_WEIGHT)
            .propertyType(DEFAULT_PROPERTY_TYPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPedroTaxVi createUpdatedEntity() {
        return new AlPedroTaxVi()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .weight(UPDATED_WEIGHT)
            .propertyType(UPDATED_PROPERTY_TYPE);
    }

    @BeforeEach
    public void initTest() {
        alPedroTaxVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlPedroTaxVi != null) {
            alPedroTaxViRepository.delete(insertedAlPedroTaxVi);
            insertedAlPedroTaxVi = null;
        }
    }

    @Test
    @Transactional
    void createAlPedroTaxVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlPedroTaxVi
        AlPedroTaxViDTO alPedroTaxViDTO = alPedroTaxViMapper.toDto(alPedroTaxVi);
        var returnedAlPedroTaxViDTO = om.readValue(
            restAlPedroTaxViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPedroTaxViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlPedroTaxViDTO.class
        );

        // Validate the AlPedroTaxVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlPedroTaxVi = alPedroTaxViMapper.toEntity(returnedAlPedroTaxViDTO);
        assertAlPedroTaxViUpdatableFieldsEquals(returnedAlPedroTaxVi, getPersistedAlPedroTaxVi(returnedAlPedroTaxVi));

        insertedAlPedroTaxVi = returnedAlPedroTaxVi;
    }

    @Test
    @Transactional
    void createAlPedroTaxViWithExistingId() throws Exception {
        // Create the AlPedroTaxVi with an existing ID
        alPedroTaxVi.setId(1L);
        AlPedroTaxViDTO alPedroTaxViDTO = alPedroTaxViMapper.toDto(alPedroTaxVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlPedroTaxViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPedroTaxViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlPedroTaxVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alPedroTaxVi.setName(null);

        // Create the AlPedroTaxVi, which fails.
        AlPedroTaxViDTO alPedroTaxViDTO = alPedroTaxViMapper.toDto(alPedroTaxVi);

        restAlPedroTaxViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPedroTaxViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlPedroTaxVis() throws Exception {
        // Initialize the database
        insertedAlPedroTaxVi = alPedroTaxViRepository.saveAndFlush(alPedroTaxVi);

        // Get all the alPedroTaxViList
        restAlPedroTaxViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPedroTaxVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)))
            .andExpect(jsonPath("$.[*].propertyType").value(hasItem(DEFAULT_PROPERTY_TYPE.toString())));
    }

    @Test
    @Transactional
    void getAlPedroTaxVi() throws Exception {
        // Initialize the database
        insertedAlPedroTaxVi = alPedroTaxViRepository.saveAndFlush(alPedroTaxVi);

        // Get the alPedroTaxVi
        restAlPedroTaxViMockMvc
            .perform(get(ENTITY_API_URL_ID, alPedroTaxVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alPedroTaxVi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT))
            .andExpect(jsonPath("$.propertyType").value(DEFAULT_PROPERTY_TYPE.toString()));
    }

    @Test
    @Transactional
    void getAlPedroTaxVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlPedroTaxVi = alPedroTaxViRepository.saveAndFlush(alPedroTaxVi);

        Long id = alPedroTaxVi.getId();

        defaultAlPedroTaxViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlPedroTaxViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlPedroTaxViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlPedroTaxVisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPedroTaxVi = alPedroTaxViRepository.saveAndFlush(alPedroTaxVi);

        // Get all the alPedroTaxViList where name equals to
        defaultAlPedroTaxViFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlPedroTaxVisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPedroTaxVi = alPedroTaxViRepository.saveAndFlush(alPedroTaxVi);

        // Get all the alPedroTaxViList where name in
        defaultAlPedroTaxViFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlPedroTaxVisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPedroTaxVi = alPedroTaxViRepository.saveAndFlush(alPedroTaxVi);

        // Get all the alPedroTaxViList where name is not null
        defaultAlPedroTaxViFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPedroTaxVisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPedroTaxVi = alPedroTaxViRepository.saveAndFlush(alPedroTaxVi);

        // Get all the alPedroTaxViList where name contains
        defaultAlPedroTaxViFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlPedroTaxVisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPedroTaxVi = alPedroTaxViRepository.saveAndFlush(alPedroTaxVi);

        // Get all the alPedroTaxViList where name does not contain
        defaultAlPedroTaxViFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAlPedroTaxVisByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPedroTaxVi = alPedroTaxViRepository.saveAndFlush(alPedroTaxVi);

        // Get all the alPedroTaxViList where description equals to
        defaultAlPedroTaxViFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAlPedroTaxVisByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPedroTaxVi = alPedroTaxViRepository.saveAndFlush(alPedroTaxVi);

        // Get all the alPedroTaxViList where description in
        defaultAlPedroTaxViFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllAlPedroTaxVisByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPedroTaxVi = alPedroTaxViRepository.saveAndFlush(alPedroTaxVi);

        // Get all the alPedroTaxViList where description is not null
        defaultAlPedroTaxViFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPedroTaxVisByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPedroTaxVi = alPedroTaxViRepository.saveAndFlush(alPedroTaxVi);

        // Get all the alPedroTaxViList where description contains
        defaultAlPedroTaxViFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAlPedroTaxVisByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPedroTaxVi = alPedroTaxViRepository.saveAndFlush(alPedroTaxVi);

        // Get all the alPedroTaxViList where description does not contain
        defaultAlPedroTaxViFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllAlPedroTaxVisByWeightIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPedroTaxVi = alPedroTaxViRepository.saveAndFlush(alPedroTaxVi);

        // Get all the alPedroTaxViList where weight equals to
        defaultAlPedroTaxViFiltering("weight.equals=" + DEFAULT_WEIGHT, "weight.equals=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlPedroTaxVisByWeightIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPedroTaxVi = alPedroTaxViRepository.saveAndFlush(alPedroTaxVi);

        // Get all the alPedroTaxViList where weight in
        defaultAlPedroTaxViFiltering("weight.in=" + DEFAULT_WEIGHT + "," + UPDATED_WEIGHT, "weight.in=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlPedroTaxVisByWeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPedroTaxVi = alPedroTaxViRepository.saveAndFlush(alPedroTaxVi);

        // Get all the alPedroTaxViList where weight is not null
        defaultAlPedroTaxViFiltering("weight.specified=true", "weight.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPedroTaxVisByWeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPedroTaxVi = alPedroTaxViRepository.saveAndFlush(alPedroTaxVi);

        // Get all the alPedroTaxViList where weight is greater than or equal to
        defaultAlPedroTaxViFiltering("weight.greaterThanOrEqual=" + DEFAULT_WEIGHT, "weight.greaterThanOrEqual=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlPedroTaxVisByWeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPedroTaxVi = alPedroTaxViRepository.saveAndFlush(alPedroTaxVi);

        // Get all the alPedroTaxViList where weight is less than or equal to
        defaultAlPedroTaxViFiltering("weight.lessThanOrEqual=" + DEFAULT_WEIGHT, "weight.lessThanOrEqual=" + SMALLER_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlPedroTaxVisByWeightIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlPedroTaxVi = alPedroTaxViRepository.saveAndFlush(alPedroTaxVi);

        // Get all the alPedroTaxViList where weight is less than
        defaultAlPedroTaxViFiltering("weight.lessThan=" + UPDATED_WEIGHT, "weight.lessThan=" + DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlPedroTaxVisByWeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlPedroTaxVi = alPedroTaxViRepository.saveAndFlush(alPedroTaxVi);

        // Get all the alPedroTaxViList where weight is greater than
        defaultAlPedroTaxViFiltering("weight.greaterThan=" + SMALLER_WEIGHT, "weight.greaterThan=" + DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlPedroTaxVisByPropertyTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPedroTaxVi = alPedroTaxViRepository.saveAndFlush(alPedroTaxVi);

        // Get all the alPedroTaxViList where propertyType equals to
        defaultAlPedroTaxViFiltering("propertyType.equals=" + DEFAULT_PROPERTY_TYPE, "propertyType.equals=" + UPDATED_PROPERTY_TYPE);
    }

    @Test
    @Transactional
    void getAllAlPedroTaxVisByPropertyTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPedroTaxVi = alPedroTaxViRepository.saveAndFlush(alPedroTaxVi);

        // Get all the alPedroTaxViList where propertyType in
        defaultAlPedroTaxViFiltering(
            "propertyType.in=" + DEFAULT_PROPERTY_TYPE + "," + UPDATED_PROPERTY_TYPE,
            "propertyType.in=" + UPDATED_PROPERTY_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAlPedroTaxVisByPropertyTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPedroTaxVi = alPedroTaxViRepository.saveAndFlush(alPedroTaxVi);

        // Get all the alPedroTaxViList where propertyType is not null
        defaultAlPedroTaxViFiltering("propertyType.specified=true", "propertyType.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPedroTaxVisByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alPedroTaxViRepository.saveAndFlush(alPedroTaxVi);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alPedroTaxVi.setApplication(application);
        alPedroTaxViRepository.saveAndFlush(alPedroTaxVi);
        UUID applicationId = application.getId();
        // Get all the alPedroTaxViList where application equals to applicationId
        defaultAlPedroTaxViShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alPedroTaxViList where application equals to UUID.randomUUID()
        defaultAlPedroTaxViShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlPedroTaxViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlPedroTaxViShouldBeFound(shouldBeFound);
        defaultAlPedroTaxViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlPedroTaxViShouldBeFound(String filter) throws Exception {
        restAlPedroTaxViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPedroTaxVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)))
            .andExpect(jsonPath("$.[*].propertyType").value(hasItem(DEFAULT_PROPERTY_TYPE.toString())));

        // Check, that the count call also returns 1
        restAlPedroTaxViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlPedroTaxViShouldNotBeFound(String filter) throws Exception {
        restAlPedroTaxViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlPedroTaxViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlPedroTaxVi() throws Exception {
        // Get the alPedroTaxVi
        restAlPedroTaxViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlPedroTaxVi() throws Exception {
        // Initialize the database
        insertedAlPedroTaxVi = alPedroTaxViRepository.saveAndFlush(alPedroTaxVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPedroTaxVi
        AlPedroTaxVi updatedAlPedroTaxVi = alPedroTaxViRepository.findById(alPedroTaxVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlPedroTaxVi are not directly saved in db
        em.detach(updatedAlPedroTaxVi);
        updatedAlPedroTaxVi.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).weight(UPDATED_WEIGHT).propertyType(UPDATED_PROPERTY_TYPE);
        AlPedroTaxViDTO alPedroTaxViDTO = alPedroTaxViMapper.toDto(updatedAlPedroTaxVi);

        restAlPedroTaxViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alPedroTaxViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPedroTaxViDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlPedroTaxVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlPedroTaxViToMatchAllProperties(updatedAlPedroTaxVi);
    }

    @Test
    @Transactional
    void putNonExistingAlPedroTaxVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPedroTaxVi.setId(longCount.incrementAndGet());

        // Create the AlPedroTaxVi
        AlPedroTaxViDTO alPedroTaxViDTO = alPedroTaxViMapper.toDto(alPedroTaxVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPedroTaxViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alPedroTaxViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPedroTaxViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPedroTaxVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlPedroTaxVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPedroTaxVi.setId(longCount.incrementAndGet());

        // Create the AlPedroTaxVi
        AlPedroTaxViDTO alPedroTaxViDTO = alPedroTaxViMapper.toDto(alPedroTaxVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPedroTaxViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPedroTaxViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPedroTaxVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlPedroTaxVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPedroTaxVi.setId(longCount.incrementAndGet());

        // Create the AlPedroTaxVi
        AlPedroTaxViDTO alPedroTaxViDTO = alPedroTaxViMapper.toDto(alPedroTaxVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPedroTaxViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPedroTaxViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPedroTaxVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlPedroTaxViWithPatch() throws Exception {
        // Initialize the database
        insertedAlPedroTaxVi = alPedroTaxViRepository.saveAndFlush(alPedroTaxVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPedroTaxVi using partial update
        AlPedroTaxVi partialUpdatedAlPedroTaxVi = new AlPedroTaxVi();
        partialUpdatedAlPedroTaxVi.setId(alPedroTaxVi.getId());

        partialUpdatedAlPedroTaxVi.description(UPDATED_DESCRIPTION);

        restAlPedroTaxViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPedroTaxVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPedroTaxVi))
            )
            .andExpect(status().isOk());

        // Validate the AlPedroTaxVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPedroTaxViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlPedroTaxVi, alPedroTaxVi),
            getPersistedAlPedroTaxVi(alPedroTaxVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlPedroTaxViWithPatch() throws Exception {
        // Initialize the database
        insertedAlPedroTaxVi = alPedroTaxViRepository.saveAndFlush(alPedroTaxVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPedroTaxVi using partial update
        AlPedroTaxVi partialUpdatedAlPedroTaxVi = new AlPedroTaxVi();
        partialUpdatedAlPedroTaxVi.setId(alPedroTaxVi.getId());

        partialUpdatedAlPedroTaxVi
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .weight(UPDATED_WEIGHT)
            .propertyType(UPDATED_PROPERTY_TYPE);

        restAlPedroTaxViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPedroTaxVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPedroTaxVi))
            )
            .andExpect(status().isOk());

        // Validate the AlPedroTaxVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPedroTaxViUpdatableFieldsEquals(partialUpdatedAlPedroTaxVi, getPersistedAlPedroTaxVi(partialUpdatedAlPedroTaxVi));
    }

    @Test
    @Transactional
    void patchNonExistingAlPedroTaxVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPedroTaxVi.setId(longCount.incrementAndGet());

        // Create the AlPedroTaxVi
        AlPedroTaxViDTO alPedroTaxViDTO = alPedroTaxViMapper.toDto(alPedroTaxVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPedroTaxViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alPedroTaxViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPedroTaxViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPedroTaxVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlPedroTaxVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPedroTaxVi.setId(longCount.incrementAndGet());

        // Create the AlPedroTaxVi
        AlPedroTaxViDTO alPedroTaxViDTO = alPedroTaxViMapper.toDto(alPedroTaxVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPedroTaxViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPedroTaxViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPedroTaxVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlPedroTaxVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPedroTaxVi.setId(longCount.incrementAndGet());

        // Create the AlPedroTaxVi
        AlPedroTaxViDTO alPedroTaxViDTO = alPedroTaxViMapper.toDto(alPedroTaxVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPedroTaxViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alPedroTaxViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPedroTaxVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlPedroTaxVi() throws Exception {
        // Initialize the database
        insertedAlPedroTaxVi = alPedroTaxViRepository.saveAndFlush(alPedroTaxVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alPedroTaxVi
        restAlPedroTaxViMockMvc
            .perform(delete(ENTITY_API_URL_ID, alPedroTaxVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alPedroTaxViRepository.count();
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

    protected AlPedroTaxVi getPersistedAlPedroTaxVi(AlPedroTaxVi alPedroTaxVi) {
        return alPedroTaxViRepository.findById(alPedroTaxVi.getId()).orElseThrow();
    }

    protected void assertPersistedAlPedroTaxViToMatchAllProperties(AlPedroTaxVi expectedAlPedroTaxVi) {
        assertAlPedroTaxViAllPropertiesEquals(expectedAlPedroTaxVi, getPersistedAlPedroTaxVi(expectedAlPedroTaxVi));
    }

    protected void assertPersistedAlPedroTaxViToMatchUpdatableProperties(AlPedroTaxVi expectedAlPedroTaxVi) {
        assertAlPedroTaxViAllUpdatablePropertiesEquals(expectedAlPedroTaxVi, getPersistedAlPedroTaxVi(expectedAlPedroTaxVi));
    }
}
