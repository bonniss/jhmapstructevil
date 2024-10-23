package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextSupplierViAsserts.*;
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
import xyz.jhmapstruct.domain.NextProductVi;
import xyz.jhmapstruct.domain.NextSupplierVi;
import xyz.jhmapstruct.repository.NextSupplierViRepository;
import xyz.jhmapstruct.service.NextSupplierViService;
import xyz.jhmapstruct.service.dto.NextSupplierViDTO;
import xyz.jhmapstruct.service.mapper.NextSupplierViMapper;

/**
 * Integration tests for the {@link NextSupplierViResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextSupplierViResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_PERSON = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_PERSON = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/next-supplier-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextSupplierViRepository nextSupplierViRepository;

    @Mock
    private NextSupplierViRepository nextSupplierViRepositoryMock;

    @Autowired
    private NextSupplierViMapper nextSupplierViMapper;

    @Mock
    private NextSupplierViService nextSupplierViServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextSupplierViMockMvc;

    private NextSupplierVi nextSupplierVi;

    private NextSupplierVi insertedNextSupplierVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextSupplierVi createEntity() {
        return new NextSupplierVi()
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
    public static NextSupplierVi createUpdatedEntity() {
        return new NextSupplierVi()
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        nextSupplierVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextSupplierVi != null) {
            nextSupplierViRepository.delete(insertedNextSupplierVi);
            insertedNextSupplierVi = null;
        }
    }

    @Test
    @Transactional
    void createNextSupplierVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextSupplierVi
        NextSupplierViDTO nextSupplierViDTO = nextSupplierViMapper.toDto(nextSupplierVi);
        var returnedNextSupplierViDTO = om.readValue(
            restNextSupplierViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextSupplierViDTO.class
        );

        // Validate the NextSupplierVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextSupplierVi = nextSupplierViMapper.toEntity(returnedNextSupplierViDTO);
        assertNextSupplierViUpdatableFieldsEquals(returnedNextSupplierVi, getPersistedNextSupplierVi(returnedNextSupplierVi));

        insertedNextSupplierVi = returnedNextSupplierVi;
    }

    @Test
    @Transactional
    void createNextSupplierViWithExistingId() throws Exception {
        // Create the NextSupplierVi with an existing ID
        nextSupplierVi.setId(1L);
        NextSupplierViDTO nextSupplierViDTO = nextSupplierViMapper.toDto(nextSupplierVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextSupplierViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextSupplierVi.setName(null);

        // Create the NextSupplierVi, which fails.
        NextSupplierViDTO nextSupplierViDTO = nextSupplierViMapper.toDto(nextSupplierVi);

        restNextSupplierViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextSupplierVis() throws Exception {
        // Initialize the database
        insertedNextSupplierVi = nextSupplierViRepository.saveAndFlush(nextSupplierVi);

        // Get all the nextSupplierViList
        restNextSupplierViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextSupplierVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextSupplierVisWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextSupplierViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextSupplierViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextSupplierViServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextSupplierVisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextSupplierViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextSupplierViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextSupplierViRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextSupplierVi() throws Exception {
        // Initialize the database
        insertedNextSupplierVi = nextSupplierViRepository.saveAndFlush(nextSupplierVi);

        // Get the nextSupplierVi
        restNextSupplierViMockMvc
            .perform(get(ENTITY_API_URL_ID, nextSupplierVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextSupplierVi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.contactPerson").value(DEFAULT_CONTACT_PERSON))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getNextSupplierVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextSupplierVi = nextSupplierViRepository.saveAndFlush(nextSupplierVi);

        Long id = nextSupplierVi.getId();

        defaultNextSupplierViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextSupplierViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextSupplierViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextSupplierVisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierVi = nextSupplierViRepository.saveAndFlush(nextSupplierVi);

        // Get all the nextSupplierViList where name equals to
        defaultNextSupplierViFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextSupplierVisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplierVi = nextSupplierViRepository.saveAndFlush(nextSupplierVi);

        // Get all the nextSupplierViList where name in
        defaultNextSupplierViFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextSupplierVisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplierVi = nextSupplierViRepository.saveAndFlush(nextSupplierVi);

        // Get all the nextSupplierViList where name is not null
        defaultNextSupplierViFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSupplierVisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierVi = nextSupplierViRepository.saveAndFlush(nextSupplierVi);

        // Get all the nextSupplierViList where name contains
        defaultNextSupplierViFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextSupplierVisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierVi = nextSupplierViRepository.saveAndFlush(nextSupplierVi);

        // Get all the nextSupplierViList where name does not contain
        defaultNextSupplierViFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllNextSupplierVisByContactPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierVi = nextSupplierViRepository.saveAndFlush(nextSupplierVi);

        // Get all the nextSupplierViList where contactPerson equals to
        defaultNextSupplierViFiltering("contactPerson.equals=" + DEFAULT_CONTACT_PERSON, "contactPerson.equals=" + UPDATED_CONTACT_PERSON);
    }

    @Test
    @Transactional
    void getAllNextSupplierVisByContactPersonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplierVi = nextSupplierViRepository.saveAndFlush(nextSupplierVi);

        // Get all the nextSupplierViList where contactPerson in
        defaultNextSupplierViFiltering(
            "contactPerson.in=" + DEFAULT_CONTACT_PERSON + "," + UPDATED_CONTACT_PERSON,
            "contactPerson.in=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierVisByContactPersonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplierVi = nextSupplierViRepository.saveAndFlush(nextSupplierVi);

        // Get all the nextSupplierViList where contactPerson is not null
        defaultNextSupplierViFiltering("contactPerson.specified=true", "contactPerson.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSupplierVisByContactPersonContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierVi = nextSupplierViRepository.saveAndFlush(nextSupplierVi);

        // Get all the nextSupplierViList where contactPerson contains
        defaultNextSupplierViFiltering(
            "contactPerson.contains=" + DEFAULT_CONTACT_PERSON,
            "contactPerson.contains=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierVisByContactPersonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierVi = nextSupplierViRepository.saveAndFlush(nextSupplierVi);

        // Get all the nextSupplierViList where contactPerson does not contain
        defaultNextSupplierViFiltering(
            "contactPerson.doesNotContain=" + UPDATED_CONTACT_PERSON,
            "contactPerson.doesNotContain=" + DEFAULT_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierVisByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierVi = nextSupplierViRepository.saveAndFlush(nextSupplierVi);

        // Get all the nextSupplierViList where email equals to
        defaultNextSupplierViFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSupplierVisByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplierVi = nextSupplierViRepository.saveAndFlush(nextSupplierVi);

        // Get all the nextSupplierViList where email in
        defaultNextSupplierViFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSupplierVisByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplierVi = nextSupplierViRepository.saveAndFlush(nextSupplierVi);

        // Get all the nextSupplierViList where email is not null
        defaultNextSupplierViFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSupplierVisByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierVi = nextSupplierViRepository.saveAndFlush(nextSupplierVi);

        // Get all the nextSupplierViList where email contains
        defaultNextSupplierViFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSupplierVisByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierVi = nextSupplierViRepository.saveAndFlush(nextSupplierVi);

        // Get all the nextSupplierViList where email does not contain
        defaultNextSupplierViFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSupplierVisByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierVi = nextSupplierViRepository.saveAndFlush(nextSupplierVi);

        // Get all the nextSupplierViList where phoneNumber equals to
        defaultNextSupplierViFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextSupplierVisByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplierVi = nextSupplierViRepository.saveAndFlush(nextSupplierVi);

        // Get all the nextSupplierViList where phoneNumber in
        defaultNextSupplierViFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierVisByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplierVi = nextSupplierViRepository.saveAndFlush(nextSupplierVi);

        // Get all the nextSupplierViList where phoneNumber is not null
        defaultNextSupplierViFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSupplierVisByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierVi = nextSupplierViRepository.saveAndFlush(nextSupplierVi);

        // Get all the nextSupplierViList where phoneNumber contains
        defaultNextSupplierViFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextSupplierVisByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierVi = nextSupplierViRepository.saveAndFlush(nextSupplierVi);

        // Get all the nextSupplierViList where phoneNumber does not contain
        defaultNextSupplierViFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierVisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextSupplierViRepository.saveAndFlush(nextSupplierVi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextSupplierVi.setTenant(tenant);
        nextSupplierViRepository.saveAndFlush(nextSupplierVi);
        Long tenantId = tenant.getId();
        // Get all the nextSupplierViList where tenant equals to tenantId
        defaultNextSupplierViShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextSupplierViList where tenant equals to (tenantId + 1)
        defaultNextSupplierViShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllNextSupplierVisByProductsIsEqualToSomething() throws Exception {
        NextProductVi products;
        if (TestUtil.findAll(em, NextProductVi.class).isEmpty()) {
            nextSupplierViRepository.saveAndFlush(nextSupplierVi);
            products = NextProductViResourceIT.createEntity();
        } else {
            products = TestUtil.findAll(em, NextProductVi.class).get(0);
        }
        em.persist(products);
        em.flush();
        nextSupplierVi.addProducts(products);
        nextSupplierViRepository.saveAndFlush(nextSupplierVi);
        Long productsId = products.getId();
        // Get all the nextSupplierViList where products equals to productsId
        defaultNextSupplierViShouldBeFound("productsId.equals=" + productsId);

        // Get all the nextSupplierViList where products equals to (productsId + 1)
        defaultNextSupplierViShouldNotBeFound("productsId.equals=" + (productsId + 1));
    }

    private void defaultNextSupplierViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextSupplierViShouldBeFound(shouldBeFound);
        defaultNextSupplierViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextSupplierViShouldBeFound(String filter) throws Exception {
        restNextSupplierViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextSupplierVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restNextSupplierViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextSupplierViShouldNotBeFound(String filter) throws Exception {
        restNextSupplierViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextSupplierViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextSupplierVi() throws Exception {
        // Get the nextSupplierVi
        restNextSupplierViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextSupplierVi() throws Exception {
        // Initialize the database
        insertedNextSupplierVi = nextSupplierViRepository.saveAndFlush(nextSupplierVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextSupplierVi
        NextSupplierVi updatedNextSupplierVi = nextSupplierViRepository.findById(nextSupplierVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextSupplierVi are not directly saved in db
        em.detach(updatedNextSupplierVi);
        updatedNextSupplierVi
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        NextSupplierViDTO nextSupplierViDTO = nextSupplierViMapper.toDto(updatedNextSupplierVi);

        restNextSupplierViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextSupplierViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextSupplierViDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextSupplierVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextSupplierViToMatchAllProperties(updatedNextSupplierVi);
    }

    @Test
    @Transactional
    void putNonExistingNextSupplierVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierVi.setId(longCount.incrementAndGet());

        // Create the NextSupplierVi
        NextSupplierViDTO nextSupplierViDTO = nextSupplierViMapper.toDto(nextSupplierVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextSupplierViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextSupplierViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextSupplierViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextSupplierVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierVi.setId(longCount.incrementAndGet());

        // Create the NextSupplierVi
        NextSupplierViDTO nextSupplierViDTO = nextSupplierViMapper.toDto(nextSupplierVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextSupplierViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextSupplierVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierVi.setId(longCount.incrementAndGet());

        // Create the NextSupplierVi
        NextSupplierViDTO nextSupplierViDTO = nextSupplierViMapper.toDto(nextSupplierVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextSupplierVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextSupplierViWithPatch() throws Exception {
        // Initialize the database
        insertedNextSupplierVi = nextSupplierViRepository.saveAndFlush(nextSupplierVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextSupplierVi using partial update
        NextSupplierVi partialUpdatedNextSupplierVi = new NextSupplierVi();
        partialUpdatedNextSupplierVi.setId(nextSupplierVi.getId());

        partialUpdatedNextSupplierVi.contactPerson(UPDATED_CONTACT_PERSON);

        restNextSupplierViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextSupplierVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextSupplierVi))
            )
            .andExpect(status().isOk());

        // Validate the NextSupplierVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextSupplierViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextSupplierVi, nextSupplierVi),
            getPersistedNextSupplierVi(nextSupplierVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextSupplierViWithPatch() throws Exception {
        // Initialize the database
        insertedNextSupplierVi = nextSupplierViRepository.saveAndFlush(nextSupplierVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextSupplierVi using partial update
        NextSupplierVi partialUpdatedNextSupplierVi = new NextSupplierVi();
        partialUpdatedNextSupplierVi.setId(nextSupplierVi.getId());

        partialUpdatedNextSupplierVi
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restNextSupplierViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextSupplierVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextSupplierVi))
            )
            .andExpect(status().isOk());

        // Validate the NextSupplierVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextSupplierViUpdatableFieldsEquals(partialUpdatedNextSupplierVi, getPersistedNextSupplierVi(partialUpdatedNextSupplierVi));
    }

    @Test
    @Transactional
    void patchNonExistingNextSupplierVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierVi.setId(longCount.incrementAndGet());

        // Create the NextSupplierVi
        NextSupplierViDTO nextSupplierViDTO = nextSupplierViMapper.toDto(nextSupplierVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextSupplierViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextSupplierViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextSupplierViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextSupplierVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierVi.setId(longCount.incrementAndGet());

        // Create the NextSupplierVi
        NextSupplierViDTO nextSupplierViDTO = nextSupplierViMapper.toDto(nextSupplierVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextSupplierViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextSupplierVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierVi.setId(longCount.incrementAndGet());

        // Create the NextSupplierVi
        NextSupplierViDTO nextSupplierViDTO = nextSupplierViMapper.toDto(nextSupplierVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextSupplierViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextSupplierVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextSupplierVi() throws Exception {
        // Initialize the database
        insertedNextSupplierVi = nextSupplierViRepository.saveAndFlush(nextSupplierVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextSupplierVi
        restNextSupplierViMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextSupplierVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextSupplierViRepository.count();
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

    protected NextSupplierVi getPersistedNextSupplierVi(NextSupplierVi nextSupplierVi) {
        return nextSupplierViRepository.findById(nextSupplierVi.getId()).orElseThrow();
    }

    protected void assertPersistedNextSupplierViToMatchAllProperties(NextSupplierVi expectedNextSupplierVi) {
        assertNextSupplierViAllPropertiesEquals(expectedNextSupplierVi, getPersistedNextSupplierVi(expectedNextSupplierVi));
    }

    protected void assertPersistedNextSupplierViToMatchUpdatableProperties(NextSupplierVi expectedNextSupplierVi) {
        assertNextSupplierViAllUpdatablePropertiesEquals(expectedNextSupplierVi, getPersistedNextSupplierVi(expectedNextSupplierVi));
    }
}
