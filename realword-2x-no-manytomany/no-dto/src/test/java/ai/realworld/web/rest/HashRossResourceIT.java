package ai.realworld.web.rest;

import static ai.realworld.domain.HashRossAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.HashRoss;
import ai.realworld.repository.HashRossRepository;
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

/**
 * Integration tests for the {@link HashRossResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HashRossResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SLUG = "AAAAAAAAAA";
    private static final String UPDATED_SLUG = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PERMISSION_GRID_JASON = "AAAAAAAAAA";
    private static final String UPDATED_PERMISSION_GRID_JASON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/hash-rosses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HashRossRepository hashRossRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHashRossMockMvc;

    private HashRoss hashRoss;

    private HashRoss insertedHashRoss;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HashRoss createEntity() {
        return new HashRoss()
            .name(DEFAULT_NAME)
            .slug(DEFAULT_SLUG)
            .description(DEFAULT_DESCRIPTION)
            .permissionGridJason(DEFAULT_PERMISSION_GRID_JASON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HashRoss createUpdatedEntity() {
        return new HashRoss()
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .description(UPDATED_DESCRIPTION)
            .permissionGridJason(UPDATED_PERMISSION_GRID_JASON);
    }

    @BeforeEach
    public void initTest() {
        hashRoss = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedHashRoss != null) {
            hashRossRepository.delete(insertedHashRoss);
            insertedHashRoss = null;
        }
    }

    @Test
    @Transactional
    void createHashRoss() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HashRoss
        var returnedHashRoss = om.readValue(
            restHashRossMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hashRoss)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HashRoss.class
        );

        // Validate the HashRoss in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertHashRossUpdatableFieldsEquals(returnedHashRoss, getPersistedHashRoss(returnedHashRoss));

        insertedHashRoss = returnedHashRoss;
    }

    @Test
    @Transactional
    void createHashRossWithExistingId() throws Exception {
        // Create the HashRoss with an existing ID
        hashRoss.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHashRossMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hashRoss)))
            .andExpect(status().isBadRequest());

        // Validate the HashRoss in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hashRoss.setName(null);

        // Create the HashRoss, which fails.

        restHashRossMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hashRoss)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSlugIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hashRoss.setSlug(null);

        // Create the HashRoss, which fails.

        restHashRossMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hashRoss)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHashRosses() throws Exception {
        // Initialize the database
        insertedHashRoss = hashRossRepository.saveAndFlush(hashRoss);

        // Get all the hashRossList
        restHashRossMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hashRoss.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].permissionGridJason").value(hasItem(DEFAULT_PERMISSION_GRID_JASON)));
    }

    @Test
    @Transactional
    void getHashRoss() throws Exception {
        // Initialize the database
        insertedHashRoss = hashRossRepository.saveAndFlush(hashRoss);

        // Get the hashRoss
        restHashRossMockMvc
            .perform(get(ENTITY_API_URL_ID, hashRoss.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hashRoss.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.permissionGridJason").value(DEFAULT_PERMISSION_GRID_JASON));
    }

    @Test
    @Transactional
    void getHashRossesByIdFiltering() throws Exception {
        // Initialize the database
        insertedHashRoss = hashRossRepository.saveAndFlush(hashRoss);

        Long id = hashRoss.getId();

        defaultHashRossFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultHashRossFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultHashRossFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHashRossesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHashRoss = hashRossRepository.saveAndFlush(hashRoss);

        // Get all the hashRossList where name equals to
        defaultHashRossFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHashRossesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHashRoss = hashRossRepository.saveAndFlush(hashRoss);

        // Get all the hashRossList where name in
        defaultHashRossFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHashRossesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHashRoss = hashRossRepository.saveAndFlush(hashRoss);

        // Get all the hashRossList where name is not null
        defaultHashRossFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllHashRossesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedHashRoss = hashRossRepository.saveAndFlush(hashRoss);

        // Get all the hashRossList where name contains
        defaultHashRossFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHashRossesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHashRoss = hashRossRepository.saveAndFlush(hashRoss);

        // Get all the hashRossList where name does not contain
        defaultHashRossFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllHashRossesBySlugIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHashRoss = hashRossRepository.saveAndFlush(hashRoss);

        // Get all the hashRossList where slug equals to
        defaultHashRossFiltering("slug.equals=" + DEFAULT_SLUG, "slug.equals=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllHashRossesBySlugIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHashRoss = hashRossRepository.saveAndFlush(hashRoss);

        // Get all the hashRossList where slug in
        defaultHashRossFiltering("slug.in=" + DEFAULT_SLUG + "," + UPDATED_SLUG, "slug.in=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllHashRossesBySlugIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHashRoss = hashRossRepository.saveAndFlush(hashRoss);

        // Get all the hashRossList where slug is not null
        defaultHashRossFiltering("slug.specified=true", "slug.specified=false");
    }

    @Test
    @Transactional
    void getAllHashRossesBySlugContainsSomething() throws Exception {
        // Initialize the database
        insertedHashRoss = hashRossRepository.saveAndFlush(hashRoss);

        // Get all the hashRossList where slug contains
        defaultHashRossFiltering("slug.contains=" + DEFAULT_SLUG, "slug.contains=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllHashRossesBySlugNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHashRoss = hashRossRepository.saveAndFlush(hashRoss);

        // Get all the hashRossList where slug does not contain
        defaultHashRossFiltering("slug.doesNotContain=" + UPDATED_SLUG, "slug.doesNotContain=" + DEFAULT_SLUG);
    }

    @Test
    @Transactional
    void getAllHashRossesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHashRoss = hashRossRepository.saveAndFlush(hashRoss);

        // Get all the hashRossList where description equals to
        defaultHashRossFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllHashRossesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHashRoss = hashRossRepository.saveAndFlush(hashRoss);

        // Get all the hashRossList where description in
        defaultHashRossFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllHashRossesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHashRoss = hashRossRepository.saveAndFlush(hashRoss);

        // Get all the hashRossList where description is not null
        defaultHashRossFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllHashRossesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedHashRoss = hashRossRepository.saveAndFlush(hashRoss);

        // Get all the hashRossList where description contains
        defaultHashRossFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllHashRossesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHashRoss = hashRossRepository.saveAndFlush(hashRoss);

        // Get all the hashRossList where description does not contain
        defaultHashRossFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllHashRossesByPermissionGridJasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHashRoss = hashRossRepository.saveAndFlush(hashRoss);

        // Get all the hashRossList where permissionGridJason equals to
        defaultHashRossFiltering(
            "permissionGridJason.equals=" + DEFAULT_PERMISSION_GRID_JASON,
            "permissionGridJason.equals=" + UPDATED_PERMISSION_GRID_JASON
        );
    }

    @Test
    @Transactional
    void getAllHashRossesByPermissionGridJasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHashRoss = hashRossRepository.saveAndFlush(hashRoss);

        // Get all the hashRossList where permissionGridJason in
        defaultHashRossFiltering(
            "permissionGridJason.in=" + DEFAULT_PERMISSION_GRID_JASON + "," + UPDATED_PERMISSION_GRID_JASON,
            "permissionGridJason.in=" + UPDATED_PERMISSION_GRID_JASON
        );
    }

    @Test
    @Transactional
    void getAllHashRossesByPermissionGridJasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHashRoss = hashRossRepository.saveAndFlush(hashRoss);

        // Get all the hashRossList where permissionGridJason is not null
        defaultHashRossFiltering("permissionGridJason.specified=true", "permissionGridJason.specified=false");
    }

    @Test
    @Transactional
    void getAllHashRossesByPermissionGridJasonContainsSomething() throws Exception {
        // Initialize the database
        insertedHashRoss = hashRossRepository.saveAndFlush(hashRoss);

        // Get all the hashRossList where permissionGridJason contains
        defaultHashRossFiltering(
            "permissionGridJason.contains=" + DEFAULT_PERMISSION_GRID_JASON,
            "permissionGridJason.contains=" + UPDATED_PERMISSION_GRID_JASON
        );
    }

    @Test
    @Transactional
    void getAllHashRossesByPermissionGridJasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHashRoss = hashRossRepository.saveAndFlush(hashRoss);

        // Get all the hashRossList where permissionGridJason does not contain
        defaultHashRossFiltering(
            "permissionGridJason.doesNotContain=" + UPDATED_PERMISSION_GRID_JASON,
            "permissionGridJason.doesNotContain=" + DEFAULT_PERMISSION_GRID_JASON
        );
    }

    private void defaultHashRossFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultHashRossShouldBeFound(shouldBeFound);
        defaultHashRossShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHashRossShouldBeFound(String filter) throws Exception {
        restHashRossMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hashRoss.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].permissionGridJason").value(hasItem(DEFAULT_PERMISSION_GRID_JASON)));

        // Check, that the count call also returns 1
        restHashRossMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHashRossShouldNotBeFound(String filter) throws Exception {
        restHashRossMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHashRossMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHashRoss() throws Exception {
        // Get the hashRoss
        restHashRossMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHashRoss() throws Exception {
        // Initialize the database
        insertedHashRoss = hashRossRepository.saveAndFlush(hashRoss);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hashRoss
        HashRoss updatedHashRoss = hashRossRepository.findById(hashRoss.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHashRoss are not directly saved in db
        em.detach(updatedHashRoss);
        updatedHashRoss
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .description(UPDATED_DESCRIPTION)
            .permissionGridJason(UPDATED_PERMISSION_GRID_JASON);

        restHashRossMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHashRoss.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedHashRoss))
            )
            .andExpect(status().isOk());

        // Validate the HashRoss in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHashRossToMatchAllProperties(updatedHashRoss);
    }

    @Test
    @Transactional
    void putNonExistingHashRoss() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hashRoss.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHashRossMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hashRoss.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hashRoss))
            )
            .andExpect(status().isBadRequest());

        // Validate the HashRoss in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHashRoss() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hashRoss.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHashRossMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hashRoss))
            )
            .andExpect(status().isBadRequest());

        // Validate the HashRoss in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHashRoss() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hashRoss.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHashRossMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hashRoss)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HashRoss in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHashRossWithPatch() throws Exception {
        // Initialize the database
        insertedHashRoss = hashRossRepository.saveAndFlush(hashRoss);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hashRoss using partial update
        HashRoss partialUpdatedHashRoss = new HashRoss();
        partialUpdatedHashRoss.setId(hashRoss.getId());

        partialUpdatedHashRoss.slug(UPDATED_SLUG).description(UPDATED_DESCRIPTION).permissionGridJason(UPDATED_PERMISSION_GRID_JASON);

        restHashRossMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHashRoss.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHashRoss))
            )
            .andExpect(status().isOk());

        // Validate the HashRoss in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHashRossUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedHashRoss, hashRoss), getPersistedHashRoss(hashRoss));
    }

    @Test
    @Transactional
    void fullUpdateHashRossWithPatch() throws Exception {
        // Initialize the database
        insertedHashRoss = hashRossRepository.saveAndFlush(hashRoss);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hashRoss using partial update
        HashRoss partialUpdatedHashRoss = new HashRoss();
        partialUpdatedHashRoss.setId(hashRoss.getId());

        partialUpdatedHashRoss
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .description(UPDATED_DESCRIPTION)
            .permissionGridJason(UPDATED_PERMISSION_GRID_JASON);

        restHashRossMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHashRoss.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHashRoss))
            )
            .andExpect(status().isOk());

        // Validate the HashRoss in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHashRossUpdatableFieldsEquals(partialUpdatedHashRoss, getPersistedHashRoss(partialUpdatedHashRoss));
    }

    @Test
    @Transactional
    void patchNonExistingHashRoss() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hashRoss.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHashRossMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hashRoss.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hashRoss))
            )
            .andExpect(status().isBadRequest());

        // Validate the HashRoss in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHashRoss() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hashRoss.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHashRossMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hashRoss))
            )
            .andExpect(status().isBadRequest());

        // Validate the HashRoss in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHashRoss() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hashRoss.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHashRossMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(hashRoss)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HashRoss in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHashRoss() throws Exception {
        // Initialize the database
        insertedHashRoss = hashRossRepository.saveAndFlush(hashRoss);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the hashRoss
        restHashRossMockMvc
            .perform(delete(ENTITY_API_URL_ID, hashRoss.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return hashRossRepository.count();
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

    protected HashRoss getPersistedHashRoss(HashRoss hashRoss) {
        return hashRossRepository.findById(hashRoss.getId()).orElseThrow();
    }

    protected void assertPersistedHashRossToMatchAllProperties(HashRoss expectedHashRoss) {
        assertHashRossAllPropertiesEquals(expectedHashRoss, getPersistedHashRoss(expectedHashRoss));
    }

    protected void assertPersistedHashRossToMatchUpdatableProperties(HashRoss expectedHashRoss) {
        assertHashRossAllUpdatablePropertiesEquals(expectedHashRoss, getPersistedHashRoss(expectedHashRoss));
    }
}
