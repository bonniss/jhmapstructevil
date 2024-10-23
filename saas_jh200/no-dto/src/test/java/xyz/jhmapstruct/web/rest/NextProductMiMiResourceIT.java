package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextProductMiMiAsserts.*;
import static xyz.jhmapstruct.web.rest.TestUtil.createUpdateProxyForBean;
import static xyz.jhmapstruct.web.rest.TestUtil.sameNumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
import xyz.jhmapstruct.domain.NextCategoryMiMi;
import xyz.jhmapstruct.domain.NextOrderMiMi;
import xyz.jhmapstruct.domain.NextProductMiMi;
import xyz.jhmapstruct.domain.NextSupplierMiMi;
import xyz.jhmapstruct.repository.NextProductMiMiRepository;
import xyz.jhmapstruct.service.NextProductMiMiService;

/**
 * Integration tests for the {@link NextProductMiMiResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextProductMiMiResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRICE = new BigDecimal(1 - 1);

    private static final Integer DEFAULT_STOCK = 1;
    private static final Integer UPDATED_STOCK = 2;
    private static final Integer SMALLER_STOCK = 1 - 1;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/next-product-mi-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextProductMiMiRepository nextProductMiMiRepository;

    @Mock
    private NextProductMiMiRepository nextProductMiMiRepositoryMock;

    @Mock
    private NextProductMiMiService nextProductMiMiServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextProductMiMiMockMvc;

    private NextProductMiMi nextProductMiMi;

    private NextProductMiMi insertedNextProductMiMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextProductMiMi createEntity() {
        return new NextProductMiMi().name(DEFAULT_NAME).price(DEFAULT_PRICE).stock(DEFAULT_STOCK).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextProductMiMi createUpdatedEntity() {
        return new NextProductMiMi().name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        nextProductMiMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextProductMiMi != null) {
            nextProductMiMiRepository.delete(insertedNextProductMiMi);
            insertedNextProductMiMi = null;
        }
    }

    @Test
    @Transactional
    void createNextProductMiMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextProductMiMi
        var returnedNextProductMiMi = om.readValue(
            restNextProductMiMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductMiMi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextProductMiMi.class
        );

        // Validate the NextProductMiMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextProductMiMiUpdatableFieldsEquals(returnedNextProductMiMi, getPersistedNextProductMiMi(returnedNextProductMiMi));

        insertedNextProductMiMi = returnedNextProductMiMi;
    }

    @Test
    @Transactional
    void createNextProductMiMiWithExistingId() throws Exception {
        // Create the NextProductMiMi with an existing ID
        nextProductMiMi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextProductMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductMiMi)))
            .andExpect(status().isBadRequest());

        // Validate the NextProductMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextProductMiMi.setName(null);

        // Create the NextProductMiMi, which fails.

        restNextProductMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductMiMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextProductMiMi.setPrice(null);

        // Create the NextProductMiMi, which fails.

        restNextProductMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductMiMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStockIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextProductMiMi.setStock(null);

        // Create the NextProductMiMi, which fails.

        restNextProductMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductMiMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextProductMiMis() throws Exception {
        // Initialize the database
        insertedNextProductMiMi = nextProductMiMiRepository.saveAndFlush(nextProductMiMi);

        // Get all the nextProductMiMiList
        restNextProductMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextProductMiMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextProductMiMisWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextProductMiMiServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextProductMiMiMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextProductMiMiServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextProductMiMisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextProductMiMiServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextProductMiMiMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextProductMiMiRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextProductMiMi() throws Exception {
        // Initialize the database
        insertedNextProductMiMi = nextProductMiMiRepository.saveAndFlush(nextProductMiMi);

        // Get the nextProductMiMi
        restNextProductMiMiMockMvc
            .perform(get(ENTITY_API_URL_ID, nextProductMiMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextProductMiMi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getNextProductMiMisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextProductMiMi = nextProductMiMiRepository.saveAndFlush(nextProductMiMi);

        Long id = nextProductMiMi.getId();

        defaultNextProductMiMiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextProductMiMiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextProductMiMiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextProductMiMisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductMiMi = nextProductMiMiRepository.saveAndFlush(nextProductMiMi);

        // Get all the nextProductMiMiList where name equals to
        defaultNextProductMiMiFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductMiMisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextProductMiMi = nextProductMiMiRepository.saveAndFlush(nextProductMiMi);

        // Get all the nextProductMiMiList where name in
        defaultNextProductMiMiFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductMiMisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextProductMiMi = nextProductMiMiRepository.saveAndFlush(nextProductMiMi);

        // Get all the nextProductMiMiList where name is not null
        defaultNextProductMiMiFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllNextProductMiMisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextProductMiMi = nextProductMiMiRepository.saveAndFlush(nextProductMiMi);

        // Get all the nextProductMiMiList where name contains
        defaultNextProductMiMiFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductMiMisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextProductMiMi = nextProductMiMiRepository.saveAndFlush(nextProductMiMi);

        // Get all the nextProductMiMiList where name does not contain
        defaultNextProductMiMiFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductMiMisByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductMiMi = nextProductMiMiRepository.saveAndFlush(nextProductMiMi);

        // Get all the nextProductMiMiList where price equals to
        defaultNextProductMiMiFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductMiMisByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextProductMiMi = nextProductMiMiRepository.saveAndFlush(nextProductMiMi);

        // Get all the nextProductMiMiList where price in
        defaultNextProductMiMiFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductMiMisByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextProductMiMi = nextProductMiMiRepository.saveAndFlush(nextProductMiMi);

        // Get all the nextProductMiMiList where price is not null
        defaultNextProductMiMiFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllNextProductMiMisByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductMiMi = nextProductMiMiRepository.saveAndFlush(nextProductMiMi);

        // Get all the nextProductMiMiList where price is greater than or equal to
        defaultNextProductMiMiFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductMiMisByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductMiMi = nextProductMiMiRepository.saveAndFlush(nextProductMiMi);

        // Get all the nextProductMiMiList where price is less than or equal to
        defaultNextProductMiMiFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductMiMisByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextProductMiMi = nextProductMiMiRepository.saveAndFlush(nextProductMiMi);

        // Get all the nextProductMiMiList where price is less than
        defaultNextProductMiMiFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductMiMisByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextProductMiMi = nextProductMiMiRepository.saveAndFlush(nextProductMiMi);

        // Get all the nextProductMiMiList where price is greater than
        defaultNextProductMiMiFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductMiMisByStockIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductMiMi = nextProductMiMiRepository.saveAndFlush(nextProductMiMi);

        // Get all the nextProductMiMiList where stock equals to
        defaultNextProductMiMiFiltering("stock.equals=" + DEFAULT_STOCK, "stock.equals=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductMiMisByStockIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextProductMiMi = nextProductMiMiRepository.saveAndFlush(nextProductMiMi);

        // Get all the nextProductMiMiList where stock in
        defaultNextProductMiMiFiltering("stock.in=" + DEFAULT_STOCK + "," + UPDATED_STOCK, "stock.in=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductMiMisByStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextProductMiMi = nextProductMiMiRepository.saveAndFlush(nextProductMiMi);

        // Get all the nextProductMiMiList where stock is not null
        defaultNextProductMiMiFiltering("stock.specified=true", "stock.specified=false");
    }

    @Test
    @Transactional
    void getAllNextProductMiMisByStockIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductMiMi = nextProductMiMiRepository.saveAndFlush(nextProductMiMi);

        // Get all the nextProductMiMiList where stock is greater than or equal to
        defaultNextProductMiMiFiltering("stock.greaterThanOrEqual=" + DEFAULT_STOCK, "stock.greaterThanOrEqual=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductMiMisByStockIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductMiMi = nextProductMiMiRepository.saveAndFlush(nextProductMiMi);

        // Get all the nextProductMiMiList where stock is less than or equal to
        defaultNextProductMiMiFiltering("stock.lessThanOrEqual=" + DEFAULT_STOCK, "stock.lessThanOrEqual=" + SMALLER_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductMiMisByStockIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextProductMiMi = nextProductMiMiRepository.saveAndFlush(nextProductMiMi);

        // Get all the nextProductMiMiList where stock is less than
        defaultNextProductMiMiFiltering("stock.lessThan=" + UPDATED_STOCK, "stock.lessThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductMiMisByStockIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextProductMiMi = nextProductMiMiRepository.saveAndFlush(nextProductMiMi);

        // Get all the nextProductMiMiList where stock is greater than
        defaultNextProductMiMiFiltering("stock.greaterThan=" + SMALLER_STOCK, "stock.greaterThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductMiMisByCategoryIsEqualToSomething() throws Exception {
        NextCategoryMiMi category;
        if (TestUtil.findAll(em, NextCategoryMiMi.class).isEmpty()) {
            nextProductMiMiRepository.saveAndFlush(nextProductMiMi);
            category = NextCategoryMiMiResourceIT.createEntity();
        } else {
            category = TestUtil.findAll(em, NextCategoryMiMi.class).get(0);
        }
        em.persist(category);
        em.flush();
        nextProductMiMi.setCategory(category);
        nextProductMiMiRepository.saveAndFlush(nextProductMiMi);
        Long categoryId = category.getId();
        // Get all the nextProductMiMiList where category equals to categoryId
        defaultNextProductMiMiShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the nextProductMiMiList where category equals to (categoryId + 1)
        defaultNextProductMiMiShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllNextProductMiMisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextProductMiMiRepository.saveAndFlush(nextProductMiMi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextProductMiMi.setTenant(tenant);
        nextProductMiMiRepository.saveAndFlush(nextProductMiMi);
        Long tenantId = tenant.getId();
        // Get all the nextProductMiMiList where tenant equals to tenantId
        defaultNextProductMiMiShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextProductMiMiList where tenant equals to (tenantId + 1)
        defaultNextProductMiMiShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllNextProductMiMisByOrderIsEqualToSomething() throws Exception {
        NextOrderMiMi order;
        if (TestUtil.findAll(em, NextOrderMiMi.class).isEmpty()) {
            nextProductMiMiRepository.saveAndFlush(nextProductMiMi);
            order = NextOrderMiMiResourceIT.createEntity();
        } else {
            order = TestUtil.findAll(em, NextOrderMiMi.class).get(0);
        }
        em.persist(order);
        em.flush();
        nextProductMiMi.setOrder(order);
        nextProductMiMiRepository.saveAndFlush(nextProductMiMi);
        Long orderId = order.getId();
        // Get all the nextProductMiMiList where order equals to orderId
        defaultNextProductMiMiShouldBeFound("orderId.equals=" + orderId);

        // Get all the nextProductMiMiList where order equals to (orderId + 1)
        defaultNextProductMiMiShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllNextProductMiMisBySuppliersIsEqualToSomething() throws Exception {
        NextSupplierMiMi suppliers;
        if (TestUtil.findAll(em, NextSupplierMiMi.class).isEmpty()) {
            nextProductMiMiRepository.saveAndFlush(nextProductMiMi);
            suppliers = NextSupplierMiMiResourceIT.createEntity();
        } else {
            suppliers = TestUtil.findAll(em, NextSupplierMiMi.class).get(0);
        }
        em.persist(suppliers);
        em.flush();
        nextProductMiMi.addSuppliers(suppliers);
        nextProductMiMiRepository.saveAndFlush(nextProductMiMi);
        Long suppliersId = suppliers.getId();
        // Get all the nextProductMiMiList where suppliers equals to suppliersId
        defaultNextProductMiMiShouldBeFound("suppliersId.equals=" + suppliersId);

        // Get all the nextProductMiMiList where suppliers equals to (suppliersId + 1)
        defaultNextProductMiMiShouldNotBeFound("suppliersId.equals=" + (suppliersId + 1));
    }

    private void defaultNextProductMiMiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextProductMiMiShouldBeFound(shouldBeFound);
        defaultNextProductMiMiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextProductMiMiShouldBeFound(String filter) throws Exception {
        restNextProductMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextProductMiMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restNextProductMiMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextProductMiMiShouldNotBeFound(String filter) throws Exception {
        restNextProductMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextProductMiMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextProductMiMi() throws Exception {
        // Get the nextProductMiMi
        restNextProductMiMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextProductMiMi() throws Exception {
        // Initialize the database
        insertedNextProductMiMi = nextProductMiMiRepository.saveAndFlush(nextProductMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextProductMiMi
        NextProductMiMi updatedNextProductMiMi = nextProductMiMiRepository.findById(nextProductMiMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextProductMiMi are not directly saved in db
        em.detach(updatedNextProductMiMi);
        updatedNextProductMiMi.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);

        restNextProductMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextProductMiMi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextProductMiMi))
            )
            .andExpect(status().isOk());

        // Validate the NextProductMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextProductMiMiToMatchAllProperties(updatedNextProductMiMi);
    }

    @Test
    @Transactional
    void putNonExistingNextProductMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductMiMi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextProductMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextProductMiMi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextProductMiMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProductMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextProductMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductMiMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextProductMiMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProductMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextProductMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductMiMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductMiMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductMiMi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextProductMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextProductMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextProductMiMi = nextProductMiMiRepository.saveAndFlush(nextProductMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextProductMiMi using partial update
        NextProductMiMi partialUpdatedNextProductMiMi = new NextProductMiMi();
        partialUpdatedNextProductMiMi.setId(nextProductMiMi.getId());

        partialUpdatedNextProductMiMi.name(UPDATED_NAME).stock(UPDATED_STOCK);

        restNextProductMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextProductMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextProductMiMi))
            )
            .andExpect(status().isOk());

        // Validate the NextProductMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextProductMiMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextProductMiMi, nextProductMiMi),
            getPersistedNextProductMiMi(nextProductMiMi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextProductMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextProductMiMi = nextProductMiMiRepository.saveAndFlush(nextProductMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextProductMiMi using partial update
        NextProductMiMi partialUpdatedNextProductMiMi = new NextProductMiMi();
        partialUpdatedNextProductMiMi.setId(nextProductMiMi.getId());

        partialUpdatedNextProductMiMi.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);

        restNextProductMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextProductMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextProductMiMi))
            )
            .andExpect(status().isOk());

        // Validate the NextProductMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextProductMiMiUpdatableFieldsEquals(
            partialUpdatedNextProductMiMi,
            getPersistedNextProductMiMi(partialUpdatedNextProductMiMi)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextProductMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductMiMi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextProductMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextProductMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextProductMiMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProductMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextProductMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductMiMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextProductMiMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProductMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextProductMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductMiMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductMiMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextProductMiMi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextProductMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextProductMiMi() throws Exception {
        // Initialize the database
        insertedNextProductMiMi = nextProductMiMiRepository.saveAndFlush(nextProductMiMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextProductMiMi
        restNextProductMiMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextProductMiMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextProductMiMiRepository.count();
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

    protected NextProductMiMi getPersistedNextProductMiMi(NextProductMiMi nextProductMiMi) {
        return nextProductMiMiRepository.findById(nextProductMiMi.getId()).orElseThrow();
    }

    protected void assertPersistedNextProductMiMiToMatchAllProperties(NextProductMiMi expectedNextProductMiMi) {
        assertNextProductMiMiAllPropertiesEquals(expectedNextProductMiMi, getPersistedNextProductMiMi(expectedNextProductMiMi));
    }

    protected void assertPersistedNextProductMiMiToMatchUpdatableProperties(NextProductMiMi expectedNextProductMiMi) {
        assertNextProductMiMiAllUpdatablePropertiesEquals(expectedNextProductMiMi, getPersistedNextProductMiMi(expectedNextProductMiMi));
    }
}
