package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextOrderBetaAsserts.*;
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
import xyz.jhmapstruct.domain.NextCustomerBeta;
import xyz.jhmapstruct.domain.NextOrderBeta;
import xyz.jhmapstruct.domain.NextPaymentBeta;
import xyz.jhmapstruct.domain.NextShipmentBeta;
import xyz.jhmapstruct.domain.enumeration.OrderBetaStatus;
import xyz.jhmapstruct.repository.NextOrderBetaRepository;
import xyz.jhmapstruct.service.NextOrderBetaService;

/**
 * Integration tests for the {@link NextOrderBetaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextOrderBetaResourceIT {

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_PRICE = new BigDecimal(1 - 1);

    private static final OrderBetaStatus DEFAULT_STATUS = OrderBetaStatus.PENDING;
    private static final OrderBetaStatus UPDATED_STATUS = OrderBetaStatus.SHIPPED;

    private static final String ENTITY_API_URL = "/api/next-order-betas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextOrderBetaRepository nextOrderBetaRepository;

    @Mock
    private NextOrderBetaRepository nextOrderBetaRepositoryMock;

    @Mock
    private NextOrderBetaService nextOrderBetaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextOrderBetaMockMvc;

    private NextOrderBeta nextOrderBeta;

    private NextOrderBeta insertedNextOrderBeta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextOrderBeta createEntity() {
        return new NextOrderBeta().orderDate(DEFAULT_ORDER_DATE).totalPrice(DEFAULT_TOTAL_PRICE).status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextOrderBeta createUpdatedEntity() {
        return new NextOrderBeta().orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        nextOrderBeta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextOrderBeta != null) {
            nextOrderBetaRepository.delete(insertedNextOrderBeta);
            insertedNextOrderBeta = null;
        }
    }

    @Test
    @Transactional
    void createNextOrderBeta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextOrderBeta
        var returnedNextOrderBeta = om.readValue(
            restNextOrderBetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderBeta)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextOrderBeta.class
        );

        // Validate the NextOrderBeta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextOrderBetaUpdatableFieldsEquals(returnedNextOrderBeta, getPersistedNextOrderBeta(returnedNextOrderBeta));

        insertedNextOrderBeta = returnedNextOrderBeta;
    }

    @Test
    @Transactional
    void createNextOrderBetaWithExistingId() throws Exception {
        // Create the NextOrderBeta with an existing ID
        nextOrderBeta.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextOrderBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderBeta)))
            .andExpect(status().isBadRequest());

        // Validate the NextOrderBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextOrderBeta.setOrderDate(null);

        // Create the NextOrderBeta, which fails.

        restNextOrderBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderBeta)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextOrderBeta.setTotalPrice(null);

        // Create the NextOrderBeta, which fails.

        restNextOrderBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderBeta)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextOrderBetas() throws Exception {
        // Initialize the database
        insertedNextOrderBeta = nextOrderBetaRepository.saveAndFlush(nextOrderBeta);

        // Get all the nextOrderBetaList
        restNextOrderBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextOrderBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextOrderBetasWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextOrderBetaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextOrderBetaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextOrderBetaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextOrderBetasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextOrderBetaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextOrderBetaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextOrderBetaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextOrderBeta() throws Exception {
        // Initialize the database
        insertedNextOrderBeta = nextOrderBetaRepository.saveAndFlush(nextOrderBeta);

        // Get the nextOrderBeta
        restNextOrderBetaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextOrderBeta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextOrderBeta.getId().intValue()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNextOrderBetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextOrderBeta = nextOrderBetaRepository.saveAndFlush(nextOrderBeta);

        Long id = nextOrderBeta.getId();

        defaultNextOrderBetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextOrderBetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextOrderBetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextOrderBetasByOrderDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderBeta = nextOrderBetaRepository.saveAndFlush(nextOrderBeta);

        // Get all the nextOrderBetaList where orderDate equals to
        defaultNextOrderBetaFiltering("orderDate.equals=" + DEFAULT_ORDER_DATE, "orderDate.equals=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllNextOrderBetasByOrderDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextOrderBeta = nextOrderBetaRepository.saveAndFlush(nextOrderBeta);

        // Get all the nextOrderBetaList where orderDate in
        defaultNextOrderBetaFiltering(
            "orderDate.in=" + DEFAULT_ORDER_DATE + "," + UPDATED_ORDER_DATE,
            "orderDate.in=" + UPDATED_ORDER_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextOrderBetasByOrderDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextOrderBeta = nextOrderBetaRepository.saveAndFlush(nextOrderBeta);

        // Get all the nextOrderBetaList where orderDate is not null
        defaultNextOrderBetaFiltering("orderDate.specified=true", "orderDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextOrderBetasByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderBeta = nextOrderBetaRepository.saveAndFlush(nextOrderBeta);

        // Get all the nextOrderBetaList where totalPrice equals to
        defaultNextOrderBetaFiltering("totalPrice.equals=" + DEFAULT_TOTAL_PRICE, "totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllNextOrderBetasByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextOrderBeta = nextOrderBetaRepository.saveAndFlush(nextOrderBeta);

        // Get all the nextOrderBetaList where totalPrice in
        defaultNextOrderBetaFiltering(
            "totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE,
            "totalPrice.in=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllNextOrderBetasByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextOrderBeta = nextOrderBetaRepository.saveAndFlush(nextOrderBeta);

        // Get all the nextOrderBetaList where totalPrice is not null
        defaultNextOrderBetaFiltering("totalPrice.specified=true", "totalPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllNextOrderBetasByTotalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderBeta = nextOrderBetaRepository.saveAndFlush(nextOrderBeta);

        // Get all the nextOrderBetaList where totalPrice is greater than or equal to
        defaultNextOrderBetaFiltering(
            "totalPrice.greaterThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.greaterThanOrEqual=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllNextOrderBetasByTotalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderBeta = nextOrderBetaRepository.saveAndFlush(nextOrderBeta);

        // Get all the nextOrderBetaList where totalPrice is less than or equal to
        defaultNextOrderBetaFiltering(
            "totalPrice.lessThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.lessThanOrEqual=" + SMALLER_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllNextOrderBetasByTotalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextOrderBeta = nextOrderBetaRepository.saveAndFlush(nextOrderBeta);

        // Get all the nextOrderBetaList where totalPrice is less than
        defaultNextOrderBetaFiltering("totalPrice.lessThan=" + UPDATED_TOTAL_PRICE, "totalPrice.lessThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllNextOrderBetasByTotalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextOrderBeta = nextOrderBetaRepository.saveAndFlush(nextOrderBeta);

        // Get all the nextOrderBetaList where totalPrice is greater than
        defaultNextOrderBetaFiltering("totalPrice.greaterThan=" + SMALLER_TOTAL_PRICE, "totalPrice.greaterThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllNextOrderBetasByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderBeta = nextOrderBetaRepository.saveAndFlush(nextOrderBeta);

        // Get all the nextOrderBetaList where status equals to
        defaultNextOrderBetaFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllNextOrderBetasByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextOrderBeta = nextOrderBetaRepository.saveAndFlush(nextOrderBeta);

        // Get all the nextOrderBetaList where status in
        defaultNextOrderBetaFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllNextOrderBetasByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextOrderBeta = nextOrderBetaRepository.saveAndFlush(nextOrderBeta);

        // Get all the nextOrderBetaList where status is not null
        defaultNextOrderBetaFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllNextOrderBetasByPaymentIsEqualToSomething() throws Exception {
        NextPaymentBeta payment;
        if (TestUtil.findAll(em, NextPaymentBeta.class).isEmpty()) {
            nextOrderBetaRepository.saveAndFlush(nextOrderBeta);
            payment = NextPaymentBetaResourceIT.createEntity();
        } else {
            payment = TestUtil.findAll(em, NextPaymentBeta.class).get(0);
        }
        em.persist(payment);
        em.flush();
        nextOrderBeta.setPayment(payment);
        nextOrderBetaRepository.saveAndFlush(nextOrderBeta);
        Long paymentId = payment.getId();
        // Get all the nextOrderBetaList where payment equals to paymentId
        defaultNextOrderBetaShouldBeFound("paymentId.equals=" + paymentId);

        // Get all the nextOrderBetaList where payment equals to (paymentId + 1)
        defaultNextOrderBetaShouldNotBeFound("paymentId.equals=" + (paymentId + 1));
    }

    @Test
    @Transactional
    void getAllNextOrderBetasByShipmentIsEqualToSomething() throws Exception {
        NextShipmentBeta shipment;
        if (TestUtil.findAll(em, NextShipmentBeta.class).isEmpty()) {
            nextOrderBetaRepository.saveAndFlush(nextOrderBeta);
            shipment = NextShipmentBetaResourceIT.createEntity();
        } else {
            shipment = TestUtil.findAll(em, NextShipmentBeta.class).get(0);
        }
        em.persist(shipment);
        em.flush();
        nextOrderBeta.setShipment(shipment);
        nextOrderBetaRepository.saveAndFlush(nextOrderBeta);
        Long shipmentId = shipment.getId();
        // Get all the nextOrderBetaList where shipment equals to shipmentId
        defaultNextOrderBetaShouldBeFound("shipmentId.equals=" + shipmentId);

        // Get all the nextOrderBetaList where shipment equals to (shipmentId + 1)
        defaultNextOrderBetaShouldNotBeFound("shipmentId.equals=" + (shipmentId + 1));
    }

    @Test
    @Transactional
    void getAllNextOrderBetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextOrderBetaRepository.saveAndFlush(nextOrderBeta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextOrderBeta.setTenant(tenant);
        nextOrderBetaRepository.saveAndFlush(nextOrderBeta);
        Long tenantId = tenant.getId();
        // Get all the nextOrderBetaList where tenant equals to tenantId
        defaultNextOrderBetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextOrderBetaList where tenant equals to (tenantId + 1)
        defaultNextOrderBetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllNextOrderBetasByCustomerIsEqualToSomething() throws Exception {
        NextCustomerBeta customer;
        if (TestUtil.findAll(em, NextCustomerBeta.class).isEmpty()) {
            nextOrderBetaRepository.saveAndFlush(nextOrderBeta);
            customer = NextCustomerBetaResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, NextCustomerBeta.class).get(0);
        }
        em.persist(customer);
        em.flush();
        nextOrderBeta.setCustomer(customer);
        nextOrderBetaRepository.saveAndFlush(nextOrderBeta);
        Long customerId = customer.getId();
        // Get all the nextOrderBetaList where customer equals to customerId
        defaultNextOrderBetaShouldBeFound("customerId.equals=" + customerId);

        // Get all the nextOrderBetaList where customer equals to (customerId + 1)
        defaultNextOrderBetaShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    private void defaultNextOrderBetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextOrderBetaShouldBeFound(shouldBeFound);
        defaultNextOrderBetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextOrderBetaShouldBeFound(String filter) throws Exception {
        restNextOrderBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextOrderBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restNextOrderBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextOrderBetaShouldNotBeFound(String filter) throws Exception {
        restNextOrderBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextOrderBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextOrderBeta() throws Exception {
        // Get the nextOrderBeta
        restNextOrderBetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextOrderBeta() throws Exception {
        // Initialize the database
        insertedNextOrderBeta = nextOrderBetaRepository.saveAndFlush(nextOrderBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextOrderBeta
        NextOrderBeta updatedNextOrderBeta = nextOrderBetaRepository.findById(nextOrderBeta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextOrderBeta are not directly saved in db
        em.detach(updatedNextOrderBeta);
        updatedNextOrderBeta.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);

        restNextOrderBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextOrderBeta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextOrderBeta))
            )
            .andExpect(status().isOk());

        // Validate the NextOrderBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextOrderBetaToMatchAllProperties(updatedNextOrderBeta);
    }

    @Test
    @Transactional
    void putNonExistingNextOrderBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderBeta.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextOrderBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextOrderBeta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextOrderBeta))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrderBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextOrderBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderBeta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextOrderBeta))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrderBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextOrderBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderBeta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderBetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderBeta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextOrderBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextOrderBetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextOrderBeta = nextOrderBetaRepository.saveAndFlush(nextOrderBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextOrderBeta using partial update
        NextOrderBeta partialUpdatedNextOrderBeta = new NextOrderBeta();
        partialUpdatedNextOrderBeta.setId(nextOrderBeta.getId());

        partialUpdatedNextOrderBeta.orderDate(UPDATED_ORDER_DATE);

        restNextOrderBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextOrderBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextOrderBeta))
            )
            .andExpect(status().isOk());

        // Validate the NextOrderBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextOrderBetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextOrderBeta, nextOrderBeta),
            getPersistedNextOrderBeta(nextOrderBeta)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextOrderBetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextOrderBeta = nextOrderBetaRepository.saveAndFlush(nextOrderBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextOrderBeta using partial update
        NextOrderBeta partialUpdatedNextOrderBeta = new NextOrderBeta();
        partialUpdatedNextOrderBeta.setId(nextOrderBeta.getId());

        partialUpdatedNextOrderBeta.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);

        restNextOrderBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextOrderBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextOrderBeta))
            )
            .andExpect(status().isOk());

        // Validate the NextOrderBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextOrderBetaUpdatableFieldsEquals(partialUpdatedNextOrderBeta, getPersistedNextOrderBeta(partialUpdatedNextOrderBeta));
    }

    @Test
    @Transactional
    void patchNonExistingNextOrderBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderBeta.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextOrderBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextOrderBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextOrderBeta))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrderBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextOrderBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderBeta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextOrderBeta))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrderBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextOrderBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderBeta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderBetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextOrderBeta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextOrderBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextOrderBeta() throws Exception {
        // Initialize the database
        insertedNextOrderBeta = nextOrderBetaRepository.saveAndFlush(nextOrderBeta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextOrderBeta
        restNextOrderBetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextOrderBeta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextOrderBetaRepository.count();
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

    protected NextOrderBeta getPersistedNextOrderBeta(NextOrderBeta nextOrderBeta) {
        return nextOrderBetaRepository.findById(nextOrderBeta.getId()).orElseThrow();
    }

    protected void assertPersistedNextOrderBetaToMatchAllProperties(NextOrderBeta expectedNextOrderBeta) {
        assertNextOrderBetaAllPropertiesEquals(expectedNextOrderBeta, getPersistedNextOrderBeta(expectedNextOrderBeta));
    }

    protected void assertPersistedNextOrderBetaToMatchUpdatableProperties(NextOrderBeta expectedNextOrderBeta) {
        assertNextOrderBetaAllUpdatablePropertiesEquals(expectedNextOrderBeta, getPersistedNextOrderBeta(expectedNextOrderBeta));
    }
}
