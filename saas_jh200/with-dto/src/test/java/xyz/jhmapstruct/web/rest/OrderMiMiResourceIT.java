package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.OrderMiMiAsserts.*;
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
import xyz.jhmapstruct.domain.CustomerMiMi;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.OrderMiMi;
import xyz.jhmapstruct.domain.PaymentMiMi;
import xyz.jhmapstruct.domain.ShipmentMiMi;
import xyz.jhmapstruct.domain.enumeration.OrderMiMiStatus;
import xyz.jhmapstruct.repository.OrderMiMiRepository;
import xyz.jhmapstruct.service.OrderMiMiService;
import xyz.jhmapstruct.service.dto.OrderMiMiDTO;
import xyz.jhmapstruct.service.mapper.OrderMiMiMapper;

/**
 * Integration tests for the {@link OrderMiMiResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OrderMiMiResourceIT {

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_PRICE = new BigDecimal(1 - 1);

    private static final OrderMiMiStatus DEFAULT_STATUS = OrderMiMiStatus.PENDING;
    private static final OrderMiMiStatus UPDATED_STATUS = OrderMiMiStatus.SHIPPED;

    private static final String ENTITY_API_URL = "/api/order-mi-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrderMiMiRepository orderMiMiRepository;

    @Mock
    private OrderMiMiRepository orderMiMiRepositoryMock;

    @Autowired
    private OrderMiMiMapper orderMiMiMapper;

    @Mock
    private OrderMiMiService orderMiMiServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderMiMiMockMvc;

    private OrderMiMi orderMiMi;

    private OrderMiMi insertedOrderMiMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderMiMi createEntity() {
        return new OrderMiMi().orderDate(DEFAULT_ORDER_DATE).totalPrice(DEFAULT_TOTAL_PRICE).status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderMiMi createUpdatedEntity() {
        return new OrderMiMi().orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        orderMiMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedOrderMiMi != null) {
            orderMiMiRepository.delete(insertedOrderMiMi);
            insertedOrderMiMi = null;
        }
    }

    @Test
    @Transactional
    void createOrderMiMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OrderMiMi
        OrderMiMiDTO orderMiMiDTO = orderMiMiMapper.toDto(orderMiMi);
        var returnedOrderMiMiDTO = om.readValue(
            restOrderMiMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderMiMiDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OrderMiMiDTO.class
        );

        // Validate the OrderMiMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOrderMiMi = orderMiMiMapper.toEntity(returnedOrderMiMiDTO);
        assertOrderMiMiUpdatableFieldsEquals(returnedOrderMiMi, getPersistedOrderMiMi(returnedOrderMiMi));

        insertedOrderMiMi = returnedOrderMiMi;
    }

    @Test
    @Transactional
    void createOrderMiMiWithExistingId() throws Exception {
        // Create the OrderMiMi with an existing ID
        orderMiMi.setId(1L);
        OrderMiMiDTO orderMiMiDTO = orderMiMiMapper.toDto(orderMiMi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderMiMiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrderMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderMiMi.setOrderDate(null);

        // Create the OrderMiMi, which fails.
        OrderMiMiDTO orderMiMiDTO = orderMiMiMapper.toDto(orderMiMi);

        restOrderMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderMiMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderMiMi.setTotalPrice(null);

        // Create the OrderMiMi, which fails.
        OrderMiMiDTO orderMiMiDTO = orderMiMiMapper.toDto(orderMiMi);

        restOrderMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderMiMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrderMiMis() throws Exception {
        // Initialize the database
        insertedOrderMiMi = orderMiMiRepository.saveAndFlush(orderMiMi);

        // Get all the orderMiMiList
        restOrderMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderMiMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderMiMisWithEagerRelationshipsIsEnabled() throws Exception {
        when(orderMiMiServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderMiMiMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(orderMiMiServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderMiMisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(orderMiMiServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderMiMiMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(orderMiMiRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getOrderMiMi() throws Exception {
        // Initialize the database
        insertedOrderMiMi = orderMiMiRepository.saveAndFlush(orderMiMi);

        // Get the orderMiMi
        restOrderMiMiMockMvc
            .perform(get(ENTITY_API_URL_ID, orderMiMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderMiMi.getId().intValue()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getOrderMiMisByIdFiltering() throws Exception {
        // Initialize the database
        insertedOrderMiMi = orderMiMiRepository.saveAndFlush(orderMiMi);

        Long id = orderMiMi.getId();

        defaultOrderMiMiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOrderMiMiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOrderMiMiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrderMiMisByOrderDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderMiMi = orderMiMiRepository.saveAndFlush(orderMiMi);

        // Get all the orderMiMiList where orderDate equals to
        defaultOrderMiMiFiltering("orderDate.equals=" + DEFAULT_ORDER_DATE, "orderDate.equals=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllOrderMiMisByOrderDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderMiMi = orderMiMiRepository.saveAndFlush(orderMiMi);

        // Get all the orderMiMiList where orderDate in
        defaultOrderMiMiFiltering("orderDate.in=" + DEFAULT_ORDER_DATE + "," + UPDATED_ORDER_DATE, "orderDate.in=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllOrderMiMisByOrderDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderMiMi = orderMiMiRepository.saveAndFlush(orderMiMi);

        // Get all the orderMiMiList where orderDate is not null
        defaultOrderMiMiFiltering("orderDate.specified=true", "orderDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderMiMisByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderMiMi = orderMiMiRepository.saveAndFlush(orderMiMi);

        // Get all the orderMiMiList where totalPrice equals to
        defaultOrderMiMiFiltering("totalPrice.equals=" + DEFAULT_TOTAL_PRICE, "totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderMiMisByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderMiMi = orderMiMiRepository.saveAndFlush(orderMiMi);

        // Get all the orderMiMiList where totalPrice in
        defaultOrderMiMiFiltering(
            "totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE,
            "totalPrice.in=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllOrderMiMisByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderMiMi = orderMiMiRepository.saveAndFlush(orderMiMi);

        // Get all the orderMiMiList where totalPrice is not null
        defaultOrderMiMiFiltering("totalPrice.specified=true", "totalPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderMiMisByTotalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderMiMi = orderMiMiRepository.saveAndFlush(orderMiMi);

        // Get all the orderMiMiList where totalPrice is greater than or equal to
        defaultOrderMiMiFiltering(
            "totalPrice.greaterThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.greaterThanOrEqual=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllOrderMiMisByTotalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderMiMi = orderMiMiRepository.saveAndFlush(orderMiMi);

        // Get all the orderMiMiList where totalPrice is less than or equal to
        defaultOrderMiMiFiltering("totalPrice.lessThanOrEqual=" + DEFAULT_TOTAL_PRICE, "totalPrice.lessThanOrEqual=" + SMALLER_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderMiMisByTotalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrderMiMi = orderMiMiRepository.saveAndFlush(orderMiMi);

        // Get all the orderMiMiList where totalPrice is less than
        defaultOrderMiMiFiltering("totalPrice.lessThan=" + UPDATED_TOTAL_PRICE, "totalPrice.lessThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderMiMisByTotalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrderMiMi = orderMiMiRepository.saveAndFlush(orderMiMi);

        // Get all the orderMiMiList where totalPrice is greater than
        defaultOrderMiMiFiltering("totalPrice.greaterThan=" + SMALLER_TOTAL_PRICE, "totalPrice.greaterThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderMiMisByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderMiMi = orderMiMiRepository.saveAndFlush(orderMiMi);

        // Get all the orderMiMiList where status equals to
        defaultOrderMiMiFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOrderMiMisByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderMiMi = orderMiMiRepository.saveAndFlush(orderMiMi);

        // Get all the orderMiMiList where status in
        defaultOrderMiMiFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOrderMiMisByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderMiMi = orderMiMiRepository.saveAndFlush(orderMiMi);

        // Get all the orderMiMiList where status is not null
        defaultOrderMiMiFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderMiMisByPaymentIsEqualToSomething() throws Exception {
        PaymentMiMi payment;
        if (TestUtil.findAll(em, PaymentMiMi.class).isEmpty()) {
            orderMiMiRepository.saveAndFlush(orderMiMi);
            payment = PaymentMiMiResourceIT.createEntity();
        } else {
            payment = TestUtil.findAll(em, PaymentMiMi.class).get(0);
        }
        em.persist(payment);
        em.flush();
        orderMiMi.setPayment(payment);
        orderMiMiRepository.saveAndFlush(orderMiMi);
        Long paymentId = payment.getId();
        // Get all the orderMiMiList where payment equals to paymentId
        defaultOrderMiMiShouldBeFound("paymentId.equals=" + paymentId);

        // Get all the orderMiMiList where payment equals to (paymentId + 1)
        defaultOrderMiMiShouldNotBeFound("paymentId.equals=" + (paymentId + 1));
    }

    @Test
    @Transactional
    void getAllOrderMiMisByShipmentIsEqualToSomething() throws Exception {
        ShipmentMiMi shipment;
        if (TestUtil.findAll(em, ShipmentMiMi.class).isEmpty()) {
            orderMiMiRepository.saveAndFlush(orderMiMi);
            shipment = ShipmentMiMiResourceIT.createEntity();
        } else {
            shipment = TestUtil.findAll(em, ShipmentMiMi.class).get(0);
        }
        em.persist(shipment);
        em.flush();
        orderMiMi.setShipment(shipment);
        orderMiMiRepository.saveAndFlush(orderMiMi);
        Long shipmentId = shipment.getId();
        // Get all the orderMiMiList where shipment equals to shipmentId
        defaultOrderMiMiShouldBeFound("shipmentId.equals=" + shipmentId);

        // Get all the orderMiMiList where shipment equals to (shipmentId + 1)
        defaultOrderMiMiShouldNotBeFound("shipmentId.equals=" + (shipmentId + 1));
    }

    @Test
    @Transactional
    void getAllOrderMiMisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            orderMiMiRepository.saveAndFlush(orderMiMi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        orderMiMi.setTenant(tenant);
        orderMiMiRepository.saveAndFlush(orderMiMi);
        Long tenantId = tenant.getId();
        // Get all the orderMiMiList where tenant equals to tenantId
        defaultOrderMiMiShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the orderMiMiList where tenant equals to (tenantId + 1)
        defaultOrderMiMiShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllOrderMiMisByCustomerIsEqualToSomething() throws Exception {
        CustomerMiMi customer;
        if (TestUtil.findAll(em, CustomerMiMi.class).isEmpty()) {
            orderMiMiRepository.saveAndFlush(orderMiMi);
            customer = CustomerMiMiResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, CustomerMiMi.class).get(0);
        }
        em.persist(customer);
        em.flush();
        orderMiMi.setCustomer(customer);
        orderMiMiRepository.saveAndFlush(orderMiMi);
        Long customerId = customer.getId();
        // Get all the orderMiMiList where customer equals to customerId
        defaultOrderMiMiShouldBeFound("customerId.equals=" + customerId);

        // Get all the orderMiMiList where customer equals to (customerId + 1)
        defaultOrderMiMiShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    private void defaultOrderMiMiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOrderMiMiShouldBeFound(shouldBeFound);
        defaultOrderMiMiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderMiMiShouldBeFound(String filter) throws Exception {
        restOrderMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderMiMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restOrderMiMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderMiMiShouldNotBeFound(String filter) throws Exception {
        restOrderMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderMiMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrderMiMi() throws Exception {
        // Get the orderMiMi
        restOrderMiMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderMiMi() throws Exception {
        // Initialize the database
        insertedOrderMiMi = orderMiMiRepository.saveAndFlush(orderMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderMiMi
        OrderMiMi updatedOrderMiMi = orderMiMiRepository.findById(orderMiMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrderMiMi are not directly saved in db
        em.detach(updatedOrderMiMi);
        updatedOrderMiMi.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);
        OrderMiMiDTO orderMiMiDTO = orderMiMiMapper.toDto(updatedOrderMiMi);

        restOrderMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderMiMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderMiMiDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrderMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrderMiMiToMatchAllProperties(updatedOrderMiMi);
    }

    @Test
    @Transactional
    void putNonExistingOrderMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderMiMi.setId(longCount.incrementAndGet());

        // Create the OrderMiMi
        OrderMiMiDTO orderMiMiDTO = orderMiMiMapper.toDto(orderMiMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderMiMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderMiMi.setId(longCount.incrementAndGet());

        // Create the OrderMiMi
        OrderMiMiDTO orderMiMiDTO = orderMiMiMapper.toDto(orderMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderMiMi.setId(longCount.incrementAndGet());

        // Create the OrderMiMi
        OrderMiMiDTO orderMiMiDTO = orderMiMiMapper.toDto(orderMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMiMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderMiMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedOrderMiMi = orderMiMiRepository.saveAndFlush(orderMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderMiMi using partial update
        OrderMiMi partialUpdatedOrderMiMi = new OrderMiMi();
        partialUpdatedOrderMiMi.setId(orderMiMi.getId());

        partialUpdatedOrderMiMi.status(UPDATED_STATUS);

        restOrderMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderMiMi))
            )
            .andExpect(status().isOk());

        // Validate the OrderMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderMiMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOrderMiMi, orderMiMi),
            getPersistedOrderMiMi(orderMiMi)
        );
    }

    @Test
    @Transactional
    void fullUpdateOrderMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedOrderMiMi = orderMiMiRepository.saveAndFlush(orderMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderMiMi using partial update
        OrderMiMi partialUpdatedOrderMiMi = new OrderMiMi();
        partialUpdatedOrderMiMi.setId(orderMiMi.getId());

        partialUpdatedOrderMiMi.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);

        restOrderMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderMiMi))
            )
            .andExpect(status().isOk());

        // Validate the OrderMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderMiMiUpdatableFieldsEquals(partialUpdatedOrderMiMi, getPersistedOrderMiMi(partialUpdatedOrderMiMi));
    }

    @Test
    @Transactional
    void patchNonExistingOrderMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderMiMi.setId(longCount.incrementAndGet());

        // Create the OrderMiMi
        OrderMiMiDTO orderMiMiDTO = orderMiMiMapper.toDto(orderMiMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderMiMiDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderMiMi.setId(longCount.incrementAndGet());

        // Create the OrderMiMi
        OrderMiMiDTO orderMiMiDTO = orderMiMiMapper.toDto(orderMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderMiMi.setId(longCount.incrementAndGet());

        // Create the OrderMiMi
        OrderMiMiDTO orderMiMiDTO = orderMiMiMapper.toDto(orderMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMiMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(orderMiMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderMiMi() throws Exception {
        // Initialize the database
        insertedOrderMiMi = orderMiMiRepository.saveAndFlush(orderMiMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the orderMiMi
        restOrderMiMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderMiMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return orderMiMiRepository.count();
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

    protected OrderMiMi getPersistedOrderMiMi(OrderMiMi orderMiMi) {
        return orderMiMiRepository.findById(orderMiMi.getId()).orElseThrow();
    }

    protected void assertPersistedOrderMiMiToMatchAllProperties(OrderMiMi expectedOrderMiMi) {
        assertOrderMiMiAllPropertiesEquals(expectedOrderMiMi, getPersistedOrderMiMi(expectedOrderMiMi));
    }

    protected void assertPersistedOrderMiMiToMatchUpdatableProperties(OrderMiMi expectedOrderMiMi) {
        assertOrderMiMiAllUpdatablePropertiesEquals(expectedOrderMiMi, getPersistedOrderMiMi(expectedOrderMiMi));
    }
}
