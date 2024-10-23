package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.SupplierAlphaAsserts.*;
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
import xyz.jhmapstruct.domain.ProductAlpha;
import xyz.jhmapstruct.domain.SupplierAlpha;
import xyz.jhmapstruct.repository.SupplierAlphaRepository;
import xyz.jhmapstruct.service.SupplierAlphaService;
import xyz.jhmapstruct.service.dto.SupplierAlphaDTO;
import xyz.jhmapstruct.service.mapper.SupplierAlphaMapper;

/**
 * Integration tests for the {@link SupplierAlphaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SupplierAlphaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_PERSON = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_PERSON = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/supplier-alphas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SupplierAlphaRepository supplierAlphaRepository;

    @Mock
    private SupplierAlphaRepository supplierAlphaRepositoryMock;

    @Autowired
    private SupplierAlphaMapper supplierAlphaMapper;

    @Mock
    private SupplierAlphaService supplierAlphaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSupplierAlphaMockMvc;

    private SupplierAlpha supplierAlpha;

    private SupplierAlpha insertedSupplierAlpha;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SupplierAlpha createEntity() {
        return new SupplierAlpha()
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
    public static SupplierAlpha createUpdatedEntity() {
        return new SupplierAlpha()
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        supplierAlpha = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSupplierAlpha != null) {
            supplierAlphaRepository.delete(insertedSupplierAlpha);
            insertedSupplierAlpha = null;
        }
    }

    @Test
    @Transactional
    void createSupplierAlpha() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SupplierAlpha
        SupplierAlphaDTO supplierAlphaDTO = supplierAlphaMapper.toDto(supplierAlpha);
        var returnedSupplierAlphaDTO = om.readValue(
            restSupplierAlphaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierAlphaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SupplierAlphaDTO.class
        );

        // Validate the SupplierAlpha in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSupplierAlpha = supplierAlphaMapper.toEntity(returnedSupplierAlphaDTO);
        assertSupplierAlphaUpdatableFieldsEquals(returnedSupplierAlpha, getPersistedSupplierAlpha(returnedSupplierAlpha));

        insertedSupplierAlpha = returnedSupplierAlpha;
    }

    @Test
    @Transactional
    void createSupplierAlphaWithExistingId() throws Exception {
        // Create the SupplierAlpha with an existing ID
        supplierAlpha.setId(1L);
        SupplierAlphaDTO supplierAlphaDTO = supplierAlphaMapper.toDto(supplierAlpha);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSupplierAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierAlphaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SupplierAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        supplierAlpha.setName(null);

        // Create the SupplierAlpha, which fails.
        SupplierAlphaDTO supplierAlphaDTO = supplierAlphaMapper.toDto(supplierAlpha);

        restSupplierAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierAlphaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSupplierAlphas() throws Exception {
        // Initialize the database
        insertedSupplierAlpha = supplierAlphaRepository.saveAndFlush(supplierAlpha);

        // Get all the supplierAlphaList
        restSupplierAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplierAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSupplierAlphasWithEagerRelationshipsIsEnabled() throws Exception {
        when(supplierAlphaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSupplierAlphaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(supplierAlphaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSupplierAlphasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(supplierAlphaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSupplierAlphaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(supplierAlphaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSupplierAlpha() throws Exception {
        // Initialize the database
        insertedSupplierAlpha = supplierAlphaRepository.saveAndFlush(supplierAlpha);

        // Get the supplierAlpha
        restSupplierAlphaMockMvc
            .perform(get(ENTITY_API_URL_ID, supplierAlpha.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(supplierAlpha.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.contactPerson").value(DEFAULT_CONTACT_PERSON))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getSupplierAlphasByIdFiltering() throws Exception {
        // Initialize the database
        insertedSupplierAlpha = supplierAlphaRepository.saveAndFlush(supplierAlpha);

        Long id = supplierAlpha.getId();

        defaultSupplierAlphaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSupplierAlphaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSupplierAlphaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSupplierAlphasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierAlpha = supplierAlphaRepository.saveAndFlush(supplierAlpha);

        // Get all the supplierAlphaList where name equals to
        defaultSupplierAlphaFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSupplierAlphasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierAlpha = supplierAlphaRepository.saveAndFlush(supplierAlpha);

        // Get all the supplierAlphaList where name in
        defaultSupplierAlphaFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSupplierAlphasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierAlpha = supplierAlphaRepository.saveAndFlush(supplierAlpha);

        // Get all the supplierAlphaList where name is not null
        defaultSupplierAlphaFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierAlphasByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierAlpha = supplierAlphaRepository.saveAndFlush(supplierAlpha);

        // Get all the supplierAlphaList where name contains
        defaultSupplierAlphaFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSupplierAlphasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierAlpha = supplierAlphaRepository.saveAndFlush(supplierAlpha);

        // Get all the supplierAlphaList where name does not contain
        defaultSupplierAlphaFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllSupplierAlphasByContactPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierAlpha = supplierAlphaRepository.saveAndFlush(supplierAlpha);

        // Get all the supplierAlphaList where contactPerson equals to
        defaultSupplierAlphaFiltering("contactPerson.equals=" + DEFAULT_CONTACT_PERSON, "contactPerson.equals=" + UPDATED_CONTACT_PERSON);
    }

    @Test
    @Transactional
    void getAllSupplierAlphasByContactPersonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierAlpha = supplierAlphaRepository.saveAndFlush(supplierAlpha);

        // Get all the supplierAlphaList where contactPerson in
        defaultSupplierAlphaFiltering(
            "contactPerson.in=" + DEFAULT_CONTACT_PERSON + "," + UPDATED_CONTACT_PERSON,
            "contactPerson.in=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllSupplierAlphasByContactPersonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierAlpha = supplierAlphaRepository.saveAndFlush(supplierAlpha);

        // Get all the supplierAlphaList where contactPerson is not null
        defaultSupplierAlphaFiltering("contactPerson.specified=true", "contactPerson.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierAlphasByContactPersonContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierAlpha = supplierAlphaRepository.saveAndFlush(supplierAlpha);

        // Get all the supplierAlphaList where contactPerson contains
        defaultSupplierAlphaFiltering(
            "contactPerson.contains=" + DEFAULT_CONTACT_PERSON,
            "contactPerson.contains=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllSupplierAlphasByContactPersonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierAlpha = supplierAlphaRepository.saveAndFlush(supplierAlpha);

        // Get all the supplierAlphaList where contactPerson does not contain
        defaultSupplierAlphaFiltering(
            "contactPerson.doesNotContain=" + UPDATED_CONTACT_PERSON,
            "contactPerson.doesNotContain=" + DEFAULT_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllSupplierAlphasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierAlpha = supplierAlphaRepository.saveAndFlush(supplierAlpha);

        // Get all the supplierAlphaList where email equals to
        defaultSupplierAlphaFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSupplierAlphasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierAlpha = supplierAlphaRepository.saveAndFlush(supplierAlpha);

        // Get all the supplierAlphaList where email in
        defaultSupplierAlphaFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSupplierAlphasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierAlpha = supplierAlphaRepository.saveAndFlush(supplierAlpha);

        // Get all the supplierAlphaList where email is not null
        defaultSupplierAlphaFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierAlphasByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierAlpha = supplierAlphaRepository.saveAndFlush(supplierAlpha);

        // Get all the supplierAlphaList where email contains
        defaultSupplierAlphaFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSupplierAlphasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierAlpha = supplierAlphaRepository.saveAndFlush(supplierAlpha);

        // Get all the supplierAlphaList where email does not contain
        defaultSupplierAlphaFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllSupplierAlphasByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierAlpha = supplierAlphaRepository.saveAndFlush(supplierAlpha);

        // Get all the supplierAlphaList where phoneNumber equals to
        defaultSupplierAlphaFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllSupplierAlphasByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierAlpha = supplierAlphaRepository.saveAndFlush(supplierAlpha);

        // Get all the supplierAlphaList where phoneNumber in
        defaultSupplierAlphaFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllSupplierAlphasByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierAlpha = supplierAlphaRepository.saveAndFlush(supplierAlpha);

        // Get all the supplierAlphaList where phoneNumber is not null
        defaultSupplierAlphaFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierAlphasByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierAlpha = supplierAlphaRepository.saveAndFlush(supplierAlpha);

        // Get all the supplierAlphaList where phoneNumber contains
        defaultSupplierAlphaFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllSupplierAlphasByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierAlpha = supplierAlphaRepository.saveAndFlush(supplierAlpha);

        // Get all the supplierAlphaList where phoneNumber does not contain
        defaultSupplierAlphaFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllSupplierAlphasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            supplierAlphaRepository.saveAndFlush(supplierAlpha);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        supplierAlpha.setTenant(tenant);
        supplierAlphaRepository.saveAndFlush(supplierAlpha);
        Long tenantId = tenant.getId();
        // Get all the supplierAlphaList where tenant equals to tenantId
        defaultSupplierAlphaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the supplierAlphaList where tenant equals to (tenantId + 1)
        defaultSupplierAlphaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllSupplierAlphasByProductsIsEqualToSomething() throws Exception {
        ProductAlpha products;
        if (TestUtil.findAll(em, ProductAlpha.class).isEmpty()) {
            supplierAlphaRepository.saveAndFlush(supplierAlpha);
            products = ProductAlphaResourceIT.createEntity();
        } else {
            products = TestUtil.findAll(em, ProductAlpha.class).get(0);
        }
        em.persist(products);
        em.flush();
        supplierAlpha.addProducts(products);
        supplierAlphaRepository.saveAndFlush(supplierAlpha);
        Long productsId = products.getId();
        // Get all the supplierAlphaList where products equals to productsId
        defaultSupplierAlphaShouldBeFound("productsId.equals=" + productsId);

        // Get all the supplierAlphaList where products equals to (productsId + 1)
        defaultSupplierAlphaShouldNotBeFound("productsId.equals=" + (productsId + 1));
    }

    private void defaultSupplierAlphaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSupplierAlphaShouldBeFound(shouldBeFound);
        defaultSupplierAlphaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSupplierAlphaShouldBeFound(String filter) throws Exception {
        restSupplierAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplierAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restSupplierAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSupplierAlphaShouldNotBeFound(String filter) throws Exception {
        restSupplierAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSupplierAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSupplierAlpha() throws Exception {
        // Get the supplierAlpha
        restSupplierAlphaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSupplierAlpha() throws Exception {
        // Initialize the database
        insertedSupplierAlpha = supplierAlphaRepository.saveAndFlush(supplierAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplierAlpha
        SupplierAlpha updatedSupplierAlpha = supplierAlphaRepository.findById(supplierAlpha.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSupplierAlpha are not directly saved in db
        em.detach(updatedSupplierAlpha);
        updatedSupplierAlpha
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        SupplierAlphaDTO supplierAlphaDTO = supplierAlphaMapper.toDto(updatedSupplierAlpha);

        restSupplierAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, supplierAlphaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supplierAlphaDTO))
            )
            .andExpect(status().isOk());

        // Validate the SupplierAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSupplierAlphaToMatchAllProperties(updatedSupplierAlpha);
    }

    @Test
    @Transactional
    void putNonExistingSupplierAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierAlpha.setId(longCount.incrementAndGet());

        // Create the SupplierAlpha
        SupplierAlphaDTO supplierAlphaDTO = supplierAlphaMapper.toDto(supplierAlpha);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, supplierAlphaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supplierAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSupplierAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierAlpha.setId(longCount.incrementAndGet());

        // Create the SupplierAlpha
        SupplierAlphaDTO supplierAlphaDTO = supplierAlphaMapper.toDto(supplierAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supplierAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSupplierAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierAlpha.setId(longCount.incrementAndGet());

        // Create the SupplierAlpha
        SupplierAlphaDTO supplierAlphaDTO = supplierAlphaMapper.toDto(supplierAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierAlphaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierAlphaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SupplierAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSupplierAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedSupplierAlpha = supplierAlphaRepository.saveAndFlush(supplierAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplierAlpha using partial update
        SupplierAlpha partialUpdatedSupplierAlpha = new SupplierAlpha();
        partialUpdatedSupplierAlpha.setId(supplierAlpha.getId());

        partialUpdatedSupplierAlpha.phoneNumber(UPDATED_PHONE_NUMBER);

        restSupplierAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplierAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSupplierAlpha))
            )
            .andExpect(status().isOk());

        // Validate the SupplierAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSupplierAlphaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSupplierAlpha, supplierAlpha),
            getPersistedSupplierAlpha(supplierAlpha)
        );
    }

    @Test
    @Transactional
    void fullUpdateSupplierAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedSupplierAlpha = supplierAlphaRepository.saveAndFlush(supplierAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplierAlpha using partial update
        SupplierAlpha partialUpdatedSupplierAlpha = new SupplierAlpha();
        partialUpdatedSupplierAlpha.setId(supplierAlpha.getId());

        partialUpdatedSupplierAlpha
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restSupplierAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplierAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSupplierAlpha))
            )
            .andExpect(status().isOk());

        // Validate the SupplierAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSupplierAlphaUpdatableFieldsEquals(partialUpdatedSupplierAlpha, getPersistedSupplierAlpha(partialUpdatedSupplierAlpha));
    }

    @Test
    @Transactional
    void patchNonExistingSupplierAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierAlpha.setId(longCount.incrementAndGet());

        // Create the SupplierAlpha
        SupplierAlphaDTO supplierAlphaDTO = supplierAlphaMapper.toDto(supplierAlpha);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, supplierAlphaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(supplierAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSupplierAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierAlpha.setId(longCount.incrementAndGet());

        // Create the SupplierAlpha
        SupplierAlphaDTO supplierAlphaDTO = supplierAlphaMapper.toDto(supplierAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(supplierAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSupplierAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierAlpha.setId(longCount.incrementAndGet());

        // Create the SupplierAlpha
        SupplierAlphaDTO supplierAlphaDTO = supplierAlphaMapper.toDto(supplierAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierAlphaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(supplierAlphaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SupplierAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSupplierAlpha() throws Exception {
        // Initialize the database
        insertedSupplierAlpha = supplierAlphaRepository.saveAndFlush(supplierAlpha);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the supplierAlpha
        restSupplierAlphaMockMvc
            .perform(delete(ENTITY_API_URL_ID, supplierAlpha.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return supplierAlphaRepository.count();
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

    protected SupplierAlpha getPersistedSupplierAlpha(SupplierAlpha supplierAlpha) {
        return supplierAlphaRepository.findById(supplierAlpha.getId()).orElseThrow();
    }

    protected void assertPersistedSupplierAlphaToMatchAllProperties(SupplierAlpha expectedSupplierAlpha) {
        assertSupplierAlphaAllPropertiesEquals(expectedSupplierAlpha, getPersistedSupplierAlpha(expectedSupplierAlpha));
    }

    protected void assertPersistedSupplierAlphaToMatchUpdatableProperties(SupplierAlpha expectedSupplierAlpha) {
        assertSupplierAlphaAllUpdatablePropertiesEquals(expectedSupplierAlpha, getPersistedSupplierAlpha(expectedSupplierAlpha));
    }
}
