package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.ProductThetaAsserts.*;
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
import xyz.jhmapstruct.domain.CategoryTheta;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.OrderTheta;
import xyz.jhmapstruct.domain.ProductTheta;
import xyz.jhmapstruct.domain.SupplierTheta;
import xyz.jhmapstruct.repository.ProductThetaRepository;
import xyz.jhmapstruct.service.ProductThetaService;
import xyz.jhmapstruct.service.dto.ProductThetaDTO;
import xyz.jhmapstruct.service.mapper.ProductThetaMapper;

/**
 * Integration tests for the {@link ProductThetaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductThetaResourceIT {

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

    private static final String ENTITY_API_URL = "/api/product-thetas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductThetaRepository productThetaRepository;

    @Mock
    private ProductThetaRepository productThetaRepositoryMock;

    @Autowired
    private ProductThetaMapper productThetaMapper;

    @Mock
    private ProductThetaService productThetaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductThetaMockMvc;

    private ProductTheta productTheta;

    private ProductTheta insertedProductTheta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductTheta createEntity() {
        return new ProductTheta().name(DEFAULT_NAME).price(DEFAULT_PRICE).stock(DEFAULT_STOCK).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductTheta createUpdatedEntity() {
        return new ProductTheta().name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        productTheta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductTheta != null) {
            productThetaRepository.delete(insertedProductTheta);
            insertedProductTheta = null;
        }
    }

    @Test
    @Transactional
    void createProductTheta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductTheta
        ProductThetaDTO productThetaDTO = productThetaMapper.toDto(productTheta);
        var returnedProductThetaDTO = om.readValue(
            restProductThetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productThetaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductThetaDTO.class
        );

        // Validate the ProductTheta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProductTheta = productThetaMapper.toEntity(returnedProductThetaDTO);
        assertProductThetaUpdatableFieldsEquals(returnedProductTheta, getPersistedProductTheta(returnedProductTheta));

        insertedProductTheta = returnedProductTheta;
    }

    @Test
    @Transactional
    void createProductThetaWithExistingId() throws Exception {
        // Create the ProductTheta with an existing ID
        productTheta.setId(1L);
        ProductThetaDTO productThetaDTO = productThetaMapper.toDto(productTheta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productThetaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productTheta.setName(null);

        // Create the ProductTheta, which fails.
        ProductThetaDTO productThetaDTO = productThetaMapper.toDto(productTheta);

        restProductThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productThetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productTheta.setPrice(null);

        // Create the ProductTheta, which fails.
        ProductThetaDTO productThetaDTO = productThetaMapper.toDto(productTheta);

        restProductThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productThetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStockIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productTheta.setStock(null);

        // Create the ProductTheta, which fails.
        ProductThetaDTO productThetaDTO = productThetaMapper.toDto(productTheta);

        restProductThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productThetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductThetas() throws Exception {
        // Initialize the database
        insertedProductTheta = productThetaRepository.saveAndFlush(productTheta);

        // Get all the productThetaList
        restProductThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductThetasWithEagerRelationshipsIsEnabled() throws Exception {
        when(productThetaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductThetaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productThetaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductThetasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productThetaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductThetaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productThetaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProductTheta() throws Exception {
        // Initialize the database
        insertedProductTheta = productThetaRepository.saveAndFlush(productTheta);

        // Get the productTheta
        restProductThetaMockMvc
            .perform(get(ENTITY_API_URL_ID, productTheta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productTheta.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getProductThetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductTheta = productThetaRepository.saveAndFlush(productTheta);

        Long id = productTheta.getId();

        defaultProductThetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductThetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductThetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductThetasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductTheta = productThetaRepository.saveAndFlush(productTheta);

        // Get all the productThetaList where name equals to
        defaultProductThetaFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductThetasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductTheta = productThetaRepository.saveAndFlush(productTheta);

        // Get all the productThetaList where name in
        defaultProductThetaFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductThetasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductTheta = productThetaRepository.saveAndFlush(productTheta);

        // Get all the productThetaList where name is not null
        defaultProductThetaFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllProductThetasByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedProductTheta = productThetaRepository.saveAndFlush(productTheta);

        // Get all the productThetaList where name contains
        defaultProductThetaFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductThetasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductTheta = productThetaRepository.saveAndFlush(productTheta);

        // Get all the productThetaList where name does not contain
        defaultProductThetaFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllProductThetasByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductTheta = productThetaRepository.saveAndFlush(productTheta);

        // Get all the productThetaList where price equals to
        defaultProductThetaFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductThetasByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductTheta = productThetaRepository.saveAndFlush(productTheta);

        // Get all the productThetaList where price in
        defaultProductThetaFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductThetasByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductTheta = productThetaRepository.saveAndFlush(productTheta);

        // Get all the productThetaList where price is not null
        defaultProductThetaFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllProductThetasByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductTheta = productThetaRepository.saveAndFlush(productTheta);

        // Get all the productThetaList where price is greater than or equal to
        defaultProductThetaFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductThetasByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductTheta = productThetaRepository.saveAndFlush(productTheta);

        // Get all the productThetaList where price is less than or equal to
        defaultProductThetaFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllProductThetasByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductTheta = productThetaRepository.saveAndFlush(productTheta);

        // Get all the productThetaList where price is less than
        defaultProductThetaFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductThetasByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductTheta = productThetaRepository.saveAndFlush(productTheta);

        // Get all the productThetaList where price is greater than
        defaultProductThetaFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductThetasByStockIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductTheta = productThetaRepository.saveAndFlush(productTheta);

        // Get all the productThetaList where stock equals to
        defaultProductThetaFiltering("stock.equals=" + DEFAULT_STOCK, "stock.equals=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProductThetasByStockIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductTheta = productThetaRepository.saveAndFlush(productTheta);

        // Get all the productThetaList where stock in
        defaultProductThetaFiltering("stock.in=" + DEFAULT_STOCK + "," + UPDATED_STOCK, "stock.in=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProductThetasByStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductTheta = productThetaRepository.saveAndFlush(productTheta);

        // Get all the productThetaList where stock is not null
        defaultProductThetaFiltering("stock.specified=true", "stock.specified=false");
    }

    @Test
    @Transactional
    void getAllProductThetasByStockIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductTheta = productThetaRepository.saveAndFlush(productTheta);

        // Get all the productThetaList where stock is greater than or equal to
        defaultProductThetaFiltering("stock.greaterThanOrEqual=" + DEFAULT_STOCK, "stock.greaterThanOrEqual=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProductThetasByStockIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductTheta = productThetaRepository.saveAndFlush(productTheta);

        // Get all the productThetaList where stock is less than or equal to
        defaultProductThetaFiltering("stock.lessThanOrEqual=" + DEFAULT_STOCK, "stock.lessThanOrEqual=" + SMALLER_STOCK);
    }

    @Test
    @Transactional
    void getAllProductThetasByStockIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductTheta = productThetaRepository.saveAndFlush(productTheta);

        // Get all the productThetaList where stock is less than
        defaultProductThetaFiltering("stock.lessThan=" + UPDATED_STOCK, "stock.lessThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllProductThetasByStockIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductTheta = productThetaRepository.saveAndFlush(productTheta);

        // Get all the productThetaList where stock is greater than
        defaultProductThetaFiltering("stock.greaterThan=" + SMALLER_STOCK, "stock.greaterThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllProductThetasByCategoryIsEqualToSomething() throws Exception {
        CategoryTheta category;
        if (TestUtil.findAll(em, CategoryTheta.class).isEmpty()) {
            productThetaRepository.saveAndFlush(productTheta);
            category = CategoryThetaResourceIT.createEntity();
        } else {
            category = TestUtil.findAll(em, CategoryTheta.class).get(0);
        }
        em.persist(category);
        em.flush();
        productTheta.setCategory(category);
        productThetaRepository.saveAndFlush(productTheta);
        Long categoryId = category.getId();
        // Get all the productThetaList where category equals to categoryId
        defaultProductThetaShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the productThetaList where category equals to (categoryId + 1)
        defaultProductThetaShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllProductThetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            productThetaRepository.saveAndFlush(productTheta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        productTheta.setTenant(tenant);
        productThetaRepository.saveAndFlush(productTheta);
        Long tenantId = tenant.getId();
        // Get all the productThetaList where tenant equals to tenantId
        defaultProductThetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the productThetaList where tenant equals to (tenantId + 1)
        defaultProductThetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllProductThetasByOrderIsEqualToSomething() throws Exception {
        OrderTheta order;
        if (TestUtil.findAll(em, OrderTheta.class).isEmpty()) {
            productThetaRepository.saveAndFlush(productTheta);
            order = OrderThetaResourceIT.createEntity();
        } else {
            order = TestUtil.findAll(em, OrderTheta.class).get(0);
        }
        em.persist(order);
        em.flush();
        productTheta.setOrder(order);
        productThetaRepository.saveAndFlush(productTheta);
        Long orderId = order.getId();
        // Get all the productThetaList where order equals to orderId
        defaultProductThetaShouldBeFound("orderId.equals=" + orderId);

        // Get all the productThetaList where order equals to (orderId + 1)
        defaultProductThetaShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllProductThetasBySuppliersIsEqualToSomething() throws Exception {
        SupplierTheta suppliers;
        if (TestUtil.findAll(em, SupplierTheta.class).isEmpty()) {
            productThetaRepository.saveAndFlush(productTheta);
            suppliers = SupplierThetaResourceIT.createEntity();
        } else {
            suppliers = TestUtil.findAll(em, SupplierTheta.class).get(0);
        }
        em.persist(suppliers);
        em.flush();
        productTheta.addSuppliers(suppliers);
        productThetaRepository.saveAndFlush(productTheta);
        Long suppliersId = suppliers.getId();
        // Get all the productThetaList where suppliers equals to suppliersId
        defaultProductThetaShouldBeFound("suppliersId.equals=" + suppliersId);

        // Get all the productThetaList where suppliers equals to (suppliersId + 1)
        defaultProductThetaShouldNotBeFound("suppliersId.equals=" + (suppliersId + 1));
    }

    private void defaultProductThetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductThetaShouldBeFound(shouldBeFound);
        defaultProductThetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductThetaShouldBeFound(String filter) throws Exception {
        restProductThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restProductThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductThetaShouldNotBeFound(String filter) throws Exception {
        restProductThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductTheta() throws Exception {
        // Get the productTheta
        restProductThetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductTheta() throws Exception {
        // Initialize the database
        insertedProductTheta = productThetaRepository.saveAndFlush(productTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productTheta
        ProductTheta updatedProductTheta = productThetaRepository.findById(productTheta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductTheta are not directly saved in db
        em.detach(updatedProductTheta);
        updatedProductTheta.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);
        ProductThetaDTO productThetaDTO = productThetaMapper.toDto(updatedProductTheta);

        restProductThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productThetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productThetaDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductThetaToMatchAllProperties(updatedProductTheta);
    }

    @Test
    @Transactional
    void putNonExistingProductTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productTheta.setId(longCount.incrementAndGet());

        // Create the ProductTheta
        ProductThetaDTO productThetaDTO = productThetaMapper.toDto(productTheta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productThetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productTheta.setId(longCount.incrementAndGet());

        // Create the ProductTheta
        ProductThetaDTO productThetaDTO = productThetaMapper.toDto(productTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productTheta.setId(longCount.incrementAndGet());

        // Create the ProductTheta
        ProductThetaDTO productThetaDTO = productThetaMapper.toDto(productTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductThetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productThetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductThetaWithPatch() throws Exception {
        // Initialize the database
        insertedProductTheta = productThetaRepository.saveAndFlush(productTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productTheta using partial update
        ProductTheta partialUpdatedProductTheta = new ProductTheta();
        partialUpdatedProductTheta.setId(productTheta.getId());

        partialUpdatedProductTheta.price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);

        restProductThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductTheta))
            )
            .andExpect(status().isOk());

        // Validate the ProductTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductThetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductTheta, productTheta),
            getPersistedProductTheta(productTheta)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductThetaWithPatch() throws Exception {
        // Initialize the database
        insertedProductTheta = productThetaRepository.saveAndFlush(productTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productTheta using partial update
        ProductTheta partialUpdatedProductTheta = new ProductTheta();
        partialUpdatedProductTheta.setId(productTheta.getId());

        partialUpdatedProductTheta.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);

        restProductThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductTheta))
            )
            .andExpect(status().isOk());

        // Validate the ProductTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductThetaUpdatableFieldsEquals(partialUpdatedProductTheta, getPersistedProductTheta(partialUpdatedProductTheta));
    }

    @Test
    @Transactional
    void patchNonExistingProductTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productTheta.setId(longCount.incrementAndGet());

        // Create the ProductTheta
        ProductThetaDTO productThetaDTO = productThetaMapper.toDto(productTheta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productThetaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productTheta.setId(longCount.incrementAndGet());

        // Create the ProductTheta
        ProductThetaDTO productThetaDTO = productThetaMapper.toDto(productTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productTheta.setId(longCount.incrementAndGet());

        // Create the ProductTheta
        ProductThetaDTO productThetaDTO = productThetaMapper.toDto(productTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductThetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productThetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductTheta() throws Exception {
        // Initialize the database
        insertedProductTheta = productThetaRepository.saveAndFlush(productTheta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productTheta
        restProductThetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, productTheta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productThetaRepository.count();
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

    protected ProductTheta getPersistedProductTheta(ProductTheta productTheta) {
        return productThetaRepository.findById(productTheta.getId()).orElseThrow();
    }

    protected void assertPersistedProductThetaToMatchAllProperties(ProductTheta expectedProductTheta) {
        assertProductThetaAllPropertiesEquals(expectedProductTheta, getPersistedProductTheta(expectedProductTheta));
    }

    protected void assertPersistedProductThetaToMatchUpdatableProperties(ProductTheta expectedProductTheta) {
        assertProductThetaAllUpdatablePropertiesEquals(expectedProductTheta, getPersistedProductTheta(expectedProductTheta));
    }
}
