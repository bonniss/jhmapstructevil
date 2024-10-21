package ai.realworld.web.rest;

import static ai.realworld.domain.RihannaViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.RihannaVi;
import ai.realworld.repository.RihannaViRepository;
import ai.realworld.service.dto.RihannaViDTO;
import ai.realworld.service.mapper.RihannaViMapper;
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
 * Integration tests for the {@link RihannaViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RihannaViResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PERMISSION_GRID_JASON = "AAAAAAAAAA";
    private static final String UPDATED_PERMISSION_GRID_JASON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/rihanna-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RihannaViRepository rihannaViRepository;

    @Autowired
    private RihannaViMapper rihannaViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRihannaViMockMvc;

    private RihannaVi rihannaVi;

    private RihannaVi insertedRihannaVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RihannaVi createEntity() {
        return new RihannaVi().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).permissionGridJason(DEFAULT_PERMISSION_GRID_JASON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RihannaVi createUpdatedEntity() {
        return new RihannaVi().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).permissionGridJason(UPDATED_PERMISSION_GRID_JASON);
    }

    @BeforeEach
    public void initTest() {
        rihannaVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedRihannaVi != null) {
            rihannaViRepository.delete(insertedRihannaVi);
            insertedRihannaVi = null;
        }
    }

    @Test
    @Transactional
    void createRihannaVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RihannaVi
        RihannaViDTO rihannaViDTO = rihannaViMapper.toDto(rihannaVi);
        var returnedRihannaViDTO = om.readValue(
            restRihannaViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rihannaViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RihannaViDTO.class
        );

        // Validate the RihannaVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRihannaVi = rihannaViMapper.toEntity(returnedRihannaViDTO);
        assertRihannaViUpdatableFieldsEquals(returnedRihannaVi, getPersistedRihannaVi(returnedRihannaVi));

        insertedRihannaVi = returnedRihannaVi;
    }

    @Test
    @Transactional
    void createRihannaViWithExistingId() throws Exception {
        // Create the RihannaVi with an existing ID
        rihannaVi.setId(1L);
        RihannaViDTO rihannaViDTO = rihannaViMapper.toDto(rihannaVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRihannaViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rihannaViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RihannaVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rihannaVi.setName(null);

        // Create the RihannaVi, which fails.
        RihannaViDTO rihannaViDTO = rihannaViMapper.toDto(rihannaVi);

        restRihannaViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rihannaViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRihannaVis() throws Exception {
        // Initialize the database
        insertedRihannaVi = rihannaViRepository.saveAndFlush(rihannaVi);

        // Get all the rihannaViList
        restRihannaViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rihannaVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].permissionGridJason").value(hasItem(DEFAULT_PERMISSION_GRID_JASON)));
    }

    @Test
    @Transactional
    void getRihannaVi() throws Exception {
        // Initialize the database
        insertedRihannaVi = rihannaViRepository.saveAndFlush(rihannaVi);

        // Get the rihannaVi
        restRihannaViMockMvc
            .perform(get(ENTITY_API_URL_ID, rihannaVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rihannaVi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.permissionGridJason").value(DEFAULT_PERMISSION_GRID_JASON));
    }

    @Test
    @Transactional
    void getRihannaVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedRihannaVi = rihannaViRepository.saveAndFlush(rihannaVi);

        Long id = rihannaVi.getId();

        defaultRihannaViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultRihannaViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultRihannaViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRihannaVisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRihannaVi = rihannaViRepository.saveAndFlush(rihannaVi);

        // Get all the rihannaViList where name equals to
        defaultRihannaViFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRihannaVisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRihannaVi = rihannaViRepository.saveAndFlush(rihannaVi);

        // Get all the rihannaViList where name in
        defaultRihannaViFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRihannaVisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRihannaVi = rihannaViRepository.saveAndFlush(rihannaVi);

        // Get all the rihannaViList where name is not null
        defaultRihannaViFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllRihannaVisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedRihannaVi = rihannaViRepository.saveAndFlush(rihannaVi);

        // Get all the rihannaViList where name contains
        defaultRihannaViFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRihannaVisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRihannaVi = rihannaViRepository.saveAndFlush(rihannaVi);

        // Get all the rihannaViList where name does not contain
        defaultRihannaViFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllRihannaVisByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRihannaVi = rihannaViRepository.saveAndFlush(rihannaVi);

        // Get all the rihannaViList where description equals to
        defaultRihannaViFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRihannaVisByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRihannaVi = rihannaViRepository.saveAndFlush(rihannaVi);

        // Get all the rihannaViList where description in
        defaultRihannaViFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllRihannaVisByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRihannaVi = rihannaViRepository.saveAndFlush(rihannaVi);

        // Get all the rihannaViList where description is not null
        defaultRihannaViFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllRihannaVisByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedRihannaVi = rihannaViRepository.saveAndFlush(rihannaVi);

        // Get all the rihannaViList where description contains
        defaultRihannaViFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRihannaVisByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRihannaVi = rihannaViRepository.saveAndFlush(rihannaVi);

        // Get all the rihannaViList where description does not contain
        defaultRihannaViFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRihannaVisByPermissionGridJasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRihannaVi = rihannaViRepository.saveAndFlush(rihannaVi);

        // Get all the rihannaViList where permissionGridJason equals to
        defaultRihannaViFiltering(
            "permissionGridJason.equals=" + DEFAULT_PERMISSION_GRID_JASON,
            "permissionGridJason.equals=" + UPDATED_PERMISSION_GRID_JASON
        );
    }

    @Test
    @Transactional
    void getAllRihannaVisByPermissionGridJasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRihannaVi = rihannaViRepository.saveAndFlush(rihannaVi);

        // Get all the rihannaViList where permissionGridJason in
        defaultRihannaViFiltering(
            "permissionGridJason.in=" + DEFAULT_PERMISSION_GRID_JASON + "," + UPDATED_PERMISSION_GRID_JASON,
            "permissionGridJason.in=" + UPDATED_PERMISSION_GRID_JASON
        );
    }

    @Test
    @Transactional
    void getAllRihannaVisByPermissionGridJasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRihannaVi = rihannaViRepository.saveAndFlush(rihannaVi);

        // Get all the rihannaViList where permissionGridJason is not null
        defaultRihannaViFiltering("permissionGridJason.specified=true", "permissionGridJason.specified=false");
    }

    @Test
    @Transactional
    void getAllRihannaVisByPermissionGridJasonContainsSomething() throws Exception {
        // Initialize the database
        insertedRihannaVi = rihannaViRepository.saveAndFlush(rihannaVi);

        // Get all the rihannaViList where permissionGridJason contains
        defaultRihannaViFiltering(
            "permissionGridJason.contains=" + DEFAULT_PERMISSION_GRID_JASON,
            "permissionGridJason.contains=" + UPDATED_PERMISSION_GRID_JASON
        );
    }

    @Test
    @Transactional
    void getAllRihannaVisByPermissionGridJasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRihannaVi = rihannaViRepository.saveAndFlush(rihannaVi);

        // Get all the rihannaViList where permissionGridJason does not contain
        defaultRihannaViFiltering(
            "permissionGridJason.doesNotContain=" + UPDATED_PERMISSION_GRID_JASON,
            "permissionGridJason.doesNotContain=" + DEFAULT_PERMISSION_GRID_JASON
        );
    }

    @Test
    @Transactional
    void getAllRihannaVisByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            rihannaViRepository.saveAndFlush(rihannaVi);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        rihannaVi.setApplication(application);
        rihannaViRepository.saveAndFlush(rihannaVi);
        UUID applicationId = application.getId();
        // Get all the rihannaViList where application equals to applicationId
        defaultRihannaViShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the rihannaViList where application equals to UUID.randomUUID()
        defaultRihannaViShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultRihannaViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultRihannaViShouldBeFound(shouldBeFound);
        defaultRihannaViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRihannaViShouldBeFound(String filter) throws Exception {
        restRihannaViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rihannaVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].permissionGridJason").value(hasItem(DEFAULT_PERMISSION_GRID_JASON)));

        // Check, that the count call also returns 1
        restRihannaViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRihannaViShouldNotBeFound(String filter) throws Exception {
        restRihannaViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRihannaViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRihannaVi() throws Exception {
        // Get the rihannaVi
        restRihannaViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRihannaVi() throws Exception {
        // Initialize the database
        insertedRihannaVi = rihannaViRepository.saveAndFlush(rihannaVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rihannaVi
        RihannaVi updatedRihannaVi = rihannaViRepository.findById(rihannaVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRihannaVi are not directly saved in db
        em.detach(updatedRihannaVi);
        updatedRihannaVi.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).permissionGridJason(UPDATED_PERMISSION_GRID_JASON);
        RihannaViDTO rihannaViDTO = rihannaViMapper.toDto(updatedRihannaVi);

        restRihannaViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rihannaViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rihannaViDTO))
            )
            .andExpect(status().isOk());

        // Validate the RihannaVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRihannaViToMatchAllProperties(updatedRihannaVi);
    }

    @Test
    @Transactional
    void putNonExistingRihannaVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rihannaVi.setId(longCount.incrementAndGet());

        // Create the RihannaVi
        RihannaViDTO rihannaViDTO = rihannaViMapper.toDto(rihannaVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRihannaViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rihannaViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rihannaViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RihannaVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRihannaVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rihannaVi.setId(longCount.incrementAndGet());

        // Create the RihannaVi
        RihannaViDTO rihannaViDTO = rihannaViMapper.toDto(rihannaVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRihannaViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rihannaViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RihannaVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRihannaVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rihannaVi.setId(longCount.incrementAndGet());

        // Create the RihannaVi
        RihannaViDTO rihannaViDTO = rihannaViMapper.toDto(rihannaVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRihannaViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rihannaViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RihannaVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRihannaViWithPatch() throws Exception {
        // Initialize the database
        insertedRihannaVi = rihannaViRepository.saveAndFlush(rihannaVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rihannaVi using partial update
        RihannaVi partialUpdatedRihannaVi = new RihannaVi();
        partialUpdatedRihannaVi.setId(rihannaVi.getId());

        partialUpdatedRihannaVi.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restRihannaViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRihannaVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRihannaVi))
            )
            .andExpect(status().isOk());

        // Validate the RihannaVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRihannaViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRihannaVi, rihannaVi),
            getPersistedRihannaVi(rihannaVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateRihannaViWithPatch() throws Exception {
        // Initialize the database
        insertedRihannaVi = rihannaViRepository.saveAndFlush(rihannaVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rihannaVi using partial update
        RihannaVi partialUpdatedRihannaVi = new RihannaVi();
        partialUpdatedRihannaVi.setId(rihannaVi.getId());

        partialUpdatedRihannaVi.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).permissionGridJason(UPDATED_PERMISSION_GRID_JASON);

        restRihannaViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRihannaVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRihannaVi))
            )
            .andExpect(status().isOk());

        // Validate the RihannaVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRihannaViUpdatableFieldsEquals(partialUpdatedRihannaVi, getPersistedRihannaVi(partialUpdatedRihannaVi));
    }

    @Test
    @Transactional
    void patchNonExistingRihannaVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rihannaVi.setId(longCount.incrementAndGet());

        // Create the RihannaVi
        RihannaViDTO rihannaViDTO = rihannaViMapper.toDto(rihannaVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRihannaViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rihannaViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rihannaViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RihannaVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRihannaVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rihannaVi.setId(longCount.incrementAndGet());

        // Create the RihannaVi
        RihannaViDTO rihannaViDTO = rihannaViMapper.toDto(rihannaVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRihannaViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rihannaViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RihannaVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRihannaVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rihannaVi.setId(longCount.incrementAndGet());

        // Create the RihannaVi
        RihannaViDTO rihannaViDTO = rihannaViMapper.toDto(rihannaVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRihannaViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(rihannaViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RihannaVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRihannaVi() throws Exception {
        // Initialize the database
        insertedRihannaVi = rihannaViRepository.saveAndFlush(rihannaVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the rihannaVi
        restRihannaViMockMvc
            .perform(delete(ENTITY_API_URL_ID, rihannaVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return rihannaViRepository.count();
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

    protected RihannaVi getPersistedRihannaVi(RihannaVi rihannaVi) {
        return rihannaViRepository.findById(rihannaVi.getId()).orElseThrow();
    }

    protected void assertPersistedRihannaViToMatchAllProperties(RihannaVi expectedRihannaVi) {
        assertRihannaViAllPropertiesEquals(expectedRihannaVi, getPersistedRihannaVi(expectedRihannaVi));
    }

    protected void assertPersistedRihannaViToMatchUpdatableProperties(RihannaVi expectedRihannaVi) {
        assertRihannaViAllUpdatablePropertiesEquals(expectedRihannaVi, getPersistedRihannaVi(expectedRihannaVi));
    }
}
