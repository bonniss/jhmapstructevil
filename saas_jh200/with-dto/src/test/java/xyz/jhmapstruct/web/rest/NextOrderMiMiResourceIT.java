package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextOrderMiMiAsserts.*;
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
import xyz.jhmapstruct.domain.NextCustomerMiMi;
import xyz.jhmapstruct.domain.NextOrderMiMi;
import xyz.jhmapstruct.domain.NextPaymentMiMi;
import xyz.jhmapstruct.domain.NextShipmentMiMi;
import xyz.jhmapstruct.domain.enumeration.OrderMiMiStatus;
import xyz.jhmapstruct.repository.NextOrderMiMiRepository;
import xyz.jhmapstruct.service.NextOrderMiMiService;
import xyz.jhmapstruct.service.dto.NextOrderMiMiDTO;
import xyz.jhmapstruct.service.mapper.NextOrderMiMiMapper;

/**
 * Integration tests for the {@link NextOrderMiMiResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextOrderMiMiResourceIT {

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_PRICE = new BigDecimal(1 - 1);

    private static final OrderMiMiStatus DEFAULT_STATUS = OrderMiMiStatus.PENDING;
    private static final OrderMiMiStatus UPDATED_STATUS = OrderMiMiStatus.SHIPPED;

    private static final String ENTITY_API_URL = "/api/next-order-mi-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextOrderMiMiRepository nextOrderMiMiRepository;

    @Mock
    private NextOrderMiMiRepository nextOrderMiMiRepositoryMock;

    @Autowired
    private NextOrderMiMiMapper nextOrderMiMiMapper;

    @Mock
    private NextOrderMiMiService nextOrderMiMiServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextOrderMiMiMockMvc;

    private NextOrderMiMi nextOrderMiMi;

    private NextOrderMiMi insertedNextOrderMiMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextOrderMiMi createEntity() {
        return new NextOrderMiMi().orderDate(DEFAULT_ORDER_DATE).totalPrice(DEFAULT_TOTAL_PRICE).status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextOrderMiMi createUpdatedEntity() {
        return new NextOrderMiMi().orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        nextOrderMiMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextOrderMiMi != null) {
            nextOrderMiMiRepository.delete(insertedNextOrderMiMi);
            insertedNextOrderMiMi = null;
        }
    }

    @Test
    @Transactional
    void createNextOrderMiMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextOrderMiMi
        NextOrderMiMiDTO nextOrderMiMiDTO = nextOrderMiMiMapper.toDto(nextOrderMiMi);
        var returnedNextOrderMiMiDTO = om.readValue(
            restNextOrderMiMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderMiMiDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextOrderMiMiDTO.class
        );

        // Validate the NextOrderMiMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextOrderMiMi = nextOrderMiMiMapper.toEntity(returnedNextOrderMiMiDTO);
        assertNextOrderMiMiUpdatableFieldsEquals(returnedNextOrderMiMi, getPersistedNextOrderMiMi(returnedNextOrderMiMi));

        insertedNextOrderMiMi = returnedNextOrderMiMi;
    }

    @Test
    @Transactional
    void createNextOrderMiMiWithExistingId() throws Exception {
        // Create the NextOrderMiMi with an existing ID
        nextOrderMiMi.setId(1L);
        NextOrderMiMiDTO nextOrderMiMiDTO = nextOrderMiMiMapper.toDto(nextOrderMiMi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextOrderMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderMiMiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextOrderMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextOrderMiMi.setOrderDate(null);

        // Create the NextOrderMiMi, which fails.
        NextOrderMiMiDTO nextOrderMiMiDTO = nextOrderMiMiMapper.toDto(nextOrderMiMi);

        restNextOrderMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderMiMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextOrderMiMi.setTotalPrice(null);

        // Create the NextOrderMiMi, which fails.
        NextOrderMiMiDTO nextOrderMiMiDTO = nextOrderMiMiMapper.toDto(nextOrderMiMi);

        restNextOrderMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderMiMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextOrderMiMis() throws Exception {
        // Initialize the database
        insertedNextOrderMiMi = nextOrderMiMiRepository.saveAndFlush(nextOrderMiMi);

        // Get all the nextOrderMiMiList
        restNextOrderMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextOrderMiMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextOrderMiMisWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextOrderMiMiServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextOrderMiMiMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextOrderMiMiServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextOrderMiMisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextOrderMiMiServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextOrderMiMiMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextOrderMiMiRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextOrderMiMi() throws Exception {
        // Initialize the database
        insertedNextOrderMiMi = nextOrderMiMiRepository.saveAndFlush(nextOrderMiMi);

        // Get the nextOrderMiMi
        restNextOrderMiMiMockMvc
            .perform(get(ENTITY_API_URL_ID, nextOrderMiMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextOrderMiMi.getId().intValue()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNextOrderMiMisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextOrderMiMi = nextOrderMiMiRepository.saveAndFlush(nextOrderMiMi);

        Long id = nextOrderMiMi.getId();

        defaultNextOrderMiMiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextOrderMiMiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextOrderMiMiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextOrderMiMisByOrderDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderMiMi = nextOrderMiMiRepository.saveAndFlush(nextOrderMiMi);

        // Get all the nextOrderMiMiList where orderDate equals to
        defaultNextOrderMiMiFiltering("orderDate.equals=" + DEFAULT_ORDER_DATE, "orderDate.equals=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllNextOrderMiMisByOrderDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextOrderMiMi = nextOrderMiMiRepository.saveAndFlush(nextOrderMiMi);

        // Get all the nextOrderMiMiList where orderDate in
        defaultNextOrderMiMiFiltering(
            "orderDate.in=" + DEFAULT_ORDER_DATE + "," + UPDATED_ORDER_DATE,
            "orderDate.in=" + UPDATED_ORDER_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextOrderMiMisByOrderDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextOrderMiMi = nextOrderMiMiRepository.saveAndFlush(nextOrderMiMi);

        // Get all the nextOrderMiMiList where orderDate is not null
        defaultNextOrderMiMiFiltering("orderDate.specified=true", "orderDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextOrderMiMisByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderMiMi = nextOrderMiMiRepository.saveAndFlush(nextOrderMiMi);

        // Get all the nextOrderMiMiList where totalPrice equals to
        defaultNextOrderMiMiFiltering("totalPrice.equals=" + DEFAULT_TOTAL_PRICE, "totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllNextOrderMiMisByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextOrderMiMi = nextOrderMiMiRepository.saveAndFlush(nextOrderMiMi);

        // Get all the nextOrderMiMiList where totalPrice in
        defaultNextOrderMiMiFiltering(
            "totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE,
            "totalPrice.in=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllNextOrderMiMisByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextOrderMiMi = nextOrderMiMiRepository.saveAndFlush(nextOrderMiMi);

        // Get all the nextOrderMiMiList where totalPrice is not null
        defaultNextOrderMiMiFiltering("totalPrice.specified=true", "totalPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllNextOrderMiMisByTotalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderMiMi = nextOrderMiMiRepository.saveAndFlush(nextOrderMiMi);

        // Get all the nextOrderMiMiList where totalPrice is greater than or equal to
        defaultNextOrderMiMiFiltering(
            "totalPrice.greaterThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.greaterThanOrEqual=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllNextOrderMiMisByTotalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderMiMi = nextOrderMiMiRepository.saveAndFlush(nextOrderMiMi);

        // Get all the nextOrderMiMiList where totalPrice is less than or equal to
        defaultNextOrderMiMiFiltering(
            "totalPrice.lessThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.lessThanOrEqual=" + SMALLER_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllNextOrderMiMisByTotalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextOrderMiMi = nextOrderMiMiRepository.saveAndFlush(nextOrderMiMi);

        // Get all the nextOrderMiMiList where totalPrice is less than
        defaultNextOrderMiMiFiltering("totalPrice.lessThan=" + UPDATED_TOTAL_PRICE, "totalPrice.lessThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllNextOrderMiMisByTotalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextOrderMiMi = nextOrderMiMiRepository.saveAndFlush(nextOrderMiMi);

        // Get all the nextOrderMiMiList where totalPrice is greater than
        defaultNextOrderMiMiFiltering("totalPrice.greaterThan=" + SMALLER_TOTAL_PRICE, "totalPrice.greaterThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllNextOrderMiMisByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderMiMi = nextOrderMiMiRepository.saveAndFlush(nextOrderMiMi);

        // Get all the nextOrderMiMiList where status equals to
        defaultNextOrderMiMiFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllNextOrderMiMisByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextOrderMiMi = nextOrderMiMiRepository.saveAndFlush(nextOrderMiMi);

        // Get all the nextOrderMiMiList where status in
        defaultNextOrderMiMiFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllNextOrderMiMisByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextOrderMiMi = nextOrderMiMiRepository.saveAndFlush(nextOrderMiMi);

        // Get all the nextOrderMiMiList where status is not null
        defaultNextOrderMiMiFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllNextOrderMiMisByPaymentIsEqualToSomething() throws Exception {
        NextPaymentMiMi payment;
        if (TestUtil.findAll(em, NextPaymentMiMi.class).isEmpty()) {
            nextOrderMiMiRepository.saveAndFlush(nextOrderMiMi);
            payment = NextPaymentMiMiResourceIT.createEntity();
        } else {
            payment = TestUtil.findAll(em, NextPaymentMiMi.class).get(0);
        }
        em.persist(payment);
        em.flush();
        nextOrderMiMi.setPayment(payment);
        nextOrderMiMiRepository.saveAndFlush(nextOrderMiMi);
        Long paymentId = payment.getId();
        // Get all the nextOrderMiMiList where payment equals to paymentId
        defaultNextOrderMiMiShouldBeFound("paymentId.equals=" + paymentId);

        // Get all the nextOrderMiMiList where payment equals to (paymentId + 1)
        defaultNextOrderMiMiShouldNotBeFound("paymentId.equals=" + (paymentId + 1));
    }

    @Test
    @Transactional
    void getAllNextOrderMiMisByShipmentIsEqualToSomething() throws Exception {
        NextShipmentMiMi shipment;
        if (TestUtil.findAll(em, NextShipmentMiMi.class).isEmpty()) {
            nextOrderMiMiRepository.saveAndFlush(nextOrderMiMi);
            shipment = NextShipmentMiMiResourceIT.createEntity();
        } else {
            shipment = TestUtil.findAll(em, NextShipmentMiMi.class).get(0);
        }
        em.persist(shipment);
        em.flush();
        nextOrderMiMi.setShipment(shipment);
        nextOrderMiMiRepository.saveAndFlush(nextOrderMiMi);
        Long shipmentId = shipment.getId();
        // Get all the nextOrderMiMiList where shipment equals to shipmentId
        defaultNextOrderMiMiShouldBeFound("shipmentId.equals=" + shipmentId);

        // Get all the nextOrderMiMiList where shipment equals to (shipmentId + 1)
        defaultNextOrderMiMiShouldNotBeFound("shipmentId.equals=" + (shipmentId + 1));
    }

    @Test
    @Transactional
    void getAllNextOrderMiMisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextOrderMiMiRepository.saveAndFlush(nextOrderMiMi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextOrderMiMi.setTenant(tenant);
        nextOrderMiMiRepository.saveAndFlush(nextOrderMiMi);
        Long tenantId = tenant.getId();
        // Get all the nextOrderMiMiList where tenant equals to tenantId
        defaultNextOrderMiMiShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextOrderMiMiList where tenant equals to (tenantId + 1)
        defaultNextOrderMiMiShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllNextOrderMiMisByCustomerIsEqualToSomething() throws Exception {
        NextCustomerMiMi customer;
        if (TestUtil.findAll(em, NextCustomerMiMi.class).isEmpty()) {
            nextOrderMiMiRepository.saveAndFlush(nextOrderMiMi);
            customer = NextCustomerMiMiResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, NextCustomerMiMi.class).get(0);
        }
        em.persist(customer);
        em.flush();
        nextOrderMiMi.setCustomer(customer);
        nextOrderMiMiRepository.saveAndFlush(nextOrderMiMi);
        Long customerId = customer.getId();
        // Get all the nextOrderMiMiList where customer equals to customerId
        defaultNextOrderMiMiShouldBeFound("customerId.equals=" + customerId);

        // Get all the nextOrderMiMiList where customer equals to (customerId + 1)
        defaultNextOrderMiMiShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    private void defaultNextOrderMiMiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextOrderMiMiShouldBeFound(shouldBeFound);
        defaultNextOrderMiMiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextOrderMiMiShouldBeFound(String filter) throws Exception {
        restNextOrderMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextOrderMiMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restNextOrderMiMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextOrderMiMiShouldNotBeFound(String filter) throws Exception {
        restNextOrderMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextOrderMiMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextOrderMiMi() throws Exception {
        // Get the nextOrderMiMi
        restNextOrderMiMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextOrderMiMi() throws Exception {
        // Initialize the database
        insertedNextOrderMiMi = nextOrderMiMiRepository.saveAndFlush(nextOrderMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextOrderMiMi
        NextOrderMiMi updatedNextOrderMiMi = nextOrderMiMiRepository.findById(nextOrderMiMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextOrderMiMi are not directly saved in db
        em.detach(updatedNextOrderMiMi);
        updatedNextOrderMiMi.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);
        NextOrderMiMiDTO nextOrderMiMiDTO = nextOrderMiMiMapper.toDto(updatedNextOrderMiMi);

        restNextOrderMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextOrderMiMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextOrderMiMiDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextOrderMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextOrderMiMiToMatchAllProperties(updatedNextOrderMiMi);
    }

    @Test
    @Transactional
    void putNonExistingNextOrderMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderMiMi.setId(longCount.incrementAndGet());

        // Create the NextOrderMiMi
        NextOrderMiMiDTO nextOrderMiMiDTO = nextOrderMiMiMapper.toDto(nextOrderMiMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextOrderMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextOrderMiMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextOrderMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrderMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextOrderMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderMiMi.setId(longCount.incrementAndGet());

        // Create the NextOrderMiMi
        NextOrderMiMiDTO nextOrderMiMiDTO = nextOrderMiMiMapper.toDto(nextOrderMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextOrderMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrderMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextOrderMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderMiMi.setId(longCount.incrementAndGet());

        // Create the NextOrderMiMi
        NextOrderMiMiDTO nextOrderMiMiDTO = nextOrderMiMiMapper.toDto(nextOrderMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderMiMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderMiMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextOrderMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextOrderMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextOrderMiMi = nextOrderMiMiRepository.saveAndFlush(nextOrderMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextOrderMiMi using partial update
        NextOrderMiMi partialUpdatedNextOrderMiMi = new NextOrderMiMi();
        partialUpdatedNextOrderMiMi.setId(nextOrderMiMi.getId());

        partialUpdatedNextOrderMiMi.orderDate(UPDATED_ORDER_DATE).status(UPDATED_STATUS);

        restNextOrderMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextOrderMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextOrderMiMi))
            )
            .andExpect(status().isOk());

        // Validate the NextOrderMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextOrderMiMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextOrderMiMi, nextOrderMiMi),
            getPersistedNextOrderMiMi(nextOrderMiMi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextOrderMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextOrderMiMi = nextOrderMiMiRepository.saveAndFlush(nextOrderMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextOrderMiMi using partial update
        NextOrderMiMi partialUpdatedNextOrderMiMi = new NextOrderMiMi();
        partialUpdatedNextOrderMiMi.setId(nextOrderMiMi.getId());

        partialUpdatedNextOrderMiMi.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);

        restNextOrderMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextOrderMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextOrderMiMi))
            )
            .andExpect(status().isOk());

        // Validate the NextOrderMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextOrderMiMiUpdatableFieldsEquals(partialUpdatedNextOrderMiMi, getPersistedNextOrderMiMi(partialUpdatedNextOrderMiMi));
    }

    @Test
    @Transactional
    void patchNonExistingNextOrderMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderMiMi.setId(longCount.incrementAndGet());

        // Create the NextOrderMiMi
        NextOrderMiMiDTO nextOrderMiMiDTO = nextOrderMiMiMapper.toDto(nextOrderMiMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextOrderMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextOrderMiMiDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextOrderMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrderMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextOrderMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderMiMi.setId(longCount.incrementAndGet());

        // Create the NextOrderMiMi
        NextOrderMiMiDTO nextOrderMiMiDTO = nextOrderMiMiMapper.toDto(nextOrderMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextOrderMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrderMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextOrderMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderMiMi.setId(longCount.incrementAndGet());

        // Create the NextOrderMiMi
        NextOrderMiMiDTO nextOrderMiMiDTO = nextOrderMiMiMapper.toDto(nextOrderMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderMiMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextOrderMiMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextOrderMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextOrderMiMi() throws Exception {
        // Initialize the database
        insertedNextOrderMiMi = nextOrderMiMiRepository.saveAndFlush(nextOrderMiMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextOrderMiMi
        restNextOrderMiMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextOrderMiMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextOrderMiMiRepository.count();
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

    protected NextOrderMiMi getPersistedNextOrderMiMi(NextOrderMiMi nextOrderMiMi) {
        return nextOrderMiMiRepository.findById(nextOrderMiMi.getId()).orElseThrow();
    }

    protected void assertPersistedNextOrderMiMiToMatchAllProperties(NextOrderMiMi expectedNextOrderMiMi) {
        assertNextOrderMiMiAllPropertiesEquals(expectedNextOrderMiMi, getPersistedNextOrderMiMi(expectedNextOrderMiMi));
    }

    protected void assertPersistedNextOrderMiMiToMatchUpdatableProperties(NextOrderMiMi expectedNextOrderMiMi) {
        assertNextOrderMiMiAllUpdatablePropertiesEquals(expectedNextOrderMiMi, getPersistedNextOrderMiMi(expectedNextOrderMiMi));
    }
}
