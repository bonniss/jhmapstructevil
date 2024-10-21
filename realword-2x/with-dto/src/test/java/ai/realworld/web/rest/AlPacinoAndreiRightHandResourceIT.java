package ai.realworld.web.rest;

import static ai.realworld.domain.AlPacinoAndreiRightHandAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlPacino;
import ai.realworld.domain.AlPacinoAndreiRightHand;
import ai.realworld.domain.AndreiRightHand;
import ai.realworld.repository.AlPacinoAndreiRightHandRepository;
import ai.realworld.service.dto.AlPacinoAndreiRightHandDTO;
import ai.realworld.service.mapper.AlPacinoAndreiRightHandMapper;
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
 * Integration tests for the {@link AlPacinoAndreiRightHandResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlPacinoAndreiRightHandResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DEFAULT = false;
    private static final Boolean UPDATED_IS_DEFAULT = true;

    private static final String ENTITY_API_URL = "/api/al-pacino-andrei-right-hands";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlPacinoAndreiRightHandRepository alPacinoAndreiRightHandRepository;

    @Autowired
    private AlPacinoAndreiRightHandMapper alPacinoAndreiRightHandMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlPacinoAndreiRightHandMockMvc;

    private AlPacinoAndreiRightHand alPacinoAndreiRightHand;

    private AlPacinoAndreiRightHand insertedAlPacinoAndreiRightHand;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPacinoAndreiRightHand createEntity() {
        return new AlPacinoAndreiRightHand().name(DEFAULT_NAME).isDefault(DEFAULT_IS_DEFAULT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPacinoAndreiRightHand createUpdatedEntity() {
        return new AlPacinoAndreiRightHand().name(UPDATED_NAME).isDefault(UPDATED_IS_DEFAULT);
    }

    @BeforeEach
    public void initTest() {
        alPacinoAndreiRightHand = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlPacinoAndreiRightHand != null) {
            alPacinoAndreiRightHandRepository.delete(insertedAlPacinoAndreiRightHand);
            insertedAlPacinoAndreiRightHand = null;
        }
    }

    @Test
    @Transactional
    void createAlPacinoAndreiRightHand() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlPacinoAndreiRightHand
        AlPacinoAndreiRightHandDTO alPacinoAndreiRightHandDTO = alPacinoAndreiRightHandMapper.toDto(alPacinoAndreiRightHand);
        var returnedAlPacinoAndreiRightHandDTO = om.readValue(
            restAlPacinoAndreiRightHandMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPacinoAndreiRightHandDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlPacinoAndreiRightHandDTO.class
        );

        // Validate the AlPacinoAndreiRightHand in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlPacinoAndreiRightHand = alPacinoAndreiRightHandMapper.toEntity(returnedAlPacinoAndreiRightHandDTO);
        assertAlPacinoAndreiRightHandUpdatableFieldsEquals(
            returnedAlPacinoAndreiRightHand,
            getPersistedAlPacinoAndreiRightHand(returnedAlPacinoAndreiRightHand)
        );

        insertedAlPacinoAndreiRightHand = returnedAlPacinoAndreiRightHand;
    }

    @Test
    @Transactional
    void createAlPacinoAndreiRightHandWithExistingId() throws Exception {
        // Create the AlPacinoAndreiRightHand with an existing ID
        alPacinoAndreiRightHand.setId(1L);
        AlPacinoAndreiRightHandDTO alPacinoAndreiRightHandDTO = alPacinoAndreiRightHandMapper.toDto(alPacinoAndreiRightHand);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlPacinoAndreiRightHandMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPacinoAndreiRightHandDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlPacinoAndreiRightHand in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alPacinoAndreiRightHand.setName(null);

        // Create the AlPacinoAndreiRightHand, which fails.
        AlPacinoAndreiRightHandDTO alPacinoAndreiRightHandDTO = alPacinoAndreiRightHandMapper.toDto(alPacinoAndreiRightHand);

        restAlPacinoAndreiRightHandMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPacinoAndreiRightHandDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlPacinoAndreiRightHands() throws Exception {
        // Initialize the database
        insertedAlPacinoAndreiRightHand = alPacinoAndreiRightHandRepository.saveAndFlush(alPacinoAndreiRightHand);

        // Get all the alPacinoAndreiRightHandList
        restAlPacinoAndreiRightHandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPacinoAndreiRightHand.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT.booleanValue())));
    }

    @Test
    @Transactional
    void getAlPacinoAndreiRightHand() throws Exception {
        // Initialize the database
        insertedAlPacinoAndreiRightHand = alPacinoAndreiRightHandRepository.saveAndFlush(alPacinoAndreiRightHand);

        // Get the alPacinoAndreiRightHand
        restAlPacinoAndreiRightHandMockMvc
            .perform(get(ENTITY_API_URL_ID, alPacinoAndreiRightHand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alPacinoAndreiRightHand.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.isDefault").value(DEFAULT_IS_DEFAULT.booleanValue()));
    }

    @Test
    @Transactional
    void getAlPacinoAndreiRightHandsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlPacinoAndreiRightHand = alPacinoAndreiRightHandRepository.saveAndFlush(alPacinoAndreiRightHand);

        Long id = alPacinoAndreiRightHand.getId();

        defaultAlPacinoAndreiRightHandFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlPacinoAndreiRightHandFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlPacinoAndreiRightHandFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlPacinoAndreiRightHandsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoAndreiRightHand = alPacinoAndreiRightHandRepository.saveAndFlush(alPacinoAndreiRightHand);

        // Get all the alPacinoAndreiRightHandList where name equals to
        defaultAlPacinoAndreiRightHandFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlPacinoAndreiRightHandsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPacinoAndreiRightHand = alPacinoAndreiRightHandRepository.saveAndFlush(alPacinoAndreiRightHand);

        // Get all the alPacinoAndreiRightHandList where name in
        defaultAlPacinoAndreiRightHandFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlPacinoAndreiRightHandsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPacinoAndreiRightHand = alPacinoAndreiRightHandRepository.saveAndFlush(alPacinoAndreiRightHand);

        // Get all the alPacinoAndreiRightHandList where name is not null
        defaultAlPacinoAndreiRightHandFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPacinoAndreiRightHandsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoAndreiRightHand = alPacinoAndreiRightHandRepository.saveAndFlush(alPacinoAndreiRightHand);

        // Get all the alPacinoAndreiRightHandList where name contains
        defaultAlPacinoAndreiRightHandFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlPacinoAndreiRightHandsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoAndreiRightHand = alPacinoAndreiRightHandRepository.saveAndFlush(alPacinoAndreiRightHand);

        // Get all the alPacinoAndreiRightHandList where name does not contain
        defaultAlPacinoAndreiRightHandFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAlPacinoAndreiRightHandsByIsDefaultIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPacinoAndreiRightHand = alPacinoAndreiRightHandRepository.saveAndFlush(alPacinoAndreiRightHand);

        // Get all the alPacinoAndreiRightHandList where isDefault equals to
        defaultAlPacinoAndreiRightHandFiltering("isDefault.equals=" + DEFAULT_IS_DEFAULT, "isDefault.equals=" + UPDATED_IS_DEFAULT);
    }

    @Test
    @Transactional
    void getAllAlPacinoAndreiRightHandsByIsDefaultIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPacinoAndreiRightHand = alPacinoAndreiRightHandRepository.saveAndFlush(alPacinoAndreiRightHand);

        // Get all the alPacinoAndreiRightHandList where isDefault in
        defaultAlPacinoAndreiRightHandFiltering(
            "isDefault.in=" + DEFAULT_IS_DEFAULT + "," + UPDATED_IS_DEFAULT,
            "isDefault.in=" + UPDATED_IS_DEFAULT
        );
    }

    @Test
    @Transactional
    void getAllAlPacinoAndreiRightHandsByIsDefaultIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPacinoAndreiRightHand = alPacinoAndreiRightHandRepository.saveAndFlush(alPacinoAndreiRightHand);

        // Get all the alPacinoAndreiRightHandList where isDefault is not null
        defaultAlPacinoAndreiRightHandFiltering("isDefault.specified=true", "isDefault.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPacinoAndreiRightHandsByUserIsEqualToSomething() throws Exception {
        AlPacino user;
        if (TestUtil.findAll(em, AlPacino.class).isEmpty()) {
            alPacinoAndreiRightHandRepository.saveAndFlush(alPacinoAndreiRightHand);
            user = AlPacinoResourceIT.createEntity();
        } else {
            user = TestUtil.findAll(em, AlPacino.class).get(0);
        }
        em.persist(user);
        em.flush();
        alPacinoAndreiRightHand.setUser(user);
        alPacinoAndreiRightHandRepository.saveAndFlush(alPacinoAndreiRightHand);
        UUID userId = user.getId();
        // Get all the alPacinoAndreiRightHandList where user equals to userId
        defaultAlPacinoAndreiRightHandShouldBeFound("userId.equals=" + userId);

        // Get all the alPacinoAndreiRightHandList where user equals to UUID.randomUUID()
        defaultAlPacinoAndreiRightHandShouldNotBeFound("userId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlPacinoAndreiRightHandsByAddressIsEqualToSomething() throws Exception {
        AndreiRightHand address;
        if (TestUtil.findAll(em, AndreiRightHand.class).isEmpty()) {
            alPacinoAndreiRightHandRepository.saveAndFlush(alPacinoAndreiRightHand);
            address = AndreiRightHandResourceIT.createEntity();
        } else {
            address = TestUtil.findAll(em, AndreiRightHand.class).get(0);
        }
        em.persist(address);
        em.flush();
        alPacinoAndreiRightHand.setAddress(address);
        alPacinoAndreiRightHandRepository.saveAndFlush(alPacinoAndreiRightHand);
        Long addressId = address.getId();
        // Get all the alPacinoAndreiRightHandList where address equals to addressId
        defaultAlPacinoAndreiRightHandShouldBeFound("addressId.equals=" + addressId);

        // Get all the alPacinoAndreiRightHandList where address equals to (addressId + 1)
        defaultAlPacinoAndreiRightHandShouldNotBeFound("addressId.equals=" + (addressId + 1));
    }

    private void defaultAlPacinoAndreiRightHandFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlPacinoAndreiRightHandShouldBeFound(shouldBeFound);
        defaultAlPacinoAndreiRightHandShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlPacinoAndreiRightHandShouldBeFound(String filter) throws Exception {
        restAlPacinoAndreiRightHandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPacinoAndreiRightHand.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT.booleanValue())));

        // Check, that the count call also returns 1
        restAlPacinoAndreiRightHandMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlPacinoAndreiRightHandShouldNotBeFound(String filter) throws Exception {
        restAlPacinoAndreiRightHandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlPacinoAndreiRightHandMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlPacinoAndreiRightHand() throws Exception {
        // Get the alPacinoAndreiRightHand
        restAlPacinoAndreiRightHandMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlPacinoAndreiRightHand() throws Exception {
        // Initialize the database
        insertedAlPacinoAndreiRightHand = alPacinoAndreiRightHandRepository.saveAndFlush(alPacinoAndreiRightHand);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPacinoAndreiRightHand
        AlPacinoAndreiRightHand updatedAlPacinoAndreiRightHand = alPacinoAndreiRightHandRepository
            .findById(alPacinoAndreiRightHand.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedAlPacinoAndreiRightHand are not directly saved in db
        em.detach(updatedAlPacinoAndreiRightHand);
        updatedAlPacinoAndreiRightHand.name(UPDATED_NAME).isDefault(UPDATED_IS_DEFAULT);
        AlPacinoAndreiRightHandDTO alPacinoAndreiRightHandDTO = alPacinoAndreiRightHandMapper.toDto(updatedAlPacinoAndreiRightHand);

        restAlPacinoAndreiRightHandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alPacinoAndreiRightHandDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPacinoAndreiRightHandDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlPacinoAndreiRightHand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlPacinoAndreiRightHandToMatchAllProperties(updatedAlPacinoAndreiRightHand);
    }

    @Test
    @Transactional
    void putNonExistingAlPacinoAndreiRightHand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacinoAndreiRightHand.setId(longCount.incrementAndGet());

        // Create the AlPacinoAndreiRightHand
        AlPacinoAndreiRightHandDTO alPacinoAndreiRightHandDTO = alPacinoAndreiRightHandMapper.toDto(alPacinoAndreiRightHand);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPacinoAndreiRightHandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alPacinoAndreiRightHandDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPacinoAndreiRightHandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPacinoAndreiRightHand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlPacinoAndreiRightHand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacinoAndreiRightHand.setId(longCount.incrementAndGet());

        // Create the AlPacinoAndreiRightHand
        AlPacinoAndreiRightHandDTO alPacinoAndreiRightHandDTO = alPacinoAndreiRightHandMapper.toDto(alPacinoAndreiRightHand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPacinoAndreiRightHandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPacinoAndreiRightHandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPacinoAndreiRightHand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlPacinoAndreiRightHand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacinoAndreiRightHand.setId(longCount.incrementAndGet());

        // Create the AlPacinoAndreiRightHand
        AlPacinoAndreiRightHandDTO alPacinoAndreiRightHandDTO = alPacinoAndreiRightHandMapper.toDto(alPacinoAndreiRightHand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPacinoAndreiRightHandMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPacinoAndreiRightHandDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPacinoAndreiRightHand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlPacinoAndreiRightHandWithPatch() throws Exception {
        // Initialize the database
        insertedAlPacinoAndreiRightHand = alPacinoAndreiRightHandRepository.saveAndFlush(alPacinoAndreiRightHand);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPacinoAndreiRightHand using partial update
        AlPacinoAndreiRightHand partialUpdatedAlPacinoAndreiRightHand = new AlPacinoAndreiRightHand();
        partialUpdatedAlPacinoAndreiRightHand.setId(alPacinoAndreiRightHand.getId());

        restAlPacinoAndreiRightHandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPacinoAndreiRightHand.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPacinoAndreiRightHand))
            )
            .andExpect(status().isOk());

        // Validate the AlPacinoAndreiRightHand in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPacinoAndreiRightHandUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlPacinoAndreiRightHand, alPacinoAndreiRightHand),
            getPersistedAlPacinoAndreiRightHand(alPacinoAndreiRightHand)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlPacinoAndreiRightHandWithPatch() throws Exception {
        // Initialize the database
        insertedAlPacinoAndreiRightHand = alPacinoAndreiRightHandRepository.saveAndFlush(alPacinoAndreiRightHand);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPacinoAndreiRightHand using partial update
        AlPacinoAndreiRightHand partialUpdatedAlPacinoAndreiRightHand = new AlPacinoAndreiRightHand();
        partialUpdatedAlPacinoAndreiRightHand.setId(alPacinoAndreiRightHand.getId());

        partialUpdatedAlPacinoAndreiRightHand.name(UPDATED_NAME).isDefault(UPDATED_IS_DEFAULT);

        restAlPacinoAndreiRightHandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPacinoAndreiRightHand.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPacinoAndreiRightHand))
            )
            .andExpect(status().isOk());

        // Validate the AlPacinoAndreiRightHand in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPacinoAndreiRightHandUpdatableFieldsEquals(
            partialUpdatedAlPacinoAndreiRightHand,
            getPersistedAlPacinoAndreiRightHand(partialUpdatedAlPacinoAndreiRightHand)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAlPacinoAndreiRightHand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacinoAndreiRightHand.setId(longCount.incrementAndGet());

        // Create the AlPacinoAndreiRightHand
        AlPacinoAndreiRightHandDTO alPacinoAndreiRightHandDTO = alPacinoAndreiRightHandMapper.toDto(alPacinoAndreiRightHand);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPacinoAndreiRightHandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alPacinoAndreiRightHandDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPacinoAndreiRightHandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPacinoAndreiRightHand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlPacinoAndreiRightHand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacinoAndreiRightHand.setId(longCount.incrementAndGet());

        // Create the AlPacinoAndreiRightHand
        AlPacinoAndreiRightHandDTO alPacinoAndreiRightHandDTO = alPacinoAndreiRightHandMapper.toDto(alPacinoAndreiRightHand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPacinoAndreiRightHandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPacinoAndreiRightHandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPacinoAndreiRightHand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlPacinoAndreiRightHand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPacinoAndreiRightHand.setId(longCount.incrementAndGet());

        // Create the AlPacinoAndreiRightHand
        AlPacinoAndreiRightHandDTO alPacinoAndreiRightHandDTO = alPacinoAndreiRightHandMapper.toDto(alPacinoAndreiRightHand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPacinoAndreiRightHandMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alPacinoAndreiRightHandDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPacinoAndreiRightHand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlPacinoAndreiRightHand() throws Exception {
        // Initialize the database
        insertedAlPacinoAndreiRightHand = alPacinoAndreiRightHandRepository.saveAndFlush(alPacinoAndreiRightHand);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alPacinoAndreiRightHand
        restAlPacinoAndreiRightHandMockMvc
            .perform(delete(ENTITY_API_URL_ID, alPacinoAndreiRightHand.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alPacinoAndreiRightHandRepository.count();
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

    protected AlPacinoAndreiRightHand getPersistedAlPacinoAndreiRightHand(AlPacinoAndreiRightHand alPacinoAndreiRightHand) {
        return alPacinoAndreiRightHandRepository.findById(alPacinoAndreiRightHand.getId()).orElseThrow();
    }

    protected void assertPersistedAlPacinoAndreiRightHandToMatchAllProperties(AlPacinoAndreiRightHand expectedAlPacinoAndreiRightHand) {
        assertAlPacinoAndreiRightHandAllPropertiesEquals(
            expectedAlPacinoAndreiRightHand,
            getPersistedAlPacinoAndreiRightHand(expectedAlPacinoAndreiRightHand)
        );
    }

    protected void assertPersistedAlPacinoAndreiRightHandToMatchUpdatableProperties(
        AlPacinoAndreiRightHand expectedAlPacinoAndreiRightHand
    ) {
        assertAlPacinoAndreiRightHandAllUpdatablePropertiesEquals(
            expectedAlPacinoAndreiRightHand,
            getPersistedAlPacinoAndreiRightHand(expectedAlPacinoAndreiRightHand)
        );
    }
}
