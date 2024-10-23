package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextSupplierViViAsserts.*;
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
import xyz.jhmapstruct.domain.NextProductViVi;
import xyz.jhmapstruct.domain.NextSupplierViVi;
import xyz.jhmapstruct.repository.NextSupplierViViRepository;
import xyz.jhmapstruct.service.NextSupplierViViService;

/**
 * Integration tests for the {@link NextSupplierViViResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextSupplierViViResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_PERSON = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_PERSON = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/next-supplier-vi-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextSupplierViViRepository nextSupplierViViRepository;

    @Mock
    private NextSupplierViViRepository nextSupplierViViRepositoryMock;

    @Mock
    private NextSupplierViViService nextSupplierViViServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextSupplierViViMockMvc;

    private NextSupplierViVi nextSupplierViVi;

    private NextSupplierViVi insertedNextSupplierViVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextSupplierViVi createEntity() {
        return new NextSupplierViVi()
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
    public static NextSupplierViVi createUpdatedEntity() {
        return new NextSupplierViVi()
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        nextSupplierViVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextSupplierViVi != null) {
            nextSupplierViViRepository.delete(insertedNextSupplierViVi);
            insertedNextSupplierViVi = null;
        }
    }

    @Test
    @Transactional
    void createNextSupplierViVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextSupplierViVi
        var returnedNextSupplierViVi = om.readValue(
            restNextSupplierViViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierViVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextSupplierViVi.class
        );

        // Validate the NextSupplierViVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextSupplierViViUpdatableFieldsEquals(returnedNextSupplierViVi, getPersistedNextSupplierViVi(returnedNextSupplierViVi));

        insertedNextSupplierViVi = returnedNextSupplierViVi;
    }

    @Test
    @Transactional
    void createNextSupplierViViWithExistingId() throws Exception {
        // Create the NextSupplierViVi with an existing ID
        nextSupplierViVi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextSupplierViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierViVi)))
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextSupplierViVi.setName(null);

        // Create the NextSupplierViVi, which fails.

        restNextSupplierViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierViVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextSupplierViVis() throws Exception {
        // Initialize the database
        insertedNextSupplierViVi = nextSupplierViViRepository.saveAndFlush(nextSupplierViVi);

        // Get all the nextSupplierViViList
        restNextSupplierViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextSupplierViVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextSupplierViVisWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextSupplierViViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextSupplierViViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextSupplierViViServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextSupplierViVisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextSupplierViViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextSupplierViViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextSupplierViViRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextSupplierViVi() throws Exception {
        // Initialize the database
        insertedNextSupplierViVi = nextSupplierViViRepository.saveAndFlush(nextSupplierViVi);

        // Get the nextSupplierViVi
        restNextSupplierViViMockMvc
            .perform(get(ENTITY_API_URL_ID, nextSupplierViVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextSupplierViVi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.contactPerson").value(DEFAULT_CONTACT_PERSON))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getNextSupplierViVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextSupplierViVi = nextSupplierViViRepository.saveAndFlush(nextSupplierViVi);

        Long id = nextSupplierViVi.getId();

        defaultNextSupplierViViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextSupplierViViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextSupplierViViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextSupplierViVisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierViVi = nextSupplierViViRepository.saveAndFlush(nextSupplierViVi);

        // Get all the nextSupplierViViList where name equals to
        defaultNextSupplierViViFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextSupplierViVisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplierViVi = nextSupplierViViRepository.saveAndFlush(nextSupplierViVi);

        // Get all the nextSupplierViViList where name in
        defaultNextSupplierViViFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextSupplierViVisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplierViVi = nextSupplierViViRepository.saveAndFlush(nextSupplierViVi);

        // Get all the nextSupplierViViList where name is not null
        defaultNextSupplierViViFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSupplierViVisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierViVi = nextSupplierViViRepository.saveAndFlush(nextSupplierViVi);

        // Get all the nextSupplierViViList where name contains
        defaultNextSupplierViViFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextSupplierViVisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierViVi = nextSupplierViViRepository.saveAndFlush(nextSupplierViVi);

        // Get all the nextSupplierViViList where name does not contain
        defaultNextSupplierViViFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllNextSupplierViVisByContactPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierViVi = nextSupplierViViRepository.saveAndFlush(nextSupplierViVi);

        // Get all the nextSupplierViViList where contactPerson equals to
        defaultNextSupplierViViFiltering(
            "contactPerson.equals=" + DEFAULT_CONTACT_PERSON,
            "contactPerson.equals=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierViVisByContactPersonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplierViVi = nextSupplierViViRepository.saveAndFlush(nextSupplierViVi);

        // Get all the nextSupplierViViList where contactPerson in
        defaultNextSupplierViViFiltering(
            "contactPerson.in=" + DEFAULT_CONTACT_PERSON + "," + UPDATED_CONTACT_PERSON,
            "contactPerson.in=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierViVisByContactPersonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplierViVi = nextSupplierViViRepository.saveAndFlush(nextSupplierViVi);

        // Get all the nextSupplierViViList where contactPerson is not null
        defaultNextSupplierViViFiltering("contactPerson.specified=true", "contactPerson.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSupplierViVisByContactPersonContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierViVi = nextSupplierViViRepository.saveAndFlush(nextSupplierViVi);

        // Get all the nextSupplierViViList where contactPerson contains
        defaultNextSupplierViViFiltering(
            "contactPerson.contains=" + DEFAULT_CONTACT_PERSON,
            "contactPerson.contains=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierViVisByContactPersonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierViVi = nextSupplierViViRepository.saveAndFlush(nextSupplierViVi);

        // Get all the nextSupplierViViList where contactPerson does not contain
        defaultNextSupplierViViFiltering(
            "contactPerson.doesNotContain=" + UPDATED_CONTACT_PERSON,
            "contactPerson.doesNotContain=" + DEFAULT_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierViVisByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierViVi = nextSupplierViViRepository.saveAndFlush(nextSupplierViVi);

        // Get all the nextSupplierViViList where email equals to
        defaultNextSupplierViViFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSupplierViVisByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplierViVi = nextSupplierViViRepository.saveAndFlush(nextSupplierViVi);

        // Get all the nextSupplierViViList where email in
        defaultNextSupplierViViFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSupplierViVisByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplierViVi = nextSupplierViViRepository.saveAndFlush(nextSupplierViVi);

        // Get all the nextSupplierViViList where email is not null
        defaultNextSupplierViViFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSupplierViVisByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierViVi = nextSupplierViViRepository.saveAndFlush(nextSupplierViVi);

        // Get all the nextSupplierViViList where email contains
        defaultNextSupplierViViFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSupplierViVisByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierViVi = nextSupplierViViRepository.saveAndFlush(nextSupplierViVi);

        // Get all the nextSupplierViViList where email does not contain
        defaultNextSupplierViViFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSupplierViVisByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierViVi = nextSupplierViViRepository.saveAndFlush(nextSupplierViVi);

        // Get all the nextSupplierViViList where phoneNumber equals to
        defaultNextSupplierViViFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextSupplierViVisByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplierViVi = nextSupplierViViRepository.saveAndFlush(nextSupplierViVi);

        // Get all the nextSupplierViViList where phoneNumber in
        defaultNextSupplierViViFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierViVisByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplierViVi = nextSupplierViViRepository.saveAndFlush(nextSupplierViVi);

        // Get all the nextSupplierViViList where phoneNumber is not null
        defaultNextSupplierViViFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSupplierViVisByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierViVi = nextSupplierViViRepository.saveAndFlush(nextSupplierViVi);

        // Get all the nextSupplierViViList where phoneNumber contains
        defaultNextSupplierViViFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextSupplierViVisByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierViVi = nextSupplierViViRepository.saveAndFlush(nextSupplierViVi);

        // Get all the nextSupplierViViList where phoneNumber does not contain
        defaultNextSupplierViViFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierViVisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextSupplierViViRepository.saveAndFlush(nextSupplierViVi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextSupplierViVi.setTenant(tenant);
        nextSupplierViViRepository.saveAndFlush(nextSupplierViVi);
        Long tenantId = tenant.getId();
        // Get all the nextSupplierViViList where tenant equals to tenantId
        defaultNextSupplierViViShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextSupplierViViList where tenant equals to (tenantId + 1)
        defaultNextSupplierViViShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllNextSupplierViVisByProductsIsEqualToSomething() throws Exception {
        NextProductViVi products;
        if (TestUtil.findAll(em, NextProductViVi.class).isEmpty()) {
            nextSupplierViViRepository.saveAndFlush(nextSupplierViVi);
            products = NextProductViViResourceIT.createEntity();
        } else {
            products = TestUtil.findAll(em, NextProductViVi.class).get(0);
        }
        em.persist(products);
        em.flush();
        nextSupplierViVi.addProducts(products);
        nextSupplierViViRepository.saveAndFlush(nextSupplierViVi);
        Long productsId = products.getId();
        // Get all the nextSupplierViViList where products equals to productsId
        defaultNextSupplierViViShouldBeFound("productsId.equals=" + productsId);

        // Get all the nextSupplierViViList where products equals to (productsId + 1)
        defaultNextSupplierViViShouldNotBeFound("productsId.equals=" + (productsId + 1));
    }

    private void defaultNextSupplierViViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextSupplierViViShouldBeFound(shouldBeFound);
        defaultNextSupplierViViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextSupplierViViShouldBeFound(String filter) throws Exception {
        restNextSupplierViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextSupplierViVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restNextSupplierViViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextSupplierViViShouldNotBeFound(String filter) throws Exception {
        restNextSupplierViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextSupplierViViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextSupplierViVi() throws Exception {
        // Get the nextSupplierViVi
        restNextSupplierViViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextSupplierViVi() throws Exception {
        // Initialize the database
        insertedNextSupplierViVi = nextSupplierViViRepository.saveAndFlush(nextSupplierViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextSupplierViVi
        NextSupplierViVi updatedNextSupplierViVi = nextSupplierViViRepository.findById(nextSupplierViVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextSupplierViVi are not directly saved in db
        em.detach(updatedNextSupplierViVi);
        updatedNextSupplierViVi
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restNextSupplierViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextSupplierViVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextSupplierViVi))
            )
            .andExpect(status().isOk());

        // Validate the NextSupplierViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextSupplierViViToMatchAllProperties(updatedNextSupplierViVi);
    }

    @Test
    @Transactional
    void putNonExistingNextSupplierViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierViVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextSupplierViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextSupplierViVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextSupplierViVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextSupplierViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierViVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextSupplierViVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextSupplierViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierViVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierViViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierViVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextSupplierViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextSupplierViViWithPatch() throws Exception {
        // Initialize the database
        insertedNextSupplierViVi = nextSupplierViViRepository.saveAndFlush(nextSupplierViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextSupplierViVi using partial update
        NextSupplierViVi partialUpdatedNextSupplierViVi = new NextSupplierViVi();
        partialUpdatedNextSupplierViVi.setId(nextSupplierViVi.getId());

        partialUpdatedNextSupplierViVi.name(UPDATED_NAME).email(UPDATED_EMAIL);

        restNextSupplierViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextSupplierViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextSupplierViVi))
            )
            .andExpect(status().isOk());

        // Validate the NextSupplierViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextSupplierViViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextSupplierViVi, nextSupplierViVi),
            getPersistedNextSupplierViVi(nextSupplierViVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextSupplierViViWithPatch() throws Exception {
        // Initialize the database
        insertedNextSupplierViVi = nextSupplierViViRepository.saveAndFlush(nextSupplierViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextSupplierViVi using partial update
        NextSupplierViVi partialUpdatedNextSupplierViVi = new NextSupplierViVi();
        partialUpdatedNextSupplierViVi.setId(nextSupplierViVi.getId());

        partialUpdatedNextSupplierViVi
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restNextSupplierViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextSupplierViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextSupplierViVi))
            )
            .andExpect(status().isOk());

        // Validate the NextSupplierViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextSupplierViViUpdatableFieldsEquals(
            partialUpdatedNextSupplierViVi,
            getPersistedNextSupplierViVi(partialUpdatedNextSupplierViVi)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextSupplierViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierViVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextSupplierViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextSupplierViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextSupplierViVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextSupplierViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierViVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextSupplierViVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextSupplierViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierViVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierViViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextSupplierViVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextSupplierViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextSupplierViVi() throws Exception {
        // Initialize the database
        insertedNextSupplierViVi = nextSupplierViViRepository.saveAndFlush(nextSupplierViVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextSupplierViVi
        restNextSupplierViViMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextSupplierViVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextSupplierViViRepository.count();
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

    protected NextSupplierViVi getPersistedNextSupplierViVi(NextSupplierViVi nextSupplierViVi) {
        return nextSupplierViViRepository.findById(nextSupplierViVi.getId()).orElseThrow();
    }

    protected void assertPersistedNextSupplierViViToMatchAllProperties(NextSupplierViVi expectedNextSupplierViVi) {
        assertNextSupplierViViAllPropertiesEquals(expectedNextSupplierViVi, getPersistedNextSupplierViVi(expectedNextSupplierViVi));
    }

    protected void assertPersistedNextSupplierViViToMatchUpdatableProperties(NextSupplierViVi expectedNextSupplierViVi) {
        assertNextSupplierViViAllUpdatablePropertiesEquals(
            expectedNextSupplierViVi,
            getPersistedNextSupplierViVi(expectedNextSupplierViVi)
        );
    }
}
