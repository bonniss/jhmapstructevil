package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextProductAlphaAsserts.*;
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
import xyz.jhmapstruct.domain.NextCategoryAlpha;
import xyz.jhmapstruct.domain.NextOrderAlpha;
import xyz.jhmapstruct.domain.NextProductAlpha;
import xyz.jhmapstruct.domain.NextSupplierAlpha;
import xyz.jhmapstruct.repository.NextProductAlphaRepository;
import xyz.jhmapstruct.service.NextProductAlphaService;
import xyz.jhmapstruct.service.dto.NextProductAlphaDTO;
import xyz.jhmapstruct.service.mapper.NextProductAlphaMapper;

/**
 * Integration tests for the {@link NextProductAlphaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextProductAlphaResourceIT {

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

    private static final String ENTITY_API_URL = "/api/next-product-alphas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextProductAlphaRepository nextProductAlphaRepository;

    @Mock
    private NextProductAlphaRepository nextProductAlphaRepositoryMock;

    @Autowired
    private NextProductAlphaMapper nextProductAlphaMapper;

    @Mock
    private NextProductAlphaService nextProductAlphaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextProductAlphaMockMvc;

    private NextProductAlpha nextProductAlpha;

    private NextProductAlpha insertedNextProductAlpha;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextProductAlpha createEntity() {
        return new NextProductAlpha().name(DEFAULT_NAME).price(DEFAULT_PRICE).stock(DEFAULT_STOCK).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextProductAlpha createUpdatedEntity() {
        return new NextProductAlpha().name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        nextProductAlpha = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextProductAlpha != null) {
            nextProductAlphaRepository.delete(insertedNextProductAlpha);
            insertedNextProductAlpha = null;
        }
    }

    @Test
    @Transactional
    void createNextProductAlpha() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextProductAlpha
        NextProductAlphaDTO nextProductAlphaDTO = nextProductAlphaMapper.toDto(nextProductAlpha);
        var returnedNextProductAlphaDTO = om.readValue(
            restNextProductAlphaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductAlphaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextProductAlphaDTO.class
        );

        // Validate the NextProductAlpha in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextProductAlpha = nextProductAlphaMapper.toEntity(returnedNextProductAlphaDTO);
        assertNextProductAlphaUpdatableFieldsEquals(returnedNextProductAlpha, getPersistedNextProductAlpha(returnedNextProductAlpha));

        insertedNextProductAlpha = returnedNextProductAlpha;
    }

    @Test
    @Transactional
    void createNextProductAlphaWithExistingId() throws Exception {
        // Create the NextProductAlpha with an existing ID
        nextProductAlpha.setId(1L);
        NextProductAlphaDTO nextProductAlphaDTO = nextProductAlphaMapper.toDto(nextProductAlpha);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextProductAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductAlphaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextProductAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextProductAlpha.setName(null);

        // Create the NextProductAlpha, which fails.
        NextProductAlphaDTO nextProductAlphaDTO = nextProductAlphaMapper.toDto(nextProductAlpha);

        restNextProductAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductAlphaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextProductAlpha.setPrice(null);

        // Create the NextProductAlpha, which fails.
        NextProductAlphaDTO nextProductAlphaDTO = nextProductAlphaMapper.toDto(nextProductAlpha);

        restNextProductAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductAlphaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStockIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextProductAlpha.setStock(null);

        // Create the NextProductAlpha, which fails.
        NextProductAlphaDTO nextProductAlphaDTO = nextProductAlphaMapper.toDto(nextProductAlpha);

        restNextProductAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductAlphaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextProductAlphas() throws Exception {
        // Initialize the database
        insertedNextProductAlpha = nextProductAlphaRepository.saveAndFlush(nextProductAlpha);

        // Get all the nextProductAlphaList
        restNextProductAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextProductAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextProductAlphasWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextProductAlphaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextProductAlphaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextProductAlphaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextProductAlphasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextProductAlphaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextProductAlphaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextProductAlphaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextProductAlpha() throws Exception {
        // Initialize the database
        insertedNextProductAlpha = nextProductAlphaRepository.saveAndFlush(nextProductAlpha);

        // Get the nextProductAlpha
        restNextProductAlphaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextProductAlpha.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextProductAlpha.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getNextProductAlphasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextProductAlpha = nextProductAlphaRepository.saveAndFlush(nextProductAlpha);

        Long id = nextProductAlpha.getId();

        defaultNextProductAlphaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextProductAlphaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextProductAlphaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextProductAlphasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductAlpha = nextProductAlphaRepository.saveAndFlush(nextProductAlpha);

        // Get all the nextProductAlphaList where name equals to
        defaultNextProductAlphaFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductAlphasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextProductAlpha = nextProductAlphaRepository.saveAndFlush(nextProductAlpha);

        // Get all the nextProductAlphaList where name in
        defaultNextProductAlphaFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductAlphasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextProductAlpha = nextProductAlphaRepository.saveAndFlush(nextProductAlpha);

        // Get all the nextProductAlphaList where name is not null
        defaultNextProductAlphaFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllNextProductAlphasByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextProductAlpha = nextProductAlphaRepository.saveAndFlush(nextProductAlpha);

        // Get all the nextProductAlphaList where name contains
        defaultNextProductAlphaFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductAlphasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextProductAlpha = nextProductAlphaRepository.saveAndFlush(nextProductAlpha);

        // Get all the nextProductAlphaList where name does not contain
        defaultNextProductAlphaFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductAlphasByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductAlpha = nextProductAlphaRepository.saveAndFlush(nextProductAlpha);

        // Get all the nextProductAlphaList where price equals to
        defaultNextProductAlphaFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductAlphasByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextProductAlpha = nextProductAlphaRepository.saveAndFlush(nextProductAlpha);

        // Get all the nextProductAlphaList where price in
        defaultNextProductAlphaFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductAlphasByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextProductAlpha = nextProductAlphaRepository.saveAndFlush(nextProductAlpha);

        // Get all the nextProductAlphaList where price is not null
        defaultNextProductAlphaFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllNextProductAlphasByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductAlpha = nextProductAlphaRepository.saveAndFlush(nextProductAlpha);

        // Get all the nextProductAlphaList where price is greater than or equal to
        defaultNextProductAlphaFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductAlphasByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductAlpha = nextProductAlphaRepository.saveAndFlush(nextProductAlpha);

        // Get all the nextProductAlphaList where price is less than or equal to
        defaultNextProductAlphaFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductAlphasByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextProductAlpha = nextProductAlphaRepository.saveAndFlush(nextProductAlpha);

        // Get all the nextProductAlphaList where price is less than
        defaultNextProductAlphaFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductAlphasByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextProductAlpha = nextProductAlphaRepository.saveAndFlush(nextProductAlpha);

        // Get all the nextProductAlphaList where price is greater than
        defaultNextProductAlphaFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductAlphasByStockIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductAlpha = nextProductAlphaRepository.saveAndFlush(nextProductAlpha);

        // Get all the nextProductAlphaList where stock equals to
        defaultNextProductAlphaFiltering("stock.equals=" + DEFAULT_STOCK, "stock.equals=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductAlphasByStockIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextProductAlpha = nextProductAlphaRepository.saveAndFlush(nextProductAlpha);

        // Get all the nextProductAlphaList where stock in
        defaultNextProductAlphaFiltering("stock.in=" + DEFAULT_STOCK + "," + UPDATED_STOCK, "stock.in=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductAlphasByStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextProductAlpha = nextProductAlphaRepository.saveAndFlush(nextProductAlpha);

        // Get all the nextProductAlphaList where stock is not null
        defaultNextProductAlphaFiltering("stock.specified=true", "stock.specified=false");
    }

    @Test
    @Transactional
    void getAllNextProductAlphasByStockIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductAlpha = nextProductAlphaRepository.saveAndFlush(nextProductAlpha);

        // Get all the nextProductAlphaList where stock is greater than or equal to
        defaultNextProductAlphaFiltering("stock.greaterThanOrEqual=" + DEFAULT_STOCK, "stock.greaterThanOrEqual=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductAlphasByStockIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductAlpha = nextProductAlphaRepository.saveAndFlush(nextProductAlpha);

        // Get all the nextProductAlphaList where stock is less than or equal to
        defaultNextProductAlphaFiltering("stock.lessThanOrEqual=" + DEFAULT_STOCK, "stock.lessThanOrEqual=" + SMALLER_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductAlphasByStockIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextProductAlpha = nextProductAlphaRepository.saveAndFlush(nextProductAlpha);

        // Get all the nextProductAlphaList where stock is less than
        defaultNextProductAlphaFiltering("stock.lessThan=" + UPDATED_STOCK, "stock.lessThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductAlphasByStockIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextProductAlpha = nextProductAlphaRepository.saveAndFlush(nextProductAlpha);

        // Get all the nextProductAlphaList where stock is greater than
        defaultNextProductAlphaFiltering("stock.greaterThan=" + SMALLER_STOCK, "stock.greaterThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductAlphasByCategoryIsEqualToSomething() throws Exception {
        NextCategoryAlpha category;
        if (TestUtil.findAll(em, NextCategoryAlpha.class).isEmpty()) {
            nextProductAlphaRepository.saveAndFlush(nextProductAlpha);
            category = NextCategoryAlphaResourceIT.createEntity();
        } else {
            category = TestUtil.findAll(em, NextCategoryAlpha.class).get(0);
        }
        em.persist(category);
        em.flush();
        nextProductAlpha.setCategory(category);
        nextProductAlphaRepository.saveAndFlush(nextProductAlpha);
        Long categoryId = category.getId();
        // Get all the nextProductAlphaList where category equals to categoryId
        defaultNextProductAlphaShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the nextProductAlphaList where category equals to (categoryId + 1)
        defaultNextProductAlphaShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllNextProductAlphasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextProductAlphaRepository.saveAndFlush(nextProductAlpha);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextProductAlpha.setTenant(tenant);
        nextProductAlphaRepository.saveAndFlush(nextProductAlpha);
        Long tenantId = tenant.getId();
        // Get all the nextProductAlphaList where tenant equals to tenantId
        defaultNextProductAlphaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextProductAlphaList where tenant equals to (tenantId + 1)
        defaultNextProductAlphaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllNextProductAlphasByOrderIsEqualToSomething() throws Exception {
        NextOrderAlpha order;
        if (TestUtil.findAll(em, NextOrderAlpha.class).isEmpty()) {
            nextProductAlphaRepository.saveAndFlush(nextProductAlpha);
            order = NextOrderAlphaResourceIT.createEntity();
        } else {
            order = TestUtil.findAll(em, NextOrderAlpha.class).get(0);
        }
        em.persist(order);
        em.flush();
        nextProductAlpha.setOrder(order);
        nextProductAlphaRepository.saveAndFlush(nextProductAlpha);
        Long orderId = order.getId();
        // Get all the nextProductAlphaList where order equals to orderId
        defaultNextProductAlphaShouldBeFound("orderId.equals=" + orderId);

        // Get all the nextProductAlphaList where order equals to (orderId + 1)
        defaultNextProductAlphaShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllNextProductAlphasBySuppliersIsEqualToSomething() throws Exception {
        NextSupplierAlpha suppliers;
        if (TestUtil.findAll(em, NextSupplierAlpha.class).isEmpty()) {
            nextProductAlphaRepository.saveAndFlush(nextProductAlpha);
            suppliers = NextSupplierAlphaResourceIT.createEntity();
        } else {
            suppliers = TestUtil.findAll(em, NextSupplierAlpha.class).get(0);
        }
        em.persist(suppliers);
        em.flush();
        nextProductAlpha.addSuppliers(suppliers);
        nextProductAlphaRepository.saveAndFlush(nextProductAlpha);
        Long suppliersId = suppliers.getId();
        // Get all the nextProductAlphaList where suppliers equals to suppliersId
        defaultNextProductAlphaShouldBeFound("suppliersId.equals=" + suppliersId);

        // Get all the nextProductAlphaList where suppliers equals to (suppliersId + 1)
        defaultNextProductAlphaShouldNotBeFound("suppliersId.equals=" + (suppliersId + 1));
    }

    private void defaultNextProductAlphaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextProductAlphaShouldBeFound(shouldBeFound);
        defaultNextProductAlphaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextProductAlphaShouldBeFound(String filter) throws Exception {
        restNextProductAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextProductAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restNextProductAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextProductAlphaShouldNotBeFound(String filter) throws Exception {
        restNextProductAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextProductAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextProductAlpha() throws Exception {
        // Get the nextProductAlpha
        restNextProductAlphaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextProductAlpha() throws Exception {
        // Initialize the database
        insertedNextProductAlpha = nextProductAlphaRepository.saveAndFlush(nextProductAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextProductAlpha
        NextProductAlpha updatedNextProductAlpha = nextProductAlphaRepository.findById(nextProductAlpha.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextProductAlpha are not directly saved in db
        em.detach(updatedNextProductAlpha);
        updatedNextProductAlpha.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);
        NextProductAlphaDTO nextProductAlphaDTO = nextProductAlphaMapper.toDto(updatedNextProductAlpha);

        restNextProductAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextProductAlphaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextProductAlphaDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextProductAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextProductAlphaToMatchAllProperties(updatedNextProductAlpha);
    }

    @Test
    @Transactional
    void putNonExistingNextProductAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductAlpha.setId(longCount.incrementAndGet());

        // Create the NextProductAlpha
        NextProductAlphaDTO nextProductAlphaDTO = nextProductAlphaMapper.toDto(nextProductAlpha);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextProductAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextProductAlphaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextProductAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProductAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextProductAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductAlpha.setId(longCount.incrementAndGet());

        // Create the NextProductAlpha
        NextProductAlphaDTO nextProductAlphaDTO = nextProductAlphaMapper.toDto(nextProductAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextProductAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProductAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextProductAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductAlpha.setId(longCount.incrementAndGet());

        // Create the NextProductAlpha
        NextProductAlphaDTO nextProductAlphaDTO = nextProductAlphaMapper.toDto(nextProductAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductAlphaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductAlphaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextProductAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextProductAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedNextProductAlpha = nextProductAlphaRepository.saveAndFlush(nextProductAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextProductAlpha using partial update
        NextProductAlpha partialUpdatedNextProductAlpha = new NextProductAlpha();
        partialUpdatedNextProductAlpha.setId(nextProductAlpha.getId());

        partialUpdatedNextProductAlpha.price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);

        restNextProductAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextProductAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextProductAlpha))
            )
            .andExpect(status().isOk());

        // Validate the NextProductAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextProductAlphaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextProductAlpha, nextProductAlpha),
            getPersistedNextProductAlpha(nextProductAlpha)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextProductAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedNextProductAlpha = nextProductAlphaRepository.saveAndFlush(nextProductAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextProductAlpha using partial update
        NextProductAlpha partialUpdatedNextProductAlpha = new NextProductAlpha();
        partialUpdatedNextProductAlpha.setId(nextProductAlpha.getId());

        partialUpdatedNextProductAlpha.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);

        restNextProductAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextProductAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextProductAlpha))
            )
            .andExpect(status().isOk());

        // Validate the NextProductAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextProductAlphaUpdatableFieldsEquals(
            partialUpdatedNextProductAlpha,
            getPersistedNextProductAlpha(partialUpdatedNextProductAlpha)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextProductAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductAlpha.setId(longCount.incrementAndGet());

        // Create the NextProductAlpha
        NextProductAlphaDTO nextProductAlphaDTO = nextProductAlphaMapper.toDto(nextProductAlpha);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextProductAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextProductAlphaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextProductAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProductAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextProductAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductAlpha.setId(longCount.incrementAndGet());

        // Create the NextProductAlpha
        NextProductAlphaDTO nextProductAlphaDTO = nextProductAlphaMapper.toDto(nextProductAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextProductAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProductAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextProductAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductAlpha.setId(longCount.incrementAndGet());

        // Create the NextProductAlpha
        NextProductAlphaDTO nextProductAlphaDTO = nextProductAlphaMapper.toDto(nextProductAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductAlphaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextProductAlphaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextProductAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextProductAlpha() throws Exception {
        // Initialize the database
        insertedNextProductAlpha = nextProductAlphaRepository.saveAndFlush(nextProductAlpha);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextProductAlpha
        restNextProductAlphaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextProductAlpha.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextProductAlphaRepository.count();
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

    protected NextProductAlpha getPersistedNextProductAlpha(NextProductAlpha nextProductAlpha) {
        return nextProductAlphaRepository.findById(nextProductAlpha.getId()).orElseThrow();
    }

    protected void assertPersistedNextProductAlphaToMatchAllProperties(NextProductAlpha expectedNextProductAlpha) {
        assertNextProductAlphaAllPropertiesEquals(expectedNextProductAlpha, getPersistedNextProductAlpha(expectedNextProductAlpha));
    }

    protected void assertPersistedNextProductAlphaToMatchUpdatableProperties(NextProductAlpha expectedNextProductAlpha) {
        assertNextProductAlphaAllUpdatablePropertiesEquals(
            expectedNextProductAlpha,
            getPersistedNextProductAlpha(expectedNextProductAlpha)
        );
    }
}
