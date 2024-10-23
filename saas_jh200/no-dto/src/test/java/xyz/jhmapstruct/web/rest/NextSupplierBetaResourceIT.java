package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextSupplierBetaAsserts.*;
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
import xyz.jhmapstruct.domain.NextProductBeta;
import xyz.jhmapstruct.domain.NextSupplierBeta;
import xyz.jhmapstruct.repository.NextSupplierBetaRepository;
import xyz.jhmapstruct.service.NextSupplierBetaService;

/**
 * Integration tests for the {@link NextSupplierBetaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextSupplierBetaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_PERSON = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_PERSON = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/next-supplier-betas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextSupplierBetaRepository nextSupplierBetaRepository;

    @Mock
    private NextSupplierBetaRepository nextSupplierBetaRepositoryMock;

    @Mock
    private NextSupplierBetaService nextSupplierBetaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextSupplierBetaMockMvc;

    private NextSupplierBeta nextSupplierBeta;

    private NextSupplierBeta insertedNextSupplierBeta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextSupplierBeta createEntity() {
        return new NextSupplierBeta()
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
    public static NextSupplierBeta createUpdatedEntity() {
        return new NextSupplierBeta()
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        nextSupplierBeta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextSupplierBeta != null) {
            nextSupplierBetaRepository.delete(insertedNextSupplierBeta);
            insertedNextSupplierBeta = null;
        }
    }

    @Test
    @Transactional
    void createNextSupplierBeta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextSupplierBeta
        var returnedNextSupplierBeta = om.readValue(
            restNextSupplierBetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierBeta)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextSupplierBeta.class
        );

        // Validate the NextSupplierBeta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextSupplierBetaUpdatableFieldsEquals(returnedNextSupplierBeta, getPersistedNextSupplierBeta(returnedNextSupplierBeta));

        insertedNextSupplierBeta = returnedNextSupplierBeta;
    }

    @Test
    @Transactional
    void createNextSupplierBetaWithExistingId() throws Exception {
        // Create the NextSupplierBeta with an existing ID
        nextSupplierBeta.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextSupplierBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierBeta)))
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextSupplierBeta.setName(null);

        // Create the NextSupplierBeta, which fails.

        restNextSupplierBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierBeta)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextSupplierBetas() throws Exception {
        // Initialize the database
        insertedNextSupplierBeta = nextSupplierBetaRepository.saveAndFlush(nextSupplierBeta);

        // Get all the nextSupplierBetaList
        restNextSupplierBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextSupplierBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextSupplierBetasWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextSupplierBetaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextSupplierBetaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextSupplierBetaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextSupplierBetasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextSupplierBetaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextSupplierBetaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextSupplierBetaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextSupplierBeta() throws Exception {
        // Initialize the database
        insertedNextSupplierBeta = nextSupplierBetaRepository.saveAndFlush(nextSupplierBeta);

        // Get the nextSupplierBeta
        restNextSupplierBetaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextSupplierBeta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextSupplierBeta.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.contactPerson").value(DEFAULT_CONTACT_PERSON))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getNextSupplierBetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextSupplierBeta = nextSupplierBetaRepository.saveAndFlush(nextSupplierBeta);

        Long id = nextSupplierBeta.getId();

        defaultNextSupplierBetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextSupplierBetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextSupplierBetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextSupplierBetasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierBeta = nextSupplierBetaRepository.saveAndFlush(nextSupplierBeta);

        // Get all the nextSupplierBetaList where name equals to
        defaultNextSupplierBetaFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextSupplierBetasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplierBeta = nextSupplierBetaRepository.saveAndFlush(nextSupplierBeta);

        // Get all the nextSupplierBetaList where name in
        defaultNextSupplierBetaFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextSupplierBetasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplierBeta = nextSupplierBetaRepository.saveAndFlush(nextSupplierBeta);

        // Get all the nextSupplierBetaList where name is not null
        defaultNextSupplierBetaFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSupplierBetasByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierBeta = nextSupplierBetaRepository.saveAndFlush(nextSupplierBeta);

        // Get all the nextSupplierBetaList where name contains
        defaultNextSupplierBetaFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextSupplierBetasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierBeta = nextSupplierBetaRepository.saveAndFlush(nextSupplierBeta);

        // Get all the nextSupplierBetaList where name does not contain
        defaultNextSupplierBetaFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllNextSupplierBetasByContactPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierBeta = nextSupplierBetaRepository.saveAndFlush(nextSupplierBeta);

        // Get all the nextSupplierBetaList where contactPerson equals to
        defaultNextSupplierBetaFiltering(
            "contactPerson.equals=" + DEFAULT_CONTACT_PERSON,
            "contactPerson.equals=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierBetasByContactPersonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplierBeta = nextSupplierBetaRepository.saveAndFlush(nextSupplierBeta);

        // Get all the nextSupplierBetaList where contactPerson in
        defaultNextSupplierBetaFiltering(
            "contactPerson.in=" + DEFAULT_CONTACT_PERSON + "," + UPDATED_CONTACT_PERSON,
            "contactPerson.in=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierBetasByContactPersonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplierBeta = nextSupplierBetaRepository.saveAndFlush(nextSupplierBeta);

        // Get all the nextSupplierBetaList where contactPerson is not null
        defaultNextSupplierBetaFiltering("contactPerson.specified=true", "contactPerson.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSupplierBetasByContactPersonContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierBeta = nextSupplierBetaRepository.saveAndFlush(nextSupplierBeta);

        // Get all the nextSupplierBetaList where contactPerson contains
        defaultNextSupplierBetaFiltering(
            "contactPerson.contains=" + DEFAULT_CONTACT_PERSON,
            "contactPerson.contains=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierBetasByContactPersonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierBeta = nextSupplierBetaRepository.saveAndFlush(nextSupplierBeta);

        // Get all the nextSupplierBetaList where contactPerson does not contain
        defaultNextSupplierBetaFiltering(
            "contactPerson.doesNotContain=" + UPDATED_CONTACT_PERSON,
            "contactPerson.doesNotContain=" + DEFAULT_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierBetasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierBeta = nextSupplierBetaRepository.saveAndFlush(nextSupplierBeta);

        // Get all the nextSupplierBetaList where email equals to
        defaultNextSupplierBetaFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSupplierBetasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplierBeta = nextSupplierBetaRepository.saveAndFlush(nextSupplierBeta);

        // Get all the nextSupplierBetaList where email in
        defaultNextSupplierBetaFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSupplierBetasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplierBeta = nextSupplierBetaRepository.saveAndFlush(nextSupplierBeta);

        // Get all the nextSupplierBetaList where email is not null
        defaultNextSupplierBetaFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSupplierBetasByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierBeta = nextSupplierBetaRepository.saveAndFlush(nextSupplierBeta);

        // Get all the nextSupplierBetaList where email contains
        defaultNextSupplierBetaFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSupplierBetasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierBeta = nextSupplierBetaRepository.saveAndFlush(nextSupplierBeta);

        // Get all the nextSupplierBetaList where email does not contain
        defaultNextSupplierBetaFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSupplierBetasByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierBeta = nextSupplierBetaRepository.saveAndFlush(nextSupplierBeta);

        // Get all the nextSupplierBetaList where phoneNumber equals to
        defaultNextSupplierBetaFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextSupplierBetasByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplierBeta = nextSupplierBetaRepository.saveAndFlush(nextSupplierBeta);

        // Get all the nextSupplierBetaList where phoneNumber in
        defaultNextSupplierBetaFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierBetasByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplierBeta = nextSupplierBetaRepository.saveAndFlush(nextSupplierBeta);

        // Get all the nextSupplierBetaList where phoneNumber is not null
        defaultNextSupplierBetaFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSupplierBetasByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierBeta = nextSupplierBetaRepository.saveAndFlush(nextSupplierBeta);

        // Get all the nextSupplierBetaList where phoneNumber contains
        defaultNextSupplierBetaFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextSupplierBetasByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierBeta = nextSupplierBetaRepository.saveAndFlush(nextSupplierBeta);

        // Get all the nextSupplierBetaList where phoneNumber does not contain
        defaultNextSupplierBetaFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierBetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextSupplierBetaRepository.saveAndFlush(nextSupplierBeta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextSupplierBeta.setTenant(tenant);
        nextSupplierBetaRepository.saveAndFlush(nextSupplierBeta);
        Long tenantId = tenant.getId();
        // Get all the nextSupplierBetaList where tenant equals to tenantId
        defaultNextSupplierBetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextSupplierBetaList where tenant equals to (tenantId + 1)
        defaultNextSupplierBetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllNextSupplierBetasByProductsIsEqualToSomething() throws Exception {
        NextProductBeta products;
        if (TestUtil.findAll(em, NextProductBeta.class).isEmpty()) {
            nextSupplierBetaRepository.saveAndFlush(nextSupplierBeta);
            products = NextProductBetaResourceIT.createEntity();
        } else {
            products = TestUtil.findAll(em, NextProductBeta.class).get(0);
        }
        em.persist(products);
        em.flush();
        nextSupplierBeta.addProducts(products);
        nextSupplierBetaRepository.saveAndFlush(nextSupplierBeta);
        Long productsId = products.getId();
        // Get all the nextSupplierBetaList where products equals to productsId
        defaultNextSupplierBetaShouldBeFound("productsId.equals=" + productsId);

        // Get all the nextSupplierBetaList where products equals to (productsId + 1)
        defaultNextSupplierBetaShouldNotBeFound("productsId.equals=" + (productsId + 1));
    }

    private void defaultNextSupplierBetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextSupplierBetaShouldBeFound(shouldBeFound);
        defaultNextSupplierBetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextSupplierBetaShouldBeFound(String filter) throws Exception {
        restNextSupplierBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextSupplierBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restNextSupplierBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextSupplierBetaShouldNotBeFound(String filter) throws Exception {
        restNextSupplierBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextSupplierBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextSupplierBeta() throws Exception {
        // Get the nextSupplierBeta
        restNextSupplierBetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextSupplierBeta() throws Exception {
        // Initialize the database
        insertedNextSupplierBeta = nextSupplierBetaRepository.saveAndFlush(nextSupplierBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextSupplierBeta
        NextSupplierBeta updatedNextSupplierBeta = nextSupplierBetaRepository.findById(nextSupplierBeta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextSupplierBeta are not directly saved in db
        em.detach(updatedNextSupplierBeta);
        updatedNextSupplierBeta
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restNextSupplierBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextSupplierBeta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextSupplierBeta))
            )
            .andExpect(status().isOk());

        // Validate the NextSupplierBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextSupplierBetaToMatchAllProperties(updatedNextSupplierBeta);
    }

    @Test
    @Transactional
    void putNonExistingNextSupplierBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierBeta.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextSupplierBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextSupplierBeta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextSupplierBeta))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextSupplierBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierBeta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextSupplierBeta))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextSupplierBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierBeta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierBetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierBeta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextSupplierBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextSupplierBetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextSupplierBeta = nextSupplierBetaRepository.saveAndFlush(nextSupplierBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextSupplierBeta using partial update
        NextSupplierBeta partialUpdatedNextSupplierBeta = new NextSupplierBeta();
        partialUpdatedNextSupplierBeta.setId(nextSupplierBeta.getId());

        partialUpdatedNextSupplierBeta.contactPerson(UPDATED_CONTACT_PERSON).email(UPDATED_EMAIL).phoneNumber(UPDATED_PHONE_NUMBER);

        restNextSupplierBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextSupplierBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextSupplierBeta))
            )
            .andExpect(status().isOk());

        // Validate the NextSupplierBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextSupplierBetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextSupplierBeta, nextSupplierBeta),
            getPersistedNextSupplierBeta(nextSupplierBeta)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextSupplierBetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextSupplierBeta = nextSupplierBetaRepository.saveAndFlush(nextSupplierBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextSupplierBeta using partial update
        NextSupplierBeta partialUpdatedNextSupplierBeta = new NextSupplierBeta();
        partialUpdatedNextSupplierBeta.setId(nextSupplierBeta.getId());

        partialUpdatedNextSupplierBeta
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restNextSupplierBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextSupplierBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextSupplierBeta))
            )
            .andExpect(status().isOk());

        // Validate the NextSupplierBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextSupplierBetaUpdatableFieldsEquals(
            partialUpdatedNextSupplierBeta,
            getPersistedNextSupplierBeta(partialUpdatedNextSupplierBeta)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextSupplierBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierBeta.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextSupplierBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextSupplierBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextSupplierBeta))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextSupplierBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierBeta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextSupplierBeta))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextSupplierBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierBeta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierBetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextSupplierBeta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextSupplierBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextSupplierBeta() throws Exception {
        // Initialize the database
        insertedNextSupplierBeta = nextSupplierBetaRepository.saveAndFlush(nextSupplierBeta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextSupplierBeta
        restNextSupplierBetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextSupplierBeta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextSupplierBetaRepository.count();
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

    protected NextSupplierBeta getPersistedNextSupplierBeta(NextSupplierBeta nextSupplierBeta) {
        return nextSupplierBetaRepository.findById(nextSupplierBeta.getId()).orElseThrow();
    }

    protected void assertPersistedNextSupplierBetaToMatchAllProperties(NextSupplierBeta expectedNextSupplierBeta) {
        assertNextSupplierBetaAllPropertiesEquals(expectedNextSupplierBeta, getPersistedNextSupplierBeta(expectedNextSupplierBeta));
    }

    protected void assertPersistedNextSupplierBetaToMatchUpdatableProperties(NextSupplierBeta expectedNextSupplierBeta) {
        assertNextSupplierBetaAllUpdatablePropertiesEquals(
            expectedNextSupplierBeta,
            getPersistedNextSupplierBeta(expectedNextSupplierBeta)
        );
    }
}
