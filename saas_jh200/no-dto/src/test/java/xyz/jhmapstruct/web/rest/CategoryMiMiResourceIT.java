package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.CategoryMiMiAsserts.*;
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
import xyz.jhmapstruct.domain.CategoryMiMi;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.repository.CategoryMiMiRepository;

/**
 * Integration tests for the {@link CategoryMiMiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CategoryMiMiResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/category-mi-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CategoryMiMiRepository categoryMiMiRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategoryMiMiMockMvc;

    private CategoryMiMi categoryMiMi;

    private CategoryMiMi insertedCategoryMiMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoryMiMi createEntity() {
        return new CategoryMiMi().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoryMiMi createUpdatedEntity() {
        return new CategoryMiMi().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        categoryMiMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCategoryMiMi != null) {
            categoryMiMiRepository.delete(insertedCategoryMiMi);
            insertedCategoryMiMi = null;
        }
    }

    @Test
    @Transactional
    void createCategoryMiMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CategoryMiMi
        var returnedCategoryMiMi = om.readValue(
            restCategoryMiMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryMiMi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CategoryMiMi.class
        );

        // Validate the CategoryMiMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCategoryMiMiUpdatableFieldsEquals(returnedCategoryMiMi, getPersistedCategoryMiMi(returnedCategoryMiMi));

        insertedCategoryMiMi = returnedCategoryMiMi;
    }

    @Test
    @Transactional
    void createCategoryMiMiWithExistingId() throws Exception {
        // Create the CategoryMiMi with an existing ID
        categoryMiMi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoryMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryMiMi)))
            .andExpect(status().isBadRequest());

        // Validate the CategoryMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        categoryMiMi.setName(null);

        // Create the CategoryMiMi, which fails.

        restCategoryMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryMiMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCategoryMiMis() throws Exception {
        // Initialize the database
        insertedCategoryMiMi = categoryMiMiRepository.saveAndFlush(categoryMiMi);

        // Get all the categoryMiMiList
        restCategoryMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoryMiMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getCategoryMiMi() throws Exception {
        // Initialize the database
        insertedCategoryMiMi = categoryMiMiRepository.saveAndFlush(categoryMiMi);

        // Get the categoryMiMi
        restCategoryMiMiMockMvc
            .perform(get(ENTITY_API_URL_ID, categoryMiMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(categoryMiMi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getCategoryMiMisByIdFiltering() throws Exception {
        // Initialize the database
        insertedCategoryMiMi = categoryMiMiRepository.saveAndFlush(categoryMiMi);

        Long id = categoryMiMi.getId();

        defaultCategoryMiMiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCategoryMiMiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCategoryMiMiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCategoryMiMisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCategoryMiMi = categoryMiMiRepository.saveAndFlush(categoryMiMi);

        // Get all the categoryMiMiList where name equals to
        defaultCategoryMiMiFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCategoryMiMisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCategoryMiMi = categoryMiMiRepository.saveAndFlush(categoryMiMi);

        // Get all the categoryMiMiList where name in
        defaultCategoryMiMiFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCategoryMiMisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCategoryMiMi = categoryMiMiRepository.saveAndFlush(categoryMiMi);

        // Get all the categoryMiMiList where name is not null
        defaultCategoryMiMiFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoryMiMisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCategoryMiMi = categoryMiMiRepository.saveAndFlush(categoryMiMi);

        // Get all the categoryMiMiList where name contains
        defaultCategoryMiMiFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCategoryMiMisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCategoryMiMi = categoryMiMiRepository.saveAndFlush(categoryMiMi);

        // Get all the categoryMiMiList where name does not contain
        defaultCategoryMiMiFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCategoryMiMisByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCategoryMiMi = categoryMiMiRepository.saveAndFlush(categoryMiMi);

        // Get all the categoryMiMiList where description equals to
        defaultCategoryMiMiFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCategoryMiMisByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCategoryMiMi = categoryMiMiRepository.saveAndFlush(categoryMiMi);

        // Get all the categoryMiMiList where description in
        defaultCategoryMiMiFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCategoryMiMisByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCategoryMiMi = categoryMiMiRepository.saveAndFlush(categoryMiMi);

        // Get all the categoryMiMiList where description is not null
        defaultCategoryMiMiFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoryMiMisByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedCategoryMiMi = categoryMiMiRepository.saveAndFlush(categoryMiMi);

        // Get all the categoryMiMiList where description contains
        defaultCategoryMiMiFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCategoryMiMisByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCategoryMiMi = categoryMiMiRepository.saveAndFlush(categoryMiMi);

        // Get all the categoryMiMiList where description does not contain
        defaultCategoryMiMiFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCategoryMiMisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            categoryMiMiRepository.saveAndFlush(categoryMiMi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        categoryMiMi.setTenant(tenant);
        categoryMiMiRepository.saveAndFlush(categoryMiMi);
        Long tenantId = tenant.getId();
        // Get all the categoryMiMiList where tenant equals to tenantId
        defaultCategoryMiMiShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the categoryMiMiList where tenant equals to (tenantId + 1)
        defaultCategoryMiMiShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultCategoryMiMiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCategoryMiMiShouldBeFound(shouldBeFound);
        defaultCategoryMiMiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCategoryMiMiShouldBeFound(String filter) throws Exception {
        restCategoryMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoryMiMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restCategoryMiMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCategoryMiMiShouldNotBeFound(String filter) throws Exception {
        restCategoryMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCategoryMiMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCategoryMiMi() throws Exception {
        // Get the categoryMiMi
        restCategoryMiMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCategoryMiMi() throws Exception {
        // Initialize the database
        insertedCategoryMiMi = categoryMiMiRepository.saveAndFlush(categoryMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the categoryMiMi
        CategoryMiMi updatedCategoryMiMi = categoryMiMiRepository.findById(categoryMiMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCategoryMiMi are not directly saved in db
        em.detach(updatedCategoryMiMi);
        updatedCategoryMiMi.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restCategoryMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCategoryMiMi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCategoryMiMi))
            )
            .andExpect(status().isOk());

        // Validate the CategoryMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCategoryMiMiToMatchAllProperties(updatedCategoryMiMi);
    }

    @Test
    @Transactional
    void putNonExistingCategoryMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryMiMi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoryMiMi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(categoryMiMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCategoryMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryMiMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(categoryMiMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCategoryMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryMiMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryMiMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryMiMi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategoryMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCategoryMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedCategoryMiMi = categoryMiMiRepository.saveAndFlush(categoryMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the categoryMiMi using partial update
        CategoryMiMi partialUpdatedCategoryMiMi = new CategoryMiMi();
        partialUpdatedCategoryMiMi.setId(categoryMiMi.getId());

        partialUpdatedCategoryMiMi.description(UPDATED_DESCRIPTION);

        restCategoryMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoryMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCategoryMiMi))
            )
            .andExpect(status().isOk());

        // Validate the CategoryMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCategoryMiMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCategoryMiMi, categoryMiMi),
            getPersistedCategoryMiMi(categoryMiMi)
        );
    }

    @Test
    @Transactional
    void fullUpdateCategoryMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedCategoryMiMi = categoryMiMiRepository.saveAndFlush(categoryMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the categoryMiMi using partial update
        CategoryMiMi partialUpdatedCategoryMiMi = new CategoryMiMi();
        partialUpdatedCategoryMiMi.setId(categoryMiMi.getId());

        partialUpdatedCategoryMiMi.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restCategoryMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoryMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCategoryMiMi))
            )
            .andExpect(status().isOk());

        // Validate the CategoryMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCategoryMiMiUpdatableFieldsEquals(partialUpdatedCategoryMiMi, getPersistedCategoryMiMi(partialUpdatedCategoryMiMi));
    }

    @Test
    @Transactional
    void patchNonExistingCategoryMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryMiMi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, categoryMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(categoryMiMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCategoryMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryMiMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(categoryMiMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCategoryMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryMiMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryMiMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(categoryMiMi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategoryMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCategoryMiMi() throws Exception {
        // Initialize the database
        insertedCategoryMiMi = categoryMiMiRepository.saveAndFlush(categoryMiMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the categoryMiMi
        restCategoryMiMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, categoryMiMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return categoryMiMiRepository.count();
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

    protected CategoryMiMi getPersistedCategoryMiMi(CategoryMiMi categoryMiMi) {
        return categoryMiMiRepository.findById(categoryMiMi.getId()).orElseThrow();
    }

    protected void assertPersistedCategoryMiMiToMatchAllProperties(CategoryMiMi expectedCategoryMiMi) {
        assertCategoryMiMiAllPropertiesEquals(expectedCategoryMiMi, getPersistedCategoryMiMi(expectedCategoryMiMi));
    }

    protected void assertPersistedCategoryMiMiToMatchUpdatableProperties(CategoryMiMi expectedCategoryMiMi) {
        assertCategoryMiMiAllUpdatablePropertiesEquals(expectedCategoryMiMi, getPersistedCategoryMiMi(expectedCategoryMiMi));
    }
}
