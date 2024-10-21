package ai.realworld.web.rest;

import static ai.realworld.domain.SicilyUmetoAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.SicilyUmeto;
import ai.realworld.domain.enumeration.SitomutaType;
import ai.realworld.repository.SicilyUmetoRepository;
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
 * Integration tests for the {@link SicilyUmetoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SicilyUmetoResourceIT {

    private static final SitomutaType DEFAULT_TYPE = SitomutaType.LOGIN_FAILED;
    private static final SitomutaType UPDATED_TYPE = SitomutaType.LOGIN_SUCCESS;

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sicily-umetos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SicilyUmetoRepository sicilyUmetoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSicilyUmetoMockMvc;

    private SicilyUmeto sicilyUmeto;

    private SicilyUmeto insertedSicilyUmeto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SicilyUmeto createEntity() {
        return new SicilyUmeto().type(DEFAULT_TYPE).content(DEFAULT_CONTENT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SicilyUmeto createUpdatedEntity() {
        return new SicilyUmeto().type(UPDATED_TYPE).content(UPDATED_CONTENT);
    }

    @BeforeEach
    public void initTest() {
        sicilyUmeto = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSicilyUmeto != null) {
            sicilyUmetoRepository.delete(insertedSicilyUmeto);
            insertedSicilyUmeto = null;
        }
    }

    @Test
    @Transactional
    void createSicilyUmeto() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SicilyUmeto
        var returnedSicilyUmeto = om.readValue(
            restSicilyUmetoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sicilyUmeto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SicilyUmeto.class
        );

        // Validate the SicilyUmeto in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSicilyUmetoUpdatableFieldsEquals(returnedSicilyUmeto, getPersistedSicilyUmeto(returnedSicilyUmeto));

        insertedSicilyUmeto = returnedSicilyUmeto;
    }

    @Test
    @Transactional
    void createSicilyUmetoWithExistingId() throws Exception {
        // Create the SicilyUmeto with an existing ID
        sicilyUmeto.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSicilyUmetoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sicilyUmeto)))
            .andExpect(status().isBadRequest());

        // Validate the SicilyUmeto in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSicilyUmetos() throws Exception {
        // Initialize the database
        insertedSicilyUmeto = sicilyUmetoRepository.saveAndFlush(sicilyUmeto);

        // Get all the sicilyUmetoList
        restSicilyUmetoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sicilyUmeto.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)));
    }

    @Test
    @Transactional
    void getSicilyUmeto() throws Exception {
        // Initialize the database
        insertedSicilyUmeto = sicilyUmetoRepository.saveAndFlush(sicilyUmeto);

        // Get the sicilyUmeto
        restSicilyUmetoMockMvc
            .perform(get(ENTITY_API_URL_ID, sicilyUmeto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sicilyUmeto.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT));
    }

    @Test
    @Transactional
    void getSicilyUmetosByIdFiltering() throws Exception {
        // Initialize the database
        insertedSicilyUmeto = sicilyUmetoRepository.saveAndFlush(sicilyUmeto);

        Long id = sicilyUmeto.getId();

        defaultSicilyUmetoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSicilyUmetoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSicilyUmetoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSicilyUmetosByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSicilyUmeto = sicilyUmetoRepository.saveAndFlush(sicilyUmeto);

        // Get all the sicilyUmetoList where type equals to
        defaultSicilyUmetoFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllSicilyUmetosByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSicilyUmeto = sicilyUmetoRepository.saveAndFlush(sicilyUmeto);

        // Get all the sicilyUmetoList where type in
        defaultSicilyUmetoFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllSicilyUmetosByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSicilyUmeto = sicilyUmetoRepository.saveAndFlush(sicilyUmeto);

        // Get all the sicilyUmetoList where type is not null
        defaultSicilyUmetoFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllSicilyUmetosByContentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSicilyUmeto = sicilyUmetoRepository.saveAndFlush(sicilyUmeto);

        // Get all the sicilyUmetoList where content equals to
        defaultSicilyUmetoFiltering("content.equals=" + DEFAULT_CONTENT, "content.equals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllSicilyUmetosByContentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSicilyUmeto = sicilyUmetoRepository.saveAndFlush(sicilyUmeto);

        // Get all the sicilyUmetoList where content in
        defaultSicilyUmetoFiltering("content.in=" + DEFAULT_CONTENT + "," + UPDATED_CONTENT, "content.in=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllSicilyUmetosByContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSicilyUmeto = sicilyUmetoRepository.saveAndFlush(sicilyUmeto);

        // Get all the sicilyUmetoList where content is not null
        defaultSicilyUmetoFiltering("content.specified=true", "content.specified=false");
    }

    @Test
    @Transactional
    void getAllSicilyUmetosByContentContainsSomething() throws Exception {
        // Initialize the database
        insertedSicilyUmeto = sicilyUmetoRepository.saveAndFlush(sicilyUmeto);

        // Get all the sicilyUmetoList where content contains
        defaultSicilyUmetoFiltering("content.contains=" + DEFAULT_CONTENT, "content.contains=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllSicilyUmetosByContentNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSicilyUmeto = sicilyUmetoRepository.saveAndFlush(sicilyUmeto);

        // Get all the sicilyUmetoList where content does not contain
        defaultSicilyUmetoFiltering("content.doesNotContain=" + UPDATED_CONTENT, "content.doesNotContain=" + DEFAULT_CONTENT);
    }

    private void defaultSicilyUmetoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSicilyUmetoShouldBeFound(shouldBeFound);
        defaultSicilyUmetoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSicilyUmetoShouldBeFound(String filter) throws Exception {
        restSicilyUmetoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sicilyUmeto.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)));

        // Check, that the count call also returns 1
        restSicilyUmetoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSicilyUmetoShouldNotBeFound(String filter) throws Exception {
        restSicilyUmetoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSicilyUmetoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSicilyUmeto() throws Exception {
        // Get the sicilyUmeto
        restSicilyUmetoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSicilyUmeto() throws Exception {
        // Initialize the database
        insertedSicilyUmeto = sicilyUmetoRepository.saveAndFlush(sicilyUmeto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sicilyUmeto
        SicilyUmeto updatedSicilyUmeto = sicilyUmetoRepository.findById(sicilyUmeto.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSicilyUmeto are not directly saved in db
        em.detach(updatedSicilyUmeto);
        updatedSicilyUmeto.type(UPDATED_TYPE).content(UPDATED_CONTENT);

        restSicilyUmetoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSicilyUmeto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSicilyUmeto))
            )
            .andExpect(status().isOk());

        // Validate the SicilyUmeto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSicilyUmetoToMatchAllProperties(updatedSicilyUmeto);
    }

    @Test
    @Transactional
    void putNonExistingSicilyUmeto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sicilyUmeto.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSicilyUmetoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sicilyUmeto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sicilyUmeto))
            )
            .andExpect(status().isBadRequest());

        // Validate the SicilyUmeto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSicilyUmeto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sicilyUmeto.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSicilyUmetoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sicilyUmeto))
            )
            .andExpect(status().isBadRequest());

        // Validate the SicilyUmeto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSicilyUmeto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sicilyUmeto.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSicilyUmetoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sicilyUmeto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SicilyUmeto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSicilyUmetoWithPatch() throws Exception {
        // Initialize the database
        insertedSicilyUmeto = sicilyUmetoRepository.saveAndFlush(sicilyUmeto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sicilyUmeto using partial update
        SicilyUmeto partialUpdatedSicilyUmeto = new SicilyUmeto();
        partialUpdatedSicilyUmeto.setId(sicilyUmeto.getId());

        partialUpdatedSicilyUmeto.type(UPDATED_TYPE).content(UPDATED_CONTENT);

        restSicilyUmetoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSicilyUmeto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSicilyUmeto))
            )
            .andExpect(status().isOk());

        // Validate the SicilyUmeto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSicilyUmetoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSicilyUmeto, sicilyUmeto),
            getPersistedSicilyUmeto(sicilyUmeto)
        );
    }

    @Test
    @Transactional
    void fullUpdateSicilyUmetoWithPatch() throws Exception {
        // Initialize the database
        insertedSicilyUmeto = sicilyUmetoRepository.saveAndFlush(sicilyUmeto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sicilyUmeto using partial update
        SicilyUmeto partialUpdatedSicilyUmeto = new SicilyUmeto();
        partialUpdatedSicilyUmeto.setId(sicilyUmeto.getId());

        partialUpdatedSicilyUmeto.type(UPDATED_TYPE).content(UPDATED_CONTENT);

        restSicilyUmetoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSicilyUmeto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSicilyUmeto))
            )
            .andExpect(status().isOk());

        // Validate the SicilyUmeto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSicilyUmetoUpdatableFieldsEquals(partialUpdatedSicilyUmeto, getPersistedSicilyUmeto(partialUpdatedSicilyUmeto));
    }

    @Test
    @Transactional
    void patchNonExistingSicilyUmeto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sicilyUmeto.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSicilyUmetoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sicilyUmeto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sicilyUmeto))
            )
            .andExpect(status().isBadRequest());

        // Validate the SicilyUmeto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSicilyUmeto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sicilyUmeto.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSicilyUmetoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sicilyUmeto))
            )
            .andExpect(status().isBadRequest());

        // Validate the SicilyUmeto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSicilyUmeto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sicilyUmeto.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSicilyUmetoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(sicilyUmeto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SicilyUmeto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSicilyUmeto() throws Exception {
        // Initialize the database
        insertedSicilyUmeto = sicilyUmetoRepository.saveAndFlush(sicilyUmeto);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the sicilyUmeto
        restSicilyUmetoMockMvc
            .perform(delete(ENTITY_API_URL_ID, sicilyUmeto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return sicilyUmetoRepository.count();
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

    protected SicilyUmeto getPersistedSicilyUmeto(SicilyUmeto sicilyUmeto) {
        return sicilyUmetoRepository.findById(sicilyUmeto.getId()).orElseThrow();
    }

    protected void assertPersistedSicilyUmetoToMatchAllProperties(SicilyUmeto expectedSicilyUmeto) {
        assertSicilyUmetoAllPropertiesEquals(expectedSicilyUmeto, getPersistedSicilyUmeto(expectedSicilyUmeto));
    }

    protected void assertPersistedSicilyUmetoToMatchUpdatableProperties(SicilyUmeto expectedSicilyUmeto) {
        assertSicilyUmetoAllUpdatablePropertiesEquals(expectedSicilyUmeto, getPersistedSicilyUmeto(expectedSicilyUmeto));
    }
}
