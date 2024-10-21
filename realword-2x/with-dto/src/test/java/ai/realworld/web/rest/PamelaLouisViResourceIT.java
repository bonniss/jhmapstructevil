package ai.realworld.web.rest;

import static ai.realworld.domain.PamelaLouisViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.PamelaLouisVi;
import ai.realworld.repository.PamelaLouisViRepository;
import ai.realworld.service.dto.PamelaLouisViDTO;
import ai.realworld.service.mapper.PamelaLouisViMapper;
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
 * Integration tests for the {@link PamelaLouisViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PamelaLouisViResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIG_JASON = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_JASON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pamela-louis-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PamelaLouisViRepository pamelaLouisViRepository;

    @Autowired
    private PamelaLouisViMapper pamelaLouisViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPamelaLouisViMockMvc;

    private PamelaLouisVi pamelaLouisVi;

    private PamelaLouisVi insertedPamelaLouisVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PamelaLouisVi createEntity() {
        return new PamelaLouisVi().name(DEFAULT_NAME).configJason(DEFAULT_CONFIG_JASON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PamelaLouisVi createUpdatedEntity() {
        return new PamelaLouisVi().name(UPDATED_NAME).configJason(UPDATED_CONFIG_JASON);
    }

    @BeforeEach
    public void initTest() {
        pamelaLouisVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPamelaLouisVi != null) {
            pamelaLouisViRepository.delete(insertedPamelaLouisVi);
            insertedPamelaLouisVi = null;
        }
    }

    @Test
    @Transactional
    void createPamelaLouisVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PamelaLouisVi
        PamelaLouisViDTO pamelaLouisViDTO = pamelaLouisViMapper.toDto(pamelaLouisVi);
        var returnedPamelaLouisViDTO = om.readValue(
            restPamelaLouisViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pamelaLouisViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PamelaLouisViDTO.class
        );

        // Validate the PamelaLouisVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPamelaLouisVi = pamelaLouisViMapper.toEntity(returnedPamelaLouisViDTO);
        assertPamelaLouisViUpdatableFieldsEquals(returnedPamelaLouisVi, getPersistedPamelaLouisVi(returnedPamelaLouisVi));

        insertedPamelaLouisVi = returnedPamelaLouisVi;
    }

    @Test
    @Transactional
    void createPamelaLouisViWithExistingId() throws Exception {
        // Create the PamelaLouisVi with an existing ID
        pamelaLouisVi.setId(1L);
        PamelaLouisViDTO pamelaLouisViDTO = pamelaLouisViMapper.toDto(pamelaLouisVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPamelaLouisViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pamelaLouisViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PamelaLouisVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pamelaLouisVi.setName(null);

        // Create the PamelaLouisVi, which fails.
        PamelaLouisViDTO pamelaLouisViDTO = pamelaLouisViMapper.toDto(pamelaLouisVi);

        restPamelaLouisViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pamelaLouisViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPamelaLouisVis() throws Exception {
        // Initialize the database
        insertedPamelaLouisVi = pamelaLouisViRepository.saveAndFlush(pamelaLouisVi);

        // Get all the pamelaLouisViList
        restPamelaLouisViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pamelaLouisVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].configJason").value(hasItem(DEFAULT_CONFIG_JASON)));
    }

    @Test
    @Transactional
    void getPamelaLouisVi() throws Exception {
        // Initialize the database
        insertedPamelaLouisVi = pamelaLouisViRepository.saveAndFlush(pamelaLouisVi);

        // Get the pamelaLouisVi
        restPamelaLouisViMockMvc
            .perform(get(ENTITY_API_URL_ID, pamelaLouisVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pamelaLouisVi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.configJason").value(DEFAULT_CONFIG_JASON));
    }

    @Test
    @Transactional
    void getPamelaLouisVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedPamelaLouisVi = pamelaLouisViRepository.saveAndFlush(pamelaLouisVi);

        Long id = pamelaLouisVi.getId();

        defaultPamelaLouisViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPamelaLouisViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPamelaLouisViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPamelaLouisVisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPamelaLouisVi = pamelaLouisViRepository.saveAndFlush(pamelaLouisVi);

        // Get all the pamelaLouisViList where name equals to
        defaultPamelaLouisViFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPamelaLouisVisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPamelaLouisVi = pamelaLouisViRepository.saveAndFlush(pamelaLouisVi);

        // Get all the pamelaLouisViList where name in
        defaultPamelaLouisViFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPamelaLouisVisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPamelaLouisVi = pamelaLouisViRepository.saveAndFlush(pamelaLouisVi);

        // Get all the pamelaLouisViList where name is not null
        defaultPamelaLouisViFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllPamelaLouisVisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedPamelaLouisVi = pamelaLouisViRepository.saveAndFlush(pamelaLouisVi);

        // Get all the pamelaLouisViList where name contains
        defaultPamelaLouisViFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPamelaLouisVisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPamelaLouisVi = pamelaLouisViRepository.saveAndFlush(pamelaLouisVi);

        // Get all the pamelaLouisViList where name does not contain
        defaultPamelaLouisViFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllPamelaLouisVisByConfigJasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPamelaLouisVi = pamelaLouisViRepository.saveAndFlush(pamelaLouisVi);

        // Get all the pamelaLouisViList where configJason equals to
        defaultPamelaLouisViFiltering("configJason.equals=" + DEFAULT_CONFIG_JASON, "configJason.equals=" + UPDATED_CONFIG_JASON);
    }

    @Test
    @Transactional
    void getAllPamelaLouisVisByConfigJasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPamelaLouisVi = pamelaLouisViRepository.saveAndFlush(pamelaLouisVi);

        // Get all the pamelaLouisViList where configJason in
        defaultPamelaLouisViFiltering(
            "configJason.in=" + DEFAULT_CONFIG_JASON + "," + UPDATED_CONFIG_JASON,
            "configJason.in=" + UPDATED_CONFIG_JASON
        );
    }

    @Test
    @Transactional
    void getAllPamelaLouisVisByConfigJasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPamelaLouisVi = pamelaLouisViRepository.saveAndFlush(pamelaLouisVi);

        // Get all the pamelaLouisViList where configJason is not null
        defaultPamelaLouisViFiltering("configJason.specified=true", "configJason.specified=false");
    }

    @Test
    @Transactional
    void getAllPamelaLouisVisByConfigJasonContainsSomething() throws Exception {
        // Initialize the database
        insertedPamelaLouisVi = pamelaLouisViRepository.saveAndFlush(pamelaLouisVi);

        // Get all the pamelaLouisViList where configJason contains
        defaultPamelaLouisViFiltering("configJason.contains=" + DEFAULT_CONFIG_JASON, "configJason.contains=" + UPDATED_CONFIG_JASON);
    }

    @Test
    @Transactional
    void getAllPamelaLouisVisByConfigJasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPamelaLouisVi = pamelaLouisViRepository.saveAndFlush(pamelaLouisVi);

        // Get all the pamelaLouisViList where configJason does not contain
        defaultPamelaLouisViFiltering(
            "configJason.doesNotContain=" + UPDATED_CONFIG_JASON,
            "configJason.doesNotContain=" + DEFAULT_CONFIG_JASON
        );
    }

    private void defaultPamelaLouisViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPamelaLouisViShouldBeFound(shouldBeFound);
        defaultPamelaLouisViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPamelaLouisViShouldBeFound(String filter) throws Exception {
        restPamelaLouisViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pamelaLouisVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].configJason").value(hasItem(DEFAULT_CONFIG_JASON)));

        // Check, that the count call also returns 1
        restPamelaLouisViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPamelaLouisViShouldNotBeFound(String filter) throws Exception {
        restPamelaLouisViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPamelaLouisViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPamelaLouisVi() throws Exception {
        // Get the pamelaLouisVi
        restPamelaLouisViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPamelaLouisVi() throws Exception {
        // Initialize the database
        insertedPamelaLouisVi = pamelaLouisViRepository.saveAndFlush(pamelaLouisVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pamelaLouisVi
        PamelaLouisVi updatedPamelaLouisVi = pamelaLouisViRepository.findById(pamelaLouisVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPamelaLouisVi are not directly saved in db
        em.detach(updatedPamelaLouisVi);
        updatedPamelaLouisVi.name(UPDATED_NAME).configJason(UPDATED_CONFIG_JASON);
        PamelaLouisViDTO pamelaLouisViDTO = pamelaLouisViMapper.toDto(updatedPamelaLouisVi);

        restPamelaLouisViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pamelaLouisViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pamelaLouisViDTO))
            )
            .andExpect(status().isOk());

        // Validate the PamelaLouisVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPamelaLouisViToMatchAllProperties(updatedPamelaLouisVi);
    }

    @Test
    @Transactional
    void putNonExistingPamelaLouisVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pamelaLouisVi.setId(longCount.incrementAndGet());

        // Create the PamelaLouisVi
        PamelaLouisViDTO pamelaLouisViDTO = pamelaLouisViMapper.toDto(pamelaLouisVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPamelaLouisViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pamelaLouisViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pamelaLouisViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PamelaLouisVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPamelaLouisVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pamelaLouisVi.setId(longCount.incrementAndGet());

        // Create the PamelaLouisVi
        PamelaLouisViDTO pamelaLouisViDTO = pamelaLouisViMapper.toDto(pamelaLouisVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPamelaLouisViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pamelaLouisViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PamelaLouisVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPamelaLouisVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pamelaLouisVi.setId(longCount.incrementAndGet());

        // Create the PamelaLouisVi
        PamelaLouisViDTO pamelaLouisViDTO = pamelaLouisViMapper.toDto(pamelaLouisVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPamelaLouisViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pamelaLouisViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PamelaLouisVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePamelaLouisViWithPatch() throws Exception {
        // Initialize the database
        insertedPamelaLouisVi = pamelaLouisViRepository.saveAndFlush(pamelaLouisVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pamelaLouisVi using partial update
        PamelaLouisVi partialUpdatedPamelaLouisVi = new PamelaLouisVi();
        partialUpdatedPamelaLouisVi.setId(pamelaLouisVi.getId());

        partialUpdatedPamelaLouisVi.name(UPDATED_NAME);

        restPamelaLouisViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPamelaLouisVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPamelaLouisVi))
            )
            .andExpect(status().isOk());

        // Validate the PamelaLouisVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPamelaLouisViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPamelaLouisVi, pamelaLouisVi),
            getPersistedPamelaLouisVi(pamelaLouisVi)
        );
    }

    @Test
    @Transactional
    void fullUpdatePamelaLouisViWithPatch() throws Exception {
        // Initialize the database
        insertedPamelaLouisVi = pamelaLouisViRepository.saveAndFlush(pamelaLouisVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pamelaLouisVi using partial update
        PamelaLouisVi partialUpdatedPamelaLouisVi = new PamelaLouisVi();
        partialUpdatedPamelaLouisVi.setId(pamelaLouisVi.getId());

        partialUpdatedPamelaLouisVi.name(UPDATED_NAME).configJason(UPDATED_CONFIG_JASON);

        restPamelaLouisViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPamelaLouisVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPamelaLouisVi))
            )
            .andExpect(status().isOk());

        // Validate the PamelaLouisVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPamelaLouisViUpdatableFieldsEquals(partialUpdatedPamelaLouisVi, getPersistedPamelaLouisVi(partialUpdatedPamelaLouisVi));
    }

    @Test
    @Transactional
    void patchNonExistingPamelaLouisVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pamelaLouisVi.setId(longCount.incrementAndGet());

        // Create the PamelaLouisVi
        PamelaLouisViDTO pamelaLouisViDTO = pamelaLouisViMapper.toDto(pamelaLouisVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPamelaLouisViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pamelaLouisViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pamelaLouisViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PamelaLouisVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPamelaLouisVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pamelaLouisVi.setId(longCount.incrementAndGet());

        // Create the PamelaLouisVi
        PamelaLouisViDTO pamelaLouisViDTO = pamelaLouisViMapper.toDto(pamelaLouisVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPamelaLouisViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pamelaLouisViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PamelaLouisVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPamelaLouisVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pamelaLouisVi.setId(longCount.incrementAndGet());

        // Create the PamelaLouisVi
        PamelaLouisViDTO pamelaLouisViDTO = pamelaLouisViMapper.toDto(pamelaLouisVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPamelaLouisViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pamelaLouisViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PamelaLouisVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePamelaLouisVi() throws Exception {
        // Initialize the database
        insertedPamelaLouisVi = pamelaLouisViRepository.saveAndFlush(pamelaLouisVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pamelaLouisVi
        restPamelaLouisViMockMvc
            .perform(delete(ENTITY_API_URL_ID, pamelaLouisVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pamelaLouisViRepository.count();
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

    protected PamelaLouisVi getPersistedPamelaLouisVi(PamelaLouisVi pamelaLouisVi) {
        return pamelaLouisViRepository.findById(pamelaLouisVi.getId()).orElseThrow();
    }

    protected void assertPersistedPamelaLouisViToMatchAllProperties(PamelaLouisVi expectedPamelaLouisVi) {
        assertPamelaLouisViAllPropertiesEquals(expectedPamelaLouisVi, getPersistedPamelaLouisVi(expectedPamelaLouisVi));
    }

    protected void assertPersistedPamelaLouisViToMatchUpdatableProperties(PamelaLouisVi expectedPamelaLouisVi) {
        assertPamelaLouisViAllUpdatablePropertiesEquals(expectedPamelaLouisVi, getPersistedPamelaLouisVi(expectedPamelaLouisVi));
    }
}
