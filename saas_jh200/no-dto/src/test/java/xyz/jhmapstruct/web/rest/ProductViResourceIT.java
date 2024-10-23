package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.ProductViAsserts.*;
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
import xyz.jhmapstruct.domain.CategoryVi;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.OrderVi;
import xyz.jhmapstruct.domain.ProductVi;
import xyz.jhmapstruct.domain.SupplierVi;
import xyz.jhmapstruct.repository.ProductViRepository;
import xyz.jhmapstruct.service.ProductViService;

/**
 * Integration tests for the {@link ProductViResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductViResourceIT {

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

    private static final String ENTITY_API_URL = "/api/product-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductViRepository productViRepository;

    @Mock
    private ProductViRepository productViRepositoryMock;

    @Mock
    private ProductViService productViServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductViMockMvc;

    private ProductVi productVi;

    private ProductVi insertedProductVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductVi createEntity() {
        return new ProductVi().name(DEFAULT_NAME).price(DEFAULT_PRICE).stock(DEFAULT_STOCK).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductVi createUpdatedEntity() {
        return new ProductVi().name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        productVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductVi != null) {
            productViRepository.delete(insertedProductVi);
            insertedProductVi = null;
        }
    }

    @Test
    @Transactional
    void createProductVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductVi
        var returnedProductVi = om.readValue(
            restProductViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductVi.class
        );

        // Validate the ProductVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductViUpdatableFieldsEquals(returnedProductVi, getPersistedProductVi(returnedProductVi));

        insertedProductVi = returnedProductVi;
    }

    @Test
    @Transactional
    void createProductViWithExistingId() throws Exception {
        // Create the ProductVi with an existing ID
        productVi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productVi)))
            .andExpect(status().isBadRequest());

        // Validate the ProductVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productVi.setName(null);

        // Create the ProductVi, which fails.

        restProductViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productVi.setPrice(null);

        // Create the ProductVi, which fails.

        restProductViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStockIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productVi.setStock(null);

        // Create the ProductVi, which fails.

        restProductViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductVis() throws Exception {
        // Initialize the database
        insertedProductVi = productViRepository.saveAndFlush(productVi);

        // Get all the productViList
        restProductViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductVisWithEagerRelationshipsIsEnabled() throws Exception {
        when(productViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productViServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductVisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productViRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProductVi() throws Exception {
        // Initialize the database
        insertedProductVi = productViRepository.saveAndFlush(productVi);

        // Get the productVi
        restProductViMockMvc
            .perform(get(ENTITY_API_URL_ID, productVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productVi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getProductVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductVi = productViRepository.saveAndFlush(productVi);

        Long id = productVi.getId();

        defaultProductViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductVisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductVi = productViRepository.saveAndFlush(productVi);

        // Get all the productViList where name equals to
        defaultProductViFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductVisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductVi = productViRepository.saveAndFlush(productVi);

        // Get all the productViList where name in
        defaultProductViFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductVisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductVi = productViRepository.saveAndFlush(productVi);

        // Get all the productViList where name is not null
        defaultProductViFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllProductVisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedProductVi = productViRepository.saveAndFlush(productVi);

        // Get all the productViList where name contains
        defaultProductViFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductVisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductVi = productViRepository.saveAndFlush(productVi);

        // Get all the productViList where name does not contain
        defaultProductViFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllProductVisByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductVi = productViRepository.saveAndFlush(productVi);

        // Get all the productViList where price equals to
        defaultProductViFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductVisByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductVi = productViRepository.saveAndFlush(productVi);

        // Get all the productViList where price in
        defaultProductViFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductVisByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductVi = productViRepository.saveAndFlush(productVi);

        // Get all the productViList where price is not null
        defaultProductViFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllProductVisByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductVi = productViRepository.saveAndFlush(productVi);

        // Get all the productViList where price is greater than or equal to
        defaultProductViFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductVisByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductVi = productViRepository.saveAndFlush(productVi);

        // Get all the productViList where price is less than or equal to
        defaultProductViFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllProductVisByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductVi = productViRepository.saveAndFlush(productVi);

        // Get all the productViList where price is less than
        defaultProductViFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductVisByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductVi = productViRepository.saveAndFlush(productVi);

        // Get all the productViList where price is greater than
        defaultProductViFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductVisByStockIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductVi = productViRepository.saveAndFlush(productVi);

        // Get all the productViList where stock equals to
        defaultProductViFiltering("stock.equals=" + DEFAULT_STOCK, "stock.equals=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProductVisByStockIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductVi = productViRepository.saveAndFlush(productVi);

        // Get all the productViList where stock in
        defaultProductViFiltering("stock.in=" + DEFAULT_STOCK + "," + UPDATED_STOCK, "stock.in=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProductVisByStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductVi = productViRepository.saveAndFlush(productVi);

        // Get all the productViList where stock is not null
        defaultProductViFiltering("stock.specified=true", "stock.specified=false");
    }

    @Test
    @Transactional
    void getAllProductVisByStockIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductVi = productViRepository.saveAndFlush(productVi);

        // Get all the productViList where stock is greater than or equal to
        defaultProductViFiltering("stock.greaterThanOrEqual=" + DEFAULT_STOCK, "stock.greaterThanOrEqual=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProductVisByStockIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductVi = productViRepository.saveAndFlush(productVi);

        // Get all the productViList where stock is less than or equal to
        defaultProductViFiltering("stock.lessThanOrEqual=" + DEFAULT_STOCK, "stock.lessThanOrEqual=" + SMALLER_STOCK);
    }

    @Test
    @Transactional
    void getAllProductVisByStockIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductVi = productViRepository.saveAndFlush(productVi);

        // Get all the productViList where stock is less than
        defaultProductViFiltering("stock.lessThan=" + UPDATED_STOCK, "stock.lessThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllProductVisByStockIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductVi = productViRepository.saveAndFlush(productVi);

        // Get all the productViList where stock is greater than
        defaultProductViFiltering("stock.greaterThan=" + SMALLER_STOCK, "stock.greaterThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllProductVisByCategoryIsEqualToSomething() throws Exception {
        CategoryVi category;
        if (TestUtil.findAll(em, CategoryVi.class).isEmpty()) {
            productViRepository.saveAndFlush(productVi);
            category = CategoryViResourceIT.createEntity();
        } else {
            category = TestUtil.findAll(em, CategoryVi.class).get(0);
        }
        em.persist(category);
        em.flush();
        productVi.setCategory(category);
        productViRepository.saveAndFlush(productVi);
        Long categoryId = category.getId();
        // Get all the productViList where category equals to categoryId
        defaultProductViShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the productViList where category equals to (categoryId + 1)
        defaultProductViShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllProductVisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            productViRepository.saveAndFlush(productVi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        productVi.setTenant(tenant);
        productViRepository.saveAndFlush(productVi);
        Long tenantId = tenant.getId();
        // Get all the productViList where tenant equals to tenantId
        defaultProductViShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the productViList where tenant equals to (tenantId + 1)
        defaultProductViShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllProductVisByOrderIsEqualToSomething() throws Exception {
        OrderVi order;
        if (TestUtil.findAll(em, OrderVi.class).isEmpty()) {
            productViRepository.saveAndFlush(productVi);
            order = OrderViResourceIT.createEntity();
        } else {
            order = TestUtil.findAll(em, OrderVi.class).get(0);
        }
        em.persist(order);
        em.flush();
        productVi.setOrder(order);
        productViRepository.saveAndFlush(productVi);
        Long orderId = order.getId();
        // Get all the productViList where order equals to orderId
        defaultProductViShouldBeFound("orderId.equals=" + orderId);

        // Get all the productViList where order equals to (orderId + 1)
        defaultProductViShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllProductVisBySuppliersIsEqualToSomething() throws Exception {
        SupplierVi suppliers;
        if (TestUtil.findAll(em, SupplierVi.class).isEmpty()) {
            productViRepository.saveAndFlush(productVi);
            suppliers = SupplierViResourceIT.createEntity();
        } else {
            suppliers = TestUtil.findAll(em, SupplierVi.class).get(0);
        }
        em.persist(suppliers);
        em.flush();
        productVi.addSuppliers(suppliers);
        productViRepository.saveAndFlush(productVi);
        Long suppliersId = suppliers.getId();
        // Get all the productViList where suppliers equals to suppliersId
        defaultProductViShouldBeFound("suppliersId.equals=" + suppliersId);

        // Get all the productViList where suppliers equals to (suppliersId + 1)
        defaultProductViShouldNotBeFound("suppliersId.equals=" + (suppliersId + 1));
    }

    private void defaultProductViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductViShouldBeFound(shouldBeFound);
        defaultProductViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductViShouldBeFound(String filter) throws Exception {
        restProductViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restProductViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductViShouldNotBeFound(String filter) throws Exception {
        restProductViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductVi() throws Exception {
        // Get the productVi
        restProductViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductVi() throws Exception {
        // Initialize the database
        insertedProductVi = productViRepository.saveAndFlush(productVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productVi
        ProductVi updatedProductVi = productViRepository.findById(productVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductVi are not directly saved in db
        em.detach(updatedProductVi);
        updatedProductVi.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);

        restProductViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductVi))
            )
            .andExpect(status().isOk());

        // Validate the ProductVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductViToMatchAllProperties(updatedProductVi);
    }

    @Test
    @Transactional
    void putNonExistingProductVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productVi.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductViWithPatch() throws Exception {
        // Initialize the database
        insertedProductVi = productViRepository.saveAndFlush(productVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productVi using partial update
        ProductVi partialUpdatedProductVi = new ProductVi();
        partialUpdatedProductVi.setId(productVi.getId());

        partialUpdatedProductVi.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restProductViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductVi))
            )
            .andExpect(status().isOk());

        // Validate the ProductVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductVi, productVi),
            getPersistedProductVi(productVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductViWithPatch() throws Exception {
        // Initialize the database
        insertedProductVi = productViRepository.saveAndFlush(productVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productVi using partial update
        ProductVi partialUpdatedProductVi = new ProductVi();
        partialUpdatedProductVi.setId(productVi.getId());

        partialUpdatedProductVi.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);

        restProductViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductVi))
            )
            .andExpect(status().isOk());

        // Validate the ProductVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductViUpdatableFieldsEquals(partialUpdatedProductVi, getPersistedProductVi(partialUpdatedProductVi));
    }

    @Test
    @Transactional
    void patchNonExistingProductVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductVi() throws Exception {
        // Initialize the database
        insertedProductVi = productViRepository.saveAndFlush(productVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productVi
        restProductViMockMvc
            .perform(delete(ENTITY_API_URL_ID, productVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productViRepository.count();
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

    protected ProductVi getPersistedProductVi(ProductVi productVi) {
        return productViRepository.findById(productVi.getId()).orElseThrow();
    }

    protected void assertPersistedProductViToMatchAllProperties(ProductVi expectedProductVi) {
        assertProductViAllPropertiesEquals(expectedProductVi, getPersistedProductVi(expectedProductVi));
    }

    protected void assertPersistedProductViToMatchUpdatableProperties(ProductVi expectedProductVi) {
        assertProductViAllUpdatablePropertiesEquals(expectedProductVi, getPersistedProductVi(expectedProductVi));
    }
}
