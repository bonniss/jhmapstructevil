package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.SupplierGammaAsserts.*;
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
import xyz.jhmapstruct.domain.ProductGamma;
import xyz.jhmapstruct.domain.SupplierGamma;
import xyz.jhmapstruct.repository.SupplierGammaRepository;
import xyz.jhmapstruct.service.SupplierGammaService;
import xyz.jhmapstruct.service.dto.SupplierGammaDTO;
import xyz.jhmapstruct.service.mapper.SupplierGammaMapper;

/**
 * Integration tests for the {@link SupplierGammaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SupplierGammaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_PERSON = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_PERSON = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/supplier-gammas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SupplierGammaRepository supplierGammaRepository;

    @Mock
    private SupplierGammaRepository supplierGammaRepositoryMock;

    @Autowired
    private SupplierGammaMapper supplierGammaMapper;

    @Mock
    private SupplierGammaService supplierGammaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSupplierGammaMockMvc;

    private SupplierGamma supplierGamma;

    private SupplierGamma insertedSupplierGamma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SupplierGamma createEntity() {
        return new SupplierGamma()
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
    public static SupplierGamma createUpdatedEntity() {
        return new SupplierGamma()
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        supplierGamma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSupplierGamma != null) {
            supplierGammaRepository.delete(insertedSupplierGamma);
            insertedSupplierGamma = null;
        }
    }

    @Test
    @Transactional
    void createSupplierGamma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SupplierGamma
        SupplierGammaDTO supplierGammaDTO = supplierGammaMapper.toDto(supplierGamma);
        var returnedSupplierGammaDTO = om.readValue(
            restSupplierGammaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierGammaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SupplierGammaDTO.class
        );

        // Validate the SupplierGamma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSupplierGamma = supplierGammaMapper.toEntity(returnedSupplierGammaDTO);
        assertSupplierGammaUpdatableFieldsEquals(returnedSupplierGamma, getPersistedSupplierGamma(returnedSupplierGamma));

        insertedSupplierGamma = returnedSupplierGamma;
    }

    @Test
    @Transactional
    void createSupplierGammaWithExistingId() throws Exception {
        // Create the SupplierGamma with an existing ID
        supplierGamma.setId(1L);
        SupplierGammaDTO supplierGammaDTO = supplierGammaMapper.toDto(supplierGamma);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSupplierGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierGammaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SupplierGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        supplierGamma.setName(null);

        // Create the SupplierGamma, which fails.
        SupplierGammaDTO supplierGammaDTO = supplierGammaMapper.toDto(supplierGamma);

        restSupplierGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierGammaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSupplierGammas() throws Exception {
        // Initialize the database
        insertedSupplierGamma = supplierGammaRepository.saveAndFlush(supplierGamma);

        // Get all the supplierGammaList
        restSupplierGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplierGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSupplierGammasWithEagerRelationshipsIsEnabled() throws Exception {
        when(supplierGammaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSupplierGammaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(supplierGammaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSupplierGammasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(supplierGammaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSupplierGammaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(supplierGammaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSupplierGamma() throws Exception {
        // Initialize the database
        insertedSupplierGamma = supplierGammaRepository.saveAndFlush(supplierGamma);

        // Get the supplierGamma
        restSupplierGammaMockMvc
            .perform(get(ENTITY_API_URL_ID, supplierGamma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(supplierGamma.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.contactPerson").value(DEFAULT_CONTACT_PERSON))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getSupplierGammasByIdFiltering() throws Exception {
        // Initialize the database
        insertedSupplierGamma = supplierGammaRepository.saveAndFlush(supplierGamma);

        Long id = supplierGamma.getId();

        defaultSupplierGammaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSupplierGammaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSupplierGammaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSupplierGammasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierGamma = supplierGammaRepository.saveAndFlush(supplierGamma);

        // Get all the supplierGammaList where name equals to
        defaultSupplierGammaFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSupplierGammasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierGamma = supplierGammaRepository.saveAndFlush(supplierGamma);

        // Get all the supplierGammaList where name in
        defaultSupplierGammaFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSupplierGammasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierGamma = supplierGammaRepository.saveAndFlush(supplierGamma);

        // Get all the supplierGammaList where name is not null
        defaultSupplierGammaFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierGammasByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierGamma = supplierGammaRepository.saveAndFlush(supplierGamma);

        // Get all the supplierGammaList where name contains
        defaultSupplierGammaFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSupplierGammasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierGamma = supplierGammaRepository.saveAndFlush(supplierGamma);

        // Get all the supplierGammaList where name does not contain
        defaultSupplierGammaFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllSupplierGammasByContactPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierGamma = supplierGammaRepository.saveAndFlush(supplierGamma);

        // Get all the supplierGammaList where contactPerson equals to
        defaultSupplierGammaFiltering("contactPerson.equals=" + DEFAULT_CONTACT_PERSON, "contactPerson.equals=" + UPDATED_CONTACT_PERSON);
    }

    @Test
    @Transactional
    void getAllSupplierGammasByContactPersonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierGamma = supplierGammaRepository.saveAndFlush(supplierGamma);

        // Get all the supplierGammaList where contactPerson in
        defaultSupplierGammaFiltering(
            "contactPerson.in=" + DEFAULT_CONTACT_PERSON + "," + UPDATED_CONTACT_PERSON,
            "contactPerson.in=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllSupplierGammasByContactPersonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierGamma = supplierGammaRepository.saveAndFlush(supplierGamma);

        // Get all the supplierGammaList where contactPerson is not null
        defaultSupplierGammaFiltering("contactPerson.specified=true", "contactPerson.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierGammasByContactPersonContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierGamma = supplierGammaRepository.saveAndFlush(supplierGamma);

        // Get all the supplierGammaList where contactPerson contains
        defaultSupplierGammaFiltering(
            "contactPerson.contains=" + DEFAULT_CONTACT_PERSON,
            "contactPerson.contains=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllSupplierGammasByContactPersonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierGamma = supplierGammaRepository.saveAndFlush(supplierGamma);

        // Get all the supplierGammaList where contactPerson does not contain
        defaultSupplierGammaFiltering(
            "contactPerson.doesNotContain=" + UPDATED_CONTACT_PERSON,
            "contactPerson.doesNotContain=" + DEFAULT_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllSupplierGammasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierGamma = supplierGammaRepository.saveAndFlush(supplierGamma);

        // Get all the supplierGammaList where email equals to
        defaultSupplierGammaFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSupplierGammasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierGamma = supplierGammaRepository.saveAndFlush(supplierGamma);

        // Get all the supplierGammaList where email in
        defaultSupplierGammaFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSupplierGammasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierGamma = supplierGammaRepository.saveAndFlush(supplierGamma);

        // Get all the supplierGammaList where email is not null
        defaultSupplierGammaFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierGammasByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierGamma = supplierGammaRepository.saveAndFlush(supplierGamma);

        // Get all the supplierGammaList where email contains
        defaultSupplierGammaFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSupplierGammasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierGamma = supplierGammaRepository.saveAndFlush(supplierGamma);

        // Get all the supplierGammaList where email does not contain
        defaultSupplierGammaFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllSupplierGammasByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierGamma = supplierGammaRepository.saveAndFlush(supplierGamma);

        // Get all the supplierGammaList where phoneNumber equals to
        defaultSupplierGammaFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllSupplierGammasByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierGamma = supplierGammaRepository.saveAndFlush(supplierGamma);

        // Get all the supplierGammaList where phoneNumber in
        defaultSupplierGammaFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllSupplierGammasByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierGamma = supplierGammaRepository.saveAndFlush(supplierGamma);

        // Get all the supplierGammaList where phoneNumber is not null
        defaultSupplierGammaFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierGammasByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierGamma = supplierGammaRepository.saveAndFlush(supplierGamma);

        // Get all the supplierGammaList where phoneNumber contains
        defaultSupplierGammaFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllSupplierGammasByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierGamma = supplierGammaRepository.saveAndFlush(supplierGamma);

        // Get all the supplierGammaList where phoneNumber does not contain
        defaultSupplierGammaFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllSupplierGammasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            supplierGammaRepository.saveAndFlush(supplierGamma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        supplierGamma.setTenant(tenant);
        supplierGammaRepository.saveAndFlush(supplierGamma);
        Long tenantId = tenant.getId();
        // Get all the supplierGammaList where tenant equals to tenantId
        defaultSupplierGammaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the supplierGammaList where tenant equals to (tenantId + 1)
        defaultSupplierGammaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllSupplierGammasByProductsIsEqualToSomething() throws Exception {
        ProductGamma products;
        if (TestUtil.findAll(em, ProductGamma.class).isEmpty()) {
            supplierGammaRepository.saveAndFlush(supplierGamma);
            products = ProductGammaResourceIT.createEntity();
        } else {
            products = TestUtil.findAll(em, ProductGamma.class).get(0);
        }
        em.persist(products);
        em.flush();
        supplierGamma.addProducts(products);
        supplierGammaRepository.saveAndFlush(supplierGamma);
        Long productsId = products.getId();
        // Get all the supplierGammaList where products equals to productsId
        defaultSupplierGammaShouldBeFound("productsId.equals=" + productsId);

        // Get all the supplierGammaList where products equals to (productsId + 1)
        defaultSupplierGammaShouldNotBeFound("productsId.equals=" + (productsId + 1));
    }

    private void defaultSupplierGammaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSupplierGammaShouldBeFound(shouldBeFound);
        defaultSupplierGammaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSupplierGammaShouldBeFound(String filter) throws Exception {
        restSupplierGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplierGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restSupplierGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSupplierGammaShouldNotBeFound(String filter) throws Exception {
        restSupplierGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSupplierGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSupplierGamma() throws Exception {
        // Get the supplierGamma
        restSupplierGammaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSupplierGamma() throws Exception {
        // Initialize the database
        insertedSupplierGamma = supplierGammaRepository.saveAndFlush(supplierGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplierGamma
        SupplierGamma updatedSupplierGamma = supplierGammaRepository.findById(supplierGamma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSupplierGamma are not directly saved in db
        em.detach(updatedSupplierGamma);
        updatedSupplierGamma
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        SupplierGammaDTO supplierGammaDTO = supplierGammaMapper.toDto(updatedSupplierGamma);

        restSupplierGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, supplierGammaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supplierGammaDTO))
            )
            .andExpect(status().isOk());

        // Validate the SupplierGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSupplierGammaToMatchAllProperties(updatedSupplierGamma);
    }

    @Test
    @Transactional
    void putNonExistingSupplierGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierGamma.setId(longCount.incrementAndGet());

        // Create the SupplierGamma
        SupplierGammaDTO supplierGammaDTO = supplierGammaMapper.toDto(supplierGamma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, supplierGammaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supplierGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSupplierGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierGamma.setId(longCount.incrementAndGet());

        // Create the SupplierGamma
        SupplierGammaDTO supplierGammaDTO = supplierGammaMapper.toDto(supplierGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supplierGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSupplierGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierGamma.setId(longCount.incrementAndGet());

        // Create the SupplierGamma
        SupplierGammaDTO supplierGammaDTO = supplierGammaMapper.toDto(supplierGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierGammaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierGammaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SupplierGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSupplierGammaWithPatch() throws Exception {
        // Initialize the database
        insertedSupplierGamma = supplierGammaRepository.saveAndFlush(supplierGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplierGamma using partial update
        SupplierGamma partialUpdatedSupplierGamma = new SupplierGamma();
        partialUpdatedSupplierGamma.setId(supplierGamma.getId());

        restSupplierGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplierGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSupplierGamma))
            )
            .andExpect(status().isOk());

        // Validate the SupplierGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSupplierGammaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSupplierGamma, supplierGamma),
            getPersistedSupplierGamma(supplierGamma)
        );
    }

    @Test
    @Transactional
    void fullUpdateSupplierGammaWithPatch() throws Exception {
        // Initialize the database
        insertedSupplierGamma = supplierGammaRepository.saveAndFlush(supplierGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplierGamma using partial update
        SupplierGamma partialUpdatedSupplierGamma = new SupplierGamma();
        partialUpdatedSupplierGamma.setId(supplierGamma.getId());

        partialUpdatedSupplierGamma
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restSupplierGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplierGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSupplierGamma))
            )
            .andExpect(status().isOk());

        // Validate the SupplierGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSupplierGammaUpdatableFieldsEquals(partialUpdatedSupplierGamma, getPersistedSupplierGamma(partialUpdatedSupplierGamma));
    }

    @Test
    @Transactional
    void patchNonExistingSupplierGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierGamma.setId(longCount.incrementAndGet());

        // Create the SupplierGamma
        SupplierGammaDTO supplierGammaDTO = supplierGammaMapper.toDto(supplierGamma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, supplierGammaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(supplierGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSupplierGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierGamma.setId(longCount.incrementAndGet());

        // Create the SupplierGamma
        SupplierGammaDTO supplierGammaDTO = supplierGammaMapper.toDto(supplierGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(supplierGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSupplierGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierGamma.setId(longCount.incrementAndGet());

        // Create the SupplierGamma
        SupplierGammaDTO supplierGammaDTO = supplierGammaMapper.toDto(supplierGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierGammaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(supplierGammaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SupplierGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSupplierGamma() throws Exception {
        // Initialize the database
        insertedSupplierGamma = supplierGammaRepository.saveAndFlush(supplierGamma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the supplierGamma
        restSupplierGammaMockMvc
            .perform(delete(ENTITY_API_URL_ID, supplierGamma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return supplierGammaRepository.count();
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

    protected SupplierGamma getPersistedSupplierGamma(SupplierGamma supplierGamma) {
        return supplierGammaRepository.findById(supplierGamma.getId()).orElseThrow();
    }

    protected void assertPersistedSupplierGammaToMatchAllProperties(SupplierGamma expectedSupplierGamma) {
        assertSupplierGammaAllPropertiesEquals(expectedSupplierGamma, getPersistedSupplierGamma(expectedSupplierGamma));
    }

    protected void assertPersistedSupplierGammaToMatchUpdatableProperties(SupplierGamma expectedSupplierGamma) {
        assertSupplierGammaAllUpdatablePropertiesEquals(expectedSupplierGamma, getPersistedSupplierGamma(expectedSupplierGamma));
    }
}
