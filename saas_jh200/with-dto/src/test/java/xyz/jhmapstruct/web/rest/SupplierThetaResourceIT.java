package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.SupplierThetaAsserts.*;
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
import xyz.jhmapstruct.domain.ProductTheta;
import xyz.jhmapstruct.domain.SupplierTheta;
import xyz.jhmapstruct.repository.SupplierThetaRepository;
import xyz.jhmapstruct.service.SupplierThetaService;
import xyz.jhmapstruct.service.dto.SupplierThetaDTO;
import xyz.jhmapstruct.service.mapper.SupplierThetaMapper;

/**
 * Integration tests for the {@link SupplierThetaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SupplierThetaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_PERSON = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_PERSON = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/supplier-thetas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SupplierThetaRepository supplierThetaRepository;

    @Mock
    private SupplierThetaRepository supplierThetaRepositoryMock;

    @Autowired
    private SupplierThetaMapper supplierThetaMapper;

    @Mock
    private SupplierThetaService supplierThetaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSupplierThetaMockMvc;

    private SupplierTheta supplierTheta;

    private SupplierTheta insertedSupplierTheta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SupplierTheta createEntity() {
        return new SupplierTheta()
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
    public static SupplierTheta createUpdatedEntity() {
        return new SupplierTheta()
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        supplierTheta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSupplierTheta != null) {
            supplierThetaRepository.delete(insertedSupplierTheta);
            insertedSupplierTheta = null;
        }
    }

    @Test
    @Transactional
    void createSupplierTheta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SupplierTheta
        SupplierThetaDTO supplierThetaDTO = supplierThetaMapper.toDto(supplierTheta);
        var returnedSupplierThetaDTO = om.readValue(
            restSupplierThetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierThetaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SupplierThetaDTO.class
        );

        // Validate the SupplierTheta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSupplierTheta = supplierThetaMapper.toEntity(returnedSupplierThetaDTO);
        assertSupplierThetaUpdatableFieldsEquals(returnedSupplierTheta, getPersistedSupplierTheta(returnedSupplierTheta));

        insertedSupplierTheta = returnedSupplierTheta;
    }

    @Test
    @Transactional
    void createSupplierThetaWithExistingId() throws Exception {
        // Create the SupplierTheta with an existing ID
        supplierTheta.setId(1L);
        SupplierThetaDTO supplierThetaDTO = supplierThetaMapper.toDto(supplierTheta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSupplierThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierThetaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SupplierTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        supplierTheta.setName(null);

        // Create the SupplierTheta, which fails.
        SupplierThetaDTO supplierThetaDTO = supplierThetaMapper.toDto(supplierTheta);

        restSupplierThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierThetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSupplierThetas() throws Exception {
        // Initialize the database
        insertedSupplierTheta = supplierThetaRepository.saveAndFlush(supplierTheta);

        // Get all the supplierThetaList
        restSupplierThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplierTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSupplierThetasWithEagerRelationshipsIsEnabled() throws Exception {
        when(supplierThetaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSupplierThetaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(supplierThetaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSupplierThetasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(supplierThetaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSupplierThetaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(supplierThetaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSupplierTheta() throws Exception {
        // Initialize the database
        insertedSupplierTheta = supplierThetaRepository.saveAndFlush(supplierTheta);

        // Get the supplierTheta
        restSupplierThetaMockMvc
            .perform(get(ENTITY_API_URL_ID, supplierTheta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(supplierTheta.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.contactPerson").value(DEFAULT_CONTACT_PERSON))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getSupplierThetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedSupplierTheta = supplierThetaRepository.saveAndFlush(supplierTheta);

        Long id = supplierTheta.getId();

        defaultSupplierThetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSupplierThetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSupplierThetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSupplierThetasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierTheta = supplierThetaRepository.saveAndFlush(supplierTheta);

        // Get all the supplierThetaList where name equals to
        defaultSupplierThetaFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSupplierThetasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierTheta = supplierThetaRepository.saveAndFlush(supplierTheta);

        // Get all the supplierThetaList where name in
        defaultSupplierThetaFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSupplierThetasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierTheta = supplierThetaRepository.saveAndFlush(supplierTheta);

        // Get all the supplierThetaList where name is not null
        defaultSupplierThetaFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierThetasByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierTheta = supplierThetaRepository.saveAndFlush(supplierTheta);

        // Get all the supplierThetaList where name contains
        defaultSupplierThetaFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSupplierThetasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierTheta = supplierThetaRepository.saveAndFlush(supplierTheta);

        // Get all the supplierThetaList where name does not contain
        defaultSupplierThetaFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllSupplierThetasByContactPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierTheta = supplierThetaRepository.saveAndFlush(supplierTheta);

        // Get all the supplierThetaList where contactPerson equals to
        defaultSupplierThetaFiltering("contactPerson.equals=" + DEFAULT_CONTACT_PERSON, "contactPerson.equals=" + UPDATED_CONTACT_PERSON);
    }

    @Test
    @Transactional
    void getAllSupplierThetasByContactPersonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierTheta = supplierThetaRepository.saveAndFlush(supplierTheta);

        // Get all the supplierThetaList where contactPerson in
        defaultSupplierThetaFiltering(
            "contactPerson.in=" + DEFAULT_CONTACT_PERSON + "," + UPDATED_CONTACT_PERSON,
            "contactPerson.in=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllSupplierThetasByContactPersonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierTheta = supplierThetaRepository.saveAndFlush(supplierTheta);

        // Get all the supplierThetaList where contactPerson is not null
        defaultSupplierThetaFiltering("contactPerson.specified=true", "contactPerson.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierThetasByContactPersonContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierTheta = supplierThetaRepository.saveAndFlush(supplierTheta);

        // Get all the supplierThetaList where contactPerson contains
        defaultSupplierThetaFiltering(
            "contactPerson.contains=" + DEFAULT_CONTACT_PERSON,
            "contactPerson.contains=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllSupplierThetasByContactPersonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierTheta = supplierThetaRepository.saveAndFlush(supplierTheta);

        // Get all the supplierThetaList where contactPerson does not contain
        defaultSupplierThetaFiltering(
            "contactPerson.doesNotContain=" + UPDATED_CONTACT_PERSON,
            "contactPerson.doesNotContain=" + DEFAULT_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllSupplierThetasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierTheta = supplierThetaRepository.saveAndFlush(supplierTheta);

        // Get all the supplierThetaList where email equals to
        defaultSupplierThetaFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSupplierThetasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierTheta = supplierThetaRepository.saveAndFlush(supplierTheta);

        // Get all the supplierThetaList where email in
        defaultSupplierThetaFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSupplierThetasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierTheta = supplierThetaRepository.saveAndFlush(supplierTheta);

        // Get all the supplierThetaList where email is not null
        defaultSupplierThetaFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierThetasByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierTheta = supplierThetaRepository.saveAndFlush(supplierTheta);

        // Get all the supplierThetaList where email contains
        defaultSupplierThetaFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSupplierThetasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierTheta = supplierThetaRepository.saveAndFlush(supplierTheta);

        // Get all the supplierThetaList where email does not contain
        defaultSupplierThetaFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllSupplierThetasByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierTheta = supplierThetaRepository.saveAndFlush(supplierTheta);

        // Get all the supplierThetaList where phoneNumber equals to
        defaultSupplierThetaFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllSupplierThetasByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierTheta = supplierThetaRepository.saveAndFlush(supplierTheta);

        // Get all the supplierThetaList where phoneNumber in
        defaultSupplierThetaFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllSupplierThetasByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierTheta = supplierThetaRepository.saveAndFlush(supplierTheta);

        // Get all the supplierThetaList where phoneNumber is not null
        defaultSupplierThetaFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierThetasByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierTheta = supplierThetaRepository.saveAndFlush(supplierTheta);

        // Get all the supplierThetaList where phoneNumber contains
        defaultSupplierThetaFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllSupplierThetasByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierTheta = supplierThetaRepository.saveAndFlush(supplierTheta);

        // Get all the supplierThetaList where phoneNumber does not contain
        defaultSupplierThetaFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllSupplierThetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            supplierThetaRepository.saveAndFlush(supplierTheta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        supplierTheta.setTenant(tenant);
        supplierThetaRepository.saveAndFlush(supplierTheta);
        Long tenantId = tenant.getId();
        // Get all the supplierThetaList where tenant equals to tenantId
        defaultSupplierThetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the supplierThetaList where tenant equals to (tenantId + 1)
        defaultSupplierThetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllSupplierThetasByProductsIsEqualToSomething() throws Exception {
        ProductTheta products;
        if (TestUtil.findAll(em, ProductTheta.class).isEmpty()) {
            supplierThetaRepository.saveAndFlush(supplierTheta);
            products = ProductThetaResourceIT.createEntity();
        } else {
            products = TestUtil.findAll(em, ProductTheta.class).get(0);
        }
        em.persist(products);
        em.flush();
        supplierTheta.addProducts(products);
        supplierThetaRepository.saveAndFlush(supplierTheta);
        Long productsId = products.getId();
        // Get all the supplierThetaList where products equals to productsId
        defaultSupplierThetaShouldBeFound("productsId.equals=" + productsId);

        // Get all the supplierThetaList where products equals to (productsId + 1)
        defaultSupplierThetaShouldNotBeFound("productsId.equals=" + (productsId + 1));
    }

    private void defaultSupplierThetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSupplierThetaShouldBeFound(shouldBeFound);
        defaultSupplierThetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSupplierThetaShouldBeFound(String filter) throws Exception {
        restSupplierThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplierTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restSupplierThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSupplierThetaShouldNotBeFound(String filter) throws Exception {
        restSupplierThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSupplierThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSupplierTheta() throws Exception {
        // Get the supplierTheta
        restSupplierThetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSupplierTheta() throws Exception {
        // Initialize the database
        insertedSupplierTheta = supplierThetaRepository.saveAndFlush(supplierTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplierTheta
        SupplierTheta updatedSupplierTheta = supplierThetaRepository.findById(supplierTheta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSupplierTheta are not directly saved in db
        em.detach(updatedSupplierTheta);
        updatedSupplierTheta
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        SupplierThetaDTO supplierThetaDTO = supplierThetaMapper.toDto(updatedSupplierTheta);

        restSupplierThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, supplierThetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supplierThetaDTO))
            )
            .andExpect(status().isOk());

        // Validate the SupplierTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSupplierThetaToMatchAllProperties(updatedSupplierTheta);
    }

    @Test
    @Transactional
    void putNonExistingSupplierTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierTheta.setId(longCount.incrementAndGet());

        // Create the SupplierTheta
        SupplierThetaDTO supplierThetaDTO = supplierThetaMapper.toDto(supplierTheta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, supplierThetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supplierThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSupplierTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierTheta.setId(longCount.incrementAndGet());

        // Create the SupplierTheta
        SupplierThetaDTO supplierThetaDTO = supplierThetaMapper.toDto(supplierTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supplierThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSupplierTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierTheta.setId(longCount.incrementAndGet());

        // Create the SupplierTheta
        SupplierThetaDTO supplierThetaDTO = supplierThetaMapper.toDto(supplierTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierThetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierThetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SupplierTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSupplierThetaWithPatch() throws Exception {
        // Initialize the database
        insertedSupplierTheta = supplierThetaRepository.saveAndFlush(supplierTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplierTheta using partial update
        SupplierTheta partialUpdatedSupplierTheta = new SupplierTheta();
        partialUpdatedSupplierTheta.setId(supplierTheta.getId());

        partialUpdatedSupplierTheta.contactPerson(UPDATED_CONTACT_PERSON);

        restSupplierThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplierTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSupplierTheta))
            )
            .andExpect(status().isOk());

        // Validate the SupplierTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSupplierThetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSupplierTheta, supplierTheta),
            getPersistedSupplierTheta(supplierTheta)
        );
    }

    @Test
    @Transactional
    void fullUpdateSupplierThetaWithPatch() throws Exception {
        // Initialize the database
        insertedSupplierTheta = supplierThetaRepository.saveAndFlush(supplierTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplierTheta using partial update
        SupplierTheta partialUpdatedSupplierTheta = new SupplierTheta();
        partialUpdatedSupplierTheta.setId(supplierTheta.getId());

        partialUpdatedSupplierTheta
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restSupplierThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplierTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSupplierTheta))
            )
            .andExpect(status().isOk());

        // Validate the SupplierTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSupplierThetaUpdatableFieldsEquals(partialUpdatedSupplierTheta, getPersistedSupplierTheta(partialUpdatedSupplierTheta));
    }

    @Test
    @Transactional
    void patchNonExistingSupplierTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierTheta.setId(longCount.incrementAndGet());

        // Create the SupplierTheta
        SupplierThetaDTO supplierThetaDTO = supplierThetaMapper.toDto(supplierTheta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, supplierThetaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(supplierThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSupplierTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierTheta.setId(longCount.incrementAndGet());

        // Create the SupplierTheta
        SupplierThetaDTO supplierThetaDTO = supplierThetaMapper.toDto(supplierTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(supplierThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSupplierTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierTheta.setId(longCount.incrementAndGet());

        // Create the SupplierTheta
        SupplierThetaDTO supplierThetaDTO = supplierThetaMapper.toDto(supplierTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierThetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(supplierThetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SupplierTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSupplierTheta() throws Exception {
        // Initialize the database
        insertedSupplierTheta = supplierThetaRepository.saveAndFlush(supplierTheta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the supplierTheta
        restSupplierThetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, supplierTheta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return supplierThetaRepository.count();
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

    protected SupplierTheta getPersistedSupplierTheta(SupplierTheta supplierTheta) {
        return supplierThetaRepository.findById(supplierTheta.getId()).orElseThrow();
    }

    protected void assertPersistedSupplierThetaToMatchAllProperties(SupplierTheta expectedSupplierTheta) {
        assertSupplierThetaAllPropertiesEquals(expectedSupplierTheta, getPersistedSupplierTheta(expectedSupplierTheta));
    }

    protected void assertPersistedSupplierThetaToMatchUpdatableProperties(SupplierTheta expectedSupplierTheta) {
        assertSupplierThetaAllUpdatablePropertiesEquals(expectedSupplierTheta, getPersistedSupplierTheta(expectedSupplierTheta));
    }
}