package ai.realworld.web.rest;

import static ai.realworld.domain.AlAlexTypeAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlAlexType;
import ai.realworld.domain.JohnLennon;
import ai.realworld.repository.AlAlexTypeRepository;
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
 * Integration tests for the {@link AlAlexTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlAlexTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CAN_DO_RETAIL = false;
    private static final Boolean UPDATED_CAN_DO_RETAIL = true;

    private static final Boolean DEFAULT_IS_ORG_DIVISION = false;
    private static final Boolean UPDATED_IS_ORG_DIVISION = true;

    private static final String DEFAULT_CONFIG_JASON = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_JASON = "BBBBBBBBBB";

    private static final Integer DEFAULT_TREE_DEPTH = 1;
    private static final Integer UPDATED_TREE_DEPTH = 2;
    private static final Integer SMALLER_TREE_DEPTH = 1 - 1;

    private static final String ENTITY_API_URL = "/api/al-alex-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlAlexTypeRepository alAlexTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlAlexTypeMockMvc;

    private AlAlexType alAlexType;

    private AlAlexType insertedAlAlexType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlAlexType createEntity() {
        return new AlAlexType()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .canDoRetail(DEFAULT_CAN_DO_RETAIL)
            .isOrgDivision(DEFAULT_IS_ORG_DIVISION)
            .configJason(DEFAULT_CONFIG_JASON)
            .treeDepth(DEFAULT_TREE_DEPTH);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlAlexType createUpdatedEntity() {
        return new AlAlexType()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .canDoRetail(UPDATED_CAN_DO_RETAIL)
            .isOrgDivision(UPDATED_IS_ORG_DIVISION)
            .configJason(UPDATED_CONFIG_JASON)
            .treeDepth(UPDATED_TREE_DEPTH);
    }

    @BeforeEach
    public void initTest() {
        alAlexType = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlAlexType != null) {
            alAlexTypeRepository.delete(insertedAlAlexType);
            insertedAlAlexType = null;
        }
    }

    @Test
    @Transactional
    void createAlAlexType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlAlexType
        var returnedAlAlexType = om.readValue(
            restAlAlexTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alAlexType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlAlexType.class
        );

        // Validate the AlAlexType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlAlexTypeUpdatableFieldsEquals(returnedAlAlexType, getPersistedAlAlexType(returnedAlAlexType));

        insertedAlAlexType = returnedAlAlexType;
    }

    @Test
    @Transactional
    void createAlAlexTypeWithExistingId() throws Exception {
        // Create the AlAlexType with an existing ID
        insertedAlAlexType = alAlexTypeRepository.saveAndFlush(alAlexType);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlAlexTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alAlexType)))
            .andExpect(status().isBadRequest());

        // Validate the AlAlexType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alAlexType.setName(null);

        // Create the AlAlexType, which fails.

        restAlAlexTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alAlexType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlAlexTypes() throws Exception {
        // Initialize the database
        insertedAlAlexType = alAlexTypeRepository.saveAndFlush(alAlexType);

        // Get all the alAlexTypeList
        restAlAlexTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alAlexType.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].canDoRetail").value(hasItem(DEFAULT_CAN_DO_RETAIL.booleanValue())))
            .andExpect(jsonPath("$.[*].isOrgDivision").value(hasItem(DEFAULT_IS_ORG_DIVISION.booleanValue())))
            .andExpect(jsonPath("$.[*].configJason").value(hasItem(DEFAULT_CONFIG_JASON)))
            .andExpect(jsonPath("$.[*].treeDepth").value(hasItem(DEFAULT_TREE_DEPTH)));
    }

    @Test
    @Transactional
    void getAlAlexType() throws Exception {
        // Initialize the database
        insertedAlAlexType = alAlexTypeRepository.saveAndFlush(alAlexType);

        // Get the alAlexType
        restAlAlexTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, alAlexType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alAlexType.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.canDoRetail").value(DEFAULT_CAN_DO_RETAIL.booleanValue()))
            .andExpect(jsonPath("$.isOrgDivision").value(DEFAULT_IS_ORG_DIVISION.booleanValue()))
            .andExpect(jsonPath("$.configJason").value(DEFAULT_CONFIG_JASON))
            .andExpect(jsonPath("$.treeDepth").value(DEFAULT_TREE_DEPTH));
    }

    @Test
    @Transactional
    void getAlAlexTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlAlexType = alAlexTypeRepository.saveAndFlush(alAlexType);

        UUID id = alAlexType.getId();

        defaultAlAlexTypeFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllAlAlexTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlAlexType = alAlexTypeRepository.saveAndFlush(alAlexType);

        // Get all the alAlexTypeList where name equals to
        defaultAlAlexTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlAlexTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlAlexType = alAlexTypeRepository.saveAndFlush(alAlexType);

        // Get all the alAlexTypeList where name in
        defaultAlAlexTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlAlexTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlAlexType = alAlexTypeRepository.saveAndFlush(alAlexType);

        // Get all the alAlexTypeList where name is not null
        defaultAlAlexTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAlAlexTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlAlexType = alAlexTypeRepository.saveAndFlush(alAlexType);

        // Get all the alAlexTypeList where name contains
        defaultAlAlexTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlAlexTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlAlexType = alAlexTypeRepository.saveAndFlush(alAlexType);

        // Get all the alAlexTypeList where name does not contain
        defaultAlAlexTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAlAlexTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlAlexType = alAlexTypeRepository.saveAndFlush(alAlexType);

        // Get all the alAlexTypeList where description equals to
        defaultAlAlexTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAlAlexTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlAlexType = alAlexTypeRepository.saveAndFlush(alAlexType);

        // Get all the alAlexTypeList where description in
        defaultAlAlexTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllAlAlexTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlAlexType = alAlexTypeRepository.saveAndFlush(alAlexType);

        // Get all the alAlexTypeList where description is not null
        defaultAlAlexTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllAlAlexTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedAlAlexType = alAlexTypeRepository.saveAndFlush(alAlexType);

        // Get all the alAlexTypeList where description contains
        defaultAlAlexTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAlAlexTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlAlexType = alAlexTypeRepository.saveAndFlush(alAlexType);

        // Get all the alAlexTypeList where description does not contain
        defaultAlAlexTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllAlAlexTypesByCanDoRetailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlAlexType = alAlexTypeRepository.saveAndFlush(alAlexType);

        // Get all the alAlexTypeList where canDoRetail equals to
        defaultAlAlexTypeFiltering("canDoRetail.equals=" + DEFAULT_CAN_DO_RETAIL, "canDoRetail.equals=" + UPDATED_CAN_DO_RETAIL);
    }

    @Test
    @Transactional
    void getAllAlAlexTypesByCanDoRetailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlAlexType = alAlexTypeRepository.saveAndFlush(alAlexType);

        // Get all the alAlexTypeList where canDoRetail in
        defaultAlAlexTypeFiltering(
            "canDoRetail.in=" + DEFAULT_CAN_DO_RETAIL + "," + UPDATED_CAN_DO_RETAIL,
            "canDoRetail.in=" + UPDATED_CAN_DO_RETAIL
        );
    }

    @Test
    @Transactional
    void getAllAlAlexTypesByCanDoRetailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlAlexType = alAlexTypeRepository.saveAndFlush(alAlexType);

        // Get all the alAlexTypeList where canDoRetail is not null
        defaultAlAlexTypeFiltering("canDoRetail.specified=true", "canDoRetail.specified=false");
    }

    @Test
    @Transactional
    void getAllAlAlexTypesByIsOrgDivisionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlAlexType = alAlexTypeRepository.saveAndFlush(alAlexType);

        // Get all the alAlexTypeList where isOrgDivision equals to
        defaultAlAlexTypeFiltering("isOrgDivision.equals=" + DEFAULT_IS_ORG_DIVISION, "isOrgDivision.equals=" + UPDATED_IS_ORG_DIVISION);
    }

    @Test
    @Transactional
    void getAllAlAlexTypesByIsOrgDivisionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlAlexType = alAlexTypeRepository.saveAndFlush(alAlexType);

        // Get all the alAlexTypeList where isOrgDivision in
        defaultAlAlexTypeFiltering(
            "isOrgDivision.in=" + DEFAULT_IS_ORG_DIVISION + "," + UPDATED_IS_ORG_DIVISION,
            "isOrgDivision.in=" + UPDATED_IS_ORG_DIVISION
        );
    }

    @Test
    @Transactional
    void getAllAlAlexTypesByIsOrgDivisionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlAlexType = alAlexTypeRepository.saveAndFlush(alAlexType);

        // Get all the alAlexTypeList where isOrgDivision is not null
        defaultAlAlexTypeFiltering("isOrgDivision.specified=true", "isOrgDivision.specified=false");
    }

    @Test
    @Transactional
    void getAllAlAlexTypesByConfigJasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlAlexType = alAlexTypeRepository.saveAndFlush(alAlexType);

        // Get all the alAlexTypeList where configJason equals to
        defaultAlAlexTypeFiltering("configJason.equals=" + DEFAULT_CONFIG_JASON, "configJason.equals=" + UPDATED_CONFIG_JASON);
    }

    @Test
    @Transactional
    void getAllAlAlexTypesByConfigJasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlAlexType = alAlexTypeRepository.saveAndFlush(alAlexType);

        // Get all the alAlexTypeList where configJason in
        defaultAlAlexTypeFiltering(
            "configJason.in=" + DEFAULT_CONFIG_JASON + "," + UPDATED_CONFIG_JASON,
            "configJason.in=" + UPDATED_CONFIG_JASON
        );
    }

    @Test
    @Transactional
    void getAllAlAlexTypesByConfigJasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlAlexType = alAlexTypeRepository.saveAndFlush(alAlexType);

        // Get all the alAlexTypeList where configJason is not null
        defaultAlAlexTypeFiltering("configJason.specified=true", "configJason.specified=false");
    }

    @Test
    @Transactional
    void getAllAlAlexTypesByConfigJasonContainsSomething() throws Exception {
        // Initialize the database
        insertedAlAlexType = alAlexTypeRepository.saveAndFlush(alAlexType);

        // Get all the alAlexTypeList where configJason contains
        defaultAlAlexTypeFiltering("configJason.contains=" + DEFAULT_CONFIG_JASON, "configJason.contains=" + UPDATED_CONFIG_JASON);
    }

    @Test
    @Transactional
    void getAllAlAlexTypesByConfigJasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlAlexType = alAlexTypeRepository.saveAndFlush(alAlexType);

        // Get all the alAlexTypeList where configJason does not contain
        defaultAlAlexTypeFiltering(
            "configJason.doesNotContain=" + UPDATED_CONFIG_JASON,
            "configJason.doesNotContain=" + DEFAULT_CONFIG_JASON
        );
    }

    @Test
    @Transactional
    void getAllAlAlexTypesByTreeDepthIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlAlexType = alAlexTypeRepository.saveAndFlush(alAlexType);

        // Get all the alAlexTypeList where treeDepth equals to
        defaultAlAlexTypeFiltering("treeDepth.equals=" + DEFAULT_TREE_DEPTH, "treeDepth.equals=" + UPDATED_TREE_DEPTH);
    }

    @Test
    @Transactional
    void getAllAlAlexTypesByTreeDepthIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlAlexType = alAlexTypeRepository.saveAndFlush(alAlexType);

        // Get all the alAlexTypeList where treeDepth in
        defaultAlAlexTypeFiltering("treeDepth.in=" + DEFAULT_TREE_DEPTH + "," + UPDATED_TREE_DEPTH, "treeDepth.in=" + UPDATED_TREE_DEPTH);
    }

    @Test
    @Transactional
    void getAllAlAlexTypesByTreeDepthIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlAlexType = alAlexTypeRepository.saveAndFlush(alAlexType);

        // Get all the alAlexTypeList where treeDepth is not null
        defaultAlAlexTypeFiltering("treeDepth.specified=true", "treeDepth.specified=false");
    }

    @Test
    @Transactional
    void getAllAlAlexTypesByTreeDepthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlAlexType = alAlexTypeRepository.saveAndFlush(alAlexType);

        // Get all the alAlexTypeList where treeDepth is greater than or equal to
        defaultAlAlexTypeFiltering(
            "treeDepth.greaterThanOrEqual=" + DEFAULT_TREE_DEPTH,
            "treeDepth.greaterThanOrEqual=" + UPDATED_TREE_DEPTH
        );
    }

    @Test
    @Transactional
    void getAllAlAlexTypesByTreeDepthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlAlexType = alAlexTypeRepository.saveAndFlush(alAlexType);

        // Get all the alAlexTypeList where treeDepth is less than or equal to
        defaultAlAlexTypeFiltering("treeDepth.lessThanOrEqual=" + DEFAULT_TREE_DEPTH, "treeDepth.lessThanOrEqual=" + SMALLER_TREE_DEPTH);
    }

    @Test
    @Transactional
    void getAllAlAlexTypesByTreeDepthIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlAlexType = alAlexTypeRepository.saveAndFlush(alAlexType);

        // Get all the alAlexTypeList where treeDepth is less than
        defaultAlAlexTypeFiltering("treeDepth.lessThan=" + UPDATED_TREE_DEPTH, "treeDepth.lessThan=" + DEFAULT_TREE_DEPTH);
    }

    @Test
    @Transactional
    void getAllAlAlexTypesByTreeDepthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlAlexType = alAlexTypeRepository.saveAndFlush(alAlexType);

        // Get all the alAlexTypeList where treeDepth is greater than
        defaultAlAlexTypeFiltering("treeDepth.greaterThan=" + SMALLER_TREE_DEPTH, "treeDepth.greaterThan=" + DEFAULT_TREE_DEPTH);
    }

    @Test
    @Transactional
    void getAllAlAlexTypesByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alAlexTypeRepository.saveAndFlush(alAlexType);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alAlexType.setApplication(application);
        alAlexTypeRepository.saveAndFlush(alAlexType);
        UUID applicationId = application.getId();
        // Get all the alAlexTypeList where application equals to applicationId
        defaultAlAlexTypeShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alAlexTypeList where application equals to UUID.randomUUID()
        defaultAlAlexTypeShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlAlexTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlAlexTypeShouldBeFound(shouldBeFound);
        defaultAlAlexTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlAlexTypeShouldBeFound(String filter) throws Exception {
        restAlAlexTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alAlexType.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].canDoRetail").value(hasItem(DEFAULT_CAN_DO_RETAIL.booleanValue())))
            .andExpect(jsonPath("$.[*].isOrgDivision").value(hasItem(DEFAULT_IS_ORG_DIVISION.booleanValue())))
            .andExpect(jsonPath("$.[*].configJason").value(hasItem(DEFAULT_CONFIG_JASON)))
            .andExpect(jsonPath("$.[*].treeDepth").value(hasItem(DEFAULT_TREE_DEPTH)));

        // Check, that the count call also returns 1
        restAlAlexTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlAlexTypeShouldNotBeFound(String filter) throws Exception {
        restAlAlexTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlAlexTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlAlexType() throws Exception {
        // Get the alAlexType
        restAlAlexTypeMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlAlexType() throws Exception {
        // Initialize the database
        insertedAlAlexType = alAlexTypeRepository.saveAndFlush(alAlexType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alAlexType
        AlAlexType updatedAlAlexType = alAlexTypeRepository.findById(alAlexType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlAlexType are not directly saved in db
        em.detach(updatedAlAlexType);
        updatedAlAlexType
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .canDoRetail(UPDATED_CAN_DO_RETAIL)
            .isOrgDivision(UPDATED_IS_ORG_DIVISION)
            .configJason(UPDATED_CONFIG_JASON)
            .treeDepth(UPDATED_TREE_DEPTH);

        restAlAlexTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlAlexType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlAlexType))
            )
            .andExpect(status().isOk());

        // Validate the AlAlexType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlAlexTypeToMatchAllProperties(updatedAlAlexType);
    }

    @Test
    @Transactional
    void putNonExistingAlAlexType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alAlexType.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlAlexTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alAlexType.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alAlexType))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlAlexType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlAlexType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alAlexType.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlAlexTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alAlexType))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlAlexType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlAlexType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alAlexType.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlAlexTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alAlexType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlAlexType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlAlexTypeWithPatch() throws Exception {
        // Initialize the database
        insertedAlAlexType = alAlexTypeRepository.saveAndFlush(alAlexType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alAlexType using partial update
        AlAlexType partialUpdatedAlAlexType = new AlAlexType();
        partialUpdatedAlAlexType.setId(alAlexType.getId());

        partialUpdatedAlAlexType
            .canDoRetail(UPDATED_CAN_DO_RETAIL)
            .isOrgDivision(UPDATED_IS_ORG_DIVISION)
            .configJason(UPDATED_CONFIG_JASON);

        restAlAlexTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlAlexType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlAlexType))
            )
            .andExpect(status().isOk());

        // Validate the AlAlexType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlAlexTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlAlexType, alAlexType),
            getPersistedAlAlexType(alAlexType)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlAlexTypeWithPatch() throws Exception {
        // Initialize the database
        insertedAlAlexType = alAlexTypeRepository.saveAndFlush(alAlexType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alAlexType using partial update
        AlAlexType partialUpdatedAlAlexType = new AlAlexType();
        partialUpdatedAlAlexType.setId(alAlexType.getId());

        partialUpdatedAlAlexType
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .canDoRetail(UPDATED_CAN_DO_RETAIL)
            .isOrgDivision(UPDATED_IS_ORG_DIVISION)
            .configJason(UPDATED_CONFIG_JASON)
            .treeDepth(UPDATED_TREE_DEPTH);

        restAlAlexTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlAlexType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlAlexType))
            )
            .andExpect(status().isOk());

        // Validate the AlAlexType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlAlexTypeUpdatableFieldsEquals(partialUpdatedAlAlexType, getPersistedAlAlexType(partialUpdatedAlAlexType));
    }

    @Test
    @Transactional
    void patchNonExistingAlAlexType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alAlexType.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlAlexTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alAlexType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alAlexType))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlAlexType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlAlexType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alAlexType.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlAlexTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alAlexType))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlAlexType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlAlexType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alAlexType.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlAlexTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alAlexType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlAlexType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlAlexType() throws Exception {
        // Initialize the database
        insertedAlAlexType = alAlexTypeRepository.saveAndFlush(alAlexType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alAlexType
        restAlAlexTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, alAlexType.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alAlexTypeRepository.count();
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

    protected AlAlexType getPersistedAlAlexType(AlAlexType alAlexType) {
        return alAlexTypeRepository.findById(alAlexType.getId()).orElseThrow();
    }

    protected void assertPersistedAlAlexTypeToMatchAllProperties(AlAlexType expectedAlAlexType) {
        assertAlAlexTypeAllPropertiesEquals(expectedAlAlexType, getPersistedAlAlexType(expectedAlAlexType));
    }

    protected void assertPersistedAlAlexTypeToMatchUpdatableProperties(AlAlexType expectedAlAlexType) {
        assertAlAlexTypeAllUpdatablePropertiesEquals(expectedAlAlexType, getPersistedAlAlexType(expectedAlAlexType));
    }
}
