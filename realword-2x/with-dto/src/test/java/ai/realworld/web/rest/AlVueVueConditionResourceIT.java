package ai.realworld.web.rest;

import static ai.realworld.domain.AlVueVueConditionAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlVueVue;
import ai.realworld.domain.AlVueVueCondition;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.enumeration.AlcountSubjectivity;
import ai.realworld.domain.enumeration.NeonEction;
import ai.realworld.repository.AlVueVueConditionRepository;
import ai.realworld.service.dto.AlVueVueConditionDTO;
import ai.realworld.service.mapper.AlVueVueConditionMapper;
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
 * Integration tests for the {@link AlVueVueConditionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlVueVueConditionResourceIT {

    private static final AlcountSubjectivity DEFAULT_SUBJECT_TYPE = AlcountSubjectivity.PRODUCT;
    private static final AlcountSubjectivity UPDATED_SUBJECT_TYPE = AlcountSubjectivity.CATEGORY;

    private static final Long DEFAULT_SUBJECT = 1L;
    private static final Long UPDATED_SUBJECT = 2L;
    private static final Long SMALLER_SUBJECT = 1L - 1L;

    private static final NeonEction DEFAULT_ACTION = NeonEction.INCLUDE;
    private static final NeonEction UPDATED_ACTION = NeonEction.EXCLUDE;

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/al-vue-vue-conditions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlVueVueConditionRepository alVueVueConditionRepository;

    @Autowired
    private AlVueVueConditionMapper alVueVueConditionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlVueVueConditionMockMvc;

    private AlVueVueCondition alVueVueCondition;

    private AlVueVueCondition insertedAlVueVueCondition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlVueVueCondition createEntity() {
        return new AlVueVueCondition().subjectType(DEFAULT_SUBJECT_TYPE).subject(DEFAULT_SUBJECT).action(DEFAULT_ACTION).note(DEFAULT_NOTE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlVueVueCondition createUpdatedEntity() {
        return new AlVueVueCondition().subjectType(UPDATED_SUBJECT_TYPE).subject(UPDATED_SUBJECT).action(UPDATED_ACTION).note(UPDATED_NOTE);
    }

    @BeforeEach
    public void initTest() {
        alVueVueCondition = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlVueVueCondition != null) {
            alVueVueConditionRepository.delete(insertedAlVueVueCondition);
            insertedAlVueVueCondition = null;
        }
    }

    @Test
    @Transactional
    void createAlVueVueCondition() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlVueVueCondition
        AlVueVueConditionDTO alVueVueConditionDTO = alVueVueConditionMapper.toDto(alVueVueCondition);
        var returnedAlVueVueConditionDTO = om.readValue(
            restAlVueVueConditionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alVueVueConditionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlVueVueConditionDTO.class
        );

        // Validate the AlVueVueCondition in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlVueVueCondition = alVueVueConditionMapper.toEntity(returnedAlVueVueConditionDTO);
        assertAlVueVueConditionUpdatableFieldsEquals(returnedAlVueVueCondition, getPersistedAlVueVueCondition(returnedAlVueVueCondition));

        insertedAlVueVueCondition = returnedAlVueVueCondition;
    }

    @Test
    @Transactional
    void createAlVueVueConditionWithExistingId() throws Exception {
        // Create the AlVueVueCondition with an existing ID
        alVueVueCondition.setId(1L);
        AlVueVueConditionDTO alVueVueConditionDTO = alVueVueConditionMapper.toDto(alVueVueCondition);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlVueVueConditionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alVueVueConditionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlVueVueCondition in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAlVueVueConditions() throws Exception {
        // Initialize the database
        insertedAlVueVueCondition = alVueVueConditionRepository.saveAndFlush(alVueVueCondition);

        // Get all the alVueVueConditionList
        restAlVueVueConditionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alVueVueCondition.getId().intValue())))
            .andExpect(jsonPath("$.[*].subjectType").value(hasItem(DEFAULT_SUBJECT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT.intValue())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @Test
    @Transactional
    void getAlVueVueCondition() throws Exception {
        // Initialize the database
        insertedAlVueVueCondition = alVueVueConditionRepository.saveAndFlush(alVueVueCondition);

        // Get the alVueVueCondition
        restAlVueVueConditionMockMvc
            .perform(get(ENTITY_API_URL_ID, alVueVueCondition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alVueVueCondition.getId().intValue()))
            .andExpect(jsonPath("$.subjectType").value(DEFAULT_SUBJECT_TYPE.toString()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT.intValue()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }

    @Test
    @Transactional
    void getAlVueVueConditionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlVueVueCondition = alVueVueConditionRepository.saveAndFlush(alVueVueCondition);

        Long id = alVueVueCondition.getId();

        defaultAlVueVueConditionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlVueVueConditionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlVueVueConditionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlVueVueConditionsBySubjectTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueCondition = alVueVueConditionRepository.saveAndFlush(alVueVueCondition);

        // Get all the alVueVueConditionList where subjectType equals to
        defaultAlVueVueConditionFiltering("subjectType.equals=" + DEFAULT_SUBJECT_TYPE, "subjectType.equals=" + UPDATED_SUBJECT_TYPE);
    }

    @Test
    @Transactional
    void getAllAlVueVueConditionsBySubjectTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVueCondition = alVueVueConditionRepository.saveAndFlush(alVueVueCondition);

        // Get all the alVueVueConditionList where subjectType in
        defaultAlVueVueConditionFiltering(
            "subjectType.in=" + DEFAULT_SUBJECT_TYPE + "," + UPDATED_SUBJECT_TYPE,
            "subjectType.in=" + UPDATED_SUBJECT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueConditionsBySubjectTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVueCondition = alVueVueConditionRepository.saveAndFlush(alVueVueCondition);

        // Get all the alVueVueConditionList where subjectType is not null
        defaultAlVueVueConditionFiltering("subjectType.specified=true", "subjectType.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVueConditionsBySubjectIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueCondition = alVueVueConditionRepository.saveAndFlush(alVueVueCondition);

        // Get all the alVueVueConditionList where subject equals to
        defaultAlVueVueConditionFiltering("subject.equals=" + DEFAULT_SUBJECT, "subject.equals=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllAlVueVueConditionsBySubjectIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVueCondition = alVueVueConditionRepository.saveAndFlush(alVueVueCondition);

        // Get all the alVueVueConditionList where subject in
        defaultAlVueVueConditionFiltering("subject.in=" + DEFAULT_SUBJECT + "," + UPDATED_SUBJECT, "subject.in=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllAlVueVueConditionsBySubjectIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVueCondition = alVueVueConditionRepository.saveAndFlush(alVueVueCondition);

        // Get all the alVueVueConditionList where subject is not null
        defaultAlVueVueConditionFiltering("subject.specified=true", "subject.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVueConditionsBySubjectIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueCondition = alVueVueConditionRepository.saveAndFlush(alVueVueCondition);

        // Get all the alVueVueConditionList where subject is greater than or equal to
        defaultAlVueVueConditionFiltering("subject.greaterThanOrEqual=" + DEFAULT_SUBJECT, "subject.greaterThanOrEqual=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllAlVueVueConditionsBySubjectIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueCondition = alVueVueConditionRepository.saveAndFlush(alVueVueCondition);

        // Get all the alVueVueConditionList where subject is less than or equal to
        defaultAlVueVueConditionFiltering("subject.lessThanOrEqual=" + DEFAULT_SUBJECT, "subject.lessThanOrEqual=" + SMALLER_SUBJECT);
    }

    @Test
    @Transactional
    void getAllAlVueVueConditionsBySubjectIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueCondition = alVueVueConditionRepository.saveAndFlush(alVueVueCondition);

        // Get all the alVueVueConditionList where subject is less than
        defaultAlVueVueConditionFiltering("subject.lessThan=" + UPDATED_SUBJECT, "subject.lessThan=" + DEFAULT_SUBJECT);
    }

    @Test
    @Transactional
    void getAllAlVueVueConditionsBySubjectIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueCondition = alVueVueConditionRepository.saveAndFlush(alVueVueCondition);

        // Get all the alVueVueConditionList where subject is greater than
        defaultAlVueVueConditionFiltering("subject.greaterThan=" + SMALLER_SUBJECT, "subject.greaterThan=" + DEFAULT_SUBJECT);
    }

    @Test
    @Transactional
    void getAllAlVueVueConditionsByActionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueCondition = alVueVueConditionRepository.saveAndFlush(alVueVueCondition);

        // Get all the alVueVueConditionList where action equals to
        defaultAlVueVueConditionFiltering("action.equals=" + DEFAULT_ACTION, "action.equals=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllAlVueVueConditionsByActionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVueCondition = alVueVueConditionRepository.saveAndFlush(alVueVueCondition);

        // Get all the alVueVueConditionList where action in
        defaultAlVueVueConditionFiltering("action.in=" + DEFAULT_ACTION + "," + UPDATED_ACTION, "action.in=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllAlVueVueConditionsByActionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVueCondition = alVueVueConditionRepository.saveAndFlush(alVueVueCondition);

        // Get all the alVueVueConditionList where action is not null
        defaultAlVueVueConditionFiltering("action.specified=true", "action.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVueConditionsByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueCondition = alVueVueConditionRepository.saveAndFlush(alVueVueCondition);

        // Get all the alVueVueConditionList where note equals to
        defaultAlVueVueConditionFiltering("note.equals=" + DEFAULT_NOTE, "note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllAlVueVueConditionsByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVueCondition = alVueVueConditionRepository.saveAndFlush(alVueVueCondition);

        // Get all the alVueVueConditionList where note in
        defaultAlVueVueConditionFiltering("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE, "note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllAlVueVueConditionsByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVueCondition = alVueVueConditionRepository.saveAndFlush(alVueVueCondition);

        // Get all the alVueVueConditionList where note is not null
        defaultAlVueVueConditionFiltering("note.specified=true", "note.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVueConditionsByNoteContainsSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueCondition = alVueVueConditionRepository.saveAndFlush(alVueVueCondition);

        // Get all the alVueVueConditionList where note contains
        defaultAlVueVueConditionFiltering("note.contains=" + DEFAULT_NOTE, "note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllAlVueVueConditionsByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueCondition = alVueVueConditionRepository.saveAndFlush(alVueVueCondition);

        // Get all the alVueVueConditionList where note does not contain
        defaultAlVueVueConditionFiltering("note.doesNotContain=" + UPDATED_NOTE, "note.doesNotContain=" + DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void getAllAlVueVueConditionsByParentIsEqualToSomething() throws Exception {
        AlVueVue parent;
        if (TestUtil.findAll(em, AlVueVue.class).isEmpty()) {
            alVueVueConditionRepository.saveAndFlush(alVueVueCondition);
            parent = AlVueVueResourceIT.createEntity();
        } else {
            parent = TestUtil.findAll(em, AlVueVue.class).get(0);
        }
        em.persist(parent);
        em.flush();
        alVueVueCondition.setParent(parent);
        alVueVueConditionRepository.saveAndFlush(alVueVueCondition);
        UUID parentId = parent.getId();
        // Get all the alVueVueConditionList where parent equals to parentId
        defaultAlVueVueConditionShouldBeFound("parentId.equals=" + parentId);

        // Get all the alVueVueConditionList where parent equals to UUID.randomUUID()
        defaultAlVueVueConditionShouldNotBeFound("parentId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlVueVueConditionsByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alVueVueConditionRepository.saveAndFlush(alVueVueCondition);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alVueVueCondition.setApplication(application);
        alVueVueConditionRepository.saveAndFlush(alVueVueCondition);
        UUID applicationId = application.getId();
        // Get all the alVueVueConditionList where application equals to applicationId
        defaultAlVueVueConditionShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alVueVueConditionList where application equals to UUID.randomUUID()
        defaultAlVueVueConditionShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlVueVueConditionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlVueVueConditionShouldBeFound(shouldBeFound);
        defaultAlVueVueConditionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlVueVueConditionShouldBeFound(String filter) throws Exception {
        restAlVueVueConditionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alVueVueCondition.getId().intValue())))
            .andExpect(jsonPath("$.[*].subjectType").value(hasItem(DEFAULT_SUBJECT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT.intValue())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));

        // Check, that the count call also returns 1
        restAlVueVueConditionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlVueVueConditionShouldNotBeFound(String filter) throws Exception {
        restAlVueVueConditionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlVueVueConditionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlVueVueCondition() throws Exception {
        // Get the alVueVueCondition
        restAlVueVueConditionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlVueVueCondition() throws Exception {
        // Initialize the database
        insertedAlVueVueCondition = alVueVueConditionRepository.saveAndFlush(alVueVueCondition);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alVueVueCondition
        AlVueVueCondition updatedAlVueVueCondition = alVueVueConditionRepository.findById(alVueVueCondition.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlVueVueCondition are not directly saved in db
        em.detach(updatedAlVueVueCondition);
        updatedAlVueVueCondition.subjectType(UPDATED_SUBJECT_TYPE).subject(UPDATED_SUBJECT).action(UPDATED_ACTION).note(UPDATED_NOTE);
        AlVueVueConditionDTO alVueVueConditionDTO = alVueVueConditionMapper.toDto(updatedAlVueVueCondition);

        restAlVueVueConditionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alVueVueConditionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alVueVueConditionDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlVueVueCondition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlVueVueConditionToMatchAllProperties(updatedAlVueVueCondition);
    }

    @Test
    @Transactional
    void putNonExistingAlVueVueCondition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alVueVueCondition.setId(longCount.incrementAndGet());

        // Create the AlVueVueCondition
        AlVueVueConditionDTO alVueVueConditionDTO = alVueVueConditionMapper.toDto(alVueVueCondition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlVueVueConditionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alVueVueConditionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alVueVueConditionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlVueVueCondition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlVueVueCondition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alVueVueCondition.setId(longCount.incrementAndGet());

        // Create the AlVueVueCondition
        AlVueVueConditionDTO alVueVueConditionDTO = alVueVueConditionMapper.toDto(alVueVueCondition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlVueVueConditionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alVueVueConditionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlVueVueCondition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlVueVueCondition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alVueVueCondition.setId(longCount.incrementAndGet());

        // Create the AlVueVueCondition
        AlVueVueConditionDTO alVueVueConditionDTO = alVueVueConditionMapper.toDto(alVueVueCondition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlVueVueConditionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alVueVueConditionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlVueVueCondition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlVueVueConditionWithPatch() throws Exception {
        // Initialize the database
        insertedAlVueVueCondition = alVueVueConditionRepository.saveAndFlush(alVueVueCondition);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alVueVueCondition using partial update
        AlVueVueCondition partialUpdatedAlVueVueCondition = new AlVueVueCondition();
        partialUpdatedAlVueVueCondition.setId(alVueVueCondition.getId());

        partialUpdatedAlVueVueCondition.action(UPDATED_ACTION).note(UPDATED_NOTE);

        restAlVueVueConditionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlVueVueCondition.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlVueVueCondition))
            )
            .andExpect(status().isOk());

        // Validate the AlVueVueCondition in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlVueVueConditionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlVueVueCondition, alVueVueCondition),
            getPersistedAlVueVueCondition(alVueVueCondition)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlVueVueConditionWithPatch() throws Exception {
        // Initialize the database
        insertedAlVueVueCondition = alVueVueConditionRepository.saveAndFlush(alVueVueCondition);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alVueVueCondition using partial update
        AlVueVueCondition partialUpdatedAlVueVueCondition = new AlVueVueCondition();
        partialUpdatedAlVueVueCondition.setId(alVueVueCondition.getId());

        partialUpdatedAlVueVueCondition
            .subjectType(UPDATED_SUBJECT_TYPE)
            .subject(UPDATED_SUBJECT)
            .action(UPDATED_ACTION)
            .note(UPDATED_NOTE);

        restAlVueVueConditionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlVueVueCondition.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlVueVueCondition))
            )
            .andExpect(status().isOk());

        // Validate the AlVueVueCondition in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlVueVueConditionUpdatableFieldsEquals(
            partialUpdatedAlVueVueCondition,
            getPersistedAlVueVueCondition(partialUpdatedAlVueVueCondition)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAlVueVueCondition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alVueVueCondition.setId(longCount.incrementAndGet());

        // Create the AlVueVueCondition
        AlVueVueConditionDTO alVueVueConditionDTO = alVueVueConditionMapper.toDto(alVueVueCondition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlVueVueConditionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alVueVueConditionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alVueVueConditionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlVueVueCondition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlVueVueCondition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alVueVueCondition.setId(longCount.incrementAndGet());

        // Create the AlVueVueCondition
        AlVueVueConditionDTO alVueVueConditionDTO = alVueVueConditionMapper.toDto(alVueVueCondition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlVueVueConditionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alVueVueConditionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlVueVueCondition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlVueVueCondition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alVueVueCondition.setId(longCount.incrementAndGet());

        // Create the AlVueVueCondition
        AlVueVueConditionDTO alVueVueConditionDTO = alVueVueConditionMapper.toDto(alVueVueCondition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlVueVueConditionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alVueVueConditionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlVueVueCondition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlVueVueCondition() throws Exception {
        // Initialize the database
        insertedAlVueVueCondition = alVueVueConditionRepository.saveAndFlush(alVueVueCondition);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alVueVueCondition
        restAlVueVueConditionMockMvc
            .perform(delete(ENTITY_API_URL_ID, alVueVueCondition.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alVueVueConditionRepository.count();
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

    protected AlVueVueCondition getPersistedAlVueVueCondition(AlVueVueCondition alVueVueCondition) {
        return alVueVueConditionRepository.findById(alVueVueCondition.getId()).orElseThrow();
    }

    protected void assertPersistedAlVueVueConditionToMatchAllProperties(AlVueVueCondition expectedAlVueVueCondition) {
        assertAlVueVueConditionAllPropertiesEquals(expectedAlVueVueCondition, getPersistedAlVueVueCondition(expectedAlVueVueCondition));
    }

    protected void assertPersistedAlVueVueConditionToMatchUpdatableProperties(AlVueVueCondition expectedAlVueVueCondition) {
        assertAlVueVueConditionAllUpdatablePropertiesEquals(
            expectedAlVueVueCondition,
            getPersistedAlVueVueCondition(expectedAlVueVueCondition)
        );
    }
}
