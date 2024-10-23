package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextProductGammaAsserts.*;
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
import xyz.jhmapstruct.domain.NextCategoryGamma;
import xyz.jhmapstruct.domain.NextOrderGamma;
import xyz.jhmapstruct.domain.NextProductGamma;
import xyz.jhmapstruct.domain.NextSupplierGamma;
import xyz.jhmapstruct.repository.NextProductGammaRepository;
import xyz.jhmapstruct.service.NextProductGammaService;

/**
 * Integration tests for the {@link NextProductGammaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextProductGammaResourceIT {

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

    private static final String ENTITY_API_URL = "/api/next-product-gammas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextProductGammaRepository nextProductGammaRepository;

    @Mock
    private NextProductGammaRepository nextProductGammaRepositoryMock;

    @Mock
    private NextProductGammaService nextProductGammaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextProductGammaMockMvc;

    private NextProductGamma nextProductGamma;

    private NextProductGamma insertedNextProductGamma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextProductGamma createEntity() {
        return new NextProductGamma().name(DEFAULT_NAME).price(DEFAULT_PRICE).stock(DEFAULT_STOCK).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextProductGamma createUpdatedEntity() {
        return new NextProductGamma().name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        nextProductGamma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextProductGamma != null) {
            nextProductGammaRepository.delete(insertedNextProductGamma);
            insertedNextProductGamma = null;
        }
    }

    @Test
    @Transactional
    void createNextProductGamma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextProductGamma
        var returnedNextProductGamma = om.readValue(
            restNextProductGammaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductGamma)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextProductGamma.class
        );

        // Validate the NextProductGamma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextProductGammaUpdatableFieldsEquals(returnedNextProductGamma, getPersistedNextProductGamma(returnedNextProductGamma));

        insertedNextProductGamma = returnedNextProductGamma;
    }

    @Test
    @Transactional
    void createNextProductGammaWithExistingId() throws Exception {
        // Create the NextProductGamma with an existing ID
        nextProductGamma.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextProductGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductGamma)))
            .andExpect(status().isBadRequest());

        // Validate the NextProductGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextProductGamma.setName(null);

        // Create the NextProductGamma, which fails.

        restNextProductGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductGamma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextProductGamma.setPrice(null);

        // Create the NextProductGamma, which fails.

        restNextProductGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductGamma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStockIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextProductGamma.setStock(null);

        // Create the NextProductGamma, which fails.

        restNextProductGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductGamma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextProductGammas() throws Exception {
        // Initialize the database
        insertedNextProductGamma = nextProductGammaRepository.saveAndFlush(nextProductGamma);

        // Get all the nextProductGammaList
        restNextProductGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextProductGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextProductGammasWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextProductGammaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextProductGammaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextProductGammaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextProductGammasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextProductGammaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextProductGammaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextProductGammaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextProductGamma() throws Exception {
        // Initialize the database
        insertedNextProductGamma = nextProductGammaRepository.saveAndFlush(nextProductGamma);

        // Get the nextProductGamma
        restNextProductGammaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextProductGamma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextProductGamma.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getNextProductGammasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextProductGamma = nextProductGammaRepository.saveAndFlush(nextProductGamma);

        Long id = nextProductGamma.getId();

        defaultNextProductGammaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextProductGammaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextProductGammaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextProductGammasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductGamma = nextProductGammaRepository.saveAndFlush(nextProductGamma);

        // Get all the nextProductGammaList where name equals to
        defaultNextProductGammaFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductGammasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextProductGamma = nextProductGammaRepository.saveAndFlush(nextProductGamma);

        // Get all the nextProductGammaList where name in
        defaultNextProductGammaFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductGammasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextProductGamma = nextProductGammaRepository.saveAndFlush(nextProductGamma);

        // Get all the nextProductGammaList where name is not null
        defaultNextProductGammaFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllNextProductGammasByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextProductGamma = nextProductGammaRepository.saveAndFlush(nextProductGamma);

        // Get all the nextProductGammaList where name contains
        defaultNextProductGammaFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductGammasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextProductGamma = nextProductGammaRepository.saveAndFlush(nextProductGamma);

        // Get all the nextProductGammaList where name does not contain
        defaultNextProductGammaFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductGammasByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductGamma = nextProductGammaRepository.saveAndFlush(nextProductGamma);

        // Get all the nextProductGammaList where price equals to
        defaultNextProductGammaFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductGammasByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextProductGamma = nextProductGammaRepository.saveAndFlush(nextProductGamma);

        // Get all the nextProductGammaList where price in
        defaultNextProductGammaFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductGammasByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextProductGamma = nextProductGammaRepository.saveAndFlush(nextProductGamma);

        // Get all the nextProductGammaList where price is not null
        defaultNextProductGammaFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllNextProductGammasByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductGamma = nextProductGammaRepository.saveAndFlush(nextProductGamma);

        // Get all the nextProductGammaList where price is greater than or equal to
        defaultNextProductGammaFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductGammasByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductGamma = nextProductGammaRepository.saveAndFlush(nextProductGamma);

        // Get all the nextProductGammaList where price is less than or equal to
        defaultNextProductGammaFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductGammasByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextProductGamma = nextProductGammaRepository.saveAndFlush(nextProductGamma);

        // Get all the nextProductGammaList where price is less than
        defaultNextProductGammaFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductGammasByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextProductGamma = nextProductGammaRepository.saveAndFlush(nextProductGamma);

        // Get all the nextProductGammaList where price is greater than
        defaultNextProductGammaFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductGammasByStockIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductGamma = nextProductGammaRepository.saveAndFlush(nextProductGamma);

        // Get all the nextProductGammaList where stock equals to
        defaultNextProductGammaFiltering("stock.equals=" + DEFAULT_STOCK, "stock.equals=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductGammasByStockIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextProductGamma = nextProductGammaRepository.saveAndFlush(nextProductGamma);

        // Get all the nextProductGammaList where stock in
        defaultNextProductGammaFiltering("stock.in=" + DEFAULT_STOCK + "," + UPDATED_STOCK, "stock.in=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductGammasByStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextProductGamma = nextProductGammaRepository.saveAndFlush(nextProductGamma);

        // Get all the nextProductGammaList where stock is not null
        defaultNextProductGammaFiltering("stock.specified=true", "stock.specified=false");
    }

    @Test
    @Transactional
    void getAllNextProductGammasByStockIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductGamma = nextProductGammaRepository.saveAndFlush(nextProductGamma);

        // Get all the nextProductGammaList where stock is greater than or equal to
        defaultNextProductGammaFiltering("stock.greaterThanOrEqual=" + DEFAULT_STOCK, "stock.greaterThanOrEqual=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductGammasByStockIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductGamma = nextProductGammaRepository.saveAndFlush(nextProductGamma);

        // Get all the nextProductGammaList where stock is less than or equal to
        defaultNextProductGammaFiltering("stock.lessThanOrEqual=" + DEFAULT_STOCK, "stock.lessThanOrEqual=" + SMALLER_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductGammasByStockIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextProductGamma = nextProductGammaRepository.saveAndFlush(nextProductGamma);

        // Get all the nextProductGammaList where stock is less than
        defaultNextProductGammaFiltering("stock.lessThan=" + UPDATED_STOCK, "stock.lessThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductGammasByStockIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextProductGamma = nextProductGammaRepository.saveAndFlush(nextProductGamma);

        // Get all the nextProductGammaList where stock is greater than
        defaultNextProductGammaFiltering("stock.greaterThan=" + SMALLER_STOCK, "stock.greaterThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductGammasByCategoryIsEqualToSomething() throws Exception {
        NextCategoryGamma category;
        if (TestUtil.findAll(em, NextCategoryGamma.class).isEmpty()) {
            nextProductGammaRepository.saveAndFlush(nextProductGamma);
            category = NextCategoryGammaResourceIT.createEntity();
        } else {
            category = TestUtil.findAll(em, NextCategoryGamma.class).get(0);
        }
        em.persist(category);
        em.flush();
        nextProductGamma.setCategory(category);
        nextProductGammaRepository.saveAndFlush(nextProductGamma);
        Long categoryId = category.getId();
        // Get all the nextProductGammaList where category equals to categoryId
        defaultNextProductGammaShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the nextProductGammaList where category equals to (categoryId + 1)
        defaultNextProductGammaShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllNextProductGammasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextProductGammaRepository.saveAndFlush(nextProductGamma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextProductGamma.setTenant(tenant);
        nextProductGammaRepository.saveAndFlush(nextProductGamma);
        Long tenantId = tenant.getId();
        // Get all the nextProductGammaList where tenant equals to tenantId
        defaultNextProductGammaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextProductGammaList where tenant equals to (tenantId + 1)
        defaultNextProductGammaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllNextProductGammasByOrderIsEqualToSomething() throws Exception {
        NextOrderGamma order;
        if (TestUtil.findAll(em, NextOrderGamma.class).isEmpty()) {
            nextProductGammaRepository.saveAndFlush(nextProductGamma);
            order = NextOrderGammaResourceIT.createEntity();
        } else {
            order = TestUtil.findAll(em, NextOrderGamma.class).get(0);
        }
        em.persist(order);
        em.flush();
        nextProductGamma.setOrder(order);
        nextProductGammaRepository.saveAndFlush(nextProductGamma);
        Long orderId = order.getId();
        // Get all the nextProductGammaList where order equals to orderId
        defaultNextProductGammaShouldBeFound("orderId.equals=" + orderId);

        // Get all the nextProductGammaList where order equals to (orderId + 1)
        defaultNextProductGammaShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllNextProductGammasBySuppliersIsEqualToSomething() throws Exception {
        NextSupplierGamma suppliers;
        if (TestUtil.findAll(em, NextSupplierGamma.class).isEmpty()) {
            nextProductGammaRepository.saveAndFlush(nextProductGamma);
            suppliers = NextSupplierGammaResourceIT.createEntity();
        } else {
            suppliers = TestUtil.findAll(em, NextSupplierGamma.class).get(0);
        }
        em.persist(suppliers);
        em.flush();
        nextProductGamma.addSuppliers(suppliers);
        nextProductGammaRepository.saveAndFlush(nextProductGamma);
        Long suppliersId = suppliers.getId();
        // Get all the nextProductGammaList where suppliers equals to suppliersId
        defaultNextProductGammaShouldBeFound("suppliersId.equals=" + suppliersId);

        // Get all the nextProductGammaList where suppliers equals to (suppliersId + 1)
        defaultNextProductGammaShouldNotBeFound("suppliersId.equals=" + (suppliersId + 1));
    }

    private void defaultNextProductGammaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextProductGammaShouldBeFound(shouldBeFound);
        defaultNextProductGammaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextProductGammaShouldBeFound(String filter) throws Exception {
        restNextProductGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextProductGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restNextProductGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextProductGammaShouldNotBeFound(String filter) throws Exception {
        restNextProductGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextProductGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextProductGamma() throws Exception {
        // Get the nextProductGamma
        restNextProductGammaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextProductGamma() throws Exception {
        // Initialize the database
        insertedNextProductGamma = nextProductGammaRepository.saveAndFlush(nextProductGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextProductGamma
        NextProductGamma updatedNextProductGamma = nextProductGammaRepository.findById(nextProductGamma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextProductGamma are not directly saved in db
        em.detach(updatedNextProductGamma);
        updatedNextProductGamma.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);

        restNextProductGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextProductGamma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextProductGamma))
            )
            .andExpect(status().isOk());

        // Validate the NextProductGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextProductGammaToMatchAllProperties(updatedNextProductGamma);
    }

    @Test
    @Transactional
    void putNonExistingNextProductGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductGamma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextProductGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextProductGamma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextProductGamma))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProductGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextProductGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductGamma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextProductGamma))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProductGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextProductGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductGamma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductGammaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductGamma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextProductGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextProductGammaWithPatch() throws Exception {
        // Initialize the database
        insertedNextProductGamma = nextProductGammaRepository.saveAndFlush(nextProductGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextProductGamma using partial update
        NextProductGamma partialUpdatedNextProductGamma = new NextProductGamma();
        partialUpdatedNextProductGamma.setId(nextProductGamma.getId());

        partialUpdatedNextProductGamma.name(UPDATED_NAME).stock(UPDATED_STOCK);

        restNextProductGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextProductGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextProductGamma))
            )
            .andExpect(status().isOk());

        // Validate the NextProductGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextProductGammaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextProductGamma, nextProductGamma),
            getPersistedNextProductGamma(nextProductGamma)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextProductGammaWithPatch() throws Exception {
        // Initialize the database
        insertedNextProductGamma = nextProductGammaRepository.saveAndFlush(nextProductGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextProductGamma using partial update
        NextProductGamma partialUpdatedNextProductGamma = new NextProductGamma();
        partialUpdatedNextProductGamma.setId(nextProductGamma.getId());

        partialUpdatedNextProductGamma.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);

        restNextProductGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextProductGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextProductGamma))
            )
            .andExpect(status().isOk());

        // Validate the NextProductGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextProductGammaUpdatableFieldsEquals(
            partialUpdatedNextProductGamma,
            getPersistedNextProductGamma(partialUpdatedNextProductGamma)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextProductGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductGamma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextProductGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextProductGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextProductGamma))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProductGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextProductGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductGamma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextProductGamma))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProductGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextProductGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductGamma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductGammaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextProductGamma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextProductGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextProductGamma() throws Exception {
        // Initialize the database
        insertedNextProductGamma = nextProductGammaRepository.saveAndFlush(nextProductGamma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextProductGamma
        restNextProductGammaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextProductGamma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextProductGammaRepository.count();
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

    protected NextProductGamma getPersistedNextProductGamma(NextProductGamma nextProductGamma) {
        return nextProductGammaRepository.findById(nextProductGamma.getId()).orElseThrow();
    }

    protected void assertPersistedNextProductGammaToMatchAllProperties(NextProductGamma expectedNextProductGamma) {
        assertNextProductGammaAllPropertiesEquals(expectedNextProductGamma, getPersistedNextProductGamma(expectedNextProductGamma));
    }

    protected void assertPersistedNextProductGammaToMatchUpdatableProperties(NextProductGamma expectedNextProductGamma) {
        assertNextProductGammaAllUpdatablePropertiesEquals(
            expectedNextProductGamma,
            getPersistedNextProductGamma(expectedNextProductGamma)
        );
    }
}
