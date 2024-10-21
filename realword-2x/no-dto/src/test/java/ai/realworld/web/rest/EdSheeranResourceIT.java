package ai.realworld.web.rest;

import static ai.realworld.domain.EdSheeranAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlApple;
import ai.realworld.domain.AlPacino;
import ai.realworld.domain.EdSheeran;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.domain.User;
import ai.realworld.domain.enumeration.TyrantSex;
import ai.realworld.repository.EdSheeranRepository;
import ai.realworld.repository.UserRepository;
import ai.realworld.service.EdSheeranService;
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
 * Integration tests for the {@link EdSheeranResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EdSheeranResourceIT {

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

    private static final String ENTITY_API_URL = "/api/ed-sheerans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EdSheeranRepository edSheeranRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private EdSheeranRepository edSheeranRepositoryMock;

    @Mock
    private EdSheeranService edSheeranServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEdSheeranMockMvc;

    private EdSheeran edSheeran;

    private EdSheeran insertedEdSheeran;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EdSheeran createEntity(EntityManager em) {
        EdSheeran edSheeran = new EdSheeran()
            .familyName(DEFAULT_FAMILY_NAME)
            .givenName(DEFAULT_GIVEN_NAME)
            .display(DEFAULT_DISPLAY)
            .dob(DEFAULT_DOB)
            .gender(DEFAULT_GENDER)
            .phone(DEFAULT_PHONE)
            .contactsJason(DEFAULT_CONTACTS_JASON)
            .isEnabled(DEFAULT_IS_ENABLED);
        // Add required entity
        AlApple alApple;
        if (TestUtil.findAll(em, AlApple.class).isEmpty()) {
            alApple = AlAppleResourceIT.createEntity(em);
            em.persist(alApple);
            em.flush();
        } else {
            alApple = TestUtil.findAll(em, AlApple.class).get(0);
        }
        edSheeran.setAgency(alApple);
        return edSheeran;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EdSheeran createUpdatedEntity(EntityManager em) {
        EdSheeran updatedEdSheeran = new EdSheeran()
            .familyName(UPDATED_FAMILY_NAME)
            .givenName(UPDATED_GIVEN_NAME)
            .display(UPDATED_DISPLAY)
            .dob(UPDATED_DOB)
            .gender(UPDATED_GENDER)
            .phone(UPDATED_PHONE)
            .contactsJason(UPDATED_CONTACTS_JASON)
            .isEnabled(UPDATED_IS_ENABLED);
        // Add required entity
        AlApple alApple;
        if (TestUtil.findAll(em, AlApple.class).isEmpty()) {
            alApple = AlAppleResourceIT.createUpdatedEntity(em);
            em.persist(alApple);
            em.flush();
        } else {
            alApple = TestUtil.findAll(em, AlApple.class).get(0);
        }
        updatedEdSheeran.setAgency(alApple);
        return updatedEdSheeran;
    }

    @BeforeEach
    public void initTest() {
        edSheeran = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedEdSheeran != null) {
            edSheeranRepository.delete(insertedEdSheeran);
            insertedEdSheeran = null;
        }
    }

    @Test
    @Transactional
    void createEdSheeran() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EdSheeran
        var returnedEdSheeran = om.readValue(
            restEdSheeranMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(edSheeran)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EdSheeran.class
        );

        // Validate the EdSheeran in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEdSheeranUpdatableFieldsEquals(returnedEdSheeran, getPersistedEdSheeran(returnedEdSheeran));

        insertedEdSheeran = returnedEdSheeran;
    }

    @Test
    @Transactional
    void createEdSheeranWithExistingId() throws Exception {
        // Create the EdSheeran with an existing ID
        edSheeran.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEdSheeranMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(edSheeran)))
            .andExpect(status().isBadRequest());

        // Validate the EdSheeran in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFamilyNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        edSheeran.setFamilyName(null);

        // Create the EdSheeran, which fails.

        restEdSheeranMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(edSheeran)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGivenNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        edSheeran.setGivenName(null);

        // Create the EdSheeran, which fails.

        restEdSheeranMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(edSheeran)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEdSheerans() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList
        restEdSheeranMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(edSheeran.getId().intValue())))
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
    void getAllEdSheeransWithEagerRelationshipsIsEnabled() throws Exception {
        when(edSheeranServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEdSheeranMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(edSheeranServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEdSheeransWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(edSheeranServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEdSheeranMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(edSheeranRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEdSheeran() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get the edSheeran
        restEdSheeranMockMvc
            .perform(get(ENTITY_API_URL_ID, edSheeran.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(edSheeran.getId().intValue()))
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
    void getEdSheeransByIdFiltering() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        Long id = edSheeran.getId();

        defaultEdSheeranFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEdSheeranFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEdSheeranFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEdSheeransByFamilyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where familyName equals to
        defaultEdSheeranFiltering("familyName.equals=" + DEFAULT_FAMILY_NAME, "familyName.equals=" + UPDATED_FAMILY_NAME);
    }

    @Test
    @Transactional
    void getAllEdSheeransByFamilyNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where familyName in
        defaultEdSheeranFiltering(
            "familyName.in=" + DEFAULT_FAMILY_NAME + "," + UPDATED_FAMILY_NAME,
            "familyName.in=" + UPDATED_FAMILY_NAME
        );
    }

    @Test
    @Transactional
    void getAllEdSheeransByFamilyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where familyName is not null
        defaultEdSheeranFiltering("familyName.specified=true", "familyName.specified=false");
    }

    @Test
    @Transactional
    void getAllEdSheeransByFamilyNameContainsSomething() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where familyName contains
        defaultEdSheeranFiltering("familyName.contains=" + DEFAULT_FAMILY_NAME, "familyName.contains=" + UPDATED_FAMILY_NAME);
    }

    @Test
    @Transactional
    void getAllEdSheeransByFamilyNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where familyName does not contain
        defaultEdSheeranFiltering("familyName.doesNotContain=" + UPDATED_FAMILY_NAME, "familyName.doesNotContain=" + DEFAULT_FAMILY_NAME);
    }

    @Test
    @Transactional
    void getAllEdSheeransByGivenNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where givenName equals to
        defaultEdSheeranFiltering("givenName.equals=" + DEFAULT_GIVEN_NAME, "givenName.equals=" + UPDATED_GIVEN_NAME);
    }

    @Test
    @Transactional
    void getAllEdSheeransByGivenNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where givenName in
        defaultEdSheeranFiltering("givenName.in=" + DEFAULT_GIVEN_NAME + "," + UPDATED_GIVEN_NAME, "givenName.in=" + UPDATED_GIVEN_NAME);
    }

    @Test
    @Transactional
    void getAllEdSheeransByGivenNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where givenName is not null
        defaultEdSheeranFiltering("givenName.specified=true", "givenName.specified=false");
    }

    @Test
    @Transactional
    void getAllEdSheeransByGivenNameContainsSomething() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where givenName contains
        defaultEdSheeranFiltering("givenName.contains=" + DEFAULT_GIVEN_NAME, "givenName.contains=" + UPDATED_GIVEN_NAME);
    }

    @Test
    @Transactional
    void getAllEdSheeransByGivenNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where givenName does not contain
        defaultEdSheeranFiltering("givenName.doesNotContain=" + UPDATED_GIVEN_NAME, "givenName.doesNotContain=" + DEFAULT_GIVEN_NAME);
    }

    @Test
    @Transactional
    void getAllEdSheeransByDisplayIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where display equals to
        defaultEdSheeranFiltering("display.equals=" + DEFAULT_DISPLAY, "display.equals=" + UPDATED_DISPLAY);
    }

    @Test
    @Transactional
    void getAllEdSheeransByDisplayIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where display in
        defaultEdSheeranFiltering("display.in=" + DEFAULT_DISPLAY + "," + UPDATED_DISPLAY, "display.in=" + UPDATED_DISPLAY);
    }

    @Test
    @Transactional
    void getAllEdSheeransByDisplayIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where display is not null
        defaultEdSheeranFiltering("display.specified=true", "display.specified=false");
    }

    @Test
    @Transactional
    void getAllEdSheeransByDisplayContainsSomething() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where display contains
        defaultEdSheeranFiltering("display.contains=" + DEFAULT_DISPLAY, "display.contains=" + UPDATED_DISPLAY);
    }

    @Test
    @Transactional
    void getAllEdSheeransByDisplayNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where display does not contain
        defaultEdSheeranFiltering("display.doesNotContain=" + UPDATED_DISPLAY, "display.doesNotContain=" + DEFAULT_DISPLAY);
    }

    @Test
    @Transactional
    void getAllEdSheeransByDobIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where dob equals to
        defaultEdSheeranFiltering("dob.equals=" + DEFAULT_DOB, "dob.equals=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllEdSheeransByDobIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where dob in
        defaultEdSheeranFiltering("dob.in=" + DEFAULT_DOB + "," + UPDATED_DOB, "dob.in=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllEdSheeransByDobIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where dob is not null
        defaultEdSheeranFiltering("dob.specified=true", "dob.specified=false");
    }

    @Test
    @Transactional
    void getAllEdSheeransByDobIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where dob is greater than or equal to
        defaultEdSheeranFiltering("dob.greaterThanOrEqual=" + DEFAULT_DOB, "dob.greaterThanOrEqual=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllEdSheeransByDobIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where dob is less than or equal to
        defaultEdSheeranFiltering("dob.lessThanOrEqual=" + DEFAULT_DOB, "dob.lessThanOrEqual=" + SMALLER_DOB);
    }

    @Test
    @Transactional
    void getAllEdSheeransByDobIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where dob is less than
        defaultEdSheeranFiltering("dob.lessThan=" + UPDATED_DOB, "dob.lessThan=" + DEFAULT_DOB);
    }

    @Test
    @Transactional
    void getAllEdSheeransByDobIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where dob is greater than
        defaultEdSheeranFiltering("dob.greaterThan=" + SMALLER_DOB, "dob.greaterThan=" + DEFAULT_DOB);
    }

    @Test
    @Transactional
    void getAllEdSheeransByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where gender equals to
        defaultEdSheeranFiltering("gender.equals=" + DEFAULT_GENDER, "gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllEdSheeransByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where gender in
        defaultEdSheeranFiltering("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER, "gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllEdSheeransByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where gender is not null
        defaultEdSheeranFiltering("gender.specified=true", "gender.specified=false");
    }

    @Test
    @Transactional
    void getAllEdSheeransByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where phone equals to
        defaultEdSheeranFiltering("phone.equals=" + DEFAULT_PHONE, "phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllEdSheeransByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where phone in
        defaultEdSheeranFiltering("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE, "phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllEdSheeransByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where phone is not null
        defaultEdSheeranFiltering("phone.specified=true", "phone.specified=false");
    }

    @Test
    @Transactional
    void getAllEdSheeransByPhoneContainsSomething() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where phone contains
        defaultEdSheeranFiltering("phone.contains=" + DEFAULT_PHONE, "phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllEdSheeransByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where phone does not contain
        defaultEdSheeranFiltering("phone.doesNotContain=" + UPDATED_PHONE, "phone.doesNotContain=" + DEFAULT_PHONE);
    }

    @Test
    @Transactional
    void getAllEdSheeransByContactsJasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where contactsJason equals to
        defaultEdSheeranFiltering("contactsJason.equals=" + DEFAULT_CONTACTS_JASON, "contactsJason.equals=" + UPDATED_CONTACTS_JASON);
    }

    @Test
    @Transactional
    void getAllEdSheeransByContactsJasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where contactsJason in
        defaultEdSheeranFiltering(
            "contactsJason.in=" + DEFAULT_CONTACTS_JASON + "," + UPDATED_CONTACTS_JASON,
            "contactsJason.in=" + UPDATED_CONTACTS_JASON
        );
    }

    @Test
    @Transactional
    void getAllEdSheeransByContactsJasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where contactsJason is not null
        defaultEdSheeranFiltering("contactsJason.specified=true", "contactsJason.specified=false");
    }

    @Test
    @Transactional
    void getAllEdSheeransByContactsJasonContainsSomething() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where contactsJason contains
        defaultEdSheeranFiltering("contactsJason.contains=" + DEFAULT_CONTACTS_JASON, "contactsJason.contains=" + UPDATED_CONTACTS_JASON);
    }

    @Test
    @Transactional
    void getAllEdSheeransByContactsJasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where contactsJason does not contain
        defaultEdSheeranFiltering(
            "contactsJason.doesNotContain=" + UPDATED_CONTACTS_JASON,
            "contactsJason.doesNotContain=" + DEFAULT_CONTACTS_JASON
        );
    }

    @Test
    @Transactional
    void getAllEdSheeransByIsEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where isEnabled equals to
        defaultEdSheeranFiltering("isEnabled.equals=" + DEFAULT_IS_ENABLED, "isEnabled.equals=" + UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    void getAllEdSheeransByIsEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where isEnabled in
        defaultEdSheeranFiltering("isEnabled.in=" + DEFAULT_IS_ENABLED + "," + UPDATED_IS_ENABLED, "isEnabled.in=" + UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    void getAllEdSheeransByIsEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        // Get all the edSheeranList where isEnabled is not null
        defaultEdSheeranFiltering("isEnabled.specified=true", "isEnabled.specified=false");
    }

    @Test
    @Transactional
    void getAllEdSheeransByAgencyIsEqualToSomething() throws Exception {
        AlApple agency;
        if (TestUtil.findAll(em, AlApple.class).isEmpty()) {
            edSheeranRepository.saveAndFlush(edSheeran);
            agency = AlAppleResourceIT.createEntity(em);
        } else {
            agency = TestUtil.findAll(em, AlApple.class).get(0);
        }
        em.persist(agency);
        em.flush();
        edSheeran.setAgency(agency);
        edSheeranRepository.saveAndFlush(edSheeran);
        UUID agencyId = agency.getId();
        // Get all the edSheeranList where agency equals to agencyId
        defaultEdSheeranShouldBeFound("agencyId.equals=" + agencyId);

        // Get all the edSheeranList where agency equals to UUID.randomUUID()
        defaultEdSheeranShouldNotBeFound("agencyId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllEdSheeransByAvatarIsEqualToSomething() throws Exception {
        Metaverse avatar;
        if (TestUtil.findAll(em, Metaverse.class).isEmpty()) {
            edSheeranRepository.saveAndFlush(edSheeran);
            avatar = MetaverseResourceIT.createEntity();
        } else {
            avatar = TestUtil.findAll(em, Metaverse.class).get(0);
        }
        em.persist(avatar);
        em.flush();
        edSheeran.setAvatar(avatar);
        edSheeranRepository.saveAndFlush(edSheeran);
        Long avatarId = avatar.getId();
        // Get all the edSheeranList where avatar equals to avatarId
        defaultEdSheeranShouldBeFound("avatarId.equals=" + avatarId);

        // Get all the edSheeranList where avatar equals to (avatarId + 1)
        defaultEdSheeranShouldNotBeFound("avatarId.equals=" + (avatarId + 1));
    }

    @Test
    @Transactional
    void getAllEdSheeransByInternalUserIsEqualToSomething() throws Exception {
        User internalUser;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            edSheeranRepository.saveAndFlush(edSheeran);
            internalUser = UserResourceIT.createEntity();
        } else {
            internalUser = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(internalUser);
        em.flush();
        edSheeran.setInternalUser(internalUser);
        edSheeranRepository.saveAndFlush(edSheeran);
        Long internalUserId = internalUser.getId();
        // Get all the edSheeranList where internalUser equals to internalUserId
        defaultEdSheeranShouldBeFound("internalUserId.equals=" + internalUserId);

        // Get all the edSheeranList where internalUser equals to (internalUserId + 1)
        defaultEdSheeranShouldNotBeFound("internalUserId.equals=" + (internalUserId + 1));
    }

    @Test
    @Transactional
    void getAllEdSheeransByAppUserIsEqualToSomething() throws Exception {
        AlPacino appUser;
        if (TestUtil.findAll(em, AlPacino.class).isEmpty()) {
            edSheeranRepository.saveAndFlush(edSheeran);
            appUser = AlPacinoResourceIT.createEntity();
        } else {
            appUser = TestUtil.findAll(em, AlPacino.class).get(0);
        }
        em.persist(appUser);
        em.flush();
        edSheeran.setAppUser(appUser);
        edSheeranRepository.saveAndFlush(edSheeran);
        UUID appUserId = appUser.getId();
        // Get all the edSheeranList where appUser equals to appUserId
        defaultEdSheeranShouldBeFound("appUserId.equals=" + appUserId);

        // Get all the edSheeranList where appUser equals to UUID.randomUUID()
        defaultEdSheeranShouldNotBeFound("appUserId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllEdSheeransByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            edSheeranRepository.saveAndFlush(edSheeran);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        edSheeran.setApplication(application);
        edSheeranRepository.saveAndFlush(edSheeran);
        UUID applicationId = application.getId();
        // Get all the edSheeranList where application equals to applicationId
        defaultEdSheeranShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the edSheeranList where application equals to UUID.randomUUID()
        defaultEdSheeranShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultEdSheeranFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEdSheeranShouldBeFound(shouldBeFound);
        defaultEdSheeranShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEdSheeranShouldBeFound(String filter) throws Exception {
        restEdSheeranMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(edSheeran.getId().intValue())))
            .andExpect(jsonPath("$.[*].familyName").value(hasItem(DEFAULT_FAMILY_NAME)))
            .andExpect(jsonPath("$.[*].givenName").value(hasItem(DEFAULT_GIVEN_NAME)))
            .andExpect(jsonPath("$.[*].display").value(hasItem(DEFAULT_DISPLAY)))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].contactsJason").value(hasItem(DEFAULT_CONTACTS_JASON)))
            .andExpect(jsonPath("$.[*].isEnabled").value(hasItem(DEFAULT_IS_ENABLED.booleanValue())));

        // Check, that the count call also returns 1
        restEdSheeranMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEdSheeranShouldNotBeFound(String filter) throws Exception {
        restEdSheeranMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEdSheeranMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEdSheeran() throws Exception {
        // Get the edSheeran
        restEdSheeranMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEdSheeran() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the edSheeran
        EdSheeran updatedEdSheeran = edSheeranRepository.findById(edSheeran.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEdSheeran are not directly saved in db
        em.detach(updatedEdSheeran);
        updatedEdSheeran
            .familyName(UPDATED_FAMILY_NAME)
            .givenName(UPDATED_GIVEN_NAME)
            .display(UPDATED_DISPLAY)
            .dob(UPDATED_DOB)
            .gender(UPDATED_GENDER)
            .phone(UPDATED_PHONE)
            .contactsJason(UPDATED_CONTACTS_JASON)
            .isEnabled(UPDATED_IS_ENABLED);

        restEdSheeranMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEdSheeran.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedEdSheeran))
            )
            .andExpect(status().isOk());

        // Validate the EdSheeran in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEdSheeranToMatchAllProperties(updatedEdSheeran);
    }

    @Test
    @Transactional
    void putNonExistingEdSheeran() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        edSheeran.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEdSheeranMockMvc
            .perform(
                put(ENTITY_API_URL_ID, edSheeran.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(edSheeran))
            )
            .andExpect(status().isBadRequest());

        // Validate the EdSheeran in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEdSheeran() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        edSheeran.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEdSheeranMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(edSheeran))
            )
            .andExpect(status().isBadRequest());

        // Validate the EdSheeran in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEdSheeran() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        edSheeran.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEdSheeranMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(edSheeran)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EdSheeran in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEdSheeranWithPatch() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the edSheeran using partial update
        EdSheeran partialUpdatedEdSheeran = new EdSheeran();
        partialUpdatedEdSheeran.setId(edSheeran.getId());

        partialUpdatedEdSheeran.givenName(UPDATED_GIVEN_NAME).display(UPDATED_DISPLAY).dob(UPDATED_DOB);

        restEdSheeranMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEdSheeran.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEdSheeran))
            )
            .andExpect(status().isOk());

        // Validate the EdSheeran in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEdSheeranUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEdSheeran, edSheeran),
            getPersistedEdSheeran(edSheeran)
        );
    }

    @Test
    @Transactional
    void fullUpdateEdSheeranWithPatch() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the edSheeran using partial update
        EdSheeran partialUpdatedEdSheeran = new EdSheeran();
        partialUpdatedEdSheeran.setId(edSheeran.getId());

        partialUpdatedEdSheeran
            .familyName(UPDATED_FAMILY_NAME)
            .givenName(UPDATED_GIVEN_NAME)
            .display(UPDATED_DISPLAY)
            .dob(UPDATED_DOB)
            .gender(UPDATED_GENDER)
            .phone(UPDATED_PHONE)
            .contactsJason(UPDATED_CONTACTS_JASON)
            .isEnabled(UPDATED_IS_ENABLED);

        restEdSheeranMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEdSheeran.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEdSheeran))
            )
            .andExpect(status().isOk());

        // Validate the EdSheeran in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEdSheeranUpdatableFieldsEquals(partialUpdatedEdSheeran, getPersistedEdSheeran(partialUpdatedEdSheeran));
    }

    @Test
    @Transactional
    void patchNonExistingEdSheeran() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        edSheeran.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEdSheeranMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, edSheeran.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(edSheeran))
            )
            .andExpect(status().isBadRequest());

        // Validate the EdSheeran in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEdSheeran() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        edSheeran.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEdSheeranMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(edSheeran))
            )
            .andExpect(status().isBadRequest());

        // Validate the EdSheeran in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEdSheeran() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        edSheeran.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEdSheeranMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(edSheeran)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EdSheeran in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEdSheeran() throws Exception {
        // Initialize the database
        insertedEdSheeran = edSheeranRepository.saveAndFlush(edSheeran);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the edSheeran
        restEdSheeranMockMvc
            .perform(delete(ENTITY_API_URL_ID, edSheeran.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return edSheeranRepository.count();
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

    protected EdSheeran getPersistedEdSheeran(EdSheeran edSheeran) {
        return edSheeranRepository.findById(edSheeran.getId()).orElseThrow();
    }

    protected void assertPersistedEdSheeranToMatchAllProperties(EdSheeran expectedEdSheeran) {
        assertEdSheeranAllPropertiesEquals(expectedEdSheeran, getPersistedEdSheeran(expectedEdSheeran));
    }

    protected void assertPersistedEdSheeranToMatchUpdatableProperties(EdSheeran expectedEdSheeran) {
        assertEdSheeranAllUpdatablePropertiesEquals(expectedEdSheeran, getPersistedEdSheeran(expectedEdSheeran));
    }
}
