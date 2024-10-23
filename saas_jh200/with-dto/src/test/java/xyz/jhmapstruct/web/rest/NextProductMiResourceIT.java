package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextProductMiAsserts.*;
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
import xyz.jhmapstruct.domain.CategoryMi;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextOrderMi;
import xyz.jhmapstruct.domain.NextProductMi;
import xyz.jhmapstruct.domain.SupplierMi;
import xyz.jhmapstruct.repository.NextProductMiRepository;
import xyz.jhmapstruct.service.NextProductMiService;
import xyz.jhmapstruct.service.dto.NextProductMiDTO;
import xyz.jhmapstruct.service.mapper.NextProductMiMapper;

/**
 * Integration tests for the {@link NextProductMiResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextProductMiResourceIT {

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

    private static final String ENTITY_API_URL = "/api/next-product-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextProductMiRepository nextProductMiRepository;

    @Mock
    private NextProductMiRepository nextProductMiRepositoryMock;

    @Autowired
    private NextProductMiMapper nextProductMiMapper;

    @Mock
    private NextProductMiService nextProductMiServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextProductMiMockMvc;

    private NextProductMi nextProductMi;

    private NextProductMi insertedNextProductMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextProductMi createEntity() {
        return new NextProductMi().name(DEFAULT_NAME).price(DEFAULT_PRICE).stock(DEFAULT_STOCK).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextProductMi createUpdatedEntity() {
        return new NextProductMi().name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        nextProductMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextProductMi != null) {
            nextProductMiRepository.delete(insertedNextProductMi);
            insertedNextProductMi = null;
        }
    }

    @Test
    @Transactional
    void createNextProductMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextProductMi
        NextProductMiDTO nextProductMiDTO = nextProductMiMapper.toDto(nextProductMi);
        var returnedNextProductMiDTO = om.readValue(
            restNextProductMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductMiDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextProductMiDTO.class
        );

        // Validate the NextProductMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextProductMi = nextProductMiMapper.toEntity(returnedNextProductMiDTO);
        assertNextProductMiUpdatableFieldsEquals(returnedNextProductMi, getPersistedNextProductMi(returnedNextProductMi));

        insertedNextProductMi = returnedNextProductMi;
    }

    @Test
    @Transactional
    void createNextProductMiWithExistingId() throws Exception {
        // Create the NextProductMi with an existing ID
        nextProductMi.setId(1L);
        NextProductMiDTO nextProductMiDTO = nextProductMiMapper.toDto(nextProductMi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextProductMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductMiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextProductMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextProductMi.setName(null);

        // Create the NextProductMi, which fails.
        NextProductMiDTO nextProductMiDTO = nextProductMiMapper.toDto(nextProductMi);

        restNextProductMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextProductMi.setPrice(null);

        // Create the NextProductMi, which fails.
        NextProductMiDTO nextProductMiDTO = nextProductMiMapper.toDto(nextProductMi);

        restNextProductMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStockIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextProductMi.setStock(null);

        // Create the NextProductMi, which fails.
        NextProductMiDTO nextProductMiDTO = nextProductMiMapper.toDto(nextProductMi);

        restNextProductMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextProductMis() throws Exception {
        // Initialize the database
        insertedNextProductMi = nextProductMiRepository.saveAndFlush(nextProductMi);

        // Get all the nextProductMiList
        restNextProductMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextProductMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextProductMisWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextProductMiServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextProductMiMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextProductMiServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextProductMisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextProductMiServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextProductMiMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextProductMiRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextProductMi() throws Exception {
        // Initialize the database
        insertedNextProductMi = nextProductMiRepository.saveAndFlush(nextProductMi);

        // Get the nextProductMi
        restNextProductMiMockMvc
            .perform(get(ENTITY_API_URL_ID, nextProductMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextProductMi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getNextProductMisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextProductMi = nextProductMiRepository.saveAndFlush(nextProductMi);

        Long id = nextProductMi.getId();

        defaultNextProductMiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextProductMiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextProductMiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextProductMisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductMi = nextProductMiRepository.saveAndFlush(nextProductMi);

        // Get all the nextProductMiList where name equals to
        defaultNextProductMiFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductMisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextProductMi = nextProductMiRepository.saveAndFlush(nextProductMi);

        // Get all the nextProductMiList where name in
        defaultNextProductMiFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductMisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextProductMi = nextProductMiRepository.saveAndFlush(nextProductMi);

        // Get all the nextProductMiList where name is not null
        defaultNextProductMiFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllNextProductMisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextProductMi = nextProductMiRepository.saveAndFlush(nextProductMi);

        // Get all the nextProductMiList where name contains
        defaultNextProductMiFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductMisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextProductMi = nextProductMiRepository.saveAndFlush(nextProductMi);

        // Get all the nextProductMiList where name does not contain
        defaultNextProductMiFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductMisByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductMi = nextProductMiRepository.saveAndFlush(nextProductMi);

        // Get all the nextProductMiList where price equals to
        defaultNextProductMiFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductMisByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextProductMi = nextProductMiRepository.saveAndFlush(nextProductMi);

        // Get all the nextProductMiList where price in
        defaultNextProductMiFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductMisByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextProductMi = nextProductMiRepository.saveAndFlush(nextProductMi);

        // Get all the nextProductMiList where price is not null
        defaultNextProductMiFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllNextProductMisByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductMi = nextProductMiRepository.saveAndFlush(nextProductMi);

        // Get all the nextProductMiList where price is greater than or equal to
        defaultNextProductMiFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductMisByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductMi = nextProductMiRepository.saveAndFlush(nextProductMi);

        // Get all the nextProductMiList where price is less than or equal to
        defaultNextProductMiFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductMisByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextProductMi = nextProductMiRepository.saveAndFlush(nextProductMi);

        // Get all the nextProductMiList where price is less than
        defaultNextProductMiFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductMisByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextProductMi = nextProductMiRepository.saveAndFlush(nextProductMi);

        // Get all the nextProductMiList where price is greater than
        defaultNextProductMiFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductMisByStockIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductMi = nextProductMiRepository.saveAndFlush(nextProductMi);

        // Get all the nextProductMiList where stock equals to
        defaultNextProductMiFiltering("stock.equals=" + DEFAULT_STOCK, "stock.equals=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductMisByStockIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextProductMi = nextProductMiRepository.saveAndFlush(nextProductMi);

        // Get all the nextProductMiList where stock in
        defaultNextProductMiFiltering("stock.in=" + DEFAULT_STOCK + "," + UPDATED_STOCK, "stock.in=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductMisByStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextProductMi = nextProductMiRepository.saveAndFlush(nextProductMi);

        // Get all the nextProductMiList where stock is not null
        defaultNextProductMiFiltering("stock.specified=true", "stock.specified=false");
    }

    @Test
    @Transactional
    void getAllNextProductMisByStockIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductMi = nextProductMiRepository.saveAndFlush(nextProductMi);

        // Get all the nextProductMiList where stock is greater than or equal to
        defaultNextProductMiFiltering("stock.greaterThanOrEqual=" + DEFAULT_STOCK, "stock.greaterThanOrEqual=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductMisByStockIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductMi = nextProductMiRepository.saveAndFlush(nextProductMi);

        // Get all the nextProductMiList where stock is less than or equal to
        defaultNextProductMiFiltering("stock.lessThanOrEqual=" + DEFAULT_STOCK, "stock.lessThanOrEqual=" + SMALLER_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductMisByStockIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextProductMi = nextProductMiRepository.saveAndFlush(nextProductMi);

        // Get all the nextProductMiList where stock is less than
        defaultNextProductMiFiltering("stock.lessThan=" + UPDATED_STOCK, "stock.lessThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductMisByStockIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextProductMi = nextProductMiRepository.saveAndFlush(nextProductMi);

        // Get all the nextProductMiList where stock is greater than
        defaultNextProductMiFiltering("stock.greaterThan=" + SMALLER_STOCK, "stock.greaterThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductMisByCategoryIsEqualToSomething() throws Exception {
        CategoryMi category;
        if (TestUtil.findAll(em, CategoryMi.class).isEmpty()) {
            nextProductMiRepository.saveAndFlush(nextProductMi);
            category = CategoryMiResourceIT.createEntity();
        } else {
            category = TestUtil.findAll(em, CategoryMi.class).get(0);
        }
        em.persist(category);
        em.flush();
        nextProductMi.setCategory(category);
        nextProductMiRepository.saveAndFlush(nextProductMi);
        Long categoryId = category.getId();
        // Get all the nextProductMiList where category equals to categoryId
        defaultNextProductMiShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the nextProductMiList where category equals to (categoryId + 1)
        defaultNextProductMiShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllNextProductMisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextProductMiRepository.saveAndFlush(nextProductMi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextProductMi.setTenant(tenant);
        nextProductMiRepository.saveAndFlush(nextProductMi);
        Long tenantId = tenant.getId();
        // Get all the nextProductMiList where tenant equals to tenantId
        defaultNextProductMiShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextProductMiList where tenant equals to (tenantId + 1)
        defaultNextProductMiShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllNextProductMisByOrderIsEqualToSomething() throws Exception {
        NextOrderMi order;
        if (TestUtil.findAll(em, NextOrderMi.class).isEmpty()) {
            nextProductMiRepository.saveAndFlush(nextProductMi);
            order = NextOrderMiResourceIT.createEntity();
        } else {
            order = TestUtil.findAll(em, NextOrderMi.class).get(0);
        }
        em.persist(order);
        em.flush();
        nextProductMi.setOrder(order);
        nextProductMiRepository.saveAndFlush(nextProductMi);
        Long orderId = order.getId();
        // Get all the nextProductMiList where order equals to orderId
        defaultNextProductMiShouldBeFound("orderId.equals=" + orderId);

        // Get all the nextProductMiList where order equals to (orderId + 1)
        defaultNextProductMiShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllNextProductMisBySuppliersIsEqualToSomething() throws Exception {
        SupplierMi suppliers;
        if (TestUtil.findAll(em, SupplierMi.class).isEmpty()) {
            nextProductMiRepository.saveAndFlush(nextProductMi);
            suppliers = SupplierMiResourceIT.createEntity();
        } else {
            suppliers = TestUtil.findAll(em, SupplierMi.class).get(0);
        }
        em.persist(suppliers);
        em.flush();
        nextProductMi.addSuppliers(suppliers);
        nextProductMiRepository.saveAndFlush(nextProductMi);
        Long suppliersId = suppliers.getId();
        // Get all the nextProductMiList where suppliers equals to suppliersId
        defaultNextProductMiShouldBeFound("suppliersId.equals=" + suppliersId);

        // Get all the nextProductMiList where suppliers equals to (suppliersId + 1)
        defaultNextProductMiShouldNotBeFound("suppliersId.equals=" + (suppliersId + 1));
    }

    private void defaultNextProductMiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextProductMiShouldBeFound(shouldBeFound);
        defaultNextProductMiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextProductMiShouldBeFound(String filter) throws Exception {
        restNextProductMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextProductMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restNextProductMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextProductMiShouldNotBeFound(String filter) throws Exception {
        restNextProductMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextProductMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextProductMi() throws Exception {
        // Get the nextProductMi
        restNextProductMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextProductMi() throws Exception {
        // Initialize the database
        insertedNextProductMi = nextProductMiRepository.saveAndFlush(nextProductMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextProductMi
        NextProductMi updatedNextProductMi = nextProductMiRepository.findById(nextProductMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextProductMi are not directly saved in db
        em.detach(updatedNextProductMi);
        updatedNextProductMi.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);
        NextProductMiDTO nextProductMiDTO = nextProductMiMapper.toDto(updatedNextProductMi);

        restNextProductMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextProductMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextProductMiDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextProductMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextProductMiToMatchAllProperties(updatedNextProductMi);
    }

    @Test
    @Transactional
    void putNonExistingNextProductMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductMi.setId(longCount.incrementAndGet());

        // Create the NextProductMi
        NextProductMiDTO nextProductMiDTO = nextProductMiMapper.toDto(nextProductMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextProductMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextProductMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextProductMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProductMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextProductMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductMi.setId(longCount.incrementAndGet());

        // Create the NextProductMi
        NextProductMiDTO nextProductMiDTO = nextProductMiMapper.toDto(nextProductMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextProductMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProductMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextProductMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductMi.setId(longCount.incrementAndGet());

        // Create the NextProductMi
        NextProductMiDTO nextProductMiDTO = nextProductMiMapper.toDto(nextProductMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextProductMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextProductMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextProductMi = nextProductMiRepository.saveAndFlush(nextProductMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextProductMi using partial update
        NextProductMi partialUpdatedNextProductMi = new NextProductMi();
        partialUpdatedNextProductMi.setId(nextProductMi.getId());

        partialUpdatedNextProductMi.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);

        restNextProductMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextProductMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextProductMi))
            )
            .andExpect(status().isOk());

        // Validate the NextProductMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextProductMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextProductMi, nextProductMi),
            getPersistedNextProductMi(nextProductMi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextProductMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextProductMi = nextProductMiRepository.saveAndFlush(nextProductMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextProductMi using partial update
        NextProductMi partialUpdatedNextProductMi = new NextProductMi();
        partialUpdatedNextProductMi.setId(nextProductMi.getId());

        partialUpdatedNextProductMi.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);

        restNextProductMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextProductMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextProductMi))
            )
            .andExpect(status().isOk());

        // Validate the NextProductMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextProductMiUpdatableFieldsEquals(partialUpdatedNextProductMi, getPersistedNextProductMi(partialUpdatedNextProductMi));
    }

    @Test
    @Transactional
    void patchNonExistingNextProductMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductMi.setId(longCount.incrementAndGet());

        // Create the NextProductMi
        NextProductMiDTO nextProductMiDTO = nextProductMiMapper.toDto(nextProductMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextProductMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextProductMiDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextProductMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProductMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextProductMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductMi.setId(longCount.incrementAndGet());

        // Create the NextProductMi
        NextProductMiDTO nextProductMiDTO = nextProductMiMapper.toDto(nextProductMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextProductMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProductMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextProductMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductMi.setId(longCount.incrementAndGet());

        // Create the NextProductMi
        NextProductMiDTO nextProductMiDTO = nextProductMiMapper.toDto(nextProductMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextProductMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextProductMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextProductMi() throws Exception {
        // Initialize the database
        insertedNextProductMi = nextProductMiRepository.saveAndFlush(nextProductMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextProductMi
        restNextProductMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextProductMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextProductMiRepository.count();
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

    protected NextProductMi getPersistedNextProductMi(NextProductMi nextProductMi) {
        return nextProductMiRepository.findById(nextProductMi.getId()).orElseThrow();
    }

    protected void assertPersistedNextProductMiToMatchAllProperties(NextProductMi expectedNextProductMi) {
        assertNextProductMiAllPropertiesEquals(expectedNextProductMi, getPersistedNextProductMi(expectedNextProductMi));
    }

    protected void assertPersistedNextProductMiToMatchUpdatableProperties(NextProductMi expectedNextProductMi) {
        assertNextProductMiAllUpdatablePropertiesEquals(expectedNextProductMi, getPersistedNextProductMi(expectedNextProductMi));
    }
}
