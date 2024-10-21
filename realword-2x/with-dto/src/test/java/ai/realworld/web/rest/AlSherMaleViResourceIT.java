package ai.realworld.web.rest;

import static ai.realworld.domain.AlSherMaleViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlSherMaleVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.enumeration.AlphaSourceType;
import ai.realworld.repository.AlSherMaleViRepository;
import ai.realworld.service.dto.AlSherMaleViDTO;
import ai.realworld.service.mapper.AlSherMaleViMapper;
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
 * Integration tests for the {@link AlSherMaleViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlSherMaleViResourceIT {

    private static final AlphaSourceType DEFAULT_DATA_SOURCE_MAPPING_TYPE = AlphaSourceType.COMPLETED_ORDER;
    private static final AlphaSourceType UPDATED_DATA_SOURCE_MAPPING_TYPE = AlphaSourceType.BOOKING_INFORMATION;

    private static final String DEFAULT_KEYWORD = "AAAAAAAAAA";
    private static final String UPDATED_KEYWORD = "BBBBBBBBBB";

    private static final String DEFAULT_PROPERTY = "AAAAAAAAAA";
    private static final String UPDATED_PROPERTY = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/al-sher-male-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlSherMaleViRepository alSherMaleViRepository;

    @Autowired
    private AlSherMaleViMapper alSherMaleViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlSherMaleViMockMvc;

    private AlSherMaleVi alSherMaleVi;

    private AlSherMaleVi insertedAlSherMaleVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlSherMaleVi createEntity() {
        return new AlSherMaleVi()
            .dataSourceMappingType(DEFAULT_DATA_SOURCE_MAPPING_TYPE)
            .keyword(DEFAULT_KEYWORD)
            .property(DEFAULT_PROPERTY)
            .title(DEFAULT_TITLE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlSherMaleVi createUpdatedEntity() {
        return new AlSherMaleVi()
            .dataSourceMappingType(UPDATED_DATA_SOURCE_MAPPING_TYPE)
            .keyword(UPDATED_KEYWORD)
            .property(UPDATED_PROPERTY)
            .title(UPDATED_TITLE);
    }

    @BeforeEach
    public void initTest() {
        alSherMaleVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlSherMaleVi != null) {
            alSherMaleViRepository.delete(insertedAlSherMaleVi);
            insertedAlSherMaleVi = null;
        }
    }

    @Test
    @Transactional
    void createAlSherMaleVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlSherMaleVi
        AlSherMaleViDTO alSherMaleViDTO = alSherMaleViMapper.toDto(alSherMaleVi);
        var returnedAlSherMaleViDTO = om.readValue(
            restAlSherMaleViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alSherMaleViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlSherMaleViDTO.class
        );

        // Validate the AlSherMaleVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlSherMaleVi = alSherMaleViMapper.toEntity(returnedAlSherMaleViDTO);
        assertAlSherMaleViUpdatableFieldsEquals(returnedAlSherMaleVi, getPersistedAlSherMaleVi(returnedAlSherMaleVi));

        insertedAlSherMaleVi = returnedAlSherMaleVi;
    }

    @Test
    @Transactional
    void createAlSherMaleViWithExistingId() throws Exception {
        // Create the AlSherMaleVi with an existing ID
        alSherMaleVi.setId(1L);
        AlSherMaleViDTO alSherMaleViDTO = alSherMaleViMapper.toDto(alSherMaleVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlSherMaleViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alSherMaleViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlSherMaleVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAlSherMaleVis() throws Exception {
        // Initialize the database
        insertedAlSherMaleVi = alSherMaleViRepository.saveAndFlush(alSherMaleVi);

        // Get all the alSherMaleViList
        restAlSherMaleViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alSherMaleVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataSourceMappingType").value(hasItem(DEFAULT_DATA_SOURCE_MAPPING_TYPE.toString())))
            .andExpect(jsonPath("$.[*].keyword").value(hasItem(DEFAULT_KEYWORD)))
            .andExpect(jsonPath("$.[*].property").value(hasItem(DEFAULT_PROPERTY)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));
    }

    @Test
    @Transactional
    void getAlSherMaleVi() throws Exception {
        // Initialize the database
        insertedAlSherMaleVi = alSherMaleViRepository.saveAndFlush(alSherMaleVi);

        // Get the alSherMaleVi
        restAlSherMaleViMockMvc
            .perform(get(ENTITY_API_URL_ID, alSherMaleVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alSherMaleVi.getId().intValue()))
            .andExpect(jsonPath("$.dataSourceMappingType").value(DEFAULT_DATA_SOURCE_MAPPING_TYPE.toString()))
            .andExpect(jsonPath("$.keyword").value(DEFAULT_KEYWORD))
            .andExpect(jsonPath("$.property").value(DEFAULT_PROPERTY))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE));
    }

    @Test
    @Transactional
    void getAlSherMaleVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlSherMaleVi = alSherMaleViRepository.saveAndFlush(alSherMaleVi);

        Long id = alSherMaleVi.getId();

        defaultAlSherMaleViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlSherMaleViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlSherMaleViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlSherMaleVisByDataSourceMappingTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlSherMaleVi = alSherMaleViRepository.saveAndFlush(alSherMaleVi);

        // Get all the alSherMaleViList where dataSourceMappingType equals to
        defaultAlSherMaleViFiltering(
            "dataSourceMappingType.equals=" + DEFAULT_DATA_SOURCE_MAPPING_TYPE,
            "dataSourceMappingType.equals=" + UPDATED_DATA_SOURCE_MAPPING_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAlSherMaleVisByDataSourceMappingTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlSherMaleVi = alSherMaleViRepository.saveAndFlush(alSherMaleVi);

        // Get all the alSherMaleViList where dataSourceMappingType in
        defaultAlSherMaleViFiltering(
            "dataSourceMappingType.in=" + DEFAULT_DATA_SOURCE_MAPPING_TYPE + "," + UPDATED_DATA_SOURCE_MAPPING_TYPE,
            "dataSourceMappingType.in=" + UPDATED_DATA_SOURCE_MAPPING_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAlSherMaleVisByDataSourceMappingTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlSherMaleVi = alSherMaleViRepository.saveAndFlush(alSherMaleVi);

        // Get all the alSherMaleViList where dataSourceMappingType is not null
        defaultAlSherMaleViFiltering("dataSourceMappingType.specified=true", "dataSourceMappingType.specified=false");
    }

    @Test
    @Transactional
    void getAllAlSherMaleVisByKeywordIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlSherMaleVi = alSherMaleViRepository.saveAndFlush(alSherMaleVi);

        // Get all the alSherMaleViList where keyword equals to
        defaultAlSherMaleViFiltering("keyword.equals=" + DEFAULT_KEYWORD, "keyword.equals=" + UPDATED_KEYWORD);
    }

    @Test
    @Transactional
    void getAllAlSherMaleVisByKeywordIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlSherMaleVi = alSherMaleViRepository.saveAndFlush(alSherMaleVi);

        // Get all the alSherMaleViList where keyword in
        defaultAlSherMaleViFiltering("keyword.in=" + DEFAULT_KEYWORD + "," + UPDATED_KEYWORD, "keyword.in=" + UPDATED_KEYWORD);
    }

    @Test
    @Transactional
    void getAllAlSherMaleVisByKeywordIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlSherMaleVi = alSherMaleViRepository.saveAndFlush(alSherMaleVi);

        // Get all the alSherMaleViList where keyword is not null
        defaultAlSherMaleViFiltering("keyword.specified=true", "keyword.specified=false");
    }

    @Test
    @Transactional
    void getAllAlSherMaleVisByKeywordContainsSomething() throws Exception {
        // Initialize the database
        insertedAlSherMaleVi = alSherMaleViRepository.saveAndFlush(alSherMaleVi);

        // Get all the alSherMaleViList where keyword contains
        defaultAlSherMaleViFiltering("keyword.contains=" + DEFAULT_KEYWORD, "keyword.contains=" + UPDATED_KEYWORD);
    }

    @Test
    @Transactional
    void getAllAlSherMaleVisByKeywordNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlSherMaleVi = alSherMaleViRepository.saveAndFlush(alSherMaleVi);

        // Get all the alSherMaleViList where keyword does not contain
        defaultAlSherMaleViFiltering("keyword.doesNotContain=" + UPDATED_KEYWORD, "keyword.doesNotContain=" + DEFAULT_KEYWORD);
    }

    @Test
    @Transactional
    void getAllAlSherMaleVisByPropertyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlSherMaleVi = alSherMaleViRepository.saveAndFlush(alSherMaleVi);

        // Get all the alSherMaleViList where property equals to
        defaultAlSherMaleViFiltering("property.equals=" + DEFAULT_PROPERTY, "property.equals=" + UPDATED_PROPERTY);
    }

    @Test
    @Transactional
    void getAllAlSherMaleVisByPropertyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlSherMaleVi = alSherMaleViRepository.saveAndFlush(alSherMaleVi);

        // Get all the alSherMaleViList where property in
        defaultAlSherMaleViFiltering("property.in=" + DEFAULT_PROPERTY + "," + UPDATED_PROPERTY, "property.in=" + UPDATED_PROPERTY);
    }

    @Test
    @Transactional
    void getAllAlSherMaleVisByPropertyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlSherMaleVi = alSherMaleViRepository.saveAndFlush(alSherMaleVi);

        // Get all the alSherMaleViList where property is not null
        defaultAlSherMaleViFiltering("property.specified=true", "property.specified=false");
    }

    @Test
    @Transactional
    void getAllAlSherMaleVisByPropertyContainsSomething() throws Exception {
        // Initialize the database
        insertedAlSherMaleVi = alSherMaleViRepository.saveAndFlush(alSherMaleVi);

        // Get all the alSherMaleViList where property contains
        defaultAlSherMaleViFiltering("property.contains=" + DEFAULT_PROPERTY, "property.contains=" + UPDATED_PROPERTY);
    }

    @Test
    @Transactional
    void getAllAlSherMaleVisByPropertyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlSherMaleVi = alSherMaleViRepository.saveAndFlush(alSherMaleVi);

        // Get all the alSherMaleViList where property does not contain
        defaultAlSherMaleViFiltering("property.doesNotContain=" + UPDATED_PROPERTY, "property.doesNotContain=" + DEFAULT_PROPERTY);
    }

    @Test
    @Transactional
    void getAllAlSherMaleVisByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlSherMaleVi = alSherMaleViRepository.saveAndFlush(alSherMaleVi);

        // Get all the alSherMaleViList where title equals to
        defaultAlSherMaleViFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAlSherMaleVisByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlSherMaleVi = alSherMaleViRepository.saveAndFlush(alSherMaleVi);

        // Get all the alSherMaleViList where title in
        defaultAlSherMaleViFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAlSherMaleVisByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlSherMaleVi = alSherMaleViRepository.saveAndFlush(alSherMaleVi);

        // Get all the alSherMaleViList where title is not null
        defaultAlSherMaleViFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllAlSherMaleVisByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedAlSherMaleVi = alSherMaleViRepository.saveAndFlush(alSherMaleVi);

        // Get all the alSherMaleViList where title contains
        defaultAlSherMaleViFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAlSherMaleVisByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlSherMaleVi = alSherMaleViRepository.saveAndFlush(alSherMaleVi);

        // Get all the alSherMaleViList where title does not contain
        defaultAlSherMaleViFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllAlSherMaleVisByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alSherMaleViRepository.saveAndFlush(alSherMaleVi);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alSherMaleVi.setApplication(application);
        alSherMaleViRepository.saveAndFlush(alSherMaleVi);
        UUID applicationId = application.getId();
        // Get all the alSherMaleViList where application equals to applicationId
        defaultAlSherMaleViShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alSherMaleViList where application equals to UUID.randomUUID()
        defaultAlSherMaleViShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlSherMaleViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlSherMaleViShouldBeFound(shouldBeFound);
        defaultAlSherMaleViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlSherMaleViShouldBeFound(String filter) throws Exception {
        restAlSherMaleViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alSherMaleVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataSourceMappingType").value(hasItem(DEFAULT_DATA_SOURCE_MAPPING_TYPE.toString())))
            .andExpect(jsonPath("$.[*].keyword").value(hasItem(DEFAULT_KEYWORD)))
            .andExpect(jsonPath("$.[*].property").value(hasItem(DEFAULT_PROPERTY)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));

        // Check, that the count call also returns 1
        restAlSherMaleViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlSherMaleViShouldNotBeFound(String filter) throws Exception {
        restAlSherMaleViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlSherMaleViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlSherMaleVi() throws Exception {
        // Get the alSherMaleVi
        restAlSherMaleViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlSherMaleVi() throws Exception {
        // Initialize the database
        insertedAlSherMaleVi = alSherMaleViRepository.saveAndFlush(alSherMaleVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alSherMaleVi
        AlSherMaleVi updatedAlSherMaleVi = alSherMaleViRepository.findById(alSherMaleVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlSherMaleVi are not directly saved in db
        em.detach(updatedAlSherMaleVi);
        updatedAlSherMaleVi
            .dataSourceMappingType(UPDATED_DATA_SOURCE_MAPPING_TYPE)
            .keyword(UPDATED_KEYWORD)
            .property(UPDATED_PROPERTY)
            .title(UPDATED_TITLE);
        AlSherMaleViDTO alSherMaleViDTO = alSherMaleViMapper.toDto(updatedAlSherMaleVi);

        restAlSherMaleViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alSherMaleViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alSherMaleViDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlSherMaleVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlSherMaleViToMatchAllProperties(updatedAlSherMaleVi);
    }

    @Test
    @Transactional
    void putNonExistingAlSherMaleVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alSherMaleVi.setId(longCount.incrementAndGet());

        // Create the AlSherMaleVi
        AlSherMaleViDTO alSherMaleViDTO = alSherMaleViMapper.toDto(alSherMaleVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlSherMaleViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alSherMaleViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alSherMaleViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlSherMaleVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlSherMaleVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alSherMaleVi.setId(longCount.incrementAndGet());

        // Create the AlSherMaleVi
        AlSherMaleViDTO alSherMaleViDTO = alSherMaleViMapper.toDto(alSherMaleVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlSherMaleViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alSherMaleViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlSherMaleVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlSherMaleVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alSherMaleVi.setId(longCount.incrementAndGet());

        // Create the AlSherMaleVi
        AlSherMaleViDTO alSherMaleViDTO = alSherMaleViMapper.toDto(alSherMaleVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlSherMaleViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alSherMaleViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlSherMaleVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlSherMaleViWithPatch() throws Exception {
        // Initialize the database
        insertedAlSherMaleVi = alSherMaleViRepository.saveAndFlush(alSherMaleVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alSherMaleVi using partial update
        AlSherMaleVi partialUpdatedAlSherMaleVi = new AlSherMaleVi();
        partialUpdatedAlSherMaleVi.setId(alSherMaleVi.getId());

        partialUpdatedAlSherMaleVi.dataSourceMappingType(UPDATED_DATA_SOURCE_MAPPING_TYPE).property(UPDATED_PROPERTY);

        restAlSherMaleViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlSherMaleVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlSherMaleVi))
            )
            .andExpect(status().isOk());

        // Validate the AlSherMaleVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlSherMaleViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlSherMaleVi, alSherMaleVi),
            getPersistedAlSherMaleVi(alSherMaleVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlSherMaleViWithPatch() throws Exception {
        // Initialize the database
        insertedAlSherMaleVi = alSherMaleViRepository.saveAndFlush(alSherMaleVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alSherMaleVi using partial update
        AlSherMaleVi partialUpdatedAlSherMaleVi = new AlSherMaleVi();
        partialUpdatedAlSherMaleVi.setId(alSherMaleVi.getId());

        partialUpdatedAlSherMaleVi
            .dataSourceMappingType(UPDATED_DATA_SOURCE_MAPPING_TYPE)
            .keyword(UPDATED_KEYWORD)
            .property(UPDATED_PROPERTY)
            .title(UPDATED_TITLE);

        restAlSherMaleViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlSherMaleVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlSherMaleVi))
            )
            .andExpect(status().isOk());

        // Validate the AlSherMaleVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlSherMaleViUpdatableFieldsEquals(partialUpdatedAlSherMaleVi, getPersistedAlSherMaleVi(partialUpdatedAlSherMaleVi));
    }

    @Test
    @Transactional
    void patchNonExistingAlSherMaleVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alSherMaleVi.setId(longCount.incrementAndGet());

        // Create the AlSherMaleVi
        AlSherMaleViDTO alSherMaleViDTO = alSherMaleViMapper.toDto(alSherMaleVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlSherMaleViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alSherMaleViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alSherMaleViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlSherMaleVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlSherMaleVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alSherMaleVi.setId(longCount.incrementAndGet());

        // Create the AlSherMaleVi
        AlSherMaleViDTO alSherMaleViDTO = alSherMaleViMapper.toDto(alSherMaleVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlSherMaleViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alSherMaleViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlSherMaleVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlSherMaleVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alSherMaleVi.setId(longCount.incrementAndGet());

        // Create the AlSherMaleVi
        AlSherMaleViDTO alSherMaleViDTO = alSherMaleViMapper.toDto(alSherMaleVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlSherMaleViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alSherMaleViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlSherMaleVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlSherMaleVi() throws Exception {
        // Initialize the database
        insertedAlSherMaleVi = alSherMaleViRepository.saveAndFlush(alSherMaleVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alSherMaleVi
        restAlSherMaleViMockMvc
            .perform(delete(ENTITY_API_URL_ID, alSherMaleVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alSherMaleViRepository.count();
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

    protected AlSherMaleVi getPersistedAlSherMaleVi(AlSherMaleVi alSherMaleVi) {
        return alSherMaleViRepository.findById(alSherMaleVi.getId()).orElseThrow();
    }

    protected void assertPersistedAlSherMaleViToMatchAllProperties(AlSherMaleVi expectedAlSherMaleVi) {
        assertAlSherMaleViAllPropertiesEquals(expectedAlSherMaleVi, getPersistedAlSherMaleVi(expectedAlSherMaleVi));
    }

    protected void assertPersistedAlSherMaleViToMatchUpdatableProperties(AlSherMaleVi expectedAlSherMaleVi) {
        assertAlSherMaleViAllUpdatablePropertiesEquals(expectedAlSherMaleVi, getPersistedAlSherMaleVi(expectedAlSherMaleVi));
    }
}
