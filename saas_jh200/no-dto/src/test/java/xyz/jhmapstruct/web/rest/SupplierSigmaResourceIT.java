package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.SupplierSigmaAsserts.*;
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
import xyz.jhmapstruct.domain.ProductSigma;
import xyz.jhmapstruct.domain.SupplierSigma;
import xyz.jhmapstruct.repository.SupplierSigmaRepository;
import xyz.jhmapstruct.service.SupplierSigmaService;

/**
 * Integration tests for the {@link SupplierSigmaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SupplierSigmaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_PERSON = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_PERSON = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/supplier-sigmas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SupplierSigmaRepository supplierSigmaRepository;

    @Mock
    private SupplierSigmaRepository supplierSigmaRepositoryMock;

    @Mock
    private SupplierSigmaService supplierSigmaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSupplierSigmaMockMvc;

    private SupplierSigma supplierSigma;

    private SupplierSigma insertedSupplierSigma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SupplierSigma createEntity() {
        return new SupplierSigma()
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
    public static SupplierSigma createUpdatedEntity() {
        return new SupplierSigma()
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        supplierSigma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSupplierSigma != null) {
            supplierSigmaRepository.delete(insertedSupplierSigma);
            insertedSupplierSigma = null;
        }
    }

    @Test
    @Transactional
    void createSupplierSigma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SupplierSigma
        var returnedSupplierSigma = om.readValue(
            restSupplierSigmaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierSigma)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SupplierSigma.class
        );

        // Validate the SupplierSigma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSupplierSigmaUpdatableFieldsEquals(returnedSupplierSigma, getPersistedSupplierSigma(returnedSupplierSigma));

        insertedSupplierSigma = returnedSupplierSigma;
    }

    @Test
    @Transactional
    void createSupplierSigmaWithExistingId() throws Exception {
        // Create the SupplierSigma with an existing ID
        supplierSigma.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSupplierSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierSigma)))
            .andExpect(status().isBadRequest());

        // Validate the SupplierSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        supplierSigma.setName(null);

        // Create the SupplierSigma, which fails.

        restSupplierSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierSigma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSupplierSigmas() throws Exception {
        // Initialize the database
        insertedSupplierSigma = supplierSigmaRepository.saveAndFlush(supplierSigma);

        // Get all the supplierSigmaList
        restSupplierSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplierSigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSupplierSigmasWithEagerRelationshipsIsEnabled() throws Exception {
        when(supplierSigmaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSupplierSigmaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(supplierSigmaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSupplierSigmasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(supplierSigmaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSupplierSigmaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(supplierSigmaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSupplierSigma() throws Exception {
        // Initialize the database
        insertedSupplierSigma = supplierSigmaRepository.saveAndFlush(supplierSigma);

        // Get the supplierSigma
        restSupplierSigmaMockMvc
            .perform(get(ENTITY_API_URL_ID, supplierSigma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(supplierSigma.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.contactPerson").value(DEFAULT_CONTACT_PERSON))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getSupplierSigmasByIdFiltering() throws Exception {
        // Initialize the database
        insertedSupplierSigma = supplierSigmaRepository.saveAndFlush(supplierSigma);

        Long id = supplierSigma.getId();

        defaultSupplierSigmaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSupplierSigmaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSupplierSigmaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSupplierSigmasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierSigma = supplierSigmaRepository.saveAndFlush(supplierSigma);

        // Get all the supplierSigmaList where name equals to
        defaultSupplierSigmaFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSupplierSigmasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierSigma = supplierSigmaRepository.saveAndFlush(supplierSigma);

        // Get all the supplierSigmaList where name in
        defaultSupplierSigmaFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSupplierSigmasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierSigma = supplierSigmaRepository.saveAndFlush(supplierSigma);

        // Get all the supplierSigmaList where name is not null
        defaultSupplierSigmaFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierSigmasByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierSigma = supplierSigmaRepository.saveAndFlush(supplierSigma);

        // Get all the supplierSigmaList where name contains
        defaultSupplierSigmaFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSupplierSigmasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierSigma = supplierSigmaRepository.saveAndFlush(supplierSigma);

        // Get all the supplierSigmaList where name does not contain
        defaultSupplierSigmaFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllSupplierSigmasByContactPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierSigma = supplierSigmaRepository.saveAndFlush(supplierSigma);

        // Get all the supplierSigmaList where contactPerson equals to
        defaultSupplierSigmaFiltering("contactPerson.equals=" + DEFAULT_CONTACT_PERSON, "contactPerson.equals=" + UPDATED_CONTACT_PERSON);
    }

    @Test
    @Transactional
    void getAllSupplierSigmasByContactPersonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierSigma = supplierSigmaRepository.saveAndFlush(supplierSigma);

        // Get all the supplierSigmaList where contactPerson in
        defaultSupplierSigmaFiltering(
            "contactPerson.in=" + DEFAULT_CONTACT_PERSON + "," + UPDATED_CONTACT_PERSON,
            "contactPerson.in=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllSupplierSigmasByContactPersonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierSigma = supplierSigmaRepository.saveAndFlush(supplierSigma);

        // Get all the supplierSigmaList where contactPerson is not null
        defaultSupplierSigmaFiltering("contactPerson.specified=true", "contactPerson.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierSigmasByContactPersonContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierSigma = supplierSigmaRepository.saveAndFlush(supplierSigma);

        // Get all the supplierSigmaList where contactPerson contains
        defaultSupplierSigmaFiltering(
            "contactPerson.contains=" + DEFAULT_CONTACT_PERSON,
            "contactPerson.contains=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllSupplierSigmasByContactPersonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierSigma = supplierSigmaRepository.saveAndFlush(supplierSigma);

        // Get all the supplierSigmaList where contactPerson does not contain
        defaultSupplierSigmaFiltering(
            "contactPerson.doesNotContain=" + UPDATED_CONTACT_PERSON,
            "contactPerson.doesNotContain=" + DEFAULT_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllSupplierSigmasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierSigma = supplierSigmaRepository.saveAndFlush(supplierSigma);

        // Get all the supplierSigmaList where email equals to
        defaultSupplierSigmaFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSupplierSigmasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierSigma = supplierSigmaRepository.saveAndFlush(supplierSigma);

        // Get all the supplierSigmaList where email in
        defaultSupplierSigmaFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSupplierSigmasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierSigma = supplierSigmaRepository.saveAndFlush(supplierSigma);

        // Get all the supplierSigmaList where email is not null
        defaultSupplierSigmaFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierSigmasByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierSigma = supplierSigmaRepository.saveAndFlush(supplierSigma);

        // Get all the supplierSigmaList where email contains
        defaultSupplierSigmaFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSupplierSigmasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierSigma = supplierSigmaRepository.saveAndFlush(supplierSigma);

        // Get all the supplierSigmaList where email does not contain
        defaultSupplierSigmaFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllSupplierSigmasByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierSigma = supplierSigmaRepository.saveAndFlush(supplierSigma);

        // Get all the supplierSigmaList where phoneNumber equals to
        defaultSupplierSigmaFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllSupplierSigmasByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierSigma = supplierSigmaRepository.saveAndFlush(supplierSigma);

        // Get all the supplierSigmaList where phoneNumber in
        defaultSupplierSigmaFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllSupplierSigmasByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierSigma = supplierSigmaRepository.saveAndFlush(supplierSigma);

        // Get all the supplierSigmaList where phoneNumber is not null
        defaultSupplierSigmaFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierSigmasByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierSigma = supplierSigmaRepository.saveAndFlush(supplierSigma);

        // Get all the supplierSigmaList where phoneNumber contains
        defaultSupplierSigmaFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllSupplierSigmasByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierSigma = supplierSigmaRepository.saveAndFlush(supplierSigma);

        // Get all the supplierSigmaList where phoneNumber does not contain
        defaultSupplierSigmaFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllSupplierSigmasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            supplierSigmaRepository.saveAndFlush(supplierSigma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        supplierSigma.setTenant(tenant);
        supplierSigmaRepository.saveAndFlush(supplierSigma);
        Long tenantId = tenant.getId();
        // Get all the supplierSigmaList where tenant equals to tenantId
        defaultSupplierSigmaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the supplierSigmaList where tenant equals to (tenantId + 1)
        defaultSupplierSigmaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllSupplierSigmasByProductsIsEqualToSomething() throws Exception {
        ProductSigma products;
        if (TestUtil.findAll(em, ProductSigma.class).isEmpty()) {
            supplierSigmaRepository.saveAndFlush(supplierSigma);
            products = ProductSigmaResourceIT.createEntity();
        } else {
            products = TestUtil.findAll(em, ProductSigma.class).get(0);
        }
        em.persist(products);
        em.flush();
        supplierSigma.addProducts(products);
        supplierSigmaRepository.saveAndFlush(supplierSigma);
        Long productsId = products.getId();
        // Get all the supplierSigmaList where products equals to productsId
        defaultSupplierSigmaShouldBeFound("productsId.equals=" + productsId);

        // Get all the supplierSigmaList where products equals to (productsId + 1)
        defaultSupplierSigmaShouldNotBeFound("productsId.equals=" + (productsId + 1));
    }

    private void defaultSupplierSigmaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSupplierSigmaShouldBeFound(shouldBeFound);
        defaultSupplierSigmaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSupplierSigmaShouldBeFound(String filter) throws Exception {
        restSupplierSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplierSigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restSupplierSigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSupplierSigmaShouldNotBeFound(String filter) throws Exception {
        restSupplierSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSupplierSigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSupplierSigma() throws Exception {
        // Get the supplierSigma
        restSupplierSigmaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSupplierSigma() throws Exception {
        // Initialize the database
        insertedSupplierSigma = supplierSigmaRepository.saveAndFlush(supplierSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplierSigma
        SupplierSigma updatedSupplierSigma = supplierSigmaRepository.findById(supplierSigma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSupplierSigma are not directly saved in db
        em.detach(updatedSupplierSigma);
        updatedSupplierSigma
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restSupplierSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSupplierSigma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSupplierSigma))
            )
            .andExpect(status().isOk());

        // Validate the SupplierSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSupplierSigmaToMatchAllProperties(updatedSupplierSigma);
    }

    @Test
    @Transactional
    void putNonExistingSupplierSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierSigma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, supplierSigma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supplierSigma))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSupplierSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierSigma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supplierSigma))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSupplierSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierSigma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierSigmaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierSigma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SupplierSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSupplierSigmaWithPatch() throws Exception {
        // Initialize the database
        insertedSupplierSigma = supplierSigmaRepository.saveAndFlush(supplierSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplierSigma using partial update
        SupplierSigma partialUpdatedSupplierSigma = new SupplierSigma();
        partialUpdatedSupplierSigma.setId(supplierSigma.getId());

        partialUpdatedSupplierSigma
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restSupplierSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplierSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSupplierSigma))
            )
            .andExpect(status().isOk());

        // Validate the SupplierSigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSupplierSigmaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSupplierSigma, supplierSigma),
            getPersistedSupplierSigma(supplierSigma)
        );
    }

    @Test
    @Transactional
    void fullUpdateSupplierSigmaWithPatch() throws Exception {
        // Initialize the database
        insertedSupplierSigma = supplierSigmaRepository.saveAndFlush(supplierSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplierSigma using partial update
        SupplierSigma partialUpdatedSupplierSigma = new SupplierSigma();
        partialUpdatedSupplierSigma.setId(supplierSigma.getId());

        partialUpdatedSupplierSigma
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restSupplierSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplierSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSupplierSigma))
            )
            .andExpect(status().isOk());

        // Validate the SupplierSigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSupplierSigmaUpdatableFieldsEquals(partialUpdatedSupplierSigma, getPersistedSupplierSigma(partialUpdatedSupplierSigma));
    }

    @Test
    @Transactional
    void patchNonExistingSupplierSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierSigma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, supplierSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(supplierSigma))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSupplierSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierSigma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(supplierSigma))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSupplierSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierSigma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierSigmaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(supplierSigma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SupplierSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSupplierSigma() throws Exception {
        // Initialize the database
        insertedSupplierSigma = supplierSigmaRepository.saveAndFlush(supplierSigma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the supplierSigma
        restSupplierSigmaMockMvc
            .perform(delete(ENTITY_API_URL_ID, supplierSigma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return supplierSigmaRepository.count();
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

    protected SupplierSigma getPersistedSupplierSigma(SupplierSigma supplierSigma) {
        return supplierSigmaRepository.findById(supplierSigma.getId()).orElseThrow();
    }

    protected void assertPersistedSupplierSigmaToMatchAllProperties(SupplierSigma expectedSupplierSigma) {
        assertSupplierSigmaAllPropertiesEquals(expectedSupplierSigma, getPersistedSupplierSigma(expectedSupplierSigma));
    }

    protected void assertPersistedSupplierSigmaToMatchUpdatableProperties(SupplierSigma expectedSupplierSigma) {
        assertSupplierSigmaAllUpdatablePropertiesEquals(expectedSupplierSigma, getPersistedSupplierSigma(expectedSupplierSigma));
    }
}
