package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.ShipmentMiMiAsserts.*;
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
import xyz.jhmapstruct.domain.ShipmentMiMi;
import xyz.jhmapstruct.repository.ShipmentMiMiRepository;
import xyz.jhmapstruct.service.dto.ShipmentMiMiDTO;
import xyz.jhmapstruct.service.mapper.ShipmentMiMiMapper;

/**
 * Integration tests for the {@link ShipmentMiMiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShipmentMiMiResourceIT {

    private static final String DEFAULT_TRACKING_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TRACKING_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_SHIPPED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SHIPPED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELIVERY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELIVERY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/shipment-mi-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShipmentMiMiRepository shipmentMiMiRepository;

    @Autowired
    private ShipmentMiMiMapper shipmentMiMiMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShipmentMiMiMockMvc;

    private ShipmentMiMi shipmentMiMi;

    private ShipmentMiMi insertedShipmentMiMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShipmentMiMi createEntity() {
        return new ShipmentMiMi()
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
    public static ShipmentMiMi createUpdatedEntity() {
        return new ShipmentMiMi()
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);
    }

    @BeforeEach
    public void initTest() {
        shipmentMiMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedShipmentMiMi != null) {
            shipmentMiMiRepository.delete(insertedShipmentMiMi);
            insertedShipmentMiMi = null;
        }
    }

    @Test
    @Transactional
    void createShipmentMiMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ShipmentMiMi
        ShipmentMiMiDTO shipmentMiMiDTO = shipmentMiMiMapper.toDto(shipmentMiMi);
        var returnedShipmentMiMiDTO = om.readValue(
            restShipmentMiMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentMiMiDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ShipmentMiMiDTO.class
        );

        // Validate the ShipmentMiMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedShipmentMiMi = shipmentMiMiMapper.toEntity(returnedShipmentMiMiDTO);
        assertShipmentMiMiUpdatableFieldsEquals(returnedShipmentMiMi, getPersistedShipmentMiMi(returnedShipmentMiMi));

        insertedShipmentMiMi = returnedShipmentMiMi;
    }

    @Test
    @Transactional
    void createShipmentMiMiWithExistingId() throws Exception {
        // Create the ShipmentMiMi with an existing ID
        shipmentMiMi.setId(1L);
        ShipmentMiMiDTO shipmentMiMiDTO = shipmentMiMiMapper.toDto(shipmentMiMi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShipmentMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentMiMiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ShipmentMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTrackingNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        shipmentMiMi.setTrackingNumber(null);

        // Create the ShipmentMiMi, which fails.
        ShipmentMiMiDTO shipmentMiMiDTO = shipmentMiMiMapper.toDto(shipmentMiMi);

        restShipmentMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentMiMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllShipmentMiMis() throws Exception {
        // Initialize the database
        insertedShipmentMiMi = shipmentMiMiRepository.saveAndFlush(shipmentMiMi);

        // Get all the shipmentMiMiList
        restShipmentMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentMiMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));
    }

    @Test
    @Transactional
    void getShipmentMiMi() throws Exception {
        // Initialize the database
        insertedShipmentMiMi = shipmentMiMiRepository.saveAndFlush(shipmentMiMi);

        // Get the shipmentMiMi
        restShipmentMiMiMockMvc
            .perform(get(ENTITY_API_URL_ID, shipmentMiMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shipmentMiMi.getId().intValue()))
            .andExpect(jsonPath("$.trackingNumber").value(DEFAULT_TRACKING_NUMBER))
            .andExpect(jsonPath("$.shippedDate").value(DEFAULT_SHIPPED_DATE.toString()))
            .andExpect(jsonPath("$.deliveryDate").value(DEFAULT_DELIVERY_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingShipmentMiMi() throws Exception {
        // Get the shipmentMiMi
        restShipmentMiMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShipmentMiMi() throws Exception {
        // Initialize the database
        insertedShipmentMiMi = shipmentMiMiRepository.saveAndFlush(shipmentMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentMiMi
        ShipmentMiMi updatedShipmentMiMi = shipmentMiMiRepository.findById(shipmentMiMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedShipmentMiMi are not directly saved in db
        em.detach(updatedShipmentMiMi);
        updatedShipmentMiMi.trackingNumber(UPDATED_TRACKING_NUMBER).shippedDate(UPDATED_SHIPPED_DATE).deliveryDate(UPDATED_DELIVERY_DATE);
        ShipmentMiMiDTO shipmentMiMiDTO = shipmentMiMiMapper.toDto(updatedShipmentMiMi);

        restShipmentMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shipmentMiMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentMiMiDTO))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedShipmentMiMiToMatchAllProperties(updatedShipmentMiMi);
    }

    @Test
    @Transactional
    void putNonExistingShipmentMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentMiMi.setId(longCount.incrementAndGet());

        // Create the ShipmentMiMi
        ShipmentMiMiDTO shipmentMiMiDTO = shipmentMiMiMapper.toDto(shipmentMiMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shipmentMiMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShipmentMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentMiMi.setId(longCount.incrementAndGet());

        // Create the ShipmentMiMi
        ShipmentMiMiDTO shipmentMiMiDTO = shipmentMiMiMapper.toDto(shipmentMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShipmentMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentMiMi.setId(longCount.incrementAndGet());

        // Create the ShipmentMiMi
        ShipmentMiMiDTO shipmentMiMiDTO = shipmentMiMiMapper.toDto(shipmentMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentMiMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentMiMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShipmentMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedShipmentMiMi = shipmentMiMiRepository.saveAndFlush(shipmentMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentMiMi using partial update
        ShipmentMiMi partialUpdatedShipmentMiMi = new ShipmentMiMi();
        partialUpdatedShipmentMiMi.setId(shipmentMiMi.getId());

        partialUpdatedShipmentMiMi.shippedDate(UPDATED_SHIPPED_DATE);

        restShipmentMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipmentMiMi))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipmentMiMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedShipmentMiMi, shipmentMiMi),
            getPersistedShipmentMiMi(shipmentMiMi)
        );
    }

    @Test
    @Transactional
    void fullUpdateShipmentMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedShipmentMiMi = shipmentMiMiRepository.saveAndFlush(shipmentMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentMiMi using partial update
        ShipmentMiMi partialUpdatedShipmentMiMi = new ShipmentMiMi();
        partialUpdatedShipmentMiMi.setId(shipmentMiMi.getId());

        partialUpdatedShipmentMiMi
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);

        restShipmentMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipmentMiMi))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipmentMiMiUpdatableFieldsEquals(partialUpdatedShipmentMiMi, getPersistedShipmentMiMi(partialUpdatedShipmentMiMi));
    }

    @Test
    @Transactional
    void patchNonExistingShipmentMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentMiMi.setId(longCount.incrementAndGet());

        // Create the ShipmentMiMi
        ShipmentMiMiDTO shipmentMiMiDTO = shipmentMiMiMapper.toDto(shipmentMiMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shipmentMiMiDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipmentMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShipmentMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentMiMi.setId(longCount.incrementAndGet());

        // Create the ShipmentMiMi
        ShipmentMiMiDTO shipmentMiMiDTO = shipmentMiMiMapper.toDto(shipmentMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipmentMiMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShipmentMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentMiMi.setId(longCount.incrementAndGet());

        // Create the ShipmentMiMi
        ShipmentMiMiDTO shipmentMiMiDTO = shipmentMiMiMapper.toDto(shipmentMiMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentMiMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(shipmentMiMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShipmentMiMi() throws Exception {
        // Initialize the database
        insertedShipmentMiMi = shipmentMiMiRepository.saveAndFlush(shipmentMiMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the shipmentMiMi
        restShipmentMiMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, shipmentMiMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return shipmentMiMiRepository.count();
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

    protected ShipmentMiMi getPersistedShipmentMiMi(ShipmentMiMi shipmentMiMi) {
        return shipmentMiMiRepository.findById(shipmentMiMi.getId()).orElseThrow();
    }

    protected void assertPersistedShipmentMiMiToMatchAllProperties(ShipmentMiMi expectedShipmentMiMi) {
        assertShipmentMiMiAllPropertiesEquals(expectedShipmentMiMi, getPersistedShipmentMiMi(expectedShipmentMiMi));
    }

    protected void assertPersistedShipmentMiMiToMatchUpdatableProperties(ShipmentMiMi expectedShipmentMiMi) {
        assertShipmentMiMiAllUpdatablePropertiesEquals(expectedShipmentMiMi, getPersistedShipmentMiMi(expectedShipmentMiMi));
    }
}
