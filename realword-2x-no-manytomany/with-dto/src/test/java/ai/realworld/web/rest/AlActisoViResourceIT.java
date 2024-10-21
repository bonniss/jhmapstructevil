package ai.realworld.web.rest;

import static ai.realworld.domain.AlActisoViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlActisoVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.repository.AlActisoViRepository;
import ai.realworld.service.dto.AlActisoViDTO;
import ai.realworld.service.mapper.AlActisoViMapper;
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
 * Integration tests for the {@link AlActisoViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlActisoViResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE_JASON = "AAAAAAAAAA";
    private static final String UPDATED_VALUE_JASON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/al-actiso-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlActisoViRepository alActisoViRepository;

    @Autowired
    private AlActisoViMapper alActisoViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlActisoViMockMvc;

    private AlActisoVi alActisoVi;

    private AlActisoVi insertedAlActisoVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlActisoVi createEntity() {
        return new AlActisoVi().key(DEFAULT_KEY).valueJason(DEFAULT_VALUE_JASON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlActisoVi createUpdatedEntity() {
        return new AlActisoVi().key(UPDATED_KEY).valueJason(UPDATED_VALUE_JASON);
    }

    @BeforeEach
    public void initTest() {
        alActisoVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlActisoVi != null) {
            alActisoViRepository.delete(insertedAlActisoVi);
            insertedAlActisoVi = null;
        }
    }

    @Test
    @Transactional
    void createAlActisoVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlActisoVi
        AlActisoViDTO alActisoViDTO = alActisoViMapper.toDto(alActisoVi);
        var returnedAlActisoViDTO = om.readValue(
            restAlActisoViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alActisoViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlActisoViDTO.class
        );

        // Validate the AlActisoVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlActisoVi = alActisoViMapper.toEntity(returnedAlActisoViDTO);
        assertAlActisoViUpdatableFieldsEquals(returnedAlActisoVi, getPersistedAlActisoVi(returnedAlActisoVi));

        insertedAlActisoVi = returnedAlActisoVi;
    }

    @Test
    @Transactional
    void createAlActisoViWithExistingId() throws Exception {
        // Create the AlActisoVi with an existing ID
        alActisoVi.setId(1L);
        AlActisoViDTO alActisoViDTO = alActisoViMapper.toDto(alActisoVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlActisoViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alActisoViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlActisoVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkKeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alActisoVi.setKey(null);

        // Create the AlActisoVi, which fails.
        AlActisoViDTO alActisoViDTO = alActisoViMapper.toDto(alActisoVi);

        restAlActisoViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alActisoViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlActisoVis() throws Exception {
        // Initialize the database
        insertedAlActisoVi = alActisoViRepository.saveAndFlush(alActisoVi);

        // Get all the alActisoViList
        restAlActisoViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alActisoVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].valueJason").value(hasItem(DEFAULT_VALUE_JASON)));
    }

    @Test
    @Transactional
    void getAlActisoVi() throws Exception {
        // Initialize the database
        insertedAlActisoVi = alActisoViRepository.saveAndFlush(alActisoVi);

        // Get the alActisoVi
        restAlActisoViMockMvc
            .perform(get(ENTITY_API_URL_ID, alActisoVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alActisoVi.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.valueJason").value(DEFAULT_VALUE_JASON));
    }

    @Test
    @Transactional
    void getAlActisoVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlActisoVi = alActisoViRepository.saveAndFlush(alActisoVi);

        Long id = alActisoVi.getId();

        defaultAlActisoViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlActisoViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlActisoViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlActisoVisByKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlActisoVi = alActisoViRepository.saveAndFlush(alActisoVi);

        // Get all the alActisoViList where key equals to
        defaultAlActisoViFiltering("key.equals=" + DEFAULT_KEY, "key.equals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllAlActisoVisByKeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlActisoVi = alActisoViRepository.saveAndFlush(alActisoVi);

        // Get all the alActisoViList where key in
        defaultAlActisoViFiltering("key.in=" + DEFAULT_KEY + "," + UPDATED_KEY, "key.in=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllAlActisoVisByKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlActisoVi = alActisoViRepository.saveAndFlush(alActisoVi);

        // Get all the alActisoViList where key is not null
        defaultAlActisoViFiltering("key.specified=true", "key.specified=false");
    }

    @Test
    @Transactional
    void getAllAlActisoVisByKeyContainsSomething() throws Exception {
        // Initialize the database
        insertedAlActisoVi = alActisoViRepository.saveAndFlush(alActisoVi);

        // Get all the alActisoViList where key contains
        defaultAlActisoViFiltering("key.contains=" + DEFAULT_KEY, "key.contains=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllAlActisoVisByKeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlActisoVi = alActisoViRepository.saveAndFlush(alActisoVi);

        // Get all the alActisoViList where key does not contain
        defaultAlActisoViFiltering("key.doesNotContain=" + UPDATED_KEY, "key.doesNotContain=" + DEFAULT_KEY);
    }

    @Test
    @Transactional
    void getAllAlActisoVisByValueJasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlActisoVi = alActisoViRepository.saveAndFlush(alActisoVi);

        // Get all the alActisoViList where valueJason equals to
        defaultAlActisoViFiltering("valueJason.equals=" + DEFAULT_VALUE_JASON, "valueJason.equals=" + UPDATED_VALUE_JASON);
    }

    @Test
    @Transactional
    void getAllAlActisoVisByValueJasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlActisoVi = alActisoViRepository.saveAndFlush(alActisoVi);

        // Get all the alActisoViList where valueJason in
        defaultAlActisoViFiltering(
            "valueJason.in=" + DEFAULT_VALUE_JASON + "," + UPDATED_VALUE_JASON,
            "valueJason.in=" + UPDATED_VALUE_JASON
        );
    }

    @Test
    @Transactional
    void getAllAlActisoVisByValueJasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlActisoVi = alActisoViRepository.saveAndFlush(alActisoVi);

        // Get all the alActisoViList where valueJason is not null
        defaultAlActisoViFiltering("valueJason.specified=true", "valueJason.specified=false");
    }

    @Test
    @Transactional
    void getAllAlActisoVisByValueJasonContainsSomething() throws Exception {
        // Initialize the database
        insertedAlActisoVi = alActisoViRepository.saveAndFlush(alActisoVi);

        // Get all the alActisoViList where valueJason contains
        defaultAlActisoViFiltering("valueJason.contains=" + DEFAULT_VALUE_JASON, "valueJason.contains=" + UPDATED_VALUE_JASON);
    }

    @Test
    @Transactional
    void getAllAlActisoVisByValueJasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlActisoVi = alActisoViRepository.saveAndFlush(alActisoVi);

        // Get all the alActisoViList where valueJason does not contain
        defaultAlActisoViFiltering("valueJason.doesNotContain=" + UPDATED_VALUE_JASON, "valueJason.doesNotContain=" + DEFAULT_VALUE_JASON);
    }

    @Test
    @Transactional
    void getAllAlActisoVisByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alActisoViRepository.saveAndFlush(alActisoVi);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alActisoVi.setApplication(application);
        alActisoViRepository.saveAndFlush(alActisoVi);
        UUID applicationId = application.getId();
        // Get all the alActisoViList where application equals to applicationId
        defaultAlActisoViShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alActisoViList where application equals to UUID.randomUUID()
        defaultAlActisoViShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlActisoViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlActisoViShouldBeFound(shouldBeFound);
        defaultAlActisoViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlActisoViShouldBeFound(String filter) throws Exception {
        restAlActisoViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alActisoVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].valueJason").value(hasItem(DEFAULT_VALUE_JASON)));

        // Check, that the count call also returns 1
        restAlActisoViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlActisoViShouldNotBeFound(String filter) throws Exception {
        restAlActisoViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlActisoViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlActisoVi() throws Exception {
        // Get the alActisoVi
        restAlActisoViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlActisoVi() throws Exception {
        // Initialize the database
        insertedAlActisoVi = alActisoViRepository.saveAndFlush(alActisoVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alActisoVi
        AlActisoVi updatedAlActisoVi = alActisoViRepository.findById(alActisoVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlActisoVi are not directly saved in db
        em.detach(updatedAlActisoVi);
        updatedAlActisoVi.key(UPDATED_KEY).valueJason(UPDATED_VALUE_JASON);
        AlActisoViDTO alActisoViDTO = alActisoViMapper.toDto(updatedAlActisoVi);

        restAlActisoViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alActisoViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alActisoViDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlActisoVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlActisoViToMatchAllProperties(updatedAlActisoVi);
    }

    @Test
    @Transactional
    void putNonExistingAlActisoVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alActisoVi.setId(longCount.incrementAndGet());

        // Create the AlActisoVi
        AlActisoViDTO alActisoViDTO = alActisoViMapper.toDto(alActisoVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlActisoViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alActisoViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alActisoViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlActisoVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlActisoVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alActisoVi.setId(longCount.incrementAndGet());

        // Create the AlActisoVi
        AlActisoViDTO alActisoViDTO = alActisoViMapper.toDto(alActisoVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlActisoViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alActisoViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlActisoVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlActisoVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alActisoVi.setId(longCount.incrementAndGet());

        // Create the AlActisoVi
        AlActisoViDTO alActisoViDTO = alActisoViMapper.toDto(alActisoVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlActisoViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alActisoViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlActisoVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlActisoViWithPatch() throws Exception {
        // Initialize the database
        insertedAlActisoVi = alActisoViRepository.saveAndFlush(alActisoVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alActisoVi using partial update
        AlActisoVi partialUpdatedAlActisoVi = new AlActisoVi();
        partialUpdatedAlActisoVi.setId(alActisoVi.getId());

        partialUpdatedAlActisoVi.valueJason(UPDATED_VALUE_JASON);

        restAlActisoViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlActisoVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlActisoVi))
            )
            .andExpect(status().isOk());

        // Validate the AlActisoVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlActisoViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlActisoVi, alActisoVi),
            getPersistedAlActisoVi(alActisoVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlActisoViWithPatch() throws Exception {
        // Initialize the database
        insertedAlActisoVi = alActisoViRepository.saveAndFlush(alActisoVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alActisoVi using partial update
        AlActisoVi partialUpdatedAlActisoVi = new AlActisoVi();
        partialUpdatedAlActisoVi.setId(alActisoVi.getId());

        partialUpdatedAlActisoVi.key(UPDATED_KEY).valueJason(UPDATED_VALUE_JASON);

        restAlActisoViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlActisoVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlActisoVi))
            )
            .andExpect(status().isOk());

        // Validate the AlActisoVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlActisoViUpdatableFieldsEquals(partialUpdatedAlActisoVi, getPersistedAlActisoVi(partialUpdatedAlActisoVi));
    }

    @Test
    @Transactional
    void patchNonExistingAlActisoVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alActisoVi.setId(longCount.incrementAndGet());

        // Create the AlActisoVi
        AlActisoViDTO alActisoViDTO = alActisoViMapper.toDto(alActisoVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlActisoViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alActisoViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alActisoViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlActisoVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlActisoVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alActisoVi.setId(longCount.incrementAndGet());

        // Create the AlActisoVi
        AlActisoViDTO alActisoViDTO = alActisoViMapper.toDto(alActisoVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlActisoViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alActisoViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlActisoVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlActisoVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alActisoVi.setId(longCount.incrementAndGet());

        // Create the AlActisoVi
        AlActisoViDTO alActisoViDTO = alActisoViMapper.toDto(alActisoVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlActisoViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alActisoViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlActisoVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlActisoVi() throws Exception {
        // Initialize the database
        insertedAlActisoVi = alActisoViRepository.saveAndFlush(alActisoVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alActisoVi
        restAlActisoViMockMvc
            .perform(delete(ENTITY_API_URL_ID, alActisoVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alActisoViRepository.count();
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

    protected AlActisoVi getPersistedAlActisoVi(AlActisoVi alActisoVi) {
        return alActisoViRepository.findById(alActisoVi.getId()).orElseThrow();
    }

    protected void assertPersistedAlActisoViToMatchAllProperties(AlActisoVi expectedAlActisoVi) {
        assertAlActisoViAllPropertiesEquals(expectedAlActisoVi, getPersistedAlActisoVi(expectedAlActisoVi));
    }

    protected void assertPersistedAlActisoViToMatchUpdatableProperties(AlActisoVi expectedAlActisoVi) {
        assertAlActisoViAllUpdatablePropertiesEquals(expectedAlActisoVi, getPersistedAlActisoVi(expectedAlActisoVi));
    }
}
