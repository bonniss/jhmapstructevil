package ai.realworld.web.rest;

import static ai.realworld.domain.AlPowerShellViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlPounderVi;
import ai.realworld.domain.AlPowerShellVi;
import ai.realworld.domain.AlProProVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.repository.AlPowerShellViRepository;
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
 * Integration tests for the {@link AlPowerShellViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlPowerShellViResourceIT {

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/al-power-shell-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlPowerShellViRepository alPowerShellViRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlPowerShellViMockMvc;

    private AlPowerShellVi alPowerShellVi;

    private AlPowerShellVi insertedAlPowerShellVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPowerShellVi createEntity() {
        return new AlPowerShellVi().value(DEFAULT_VALUE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPowerShellVi createUpdatedEntity() {
        return new AlPowerShellVi().value(UPDATED_VALUE);
    }

    @BeforeEach
    public void initTest() {
        alPowerShellVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlPowerShellVi != null) {
            alPowerShellViRepository.delete(insertedAlPowerShellVi);
            insertedAlPowerShellVi = null;
        }
    }

    @Test
    @Transactional
    void createAlPowerShellVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlPowerShellVi
        var returnedAlPowerShellVi = om.readValue(
            restAlPowerShellViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPowerShellVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlPowerShellVi.class
        );

        // Validate the AlPowerShellVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlPowerShellViUpdatableFieldsEquals(returnedAlPowerShellVi, getPersistedAlPowerShellVi(returnedAlPowerShellVi));

        insertedAlPowerShellVi = returnedAlPowerShellVi;
    }

    @Test
    @Transactional
    void createAlPowerShellViWithExistingId() throws Exception {
        // Create the AlPowerShellVi with an existing ID
        alPowerShellVi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlPowerShellViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPowerShellVi)))
            .andExpect(status().isBadRequest());

        // Validate the AlPowerShellVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAlPowerShellVis() throws Exception {
        // Initialize the database
        insertedAlPowerShellVi = alPowerShellViRepository.saveAndFlush(alPowerShellVi);

        // Get all the alPowerShellViList
        restAlPowerShellViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPowerShellVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getAlPowerShellVi() throws Exception {
        // Initialize the database
        insertedAlPowerShellVi = alPowerShellViRepository.saveAndFlush(alPowerShellVi);

        // Get the alPowerShellVi
        restAlPowerShellViMockMvc
            .perform(get(ENTITY_API_URL_ID, alPowerShellVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alPowerShellVi.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getAlPowerShellVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlPowerShellVi = alPowerShellViRepository.saveAndFlush(alPowerShellVi);

        Long id = alPowerShellVi.getId();

        defaultAlPowerShellViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlPowerShellViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlPowerShellViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlPowerShellVisByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPowerShellVi = alPowerShellViRepository.saveAndFlush(alPowerShellVi);

        // Get all the alPowerShellViList where value equals to
        defaultAlPowerShellViFiltering("value.equals=" + DEFAULT_VALUE, "value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllAlPowerShellVisByValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPowerShellVi = alPowerShellViRepository.saveAndFlush(alPowerShellVi);

        // Get all the alPowerShellViList where value in
        defaultAlPowerShellViFiltering("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE, "value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllAlPowerShellVisByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPowerShellVi = alPowerShellViRepository.saveAndFlush(alPowerShellVi);

        // Get all the alPowerShellViList where value is not null
        defaultAlPowerShellViFiltering("value.specified=true", "value.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPowerShellVisByValueContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPowerShellVi = alPowerShellViRepository.saveAndFlush(alPowerShellVi);

        // Get all the alPowerShellViList where value contains
        defaultAlPowerShellViFiltering("value.contains=" + DEFAULT_VALUE, "value.contains=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllAlPowerShellVisByValueNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPowerShellVi = alPowerShellViRepository.saveAndFlush(alPowerShellVi);

        // Get all the alPowerShellViList where value does not contain
        defaultAlPowerShellViFiltering("value.doesNotContain=" + UPDATED_VALUE, "value.doesNotContain=" + DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void getAllAlPowerShellVisByPropertyProfileIsEqualToSomething() throws Exception {
        AlProProVi propertyProfile;
        if (TestUtil.findAll(em, AlProProVi.class).isEmpty()) {
            alPowerShellViRepository.saveAndFlush(alPowerShellVi);
            propertyProfile = AlProProViResourceIT.createEntity();
        } else {
            propertyProfile = TestUtil.findAll(em, AlProProVi.class).get(0);
        }
        em.persist(propertyProfile);
        em.flush();
        alPowerShellVi.setPropertyProfile(propertyProfile);
        alPowerShellViRepository.saveAndFlush(alPowerShellVi);
        UUID propertyProfileId = propertyProfile.getId();
        // Get all the alPowerShellViList where propertyProfile equals to propertyProfileId
        defaultAlPowerShellViShouldBeFound("propertyProfileId.equals=" + propertyProfileId);

        // Get all the alPowerShellViList where propertyProfile equals to UUID.randomUUID()
        defaultAlPowerShellViShouldNotBeFound("propertyProfileId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlPowerShellVisByAttributeTermIsEqualToSomething() throws Exception {
        AlPounderVi attributeTerm;
        if (TestUtil.findAll(em, AlPounderVi.class).isEmpty()) {
            alPowerShellViRepository.saveAndFlush(alPowerShellVi);
            attributeTerm = AlPounderViResourceIT.createEntity(em);
        } else {
            attributeTerm = TestUtil.findAll(em, AlPounderVi.class).get(0);
        }
        em.persist(attributeTerm);
        em.flush();
        alPowerShellVi.setAttributeTerm(attributeTerm);
        alPowerShellViRepository.saveAndFlush(alPowerShellVi);
        Long attributeTermId = attributeTerm.getId();
        // Get all the alPowerShellViList where attributeTerm equals to attributeTermId
        defaultAlPowerShellViShouldBeFound("attributeTermId.equals=" + attributeTermId);

        // Get all the alPowerShellViList where attributeTerm equals to (attributeTermId + 1)
        defaultAlPowerShellViShouldNotBeFound("attributeTermId.equals=" + (attributeTermId + 1));
    }

    @Test
    @Transactional
    void getAllAlPowerShellVisByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alPowerShellViRepository.saveAndFlush(alPowerShellVi);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alPowerShellVi.setApplication(application);
        alPowerShellViRepository.saveAndFlush(alPowerShellVi);
        UUID applicationId = application.getId();
        // Get all the alPowerShellViList where application equals to applicationId
        defaultAlPowerShellViShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alPowerShellViList where application equals to UUID.randomUUID()
        defaultAlPowerShellViShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlPowerShellViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlPowerShellViShouldBeFound(shouldBeFound);
        defaultAlPowerShellViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlPowerShellViShouldBeFound(String filter) throws Exception {
        restAlPowerShellViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPowerShellVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));

        // Check, that the count call also returns 1
        restAlPowerShellViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlPowerShellViShouldNotBeFound(String filter) throws Exception {
        restAlPowerShellViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlPowerShellViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlPowerShellVi() throws Exception {
        // Get the alPowerShellVi
        restAlPowerShellViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlPowerShellVi() throws Exception {
        // Initialize the database
        insertedAlPowerShellVi = alPowerShellViRepository.saveAndFlush(alPowerShellVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPowerShellVi
        AlPowerShellVi updatedAlPowerShellVi = alPowerShellViRepository.findById(alPowerShellVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlPowerShellVi are not directly saved in db
        em.detach(updatedAlPowerShellVi);
        updatedAlPowerShellVi.value(UPDATED_VALUE);

        restAlPowerShellViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlPowerShellVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlPowerShellVi))
            )
            .andExpect(status().isOk());

        // Validate the AlPowerShellVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlPowerShellViToMatchAllProperties(updatedAlPowerShellVi);
    }

    @Test
    @Transactional
    void putNonExistingAlPowerShellVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPowerShellVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPowerShellViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alPowerShellVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPowerShellVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPowerShellVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlPowerShellVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPowerShellVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPowerShellViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPowerShellVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPowerShellVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlPowerShellVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPowerShellVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPowerShellViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPowerShellVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPowerShellVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlPowerShellViWithPatch() throws Exception {
        // Initialize the database
        insertedAlPowerShellVi = alPowerShellViRepository.saveAndFlush(alPowerShellVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPowerShellVi using partial update
        AlPowerShellVi partialUpdatedAlPowerShellVi = new AlPowerShellVi();
        partialUpdatedAlPowerShellVi.setId(alPowerShellVi.getId());

        restAlPowerShellViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPowerShellVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPowerShellVi))
            )
            .andExpect(status().isOk());

        // Validate the AlPowerShellVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPowerShellViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlPowerShellVi, alPowerShellVi),
            getPersistedAlPowerShellVi(alPowerShellVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlPowerShellViWithPatch() throws Exception {
        // Initialize the database
        insertedAlPowerShellVi = alPowerShellViRepository.saveAndFlush(alPowerShellVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPowerShellVi using partial update
        AlPowerShellVi partialUpdatedAlPowerShellVi = new AlPowerShellVi();
        partialUpdatedAlPowerShellVi.setId(alPowerShellVi.getId());

        partialUpdatedAlPowerShellVi.value(UPDATED_VALUE);

        restAlPowerShellViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPowerShellVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPowerShellVi))
            )
            .andExpect(status().isOk());

        // Validate the AlPowerShellVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPowerShellViUpdatableFieldsEquals(partialUpdatedAlPowerShellVi, getPersistedAlPowerShellVi(partialUpdatedAlPowerShellVi));
    }

    @Test
    @Transactional
    void patchNonExistingAlPowerShellVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPowerShellVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPowerShellViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alPowerShellVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPowerShellVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPowerShellVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlPowerShellVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPowerShellVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPowerShellViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPowerShellVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPowerShellVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlPowerShellVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPowerShellVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPowerShellViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alPowerShellVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPowerShellVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlPowerShellVi() throws Exception {
        // Initialize the database
        insertedAlPowerShellVi = alPowerShellViRepository.saveAndFlush(alPowerShellVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alPowerShellVi
        restAlPowerShellViMockMvc
            .perform(delete(ENTITY_API_URL_ID, alPowerShellVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alPowerShellViRepository.count();
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

    protected AlPowerShellVi getPersistedAlPowerShellVi(AlPowerShellVi alPowerShellVi) {
        return alPowerShellViRepository.findById(alPowerShellVi.getId()).orElseThrow();
    }

    protected void assertPersistedAlPowerShellViToMatchAllProperties(AlPowerShellVi expectedAlPowerShellVi) {
        assertAlPowerShellViAllPropertiesEquals(expectedAlPowerShellVi, getPersistedAlPowerShellVi(expectedAlPowerShellVi));
    }

    protected void assertPersistedAlPowerShellViToMatchUpdatableProperties(AlPowerShellVi expectedAlPowerShellVi) {
        assertAlPowerShellViAllUpdatablePropertiesEquals(expectedAlPowerShellVi, getPersistedAlPowerShellVi(expectedAlPowerShellVi));
    }
}
