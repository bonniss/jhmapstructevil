package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextProductViViAsserts.*;
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
import xyz.jhmapstruct.domain.NextCategoryViVi;
import xyz.jhmapstruct.domain.NextOrderViVi;
import xyz.jhmapstruct.domain.NextProductViVi;
import xyz.jhmapstruct.domain.NextSupplierViVi;
import xyz.jhmapstruct.repository.NextProductViViRepository;
import xyz.jhmapstruct.service.NextProductViViService;
import xyz.jhmapstruct.service.dto.NextProductViViDTO;
import xyz.jhmapstruct.service.mapper.NextProductViViMapper;

/**
 * Integration tests for the {@link NextProductViViResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextProductViViResourceIT {

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

    private static final String ENTITY_API_URL = "/api/next-product-vi-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextProductViViRepository nextProductViViRepository;

    @Mock
    private NextProductViViRepository nextProductViViRepositoryMock;

    @Autowired
    private NextProductViViMapper nextProductViViMapper;

    @Mock
    private NextProductViViService nextProductViViServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextProductViViMockMvc;

    private NextProductViVi nextProductViVi;

    private NextProductViVi insertedNextProductViVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextProductViVi createEntity() {
        return new NextProductViVi().name(DEFAULT_NAME).price(DEFAULT_PRICE).stock(DEFAULT_STOCK).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextProductViVi createUpdatedEntity() {
        return new NextProductViVi().name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        nextProductViVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextProductViVi != null) {
            nextProductViViRepository.delete(insertedNextProductViVi);
            insertedNextProductViVi = null;
        }
    }

    @Test
    @Transactional
    void createNextProductViVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextProductViVi
        NextProductViViDTO nextProductViViDTO = nextProductViViMapper.toDto(nextProductViVi);
        var returnedNextProductViViDTO = om.readValue(
            restNextProductViViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductViViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextProductViViDTO.class
        );

        // Validate the NextProductViVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextProductViVi = nextProductViViMapper.toEntity(returnedNextProductViViDTO);
        assertNextProductViViUpdatableFieldsEquals(returnedNextProductViVi, getPersistedNextProductViVi(returnedNextProductViVi));

        insertedNextProductViVi = returnedNextProductViVi;
    }

    @Test
    @Transactional
    void createNextProductViViWithExistingId() throws Exception {
        // Create the NextProductViVi with an existing ID
        nextProductViVi.setId(1L);
        NextProductViViDTO nextProductViViDTO = nextProductViViMapper.toDto(nextProductViVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextProductViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductViViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextProductViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextProductViVi.setName(null);

        // Create the NextProductViVi, which fails.
        NextProductViViDTO nextProductViViDTO = nextProductViViMapper.toDto(nextProductViVi);

        restNextProductViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductViViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextProductViVi.setPrice(null);

        // Create the NextProductViVi, which fails.
        NextProductViViDTO nextProductViViDTO = nextProductViViMapper.toDto(nextProductViVi);

        restNextProductViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductViViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStockIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextProductViVi.setStock(null);

        // Create the NextProductViVi, which fails.
        NextProductViViDTO nextProductViViDTO = nextProductViViMapper.toDto(nextProductViVi);

        restNextProductViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductViViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextProductViVis() throws Exception {
        // Initialize the database
        insertedNextProductViVi = nextProductViViRepository.saveAndFlush(nextProductViVi);

        // Get all the nextProductViViList
        restNextProductViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextProductViVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextProductViVisWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextProductViViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextProductViViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextProductViViServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextProductViVisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextProductViViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextProductViViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextProductViViRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextProductViVi() throws Exception {
        // Initialize the database
        insertedNextProductViVi = nextProductViViRepository.saveAndFlush(nextProductViVi);

        // Get the nextProductViVi
        restNextProductViViMockMvc
            .perform(get(ENTITY_API_URL_ID, nextProductViVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextProductViVi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getNextProductViVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextProductViVi = nextProductViViRepository.saveAndFlush(nextProductViVi);

        Long id = nextProductViVi.getId();

        defaultNextProductViViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextProductViViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextProductViViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextProductViVisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductViVi = nextProductViViRepository.saveAndFlush(nextProductViVi);

        // Get all the nextProductViViList where name equals to
        defaultNextProductViViFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductViVisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextProductViVi = nextProductViViRepository.saveAndFlush(nextProductViVi);

        // Get all the nextProductViViList where name in
        defaultNextProductViViFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductViVisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextProductViVi = nextProductViViRepository.saveAndFlush(nextProductViVi);

        // Get all the nextProductViViList where name is not null
        defaultNextProductViViFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllNextProductViVisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextProductViVi = nextProductViViRepository.saveAndFlush(nextProductViVi);

        // Get all the nextProductViViList where name contains
        defaultNextProductViViFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductViVisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextProductViVi = nextProductViViRepository.saveAndFlush(nextProductViVi);

        // Get all the nextProductViViList where name does not contain
        defaultNextProductViViFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductViVisByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductViVi = nextProductViViRepository.saveAndFlush(nextProductViVi);

        // Get all the nextProductViViList where price equals to
        defaultNextProductViViFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductViVisByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextProductViVi = nextProductViViRepository.saveAndFlush(nextProductViVi);

        // Get all the nextProductViViList where price in
        defaultNextProductViViFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductViVisByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextProductViVi = nextProductViViRepository.saveAndFlush(nextProductViVi);

        // Get all the nextProductViViList where price is not null
        defaultNextProductViViFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllNextProductViVisByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductViVi = nextProductViViRepository.saveAndFlush(nextProductViVi);

        // Get all the nextProductViViList where price is greater than or equal to
        defaultNextProductViViFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductViVisByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductViVi = nextProductViViRepository.saveAndFlush(nextProductViVi);

        // Get all the nextProductViViList where price is less than or equal to
        defaultNextProductViViFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductViVisByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextProductViVi = nextProductViViRepository.saveAndFlush(nextProductViVi);

        // Get all the nextProductViViList where price is less than
        defaultNextProductViViFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductViVisByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextProductViVi = nextProductViViRepository.saveAndFlush(nextProductViVi);

        // Get all the nextProductViViList where price is greater than
        defaultNextProductViViFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductViVisByStockIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductViVi = nextProductViViRepository.saveAndFlush(nextProductViVi);

        // Get all the nextProductViViList where stock equals to
        defaultNextProductViViFiltering("stock.equals=" + DEFAULT_STOCK, "stock.equals=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductViVisByStockIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextProductViVi = nextProductViViRepository.saveAndFlush(nextProductViVi);

        // Get all the nextProductViViList where stock in
        defaultNextProductViViFiltering("stock.in=" + DEFAULT_STOCK + "," + UPDATED_STOCK, "stock.in=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductViVisByStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextProductViVi = nextProductViViRepository.saveAndFlush(nextProductViVi);

        // Get all the nextProductViViList where stock is not null
        defaultNextProductViViFiltering("stock.specified=true", "stock.specified=false");
    }

    @Test
    @Transactional
    void getAllNextProductViVisByStockIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductViVi = nextProductViViRepository.saveAndFlush(nextProductViVi);

        // Get all the nextProductViViList where stock is greater than or equal to
        defaultNextProductViViFiltering("stock.greaterThanOrEqual=" + DEFAULT_STOCK, "stock.greaterThanOrEqual=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductViVisByStockIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductViVi = nextProductViViRepository.saveAndFlush(nextProductViVi);

        // Get all the nextProductViViList where stock is less than or equal to
        defaultNextProductViViFiltering("stock.lessThanOrEqual=" + DEFAULT_STOCK, "stock.lessThanOrEqual=" + SMALLER_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductViVisByStockIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextProductViVi = nextProductViViRepository.saveAndFlush(nextProductViVi);

        // Get all the nextProductViViList where stock is less than
        defaultNextProductViViFiltering("stock.lessThan=" + UPDATED_STOCK, "stock.lessThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductViVisByStockIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextProductViVi = nextProductViViRepository.saveAndFlush(nextProductViVi);

        // Get all the nextProductViViList where stock is greater than
        defaultNextProductViViFiltering("stock.greaterThan=" + SMALLER_STOCK, "stock.greaterThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductViVisByCategoryIsEqualToSomething() throws Exception {
        NextCategoryViVi category;
        if (TestUtil.findAll(em, NextCategoryViVi.class).isEmpty()) {
            nextProductViViRepository.saveAndFlush(nextProductViVi);
            category = NextCategoryViViResourceIT.createEntity();
        } else {
            category = TestUtil.findAll(em, NextCategoryViVi.class).get(0);
        }
        em.persist(category);
        em.flush();
        nextProductViVi.setCategory(category);
        nextProductViViRepository.saveAndFlush(nextProductViVi);
        Long categoryId = category.getId();
        // Get all the nextProductViViList where category equals to categoryId
        defaultNextProductViViShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the nextProductViViList where category equals to (categoryId + 1)
        defaultNextProductViViShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllNextProductViVisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextProductViViRepository.saveAndFlush(nextProductViVi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextProductViVi.setTenant(tenant);
        nextProductViViRepository.saveAndFlush(nextProductViVi);
        Long tenantId = tenant.getId();
        // Get all the nextProductViViList where tenant equals to tenantId
        defaultNextProductViViShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextProductViViList where tenant equals to (tenantId + 1)
        defaultNextProductViViShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllNextProductViVisByOrderIsEqualToSomething() throws Exception {
        NextOrderViVi order;
        if (TestUtil.findAll(em, NextOrderViVi.class).isEmpty()) {
            nextProductViViRepository.saveAndFlush(nextProductViVi);
            order = NextOrderViViResourceIT.createEntity();
        } else {
            order = TestUtil.findAll(em, NextOrderViVi.class).get(0);
        }
        em.persist(order);
        em.flush();
        nextProductViVi.setOrder(order);
        nextProductViViRepository.saveAndFlush(nextProductViVi);
        Long orderId = order.getId();
        // Get all the nextProductViViList where order equals to orderId
        defaultNextProductViViShouldBeFound("orderId.equals=" + orderId);

        // Get all the nextProductViViList where order equals to (orderId + 1)
        defaultNextProductViViShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllNextProductViVisBySuppliersIsEqualToSomething() throws Exception {
        NextSupplierViVi suppliers;
        if (TestUtil.findAll(em, NextSupplierViVi.class).isEmpty()) {
            nextProductViViRepository.saveAndFlush(nextProductViVi);
            suppliers = NextSupplierViViResourceIT.createEntity();
        } else {
            suppliers = TestUtil.findAll(em, NextSupplierViVi.class).get(0);
        }
        em.persist(suppliers);
        em.flush();
        nextProductViVi.addSuppliers(suppliers);
        nextProductViViRepository.saveAndFlush(nextProductViVi);
        Long suppliersId = suppliers.getId();
        // Get all the nextProductViViList where suppliers equals to suppliersId
        defaultNextProductViViShouldBeFound("suppliersId.equals=" + suppliersId);

        // Get all the nextProductViViList where suppliers equals to (suppliersId + 1)
        defaultNextProductViViShouldNotBeFound("suppliersId.equals=" + (suppliersId + 1));
    }

    private void defaultNextProductViViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextProductViViShouldBeFound(shouldBeFound);
        defaultNextProductViViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextProductViViShouldBeFound(String filter) throws Exception {
        restNextProductViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextProductViVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restNextProductViViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextProductViViShouldNotBeFound(String filter) throws Exception {
        restNextProductViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextProductViViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextProductViVi() throws Exception {
        // Get the nextProductViVi
        restNextProductViViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextProductViVi() throws Exception {
        // Initialize the database
        insertedNextProductViVi = nextProductViViRepository.saveAndFlush(nextProductViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextProductViVi
        NextProductViVi updatedNextProductViVi = nextProductViViRepository.findById(nextProductViVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextProductViVi are not directly saved in db
        em.detach(updatedNextProductViVi);
        updatedNextProductViVi.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);
        NextProductViViDTO nextProductViViDTO = nextProductViViMapper.toDto(updatedNextProductViVi);

        restNextProductViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextProductViViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextProductViViDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextProductViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextProductViViToMatchAllProperties(updatedNextProductViVi);
    }

    @Test
    @Transactional
    void putNonExistingNextProductViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductViVi.setId(longCount.incrementAndGet());

        // Create the NextProductViVi
        NextProductViViDTO nextProductViViDTO = nextProductViViMapper.toDto(nextProductViVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextProductViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextProductViViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextProductViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProductViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextProductViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductViVi.setId(longCount.incrementAndGet());

        // Create the NextProductViVi
        NextProductViViDTO nextProductViViDTO = nextProductViViMapper.toDto(nextProductViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextProductViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProductViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextProductViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductViVi.setId(longCount.incrementAndGet());

        // Create the NextProductViVi
        NextProductViViDTO nextProductViViDTO = nextProductViViMapper.toDto(nextProductViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductViViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductViViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextProductViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextProductViViWithPatch() throws Exception {
        // Initialize the database
        insertedNextProductViVi = nextProductViViRepository.saveAndFlush(nextProductViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextProductViVi using partial update
        NextProductViVi partialUpdatedNextProductViVi = new NextProductViVi();
        partialUpdatedNextProductViVi.setId(nextProductViVi.getId());

        partialUpdatedNextProductViVi.name(UPDATED_NAME).stock(UPDATED_STOCK);

        restNextProductViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextProductViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextProductViVi))
            )
            .andExpect(status().isOk());

        // Validate the NextProductViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextProductViViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextProductViVi, nextProductViVi),
            getPersistedNextProductViVi(nextProductViVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextProductViViWithPatch() throws Exception {
        // Initialize the database
        insertedNextProductViVi = nextProductViViRepository.saveAndFlush(nextProductViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextProductViVi using partial update
        NextProductViVi partialUpdatedNextProductViVi = new NextProductViVi();
        partialUpdatedNextProductViVi.setId(nextProductViVi.getId());

        partialUpdatedNextProductViVi.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);

        restNextProductViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextProductViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextProductViVi))
            )
            .andExpect(status().isOk());

        // Validate the NextProductViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextProductViViUpdatableFieldsEquals(
            partialUpdatedNextProductViVi,
            getPersistedNextProductViVi(partialUpdatedNextProductViVi)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextProductViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductViVi.setId(longCount.incrementAndGet());

        // Create the NextProductViVi
        NextProductViViDTO nextProductViViDTO = nextProductViViMapper.toDto(nextProductViVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextProductViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextProductViViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextProductViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProductViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextProductViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductViVi.setId(longCount.incrementAndGet());

        // Create the NextProductViVi
        NextProductViViDTO nextProductViViDTO = nextProductViViMapper.toDto(nextProductViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextProductViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProductViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextProductViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductViVi.setId(longCount.incrementAndGet());

        // Create the NextProductViVi
        NextProductViViDTO nextProductViViDTO = nextProductViViMapper.toDto(nextProductViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductViViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextProductViViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextProductViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextProductViVi() throws Exception {
        // Initialize the database
        insertedNextProductViVi = nextProductViViRepository.saveAndFlush(nextProductViVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextProductViVi
        restNextProductViViMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextProductViVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextProductViViRepository.count();
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

    protected NextProductViVi getPersistedNextProductViVi(NextProductViVi nextProductViVi) {
        return nextProductViViRepository.findById(nextProductViVi.getId()).orElseThrow();
    }

    protected void assertPersistedNextProductViViToMatchAllProperties(NextProductViVi expectedNextProductViVi) {
        assertNextProductViViAllPropertiesEquals(expectedNextProductViVi, getPersistedNextProductViVi(expectedNextProductViVi));
    }

    protected void assertPersistedNextProductViViToMatchUpdatableProperties(NextProductViVi expectedNextProductViVi) {
        assertNextProductViViAllUpdatablePropertiesEquals(expectedNextProductViVi, getPersistedNextProductViVi(expectedNextProductViVi));
    }
}
