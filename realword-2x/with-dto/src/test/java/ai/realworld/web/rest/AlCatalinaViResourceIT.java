package ai.realworld.web.rest;

import static ai.realworld.domain.AlCatalinaViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlCatalinaVi;
import ai.realworld.domain.AlCatalinaVi;
import ai.realworld.domain.Metaverse;
import ai.realworld.repository.AlCatalinaViRepository;
import ai.realworld.service.dto.AlCatalinaViDTO;
import ai.realworld.service.mapper.AlCatalinaViMapper;
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
 * Integration tests for the {@link AlCatalinaViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlCatalinaViResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_TREE_DEPTH = 1;
    private static final Integer UPDATED_TREE_DEPTH = 2;
    private static final Integer SMALLER_TREE_DEPTH = 1 - 1;

    private static final String ENTITY_API_URL = "/api/al-catalina-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlCatalinaViRepository alCatalinaViRepository;

    @Autowired
    private AlCatalinaViMapper alCatalinaViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlCatalinaViMockMvc;

    private AlCatalinaVi alCatalinaVi;

    private AlCatalinaVi insertedAlCatalinaVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlCatalinaVi createEntity() {
        return new AlCatalinaVi().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).treeDepth(DEFAULT_TREE_DEPTH);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlCatalinaVi createUpdatedEntity() {
        return new AlCatalinaVi().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).treeDepth(UPDATED_TREE_DEPTH);
    }

    @BeforeEach
    public void initTest() {
        alCatalinaVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlCatalinaVi != null) {
            alCatalinaViRepository.delete(insertedAlCatalinaVi);
            insertedAlCatalinaVi = null;
        }
    }

    @Test
    @Transactional
    void createAlCatalinaVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlCatalinaVi
        AlCatalinaViDTO alCatalinaViDTO = alCatalinaViMapper.toDto(alCatalinaVi);
        var returnedAlCatalinaViDTO = om.readValue(
            restAlCatalinaViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alCatalinaViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlCatalinaViDTO.class
        );

        // Validate the AlCatalinaVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlCatalinaVi = alCatalinaViMapper.toEntity(returnedAlCatalinaViDTO);
        assertAlCatalinaViUpdatableFieldsEquals(returnedAlCatalinaVi, getPersistedAlCatalinaVi(returnedAlCatalinaVi));

        insertedAlCatalinaVi = returnedAlCatalinaVi;
    }

    @Test
    @Transactional
    void createAlCatalinaViWithExistingId() throws Exception {
        // Create the AlCatalinaVi with an existing ID
        alCatalinaVi.setId(1L);
        AlCatalinaViDTO alCatalinaViDTO = alCatalinaViMapper.toDto(alCatalinaVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlCatalinaViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alCatalinaViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlCatalinaVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alCatalinaVi.setName(null);

        // Create the AlCatalinaVi, which fails.
        AlCatalinaViDTO alCatalinaViDTO = alCatalinaViMapper.toDto(alCatalinaVi);

        restAlCatalinaViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alCatalinaViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlCatalinaVis() throws Exception {
        // Initialize the database
        insertedAlCatalinaVi = alCatalinaViRepository.saveAndFlush(alCatalinaVi);

        // Get all the alCatalinaViList
        restAlCatalinaViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alCatalinaVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].treeDepth").value(hasItem(DEFAULT_TREE_DEPTH)));
    }

    @Test
    @Transactional
    void getAlCatalinaVi() throws Exception {
        // Initialize the database
        insertedAlCatalinaVi = alCatalinaViRepository.saveAndFlush(alCatalinaVi);

        // Get the alCatalinaVi
        restAlCatalinaViMockMvc
            .perform(get(ENTITY_API_URL_ID, alCatalinaVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alCatalinaVi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.treeDepth").value(DEFAULT_TREE_DEPTH));
    }

    @Test
    @Transactional
    void getAlCatalinaVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlCatalinaVi = alCatalinaViRepository.saveAndFlush(alCatalinaVi);

        Long id = alCatalinaVi.getId();

        defaultAlCatalinaViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlCatalinaViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlCatalinaViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlCatalinaVisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlCatalinaVi = alCatalinaViRepository.saveAndFlush(alCatalinaVi);

        // Get all the alCatalinaViList where name equals to
        defaultAlCatalinaViFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlCatalinaVisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlCatalinaVi = alCatalinaViRepository.saveAndFlush(alCatalinaVi);

        // Get all the alCatalinaViList where name in
        defaultAlCatalinaViFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlCatalinaVisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlCatalinaVi = alCatalinaViRepository.saveAndFlush(alCatalinaVi);

        // Get all the alCatalinaViList where name is not null
        defaultAlCatalinaViFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAlCatalinaVisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlCatalinaVi = alCatalinaViRepository.saveAndFlush(alCatalinaVi);

        // Get all the alCatalinaViList where name contains
        defaultAlCatalinaViFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlCatalinaVisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlCatalinaVi = alCatalinaViRepository.saveAndFlush(alCatalinaVi);

        // Get all the alCatalinaViList where name does not contain
        defaultAlCatalinaViFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAlCatalinaVisByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlCatalinaVi = alCatalinaViRepository.saveAndFlush(alCatalinaVi);

        // Get all the alCatalinaViList where description equals to
        defaultAlCatalinaViFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAlCatalinaVisByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlCatalinaVi = alCatalinaViRepository.saveAndFlush(alCatalinaVi);

        // Get all the alCatalinaViList where description in
        defaultAlCatalinaViFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllAlCatalinaVisByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlCatalinaVi = alCatalinaViRepository.saveAndFlush(alCatalinaVi);

        // Get all the alCatalinaViList where description is not null
        defaultAlCatalinaViFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllAlCatalinaVisByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedAlCatalinaVi = alCatalinaViRepository.saveAndFlush(alCatalinaVi);

        // Get all the alCatalinaViList where description contains
        defaultAlCatalinaViFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAlCatalinaVisByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlCatalinaVi = alCatalinaViRepository.saveAndFlush(alCatalinaVi);

        // Get all the alCatalinaViList where description does not contain
        defaultAlCatalinaViFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllAlCatalinaVisByTreeDepthIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlCatalinaVi = alCatalinaViRepository.saveAndFlush(alCatalinaVi);

        // Get all the alCatalinaViList where treeDepth equals to
        defaultAlCatalinaViFiltering("treeDepth.equals=" + DEFAULT_TREE_DEPTH, "treeDepth.equals=" + UPDATED_TREE_DEPTH);
    }

    @Test
    @Transactional
    void getAllAlCatalinaVisByTreeDepthIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlCatalinaVi = alCatalinaViRepository.saveAndFlush(alCatalinaVi);

        // Get all the alCatalinaViList where treeDepth in
        defaultAlCatalinaViFiltering("treeDepth.in=" + DEFAULT_TREE_DEPTH + "," + UPDATED_TREE_DEPTH, "treeDepth.in=" + UPDATED_TREE_DEPTH);
    }

    @Test
    @Transactional
    void getAllAlCatalinaVisByTreeDepthIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlCatalinaVi = alCatalinaViRepository.saveAndFlush(alCatalinaVi);

        // Get all the alCatalinaViList where treeDepth is not null
        defaultAlCatalinaViFiltering("treeDepth.specified=true", "treeDepth.specified=false");
    }

    @Test
    @Transactional
    void getAllAlCatalinaVisByTreeDepthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlCatalinaVi = alCatalinaViRepository.saveAndFlush(alCatalinaVi);

        // Get all the alCatalinaViList where treeDepth is greater than or equal to
        defaultAlCatalinaViFiltering(
            "treeDepth.greaterThanOrEqual=" + DEFAULT_TREE_DEPTH,
            "treeDepth.greaterThanOrEqual=" + UPDATED_TREE_DEPTH
        );
    }

    @Test
    @Transactional
    void getAllAlCatalinaVisByTreeDepthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlCatalinaVi = alCatalinaViRepository.saveAndFlush(alCatalinaVi);

        // Get all the alCatalinaViList where treeDepth is less than or equal to
        defaultAlCatalinaViFiltering("treeDepth.lessThanOrEqual=" + DEFAULT_TREE_DEPTH, "treeDepth.lessThanOrEqual=" + SMALLER_TREE_DEPTH);
    }

    @Test
    @Transactional
    void getAllAlCatalinaVisByTreeDepthIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlCatalinaVi = alCatalinaViRepository.saveAndFlush(alCatalinaVi);

        // Get all the alCatalinaViList where treeDepth is less than
        defaultAlCatalinaViFiltering("treeDepth.lessThan=" + UPDATED_TREE_DEPTH, "treeDepth.lessThan=" + DEFAULT_TREE_DEPTH);
    }

    @Test
    @Transactional
    void getAllAlCatalinaVisByTreeDepthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlCatalinaVi = alCatalinaViRepository.saveAndFlush(alCatalinaVi);

        // Get all the alCatalinaViList where treeDepth is greater than
        defaultAlCatalinaViFiltering("treeDepth.greaterThan=" + SMALLER_TREE_DEPTH, "treeDepth.greaterThan=" + DEFAULT_TREE_DEPTH);
    }

    @Test
    @Transactional
    void getAllAlCatalinaVisByParentIsEqualToSomething() throws Exception {
        AlCatalinaVi parent;
        if (TestUtil.findAll(em, AlCatalinaVi.class).isEmpty()) {
            alCatalinaViRepository.saveAndFlush(alCatalinaVi);
            parent = AlCatalinaViResourceIT.createEntity();
        } else {
            parent = TestUtil.findAll(em, AlCatalinaVi.class).get(0);
        }
        em.persist(parent);
        em.flush();
        alCatalinaVi.setParent(parent);
        alCatalinaViRepository.saveAndFlush(alCatalinaVi);
        Long parentId = parent.getId();
        // Get all the alCatalinaViList where parent equals to parentId
        defaultAlCatalinaViShouldBeFound("parentId.equals=" + parentId);

        // Get all the alCatalinaViList where parent equals to (parentId + 1)
        defaultAlCatalinaViShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    @Test
    @Transactional
    void getAllAlCatalinaVisByAvatarIsEqualToSomething() throws Exception {
        Metaverse avatar;
        if (TestUtil.findAll(em, Metaverse.class).isEmpty()) {
            alCatalinaViRepository.saveAndFlush(alCatalinaVi);
            avatar = MetaverseResourceIT.createEntity();
        } else {
            avatar = TestUtil.findAll(em, Metaverse.class).get(0);
        }
        em.persist(avatar);
        em.flush();
        alCatalinaVi.setAvatar(avatar);
        alCatalinaViRepository.saveAndFlush(alCatalinaVi);
        Long avatarId = avatar.getId();
        // Get all the alCatalinaViList where avatar equals to avatarId
        defaultAlCatalinaViShouldBeFound("avatarId.equals=" + avatarId);

        // Get all the alCatalinaViList where avatar equals to (avatarId + 1)
        defaultAlCatalinaViShouldNotBeFound("avatarId.equals=" + (avatarId + 1));
    }

    private void defaultAlCatalinaViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlCatalinaViShouldBeFound(shouldBeFound);
        defaultAlCatalinaViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlCatalinaViShouldBeFound(String filter) throws Exception {
        restAlCatalinaViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alCatalinaVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].treeDepth").value(hasItem(DEFAULT_TREE_DEPTH)));

        // Check, that the count call also returns 1
        restAlCatalinaViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlCatalinaViShouldNotBeFound(String filter) throws Exception {
        restAlCatalinaViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlCatalinaViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlCatalinaVi() throws Exception {
        // Get the alCatalinaVi
        restAlCatalinaViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlCatalinaVi() throws Exception {
        // Initialize the database
        insertedAlCatalinaVi = alCatalinaViRepository.saveAndFlush(alCatalinaVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alCatalinaVi
        AlCatalinaVi updatedAlCatalinaVi = alCatalinaViRepository.findById(alCatalinaVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlCatalinaVi are not directly saved in db
        em.detach(updatedAlCatalinaVi);
        updatedAlCatalinaVi.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).treeDepth(UPDATED_TREE_DEPTH);
        AlCatalinaViDTO alCatalinaViDTO = alCatalinaViMapper.toDto(updatedAlCatalinaVi);

        restAlCatalinaViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alCatalinaViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alCatalinaViDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlCatalinaVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlCatalinaViToMatchAllProperties(updatedAlCatalinaVi);
    }

    @Test
    @Transactional
    void putNonExistingAlCatalinaVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alCatalinaVi.setId(longCount.incrementAndGet());

        // Create the AlCatalinaVi
        AlCatalinaViDTO alCatalinaViDTO = alCatalinaViMapper.toDto(alCatalinaVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlCatalinaViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alCatalinaViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alCatalinaViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlCatalinaVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlCatalinaVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alCatalinaVi.setId(longCount.incrementAndGet());

        // Create the AlCatalinaVi
        AlCatalinaViDTO alCatalinaViDTO = alCatalinaViMapper.toDto(alCatalinaVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlCatalinaViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alCatalinaViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlCatalinaVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlCatalinaVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alCatalinaVi.setId(longCount.incrementAndGet());

        // Create the AlCatalinaVi
        AlCatalinaViDTO alCatalinaViDTO = alCatalinaViMapper.toDto(alCatalinaVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlCatalinaViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alCatalinaViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlCatalinaVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlCatalinaViWithPatch() throws Exception {
        // Initialize the database
        insertedAlCatalinaVi = alCatalinaViRepository.saveAndFlush(alCatalinaVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alCatalinaVi using partial update
        AlCatalinaVi partialUpdatedAlCatalinaVi = new AlCatalinaVi();
        partialUpdatedAlCatalinaVi.setId(alCatalinaVi.getId());

        partialUpdatedAlCatalinaVi.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restAlCatalinaViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlCatalinaVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlCatalinaVi))
            )
            .andExpect(status().isOk());

        // Validate the AlCatalinaVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlCatalinaViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlCatalinaVi, alCatalinaVi),
            getPersistedAlCatalinaVi(alCatalinaVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlCatalinaViWithPatch() throws Exception {
        // Initialize the database
        insertedAlCatalinaVi = alCatalinaViRepository.saveAndFlush(alCatalinaVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alCatalinaVi using partial update
        AlCatalinaVi partialUpdatedAlCatalinaVi = new AlCatalinaVi();
        partialUpdatedAlCatalinaVi.setId(alCatalinaVi.getId());

        partialUpdatedAlCatalinaVi.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).treeDepth(UPDATED_TREE_DEPTH);

        restAlCatalinaViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlCatalinaVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlCatalinaVi))
            )
            .andExpect(status().isOk());

        // Validate the AlCatalinaVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlCatalinaViUpdatableFieldsEquals(partialUpdatedAlCatalinaVi, getPersistedAlCatalinaVi(partialUpdatedAlCatalinaVi));
    }

    @Test
    @Transactional
    void patchNonExistingAlCatalinaVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alCatalinaVi.setId(longCount.incrementAndGet());

        // Create the AlCatalinaVi
        AlCatalinaViDTO alCatalinaViDTO = alCatalinaViMapper.toDto(alCatalinaVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlCatalinaViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alCatalinaViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alCatalinaViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlCatalinaVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlCatalinaVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alCatalinaVi.setId(longCount.incrementAndGet());

        // Create the AlCatalinaVi
        AlCatalinaViDTO alCatalinaViDTO = alCatalinaViMapper.toDto(alCatalinaVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlCatalinaViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alCatalinaViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlCatalinaVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlCatalinaVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alCatalinaVi.setId(longCount.incrementAndGet());

        // Create the AlCatalinaVi
        AlCatalinaViDTO alCatalinaViDTO = alCatalinaViMapper.toDto(alCatalinaVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlCatalinaViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alCatalinaViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlCatalinaVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlCatalinaVi() throws Exception {
        // Initialize the database
        insertedAlCatalinaVi = alCatalinaViRepository.saveAndFlush(alCatalinaVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alCatalinaVi
        restAlCatalinaViMockMvc
            .perform(delete(ENTITY_API_URL_ID, alCatalinaVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alCatalinaViRepository.count();
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

    protected AlCatalinaVi getPersistedAlCatalinaVi(AlCatalinaVi alCatalinaVi) {
        return alCatalinaViRepository.findById(alCatalinaVi.getId()).orElseThrow();
    }

    protected void assertPersistedAlCatalinaViToMatchAllProperties(AlCatalinaVi expectedAlCatalinaVi) {
        assertAlCatalinaViAllPropertiesEquals(expectedAlCatalinaVi, getPersistedAlCatalinaVi(expectedAlCatalinaVi));
    }

    protected void assertPersistedAlCatalinaViToMatchUpdatableProperties(AlCatalinaVi expectedAlCatalinaVi) {
        assertAlCatalinaViAllUpdatablePropertiesEquals(expectedAlCatalinaVi, getPersistedAlCatalinaVi(expectedAlCatalinaVi));
    }
}
