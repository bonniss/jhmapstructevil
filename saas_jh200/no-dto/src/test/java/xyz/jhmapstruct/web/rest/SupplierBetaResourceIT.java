package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.SupplierBetaAsserts.*;
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
import xyz.jhmapstruct.domain.ProductBeta;
import xyz.jhmapstruct.domain.SupplierBeta;
import xyz.jhmapstruct.repository.SupplierBetaRepository;
import xyz.jhmapstruct.service.SupplierBetaService;

/**
 * Integration tests for the {@link SupplierBetaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SupplierBetaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_PERSON = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_PERSON = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/supplier-betas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SupplierBetaRepository supplierBetaRepository;

    @Mock
    private SupplierBetaRepository supplierBetaRepositoryMock;

    @Mock
    private SupplierBetaService supplierBetaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSupplierBetaMockMvc;

    private SupplierBeta supplierBeta;

    private SupplierBeta insertedSupplierBeta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SupplierBeta createEntity() {
        return new SupplierBeta()
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
    public static SupplierBeta createUpdatedEntity() {
        return new SupplierBeta()
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        supplierBeta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSupplierBeta != null) {
            supplierBetaRepository.delete(insertedSupplierBeta);
            insertedSupplierBeta = null;
        }
    }

    @Test
    @Transactional
    void createSupplierBeta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SupplierBeta
        var returnedSupplierBeta = om.readValue(
            restSupplierBetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierBeta)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SupplierBeta.class
        );

        // Validate the SupplierBeta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSupplierBetaUpdatableFieldsEquals(returnedSupplierBeta, getPersistedSupplierBeta(returnedSupplierBeta));

        insertedSupplierBeta = returnedSupplierBeta;
    }

    @Test
    @Transactional
    void createSupplierBetaWithExistingId() throws Exception {
        // Create the SupplierBeta with an existing ID
        supplierBeta.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSupplierBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierBeta)))
            .andExpect(status().isBadRequest());

        // Validate the SupplierBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        supplierBeta.setName(null);

        // Create the SupplierBeta, which fails.

        restSupplierBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierBeta)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSupplierBetas() throws Exception {
        // Initialize the database
        insertedSupplierBeta = supplierBetaRepository.saveAndFlush(supplierBeta);

        // Get all the supplierBetaList
        restSupplierBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplierBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSupplierBetasWithEagerRelationshipsIsEnabled() throws Exception {
        when(supplierBetaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSupplierBetaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(supplierBetaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSupplierBetasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(supplierBetaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSupplierBetaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(supplierBetaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSupplierBeta() throws Exception {
        // Initialize the database
        insertedSupplierBeta = supplierBetaRepository.saveAndFlush(supplierBeta);

        // Get the supplierBeta
        restSupplierBetaMockMvc
            .perform(get(ENTITY_API_URL_ID, supplierBeta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(supplierBeta.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.contactPerson").value(DEFAULT_CONTACT_PERSON))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getSupplierBetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedSupplierBeta = supplierBetaRepository.saveAndFlush(supplierBeta);

        Long id = supplierBeta.getId();

        defaultSupplierBetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSupplierBetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSupplierBetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSupplierBetasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierBeta = supplierBetaRepository.saveAndFlush(supplierBeta);

        // Get all the supplierBetaList where name equals to
        defaultSupplierBetaFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSupplierBetasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierBeta = supplierBetaRepository.saveAndFlush(supplierBeta);

        // Get all the supplierBetaList where name in
        defaultSupplierBetaFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSupplierBetasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierBeta = supplierBetaRepository.saveAndFlush(supplierBeta);

        // Get all the supplierBetaList where name is not null
        defaultSupplierBetaFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierBetasByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierBeta = supplierBetaRepository.saveAndFlush(supplierBeta);

        // Get all the supplierBetaList where name contains
        defaultSupplierBetaFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSupplierBetasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierBeta = supplierBetaRepository.saveAndFlush(supplierBeta);

        // Get all the supplierBetaList where name does not contain
        defaultSupplierBetaFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllSupplierBetasByContactPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierBeta = supplierBetaRepository.saveAndFlush(supplierBeta);

        // Get all the supplierBetaList where contactPerson equals to
        defaultSupplierBetaFiltering("contactPerson.equals=" + DEFAULT_CONTACT_PERSON, "contactPerson.equals=" + UPDATED_CONTACT_PERSON);
    }

    @Test
    @Transactional
    void getAllSupplierBetasByContactPersonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierBeta = supplierBetaRepository.saveAndFlush(supplierBeta);

        // Get all the supplierBetaList where contactPerson in
        defaultSupplierBetaFiltering(
            "contactPerson.in=" + DEFAULT_CONTACT_PERSON + "," + UPDATED_CONTACT_PERSON,
            "contactPerson.in=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllSupplierBetasByContactPersonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierBeta = supplierBetaRepository.saveAndFlush(supplierBeta);

        // Get all the supplierBetaList where contactPerson is not null
        defaultSupplierBetaFiltering("contactPerson.specified=true", "contactPerson.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierBetasByContactPersonContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierBeta = supplierBetaRepository.saveAndFlush(supplierBeta);

        // Get all the supplierBetaList where contactPerson contains
        defaultSupplierBetaFiltering(
            "contactPerson.contains=" + DEFAULT_CONTACT_PERSON,
            "contactPerson.contains=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllSupplierBetasByContactPersonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierBeta = supplierBetaRepository.saveAndFlush(supplierBeta);

        // Get all the supplierBetaList where contactPerson does not contain
        defaultSupplierBetaFiltering(
            "contactPerson.doesNotContain=" + UPDATED_CONTACT_PERSON,
            "contactPerson.doesNotContain=" + DEFAULT_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllSupplierBetasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierBeta = supplierBetaRepository.saveAndFlush(supplierBeta);

        // Get all the supplierBetaList where email equals to
        defaultSupplierBetaFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSupplierBetasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierBeta = supplierBetaRepository.saveAndFlush(supplierBeta);

        // Get all the supplierBetaList where email in
        defaultSupplierBetaFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSupplierBetasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierBeta = supplierBetaRepository.saveAndFlush(supplierBeta);

        // Get all the supplierBetaList where email is not null
        defaultSupplierBetaFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierBetasByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierBeta = supplierBetaRepository.saveAndFlush(supplierBeta);

        // Get all the supplierBetaList where email contains
        defaultSupplierBetaFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSupplierBetasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierBeta = supplierBetaRepository.saveAndFlush(supplierBeta);

        // Get all the supplierBetaList where email does not contain
        defaultSupplierBetaFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllSupplierBetasByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierBeta = supplierBetaRepository.saveAndFlush(supplierBeta);

        // Get all the supplierBetaList where phoneNumber equals to
        defaultSupplierBetaFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllSupplierBetasByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierBeta = supplierBetaRepository.saveAndFlush(supplierBeta);

        // Get all the supplierBetaList where phoneNumber in
        defaultSupplierBetaFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllSupplierBetasByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierBeta = supplierBetaRepository.saveAndFlush(supplierBeta);

        // Get all the supplierBetaList where phoneNumber is not null
        defaultSupplierBetaFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierBetasByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierBeta = supplierBetaRepository.saveAndFlush(supplierBeta);

        // Get all the supplierBetaList where phoneNumber contains
        defaultSupplierBetaFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllSupplierBetasByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierBeta = supplierBetaRepository.saveAndFlush(supplierBeta);

        // Get all the supplierBetaList where phoneNumber does not contain
        defaultSupplierBetaFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllSupplierBetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            supplierBetaRepository.saveAndFlush(supplierBeta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        supplierBeta.setTenant(tenant);
        supplierBetaRepository.saveAndFlush(supplierBeta);
        Long tenantId = tenant.getId();
        // Get all the supplierBetaList where tenant equals to tenantId
        defaultSupplierBetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the supplierBetaList where tenant equals to (tenantId + 1)
        defaultSupplierBetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllSupplierBetasByProductsIsEqualToSomething() throws Exception {
        ProductBeta products;
        if (TestUtil.findAll(em, ProductBeta.class).isEmpty()) {
            supplierBetaRepository.saveAndFlush(supplierBeta);
            products = ProductBetaResourceIT.createEntity();
        } else {
            products = TestUtil.findAll(em, ProductBeta.class).get(0);
        }
        em.persist(products);
        em.flush();
        supplierBeta.addProducts(products);
        supplierBetaRepository.saveAndFlush(supplierBeta);
        Long productsId = products.getId();
        // Get all the supplierBetaList where products equals to productsId
        defaultSupplierBetaShouldBeFound("productsId.equals=" + productsId);

        // Get all the supplierBetaList where products equals to (productsId + 1)
        defaultSupplierBetaShouldNotBeFound("productsId.equals=" + (productsId + 1));
    }

    private void defaultSupplierBetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSupplierBetaShouldBeFound(shouldBeFound);
        defaultSupplierBetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSupplierBetaShouldBeFound(String filter) throws Exception {
        restSupplierBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplierBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restSupplierBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSupplierBetaShouldNotBeFound(String filter) throws Exception {
        restSupplierBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSupplierBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSupplierBeta() throws Exception {
        // Get the supplierBeta
        restSupplierBetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSupplierBeta() throws Exception {
        // Initialize the database
        insertedSupplierBeta = supplierBetaRepository.saveAndFlush(supplierBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplierBeta
        SupplierBeta updatedSupplierBeta = supplierBetaRepository.findById(supplierBeta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSupplierBeta are not directly saved in db
        em.detach(updatedSupplierBeta);
        updatedSupplierBeta.name(UPDATED_NAME).contactPerson(UPDATED_CONTACT_PERSON).email(UPDATED_EMAIL).phoneNumber(UPDATED_PHONE_NUMBER);

        restSupplierBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSupplierBeta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSupplierBeta))
            )
            .andExpect(status().isOk());

        // Validate the SupplierBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSupplierBetaToMatchAllProperties(updatedSupplierBeta);
    }

    @Test
    @Transactional
    void putNonExistingSupplierBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierBeta.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, supplierBeta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supplierBeta))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSupplierBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierBeta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supplierBeta))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSupplierBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierBeta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierBetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierBeta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SupplierBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSupplierBetaWithPatch() throws Exception {
        // Initialize the database
        insertedSupplierBeta = supplierBetaRepository.saveAndFlush(supplierBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplierBeta using partial update
        SupplierBeta partialUpdatedSupplierBeta = new SupplierBeta();
        partialUpdatedSupplierBeta.setId(supplierBeta.getId());

        partialUpdatedSupplierBeta.name(UPDATED_NAME);

        restSupplierBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplierBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSupplierBeta))
            )
            .andExpect(status().isOk());

        // Validate the SupplierBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSupplierBetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSupplierBeta, supplierBeta),
            getPersistedSupplierBeta(supplierBeta)
        );
    }

    @Test
    @Transactional
    void fullUpdateSupplierBetaWithPatch() throws Exception {
        // Initialize the database
        insertedSupplierBeta = supplierBetaRepository.saveAndFlush(supplierBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplierBeta using partial update
        SupplierBeta partialUpdatedSupplierBeta = new SupplierBeta();
        partialUpdatedSupplierBeta.setId(supplierBeta.getId());

        partialUpdatedSupplierBeta
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restSupplierBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplierBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSupplierBeta))
            )
            .andExpect(status().isOk());

        // Validate the SupplierBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSupplierBetaUpdatableFieldsEquals(partialUpdatedSupplierBeta, getPersistedSupplierBeta(partialUpdatedSupplierBeta));
    }

    @Test
    @Transactional
    void patchNonExistingSupplierBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierBeta.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, supplierBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(supplierBeta))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSupplierBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierBeta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(supplierBeta))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSupplierBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierBeta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierBetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(supplierBeta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SupplierBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSupplierBeta() throws Exception {
        // Initialize the database
        insertedSupplierBeta = supplierBetaRepository.saveAndFlush(supplierBeta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the supplierBeta
        restSupplierBetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, supplierBeta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return supplierBetaRepository.count();
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

    protected SupplierBeta getPersistedSupplierBeta(SupplierBeta supplierBeta) {
        return supplierBetaRepository.findById(supplierBeta.getId()).orElseThrow();
    }

    protected void assertPersistedSupplierBetaToMatchAllProperties(SupplierBeta expectedSupplierBeta) {
        assertSupplierBetaAllPropertiesEquals(expectedSupplierBeta, getPersistedSupplierBeta(expectedSupplierBeta));
    }

    protected void assertPersistedSupplierBetaToMatchUpdatableProperties(SupplierBeta expectedSupplierBeta) {
        assertSupplierBetaAllUpdatablePropertiesEquals(expectedSupplierBeta, getPersistedSupplierBeta(expectedSupplierBeta));
    }
}
