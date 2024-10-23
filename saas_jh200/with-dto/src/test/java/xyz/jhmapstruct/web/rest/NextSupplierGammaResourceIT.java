package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextSupplierGammaAsserts.*;
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
import xyz.jhmapstruct.domain.NextProductGamma;
import xyz.jhmapstruct.domain.NextSupplierGamma;
import xyz.jhmapstruct.repository.NextSupplierGammaRepository;
import xyz.jhmapstruct.service.NextSupplierGammaService;
import xyz.jhmapstruct.service.dto.NextSupplierGammaDTO;
import xyz.jhmapstruct.service.mapper.NextSupplierGammaMapper;

/**
 * Integration tests for the {@link NextSupplierGammaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextSupplierGammaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_PERSON = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_PERSON = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/next-supplier-gammas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextSupplierGammaRepository nextSupplierGammaRepository;

    @Mock
    private NextSupplierGammaRepository nextSupplierGammaRepositoryMock;

    @Autowired
    private NextSupplierGammaMapper nextSupplierGammaMapper;

    @Mock
    private NextSupplierGammaService nextSupplierGammaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextSupplierGammaMockMvc;

    private NextSupplierGamma nextSupplierGamma;

    private NextSupplierGamma insertedNextSupplierGamma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextSupplierGamma createEntity() {
        return new NextSupplierGamma()
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
    public static NextSupplierGamma createUpdatedEntity() {
        return new NextSupplierGamma()
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        nextSupplierGamma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextSupplierGamma != null) {
            nextSupplierGammaRepository.delete(insertedNextSupplierGamma);
            insertedNextSupplierGamma = null;
        }
    }

    @Test
    @Transactional
    void createNextSupplierGamma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextSupplierGamma
        NextSupplierGammaDTO nextSupplierGammaDTO = nextSupplierGammaMapper.toDto(nextSupplierGamma);
        var returnedNextSupplierGammaDTO = om.readValue(
            restNextSupplierGammaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierGammaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextSupplierGammaDTO.class
        );

        // Validate the NextSupplierGamma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextSupplierGamma = nextSupplierGammaMapper.toEntity(returnedNextSupplierGammaDTO);
        assertNextSupplierGammaUpdatableFieldsEquals(returnedNextSupplierGamma, getPersistedNextSupplierGamma(returnedNextSupplierGamma));

        insertedNextSupplierGamma = returnedNextSupplierGamma;
    }

    @Test
    @Transactional
    void createNextSupplierGammaWithExistingId() throws Exception {
        // Create the NextSupplierGamma with an existing ID
        nextSupplierGamma.setId(1L);
        NextSupplierGammaDTO nextSupplierGammaDTO = nextSupplierGammaMapper.toDto(nextSupplierGamma);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextSupplierGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierGammaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextSupplierGamma.setName(null);

        // Create the NextSupplierGamma, which fails.
        NextSupplierGammaDTO nextSupplierGammaDTO = nextSupplierGammaMapper.toDto(nextSupplierGamma);

        restNextSupplierGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierGammaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextSupplierGammas() throws Exception {
        // Initialize the database
        insertedNextSupplierGamma = nextSupplierGammaRepository.saveAndFlush(nextSupplierGamma);

        // Get all the nextSupplierGammaList
        restNextSupplierGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextSupplierGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextSupplierGammasWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextSupplierGammaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextSupplierGammaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextSupplierGammaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextSupplierGammasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextSupplierGammaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextSupplierGammaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextSupplierGammaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextSupplierGamma() throws Exception {
        // Initialize the database
        insertedNextSupplierGamma = nextSupplierGammaRepository.saveAndFlush(nextSupplierGamma);

        // Get the nextSupplierGamma
        restNextSupplierGammaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextSupplierGamma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextSupplierGamma.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.contactPerson").value(DEFAULT_CONTACT_PERSON))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getNextSupplierGammasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextSupplierGamma = nextSupplierGammaRepository.saveAndFlush(nextSupplierGamma);

        Long id = nextSupplierGamma.getId();

        defaultNextSupplierGammaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextSupplierGammaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextSupplierGammaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextSupplierGammasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierGamma = nextSupplierGammaRepository.saveAndFlush(nextSupplierGamma);

        // Get all the nextSupplierGammaList where name equals to
        defaultNextSupplierGammaFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextSupplierGammasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplierGamma = nextSupplierGammaRepository.saveAndFlush(nextSupplierGamma);

        // Get all the nextSupplierGammaList where name in
        defaultNextSupplierGammaFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextSupplierGammasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplierGamma = nextSupplierGammaRepository.saveAndFlush(nextSupplierGamma);

        // Get all the nextSupplierGammaList where name is not null
        defaultNextSupplierGammaFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSupplierGammasByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierGamma = nextSupplierGammaRepository.saveAndFlush(nextSupplierGamma);

        // Get all the nextSupplierGammaList where name contains
        defaultNextSupplierGammaFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextSupplierGammasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierGamma = nextSupplierGammaRepository.saveAndFlush(nextSupplierGamma);

        // Get all the nextSupplierGammaList where name does not contain
        defaultNextSupplierGammaFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllNextSupplierGammasByContactPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierGamma = nextSupplierGammaRepository.saveAndFlush(nextSupplierGamma);

        // Get all the nextSupplierGammaList where contactPerson equals to
        defaultNextSupplierGammaFiltering(
            "contactPerson.equals=" + DEFAULT_CONTACT_PERSON,
            "contactPerson.equals=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierGammasByContactPersonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplierGamma = nextSupplierGammaRepository.saveAndFlush(nextSupplierGamma);

        // Get all the nextSupplierGammaList where contactPerson in
        defaultNextSupplierGammaFiltering(
            "contactPerson.in=" + DEFAULT_CONTACT_PERSON + "," + UPDATED_CONTACT_PERSON,
            "contactPerson.in=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierGammasByContactPersonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplierGamma = nextSupplierGammaRepository.saveAndFlush(nextSupplierGamma);

        // Get all the nextSupplierGammaList where contactPerson is not null
        defaultNextSupplierGammaFiltering("contactPerson.specified=true", "contactPerson.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSupplierGammasByContactPersonContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierGamma = nextSupplierGammaRepository.saveAndFlush(nextSupplierGamma);

        // Get all the nextSupplierGammaList where contactPerson contains
        defaultNextSupplierGammaFiltering(
            "contactPerson.contains=" + DEFAULT_CONTACT_PERSON,
            "contactPerson.contains=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierGammasByContactPersonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierGamma = nextSupplierGammaRepository.saveAndFlush(nextSupplierGamma);

        // Get all the nextSupplierGammaList where contactPerson does not contain
        defaultNextSupplierGammaFiltering(
            "contactPerson.doesNotContain=" + UPDATED_CONTACT_PERSON,
            "contactPerson.doesNotContain=" + DEFAULT_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierGammasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierGamma = nextSupplierGammaRepository.saveAndFlush(nextSupplierGamma);

        // Get all the nextSupplierGammaList where email equals to
        defaultNextSupplierGammaFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSupplierGammasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplierGamma = nextSupplierGammaRepository.saveAndFlush(nextSupplierGamma);

        // Get all the nextSupplierGammaList where email in
        defaultNextSupplierGammaFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSupplierGammasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplierGamma = nextSupplierGammaRepository.saveAndFlush(nextSupplierGamma);

        // Get all the nextSupplierGammaList where email is not null
        defaultNextSupplierGammaFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSupplierGammasByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierGamma = nextSupplierGammaRepository.saveAndFlush(nextSupplierGamma);

        // Get all the nextSupplierGammaList where email contains
        defaultNextSupplierGammaFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSupplierGammasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierGamma = nextSupplierGammaRepository.saveAndFlush(nextSupplierGamma);

        // Get all the nextSupplierGammaList where email does not contain
        defaultNextSupplierGammaFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSupplierGammasByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierGamma = nextSupplierGammaRepository.saveAndFlush(nextSupplierGamma);

        // Get all the nextSupplierGammaList where phoneNumber equals to
        defaultNextSupplierGammaFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextSupplierGammasByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplierGamma = nextSupplierGammaRepository.saveAndFlush(nextSupplierGamma);

        // Get all the nextSupplierGammaList where phoneNumber in
        defaultNextSupplierGammaFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierGammasByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplierGamma = nextSupplierGammaRepository.saveAndFlush(nextSupplierGamma);

        // Get all the nextSupplierGammaList where phoneNumber is not null
        defaultNextSupplierGammaFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSupplierGammasByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierGamma = nextSupplierGammaRepository.saveAndFlush(nextSupplierGamma);

        // Get all the nextSupplierGammaList where phoneNumber contains
        defaultNextSupplierGammaFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextSupplierGammasByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierGamma = nextSupplierGammaRepository.saveAndFlush(nextSupplierGamma);

        // Get all the nextSupplierGammaList where phoneNumber does not contain
        defaultNextSupplierGammaFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierGammasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextSupplierGammaRepository.saveAndFlush(nextSupplierGamma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextSupplierGamma.setTenant(tenant);
        nextSupplierGammaRepository.saveAndFlush(nextSupplierGamma);
        Long tenantId = tenant.getId();
        // Get all the nextSupplierGammaList where tenant equals to tenantId
        defaultNextSupplierGammaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextSupplierGammaList where tenant equals to (tenantId + 1)
        defaultNextSupplierGammaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllNextSupplierGammasByProductsIsEqualToSomething() throws Exception {
        NextProductGamma products;
        if (TestUtil.findAll(em, NextProductGamma.class).isEmpty()) {
            nextSupplierGammaRepository.saveAndFlush(nextSupplierGamma);
            products = NextProductGammaResourceIT.createEntity();
        } else {
            products = TestUtil.findAll(em, NextProductGamma.class).get(0);
        }
        em.persist(products);
        em.flush();
        nextSupplierGamma.addProducts(products);
        nextSupplierGammaRepository.saveAndFlush(nextSupplierGamma);
        Long productsId = products.getId();
        // Get all the nextSupplierGammaList where products equals to productsId
        defaultNextSupplierGammaShouldBeFound("productsId.equals=" + productsId);

        // Get all the nextSupplierGammaList where products equals to (productsId + 1)
        defaultNextSupplierGammaShouldNotBeFound("productsId.equals=" + (productsId + 1));
    }

    private void defaultNextSupplierGammaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextSupplierGammaShouldBeFound(shouldBeFound);
        defaultNextSupplierGammaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextSupplierGammaShouldBeFound(String filter) throws Exception {
        restNextSupplierGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextSupplierGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restNextSupplierGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextSupplierGammaShouldNotBeFound(String filter) throws Exception {
        restNextSupplierGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextSupplierGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextSupplierGamma() throws Exception {
        // Get the nextSupplierGamma
        restNextSupplierGammaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextSupplierGamma() throws Exception {
        // Initialize the database
        insertedNextSupplierGamma = nextSupplierGammaRepository.saveAndFlush(nextSupplierGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextSupplierGamma
        NextSupplierGamma updatedNextSupplierGamma = nextSupplierGammaRepository.findById(nextSupplierGamma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextSupplierGamma are not directly saved in db
        em.detach(updatedNextSupplierGamma);
        updatedNextSupplierGamma
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        NextSupplierGammaDTO nextSupplierGammaDTO = nextSupplierGammaMapper.toDto(updatedNextSupplierGamma);

        restNextSupplierGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextSupplierGammaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextSupplierGammaDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextSupplierGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextSupplierGammaToMatchAllProperties(updatedNextSupplierGamma);
    }

    @Test
    @Transactional
    void putNonExistingNextSupplierGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierGamma.setId(longCount.incrementAndGet());

        // Create the NextSupplierGamma
        NextSupplierGammaDTO nextSupplierGammaDTO = nextSupplierGammaMapper.toDto(nextSupplierGamma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextSupplierGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextSupplierGammaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextSupplierGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextSupplierGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierGamma.setId(longCount.incrementAndGet());

        // Create the NextSupplierGamma
        NextSupplierGammaDTO nextSupplierGammaDTO = nextSupplierGammaMapper.toDto(nextSupplierGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextSupplierGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextSupplierGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierGamma.setId(longCount.incrementAndGet());

        // Create the NextSupplierGamma
        NextSupplierGammaDTO nextSupplierGammaDTO = nextSupplierGammaMapper.toDto(nextSupplierGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierGammaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierGammaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextSupplierGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextSupplierGammaWithPatch() throws Exception {
        // Initialize the database
        insertedNextSupplierGamma = nextSupplierGammaRepository.saveAndFlush(nextSupplierGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextSupplierGamma using partial update
        NextSupplierGamma partialUpdatedNextSupplierGamma = new NextSupplierGamma();
        partialUpdatedNextSupplierGamma.setId(nextSupplierGamma.getId());

        partialUpdatedNextSupplierGamma.contactPerson(UPDATED_CONTACT_PERSON).email(UPDATED_EMAIL);

        restNextSupplierGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextSupplierGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextSupplierGamma))
            )
            .andExpect(status().isOk());

        // Validate the NextSupplierGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextSupplierGammaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextSupplierGamma, nextSupplierGamma),
            getPersistedNextSupplierGamma(nextSupplierGamma)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextSupplierGammaWithPatch() throws Exception {
        // Initialize the database
        insertedNextSupplierGamma = nextSupplierGammaRepository.saveAndFlush(nextSupplierGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextSupplierGamma using partial update
        NextSupplierGamma partialUpdatedNextSupplierGamma = new NextSupplierGamma();
        partialUpdatedNextSupplierGamma.setId(nextSupplierGamma.getId());

        partialUpdatedNextSupplierGamma
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restNextSupplierGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextSupplierGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextSupplierGamma))
            )
            .andExpect(status().isOk());

        // Validate the NextSupplierGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextSupplierGammaUpdatableFieldsEquals(
            partialUpdatedNextSupplierGamma,
            getPersistedNextSupplierGamma(partialUpdatedNextSupplierGamma)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextSupplierGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierGamma.setId(longCount.incrementAndGet());

        // Create the NextSupplierGamma
        NextSupplierGammaDTO nextSupplierGammaDTO = nextSupplierGammaMapper.toDto(nextSupplierGamma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextSupplierGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextSupplierGammaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextSupplierGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextSupplierGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierGamma.setId(longCount.incrementAndGet());

        // Create the NextSupplierGamma
        NextSupplierGammaDTO nextSupplierGammaDTO = nextSupplierGammaMapper.toDto(nextSupplierGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextSupplierGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextSupplierGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierGamma.setId(longCount.incrementAndGet());

        // Create the NextSupplierGamma
        NextSupplierGammaDTO nextSupplierGammaDTO = nextSupplierGammaMapper.toDto(nextSupplierGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierGammaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextSupplierGammaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextSupplierGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextSupplierGamma() throws Exception {
        // Initialize the database
        insertedNextSupplierGamma = nextSupplierGammaRepository.saveAndFlush(nextSupplierGamma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextSupplierGamma
        restNextSupplierGammaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextSupplierGamma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextSupplierGammaRepository.count();
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

    protected NextSupplierGamma getPersistedNextSupplierGamma(NextSupplierGamma nextSupplierGamma) {
        return nextSupplierGammaRepository.findById(nextSupplierGamma.getId()).orElseThrow();
    }

    protected void assertPersistedNextSupplierGammaToMatchAllProperties(NextSupplierGamma expectedNextSupplierGamma) {
        assertNextSupplierGammaAllPropertiesEquals(expectedNextSupplierGamma, getPersistedNextSupplierGamma(expectedNextSupplierGamma));
    }

    protected void assertPersistedNextSupplierGammaToMatchUpdatableProperties(NextSupplierGamma expectedNextSupplierGamma) {
        assertNextSupplierGammaAllUpdatablePropertiesEquals(
            expectedNextSupplierGamma,
            getPersistedNextSupplierGamma(expectedNextSupplierGamma)
        );
    }
}
