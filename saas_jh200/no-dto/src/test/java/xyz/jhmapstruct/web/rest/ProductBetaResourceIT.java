package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.ProductBetaAsserts.*;
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
import xyz.jhmapstruct.domain.CategoryBeta;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.OrderBeta;
import xyz.jhmapstruct.domain.ProductBeta;
import xyz.jhmapstruct.domain.SupplierBeta;
import xyz.jhmapstruct.repository.ProductBetaRepository;
import xyz.jhmapstruct.service.ProductBetaService;

/**
 * Integration tests for the {@link ProductBetaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductBetaResourceIT {

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

    private static final String ENTITY_API_URL = "/api/product-betas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductBetaRepository productBetaRepository;

    @Mock
    private ProductBetaRepository productBetaRepositoryMock;

    @Mock
    private ProductBetaService productBetaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductBetaMockMvc;

    private ProductBeta productBeta;

    private ProductBeta insertedProductBeta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductBeta createEntity() {
        return new ProductBeta().name(DEFAULT_NAME).price(DEFAULT_PRICE).stock(DEFAULT_STOCK).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductBeta createUpdatedEntity() {
        return new ProductBeta().name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        productBeta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductBeta != null) {
            productBetaRepository.delete(insertedProductBeta);
            insertedProductBeta = null;
        }
    }

    @Test
    @Transactional
    void createProductBeta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductBeta
        var returnedProductBeta = om.readValue(
            restProductBetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productBeta)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductBeta.class
        );

        // Validate the ProductBeta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductBetaUpdatableFieldsEquals(returnedProductBeta, getPersistedProductBeta(returnedProductBeta));

        insertedProductBeta = returnedProductBeta;
    }

    @Test
    @Transactional
    void createProductBetaWithExistingId() throws Exception {
        // Create the ProductBeta with an existing ID
        productBeta.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productBeta)))
            .andExpect(status().isBadRequest());

        // Validate the ProductBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productBeta.setName(null);

        // Create the ProductBeta, which fails.

        restProductBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productBeta)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productBeta.setPrice(null);

        // Create the ProductBeta, which fails.

        restProductBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productBeta)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStockIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productBeta.setStock(null);

        // Create the ProductBeta, which fails.

        restProductBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productBeta)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductBetas() throws Exception {
        // Initialize the database
        insertedProductBeta = productBetaRepository.saveAndFlush(productBeta);

        // Get all the productBetaList
        restProductBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductBetasWithEagerRelationshipsIsEnabled() throws Exception {
        when(productBetaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductBetaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productBetaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductBetasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productBetaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductBetaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productBetaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProductBeta() throws Exception {
        // Initialize the database
        insertedProductBeta = productBetaRepository.saveAndFlush(productBeta);

        // Get the productBeta
        restProductBetaMockMvc
            .perform(get(ENTITY_API_URL_ID, productBeta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productBeta.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getProductBetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductBeta = productBetaRepository.saveAndFlush(productBeta);

        Long id = productBeta.getId();

        defaultProductBetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductBetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductBetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductBetasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductBeta = productBetaRepository.saveAndFlush(productBeta);

        // Get all the productBetaList where name equals to
        defaultProductBetaFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductBetasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductBeta = productBetaRepository.saveAndFlush(productBeta);

        // Get all the productBetaList where name in
        defaultProductBetaFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductBetasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductBeta = productBetaRepository.saveAndFlush(productBeta);

        // Get all the productBetaList where name is not null
        defaultProductBetaFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllProductBetasByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedProductBeta = productBetaRepository.saveAndFlush(productBeta);

        // Get all the productBetaList where name contains
        defaultProductBetaFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductBetasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductBeta = productBetaRepository.saveAndFlush(productBeta);

        // Get all the productBetaList where name does not contain
        defaultProductBetaFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllProductBetasByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductBeta = productBetaRepository.saveAndFlush(productBeta);

        // Get all the productBetaList where price equals to
        defaultProductBetaFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductBetasByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductBeta = productBetaRepository.saveAndFlush(productBeta);

        // Get all the productBetaList where price in
        defaultProductBetaFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductBetasByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductBeta = productBetaRepository.saveAndFlush(productBeta);

        // Get all the productBetaList where price is not null
        defaultProductBetaFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllProductBetasByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductBeta = productBetaRepository.saveAndFlush(productBeta);

        // Get all the productBetaList where price is greater than or equal to
        defaultProductBetaFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductBetasByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductBeta = productBetaRepository.saveAndFlush(productBeta);

        // Get all the productBetaList where price is less than or equal to
        defaultProductBetaFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllProductBetasByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductBeta = productBetaRepository.saveAndFlush(productBeta);

        // Get all the productBetaList where price is less than
        defaultProductBetaFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductBetasByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductBeta = productBetaRepository.saveAndFlush(productBeta);

        // Get all the productBetaList where price is greater than
        defaultProductBetaFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductBetasByStockIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductBeta = productBetaRepository.saveAndFlush(productBeta);

        // Get all the productBetaList where stock equals to
        defaultProductBetaFiltering("stock.equals=" + DEFAULT_STOCK, "stock.equals=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProductBetasByStockIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductBeta = productBetaRepository.saveAndFlush(productBeta);

        // Get all the productBetaList where stock in
        defaultProductBetaFiltering("stock.in=" + DEFAULT_STOCK + "," + UPDATED_STOCK, "stock.in=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProductBetasByStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductBeta = productBetaRepository.saveAndFlush(productBeta);

        // Get all the productBetaList where stock is not null
        defaultProductBetaFiltering("stock.specified=true", "stock.specified=false");
    }

    @Test
    @Transactional
    void getAllProductBetasByStockIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductBeta = productBetaRepository.saveAndFlush(productBeta);

        // Get all the productBetaList where stock is greater than or equal to
        defaultProductBetaFiltering("stock.greaterThanOrEqual=" + DEFAULT_STOCK, "stock.greaterThanOrEqual=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProductBetasByStockIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductBeta = productBetaRepository.saveAndFlush(productBeta);

        // Get all the productBetaList where stock is less than or equal to
        defaultProductBetaFiltering("stock.lessThanOrEqual=" + DEFAULT_STOCK, "stock.lessThanOrEqual=" + SMALLER_STOCK);
    }

    @Test
    @Transactional
    void getAllProductBetasByStockIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductBeta = productBetaRepository.saveAndFlush(productBeta);

        // Get all the productBetaList where stock is less than
        defaultProductBetaFiltering("stock.lessThan=" + UPDATED_STOCK, "stock.lessThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllProductBetasByStockIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductBeta = productBetaRepository.saveAndFlush(productBeta);

        // Get all the productBetaList where stock is greater than
        defaultProductBetaFiltering("stock.greaterThan=" + SMALLER_STOCK, "stock.greaterThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllProductBetasByCategoryIsEqualToSomething() throws Exception {
        CategoryBeta category;
        if (TestUtil.findAll(em, CategoryBeta.class).isEmpty()) {
            productBetaRepository.saveAndFlush(productBeta);
            category = CategoryBetaResourceIT.createEntity();
        } else {
            category = TestUtil.findAll(em, CategoryBeta.class).get(0);
        }
        em.persist(category);
        em.flush();
        productBeta.setCategory(category);
        productBetaRepository.saveAndFlush(productBeta);
        Long categoryId = category.getId();
        // Get all the productBetaList where category equals to categoryId
        defaultProductBetaShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the productBetaList where category equals to (categoryId + 1)
        defaultProductBetaShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllProductBetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            productBetaRepository.saveAndFlush(productBeta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        productBeta.setTenant(tenant);
        productBetaRepository.saveAndFlush(productBeta);
        Long tenantId = tenant.getId();
        // Get all the productBetaList where tenant equals to tenantId
        defaultProductBetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the productBetaList where tenant equals to (tenantId + 1)
        defaultProductBetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllProductBetasByOrderIsEqualToSomething() throws Exception {
        OrderBeta order;
        if (TestUtil.findAll(em, OrderBeta.class).isEmpty()) {
            productBetaRepository.saveAndFlush(productBeta);
            order = OrderBetaResourceIT.createEntity();
        } else {
            order = TestUtil.findAll(em, OrderBeta.class).get(0);
        }
        em.persist(order);
        em.flush();
        productBeta.setOrder(order);
        productBetaRepository.saveAndFlush(productBeta);
        Long orderId = order.getId();
        // Get all the productBetaList where order equals to orderId
        defaultProductBetaShouldBeFound("orderId.equals=" + orderId);

        // Get all the productBetaList where order equals to (orderId + 1)
        defaultProductBetaShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllProductBetasBySuppliersIsEqualToSomething() throws Exception {
        SupplierBeta suppliers;
        if (TestUtil.findAll(em, SupplierBeta.class).isEmpty()) {
            productBetaRepository.saveAndFlush(productBeta);
            suppliers = SupplierBetaResourceIT.createEntity();
        } else {
            suppliers = TestUtil.findAll(em, SupplierBeta.class).get(0);
        }
        em.persist(suppliers);
        em.flush();
        productBeta.addSuppliers(suppliers);
        productBetaRepository.saveAndFlush(productBeta);
        Long suppliersId = suppliers.getId();
        // Get all the productBetaList where suppliers equals to suppliersId
        defaultProductBetaShouldBeFound("suppliersId.equals=" + suppliersId);

        // Get all the productBetaList where suppliers equals to (suppliersId + 1)
        defaultProductBetaShouldNotBeFound("suppliersId.equals=" + (suppliersId + 1));
    }

    private void defaultProductBetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductBetaShouldBeFound(shouldBeFound);
        defaultProductBetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductBetaShouldBeFound(String filter) throws Exception {
        restProductBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restProductBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductBetaShouldNotBeFound(String filter) throws Exception {
        restProductBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductBeta() throws Exception {
        // Get the productBeta
        restProductBetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductBeta() throws Exception {
        // Initialize the database
        insertedProductBeta = productBetaRepository.saveAndFlush(productBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productBeta
        ProductBeta updatedProductBeta = productBetaRepository.findById(productBeta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductBeta are not directly saved in db
        em.detach(updatedProductBeta);
        updatedProductBeta.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);

        restProductBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductBeta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductBeta))
            )
            .andExpect(status().isOk());

        // Validate the ProductBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductBetaToMatchAllProperties(updatedProductBeta);
    }

    @Test
    @Transactional
    void putNonExistingProductBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productBeta.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productBeta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productBeta))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productBeta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productBeta))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productBeta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductBetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productBeta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductBetaWithPatch() throws Exception {
        // Initialize the database
        insertedProductBeta = productBetaRepository.saveAndFlush(productBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productBeta using partial update
        ProductBeta partialUpdatedProductBeta = new ProductBeta();
        partialUpdatedProductBeta.setId(productBeta.getId());

        partialUpdatedProductBeta.description(UPDATED_DESCRIPTION);

        restProductBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductBeta))
            )
            .andExpect(status().isOk());

        // Validate the ProductBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductBetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductBeta, productBeta),
            getPersistedProductBeta(productBeta)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductBetaWithPatch() throws Exception {
        // Initialize the database
        insertedProductBeta = productBetaRepository.saveAndFlush(productBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productBeta using partial update
        ProductBeta partialUpdatedProductBeta = new ProductBeta();
        partialUpdatedProductBeta.setId(productBeta.getId());

        partialUpdatedProductBeta.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);

        restProductBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductBeta))
            )
            .andExpect(status().isOk());

        // Validate the ProductBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductBetaUpdatableFieldsEquals(partialUpdatedProductBeta, getPersistedProductBeta(partialUpdatedProductBeta));
    }

    @Test
    @Transactional
    void patchNonExistingProductBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productBeta.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productBeta))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productBeta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productBeta))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productBeta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductBetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productBeta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductBeta() throws Exception {
        // Initialize the database
        insertedProductBeta = productBetaRepository.saveAndFlush(productBeta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productBeta
        restProductBetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, productBeta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productBetaRepository.count();
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

    protected ProductBeta getPersistedProductBeta(ProductBeta productBeta) {
        return productBetaRepository.findById(productBeta.getId()).orElseThrow();
    }

    protected void assertPersistedProductBetaToMatchAllProperties(ProductBeta expectedProductBeta) {
        assertProductBetaAllPropertiesEquals(expectedProductBeta, getPersistedProductBeta(expectedProductBeta));
    }

    protected void assertPersistedProductBetaToMatchUpdatableProperties(ProductBeta expectedProductBeta) {
        assertProductBetaAllUpdatablePropertiesEquals(expectedProductBeta, getPersistedProductBeta(expectedProductBeta));
    }
}
