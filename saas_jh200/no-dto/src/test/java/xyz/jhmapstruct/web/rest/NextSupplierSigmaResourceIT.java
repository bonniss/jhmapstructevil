package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextSupplierSigmaAsserts.*;
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
import xyz.jhmapstruct.domain.NextProductSigma;
import xyz.jhmapstruct.domain.NextSupplierSigma;
import xyz.jhmapstruct.repository.NextSupplierSigmaRepository;
import xyz.jhmapstruct.service.NextSupplierSigmaService;

/**
 * Integration tests for the {@link NextSupplierSigmaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextSupplierSigmaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_PERSON = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_PERSON = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/next-supplier-sigmas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextSupplierSigmaRepository nextSupplierSigmaRepository;

    @Mock
    private NextSupplierSigmaRepository nextSupplierSigmaRepositoryMock;

    @Mock
    private NextSupplierSigmaService nextSupplierSigmaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextSupplierSigmaMockMvc;

    private NextSupplierSigma nextSupplierSigma;

    private NextSupplierSigma insertedNextSupplierSigma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextSupplierSigma createEntity() {
        return new NextSupplierSigma()
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
    public static NextSupplierSigma createUpdatedEntity() {
        return new NextSupplierSigma()
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        nextSupplierSigma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextSupplierSigma != null) {
            nextSupplierSigmaRepository.delete(insertedNextSupplierSigma);
            insertedNextSupplierSigma = null;
        }
    }

    @Test
    @Transactional
    void createNextSupplierSigma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextSupplierSigma
        var returnedNextSupplierSigma = om.readValue(
            restNextSupplierSigmaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierSigma)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextSupplierSigma.class
        );

        // Validate the NextSupplierSigma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextSupplierSigmaUpdatableFieldsEquals(returnedNextSupplierSigma, getPersistedNextSupplierSigma(returnedNextSupplierSigma));

        insertedNextSupplierSigma = returnedNextSupplierSigma;
    }

    @Test
    @Transactional
    void createNextSupplierSigmaWithExistingId() throws Exception {
        // Create the NextSupplierSigma with an existing ID
        nextSupplierSigma.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextSupplierSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierSigma)))
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextSupplierSigma.setName(null);

        // Create the NextSupplierSigma, which fails.

        restNextSupplierSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierSigma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextSupplierSigmas() throws Exception {
        // Initialize the database
        insertedNextSupplierSigma = nextSupplierSigmaRepository.saveAndFlush(nextSupplierSigma);

        // Get all the nextSupplierSigmaList
        restNextSupplierSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextSupplierSigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextSupplierSigmasWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextSupplierSigmaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextSupplierSigmaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextSupplierSigmaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextSupplierSigmasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextSupplierSigmaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextSupplierSigmaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextSupplierSigmaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextSupplierSigma() throws Exception {
        // Initialize the database
        insertedNextSupplierSigma = nextSupplierSigmaRepository.saveAndFlush(nextSupplierSigma);

        // Get the nextSupplierSigma
        restNextSupplierSigmaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextSupplierSigma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextSupplierSigma.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.contactPerson").value(DEFAULT_CONTACT_PERSON))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getNextSupplierSigmasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextSupplierSigma = nextSupplierSigmaRepository.saveAndFlush(nextSupplierSigma);

        Long id = nextSupplierSigma.getId();

        defaultNextSupplierSigmaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextSupplierSigmaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextSupplierSigmaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextSupplierSigmasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierSigma = nextSupplierSigmaRepository.saveAndFlush(nextSupplierSigma);

        // Get all the nextSupplierSigmaList where name equals to
        defaultNextSupplierSigmaFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextSupplierSigmasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplierSigma = nextSupplierSigmaRepository.saveAndFlush(nextSupplierSigma);

        // Get all the nextSupplierSigmaList where name in
        defaultNextSupplierSigmaFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextSupplierSigmasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplierSigma = nextSupplierSigmaRepository.saveAndFlush(nextSupplierSigma);

        // Get all the nextSupplierSigmaList where name is not null
        defaultNextSupplierSigmaFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSupplierSigmasByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierSigma = nextSupplierSigmaRepository.saveAndFlush(nextSupplierSigma);

        // Get all the nextSupplierSigmaList where name contains
        defaultNextSupplierSigmaFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextSupplierSigmasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierSigma = nextSupplierSigmaRepository.saveAndFlush(nextSupplierSigma);

        // Get all the nextSupplierSigmaList where name does not contain
        defaultNextSupplierSigmaFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllNextSupplierSigmasByContactPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierSigma = nextSupplierSigmaRepository.saveAndFlush(nextSupplierSigma);

        // Get all the nextSupplierSigmaList where contactPerson equals to
        defaultNextSupplierSigmaFiltering(
            "contactPerson.equals=" + DEFAULT_CONTACT_PERSON,
            "contactPerson.equals=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierSigmasByContactPersonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplierSigma = nextSupplierSigmaRepository.saveAndFlush(nextSupplierSigma);

        // Get all the nextSupplierSigmaList where contactPerson in
        defaultNextSupplierSigmaFiltering(
            "contactPerson.in=" + DEFAULT_CONTACT_PERSON + "," + UPDATED_CONTACT_PERSON,
            "contactPerson.in=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierSigmasByContactPersonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplierSigma = nextSupplierSigmaRepository.saveAndFlush(nextSupplierSigma);

        // Get all the nextSupplierSigmaList where contactPerson is not null
        defaultNextSupplierSigmaFiltering("contactPerson.specified=true", "contactPerson.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSupplierSigmasByContactPersonContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierSigma = nextSupplierSigmaRepository.saveAndFlush(nextSupplierSigma);

        // Get all the nextSupplierSigmaList where contactPerson contains
        defaultNextSupplierSigmaFiltering(
            "contactPerson.contains=" + DEFAULT_CONTACT_PERSON,
            "contactPerson.contains=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierSigmasByContactPersonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierSigma = nextSupplierSigmaRepository.saveAndFlush(nextSupplierSigma);

        // Get all the nextSupplierSigmaList where contactPerson does not contain
        defaultNextSupplierSigmaFiltering(
            "contactPerson.doesNotContain=" + UPDATED_CONTACT_PERSON,
            "contactPerson.doesNotContain=" + DEFAULT_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierSigmasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierSigma = nextSupplierSigmaRepository.saveAndFlush(nextSupplierSigma);

        // Get all the nextSupplierSigmaList where email equals to
        defaultNextSupplierSigmaFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSupplierSigmasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplierSigma = nextSupplierSigmaRepository.saveAndFlush(nextSupplierSigma);

        // Get all the nextSupplierSigmaList where email in
        defaultNextSupplierSigmaFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSupplierSigmasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplierSigma = nextSupplierSigmaRepository.saveAndFlush(nextSupplierSigma);

        // Get all the nextSupplierSigmaList where email is not null
        defaultNextSupplierSigmaFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSupplierSigmasByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierSigma = nextSupplierSigmaRepository.saveAndFlush(nextSupplierSigma);

        // Get all the nextSupplierSigmaList where email contains
        defaultNextSupplierSigmaFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSupplierSigmasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierSigma = nextSupplierSigmaRepository.saveAndFlush(nextSupplierSigma);

        // Get all the nextSupplierSigmaList where email does not contain
        defaultNextSupplierSigmaFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSupplierSigmasByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierSigma = nextSupplierSigmaRepository.saveAndFlush(nextSupplierSigma);

        // Get all the nextSupplierSigmaList where phoneNumber equals to
        defaultNextSupplierSigmaFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextSupplierSigmasByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplierSigma = nextSupplierSigmaRepository.saveAndFlush(nextSupplierSigma);

        // Get all the nextSupplierSigmaList where phoneNumber in
        defaultNextSupplierSigmaFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierSigmasByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplierSigma = nextSupplierSigmaRepository.saveAndFlush(nextSupplierSigma);

        // Get all the nextSupplierSigmaList where phoneNumber is not null
        defaultNextSupplierSigmaFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSupplierSigmasByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierSigma = nextSupplierSigmaRepository.saveAndFlush(nextSupplierSigma);

        // Get all the nextSupplierSigmaList where phoneNumber contains
        defaultNextSupplierSigmaFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextSupplierSigmasByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierSigma = nextSupplierSigmaRepository.saveAndFlush(nextSupplierSigma);

        // Get all the nextSupplierSigmaList where phoneNumber does not contain
        defaultNextSupplierSigmaFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierSigmasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextSupplierSigmaRepository.saveAndFlush(nextSupplierSigma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextSupplierSigma.setTenant(tenant);
        nextSupplierSigmaRepository.saveAndFlush(nextSupplierSigma);
        Long tenantId = tenant.getId();
        // Get all the nextSupplierSigmaList where tenant equals to tenantId
        defaultNextSupplierSigmaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextSupplierSigmaList where tenant equals to (tenantId + 1)
        defaultNextSupplierSigmaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllNextSupplierSigmasByProductsIsEqualToSomething() throws Exception {
        NextProductSigma products;
        if (TestUtil.findAll(em, NextProductSigma.class).isEmpty()) {
            nextSupplierSigmaRepository.saveAndFlush(nextSupplierSigma);
            products = NextProductSigmaResourceIT.createEntity();
        } else {
            products = TestUtil.findAll(em, NextProductSigma.class).get(0);
        }
        em.persist(products);
        em.flush();
        nextSupplierSigma.addProducts(products);
        nextSupplierSigmaRepository.saveAndFlush(nextSupplierSigma);
        Long productsId = products.getId();
        // Get all the nextSupplierSigmaList where products equals to productsId
        defaultNextSupplierSigmaShouldBeFound("productsId.equals=" + productsId);

        // Get all the nextSupplierSigmaList where products equals to (productsId + 1)
        defaultNextSupplierSigmaShouldNotBeFound("productsId.equals=" + (productsId + 1));
    }

    private void defaultNextSupplierSigmaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextSupplierSigmaShouldBeFound(shouldBeFound);
        defaultNextSupplierSigmaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextSupplierSigmaShouldBeFound(String filter) throws Exception {
        restNextSupplierSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextSupplierSigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restNextSupplierSigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextSupplierSigmaShouldNotBeFound(String filter) throws Exception {
        restNextSupplierSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextSupplierSigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextSupplierSigma() throws Exception {
        // Get the nextSupplierSigma
        restNextSupplierSigmaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextSupplierSigma() throws Exception {
        // Initialize the database
        insertedNextSupplierSigma = nextSupplierSigmaRepository.saveAndFlush(nextSupplierSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextSupplierSigma
        NextSupplierSigma updatedNextSupplierSigma = nextSupplierSigmaRepository.findById(nextSupplierSigma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextSupplierSigma are not directly saved in db
        em.detach(updatedNextSupplierSigma);
        updatedNextSupplierSigma
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restNextSupplierSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextSupplierSigma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextSupplierSigma))
            )
            .andExpect(status().isOk());

        // Validate the NextSupplierSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextSupplierSigmaToMatchAllProperties(updatedNextSupplierSigma);
    }

    @Test
    @Transactional
    void putNonExistingNextSupplierSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierSigma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextSupplierSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextSupplierSigma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextSupplierSigma))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextSupplierSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierSigma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextSupplierSigma))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextSupplierSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierSigma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierSigmaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierSigma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextSupplierSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextSupplierSigmaWithPatch() throws Exception {
        // Initialize the database
        insertedNextSupplierSigma = nextSupplierSigmaRepository.saveAndFlush(nextSupplierSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextSupplierSigma using partial update
        NextSupplierSigma partialUpdatedNextSupplierSigma = new NextSupplierSigma();
        partialUpdatedNextSupplierSigma.setId(nextSupplierSigma.getId());

        partialUpdatedNextSupplierSigma.name(UPDATED_NAME).contactPerson(UPDATED_CONTACT_PERSON).email(UPDATED_EMAIL);

        restNextSupplierSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextSupplierSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextSupplierSigma))
            )
            .andExpect(status().isOk());

        // Validate the NextSupplierSigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextSupplierSigmaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextSupplierSigma, nextSupplierSigma),
            getPersistedNextSupplierSigma(nextSupplierSigma)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextSupplierSigmaWithPatch() throws Exception {
        // Initialize the database
        insertedNextSupplierSigma = nextSupplierSigmaRepository.saveAndFlush(nextSupplierSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextSupplierSigma using partial update
        NextSupplierSigma partialUpdatedNextSupplierSigma = new NextSupplierSigma();
        partialUpdatedNextSupplierSigma.setId(nextSupplierSigma.getId());

        partialUpdatedNextSupplierSigma
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restNextSupplierSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextSupplierSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextSupplierSigma))
            )
            .andExpect(status().isOk());

        // Validate the NextSupplierSigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextSupplierSigmaUpdatableFieldsEquals(
            partialUpdatedNextSupplierSigma,
            getPersistedNextSupplierSigma(partialUpdatedNextSupplierSigma)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextSupplierSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierSigma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextSupplierSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextSupplierSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextSupplierSigma))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextSupplierSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierSigma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextSupplierSigma))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextSupplierSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierSigma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierSigmaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextSupplierSigma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextSupplierSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextSupplierSigma() throws Exception {
        // Initialize the database
        insertedNextSupplierSigma = nextSupplierSigmaRepository.saveAndFlush(nextSupplierSigma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextSupplierSigma
        restNextSupplierSigmaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextSupplierSigma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextSupplierSigmaRepository.count();
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

    protected NextSupplierSigma getPersistedNextSupplierSigma(NextSupplierSigma nextSupplierSigma) {
        return nextSupplierSigmaRepository.findById(nextSupplierSigma.getId()).orElseThrow();
    }

    protected void assertPersistedNextSupplierSigmaToMatchAllProperties(NextSupplierSigma expectedNextSupplierSigma) {
        assertNextSupplierSigmaAllPropertiesEquals(expectedNextSupplierSigma, getPersistedNextSupplierSigma(expectedNextSupplierSigma));
    }

    protected void assertPersistedNextSupplierSigmaToMatchUpdatableProperties(NextSupplierSigma expectedNextSupplierSigma) {
        assertNextSupplierSigmaAllUpdatablePropertiesEquals(
            expectedNextSupplierSigma,
            getPersistedNextSupplierSigma(expectedNextSupplierSigma)
        );
    }
}
