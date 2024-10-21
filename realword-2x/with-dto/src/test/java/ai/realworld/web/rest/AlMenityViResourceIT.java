package ai.realworld.web.rest;

import static ai.realworld.domain.AlMenityViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlMenityVi;
import ai.realworld.domain.AlProProVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.enumeration.PeteType;
import ai.realworld.repository.AlMenityViRepository;
import ai.realworld.service.dto.AlMenityViDTO;
import ai.realworld.service.mapper.AlMenityViMapper;
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
 * Integration tests for the {@link AlMenityViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlMenityViResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ICON_SVG = "AAAAAAAAAA";
    private static final String UPDATED_ICON_SVG = "BBBBBBBBBB";

    private static final PeteType DEFAULT_PROPERTY_TYPE = PeteType.VILLA;
    private static final PeteType UPDATED_PROPERTY_TYPE = PeteType.ROOM;

    private static final String ENTITY_API_URL = "/api/al-menity-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlMenityViRepository alMenityViRepository;

    @Autowired
    private AlMenityViMapper alMenityViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlMenityViMockMvc;

    private AlMenityVi alMenityVi;

    private AlMenityVi insertedAlMenityVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlMenityVi createEntity() {
        return new AlMenityVi().name(DEFAULT_NAME).iconSvg(DEFAULT_ICON_SVG).propertyType(DEFAULT_PROPERTY_TYPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlMenityVi createUpdatedEntity() {
        return new AlMenityVi().name(UPDATED_NAME).iconSvg(UPDATED_ICON_SVG).propertyType(UPDATED_PROPERTY_TYPE);
    }

    @BeforeEach
    public void initTest() {
        alMenityVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlMenityVi != null) {
            alMenityViRepository.delete(insertedAlMenityVi);
            insertedAlMenityVi = null;
        }
    }

    @Test
    @Transactional
    void createAlMenityVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlMenityVi
        AlMenityViDTO alMenityViDTO = alMenityViMapper.toDto(alMenityVi);
        var returnedAlMenityViDTO = om.readValue(
            restAlMenityViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alMenityViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlMenityViDTO.class
        );

        // Validate the AlMenityVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlMenityVi = alMenityViMapper.toEntity(returnedAlMenityViDTO);
        assertAlMenityViUpdatableFieldsEquals(returnedAlMenityVi, getPersistedAlMenityVi(returnedAlMenityVi));

        insertedAlMenityVi = returnedAlMenityVi;
    }

    @Test
    @Transactional
    void createAlMenityViWithExistingId() throws Exception {
        // Create the AlMenityVi with an existing ID
        alMenityVi.setId(1L);
        AlMenityViDTO alMenityViDTO = alMenityViMapper.toDto(alMenityVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlMenityViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alMenityViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlMenityVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alMenityVi.setName(null);

        // Create the AlMenityVi, which fails.
        AlMenityViDTO alMenityViDTO = alMenityViMapper.toDto(alMenityVi);

        restAlMenityViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alMenityViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlMenityVis() throws Exception {
        // Initialize the database
        insertedAlMenityVi = alMenityViRepository.saveAndFlush(alMenityVi);

        // Get all the alMenityViList
        restAlMenityViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alMenityVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].iconSvg").value(hasItem(DEFAULT_ICON_SVG)))
            .andExpect(jsonPath("$.[*].propertyType").value(hasItem(DEFAULT_PROPERTY_TYPE.toString())));
    }

    @Test
    @Transactional
    void getAlMenityVi() throws Exception {
        // Initialize the database
        insertedAlMenityVi = alMenityViRepository.saveAndFlush(alMenityVi);

        // Get the alMenityVi
        restAlMenityViMockMvc
            .perform(get(ENTITY_API_URL_ID, alMenityVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alMenityVi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.iconSvg").value(DEFAULT_ICON_SVG))
            .andExpect(jsonPath("$.propertyType").value(DEFAULT_PROPERTY_TYPE.toString()));
    }

    @Test
    @Transactional
    void getAlMenityVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlMenityVi = alMenityViRepository.saveAndFlush(alMenityVi);

        Long id = alMenityVi.getId();

        defaultAlMenityViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlMenityViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlMenityViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlMenityVisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlMenityVi = alMenityViRepository.saveAndFlush(alMenityVi);

        // Get all the alMenityViList where name equals to
        defaultAlMenityViFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlMenityVisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlMenityVi = alMenityViRepository.saveAndFlush(alMenityVi);

        // Get all the alMenityViList where name in
        defaultAlMenityViFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlMenityVisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlMenityVi = alMenityViRepository.saveAndFlush(alMenityVi);

        // Get all the alMenityViList where name is not null
        defaultAlMenityViFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAlMenityVisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlMenityVi = alMenityViRepository.saveAndFlush(alMenityVi);

        // Get all the alMenityViList where name contains
        defaultAlMenityViFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlMenityVisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlMenityVi = alMenityViRepository.saveAndFlush(alMenityVi);

        // Get all the alMenityViList where name does not contain
        defaultAlMenityViFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAlMenityVisByIconSvgIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlMenityVi = alMenityViRepository.saveAndFlush(alMenityVi);

        // Get all the alMenityViList where iconSvg equals to
        defaultAlMenityViFiltering("iconSvg.equals=" + DEFAULT_ICON_SVG, "iconSvg.equals=" + UPDATED_ICON_SVG);
    }

    @Test
    @Transactional
    void getAllAlMenityVisByIconSvgIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlMenityVi = alMenityViRepository.saveAndFlush(alMenityVi);

        // Get all the alMenityViList where iconSvg in
        defaultAlMenityViFiltering("iconSvg.in=" + DEFAULT_ICON_SVG + "," + UPDATED_ICON_SVG, "iconSvg.in=" + UPDATED_ICON_SVG);
    }

    @Test
    @Transactional
    void getAllAlMenityVisByIconSvgIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlMenityVi = alMenityViRepository.saveAndFlush(alMenityVi);

        // Get all the alMenityViList where iconSvg is not null
        defaultAlMenityViFiltering("iconSvg.specified=true", "iconSvg.specified=false");
    }

    @Test
    @Transactional
    void getAllAlMenityVisByIconSvgContainsSomething() throws Exception {
        // Initialize the database
        insertedAlMenityVi = alMenityViRepository.saveAndFlush(alMenityVi);

        // Get all the alMenityViList where iconSvg contains
        defaultAlMenityViFiltering("iconSvg.contains=" + DEFAULT_ICON_SVG, "iconSvg.contains=" + UPDATED_ICON_SVG);
    }

    @Test
    @Transactional
    void getAllAlMenityVisByIconSvgNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlMenityVi = alMenityViRepository.saveAndFlush(alMenityVi);

        // Get all the alMenityViList where iconSvg does not contain
        defaultAlMenityViFiltering("iconSvg.doesNotContain=" + UPDATED_ICON_SVG, "iconSvg.doesNotContain=" + DEFAULT_ICON_SVG);
    }

    @Test
    @Transactional
    void getAllAlMenityVisByPropertyTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlMenityVi = alMenityViRepository.saveAndFlush(alMenityVi);

        // Get all the alMenityViList where propertyType equals to
        defaultAlMenityViFiltering("propertyType.equals=" + DEFAULT_PROPERTY_TYPE, "propertyType.equals=" + UPDATED_PROPERTY_TYPE);
    }

    @Test
    @Transactional
    void getAllAlMenityVisByPropertyTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlMenityVi = alMenityViRepository.saveAndFlush(alMenityVi);

        // Get all the alMenityViList where propertyType in
        defaultAlMenityViFiltering(
            "propertyType.in=" + DEFAULT_PROPERTY_TYPE + "," + UPDATED_PROPERTY_TYPE,
            "propertyType.in=" + UPDATED_PROPERTY_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAlMenityVisByPropertyTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlMenityVi = alMenityViRepository.saveAndFlush(alMenityVi);

        // Get all the alMenityViList where propertyType is not null
        defaultAlMenityViFiltering("propertyType.specified=true", "propertyType.specified=false");
    }

    @Test
    @Transactional
    void getAllAlMenityVisByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alMenityViRepository.saveAndFlush(alMenityVi);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alMenityVi.setApplication(application);
        alMenityViRepository.saveAndFlush(alMenityVi);
        UUID applicationId = application.getId();
        // Get all the alMenityViList where application equals to applicationId
        defaultAlMenityViShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alMenityViList where application equals to UUID.randomUUID()
        defaultAlMenityViShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlMenityVisByPropertyProfileIsEqualToSomething() throws Exception {
        AlProProVi propertyProfile;
        if (TestUtil.findAll(em, AlProProVi.class).isEmpty()) {
            alMenityViRepository.saveAndFlush(alMenityVi);
            propertyProfile = AlProProViResourceIT.createEntity();
        } else {
            propertyProfile = TestUtil.findAll(em, AlProProVi.class).get(0);
        }
        em.persist(propertyProfile);
        em.flush();
        alMenityVi.addPropertyProfile(propertyProfile);
        alMenityViRepository.saveAndFlush(alMenityVi);
        UUID propertyProfileId = propertyProfile.getId();
        // Get all the alMenityViList where propertyProfile equals to propertyProfileId
        defaultAlMenityViShouldBeFound("propertyProfileId.equals=" + propertyProfileId);

        // Get all the alMenityViList where propertyProfile equals to UUID.randomUUID()
        defaultAlMenityViShouldNotBeFound("propertyProfileId.equals=" + UUID.randomUUID());
    }

    private void defaultAlMenityViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlMenityViShouldBeFound(shouldBeFound);
        defaultAlMenityViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlMenityViShouldBeFound(String filter) throws Exception {
        restAlMenityViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alMenityVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].iconSvg").value(hasItem(DEFAULT_ICON_SVG)))
            .andExpect(jsonPath("$.[*].propertyType").value(hasItem(DEFAULT_PROPERTY_TYPE.toString())));

        // Check, that the count call also returns 1
        restAlMenityViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlMenityViShouldNotBeFound(String filter) throws Exception {
        restAlMenityViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlMenityViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlMenityVi() throws Exception {
        // Get the alMenityVi
        restAlMenityViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlMenityVi() throws Exception {
        // Initialize the database
        insertedAlMenityVi = alMenityViRepository.saveAndFlush(alMenityVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alMenityVi
        AlMenityVi updatedAlMenityVi = alMenityViRepository.findById(alMenityVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlMenityVi are not directly saved in db
        em.detach(updatedAlMenityVi);
        updatedAlMenityVi.name(UPDATED_NAME).iconSvg(UPDATED_ICON_SVG).propertyType(UPDATED_PROPERTY_TYPE);
        AlMenityViDTO alMenityViDTO = alMenityViMapper.toDto(updatedAlMenityVi);

        restAlMenityViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alMenityViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alMenityViDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlMenityVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlMenityViToMatchAllProperties(updatedAlMenityVi);
    }

    @Test
    @Transactional
    void putNonExistingAlMenityVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alMenityVi.setId(longCount.incrementAndGet());

        // Create the AlMenityVi
        AlMenityViDTO alMenityViDTO = alMenityViMapper.toDto(alMenityVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlMenityViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alMenityViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alMenityViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlMenityVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlMenityVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alMenityVi.setId(longCount.incrementAndGet());

        // Create the AlMenityVi
        AlMenityViDTO alMenityViDTO = alMenityViMapper.toDto(alMenityVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlMenityViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alMenityViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlMenityVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlMenityVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alMenityVi.setId(longCount.incrementAndGet());

        // Create the AlMenityVi
        AlMenityViDTO alMenityViDTO = alMenityViMapper.toDto(alMenityVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlMenityViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alMenityViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlMenityVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlMenityViWithPatch() throws Exception {
        // Initialize the database
        insertedAlMenityVi = alMenityViRepository.saveAndFlush(alMenityVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alMenityVi using partial update
        AlMenityVi partialUpdatedAlMenityVi = new AlMenityVi();
        partialUpdatedAlMenityVi.setId(alMenityVi.getId());

        partialUpdatedAlMenityVi.iconSvg(UPDATED_ICON_SVG);

        restAlMenityViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlMenityVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlMenityVi))
            )
            .andExpect(status().isOk());

        // Validate the AlMenityVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlMenityViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlMenityVi, alMenityVi),
            getPersistedAlMenityVi(alMenityVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlMenityViWithPatch() throws Exception {
        // Initialize the database
        insertedAlMenityVi = alMenityViRepository.saveAndFlush(alMenityVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alMenityVi using partial update
        AlMenityVi partialUpdatedAlMenityVi = new AlMenityVi();
        partialUpdatedAlMenityVi.setId(alMenityVi.getId());

        partialUpdatedAlMenityVi.name(UPDATED_NAME).iconSvg(UPDATED_ICON_SVG).propertyType(UPDATED_PROPERTY_TYPE);

        restAlMenityViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlMenityVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlMenityVi))
            )
            .andExpect(status().isOk());

        // Validate the AlMenityVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlMenityViUpdatableFieldsEquals(partialUpdatedAlMenityVi, getPersistedAlMenityVi(partialUpdatedAlMenityVi));
    }

    @Test
    @Transactional
    void patchNonExistingAlMenityVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alMenityVi.setId(longCount.incrementAndGet());

        // Create the AlMenityVi
        AlMenityViDTO alMenityViDTO = alMenityViMapper.toDto(alMenityVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlMenityViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alMenityViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alMenityViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlMenityVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlMenityVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alMenityVi.setId(longCount.incrementAndGet());

        // Create the AlMenityVi
        AlMenityViDTO alMenityViDTO = alMenityViMapper.toDto(alMenityVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlMenityViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alMenityViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlMenityVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlMenityVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alMenityVi.setId(longCount.incrementAndGet());

        // Create the AlMenityVi
        AlMenityViDTO alMenityViDTO = alMenityViMapper.toDto(alMenityVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlMenityViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alMenityViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlMenityVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlMenityVi() throws Exception {
        // Initialize the database
        insertedAlMenityVi = alMenityViRepository.saveAndFlush(alMenityVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alMenityVi
        restAlMenityViMockMvc
            .perform(delete(ENTITY_API_URL_ID, alMenityVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alMenityViRepository.count();
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

    protected AlMenityVi getPersistedAlMenityVi(AlMenityVi alMenityVi) {
        return alMenityViRepository.findById(alMenityVi.getId()).orElseThrow();
    }

    protected void assertPersistedAlMenityViToMatchAllProperties(AlMenityVi expectedAlMenityVi) {
        assertAlMenityViAllPropertiesEquals(expectedAlMenityVi, getPersistedAlMenityVi(expectedAlMenityVi));
    }

    protected void assertPersistedAlMenityViToMatchUpdatableProperties(AlMenityVi expectedAlMenityVi) {
        assertAlMenityViAllUpdatablePropertiesEquals(expectedAlMenityVi, getPersistedAlMenityVi(expectedAlMenityVi));
    }
}
