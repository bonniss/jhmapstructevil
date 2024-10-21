package ai.realworld.web.rest;

import static ai.realworld.domain.AlPacinoVoucherAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlPacino;
import ai.realworld.domain.AlPacinoVoucher;
import ai.realworld.domain.AlVueVue;
import ai.realworld.domain.JohnLennon;
import ai.realworld.repository.AlPacinoVoucherRepository;
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
 * Integration tests for the {@link AlPacinoVoucherResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlPacinoVoucherResourceIT {

    private static final String DEFAULT_SOURCE_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_URL = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_URL = "BBBBBBBBBB";

    private static final Instant DEFAULT_COLLECTED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_COLLECTED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/al-pacino-vouchers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlPacinoVoucherRepository alPacinoVoucherRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlPacinoVoucherMockMvc;

    private AlPacinoVoucher alPacinoVoucher;

    private AlPacinoVoucher insertedAlPacinoVoucher;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPacinoVoucher createEntity() {
        return new AlPacinoVoucher().sourceTitle(DEFAULT_SOURCE_TITLE).sourceUrl(DEFAULT_SOURCE_URL).collectedDate(DEFAULT_COLLECTED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPacinoVoucher createUpdatedEntity() {
        return new AlPacinoVoucher().sourceTitle(UPDATED_SOURCE_TITLE).sourceUrl(UPDATED_SOURCE_URL).collectedDate(UPDATED_COLLECTED_DATE);
    }

    @BeforeEach
    public void initTest() {
        alPacinoVoucher = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlPacinoVoucher != null) {
            alPacinoVoucherRepository.delete(insertedAlPacinoVoucher);
            insertedAlPacinoVoucher = null;
        }
    }

    @Test
    @Transactional
    void createAlPacinoVoucher() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlPacinoVoucher
        var returnedAlPacinoVoucher = om.readValue(
            restAlPacinoVoucherMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPacinoVoucher)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlPacinoVoucher.class
        );

        // Validate the AlPacinoVoucher in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlPacinoVoucherUpdatableFieldsEquals(returnedAlPacinoVoucher, getPersistedAlPacinoVoucher(returnedAlPacinoVoucher));

        insertedAlPacinoVoucher = returnedAlPacinoVoucher;
    }

    @Test
    @Transactional
    void createAlPacinoVoucherWithExistingId() throws Exception {
        // Create the AlPacinoVoucher with an existing ID
        insertedAlPacinoVoucher = alPacinoVoucherRepository.saveAndFlush(alPacinoVoucher);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlPacinoVoucherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPacinoVoucher)))
            .andExpect(status().isBadRequest());

        // Validate the AlPacinoVoucher in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAlPacinoVouchers() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucher = alPacinoVoucherRepository.saveAndFlush(alPacinoVoucher);

        // Get all the alPacinoVoucherList
        restAlPacinoVoucherMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPacinoVoucher.getId().toString())))
            .andExpect(jsonPath("$.[*].sourceTitle").value(hasItem(DEFAULT_SOURCE_TITLE)))
            .andExpect(jsonPath("$.[*].sourceUrl").value(hasItem(DEFAULT_SOURCE_URL)))
            .andExpect(jsonPath("$.[*].collectedDate").value(hasItem(DEFAULT_COLLECTED_DATE.toString())));
    }

    @Test
    @Transactional
    void getAlPacinoVoucher() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucher = alPacinoVoucherRepository.saveAndFlush(alPacinoVoucher);

        // Get the alPacinoVoucher
        restAlPacinoVoucherMockMvc
            .perform(get(ENTITY_API_URL_ID, alPacinoVoucher.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alPacinoVoucher.getId().toString()))
            .andExpect(jsonPath("$.sourceTitle").value(DEFAULT_SOURCE_TITLE))
            .andExpect(jsonPath("$.sourceUrl").value(DEFAULT_SOURCE_URL))
            .andExpect(jsonPath("$.collectedDate").value(DEFAULT_COLLECTED_DATE.toString()));
    }

    @Test
    @Transactional
    void getAlPacinoVouchersByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucher = alPacinoVoucherRepository.saveAndFlush(alPacinoVoucher);

        UUID id = alPacinoVoucher.getId();

        defaultAlPacinoVoucherFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllAlPacinoVouchersBySourceTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucher = alPacinoVoucherRepository.saveAndFlush(alPacinoVoucher);

        // Get all the alPacinoVoucherList where sourceTitle equals to
        defaultAlPacinoVoucherFiltering("sourceTitle.equals=" + DEFAULT_SOURCE_TITLE, "sourceTitle.equals=" + UPDATED_SOURCE_TITLE);
    }

    @Test
    @Transactional
    void getAllAlPacinoVouchersBySourceTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucher = alPacinoVoucherRepository.saveAndFlush(alPacinoVoucher);

        // Get all the alPacinoVoucherList where sourceTitle in
        defaultAlPacinoVoucherFiltering(
            "sourceTitle.in=" + DEFAULT_SOURCE_TITLE + "," + UPDATED_SOURCE_TITLE,
            "sourceTitle.in=" + UPDATED_SOURCE_TITLE
        );
    }

    @Test
    @Transactional
    void getAllAlPacinoVouchersBySourceTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucher = alPacinoVoucherRepository.saveAndFlush(alPacinoVoucher);

        // Get all the alPacinoVoucherList where sourceTitle is not null
        defaultAlPacinoVoucherFiltering("sourceTitle.specified=true", "sourceTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPacinoVouchersBySourceTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucher = alPacinoVoucherRepository.saveAndFlush(alPacinoVoucher);

        // Get all the alPacinoVoucherList where sourceTitle contains
        defaultAlPacinoVoucherFiltering("sourceTitle.contains=" + DEFAULT_SOURCE_TITLE, "sourceTitle.contains=" + UPDATED_SOURCE_TITLE);
    }

    @Test
    @Transactional
    void getAllAlPacinoVouchersBySourceTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucher = alPacinoVoucherRepository.saveAndFlush(alPacinoVoucher);

        // Get all the alPacinoVoucherList where sourceTitle does not contain
        defaultAlPacinoVoucherFiltering(
            "sourceTitle.doesNotContain=" + UPDATED_SOURCE_TITLE,
            "sourceTitle.doesNotContain=" + DEFAULT_SOURCE_TITLE
        );
    }

    @Test
    @Transactional
    void getAllAlPacinoVouchersBySourceUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucher = alPacinoVoucherRepository.saveAndFlush(alPacinoVoucher);

        // Get all the alPacinoVoucherList where sourceUrl equals to
        defaultAlPacinoVoucherFiltering("sourceUrl.equals=" + DEFAULT_SOURCE_URL, "sourceUrl.equals=" + UPDATED_SOURCE_URL);
    }

    @Test
    @Transactional
    void getAllAlPacinoVouchersBySourceUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucher = alPacinoVoucherRepository.saveAndFlush(alPacinoVoucher);

        // Get all the alPacinoVoucherList where sourceUrl in
        defaultAlPacinoVoucherFiltering(
            "sourceUrl.in=" + DEFAULT_SOURCE_URL + "," + UPDATED_SOURCE_URL,
            "sourceUrl.in=" + UPDATED_SOURCE_URL
        );
    }

    @Test
    @Transactional
    void getAllAlPacinoVouchersBySourceUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucher = alPacinoVoucherRepository.saveAndFlush(alPacinoVoucher);

        // Get all the alPacinoVoucherList where sourceUrl is not null
        defaultAlPacinoVoucherFiltering("sourceUrl.specified=true", "sourceUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPacinoVouchersBySourceUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucher = alPacinoVoucherRepository.saveAndFlush(alPacinoVoucher);

        // Get all the alPacinoVoucherList where sourceUrl contains
        defaultAlPacinoVoucherFiltering("sourceUrl.contains=" + DEFAULT_SOURCE_URL, "sourceUrl.contains=" + UPDATED_SOURCE_URL);
    }

    @Test
    @Transactional
    void getAllAlPacinoVouchersBySourceUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucher = alPacinoVoucherRepository.saveAndFlush(alPacinoVoucher);

        // Get all the alPacinoVoucherList where sourceUrl does not contain
        defaultAlPacinoVoucherFiltering("sourceUrl.doesNotContain=" + UPDATED_SOURCE_URL, "sourceUrl.doesNotContain=" + DEFAULT_SOURCE_URL);
    }

    @Test
    @Transactional
    void getAllAlPacinoVouchersByCollectedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucher = alPacinoVoucherRepository.saveAndFlush(alPacinoVoucher);

        // Get all the alPacinoVoucherList where collectedDate equals to
        defaultAlPacinoVoucherFiltering("collectedDate.equals=" + DEFAULT_COLLECTED_DATE, "collectedDate.equals=" + UPDATED_COLLECTED_DATE);
    }

    @Test
    @Transactional
    void getAllAlPacinoVouchersByCollectedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucher = alPacinoVoucherRepository.saveAndFlush(alPacinoVoucher);

        // Get all the alPacinoVoucherList where collectedDate in
        defaultAlPacinoVoucherFiltering(
            "collectedDate.in=" + DEFAULT_COLLECTED_DATE + "," + UPDATED_COLLECTED_DATE,
            "collectedDate.in=" + UPDATED_COLLECTED_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlPacinoVouchersByCollectedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucher = alPacinoVoucherRepository.saveAndFlush(alPacinoVoucher);

        // Get all the alPacinoVoucherList where collectedDate is not null
        defaultAlPacinoVoucherFiltering("collectedDate.specified=true", "collectedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPacinoVouchersByUserIsEqualToSomething() throws Exception {
        AlPacino user;
        if (TestUtil.findAll(em, AlPacino.class).isEmpty()) {
            alPacinoVoucherRepository.saveAndFlush(alPacinoVoucher);
            user = AlPacinoResourceIT.createEntity();
        } else {
            user = TestUtil.findAll(em, AlPacino.class).get(0);
        }
        em.persist(user);
        em.flush();
        alPacinoVoucher.setUser(user);
        alPacinoVoucherRepository.saveAndFlush(alPacinoVoucher);
        UUID userId = user.getId();
        // Get all the alPacinoVoucherList where user equals to userId
        defaultAlPacinoVoucherShouldBeFound("userId.equals=" + userId);

        // Get all the alPacinoVoucherList where user equals to UUID.randomUUID()
        defaultAlPacinoVoucherShouldNotBeFound("userId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlPacinoVouchersByVoucherIsEqualToSomething() throws Exception {
        AlVueVue voucher;
        if (TestUtil.findAll(em, AlVueVue.class).isEmpty()) {
            alPacinoVoucherRepository.saveAndFlush(alPacinoVoucher);
            voucher = AlVueVueResourceIT.createEntity();
        } else {
            voucher = TestUtil.findAll(em, AlVueVue.class).get(0);
        }
        em.persist(voucher);
        em.flush();
        alPacinoVoucher.setVoucher(voucher);
        alPacinoVoucherRepository.saveAndFlush(alPacinoVoucher);
        UUID voucherId = voucher.getId();
        // Get all the alPacinoVoucherList where voucher equals to voucherId
        defaultAlPacinoVoucherShouldBeFound("voucherId.equals=" + voucherId);

        // Get all the alPacinoVoucherList where voucher equals to UUID.randomUUID()
        defaultAlPacinoVoucherShouldNotBeFound("voucherId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlPacinoVouchersByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alPacinoVoucherRepository.saveAndFlush(alPacinoVoucher);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alPacinoVoucher.setApplication(application);
        alPacinoVoucherRepository.saveAndFlush(alPacinoVoucher);
        UUID applicationId = application.getId();
        // Get all the alPacinoVoucherList where application equals to applicationId
        defaultAlPacinoVoucherShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alPacinoVoucherList where application equals to UUID.randomUUID()
        defaultAlPacinoVoucherShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlPacinoVoucherFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlPacinoVoucherShouldBeFound(shouldBeFound);
        defaultAlPacinoVoucherShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlPacinoVoucherShouldBeFound(String filter) throws Exception {
        restAlPacinoVoucherMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPacinoVoucher.getId().toString())))
            .andExpect(jsonPath("$.[*].sourceTitle").value(hasItem(DEFAULT_SOURCE_TITLE)))
            .andExpect(jsonPath("$.[*].sourceUrl").value(hasItem(DEFAULT_SOURCE_URL)))
            .andExpect(jsonPath("$.[*].collectedDate").value(hasItem(DEFAULT_COLLECTED_DATE.toString())));

        // Check, that the count call also returns 1
        restAlPacinoVoucherMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlPacinoVoucherShouldNotBeFound(String filter) throws Exception {
        restAlPacinoVoucherMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlPacinoVoucherMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlPacinoVoucher() throws Exception {
        // Get the alPacinoVoucher
        restAlPacinoVoucherMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlPacinoVoucher() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucher = alPacinoVoucherRepository.saveAndFlush(alPacinoVoucher);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPacinoVoucher
        AlPacinoVoucher updatedAlPacinoVoucher = alPacinoVoucherRepository.findById(alPacinoVoucher.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlPacinoVoucher are not directly saved in db
        em.detach(updatedAlPacinoVoucher);
        updatedAlPacinoVoucher.sourceTitle(UPDATED_SOURCE_TITLE).sourceUrl(UPDATED_SOURCE_URL).collectedDate(UPDATED_COLLECTED_DATE);

        restAlPacinoVoucherMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlPacinoVoucher.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlPacinoVoucher))
            )
            .andExpect(status().isOk());

        // Validate the AlPacinoVoucher in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlPacinoVoucherToMatchAllProperties(updatedAlPacinoVoucher);
    }

    @Test
    @Transactional
    void putNonExistingAlPacinoVoucher() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacinoVoucher.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPacinoVoucherMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alPacinoVoucher.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPacinoVoucher))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPacinoVoucher in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlPacinoVoucher() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacinoVoucher.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPacinoVoucherMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPacinoVoucher))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPacinoVoucher in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlPacinoVoucher() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacinoVoucher.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPacinoVoucherMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPacinoVoucher)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPacinoVoucher in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlPacinoVoucherWithPatch() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucher = alPacinoVoucherRepository.saveAndFlush(alPacinoVoucher);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPacinoVoucher using partial update
        AlPacinoVoucher partialUpdatedAlPacinoVoucher = new AlPacinoVoucher();
        partialUpdatedAlPacinoVoucher.setId(alPacinoVoucher.getId());

        partialUpdatedAlPacinoVoucher.sourceTitle(UPDATED_SOURCE_TITLE).collectedDate(UPDATED_COLLECTED_DATE);

        restAlPacinoVoucherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPacinoVoucher.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPacinoVoucher))
            )
            .andExpect(status().isOk());

        // Validate the AlPacinoVoucher in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPacinoVoucherUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlPacinoVoucher, alPacinoVoucher),
            getPersistedAlPacinoVoucher(alPacinoVoucher)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlPacinoVoucherWithPatch() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucher = alPacinoVoucherRepository.saveAndFlush(alPacinoVoucher);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPacinoVoucher using partial update
        AlPacinoVoucher partialUpdatedAlPacinoVoucher = new AlPacinoVoucher();
        partialUpdatedAlPacinoVoucher.setId(alPacinoVoucher.getId());

        partialUpdatedAlPacinoVoucher.sourceTitle(UPDATED_SOURCE_TITLE).sourceUrl(UPDATED_SOURCE_URL).collectedDate(UPDATED_COLLECTED_DATE);

        restAlPacinoVoucherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPacinoVoucher.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPacinoVoucher))
            )
            .andExpect(status().isOk());

        // Validate the AlPacinoVoucher in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPacinoVoucherUpdatableFieldsEquals(
            partialUpdatedAlPacinoVoucher,
            getPersistedAlPacinoVoucher(partialUpdatedAlPacinoVoucher)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAlPacinoVoucher() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacinoVoucher.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPacinoVoucherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alPacinoVoucher.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPacinoVoucher))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPacinoVoucher in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlPacinoVoucher() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacinoVoucher.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPacinoVoucherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPacinoVoucher))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPacinoVoucher in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlPacinoVoucher() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacinoVoucher.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPacinoVoucherMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alPacinoVoucher)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPacinoVoucher in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlPacinoVoucher() throws Exception {
        // Initialize the database
        insertedAlPacinoVoucher = alPacinoVoucherRepository.saveAndFlush(alPacinoVoucher);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alPacinoVoucher
        restAlPacinoVoucherMockMvc
            .perform(delete(ENTITY_API_URL_ID, alPacinoVoucher.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alPacinoVoucherRepository.count();
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

    protected AlPacinoVoucher getPersistedAlPacinoVoucher(AlPacinoVoucher alPacinoVoucher) {
        return alPacinoVoucherRepository.findById(alPacinoVoucher.getId()).orElseThrow();
    }

    protected void assertPersistedAlPacinoVoucherToMatchAllProperties(AlPacinoVoucher expectedAlPacinoVoucher) {
        assertAlPacinoVoucherAllPropertiesEquals(expectedAlPacinoVoucher, getPersistedAlPacinoVoucher(expectedAlPacinoVoucher));
    }

    protected void assertPersistedAlPacinoVoucherToMatchUpdatableProperties(AlPacinoVoucher expectedAlPacinoVoucher) {
        assertAlPacinoVoucherAllUpdatablePropertiesEquals(expectedAlPacinoVoucher, getPersistedAlPacinoVoucher(expectedAlPacinoVoucher));
    }
}
