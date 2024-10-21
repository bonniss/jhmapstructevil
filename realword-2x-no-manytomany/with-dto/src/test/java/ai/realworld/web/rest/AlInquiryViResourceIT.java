package ai.realworld.web.rest;

import static ai.realworld.domain.AlInquiryViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlAppleVi;
import ai.realworld.domain.AlInquiryVi;
import ai.realworld.domain.AlPacino;
import ai.realworld.domain.EdSheeranVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.repository.AlInquiryViRepository;
import ai.realworld.service.dto.AlInquiryViDTO;
import ai.realworld.service.mapper.AlInquiryViMapper;
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
 * Integration tests for the {@link AlInquiryViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlInquiryViResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_BODY = "AAAAAAAAAA";
    private static final String UPDATED_BODY = "BBBBBBBBBB";

    private static final String DEFAULT_SENDER = "AAAAAAAAAA";
    private static final String UPDATED_SENDER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "Mxafa^@..8V";
    private static final String UPDATED_EMAIL = "{8uW@'cgo.Lm";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_JASON = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_JASON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/al-inquiry-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlInquiryViRepository alInquiryViRepository;

    @Autowired
    private AlInquiryViMapper alInquiryViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlInquiryViMockMvc;

    private AlInquiryVi alInquiryVi;

    private AlInquiryVi insertedAlInquiryVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlInquiryVi createEntity() {
        return new AlInquiryVi()
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
    public static AlInquiryVi createUpdatedEntity() {
        return new AlInquiryVi()
            .title(UPDATED_TITLE)
            .body(UPDATED_BODY)
            .sender(UPDATED_SENDER)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .contentJason(UPDATED_CONTENT_JASON);
    }

    @BeforeEach
    public void initTest() {
        alInquiryVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlInquiryVi != null) {
            alInquiryViRepository.delete(insertedAlInquiryVi);
            insertedAlInquiryVi = null;
        }
    }

    @Test
    @Transactional
    void createAlInquiryVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlInquiryVi
        AlInquiryViDTO alInquiryViDTO = alInquiryViMapper.toDto(alInquiryVi);
        var returnedAlInquiryViDTO = om.readValue(
            restAlInquiryViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alInquiryViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlInquiryViDTO.class
        );

        // Validate the AlInquiryVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlInquiryVi = alInquiryViMapper.toEntity(returnedAlInquiryViDTO);
        assertAlInquiryViUpdatableFieldsEquals(returnedAlInquiryVi, getPersistedAlInquiryVi(returnedAlInquiryVi));

        insertedAlInquiryVi = returnedAlInquiryVi;
    }

    @Test
    @Transactional
    void createAlInquiryViWithExistingId() throws Exception {
        // Create the AlInquiryVi with an existing ID
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);
        AlInquiryViDTO alInquiryViDTO = alInquiryViMapper.toDto(alInquiryVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlInquiryViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alInquiryViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlInquiryVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alInquiryVi.setTitle(null);

        // Create the AlInquiryVi, which fails.
        AlInquiryViDTO alInquiryViDTO = alInquiryViMapper.toDto(alInquiryVi);

        restAlInquiryViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alInquiryViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSenderIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alInquiryVi.setSender(null);

        // Create the AlInquiryVi, which fails.
        AlInquiryViDTO alInquiryViDTO = alInquiryViMapper.toDto(alInquiryVi);

        restAlInquiryViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alInquiryViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlInquiryVis() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        // Get all the alInquiryViList
        restAlInquiryViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alInquiryVi.getId().toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY)))
            .andExpect(jsonPath("$.[*].sender").value(hasItem(DEFAULT_SENDER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].contentJason").value(hasItem(DEFAULT_CONTENT_JASON)));
    }

    @Test
    @Transactional
    void getAlInquiryVi() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        // Get the alInquiryVi
        restAlInquiryViMockMvc
            .perform(get(ENTITY_API_URL_ID, alInquiryVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alInquiryVi.getId().toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.body").value(DEFAULT_BODY))
            .andExpect(jsonPath("$.sender").value(DEFAULT_SENDER))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.contentJason").value(DEFAULT_CONTENT_JASON));
    }

    @Test
    @Transactional
    void getAlInquiryVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        UUID id = alInquiryVi.getId();

        defaultAlInquiryViFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllAlInquiryVisByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        // Get all the alInquiryViList where title equals to
        defaultAlInquiryViFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAlInquiryVisByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        // Get all the alInquiryViList where title in
        defaultAlInquiryViFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAlInquiryVisByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        // Get all the alInquiryViList where title is not null
        defaultAlInquiryViFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllAlInquiryVisByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        // Get all the alInquiryViList where title contains
        defaultAlInquiryViFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAlInquiryVisByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        // Get all the alInquiryViList where title does not contain
        defaultAlInquiryViFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllAlInquiryVisByBodyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        // Get all the alInquiryViList where body equals to
        defaultAlInquiryViFiltering("body.equals=" + DEFAULT_BODY, "body.equals=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    void getAllAlInquiryVisByBodyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        // Get all the alInquiryViList where body in
        defaultAlInquiryViFiltering("body.in=" + DEFAULT_BODY + "," + UPDATED_BODY, "body.in=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    void getAllAlInquiryVisByBodyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        // Get all the alInquiryViList where body is not null
        defaultAlInquiryViFiltering("body.specified=true", "body.specified=false");
    }

    @Test
    @Transactional
    void getAllAlInquiryVisByBodyContainsSomething() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        // Get all the alInquiryViList where body contains
        defaultAlInquiryViFiltering("body.contains=" + DEFAULT_BODY, "body.contains=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    void getAllAlInquiryVisByBodyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        // Get all the alInquiryViList where body does not contain
        defaultAlInquiryViFiltering("body.doesNotContain=" + UPDATED_BODY, "body.doesNotContain=" + DEFAULT_BODY);
    }

    @Test
    @Transactional
    void getAllAlInquiryVisBySenderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        // Get all the alInquiryViList where sender equals to
        defaultAlInquiryViFiltering("sender.equals=" + DEFAULT_SENDER, "sender.equals=" + UPDATED_SENDER);
    }

    @Test
    @Transactional
    void getAllAlInquiryVisBySenderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        // Get all the alInquiryViList where sender in
        defaultAlInquiryViFiltering("sender.in=" + DEFAULT_SENDER + "," + UPDATED_SENDER, "sender.in=" + UPDATED_SENDER);
    }

    @Test
    @Transactional
    void getAllAlInquiryVisBySenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        // Get all the alInquiryViList where sender is not null
        defaultAlInquiryViFiltering("sender.specified=true", "sender.specified=false");
    }

    @Test
    @Transactional
    void getAllAlInquiryVisBySenderContainsSomething() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        // Get all the alInquiryViList where sender contains
        defaultAlInquiryViFiltering("sender.contains=" + DEFAULT_SENDER, "sender.contains=" + UPDATED_SENDER);
    }

    @Test
    @Transactional
    void getAllAlInquiryVisBySenderNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        // Get all the alInquiryViList where sender does not contain
        defaultAlInquiryViFiltering("sender.doesNotContain=" + UPDATED_SENDER, "sender.doesNotContain=" + DEFAULT_SENDER);
    }

    @Test
    @Transactional
    void getAllAlInquiryVisByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        // Get all the alInquiryViList where email equals to
        defaultAlInquiryViFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAlInquiryVisByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        // Get all the alInquiryViList where email in
        defaultAlInquiryViFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAlInquiryVisByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        // Get all the alInquiryViList where email is not null
        defaultAlInquiryViFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllAlInquiryVisByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        // Get all the alInquiryViList where email contains
        defaultAlInquiryViFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAlInquiryVisByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        // Get all the alInquiryViList where email does not contain
        defaultAlInquiryViFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllAlInquiryVisByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        // Get all the alInquiryViList where phone equals to
        defaultAlInquiryViFiltering("phone.equals=" + DEFAULT_PHONE, "phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllAlInquiryVisByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        // Get all the alInquiryViList where phone in
        defaultAlInquiryViFiltering("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE, "phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllAlInquiryVisByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        // Get all the alInquiryViList where phone is not null
        defaultAlInquiryViFiltering("phone.specified=true", "phone.specified=false");
    }

    @Test
    @Transactional
    void getAllAlInquiryVisByPhoneContainsSomething() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        // Get all the alInquiryViList where phone contains
        defaultAlInquiryViFiltering("phone.contains=" + DEFAULT_PHONE, "phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllAlInquiryVisByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        // Get all the alInquiryViList where phone does not contain
        defaultAlInquiryViFiltering("phone.doesNotContain=" + UPDATED_PHONE, "phone.doesNotContain=" + DEFAULT_PHONE);
    }

    @Test
    @Transactional
    void getAllAlInquiryVisByContentJasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        // Get all the alInquiryViList where contentJason equals to
        defaultAlInquiryViFiltering("contentJason.equals=" + DEFAULT_CONTENT_JASON, "contentJason.equals=" + UPDATED_CONTENT_JASON);
    }

    @Test
    @Transactional
    void getAllAlInquiryVisByContentJasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        // Get all the alInquiryViList where contentJason in
        defaultAlInquiryViFiltering(
            "contentJason.in=" + DEFAULT_CONTENT_JASON + "," + UPDATED_CONTENT_JASON,
            "contentJason.in=" + UPDATED_CONTENT_JASON
        );
    }

    @Test
    @Transactional
    void getAllAlInquiryVisByContentJasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        // Get all the alInquiryViList where contentJason is not null
        defaultAlInquiryViFiltering("contentJason.specified=true", "contentJason.specified=false");
    }

    @Test
    @Transactional
    void getAllAlInquiryVisByContentJasonContainsSomething() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        // Get all the alInquiryViList where contentJason contains
        defaultAlInquiryViFiltering("contentJason.contains=" + DEFAULT_CONTENT_JASON, "contentJason.contains=" + UPDATED_CONTENT_JASON);
    }

    @Test
    @Transactional
    void getAllAlInquiryVisByContentJasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        // Get all the alInquiryViList where contentJason does not contain
        defaultAlInquiryViFiltering(
            "contentJason.doesNotContain=" + UPDATED_CONTENT_JASON,
            "contentJason.doesNotContain=" + DEFAULT_CONTENT_JASON
        );
    }

    @Test
    @Transactional
    void getAllAlInquiryVisByCustomerIsEqualToSomething() throws Exception {
        AlPacino customer;
        if (TestUtil.findAll(em, AlPacino.class).isEmpty()) {
            alInquiryViRepository.saveAndFlush(alInquiryVi);
            customer = AlPacinoResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, AlPacino.class).get(0);
        }
        em.persist(customer);
        em.flush();
        alInquiryVi.setCustomer(customer);
        alInquiryViRepository.saveAndFlush(alInquiryVi);
        UUID customerId = customer.getId();
        // Get all the alInquiryViList where customer equals to customerId
        defaultAlInquiryViShouldBeFound("customerId.equals=" + customerId);

        // Get all the alInquiryViList where customer equals to UUID.randomUUID()
        defaultAlInquiryViShouldNotBeFound("customerId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlInquiryVisByAgencyIsEqualToSomething() throws Exception {
        AlAppleVi agency;
        if (TestUtil.findAll(em, AlAppleVi.class).isEmpty()) {
            alInquiryViRepository.saveAndFlush(alInquiryVi);
            agency = AlAppleViResourceIT.createEntity(em);
        } else {
            agency = TestUtil.findAll(em, AlAppleVi.class).get(0);
        }
        em.persist(agency);
        em.flush();
        alInquiryVi.setAgency(agency);
        alInquiryViRepository.saveAndFlush(alInquiryVi);
        UUID agencyId = agency.getId();
        // Get all the alInquiryViList where agency equals to agencyId
        defaultAlInquiryViShouldBeFound("agencyId.equals=" + agencyId);

        // Get all the alInquiryViList where agency equals to UUID.randomUUID()
        defaultAlInquiryViShouldNotBeFound("agencyId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlInquiryVisByPersonInChargeIsEqualToSomething() throws Exception {
        EdSheeranVi personInCharge;
        if (TestUtil.findAll(em, EdSheeranVi.class).isEmpty()) {
            alInquiryViRepository.saveAndFlush(alInquiryVi);
            personInCharge = EdSheeranViResourceIT.createEntity(em);
        } else {
            personInCharge = TestUtil.findAll(em, EdSheeranVi.class).get(0);
        }
        em.persist(personInCharge);
        em.flush();
        alInquiryVi.setPersonInCharge(personInCharge);
        alInquiryViRepository.saveAndFlush(alInquiryVi);
        Long personInChargeId = personInCharge.getId();
        // Get all the alInquiryViList where personInCharge equals to personInChargeId
        defaultAlInquiryViShouldBeFound("personInChargeId.equals=" + personInChargeId);

        // Get all the alInquiryViList where personInCharge equals to (personInChargeId + 1)
        defaultAlInquiryViShouldNotBeFound("personInChargeId.equals=" + (personInChargeId + 1));
    }

    @Test
    @Transactional
    void getAllAlInquiryVisByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alInquiryViRepository.saveAndFlush(alInquiryVi);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alInquiryVi.setApplication(application);
        alInquiryViRepository.saveAndFlush(alInquiryVi);
        UUID applicationId = application.getId();
        // Get all the alInquiryViList where application equals to applicationId
        defaultAlInquiryViShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alInquiryViList where application equals to UUID.randomUUID()
        defaultAlInquiryViShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlInquiryViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlInquiryViShouldBeFound(shouldBeFound);
        defaultAlInquiryViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlInquiryViShouldBeFound(String filter) throws Exception {
        restAlInquiryViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alInquiryVi.getId().toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY)))
            .andExpect(jsonPath("$.[*].sender").value(hasItem(DEFAULT_SENDER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].contentJason").value(hasItem(DEFAULT_CONTENT_JASON)));

        // Check, that the count call also returns 1
        restAlInquiryViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlInquiryViShouldNotBeFound(String filter) throws Exception {
        restAlInquiryViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlInquiryViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlInquiryVi() throws Exception {
        // Get the alInquiryVi
        restAlInquiryViMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlInquiryVi() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alInquiryVi
        AlInquiryVi updatedAlInquiryVi = alInquiryViRepository.findById(alInquiryVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlInquiryVi are not directly saved in db
        em.detach(updatedAlInquiryVi);
        updatedAlInquiryVi
            .title(UPDATED_TITLE)
            .body(UPDATED_BODY)
            .sender(UPDATED_SENDER)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .contentJason(UPDATED_CONTENT_JASON);
        AlInquiryViDTO alInquiryViDTO = alInquiryViMapper.toDto(updatedAlInquiryVi);

        restAlInquiryViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alInquiryViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alInquiryViDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlInquiryVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlInquiryViToMatchAllProperties(updatedAlInquiryVi);
    }

    @Test
    @Transactional
    void putNonExistingAlInquiryVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alInquiryVi.setId(UUID.randomUUID());

        // Create the AlInquiryVi
        AlInquiryViDTO alInquiryViDTO = alInquiryViMapper.toDto(alInquiryVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlInquiryViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alInquiryViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alInquiryViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlInquiryVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlInquiryVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alInquiryVi.setId(UUID.randomUUID());

        // Create the AlInquiryVi
        AlInquiryViDTO alInquiryViDTO = alInquiryViMapper.toDto(alInquiryVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlInquiryViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alInquiryViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlInquiryVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlInquiryVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alInquiryVi.setId(UUID.randomUUID());

        // Create the AlInquiryVi
        AlInquiryViDTO alInquiryViDTO = alInquiryViMapper.toDto(alInquiryVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlInquiryViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alInquiryViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlInquiryVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlInquiryViWithPatch() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alInquiryVi using partial update
        AlInquiryVi partialUpdatedAlInquiryVi = new AlInquiryVi();
        partialUpdatedAlInquiryVi.setId(alInquiryVi.getId());

        partialUpdatedAlInquiryVi.title(UPDATED_TITLE).sender(UPDATED_SENDER).email(UPDATED_EMAIL);

        restAlInquiryViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlInquiryVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlInquiryVi))
            )
            .andExpect(status().isOk());

        // Validate the AlInquiryVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlInquiryViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlInquiryVi, alInquiryVi),
            getPersistedAlInquiryVi(alInquiryVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlInquiryViWithPatch() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alInquiryVi using partial update
        AlInquiryVi partialUpdatedAlInquiryVi = new AlInquiryVi();
        partialUpdatedAlInquiryVi.setId(alInquiryVi.getId());

        partialUpdatedAlInquiryVi
            .title(UPDATED_TITLE)
            .body(UPDATED_BODY)
            .sender(UPDATED_SENDER)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .contentJason(UPDATED_CONTENT_JASON);

        restAlInquiryViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlInquiryVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlInquiryVi))
            )
            .andExpect(status().isOk());

        // Validate the AlInquiryVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlInquiryViUpdatableFieldsEquals(partialUpdatedAlInquiryVi, getPersistedAlInquiryVi(partialUpdatedAlInquiryVi));
    }

    @Test
    @Transactional
    void patchNonExistingAlInquiryVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alInquiryVi.setId(UUID.randomUUID());

        // Create the AlInquiryVi
        AlInquiryViDTO alInquiryViDTO = alInquiryViMapper.toDto(alInquiryVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlInquiryViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alInquiryViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alInquiryViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlInquiryVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlInquiryVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alInquiryVi.setId(UUID.randomUUID());

        // Create the AlInquiryVi
        AlInquiryViDTO alInquiryViDTO = alInquiryViMapper.toDto(alInquiryVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlInquiryViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alInquiryViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlInquiryVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlInquiryVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alInquiryVi.setId(UUID.randomUUID());

        // Create the AlInquiryVi
        AlInquiryViDTO alInquiryViDTO = alInquiryViMapper.toDto(alInquiryVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlInquiryViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alInquiryViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlInquiryVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlInquiryVi() throws Exception {
        // Initialize the database
        insertedAlInquiryVi = alInquiryViRepository.saveAndFlush(alInquiryVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alInquiryVi
        restAlInquiryViMockMvc
            .perform(delete(ENTITY_API_URL_ID, alInquiryVi.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alInquiryViRepository.count();
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

    protected AlInquiryVi getPersistedAlInquiryVi(AlInquiryVi alInquiryVi) {
        return alInquiryViRepository.findById(alInquiryVi.getId()).orElseThrow();
    }

    protected void assertPersistedAlInquiryViToMatchAllProperties(AlInquiryVi expectedAlInquiryVi) {
        assertAlInquiryViAllPropertiesEquals(expectedAlInquiryVi, getPersistedAlInquiryVi(expectedAlInquiryVi));
    }

    protected void assertPersistedAlInquiryViToMatchUpdatableProperties(AlInquiryVi expectedAlInquiryVi) {
        assertAlInquiryViAllUpdatablePropertiesEquals(expectedAlInquiryVi, getPersistedAlInquiryVi(expectedAlInquiryVi));
    }
}
