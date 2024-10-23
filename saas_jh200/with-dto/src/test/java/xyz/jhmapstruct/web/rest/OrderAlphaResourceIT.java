package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.OrderAlphaAsserts.*;
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
import xyz.jhmapstruct.domain.CustomerAlpha;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.OrderAlpha;
import xyz.jhmapstruct.domain.PaymentAlpha;
import xyz.jhmapstruct.domain.ShipmentAlpha;
import xyz.jhmapstruct.domain.enumeration.OrderAlphaStatus;
import xyz.jhmapstruct.repository.OrderAlphaRepository;
import xyz.jhmapstruct.service.OrderAlphaService;
import xyz.jhmapstruct.service.dto.OrderAlphaDTO;
import xyz.jhmapstruct.service.mapper.OrderAlphaMapper;

/**
 * Integration tests for the {@link OrderAlphaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OrderAlphaResourceIT {

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_PRICE = new BigDecimal(1 - 1);

    private static final OrderAlphaStatus DEFAULT_STATUS = OrderAlphaStatus.PENDING;
    private static final OrderAlphaStatus UPDATED_STATUS = OrderAlphaStatus.SHIPPED;

    private static final String ENTITY_API_URL = "/api/order-alphas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrderAlphaRepository orderAlphaRepository;

    @Mock
    private OrderAlphaRepository orderAlphaRepositoryMock;

    @Autowired
    private OrderAlphaMapper orderAlphaMapper;

    @Mock
    private OrderAlphaService orderAlphaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderAlphaMockMvc;

    private OrderAlpha orderAlpha;

    private OrderAlpha insertedOrderAlpha;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderAlpha createEntity() {
        return new OrderAlpha().orderDate(DEFAULT_ORDER_DATE).totalPrice(DEFAULT_TOTAL_PRICE).status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderAlpha createUpdatedEntity() {
        return new OrderAlpha().orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        orderAlpha = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedOrderAlpha != null) {
            orderAlphaRepository.delete(insertedOrderAlpha);
            insertedOrderAlpha = null;
        }
    }

    @Test
    @Transactional
    void createOrderAlpha() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OrderAlpha
        OrderAlphaDTO orderAlphaDTO = orderAlphaMapper.toDto(orderAlpha);
        var returnedOrderAlphaDTO = om.readValue(
            restOrderAlphaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderAlphaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OrderAlphaDTO.class
        );

        // Validate the OrderAlpha in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOrderAlpha = orderAlphaMapper.toEntity(returnedOrderAlphaDTO);
        assertOrderAlphaUpdatableFieldsEquals(returnedOrderAlpha, getPersistedOrderAlpha(returnedOrderAlpha));

        insertedOrderAlpha = returnedOrderAlpha;
    }

    @Test
    @Transactional
    void createOrderAlphaWithExistingId() throws Exception {
        // Create the OrderAlpha with an existing ID
        orderAlpha.setId(1L);
        OrderAlphaDTO orderAlphaDTO = orderAlphaMapper.toDto(orderAlpha);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderAlphaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrderAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderAlpha.setOrderDate(null);

        // Create the OrderAlpha, which fails.
        OrderAlphaDTO orderAlphaDTO = orderAlphaMapper.toDto(orderAlpha);

        restOrderAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderAlphaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderAlpha.setTotalPrice(null);

        // Create the OrderAlpha, which fails.
        OrderAlphaDTO orderAlphaDTO = orderAlphaMapper.toDto(orderAlpha);

        restOrderAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderAlphaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrderAlphas() throws Exception {
        // Initialize the database
        insertedOrderAlpha = orderAlphaRepository.saveAndFlush(orderAlpha);

        // Get all the orderAlphaList
        restOrderAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderAlphasWithEagerRelationshipsIsEnabled() throws Exception {
        when(orderAlphaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderAlphaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(orderAlphaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderAlphasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(orderAlphaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderAlphaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(orderAlphaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getOrderAlpha() throws Exception {
        // Initialize the database
        insertedOrderAlpha = orderAlphaRepository.saveAndFlush(orderAlpha);

        // Get the orderAlpha
        restOrderAlphaMockMvc
            .perform(get(ENTITY_API_URL_ID, orderAlpha.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderAlpha.getId().intValue()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getOrderAlphasByIdFiltering() throws Exception {
        // Initialize the database
        insertedOrderAlpha = orderAlphaRepository.saveAndFlush(orderAlpha);

        Long id = orderAlpha.getId();

        defaultOrderAlphaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOrderAlphaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOrderAlphaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrderAlphasByOrderDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderAlpha = orderAlphaRepository.saveAndFlush(orderAlpha);

        // Get all the orderAlphaList where orderDate equals to
        defaultOrderAlphaFiltering("orderDate.equals=" + DEFAULT_ORDER_DATE, "orderDate.equals=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllOrderAlphasByOrderDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderAlpha = orderAlphaRepository.saveAndFlush(orderAlpha);

        // Get all the orderAlphaList where orderDate in
        defaultOrderAlphaFiltering("orderDate.in=" + DEFAULT_ORDER_DATE + "," + UPDATED_ORDER_DATE, "orderDate.in=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllOrderAlphasByOrderDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderAlpha = orderAlphaRepository.saveAndFlush(orderAlpha);

        // Get all the orderAlphaList where orderDate is not null
        defaultOrderAlphaFiltering("orderDate.specified=true", "orderDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderAlphasByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderAlpha = orderAlphaRepository.saveAndFlush(orderAlpha);

        // Get all the orderAlphaList where totalPrice equals to
        defaultOrderAlphaFiltering("totalPrice.equals=" + DEFAULT_TOTAL_PRICE, "totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderAlphasByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderAlpha = orderAlphaRepository.saveAndFlush(orderAlpha);

        // Get all the orderAlphaList where totalPrice in
        defaultOrderAlphaFiltering(
            "totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE,
            "totalPrice.in=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllOrderAlphasByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderAlpha = orderAlphaRepository.saveAndFlush(orderAlpha);

        // Get all the orderAlphaList where totalPrice is not null
        defaultOrderAlphaFiltering("totalPrice.specified=true", "totalPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderAlphasByTotalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderAlpha = orderAlphaRepository.saveAndFlush(orderAlpha);

        // Get all the orderAlphaList where totalPrice is greater than or equal to
        defaultOrderAlphaFiltering(
            "totalPrice.greaterThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.greaterThanOrEqual=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllOrderAlphasByTotalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderAlpha = orderAlphaRepository.saveAndFlush(orderAlpha);

        // Get all the orderAlphaList where totalPrice is less than or equal to
        defaultOrderAlphaFiltering(
            "totalPrice.lessThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.lessThanOrEqual=" + SMALLER_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllOrderAlphasByTotalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrderAlpha = orderAlphaRepository.saveAndFlush(orderAlpha);

        // Get all the orderAlphaList where totalPrice is less than
        defaultOrderAlphaFiltering("totalPrice.lessThan=" + UPDATED_TOTAL_PRICE, "totalPrice.lessThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderAlphasByTotalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrderAlpha = orderAlphaRepository.saveAndFlush(orderAlpha);

        // Get all the orderAlphaList where totalPrice is greater than
        defaultOrderAlphaFiltering("totalPrice.greaterThan=" + SMALLER_TOTAL_PRICE, "totalPrice.greaterThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderAlphasByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderAlpha = orderAlphaRepository.saveAndFlush(orderAlpha);

        // Get all the orderAlphaList where status equals to
        defaultOrderAlphaFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOrderAlphasByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderAlpha = orderAlphaRepository.saveAndFlush(orderAlpha);

        // Get all the orderAlphaList where status in
        defaultOrderAlphaFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOrderAlphasByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderAlpha = orderAlphaRepository.saveAndFlush(orderAlpha);

        // Get all the orderAlphaList where status is not null
        defaultOrderAlphaFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderAlphasByPaymentIsEqualToSomething() throws Exception {
        PaymentAlpha payment;
        if (TestUtil.findAll(em, PaymentAlpha.class).isEmpty()) {
            orderAlphaRepository.saveAndFlush(orderAlpha);
            payment = PaymentAlphaResourceIT.createEntity();
        } else {
            payment = TestUtil.findAll(em, PaymentAlpha.class).get(0);
        }
        em.persist(payment);
        em.flush();
        orderAlpha.setPayment(payment);
        orderAlphaRepository.saveAndFlush(orderAlpha);
        Long paymentId = payment.getId();
        // Get all the orderAlphaList where payment equals to paymentId
        defaultOrderAlphaShouldBeFound("paymentId.equals=" + paymentId);

        // Get all the orderAlphaList where payment equals to (paymentId + 1)
        defaultOrderAlphaShouldNotBeFound("paymentId.equals=" + (paymentId + 1));
    }

    @Test
    @Transactional
    void getAllOrderAlphasByShipmentIsEqualToSomething() throws Exception {
        ShipmentAlpha shipment;
        if (TestUtil.findAll(em, ShipmentAlpha.class).isEmpty()) {
            orderAlphaRepository.saveAndFlush(orderAlpha);
            shipment = ShipmentAlphaResourceIT.createEntity();
        } else {
            shipment = TestUtil.findAll(em, ShipmentAlpha.class).get(0);
        }
        em.persist(shipment);
        em.flush();
        orderAlpha.setShipment(shipment);
        orderAlphaRepository.saveAndFlush(orderAlpha);
        Long shipmentId = shipment.getId();
        // Get all the orderAlphaList where shipment equals to shipmentId
        defaultOrderAlphaShouldBeFound("shipmentId.equals=" + shipmentId);

        // Get all the orderAlphaList where shipment equals to (shipmentId + 1)
        defaultOrderAlphaShouldNotBeFound("shipmentId.equals=" + (shipmentId + 1));
    }

    @Test
    @Transactional
    void getAllOrderAlphasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            orderAlphaRepository.saveAndFlush(orderAlpha);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        orderAlpha.setTenant(tenant);
        orderAlphaRepository.saveAndFlush(orderAlpha);
        Long tenantId = tenant.getId();
        // Get all the orderAlphaList where tenant equals to tenantId
        defaultOrderAlphaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the orderAlphaList where tenant equals to (tenantId + 1)
        defaultOrderAlphaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllOrderAlphasByCustomerIsEqualToSomething() throws Exception {
        CustomerAlpha customer;
        if (TestUtil.findAll(em, CustomerAlpha.class).isEmpty()) {
            orderAlphaRepository.saveAndFlush(orderAlpha);
            customer = CustomerAlphaResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, CustomerAlpha.class).get(0);
        }
        em.persist(customer);
        em.flush();
        orderAlpha.setCustomer(customer);
        orderAlphaRepository.saveAndFlush(orderAlpha);
        Long customerId = customer.getId();
        // Get all the orderAlphaList where customer equals to customerId
        defaultOrderAlphaShouldBeFound("customerId.equals=" + customerId);

        // Get all the orderAlphaList where customer equals to (customerId + 1)
        defaultOrderAlphaShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    private void defaultOrderAlphaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOrderAlphaShouldBeFound(shouldBeFound);
        defaultOrderAlphaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderAlphaShouldBeFound(String filter) throws Exception {
        restOrderAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restOrderAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderAlphaShouldNotBeFound(String filter) throws Exception {
        restOrderAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrderAlpha() throws Exception {
        // Get the orderAlpha
        restOrderAlphaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderAlpha() throws Exception {
        // Initialize the database
        insertedOrderAlpha = orderAlphaRepository.saveAndFlush(orderAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderAlpha
        OrderAlpha updatedOrderAlpha = orderAlphaRepository.findById(orderAlpha.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrderAlpha are not directly saved in db
        em.detach(updatedOrderAlpha);
        updatedOrderAlpha.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);
        OrderAlphaDTO orderAlphaDTO = orderAlphaMapper.toDto(updatedOrderAlpha);

        restOrderAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderAlphaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderAlphaDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrderAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrderAlphaToMatchAllProperties(updatedOrderAlpha);
    }

    @Test
    @Transactional
    void putNonExistingOrderAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderAlpha.setId(longCount.incrementAndGet());

        // Create the OrderAlpha
        OrderAlphaDTO orderAlphaDTO = orderAlphaMapper.toDto(orderAlpha);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderAlphaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderAlpha.setId(longCount.incrementAndGet());

        // Create the OrderAlpha
        OrderAlphaDTO orderAlphaDTO = orderAlphaMapper.toDto(orderAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderAlpha.setId(longCount.incrementAndGet());

        // Create the OrderAlpha
        OrderAlphaDTO orderAlphaDTO = orderAlphaMapper.toDto(orderAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderAlphaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderAlphaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedOrderAlpha = orderAlphaRepository.saveAndFlush(orderAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderAlpha using partial update
        OrderAlpha partialUpdatedOrderAlpha = new OrderAlpha();
        partialUpdatedOrderAlpha.setId(orderAlpha.getId());

        partialUpdatedOrderAlpha.orderDate(UPDATED_ORDER_DATE);

        restOrderAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderAlpha))
            )
            .andExpect(status().isOk());

        // Validate the OrderAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderAlphaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOrderAlpha, orderAlpha),
            getPersistedOrderAlpha(orderAlpha)
        );
    }

    @Test
    @Transactional
    void fullUpdateOrderAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedOrderAlpha = orderAlphaRepository.saveAndFlush(orderAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderAlpha using partial update
        OrderAlpha partialUpdatedOrderAlpha = new OrderAlpha();
        partialUpdatedOrderAlpha.setId(orderAlpha.getId());

        partialUpdatedOrderAlpha.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);

        restOrderAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderAlpha))
            )
            .andExpect(status().isOk());

        // Validate the OrderAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderAlphaUpdatableFieldsEquals(partialUpdatedOrderAlpha, getPersistedOrderAlpha(partialUpdatedOrderAlpha));
    }

    @Test
    @Transactional
    void patchNonExistingOrderAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderAlpha.setId(longCount.incrementAndGet());

        // Create the OrderAlpha
        OrderAlphaDTO orderAlphaDTO = orderAlphaMapper.toDto(orderAlpha);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderAlphaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderAlpha.setId(longCount.incrementAndGet());

        // Create the OrderAlpha
        OrderAlphaDTO orderAlphaDTO = orderAlphaMapper.toDto(orderAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderAlpha.setId(longCount.incrementAndGet());

        // Create the OrderAlpha
        OrderAlphaDTO orderAlphaDTO = orderAlphaMapper.toDto(orderAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderAlphaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(orderAlphaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderAlpha() throws Exception {
        // Initialize the database
        insertedOrderAlpha = orderAlphaRepository.saveAndFlush(orderAlpha);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the orderAlpha
        restOrderAlphaMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderAlpha.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return orderAlphaRepository.count();
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

    protected OrderAlpha getPersistedOrderAlpha(OrderAlpha orderAlpha) {
        return orderAlphaRepository.findById(orderAlpha.getId()).orElseThrow();
    }

    protected void assertPersistedOrderAlphaToMatchAllProperties(OrderAlpha expectedOrderAlpha) {
        assertOrderAlphaAllPropertiesEquals(expectedOrderAlpha, getPersistedOrderAlpha(expectedOrderAlpha));
    }

    protected void assertPersistedOrderAlphaToMatchUpdatableProperties(OrderAlpha expectedOrderAlpha) {
        assertOrderAlphaAllUpdatablePropertiesEquals(expectedOrderAlpha, getPersistedOrderAlpha(expectedOrderAlpha));
    }
}
