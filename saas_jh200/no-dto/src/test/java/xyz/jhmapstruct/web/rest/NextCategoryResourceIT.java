package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextCategoryAsserts.*;
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
import xyz.jhmapstruct.domain.NextCategory;
import xyz.jhmapstruct.repository.NextCategoryRepository;

/**
 * Integration tests for the {@link NextCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/next-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextCategoryRepository nextCategoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextCategoryMockMvc;

    private NextCategory nextCategory;

    private NextCategory insertedNextCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextCategory createEntity() {
        return new NextCategory().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextCategory createUpdatedEntity() {
        return new NextCategory().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        nextCategory = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextCategory != null) {
            nextCategoryRepository.delete(insertedNextCategory);
            insertedNextCategory = null;
        }
    }

    @Test
    @Transactional
    void createNextCategory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextCategory
        var returnedNextCategory = om.readValue(
            restNextCategoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategory)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextCategory.class
        );

        // Validate the NextCategory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextCategoryUpdatableFieldsEquals(returnedNextCategory, getPersistedNextCategory(returnedNextCategory));

        insertedNextCategory = returnedNextCategory;
    }

    @Test
    @Transactional
    void createNextCategoryWithExistingId() throws Exception {
        // Create the NextCategory with an existing ID
        nextCategory.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategory)))
            .andExpect(status().isBadRequest());

        // Validate the NextCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCategory.setName(null);

        // Create the NextCategory, which fails.

        restNextCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategory)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextCategories() throws Exception {
        // Initialize the database
        insertedNextCategory = nextCategoryRepository.saveAndFlush(nextCategory);

        // Get all the nextCategoryList
        restNextCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getNextCategory() throws Exception {
        // Initialize the database
        insertedNextCategory = nextCategoryRepository.saveAndFlush(nextCategory);

        // Get the nextCategory
        restNextCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, nextCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNextCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextCategory = nextCategoryRepository.saveAndFlush(nextCategory);

        Long id = nextCategory.getId();

        defaultNextCategoryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextCategoryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextCategoryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextCategoriesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCategory = nextCategoryRepository.saveAndFlush(nextCategory);

        // Get all the nextCategoryList where name equals to
        defaultNextCategoryFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategoriesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCategory = nextCategoryRepository.saveAndFlush(nextCategory);

        // Get all the nextCategoryList where name in
        defaultNextCategoryFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategoriesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCategory = nextCategoryRepository.saveAndFlush(nextCategory);

        // Get all the nextCategoryList where name is not null
        defaultNextCategoryFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCategoriesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategory = nextCategoryRepository.saveAndFlush(nextCategory);

        // Get all the nextCategoryList where name contains
        defaultNextCategoryFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategoriesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategory = nextCategoryRepository.saveAndFlush(nextCategory);

        // Get all the nextCategoryList where name does not contain
        defaultNextCategoryFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategoriesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCategory = nextCategoryRepository.saveAndFlush(nextCategory);

        // Get all the nextCategoryList where description equals to
        defaultNextCategoryFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNextCategoriesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCategory = nextCategoryRepository.saveAndFlush(nextCategory);

        // Get all the nextCategoryList where description in
        defaultNextCategoryFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllNextCategoriesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCategory = nextCategoryRepository.saveAndFlush(nextCategory);

        // Get all the nextCategoryList where description is not null
        defaultNextCategoryFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCategoriesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategory = nextCategoryRepository.saveAndFlush(nextCategory);

        // Get all the nextCategoryList where description contains
        defaultNextCategoryFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNextCategoriesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategory = nextCategoryRepository.saveAndFlush(nextCategory);

        // Get all the nextCategoryList where description does not contain
        defaultNextCategoryFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllNextCategoriesByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextCategoryRepository.saveAndFlush(nextCategory);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextCategory.setTenant(tenant);
        nextCategoryRepository.saveAndFlush(nextCategory);
        Long tenantId = tenant.getId();
        // Get all the nextCategoryList where tenant equals to tenantId
        defaultNextCategoryShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextCategoryList where tenant equals to (tenantId + 1)
        defaultNextCategoryShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextCategoryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextCategoryShouldBeFound(shouldBeFound);
        defaultNextCategoryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextCategoryShouldBeFound(String filter) throws Exception {
        restNextCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restNextCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextCategoryShouldNotBeFound(String filter) throws Exception {
        restNextCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextCategory() throws Exception {
        // Get the nextCategory
        restNextCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextCategory() throws Exception {
        // Initialize the database
        insertedNextCategory = nextCategoryRepository.saveAndFlush(nextCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCategory
        NextCategory updatedNextCategory = nextCategoryRepository.findById(nextCategory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextCategory are not directly saved in db
        em.detach(updatedNextCategory);
        updatedNextCategory.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restNextCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextCategory))
            )
            .andExpect(status().isOk());

        // Validate the NextCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextCategoryToMatchAllProperties(updatedNextCategory);
    }

    @Test
    @Transactional
    void putNonExistingNextCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedNextCategory = nextCategoryRepository.saveAndFlush(nextCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCategory using partial update
        NextCategory partialUpdatedNextCategory = new NextCategory();
        partialUpdatedNextCategory.setId(nextCategory.getId());

        partialUpdatedNextCategory.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restNextCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCategory))
            )
            .andExpect(status().isOk());

        // Validate the NextCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCategoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextCategory, nextCategory),
            getPersistedNextCategory(nextCategory)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedNextCategory = nextCategoryRepository.saveAndFlush(nextCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCategory using partial update
        NextCategory partialUpdatedNextCategory = new NextCategory();
        partialUpdatedNextCategory.setId(nextCategory.getId());

        partialUpdatedNextCategory.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restNextCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCategory))
            )
            .andExpect(status().isOk());

        // Validate the NextCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCategoryUpdatableFieldsEquals(partialUpdatedNextCategory, getPersistedNextCategory(partialUpdatedNextCategory));
    }

    @Test
    @Transactional
    void patchNonExistingNextCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextCategory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextCategory() throws Exception {
        // Initialize the database
        insertedNextCategory = nextCategoryRepository.saveAndFlush(nextCategory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextCategory
        restNextCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextCategoryRepository.count();
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

    protected NextCategory getPersistedNextCategory(NextCategory nextCategory) {
        return nextCategoryRepository.findById(nextCategory.getId()).orElseThrow();
    }

    protected void assertPersistedNextCategoryToMatchAllProperties(NextCategory expectedNextCategory) {
        assertNextCategoryAllPropertiesEquals(expectedNextCategory, getPersistedNextCategory(expectedNextCategory));
    }

    protected void assertPersistedNextCategoryToMatchUpdatableProperties(NextCategory expectedNextCategory) {
        assertNextCategoryAllUpdatablePropertiesEquals(expectedNextCategory, getPersistedNextCategory(expectedNextCategory));
    }
}
