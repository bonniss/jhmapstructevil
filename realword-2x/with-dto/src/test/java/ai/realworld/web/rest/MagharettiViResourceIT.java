package ai.realworld.web.rest;

import static ai.realworld.domain.MagharettiViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.MagharettiVi;
import ai.realworld.domain.enumeration.MissisipiType;
import ai.realworld.repository.MagharettiViRepository;
import ai.realworld.service.dto.MagharettiViDTO;
import ai.realworld.service.mapper.MagharettiViMapper;
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
 * Integration tests for the {@link MagharettiViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MagharettiViResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final MissisipiType DEFAULT_TYPE = MissisipiType.CONTAINER;
    private static final MissisipiType UPDATED_TYPE = MissisipiType.WEIGHT;

    private static final String ENTITY_API_URL = "/api/magharetti-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MagharettiViRepository magharettiViRepository;

    @Autowired
    private MagharettiViMapper magharettiViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMagharettiViMockMvc;

    private MagharettiVi magharettiVi;

    private MagharettiVi insertedMagharettiVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MagharettiVi createEntity() {
        return new MagharettiVi().name(DEFAULT_NAME).label(DEFAULT_LABEL).type(DEFAULT_TYPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MagharettiVi createUpdatedEntity() {
        return new MagharettiVi().name(UPDATED_NAME).label(UPDATED_LABEL).type(UPDATED_TYPE);
    }

    @BeforeEach
    public void initTest() {
        magharettiVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMagharettiVi != null) {
            magharettiViRepository.delete(insertedMagharettiVi);
            insertedMagharettiVi = null;
        }
    }

    @Test
    @Transactional
    void createMagharettiVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MagharettiVi
        MagharettiViDTO magharettiViDTO = magharettiViMapper.toDto(magharettiVi);
        var returnedMagharettiViDTO = om.readValue(
            restMagharettiViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(magharettiViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MagharettiViDTO.class
        );

        // Validate the MagharettiVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMagharettiVi = magharettiViMapper.toEntity(returnedMagharettiViDTO);
        assertMagharettiViUpdatableFieldsEquals(returnedMagharettiVi, getPersistedMagharettiVi(returnedMagharettiVi));

        insertedMagharettiVi = returnedMagharettiVi;
    }

    @Test
    @Transactional
    void createMagharettiViWithExistingId() throws Exception {
        // Create the MagharettiVi with an existing ID
        magharettiVi.setId(1L);
        MagharettiViDTO magharettiViDTO = magharettiViMapper.toDto(magharettiVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMagharettiViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(magharettiViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MagharettiVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        magharettiVi.setName(null);

        // Create the MagharettiVi, which fails.
        MagharettiViDTO magharettiViDTO = magharettiViMapper.toDto(magharettiVi);

        restMagharettiViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(magharettiViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLabelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        magharettiVi.setLabel(null);

        // Create the MagharettiVi, which fails.
        MagharettiViDTO magharettiViDTO = magharettiViMapper.toDto(magharettiVi);

        restMagharettiViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(magharettiViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMagharettiVis() throws Exception {
        // Initialize the database
        insertedMagharettiVi = magharettiViRepository.saveAndFlush(magharettiVi);

        // Get all the magharettiViList
        restMagharettiViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(magharettiVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    void getMagharettiVi() throws Exception {
        // Initialize the database
        insertedMagharettiVi = magharettiViRepository.saveAndFlush(magharettiVi);

        // Get the magharettiVi
        restMagharettiViMockMvc
            .perform(get(ENTITY_API_URL_ID, magharettiVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(magharettiVi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getMagharettiVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedMagharettiVi = magharettiViRepository.saveAndFlush(magharettiVi);

        Long id = magharettiVi.getId();

        defaultMagharettiViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMagharettiViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMagharettiViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMagharettiVisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMagharettiVi = magharettiViRepository.saveAndFlush(magharettiVi);

        // Get all the magharettiViList where name equals to
        defaultMagharettiViFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMagharettiVisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMagharettiVi = magharettiViRepository.saveAndFlush(magharettiVi);

        // Get all the magharettiViList where name in
        defaultMagharettiViFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMagharettiVisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMagharettiVi = magharettiViRepository.saveAndFlush(magharettiVi);

        // Get all the magharettiViList where name is not null
        defaultMagharettiViFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllMagharettiVisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedMagharettiVi = magharettiViRepository.saveAndFlush(magharettiVi);

        // Get all the magharettiViList where name contains
        defaultMagharettiViFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMagharettiVisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMagharettiVi = magharettiViRepository.saveAndFlush(magharettiVi);

        // Get all the magharettiViList where name does not contain
        defaultMagharettiViFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllMagharettiVisByLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMagharettiVi = magharettiViRepository.saveAndFlush(magharettiVi);

        // Get all the magharettiViList where label equals to
        defaultMagharettiViFiltering("label.equals=" + DEFAULT_LABEL, "label.equals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllMagharettiVisByLabelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMagharettiVi = magharettiViRepository.saveAndFlush(magharettiVi);

        // Get all the magharettiViList where label in
        defaultMagharettiViFiltering("label.in=" + DEFAULT_LABEL + "," + UPDATED_LABEL, "label.in=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllMagharettiVisByLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMagharettiVi = magharettiViRepository.saveAndFlush(magharettiVi);

        // Get all the magharettiViList where label is not null
        defaultMagharettiViFiltering("label.specified=true", "label.specified=false");
    }

    @Test
    @Transactional
    void getAllMagharettiVisByLabelContainsSomething() throws Exception {
        // Initialize the database
        insertedMagharettiVi = magharettiViRepository.saveAndFlush(magharettiVi);

        // Get all the magharettiViList where label contains
        defaultMagharettiViFiltering("label.contains=" + DEFAULT_LABEL, "label.contains=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllMagharettiVisByLabelNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMagharettiVi = magharettiViRepository.saveAndFlush(magharettiVi);

        // Get all the magharettiViList where label does not contain
        defaultMagharettiViFiltering("label.doesNotContain=" + UPDATED_LABEL, "label.doesNotContain=" + DEFAULT_LABEL);
    }

    @Test
    @Transactional
    void getAllMagharettiVisByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMagharettiVi = magharettiViRepository.saveAndFlush(magharettiVi);

        // Get all the magharettiViList where type equals to
        defaultMagharettiViFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllMagharettiVisByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMagharettiVi = magharettiViRepository.saveAndFlush(magharettiVi);

        // Get all the magharettiViList where type in
        defaultMagharettiViFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllMagharettiVisByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMagharettiVi = magharettiViRepository.saveAndFlush(magharettiVi);

        // Get all the magharettiViList where type is not null
        defaultMagharettiViFiltering("type.specified=true", "type.specified=false");
    }

    private void defaultMagharettiViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMagharettiViShouldBeFound(shouldBeFound);
        defaultMagharettiViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMagharettiViShouldBeFound(String filter) throws Exception {
        restMagharettiViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(magharettiVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));

        // Check, that the count call also returns 1
        restMagharettiViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMagharettiViShouldNotBeFound(String filter) throws Exception {
        restMagharettiViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMagharettiViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMagharettiVi() throws Exception {
        // Get the magharettiVi
        restMagharettiViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMagharettiVi() throws Exception {
        // Initialize the database
        insertedMagharettiVi = magharettiViRepository.saveAndFlush(magharettiVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the magharettiVi
        MagharettiVi updatedMagharettiVi = magharettiViRepository.findById(magharettiVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMagharettiVi are not directly saved in db
        em.detach(updatedMagharettiVi);
        updatedMagharettiVi.name(UPDATED_NAME).label(UPDATED_LABEL).type(UPDATED_TYPE);
        MagharettiViDTO magharettiViDTO = magharettiViMapper.toDto(updatedMagharettiVi);

        restMagharettiViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, magharettiViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(magharettiViDTO))
            )
            .andExpect(status().isOk());

        // Validate the MagharettiVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMagharettiViToMatchAllProperties(updatedMagharettiVi);
    }

    @Test
    @Transactional
    void putNonExistingMagharettiVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        magharettiVi.setId(longCount.incrementAndGet());

        // Create the MagharettiVi
        MagharettiViDTO magharettiViDTO = magharettiViMapper.toDto(magharettiVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMagharettiViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, magharettiViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(magharettiViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MagharettiVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMagharettiVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        magharettiVi.setId(longCount.incrementAndGet());

        // Create the MagharettiVi
        MagharettiViDTO magharettiViDTO = magharettiViMapper.toDto(magharettiVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMagharettiViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(magharettiViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MagharettiVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMagharettiVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        magharettiVi.setId(longCount.incrementAndGet());

        // Create the MagharettiVi
        MagharettiViDTO magharettiViDTO = magharettiViMapper.toDto(magharettiVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMagharettiViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(magharettiViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MagharettiVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMagharettiViWithPatch() throws Exception {
        // Initialize the database
        insertedMagharettiVi = magharettiViRepository.saveAndFlush(magharettiVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the magharettiVi using partial update
        MagharettiVi partialUpdatedMagharettiVi = new MagharettiVi();
        partialUpdatedMagharettiVi.setId(magharettiVi.getId());

        partialUpdatedMagharettiVi.type(UPDATED_TYPE);

        restMagharettiViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMagharettiVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMagharettiVi))
            )
            .andExpect(status().isOk());

        // Validate the MagharettiVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMagharettiViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMagharettiVi, magharettiVi),
            getPersistedMagharettiVi(magharettiVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateMagharettiViWithPatch() throws Exception {
        // Initialize the database
        insertedMagharettiVi = magharettiViRepository.saveAndFlush(magharettiVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the magharettiVi using partial update
        MagharettiVi partialUpdatedMagharettiVi = new MagharettiVi();
        partialUpdatedMagharettiVi.setId(magharettiVi.getId());

        partialUpdatedMagharettiVi.name(UPDATED_NAME).label(UPDATED_LABEL).type(UPDATED_TYPE);

        restMagharettiViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMagharettiVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMagharettiVi))
            )
            .andExpect(status().isOk());

        // Validate the MagharettiVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMagharettiViUpdatableFieldsEquals(partialUpdatedMagharettiVi, getPersistedMagharettiVi(partialUpdatedMagharettiVi));
    }

    @Test
    @Transactional
    void patchNonExistingMagharettiVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        magharettiVi.setId(longCount.incrementAndGet());

        // Create the MagharettiVi
        MagharettiViDTO magharettiViDTO = magharettiViMapper.toDto(magharettiVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMagharettiViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, magharettiViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(magharettiViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MagharettiVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMagharettiVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        magharettiVi.setId(longCount.incrementAndGet());

        // Create the MagharettiVi
        MagharettiViDTO magharettiViDTO = magharettiViMapper.toDto(magharettiVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMagharettiViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(magharettiViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MagharettiVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMagharettiVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        magharettiVi.setId(longCount.incrementAndGet());

        // Create the MagharettiVi
        MagharettiViDTO magharettiViDTO = magharettiViMapper.toDto(magharettiVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMagharettiViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(magharettiViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MagharettiVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMagharettiVi() throws Exception {
        // Initialize the database
        insertedMagharettiVi = magharettiViRepository.saveAndFlush(magharettiVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the magharettiVi
        restMagharettiViMockMvc
            .perform(delete(ENTITY_API_URL_ID, magharettiVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return magharettiViRepository.count();
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

    protected MagharettiVi getPersistedMagharettiVi(MagharettiVi magharettiVi) {
        return magharettiViRepository.findById(magharettiVi.getId()).orElseThrow();
    }

    protected void assertPersistedMagharettiViToMatchAllProperties(MagharettiVi expectedMagharettiVi) {
        assertMagharettiViAllPropertiesEquals(expectedMagharettiVi, getPersistedMagharettiVi(expectedMagharettiVi));
    }

    protected void assertPersistedMagharettiViToMatchUpdatableProperties(MagharettiVi expectedMagharettiVi) {
        assertMagharettiViAllUpdatablePropertiesEquals(expectedMagharettiVi, getPersistedMagharettiVi(expectedMagharettiVi));
    }
}
