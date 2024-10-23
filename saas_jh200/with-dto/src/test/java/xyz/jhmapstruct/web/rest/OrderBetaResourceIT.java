package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.OrderBetaAsserts.*;
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
import xyz.jhmapstruct.domain.CustomerBeta;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.OrderBeta;
import xyz.jhmapstruct.domain.PaymentBeta;
import xyz.jhmapstruct.domain.ShipmentBeta;
import xyz.jhmapstruct.domain.enumeration.OrderBetaStatus;
import xyz.jhmapstruct.repository.OrderBetaRepository;
import xyz.jhmapstruct.service.OrderBetaService;
import xyz.jhmapstruct.service.dto.OrderBetaDTO;
import xyz.jhmapstruct.service.mapper.OrderBetaMapper;

/**
 * Integration tests for the {@link OrderBetaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OrderBetaResourceIT {

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_PRICE = new BigDecimal(1 - 1);

    private static final OrderBetaStatus DEFAULT_STATUS = OrderBetaStatus.PENDING;
    private static final OrderBetaStatus UPDATED_STATUS = OrderBetaStatus.SHIPPED;

    private static final String ENTITY_API_URL = "/api/order-betas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrderBetaRepository orderBetaRepository;

    @Mock
    private OrderBetaRepository orderBetaRepositoryMock;

    @Autowired
    private OrderBetaMapper orderBetaMapper;

    @Mock
    private OrderBetaService orderBetaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderBetaMockMvc;

    private OrderBeta orderBeta;

    private OrderBeta insertedOrderBeta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderBeta createEntity() {
        return new OrderBeta().orderDate(DEFAULT_ORDER_DATE).totalPrice(DEFAULT_TOTAL_PRICE).status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderBeta createUpdatedEntity() {
        return new OrderBeta().orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        orderBeta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedOrderBeta != null) {
            orderBetaRepository.delete(insertedOrderBeta);
            insertedOrderBeta = null;
        }
    }

    @Test
    @Transactional
    void createOrderBeta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OrderBeta
        OrderBetaDTO orderBetaDTO = orderBetaMapper.toDto(orderBeta);
        var returnedOrderBetaDTO = om.readValue(
            restOrderBetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderBetaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OrderBetaDTO.class
        );

        // Validate the OrderBeta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOrderBeta = orderBetaMapper.toEntity(returnedOrderBetaDTO);
        assertOrderBetaUpdatableFieldsEquals(returnedOrderBeta, getPersistedOrderBeta(returnedOrderBeta));

        insertedOrderBeta = returnedOrderBeta;
    }

    @Test
    @Transactional
    void createOrderBetaWithExistingId() throws Exception {
        // Create the OrderBeta with an existing ID
        orderBeta.setId(1L);
        OrderBetaDTO orderBetaDTO = orderBetaMapper.toDto(orderBeta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderBetaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrderBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderBeta.setOrderDate(null);

        // Create the OrderBeta, which fails.
        OrderBetaDTO orderBetaDTO = orderBetaMapper.toDto(orderBeta);

        restOrderBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderBetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderBeta.setTotalPrice(null);

        // Create the OrderBeta, which fails.
        OrderBetaDTO orderBetaDTO = orderBetaMapper.toDto(orderBeta);

        restOrderBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderBetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrderBetas() throws Exception {
        // Initialize the database
        insertedOrderBeta = orderBetaRepository.saveAndFlush(orderBeta);

        // Get all the orderBetaList
        restOrderBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderBetasWithEagerRelationshipsIsEnabled() throws Exception {
        when(orderBetaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderBetaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(orderBetaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderBetasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(orderBetaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderBetaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(orderBetaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getOrderBeta() throws Exception {
        // Initialize the database
        insertedOrderBeta = orderBetaRepository.saveAndFlush(orderBeta);

        // Get the orderBeta
        restOrderBetaMockMvc
            .perform(get(ENTITY_API_URL_ID, orderBeta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderBeta.getId().intValue()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getOrderBetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedOrderBeta = orderBetaRepository.saveAndFlush(orderBeta);

        Long id = orderBeta.getId();

        defaultOrderBetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOrderBetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOrderBetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrderBetasByOrderDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderBeta = orderBetaRepository.saveAndFlush(orderBeta);

        // Get all the orderBetaList where orderDate equals to
        defaultOrderBetaFiltering("orderDate.equals=" + DEFAULT_ORDER_DATE, "orderDate.equals=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllOrderBetasByOrderDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderBeta = orderBetaRepository.saveAndFlush(orderBeta);

        // Get all the orderBetaList where orderDate in
        defaultOrderBetaFiltering("orderDate.in=" + DEFAULT_ORDER_DATE + "," + UPDATED_ORDER_DATE, "orderDate.in=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllOrderBetasByOrderDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderBeta = orderBetaRepository.saveAndFlush(orderBeta);

        // Get all the orderBetaList where orderDate is not null
        defaultOrderBetaFiltering("orderDate.specified=true", "orderDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderBetasByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderBeta = orderBetaRepository.saveAndFlush(orderBeta);

        // Get all the orderBetaList where totalPrice equals to
        defaultOrderBetaFiltering("totalPrice.equals=" + DEFAULT_TOTAL_PRICE, "totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderBetasByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderBeta = orderBetaRepository.saveAndFlush(orderBeta);

        // Get all the orderBetaList where totalPrice in
        defaultOrderBetaFiltering(
            "totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE,
            "totalPrice.in=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllOrderBetasByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderBeta = orderBetaRepository.saveAndFlush(orderBeta);

        // Get all the orderBetaList where totalPrice is not null
        defaultOrderBetaFiltering("totalPrice.specified=true", "totalPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderBetasByTotalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderBeta = orderBetaRepository.saveAndFlush(orderBeta);

        // Get all the orderBetaList where totalPrice is greater than or equal to
        defaultOrderBetaFiltering(
            "totalPrice.greaterThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.greaterThanOrEqual=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllOrderBetasByTotalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderBeta = orderBetaRepository.saveAndFlush(orderBeta);

        // Get all the orderBetaList where totalPrice is less than or equal to
        defaultOrderBetaFiltering("totalPrice.lessThanOrEqual=" + DEFAULT_TOTAL_PRICE, "totalPrice.lessThanOrEqual=" + SMALLER_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderBetasByTotalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrderBeta = orderBetaRepository.saveAndFlush(orderBeta);

        // Get all the orderBetaList where totalPrice is less than
        defaultOrderBetaFiltering("totalPrice.lessThan=" + UPDATED_TOTAL_PRICE, "totalPrice.lessThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderBetasByTotalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrderBeta = orderBetaRepository.saveAndFlush(orderBeta);

        // Get all the orderBetaList where totalPrice is greater than
        defaultOrderBetaFiltering("totalPrice.greaterThan=" + SMALLER_TOTAL_PRICE, "totalPrice.greaterThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderBetasByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderBeta = orderBetaRepository.saveAndFlush(orderBeta);

        // Get all the orderBetaList where status equals to
        defaultOrderBetaFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOrderBetasByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderBeta = orderBetaRepository.saveAndFlush(orderBeta);

        // Get all the orderBetaList where status in
        defaultOrderBetaFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOrderBetasByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderBeta = orderBetaRepository.saveAndFlush(orderBeta);

        // Get all the orderBetaList where status is not null
        defaultOrderBetaFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderBetasByPaymentIsEqualToSomething() throws Exception {
        PaymentBeta payment;
        if (TestUtil.findAll(em, PaymentBeta.class).isEmpty()) {
            orderBetaRepository.saveAndFlush(orderBeta);
            payment = PaymentBetaResourceIT.createEntity();
        } else {
            payment = TestUtil.findAll(em, PaymentBeta.class).get(0);
        }
        em.persist(payment);
        em.flush();
        orderBeta.setPayment(payment);
        orderBetaRepository.saveAndFlush(orderBeta);
        Long paymentId = payment.getId();
        // Get all the orderBetaList where payment equals to paymentId
        defaultOrderBetaShouldBeFound("paymentId.equals=" + paymentId);

        // Get all the orderBetaList where payment equals to (paymentId + 1)
        defaultOrderBetaShouldNotBeFound("paymentId.equals=" + (paymentId + 1));
    }

    @Test
    @Transactional
    void getAllOrderBetasByShipmentIsEqualToSomething() throws Exception {
        ShipmentBeta shipment;
        if (TestUtil.findAll(em, ShipmentBeta.class).isEmpty()) {
            orderBetaRepository.saveAndFlush(orderBeta);
            shipment = ShipmentBetaResourceIT.createEntity();
        } else {
            shipment = TestUtil.findAll(em, ShipmentBeta.class).get(0);
        }
        em.persist(shipment);
        em.flush();
        orderBeta.setShipment(shipment);
        orderBetaRepository.saveAndFlush(orderBeta);
        Long shipmentId = shipment.getId();
        // Get all the orderBetaList where shipment equals to shipmentId
        defaultOrderBetaShouldBeFound("shipmentId.equals=" + shipmentId);

        // Get all the orderBetaList where shipment equals to (shipmentId + 1)
        defaultOrderBetaShouldNotBeFound("shipmentId.equals=" + (shipmentId + 1));
    }

    @Test
    @Transactional
    void getAllOrderBetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            orderBetaRepository.saveAndFlush(orderBeta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        orderBeta.setTenant(tenant);
        orderBetaRepository.saveAndFlush(orderBeta);
        Long tenantId = tenant.getId();
        // Get all the orderBetaList where tenant equals to tenantId
        defaultOrderBetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the orderBetaList where tenant equals to (tenantId + 1)
        defaultOrderBetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllOrderBetasByCustomerIsEqualToSomething() throws Exception {
        CustomerBeta customer;
        if (TestUtil.findAll(em, CustomerBeta.class).isEmpty()) {
            orderBetaRepository.saveAndFlush(orderBeta);
            customer = CustomerBetaResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, CustomerBeta.class).get(0);
        }
        em.persist(customer);
        em.flush();
        orderBeta.setCustomer(customer);
        orderBetaRepository.saveAndFlush(orderBeta);
        Long customerId = customer.getId();
        // Get all the orderBetaList where customer equals to customerId
        defaultOrderBetaShouldBeFound("customerId.equals=" + customerId);

        // Get all the orderBetaList where customer equals to (customerId + 1)
        defaultOrderBetaShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    private void defaultOrderBetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOrderBetaShouldBeFound(shouldBeFound);
        defaultOrderBetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderBetaShouldBeFound(String filter) throws Exception {
        restOrderBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restOrderBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderBetaShouldNotBeFound(String filter) throws Exception {
        restOrderBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrderBeta() throws Exception {
        // Get the orderBeta
        restOrderBetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderBeta() throws Exception {
        // Initialize the database
        insertedOrderBeta = orderBetaRepository.saveAndFlush(orderBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderBeta
        OrderBeta updatedOrderBeta = orderBetaRepository.findById(orderBeta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrderBeta are not directly saved in db
        em.detach(updatedOrderBeta);
        updatedOrderBeta.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);
        OrderBetaDTO orderBetaDTO = orderBetaMapper.toDto(updatedOrderBeta);

        restOrderBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderBetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderBetaDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrderBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrderBetaToMatchAllProperties(updatedOrderBeta);
    }

    @Test
    @Transactional
    void putNonExistingOrderBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderBeta.setId(longCount.incrementAndGet());

        // Create the OrderBeta
        OrderBetaDTO orderBetaDTO = orderBetaMapper.toDto(orderBeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderBetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderBeta.setId(longCount.incrementAndGet());

        // Create the OrderBeta
        OrderBetaDTO orderBetaDTO = orderBetaMapper.toDto(orderBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderBeta.setId(longCount.incrementAndGet());

        // Create the OrderBeta
        OrderBetaDTO orderBetaDTO = orderBetaMapper.toDto(orderBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderBetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderBetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderBetaWithPatch() throws Exception {
        // Initialize the database
        insertedOrderBeta = orderBetaRepository.saveAndFlush(orderBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderBeta using partial update
        OrderBeta partialUpdatedOrderBeta = new OrderBeta();
        partialUpdatedOrderBeta.setId(orderBeta.getId());

        partialUpdatedOrderBeta.status(UPDATED_STATUS);

        restOrderBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderBeta))
            )
            .andExpect(status().isOk());

        // Validate the OrderBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderBetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOrderBeta, orderBeta),
            getPersistedOrderBeta(orderBeta)
        );
    }

    @Test
    @Transactional
    void fullUpdateOrderBetaWithPatch() throws Exception {
        // Initialize the database
        insertedOrderBeta = orderBetaRepository.saveAndFlush(orderBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderBeta using partial update
        OrderBeta partialUpdatedOrderBeta = new OrderBeta();
        partialUpdatedOrderBeta.setId(orderBeta.getId());

        partialUpdatedOrderBeta.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);

        restOrderBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderBeta))
            )
            .andExpect(status().isOk());

        // Validate the OrderBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderBetaUpdatableFieldsEquals(partialUpdatedOrderBeta, getPersistedOrderBeta(partialUpdatedOrderBeta));
    }

    @Test
    @Transactional
    void patchNonExistingOrderBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderBeta.setId(longCount.incrementAndGet());

        // Create the OrderBeta
        OrderBetaDTO orderBetaDTO = orderBetaMapper.toDto(orderBeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderBetaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderBeta.setId(longCount.incrementAndGet());

        // Create the OrderBeta
        OrderBetaDTO orderBetaDTO = orderBetaMapper.toDto(orderBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderBeta.setId(longCount.incrementAndGet());

        // Create the OrderBeta
        OrderBetaDTO orderBetaDTO = orderBetaMapper.toDto(orderBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderBetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(orderBetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderBeta() throws Exception {
        // Initialize the database
        insertedOrderBeta = orderBetaRepository.saveAndFlush(orderBeta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the orderBeta
        restOrderBetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderBeta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return orderBetaRepository.count();
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

    protected OrderBeta getPersistedOrderBeta(OrderBeta orderBeta) {
        return orderBetaRepository.findById(orderBeta.getId()).orElseThrow();
    }

    protected void assertPersistedOrderBetaToMatchAllProperties(OrderBeta expectedOrderBeta) {
        assertOrderBetaAllPropertiesEquals(expectedOrderBeta, getPersistedOrderBeta(expectedOrderBeta));
    }

    protected void assertPersistedOrderBetaToMatchUpdatableProperties(OrderBeta expectedOrderBeta) {
        assertOrderBetaAllUpdatablePropertiesEquals(expectedOrderBeta, getPersistedOrderBeta(expectedOrderBeta));
    }
}
