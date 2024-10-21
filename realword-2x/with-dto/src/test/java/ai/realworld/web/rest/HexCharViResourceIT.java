package ai.realworld.web.rest;

import static ai.realworld.domain.HexCharViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.HashRossVi;
import ai.realworld.domain.HexCharVi;
import ai.realworld.domain.User;
import ai.realworld.domain.enumeration.TyrantSex;
import ai.realworld.repository.HexCharViRepository;
import ai.realworld.repository.UserRepository;
import ai.realworld.service.HexCharViService;
import ai.realworld.service.dto.HexCharViDTO;
import ai.realworld.service.mapper.HexCharViMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link HexCharViResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class HexCharViResourceIT {

    private static final LocalDate DEFAULT_DOB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DOB = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DOB = LocalDate.ofEpochDay(-1L);

    private static final TyrantSex DEFAULT_GENDER = TyrantSex.MALE;
    private static final TyrantSex UPDATED_GENDER = TyrantSex.FEMALE;

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_BIO_HEITIGA = "AAAAAAAAAA";
    private static final String UPDATED_BIO_HEITIGA = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ENABLED = false;
    private static final Boolean UPDATED_IS_ENABLED = true;

    private static final String ENTITY_API_URL = "/api/hex-char-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HexCharViRepository hexCharViRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private HexCharViRepository hexCharViRepositoryMock;

    @Autowired
    private HexCharViMapper hexCharViMapper;

    @Mock
    private HexCharViService hexCharViServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHexCharViMockMvc;

    private HexCharVi hexCharVi;

    private HexCharVi insertedHexCharVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HexCharVi createEntity(EntityManager em) {
        HexCharVi hexCharVi = new HexCharVi()
            .dob(DEFAULT_DOB)
            .gender(DEFAULT_GENDER)
            .phone(DEFAULT_PHONE)
            .bioHeitiga(DEFAULT_BIO_HEITIGA)
            .isEnabled(DEFAULT_IS_ENABLED);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        hexCharVi.setInternalUser(user);
        return hexCharVi;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HexCharVi createUpdatedEntity(EntityManager em) {
        HexCharVi updatedHexCharVi = new HexCharVi()
            .dob(UPDATED_DOB)
            .gender(UPDATED_GENDER)
            .phone(UPDATED_PHONE)
            .bioHeitiga(UPDATED_BIO_HEITIGA)
            .isEnabled(UPDATED_IS_ENABLED);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedHexCharVi.setInternalUser(user);
        return updatedHexCharVi;
    }

    @BeforeEach
    public void initTest() {
        hexCharVi = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedHexCharVi != null) {
            hexCharViRepository.delete(insertedHexCharVi);
            insertedHexCharVi = null;
        }
    }

    @Test
    @Transactional
    void createHexCharVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HexCharVi
        HexCharViDTO hexCharViDTO = hexCharViMapper.toDto(hexCharVi);
        var returnedHexCharViDTO = om.readValue(
            restHexCharViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hexCharViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HexCharViDTO.class
        );

        // Validate the HexCharVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHexCharVi = hexCharViMapper.toEntity(returnedHexCharViDTO);
        assertHexCharViUpdatableFieldsEquals(returnedHexCharVi, getPersistedHexCharVi(returnedHexCharVi));

        insertedHexCharVi = returnedHexCharVi;
    }

    @Test
    @Transactional
    void createHexCharViWithExistingId() throws Exception {
        // Create the HexCharVi with an existing ID
        hexCharVi.setId(1L);
        HexCharViDTO hexCharViDTO = hexCharViMapper.toDto(hexCharVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHexCharViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hexCharViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HexCharVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHexCharVis() throws Exception {
        // Initialize the database
        insertedHexCharVi = hexCharViRepository.saveAndFlush(hexCharVi);

        // Get all the hexCharViList
        restHexCharViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hexCharVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].bioHeitiga").value(hasItem(DEFAULT_BIO_HEITIGA)))
            .andExpect(jsonPath("$.[*].isEnabled").value(hasItem(DEFAULT_IS_ENABLED.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHexCharVisWithEagerRelationshipsIsEnabled() throws Exception {
        when(hexCharViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHexCharViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(hexCharViServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHexCharVisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(hexCharViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHexCharViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(hexCharViRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getHexCharVi() throws Exception {
        // Initialize the database
        insertedHexCharVi = hexCharViRepository.saveAndFlush(hexCharVi);

        // Get the hexCharVi
        restHexCharViMockMvc
            .perform(get(ENTITY_API_URL_ID, hexCharVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hexCharVi.getId().intValue()))
            .andExpect(jsonPath("$.dob").value(DEFAULT_DOB.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.bioHeitiga").value(DEFAULT_BIO_HEITIGA))
            .andExpect(jsonPath("$.isEnabled").value(DEFAULT_IS_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    void getHexCharVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedHexCharVi = hexCharViRepository.saveAndFlush(hexCharVi);

        Long id = hexCharVi.getId();

        defaultHexCharViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultHexCharViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultHexCharViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHexCharVisByDobIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHexCharVi = hexCharViRepository.saveAndFlush(hexCharVi);

        // Get all the hexCharViList where dob equals to
        defaultHexCharViFiltering("dob.equals=" + DEFAULT_DOB, "dob.equals=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllHexCharVisByDobIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHexCharVi = hexCharViRepository.saveAndFlush(hexCharVi);

        // Get all the hexCharViList where dob in
        defaultHexCharViFiltering("dob.in=" + DEFAULT_DOB + "," + UPDATED_DOB, "dob.in=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllHexCharVisByDobIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHexCharVi = hexCharViRepository.saveAndFlush(hexCharVi);

        // Get all the hexCharViList where dob is not null
        defaultHexCharViFiltering("dob.specified=true", "dob.specified=false");
    }

    @Test
    @Transactional
    void getAllHexCharVisByDobIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHexCharVi = hexCharViRepository.saveAndFlush(hexCharVi);

        // Get all the hexCharViList where dob is greater than or equal to
        defaultHexCharViFiltering("dob.greaterThanOrEqual=" + DEFAULT_DOB, "dob.greaterThanOrEqual=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllHexCharVisByDobIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHexCharVi = hexCharViRepository.saveAndFlush(hexCharVi);

        // Get all the hexCharViList where dob is less than or equal to
        defaultHexCharViFiltering("dob.lessThanOrEqual=" + DEFAULT_DOB, "dob.lessThanOrEqual=" + SMALLER_DOB);
    }

    @Test
    @Transactional
    void getAllHexCharVisByDobIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHexCharVi = hexCharViRepository.saveAndFlush(hexCharVi);

        // Get all the hexCharViList where dob is less than
        defaultHexCharViFiltering("dob.lessThan=" + UPDATED_DOB, "dob.lessThan=" + DEFAULT_DOB);
    }

    @Test
    @Transactional
    void getAllHexCharVisByDobIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHexCharVi = hexCharViRepository.saveAndFlush(hexCharVi);

        // Get all the hexCharViList where dob is greater than
        defaultHexCharViFiltering("dob.greaterThan=" + SMALLER_DOB, "dob.greaterThan=" + DEFAULT_DOB);
    }

    @Test
    @Transactional
    void getAllHexCharVisByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHexCharVi = hexCharViRepository.saveAndFlush(hexCharVi);

        // Get all the hexCharViList where gender equals to
        defaultHexCharViFiltering("gender.equals=" + DEFAULT_GENDER, "gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllHexCharVisByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHexCharVi = hexCharViRepository.saveAndFlush(hexCharVi);

        // Get all the hexCharViList where gender in
        defaultHexCharViFiltering("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER, "gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllHexCharVisByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHexCharVi = hexCharViRepository.saveAndFlush(hexCharVi);

        // Get all the hexCharViList where gender is not null
        defaultHexCharViFiltering("gender.specified=true", "gender.specified=false");
    }

    @Test
    @Transactional
    void getAllHexCharVisByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHexCharVi = hexCharViRepository.saveAndFlush(hexCharVi);

        // Get all the hexCharViList where phone equals to
        defaultHexCharViFiltering("phone.equals=" + DEFAULT_PHONE, "phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllHexCharVisByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHexCharVi = hexCharViRepository.saveAndFlush(hexCharVi);

        // Get all the hexCharViList where phone in
        defaultHexCharViFiltering("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE, "phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllHexCharVisByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHexCharVi = hexCharViRepository.saveAndFlush(hexCharVi);

        // Get all the hexCharViList where phone is not null
        defaultHexCharViFiltering("phone.specified=true", "phone.specified=false");
    }

    @Test
    @Transactional
    void getAllHexCharVisByPhoneContainsSomething() throws Exception {
        // Initialize the database
        insertedHexCharVi = hexCharViRepository.saveAndFlush(hexCharVi);

        // Get all the hexCharViList where phone contains
        defaultHexCharViFiltering("phone.contains=" + DEFAULT_PHONE, "phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllHexCharVisByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHexCharVi = hexCharViRepository.saveAndFlush(hexCharVi);

        // Get all the hexCharViList where phone does not contain
        defaultHexCharViFiltering("phone.doesNotContain=" + UPDATED_PHONE, "phone.doesNotContain=" + DEFAULT_PHONE);
    }

    @Test
    @Transactional
    void getAllHexCharVisByBioHeitigaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHexCharVi = hexCharViRepository.saveAndFlush(hexCharVi);

        // Get all the hexCharViList where bioHeitiga equals to
        defaultHexCharViFiltering("bioHeitiga.equals=" + DEFAULT_BIO_HEITIGA, "bioHeitiga.equals=" + UPDATED_BIO_HEITIGA);
    }

    @Test
    @Transactional
    void getAllHexCharVisByBioHeitigaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHexCharVi = hexCharViRepository.saveAndFlush(hexCharVi);

        // Get all the hexCharViList where bioHeitiga in
        defaultHexCharViFiltering(
            "bioHeitiga.in=" + DEFAULT_BIO_HEITIGA + "," + UPDATED_BIO_HEITIGA,
            "bioHeitiga.in=" + UPDATED_BIO_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllHexCharVisByBioHeitigaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHexCharVi = hexCharViRepository.saveAndFlush(hexCharVi);

        // Get all the hexCharViList where bioHeitiga is not null
        defaultHexCharViFiltering("bioHeitiga.specified=true", "bioHeitiga.specified=false");
    }

    @Test
    @Transactional
    void getAllHexCharVisByBioHeitigaContainsSomething() throws Exception {
        // Initialize the database
        insertedHexCharVi = hexCharViRepository.saveAndFlush(hexCharVi);

        // Get all the hexCharViList where bioHeitiga contains
        defaultHexCharViFiltering("bioHeitiga.contains=" + DEFAULT_BIO_HEITIGA, "bioHeitiga.contains=" + UPDATED_BIO_HEITIGA);
    }

    @Test
    @Transactional
    void getAllHexCharVisByBioHeitigaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHexCharVi = hexCharViRepository.saveAndFlush(hexCharVi);

        // Get all the hexCharViList where bioHeitiga does not contain
        defaultHexCharViFiltering("bioHeitiga.doesNotContain=" + UPDATED_BIO_HEITIGA, "bioHeitiga.doesNotContain=" + DEFAULT_BIO_HEITIGA);
    }

    @Test
    @Transactional
    void getAllHexCharVisByIsEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHexCharVi = hexCharViRepository.saveAndFlush(hexCharVi);

        // Get all the hexCharViList where isEnabled equals to
        defaultHexCharViFiltering("isEnabled.equals=" + DEFAULT_IS_ENABLED, "isEnabled.equals=" + UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    void getAllHexCharVisByIsEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHexCharVi = hexCharViRepository.saveAndFlush(hexCharVi);

        // Get all the hexCharViList where isEnabled in
        defaultHexCharViFiltering("isEnabled.in=" + DEFAULT_IS_ENABLED + "," + UPDATED_IS_ENABLED, "isEnabled.in=" + UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    void getAllHexCharVisByIsEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHexCharVi = hexCharViRepository.saveAndFlush(hexCharVi);

        // Get all the hexCharViList where isEnabled is not null
        defaultHexCharViFiltering("isEnabled.specified=true", "isEnabled.specified=false");
    }

    @Test
    @Transactional
    void getAllHexCharVisByInternalUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User internalUser = hexCharVi.getInternalUser();
        hexCharViRepository.saveAndFlush(hexCharVi);
        Long internalUserId = internalUser.getId();
        // Get all the hexCharViList where internalUser equals to internalUserId
        defaultHexCharViShouldBeFound("internalUserId.equals=" + internalUserId);

        // Get all the hexCharViList where internalUser equals to (internalUserId + 1)
        defaultHexCharViShouldNotBeFound("internalUserId.equals=" + (internalUserId + 1));
    }

    @Test
    @Transactional
    void getAllHexCharVisByRoleIsEqualToSomething() throws Exception {
        HashRossVi role;
        if (TestUtil.findAll(em, HashRossVi.class).isEmpty()) {
            hexCharViRepository.saveAndFlush(hexCharVi);
            role = HashRossViResourceIT.createEntity();
        } else {
            role = TestUtil.findAll(em, HashRossVi.class).get(0);
        }
        em.persist(role);
        em.flush();
        hexCharVi.setRole(role);
        hexCharViRepository.saveAndFlush(hexCharVi);
        Long roleId = role.getId();
        // Get all the hexCharViList where role equals to roleId
        defaultHexCharViShouldBeFound("roleId.equals=" + roleId);

        // Get all the hexCharViList where role equals to (roleId + 1)
        defaultHexCharViShouldNotBeFound("roleId.equals=" + (roleId + 1));
    }

    private void defaultHexCharViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultHexCharViShouldBeFound(shouldBeFound);
        defaultHexCharViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHexCharViShouldBeFound(String filter) throws Exception {
        restHexCharViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hexCharVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].bioHeitiga").value(hasItem(DEFAULT_BIO_HEITIGA)))
            .andExpect(jsonPath("$.[*].isEnabled").value(hasItem(DEFAULT_IS_ENABLED.booleanValue())));

        // Check, that the count call also returns 1
        restHexCharViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHexCharViShouldNotBeFound(String filter) throws Exception {
        restHexCharViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHexCharViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHexCharVi() throws Exception {
        // Get the hexCharVi
        restHexCharViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHexCharVi() throws Exception {
        // Initialize the database
        insertedHexCharVi = hexCharViRepository.saveAndFlush(hexCharVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hexCharVi
        HexCharVi updatedHexCharVi = hexCharViRepository.findById(hexCharVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHexCharVi are not directly saved in db
        em.detach(updatedHexCharVi);
        updatedHexCharVi
            .dob(UPDATED_DOB)
            .gender(UPDATED_GENDER)
            .phone(UPDATED_PHONE)
            .bioHeitiga(UPDATED_BIO_HEITIGA)
            .isEnabled(UPDATED_IS_ENABLED);
        HexCharViDTO hexCharViDTO = hexCharViMapper.toDto(updatedHexCharVi);

        restHexCharViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hexCharViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hexCharViDTO))
            )
            .andExpect(status().isOk());

        // Validate the HexCharVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHexCharViToMatchAllProperties(updatedHexCharVi);
    }

    @Test
    @Transactional
    void putNonExistingHexCharVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hexCharVi.setId(longCount.incrementAndGet());

        // Create the HexCharVi
        HexCharViDTO hexCharViDTO = hexCharViMapper.toDto(hexCharVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHexCharViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hexCharViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hexCharViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HexCharVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHexCharVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hexCharVi.setId(longCount.incrementAndGet());

        // Create the HexCharVi
        HexCharViDTO hexCharViDTO = hexCharViMapper.toDto(hexCharVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHexCharViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hexCharViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HexCharVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHexCharVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hexCharVi.setId(longCount.incrementAndGet());

        // Create the HexCharVi
        HexCharViDTO hexCharViDTO = hexCharViMapper.toDto(hexCharVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHexCharViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hexCharViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HexCharVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHexCharViWithPatch() throws Exception {
        // Initialize the database
        insertedHexCharVi = hexCharViRepository.saveAndFlush(hexCharVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hexCharVi using partial update
        HexCharVi partialUpdatedHexCharVi = new HexCharVi();
        partialUpdatedHexCharVi.setId(hexCharVi.getId());

        partialUpdatedHexCharVi.dob(UPDATED_DOB);

        restHexCharViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHexCharVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHexCharVi))
            )
            .andExpect(status().isOk());

        // Validate the HexCharVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHexCharViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHexCharVi, hexCharVi),
            getPersistedHexCharVi(hexCharVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateHexCharViWithPatch() throws Exception {
        // Initialize the database
        insertedHexCharVi = hexCharViRepository.saveAndFlush(hexCharVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hexCharVi using partial update
        HexCharVi partialUpdatedHexCharVi = new HexCharVi();
        partialUpdatedHexCharVi.setId(hexCharVi.getId());

        partialUpdatedHexCharVi
            .dob(UPDATED_DOB)
            .gender(UPDATED_GENDER)
            .phone(UPDATED_PHONE)
            .bioHeitiga(UPDATED_BIO_HEITIGA)
            .isEnabled(UPDATED_IS_ENABLED);

        restHexCharViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHexCharVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHexCharVi))
            )
            .andExpect(status().isOk());

        // Validate the HexCharVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHexCharViUpdatableFieldsEquals(partialUpdatedHexCharVi, getPersistedHexCharVi(partialUpdatedHexCharVi));
    }

    @Test
    @Transactional
    void patchNonExistingHexCharVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hexCharVi.setId(longCount.incrementAndGet());

        // Create the HexCharVi
        HexCharViDTO hexCharViDTO = hexCharViMapper.toDto(hexCharVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHexCharViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hexCharViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hexCharViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HexCharVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHexCharVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hexCharVi.setId(longCount.incrementAndGet());

        // Create the HexCharVi
        HexCharViDTO hexCharViDTO = hexCharViMapper.toDto(hexCharVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHexCharViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hexCharViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HexCharVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHexCharVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hexCharVi.setId(longCount.incrementAndGet());

        // Create the HexCharVi
        HexCharViDTO hexCharViDTO = hexCharViMapper.toDto(hexCharVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHexCharViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(hexCharViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HexCharVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHexCharVi() throws Exception {
        // Initialize the database
        insertedHexCharVi = hexCharViRepository.saveAndFlush(hexCharVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the hexCharVi
        restHexCharViMockMvc
            .perform(delete(ENTITY_API_URL_ID, hexCharVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return hexCharViRepository.count();
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

    protected HexCharVi getPersistedHexCharVi(HexCharVi hexCharVi) {
        return hexCharViRepository.findById(hexCharVi.getId()).orElseThrow();
    }

    protected void assertPersistedHexCharViToMatchAllProperties(HexCharVi expectedHexCharVi) {
        assertHexCharViAllPropertiesEquals(expectedHexCharVi, getPersistedHexCharVi(expectedHexCharVi));
    }

    protected void assertPersistedHexCharViToMatchUpdatableProperties(HexCharVi expectedHexCharVi) {
        assertHexCharViAllUpdatablePropertiesEquals(expectedHexCharVi, getPersistedHexCharVi(expectedHexCharVi));
    }
}
