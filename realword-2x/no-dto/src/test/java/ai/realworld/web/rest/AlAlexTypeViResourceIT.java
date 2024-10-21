package ai.realworld.web.rest;

import static ai.realworld.domain.AlAlexTypeViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlAlexTypeVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.repository.AlAlexTypeViRepository;
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
 * Integration tests for the {@link AlAlexTypeViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlAlexTypeViResourceIT {

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

    private static final String ENTITY_API_URL = "/api/al-alex-type-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlAlexTypeViRepository alAlexTypeViRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlAlexTypeViMockMvc;

    private AlAlexTypeVi alAlexTypeVi;

    private AlAlexTypeVi insertedAlAlexTypeVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlAlexTypeVi createEntity() {
        return new AlAlexTypeVi()
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
    public static AlAlexTypeVi createUpdatedEntity() {
        return new AlAlexTypeVi()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .canDoRetail(UPDATED_CAN_DO_RETAIL)
            .isOrgDivision(UPDATED_IS_ORG_DIVISION)
            .configJason(UPDATED_CONFIG_JASON)
            .treeDepth(UPDATED_TREE_DEPTH);
    }

    @BeforeEach
    public void initTest() {
        alAlexTypeVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlAlexTypeVi != null) {
            alAlexTypeViRepository.delete(insertedAlAlexTypeVi);
            insertedAlAlexTypeVi = null;
        }
    }

    @Test
    @Transactional
    void createAlAlexTypeVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlAlexTypeVi
        var returnedAlAlexTypeVi = om.readValue(
            restAlAlexTypeViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alAlexTypeVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlAlexTypeVi.class
        );

        // Validate the AlAlexTypeVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlAlexTypeViUpdatableFieldsEquals(returnedAlAlexTypeVi, getPersistedAlAlexTypeVi(returnedAlAlexTypeVi));

        insertedAlAlexTypeVi = returnedAlAlexTypeVi;
    }

    @Test
    @Transactional
    void createAlAlexTypeViWithExistingId() throws Exception {
        // Create the AlAlexTypeVi with an existing ID
        insertedAlAlexTypeVi = alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlAlexTypeViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alAlexTypeVi)))
            .andExpect(status().isBadRequest());

        // Validate the AlAlexTypeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alAlexTypeVi.setName(null);

        // Create the AlAlexTypeVi, which fails.

        restAlAlexTypeViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alAlexTypeVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlAlexTypeVis() throws Exception {
        // Initialize the database
        insertedAlAlexTypeVi = alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);

        // Get all the alAlexTypeViList
        restAlAlexTypeViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alAlexTypeVi.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].canDoRetail").value(hasItem(DEFAULT_CAN_DO_RETAIL.booleanValue())))
            .andExpect(jsonPath("$.[*].isOrgDivision").value(hasItem(DEFAULT_IS_ORG_DIVISION.booleanValue())))
            .andExpect(jsonPath("$.[*].configJason").value(hasItem(DEFAULT_CONFIG_JASON)))
            .andExpect(jsonPath("$.[*].treeDepth").value(hasItem(DEFAULT_TREE_DEPTH)));
    }

    @Test
    @Transactional
    void getAlAlexTypeVi() throws Exception {
        // Initialize the database
        insertedAlAlexTypeVi = alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);

        // Get the alAlexTypeVi
        restAlAlexTypeViMockMvc
            .perform(get(ENTITY_API_URL_ID, alAlexTypeVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alAlexTypeVi.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.canDoRetail").value(DEFAULT_CAN_DO_RETAIL.booleanValue()))
            .andExpect(jsonPath("$.isOrgDivision").value(DEFAULT_IS_ORG_DIVISION.booleanValue()))
            .andExpect(jsonPath("$.configJason").value(DEFAULT_CONFIG_JASON))
            .andExpect(jsonPath("$.treeDepth").value(DEFAULT_TREE_DEPTH));
    }

    @Test
    @Transactional
    void getAlAlexTypeVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlAlexTypeVi = alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);

        UUID id = alAlexTypeVi.getId();

        defaultAlAlexTypeViFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllAlAlexTypeVisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlAlexTypeVi = alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);

        // Get all the alAlexTypeViList where name equals to
        defaultAlAlexTypeViFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlAlexTypeVisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlAlexTypeVi = alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);

        // Get all the alAlexTypeViList where name in
        defaultAlAlexTypeViFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlAlexTypeVisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlAlexTypeVi = alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);

        // Get all the alAlexTypeViList where name is not null
        defaultAlAlexTypeViFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAlAlexTypeVisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlAlexTypeVi = alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);

        // Get all the alAlexTypeViList where name contains
        defaultAlAlexTypeViFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlAlexTypeVisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlAlexTypeVi = alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);

        // Get all the alAlexTypeViList where name does not contain
        defaultAlAlexTypeViFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAlAlexTypeVisByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlAlexTypeVi = alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);

        // Get all the alAlexTypeViList where description equals to
        defaultAlAlexTypeViFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAlAlexTypeVisByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlAlexTypeVi = alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);

        // Get all the alAlexTypeViList where description in
        defaultAlAlexTypeViFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllAlAlexTypeVisByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlAlexTypeVi = alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);

        // Get all the alAlexTypeViList where description is not null
        defaultAlAlexTypeViFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllAlAlexTypeVisByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedAlAlexTypeVi = alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);

        // Get all the alAlexTypeViList where description contains
        defaultAlAlexTypeViFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAlAlexTypeVisByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlAlexTypeVi = alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);

        // Get all the alAlexTypeViList where description does not contain
        defaultAlAlexTypeViFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllAlAlexTypeVisByCanDoRetailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlAlexTypeVi = alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);

        // Get all the alAlexTypeViList where canDoRetail equals to
        defaultAlAlexTypeViFiltering("canDoRetail.equals=" + DEFAULT_CAN_DO_RETAIL, "canDoRetail.equals=" + UPDATED_CAN_DO_RETAIL);
    }

    @Test
    @Transactional
    void getAllAlAlexTypeVisByCanDoRetailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlAlexTypeVi = alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);

        // Get all the alAlexTypeViList where canDoRetail in
        defaultAlAlexTypeViFiltering(
            "canDoRetail.in=" + DEFAULT_CAN_DO_RETAIL + "," + UPDATED_CAN_DO_RETAIL,
            "canDoRetail.in=" + UPDATED_CAN_DO_RETAIL
        );
    }

    @Test
    @Transactional
    void getAllAlAlexTypeVisByCanDoRetailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlAlexTypeVi = alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);

        // Get all the alAlexTypeViList where canDoRetail is not null
        defaultAlAlexTypeViFiltering("canDoRetail.specified=true", "canDoRetail.specified=false");
    }

    @Test
    @Transactional
    void getAllAlAlexTypeVisByIsOrgDivisionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlAlexTypeVi = alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);

        // Get all the alAlexTypeViList where isOrgDivision equals to
        defaultAlAlexTypeViFiltering("isOrgDivision.equals=" + DEFAULT_IS_ORG_DIVISION, "isOrgDivision.equals=" + UPDATED_IS_ORG_DIVISION);
    }

    @Test
    @Transactional
    void getAllAlAlexTypeVisByIsOrgDivisionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlAlexTypeVi = alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);

        // Get all the alAlexTypeViList where isOrgDivision in
        defaultAlAlexTypeViFiltering(
            "isOrgDivision.in=" + DEFAULT_IS_ORG_DIVISION + "," + UPDATED_IS_ORG_DIVISION,
            "isOrgDivision.in=" + UPDATED_IS_ORG_DIVISION
        );
    }

    @Test
    @Transactional
    void getAllAlAlexTypeVisByIsOrgDivisionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlAlexTypeVi = alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);

        // Get all the alAlexTypeViList where isOrgDivision is not null
        defaultAlAlexTypeViFiltering("isOrgDivision.specified=true", "isOrgDivision.specified=false");
    }

    @Test
    @Transactional
    void getAllAlAlexTypeVisByConfigJasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlAlexTypeVi = alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);

        // Get all the alAlexTypeViList where configJason equals to
        defaultAlAlexTypeViFiltering("configJason.equals=" + DEFAULT_CONFIG_JASON, "configJason.equals=" + UPDATED_CONFIG_JASON);
    }

    @Test
    @Transactional
    void getAllAlAlexTypeVisByConfigJasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlAlexTypeVi = alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);

        // Get all the alAlexTypeViList where configJason in
        defaultAlAlexTypeViFiltering(
            "configJason.in=" + DEFAULT_CONFIG_JASON + "," + UPDATED_CONFIG_JASON,
            "configJason.in=" + UPDATED_CONFIG_JASON
        );
    }

    @Test
    @Transactional
    void getAllAlAlexTypeVisByConfigJasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlAlexTypeVi = alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);

        // Get all the alAlexTypeViList where configJason is not null
        defaultAlAlexTypeViFiltering("configJason.specified=true", "configJason.specified=false");
    }

    @Test
    @Transactional
    void getAllAlAlexTypeVisByConfigJasonContainsSomething() throws Exception {
        // Initialize the database
        insertedAlAlexTypeVi = alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);

        // Get all the alAlexTypeViList where configJason contains
        defaultAlAlexTypeViFiltering("configJason.contains=" + DEFAULT_CONFIG_JASON, "configJason.contains=" + UPDATED_CONFIG_JASON);
    }

    @Test
    @Transactional
    void getAllAlAlexTypeVisByConfigJasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlAlexTypeVi = alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);

        // Get all the alAlexTypeViList where configJason does not contain
        defaultAlAlexTypeViFiltering(
            "configJason.doesNotContain=" + UPDATED_CONFIG_JASON,
            "configJason.doesNotContain=" + DEFAULT_CONFIG_JASON
        );
    }

    @Test
    @Transactional
    void getAllAlAlexTypeVisByTreeDepthIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlAlexTypeVi = alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);

        // Get all the alAlexTypeViList where treeDepth equals to
        defaultAlAlexTypeViFiltering("treeDepth.equals=" + DEFAULT_TREE_DEPTH, "treeDepth.equals=" + UPDATED_TREE_DEPTH);
    }

    @Test
    @Transactional
    void getAllAlAlexTypeVisByTreeDepthIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlAlexTypeVi = alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);

        // Get all the alAlexTypeViList where treeDepth in
        defaultAlAlexTypeViFiltering("treeDepth.in=" + DEFAULT_TREE_DEPTH + "," + UPDATED_TREE_DEPTH, "treeDepth.in=" + UPDATED_TREE_DEPTH);
    }

    @Test
    @Transactional
    void getAllAlAlexTypeVisByTreeDepthIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlAlexTypeVi = alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);

        // Get all the alAlexTypeViList where treeDepth is not null
        defaultAlAlexTypeViFiltering("treeDepth.specified=true", "treeDepth.specified=false");
    }

    @Test
    @Transactional
    void getAllAlAlexTypeVisByTreeDepthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlAlexTypeVi = alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);

        // Get all the alAlexTypeViList where treeDepth is greater than or equal to
        defaultAlAlexTypeViFiltering(
            "treeDepth.greaterThanOrEqual=" + DEFAULT_TREE_DEPTH,
            "treeDepth.greaterThanOrEqual=" + UPDATED_TREE_DEPTH
        );
    }

    @Test
    @Transactional
    void getAllAlAlexTypeVisByTreeDepthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlAlexTypeVi = alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);

        // Get all the alAlexTypeViList where treeDepth is less than or equal to
        defaultAlAlexTypeViFiltering("treeDepth.lessThanOrEqual=" + DEFAULT_TREE_DEPTH, "treeDepth.lessThanOrEqual=" + SMALLER_TREE_DEPTH);
    }

    @Test
    @Transactional
    void getAllAlAlexTypeVisByTreeDepthIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlAlexTypeVi = alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);

        // Get all the alAlexTypeViList where treeDepth is less than
        defaultAlAlexTypeViFiltering("treeDepth.lessThan=" + UPDATED_TREE_DEPTH, "treeDepth.lessThan=" + DEFAULT_TREE_DEPTH);
    }

    @Test
    @Transactional
    void getAllAlAlexTypeVisByTreeDepthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlAlexTypeVi = alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);

        // Get all the alAlexTypeViList where treeDepth is greater than
        defaultAlAlexTypeViFiltering("treeDepth.greaterThan=" + SMALLER_TREE_DEPTH, "treeDepth.greaterThan=" + DEFAULT_TREE_DEPTH);
    }

    @Test
    @Transactional
    void getAllAlAlexTypeVisByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alAlexTypeVi.setApplication(application);
        alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);
        UUID applicationId = application.getId();
        // Get all the alAlexTypeViList where application equals to applicationId
        defaultAlAlexTypeViShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alAlexTypeViList where application equals to UUID.randomUUID()
        defaultAlAlexTypeViShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlAlexTypeViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlAlexTypeViShouldBeFound(shouldBeFound);
        defaultAlAlexTypeViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlAlexTypeViShouldBeFound(String filter) throws Exception {
        restAlAlexTypeViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alAlexTypeVi.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].canDoRetail").value(hasItem(DEFAULT_CAN_DO_RETAIL.booleanValue())))
            .andExpect(jsonPath("$.[*].isOrgDivision").value(hasItem(DEFAULT_IS_ORG_DIVISION.booleanValue())))
            .andExpect(jsonPath("$.[*].configJason").value(hasItem(DEFAULT_CONFIG_JASON)))
            .andExpect(jsonPath("$.[*].treeDepth").value(hasItem(DEFAULT_TREE_DEPTH)));

        // Check, that the count call also returns 1
        restAlAlexTypeViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlAlexTypeViShouldNotBeFound(String filter) throws Exception {
        restAlAlexTypeViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlAlexTypeViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlAlexTypeVi() throws Exception {
        // Get the alAlexTypeVi
        restAlAlexTypeViMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlAlexTypeVi() throws Exception {
        // Initialize the database
        insertedAlAlexTypeVi = alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alAlexTypeVi
        AlAlexTypeVi updatedAlAlexTypeVi = alAlexTypeViRepository.findById(alAlexTypeVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlAlexTypeVi are not directly saved in db
        em.detach(updatedAlAlexTypeVi);
        updatedAlAlexTypeVi
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .canDoRetail(UPDATED_CAN_DO_RETAIL)
            .isOrgDivision(UPDATED_IS_ORG_DIVISION)
            .configJason(UPDATED_CONFIG_JASON)
            .treeDepth(UPDATED_TREE_DEPTH);

        restAlAlexTypeViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlAlexTypeVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlAlexTypeVi))
            )
            .andExpect(status().isOk());

        // Validate the AlAlexTypeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlAlexTypeViToMatchAllProperties(updatedAlAlexTypeVi);
    }

    @Test
    @Transactional
    void putNonExistingAlAlexTypeVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alAlexTypeVi.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlAlexTypeViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alAlexTypeVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alAlexTypeVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlAlexTypeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlAlexTypeVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alAlexTypeVi.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlAlexTypeViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alAlexTypeVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlAlexTypeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlAlexTypeVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alAlexTypeVi.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlAlexTypeViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alAlexTypeVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlAlexTypeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlAlexTypeViWithPatch() throws Exception {
        // Initialize the database
        insertedAlAlexTypeVi = alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alAlexTypeVi using partial update
        AlAlexTypeVi partialUpdatedAlAlexTypeVi = new AlAlexTypeVi();
        partialUpdatedAlAlexTypeVi.setId(alAlexTypeVi.getId());

        partialUpdatedAlAlexTypeVi.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).configJason(UPDATED_CONFIG_JASON);

        restAlAlexTypeViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlAlexTypeVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlAlexTypeVi))
            )
            .andExpect(status().isOk());

        // Validate the AlAlexTypeVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlAlexTypeViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlAlexTypeVi, alAlexTypeVi),
            getPersistedAlAlexTypeVi(alAlexTypeVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlAlexTypeViWithPatch() throws Exception {
        // Initialize the database
        insertedAlAlexTypeVi = alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alAlexTypeVi using partial update
        AlAlexTypeVi partialUpdatedAlAlexTypeVi = new AlAlexTypeVi();
        partialUpdatedAlAlexTypeVi.setId(alAlexTypeVi.getId());

        partialUpdatedAlAlexTypeVi
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .canDoRetail(UPDATED_CAN_DO_RETAIL)
            .isOrgDivision(UPDATED_IS_ORG_DIVISION)
            .configJason(UPDATED_CONFIG_JASON)
            .treeDepth(UPDATED_TREE_DEPTH);

        restAlAlexTypeViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlAlexTypeVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlAlexTypeVi))
            )
            .andExpect(status().isOk());

        // Validate the AlAlexTypeVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlAlexTypeViUpdatableFieldsEquals(partialUpdatedAlAlexTypeVi, getPersistedAlAlexTypeVi(partialUpdatedAlAlexTypeVi));
    }

    @Test
    @Transactional
    void patchNonExistingAlAlexTypeVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alAlexTypeVi.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlAlexTypeViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alAlexTypeVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alAlexTypeVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlAlexTypeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlAlexTypeVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alAlexTypeVi.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlAlexTypeViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alAlexTypeVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlAlexTypeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlAlexTypeVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alAlexTypeVi.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlAlexTypeViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alAlexTypeVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlAlexTypeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlAlexTypeVi() throws Exception {
        // Initialize the database
        insertedAlAlexTypeVi = alAlexTypeViRepository.saveAndFlush(alAlexTypeVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alAlexTypeVi
        restAlAlexTypeViMockMvc
            .perform(delete(ENTITY_API_URL_ID, alAlexTypeVi.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alAlexTypeViRepository.count();
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

    protected AlAlexTypeVi getPersistedAlAlexTypeVi(AlAlexTypeVi alAlexTypeVi) {
        return alAlexTypeViRepository.findById(alAlexTypeVi.getId()).orElseThrow();
    }

    protected void assertPersistedAlAlexTypeViToMatchAllProperties(AlAlexTypeVi expectedAlAlexTypeVi) {
        assertAlAlexTypeViAllPropertiesEquals(expectedAlAlexTypeVi, getPersistedAlAlexTypeVi(expectedAlAlexTypeVi));
    }

    protected void assertPersistedAlAlexTypeViToMatchUpdatableProperties(AlAlexTypeVi expectedAlAlexTypeVi) {
        assertAlAlexTypeViAllUpdatablePropertiesEquals(expectedAlAlexTypeVi, getPersistedAlAlexTypeVi(expectedAlAlexTypeVi));
    }
}
