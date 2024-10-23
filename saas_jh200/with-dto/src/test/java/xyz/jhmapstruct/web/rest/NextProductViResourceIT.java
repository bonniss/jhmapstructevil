package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextProductViAsserts.*;
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
import xyz.jhmapstruct.domain.NextCategoryVi;
import xyz.jhmapstruct.domain.NextOrderVi;
import xyz.jhmapstruct.domain.NextProductVi;
import xyz.jhmapstruct.domain.NextSupplierVi;
import xyz.jhmapstruct.repository.NextProductViRepository;
import xyz.jhmapstruct.service.NextProductViService;
import xyz.jhmapstruct.service.dto.NextProductViDTO;
import xyz.jhmapstruct.service.mapper.NextProductViMapper;

/**
 * Integration tests for the {@link NextProductViResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextProductViResourceIT {

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

    private static final String ENTITY_API_URL = "/api/next-product-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextProductViRepository nextProductViRepository;

    @Mock
    private NextProductViRepository nextProductViRepositoryMock;

    @Autowired
    private NextProductViMapper nextProductViMapper;

    @Mock
    private NextProductViService nextProductViServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextProductViMockMvc;

    private NextProductVi nextProductVi;

    private NextProductVi insertedNextProductVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextProductVi createEntity() {
        return new NextProductVi().name(DEFAULT_NAME).price(DEFAULT_PRICE).stock(DEFAULT_STOCK).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextProductVi createUpdatedEntity() {
        return new NextProductVi().name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        nextProductVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextProductVi != null) {
            nextProductViRepository.delete(insertedNextProductVi);
            insertedNextProductVi = null;
        }
    }

    @Test
    @Transactional
    void createNextProductVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextProductVi
        NextProductViDTO nextProductViDTO = nextProductViMapper.toDto(nextProductVi);
        var returnedNextProductViDTO = om.readValue(
            restNextProductViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextProductViDTO.class
        );

        // Validate the NextProductVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextProductVi = nextProductViMapper.toEntity(returnedNextProductViDTO);
        assertNextProductViUpdatableFieldsEquals(returnedNextProductVi, getPersistedNextProductVi(returnedNextProductVi));

        insertedNextProductVi = returnedNextProductVi;
    }

    @Test
    @Transactional
    void createNextProductViWithExistingId() throws Exception {
        // Create the NextProductVi with an existing ID
        nextProductVi.setId(1L);
        NextProductViDTO nextProductViDTO = nextProductViMapper.toDto(nextProductVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextProductViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextProductVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextProductVi.setName(null);

        // Create the NextProductVi, which fails.
        NextProductViDTO nextProductViDTO = nextProductViMapper.toDto(nextProductVi);

        restNextProductViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextProductVi.setPrice(null);

        // Create the NextProductVi, which fails.
        NextProductViDTO nextProductViDTO = nextProductViMapper.toDto(nextProductVi);

        restNextProductViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStockIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextProductVi.setStock(null);

        // Create the NextProductVi, which fails.
        NextProductViDTO nextProductViDTO = nextProductViMapper.toDto(nextProductVi);

        restNextProductViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextProductVis() throws Exception {
        // Initialize the database
        insertedNextProductVi = nextProductViRepository.saveAndFlush(nextProductVi);

        // Get all the nextProductViList
        restNextProductViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextProductVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextProductVisWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextProductViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextProductViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextProductViServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextProductVisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextProductViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextProductViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextProductViRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextProductVi() throws Exception {
        // Initialize the database
        insertedNextProductVi = nextProductViRepository.saveAndFlush(nextProductVi);

        // Get the nextProductVi
        restNextProductViMockMvc
            .perform(get(ENTITY_API_URL_ID, nextProductVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextProductVi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getNextProductVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextProductVi = nextProductViRepository.saveAndFlush(nextProductVi);

        Long id = nextProductVi.getId();

        defaultNextProductViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextProductViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextProductViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextProductVisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductVi = nextProductViRepository.saveAndFlush(nextProductVi);

        // Get all the nextProductViList where name equals to
        defaultNextProductViFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductVisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextProductVi = nextProductViRepository.saveAndFlush(nextProductVi);

        // Get all the nextProductViList where name in
        defaultNextProductViFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductVisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextProductVi = nextProductViRepository.saveAndFlush(nextProductVi);

        // Get all the nextProductViList where name is not null
        defaultNextProductViFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllNextProductVisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextProductVi = nextProductViRepository.saveAndFlush(nextProductVi);

        // Get all the nextProductViList where name contains
        defaultNextProductViFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductVisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextProductVi = nextProductViRepository.saveAndFlush(nextProductVi);

        // Get all the nextProductViList where name does not contain
        defaultNextProductViFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllNextProductVisByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductVi = nextProductViRepository.saveAndFlush(nextProductVi);

        // Get all the nextProductViList where price equals to
        defaultNextProductViFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductVisByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextProductVi = nextProductViRepository.saveAndFlush(nextProductVi);

        // Get all the nextProductViList where price in
        defaultNextProductViFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductVisByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextProductVi = nextProductViRepository.saveAndFlush(nextProductVi);

        // Get all the nextProductViList where price is not null
        defaultNextProductViFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllNextProductVisByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductVi = nextProductViRepository.saveAndFlush(nextProductVi);

        // Get all the nextProductViList where price is greater than or equal to
        defaultNextProductViFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductVisByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductVi = nextProductViRepository.saveAndFlush(nextProductVi);

        // Get all the nextProductViList where price is less than or equal to
        defaultNextProductViFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductVisByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextProductVi = nextProductViRepository.saveAndFlush(nextProductVi);

        // Get all the nextProductViList where price is less than
        defaultNextProductViFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductVisByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextProductVi = nextProductViRepository.saveAndFlush(nextProductVi);

        // Get all the nextProductViList where price is greater than
        defaultNextProductViFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllNextProductVisByStockIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductVi = nextProductViRepository.saveAndFlush(nextProductVi);

        // Get all the nextProductViList where stock equals to
        defaultNextProductViFiltering("stock.equals=" + DEFAULT_STOCK, "stock.equals=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductVisByStockIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextProductVi = nextProductViRepository.saveAndFlush(nextProductVi);

        // Get all the nextProductViList where stock in
        defaultNextProductViFiltering("stock.in=" + DEFAULT_STOCK + "," + UPDATED_STOCK, "stock.in=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductVisByStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextProductVi = nextProductViRepository.saveAndFlush(nextProductVi);

        // Get all the nextProductViList where stock is not null
        defaultNextProductViFiltering("stock.specified=true", "stock.specified=false");
    }

    @Test
    @Transactional
    void getAllNextProductVisByStockIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductVi = nextProductViRepository.saveAndFlush(nextProductVi);

        // Get all the nextProductViList where stock is greater than or equal to
        defaultNextProductViFiltering("stock.greaterThanOrEqual=" + DEFAULT_STOCK, "stock.greaterThanOrEqual=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductVisByStockIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextProductVi = nextProductViRepository.saveAndFlush(nextProductVi);

        // Get all the nextProductViList where stock is less than or equal to
        defaultNextProductViFiltering("stock.lessThanOrEqual=" + DEFAULT_STOCK, "stock.lessThanOrEqual=" + SMALLER_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductVisByStockIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextProductVi = nextProductViRepository.saveAndFlush(nextProductVi);

        // Get all the nextProductViList where stock is less than
        defaultNextProductViFiltering("stock.lessThan=" + UPDATED_STOCK, "stock.lessThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductVisByStockIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextProductVi = nextProductViRepository.saveAndFlush(nextProductVi);

        // Get all the nextProductViList where stock is greater than
        defaultNextProductViFiltering("stock.greaterThan=" + SMALLER_STOCK, "stock.greaterThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllNextProductVisByCategoryIsEqualToSomething() throws Exception {
        NextCategoryVi category;
        if (TestUtil.findAll(em, NextCategoryVi.class).isEmpty()) {
            nextProductViRepository.saveAndFlush(nextProductVi);
            category = NextCategoryViResourceIT.createEntity();
        } else {
            category = TestUtil.findAll(em, NextCategoryVi.class).get(0);
        }
        em.persist(category);
        em.flush();
        nextProductVi.setCategory(category);
        nextProductViRepository.saveAndFlush(nextProductVi);
        Long categoryId = category.getId();
        // Get all the nextProductViList where category equals to categoryId
        defaultNextProductViShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the nextProductViList where category equals to (categoryId + 1)
        defaultNextProductViShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllNextProductVisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextProductViRepository.saveAndFlush(nextProductVi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextProductVi.setTenant(tenant);
        nextProductViRepository.saveAndFlush(nextProductVi);
        Long tenantId = tenant.getId();
        // Get all the nextProductViList where tenant equals to tenantId
        defaultNextProductViShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextProductViList where tenant equals to (tenantId + 1)
        defaultNextProductViShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllNextProductVisByOrderIsEqualToSomething() throws Exception {
        NextOrderVi order;
        if (TestUtil.findAll(em, NextOrderVi.class).isEmpty()) {
            nextProductViRepository.saveAndFlush(nextProductVi);
            order = NextOrderViResourceIT.createEntity();
        } else {
            order = TestUtil.findAll(em, NextOrderVi.class).get(0);
        }
        em.persist(order);
        em.flush();
        nextProductVi.setOrder(order);
        nextProductViRepository.saveAndFlush(nextProductVi);
        Long orderId = order.getId();
        // Get all the nextProductViList where order equals to orderId
        defaultNextProductViShouldBeFound("orderId.equals=" + orderId);

        // Get all the nextProductViList where order equals to (orderId + 1)
        defaultNextProductViShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllNextProductVisBySuppliersIsEqualToSomething() throws Exception {
        NextSupplierVi suppliers;
        if (TestUtil.findAll(em, NextSupplierVi.class).isEmpty()) {
            nextProductViRepository.saveAndFlush(nextProductVi);
            suppliers = NextSupplierViResourceIT.createEntity();
        } else {
            suppliers = TestUtil.findAll(em, NextSupplierVi.class).get(0);
        }
        em.persist(suppliers);
        em.flush();
        nextProductVi.addSuppliers(suppliers);
        nextProductViRepository.saveAndFlush(nextProductVi);
        Long suppliersId = suppliers.getId();
        // Get all the nextProductViList where suppliers equals to suppliersId
        defaultNextProductViShouldBeFound("suppliersId.equals=" + suppliersId);

        // Get all the nextProductViList where suppliers equals to (suppliersId + 1)
        defaultNextProductViShouldNotBeFound("suppliersId.equals=" + (suppliersId + 1));
    }

    private void defaultNextProductViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextProductViShouldBeFound(shouldBeFound);
        defaultNextProductViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextProductViShouldBeFound(String filter) throws Exception {
        restNextProductViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextProductVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restNextProductViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextProductViShouldNotBeFound(String filter) throws Exception {
        restNextProductViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextProductViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextProductVi() throws Exception {
        // Get the nextProductVi
        restNextProductViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextProductVi() throws Exception {
        // Initialize the database
        insertedNextProductVi = nextProductViRepository.saveAndFlush(nextProductVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextProductVi
        NextProductVi updatedNextProductVi = nextProductViRepository.findById(nextProductVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextProductVi are not directly saved in db
        em.detach(updatedNextProductVi);
        updatedNextProductVi.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);
        NextProductViDTO nextProductViDTO = nextProductViMapper.toDto(updatedNextProductVi);

        restNextProductViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextProductViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextProductViDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextProductVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextProductViToMatchAllProperties(updatedNextProductVi);
    }

    @Test
    @Transactional
    void putNonExistingNextProductVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductVi.setId(longCount.incrementAndGet());

        // Create the NextProductVi
        NextProductViDTO nextProductViDTO = nextProductViMapper.toDto(nextProductVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextProductViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextProductViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextProductViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProductVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextProductVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductVi.setId(longCount.incrementAndGet());

        // Create the NextProductVi
        NextProductViDTO nextProductViDTO = nextProductViMapper.toDto(nextProductVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextProductViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProductVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextProductVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductVi.setId(longCount.incrementAndGet());

        // Create the NextProductVi
        NextProductViDTO nextProductViDTO = nextProductViMapper.toDto(nextProductVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextProductViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextProductVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextProductViWithPatch() throws Exception {
        // Initialize the database
        insertedNextProductVi = nextProductViRepository.saveAndFlush(nextProductVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextProductVi using partial update
        NextProductVi partialUpdatedNextProductVi = new NextProductVi();
        partialUpdatedNextProductVi.setId(nextProductVi.getId());

        partialUpdatedNextProductVi.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);

        restNextProductViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextProductVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextProductVi))
            )
            .andExpect(status().isOk());

        // Validate the NextProductVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextProductViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextProductVi, nextProductVi),
            getPersistedNextProductVi(nextProductVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextProductViWithPatch() throws Exception {
        // Initialize the database
        insertedNextProductVi = nextProductViRepository.saveAndFlush(nextProductVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextProductVi using partial update
        NextProductVi partialUpdatedNextProductVi = new NextProductVi();
        partialUpdatedNextProductVi.setId(nextProductVi.getId());

        partialUpdatedNextProductVi.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);

        restNextProductViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextProductVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextProductVi))
            )
            .andExpect(status().isOk());

        // Validate the NextProductVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextProductViUpdatableFieldsEquals(partialUpdatedNextProductVi, getPersistedNextProductVi(partialUpdatedNextProductVi));
    }

    @Test
    @Transactional
    void patchNonExistingNextProductVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductVi.setId(longCount.incrementAndGet());

        // Create the NextProductVi
        NextProductViDTO nextProductViDTO = nextProductViMapper.toDto(nextProductVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextProductViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextProductViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextProductViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProductVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextProductVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductVi.setId(longCount.incrementAndGet());

        // Create the NextProductVi
        NextProductViDTO nextProductViDTO = nextProductViMapper.toDto(nextProductVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextProductViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextProductVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextProductVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextProductVi.setId(longCount.incrementAndGet());

        // Create the NextProductVi
        NextProductViDTO nextProductViDTO = nextProductViMapper.toDto(nextProductVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextProductViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextProductViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextProductVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextProductVi() throws Exception {
        // Initialize the database
        insertedNextProductVi = nextProductViRepository.saveAndFlush(nextProductVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextProductVi
        restNextProductViMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextProductVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextProductViRepository.count();
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

    protected NextProductVi getPersistedNextProductVi(NextProductVi nextProductVi) {
        return nextProductViRepository.findById(nextProductVi.getId()).orElseThrow();
    }

    protected void assertPersistedNextProductViToMatchAllProperties(NextProductVi expectedNextProductVi) {
        assertNextProductViAllPropertiesEquals(expectedNextProductVi, getPersistedNextProductVi(expectedNextProductVi));
    }

    protected void assertPersistedNextProductViToMatchUpdatableProperties(NextProductVi expectedNextProductVi) {
        assertNextProductViAllUpdatablePropertiesEquals(expectedNextProductVi, getPersistedNextProductVi(expectedNextProductVi));
    }
}
