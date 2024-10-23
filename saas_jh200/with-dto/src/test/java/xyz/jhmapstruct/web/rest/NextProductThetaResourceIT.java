package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextProductThetaAsserts.*;
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
import xyz.jhmapstruct.domain.NextCategoryTheta;
import xyz.jhmapstruct.domain.NextOrderTheta;
import xyz.jhmapstruct.domain.NextProductTheta;
import xyz.jhmapstruct.domain.NextSupplierTheta;
import xyz.jhmapstruct.repository.NextProductThetaRepository;
import xyz.jhmapstruct.service.NextProductThetaService;
import xyz.jhmapstruct.service.dto.NextProductThetaDTO;
import xyz.jhmapstruct.service.mapper.NextProductThetaMapper;

/**
 * Integration tests for the {@link NextProductThetaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextProductThetaResourceIT {

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

    private static final String ENTITY_API_URL = "/api/next-product-thetas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextProductThetaRepository nextProductThetaRepository;

    @Mock
    private NextProductThetaRepository nextProductThetaRepositoryMock;

    @Autowired
    private NextProductThetaMapper nextProductThetaMapper;

    @Mock
    private NextProductThetaService nextProductThetaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextProductThetaMockMvc;

    private NextProductTheta nextProductTheta;

    private NextProductTheta insertedNextProductTheta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextProductTheta createEntity() {
        return new NextProductTheta().name(DEFAULT_NAME).price(DEFAULT_PRICE).stock(DEFAULT_STOCK).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextProductTheta createUpdatedEntity() {
        return new NextProductTheta().name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        nextProductTheta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextProductTheta != null) {
            nextProductThetaRepository.delete(insertedNextProductTheta);
            insertedNextProductTheta = null;
        }
    }

    @Test
    @Transactional
    void createNextProductTheta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextProductTheta
        NextProductThetaDTO nextProductThetaDTO = nextProductThetaMapper.toDto(nextProductTheta);
        var returnedNextProductThetaDTO = om.readValue(
            restNextProductThetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductThetaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextProductThetaDTO.class
        );

        // Validate the NextProductTheta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextProductTheta = nextProductThetaMapper.toEntity(returnedNextProductThetaDTO);
        assertNextProductThetaUpdatableFieldsEquals(returnedNextProductTheta, getPersistedNextProductTheta(returnedNextProductTheta));

        insertedNextProductTheta = returnedNextProductTheta;
    }

    @Test
    @Transactional
    void createNextProductThetaWithExistingId() throws Exception {
        // Create the NextProductTheta with an existing ID
        nextProductTheta.setId(1L);
        NextProductThetaDTO nextProductThetaDTO = nextProductThetaMapper.toDto(nextProductTheta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextProductThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductThetaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextProductTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextProductTheta.setName(null);

        // Create the NextProductTheta, which fails.
        NextProductThetaDTO nextProductThetaDTO = nextProductThetaMapper.toDto(nextProductTheta);

        restNextProductThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductThetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextProductTheta.setPrice(null);

        // Create the NextProductTheta, which fails.
        NextProductThetaDTO nextProductThetaDTO = nextProductThetaMapper.toDto(nextProductTheta);

        restNextProductThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductThetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStockIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextProductTheta.setStock(null);

        // Create the NextProductTheta, which fails.
        NextProductThetaDTO nextProductThetaDTO = nextProductThetaMapper.toDto(nextProductTheta);

        restNextProductThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductThetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextProductThetas() throws Exception {
        // Initialize the database
        insertedNextProductTheta = nextProductThetaRepository.saveAndFlush(nextProductTheta);

        // Get all the nextProductThetaList
        restNextProductThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextProductTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextProductThetasWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextProductThetaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextProductThetaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextProductThetaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextProductThetasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextProductThetaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextProductThetaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextProductThetaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextProductTheta() throws Exception {
        // Initialize the database
        insertedNextProductTheta = nextProductThetaRepository.saveAndFlush(nextProductTheta);

        // Get the nextProductTheta
        restNextProductThetaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextProductTheta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextProductTheta.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getNextProductThetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextProductTheta = nextProductThetaRepository.saveAndFlush(nextProductTheta);

        Long id = nextProductTheta.getId();

        defaultNextProductThetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextProductThetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextProductThetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextProductThetasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductTheta = nextProductThetaRepository.saveAndFlush(nextProductTheta);

        // Get all the nextProductThetaList where name equals to
        defaultNextProductThetaFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductThetasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextProductTheta = nextProductThetaRepository.saveAndFlush(nextProductTheta);

        // Get all the nextProductThetaList where name in
        defaultNextProductThetaFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductThetasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextProductTheta = nextProductThetaRepository.saveAndFlush(nextProductTheta);

        // Get all the nextProductThetaList where name is not null
        defaultNextProductThetaFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllNextProductThetasByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextProductTheta = nextProductThetaRepository.saveAndFlush(nextProductTheta);

        // Get all the nextProductThetaList where name contains
        defaultNextProductThetaFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductThetasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextProductTheta = nextProductThetaRepository.saveAndFlush(nextProductTheta);

        // Get all the nextProductThetaList where name does not contain
        defaultNextProductThetaFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductThetasByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductTheta = nextProductThetaRepository.saveAndFlush(nextProductTheta);

        // Get all the nextProductThetaList where price equals to
        defaultNextProductThetaFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductThetasByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextProductTheta = nextProductThetaRepository.saveAndFlush(nextProductTheta);

        // Get all the nextProductThetaList where price in
        defaultNextProductThetaFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductThetasByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextProductTheta = nextProductThetaRepository.saveAndFlush(nextProductTheta);

        // Get all the nextProductThetaList where price is not null
        defaultNextProductThetaFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllNextProductThetasByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductTheta = nextProductThetaRepository.saveAndFlush(nextProductTheta);

        // Get all the nextProductThetaList where price is greater than or equal to
        defaultNextProductThetaFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductThetasByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductTheta = nextProductThetaRepository.saveAndFlush(nextProductTheta);

        // Get all the nextProductThetaList where price is less than or equal to
        defaultNextProductThetaFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductThetasByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextProductTheta = nextProductThetaRepository.saveAndFlush(nextProductTheta);

        // Get all the nextProductThetaList where price is less than
        defaultNextProductThetaFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductThetasByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextProductTheta = nextProductThetaRepository.saveAndFlush(nextProductTheta);

        // Get all the nextProductThetaList where price is greater than
        defaultNextProductThetaFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductThetasByStockIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductTheta = nextProductThetaRepository.saveAndFlush(nextProductTheta);

        // Get all the nextProductThetaList where stock equals to
        defaultNextProductThetaFiltering("stock.equals=" + DEFAULT_STOCK, "stock.equals=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductThetasByStockIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextProductTheta = nextProductThetaRepository.saveAndFlush(nextProductTheta);

        // Get all the nextProductThetaList where stock in
        defaultNextProductThetaFiltering("stock.in=" + DEFAULT_STOCK + "," + UPDATED_STOCK, "stock.in=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductThetasByStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextProductTheta = nextProductThetaRepository.saveAndFlush(nextProductTheta);

        // Get all the nextProductThetaList where stock is not null
        defaultNextProductThetaFiltering("stock.specified=true", "stock.specified=false");
    }

    @Test
    @Transactional
    void getAllNextProductThetasByStockIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductTheta = nextProductThetaRepository.saveAndFlush(nextProductTheta);

        // Get all the nextProductThetaList where stock is greater than or equal to
        defaultNextProductThetaFiltering("stock.greaterThanOrEqual=" + DEFAULT_STOCK, "stock.greaterThanOrEqual=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductThetasByStockIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductTheta = nextProductThetaRepository.saveAndFlush(nextProductTheta);

        // Get all the nextProductThetaList where stock is less than or equal to
        defaultNextProductThetaFiltering("stock.lessThanOrEqual=" + DEFAULT_STOCK, "stock.lessThanOrEqual=" + SMALLER_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductThetasByStockIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextProductTheta = nextProductThetaRepository.saveAndFlush(nextProductTheta);

        // Get all the nextProductThetaList where stock is less than
        defaultNextProductThetaFiltering("stock.lessThan=" + UPDATED_STOCK, "stock.lessThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductThetasByStockIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextProductTheta = nextProductThetaRepository.saveAndFlush(nextProductTheta);

        // Get all the nextProductThetaList where stock is greater than
        defaultNextProductThetaFiltering("stock.greaterThan=" + SMALLER_STOCK, "stock.greaterThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductThetasByCategoryIsEqualToSomething() throws Exception {
        NextCategoryTheta category;
        if (TestUtil.findAll(em, NextCategoryTheta.class).isEmpty()) {
            nextProductThetaRepository.saveAndFlush(nextProductTheta);
            category = NextCategoryThetaResourceIT.createEntity();
        } else {
            category = TestUtil.findAll(em, NextCategoryTheta.class).get(0);
        }
        em.persist(category);
        em.flush();
        nextProductTheta.setCategory(category);
        nextProductThetaRepository.saveAndFlush(nextProductTheta);
        Long categoryId = category.getId();
        // Get all the nextProductThetaList where category equals to categoryId
        defaultNextProductThetaShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the nextProductThetaList where category equals to (categoryId + 1)
        defaultNextProductThetaShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllNextProductThetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextProductThetaRepository.saveAndFlush(nextProductTheta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextProductTheta.setTenant(tenant);
        nextProductThetaRepository.saveAndFlush(nextProductTheta);
        Long tenantId = tenant.getId();
        // Get all the nextProductThetaList where tenant equals to tenantId
        defaultNextProductThetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextProductThetaList where tenant equals to (tenantId + 1)
        defaultNextProductThetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllNextProductThetasByOrderIsEqualToSomething() throws Exception {
        NextOrderTheta order;
        if (TestUtil.findAll(em, NextOrderTheta.class).isEmpty()) {
            nextProductThetaRepository.saveAndFlush(nextProductTheta);
            order = NextOrderThetaResourceIT.createEntity();
        } else {
            order = TestUtil.findAll(em, NextOrderTheta.class).get(0);
        }
        em.persist(order);
        em.flush();
        nextProductTheta.setOrder(order);
        nextProductThetaRepository.saveAndFlush(nextProductTheta);
        Long orderId = order.getId();
        // Get all the nextProductThetaList where order equals to orderId
        defaultNextProductThetaShouldBeFound("orderId.equals=" + orderId);

        // Get all the nextProductThetaList where order equals to (orderId + 1)
        defaultNextProductThetaShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllNextProductThetasBySuppliersIsEqualToSomething() throws Exception {
        NextSupplierTheta suppliers;
        if (TestUtil.findAll(em, NextSupplierTheta.class).isEmpty()) {
            nextProductThetaRepository.saveAndFlush(nextProductTheta);
            suppliers = NextSupplierThetaResourceIT.createEntity();
        } else {
            suppliers = TestUtil.findAll(em, NextSupplierTheta.class).get(0);
        }
        em.persist(suppliers);
        em.flush();
        nextProductTheta.addSuppliers(suppliers);
        nextProductThetaRepository.saveAndFlush(nextProductTheta);
        Long suppliersId = suppliers.getId();
        // Get all the nextProductThetaList where suppliers equals to suppliersId
        defaultNextProductThetaShouldBeFound("suppliersId.equals=" + suppliersId);

        // Get all the nextProductThetaList where suppliers equals to (suppliersId + 1)
        defaultNextProductThetaShouldNotBeFound("suppliersId.equals=" + (suppliersId + 1));
    }

    private void defaultNextProductThetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextProductThetaShouldBeFound(shouldBeFound);
        defaultNextProductThetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextProductThetaShouldBeFound(String filter) throws Exception {
        restNextProductThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextProductTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restNextProductThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextProductThetaShouldNotBeFound(String filter) throws Exception {
        restNextProductThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextProductThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextProductTheta() throws Exception {
        // Get the nextProductTheta
        restNextProductThetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextProductTheta() throws Exception {
        // Initialize the database
        insertedNextProductTheta = nextProductThetaRepository.saveAndFlush(nextProductTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextProductTheta
        NextProductTheta updatedNextProductTheta = nextProductThetaRepository.findById(nextProductTheta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextProductTheta are not directly saved in db
        em.detach(updatedNextProductTheta);
        updatedNextProductTheta.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);
        NextProductThetaDTO nextProductThetaDTO = nextProductThetaMapper.toDto(updatedNextProductTheta);

        restNextProductThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextProductThetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextProductThetaDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextProductTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextProductThetaToMatchAllProperties(updatedNextProductTheta);
    }

    @Test
    @Transactional
    void putNonExistingNextProductTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductTheta.setId(longCount.incrementAndGet());

        // Create the NextProductTheta
        NextProductThetaDTO nextProductThetaDTO = nextProductThetaMapper.toDto(nextProductTheta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextProductThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextProductThetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextProductThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProductTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextProductTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductTheta.setId(longCount.incrementAndGet());

        // Create the NextProductTheta
        NextProductThetaDTO nextProductThetaDTO = nextProductThetaMapper.toDto(nextProductTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextProductThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProductTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextProductTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductTheta.setId(longCount.incrementAndGet());

        // Create the NextProductTheta
        NextProductThetaDTO nextProductThetaDTO = nextProductThetaMapper.toDto(nextProductTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductThetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductThetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextProductTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextProductThetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextProductTheta = nextProductThetaRepository.saveAndFlush(nextProductTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextProductTheta using partial update
        NextProductTheta partialUpdatedNextProductTheta = new NextProductTheta();
        partialUpdatedNextProductTheta.setId(nextProductTheta.getId());

        partialUpdatedNextProductTheta.price(UPDATED_PRICE).description(UPDATED_DESCRIPTION);

        restNextProductThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextProductTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextProductTheta))
            )
            .andExpect(status().isOk());

        // Validate the NextProductTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextProductThetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextProductTheta, nextProductTheta),
            getPersistedNextProductTheta(nextProductTheta)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextProductThetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextProductTheta = nextProductThetaRepository.saveAndFlush(nextProductTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextProductTheta using partial update
        NextProductTheta partialUpdatedNextProductTheta = new NextProductTheta();
        partialUpdatedNextProductTheta.setId(nextProductTheta.getId());

        partialUpdatedNextProductTheta.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);

        restNextProductThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextProductTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextProductTheta))
            )
            .andExpect(status().isOk());

        // Validate the NextProductTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextProductThetaUpdatableFieldsEquals(
            partialUpdatedNextProductTheta,
            getPersistedNextProductTheta(partialUpdatedNextProductTheta)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextProductTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductTheta.setId(longCount.incrementAndGet());

        // Create the NextProductTheta
        NextProductThetaDTO nextProductThetaDTO = nextProductThetaMapper.toDto(nextProductTheta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextProductThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextProductThetaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextProductThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProductTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextProductTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductTheta.setId(longCount.incrementAndGet());

        // Create the NextProductTheta
        NextProductThetaDTO nextProductThetaDTO = nextProductThetaMapper.toDto(nextProductTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextProductThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProductTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextProductTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductTheta.setId(longCount.incrementAndGet());

        // Create the NextProductTheta
        NextProductThetaDTO nextProductThetaDTO = nextProductThetaMapper.toDto(nextProductTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductThetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextProductThetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextProductTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextProductTheta() throws Exception {
        // Initialize the database
        insertedNextProductTheta = nextProductThetaRepository.saveAndFlush(nextProductTheta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextProductTheta
        restNextProductThetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextProductTheta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextProductThetaRepository.count();
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

    protected NextProductTheta getPersistedNextProductTheta(NextProductTheta nextProductTheta) {
        return nextProductThetaRepository.findById(nextProductTheta.getId()).orElseThrow();
    }

    protected void assertPersistedNextProductThetaToMatchAllProperties(NextProductTheta expectedNextProductTheta) {
        assertNextProductThetaAllPropertiesEquals(expectedNextProductTheta, getPersistedNextProductTheta(expectedNextProductTheta));
    }

    protected void assertPersistedNextProductThetaToMatchUpdatableProperties(NextProductTheta expectedNextProductTheta) {
        assertNextProductThetaAllUpdatablePropertiesEquals(
            expectedNextProductTheta,
            getPersistedNextProductTheta(expectedNextProductTheta)
        );
    }
}
