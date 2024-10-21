package ai.realworld.web.rest;

import static ai.realworld.domain.HashRossViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.HashRossVi;
import ai.realworld.repository.HashRossViRepository;
import ai.realworld.service.dto.HashRossViDTO;
import ai.realworld.service.mapper.HashRossViMapper;
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
 * Integration tests for the {@link HashRossViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HashRossViResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SLUG = "AAAAAAAAAA";
    private static final String UPDATED_SLUG = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PERMISSION_GRID_JASON = "AAAAAAAAAA";
    private static final String UPDATED_PERMISSION_GRID_JASON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/hash-ross-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HashRossViRepository hashRossViRepository;

    @Autowired
    private HashRossViMapper hashRossViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHashRossViMockMvc;

    private HashRossVi hashRossVi;

    private HashRossVi insertedHashRossVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HashRossVi createEntity() {
        return new HashRossVi()
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
    public static HashRossVi createUpdatedEntity() {
        return new HashRossVi()
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .description(UPDATED_DESCRIPTION)
            .permissionGridJason(UPDATED_PERMISSION_GRID_JASON);
    }

    @BeforeEach
    public void initTest() {
        hashRossVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedHashRossVi != null) {
            hashRossViRepository.delete(insertedHashRossVi);
            insertedHashRossVi = null;
        }
    }

    @Test
    @Transactional
    void createHashRossVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HashRossVi
        HashRossViDTO hashRossViDTO = hashRossViMapper.toDto(hashRossVi);
        var returnedHashRossViDTO = om.readValue(
            restHashRossViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hashRossViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HashRossViDTO.class
        );

        // Validate the HashRossVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHashRossVi = hashRossViMapper.toEntity(returnedHashRossViDTO);
        assertHashRossViUpdatableFieldsEquals(returnedHashRossVi, getPersistedHashRossVi(returnedHashRossVi));

        insertedHashRossVi = returnedHashRossVi;
    }

    @Test
    @Transactional
    void createHashRossViWithExistingId() throws Exception {
        // Create the HashRossVi with an existing ID
        hashRossVi.setId(1L);
        HashRossViDTO hashRossViDTO = hashRossViMapper.toDto(hashRossVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHashRossViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hashRossViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HashRossVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hashRossVi.setName(null);

        // Create the HashRossVi, which fails.
        HashRossViDTO hashRossViDTO = hashRossViMapper.toDto(hashRossVi);

        restHashRossViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hashRossViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSlugIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hashRossVi.setSlug(null);

        // Create the HashRossVi, which fails.
        HashRossViDTO hashRossViDTO = hashRossViMapper.toDto(hashRossVi);

        restHashRossViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hashRossViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHashRossVis() throws Exception {
        // Initialize the database
        insertedHashRossVi = hashRossViRepository.saveAndFlush(hashRossVi);

        // Get all the hashRossViList
        restHashRossViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hashRossVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].permissionGridJason").value(hasItem(DEFAULT_PERMISSION_GRID_JASON)));
    }

    @Test
    @Transactional
    void getHashRossVi() throws Exception {
        // Initialize the database
        insertedHashRossVi = hashRossViRepository.saveAndFlush(hashRossVi);

        // Get the hashRossVi
        restHashRossViMockMvc
            .perform(get(ENTITY_API_URL_ID, hashRossVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hashRossVi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.permissionGridJason").value(DEFAULT_PERMISSION_GRID_JASON));
    }

    @Test
    @Transactional
    void getHashRossVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedHashRossVi = hashRossViRepository.saveAndFlush(hashRossVi);

        Long id = hashRossVi.getId();

        defaultHashRossViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultHashRossViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultHashRossViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHashRossVisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHashRossVi = hashRossViRepository.saveAndFlush(hashRossVi);

        // Get all the hashRossViList where name equals to
        defaultHashRossViFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHashRossVisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHashRossVi = hashRossViRepository.saveAndFlush(hashRossVi);

        // Get all the hashRossViList where name in
        defaultHashRossViFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHashRossVisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHashRossVi = hashRossViRepository.saveAndFlush(hashRossVi);

        // Get all the hashRossViList where name is not null
        defaultHashRossViFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllHashRossVisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedHashRossVi = hashRossViRepository.saveAndFlush(hashRossVi);

        // Get all the hashRossViList where name contains
        defaultHashRossViFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHashRossVisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHashRossVi = hashRossViRepository.saveAndFlush(hashRossVi);

        // Get all the hashRossViList where name does not contain
        defaultHashRossViFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllHashRossVisBySlugIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHashRossVi = hashRossViRepository.saveAndFlush(hashRossVi);

        // Get all the hashRossViList where slug equals to
        defaultHashRossViFiltering("slug.equals=" + DEFAULT_SLUG, "slug.equals=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllHashRossVisBySlugIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHashRossVi = hashRossViRepository.saveAndFlush(hashRossVi);

        // Get all the hashRossViList where slug in
        defaultHashRossViFiltering("slug.in=" + DEFAULT_SLUG + "," + UPDATED_SLUG, "slug.in=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllHashRossVisBySlugIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHashRossVi = hashRossViRepository.saveAndFlush(hashRossVi);

        // Get all the hashRossViList where slug is not null
        defaultHashRossViFiltering("slug.specified=true", "slug.specified=false");
    }

    @Test
    @Transactional
    void getAllHashRossVisBySlugContainsSomething() throws Exception {
        // Initialize the database
        insertedHashRossVi = hashRossViRepository.saveAndFlush(hashRossVi);

        // Get all the hashRossViList where slug contains
        defaultHashRossViFiltering("slug.contains=" + DEFAULT_SLUG, "slug.contains=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllHashRossVisBySlugNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHashRossVi = hashRossViRepository.saveAndFlush(hashRossVi);

        // Get all the hashRossViList where slug does not contain
        defaultHashRossViFiltering("slug.doesNotContain=" + UPDATED_SLUG, "slug.doesNotContain=" + DEFAULT_SLUG);
    }

    @Test
    @Transactional
    void getAllHashRossVisByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHashRossVi = hashRossViRepository.saveAndFlush(hashRossVi);

        // Get all the hashRossViList where description equals to
        defaultHashRossViFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllHashRossVisByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHashRossVi = hashRossViRepository.saveAndFlush(hashRossVi);

        // Get all the hashRossViList where description in
        defaultHashRossViFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllHashRossVisByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHashRossVi = hashRossViRepository.saveAndFlush(hashRossVi);

        // Get all the hashRossViList where description is not null
        defaultHashRossViFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllHashRossVisByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedHashRossVi = hashRossViRepository.saveAndFlush(hashRossVi);

        // Get all the hashRossViList where description contains
        defaultHashRossViFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllHashRossVisByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHashRossVi = hashRossViRepository.saveAndFlush(hashRossVi);

        // Get all the hashRossViList where description does not contain
        defaultHashRossViFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllHashRossVisByPermissionGridJasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHashRossVi = hashRossViRepository.saveAndFlush(hashRossVi);

        // Get all the hashRossViList where permissionGridJason equals to
        defaultHashRossViFiltering(
            "permissionGridJason.equals=" + DEFAULT_PERMISSION_GRID_JASON,
            "permissionGridJason.equals=" + UPDATED_PERMISSION_GRID_JASON
        );
    }

    @Test
    @Transactional
    void getAllHashRossVisByPermissionGridJasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHashRossVi = hashRossViRepository.saveAndFlush(hashRossVi);

        // Get all the hashRossViList where permissionGridJason in
        defaultHashRossViFiltering(
            "permissionGridJason.in=" + DEFAULT_PERMISSION_GRID_JASON + "," + UPDATED_PERMISSION_GRID_JASON,
            "permissionGridJason.in=" + UPDATED_PERMISSION_GRID_JASON
        );
    }

    @Test
    @Transactional
    void getAllHashRossVisByPermissionGridJasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHashRossVi = hashRossViRepository.saveAndFlush(hashRossVi);

        // Get all the hashRossViList where permissionGridJason is not null
        defaultHashRossViFiltering("permissionGridJason.specified=true", "permissionGridJason.specified=false");
    }

    @Test
    @Transactional
    void getAllHashRossVisByPermissionGridJasonContainsSomething() throws Exception {
        // Initialize the database
        insertedHashRossVi = hashRossViRepository.saveAndFlush(hashRossVi);

        // Get all the hashRossViList where permissionGridJason contains
        defaultHashRossViFiltering(
            "permissionGridJason.contains=" + DEFAULT_PERMISSION_GRID_JASON,
            "permissionGridJason.contains=" + UPDATED_PERMISSION_GRID_JASON
        );
    }

    @Test
    @Transactional
    void getAllHashRossVisByPermissionGridJasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHashRossVi = hashRossViRepository.saveAndFlush(hashRossVi);

        // Get all the hashRossViList where permissionGridJason does not contain
        defaultHashRossViFiltering(
            "permissionGridJason.doesNotContain=" + UPDATED_PERMISSION_GRID_JASON,
            "permissionGridJason.doesNotContain=" + DEFAULT_PERMISSION_GRID_JASON
        );
    }

    private void defaultHashRossViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultHashRossViShouldBeFound(shouldBeFound);
        defaultHashRossViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHashRossViShouldBeFound(String filter) throws Exception {
        restHashRossViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hashRossVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].permissionGridJason").value(hasItem(DEFAULT_PERMISSION_GRID_JASON)));

        // Check, that the count call also returns 1
        restHashRossViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHashRossViShouldNotBeFound(String filter) throws Exception {
        restHashRossViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHashRossViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHashRossVi() throws Exception {
        // Get the hashRossVi
        restHashRossViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHashRossVi() throws Exception {
        // Initialize the database
        insertedHashRossVi = hashRossViRepository.saveAndFlush(hashRossVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hashRossVi
        HashRossVi updatedHashRossVi = hashRossViRepository.findById(hashRossVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHashRossVi are not directly saved in db
        em.detach(updatedHashRossVi);
        updatedHashRossVi
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .description(UPDATED_DESCRIPTION)
            .permissionGridJason(UPDATED_PERMISSION_GRID_JASON);
        HashRossViDTO hashRossViDTO = hashRossViMapper.toDto(updatedHashRossVi);

        restHashRossViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hashRossViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hashRossViDTO))
            )
            .andExpect(status().isOk());

        // Validate the HashRossVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHashRossViToMatchAllProperties(updatedHashRossVi);
    }

    @Test
    @Transactional
    void putNonExistingHashRossVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hashRossVi.setId(longCount.incrementAndGet());

        // Create the HashRossVi
        HashRossViDTO hashRossViDTO = hashRossViMapper.toDto(hashRossVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHashRossViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hashRossViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hashRossViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HashRossVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHashRossVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hashRossVi.setId(longCount.incrementAndGet());

        // Create the HashRossVi
        HashRossViDTO hashRossViDTO = hashRossViMapper.toDto(hashRossVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHashRossViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hashRossViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HashRossVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHashRossVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hashRossVi.setId(longCount.incrementAndGet());

        // Create the HashRossVi
        HashRossViDTO hashRossViDTO = hashRossViMapper.toDto(hashRossVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHashRossViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hashRossViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HashRossVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHashRossViWithPatch() throws Exception {
        // Initialize the database
        insertedHashRossVi = hashRossViRepository.saveAndFlush(hashRossVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hashRossVi using partial update
        HashRossVi partialUpdatedHashRossVi = new HashRossVi();
        partialUpdatedHashRossVi.setId(hashRossVi.getId());

        partialUpdatedHashRossVi.slug(UPDATED_SLUG).description(UPDATED_DESCRIPTION);

        restHashRossViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHashRossVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHashRossVi))
            )
            .andExpect(status().isOk());

        // Validate the HashRossVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHashRossViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHashRossVi, hashRossVi),
            getPersistedHashRossVi(hashRossVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateHashRossViWithPatch() throws Exception {
        // Initialize the database
        insertedHashRossVi = hashRossViRepository.saveAndFlush(hashRossVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hashRossVi using partial update
        HashRossVi partialUpdatedHashRossVi = new HashRossVi();
        partialUpdatedHashRossVi.setId(hashRossVi.getId());

        partialUpdatedHashRossVi
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .description(UPDATED_DESCRIPTION)
            .permissionGridJason(UPDATED_PERMISSION_GRID_JASON);

        restHashRossViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHashRossVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHashRossVi))
            )
            .andExpect(status().isOk());

        // Validate the HashRossVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHashRossViUpdatableFieldsEquals(partialUpdatedHashRossVi, getPersistedHashRossVi(partialUpdatedHashRossVi));
    }

    @Test
    @Transactional
    void patchNonExistingHashRossVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hashRossVi.setId(longCount.incrementAndGet());

        // Create the HashRossVi
        HashRossViDTO hashRossViDTO = hashRossViMapper.toDto(hashRossVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHashRossViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hashRossViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hashRossViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HashRossVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHashRossVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hashRossVi.setId(longCount.incrementAndGet());

        // Create the HashRossVi
        HashRossViDTO hashRossViDTO = hashRossViMapper.toDto(hashRossVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHashRossViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hashRossViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HashRossVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHashRossVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hashRossVi.setId(longCount.incrementAndGet());

        // Create the HashRossVi
        HashRossViDTO hashRossViDTO = hashRossViMapper.toDto(hashRossVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHashRossViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(hashRossViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HashRossVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHashRossVi() throws Exception {
        // Initialize the database
        insertedHashRossVi = hashRossViRepository.saveAndFlush(hashRossVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the hashRossVi
        restHashRossViMockMvc
            .perform(delete(ENTITY_API_URL_ID, hashRossVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return hashRossViRepository.count();
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

    protected HashRossVi getPersistedHashRossVi(HashRossVi hashRossVi) {
        return hashRossViRepository.findById(hashRossVi.getId()).orElseThrow();
    }

    protected void assertPersistedHashRossViToMatchAllProperties(HashRossVi expectedHashRossVi) {
        assertHashRossViAllPropertiesEquals(expectedHashRossVi, getPersistedHashRossVi(expectedHashRossVi));
    }

    protected void assertPersistedHashRossViToMatchUpdatableProperties(HashRossVi expectedHashRossVi) {
        assertHashRossViAllUpdatablePropertiesEquals(expectedHashRossVi, getPersistedHashRossVi(expectedHashRossVi));
    }
}
