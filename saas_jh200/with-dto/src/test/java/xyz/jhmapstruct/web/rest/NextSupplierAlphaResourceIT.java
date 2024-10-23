package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextSupplierAlphaAsserts.*;
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
import xyz.jhmapstruct.domain.NextProductAlpha;
import xyz.jhmapstruct.domain.NextSupplierAlpha;
import xyz.jhmapstruct.repository.NextSupplierAlphaRepository;
import xyz.jhmapstruct.service.NextSupplierAlphaService;
import xyz.jhmapstruct.service.dto.NextSupplierAlphaDTO;
import xyz.jhmapstruct.service.mapper.NextSupplierAlphaMapper;

/**
 * Integration tests for the {@link NextSupplierAlphaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextSupplierAlphaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_PERSON = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_PERSON = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/next-supplier-alphas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextSupplierAlphaRepository nextSupplierAlphaRepository;

    @Mock
    private NextSupplierAlphaRepository nextSupplierAlphaRepositoryMock;

    @Autowired
    private NextSupplierAlphaMapper nextSupplierAlphaMapper;

    @Mock
    private NextSupplierAlphaService nextSupplierAlphaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextSupplierAlphaMockMvc;

    private NextSupplierAlpha nextSupplierAlpha;

    private NextSupplierAlpha insertedNextSupplierAlpha;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextSupplierAlpha createEntity() {
        return new NextSupplierAlpha()
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
    public static NextSupplierAlpha createUpdatedEntity() {
        return new NextSupplierAlpha()
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        nextSupplierAlpha = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextSupplierAlpha != null) {
            nextSupplierAlphaRepository.delete(insertedNextSupplierAlpha);
            insertedNextSupplierAlpha = null;
        }
    }

    @Test
    @Transactional
    void createNextSupplierAlpha() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextSupplierAlpha
        NextSupplierAlphaDTO nextSupplierAlphaDTO = nextSupplierAlphaMapper.toDto(nextSupplierAlpha);
        var returnedNextSupplierAlphaDTO = om.readValue(
            restNextSupplierAlphaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierAlphaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextSupplierAlphaDTO.class
        );

        // Validate the NextSupplierAlpha in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextSupplierAlpha = nextSupplierAlphaMapper.toEntity(returnedNextSupplierAlphaDTO);
        assertNextSupplierAlphaUpdatableFieldsEquals(returnedNextSupplierAlpha, getPersistedNextSupplierAlpha(returnedNextSupplierAlpha));

        insertedNextSupplierAlpha = returnedNextSupplierAlpha;
    }

    @Test
    @Transactional
    void createNextSupplierAlphaWithExistingId() throws Exception {
        // Create the NextSupplierAlpha with an existing ID
        nextSupplierAlpha.setId(1L);
        NextSupplierAlphaDTO nextSupplierAlphaDTO = nextSupplierAlphaMapper.toDto(nextSupplierAlpha);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextSupplierAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierAlphaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextSupplierAlpha.setName(null);

        // Create the NextSupplierAlpha, which fails.
        NextSupplierAlphaDTO nextSupplierAlphaDTO = nextSupplierAlphaMapper.toDto(nextSupplierAlpha);

        restNextSupplierAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierAlphaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextSupplierAlphas() throws Exception {
        // Initialize the database
        insertedNextSupplierAlpha = nextSupplierAlphaRepository.saveAndFlush(nextSupplierAlpha);

        // Get all the nextSupplierAlphaList
        restNextSupplierAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextSupplierAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextSupplierAlphasWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextSupplierAlphaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextSupplierAlphaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextSupplierAlphaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextSupplierAlphasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextSupplierAlphaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextSupplierAlphaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextSupplierAlphaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextSupplierAlpha() throws Exception {
        // Initialize the database
        insertedNextSupplierAlpha = nextSupplierAlphaRepository.saveAndFlush(nextSupplierAlpha);

        // Get the nextSupplierAlpha
        restNextSupplierAlphaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextSupplierAlpha.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextSupplierAlpha.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.contactPerson").value(DEFAULT_CONTACT_PERSON))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getNextSupplierAlphasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextSupplierAlpha = nextSupplierAlphaRepository.saveAndFlush(nextSupplierAlpha);

        Long id = nextSupplierAlpha.getId();

        defaultNextSupplierAlphaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextSupplierAlphaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextSupplierAlphaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextSupplierAlphasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierAlpha = nextSupplierAlphaRepository.saveAndFlush(nextSupplierAlpha);

        // Get all the nextSupplierAlphaList where name equals to
        defaultNextSupplierAlphaFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextSupplierAlphasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplierAlpha = nextSupplierAlphaRepository.saveAndFlush(nextSupplierAlpha);

        // Get all the nextSupplierAlphaList where name in
        defaultNextSupplierAlphaFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextSupplierAlphasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplierAlpha = nextSupplierAlphaRepository.saveAndFlush(nextSupplierAlpha);

        // Get all the nextSupplierAlphaList where name is not null
        defaultNextSupplierAlphaFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSupplierAlphasByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierAlpha = nextSupplierAlphaRepository.saveAndFlush(nextSupplierAlpha);

        // Get all the nextSupplierAlphaList where name contains
        defaultNextSupplierAlphaFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextSupplierAlphasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierAlpha = nextSupplierAlphaRepository.saveAndFlush(nextSupplierAlpha);

        // Get all the nextSupplierAlphaList where name does not contain
        defaultNextSupplierAlphaFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllNextSupplierAlphasByContactPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierAlpha = nextSupplierAlphaRepository.saveAndFlush(nextSupplierAlpha);

        // Get all the nextSupplierAlphaList where contactPerson equals to
        defaultNextSupplierAlphaFiltering(
            "contactPerson.equals=" + DEFAULT_CONTACT_PERSON,
            "contactPerson.equals=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierAlphasByContactPersonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplierAlpha = nextSupplierAlphaRepository.saveAndFlush(nextSupplierAlpha);

        // Get all the nextSupplierAlphaList where contactPerson in
        defaultNextSupplierAlphaFiltering(
            "contactPerson.in=" + DEFAULT_CONTACT_PERSON + "," + UPDATED_CONTACT_PERSON,
            "contactPerson.in=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierAlphasByContactPersonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplierAlpha = nextSupplierAlphaRepository.saveAndFlush(nextSupplierAlpha);

        // Get all the nextSupplierAlphaList where contactPerson is not null
        defaultNextSupplierAlphaFiltering("contactPerson.specified=true", "contactPerson.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSupplierAlphasByContactPersonContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierAlpha = nextSupplierAlphaRepository.saveAndFlush(nextSupplierAlpha);

        // Get all the nextSupplierAlphaList where contactPerson contains
        defaultNextSupplierAlphaFiltering(
            "contactPerson.contains=" + DEFAULT_CONTACT_PERSON,
            "contactPerson.contains=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierAlphasByContactPersonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierAlpha = nextSupplierAlphaRepository.saveAndFlush(nextSupplierAlpha);

        // Get all the nextSupplierAlphaList where contactPerson does not contain
        defaultNextSupplierAlphaFiltering(
            "contactPerson.doesNotContain=" + UPDATED_CONTACT_PERSON,
            "contactPerson.doesNotContain=" + DEFAULT_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierAlphasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierAlpha = nextSupplierAlphaRepository.saveAndFlush(nextSupplierAlpha);

        // Get all the nextSupplierAlphaList where email equals to
        defaultNextSupplierAlphaFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSupplierAlphasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplierAlpha = nextSupplierAlphaRepository.saveAndFlush(nextSupplierAlpha);

        // Get all the nextSupplierAlphaList where email in
        defaultNextSupplierAlphaFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSupplierAlphasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplierAlpha = nextSupplierAlphaRepository.saveAndFlush(nextSupplierAlpha);

        // Get all the nextSupplierAlphaList where email is not null
        defaultNextSupplierAlphaFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSupplierAlphasByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierAlpha = nextSupplierAlphaRepository.saveAndFlush(nextSupplierAlpha);

        // Get all the nextSupplierAlphaList where email contains
        defaultNextSupplierAlphaFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSupplierAlphasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierAlpha = nextSupplierAlphaRepository.saveAndFlush(nextSupplierAlpha);

        // Get all the nextSupplierAlphaList where email does not contain
        defaultNextSupplierAlphaFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSupplierAlphasByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierAlpha = nextSupplierAlphaRepository.saveAndFlush(nextSupplierAlpha);

        // Get all the nextSupplierAlphaList where phoneNumber equals to
        defaultNextSupplierAlphaFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextSupplierAlphasByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplierAlpha = nextSupplierAlphaRepository.saveAndFlush(nextSupplierAlpha);

        // Get all the nextSupplierAlphaList where phoneNumber in
        defaultNextSupplierAlphaFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierAlphasByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplierAlpha = nextSupplierAlphaRepository.saveAndFlush(nextSupplierAlpha);

        // Get all the nextSupplierAlphaList where phoneNumber is not null
        defaultNextSupplierAlphaFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSupplierAlphasByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierAlpha = nextSupplierAlphaRepository.saveAndFlush(nextSupplierAlpha);

        // Get all the nextSupplierAlphaList where phoneNumber contains
        defaultNextSupplierAlphaFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextSupplierAlphasByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierAlpha = nextSupplierAlphaRepository.saveAndFlush(nextSupplierAlpha);

        // Get all the nextSupplierAlphaList where phoneNumber does not contain
        defaultNextSupplierAlphaFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierAlphasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextSupplierAlphaRepository.saveAndFlush(nextSupplierAlpha);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextSupplierAlpha.setTenant(tenant);
        nextSupplierAlphaRepository.saveAndFlush(nextSupplierAlpha);
        Long tenantId = tenant.getId();
        // Get all the nextSupplierAlphaList where tenant equals to tenantId
        defaultNextSupplierAlphaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextSupplierAlphaList where tenant equals to (tenantId + 1)
        defaultNextSupplierAlphaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllNextSupplierAlphasByProductsIsEqualToSomething() throws Exception {
        NextProductAlpha products;
        if (TestUtil.findAll(em, NextProductAlpha.class).isEmpty()) {
            nextSupplierAlphaRepository.saveAndFlush(nextSupplierAlpha);
            products = NextProductAlphaResourceIT.createEntity();
        } else {
            products = TestUtil.findAll(em, NextProductAlpha.class).get(0);
        }
        em.persist(products);
        em.flush();
        nextSupplierAlpha.addProducts(products);
        nextSupplierAlphaRepository.saveAndFlush(nextSupplierAlpha);
        Long productsId = products.getId();
        // Get all the nextSupplierAlphaList where products equals to productsId
        defaultNextSupplierAlphaShouldBeFound("productsId.equals=" + productsId);

        // Get all the nextSupplierAlphaList where products equals to (productsId + 1)
        defaultNextSupplierAlphaShouldNotBeFound("productsId.equals=" + (productsId + 1));
    }

    private void defaultNextSupplierAlphaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextSupplierAlphaShouldBeFound(shouldBeFound);
        defaultNextSupplierAlphaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextSupplierAlphaShouldBeFound(String filter) throws Exception {
        restNextSupplierAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextSupplierAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restNextSupplierAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextSupplierAlphaShouldNotBeFound(String filter) throws Exception {
        restNextSupplierAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextSupplierAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextSupplierAlpha() throws Exception {
        // Get the nextSupplierAlpha
        restNextSupplierAlphaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextSupplierAlpha() throws Exception {
        // Initialize the database
        insertedNextSupplierAlpha = nextSupplierAlphaRepository.saveAndFlush(nextSupplierAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextSupplierAlpha
        NextSupplierAlpha updatedNextSupplierAlpha = nextSupplierAlphaRepository.findById(nextSupplierAlpha.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextSupplierAlpha are not directly saved in db
        em.detach(updatedNextSupplierAlpha);
        updatedNextSupplierAlpha
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        NextSupplierAlphaDTO nextSupplierAlphaDTO = nextSupplierAlphaMapper.toDto(updatedNextSupplierAlpha);

        restNextSupplierAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextSupplierAlphaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextSupplierAlphaDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextSupplierAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextSupplierAlphaToMatchAllProperties(updatedNextSupplierAlpha);
    }

    @Test
    @Transactional
    void putNonExistingNextSupplierAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierAlpha.setId(longCount.incrementAndGet());

        // Create the NextSupplierAlpha
        NextSupplierAlphaDTO nextSupplierAlphaDTO = nextSupplierAlphaMapper.toDto(nextSupplierAlpha);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextSupplierAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextSupplierAlphaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextSupplierAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextSupplierAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierAlpha.setId(longCount.incrementAndGet());

        // Create the NextSupplierAlpha
        NextSupplierAlphaDTO nextSupplierAlphaDTO = nextSupplierAlphaMapper.toDto(nextSupplierAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextSupplierAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextSupplierAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierAlpha.setId(longCount.incrementAndGet());

        // Create the NextSupplierAlpha
        NextSupplierAlphaDTO nextSupplierAlphaDTO = nextSupplierAlphaMapper.toDto(nextSupplierAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierAlphaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierAlphaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextSupplierAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextSupplierAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedNextSupplierAlpha = nextSupplierAlphaRepository.saveAndFlush(nextSupplierAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextSupplierAlpha using partial update
        NextSupplierAlpha partialUpdatedNextSupplierAlpha = new NextSupplierAlpha();
        partialUpdatedNextSupplierAlpha.setId(nextSupplierAlpha.getId());

        partialUpdatedNextSupplierAlpha.phoneNumber(UPDATED_PHONE_NUMBER);

        restNextSupplierAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextSupplierAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextSupplierAlpha))
            )
            .andExpect(status().isOk());

        // Validate the NextSupplierAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextSupplierAlphaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextSupplierAlpha, nextSupplierAlpha),
            getPersistedNextSupplierAlpha(nextSupplierAlpha)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextSupplierAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedNextSupplierAlpha = nextSupplierAlphaRepository.saveAndFlush(nextSupplierAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextSupplierAlpha using partial update
        NextSupplierAlpha partialUpdatedNextSupplierAlpha = new NextSupplierAlpha();
        partialUpdatedNextSupplierAlpha.setId(nextSupplierAlpha.getId());

        partialUpdatedNextSupplierAlpha
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restNextSupplierAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextSupplierAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextSupplierAlpha))
            )
            .andExpect(status().isOk());

        // Validate the NextSupplierAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextSupplierAlphaUpdatableFieldsEquals(
            partialUpdatedNextSupplierAlpha,
            getPersistedNextSupplierAlpha(partialUpdatedNextSupplierAlpha)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextSupplierAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierAlpha.setId(longCount.incrementAndGet());

        // Create the NextSupplierAlpha
        NextSupplierAlphaDTO nextSupplierAlphaDTO = nextSupplierAlphaMapper.toDto(nextSupplierAlpha);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextSupplierAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextSupplierAlphaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextSupplierAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextSupplierAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierAlpha.setId(longCount.incrementAndGet());

        // Create the NextSupplierAlpha
        NextSupplierAlphaDTO nextSupplierAlphaDTO = nextSupplierAlphaMapper.toDto(nextSupplierAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextSupplierAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextSupplierAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierAlpha.setId(longCount.incrementAndGet());

        // Create the NextSupplierAlpha
        NextSupplierAlphaDTO nextSupplierAlphaDTO = nextSupplierAlphaMapper.toDto(nextSupplierAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierAlphaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextSupplierAlphaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextSupplierAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextSupplierAlpha() throws Exception {
        // Initialize the database
        insertedNextSupplierAlpha = nextSupplierAlphaRepository.saveAndFlush(nextSupplierAlpha);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextSupplierAlpha
        restNextSupplierAlphaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextSupplierAlpha.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextSupplierAlphaRepository.count();
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

    protected NextSupplierAlpha getPersistedNextSupplierAlpha(NextSupplierAlpha nextSupplierAlpha) {
        return nextSupplierAlphaRepository.findById(nextSupplierAlpha.getId()).orElseThrow();
    }

    protected void assertPersistedNextSupplierAlphaToMatchAllProperties(NextSupplierAlpha expectedNextSupplierAlpha) {
        assertNextSupplierAlphaAllPropertiesEquals(expectedNextSupplierAlpha, getPersistedNextSupplierAlpha(expectedNextSupplierAlpha));
    }

    protected void assertPersistedNextSupplierAlphaToMatchUpdatableProperties(NextSupplierAlpha expectedNextSupplierAlpha) {
        assertNextSupplierAlphaAllUpdatablePropertiesEquals(
            expectedNextSupplierAlpha,
            getPersistedNextSupplierAlpha(expectedNextSupplierAlpha)
        );
    }
}
