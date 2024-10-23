package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.ShipmentGammaAsserts.*;
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
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.ShipmentGamma;
import xyz.jhmapstruct.repository.ShipmentGammaRepository;
import xyz.jhmapstruct.service.dto.ShipmentGammaDTO;
import xyz.jhmapstruct.service.mapper.ShipmentGammaMapper;

/**
 * Integration tests for the {@link ShipmentGammaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShipmentGammaResourceIT {

    private static final String DEFAULT_TRACKING_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TRACKING_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_SHIPPED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SHIPPED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELIVERY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELIVERY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/shipment-gammas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShipmentGammaRepository shipmentGammaRepository;

    @Autowired
    private ShipmentGammaMapper shipmentGammaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShipmentGammaMockMvc;

    private ShipmentGamma shipmentGamma;

    private ShipmentGamma insertedShipmentGamma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShipmentGamma createEntity() {
        return new ShipmentGamma()
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
    public static ShipmentGamma createUpdatedEntity() {
        return new ShipmentGamma()
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);
    }

    @BeforeEach
    public void initTest() {
        shipmentGamma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedShipmentGamma != null) {
            shipmentGammaRepository.delete(insertedShipmentGamma);
            insertedShipmentGamma = null;
        }
    }

    @Test
    @Transactional
    void createShipmentGamma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ShipmentGamma
        ShipmentGammaDTO shipmentGammaDTO = shipmentGammaMapper.toDto(shipmentGamma);
        var returnedShipmentGammaDTO = om.readValue(
            restShipmentGammaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentGammaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ShipmentGammaDTO.class
        );

        // Validate the ShipmentGamma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedShipmentGamma = shipmentGammaMapper.toEntity(returnedShipmentGammaDTO);
        assertShipmentGammaUpdatableFieldsEquals(returnedShipmentGamma, getPersistedShipmentGamma(returnedShipmentGamma));

        insertedShipmentGamma = returnedShipmentGamma;
    }

    @Test
    @Transactional
    void createShipmentGammaWithExistingId() throws Exception {
        // Create the ShipmentGamma with an existing ID
        shipmentGamma.setId(1L);
        ShipmentGammaDTO shipmentGammaDTO = shipmentGammaMapper.toDto(shipmentGamma);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShipmentGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentGammaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ShipmentGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTrackingNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        shipmentGamma.setTrackingNumber(null);

        // Create the ShipmentGamma, which fails.
        ShipmentGammaDTO shipmentGammaDTO = shipmentGammaMapper.toDto(shipmentGamma);

        restShipmentGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentGammaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllShipmentGammas() throws Exception {
        // Initialize the database
        insertedShipmentGamma = shipmentGammaRepository.saveAndFlush(shipmentGamma);

        // Get all the shipmentGammaList
        restShipmentGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));
    }

    @Test
    @Transactional
    void getShipmentGamma() throws Exception {
        // Initialize the database
        insertedShipmentGamma = shipmentGammaRepository.saveAndFlush(shipmentGamma);

        // Get the shipmentGamma
        restShipmentGammaMockMvc
            .perform(get(ENTITY_API_URL_ID, shipmentGamma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shipmentGamma.getId().intValue()))
            .andExpect(jsonPath("$.trackingNumber").value(DEFAULT_TRACKING_NUMBER))
            .andExpect(jsonPath("$.shippedDate").value(DEFAULT_SHIPPED_DATE.toString()))
            .andExpect(jsonPath("$.deliveryDate").value(DEFAULT_DELIVERY_DATE.toString()));
    }

    @Test
    @Transactional
    void getShipmentGammasByIdFiltering() throws Exception {
        // Initialize the database
        insertedShipmentGamma = shipmentGammaRepository.saveAndFlush(shipmentGamma);

        Long id = shipmentGamma.getId();

        defaultShipmentGammaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultShipmentGammaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultShipmentGammaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllShipmentGammasByTrackingNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentGamma = shipmentGammaRepository.saveAndFlush(shipmentGamma);

        // Get all the shipmentGammaList where trackingNumber equals to
        defaultShipmentGammaFiltering(
            "trackingNumber.equals=" + DEFAULT_TRACKING_NUMBER,
            "trackingNumber.equals=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllShipmentGammasByTrackingNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentGamma = shipmentGammaRepository.saveAndFlush(shipmentGamma);

        // Get all the shipmentGammaList where trackingNumber in
        defaultShipmentGammaFiltering(
            "trackingNumber.in=" + DEFAULT_TRACKING_NUMBER + "," + UPDATED_TRACKING_NUMBER,
            "trackingNumber.in=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllShipmentGammasByTrackingNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentGamma = shipmentGammaRepository.saveAndFlush(shipmentGamma);

        // Get all the shipmentGammaList where trackingNumber is not null
        defaultShipmentGammaFiltering("trackingNumber.specified=true", "trackingNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentGammasByTrackingNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedShipmentGamma = shipmentGammaRepository.saveAndFlush(shipmentGamma);

        // Get all the shipmentGammaList where trackingNumber contains
        defaultShipmentGammaFiltering(
            "trackingNumber.contains=" + DEFAULT_TRACKING_NUMBER,
            "trackingNumber.contains=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllShipmentGammasByTrackingNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedShipmentGamma = shipmentGammaRepository.saveAndFlush(shipmentGamma);

        // Get all the shipmentGammaList where trackingNumber does not contain
        defaultShipmentGammaFiltering(
            "trackingNumber.doesNotContain=" + UPDATED_TRACKING_NUMBER,
            "trackingNumber.doesNotContain=" + DEFAULT_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllShipmentGammasByShippedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentGamma = shipmentGammaRepository.saveAndFlush(shipmentGamma);

        // Get all the shipmentGammaList where shippedDate equals to
        defaultShipmentGammaFiltering("shippedDate.equals=" + DEFAULT_SHIPPED_DATE, "shippedDate.equals=" + UPDATED_SHIPPED_DATE);
    }

    @Test
    @Transactional
    void getAllShipmentGammasByShippedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentGamma = shipmentGammaRepository.saveAndFlush(shipmentGamma);

        // Get all the shipmentGammaList where shippedDate in
        defaultShipmentGammaFiltering(
            "shippedDate.in=" + DEFAULT_SHIPPED_DATE + "," + UPDATED_SHIPPED_DATE,
            "shippedDate.in=" + UPDATED_SHIPPED_DATE
        );
    }

    @Test
    @Transactional
    void getAllShipmentGammasByShippedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentGamma = shipmentGammaRepository.saveAndFlush(shipmentGamma);

        // Get all the shipmentGammaList where shippedDate is not null
        defaultShipmentGammaFiltering("shippedDate.specified=true", "shippedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentGammasByDeliveryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentGamma = shipmentGammaRepository.saveAndFlush(shipmentGamma);

        // Get all the shipmentGammaList where deliveryDate equals to
        defaultShipmentGammaFiltering("deliveryDate.equals=" + DEFAULT_DELIVERY_DATE, "deliveryDate.equals=" + UPDATED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    void getAllShipmentGammasByDeliveryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentGamma = shipmentGammaRepository.saveAndFlush(shipmentGamma);

        // Get all the shipmentGammaList where deliveryDate in
        defaultShipmentGammaFiltering(
            "deliveryDate.in=" + DEFAULT_DELIVERY_DATE + "," + UPDATED_DELIVERY_DATE,
            "deliveryDate.in=" + UPDATED_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllShipmentGammasByDeliveryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentGamma = shipmentGammaRepository.saveAndFlush(shipmentGamma);

        // Get all the shipmentGammaList where deliveryDate is not null
        defaultShipmentGammaFiltering("deliveryDate.specified=true", "deliveryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentGammasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            shipmentGammaRepository.saveAndFlush(shipmentGamma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        shipmentGamma.setTenant(tenant);
        shipmentGammaRepository.saveAndFlush(shipmentGamma);
        Long tenantId = tenant.getId();
        // Get all the shipmentGammaList where tenant equals to tenantId
        defaultShipmentGammaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the shipmentGammaList where tenant equals to (tenantId + 1)
        defaultShipmentGammaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultShipmentGammaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultShipmentGammaShouldBeFound(shouldBeFound);
        defaultShipmentGammaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShipmentGammaShouldBeFound(String filter) throws Exception {
        restShipmentGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));

        // Check, that the count call also returns 1
        restShipmentGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShipmentGammaShouldNotBeFound(String filter) throws Exception {
        restShipmentGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShipmentGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingShipmentGamma() throws Exception {
        // Get the shipmentGamma
        restShipmentGammaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShipmentGamma() throws Exception {
        // Initialize the database
        insertedShipmentGamma = shipmentGammaRepository.saveAndFlush(shipmentGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentGamma
        ShipmentGamma updatedShipmentGamma = shipmentGammaRepository.findById(shipmentGamma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedShipmentGamma are not directly saved in db
        em.detach(updatedShipmentGamma);
        updatedShipmentGamma.trackingNumber(UPDATED_TRACKING_NUMBER).shippedDate(UPDATED_SHIPPED_DATE).deliveryDate(UPDATED_DELIVERY_DATE);
        ShipmentGammaDTO shipmentGammaDTO = shipmentGammaMapper.toDto(updatedShipmentGamma);

        restShipmentGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shipmentGammaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentGammaDTO))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedShipmentGammaToMatchAllProperties(updatedShipmentGamma);
    }

    @Test
    @Transactional
    void putNonExistingShipmentGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentGamma.setId(longCount.incrementAndGet());

        // Create the ShipmentGamma
        ShipmentGammaDTO shipmentGammaDTO = shipmentGammaMapper.toDto(shipmentGamma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shipmentGammaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShipmentGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentGamma.setId(longCount.incrementAndGet());

        // Create the ShipmentGamma
        ShipmentGammaDTO shipmentGammaDTO = shipmentGammaMapper.toDto(shipmentGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShipmentGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentGamma.setId(longCount.incrementAndGet());

        // Create the ShipmentGamma
        ShipmentGammaDTO shipmentGammaDTO = shipmentGammaMapper.toDto(shipmentGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentGammaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentGammaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShipmentGammaWithPatch() throws Exception {
        // Initialize the database
        insertedShipmentGamma = shipmentGammaRepository.saveAndFlush(shipmentGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentGamma using partial update
        ShipmentGamma partialUpdatedShipmentGamma = new ShipmentGamma();
        partialUpdatedShipmentGamma.setId(shipmentGamma.getId());

        restShipmentGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipmentGamma))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipmentGammaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedShipmentGamma, shipmentGamma),
            getPersistedShipmentGamma(shipmentGamma)
        );
    }

    @Test
    @Transactional
    void fullUpdateShipmentGammaWithPatch() throws Exception {
        // Initialize the database
        insertedShipmentGamma = shipmentGammaRepository.saveAndFlush(shipmentGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentGamma using partial update
        ShipmentGamma partialUpdatedShipmentGamma = new ShipmentGamma();
        partialUpdatedShipmentGamma.setId(shipmentGamma.getId());

        partialUpdatedShipmentGamma
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);

        restShipmentGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipmentGamma))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipmentGammaUpdatableFieldsEquals(partialUpdatedShipmentGamma, getPersistedShipmentGamma(partialUpdatedShipmentGamma));
    }

    @Test
    @Transactional
    void patchNonExistingShipmentGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentGamma.setId(longCount.incrementAndGet());

        // Create the ShipmentGamma
        ShipmentGammaDTO shipmentGammaDTO = shipmentGammaMapper.toDto(shipmentGamma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shipmentGammaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipmentGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShipmentGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentGamma.setId(longCount.incrementAndGet());

        // Create the ShipmentGamma
        ShipmentGammaDTO shipmentGammaDTO = shipmentGammaMapper.toDto(shipmentGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipmentGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShipmentGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentGamma.setId(longCount.incrementAndGet());

        // Create the ShipmentGamma
        ShipmentGammaDTO shipmentGammaDTO = shipmentGammaMapper.toDto(shipmentGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentGammaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(shipmentGammaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShipmentGamma() throws Exception {
        // Initialize the database
        insertedShipmentGamma = shipmentGammaRepository.saveAndFlush(shipmentGamma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the shipmentGamma
        restShipmentGammaMockMvc
            .perform(delete(ENTITY_API_URL_ID, shipmentGamma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return shipmentGammaRepository.count();
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

    protected ShipmentGamma getPersistedShipmentGamma(ShipmentGamma shipmentGamma) {
        return shipmentGammaRepository.findById(shipmentGamma.getId()).orElseThrow();
    }

    protected void assertPersistedShipmentGammaToMatchAllProperties(ShipmentGamma expectedShipmentGamma) {
        assertShipmentGammaAllPropertiesEquals(expectedShipmentGamma, getPersistedShipmentGamma(expectedShipmentGamma));
    }

    protected void assertPersistedShipmentGammaToMatchUpdatableProperties(ShipmentGamma expectedShipmentGamma) {
        assertShipmentGammaAllUpdatablePropertiesEquals(expectedShipmentGamma, getPersistedShipmentGamma(expectedShipmentGamma));
    }
}
