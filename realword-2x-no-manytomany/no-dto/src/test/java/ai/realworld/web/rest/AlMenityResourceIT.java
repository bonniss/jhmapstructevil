package ai.realworld.web.rest;

import static ai.realworld.domain.AlMenityAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlMenity;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.enumeration.PeteType;
import ai.realworld.repository.AlMenityRepository;
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
 * Integration tests for the {@link AlMenityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlMenityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ICON_SVG = "AAAAAAAAAA";
    private static final String UPDATED_ICON_SVG = "BBBBBBBBBB";

    private static final PeteType DEFAULT_PROPERTY_TYPE = PeteType.VILLA;
    private static final PeteType UPDATED_PROPERTY_TYPE = PeteType.ROOM;

    private static final String ENTITY_API_URL = "/api/al-menities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlMenityRepository alMenityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlMenityMockMvc;

    private AlMenity alMenity;

    private AlMenity insertedAlMenity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlMenity createEntity() {
        return new AlMenity().name(DEFAULT_NAME).iconSvg(DEFAULT_ICON_SVG).propertyType(DEFAULT_PROPERTY_TYPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlMenity createUpdatedEntity() {
        return new AlMenity().name(UPDATED_NAME).iconSvg(UPDATED_ICON_SVG).propertyType(UPDATED_PROPERTY_TYPE);
    }

    @BeforeEach
    public void initTest() {
        alMenity = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlMenity != null) {
            alMenityRepository.delete(insertedAlMenity);
            insertedAlMenity = null;
        }
    }

    @Test
    @Transactional
    void createAlMenity() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlMenity
        var returnedAlMenity = om.readValue(
            restAlMenityMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alMenity)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlMenity.class
        );

        // Validate the AlMenity in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlMenityUpdatableFieldsEquals(returnedAlMenity, getPersistedAlMenity(returnedAlMenity));

        insertedAlMenity = returnedAlMenity;
    }

    @Test
    @Transactional
    void createAlMenityWithExistingId() throws Exception {
        // Create the AlMenity with an existing ID
        alMenity.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlMenityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alMenity)))
            .andExpect(status().isBadRequest());

        // Validate the AlMenity in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alMenity.setName(null);

        // Create the AlMenity, which fails.

        restAlMenityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alMenity)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlMenities() throws Exception {
        // Initialize the database
        insertedAlMenity = alMenityRepository.saveAndFlush(alMenity);

        // Get all the alMenityList
        restAlMenityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alMenity.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].iconSvg").value(hasItem(DEFAULT_ICON_SVG)))
            .andExpect(jsonPath("$.[*].propertyType").value(hasItem(DEFAULT_PROPERTY_TYPE.toString())));
    }

    @Test
    @Transactional
    void getAlMenity() throws Exception {
        // Initialize the database
        insertedAlMenity = alMenityRepository.saveAndFlush(alMenity);

        // Get the alMenity
        restAlMenityMockMvc
            .perform(get(ENTITY_API_URL_ID, alMenity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alMenity.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.iconSvg").value(DEFAULT_ICON_SVG))
            .andExpect(jsonPath("$.propertyType").value(DEFAULT_PROPERTY_TYPE.toString()));
    }

    @Test
    @Transactional
    void getAlMenitiesByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlMenity = alMenityRepository.saveAndFlush(alMenity);

        Long id = alMenity.getId();

        defaultAlMenityFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlMenityFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlMenityFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlMenitiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlMenity = alMenityRepository.saveAndFlush(alMenity);

        // Get all the alMenityList where name equals to
        defaultAlMenityFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlMenitiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlMenity = alMenityRepository.saveAndFlush(alMenity);

        // Get all the alMenityList where name in
        defaultAlMenityFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlMenitiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlMenity = alMenityRepository.saveAndFlush(alMenity);

        // Get all the alMenityList where name is not null
        defaultAlMenityFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAlMenitiesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlMenity = alMenityRepository.saveAndFlush(alMenity);

        // Get all the alMenityList where name contains
        defaultAlMenityFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlMenitiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlMenity = alMenityRepository.saveAndFlush(alMenity);

        // Get all the alMenityList where name does not contain
        defaultAlMenityFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAlMenitiesByIconSvgIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlMenity = alMenityRepository.saveAndFlush(alMenity);

        // Get all the alMenityList where iconSvg equals to
        defaultAlMenityFiltering("iconSvg.equals=" + DEFAULT_ICON_SVG, "iconSvg.equals=" + UPDATED_ICON_SVG);
    }

    @Test
    @Transactional
    void getAllAlMenitiesByIconSvgIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlMenity = alMenityRepository.saveAndFlush(alMenity);

        // Get all the alMenityList where iconSvg in
        defaultAlMenityFiltering("iconSvg.in=" + DEFAULT_ICON_SVG + "," + UPDATED_ICON_SVG, "iconSvg.in=" + UPDATED_ICON_SVG);
    }

    @Test
    @Transactional
    void getAllAlMenitiesByIconSvgIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlMenity = alMenityRepository.saveAndFlush(alMenity);

        // Get all the alMenityList where iconSvg is not null
        defaultAlMenityFiltering("iconSvg.specified=true", "iconSvg.specified=false");
    }

    @Test
    @Transactional
    void getAllAlMenitiesByIconSvgContainsSomething() throws Exception {
        // Initialize the database
        insertedAlMenity = alMenityRepository.saveAndFlush(alMenity);

        // Get all the alMenityList where iconSvg contains
        defaultAlMenityFiltering("iconSvg.contains=" + DEFAULT_ICON_SVG, "iconSvg.contains=" + UPDATED_ICON_SVG);
    }

    @Test
    @Transactional
    void getAllAlMenitiesByIconSvgNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlMenity = alMenityRepository.saveAndFlush(alMenity);

        // Get all the alMenityList where iconSvg does not contain
        defaultAlMenityFiltering("iconSvg.doesNotContain=" + UPDATED_ICON_SVG, "iconSvg.doesNotContain=" + DEFAULT_ICON_SVG);
    }

    @Test
    @Transactional
    void getAllAlMenitiesByPropertyTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlMenity = alMenityRepository.saveAndFlush(alMenity);

        // Get all the alMenityList where propertyType equals to
        defaultAlMenityFiltering("propertyType.equals=" + DEFAULT_PROPERTY_TYPE, "propertyType.equals=" + UPDATED_PROPERTY_TYPE);
    }

    @Test
    @Transactional
    void getAllAlMenitiesByPropertyTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlMenity = alMenityRepository.saveAndFlush(alMenity);

        // Get all the alMenityList where propertyType in
        defaultAlMenityFiltering(
            "propertyType.in=" + DEFAULT_PROPERTY_TYPE + "," + UPDATED_PROPERTY_TYPE,
            "propertyType.in=" + UPDATED_PROPERTY_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAlMenitiesByPropertyTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlMenity = alMenityRepository.saveAndFlush(alMenity);

        // Get all the alMenityList where propertyType is not null
        defaultAlMenityFiltering("propertyType.specified=true", "propertyType.specified=false");
    }

    @Test
    @Transactional
    void getAllAlMenitiesByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alMenityRepository.saveAndFlush(alMenity);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alMenity.setApplication(application);
        alMenityRepository.saveAndFlush(alMenity);
        UUID applicationId = application.getId();
        // Get all the alMenityList where application equals to applicationId
        defaultAlMenityShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alMenityList where application equals to UUID.randomUUID()
        defaultAlMenityShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlMenityFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlMenityShouldBeFound(shouldBeFound);
        defaultAlMenityShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlMenityShouldBeFound(String filter) throws Exception {
        restAlMenityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alMenity.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].iconSvg").value(hasItem(DEFAULT_ICON_SVG)))
            .andExpect(jsonPath("$.[*].propertyType").value(hasItem(DEFAULT_PROPERTY_TYPE.toString())));

        // Check, that the count call also returns 1
        restAlMenityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlMenityShouldNotBeFound(String filter) throws Exception {
        restAlMenityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlMenityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlMenity() throws Exception {
        // Get the alMenity
        restAlMenityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlMenity() throws Exception {
        // Initialize the database
        insertedAlMenity = alMenityRepository.saveAndFlush(alMenity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alMenity
        AlMenity updatedAlMenity = alMenityRepository.findById(alMenity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlMenity are not directly saved in db
        em.detach(updatedAlMenity);
        updatedAlMenity.name(UPDATED_NAME).iconSvg(UPDATED_ICON_SVG).propertyType(UPDATED_PROPERTY_TYPE);

        restAlMenityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlMenity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlMenity))
            )
            .andExpect(status().isOk());

        // Validate the AlMenity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlMenityToMatchAllProperties(updatedAlMenity);
    }

    @Test
    @Transactional
    void putNonExistingAlMenity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alMenity.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlMenityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alMenity.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alMenity))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlMenity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlMenity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alMenity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlMenityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alMenity))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlMenity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlMenity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alMenity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlMenityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alMenity)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlMenity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlMenityWithPatch() throws Exception {
        // Initialize the database
        insertedAlMenity = alMenityRepository.saveAndFlush(alMenity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alMenity using partial update
        AlMenity partialUpdatedAlMenity = new AlMenity();
        partialUpdatedAlMenity.setId(alMenity.getId());

        partialUpdatedAlMenity.iconSvg(UPDATED_ICON_SVG);

        restAlMenityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlMenity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlMenity))
            )
            .andExpect(status().isOk());

        // Validate the AlMenity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlMenityUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAlMenity, alMenity), getPersistedAlMenity(alMenity));
    }

    @Test
    @Transactional
    void fullUpdateAlMenityWithPatch() throws Exception {
        // Initialize the database
        insertedAlMenity = alMenityRepository.saveAndFlush(alMenity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alMenity using partial update
        AlMenity partialUpdatedAlMenity = new AlMenity();
        partialUpdatedAlMenity.setId(alMenity.getId());

        partialUpdatedAlMenity.name(UPDATED_NAME).iconSvg(UPDATED_ICON_SVG).propertyType(UPDATED_PROPERTY_TYPE);

        restAlMenityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlMenity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlMenity))
            )
            .andExpect(status().isOk());

        // Validate the AlMenity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlMenityUpdatableFieldsEquals(partialUpdatedAlMenity, getPersistedAlMenity(partialUpdatedAlMenity));
    }

    @Test
    @Transactional
    void patchNonExistingAlMenity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alMenity.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlMenityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alMenity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alMenity))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlMenity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlMenity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alMenity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlMenityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alMenity))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlMenity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlMenity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alMenity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlMenityMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alMenity)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlMenity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlMenity() throws Exception {
        // Initialize the database
        insertedAlMenity = alMenityRepository.saveAndFlush(alMenity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alMenity
        restAlMenityMockMvc
            .perform(delete(ENTITY_API_URL_ID, alMenity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alMenityRepository.count();
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

    protected AlMenity getPersistedAlMenity(AlMenity alMenity) {
        return alMenityRepository.findById(alMenity.getId()).orElseThrow();
    }

    protected void assertPersistedAlMenityToMatchAllProperties(AlMenity expectedAlMenity) {
        assertAlMenityAllPropertiesEquals(expectedAlMenity, getPersistedAlMenity(expectedAlMenity));
    }

    protected void assertPersistedAlMenityToMatchUpdatableProperties(AlMenity expectedAlMenity) {
        assertAlMenityAllUpdatablePropertiesEquals(expectedAlMenity, getPersistedAlMenity(expectedAlMenity));
    }
}
