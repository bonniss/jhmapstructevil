package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextCategorySigmaAsserts.*;
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
import xyz.jhmapstruct.domain.NextCategorySigma;
import xyz.jhmapstruct.repository.NextCategorySigmaRepository;
import xyz.jhmapstruct.service.dto.NextCategorySigmaDTO;
import xyz.jhmapstruct.service.mapper.NextCategorySigmaMapper;

/**
 * Integration tests for the {@link NextCategorySigmaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextCategorySigmaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/next-category-sigmas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextCategorySigmaRepository nextCategorySigmaRepository;

    @Autowired
    private NextCategorySigmaMapper nextCategorySigmaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextCategorySigmaMockMvc;

    private NextCategorySigma nextCategorySigma;

    private NextCategorySigma insertedNextCategorySigma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextCategorySigma createEntity() {
        return new NextCategorySigma().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextCategorySigma createUpdatedEntity() {
        return new NextCategorySigma().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        nextCategorySigma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextCategorySigma != null) {
            nextCategorySigmaRepository.delete(insertedNextCategorySigma);
            insertedNextCategorySigma = null;
        }
    }

    @Test
    @Transactional
    void createNextCategorySigma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextCategorySigma
        NextCategorySigmaDTO nextCategorySigmaDTO = nextCategorySigmaMapper.toDto(nextCategorySigma);
        var returnedNextCategorySigmaDTO = om.readValue(
            restNextCategorySigmaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategorySigmaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextCategorySigmaDTO.class
        );

        // Validate the NextCategorySigma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextCategorySigma = nextCategorySigmaMapper.toEntity(returnedNextCategorySigmaDTO);
        assertNextCategorySigmaUpdatableFieldsEquals(returnedNextCategorySigma, getPersistedNextCategorySigma(returnedNextCategorySigma));

        insertedNextCategorySigma = returnedNextCategorySigma;
    }

    @Test
    @Transactional
    void createNextCategorySigmaWithExistingId() throws Exception {
        // Create the NextCategorySigma with an existing ID
        nextCategorySigma.setId(1L);
        NextCategorySigmaDTO nextCategorySigmaDTO = nextCategorySigmaMapper.toDto(nextCategorySigma);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextCategorySigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategorySigmaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextCategorySigma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCategorySigma.setName(null);

        // Create the NextCategorySigma, which fails.
        NextCategorySigmaDTO nextCategorySigmaDTO = nextCategorySigmaMapper.toDto(nextCategorySigma);

        restNextCategorySigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategorySigmaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextCategorySigmas() throws Exception {
        // Initialize the database
        insertedNextCategorySigma = nextCategorySigmaRepository.saveAndFlush(nextCategorySigma);

        // Get all the nextCategorySigmaList
        restNextCategorySigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCategorySigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getNextCategorySigma() throws Exception {
        // Initialize the database
        insertedNextCategorySigma = nextCategorySigmaRepository.saveAndFlush(nextCategorySigma);

        // Get the nextCategorySigma
        restNextCategorySigmaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextCategorySigma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextCategorySigma.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNextCategorySigmasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextCategorySigma = nextCategorySigmaRepository.saveAndFlush(nextCategorySigma);

        Long id = nextCategorySigma.getId();

        defaultNextCategorySigmaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextCategorySigmaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextCategorySigmaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextCategorySigmasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCategorySigma = nextCategorySigmaRepository.saveAndFlush(nextCategorySigma);

        // Get all the nextCategorySigmaList where name equals to
        defaultNextCategorySigmaFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategorySigmasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCategorySigma = nextCategorySigmaRepository.saveAndFlush(nextCategorySigma);

        // Get all the nextCategorySigmaList where name in
        defaultNextCategorySigmaFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategorySigmasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCategorySigma = nextCategorySigmaRepository.saveAndFlush(nextCategorySigma);

        // Get all the nextCategorySigmaList where name is not null
        defaultNextCategorySigmaFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCategorySigmasByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategorySigma = nextCategorySigmaRepository.saveAndFlush(nextCategorySigma);

        // Get all the nextCategorySigmaList where name contains
        defaultNextCategorySigmaFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategorySigmasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategorySigma = nextCategorySigmaRepository.saveAndFlush(nextCategorySigma);

        // Get all the nextCategorySigmaList where name does not contain
        defaultNextCategorySigmaFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategorySigmasByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCategorySigma = nextCategorySigmaRepository.saveAndFlush(nextCategorySigma);

        // Get all the nextCategorySigmaList where description equals to
        defaultNextCategorySigmaFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNextCategorySigmasByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCategorySigma = nextCategorySigmaRepository.saveAndFlush(nextCategorySigma);

        // Get all the nextCategorySigmaList where description in
        defaultNextCategorySigmaFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllNextCategorySigmasByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCategorySigma = nextCategorySigmaRepository.saveAndFlush(nextCategorySigma);

        // Get all the nextCategorySigmaList where description is not null
        defaultNextCategorySigmaFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCategorySigmasByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategorySigma = nextCategorySigmaRepository.saveAndFlush(nextCategorySigma);

        // Get all the nextCategorySigmaList where description contains
        defaultNextCategorySigmaFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNextCategorySigmasByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategorySigma = nextCategorySigmaRepository.saveAndFlush(nextCategorySigma);

        // Get all the nextCategorySigmaList where description does not contain
        defaultNextCategorySigmaFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllNextCategorySigmasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextCategorySigmaRepository.saveAndFlush(nextCategorySigma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextCategorySigma.setTenant(tenant);
        nextCategorySigmaRepository.saveAndFlush(nextCategorySigma);
        Long tenantId = tenant.getId();
        // Get all the nextCategorySigmaList where tenant equals to tenantId
        defaultNextCategorySigmaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextCategorySigmaList where tenant equals to (tenantId + 1)
        defaultNextCategorySigmaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextCategorySigmaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextCategorySigmaShouldBeFound(shouldBeFound);
        defaultNextCategorySigmaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextCategorySigmaShouldBeFound(String filter) throws Exception {
        restNextCategorySigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCategorySigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restNextCategorySigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextCategorySigmaShouldNotBeFound(String filter) throws Exception {
        restNextCategorySigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextCategorySigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextCategorySigma() throws Exception {
        // Get the nextCategorySigma
        restNextCategorySigmaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextCategorySigma() throws Exception {
        // Initialize the database
        insertedNextCategorySigma = nextCategorySigmaRepository.saveAndFlush(nextCategorySigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCategorySigma
        NextCategorySigma updatedNextCategorySigma = nextCategorySigmaRepository.findById(nextCategorySigma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextCategorySigma are not directly saved in db
        em.detach(updatedNextCategorySigma);
        updatedNextCategorySigma.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        NextCategorySigmaDTO nextCategorySigmaDTO = nextCategorySigmaMapper.toDto(updatedNextCategorySigma);

        restNextCategorySigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextCategorySigmaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCategorySigmaDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextCategorySigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextCategorySigmaToMatchAllProperties(updatedNextCategorySigma);
    }

    @Test
    @Transactional
    void putNonExistingNextCategorySigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategorySigma.setId(longCount.incrementAndGet());

        // Create the NextCategorySigma
        NextCategorySigmaDTO nextCategorySigmaDTO = nextCategorySigmaMapper.toDto(nextCategorySigma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCategorySigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextCategorySigmaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCategorySigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategorySigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextCategorySigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategorySigma.setId(longCount.incrementAndGet());

        // Create the NextCategorySigma
        NextCategorySigmaDTO nextCategorySigmaDTO = nextCategorySigmaMapper.toDto(nextCategorySigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategorySigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCategorySigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategorySigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextCategorySigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategorySigma.setId(longCount.incrementAndGet());

        // Create the NextCategorySigma
        NextCategorySigmaDTO nextCategorySigmaDTO = nextCategorySigmaMapper.toDto(nextCategorySigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategorySigmaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategorySigmaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCategorySigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextCategorySigmaWithPatch() throws Exception {
        // Initialize the database
        insertedNextCategorySigma = nextCategorySigmaRepository.saveAndFlush(nextCategorySigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCategorySigma using partial update
        NextCategorySigma partialUpdatedNextCategorySigma = new NextCategorySigma();
        partialUpdatedNextCategorySigma.setId(nextCategorySigma.getId());

        restNextCategorySigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCategorySigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCategorySigma))
            )
            .andExpect(status().isOk());

        // Validate the NextCategorySigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCategorySigmaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextCategorySigma, nextCategorySigma),
            getPersistedNextCategorySigma(nextCategorySigma)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextCategorySigmaWithPatch() throws Exception {
        // Initialize the database
        insertedNextCategorySigma = nextCategorySigmaRepository.saveAndFlush(nextCategorySigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCategorySigma using partial update
        NextCategorySigma partialUpdatedNextCategorySigma = new NextCategorySigma();
        partialUpdatedNextCategorySigma.setId(nextCategorySigma.getId());

        partialUpdatedNextCategorySigma.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restNextCategorySigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCategorySigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCategorySigma))
            )
            .andExpect(status().isOk());

        // Validate the NextCategorySigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCategorySigmaUpdatableFieldsEquals(
            partialUpdatedNextCategorySigma,
            getPersistedNextCategorySigma(partialUpdatedNextCategorySigma)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextCategorySigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategorySigma.setId(longCount.incrementAndGet());

        // Create the NextCategorySigma
        NextCategorySigmaDTO nextCategorySigmaDTO = nextCategorySigmaMapper.toDto(nextCategorySigma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCategorySigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextCategorySigmaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCategorySigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategorySigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextCategorySigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategorySigma.setId(longCount.incrementAndGet());

        // Create the NextCategorySigma
        NextCategorySigmaDTO nextCategorySigmaDTO = nextCategorySigmaMapper.toDto(nextCategorySigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategorySigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCategorySigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategorySigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextCategorySigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategorySigma.setId(longCount.incrementAndGet());

        // Create the NextCategorySigma
        NextCategorySigmaDTO nextCategorySigmaDTO = nextCategorySigmaMapper.toDto(nextCategorySigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategorySigmaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextCategorySigmaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCategorySigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextCategorySigma() throws Exception {
        // Initialize the database
        insertedNextCategorySigma = nextCategorySigmaRepository.saveAndFlush(nextCategorySigma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextCategorySigma
        restNextCategorySigmaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextCategorySigma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextCategorySigmaRepository.count();
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

    protected NextCategorySigma getPersistedNextCategorySigma(NextCategorySigma nextCategorySigma) {
        return nextCategorySigmaRepository.findById(nextCategorySigma.getId()).orElseThrow();
    }

    protected void assertPersistedNextCategorySigmaToMatchAllProperties(NextCategorySigma expectedNextCategorySigma) {
        assertNextCategorySigmaAllPropertiesEquals(expectedNextCategorySigma, getPersistedNextCategorySigma(expectedNextCategorySigma));
    }

    protected void assertPersistedNextCategorySigmaToMatchUpdatableProperties(NextCategorySigma expectedNextCategorySigma) {
        assertNextCategorySigmaAllUpdatablePropertiesEquals(
            expectedNextCategorySigma,
            getPersistedNextCategorySigma(expectedNextCategorySigma)
        );
    }
}
