package ai.realworld.web.rest;

import static ai.realworld.domain.AlPowerShellAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlPounder;
import ai.realworld.domain.AlPowerShell;
import ai.realworld.domain.AlProPro;
import ai.realworld.domain.JohnLennon;
import ai.realworld.repository.AlPowerShellRepository;
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
 * Integration tests for the {@link AlPowerShellResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlPowerShellResourceIT {

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/al-power-shells";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlPowerShellRepository alPowerShellRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlPowerShellMockMvc;

    private AlPowerShell alPowerShell;

    private AlPowerShell insertedAlPowerShell;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPowerShell createEntity() {
        return new AlPowerShell().value(DEFAULT_VALUE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPowerShell createUpdatedEntity() {
        return new AlPowerShell().value(UPDATED_VALUE);
    }

    @BeforeEach
    public void initTest() {
        alPowerShell = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlPowerShell != null) {
            alPowerShellRepository.delete(insertedAlPowerShell);
            insertedAlPowerShell = null;
        }
    }

    @Test
    @Transactional
    void createAlPowerShell() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlPowerShell
        var returnedAlPowerShell = om.readValue(
            restAlPowerShellMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPowerShell)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlPowerShell.class
        );

        // Validate the AlPowerShell in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlPowerShellUpdatableFieldsEquals(returnedAlPowerShell, getPersistedAlPowerShell(returnedAlPowerShell));

        insertedAlPowerShell = returnedAlPowerShell;
    }

    @Test
    @Transactional
    void createAlPowerShellWithExistingId() throws Exception {
        // Create the AlPowerShell with an existing ID
        alPowerShell.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlPowerShellMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPowerShell)))
            .andExpect(status().isBadRequest());

        // Validate the AlPowerShell in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAlPowerShells() throws Exception {
        // Initialize the database
        insertedAlPowerShell = alPowerShellRepository.saveAndFlush(alPowerShell);

        // Get all the alPowerShellList
        restAlPowerShellMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPowerShell.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getAlPowerShell() throws Exception {
        // Initialize the database
        insertedAlPowerShell = alPowerShellRepository.saveAndFlush(alPowerShell);

        // Get the alPowerShell
        restAlPowerShellMockMvc
            .perform(get(ENTITY_API_URL_ID, alPowerShell.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alPowerShell.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getAlPowerShellsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlPowerShell = alPowerShellRepository.saveAndFlush(alPowerShell);

        Long id = alPowerShell.getId();

        defaultAlPowerShellFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlPowerShellFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlPowerShellFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlPowerShellsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPowerShell = alPowerShellRepository.saveAndFlush(alPowerShell);

        // Get all the alPowerShellList where value equals to
        defaultAlPowerShellFiltering("value.equals=" + DEFAULT_VALUE, "value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllAlPowerShellsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPowerShell = alPowerShellRepository.saveAndFlush(alPowerShell);

        // Get all the alPowerShellList where value in
        defaultAlPowerShellFiltering("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE, "value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllAlPowerShellsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPowerShell = alPowerShellRepository.saveAndFlush(alPowerShell);

        // Get all the alPowerShellList where value is not null
        defaultAlPowerShellFiltering("value.specified=true", "value.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPowerShellsByValueContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPowerShell = alPowerShellRepository.saveAndFlush(alPowerShell);

        // Get all the alPowerShellList where value contains
        defaultAlPowerShellFiltering("value.contains=" + DEFAULT_VALUE, "value.contains=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllAlPowerShellsByValueNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPowerShell = alPowerShellRepository.saveAndFlush(alPowerShell);

        // Get all the alPowerShellList where value does not contain
        defaultAlPowerShellFiltering("value.doesNotContain=" + UPDATED_VALUE, "value.doesNotContain=" + DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void getAllAlPowerShellsByPropertyProfileIsEqualToSomething() throws Exception {
        AlProPro propertyProfile;
        if (TestUtil.findAll(em, AlProPro.class).isEmpty()) {
            alPowerShellRepository.saveAndFlush(alPowerShell);
            propertyProfile = AlProProResourceIT.createEntity();
        } else {
            propertyProfile = TestUtil.findAll(em, AlProPro.class).get(0);
        }
        em.persist(propertyProfile);
        em.flush();
        alPowerShell.setPropertyProfile(propertyProfile);
        alPowerShellRepository.saveAndFlush(alPowerShell);
        UUID propertyProfileId = propertyProfile.getId();
        // Get all the alPowerShellList where propertyProfile equals to propertyProfileId
        defaultAlPowerShellShouldBeFound("propertyProfileId.equals=" + propertyProfileId);

        // Get all the alPowerShellList where propertyProfile equals to UUID.randomUUID()
        defaultAlPowerShellShouldNotBeFound("propertyProfileId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlPowerShellsByAttributeTermIsEqualToSomething() throws Exception {
        AlPounder attributeTerm;
        if (TestUtil.findAll(em, AlPounder.class).isEmpty()) {
            alPowerShellRepository.saveAndFlush(alPowerShell);
            attributeTerm = AlPounderResourceIT.createEntity(em);
        } else {
            attributeTerm = TestUtil.findAll(em, AlPounder.class).get(0);
        }
        em.persist(attributeTerm);
        em.flush();
        alPowerShell.setAttributeTerm(attributeTerm);
        alPowerShellRepository.saveAndFlush(alPowerShell);
        Long attributeTermId = attributeTerm.getId();
        // Get all the alPowerShellList where attributeTerm equals to attributeTermId
        defaultAlPowerShellShouldBeFound("attributeTermId.equals=" + attributeTermId);

        // Get all the alPowerShellList where attributeTerm equals to (attributeTermId + 1)
        defaultAlPowerShellShouldNotBeFound("attributeTermId.equals=" + (attributeTermId + 1));
    }

    @Test
    @Transactional
    void getAllAlPowerShellsByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alPowerShellRepository.saveAndFlush(alPowerShell);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alPowerShell.setApplication(application);
        alPowerShellRepository.saveAndFlush(alPowerShell);
        UUID applicationId = application.getId();
        // Get all the alPowerShellList where application equals to applicationId
        defaultAlPowerShellShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alPowerShellList where application equals to UUID.randomUUID()
        defaultAlPowerShellShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlPowerShellFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlPowerShellShouldBeFound(shouldBeFound);
        defaultAlPowerShellShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlPowerShellShouldBeFound(String filter) throws Exception {
        restAlPowerShellMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPowerShell.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));

        // Check, that the count call also returns 1
        restAlPowerShellMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlPowerShellShouldNotBeFound(String filter) throws Exception {
        restAlPowerShellMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlPowerShellMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlPowerShell() throws Exception {
        // Get the alPowerShell
        restAlPowerShellMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlPowerShell() throws Exception {
        // Initialize the database
        insertedAlPowerShell = alPowerShellRepository.saveAndFlush(alPowerShell);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPowerShell
        AlPowerShell updatedAlPowerShell = alPowerShellRepository.findById(alPowerShell.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlPowerShell are not directly saved in db
        em.detach(updatedAlPowerShell);
        updatedAlPowerShell.value(UPDATED_VALUE);

        restAlPowerShellMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlPowerShell.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlPowerShell))
            )
            .andExpect(status().isOk());

        // Validate the AlPowerShell in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlPowerShellToMatchAllProperties(updatedAlPowerShell);
    }

    @Test
    @Transactional
    void putNonExistingAlPowerShell() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPowerShell.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPowerShellMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alPowerShell.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPowerShell))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPowerShell in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlPowerShell() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPowerShell.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPowerShellMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPowerShell))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPowerShell in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlPowerShell() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPowerShell.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPowerShellMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPowerShell)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPowerShell in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlPowerShellWithPatch() throws Exception {
        // Initialize the database
        insertedAlPowerShell = alPowerShellRepository.saveAndFlush(alPowerShell);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPowerShell using partial update
        AlPowerShell partialUpdatedAlPowerShell = new AlPowerShell();
        partialUpdatedAlPowerShell.setId(alPowerShell.getId());

        partialUpdatedAlPowerShell.value(UPDATED_VALUE);

        restAlPowerShellMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPowerShell.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPowerShell))
            )
            .andExpect(status().isOk());

        // Validate the AlPowerShell in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPowerShellUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlPowerShell, alPowerShell),
            getPersistedAlPowerShell(alPowerShell)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlPowerShellWithPatch() throws Exception {
        // Initialize the database
        insertedAlPowerShell = alPowerShellRepository.saveAndFlush(alPowerShell);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPowerShell using partial update
        AlPowerShell partialUpdatedAlPowerShell = new AlPowerShell();
        partialUpdatedAlPowerShell.setId(alPowerShell.getId());

        partialUpdatedAlPowerShell.value(UPDATED_VALUE);

        restAlPowerShellMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPowerShell.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPowerShell))
            )
            .andExpect(status().isOk());

        // Validate the AlPowerShell in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPowerShellUpdatableFieldsEquals(partialUpdatedAlPowerShell, getPersistedAlPowerShell(partialUpdatedAlPowerShell));
    }

    @Test
    @Transactional
    void patchNonExistingAlPowerShell() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPowerShell.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPowerShellMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alPowerShell.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPowerShell))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPowerShell in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlPowerShell() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPowerShell.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPowerShellMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPowerShell))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPowerShell in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlPowerShell() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPowerShell.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPowerShellMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alPowerShell)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPowerShell in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlPowerShell() throws Exception {
        // Initialize the database
        insertedAlPowerShell = alPowerShellRepository.saveAndFlush(alPowerShell);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alPowerShell
        restAlPowerShellMockMvc
            .perform(delete(ENTITY_API_URL_ID, alPowerShell.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alPowerShellRepository.count();
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

    protected AlPowerShell getPersistedAlPowerShell(AlPowerShell alPowerShell) {
        return alPowerShellRepository.findById(alPowerShell.getId()).orElseThrow();
    }

    protected void assertPersistedAlPowerShellToMatchAllProperties(AlPowerShell expectedAlPowerShell) {
        assertAlPowerShellAllPropertiesEquals(expectedAlPowerShell, getPersistedAlPowerShell(expectedAlPowerShell));
    }

    protected void assertPersistedAlPowerShellToMatchUpdatableProperties(AlPowerShell expectedAlPowerShell) {
        assertAlPowerShellAllUpdatablePropertiesEquals(expectedAlPowerShell, getPersistedAlPowerShell(expectedAlPowerShell));
    }
}
