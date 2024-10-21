package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.OrderViViAsserts.*;
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
import xyz.jhmapstruct.domain.OrderViVi;
import xyz.jhmapstruct.domain.enumeration.OrderViViStatus;
import xyz.jhmapstruct.repository.OrderViViRepository;
import xyz.jhmapstruct.service.OrderViViService;
import xyz.jhmapstruct.service.dto.OrderViViDTO;
import xyz.jhmapstruct.service.mapper.OrderViViMapper;

/**
 * Integration tests for the {@link OrderViViResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OrderViViResourceIT {

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);

    private static final OrderViViStatus DEFAULT_STATUS = OrderViViStatus.PENDING;
    private static final OrderViViStatus UPDATED_STATUS = OrderViViStatus.SHIPPED;

    private static final String ENTITY_API_URL = "/api/order-vi-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrderViViRepository orderViViRepository;

    @Mock
    private OrderViViRepository orderViViRepositoryMock;

    @Autowired
    private OrderViViMapper orderViViMapper;

    @Mock
    private OrderViViService orderViViServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderViViMockMvc;

    private OrderViVi orderViVi;

    private OrderViVi insertedOrderViVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderViVi createEntity() {
        return new OrderViVi().orderDate(DEFAULT_ORDER_DATE).totalPrice(DEFAULT_TOTAL_PRICE).status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderViVi createUpdatedEntity() {
        return new OrderViVi().orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        orderViVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedOrderViVi != null) {
            orderViViRepository.delete(insertedOrderViVi);
            insertedOrderViVi = null;
        }
    }

    @Test
    @Transactional
    void createOrderViVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OrderViVi
        OrderViViDTO orderViViDTO = orderViViMapper.toDto(orderViVi);
        var returnedOrderViViDTO = om.readValue(
            restOrderViViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderViViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OrderViViDTO.class
        );

        // Validate the OrderViVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOrderViVi = orderViViMapper.toEntity(returnedOrderViViDTO);
        assertOrderViViUpdatableFieldsEquals(returnedOrderViVi, getPersistedOrderViVi(returnedOrderViVi));

        insertedOrderViVi = returnedOrderViVi;
    }

    @Test
    @Transactional
    void createOrderViViWithExistingId() throws Exception {
        // Create the OrderViVi with an existing ID
        orderViVi.setId(1L);
        OrderViViDTO orderViViDTO = orderViViMapper.toDto(orderViVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderViViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrderViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderViVi.setOrderDate(null);

        // Create the OrderViVi, which fails.
        OrderViViDTO orderViViDTO = orderViViMapper.toDto(orderViVi);

        restOrderViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderViViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderViVi.setTotalPrice(null);

        // Create the OrderViVi, which fails.
        OrderViViDTO orderViViDTO = orderViViMapper.toDto(orderViVi);

        restOrderViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderViViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrderViVis() throws Exception {
        // Initialize the database
        insertedOrderViVi = orderViViRepository.saveAndFlush(orderViVi);

        // Get all the orderViViList
        restOrderViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderViVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderViVisWithEagerRelationshipsIsEnabled() throws Exception {
        when(orderViViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderViViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(orderViViServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderViVisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(orderViViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderViViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(orderViViRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getOrderViVi() throws Exception {
        // Initialize the database
        insertedOrderViVi = orderViViRepository.saveAndFlush(orderViVi);

        // Get the orderViVi
        restOrderViViMockMvc
            .perform(get(ENTITY_API_URL_ID, orderViVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderViVi.getId().intValue()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingOrderViVi() throws Exception {
        // Get the orderViVi
        restOrderViViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderViVi() throws Exception {
        // Initialize the database
        insertedOrderViVi = orderViViRepository.saveAndFlush(orderViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderViVi
        OrderViVi updatedOrderViVi = orderViViRepository.findById(orderViVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrderViVi are not directly saved in db
        em.detach(updatedOrderViVi);
        updatedOrderViVi.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);
        OrderViViDTO orderViViDTO = orderViViMapper.toDto(updatedOrderViVi);

        restOrderViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderViViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderViViDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrderViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrderViViToMatchAllProperties(updatedOrderViVi);
    }

    @Test
    @Transactional
    void putNonExistingOrderViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderViVi.setId(longCount.incrementAndGet());

        // Create the OrderViVi
        OrderViViDTO orderViViDTO = orderViViMapper.toDto(orderViVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderViViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderViVi.setId(longCount.incrementAndGet());

        // Create the OrderViVi
        OrderViViDTO orderViViDTO = orderViViMapper.toDto(orderViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderViVi.setId(longCount.incrementAndGet());

        // Create the OrderViVi
        OrderViViDTO orderViViDTO = orderViViMapper.toDto(orderViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderViViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderViViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderViViWithPatch() throws Exception {
        // Initialize the database
        insertedOrderViVi = orderViViRepository.saveAndFlush(orderViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderViVi using partial update
        OrderViVi partialUpdatedOrderViVi = new OrderViVi();
        partialUpdatedOrderViVi.setId(orderViVi.getId());

        partialUpdatedOrderViVi.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);

        restOrderViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderViVi))
            )
            .andExpect(status().isOk());

        // Validate the OrderViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderViViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOrderViVi, orderViVi),
            getPersistedOrderViVi(orderViVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateOrderViViWithPatch() throws Exception {
        // Initialize the database
        insertedOrderViVi = orderViViRepository.saveAndFlush(orderViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderViVi using partial update
        OrderViVi partialUpdatedOrderViVi = new OrderViVi();
        partialUpdatedOrderViVi.setId(orderViVi.getId());

        partialUpdatedOrderViVi.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE).status(UPDATED_STATUS);

        restOrderViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderViVi))
            )
            .andExpect(status().isOk());

        // Validate the OrderViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderViViUpdatableFieldsEquals(partialUpdatedOrderViVi, getPersistedOrderViVi(partialUpdatedOrderViVi));
    }

    @Test
    @Transactional
    void patchNonExistingOrderViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderViVi.setId(longCount.incrementAndGet());

        // Create the OrderViVi
        OrderViViDTO orderViViDTO = orderViViMapper.toDto(orderViVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderViViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderViVi.setId(longCount.incrementAndGet());

        // Create the OrderViVi
        OrderViViDTO orderViViDTO = orderViViMapper.toDto(orderViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderViVi.setId(longCount.incrementAndGet());

        // Create the OrderViVi
        OrderViViDTO orderViViDTO = orderViViMapper.toDto(orderViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderViViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(orderViViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderViVi() throws Exception {
        // Initialize the database
        insertedOrderViVi = orderViViRepository.saveAndFlush(orderViVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the orderViVi
        restOrderViViMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderViVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return orderViViRepository.count();
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

    protected OrderViVi getPersistedOrderViVi(OrderViVi orderViVi) {
        return orderViViRepository.findById(orderViVi.getId()).orElseThrow();
    }

    protected void assertPersistedOrderViViToMatchAllProperties(OrderViVi expectedOrderViVi) {
        assertOrderViViAllPropertiesEquals(expectedOrderViVi, getPersistedOrderViVi(expectedOrderViVi));
    }

    protected void assertPersistedOrderViViToMatchUpdatableProperties(OrderViVi expectedOrderViVi) {
        assertOrderViViAllUpdatablePropertiesEquals(expectedOrderViVi, getPersistedOrderViVi(expectedOrderViVi));
    }
}
