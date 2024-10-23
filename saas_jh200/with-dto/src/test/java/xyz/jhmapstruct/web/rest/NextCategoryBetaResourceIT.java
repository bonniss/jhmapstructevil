package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextCategoryBetaAsserts.*;
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
import xyz.jhmapstruct.domain.NextCategoryBeta;
import xyz.jhmapstruct.repository.NextCategoryBetaRepository;
import xyz.jhmapstruct.service.dto.NextCategoryBetaDTO;
import xyz.jhmapstruct.service.mapper.NextCategoryBetaMapper;

/**
 * Integration tests for the {@link NextCategoryBetaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextCategoryBetaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/next-category-betas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextCategoryBetaRepository nextCategoryBetaRepository;

    @Autowired
    private NextCategoryBetaMapper nextCategoryBetaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextCategoryBetaMockMvc;

    private NextCategoryBeta nextCategoryBeta;

    private NextCategoryBeta insertedNextCategoryBeta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextCategoryBeta createEntity() {
        return new NextCategoryBeta().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextCategoryBeta createUpdatedEntity() {
        return new NextCategoryBeta().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        nextCategoryBeta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextCategoryBeta != null) {
            nextCategoryBetaRepository.delete(insertedNextCategoryBeta);
            insertedNextCategoryBeta = null;
        }
    }

    @Test
    @Transactional
    void createNextCategoryBeta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextCategoryBeta
        NextCategoryBetaDTO nextCategoryBetaDTO = nextCategoryBetaMapper.toDto(nextCategoryBeta);
        var returnedNextCategoryBetaDTO = om.readValue(
            restNextCategoryBetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategoryBetaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextCategoryBetaDTO.class
        );

        // Validate the NextCategoryBeta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextCategoryBeta = nextCategoryBetaMapper.toEntity(returnedNextCategoryBetaDTO);
        assertNextCategoryBetaUpdatableFieldsEquals(returnedNextCategoryBeta, getPersistedNextCategoryBeta(returnedNextCategoryBeta));

        insertedNextCategoryBeta = returnedNextCategoryBeta;
    }

    @Test
    @Transactional
    void createNextCategoryBetaWithExistingId() throws Exception {
        // Create the NextCategoryBeta with an existing ID
        nextCategoryBeta.setId(1L);
        NextCategoryBetaDTO nextCategoryBetaDTO = nextCategoryBetaMapper.toDto(nextCategoryBeta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextCategoryBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategoryBetaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCategoryBeta.setName(null);

        // Create the NextCategoryBeta, which fails.
        NextCategoryBetaDTO nextCategoryBetaDTO = nextCategoryBetaMapper.toDto(nextCategoryBeta);

        restNextCategoryBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategoryBetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextCategoryBetas() throws Exception {
        // Initialize the database
        insertedNextCategoryBeta = nextCategoryBetaRepository.saveAndFlush(nextCategoryBeta);

        // Get all the nextCategoryBetaList
        restNextCategoryBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCategoryBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getNextCategoryBeta() throws Exception {
        // Initialize the database
        insertedNextCategoryBeta = nextCategoryBetaRepository.saveAndFlush(nextCategoryBeta);

        // Get the nextCategoryBeta
        restNextCategoryBetaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextCategoryBeta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextCategoryBeta.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNextCategoryBetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextCategoryBeta = nextCategoryBetaRepository.saveAndFlush(nextCategoryBeta);

        Long id = nextCategoryBeta.getId();

        defaultNextCategoryBetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextCategoryBetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextCategoryBetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextCategoryBetasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryBeta = nextCategoryBetaRepository.saveAndFlush(nextCategoryBeta);

        // Get all the nextCategoryBetaList where name equals to
        defaultNextCategoryBetaFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategoryBetasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCategoryBeta = nextCategoryBetaRepository.saveAndFlush(nextCategoryBeta);

        // Get all the nextCategoryBetaList where name in
        defaultNextCategoryBetaFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategoryBetasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCategoryBeta = nextCategoryBetaRepository.saveAndFlush(nextCategoryBeta);

        // Get all the nextCategoryBetaList where name is not null
        defaultNextCategoryBetaFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCategoryBetasByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryBeta = nextCategoryBetaRepository.saveAndFlush(nextCategoryBeta);

        // Get all the nextCategoryBetaList where name contains
        defaultNextCategoryBetaFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategoryBetasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryBeta = nextCategoryBetaRepository.saveAndFlush(nextCategoryBeta);

        // Get all the nextCategoryBetaList where name does not contain
        defaultNextCategoryBetaFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategoryBetasByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryBeta = nextCategoryBetaRepository.saveAndFlush(nextCategoryBeta);

        // Get all the nextCategoryBetaList where description equals to
        defaultNextCategoryBetaFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNextCategoryBetasByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCategoryBeta = nextCategoryBetaRepository.saveAndFlush(nextCategoryBeta);

        // Get all the nextCategoryBetaList where description in
        defaultNextCategoryBetaFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllNextCategoryBetasByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCategoryBeta = nextCategoryBetaRepository.saveAndFlush(nextCategoryBeta);

        // Get all the nextCategoryBetaList where description is not null
        defaultNextCategoryBetaFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCategoryBetasByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryBeta = nextCategoryBetaRepository.saveAndFlush(nextCategoryBeta);

        // Get all the nextCategoryBetaList where description contains
        defaultNextCategoryBetaFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNextCategoryBetasByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryBeta = nextCategoryBetaRepository.saveAndFlush(nextCategoryBeta);

        // Get all the nextCategoryBetaList where description does not contain
        defaultNextCategoryBetaFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllNextCategoryBetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextCategoryBetaRepository.saveAndFlush(nextCategoryBeta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextCategoryBeta.setTenant(tenant);
        nextCategoryBetaRepository.saveAndFlush(nextCategoryBeta);
        Long tenantId = tenant.getId();
        // Get all the nextCategoryBetaList where tenant equals to tenantId
        defaultNextCategoryBetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextCategoryBetaList where tenant equals to (tenantId + 1)
        defaultNextCategoryBetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextCategoryBetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextCategoryBetaShouldBeFound(shouldBeFound);
        defaultNextCategoryBetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextCategoryBetaShouldBeFound(String filter) throws Exception {
        restNextCategoryBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCategoryBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restNextCategoryBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextCategoryBetaShouldNotBeFound(String filter) throws Exception {
        restNextCategoryBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextCategoryBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextCategoryBeta() throws Exception {
        // Get the nextCategoryBeta
        restNextCategoryBetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextCategoryBeta() throws Exception {
        // Initialize the database
        insertedNextCategoryBeta = nextCategoryBetaRepository.saveAndFlush(nextCategoryBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCategoryBeta
        NextCategoryBeta updatedNextCategoryBeta = nextCategoryBetaRepository.findById(nextCategoryBeta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextCategoryBeta are not directly saved in db
        em.detach(updatedNextCategoryBeta);
        updatedNextCategoryBeta.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        NextCategoryBetaDTO nextCategoryBetaDTO = nextCategoryBetaMapper.toDto(updatedNextCategoryBeta);

        restNextCategoryBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextCategoryBetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCategoryBetaDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextCategoryBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextCategoryBetaToMatchAllProperties(updatedNextCategoryBeta);
    }

    @Test
    @Transactional
    void putNonExistingNextCategoryBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryBeta.setId(longCount.incrementAndGet());

        // Create the NextCategoryBeta
        NextCategoryBetaDTO nextCategoryBetaDTO = nextCategoryBetaMapper.toDto(nextCategoryBeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCategoryBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextCategoryBetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCategoryBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextCategoryBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryBeta.setId(longCount.incrementAndGet());

        // Create the NextCategoryBeta
        NextCategoryBetaDTO nextCategoryBetaDTO = nextCategoryBetaMapper.toDto(nextCategoryBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategoryBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCategoryBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextCategoryBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryBeta.setId(longCount.incrementAndGet());

        // Create the NextCategoryBeta
        NextCategoryBetaDTO nextCategoryBetaDTO = nextCategoryBetaMapper.toDto(nextCategoryBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategoryBetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategoryBetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCategoryBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextCategoryBetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextCategoryBeta = nextCategoryBetaRepository.saveAndFlush(nextCategoryBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCategoryBeta using partial update
        NextCategoryBeta partialUpdatedNextCategoryBeta = new NextCategoryBeta();
        partialUpdatedNextCategoryBeta.setId(nextCategoryBeta.getId());

        partialUpdatedNextCategoryBeta.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restNextCategoryBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCategoryBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCategoryBeta))
            )
            .andExpect(status().isOk());

        // Validate the NextCategoryBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCategoryBetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextCategoryBeta, nextCategoryBeta),
            getPersistedNextCategoryBeta(nextCategoryBeta)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextCategoryBetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextCategoryBeta = nextCategoryBetaRepository.saveAndFlush(nextCategoryBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCategoryBeta using partial update
        NextCategoryBeta partialUpdatedNextCategoryBeta = new NextCategoryBeta();
        partialUpdatedNextCategoryBeta.setId(nextCategoryBeta.getId());

        partialUpdatedNextCategoryBeta.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restNextCategoryBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCategoryBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCategoryBeta))
            )
            .andExpect(status().isOk());

        // Validate the NextCategoryBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCategoryBetaUpdatableFieldsEquals(
            partialUpdatedNextCategoryBeta,
            getPersistedNextCategoryBeta(partialUpdatedNextCategoryBeta)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextCategoryBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryBeta.setId(longCount.incrementAndGet());

        // Create the NextCategoryBeta
        NextCategoryBetaDTO nextCategoryBetaDTO = nextCategoryBetaMapper.toDto(nextCategoryBeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCategoryBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextCategoryBetaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCategoryBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextCategoryBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryBeta.setId(longCount.incrementAndGet());

        // Create the NextCategoryBeta
        NextCategoryBetaDTO nextCategoryBetaDTO = nextCategoryBetaMapper.toDto(nextCategoryBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategoryBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCategoryBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextCategoryBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryBeta.setId(longCount.incrementAndGet());

        // Create the NextCategoryBeta
        NextCategoryBetaDTO nextCategoryBetaDTO = nextCategoryBetaMapper.toDto(nextCategoryBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategoryBetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextCategoryBetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCategoryBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextCategoryBeta() throws Exception {
        // Initialize the database
        insertedNextCategoryBeta = nextCategoryBetaRepository.saveAndFlush(nextCategoryBeta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextCategoryBeta
        restNextCategoryBetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextCategoryBeta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextCategoryBetaRepository.count();
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

    protected NextCategoryBeta getPersistedNextCategoryBeta(NextCategoryBeta nextCategoryBeta) {
        return nextCategoryBetaRepository.findById(nextCategoryBeta.getId()).orElseThrow();
    }

    protected void assertPersistedNextCategoryBetaToMatchAllProperties(NextCategoryBeta expectedNextCategoryBeta) {
        assertNextCategoryBetaAllPropertiesEquals(expectedNextCategoryBeta, getPersistedNextCategoryBeta(expectedNextCategoryBeta));
    }

    protected void assertPersistedNextCategoryBetaToMatchUpdatableProperties(NextCategoryBeta expectedNextCategoryBeta) {
        assertNextCategoryBetaAllUpdatablePropertiesEquals(
            expectedNextCategoryBeta,
            getPersistedNextCategoryBeta(expectedNextCategoryBeta)
        );
    }
}
