package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.OrderMiAsserts.*;
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
import xyz.jhmapstruct.domain.OrderMi;
import xyz.jhmapstruct.domain.enumeration.OrderMiStatus;
import xyz.jhmapstruct.repository.OrderMiRepository;
import xyz.jhmapstruct.service.OrderMiService;

/**
 * Integration tests for the {@link OrderMiResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OrderMiResourceIT {

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);

    private static final OrderMiStatus DEFAULT_STATUS = OrderMiStatus.PENDING;
    private static final OrderMiStatus UPDATED_STATUS = OrderMiStatus.SHIPPED;

    private static final String ENTITY_API_URL = "/api/order-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrderMiRepository orderMiRepository;

    @Mock
    private OrderMiRepository orderMiRepositoryMock;

    @Mock
    private OrderMiService orderMiServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderMiMockMvc;

    private OrderMi orderMi;

    private OrderMi insertedOrderMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderMi createEntity() {
        return new OrderMi().orderDate(DEFAULT_ORDER_DATE).totalPrice(DEFAULT_TOTAL_PRICE).status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderMi createUpdatedEntity() {
        return new OrderMi().orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        orderMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedOrderMi != null) {
            orderMiRepository.delete(insertedOrderMi);
            insertedOrderMi = null;
        }
    }

    @Test
    @Transactional
    void createOrderMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OrderMi
        var returnedOrderMi = om.readValue(
            restOrderMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderMi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OrderMi.class
        );

        // Validate the OrderMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertOrderMiUpdatableFieldsEquals(returnedOrderMi, getPersistedOrderMi(returnedOrderMi));

        insertedOrderMi = returnedOrderMi;
    }

    @Test
    @Transactional
    void createOrderMiWithExistingId() throws Exception {
        // Create the OrderMi with an existing ID
        orderMi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderMi)))
            .andExpect(status().isBadRequest());

        // Validate the OrderMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderMi.setOrderDate(null);

        // Create the OrderMi, which fails.

        restOrderMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderMi.setTotalPrice(null);

        // Create the OrderMi, which fails.

        restOrderMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrderMis() throws Exception {
        // Initialize the database
        insertedOrderMi = orderMiRepository.saveAndFlush(orderMi);

        // Get all the orderMiList
        restOrderMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderMisWithEagerRelationshipsIsEnabled() throws Exception {
        when(orderMiServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderMiMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(orderMiServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderMisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(orderMiServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderMiMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(orderMiRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getOrderMi() throws Exception {
        // Initialize the database
        insertedOrderMi = orderMiRepository.saveAndFlush(orderMi);

        // Get the orderMi
        restOrderMiMockMvc
            .perform(get(ENTITY_API_URL_ID, orderMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderMi.getId().intValue()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingOrderMi() throws Exception {
        // Get the orderMi
        restOrderMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderMi() throws Exception {
        // Initialize the database
        insertedOrderMi = orderMiRepository.saveAndFlush(orderMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderMi
        OrderMi updatedOrderMi = orderMiRepository.findById(orderMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrderMi are not directly saved in db
        em.detach(updatedOrderMi);
        updatedOrderMi.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);

        restOrderMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrderMi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedOrderMi))
            )
            .andExpect(status().isOk());

        // Validate the OrderMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrderMiToMatchAllProperties(updatedOrderMi);
    }

    @Test
    @Transactional
    void putNonExistingOrderMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderMi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderMiMockMvc
            .perform(put(ENTITY_API_URL_ID, orderMi.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderMi)))
            .andExpect(status().isBadRequest());

        // Validate the OrderMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderMi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderMiWithPatch() throws Exception {
        // Initialize the database
        insertedOrderMi = orderMiRepository.saveAndFlush(orderMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderMi using partial update
        OrderMi partialUpdatedOrderMi = new OrderMi();
        partialUpdatedOrderMi.setId(orderMi.getId());

        partialUpdatedOrderMi.orderDate(UPDATED_ORDER_DATE);

        restOrderMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderMi))
            )
            .andExpect(status().isOk());

        // Validate the OrderMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderMiUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedOrderMi, orderMi), getPersistedOrderMi(orderMi));
    }

    @Test
    @Transactional
    void fullUpdateOrderMiWithPatch() throws Exception {
        // Initialize the database
        insertedOrderMi = orderMiRepository.saveAndFlush(orderMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderMi using partial update
        OrderMi partialUpdatedOrderMi = new OrderMi();
        partialUpdatedOrderMi.setId(orderMi.getId());

        partialUpdatedOrderMi.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);

        restOrderMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderMi))
            )
            .andExpect(status().isOk());

        // Validate the OrderMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderMiUpdatableFieldsEquals(partialUpdatedOrderMi, getPersistedOrderMi(partialUpdatedOrderMi));
    }

    @Test
    @Transactional
    void patchNonExistingOrderMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderMi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderMi.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(orderMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(orderMi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderMi() throws Exception {
        // Initialize the database
        insertedOrderMi = orderMiRepository.saveAndFlush(orderMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the orderMi
        restOrderMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return orderMiRepository.count();
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

    protected OrderMi getPersistedOrderMi(OrderMi orderMi) {
        return orderMiRepository.findById(orderMi.getId()).orElseThrow();
    }

    protected void assertPersistedOrderMiToMatchAllProperties(OrderMi expectedOrderMi) {
        assertOrderMiAllPropertiesEquals(expectedOrderMi, getPersistedOrderMi(expectedOrderMi));
    }

    protected void assertPersistedOrderMiToMatchUpdatableProperties(OrderMi expectedOrderMi) {
        assertOrderMiAllUpdatablePropertiesEquals(expectedOrderMi, getPersistedOrderMi(expectedOrderMi));
    }
}
