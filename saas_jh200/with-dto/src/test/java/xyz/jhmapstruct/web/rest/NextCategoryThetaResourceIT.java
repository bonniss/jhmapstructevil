package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextCategoryThetaAsserts.*;
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
import xyz.jhmapstruct.domain.NextCategoryTheta;
import xyz.jhmapstruct.repository.NextCategoryThetaRepository;
import xyz.jhmapstruct.service.dto.NextCategoryThetaDTO;
import xyz.jhmapstruct.service.mapper.NextCategoryThetaMapper;

/**
 * Integration tests for the {@link NextCategoryThetaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextCategoryThetaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/next-category-thetas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextCategoryThetaRepository nextCategoryThetaRepository;

    @Autowired
    private NextCategoryThetaMapper nextCategoryThetaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextCategoryThetaMockMvc;

    private NextCategoryTheta nextCategoryTheta;

    private NextCategoryTheta insertedNextCategoryTheta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextCategoryTheta createEntity() {
        return new NextCategoryTheta().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextCategoryTheta createUpdatedEntity() {
        return new NextCategoryTheta().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        nextCategoryTheta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextCategoryTheta != null) {
            nextCategoryThetaRepository.delete(insertedNextCategoryTheta);
            insertedNextCategoryTheta = null;
        }
    }

    @Test
    @Transactional
    void createNextCategoryTheta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextCategoryTheta
        NextCategoryThetaDTO nextCategoryThetaDTO = nextCategoryThetaMapper.toDto(nextCategoryTheta);
        var returnedNextCategoryThetaDTO = om.readValue(
            restNextCategoryThetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategoryThetaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextCategoryThetaDTO.class
        );

        // Validate the NextCategoryTheta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextCategoryTheta = nextCategoryThetaMapper.toEntity(returnedNextCategoryThetaDTO);
        assertNextCategoryThetaUpdatableFieldsEquals(returnedNextCategoryTheta, getPersistedNextCategoryTheta(returnedNextCategoryTheta));

        insertedNextCategoryTheta = returnedNextCategoryTheta;
    }

    @Test
    @Transactional
    void createNextCategoryThetaWithExistingId() throws Exception {
        // Create the NextCategoryTheta with an existing ID
        nextCategoryTheta.setId(1L);
        NextCategoryThetaDTO nextCategoryThetaDTO = nextCategoryThetaMapper.toDto(nextCategoryTheta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextCategoryThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategoryThetaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCategoryTheta.setName(null);

        // Create the NextCategoryTheta, which fails.
        NextCategoryThetaDTO nextCategoryThetaDTO = nextCategoryThetaMapper.toDto(nextCategoryTheta);

        restNextCategoryThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategoryThetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextCategoryThetas() throws Exception {
        // Initialize the database
        insertedNextCategoryTheta = nextCategoryThetaRepository.saveAndFlush(nextCategoryTheta);

        // Get all the nextCategoryThetaList
        restNextCategoryThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCategoryTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getNextCategoryTheta() throws Exception {
        // Initialize the database
        insertedNextCategoryTheta = nextCategoryThetaRepository.saveAndFlush(nextCategoryTheta);

        // Get the nextCategoryTheta
        restNextCategoryThetaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextCategoryTheta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextCategoryTheta.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNextCategoryThetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextCategoryTheta = nextCategoryThetaRepository.saveAndFlush(nextCategoryTheta);

        Long id = nextCategoryTheta.getId();

        defaultNextCategoryThetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextCategoryThetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextCategoryThetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextCategoryThetasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryTheta = nextCategoryThetaRepository.saveAndFlush(nextCategoryTheta);

        // Get all the nextCategoryThetaList where name equals to
        defaultNextCategoryThetaFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategoryThetasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCategoryTheta = nextCategoryThetaRepository.saveAndFlush(nextCategoryTheta);

        // Get all the nextCategoryThetaList where name in
        defaultNextCategoryThetaFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategoryThetasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCategoryTheta = nextCategoryThetaRepository.saveAndFlush(nextCategoryTheta);

        // Get all the nextCategoryThetaList where name is not null
        defaultNextCategoryThetaFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCategoryThetasByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryTheta = nextCategoryThetaRepository.saveAndFlush(nextCategoryTheta);

        // Get all the nextCategoryThetaList where name contains
        defaultNextCategoryThetaFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategoryThetasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryTheta = nextCategoryThetaRepository.saveAndFlush(nextCategoryTheta);

        // Get all the nextCategoryThetaList where name does not contain
        defaultNextCategoryThetaFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategoryThetasByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryTheta = nextCategoryThetaRepository.saveAndFlush(nextCategoryTheta);

        // Get all the nextCategoryThetaList where description equals to
        defaultNextCategoryThetaFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNextCategoryThetasByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCategoryTheta = nextCategoryThetaRepository.saveAndFlush(nextCategoryTheta);

        // Get all the nextCategoryThetaList where description in
        defaultNextCategoryThetaFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllNextCategoryThetasByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCategoryTheta = nextCategoryThetaRepository.saveAndFlush(nextCategoryTheta);

        // Get all the nextCategoryThetaList where description is not null
        defaultNextCategoryThetaFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCategoryThetasByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryTheta = nextCategoryThetaRepository.saveAndFlush(nextCategoryTheta);

        // Get all the nextCategoryThetaList where description contains
        defaultNextCategoryThetaFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNextCategoryThetasByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryTheta = nextCategoryThetaRepository.saveAndFlush(nextCategoryTheta);

        // Get all the nextCategoryThetaList where description does not contain
        defaultNextCategoryThetaFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllNextCategoryThetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextCategoryThetaRepository.saveAndFlush(nextCategoryTheta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextCategoryTheta.setTenant(tenant);
        nextCategoryThetaRepository.saveAndFlush(nextCategoryTheta);
        Long tenantId = tenant.getId();
        // Get all the nextCategoryThetaList where tenant equals to tenantId
        defaultNextCategoryThetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextCategoryThetaList where tenant equals to (tenantId + 1)
        defaultNextCategoryThetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextCategoryThetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextCategoryThetaShouldBeFound(shouldBeFound);
        defaultNextCategoryThetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextCategoryThetaShouldBeFound(String filter) throws Exception {
        restNextCategoryThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCategoryTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restNextCategoryThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextCategoryThetaShouldNotBeFound(String filter) throws Exception {
        restNextCategoryThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextCategoryThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextCategoryTheta() throws Exception {
        // Get the nextCategoryTheta
        restNextCategoryThetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextCategoryTheta() throws Exception {
        // Initialize the database
        insertedNextCategoryTheta = nextCategoryThetaRepository.saveAndFlush(nextCategoryTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCategoryTheta
        NextCategoryTheta updatedNextCategoryTheta = nextCategoryThetaRepository.findById(nextCategoryTheta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextCategoryTheta are not directly saved in db
        em.detach(updatedNextCategoryTheta);
        updatedNextCategoryTheta.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        NextCategoryThetaDTO nextCategoryThetaDTO = nextCategoryThetaMapper.toDto(updatedNextCategoryTheta);

        restNextCategoryThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextCategoryThetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCategoryThetaDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextCategoryTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextCategoryThetaToMatchAllProperties(updatedNextCategoryTheta);
    }

    @Test
    @Transactional
    void putNonExistingNextCategoryTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryTheta.setId(longCount.incrementAndGet());

        // Create the NextCategoryTheta
        NextCategoryThetaDTO nextCategoryThetaDTO = nextCategoryThetaMapper.toDto(nextCategoryTheta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCategoryThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextCategoryThetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCategoryThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextCategoryTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryTheta.setId(longCount.incrementAndGet());

        // Create the NextCategoryTheta
        NextCategoryThetaDTO nextCategoryThetaDTO = nextCategoryThetaMapper.toDto(nextCategoryTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategoryThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCategoryThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextCategoryTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryTheta.setId(longCount.incrementAndGet());

        // Create the NextCategoryTheta
        NextCategoryThetaDTO nextCategoryThetaDTO = nextCategoryThetaMapper.toDto(nextCategoryTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategoryThetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategoryThetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCategoryTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextCategoryThetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextCategoryTheta = nextCategoryThetaRepository.saveAndFlush(nextCategoryTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCategoryTheta using partial update
        NextCategoryTheta partialUpdatedNextCategoryTheta = new NextCategoryTheta();
        partialUpdatedNextCategoryTheta.setId(nextCategoryTheta.getId());

        partialUpdatedNextCategoryTheta.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restNextCategoryThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCategoryTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCategoryTheta))
            )
            .andExpect(status().isOk());

        // Validate the NextCategoryTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCategoryThetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextCategoryTheta, nextCategoryTheta),
            getPersistedNextCategoryTheta(nextCategoryTheta)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextCategoryThetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextCategoryTheta = nextCategoryThetaRepository.saveAndFlush(nextCategoryTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCategoryTheta using partial update
        NextCategoryTheta partialUpdatedNextCategoryTheta = new NextCategoryTheta();
        partialUpdatedNextCategoryTheta.setId(nextCategoryTheta.getId());

        partialUpdatedNextCategoryTheta.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restNextCategoryThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCategoryTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCategoryTheta))
            )
            .andExpect(status().isOk());

        // Validate the NextCategoryTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCategoryThetaUpdatableFieldsEquals(
            partialUpdatedNextCategoryTheta,
            getPersistedNextCategoryTheta(partialUpdatedNextCategoryTheta)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextCategoryTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryTheta.setId(longCount.incrementAndGet());

        // Create the NextCategoryTheta
        NextCategoryThetaDTO nextCategoryThetaDTO = nextCategoryThetaMapper.toDto(nextCategoryTheta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCategoryThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextCategoryThetaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCategoryThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextCategoryTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryTheta.setId(longCount.incrementAndGet());

        // Create the NextCategoryTheta
        NextCategoryThetaDTO nextCategoryThetaDTO = nextCategoryThetaMapper.toDto(nextCategoryTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategoryThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCategoryThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextCategoryTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryTheta.setId(longCount.incrementAndGet());

        // Create the NextCategoryTheta
        NextCategoryThetaDTO nextCategoryThetaDTO = nextCategoryThetaMapper.toDto(nextCategoryTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategoryThetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextCategoryThetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCategoryTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextCategoryTheta() throws Exception {
        // Initialize the database
        insertedNextCategoryTheta = nextCategoryThetaRepository.saveAndFlush(nextCategoryTheta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextCategoryTheta
        restNextCategoryThetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextCategoryTheta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextCategoryThetaRepository.count();
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

    protected NextCategoryTheta getPersistedNextCategoryTheta(NextCategoryTheta nextCategoryTheta) {
        return nextCategoryThetaRepository.findById(nextCategoryTheta.getId()).orElseThrow();
    }

    protected void assertPersistedNextCategoryThetaToMatchAllProperties(NextCategoryTheta expectedNextCategoryTheta) {
        assertNextCategoryThetaAllPropertiesEquals(expectedNextCategoryTheta, getPersistedNextCategoryTheta(expectedNextCategoryTheta));
    }

    protected void assertPersistedNextCategoryThetaToMatchUpdatableProperties(NextCategoryTheta expectedNextCategoryTheta) {
        assertNextCategoryThetaAllUpdatablePropertiesEquals(
            expectedNextCategoryTheta,
            getPersistedNextCategoryTheta(expectedNextCategoryTheta)
        );
    }
}
