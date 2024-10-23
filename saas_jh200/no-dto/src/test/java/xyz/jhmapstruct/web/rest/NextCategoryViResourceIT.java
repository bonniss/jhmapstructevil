package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextCategoryViAsserts.*;
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
import xyz.jhmapstruct.domain.NextCategoryVi;
import xyz.jhmapstruct.repository.NextCategoryViRepository;

/**
 * Integration tests for the {@link NextCategoryViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextCategoryViResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/next-category-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextCategoryViRepository nextCategoryViRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextCategoryViMockMvc;

    private NextCategoryVi nextCategoryVi;

    private NextCategoryVi insertedNextCategoryVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextCategoryVi createEntity() {
        return new NextCategoryVi().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextCategoryVi createUpdatedEntity() {
        return new NextCategoryVi().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        nextCategoryVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextCategoryVi != null) {
            nextCategoryViRepository.delete(insertedNextCategoryVi);
            insertedNextCategoryVi = null;
        }
    }

    @Test
    @Transactional
    void createNextCategoryVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextCategoryVi
        var returnedNextCategoryVi = om.readValue(
            restNextCategoryViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategoryVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextCategoryVi.class
        );

        // Validate the NextCategoryVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextCategoryViUpdatableFieldsEquals(returnedNextCategoryVi, getPersistedNextCategoryVi(returnedNextCategoryVi));

        insertedNextCategoryVi = returnedNextCategoryVi;
    }

    @Test
    @Transactional
    void createNextCategoryViWithExistingId() throws Exception {
        // Create the NextCategoryVi with an existing ID
        nextCategoryVi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextCategoryViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategoryVi)))
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCategoryVi.setName(null);

        // Create the NextCategoryVi, which fails.

        restNextCategoryViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategoryVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextCategoryVis() throws Exception {
        // Initialize the database
        insertedNextCategoryVi = nextCategoryViRepository.saveAndFlush(nextCategoryVi);

        // Get all the nextCategoryViList
        restNextCategoryViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCategoryVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getNextCategoryVi() throws Exception {
        // Initialize the database
        insertedNextCategoryVi = nextCategoryViRepository.saveAndFlush(nextCategoryVi);

        // Get the nextCategoryVi
        restNextCategoryViMockMvc
            .perform(get(ENTITY_API_URL_ID, nextCategoryVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextCategoryVi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNextCategoryVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextCategoryVi = nextCategoryViRepository.saveAndFlush(nextCategoryVi);

        Long id = nextCategoryVi.getId();

        defaultNextCategoryViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextCategoryViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextCategoryViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextCategoryVisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryVi = nextCategoryViRepository.saveAndFlush(nextCategoryVi);

        // Get all the nextCategoryViList where name equals to
        defaultNextCategoryViFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategoryVisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCategoryVi = nextCategoryViRepository.saveAndFlush(nextCategoryVi);

        // Get all the nextCategoryViList where name in
        defaultNextCategoryViFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategoryVisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCategoryVi = nextCategoryViRepository.saveAndFlush(nextCategoryVi);

        // Get all the nextCategoryViList where name is not null
        defaultNextCategoryViFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCategoryVisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryVi = nextCategoryViRepository.saveAndFlush(nextCategoryVi);

        // Get all the nextCategoryViList where name contains
        defaultNextCategoryViFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategoryVisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryVi = nextCategoryViRepository.saveAndFlush(nextCategoryVi);

        // Get all the nextCategoryViList where name does not contain
        defaultNextCategoryViFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategoryVisByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryVi = nextCategoryViRepository.saveAndFlush(nextCategoryVi);

        // Get all the nextCategoryViList where description equals to
        defaultNextCategoryViFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNextCategoryVisByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCategoryVi = nextCategoryViRepository.saveAndFlush(nextCategoryVi);

        // Get all the nextCategoryViList where description in
        defaultNextCategoryViFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllNextCategoryVisByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCategoryVi = nextCategoryViRepository.saveAndFlush(nextCategoryVi);

        // Get all the nextCategoryViList where description is not null
        defaultNextCategoryViFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCategoryVisByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryVi = nextCategoryViRepository.saveAndFlush(nextCategoryVi);

        // Get all the nextCategoryViList where description contains
        defaultNextCategoryViFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNextCategoryVisByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryVi = nextCategoryViRepository.saveAndFlush(nextCategoryVi);

        // Get all the nextCategoryViList where description does not contain
        defaultNextCategoryViFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllNextCategoryVisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextCategoryViRepository.saveAndFlush(nextCategoryVi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextCategoryVi.setTenant(tenant);
        nextCategoryViRepository.saveAndFlush(nextCategoryVi);
        Long tenantId = tenant.getId();
        // Get all the nextCategoryViList where tenant equals to tenantId
        defaultNextCategoryViShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextCategoryViList where tenant equals to (tenantId + 1)
        defaultNextCategoryViShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextCategoryViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextCategoryViShouldBeFound(shouldBeFound);
        defaultNextCategoryViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextCategoryViShouldBeFound(String filter) throws Exception {
        restNextCategoryViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCategoryVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restNextCategoryViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextCategoryViShouldNotBeFound(String filter) throws Exception {
        restNextCategoryViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextCategoryViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextCategoryVi() throws Exception {
        // Get the nextCategoryVi
        restNextCategoryViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextCategoryVi() throws Exception {
        // Initialize the database
        insertedNextCategoryVi = nextCategoryViRepository.saveAndFlush(nextCategoryVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCategoryVi
        NextCategoryVi updatedNextCategoryVi = nextCategoryViRepository.findById(nextCategoryVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextCategoryVi are not directly saved in db
        em.detach(updatedNextCategoryVi);
        updatedNextCategoryVi.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restNextCategoryViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextCategoryVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextCategoryVi))
            )
            .andExpect(status().isOk());

        // Validate the NextCategoryVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextCategoryViToMatchAllProperties(updatedNextCategoryVi);
    }

    @Test
    @Transactional
    void putNonExistingNextCategoryVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCategoryViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextCategoryVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCategoryVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextCategoryVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategoryViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCategoryVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextCategoryVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategoryViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategoryVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCategoryVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextCategoryViWithPatch() throws Exception {
        // Initialize the database
        insertedNextCategoryVi = nextCategoryViRepository.saveAndFlush(nextCategoryVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCategoryVi using partial update
        NextCategoryVi partialUpdatedNextCategoryVi = new NextCategoryVi();
        partialUpdatedNextCategoryVi.setId(nextCategoryVi.getId());

        partialUpdatedNextCategoryVi.description(UPDATED_DESCRIPTION);

        restNextCategoryViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCategoryVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCategoryVi))
            )
            .andExpect(status().isOk());

        // Validate the NextCategoryVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCategoryViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextCategoryVi, nextCategoryVi),
            getPersistedNextCategoryVi(nextCategoryVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextCategoryViWithPatch() throws Exception {
        // Initialize the database
        insertedNextCategoryVi = nextCategoryViRepository.saveAndFlush(nextCategoryVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCategoryVi using partial update
        NextCategoryVi partialUpdatedNextCategoryVi = new NextCategoryVi();
        partialUpdatedNextCategoryVi.setId(nextCategoryVi.getId());

        partialUpdatedNextCategoryVi.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restNextCategoryViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCategoryVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCategoryVi))
            )
            .andExpect(status().isOk());

        // Validate the NextCategoryVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCategoryViUpdatableFieldsEquals(partialUpdatedNextCategoryVi, getPersistedNextCategoryVi(partialUpdatedNextCategoryVi));
    }

    @Test
    @Transactional
    void patchNonExistingNextCategoryVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCategoryViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextCategoryVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCategoryVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextCategoryVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategoryViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCategoryVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextCategoryVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategoryViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextCategoryVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCategoryVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextCategoryVi() throws Exception {
        // Initialize the database
        insertedNextCategoryVi = nextCategoryViRepository.saveAndFlush(nextCategoryVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextCategoryVi
        restNextCategoryViMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextCategoryVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextCategoryViRepository.count();
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

    protected NextCategoryVi getPersistedNextCategoryVi(NextCategoryVi nextCategoryVi) {
        return nextCategoryViRepository.findById(nextCategoryVi.getId()).orElseThrow();
    }

    protected void assertPersistedNextCategoryViToMatchAllProperties(NextCategoryVi expectedNextCategoryVi) {
        assertNextCategoryViAllPropertiesEquals(expectedNextCategoryVi, getPersistedNextCategoryVi(expectedNextCategoryVi));
    }

    protected void assertPersistedNextCategoryViToMatchUpdatableProperties(NextCategoryVi expectedNextCategoryVi) {
        assertNextCategoryViAllUpdatablePropertiesEquals(expectedNextCategoryVi, getPersistedNextCategoryVi(expectedNextCategoryVi));
    }
}
