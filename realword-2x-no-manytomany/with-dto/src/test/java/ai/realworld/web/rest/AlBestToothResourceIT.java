package ai.realworld.web.rest;

import static ai.realworld.domain.AlBestToothAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlBestTooth;
import ai.realworld.domain.JohnLennon;
import ai.realworld.repository.AlBestToothRepository;
import ai.realworld.service.dto.AlBestToothDTO;
import ai.realworld.service.mapper.AlBestToothMapper;
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
 * Integration tests for the {@link AlBestToothResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlBestToothResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/al-best-tooths";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlBestToothRepository alBestToothRepository;

    @Autowired
    private AlBestToothMapper alBestToothMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlBestToothMockMvc;

    private AlBestTooth alBestTooth;

    private AlBestTooth insertedAlBestTooth;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlBestTooth createEntity() {
        return new AlBestTooth().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlBestTooth createUpdatedEntity() {
        return new AlBestTooth().name(UPDATED_NAME);
    }

    @BeforeEach
    public void initTest() {
        alBestTooth = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlBestTooth != null) {
            alBestToothRepository.delete(insertedAlBestTooth);
            insertedAlBestTooth = null;
        }
    }

    @Test
    @Transactional
    void createAlBestTooth() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlBestTooth
        AlBestToothDTO alBestToothDTO = alBestToothMapper.toDto(alBestTooth);
        var returnedAlBestToothDTO = om.readValue(
            restAlBestToothMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alBestToothDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlBestToothDTO.class
        );

        // Validate the AlBestTooth in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlBestTooth = alBestToothMapper.toEntity(returnedAlBestToothDTO);
        assertAlBestToothUpdatableFieldsEquals(returnedAlBestTooth, getPersistedAlBestTooth(returnedAlBestTooth));

        insertedAlBestTooth = returnedAlBestTooth;
    }

    @Test
    @Transactional
    void createAlBestToothWithExistingId() throws Exception {
        // Create the AlBestTooth with an existing ID
        alBestTooth.setId(1L);
        AlBestToothDTO alBestToothDTO = alBestToothMapper.toDto(alBestTooth);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlBestToothMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alBestToothDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlBestTooth in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alBestTooth.setName(null);

        // Create the AlBestTooth, which fails.
        AlBestToothDTO alBestToothDTO = alBestToothMapper.toDto(alBestTooth);

        restAlBestToothMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alBestToothDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlBestTooths() throws Exception {
        // Initialize the database
        insertedAlBestTooth = alBestToothRepository.saveAndFlush(alBestTooth);

        // Get all the alBestToothList
        restAlBestToothMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alBestTooth.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getAlBestTooth() throws Exception {
        // Initialize the database
        insertedAlBestTooth = alBestToothRepository.saveAndFlush(alBestTooth);

        // Get the alBestTooth
        restAlBestToothMockMvc
            .perform(get(ENTITY_API_URL_ID, alBestTooth.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alBestTooth.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getAlBestToothsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlBestTooth = alBestToothRepository.saveAndFlush(alBestTooth);

        Long id = alBestTooth.getId();

        defaultAlBestToothFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlBestToothFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlBestToothFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlBestToothsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlBestTooth = alBestToothRepository.saveAndFlush(alBestTooth);

        // Get all the alBestToothList where name equals to
        defaultAlBestToothFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlBestToothsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlBestTooth = alBestToothRepository.saveAndFlush(alBestTooth);

        // Get all the alBestToothList where name in
        defaultAlBestToothFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlBestToothsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlBestTooth = alBestToothRepository.saveAndFlush(alBestTooth);

        // Get all the alBestToothList where name is not null
        defaultAlBestToothFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAlBestToothsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlBestTooth = alBestToothRepository.saveAndFlush(alBestTooth);

        // Get all the alBestToothList where name contains
        defaultAlBestToothFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlBestToothsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlBestTooth = alBestToothRepository.saveAndFlush(alBestTooth);

        // Get all the alBestToothList where name does not contain
        defaultAlBestToothFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAlBestToothsByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alBestToothRepository.saveAndFlush(alBestTooth);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alBestTooth.setApplication(application);
        alBestToothRepository.saveAndFlush(alBestTooth);
        UUID applicationId = application.getId();
        // Get all the alBestToothList where application equals to applicationId
        defaultAlBestToothShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alBestToothList where application equals to UUID.randomUUID()
        defaultAlBestToothShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlBestToothFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlBestToothShouldBeFound(shouldBeFound);
        defaultAlBestToothShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlBestToothShouldBeFound(String filter) throws Exception {
        restAlBestToothMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alBestTooth.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restAlBestToothMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlBestToothShouldNotBeFound(String filter) throws Exception {
        restAlBestToothMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlBestToothMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlBestTooth() throws Exception {
        // Get the alBestTooth
        restAlBestToothMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlBestTooth() throws Exception {
        // Initialize the database
        insertedAlBestTooth = alBestToothRepository.saveAndFlush(alBestTooth);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alBestTooth
        AlBestTooth updatedAlBestTooth = alBestToothRepository.findById(alBestTooth.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlBestTooth are not directly saved in db
        em.detach(updatedAlBestTooth);
        updatedAlBestTooth.name(UPDATED_NAME);
        AlBestToothDTO alBestToothDTO = alBestToothMapper.toDto(updatedAlBestTooth);

        restAlBestToothMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alBestToothDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alBestToothDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlBestTooth in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlBestToothToMatchAllProperties(updatedAlBestTooth);
    }

    @Test
    @Transactional
    void putNonExistingAlBestTooth() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alBestTooth.setId(longCount.incrementAndGet());

        // Create the AlBestTooth
        AlBestToothDTO alBestToothDTO = alBestToothMapper.toDto(alBestTooth);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlBestToothMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alBestToothDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alBestToothDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlBestTooth in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlBestTooth() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alBestTooth.setId(longCount.incrementAndGet());

        // Create the AlBestTooth
        AlBestToothDTO alBestToothDTO = alBestToothMapper.toDto(alBestTooth);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlBestToothMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alBestToothDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlBestTooth in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlBestTooth() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alBestTooth.setId(longCount.incrementAndGet());

        // Create the AlBestTooth
        AlBestToothDTO alBestToothDTO = alBestToothMapper.toDto(alBestTooth);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlBestToothMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alBestToothDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlBestTooth in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlBestToothWithPatch() throws Exception {
        // Initialize the database
        insertedAlBestTooth = alBestToothRepository.saveAndFlush(alBestTooth);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alBestTooth using partial update
        AlBestTooth partialUpdatedAlBestTooth = new AlBestTooth();
        partialUpdatedAlBestTooth.setId(alBestTooth.getId());

        partialUpdatedAlBestTooth.name(UPDATED_NAME);

        restAlBestToothMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlBestTooth.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlBestTooth))
            )
            .andExpect(status().isOk());

        // Validate the AlBestTooth in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlBestToothUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlBestTooth, alBestTooth),
            getPersistedAlBestTooth(alBestTooth)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlBestToothWithPatch() throws Exception {
        // Initialize the database
        insertedAlBestTooth = alBestToothRepository.saveAndFlush(alBestTooth);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alBestTooth using partial update
        AlBestTooth partialUpdatedAlBestTooth = new AlBestTooth();
        partialUpdatedAlBestTooth.setId(alBestTooth.getId());

        partialUpdatedAlBestTooth.name(UPDATED_NAME);

        restAlBestToothMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlBestTooth.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlBestTooth))
            )
            .andExpect(status().isOk());

        // Validate the AlBestTooth in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlBestToothUpdatableFieldsEquals(partialUpdatedAlBestTooth, getPersistedAlBestTooth(partialUpdatedAlBestTooth));
    }

    @Test
    @Transactional
    void patchNonExistingAlBestTooth() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alBestTooth.setId(longCount.incrementAndGet());

        // Create the AlBestTooth
        AlBestToothDTO alBestToothDTO = alBestToothMapper.toDto(alBestTooth);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlBestToothMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alBestToothDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alBestToothDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlBestTooth in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlBestTooth() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alBestTooth.setId(longCount.incrementAndGet());

        // Create the AlBestTooth
        AlBestToothDTO alBestToothDTO = alBestToothMapper.toDto(alBestTooth);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlBestToothMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alBestToothDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlBestTooth in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlBestTooth() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alBestTooth.setId(longCount.incrementAndGet());

        // Create the AlBestTooth
        AlBestToothDTO alBestToothDTO = alBestToothMapper.toDto(alBestTooth);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlBestToothMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alBestToothDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlBestTooth in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlBestTooth() throws Exception {
        // Initialize the database
        insertedAlBestTooth = alBestToothRepository.saveAndFlush(alBestTooth);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alBestTooth
        restAlBestToothMockMvc
            .perform(delete(ENTITY_API_URL_ID, alBestTooth.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alBestToothRepository.count();
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

    protected AlBestTooth getPersistedAlBestTooth(AlBestTooth alBestTooth) {
        return alBestToothRepository.findById(alBestTooth.getId()).orElseThrow();
    }

    protected void assertPersistedAlBestToothToMatchAllProperties(AlBestTooth expectedAlBestTooth) {
        assertAlBestToothAllPropertiesEquals(expectedAlBestTooth, getPersistedAlBestTooth(expectedAlBestTooth));
    }

    protected void assertPersistedAlBestToothToMatchUpdatableProperties(AlBestTooth expectedAlBestTooth) {
        assertAlBestToothAllUpdatablePropertiesEquals(expectedAlBestTooth, getPersistedAlBestTooth(expectedAlBestTooth));
    }
}
