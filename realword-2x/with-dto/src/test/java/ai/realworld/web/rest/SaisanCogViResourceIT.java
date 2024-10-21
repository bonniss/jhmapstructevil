package ai.realworld.web.rest;

import static ai.realworld.domain.SaisanCogViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.SaisanCogVi;
import ai.realworld.repository.SaisanCogViRepository;
import ai.realworld.service.dto.SaisanCogViDTO;
import ai.realworld.service.mapper.SaisanCogViMapper;
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
 * Integration tests for the {@link SaisanCogViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SaisanCogViResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE_JASON = "AAAAAAAAAA";
    private static final String UPDATED_VALUE_JASON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/saisan-cog-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SaisanCogViRepository saisanCogViRepository;

    @Autowired
    private SaisanCogViMapper saisanCogViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSaisanCogViMockMvc;

    private SaisanCogVi saisanCogVi;

    private SaisanCogVi insertedSaisanCogVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SaisanCogVi createEntity() {
        return new SaisanCogVi().key(DEFAULT_KEY).valueJason(DEFAULT_VALUE_JASON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SaisanCogVi createUpdatedEntity() {
        return new SaisanCogVi().key(UPDATED_KEY).valueJason(UPDATED_VALUE_JASON);
    }

    @BeforeEach
    public void initTest() {
        saisanCogVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSaisanCogVi != null) {
            saisanCogViRepository.delete(insertedSaisanCogVi);
            insertedSaisanCogVi = null;
        }
    }

    @Test
    @Transactional
    void createSaisanCogVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SaisanCogVi
        SaisanCogViDTO saisanCogViDTO = saisanCogViMapper.toDto(saisanCogVi);
        var returnedSaisanCogViDTO = om.readValue(
            restSaisanCogViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saisanCogViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SaisanCogViDTO.class
        );

        // Validate the SaisanCogVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSaisanCogVi = saisanCogViMapper.toEntity(returnedSaisanCogViDTO);
        assertSaisanCogViUpdatableFieldsEquals(returnedSaisanCogVi, getPersistedSaisanCogVi(returnedSaisanCogVi));

        insertedSaisanCogVi = returnedSaisanCogVi;
    }

    @Test
    @Transactional
    void createSaisanCogViWithExistingId() throws Exception {
        // Create the SaisanCogVi with an existing ID
        saisanCogVi.setId(1L);
        SaisanCogViDTO saisanCogViDTO = saisanCogViMapper.toDto(saisanCogVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSaisanCogViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saisanCogViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SaisanCogVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkKeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        saisanCogVi.setKey(null);

        // Create the SaisanCogVi, which fails.
        SaisanCogViDTO saisanCogViDTO = saisanCogViMapper.toDto(saisanCogVi);

        restSaisanCogViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saisanCogViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSaisanCogVis() throws Exception {
        // Initialize the database
        insertedSaisanCogVi = saisanCogViRepository.saveAndFlush(saisanCogVi);

        // Get all the saisanCogViList
        restSaisanCogViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saisanCogVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].valueJason").value(hasItem(DEFAULT_VALUE_JASON)));
    }

    @Test
    @Transactional
    void getSaisanCogVi() throws Exception {
        // Initialize the database
        insertedSaisanCogVi = saisanCogViRepository.saveAndFlush(saisanCogVi);

        // Get the saisanCogVi
        restSaisanCogViMockMvc
            .perform(get(ENTITY_API_URL_ID, saisanCogVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(saisanCogVi.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.valueJason").value(DEFAULT_VALUE_JASON));
    }

    @Test
    @Transactional
    void getSaisanCogVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedSaisanCogVi = saisanCogViRepository.saveAndFlush(saisanCogVi);

        Long id = saisanCogVi.getId();

        defaultSaisanCogViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSaisanCogViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSaisanCogViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSaisanCogVisByKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSaisanCogVi = saisanCogViRepository.saveAndFlush(saisanCogVi);

        // Get all the saisanCogViList where key equals to
        defaultSaisanCogViFiltering("key.equals=" + DEFAULT_KEY, "key.equals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllSaisanCogVisByKeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSaisanCogVi = saisanCogViRepository.saveAndFlush(saisanCogVi);

        // Get all the saisanCogViList where key in
        defaultSaisanCogViFiltering("key.in=" + DEFAULT_KEY + "," + UPDATED_KEY, "key.in=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllSaisanCogVisByKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSaisanCogVi = saisanCogViRepository.saveAndFlush(saisanCogVi);

        // Get all the saisanCogViList where key is not null
        defaultSaisanCogViFiltering("key.specified=true", "key.specified=false");
    }

    @Test
    @Transactional
    void getAllSaisanCogVisByKeyContainsSomething() throws Exception {
        // Initialize the database
        insertedSaisanCogVi = saisanCogViRepository.saveAndFlush(saisanCogVi);

        // Get all the saisanCogViList where key contains
        defaultSaisanCogViFiltering("key.contains=" + DEFAULT_KEY, "key.contains=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllSaisanCogVisByKeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSaisanCogVi = saisanCogViRepository.saveAndFlush(saisanCogVi);

        // Get all the saisanCogViList where key does not contain
        defaultSaisanCogViFiltering("key.doesNotContain=" + UPDATED_KEY, "key.doesNotContain=" + DEFAULT_KEY);
    }

    @Test
    @Transactional
    void getAllSaisanCogVisByValueJasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSaisanCogVi = saisanCogViRepository.saveAndFlush(saisanCogVi);

        // Get all the saisanCogViList where valueJason equals to
        defaultSaisanCogViFiltering("valueJason.equals=" + DEFAULT_VALUE_JASON, "valueJason.equals=" + UPDATED_VALUE_JASON);
    }

    @Test
    @Transactional
    void getAllSaisanCogVisByValueJasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSaisanCogVi = saisanCogViRepository.saveAndFlush(saisanCogVi);

        // Get all the saisanCogViList where valueJason in
        defaultSaisanCogViFiltering(
            "valueJason.in=" + DEFAULT_VALUE_JASON + "," + UPDATED_VALUE_JASON,
            "valueJason.in=" + UPDATED_VALUE_JASON
        );
    }

    @Test
    @Transactional
    void getAllSaisanCogVisByValueJasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSaisanCogVi = saisanCogViRepository.saveAndFlush(saisanCogVi);

        // Get all the saisanCogViList where valueJason is not null
        defaultSaisanCogViFiltering("valueJason.specified=true", "valueJason.specified=false");
    }

    @Test
    @Transactional
    void getAllSaisanCogVisByValueJasonContainsSomething() throws Exception {
        // Initialize the database
        insertedSaisanCogVi = saisanCogViRepository.saveAndFlush(saisanCogVi);

        // Get all the saisanCogViList where valueJason contains
        defaultSaisanCogViFiltering("valueJason.contains=" + DEFAULT_VALUE_JASON, "valueJason.contains=" + UPDATED_VALUE_JASON);
    }

    @Test
    @Transactional
    void getAllSaisanCogVisByValueJasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSaisanCogVi = saisanCogViRepository.saveAndFlush(saisanCogVi);

        // Get all the saisanCogViList where valueJason does not contain
        defaultSaisanCogViFiltering("valueJason.doesNotContain=" + UPDATED_VALUE_JASON, "valueJason.doesNotContain=" + DEFAULT_VALUE_JASON);
    }

    private void defaultSaisanCogViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSaisanCogViShouldBeFound(shouldBeFound);
        defaultSaisanCogViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSaisanCogViShouldBeFound(String filter) throws Exception {
        restSaisanCogViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saisanCogVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].valueJason").value(hasItem(DEFAULT_VALUE_JASON)));

        // Check, that the count call also returns 1
        restSaisanCogViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSaisanCogViShouldNotBeFound(String filter) throws Exception {
        restSaisanCogViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSaisanCogViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSaisanCogVi() throws Exception {
        // Get the saisanCogVi
        restSaisanCogViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSaisanCogVi() throws Exception {
        // Initialize the database
        insertedSaisanCogVi = saisanCogViRepository.saveAndFlush(saisanCogVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the saisanCogVi
        SaisanCogVi updatedSaisanCogVi = saisanCogViRepository.findById(saisanCogVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSaisanCogVi are not directly saved in db
        em.detach(updatedSaisanCogVi);
        updatedSaisanCogVi.key(UPDATED_KEY).valueJason(UPDATED_VALUE_JASON);
        SaisanCogViDTO saisanCogViDTO = saisanCogViMapper.toDto(updatedSaisanCogVi);

        restSaisanCogViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, saisanCogViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(saisanCogViDTO))
            )
            .andExpect(status().isOk());

        // Validate the SaisanCogVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSaisanCogViToMatchAllProperties(updatedSaisanCogVi);
    }

    @Test
    @Transactional
    void putNonExistingSaisanCogVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saisanCogVi.setId(longCount.incrementAndGet());

        // Create the SaisanCogVi
        SaisanCogViDTO saisanCogViDTO = saisanCogViMapper.toDto(saisanCogVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSaisanCogViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, saisanCogViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(saisanCogViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SaisanCogVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSaisanCogVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saisanCogVi.setId(longCount.incrementAndGet());

        // Create the SaisanCogVi
        SaisanCogViDTO saisanCogViDTO = saisanCogViMapper.toDto(saisanCogVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaisanCogViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(saisanCogViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SaisanCogVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSaisanCogVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saisanCogVi.setId(longCount.incrementAndGet());

        // Create the SaisanCogVi
        SaisanCogViDTO saisanCogViDTO = saisanCogViMapper.toDto(saisanCogVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaisanCogViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saisanCogViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SaisanCogVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSaisanCogViWithPatch() throws Exception {
        // Initialize the database
        insertedSaisanCogVi = saisanCogViRepository.saveAndFlush(saisanCogVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the saisanCogVi using partial update
        SaisanCogVi partialUpdatedSaisanCogVi = new SaisanCogVi();
        partialUpdatedSaisanCogVi.setId(saisanCogVi.getId());

        restSaisanCogViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSaisanCogVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSaisanCogVi))
            )
            .andExpect(status().isOk());

        // Validate the SaisanCogVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSaisanCogViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSaisanCogVi, saisanCogVi),
            getPersistedSaisanCogVi(saisanCogVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateSaisanCogViWithPatch() throws Exception {
        // Initialize the database
        insertedSaisanCogVi = saisanCogViRepository.saveAndFlush(saisanCogVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the saisanCogVi using partial update
        SaisanCogVi partialUpdatedSaisanCogVi = new SaisanCogVi();
        partialUpdatedSaisanCogVi.setId(saisanCogVi.getId());

        partialUpdatedSaisanCogVi.key(UPDATED_KEY).valueJason(UPDATED_VALUE_JASON);

        restSaisanCogViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSaisanCogVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSaisanCogVi))
            )
            .andExpect(status().isOk());

        // Validate the SaisanCogVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSaisanCogViUpdatableFieldsEquals(partialUpdatedSaisanCogVi, getPersistedSaisanCogVi(partialUpdatedSaisanCogVi));
    }

    @Test
    @Transactional
    void patchNonExistingSaisanCogVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saisanCogVi.setId(longCount.incrementAndGet());

        // Create the SaisanCogVi
        SaisanCogViDTO saisanCogViDTO = saisanCogViMapper.toDto(saisanCogVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSaisanCogViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, saisanCogViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(saisanCogViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SaisanCogVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSaisanCogVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saisanCogVi.setId(longCount.incrementAndGet());

        // Create the SaisanCogVi
        SaisanCogViDTO saisanCogViDTO = saisanCogViMapper.toDto(saisanCogVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaisanCogViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(saisanCogViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SaisanCogVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSaisanCogVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saisanCogVi.setId(longCount.incrementAndGet());

        // Create the SaisanCogVi
        SaisanCogViDTO saisanCogViDTO = saisanCogViMapper.toDto(saisanCogVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaisanCogViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(saisanCogViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SaisanCogVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSaisanCogVi() throws Exception {
        // Initialize the database
        insertedSaisanCogVi = saisanCogViRepository.saveAndFlush(saisanCogVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the saisanCogVi
        restSaisanCogViMockMvc
            .perform(delete(ENTITY_API_URL_ID, saisanCogVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return saisanCogViRepository.count();
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

    protected SaisanCogVi getPersistedSaisanCogVi(SaisanCogVi saisanCogVi) {
        return saisanCogViRepository.findById(saisanCogVi.getId()).orElseThrow();
    }

    protected void assertPersistedSaisanCogViToMatchAllProperties(SaisanCogVi expectedSaisanCogVi) {
        assertSaisanCogViAllPropertiesEquals(expectedSaisanCogVi, getPersistedSaisanCogVi(expectedSaisanCogVi));
    }

    protected void assertPersistedSaisanCogViToMatchUpdatableProperties(SaisanCogVi expectedSaisanCogVi) {
        assertSaisanCogViAllUpdatablePropertiesEquals(expectedSaisanCogVi, getPersistedSaisanCogVi(expectedSaisanCogVi));
    }
}
