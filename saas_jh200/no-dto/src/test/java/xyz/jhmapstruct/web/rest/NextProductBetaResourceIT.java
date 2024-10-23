package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextProductBetaAsserts.*;
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
import xyz.jhmapstruct.domain.NextCategoryBeta;
import xyz.jhmapstruct.domain.NextOrderBeta;
import xyz.jhmapstruct.domain.NextProductBeta;
import xyz.jhmapstruct.domain.NextSupplierBeta;
import xyz.jhmapstruct.repository.NextProductBetaRepository;
import xyz.jhmapstruct.service.NextProductBetaService;

/**
 * Integration tests for the {@link NextProductBetaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextProductBetaResourceIT {

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

    private static final String ENTITY_API_URL = "/api/next-product-betas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextProductBetaRepository nextProductBetaRepository;

    @Mock
    private NextProductBetaRepository nextProductBetaRepositoryMock;

    @Mock
    private NextProductBetaService nextProductBetaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextProductBetaMockMvc;

    private NextProductBeta nextProductBeta;

    private NextProductBeta insertedNextProductBeta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextProductBeta createEntity() {
        return new NextProductBeta().name(DEFAULT_NAME).price(DEFAULT_PRICE).stock(DEFAULT_STOCK).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextProductBeta createUpdatedEntity() {
        return new NextProductBeta().name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        nextProductBeta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextProductBeta != null) {
            nextProductBetaRepository.delete(insertedNextProductBeta);
            insertedNextProductBeta = null;
        }
    }

    @Test
    @Transactional
    void createNextProductBeta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextProductBeta
        var returnedNextProductBeta = om.readValue(
            restNextProductBetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductBeta)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextProductBeta.class
        );

        // Validate the NextProductBeta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextProductBetaUpdatableFieldsEquals(returnedNextProductBeta, getPersistedNextProductBeta(returnedNextProductBeta));

        insertedNextProductBeta = returnedNextProductBeta;
    }

    @Test
    @Transactional
    void createNextProductBetaWithExistingId() throws Exception {
        // Create the NextProductBeta with an existing ID
        nextProductBeta.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextProductBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductBeta)))
            .andExpect(status().isBadRequest());

        // Validate the NextProductBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextProductBeta.setName(null);

        // Create the NextProductBeta, which fails.

        restNextProductBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductBeta)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextProductBeta.setPrice(null);

        // Create the NextProductBeta, which fails.

        restNextProductBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductBeta)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStockIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextProductBeta.setStock(null);

        // Create the NextProductBeta, which fails.

        restNextProductBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductBeta)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextProductBetas() throws Exception {
        // Initialize the database
        insertedNextProductBeta = nextProductBetaRepository.saveAndFlush(nextProductBeta);

        // Get all the nextProductBetaList
        restNextProductBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextProductBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextProductBetasWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextProductBetaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextProductBetaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextProductBetaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextProductBetasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextProductBetaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextProductBetaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextProductBetaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextProductBeta() throws Exception {
        // Initialize the database
        insertedNextProductBeta = nextProductBetaRepository.saveAndFlush(nextProductBeta);

        // Get the nextProductBeta
        restNextProductBetaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextProductBeta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextProductBeta.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getNextProductBetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextProductBeta = nextProductBetaRepository.saveAndFlush(nextProductBeta);

        Long id = nextProductBeta.getId();

        defaultNextProductBetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextProductBetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextProductBetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextProductBetasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductBeta = nextProductBetaRepository.saveAndFlush(nextProductBeta);

        // Get all the nextProductBetaList where name equals to
        defaultNextProductBetaFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductBetasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextProductBeta = nextProductBetaRepository.saveAndFlush(nextProductBeta);

        // Get all the nextProductBetaList where name in
        defaultNextProductBetaFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductBetasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextProductBeta = nextProductBetaRepository.saveAndFlush(nextProductBeta);

        // Get all the nextProductBetaList where name is not null
        defaultNextProductBetaFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllNextProductBetasByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextProductBeta = nextProductBetaRepository.saveAndFlush(nextProductBeta);

        // Get all the nextProductBetaList where name contains
        defaultNextProductBetaFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductBetasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextProductBeta = nextProductBetaRepository.saveAndFlush(nextProductBeta);

        // Get all the nextProductBetaList where name does not contain
        defaultNextProductBetaFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductBetasByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductBeta = nextProductBetaRepository.saveAndFlush(nextProductBeta);

        // Get all the nextProductBetaList where price equals to
        defaultNextProductBetaFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductBetasByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextProductBeta = nextProductBetaRepository.saveAndFlush(nextProductBeta);

        // Get all the nextProductBetaList where price in
        defaultNextProductBetaFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductBetasByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextProductBeta = nextProductBetaRepository.saveAndFlush(nextProductBeta);

        // Get all the nextProductBetaList where price is not null
        defaultNextProductBetaFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllNextProductBetasByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductBeta = nextProductBetaRepository.saveAndFlush(nextProductBeta);

        // Get all the nextProductBetaList where price is greater than or equal to
        defaultNextProductBetaFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductBetasByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductBeta = nextProductBetaRepository.saveAndFlush(nextProductBeta);

        // Get all the nextProductBetaList where price is less than or equal to
        defaultNextProductBetaFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductBetasByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextProductBeta = nextProductBetaRepository.saveAndFlush(nextProductBeta);

        // Get all the nextProductBetaList where price is less than
        defaultNextProductBetaFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductBetasByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextProductBeta = nextProductBetaRepository.saveAndFlush(nextProductBeta);

        // Get all the nextProductBetaList where price is greater than
        defaultNextProductBetaFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductBetasByStockIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductBeta = nextProductBetaRepository.saveAndFlush(nextProductBeta);

        // Get all the nextProductBetaList where stock equals to
        defaultNextProductBetaFiltering("stock.equals=" + DEFAULT_STOCK, "stock.equals=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductBetasByStockIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextProductBeta = nextProductBetaRepository.saveAndFlush(nextProductBeta);

        // Get all the nextProductBetaList where stock in
        defaultNextProductBetaFiltering("stock.in=" + DEFAULT_STOCK + "," + UPDATED_STOCK, "stock.in=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductBetasByStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextProductBeta = nextProductBetaRepository.saveAndFlush(nextProductBeta);

        // Get all the nextProductBetaList where stock is not null
        defaultNextProductBetaFiltering("stock.specified=true", "stock.specified=false");
    }

    @Test
    @Transactional
    void getAllNextProductBetasByStockIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductBeta = nextProductBetaRepository.saveAndFlush(nextProductBeta);

        // Get all the nextProductBetaList where stock is greater than or equal to
        defaultNextProductBetaFiltering("stock.greaterThanOrEqual=" + DEFAULT_STOCK, "stock.greaterThanOrEqual=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductBetasByStockIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductBeta = nextProductBetaRepository.saveAndFlush(nextProductBeta);

        // Get all the nextProductBetaList where stock is less than or equal to
        defaultNextProductBetaFiltering("stock.lessThanOrEqual=" + DEFAULT_STOCK, "stock.lessThanOrEqual=" + SMALLER_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductBetasByStockIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextProductBeta = nextProductBetaRepository.saveAndFlush(nextProductBeta);

        // Get all the nextProductBetaList where stock is less than
        defaultNextProductBetaFiltering("stock.lessThan=" + UPDATED_STOCK, "stock.lessThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductBetasByStockIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextProductBeta = nextProductBetaRepository.saveAndFlush(nextProductBeta);

        // Get all the nextProductBetaList where stock is greater than
        defaultNextProductBetaFiltering("stock.greaterThan=" + SMALLER_STOCK, "stock.greaterThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductBetasByCategoryIsEqualToSomething() throws Exception {
        NextCategoryBeta category;
        if (TestUtil.findAll(em, NextCategoryBeta.class).isEmpty()) {
            nextProductBetaRepository.saveAndFlush(nextProductBeta);
            category = NextCategoryBetaResourceIT.createEntity();
        } else {
            category = TestUtil.findAll(em, NextCategoryBeta.class).get(0);
        }
        em.persist(category);
        em.flush();
        nextProductBeta.setCategory(category);
        nextProductBetaRepository.saveAndFlush(nextProductBeta);
        Long categoryId = category.getId();
        // Get all the nextProductBetaList where category equals to categoryId
        defaultNextProductBetaShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the nextProductBetaList where category equals to (categoryId + 1)
        defaultNextProductBetaShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllNextProductBetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextProductBetaRepository.saveAndFlush(nextProductBeta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextProductBeta.setTenant(tenant);
        nextProductBetaRepository.saveAndFlush(nextProductBeta);
        Long tenantId = tenant.getId();
        // Get all the nextProductBetaList where tenant equals to tenantId
        defaultNextProductBetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextProductBetaList where tenant equals to (tenantId + 1)
        defaultNextProductBetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllNextProductBetasByOrderIsEqualToSomething() throws Exception {
        NextOrderBeta order;
        if (TestUtil.findAll(em, NextOrderBeta.class).isEmpty()) {
            nextProductBetaRepository.saveAndFlush(nextProductBeta);
            order = NextOrderBetaResourceIT.createEntity();
        } else {
            order = TestUtil.findAll(em, NextOrderBeta.class).get(0);
        }
        em.persist(order);
        em.flush();
        nextProductBeta.setOrder(order);
        nextProductBetaRepository.saveAndFlush(nextProductBeta);
        Long orderId = order.getId();
        // Get all the nextProductBetaList where order equals to orderId
        defaultNextProductBetaShouldBeFound("orderId.equals=" + orderId);

        // Get all the nextProductBetaList where order equals to (orderId + 1)
        defaultNextProductBetaShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllNextProductBetasBySuppliersIsEqualToSomething() throws Exception {
        NextSupplierBeta suppliers;
        if (TestUtil.findAll(em, NextSupplierBeta.class).isEmpty()) {
            nextProductBetaRepository.saveAndFlush(nextProductBeta);
            suppliers = NextSupplierBetaResourceIT.createEntity();
        } else {
            suppliers = TestUtil.findAll(em, NextSupplierBeta.class).get(0);
        }
        em.persist(suppliers);
        em.flush();
        nextProductBeta.addSuppliers(suppliers);
        nextProductBetaRepository.saveAndFlush(nextProductBeta);
        Long suppliersId = suppliers.getId();
        // Get all the nextProductBetaList where suppliers equals to suppliersId
        defaultNextProductBetaShouldBeFound("suppliersId.equals=" + suppliersId);

        // Get all the nextProductBetaList where suppliers equals to (suppliersId + 1)
        defaultNextProductBetaShouldNotBeFound("suppliersId.equals=" + (suppliersId + 1));
    }

    private void defaultNextProductBetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextProductBetaShouldBeFound(shouldBeFound);
        defaultNextProductBetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextProductBetaShouldBeFound(String filter) throws Exception {
        restNextProductBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextProductBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restNextProductBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextProductBetaShouldNotBeFound(String filter) throws Exception {
        restNextProductBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextProductBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextProductBeta() throws Exception {
        // Get the nextProductBeta
        restNextProductBetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextProductBeta() throws Exception {
        // Initialize the database
        insertedNextProductBeta = nextProductBetaRepository.saveAndFlush(nextProductBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextProductBeta
        NextProductBeta updatedNextProductBeta = nextProductBetaRepository.findById(nextProductBeta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextProductBeta are not directly saved in db
        em.detach(updatedNextProductBeta);
        updatedNextProductBeta.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);

        restNextProductBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextProductBeta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextProductBeta))
            )
            .andExpect(status().isOk());

        // Validate the NextProductBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextProductBetaToMatchAllProperties(updatedNextProductBeta);
    }

    @Test
    @Transactional
    void putNonExistingNextProductBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductBeta.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextProductBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextProductBeta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextProductBeta))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProductBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextProductBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductBeta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextProductBeta))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProductBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextProductBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductBeta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductBetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductBeta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextProductBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextProductBetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextProductBeta = nextProductBetaRepository.saveAndFlush(nextProductBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextProductBeta using partial update
        NextProductBeta partialUpdatedNextProductBeta = new NextProductBeta();
        partialUpdatedNextProductBeta.setId(nextProductBeta.getId());

        partialUpdatedNextProductBeta.description(UPDATED_DESCRIPTION);

        restNextProductBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextProductBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextProductBeta))
            )
            .andExpect(status().isOk());

        // Validate the NextProductBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextProductBetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextProductBeta, nextProductBeta),
            getPersistedNextProductBeta(nextProductBeta)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextProductBetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextProductBeta = nextProductBetaRepository.saveAndFlush(nextProductBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextProductBeta using partial update
        NextProductBeta partialUpdatedNextProductBeta = new NextProductBeta();
        partialUpdatedNextProductBeta.setId(nextProductBeta.getId());

        partialUpdatedNextProductBeta.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);

        restNextProductBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextProductBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextProductBeta))
            )
            .andExpect(status().isOk());

        // Validate the NextProductBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextProductBetaUpdatableFieldsEquals(
            partialUpdatedNextProductBeta,
            getPersistedNextProductBeta(partialUpdatedNextProductBeta)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextProductBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductBeta.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextProductBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextProductBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextProductBeta))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProductBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextProductBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductBeta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextProductBeta))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProductBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextProductBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductBeta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductBetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextProductBeta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextProductBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextProductBeta() throws Exception {
        // Initialize the database
        insertedNextProductBeta = nextProductBetaRepository.saveAndFlush(nextProductBeta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextProductBeta
        restNextProductBetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextProductBeta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextProductBetaRepository.count();
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

    protected NextProductBeta getPersistedNextProductBeta(NextProductBeta nextProductBeta) {
        return nextProductBetaRepository.findById(nextProductBeta.getId()).orElseThrow();
    }

    protected void assertPersistedNextProductBetaToMatchAllProperties(NextProductBeta expectedNextProductBeta) {
        assertNextProductBetaAllPropertiesEquals(expectedNextProductBeta, getPersistedNextProductBeta(expectedNextProductBeta));
    }

    protected void assertPersistedNextProductBetaToMatchUpdatableProperties(NextProductBeta expectedNextProductBeta) {
        assertNextProductBetaAllUpdatablePropertiesEquals(expectedNextProductBeta, getPersistedNextProductBeta(expectedNextProductBeta));
    }
}
