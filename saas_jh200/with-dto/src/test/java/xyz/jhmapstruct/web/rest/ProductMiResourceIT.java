package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.ProductMiAsserts.*;
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
import xyz.jhmapstruct.domain.NextCategoryMi;
import xyz.jhmapstruct.domain.NextSupplierMi;
import xyz.jhmapstruct.domain.OrderMi;
import xyz.jhmapstruct.domain.ProductMi;
import xyz.jhmapstruct.repository.ProductMiRepository;
import xyz.jhmapstruct.service.ProductMiService;
import xyz.jhmapstruct.service.dto.ProductMiDTO;
import xyz.jhmapstruct.service.mapper.ProductMiMapper;

/**
 * Integration tests for the {@link ProductMiResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductMiResourceIT {

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

    private static final String ENTITY_API_URL = "/api/product-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductMiRepository productMiRepository;

    @Mock
    private ProductMiRepository productMiRepositoryMock;

    @Autowired
    private ProductMiMapper productMiMapper;

    @Mock
    private ProductMiService productMiServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductMiMockMvc;

    private ProductMi productMi;

    private ProductMi insertedProductMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductMi createEntity() {
        return new ProductMi().name(DEFAULT_NAME).price(DEFAULT_PRICE).stock(DEFAULT_STOCK).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductMi createUpdatedEntity() {
        return new ProductMi().name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        productMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductMi != null) {
            productMiRepository.delete(insertedProductMi);
            insertedProductMi = null;
        }
    }

    @Test
    @Transactional
    void createProductMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductMi
        ProductMiDTO productMiDTO = productMiMapper.toDto(productMi);
        var returnedProductMiDTO = om.readValue(
            restProductMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productMiDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductMiDTO.class
        );

        // Validate the ProductMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProductMi = productMiMapper.toEntity(returnedProductMiDTO);
        assertProductMiUpdatableFieldsEquals(returnedProductMi, getPersistedProductMi(returnedProductMi));

        insertedProductMi = returnedProductMi;
    }

    @Test
    @Transactional
    void createProductMiWithExistingId() throws Exception {
        // Create the ProductMi with an existing ID
        productMi.setId(1L);
        ProductMiDTO productMiDTO = productMiMapper.toDto(productMi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productMiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productMi.setName(null);

        // Create the ProductMi, which fails.
        ProductMiDTO productMiDTO = productMiMapper.toDto(productMi);

        restProductMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productMi.setPrice(null);

        // Create the ProductMi, which fails.
        ProductMiDTO productMiDTO = productMiMapper.toDto(productMi);

        restProductMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStockIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productMi.setStock(null);

        // Create the ProductMi, which fails.
        ProductMiDTO productMiDTO = productMiMapper.toDto(productMi);

        restProductMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductMis() throws Exception {
        // Initialize the database
        insertedProductMi = productMiRepository.saveAndFlush(productMi);

        // Get all the productMiList
        restProductMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductMisWithEagerRelationshipsIsEnabled() throws Exception {
        when(productMiServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductMiMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productMiServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductMisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productMiServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductMiMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productMiRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProductMi() throws Exception {
        // Initialize the database
        insertedProductMi = productMiRepository.saveAndFlush(productMi);

        // Get the productMi
        restProductMiMockMvc
            .perform(get(ENTITY_API_URL_ID, productMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productMi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getProductMisByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductMi = productMiRepository.saveAndFlush(productMi);

        Long id = productMi.getId();

        defaultProductMiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductMiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductMiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductMisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductMi = productMiRepository.saveAndFlush(productMi);

        // Get all the productMiList where name equals to
        defaultProductMiFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductMisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductMi = productMiRepository.saveAndFlush(productMi);

        // Get all the productMiList where name in
        defaultProductMiFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductMisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductMi = productMiRepository.saveAndFlush(productMi);

        // Get all the productMiList where name is not null
        defaultProductMiFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllProductMisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedProductMi = productMiRepository.saveAndFlush(productMi);

        // Get all the productMiList where name contains
        defaultProductMiFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductMisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductMi = productMiRepository.saveAndFlush(productMi);

        // Get all the productMiList where name does not contain
        defaultProductMiFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllProductMisByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductMi = productMiRepository.saveAndFlush(productMi);

        // Get all the productMiList where price equals to
        defaultProductMiFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductMisByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductMi = productMiRepository.saveAndFlush(productMi);

        // Get all the productMiList where price in
        defaultProductMiFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductMisByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductMi = productMiRepository.saveAndFlush(productMi);

        // Get all the productMiList where price is not null
        defaultProductMiFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllProductMisByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductMi = productMiRepository.saveAndFlush(productMi);

        // Get all the productMiList where price is greater than or equal to
        defaultProductMiFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductMisByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductMi = productMiRepository.saveAndFlush(productMi);

        // Get all the productMiList where price is less than or equal to
        defaultProductMiFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllProductMisByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductMi = productMiRepository.saveAndFlush(productMi);

        // Get all the productMiList where price is less than
        defaultProductMiFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductMisByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductMi = productMiRepository.saveAndFlush(productMi);

        // Get all the productMiList where price is greater than
        defaultProductMiFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductMisByStockIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductMi = productMiRepository.saveAndFlush(productMi);

        // Get all the productMiList where stock equals to
        defaultProductMiFiltering("stock.equals=" + DEFAULT_STOCK, "stock.equals=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProductMisByStockIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductMi = productMiRepository.saveAndFlush(productMi);

        // Get all the productMiList where stock in
        defaultProductMiFiltering("stock.in=" + DEFAULT_STOCK + "," + UPDATED_STOCK, "stock.in=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProductMisByStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductMi = productMiRepository.saveAndFlush(productMi);

        // Get all the productMiList where stock is not null
        defaultProductMiFiltering("stock.specified=true", "stock.specified=false");
    }

    @Test
    @Transactional
    void getAllProductMisByStockIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductMi = productMiRepository.saveAndFlush(productMi);

        // Get all the productMiList where stock is greater than or equal to
        defaultProductMiFiltering("stock.greaterThanOrEqual=" + DEFAULT_STOCK, "stock.greaterThanOrEqual=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProductMisByStockIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductMi = productMiRepository.saveAndFlush(productMi);

        // Get all the productMiList where stock is less than or equal to
        defaultProductMiFiltering("stock.lessThanOrEqual=" + DEFAULT_STOCK, "stock.lessThanOrEqual=" + SMALLER_STOCK);
    }

    @Test
    @Transactional
    void getAllProductMisByStockIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductMi = productMiRepository.saveAndFlush(productMi);

        // Get all the productMiList where stock is less than
        defaultProductMiFiltering("stock.lessThan=" + UPDATED_STOCK, "stock.lessThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllProductMisByStockIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductMi = productMiRepository.saveAndFlush(productMi);

        // Get all the productMiList where stock is greater than
        defaultProductMiFiltering("stock.greaterThan=" + SMALLER_STOCK, "stock.greaterThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllProductMisByCategoryIsEqualToSomething() throws Exception {
        NextCategoryMi category;
        if (TestUtil.findAll(em, NextCategoryMi.class).isEmpty()) {
            productMiRepository.saveAndFlush(productMi);
            category = NextCategoryMiResourceIT.createEntity();
        } else {
            category = TestUtil.findAll(em, NextCategoryMi.class).get(0);
        }
        em.persist(category);
        em.flush();
        productMi.setCategory(category);
        productMiRepository.saveAndFlush(productMi);
        Long categoryId = category.getId();
        // Get all the productMiList where category equals to categoryId
        defaultProductMiShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the productMiList where category equals to (categoryId + 1)
        defaultProductMiShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllProductMisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            productMiRepository.saveAndFlush(productMi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        productMi.setTenant(tenant);
        productMiRepository.saveAndFlush(productMi);
        Long tenantId = tenant.getId();
        // Get all the productMiList where tenant equals to tenantId
        defaultProductMiShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the productMiList where tenant equals to (tenantId + 1)
        defaultProductMiShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllProductMisByOrderIsEqualToSomething() throws Exception {
        OrderMi order;
        if (TestUtil.findAll(em, OrderMi.class).isEmpty()) {
            productMiRepository.saveAndFlush(productMi);
            order = OrderMiResourceIT.createEntity();
        } else {
            order = TestUtil.findAll(em, OrderMi.class).get(0);
        }
        em.persist(order);
        em.flush();
        productMi.setOrder(order);
        productMiRepository.saveAndFlush(productMi);
        Long orderId = order.getId();
        // Get all the productMiList where order equals to orderId
        defaultProductMiShouldBeFound("orderId.equals=" + orderId);

        // Get all the productMiList where order equals to (orderId + 1)
        defaultProductMiShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllProductMisBySuppliersIsEqualToSomething() throws Exception {
        NextSupplierMi suppliers;
        if (TestUtil.findAll(em, NextSupplierMi.class).isEmpty()) {
            productMiRepository.saveAndFlush(productMi);
            suppliers = NextSupplierMiResourceIT.createEntity();
        } else {
            suppliers = TestUtil.findAll(em, NextSupplierMi.class).get(0);
        }
        em.persist(suppliers);
        em.flush();
        productMi.addSuppliers(suppliers);
        productMiRepository.saveAndFlush(productMi);
        Long suppliersId = suppliers.getId();
        // Get all the productMiList where suppliers equals to suppliersId
        defaultProductMiShouldBeFound("suppliersId.equals=" + suppliersId);

        // Get all the productMiList where suppliers equals to (suppliersId + 1)
        defaultProductMiShouldNotBeFound("suppliersId.equals=" + (suppliersId + 1));
    }

    private void defaultProductMiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductMiShouldBeFound(shouldBeFound);
        defaultProductMiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductMiShouldBeFound(String filter) throws Exception {
        restProductMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restProductMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductMiShouldNotBeFound(String filter) throws Exception {
        restProductMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductMi() throws Exception {
        // Get the productMi
        restProductMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductMi() throws Exception {
        // Initialize the database
        insertedProductMi = productMiRepository.saveAndFlush(productMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productMi
        ProductMi updatedProductMi = productMiRepository.findById(productMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductMi are not directly saved in db
        em.detach(updatedProductMi);
        updatedProductMi.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);
        ProductMiDTO productMiDTO = productMiMapper.toDto(updatedProductMi);

        restProductMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productMiDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductMiToMatchAllProperties(updatedProductMi);
    }

    @Test
    @Transactional
    void putNonExistingProductMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productMi.setId(longCount.incrementAndGet());

        // Create the ProductMi
        ProductMiDTO productMiDTO = productMiMapper.toDto(productMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productMi.setId(longCount.incrementAndGet());

        // Create the ProductMi
        ProductMiDTO productMiDTO = productMiMapper.toDto(productMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productMi.setId(longCount.incrementAndGet());

        // Create the ProductMi
        ProductMiDTO productMiDTO = productMiMapper.toDto(productMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductMiWithPatch() throws Exception {
        // Initialize the database
        insertedProductMi = productMiRepository.saveAndFlush(productMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productMi using partial update
        ProductMi partialUpdatedProductMi = new ProductMi();
        partialUpdatedProductMi.setId(productMi.getId());

        restProductMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductMi))
            )
            .andExpect(status().isOk());

        // Validate the ProductMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductMi, productMi),
            getPersistedProductMi(productMi)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductMiWithPatch() throws Exception {
        // Initialize the database
        insertedProductMi = productMiRepository.saveAndFlush(productMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productMi using partial update
        ProductMi partialUpdatedProductMi = new ProductMi();
        partialUpdatedProductMi.setId(productMi.getId());

        partialUpdatedProductMi.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);

        restProductMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductMi))
            )
            .andExpect(status().isOk());

        // Validate the ProductMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductMiUpdatableFieldsEquals(partialUpdatedProductMi, getPersistedProductMi(partialUpdatedProductMi));
    }

    @Test
    @Transactional
    void patchNonExistingProductMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productMi.setId(longCount.incrementAndGet());

        // Create the ProductMi
        ProductMiDTO productMiDTO = productMiMapper.toDto(productMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productMiDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productMi.setId(longCount.incrementAndGet());

        // Create the ProductMi
        ProductMiDTO productMiDTO = productMiMapper.toDto(productMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productMi.setId(longCount.incrementAndGet());

        // Create the ProductMi
        ProductMiDTO productMiDTO = productMiMapper.toDto(productMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductMi() throws Exception {
        // Initialize the database
        insertedProductMi = productMiRepository.saveAndFlush(productMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productMi
        restProductMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, productMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productMiRepository.count();
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

    protected ProductMi getPersistedProductMi(ProductMi productMi) {
        return productMiRepository.findById(productMi.getId()).orElseThrow();
    }

    protected void assertPersistedProductMiToMatchAllProperties(ProductMi expectedProductMi) {
        assertProductMiAllPropertiesEquals(expectedProductMi, getPersistedProductMi(expectedProductMi));
    }

    protected void assertPersistedProductMiToMatchUpdatableProperties(ProductMi expectedProductMi) {
        assertProductMiAllUpdatablePropertiesEquals(expectedProductMi, getPersistedProductMi(expectedProductMi));
    }
}
