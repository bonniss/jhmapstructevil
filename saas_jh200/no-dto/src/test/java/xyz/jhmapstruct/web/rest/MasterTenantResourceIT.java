package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.MasterTenantAsserts.*;
import static xyz.jhmapstruct.web.rest.TestUtil.createUpdateProxyForBean;

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
import xyz.jhmapstruct.IntegrationTest;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.repository.MasterTenantRepository;

/**
 * Integration tests for the {@link MasterTenantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MasterTenantResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/master-tenants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MasterTenantRepository masterTenantRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMasterTenantMockMvc;

    private MasterTenant masterTenant;

    private MasterTenant insertedMasterTenant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MasterTenant createEntity() {
        return new MasterTenant().code(DEFAULT_CODE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MasterTenant createUpdatedEntity() {
        return new MasterTenant().code(UPDATED_CODE);
    }

    @BeforeEach
    public void initTest() {
        masterTenant = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMasterTenant != null) {
            masterTenantRepository.delete(insertedMasterTenant);
            insertedMasterTenant = null;
        }
    }

    @Test
    @Transactional
    void createMasterTenant() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MasterTenant
        var returnedMasterTenant = om.readValue(
            restMasterTenantMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(masterTenant)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MasterTenant.class
        );

        // Validate the MasterTenant in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMasterTenantUpdatableFieldsEquals(returnedMasterTenant, getPersistedMasterTenant(returnedMasterTenant));

        insertedMasterTenant = returnedMasterTenant;
    }

    @Test
    @Transactional
    void createMasterTenantWithExistingId() throws Exception {
        // Create the MasterTenant with an existing ID
        masterTenant.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMasterTenantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(masterTenant)))
            .andExpect(status().isBadRequest());

        // Validate the MasterTenant in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        masterTenant.setCode(null);

        // Create the MasterTenant, which fails.

        restMasterTenantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(masterTenant)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMasterTenants() throws Exception {
        // Initialize the database
        insertedMasterTenant = masterTenantRepository.saveAndFlush(masterTenant);

        // Get all the masterTenantList
        restMasterTenantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(masterTenant.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getMasterTenant() throws Exception {
        // Initialize the database
        insertedMasterTenant = masterTenantRepository.saveAndFlush(masterTenant);

        // Get the masterTenant
        restMasterTenantMockMvc
            .perform(get(ENTITY_API_URL_ID, masterTenant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(masterTenant.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getMasterTenantsByIdFiltering() throws Exception {
        // Initialize the database
        insertedMasterTenant = masterTenantRepository.saveAndFlush(masterTenant);

        Long id = masterTenant.getId();

        defaultMasterTenantFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMasterTenantFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMasterTenantFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMasterTenantsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMasterTenant = masterTenantRepository.saveAndFlush(masterTenant);

        // Get all the masterTenantList where code equals to
        defaultMasterTenantFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMasterTenantsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMasterTenant = masterTenantRepository.saveAndFlush(masterTenant);

        // Get all the masterTenantList where code in
        defaultMasterTenantFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMasterTenantsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMasterTenant = masterTenantRepository.saveAndFlush(masterTenant);

        // Get all the masterTenantList where code is not null
        defaultMasterTenantFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllMasterTenantsByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedMasterTenant = masterTenantRepository.saveAndFlush(masterTenant);

        // Get all the masterTenantList where code contains
        defaultMasterTenantFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMasterTenantsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMasterTenant = masterTenantRepository.saveAndFlush(masterTenant);

        // Get all the masterTenantList where code does not contain
        defaultMasterTenantFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    private void defaultMasterTenantFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMasterTenantShouldBeFound(shouldBeFound);
        defaultMasterTenantShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMasterTenantShouldBeFound(String filter) throws Exception {
        restMasterTenantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(masterTenant.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));

        // Check, that the count call also returns 1
        restMasterTenantMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMasterTenantShouldNotBeFound(String filter) throws Exception {
        restMasterTenantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMasterTenantMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMasterTenant() throws Exception {
        // Get the masterTenant
        restMasterTenantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMasterTenant() throws Exception {
        // Initialize the database
        insertedMasterTenant = masterTenantRepository.saveAndFlush(masterTenant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the masterTenant
        MasterTenant updatedMasterTenant = masterTenantRepository.findById(masterTenant.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMasterTenant are not directly saved in db
        em.detach(updatedMasterTenant);
        updatedMasterTenant.code(UPDATED_CODE);

        restMasterTenantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMasterTenant.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMasterTenant))
            )
            .andExpect(status().isOk());

        // Validate the MasterTenant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMasterTenantToMatchAllProperties(updatedMasterTenant);
    }

    @Test
    @Transactional
    void putNonExistingMasterTenant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        masterTenant.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMasterTenantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, masterTenant.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(masterTenant))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterTenant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMasterTenant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        masterTenant.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMasterTenantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(masterTenant))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterTenant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMasterTenant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        masterTenant.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMasterTenantMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(masterTenant)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MasterTenant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMasterTenantWithPatch() throws Exception {
        // Initialize the database
        insertedMasterTenant = masterTenantRepository.saveAndFlush(masterTenant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the masterTenant using partial update
        MasterTenant partialUpdatedMasterTenant = new MasterTenant();
        partialUpdatedMasterTenant.setId(masterTenant.getId());

        restMasterTenantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMasterTenant.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMasterTenant))
            )
            .andExpect(status().isOk());

        // Validate the MasterTenant in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMasterTenantUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMasterTenant, masterTenant),
            getPersistedMasterTenant(masterTenant)
        );
    }

    @Test
    @Transactional
    void fullUpdateMasterTenantWithPatch() throws Exception {
        // Initialize the database
        insertedMasterTenant = masterTenantRepository.saveAndFlush(masterTenant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the masterTenant using partial update
        MasterTenant partialUpdatedMasterTenant = new MasterTenant();
        partialUpdatedMasterTenant.setId(masterTenant.getId());

        partialUpdatedMasterTenant.code(UPDATED_CODE);

        restMasterTenantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMasterTenant.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMasterTenant))
            )
            .andExpect(status().isOk());

        // Validate the MasterTenant in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMasterTenantUpdatableFieldsEquals(partialUpdatedMasterTenant, getPersistedMasterTenant(partialUpdatedMasterTenant));
    }

    @Test
    @Transactional
    void patchNonExistingMasterTenant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        masterTenant.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMasterTenantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, masterTenant.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(masterTenant))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterTenant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMasterTenant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        masterTenant.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMasterTenantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(masterTenant))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterTenant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMasterTenant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        masterTenant.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMasterTenantMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(masterTenant)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MasterTenant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMasterTenant() throws Exception {
        // Initialize the database
        insertedMasterTenant = masterTenantRepository.saveAndFlush(masterTenant);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the masterTenant
        restMasterTenantMockMvc
            .perform(delete(ENTITY_API_URL_ID, masterTenant.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return masterTenantRepository.count();
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

    protected MasterTenant getPersistedMasterTenant(MasterTenant masterTenant) {
        return masterTenantRepository.findById(masterTenant.getId()).orElseThrow();
    }

    protected void assertPersistedMasterTenantToMatchAllProperties(MasterTenant expectedMasterTenant) {
        assertMasterTenantAllPropertiesEquals(expectedMasterTenant, getPersistedMasterTenant(expectedMasterTenant));
    }

    protected void assertPersistedMasterTenantToMatchUpdatableProperties(MasterTenant expectedMasterTenant) {
        assertMasterTenantAllUpdatablePropertiesEquals(expectedMasterTenant, getPersistedMasterTenant(expectedMasterTenant));
    }
}
