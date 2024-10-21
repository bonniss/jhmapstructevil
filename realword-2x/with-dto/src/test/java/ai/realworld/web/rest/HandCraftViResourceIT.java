package ai.realworld.web.rest;

import static ai.realworld.domain.HandCraftViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.EdSheeranVi;
import ai.realworld.domain.HandCraftVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.RihannaVi;
import ai.realworld.repository.HandCraftViRepository;
import ai.realworld.service.dto.HandCraftViDTO;
import ai.realworld.service.mapper.HandCraftViMapper;
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
 * Integration tests for the {@link HandCraftViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HandCraftViResourceIT {

    private static final String ENTITY_API_URL = "/api/hand-craft-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HandCraftViRepository handCraftViRepository;

    @Autowired
    private HandCraftViMapper handCraftViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHandCraftViMockMvc;

    private HandCraftVi handCraftVi;

    private HandCraftVi insertedHandCraftVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HandCraftVi createEntity(EntityManager em) {
        HandCraftVi handCraftVi = new HandCraftVi();
        // Add required entity
        EdSheeranVi edSheeranVi;
        if (TestUtil.findAll(em, EdSheeranVi.class).isEmpty()) {
            edSheeranVi = EdSheeranViResourceIT.createEntity(em);
            em.persist(edSheeranVi);
            em.flush();
        } else {
            edSheeranVi = TestUtil.findAll(em, EdSheeranVi.class).get(0);
        }
        handCraftVi.setAgent(edSheeranVi);
        // Add required entity
        RihannaVi rihannaVi;
        if (TestUtil.findAll(em, RihannaVi.class).isEmpty()) {
            rihannaVi = RihannaViResourceIT.createEntity();
            em.persist(rihannaVi);
            em.flush();
        } else {
            rihannaVi = TestUtil.findAll(em, RihannaVi.class).get(0);
        }
        handCraftVi.setRole(rihannaVi);
        return handCraftVi;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HandCraftVi createUpdatedEntity(EntityManager em) {
        HandCraftVi updatedHandCraftVi = new HandCraftVi();
        // Add required entity
        EdSheeranVi edSheeranVi;
        if (TestUtil.findAll(em, EdSheeranVi.class).isEmpty()) {
            edSheeranVi = EdSheeranViResourceIT.createUpdatedEntity(em);
            em.persist(edSheeranVi);
            em.flush();
        } else {
            edSheeranVi = TestUtil.findAll(em, EdSheeranVi.class).get(0);
        }
        updatedHandCraftVi.setAgent(edSheeranVi);
        // Add required entity
        RihannaVi rihannaVi;
        if (TestUtil.findAll(em, RihannaVi.class).isEmpty()) {
            rihannaVi = RihannaViResourceIT.createUpdatedEntity();
            em.persist(rihannaVi);
            em.flush();
        } else {
            rihannaVi = TestUtil.findAll(em, RihannaVi.class).get(0);
        }
        updatedHandCraftVi.setRole(rihannaVi);
        return updatedHandCraftVi;
    }

    @BeforeEach
    public void initTest() {
        handCraftVi = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedHandCraftVi != null) {
            handCraftViRepository.delete(insertedHandCraftVi);
            insertedHandCraftVi = null;
        }
    }

    @Test
    @Transactional
    void createHandCraftVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HandCraftVi
        HandCraftViDTO handCraftViDTO = handCraftViMapper.toDto(handCraftVi);
        var returnedHandCraftViDTO = om.readValue(
            restHandCraftViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(handCraftViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HandCraftViDTO.class
        );

        // Validate the HandCraftVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHandCraftVi = handCraftViMapper.toEntity(returnedHandCraftViDTO);
        assertHandCraftViUpdatableFieldsEquals(returnedHandCraftVi, getPersistedHandCraftVi(returnedHandCraftVi));

        insertedHandCraftVi = returnedHandCraftVi;
    }

    @Test
    @Transactional
    void createHandCraftViWithExistingId() throws Exception {
        // Create the HandCraftVi with an existing ID
        handCraftVi.setId(1L);
        HandCraftViDTO handCraftViDTO = handCraftViMapper.toDto(handCraftVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHandCraftViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(handCraftViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HandCraftVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHandCraftVis() throws Exception {
        // Initialize the database
        insertedHandCraftVi = handCraftViRepository.saveAndFlush(handCraftVi);

        // Get all the handCraftViList
        restHandCraftViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(handCraftVi.getId().intValue())));
    }

    @Test
    @Transactional
    void getHandCraftVi() throws Exception {
        // Initialize the database
        insertedHandCraftVi = handCraftViRepository.saveAndFlush(handCraftVi);

        // Get the handCraftVi
        restHandCraftViMockMvc
            .perform(get(ENTITY_API_URL_ID, handCraftVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(handCraftVi.getId().intValue()));
    }

    @Test
    @Transactional
    void getHandCraftVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedHandCraftVi = handCraftViRepository.saveAndFlush(handCraftVi);

        Long id = handCraftVi.getId();

        defaultHandCraftViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultHandCraftViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultHandCraftViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHandCraftVisByAgentIsEqualToSomething() throws Exception {
        EdSheeranVi agent;
        if (TestUtil.findAll(em, EdSheeranVi.class).isEmpty()) {
            handCraftViRepository.saveAndFlush(handCraftVi);
            agent = EdSheeranViResourceIT.createEntity(em);
        } else {
            agent = TestUtil.findAll(em, EdSheeranVi.class).get(0);
        }
        em.persist(agent);
        em.flush();
        handCraftVi.setAgent(agent);
        handCraftViRepository.saveAndFlush(handCraftVi);
        Long agentId = agent.getId();
        // Get all the handCraftViList where agent equals to agentId
        defaultHandCraftViShouldBeFound("agentId.equals=" + agentId);

        // Get all the handCraftViList where agent equals to (agentId + 1)
        defaultHandCraftViShouldNotBeFound("agentId.equals=" + (agentId + 1));
    }

    @Test
    @Transactional
    void getAllHandCraftVisByRoleIsEqualToSomething() throws Exception {
        RihannaVi role;
        if (TestUtil.findAll(em, RihannaVi.class).isEmpty()) {
            handCraftViRepository.saveAndFlush(handCraftVi);
            role = RihannaViResourceIT.createEntity();
        } else {
            role = TestUtil.findAll(em, RihannaVi.class).get(0);
        }
        em.persist(role);
        em.flush();
        handCraftVi.setRole(role);
        handCraftViRepository.saveAndFlush(handCraftVi);
        Long roleId = role.getId();
        // Get all the handCraftViList where role equals to roleId
        defaultHandCraftViShouldBeFound("roleId.equals=" + roleId);

        // Get all the handCraftViList where role equals to (roleId + 1)
        defaultHandCraftViShouldNotBeFound("roleId.equals=" + (roleId + 1));
    }

    @Test
    @Transactional
    void getAllHandCraftVisByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            handCraftViRepository.saveAndFlush(handCraftVi);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        handCraftVi.setApplication(application);
        handCraftViRepository.saveAndFlush(handCraftVi);
        UUID applicationId = application.getId();
        // Get all the handCraftViList where application equals to applicationId
        defaultHandCraftViShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the handCraftViList where application equals to UUID.randomUUID()
        defaultHandCraftViShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultHandCraftViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultHandCraftViShouldBeFound(shouldBeFound);
        defaultHandCraftViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHandCraftViShouldBeFound(String filter) throws Exception {
        restHandCraftViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(handCraftVi.getId().intValue())));

        // Check, that the count call also returns 1
        restHandCraftViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHandCraftViShouldNotBeFound(String filter) throws Exception {
        restHandCraftViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHandCraftViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHandCraftVi() throws Exception {
        // Get the handCraftVi
        restHandCraftViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHandCraftVi() throws Exception {
        // Initialize the database
        insertedHandCraftVi = handCraftViRepository.saveAndFlush(handCraftVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the handCraftVi
        HandCraftVi updatedHandCraftVi = handCraftViRepository.findById(handCraftVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHandCraftVi are not directly saved in db
        em.detach(updatedHandCraftVi);
        HandCraftViDTO handCraftViDTO = handCraftViMapper.toDto(updatedHandCraftVi);

        restHandCraftViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, handCraftViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(handCraftViDTO))
            )
            .andExpect(status().isOk());

        // Validate the HandCraftVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHandCraftViToMatchAllProperties(updatedHandCraftVi);
    }

    @Test
    @Transactional
    void putNonExistingHandCraftVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        handCraftVi.setId(longCount.incrementAndGet());

        // Create the HandCraftVi
        HandCraftViDTO handCraftViDTO = handCraftViMapper.toDto(handCraftVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHandCraftViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, handCraftViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(handCraftViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HandCraftVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHandCraftVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        handCraftVi.setId(longCount.incrementAndGet());

        // Create the HandCraftVi
        HandCraftViDTO handCraftViDTO = handCraftViMapper.toDto(handCraftVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHandCraftViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(handCraftViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HandCraftVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHandCraftVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        handCraftVi.setId(longCount.incrementAndGet());

        // Create the HandCraftVi
        HandCraftViDTO handCraftViDTO = handCraftViMapper.toDto(handCraftVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHandCraftViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(handCraftViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HandCraftVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHandCraftViWithPatch() throws Exception {
        // Initialize the database
        insertedHandCraftVi = handCraftViRepository.saveAndFlush(handCraftVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the handCraftVi using partial update
        HandCraftVi partialUpdatedHandCraftVi = new HandCraftVi();
        partialUpdatedHandCraftVi.setId(handCraftVi.getId());

        restHandCraftViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHandCraftVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHandCraftVi))
            )
            .andExpect(status().isOk());

        // Validate the HandCraftVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHandCraftViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHandCraftVi, handCraftVi),
            getPersistedHandCraftVi(handCraftVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateHandCraftViWithPatch() throws Exception {
        // Initialize the database
        insertedHandCraftVi = handCraftViRepository.saveAndFlush(handCraftVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the handCraftVi using partial update
        HandCraftVi partialUpdatedHandCraftVi = new HandCraftVi();
        partialUpdatedHandCraftVi.setId(handCraftVi.getId());

        restHandCraftViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHandCraftVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHandCraftVi))
            )
            .andExpect(status().isOk());

        // Validate the HandCraftVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHandCraftViUpdatableFieldsEquals(partialUpdatedHandCraftVi, getPersistedHandCraftVi(partialUpdatedHandCraftVi));
    }

    @Test
    @Transactional
    void patchNonExistingHandCraftVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        handCraftVi.setId(longCount.incrementAndGet());

        // Create the HandCraftVi
        HandCraftViDTO handCraftViDTO = handCraftViMapper.toDto(handCraftVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHandCraftViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, handCraftViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(handCraftViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HandCraftVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHandCraftVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        handCraftVi.setId(longCount.incrementAndGet());

        // Create the HandCraftVi
        HandCraftViDTO handCraftViDTO = handCraftViMapper.toDto(handCraftVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHandCraftViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(handCraftViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HandCraftVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHandCraftVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        handCraftVi.setId(longCount.incrementAndGet());

        // Create the HandCraftVi
        HandCraftViDTO handCraftViDTO = handCraftViMapper.toDto(handCraftVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHandCraftViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(handCraftViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HandCraftVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHandCraftVi() throws Exception {
        // Initialize the database
        insertedHandCraftVi = handCraftViRepository.saveAndFlush(handCraftVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the handCraftVi
        restHandCraftViMockMvc
            .perform(delete(ENTITY_API_URL_ID, handCraftVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return handCraftViRepository.count();
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

    protected HandCraftVi getPersistedHandCraftVi(HandCraftVi handCraftVi) {
        return handCraftViRepository.findById(handCraftVi.getId()).orElseThrow();
    }

    protected void assertPersistedHandCraftViToMatchAllProperties(HandCraftVi expectedHandCraftVi) {
        assertHandCraftViAllPropertiesEquals(expectedHandCraftVi, getPersistedHandCraftVi(expectedHandCraftVi));
    }

    protected void assertPersistedHandCraftViToMatchUpdatableProperties(HandCraftVi expectedHandCraftVi) {
        assertHandCraftViAllUpdatablePropertiesEquals(expectedHandCraftVi, getPersistedHandCraftVi(expectedHandCraftVi));
    }
}
