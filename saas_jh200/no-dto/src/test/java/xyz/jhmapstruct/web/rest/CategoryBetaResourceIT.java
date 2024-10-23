package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.CategoryBetaAsserts.*;
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
import xyz.jhmapstruct.domain.CategoryBeta;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.repository.CategoryBetaRepository;

/**
 * Integration tests for the {@link CategoryBetaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CategoryBetaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/category-betas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CategoryBetaRepository categoryBetaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategoryBetaMockMvc;

    private CategoryBeta categoryBeta;

    private CategoryBeta insertedCategoryBeta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoryBeta createEntity() {
        return new CategoryBeta().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoryBeta createUpdatedEntity() {
        return new CategoryBeta().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        categoryBeta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCategoryBeta != null) {
            categoryBetaRepository.delete(insertedCategoryBeta);
            insertedCategoryBeta = null;
        }
    }

    @Test
    @Transactional
    void createCategoryBeta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CategoryBeta
        var returnedCategoryBeta = om.readValue(
            restCategoryBetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryBeta)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CategoryBeta.class
        );

        // Validate the CategoryBeta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCategoryBetaUpdatableFieldsEquals(returnedCategoryBeta, getPersistedCategoryBeta(returnedCategoryBeta));

        insertedCategoryBeta = returnedCategoryBeta;
    }

    @Test
    @Transactional
    void createCategoryBetaWithExistingId() throws Exception {
        // Create the CategoryBeta with an existing ID
        categoryBeta.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoryBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryBeta)))
            .andExpect(status().isBadRequest());

        // Validate the CategoryBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        categoryBeta.setName(null);

        // Create the CategoryBeta, which fails.

        restCategoryBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryBeta)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCategoryBetas() throws Exception {
        // Initialize the database
        insertedCategoryBeta = categoryBetaRepository.saveAndFlush(categoryBeta);

        // Get all the categoryBetaList
        restCategoryBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoryBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getCategoryBeta() throws Exception {
        // Initialize the database
        insertedCategoryBeta = categoryBetaRepository.saveAndFlush(categoryBeta);

        // Get the categoryBeta
        restCategoryBetaMockMvc
            .perform(get(ENTITY_API_URL_ID, categoryBeta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(categoryBeta.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getCategoryBetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedCategoryBeta = categoryBetaRepository.saveAndFlush(categoryBeta);

        Long id = categoryBeta.getId();

        defaultCategoryBetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCategoryBetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCategoryBetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCategoryBetasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCategoryBeta = categoryBetaRepository.saveAndFlush(categoryBeta);

        // Get all the categoryBetaList where name equals to
        defaultCategoryBetaFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCategoryBetasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCategoryBeta = categoryBetaRepository.saveAndFlush(categoryBeta);

        // Get all the categoryBetaList where name in
        defaultCategoryBetaFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCategoryBetasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCategoryBeta = categoryBetaRepository.saveAndFlush(categoryBeta);

        // Get all the categoryBetaList where name is not null
        defaultCategoryBetaFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoryBetasByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCategoryBeta = categoryBetaRepository.saveAndFlush(categoryBeta);

        // Get all the categoryBetaList where name contains
        defaultCategoryBetaFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCategoryBetasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCategoryBeta = categoryBetaRepository.saveAndFlush(categoryBeta);

        // Get all the categoryBetaList where name does not contain
        defaultCategoryBetaFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCategoryBetasByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCategoryBeta = categoryBetaRepository.saveAndFlush(categoryBeta);

        // Get all the categoryBetaList where description equals to
        defaultCategoryBetaFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCategoryBetasByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCategoryBeta = categoryBetaRepository.saveAndFlush(categoryBeta);

        // Get all the categoryBetaList where description in
        defaultCategoryBetaFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCategoryBetasByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCategoryBeta = categoryBetaRepository.saveAndFlush(categoryBeta);

        // Get all the categoryBetaList where description is not null
        defaultCategoryBetaFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoryBetasByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedCategoryBeta = categoryBetaRepository.saveAndFlush(categoryBeta);

        // Get all the categoryBetaList where description contains
        defaultCategoryBetaFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCategoryBetasByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCategoryBeta = categoryBetaRepository.saveAndFlush(categoryBeta);

        // Get all the categoryBetaList where description does not contain
        defaultCategoryBetaFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCategoryBetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            categoryBetaRepository.saveAndFlush(categoryBeta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        categoryBeta.setTenant(tenant);
        categoryBetaRepository.saveAndFlush(categoryBeta);
        Long tenantId = tenant.getId();
        // Get all the categoryBetaList where tenant equals to tenantId
        defaultCategoryBetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the categoryBetaList where tenant equals to (tenantId + 1)
        defaultCategoryBetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultCategoryBetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCategoryBetaShouldBeFound(shouldBeFound);
        defaultCategoryBetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCategoryBetaShouldBeFound(String filter) throws Exception {
        restCategoryBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoryBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restCategoryBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCategoryBetaShouldNotBeFound(String filter) throws Exception {
        restCategoryBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCategoryBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCategoryBeta() throws Exception {
        // Get the categoryBeta
        restCategoryBetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCategoryBeta() throws Exception {
        // Initialize the database
        insertedCategoryBeta = categoryBetaRepository.saveAndFlush(categoryBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the categoryBeta
        CategoryBeta updatedCategoryBeta = categoryBetaRepository.findById(categoryBeta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCategoryBeta are not directly saved in db
        em.detach(updatedCategoryBeta);
        updatedCategoryBeta.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restCategoryBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCategoryBeta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCategoryBeta))
            )
            .andExpect(status().isOk());

        // Validate the CategoryBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCategoryBetaToMatchAllProperties(updatedCategoryBeta);
    }

    @Test
    @Transactional
    void putNonExistingCategoryBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryBeta.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoryBeta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(categoryBeta))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCategoryBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryBeta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(categoryBeta))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCategoryBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryBeta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryBetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryBeta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategoryBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCategoryBetaWithPatch() throws Exception {
        // Initialize the database
        insertedCategoryBeta = categoryBetaRepository.saveAndFlush(categoryBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the categoryBeta using partial update
        CategoryBeta partialUpdatedCategoryBeta = new CategoryBeta();
        partialUpdatedCategoryBeta.setId(categoryBeta.getId());

        partialUpdatedCategoryBeta.name(UPDATED_NAME);

        restCategoryBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoryBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCategoryBeta))
            )
            .andExpect(status().isOk());

        // Validate the CategoryBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCategoryBetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCategoryBeta, categoryBeta),
            getPersistedCategoryBeta(categoryBeta)
        );
    }

    @Test
    @Transactional
    void fullUpdateCategoryBetaWithPatch() throws Exception {
        // Initialize the database
        insertedCategoryBeta = categoryBetaRepository.saveAndFlush(categoryBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the categoryBeta using partial update
        CategoryBeta partialUpdatedCategoryBeta = new CategoryBeta();
        partialUpdatedCategoryBeta.setId(categoryBeta.getId());

        partialUpdatedCategoryBeta.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restCategoryBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoryBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCategoryBeta))
            )
            .andExpect(status().isOk());

        // Validate the CategoryBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCategoryBetaUpdatableFieldsEquals(partialUpdatedCategoryBeta, getPersistedCategoryBeta(partialUpdatedCategoryBeta));
    }

    @Test
    @Transactional
    void patchNonExistingCategoryBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryBeta.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, categoryBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(categoryBeta))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCategoryBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryBeta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(categoryBeta))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCategoryBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryBeta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryBetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(categoryBeta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategoryBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCategoryBeta() throws Exception {
        // Initialize the database
        insertedCategoryBeta = categoryBetaRepository.saveAndFlush(categoryBeta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the categoryBeta
        restCategoryBetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, categoryBeta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return categoryBetaRepository.count();
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

    protected CategoryBeta getPersistedCategoryBeta(CategoryBeta categoryBeta) {
        return categoryBetaRepository.findById(categoryBeta.getId()).orElseThrow();
    }

    protected void assertPersistedCategoryBetaToMatchAllProperties(CategoryBeta expectedCategoryBeta) {
        assertCategoryBetaAllPropertiesEquals(expectedCategoryBeta, getPersistedCategoryBeta(expectedCategoryBeta));
    }

    protected void assertPersistedCategoryBetaToMatchUpdatableProperties(CategoryBeta expectedCategoryBeta) {
        assertCategoryBetaAllUpdatablePropertiesEquals(expectedCategoryBeta, getPersistedCategoryBeta(expectedCategoryBeta));
    }
}
