package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.ProductSigmaAsserts.*;
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
import xyz.jhmapstruct.domain.CategorySigma;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.OrderSigma;
import xyz.jhmapstruct.domain.ProductSigma;
import xyz.jhmapstruct.domain.SupplierSigma;
import xyz.jhmapstruct.repository.ProductSigmaRepository;
import xyz.jhmapstruct.service.ProductSigmaService;

/**
 * Integration tests for the {@link ProductSigmaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductSigmaResourceIT {

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

    private static final String ENTITY_API_URL = "/api/product-sigmas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductSigmaRepository productSigmaRepository;

    @Mock
    private ProductSigmaRepository productSigmaRepositoryMock;

    @Mock
    private ProductSigmaService productSigmaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductSigmaMockMvc;

    private ProductSigma productSigma;

    private ProductSigma insertedProductSigma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductSigma createEntity() {
        return new ProductSigma().name(DEFAULT_NAME).price(DEFAULT_PRICE).stock(DEFAULT_STOCK).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductSigma createUpdatedEntity() {
        return new ProductSigma().name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        productSigma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductSigma != null) {
            productSigmaRepository.delete(insertedProductSigma);
            insertedProductSigma = null;
        }
    }

    @Test
    @Transactional
    void createProductSigma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductSigma
        var returnedProductSigma = om.readValue(
            restProductSigmaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productSigma)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductSigma.class
        );

        // Validate the ProductSigma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductSigmaUpdatableFieldsEquals(returnedProductSigma, getPersistedProductSigma(returnedProductSigma));

        insertedProductSigma = returnedProductSigma;
    }

    @Test
    @Transactional
    void createProductSigmaWithExistingId() throws Exception {
        // Create the ProductSigma with an existing ID
        productSigma.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productSigma)))
            .andExpect(status().isBadRequest());

        // Validate the ProductSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productSigma.setName(null);

        // Create the ProductSigma, which fails.

        restProductSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productSigma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productSigma.setPrice(null);

        // Create the ProductSigma, which fails.

        restProductSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productSigma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStockIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productSigma.setStock(null);

        // Create the ProductSigma, which fails.

        restProductSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productSigma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductSigmas() throws Exception {
        // Initialize the database
        insertedProductSigma = productSigmaRepository.saveAndFlush(productSigma);

        // Get all the productSigmaList
        restProductSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productSigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductSigmasWithEagerRelationshipsIsEnabled() throws Exception {
        when(productSigmaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductSigmaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productSigmaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductSigmasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productSigmaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductSigmaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productSigmaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProductSigma() throws Exception {
        // Initialize the database
        insertedProductSigma = productSigmaRepository.saveAndFlush(productSigma);

        // Get the productSigma
        restProductSigmaMockMvc
            .perform(get(ENTITY_API_URL_ID, productSigma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productSigma.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getProductSigmasByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductSigma = productSigmaRepository.saveAndFlush(productSigma);

        Long id = productSigma.getId();

        defaultProductSigmaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductSigmaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductSigmaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductSigmasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductSigma = productSigmaRepository.saveAndFlush(productSigma);

        // Get all the productSigmaList where name equals to
        defaultProductSigmaFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductSigmasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductSigma = productSigmaRepository.saveAndFlush(productSigma);

        // Get all the productSigmaList where name in
        defaultProductSigmaFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductSigmasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductSigma = productSigmaRepository.saveAndFlush(productSigma);

        // Get all the productSigmaList where name is not null
        defaultProductSigmaFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllProductSigmasByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedProductSigma = productSigmaRepository.saveAndFlush(productSigma);

        // Get all the productSigmaList where name contains
        defaultProductSigmaFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductSigmasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductSigma = productSigmaRepository.saveAndFlush(productSigma);

        // Get all the productSigmaList where name does not contain
        defaultProductSigmaFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllProductSigmasByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductSigma = productSigmaRepository.saveAndFlush(productSigma);

        // Get all the productSigmaList where price equals to
        defaultProductSigmaFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductSigmasByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductSigma = productSigmaRepository.saveAndFlush(productSigma);

        // Get all the productSigmaList where price in
        defaultProductSigmaFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductSigmasByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductSigma = productSigmaRepository.saveAndFlush(productSigma);

        // Get all the productSigmaList where price is not null
        defaultProductSigmaFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllProductSigmasByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductSigma = productSigmaRepository.saveAndFlush(productSigma);

        // Get all the productSigmaList where price is greater than or equal to
        defaultProductSigmaFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductSigmasByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductSigma = productSigmaRepository.saveAndFlush(productSigma);

        // Get all the productSigmaList where price is less than or equal to
        defaultProductSigmaFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllProductSigmasByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductSigma = productSigmaRepository.saveAndFlush(productSigma);

        // Get all the productSigmaList where price is less than
        defaultProductSigmaFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductSigmasByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductSigma = productSigmaRepository.saveAndFlush(productSigma);

        // Get all the productSigmaList where price is greater than
        defaultProductSigmaFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductSigmasByStockIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductSigma = productSigmaRepository.saveAndFlush(productSigma);

        // Get all the productSigmaList where stock equals to
        defaultProductSigmaFiltering("stock.equals=" + DEFAULT_STOCK, "stock.equals=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProductSigmasByStockIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductSigma = productSigmaRepository.saveAndFlush(productSigma);

        // Get all the productSigmaList where stock in
        defaultProductSigmaFiltering("stock.in=" + DEFAULT_STOCK + "," + UPDATED_STOCK, "stock.in=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProductSigmasByStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductSigma = productSigmaRepository.saveAndFlush(productSigma);

        // Get all the productSigmaList where stock is not null
        defaultProductSigmaFiltering("stock.specified=true", "stock.specified=false");
    }

    @Test
    @Transactional
    void getAllProductSigmasByStockIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductSigma = productSigmaRepository.saveAndFlush(productSigma);

        // Get all the productSigmaList where stock is greater than or equal to
        defaultProductSigmaFiltering("stock.greaterThanOrEqual=" + DEFAULT_STOCK, "stock.greaterThanOrEqual=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProductSigmasByStockIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductSigma = productSigmaRepository.saveAndFlush(productSigma);

        // Get all the productSigmaList where stock is less than or equal to
        defaultProductSigmaFiltering("stock.lessThanOrEqual=" + DEFAULT_STOCK, "stock.lessThanOrEqual=" + SMALLER_STOCK);
    }

    @Test
    @Transactional
    void getAllProductSigmasByStockIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductSigma = productSigmaRepository.saveAndFlush(productSigma);

        // Get all the productSigmaList where stock is less than
        defaultProductSigmaFiltering("stock.lessThan=" + UPDATED_STOCK, "stock.lessThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllProductSigmasByStockIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductSigma = productSigmaRepository.saveAndFlush(productSigma);

        // Get all the productSigmaList where stock is greater than
        defaultProductSigmaFiltering("stock.greaterThan=" + SMALLER_STOCK, "stock.greaterThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllProductSigmasByCategoryIsEqualToSomething() throws Exception {
        CategorySigma category;
        if (TestUtil.findAll(em, CategorySigma.class).isEmpty()) {
            productSigmaRepository.saveAndFlush(productSigma);
            category = CategorySigmaResourceIT.createEntity();
        } else {
            category = TestUtil.findAll(em, CategorySigma.class).get(0);
        }
        em.persist(category);
        em.flush();
        productSigma.setCategory(category);
        productSigmaRepository.saveAndFlush(productSigma);
        Long categoryId = category.getId();
        // Get all the productSigmaList where category equals to categoryId
        defaultProductSigmaShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the productSigmaList where category equals to (categoryId + 1)
        defaultProductSigmaShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllProductSigmasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            productSigmaRepository.saveAndFlush(productSigma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        productSigma.setTenant(tenant);
        productSigmaRepository.saveAndFlush(productSigma);
        Long tenantId = tenant.getId();
        // Get all the productSigmaList where tenant equals to tenantId
        defaultProductSigmaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the productSigmaList where tenant equals to (tenantId + 1)
        defaultProductSigmaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllProductSigmasByOrderIsEqualToSomething() throws Exception {
        OrderSigma order;
        if (TestUtil.findAll(em, OrderSigma.class).isEmpty()) {
            productSigmaRepository.saveAndFlush(productSigma);
            order = OrderSigmaResourceIT.createEntity();
        } else {
            order = TestUtil.findAll(em, OrderSigma.class).get(0);
        }
        em.persist(order);
        em.flush();
        productSigma.setOrder(order);
        productSigmaRepository.saveAndFlush(productSigma);
        Long orderId = order.getId();
        // Get all the productSigmaList where order equals to orderId
        defaultProductSigmaShouldBeFound("orderId.equals=" + orderId);

        // Get all the productSigmaList where order equals to (orderId + 1)
        defaultProductSigmaShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllProductSigmasBySuppliersIsEqualToSomething() throws Exception {
        SupplierSigma suppliers;
        if (TestUtil.findAll(em, SupplierSigma.class).isEmpty()) {
            productSigmaRepository.saveAndFlush(productSigma);
            suppliers = SupplierSigmaResourceIT.createEntity();
        } else {
            suppliers = TestUtil.findAll(em, SupplierSigma.class).get(0);
        }
        em.persist(suppliers);
        em.flush();
        productSigma.addSuppliers(suppliers);
        productSigmaRepository.saveAndFlush(productSigma);
        Long suppliersId = suppliers.getId();
        // Get all the productSigmaList where suppliers equals to suppliersId
        defaultProductSigmaShouldBeFound("suppliersId.equals=" + suppliersId);

        // Get all the productSigmaList where suppliers equals to (suppliersId + 1)
        defaultProductSigmaShouldNotBeFound("suppliersId.equals=" + (suppliersId + 1));
    }

    private void defaultProductSigmaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductSigmaShouldBeFound(shouldBeFound);
        defaultProductSigmaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductSigmaShouldBeFound(String filter) throws Exception {
        restProductSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productSigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restProductSigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductSigmaShouldNotBeFound(String filter) throws Exception {
        restProductSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductSigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductSigma() throws Exception {
        // Get the productSigma
        restProductSigmaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductSigma() throws Exception {
        // Initialize the database
        insertedProductSigma = productSigmaRepository.saveAndFlush(productSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productSigma
        ProductSigma updatedProductSigma = productSigmaRepository.findById(productSigma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductSigma are not directly saved in db
        em.detach(updatedProductSigma);
        updatedProductSigma.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);

        restProductSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductSigma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductSigma))
            )
            .andExpect(status().isOk());

        // Validate the ProductSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductSigmaToMatchAllProperties(updatedProductSigma);
    }

    @Test
    @Transactional
    void putNonExistingProductSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productSigma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productSigma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productSigma))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productSigma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productSigma))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productSigma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductSigmaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productSigma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductSigmaWithPatch() throws Exception {
        // Initialize the database
        insertedProductSigma = productSigmaRepository.saveAndFlush(productSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productSigma using partial update
        ProductSigma partialUpdatedProductSigma = new ProductSigma();
        partialUpdatedProductSigma.setId(productSigma.getId());

        partialUpdatedProductSigma.price(UPDATED_PRICE).description(UPDATED_DESCRIPTION);

        restProductSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductSigma))
            )
            .andExpect(status().isOk());

        // Validate the ProductSigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductSigmaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductSigma, productSigma),
            getPersistedProductSigma(productSigma)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductSigmaWithPatch() throws Exception {
        // Initialize the database
        insertedProductSigma = productSigmaRepository.saveAndFlush(productSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productSigma using partial update
        ProductSigma partialUpdatedProductSigma = new ProductSigma();
        partialUpdatedProductSigma.setId(productSigma.getId());

        partialUpdatedProductSigma.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);

        restProductSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductSigma))
            )
            .andExpect(status().isOk());

        // Validate the ProductSigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductSigmaUpdatableFieldsEquals(partialUpdatedProductSigma, getPersistedProductSigma(partialUpdatedProductSigma));
    }

    @Test
    @Transactional
    void patchNonExistingProductSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productSigma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productSigma))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productSigma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productSigma))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productSigma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductSigmaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productSigma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductSigma() throws Exception {
        // Initialize the database
        insertedProductSigma = productSigmaRepository.saveAndFlush(productSigma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productSigma
        restProductSigmaMockMvc
            .perform(delete(ENTITY_API_URL_ID, productSigma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productSigmaRepository.count();
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

    protected ProductSigma getPersistedProductSigma(ProductSigma productSigma) {
        return productSigmaRepository.findById(productSigma.getId()).orElseThrow();
    }

    protected void assertPersistedProductSigmaToMatchAllProperties(ProductSigma expectedProductSigma) {
        assertProductSigmaAllPropertiesEquals(expectedProductSigma, getPersistedProductSigma(expectedProductSigma));
    }

    protected void assertPersistedProductSigmaToMatchUpdatableProperties(ProductSigma expectedProductSigma) {
        assertProductSigmaAllUpdatablePropertiesEquals(expectedProductSigma, getPersistedProductSigma(expectedProductSigma));
    }
}
