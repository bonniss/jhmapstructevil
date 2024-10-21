package ai.realworld.web.rest;

import static ai.realworld.domain.AlPacinoAndreiRightHandViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlPacino;
import ai.realworld.domain.AlPacinoAndreiRightHandVi;
import ai.realworld.domain.AndreiRightHandVi;
import ai.realworld.repository.AlPacinoAndreiRightHandViRepository;
import ai.realworld.service.dto.AlPacinoAndreiRightHandViDTO;
import ai.realworld.service.mapper.AlPacinoAndreiRightHandViMapper;
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
 * Integration tests for the {@link AlPacinoAndreiRightHandViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlPacinoAndreiRightHandViResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DEFAULT = false;
    private static final Boolean UPDATED_IS_DEFAULT = true;

    private static final String ENTITY_API_URL = "/api/al-pacino-andrei-right-hand-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlPacinoAndreiRightHandViRepository alPacinoAndreiRightHandViRepository;

    @Autowired
    private AlPacinoAndreiRightHandViMapper alPacinoAndreiRightHandViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlPacinoAndreiRightHandViMockMvc;

    private AlPacinoAndreiRightHandVi alPacinoAndreiRightHandVi;

    private AlPacinoAndreiRightHandVi insertedAlPacinoAndreiRightHandVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPacinoAndreiRightHandVi createEntity() {
        return new AlPacinoAndreiRightHandVi().name(DEFAULT_NAME).isDefault(DEFAULT_IS_DEFAULT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPacinoAndreiRightHandVi createUpdatedEntity() {
        return new AlPacinoAndreiRightHandVi().name(UPDATED_NAME).isDefault(UPDATED_IS_DEFAULT);
    }

    @BeforeEach
    public void initTest() {
        alPacinoAndreiRightHandVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlPacinoAndreiRightHandVi != null) {
            alPacinoAndreiRightHandViRepository.delete(insertedAlPacinoAndreiRightHandVi);
            insertedAlPacinoAndreiRightHandVi = null;
        }
    }

    @Test
    @Transactional
    void createAlPacinoAndreiRightHandVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlPacinoAndreiRightHandVi
        AlPacinoAndreiRightHandViDTO alPacinoAndreiRightHandViDTO = alPacinoAndreiRightHandViMapper.toDto(alPacinoAndreiRightHandVi);
        var returnedAlPacinoAndreiRightHandViDTO = om.readValue(
            restAlPacinoAndreiRightHandViMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPacinoAndreiRightHandViDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlPacinoAndreiRightHandViDTO.class
        );

        // Validate the AlPacinoAndreiRightHandVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlPacinoAndreiRightHandVi = alPacinoAndreiRightHandViMapper.toEntity(returnedAlPacinoAndreiRightHandViDTO);
        assertAlPacinoAndreiRightHandViUpdatableFieldsEquals(
            returnedAlPacinoAndreiRightHandVi,
            getPersistedAlPacinoAndreiRightHandVi(returnedAlPacinoAndreiRightHandVi)
        );

        insertedAlPacinoAndreiRightHandVi = returnedAlPacinoAndreiRightHandVi;
    }

    @Test
    @Transactional
    void createAlPacinoAndreiRightHandViWithExistingId() throws Exception {
        // Create the AlPacinoAndreiRightHandVi with an existing ID
        alPacinoAndreiRightHandVi.setId(1L);
        AlPacinoAndreiRightHandViDTO alPacinoAndreiRightHandViDTO = alPacinoAndreiRightHandViMapper.toDto(alPacinoAndreiRightHandVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlPacinoAndreiRightHandViMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPacinoAndreiRightHandViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPacinoAndreiRightHandVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alPacinoAndreiRightHandVi.setName(null);

        // Create the AlPacinoAndreiRightHandVi, which fails.
        AlPacinoAndreiRightHandViDTO alPacinoAndreiRightHandViDTO = alPacinoAndreiRightHandViMapper.toDto(alPacinoAndreiRightHandVi);

        restAlPacinoAndreiRightHandViMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPacinoAndreiRightHandViDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlPacinoAndreiRightHandVis() throws Exception {
        // Initialize the database
        insertedAlPacinoAndreiRightHandVi = alPacinoAndreiRightHandViRepository.saveAndFlush(alPacinoAndreiRightHandVi);

        // Get all the alPacinoAndreiRightHandViList
        restAlPacinoAndreiRightHandViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPacinoAndreiRightHandVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT.booleanValue())));
    }

    @Test
    @Transactional
    void getAlPacinoAndreiRightHandVi() throws Exception {
        // Initialize the database
        insertedAlPacinoAndreiRightHandVi = alPacinoAndreiRightHandViRepository.saveAndFlush(alPacinoAndreiRightHandVi);

        // Get the alPacinoAndreiRightHandVi
        restAlPacinoAndreiRightHandViMockMvc
            .perform(get(ENTITY_API_URL_ID, alPacinoAndreiRightHandVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alPacinoAndreiRightHandVi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.isDefault").value(DEFAULT_IS_DEFAULT.booleanValue()));
    }

    @Test
    @Transactional
    void getAlPacinoAndreiRightHandVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlPacinoAndreiRightHandVi = alPacinoAndreiRightHandViRepository.saveAndFlush(alPacinoAndreiRightHandVi);

        Long id = alPacinoAndreiRightHandVi.getId();

        defaultAlPacinoAndreiRightHandViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlPacinoAndreiRightHandViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlPacinoAndreiRightHandViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlPacinoAndreiRightHandVisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoAndreiRightHandVi = alPacinoAndreiRightHandViRepository.saveAndFlush(alPacinoAndreiRightHandVi);

        // Get all the alPacinoAndreiRightHandViList where name equals to
        defaultAlPacinoAndreiRightHandViFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlPacinoAndreiRightHandVisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPacinoAndreiRightHandVi = alPacinoAndreiRightHandViRepository.saveAndFlush(alPacinoAndreiRightHandVi);

        // Get all the alPacinoAndreiRightHandViList where name in
        defaultAlPacinoAndreiRightHandViFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlPacinoAndreiRightHandVisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPacinoAndreiRightHandVi = alPacinoAndreiRightHandViRepository.saveAndFlush(alPacinoAndreiRightHandVi);

        // Get all the alPacinoAndreiRightHandViList where name is not null
        defaultAlPacinoAndreiRightHandViFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPacinoAndreiRightHandVisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoAndreiRightHandVi = alPacinoAndreiRightHandViRepository.saveAndFlush(alPacinoAndreiRightHandVi);

        // Get all the alPacinoAndreiRightHandViList where name contains
        defaultAlPacinoAndreiRightHandViFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlPacinoAndreiRightHandVisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoAndreiRightHandVi = alPacinoAndreiRightHandViRepository.saveAndFlush(alPacinoAndreiRightHandVi);

        // Get all the alPacinoAndreiRightHandViList where name does not contain
        defaultAlPacinoAndreiRightHandViFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAlPacinoAndreiRightHandVisByIsDefaultIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoAndreiRightHandVi = alPacinoAndreiRightHandViRepository.saveAndFlush(alPacinoAndreiRightHandVi);

        // Get all the alPacinoAndreiRightHandViList where isDefault equals to
        defaultAlPacinoAndreiRightHandViFiltering("isDefault.equals=" + DEFAULT_IS_DEFAULT, "isDefault.equals=" + UPDATED_IS_DEFAULT);
    }

    @Test
    @Transactional
    void getAllAlPacinoAndreiRightHandVisByIsDefaultIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPacinoAndreiRightHandVi = alPacinoAndreiRightHandViRepository.saveAndFlush(alPacinoAndreiRightHandVi);

        // Get all the alPacinoAndreiRightHandViList where isDefault in
        defaultAlPacinoAndreiRightHandViFiltering(
            "isDefault.in=" + DEFAULT_IS_DEFAULT + "," + UPDATED_IS_DEFAULT,
            "isDefault.in=" + UPDATED_IS_DEFAULT
        );
    }

    @Test
    @Transactional
    void getAllAlPacinoAndreiRightHandVisByIsDefaultIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPacinoAndreiRightHandVi = alPacinoAndreiRightHandViRepository.saveAndFlush(alPacinoAndreiRightHandVi);

        // Get all the alPacinoAndreiRightHandViList where isDefault is not null
        defaultAlPacinoAndreiRightHandViFiltering("isDefault.specified=true", "isDefault.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPacinoAndreiRightHandVisByUserIsEqualToSomething() throws Exception {
        AlPacino user;
        if (TestUtil.findAll(em, AlPacino.class).isEmpty()) {
            alPacinoAndreiRightHandViRepository.saveAndFlush(alPacinoAndreiRightHandVi);
            user = AlPacinoResourceIT.createEntity();
        } else {
            user = TestUtil.findAll(em, AlPacino.class).get(0);
        }
        em.persist(user);
        em.flush();
        alPacinoAndreiRightHandVi.setUser(user);
        alPacinoAndreiRightHandViRepository.saveAndFlush(alPacinoAndreiRightHandVi);
        UUID userId = user.getId();
        // Get all the alPacinoAndreiRightHandViList where user equals to userId
        defaultAlPacinoAndreiRightHandViShouldBeFound("userId.equals=" + userId);

        // Get all the alPacinoAndreiRightHandViList where user equals to UUID.randomUUID()
        defaultAlPacinoAndreiRightHandViShouldNotBeFound("userId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlPacinoAndreiRightHandVisByAddressIsEqualToSomething() throws Exception {
        AndreiRightHandVi address;
        if (TestUtil.findAll(em, AndreiRightHandVi.class).isEmpty()) {
            alPacinoAndreiRightHandViRepository.saveAndFlush(alPacinoAndreiRightHandVi);
            address = AndreiRightHandViResourceIT.createEntity();
        } else {
            address = TestUtil.findAll(em, AndreiRightHandVi.class).get(0);
        }
        em.persist(address);
        em.flush();
        alPacinoAndreiRightHandVi.setAddress(address);
        alPacinoAndreiRightHandViRepository.saveAndFlush(alPacinoAndreiRightHandVi);
        Long addressId = address.getId();
        // Get all the alPacinoAndreiRightHandViList where address equals to addressId
        defaultAlPacinoAndreiRightHandViShouldBeFound("addressId.equals=" + addressId);

        // Get all the alPacinoAndreiRightHandViList where address equals to (addressId + 1)
        defaultAlPacinoAndreiRightHandViShouldNotBeFound("addressId.equals=" + (addressId + 1));
    }

    private void defaultAlPacinoAndreiRightHandViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlPacinoAndreiRightHandViShouldBeFound(shouldBeFound);
        defaultAlPacinoAndreiRightHandViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlPacinoAndreiRightHandViShouldBeFound(String filter) throws Exception {
        restAlPacinoAndreiRightHandViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPacinoAndreiRightHandVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT.booleanValue())));

        // Check, that the count call also returns 1
        restAlPacinoAndreiRightHandViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlPacinoAndreiRightHandViShouldNotBeFound(String filter) throws Exception {
        restAlPacinoAndreiRightHandViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlPacinoAndreiRightHandViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlPacinoAndreiRightHandVi() throws Exception {
        // Get the alPacinoAndreiRightHandVi
        restAlPacinoAndreiRightHandViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlPacinoAndreiRightHandVi() throws Exception {
        // Initialize the database
        insertedAlPacinoAndreiRightHandVi = alPacinoAndreiRightHandViRepository.saveAndFlush(alPacinoAndreiRightHandVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPacinoAndreiRightHandVi
        AlPacinoAndreiRightHandVi updatedAlPacinoAndreiRightHandVi = alPacinoAndreiRightHandViRepository
            .findById(alPacinoAndreiRightHandVi.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedAlPacinoAndreiRightHandVi are not directly saved in db
        em.detach(updatedAlPacinoAndreiRightHandVi);
        updatedAlPacinoAndreiRightHandVi.name(UPDATED_NAME).isDefault(UPDATED_IS_DEFAULT);
        AlPacinoAndreiRightHandViDTO alPacinoAndreiRightHandViDTO = alPacinoAndreiRightHandViMapper.toDto(updatedAlPacinoAndreiRightHandVi);

        restAlPacinoAndreiRightHandViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alPacinoAndreiRightHandViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPacinoAndreiRightHandViDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlPacinoAndreiRightHandVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlPacinoAndreiRightHandViToMatchAllProperties(updatedAlPacinoAndreiRightHandVi);
    }

    @Test
    @Transactional
    void putNonExistingAlPacinoAndreiRightHandVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacinoAndreiRightHandVi.setId(longCount.incrementAndGet());

        // Create the AlPacinoAndreiRightHandVi
        AlPacinoAndreiRightHandViDTO alPacinoAndreiRightHandViDTO = alPacinoAndreiRightHandViMapper.toDto(alPacinoAndreiRightHandVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPacinoAndreiRightHandViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alPacinoAndreiRightHandViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPacinoAndreiRightHandViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPacinoAndreiRightHandVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlPacinoAndreiRightHandVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacinoAndreiRightHandVi.setId(longCount.incrementAndGet());

        // Create the AlPacinoAndreiRightHandVi
        AlPacinoAndreiRightHandViDTO alPacinoAndreiRightHandViDTO = alPacinoAndreiRightHandViMapper.toDto(alPacinoAndreiRightHandVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPacinoAndreiRightHandViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPacinoAndreiRightHandViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPacinoAndreiRightHandVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlPacinoAndreiRightHandVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacinoAndreiRightHandVi.setId(longCount.incrementAndGet());

        // Create the AlPacinoAndreiRightHandVi
        AlPacinoAndreiRightHandViDTO alPacinoAndreiRightHandViDTO = alPacinoAndreiRightHandViMapper.toDto(alPacinoAndreiRightHandVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPacinoAndreiRightHandViMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPacinoAndreiRightHandViDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPacinoAndreiRightHandVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlPacinoAndreiRightHandViWithPatch() throws Exception {
        // Initialize the database
        insertedAlPacinoAndreiRightHandVi = alPacinoAndreiRightHandViRepository.saveAndFlush(alPacinoAndreiRightHandVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPacinoAndreiRightHandVi using partial update
        AlPacinoAndreiRightHandVi partialUpdatedAlPacinoAndreiRightHandVi = new AlPacinoAndreiRightHandVi();
        partialUpdatedAlPacinoAndreiRightHandVi.setId(alPacinoAndreiRightHandVi.getId());

        partialUpdatedAlPacinoAndreiRightHandVi.name(UPDATED_NAME).isDefault(UPDATED_IS_DEFAULT);

        restAlPacinoAndreiRightHandViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPacinoAndreiRightHandVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPacinoAndreiRightHandVi))
            )
            .andExpect(status().isOk());

        // Validate the AlPacinoAndreiRightHandVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPacinoAndreiRightHandViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlPacinoAndreiRightHandVi, alPacinoAndreiRightHandVi),
            getPersistedAlPacinoAndreiRightHandVi(alPacinoAndreiRightHandVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlPacinoAndreiRightHandViWithPatch() throws Exception {
        // Initialize the database
        insertedAlPacinoAndreiRightHandVi = alPacinoAndreiRightHandViRepository.saveAndFlush(alPacinoAndreiRightHandVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPacinoAndreiRightHandVi using partial update
        AlPacinoAndreiRightHandVi partialUpdatedAlPacinoAndreiRightHandVi = new AlPacinoAndreiRightHandVi();
        partialUpdatedAlPacinoAndreiRightHandVi.setId(alPacinoAndreiRightHandVi.getId());

        partialUpdatedAlPacinoAndreiRightHandVi.name(UPDATED_NAME).isDefault(UPDATED_IS_DEFAULT);

        restAlPacinoAndreiRightHandViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPacinoAndreiRightHandVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPacinoAndreiRightHandVi))
            )
            .andExpect(status().isOk());

        // Validate the AlPacinoAndreiRightHandVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPacinoAndreiRightHandViUpdatableFieldsEquals(
            partialUpdatedAlPacinoAndreiRightHandVi,
            getPersistedAlPacinoAndreiRightHandVi(partialUpdatedAlPacinoAndreiRightHandVi)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAlPacinoAndreiRightHandVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacinoAndreiRightHandVi.setId(longCount.incrementAndGet());

        // Create the AlPacinoAndreiRightHandVi
        AlPacinoAndreiRightHandViDTO alPacinoAndreiRightHandViDTO = alPacinoAndreiRightHandViMapper.toDto(alPacinoAndreiRightHandVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPacinoAndreiRightHandViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alPacinoAndreiRightHandViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPacinoAndreiRightHandViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPacinoAndreiRightHandVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlPacinoAndreiRightHandVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacinoAndreiRightHandVi.setId(longCount.incrementAndGet());

        // Create the AlPacinoAndreiRightHandVi
        AlPacinoAndreiRightHandViDTO alPacinoAndreiRightHandViDTO = alPacinoAndreiRightHandViMapper.toDto(alPacinoAndreiRightHandVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPacinoAndreiRightHandViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPacinoAndreiRightHandViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPacinoAndreiRightHandVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlPacinoAndreiRightHandVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacinoAndreiRightHandVi.setId(longCount.incrementAndGet());

        // Create the AlPacinoAndreiRightHandVi
        AlPacinoAndreiRightHandViDTO alPacinoAndreiRightHandViDTO = alPacinoAndreiRightHandViMapper.toDto(alPacinoAndreiRightHandVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPacinoAndreiRightHandViMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPacinoAndreiRightHandViDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPacinoAndreiRightHandVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlPacinoAndreiRightHandVi() throws Exception {
        // Initialize the database
        insertedAlPacinoAndreiRightHandVi = alPacinoAndreiRightHandViRepository.saveAndFlush(alPacinoAndreiRightHandVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alPacinoAndreiRightHandVi
        restAlPacinoAndreiRightHandViMockMvc
            .perform(delete(ENTITY_API_URL_ID, alPacinoAndreiRightHandVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alPacinoAndreiRightHandViRepository.count();
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

    protected AlPacinoAndreiRightHandVi getPersistedAlPacinoAndreiRightHandVi(AlPacinoAndreiRightHandVi alPacinoAndreiRightHandVi) {
        return alPacinoAndreiRightHandViRepository.findById(alPacinoAndreiRightHandVi.getId()).orElseThrow();
    }

    protected void assertPersistedAlPacinoAndreiRightHandViToMatchAllProperties(
        AlPacinoAndreiRightHandVi expectedAlPacinoAndreiRightHandVi
    ) {
        assertAlPacinoAndreiRightHandViAllPropertiesEquals(
            expectedAlPacinoAndreiRightHandVi,
            getPersistedAlPacinoAndreiRightHandVi(expectedAlPacinoAndreiRightHandVi)
        );
    }

    protected void assertPersistedAlPacinoAndreiRightHandViToMatchUpdatableProperties(
        AlPacinoAndreiRightHandVi expectedAlPacinoAndreiRightHandVi
    ) {
        assertAlPacinoAndreiRightHandViAllUpdatablePropertiesEquals(
            expectedAlPacinoAndreiRightHandVi,
            getPersistedAlPacinoAndreiRightHandVi(expectedAlPacinoAndreiRightHandVi)
        );
    }
}
