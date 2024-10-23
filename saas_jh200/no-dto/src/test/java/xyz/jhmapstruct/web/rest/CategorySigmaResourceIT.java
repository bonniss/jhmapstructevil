package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.CategorySigmaAsserts.*;
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
import xyz.jhmapstruct.domain.CategorySigma;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.repository.CategorySigmaRepository;

/**
 * Integration tests for the {@link CategorySigmaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CategorySigmaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/category-sigmas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CategorySigmaRepository categorySigmaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategorySigmaMockMvc;

    private CategorySigma categorySigma;

    private CategorySigma insertedCategorySigma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategorySigma createEntity() {
        return new CategorySigma().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategorySigma createUpdatedEntity() {
        return new CategorySigma().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        categorySigma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCategorySigma != null) {
            categorySigmaRepository.delete(insertedCategorySigma);
            insertedCategorySigma = null;
        }
    }

    @Test
    @Transactional
    void createCategorySigma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CategorySigma
        var returnedCategorySigma = om.readValue(
            restCategorySigmaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categorySigma)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CategorySigma.class
        );

        // Validate the CategorySigma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCategorySigmaUpdatableFieldsEquals(returnedCategorySigma, getPersistedCategorySigma(returnedCategorySigma));

        insertedCategorySigma = returnedCategorySigma;
    }

    @Test
    @Transactional
    void createCategorySigmaWithExistingId() throws Exception {
        // Create the CategorySigma with an existing ID
        categorySigma.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategorySigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categorySigma)))
            .andExpect(status().isBadRequest());

        // Validate the CategorySigma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        categorySigma.setName(null);

        // Create the CategorySigma, which fails.

        restCategorySigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categorySigma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCategorySigmas() throws Exception {
        // Initialize the database
        insertedCategorySigma = categorySigmaRepository.saveAndFlush(categorySigma);

        // Get all the categorySigmaList
        restCategorySigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categorySigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getCategorySigma() throws Exception {
        // Initialize the database
        insertedCategorySigma = categorySigmaRepository.saveAndFlush(categorySigma);

        // Get the categorySigma
        restCategorySigmaMockMvc
            .perform(get(ENTITY_API_URL_ID, categorySigma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(categorySigma.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getCategorySigmasByIdFiltering() throws Exception {
        // Initialize the database
        insertedCategorySigma = categorySigmaRepository.saveAndFlush(categorySigma);

        Long id = categorySigma.getId();

        defaultCategorySigmaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCategorySigmaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCategorySigmaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCategorySigmasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCategorySigma = categorySigmaRepository.saveAndFlush(categorySigma);

        // Get all the categorySigmaList where name equals to
        defaultCategorySigmaFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCategorySigmasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCategorySigma = categorySigmaRepository.saveAndFlush(categorySigma);

        // Get all the categorySigmaList where name in
        defaultCategorySigmaFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCategorySigmasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCategorySigma = categorySigmaRepository.saveAndFlush(categorySigma);

        // Get all the categorySigmaList where name is not null
        defaultCategorySigmaFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCategorySigmasByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCategorySigma = categorySigmaRepository.saveAndFlush(categorySigma);

        // Get all the categorySigmaList where name contains
        defaultCategorySigmaFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCategorySigmasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCategorySigma = categorySigmaRepository.saveAndFlush(categorySigma);

        // Get all the categorySigmaList where name does not contain
        defaultCategorySigmaFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCategorySigmasByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCategorySigma = categorySigmaRepository.saveAndFlush(categorySigma);

        // Get all the categorySigmaList where description equals to
        defaultCategorySigmaFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCategorySigmasByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCategorySigma = categorySigmaRepository.saveAndFlush(categorySigma);

        // Get all the categorySigmaList where description in
        defaultCategorySigmaFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCategorySigmasByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCategorySigma = categorySigmaRepository.saveAndFlush(categorySigma);

        // Get all the categorySigmaList where description is not null
        defaultCategorySigmaFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllCategorySigmasByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedCategorySigma = categorySigmaRepository.saveAndFlush(categorySigma);

        // Get all the categorySigmaList where description contains
        defaultCategorySigmaFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCategorySigmasByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCategorySigma = categorySigmaRepository.saveAndFlush(categorySigma);

        // Get all the categorySigmaList where description does not contain
        defaultCategorySigmaFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCategorySigmasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            categorySigmaRepository.saveAndFlush(categorySigma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        categorySigma.setTenant(tenant);
        categorySigmaRepository.saveAndFlush(categorySigma);
        Long tenantId = tenant.getId();
        // Get all the categorySigmaList where tenant equals to tenantId
        defaultCategorySigmaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the categorySigmaList where tenant equals to (tenantId + 1)
        defaultCategorySigmaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultCategorySigmaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCategorySigmaShouldBeFound(shouldBeFound);
        defaultCategorySigmaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCategorySigmaShouldBeFound(String filter) throws Exception {
        restCategorySigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categorySigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restCategorySigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCategorySigmaShouldNotBeFound(String filter) throws Exception {
        restCategorySigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCategorySigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCategorySigma() throws Exception {
        // Get the categorySigma
        restCategorySigmaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCategorySigma() throws Exception {
        // Initialize the database
        insertedCategorySigma = categorySigmaRepository.saveAndFlush(categorySigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the categorySigma
        CategorySigma updatedCategorySigma = categorySigmaRepository.findById(categorySigma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCategorySigma are not directly saved in db
        em.detach(updatedCategorySigma);
        updatedCategorySigma.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restCategorySigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCategorySigma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCategorySigma))
            )
            .andExpect(status().isOk());

        // Validate the CategorySigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCategorySigmaToMatchAllProperties(updatedCategorySigma);
    }

    @Test
    @Transactional
    void putNonExistingCategorySigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categorySigma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategorySigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categorySigma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(categorySigma))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategorySigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCategorySigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categorySigma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategorySigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(categorySigma))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategorySigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCategorySigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categorySigma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategorySigmaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categorySigma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategorySigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCategorySigmaWithPatch() throws Exception {
        // Initialize the database
        insertedCategorySigma = categorySigmaRepository.saveAndFlush(categorySigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the categorySigma using partial update
        CategorySigma partialUpdatedCategorySigma = new CategorySigma();
        partialUpdatedCategorySigma.setId(categorySigma.getId());

        restCategorySigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategorySigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCategorySigma))
            )
            .andExpect(status().isOk());

        // Validate the CategorySigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCategorySigmaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCategorySigma, categorySigma),
            getPersistedCategorySigma(categorySigma)
        );
    }

    @Test
    @Transactional
    void fullUpdateCategorySigmaWithPatch() throws Exception {
        // Initialize the database
        insertedCategorySigma = categorySigmaRepository.saveAndFlush(categorySigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the categorySigma using partial update
        CategorySigma partialUpdatedCategorySigma = new CategorySigma();
        partialUpdatedCategorySigma.setId(categorySigma.getId());

        partialUpdatedCategorySigma.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restCategorySigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategorySigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCategorySigma))
            )
            .andExpect(status().isOk());

        // Validate the CategorySigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCategorySigmaUpdatableFieldsEquals(partialUpdatedCategorySigma, getPersistedCategorySigma(partialUpdatedCategorySigma));
    }

    @Test
    @Transactional
    void patchNonExistingCategorySigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categorySigma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategorySigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, categorySigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(categorySigma))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategorySigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCategorySigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categorySigma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategorySigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(categorySigma))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategorySigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCategorySigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categorySigma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategorySigmaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(categorySigma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategorySigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCategorySigma() throws Exception {
        // Initialize the database
        insertedCategorySigma = categorySigmaRepository.saveAndFlush(categorySigma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the categorySigma
        restCategorySigmaMockMvc
            .perform(delete(ENTITY_API_URL_ID, categorySigma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return categorySigmaRepository.count();
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

    protected CategorySigma getPersistedCategorySigma(CategorySigma categorySigma) {
        return categorySigmaRepository.findById(categorySigma.getId()).orElseThrow();
    }

    protected void assertPersistedCategorySigmaToMatchAllProperties(CategorySigma expectedCategorySigma) {
        assertCategorySigmaAllPropertiesEquals(expectedCategorySigma, getPersistedCategorySigma(expectedCategorySigma));
    }

    protected void assertPersistedCategorySigmaToMatchUpdatableProperties(CategorySigma expectedCategorySigma) {
        assertCategorySigmaAllUpdatablePropertiesEquals(expectedCategorySigma, getPersistedCategorySigma(expectedCategorySigma));
    }
}
