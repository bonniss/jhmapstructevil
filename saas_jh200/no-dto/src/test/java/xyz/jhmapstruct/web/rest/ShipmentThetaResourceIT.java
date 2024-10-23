package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.ShipmentThetaAsserts.*;
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
import xyz.jhmapstruct.domain.ShipmentTheta;
import xyz.jhmapstruct.repository.ShipmentThetaRepository;

/**
 * Integration tests for the {@link ShipmentThetaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShipmentThetaResourceIT {

    private static final String DEFAULT_TRACKING_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TRACKING_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_SHIPPED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SHIPPED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELIVERY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELIVERY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/shipment-thetas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShipmentThetaRepository shipmentThetaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShipmentThetaMockMvc;

    private ShipmentTheta shipmentTheta;

    private ShipmentTheta insertedShipmentTheta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShipmentTheta createEntity() {
        return new ShipmentTheta()
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
    public static ShipmentTheta createUpdatedEntity() {
        return new ShipmentTheta()
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);
    }

    @BeforeEach
    public void initTest() {
        shipmentTheta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedShipmentTheta != null) {
            shipmentThetaRepository.delete(insertedShipmentTheta);
            insertedShipmentTheta = null;
        }
    }

    @Test
    @Transactional
    void createShipmentTheta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ShipmentTheta
        var returnedShipmentTheta = om.readValue(
            restShipmentThetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentTheta)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ShipmentTheta.class
        );

        // Validate the ShipmentTheta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertShipmentThetaUpdatableFieldsEquals(returnedShipmentTheta, getPersistedShipmentTheta(returnedShipmentTheta));

        insertedShipmentTheta = returnedShipmentTheta;
    }

    @Test
    @Transactional
    void createShipmentThetaWithExistingId() throws Exception {
        // Create the ShipmentTheta with an existing ID
        shipmentTheta.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShipmentThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentTheta)))
            .andExpect(status().isBadRequest());

        // Validate the ShipmentTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTrackingNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        shipmentTheta.setTrackingNumber(null);

        // Create the ShipmentTheta, which fails.

        restShipmentThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentTheta)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllShipmentThetas() throws Exception {
        // Initialize the database
        insertedShipmentTheta = shipmentThetaRepository.saveAndFlush(shipmentTheta);

        // Get all the shipmentThetaList
        restShipmentThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));
    }

    @Test
    @Transactional
    void getShipmentTheta() throws Exception {
        // Initialize the database
        insertedShipmentTheta = shipmentThetaRepository.saveAndFlush(shipmentTheta);

        // Get the shipmentTheta
        restShipmentThetaMockMvc
            .perform(get(ENTITY_API_URL_ID, shipmentTheta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shipmentTheta.getId().intValue()))
            .andExpect(jsonPath("$.trackingNumber").value(DEFAULT_TRACKING_NUMBER))
            .andExpect(jsonPath("$.shippedDate").value(DEFAULT_SHIPPED_DATE.toString()))
            .andExpect(jsonPath("$.deliveryDate").value(DEFAULT_DELIVERY_DATE.toString()));
    }

    @Test
    @Transactional
    void getShipmentThetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedShipmentTheta = shipmentThetaRepository.saveAndFlush(shipmentTheta);

        Long id = shipmentTheta.getId();

        defaultShipmentThetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultShipmentThetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultShipmentThetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllShipmentThetasByTrackingNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentTheta = shipmentThetaRepository.saveAndFlush(shipmentTheta);

        // Get all the shipmentThetaList where trackingNumber equals to
        defaultShipmentThetaFiltering(
            "trackingNumber.equals=" + DEFAULT_TRACKING_NUMBER,
            "trackingNumber.equals=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllShipmentThetasByTrackingNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentTheta = shipmentThetaRepository.saveAndFlush(shipmentTheta);

        // Get all the shipmentThetaList where trackingNumber in
        defaultShipmentThetaFiltering(
            "trackingNumber.in=" + DEFAULT_TRACKING_NUMBER + "," + UPDATED_TRACKING_NUMBER,
            "trackingNumber.in=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllShipmentThetasByTrackingNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentTheta = shipmentThetaRepository.saveAndFlush(shipmentTheta);

        // Get all the shipmentThetaList where trackingNumber is not null
        defaultShipmentThetaFiltering("trackingNumber.specified=true", "trackingNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentThetasByTrackingNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedShipmentTheta = shipmentThetaRepository.saveAndFlush(shipmentTheta);

        // Get all the shipmentThetaList where trackingNumber contains
        defaultShipmentThetaFiltering(
            "trackingNumber.contains=" + DEFAULT_TRACKING_NUMBER,
            "trackingNumber.contains=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllShipmentThetasByTrackingNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedShipmentTheta = shipmentThetaRepository.saveAndFlush(shipmentTheta);

        // Get all the shipmentThetaList where trackingNumber does not contain
        defaultShipmentThetaFiltering(
            "trackingNumber.doesNotContain=" + UPDATED_TRACKING_NUMBER,
            "trackingNumber.doesNotContain=" + DEFAULT_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllShipmentThetasByShippedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentTheta = shipmentThetaRepository.saveAndFlush(shipmentTheta);

        // Get all the shipmentThetaList where shippedDate equals to
        defaultShipmentThetaFiltering("shippedDate.equals=" + DEFAULT_SHIPPED_DATE, "shippedDate.equals=" + UPDATED_SHIPPED_DATE);
    }

    @Test
    @Transactional
    void getAllShipmentThetasByShippedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentTheta = shipmentThetaRepository.saveAndFlush(shipmentTheta);

        // Get all the shipmentThetaList where shippedDate in
        defaultShipmentThetaFiltering(
            "shippedDate.in=" + DEFAULT_SHIPPED_DATE + "," + UPDATED_SHIPPED_DATE,
            "shippedDate.in=" + UPDATED_SHIPPED_DATE
        );
    }

    @Test
    @Transactional
    void getAllShipmentThetasByShippedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentTheta = shipmentThetaRepository.saveAndFlush(shipmentTheta);

        // Get all the shipmentThetaList where shippedDate is not null
        defaultShipmentThetaFiltering("shippedDate.specified=true", "shippedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentThetasByDeliveryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentTheta = shipmentThetaRepository.saveAndFlush(shipmentTheta);

        // Get all the shipmentThetaList where deliveryDate equals to
        defaultShipmentThetaFiltering("deliveryDate.equals=" + DEFAULT_DELIVERY_DATE, "deliveryDate.equals=" + UPDATED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    void getAllShipmentThetasByDeliveryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentTheta = shipmentThetaRepository.saveAndFlush(shipmentTheta);

        // Get all the shipmentThetaList where deliveryDate in
        defaultShipmentThetaFiltering(
            "deliveryDate.in=" + DEFAULT_DELIVERY_DATE + "," + UPDATED_DELIVERY_DATE,
            "deliveryDate.in=" + UPDATED_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllShipmentThetasByDeliveryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentTheta = shipmentThetaRepository.saveAndFlush(shipmentTheta);

        // Get all the shipmentThetaList where deliveryDate is not null
        defaultShipmentThetaFiltering("deliveryDate.specified=true", "deliveryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentThetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            shipmentThetaRepository.saveAndFlush(shipmentTheta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        shipmentTheta.setTenant(tenant);
        shipmentThetaRepository.saveAndFlush(shipmentTheta);
        Long tenantId = tenant.getId();
        // Get all the shipmentThetaList where tenant equals to tenantId
        defaultShipmentThetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the shipmentThetaList where tenant equals to (tenantId + 1)
        defaultShipmentThetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultShipmentThetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultShipmentThetaShouldBeFound(shouldBeFound);
        defaultShipmentThetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShipmentThetaShouldBeFound(String filter) throws Exception {
        restShipmentThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));

        // Check, that the count call also returns 1
        restShipmentThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShipmentThetaShouldNotBeFound(String filter) throws Exception {
        restShipmentThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShipmentThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingShipmentTheta() throws Exception {
        // Get the shipmentTheta
        restShipmentThetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShipmentTheta() throws Exception {
        // Initialize the database
        insertedShipmentTheta = shipmentThetaRepository.saveAndFlush(shipmentTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentTheta
        ShipmentTheta updatedShipmentTheta = shipmentThetaRepository.findById(shipmentTheta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedShipmentTheta are not directly saved in db
        em.detach(updatedShipmentTheta);
        updatedShipmentTheta.trackingNumber(UPDATED_TRACKING_NUMBER).shippedDate(UPDATED_SHIPPED_DATE).deliveryDate(UPDATED_DELIVERY_DATE);

        restShipmentThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedShipmentTheta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedShipmentTheta))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedShipmentThetaToMatchAllProperties(updatedShipmentTheta);
    }

    @Test
    @Transactional
    void putNonExistingShipmentTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentTheta.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shipmentTheta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentTheta))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShipmentTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentTheta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentTheta))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShipmentTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentTheta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentThetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentTheta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShipmentThetaWithPatch() throws Exception {
        // Initialize the database
        insertedShipmentTheta = shipmentThetaRepository.saveAndFlush(shipmentTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentTheta using partial update
        ShipmentTheta partialUpdatedShipmentTheta = new ShipmentTheta();
        partialUpdatedShipmentTheta.setId(shipmentTheta.getId());

        partialUpdatedShipmentTheta.trackingNumber(UPDATED_TRACKING_NUMBER).shippedDate(UPDATED_SHIPPED_DATE);

        restShipmentThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipmentTheta))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipmentThetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedShipmentTheta, shipmentTheta),
            getPersistedShipmentTheta(shipmentTheta)
        );
    }

    @Test
    @Transactional
    void fullUpdateShipmentThetaWithPatch() throws Exception {
        // Initialize the database
        insertedShipmentTheta = shipmentThetaRepository.saveAndFlush(shipmentTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentTheta using partial update
        ShipmentTheta partialUpdatedShipmentTheta = new ShipmentTheta();
        partialUpdatedShipmentTheta.setId(shipmentTheta.getId());

        partialUpdatedShipmentTheta
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);

        restShipmentThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipmentTheta))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipmentThetaUpdatableFieldsEquals(partialUpdatedShipmentTheta, getPersistedShipmentTheta(partialUpdatedShipmentTheta));
    }

    @Test
    @Transactional
    void patchNonExistingShipmentTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentTheta.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shipmentTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipmentTheta))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShipmentTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentTheta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipmentTheta))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShipmentTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentTheta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentThetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(shipmentTheta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShipmentTheta() throws Exception {
        // Initialize the database
        insertedShipmentTheta = shipmentThetaRepository.saveAndFlush(shipmentTheta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the shipmentTheta
        restShipmentThetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, shipmentTheta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return shipmentThetaRepository.count();
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

    protected ShipmentTheta getPersistedShipmentTheta(ShipmentTheta shipmentTheta) {
        return shipmentThetaRepository.findById(shipmentTheta.getId()).orElseThrow();
    }

    protected void assertPersistedShipmentThetaToMatchAllProperties(ShipmentTheta expectedShipmentTheta) {
        assertShipmentThetaAllPropertiesEquals(expectedShipmentTheta, getPersistedShipmentTheta(expectedShipmentTheta));
    }

    protected void assertPersistedShipmentThetaToMatchUpdatableProperties(ShipmentTheta expectedShipmentTheta) {
        assertShipmentThetaAllUpdatablePropertiesEquals(expectedShipmentTheta, getPersistedShipmentTheta(expectedShipmentTheta));
    }
}
