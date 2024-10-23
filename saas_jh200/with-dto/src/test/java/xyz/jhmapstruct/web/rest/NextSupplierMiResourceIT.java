package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextSupplierMiAsserts.*;
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
import xyz.jhmapstruct.domain.NextSupplierMi;
import xyz.jhmapstruct.domain.ProductMi;
import xyz.jhmapstruct.repository.NextSupplierMiRepository;
import xyz.jhmapstruct.service.NextSupplierMiService;
import xyz.jhmapstruct.service.dto.NextSupplierMiDTO;
import xyz.jhmapstruct.service.mapper.NextSupplierMiMapper;

/**
 * Integration tests for the {@link NextSupplierMiResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextSupplierMiResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_PERSON = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_PERSON = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/next-supplier-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextSupplierMiRepository nextSupplierMiRepository;

    @Mock
    private NextSupplierMiRepository nextSupplierMiRepositoryMock;

    @Autowired
    private NextSupplierMiMapper nextSupplierMiMapper;

    @Mock
    private NextSupplierMiService nextSupplierMiServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextSupplierMiMockMvc;

    private NextSupplierMi nextSupplierMi;

    private NextSupplierMi insertedNextSupplierMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextSupplierMi createEntity() {
        return new NextSupplierMi()
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
    public static NextSupplierMi createUpdatedEntity() {
        return new NextSupplierMi()
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        nextSupplierMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextSupplierMi != null) {
            nextSupplierMiRepository.delete(insertedNextSupplierMi);
            insertedNextSupplierMi = null;
        }
    }

    @Test
    @Transactional
    void createNextSupplierMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextSupplierMi
        NextSupplierMiDTO nextSupplierMiDTO = nextSupplierMiMapper.toDto(nextSupplierMi);
        var returnedNextSupplierMiDTO = om.readValue(
            restNextSupplierMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierMiDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextSupplierMiDTO.class
        );

        // Validate the NextSupplierMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextSupplierMi = nextSupplierMiMapper.toEntity(returnedNextSupplierMiDTO);
        assertNextSupplierMiUpdatableFieldsEquals(returnedNextSupplierMi, getPersistedNextSupplierMi(returnedNextSupplierMi));

        insertedNextSupplierMi = returnedNextSupplierMi;
    }

    @Test
    @Transactional
    void createNextSupplierMiWithExistingId() throws Exception {
        // Create the NextSupplierMi with an existing ID
        nextSupplierMi.setId(1L);
        NextSupplierMiDTO nextSupplierMiDTO = nextSupplierMiMapper.toDto(nextSupplierMi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextSupplierMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierMiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextSupplierMi.setName(null);

        // Create the NextSupplierMi, which fails.
        NextSupplierMiDTO nextSupplierMiDTO = nextSupplierMiMapper.toDto(nextSupplierMi);

        restNextSupplierMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextSupplierMis() throws Exception {
        // Initialize the database
        insertedNextSupplierMi = nextSupplierMiRepository.saveAndFlush(nextSupplierMi);

        // Get all the nextSupplierMiList
        restNextSupplierMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextSupplierMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextSupplierMisWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextSupplierMiServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextSupplierMiMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextSupplierMiServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextSupplierMisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextSupplierMiServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextSupplierMiMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextSupplierMiRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextSupplierMi() throws Exception {
        // Initialize the database
        insertedNextSupplierMi = nextSupplierMiRepository.saveAndFlush(nextSupplierMi);

        // Get the nextSupplierMi
        restNextSupplierMiMockMvc
            .perform(get(ENTITY_API_URL_ID, nextSupplierMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextSupplierMi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.contactPerson").value(DEFAULT_CONTACT_PERSON))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getNextSupplierMisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextSupplierMi = nextSupplierMiRepository.saveAndFlush(nextSupplierMi);

        Long id = nextSupplierMi.getId();

        defaultNextSupplierMiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextSupplierMiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextSupplierMiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextSupplierMisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierMi = nextSupplierMiRepository.saveAndFlush(nextSupplierMi);

        // Get all the nextSupplierMiList where name equals to
        defaultNextSupplierMiFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextSupplierMisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplierMi = nextSupplierMiRepository.saveAndFlush(nextSupplierMi);

        // Get all the nextSupplierMiList where name in
        defaultNextSupplierMiFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextSupplierMisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplierMi = nextSupplierMiRepository.saveAndFlush(nextSupplierMi);

        // Get all the nextSupplierMiList where name is not null
        defaultNextSupplierMiFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSupplierMisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierMi = nextSupplierMiRepository.saveAndFlush(nextSupplierMi);

        // Get all the nextSupplierMiList where name contains
        defaultNextSupplierMiFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextSupplierMisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierMi = nextSupplierMiRepository.saveAndFlush(nextSupplierMi);

        // Get all the nextSupplierMiList where name does not contain
        defaultNextSupplierMiFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllNextSupplierMisByContactPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierMi = nextSupplierMiRepository.saveAndFlush(nextSupplierMi);

        // Get all the nextSupplierMiList where contactPerson equals to
        defaultNextSupplierMiFiltering("contactPerson.equals=" + DEFAULT_CONTACT_PERSON, "contactPerson.equals=" + UPDATED_CONTACT_PERSON);
    }

    @Test
    @Transactional
    void getAllNextSupplierMisByContactPersonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplierMi = nextSupplierMiRepository.saveAndFlush(nextSupplierMi);

        // Get all the nextSupplierMiList where contactPerson in
        defaultNextSupplierMiFiltering(
            "contactPerson.in=" + DEFAULT_CONTACT_PERSON + "," + UPDATED_CONTACT_PERSON,
            "contactPerson.in=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierMisByContactPersonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplierMi = nextSupplierMiRepository.saveAndFlush(nextSupplierMi);

        // Get all the nextSupplierMiList where contactPerson is not null
        defaultNextSupplierMiFiltering("contactPerson.specified=true", "contactPerson.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSupplierMisByContactPersonContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierMi = nextSupplierMiRepository.saveAndFlush(nextSupplierMi);

        // Get all the nextSupplierMiList where contactPerson contains
        defaultNextSupplierMiFiltering(
            "contactPerson.contains=" + DEFAULT_CONTACT_PERSON,
            "contactPerson.contains=" + UPDATED_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierMisByContactPersonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierMi = nextSupplierMiRepository.saveAndFlush(nextSupplierMi);

        // Get all the nextSupplierMiList where contactPerson does not contain
        defaultNextSupplierMiFiltering(
            "contactPerson.doesNotContain=" + UPDATED_CONTACT_PERSON,
            "contactPerson.doesNotContain=" + DEFAULT_CONTACT_PERSON
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierMisByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierMi = nextSupplierMiRepository.saveAndFlush(nextSupplierMi);

        // Get all the nextSupplierMiList where email equals to
        defaultNextSupplierMiFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSupplierMisByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplierMi = nextSupplierMiRepository.saveAndFlush(nextSupplierMi);

        // Get all the nextSupplierMiList where email in
        defaultNextSupplierMiFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSupplierMisByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplierMi = nextSupplierMiRepository.saveAndFlush(nextSupplierMi);

        // Get all the nextSupplierMiList where email is not null
        defaultNextSupplierMiFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSupplierMisByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierMi = nextSupplierMiRepository.saveAndFlush(nextSupplierMi);

        // Get all the nextSupplierMiList where email contains
        defaultNextSupplierMiFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSupplierMisByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierMi = nextSupplierMiRepository.saveAndFlush(nextSupplierMi);

        // Get all the nextSupplierMiList where email does not contain
        defaultNextSupplierMiFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllNextSupplierMisByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierMi = nextSupplierMiRepository.saveAndFlush(nextSupplierMi);

        // Get all the nextSupplierMiList where phoneNumber equals to
        defaultNextSupplierMiFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextSupplierMisByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextSupplierMi = nextSupplierMiRepository.saveAndFlush(nextSupplierMi);

        // Get all the nextSupplierMiList where phoneNumber in
        defaultNextSupplierMiFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierMisByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextSupplierMi = nextSupplierMiRepository.saveAndFlush(nextSupplierMi);

        // Get all the nextSupplierMiList where phoneNumber is not null
        defaultNextSupplierMiFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextSupplierMisByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierMi = nextSupplierMiRepository.saveAndFlush(nextSupplierMi);

        // Get all the nextSupplierMiList where phoneNumber contains
        defaultNextSupplierMiFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNextSupplierMisByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextSupplierMi = nextSupplierMiRepository.saveAndFlush(nextSupplierMi);

        // Get all the nextSupplierMiList where phoneNumber does not contain
        defaultNextSupplierMiFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextSupplierMisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextSupplierMiRepository.saveAndFlush(nextSupplierMi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextSupplierMi.setTenant(tenant);
        nextSupplierMiRepository.saveAndFlush(nextSupplierMi);
        Long tenantId = tenant.getId();
        // Get all the nextSupplierMiList where tenant equals to tenantId
        defaultNextSupplierMiShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextSupplierMiList where tenant equals to (tenantId + 1)
        defaultNextSupplierMiShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllNextSupplierMisByProductsIsEqualToSomething() throws Exception {
        ProductMi products;
        if (TestUtil.findAll(em, ProductMi.class).isEmpty()) {
            nextSupplierMiRepository.saveAndFlush(nextSupplierMi);
            products = ProductMiResourceIT.createEntity();
        } else {
            products = TestUtil.findAll(em, ProductMi.class).get(0);
        }
        em.persist(products);
        em.flush();
        nextSupplierMi.addProducts(products);
        nextSupplierMiRepository.saveAndFlush(nextSupplierMi);
        Long productsId = products.getId();
        // Get all the nextSupplierMiList where products equals to productsId
        defaultNextSupplierMiShouldBeFound("productsId.equals=" + productsId);

        // Get all the nextSupplierMiList where products equals to (productsId + 1)
        defaultNextSupplierMiShouldNotBeFound("productsId.equals=" + (productsId + 1));
    }

    private void defaultNextSupplierMiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextSupplierMiShouldBeFound(shouldBeFound);
        defaultNextSupplierMiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextSupplierMiShouldBeFound(String filter) throws Exception {
        restNextSupplierMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextSupplierMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restNextSupplierMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextSupplierMiShouldNotBeFound(String filter) throws Exception {
        restNextSupplierMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextSupplierMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextSupplierMi() throws Exception {
        // Get the nextSupplierMi
        restNextSupplierMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextSupplierMi() throws Exception {
        // Initialize the database
        insertedNextSupplierMi = nextSupplierMiRepository.saveAndFlush(nextSupplierMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextSupplierMi
        NextSupplierMi updatedNextSupplierMi = nextSupplierMiRepository.findById(nextSupplierMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextSupplierMi are not directly saved in db
        em.detach(updatedNextSupplierMi);
        updatedNextSupplierMi
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        NextSupplierMiDTO nextSupplierMiDTO = nextSupplierMiMapper.toDto(updatedNextSupplierMi);

        restNextSupplierMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextSupplierMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextSupplierMiDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextSupplierMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextSupplierMiToMatchAllProperties(updatedNextSupplierMi);
    }

    @Test
    @Transactional
    void putNonExistingNextSupplierMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierMi.setId(longCount.incrementAndGet());

        // Create the NextSupplierMi
        NextSupplierMiDTO nextSupplierMiDTO = nextSupplierMiMapper.toDto(nextSupplierMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextSupplierMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextSupplierMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextSupplierMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextSupplierMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierMi.setId(longCount.incrementAndGet());

        // Create the NextSupplierMi
        NextSupplierMiDTO nextSupplierMiDTO = nextSupplierMiMapper.toDto(nextSupplierMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextSupplierMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextSupplierMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierMi.setId(longCount.incrementAndGet());

        // Create the NextSupplierMi
        NextSupplierMiDTO nextSupplierMiDTO = nextSupplierMiMapper.toDto(nextSupplierMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextSupplierMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextSupplierMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextSupplierMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextSupplierMi = nextSupplierMiRepository.saveAndFlush(nextSupplierMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextSupplierMi using partial update
        NextSupplierMi partialUpdatedNextSupplierMi = new NextSupplierMi();
        partialUpdatedNextSupplierMi.setId(nextSupplierMi.getId());

        partialUpdatedNextSupplierMi.contactPerson(UPDATED_CONTACT_PERSON).phoneNumber(UPDATED_PHONE_NUMBER);

        restNextSupplierMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextSupplierMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextSupplierMi))
            )
            .andExpect(status().isOk());

        // Validate the NextSupplierMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextSupplierMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextSupplierMi, nextSupplierMi),
            getPersistedNextSupplierMi(nextSupplierMi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextSupplierMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextSupplierMi = nextSupplierMiRepository.saveAndFlush(nextSupplierMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextSupplierMi using partial update
        NextSupplierMi partialUpdatedNextSupplierMi = new NextSupplierMi();
        partialUpdatedNextSupplierMi.setId(nextSupplierMi.getId());

        partialUpdatedNextSupplierMi
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restNextSupplierMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextSupplierMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextSupplierMi))
            )
            .andExpect(status().isOk());

        // Validate the NextSupplierMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextSupplierMiUpdatableFieldsEquals(partialUpdatedNextSupplierMi, getPersistedNextSupplierMi(partialUpdatedNextSupplierMi));
    }

    @Test
    @Transactional
    void patchNonExistingNextSupplierMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierMi.setId(longCount.incrementAndGet());

        // Create the NextSupplierMi
        NextSupplierMiDTO nextSupplierMiDTO = nextSupplierMiMapper.toDto(nextSupplierMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextSupplierMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextSupplierMiDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextSupplierMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextSupplierMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierMi.setId(longCount.incrementAndGet());

        // Create the NextSupplierMi
        NextSupplierMiDTO nextSupplierMiDTO = nextSupplierMiMapper.toDto(nextSupplierMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextSupplierMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextSupplierMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextSupplierMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextSupplierMi.setId(longCount.incrementAndGet());

        // Create the NextSupplierMi
        NextSupplierMiDTO nextSupplierMiDTO = nextSupplierMiMapper.toDto(nextSupplierMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextSupplierMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextSupplierMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextSupplierMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextSupplierMi() throws Exception {
        // Initialize the database
        insertedNextSupplierMi = nextSupplierMiRepository.saveAndFlush(nextSupplierMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextSupplierMi
        restNextSupplierMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextSupplierMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextSupplierMiRepository.count();
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

    protected NextSupplierMi getPersistedNextSupplierMi(NextSupplierMi nextSupplierMi) {
        return nextSupplierMiRepository.findById(nextSupplierMi.getId()).orElseThrow();
    }

    protected void assertPersistedNextSupplierMiToMatchAllProperties(NextSupplierMi expectedNextSupplierMi) {
        assertNextSupplierMiAllPropertiesEquals(expectedNextSupplierMi, getPersistedNextSupplierMi(expectedNextSupplierMi));
    }

    protected void assertPersistedNextSupplierMiToMatchUpdatableProperties(NextSupplierMi expectedNextSupplierMi) {
        assertNextSupplierMiAllUpdatablePropertiesEquals(expectedNextSupplierMi, getPersistedNextSupplierMi(expectedNextSupplierMi));
    }
}
