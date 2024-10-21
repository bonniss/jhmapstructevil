package ai.realworld.web.rest;

import static ai.realworld.domain.AlVueVueViConditionAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlVueVueVi;
import ai.realworld.domain.AlVueVueViCondition;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.enumeration.AlcountSubjectivity;
import ai.realworld.domain.enumeration.NeonEction;
import ai.realworld.repository.AlVueVueViConditionRepository;
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
 * Integration tests for the {@link AlVueVueViConditionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlVueVueViConditionResourceIT {

    private static final AlcountSubjectivity DEFAULT_SUBJECT_TYPE = AlcountSubjectivity.PRODUCT;
    private static final AlcountSubjectivity UPDATED_SUBJECT_TYPE = AlcountSubjectivity.CATEGORY;

    private static final Long DEFAULT_SUBJECT = 1L;
    private static final Long UPDATED_SUBJECT = 2L;
    private static final Long SMALLER_SUBJECT = 1L - 1L;

    private static final NeonEction DEFAULT_ACTION = NeonEction.INCLUDE;
    private static final NeonEction UPDATED_ACTION = NeonEction.EXCLUDE;

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/al-vue-vue-vi-conditions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlVueVueViConditionRepository alVueVueViConditionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlVueVueViConditionMockMvc;

    private AlVueVueViCondition alVueVueViCondition;

    private AlVueVueViCondition insertedAlVueVueViCondition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlVueVueViCondition createEntity() {
        return new AlVueVueViCondition()
            .subjectType(DEFAULT_SUBJECT_TYPE)
            .subject(DEFAULT_SUBJECT)
            .action(DEFAULT_ACTION)
            .note(DEFAULT_NOTE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlVueVueViCondition createUpdatedEntity() {
        return new AlVueVueViCondition()
            .subjectType(UPDATED_SUBJECT_TYPE)
            .subject(UPDATED_SUBJECT)
            .action(UPDATED_ACTION)
            .note(UPDATED_NOTE);
    }

    @BeforeEach
    public void initTest() {
        alVueVueViCondition = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlVueVueViCondition != null) {
            alVueVueViConditionRepository.delete(insertedAlVueVueViCondition);
            insertedAlVueVueViCondition = null;
        }
    }

    @Test
    @Transactional
    void createAlVueVueViCondition() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlVueVueViCondition
        var returnedAlVueVueViCondition = om.readValue(
            restAlVueVueViConditionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alVueVueViCondition)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlVueVueViCondition.class
        );

        // Validate the AlVueVueViCondition in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlVueVueViConditionUpdatableFieldsEquals(
            returnedAlVueVueViCondition,
            getPersistedAlVueVueViCondition(returnedAlVueVueViCondition)
        );

        insertedAlVueVueViCondition = returnedAlVueVueViCondition;
    }

    @Test
    @Transactional
    void createAlVueVueViConditionWithExistingId() throws Exception {
        // Create the AlVueVueViCondition with an existing ID
        alVueVueViCondition.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlVueVueViConditionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alVueVueViCondition)))
            .andExpect(status().isBadRequest());

        // Validate the AlVueVueViCondition in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAlVueVueViConditions() throws Exception {
        // Initialize the database
        insertedAlVueVueViCondition = alVueVueViConditionRepository.saveAndFlush(alVueVueViCondition);

        // Get all the alVueVueViConditionList
        restAlVueVueViConditionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alVueVueViCondition.getId().intValue())))
            .andExpect(jsonPath("$.[*].subjectType").value(hasItem(DEFAULT_SUBJECT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT.intValue())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @Test
    @Transactional
    void getAlVueVueViCondition() throws Exception {
        // Initialize the database
        insertedAlVueVueViCondition = alVueVueViConditionRepository.saveAndFlush(alVueVueViCondition);

        // Get the alVueVueViCondition
        restAlVueVueViConditionMockMvc
            .perform(get(ENTITY_API_URL_ID, alVueVueViCondition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alVueVueViCondition.getId().intValue()))
            .andExpect(jsonPath("$.subjectType").value(DEFAULT_SUBJECT_TYPE.toString()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT.intValue()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }

    @Test
    @Transactional
    void getAlVueVueViConditionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlVueVueViCondition = alVueVueViConditionRepository.saveAndFlush(alVueVueViCondition);

        Long id = alVueVueViCondition.getId();

        defaultAlVueVueViConditionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlVueVueViConditionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlVueVueViConditionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlVueVueViConditionsBySubjectTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueViCondition = alVueVueViConditionRepository.saveAndFlush(alVueVueViCondition);

        // Get all the alVueVueViConditionList where subjectType equals to
        defaultAlVueVueViConditionFiltering("subjectType.equals=" + DEFAULT_SUBJECT_TYPE, "subjectType.equals=" + UPDATED_SUBJECT_TYPE);
    }

    @Test
    @Transactional
    void getAllAlVueVueViConditionsBySubjectTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVueViCondition = alVueVueViConditionRepository.saveAndFlush(alVueVueViCondition);

        // Get all the alVueVueViConditionList where subjectType in
        defaultAlVueVueViConditionFiltering(
            "subjectType.in=" + DEFAULT_SUBJECT_TYPE + "," + UPDATED_SUBJECT_TYPE,
            "subjectType.in=" + UPDATED_SUBJECT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueViConditionsBySubjectTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVueViCondition = alVueVueViConditionRepository.saveAndFlush(alVueVueViCondition);

        // Get all the alVueVueViConditionList where subjectType is not null
        defaultAlVueVueViConditionFiltering("subjectType.specified=true", "subjectType.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVueViConditionsBySubjectIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueViCondition = alVueVueViConditionRepository.saveAndFlush(alVueVueViCondition);

        // Get all the alVueVueViConditionList where subject equals to
        defaultAlVueVueViConditionFiltering("subject.equals=" + DEFAULT_SUBJECT, "subject.equals=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllAlVueVueViConditionsBySubjectIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVueViCondition = alVueVueViConditionRepository.saveAndFlush(alVueVueViCondition);

        // Get all the alVueVueViConditionList where subject in
        defaultAlVueVueViConditionFiltering("subject.in=" + DEFAULT_SUBJECT + "," + UPDATED_SUBJECT, "subject.in=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllAlVueVueViConditionsBySubjectIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVueViCondition = alVueVueViConditionRepository.saveAndFlush(alVueVueViCondition);

        // Get all the alVueVueViConditionList where subject is not null
        defaultAlVueVueViConditionFiltering("subject.specified=true", "subject.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVueViConditionsBySubjectIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueViCondition = alVueVueViConditionRepository.saveAndFlush(alVueVueViCondition);

        // Get all the alVueVueViConditionList where subject is greater than or equal to
        defaultAlVueVueViConditionFiltering(
            "subject.greaterThanOrEqual=" + DEFAULT_SUBJECT,
            "subject.greaterThanOrEqual=" + UPDATED_SUBJECT
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueViConditionsBySubjectIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueViCondition = alVueVueViConditionRepository.saveAndFlush(alVueVueViCondition);

        // Get all the alVueVueViConditionList where subject is less than or equal to
        defaultAlVueVueViConditionFiltering("subject.lessThanOrEqual=" + DEFAULT_SUBJECT, "subject.lessThanOrEqual=" + SMALLER_SUBJECT);
    }

    @Test
    @Transactional
    void getAllAlVueVueViConditionsBySubjectIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueViCondition = alVueVueViConditionRepository.saveAndFlush(alVueVueViCondition);

        // Get all the alVueVueViConditionList where subject is less than
        defaultAlVueVueViConditionFiltering("subject.lessThan=" + UPDATED_SUBJECT, "subject.lessThan=" + DEFAULT_SUBJECT);
    }

    @Test
    @Transactional
    void getAllAlVueVueViConditionsBySubjectIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueViCondition = alVueVueViConditionRepository.saveAndFlush(alVueVueViCondition);

        // Get all the alVueVueViConditionList where subject is greater than
        defaultAlVueVueViConditionFiltering("subject.greaterThan=" + SMALLER_SUBJECT, "subject.greaterThan=" + DEFAULT_SUBJECT);
    }

    @Test
    @Transactional
    void getAllAlVueVueViConditionsByActionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueViCondition = alVueVueViConditionRepository.saveAndFlush(alVueVueViCondition);

        // Get all the alVueVueViConditionList where action equals to
        defaultAlVueVueViConditionFiltering("action.equals=" + DEFAULT_ACTION, "action.equals=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllAlVueVueViConditionsByActionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVueViCondition = alVueVueViConditionRepository.saveAndFlush(alVueVueViCondition);

        // Get all the alVueVueViConditionList where action in
        defaultAlVueVueViConditionFiltering("action.in=" + DEFAULT_ACTION + "," + UPDATED_ACTION, "action.in=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllAlVueVueViConditionsByActionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVueViCondition = alVueVueViConditionRepository.saveAndFlush(alVueVueViCondition);

        // Get all the alVueVueViConditionList where action is not null
        defaultAlVueVueViConditionFiltering("action.specified=true", "action.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVueViConditionsByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueViCondition = alVueVueViConditionRepository.saveAndFlush(alVueVueViCondition);

        // Get all the alVueVueViConditionList where note equals to
        defaultAlVueVueViConditionFiltering("note.equals=" + DEFAULT_NOTE, "note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllAlVueVueViConditionsByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVueViCondition = alVueVueViConditionRepository.saveAndFlush(alVueVueViCondition);

        // Get all the alVueVueViConditionList where note in
        defaultAlVueVueViConditionFiltering("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE, "note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllAlVueVueViConditionsByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVueViCondition = alVueVueViConditionRepository.saveAndFlush(alVueVueViCondition);

        // Get all the alVueVueViConditionList where note is not null
        defaultAlVueVueViConditionFiltering("note.specified=true", "note.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVueViConditionsByNoteContainsSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueViCondition = alVueVueViConditionRepository.saveAndFlush(alVueVueViCondition);

        // Get all the alVueVueViConditionList where note contains
        defaultAlVueVueViConditionFiltering("note.contains=" + DEFAULT_NOTE, "note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllAlVueVueViConditionsByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueViCondition = alVueVueViConditionRepository.saveAndFlush(alVueVueViCondition);

        // Get all the alVueVueViConditionList where note does not contain
        defaultAlVueVueViConditionFiltering("note.doesNotContain=" + UPDATED_NOTE, "note.doesNotContain=" + DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void getAllAlVueVueViConditionsByParentIsEqualToSomething() throws Exception {
        AlVueVueVi parent;
        if (TestUtil.findAll(em, AlVueVueVi.class).isEmpty()) {
            alVueVueViConditionRepository.saveAndFlush(alVueVueViCondition);
            parent = AlVueVueViResourceIT.createEntity();
        } else {
            parent = TestUtil.findAll(em, AlVueVueVi.class).get(0);
        }
        em.persist(parent);
        em.flush();
        alVueVueViCondition.setParent(parent);
        alVueVueViConditionRepository.saveAndFlush(alVueVueViCondition);
        UUID parentId = parent.getId();
        // Get all the alVueVueViConditionList where parent equals to parentId
        defaultAlVueVueViConditionShouldBeFound("parentId.equals=" + parentId);

        // Get all the alVueVueViConditionList where parent equals to UUID.randomUUID()
        defaultAlVueVueViConditionShouldNotBeFound("parentId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlVueVueViConditionsByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alVueVueViConditionRepository.saveAndFlush(alVueVueViCondition);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alVueVueViCondition.setApplication(application);
        alVueVueViConditionRepository.saveAndFlush(alVueVueViCondition);
        UUID applicationId = application.getId();
        // Get all the alVueVueViConditionList where application equals to applicationId
        defaultAlVueVueViConditionShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alVueVueViConditionList where application equals to UUID.randomUUID()
        defaultAlVueVueViConditionShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlVueVueViConditionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlVueVueViConditionShouldBeFound(shouldBeFound);
        defaultAlVueVueViConditionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlVueVueViConditionShouldBeFound(String filter) throws Exception {
        restAlVueVueViConditionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alVueVueViCondition.getId().intValue())))
            .andExpect(jsonPath("$.[*].subjectType").value(hasItem(DEFAULT_SUBJECT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT.intValue())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));

        // Check, that the count call also returns 1
        restAlVueVueViConditionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlVueVueViConditionShouldNotBeFound(String filter) throws Exception {
        restAlVueVueViConditionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlVueVueViConditionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlVueVueViCondition() throws Exception {
        // Get the alVueVueViCondition
        restAlVueVueViConditionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlVueVueViCondition() throws Exception {
        // Initialize the database
        insertedAlVueVueViCondition = alVueVueViConditionRepository.saveAndFlush(alVueVueViCondition);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alVueVueViCondition
        AlVueVueViCondition updatedAlVueVueViCondition = alVueVueViConditionRepository.findById(alVueVueViCondition.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlVueVueViCondition are not directly saved in db
        em.detach(updatedAlVueVueViCondition);
        updatedAlVueVueViCondition.subjectType(UPDATED_SUBJECT_TYPE).subject(UPDATED_SUBJECT).action(UPDATED_ACTION).note(UPDATED_NOTE);

        restAlVueVueViConditionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlVueVueViCondition.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlVueVueViCondition))
            )
            .andExpect(status().isOk());

        // Validate the AlVueVueViCondition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlVueVueViConditionToMatchAllProperties(updatedAlVueVueViCondition);
    }

    @Test
    @Transactional
    void putNonExistingAlVueVueViCondition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alVueVueViCondition.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlVueVueViConditionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alVueVueViCondition.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alVueVueViCondition))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlVueVueViCondition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlVueVueViCondition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alVueVueViCondition.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlVueVueViConditionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alVueVueViCondition))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlVueVueViCondition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlVueVueViCondition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alVueVueViCondition.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlVueVueViConditionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alVueVueViCondition)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlVueVueViCondition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlVueVueViConditionWithPatch() throws Exception {
        // Initialize the database
        insertedAlVueVueViCondition = alVueVueViConditionRepository.saveAndFlush(alVueVueViCondition);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alVueVueViCondition using partial update
        AlVueVueViCondition partialUpdatedAlVueVueViCondition = new AlVueVueViCondition();
        partialUpdatedAlVueVueViCondition.setId(alVueVueViCondition.getId());

        partialUpdatedAlVueVueViCondition.subjectType(UPDATED_SUBJECT_TYPE);

        restAlVueVueViConditionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlVueVueViCondition.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlVueVueViCondition))
            )
            .andExpect(status().isOk());

        // Validate the AlVueVueViCondition in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlVueVueViConditionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlVueVueViCondition, alVueVueViCondition),
            getPersistedAlVueVueViCondition(alVueVueViCondition)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlVueVueViConditionWithPatch() throws Exception {
        // Initialize the database
        insertedAlVueVueViCondition = alVueVueViConditionRepository.saveAndFlush(alVueVueViCondition);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alVueVueViCondition using partial update
        AlVueVueViCondition partialUpdatedAlVueVueViCondition = new AlVueVueViCondition();
        partialUpdatedAlVueVueViCondition.setId(alVueVueViCondition.getId());

        partialUpdatedAlVueVueViCondition
            .subjectType(UPDATED_SUBJECT_TYPE)
            .subject(UPDATED_SUBJECT)
            .action(UPDATED_ACTION)
            .note(UPDATED_NOTE);

        restAlVueVueViConditionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlVueVueViCondition.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlVueVueViCondition))
            )
            .andExpect(status().isOk());

        // Validate the AlVueVueViCondition in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlVueVueViConditionUpdatableFieldsEquals(
            partialUpdatedAlVueVueViCondition,
            getPersistedAlVueVueViCondition(partialUpdatedAlVueVueViCondition)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAlVueVueViCondition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alVueVueViCondition.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlVueVueViConditionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alVueVueViCondition.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alVueVueViCondition))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlVueVueViCondition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlVueVueViCondition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alVueVueViCondition.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlVueVueViConditionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alVueVueViCondition))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlVueVueViCondition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlVueVueViCondition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alVueVueViCondition.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlVueVueViConditionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alVueVueViCondition)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlVueVueViCondition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlVueVueViCondition() throws Exception {
        // Initialize the database
        insertedAlVueVueViCondition = alVueVueViConditionRepository.saveAndFlush(alVueVueViCondition);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alVueVueViCondition
        restAlVueVueViConditionMockMvc
            .perform(delete(ENTITY_API_URL_ID, alVueVueViCondition.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alVueVueViConditionRepository.count();
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

    protected AlVueVueViCondition getPersistedAlVueVueViCondition(AlVueVueViCondition alVueVueViCondition) {
        return alVueVueViConditionRepository.findById(alVueVueViCondition.getId()).orElseThrow();
    }

    protected void assertPersistedAlVueVueViConditionToMatchAllProperties(AlVueVueViCondition expectedAlVueVueViCondition) {
        assertAlVueVueViConditionAllPropertiesEquals(
            expectedAlVueVueViCondition,
            getPersistedAlVueVueViCondition(expectedAlVueVueViCondition)
        );
    }

    protected void assertPersistedAlVueVueViConditionToMatchUpdatableProperties(AlVueVueViCondition expectedAlVueVueViCondition) {
        assertAlVueVueViConditionAllUpdatablePropertiesEquals(
            expectedAlVueVueViCondition,
            getPersistedAlVueVueViCondition(expectedAlVueVueViCondition)
        );
    }
}
