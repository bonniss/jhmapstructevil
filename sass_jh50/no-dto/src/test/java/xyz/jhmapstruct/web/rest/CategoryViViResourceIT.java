package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.CategoryViViAsserts.*;
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
import xyz.jhmapstruct.domain.CategoryViVi;
import xyz.jhmapstruct.repository.CategoryViViRepository;

/**
 * Integration tests for the {@link CategoryViViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CategoryViViResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/category-vi-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CategoryViViRepository categoryViViRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategoryViViMockMvc;

    private CategoryViVi categoryViVi;

    private CategoryViVi insertedCategoryViVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoryViVi createEntity() {
        return new CategoryViVi().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoryViVi createUpdatedEntity() {
        return new CategoryViVi().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        categoryViVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCategoryViVi != null) {
            categoryViViRepository.delete(insertedCategoryViVi);
            insertedCategoryViVi = null;
        }
    }

    @Test
    @Transactional
    void createCategoryViVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CategoryViVi
        var returnedCategoryViVi = om.readValue(
            restCategoryViViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryViVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CategoryViVi.class
        );

        // Validate the CategoryViVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCategoryViViUpdatableFieldsEquals(returnedCategoryViVi, getPersistedCategoryViVi(returnedCategoryViVi));

        insertedCategoryViVi = returnedCategoryViVi;
    }

    @Test
    @Transactional
    void createCategoryViViWithExistingId() throws Exception {
        // Create the CategoryViVi with an existing ID
        categoryViVi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoryViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryViVi)))
            .andExpect(status().isBadRequest());

        // Validate the CategoryViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        categoryViVi.setName(null);

        // Create the CategoryViVi, which fails.

        restCategoryViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryViVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCategoryViVis() throws Exception {
        // Initialize the database
        insertedCategoryViVi = categoryViViRepository.saveAndFlush(categoryViVi);

        // Get all the categoryViViList
        restCategoryViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoryViVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getCategoryViVi() throws Exception {
        // Initialize the database
        insertedCategoryViVi = categoryViViRepository.saveAndFlush(categoryViVi);

        // Get the categoryViVi
        restCategoryViViMockMvc
            .perform(get(ENTITY_API_URL_ID, categoryViVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(categoryViVi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingCategoryViVi() throws Exception {
        // Get the categoryViVi
        restCategoryViViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCategoryViVi() throws Exception {
        // Initialize the database
        insertedCategoryViVi = categoryViViRepository.saveAndFlush(categoryViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the categoryViVi
        CategoryViVi updatedCategoryViVi = categoryViViRepository.findById(categoryViVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCategoryViVi are not directly saved in db
        em.detach(updatedCategoryViVi);
        updatedCategoryViVi.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restCategoryViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCategoryViVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCategoryViVi))
            )
            .andExpect(status().isOk());

        // Validate the CategoryViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCategoryViViToMatchAllProperties(updatedCategoryViVi);
    }

    @Test
    @Transactional
    void putNonExistingCategoryViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryViVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoryViVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(categoryViVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCategoryViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryViVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(categoryViVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCategoryViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryViVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryViViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryViVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategoryViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCategoryViViWithPatch() throws Exception {
        // Initialize the database
        insertedCategoryViVi = categoryViViRepository.saveAndFlush(categoryViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the categoryViVi using partial update
        CategoryViVi partialUpdatedCategoryViVi = new CategoryViVi();
        partialUpdatedCategoryViVi.setId(categoryViVi.getId());

        partialUpdatedCategoryViVi.description(UPDATED_DESCRIPTION);

        restCategoryViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoryViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCategoryViVi))
            )
            .andExpect(status().isOk());

        // Validate the CategoryViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCategoryViViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCategoryViVi, categoryViVi),
            getPersistedCategoryViVi(categoryViVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateCategoryViViWithPatch() throws Exception {
        // Initialize the database
        insertedCategoryViVi = categoryViViRepository.saveAndFlush(categoryViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the categoryViVi using partial update
        CategoryViVi partialUpdatedCategoryViVi = new CategoryViVi();
        partialUpdatedCategoryViVi.setId(categoryViVi.getId());

        partialUpdatedCategoryViVi.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restCategoryViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoryViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCategoryViVi))
            )
            .andExpect(status().isOk());

        // Validate the CategoryViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCategoryViViUpdatableFieldsEquals(partialUpdatedCategoryViVi, getPersistedCategoryViVi(partialUpdatedCategoryViVi));
    }

    @Test
    @Transactional
    void patchNonExistingCategoryViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryViVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, categoryViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(categoryViVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCategoryViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryViVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(categoryViVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCategoryViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryViVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryViViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(categoryViVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategoryViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCategoryViVi() throws Exception {
        // Initialize the database
        insertedCategoryViVi = categoryViViRepository.saveAndFlush(categoryViVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the categoryViVi
        restCategoryViViMockMvc
            .perform(delete(ENTITY_API_URL_ID, categoryViVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return categoryViViRepository.count();
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

    protected CategoryViVi getPersistedCategoryViVi(CategoryViVi categoryViVi) {
        return categoryViViRepository.findById(categoryViVi.getId()).orElseThrow();
    }

    protected void assertPersistedCategoryViViToMatchAllProperties(CategoryViVi expectedCategoryViVi) {
        assertCategoryViViAllPropertiesEquals(expectedCategoryViVi, getPersistedCategoryViVi(expectedCategoryViVi));
    }

    protected void assertPersistedCategoryViViToMatchUpdatableProperties(CategoryViVi expectedCategoryViVi) {
        assertCategoryViViAllUpdatablePropertiesEquals(expectedCategoryViVi, getPersistedCategoryViVi(expectedCategoryViVi));
    }
}