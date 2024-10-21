package ai.realworld.web.rest;

import static ai.realworld.domain.HexCharAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.HashRoss;
import ai.realworld.domain.HexChar;
import ai.realworld.domain.User;
import ai.realworld.domain.enumeration.TyrantSex;
import ai.realworld.repository.HexCharRepository;
import ai.realworld.repository.UserRepository;
import ai.realworld.service.HexCharService;
import ai.realworld.service.dto.HexCharDTO;
import ai.realworld.service.mapper.HexCharMapper;
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
 * Integration tests for the {@link HexCharResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class HexCharResourceIT {

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

    private static final String ENTITY_API_URL = "/api/hex-chars";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HexCharRepository hexCharRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private HexCharRepository hexCharRepositoryMock;

    @Autowired
    private HexCharMapper hexCharMapper;

    @Mock
    private HexCharService hexCharServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHexCharMockMvc;

    private HexChar hexChar;

    private HexChar insertedHexChar;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HexChar createEntity(EntityManager em) {
        HexChar hexChar = new HexChar()
            .dob(DEFAULT_DOB)
            .gender(DEFAULT_GENDER)
            .phone(DEFAULT_PHONE)
            .bioHeitiga(DEFAULT_BIO_HEITIGA)
            .isEnabled(DEFAULT_IS_ENABLED);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        hexChar.setInternalUser(user);
        return hexChar;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HexChar createUpdatedEntity(EntityManager em) {
        HexChar updatedHexChar = new HexChar()
            .dob(UPDATED_DOB)
            .gender(UPDATED_GENDER)
            .phone(UPDATED_PHONE)
            .bioHeitiga(UPDATED_BIO_HEITIGA)
            .isEnabled(UPDATED_IS_ENABLED);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedHexChar.setInternalUser(user);
        return updatedHexChar;
    }

    @BeforeEach
    public void initTest() {
        hexChar = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedHexChar != null) {
            hexCharRepository.delete(insertedHexChar);
            insertedHexChar = null;
        }
    }

    @Test
    @Transactional
    void createHexChar() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HexChar
        HexCharDTO hexCharDTO = hexCharMapper.toDto(hexChar);
        var returnedHexCharDTO = om.readValue(
            restHexCharMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hexCharDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HexCharDTO.class
        );

        // Validate the HexChar in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHexChar = hexCharMapper.toEntity(returnedHexCharDTO);
        assertHexCharUpdatableFieldsEquals(returnedHexChar, getPersistedHexChar(returnedHexChar));

        insertedHexChar = returnedHexChar;
    }

    @Test
    @Transactional
    void createHexCharWithExistingId() throws Exception {
        // Create the HexChar with an existing ID
        hexChar.setId(1L);
        HexCharDTO hexCharDTO = hexCharMapper.toDto(hexChar);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHexCharMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hexCharDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HexChar in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHexChars() throws Exception {
        // Initialize the database
        insertedHexChar = hexCharRepository.saveAndFlush(hexChar);

        // Get all the hexCharList
        restHexCharMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hexChar.getId().intValue())))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].bioHeitiga").value(hasItem(DEFAULT_BIO_HEITIGA)))
            .andExpect(jsonPath("$.[*].isEnabled").value(hasItem(DEFAULT_IS_ENABLED.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHexCharsWithEagerRelationshipsIsEnabled() throws Exception {
        when(hexCharServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHexCharMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(hexCharServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHexCharsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(hexCharServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHexCharMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(hexCharRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getHexChar() throws Exception {
        // Initialize the database
        insertedHexChar = hexCharRepository.saveAndFlush(hexChar);

        // Get the hexChar
        restHexCharMockMvc
            .perform(get(ENTITY_API_URL_ID, hexChar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hexChar.getId().intValue()))
            .andExpect(jsonPath("$.dob").value(DEFAULT_DOB.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.bioHeitiga").value(DEFAULT_BIO_HEITIGA))
            .andExpect(jsonPath("$.isEnabled").value(DEFAULT_IS_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    void getHexCharsByIdFiltering() throws Exception {
        // Initialize the database
        insertedHexChar = hexCharRepository.saveAndFlush(hexChar);

        Long id = hexChar.getId();

        defaultHexCharFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultHexCharFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultHexCharFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHexCharsByDobIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHexChar = hexCharRepository.saveAndFlush(hexChar);

        // Get all the hexCharList where dob equals to
        defaultHexCharFiltering("dob.equals=" + DEFAULT_DOB, "dob.equals=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllHexCharsByDobIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHexChar = hexCharRepository.saveAndFlush(hexChar);

        // Get all the hexCharList where dob in
        defaultHexCharFiltering("dob.in=" + DEFAULT_DOB + "," + UPDATED_DOB, "dob.in=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllHexCharsByDobIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHexChar = hexCharRepository.saveAndFlush(hexChar);

        // Get all the hexCharList where dob is not null
        defaultHexCharFiltering("dob.specified=true", "dob.specified=false");
    }

    @Test
    @Transactional
    void getAllHexCharsByDobIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHexChar = hexCharRepository.saveAndFlush(hexChar);

        // Get all the hexCharList where dob is greater than or equal to
        defaultHexCharFiltering("dob.greaterThanOrEqual=" + DEFAULT_DOB, "dob.greaterThanOrEqual=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllHexCharsByDobIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHexChar = hexCharRepository.saveAndFlush(hexChar);

        // Get all the hexCharList where dob is less than or equal to
        defaultHexCharFiltering("dob.lessThanOrEqual=" + DEFAULT_DOB, "dob.lessThanOrEqual=" + SMALLER_DOB);
    }

    @Test
    @Transactional
    void getAllHexCharsByDobIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHexChar = hexCharRepository.saveAndFlush(hexChar);

        // Get all the hexCharList where dob is less than
        defaultHexCharFiltering("dob.lessThan=" + UPDATED_DOB, "dob.lessThan=" + DEFAULT_DOB);
    }

    @Test
    @Transactional
    void getAllHexCharsByDobIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHexChar = hexCharRepository.saveAndFlush(hexChar);

        // Get all the hexCharList where dob is greater than
        defaultHexCharFiltering("dob.greaterThan=" + SMALLER_DOB, "dob.greaterThan=" + DEFAULT_DOB);
    }

    @Test
    @Transactional
    void getAllHexCharsByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHexChar = hexCharRepository.saveAndFlush(hexChar);

        // Get all the hexCharList where gender equals to
        defaultHexCharFiltering("gender.equals=" + DEFAULT_GENDER, "gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllHexCharsByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHexChar = hexCharRepository.saveAndFlush(hexChar);

        // Get all the hexCharList where gender in
        defaultHexCharFiltering("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER, "gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllHexCharsByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHexChar = hexCharRepository.saveAndFlush(hexChar);

        // Get all the hexCharList where gender is not null
        defaultHexCharFiltering("gender.specified=true", "gender.specified=false");
    }

    @Test
    @Transactional
    void getAllHexCharsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHexChar = hexCharRepository.saveAndFlush(hexChar);

        // Get all the hexCharList where phone equals to
        defaultHexCharFiltering("phone.equals=" + DEFAULT_PHONE, "phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllHexCharsByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHexChar = hexCharRepository.saveAndFlush(hexChar);

        // Get all the hexCharList where phone in
        defaultHexCharFiltering("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE, "phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllHexCharsByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHexChar = hexCharRepository.saveAndFlush(hexChar);

        // Get all the hexCharList where phone is not null
        defaultHexCharFiltering("phone.specified=true", "phone.specified=false");
    }

    @Test
    @Transactional
    void getAllHexCharsByPhoneContainsSomething() throws Exception {
        // Initialize the database
        insertedHexChar = hexCharRepository.saveAndFlush(hexChar);

        // Get all the hexCharList where phone contains
        defaultHexCharFiltering("phone.contains=" + DEFAULT_PHONE, "phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllHexCharsByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHexChar = hexCharRepository.saveAndFlush(hexChar);

        // Get all the hexCharList where phone does not contain
        defaultHexCharFiltering("phone.doesNotContain=" + UPDATED_PHONE, "phone.doesNotContain=" + DEFAULT_PHONE);
    }

    @Test
    @Transactional
    void getAllHexCharsByBioHeitigaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHexChar = hexCharRepository.saveAndFlush(hexChar);

        // Get all the hexCharList where bioHeitiga equals to
        defaultHexCharFiltering("bioHeitiga.equals=" + DEFAULT_BIO_HEITIGA, "bioHeitiga.equals=" + UPDATED_BIO_HEITIGA);
    }

    @Test
    @Transactional
    void getAllHexCharsByBioHeitigaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHexChar = hexCharRepository.saveAndFlush(hexChar);

        // Get all the hexCharList where bioHeitiga in
        defaultHexCharFiltering("bioHeitiga.in=" + DEFAULT_BIO_HEITIGA + "," + UPDATED_BIO_HEITIGA, "bioHeitiga.in=" + UPDATED_BIO_HEITIGA);
    }

    @Test
    @Transactional
    void getAllHexCharsByBioHeitigaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHexChar = hexCharRepository.saveAndFlush(hexChar);

        // Get all the hexCharList where bioHeitiga is not null
        defaultHexCharFiltering("bioHeitiga.specified=true", "bioHeitiga.specified=false");
    }

    @Test
    @Transactional
    void getAllHexCharsByBioHeitigaContainsSomething() throws Exception {
        // Initialize the database
        insertedHexChar = hexCharRepository.saveAndFlush(hexChar);

        // Get all the hexCharList where bioHeitiga contains
        defaultHexCharFiltering("bioHeitiga.contains=" + DEFAULT_BIO_HEITIGA, "bioHeitiga.contains=" + UPDATED_BIO_HEITIGA);
    }

    @Test
    @Transactional
    void getAllHexCharsByBioHeitigaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHexChar = hexCharRepository.saveAndFlush(hexChar);

        // Get all the hexCharList where bioHeitiga does not contain
        defaultHexCharFiltering("bioHeitiga.doesNotContain=" + UPDATED_BIO_HEITIGA, "bioHeitiga.doesNotContain=" + DEFAULT_BIO_HEITIGA);
    }

    @Test
    @Transactional
    void getAllHexCharsByIsEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHexChar = hexCharRepository.saveAndFlush(hexChar);

        // Get all the hexCharList where isEnabled equals to
        defaultHexCharFiltering("isEnabled.equals=" + DEFAULT_IS_ENABLED, "isEnabled.equals=" + UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    void getAllHexCharsByIsEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHexChar = hexCharRepository.saveAndFlush(hexChar);

        // Get all the hexCharList where isEnabled in
        defaultHexCharFiltering("isEnabled.in=" + DEFAULT_IS_ENABLED + "," + UPDATED_IS_ENABLED, "isEnabled.in=" + UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    void getAllHexCharsByIsEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHexChar = hexCharRepository.saveAndFlush(hexChar);

        // Get all the hexCharList where isEnabled is not null
        defaultHexCharFiltering("isEnabled.specified=true", "isEnabled.specified=false");
    }

    @Test
    @Transactional
    void getAllHexCharsByInternalUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User internalUser = hexChar.getInternalUser();
        hexCharRepository.saveAndFlush(hexChar);
        Long internalUserId = internalUser.getId();
        // Get all the hexCharList where internalUser equals to internalUserId
        defaultHexCharShouldBeFound("internalUserId.equals=" + internalUserId);

        // Get all the hexCharList where internalUser equals to (internalUserId + 1)
        defaultHexCharShouldNotBeFound("internalUserId.equals=" + (internalUserId + 1));
    }

    @Test
    @Transactional
    void getAllHexCharsByRoleIsEqualToSomething() throws Exception {
        HashRoss role;
        if (TestUtil.findAll(em, HashRoss.class).isEmpty()) {
            hexCharRepository.saveAndFlush(hexChar);
            role = HashRossResourceIT.createEntity();
        } else {
            role = TestUtil.findAll(em, HashRoss.class).get(0);
        }
        em.persist(role);
        em.flush();
        hexChar.setRole(role);
        hexCharRepository.saveAndFlush(hexChar);
        Long roleId = role.getId();
        // Get all the hexCharList where role equals to roleId
        defaultHexCharShouldBeFound("roleId.equals=" + roleId);

        // Get all the hexCharList where role equals to (roleId + 1)
        defaultHexCharShouldNotBeFound("roleId.equals=" + (roleId + 1));
    }

    private void defaultHexCharFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultHexCharShouldBeFound(shouldBeFound);
        defaultHexCharShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHexCharShouldBeFound(String filter) throws Exception {
        restHexCharMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hexChar.getId().intValue())))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].bioHeitiga").value(hasItem(DEFAULT_BIO_HEITIGA)))
            .andExpect(jsonPath("$.[*].isEnabled").value(hasItem(DEFAULT_IS_ENABLED.booleanValue())));

        // Check, that the count call also returns 1
        restHexCharMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHexCharShouldNotBeFound(String filter) throws Exception {
        restHexCharMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHexCharMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHexChar() throws Exception {
        // Get the hexChar
        restHexCharMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHexChar() throws Exception {
        // Initialize the database
        insertedHexChar = hexCharRepository.saveAndFlush(hexChar);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hexChar
        HexChar updatedHexChar = hexCharRepository.findById(hexChar.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHexChar are not directly saved in db
        em.detach(updatedHexChar);
        updatedHexChar
            .dob(UPDATED_DOB)
            .gender(UPDATED_GENDER)
            .phone(UPDATED_PHONE)
            .bioHeitiga(UPDATED_BIO_HEITIGA)
            .isEnabled(UPDATED_IS_ENABLED);
        HexCharDTO hexCharDTO = hexCharMapper.toDto(updatedHexChar);

        restHexCharMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hexCharDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hexCharDTO))
            )
            .andExpect(status().isOk());

        // Validate the HexChar in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHexCharToMatchAllProperties(updatedHexChar);
    }

    @Test
    @Transactional
    void putNonExistingHexChar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hexChar.setId(longCount.incrementAndGet());

        // Create the HexChar
        HexCharDTO hexCharDTO = hexCharMapper.toDto(hexChar);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHexCharMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hexCharDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hexCharDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HexChar in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHexChar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hexChar.setId(longCount.incrementAndGet());

        // Create the HexChar
        HexCharDTO hexCharDTO = hexCharMapper.toDto(hexChar);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHexCharMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hexCharDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HexChar in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHexChar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hexChar.setId(longCount.incrementAndGet());

        // Create the HexChar
        HexCharDTO hexCharDTO = hexCharMapper.toDto(hexChar);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHexCharMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hexCharDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HexChar in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHexCharWithPatch() throws Exception {
        // Initialize the database
        insertedHexChar = hexCharRepository.saveAndFlush(hexChar);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hexChar using partial update
        HexChar partialUpdatedHexChar = new HexChar();
        partialUpdatedHexChar.setId(hexChar.getId());

        partialUpdatedHexChar.dob(UPDATED_DOB);

        restHexCharMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHexChar.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHexChar))
            )
            .andExpect(status().isOk());

        // Validate the HexChar in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHexCharUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedHexChar, hexChar), getPersistedHexChar(hexChar));
    }

    @Test
    @Transactional
    void fullUpdateHexCharWithPatch() throws Exception {
        // Initialize the database
        insertedHexChar = hexCharRepository.saveAndFlush(hexChar);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hexChar using partial update
        HexChar partialUpdatedHexChar = new HexChar();
        partialUpdatedHexChar.setId(hexChar.getId());

        partialUpdatedHexChar
            .dob(UPDATED_DOB)
            .gender(UPDATED_GENDER)
            .phone(UPDATED_PHONE)
            .bioHeitiga(UPDATED_BIO_HEITIGA)
            .isEnabled(UPDATED_IS_ENABLED);

        restHexCharMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHexChar.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHexChar))
            )
            .andExpect(status().isOk());

        // Validate the HexChar in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHexCharUpdatableFieldsEquals(partialUpdatedHexChar, getPersistedHexChar(partialUpdatedHexChar));
    }

    @Test
    @Transactional
    void patchNonExistingHexChar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hexChar.setId(longCount.incrementAndGet());

        // Create the HexChar
        HexCharDTO hexCharDTO = hexCharMapper.toDto(hexChar);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHexCharMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hexCharDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hexCharDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HexChar in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHexChar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hexChar.setId(longCount.incrementAndGet());

        // Create the HexChar
        HexCharDTO hexCharDTO = hexCharMapper.toDto(hexChar);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHexCharMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hexCharDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HexChar in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHexChar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hexChar.setId(longCount.incrementAndGet());

        // Create the HexChar
        HexCharDTO hexCharDTO = hexCharMapper.toDto(hexChar);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHexCharMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(hexCharDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HexChar in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHexChar() throws Exception {
        // Initialize the database
        insertedHexChar = hexCharRepository.saveAndFlush(hexChar);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the hexChar
        restHexCharMockMvc
            .perform(delete(ENTITY_API_URL_ID, hexChar.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return hexCharRepository.count();
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

    protected HexChar getPersistedHexChar(HexChar hexChar) {
        return hexCharRepository.findById(hexChar.getId()).orElseThrow();
    }

    protected void assertPersistedHexCharToMatchAllProperties(HexChar expectedHexChar) {
        assertHexCharAllPropertiesEquals(expectedHexChar, getPersistedHexChar(expectedHexChar));
    }

    protected void assertPersistedHexCharToMatchUpdatableProperties(HexChar expectedHexChar) {
        assertHexCharAllUpdatablePropertiesEquals(expectedHexChar, getPersistedHexChar(expectedHexChar));
    }
}
