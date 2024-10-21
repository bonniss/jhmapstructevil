package ai.realworld.web.rest;

import static ai.realworld.domain.PamelaLouisAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.PamelaLouis;
import ai.realworld.repository.PamelaLouisRepository;
import ai.realworld.service.dto.PamelaLouisDTO;
import ai.realworld.service.mapper.PamelaLouisMapper;
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
 * Integration tests for the {@link PamelaLouisResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PamelaLouisResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIG_JASON = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_JASON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pamela-louis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PamelaLouisRepository pamelaLouisRepository;

    @Autowired
    private PamelaLouisMapper pamelaLouisMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPamelaLouisMockMvc;

    private PamelaLouis pamelaLouis;

    private PamelaLouis insertedPamelaLouis;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PamelaLouis createEntity() {
        return new PamelaLouis().name(DEFAULT_NAME).configJason(DEFAULT_CONFIG_JASON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PamelaLouis createUpdatedEntity() {
        return new PamelaLouis().name(UPDATED_NAME).configJason(UPDATED_CONFIG_JASON);
    }

    @BeforeEach
    public void initTest() {
        pamelaLouis = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPamelaLouis != null) {
            pamelaLouisRepository.delete(insertedPamelaLouis);
            insertedPamelaLouis = null;
        }
    }

    @Test
    @Transactional
    void createPamelaLouis() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PamelaLouis
        PamelaLouisDTO pamelaLouisDTO = pamelaLouisMapper.toDto(pamelaLouis);
        var returnedPamelaLouisDTO = om.readValue(
            restPamelaLouisMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pamelaLouisDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PamelaLouisDTO.class
        );

        // Validate the PamelaLouis in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPamelaLouis = pamelaLouisMapper.toEntity(returnedPamelaLouisDTO);
        assertPamelaLouisUpdatableFieldsEquals(returnedPamelaLouis, getPersistedPamelaLouis(returnedPamelaLouis));

        insertedPamelaLouis = returnedPamelaLouis;
    }

    @Test
    @Transactional
    void createPamelaLouisWithExistingId() throws Exception {
        // Create the PamelaLouis with an existing ID
        pamelaLouis.setId(1L);
        PamelaLouisDTO pamelaLouisDTO = pamelaLouisMapper.toDto(pamelaLouis);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPamelaLouisMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pamelaLouisDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PamelaLouis in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pamelaLouis.setName(null);

        // Create the PamelaLouis, which fails.
        PamelaLouisDTO pamelaLouisDTO = pamelaLouisMapper.toDto(pamelaLouis);

        restPamelaLouisMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pamelaLouisDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPamelaLouis() throws Exception {
        // Initialize the database
        insertedPamelaLouis = pamelaLouisRepository.saveAndFlush(pamelaLouis);

        // Get all the pamelaLouisList
        restPamelaLouisMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pamelaLouis.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].configJason").value(hasItem(DEFAULT_CONFIG_JASON)));
    }

    @Test
    @Transactional
    void getPamelaLouis() throws Exception {
        // Initialize the database
        insertedPamelaLouis = pamelaLouisRepository.saveAndFlush(pamelaLouis);

        // Get the pamelaLouis
        restPamelaLouisMockMvc
            .perform(get(ENTITY_API_URL_ID, pamelaLouis.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pamelaLouis.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.configJason").value(DEFAULT_CONFIG_JASON));
    }

    @Test
    @Transactional
    void getPamelaLouisByIdFiltering() throws Exception {
        // Initialize the database
        insertedPamelaLouis = pamelaLouisRepository.saveAndFlush(pamelaLouis);

        Long id = pamelaLouis.getId();

        defaultPamelaLouisFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPamelaLouisFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPamelaLouisFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPamelaLouisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPamelaLouis = pamelaLouisRepository.saveAndFlush(pamelaLouis);

        // Get all the pamelaLouisList where name equals to
        defaultPamelaLouisFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPamelaLouisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPamelaLouis = pamelaLouisRepository.saveAndFlush(pamelaLouis);

        // Get all the pamelaLouisList where name in
        defaultPamelaLouisFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPamelaLouisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPamelaLouis = pamelaLouisRepository.saveAndFlush(pamelaLouis);

        // Get all the pamelaLouisList where name is not null
        defaultPamelaLouisFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllPamelaLouisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedPamelaLouis = pamelaLouisRepository.saveAndFlush(pamelaLouis);

        // Get all the pamelaLouisList where name contains
        defaultPamelaLouisFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPamelaLouisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPamelaLouis = pamelaLouisRepository.saveAndFlush(pamelaLouis);

        // Get all the pamelaLouisList where name does not contain
        defaultPamelaLouisFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllPamelaLouisByConfigJasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPamelaLouis = pamelaLouisRepository.saveAndFlush(pamelaLouis);

        // Get all the pamelaLouisList where configJason equals to
        defaultPamelaLouisFiltering("configJason.equals=" + DEFAULT_CONFIG_JASON, "configJason.equals=" + UPDATED_CONFIG_JASON);
    }

    @Test
    @Transactional
    void getAllPamelaLouisByConfigJasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPamelaLouis = pamelaLouisRepository.saveAndFlush(pamelaLouis);

        // Get all the pamelaLouisList where configJason in
        defaultPamelaLouisFiltering(
            "configJason.in=" + DEFAULT_CONFIG_JASON + "," + UPDATED_CONFIG_JASON,
            "configJason.in=" + UPDATED_CONFIG_JASON
        );
    }

    @Test
    @Transactional
    void getAllPamelaLouisByConfigJasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPamelaLouis = pamelaLouisRepository.saveAndFlush(pamelaLouis);

        // Get all the pamelaLouisList where configJason is not null
        defaultPamelaLouisFiltering("configJason.specified=true", "configJason.specified=false");
    }

    @Test
    @Transactional
    void getAllPamelaLouisByConfigJasonContainsSomething() throws Exception {
        // Initialize the database
        insertedPamelaLouis = pamelaLouisRepository.saveAndFlush(pamelaLouis);

        // Get all the pamelaLouisList where configJason contains
        defaultPamelaLouisFiltering("configJason.contains=" + DEFAULT_CONFIG_JASON, "configJason.contains=" + UPDATED_CONFIG_JASON);
    }

    @Test
    @Transactional
    void getAllPamelaLouisByConfigJasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPamelaLouis = pamelaLouisRepository.saveAndFlush(pamelaLouis);

        // Get all the pamelaLouisList where configJason does not contain
        defaultPamelaLouisFiltering(
            "configJason.doesNotContain=" + UPDATED_CONFIG_JASON,
            "configJason.doesNotContain=" + DEFAULT_CONFIG_JASON
        );
    }

    private void defaultPamelaLouisFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPamelaLouisShouldBeFound(shouldBeFound);
        defaultPamelaLouisShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPamelaLouisShouldBeFound(String filter) throws Exception {
        restPamelaLouisMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pamelaLouis.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].configJason").value(hasItem(DEFAULT_CONFIG_JASON)));

        // Check, that the count call also returns 1
        restPamelaLouisMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPamelaLouisShouldNotBeFound(String filter) throws Exception {
        restPamelaLouisMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPamelaLouisMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPamelaLouis() throws Exception {
        // Get the pamelaLouis
        restPamelaLouisMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPamelaLouis() throws Exception {
        // Initialize the database
        insertedPamelaLouis = pamelaLouisRepository.saveAndFlush(pamelaLouis);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pamelaLouis
        PamelaLouis updatedPamelaLouis = pamelaLouisRepository.findById(pamelaLouis.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPamelaLouis are not directly saved in db
        em.detach(updatedPamelaLouis);
        updatedPamelaLouis.name(UPDATED_NAME).configJason(UPDATED_CONFIG_JASON);
        PamelaLouisDTO pamelaLouisDTO = pamelaLouisMapper.toDto(updatedPamelaLouis);

        restPamelaLouisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pamelaLouisDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pamelaLouisDTO))
            )
            .andExpect(status().isOk());

        // Validate the PamelaLouis in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPamelaLouisToMatchAllProperties(updatedPamelaLouis);
    }

    @Test
    @Transactional
    void putNonExistingPamelaLouis() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pamelaLouis.setId(longCount.incrementAndGet());

        // Create the PamelaLouis
        PamelaLouisDTO pamelaLouisDTO = pamelaLouisMapper.toDto(pamelaLouis);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPamelaLouisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pamelaLouisDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pamelaLouisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PamelaLouis in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPamelaLouis() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pamelaLouis.setId(longCount.incrementAndGet());

        // Create the PamelaLouis
        PamelaLouisDTO pamelaLouisDTO = pamelaLouisMapper.toDto(pamelaLouis);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPamelaLouisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pamelaLouisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PamelaLouis in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPamelaLouis() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pamelaLouis.setId(longCount.incrementAndGet());

        // Create the PamelaLouis
        PamelaLouisDTO pamelaLouisDTO = pamelaLouisMapper.toDto(pamelaLouis);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPamelaLouisMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pamelaLouisDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PamelaLouis in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePamelaLouisWithPatch() throws Exception {
        // Initialize the database
        insertedPamelaLouis = pamelaLouisRepository.saveAndFlush(pamelaLouis);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pamelaLouis using partial update
        PamelaLouis partialUpdatedPamelaLouis = new PamelaLouis();
        partialUpdatedPamelaLouis.setId(pamelaLouis.getId());

        partialUpdatedPamelaLouis.configJason(UPDATED_CONFIG_JASON);

        restPamelaLouisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPamelaLouis.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPamelaLouis))
            )
            .andExpect(status().isOk());

        // Validate the PamelaLouis in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPamelaLouisUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPamelaLouis, pamelaLouis),
            getPersistedPamelaLouis(pamelaLouis)
        );
    }

    @Test
    @Transactional
    void fullUpdatePamelaLouisWithPatch() throws Exception {
        // Initialize the database
        insertedPamelaLouis = pamelaLouisRepository.saveAndFlush(pamelaLouis);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pamelaLouis using partial update
        PamelaLouis partialUpdatedPamelaLouis = new PamelaLouis();
        partialUpdatedPamelaLouis.setId(pamelaLouis.getId());

        partialUpdatedPamelaLouis.name(UPDATED_NAME).configJason(UPDATED_CONFIG_JASON);

        restPamelaLouisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPamelaLouis.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPamelaLouis))
            )
            .andExpect(status().isOk());

        // Validate the PamelaLouis in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPamelaLouisUpdatableFieldsEquals(partialUpdatedPamelaLouis, getPersistedPamelaLouis(partialUpdatedPamelaLouis));
    }

    @Test
    @Transactional
    void patchNonExistingPamelaLouis() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pamelaLouis.setId(longCount.incrementAndGet());

        // Create the PamelaLouis
        PamelaLouisDTO pamelaLouisDTO = pamelaLouisMapper.toDto(pamelaLouis);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPamelaLouisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pamelaLouisDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pamelaLouisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PamelaLouis in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPamelaLouis() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pamelaLouis.setId(longCount.incrementAndGet());

        // Create the PamelaLouis
        PamelaLouisDTO pamelaLouisDTO = pamelaLouisMapper.toDto(pamelaLouis);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPamelaLouisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pamelaLouisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PamelaLouis in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPamelaLouis() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pamelaLouis.setId(longCount.incrementAndGet());

        // Create the PamelaLouis
        PamelaLouisDTO pamelaLouisDTO = pamelaLouisMapper.toDto(pamelaLouis);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPamelaLouisMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pamelaLouisDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PamelaLouis in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePamelaLouis() throws Exception {
        // Initialize the database
        insertedPamelaLouis = pamelaLouisRepository.saveAndFlush(pamelaLouis);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pamelaLouis
        restPamelaLouisMockMvc
            .perform(delete(ENTITY_API_URL_ID, pamelaLouis.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pamelaLouisRepository.count();
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

    protected PamelaLouis getPersistedPamelaLouis(PamelaLouis pamelaLouis) {
        return pamelaLouisRepository.findById(pamelaLouis.getId()).orElseThrow();
    }

    protected void assertPersistedPamelaLouisToMatchAllProperties(PamelaLouis expectedPamelaLouis) {
        assertPamelaLouisAllPropertiesEquals(expectedPamelaLouis, getPersistedPamelaLouis(expectedPamelaLouis));
    }

    protected void assertPersistedPamelaLouisToMatchUpdatableProperties(PamelaLouis expectedPamelaLouis) {
        assertPamelaLouisAllUpdatablePropertiesEquals(expectedPamelaLouis, getPersistedPamelaLouis(expectedPamelaLouis));
    }
}
