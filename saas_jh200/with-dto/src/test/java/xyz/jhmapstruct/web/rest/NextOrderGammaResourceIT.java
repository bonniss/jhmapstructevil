package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextOrderGammaAsserts.*;
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
import xyz.jhmapstruct.domain.NextCustomerGamma;
import xyz.jhmapstruct.domain.NextOrderGamma;
import xyz.jhmapstruct.domain.NextPaymentGamma;
import xyz.jhmapstruct.domain.NextShipmentGamma;
import xyz.jhmapstruct.domain.enumeration.OrderGammaStatus;
import xyz.jhmapstruct.repository.NextOrderGammaRepository;
import xyz.jhmapstruct.service.NextOrderGammaService;
import xyz.jhmapstruct.service.dto.NextOrderGammaDTO;
import xyz.jhmapstruct.service.mapper.NextOrderGammaMapper;

/**
 * Integration tests for the {@link NextOrderGammaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextOrderGammaResourceIT {

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_PRICE = new BigDecimal(1 - 1);

    private static final OrderGammaStatus DEFAULT_STATUS = OrderGammaStatus.PENDING;
    private static final OrderGammaStatus UPDATED_STATUS = OrderGammaStatus.SHIPPED;

    private static final String ENTITY_API_URL = "/api/next-order-gammas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextOrderGammaRepository nextOrderGammaRepository;

    @Mock
    private NextOrderGammaRepository nextOrderGammaRepositoryMock;

    @Autowired
    private NextOrderGammaMapper nextOrderGammaMapper;

    @Mock
    private NextOrderGammaService nextOrderGammaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextOrderGammaMockMvc;

    private NextOrderGamma nextOrderGamma;

    private NextOrderGamma insertedNextOrderGamma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextOrderGamma createEntity() {
        return new NextOrderGamma().orderDate(DEFAULT_ORDER_DATE).totalPrice(DEFAULT_TOTAL_PRICE).status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextOrderGamma createUpdatedEntity() {
        return new NextOrderGamma().orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        nextOrderGamma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextOrderGamma != null) {
            nextOrderGammaRepository.delete(insertedNextOrderGamma);
            insertedNextOrderGamma = null;
        }
    }

    @Test
    @Transactional
    void createNextOrderGamma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextOrderGamma
        NextOrderGammaDTO nextOrderGammaDTO = nextOrderGammaMapper.toDto(nextOrderGamma);
        var returnedNextOrderGammaDTO = om.readValue(
            restNextOrderGammaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderGammaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextOrderGammaDTO.class
        );

        // Validate the NextOrderGamma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextOrderGamma = nextOrderGammaMapper.toEntity(returnedNextOrderGammaDTO);
        assertNextOrderGammaUpdatableFieldsEquals(returnedNextOrderGamma, getPersistedNextOrderGamma(returnedNextOrderGamma));

        insertedNextOrderGamma = returnedNextOrderGamma;
    }

    @Test
    @Transactional
    void createNextOrderGammaWithExistingId() throws Exception {
        // Create the NextOrderGamma with an existing ID
        nextOrderGamma.setId(1L);
        NextOrderGammaDTO nextOrderGammaDTO = nextOrderGammaMapper.toDto(nextOrderGamma);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextOrderGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderGammaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextOrderGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextOrderGamma.setOrderDate(null);

        // Create the NextOrderGamma, which fails.
        NextOrderGammaDTO nextOrderGammaDTO = nextOrderGammaMapper.toDto(nextOrderGamma);

        restNextOrderGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderGammaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextOrderGamma.setTotalPrice(null);

        // Create the NextOrderGamma, which fails.
        NextOrderGammaDTO nextOrderGammaDTO = nextOrderGammaMapper.toDto(nextOrderGamma);

        restNextOrderGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderGammaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextOrderGammas() throws Exception {
        // Initialize the database
        insertedNextOrderGamma = nextOrderGammaRepository.saveAndFlush(nextOrderGamma);

        // Get all the nextOrderGammaList
        restNextOrderGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextOrderGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextOrderGammasWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextOrderGammaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextOrderGammaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextOrderGammaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextOrderGammasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextOrderGammaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextOrderGammaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextOrderGammaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextOrderGamma() throws Exception {
        // Initialize the database
        insertedNextOrderGamma = nextOrderGammaRepository.saveAndFlush(nextOrderGamma);

        // Get the nextOrderGamma
        restNextOrderGammaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextOrderGamma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextOrderGamma.getId().intValue()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNextOrderGammasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextOrderGamma = nextOrderGammaRepository.saveAndFlush(nextOrderGamma);

        Long id = nextOrderGamma.getId();

        defaultNextOrderGammaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextOrderGammaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextOrderGammaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextOrderGammasByOrderDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderGamma = nextOrderGammaRepository.saveAndFlush(nextOrderGamma);

        // Get all the nextOrderGammaList where orderDate equals to
        defaultNextOrderGammaFiltering("orderDate.equals=" + DEFAULT_ORDER_DATE, "orderDate.equals=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllNextOrderGammasByOrderDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextOrderGamma = nextOrderGammaRepository.saveAndFlush(nextOrderGamma);

        // Get all the nextOrderGammaList where orderDate in
        defaultNextOrderGammaFiltering(
            "orderDate.in=" + DEFAULT_ORDER_DATE + "," + UPDATED_ORDER_DATE,
            "orderDate.in=" + UPDATED_ORDER_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextOrderGammasByOrderDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextOrderGamma = nextOrderGammaRepository.saveAndFlush(nextOrderGamma);

        // Get all the nextOrderGammaList where orderDate is not null
        defaultNextOrderGammaFiltering("orderDate.specified=true", "orderDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextOrderGammasByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderGamma = nextOrderGammaRepository.saveAndFlush(nextOrderGamma);

        // Get all the nextOrderGammaList where totalPrice equals to
        defaultNextOrderGammaFiltering("totalPrice.equals=" + DEFAULT_TOTAL_PRICE, "totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllNextOrderGammasByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextOrderGamma = nextOrderGammaRepository.saveAndFlush(nextOrderGamma);

        // Get all the nextOrderGammaList where totalPrice in
        defaultNextOrderGammaFiltering(
            "totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE,
            "totalPrice.in=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllNextOrderGammasByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextOrderGamma = nextOrderGammaRepository.saveAndFlush(nextOrderGamma);

        // Get all the nextOrderGammaList where totalPrice is not null
        defaultNextOrderGammaFiltering("totalPrice.specified=true", "totalPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllNextOrderGammasByTotalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderGamma = nextOrderGammaRepository.saveAndFlush(nextOrderGamma);

        // Get all the nextOrderGammaList where totalPrice is greater than or equal to
        defaultNextOrderGammaFiltering(
            "totalPrice.greaterThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.greaterThanOrEqual=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllNextOrderGammasByTotalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderGamma = nextOrderGammaRepository.saveAndFlush(nextOrderGamma);

        // Get all the nextOrderGammaList where totalPrice is less than or equal to
        defaultNextOrderGammaFiltering(
            "totalPrice.lessThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.lessThanOrEqual=" + SMALLER_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllNextOrderGammasByTotalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextOrderGamma = nextOrderGammaRepository.saveAndFlush(nextOrderGamma);

        // Get all the nextOrderGammaList where totalPrice is less than
        defaultNextOrderGammaFiltering("totalPrice.lessThan=" + UPDATED_TOTAL_PRICE, "totalPrice.lessThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllNextOrderGammasByTotalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextOrderGamma = nextOrderGammaRepository.saveAndFlush(nextOrderGamma);

        // Get all the nextOrderGammaList where totalPrice is greater than
        defaultNextOrderGammaFiltering("totalPrice.greaterThan=" + SMALLER_TOTAL_PRICE, "totalPrice.greaterThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllNextOrderGammasByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderGamma = nextOrderGammaRepository.saveAndFlush(nextOrderGamma);

        // Get all the nextOrderGammaList where status equals to
        defaultNextOrderGammaFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllNextOrderGammasByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextOrderGamma = nextOrderGammaRepository.saveAndFlush(nextOrderGamma);

        // Get all the nextOrderGammaList where status in
        defaultNextOrderGammaFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllNextOrderGammasByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextOrderGamma = nextOrderGammaRepository.saveAndFlush(nextOrderGamma);

        // Get all the nextOrderGammaList where status is not null
        defaultNextOrderGammaFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllNextOrderGammasByPaymentIsEqualToSomething() throws Exception {
        NextPaymentGamma payment;
        if (TestUtil.findAll(em, NextPaymentGamma.class).isEmpty()) {
            nextOrderGammaRepository.saveAndFlush(nextOrderGamma);
            payment = NextPaymentGammaResourceIT.createEntity();
        } else {
            payment = TestUtil.findAll(em, NextPaymentGamma.class).get(0);
        }
        em.persist(payment);
        em.flush();
        nextOrderGamma.setPayment(payment);
        nextOrderGammaRepository.saveAndFlush(nextOrderGamma);
        Long paymentId = payment.getId();
        // Get all the nextOrderGammaList where payment equals to paymentId
        defaultNextOrderGammaShouldBeFound("paymentId.equals=" + paymentId);

        // Get all the nextOrderGammaList where payment equals to (paymentId + 1)
        defaultNextOrderGammaShouldNotBeFound("paymentId.equals=" + (paymentId + 1));
    }

    @Test
    @Transactional
    void getAllNextOrderGammasByShipmentIsEqualToSomething() throws Exception {
        NextShipmentGamma shipment;
        if (TestUtil.findAll(em, NextShipmentGamma.class).isEmpty()) {
            nextOrderGammaRepository.saveAndFlush(nextOrderGamma);
            shipment = NextShipmentGammaResourceIT.createEntity();
        } else {
            shipment = TestUtil.findAll(em, NextShipmentGamma.class).get(0);
        }
        em.persist(shipment);
        em.flush();
        nextOrderGamma.setShipment(shipment);
        nextOrderGammaRepository.saveAndFlush(nextOrderGamma);
        Long shipmentId = shipment.getId();
        // Get all the nextOrderGammaList where shipment equals to shipmentId
        defaultNextOrderGammaShouldBeFound("shipmentId.equals=" + shipmentId);

        // Get all the nextOrderGammaList where shipment equals to (shipmentId + 1)
        defaultNextOrderGammaShouldNotBeFound("shipmentId.equals=" + (shipmentId + 1));
    }

    @Test
    @Transactional
    void getAllNextOrderGammasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextOrderGammaRepository.saveAndFlush(nextOrderGamma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextOrderGamma.setTenant(tenant);
        nextOrderGammaRepository.saveAndFlush(nextOrderGamma);
        Long tenantId = tenant.getId();
        // Get all the nextOrderGammaList where tenant equals to tenantId
        defaultNextOrderGammaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextOrderGammaList where tenant equals to (tenantId + 1)
        defaultNextOrderGammaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllNextOrderGammasByCustomerIsEqualToSomething() throws Exception {
        NextCustomerGamma customer;
        if (TestUtil.findAll(em, NextCustomerGamma.class).isEmpty()) {
            nextOrderGammaRepository.saveAndFlush(nextOrderGamma);
            customer = NextCustomerGammaResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, NextCustomerGamma.class).get(0);
        }
        em.persist(customer);
        em.flush();
        nextOrderGamma.setCustomer(customer);
        nextOrderGammaRepository.saveAndFlush(nextOrderGamma);
        Long customerId = customer.getId();
        // Get all the nextOrderGammaList where customer equals to customerId
        defaultNextOrderGammaShouldBeFound("customerId.equals=" + customerId);

        // Get all the nextOrderGammaList where customer equals to (customerId + 1)
        defaultNextOrderGammaShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    private void defaultNextOrderGammaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextOrderGammaShouldBeFound(shouldBeFound);
        defaultNextOrderGammaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextOrderGammaShouldBeFound(String filter) throws Exception {
        restNextOrderGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextOrderGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restNextOrderGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextOrderGammaShouldNotBeFound(String filter) throws Exception {
        restNextOrderGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextOrderGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextOrderGamma() throws Exception {
        // Get the nextOrderGamma
        restNextOrderGammaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextOrderGamma() throws Exception {
        // Initialize the database
        insertedNextOrderGamma = nextOrderGammaRepository.saveAndFlush(nextOrderGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextOrderGamma
        NextOrderGamma updatedNextOrderGamma = nextOrderGammaRepository.findById(nextOrderGamma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextOrderGamma are not directly saved in db
        em.detach(updatedNextOrderGamma);
        updatedNextOrderGamma.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);
        NextOrderGammaDTO nextOrderGammaDTO = nextOrderGammaMapper.toDto(updatedNextOrderGamma);

        restNextOrderGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextOrderGammaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextOrderGammaDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextOrderGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextOrderGammaToMatchAllProperties(updatedNextOrderGamma);
    }

    @Test
    @Transactional
    void putNonExistingNextOrderGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderGamma.setId(longCount.incrementAndGet());

        // Create the NextOrderGamma
        NextOrderGammaDTO nextOrderGammaDTO = nextOrderGammaMapper.toDto(nextOrderGamma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextOrderGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextOrderGammaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextOrderGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrderGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextOrderGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderGamma.setId(longCount.incrementAndGet());

        // Create the NextOrderGamma
        NextOrderGammaDTO nextOrderGammaDTO = nextOrderGammaMapper.toDto(nextOrderGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextOrderGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrderGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextOrderGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderGamma.setId(longCount.incrementAndGet());

        // Create the NextOrderGamma
        NextOrderGammaDTO nextOrderGammaDTO = nextOrderGammaMapper.toDto(nextOrderGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderGammaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderGammaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextOrderGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextOrderGammaWithPatch() throws Exception {
        // Initialize the database
        insertedNextOrderGamma = nextOrderGammaRepository.saveAndFlush(nextOrderGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextOrderGamma using partial update
        NextOrderGamma partialUpdatedNextOrderGamma = new NextOrderGamma();
        partialUpdatedNextOrderGamma.setId(nextOrderGamma.getId());

        restNextOrderGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextOrderGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextOrderGamma))
            )
            .andExpect(status().isOk());

        // Validate the NextOrderGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextOrderGammaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextOrderGamma, nextOrderGamma),
            getPersistedNextOrderGamma(nextOrderGamma)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextOrderGammaWithPatch() throws Exception {
        // Initialize the database
        insertedNextOrderGamma = nextOrderGammaRepository.saveAndFlush(nextOrderGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextOrderGamma using partial update
        NextOrderGamma partialUpdatedNextOrderGamma = new NextOrderGamma();
        partialUpdatedNextOrderGamma.setId(nextOrderGamma.getId());

        partialUpdatedNextOrderGamma.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);

        restNextOrderGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextOrderGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextOrderGamma))
            )
            .andExpect(status().isOk());

        // Validate the NextOrderGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextOrderGammaUpdatableFieldsEquals(partialUpdatedNextOrderGamma, getPersistedNextOrderGamma(partialUpdatedNextOrderGamma));
    }

    @Test
    @Transactional
    void patchNonExistingNextOrderGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderGamma.setId(longCount.incrementAndGet());

        // Create the NextOrderGamma
        NextOrderGammaDTO nextOrderGammaDTO = nextOrderGammaMapper.toDto(nextOrderGamma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextOrderGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextOrderGammaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextOrderGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrderGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextOrderGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderGamma.setId(longCount.incrementAndGet());

        // Create the NextOrderGamma
        NextOrderGammaDTO nextOrderGammaDTO = nextOrderGammaMapper.toDto(nextOrderGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextOrderGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrderGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextOrderGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderGamma.setId(longCount.incrementAndGet());

        // Create the NextOrderGamma
        NextOrderGammaDTO nextOrderGammaDTO = nextOrderGammaMapper.toDto(nextOrderGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderGammaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextOrderGammaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextOrderGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextOrderGamma() throws Exception {
        // Initialize the database
        insertedNextOrderGamma = nextOrderGammaRepository.saveAndFlush(nextOrderGamma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextOrderGamma
        restNextOrderGammaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextOrderGamma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextOrderGammaRepository.count();
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

    protected NextOrderGamma getPersistedNextOrderGamma(NextOrderGamma nextOrderGamma) {
        return nextOrderGammaRepository.findById(nextOrderGamma.getId()).orElseThrow();
    }

    protected void assertPersistedNextOrderGammaToMatchAllProperties(NextOrderGamma expectedNextOrderGamma) {
        assertNextOrderGammaAllPropertiesEquals(expectedNextOrderGamma, getPersistedNextOrderGamma(expectedNextOrderGamma));
    }

    protected void assertPersistedNextOrderGammaToMatchUpdatableProperties(NextOrderGamma expectedNextOrderGamma) {
        assertNextOrderGammaAllUpdatablePropertiesEquals(expectedNextOrderGamma, getPersistedNextOrderGamma(expectedNextOrderGamma));
    }
}
