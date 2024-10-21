package ai.realworld.web.rest;

import static ai.realworld.domain.AlSherMaleAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlSherMale;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.enumeration.AlphaSourceType;
import ai.realworld.repository.AlSherMaleRepository;
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
 * Integration tests for the {@link AlSherMaleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlSherMaleResourceIT {

    private static final AlphaSourceType DEFAULT_DATA_SOURCE_MAPPING_TYPE = AlphaSourceType.COMPLETED_ORDER;
    private static final AlphaSourceType UPDATED_DATA_SOURCE_MAPPING_TYPE = AlphaSourceType.BOOKING_INFORMATION;

    private static final String DEFAULT_KEYWORD = "AAAAAAAAAA";
    private static final String UPDATED_KEYWORD = "BBBBBBBBBB";

    private static final String DEFAULT_PROPERTY = "AAAAAAAAAA";
    private static final String UPDATED_PROPERTY = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/al-sher-males";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlSherMaleRepository alSherMaleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlSherMaleMockMvc;

    private AlSherMale alSherMale;

    private AlSherMale insertedAlSherMale;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlSherMale createEntity() {
        return new AlSherMale()
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
    public static AlSherMale createUpdatedEntity() {
        return new AlSherMale()
            .dataSourceMappingType(UPDATED_DATA_SOURCE_MAPPING_TYPE)
            .keyword(UPDATED_KEYWORD)
            .property(UPDATED_PROPERTY)
            .title(UPDATED_TITLE);
    }

    @BeforeEach
    public void initTest() {
        alSherMale = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlSherMale != null) {
            alSherMaleRepository.delete(insertedAlSherMale);
            insertedAlSherMale = null;
        }
    }

    @Test
    @Transactional
    void createAlSherMale() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlSherMale
        var returnedAlSherMale = om.readValue(
            restAlSherMaleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alSherMale)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlSherMale.class
        );

        // Validate the AlSherMale in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlSherMaleUpdatableFieldsEquals(returnedAlSherMale, getPersistedAlSherMale(returnedAlSherMale));

        insertedAlSherMale = returnedAlSherMale;
    }

    @Test
    @Transactional
    void createAlSherMaleWithExistingId() throws Exception {
        // Create the AlSherMale with an existing ID
        alSherMale.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlSherMaleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alSherMale)))
            .andExpect(status().isBadRequest());

        // Validate the AlSherMale in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAlSherMales() throws Exception {
        // Initialize the database
        insertedAlSherMale = alSherMaleRepository.saveAndFlush(alSherMale);

        // Get all the alSherMaleList
        restAlSherMaleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alSherMale.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataSourceMappingType").value(hasItem(DEFAULT_DATA_SOURCE_MAPPING_TYPE.toString())))
            .andExpect(jsonPath("$.[*].keyword").value(hasItem(DEFAULT_KEYWORD)))
            .andExpect(jsonPath("$.[*].property").value(hasItem(DEFAULT_PROPERTY)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));
    }

    @Test
    @Transactional
    void getAlSherMale() throws Exception {
        // Initialize the database
        insertedAlSherMale = alSherMaleRepository.saveAndFlush(alSherMale);

        // Get the alSherMale
        restAlSherMaleMockMvc
            .perform(get(ENTITY_API_URL_ID, alSherMale.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alSherMale.getId().intValue()))
            .andExpect(jsonPath("$.dataSourceMappingType").value(DEFAULT_DATA_SOURCE_MAPPING_TYPE.toString()))
            .andExpect(jsonPath("$.keyword").value(DEFAULT_KEYWORD))
            .andExpect(jsonPath("$.property").value(DEFAULT_PROPERTY))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE));
    }

    @Test
    @Transactional
    void getAlSherMalesByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlSherMale = alSherMaleRepository.saveAndFlush(alSherMale);

        Long id = alSherMale.getId();

        defaultAlSherMaleFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlSherMaleFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlSherMaleFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlSherMalesByDataSourceMappingTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlSherMale = alSherMaleRepository.saveAndFlush(alSherMale);

        // Get all the alSherMaleList where dataSourceMappingType equals to
        defaultAlSherMaleFiltering(
            "dataSourceMappingType.equals=" + DEFAULT_DATA_SOURCE_MAPPING_TYPE,
            "dataSourceMappingType.equals=" + UPDATED_DATA_SOURCE_MAPPING_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAlSherMalesByDataSourceMappingTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlSherMale = alSherMaleRepository.saveAndFlush(alSherMale);

        // Get all the alSherMaleList where dataSourceMappingType in
        defaultAlSherMaleFiltering(
            "dataSourceMappingType.in=" + DEFAULT_DATA_SOURCE_MAPPING_TYPE + "," + UPDATED_DATA_SOURCE_MAPPING_TYPE,
            "dataSourceMappingType.in=" + UPDATED_DATA_SOURCE_MAPPING_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAlSherMalesByDataSourceMappingTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlSherMale = alSherMaleRepository.saveAndFlush(alSherMale);

        // Get all the alSherMaleList where dataSourceMappingType is not null
        defaultAlSherMaleFiltering("dataSourceMappingType.specified=true", "dataSourceMappingType.specified=false");
    }

    @Test
    @Transactional
    void getAllAlSherMalesByKeywordIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlSherMale = alSherMaleRepository.saveAndFlush(alSherMale);

        // Get all the alSherMaleList where keyword equals to
        defaultAlSherMaleFiltering("keyword.equals=" + DEFAULT_KEYWORD, "keyword.equals=" + UPDATED_KEYWORD);
    }

    @Test
    @Transactional
    void getAllAlSherMalesByKeywordIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlSherMale = alSherMaleRepository.saveAndFlush(alSherMale);

        // Get all the alSherMaleList where keyword in
        defaultAlSherMaleFiltering("keyword.in=" + DEFAULT_KEYWORD + "," + UPDATED_KEYWORD, "keyword.in=" + UPDATED_KEYWORD);
    }

    @Test
    @Transactional
    void getAllAlSherMalesByKeywordIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlSherMale = alSherMaleRepository.saveAndFlush(alSherMale);

        // Get all the alSherMaleList where keyword is not null
        defaultAlSherMaleFiltering("keyword.specified=true", "keyword.specified=false");
    }

    @Test
    @Transactional
    void getAllAlSherMalesByKeywordContainsSomething() throws Exception {
        // Initialize the database
        insertedAlSherMale = alSherMaleRepository.saveAndFlush(alSherMale);

        // Get all the alSherMaleList where keyword contains
        defaultAlSherMaleFiltering("keyword.contains=" + DEFAULT_KEYWORD, "keyword.contains=" + UPDATED_KEYWORD);
    }

    @Test
    @Transactional
    void getAllAlSherMalesByKeywordNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlSherMale = alSherMaleRepository.saveAndFlush(alSherMale);

        // Get all the alSherMaleList where keyword does not contain
        defaultAlSherMaleFiltering("keyword.doesNotContain=" + UPDATED_KEYWORD, "keyword.doesNotContain=" + DEFAULT_KEYWORD);
    }

    @Test
    @Transactional
    void getAllAlSherMalesByPropertyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlSherMale = alSherMaleRepository.saveAndFlush(alSherMale);

        // Get all the alSherMaleList where property equals to
        defaultAlSherMaleFiltering("property.equals=" + DEFAULT_PROPERTY, "property.equals=" + UPDATED_PROPERTY);
    }

    @Test
    @Transactional
    void getAllAlSherMalesByPropertyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlSherMale = alSherMaleRepository.saveAndFlush(alSherMale);

        // Get all the alSherMaleList where property in
        defaultAlSherMaleFiltering("property.in=" + DEFAULT_PROPERTY + "," + UPDATED_PROPERTY, "property.in=" + UPDATED_PROPERTY);
    }

    @Test
    @Transactional
    void getAllAlSherMalesByPropertyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlSherMale = alSherMaleRepository.saveAndFlush(alSherMale);

        // Get all the alSherMaleList where property is not null
        defaultAlSherMaleFiltering("property.specified=true", "property.specified=false");
    }

    @Test
    @Transactional
    void getAllAlSherMalesByPropertyContainsSomething() throws Exception {
        // Initialize the database
        insertedAlSherMale = alSherMaleRepository.saveAndFlush(alSherMale);

        // Get all the alSherMaleList where property contains
        defaultAlSherMaleFiltering("property.contains=" + DEFAULT_PROPERTY, "property.contains=" + UPDATED_PROPERTY);
    }

    @Test
    @Transactional
    void getAllAlSherMalesByPropertyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlSherMale = alSherMaleRepository.saveAndFlush(alSherMale);

        // Get all the alSherMaleList where property does not contain
        defaultAlSherMaleFiltering("property.doesNotContain=" + UPDATED_PROPERTY, "property.doesNotContain=" + DEFAULT_PROPERTY);
    }

    @Test
    @Transactional
    void getAllAlSherMalesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlSherMale = alSherMaleRepository.saveAndFlush(alSherMale);

        // Get all the alSherMaleList where title equals to
        defaultAlSherMaleFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAlSherMalesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlSherMale = alSherMaleRepository.saveAndFlush(alSherMale);

        // Get all the alSherMaleList where title in
        defaultAlSherMaleFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAlSherMalesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlSherMale = alSherMaleRepository.saveAndFlush(alSherMale);

        // Get all the alSherMaleList where title is not null
        defaultAlSherMaleFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllAlSherMalesByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedAlSherMale = alSherMaleRepository.saveAndFlush(alSherMale);

        // Get all the alSherMaleList where title contains
        defaultAlSherMaleFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAlSherMalesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlSherMale = alSherMaleRepository.saveAndFlush(alSherMale);

        // Get all the alSherMaleList where title does not contain
        defaultAlSherMaleFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllAlSherMalesByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alSherMaleRepository.saveAndFlush(alSherMale);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alSherMale.setApplication(application);
        alSherMaleRepository.saveAndFlush(alSherMale);
        UUID applicationId = application.getId();
        // Get all the alSherMaleList where application equals to applicationId
        defaultAlSherMaleShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alSherMaleList where application equals to UUID.randomUUID()
        defaultAlSherMaleShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlSherMaleFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlSherMaleShouldBeFound(shouldBeFound);
        defaultAlSherMaleShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlSherMaleShouldBeFound(String filter) throws Exception {
        restAlSherMaleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alSherMale.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataSourceMappingType").value(hasItem(DEFAULT_DATA_SOURCE_MAPPING_TYPE.toString())))
            .andExpect(jsonPath("$.[*].keyword").value(hasItem(DEFAULT_KEYWORD)))
            .andExpect(jsonPath("$.[*].property").value(hasItem(DEFAULT_PROPERTY)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));

        // Check, that the count call also returns 1
        restAlSherMaleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlSherMaleShouldNotBeFound(String filter) throws Exception {
        restAlSherMaleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlSherMaleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlSherMale() throws Exception {
        // Get the alSherMale
        restAlSherMaleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlSherMale() throws Exception {
        // Initialize the database
        insertedAlSherMale = alSherMaleRepository.saveAndFlush(alSherMale);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alSherMale
        AlSherMale updatedAlSherMale = alSherMaleRepository.findById(alSherMale.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlSherMale are not directly saved in db
        em.detach(updatedAlSherMale);
        updatedAlSherMale
            .dataSourceMappingType(UPDATED_DATA_SOURCE_MAPPING_TYPE)
            .keyword(UPDATED_KEYWORD)
            .property(UPDATED_PROPERTY)
            .title(UPDATED_TITLE);

        restAlSherMaleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlSherMale.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlSherMale))
            )
            .andExpect(status().isOk());

        // Validate the AlSherMale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlSherMaleToMatchAllProperties(updatedAlSherMale);
    }

    @Test
    @Transactional
    void putNonExistingAlSherMale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alSherMale.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlSherMaleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alSherMale.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alSherMale))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlSherMale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlSherMale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alSherMale.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlSherMaleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alSherMale))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlSherMale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlSherMale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alSherMale.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlSherMaleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alSherMale)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlSherMale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlSherMaleWithPatch() throws Exception {
        // Initialize the database
        insertedAlSherMale = alSherMaleRepository.saveAndFlush(alSherMale);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alSherMale using partial update
        AlSherMale partialUpdatedAlSherMale = new AlSherMale();
        partialUpdatedAlSherMale.setId(alSherMale.getId());

        partialUpdatedAlSherMale.property(UPDATED_PROPERTY).title(UPDATED_TITLE);

        restAlSherMaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlSherMale.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlSherMale))
            )
            .andExpect(status().isOk());

        // Validate the AlSherMale in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlSherMaleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlSherMale, alSherMale),
            getPersistedAlSherMale(alSherMale)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlSherMaleWithPatch() throws Exception {
        // Initialize the database
        insertedAlSherMale = alSherMaleRepository.saveAndFlush(alSherMale);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alSherMale using partial update
        AlSherMale partialUpdatedAlSherMale = new AlSherMale();
        partialUpdatedAlSherMale.setId(alSherMale.getId());

        partialUpdatedAlSherMale
            .dataSourceMappingType(UPDATED_DATA_SOURCE_MAPPING_TYPE)
            .keyword(UPDATED_KEYWORD)
            .property(UPDATED_PROPERTY)
            .title(UPDATED_TITLE);

        restAlSherMaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlSherMale.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlSherMale))
            )
            .andExpect(status().isOk());

        // Validate the AlSherMale in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlSherMaleUpdatableFieldsEquals(partialUpdatedAlSherMale, getPersistedAlSherMale(partialUpdatedAlSherMale));
    }

    @Test
    @Transactional
    void patchNonExistingAlSherMale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alSherMale.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlSherMaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alSherMale.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alSherMale))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlSherMale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlSherMale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alSherMale.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlSherMaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alSherMale))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlSherMale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlSherMale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alSherMale.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlSherMaleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alSherMale)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlSherMale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlSherMale() throws Exception {
        // Initialize the database
        insertedAlSherMale = alSherMaleRepository.saveAndFlush(alSherMale);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alSherMale
        restAlSherMaleMockMvc
            .perform(delete(ENTITY_API_URL_ID, alSherMale.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alSherMaleRepository.count();
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

    protected AlSherMale getPersistedAlSherMale(AlSherMale alSherMale) {
        return alSherMaleRepository.findById(alSherMale.getId()).orElseThrow();
    }

    protected void assertPersistedAlSherMaleToMatchAllProperties(AlSherMale expectedAlSherMale) {
        assertAlSherMaleAllPropertiesEquals(expectedAlSherMale, getPersistedAlSherMale(expectedAlSherMale));
    }

    protected void assertPersistedAlSherMaleToMatchUpdatableProperties(AlSherMale expectedAlSherMale) {
        assertAlSherMaleAllUpdatablePropertiesEquals(expectedAlSherMale, getPersistedAlSherMale(expectedAlSherMale));
    }
}
