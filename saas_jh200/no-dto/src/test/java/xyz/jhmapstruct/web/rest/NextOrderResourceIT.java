package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextOrderAsserts.*;
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
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCustomer;
import xyz.jhmapstruct.domain.NextOrder;
import xyz.jhmapstruct.domain.NextPayment;
import xyz.jhmapstruct.domain.NextShipment;
import xyz.jhmapstruct.domain.enumeration.OrderStatus;
import xyz.jhmapstruct.repository.NextOrderRepository;
import xyz.jhmapstruct.service.NextOrderService;

/**
 * Integration tests for the {@link NextOrderResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextOrderResourceIT {

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_PRICE = new BigDecimal(1 - 1);

    private static final OrderStatus DEFAULT_STATUS = OrderStatus.PENDING;
    private static final OrderStatus UPDATED_STATUS = OrderStatus.SHIPPED;

    private static final String ENTITY_API_URL = "/api/next-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextOrderRepository nextOrderRepository;

    @Mock
    private NextOrderRepository nextOrderRepositoryMock;

    @Mock
    private NextOrderService nextOrderServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextOrderMockMvc;

    private NextOrder nextOrder;

    private NextOrder insertedNextOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextOrder createEntity() {
        return new NextOrder().orderDate(DEFAULT_ORDER_DATE).totalPrice(DEFAULT_TOTAL_PRICE).status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextOrder createUpdatedEntity() {
        return new NextOrder().orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        nextOrder = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextOrder != null) {
            nextOrderRepository.delete(insertedNextOrder);
            insertedNextOrder = null;
        }
    }

    @Test
    @Transactional
    void createNextOrder() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextOrder
        var returnedNextOrder = om.readValue(
            restNextOrderMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrder)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextOrder.class
        );

        // Validate the NextOrder in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextOrderUpdatableFieldsEquals(returnedNextOrder, getPersistedNextOrder(returnedNextOrder));

        insertedNextOrder = returnedNextOrder;
    }

    @Test
    @Transactional
    void createNextOrderWithExistingId() throws Exception {
        // Create the NextOrder with an existing ID
        nextOrder.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrder)))
            .andExpect(status().isBadRequest());

        // Validate the NextOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextOrder.setOrderDate(null);

        // Create the NextOrder, which fails.

        restNextOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrder)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextOrder.setTotalPrice(null);

        // Create the NextOrder, which fails.

        restNextOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrder)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextOrders() throws Exception {
        // Initialize the database
        insertedNextOrder = nextOrderRepository.saveAndFlush(nextOrder);

        // Get all the nextOrderList
        restNextOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextOrdersWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextOrderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextOrderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextOrderServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextOrdersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextOrderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextOrderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextOrderRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextOrder() throws Exception {
        // Initialize the database
        insertedNextOrder = nextOrderRepository.saveAndFlush(nextOrder);

        // Get the nextOrder
        restNextOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, nextOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextOrder.getId().intValue()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNextOrdersByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextOrder = nextOrderRepository.saveAndFlush(nextOrder);

        Long id = nextOrder.getId();

        defaultNextOrderFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextOrderFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextOrderFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextOrdersByOrderDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrder = nextOrderRepository.saveAndFlush(nextOrder);

        // Get all the nextOrderList where orderDate equals to
        defaultNextOrderFiltering("orderDate.equals=" + DEFAULT_ORDER_DATE, "orderDate.equals=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllNextOrdersByOrderDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextOrder = nextOrderRepository.saveAndFlush(nextOrder);

        // Get all the nextOrderList where orderDate in
        defaultNextOrderFiltering("orderDate.in=" + DEFAULT_ORDER_DATE + "," + UPDATED_ORDER_DATE, "orderDate.in=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllNextOrdersByOrderDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextOrder = nextOrderRepository.saveAndFlush(nextOrder);

        // Get all the nextOrderList where orderDate is not null
        defaultNextOrderFiltering("orderDate.specified=true", "orderDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextOrdersByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrder = nextOrderRepository.saveAndFlush(nextOrder);

        // Get all the nextOrderList where totalPrice equals to
        defaultNextOrderFiltering("totalPrice.equals=" + DEFAULT_TOTAL_PRICE, "totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllNextOrdersByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextOrder = nextOrderRepository.saveAndFlush(nextOrder);

        // Get all the nextOrderList where totalPrice in
        defaultNextOrderFiltering(
            "totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE,
            "totalPrice.in=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllNextOrdersByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextOrder = nextOrderRepository.saveAndFlush(nextOrder);

        // Get all the nextOrderList where totalPrice is not null
        defaultNextOrderFiltering("totalPrice.specified=true", "totalPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllNextOrdersByTotalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrder = nextOrderRepository.saveAndFlush(nextOrder);

        // Get all the nextOrderList where totalPrice is greater than or equal to
        defaultNextOrderFiltering(
            "totalPrice.greaterThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.greaterThanOrEqual=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllNextOrdersByTotalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrder = nextOrderRepository.saveAndFlush(nextOrder);

        // Get all the nextOrderList where totalPrice is less than or equal to
        defaultNextOrderFiltering("totalPrice.lessThanOrEqual=" + DEFAULT_TOTAL_PRICE, "totalPrice.lessThanOrEqual=" + SMALLER_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllNextOrdersByTotalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextOrder = nextOrderRepository.saveAndFlush(nextOrder);

        // Get all the nextOrderList where totalPrice is less than
        defaultNextOrderFiltering("totalPrice.lessThan=" + UPDATED_TOTAL_PRICE, "totalPrice.lessThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllNextOrdersByTotalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextOrder = nextOrderRepository.saveAndFlush(nextOrder);

        // Get all the nextOrderList where totalPrice is greater than
        defaultNextOrderFiltering("totalPrice.greaterThan=" + SMALLER_TOTAL_PRICE, "totalPrice.greaterThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllNextOrdersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrder = nextOrderRepository.saveAndFlush(nextOrder);

        // Get all the nextOrderList where status equals to
        defaultNextOrderFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllNextOrdersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextOrder = nextOrderRepository.saveAndFlush(nextOrder);

        // Get all the nextOrderList where status in
        defaultNextOrderFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllNextOrdersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextOrder = nextOrderRepository.saveAndFlush(nextOrder);

        // Get all the nextOrderList where status is not null
        defaultNextOrderFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllNextOrdersByPaymentIsEqualToSomething() throws Exception {
        NextPayment payment;
        if (TestUtil.findAll(em, NextPayment.class).isEmpty()) {
            nextOrderRepository.saveAndFlush(nextOrder);
            payment = NextPaymentResourceIT.createEntity();
        } else {
            payment = TestUtil.findAll(em, NextPayment.class).get(0);
        }
        em.persist(payment);
        em.flush();
        nextOrder.setPayment(payment);
        nextOrderRepository.saveAndFlush(nextOrder);
        Long paymentId = payment.getId();
        // Get all the nextOrderList where payment equals to paymentId
        defaultNextOrderShouldBeFound("paymentId.equals=" + paymentId);

        // Get all the nextOrderList where payment equals to (paymentId + 1)
        defaultNextOrderShouldNotBeFound("paymentId.equals=" + (paymentId + 1));
    }

    @Test
    @Transactional
    void getAllNextOrdersByShipmentIsEqualToSomething() throws Exception {
        NextShipment shipment;
        if (TestUtil.findAll(em, NextShipment.class).isEmpty()) {
            nextOrderRepository.saveAndFlush(nextOrder);
            shipment = NextShipmentResourceIT.createEntity();
        } else {
            shipment = TestUtil.findAll(em, NextShipment.class).get(0);
        }
        em.persist(shipment);
        em.flush();
        nextOrder.setShipment(shipment);
        nextOrderRepository.saveAndFlush(nextOrder);
        Long shipmentId = shipment.getId();
        // Get all the nextOrderList where shipment equals to shipmentId
        defaultNextOrderShouldBeFound("shipmentId.equals=" + shipmentId);

        // Get all the nextOrderList where shipment equals to (shipmentId + 1)
        defaultNextOrderShouldNotBeFound("shipmentId.equals=" + (shipmentId + 1));
    }

    @Test
    @Transactional
    void getAllNextOrdersByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextOrderRepository.saveAndFlush(nextOrder);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextOrder.setTenant(tenant);
        nextOrderRepository.saveAndFlush(nextOrder);
        Long tenantId = tenant.getId();
        // Get all the nextOrderList where tenant equals to tenantId
        defaultNextOrderShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextOrderList where tenant equals to (tenantId + 1)
        defaultNextOrderShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllNextOrdersByCustomerIsEqualToSomething() throws Exception {
        NextCustomer customer;
        if (TestUtil.findAll(em, NextCustomer.class).isEmpty()) {
            nextOrderRepository.saveAndFlush(nextOrder);
            customer = NextCustomerResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, NextCustomer.class).get(0);
        }
        em.persist(customer);
        em.flush();
        nextOrder.setCustomer(customer);
        nextOrderRepository.saveAndFlush(nextOrder);
        Long customerId = customer.getId();
        // Get all the nextOrderList where customer equals to customerId
        defaultNextOrderShouldBeFound("customerId.equals=" + customerId);

        // Get all the nextOrderList where customer equals to (customerId + 1)
        defaultNextOrderShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    private void defaultNextOrderFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextOrderShouldBeFound(shouldBeFound);
        defaultNextOrderShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextOrderShouldBeFound(String filter) throws Exception {
        restNextOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restNextOrderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextOrderShouldNotBeFound(String filter) throws Exception {
        restNextOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextOrderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextOrder() throws Exception {
        // Get the nextOrder
        restNextOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextOrder() throws Exception {
        // Initialize the database
        insertedNextOrder = nextOrderRepository.saveAndFlush(nextOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextOrder
        NextOrder updatedNextOrder = nextOrderRepository.findById(nextOrder.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextOrder are not directly saved in db
        em.detach(updatedNextOrder);
        updatedNextOrder.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);

        restNextOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextOrder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextOrder))
            )
            .andExpect(status().isOk());

        // Validate the NextOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextOrderToMatchAllProperties(updatedNextOrder);
    }

    @Test
    @Transactional
    void putNonExistingNextOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrder.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextOrder.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrder.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrder.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrder)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextOrderWithPatch() throws Exception {
        // Initialize the database
        insertedNextOrder = nextOrderRepository.saveAndFlush(nextOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextOrder using partial update
        NextOrder partialUpdatedNextOrder = new NextOrder();
        partialUpdatedNextOrder.setId(nextOrder.getId());

        partialUpdatedNextOrder.orderDate(UPDATED_ORDER_DATE);

        restNextOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextOrder))
            )
            .andExpect(status().isOk());

        // Validate the NextOrder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextOrderUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextOrder, nextOrder),
            getPersistedNextOrder(nextOrder)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextOrderWithPatch() throws Exception {
        // Initialize the database
        insertedNextOrder = nextOrderRepository.saveAndFlush(nextOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextOrder using partial update
        NextOrder partialUpdatedNextOrder = new NextOrder();
        partialUpdatedNextOrder.setId(nextOrder.getId());

        partialUpdatedNextOrder.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);

        restNextOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextOrder))
            )
            .andExpect(status().isOk());

        // Validate the NextOrder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextOrderUpdatableFieldsEquals(partialUpdatedNextOrder, getPersistedNextOrder(partialUpdatedNextOrder));
    }

    @Test
    @Transactional
    void patchNonExistingNextOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrder.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrder.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrder.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextOrder)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextOrder() throws Exception {
        // Initialize the database
        insertedNextOrder = nextOrderRepository.saveAndFlush(nextOrder);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextOrder
        restNextOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextOrder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextOrderRepository.count();
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

    protected NextOrder getPersistedNextOrder(NextOrder nextOrder) {
        return nextOrderRepository.findById(nextOrder.getId()).orElseThrow();
    }

    protected void assertPersistedNextOrderToMatchAllProperties(NextOrder expectedNextOrder) {
        assertNextOrderAllPropertiesEquals(expectedNextOrder, getPersistedNextOrder(expectedNextOrder));
    }

    protected void assertPersistedNextOrderToMatchUpdatableProperties(NextOrder expectedNextOrder) {
        assertNextOrderAllUpdatablePropertiesEquals(expectedNextOrder, getPersistedNextOrder(expectedNextOrder));
    }
}
