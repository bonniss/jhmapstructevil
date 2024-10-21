package ai.realworld.web.rest;

import static ai.realworld.domain.AlLadyGagaViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlLadyGagaVi;
import ai.realworld.domain.AndreiRightHandVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.repository.AlLadyGagaViRepository;
import ai.realworld.service.dto.AlLadyGagaViDTO;
import ai.realworld.service.mapper.AlLadyGagaViMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.UUID;
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
 * Integration tests for the {@link AlLadyGagaViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlLadyGagaViResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION_HEITIGA = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_HEITIGA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/al-lady-gaga-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlLadyGagaViRepository alLadyGagaViRepository;

    @Autowired
    private AlLadyGagaViMapper alLadyGagaViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlLadyGagaViMockMvc;

    private AlLadyGagaVi alLadyGagaVi;

    private AlLadyGagaVi insertedAlLadyGagaVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlLadyGagaVi createEntity() {
        return new AlLadyGagaVi().name(DEFAULT_NAME).descriptionHeitiga(DEFAULT_DESCRIPTION_HEITIGA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlLadyGagaVi createUpdatedEntity() {
        return new AlLadyGagaVi().name(UPDATED_NAME).descriptionHeitiga(UPDATED_DESCRIPTION_HEITIGA);
    }

    @BeforeEach
    public void initTest() {
        alLadyGagaVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlLadyGagaVi != null) {
            alLadyGagaViRepository.delete(insertedAlLadyGagaVi);
            insertedAlLadyGagaVi = null;
        }
    }

    @Test
    @Transactional
    void createAlLadyGagaVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlLadyGagaVi
        AlLadyGagaViDTO alLadyGagaViDTO = alLadyGagaViMapper.toDto(alLadyGagaVi);
        var returnedAlLadyGagaViDTO = om.readValue(
            restAlLadyGagaViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alLadyGagaViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlLadyGagaViDTO.class
        );

        // Validate the AlLadyGagaVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlLadyGagaVi = alLadyGagaViMapper.toEntity(returnedAlLadyGagaViDTO);
        assertAlLadyGagaViUpdatableFieldsEquals(returnedAlLadyGagaVi, getPersistedAlLadyGagaVi(returnedAlLadyGagaVi));

        insertedAlLadyGagaVi = returnedAlLadyGagaVi;
    }

    @Test
    @Transactional
    void createAlLadyGagaViWithExistingId() throws Exception {
        // Create the AlLadyGagaVi with an existing ID
        insertedAlLadyGagaVi = alLadyGagaViRepository.saveAndFlush(alLadyGagaVi);
        AlLadyGagaViDTO alLadyGagaViDTO = alLadyGagaViMapper.toDto(alLadyGagaVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlLadyGagaViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alLadyGagaViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlLadyGagaVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alLadyGagaVi.setName(null);

        // Create the AlLadyGagaVi, which fails.
        AlLadyGagaViDTO alLadyGagaViDTO = alLadyGagaViMapper.toDto(alLadyGagaVi);

        restAlLadyGagaViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alLadyGagaViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlLadyGagaVis() throws Exception {
        // Initialize the database
        insertedAlLadyGagaVi = alLadyGagaViRepository.saveAndFlush(alLadyGagaVi);

        // Get all the alLadyGagaViList
        restAlLadyGagaViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alLadyGagaVi.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].descriptionHeitiga").value(hasItem(DEFAULT_DESCRIPTION_HEITIGA)));
    }

    @Test
    @Transactional
    void getAlLadyGagaVi() throws Exception {
        // Initialize the database
        insertedAlLadyGagaVi = alLadyGagaViRepository.saveAndFlush(alLadyGagaVi);

        // Get the alLadyGagaVi
        restAlLadyGagaViMockMvc
            .perform(get(ENTITY_API_URL_ID, alLadyGagaVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alLadyGagaVi.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.descriptionHeitiga").value(DEFAULT_DESCRIPTION_HEITIGA));
    }

    @Test
    @Transactional
    void getAlLadyGagaVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlLadyGagaVi = alLadyGagaViRepository.saveAndFlush(alLadyGagaVi);

        UUID id = alLadyGagaVi.getId();

        defaultAlLadyGagaViFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllAlLadyGagaVisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLadyGagaVi = alLadyGagaViRepository.saveAndFlush(alLadyGagaVi);

        // Get all the alLadyGagaViList where name equals to
        defaultAlLadyGagaViFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlLadyGagaVisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlLadyGagaVi = alLadyGagaViRepository.saveAndFlush(alLadyGagaVi);

        // Get all the alLadyGagaViList where name in
        defaultAlLadyGagaViFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlLadyGagaVisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlLadyGagaVi = alLadyGagaViRepository.saveAndFlush(alLadyGagaVi);

        // Get all the alLadyGagaViList where name is not null
        defaultAlLadyGagaViFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAlLadyGagaVisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlLadyGagaVi = alLadyGagaViRepository.saveAndFlush(alLadyGagaVi);

        // Get all the alLadyGagaViList where name contains
        defaultAlLadyGagaViFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlLadyGagaVisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlLadyGagaVi = alLadyGagaViRepository.saveAndFlush(alLadyGagaVi);

        // Get all the alLadyGagaViList where name does not contain
        defaultAlLadyGagaViFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAlLadyGagaVisByDescriptionHeitigaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLadyGagaVi = alLadyGagaViRepository.saveAndFlush(alLadyGagaVi);

        // Get all the alLadyGagaViList where descriptionHeitiga equals to
        defaultAlLadyGagaViFiltering(
            "descriptionHeitiga.equals=" + DEFAULT_DESCRIPTION_HEITIGA,
            "descriptionHeitiga.equals=" + UPDATED_DESCRIPTION_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlLadyGagaVisByDescriptionHeitigaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlLadyGagaVi = alLadyGagaViRepository.saveAndFlush(alLadyGagaVi);

        // Get all the alLadyGagaViList where descriptionHeitiga in
        defaultAlLadyGagaViFiltering(
            "descriptionHeitiga.in=" + DEFAULT_DESCRIPTION_HEITIGA + "," + UPDATED_DESCRIPTION_HEITIGA,
            "descriptionHeitiga.in=" + UPDATED_DESCRIPTION_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlLadyGagaVisByDescriptionHeitigaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlLadyGagaVi = alLadyGagaViRepository.saveAndFlush(alLadyGagaVi);

        // Get all the alLadyGagaViList where descriptionHeitiga is not null
        defaultAlLadyGagaViFiltering("descriptionHeitiga.specified=true", "descriptionHeitiga.specified=false");
    }

    @Test
    @Transactional
    void getAllAlLadyGagaVisByDescriptionHeitigaContainsSomething() throws Exception {
        // Initialize the database
        insertedAlLadyGagaVi = alLadyGagaViRepository.saveAndFlush(alLadyGagaVi);

        // Get all the alLadyGagaViList where descriptionHeitiga contains
        defaultAlLadyGagaViFiltering(
            "descriptionHeitiga.contains=" + DEFAULT_DESCRIPTION_HEITIGA,
            "descriptionHeitiga.contains=" + UPDATED_DESCRIPTION_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlLadyGagaVisByDescriptionHeitigaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlLadyGagaVi = alLadyGagaViRepository.saveAndFlush(alLadyGagaVi);

        // Get all the alLadyGagaViList where descriptionHeitiga does not contain
        defaultAlLadyGagaViFiltering(
            "descriptionHeitiga.doesNotContain=" + UPDATED_DESCRIPTION_HEITIGA,
            "descriptionHeitiga.doesNotContain=" + DEFAULT_DESCRIPTION_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlLadyGagaVisByAddressIsEqualToSomething() throws Exception {
        AndreiRightHandVi address;
        if (TestUtil.findAll(em, AndreiRightHandVi.class).isEmpty()) {
            alLadyGagaViRepository.saveAndFlush(alLadyGagaVi);
            address = AndreiRightHandViResourceIT.createEntity();
        } else {
            address = TestUtil.findAll(em, AndreiRightHandVi.class).get(0);
        }
        em.persist(address);
        em.flush();
        alLadyGagaVi.setAddress(address);
        alLadyGagaViRepository.saveAndFlush(alLadyGagaVi);
        Long addressId = address.getId();
        // Get all the alLadyGagaViList where address equals to addressId
        defaultAlLadyGagaViShouldBeFound("addressId.equals=" + addressId);

        // Get all the alLadyGagaViList where address equals to (addressId + 1)
        defaultAlLadyGagaViShouldNotBeFound("addressId.equals=" + (addressId + 1));
    }

    @Test
    @Transactional
    void getAllAlLadyGagaVisByAvatarIsEqualToSomething() throws Exception {
        Metaverse avatar;
        if (TestUtil.findAll(em, Metaverse.class).isEmpty()) {
            alLadyGagaViRepository.saveAndFlush(alLadyGagaVi);
            avatar = MetaverseResourceIT.createEntity();
        } else {
            avatar = TestUtil.findAll(em, Metaverse.class).get(0);
        }
        em.persist(avatar);
        em.flush();
        alLadyGagaVi.setAvatar(avatar);
        alLadyGagaViRepository.saveAndFlush(alLadyGagaVi);
        Long avatarId = avatar.getId();
        // Get all the alLadyGagaViList where avatar equals to avatarId
        defaultAlLadyGagaViShouldBeFound("avatarId.equals=" + avatarId);

        // Get all the alLadyGagaViList where avatar equals to (avatarId + 1)
        defaultAlLadyGagaViShouldNotBeFound("avatarId.equals=" + (avatarId + 1));
    }

    @Test
    @Transactional
    void getAllAlLadyGagaVisByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alLadyGagaViRepository.saveAndFlush(alLadyGagaVi);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alLadyGagaVi.setApplication(application);
        alLadyGagaViRepository.saveAndFlush(alLadyGagaVi);
        UUID applicationId = application.getId();
        // Get all the alLadyGagaViList where application equals to applicationId
        defaultAlLadyGagaViShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alLadyGagaViList where application equals to UUID.randomUUID()
        defaultAlLadyGagaViShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlLadyGagaViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlLadyGagaViShouldBeFound(shouldBeFound);
        defaultAlLadyGagaViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlLadyGagaViShouldBeFound(String filter) throws Exception {
        restAlLadyGagaViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alLadyGagaVi.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].descriptionHeitiga").value(hasItem(DEFAULT_DESCRIPTION_HEITIGA)));

        // Check, that the count call also returns 1
        restAlLadyGagaViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlLadyGagaViShouldNotBeFound(String filter) throws Exception {
        restAlLadyGagaViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlLadyGagaViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlLadyGagaVi() throws Exception {
        // Get the alLadyGagaVi
        restAlLadyGagaViMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlLadyGagaVi() throws Exception {
        // Initialize the database
        insertedAlLadyGagaVi = alLadyGagaViRepository.saveAndFlush(alLadyGagaVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alLadyGagaVi
        AlLadyGagaVi updatedAlLadyGagaVi = alLadyGagaViRepository.findById(alLadyGagaVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlLadyGagaVi are not directly saved in db
        em.detach(updatedAlLadyGagaVi);
        updatedAlLadyGagaVi.name(UPDATED_NAME).descriptionHeitiga(UPDATED_DESCRIPTION_HEITIGA);
        AlLadyGagaViDTO alLadyGagaViDTO = alLadyGagaViMapper.toDto(updatedAlLadyGagaVi);

        restAlLadyGagaViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alLadyGagaViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alLadyGagaViDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlLadyGagaVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlLadyGagaViToMatchAllProperties(updatedAlLadyGagaVi);
    }

    @Test
    @Transactional
    void putNonExistingAlLadyGagaVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLadyGagaVi.setId(UUID.randomUUID());

        // Create the AlLadyGagaVi
        AlLadyGagaViDTO alLadyGagaViDTO = alLadyGagaViMapper.toDto(alLadyGagaVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlLadyGagaViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alLadyGagaViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alLadyGagaViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlLadyGagaVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlLadyGagaVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLadyGagaVi.setId(UUID.randomUUID());

        // Create the AlLadyGagaVi
        AlLadyGagaViDTO alLadyGagaViDTO = alLadyGagaViMapper.toDto(alLadyGagaVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlLadyGagaViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alLadyGagaViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlLadyGagaVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlLadyGagaVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLadyGagaVi.setId(UUID.randomUUID());

        // Create the AlLadyGagaVi
        AlLadyGagaViDTO alLadyGagaViDTO = alLadyGagaViMapper.toDto(alLadyGagaVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlLadyGagaViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alLadyGagaViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlLadyGagaVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlLadyGagaViWithPatch() throws Exception {
        // Initialize the database
        insertedAlLadyGagaVi = alLadyGagaViRepository.saveAndFlush(alLadyGagaVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alLadyGagaVi using partial update
        AlLadyGagaVi partialUpdatedAlLadyGagaVi = new AlLadyGagaVi();
        partialUpdatedAlLadyGagaVi.setId(alLadyGagaVi.getId());

        partialUpdatedAlLadyGagaVi.name(UPDATED_NAME).descriptionHeitiga(UPDATED_DESCRIPTION_HEITIGA);

        restAlLadyGagaViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlLadyGagaVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlLadyGagaVi))
            )
            .andExpect(status().isOk());

        // Validate the AlLadyGagaVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlLadyGagaViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlLadyGagaVi, alLadyGagaVi),
            getPersistedAlLadyGagaVi(alLadyGagaVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlLadyGagaViWithPatch() throws Exception {
        // Initialize the database
        insertedAlLadyGagaVi = alLadyGagaViRepository.saveAndFlush(alLadyGagaVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alLadyGagaVi using partial update
        AlLadyGagaVi partialUpdatedAlLadyGagaVi = new AlLadyGagaVi();
        partialUpdatedAlLadyGagaVi.setId(alLadyGagaVi.getId());

        partialUpdatedAlLadyGagaVi.name(UPDATED_NAME).descriptionHeitiga(UPDATED_DESCRIPTION_HEITIGA);

        restAlLadyGagaViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlLadyGagaVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlLadyGagaVi))
            )
            .andExpect(status().isOk());

        // Validate the AlLadyGagaVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlLadyGagaViUpdatableFieldsEquals(partialUpdatedAlLadyGagaVi, getPersistedAlLadyGagaVi(partialUpdatedAlLadyGagaVi));
    }

    @Test
    @Transactional
    void patchNonExistingAlLadyGagaVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLadyGagaVi.setId(UUID.randomUUID());

        // Create the AlLadyGagaVi
        AlLadyGagaViDTO alLadyGagaViDTO = alLadyGagaViMapper.toDto(alLadyGagaVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlLadyGagaViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alLadyGagaViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alLadyGagaViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlLadyGagaVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlLadyGagaVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLadyGagaVi.setId(UUID.randomUUID());

        // Create the AlLadyGagaVi
        AlLadyGagaViDTO alLadyGagaViDTO = alLadyGagaViMapper.toDto(alLadyGagaVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlLadyGagaViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alLadyGagaViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlLadyGagaVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlLadyGagaVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLadyGagaVi.setId(UUID.randomUUID());

        // Create the AlLadyGagaVi
        AlLadyGagaViDTO alLadyGagaViDTO = alLadyGagaViMapper.toDto(alLadyGagaVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlLadyGagaViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alLadyGagaViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlLadyGagaVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlLadyGagaVi() throws Exception {
        // Initialize the database
        insertedAlLadyGagaVi = alLadyGagaViRepository.saveAndFlush(alLadyGagaVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alLadyGagaVi
        restAlLadyGagaViMockMvc
            .perform(delete(ENTITY_API_URL_ID, alLadyGagaVi.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alLadyGagaViRepository.count();
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

    protected AlLadyGagaVi getPersistedAlLadyGagaVi(AlLadyGagaVi alLadyGagaVi) {
        return alLadyGagaViRepository.findById(alLadyGagaVi.getId()).orElseThrow();
    }

    protected void assertPersistedAlLadyGagaViToMatchAllProperties(AlLadyGagaVi expectedAlLadyGagaVi) {
        assertAlLadyGagaViAllPropertiesEquals(expectedAlLadyGagaVi, getPersistedAlLadyGagaVi(expectedAlLadyGagaVi));
    }

    protected void assertPersistedAlLadyGagaViToMatchUpdatableProperties(AlLadyGagaVi expectedAlLadyGagaVi) {
        assertAlLadyGagaViAllUpdatablePropertiesEquals(expectedAlLadyGagaVi, getPersistedAlLadyGagaVi(expectedAlLadyGagaVi));
    }
}
