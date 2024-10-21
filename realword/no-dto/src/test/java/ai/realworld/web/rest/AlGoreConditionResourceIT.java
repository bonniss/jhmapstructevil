package ai.realworld.web.rest;

import static ai.realworld.domain.AlGoreConditionAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlGore;
import ai.realworld.domain.AlGoreCondition;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.enumeration.AlcountSubjectivity;
import ai.realworld.domain.enumeration.NeonEction;
import ai.realworld.repository.AlGoreConditionRepository;
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
 * Integration tests for the {@link AlGoreConditionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlGoreConditionResourceIT {

    private static final AlcountSubjectivity DEFAULT_SUBJECT_TYPE = AlcountSubjectivity.PRODUCT;
    private static final AlcountSubjectivity UPDATED_SUBJECT_TYPE = AlcountSubjectivity.CATEGORY;

    private static final Long DEFAULT_SUBJECT = 1L;
    private static final Long UPDATED_SUBJECT = 2L;
    private static final Long SMALLER_SUBJECT = 1L - 1L;

    private static final NeonEction DEFAULT_ACTION = NeonEction.INCLUDE;
    private static final NeonEction UPDATED_ACTION = NeonEction.EXCLUDE;

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/al-gore-conditions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlGoreConditionRepository alGoreConditionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlGoreConditionMockMvc;

    private AlGoreCondition alGoreCondition;

    private AlGoreCondition insertedAlGoreCondition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlGoreCondition createEntity() {
        return new AlGoreCondition().subjectType(DEFAULT_SUBJECT_TYPE).subject(DEFAULT_SUBJECT).action(DEFAULT_ACTION).note(DEFAULT_NOTE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlGoreCondition createUpdatedEntity() {
        return new AlGoreCondition().subjectType(UPDATED_SUBJECT_TYPE).subject(UPDATED_SUBJECT).action(UPDATED_ACTION).note(UPDATED_NOTE);
    }

    @BeforeEach
    public void initTest() {
        alGoreCondition = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlGoreCondition != null) {
            alGoreConditionRepository.delete(insertedAlGoreCondition);
            insertedAlGoreCondition = null;
        }
    }

    @Test
    @Transactional
    void createAlGoreCondition() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlGoreCondition
        var returnedAlGoreCondition = om.readValue(
            restAlGoreConditionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alGoreCondition)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlGoreCondition.class
        );

        // Validate the AlGoreCondition in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlGoreConditionUpdatableFieldsEquals(returnedAlGoreCondition, getPersistedAlGoreCondition(returnedAlGoreCondition));

        insertedAlGoreCondition = returnedAlGoreCondition;
    }

    @Test
    @Transactional
    void createAlGoreConditionWithExistingId() throws Exception {
        // Create the AlGoreCondition with an existing ID
        alGoreCondition.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlGoreConditionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alGoreCondition)))
            .andExpect(status().isBadRequest());

        // Validate the AlGoreCondition in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAlGoreConditions() throws Exception {
        // Initialize the database
        insertedAlGoreCondition = alGoreConditionRepository.saveAndFlush(alGoreCondition);

        // Get all the alGoreConditionList
        restAlGoreConditionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alGoreCondition.getId().intValue())))
            .andExpect(jsonPath("$.[*].subjectType").value(hasItem(DEFAULT_SUBJECT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT.intValue())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @Test
    @Transactional
    void getAlGoreCondition() throws Exception {
        // Initialize the database
        insertedAlGoreCondition = alGoreConditionRepository.saveAndFlush(alGoreCondition);

        // Get the alGoreCondition
        restAlGoreConditionMockMvc
            .perform(get(ENTITY_API_URL_ID, alGoreCondition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alGoreCondition.getId().intValue()))
            .andExpect(jsonPath("$.subjectType").value(DEFAULT_SUBJECT_TYPE.toString()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT.intValue()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }

    @Test
    @Transactional
    void getAlGoreConditionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlGoreCondition = alGoreConditionRepository.saveAndFlush(alGoreCondition);

        Long id = alGoreCondition.getId();

        defaultAlGoreConditionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlGoreConditionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlGoreConditionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlGoreConditionsBySubjectTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoreCondition = alGoreConditionRepository.saveAndFlush(alGoreCondition);

        // Get all the alGoreConditionList where subjectType equals to
        defaultAlGoreConditionFiltering("subjectType.equals=" + DEFAULT_SUBJECT_TYPE, "subjectType.equals=" + UPDATED_SUBJECT_TYPE);
    }

    @Test
    @Transactional
    void getAllAlGoreConditionsBySubjectTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlGoreCondition = alGoreConditionRepository.saveAndFlush(alGoreCondition);

        // Get all the alGoreConditionList where subjectType in
        defaultAlGoreConditionFiltering(
            "subjectType.in=" + DEFAULT_SUBJECT_TYPE + "," + UPDATED_SUBJECT_TYPE,
            "subjectType.in=" + UPDATED_SUBJECT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAlGoreConditionsBySubjectTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlGoreCondition = alGoreConditionRepository.saveAndFlush(alGoreCondition);

        // Get all the alGoreConditionList where subjectType is not null
        defaultAlGoreConditionFiltering("subjectType.specified=true", "subjectType.specified=false");
    }

    @Test
    @Transactional
    void getAllAlGoreConditionsBySubjectIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoreCondition = alGoreConditionRepository.saveAndFlush(alGoreCondition);

        // Get all the alGoreConditionList where subject equals to
        defaultAlGoreConditionFiltering("subject.equals=" + DEFAULT_SUBJECT, "subject.equals=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllAlGoreConditionsBySubjectIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlGoreCondition = alGoreConditionRepository.saveAndFlush(alGoreCondition);

        // Get all the alGoreConditionList where subject in
        defaultAlGoreConditionFiltering("subject.in=" + DEFAULT_SUBJECT + "," + UPDATED_SUBJECT, "subject.in=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllAlGoreConditionsBySubjectIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlGoreCondition = alGoreConditionRepository.saveAndFlush(alGoreCondition);

        // Get all the alGoreConditionList where subject is not null
        defaultAlGoreConditionFiltering("subject.specified=true", "subject.specified=false");
    }

    @Test
    @Transactional
    void getAllAlGoreConditionsBySubjectIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoreCondition = alGoreConditionRepository.saveAndFlush(alGoreCondition);

        // Get all the alGoreConditionList where subject is greater than or equal to
        defaultAlGoreConditionFiltering("subject.greaterThanOrEqual=" + DEFAULT_SUBJECT, "subject.greaterThanOrEqual=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllAlGoreConditionsBySubjectIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoreCondition = alGoreConditionRepository.saveAndFlush(alGoreCondition);

        // Get all the alGoreConditionList where subject is less than or equal to
        defaultAlGoreConditionFiltering("subject.lessThanOrEqual=" + DEFAULT_SUBJECT, "subject.lessThanOrEqual=" + SMALLER_SUBJECT);
    }

    @Test
    @Transactional
    void getAllAlGoreConditionsBySubjectIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlGoreCondition = alGoreConditionRepository.saveAndFlush(alGoreCondition);

        // Get all the alGoreConditionList where subject is less than
        defaultAlGoreConditionFiltering("subject.lessThan=" + UPDATED_SUBJECT, "subject.lessThan=" + DEFAULT_SUBJECT);
    }

    @Test
    @Transactional
    void getAllAlGoreConditionsBySubjectIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlGoreCondition = alGoreConditionRepository.saveAndFlush(alGoreCondition);

        // Get all the alGoreConditionList where subject is greater than
        defaultAlGoreConditionFiltering("subject.greaterThan=" + SMALLER_SUBJECT, "subject.greaterThan=" + DEFAULT_SUBJECT);
    }

    @Test
    @Transactional
    void getAllAlGoreConditionsByActionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoreCondition = alGoreConditionRepository.saveAndFlush(alGoreCondition);

        // Get all the alGoreConditionList where action equals to
        defaultAlGoreConditionFiltering("action.equals=" + DEFAULT_ACTION, "action.equals=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllAlGoreConditionsByActionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlGoreCondition = alGoreConditionRepository.saveAndFlush(alGoreCondition);

        // Get all the alGoreConditionList where action in
        defaultAlGoreConditionFiltering("action.in=" + DEFAULT_ACTION + "," + UPDATED_ACTION, "action.in=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllAlGoreConditionsByActionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlGoreCondition = alGoreConditionRepository.saveAndFlush(alGoreCondition);

        // Get all the alGoreConditionList where action is not null
        defaultAlGoreConditionFiltering("action.specified=true", "action.specified=false");
    }

    @Test
    @Transactional
    void getAllAlGoreConditionsByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoreCondition = alGoreConditionRepository.saveAndFlush(alGoreCondition);

        // Get all the alGoreConditionList where note equals to
        defaultAlGoreConditionFiltering("note.equals=" + DEFAULT_NOTE, "note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllAlGoreConditionsByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlGoreCondition = alGoreConditionRepository.saveAndFlush(alGoreCondition);

        // Get all the alGoreConditionList where note in
        defaultAlGoreConditionFiltering("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE, "note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllAlGoreConditionsByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlGoreCondition = alGoreConditionRepository.saveAndFlush(alGoreCondition);

        // Get all the alGoreConditionList where note is not null
        defaultAlGoreConditionFiltering("note.specified=true", "note.specified=false");
    }

    @Test
    @Transactional
    void getAllAlGoreConditionsByNoteContainsSomething() throws Exception {
        // Initialize the database
        insertedAlGoreCondition = alGoreConditionRepository.saveAndFlush(alGoreCondition);

        // Get all the alGoreConditionList where note contains
        defaultAlGoreConditionFiltering("note.contains=" + DEFAULT_NOTE, "note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllAlGoreConditionsByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlGoreCondition = alGoreConditionRepository.saveAndFlush(alGoreCondition);

        // Get all the alGoreConditionList where note does not contain
        defaultAlGoreConditionFiltering("note.doesNotContain=" + UPDATED_NOTE, "note.doesNotContain=" + DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void getAllAlGoreConditionsByParentIsEqualToSomething() throws Exception {
        AlGore parent;
        if (TestUtil.findAll(em, AlGore.class).isEmpty()) {
            alGoreConditionRepository.saveAndFlush(alGoreCondition);
            parent = AlGoreResourceIT.createEntity();
        } else {
            parent = TestUtil.findAll(em, AlGore.class).get(0);
        }
        em.persist(parent);
        em.flush();
        alGoreCondition.setParent(parent);
        alGoreConditionRepository.saveAndFlush(alGoreCondition);
        Long parentId = parent.getId();
        // Get all the alGoreConditionList where parent equals to parentId
        defaultAlGoreConditionShouldBeFound("parentId.equals=" + parentId);

        // Get all the alGoreConditionList where parent equals to (parentId + 1)
        defaultAlGoreConditionShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    @Test
    @Transactional
    void getAllAlGoreConditionsByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alGoreConditionRepository.saveAndFlush(alGoreCondition);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alGoreCondition.setApplication(application);
        alGoreConditionRepository.saveAndFlush(alGoreCondition);
        UUID applicationId = application.getId();
        // Get all the alGoreConditionList where application equals to applicationId
        defaultAlGoreConditionShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alGoreConditionList where application equals to UUID.randomUUID()
        defaultAlGoreConditionShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlGoreConditionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlGoreConditionShouldBeFound(shouldBeFound);
        defaultAlGoreConditionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlGoreConditionShouldBeFound(String filter) throws Exception {
        restAlGoreConditionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alGoreCondition.getId().intValue())))
            .andExpect(jsonPath("$.[*].subjectType").value(hasItem(DEFAULT_SUBJECT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT.intValue())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));

        // Check, that the count call also returns 1
        restAlGoreConditionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlGoreConditionShouldNotBeFound(String filter) throws Exception {
        restAlGoreConditionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlGoreConditionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlGoreCondition() throws Exception {
        // Get the alGoreCondition
        restAlGoreConditionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlGoreCondition() throws Exception {
        // Initialize the database
        insertedAlGoreCondition = alGoreConditionRepository.saveAndFlush(alGoreCondition);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alGoreCondition
        AlGoreCondition updatedAlGoreCondition = alGoreConditionRepository.findById(alGoreCondition.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlGoreCondition are not directly saved in db
        em.detach(updatedAlGoreCondition);
        updatedAlGoreCondition.subjectType(UPDATED_SUBJECT_TYPE).subject(UPDATED_SUBJECT).action(UPDATED_ACTION).note(UPDATED_NOTE);

        restAlGoreConditionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlGoreCondition.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlGoreCondition))
            )
            .andExpect(status().isOk());

        // Validate the AlGoreCondition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlGoreConditionToMatchAllProperties(updatedAlGoreCondition);
    }

    @Test
    @Transactional
    void putNonExistingAlGoreCondition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alGoreCondition.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlGoreConditionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alGoreCondition.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alGoreCondition))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlGoreCondition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlGoreCondition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alGoreCondition.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlGoreConditionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alGoreCondition))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlGoreCondition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlGoreCondition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alGoreCondition.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlGoreConditionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alGoreCondition)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlGoreCondition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlGoreConditionWithPatch() throws Exception {
        // Initialize the database
        insertedAlGoreCondition = alGoreConditionRepository.saveAndFlush(alGoreCondition);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alGoreCondition using partial update
        AlGoreCondition partialUpdatedAlGoreCondition = new AlGoreCondition();
        partialUpdatedAlGoreCondition.setId(alGoreCondition.getId());

        partialUpdatedAlGoreCondition.subjectType(UPDATED_SUBJECT_TYPE).subject(UPDATED_SUBJECT).note(UPDATED_NOTE);

        restAlGoreConditionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlGoreCondition.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlGoreCondition))
            )
            .andExpect(status().isOk());

        // Validate the AlGoreCondition in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlGoreConditionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlGoreCondition, alGoreCondition),
            getPersistedAlGoreCondition(alGoreCondition)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlGoreConditionWithPatch() throws Exception {
        // Initialize the database
        insertedAlGoreCondition = alGoreConditionRepository.saveAndFlush(alGoreCondition);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alGoreCondition using partial update
        AlGoreCondition partialUpdatedAlGoreCondition = new AlGoreCondition();
        partialUpdatedAlGoreCondition.setId(alGoreCondition.getId());

        partialUpdatedAlGoreCondition.subjectType(UPDATED_SUBJECT_TYPE).subject(UPDATED_SUBJECT).action(UPDATED_ACTION).note(UPDATED_NOTE);

        restAlGoreConditionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlGoreCondition.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlGoreCondition))
            )
            .andExpect(status().isOk());

        // Validate the AlGoreCondition in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlGoreConditionUpdatableFieldsEquals(
            partialUpdatedAlGoreCondition,
            getPersistedAlGoreCondition(partialUpdatedAlGoreCondition)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAlGoreCondition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alGoreCondition.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlGoreConditionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alGoreCondition.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alGoreCondition))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlGoreCondition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlGoreCondition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alGoreCondition.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlGoreConditionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alGoreCondition))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlGoreCondition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlGoreCondition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alGoreCondition.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlGoreConditionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alGoreCondition)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlGoreCondition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlGoreCondition() throws Exception {
        // Initialize the database
        insertedAlGoreCondition = alGoreConditionRepository.saveAndFlush(alGoreCondition);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alGoreCondition
        restAlGoreConditionMockMvc
            .perform(delete(ENTITY_API_URL_ID, alGoreCondition.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alGoreConditionRepository.count();
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

    protected AlGoreCondition getPersistedAlGoreCondition(AlGoreCondition alGoreCondition) {
        return alGoreConditionRepository.findById(alGoreCondition.getId()).orElseThrow();
    }

    protected void assertPersistedAlGoreConditionToMatchAllProperties(AlGoreCondition expectedAlGoreCondition) {
        assertAlGoreConditionAllPropertiesEquals(expectedAlGoreCondition, getPersistedAlGoreCondition(expectedAlGoreCondition));
    }

    protected void assertPersistedAlGoreConditionToMatchUpdatableProperties(AlGoreCondition expectedAlGoreCondition) {
        assertAlGoreConditionAllUpdatablePropertiesEquals(expectedAlGoreCondition, getPersistedAlGoreCondition(expectedAlGoreCondition));
    }
}
