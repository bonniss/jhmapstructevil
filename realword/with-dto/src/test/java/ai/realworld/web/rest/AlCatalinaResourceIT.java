package ai.realworld.web.rest;

import static ai.realworld.domain.AlCatalinaAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlCatalina;
import ai.realworld.domain.AlCatalina;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.repository.AlCatalinaRepository;
import ai.realworld.service.dto.AlCatalinaDTO;
import ai.realworld.service.mapper.AlCatalinaMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.UUID;
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
 * Integration tests for the {@link AlCatalinaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlCatalinaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_TREE_DEPTH = 1;
    private static final Integer UPDATED_TREE_DEPTH = 2;
    private static final Integer SMALLER_TREE_DEPTH = 1 - 1;

    private static final String ENTITY_API_URL = "/api/al-catalinas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlCatalinaRepository alCatalinaRepository;

    @Autowired
    private AlCatalinaMapper alCatalinaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlCatalinaMockMvc;

    private AlCatalina alCatalina;

    private AlCatalina insertedAlCatalina;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlCatalina createEntity() {
        return new AlCatalina().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).treeDepth(DEFAULT_TREE_DEPTH);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlCatalina createUpdatedEntity() {
        return new AlCatalina().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).treeDepth(UPDATED_TREE_DEPTH);
    }

    @BeforeEach
    public void initTest() {
        alCatalina = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlCatalina != null) {
            alCatalinaRepository.delete(insertedAlCatalina);
            insertedAlCatalina = null;
        }
    }

    @Test
    @Transactional
    void createAlCatalina() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlCatalina
        AlCatalinaDTO alCatalinaDTO = alCatalinaMapper.toDto(alCatalina);
        var returnedAlCatalinaDTO = om.readValue(
            restAlCatalinaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alCatalinaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlCatalinaDTO.class
        );

        // Validate the AlCatalina in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlCatalina = alCatalinaMapper.toEntity(returnedAlCatalinaDTO);
        assertAlCatalinaUpdatableFieldsEquals(returnedAlCatalina, getPersistedAlCatalina(returnedAlCatalina));

        insertedAlCatalina = returnedAlCatalina;
    }

    @Test
    @Transactional
    void createAlCatalinaWithExistingId() throws Exception {
        // Create the AlCatalina with an existing ID
        alCatalina.setId(1L);
        AlCatalinaDTO alCatalinaDTO = alCatalinaMapper.toDto(alCatalina);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlCatalinaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alCatalinaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlCatalina in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alCatalina.setName(null);

        // Create the AlCatalina, which fails.
        AlCatalinaDTO alCatalinaDTO = alCatalinaMapper.toDto(alCatalina);

        restAlCatalinaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alCatalinaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlCatalinas() throws Exception {
        // Initialize the database
        insertedAlCatalina = alCatalinaRepository.saveAndFlush(alCatalina);

        // Get all the alCatalinaList
        restAlCatalinaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alCatalina.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].treeDepth").value(hasItem(DEFAULT_TREE_DEPTH)));
    }

    @Test
    @Transactional
    void getAlCatalina() throws Exception {
        // Initialize the database
        insertedAlCatalina = alCatalinaRepository.saveAndFlush(alCatalina);

        // Get the alCatalina
        restAlCatalinaMockMvc
            .perform(get(ENTITY_API_URL_ID, alCatalina.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alCatalina.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.treeDepth").value(DEFAULT_TREE_DEPTH));
    }

    @Test
    @Transactional
    void getAlCatalinasByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlCatalina = alCatalinaRepository.saveAndFlush(alCatalina);

        Long id = alCatalina.getId();

        defaultAlCatalinaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlCatalinaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlCatalinaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlCatalinasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlCatalina = alCatalinaRepository.saveAndFlush(alCatalina);

        // Get all the alCatalinaList where name equals to
        defaultAlCatalinaFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlCatalinasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlCatalina = alCatalinaRepository.saveAndFlush(alCatalina);

        // Get all the alCatalinaList where name in
        defaultAlCatalinaFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlCatalinasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlCatalina = alCatalinaRepository.saveAndFlush(alCatalina);

        // Get all the alCatalinaList where name is not null
        defaultAlCatalinaFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAlCatalinasByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlCatalina = alCatalinaRepository.saveAndFlush(alCatalina);

        // Get all the alCatalinaList where name contains
        defaultAlCatalinaFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlCatalinasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlCatalina = alCatalinaRepository.saveAndFlush(alCatalina);

        // Get all the alCatalinaList where name does not contain
        defaultAlCatalinaFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAlCatalinasByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlCatalina = alCatalinaRepository.saveAndFlush(alCatalina);

        // Get all the alCatalinaList where description equals to
        defaultAlCatalinaFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAlCatalinasByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlCatalina = alCatalinaRepository.saveAndFlush(alCatalina);

        // Get all the alCatalinaList where description in
        defaultAlCatalinaFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllAlCatalinasByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlCatalina = alCatalinaRepository.saveAndFlush(alCatalina);

        // Get all the alCatalinaList where description is not null
        defaultAlCatalinaFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllAlCatalinasByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedAlCatalina = alCatalinaRepository.saveAndFlush(alCatalina);

        // Get all the alCatalinaList where description contains
        defaultAlCatalinaFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAlCatalinasByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlCatalina = alCatalinaRepository.saveAndFlush(alCatalina);

        // Get all the alCatalinaList where description does not contain
        defaultAlCatalinaFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllAlCatalinasByTreeDepthIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlCatalina = alCatalinaRepository.saveAndFlush(alCatalina);

        // Get all the alCatalinaList where treeDepth equals to
        defaultAlCatalinaFiltering("treeDepth.equals=" + DEFAULT_TREE_DEPTH, "treeDepth.equals=" + UPDATED_TREE_DEPTH);
    }

    @Test
    @Transactional
    void getAllAlCatalinasByTreeDepthIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlCatalina = alCatalinaRepository.saveAndFlush(alCatalina);

        // Get all the alCatalinaList where treeDepth in
        defaultAlCatalinaFiltering("treeDepth.in=" + DEFAULT_TREE_DEPTH + "," + UPDATED_TREE_DEPTH, "treeDepth.in=" + UPDATED_TREE_DEPTH);
    }

    @Test
    @Transactional
    void getAllAlCatalinasByTreeDepthIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlCatalina = alCatalinaRepository.saveAndFlush(alCatalina);

        // Get all the alCatalinaList where treeDepth is not null
        defaultAlCatalinaFiltering("treeDepth.specified=true", "treeDepth.specified=false");
    }

    @Test
    @Transactional
    void getAllAlCatalinasByTreeDepthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlCatalina = alCatalinaRepository.saveAndFlush(alCatalina);

        // Get all the alCatalinaList where treeDepth is greater than or equal to
        defaultAlCatalinaFiltering(
            "treeDepth.greaterThanOrEqual=" + DEFAULT_TREE_DEPTH,
            "treeDepth.greaterThanOrEqual=" + UPDATED_TREE_DEPTH
        );
    }

    @Test
    @Transactional
    void getAllAlCatalinasByTreeDepthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlCatalina = alCatalinaRepository.saveAndFlush(alCatalina);

        // Get all the alCatalinaList where treeDepth is less than or equal to
        defaultAlCatalinaFiltering("treeDepth.lessThanOrEqual=" + DEFAULT_TREE_DEPTH, "treeDepth.lessThanOrEqual=" + SMALLER_TREE_DEPTH);
    }

    @Test
    @Transactional
    void getAllAlCatalinasByTreeDepthIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlCatalina = alCatalinaRepository.saveAndFlush(alCatalina);

        // Get all the alCatalinaList where treeDepth is less than
        defaultAlCatalinaFiltering("treeDepth.lessThan=" + UPDATED_TREE_DEPTH, "treeDepth.lessThan=" + DEFAULT_TREE_DEPTH);
    }

    @Test
    @Transactional
    void getAllAlCatalinasByTreeDepthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlCatalina = alCatalinaRepository.saveAndFlush(alCatalina);

        // Get all the alCatalinaList where treeDepth is greater than
        defaultAlCatalinaFiltering("treeDepth.greaterThan=" + SMALLER_TREE_DEPTH, "treeDepth.greaterThan=" + DEFAULT_TREE_DEPTH);
    }

    @Test
    @Transactional
    void getAllAlCatalinasByParentIsEqualToSomething() throws Exception {
        AlCatalina parent;
        if (TestUtil.findAll(em, AlCatalina.class).isEmpty()) {
            alCatalinaRepository.saveAndFlush(alCatalina);
            parent = AlCatalinaResourceIT.createEntity();
        } else {
            parent = TestUtil.findAll(em, AlCatalina.class).get(0);
        }
        em.persist(parent);
        em.flush();
        alCatalina.setParent(parent);
        alCatalinaRepository.saveAndFlush(alCatalina);
        Long parentId = parent.getId();
        // Get all the alCatalinaList where parent equals to parentId
        defaultAlCatalinaShouldBeFound("parentId.equals=" + parentId);

        // Get all the alCatalinaList where parent equals to (parentId + 1)
        defaultAlCatalinaShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    @Test
    @Transactional
    void getAllAlCatalinasByAvatarIsEqualToSomething() throws Exception {
        Metaverse avatar;
        if (TestUtil.findAll(em, Metaverse.class).isEmpty()) {
            alCatalinaRepository.saveAndFlush(alCatalina);
            avatar = MetaverseResourceIT.createEntity();
        } else {
            avatar = TestUtil.findAll(em, Metaverse.class).get(0);
        }
        em.persist(avatar);
        em.flush();
        alCatalina.setAvatar(avatar);
        alCatalinaRepository.saveAndFlush(alCatalina);
        Long avatarId = avatar.getId();
        // Get all the alCatalinaList where avatar equals to avatarId
        defaultAlCatalinaShouldBeFound("avatarId.equals=" + avatarId);

        // Get all the alCatalinaList where avatar equals to (avatarId + 1)
        defaultAlCatalinaShouldNotBeFound("avatarId.equals=" + (avatarId + 1));
    }

    @Test
    @Transactional
    void getAllAlCatalinasByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alCatalinaRepository.saveAndFlush(alCatalina);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alCatalina.setApplication(application);
        alCatalinaRepository.saveAndFlush(alCatalina);
        UUID applicationId = application.getId();
        // Get all the alCatalinaList where application equals to applicationId
        defaultAlCatalinaShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alCatalinaList where application equals to UUID.randomUUID()
        defaultAlCatalinaShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlCatalinaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlCatalinaShouldBeFound(shouldBeFound);
        defaultAlCatalinaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlCatalinaShouldBeFound(String filter) throws Exception {
        restAlCatalinaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alCatalina.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].treeDepth").value(hasItem(DEFAULT_TREE_DEPTH)));

        // Check, that the count call also returns 1
        restAlCatalinaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlCatalinaShouldNotBeFound(String filter) throws Exception {
        restAlCatalinaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlCatalinaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlCatalina() throws Exception {
        // Get the alCatalina
        restAlCatalinaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlCatalina() throws Exception {
        // Initialize the database
        insertedAlCatalina = alCatalinaRepository.saveAndFlush(alCatalina);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alCatalina
        AlCatalina updatedAlCatalina = alCatalinaRepository.findById(alCatalina.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlCatalina are not directly saved in db
        em.detach(updatedAlCatalina);
        updatedAlCatalina.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).treeDepth(UPDATED_TREE_DEPTH);
        AlCatalinaDTO alCatalinaDTO = alCatalinaMapper.toDto(updatedAlCatalina);

        restAlCatalinaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alCatalinaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alCatalinaDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlCatalina in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlCatalinaToMatchAllProperties(updatedAlCatalina);
    }

    @Test
    @Transactional
    void putNonExistingAlCatalina() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alCatalina.setId(longCount.incrementAndGet());

        // Create the AlCatalina
        AlCatalinaDTO alCatalinaDTO = alCatalinaMapper.toDto(alCatalina);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlCatalinaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alCatalinaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alCatalinaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlCatalina in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlCatalina() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alCatalina.setId(longCount.incrementAndGet());

        // Create the AlCatalina
        AlCatalinaDTO alCatalinaDTO = alCatalinaMapper.toDto(alCatalina);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlCatalinaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alCatalinaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlCatalina in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlCatalina() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alCatalina.setId(longCount.incrementAndGet());

        // Create the AlCatalina
        AlCatalinaDTO alCatalinaDTO = alCatalinaMapper.toDto(alCatalina);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlCatalinaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alCatalinaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlCatalina in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlCatalinaWithPatch() throws Exception {
        // Initialize the database
        insertedAlCatalina = alCatalinaRepository.saveAndFlush(alCatalina);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alCatalina using partial update
        AlCatalina partialUpdatedAlCatalina = new AlCatalina();
        partialUpdatedAlCatalina.setId(alCatalina.getId());

        partialUpdatedAlCatalina.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restAlCatalinaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlCatalina.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlCatalina))
            )
            .andExpect(status().isOk());

        // Validate the AlCatalina in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlCatalinaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlCatalina, alCatalina),
            getPersistedAlCatalina(alCatalina)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlCatalinaWithPatch() throws Exception {
        // Initialize the database
        insertedAlCatalina = alCatalinaRepository.saveAndFlush(alCatalina);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alCatalina using partial update
        AlCatalina partialUpdatedAlCatalina = new AlCatalina();
        partialUpdatedAlCatalina.setId(alCatalina.getId());

        partialUpdatedAlCatalina.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).treeDepth(UPDATED_TREE_DEPTH);

        restAlCatalinaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlCatalina.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlCatalina))
            )
            .andExpect(status().isOk());

        // Validate the AlCatalina in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlCatalinaUpdatableFieldsEquals(partialUpdatedAlCatalina, getPersistedAlCatalina(partialUpdatedAlCatalina));
    }

    @Test
    @Transactional
    void patchNonExistingAlCatalina() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alCatalina.setId(longCount.incrementAndGet());

        // Create the AlCatalina
        AlCatalinaDTO alCatalinaDTO = alCatalinaMapper.toDto(alCatalina);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlCatalinaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alCatalinaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alCatalinaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlCatalina in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlCatalina() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alCatalina.setId(longCount.incrementAndGet());

        // Create the AlCatalina
        AlCatalinaDTO alCatalinaDTO = alCatalinaMapper.toDto(alCatalina);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlCatalinaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alCatalinaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlCatalina in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlCatalina() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alCatalina.setId(longCount.incrementAndGet());

        // Create the AlCatalina
        AlCatalinaDTO alCatalinaDTO = alCatalinaMapper.toDto(alCatalina);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlCatalinaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alCatalinaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlCatalina in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlCatalina() throws Exception {
        // Initialize the database
        insertedAlCatalina = alCatalinaRepository.saveAndFlush(alCatalina);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alCatalina
        restAlCatalinaMockMvc
            .perform(delete(ENTITY_API_URL_ID, alCatalina.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alCatalinaRepository.count();
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

    protected AlCatalina getPersistedAlCatalina(AlCatalina alCatalina) {
        return alCatalinaRepository.findById(alCatalina.getId()).orElseThrow();
    }

    protected void assertPersistedAlCatalinaToMatchAllProperties(AlCatalina expectedAlCatalina) {
        assertAlCatalinaAllPropertiesEquals(expectedAlCatalina, getPersistedAlCatalina(expectedAlCatalina));
    }

    protected void assertPersistedAlCatalinaToMatchUpdatableProperties(AlCatalina expectedAlCatalina) {
        assertAlCatalinaAllUpdatablePropertiesEquals(expectedAlCatalina, getPersistedAlCatalina(expectedAlCatalina));
    }
}
