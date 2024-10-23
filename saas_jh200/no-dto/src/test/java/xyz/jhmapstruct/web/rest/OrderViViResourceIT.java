package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.OrderViViAsserts.*;
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
import xyz.jhmapstruct.domain.CustomerViVi;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.OrderViVi;
import xyz.jhmapstruct.domain.PaymentViVi;
import xyz.jhmapstruct.domain.ShipmentViVi;
import xyz.jhmapstruct.domain.enumeration.OrderViViStatus;
import xyz.jhmapstruct.repository.OrderViViRepository;
import xyz.jhmapstruct.service.OrderViViService;

/**
 * Integration tests for the {@link OrderViViResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OrderViViResourceIT {

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_PRICE = new BigDecimal(1 - 1);

    private static final OrderViViStatus DEFAULT_STATUS = OrderViViStatus.PENDING;
    private static final OrderViViStatus UPDATED_STATUS = OrderViViStatus.SHIPPED;

    private static final String ENTITY_API_URL = "/api/order-vi-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrderViViRepository orderViViRepository;

    @Mock
    private OrderViViRepository orderViViRepositoryMock;

    @Mock
    private OrderViViService orderViViServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderViViMockMvc;

    private OrderViVi orderViVi;

    private OrderViVi insertedOrderViVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderViVi createEntity() {
        return new OrderViVi().orderDate(DEFAULT_ORDER_DATE).totalPrice(DEFAULT_TOTAL_PRICE).status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderViVi createUpdatedEntity() {
        return new OrderViVi().orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        orderViVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedOrderViVi != null) {
            orderViViRepository.delete(insertedOrderViVi);
            insertedOrderViVi = null;
        }
    }

    @Test
    @Transactional
    void createOrderViVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OrderViVi
        var returnedOrderViVi = om.readValue(
            restOrderViViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderViVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OrderViVi.class
        );

        // Validate the OrderViVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertOrderViViUpdatableFieldsEquals(returnedOrderViVi, getPersistedOrderViVi(returnedOrderViVi));

        insertedOrderViVi = returnedOrderViVi;
    }

    @Test
    @Transactional
    void createOrderViViWithExistingId() throws Exception {
        // Create the OrderViVi with an existing ID
        orderViVi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderViVi)))
            .andExpect(status().isBadRequest());

        // Validate the OrderViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderViVi.setOrderDate(null);

        // Create the OrderViVi, which fails.

        restOrderViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderViVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderViVi.setTotalPrice(null);

        // Create the OrderViVi, which fails.

        restOrderViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderViVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrderViVis() throws Exception {
        // Initialize the database
        insertedOrderViVi = orderViViRepository.saveAndFlush(orderViVi);

        // Get all the orderViViList
        restOrderViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderViVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderViVisWithEagerRelationshipsIsEnabled() throws Exception {
        when(orderViViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderViViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(orderViViServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderViVisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(orderViViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderViViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(orderViViRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getOrderViVi() throws Exception {
        // Initialize the database
        insertedOrderViVi = orderViViRepository.saveAndFlush(orderViVi);

        // Get the orderViVi
        restOrderViViMockMvc
            .perform(get(ENTITY_API_URL_ID, orderViVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderViVi.getId().intValue()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getOrderViVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedOrderViVi = orderViViRepository.saveAndFlush(orderViVi);

        Long id = orderViVi.getId();

        defaultOrderViViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOrderViViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOrderViViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrderViVisByOrderDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderViVi = orderViViRepository.saveAndFlush(orderViVi);

        // Get all the orderViViList where orderDate equals to
        defaultOrderViViFiltering("orderDate.equals=" + DEFAULT_ORDER_DATE, "orderDate.equals=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllOrderViVisByOrderDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderViVi = orderViViRepository.saveAndFlush(orderViVi);

        // Get all the orderViViList where orderDate in
        defaultOrderViViFiltering("orderDate.in=" + DEFAULT_ORDER_DATE + "," + UPDATED_ORDER_DATE, "orderDate.in=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllOrderViVisByOrderDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderViVi = orderViViRepository.saveAndFlush(orderViVi);

        // Get all the orderViViList where orderDate is not null
        defaultOrderViViFiltering("orderDate.specified=true", "orderDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderViVisByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderViVi = orderViViRepository.saveAndFlush(orderViVi);

        // Get all the orderViViList where totalPrice equals to
        defaultOrderViViFiltering("totalPrice.equals=" + DEFAULT_TOTAL_PRICE, "totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderViVisByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderViVi = orderViViRepository.saveAndFlush(orderViVi);

        // Get all the orderViViList where totalPrice in
        defaultOrderViViFiltering(
            "totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE,
            "totalPrice.in=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllOrderViVisByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderViVi = orderViViRepository.saveAndFlush(orderViVi);

        // Get all the orderViViList where totalPrice is not null
        defaultOrderViViFiltering("totalPrice.specified=true", "totalPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderViVisByTotalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderViVi = orderViViRepository.saveAndFlush(orderViVi);

        // Get all the orderViViList where totalPrice is greater than or equal to
        defaultOrderViViFiltering(
            "totalPrice.greaterThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.greaterThanOrEqual=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllOrderViVisByTotalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderViVi = orderViViRepository.saveAndFlush(orderViVi);

        // Get all the orderViViList where totalPrice is less than or equal to
        defaultOrderViViFiltering("totalPrice.lessThanOrEqual=" + DEFAULT_TOTAL_PRICE, "totalPrice.lessThanOrEqual=" + SMALLER_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderViVisByTotalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrderViVi = orderViViRepository.saveAndFlush(orderViVi);

        // Get all the orderViViList where totalPrice is less than
        defaultOrderViViFiltering("totalPrice.lessThan=" + UPDATED_TOTAL_PRICE, "totalPrice.lessThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderViVisByTotalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrderViVi = orderViViRepository.saveAndFlush(orderViVi);

        // Get all the orderViViList where totalPrice is greater than
        defaultOrderViViFiltering("totalPrice.greaterThan=" + SMALLER_TOTAL_PRICE, "totalPrice.greaterThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderViVisByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderViVi = orderViViRepository.saveAndFlush(orderViVi);

        // Get all the orderViViList where status equals to
        defaultOrderViViFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOrderViVisByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderViVi = orderViViRepository.saveAndFlush(orderViVi);

        // Get all the orderViViList where status in
        defaultOrderViViFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOrderViVisByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderViVi = orderViViRepository.saveAndFlush(orderViVi);

        // Get all the orderViViList where status is not null
        defaultOrderViViFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderViVisByPaymentIsEqualToSomething() throws Exception {
        PaymentViVi payment;
        if (TestUtil.findAll(em, PaymentViVi.class).isEmpty()) {
            orderViViRepository.saveAndFlush(orderViVi);
            payment = PaymentViViResourceIT.createEntity();
        } else {
            payment = TestUtil.findAll(em, PaymentViVi.class).get(0);
        }
        em.persist(payment);
        em.flush();
        orderViVi.setPayment(payment);
        orderViViRepository.saveAndFlush(orderViVi);
        Long paymentId = payment.getId();
        // Get all the orderViViList where payment equals to paymentId
        defaultOrderViViShouldBeFound("paymentId.equals=" + paymentId);

        // Get all the orderViViList where payment equals to (paymentId + 1)
        defaultOrderViViShouldNotBeFound("paymentId.equals=" + (paymentId + 1));
    }

    @Test
    @Transactional
    void getAllOrderViVisByShipmentIsEqualToSomething() throws Exception {
        ShipmentViVi shipment;
        if (TestUtil.findAll(em, ShipmentViVi.class).isEmpty()) {
            orderViViRepository.saveAndFlush(orderViVi);
            shipment = ShipmentViViResourceIT.createEntity();
        } else {
            shipment = TestUtil.findAll(em, ShipmentViVi.class).get(0);
        }
        em.persist(shipment);
        em.flush();
        orderViVi.setShipment(shipment);
        orderViViRepository.saveAndFlush(orderViVi);
        Long shipmentId = shipment.getId();
        // Get all the orderViViList where shipment equals to shipmentId
        defaultOrderViViShouldBeFound("shipmentId.equals=" + shipmentId);

        // Get all the orderViViList where shipment equals to (shipmentId + 1)
        defaultOrderViViShouldNotBeFound("shipmentId.equals=" + (shipmentId + 1));
    }

    @Test
    @Transactional
    void getAllOrderViVisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            orderViViRepository.saveAndFlush(orderViVi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        orderViVi.setTenant(tenant);
        orderViViRepository.saveAndFlush(orderViVi);
        Long tenantId = tenant.getId();
        // Get all the orderViViList where tenant equals to tenantId
        defaultOrderViViShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the orderViViList where tenant equals to (tenantId + 1)
        defaultOrderViViShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllOrderViVisByCustomerIsEqualToSomething() throws Exception {
        CustomerViVi customer;
        if (TestUtil.findAll(em, CustomerViVi.class).isEmpty()) {
            orderViViRepository.saveAndFlush(orderViVi);
            customer = CustomerViViResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, CustomerViVi.class).get(0);
        }
        em.persist(customer);
        em.flush();
        orderViVi.setCustomer(customer);
        orderViViRepository.saveAndFlush(orderViVi);
        Long customerId = customer.getId();
        // Get all the orderViViList where customer equals to customerId
        defaultOrderViViShouldBeFound("customerId.equals=" + customerId);

        // Get all the orderViViList where customer equals to (customerId + 1)
        defaultOrderViViShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    private void defaultOrderViViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOrderViViShouldBeFound(shouldBeFound);
        defaultOrderViViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderViViShouldBeFound(String filter) throws Exception {
        restOrderViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderViVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restOrderViViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderViViShouldNotBeFound(String filter) throws Exception {
        restOrderViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderViViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrderViVi() throws Exception {
        // Get the orderViVi
        restOrderViViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderViVi() throws Exception {
        // Initialize the database
        insertedOrderViVi = orderViViRepository.saveAndFlush(orderViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderViVi
        OrderViVi updatedOrderViVi = orderViViRepository.findById(orderViVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrderViVi are not directly saved in db
        em.detach(updatedOrderViVi);
        updatedOrderViVi.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);

        restOrderViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrderViVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedOrderViVi))
            )
            .andExpect(status().isOk());

        // Validate the OrderViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrderViViToMatchAllProperties(updatedOrderViVi);
    }

    @Test
    @Transactional
    void putNonExistingOrderViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderViVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderViVi.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderViVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderViVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderViVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderViVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderViViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderViVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderViViWithPatch() throws Exception {
        // Initialize the database
        insertedOrderViVi = orderViViRepository.saveAndFlush(orderViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderViVi using partial update
        OrderViVi partialUpdatedOrderViVi = new OrderViVi();
        partialUpdatedOrderViVi.setId(orderViVi.getId());

        partialUpdatedOrderViVi.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);

        restOrderViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderViVi))
            )
            .andExpect(status().isOk());

        // Validate the OrderViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderViViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOrderViVi, orderViVi),
            getPersistedOrderViVi(orderViVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateOrderViViWithPatch() throws Exception {
        // Initialize the database
        insertedOrderViVi = orderViViRepository.saveAndFlush(orderViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderViVi using partial update
        OrderViVi partialUpdatedOrderViVi = new OrderViVi();
        partialUpdatedOrderViVi.setId(orderViVi.getId());

        partialUpdatedOrderViVi.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);

        restOrderViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderViVi))
            )
            .andExpect(status().isOk());

        // Validate the OrderViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderViViUpdatableFieldsEquals(partialUpdatedOrderViVi, getPersistedOrderViVi(partialUpdatedOrderViVi));
    }

    @Test
    @Transactional
    void patchNonExistingOrderViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderViVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderViVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderViVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderViVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderViVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderViViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(orderViVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderViVi() throws Exception {
        // Initialize the database
        insertedOrderViVi = orderViViRepository.saveAndFlush(orderViVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the orderViVi
        restOrderViViMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderViVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return orderViViRepository.count();
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

    protected OrderViVi getPersistedOrderViVi(OrderViVi orderViVi) {
        return orderViViRepository.findById(orderViVi.getId()).orElseThrow();
    }

    protected void assertPersistedOrderViViToMatchAllProperties(OrderViVi expectedOrderViVi) {
        assertOrderViViAllPropertiesEquals(expectedOrderViVi, getPersistedOrderViVi(expectedOrderViVi));
    }

    protected void assertPersistedOrderViViToMatchUpdatableProperties(OrderViVi expectedOrderViVi) {
        assertOrderViViAllUpdatablePropertiesEquals(expectedOrderViVi, getPersistedOrderViVi(expectedOrderViVi));
    }
}
