package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextCategoryAlphaAsserts.*;
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
import xyz.jhmapstruct.domain.NextCategoryAlpha;
import xyz.jhmapstruct.repository.NextCategoryAlphaRepository;

/**
 * Integration tests for the {@link NextCategoryAlphaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextCategoryAlphaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/next-category-alphas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextCategoryAlphaRepository nextCategoryAlphaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextCategoryAlphaMockMvc;

    private NextCategoryAlpha nextCategoryAlpha;

    private NextCategoryAlpha insertedNextCategoryAlpha;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextCategoryAlpha createEntity() {
        return new NextCategoryAlpha().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextCategoryAlpha createUpdatedEntity() {
        return new NextCategoryAlpha().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        nextCategoryAlpha = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextCategoryAlpha != null) {
            nextCategoryAlphaRepository.delete(insertedNextCategoryAlpha);
            insertedNextCategoryAlpha = null;
        }
    }

    @Test
    @Transactional
    void createNextCategoryAlpha() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextCategoryAlpha
        var returnedNextCategoryAlpha = om.readValue(
            restNextCategoryAlphaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategoryAlpha)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextCategoryAlpha.class
        );

        // Validate the NextCategoryAlpha in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextCategoryAlphaUpdatableFieldsEquals(returnedNextCategoryAlpha, getPersistedNextCategoryAlpha(returnedNextCategoryAlpha));

        insertedNextCategoryAlpha = returnedNextCategoryAlpha;
    }

    @Test
    @Transactional
    void createNextCategoryAlphaWithExistingId() throws Exception {
        // Create the NextCategoryAlpha with an existing ID
        nextCategoryAlpha.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextCategoryAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategoryAlpha)))
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCategoryAlpha.setName(null);

        // Create the NextCategoryAlpha, which fails.

        restNextCategoryAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategoryAlpha)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextCategoryAlphas() throws Exception {
        // Initialize the database
        insertedNextCategoryAlpha = nextCategoryAlphaRepository.saveAndFlush(nextCategoryAlpha);

        // Get all the nextCategoryAlphaList
        restNextCategoryAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCategoryAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getNextCategoryAlpha() throws Exception {
        // Initialize the database
        insertedNextCategoryAlpha = nextCategoryAlphaRepository.saveAndFlush(nextCategoryAlpha);

        // Get the nextCategoryAlpha
        restNextCategoryAlphaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextCategoryAlpha.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextCategoryAlpha.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNextCategoryAlphasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextCategoryAlpha = nextCategoryAlphaRepository.saveAndFlush(nextCategoryAlpha);

        Long id = nextCategoryAlpha.getId();

        defaultNextCategoryAlphaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextCategoryAlphaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextCategoryAlphaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextCategoryAlphasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryAlpha = nextCategoryAlphaRepository.saveAndFlush(nextCategoryAlpha);

        // Get all the nextCategoryAlphaList where name equals to
        defaultNextCategoryAlphaFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategoryAlphasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCategoryAlpha = nextCategoryAlphaRepository.saveAndFlush(nextCategoryAlpha);

        // Get all the nextCategoryAlphaList where name in
        defaultNextCategoryAlphaFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategoryAlphasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCategoryAlpha = nextCategoryAlphaRepository.saveAndFlush(nextCategoryAlpha);

        // Get all the nextCategoryAlphaList where name is not null
        defaultNextCategoryAlphaFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCategoryAlphasByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryAlpha = nextCategoryAlphaRepository.saveAndFlush(nextCategoryAlpha);

        // Get all the nextCategoryAlphaList where name contains
        defaultNextCategoryAlphaFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategoryAlphasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryAlpha = nextCategoryAlphaRepository.saveAndFlush(nextCategoryAlpha);

        // Get all the nextCategoryAlphaList where name does not contain
        defaultNextCategoryAlphaFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategoryAlphasByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryAlpha = nextCategoryAlphaRepository.saveAndFlush(nextCategoryAlpha);

        // Get all the nextCategoryAlphaList where description equals to
        defaultNextCategoryAlphaFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNextCategoryAlphasByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCategoryAlpha = nextCategoryAlphaRepository.saveAndFlush(nextCategoryAlpha);

        // Get all the nextCategoryAlphaList where description in
        defaultNextCategoryAlphaFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllNextCategoryAlphasByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCategoryAlpha = nextCategoryAlphaRepository.saveAndFlush(nextCategoryAlpha);

        // Get all the nextCategoryAlphaList where description is not null
        defaultNextCategoryAlphaFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCategoryAlphasByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryAlpha = nextCategoryAlphaRepository.saveAndFlush(nextCategoryAlpha);

        // Get all the nextCategoryAlphaList where description contains
        defaultNextCategoryAlphaFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNextCategoryAlphasByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryAlpha = nextCategoryAlphaRepository.saveAndFlush(nextCategoryAlpha);

        // Get all the nextCategoryAlphaList where description does not contain
        defaultNextCategoryAlphaFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllNextCategoryAlphasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextCategoryAlphaRepository.saveAndFlush(nextCategoryAlpha);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextCategoryAlpha.setTenant(tenant);
        nextCategoryAlphaRepository.saveAndFlush(nextCategoryAlpha);
        Long tenantId = tenant.getId();
        // Get all the nextCategoryAlphaList where tenant equals to tenantId
        defaultNextCategoryAlphaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextCategoryAlphaList where tenant equals to (tenantId + 1)
        defaultNextCategoryAlphaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextCategoryAlphaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextCategoryAlphaShouldBeFound(shouldBeFound);
        defaultNextCategoryAlphaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextCategoryAlphaShouldBeFound(String filter) throws Exception {
        restNextCategoryAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCategoryAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restNextCategoryAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextCategoryAlphaShouldNotBeFound(String filter) throws Exception {
        restNextCategoryAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextCategoryAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextCategoryAlpha() throws Exception {
        // Get the nextCategoryAlpha
        restNextCategoryAlphaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextCategoryAlpha() throws Exception {
        // Initialize the database
        insertedNextCategoryAlpha = nextCategoryAlphaRepository.saveAndFlush(nextCategoryAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCategoryAlpha
        NextCategoryAlpha updatedNextCategoryAlpha = nextCategoryAlphaRepository.findById(nextCategoryAlpha.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextCategoryAlpha are not directly saved in db
        em.detach(updatedNextCategoryAlpha);
        updatedNextCategoryAlpha.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restNextCategoryAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextCategoryAlpha.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextCategoryAlpha))
            )
            .andExpect(status().isOk());

        // Validate the NextCategoryAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextCategoryAlphaToMatchAllProperties(updatedNextCategoryAlpha);
    }

    @Test
    @Transactional
    void putNonExistingNextCategoryAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryAlpha.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCategoryAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextCategoryAlpha.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCategoryAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextCategoryAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategoryAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCategoryAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextCategoryAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategoryAlphaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategoryAlpha)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCategoryAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextCategoryAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedNextCategoryAlpha = nextCategoryAlphaRepository.saveAndFlush(nextCategoryAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCategoryAlpha using partial update
        NextCategoryAlpha partialUpdatedNextCategoryAlpha = new NextCategoryAlpha();
        partialUpdatedNextCategoryAlpha.setId(nextCategoryAlpha.getId());

        partialUpdatedNextCategoryAlpha.description(UPDATED_DESCRIPTION);

        restNextCategoryAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCategoryAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCategoryAlpha))
            )
            .andExpect(status().isOk());

        // Validate the NextCategoryAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCategoryAlphaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextCategoryAlpha, nextCategoryAlpha),
            getPersistedNextCategoryAlpha(nextCategoryAlpha)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextCategoryAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedNextCategoryAlpha = nextCategoryAlphaRepository.saveAndFlush(nextCategoryAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCategoryAlpha using partial update
        NextCategoryAlpha partialUpdatedNextCategoryAlpha = new NextCategoryAlpha();
        partialUpdatedNextCategoryAlpha.setId(nextCategoryAlpha.getId());

        partialUpdatedNextCategoryAlpha.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restNextCategoryAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCategoryAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCategoryAlpha))
            )
            .andExpect(status().isOk());

        // Validate the NextCategoryAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCategoryAlphaUpdatableFieldsEquals(
            partialUpdatedNextCategoryAlpha,
            getPersistedNextCategoryAlpha(partialUpdatedNextCategoryAlpha)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextCategoryAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryAlpha.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCategoryAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextCategoryAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCategoryAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextCategoryAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategoryAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCategoryAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextCategoryAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategoryAlphaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextCategoryAlpha)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCategoryAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextCategoryAlpha() throws Exception {
        // Initialize the database
        insertedNextCategoryAlpha = nextCategoryAlphaRepository.saveAndFlush(nextCategoryAlpha);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextCategoryAlpha
        restNextCategoryAlphaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextCategoryAlpha.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextCategoryAlphaRepository.count();
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

    protected NextCategoryAlpha getPersistedNextCategoryAlpha(NextCategoryAlpha nextCategoryAlpha) {
        return nextCategoryAlphaRepository.findById(nextCategoryAlpha.getId()).orElseThrow();
    }

    protected void assertPersistedNextCategoryAlphaToMatchAllProperties(NextCategoryAlpha expectedNextCategoryAlpha) {
        assertNextCategoryAlphaAllPropertiesEquals(expectedNextCategoryAlpha, getPersistedNextCategoryAlpha(expectedNextCategoryAlpha));
    }

    protected void assertPersistedNextCategoryAlphaToMatchUpdatableProperties(NextCategoryAlpha expectedNextCategoryAlpha) {
        assertNextCategoryAlphaAllUpdatablePropertiesEquals(
            expectedNextCategoryAlpha,
            getPersistedNextCategoryAlpha(expectedNextCategoryAlpha)
        );
    }
}
