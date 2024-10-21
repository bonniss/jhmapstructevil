package ai.realworld.web.rest;

import static ai.realworld.domain.SaisanCogAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.SaisanCog;
import ai.realworld.repository.SaisanCogRepository;
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
 * Integration tests for the {@link SaisanCogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SaisanCogResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE_JASON = "AAAAAAAAAA";
    private static final String UPDATED_VALUE_JASON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/saisan-cogs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SaisanCogRepository saisanCogRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSaisanCogMockMvc;

    private SaisanCog saisanCog;

    private SaisanCog insertedSaisanCog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SaisanCog createEntity() {
        return new SaisanCog().key(DEFAULT_KEY).valueJason(DEFAULT_VALUE_JASON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SaisanCog createUpdatedEntity() {
        return new SaisanCog().key(UPDATED_KEY).valueJason(UPDATED_VALUE_JASON);
    }

    @BeforeEach
    public void initTest() {
        saisanCog = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSaisanCog != null) {
            saisanCogRepository.delete(insertedSaisanCog);
            insertedSaisanCog = null;
        }
    }

    @Test
    @Transactional
    void createSaisanCog() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SaisanCog
        var returnedSaisanCog = om.readValue(
            restSaisanCogMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saisanCog)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SaisanCog.class
        );

        // Validate the SaisanCog in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSaisanCogUpdatableFieldsEquals(returnedSaisanCog, getPersistedSaisanCog(returnedSaisanCog));

        insertedSaisanCog = returnedSaisanCog;
    }

    @Test
    @Transactional
    void createSaisanCogWithExistingId() throws Exception {
        // Create the SaisanCog with an existing ID
        saisanCog.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSaisanCogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saisanCog)))
            .andExpect(status().isBadRequest());

        // Validate the SaisanCog in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkKeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        saisanCog.setKey(null);

        // Create the SaisanCog, which fails.

        restSaisanCogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saisanCog)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSaisanCogs() throws Exception {
        // Initialize the database
        insertedSaisanCog = saisanCogRepository.saveAndFlush(saisanCog);

        // Get all the saisanCogList
        restSaisanCogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saisanCog.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].valueJason").value(hasItem(DEFAULT_VALUE_JASON)));
    }

    @Test
    @Transactional
    void getSaisanCog() throws Exception {
        // Initialize the database
        insertedSaisanCog = saisanCogRepository.saveAndFlush(saisanCog);

        // Get the saisanCog
        restSaisanCogMockMvc
            .perform(get(ENTITY_API_URL_ID, saisanCog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(saisanCog.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.valueJason").value(DEFAULT_VALUE_JASON));
    }

    @Test
    @Transactional
    void getSaisanCogsByIdFiltering() throws Exception {
        // Initialize the database
        insertedSaisanCog = saisanCogRepository.saveAndFlush(saisanCog);

        Long id = saisanCog.getId();

        defaultSaisanCogFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSaisanCogFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSaisanCogFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSaisanCogsByKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSaisanCog = saisanCogRepository.saveAndFlush(saisanCog);

        // Get all the saisanCogList where key equals to
        defaultSaisanCogFiltering("key.equals=" + DEFAULT_KEY, "key.equals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllSaisanCogsByKeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSaisanCog = saisanCogRepository.saveAndFlush(saisanCog);

        // Get all the saisanCogList where key in
        defaultSaisanCogFiltering("key.in=" + DEFAULT_KEY + "," + UPDATED_KEY, "key.in=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllSaisanCogsByKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSaisanCog = saisanCogRepository.saveAndFlush(saisanCog);

        // Get all the saisanCogList where key is not null
        defaultSaisanCogFiltering("key.specified=true", "key.specified=false");
    }

    @Test
    @Transactional
    void getAllSaisanCogsByKeyContainsSomething() throws Exception {
        // Initialize the database
        insertedSaisanCog = saisanCogRepository.saveAndFlush(saisanCog);

        // Get all the saisanCogList where key contains
        defaultSaisanCogFiltering("key.contains=" + DEFAULT_KEY, "key.contains=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllSaisanCogsByKeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSaisanCog = saisanCogRepository.saveAndFlush(saisanCog);

        // Get all the saisanCogList where key does not contain
        defaultSaisanCogFiltering("key.doesNotContain=" + UPDATED_KEY, "key.doesNotContain=" + DEFAULT_KEY);
    }

    @Test
    @Transactional
    void getAllSaisanCogsByValueJasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSaisanCog = saisanCogRepository.saveAndFlush(saisanCog);

        // Get all the saisanCogList where valueJason equals to
        defaultSaisanCogFiltering("valueJason.equals=" + DEFAULT_VALUE_JASON, "valueJason.equals=" + UPDATED_VALUE_JASON);
    }

    @Test
    @Transactional
    void getAllSaisanCogsByValueJasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSaisanCog = saisanCogRepository.saveAndFlush(saisanCog);

        // Get all the saisanCogList where valueJason in
        defaultSaisanCogFiltering(
            "valueJason.in=" + DEFAULT_VALUE_JASON + "," + UPDATED_VALUE_JASON,
            "valueJason.in=" + UPDATED_VALUE_JASON
        );
    }

    @Test
    @Transactional
    void getAllSaisanCogsByValueJasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSaisanCog = saisanCogRepository.saveAndFlush(saisanCog);

        // Get all the saisanCogList where valueJason is not null
        defaultSaisanCogFiltering("valueJason.specified=true", "valueJason.specified=false");
    }

    @Test
    @Transactional
    void getAllSaisanCogsByValueJasonContainsSomething() throws Exception {
        // Initialize the database
        insertedSaisanCog = saisanCogRepository.saveAndFlush(saisanCog);

        // Get all the saisanCogList where valueJason contains
        defaultSaisanCogFiltering("valueJason.contains=" + DEFAULT_VALUE_JASON, "valueJason.contains=" + UPDATED_VALUE_JASON);
    }

    @Test
    @Transactional
    void getAllSaisanCogsByValueJasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSaisanCog = saisanCogRepository.saveAndFlush(saisanCog);

        // Get all the saisanCogList where valueJason does not contain
        defaultSaisanCogFiltering("valueJason.doesNotContain=" + UPDATED_VALUE_JASON, "valueJason.doesNotContain=" + DEFAULT_VALUE_JASON);
    }

    private void defaultSaisanCogFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSaisanCogShouldBeFound(shouldBeFound);
        defaultSaisanCogShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSaisanCogShouldBeFound(String filter) throws Exception {
        restSaisanCogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saisanCog.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].valueJason").value(hasItem(DEFAULT_VALUE_JASON)));

        // Check, that the count call also returns 1
        restSaisanCogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSaisanCogShouldNotBeFound(String filter) throws Exception {
        restSaisanCogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSaisanCogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSaisanCog() throws Exception {
        // Get the saisanCog
        restSaisanCogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSaisanCog() throws Exception {
        // Initialize the database
        insertedSaisanCog = saisanCogRepository.saveAndFlush(saisanCog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the saisanCog
        SaisanCog updatedSaisanCog = saisanCogRepository.findById(saisanCog.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSaisanCog are not directly saved in db
        em.detach(updatedSaisanCog);
        updatedSaisanCog.key(UPDATED_KEY).valueJason(UPDATED_VALUE_JASON);

        restSaisanCogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSaisanCog.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSaisanCog))
            )
            .andExpect(status().isOk());

        // Validate the SaisanCog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSaisanCogToMatchAllProperties(updatedSaisanCog);
    }

    @Test
    @Transactional
    void putNonExistingSaisanCog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saisanCog.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSaisanCogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, saisanCog.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saisanCog))
            )
            .andExpect(status().isBadRequest());

        // Validate the SaisanCog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSaisanCog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saisanCog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaisanCogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(saisanCog))
            )
            .andExpect(status().isBadRequest());

        // Validate the SaisanCog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSaisanCog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saisanCog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaisanCogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saisanCog)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SaisanCog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSaisanCogWithPatch() throws Exception {
        // Initialize the database
        insertedSaisanCog = saisanCogRepository.saveAndFlush(saisanCog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the saisanCog using partial update
        SaisanCog partialUpdatedSaisanCog = new SaisanCog();
        partialUpdatedSaisanCog.setId(saisanCog.getId());

        partialUpdatedSaisanCog.valueJason(UPDATED_VALUE_JASON);

        restSaisanCogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSaisanCog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSaisanCog))
            )
            .andExpect(status().isOk());

        // Validate the SaisanCog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSaisanCogUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSaisanCog, saisanCog),
            getPersistedSaisanCog(saisanCog)
        );
    }

    @Test
    @Transactional
    void fullUpdateSaisanCogWithPatch() throws Exception {
        // Initialize the database
        insertedSaisanCog = saisanCogRepository.saveAndFlush(saisanCog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the saisanCog using partial update
        SaisanCog partialUpdatedSaisanCog = new SaisanCog();
        partialUpdatedSaisanCog.setId(saisanCog.getId());

        partialUpdatedSaisanCog.key(UPDATED_KEY).valueJason(UPDATED_VALUE_JASON);

        restSaisanCogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSaisanCog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSaisanCog))
            )
            .andExpect(status().isOk());

        // Validate the SaisanCog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSaisanCogUpdatableFieldsEquals(partialUpdatedSaisanCog, getPersistedSaisanCog(partialUpdatedSaisanCog));
    }

    @Test
    @Transactional
    void patchNonExistingSaisanCog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saisanCog.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSaisanCogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, saisanCog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(saisanCog))
            )
            .andExpect(status().isBadRequest());

        // Validate the SaisanCog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSaisanCog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saisanCog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaisanCogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(saisanCog))
            )
            .andExpect(status().isBadRequest());

        // Validate the SaisanCog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSaisanCog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saisanCog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaisanCogMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(saisanCog)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SaisanCog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSaisanCog() throws Exception {
        // Initialize the database
        insertedSaisanCog = saisanCogRepository.saveAndFlush(saisanCog);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the saisanCog
        restSaisanCogMockMvc
            .perform(delete(ENTITY_API_URL_ID, saisanCog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return saisanCogRepository.count();
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

    protected SaisanCog getPersistedSaisanCog(SaisanCog saisanCog) {
        return saisanCogRepository.findById(saisanCog.getId()).orElseThrow();
    }

    protected void assertPersistedSaisanCogToMatchAllProperties(SaisanCog expectedSaisanCog) {
        assertSaisanCogAllPropertiesEquals(expectedSaisanCog, getPersistedSaisanCog(expectedSaisanCog));
    }

    protected void assertPersistedSaisanCogToMatchUpdatableProperties(SaisanCog expectedSaisanCog) {
        assertSaisanCogAllUpdatablePropertiesEquals(expectedSaisanCog, getPersistedSaisanCog(expectedSaisanCog));
    }
}
