package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextSupplierMiMiAsserts.*;
import static xyz.jhmapstruct.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
import xyz.jhmapstruct.IntegrationTest;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextProductMiMi;
import xyz.jhmapstruct.domain.NextSupplierMiMi;
import xyz.jhmapstruct.repository.NextSupplierMiMiRepository;
import xyz.jhmapstruct.service.NextSupplierMiMiService;
import xyz.jhmapstruct.service.dto.NextSupplierMiMiDTO;
import xyz.jhmapstruct.service.mapper.NextSupplierMiMiMapper;

/**
 * Integration tests for the {@link NextSupplierMiMiResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextSupplierMiMiResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_PERSON = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_PERSON = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/next-supplier-mi-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextSupplierMiMiRepository nextSupplierMiMiRepository;

    @Mock
    private NextSupplierMiMiRepository nextSupplierMiMiRepositoryMock;

    @Autowired
    private NextSupplierMiMiMapper nextSupplierMiMiMapper;

    @Mock
    private NextSupplierMiMiService nextSupplierMiMiServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextSupplierMiMiMockMvc;

    private NextSupplierMiMi nextSupplierMiMi;

    private NextSupplierMiMi insertedNextSupplierMiMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextSupplierMiMi createEntity() {
        return new NextSupplierMiMi()
            .name(DEFAULT_NAME)
            .contactPerson(DEFAULT_CONTACT_PERSON)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextSupplierMiMi createUpdatedEntity() {
        return new NextSupplierMiMi()
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        nextSupplierMiMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextSupplierMiMi != null) {
            nextSupplierMiMiRepository.delete(insertedNextSupplierMiMi);
            insertedNextSupplierMiMi = null;
        }
    }

    @Test
    @Transactional
    void createNextSupplierMiMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextSupplierMiMi
        NextSupplierMiMiDTO nextSupplierMiMiDTO = nextSupplierMiMiMapper.toDto(nextSupplierMiMi);
        var returnedNextSupplierMiMiDTO = om.readValue(
            restNextSupplierMiMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierMiMiDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextSupplierMiMiDTO.class
        );

        // Validate the NextSupplierMiMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextSupplierMiMi = nextSupplierMiMiMapper.toEntity(returnedNextSupplierMiMiDTO);
        assertNextSupplierMiMiUpdatableFieldsEquals(returnedNextSupplierMiMi, getPersistedNextSupplierMiMi(returnedNextSupplierMiMi));

        insertedNextSupplierMiMi = returnedNextSupplierMiMi;
    }

    @Test
    @Transactional
    void createNextSupplierMiMiWithExistingId() throws Exception {
        // Create the NextSupplierMiMi with an existing ID
        nextSupplierMiMi.setId(1L);
        NextSupplierMiMiDTO nextSupplierMiMiDTO = nextSupplierMiMiMapper.toDto(nextSupplierMiMi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextSupplierMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierMiMiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextSupplierMiMi.setName(null);

        // Create the NextSupplierMiMi, which fails.
        NextSupplierMiMiDTO nextSupplierMiMiDTO = nextSupplierMiMiMapper.toDto(nextSupplierMiMi);

        restNextSupplierMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierMiMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextSupplierMiMis() throws Exception {
        // Initialize the database
        insertedNextSupplierMiMi = nextSupplierMiMiRepository.saveAndFlush(nextSupplierMiMi);

        // Get all the nextSupplierMiMiList
        restNextSupplierMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextSupplierMiMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextSupplierMiMisWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextSupplierMiMiServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextSupplierMiMiMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextSupplierMiMiServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextSupplierMiMisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextSupplierMiMiServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextSupplierMiMiMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextSupplierMiMiRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextSupplierMiMi() throws Exception {
        // Initialize the database
        insertedNextSupplierMiMi = nextSupplierMiMiRepository.saveAndFlush(nextSupplierMiMi);

        // Get the nextSupplierMiMi
        restNextSupplierMiMiMockMvc
            .perform(get(ENTITY_API_URL_ID, nextSupplierMiMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextSupplierMiMi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.contactPerson").value(DEFAULT_CONTACT_PERSON))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getNextSupplierMiMisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextSupplierMiMi = nextSupplierMiMiRepository.saveAndFlush(nextSupplierMiMi);

        Long id = nextSupplierMiMi.getId();

        defaultNextSupplierMiMiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextSupplierMiMiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextSupplierMiMiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextSupplierMiMisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierMiMi = nextSupplierMiMiRepository.saveAndFlush(nextSupplierMiMi);

        // Get all the nextSupplierMiMiList where name equals to
        defaultNextSupplierMiMiFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextSupplierMiMisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplierMiMi = nextSupplierMiMiRepository.saveAndFlush(nextSupplierMiMi);

        // Get all the nextSupplierMiMiList where name in
        defaultNextSupplierMiMiFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextSupplierMiMisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplierMiMi = nextSupplierMiMiRepository.saveAndFlush(nextSupplierMiMi);

        // Get all the nextSupplierMiMiList where name is not null
        defaultNextSupplierMiMiFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSupplierMiMisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierMiMi = nextSupplierMiMiRepository.saveAndFlush(nextSupplierMiMi);

        // Get all the nextSupplierMiMiList where name contains
        defaultNextSupplierMiMiFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextSupplierMiMisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierMiMi = nextSupplierMiMiRepository.saveAndFlush(nextSupplierMiMi);

        // Get all the nextSupplierMiMiList where name does not contain
        defaultNextSupplierMiMiFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllNextSupplierMiMisByContactPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierMiMi = nextSupplierMiMiRepository.saveAndFlush(nextSupplierMiMi);

        // Get all the nextSupplierMiMiList where contactPerson equals to
        defaultNextSupplierMiMiFiltering(
            "contactPerson.equals=" + DEFAULT_CONTACT_PERSON,
            "contactPerson.equals=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierMiMisByContactPersonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplierMiMi = nextSupplierMiMiRepository.saveAndFlush(nextSupplierMiMi);

        // Get all the nextSupplierMiMiList where contactPerson in
        defaultNextSupplierMiMiFiltering(
            "contactPerson.in=" + DEFAULT_CONTACT_PERSON + "," + UPDATED_CONTACT_PERSON,
            "contactPerson.in=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierMiMisByContactPersonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplierMiMi = nextSupplierMiMiRepository.saveAndFlush(nextSupplierMiMi);

        // Get all the nextSupplierMiMiList where contactPerson is not null
        defaultNextSupplierMiMiFiltering("contactPerson.specified=true", "contactPerson.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSupplierMiMisByContactPersonContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierMiMi = nextSupplierMiMiRepository.saveAndFlush(nextSupplierMiMi);

        // Get all the nextSupplierMiMiList where contactPerson contains
        defaultNextSupplierMiMiFiltering(
            "contactPerson.contains=" + DEFAULT_CONTACT_PERSON,
            "contactPerson.contains=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierMiMisByContactPersonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierMiMi = nextSupplierMiMiRepository.saveAndFlush(nextSupplierMiMi);

        // Get all the nextSupplierMiMiList where contactPerson does not contain
        defaultNextSupplierMiMiFiltering(
            "contactPerson.doesNotContain=" + UPDATED_CONTACT_PERSON,
            "contactPerson.doesNotContain=" + DEFAULT_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierMiMisByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierMiMi = nextSupplierMiMiRepository.saveAndFlush(nextSupplierMiMi);

        // Get all the nextSupplierMiMiList where email equals to
        defaultNextSupplierMiMiFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSupplierMiMisByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplierMiMi = nextSupplierMiMiRepository.saveAndFlush(nextSupplierMiMi);

        // Get all the nextSupplierMiMiList where email in
        defaultNextSupplierMiMiFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSupplierMiMisByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplierMiMi = nextSupplierMiMiRepository.saveAndFlush(nextSupplierMiMi);

        // Get all the nextSupplierMiMiList where email is not null
        defaultNextSupplierMiMiFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSupplierMiMisByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierMiMi = nextSupplierMiMiRepository.saveAndFlush(nextSupplierMiMi);

        // Get all the nextSupplierMiMiList where email contains
        defaultNextSupplierMiMiFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSupplierMiMisByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierMiMi = nextSupplierMiMiRepository.saveAndFlush(nextSupplierMiMi);

        // Get all the nextSupplierMiMiList where email does not contain
        defaultNextSupplierMiMiFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSupplierMiMisByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierMiMi = nextSupplierMiMiRepository.saveAndFlush(nextSupplierMiMi);

        // Get all the nextSupplierMiMiList where phoneNumber equals to
        defaultNextSupplierMiMiFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextSupplierMiMisByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplierMiMi = nextSupplierMiMiRepository.saveAndFlush(nextSupplierMiMi);

        // Get all the nextSupplierMiMiList where phoneNumber in
        defaultNextSupplierMiMiFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierMiMisByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplierMiMi = nextSupplierMiMiRepository.saveAndFlush(nextSupplierMiMi);

        // Get all the nextSupplierMiMiList where phoneNumber is not null
        defaultNextSupplierMiMiFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSupplierMiMisByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierMiMi = nextSupplierMiMiRepository.saveAndFlush(nextSupplierMiMi);

        // Get all the nextSupplierMiMiList where phoneNumber contains
        defaultNextSupplierMiMiFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextSupplierMiMisByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierMiMi = nextSupplierMiMiRepository.saveAndFlush(nextSupplierMiMi);

        // Get all the nextSupplierMiMiList where phoneNumber does not contain
        defaultNextSupplierMiMiFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierMiMisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextSupplierMiMiRepository.saveAndFlush(nextSupplierMiMi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextSupplierMiMi.setTenant(tenant);
        nextSupplierMiMiRepository.saveAndFlush(nextSupplierMiMi);
        Long tenantId = tenant.getId();
        // Get all the nextSupplierMiMiList where tenant equals to tenantId
        defaultNextSupplierMiMiShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextSupplierMiMiList where tenant equals to (tenantId + 1)
        defaultNextSupplierMiMiShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllNextSupplierMiMisByProductsIsEqualToSomething() throws Exception {
        NextProductMiMi products;
        if (TestUtil.findAll(em, NextProductMiMi.class).isEmpty()) {
            nextSupplierMiMiRepository.saveAndFlush(nextSupplierMiMi);
            products = NextProductMiMiResourceIT.createEntity();
        } else {
            products = TestUtil.findAll(em, NextProductMiMi.class).get(0);
        }
        em.persist(products);
        em.flush();
        nextSupplierMiMi.addProducts(products);
        nextSupplierMiMiRepository.saveAndFlush(nextSupplierMiMi);
        Long productsId = products.getId();
        // Get all the nextSupplierMiMiList where products equals to productsId
        defaultNextSupplierMiMiShouldBeFound("productsId.equals=" + productsId);

        // Get all the nextSupplierMiMiList where products equals to (productsId + 1)
        defaultNextSupplierMiMiShouldNotBeFound("productsId.equals=" + (productsId + 1));
    }

    private void defaultNextSupplierMiMiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextSupplierMiMiShouldBeFound(shouldBeFound);
        defaultNextSupplierMiMiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextSupplierMiMiShouldBeFound(String filter) throws Exception {
        restNextSupplierMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextSupplierMiMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restNextSupplierMiMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextSupplierMiMiShouldNotBeFound(String filter) throws Exception {
        restNextSupplierMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextSupplierMiMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextSupplierMiMi() throws Exception {
        // Get the nextSupplierMiMi
        restNextSupplierMiMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextSupplierMiMi() throws Exception {
        // Initialize the database
        insertedNextSupplierMiMi = nextSupplierMiMiRepository.saveAndFlush(nextSupplierMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextSupplierMiMi
        NextSupplierMiMi updatedNextSupplierMiMi = nextSupplierMiMiRepository.findById(nextSupplierMiMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextSupplierMiMi are not directly saved in db
        em.detach(updatedNextSupplierMiMi);
        updatedNextSupplierMiMi
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        NextSupplierMiMiDTO nextSupplierMiMiDTO = nextSupplierMiMiMapper.toDto(updatedNextSupplierMiMi);

        restNextSupplierMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextSupplierMiMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextSupplierMiMiDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextSupplierMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextSupplierMiMiToMatchAllProperties(updatedNextSupplierMiMi);
    }

    @Test
    @Transactional
    void putNonExistingNextSupplierMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierMiMi.setId(longCount.incrementAndGet());

        // Create the NextSupplierMiMi
        NextSupplierMiMiDTO nextSupplierMiMiDTO = nextSupplierMiMiMapper.toDto(nextSupplierMiMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextSupplierMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextSupplierMiMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextSupplierMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextSupplierMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierMiMi.setId(longCount.incrementAndGet());

        // Create the NextSupplierMiMi
        NextSupplierMiMiDTO nextSupplierMiMiDTO = nextSupplierMiMiMapper.toDto(nextSupplierMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextSupplierMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextSupplierMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierMiMi.setId(longCount.incrementAndGet());

        // Create the NextSupplierMiMi
        NextSupplierMiMiDTO nextSupplierMiMiDTO = nextSupplierMiMiMapper.toDto(nextSupplierMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierMiMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierMiMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextSupplierMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextSupplierMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextSupplierMiMi = nextSupplierMiMiRepository.saveAndFlush(nextSupplierMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextSupplierMiMi using partial update
        NextSupplierMiMi partialUpdatedNextSupplierMiMi = new NextSupplierMiMi();
        partialUpdatedNextSupplierMiMi.setId(nextSupplierMiMi.getId());

        partialUpdatedNextSupplierMiMi.email(UPDATED_EMAIL);

        restNextSupplierMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextSupplierMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextSupplierMiMi))
            )
            .andExpect(status().isOk());

        // Validate the NextSupplierMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextSupplierMiMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextSupplierMiMi, nextSupplierMiMi),
            getPersistedNextSupplierMiMi(nextSupplierMiMi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextSupplierMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextSupplierMiMi = nextSupplierMiMiRepository.saveAndFlush(nextSupplierMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextSupplierMiMi using partial update
        NextSupplierMiMi partialUpdatedNextSupplierMiMi = new NextSupplierMiMi();
        partialUpdatedNextSupplierMiMi.setId(nextSupplierMiMi.getId());

        partialUpdatedNextSupplierMiMi
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restNextSupplierMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextSupplierMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextSupplierMiMi))
            )
            .andExpect(status().isOk());

        // Validate the NextSupplierMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextSupplierMiMiUpdatableFieldsEquals(
            partialUpdatedNextSupplierMiMi,
            getPersistedNextSupplierMiMi(partialUpdatedNextSupplierMiMi)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextSupplierMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierMiMi.setId(longCount.incrementAndGet());

        // Create the NextSupplierMiMi
        NextSupplierMiMiDTO nextSupplierMiMiDTO = nextSupplierMiMiMapper.toDto(nextSupplierMiMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextSupplierMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextSupplierMiMiDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextSupplierMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextSupplierMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierMiMi.setId(longCount.incrementAndGet());

        // Create the NextSupplierMiMi
        NextSupplierMiMiDTO nextSupplierMiMiDTO = nextSupplierMiMiMapper.toDto(nextSupplierMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextSupplierMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextSupplierMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierMiMi.setId(longCount.incrementAndGet());

        // Create the NextSupplierMiMi
        NextSupplierMiMiDTO nextSupplierMiMiDTO = nextSupplierMiMiMapper.toDto(nextSupplierMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierMiMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextSupplierMiMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextSupplierMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextSupplierMiMi() throws Exception {
        // Initialize the database
        insertedNextSupplierMiMi = nextSupplierMiMiRepository.saveAndFlush(nextSupplierMiMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextSupplierMiMi
        restNextSupplierMiMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextSupplierMiMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextSupplierMiMiRepository.count();
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

    protected NextSupplierMiMi getPersistedNextSupplierMiMi(NextSupplierMiMi nextSupplierMiMi) {
        return nextSupplierMiMiRepository.findById(nextSupplierMiMi.getId()).orElseThrow();
    }

    protected void assertPersistedNextSupplierMiMiToMatchAllProperties(NextSupplierMiMi expectedNextSupplierMiMi) {
        assertNextSupplierMiMiAllPropertiesEquals(expectedNextSupplierMiMi, getPersistedNextSupplierMiMi(expectedNextSupplierMiMi));
    }

    protected void assertPersistedNextSupplierMiMiToMatchUpdatableProperties(NextSupplierMiMi expectedNextSupplierMiMi) {
        assertNextSupplierMiMiAllUpdatablePropertiesEquals(
            expectedNextSupplierMiMi,
            getPersistedNextSupplierMiMi(expectedNextSupplierMiMi)
        );
    }
}
