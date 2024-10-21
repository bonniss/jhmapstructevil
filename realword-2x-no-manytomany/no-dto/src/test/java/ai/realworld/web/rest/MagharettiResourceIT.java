package ai.realworld.web.rest;

import static ai.realworld.domain.MagharettiAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.Magharetti;
import ai.realworld.domain.enumeration.MissisipiType;
import ai.realworld.repository.MagharettiRepository;
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
 * Integration tests for the {@link MagharettiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MagharettiResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final MissisipiType DEFAULT_TYPE = MissisipiType.CONTAINER;
    private static final MissisipiType UPDATED_TYPE = MissisipiType.WEIGHT;

    private static final String ENTITY_API_URL = "/api/magharettis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MagharettiRepository magharettiRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMagharettiMockMvc;

    private Magharetti magharetti;

    private Magharetti insertedMagharetti;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Magharetti createEntity() {
        return new Magharetti().name(DEFAULT_NAME).label(DEFAULT_LABEL).type(DEFAULT_TYPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Magharetti createUpdatedEntity() {
        return new Magharetti().name(UPDATED_NAME).label(UPDATED_LABEL).type(UPDATED_TYPE);
    }

    @BeforeEach
    public void initTest() {
        magharetti = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMagharetti != null) {
            magharettiRepository.delete(insertedMagharetti);
            insertedMagharetti = null;
        }
    }

    @Test
    @Transactional
    void createMagharetti() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Magharetti
        var returnedMagharetti = om.readValue(
            restMagharettiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(magharetti)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Magharetti.class
        );

        // Validate the Magharetti in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMagharettiUpdatableFieldsEquals(returnedMagharetti, getPersistedMagharetti(returnedMagharetti));

        insertedMagharetti = returnedMagharetti;
    }

    @Test
    @Transactional
    void createMagharettiWithExistingId() throws Exception {
        // Create the Magharetti with an existing ID
        magharetti.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMagharettiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(magharetti)))
            .andExpect(status().isBadRequest());

        // Validate the Magharetti in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        magharetti.setName(null);

        // Create the Magharetti, which fails.

        restMagharettiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(magharetti)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLabelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        magharetti.setLabel(null);

        // Create the Magharetti, which fails.

        restMagharettiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(magharetti)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMagharettis() throws Exception {
        // Initialize the database
        insertedMagharetti = magharettiRepository.saveAndFlush(magharetti);

        // Get all the magharettiList
        restMagharettiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(magharetti.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    void getMagharetti() throws Exception {
        // Initialize the database
        insertedMagharetti = magharettiRepository.saveAndFlush(magharetti);

        // Get the magharetti
        restMagharettiMockMvc
            .perform(get(ENTITY_API_URL_ID, magharetti.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(magharetti.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getMagharettisByIdFiltering() throws Exception {
        // Initialize the database
        insertedMagharetti = magharettiRepository.saveAndFlush(magharetti);

        Long id = magharetti.getId();

        defaultMagharettiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMagharettiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMagharettiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMagharettisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMagharetti = magharettiRepository.saveAndFlush(magharetti);

        // Get all the magharettiList where name equals to
        defaultMagharettiFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMagharettisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMagharetti = magharettiRepository.saveAndFlush(magharetti);

        // Get all the magharettiList where name in
        defaultMagharettiFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMagharettisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMagharetti = magharettiRepository.saveAndFlush(magharetti);

        // Get all the magharettiList where name is not null
        defaultMagharettiFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllMagharettisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedMagharetti = magharettiRepository.saveAndFlush(magharetti);

        // Get all the magharettiList where name contains
        defaultMagharettiFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMagharettisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMagharetti = magharettiRepository.saveAndFlush(magharetti);

        // Get all the magharettiList where name does not contain
        defaultMagharettiFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllMagharettisByLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMagharetti = magharettiRepository.saveAndFlush(magharetti);

        // Get all the magharettiList where label equals to
        defaultMagharettiFiltering("label.equals=" + DEFAULT_LABEL, "label.equals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllMagharettisByLabelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMagharetti = magharettiRepository.saveAndFlush(magharetti);

        // Get all the magharettiList where label in
        defaultMagharettiFiltering("label.in=" + DEFAULT_LABEL + "," + UPDATED_LABEL, "label.in=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllMagharettisByLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMagharetti = magharettiRepository.saveAndFlush(magharetti);

        // Get all the magharettiList where label is not null
        defaultMagharettiFiltering("label.specified=true", "label.specified=false");
    }

    @Test
    @Transactional
    void getAllMagharettisByLabelContainsSomething() throws Exception {
        // Initialize the database
        insertedMagharetti = magharettiRepository.saveAndFlush(magharetti);

        // Get all the magharettiList where label contains
        defaultMagharettiFiltering("label.contains=" + DEFAULT_LABEL, "label.contains=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllMagharettisByLabelNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMagharetti = magharettiRepository.saveAndFlush(magharetti);

        // Get all the magharettiList where label does not contain
        defaultMagharettiFiltering("label.doesNotContain=" + UPDATED_LABEL, "label.doesNotContain=" + DEFAULT_LABEL);
    }

    @Test
    @Transactional
    void getAllMagharettisByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMagharetti = magharettiRepository.saveAndFlush(magharetti);

        // Get all the magharettiList where type equals to
        defaultMagharettiFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllMagharettisByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMagharetti = magharettiRepository.saveAndFlush(magharetti);

        // Get all the magharettiList where type in
        defaultMagharettiFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllMagharettisByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMagharetti = magharettiRepository.saveAndFlush(magharetti);

        // Get all the magharettiList where type is not null
        defaultMagharettiFiltering("type.specified=true", "type.specified=false");
    }

    private void defaultMagharettiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMagharettiShouldBeFound(shouldBeFound);
        defaultMagharettiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMagharettiShouldBeFound(String filter) throws Exception {
        restMagharettiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(magharetti.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));

        // Check, that the count call also returns 1
        restMagharettiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMagharettiShouldNotBeFound(String filter) throws Exception {
        restMagharettiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMagharettiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMagharetti() throws Exception {
        // Get the magharetti
        restMagharettiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMagharetti() throws Exception {
        // Initialize the database
        insertedMagharetti = magharettiRepository.saveAndFlush(magharetti);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the magharetti
        Magharetti updatedMagharetti = magharettiRepository.findById(magharetti.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMagharetti are not directly saved in db
        em.detach(updatedMagharetti);
        updatedMagharetti.name(UPDATED_NAME).label(UPDATED_LABEL).type(UPDATED_TYPE);

        restMagharettiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMagharetti.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMagharetti))
            )
            .andExpect(status().isOk());

        // Validate the Magharetti in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMagharettiToMatchAllProperties(updatedMagharetti);
    }

    @Test
    @Transactional
    void putNonExistingMagharetti() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        magharetti.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMagharettiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, magharetti.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(magharetti))
            )
            .andExpect(status().isBadRequest());

        // Validate the Magharetti in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMagharetti() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        magharetti.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMagharettiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(magharetti))
            )
            .andExpect(status().isBadRequest());

        // Validate the Magharetti in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMagharetti() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        magharetti.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMagharettiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(magharetti)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Magharetti in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMagharettiWithPatch() throws Exception {
        // Initialize the database
        insertedMagharetti = magharettiRepository.saveAndFlush(magharetti);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the magharetti using partial update
        Magharetti partialUpdatedMagharetti = new Magharetti();
        partialUpdatedMagharetti.setId(magharetti.getId());

        partialUpdatedMagharetti.label(UPDATED_LABEL);

        restMagharettiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMagharetti.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMagharetti))
            )
            .andExpect(status().isOk());

        // Validate the Magharetti in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMagharettiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMagharetti, magharetti),
            getPersistedMagharetti(magharetti)
        );
    }

    @Test
    @Transactional
    void fullUpdateMagharettiWithPatch() throws Exception {
        // Initialize the database
        insertedMagharetti = magharettiRepository.saveAndFlush(magharetti);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the magharetti using partial update
        Magharetti partialUpdatedMagharetti = new Magharetti();
        partialUpdatedMagharetti.setId(magharetti.getId());

        partialUpdatedMagharetti.name(UPDATED_NAME).label(UPDATED_LABEL).type(UPDATED_TYPE);

        restMagharettiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMagharetti.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMagharetti))
            )
            .andExpect(status().isOk());

        // Validate the Magharetti in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMagharettiUpdatableFieldsEquals(partialUpdatedMagharetti, getPersistedMagharetti(partialUpdatedMagharetti));
    }

    @Test
    @Transactional
    void patchNonExistingMagharetti() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        magharetti.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMagharettiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, magharetti.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(magharetti))
            )
            .andExpect(status().isBadRequest());

        // Validate the Magharetti in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMagharetti() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        magharetti.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMagharettiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(magharetti))
            )
            .andExpect(status().isBadRequest());

        // Validate the Magharetti in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMagharetti() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        magharetti.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMagharettiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(magharetti)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Magharetti in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMagharetti() throws Exception {
        // Initialize the database
        insertedMagharetti = magharettiRepository.saveAndFlush(magharetti);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the magharetti
        restMagharettiMockMvc
            .perform(delete(ENTITY_API_URL_ID, magharetti.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return magharettiRepository.count();
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

    protected Magharetti getPersistedMagharetti(Magharetti magharetti) {
        return magharettiRepository.findById(magharetti.getId()).orElseThrow();
    }

    protected void assertPersistedMagharettiToMatchAllProperties(Magharetti expectedMagharetti) {
        assertMagharettiAllPropertiesEquals(expectedMagharetti, getPersistedMagharetti(expectedMagharetti));
    }

    protected void assertPersistedMagharettiToMatchUpdatableProperties(Magharetti expectedMagharetti) {
        assertMagharettiAllUpdatablePropertiesEquals(expectedMagharetti, getPersistedMagharetti(expectedMagharetti));
    }
}
