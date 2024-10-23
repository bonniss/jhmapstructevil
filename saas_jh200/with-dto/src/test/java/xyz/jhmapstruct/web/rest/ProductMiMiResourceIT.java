package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.ProductMiMiAsserts.*;
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
import xyz.jhmapstruct.domain.CategoryMiMi;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.OrderMiMi;
import xyz.jhmapstruct.domain.ProductMiMi;
import xyz.jhmapstruct.domain.SupplierMiMi;
import xyz.jhmapstruct.repository.ProductMiMiRepository;
import xyz.jhmapstruct.service.ProductMiMiService;
import xyz.jhmapstruct.service.dto.ProductMiMiDTO;
import xyz.jhmapstruct.service.mapper.ProductMiMiMapper;

/**
 * Integration tests for the {@link ProductMiMiResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductMiMiResourceIT {

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

    private static final String ENTITY_API_URL = "/api/product-mi-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductMiMiRepository productMiMiRepository;

    @Mock
    private ProductMiMiRepository productMiMiRepositoryMock;

    @Autowired
    private ProductMiMiMapper productMiMiMapper;

    @Mock
    private ProductMiMiService productMiMiServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductMiMiMockMvc;

    private ProductMiMi productMiMi;

    private ProductMiMi insertedProductMiMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductMiMi createEntity() {
        return new ProductMiMi().name(DEFAULT_NAME).price(DEFAULT_PRICE).stock(DEFAULT_STOCK).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductMiMi createUpdatedEntity() {
        return new ProductMiMi().name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        productMiMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductMiMi != null) {
            productMiMiRepository.delete(insertedProductMiMi);
            insertedProductMiMi = null;
        }
    }

    @Test
    @Transactional
    void createProductMiMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductMiMi
        ProductMiMiDTO productMiMiDTO = productMiMiMapper.toDto(productMiMi);
        var returnedProductMiMiDTO = om.readValue(
            restProductMiMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productMiMiDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductMiMiDTO.class
        );

        // Validate the ProductMiMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProductMiMi = productMiMiMapper.toEntity(returnedProductMiMiDTO);
        assertProductMiMiUpdatableFieldsEquals(returnedProductMiMi, getPersistedProductMiMi(returnedProductMiMi));

        insertedProductMiMi = returnedProductMiMi;
    }

    @Test
    @Transactional
    void createProductMiMiWithExistingId() throws Exception {
        // Create the ProductMiMi with an existing ID
        productMiMi.setId(1L);
        ProductMiMiDTO productMiMiDTO = productMiMiMapper.toDto(productMiMi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productMiMiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productMiMi.setName(null);

        // Create the ProductMiMi, which fails.
        ProductMiMiDTO productMiMiDTO = productMiMiMapper.toDto(productMiMi);

        restProductMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productMiMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productMiMi.setPrice(null);

        // Create the ProductMiMi, which fails.
        ProductMiMiDTO productMiMiDTO = productMiMiMapper.toDto(productMiMi);

        restProductMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productMiMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStockIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productMiMi.setStock(null);

        // Create the ProductMiMi, which fails.
        ProductMiMiDTO productMiMiDTO = productMiMiMapper.toDto(productMiMi);

        restProductMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productMiMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductMiMis() throws Exception {
        // Initialize the database
        insertedProductMiMi = productMiMiRepository.saveAndFlush(productMiMi);

        // Get all the productMiMiList
        restProductMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productMiMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductMiMisWithEagerRelationshipsIsEnabled() throws Exception {
        when(productMiMiServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductMiMiMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productMiMiServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductMiMisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productMiMiServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductMiMiMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productMiMiRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProductMiMi() throws Exception {
        // Initialize the database
        insertedProductMiMi = productMiMiRepository.saveAndFlush(productMiMi);

        // Get the productMiMi
        restProductMiMiMockMvc
            .perform(get(ENTITY_API_URL_ID, productMiMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productMiMi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getProductMiMisByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductMiMi = productMiMiRepository.saveAndFlush(productMiMi);

        Long id = productMiMi.getId();

        defaultProductMiMiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductMiMiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductMiMiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductMiMisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductMiMi = productMiMiRepository.saveAndFlush(productMiMi);

        // Get all the productMiMiList where name equals to
        defaultProductMiMiFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductMiMisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductMiMi = productMiMiRepository.saveAndFlush(productMiMi);

        // Get all the productMiMiList where name in
        defaultProductMiMiFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductMiMisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductMiMi = productMiMiRepository.saveAndFlush(productMiMi);

        // Get all the productMiMiList where name is not null
        defaultProductMiMiFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllProductMiMisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedProductMiMi = productMiMiRepository.saveAndFlush(productMiMi);

        // Get all the productMiMiList where name contains
        defaultProductMiMiFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductMiMisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductMiMi = productMiMiRepository.saveAndFlush(productMiMi);

        // Get all the productMiMiList where name does not contain
        defaultProductMiMiFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllProductMiMisByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductMiMi = productMiMiRepository.saveAndFlush(productMiMi);

        // Get all the productMiMiList where price equals to
        defaultProductMiMiFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductMiMisByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductMiMi = productMiMiRepository.saveAndFlush(productMiMi);

        // Get all the productMiMiList where price in
        defaultProductMiMiFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductMiMisByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductMiMi = productMiMiRepository.saveAndFlush(productMiMi);

        // Get all the productMiMiList where price is not null
        defaultProductMiMiFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllProductMiMisByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductMiMi = productMiMiRepository.saveAndFlush(productMiMi);

        // Get all the productMiMiList where price is greater than or equal to
        defaultProductMiMiFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductMiMisByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductMiMi = productMiMiRepository.saveAndFlush(productMiMi);

        // Get all the productMiMiList where price is less than or equal to
        defaultProductMiMiFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllProductMiMisByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductMiMi = productMiMiRepository.saveAndFlush(productMiMi);

        // Get all the productMiMiList where price is less than
        defaultProductMiMiFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductMiMisByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductMiMi = productMiMiRepository.saveAndFlush(productMiMi);

        // Get all the productMiMiList where price is greater than
        defaultProductMiMiFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductMiMisByStockIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductMiMi = productMiMiRepository.saveAndFlush(productMiMi);

        // Get all the productMiMiList where stock equals to
        defaultProductMiMiFiltering("stock.equals=" + DEFAULT_STOCK, "stock.equals=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProductMiMisByStockIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductMiMi = productMiMiRepository.saveAndFlush(productMiMi);

        // Get all the productMiMiList where stock in
        defaultProductMiMiFiltering("stock.in=" + DEFAULT_STOCK + "," + UPDATED_STOCK, "stock.in=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProductMiMisByStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductMiMi = productMiMiRepository.saveAndFlush(productMiMi);

        // Get all the productMiMiList where stock is not null
        defaultProductMiMiFiltering("stock.specified=true", "stock.specified=false");
    }

    @Test
    @Transactional
    void getAllProductMiMisByStockIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductMiMi = productMiMiRepository.saveAndFlush(productMiMi);

        // Get all the productMiMiList where stock is greater than or equal to
        defaultProductMiMiFiltering("stock.greaterThanOrEqual=" + DEFAULT_STOCK, "stock.greaterThanOrEqual=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProductMiMisByStockIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductMiMi = productMiMiRepository.saveAndFlush(productMiMi);

        // Get all the productMiMiList where stock is less than or equal to
        defaultProductMiMiFiltering("stock.lessThanOrEqual=" + DEFAULT_STOCK, "stock.lessThanOrEqual=" + SMALLER_STOCK);
    }

    @Test
    @Transactional
    void getAllProductMiMisByStockIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductMiMi = productMiMiRepository.saveAndFlush(productMiMi);

        // Get all the productMiMiList where stock is less than
        defaultProductMiMiFiltering("stock.lessThan=" + UPDATED_STOCK, "stock.lessThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllProductMiMisByStockIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductMiMi = productMiMiRepository.saveAndFlush(productMiMi);

        // Get all the productMiMiList where stock is greater than
        defaultProductMiMiFiltering("stock.greaterThan=" + SMALLER_STOCK, "stock.greaterThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllProductMiMisByCategoryIsEqualToSomething() throws Exception {
        CategoryMiMi category;
        if (TestUtil.findAll(em, CategoryMiMi.class).isEmpty()) {
            productMiMiRepository.saveAndFlush(productMiMi);
            category = CategoryMiMiResourceIT.createEntity();
        } else {
            category = TestUtil.findAll(em, CategoryMiMi.class).get(0);
        }
        em.persist(category);
        em.flush();
        productMiMi.setCategory(category);
        productMiMiRepository.saveAndFlush(productMiMi);
        Long categoryId = category.getId();
        // Get all the productMiMiList where category equals to categoryId
        defaultProductMiMiShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the productMiMiList where category equals to (categoryId + 1)
        defaultProductMiMiShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllProductMiMisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            productMiMiRepository.saveAndFlush(productMiMi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        productMiMi.setTenant(tenant);
        productMiMiRepository.saveAndFlush(productMiMi);
        Long tenantId = tenant.getId();
        // Get all the productMiMiList where tenant equals to tenantId
        defaultProductMiMiShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the productMiMiList where tenant equals to (tenantId + 1)
        defaultProductMiMiShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllProductMiMisByOrderIsEqualToSomething() throws Exception {
        OrderMiMi order;
        if (TestUtil.findAll(em, OrderMiMi.class).isEmpty()) {
            productMiMiRepository.saveAndFlush(productMiMi);
            order = OrderMiMiResourceIT.createEntity();
        } else {
            order = TestUtil.findAll(em, OrderMiMi.class).get(0);
        }
        em.persist(order);
        em.flush();
        productMiMi.setOrder(order);
        productMiMiRepository.saveAndFlush(productMiMi);
        Long orderId = order.getId();
        // Get all the productMiMiList where order equals to orderId
        defaultProductMiMiShouldBeFound("orderId.equals=" + orderId);

        // Get all the productMiMiList where order equals to (orderId + 1)
        defaultProductMiMiShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllProductMiMisBySuppliersIsEqualToSomething() throws Exception {
        SupplierMiMi suppliers;
        if (TestUtil.findAll(em, SupplierMiMi.class).isEmpty()) {
            productMiMiRepository.saveAndFlush(productMiMi);
            suppliers = SupplierMiMiResourceIT.createEntity();
        } else {
            suppliers = TestUtil.findAll(em, SupplierMiMi.class).get(0);
        }
        em.persist(suppliers);
        em.flush();
        productMiMi.addSuppliers(suppliers);
        productMiMiRepository.saveAndFlush(productMiMi);
        Long suppliersId = suppliers.getId();
        // Get all the productMiMiList where suppliers equals to suppliersId
        defaultProductMiMiShouldBeFound("suppliersId.equals=" + suppliersId);

        // Get all the productMiMiList where suppliers equals to (suppliersId + 1)
        defaultProductMiMiShouldNotBeFound("suppliersId.equals=" + (suppliersId + 1));
    }

    private void defaultProductMiMiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductMiMiShouldBeFound(shouldBeFound);
        defaultProductMiMiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductMiMiShouldBeFound(String filter) throws Exception {
        restProductMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productMiMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restProductMiMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductMiMiShouldNotBeFound(String filter) throws Exception {
        restProductMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductMiMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductMiMi() throws Exception {
        // Get the productMiMi
        restProductMiMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductMiMi() throws Exception {
        // Initialize the database
        insertedProductMiMi = productMiMiRepository.saveAndFlush(productMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productMiMi
        ProductMiMi updatedProductMiMi = productMiMiRepository.findById(productMiMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductMiMi are not directly saved in db
        em.detach(updatedProductMiMi);
        updatedProductMiMi.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);
        ProductMiMiDTO productMiMiDTO = productMiMiMapper.toDto(updatedProductMiMi);

        restProductMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productMiMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productMiMiDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductMiMiToMatchAllProperties(updatedProductMiMi);
    }

    @Test
    @Transactional
    void putNonExistingProductMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productMiMi.setId(longCount.incrementAndGet());

        // Create the ProductMiMi
        ProductMiMiDTO productMiMiDTO = productMiMiMapper.toDto(productMiMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productMiMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productMiMi.setId(longCount.incrementAndGet());

        // Create the ProductMiMi
        ProductMiMiDTO productMiMiDTO = productMiMiMapper.toDto(productMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productMiMi.setId(longCount.incrementAndGet());

        // Create the ProductMiMi
        ProductMiMiDTO productMiMiDTO = productMiMiMapper.toDto(productMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMiMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productMiMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedProductMiMi = productMiMiRepository.saveAndFlush(productMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productMiMi using partial update
        ProductMiMi partialUpdatedProductMiMi = new ProductMiMi();
        partialUpdatedProductMiMi.setId(productMiMi.getId());

        partialUpdatedProductMiMi.price(UPDATED_PRICE).stock(UPDATED_STOCK);

        restProductMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductMiMi))
            )
            .andExpect(status().isOk());

        // Validate the ProductMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductMiMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductMiMi, productMiMi),
            getPersistedProductMiMi(productMiMi)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedProductMiMi = productMiMiRepository.saveAndFlush(productMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productMiMi using partial update
        ProductMiMi partialUpdatedProductMiMi = new ProductMiMi();
        partialUpdatedProductMiMi.setId(productMiMi.getId());

        partialUpdatedProductMiMi.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);

        restProductMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductMiMi))
            )
            .andExpect(status().isOk());

        // Validate the ProductMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductMiMiUpdatableFieldsEquals(partialUpdatedProductMiMi, getPersistedProductMiMi(partialUpdatedProductMiMi));
    }

    @Test
    @Transactional
    void patchNonExistingProductMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productMiMi.setId(longCount.incrementAndGet());

        // Create the ProductMiMi
        ProductMiMiDTO productMiMiDTO = productMiMiMapper.toDto(productMiMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productMiMiDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productMiMi.setId(longCount.incrementAndGet());

        // Create the ProductMiMi
        ProductMiMiDTO productMiMiDTO = productMiMiMapper.toDto(productMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productMiMi.setId(longCount.incrementAndGet());

        // Create the ProductMiMi
        ProductMiMiDTO productMiMiDTO = productMiMiMapper.toDto(productMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMiMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productMiMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductMiMi() throws Exception {
        // Initialize the database
        insertedProductMiMi = productMiMiRepository.saveAndFlush(productMiMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productMiMi
        restProductMiMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, productMiMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productMiMiRepository.count();
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

    protected ProductMiMi getPersistedProductMiMi(ProductMiMi productMiMi) {
        return productMiMiRepository.findById(productMiMi.getId()).orElseThrow();
    }

    protected void assertPersistedProductMiMiToMatchAllProperties(ProductMiMi expectedProductMiMi) {
        assertProductMiMiAllPropertiesEquals(expectedProductMiMi, getPersistedProductMiMi(expectedProductMiMi));
    }

    protected void assertPersistedProductMiMiToMatchUpdatableProperties(ProductMiMi expectedProductMiMi) {
        assertProductMiMiAllUpdatablePropertiesEquals(expectedProductMiMi, getPersistedProductMiMi(expectedProductMiMi));
    }
}
