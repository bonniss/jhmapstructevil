package ai.realworld.web.rest;

import static ai.realworld.domain.AlInquiryAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlApple;
import ai.realworld.domain.AlInquiry;
import ai.realworld.domain.AlPacino;
import ai.realworld.domain.EdSheeran;
import ai.realworld.domain.JohnLennon;
import ai.realworld.repository.AlInquiryRepository;
import ai.realworld.service.dto.AlInquiryDTO;
import ai.realworld.service.mapper.AlInquiryMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link AlInquiryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlInquiryResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_BODY = "AAAAAAAAAA";
    private static final String UPDATED_BODY = "BBBBBBBBBB";

    private static final String DEFAULT_SENDER = "AAAAAAAAAA";
    private static final String UPDATED_SENDER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "o@o4$waw.AN#dE";
    private static final String UPDATED_EMAIL = "q4F@D.D";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_JASON = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_JASON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/al-inquiries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlInquiryRepository alInquiryRepository;

    @Autowired
    private AlInquiryMapper alInquiryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlInquiryMockMvc;

    private AlInquiry alInquiry;

    private AlInquiry insertedAlInquiry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlInquiry createEntity() {
        return new AlInquiry()
            .title(DEFAULT_TITLE)
            .body(DEFAULT_BODY)
            .sender(DEFAULT_SENDER)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .contentJason(DEFAULT_CONTENT_JASON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlInquiry createUpdatedEntity() {
        return new AlInquiry()
            .title(UPDATED_TITLE)
            .body(UPDATED_BODY)
            .sender(UPDATED_SENDER)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .contentJason(UPDATED_CONTENT_JASON);
    }

    @BeforeEach
    public void initTest() {
        alInquiry = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlInquiry != null) {
            alInquiryRepository.delete(insertedAlInquiry);
            insertedAlInquiry = null;
        }
    }

    @Test
    @Transactional
    void createAlInquiry() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlInquiry
        AlInquiryDTO alInquiryDTO = alInquiryMapper.toDto(alInquiry);
        var returnedAlInquiryDTO = om.readValue(
            restAlInquiryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alInquiryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlInquiryDTO.class
        );

        // Validate the AlInquiry in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlInquiry = alInquiryMapper.toEntity(returnedAlInquiryDTO);
        assertAlInquiryUpdatableFieldsEquals(returnedAlInquiry, getPersistedAlInquiry(returnedAlInquiry));

        insertedAlInquiry = returnedAlInquiry;
    }

    @Test
    @Transactional
    void createAlInquiryWithExistingId() throws Exception {
        // Create the AlInquiry with an existing ID
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);
        AlInquiryDTO alInquiryDTO = alInquiryMapper.toDto(alInquiry);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlInquiryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alInquiryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlInquiry in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alInquiry.setTitle(null);

        // Create the AlInquiry, which fails.
        AlInquiryDTO alInquiryDTO = alInquiryMapper.toDto(alInquiry);

        restAlInquiryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alInquiryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSenderIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alInquiry.setSender(null);

        // Create the AlInquiry, which fails.
        AlInquiryDTO alInquiryDTO = alInquiryMapper.toDto(alInquiry);

        restAlInquiryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alInquiryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlInquiries() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        // Get all the alInquiryList
        restAlInquiryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alInquiry.getId().toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY)))
            .andExpect(jsonPath("$.[*].sender").value(hasItem(DEFAULT_SENDER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].contentJason").value(hasItem(DEFAULT_CONTENT_JASON)));
    }

    @Test
    @Transactional
    void getAlInquiry() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        // Get the alInquiry
        restAlInquiryMockMvc
            .perform(get(ENTITY_API_URL_ID, alInquiry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alInquiry.getId().toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.body").value(DEFAULT_BODY))
            .andExpect(jsonPath("$.sender").value(DEFAULT_SENDER))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.contentJason").value(DEFAULT_CONTENT_JASON));
    }

    @Test
    @Transactional
    void getAlInquiriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        UUID id = alInquiry.getId();

        defaultAlInquiryFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllAlInquiriesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        // Get all the alInquiryList where title equals to
        defaultAlInquiryFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAlInquiriesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        // Get all the alInquiryList where title in
        defaultAlInquiryFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAlInquiriesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        // Get all the alInquiryList where title is not null
        defaultAlInquiryFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllAlInquiriesByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        // Get all the alInquiryList where title contains
        defaultAlInquiryFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAlInquiriesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        // Get all the alInquiryList where title does not contain
        defaultAlInquiryFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllAlInquiriesByBodyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        // Get all the alInquiryList where body equals to
        defaultAlInquiryFiltering("body.equals=" + DEFAULT_BODY, "body.equals=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    void getAllAlInquiriesByBodyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        // Get all the alInquiryList where body in
        defaultAlInquiryFiltering("body.in=" + DEFAULT_BODY + "," + UPDATED_BODY, "body.in=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    void getAllAlInquiriesByBodyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        // Get all the alInquiryList where body is not null
        defaultAlInquiryFiltering("body.specified=true", "body.specified=false");
    }

    @Test
    @Transactional
    void getAllAlInquiriesByBodyContainsSomething() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        // Get all the alInquiryList where body contains
        defaultAlInquiryFiltering("body.contains=" + DEFAULT_BODY, "body.contains=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    void getAllAlInquiriesByBodyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        // Get all the alInquiryList where body does not contain
        defaultAlInquiryFiltering("body.doesNotContain=" + UPDATED_BODY, "body.doesNotContain=" + DEFAULT_BODY);
    }

    @Test
    @Transactional
    void getAllAlInquiriesBySenderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        // Get all the alInquiryList where sender equals to
        defaultAlInquiryFiltering("sender.equals=" + DEFAULT_SENDER, "sender.equals=" + UPDATED_SENDER);
    }

    @Test
    @Transactional
    void getAllAlInquiriesBySenderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        // Get all the alInquiryList where sender in
        defaultAlInquiryFiltering("sender.in=" + DEFAULT_SENDER + "," + UPDATED_SENDER, "sender.in=" + UPDATED_SENDER);
    }

    @Test
    @Transactional
    void getAllAlInquiriesBySenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        // Get all the alInquiryList where sender is not null
        defaultAlInquiryFiltering("sender.specified=true", "sender.specified=false");
    }

    @Test
    @Transactional
    void getAllAlInquiriesBySenderContainsSomething() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        // Get all the alInquiryList where sender contains
        defaultAlInquiryFiltering("sender.contains=" + DEFAULT_SENDER, "sender.contains=" + UPDATED_SENDER);
    }

    @Test
    @Transactional
    void getAllAlInquiriesBySenderNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        // Get all the alInquiryList where sender does not contain
        defaultAlInquiryFiltering("sender.doesNotContain=" + UPDATED_SENDER, "sender.doesNotContain=" + DEFAULT_SENDER);
    }

    @Test
    @Transactional
    void getAllAlInquiriesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        // Get all the alInquiryList where email equals to
        defaultAlInquiryFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAlInquiriesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        // Get all the alInquiryList where email in
        defaultAlInquiryFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAlInquiriesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        // Get all the alInquiryList where email is not null
        defaultAlInquiryFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllAlInquiriesByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        // Get all the alInquiryList where email contains
        defaultAlInquiryFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAlInquiriesByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        // Get all the alInquiryList where email does not contain
        defaultAlInquiryFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllAlInquiriesByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        // Get all the alInquiryList where phone equals to
        defaultAlInquiryFiltering("phone.equals=" + DEFAULT_PHONE, "phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllAlInquiriesByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        // Get all the alInquiryList where phone in
        defaultAlInquiryFiltering("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE, "phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllAlInquiriesByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        // Get all the alInquiryList where phone is not null
        defaultAlInquiryFiltering("phone.specified=true", "phone.specified=false");
    }

    @Test
    @Transactional
    void getAllAlInquiriesByPhoneContainsSomething() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        // Get all the alInquiryList where phone contains
        defaultAlInquiryFiltering("phone.contains=" + DEFAULT_PHONE, "phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllAlInquiriesByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        // Get all the alInquiryList where phone does not contain
        defaultAlInquiryFiltering("phone.doesNotContain=" + UPDATED_PHONE, "phone.doesNotContain=" + DEFAULT_PHONE);
    }

    @Test
    @Transactional
    void getAllAlInquiriesByContentJasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        // Get all the alInquiryList where contentJason equals to
        defaultAlInquiryFiltering("contentJason.equals=" + DEFAULT_CONTENT_JASON, "contentJason.equals=" + UPDATED_CONTENT_JASON);
    }

    @Test
    @Transactional
    void getAllAlInquiriesByContentJasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        // Get all the alInquiryList where contentJason in
        defaultAlInquiryFiltering(
            "contentJason.in=" + DEFAULT_CONTENT_JASON + "," + UPDATED_CONTENT_JASON,
            "contentJason.in=" + UPDATED_CONTENT_JASON
        );
    }

    @Test
    @Transactional
    void getAllAlInquiriesByContentJasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        // Get all the alInquiryList where contentJason is not null
        defaultAlInquiryFiltering("contentJason.specified=true", "contentJason.specified=false");
    }

    @Test
    @Transactional
    void getAllAlInquiriesByContentJasonContainsSomething() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        // Get all the alInquiryList where contentJason contains
        defaultAlInquiryFiltering("contentJason.contains=" + DEFAULT_CONTENT_JASON, "contentJason.contains=" + UPDATED_CONTENT_JASON);
    }

    @Test
    @Transactional
    void getAllAlInquiriesByContentJasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        // Get all the alInquiryList where contentJason does not contain
        defaultAlInquiryFiltering(
            "contentJason.doesNotContain=" + UPDATED_CONTENT_JASON,
            "contentJason.doesNotContain=" + DEFAULT_CONTENT_JASON
        );
    }

    @Test
    @Transactional
    void getAllAlInquiriesByCustomerIsEqualToSomething() throws Exception {
        AlPacino customer;
        if (TestUtil.findAll(em, AlPacino.class).isEmpty()) {
            alInquiryRepository.saveAndFlush(alInquiry);
            customer = AlPacinoResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, AlPacino.class).get(0);
        }
        em.persist(customer);
        em.flush();
        alInquiry.setCustomer(customer);
        alInquiryRepository.saveAndFlush(alInquiry);
        UUID customerId = customer.getId();
        // Get all the alInquiryList where customer equals to customerId
        defaultAlInquiryShouldBeFound("customerId.equals=" + customerId);

        // Get all the alInquiryList where customer equals to UUID.randomUUID()
        defaultAlInquiryShouldNotBeFound("customerId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlInquiriesByAgencyIsEqualToSomething() throws Exception {
        AlApple agency;
        if (TestUtil.findAll(em, AlApple.class).isEmpty()) {
            alInquiryRepository.saveAndFlush(alInquiry);
            agency = AlAppleResourceIT.createEntity(em);
        } else {
            agency = TestUtil.findAll(em, AlApple.class).get(0);
        }
        em.persist(agency);
        em.flush();
        alInquiry.setAgency(agency);
        alInquiryRepository.saveAndFlush(alInquiry);
        UUID agencyId = agency.getId();
        // Get all the alInquiryList where agency equals to agencyId
        defaultAlInquiryShouldBeFound("agencyId.equals=" + agencyId);

        // Get all the alInquiryList where agency equals to UUID.randomUUID()
        defaultAlInquiryShouldNotBeFound("agencyId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlInquiriesByPersonInChargeIsEqualToSomething() throws Exception {
        EdSheeran personInCharge;
        if (TestUtil.findAll(em, EdSheeran.class).isEmpty()) {
            alInquiryRepository.saveAndFlush(alInquiry);
            personInCharge = EdSheeranResourceIT.createEntity(em);
        } else {
            personInCharge = TestUtil.findAll(em, EdSheeran.class).get(0);
        }
        em.persist(personInCharge);
        em.flush();
        alInquiry.setPersonInCharge(personInCharge);
        alInquiryRepository.saveAndFlush(alInquiry);
        Long personInChargeId = personInCharge.getId();
        // Get all the alInquiryList where personInCharge equals to personInChargeId
        defaultAlInquiryShouldBeFound("personInChargeId.equals=" + personInChargeId);

        // Get all the alInquiryList where personInCharge equals to (personInChargeId + 1)
        defaultAlInquiryShouldNotBeFound("personInChargeId.equals=" + (personInChargeId + 1));
    }

    @Test
    @Transactional
    void getAllAlInquiriesByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alInquiryRepository.saveAndFlush(alInquiry);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alInquiry.setApplication(application);
        alInquiryRepository.saveAndFlush(alInquiry);
        UUID applicationId = application.getId();
        // Get all the alInquiryList where application equals to applicationId
        defaultAlInquiryShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alInquiryList where application equals to UUID.randomUUID()
        defaultAlInquiryShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlInquiryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlInquiryShouldBeFound(shouldBeFound);
        defaultAlInquiryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlInquiryShouldBeFound(String filter) throws Exception {
        restAlInquiryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alInquiry.getId().toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY)))
            .andExpect(jsonPath("$.[*].sender").value(hasItem(DEFAULT_SENDER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].contentJason").value(hasItem(DEFAULT_CONTENT_JASON)));

        // Check, that the count call also returns 1
        restAlInquiryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlInquiryShouldNotBeFound(String filter) throws Exception {
        restAlInquiryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlInquiryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlInquiry() throws Exception {
        // Get the alInquiry
        restAlInquiryMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlInquiry() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alInquiry
        AlInquiry updatedAlInquiry = alInquiryRepository.findById(alInquiry.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlInquiry are not directly saved in db
        em.detach(updatedAlInquiry);
        updatedAlInquiry
            .title(UPDATED_TITLE)
            .body(UPDATED_BODY)
            .sender(UPDATED_SENDER)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .contentJason(UPDATED_CONTENT_JASON);
        AlInquiryDTO alInquiryDTO = alInquiryMapper.toDto(updatedAlInquiry);

        restAlInquiryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alInquiryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alInquiryDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlInquiry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlInquiryToMatchAllProperties(updatedAlInquiry);
    }

    @Test
    @Transactional
    void putNonExistingAlInquiry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alInquiry.setId(UUID.randomUUID());

        // Create the AlInquiry
        AlInquiryDTO alInquiryDTO = alInquiryMapper.toDto(alInquiry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlInquiryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alInquiryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alInquiryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlInquiry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlInquiry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alInquiry.setId(UUID.randomUUID());

        // Create the AlInquiry
        AlInquiryDTO alInquiryDTO = alInquiryMapper.toDto(alInquiry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlInquiryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alInquiryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlInquiry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlInquiry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alInquiry.setId(UUID.randomUUID());

        // Create the AlInquiry
        AlInquiryDTO alInquiryDTO = alInquiryMapper.toDto(alInquiry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlInquiryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alInquiryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlInquiry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlInquiryWithPatch() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alInquiry using partial update
        AlInquiry partialUpdatedAlInquiry = new AlInquiry();
        partialUpdatedAlInquiry.setId(alInquiry.getId());

        partialUpdatedAlInquiry.title(UPDATED_TITLE).sender(UPDATED_SENDER).phone(UPDATED_PHONE);

        restAlInquiryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlInquiry.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlInquiry))
            )
            .andExpect(status().isOk());

        // Validate the AlInquiry in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlInquiryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlInquiry, alInquiry),
            getPersistedAlInquiry(alInquiry)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlInquiryWithPatch() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alInquiry using partial update
        AlInquiry partialUpdatedAlInquiry = new AlInquiry();
        partialUpdatedAlInquiry.setId(alInquiry.getId());

        partialUpdatedAlInquiry
            .title(UPDATED_TITLE)
            .body(UPDATED_BODY)
            .sender(UPDATED_SENDER)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .contentJason(UPDATED_CONTENT_JASON);

        restAlInquiryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlInquiry.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlInquiry))
            )
            .andExpect(status().isOk());

        // Validate the AlInquiry in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlInquiryUpdatableFieldsEquals(partialUpdatedAlInquiry, getPersistedAlInquiry(partialUpdatedAlInquiry));
    }

    @Test
    @Transactional
    void patchNonExistingAlInquiry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alInquiry.setId(UUID.randomUUID());

        // Create the AlInquiry
        AlInquiryDTO alInquiryDTO = alInquiryMapper.toDto(alInquiry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlInquiryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alInquiryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alInquiryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlInquiry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlInquiry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alInquiry.setId(UUID.randomUUID());

        // Create the AlInquiry
        AlInquiryDTO alInquiryDTO = alInquiryMapper.toDto(alInquiry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlInquiryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alInquiryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlInquiry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlInquiry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alInquiry.setId(UUID.randomUUID());

        // Create the AlInquiry
        AlInquiryDTO alInquiryDTO = alInquiryMapper.toDto(alInquiry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlInquiryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alInquiryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlInquiry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlInquiry() throws Exception {
        // Initialize the database
        insertedAlInquiry = alInquiryRepository.saveAndFlush(alInquiry);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alInquiry
        restAlInquiryMockMvc
            .perform(delete(ENTITY_API_URL_ID, alInquiry.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alInquiryRepository.count();
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

    protected AlInquiry getPersistedAlInquiry(AlInquiry alInquiry) {
        return alInquiryRepository.findById(alInquiry.getId()).orElseThrow();
    }

    protected void assertPersistedAlInquiryToMatchAllProperties(AlInquiry expectedAlInquiry) {
        assertAlInquiryAllPropertiesEquals(expectedAlInquiry, getPersistedAlInquiry(expectedAlInquiry));
    }

    protected void assertPersistedAlInquiryToMatchUpdatableProperties(AlInquiry expectedAlInquiry) {
        assertAlInquiryAllUpdatablePropertiesEquals(expectedAlInquiry, getPersistedAlInquiry(expectedAlInquiry));
    }
}
