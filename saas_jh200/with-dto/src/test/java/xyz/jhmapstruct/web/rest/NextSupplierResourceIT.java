package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextSupplierAsserts.*;
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
import xyz.jhmapstruct.domain.NextProduct;
import xyz.jhmapstruct.domain.NextSupplier;
import xyz.jhmapstruct.repository.NextSupplierRepository;
import xyz.jhmapstruct.service.NextSupplierService;
import xyz.jhmapstruct.service.dto.NextSupplierDTO;
import xyz.jhmapstruct.service.mapper.NextSupplierMapper;

/**
 * Integration tests for the {@link NextSupplierResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextSupplierResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_PERSON = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_PERSON = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/next-suppliers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextSupplierRepository nextSupplierRepository;

    @Mock
    private NextSupplierRepository nextSupplierRepositoryMock;

    @Autowired
    private NextSupplierMapper nextSupplierMapper;

    @Mock
    private NextSupplierService nextSupplierServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextSupplierMockMvc;

    private NextSupplier nextSupplier;

    private NextSupplier insertedNextSupplier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextSupplier createEntity() {
        return new NextSupplier()
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
    public static NextSupplier createUpdatedEntity() {
        return new NextSupplier()
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        nextSupplier = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextSupplier != null) {
            nextSupplierRepository.delete(insertedNextSupplier);
            insertedNextSupplier = null;
        }
    }

    @Test
    @Transactional
    void createNextSupplier() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextSupplier
        NextSupplierDTO nextSupplierDTO = nextSupplierMapper.toDto(nextSupplier);
        var returnedNextSupplierDTO = om.readValue(
            restNextSupplierMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextSupplierDTO.class
        );

        // Validate the NextSupplier in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextSupplier = nextSupplierMapper.toEntity(returnedNextSupplierDTO);
        assertNextSupplierUpdatableFieldsEquals(returnedNextSupplier, getPersistedNextSupplier(returnedNextSupplier));

        insertedNextSupplier = returnedNextSupplier;
    }

    @Test
    @Transactional
    void createNextSupplierWithExistingId() throws Exception {
        // Create the NextSupplier with an existing ID
        nextSupplier.setId(1L);
        NextSupplierDTO nextSupplierDTO = nextSupplierMapper.toDto(nextSupplier);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextSupplierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextSupplier in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextSupplier.setName(null);

        // Create the NextSupplier, which fails.
        NextSupplierDTO nextSupplierDTO = nextSupplierMapper.toDto(nextSupplier);

        restNextSupplierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextSuppliers() throws Exception {
        // Initialize the database
        insertedNextSupplier = nextSupplierRepository.saveAndFlush(nextSupplier);

        // Get all the nextSupplierList
        restNextSupplierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextSupplier.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextSuppliersWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextSupplierServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextSupplierMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextSupplierServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextSuppliersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextSupplierServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextSupplierMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextSupplierRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextSupplier() throws Exception {
        // Initialize the database
        insertedNextSupplier = nextSupplierRepository.saveAndFlush(nextSupplier);

        // Get the nextSupplier
        restNextSupplierMockMvc
            .perform(get(ENTITY_API_URL_ID, nextSupplier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextSupplier.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.contactPerson").value(DEFAULT_CONTACT_PERSON))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getNextSuppliersByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextSupplier = nextSupplierRepository.saveAndFlush(nextSupplier);

        Long id = nextSupplier.getId();

        defaultNextSupplierFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextSupplierFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextSupplierFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextSuppliersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplier = nextSupplierRepository.saveAndFlush(nextSupplier);

        // Get all the nextSupplierList where name equals to
        defaultNextSupplierFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextSuppliersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplier = nextSupplierRepository.saveAndFlush(nextSupplier);

        // Get all the nextSupplierList where name in
        defaultNextSupplierFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextSuppliersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplier = nextSupplierRepository.saveAndFlush(nextSupplier);

        // Get all the nextSupplierList where name is not null
        defaultNextSupplierFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSuppliersByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplier = nextSupplierRepository.saveAndFlush(nextSupplier);

        // Get all the nextSupplierList where name contains
        defaultNextSupplierFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextSuppliersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplier = nextSupplierRepository.saveAndFlush(nextSupplier);

        // Get all the nextSupplierList where name does not contain
        defaultNextSupplierFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllNextSuppliersByContactPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplier = nextSupplierRepository.saveAndFlush(nextSupplier);

        // Get all the nextSupplierList where contactPerson equals to
        defaultNextSupplierFiltering("contactPerson.equals=" + DEFAULT_CONTACT_PERSON, "contactPerson.equals=" + UPDATED_CONTACT_PERSON);
    }

    @Test
    @Transactional
    void getAllNextSuppliersByContactPersonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplier = nextSupplierRepository.saveAndFlush(nextSupplier);

        // Get all the nextSupplierList where contactPerson in
        defaultNextSupplierFiltering(
            "contactPerson.in=" + DEFAULT_CONTACT_PERSON + "," + UPDATED_CONTACT_PERSON,
            "contactPerson.in=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSuppliersByContactPersonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplier = nextSupplierRepository.saveAndFlush(nextSupplier);

        // Get all the nextSupplierList where contactPerson is not null
        defaultNextSupplierFiltering("contactPerson.specified=true", "contactPerson.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSuppliersByContactPersonContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplier = nextSupplierRepository.saveAndFlush(nextSupplier);

        // Get all the nextSupplierList where contactPerson contains
        defaultNextSupplierFiltering(
            "contactPerson.contains=" + DEFAULT_CONTACT_PERSON,
            "contactPerson.contains=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSuppliersByContactPersonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplier = nextSupplierRepository.saveAndFlush(nextSupplier);

        // Get all the nextSupplierList where contactPerson does not contain
        defaultNextSupplierFiltering(
            "contactPerson.doesNotContain=" + UPDATED_CONTACT_PERSON,
            "contactPerson.doesNotContain=" + DEFAULT_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSuppliersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplier = nextSupplierRepository.saveAndFlush(nextSupplier);

        // Get all the nextSupplierList where email equals to
        defaultNextSupplierFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSuppliersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplier = nextSupplierRepository.saveAndFlush(nextSupplier);

        // Get all the nextSupplierList where email in
        defaultNextSupplierFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSuppliersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplier = nextSupplierRepository.saveAndFlush(nextSupplier);

        // Get all the nextSupplierList where email is not null
        defaultNextSupplierFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSuppliersByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplier = nextSupplierRepository.saveAndFlush(nextSupplier);

        // Get all the nextSupplierList where email contains
        defaultNextSupplierFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSuppliersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplier = nextSupplierRepository.saveAndFlush(nextSupplier);

        // Get all the nextSupplierList where email does not contain
        defaultNextSupplierFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSuppliersByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplier = nextSupplierRepository.saveAndFlush(nextSupplier);

        // Get all the nextSupplierList where phoneNumber equals to
        defaultNextSupplierFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextSuppliersByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplier = nextSupplierRepository.saveAndFlush(nextSupplier);

        // Get all the nextSupplierList where phoneNumber in
        defaultNextSupplierFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextSuppliersByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplier = nextSupplierRepository.saveAndFlush(nextSupplier);

        // Get all the nextSupplierList where phoneNumber is not null
        defaultNextSupplierFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSuppliersByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplier = nextSupplierRepository.saveAndFlush(nextSupplier);

        // Get all the nextSupplierList where phoneNumber contains
        defaultNextSupplierFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextSuppliersByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplier = nextSupplierRepository.saveAndFlush(nextSupplier);

        // Get all the nextSupplierList where phoneNumber does not contain
        defaultNextSupplierFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextSuppliersByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextSupplierRepository.saveAndFlush(nextSupplier);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextSupplier.setTenant(tenant);
        nextSupplierRepository.saveAndFlush(nextSupplier);
        Long tenantId = tenant.getId();
        // Get all the nextSupplierList where tenant equals to tenantId
        defaultNextSupplierShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextSupplierList where tenant equals to (tenantId + 1)
        defaultNextSupplierShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllNextSuppliersByProductsIsEqualToSomething() throws Exception {
        NextProduct products;
        if (TestUtil.findAll(em, NextProduct.class).isEmpty()) {
            nextSupplierRepository.saveAndFlush(nextSupplier);
            products = NextProductResourceIT.createEntity();
        } else {
            products = TestUtil.findAll(em, NextProduct.class).get(0);
        }
        em.persist(products);
        em.flush();
        nextSupplier.addProducts(products);
        nextSupplierRepository.saveAndFlush(nextSupplier);
        Long productsId = products.getId();
        // Get all the nextSupplierList where products equals to productsId
        defaultNextSupplierShouldBeFound("productsId.equals=" + productsId);

        // Get all the nextSupplierList where products equals to (productsId + 1)
        defaultNextSupplierShouldNotBeFound("productsId.equals=" + (productsId + 1));
    }

    private void defaultNextSupplierFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextSupplierShouldBeFound(shouldBeFound);
        defaultNextSupplierShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextSupplierShouldBeFound(String filter) throws Exception {
        restNextSupplierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextSupplier.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restNextSupplierMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextSupplierShouldNotBeFound(String filter) throws Exception {
        restNextSupplierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextSupplierMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextSupplier() throws Exception {
        // Get the nextSupplier
        restNextSupplierMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextSupplier() throws Exception {
        // Initialize the database
        insertedNextSupplier = nextSupplierRepository.saveAndFlush(nextSupplier);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextSupplier
        NextSupplier updatedNextSupplier = nextSupplierRepository.findById(nextSupplier.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextSupplier are not directly saved in db
        em.detach(updatedNextSupplier);
        updatedNextSupplier.name(UPDATED_NAME).contactPerson(UPDATED_CONTACT_PERSON).email(UPDATED_EMAIL).phoneNumber(UPDATED_PHONE_NUMBER);
        NextSupplierDTO nextSupplierDTO = nextSupplierMapper.toDto(updatedNextSupplier);

        restNextSupplierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextSupplierDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextSupplierDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextSupplier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextSupplierToMatchAllProperties(updatedNextSupplier);
    }

    @Test
    @Transactional
    void putNonExistingNextSupplier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplier.setId(longCount.incrementAndGet());

        // Create the NextSupplier
        NextSupplierDTO nextSupplierDTO = nextSupplierMapper.toDto(nextSupplier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextSupplierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextSupplierDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextSupplierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextSupplier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplier.setId(longCount.incrementAndGet());

        // Create the NextSupplier
        NextSupplierDTO nextSupplierDTO = nextSupplierMapper.toDto(nextSupplier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextSupplierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextSupplier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplier.setId(longCount.incrementAndGet());

        // Create the NextSupplier
        NextSupplierDTO nextSupplierDTO = nextSupplierMapper.toDto(nextSupplier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextSupplier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextSupplierWithPatch() throws Exception {
        // Initialize the database
        insertedNextSupplier = nextSupplierRepository.saveAndFlush(nextSupplier);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextSupplier using partial update
        NextSupplier partialUpdatedNextSupplier = new NextSupplier();
        partialUpdatedNextSupplier.setId(nextSupplier.getId());

        partialUpdatedNextSupplier.email(UPDATED_EMAIL).phoneNumber(UPDATED_PHONE_NUMBER);

        restNextSupplierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextSupplier.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextSupplier))
            )
            .andExpect(status().isOk());

        // Validate the NextSupplier in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextSupplierUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextSupplier, nextSupplier),
            getPersistedNextSupplier(nextSupplier)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextSupplierWithPatch() throws Exception {
        // Initialize the database
        insertedNextSupplier = nextSupplierRepository.saveAndFlush(nextSupplier);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextSupplier using partial update
        NextSupplier partialUpdatedNextSupplier = new NextSupplier();
        partialUpdatedNextSupplier.setId(nextSupplier.getId());

        partialUpdatedNextSupplier
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restNextSupplierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextSupplier.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextSupplier))
            )
            .andExpect(status().isOk());

        // Validate the NextSupplier in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextSupplierUpdatableFieldsEquals(partialUpdatedNextSupplier, getPersistedNextSupplier(partialUpdatedNextSupplier));
    }

    @Test
    @Transactional
    void patchNonExistingNextSupplier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplier.setId(longCount.incrementAndGet());

        // Create the NextSupplier
        NextSupplierDTO nextSupplierDTO = nextSupplierMapper.toDto(nextSupplier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextSupplierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextSupplierDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextSupplierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextSupplier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplier.setId(longCount.incrementAndGet());

        // Create the NextSupplier
        NextSupplierDTO nextSupplierDTO = nextSupplierMapper.toDto(nextSupplier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextSupplierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextSupplier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplier.setId(longCount.incrementAndGet());

        // Create the NextSupplier
        NextSupplierDTO nextSupplierDTO = nextSupplierMapper.toDto(nextSupplier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextSupplierDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextSupplier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextSupplier() throws Exception {
        // Initialize the database
        insertedNextSupplier = nextSupplierRepository.saveAndFlush(nextSupplier);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextSupplier
        restNextSupplierMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextSupplier.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextSupplierRepository.count();
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

    protected NextSupplier getPersistedNextSupplier(NextSupplier nextSupplier) {
        return nextSupplierRepository.findById(nextSupplier.getId()).orElseThrow();
    }

    protected void assertPersistedNextSupplierToMatchAllProperties(NextSupplier expectedNextSupplier) {
        assertNextSupplierAllPropertiesEquals(expectedNextSupplier, getPersistedNextSupplier(expectedNextSupplier));
    }

    protected void assertPersistedNextSupplierToMatchUpdatableProperties(NextSupplier expectedNextSupplier) {
        assertNextSupplierAllUpdatablePropertiesEquals(expectedNextSupplier, getPersistedNextSupplier(expectedNextSupplier));
    }
}
