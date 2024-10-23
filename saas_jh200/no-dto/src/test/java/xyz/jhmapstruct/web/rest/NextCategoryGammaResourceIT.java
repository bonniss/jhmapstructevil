package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextCategoryGammaAsserts.*;
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
import xyz.jhmapstruct.domain.NextCategoryGamma;
import xyz.jhmapstruct.repository.NextCategoryGammaRepository;

/**
 * Integration tests for the {@link NextCategoryGammaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextCategoryGammaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/next-category-gammas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextCategoryGammaRepository nextCategoryGammaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextCategoryGammaMockMvc;

    private NextCategoryGamma nextCategoryGamma;

    private NextCategoryGamma insertedNextCategoryGamma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextCategoryGamma createEntity() {
        return new NextCategoryGamma().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextCategoryGamma createUpdatedEntity() {
        return new NextCategoryGamma().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        nextCategoryGamma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextCategoryGamma != null) {
            nextCategoryGammaRepository.delete(insertedNextCategoryGamma);
            insertedNextCategoryGamma = null;
        }
    }

    @Test
    @Transactional
    void createNextCategoryGamma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextCategoryGamma
        var returnedNextCategoryGamma = om.readValue(
            restNextCategoryGammaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategoryGamma)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextCategoryGamma.class
        );

        // Validate the NextCategoryGamma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextCategoryGammaUpdatableFieldsEquals(returnedNextCategoryGamma, getPersistedNextCategoryGamma(returnedNextCategoryGamma));

        insertedNextCategoryGamma = returnedNextCategoryGamma;
    }

    @Test
    @Transactional
    void createNextCategoryGammaWithExistingId() throws Exception {
        // Create the NextCategoryGamma with an existing ID
        nextCategoryGamma.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextCategoryGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategoryGamma)))
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCategoryGamma.setName(null);

        // Create the NextCategoryGamma, which fails.

        restNextCategoryGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategoryGamma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextCategoryGammas() throws Exception {
        // Initialize the database
        insertedNextCategoryGamma = nextCategoryGammaRepository.saveAndFlush(nextCategoryGamma);

        // Get all the nextCategoryGammaList
        restNextCategoryGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCategoryGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getNextCategoryGamma() throws Exception {
        // Initialize the database
        insertedNextCategoryGamma = nextCategoryGammaRepository.saveAndFlush(nextCategoryGamma);

        // Get the nextCategoryGamma
        restNextCategoryGammaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextCategoryGamma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextCategoryGamma.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNextCategoryGammasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextCategoryGamma = nextCategoryGammaRepository.saveAndFlush(nextCategoryGamma);

        Long id = nextCategoryGamma.getId();

        defaultNextCategoryGammaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextCategoryGammaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextCategoryGammaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextCategoryGammasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryGamma = nextCategoryGammaRepository.saveAndFlush(nextCategoryGamma);

        // Get all the nextCategoryGammaList where name equals to
        defaultNextCategoryGammaFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategoryGammasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCategoryGamma = nextCategoryGammaRepository.saveAndFlush(nextCategoryGamma);

        // Get all the nextCategoryGammaList where name in
        defaultNextCategoryGammaFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategoryGammasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCategoryGamma = nextCategoryGammaRepository.saveAndFlush(nextCategoryGamma);

        // Get all the nextCategoryGammaList where name is not null
        defaultNextCategoryGammaFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCategoryGammasByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryGamma = nextCategoryGammaRepository.saveAndFlush(nextCategoryGamma);

        // Get all the nextCategoryGammaList where name contains
        defaultNextCategoryGammaFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategoryGammasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryGamma = nextCategoryGammaRepository.saveAndFlush(nextCategoryGamma);

        // Get all the nextCategoryGammaList where name does not contain
        defaultNextCategoryGammaFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategoryGammasByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryGamma = nextCategoryGammaRepository.saveAndFlush(nextCategoryGamma);

        // Get all the nextCategoryGammaList where description equals to
        defaultNextCategoryGammaFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNextCategoryGammasByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCategoryGamma = nextCategoryGammaRepository.saveAndFlush(nextCategoryGamma);

        // Get all the nextCategoryGammaList where description in
        defaultNextCategoryGammaFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllNextCategoryGammasByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCategoryGamma = nextCategoryGammaRepository.saveAndFlush(nextCategoryGamma);

        // Get all the nextCategoryGammaList where description is not null
        defaultNextCategoryGammaFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCategoryGammasByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryGamma = nextCategoryGammaRepository.saveAndFlush(nextCategoryGamma);

        // Get all the nextCategoryGammaList where description contains
        defaultNextCategoryGammaFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNextCategoryGammasByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryGamma = nextCategoryGammaRepository.saveAndFlush(nextCategoryGamma);

        // Get all the nextCategoryGammaList where description does not contain
        defaultNextCategoryGammaFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllNextCategoryGammasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextCategoryGammaRepository.saveAndFlush(nextCategoryGamma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextCategoryGamma.setTenant(tenant);
        nextCategoryGammaRepository.saveAndFlush(nextCategoryGamma);
        Long tenantId = tenant.getId();
        // Get all the nextCategoryGammaList where tenant equals to tenantId
        defaultNextCategoryGammaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextCategoryGammaList where tenant equals to (tenantId + 1)
        defaultNextCategoryGammaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextCategoryGammaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextCategoryGammaShouldBeFound(shouldBeFound);
        defaultNextCategoryGammaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextCategoryGammaShouldBeFound(String filter) throws Exception {
        restNextCategoryGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCategoryGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restNextCategoryGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextCategoryGammaShouldNotBeFound(String filter) throws Exception {
        restNextCategoryGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextCategoryGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextCategoryGamma() throws Exception {
        // Get the nextCategoryGamma
        restNextCategoryGammaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextCategoryGamma() throws Exception {
        // Initialize the database
        insertedNextCategoryGamma = nextCategoryGammaRepository.saveAndFlush(nextCategoryGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCategoryGamma
        NextCategoryGamma updatedNextCategoryGamma = nextCategoryGammaRepository.findById(nextCategoryGamma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextCategoryGamma are not directly saved in db
        em.detach(updatedNextCategoryGamma);
        updatedNextCategoryGamma.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restNextCategoryGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextCategoryGamma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextCategoryGamma))
            )
            .andExpect(status().isOk());

        // Validate the NextCategoryGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextCategoryGammaToMatchAllProperties(updatedNextCategoryGamma);
    }

    @Test
    @Transactional
    void putNonExistingNextCategoryGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryGamma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCategoryGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextCategoryGamma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCategoryGamma))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextCategoryGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryGamma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategoryGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCategoryGamma))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextCategoryGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryGamma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategoryGammaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategoryGamma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCategoryGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextCategoryGammaWithPatch() throws Exception {
        // Initialize the database
        insertedNextCategoryGamma = nextCategoryGammaRepository.saveAndFlush(nextCategoryGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCategoryGamma using partial update
        NextCategoryGamma partialUpdatedNextCategoryGamma = new NextCategoryGamma();
        partialUpdatedNextCategoryGamma.setId(nextCategoryGamma.getId());

        partialUpdatedNextCategoryGamma.description(UPDATED_DESCRIPTION);

        restNextCategoryGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCategoryGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCategoryGamma))
            )
            .andExpect(status().isOk());

        // Validate the NextCategoryGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCategoryGammaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextCategoryGamma, nextCategoryGamma),
            getPersistedNextCategoryGamma(nextCategoryGamma)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextCategoryGammaWithPatch() throws Exception {
        // Initialize the database
        insertedNextCategoryGamma = nextCategoryGammaRepository.saveAndFlush(nextCategoryGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCategoryGamma using partial update
        NextCategoryGamma partialUpdatedNextCategoryGamma = new NextCategoryGamma();
        partialUpdatedNextCategoryGamma.setId(nextCategoryGamma.getId());

        partialUpdatedNextCategoryGamma.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restNextCategoryGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCategoryGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCategoryGamma))
            )
            .andExpect(status().isOk());

        // Validate the NextCategoryGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCategoryGammaUpdatableFieldsEquals(
            partialUpdatedNextCategoryGamma,
            getPersistedNextCategoryGamma(partialUpdatedNextCategoryGamma)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextCategoryGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryGamma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCategoryGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextCategoryGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCategoryGamma))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextCategoryGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryGamma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategoryGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCategoryGamma))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextCategoryGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryGamma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategoryGammaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextCategoryGamma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCategoryGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextCategoryGamma() throws Exception {
        // Initialize the database
        insertedNextCategoryGamma = nextCategoryGammaRepository.saveAndFlush(nextCategoryGamma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextCategoryGamma
        restNextCategoryGammaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextCategoryGamma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextCategoryGammaRepository.count();
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

    protected NextCategoryGamma getPersistedNextCategoryGamma(NextCategoryGamma nextCategoryGamma) {
        return nextCategoryGammaRepository.findById(nextCategoryGamma.getId()).orElseThrow();
    }

    protected void assertPersistedNextCategoryGammaToMatchAllProperties(NextCategoryGamma expectedNextCategoryGamma) {
        assertNextCategoryGammaAllPropertiesEquals(expectedNextCategoryGamma, getPersistedNextCategoryGamma(expectedNextCategoryGamma));
    }

    protected void assertPersistedNextCategoryGammaToMatchUpdatableProperties(NextCategoryGamma expectedNextCategoryGamma) {
        assertNextCategoryGammaAllUpdatablePropertiesEquals(
            expectedNextCategoryGamma,
            getPersistedNextCategoryGamma(expectedNextCategoryGamma)
        );
    }
}
