package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.SupplierMiAsserts.*;
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
import xyz.jhmapstruct.domain.NextProductMi;
import xyz.jhmapstruct.domain.SupplierMi;
import xyz.jhmapstruct.repository.SupplierMiRepository;
import xyz.jhmapstruct.service.SupplierMiService;
import xyz.jhmapstruct.service.dto.SupplierMiDTO;
import xyz.jhmapstruct.service.mapper.SupplierMiMapper;

/**
 * Integration tests for the {@link SupplierMiResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SupplierMiResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_PERSON = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_PERSON = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/supplier-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SupplierMiRepository supplierMiRepository;

    @Mock
    private SupplierMiRepository supplierMiRepositoryMock;

    @Autowired
    private SupplierMiMapper supplierMiMapper;

    @Mock
    private SupplierMiService supplierMiServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSupplierMiMockMvc;

    private SupplierMi supplierMi;

    private SupplierMi insertedSupplierMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SupplierMi createEntity() {
        return new SupplierMi()
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
    public static SupplierMi createUpdatedEntity() {
        return new SupplierMi()
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        supplierMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSupplierMi != null) {
            supplierMiRepository.delete(insertedSupplierMi);
            insertedSupplierMi = null;
        }
    }

    @Test
    @Transactional
    void createSupplierMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SupplierMi
        SupplierMiDTO supplierMiDTO = supplierMiMapper.toDto(supplierMi);
        var returnedSupplierMiDTO = om.readValue(
            restSupplierMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierMiDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SupplierMiDTO.class
        );

        // Validate the SupplierMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSupplierMi = supplierMiMapper.toEntity(returnedSupplierMiDTO);
        assertSupplierMiUpdatableFieldsEquals(returnedSupplierMi, getPersistedSupplierMi(returnedSupplierMi));

        insertedSupplierMi = returnedSupplierMi;
    }

    @Test
    @Transactional
    void createSupplierMiWithExistingId() throws Exception {
        // Create the SupplierMi with an existing ID
        supplierMi.setId(1L);
        SupplierMiDTO supplierMiDTO = supplierMiMapper.toDto(supplierMi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSupplierMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierMiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SupplierMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        supplierMi.setName(null);

        // Create the SupplierMi, which fails.
        SupplierMiDTO supplierMiDTO = supplierMiMapper.toDto(supplierMi);

        restSupplierMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSupplierMis() throws Exception {
        // Initialize the database
        insertedSupplierMi = supplierMiRepository.saveAndFlush(supplierMi);

        // Get all the supplierMiList
        restSupplierMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplierMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSupplierMisWithEagerRelationshipsIsEnabled() throws Exception {
        when(supplierMiServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSupplierMiMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(supplierMiServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSupplierMisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(supplierMiServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSupplierMiMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(supplierMiRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSupplierMi() throws Exception {
        // Initialize the database
        insertedSupplierMi = supplierMiRepository.saveAndFlush(supplierMi);

        // Get the supplierMi
        restSupplierMiMockMvc
            .perform(get(ENTITY_API_URL_ID, supplierMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(supplierMi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.contactPerson").value(DEFAULT_CONTACT_PERSON))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getSupplierMisByIdFiltering() throws Exception {
        // Initialize the database
        insertedSupplierMi = supplierMiRepository.saveAndFlush(supplierMi);

        Long id = supplierMi.getId();

        defaultSupplierMiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSupplierMiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSupplierMiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSupplierMisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierMi = supplierMiRepository.saveAndFlush(supplierMi);

        // Get all the supplierMiList where name equals to
        defaultSupplierMiFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSupplierMisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierMi = supplierMiRepository.saveAndFlush(supplierMi);

        // Get all the supplierMiList where name in
        defaultSupplierMiFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSupplierMisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierMi = supplierMiRepository.saveAndFlush(supplierMi);

        // Get all the supplierMiList where name is not null
        defaultSupplierMiFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierMisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierMi = supplierMiRepository.saveAndFlush(supplierMi);

        // Get all the supplierMiList where name contains
        defaultSupplierMiFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSupplierMisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierMi = supplierMiRepository.saveAndFlush(supplierMi);

        // Get all the supplierMiList where name does not contain
        defaultSupplierMiFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllSupplierMisByContactPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierMi = supplierMiRepository.saveAndFlush(supplierMi);

        // Get all the supplierMiList where contactPerson equals to
        defaultSupplierMiFiltering("contactPerson.equals=" + DEFAULT_CONTACT_PERSON, "contactPerson.equals=" + UPDATED_CONTACT_PERSON);
    }

    @Test
    @Transactional
    void getAllSupplierMisByContactPersonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierMi = supplierMiRepository.saveAndFlush(supplierMi);

        // Get all the supplierMiList where contactPerson in
        defaultSupplierMiFiltering(
            "contactPerson.in=" + DEFAULT_CONTACT_PERSON + "," + UPDATED_CONTACT_PERSON,
            "contactPerson.in=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllSupplierMisByContactPersonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierMi = supplierMiRepository.saveAndFlush(supplierMi);

        // Get all the supplierMiList where contactPerson is not null
        defaultSupplierMiFiltering("contactPerson.specified=true", "contactPerson.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierMisByContactPersonContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierMi = supplierMiRepository.saveAndFlush(supplierMi);

        // Get all the supplierMiList where contactPerson contains
        defaultSupplierMiFiltering("contactPerson.contains=" + DEFAULT_CONTACT_PERSON, "contactPerson.contains=" + UPDATED_CONTACT_PERSON);
    }

    @Test
    @Transactional
    void getAllSupplierMisByContactPersonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierMi = supplierMiRepository.saveAndFlush(supplierMi);

        // Get all the supplierMiList where contactPerson does not contain
        defaultSupplierMiFiltering(
            "contactPerson.doesNotContain=" + UPDATED_CONTACT_PERSON,
            "contactPerson.doesNotContain=" + DEFAULT_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllSupplierMisByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierMi = supplierMiRepository.saveAndFlush(supplierMi);

        // Get all the supplierMiList where email equals to
        defaultSupplierMiFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSupplierMisByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierMi = supplierMiRepository.saveAndFlush(supplierMi);

        // Get all the supplierMiList where email in
        defaultSupplierMiFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSupplierMisByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierMi = supplierMiRepository.saveAndFlush(supplierMi);

        // Get all the supplierMiList where email is not null
        defaultSupplierMiFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierMisByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierMi = supplierMiRepository.saveAndFlush(supplierMi);

        // Get all the supplierMiList where email contains
        defaultSupplierMiFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSupplierMisByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierMi = supplierMiRepository.saveAndFlush(supplierMi);

        // Get all the supplierMiList where email does not contain
        defaultSupplierMiFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllSupplierMisByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierMi = supplierMiRepository.saveAndFlush(supplierMi);

        // Get all the supplierMiList where phoneNumber equals to
        defaultSupplierMiFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllSupplierMisByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierMi = supplierMiRepository.saveAndFlush(supplierMi);

        // Get all the supplierMiList where phoneNumber in
        defaultSupplierMiFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllSupplierMisByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierMi = supplierMiRepository.saveAndFlush(supplierMi);

        // Get all the supplierMiList where phoneNumber is not null
        defaultSupplierMiFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierMisByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierMi = supplierMiRepository.saveAndFlush(supplierMi);

        // Get all the supplierMiList where phoneNumber contains
        defaultSupplierMiFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllSupplierMisByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierMi = supplierMiRepository.saveAndFlush(supplierMi);

        // Get all the supplierMiList where phoneNumber does not contain
        defaultSupplierMiFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllSupplierMisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            supplierMiRepository.saveAndFlush(supplierMi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        supplierMi.setTenant(tenant);
        supplierMiRepository.saveAndFlush(supplierMi);
        Long tenantId = tenant.getId();
        // Get all the supplierMiList where tenant equals to tenantId
        defaultSupplierMiShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the supplierMiList where tenant equals to (tenantId + 1)
        defaultSupplierMiShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllSupplierMisByProductsIsEqualToSomething() throws Exception {
        NextProductMi products;
        if (TestUtil.findAll(em, NextProductMi.class).isEmpty()) {
            supplierMiRepository.saveAndFlush(supplierMi);
            products = NextProductMiResourceIT.createEntity();
        } else {
            products = TestUtil.findAll(em, NextProductMi.class).get(0);
        }
        em.persist(products);
        em.flush();
        supplierMi.addProducts(products);
        supplierMiRepository.saveAndFlush(supplierMi);
        Long productsId = products.getId();
        // Get all the supplierMiList where products equals to productsId
        defaultSupplierMiShouldBeFound("productsId.equals=" + productsId);

        // Get all the supplierMiList where products equals to (productsId + 1)
        defaultSupplierMiShouldNotBeFound("productsId.equals=" + (productsId + 1));
    }

    private void defaultSupplierMiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSupplierMiShouldBeFound(shouldBeFound);
        defaultSupplierMiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSupplierMiShouldBeFound(String filter) throws Exception {
        restSupplierMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplierMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restSupplierMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSupplierMiShouldNotBeFound(String filter) throws Exception {
        restSupplierMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSupplierMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSupplierMi() throws Exception {
        // Get the supplierMi
        restSupplierMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSupplierMi() throws Exception {
        // Initialize the database
        insertedSupplierMi = supplierMiRepository.saveAndFlush(supplierMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplierMi
        SupplierMi updatedSupplierMi = supplierMiRepository.findById(supplierMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSupplierMi are not directly saved in db
        em.detach(updatedSupplierMi);
        updatedSupplierMi.name(UPDATED_NAME).contactPerson(UPDATED_CONTACT_PERSON).email(UPDATED_EMAIL).phoneNumber(UPDATED_PHONE_NUMBER);
        SupplierMiDTO supplierMiDTO = supplierMiMapper.toDto(updatedSupplierMi);

        restSupplierMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, supplierMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supplierMiDTO))
            )
            .andExpect(status().isOk());

        // Validate the SupplierMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSupplierMiToMatchAllProperties(updatedSupplierMi);
    }

    @Test
    @Transactional
    void putNonExistingSupplierMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierMi.setId(longCount.incrementAndGet());

        // Create the SupplierMi
        SupplierMiDTO supplierMiDTO = supplierMiMapper.toDto(supplierMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, supplierMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supplierMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSupplierMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierMi.setId(longCount.incrementAndGet());

        // Create the SupplierMi
        SupplierMiDTO supplierMiDTO = supplierMiMapper.toDto(supplierMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supplierMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSupplierMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierMi.setId(longCount.incrementAndGet());

        // Create the SupplierMi
        SupplierMiDTO supplierMiDTO = supplierMiMapper.toDto(supplierMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SupplierMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSupplierMiWithPatch() throws Exception {
        // Initialize the database
        insertedSupplierMi = supplierMiRepository.saveAndFlush(supplierMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplierMi using partial update
        SupplierMi partialUpdatedSupplierMi = new SupplierMi();
        partialUpdatedSupplierMi.setId(supplierMi.getId());

        partialUpdatedSupplierMi.email(UPDATED_EMAIL).phoneNumber(UPDATED_PHONE_NUMBER);

        restSupplierMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplierMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSupplierMi))
            )
            .andExpect(status().isOk());

        // Validate the SupplierMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSupplierMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSupplierMi, supplierMi),
            getPersistedSupplierMi(supplierMi)
        );
    }

    @Test
    @Transactional
    void fullUpdateSupplierMiWithPatch() throws Exception {
        // Initialize the database
        insertedSupplierMi = supplierMiRepository.saveAndFlush(supplierMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplierMi using partial update
        SupplierMi partialUpdatedSupplierMi = new SupplierMi();
        partialUpdatedSupplierMi.setId(supplierMi.getId());

        partialUpdatedSupplierMi
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restSupplierMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplierMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSupplierMi))
            )
            .andExpect(status().isOk());

        // Validate the SupplierMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSupplierMiUpdatableFieldsEquals(partialUpdatedSupplierMi, getPersistedSupplierMi(partialUpdatedSupplierMi));
    }

    @Test
    @Transactional
    void patchNonExistingSupplierMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierMi.setId(longCount.incrementAndGet());

        // Create the SupplierMi
        SupplierMiDTO supplierMiDTO = supplierMiMapper.toDto(supplierMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, supplierMiDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(supplierMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSupplierMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierMi.setId(longCount.incrementAndGet());

        // Create the SupplierMi
        SupplierMiDTO supplierMiDTO = supplierMiMapper.toDto(supplierMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(supplierMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSupplierMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierMi.setId(longCount.incrementAndGet());

        // Create the SupplierMi
        SupplierMiDTO supplierMiDTO = supplierMiMapper.toDto(supplierMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(supplierMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SupplierMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSupplierMi() throws Exception {
        // Initialize the database
        insertedSupplierMi = supplierMiRepository.saveAndFlush(supplierMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the supplierMi
        restSupplierMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, supplierMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return supplierMiRepository.count();
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

    protected SupplierMi getPersistedSupplierMi(SupplierMi supplierMi) {
        return supplierMiRepository.findById(supplierMi.getId()).orElseThrow();
    }

    protected void assertPersistedSupplierMiToMatchAllProperties(SupplierMi expectedSupplierMi) {
        assertSupplierMiAllPropertiesEquals(expectedSupplierMi, getPersistedSupplierMi(expectedSupplierMi));
    }

    protected void assertPersistedSupplierMiToMatchUpdatableProperties(SupplierMi expectedSupplierMi) {
        assertSupplierMiAllUpdatablePropertiesEquals(expectedSupplierMi, getPersistedSupplierMi(expectedSupplierMi));
    }
}
