package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.OrderViAsserts.*;
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
import xyz.jhmapstruct.domain.OrderVi;
import xyz.jhmapstruct.domain.enumeration.OrderViStatus;
import xyz.jhmapstruct.repository.OrderViRepository;
import xyz.jhmapstruct.service.OrderViService;

/**
 * Integration tests for the {@link OrderViResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OrderViResourceIT {

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);

    private static final OrderViStatus DEFAULT_STATUS = OrderViStatus.PENDING;
    private static final OrderViStatus UPDATED_STATUS = OrderViStatus.SHIPPED;

    private static final String ENTITY_API_URL = "/api/order-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrderViRepository orderViRepository;

    @Mock
    private OrderViRepository orderViRepositoryMock;

    @Mock
    private OrderViService orderViServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderViMockMvc;

    private OrderVi orderVi;

    private OrderVi insertedOrderVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderVi createEntity() {
        return new OrderVi().orderDate(DEFAULT_ORDER_DATE).totalPrice(DEFAULT_TOTAL_PRICE).status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderVi createUpdatedEntity() {
        return new OrderVi().orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        orderVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedOrderVi != null) {
            orderViRepository.delete(insertedOrderVi);
            insertedOrderVi = null;
        }
    }

    @Test
    @Transactional
    void createOrderVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OrderVi
        var returnedOrderVi = om.readValue(
            restOrderViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OrderVi.class
        );

        // Validate the OrderVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertOrderViUpdatableFieldsEquals(returnedOrderVi, getPersistedOrderVi(returnedOrderVi));

        insertedOrderVi = returnedOrderVi;
    }

    @Test
    @Transactional
    void createOrderViWithExistingId() throws Exception {
        // Create the OrderVi with an existing ID
        orderVi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderVi)))
            .andExpect(status().isBadRequest());

        // Validate the OrderVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderVi.setOrderDate(null);

        // Create the OrderVi, which fails.

        restOrderViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderVi.setTotalPrice(null);

        // Create the OrderVi, which fails.

        restOrderViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrderVis() throws Exception {
        // Initialize the database
        insertedOrderVi = orderViRepository.saveAndFlush(orderVi);

        // Get all the orderViList
        restOrderViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderVisWithEagerRelationshipsIsEnabled() throws Exception {
        when(orderViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(orderViServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderVisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(orderViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(orderViRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getOrderVi() throws Exception {
        // Initialize the database
        insertedOrderVi = orderViRepository.saveAndFlush(orderVi);

        // Get the orderVi
        restOrderViMockMvc
            .perform(get(ENTITY_API_URL_ID, orderVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderVi.getId().intValue()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingOrderVi() throws Exception {
        // Get the orderVi
        restOrderViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderVi() throws Exception {
        // Initialize the database
        insertedOrderVi = orderViRepository.saveAndFlush(orderVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderVi
        OrderVi updatedOrderVi = orderViRepository.findById(orderVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrderVi are not directly saved in db
        em.detach(updatedOrderVi);
        updatedOrderVi.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);

        restOrderViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrderVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedOrderVi))
            )
            .andExpect(status().isOk());

        // Validate the OrderVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrderViToMatchAllProperties(updatedOrderVi);
    }

    @Test
    @Transactional
    void putNonExistingOrderVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderViMockMvc
            .perform(put(ENTITY_API_URL_ID, orderVi.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderVi)))
            .andExpect(status().isBadRequest());

        // Validate the OrderVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderViWithPatch() throws Exception {
        // Initialize the database
        insertedOrderVi = orderViRepository.saveAndFlush(orderVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderVi using partial update
        OrderVi partialUpdatedOrderVi = new OrderVi();
        partialUpdatedOrderVi.setId(orderVi.getId());

        partialUpdatedOrderVi.totalPrice(UPDATED_TOTAL_PRICE);

        restOrderViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderVi))
            )
            .andExpect(status().isOk());

        // Validate the OrderVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderViUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedOrderVi, orderVi), getPersistedOrderVi(orderVi));
    }

    @Test
    @Transactional
    void fullUpdateOrderViWithPatch() throws Exception {
        // Initialize the database
        insertedOrderVi = orderViRepository.saveAndFlush(orderVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderVi using partial update
        OrderVi partialUpdatedOrderVi = new OrderVi();
        partialUpdatedOrderVi.setId(orderVi.getId());

        partialUpdatedOrderVi.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);

        restOrderViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderVi))
            )
            .andExpect(status().isOk());

        // Validate the OrderVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderViUpdatableFieldsEquals(partialUpdatedOrderVi, getPersistedOrderVi(partialUpdatedOrderVi));
    }

    @Test
    @Transactional
    void patchNonExistingOrderVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderVi.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(orderVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(orderVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderVi() throws Exception {
        // Initialize the database
        insertedOrderVi = orderViRepository.saveAndFlush(orderVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the orderVi
        restOrderViMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return orderViRepository.count();
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

    protected OrderVi getPersistedOrderVi(OrderVi orderVi) {
        return orderViRepository.findById(orderVi.getId()).orElseThrow();
    }

    protected void assertPersistedOrderViToMatchAllProperties(OrderVi expectedOrderVi) {
        assertOrderViAllPropertiesEquals(expectedOrderVi, getPersistedOrderVi(expectedOrderVi));
    }

    protected void assertPersistedOrderViToMatchUpdatableProperties(OrderVi expectedOrderVi) {
        assertOrderViAllUpdatablePropertiesEquals(expectedOrderVi, getPersistedOrderVi(expectedOrderVi));
    }
}
