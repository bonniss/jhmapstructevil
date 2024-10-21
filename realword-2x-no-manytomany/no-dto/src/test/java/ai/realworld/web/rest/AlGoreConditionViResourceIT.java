package ai.realworld.web.rest;

import static ai.realworld.domain.AlGoreConditionViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlGore;
import ai.realworld.domain.AlGoreConditionVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.enumeration.AlcountSubjectivity;
import ai.realworld.domain.enumeration.NeonEction;
import ai.realworld.repository.AlGoreConditionViRepository;
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
 * Integration tests for the {@link AlGoreConditionViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlGoreConditionViResourceIT {

    private static final AlcountSubjectivity DEFAULT_SUBJECT_TYPE = AlcountSubjectivity.PRODUCT;
    private static final AlcountSubjectivity UPDATED_SUBJECT_TYPE = AlcountSubjectivity.CATEGORY;

    private static final Long DEFAULT_SUBJECT = 1L;
    private static final Long UPDATED_SUBJECT = 2L;
    private static final Long SMALLER_SUBJECT = 1L - 1L;

    private static final NeonEction DEFAULT_ACTION = NeonEction.INCLUDE;
    private static final NeonEction UPDATED_ACTION = NeonEction.EXCLUDE;

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/al-gore-condition-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlGoreConditionViRepository alGoreConditionViRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlGoreConditionViMockMvc;

    private AlGoreConditionVi alGoreConditionVi;

    private AlGoreConditionVi insertedAlGoreConditionVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlGoreConditionVi createEntity() {
        return new AlGoreConditionVi().subjectType(DEFAULT_SUBJECT_TYPE).subject(DEFAULT_SUBJECT).action(DEFAULT_ACTION).note(DEFAULT_NOTE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlGoreConditionVi createUpdatedEntity() {
        return new AlGoreConditionVi().subjectType(UPDATED_SUBJECT_TYPE).subject(UPDATED_SUBJECT).action(UPDATED_ACTION).note(UPDATED_NOTE);
    }

    @BeforeEach
    public void initTest() {
        alGoreConditionVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlGoreConditionVi != null) {
            alGoreConditionViRepository.delete(insertedAlGoreConditionVi);
            insertedAlGoreConditionVi = null;
        }
    }

    @Test
    @Transactional
    void createAlGoreConditionVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlGoreConditionVi
        var returnedAlGoreConditionVi = om.readValue(
            restAlGoreConditionViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alGoreConditionVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlGoreConditionVi.class
        );

        // Validate the AlGoreConditionVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlGoreConditionViUpdatableFieldsEquals(returnedAlGoreConditionVi, getPersistedAlGoreConditionVi(returnedAlGoreConditionVi));

        insertedAlGoreConditionVi = returnedAlGoreConditionVi;
    }

    @Test
    @Transactional
    void createAlGoreConditionViWithExistingId() throws Exception {
        // Create the AlGoreConditionVi with an existing ID
        alGoreConditionVi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlGoreConditionViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alGoreConditionVi)))
            .andExpect(status().isBadRequest());

        // Validate the AlGoreConditionVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAlGoreConditionVis() throws Exception {
        // Initialize the database
        insertedAlGoreConditionVi = alGoreConditionViRepository.saveAndFlush(alGoreConditionVi);

        // Get all the alGoreConditionViList
        restAlGoreConditionViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alGoreConditionVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].subjectType").value(hasItem(DEFAULT_SUBJECT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT.intValue())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @Test
    @Transactional
    void getAlGoreConditionVi() throws Exception {
        // Initialize the database
        insertedAlGoreConditionVi = alGoreConditionViRepository.saveAndFlush(alGoreConditionVi);

        // Get the alGoreConditionVi
        restAlGoreConditionViMockMvc
            .perform(get(ENTITY_API_URL_ID, alGoreConditionVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alGoreConditionVi.getId().intValue()))
            .andExpect(jsonPath("$.subjectType").value(DEFAULT_SUBJECT_TYPE.toString()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT.intValue()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }

    @Test
    @Transactional
    void getAlGoreConditionVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlGoreConditionVi = alGoreConditionViRepository.saveAndFlush(alGoreConditionVi);

        Long id = alGoreConditionVi.getId();

        defaultAlGoreConditionViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlGoreConditionViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlGoreConditionViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlGoreConditionVisBySubjectTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoreConditionVi = alGoreConditionViRepository.saveAndFlush(alGoreConditionVi);

        // Get all the alGoreConditionViList where subjectType equals to
        defaultAlGoreConditionViFiltering("subjectType.equals=" + DEFAULT_SUBJECT_TYPE, "subjectType.equals=" + UPDATED_SUBJECT_TYPE);
    }

    @Test
    @Transactional
    void getAllAlGoreConditionVisBySubjectTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlGoreConditionVi = alGoreConditionViRepository.saveAndFlush(alGoreConditionVi);

        // Get all the alGoreConditionViList where subjectType in
        defaultAlGoreConditionViFiltering(
            "subjectType.in=" + DEFAULT_SUBJECT_TYPE + "," + UPDATED_SUBJECT_TYPE,
            "subjectType.in=" + UPDATED_SUBJECT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAlGoreConditionVisBySubjectTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlGoreConditionVi = alGoreConditionViRepository.saveAndFlush(alGoreConditionVi);

        // Get all the alGoreConditionViList where subjectType is not null
        defaultAlGoreConditionViFiltering("subjectType.specified=true", "subjectType.specified=false");
    }

    @Test
    @Transactional
    void getAllAlGoreConditionVisBySubjectIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoreConditionVi = alGoreConditionViRepository.saveAndFlush(alGoreConditionVi);

        // Get all the alGoreConditionViList where subject equals to
        defaultAlGoreConditionViFiltering("subject.equals=" + DEFAULT_SUBJECT, "subject.equals=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllAlGoreConditionVisBySubjectIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlGoreConditionVi = alGoreConditionViRepository.saveAndFlush(alGoreConditionVi);

        // Get all the alGoreConditionViList where subject in
        defaultAlGoreConditionViFiltering("subject.in=" + DEFAULT_SUBJECT + "," + UPDATED_SUBJECT, "subject.in=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllAlGoreConditionVisBySubjectIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlGoreConditionVi = alGoreConditionViRepository.saveAndFlush(alGoreConditionVi);

        // Get all the alGoreConditionViList where subject is not null
        defaultAlGoreConditionViFiltering("subject.specified=true", "subject.specified=false");
    }

    @Test
    @Transactional
    void getAllAlGoreConditionVisBySubjectIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoreConditionVi = alGoreConditionViRepository.saveAndFlush(alGoreConditionVi);

        // Get all the alGoreConditionViList where subject is greater than or equal to
        defaultAlGoreConditionViFiltering("subject.greaterThanOrEqual=" + DEFAULT_SUBJECT, "subject.greaterThanOrEqual=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllAlGoreConditionVisBySubjectIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoreConditionVi = alGoreConditionViRepository.saveAndFlush(alGoreConditionVi);

        // Get all the alGoreConditionViList where subject is less than or equal to
        defaultAlGoreConditionViFiltering("subject.lessThanOrEqual=" + DEFAULT_SUBJECT, "subject.lessThanOrEqual=" + SMALLER_SUBJECT);
    }

    @Test
    @Transactional
    void getAllAlGoreConditionVisBySubjectIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlGoreConditionVi = alGoreConditionViRepository.saveAndFlush(alGoreConditionVi);

        // Get all the alGoreConditionViList where subject is less than
        defaultAlGoreConditionViFiltering("subject.lessThan=" + UPDATED_SUBJECT, "subject.lessThan=" + DEFAULT_SUBJECT);
    }

    @Test
    @Transactional
    void getAllAlGoreConditionVisBySubjectIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlGoreConditionVi = alGoreConditionViRepository.saveAndFlush(alGoreConditionVi);

        // Get all the alGoreConditionViList where subject is greater than
        defaultAlGoreConditionViFiltering("subject.greaterThan=" + SMALLER_SUBJECT, "subject.greaterThan=" + DEFAULT_SUBJECT);
    }

    @Test
    @Transactional
    void getAllAlGoreConditionVisByActionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoreConditionVi = alGoreConditionViRepository.saveAndFlush(alGoreConditionVi);

        // Get all the alGoreConditionViList where action equals to
        defaultAlGoreConditionViFiltering("action.equals=" + DEFAULT_ACTION, "action.equals=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllAlGoreConditionVisByActionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlGoreConditionVi = alGoreConditionViRepository.saveAndFlush(alGoreConditionVi);

        // Get all the alGoreConditionViList where action in
        defaultAlGoreConditionViFiltering("action.in=" + DEFAULT_ACTION + "," + UPDATED_ACTION, "action.in=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllAlGoreConditionVisByActionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlGoreConditionVi = alGoreConditionViRepository.saveAndFlush(alGoreConditionVi);

        // Get all the alGoreConditionViList where action is not null
        defaultAlGoreConditionViFiltering("action.specified=true", "action.specified=false");
    }

    @Test
    @Transactional
    void getAllAlGoreConditionVisByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlGoreConditionVi = alGoreConditionViRepository.saveAndFlush(alGoreConditionVi);

        // Get all the alGoreConditionViList where note equals to
        defaultAlGoreConditionViFiltering("note.equals=" + DEFAULT_NOTE, "note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllAlGoreConditionVisByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlGoreConditionVi = alGoreConditionViRepository.saveAndFlush(alGoreConditionVi);

        // Get all the alGoreConditionViList where note in
        defaultAlGoreConditionViFiltering("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE, "note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllAlGoreConditionVisByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlGoreConditionVi = alGoreConditionViRepository.saveAndFlush(alGoreConditionVi);

        // Get all the alGoreConditionViList where note is not null
        defaultAlGoreConditionViFiltering("note.specified=true", "note.specified=false");
    }

    @Test
    @Transactional
    void getAllAlGoreConditionVisByNoteContainsSomething() throws Exception {
        // Initialize the database
        insertedAlGoreConditionVi = alGoreConditionViRepository.saveAndFlush(alGoreConditionVi);

        // Get all the alGoreConditionViList where note contains
        defaultAlGoreConditionViFiltering("note.contains=" + DEFAULT_NOTE, "note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllAlGoreConditionVisByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlGoreConditionVi = alGoreConditionViRepository.saveAndFlush(alGoreConditionVi);

        // Get all the alGoreConditionViList where note does not contain
        defaultAlGoreConditionViFiltering("note.doesNotContain=" + UPDATED_NOTE, "note.doesNotContain=" + DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void getAllAlGoreConditionVisByParentIsEqualToSomething() throws Exception {
        AlGore parent;
        if (TestUtil.findAll(em, AlGore.class).isEmpty()) {
            alGoreConditionViRepository.saveAndFlush(alGoreConditionVi);
            parent = AlGoreResourceIT.createEntity();
        } else {
            parent = TestUtil.findAll(em, AlGore.class).get(0);
        }
        em.persist(parent);
        em.flush();
        alGoreConditionVi.setParent(parent);
        alGoreConditionViRepository.saveAndFlush(alGoreConditionVi);
        Long parentId = parent.getId();
        // Get all the alGoreConditionViList where parent equals to parentId
        defaultAlGoreConditionViShouldBeFound("parentId.equals=" + parentId);

        // Get all the alGoreConditionViList where parent equals to (parentId + 1)
        defaultAlGoreConditionViShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    @Test
    @Transactional
    void getAllAlGoreConditionVisByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alGoreConditionViRepository.saveAndFlush(alGoreConditionVi);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alGoreConditionVi.setApplication(application);
        alGoreConditionViRepository.saveAndFlush(alGoreConditionVi);
        UUID applicationId = application.getId();
        // Get all the alGoreConditionViList where application equals to applicationId
        defaultAlGoreConditionViShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alGoreConditionViList where application equals to UUID.randomUUID()
        defaultAlGoreConditionViShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlGoreConditionViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlGoreConditionViShouldBeFound(shouldBeFound);
        defaultAlGoreConditionViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlGoreConditionViShouldBeFound(String filter) throws Exception {
        restAlGoreConditionViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alGoreConditionVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].subjectType").value(hasItem(DEFAULT_SUBJECT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT.intValue())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));

        // Check, that the count call also returns 1
        restAlGoreConditionViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlGoreConditionViShouldNotBeFound(String filter) throws Exception {
        restAlGoreConditionViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlGoreConditionViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlGoreConditionVi() throws Exception {
        // Get the alGoreConditionVi
        restAlGoreConditionViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlGoreConditionVi() throws Exception {
        // Initialize the database
        insertedAlGoreConditionVi = alGoreConditionViRepository.saveAndFlush(alGoreConditionVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alGoreConditionVi
        AlGoreConditionVi updatedAlGoreConditionVi = alGoreConditionViRepository.findById(alGoreConditionVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlGoreConditionVi are not directly saved in db
        em.detach(updatedAlGoreConditionVi);
        updatedAlGoreConditionVi.subjectType(UPDATED_SUBJECT_TYPE).subject(UPDATED_SUBJECT).action(UPDATED_ACTION).note(UPDATED_NOTE);

        restAlGoreConditionViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlGoreConditionVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlGoreConditionVi))
            )
            .andExpect(status().isOk());

        // Validate the AlGoreConditionVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlGoreConditionViToMatchAllProperties(updatedAlGoreConditionVi);
    }

    @Test
    @Transactional
    void putNonExistingAlGoreConditionVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alGoreConditionVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlGoreConditionViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alGoreConditionVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alGoreConditionVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlGoreConditionVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlGoreConditionVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alGoreConditionVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlGoreConditionViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alGoreConditionVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlGoreConditionVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlGoreConditionVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alGoreConditionVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlGoreConditionViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alGoreConditionVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlGoreConditionVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlGoreConditionViWithPatch() throws Exception {
        // Initialize the database
        insertedAlGoreConditionVi = alGoreConditionViRepository.saveAndFlush(alGoreConditionVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alGoreConditionVi using partial update
        AlGoreConditionVi partialUpdatedAlGoreConditionVi = new AlGoreConditionVi();
        partialUpdatedAlGoreConditionVi.setId(alGoreConditionVi.getId());

        partialUpdatedAlGoreConditionVi.subjectType(UPDATED_SUBJECT_TYPE).note(UPDATED_NOTE);

        restAlGoreConditionViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlGoreConditionVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlGoreConditionVi))
            )
            .andExpect(status().isOk());

        // Validate the AlGoreConditionVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlGoreConditionViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlGoreConditionVi, alGoreConditionVi),
            getPersistedAlGoreConditionVi(alGoreConditionVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlGoreConditionViWithPatch() throws Exception {
        // Initialize the database
        insertedAlGoreConditionVi = alGoreConditionViRepository.saveAndFlush(alGoreConditionVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alGoreConditionVi using partial update
        AlGoreConditionVi partialUpdatedAlGoreConditionVi = new AlGoreConditionVi();
        partialUpdatedAlGoreConditionVi.setId(alGoreConditionVi.getId());

        partialUpdatedAlGoreConditionVi
            .subjectType(UPDATED_SUBJECT_TYPE)
            .subject(UPDATED_SUBJECT)
            .action(UPDATED_ACTION)
            .note(UPDATED_NOTE);

        restAlGoreConditionViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlGoreConditionVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlGoreConditionVi))
            )
            .andExpect(status().isOk());

        // Validate the AlGoreConditionVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlGoreConditionViUpdatableFieldsEquals(
            partialUpdatedAlGoreConditionVi,
            getPersistedAlGoreConditionVi(partialUpdatedAlGoreConditionVi)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAlGoreConditionVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alGoreConditionVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlGoreConditionViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alGoreConditionVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alGoreConditionVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlGoreConditionVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlGoreConditionVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alGoreConditionVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlGoreConditionViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alGoreConditionVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlGoreConditionVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlGoreConditionVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alGoreConditionVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlGoreConditionViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alGoreConditionVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlGoreConditionVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlGoreConditionVi() throws Exception {
        // Initialize the database
        insertedAlGoreConditionVi = alGoreConditionViRepository.saveAndFlush(alGoreConditionVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alGoreConditionVi
        restAlGoreConditionViMockMvc
            .perform(delete(ENTITY_API_URL_ID, alGoreConditionVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alGoreConditionViRepository.count();
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

    protected AlGoreConditionVi getPersistedAlGoreConditionVi(AlGoreConditionVi alGoreConditionVi) {
        return alGoreConditionViRepository.findById(alGoreConditionVi.getId()).orElseThrow();
    }

    protected void assertPersistedAlGoreConditionViToMatchAllProperties(AlGoreConditionVi expectedAlGoreConditionVi) {
        assertAlGoreConditionViAllPropertiesEquals(expectedAlGoreConditionVi, getPersistedAlGoreConditionVi(expectedAlGoreConditionVi));
    }

    protected void assertPersistedAlGoreConditionViToMatchUpdatableProperties(AlGoreConditionVi expectedAlGoreConditionVi) {
        assertAlGoreConditionViAllUpdatablePropertiesEquals(
            expectedAlGoreConditionVi,
            getPersistedAlGoreConditionVi(expectedAlGoreConditionVi)
        );
    }
}
