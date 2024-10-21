package ai.realworld.web.rest;

import static ai.realworld.domain.AlActisoAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlActiso;
import ai.realworld.domain.JohnLennon;
import ai.realworld.repository.AlActisoRepository;
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
 * Integration tests for the {@link AlActisoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlActisoResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE_JASON = "AAAAAAAAAA";
    private static final String UPDATED_VALUE_JASON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/al-actisos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlActisoRepository alActisoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlActisoMockMvc;

    private AlActiso alActiso;

    private AlActiso insertedAlActiso;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlActiso createEntity() {
        return new AlActiso().key(DEFAULT_KEY).valueJason(DEFAULT_VALUE_JASON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlActiso createUpdatedEntity() {
        return new AlActiso().key(UPDATED_KEY).valueJason(UPDATED_VALUE_JASON);
    }

    @BeforeEach
    public void initTest() {
        alActiso = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlActiso != null) {
            alActisoRepository.delete(insertedAlActiso);
            insertedAlActiso = null;
        }
    }

    @Test
    @Transactional
    void createAlActiso() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlActiso
        var returnedAlActiso = om.readValue(
            restAlActisoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alActiso)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlActiso.class
        );

        // Validate the AlActiso in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlActisoUpdatableFieldsEquals(returnedAlActiso, getPersistedAlActiso(returnedAlActiso));

        insertedAlActiso = returnedAlActiso;
    }

    @Test
    @Transactional
    void createAlActisoWithExistingId() throws Exception {
        // Create the AlActiso with an existing ID
        alActiso.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlActisoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alActiso)))
            .andExpect(status().isBadRequest());

        // Validate the AlActiso in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkKeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alActiso.setKey(null);

        // Create the AlActiso, which fails.

        restAlActisoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alActiso)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlActisos() throws Exception {
        // Initialize the database
        insertedAlActiso = alActisoRepository.saveAndFlush(alActiso);

        // Get all the alActisoList
        restAlActisoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alActiso.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].valueJason").value(hasItem(DEFAULT_VALUE_JASON)));
    }

    @Test
    @Transactional
    void getAlActiso() throws Exception {
        // Initialize the database
        insertedAlActiso = alActisoRepository.saveAndFlush(alActiso);

        // Get the alActiso
        restAlActisoMockMvc
            .perform(get(ENTITY_API_URL_ID, alActiso.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alActiso.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.valueJason").value(DEFAULT_VALUE_JASON));
    }

    @Test
    @Transactional
    void getAlActisosByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlActiso = alActisoRepository.saveAndFlush(alActiso);

        Long id = alActiso.getId();

        defaultAlActisoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlActisoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlActisoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlActisosByKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlActiso = alActisoRepository.saveAndFlush(alActiso);

        // Get all the alActisoList where key equals to
        defaultAlActisoFiltering("key.equals=" + DEFAULT_KEY, "key.equals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllAlActisosByKeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlActiso = alActisoRepository.saveAndFlush(alActiso);

        // Get all the alActisoList where key in
        defaultAlActisoFiltering("key.in=" + DEFAULT_KEY + "," + UPDATED_KEY, "key.in=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllAlActisosByKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlActiso = alActisoRepository.saveAndFlush(alActiso);

        // Get all the alActisoList where key is not null
        defaultAlActisoFiltering("key.specified=true", "key.specified=false");
    }

    @Test
    @Transactional
    void getAllAlActisosByKeyContainsSomething() throws Exception {
        // Initialize the database
        insertedAlActiso = alActisoRepository.saveAndFlush(alActiso);

        // Get all the alActisoList where key contains
        defaultAlActisoFiltering("key.contains=" + DEFAULT_KEY, "key.contains=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllAlActisosByKeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlActiso = alActisoRepository.saveAndFlush(alActiso);

        // Get all the alActisoList where key does not contain
        defaultAlActisoFiltering("key.doesNotContain=" + UPDATED_KEY, "key.doesNotContain=" + DEFAULT_KEY);
    }

    @Test
    @Transactional
    void getAllAlActisosByValueJasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlActiso = alActisoRepository.saveAndFlush(alActiso);

        // Get all the alActisoList where valueJason equals to
        defaultAlActisoFiltering("valueJason.equals=" + DEFAULT_VALUE_JASON, "valueJason.equals=" + UPDATED_VALUE_JASON);
    }

    @Test
    @Transactional
    void getAllAlActisosByValueJasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlActiso = alActisoRepository.saveAndFlush(alActiso);

        // Get all the alActisoList where valueJason in
        defaultAlActisoFiltering(
            "valueJason.in=" + DEFAULT_VALUE_JASON + "," + UPDATED_VALUE_JASON,
            "valueJason.in=" + UPDATED_VALUE_JASON
        );
    }

    @Test
    @Transactional
    void getAllAlActisosByValueJasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlActiso = alActisoRepository.saveAndFlush(alActiso);

        // Get all the alActisoList where valueJason is not null
        defaultAlActisoFiltering("valueJason.specified=true", "valueJason.specified=false");
    }

    @Test
    @Transactional
    void getAllAlActisosByValueJasonContainsSomething() throws Exception {
        // Initialize the database
        insertedAlActiso = alActisoRepository.saveAndFlush(alActiso);

        // Get all the alActisoList where valueJason contains
        defaultAlActisoFiltering("valueJason.contains=" + DEFAULT_VALUE_JASON, "valueJason.contains=" + UPDATED_VALUE_JASON);
    }

    @Test
    @Transactional
    void getAllAlActisosByValueJasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlActiso = alActisoRepository.saveAndFlush(alActiso);

        // Get all the alActisoList where valueJason does not contain
        defaultAlActisoFiltering("valueJason.doesNotContain=" + UPDATED_VALUE_JASON, "valueJason.doesNotContain=" + DEFAULT_VALUE_JASON);
    }

    @Test
    @Transactional
    void getAllAlActisosByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alActisoRepository.saveAndFlush(alActiso);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alActiso.setApplication(application);
        alActisoRepository.saveAndFlush(alActiso);
        UUID applicationId = application.getId();
        // Get all the alActisoList where application equals to applicationId
        defaultAlActisoShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alActisoList where application equals to UUID.randomUUID()
        defaultAlActisoShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlActisoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlActisoShouldBeFound(shouldBeFound);
        defaultAlActisoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlActisoShouldBeFound(String filter) throws Exception {
        restAlActisoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alActiso.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].valueJason").value(hasItem(DEFAULT_VALUE_JASON)));

        // Check, that the count call also returns 1
        restAlActisoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlActisoShouldNotBeFound(String filter) throws Exception {
        restAlActisoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlActisoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlActiso() throws Exception {
        // Get the alActiso
        restAlActisoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlActiso() throws Exception {
        // Initialize the database
        insertedAlActiso = alActisoRepository.saveAndFlush(alActiso);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alActiso
        AlActiso updatedAlActiso = alActisoRepository.findById(alActiso.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlActiso are not directly saved in db
        em.detach(updatedAlActiso);
        updatedAlActiso.key(UPDATED_KEY).valueJason(UPDATED_VALUE_JASON);

        restAlActisoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlActiso.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlActiso))
            )
            .andExpect(status().isOk());

        // Validate the AlActiso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlActisoToMatchAllProperties(updatedAlActiso);
    }

    @Test
    @Transactional
    void putNonExistingAlActiso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alActiso.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlActisoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alActiso.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alActiso))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlActiso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlActiso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alActiso.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlActisoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alActiso))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlActiso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlActiso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alActiso.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlActisoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alActiso)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlActiso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlActisoWithPatch() throws Exception {
        // Initialize the database
        insertedAlActiso = alActisoRepository.saveAndFlush(alActiso);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alActiso using partial update
        AlActiso partialUpdatedAlActiso = new AlActiso();
        partialUpdatedAlActiso.setId(alActiso.getId());

        partialUpdatedAlActiso.key(UPDATED_KEY);

        restAlActisoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlActiso.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlActiso))
            )
            .andExpect(status().isOk());

        // Validate the AlActiso in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlActisoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAlActiso, alActiso), getPersistedAlActiso(alActiso));
    }

    @Test
    @Transactional
    void fullUpdateAlActisoWithPatch() throws Exception {
        // Initialize the database
        insertedAlActiso = alActisoRepository.saveAndFlush(alActiso);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alActiso using partial update
        AlActiso partialUpdatedAlActiso = new AlActiso();
        partialUpdatedAlActiso.setId(alActiso.getId());

        partialUpdatedAlActiso.key(UPDATED_KEY).valueJason(UPDATED_VALUE_JASON);

        restAlActisoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlActiso.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlActiso))
            )
            .andExpect(status().isOk());

        // Validate the AlActiso in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlActisoUpdatableFieldsEquals(partialUpdatedAlActiso, getPersistedAlActiso(partialUpdatedAlActiso));
    }

    @Test
    @Transactional
    void patchNonExistingAlActiso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alActiso.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlActisoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alActiso.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alActiso))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlActiso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlActiso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alActiso.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlActisoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alActiso))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlActiso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlActiso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alActiso.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlActisoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alActiso)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlActiso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlActiso() throws Exception {
        // Initialize the database
        insertedAlActiso = alActisoRepository.saveAndFlush(alActiso);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alActiso
        restAlActisoMockMvc
            .perform(delete(ENTITY_API_URL_ID, alActiso.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alActisoRepository.count();
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

    protected AlActiso getPersistedAlActiso(AlActiso alActiso) {
        return alActisoRepository.findById(alActiso.getId()).orElseThrow();
    }

    protected void assertPersistedAlActisoToMatchAllProperties(AlActiso expectedAlActiso) {
        assertAlActisoAllPropertiesEquals(expectedAlActiso, getPersistedAlActiso(expectedAlActiso));
    }

    protected void assertPersistedAlActisoToMatchUpdatableProperties(AlActiso expectedAlActiso) {
        assertAlActisoAllUpdatablePropertiesEquals(expectedAlActiso, getPersistedAlActiso(expectedAlActiso));
    }
}
