package ai.realworld.web.rest;

import static ai.realworld.domain.AlPedroTaxAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlPedroTax;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.enumeration.PeteType;
import ai.realworld.repository.AlPedroTaxRepository;
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
 * Integration tests for the {@link AlPedroTaxResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlPedroTaxResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_WEIGHT = 1;
    private static final Integer UPDATED_WEIGHT = 2;
    private static final Integer SMALLER_WEIGHT = 1 - 1;

    private static final PeteType DEFAULT_PROPERTY_TYPE = PeteType.VILLA;
    private static final PeteType UPDATED_PROPERTY_TYPE = PeteType.ROOM;

    private static final String ENTITY_API_URL = "/api/al-pedro-taxes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlPedroTaxRepository alPedroTaxRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlPedroTaxMockMvc;

    private AlPedroTax alPedroTax;

    private AlPedroTax insertedAlPedroTax;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPedroTax createEntity() {
        return new AlPedroTax()
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
    public static AlPedroTax createUpdatedEntity() {
        return new AlPedroTax()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .weight(UPDATED_WEIGHT)
            .propertyType(UPDATED_PROPERTY_TYPE);
    }

    @BeforeEach
    public void initTest() {
        alPedroTax = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlPedroTax != null) {
            alPedroTaxRepository.delete(insertedAlPedroTax);
            insertedAlPedroTax = null;
        }
    }

    @Test
    @Transactional
    void createAlPedroTax() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlPedroTax
        var returnedAlPedroTax = om.readValue(
            restAlPedroTaxMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPedroTax)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlPedroTax.class
        );

        // Validate the AlPedroTax in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlPedroTaxUpdatableFieldsEquals(returnedAlPedroTax, getPersistedAlPedroTax(returnedAlPedroTax));

        insertedAlPedroTax = returnedAlPedroTax;
    }

    @Test
    @Transactional
    void createAlPedroTaxWithExistingId() throws Exception {
        // Create the AlPedroTax with an existing ID
        alPedroTax.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlPedroTaxMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPedroTax)))
            .andExpect(status().isBadRequest());

        // Validate the AlPedroTax in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alPedroTax.setName(null);

        // Create the AlPedroTax, which fails.

        restAlPedroTaxMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPedroTax)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlPedroTaxes() throws Exception {
        // Initialize the database
        insertedAlPedroTax = alPedroTaxRepository.saveAndFlush(alPedroTax);

        // Get all the alPedroTaxList
        restAlPedroTaxMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPedroTax.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)))
            .andExpect(jsonPath("$.[*].propertyType").value(hasItem(DEFAULT_PROPERTY_TYPE.toString())));
    }

    @Test
    @Transactional
    void getAlPedroTax() throws Exception {
        // Initialize the database
        insertedAlPedroTax = alPedroTaxRepository.saveAndFlush(alPedroTax);

        // Get the alPedroTax
        restAlPedroTaxMockMvc
            .perform(get(ENTITY_API_URL_ID, alPedroTax.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alPedroTax.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT))
            .andExpect(jsonPath("$.propertyType").value(DEFAULT_PROPERTY_TYPE.toString()));
    }

    @Test
    @Transactional
    void getAlPedroTaxesByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlPedroTax = alPedroTaxRepository.saveAndFlush(alPedroTax);

        Long id = alPedroTax.getId();

        defaultAlPedroTaxFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlPedroTaxFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlPedroTaxFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlPedroTaxesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPedroTax = alPedroTaxRepository.saveAndFlush(alPedroTax);

        // Get all the alPedroTaxList where name equals to
        defaultAlPedroTaxFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlPedroTaxesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPedroTax = alPedroTaxRepository.saveAndFlush(alPedroTax);

        // Get all the alPedroTaxList where name in
        defaultAlPedroTaxFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlPedroTaxesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPedroTax = alPedroTaxRepository.saveAndFlush(alPedroTax);

        // Get all the alPedroTaxList where name is not null
        defaultAlPedroTaxFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPedroTaxesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPedroTax = alPedroTaxRepository.saveAndFlush(alPedroTax);

        // Get all the alPedroTaxList where name contains
        defaultAlPedroTaxFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlPedroTaxesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPedroTax = alPedroTaxRepository.saveAndFlush(alPedroTax);

        // Get all the alPedroTaxList where name does not contain
        defaultAlPedroTaxFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAlPedroTaxesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPedroTax = alPedroTaxRepository.saveAndFlush(alPedroTax);

        // Get all the alPedroTaxList where description equals to
        defaultAlPedroTaxFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAlPedroTaxesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPedroTax = alPedroTaxRepository.saveAndFlush(alPedroTax);

        // Get all the alPedroTaxList where description in
        defaultAlPedroTaxFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllAlPedroTaxesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPedroTax = alPedroTaxRepository.saveAndFlush(alPedroTax);

        // Get all the alPedroTaxList where description is not null
        defaultAlPedroTaxFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPedroTaxesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPedroTax = alPedroTaxRepository.saveAndFlush(alPedroTax);

        // Get all the alPedroTaxList where description contains
        defaultAlPedroTaxFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAlPedroTaxesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPedroTax = alPedroTaxRepository.saveAndFlush(alPedroTax);

        // Get all the alPedroTaxList where description does not contain
        defaultAlPedroTaxFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllAlPedroTaxesByWeightIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPedroTax = alPedroTaxRepository.saveAndFlush(alPedroTax);

        // Get all the alPedroTaxList where weight equals to
        defaultAlPedroTaxFiltering("weight.equals=" + DEFAULT_WEIGHT, "weight.equals=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlPedroTaxesByWeightIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPedroTax = alPedroTaxRepository.saveAndFlush(alPedroTax);

        // Get all the alPedroTaxList where weight in
        defaultAlPedroTaxFiltering("weight.in=" + DEFAULT_WEIGHT + "," + UPDATED_WEIGHT, "weight.in=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlPedroTaxesByWeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPedroTax = alPedroTaxRepository.saveAndFlush(alPedroTax);

        // Get all the alPedroTaxList where weight is not null
        defaultAlPedroTaxFiltering("weight.specified=true", "weight.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPedroTaxesByWeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPedroTax = alPedroTaxRepository.saveAndFlush(alPedroTax);

        // Get all the alPedroTaxList where weight is greater than or equal to
        defaultAlPedroTaxFiltering("weight.greaterThanOrEqual=" + DEFAULT_WEIGHT, "weight.greaterThanOrEqual=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlPedroTaxesByWeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPedroTax = alPedroTaxRepository.saveAndFlush(alPedroTax);

        // Get all the alPedroTaxList where weight is less than or equal to
        defaultAlPedroTaxFiltering("weight.lessThanOrEqual=" + DEFAULT_WEIGHT, "weight.lessThanOrEqual=" + SMALLER_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlPedroTaxesByWeightIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlPedroTax = alPedroTaxRepository.saveAndFlush(alPedroTax);

        // Get all the alPedroTaxList where weight is less than
        defaultAlPedroTaxFiltering("weight.lessThan=" + UPDATED_WEIGHT, "weight.lessThan=" + DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlPedroTaxesByWeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlPedroTax = alPedroTaxRepository.saveAndFlush(alPedroTax);

        // Get all the alPedroTaxList where weight is greater than
        defaultAlPedroTaxFiltering("weight.greaterThan=" + SMALLER_WEIGHT, "weight.greaterThan=" + DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAlPedroTaxesByPropertyTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPedroTax = alPedroTaxRepository.saveAndFlush(alPedroTax);

        // Get all the alPedroTaxList where propertyType equals to
        defaultAlPedroTaxFiltering("propertyType.equals=" + DEFAULT_PROPERTY_TYPE, "propertyType.equals=" + UPDATED_PROPERTY_TYPE);
    }

    @Test
    @Transactional
    void getAllAlPedroTaxesByPropertyTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPedroTax = alPedroTaxRepository.saveAndFlush(alPedroTax);

        // Get all the alPedroTaxList where propertyType in
        defaultAlPedroTaxFiltering(
            "propertyType.in=" + DEFAULT_PROPERTY_TYPE + "," + UPDATED_PROPERTY_TYPE,
            "propertyType.in=" + UPDATED_PROPERTY_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAlPedroTaxesByPropertyTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPedroTax = alPedroTaxRepository.saveAndFlush(alPedroTax);

        // Get all the alPedroTaxList where propertyType is not null
        defaultAlPedroTaxFiltering("propertyType.specified=true", "propertyType.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPedroTaxesByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alPedroTaxRepository.saveAndFlush(alPedroTax);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alPedroTax.setApplication(application);
        alPedroTaxRepository.saveAndFlush(alPedroTax);
        UUID applicationId = application.getId();
        // Get all the alPedroTaxList where application equals to applicationId
        defaultAlPedroTaxShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alPedroTaxList where application equals to UUID.randomUUID()
        defaultAlPedroTaxShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlPedroTaxFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlPedroTaxShouldBeFound(shouldBeFound);
        defaultAlPedroTaxShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlPedroTaxShouldBeFound(String filter) throws Exception {
        restAlPedroTaxMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPedroTax.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)))
            .andExpect(jsonPath("$.[*].propertyType").value(hasItem(DEFAULT_PROPERTY_TYPE.toString())));

        // Check, that the count call also returns 1
        restAlPedroTaxMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlPedroTaxShouldNotBeFound(String filter) throws Exception {
        restAlPedroTaxMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlPedroTaxMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlPedroTax() throws Exception {
        // Get the alPedroTax
        restAlPedroTaxMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlPedroTax() throws Exception {
        // Initialize the database
        insertedAlPedroTax = alPedroTaxRepository.saveAndFlush(alPedroTax);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPedroTax
        AlPedroTax updatedAlPedroTax = alPedroTaxRepository.findById(alPedroTax.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlPedroTax are not directly saved in db
        em.detach(updatedAlPedroTax);
        updatedAlPedroTax.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).weight(UPDATED_WEIGHT).propertyType(UPDATED_PROPERTY_TYPE);

        restAlPedroTaxMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlPedroTax.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlPedroTax))
            )
            .andExpect(status().isOk());

        // Validate the AlPedroTax in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlPedroTaxToMatchAllProperties(updatedAlPedroTax);
    }

    @Test
    @Transactional
    void putNonExistingAlPedroTax() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPedroTax.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPedroTaxMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alPedroTax.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPedroTax))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPedroTax in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlPedroTax() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPedroTax.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPedroTaxMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPedroTax))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPedroTax in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlPedroTax() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPedroTax.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPedroTaxMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPedroTax)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPedroTax in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlPedroTaxWithPatch() throws Exception {
        // Initialize the database
        insertedAlPedroTax = alPedroTaxRepository.saveAndFlush(alPedroTax);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPedroTax using partial update
        AlPedroTax partialUpdatedAlPedroTax = new AlPedroTax();
        partialUpdatedAlPedroTax.setId(alPedroTax.getId());

        partialUpdatedAlPedroTax
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .weight(UPDATED_WEIGHT)
            .propertyType(UPDATED_PROPERTY_TYPE);

        restAlPedroTaxMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPedroTax.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPedroTax))
            )
            .andExpect(status().isOk());

        // Validate the AlPedroTax in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPedroTaxUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlPedroTax, alPedroTax),
            getPersistedAlPedroTax(alPedroTax)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlPedroTaxWithPatch() throws Exception {
        // Initialize the database
        insertedAlPedroTax = alPedroTaxRepository.saveAndFlush(alPedroTax);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPedroTax using partial update
        AlPedroTax partialUpdatedAlPedroTax = new AlPedroTax();
        partialUpdatedAlPedroTax.setId(alPedroTax.getId());

        partialUpdatedAlPedroTax
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .weight(UPDATED_WEIGHT)
            .propertyType(UPDATED_PROPERTY_TYPE);

        restAlPedroTaxMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPedroTax.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPedroTax))
            )
            .andExpect(status().isOk());

        // Validate the AlPedroTax in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPedroTaxUpdatableFieldsEquals(partialUpdatedAlPedroTax, getPersistedAlPedroTax(partialUpdatedAlPedroTax));
    }

    @Test
    @Transactional
    void patchNonExistingAlPedroTax() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPedroTax.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPedroTaxMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alPedroTax.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPedroTax))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPedroTax in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlPedroTax() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPedroTax.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPedroTaxMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPedroTax))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPedroTax in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlPedroTax() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPedroTax.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPedroTaxMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alPedroTax)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPedroTax in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlPedroTax() throws Exception {
        // Initialize the database
        insertedAlPedroTax = alPedroTaxRepository.saveAndFlush(alPedroTax);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alPedroTax
        restAlPedroTaxMockMvc
            .perform(delete(ENTITY_API_URL_ID, alPedroTax.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alPedroTaxRepository.count();
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

    protected AlPedroTax getPersistedAlPedroTax(AlPedroTax alPedroTax) {
        return alPedroTaxRepository.findById(alPedroTax.getId()).orElseThrow();
    }

    protected void assertPersistedAlPedroTaxToMatchAllProperties(AlPedroTax expectedAlPedroTax) {
        assertAlPedroTaxAllPropertiesEquals(expectedAlPedroTax, getPersistedAlPedroTax(expectedAlPedroTax));
    }

    protected void assertPersistedAlPedroTaxToMatchUpdatableProperties(AlPedroTax expectedAlPedroTax) {
        assertAlPedroTaxAllUpdatablePropertiesEquals(expectedAlPedroTax, getPersistedAlPedroTax(expectedAlPedroTax));
    }
}
