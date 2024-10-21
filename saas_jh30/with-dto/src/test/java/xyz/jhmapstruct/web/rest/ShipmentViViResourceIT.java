package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.ShipmentViViAsserts.*;
import static xyz.jhmapstruct.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.IntegrationTest;
import xyz.jhmapstruct.domain.ShipmentViVi;
import xyz.jhmapstruct.repository.ShipmentViViRepository;
import xyz.jhmapstruct.service.dto.ShipmentViViDTO;
import xyz.jhmapstruct.service.mapper.ShipmentViViMapper;

/**
 * Integration tests for the {@link ShipmentViViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShipmentViViResourceIT {

    private static final String DEFAULT_TRACKING_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TRACKING_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_SHIPPED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SHIPPED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELIVERY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELIVERY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/shipment-vi-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShipmentViViRepository shipmentViViRepository;

    @Autowired
    private ShipmentViViMapper shipmentViViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShipmentViViMockMvc;

    private ShipmentViVi shipmentViVi;

    private ShipmentViVi insertedShipmentViVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShipmentViVi createEntity() {
        return new ShipmentViVi()
            .trackingNumber(DEFAULT_TRACKING_NUMBER)
            .shippedDate(DEFAULT_SHIPPED_DATE)
            .deliveryDate(DEFAULT_DELIVERY_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShipmentViVi createUpdatedEntity() {
        return new ShipmentViVi()
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);
    }

    @BeforeEach
    public void initTest() {
        shipmentViVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedShipmentViVi != null) {
            shipmentViViRepository.delete(insertedShipmentViVi);
            insertedShipmentViVi = null;
        }
    }

    @Test
    @Transactional
    void createShipmentViVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ShipmentViVi
        ShipmentViViDTO shipmentViViDTO = shipmentViViMapper.toDto(shipmentViVi);
        var returnedShipmentViViDTO = om.readValue(
            restShipmentViViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentViViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ShipmentViViDTO.class
        );

        // Validate the ShipmentViVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedShipmentViVi = shipmentViViMapper.toEntity(returnedShipmentViViDTO);
        assertShipmentViViUpdatableFieldsEquals(returnedShipmentViVi, getPersistedShipmentViVi(returnedShipmentViVi));

        insertedShipmentViVi = returnedShipmentViVi;
    }

    @Test
    @Transactional
    void createShipmentViViWithExistingId() throws Exception {
        // Create the ShipmentViVi with an existing ID
        shipmentViVi.setId(1L);
        ShipmentViViDTO shipmentViViDTO = shipmentViViMapper.toDto(shipmentViVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShipmentViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentViViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ShipmentViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTrackingNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        shipmentViVi.setTrackingNumber(null);

        // Create the ShipmentViVi, which fails.
        ShipmentViViDTO shipmentViViDTO = shipmentViViMapper.toDto(shipmentViVi);

        restShipmentViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentViViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllShipmentViVis() throws Exception {
        // Initialize the database
        insertedShipmentViVi = shipmentViViRepository.saveAndFlush(shipmentViVi);

        // Get all the shipmentViViList
        restShipmentViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentViVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));
    }

    @Test
    @Transactional
    void getShipmentViVi() throws Exception {
        // Initialize the database
        insertedShipmentViVi = shipmentViViRepository.saveAndFlush(shipmentViVi);

        // Get the shipmentViVi
        restShipmentViViMockMvc
            .perform(get(ENTITY_API_URL_ID, shipmentViVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shipmentViVi.getId().intValue()))
            .andExpect(jsonPath("$.trackingNumber").value(DEFAULT_TRACKING_NUMBER))
            .andExpect(jsonPath("$.shippedDate").value(DEFAULT_SHIPPED_DATE.toString()))
            .andExpect(jsonPath("$.deliveryDate").value(DEFAULT_DELIVERY_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingShipmentViVi() throws Exception {
        // Get the shipmentViVi
        restShipmentViViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShipmentViVi() throws Exception {
        // Initialize the database
        insertedShipmentViVi = shipmentViViRepository.saveAndFlush(shipmentViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentViVi
        ShipmentViVi updatedShipmentViVi = shipmentViViRepository.findById(shipmentViVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedShipmentViVi are not directly saved in db
        em.detach(updatedShipmentViVi);
        updatedShipmentViVi.trackingNumber(UPDATED_TRACKING_NUMBER).shippedDate(UPDATED_SHIPPED_DATE).deliveryDate(UPDATED_DELIVERY_DATE);
        ShipmentViViDTO shipmentViViDTO = shipmentViViMapper.toDto(updatedShipmentViVi);

        restShipmentViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shipmentViViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentViViDTO))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedShipmentViViToMatchAllProperties(updatedShipmentViVi);
    }

    @Test
    @Transactional
    void putNonExistingShipmentViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentViVi.setId(longCount.incrementAndGet());

        // Create the ShipmentViVi
        ShipmentViViDTO shipmentViViDTO = shipmentViViMapper.toDto(shipmentViVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shipmentViViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShipmentViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentViVi.setId(longCount.incrementAndGet());

        // Create the ShipmentViVi
        ShipmentViViDTO shipmentViViDTO = shipmentViViMapper.toDto(shipmentViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShipmentViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentViVi.setId(longCount.incrementAndGet());

        // Create the ShipmentViVi
        ShipmentViViDTO shipmentViViDTO = shipmentViViMapper.toDto(shipmentViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentViViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentViViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShipmentViViWithPatch() throws Exception {
        // Initialize the database
        insertedShipmentViVi = shipmentViViRepository.saveAndFlush(shipmentViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentViVi using partial update
        ShipmentViVi partialUpdatedShipmentViVi = new ShipmentViVi();
        partialUpdatedShipmentViVi.setId(shipmentViVi.getId());

        partialUpdatedShipmentViVi
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);

        restShipmentViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipmentViVi))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipmentViViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedShipmentViVi, shipmentViVi),
            getPersistedShipmentViVi(shipmentViVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateShipmentViViWithPatch() throws Exception {
        // Initialize the database
        insertedShipmentViVi = shipmentViViRepository.saveAndFlush(shipmentViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentViVi using partial update
        ShipmentViVi partialUpdatedShipmentViVi = new ShipmentViVi();
        partialUpdatedShipmentViVi.setId(shipmentViVi.getId());

        partialUpdatedShipmentViVi
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);

        restShipmentViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipmentViVi))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipmentViViUpdatableFieldsEquals(partialUpdatedShipmentViVi, getPersistedShipmentViVi(partialUpdatedShipmentViVi));
    }

    @Test
    @Transactional
    void patchNonExistingShipmentViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentViVi.setId(longCount.incrementAndGet());

        // Create the ShipmentViVi
        ShipmentViViDTO shipmentViViDTO = shipmentViViMapper.toDto(shipmentViVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shipmentViViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipmentViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShipmentViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentViVi.setId(longCount.incrementAndGet());

        // Create the ShipmentViVi
        ShipmentViViDTO shipmentViViDTO = shipmentViViMapper.toDto(shipmentViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipmentViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShipmentViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentViVi.setId(longCount.incrementAndGet());

        // Create the ShipmentViVi
        ShipmentViViDTO shipmentViViDTO = shipmentViViMapper.toDto(shipmentViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentViViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(shipmentViViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShipmentViVi() throws Exception {
        // Initialize the database
        insertedShipmentViVi = shipmentViViRepository.saveAndFlush(shipmentViVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the shipmentViVi
        restShipmentViViMockMvc
            .perform(delete(ENTITY_API_URL_ID, shipmentViVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return shipmentViViRepository.count();
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

    protected ShipmentViVi getPersistedShipmentViVi(ShipmentViVi shipmentViVi) {
        return shipmentViViRepository.findById(shipmentViVi.getId()).orElseThrow();
    }

    protected void assertPersistedShipmentViViToMatchAllProperties(ShipmentViVi expectedShipmentViVi) {
        assertShipmentViViAllPropertiesEquals(expectedShipmentViVi, getPersistedShipmentViVi(expectedShipmentViVi));
    }

    protected void assertPersistedShipmentViViToMatchUpdatableProperties(ShipmentViVi expectedShipmentViVi) {
        assertShipmentViViAllUpdatablePropertiesEquals(expectedShipmentViVi, getPersistedShipmentViVi(expectedShipmentViVi));
    }
}
