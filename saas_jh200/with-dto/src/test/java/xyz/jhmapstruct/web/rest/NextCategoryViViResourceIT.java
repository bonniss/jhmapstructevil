package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextCategoryViViAsserts.*;
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
import xyz.jhmapstruct.domain.NextCategoryViVi;
import xyz.jhmapstruct.repository.NextCategoryViViRepository;
import xyz.jhmapstruct.service.dto.NextCategoryViViDTO;
import xyz.jhmapstruct.service.mapper.NextCategoryViViMapper;

/**
 * Integration tests for the {@link NextCategoryViViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextCategoryViViResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/next-category-vi-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextCategoryViViRepository nextCategoryViViRepository;

    @Autowired
    private NextCategoryViViMapper nextCategoryViViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextCategoryViViMockMvc;

    private NextCategoryViVi nextCategoryViVi;

    private NextCategoryViVi insertedNextCategoryViVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextCategoryViVi createEntity() {
        return new NextCategoryViVi().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextCategoryViVi createUpdatedEntity() {
        return new NextCategoryViVi().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        nextCategoryViVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextCategoryViVi != null) {
            nextCategoryViViRepository.delete(insertedNextCategoryViVi);
            insertedNextCategoryViVi = null;
        }
    }

    @Test
    @Transactional
    void createNextCategoryViVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextCategoryViVi
        NextCategoryViViDTO nextCategoryViViDTO = nextCategoryViViMapper.toDto(nextCategoryViVi);
        var returnedNextCategoryViViDTO = om.readValue(
            restNextCategoryViViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategoryViViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextCategoryViViDTO.class
        );

        // Validate the NextCategoryViVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextCategoryViVi = nextCategoryViViMapper.toEntity(returnedNextCategoryViViDTO);
        assertNextCategoryViViUpdatableFieldsEquals(returnedNextCategoryViVi, getPersistedNextCategoryViVi(returnedNextCategoryViVi));

        insertedNextCategoryViVi = returnedNextCategoryViVi;
    }

    @Test
    @Transactional
    void createNextCategoryViViWithExistingId() throws Exception {
        // Create the NextCategoryViVi with an existing ID
        nextCategoryViVi.setId(1L);
        NextCategoryViViDTO nextCategoryViViDTO = nextCategoryViViMapper.toDto(nextCategoryViVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextCategoryViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategoryViViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCategoryViVi.setName(null);

        // Create the NextCategoryViVi, which fails.
        NextCategoryViViDTO nextCategoryViViDTO = nextCategoryViViMapper.toDto(nextCategoryViVi);

        restNextCategoryViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategoryViViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextCategoryViVis() throws Exception {
        // Initialize the database
        insertedNextCategoryViVi = nextCategoryViViRepository.saveAndFlush(nextCategoryViVi);

        // Get all the nextCategoryViViList
        restNextCategoryViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCategoryViVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getNextCategoryViVi() throws Exception {
        // Initialize the database
        insertedNextCategoryViVi = nextCategoryViViRepository.saveAndFlush(nextCategoryViVi);

        // Get the nextCategoryViVi
        restNextCategoryViViMockMvc
            .perform(get(ENTITY_API_URL_ID, nextCategoryViVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextCategoryViVi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNextCategoryViVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextCategoryViVi = nextCategoryViViRepository.saveAndFlush(nextCategoryViVi);

        Long id = nextCategoryViVi.getId();

        defaultNextCategoryViViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextCategoryViViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextCategoryViViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextCategoryViVisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryViVi = nextCategoryViViRepository.saveAndFlush(nextCategoryViVi);

        // Get all the nextCategoryViViList where name equals to
        defaultNextCategoryViViFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategoryViVisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCategoryViVi = nextCategoryViViRepository.saveAndFlush(nextCategoryViVi);

        // Get all the nextCategoryViViList where name in
        defaultNextCategoryViViFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategoryViVisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCategoryViVi = nextCategoryViViRepository.saveAndFlush(nextCategoryViVi);

        // Get all the nextCategoryViViList where name is not null
        defaultNextCategoryViViFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCategoryViVisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryViVi = nextCategoryViViRepository.saveAndFlush(nextCategoryViVi);

        // Get all the nextCategoryViViList where name contains
        defaultNextCategoryViViFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategoryViVisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryViVi = nextCategoryViViRepository.saveAndFlush(nextCategoryViVi);

        // Get all the nextCategoryViViList where name does not contain
        defaultNextCategoryViViFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategoryViVisByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryViVi = nextCategoryViViRepository.saveAndFlush(nextCategoryViVi);

        // Get all the nextCategoryViViList where description equals to
        defaultNextCategoryViViFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNextCategoryViVisByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCategoryViVi = nextCategoryViViRepository.saveAndFlush(nextCategoryViVi);

        // Get all the nextCategoryViViList where description in
        defaultNextCategoryViViFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllNextCategoryViVisByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCategoryViVi = nextCategoryViViRepository.saveAndFlush(nextCategoryViVi);

        // Get all the nextCategoryViViList where description is not null
        defaultNextCategoryViViFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCategoryViVisByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryViVi = nextCategoryViViRepository.saveAndFlush(nextCategoryViVi);

        // Get all the nextCategoryViViList where description contains
        defaultNextCategoryViViFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNextCategoryViVisByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryViVi = nextCategoryViViRepository.saveAndFlush(nextCategoryViVi);

        // Get all the nextCategoryViViList where description does not contain
        defaultNextCategoryViViFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllNextCategoryViVisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextCategoryViViRepository.saveAndFlush(nextCategoryViVi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextCategoryViVi.setTenant(tenant);
        nextCategoryViViRepository.saveAndFlush(nextCategoryViVi);
        Long tenantId = tenant.getId();
        // Get all the nextCategoryViViList where tenant equals to tenantId
        defaultNextCategoryViViShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextCategoryViViList where tenant equals to (tenantId + 1)
        defaultNextCategoryViViShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextCategoryViViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextCategoryViViShouldBeFound(shouldBeFound);
        defaultNextCategoryViViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextCategoryViViShouldBeFound(String filter) throws Exception {
        restNextCategoryViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCategoryViVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restNextCategoryViViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextCategoryViViShouldNotBeFound(String filter) throws Exception {
        restNextCategoryViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextCategoryViViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextCategoryViVi() throws Exception {
        // Get the nextCategoryViVi
        restNextCategoryViViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextCategoryViVi() throws Exception {
        // Initialize the database
        insertedNextCategoryViVi = nextCategoryViViRepository.saveAndFlush(nextCategoryViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCategoryViVi
        NextCategoryViVi updatedNextCategoryViVi = nextCategoryViViRepository.findById(nextCategoryViVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextCategoryViVi are not directly saved in db
        em.detach(updatedNextCategoryViVi);
        updatedNextCategoryViVi.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        NextCategoryViViDTO nextCategoryViViDTO = nextCategoryViViMapper.toDto(updatedNextCategoryViVi);

        restNextCategoryViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextCategoryViViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCategoryViViDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextCategoryViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextCategoryViViToMatchAllProperties(updatedNextCategoryViVi);
    }

    @Test
    @Transactional
    void putNonExistingNextCategoryViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryViVi.setId(longCount.incrementAndGet());

        // Create the NextCategoryViVi
        NextCategoryViViDTO nextCategoryViViDTO = nextCategoryViViMapper.toDto(nextCategoryViVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCategoryViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextCategoryViViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCategoryViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextCategoryViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryViVi.setId(longCount.incrementAndGet());

        // Create the NextCategoryViVi
        NextCategoryViViDTO nextCategoryViViDTO = nextCategoryViViMapper.toDto(nextCategoryViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategoryViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCategoryViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextCategoryViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryViVi.setId(longCount.incrementAndGet());

        // Create the NextCategoryViVi
        NextCategoryViViDTO nextCategoryViViDTO = nextCategoryViViMapper.toDto(nextCategoryViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategoryViViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategoryViViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCategoryViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextCategoryViViWithPatch() throws Exception {
        // Initialize the database
        insertedNextCategoryViVi = nextCategoryViViRepository.saveAndFlush(nextCategoryViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCategoryViVi using partial update
        NextCategoryViVi partialUpdatedNextCategoryViVi = new NextCategoryViVi();
        partialUpdatedNextCategoryViVi.setId(nextCategoryViVi.getId());

        restNextCategoryViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCategoryViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCategoryViVi))
            )
            .andExpect(status().isOk());

        // Validate the NextCategoryViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCategoryViViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextCategoryViVi, nextCategoryViVi),
            getPersistedNextCategoryViVi(nextCategoryViVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextCategoryViViWithPatch() throws Exception {
        // Initialize the database
        insertedNextCategoryViVi = nextCategoryViViRepository.saveAndFlush(nextCategoryViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCategoryViVi using partial update
        NextCategoryViVi partialUpdatedNextCategoryViVi = new NextCategoryViVi();
        partialUpdatedNextCategoryViVi.setId(nextCategoryViVi.getId());

        partialUpdatedNextCategoryViVi.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restNextCategoryViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCategoryViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCategoryViVi))
            )
            .andExpect(status().isOk());

        // Validate the NextCategoryViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCategoryViViUpdatableFieldsEquals(
            partialUpdatedNextCategoryViVi,
            getPersistedNextCategoryViVi(partialUpdatedNextCategoryViVi)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextCategoryViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryViVi.setId(longCount.incrementAndGet());

        // Create the NextCategoryViVi
        NextCategoryViViDTO nextCategoryViViDTO = nextCategoryViViMapper.toDto(nextCategoryViVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCategoryViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextCategoryViViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCategoryViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextCategoryViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryViVi.setId(longCount.incrementAndGet());

        // Create the NextCategoryViVi
        NextCategoryViViDTO nextCategoryViViDTO = nextCategoryViViMapper.toDto(nextCategoryViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategoryViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCategoryViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextCategoryViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryViVi.setId(longCount.incrementAndGet());

        // Create the NextCategoryViVi
        NextCategoryViViDTO nextCategoryViViDTO = nextCategoryViViMapper.toDto(nextCategoryViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategoryViViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextCategoryViViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCategoryViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextCategoryViVi() throws Exception {
        // Initialize the database
        insertedNextCategoryViVi = nextCategoryViViRepository.saveAndFlush(nextCategoryViVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextCategoryViVi
        restNextCategoryViViMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextCategoryViVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextCategoryViViRepository.count();
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

    protected NextCategoryViVi getPersistedNextCategoryViVi(NextCategoryViVi nextCategoryViVi) {
        return nextCategoryViViRepository.findById(nextCategoryViVi.getId()).orElseThrow();
    }

    protected void assertPersistedNextCategoryViViToMatchAllProperties(NextCategoryViVi expectedNextCategoryViVi) {
        assertNextCategoryViViAllPropertiesEquals(expectedNextCategoryViVi, getPersistedNextCategoryViVi(expectedNextCategoryViVi));
    }

    protected void assertPersistedNextCategoryViViToMatchUpdatableProperties(NextCategoryViVi expectedNextCategoryViVi) {
        assertNextCategoryViViAllUpdatablePropertiesEquals(
            expectedNextCategoryViVi,
            getPersistedNextCategoryViVi(expectedNextCategoryViVi)
        );
    }
}
