package ai.realworld.web.rest;

import static ai.realworld.domain.SicilyUmetoViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.SicilyUmetoVi;
import ai.realworld.domain.enumeration.SitomutaType;
import ai.realworld.repository.SicilyUmetoViRepository;
import ai.realworld.service.dto.SicilyUmetoViDTO;
import ai.realworld.service.mapper.SicilyUmetoViMapper;
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
 * Integration tests for the {@link SicilyUmetoViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SicilyUmetoViResourceIT {

    private static final SitomutaType DEFAULT_TYPE = SitomutaType.LOGIN_FAILED;
    private static final SitomutaType UPDATED_TYPE = SitomutaType.LOGIN_SUCCESS;

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sicily-umeto-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SicilyUmetoViRepository sicilyUmetoViRepository;

    @Autowired
    private SicilyUmetoViMapper sicilyUmetoViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSicilyUmetoViMockMvc;

    private SicilyUmetoVi sicilyUmetoVi;

    private SicilyUmetoVi insertedSicilyUmetoVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SicilyUmetoVi createEntity() {
        return new SicilyUmetoVi().type(DEFAULT_TYPE).content(DEFAULT_CONTENT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SicilyUmetoVi createUpdatedEntity() {
        return new SicilyUmetoVi().type(UPDATED_TYPE).content(UPDATED_CONTENT);
    }

    @BeforeEach
    public void initTest() {
        sicilyUmetoVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSicilyUmetoVi != null) {
            sicilyUmetoViRepository.delete(insertedSicilyUmetoVi);
            insertedSicilyUmetoVi = null;
        }
    }

    @Test
    @Transactional
    void createSicilyUmetoVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SicilyUmetoVi
        SicilyUmetoViDTO sicilyUmetoViDTO = sicilyUmetoViMapper.toDto(sicilyUmetoVi);
        var returnedSicilyUmetoViDTO = om.readValue(
            restSicilyUmetoViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sicilyUmetoViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SicilyUmetoViDTO.class
        );

        // Validate the SicilyUmetoVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSicilyUmetoVi = sicilyUmetoViMapper.toEntity(returnedSicilyUmetoViDTO);
        assertSicilyUmetoViUpdatableFieldsEquals(returnedSicilyUmetoVi, getPersistedSicilyUmetoVi(returnedSicilyUmetoVi));

        insertedSicilyUmetoVi = returnedSicilyUmetoVi;
    }

    @Test
    @Transactional
    void createSicilyUmetoViWithExistingId() throws Exception {
        // Create the SicilyUmetoVi with an existing ID
        sicilyUmetoVi.setId(1L);
        SicilyUmetoViDTO sicilyUmetoViDTO = sicilyUmetoViMapper.toDto(sicilyUmetoVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSicilyUmetoViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sicilyUmetoViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SicilyUmetoVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSicilyUmetoVis() throws Exception {
        // Initialize the database
        insertedSicilyUmetoVi = sicilyUmetoViRepository.saveAndFlush(sicilyUmetoVi);

        // Get all the sicilyUmetoViList
        restSicilyUmetoViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sicilyUmetoVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)));
    }

    @Test
    @Transactional
    void getSicilyUmetoVi() throws Exception {
        // Initialize the database
        insertedSicilyUmetoVi = sicilyUmetoViRepository.saveAndFlush(sicilyUmetoVi);

        // Get the sicilyUmetoVi
        restSicilyUmetoViMockMvc
            .perform(get(ENTITY_API_URL_ID, sicilyUmetoVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sicilyUmetoVi.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT));
    }

    @Test
    @Transactional
    void getSicilyUmetoVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedSicilyUmetoVi = sicilyUmetoViRepository.saveAndFlush(sicilyUmetoVi);

        Long id = sicilyUmetoVi.getId();

        defaultSicilyUmetoViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSicilyUmetoViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSicilyUmetoViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSicilyUmetoVisByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSicilyUmetoVi = sicilyUmetoViRepository.saveAndFlush(sicilyUmetoVi);

        // Get all the sicilyUmetoViList where type equals to
        defaultSicilyUmetoViFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllSicilyUmetoVisByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSicilyUmetoVi = sicilyUmetoViRepository.saveAndFlush(sicilyUmetoVi);

        // Get all the sicilyUmetoViList where type in
        defaultSicilyUmetoViFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllSicilyUmetoVisByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSicilyUmetoVi = sicilyUmetoViRepository.saveAndFlush(sicilyUmetoVi);

        // Get all the sicilyUmetoViList where type is not null
        defaultSicilyUmetoViFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllSicilyUmetoVisByContentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSicilyUmetoVi = sicilyUmetoViRepository.saveAndFlush(sicilyUmetoVi);

        // Get all the sicilyUmetoViList where content equals to
        defaultSicilyUmetoViFiltering("content.equals=" + DEFAULT_CONTENT, "content.equals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllSicilyUmetoVisByContentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSicilyUmetoVi = sicilyUmetoViRepository.saveAndFlush(sicilyUmetoVi);

        // Get all the sicilyUmetoViList where content in
        defaultSicilyUmetoViFiltering("content.in=" + DEFAULT_CONTENT + "," + UPDATED_CONTENT, "content.in=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllSicilyUmetoVisByContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSicilyUmetoVi = sicilyUmetoViRepository.saveAndFlush(sicilyUmetoVi);

        // Get all the sicilyUmetoViList where content is not null
        defaultSicilyUmetoViFiltering("content.specified=true", "content.specified=false");
    }

    @Test
    @Transactional
    void getAllSicilyUmetoVisByContentContainsSomething() throws Exception {
        // Initialize the database
        insertedSicilyUmetoVi = sicilyUmetoViRepository.saveAndFlush(sicilyUmetoVi);

        // Get all the sicilyUmetoViList where content contains
        defaultSicilyUmetoViFiltering("content.contains=" + DEFAULT_CONTENT, "content.contains=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllSicilyUmetoVisByContentNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSicilyUmetoVi = sicilyUmetoViRepository.saveAndFlush(sicilyUmetoVi);

        // Get all the sicilyUmetoViList where content does not contain
        defaultSicilyUmetoViFiltering("content.doesNotContain=" + UPDATED_CONTENT, "content.doesNotContain=" + DEFAULT_CONTENT);
    }

    private void defaultSicilyUmetoViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSicilyUmetoViShouldBeFound(shouldBeFound);
        defaultSicilyUmetoViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSicilyUmetoViShouldBeFound(String filter) throws Exception {
        restSicilyUmetoViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sicilyUmetoVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)));

        // Check, that the count call also returns 1
        restSicilyUmetoViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSicilyUmetoViShouldNotBeFound(String filter) throws Exception {
        restSicilyUmetoViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSicilyUmetoViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSicilyUmetoVi() throws Exception {
        // Get the sicilyUmetoVi
        restSicilyUmetoViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSicilyUmetoVi() throws Exception {
        // Initialize the database
        insertedSicilyUmetoVi = sicilyUmetoViRepository.saveAndFlush(sicilyUmetoVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sicilyUmetoVi
        SicilyUmetoVi updatedSicilyUmetoVi = sicilyUmetoViRepository.findById(sicilyUmetoVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSicilyUmetoVi are not directly saved in db
        em.detach(updatedSicilyUmetoVi);
        updatedSicilyUmetoVi.type(UPDATED_TYPE).content(UPDATED_CONTENT);
        SicilyUmetoViDTO sicilyUmetoViDTO = sicilyUmetoViMapper.toDto(updatedSicilyUmetoVi);

        restSicilyUmetoViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sicilyUmetoViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sicilyUmetoViDTO))
            )
            .andExpect(status().isOk());

        // Validate the SicilyUmetoVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSicilyUmetoViToMatchAllProperties(updatedSicilyUmetoVi);
    }

    @Test
    @Transactional
    void putNonExistingSicilyUmetoVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sicilyUmetoVi.setId(longCount.incrementAndGet());

        // Create the SicilyUmetoVi
        SicilyUmetoViDTO sicilyUmetoViDTO = sicilyUmetoViMapper.toDto(sicilyUmetoVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSicilyUmetoViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sicilyUmetoViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sicilyUmetoViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SicilyUmetoVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSicilyUmetoVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sicilyUmetoVi.setId(longCount.incrementAndGet());

        // Create the SicilyUmetoVi
        SicilyUmetoViDTO sicilyUmetoViDTO = sicilyUmetoViMapper.toDto(sicilyUmetoVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSicilyUmetoViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sicilyUmetoViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SicilyUmetoVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSicilyUmetoVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sicilyUmetoVi.setId(longCount.incrementAndGet());

        // Create the SicilyUmetoVi
        SicilyUmetoViDTO sicilyUmetoViDTO = sicilyUmetoViMapper.toDto(sicilyUmetoVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSicilyUmetoViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sicilyUmetoViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SicilyUmetoVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSicilyUmetoViWithPatch() throws Exception {
        // Initialize the database
        insertedSicilyUmetoVi = sicilyUmetoViRepository.saveAndFlush(sicilyUmetoVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sicilyUmetoVi using partial update
        SicilyUmetoVi partialUpdatedSicilyUmetoVi = new SicilyUmetoVi();
        partialUpdatedSicilyUmetoVi.setId(sicilyUmetoVi.getId());

        partialUpdatedSicilyUmetoVi.content(UPDATED_CONTENT);

        restSicilyUmetoViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSicilyUmetoVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSicilyUmetoVi))
            )
            .andExpect(status().isOk());

        // Validate the SicilyUmetoVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSicilyUmetoViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSicilyUmetoVi, sicilyUmetoVi),
            getPersistedSicilyUmetoVi(sicilyUmetoVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateSicilyUmetoViWithPatch() throws Exception {
        // Initialize the database
        insertedSicilyUmetoVi = sicilyUmetoViRepository.saveAndFlush(sicilyUmetoVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sicilyUmetoVi using partial update
        SicilyUmetoVi partialUpdatedSicilyUmetoVi = new SicilyUmetoVi();
        partialUpdatedSicilyUmetoVi.setId(sicilyUmetoVi.getId());

        partialUpdatedSicilyUmetoVi.type(UPDATED_TYPE).content(UPDATED_CONTENT);

        restSicilyUmetoViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSicilyUmetoVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSicilyUmetoVi))
            )
            .andExpect(status().isOk());

        // Validate the SicilyUmetoVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSicilyUmetoViUpdatableFieldsEquals(partialUpdatedSicilyUmetoVi, getPersistedSicilyUmetoVi(partialUpdatedSicilyUmetoVi));
    }

    @Test
    @Transactional
    void patchNonExistingSicilyUmetoVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sicilyUmetoVi.setId(longCount.incrementAndGet());

        // Create the SicilyUmetoVi
        SicilyUmetoViDTO sicilyUmetoViDTO = sicilyUmetoViMapper.toDto(sicilyUmetoVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSicilyUmetoViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sicilyUmetoViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sicilyUmetoViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SicilyUmetoVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSicilyUmetoVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sicilyUmetoVi.setId(longCount.incrementAndGet());

        // Create the SicilyUmetoVi
        SicilyUmetoViDTO sicilyUmetoViDTO = sicilyUmetoViMapper.toDto(sicilyUmetoVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSicilyUmetoViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sicilyUmetoViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SicilyUmetoVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSicilyUmetoVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sicilyUmetoVi.setId(longCount.incrementAndGet());

        // Create the SicilyUmetoVi
        SicilyUmetoViDTO sicilyUmetoViDTO = sicilyUmetoViMapper.toDto(sicilyUmetoVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSicilyUmetoViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(sicilyUmetoViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SicilyUmetoVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSicilyUmetoVi() throws Exception {
        // Initialize the database
        insertedSicilyUmetoVi = sicilyUmetoViRepository.saveAndFlush(sicilyUmetoVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the sicilyUmetoVi
        restSicilyUmetoViMockMvc
            .perform(delete(ENTITY_API_URL_ID, sicilyUmetoVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return sicilyUmetoViRepository.count();
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

    protected SicilyUmetoVi getPersistedSicilyUmetoVi(SicilyUmetoVi sicilyUmetoVi) {
        return sicilyUmetoViRepository.findById(sicilyUmetoVi.getId()).orElseThrow();
    }

    protected void assertPersistedSicilyUmetoViToMatchAllProperties(SicilyUmetoVi expectedSicilyUmetoVi) {
        assertSicilyUmetoViAllPropertiesEquals(expectedSicilyUmetoVi, getPersistedSicilyUmetoVi(expectedSicilyUmetoVi));
    }

    protected void assertPersistedSicilyUmetoViToMatchUpdatableProperties(SicilyUmetoVi expectedSicilyUmetoVi) {
        assertSicilyUmetoViAllUpdatablePropertiesEquals(expectedSicilyUmetoVi, getPersistedSicilyUmetoVi(expectedSicilyUmetoVi));
    }
}
