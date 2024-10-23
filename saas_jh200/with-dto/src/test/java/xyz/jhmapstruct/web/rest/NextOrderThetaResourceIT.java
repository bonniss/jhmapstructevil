package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextOrderThetaAsserts.*;
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
import xyz.jhmapstruct.domain.NextCustomerTheta;
import xyz.jhmapstruct.domain.NextOrderTheta;
import xyz.jhmapstruct.domain.NextPaymentTheta;
import xyz.jhmapstruct.domain.NextShipmentTheta;
import xyz.jhmapstruct.domain.enumeration.OrderThetaStatus;
import xyz.jhmapstruct.repository.NextOrderThetaRepository;
import xyz.jhmapstruct.service.NextOrderThetaService;
import xyz.jhmapstruct.service.dto.NextOrderThetaDTO;
import xyz.jhmapstruct.service.mapper.NextOrderThetaMapper;

/**
 * Integration tests for the {@link NextOrderThetaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextOrderThetaResourceIT {

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_PRICE = new BigDecimal(1 - 1);

    private static final OrderThetaStatus DEFAULT_STATUS = OrderThetaStatus.PENDING;
    private static final OrderThetaStatus UPDATED_STATUS = OrderThetaStatus.SHIPPED;

    private static final String ENTITY_API_URL = "/api/next-order-thetas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextOrderThetaRepository nextOrderThetaRepository;

    @Mock
    private NextOrderThetaRepository nextOrderThetaRepositoryMock;

    @Autowired
    private NextOrderThetaMapper nextOrderThetaMapper;

    @Mock
    private NextOrderThetaService nextOrderThetaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextOrderThetaMockMvc;

    private NextOrderTheta nextOrderTheta;

    private NextOrderTheta insertedNextOrderTheta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextOrderTheta createEntity() {
        return new NextOrderTheta().orderDate(DEFAULT_ORDER_DATE).totalPrice(DEFAULT_TOTAL_PRICE).status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextOrderTheta createUpdatedEntity() {
        return new NextOrderTheta().orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        nextOrderTheta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextOrderTheta != null) {
            nextOrderThetaRepository.delete(insertedNextOrderTheta);
            insertedNextOrderTheta = null;
        }
    }

    @Test
    @Transactional
    void createNextOrderTheta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextOrderTheta
        NextOrderThetaDTO nextOrderThetaDTO = nextOrderThetaMapper.toDto(nextOrderTheta);
        var returnedNextOrderThetaDTO = om.readValue(
            restNextOrderThetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderThetaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextOrderThetaDTO.class
        );

        // Validate the NextOrderTheta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextOrderTheta = nextOrderThetaMapper.toEntity(returnedNextOrderThetaDTO);
        assertNextOrderThetaUpdatableFieldsEquals(returnedNextOrderTheta, getPersistedNextOrderTheta(returnedNextOrderTheta));

        insertedNextOrderTheta = returnedNextOrderTheta;
    }

    @Test
    @Transactional
    void createNextOrderThetaWithExistingId() throws Exception {
        // Create the NextOrderTheta with an existing ID
        nextOrderTheta.setId(1L);
        NextOrderThetaDTO nextOrderThetaDTO = nextOrderThetaMapper.toDto(nextOrderTheta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextOrderThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderThetaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextOrderTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextOrderTheta.setOrderDate(null);

        // Create the NextOrderTheta, which fails.
        NextOrderThetaDTO nextOrderThetaDTO = nextOrderThetaMapper.toDto(nextOrderTheta);

        restNextOrderThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderThetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextOrderTheta.setTotalPrice(null);

        // Create the NextOrderTheta, which fails.
        NextOrderThetaDTO nextOrderThetaDTO = nextOrderThetaMapper.toDto(nextOrderTheta);

        restNextOrderThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderThetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextOrderThetas() throws Exception {
        // Initialize the database
        insertedNextOrderTheta = nextOrderThetaRepository.saveAndFlush(nextOrderTheta);

        // Get all the nextOrderThetaList
        restNextOrderThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextOrderTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextOrderThetasWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextOrderThetaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextOrderThetaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextOrderThetaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextOrderThetasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextOrderThetaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextOrderThetaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextOrderThetaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextOrderTheta() throws Exception {
        // Initialize the database
        insertedNextOrderTheta = nextOrderThetaRepository.saveAndFlush(nextOrderTheta);

        // Get the nextOrderTheta
        restNextOrderThetaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextOrderTheta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextOrderTheta.getId().intValue()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNextOrderThetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextOrderTheta = nextOrderThetaRepository.saveAndFlush(nextOrderTheta);

        Long id = nextOrderTheta.getId();

        defaultNextOrderThetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextOrderThetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextOrderThetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextOrderThetasByOrderDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderTheta = nextOrderThetaRepository.saveAndFlush(nextOrderTheta);

        // Get all the nextOrderThetaList where orderDate equals to
        defaultNextOrderThetaFiltering("orderDate.equals=" + DEFAULT_ORDER_DATE, "orderDate.equals=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllNextOrderThetasByOrderDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextOrderTheta = nextOrderThetaRepository.saveAndFlush(nextOrderTheta);

        // Get all the nextOrderThetaList where orderDate in
        defaultNextOrderThetaFiltering(
            "orderDate.in=" + DEFAULT_ORDER_DATE + "," + UPDATED_ORDER_DATE,
            "orderDate.in=" + UPDATED_ORDER_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextOrderThetasByOrderDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextOrderTheta = nextOrderThetaRepository.saveAndFlush(nextOrderTheta);

        // Get all the nextOrderThetaList where orderDate is not null
        defaultNextOrderThetaFiltering("orderDate.specified=true", "orderDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextOrderThetasByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderTheta = nextOrderThetaRepository.saveAndFlush(nextOrderTheta);

        // Get all the nextOrderThetaList where totalPrice equals to
        defaultNextOrderThetaFiltering("totalPrice.equals=" + DEFAULT_TOTAL_PRICE, "totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllNextOrderThetasByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextOrderTheta = nextOrderThetaRepository.saveAndFlush(nextOrderTheta);

        // Get all the nextOrderThetaList where totalPrice in
        defaultNextOrderThetaFiltering(
            "totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE,
            "totalPrice.in=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllNextOrderThetasByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextOrderTheta = nextOrderThetaRepository.saveAndFlush(nextOrderTheta);

        // Get all the nextOrderThetaList where totalPrice is not null
        defaultNextOrderThetaFiltering("totalPrice.specified=true", "totalPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllNextOrderThetasByTotalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderTheta = nextOrderThetaRepository.saveAndFlush(nextOrderTheta);

        // Get all the nextOrderThetaList where totalPrice is greater than or equal to
        defaultNextOrderThetaFiltering(
            "totalPrice.greaterThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.greaterThanOrEqual=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllNextOrderThetasByTotalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderTheta = nextOrderThetaRepository.saveAndFlush(nextOrderTheta);

        // Get all the nextOrderThetaList where totalPrice is less than or equal to
        defaultNextOrderThetaFiltering(
            "totalPrice.lessThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.lessThanOrEqual=" + SMALLER_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllNextOrderThetasByTotalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextOrderTheta = nextOrderThetaRepository.saveAndFlush(nextOrderTheta);

        // Get all the nextOrderThetaList where totalPrice is less than
        defaultNextOrderThetaFiltering("totalPrice.lessThan=" + UPDATED_TOTAL_PRICE, "totalPrice.lessThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllNextOrderThetasByTotalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextOrderTheta = nextOrderThetaRepository.saveAndFlush(nextOrderTheta);

        // Get all the nextOrderThetaList where totalPrice is greater than
        defaultNextOrderThetaFiltering("totalPrice.greaterThan=" + SMALLER_TOTAL_PRICE, "totalPrice.greaterThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllNextOrderThetasByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderTheta = nextOrderThetaRepository.saveAndFlush(nextOrderTheta);

        // Get all the nextOrderThetaList where status equals to
        defaultNextOrderThetaFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllNextOrderThetasByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextOrderTheta = nextOrderThetaRepository.saveAndFlush(nextOrderTheta);

        // Get all the nextOrderThetaList where status in
        defaultNextOrderThetaFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllNextOrderThetasByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextOrderTheta = nextOrderThetaRepository.saveAndFlush(nextOrderTheta);

        // Get all the nextOrderThetaList where status is not null
        defaultNextOrderThetaFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllNextOrderThetasByPaymentIsEqualToSomething() throws Exception {
        NextPaymentTheta payment;
        if (TestUtil.findAll(em, NextPaymentTheta.class).isEmpty()) {
            nextOrderThetaRepository.saveAndFlush(nextOrderTheta);
            payment = NextPaymentThetaResourceIT.createEntity();
        } else {
            payment = TestUtil.findAll(em, NextPaymentTheta.class).get(0);
        }
        em.persist(payment);
        em.flush();
        nextOrderTheta.setPayment(payment);
        nextOrderThetaRepository.saveAndFlush(nextOrderTheta);
        Long paymentId = payment.getId();
        // Get all the nextOrderThetaList where payment equals to paymentId
        defaultNextOrderThetaShouldBeFound("paymentId.equals=" + paymentId);

        // Get all the nextOrderThetaList where payment equals to (paymentId + 1)
        defaultNextOrderThetaShouldNotBeFound("paymentId.equals=" + (paymentId + 1));
    }

    @Test
    @Transactional
    void getAllNextOrderThetasByShipmentIsEqualToSomething() throws Exception {
        NextShipmentTheta shipment;
        if (TestUtil.findAll(em, NextShipmentTheta.class).isEmpty()) {
            nextOrderThetaRepository.saveAndFlush(nextOrderTheta);
            shipment = NextShipmentThetaResourceIT.createEntity();
        } else {
            shipment = TestUtil.findAll(em, NextShipmentTheta.class).get(0);
        }
        em.persist(shipment);
        em.flush();
        nextOrderTheta.setShipment(shipment);
        nextOrderThetaRepository.saveAndFlush(nextOrderTheta);
        Long shipmentId = shipment.getId();
        // Get all the nextOrderThetaList where shipment equals to shipmentId
        defaultNextOrderThetaShouldBeFound("shipmentId.equals=" + shipmentId);

        // Get all the nextOrderThetaList where shipment equals to (shipmentId + 1)
        defaultNextOrderThetaShouldNotBeFound("shipmentId.equals=" + (shipmentId + 1));
    }

    @Test
    @Transactional
    void getAllNextOrderThetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextOrderThetaRepository.saveAndFlush(nextOrderTheta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextOrderTheta.setTenant(tenant);
        nextOrderThetaRepository.saveAndFlush(nextOrderTheta);
        Long tenantId = tenant.getId();
        // Get all the nextOrderThetaList where tenant equals to tenantId
        defaultNextOrderThetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextOrderThetaList where tenant equals to (tenantId + 1)
        defaultNextOrderThetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllNextOrderThetasByCustomerIsEqualToSomething() throws Exception {
        NextCustomerTheta customer;
        if (TestUtil.findAll(em, NextCustomerTheta.class).isEmpty()) {
            nextOrderThetaRepository.saveAndFlush(nextOrderTheta);
            customer = NextCustomerThetaResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, NextCustomerTheta.class).get(0);
        }
        em.persist(customer);
        em.flush();
        nextOrderTheta.setCustomer(customer);
        nextOrderThetaRepository.saveAndFlush(nextOrderTheta);
        Long customerId = customer.getId();
        // Get all the nextOrderThetaList where customer equals to customerId
        defaultNextOrderThetaShouldBeFound("customerId.equals=" + customerId);

        // Get all the nextOrderThetaList where customer equals to (customerId + 1)
        defaultNextOrderThetaShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    private void defaultNextOrderThetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextOrderThetaShouldBeFound(shouldBeFound);
        defaultNextOrderThetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextOrderThetaShouldBeFound(String filter) throws Exception {
        restNextOrderThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextOrderTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restNextOrderThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextOrderThetaShouldNotBeFound(String filter) throws Exception {
        restNextOrderThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextOrderThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextOrderTheta() throws Exception {
        // Get the nextOrderTheta
        restNextOrderThetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextOrderTheta() throws Exception {
        // Initialize the database
        insertedNextOrderTheta = nextOrderThetaRepository.saveAndFlush(nextOrderTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextOrderTheta
        NextOrderTheta updatedNextOrderTheta = nextOrderThetaRepository.findById(nextOrderTheta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextOrderTheta are not directly saved in db
        em.detach(updatedNextOrderTheta);
        updatedNextOrderTheta.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);
        NextOrderThetaDTO nextOrderThetaDTO = nextOrderThetaMapper.toDto(updatedNextOrderTheta);

        restNextOrderThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextOrderThetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextOrderThetaDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextOrderTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextOrderThetaToMatchAllProperties(updatedNextOrderTheta);
    }

    @Test
    @Transactional
    void putNonExistingNextOrderTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderTheta.setId(longCount.incrementAndGet());

        // Create the NextOrderTheta
        NextOrderThetaDTO nextOrderThetaDTO = nextOrderThetaMapper.toDto(nextOrderTheta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextOrderThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextOrderThetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextOrderThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrderTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextOrderTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderTheta.setId(longCount.incrementAndGet());

        // Create the NextOrderTheta
        NextOrderThetaDTO nextOrderThetaDTO = nextOrderThetaMapper.toDto(nextOrderTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextOrderThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrderTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextOrderTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderTheta.setId(longCount.incrementAndGet());

        // Create the NextOrderTheta
        NextOrderThetaDTO nextOrderThetaDTO = nextOrderThetaMapper.toDto(nextOrderTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderThetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderThetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextOrderTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextOrderThetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextOrderTheta = nextOrderThetaRepository.saveAndFlush(nextOrderTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextOrderTheta using partial update
        NextOrderTheta partialUpdatedNextOrderTheta = new NextOrderTheta();
        partialUpdatedNextOrderTheta.setId(nextOrderTheta.getId());

        partialUpdatedNextOrderTheta.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE);

        restNextOrderThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextOrderTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextOrderTheta))
            )
            .andExpect(status().isOk());

        // Validate the NextOrderTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextOrderThetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextOrderTheta, nextOrderTheta),
            getPersistedNextOrderTheta(nextOrderTheta)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextOrderThetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextOrderTheta = nextOrderThetaRepository.saveAndFlush(nextOrderTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextOrderTheta using partial update
        NextOrderTheta partialUpdatedNextOrderTheta = new NextOrderTheta();
        partialUpdatedNextOrderTheta.setId(nextOrderTheta.getId());

        partialUpdatedNextOrderTheta.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);

        restNextOrderThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextOrderTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextOrderTheta))
            )
            .andExpect(status().isOk());

        // Validate the NextOrderTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextOrderThetaUpdatableFieldsEquals(partialUpdatedNextOrderTheta, getPersistedNextOrderTheta(partialUpdatedNextOrderTheta));
    }

    @Test
    @Transactional
    void patchNonExistingNextOrderTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderTheta.setId(longCount.incrementAndGet());

        // Create the NextOrderTheta
        NextOrderThetaDTO nextOrderThetaDTO = nextOrderThetaMapper.toDto(nextOrderTheta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextOrderThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextOrderThetaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextOrderThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrderTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextOrderTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderTheta.setId(longCount.incrementAndGet());

        // Create the NextOrderTheta
        NextOrderThetaDTO nextOrderThetaDTO = nextOrderThetaMapper.toDto(nextOrderTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextOrderThetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrderTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextOrderTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderTheta.setId(longCount.incrementAndGet());

        // Create the NextOrderTheta
        NextOrderThetaDTO nextOrderThetaDTO = nextOrderThetaMapper.toDto(nextOrderTheta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderThetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextOrderThetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextOrderTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextOrderTheta() throws Exception {
        // Initialize the database
        insertedNextOrderTheta = nextOrderThetaRepository.saveAndFlush(nextOrderTheta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextOrderTheta
        restNextOrderThetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextOrderTheta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextOrderThetaRepository.count();
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

    protected NextOrderTheta getPersistedNextOrderTheta(NextOrderTheta nextOrderTheta) {
        return nextOrderThetaRepository.findById(nextOrderTheta.getId()).orElseThrow();
    }

    protected void assertPersistedNextOrderThetaToMatchAllProperties(NextOrderTheta expectedNextOrderTheta) {
        assertNextOrderThetaAllPropertiesEquals(expectedNextOrderTheta, getPersistedNextOrderTheta(expectedNextOrderTheta));
    }

    protected void assertPersistedNextOrderThetaToMatchUpdatableProperties(NextOrderTheta expectedNextOrderTheta) {
        assertNextOrderThetaAllUpdatablePropertiesEquals(expectedNextOrderTheta, getPersistedNextOrderTheta(expectedNextOrderTheta));
    }
}
