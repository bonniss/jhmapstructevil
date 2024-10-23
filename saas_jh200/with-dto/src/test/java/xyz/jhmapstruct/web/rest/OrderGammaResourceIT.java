package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.OrderGammaAsserts.*;
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
import xyz.jhmapstruct.domain.CustomerGamma;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.OrderGamma;
import xyz.jhmapstruct.domain.PaymentGamma;
import xyz.jhmapstruct.domain.ShipmentGamma;
import xyz.jhmapstruct.domain.enumeration.OrderGammaStatus;
import xyz.jhmapstruct.repository.OrderGammaRepository;
import xyz.jhmapstruct.service.OrderGammaService;
import xyz.jhmapstruct.service.dto.OrderGammaDTO;
import xyz.jhmapstruct.service.mapper.OrderGammaMapper;

/**
 * Integration tests for the {@link OrderGammaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OrderGammaResourceIT {

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_PRICE = new BigDecimal(1 - 1);

    private static final OrderGammaStatus DEFAULT_STATUS = OrderGammaStatus.PENDING;
    private static final OrderGammaStatus UPDATED_STATUS = OrderGammaStatus.SHIPPED;

    private static final String ENTITY_API_URL = "/api/order-gammas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrderGammaRepository orderGammaRepository;

    @Mock
    private OrderGammaRepository orderGammaRepositoryMock;

    @Autowired
    private OrderGammaMapper orderGammaMapper;

    @Mock
    private OrderGammaService orderGammaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderGammaMockMvc;

    private OrderGamma orderGamma;

    private OrderGamma insertedOrderGamma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderGamma createEntity() {
        return new OrderGamma().orderDate(DEFAULT_ORDER_DATE).totalPrice(DEFAULT_TOTAL_PRICE).status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderGamma createUpdatedEntity() {
        return new OrderGamma().orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        orderGamma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedOrderGamma != null) {
            orderGammaRepository.delete(insertedOrderGamma);
            insertedOrderGamma = null;
        }
    }

    @Test
    @Transactional
    void createOrderGamma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OrderGamma
        OrderGammaDTO orderGammaDTO = orderGammaMapper.toDto(orderGamma);
        var returnedOrderGammaDTO = om.readValue(
            restOrderGammaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderGammaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OrderGammaDTO.class
        );

        // Validate the OrderGamma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOrderGamma = orderGammaMapper.toEntity(returnedOrderGammaDTO);
        assertOrderGammaUpdatableFieldsEquals(returnedOrderGamma, getPersistedOrderGamma(returnedOrderGamma));

        insertedOrderGamma = returnedOrderGamma;
    }

    @Test
    @Transactional
    void createOrderGammaWithExistingId() throws Exception {
        // Create the OrderGamma with an existing ID
        orderGamma.setId(1L);
        OrderGammaDTO orderGammaDTO = orderGammaMapper.toDto(orderGamma);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderGammaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrderGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderGamma.setOrderDate(null);

        // Create the OrderGamma, which fails.
        OrderGammaDTO orderGammaDTO = orderGammaMapper.toDto(orderGamma);

        restOrderGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderGammaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderGamma.setTotalPrice(null);

        // Create the OrderGamma, which fails.
        OrderGammaDTO orderGammaDTO = orderGammaMapper.toDto(orderGamma);

        restOrderGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderGammaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrderGammas() throws Exception {
        // Initialize the database
        insertedOrderGamma = orderGammaRepository.saveAndFlush(orderGamma);

        // Get all the orderGammaList
        restOrderGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderGammasWithEagerRelationshipsIsEnabled() throws Exception {
        when(orderGammaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderGammaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(orderGammaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderGammasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(orderGammaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderGammaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(orderGammaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getOrderGamma() throws Exception {
        // Initialize the database
        insertedOrderGamma = orderGammaRepository.saveAndFlush(orderGamma);

        // Get the orderGamma
        restOrderGammaMockMvc
            .perform(get(ENTITY_API_URL_ID, orderGamma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderGamma.getId().intValue()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getOrderGammasByIdFiltering() throws Exception {
        // Initialize the database
        insertedOrderGamma = orderGammaRepository.saveAndFlush(orderGamma);

        Long id = orderGamma.getId();

        defaultOrderGammaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOrderGammaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOrderGammaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrderGammasByOrderDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderGamma = orderGammaRepository.saveAndFlush(orderGamma);

        // Get all the orderGammaList where orderDate equals to
        defaultOrderGammaFiltering("orderDate.equals=" + DEFAULT_ORDER_DATE, "orderDate.equals=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllOrderGammasByOrderDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderGamma = orderGammaRepository.saveAndFlush(orderGamma);

        // Get all the orderGammaList where orderDate in
        defaultOrderGammaFiltering("orderDate.in=" + DEFAULT_ORDER_DATE + "," + UPDATED_ORDER_DATE, "orderDate.in=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllOrderGammasByOrderDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderGamma = orderGammaRepository.saveAndFlush(orderGamma);

        // Get all the orderGammaList where orderDate is not null
        defaultOrderGammaFiltering("orderDate.specified=true", "orderDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderGammasByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderGamma = orderGammaRepository.saveAndFlush(orderGamma);

        // Get all the orderGammaList where totalPrice equals to
        defaultOrderGammaFiltering("totalPrice.equals=" + DEFAULT_TOTAL_PRICE, "totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderGammasByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderGamma = orderGammaRepository.saveAndFlush(orderGamma);

        // Get all the orderGammaList where totalPrice in
        defaultOrderGammaFiltering(
            "totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE,
            "totalPrice.in=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllOrderGammasByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderGamma = orderGammaRepository.saveAndFlush(orderGamma);

        // Get all the orderGammaList where totalPrice is not null
        defaultOrderGammaFiltering("totalPrice.specified=true", "totalPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderGammasByTotalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderGamma = orderGammaRepository.saveAndFlush(orderGamma);

        // Get all the orderGammaList where totalPrice is greater than or equal to
        defaultOrderGammaFiltering(
            "totalPrice.greaterThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.greaterThanOrEqual=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllOrderGammasByTotalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderGamma = orderGammaRepository.saveAndFlush(orderGamma);

        // Get all the orderGammaList where totalPrice is less than or equal to
        defaultOrderGammaFiltering(
            "totalPrice.lessThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.lessThanOrEqual=" + SMALLER_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllOrderGammasByTotalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrderGamma = orderGammaRepository.saveAndFlush(orderGamma);

        // Get all the orderGammaList where totalPrice is less than
        defaultOrderGammaFiltering("totalPrice.lessThan=" + UPDATED_TOTAL_PRICE, "totalPrice.lessThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderGammasByTotalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrderGamma = orderGammaRepository.saveAndFlush(orderGamma);

        // Get all the orderGammaList where totalPrice is greater than
        defaultOrderGammaFiltering("totalPrice.greaterThan=" + SMALLER_TOTAL_PRICE, "totalPrice.greaterThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderGammasByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderGamma = orderGammaRepository.saveAndFlush(orderGamma);

        // Get all the orderGammaList where status equals to
        defaultOrderGammaFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOrderGammasByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderGamma = orderGammaRepository.saveAndFlush(orderGamma);

        // Get all the orderGammaList where status in
        defaultOrderGammaFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOrderGammasByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderGamma = orderGammaRepository.saveAndFlush(orderGamma);

        // Get all the orderGammaList where status is not null
        defaultOrderGammaFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderGammasByPaymentIsEqualToSomething() throws Exception {
        PaymentGamma payment;
        if (TestUtil.findAll(em, PaymentGamma.class).isEmpty()) {
            orderGammaRepository.saveAndFlush(orderGamma);
            payment = PaymentGammaResourceIT.createEntity();
        } else {
            payment = TestUtil.findAll(em, PaymentGamma.class).get(0);
        }
        em.persist(payment);
        em.flush();
        orderGamma.setPayment(payment);
        orderGammaRepository.saveAndFlush(orderGamma);
        Long paymentId = payment.getId();
        // Get all the orderGammaList where payment equals to paymentId
        defaultOrderGammaShouldBeFound("paymentId.equals=" + paymentId);

        // Get all the orderGammaList where payment equals to (paymentId + 1)
        defaultOrderGammaShouldNotBeFound("paymentId.equals=" + (paymentId + 1));
    }

    @Test
    @Transactional
    void getAllOrderGammasByShipmentIsEqualToSomething() throws Exception {
        ShipmentGamma shipment;
        if (TestUtil.findAll(em, ShipmentGamma.class).isEmpty()) {
            orderGammaRepository.saveAndFlush(orderGamma);
            shipment = ShipmentGammaResourceIT.createEntity();
        } else {
            shipment = TestUtil.findAll(em, ShipmentGamma.class).get(0);
        }
        em.persist(shipment);
        em.flush();
        orderGamma.setShipment(shipment);
        orderGammaRepository.saveAndFlush(orderGamma);
        Long shipmentId = shipment.getId();
        // Get all the orderGammaList where shipment equals to shipmentId
        defaultOrderGammaShouldBeFound("shipmentId.equals=" + shipmentId);

        // Get all the orderGammaList where shipment equals to (shipmentId + 1)
        defaultOrderGammaShouldNotBeFound("shipmentId.equals=" + (shipmentId + 1));
    }

    @Test
    @Transactional
    void getAllOrderGammasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            orderGammaRepository.saveAndFlush(orderGamma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        orderGamma.setTenant(tenant);
        orderGammaRepository.saveAndFlush(orderGamma);
        Long tenantId = tenant.getId();
        // Get all the orderGammaList where tenant equals to tenantId
        defaultOrderGammaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the orderGammaList where tenant equals to (tenantId + 1)
        defaultOrderGammaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllOrderGammasByCustomerIsEqualToSomething() throws Exception {
        CustomerGamma customer;
        if (TestUtil.findAll(em, CustomerGamma.class).isEmpty()) {
            orderGammaRepository.saveAndFlush(orderGamma);
            customer = CustomerGammaResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, CustomerGamma.class).get(0);
        }
        em.persist(customer);
        em.flush();
        orderGamma.setCustomer(customer);
        orderGammaRepository.saveAndFlush(orderGamma);
        Long customerId = customer.getId();
        // Get all the orderGammaList where customer equals to customerId
        defaultOrderGammaShouldBeFound("customerId.equals=" + customerId);

        // Get all the orderGammaList where customer equals to (customerId + 1)
        defaultOrderGammaShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    private void defaultOrderGammaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOrderGammaShouldBeFound(shouldBeFound);
        defaultOrderGammaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderGammaShouldBeFound(String filter) throws Exception {
        restOrderGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restOrderGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderGammaShouldNotBeFound(String filter) throws Exception {
        restOrderGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrderGamma() throws Exception {
        // Get the orderGamma
        restOrderGammaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderGamma() throws Exception {
        // Initialize the database
        insertedOrderGamma = orderGammaRepository.saveAndFlush(orderGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderGamma
        OrderGamma updatedOrderGamma = orderGammaRepository.findById(orderGamma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrderGamma are not directly saved in db
        em.detach(updatedOrderGamma);
        updatedOrderGamma.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);
        OrderGammaDTO orderGammaDTO = orderGammaMapper.toDto(updatedOrderGamma);

        restOrderGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderGammaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderGammaDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrderGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrderGammaToMatchAllProperties(updatedOrderGamma);
    }

    @Test
    @Transactional
    void putNonExistingOrderGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderGamma.setId(longCount.incrementAndGet());

        // Create the OrderGamma
        OrderGammaDTO orderGammaDTO = orderGammaMapper.toDto(orderGamma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderGammaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderGamma.setId(longCount.incrementAndGet());

        // Create the OrderGamma
        OrderGammaDTO orderGammaDTO = orderGammaMapper.toDto(orderGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderGamma.setId(longCount.incrementAndGet());

        // Create the OrderGamma
        OrderGammaDTO orderGammaDTO = orderGammaMapper.toDto(orderGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderGammaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderGammaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderGammaWithPatch() throws Exception {
        // Initialize the database
        insertedOrderGamma = orderGammaRepository.saveAndFlush(orderGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderGamma using partial update
        OrderGamma partialUpdatedOrderGamma = new OrderGamma();
        partialUpdatedOrderGamma.setId(orderGamma.getId());

        partialUpdatedOrderGamma.totalPrice(UPDATED_TOTAL_PRICE);

        restOrderGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderGamma))
            )
            .andExpect(status().isOk());

        // Validate the OrderGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderGammaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOrderGamma, orderGamma),
            getPersistedOrderGamma(orderGamma)
        );
    }

    @Test
    @Transactional
    void fullUpdateOrderGammaWithPatch() throws Exception {
        // Initialize the database
        insertedOrderGamma = orderGammaRepository.saveAndFlush(orderGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderGamma using partial update
        OrderGamma partialUpdatedOrderGamma = new OrderGamma();
        partialUpdatedOrderGamma.setId(orderGamma.getId());

        partialUpdatedOrderGamma.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);

        restOrderGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderGamma))
            )
            .andExpect(status().isOk());

        // Validate the OrderGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderGammaUpdatableFieldsEquals(partialUpdatedOrderGamma, getPersistedOrderGamma(partialUpdatedOrderGamma));
    }

    @Test
    @Transactional
    void patchNonExistingOrderGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderGamma.setId(longCount.incrementAndGet());

        // Create the OrderGamma
        OrderGammaDTO orderGammaDTO = orderGammaMapper.toDto(orderGamma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderGammaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderGamma.setId(longCount.incrementAndGet());

        // Create the OrderGamma
        OrderGammaDTO orderGammaDTO = orderGammaMapper.toDto(orderGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderGamma.setId(longCount.incrementAndGet());

        // Create the OrderGamma
        OrderGammaDTO orderGammaDTO = orderGammaMapper.toDto(orderGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderGammaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(orderGammaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderGamma() throws Exception {
        // Initialize the database
        insertedOrderGamma = orderGammaRepository.saveAndFlush(orderGamma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the orderGamma
        restOrderGammaMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderGamma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return orderGammaRepository.count();
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

    protected OrderGamma getPersistedOrderGamma(OrderGamma orderGamma) {
        return orderGammaRepository.findById(orderGamma.getId()).orElseThrow();
    }

    protected void assertPersistedOrderGammaToMatchAllProperties(OrderGamma expectedOrderGamma) {
        assertOrderGammaAllPropertiesEquals(expectedOrderGamma, getPersistedOrderGamma(expectedOrderGamma));
    }

    protected void assertPersistedOrderGammaToMatchUpdatableProperties(OrderGamma expectedOrderGamma) {
        assertOrderGammaAllUpdatablePropertiesEquals(expectedOrderGamma, getPersistedOrderGamma(expectedOrderGamma));
    }
}
