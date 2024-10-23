package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextOrderSigmaAsserts.*;
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
import xyz.jhmapstruct.domain.NextCustomerSigma;
import xyz.jhmapstruct.domain.NextOrderSigma;
import xyz.jhmapstruct.domain.NextPaymentSigma;
import xyz.jhmapstruct.domain.NextShipmentSigma;
import xyz.jhmapstruct.domain.enumeration.OrderSigmaStatus;
import xyz.jhmapstruct.repository.NextOrderSigmaRepository;
import xyz.jhmapstruct.service.NextOrderSigmaService;

/**
 * Integration tests for the {@link NextOrderSigmaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextOrderSigmaResourceIT {

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_PRICE = new BigDecimal(1 - 1);

    private static final OrderSigmaStatus DEFAULT_STATUS = OrderSigmaStatus.PENDING;
    private static final OrderSigmaStatus UPDATED_STATUS = OrderSigmaStatus.SHIPPED;

    private static final String ENTITY_API_URL = "/api/next-order-sigmas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextOrderSigmaRepository nextOrderSigmaRepository;

    @Mock
    private NextOrderSigmaRepository nextOrderSigmaRepositoryMock;

    @Mock
    private NextOrderSigmaService nextOrderSigmaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextOrderSigmaMockMvc;

    private NextOrderSigma nextOrderSigma;

    private NextOrderSigma insertedNextOrderSigma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextOrderSigma createEntity() {
        return new NextOrderSigma().orderDate(DEFAULT_ORDER_DATE).totalPrice(DEFAULT_TOTAL_PRICE).status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextOrderSigma createUpdatedEntity() {
        return new NextOrderSigma().orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        nextOrderSigma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextOrderSigma != null) {
            nextOrderSigmaRepository.delete(insertedNextOrderSigma);
            insertedNextOrderSigma = null;
        }
    }

    @Test
    @Transactional
    void createNextOrderSigma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextOrderSigma
        var returnedNextOrderSigma = om.readValue(
            restNextOrderSigmaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderSigma)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextOrderSigma.class
        );

        // Validate the NextOrderSigma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextOrderSigmaUpdatableFieldsEquals(returnedNextOrderSigma, getPersistedNextOrderSigma(returnedNextOrderSigma));

        insertedNextOrderSigma = returnedNextOrderSigma;
    }

    @Test
    @Transactional
    void createNextOrderSigmaWithExistingId() throws Exception {
        // Create the NextOrderSigma with an existing ID
        nextOrderSigma.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextOrderSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderSigma)))
            .andExpect(status().isBadRequest());

        // Validate the NextOrderSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextOrderSigma.setOrderDate(null);

        // Create the NextOrderSigma, which fails.

        restNextOrderSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderSigma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextOrderSigma.setTotalPrice(null);

        // Create the NextOrderSigma, which fails.

        restNextOrderSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderSigma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextOrderSigmas() throws Exception {
        // Initialize the database
        insertedNextOrderSigma = nextOrderSigmaRepository.saveAndFlush(nextOrderSigma);

        // Get all the nextOrderSigmaList
        restNextOrderSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextOrderSigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextOrderSigmasWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextOrderSigmaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextOrderSigmaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextOrderSigmaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextOrderSigmasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextOrderSigmaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextOrderSigmaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextOrderSigmaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextOrderSigma() throws Exception {
        // Initialize the database
        insertedNextOrderSigma = nextOrderSigmaRepository.saveAndFlush(nextOrderSigma);

        // Get the nextOrderSigma
        restNextOrderSigmaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextOrderSigma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextOrderSigma.getId().intValue()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNextOrderSigmasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextOrderSigma = nextOrderSigmaRepository.saveAndFlush(nextOrderSigma);

        Long id = nextOrderSigma.getId();

        defaultNextOrderSigmaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextOrderSigmaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextOrderSigmaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextOrderSigmasByOrderDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderSigma = nextOrderSigmaRepository.saveAndFlush(nextOrderSigma);

        // Get all the nextOrderSigmaList where orderDate equals to
        defaultNextOrderSigmaFiltering("orderDate.equals=" + DEFAULT_ORDER_DATE, "orderDate.equals=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllNextOrderSigmasByOrderDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextOrderSigma = nextOrderSigmaRepository.saveAndFlush(nextOrderSigma);

        // Get all the nextOrderSigmaList where orderDate in
        defaultNextOrderSigmaFiltering(
            "orderDate.in=" + DEFAULT_ORDER_DATE + "," + UPDATED_ORDER_DATE,
            "orderDate.in=" + UPDATED_ORDER_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextOrderSigmasByOrderDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextOrderSigma = nextOrderSigmaRepository.saveAndFlush(nextOrderSigma);

        // Get all the nextOrderSigmaList where orderDate is not null
        defaultNextOrderSigmaFiltering("orderDate.specified=true", "orderDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextOrderSigmasByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderSigma = nextOrderSigmaRepository.saveAndFlush(nextOrderSigma);

        // Get all the nextOrderSigmaList where totalPrice equals to
        defaultNextOrderSigmaFiltering("totalPrice.equals=" + DEFAULT_TOTAL_PRICE, "totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllNextOrderSigmasByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextOrderSigma = nextOrderSigmaRepository.saveAndFlush(nextOrderSigma);

        // Get all the nextOrderSigmaList where totalPrice in
        defaultNextOrderSigmaFiltering(
            "totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE,
            "totalPrice.in=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllNextOrderSigmasByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextOrderSigma = nextOrderSigmaRepository.saveAndFlush(nextOrderSigma);

        // Get all the nextOrderSigmaList where totalPrice is not null
        defaultNextOrderSigmaFiltering("totalPrice.specified=true", "totalPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllNextOrderSigmasByTotalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderSigma = nextOrderSigmaRepository.saveAndFlush(nextOrderSigma);

        // Get all the nextOrderSigmaList where totalPrice is greater than or equal to
        defaultNextOrderSigmaFiltering(
            "totalPrice.greaterThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.greaterThanOrEqual=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllNextOrderSigmasByTotalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderSigma = nextOrderSigmaRepository.saveAndFlush(nextOrderSigma);

        // Get all the nextOrderSigmaList where totalPrice is less than or equal to
        defaultNextOrderSigmaFiltering(
            "totalPrice.lessThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.lessThanOrEqual=" + SMALLER_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllNextOrderSigmasByTotalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextOrderSigma = nextOrderSigmaRepository.saveAndFlush(nextOrderSigma);

        // Get all the nextOrderSigmaList where totalPrice is less than
        defaultNextOrderSigmaFiltering("totalPrice.lessThan=" + UPDATED_TOTAL_PRICE, "totalPrice.lessThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllNextOrderSigmasByTotalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextOrderSigma = nextOrderSigmaRepository.saveAndFlush(nextOrderSigma);

        // Get all the nextOrderSigmaList where totalPrice is greater than
        defaultNextOrderSigmaFiltering("totalPrice.greaterThan=" + SMALLER_TOTAL_PRICE, "totalPrice.greaterThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllNextOrderSigmasByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderSigma = nextOrderSigmaRepository.saveAndFlush(nextOrderSigma);

        // Get all the nextOrderSigmaList where status equals to
        defaultNextOrderSigmaFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllNextOrderSigmasByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextOrderSigma = nextOrderSigmaRepository.saveAndFlush(nextOrderSigma);

        // Get all the nextOrderSigmaList where status in
        defaultNextOrderSigmaFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllNextOrderSigmasByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextOrderSigma = nextOrderSigmaRepository.saveAndFlush(nextOrderSigma);

        // Get all the nextOrderSigmaList where status is not null
        defaultNextOrderSigmaFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllNextOrderSigmasByPaymentIsEqualToSomething() throws Exception {
        NextPaymentSigma payment;
        if (TestUtil.findAll(em, NextPaymentSigma.class).isEmpty()) {
            nextOrderSigmaRepository.saveAndFlush(nextOrderSigma);
            payment = NextPaymentSigmaResourceIT.createEntity();
        } else {
            payment = TestUtil.findAll(em, NextPaymentSigma.class).get(0);
        }
        em.persist(payment);
        em.flush();
        nextOrderSigma.setPayment(payment);
        nextOrderSigmaRepository.saveAndFlush(nextOrderSigma);
        Long paymentId = payment.getId();
        // Get all the nextOrderSigmaList where payment equals to paymentId
        defaultNextOrderSigmaShouldBeFound("paymentId.equals=" + paymentId);

        // Get all the nextOrderSigmaList where payment equals to (paymentId + 1)
        defaultNextOrderSigmaShouldNotBeFound("paymentId.equals=" + (paymentId + 1));
    }

    @Test
    @Transactional
    void getAllNextOrderSigmasByShipmentIsEqualToSomething() throws Exception {
        NextShipmentSigma shipment;
        if (TestUtil.findAll(em, NextShipmentSigma.class).isEmpty()) {
            nextOrderSigmaRepository.saveAndFlush(nextOrderSigma);
            shipment = NextShipmentSigmaResourceIT.createEntity();
        } else {
            shipment = TestUtil.findAll(em, NextShipmentSigma.class).get(0);
        }
        em.persist(shipment);
        em.flush();
        nextOrderSigma.setShipment(shipment);
        nextOrderSigmaRepository.saveAndFlush(nextOrderSigma);
        Long shipmentId = shipment.getId();
        // Get all the nextOrderSigmaList where shipment equals to shipmentId
        defaultNextOrderSigmaShouldBeFound("shipmentId.equals=" + shipmentId);

        // Get all the nextOrderSigmaList where shipment equals to (shipmentId + 1)
        defaultNextOrderSigmaShouldNotBeFound("shipmentId.equals=" + (shipmentId + 1));
    }

    @Test
    @Transactional
    void getAllNextOrderSigmasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextOrderSigmaRepository.saveAndFlush(nextOrderSigma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextOrderSigma.setTenant(tenant);
        nextOrderSigmaRepository.saveAndFlush(nextOrderSigma);
        Long tenantId = tenant.getId();
        // Get all the nextOrderSigmaList where tenant equals to tenantId
        defaultNextOrderSigmaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextOrderSigmaList where tenant equals to (tenantId + 1)
        defaultNextOrderSigmaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllNextOrderSigmasByCustomerIsEqualToSomething() throws Exception {
        NextCustomerSigma customer;
        if (TestUtil.findAll(em, NextCustomerSigma.class).isEmpty()) {
            nextOrderSigmaRepository.saveAndFlush(nextOrderSigma);
            customer = NextCustomerSigmaResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, NextCustomerSigma.class).get(0);
        }
        em.persist(customer);
        em.flush();
        nextOrderSigma.setCustomer(customer);
        nextOrderSigmaRepository.saveAndFlush(nextOrderSigma);
        Long customerId = customer.getId();
        // Get all the nextOrderSigmaList where customer equals to customerId
        defaultNextOrderSigmaShouldBeFound("customerId.equals=" + customerId);

        // Get all the nextOrderSigmaList where customer equals to (customerId + 1)
        defaultNextOrderSigmaShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    private void defaultNextOrderSigmaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextOrderSigmaShouldBeFound(shouldBeFound);
        defaultNextOrderSigmaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextOrderSigmaShouldBeFound(String filter) throws Exception {
        restNextOrderSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextOrderSigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restNextOrderSigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextOrderSigmaShouldNotBeFound(String filter) throws Exception {
        restNextOrderSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextOrderSigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextOrderSigma() throws Exception {
        // Get the nextOrderSigma
        restNextOrderSigmaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextOrderSigma() throws Exception {
        // Initialize the database
        insertedNextOrderSigma = nextOrderSigmaRepository.saveAndFlush(nextOrderSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextOrderSigma
        NextOrderSigma updatedNextOrderSigma = nextOrderSigmaRepository.findById(nextOrderSigma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextOrderSigma are not directly saved in db
        em.detach(updatedNextOrderSigma);
        updatedNextOrderSigma.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);

        restNextOrderSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextOrderSigma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextOrderSigma))
            )
            .andExpect(status().isOk());

        // Validate the NextOrderSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextOrderSigmaToMatchAllProperties(updatedNextOrderSigma);
    }

    @Test
    @Transactional
    void putNonExistingNextOrderSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderSigma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextOrderSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextOrderSigma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextOrderSigma))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrderSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextOrderSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderSigma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextOrderSigma))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrderSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextOrderSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderSigma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderSigmaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderSigma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextOrderSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextOrderSigmaWithPatch() throws Exception {
        // Initialize the database
        insertedNextOrderSigma = nextOrderSigmaRepository.saveAndFlush(nextOrderSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextOrderSigma using partial update
        NextOrderSigma partialUpdatedNextOrderSigma = new NextOrderSigma();
        partialUpdatedNextOrderSigma.setId(nextOrderSigma.getId());

        restNextOrderSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextOrderSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextOrderSigma))
            )
            .andExpect(status().isOk());

        // Validate the NextOrderSigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextOrderSigmaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextOrderSigma, nextOrderSigma),
            getPersistedNextOrderSigma(nextOrderSigma)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextOrderSigmaWithPatch() throws Exception {
        // Initialize the database
        insertedNextOrderSigma = nextOrderSigmaRepository.saveAndFlush(nextOrderSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextOrderSigma using partial update
        NextOrderSigma partialUpdatedNextOrderSigma = new NextOrderSigma();
        partialUpdatedNextOrderSigma.setId(nextOrderSigma.getId());

        partialUpdatedNextOrderSigma.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);

        restNextOrderSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextOrderSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextOrderSigma))
            )
            .andExpect(status().isOk());

        // Validate the NextOrderSigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextOrderSigmaUpdatableFieldsEquals(partialUpdatedNextOrderSigma, getPersistedNextOrderSigma(partialUpdatedNextOrderSigma));
    }

    @Test
    @Transactional
    void patchNonExistingNextOrderSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderSigma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextOrderSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextOrderSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextOrderSigma))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrderSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextOrderSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderSigma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextOrderSigma))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrderSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextOrderSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderSigma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderSigmaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextOrderSigma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextOrderSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextOrderSigma() throws Exception {
        // Initialize the database
        insertedNextOrderSigma = nextOrderSigmaRepository.saveAndFlush(nextOrderSigma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextOrderSigma
        restNextOrderSigmaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextOrderSigma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextOrderSigmaRepository.count();
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

    protected NextOrderSigma getPersistedNextOrderSigma(NextOrderSigma nextOrderSigma) {
        return nextOrderSigmaRepository.findById(nextOrderSigma.getId()).orElseThrow();
    }

    protected void assertPersistedNextOrderSigmaToMatchAllProperties(NextOrderSigma expectedNextOrderSigma) {
        assertNextOrderSigmaAllPropertiesEquals(expectedNextOrderSigma, getPersistedNextOrderSigma(expectedNextOrderSigma));
    }

    protected void assertPersistedNextOrderSigmaToMatchUpdatableProperties(NextOrderSigma expectedNextOrderSigma) {
        assertNextOrderSigmaAllUpdatablePropertiesEquals(expectedNextOrderSigma, getPersistedNextOrderSigma(expectedNextOrderSigma));
    }
}
