package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextOrderAlphaAsserts.*;
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
import xyz.jhmapstruct.domain.NextCustomerAlpha;
import xyz.jhmapstruct.domain.NextOrderAlpha;
import xyz.jhmapstruct.domain.NextPaymentAlpha;
import xyz.jhmapstruct.domain.NextShipmentAlpha;
import xyz.jhmapstruct.domain.enumeration.OrderAlphaStatus;
import xyz.jhmapstruct.repository.NextOrderAlphaRepository;
import xyz.jhmapstruct.service.NextOrderAlphaService;

/**
 * Integration tests for the {@link NextOrderAlphaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextOrderAlphaResourceIT {

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_PRICE = new BigDecimal(1 - 1);

    private static final OrderAlphaStatus DEFAULT_STATUS = OrderAlphaStatus.PENDING;
    private static final OrderAlphaStatus UPDATED_STATUS = OrderAlphaStatus.SHIPPED;

    private static final String ENTITY_API_URL = "/api/next-order-alphas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextOrderAlphaRepository nextOrderAlphaRepository;

    @Mock
    private NextOrderAlphaRepository nextOrderAlphaRepositoryMock;

    @Mock
    private NextOrderAlphaService nextOrderAlphaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextOrderAlphaMockMvc;

    private NextOrderAlpha nextOrderAlpha;

    private NextOrderAlpha insertedNextOrderAlpha;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextOrderAlpha createEntity() {
        return new NextOrderAlpha().orderDate(DEFAULT_ORDER_DATE).totalPrice(DEFAULT_TOTAL_PRICE).status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextOrderAlpha createUpdatedEntity() {
        return new NextOrderAlpha().orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        nextOrderAlpha = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextOrderAlpha != null) {
            nextOrderAlphaRepository.delete(insertedNextOrderAlpha);
            insertedNextOrderAlpha = null;
        }
    }

    @Test
    @Transactional
    void createNextOrderAlpha() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextOrderAlpha
        var returnedNextOrderAlpha = om.readValue(
            restNextOrderAlphaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderAlpha)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextOrderAlpha.class
        );

        // Validate the NextOrderAlpha in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextOrderAlphaUpdatableFieldsEquals(returnedNextOrderAlpha, getPersistedNextOrderAlpha(returnedNextOrderAlpha));

        insertedNextOrderAlpha = returnedNextOrderAlpha;
    }

    @Test
    @Transactional
    void createNextOrderAlphaWithExistingId() throws Exception {
        // Create the NextOrderAlpha with an existing ID
        nextOrderAlpha.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextOrderAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderAlpha)))
            .andExpect(status().isBadRequest());

        // Validate the NextOrderAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextOrderAlpha.setOrderDate(null);

        // Create the NextOrderAlpha, which fails.

        restNextOrderAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderAlpha)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextOrderAlpha.setTotalPrice(null);

        // Create the NextOrderAlpha, which fails.

        restNextOrderAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderAlpha)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextOrderAlphas() throws Exception {
        // Initialize the database
        insertedNextOrderAlpha = nextOrderAlphaRepository.saveAndFlush(nextOrderAlpha);

        // Get all the nextOrderAlphaList
        restNextOrderAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextOrderAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextOrderAlphasWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextOrderAlphaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextOrderAlphaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextOrderAlphaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextOrderAlphasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextOrderAlphaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextOrderAlphaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextOrderAlphaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextOrderAlpha() throws Exception {
        // Initialize the database
        insertedNextOrderAlpha = nextOrderAlphaRepository.saveAndFlush(nextOrderAlpha);

        // Get the nextOrderAlpha
        restNextOrderAlphaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextOrderAlpha.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextOrderAlpha.getId().intValue()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNextOrderAlphasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextOrderAlpha = nextOrderAlphaRepository.saveAndFlush(nextOrderAlpha);

        Long id = nextOrderAlpha.getId();

        defaultNextOrderAlphaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextOrderAlphaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextOrderAlphaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextOrderAlphasByOrderDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderAlpha = nextOrderAlphaRepository.saveAndFlush(nextOrderAlpha);

        // Get all the nextOrderAlphaList where orderDate equals to
        defaultNextOrderAlphaFiltering("orderDate.equals=" + DEFAULT_ORDER_DATE, "orderDate.equals=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllNextOrderAlphasByOrderDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextOrderAlpha = nextOrderAlphaRepository.saveAndFlush(nextOrderAlpha);

        // Get all the nextOrderAlphaList where orderDate in
        defaultNextOrderAlphaFiltering(
            "orderDate.in=" + DEFAULT_ORDER_DATE + "," + UPDATED_ORDER_DATE,
            "orderDate.in=" + UPDATED_ORDER_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextOrderAlphasByOrderDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextOrderAlpha = nextOrderAlphaRepository.saveAndFlush(nextOrderAlpha);

        // Get all the nextOrderAlphaList where orderDate is not null
        defaultNextOrderAlphaFiltering("orderDate.specified=true", "orderDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextOrderAlphasByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderAlpha = nextOrderAlphaRepository.saveAndFlush(nextOrderAlpha);

        // Get all the nextOrderAlphaList where totalPrice equals to
        defaultNextOrderAlphaFiltering("totalPrice.equals=" + DEFAULT_TOTAL_PRICE, "totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllNextOrderAlphasByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextOrderAlpha = nextOrderAlphaRepository.saveAndFlush(nextOrderAlpha);

        // Get all the nextOrderAlphaList where totalPrice in
        defaultNextOrderAlphaFiltering(
            "totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE,
            "totalPrice.in=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllNextOrderAlphasByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextOrderAlpha = nextOrderAlphaRepository.saveAndFlush(nextOrderAlpha);

        // Get all the nextOrderAlphaList where totalPrice is not null
        defaultNextOrderAlphaFiltering("totalPrice.specified=true", "totalPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllNextOrderAlphasByTotalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderAlpha = nextOrderAlphaRepository.saveAndFlush(nextOrderAlpha);

        // Get all the nextOrderAlphaList where totalPrice is greater than or equal to
        defaultNextOrderAlphaFiltering(
            "totalPrice.greaterThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.greaterThanOrEqual=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllNextOrderAlphasByTotalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderAlpha = nextOrderAlphaRepository.saveAndFlush(nextOrderAlpha);

        // Get all the nextOrderAlphaList where totalPrice is less than or equal to
        defaultNextOrderAlphaFiltering(
            "totalPrice.lessThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.lessThanOrEqual=" + SMALLER_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllNextOrderAlphasByTotalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextOrderAlpha = nextOrderAlphaRepository.saveAndFlush(nextOrderAlpha);

        // Get all the nextOrderAlphaList where totalPrice is less than
        defaultNextOrderAlphaFiltering("totalPrice.lessThan=" + UPDATED_TOTAL_PRICE, "totalPrice.lessThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllNextOrderAlphasByTotalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextOrderAlpha = nextOrderAlphaRepository.saveAndFlush(nextOrderAlpha);

        // Get all the nextOrderAlphaList where totalPrice is greater than
        defaultNextOrderAlphaFiltering("totalPrice.greaterThan=" + SMALLER_TOTAL_PRICE, "totalPrice.greaterThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllNextOrderAlphasByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderAlpha = nextOrderAlphaRepository.saveAndFlush(nextOrderAlpha);

        // Get all the nextOrderAlphaList where status equals to
        defaultNextOrderAlphaFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllNextOrderAlphasByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextOrderAlpha = nextOrderAlphaRepository.saveAndFlush(nextOrderAlpha);

        // Get all the nextOrderAlphaList where status in
        defaultNextOrderAlphaFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllNextOrderAlphasByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextOrderAlpha = nextOrderAlphaRepository.saveAndFlush(nextOrderAlpha);

        // Get all the nextOrderAlphaList where status is not null
        defaultNextOrderAlphaFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllNextOrderAlphasByPaymentIsEqualToSomething() throws Exception {
        NextPaymentAlpha payment;
        if (TestUtil.findAll(em, NextPaymentAlpha.class).isEmpty()) {
            nextOrderAlphaRepository.saveAndFlush(nextOrderAlpha);
            payment = NextPaymentAlphaResourceIT.createEntity();
        } else {
            payment = TestUtil.findAll(em, NextPaymentAlpha.class).get(0);
        }
        em.persist(payment);
        em.flush();
        nextOrderAlpha.setPayment(payment);
        nextOrderAlphaRepository.saveAndFlush(nextOrderAlpha);
        Long paymentId = payment.getId();
        // Get all the nextOrderAlphaList where payment equals to paymentId
        defaultNextOrderAlphaShouldBeFound("paymentId.equals=" + paymentId);

        // Get all the nextOrderAlphaList where payment equals to (paymentId + 1)
        defaultNextOrderAlphaShouldNotBeFound("paymentId.equals=" + (paymentId + 1));
    }

    @Test
    @Transactional
    void getAllNextOrderAlphasByShipmentIsEqualToSomething() throws Exception {
        NextShipmentAlpha shipment;
        if (TestUtil.findAll(em, NextShipmentAlpha.class).isEmpty()) {
            nextOrderAlphaRepository.saveAndFlush(nextOrderAlpha);
            shipment = NextShipmentAlphaResourceIT.createEntity();
        } else {
            shipment = TestUtil.findAll(em, NextShipmentAlpha.class).get(0);
        }
        em.persist(shipment);
        em.flush();
        nextOrderAlpha.setShipment(shipment);
        nextOrderAlphaRepository.saveAndFlush(nextOrderAlpha);
        Long shipmentId = shipment.getId();
        // Get all the nextOrderAlphaList where shipment equals to shipmentId
        defaultNextOrderAlphaShouldBeFound("shipmentId.equals=" + shipmentId);

        // Get all the nextOrderAlphaList where shipment equals to (shipmentId + 1)
        defaultNextOrderAlphaShouldNotBeFound("shipmentId.equals=" + (shipmentId + 1));
    }

    @Test
    @Transactional
    void getAllNextOrderAlphasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextOrderAlphaRepository.saveAndFlush(nextOrderAlpha);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextOrderAlpha.setTenant(tenant);
        nextOrderAlphaRepository.saveAndFlush(nextOrderAlpha);
        Long tenantId = tenant.getId();
        // Get all the nextOrderAlphaList where tenant equals to tenantId
        defaultNextOrderAlphaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextOrderAlphaList where tenant equals to (tenantId + 1)
        defaultNextOrderAlphaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllNextOrderAlphasByCustomerIsEqualToSomething() throws Exception {
        NextCustomerAlpha customer;
        if (TestUtil.findAll(em, NextCustomerAlpha.class).isEmpty()) {
            nextOrderAlphaRepository.saveAndFlush(nextOrderAlpha);
            customer = NextCustomerAlphaResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, NextCustomerAlpha.class).get(0);
        }
        em.persist(customer);
        em.flush();
        nextOrderAlpha.setCustomer(customer);
        nextOrderAlphaRepository.saveAndFlush(nextOrderAlpha);
        Long customerId = customer.getId();
        // Get all the nextOrderAlphaList where customer equals to customerId
        defaultNextOrderAlphaShouldBeFound("customerId.equals=" + customerId);

        // Get all the nextOrderAlphaList where customer equals to (customerId + 1)
        defaultNextOrderAlphaShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    private void defaultNextOrderAlphaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextOrderAlphaShouldBeFound(shouldBeFound);
        defaultNextOrderAlphaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextOrderAlphaShouldBeFound(String filter) throws Exception {
        restNextOrderAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextOrderAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restNextOrderAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextOrderAlphaShouldNotBeFound(String filter) throws Exception {
        restNextOrderAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextOrderAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextOrderAlpha() throws Exception {
        // Get the nextOrderAlpha
        restNextOrderAlphaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextOrderAlpha() throws Exception {
        // Initialize the database
        insertedNextOrderAlpha = nextOrderAlphaRepository.saveAndFlush(nextOrderAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextOrderAlpha
        NextOrderAlpha updatedNextOrderAlpha = nextOrderAlphaRepository.findById(nextOrderAlpha.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextOrderAlpha are not directly saved in db
        em.detach(updatedNextOrderAlpha);
        updatedNextOrderAlpha.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);

        restNextOrderAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextOrderAlpha.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextOrderAlpha))
            )
            .andExpect(status().isOk());

        // Validate the NextOrderAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextOrderAlphaToMatchAllProperties(updatedNextOrderAlpha);
    }

    @Test
    @Transactional
    void putNonExistingNextOrderAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderAlpha.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextOrderAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextOrderAlpha.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextOrderAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrderAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextOrderAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextOrderAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrderAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextOrderAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderAlphaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderAlpha)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextOrderAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextOrderAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedNextOrderAlpha = nextOrderAlphaRepository.saveAndFlush(nextOrderAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextOrderAlpha using partial update
        NextOrderAlpha partialUpdatedNextOrderAlpha = new NextOrderAlpha();
        partialUpdatedNextOrderAlpha.setId(nextOrderAlpha.getId());

        partialUpdatedNextOrderAlpha.totalPrice(UPDATED_TOTAL_PRICE);

        restNextOrderAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextOrderAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextOrderAlpha))
            )
            .andExpect(status().isOk());

        // Validate the NextOrderAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextOrderAlphaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextOrderAlpha, nextOrderAlpha),
            getPersistedNextOrderAlpha(nextOrderAlpha)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextOrderAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedNextOrderAlpha = nextOrderAlphaRepository.saveAndFlush(nextOrderAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextOrderAlpha using partial update
        NextOrderAlpha partialUpdatedNextOrderAlpha = new NextOrderAlpha();
        partialUpdatedNextOrderAlpha.setId(nextOrderAlpha.getId());

        partialUpdatedNextOrderAlpha.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);

        restNextOrderAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextOrderAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextOrderAlpha))
            )
            .andExpect(status().isOk());

        // Validate the NextOrderAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextOrderAlphaUpdatableFieldsEquals(partialUpdatedNextOrderAlpha, getPersistedNextOrderAlpha(partialUpdatedNextOrderAlpha));
    }

    @Test
    @Transactional
    void patchNonExistingNextOrderAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderAlpha.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextOrderAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextOrderAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextOrderAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrderAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextOrderAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextOrderAlpha))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrderAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextOrderAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderAlpha.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderAlphaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextOrderAlpha)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextOrderAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextOrderAlpha() throws Exception {
        // Initialize the database
        insertedNextOrderAlpha = nextOrderAlphaRepository.saveAndFlush(nextOrderAlpha);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextOrderAlpha
        restNextOrderAlphaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextOrderAlpha.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextOrderAlphaRepository.count();
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

    protected NextOrderAlpha getPersistedNextOrderAlpha(NextOrderAlpha nextOrderAlpha) {
        return nextOrderAlphaRepository.findById(nextOrderAlpha.getId()).orElseThrow();
    }

    protected void assertPersistedNextOrderAlphaToMatchAllProperties(NextOrderAlpha expectedNextOrderAlpha) {
        assertNextOrderAlphaAllPropertiesEquals(expectedNextOrderAlpha, getPersistedNextOrderAlpha(expectedNextOrderAlpha));
    }

    protected void assertPersistedNextOrderAlphaToMatchUpdatableProperties(NextOrderAlpha expectedNextOrderAlpha) {
        assertNextOrderAlphaAllUpdatablePropertiesEquals(expectedNextOrderAlpha, getPersistedNextOrderAlpha(expectedNextOrderAlpha));
    }
}
