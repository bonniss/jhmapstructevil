package ai.realworld.web.rest;

import static ai.realworld.domain.AlBestToothViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlBestToothVi;
import ai.realworld.repository.AlBestToothViRepository;
import ai.realworld.service.dto.AlBestToothViDTO;
import ai.realworld.service.mapper.AlBestToothViMapper;
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
 * Integration tests for the {@link AlBestToothViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlBestToothViResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/al-best-tooth-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlBestToothViRepository alBestToothViRepository;

    @Autowired
    private AlBestToothViMapper alBestToothViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlBestToothViMockMvc;

    private AlBestToothVi alBestToothVi;

    private AlBestToothVi insertedAlBestToothVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlBestToothVi createEntity() {
        return new AlBestToothVi().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlBestToothVi createUpdatedEntity() {
        return new AlBestToothVi().name(UPDATED_NAME);
    }

    @BeforeEach
    public void initTest() {
        alBestToothVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlBestToothVi != null) {
            alBestToothViRepository.delete(insertedAlBestToothVi);
            insertedAlBestToothVi = null;
        }
    }

    @Test
    @Transactional
    void createAlBestToothVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlBestToothVi
        AlBestToothViDTO alBestToothViDTO = alBestToothViMapper.toDto(alBestToothVi);
        var returnedAlBestToothViDTO = om.readValue(
            restAlBestToothViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alBestToothViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlBestToothViDTO.class
        );

        // Validate the AlBestToothVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlBestToothVi = alBestToothViMapper.toEntity(returnedAlBestToothViDTO);
        assertAlBestToothViUpdatableFieldsEquals(returnedAlBestToothVi, getPersistedAlBestToothVi(returnedAlBestToothVi));

        insertedAlBestToothVi = returnedAlBestToothVi;
    }

    @Test
    @Transactional
    void createAlBestToothViWithExistingId() throws Exception {
        // Create the AlBestToothVi with an existing ID
        alBestToothVi.setId(1L);
        AlBestToothViDTO alBestToothViDTO = alBestToothViMapper.toDto(alBestToothVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlBestToothViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alBestToothViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlBestToothVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alBestToothVi.setName(null);

        // Create the AlBestToothVi, which fails.
        AlBestToothViDTO alBestToothViDTO = alBestToothViMapper.toDto(alBestToothVi);

        restAlBestToothViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alBestToothViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlBestToothVis() throws Exception {
        // Initialize the database
        insertedAlBestToothVi = alBestToothViRepository.saveAndFlush(alBestToothVi);

        // Get all the alBestToothViList
        restAlBestToothViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alBestToothVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getAlBestToothVi() throws Exception {
        // Initialize the database
        insertedAlBestToothVi = alBestToothViRepository.saveAndFlush(alBestToothVi);

        // Get the alBestToothVi
        restAlBestToothViMockMvc
            .perform(get(ENTITY_API_URL_ID, alBestToothVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alBestToothVi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getAlBestToothVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlBestToothVi = alBestToothViRepository.saveAndFlush(alBestToothVi);

        Long id = alBestToothVi.getId();

        defaultAlBestToothViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlBestToothViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlBestToothViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlBestToothVisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlBestToothVi = alBestToothViRepository.saveAndFlush(alBestToothVi);

        // Get all the alBestToothViList where name equals to
        defaultAlBestToothViFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlBestToothVisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlBestToothVi = alBestToothViRepository.saveAndFlush(alBestToothVi);

        // Get all the alBestToothViList where name in
        defaultAlBestToothViFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlBestToothVisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlBestToothVi = alBestToothViRepository.saveAndFlush(alBestToothVi);

        // Get all the alBestToothViList where name is not null
        defaultAlBestToothViFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAlBestToothVisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlBestToothVi = alBestToothViRepository.saveAndFlush(alBestToothVi);

        // Get all the alBestToothViList where name contains
        defaultAlBestToothViFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlBestToothVisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlBestToothVi = alBestToothViRepository.saveAndFlush(alBestToothVi);

        // Get all the alBestToothViList where name does not contain
        defaultAlBestToothViFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    private void defaultAlBestToothViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlBestToothViShouldBeFound(shouldBeFound);
        defaultAlBestToothViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlBestToothViShouldBeFound(String filter) throws Exception {
        restAlBestToothViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alBestToothVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restAlBestToothViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlBestToothViShouldNotBeFound(String filter) throws Exception {
        restAlBestToothViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlBestToothViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlBestToothVi() throws Exception {
        // Get the alBestToothVi
        restAlBestToothViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlBestToothVi() throws Exception {
        // Initialize the database
        insertedAlBestToothVi = alBestToothViRepository.saveAndFlush(alBestToothVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alBestToothVi
        AlBestToothVi updatedAlBestToothVi = alBestToothViRepository.findById(alBestToothVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlBestToothVi are not directly saved in db
        em.detach(updatedAlBestToothVi);
        updatedAlBestToothVi.name(UPDATED_NAME);
        AlBestToothViDTO alBestToothViDTO = alBestToothViMapper.toDto(updatedAlBestToothVi);

        restAlBestToothViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alBestToothViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alBestToothViDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlBestToothVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlBestToothViToMatchAllProperties(updatedAlBestToothVi);
    }

    @Test
    @Transactional
    void putNonExistingAlBestToothVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alBestToothVi.setId(longCount.incrementAndGet());

        // Create the AlBestToothVi
        AlBestToothViDTO alBestToothViDTO = alBestToothViMapper.toDto(alBestToothVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlBestToothViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alBestToothViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alBestToothViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlBestToothVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlBestToothVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alBestToothVi.setId(longCount.incrementAndGet());

        // Create the AlBestToothVi
        AlBestToothViDTO alBestToothViDTO = alBestToothViMapper.toDto(alBestToothVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlBestToothViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alBestToothViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlBestToothVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlBestToothVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alBestToothVi.setId(longCount.incrementAndGet());

        // Create the AlBestToothVi
        AlBestToothViDTO alBestToothViDTO = alBestToothViMapper.toDto(alBestToothVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlBestToothViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alBestToothViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlBestToothVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlBestToothViWithPatch() throws Exception {
        // Initialize the database
        insertedAlBestToothVi = alBestToothViRepository.saveAndFlush(alBestToothVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alBestToothVi using partial update
        AlBestToothVi partialUpdatedAlBestToothVi = new AlBestToothVi();
        partialUpdatedAlBestToothVi.setId(alBestToothVi.getId());

        restAlBestToothViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlBestToothVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlBestToothVi))
            )
            .andExpect(status().isOk());

        // Validate the AlBestToothVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlBestToothViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlBestToothVi, alBestToothVi),
            getPersistedAlBestToothVi(alBestToothVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlBestToothViWithPatch() throws Exception {
        // Initialize the database
        insertedAlBestToothVi = alBestToothViRepository.saveAndFlush(alBestToothVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alBestToothVi using partial update
        AlBestToothVi partialUpdatedAlBestToothVi = new AlBestToothVi();
        partialUpdatedAlBestToothVi.setId(alBestToothVi.getId());

        partialUpdatedAlBestToothVi.name(UPDATED_NAME);

        restAlBestToothViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlBestToothVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlBestToothVi))
            )
            .andExpect(status().isOk());

        // Validate the AlBestToothVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlBestToothViUpdatableFieldsEquals(partialUpdatedAlBestToothVi, getPersistedAlBestToothVi(partialUpdatedAlBestToothVi));
    }

    @Test
    @Transactional
    void patchNonExistingAlBestToothVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alBestToothVi.setId(longCount.incrementAndGet());

        // Create the AlBestToothVi
        AlBestToothViDTO alBestToothViDTO = alBestToothViMapper.toDto(alBestToothVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlBestToothViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alBestToothViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alBestToothViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlBestToothVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlBestToothVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alBestToothVi.setId(longCount.incrementAndGet());

        // Create the AlBestToothVi
        AlBestToothViDTO alBestToothViDTO = alBestToothViMapper.toDto(alBestToothVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlBestToothViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alBestToothViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlBestToothVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlBestToothVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alBestToothVi.setId(longCount.incrementAndGet());

        // Create the AlBestToothVi
        AlBestToothViDTO alBestToothViDTO = alBestToothViMapper.toDto(alBestToothVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlBestToothViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alBestToothViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlBestToothVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlBestToothVi() throws Exception {
        // Initialize the database
        insertedAlBestToothVi = alBestToothViRepository.saveAndFlush(alBestToothVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alBestToothVi
        restAlBestToothViMockMvc
            .perform(delete(ENTITY_API_URL_ID, alBestToothVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alBestToothViRepository.count();
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

    protected AlBestToothVi getPersistedAlBestToothVi(AlBestToothVi alBestToothVi) {
        return alBestToothViRepository.findById(alBestToothVi.getId()).orElseThrow();
    }

    protected void assertPersistedAlBestToothViToMatchAllProperties(AlBestToothVi expectedAlBestToothVi) {
        assertAlBestToothViAllPropertiesEquals(expectedAlBestToothVi, getPersistedAlBestToothVi(expectedAlBestToothVi));
    }

    protected void assertPersistedAlBestToothViToMatchUpdatableProperties(AlBestToothVi expectedAlBestToothVi) {
        assertAlBestToothViAllUpdatablePropertiesEquals(expectedAlBestToothVi, getPersistedAlBestToothVi(expectedAlBestToothVi));
    }
}
