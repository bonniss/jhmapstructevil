package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextProductAsserts.*;
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
import xyz.jhmapstruct.domain.NextCategory;
import xyz.jhmapstruct.domain.NextOrder;
import xyz.jhmapstruct.domain.NextProduct;
import xyz.jhmapstruct.domain.NextSupplier;
import xyz.jhmapstruct.repository.NextProductRepository;
import xyz.jhmapstruct.service.NextProductService;

/**
 * Integration tests for the {@link NextProductResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextProductResourceIT {

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

    private static final String ENTITY_API_URL = "/api/next-products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextProductRepository nextProductRepository;

    @Mock
    private NextProductRepository nextProductRepositoryMock;

    @Mock
    private NextProductService nextProductServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextProductMockMvc;

    private NextProduct nextProduct;

    private NextProduct insertedNextProduct;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextProduct createEntity() {
        return new NextProduct().name(DEFAULT_NAME).price(DEFAULT_PRICE).stock(DEFAULT_STOCK).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextProduct createUpdatedEntity() {
        return new NextProduct().name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        nextProduct = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextProduct != null) {
            nextProductRepository.delete(insertedNextProduct);
            insertedNextProduct = null;
        }
    }

    @Test
    @Transactional
    void createNextProduct() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextProduct
        var returnedNextProduct = om.readValue(
            restNextProductMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProduct)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextProduct.class
        );

        // Validate the NextProduct in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextProductUpdatableFieldsEquals(returnedNextProduct, getPersistedNextProduct(returnedNextProduct));

        insertedNextProduct = returnedNextProduct;
    }

    @Test
    @Transactional
    void createNextProductWithExistingId() throws Exception {
        // Create the NextProduct with an existing ID
        nextProduct.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProduct)))
            .andExpect(status().isBadRequest());

        // Validate the NextProduct in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextProduct.setName(null);

        // Create the NextProduct, which fails.

        restNextProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProduct)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextProduct.setPrice(null);

        // Create the NextProduct, which fails.

        restNextProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProduct)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStockIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextProduct.setStock(null);

        // Create the NextProduct, which fails.

        restNextProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProduct)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextProducts() throws Exception {
        // Initialize the database
        insertedNextProduct = nextProductRepository.saveAndFlush(nextProduct);

        // Get all the nextProductList
        restNextProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextProductsWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextProductServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextProductMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextProductServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextProductsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextProductServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextProductMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextProductRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextProduct() throws Exception {
        // Initialize the database
        insertedNextProduct = nextProductRepository.saveAndFlush(nextProduct);

        // Get the nextProduct
        restNextProductMockMvc
            .perform(get(ENTITY_API_URL_ID, nextProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextProduct.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getNextProductsByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextProduct = nextProductRepository.saveAndFlush(nextProduct);

        Long id = nextProduct.getId();

        defaultNextProductFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextProductFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextProductFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextProductsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProduct = nextProductRepository.saveAndFlush(nextProduct);

        // Get all the nextProductList where name equals to
        defaultNextProductFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextProduct = nextProductRepository.saveAndFlush(nextProduct);

        // Get all the nextProductList where name in
        defaultNextProductFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextProduct = nextProductRepository.saveAndFlush(nextProduct);

        // Get all the nextProductList where name is not null
        defaultNextProductFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllNextProductsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextProduct = nextProductRepository.saveAndFlush(nextProduct);

        // Get all the nextProductList where name contains
        defaultNextProductFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextProduct = nextProductRepository.saveAndFlush(nextProduct);

        // Get all the nextProductList where name does not contain
        defaultNextProductFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProduct = nextProductRepository.saveAndFlush(nextProduct);

        // Get all the nextProductList where price equals to
        defaultNextProductFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextProduct = nextProductRepository.saveAndFlush(nextProduct);

        // Get all the nextProductList where price in
        defaultNextProductFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextProduct = nextProductRepository.saveAndFlush(nextProduct);

        // Get all the nextProductList where price is not null
        defaultNextProductFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllNextProductsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProduct = nextProductRepository.saveAndFlush(nextProduct);

        // Get all the nextProductList where price is greater than or equal to
        defaultNextProductFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProduct = nextProductRepository.saveAndFlush(nextProduct);

        // Get all the nextProductList where price is less than or equal to
        defaultNextProductFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextProduct = nextProductRepository.saveAndFlush(nextProduct);

        // Get all the nextProductList where price is less than
        defaultNextProductFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextProduct = nextProductRepository.saveAndFlush(nextProduct);

        // Get all the nextProductList where price is greater than
        defaultNextProductFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductsByStockIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProduct = nextProductRepository.saveAndFlush(nextProduct);

        // Get all the nextProductList where stock equals to
        defaultNextProductFiltering("stock.equals=" + DEFAULT_STOCK, "stock.equals=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductsByStockIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextProduct = nextProductRepository.saveAndFlush(nextProduct);

        // Get all the nextProductList where stock in
        defaultNextProductFiltering("stock.in=" + DEFAULT_STOCK + "," + UPDATED_STOCK, "stock.in=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductsByStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextProduct = nextProductRepository.saveAndFlush(nextProduct);

        // Get all the nextProductList where stock is not null
        defaultNextProductFiltering("stock.specified=true", "stock.specified=false");
    }

    @Test
    @Transactional
    void getAllNextProductsByStockIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProduct = nextProductRepository.saveAndFlush(nextProduct);

        // Get all the nextProductList where stock is greater than or equal to
        defaultNextProductFiltering("stock.greaterThanOrEqual=" + DEFAULT_STOCK, "stock.greaterThanOrEqual=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductsByStockIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProduct = nextProductRepository.saveAndFlush(nextProduct);

        // Get all the nextProductList where stock is less than or equal to
        defaultNextProductFiltering("stock.lessThanOrEqual=" + DEFAULT_STOCK, "stock.lessThanOrEqual=" + SMALLER_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductsByStockIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextProduct = nextProductRepository.saveAndFlush(nextProduct);

        // Get all the nextProductList where stock is less than
        defaultNextProductFiltering("stock.lessThan=" + UPDATED_STOCK, "stock.lessThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductsByStockIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextProduct = nextProductRepository.saveAndFlush(nextProduct);

        // Get all the nextProductList where stock is greater than
        defaultNextProductFiltering("stock.greaterThan=" + SMALLER_STOCK, "stock.greaterThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductsByCategoryIsEqualToSomething() throws Exception {
        NextCategory category;
        if (TestUtil.findAll(em, NextCategory.class).isEmpty()) {
            nextProductRepository.saveAndFlush(nextProduct);
            category = NextCategoryResourceIT.createEntity();
        } else {
            category = TestUtil.findAll(em, NextCategory.class).get(0);
        }
        em.persist(category);
        em.flush();
        nextProduct.setCategory(category);
        nextProductRepository.saveAndFlush(nextProduct);
        Long categoryId = category.getId();
        // Get all the nextProductList where category equals to categoryId
        defaultNextProductShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the nextProductList where category equals to (categoryId + 1)
        defaultNextProductShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllNextProductsByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextProductRepository.saveAndFlush(nextProduct);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextProduct.setTenant(tenant);
        nextProductRepository.saveAndFlush(nextProduct);
        Long tenantId = tenant.getId();
        // Get all the nextProductList where tenant equals to tenantId
        defaultNextProductShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextProductList where tenant equals to (tenantId + 1)
        defaultNextProductShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllNextProductsByOrderIsEqualToSomething() throws Exception {
        NextOrder order;
        if (TestUtil.findAll(em, NextOrder.class).isEmpty()) {
            nextProductRepository.saveAndFlush(nextProduct);
            order = NextOrderResourceIT.createEntity();
        } else {
            order = TestUtil.findAll(em, NextOrder.class).get(0);
        }
        em.persist(order);
        em.flush();
        nextProduct.setOrder(order);
        nextProductRepository.saveAndFlush(nextProduct);
        Long orderId = order.getId();
        // Get all the nextProductList where order equals to orderId
        defaultNextProductShouldBeFound("orderId.equals=" + orderId);

        // Get all the nextProductList where order equals to (orderId + 1)
        defaultNextProductShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllNextProductsBySuppliersIsEqualToSomething() throws Exception {
        NextSupplier suppliers;
        if (TestUtil.findAll(em, NextSupplier.class).isEmpty()) {
            nextProductRepository.saveAndFlush(nextProduct);
            suppliers = NextSupplierResourceIT.createEntity();
        } else {
            suppliers = TestUtil.findAll(em, NextSupplier.class).get(0);
        }
        em.persist(suppliers);
        em.flush();
        nextProduct.addSuppliers(suppliers);
        nextProductRepository.saveAndFlush(nextProduct);
        Long suppliersId = suppliers.getId();
        // Get all the nextProductList where suppliers equals to suppliersId
        defaultNextProductShouldBeFound("suppliersId.equals=" + suppliersId);

        // Get all the nextProductList where suppliers equals to (suppliersId + 1)
        defaultNextProductShouldNotBeFound("suppliersId.equals=" + (suppliersId + 1));
    }

    private void defaultNextProductFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextProductShouldBeFound(shouldBeFound);
        defaultNextProductShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextProductShouldBeFound(String filter) throws Exception {
        restNextProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restNextProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextProductShouldNotBeFound(String filter) throws Exception {
        restNextProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextProduct() throws Exception {
        // Get the nextProduct
        restNextProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextProduct() throws Exception {
        // Initialize the database
        insertedNextProduct = nextProductRepository.saveAndFlush(nextProduct);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextProduct
        NextProduct updatedNextProduct = nextProductRepository.findById(nextProduct.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextProduct are not directly saved in db
        em.detach(updatedNextProduct);
        updatedNextProduct.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);

        restNextProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextProduct.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextProduct))
            )
            .andExpect(status().isOk());

        // Validate the NextProduct in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextProductToMatchAllProperties(updatedNextProduct);
    }

    @Test
    @Transactional
    void putNonExistingNextProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProduct.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextProduct.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProduct in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProduct.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProduct in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProduct.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProduct)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextProduct in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextProductWithPatch() throws Exception {
        // Initialize the database
        insertedNextProduct = nextProductRepository.saveAndFlush(nextProduct);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextProduct using partial update
        NextProduct partialUpdatedNextProduct = new NextProduct();
        partialUpdatedNextProduct.setId(nextProduct.getId());

        partialUpdatedNextProduct.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK);

        restNextProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextProduct))
            )
            .andExpect(status().isOk());

        // Validate the NextProduct in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextProductUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextProduct, nextProduct),
            getPersistedNextProduct(nextProduct)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextProductWithPatch() throws Exception {
        // Initialize the database
        insertedNextProduct = nextProductRepository.saveAndFlush(nextProduct);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextProduct using partial update
        NextProduct partialUpdatedNextProduct = new NextProduct();
        partialUpdatedNextProduct.setId(nextProduct.getId());

        partialUpdatedNextProduct.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);

        restNextProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextProduct))
            )
            .andExpect(status().isOk());

        // Validate the NextProduct in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextProductUpdatableFieldsEquals(partialUpdatedNextProduct, getPersistedNextProduct(partialUpdatedNextProduct));
    }

    @Test
    @Transactional
    void patchNonExistingNextProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProduct.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProduct in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProduct.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProduct in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProduct.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextProduct)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextProduct in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextProduct() throws Exception {
        // Initialize the database
        insertedNextProduct = nextProductRepository.saveAndFlush(nextProduct);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextProduct
        restNextProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextProduct.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextProductRepository.count();
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

    protected NextProduct getPersistedNextProduct(NextProduct nextProduct) {
        return nextProductRepository.findById(nextProduct.getId()).orElseThrow();
    }

    protected void assertPersistedNextProductToMatchAllProperties(NextProduct expectedNextProduct) {
        assertNextProductAllPropertiesEquals(expectedNextProduct, getPersistedNextProduct(expectedNextProduct));
    }

    protected void assertPersistedNextProductToMatchUpdatableProperties(NextProduct expectedNextProduct) {
        assertNextProductAllUpdatablePropertiesEquals(expectedNextProduct, getPersistedNextProduct(expectedNextProduct));
    }
}
