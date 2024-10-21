package ai.realworld.web.rest;

import static ai.realworld.domain.AlPacinoVoucherViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlPacino;
import ai.realworld.domain.AlPacinoVoucherVi;
import ai.realworld.domain.AlVueVueVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.repository.AlPacinoVoucherViRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link AlPacinoVoucherViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlPacinoVoucherViResourceIT {

    private static final String DEFAULT_SOURCE_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_URL = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_URL = "BBBBBBBBBB";

    private static final Instant DEFAULT_COLLECTED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_COLLECTED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/al-pacino-voucher-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlPacinoVoucherViRepository alPacinoVoucherViRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlPacinoVoucherViMockMvc;

    private AlPacinoVoucherVi alPacinoVoucherVi;

    private AlPacinoVoucherVi insertedAlPacinoVoucherVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPacinoVoucherVi createEntity() {
        return new AlPacinoVoucherVi()
            .sourceTitle(DEFAULT_SOURCE_TITLE)
            .sourceUrl(DEFAULT_SOURCE_URL)
            .collectedDate(DEFAULT_COLLECTED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPacinoVoucherVi createUpdatedEntity() {
        return new AlPacinoVoucherVi()
            .sourceTitle(UPDATED_SOURCE_TITLE)
            .sourceUrl(UPDATED_SOURCE_URL)
            .collectedDate(UPDATED_COLLECTED_DATE);
    }

    @BeforeEach
    public void initTest() {
        alPacinoVoucherVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlPacinoVoucherVi != null) {
            alPacinoVoucherViRepository.delete(insertedAlPacinoVoucherVi);
            insertedAlPacinoVoucherVi = null;
        }
    }

    @Test
    @Transactional
    void createAlPacinoVoucherVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlPacinoVoucherVi
        var returnedAlPacinoVoucherVi = om.readValue(
            restAlPacinoVoucherViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPacinoVoucherVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlPacinoVoucherVi.class
        );

        // Validate the AlPacinoVoucherVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlPacinoVoucherViUpdatableFieldsEquals(returnedAlPacinoVoucherVi, getPersistedAlPacinoVoucherVi(returnedAlPacinoVoucherVi));

        insertedAlPacinoVoucherVi = returnedAlPacinoVoucherVi;
    }

    @Test
    @Transactional
    void createAlPacinoVoucherViWithExistingId() throws Exception {
        // Create the AlPacinoVoucherVi with an existing ID
        insertedAlPacinoVoucherVi = alPacinoVoucherViRepository.saveAndFlush(alPacinoVoucherVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlPacinoVoucherViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPacinoVoucherVi)))
            .andExpect(status().isBadRequest());

        // Validate the AlPacinoVoucherVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAlPacinoVoucherVis() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucherVi = alPacinoVoucherViRepository.saveAndFlush(alPacinoVoucherVi);

        // Get all the alPacinoVoucherViList
        restAlPacinoVoucherViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPacinoVoucherVi.getId().toString())))
            .andExpect(jsonPath("$.[*].sourceTitle").value(hasItem(DEFAULT_SOURCE_TITLE)))
            .andExpect(jsonPath("$.[*].sourceUrl").value(hasItem(DEFAULT_SOURCE_URL)))
            .andExpect(jsonPath("$.[*].collectedDate").value(hasItem(DEFAULT_COLLECTED_DATE.toString())));
    }

    @Test
    @Transactional
    void getAlPacinoVoucherVi() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucherVi = alPacinoVoucherViRepository.saveAndFlush(alPacinoVoucherVi);

        // Get the alPacinoVoucherVi
        restAlPacinoVoucherViMockMvc
            .perform(get(ENTITY_API_URL_ID, alPacinoVoucherVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alPacinoVoucherVi.getId().toString()))
            .andExpect(jsonPath("$.sourceTitle").value(DEFAULT_SOURCE_TITLE))
            .andExpect(jsonPath("$.sourceUrl").value(DEFAULT_SOURCE_URL))
            .andExpect(jsonPath("$.collectedDate").value(DEFAULT_COLLECTED_DATE.toString()));
    }

    @Test
    @Transactional
    void getAlPacinoVoucherVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucherVi = alPacinoVoucherViRepository.saveAndFlush(alPacinoVoucherVi);

        UUID id = alPacinoVoucherVi.getId();

        defaultAlPacinoVoucherViFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllAlPacinoVoucherVisBySourceTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucherVi = alPacinoVoucherViRepository.saveAndFlush(alPacinoVoucherVi);

        // Get all the alPacinoVoucherViList where sourceTitle equals to
        defaultAlPacinoVoucherViFiltering("sourceTitle.equals=" + DEFAULT_SOURCE_TITLE, "sourceTitle.equals=" + UPDATED_SOURCE_TITLE);
    }

    @Test
    @Transactional
    void getAllAlPacinoVoucherVisBySourceTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucherVi = alPacinoVoucherViRepository.saveAndFlush(alPacinoVoucherVi);

        // Get all the alPacinoVoucherViList where sourceTitle in
        defaultAlPacinoVoucherViFiltering(
            "sourceTitle.in=" + DEFAULT_SOURCE_TITLE + "," + UPDATED_SOURCE_TITLE,
            "sourceTitle.in=" + UPDATED_SOURCE_TITLE
        );
    }

    @Test
    @Transactional
    void getAllAlPacinoVoucherVisBySourceTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucherVi = alPacinoVoucherViRepository.saveAndFlush(alPacinoVoucherVi);

        // Get all the alPacinoVoucherViList where sourceTitle is not null
        defaultAlPacinoVoucherViFiltering("sourceTitle.specified=true", "sourceTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPacinoVoucherVisBySourceTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucherVi = alPacinoVoucherViRepository.saveAndFlush(alPacinoVoucherVi);

        // Get all the alPacinoVoucherViList where sourceTitle contains
        defaultAlPacinoVoucherViFiltering("sourceTitle.contains=" + DEFAULT_SOURCE_TITLE, "sourceTitle.contains=" + UPDATED_SOURCE_TITLE);
    }

    @Test
    @Transactional
    void getAllAlPacinoVoucherVisBySourceTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucherVi = alPacinoVoucherViRepository.saveAndFlush(alPacinoVoucherVi);

        // Get all the alPacinoVoucherViList where sourceTitle does not contain
        defaultAlPacinoVoucherViFiltering(
            "sourceTitle.doesNotContain=" + UPDATED_SOURCE_TITLE,
            "sourceTitle.doesNotContain=" + DEFAULT_SOURCE_TITLE
        );
    }

    @Test
    @Transactional
    void getAllAlPacinoVoucherVisBySourceUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucherVi = alPacinoVoucherViRepository.saveAndFlush(alPacinoVoucherVi);

        // Get all the alPacinoVoucherViList where sourceUrl equals to
        defaultAlPacinoVoucherViFiltering("sourceUrl.equals=" + DEFAULT_SOURCE_URL, "sourceUrl.equals=" + UPDATED_SOURCE_URL);
    }

    @Test
    @Transactional
    void getAllAlPacinoVoucherVisBySourceUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucherVi = alPacinoVoucherViRepository.saveAndFlush(alPacinoVoucherVi);

        // Get all the alPacinoVoucherViList where sourceUrl in
        defaultAlPacinoVoucherViFiltering(
            "sourceUrl.in=" + DEFAULT_SOURCE_URL + "," + UPDATED_SOURCE_URL,
            "sourceUrl.in=" + UPDATED_SOURCE_URL
        );
    }

    @Test
    @Transactional
    void getAllAlPacinoVoucherVisBySourceUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucherVi = alPacinoVoucherViRepository.saveAndFlush(alPacinoVoucherVi);

        // Get all the alPacinoVoucherViList where sourceUrl is not null
        defaultAlPacinoVoucherViFiltering("sourceUrl.specified=true", "sourceUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPacinoVoucherVisBySourceUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucherVi = alPacinoVoucherViRepository.saveAndFlush(alPacinoVoucherVi);

        // Get all the alPacinoVoucherViList where sourceUrl contains
        defaultAlPacinoVoucherViFiltering("sourceUrl.contains=" + DEFAULT_SOURCE_URL, "sourceUrl.contains=" + UPDATED_SOURCE_URL);
    }

    @Test
    @Transactional
    void getAllAlPacinoVoucherVisBySourceUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucherVi = alPacinoVoucherViRepository.saveAndFlush(alPacinoVoucherVi);

        // Get all the alPacinoVoucherViList where sourceUrl does not contain
        defaultAlPacinoVoucherViFiltering(
            "sourceUrl.doesNotContain=" + UPDATED_SOURCE_URL,
            "sourceUrl.doesNotContain=" + DEFAULT_SOURCE_URL
        );
    }

    @Test
    @Transactional
    void getAllAlPacinoVoucherVisByCollectedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucherVi = alPacinoVoucherViRepository.saveAndFlush(alPacinoVoucherVi);

        // Get all the alPacinoVoucherViList where collectedDate equals to
        defaultAlPacinoVoucherViFiltering(
            "collectedDate.equals=" + DEFAULT_COLLECTED_DATE,
            "collectedDate.equals=" + UPDATED_COLLECTED_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlPacinoVoucherVisByCollectedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucherVi = alPacinoVoucherViRepository.saveAndFlush(alPacinoVoucherVi);

        // Get all the alPacinoVoucherViList where collectedDate in
        defaultAlPacinoVoucherViFiltering(
            "collectedDate.in=" + DEFAULT_COLLECTED_DATE + "," + UPDATED_COLLECTED_DATE,
            "collectedDate.in=" + UPDATED_COLLECTED_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlPacinoVoucherVisByCollectedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucherVi = alPacinoVoucherViRepository.saveAndFlush(alPacinoVoucherVi);

        // Get all the alPacinoVoucherViList where collectedDate is not null
        defaultAlPacinoVoucherViFiltering("collectedDate.specified=true", "collectedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPacinoVoucherVisByUserIsEqualToSomething() throws Exception {
        AlPacino user;
        if (TestUtil.findAll(em, AlPacino.class).isEmpty()) {
            alPacinoVoucherViRepository.saveAndFlush(alPacinoVoucherVi);
            user = AlPacinoResourceIT.createEntity();
        } else {
            user = TestUtil.findAll(em, AlPacino.class).get(0);
        }
        em.persist(user);
        em.flush();
        alPacinoVoucherVi.setUser(user);
        alPacinoVoucherViRepository.saveAndFlush(alPacinoVoucherVi);
        UUID userId = user.getId();
        // Get all the alPacinoVoucherViList where user equals to userId
        defaultAlPacinoVoucherViShouldBeFound("userId.equals=" + userId);

        // Get all the alPacinoVoucherViList where user equals to UUID.randomUUID()
        defaultAlPacinoVoucherViShouldNotBeFound("userId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlPacinoVoucherVisByVoucherIsEqualToSomething() throws Exception {
        AlVueVueVi voucher;
        if (TestUtil.findAll(em, AlVueVueVi.class).isEmpty()) {
            alPacinoVoucherViRepository.saveAndFlush(alPacinoVoucherVi);
            voucher = AlVueVueViResourceIT.createEntity();
        } else {
            voucher = TestUtil.findAll(em, AlVueVueVi.class).get(0);
        }
        em.persist(voucher);
        em.flush();
        alPacinoVoucherVi.setVoucher(voucher);
        alPacinoVoucherViRepository.saveAndFlush(alPacinoVoucherVi);
        UUID voucherId = voucher.getId();
        // Get all the alPacinoVoucherViList where voucher equals to voucherId
        defaultAlPacinoVoucherViShouldBeFound("voucherId.equals=" + voucherId);

        // Get all the alPacinoVoucherViList where voucher equals to UUID.randomUUID()
        defaultAlPacinoVoucherViShouldNotBeFound("voucherId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlPacinoVoucherVisByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alPacinoVoucherViRepository.saveAndFlush(alPacinoVoucherVi);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alPacinoVoucherVi.setApplication(application);
        alPacinoVoucherViRepository.saveAndFlush(alPacinoVoucherVi);
        UUID applicationId = application.getId();
        // Get all the alPacinoVoucherViList where application equals to applicationId
        defaultAlPacinoVoucherViShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alPacinoVoucherViList where application equals to UUID.randomUUID()
        defaultAlPacinoVoucherViShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlPacinoVoucherViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlPacinoVoucherViShouldBeFound(shouldBeFound);
        defaultAlPacinoVoucherViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlPacinoVoucherViShouldBeFound(String filter) throws Exception {
        restAlPacinoVoucherViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPacinoVoucherVi.getId().toString())))
            .andExpect(jsonPath("$.[*].sourceTitle").value(hasItem(DEFAULT_SOURCE_TITLE)))
            .andExpect(jsonPath("$.[*].sourceUrl").value(hasItem(DEFAULT_SOURCE_URL)))
            .andExpect(jsonPath("$.[*].collectedDate").value(hasItem(DEFAULT_COLLECTED_DATE.toString())));

        // Check, that the count call also returns 1
        restAlPacinoVoucherViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlPacinoVoucherViShouldNotBeFound(String filter) throws Exception {
        restAlPacinoVoucherViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlPacinoVoucherViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlPacinoVoucherVi() throws Exception {
        // Get the alPacinoVoucherVi
        restAlPacinoVoucherViMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlPacinoVoucherVi() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucherVi = alPacinoVoucherViRepository.saveAndFlush(alPacinoVoucherVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPacinoVoucherVi
        AlPacinoVoucherVi updatedAlPacinoVoucherVi = alPacinoVoucherViRepository.findById(alPacinoVoucherVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlPacinoVoucherVi are not directly saved in db
        em.detach(updatedAlPacinoVoucherVi);
        updatedAlPacinoVoucherVi.sourceTitle(UPDATED_SOURCE_TITLE).sourceUrl(UPDATED_SOURCE_URL).collectedDate(UPDATED_COLLECTED_DATE);

        restAlPacinoVoucherViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlPacinoVoucherVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlPacinoVoucherVi))
            )
            .andExpect(status().isOk());

        // Validate the AlPacinoVoucherVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlPacinoVoucherViToMatchAllProperties(updatedAlPacinoVoucherVi);
    }

    @Test
    @Transactional
    void putNonExistingAlPacinoVoucherVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacinoVoucherVi.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPacinoVoucherViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alPacinoVoucherVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPacinoVoucherVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPacinoVoucherVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlPacinoVoucherVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacinoVoucherVi.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPacinoVoucherViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPacinoVoucherVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPacinoVoucherVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlPacinoVoucherVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacinoVoucherVi.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPacinoVoucherViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPacinoVoucherVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPacinoVoucherVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlPacinoVoucherViWithPatch() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucherVi = alPacinoVoucherViRepository.saveAndFlush(alPacinoVoucherVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPacinoVoucherVi using partial update
        AlPacinoVoucherVi partialUpdatedAlPacinoVoucherVi = new AlPacinoVoucherVi();
        partialUpdatedAlPacinoVoucherVi.setId(alPacinoVoucherVi.getId());

        partialUpdatedAlPacinoVoucherVi
            .sourceTitle(UPDATED_SOURCE_TITLE)
            .sourceUrl(UPDATED_SOURCE_URL)
            .collectedDate(UPDATED_COLLECTED_DATE);

        restAlPacinoVoucherViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPacinoVoucherVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPacinoVoucherVi))
            )
            .andExpect(status().isOk());

        // Validate the AlPacinoVoucherVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPacinoVoucherViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlPacinoVoucherVi, alPacinoVoucherVi),
            getPersistedAlPacinoVoucherVi(alPacinoVoucherVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlPacinoVoucherViWithPatch() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucherVi = alPacinoVoucherViRepository.saveAndFlush(alPacinoVoucherVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPacinoVoucherVi using partial update
        AlPacinoVoucherVi partialUpdatedAlPacinoVoucherVi = new AlPacinoVoucherVi();
        partialUpdatedAlPacinoVoucherVi.setId(alPacinoVoucherVi.getId());

        partialUpdatedAlPacinoVoucherVi
            .sourceTitle(UPDATED_SOURCE_TITLE)
            .sourceUrl(UPDATED_SOURCE_URL)
            .collectedDate(UPDATED_COLLECTED_DATE);

        restAlPacinoVoucherViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPacinoVoucherVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPacinoVoucherVi))
            )
            .andExpect(status().isOk());

        // Validate the AlPacinoVoucherVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPacinoVoucherViUpdatableFieldsEquals(
            partialUpdatedAlPacinoVoucherVi,
            getPersistedAlPacinoVoucherVi(partialUpdatedAlPacinoVoucherVi)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAlPacinoVoucherVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacinoVoucherVi.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPacinoVoucherViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alPacinoVoucherVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPacinoVoucherVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPacinoVoucherVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlPacinoVoucherVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacinoVoucherVi.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPacinoVoucherViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPacinoVoucherVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPacinoVoucherVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlPacinoVoucherVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacinoVoucherVi.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPacinoVoucherViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alPacinoVoucherVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPacinoVoucherVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlPacinoVoucherVi() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucherVi = alPacinoVoucherViRepository.saveAndFlush(alPacinoVoucherVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alPacinoVoucherVi
        restAlPacinoVoucherViMockMvc
            .perform(delete(ENTITY_API_URL_ID, alPacinoVoucherVi.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alPacinoVoucherViRepository.count();
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

    protected AlPacinoVoucherVi getPersistedAlPacinoVoucherVi(AlPacinoVoucherVi alPacinoVoucherVi) {
        return alPacinoVoucherViRepository.findById(alPacinoVoucherVi.getId()).orElseThrow();
    }

    protected void assertPersistedAlPacinoVoucherViToMatchAllProperties(AlPacinoVoucherVi expectedAlPacinoVoucherVi) {
        assertAlPacinoVoucherViAllPropertiesEquals(expectedAlPacinoVoucherVi, getPersistedAlPacinoVoucherVi(expectedAlPacinoVoucherVi));
    }

    protected void assertPersistedAlPacinoVoucherViToMatchUpdatableProperties(AlPacinoVoucherVi expectedAlPacinoVoucherVi) {
        assertAlPacinoVoucherViAllUpdatablePropertiesEquals(
            expectedAlPacinoVoucherVi,
            getPersistedAlPacinoVoucherVi(expectedAlPacinoVoucherVi)
        );
    }
}
