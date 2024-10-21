package ai.realworld.web.rest;

import static ai.realworld.domain.HandCraftAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.EdSheeran;
import ai.realworld.domain.HandCraft;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Rihanna;
import ai.realworld.repository.HandCraftRepository;
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
 * Integration tests for the {@link HandCraftResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HandCraftResourceIT {

    private static final String ENTITY_API_URL = "/api/hand-crafts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HandCraftRepository handCraftRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHandCraftMockMvc;

    private HandCraft handCraft;

    private HandCraft insertedHandCraft;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HandCraft createEntity(EntityManager em) {
        HandCraft handCraft = new HandCraft();
        // Add required entity
        EdSheeran edSheeran;
        if (TestUtil.findAll(em, EdSheeran.class).isEmpty()) {
            edSheeran = EdSheeranResourceIT.createEntity(em);
            em.persist(edSheeran);
            em.flush();
        } else {
            edSheeran = TestUtil.findAll(em, EdSheeran.class).get(0);
        }
        handCraft.setAgent(edSheeran);
        // Add required entity
        Rihanna rihanna;
        if (TestUtil.findAll(em, Rihanna.class).isEmpty()) {
            rihanna = RihannaResourceIT.createEntity();
            em.persist(rihanna);
            em.flush();
        } else {
            rihanna = TestUtil.findAll(em, Rihanna.class).get(0);
        }
        handCraft.setRole(rihanna);
        return handCraft;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HandCraft createUpdatedEntity(EntityManager em) {
        HandCraft updatedHandCraft = new HandCraft();
        // Add required entity
        EdSheeran edSheeran;
        if (TestUtil.findAll(em, EdSheeran.class).isEmpty()) {
            edSheeran = EdSheeranResourceIT.createUpdatedEntity(em);
            em.persist(edSheeran);
            em.flush();
        } else {
            edSheeran = TestUtil.findAll(em, EdSheeran.class).get(0);
        }
        updatedHandCraft.setAgent(edSheeran);
        // Add required entity
        Rihanna rihanna;
        if (TestUtil.findAll(em, Rihanna.class).isEmpty()) {
            rihanna = RihannaResourceIT.createUpdatedEntity();
            em.persist(rihanna);
            em.flush();
        } else {
            rihanna = TestUtil.findAll(em, Rihanna.class).get(0);
        }
        updatedHandCraft.setRole(rihanna);
        return updatedHandCraft;
    }

    @BeforeEach
    public void initTest() {
        handCraft = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedHandCraft != null) {
            handCraftRepository.delete(insertedHandCraft);
            insertedHandCraft = null;
        }
    }

    @Test
    @Transactional
    void createHandCraft() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HandCraft
        var returnedHandCraft = om.readValue(
            restHandCraftMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(handCraft)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HandCraft.class
        );

        // Validate the HandCraft in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertHandCraftUpdatableFieldsEquals(returnedHandCraft, getPersistedHandCraft(returnedHandCraft));

        insertedHandCraft = returnedHandCraft;
    }

    @Test
    @Transactional
    void createHandCraftWithExistingId() throws Exception {
        // Create the HandCraft with an existing ID
        handCraft.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHandCraftMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(handCraft)))
            .andExpect(status().isBadRequest());

        // Validate the HandCraft in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHandCrafts() throws Exception {
        // Initialize the database
        insertedHandCraft = handCraftRepository.saveAndFlush(handCraft);

        // Get all the handCraftList
        restHandCraftMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(handCraft.getId().intValue())));
    }

    @Test
    @Transactional
    void getHandCraft() throws Exception {
        // Initialize the database
        insertedHandCraft = handCraftRepository.saveAndFlush(handCraft);

        // Get the handCraft
        restHandCraftMockMvc
            .perform(get(ENTITY_API_URL_ID, handCraft.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(handCraft.getId().intValue()));
    }

    @Test
    @Transactional
    void getHandCraftsByIdFiltering() throws Exception {
        // Initialize the database
        insertedHandCraft = handCraftRepository.saveAndFlush(handCraft);

        Long id = handCraft.getId();

        defaultHandCraftFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultHandCraftFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultHandCraftFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHandCraftsByAgentIsEqualToSomething() throws Exception {
        EdSheeran agent;
        if (TestUtil.findAll(em, EdSheeran.class).isEmpty()) {
            handCraftRepository.saveAndFlush(handCraft);
            agent = EdSheeranResourceIT.createEntity(em);
        } else {
            agent = TestUtil.findAll(em, EdSheeran.class).get(0);
        }
        em.persist(agent);
        em.flush();
        handCraft.setAgent(agent);
        handCraftRepository.saveAndFlush(handCraft);
        Long agentId = agent.getId();
        // Get all the handCraftList where agent equals to agentId
        defaultHandCraftShouldBeFound("agentId.equals=" + agentId);

        // Get all the handCraftList where agent equals to (agentId + 1)
        defaultHandCraftShouldNotBeFound("agentId.equals=" + (agentId + 1));
    }

    @Test
    @Transactional
    void getAllHandCraftsByRoleIsEqualToSomething() throws Exception {
        Rihanna role;
        if (TestUtil.findAll(em, Rihanna.class).isEmpty()) {
            handCraftRepository.saveAndFlush(handCraft);
            role = RihannaResourceIT.createEntity();
        } else {
            role = TestUtil.findAll(em, Rihanna.class).get(0);
        }
        em.persist(role);
        em.flush();
        handCraft.setRole(role);
        handCraftRepository.saveAndFlush(handCraft);
        Long roleId = role.getId();
        // Get all the handCraftList where role equals to roleId
        defaultHandCraftShouldBeFound("roleId.equals=" + roleId);

        // Get all the handCraftList where role equals to (roleId + 1)
        defaultHandCraftShouldNotBeFound("roleId.equals=" + (roleId + 1));
    }

    @Test
    @Transactional
    void getAllHandCraftsByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            handCraftRepository.saveAndFlush(handCraft);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        handCraft.setApplication(application);
        handCraftRepository.saveAndFlush(handCraft);
        UUID applicationId = application.getId();
        // Get all the handCraftList where application equals to applicationId
        defaultHandCraftShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the handCraftList where application equals to UUID.randomUUID()
        defaultHandCraftShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultHandCraftFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultHandCraftShouldBeFound(shouldBeFound);
        defaultHandCraftShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHandCraftShouldBeFound(String filter) throws Exception {
        restHandCraftMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(handCraft.getId().intValue())));

        // Check, that the count call also returns 1
        restHandCraftMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHandCraftShouldNotBeFound(String filter) throws Exception {
        restHandCraftMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHandCraftMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHandCraft() throws Exception {
        // Get the handCraft
        restHandCraftMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHandCraft() throws Exception {
        // Initialize the database
        insertedHandCraft = handCraftRepository.saveAndFlush(handCraft);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the handCraft
        HandCraft updatedHandCraft = handCraftRepository.findById(handCraft.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHandCraft are not directly saved in db
        em.detach(updatedHandCraft);

        restHandCraftMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHandCraft.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedHandCraft))
            )
            .andExpect(status().isOk());

        // Validate the HandCraft in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHandCraftToMatchAllProperties(updatedHandCraft);
    }

    @Test
    @Transactional
    void putNonExistingHandCraft() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        handCraft.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHandCraftMockMvc
            .perform(
                put(ENTITY_API_URL_ID, handCraft.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(handCraft))
            )
            .andExpect(status().isBadRequest());

        // Validate the HandCraft in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHandCraft() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        handCraft.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHandCraftMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(handCraft))
            )
            .andExpect(status().isBadRequest());

        // Validate the HandCraft in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHandCraft() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        handCraft.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHandCraftMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(handCraft)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HandCraft in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHandCraftWithPatch() throws Exception {
        // Initialize the database
        insertedHandCraft = handCraftRepository.saveAndFlush(handCraft);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the handCraft using partial update
        HandCraft partialUpdatedHandCraft = new HandCraft();
        partialUpdatedHandCraft.setId(handCraft.getId());

        restHandCraftMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHandCraft.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHandCraft))
            )
            .andExpect(status().isOk());

        // Validate the HandCraft in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHandCraftUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHandCraft, handCraft),
            getPersistedHandCraft(handCraft)
        );
    }

    @Test
    @Transactional
    void fullUpdateHandCraftWithPatch() throws Exception {
        // Initialize the database
        insertedHandCraft = handCraftRepository.saveAndFlush(handCraft);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the handCraft using partial update
        HandCraft partialUpdatedHandCraft = new HandCraft();
        partialUpdatedHandCraft.setId(handCraft.getId());

        restHandCraftMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHandCraft.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHandCraft))
            )
            .andExpect(status().isOk());

        // Validate the HandCraft in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHandCraftUpdatableFieldsEquals(partialUpdatedHandCraft, getPersistedHandCraft(partialUpdatedHandCraft));
    }

    @Test
    @Transactional
    void patchNonExistingHandCraft() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        handCraft.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHandCraftMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, handCraft.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(handCraft))
            )
            .andExpect(status().isBadRequest());

        // Validate the HandCraft in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHandCraft() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        handCraft.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHandCraftMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(handCraft))
            )
            .andExpect(status().isBadRequest());

        // Validate the HandCraft in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHandCraft() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        handCraft.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHandCraftMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(handCraft)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HandCraft in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHandCraft() throws Exception {
        // Initialize the database
        insertedHandCraft = handCraftRepository.saveAndFlush(handCraft);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the handCraft
        restHandCraftMockMvc
            .perform(delete(ENTITY_API_URL_ID, handCraft.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return handCraftRepository.count();
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

    protected HandCraft getPersistedHandCraft(HandCraft handCraft) {
        return handCraftRepository.findById(handCraft.getId()).orElseThrow();
    }

    protected void assertPersistedHandCraftToMatchAllProperties(HandCraft expectedHandCraft) {
        assertHandCraftAllPropertiesEquals(expectedHandCraft, getPersistedHandCraft(expectedHandCraft));
    }

    protected void assertPersistedHandCraftToMatchUpdatableProperties(HandCraft expectedHandCraft) {
        assertHandCraftAllUpdatablePropertiesEquals(expectedHandCraft, getPersistedHandCraft(expectedHandCraft));
    }
}
