package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextProductSigmaAsserts.*;
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
import xyz.jhmapstruct.domain.NextCategorySigma;
import xyz.jhmapstruct.domain.NextOrderSigma;
import xyz.jhmapstruct.domain.NextProductSigma;
import xyz.jhmapstruct.domain.NextSupplierSigma;
import xyz.jhmapstruct.repository.NextProductSigmaRepository;
import xyz.jhmapstruct.service.NextProductSigmaService;
import xyz.jhmapstruct.service.dto.NextProductSigmaDTO;
import xyz.jhmapstruct.service.mapper.NextProductSigmaMapper;

/**
 * Integration tests for the {@link NextProductSigmaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextProductSigmaResourceIT {

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

    private static final String ENTITY_API_URL = "/api/next-product-sigmas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextProductSigmaRepository nextProductSigmaRepository;

    @Mock
    private NextProductSigmaRepository nextProductSigmaRepositoryMock;

    @Autowired
    private NextProductSigmaMapper nextProductSigmaMapper;

    @Mock
    private NextProductSigmaService nextProductSigmaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextProductSigmaMockMvc;

    private NextProductSigma nextProductSigma;

    private NextProductSigma insertedNextProductSigma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextProductSigma createEntity() {
        return new NextProductSigma().name(DEFAULT_NAME).price(DEFAULT_PRICE).stock(DEFAULT_STOCK).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextProductSigma createUpdatedEntity() {
        return new NextProductSigma().name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        nextProductSigma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextProductSigma != null) {
            nextProductSigmaRepository.delete(insertedNextProductSigma);
            insertedNextProductSigma = null;
        }
    }

    @Test
    @Transactional
    void createNextProductSigma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextProductSigma
        NextProductSigmaDTO nextProductSigmaDTO = nextProductSigmaMapper.toDto(nextProductSigma);
        var returnedNextProductSigmaDTO = om.readValue(
            restNextProductSigmaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductSigmaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextProductSigmaDTO.class
        );

        // Validate the NextProductSigma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextProductSigma = nextProductSigmaMapper.toEntity(returnedNextProductSigmaDTO);
        assertNextProductSigmaUpdatableFieldsEquals(returnedNextProductSigma, getPersistedNextProductSigma(returnedNextProductSigma));

        insertedNextProductSigma = returnedNextProductSigma;
    }

    @Test
    @Transactional
    void createNextProductSigmaWithExistingId() throws Exception {
        // Create the NextProductSigma with an existing ID
        nextProductSigma.setId(1L);
        NextProductSigmaDTO nextProductSigmaDTO = nextProductSigmaMapper.toDto(nextProductSigma);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextProductSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductSigmaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextProductSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextProductSigma.setName(null);

        // Create the NextProductSigma, which fails.
        NextProductSigmaDTO nextProductSigmaDTO = nextProductSigmaMapper.toDto(nextProductSigma);

        restNextProductSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductSigmaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextProductSigma.setPrice(null);

        // Create the NextProductSigma, which fails.
        NextProductSigmaDTO nextProductSigmaDTO = nextProductSigmaMapper.toDto(nextProductSigma);

        restNextProductSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductSigmaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStockIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextProductSigma.setStock(null);

        // Create the NextProductSigma, which fails.
        NextProductSigmaDTO nextProductSigmaDTO = nextProductSigmaMapper.toDto(nextProductSigma);

        restNextProductSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductSigmaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextProductSigmas() throws Exception {
        // Initialize the database
        insertedNextProductSigma = nextProductSigmaRepository.saveAndFlush(nextProductSigma);

        // Get all the nextProductSigmaList
        restNextProductSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextProductSigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextProductSigmasWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextProductSigmaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextProductSigmaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextProductSigmaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextProductSigmasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextProductSigmaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextProductSigmaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextProductSigmaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextProductSigma() throws Exception {
        // Initialize the database
        insertedNextProductSigma = nextProductSigmaRepository.saveAndFlush(nextProductSigma);

        // Get the nextProductSigma
        restNextProductSigmaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextProductSigma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextProductSigma.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getNextProductSigmasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextProductSigma = nextProductSigmaRepository.saveAndFlush(nextProductSigma);

        Long id = nextProductSigma.getId();

        defaultNextProductSigmaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextProductSigmaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextProductSigmaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextProductSigmasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductSigma = nextProductSigmaRepository.saveAndFlush(nextProductSigma);

        // Get all the nextProductSigmaList where name equals to
        defaultNextProductSigmaFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductSigmasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextProductSigma = nextProductSigmaRepository.saveAndFlush(nextProductSigma);

        // Get all the nextProductSigmaList where name in
        defaultNextProductSigmaFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductSigmasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextProductSigma = nextProductSigmaRepository.saveAndFlush(nextProductSigma);

        // Get all the nextProductSigmaList where name is not null
        defaultNextProductSigmaFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllNextProductSigmasByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextProductSigma = nextProductSigmaRepository.saveAndFlush(nextProductSigma);

        // Get all the nextProductSigmaList where name contains
        defaultNextProductSigmaFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductSigmasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextProductSigma = nextProductSigmaRepository.saveAndFlush(nextProductSigma);

        // Get all the nextProductSigmaList where name does not contain
        defaultNextProductSigmaFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductSigmasByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductSigma = nextProductSigmaRepository.saveAndFlush(nextProductSigma);

        // Get all the nextProductSigmaList where price equals to
        defaultNextProductSigmaFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductSigmasByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextProductSigma = nextProductSigmaRepository.saveAndFlush(nextProductSigma);

        // Get all the nextProductSigmaList where price in
        defaultNextProductSigmaFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductSigmasByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextProductSigma = nextProductSigmaRepository.saveAndFlush(nextProductSigma);

        // Get all the nextProductSigmaList where price is not null
        defaultNextProductSigmaFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllNextProductSigmasByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductSigma = nextProductSigmaRepository.saveAndFlush(nextProductSigma);

        // Get all the nextProductSigmaList where price is greater than or equal to
        defaultNextProductSigmaFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductSigmasByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductSigma = nextProductSigmaRepository.saveAndFlush(nextProductSigma);

        // Get all the nextProductSigmaList where price is less than or equal to
        defaultNextProductSigmaFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductSigmasByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextProductSigma = nextProductSigmaRepository.saveAndFlush(nextProductSigma);

        // Get all the nextProductSigmaList where price is less than
        defaultNextProductSigmaFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductSigmasByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextProductSigma = nextProductSigmaRepository.saveAndFlush(nextProductSigma);

        // Get all the nextProductSigmaList where price is greater than
        defaultNextProductSigmaFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductSigmasByStockIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductSigma = nextProductSigmaRepository.saveAndFlush(nextProductSigma);

        // Get all the nextProductSigmaList where stock equals to
        defaultNextProductSigmaFiltering("stock.equals=" + DEFAULT_STOCK, "stock.equals=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductSigmasByStockIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextProductSigma = nextProductSigmaRepository.saveAndFlush(nextProductSigma);

        // Get all the nextProductSigmaList where stock in
        defaultNextProductSigmaFiltering("stock.in=" + DEFAULT_STOCK + "," + UPDATED_STOCK, "stock.in=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductSigmasByStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextProductSigma = nextProductSigmaRepository.saveAndFlush(nextProductSigma);

        // Get all the nextProductSigmaList where stock is not null
        defaultNextProductSigmaFiltering("stock.specified=true", "stock.specified=false");
    }

    @Test
    @Transactional
    void getAllNextProductSigmasByStockIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductSigma = nextProductSigmaRepository.saveAndFlush(nextProductSigma);

        // Get all the nextProductSigmaList where stock is greater than or equal to
        defaultNextProductSigmaFiltering("stock.greaterThanOrEqual=" + DEFAULT_STOCK, "stock.greaterThanOrEqual=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductSigmasByStockIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductSigma = nextProductSigmaRepository.saveAndFlush(nextProductSigma);

        // Get all the nextProductSigmaList where stock is less than or equal to
        defaultNextProductSigmaFiltering("stock.lessThanOrEqual=" + DEFAULT_STOCK, "stock.lessThanOrEqual=" + SMALLER_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductSigmasByStockIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextProductSigma = nextProductSigmaRepository.saveAndFlush(nextProductSigma);

        // Get all the nextProductSigmaList where stock is less than
        defaultNextProductSigmaFiltering("stock.lessThan=" + UPDATED_STOCK, "stock.lessThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductSigmasByStockIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextProductSigma = nextProductSigmaRepository.saveAndFlush(nextProductSigma);

        // Get all the nextProductSigmaList where stock is greater than
        defaultNextProductSigmaFiltering("stock.greaterThan=" + SMALLER_STOCK, "stock.greaterThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductSigmasByCategoryIsEqualToSomething() throws Exception {
        NextCategorySigma category;
        if (TestUtil.findAll(em, NextCategorySigma.class).isEmpty()) {
            nextProductSigmaRepository.saveAndFlush(nextProductSigma);
            category = NextCategorySigmaResourceIT.createEntity();
        } else {
            category = TestUtil.findAll(em, NextCategorySigma.class).get(0);
        }
        em.persist(category);
        em.flush();
        nextProductSigma.setCategory(category);
        nextProductSigmaRepository.saveAndFlush(nextProductSigma);
        Long categoryId = category.getId();
        // Get all the nextProductSigmaList where category equals to categoryId
        defaultNextProductSigmaShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the nextProductSigmaList where category equals to (categoryId + 1)
        defaultNextProductSigmaShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllNextProductSigmasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextProductSigmaRepository.saveAndFlush(nextProductSigma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextProductSigma.setTenant(tenant);
        nextProductSigmaRepository.saveAndFlush(nextProductSigma);
        Long tenantId = tenant.getId();
        // Get all the nextProductSigmaList where tenant equals to tenantId
        defaultNextProductSigmaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextProductSigmaList where tenant equals to (tenantId + 1)
        defaultNextProductSigmaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllNextProductSigmasByOrderIsEqualToSomething() throws Exception {
        NextOrderSigma order;
        if (TestUtil.findAll(em, NextOrderSigma.class).isEmpty()) {
            nextProductSigmaRepository.saveAndFlush(nextProductSigma);
            order = NextOrderSigmaResourceIT.createEntity();
        } else {
            order = TestUtil.findAll(em, NextOrderSigma.class).get(0);
        }
        em.persist(order);
        em.flush();
        nextProductSigma.setOrder(order);
        nextProductSigmaRepository.saveAndFlush(nextProductSigma);
        Long orderId = order.getId();
        // Get all the nextProductSigmaList where order equals to orderId
        defaultNextProductSigmaShouldBeFound("orderId.equals=" + orderId);

        // Get all the nextProductSigmaList where order equals to (orderId + 1)
        defaultNextProductSigmaShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllNextProductSigmasBySuppliersIsEqualToSomething() throws Exception {
        NextSupplierSigma suppliers;
        if (TestUtil.findAll(em, NextSupplierSigma.class).isEmpty()) {
            nextProductSigmaRepository.saveAndFlush(nextProductSigma);
            suppliers = NextSupplierSigmaResourceIT.createEntity();
        } else {
            suppliers = TestUtil.findAll(em, NextSupplierSigma.class).get(0);
        }
        em.persist(suppliers);
        em.flush();
        nextProductSigma.addSuppliers(suppliers);
        nextProductSigmaRepository.saveAndFlush(nextProductSigma);
        Long suppliersId = suppliers.getId();
        // Get all the nextProductSigmaList where suppliers equals to suppliersId
        defaultNextProductSigmaShouldBeFound("suppliersId.equals=" + suppliersId);

        // Get all the nextProductSigmaList where suppliers equals to (suppliersId + 1)
        defaultNextProductSigmaShouldNotBeFound("suppliersId.equals=" + (suppliersId + 1));
    }

    private void defaultNextProductSigmaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextProductSigmaShouldBeFound(shouldBeFound);
        defaultNextProductSigmaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextProductSigmaShouldBeFound(String filter) throws Exception {
        restNextProductSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextProductSigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restNextProductSigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextProductSigmaShouldNotBeFound(String filter) throws Exception {
        restNextProductSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextProductSigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextProductSigma() throws Exception {
        // Get the nextProductSigma
        restNextProductSigmaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextProductSigma() throws Exception {
        // Initialize the database
        insertedNextProductSigma = nextProductSigmaRepository.saveAndFlush(nextProductSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextProductSigma
        NextProductSigma updatedNextProductSigma = nextProductSigmaRepository.findById(nextProductSigma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextProductSigma are not directly saved in db
        em.detach(updatedNextProductSigma);
        updatedNextProductSigma.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);
        NextProductSigmaDTO nextProductSigmaDTO = nextProductSigmaMapper.toDto(updatedNextProductSigma);

        restNextProductSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextProductSigmaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextProductSigmaDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextProductSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextProductSigmaToMatchAllProperties(updatedNextProductSigma);
    }

    @Test
    @Transactional
    void putNonExistingNextProductSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductSigma.setId(longCount.incrementAndGet());

        // Create the NextProductSigma
        NextProductSigmaDTO nextProductSigmaDTO = nextProductSigmaMapper.toDto(nextProductSigma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextProductSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextProductSigmaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextProductSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProductSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextProductSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductSigma.setId(longCount.incrementAndGet());

        // Create the NextProductSigma
        NextProductSigmaDTO nextProductSigmaDTO = nextProductSigmaMapper.toDto(nextProductSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextProductSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProductSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextProductSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductSigma.setId(longCount.incrementAndGet());

        // Create the NextProductSigma
        NextProductSigmaDTO nextProductSigmaDTO = nextProductSigmaMapper.toDto(nextProductSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductSigmaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductSigmaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextProductSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextProductSigmaWithPatch() throws Exception {
        // Initialize the database
        insertedNextProductSigma = nextProductSigmaRepository.saveAndFlush(nextProductSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextProductSigma using partial update
        NextProductSigma partialUpdatedNextProductSigma = new NextProductSigma();
        partialUpdatedNextProductSigma.setId(nextProductSigma.getId());

        partialUpdatedNextProductSigma.price(UPDATED_PRICE);

        restNextProductSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextProductSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextProductSigma))
            )
            .andExpect(status().isOk());

        // Validate the NextProductSigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextProductSigmaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextProductSigma, nextProductSigma),
            getPersistedNextProductSigma(nextProductSigma)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextProductSigmaWithPatch() throws Exception {
        // Initialize the database
        insertedNextProductSigma = nextProductSigmaRepository.saveAndFlush(nextProductSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextProductSigma using partial update
        NextProductSigma partialUpdatedNextProductSigma = new NextProductSigma();
        partialUpdatedNextProductSigma.setId(nextProductSigma.getId());

        partialUpdatedNextProductSigma.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);

        restNextProductSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextProductSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextProductSigma))
            )
            .andExpect(status().isOk());

        // Validate the NextProductSigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextProductSigmaUpdatableFieldsEquals(
            partialUpdatedNextProductSigma,
            getPersistedNextProductSigma(partialUpdatedNextProductSigma)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextProductSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductSigma.setId(longCount.incrementAndGet());

        // Create the NextProductSigma
        NextProductSigmaDTO nextProductSigmaDTO = nextProductSigmaMapper.toDto(nextProductSigma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextProductSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextProductSigmaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextProductSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProductSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextProductSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductSigma.setId(longCount.incrementAndGet());

        // Create the NextProductSigma
        NextProductSigmaDTO nextProductSigmaDTO = nextProductSigmaMapper.toDto(nextProductSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextProductSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProductSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextProductSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductSigma.setId(longCount.incrementAndGet());

        // Create the NextProductSigma
        NextProductSigmaDTO nextProductSigmaDTO = nextProductSigmaMapper.toDto(nextProductSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductSigmaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextProductSigmaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextProductSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextProductSigma() throws Exception {
        // Initialize the database
        insertedNextProductSigma = nextProductSigmaRepository.saveAndFlush(nextProductSigma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextProductSigma
        restNextProductSigmaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextProductSigma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextProductSigmaRepository.count();
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

    protected NextProductSigma getPersistedNextProductSigma(NextProductSigma nextProductSigma) {
        return nextProductSigmaRepository.findById(nextProductSigma.getId()).orElseThrow();
    }

    protected void assertPersistedNextProductSigmaToMatchAllProperties(NextProductSigma expectedNextProductSigma) {
        assertNextProductSigmaAllPropertiesEquals(expectedNextProductSigma, getPersistedNextProductSigma(expectedNextProductSigma));
    }

    protected void assertPersistedNextProductSigmaToMatchUpdatableProperties(NextProductSigma expectedNextProductSigma) {
        assertNextProductSigmaAllUpdatablePropertiesEquals(
            expectedNextProductSigma,
            getPersistedNextProductSigma(expectedNextProductSigma)
        );
    }
}
