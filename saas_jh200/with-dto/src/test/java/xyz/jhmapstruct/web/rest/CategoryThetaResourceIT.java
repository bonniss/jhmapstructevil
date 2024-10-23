package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.CategoryThetaAsserts.*;
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
import xyz.jhmapstruct.domain.CategoryTheta;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.repository.CategoryThetaRepository;
import xyz.jhmapstruct.service.dto.CategoryThetaDTO;
import xyz.jhmapstruct.service.mapper.CategoryThetaMapper;

/**
 * Integration tests for the {@link CategoryThetaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CategoryThetaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/category-thetas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CategoryThetaRepository categoryThetaRepository;

    @Autowired
    private CategoryThetaMapper categoryThetaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategoryThetaMockMvc;

    private CategoryTheta categoryTheta;

    private CategoryTheta insertedCategoryTheta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoryTheta createEntity() {
        return new CategoryTheta().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoryTheta createUpdatedEntity() {
        return new CategoryTheta().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        categoryTheta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCategoryTheta != null) {
            categoryThetaRepository.delete(insertedCategoryTheta);
            insertedCategoryTheta = null;
        }
    }

    @Test
    @Transactional
    void createCategoryTheta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CategoryTheta
        CategoryThetaDTO categoryThetaDTO = categoryThetaMapper.toDto(categoryTheta);
        var returnedCategoryThetaDTO = om.readValue(
            restCategoryThetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryThetaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CategoryThetaDTO.class
        );

        // Validate the CategoryTheta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCategoryTheta = categoryThetaMapper.toEntity(returnedCategoryThetaDTO);
        assertCategoryThetaUpdatableFieldsEquals(returnedCategoryTheta, getPersistedCategoryTheta(returnedCategoryTheta));

        insertedCategoryTheta = returnedCategoryTheta;
    }

    @Test
    @Transactional
    void createCategoryThetaWithExistingId() throws Exception {
        // Create the CategoryTheta with an existing ID
        categoryTheta.setId(1L);
        CategoryThetaDTO categoryThetaDTO = categoryThetaMapper.toDto(categoryTheta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoryThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryThetaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CategoryTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        categoryTheta.setName(null);

        // Create the CategoryTheta, which fails.
        CategoryThetaDTO categoryThetaDTO = categoryThetaMapper.toDto(categoryTheta);

        restCategoryThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryThetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCategoryThetas() throws Exception {
        // Initialize the database
        insertedCategoryTheta = categoryThetaRepository.saveAndFlush(categoryTheta);

        // Get all the categoryThetaList
        restCategoryThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoryTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getCategoryTheta() throws Exception {
        // Initialize the database
        insertedCategoryTheta = categoryThetaRepository.saveAndFlush(categoryTheta);

        // Get the categoryTheta
        restCategoryThetaMockMvc
            .perform(get(ENTITY_API_URL_ID, categoryTheta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(categoryTheta.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getCategoryThetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedCategoryTheta = categoryThetaRepository.saveAndFlush(categoryTheta);

        Long id = categoryTheta.getId();

        defaultCategoryThetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCategoryThetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCategoryThetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCategoryThetasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCategoryTheta = categoryThetaRepository.saveAndFlush(categoryTheta);

        // Get all the categoryThetaList where name equals to
        defaultCategoryThetaFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCategoryThetasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCategoryTheta = categoryThetaRepository.saveAndFlush(categoryTheta);

        // Get all the categoryThetaList where name in
        defaultCategoryThetaFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCategoryThetasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCategoryTheta = categoryThetaRepository.saveAndFlush(categoryTheta);

        // Get all the categoryThetaList where name is not null
        defaultCategoryThetaFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoryThetasByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCategoryTheta = categoryThetaRepository.saveAndFlush(categoryTheta);

        // Get all the categoryThetaList where name contains
        defaultCategoryThetaFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCategoryThetasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCategoryTheta = categoryThetaRepository.saveAndFlush(categoryTheta);

        // Get all the categoryThetaList where name does not contain
        defaultCategoryThetaFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCategoryThetasByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCategoryTheta = categoryThetaRepository.saveAndFlush(categoryTheta);

        // Get all the categoryThetaList where description equals to
        defaultCategoryThetaFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCategoryThetasByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCategoryTheta = categoryThetaRepository.saveAndFlush(categoryTheta);

        // Get all the categoryThetaList where description in
        defaultCategoryThetaFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCategoryThetasByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCategoryTheta = categoryThetaRepository.saveAndFlush(categoryTheta);

        // Get all the categoryThetaList where description is not null
        defaultCategoryThetaFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoryThetasByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedCategoryTheta = categoryThetaRepository.saveAndFlush(categoryTheta);

        // Get all the categoryThetaList where description contains
        defaultCategoryThetaFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCategoryThetasByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCategoryTheta = categoryThetaRepository.saveAndFlush(categoryTheta);

        // Get all the categoryThetaList where description does not contain
        defaultCategoryThetaFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCategoryThetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            categoryThetaRepository.saveAndFlush(categoryTheta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        categoryTheta.setTenant(tenant);
        categoryThetaRepository.saveAndFlush(categoryTheta);
        Long tenantId = tenant.getId();
        // Get all the categoryThetaList where tenant equals to tenantId
        defaultCategoryThetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the categoryThetaList where tenant equals to (tenantId + 1)
        defaultCategoryThetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultCategoryThetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCategoryThetaShouldBeFound(shouldBeFound);
        defaultCategoryThetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCategoryThetaShouldBeFound(String filter) throws Exception {
        restCategoryThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoryTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restCategoryThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCategoryThetaShouldNotBeFound(String filter) throws Exception {
        restCategoryThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCategoryThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCategoryTheta() throws Exception {
        // Get the categoryTheta
        restCategoryThetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCategoryTheta() throws Exception {
        // Initialize the database
        insertedCategoryTheta = categoryThetaRepository.saveAndFlush(categoryTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the categoryTheta
        CategoryTheta updatedCategoryTheta = categoryThetaRepository.findById(categoryTheta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCategoryTheta are not directly saved in db
        em.detach(updatedCategoryTheta);
        updatedCategoryTheta.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        CategoryThetaDTO categoryThetaDTO = categoryThetaMapper.toDto(updatedCategoryTheta);

        restCategoryThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoryThetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(categoryThetaDTO))
            )
            .andExpect(status().isOk());

        // Validate the CategoryTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCategoryThetaToMatchAllProperties(updatedCategoryTheta);
    }

    @Test
    @Transactional
    void putNonExistingCategoryTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryTheta.setId(longCount.incrementAndGet());

        // Create the CategoryTheta
        CategoryThetaDTO categoryThetaDTO = categoryThetaMapper.toDto(categoryTheta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoryThetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(categoryThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCategoryTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryTheta.setId(longCount.incrementAndGet());

        // Create the CategoryTheta
        CategoryThetaDTO categoryThetaDTO = categoryThetaMapper.toDto(categoryTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(categoryThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCategoryTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryTheta.setId(longCount.incrementAndGet());

        // Create the CategoryTheta
        CategoryThetaDTO categoryThetaDTO = categoryThetaMapper.toDto(categoryTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryThetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categoryThetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategoryTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCategoryThetaWithPatch() throws Exception {
        // Initialize the database
        insertedCategoryTheta = categoryThetaRepository.saveAndFlush(categoryTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the categoryTheta using partial update
        CategoryTheta partialUpdatedCategoryTheta = new CategoryTheta();
        partialUpdatedCategoryTheta.setId(categoryTheta.getId());

        partialUpdatedCategoryTheta.name(UPDATED_NAME);

        restCategoryThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoryTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCategoryTheta))
            )
            .andExpect(status().isOk());

        // Validate the CategoryTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCategoryThetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCategoryTheta, categoryTheta),
            getPersistedCategoryTheta(categoryTheta)
        );
    }

    @Test
    @Transactional
    void fullUpdateCategoryThetaWithPatch() throws Exception {
        // Initialize the database
        insertedCategoryTheta = categoryThetaRepository.saveAndFlush(categoryTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the categoryTheta using partial update
        CategoryTheta partialUpdatedCategoryTheta = new CategoryTheta();
        partialUpdatedCategoryTheta.setId(categoryTheta.getId());

        partialUpdatedCategoryTheta.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restCategoryThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoryTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCategoryTheta))
            )
            .andExpect(status().isOk());

        // Validate the CategoryTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCategoryThetaUpdatableFieldsEquals(partialUpdatedCategoryTheta, getPersistedCategoryTheta(partialUpdatedCategoryTheta));
    }

    @Test
    @Transactional
    void patchNonExistingCategoryTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryTheta.setId(longCount.incrementAndGet());

        // Create the CategoryTheta
        CategoryThetaDTO categoryThetaDTO = categoryThetaMapper.toDto(categoryTheta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, categoryThetaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(categoryThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCategoryTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryTheta.setId(longCount.incrementAndGet());

        // Create the CategoryTheta
        CategoryThetaDTO categoryThetaDTO = categoryThetaMapper.toDto(categoryTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(categoryThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCategoryTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categoryTheta.setId(longCount.incrementAndGet());

        // Create the CategoryTheta
        CategoryThetaDTO categoryThetaDTO = categoryThetaMapper.toDto(categoryTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryThetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(categoryThetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategoryTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCategoryTheta() throws Exception {
        // Initialize the database
        insertedCategoryTheta = categoryThetaRepository.saveAndFlush(categoryTheta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the categoryTheta
        restCategoryThetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, categoryTheta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return categoryThetaRepository.count();
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

    protected CategoryTheta getPersistedCategoryTheta(CategoryTheta categoryTheta) {
        return categoryThetaRepository.findById(categoryTheta.getId()).orElseThrow();
    }

    protected void assertPersistedCategoryThetaToMatchAllProperties(CategoryTheta expectedCategoryTheta) {
        assertCategoryThetaAllPropertiesEquals(expectedCategoryTheta, getPersistedCategoryTheta(expectedCategoryTheta));
    }

    protected void assertPersistedCategoryThetaToMatchUpdatableProperties(CategoryTheta expectedCategoryTheta) {
        assertCategoryThetaAllUpdatablePropertiesEquals(expectedCategoryTheta, getPersistedCategoryTheta(expectedCategoryTheta));
    }
}
