package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextOrderMiAsserts.*;
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
import xyz.jhmapstruct.domain.CustomerMi;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextOrderMi;
import xyz.jhmapstruct.domain.PaymentMi;
import xyz.jhmapstruct.domain.ShipmentMi;
import xyz.jhmapstruct.domain.enumeration.OrderMiStatus;
import xyz.jhmapstruct.repository.NextOrderMiRepository;
import xyz.jhmapstruct.service.NextOrderMiService;

/**
 * Integration tests for the {@link NextOrderMiResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextOrderMiResourceIT {

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_PRICE = new BigDecimal(1 - 1);

    private static final OrderMiStatus DEFAULT_STATUS = OrderMiStatus.PENDING;
    private static final OrderMiStatus UPDATED_STATUS = OrderMiStatus.SHIPPED;

    private static final String ENTITY_API_URL = "/api/next-order-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextOrderMiRepository nextOrderMiRepository;

    @Mock
    private NextOrderMiRepository nextOrderMiRepositoryMock;

    @Mock
    private NextOrderMiService nextOrderMiServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextOrderMiMockMvc;

    private NextOrderMi nextOrderMi;

    private NextOrderMi insertedNextOrderMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextOrderMi createEntity() {
        return new NextOrderMi().orderDate(DEFAULT_ORDER_DATE).totalPrice(DEFAULT_TOTAL_PRICE).status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextOrderMi createUpdatedEntity() {
        return new NextOrderMi().orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        nextOrderMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextOrderMi != null) {
            nextOrderMiRepository.delete(insertedNextOrderMi);
            insertedNextOrderMi = null;
        }
    }

    @Test
    @Transactional
    void createNextOrderMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextOrderMi
        var returnedNextOrderMi = om.readValue(
            restNextOrderMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderMi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextOrderMi.class
        );

        // Validate the NextOrderMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextOrderMiUpdatableFieldsEquals(returnedNextOrderMi, getPersistedNextOrderMi(returnedNextOrderMi));

        insertedNextOrderMi = returnedNextOrderMi;
    }

    @Test
    @Transactional
    void createNextOrderMiWithExistingId() throws Exception {
        // Create the NextOrderMi with an existing ID
        nextOrderMi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextOrderMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderMi)))
            .andExpect(status().isBadRequest());

        // Validate the NextOrderMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextOrderMi.setOrderDate(null);

        // Create the NextOrderMi, which fails.

        restNextOrderMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextOrderMi.setTotalPrice(null);

        // Create the NextOrderMi, which fails.

        restNextOrderMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextOrderMis() throws Exception {
        // Initialize the database
        insertedNextOrderMi = nextOrderMiRepository.saveAndFlush(nextOrderMi);

        // Get all the nextOrderMiList
        restNextOrderMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextOrderMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextOrderMisWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextOrderMiServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextOrderMiMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextOrderMiServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextOrderMisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextOrderMiServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextOrderMiMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextOrderMiRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextOrderMi() throws Exception {
        // Initialize the database
        insertedNextOrderMi = nextOrderMiRepository.saveAndFlush(nextOrderMi);

        // Get the nextOrderMi
        restNextOrderMiMockMvc
            .perform(get(ENTITY_API_URL_ID, nextOrderMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextOrderMi.getId().intValue()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNextOrderMisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextOrderMi = nextOrderMiRepository.saveAndFlush(nextOrderMi);

        Long id = nextOrderMi.getId();

        defaultNextOrderMiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextOrderMiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextOrderMiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextOrderMisByOrderDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderMi = nextOrderMiRepository.saveAndFlush(nextOrderMi);

        // Get all the nextOrderMiList where orderDate equals to
        defaultNextOrderMiFiltering("orderDate.equals=" + DEFAULT_ORDER_DATE, "orderDate.equals=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllNextOrderMisByOrderDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextOrderMi = nextOrderMiRepository.saveAndFlush(nextOrderMi);

        // Get all the nextOrderMiList where orderDate in
        defaultNextOrderMiFiltering("orderDate.in=" + DEFAULT_ORDER_DATE + "," + UPDATED_ORDER_DATE, "orderDate.in=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllNextOrderMisByOrderDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextOrderMi = nextOrderMiRepository.saveAndFlush(nextOrderMi);

        // Get all the nextOrderMiList where orderDate is not null
        defaultNextOrderMiFiltering("orderDate.specified=true", "orderDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextOrderMisByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderMi = nextOrderMiRepository.saveAndFlush(nextOrderMi);

        // Get all the nextOrderMiList where totalPrice equals to
        defaultNextOrderMiFiltering("totalPrice.equals=" + DEFAULT_TOTAL_PRICE, "totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllNextOrderMisByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextOrderMi = nextOrderMiRepository.saveAndFlush(nextOrderMi);

        // Get all the nextOrderMiList where totalPrice in
        defaultNextOrderMiFiltering(
            "totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE,
            "totalPrice.in=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllNextOrderMisByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextOrderMi = nextOrderMiRepository.saveAndFlush(nextOrderMi);

        // Get all the nextOrderMiList where totalPrice is not null
        defaultNextOrderMiFiltering("totalPrice.specified=true", "totalPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllNextOrderMisByTotalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderMi = nextOrderMiRepository.saveAndFlush(nextOrderMi);

        // Get all the nextOrderMiList where totalPrice is greater than or equal to
        defaultNextOrderMiFiltering(
            "totalPrice.greaterThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.greaterThanOrEqual=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllNextOrderMisByTotalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderMi = nextOrderMiRepository.saveAndFlush(nextOrderMi);

        // Get all the nextOrderMiList where totalPrice is less than or equal to
        defaultNextOrderMiFiltering(
            "totalPrice.lessThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.lessThanOrEqual=" + SMALLER_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllNextOrderMisByTotalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextOrderMi = nextOrderMiRepository.saveAndFlush(nextOrderMi);

        // Get all the nextOrderMiList where totalPrice is less than
        defaultNextOrderMiFiltering("totalPrice.lessThan=" + UPDATED_TOTAL_PRICE, "totalPrice.lessThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllNextOrderMisByTotalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextOrderMi = nextOrderMiRepository.saveAndFlush(nextOrderMi);

        // Get all the nextOrderMiList where totalPrice is greater than
        defaultNextOrderMiFiltering("totalPrice.greaterThan=" + SMALLER_TOTAL_PRICE, "totalPrice.greaterThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllNextOrderMisByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderMi = nextOrderMiRepository.saveAndFlush(nextOrderMi);

        // Get all the nextOrderMiList where status equals to
        defaultNextOrderMiFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllNextOrderMisByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextOrderMi = nextOrderMiRepository.saveAndFlush(nextOrderMi);

        // Get all the nextOrderMiList where status in
        defaultNextOrderMiFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllNextOrderMisByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextOrderMi = nextOrderMiRepository.saveAndFlush(nextOrderMi);

        // Get all the nextOrderMiList where status is not null
        defaultNextOrderMiFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllNextOrderMisByPaymentIsEqualToSomething() throws Exception {
        PaymentMi payment;
        if (TestUtil.findAll(em, PaymentMi.class).isEmpty()) {
            nextOrderMiRepository.saveAndFlush(nextOrderMi);
            payment = PaymentMiResourceIT.createEntity();
        } else {
            payment = TestUtil.findAll(em, PaymentMi.class).get(0);
        }
        em.persist(payment);
        em.flush();
        nextOrderMi.setPayment(payment);
        nextOrderMiRepository.saveAndFlush(nextOrderMi);
        Long paymentId = payment.getId();
        // Get all the nextOrderMiList where payment equals to paymentId
        defaultNextOrderMiShouldBeFound("paymentId.equals=" + paymentId);

        // Get all the nextOrderMiList where payment equals to (paymentId + 1)
        defaultNextOrderMiShouldNotBeFound("paymentId.equals=" + (paymentId + 1));
    }

    @Test
    @Transactional
    void getAllNextOrderMisByShipmentIsEqualToSomething() throws Exception {
        ShipmentMi shipment;
        if (TestUtil.findAll(em, ShipmentMi.class).isEmpty()) {
            nextOrderMiRepository.saveAndFlush(nextOrderMi);
            shipment = ShipmentMiResourceIT.createEntity();
        } else {
            shipment = TestUtil.findAll(em, ShipmentMi.class).get(0);
        }
        em.persist(shipment);
        em.flush();
        nextOrderMi.setShipment(shipment);
        nextOrderMiRepository.saveAndFlush(nextOrderMi);
        Long shipmentId = shipment.getId();
        // Get all the nextOrderMiList where shipment equals to shipmentId
        defaultNextOrderMiShouldBeFound("shipmentId.equals=" + shipmentId);

        // Get all the nextOrderMiList where shipment equals to (shipmentId + 1)
        defaultNextOrderMiShouldNotBeFound("shipmentId.equals=" + (shipmentId + 1));
    }

    @Test
    @Transactional
    void getAllNextOrderMisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextOrderMiRepository.saveAndFlush(nextOrderMi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextOrderMi.setTenant(tenant);
        nextOrderMiRepository.saveAndFlush(nextOrderMi);
        Long tenantId = tenant.getId();
        // Get all the nextOrderMiList where tenant equals to tenantId
        defaultNextOrderMiShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextOrderMiList where tenant equals to (tenantId + 1)
        defaultNextOrderMiShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllNextOrderMisByCustomerIsEqualToSomething() throws Exception {
        CustomerMi customer;
        if (TestUtil.findAll(em, CustomerMi.class).isEmpty()) {
            nextOrderMiRepository.saveAndFlush(nextOrderMi);
            customer = CustomerMiResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, CustomerMi.class).get(0);
        }
        em.persist(customer);
        em.flush();
        nextOrderMi.setCustomer(customer);
        nextOrderMiRepository.saveAndFlush(nextOrderMi);
        Long customerId = customer.getId();
        // Get all the nextOrderMiList where customer equals to customerId
        defaultNextOrderMiShouldBeFound("customerId.equals=" + customerId);

        // Get all the nextOrderMiList where customer equals to (customerId + 1)
        defaultNextOrderMiShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    private void defaultNextOrderMiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextOrderMiShouldBeFound(shouldBeFound);
        defaultNextOrderMiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextOrderMiShouldBeFound(String filter) throws Exception {
        restNextOrderMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextOrderMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restNextOrderMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextOrderMiShouldNotBeFound(String filter) throws Exception {
        restNextOrderMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextOrderMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextOrderMi() throws Exception {
        // Get the nextOrderMi
        restNextOrderMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextOrderMi() throws Exception {
        // Initialize the database
        insertedNextOrderMi = nextOrderMiRepository.saveAndFlush(nextOrderMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextOrderMi
        NextOrderMi updatedNextOrderMi = nextOrderMiRepository.findById(nextOrderMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextOrderMi are not directly saved in db
        em.detach(updatedNextOrderMi);
        updatedNextOrderMi.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);

        restNextOrderMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextOrderMi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextOrderMi))
            )
            .andExpect(status().isOk());

        // Validate the NextOrderMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextOrderMiToMatchAllProperties(updatedNextOrderMi);
    }

    @Test
    @Transactional
    void putNonExistingNextOrderMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderMi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextOrderMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextOrderMi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextOrderMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrderMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextOrderMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextOrderMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrderMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextOrderMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderMi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextOrderMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextOrderMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextOrderMi = nextOrderMiRepository.saveAndFlush(nextOrderMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextOrderMi using partial update
        NextOrderMi partialUpdatedNextOrderMi = new NextOrderMi();
        partialUpdatedNextOrderMi.setId(nextOrderMi.getId());

        partialUpdatedNextOrderMi.orderDate(UPDATED_ORDER_DATE);

        restNextOrderMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextOrderMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextOrderMi))
            )
            .andExpect(status().isOk());

        // Validate the NextOrderMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextOrderMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextOrderMi, nextOrderMi),
            getPersistedNextOrderMi(nextOrderMi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextOrderMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextOrderMi = nextOrderMiRepository.saveAndFlush(nextOrderMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextOrderMi using partial update
        NextOrderMi partialUpdatedNextOrderMi = new NextOrderMi();
        partialUpdatedNextOrderMi.setId(nextOrderMi.getId());

        partialUpdatedNextOrderMi.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);

        restNextOrderMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextOrderMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextOrderMi))
            )
            .andExpect(status().isOk());

        // Validate the NextOrderMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextOrderMiUpdatableFieldsEquals(partialUpdatedNextOrderMi, getPersistedNextOrderMi(partialUpdatedNextOrderMi));
    }

    @Test
    @Transactional
    void patchNonExistingNextOrderMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderMi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextOrderMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextOrderMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextOrderMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrderMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextOrderMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextOrderMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrderMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextOrderMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextOrderMi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextOrderMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextOrderMi() throws Exception {
        // Initialize the database
        insertedNextOrderMi = nextOrderMiRepository.saveAndFlush(nextOrderMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextOrderMi
        restNextOrderMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextOrderMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextOrderMiRepository.count();
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

    protected NextOrderMi getPersistedNextOrderMi(NextOrderMi nextOrderMi) {
        return nextOrderMiRepository.findById(nextOrderMi.getId()).orElseThrow();
    }

    protected void assertPersistedNextOrderMiToMatchAllProperties(NextOrderMi expectedNextOrderMi) {
        assertNextOrderMiAllPropertiesEquals(expectedNextOrderMi, getPersistedNextOrderMi(expectedNextOrderMi));
    }

    protected void assertPersistedNextOrderMiToMatchUpdatableProperties(NextOrderMi expectedNextOrderMi) {
        assertNextOrderMiAllUpdatablePropertiesEquals(expectedNextOrderMi, getPersistedNextOrderMi(expectedNextOrderMi));
    }
}
