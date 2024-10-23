package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.CategoryGammaAsserts.*;
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
import xyz.jhmapstruct.domain.CategoryGamma;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.repository.CategoryGammaRepository;
import xyz.jhmapstruct.service.dto.CategoryGammaDTO;
import xyz.jhmapstruct.service.mapper.CategoryGammaMapper;

/**
 * Integration tests for the {@link CategoryGammaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CategoryGammaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/category-gammas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CategoryGammaRepository categoryGammaRepository;

    @Autowired
    private CategoryGammaMapper categoryGammaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategoryGammaMockMvc;

    private CategoryGamma categoryGamma;

    private CategoryGamma insertedCategoryGamma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoryGamma createEntity() {
        return new CategoryGamma().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoryGamma createUpdatedEntity() {
        return new CategoryGamma().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        categoryGamma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCategoryGamma != null) {
            categoryGammaRepository.delete(insertedCategoryGamma);
            insertedCategoryGamma = null;
        }
    }

    @Test
    @Transactional
    void createCategoryGamma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CategoryGamma
        CategoryGammaDTO categoryGammaDTO = categoryGammaMapper.toDto(categoryGamma);
        var returnedCategoryGammaDTO = om.readValue(
            restCategoryGammaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryGammaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CategoryGammaDTO.class
        );

        // Validate the CategoryGamma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCategoryGamma = categoryGammaMapper.toEntity(returnedCategoryGammaDTO);
        assertCategoryGammaUpdatableFieldsEquals(returnedCategoryGamma, getPersistedCategoryGamma(returnedCategoryGamma));

        insertedCategoryGamma = returnedCategoryGamma;
    }

    @Test
    @Transactional
    void createCategoryGammaWithExistingId() throws Exception {
        // Create the CategoryGamma with an existing ID
        categoryGamma.setId(1L);
        CategoryGammaDTO categoryGammaDTO = categoryGammaMapper.toDto(categoryGamma);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoryGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryGammaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CategoryGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        categoryGamma.setName(null);

        // Create the CategoryGamma, which fails.
        CategoryGammaDTO categoryGammaDTO = categoryGammaMapper.toDto(categoryGamma);

        restCategoryGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryGammaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCategoryGammas() throws Exception {
        // Initialize the database
        insertedCategoryGamma = categoryGammaRepository.saveAndFlush(categoryGamma);

        // Get all the categoryGammaList
        restCategoryGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoryGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getCategoryGamma() throws Exception {
        // Initialize the database
        insertedCategoryGamma = categoryGammaRepository.saveAndFlush(categoryGamma);

        // Get the categoryGamma
        restCategoryGammaMockMvc
            .perform(get(ENTITY_API_URL_ID, categoryGamma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(categoryGamma.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getCategoryGammasByIdFiltering() throws Exception {
        // Initialize the database
        insertedCategoryGamma = categoryGammaRepository.saveAndFlush(categoryGamma);

        Long id = categoryGamma.getId();

        defaultCategoryGammaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCategoryGammaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCategoryGammaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCategoryGammasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCategoryGamma = categoryGammaRepository.saveAndFlush(categoryGamma);

        // Get all the categoryGammaList where name equals to
        defaultCategoryGammaFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCategoryGammasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCategoryGamma = categoryGammaRepository.saveAndFlush(categoryGamma);

        // Get all the categoryGammaList where name in
        defaultCategoryGammaFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCategoryGammasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCategoryGamma = categoryGammaRepository.saveAndFlush(categoryGamma);

        // Get all the categoryGammaList where name is not null
        defaultCategoryGammaFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoryGammasByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCategoryGamma = categoryGammaRepository.saveAndFlush(categoryGamma);

        // Get all the categoryGammaList where name contains
        defaultCategoryGammaFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCategoryGammasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCategoryGamma = categoryGammaRepository.saveAndFlush(categoryGamma);

        // Get all the categoryGammaList where name does not contain
        defaultCategoryGammaFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCategoryGammasByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCategoryGamma = categoryGammaRepository.saveAndFlush(categoryGamma);

        // Get all the categoryGammaList where description equals to
        defaultCategoryGammaFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCategoryGammasByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCategoryGamma = categoryGammaRepository.saveAndFlush(categoryGamma);

        // Get all the categoryGammaList where description in
        defaultCategoryGammaFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCategoryGammasByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCategoryGamma = categoryGammaRepository.saveAndFlush(categoryGamma);

        // Get all the categoryGammaList where description is not null
        defaultCategoryGammaFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoryGammasByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedCategoryGamma = categoryGammaRepository.saveAndFlush(categoryGamma);

        // Get all the categoryGammaList where description contains
        defaultCategoryGammaFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCategoryGammasByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCategoryGamma = categoryGammaRepository.saveAndFlush(categoryGamma);

        // Get all the categoryGammaList where description does not contain
        defaultCategoryGammaFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCategoryGammasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            categoryGammaRepository.saveAndFlush(categoryGamma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        categoryGamma.setTenant(tenant);
        categoryGammaRepository.saveAndFlush(categoryGamma);
        Long tenantId = tenant.getId();
        // Get all the categoryGammaList where tenant equals to tenantId
        defaultCategoryGammaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the categoryGammaList where tenant equals to (tenantId + 1)
        defaultCategoryGammaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultCategoryGammaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCategoryGammaShouldBeFound(shouldBeFound);
        defaultCategoryGammaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCategoryGammaShouldBeFound(String filter) throws Exception {
        restCategoryGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoryGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restCategoryGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCategoryGammaShouldNotBeFound(String filter) throws Exception {
        restCategoryGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCategoryGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCategoryGamma() throws Exception {
        // Get the categoryGamma
        restCategoryGammaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCategoryGamma() throws Exception {
        // Initialize the database
        insertedCategoryGamma = categoryGammaRepository.saveAndFlush(categoryGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the categoryGamma
        CategoryGamma updatedCategoryGamma = categoryGammaRepository.findById(categoryGamma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCategoryGamma are not directly saved in db
        em.detach(updatedCategoryGamma);
        updatedCategoryGamma.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        CategoryGammaDTO categoryGammaDTO = categoryGammaMapper.toDto(updatedCategoryGamma);

        restCategoryGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoryGammaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(categoryGammaDTO))
            )
            .andExpect(status().isOk());

        // Validate the CategoryGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCategoryGammaToMatchAllProperties(updatedCategoryGamma);
    }

    @Test
    @Transactional
    void putNonExistingCategoryGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryGamma.setId(longCount.incrementAndGet());

        // Create the CategoryGamma
        CategoryGammaDTO categoryGammaDTO = categoryGammaMapper.toDto(categoryGamma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoryGammaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(categoryGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCategoryGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryGamma.setId(longCount.incrementAndGet());

        // Create the CategoryGamma
        CategoryGammaDTO categoryGammaDTO = categoryGammaMapper.toDto(categoryGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(categoryGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCategoryGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryGamma.setId(longCount.incrementAndGet());

        // Create the CategoryGamma
        CategoryGammaDTO categoryGammaDTO = categoryGammaMapper.toDto(categoryGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryGammaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryGammaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategoryGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCategoryGammaWithPatch() throws Exception {
        // Initialize the database
        insertedCategoryGamma = categoryGammaRepository.saveAndFlush(categoryGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the categoryGamma using partial update
        CategoryGamma partialUpdatedCategoryGamma = new CategoryGamma();
        partialUpdatedCategoryGamma.setId(categoryGamma.getId());

        partialUpdatedCategoryGamma.name(UPDATED_NAME);

        restCategoryGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoryGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCategoryGamma))
            )
            .andExpect(status().isOk());

        // Validate the CategoryGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCategoryGammaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCategoryGamma, categoryGamma),
            getPersistedCategoryGamma(categoryGamma)
        );
    }

    @Test
    @Transactional
    void fullUpdateCategoryGammaWithPatch() throws Exception {
        // Initialize the database
        insertedCategoryGamma = categoryGammaRepository.saveAndFlush(categoryGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the categoryGamma using partial update
        CategoryGamma partialUpdatedCategoryGamma = new CategoryGamma();
        partialUpdatedCategoryGamma.setId(categoryGamma.getId());

        partialUpdatedCategoryGamma.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restCategoryGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoryGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCategoryGamma))
            )
            .andExpect(status().isOk());

        // Validate the CategoryGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCategoryGammaUpdatableFieldsEquals(partialUpdatedCategoryGamma, getPersistedCategoryGamma(partialUpdatedCategoryGamma));
    }

    @Test
    @Transactional
    void patchNonExistingCategoryGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryGamma.setId(longCount.incrementAndGet());

        // Create the CategoryGamma
        CategoryGammaDTO categoryGammaDTO = categoryGammaMapper.toDto(categoryGamma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, categoryGammaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(categoryGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCategoryGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryGamma.setId(longCount.incrementAndGet());

        // Create the CategoryGamma
        CategoryGammaDTO categoryGammaDTO = categoryGammaMapper.toDto(categoryGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(categoryGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCategoryGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryGamma.setId(longCount.incrementAndGet());

        // Create the CategoryGamma
        CategoryGammaDTO categoryGammaDTO = categoryGammaMapper.toDto(categoryGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryGammaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(categoryGammaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategoryGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCategoryGamma() throws Exception {
        // Initialize the database
        insertedCategoryGamma = categoryGammaRepository.saveAndFlush(categoryGamma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the categoryGamma
        restCategoryGammaMockMvc
            .perform(delete(ENTITY_API_URL_ID, categoryGamma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return categoryGammaRepository.count();
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

    protected CategoryGamma getPersistedCategoryGamma(CategoryGamma categoryGamma) {
        return categoryGammaRepository.findById(categoryGamma.getId()).orElseThrow();
    }

    protected void assertPersistedCategoryGammaToMatchAllProperties(CategoryGamma expectedCategoryGamma) {
        assertCategoryGammaAllPropertiesEquals(expectedCategoryGamma, getPersistedCategoryGamma(expectedCategoryGamma));
    }

    protected void assertPersistedCategoryGammaToMatchUpdatableProperties(CategoryGamma expectedCategoryGamma) {
        assertCategoryGammaAllUpdatablePropertiesEquals(expectedCategoryGamma, getPersistedCategoryGamma(expectedCategoryGamma));
    }
}
