package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.ProductGammaAsserts.*;
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
import xyz.jhmapstruct.domain.CategoryGamma;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.OrderGamma;
import xyz.jhmapstruct.domain.ProductGamma;
import xyz.jhmapstruct.domain.SupplierGamma;
import xyz.jhmapstruct.repository.ProductGammaRepository;
import xyz.jhmapstruct.service.ProductGammaService;
import xyz.jhmapstruct.service.dto.ProductGammaDTO;
import xyz.jhmapstruct.service.mapper.ProductGammaMapper;

/**
 * Integration tests for the {@link ProductGammaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductGammaResourceIT {

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

    private static final String ENTITY_API_URL = "/api/product-gammas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductGammaRepository productGammaRepository;

    @Mock
    private ProductGammaRepository productGammaRepositoryMock;

    @Autowired
    private ProductGammaMapper productGammaMapper;

    @Mock
    private ProductGammaService productGammaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductGammaMockMvc;

    private ProductGamma productGamma;

    private ProductGamma insertedProductGamma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductGamma createEntity() {
        return new ProductGamma().name(DEFAULT_NAME).price(DEFAULT_PRICE).stock(DEFAULT_STOCK).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductGamma createUpdatedEntity() {
        return new ProductGamma().name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        productGamma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductGamma != null) {
            productGammaRepository.delete(insertedProductGamma);
            insertedProductGamma = null;
        }
    }

    @Test
    @Transactional
    void createProductGamma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductGamma
        ProductGammaDTO productGammaDTO = productGammaMapper.toDto(productGamma);
        var returnedProductGammaDTO = om.readValue(
            restProductGammaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productGammaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductGammaDTO.class
        );

        // Validate the ProductGamma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProductGamma = productGammaMapper.toEntity(returnedProductGammaDTO);
        assertProductGammaUpdatableFieldsEquals(returnedProductGamma, getPersistedProductGamma(returnedProductGamma));

        insertedProductGamma = returnedProductGamma;
    }

    @Test
    @Transactional
    void createProductGammaWithExistingId() throws Exception {
        // Create the ProductGamma with an existing ID
        productGamma.setId(1L);
        ProductGammaDTO productGammaDTO = productGammaMapper.toDto(productGamma);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productGammaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productGamma.setName(null);

        // Create the ProductGamma, which fails.
        ProductGammaDTO productGammaDTO = productGammaMapper.toDto(productGamma);

        restProductGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productGammaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productGamma.setPrice(null);

        // Create the ProductGamma, which fails.
        ProductGammaDTO productGammaDTO = productGammaMapper.toDto(productGamma);

        restProductGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productGammaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStockIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productGamma.setStock(null);

        // Create the ProductGamma, which fails.
        ProductGammaDTO productGammaDTO = productGammaMapper.toDto(productGamma);

        restProductGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productGammaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductGammas() throws Exception {
        // Initialize the database
        insertedProductGamma = productGammaRepository.saveAndFlush(productGamma);

        // Get all the productGammaList
        restProductGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductGammasWithEagerRelationshipsIsEnabled() throws Exception {
        when(productGammaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductGammaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productGammaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductGammasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productGammaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductGammaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productGammaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProductGamma() throws Exception {
        // Initialize the database
        insertedProductGamma = productGammaRepository.saveAndFlush(productGamma);

        // Get the productGamma
        restProductGammaMockMvc
            .perform(get(ENTITY_API_URL_ID, productGamma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productGamma.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getProductGammasByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductGamma = productGammaRepository.saveAndFlush(productGamma);

        Long id = productGamma.getId();

        defaultProductGammaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductGammaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductGammaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductGammasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductGamma = productGammaRepository.saveAndFlush(productGamma);

        // Get all the productGammaList where name equals to
        defaultProductGammaFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductGammasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductGamma = productGammaRepository.saveAndFlush(productGamma);

        // Get all the productGammaList where name in
        defaultProductGammaFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductGammasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductGamma = productGammaRepository.saveAndFlush(productGamma);

        // Get all the productGammaList where name is not null
        defaultProductGammaFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllProductGammasByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedProductGamma = productGammaRepository.saveAndFlush(productGamma);

        // Get all the productGammaList where name contains
        defaultProductGammaFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductGammasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductGamma = productGammaRepository.saveAndFlush(productGamma);

        // Get all the productGammaList where name does not contain
        defaultProductGammaFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllProductGammasByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductGamma = productGammaRepository.saveAndFlush(productGamma);

        // Get all the productGammaList where price equals to
        defaultProductGammaFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductGammasByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductGamma = productGammaRepository.saveAndFlush(productGamma);

        // Get all the productGammaList where price in
        defaultProductGammaFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductGammasByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductGamma = productGammaRepository.saveAndFlush(productGamma);

        // Get all the productGammaList where price is not null
        defaultProductGammaFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllProductGammasByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductGamma = productGammaRepository.saveAndFlush(productGamma);

        // Get all the productGammaList where price is greater than or equal to
        defaultProductGammaFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductGammasByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductGamma = productGammaRepository.saveAndFlush(productGamma);

        // Get all the productGammaList where price is less than or equal to
        defaultProductGammaFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllProductGammasByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductGamma = productGammaRepository.saveAndFlush(productGamma);

        // Get all the productGammaList where price is less than
        defaultProductGammaFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductGammasByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductGamma = productGammaRepository.saveAndFlush(productGamma);

        // Get all the productGammaList where price is greater than
        defaultProductGammaFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductGammasByStockIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductGamma = productGammaRepository.saveAndFlush(productGamma);

        // Get all the productGammaList where stock equals to
        defaultProductGammaFiltering("stock.equals=" + DEFAULT_STOCK, "stock.equals=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProductGammasByStockIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductGamma = productGammaRepository.saveAndFlush(productGamma);

        // Get all the productGammaList where stock in
        defaultProductGammaFiltering("stock.in=" + DEFAULT_STOCK + "," + UPDATED_STOCK, "stock.in=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProductGammasByStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductGamma = productGammaRepository.saveAndFlush(productGamma);

        // Get all the productGammaList where stock is not null
        defaultProductGammaFiltering("stock.specified=true", "stock.specified=false");
    }

    @Test
    @Transactional
    void getAllProductGammasByStockIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductGamma = productGammaRepository.saveAndFlush(productGamma);

        // Get all the productGammaList where stock is greater than or equal to
        defaultProductGammaFiltering("stock.greaterThanOrEqual=" + DEFAULT_STOCK, "stock.greaterThanOrEqual=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProductGammasByStockIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductGamma = productGammaRepository.saveAndFlush(productGamma);

        // Get all the productGammaList where stock is less than or equal to
        defaultProductGammaFiltering("stock.lessThanOrEqual=" + DEFAULT_STOCK, "stock.lessThanOrEqual=" + SMALLER_STOCK);
    }

    @Test
    @Transactional
    void getAllProductGammasByStockIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductGamma = productGammaRepository.saveAndFlush(productGamma);

        // Get all the productGammaList where stock is less than
        defaultProductGammaFiltering("stock.lessThan=" + UPDATED_STOCK, "stock.lessThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllProductGammasByStockIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductGamma = productGammaRepository.saveAndFlush(productGamma);

        // Get all the productGammaList where stock is greater than
        defaultProductGammaFiltering("stock.greaterThan=" + SMALLER_STOCK, "stock.greaterThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllProductGammasByCategoryIsEqualToSomething() throws Exception {
        CategoryGamma category;
        if (TestUtil.findAll(em, CategoryGamma.class).isEmpty()) {
            productGammaRepository.saveAndFlush(productGamma);
            category = CategoryGammaResourceIT.createEntity();
        } else {
            category = TestUtil.findAll(em, CategoryGamma.class).get(0);
        }
        em.persist(category);
        em.flush();
        productGamma.setCategory(category);
        productGammaRepository.saveAndFlush(productGamma);
        Long categoryId = category.getId();
        // Get all the productGammaList where category equals to categoryId
        defaultProductGammaShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the productGammaList where category equals to (categoryId + 1)
        defaultProductGammaShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllProductGammasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            productGammaRepository.saveAndFlush(productGamma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        productGamma.setTenant(tenant);
        productGammaRepository.saveAndFlush(productGamma);
        Long tenantId = tenant.getId();
        // Get all the productGammaList where tenant equals to tenantId
        defaultProductGammaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the productGammaList where tenant equals to (tenantId + 1)
        defaultProductGammaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllProductGammasByOrderIsEqualToSomething() throws Exception {
        OrderGamma order;
        if (TestUtil.findAll(em, OrderGamma.class).isEmpty()) {
            productGammaRepository.saveAndFlush(productGamma);
            order = OrderGammaResourceIT.createEntity();
        } else {
            order = TestUtil.findAll(em, OrderGamma.class).get(0);
        }
        em.persist(order);
        em.flush();
        productGamma.setOrder(order);
        productGammaRepository.saveAndFlush(productGamma);
        Long orderId = order.getId();
        // Get all the productGammaList where order equals to orderId
        defaultProductGammaShouldBeFound("orderId.equals=" + orderId);

        // Get all the productGammaList where order equals to (orderId + 1)
        defaultProductGammaShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllProductGammasBySuppliersIsEqualToSomething() throws Exception {
        SupplierGamma suppliers;
        if (TestUtil.findAll(em, SupplierGamma.class).isEmpty()) {
            productGammaRepository.saveAndFlush(productGamma);
            suppliers = SupplierGammaResourceIT.createEntity();
        } else {
            suppliers = TestUtil.findAll(em, SupplierGamma.class).get(0);
        }
        em.persist(suppliers);
        em.flush();
        productGamma.addSuppliers(suppliers);
        productGammaRepository.saveAndFlush(productGamma);
        Long suppliersId = suppliers.getId();
        // Get all the productGammaList where suppliers equals to suppliersId
        defaultProductGammaShouldBeFound("suppliersId.equals=" + suppliersId);

        // Get all the productGammaList where suppliers equals to (suppliersId + 1)
        defaultProductGammaShouldNotBeFound("suppliersId.equals=" + (suppliersId + 1));
    }

    private void defaultProductGammaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductGammaShouldBeFound(shouldBeFound);
        defaultProductGammaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductGammaShouldBeFound(String filter) throws Exception {
        restProductGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restProductGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductGammaShouldNotBeFound(String filter) throws Exception {
        restProductGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductGamma() throws Exception {
        // Get the productGamma
        restProductGammaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductGamma() throws Exception {
        // Initialize the database
        insertedProductGamma = productGammaRepository.saveAndFlush(productGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productGamma
        ProductGamma updatedProductGamma = productGammaRepository.findById(productGamma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductGamma are not directly saved in db
        em.detach(updatedProductGamma);
        updatedProductGamma.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);
        ProductGammaDTO productGammaDTO = productGammaMapper.toDto(updatedProductGamma);

        restProductGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productGammaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productGammaDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductGammaToMatchAllProperties(updatedProductGamma);
    }

    @Test
    @Transactional
    void putNonExistingProductGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productGamma.setId(longCount.incrementAndGet());

        // Create the ProductGamma
        ProductGammaDTO productGammaDTO = productGammaMapper.toDto(productGamma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productGammaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productGamma.setId(longCount.incrementAndGet());

        // Create the ProductGamma
        ProductGammaDTO productGammaDTO = productGammaMapper.toDto(productGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productGamma.setId(longCount.incrementAndGet());

        // Create the ProductGamma
        ProductGammaDTO productGammaDTO = productGammaMapper.toDto(productGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductGammaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productGammaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductGammaWithPatch() throws Exception {
        // Initialize the database
        insertedProductGamma = productGammaRepository.saveAndFlush(productGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productGamma using partial update
        ProductGamma partialUpdatedProductGamma = new ProductGamma();
        partialUpdatedProductGamma.setId(productGamma.getId());

        partialUpdatedProductGamma.name(UPDATED_NAME).price(UPDATED_PRICE);

        restProductGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductGamma))
            )
            .andExpect(status().isOk());

        // Validate the ProductGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductGammaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductGamma, productGamma),
            getPersistedProductGamma(productGamma)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductGammaWithPatch() throws Exception {
        // Initialize the database
        insertedProductGamma = productGammaRepository.saveAndFlush(productGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productGamma using partial update
        ProductGamma partialUpdatedProductGamma = new ProductGamma();
        partialUpdatedProductGamma.setId(productGamma.getId());

        partialUpdatedProductGamma.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);

        restProductGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductGamma))
            )
            .andExpect(status().isOk());

        // Validate the ProductGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductGammaUpdatableFieldsEquals(partialUpdatedProductGamma, getPersistedProductGamma(partialUpdatedProductGamma));
    }

    @Test
    @Transactional
    void patchNonExistingProductGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productGamma.setId(longCount.incrementAndGet());

        // Create the ProductGamma
        ProductGammaDTO productGammaDTO = productGammaMapper.toDto(productGamma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productGammaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productGamma.setId(longCount.incrementAndGet());

        // Create the ProductGamma
        ProductGammaDTO productGammaDTO = productGammaMapper.toDto(productGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productGamma.setId(longCount.incrementAndGet());

        // Create the ProductGamma
        ProductGammaDTO productGammaDTO = productGammaMapper.toDto(productGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductGammaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productGammaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductGamma() throws Exception {
        // Initialize the database
        insertedProductGamma = productGammaRepository.saveAndFlush(productGamma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productGamma
        restProductGammaMockMvc
            .perform(delete(ENTITY_API_URL_ID, productGamma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productGammaRepository.count();
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

    protected ProductGamma getPersistedProductGamma(ProductGamma productGamma) {
        return productGammaRepository.findById(productGamma.getId()).orElseThrow();
    }

    protected void assertPersistedProductGammaToMatchAllProperties(ProductGamma expectedProductGamma) {
        assertProductGammaAllPropertiesEquals(expectedProductGamma, getPersistedProductGamma(expectedProductGamma));
    }

    protected void assertPersistedProductGammaToMatchUpdatableProperties(ProductGamma expectedProductGamma) {
        assertProductGammaAllUpdatablePropertiesEquals(expectedProductGamma, getPersistedProductGamma(expectedProductGamma));
    }
}
