package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.OrderSigmaAsserts.*;
import static xyz.jhmapstruct.web.rest.TestUtil.createUpdateProxyForBean;
import static xyz.jhmapstruct.web.rest.TestUtil.sameNumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import xyz.jhmapstruct.domain.CustomerSigma;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.OrderSigma;
import xyz.jhmapstruct.domain.PaymentSigma;
import xyz.jhmapstruct.domain.ShipmentSigma;
import xyz.jhmapstruct.domain.enumeration.OrderSigmaStatus;
import xyz.jhmapstruct.repository.OrderSigmaRepository;
import xyz.jhmapstruct.service.OrderSigmaService;
import xyz.jhmapstruct.service.dto.OrderSigmaDTO;
import xyz.jhmapstruct.service.mapper.OrderSigmaMapper;

/**
 * Integration tests for the {@link OrderSigmaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OrderSigmaResourceIT {

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_PRICE = new BigDecimal(1 - 1);

    private static final OrderSigmaStatus DEFAULT_STATUS = OrderSigmaStatus.PENDING;
    private static final OrderSigmaStatus UPDATED_STATUS = OrderSigmaStatus.SHIPPED;

    private static final String ENTITY_API_URL = "/api/order-sigmas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrderSigmaRepository orderSigmaRepository;

    @Mock
    private OrderSigmaRepository orderSigmaRepositoryMock;

    @Autowired
    private OrderSigmaMapper orderSigmaMapper;

    @Mock
    private OrderSigmaService orderSigmaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderSigmaMockMvc;

    private OrderSigma orderSigma;

    private OrderSigma insertedOrderSigma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderSigma createEntity() {
        return new OrderSigma().orderDate(DEFAULT_ORDER_DATE).totalPrice(DEFAULT_TOTAL_PRICE).status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderSigma createUpdatedEntity() {
        return new OrderSigma().orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        orderSigma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedOrderSigma != null) {
            orderSigmaRepository.delete(insertedOrderSigma);
            insertedOrderSigma = null;
        }
    }

    @Test
    @Transactional
    void createOrderSigma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OrderSigma
        OrderSigmaDTO orderSigmaDTO = orderSigmaMapper.toDto(orderSigma);
        var returnedOrderSigmaDTO = om.readValue(
            restOrderSigmaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderSigmaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OrderSigmaDTO.class
        );

        // Validate the OrderSigma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOrderSigma = orderSigmaMapper.toEntity(returnedOrderSigmaDTO);
        assertOrderSigmaUpdatableFieldsEquals(returnedOrderSigma, getPersistedOrderSigma(returnedOrderSigma));

        insertedOrderSigma = returnedOrderSigma;
    }

    @Test
    @Transactional
    void createOrderSigmaWithExistingId() throws Exception {
        // Create the OrderSigma with an existing ID
        orderSigma.setId(1L);
        OrderSigmaDTO orderSigmaDTO = orderSigmaMapper.toDto(orderSigma);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderSigmaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrderSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderSigma.setOrderDate(null);

        // Create the OrderSigma, which fails.
        OrderSigmaDTO orderSigmaDTO = orderSigmaMapper.toDto(orderSigma);

        restOrderSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderSigmaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderSigma.setTotalPrice(null);

        // Create the OrderSigma, which fails.
        OrderSigmaDTO orderSigmaDTO = orderSigmaMapper.toDto(orderSigma);

        restOrderSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderSigmaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrderSigmas() throws Exception {
        // Initialize the database
        insertedOrderSigma = orderSigmaRepository.saveAndFlush(orderSigma);

        // Get all the orderSigmaList
        restOrderSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderSigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderSigmasWithEagerRelationshipsIsEnabled() throws Exception {
        when(orderSigmaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderSigmaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(orderSigmaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderSigmasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(orderSigmaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderSigmaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(orderSigmaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getOrderSigma() throws Exception {
        // Initialize the database
        insertedOrderSigma = orderSigmaRepository.saveAndFlush(orderSigma);

        // Get the orderSigma
        restOrderSigmaMockMvc
            .perform(get(ENTITY_API_URL_ID, orderSigma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderSigma.getId().intValue()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getOrderSigmasByIdFiltering() throws Exception {
        // Initialize the database
        insertedOrderSigma = orderSigmaRepository.saveAndFlush(orderSigma);

        Long id = orderSigma.getId();

        defaultOrderSigmaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOrderSigmaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOrderSigmaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrderSigmasByOrderDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderSigma = orderSigmaRepository.saveAndFlush(orderSigma);

        // Get all the orderSigmaList where orderDate equals to
        defaultOrderSigmaFiltering("orderDate.equals=" + DEFAULT_ORDER_DATE, "orderDate.equals=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllOrderSigmasByOrderDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderSigma = orderSigmaRepository.saveAndFlush(orderSigma);

        // Get all the orderSigmaList where orderDate in
        defaultOrderSigmaFiltering("orderDate.in=" + DEFAULT_ORDER_DATE + "," + UPDATED_ORDER_DATE, "orderDate.in=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllOrderSigmasByOrderDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderSigma = orderSigmaRepository.saveAndFlush(orderSigma);

        // Get all the orderSigmaList where orderDate is not null
        defaultOrderSigmaFiltering("orderDate.specified=true", "orderDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderSigmasByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderSigma = orderSigmaRepository.saveAndFlush(orderSigma);

        // Get all the orderSigmaList where totalPrice equals to
        defaultOrderSigmaFiltering("totalPrice.equals=" + DEFAULT_TOTAL_PRICE, "totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderSigmasByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderSigma = orderSigmaRepository.saveAndFlush(orderSigma);

        // Get all the orderSigmaList where totalPrice in
        defaultOrderSigmaFiltering(
            "totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE,
            "totalPrice.in=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllOrderSigmasByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderSigma = orderSigmaRepository.saveAndFlush(orderSigma);

        // Get all the orderSigmaList where totalPrice is not null
        defaultOrderSigmaFiltering("totalPrice.specified=true", "totalPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderSigmasByTotalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderSigma = orderSigmaRepository.saveAndFlush(orderSigma);

        // Get all the orderSigmaList where totalPrice is greater than or equal to
        defaultOrderSigmaFiltering(
            "totalPrice.greaterThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.greaterThanOrEqual=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllOrderSigmasByTotalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderSigma = orderSigmaRepository.saveAndFlush(orderSigma);

        // Get all the orderSigmaList where totalPrice is less than or equal to
        defaultOrderSigmaFiltering(
            "totalPrice.lessThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.lessThanOrEqual=" + SMALLER_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllOrderSigmasByTotalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrderSigma = orderSigmaRepository.saveAndFlush(orderSigma);

        // Get all the orderSigmaList where totalPrice is less than
        defaultOrderSigmaFiltering("totalPrice.lessThan=" + UPDATED_TOTAL_PRICE, "totalPrice.lessThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderSigmasByTotalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrderSigma = orderSigmaRepository.saveAndFlush(orderSigma);

        // Get all the orderSigmaList where totalPrice is greater than
        defaultOrderSigmaFiltering("totalPrice.greaterThan=" + SMALLER_TOTAL_PRICE, "totalPrice.greaterThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderSigmasByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderSigma = orderSigmaRepository.saveAndFlush(orderSigma);

        // Get all the orderSigmaList where status equals to
        defaultOrderSigmaFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOrderSigmasByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderSigma = orderSigmaRepository.saveAndFlush(orderSigma);

        // Get all the orderSigmaList where status in
        defaultOrderSigmaFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOrderSigmasByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderSigma = orderSigmaRepository.saveAndFlush(orderSigma);

        // Get all the orderSigmaList where status is not null
        defaultOrderSigmaFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderSigmasByPaymentIsEqualToSomething() throws Exception {
        PaymentSigma payment;
        if (TestUtil.findAll(em, PaymentSigma.class).isEmpty()) {
            orderSigmaRepository.saveAndFlush(orderSigma);
            payment = PaymentSigmaResourceIT.createEntity();
        } else {
            payment = TestUtil.findAll(em, PaymentSigma.class).get(0);
        }
        em.persist(payment);
        em.flush();
        orderSigma.setPayment(payment);
        orderSigmaRepository.saveAndFlush(orderSigma);
        Long paymentId = payment.getId();
        // Get all the orderSigmaList where payment equals to paymentId
        defaultOrderSigmaShouldBeFound("paymentId.equals=" + paymentId);

        // Get all the orderSigmaList where payment equals to (paymentId + 1)
        defaultOrderSigmaShouldNotBeFound("paymentId.equals=" + (paymentId + 1));
    }

    @Test
    @Transactional
    void getAllOrderSigmasByShipmentIsEqualToSomething() throws Exception {
        ShipmentSigma shipment;
        if (TestUtil.findAll(em, ShipmentSigma.class).isEmpty()) {
            orderSigmaRepository.saveAndFlush(orderSigma);
            shipment = ShipmentSigmaResourceIT.createEntity();
        } else {
            shipment = TestUtil.findAll(em, ShipmentSigma.class).get(0);
        }
        em.persist(shipment);
        em.flush();
        orderSigma.setShipment(shipment);
        orderSigmaRepository.saveAndFlush(orderSigma);
        Long shipmentId = shipment.getId();
        // Get all the orderSigmaList where shipment equals to shipmentId
        defaultOrderSigmaShouldBeFound("shipmentId.equals=" + shipmentId);

        // Get all the orderSigmaList where shipment equals to (shipmentId + 1)
        defaultOrderSigmaShouldNotBeFound("shipmentId.equals=" + (shipmentId + 1));
    }

    @Test
    @Transactional
    void getAllOrderSigmasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            orderSigmaRepository.saveAndFlush(orderSigma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        orderSigma.setTenant(tenant);
        orderSigmaRepository.saveAndFlush(orderSigma);
        Long tenantId = tenant.getId();
        // Get all the orderSigmaList where tenant equals to tenantId
        defaultOrderSigmaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the orderSigmaList where tenant equals to (tenantId + 1)
        defaultOrderSigmaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllOrderSigmasByCustomerIsEqualToSomething() throws Exception {
        CustomerSigma customer;
        if (TestUtil.findAll(em, CustomerSigma.class).isEmpty()) {
            orderSigmaRepository.saveAndFlush(orderSigma);
            customer = CustomerSigmaResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, CustomerSigma.class).get(0);
        }
        em.persist(customer);
        em.flush();
        orderSigma.setCustomer(customer);
        orderSigmaRepository.saveAndFlush(orderSigma);
        Long customerId = customer.getId();
        // Get all the orderSigmaList where customer equals to customerId
        defaultOrderSigmaShouldBeFound("customerId.equals=" + customerId);

        // Get all the orderSigmaList where customer equals to (customerId + 1)
        defaultOrderSigmaShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    private void defaultOrderSigmaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOrderSigmaShouldBeFound(shouldBeFound);
        defaultOrderSigmaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderSigmaShouldBeFound(String filter) throws Exception {
        restOrderSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderSigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restOrderSigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderSigmaShouldNotBeFound(String filter) throws Exception {
        restOrderSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderSigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrderSigma() throws Exception {
        // Get the orderSigma
        restOrderSigmaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderSigma() throws Exception {
        // Initialize the database
        insertedOrderSigma = orderSigmaRepository.saveAndFlush(orderSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderSigma
        OrderSigma updatedOrderSigma = orderSigmaRepository.findById(orderSigma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrderSigma are not directly saved in db
        em.detach(updatedOrderSigma);
        updatedOrderSigma.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);
        OrderSigmaDTO orderSigmaDTO = orderSigmaMapper.toDto(updatedOrderSigma);

        restOrderSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderSigmaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderSigmaDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrderSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrderSigmaToMatchAllProperties(updatedOrderSigma);
    }

    @Test
    @Transactional
    void putNonExistingOrderSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderSigma.setId(longCount.incrementAndGet());

        // Create the OrderSigma
        OrderSigmaDTO orderSigmaDTO = orderSigmaMapper.toDto(orderSigma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderSigmaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderSigma.setId(longCount.incrementAndGet());

        // Create the OrderSigma
        OrderSigmaDTO orderSigmaDTO = orderSigmaMapper.toDto(orderSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderSigma.setId(longCount.incrementAndGet());

        // Create the OrderSigma
        OrderSigmaDTO orderSigmaDTO = orderSigmaMapper.toDto(orderSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderSigmaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderSigmaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderSigmaWithPatch() throws Exception {
        // Initialize the database
        insertedOrderSigma = orderSigmaRepository.saveAndFlush(orderSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderSigma using partial update
        OrderSigma partialUpdatedOrderSigma = new OrderSigma();
        partialUpdatedOrderSigma.setId(orderSigma.getId());

        partialUpdatedOrderSigma.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE);

        restOrderSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderSigma))
            )
            .andExpect(status().isOk());

        // Validate the OrderSigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderSigmaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOrderSigma, orderSigma),
            getPersistedOrderSigma(orderSigma)
        );
    }

    @Test
    @Transactional
    void fullUpdateOrderSigmaWithPatch() throws Exception {
        // Initialize the database
        insertedOrderSigma = orderSigmaRepository.saveAndFlush(orderSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderSigma using partial update
        OrderSigma partialUpdatedOrderSigma = new OrderSigma();
        partialUpdatedOrderSigma.setId(orderSigma.getId());

        partialUpdatedOrderSigma.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);

        restOrderSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderSigma))
            )
            .andExpect(status().isOk());

        // Validate the OrderSigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderSigmaUpdatableFieldsEquals(partialUpdatedOrderSigma, getPersistedOrderSigma(partialUpdatedOrderSigma));
    }

    @Test
    @Transactional
    void patchNonExistingOrderSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderSigma.setId(longCount.incrementAndGet());

        // Create the OrderSigma
        OrderSigmaDTO orderSigmaDTO = orderSigmaMapper.toDto(orderSigma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderSigmaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderSigma.setId(longCount.incrementAndGet());

        // Create the OrderSigma
        OrderSigmaDTO orderSigmaDTO = orderSigmaMapper.toDto(orderSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderSigma.setId(longCount.incrementAndGet());

        // Create the OrderSigma
        OrderSigmaDTO orderSigmaDTO = orderSigmaMapper.toDto(orderSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderSigmaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(orderSigmaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderSigma() throws Exception {
        // Initialize the database
        insertedOrderSigma = orderSigmaRepository.saveAndFlush(orderSigma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the orderSigma
        restOrderSigmaMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderSigma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return orderSigmaRepository.count();
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

    protected OrderSigma getPersistedOrderSigma(OrderSigma orderSigma) {
        return orderSigmaRepository.findById(orderSigma.getId()).orElseThrow();
    }

    protected void assertPersistedOrderSigmaToMatchAllProperties(OrderSigma expectedOrderSigma) {
        assertOrderSigmaAllPropertiesEquals(expectedOrderSigma, getPersistedOrderSigma(expectedOrderSigma));
    }

    protected void assertPersistedOrderSigmaToMatchUpdatableProperties(OrderSigma expectedOrderSigma) {
        assertOrderSigmaAllUpdatablePropertiesEquals(expectedOrderSigma, getPersistedOrderSigma(expectedOrderSigma));
    }
}
