package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.ProductAlphaAsserts.*;
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
import xyz.jhmapstruct.domain.CategoryAlpha;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.OrderAlpha;
import xyz.jhmapstruct.domain.ProductAlpha;
import xyz.jhmapstruct.domain.SupplierAlpha;
import xyz.jhmapstruct.repository.ProductAlphaRepository;
import xyz.jhmapstruct.service.ProductAlphaService;

/**
 * Integration tests for the {@link ProductAlphaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductAlphaResourceIT {

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

    private static final String ENTITY_API_URL = "/api/product-alphas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductAlphaRepository productAlphaRepository;

    @Mock
    private ProductAlphaRepository productAlphaRepositoryMock;

    @Mock
    private ProductAlphaService productAlphaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductAlphaMockMvc;

    private ProductAlpha productAlpha;

    private ProductAlpha insertedProductAlpha;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductAlpha createEntity() {
        return new ProductAlpha().name(DEFAULT_NAME).price(DEFAULT_PRICE).stock(DEFAULT_STOCK).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductAlpha createUpdatedEntity() {
        return new ProductAlpha().name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        productAlpha = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductAlpha != null) {
            productAlphaRepository.delete(insertedProductAlpha);
            insertedProductAlpha = null;
        }
    }

    @Test
    @Transactional
    void createProductAlpha() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductAlpha
        var returnedProductAlpha = om.readValue(
            restProductAlphaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productAlpha)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductAlpha.class
        );

        // Validate the ProductAlpha in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductAlphaUpdatableFieldsEquals(returnedProductAlpha, getPersistedProductAlpha(returnedProductAlpha));

        insertedProductAlpha = returnedProductAlpha;
    }

    @Test
    @Transactional
    void createProductAlphaWithExistingId() throws Exception {
        // Create the ProductAlpha with an existing ID
        productAlpha.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productAlpha)))
            .andExpect(status().isBadRequest());

        // Validate the ProductAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productAlpha.setName(null);

        // Create the ProductAlpha, which fails.

        restProductAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productAlpha)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productAlpha.setPrice(null);

        // Create the ProductAlpha, which fails.

        restProductAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productAlpha)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStockIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productAlpha.setStock(null);

        // Create the ProductAlpha, which fails.

        restProductAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productAlpha)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductAlphas() throws Exception {
        // Initialize the database
        insertedProductAlpha = productAlphaRepository.saveAndFlush(productAlpha);

        // Get all the productAlphaList
        restProductAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductAlphasWithEagerRelationshipsIsEnabled() throws Exception {
        when(productAlphaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductAlphaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productAlphaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductAlphasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productAlphaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductAlphaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productAlphaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProductAlpha() throws Exception {
        // Initialize the database
        insertedProductAlpha = productAlphaRepository.saveAndFlush(productAlpha);

        // Get the productAlpha
        restProductAlphaMockMvc
            .perform(get(ENTITY_API_URL_ID, productAlpha.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productAlpha.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getProductAlphasByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductAlpha = productAlphaRepository.saveAndFlush(productAlpha);

        Long id = productAlpha.getId();

        defaultProductAlphaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductAlphaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductAlphaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductAlphasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductAlpha = productAlphaRepository.saveAndFlush(productAlpha);

        // Get all the productAlphaList where name equals to
        defaultProductAlphaFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductAlphasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductAlpha = productAlphaRepository.saveAndFlush(productAlpha);

        // Get all the productAlphaList where name in
        defaultProductAlphaFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductAlphasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductAlpha = productAlphaRepository.saveAndFlush(productAlpha);

        // Get all the productAlphaList where name is not null
        defaultProductAlphaFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllProductAlphasByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedProductAlpha = productAlphaRepository.saveAndFlush(productAlpha);

        // Get all the productAlphaList where name contains
        defaultProductAlphaFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductAlphasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductAlpha = productAlphaRepository.saveAndFlush(productAlpha);

        // Get all the productAlphaList where name does not contain
        defaultProductAlphaFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllProductAlphasByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductAlpha = productAlphaRepository.saveAndFlush(productAlpha);

        // Get all the productAlphaList where price equals to
        defaultProductAlphaFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductAlphasByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductAlpha = productAlphaRepository.saveAndFlush(productAlpha);

        // Get all the productAlphaList where price in
        defaultProductAlphaFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductAlphasByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductAlpha = productAlphaRepository.saveAndFlush(productAlpha);

        // Get all the productAlphaList where price is not null
        defaultProductAlphaFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllProductAlphasByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductAlpha = productAlphaRepository.saveAndFlush(productAlpha);

        // Get all the productAlphaList where price is greater than or equal to
        defaultProductAlphaFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductAlphasByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductAlpha = productAlphaRepository.saveAndFlush(productAlpha);

        // Get all the productAlphaList where price is less than or equal to
        defaultProductAlphaFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllProductAlphasByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductAlpha = productAlphaRepository.saveAndFlush(productAlpha);

        // Get all the productAlphaList where price is less than
        defaultProductAlphaFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductAlphasByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductAlpha = productAlphaRepository.saveAndFlush(productAlpha);

        // Get all the productAlphaList where price is greater than
        defaultProductAlphaFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductAlphasByStockIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductAlpha = productAlphaRepository.saveAndFlush(productAlpha);

        // Get all the productAlphaList where stock equals to
        defaultProductAlphaFiltering("stock.equals=" + DEFAULT_STOCK, "stock.equals=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProductAlphasByStockIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductAlpha = productAlphaRepository.saveAndFlush(productAlpha);

        // Get all the productAlphaList where stock in
        defaultProductAlphaFiltering("stock.in=" + DEFAULT_STOCK + "," + UPDATED_STOCK, "stock.in=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProductAlphasByStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductAlpha = productAlphaRepository.saveAndFlush(productAlpha);

        // Get all the productAlphaList where stock is not null
        defaultProductAlphaFiltering("stock.specified=true", "stock.specified=false");
    }

    @Test
    @Transactional
    void getAllProductAlphasByStockIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductAlpha = productAlphaRepository.saveAndFlush(productAlpha);

        // Get all the productAlphaList where stock is greater than or equal to
        defaultProductAlphaFiltering("stock.greaterThanOrEqual=" + DEFAULT_STOCK, "stock.greaterThanOrEqual=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProductAlphasByStockIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductAlpha = productAlphaRepository.saveAndFlush(productAlpha);

        // Get all the productAlphaList where stock is less than or equal to
        defaultProductAlphaFiltering("stock.lessThanOrEqual=" + DEFAULT_STOCK, "stock.lessThanOrEqual=" + SMALLER_STOCK);
    }

    @Test
    @Transactional
    void getAllProductAlphasByStockIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductAlpha = productAlphaRepository.saveAndFlush(productAlpha);

        // Get all the productAlphaList where stock is less than
        defaultProductAlphaFiltering("stock.lessThan=" + UPDATED_STOCK, "stock.lessThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllProductAlphasByStockIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductAlpha = productAlphaRepository.saveAndFlush(productAlpha);

        // Get all the productAlphaList where stock is greater than
        defaultProductAlphaFiltering("stock.greaterThan=" + SMALLER_STOCK, "stock.greaterThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllProductAlphasByCategoryIsEqualToSomething() throws Exception {
        CategoryAlpha category;
        if (TestUtil.findAll(em, CategoryAlpha.class).isEmpty()) {
            productAlphaRepository.saveAndFlush(productAlpha);
            category = CategoryAlphaResourceIT.createEntity();
        } else {
            category = TestUtil.findAll(em, CategoryAlpha.class).get(0);
        }
        em.persist(category);
        em.flush();
        productAlpha.setCategory(category);
        productAlphaRepository.saveAndFlush(productAlpha);
        Long categoryId = category.getId();
        // Get all the productAlphaList where category equals to categoryId
        defaultProductAlphaShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the productAlphaList where category equals to (categoryId + 1)
        defaultProductAlphaShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllProductAlphasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            productAlphaRepository.saveAndFlush(productAlpha);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        productAlpha.setTenant(tenant);
        productAlphaRepository.saveAndFlush(productAlpha);
        Long tenantId = tenant.getId();
        // Get all the productAlphaList where tenant equals to tenantId
        defaultProductAlphaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the productAlphaList where tenant equals to (tenantId + 1)
        defaultProductAlphaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllProductAlphasByOrderIsEqualToSomething() throws Exception {
        OrderAlpha order;
        if (TestUtil.findAll(em, OrderAlpha.class).isEmpty()) {
            productAlphaRepository.saveAndFlush(productAlpha);
            order = OrderAlphaResourceIT.createEntity();
        } else {
            order = TestUtil.findAll(em, OrderAlpha.class).get(0);
        }
        em.persist(order);
        em.flush();
        productAlpha.setOrder(order);
        productAlphaRepository.saveAndFlush(productAlpha);
        Long orderId = order.getId();
        // Get all the productAlphaList where order equals to orderId
        defaultProductAlphaShouldBeFound("orderId.equals=" + orderId);

        // Get all the productAlphaList where order equals to (orderId + 1)
        defaultProductAlphaShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllProductAlphasBySuppliersIsEqualToSomething() throws Exception {
        SupplierAlpha suppliers;
        if (TestUtil.findAll(em, SupplierAlpha.class).isEmpty()) {
            productAlphaRepository.saveAndFlush(productAlpha);
            suppliers = SupplierAlphaResourceIT.createEntity();
        } else {
            suppliers = TestUtil.findAll(em, SupplierAlpha.class).get(0);
        }
        em.persist(suppliers);
        em.flush();
        productAlpha.addSuppliers(suppliers);
        productAlphaRepository.saveAndFlush(productAlpha);
        Long suppliersId = suppliers.getId();
        // Get all the productAlphaList where suppliers equals to suppliersId
        defaultProductAlphaShouldBeFound("suppliersId.equals=" + suppliersId);

        // Get all the productAlphaList where suppliers equals to (suppliersId + 1)
        defaultProductAlphaShouldNotBeFound("suppliersId.equals=" + (suppliersId + 1));
    }

    private void defaultProductAlphaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductAlphaShouldBeFound(shouldBeFound);
        defaultProductAlphaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductAlphaShouldBeFound(String filter) throws Exception {
        restProductAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restProductAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductAlphaShouldNotBeFound(String filter) throws Exception {
        restProductAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductAlpha() throws Exception {
        // Get the productAlpha
        restProductAlphaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductAlpha() throws Exception {
        // Initialize the database
        insertedProductAlpha = productAlphaRepository.saveAndFlush(productAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productAlpha
        ProductAlpha updatedProductAlpha = productAlphaRepository.findById(productAlpha.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductAlpha are not directly saved in db
        em.detach(updatedProductAlpha);
        updatedProductAlpha.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);

        restProductAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductAlpha.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductAlpha))
            )
            .andExpect(status().isOk());

        // Validate the ProductAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductAlphaToMatchAllProperties(updatedProductAlpha);
    }

    @Test
    @Transactional
    void putNonExistingProductAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productAlpha.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productAlpha.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductAlphaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productAlpha)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedProductAlpha = productAlphaRepository.saveAndFlush(productAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productAlpha using partial update
        ProductAlpha partialUpdatedProductAlpha = new ProductAlpha();
        partialUpdatedProductAlpha.setId(productAlpha.getId());

        partialUpdatedProductAlpha.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restProductAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductAlpha))
            )
            .andExpect(status().isOk());

        // Validate the ProductAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductAlphaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductAlpha, productAlpha),
            getPersistedProductAlpha(productAlpha)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedProductAlpha = productAlphaRepository.saveAndFlush(productAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productAlpha using partial update
        ProductAlpha partialUpdatedProductAlpha = new ProductAlpha();
        partialUpdatedProductAlpha.setId(productAlpha.getId());

        partialUpdatedProductAlpha.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);

        restProductAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductAlpha))
            )
            .andExpect(status().isOk());

        // Validate the ProductAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductAlphaUpdatableFieldsEquals(partialUpdatedProductAlpha, getPersistedProductAlpha(partialUpdatedProductAlpha));
    }

    @Test
    @Transactional
    void patchNonExistingProductAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productAlpha.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductAlphaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productAlpha)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductAlpha() throws Exception {
        // Initialize the database
        insertedProductAlpha = productAlphaRepository.saveAndFlush(productAlpha);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productAlpha
        restProductAlphaMockMvc
            .perform(delete(ENTITY_API_URL_ID, productAlpha.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productAlphaRepository.count();
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

    protected ProductAlpha getPersistedProductAlpha(ProductAlpha productAlpha) {
        return productAlphaRepository.findById(productAlpha.getId()).orElseThrow();
    }

    protected void assertPersistedProductAlphaToMatchAllProperties(ProductAlpha expectedProductAlpha) {
        assertProductAlphaAllPropertiesEquals(expectedProductAlpha, getPersistedProductAlpha(expectedProductAlpha));
    }

    protected void assertPersistedProductAlphaToMatchUpdatableProperties(ProductAlpha expectedProductAlpha) {
        assertProductAlphaAllUpdatablePropertiesEquals(expectedProductAlpha, getPersistedProductAlpha(expectedProductAlpha));
    }
}
