package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextSupplierThetaAsserts.*;
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
import xyz.jhmapstruct.domain.NextProductTheta;
import xyz.jhmapstruct.domain.NextSupplierTheta;
import xyz.jhmapstruct.repository.NextSupplierThetaRepository;
import xyz.jhmapstruct.service.NextSupplierThetaService;
import xyz.jhmapstruct.service.dto.NextSupplierThetaDTO;
import xyz.jhmapstruct.service.mapper.NextSupplierThetaMapper;

/**
 * Integration tests for the {@link NextSupplierThetaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextSupplierThetaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_PERSON = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_PERSON = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/next-supplier-thetas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextSupplierThetaRepository nextSupplierThetaRepository;

    @Mock
    private NextSupplierThetaRepository nextSupplierThetaRepositoryMock;

    @Autowired
    private NextSupplierThetaMapper nextSupplierThetaMapper;

    @Mock
    private NextSupplierThetaService nextSupplierThetaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextSupplierThetaMockMvc;

    private NextSupplierTheta nextSupplierTheta;

    private NextSupplierTheta insertedNextSupplierTheta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextSupplierTheta createEntity() {
        return new NextSupplierTheta()
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
    public static NextSupplierTheta createUpdatedEntity() {
        return new NextSupplierTheta()
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        nextSupplierTheta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextSupplierTheta != null) {
            nextSupplierThetaRepository.delete(insertedNextSupplierTheta);
            insertedNextSupplierTheta = null;
        }
    }

    @Test
    @Transactional
    void createNextSupplierTheta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextSupplierTheta
        NextSupplierThetaDTO nextSupplierThetaDTO = nextSupplierThetaMapper.toDto(nextSupplierTheta);
        var returnedNextSupplierThetaDTO = om.readValue(
            restNextSupplierThetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierThetaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextSupplierThetaDTO.class
        );

        // Validate the NextSupplierTheta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextSupplierTheta = nextSupplierThetaMapper.toEntity(returnedNextSupplierThetaDTO);
        assertNextSupplierThetaUpdatableFieldsEquals(returnedNextSupplierTheta, getPersistedNextSupplierTheta(returnedNextSupplierTheta));

        insertedNextSupplierTheta = returnedNextSupplierTheta;
    }

    @Test
    @Transactional
    void createNextSupplierThetaWithExistingId() throws Exception {
        // Create the NextSupplierTheta with an existing ID
        nextSupplierTheta.setId(1L);
        NextSupplierThetaDTO nextSupplierThetaDTO = nextSupplierThetaMapper.toDto(nextSupplierTheta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextSupplierThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierThetaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextSupplierTheta.setName(null);

        // Create the NextSupplierTheta, which fails.
        NextSupplierThetaDTO nextSupplierThetaDTO = nextSupplierThetaMapper.toDto(nextSupplierTheta);

        restNextSupplierThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierThetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextSupplierThetas() throws Exception {
        // Initialize the database
        insertedNextSupplierTheta = nextSupplierThetaRepository.saveAndFlush(nextSupplierTheta);

        // Get all the nextSupplierThetaList
        restNextSupplierThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextSupplierTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextSupplierThetasWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextSupplierThetaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextSupplierThetaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextSupplierThetaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextSupplierThetasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextSupplierThetaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextSupplierThetaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextSupplierThetaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextSupplierTheta() throws Exception {
        // Initialize the database
        insertedNextSupplierTheta = nextSupplierThetaRepository.saveAndFlush(nextSupplierTheta);

        // Get the nextSupplierTheta
        restNextSupplierThetaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextSupplierTheta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextSupplierTheta.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.contactPerson").value(DEFAULT_CONTACT_PERSON))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getNextSupplierThetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextSupplierTheta = nextSupplierThetaRepository.saveAndFlush(nextSupplierTheta);

        Long id = nextSupplierTheta.getId();

        defaultNextSupplierThetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextSupplierThetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextSupplierThetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextSupplierThetasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierTheta = nextSupplierThetaRepository.saveAndFlush(nextSupplierTheta);

        // Get all the nextSupplierThetaList where name equals to
        defaultNextSupplierThetaFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextSupplierThetasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplierTheta = nextSupplierThetaRepository.saveAndFlush(nextSupplierTheta);

        // Get all the nextSupplierThetaList where name in
        defaultNextSupplierThetaFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextSupplierThetasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplierTheta = nextSupplierThetaRepository.saveAndFlush(nextSupplierTheta);

        // Get all the nextSupplierThetaList where name is not null
        defaultNextSupplierThetaFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSupplierThetasByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierTheta = nextSupplierThetaRepository.saveAndFlush(nextSupplierTheta);

        // Get all the nextSupplierThetaList where name contains
        defaultNextSupplierThetaFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextSupplierThetasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierTheta = nextSupplierThetaRepository.saveAndFlush(nextSupplierTheta);

        // Get all the nextSupplierThetaList where name does not contain
        defaultNextSupplierThetaFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllNextSupplierThetasByContactPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierTheta = nextSupplierThetaRepository.saveAndFlush(nextSupplierTheta);

        // Get all the nextSupplierThetaList where contactPerson equals to
        defaultNextSupplierThetaFiltering(
            "contactPerson.equals=" + DEFAULT_CONTACT_PERSON,
            "contactPerson.equals=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierThetasByContactPersonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplierTheta = nextSupplierThetaRepository.saveAndFlush(nextSupplierTheta);

        // Get all the nextSupplierThetaList where contactPerson in
        defaultNextSupplierThetaFiltering(
            "contactPerson.in=" + DEFAULT_CONTACT_PERSON + "," + UPDATED_CONTACT_PERSON,
            "contactPerson.in=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierThetasByContactPersonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplierTheta = nextSupplierThetaRepository.saveAndFlush(nextSupplierTheta);

        // Get all the nextSupplierThetaList where contactPerson is not null
        defaultNextSupplierThetaFiltering("contactPerson.specified=true", "contactPerson.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSupplierThetasByContactPersonContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierTheta = nextSupplierThetaRepository.saveAndFlush(nextSupplierTheta);

        // Get all the nextSupplierThetaList where contactPerson contains
        defaultNextSupplierThetaFiltering(
            "contactPerson.contains=" + DEFAULT_CONTACT_PERSON,
            "contactPerson.contains=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierThetasByContactPersonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierTheta = nextSupplierThetaRepository.saveAndFlush(nextSupplierTheta);

        // Get all the nextSupplierThetaList where contactPerson does not contain
        defaultNextSupplierThetaFiltering(
            "contactPerson.doesNotContain=" + UPDATED_CONTACT_PERSON,
            "contactPerson.doesNotContain=" + DEFAULT_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierThetasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierTheta = nextSupplierThetaRepository.saveAndFlush(nextSupplierTheta);

        // Get all the nextSupplierThetaList where email equals to
        defaultNextSupplierThetaFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSupplierThetasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplierTheta = nextSupplierThetaRepository.saveAndFlush(nextSupplierTheta);

        // Get all the nextSupplierThetaList where email in
        defaultNextSupplierThetaFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSupplierThetasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplierTheta = nextSupplierThetaRepository.saveAndFlush(nextSupplierTheta);

        // Get all the nextSupplierThetaList where email is not null
        defaultNextSupplierThetaFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSupplierThetasByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierTheta = nextSupplierThetaRepository.saveAndFlush(nextSupplierTheta);

        // Get all the nextSupplierThetaList where email contains
        defaultNextSupplierThetaFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSupplierThetasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierTheta = nextSupplierThetaRepository.saveAndFlush(nextSupplierTheta);

        // Get all the nextSupplierThetaList where email does not contain
        defaultNextSupplierThetaFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSupplierThetasByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierTheta = nextSupplierThetaRepository.saveAndFlush(nextSupplierTheta);

        // Get all the nextSupplierThetaList where phoneNumber equals to
        defaultNextSupplierThetaFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextSupplierThetasByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplierTheta = nextSupplierThetaRepository.saveAndFlush(nextSupplierTheta);

        // Get all the nextSupplierThetaList where phoneNumber in
        defaultNextSupplierThetaFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierThetasByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplierTheta = nextSupplierThetaRepository.saveAndFlush(nextSupplierTheta);

        // Get all the nextSupplierThetaList where phoneNumber is not null
        defaultNextSupplierThetaFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSupplierThetasByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierTheta = nextSupplierThetaRepository.saveAndFlush(nextSupplierTheta);

        // Get all the nextSupplierThetaList where phoneNumber contains
        defaultNextSupplierThetaFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextSupplierThetasByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierTheta = nextSupplierThetaRepository.saveAndFlush(nextSupplierTheta);

        // Get all the nextSupplierThetaList where phoneNumber does not contain
        defaultNextSupplierThetaFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierThetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextSupplierThetaRepository.saveAndFlush(nextSupplierTheta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextSupplierTheta.setTenant(tenant);
        nextSupplierThetaRepository.saveAndFlush(nextSupplierTheta);
        Long tenantId = tenant.getId();
        // Get all the nextSupplierThetaList where tenant equals to tenantId
        defaultNextSupplierThetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextSupplierThetaList where tenant equals to (tenantId + 1)
        defaultNextSupplierThetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllNextSupplierThetasByProductsIsEqualToSomething() throws Exception {
        NextProductTheta products;
        if (TestUtil.findAll(em, NextProductTheta.class).isEmpty()) {
            nextSupplierThetaRepository.saveAndFlush(nextSupplierTheta);
            products = NextProductThetaResourceIT.createEntity();
        } else {
            products = TestUtil.findAll(em, NextProductTheta.class).get(0);
        }
        em.persist(products);
        em.flush();
        nextSupplierTheta.addProducts(products);
        nextSupplierThetaRepository.saveAndFlush(nextSupplierTheta);
        Long productsId = products.getId();
        // Get all the nextSupplierThetaList where products equals to productsId
        defaultNextSupplierThetaShouldBeFound("productsId.equals=" + productsId);

        // Get all the nextSupplierThetaList where products equals to (productsId + 1)
        defaultNextSupplierThetaShouldNotBeFound("productsId.equals=" + (productsId + 1));
    }

    private void defaultNextSupplierThetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextSupplierThetaShouldBeFound(shouldBeFound);
        defaultNextSupplierThetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextSupplierThetaShouldBeFound(String filter) throws Exception {
        restNextSupplierThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextSupplierTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restNextSupplierThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextSupplierThetaShouldNotBeFound(String filter) throws Exception {
        restNextSupplierThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextSupplierThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextSupplierTheta() throws Exception {
        // Get the nextSupplierTheta
        restNextSupplierThetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextSupplierTheta() throws Exception {
        // Initialize the database
        insertedNextSupplierTheta = nextSupplierThetaRepository.saveAndFlush(nextSupplierTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextSupplierTheta
        NextSupplierTheta updatedNextSupplierTheta = nextSupplierThetaRepository.findById(nextSupplierTheta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextSupplierTheta are not directly saved in db
        em.detach(updatedNextSupplierTheta);
        updatedNextSupplierTheta
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        NextSupplierThetaDTO nextSupplierThetaDTO = nextSupplierThetaMapper.toDto(updatedNextSupplierTheta);

        restNextSupplierThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextSupplierThetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextSupplierThetaDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextSupplierTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextSupplierThetaToMatchAllProperties(updatedNextSupplierTheta);
    }

    @Test
    @Transactional
    void putNonExistingNextSupplierTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierTheta.setId(longCount.incrementAndGet());

        // Create the NextSupplierTheta
        NextSupplierThetaDTO nextSupplierThetaDTO = nextSupplierThetaMapper.toDto(nextSupplierTheta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextSupplierThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextSupplierThetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextSupplierThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextSupplierTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierTheta.setId(longCount.incrementAndGet());

        // Create the NextSupplierTheta
        NextSupplierThetaDTO nextSupplierThetaDTO = nextSupplierThetaMapper.toDto(nextSupplierTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextSupplierThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextSupplierTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierTheta.setId(longCount.incrementAndGet());

        // Create the NextSupplierTheta
        NextSupplierThetaDTO nextSupplierThetaDTO = nextSupplierThetaMapper.toDto(nextSupplierTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierThetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierThetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextSupplierTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextSupplierThetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextSupplierTheta = nextSupplierThetaRepository.saveAndFlush(nextSupplierTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextSupplierTheta using partial update
        NextSupplierTheta partialUpdatedNextSupplierTheta = new NextSupplierTheta();
        partialUpdatedNextSupplierTheta.setId(nextSupplierTheta.getId());

        partialUpdatedNextSupplierTheta
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restNextSupplierThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextSupplierTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextSupplierTheta))
            )
            .andExpect(status().isOk());

        // Validate the NextSupplierTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextSupplierThetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextSupplierTheta, nextSupplierTheta),
            getPersistedNextSupplierTheta(nextSupplierTheta)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextSupplierThetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextSupplierTheta = nextSupplierThetaRepository.saveAndFlush(nextSupplierTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextSupplierTheta using partial update
        NextSupplierTheta partialUpdatedNextSupplierTheta = new NextSupplierTheta();
        partialUpdatedNextSupplierTheta.setId(nextSupplierTheta.getId());

        partialUpdatedNextSupplierTheta
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restNextSupplierThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextSupplierTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextSupplierTheta))
            )
            .andExpect(status().isOk());

        // Validate the NextSupplierTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextSupplierThetaUpdatableFieldsEquals(
            partialUpdatedNextSupplierTheta,
            getPersistedNextSupplierTheta(partialUpdatedNextSupplierTheta)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextSupplierTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierTheta.setId(longCount.incrementAndGet());

        // Create the NextSupplierTheta
        NextSupplierThetaDTO nextSupplierThetaDTO = nextSupplierThetaMapper.toDto(nextSupplierTheta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextSupplierThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextSupplierThetaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextSupplierThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextSupplierTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierTheta.setId(longCount.incrementAndGet());

        // Create the NextSupplierTheta
        NextSupplierThetaDTO nextSupplierThetaDTO = nextSupplierThetaMapper.toDto(nextSupplierTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextSupplierThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextSupplierTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierTheta.setId(longCount.incrementAndGet());

        // Create the NextSupplierTheta
        NextSupplierThetaDTO nextSupplierThetaDTO = nextSupplierThetaMapper.toDto(nextSupplierTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierThetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextSupplierThetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextSupplierTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextSupplierTheta() throws Exception {
        // Initialize the database
        insertedNextSupplierTheta = nextSupplierThetaRepository.saveAndFlush(nextSupplierTheta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextSupplierTheta
        restNextSupplierThetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextSupplierTheta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextSupplierThetaRepository.count();
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

    protected NextSupplierTheta getPersistedNextSupplierTheta(NextSupplierTheta nextSupplierTheta) {
        return nextSupplierThetaRepository.findById(nextSupplierTheta.getId()).orElseThrow();
    }

    protected void assertPersistedNextSupplierThetaToMatchAllProperties(NextSupplierTheta expectedNextSupplierTheta) {
        assertNextSupplierThetaAllPropertiesEquals(expectedNextSupplierTheta, getPersistedNextSupplierTheta(expectedNextSupplierTheta));
    }

    protected void assertPersistedNextSupplierThetaToMatchUpdatableProperties(NextSupplierTheta expectedNextSupplierTheta) {
        assertNextSupplierThetaAllUpdatablePropertiesEquals(
            expectedNextSupplierTheta,
            getPersistedNextSupplierTheta(expectedNextSupplierTheta)
        );
    }
}
