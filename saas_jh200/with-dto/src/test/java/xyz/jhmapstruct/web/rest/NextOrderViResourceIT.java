package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextOrderViAsserts.*;
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
import xyz.jhmapstruct.domain.NextCustomerVi;
import xyz.jhmapstruct.domain.NextOrderVi;
import xyz.jhmapstruct.domain.NextPaymentVi;
import xyz.jhmapstruct.domain.NextShipmentVi;
import xyz.jhmapstruct.domain.enumeration.OrderViStatus;
import xyz.jhmapstruct.repository.NextOrderViRepository;
import xyz.jhmapstruct.service.NextOrderViService;
import xyz.jhmapstruct.service.dto.NextOrderViDTO;
import xyz.jhmapstruct.service.mapper.NextOrderViMapper;

/**
 * Integration tests for the {@link NextOrderViResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextOrderViResourceIT {

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_PRICE = new BigDecimal(1 - 1);

    private static final OrderViStatus DEFAULT_STATUS = OrderViStatus.PENDING;
    private static final OrderViStatus UPDATED_STATUS = OrderViStatus.SHIPPED;

    private static final String ENTITY_API_URL = "/api/next-order-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextOrderViRepository nextOrderViRepository;

    @Mock
    private NextOrderViRepository nextOrderViRepositoryMock;

    @Autowired
    private NextOrderViMapper nextOrderViMapper;

    @Mock
    private NextOrderViService nextOrderViServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextOrderViMockMvc;

    private NextOrderVi nextOrderVi;

    private NextOrderVi insertedNextOrderVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextOrderVi createEntity() {
        return new NextOrderVi().orderDate(DEFAULT_ORDER_DATE).totalPrice(DEFAULT_TOTAL_PRICE).status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextOrderVi createUpdatedEntity() {
        return new NextOrderVi().orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        nextOrderVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextOrderVi != null) {
            nextOrderViRepository.delete(insertedNextOrderVi);
            insertedNextOrderVi = null;
        }
    }

    @Test
    @Transactional
    void createNextOrderVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextOrderVi
        NextOrderViDTO nextOrderViDTO = nextOrderViMapper.toDto(nextOrderVi);
        var returnedNextOrderViDTO = om.readValue(
            restNextOrderViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextOrderViDTO.class
        );

        // Validate the NextOrderVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextOrderVi = nextOrderViMapper.toEntity(returnedNextOrderViDTO);
        assertNextOrderViUpdatableFieldsEquals(returnedNextOrderVi, getPersistedNextOrderVi(returnedNextOrderVi));

        insertedNextOrderVi = returnedNextOrderVi;
    }

    @Test
    @Transactional
    void createNextOrderViWithExistingId() throws Exception {
        // Create the NextOrderVi with an existing ID
        nextOrderVi.setId(1L);
        NextOrderViDTO nextOrderViDTO = nextOrderViMapper.toDto(nextOrderVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextOrderViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextOrderVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextOrderVi.setOrderDate(null);

        // Create the NextOrderVi, which fails.
        NextOrderViDTO nextOrderViDTO = nextOrderViMapper.toDto(nextOrderVi);

        restNextOrderViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextOrderVi.setTotalPrice(null);

        // Create the NextOrderVi, which fails.
        NextOrderViDTO nextOrderViDTO = nextOrderViMapper.toDto(nextOrderVi);

        restNextOrderViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextOrderVis() throws Exception {
        // Initialize the database
        insertedNextOrderVi = nextOrderViRepository.saveAndFlush(nextOrderVi);

        // Get all the nextOrderViList
        restNextOrderViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextOrderVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextOrderVisWithEagerRelationshipsIsEnabled() throws Exception {
        when(nextOrderViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextOrderViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nextOrderViServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNextOrderVisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nextOrderViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNextOrderViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nextOrderViRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNextOrderVi() throws Exception {
        // Initialize the database
        insertedNextOrderVi = nextOrderViRepository.saveAndFlush(nextOrderVi);

        // Get the nextOrderVi
        restNextOrderViMockMvc
            .perform(get(ENTITY_API_URL_ID, nextOrderVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextOrderVi.getId().intValue()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNextOrderVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextOrderVi = nextOrderViRepository.saveAndFlush(nextOrderVi);

        Long id = nextOrderVi.getId();

        defaultNextOrderViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextOrderViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextOrderViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextOrderVisByOrderDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderVi = nextOrderViRepository.saveAndFlush(nextOrderVi);

        // Get all the nextOrderViList where orderDate equals to
        defaultNextOrderViFiltering("orderDate.equals=" + DEFAULT_ORDER_DATE, "orderDate.equals=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllNextOrderVisByOrderDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextOrderVi = nextOrderViRepository.saveAndFlush(nextOrderVi);

        // Get all the nextOrderViList where orderDate in
        defaultNextOrderViFiltering("orderDate.in=" + DEFAULT_ORDER_DATE + "," + UPDATED_ORDER_DATE, "orderDate.in=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllNextOrderVisByOrderDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextOrderVi = nextOrderViRepository.saveAndFlush(nextOrderVi);

        // Get all the nextOrderViList where orderDate is not null
        defaultNextOrderViFiltering("orderDate.specified=true", "orderDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextOrderVisByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderVi = nextOrderViRepository.saveAndFlush(nextOrderVi);

        // Get all the nextOrderViList where totalPrice equals to
        defaultNextOrderViFiltering("totalPrice.equals=" + DEFAULT_TOTAL_PRICE, "totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllNextOrderVisByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextOrderVi = nextOrderViRepository.saveAndFlush(nextOrderVi);

        // Get all the nextOrderViList where totalPrice in
        defaultNextOrderViFiltering(
            "totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE,
            "totalPrice.in=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllNextOrderVisByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextOrderVi = nextOrderViRepository.saveAndFlush(nextOrderVi);

        // Get all the nextOrderViList where totalPrice is not null
        defaultNextOrderViFiltering("totalPrice.specified=true", "totalPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllNextOrderVisByTotalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderVi = nextOrderViRepository.saveAndFlush(nextOrderVi);

        // Get all the nextOrderViList where totalPrice is greater than or equal to
        defaultNextOrderViFiltering(
            "totalPrice.greaterThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.greaterThanOrEqual=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllNextOrderVisByTotalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderVi = nextOrderViRepository.saveAndFlush(nextOrderVi);

        // Get all the nextOrderViList where totalPrice is less than or equal to
        defaultNextOrderViFiltering(
            "totalPrice.lessThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.lessThanOrEqual=" + SMALLER_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllNextOrderVisByTotalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNextOrderVi = nextOrderViRepository.saveAndFlush(nextOrderVi);

        // Get all the nextOrderViList where totalPrice is less than
        defaultNextOrderViFiltering("totalPrice.lessThan=" + UPDATED_TOTAL_PRICE, "totalPrice.lessThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllNextOrderVisByTotalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNextOrderVi = nextOrderViRepository.saveAndFlush(nextOrderVi);

        // Get all the nextOrderViList where totalPrice is greater than
        defaultNextOrderViFiltering("totalPrice.greaterThan=" + SMALLER_TOTAL_PRICE, "totalPrice.greaterThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllNextOrderVisByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextOrderVi = nextOrderViRepository.saveAndFlush(nextOrderVi);

        // Get all the nextOrderViList where status equals to
        defaultNextOrderViFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllNextOrderVisByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextOrderVi = nextOrderViRepository.saveAndFlush(nextOrderVi);

        // Get all the nextOrderViList where status in
        defaultNextOrderViFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllNextOrderVisByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextOrderVi = nextOrderViRepository.saveAndFlush(nextOrderVi);

        // Get all the nextOrderViList where status is not null
        defaultNextOrderViFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllNextOrderVisByPaymentIsEqualToSomething() throws Exception {
        NextPaymentVi payment;
        if (TestUtil.findAll(em, NextPaymentVi.class).isEmpty()) {
            nextOrderViRepository.saveAndFlush(nextOrderVi);
            payment = NextPaymentViResourceIT.createEntity();
        } else {
            payment = TestUtil.findAll(em, NextPaymentVi.class).get(0);
        }
        em.persist(payment);
        em.flush();
        nextOrderVi.setPayment(payment);
        nextOrderViRepository.saveAndFlush(nextOrderVi);
        Long paymentId = payment.getId();
        // Get all the nextOrderViList where payment equals to paymentId
        defaultNextOrderViShouldBeFound("paymentId.equals=" + paymentId);

        // Get all the nextOrderViList where payment equals to (paymentId + 1)
        defaultNextOrderViShouldNotBeFound("paymentId.equals=" + (paymentId + 1));
    }

    @Test
    @Transactional
    void getAllNextOrderVisByShipmentIsEqualToSomething() throws Exception {
        NextShipmentVi shipment;
        if (TestUtil.findAll(em, NextShipmentVi.class).isEmpty()) {
            nextOrderViRepository.saveAndFlush(nextOrderVi);
            shipment = NextShipmentViResourceIT.createEntity();
        } else {
            shipment = TestUtil.findAll(em, NextShipmentVi.class).get(0);
        }
        em.persist(shipment);
        em.flush();
        nextOrderVi.setShipment(shipment);
        nextOrderViRepository.saveAndFlush(nextOrderVi);
        Long shipmentId = shipment.getId();
        // Get all the nextOrderViList where shipment equals to shipmentId
        defaultNextOrderViShouldBeFound("shipmentId.equals=" + shipmentId);

        // Get all the nextOrderViList where shipment equals to (shipmentId + 1)
        defaultNextOrderViShouldNotBeFound("shipmentId.equals=" + (shipmentId + 1));
    }

    @Test
    @Transactional
    void getAllNextOrderVisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextOrderViRepository.saveAndFlush(nextOrderVi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextOrderVi.setTenant(tenant);
        nextOrderViRepository.saveAndFlush(nextOrderVi);
        Long tenantId = tenant.getId();
        // Get all the nextOrderViList where tenant equals to tenantId
        defaultNextOrderViShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextOrderViList where tenant equals to (tenantId + 1)
        defaultNextOrderViShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllNextOrderVisByCustomerIsEqualToSomething() throws Exception {
        NextCustomerVi customer;
        if (TestUtil.findAll(em, NextCustomerVi.class).isEmpty()) {
            nextOrderViRepository.saveAndFlush(nextOrderVi);
            customer = NextCustomerViResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, NextCustomerVi.class).get(0);
        }
        em.persist(customer);
        em.flush();
        nextOrderVi.setCustomer(customer);
        nextOrderViRepository.saveAndFlush(nextOrderVi);
        Long customerId = customer.getId();
        // Get all the nextOrderViList where customer equals to customerId
        defaultNextOrderViShouldBeFound("customerId.equals=" + customerId);

        // Get all the nextOrderViList where customer equals to (customerId + 1)
        defaultNextOrderViShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    private void defaultNextOrderViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextOrderViShouldBeFound(shouldBeFound);
        defaultNextOrderViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextOrderViShouldBeFound(String filter) throws Exception {
        restNextOrderViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextOrderVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restNextOrderViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextOrderViShouldNotBeFound(String filter) throws Exception {
        restNextOrderViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextOrderViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextOrderVi() throws Exception {
        // Get the nextOrderVi
        restNextOrderViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextOrderVi() throws Exception {
        // Initialize the database
        insertedNextOrderVi = nextOrderViRepository.saveAndFlush(nextOrderVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextOrderVi
        NextOrderVi updatedNextOrderVi = nextOrderViRepository.findById(nextOrderVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextOrderVi are not directly saved in db
        em.detach(updatedNextOrderVi);
        updatedNextOrderVi.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);
        NextOrderViDTO nextOrderViDTO = nextOrderViMapper.toDto(updatedNextOrderVi);

        restNextOrderViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextOrderViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextOrderViDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextOrderVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextOrderViToMatchAllProperties(updatedNextOrderVi);
    }

    @Test
    @Transactional
    void putNonExistingNextOrderVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderVi.setId(longCount.incrementAndGet());

        // Create the NextOrderVi
        NextOrderViDTO nextOrderViDTO = nextOrderViMapper.toDto(nextOrderVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextOrderViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextOrderViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextOrderViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrderVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextOrderVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderVi.setId(longCount.incrementAndGet());

        // Create the NextOrderVi
        NextOrderViDTO nextOrderViDTO = nextOrderViMapper.toDto(nextOrderVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextOrderViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrderVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextOrderVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderVi.setId(longCount.incrementAndGet());

        // Create the NextOrderVi
        NextOrderViDTO nextOrderViDTO = nextOrderViMapper.toDto(nextOrderVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextOrderViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextOrderVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextOrderViWithPatch() throws Exception {
        // Initialize the database
        insertedNextOrderVi = nextOrderViRepository.saveAndFlush(nextOrderVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextOrderVi using partial update
        NextOrderVi partialUpdatedNextOrderVi = new NextOrderVi();
        partialUpdatedNextOrderVi.setId(nextOrderVi.getId());

        partialUpdatedNextOrderVi.status(UPDATED_STATUS);

        restNextOrderViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextOrderVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextOrderVi))
            )
            .andExpect(status().isOk());

        // Validate the NextOrderVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextOrderViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextOrderVi, nextOrderVi),
            getPersistedNextOrderVi(nextOrderVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextOrderViWithPatch() throws Exception {
        // Initialize the database
        insertedNextOrderVi = nextOrderViRepository.saveAndFlush(nextOrderVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextOrderVi using partial update
        NextOrderVi partialUpdatedNextOrderVi = new NextOrderVi();
        partialUpdatedNextOrderVi.setId(nextOrderVi.getId());

        partialUpdatedNextOrderVi.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);

        restNextOrderViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextOrderVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextOrderVi))
            )
            .andExpect(status().isOk());

        // Validate the NextOrderVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextOrderViUpdatableFieldsEquals(partialUpdatedNextOrderVi, getPersistedNextOrderVi(partialUpdatedNextOrderVi));
    }

    @Test
    @Transactional
    void patchNonExistingNextOrderVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderVi.setId(longCount.incrementAndGet());

        // Create the NextOrderVi
        NextOrderViDTO nextOrderViDTO = nextOrderViMapper.toDto(nextOrderVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextOrderViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextOrderViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextOrderViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrderVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextOrderVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderVi.setId(longCount.incrementAndGet());

        // Create the NextOrderVi
        NextOrderViDTO nextOrderViDTO = nextOrderViMapper.toDto(nextOrderVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextOrderViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOrderVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextOrderVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextOrderVi.setId(longCount.incrementAndGet());

        // Create the NextOrderVi
        NextOrderViDTO nextOrderViDTO = nextOrderViMapper.toDto(nextOrderVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOrderViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextOrderViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextOrderVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextOrderVi() throws Exception {
        // Initialize the database
        insertedNextOrderVi = nextOrderViRepository.saveAndFlush(nextOrderVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextOrderVi
        restNextOrderViMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextOrderVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextOrderViRepository.count();
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

    protected NextOrderVi getPersistedNextOrderVi(NextOrderVi nextOrderVi) {
        return nextOrderViRepository.findById(nextOrderVi.getId()).orElseThrow();
    }

    protected void assertPersistedNextOrderViToMatchAllProperties(NextOrderVi expectedNextOrderVi) {
        assertNextOrderViAllPropertiesEquals(expectedNextOrderVi, getPersistedNextOrderVi(expectedNextOrderVi));
    }

    protected void assertPersistedNextOrderViToMatchUpdatableProperties(NextOrderVi expectedNextOrderVi) {
        assertNextOrderViAllUpdatablePropertiesEquals(expectedNextOrderVi, getPersistedNextOrderVi(expectedNextOrderVi));
    }
}
