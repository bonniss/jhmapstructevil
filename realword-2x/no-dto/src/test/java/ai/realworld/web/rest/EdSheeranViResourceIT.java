package ai.realworld.web.rest;

import static ai.realworld.domain.EdSheeranViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlAppleVi;
import ai.realworld.domain.AlPacino;
import ai.realworld.domain.EdSheeranVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.domain.User;
import ai.realworld.domain.enumeration.TyrantSex;
import ai.realworld.repository.EdSheeranViRepository;
import ai.realworld.repository.UserRepository;
import ai.realworld.service.EdSheeranViService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
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
 * Integration tests for the {@link EdSheeranViResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EdSheeranViResourceIT {

    private static final String DEFAULT_FAMILY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FAMILY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GIVEN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GIVEN_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DISPLAY = "AAAAAAAAAA";
    private static final String UPDATED_DISPLAY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DOB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DOB = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DOB = LocalDate.ofEpochDay(-1L);

    private static final TyrantSex DEFAULT_GENDER = TyrantSex.MALE;
    private static final TyrantSex UPDATED_GENDER = TyrantSex.FEMALE;

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACTS_JASON = "AAAAAAAAAA";
    private static final String UPDATED_CONTACTS_JASON = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ENABLED = false;
    private static final Boolean UPDATED_IS_ENABLED = true;

    private static final String ENTITY_API_URL = "/api/ed-sheeran-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EdSheeranViRepository edSheeranViRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private EdSheeranViRepository edSheeranViRepositoryMock;

    @Mock
    private EdSheeranViService edSheeranViServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEdSheeranViMockMvc;

    private EdSheeranVi edSheeranVi;

    private EdSheeranVi insertedEdSheeranVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EdSheeranVi createEntity(EntityManager em) {
        EdSheeranVi edSheeranVi = new EdSheeranVi()
            .familyName(DEFAULT_FAMILY_NAME)
            .givenName(DEFAULT_GIVEN_NAME)
            .display(DEFAULT_DISPLAY)
            .dob(DEFAULT_DOB)
            .gender(DEFAULT_GENDER)
            .phone(DEFAULT_PHONE)
            .contactsJason(DEFAULT_CONTACTS_JASON)
            .isEnabled(DEFAULT_IS_ENABLED);
        // Add required entity
        AlAppleVi alAppleVi;
        if (TestUtil.findAll(em, AlAppleVi.class).isEmpty()) {
            alAppleVi = AlAppleViResourceIT.createEntity(em);
            em.persist(alAppleVi);
            em.flush();
        } else {
            alAppleVi = TestUtil.findAll(em, AlAppleVi.class).get(0);
        }
        edSheeranVi.setAgency(alAppleVi);
        return edSheeranVi;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EdSheeranVi createUpdatedEntity(EntityManager em) {
        EdSheeranVi updatedEdSheeranVi = new EdSheeranVi()
            .familyName(UPDATED_FAMILY_NAME)
            .givenName(UPDATED_GIVEN_NAME)
            .display(UPDATED_DISPLAY)
            .dob(UPDATED_DOB)
            .gender(UPDATED_GENDER)
            .phone(UPDATED_PHONE)
            .contactsJason(UPDATED_CONTACTS_JASON)
            .isEnabled(UPDATED_IS_ENABLED);
        // Add required entity
        AlAppleVi alAppleVi;
        if (TestUtil.findAll(em, AlAppleVi.class).isEmpty()) {
            alAppleVi = AlAppleViResourceIT.createUpdatedEntity(em);
            em.persist(alAppleVi);
            em.flush();
        } else {
            alAppleVi = TestUtil.findAll(em, AlAppleVi.class).get(0);
        }
        updatedEdSheeranVi.setAgency(alAppleVi);
        return updatedEdSheeranVi;
    }

    @BeforeEach
    public void initTest() {
        edSheeranVi = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedEdSheeranVi != null) {
            edSheeranViRepository.delete(insertedEdSheeranVi);
            insertedEdSheeranVi = null;
        }
    }

    @Test
    @Transactional
    void createEdSheeranVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EdSheeranVi
        var returnedEdSheeranVi = om.readValue(
            restEdSheeranViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(edSheeranVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EdSheeranVi.class
        );

        // Validate the EdSheeranVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEdSheeranViUpdatableFieldsEquals(returnedEdSheeranVi, getPersistedEdSheeranVi(returnedEdSheeranVi));

        insertedEdSheeranVi = returnedEdSheeranVi;
    }

    @Test
    @Transactional
    void createEdSheeranViWithExistingId() throws Exception {
        // Create the EdSheeranVi with an existing ID
        edSheeranVi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEdSheeranViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(edSheeranVi)))
            .andExpect(status().isBadRequest());

        // Validate the EdSheeranVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFamilyNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        edSheeranVi.setFamilyName(null);

        // Create the EdSheeranVi, which fails.

        restEdSheeranViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(edSheeranVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGivenNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        edSheeranVi.setGivenName(null);

        // Create the EdSheeranVi, which fails.

        restEdSheeranViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(edSheeranVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEdSheeranVis() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList
        restEdSheeranViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(edSheeranVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].familyName").value(hasItem(DEFAULT_FAMILY_NAME)))
            .andExpect(jsonPath("$.[*].givenName").value(hasItem(DEFAULT_GIVEN_NAME)))
            .andExpect(jsonPath("$.[*].display").value(hasItem(DEFAULT_DISPLAY)))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].contactsJason").value(hasItem(DEFAULT_CONTACTS_JASON)))
            .andExpect(jsonPath("$.[*].isEnabled").value(hasItem(DEFAULT_IS_ENABLED.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEdSheeranVisWithEagerRelationshipsIsEnabled() throws Exception {
        when(edSheeranViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEdSheeranViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(edSheeranViServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEdSheeranVisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(edSheeranViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEdSheeranViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(edSheeranViRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEdSheeranVi() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get the edSheeranVi
        restEdSheeranViMockMvc
            .perform(get(ENTITY_API_URL_ID, edSheeranVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(edSheeranVi.getId().intValue()))
            .andExpect(jsonPath("$.familyName").value(DEFAULT_FAMILY_NAME))
            .andExpect(jsonPath("$.givenName").value(DEFAULT_GIVEN_NAME))
            .andExpect(jsonPath("$.display").value(DEFAULT_DISPLAY))
            .andExpect(jsonPath("$.dob").value(DEFAULT_DOB.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.contactsJason").value(DEFAULT_CONTACTS_JASON))
            .andExpect(jsonPath("$.isEnabled").value(DEFAULT_IS_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    void getEdSheeranVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        Long id = edSheeranVi.getId();

        defaultEdSheeranViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEdSheeranViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEdSheeranViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByFamilyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where familyName equals to
        defaultEdSheeranViFiltering("familyName.equals=" + DEFAULT_FAMILY_NAME, "familyName.equals=" + UPDATED_FAMILY_NAME);
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByFamilyNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where familyName in
        defaultEdSheeranViFiltering(
            "familyName.in=" + DEFAULT_FAMILY_NAME + "," + UPDATED_FAMILY_NAME,
            "familyName.in=" + UPDATED_FAMILY_NAME
        );
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByFamilyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where familyName is not null
        defaultEdSheeranViFiltering("familyName.specified=true", "familyName.specified=false");
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByFamilyNameContainsSomething() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where familyName contains
        defaultEdSheeranViFiltering("familyName.contains=" + DEFAULT_FAMILY_NAME, "familyName.contains=" + UPDATED_FAMILY_NAME);
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByFamilyNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where familyName does not contain
        defaultEdSheeranViFiltering("familyName.doesNotContain=" + UPDATED_FAMILY_NAME, "familyName.doesNotContain=" + DEFAULT_FAMILY_NAME);
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByGivenNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where givenName equals to
        defaultEdSheeranViFiltering("givenName.equals=" + DEFAULT_GIVEN_NAME, "givenName.equals=" + UPDATED_GIVEN_NAME);
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByGivenNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where givenName in
        defaultEdSheeranViFiltering("givenName.in=" + DEFAULT_GIVEN_NAME + "," + UPDATED_GIVEN_NAME, "givenName.in=" + UPDATED_GIVEN_NAME);
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByGivenNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where givenName is not null
        defaultEdSheeranViFiltering("givenName.specified=true", "givenName.specified=false");
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByGivenNameContainsSomething() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where givenName contains
        defaultEdSheeranViFiltering("givenName.contains=" + DEFAULT_GIVEN_NAME, "givenName.contains=" + UPDATED_GIVEN_NAME);
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByGivenNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where givenName does not contain
        defaultEdSheeranViFiltering("givenName.doesNotContain=" + UPDATED_GIVEN_NAME, "givenName.doesNotContain=" + DEFAULT_GIVEN_NAME);
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByDisplayIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where display equals to
        defaultEdSheeranViFiltering("display.equals=" + DEFAULT_DISPLAY, "display.equals=" + UPDATED_DISPLAY);
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByDisplayIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where display in
        defaultEdSheeranViFiltering("display.in=" + DEFAULT_DISPLAY + "," + UPDATED_DISPLAY, "display.in=" + UPDATED_DISPLAY);
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByDisplayIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where display is not null
        defaultEdSheeranViFiltering("display.specified=true", "display.specified=false");
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByDisplayContainsSomething() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where display contains
        defaultEdSheeranViFiltering("display.contains=" + DEFAULT_DISPLAY, "display.contains=" + UPDATED_DISPLAY);
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByDisplayNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where display does not contain
        defaultEdSheeranViFiltering("display.doesNotContain=" + UPDATED_DISPLAY, "display.doesNotContain=" + DEFAULT_DISPLAY);
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByDobIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where dob equals to
        defaultEdSheeranViFiltering("dob.equals=" + DEFAULT_DOB, "dob.equals=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByDobIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where dob in
        defaultEdSheeranViFiltering("dob.in=" + DEFAULT_DOB + "," + UPDATED_DOB, "dob.in=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByDobIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where dob is not null
        defaultEdSheeranViFiltering("dob.specified=true", "dob.specified=false");
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByDobIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where dob is greater than or equal to
        defaultEdSheeranViFiltering("dob.greaterThanOrEqual=" + DEFAULT_DOB, "dob.greaterThanOrEqual=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByDobIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where dob is less than or equal to
        defaultEdSheeranViFiltering("dob.lessThanOrEqual=" + DEFAULT_DOB, "dob.lessThanOrEqual=" + SMALLER_DOB);
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByDobIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where dob is less than
        defaultEdSheeranViFiltering("dob.lessThan=" + UPDATED_DOB, "dob.lessThan=" + DEFAULT_DOB);
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByDobIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where dob is greater than
        defaultEdSheeranViFiltering("dob.greaterThan=" + SMALLER_DOB, "dob.greaterThan=" + DEFAULT_DOB);
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where gender equals to
        defaultEdSheeranViFiltering("gender.equals=" + DEFAULT_GENDER, "gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where gender in
        defaultEdSheeranViFiltering("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER, "gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where gender is not null
        defaultEdSheeranViFiltering("gender.specified=true", "gender.specified=false");
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where phone equals to
        defaultEdSheeranViFiltering("phone.equals=" + DEFAULT_PHONE, "phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where phone in
        defaultEdSheeranViFiltering("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE, "phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where phone is not null
        defaultEdSheeranViFiltering("phone.specified=true", "phone.specified=false");
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByPhoneContainsSomething() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where phone contains
        defaultEdSheeranViFiltering("phone.contains=" + DEFAULT_PHONE, "phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where phone does not contain
        defaultEdSheeranViFiltering("phone.doesNotContain=" + UPDATED_PHONE, "phone.doesNotContain=" + DEFAULT_PHONE);
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByContactsJasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where contactsJason equals to
        defaultEdSheeranViFiltering("contactsJason.equals=" + DEFAULT_CONTACTS_JASON, "contactsJason.equals=" + UPDATED_CONTACTS_JASON);
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByContactsJasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where contactsJason in
        defaultEdSheeranViFiltering(
            "contactsJason.in=" + DEFAULT_CONTACTS_JASON + "," + UPDATED_CONTACTS_JASON,
            "contactsJason.in=" + UPDATED_CONTACTS_JASON
        );
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByContactsJasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where contactsJason is not null
        defaultEdSheeranViFiltering("contactsJason.specified=true", "contactsJason.specified=false");
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByContactsJasonContainsSomething() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where contactsJason contains
        defaultEdSheeranViFiltering("contactsJason.contains=" + DEFAULT_CONTACTS_JASON, "contactsJason.contains=" + UPDATED_CONTACTS_JASON);
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByContactsJasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where contactsJason does not contain
        defaultEdSheeranViFiltering(
            "contactsJason.doesNotContain=" + UPDATED_CONTACTS_JASON,
            "contactsJason.doesNotContain=" + DEFAULT_CONTACTS_JASON
        );
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByIsEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where isEnabled equals to
        defaultEdSheeranViFiltering("isEnabled.equals=" + DEFAULT_IS_ENABLED, "isEnabled.equals=" + UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByIsEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where isEnabled in
        defaultEdSheeranViFiltering("isEnabled.in=" + DEFAULT_IS_ENABLED + "," + UPDATED_IS_ENABLED, "isEnabled.in=" + UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByIsEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        // Get all the edSheeranViList where isEnabled is not null
        defaultEdSheeranViFiltering("isEnabled.specified=true", "isEnabled.specified=false");
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByAgencyIsEqualToSomething() throws Exception {
        AlAppleVi agency;
        if (TestUtil.findAll(em, AlAppleVi.class).isEmpty()) {
            edSheeranViRepository.saveAndFlush(edSheeranVi);
            agency = AlAppleViResourceIT.createEntity(em);
        } else {
            agency = TestUtil.findAll(em, AlAppleVi.class).get(0);
        }
        em.persist(agency);
        em.flush();
        edSheeranVi.setAgency(agency);
        edSheeranViRepository.saveAndFlush(edSheeranVi);
        UUID agencyId = agency.getId();
        // Get all the edSheeranViList where agency equals to agencyId
        defaultEdSheeranViShouldBeFound("agencyId.equals=" + agencyId);

        // Get all the edSheeranViList where agency equals to UUID.randomUUID()
        defaultEdSheeranViShouldNotBeFound("agencyId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByAvatarIsEqualToSomething() throws Exception {
        Metaverse avatar;
        if (TestUtil.findAll(em, Metaverse.class).isEmpty()) {
            edSheeranViRepository.saveAndFlush(edSheeranVi);
            avatar = MetaverseResourceIT.createEntity();
        } else {
            avatar = TestUtil.findAll(em, Metaverse.class).get(0);
        }
        em.persist(avatar);
        em.flush();
        edSheeranVi.setAvatar(avatar);
        edSheeranViRepository.saveAndFlush(edSheeranVi);
        Long avatarId = avatar.getId();
        // Get all the edSheeranViList where avatar equals to avatarId
        defaultEdSheeranViShouldBeFound("avatarId.equals=" + avatarId);

        // Get all the edSheeranViList where avatar equals to (avatarId + 1)
        defaultEdSheeranViShouldNotBeFound("avatarId.equals=" + (avatarId + 1));
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByInternalUserIsEqualToSomething() throws Exception {
        User internalUser;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            edSheeranViRepository.saveAndFlush(edSheeranVi);
            internalUser = UserResourceIT.createEntity();
        } else {
            internalUser = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(internalUser);
        em.flush();
        edSheeranVi.setInternalUser(internalUser);
        edSheeranViRepository.saveAndFlush(edSheeranVi);
        Long internalUserId = internalUser.getId();
        // Get all the edSheeranViList where internalUser equals to internalUserId
        defaultEdSheeranViShouldBeFound("internalUserId.equals=" + internalUserId);

        // Get all the edSheeranViList where internalUser equals to (internalUserId + 1)
        defaultEdSheeranViShouldNotBeFound("internalUserId.equals=" + (internalUserId + 1));
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByAppUserIsEqualToSomething() throws Exception {
        AlPacino appUser;
        if (TestUtil.findAll(em, AlPacino.class).isEmpty()) {
            edSheeranViRepository.saveAndFlush(edSheeranVi);
            appUser = AlPacinoResourceIT.createEntity();
        } else {
            appUser = TestUtil.findAll(em, AlPacino.class).get(0);
        }
        em.persist(appUser);
        em.flush();
        edSheeranVi.setAppUser(appUser);
        edSheeranViRepository.saveAndFlush(edSheeranVi);
        UUID appUserId = appUser.getId();
        // Get all the edSheeranViList where appUser equals to appUserId
        defaultEdSheeranViShouldBeFound("appUserId.equals=" + appUserId);

        // Get all the edSheeranViList where appUser equals to UUID.randomUUID()
        defaultEdSheeranViShouldNotBeFound("appUserId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllEdSheeranVisByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            edSheeranViRepository.saveAndFlush(edSheeranVi);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        edSheeranVi.setApplication(application);
        edSheeranViRepository.saveAndFlush(edSheeranVi);
        UUID applicationId = application.getId();
        // Get all the edSheeranViList where application equals to applicationId
        defaultEdSheeranViShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the edSheeranViList where application equals to UUID.randomUUID()
        defaultEdSheeranViShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultEdSheeranViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEdSheeranViShouldBeFound(shouldBeFound);
        defaultEdSheeranViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEdSheeranViShouldBeFound(String filter) throws Exception {
        restEdSheeranViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(edSheeranVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].familyName").value(hasItem(DEFAULT_FAMILY_NAME)))
            .andExpect(jsonPath("$.[*].givenName").value(hasItem(DEFAULT_GIVEN_NAME)))
            .andExpect(jsonPath("$.[*].display").value(hasItem(DEFAULT_DISPLAY)))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].contactsJason").value(hasItem(DEFAULT_CONTACTS_JASON)))
            .andExpect(jsonPath("$.[*].isEnabled").value(hasItem(DEFAULT_IS_ENABLED.booleanValue())));

        // Check, that the count call also returns 1
        restEdSheeranViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEdSheeranViShouldNotBeFound(String filter) throws Exception {
        restEdSheeranViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEdSheeranViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEdSheeranVi() throws Exception {
        // Get the edSheeranVi
        restEdSheeranViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEdSheeranVi() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the edSheeranVi
        EdSheeranVi updatedEdSheeranVi = edSheeranViRepository.findById(edSheeranVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEdSheeranVi are not directly saved in db
        em.detach(updatedEdSheeranVi);
        updatedEdSheeranVi
            .familyName(UPDATED_FAMILY_NAME)
            .givenName(UPDATED_GIVEN_NAME)
            .display(UPDATED_DISPLAY)
            .dob(UPDATED_DOB)
            .gender(UPDATED_GENDER)
            .phone(UPDATED_PHONE)
            .contactsJason(UPDATED_CONTACTS_JASON)
            .isEnabled(UPDATED_IS_ENABLED);

        restEdSheeranViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEdSheeranVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedEdSheeranVi))
            )
            .andExpect(status().isOk());

        // Validate the EdSheeranVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEdSheeranViToMatchAllProperties(updatedEdSheeranVi);
    }

    @Test
    @Transactional
    void putNonExistingEdSheeranVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        edSheeranVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEdSheeranViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, edSheeranVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(edSheeranVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the EdSheeranVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEdSheeranVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        edSheeranVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEdSheeranViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(edSheeranVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the EdSheeranVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEdSheeranVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        edSheeranVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEdSheeranViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(edSheeranVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EdSheeranVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEdSheeranViWithPatch() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the edSheeranVi using partial update
        EdSheeranVi partialUpdatedEdSheeranVi = new EdSheeranVi();
        partialUpdatedEdSheeranVi.setId(edSheeranVi.getId());

        partialUpdatedEdSheeranVi
            .givenName(UPDATED_GIVEN_NAME)
            .display(UPDATED_DISPLAY)
            .dob(UPDATED_DOB)
            .gender(UPDATED_GENDER)
            .isEnabled(UPDATED_IS_ENABLED);

        restEdSheeranViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEdSheeranVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEdSheeranVi))
            )
            .andExpect(status().isOk());

        // Validate the EdSheeranVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEdSheeranViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEdSheeranVi, edSheeranVi),
            getPersistedEdSheeranVi(edSheeranVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateEdSheeranViWithPatch() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the edSheeranVi using partial update
        EdSheeranVi partialUpdatedEdSheeranVi = new EdSheeranVi();
        partialUpdatedEdSheeranVi.setId(edSheeranVi.getId());

        partialUpdatedEdSheeranVi
            .familyName(UPDATED_FAMILY_NAME)
            .givenName(UPDATED_GIVEN_NAME)
            .display(UPDATED_DISPLAY)
            .dob(UPDATED_DOB)
            .gender(UPDATED_GENDER)
            .phone(UPDATED_PHONE)
            .contactsJason(UPDATED_CONTACTS_JASON)
            .isEnabled(UPDATED_IS_ENABLED);

        restEdSheeranViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEdSheeranVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEdSheeranVi))
            )
            .andExpect(status().isOk());

        // Validate the EdSheeranVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEdSheeranViUpdatableFieldsEquals(partialUpdatedEdSheeranVi, getPersistedEdSheeranVi(partialUpdatedEdSheeranVi));
    }

    @Test
    @Transactional
    void patchNonExistingEdSheeranVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        edSheeranVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEdSheeranViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, edSheeranVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(edSheeranVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the EdSheeranVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEdSheeranVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        edSheeranVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEdSheeranViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(edSheeranVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the EdSheeranVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEdSheeranVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        edSheeranVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEdSheeranViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(edSheeranVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EdSheeranVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEdSheeranVi() throws Exception {
        // Initialize the database
        insertedEdSheeranVi = edSheeranViRepository.saveAndFlush(edSheeranVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the edSheeranVi
        restEdSheeranViMockMvc
            .perform(delete(ENTITY_API_URL_ID, edSheeranVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return edSheeranViRepository.count();
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

    protected EdSheeranVi getPersistedEdSheeranVi(EdSheeranVi edSheeranVi) {
        return edSheeranViRepository.findById(edSheeranVi.getId()).orElseThrow();
    }

    protected void assertPersistedEdSheeranViToMatchAllProperties(EdSheeranVi expectedEdSheeranVi) {
        assertEdSheeranViAllPropertiesEquals(expectedEdSheeranVi, getPersistedEdSheeranVi(expectedEdSheeranVi));
    }

    protected void assertPersistedEdSheeranViToMatchUpdatableProperties(EdSheeranVi expectedEdSheeranVi) {
        assertEdSheeranViAllUpdatablePropertiesEquals(expectedEdSheeranVi, getPersistedEdSheeranVi(expectedEdSheeranVi));
    }
}
