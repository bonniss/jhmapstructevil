package ai.realworld.web.rest;

import static ai.realworld.domain.AlLadyGagaAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlLadyGaga;
import ai.realworld.domain.AndreiRightHand;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.repository.AlLadyGagaRepository;
import ai.realworld.service.dto.AlLadyGagaDTO;
import ai.realworld.service.mapper.AlLadyGagaMapper;
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
 * Integration tests for the {@link AlLadyGagaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlLadyGagaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION_HEITIGA = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_HEITIGA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/al-lady-gagas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlLadyGagaRepository alLadyGagaRepository;

    @Autowired
    private AlLadyGagaMapper alLadyGagaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlLadyGagaMockMvc;

    private AlLadyGaga alLadyGaga;

    private AlLadyGaga insertedAlLadyGaga;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlLadyGaga createEntity() {
        return new AlLadyGaga().name(DEFAULT_NAME).descriptionHeitiga(DEFAULT_DESCRIPTION_HEITIGA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlLadyGaga createUpdatedEntity() {
        return new AlLadyGaga().name(UPDATED_NAME).descriptionHeitiga(UPDATED_DESCRIPTION_HEITIGA);
    }

    @BeforeEach
    public void initTest() {
        alLadyGaga = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlLadyGaga != null) {
            alLadyGagaRepository.delete(insertedAlLadyGaga);
            insertedAlLadyGaga = null;
        }
    }

    @Test
    @Transactional
    void createAlLadyGaga() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlLadyGaga
        AlLadyGagaDTO alLadyGagaDTO = alLadyGagaMapper.toDto(alLadyGaga);
        var returnedAlLadyGagaDTO = om.readValue(
            restAlLadyGagaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alLadyGagaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlLadyGagaDTO.class
        );

        // Validate the AlLadyGaga in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlLadyGaga = alLadyGagaMapper.toEntity(returnedAlLadyGagaDTO);
        assertAlLadyGagaUpdatableFieldsEquals(returnedAlLadyGaga, getPersistedAlLadyGaga(returnedAlLadyGaga));

        insertedAlLadyGaga = returnedAlLadyGaga;
    }

    @Test
    @Transactional
    void createAlLadyGagaWithExistingId() throws Exception {
        // Create the AlLadyGaga with an existing ID
        insertedAlLadyGaga = alLadyGagaRepository.saveAndFlush(alLadyGaga);
        AlLadyGagaDTO alLadyGagaDTO = alLadyGagaMapper.toDto(alLadyGaga);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlLadyGagaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alLadyGagaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlLadyGaga in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alLadyGaga.setName(null);

        // Create the AlLadyGaga, which fails.
        AlLadyGagaDTO alLadyGagaDTO = alLadyGagaMapper.toDto(alLadyGaga);

        restAlLadyGagaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alLadyGagaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlLadyGagas() throws Exception {
        // Initialize the database
        insertedAlLadyGaga = alLadyGagaRepository.saveAndFlush(alLadyGaga);

        // Get all the alLadyGagaList
        restAlLadyGagaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alLadyGaga.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].descriptionHeitiga").value(hasItem(DEFAULT_DESCRIPTION_HEITIGA)));
    }

    @Test
    @Transactional
    void getAlLadyGaga() throws Exception {
        // Initialize the database
        insertedAlLadyGaga = alLadyGagaRepository.saveAndFlush(alLadyGaga);

        // Get the alLadyGaga
        restAlLadyGagaMockMvc
            .perform(get(ENTITY_API_URL_ID, alLadyGaga.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alLadyGaga.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.descriptionHeitiga").value(DEFAULT_DESCRIPTION_HEITIGA));
    }

    @Test
    @Transactional
    void getAlLadyGagasByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlLadyGaga = alLadyGagaRepository.saveAndFlush(alLadyGaga);

        UUID id = alLadyGaga.getId();

        defaultAlLadyGagaFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllAlLadyGagasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLadyGaga = alLadyGagaRepository.saveAndFlush(alLadyGaga);

        // Get all the alLadyGagaList where name equals to
        defaultAlLadyGagaFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlLadyGagasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlLadyGaga = alLadyGagaRepository.saveAndFlush(alLadyGaga);

        // Get all the alLadyGagaList where name in
        defaultAlLadyGagaFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlLadyGagasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlLadyGaga = alLadyGagaRepository.saveAndFlush(alLadyGaga);

        // Get all the alLadyGagaList where name is not null
        defaultAlLadyGagaFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAlLadyGagasByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlLadyGaga = alLadyGagaRepository.saveAndFlush(alLadyGaga);

        // Get all the alLadyGagaList where name contains
        defaultAlLadyGagaFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlLadyGagasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlLadyGaga = alLadyGagaRepository.saveAndFlush(alLadyGaga);

        // Get all the alLadyGagaList where name does not contain
        defaultAlLadyGagaFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAlLadyGagasByDescriptionHeitigaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlLadyGaga = alLadyGagaRepository.saveAndFlush(alLadyGaga);

        // Get all the alLadyGagaList where descriptionHeitiga equals to
        defaultAlLadyGagaFiltering(
            "descriptionHeitiga.equals=" + DEFAULT_DESCRIPTION_HEITIGA,
            "descriptionHeitiga.equals=" + UPDATED_DESCRIPTION_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlLadyGagasByDescriptionHeitigaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlLadyGaga = alLadyGagaRepository.saveAndFlush(alLadyGaga);

        // Get all the alLadyGagaList where descriptionHeitiga in
        defaultAlLadyGagaFiltering(
            "descriptionHeitiga.in=" + DEFAULT_DESCRIPTION_HEITIGA + "," + UPDATED_DESCRIPTION_HEITIGA,
            "descriptionHeitiga.in=" + UPDATED_DESCRIPTION_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlLadyGagasByDescriptionHeitigaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlLadyGaga = alLadyGagaRepository.saveAndFlush(alLadyGaga);

        // Get all the alLadyGagaList where descriptionHeitiga is not null
        defaultAlLadyGagaFiltering("descriptionHeitiga.specified=true", "descriptionHeitiga.specified=false");
    }

    @Test
    @Transactional
    void getAllAlLadyGagasByDescriptionHeitigaContainsSomething() throws Exception {
        // Initialize the database
        insertedAlLadyGaga = alLadyGagaRepository.saveAndFlush(alLadyGaga);

        // Get all the alLadyGagaList where descriptionHeitiga contains
        defaultAlLadyGagaFiltering(
            "descriptionHeitiga.contains=" + DEFAULT_DESCRIPTION_HEITIGA,
            "descriptionHeitiga.contains=" + UPDATED_DESCRIPTION_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlLadyGagasByDescriptionHeitigaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlLadyGaga = alLadyGagaRepository.saveAndFlush(alLadyGaga);

        // Get all the alLadyGagaList where descriptionHeitiga does not contain
        defaultAlLadyGagaFiltering(
            "descriptionHeitiga.doesNotContain=" + UPDATED_DESCRIPTION_HEITIGA,
            "descriptionHeitiga.doesNotContain=" + DEFAULT_DESCRIPTION_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlLadyGagasByAddressIsEqualToSomething() throws Exception {
        AndreiRightHand address;
        if (TestUtil.findAll(em, AndreiRightHand.class).isEmpty()) {
            alLadyGagaRepository.saveAndFlush(alLadyGaga);
            address = AndreiRightHandResourceIT.createEntity();
        } else {
            address = TestUtil.findAll(em, AndreiRightHand.class).get(0);
        }
        em.persist(address);
        em.flush();
        alLadyGaga.setAddress(address);
        alLadyGagaRepository.saveAndFlush(alLadyGaga);
        Long addressId = address.getId();
        // Get all the alLadyGagaList where address equals to addressId
        defaultAlLadyGagaShouldBeFound("addressId.equals=" + addressId);

        // Get all the alLadyGagaList where address equals to (addressId + 1)
        defaultAlLadyGagaShouldNotBeFound("addressId.equals=" + (addressId + 1));
    }

    @Test
    @Transactional
    void getAllAlLadyGagasByAvatarIsEqualToSomething() throws Exception {
        Metaverse avatar;
        if (TestUtil.findAll(em, Metaverse.class).isEmpty()) {
            alLadyGagaRepository.saveAndFlush(alLadyGaga);
            avatar = MetaverseResourceIT.createEntity();
        } else {
            avatar = TestUtil.findAll(em, Metaverse.class).get(0);
        }
        em.persist(avatar);
        em.flush();
        alLadyGaga.setAvatar(avatar);
        alLadyGagaRepository.saveAndFlush(alLadyGaga);
        Long avatarId = avatar.getId();
        // Get all the alLadyGagaList where avatar equals to avatarId
        defaultAlLadyGagaShouldBeFound("avatarId.equals=" + avatarId);

        // Get all the alLadyGagaList where avatar equals to (avatarId + 1)
        defaultAlLadyGagaShouldNotBeFound("avatarId.equals=" + (avatarId + 1));
    }

    @Test
    @Transactional
    void getAllAlLadyGagasByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alLadyGagaRepository.saveAndFlush(alLadyGaga);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alLadyGaga.setApplication(application);
        alLadyGagaRepository.saveAndFlush(alLadyGaga);
        UUID applicationId = application.getId();
        // Get all the alLadyGagaList where application equals to applicationId
        defaultAlLadyGagaShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alLadyGagaList where application equals to UUID.randomUUID()
        defaultAlLadyGagaShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlLadyGagaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlLadyGagaShouldBeFound(shouldBeFound);
        defaultAlLadyGagaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlLadyGagaShouldBeFound(String filter) throws Exception {
        restAlLadyGagaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alLadyGaga.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].descriptionHeitiga").value(hasItem(DEFAULT_DESCRIPTION_HEITIGA)));

        // Check, that the count call also returns 1
        restAlLadyGagaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlLadyGagaShouldNotBeFound(String filter) throws Exception {
        restAlLadyGagaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlLadyGagaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlLadyGaga() throws Exception {
        // Get the alLadyGaga
        restAlLadyGagaMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlLadyGaga() throws Exception {
        // Initialize the database
        insertedAlLadyGaga = alLadyGagaRepository.saveAndFlush(alLadyGaga);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alLadyGaga
        AlLadyGaga updatedAlLadyGaga = alLadyGagaRepository.findById(alLadyGaga.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlLadyGaga are not directly saved in db
        em.detach(updatedAlLadyGaga);
        updatedAlLadyGaga.name(UPDATED_NAME).descriptionHeitiga(UPDATED_DESCRIPTION_HEITIGA);
        AlLadyGagaDTO alLadyGagaDTO = alLadyGagaMapper.toDto(updatedAlLadyGaga);

        restAlLadyGagaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alLadyGagaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alLadyGagaDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlLadyGaga in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlLadyGagaToMatchAllProperties(updatedAlLadyGaga);
    }

    @Test
    @Transactional
    void putNonExistingAlLadyGaga() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLadyGaga.setId(UUID.randomUUID());

        // Create the AlLadyGaga
        AlLadyGagaDTO alLadyGagaDTO = alLadyGagaMapper.toDto(alLadyGaga);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlLadyGagaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alLadyGagaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alLadyGagaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlLadyGaga in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlLadyGaga() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLadyGaga.setId(UUID.randomUUID());

        // Create the AlLadyGaga
        AlLadyGagaDTO alLadyGagaDTO = alLadyGagaMapper.toDto(alLadyGaga);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlLadyGagaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alLadyGagaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlLadyGaga in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlLadyGaga() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLadyGaga.setId(UUID.randomUUID());

        // Create the AlLadyGaga
        AlLadyGagaDTO alLadyGagaDTO = alLadyGagaMapper.toDto(alLadyGaga);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlLadyGagaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alLadyGagaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlLadyGaga in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlLadyGagaWithPatch() throws Exception {
        // Initialize the database
        insertedAlLadyGaga = alLadyGagaRepository.saveAndFlush(alLadyGaga);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alLadyGaga using partial update
        AlLadyGaga partialUpdatedAlLadyGaga = new AlLadyGaga();
        partialUpdatedAlLadyGaga.setId(alLadyGaga.getId());

        partialUpdatedAlLadyGaga.name(UPDATED_NAME);

        restAlLadyGagaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlLadyGaga.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlLadyGaga))
            )
            .andExpect(status().isOk());

        // Validate the AlLadyGaga in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlLadyGagaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlLadyGaga, alLadyGaga),
            getPersistedAlLadyGaga(alLadyGaga)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlLadyGagaWithPatch() throws Exception {
        // Initialize the database
        insertedAlLadyGaga = alLadyGagaRepository.saveAndFlush(alLadyGaga);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alLadyGaga using partial update
        AlLadyGaga partialUpdatedAlLadyGaga = new AlLadyGaga();
        partialUpdatedAlLadyGaga.setId(alLadyGaga.getId());

        partialUpdatedAlLadyGaga.name(UPDATED_NAME).descriptionHeitiga(UPDATED_DESCRIPTION_HEITIGA);

        restAlLadyGagaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlLadyGaga.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlLadyGaga))
            )
            .andExpect(status().isOk());

        // Validate the AlLadyGaga in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlLadyGagaUpdatableFieldsEquals(partialUpdatedAlLadyGaga, getPersistedAlLadyGaga(partialUpdatedAlLadyGaga));
    }

    @Test
    @Transactional
    void patchNonExistingAlLadyGaga() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLadyGaga.setId(UUID.randomUUID());

        // Create the AlLadyGaga
        AlLadyGagaDTO alLadyGagaDTO = alLadyGagaMapper.toDto(alLadyGaga);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlLadyGagaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alLadyGagaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alLadyGagaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlLadyGaga in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlLadyGaga() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLadyGaga.setId(UUID.randomUUID());

        // Create the AlLadyGaga
        AlLadyGagaDTO alLadyGagaDTO = alLadyGagaMapper.toDto(alLadyGaga);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlLadyGagaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alLadyGagaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlLadyGaga in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlLadyGaga() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alLadyGaga.setId(UUID.randomUUID());

        // Create the AlLadyGaga
        AlLadyGagaDTO alLadyGagaDTO = alLadyGagaMapper.toDto(alLadyGaga);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlLadyGagaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alLadyGagaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlLadyGaga in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlLadyGaga() throws Exception {
        // Initialize the database
        insertedAlLadyGaga = alLadyGagaRepository.saveAndFlush(alLadyGaga);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alLadyGaga
        restAlLadyGagaMockMvc
            .perform(delete(ENTITY_API_URL_ID, alLadyGaga.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alLadyGagaRepository.count();
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

    protected AlLadyGaga getPersistedAlLadyGaga(AlLadyGaga alLadyGaga) {
        return alLadyGagaRepository.findById(alLadyGaga.getId()).orElseThrow();
    }

    protected void assertPersistedAlLadyGagaToMatchAllProperties(AlLadyGaga expectedAlLadyGaga) {
        assertAlLadyGagaAllPropertiesEquals(expectedAlLadyGaga, getPersistedAlLadyGaga(expectedAlLadyGaga));
    }

    protected void assertPersistedAlLadyGagaToMatchUpdatableProperties(AlLadyGaga expectedAlLadyGaga) {
        assertAlLadyGagaAllUpdatablePropertiesEquals(expectedAlLadyGaga, getPersistedAlLadyGaga(expectedAlLadyGaga));
    }
}
