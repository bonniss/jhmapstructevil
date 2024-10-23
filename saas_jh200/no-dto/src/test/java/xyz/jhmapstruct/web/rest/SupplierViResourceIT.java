package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.SupplierViAsserts.*;
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
import xyz.jhmapstruct.domain.ProductVi;
import xyz.jhmapstruct.domain.SupplierVi;
import xyz.jhmapstruct.repository.SupplierViRepository;
import xyz.jhmapstruct.service.SupplierViService;

/**
 * Integration tests for the {@link SupplierViResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SupplierViResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_PERSON = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_PERSON = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/supplier-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SupplierViRepository supplierViRepository;

    @Mock
    private SupplierViRepository supplierViRepositoryMock;

    @Mock
    private SupplierViService supplierViServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSupplierViMockMvc;

    private SupplierVi supplierVi;

    private SupplierVi insertedSupplierVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SupplierVi createEntity() {
        return new SupplierVi()
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
    public static SupplierVi createUpdatedEntity() {
        return new SupplierVi()
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        supplierVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSupplierVi != null) {
            supplierViRepository.delete(insertedSupplierVi);
            insertedSupplierVi = null;
        }
    }

    @Test
    @Transactional
    void createSupplierVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SupplierVi
        var returnedSupplierVi = om.readValue(
            restSupplierViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SupplierVi.class
        );

        // Validate the SupplierVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSupplierViUpdatableFieldsEquals(returnedSupplierVi, getPersistedSupplierVi(returnedSupplierVi));

        insertedSupplierVi = returnedSupplierVi;
    }

    @Test
    @Transactional
    void createSupplierViWithExistingId() throws Exception {
        // Create the SupplierVi with an existing ID
        supplierVi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSupplierViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierVi)))
            .andExpect(status().isBadRequest());

        // Validate the SupplierVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        supplierVi.setName(null);

        // Create the SupplierVi, which fails.

        restSupplierViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSupplierVis() throws Exception {
        // Initialize the database
        insertedSupplierVi = supplierViRepository.saveAndFlush(supplierVi);

        // Get all the supplierViList
        restSupplierViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplierVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSupplierVisWithEagerRelationshipsIsEnabled() throws Exception {
        when(supplierViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSupplierViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(supplierViServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSupplierVisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(supplierViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSupplierViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(supplierViRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSupplierVi() throws Exception {
        // Initialize the database
        insertedSupplierVi = supplierViRepository.saveAndFlush(supplierVi);

        // Get the supplierVi
        restSupplierViMockMvc
            .perform(get(ENTITY_API_URL_ID, supplierVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(supplierVi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.contactPerson").value(DEFAULT_CONTACT_PERSON))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getSupplierVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedSupplierVi = supplierViRepository.saveAndFlush(supplierVi);

        Long id = supplierVi.getId();

        defaultSupplierViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSupplierViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSupplierViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSupplierVisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierVi = supplierViRepository.saveAndFlush(supplierVi);

        // Get all the supplierViList where name equals to
        defaultSupplierViFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSupplierVisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierVi = supplierViRepository.saveAndFlush(supplierVi);

        // Get all the supplierViList where name in
        defaultSupplierViFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSupplierVisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierVi = supplierViRepository.saveAndFlush(supplierVi);

        // Get all the supplierViList where name is not null
        defaultSupplierViFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierVisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierVi = supplierViRepository.saveAndFlush(supplierVi);

        // Get all the supplierViList where name contains
        defaultSupplierViFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSupplierVisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierVi = supplierViRepository.saveAndFlush(supplierVi);

        // Get all the supplierViList where name does not contain
        defaultSupplierViFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllSupplierVisByContactPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierVi = supplierViRepository.saveAndFlush(supplierVi);

        // Get all the supplierViList where contactPerson equals to
        defaultSupplierViFiltering("contactPerson.equals=" + DEFAULT_CONTACT_PERSON, "contactPerson.equals=" + UPDATED_CONTACT_PERSON);
    }

    @Test
    @Transactional
    void getAllSupplierVisByContactPersonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierVi = supplierViRepository.saveAndFlush(supplierVi);

        // Get all the supplierViList where contactPerson in
        defaultSupplierViFiltering(
            "contactPerson.in=" + DEFAULT_CONTACT_PERSON + "," + UPDATED_CONTACT_PERSON,
            "contactPerson.in=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllSupplierVisByContactPersonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierVi = supplierViRepository.saveAndFlush(supplierVi);

        // Get all the supplierViList where contactPerson is not null
        defaultSupplierViFiltering("contactPerson.specified=true", "contactPerson.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierVisByContactPersonContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierVi = supplierViRepository.saveAndFlush(supplierVi);

        // Get all the supplierViList where contactPerson contains
        defaultSupplierViFiltering("contactPerson.contains=" + DEFAULT_CONTACT_PERSON, "contactPerson.contains=" + UPDATED_CONTACT_PERSON);
    }

    @Test
    @Transactional
    void getAllSupplierVisByContactPersonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierVi = supplierViRepository.saveAndFlush(supplierVi);

        // Get all the supplierViList where contactPerson does not contain
        defaultSupplierViFiltering(
            "contactPerson.doesNotContain=" + UPDATED_CONTACT_PERSON,
            "contactPerson.doesNotContain=" + DEFAULT_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllSupplierVisByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierVi = supplierViRepository.saveAndFlush(supplierVi);

        // Get all the supplierViList where email equals to
        defaultSupplierViFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSupplierVisByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierVi = supplierViRepository.saveAndFlush(supplierVi);

        // Get all the supplierViList where email in
        defaultSupplierViFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSupplierVisByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierVi = supplierViRepository.saveAndFlush(supplierVi);

        // Get all the supplierViList where email is not null
        defaultSupplierViFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierVisByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierVi = supplierViRepository.saveAndFlush(supplierVi);

        // Get all the supplierViList where email contains
        defaultSupplierViFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSupplierVisByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierVi = supplierViRepository.saveAndFlush(supplierVi);

        // Get all the supplierViList where email does not contain
        defaultSupplierViFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllSupplierVisByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierVi = supplierViRepository.saveAndFlush(supplierVi);

        // Get all the supplierViList where phoneNumber equals to
        defaultSupplierViFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllSupplierVisByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierVi = supplierViRepository.saveAndFlush(supplierVi);

        // Get all the supplierViList where phoneNumber in
        defaultSupplierViFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllSupplierVisByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierVi = supplierViRepository.saveAndFlush(supplierVi);

        // Get all the supplierViList where phoneNumber is not null
        defaultSupplierViFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierVisByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierVi = supplierViRepository.saveAndFlush(supplierVi);

        // Get all the supplierViList where phoneNumber contains
        defaultSupplierViFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllSupplierVisByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierVi = supplierViRepository.saveAndFlush(supplierVi);

        // Get all the supplierViList where phoneNumber does not contain
        defaultSupplierViFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllSupplierVisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            supplierViRepository.saveAndFlush(supplierVi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        supplierVi.setTenant(tenant);
        supplierViRepository.saveAndFlush(supplierVi);
        Long tenantId = tenant.getId();
        // Get all the supplierViList where tenant equals to tenantId
        defaultSupplierViShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the supplierViList where tenant equals to (tenantId + 1)
        defaultSupplierViShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllSupplierVisByProductsIsEqualToSomething() throws Exception {
        ProductVi products;
        if (TestUtil.findAll(em, ProductVi.class).isEmpty()) {
            supplierViRepository.saveAndFlush(supplierVi);
            products = ProductViResourceIT.createEntity();
        } else {
            products = TestUtil.findAll(em, ProductVi.class).get(0);
        }
        em.persist(products);
        em.flush();
        supplierVi.addProducts(products);
        supplierViRepository.saveAndFlush(supplierVi);
        Long productsId = products.getId();
        // Get all the supplierViList where products equals to productsId
        defaultSupplierViShouldBeFound("productsId.equals=" + productsId);

        // Get all the supplierViList where products equals to (productsId + 1)
        defaultSupplierViShouldNotBeFound("productsId.equals=" + (productsId + 1));
    }

    private void defaultSupplierViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSupplierViShouldBeFound(shouldBeFound);
        defaultSupplierViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSupplierViShouldBeFound(String filter) throws Exception {
        restSupplierViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplierVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restSupplierViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSupplierViShouldNotBeFound(String filter) throws Exception {
        restSupplierViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSupplierViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSupplierVi() throws Exception {
        // Get the supplierVi
        restSupplierViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSupplierVi() throws Exception {
        // Initialize the database
        insertedSupplierVi = supplierViRepository.saveAndFlush(supplierVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplierVi
        SupplierVi updatedSupplierVi = supplierViRepository.findById(supplierVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSupplierVi are not directly saved in db
        em.detach(updatedSupplierVi);
        updatedSupplierVi.name(UPDATED_NAME).contactPerson(UPDATED_CONTACT_PERSON).email(UPDATED_EMAIL).phoneNumber(UPDATED_PHONE_NUMBER);

        restSupplierViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSupplierVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSupplierVi))
            )
            .andExpect(status().isOk());

        // Validate the SupplierVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSupplierViToMatchAllProperties(updatedSupplierVi);
    }

    @Test
    @Transactional
    void putNonExistingSupplierVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, supplierVi.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSupplierVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supplierVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSupplierVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SupplierVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSupplierViWithPatch() throws Exception {
        // Initialize the database
        insertedSupplierVi = supplierViRepository.saveAndFlush(supplierVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplierVi using partial update
        SupplierVi partialUpdatedSupplierVi = new SupplierVi();
        partialUpdatedSupplierVi.setId(supplierVi.getId());

        partialUpdatedSupplierVi.name(UPDATED_NAME).contactPerson(UPDATED_CONTACT_PERSON).phoneNumber(UPDATED_PHONE_NUMBER);

        restSupplierViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplierVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSupplierVi))
            )
            .andExpect(status().isOk());

        // Validate the SupplierVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSupplierViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSupplierVi, supplierVi),
            getPersistedSupplierVi(supplierVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateSupplierViWithPatch() throws Exception {
        // Initialize the database
        insertedSupplierVi = supplierViRepository.saveAndFlush(supplierVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplierVi using partial update
        SupplierVi partialUpdatedSupplierVi = new SupplierVi();
        partialUpdatedSupplierVi.setId(supplierVi.getId());

        partialUpdatedSupplierVi
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restSupplierViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplierVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSupplierVi))
            )
            .andExpect(status().isOk());

        // Validate the SupplierVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSupplierViUpdatableFieldsEquals(partialUpdatedSupplierVi, getPersistedSupplierVi(partialUpdatedSupplierVi));
    }

    @Test
    @Transactional
    void patchNonExistingSupplierVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, supplierVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(supplierVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSupplierVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(supplierVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSupplierVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(supplierVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SupplierVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSupplierVi() throws Exception {
        // Initialize the database
        insertedSupplierVi = supplierViRepository.saveAndFlush(supplierVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the supplierVi
        restSupplierViMockMvc
            .perform(delete(ENTITY_API_URL_ID, supplierVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return supplierViRepository.count();
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

    protected SupplierVi getPersistedSupplierVi(SupplierVi supplierVi) {
        return supplierViRepository.findById(supplierVi.getId()).orElseThrow();
    }

    protected void assertPersistedSupplierViToMatchAllProperties(SupplierVi expectedSupplierVi) {
        assertSupplierViAllPropertiesEquals(expectedSupplierVi, getPersistedSupplierVi(expectedSupplierVi));
    }

    protected void assertPersistedSupplierViToMatchUpdatableProperties(SupplierVi expectedSupplierVi) {
        assertSupplierViAllUpdatablePropertiesEquals(expectedSupplierVi, getPersistedSupplierVi(expectedSupplierVi));
    }
}
