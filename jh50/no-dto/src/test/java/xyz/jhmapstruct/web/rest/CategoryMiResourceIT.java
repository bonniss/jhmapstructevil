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
import xyz.jhmapstruct.repository.CategoryMiRepository;

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
        var returnedCategoryMi = om.readValue(
            restCategoryMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryMi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CategoryMi.class
        );

        // Validate the CategoryMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCategoryMiUpdatableFieldsEquals(returnedCategoryMi, getPersistedCategoryMi(returnedCategoryMi));

        insertedCategoryMi = returnedCategoryMi;
    }

    @Test
    @Transactional
    void createCategoryMiWithExistingId() throws Exception {
        // Create the CategoryMi with an existing ID
        categoryMi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoryMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryMi)))
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

        restCategoryMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryMi)))
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

        restCategoryMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCategoryMi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCategoryMi))
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

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoryMi.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryMi))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(categoryMi))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryMi)))
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

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, categoryMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(categoryMi))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(categoryMi))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(categoryMi)))
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
