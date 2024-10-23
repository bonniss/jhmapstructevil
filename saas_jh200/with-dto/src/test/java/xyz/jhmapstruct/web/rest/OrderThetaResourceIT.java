package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.OrderThetaAsserts.*;
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
import xyz.jhmapstruct.domain.CustomerTheta;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.OrderTheta;
import xyz.jhmapstruct.domain.PaymentTheta;
import xyz.jhmapstruct.domain.ShipmentTheta;
import xyz.jhmapstruct.domain.enumeration.OrderThetaStatus;
import xyz.jhmapstruct.repository.OrderThetaRepository;
import xyz.jhmapstruct.service.OrderThetaService;
import xyz.jhmapstruct.service.dto.OrderThetaDTO;
import xyz.jhmapstruct.service.mapper.OrderThetaMapper;

/**
 * Integration tests for the {@link OrderThetaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OrderThetaResourceIT {

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_PRICE = new BigDecimal(1 - 1);

    private static final OrderThetaStatus DEFAULT_STATUS = OrderThetaStatus.PENDING;
    private static final OrderThetaStatus UPDATED_STATUS = OrderThetaStatus.SHIPPED;

    private static final String ENTITY_API_URL = "/api/order-thetas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrderThetaRepository orderThetaRepository;

    @Mock
    private OrderThetaRepository orderThetaRepositoryMock;

    @Autowired
    private OrderThetaMapper orderThetaMapper;

    @Mock
    private OrderThetaService orderThetaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderThetaMockMvc;

    private OrderTheta orderTheta;

    private OrderTheta insertedOrderTheta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderTheta createEntity() {
        return new OrderTheta().orderDate(DEFAULT_ORDER_DATE).totalPrice(DEFAULT_TOTAL_PRICE).status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderTheta createUpdatedEntity() {
        return new OrderTheta().orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        orderTheta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedOrderTheta != null) {
            orderThetaRepository.delete(insertedOrderTheta);
            insertedOrderTheta = null;
        }
    }

    @Test
    @Transactional
    void createOrderTheta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OrderTheta
        OrderThetaDTO orderThetaDTO = orderThetaMapper.toDto(orderTheta);
        var returnedOrderThetaDTO = om.readValue(
            restOrderThetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderThetaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OrderThetaDTO.class
        );

        // Validate the OrderTheta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOrderTheta = orderThetaMapper.toEntity(returnedOrderThetaDTO);
        assertOrderThetaUpdatableFieldsEquals(returnedOrderTheta, getPersistedOrderTheta(returnedOrderTheta));

        insertedOrderTheta = returnedOrderTheta;
    }

    @Test
    @Transactional
    void createOrderThetaWithExistingId() throws Exception {
        // Create the OrderTheta with an existing ID
        orderTheta.setId(1L);
        OrderThetaDTO orderThetaDTO = orderThetaMapper.toDto(orderTheta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderThetaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrderTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderTheta.setOrderDate(null);

        // Create the OrderTheta, which fails.
        OrderThetaDTO orderThetaDTO = orderThetaMapper.toDto(orderTheta);

        restOrderThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderThetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderTheta.setTotalPrice(null);

        // Create the OrderTheta, which fails.
        OrderThetaDTO orderThetaDTO = orderThetaMapper.toDto(orderTheta);

        restOrderThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderThetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrderThetas() throws Exception {
        // Initialize the database
        insertedOrderTheta = orderThetaRepository.saveAndFlush(orderTheta);

        // Get all the orderThetaList
        restOrderThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderThetasWithEagerRelationshipsIsEnabled() throws Exception {
        when(orderThetaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderThetaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(orderThetaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderThetasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(orderThetaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderThetaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(orderThetaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getOrderTheta() throws Exception {
        // Initialize the database
        insertedOrderTheta = orderThetaRepository.saveAndFlush(orderTheta);

        // Get the orderTheta
        restOrderThetaMockMvc
            .perform(get(ENTITY_API_URL_ID, orderTheta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderTheta.getId().intValue()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getOrderThetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedOrderTheta = orderThetaRepository.saveAndFlush(orderTheta);

        Long id = orderTheta.getId();

        defaultOrderThetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOrderThetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOrderThetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrderThetasByOrderDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderTheta = orderThetaRepository.saveAndFlush(orderTheta);

        // Get all the orderThetaList where orderDate equals to
        defaultOrderThetaFiltering("orderDate.equals=" + DEFAULT_ORDER_DATE, "orderDate.equals=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllOrderThetasByOrderDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderTheta = orderThetaRepository.saveAndFlush(orderTheta);

        // Get all the orderThetaList where orderDate in
        defaultOrderThetaFiltering("orderDate.in=" + DEFAULT_ORDER_DATE + "," + UPDATED_ORDER_DATE, "orderDate.in=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllOrderThetasByOrderDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderTheta = orderThetaRepository.saveAndFlush(orderTheta);

        // Get all the orderThetaList where orderDate is not null
        defaultOrderThetaFiltering("orderDate.specified=true", "orderDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderThetasByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderTheta = orderThetaRepository.saveAndFlush(orderTheta);

        // Get all the orderThetaList where totalPrice equals to
        defaultOrderThetaFiltering("totalPrice.equals=" + DEFAULT_TOTAL_PRICE, "totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderThetasByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderTheta = orderThetaRepository.saveAndFlush(orderTheta);

        // Get all the orderThetaList where totalPrice in
        defaultOrderThetaFiltering(
            "totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE,
            "totalPrice.in=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllOrderThetasByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderTheta = orderThetaRepository.saveAndFlush(orderTheta);

        // Get all the orderThetaList where totalPrice is not null
        defaultOrderThetaFiltering("totalPrice.specified=true", "totalPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderThetasByTotalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderTheta = orderThetaRepository.saveAndFlush(orderTheta);

        // Get all the orderThetaList where totalPrice is greater than or equal to
        defaultOrderThetaFiltering(
            "totalPrice.greaterThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.greaterThanOrEqual=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllOrderThetasByTotalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderTheta = orderThetaRepository.saveAndFlush(orderTheta);

        // Get all the orderThetaList where totalPrice is less than or equal to
        defaultOrderThetaFiltering(
            "totalPrice.lessThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.lessThanOrEqual=" + SMALLER_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllOrderThetasByTotalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrderTheta = orderThetaRepository.saveAndFlush(orderTheta);

        // Get all the orderThetaList where totalPrice is less than
        defaultOrderThetaFiltering("totalPrice.lessThan=" + UPDATED_TOTAL_PRICE, "totalPrice.lessThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderThetasByTotalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrderTheta = orderThetaRepository.saveAndFlush(orderTheta);

        // Get all the orderThetaList where totalPrice is greater than
        defaultOrderThetaFiltering("totalPrice.greaterThan=" + SMALLER_TOTAL_PRICE, "totalPrice.greaterThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderThetasByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderTheta = orderThetaRepository.saveAndFlush(orderTheta);

        // Get all the orderThetaList where status equals to
        defaultOrderThetaFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOrderThetasByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderTheta = orderThetaRepository.saveAndFlush(orderTheta);

        // Get all the orderThetaList where status in
        defaultOrderThetaFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOrderThetasByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderTheta = orderThetaRepository.saveAndFlush(orderTheta);

        // Get all the orderThetaList where status is not null
        defaultOrderThetaFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderThetasByPaymentIsEqualToSomething() throws Exception {
        PaymentTheta payment;
        if (TestUtil.findAll(em, PaymentTheta.class).isEmpty()) {
            orderThetaRepository.saveAndFlush(orderTheta);
            payment = PaymentThetaResourceIT.createEntity();
        } else {
            payment = TestUtil.findAll(em, PaymentTheta.class).get(0);
        }
        em.persist(payment);
        em.flush();
        orderTheta.setPayment(payment);
        orderThetaRepository.saveAndFlush(orderTheta);
        Long paymentId = payment.getId();
        // Get all the orderThetaList where payment equals to paymentId
        defaultOrderThetaShouldBeFound("paymentId.equals=" + paymentId);

        // Get all the orderThetaList where payment equals to (paymentId + 1)
        defaultOrderThetaShouldNotBeFound("paymentId.equals=" + (paymentId + 1));
    }

    @Test
    @Transactional
    void getAllOrderThetasByShipmentIsEqualToSomething() throws Exception {
        ShipmentTheta shipment;
        if (TestUtil.findAll(em, ShipmentTheta.class).isEmpty()) {
            orderThetaRepository.saveAndFlush(orderTheta);
            shipment = ShipmentThetaResourceIT.createEntity();
        } else {
            shipment = TestUtil.findAll(em, ShipmentTheta.class).get(0);
        }
        em.persist(shipment);
        em.flush();
        orderTheta.setShipment(shipment);
        orderThetaRepository.saveAndFlush(orderTheta);
        Long shipmentId = shipment.getId();
        // Get all the orderThetaList where shipment equals to shipmentId
        defaultOrderThetaShouldBeFound("shipmentId.equals=" + shipmentId);

        // Get all the orderThetaList where shipment equals to (shipmentId + 1)
        defaultOrderThetaShouldNotBeFound("shipmentId.equals=" + (shipmentId + 1));
    }

    @Test
    @Transactional
    void getAllOrderThetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            orderThetaRepository.saveAndFlush(orderTheta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        orderTheta.setTenant(tenant);
        orderThetaRepository.saveAndFlush(orderTheta);
        Long tenantId = tenant.getId();
        // Get all the orderThetaList where tenant equals to tenantId
        defaultOrderThetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the orderThetaList where tenant equals to (tenantId + 1)
        defaultOrderThetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllOrderThetasByCustomerIsEqualToSomething() throws Exception {
        CustomerTheta customer;
        if (TestUtil.findAll(em, CustomerTheta.class).isEmpty()) {
            orderThetaRepository.saveAndFlush(orderTheta);
            customer = CustomerThetaResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, CustomerTheta.class).get(0);
        }
        em.persist(customer);
        em.flush();
        orderTheta.setCustomer(customer);
        orderThetaRepository.saveAndFlush(orderTheta);
        Long customerId = customer.getId();
        // Get all the orderThetaList where customer equals to customerId
        defaultOrderThetaShouldBeFound("customerId.equals=" + customerId);

        // Get all the orderThetaList where customer equals to (customerId + 1)
        defaultOrderThetaShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    private void defaultOrderThetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOrderThetaShouldBeFound(shouldBeFound);
        defaultOrderThetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderThetaShouldBeFound(String filter) throws Exception {
        restOrderThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restOrderThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderThetaShouldNotBeFound(String filter) throws Exception {
        restOrderThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrderTheta() throws Exception {
        // Get the orderTheta
        restOrderThetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderTheta() throws Exception {
        // Initialize the database
        insertedOrderTheta = orderThetaRepository.saveAndFlush(orderTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderTheta
        OrderTheta updatedOrderTheta = orderThetaRepository.findById(orderTheta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrderTheta are not directly saved in db
        em.detach(updatedOrderTheta);
        updatedOrderTheta.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);
        OrderThetaDTO orderThetaDTO = orderThetaMapper.toDto(updatedOrderTheta);

        restOrderThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderThetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderThetaDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrderTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrderThetaToMatchAllProperties(updatedOrderTheta);
    }

    @Test
    @Transactional
    void putNonExistingOrderTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderTheta.setId(longCount.incrementAndGet());

        // Create the OrderTheta
        OrderThetaDTO orderThetaDTO = orderThetaMapper.toDto(orderTheta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderThetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderTheta.setId(longCount.incrementAndGet());

        // Create the OrderTheta
        OrderThetaDTO orderThetaDTO = orderThetaMapper.toDto(orderTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderTheta.setId(longCount.incrementAndGet());

        // Create the OrderTheta
        OrderThetaDTO orderThetaDTO = orderThetaMapper.toDto(orderTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderThetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderThetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderThetaWithPatch() throws Exception {
        // Initialize the database
        insertedOrderTheta = orderThetaRepository.saveAndFlush(orderTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderTheta using partial update
        OrderTheta partialUpdatedOrderTheta = new OrderTheta();
        partialUpdatedOrderTheta.setId(orderTheta.getId());

        restOrderThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderTheta))
            )
            .andExpect(status().isOk());

        // Validate the OrderTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderThetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOrderTheta, orderTheta),
            getPersistedOrderTheta(orderTheta)
        );
    }

    @Test
    @Transactional
    void fullUpdateOrderThetaWithPatch() throws Exception {
        // Initialize the database
        insertedOrderTheta = orderThetaRepository.saveAndFlush(orderTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderTheta using partial update
        OrderTheta partialUpdatedOrderTheta = new OrderTheta();
        partialUpdatedOrderTheta.setId(orderTheta.getId());

        partialUpdatedOrderTheta.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);

        restOrderThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderTheta))
            )
            .andExpect(status().isOk());

        // Validate the OrderTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderThetaUpdatableFieldsEquals(partialUpdatedOrderTheta, getPersistedOrderTheta(partialUpdatedOrderTheta));
    }

    @Test
    @Transactional
    void patchNonExistingOrderTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderTheta.setId(longCount.incrementAndGet());

        // Create the OrderTheta
        OrderThetaDTO orderThetaDTO = orderThetaMapper.toDto(orderTheta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderThetaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderTheta.setId(longCount.incrementAndGet());

        // Create the OrderTheta
        OrderThetaDTO orderThetaDTO = orderThetaMapper.toDto(orderTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderTheta.setId(longCount.incrementAndGet());

        // Create the OrderTheta
        OrderThetaDTO orderThetaDTO = orderThetaMapper.toDto(orderTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderThetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(orderThetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderTheta() throws Exception {
        // Initialize the database
        insertedOrderTheta = orderThetaRepository.saveAndFlush(orderTheta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the orderTheta
        restOrderThetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderTheta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return orderThetaRepository.count();
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

    protected OrderTheta getPersistedOrderTheta(OrderTheta orderTheta) {
        return orderThetaRepository.findById(orderTheta.getId()).orElseThrow();
    }

    protected void assertPersistedOrderThetaToMatchAllProperties(OrderTheta expectedOrderTheta) {
        assertOrderThetaAllPropertiesEquals(expectedOrderTheta, getPersistedOrderTheta(expectedOrderTheta));
    }

    protected void assertPersistedOrderThetaToMatchUpdatableProperties(OrderTheta expectedOrderTheta) {
        assertOrderThetaAllUpdatablePropertiesEquals(expectedOrderTheta, getPersistedOrderTheta(expectedOrderTheta));
    }
}
