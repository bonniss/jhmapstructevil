package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.CategoryMiAsserts.*;
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
import xyz.jhmapstruct.domain.CategoryMi;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.repository.CategoryMiRepository;
import xyz.jhmapstruct.service.dto.CategoryMiDTO;
import xyz.jhmapstruct.service.mapper.CategoryMiMapper;

/**
 * Integration tests for the {@link CategoryMiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CategoryMiResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/category-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CategoryMiRepository categoryMiRepository;

    @Autowired
    private CategoryMiMapper categoryMiMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategoryMiMockMvc;

    private CategoryMi categoryMi;

    private CategoryMi insertedCategoryMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoryMi createEntity() {
        return new CategoryMi().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoryMi createUpdatedEntity() {
        return new CategoryMi().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        categoryMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCategoryMi != null) {
            categoryMiRepository.delete(insertedCategoryMi);
            insertedCategoryMi = null;
        }
    }

    @Test
    @Transactional
    void createCategoryMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CategoryMi
        CategoryMiDTO categoryMiDTO = categoryMiMapper.toDto(categoryMi);
        var returnedCategoryMiDTO = om.readValue(
            restCategoryMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryMiDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CategoryMiDTO.class
        );

        // Validate the CategoryMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCategoryMi = categoryMiMapper.toEntity(returnedCategoryMiDTO);
        assertCategoryMiUpdatableFieldsEquals(returnedCategoryMi, getPersistedCategoryMi(returnedCategoryMi));

        insertedCategoryMi = returnedCategoryMi;
    }

    @Test
    @Transactional
    void createCategoryMiWithExistingId() throws Exception {
        // Create the CategoryMi with an existing ID
        categoryMi.setId(1L);
        CategoryMiDTO categoryMiDTO = categoryMiMapper.toDto(categoryMi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoryMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryMiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CategoryMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        categoryMi.setName(null);

        // Create the CategoryMi, which fails.
        CategoryMiDTO categoryMiDTO = categoryMiMapper.toDto(categoryMi);

        restCategoryMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCategoryMis() throws Exception {
        // Initialize the database
        insertedCategoryMi = categoryMiRepository.saveAndFlush(categoryMi);

        // Get all the categoryMiList
        restCategoryMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoryMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getCategoryMi() throws Exception {
        // Initialize the database
        insertedCategoryMi = categoryMiRepository.saveAndFlush(categoryMi);

        // Get the categoryMi
        restCategoryMiMockMvc
            .perform(get(ENTITY_API_URL_ID, categoryMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(categoryMi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getCategoryMisByIdFiltering() throws Exception {
        // Initialize the database
        insertedCategoryMi = categoryMiRepository.saveAndFlush(categoryMi);

        Long id = categoryMi.getId();

        defaultCategoryMiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCategoryMiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCategoryMiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCategoryMisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCategoryMi = categoryMiRepository.saveAndFlush(categoryMi);

        // Get all the categoryMiList where name equals to
        defaultCategoryMiFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCategoryMisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCategoryMi = categoryMiRepository.saveAndFlush(categoryMi);

        // Get all the categoryMiList where name in
        defaultCategoryMiFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCategoryMisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCategoryMi = categoryMiRepository.saveAndFlush(categoryMi);

        // Get all the categoryMiList where name is not null
        defaultCategoryMiFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoryMisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCategoryMi = categoryMiRepository.saveAndFlush(categoryMi);

        // Get all the categoryMiList where name contains
        defaultCategoryMiFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCategoryMisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCategoryMi = categoryMiRepository.saveAndFlush(categoryMi);

        // Get all the categoryMiList where name does not contain
        defaultCategoryMiFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCategoryMisByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCategoryMi = categoryMiRepository.saveAndFlush(categoryMi);

        // Get all the categoryMiList where description equals to
        defaultCategoryMiFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCategoryMisByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCategoryMi = categoryMiRepository.saveAndFlush(categoryMi);

        // Get all the categoryMiList where description in
        defaultCategoryMiFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCategoryMisByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCategoryMi = categoryMiRepository.saveAndFlush(categoryMi);

        // Get all the categoryMiList where description is not null
        defaultCategoryMiFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoryMisByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedCategoryMi = categoryMiRepository.saveAndFlush(categoryMi);

        // Get all the categoryMiList where description contains
        defaultCategoryMiFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCategoryMisByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCategoryMi = categoryMiRepository.saveAndFlush(categoryMi);

        // Get all the categoryMiList where description does not contain
        defaultCategoryMiFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCategoryMisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            categoryMiRepository.saveAndFlush(categoryMi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        categoryMi.setTenant(tenant);
        categoryMiRepository.saveAndFlush(categoryMi);
        Long tenantId = tenant.getId();
        // Get all the categoryMiList where tenant equals to tenantId
        defaultCategoryMiShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the categoryMiList where tenant equals to (tenantId + 1)
        defaultCategoryMiShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultCategoryMiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCategoryMiShouldBeFound(shouldBeFound);
        defaultCategoryMiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCategoryMiShouldBeFound(String filter) throws Exception {
        restCategoryMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoryMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restCategoryMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCategoryMiShouldNotBeFound(String filter) throws Exception {
        restCategoryMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCategoryMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCategoryMi() throws Exception {
        // Get the categoryMi
        restCategoryMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCategoryMi() throws Exception {
        // Initialize the database
        insertedCategoryMi = categoryMiRepository.saveAndFlush(categoryMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the categoryMi
        CategoryMi updatedCategoryMi = categoryMiRepository.findById(categoryMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCategoryMi are not directly saved in db
        em.detach(updatedCategoryMi);
        updatedCategoryMi.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        CategoryMiDTO categoryMiDTO = categoryMiMapper.toDto(updatedCategoryMi);

        restCategoryMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoryMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(categoryMiDTO))
            )
            .andExpect(status().isOk());

        // Validate the CategoryMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCategoryMiToMatchAllProperties(updatedCategoryMi);
    }

    @Test
    @Transactional
    void putNonExistingCategoryMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryMi.setId(longCount.incrementAndGet());

        // Create the CategoryMi
        CategoryMiDTO categoryMiDTO = categoryMiMapper.toDto(categoryMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoryMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(categoryMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCategoryMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryMi.setId(longCount.incrementAndGet());

        // Create the CategoryMi
        CategoryMiDTO categoryMiDTO = categoryMiMapper.toDto(categoryMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(categoryMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCategoryMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryMi.setId(longCount.incrementAndGet());

        // Create the CategoryMi
        CategoryMiDTO categoryMiDTO = categoryMiMapper.toDto(categoryMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategoryMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCategoryMiWithPatch() throws Exception {
        // Initialize the database
        insertedCategoryMi = categoryMiRepository.saveAndFlush(categoryMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the categoryMi using partial update
        CategoryMi partialUpdatedCategoryMi = new CategoryMi();
        partialUpdatedCategoryMi.setId(categoryMi.getId());

        partialUpdatedCategoryMi.description(UPDATED_DESCRIPTION);

        restCategoryMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoryMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCategoryMi))
            )
            .andExpect(status().isOk());

        // Validate the CategoryMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCategoryMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCategoryMi, categoryMi),
            getPersistedCategoryMi(categoryMi)
        );
    }

    @Test
    @Transactional
    void fullUpdateCategoryMiWithPatch() throws Exception {
        // Initialize the database
        insertedCategoryMi = categoryMiRepository.saveAndFlush(categoryMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the categoryMi using partial update
        CategoryMi partialUpdatedCategoryMi = new CategoryMi();
        partialUpdatedCategoryMi.setId(categoryMi.getId());

        partialUpdatedCategoryMi.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restCategoryMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoryMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCategoryMi))
            )
            .andExpect(status().isOk());

        // Validate the CategoryMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCategoryMiUpdatableFieldsEquals(partialUpdatedCategoryMi, getPersistedCategoryMi(partialUpdatedCategoryMi));
    }

    @Test
    @Transactional
    void patchNonExistingCategoryMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryMi.setId(longCount.incrementAndGet());

        // Create the CategoryMi
        CategoryMiDTO categoryMiDTO = categoryMiMapper.toDto(categoryMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, categoryMiDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(categoryMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCategoryMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryMi.setId(longCount.incrementAndGet());

        // Create the CategoryMi
        CategoryMiDTO categoryMiDTO = categoryMiMapper.toDto(categoryMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(categoryMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCategoryMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryMi.setId(longCount.incrementAndGet());

        // Create the CategoryMi
        CategoryMiDTO categoryMiDTO = categoryMiMapper.toDto(categoryMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(categoryMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategoryMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCategoryMi() throws Exception {
        // Initialize the database
        insertedCategoryMi = categoryMiRepository.saveAndFlush(categoryMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the categoryMi
        restCategoryMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, categoryMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return categoryMiRepository.count();
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

    protected CategoryMi getPersistedCategoryMi(CategoryMi categoryMi) {
        return categoryMiRepository.findById(categoryMi.getId()).orElseThrow();
    }

    protected void assertPersistedCategoryMiToMatchAllProperties(CategoryMi expectedCategoryMi) {
        assertCategoryMiAllPropertiesEquals(expectedCategoryMi, getPersistedCategoryMi(expectedCategoryMi));
    }

    protected void assertPersistedCategoryMiToMatchUpdatableProperties(CategoryMi expectedCategoryMi) {
        assertCategoryMiAllUpdatablePropertiesEquals(expectedCategoryMi, getPersistedCategoryMi(expectedCategoryMi));
    }
}
