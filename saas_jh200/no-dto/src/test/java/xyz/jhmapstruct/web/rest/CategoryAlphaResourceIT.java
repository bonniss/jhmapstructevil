package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.CategoryAlphaAsserts.*;
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
import xyz.jhmapstruct.domain.CategoryAlpha;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.repository.CategoryAlphaRepository;

/**
 * Integration tests for the {@link CategoryAlphaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CategoryAlphaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/category-alphas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CategoryAlphaRepository categoryAlphaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategoryAlphaMockMvc;

    private CategoryAlpha categoryAlpha;

    private CategoryAlpha insertedCategoryAlpha;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoryAlpha createEntity() {
        return new CategoryAlpha().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoryAlpha createUpdatedEntity() {
        return new CategoryAlpha().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        categoryAlpha = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCategoryAlpha != null) {
            categoryAlphaRepository.delete(insertedCategoryAlpha);
            insertedCategoryAlpha = null;
        }
    }

    @Test
    @Transactional
    void createCategoryAlpha() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CategoryAlpha
        var returnedCategoryAlpha = om.readValue(
            restCategoryAlphaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryAlpha)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CategoryAlpha.class
        );

        // Validate the CategoryAlpha in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCategoryAlphaUpdatableFieldsEquals(returnedCategoryAlpha, getPersistedCategoryAlpha(returnedCategoryAlpha));

        insertedCategoryAlpha = returnedCategoryAlpha;
    }

    @Test
    @Transactional
    void createCategoryAlphaWithExistingId() throws Exception {
        // Create the CategoryAlpha with an existing ID
        categoryAlpha.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoryAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryAlpha)))
            .andExpect(status().isBadRequest());

        // Validate the CategoryAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        categoryAlpha.setName(null);

        // Create the CategoryAlpha, which fails.

        restCategoryAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryAlpha)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCategoryAlphas() throws Exception {
        // Initialize the database
        insertedCategoryAlpha = categoryAlphaRepository.saveAndFlush(categoryAlpha);

        // Get all the categoryAlphaList
        restCategoryAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoryAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getCategoryAlpha() throws Exception {
        // Initialize the database
        insertedCategoryAlpha = categoryAlphaRepository.saveAndFlush(categoryAlpha);

        // Get the categoryAlpha
        restCategoryAlphaMockMvc
            .perform(get(ENTITY_API_URL_ID, categoryAlpha.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(categoryAlpha.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getCategoryAlphasByIdFiltering() throws Exception {
        // Initialize the database
        insertedCategoryAlpha = categoryAlphaRepository.saveAndFlush(categoryAlpha);

        Long id = categoryAlpha.getId();

        defaultCategoryAlphaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCategoryAlphaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCategoryAlphaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCategoryAlphasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCategoryAlpha = categoryAlphaRepository.saveAndFlush(categoryAlpha);

        // Get all the categoryAlphaList where name equals to
        defaultCategoryAlphaFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCategoryAlphasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCategoryAlpha = categoryAlphaRepository.saveAndFlush(categoryAlpha);

        // Get all the categoryAlphaList where name in
        defaultCategoryAlphaFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCategoryAlphasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCategoryAlpha = categoryAlphaRepository.saveAndFlush(categoryAlpha);

        // Get all the categoryAlphaList where name is not null
        defaultCategoryAlphaFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoryAlphasByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCategoryAlpha = categoryAlphaRepository.saveAndFlush(categoryAlpha);

        // Get all the categoryAlphaList where name contains
        defaultCategoryAlphaFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCategoryAlphasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCategoryAlpha = categoryAlphaRepository.saveAndFlush(categoryAlpha);

        // Get all the categoryAlphaList where name does not contain
        defaultCategoryAlphaFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCategoryAlphasByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCategoryAlpha = categoryAlphaRepository.saveAndFlush(categoryAlpha);

        // Get all the categoryAlphaList where description equals to
        defaultCategoryAlphaFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCategoryAlphasByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCategoryAlpha = categoryAlphaRepository.saveAndFlush(categoryAlpha);

        // Get all the categoryAlphaList where description in
        defaultCategoryAlphaFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCategoryAlphasByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCategoryAlpha = categoryAlphaRepository.saveAndFlush(categoryAlpha);

        // Get all the categoryAlphaList where description is not null
        defaultCategoryAlphaFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoryAlphasByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedCategoryAlpha = categoryAlphaRepository.saveAndFlush(categoryAlpha);

        // Get all the categoryAlphaList where description contains
        defaultCategoryAlphaFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCategoryAlphasByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCategoryAlpha = categoryAlphaRepository.saveAndFlush(categoryAlpha);

        // Get all the categoryAlphaList where description does not contain
        defaultCategoryAlphaFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCategoryAlphasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            categoryAlphaRepository.saveAndFlush(categoryAlpha);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        categoryAlpha.setTenant(tenant);
        categoryAlphaRepository.saveAndFlush(categoryAlpha);
        Long tenantId = tenant.getId();
        // Get all the categoryAlphaList where tenant equals to tenantId
        defaultCategoryAlphaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the categoryAlphaList where tenant equals to (tenantId + 1)
        defaultCategoryAlphaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultCategoryAlphaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCategoryAlphaShouldBeFound(shouldBeFound);
        defaultCategoryAlphaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCategoryAlphaShouldBeFound(String filter) throws Exception {
        restCategoryAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoryAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restCategoryAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCategoryAlphaShouldNotBeFound(String filter) throws Exception {
        restCategoryAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCategoryAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCategoryAlpha() throws Exception {
        // Get the categoryAlpha
        restCategoryAlphaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCategoryAlpha() throws Exception {
        // Initialize the database
        insertedCategoryAlpha = categoryAlphaRepository.saveAndFlush(categoryAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the categoryAlpha
        CategoryAlpha updatedCategoryAlpha = categoryAlphaRepository.findById(categoryAlpha.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCategoryAlpha are not directly saved in db
        em.detach(updatedCategoryAlpha);
        updatedCategoryAlpha.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restCategoryAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCategoryAlpha.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCategoryAlpha))
            )
            .andExpect(status().isOk());

        // Validate the CategoryAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCategoryAlphaToMatchAllProperties(updatedCategoryAlpha);
    }

    @Test
    @Transactional
    void putNonExistingCategoryAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryAlpha.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoryAlpha.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(categoryAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCategoryAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(categoryAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCategoryAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryAlphaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryAlpha)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategoryAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCategoryAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedCategoryAlpha = categoryAlphaRepository.saveAndFlush(categoryAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the categoryAlpha using partial update
        CategoryAlpha partialUpdatedCategoryAlpha = new CategoryAlpha();
        partialUpdatedCategoryAlpha.setId(categoryAlpha.getId());

        partialUpdatedCategoryAlpha.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restCategoryAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoryAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCategoryAlpha))
            )
            .andExpect(status().isOk());

        // Validate the CategoryAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCategoryAlphaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCategoryAlpha, categoryAlpha),
            getPersistedCategoryAlpha(categoryAlpha)
        );
    }

    @Test
    @Transactional
    void fullUpdateCategoryAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedCategoryAlpha = categoryAlphaRepository.saveAndFlush(categoryAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the categoryAlpha using partial update
        CategoryAlpha partialUpdatedCategoryAlpha = new CategoryAlpha();
        partialUpdatedCategoryAlpha.setId(categoryAlpha.getId());

        partialUpdatedCategoryAlpha.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restCategoryAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoryAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCategoryAlpha))
            )
            .andExpect(status().isOk());

        // Validate the CategoryAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCategoryAlphaUpdatableFieldsEquals(partialUpdatedCategoryAlpha, getPersistedCategoryAlpha(partialUpdatedCategoryAlpha));
    }

    @Test
    @Transactional
    void patchNonExistingCategoryAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryAlpha.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, categoryAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(categoryAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCategoryAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(categoryAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCategoryAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryAlphaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(categoryAlpha)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategoryAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCategoryAlpha() throws Exception {
        // Initialize the database
        insertedCategoryAlpha = categoryAlphaRepository.saveAndFlush(categoryAlpha);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the categoryAlpha
        restCategoryAlphaMockMvc
            .perform(delete(ENTITY_API_URL_ID, categoryAlpha.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return categoryAlphaRepository.count();
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

    protected CategoryAlpha getPersistedCategoryAlpha(CategoryAlpha categoryAlpha) {
        return categoryAlphaRepository.findById(categoryAlpha.getId()).orElseThrow();
    }

    protected void assertPersistedCategoryAlphaToMatchAllProperties(CategoryAlpha expectedCategoryAlpha) {
        assertCategoryAlphaAllPropertiesEquals(expectedCategoryAlpha, getPersistedCategoryAlpha(expectedCategoryAlpha));
    }

    protected void assertPersistedCategoryAlphaToMatchUpdatableProperties(CategoryAlpha expectedCategoryAlpha) {
        assertCategoryAlphaAllUpdatablePropertiesEquals(expectedCategoryAlpha, getPersistedCategoryAlpha(expectedCategoryAlpha));
    }
}
