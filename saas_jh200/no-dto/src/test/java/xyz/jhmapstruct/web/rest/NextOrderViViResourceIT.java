package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextOrderViViAsserts.*;
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
import xyz.jhmapstruct.domain.NextCustomerViVi;
import xyz.jhmapstruct.domain.NextOrderViVi;
import xyz.jhmapstruct.domain.NextPaymentViVi;
import xyz.jhmapstruct.domain.NextShipmentViVi;
import xyz.jhmapstruct.domain.enumeration.OrderViViStatus;
import xyz.jhmapstruct.repository.NextOrderViViRepository;
import xyz.jhmapstruct.service.NextOrderViViService;

/**
 * Integration tests for the {@link NextOrderViViResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextOrderViViResourceIT {

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_PRICE = new BigDecimal(1 - 1);

    private static final OrderViViStatus DEFAULT_STATUS = OrderViViStatus.PENDING;
    private static final OrderViViStatus UPDATED_STATUS = OrderViViStatus.SHIPPED;

    private static final String ENTITY_API_URL = "/api/next-order-vi-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextOrderViViRepository nextOrderViViRepository;

    @Mock
    private NextOrderViViRepository nextOrderViViRepositoryMock;

    @Mock
    private NextOrderViViService nextOrderViViServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextOrderViViMockMvc;

    private NextOrderViVi nextOrderViVi;

    private NextOrderViVi insertedNextOrderViVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextOrderViVi createEntity() {
        return new NextOrderViVi().orderDate(DEFAULT_ORDER_DATE).totalPrice(DEFAULT_TOTAL_PRICE).status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextOrderViVi createUpdatedEntity() {
        return new NextOrderViVi().orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        nextOrderViVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextOrderViVi != null) {
            nextOrderViViRepository.delete(insertedNextOrderViVi);
            insertedNextOrderViVi = null;
        }
    }

    @Test
    @Transactional
    void createNextOrderViVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextOrderViVi
        var returnedNextOrderViVi = om.readValue(
            restNextOrderViViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderViVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextOrderViVi.class
        );

        // Validate the NextOrderViVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextOrderViViUpdatableFieldsEquals(returnedNextOrderViVi, getPersistedNextOrderViVi(returnedNextOrderViVi));

        insertedNextOrderViVi = returnedNextOrderViVi;
    }

    @Test
    @Transactional
    void createNextOrderViViWithExistingId() throws Exception {
        // Create the NextOrderViVi with an existing ID
        nextOrderViVi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextOrderViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderViVi)))
            .andExpect(status().isBadRequest());

        // Validate the NextOrderViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextOrderViVi.setOrderDate(null);

        // Create the NextOrderViVi, which fails.

        restNextOrderViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderViVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextOrderViVi.setTotalPrice(null);

        // Create the NextOrderViVi, which fails.

        restNextOrderViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderViVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextOrderViVis() throws Exception {
        // Initialize the database
        insertedNextOrderViVi = nextOrderViViRepository.saveAndFlush(nextOrderViVi);

        // Get all the nextOrderViViList
        restNextOrderViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextOrderViVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextOrderViVisWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextOrderViViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextOrderViViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextOrderViViServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextOrderViVisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextOrderViViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextOrderViViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextOrderViViRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextOrderViVi() throws Exception {
        // Initialize the database
        insertedNextOrderViVi = nextOrderViViRepository.saveAndFlush(nextOrderViVi);

        // Get the nextOrderViVi
        restNextOrderViViMockMvc
            .perform(get(ENTITY_API_URL_ID, nextOrderViVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextOrderViVi.getId().intValue()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNextOrderViVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextOrderViVi = nextOrderViViRepository.saveAndFlush(nextOrderViVi);

        Long id = nextOrderViVi.getId();

        defaultNextOrderViViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextOrderViViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextOrderViViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextOrderViVisByOrderDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderViVi = nextOrderViViRepository.saveAndFlush(nextOrderViVi);

        // Get all the nextOrderViViList where orderDate equals to
        defaultNextOrderViViFiltering("orderDate.equals=" + DEFAULT_ORDER_DATE, "orderDate.equals=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllNextOrderViVisByOrderDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextOrderViVi = nextOrderViViRepository.saveAndFlush(nextOrderViVi);

        // Get all the nextOrderViViList where orderDate in
        defaultNextOrderViViFiltering(
            "orderDate.in=" + DEFAULT_ORDER_DATE + "," + UPDATED_ORDER_DATE,
            "orderDate.in=" + UPDATED_ORDER_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextOrderViVisByOrderDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextOrderViVi = nextOrderViViRepository.saveAndFlush(nextOrderViVi);

        // Get all the nextOrderViViList where orderDate is not null
        defaultNextOrderViViFiltering("orderDate.specified=true", "orderDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextOrderViVisByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderViVi = nextOrderViViRepository.saveAndFlush(nextOrderViVi);

        // Get all the nextOrderViViList where totalPrice equals to
        defaultNextOrderViViFiltering("totalPrice.equals=" + DEFAULT_TOTAL_PRICE, "totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllNextOrderViVisByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextOrderViVi = nextOrderViViRepository.saveAndFlush(nextOrderViVi);

        // Get all the nextOrderViViList where totalPrice in
        defaultNextOrderViViFiltering(
            "totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE,
            "totalPrice.in=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllNextOrderViVisByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextOrderViVi = nextOrderViViRepository.saveAndFlush(nextOrderViVi);

        // Get all the nextOrderViViList where totalPrice is not null
        defaultNextOrderViViFiltering("totalPrice.specified=true", "totalPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllNextOrderViVisByTotalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderViVi = nextOrderViViRepository.saveAndFlush(nextOrderViVi);

        // Get all the nextOrderViViList where totalPrice is greater than or equal to
        defaultNextOrderViViFiltering(
            "totalPrice.greaterThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.greaterThanOrEqual=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllNextOrderViVisByTotalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderViVi = nextOrderViViRepository.saveAndFlush(nextOrderViVi);

        // Get all the nextOrderViViList where totalPrice is less than or equal to
        defaultNextOrderViViFiltering(
            "totalPrice.lessThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.lessThanOrEqual=" + SMALLER_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllNextOrderViVisByTotalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextOrderViVi = nextOrderViViRepository.saveAndFlush(nextOrderViVi);

        // Get all the nextOrderViViList where totalPrice is less than
        defaultNextOrderViViFiltering("totalPrice.lessThan=" + UPDATED_TOTAL_PRICE, "totalPrice.lessThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllNextOrderViVisByTotalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextOrderViVi = nextOrderViViRepository.saveAndFlush(nextOrderViVi);

        // Get all the nextOrderViViList where totalPrice is greater than
        defaultNextOrderViViFiltering("totalPrice.greaterThan=" + SMALLER_TOTAL_PRICE, "totalPrice.greaterThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllNextOrderViVisByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderViVi = nextOrderViViRepository.saveAndFlush(nextOrderViVi);

        // Get all the nextOrderViViList where status equals to
        defaultNextOrderViViFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllNextOrderViVisByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextOrderViVi = nextOrderViViRepository.saveAndFlush(nextOrderViVi);

        // Get all the nextOrderViViList where status in
        defaultNextOrderViViFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllNextOrderViVisByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextOrderViVi = nextOrderViViRepository.saveAndFlush(nextOrderViVi);

        // Get all the nextOrderViViList where status is not null
        defaultNextOrderViViFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllNextOrderViVisByPaymentIsEqualToSomething() throws Exception {
        NextPaymentViVi payment;
        if (TestUtil.findAll(em, NextPaymentViVi.class).isEmpty()) {
            nextOrderViViRepository.saveAndFlush(nextOrderViVi);
            payment = NextPaymentViViResourceIT.createEntity();
        } else {
            payment = TestUtil.findAll(em, NextPaymentViVi.class).get(0);
        }
        em.persist(payment);
        em.flush();
        nextOrderViVi.setPayment(payment);
        nextOrderViViRepository.saveAndFlush(nextOrderViVi);
        Long paymentId = payment.getId();
        // Get all the nextOrderViViList where payment equals to paymentId
        defaultNextOrderViViShouldBeFound("paymentId.equals=" + paymentId);

        // Get all the nextOrderViViList where payment equals to (paymentId + 1)
        defaultNextOrderViViShouldNotBeFound("paymentId.equals=" + (paymentId + 1));
    }

    @Test
    @Transactional
    void getAllNextOrderViVisByShipmentIsEqualToSomething() throws Exception {
        NextShipmentViVi shipment;
        if (TestUtil.findAll(em, NextShipmentViVi.class).isEmpty()) {
            nextOrderViViRepository.saveAndFlush(nextOrderViVi);
            shipment = NextShipmentViViResourceIT.createEntity();
        } else {
            shipment = TestUtil.findAll(em, NextShipmentViVi.class).get(0);
        }
        em.persist(shipment);
        em.flush();
        nextOrderViVi.setShipment(shipment);
        nextOrderViViRepository.saveAndFlush(nextOrderViVi);
        Long shipmentId = shipment.getId();
        // Get all the nextOrderViViList where shipment equals to shipmentId
        defaultNextOrderViViShouldBeFound("shipmentId.equals=" + shipmentId);

        // Get all the nextOrderViViList where shipment equals to (shipmentId + 1)
        defaultNextOrderViViShouldNotBeFound("shipmentId.equals=" + (shipmentId + 1));
    }

    @Test
    @Transactional
    void getAllNextOrderViVisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextOrderViViRepository.saveAndFlush(nextOrderViVi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextOrderViVi.setTenant(tenant);
        nextOrderViViRepository.saveAndFlush(nextOrderViVi);
        Long tenantId = tenant.getId();
        // Get all the nextOrderViViList where tenant equals to tenantId
        defaultNextOrderViViShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextOrderViViList where tenant equals to (tenantId + 1)
        defaultNextOrderViViShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllNextOrderViVisByCustomerIsEqualToSomething() throws Exception {
        NextCustomerViVi customer;
        if (TestUtil.findAll(em, NextCustomerViVi.class).isEmpty()) {
            nextOrderViViRepository.saveAndFlush(nextOrderViVi);
            customer = NextCustomerViViResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, NextCustomerViVi.class).get(0);
        }
        em.persist(customer);
        em.flush();
        nextOrderViVi.setCustomer(customer);
        nextOrderViViRepository.saveAndFlush(nextOrderViVi);
        Long customerId = customer.getId();
        // Get all the nextOrderViViList where customer equals to customerId
        defaultNextOrderViViShouldBeFound("customerId.equals=" + customerId);

        // Get all the nextOrderViViList where customer equals to (customerId + 1)
        defaultNextOrderViViShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    private void defaultNextOrderViViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextOrderViViShouldBeFound(shouldBeFound);
        defaultNextOrderViViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextOrderViViShouldBeFound(String filter) throws Exception {
        restNextOrderViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextOrderViVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restNextOrderViViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextOrderViViShouldNotBeFound(String filter) throws Exception {
        restNextOrderViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextOrderViViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextOrderViVi() throws Exception {
        // Get the nextOrderViVi
        restNextOrderViViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextOrderViVi() throws Exception {
        // Initialize the database
        insertedNextOrderViVi = nextOrderViViRepository.saveAndFlush(nextOrderViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextOrderViVi
        NextOrderViVi updatedNextOrderViVi = nextOrderViViRepository.findById(nextOrderViVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextOrderViVi are not directly saved in db
        em.detach(updatedNextOrderViVi);
        updatedNextOrderViVi.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);

        restNextOrderViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextOrderViVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextOrderViVi))
            )
            .andExpect(status().isOk());

        // Validate the NextOrderViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextOrderViViToMatchAllProperties(updatedNextOrderViVi);
    }

    @Test
    @Transactional
    void putNonExistingNextOrderViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderViVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextOrderViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextOrderViVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextOrderViVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrderViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextOrderViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderViVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextOrderViVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrderViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextOrderViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderViVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderViViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderViVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextOrderViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextOrderViViWithPatch() throws Exception {
        // Initialize the database
        insertedNextOrderViVi = nextOrderViViRepository.saveAndFlush(nextOrderViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextOrderViVi using partial update
        NextOrderViVi partialUpdatedNextOrderViVi = new NextOrderViVi();
        partialUpdatedNextOrderViVi.setId(nextOrderViVi.getId());

        partialUpdatedNextOrderViVi.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);

        restNextOrderViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextOrderViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextOrderViVi))
            )
            .andExpect(status().isOk());

        // Validate the NextOrderViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextOrderViViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextOrderViVi, nextOrderViVi),
            getPersistedNextOrderViVi(nextOrderViVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextOrderViViWithPatch() throws Exception {
        // Initialize the database
        insertedNextOrderViVi = nextOrderViViRepository.saveAndFlush(nextOrderViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextOrderViVi using partial update
        NextOrderViVi partialUpdatedNextOrderViVi = new NextOrderViVi();
        partialUpdatedNextOrderViVi.setId(nextOrderViVi.getId());

        partialUpdatedNextOrderViVi.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);

        restNextOrderViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextOrderViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextOrderViVi))
            )
            .andExpect(status().isOk());

        // Validate the NextOrderViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextOrderViViUpdatableFieldsEquals(partialUpdatedNextOrderViVi, getPersistedNextOrderViVi(partialUpdatedNextOrderViVi));
    }

    @Test
    @Transactional
    void patchNonExistingNextOrderViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderViVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextOrderViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextOrderViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextOrderViVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrderViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextOrderViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderViVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextOrderViVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrderViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextOrderViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderViVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderViViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextOrderViVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextOrderViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextOrderViVi() throws Exception {
        // Initialize the database
        insertedNextOrderViVi = nextOrderViViRepository.saveAndFlush(nextOrderViVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextOrderViVi
        restNextOrderViViMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextOrderViVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextOrderViViRepository.count();
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

    protected NextOrderViVi getPersistedNextOrderViVi(NextOrderViVi nextOrderViVi) {
        return nextOrderViViRepository.findById(nextOrderViVi.getId()).orElseThrow();
    }

    protected void assertPersistedNextOrderViViToMatchAllProperties(NextOrderViVi expectedNextOrderViVi) {
        assertNextOrderViViAllPropertiesEquals(expectedNextOrderViVi, getPersistedNextOrderViVi(expectedNextOrderViVi));
    }

    protected void assertPersistedNextOrderViViToMatchUpdatableProperties(NextOrderViVi expectedNextOrderViVi) {
        assertNextOrderViViAllUpdatablePropertiesEquals(expectedNextOrderViVi, getPersistedNextOrderViVi(expectedNextOrderViVi));
    }
}
