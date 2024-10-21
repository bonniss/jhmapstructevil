package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.CategoryViAsserts.*;
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
import xyz.jhmapstruct.domain.CategoryVi;
import xyz.jhmapstruct.repository.CategoryViRepository;

/**
 * Integration tests for the {@link CategoryViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CategoryViResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/category-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CategoryViRepository categoryViRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategoryViMockMvc;

    private CategoryVi categoryVi;

    private CategoryVi insertedCategoryVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoryVi createEntity() {
        return new CategoryVi().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoryVi createUpdatedEntity() {
        return new CategoryVi().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        categoryVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCategoryVi != null) {
            categoryViRepository.delete(insertedCategoryVi);
            insertedCategoryVi = null;
        }
    }

    @Test
    @Transactional
    void createCategoryVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CategoryVi
        var returnedCategoryVi = om.readValue(
            restCategoryViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CategoryVi.class
        );

        // Validate the CategoryVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCategoryViUpdatableFieldsEquals(returnedCategoryVi, getPersistedCategoryVi(returnedCategoryVi));

        insertedCategoryVi = returnedCategoryVi;
    }

    @Test
    @Transactional
    void createCategoryViWithExistingId() throws Exception {
        // Create the CategoryVi with an existing ID
        categoryVi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoryViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryVi)))
            .andExpect(status().isBadRequest());

        // Validate the CategoryVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        categoryVi.setName(null);

        // Create the CategoryVi, which fails.

        restCategoryViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCategoryVis() throws Exception {
        // Initialize the database
        insertedCategoryVi = categoryViRepository.saveAndFlush(categoryVi);

        // Get all the categoryViList
        restCategoryViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoryVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getCategoryVi() throws Exception {
        // Initialize the database
        insertedCategoryVi = categoryViRepository.saveAndFlush(categoryVi);

        // Get the categoryVi
        restCategoryViMockMvc
            .perform(get(ENTITY_API_URL_ID, categoryVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(categoryVi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingCategoryVi() throws Exception {
        // Get the categoryVi
        restCategoryViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCategoryVi() throws Exception {
        // Initialize the database
        insertedCategoryVi = categoryViRepository.saveAndFlush(categoryVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the categoryVi
        CategoryVi updatedCategoryVi = categoryViRepository.findById(categoryVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCategoryVi are not directly saved in db
        em.detach(updatedCategoryVi);
        updatedCategoryVi.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restCategoryViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCategoryVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCategoryVi))
            )
            .andExpect(status().isOk());

        // Validate the CategoryVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCategoryViToMatchAllProperties(updatedCategoryVi);
    }

    @Test
    @Transactional
    void putNonExistingCategoryVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoryVi.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCategoryVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(categoryVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCategoryVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategoryVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCategoryViWithPatch() throws Exception {
        // Initialize the database
        insertedCategoryVi = categoryViRepository.saveAndFlush(categoryVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the categoryVi using partial update
        CategoryVi partialUpdatedCategoryVi = new CategoryVi();
        partialUpdatedCategoryVi.setId(categoryVi.getId());

        partialUpdatedCategoryVi.name(UPDATED_NAME);

        restCategoryViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoryVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCategoryVi))
            )
            .andExpect(status().isOk());

        // Validate the CategoryVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCategoryViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCategoryVi, categoryVi),
            getPersistedCategoryVi(categoryVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateCategoryViWithPatch() throws Exception {
        // Initialize the database
        insertedCategoryVi = categoryViRepository.saveAndFlush(categoryVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the categoryVi using partial update
        CategoryVi partialUpdatedCategoryVi = new CategoryVi();
        partialUpdatedCategoryVi.setId(categoryVi.getId());

        partialUpdatedCategoryVi.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restCategoryViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoryVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCategoryVi))
            )
            .andExpect(status().isOk());

        // Validate the CategoryVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCategoryViUpdatableFieldsEquals(partialUpdatedCategoryVi, getPersistedCategoryVi(partialUpdatedCategoryVi));
    }

    @Test
    @Transactional
    void patchNonExistingCategoryVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, categoryVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(categoryVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCategoryVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(categoryVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCategoryVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(categoryVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategoryVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCategoryVi() throws Exception {
        // Initialize the database
        insertedCategoryVi = categoryViRepository.saveAndFlush(categoryVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the categoryVi
        restCategoryViMockMvc
            .perform(delete(ENTITY_API_URL_ID, categoryVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return categoryViRepository.count();
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

    protected CategoryVi getPersistedCategoryVi(CategoryVi categoryVi) {
        return categoryViRepository.findById(categoryVi.getId()).orElseThrow();
    }

    protected void assertPersistedCategoryViToMatchAllProperties(CategoryVi expectedCategoryVi) {
        assertCategoryViAllPropertiesEquals(expectedCategoryVi, getPersistedCategoryVi(expectedCategoryVi));
    }

    protected void assertPersistedCategoryViToMatchUpdatableProperties(CategoryVi expectedCategoryVi) {
        assertCategoryViAllUpdatablePropertiesEquals(expectedCategoryVi, getPersistedCategoryVi(expectedCategoryVi));
    }
}
