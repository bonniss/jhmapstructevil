package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.ShipmentSigmaAsserts.*;
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
import xyz.jhmapstruct.domain.ShipmentSigma;
import xyz.jhmapstruct.repository.ShipmentSigmaRepository;

/**
 * Integration tests for the {@link ShipmentSigmaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShipmentSigmaResourceIT {

    private static final String DEFAULT_TRACKING_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TRACKING_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_SHIPPED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SHIPPED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELIVERY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELIVERY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/shipment-sigmas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShipmentSigmaRepository shipmentSigmaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShipmentSigmaMockMvc;

    private ShipmentSigma shipmentSigma;

    private ShipmentSigma insertedShipmentSigma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShipmentSigma createEntity() {
        return new ShipmentSigma()
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
    public static ShipmentSigma createUpdatedEntity() {
        return new ShipmentSigma()
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);
    }

    @BeforeEach
    public void initTest() {
        shipmentSigma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedShipmentSigma != null) {
            shipmentSigmaRepository.delete(insertedShipmentSigma);
            insertedShipmentSigma = null;
        }
    }

    @Test
    @Transactional
    void createShipmentSigma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ShipmentSigma
        var returnedShipmentSigma = om.readValue(
            restShipmentSigmaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentSigma)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ShipmentSigma.class
        );

        // Validate the ShipmentSigma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertShipmentSigmaUpdatableFieldsEquals(returnedShipmentSigma, getPersistedShipmentSigma(returnedShipmentSigma));

        insertedShipmentSigma = returnedShipmentSigma;
    }

    @Test
    @Transactional
    void createShipmentSigmaWithExistingId() throws Exception {
        // Create the ShipmentSigma with an existing ID
        shipmentSigma.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShipmentSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentSigma)))
            .andExpect(status().isBadRequest());

        // Validate the ShipmentSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTrackingNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        shipmentSigma.setTrackingNumber(null);

        // Create the ShipmentSigma, which fails.

        restShipmentSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentSigma)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllShipmentSigmas() throws Exception {
        // Initialize the database
        insertedShipmentSigma = shipmentSigmaRepository.saveAndFlush(shipmentSigma);

        // Get all the shipmentSigmaList
        restShipmentSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentSigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));
    }

    @Test
    @Transactional
    void getShipmentSigma() throws Exception {
        // Initialize the database
        insertedShipmentSigma = shipmentSigmaRepository.saveAndFlush(shipmentSigma);

        // Get the shipmentSigma
        restShipmentSigmaMockMvc
            .perform(get(ENTITY_API_URL_ID, shipmentSigma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shipmentSigma.getId().intValue()))
            .andExpect(jsonPath("$.trackingNumber").value(DEFAULT_TRACKING_NUMBER))
            .andExpect(jsonPath("$.shippedDate").value(DEFAULT_SHIPPED_DATE.toString()))
            .andExpect(jsonPath("$.deliveryDate").value(DEFAULT_DELIVERY_DATE.toString()));
    }

    @Test
    @Transactional
    void getShipmentSigmasByIdFiltering() throws Exception {
        // Initialize the database
        insertedShipmentSigma = shipmentSigmaRepository.saveAndFlush(shipmentSigma);

        Long id = shipmentSigma.getId();

        defaultShipmentSigmaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultShipmentSigmaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultShipmentSigmaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllShipmentSigmasByTrackingNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentSigma = shipmentSigmaRepository.saveAndFlush(shipmentSigma);

        // Get all the shipmentSigmaList where trackingNumber equals to
        defaultShipmentSigmaFiltering(
            "trackingNumber.equals=" + DEFAULT_TRACKING_NUMBER,
            "trackingNumber.equals=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllShipmentSigmasByTrackingNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentSigma = shipmentSigmaRepository.saveAndFlush(shipmentSigma);

        // Get all the shipmentSigmaList where trackingNumber in
        defaultShipmentSigmaFiltering(
            "trackingNumber.in=" + DEFAULT_TRACKING_NUMBER + "," + UPDATED_TRACKING_NUMBER,
            "trackingNumber.in=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllShipmentSigmasByTrackingNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentSigma = shipmentSigmaRepository.saveAndFlush(shipmentSigma);

        // Get all the shipmentSigmaList where trackingNumber is not null
        defaultShipmentSigmaFiltering("trackingNumber.specified=true", "trackingNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentSigmasByTrackingNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedShipmentSigma = shipmentSigmaRepository.saveAndFlush(shipmentSigma);

        // Get all the shipmentSigmaList where trackingNumber contains
        defaultShipmentSigmaFiltering(
            "trackingNumber.contains=" + DEFAULT_TRACKING_NUMBER,
            "trackingNumber.contains=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllShipmentSigmasByTrackingNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedShipmentSigma = shipmentSigmaRepository.saveAndFlush(shipmentSigma);

        // Get all the shipmentSigmaList where trackingNumber does not contain
        defaultShipmentSigmaFiltering(
            "trackingNumber.doesNotContain=" + UPDATED_TRACKING_NUMBER,
            "trackingNumber.doesNotContain=" + DEFAULT_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllShipmentSigmasByShippedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentSigma = shipmentSigmaRepository.saveAndFlush(shipmentSigma);

        // Get all the shipmentSigmaList where shippedDate equals to
        defaultShipmentSigmaFiltering("shippedDate.equals=" + DEFAULT_SHIPPED_DATE, "shippedDate.equals=" + UPDATED_SHIPPED_DATE);
    }

    @Test
    @Transactional
    void getAllShipmentSigmasByShippedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentSigma = shipmentSigmaRepository.saveAndFlush(shipmentSigma);

        // Get all the shipmentSigmaList where shippedDate in
        defaultShipmentSigmaFiltering(
            "shippedDate.in=" + DEFAULT_SHIPPED_DATE + "," + UPDATED_SHIPPED_DATE,
            "shippedDate.in=" + UPDATED_SHIPPED_DATE
        );
    }

    @Test
    @Transactional
    void getAllShipmentSigmasByShippedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentSigma = shipmentSigmaRepository.saveAndFlush(shipmentSigma);

        // Get all the shipmentSigmaList where shippedDate is not null
        defaultShipmentSigmaFiltering("shippedDate.specified=true", "shippedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentSigmasByDeliveryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentSigma = shipmentSigmaRepository.saveAndFlush(shipmentSigma);

        // Get all the shipmentSigmaList where deliveryDate equals to
        defaultShipmentSigmaFiltering("deliveryDate.equals=" + DEFAULT_DELIVERY_DATE, "deliveryDate.equals=" + UPDATED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    void getAllShipmentSigmasByDeliveryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentSigma = shipmentSigmaRepository.saveAndFlush(shipmentSigma);

        // Get all the shipmentSigmaList where deliveryDate in
        defaultShipmentSigmaFiltering(
            "deliveryDate.in=" + DEFAULT_DELIVERY_DATE + "," + UPDATED_DELIVERY_DATE,
            "deliveryDate.in=" + UPDATED_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllShipmentSigmasByDeliveryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentSigma = shipmentSigmaRepository.saveAndFlush(shipmentSigma);

        // Get all the shipmentSigmaList where deliveryDate is not null
        defaultShipmentSigmaFiltering("deliveryDate.specified=true", "deliveryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentSigmasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            shipmentSigmaRepository.saveAndFlush(shipmentSigma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        shipmentSigma.setTenant(tenant);
        shipmentSigmaRepository.saveAndFlush(shipmentSigma);
        Long tenantId = tenant.getId();
        // Get all the shipmentSigmaList where tenant equals to tenantId
        defaultShipmentSigmaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the shipmentSigmaList where tenant equals to (tenantId + 1)
        defaultShipmentSigmaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultShipmentSigmaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultShipmentSigmaShouldBeFound(shouldBeFound);
        defaultShipmentSigmaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShipmentSigmaShouldBeFound(String filter) throws Exception {
        restShipmentSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentSigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));

        // Check, that the count call also returns 1
        restShipmentSigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShipmentSigmaShouldNotBeFound(String filter) throws Exception {
        restShipmentSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShipmentSigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingShipmentSigma() throws Exception {
        // Get the shipmentSigma
        restShipmentSigmaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShipmentSigma() throws Exception {
        // Initialize the database
        insertedShipmentSigma = shipmentSigmaRepository.saveAndFlush(shipmentSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentSigma
        ShipmentSigma updatedShipmentSigma = shipmentSigmaRepository.findById(shipmentSigma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedShipmentSigma are not directly saved in db
        em.detach(updatedShipmentSigma);
        updatedShipmentSigma.trackingNumber(UPDATED_TRACKING_NUMBER).shippedDate(UPDATED_SHIPPED_DATE).deliveryDate(UPDATED_DELIVERY_DATE);

        restShipmentSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedShipmentSigma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedShipmentSigma))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedShipmentSigmaToMatchAllProperties(updatedShipmentSigma);
    }

    @Test
    @Transactional
    void putNonExistingShipmentSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentSigma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shipmentSigma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentSigma))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShipmentSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentSigma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentSigma))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShipmentSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentSigma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentSigmaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentSigma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShipmentSigmaWithPatch() throws Exception {
        // Initialize the database
        insertedShipmentSigma = shipmentSigmaRepository.saveAndFlush(shipmentSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentSigma using partial update
        ShipmentSigma partialUpdatedShipmentSigma = new ShipmentSigma();
        partialUpdatedShipmentSigma.setId(shipmentSigma.getId());

        partialUpdatedShipmentSigma.trackingNumber(UPDATED_TRACKING_NUMBER).shippedDate(UPDATED_SHIPPED_DATE);

        restShipmentSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipmentSigma))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentSigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipmentSigmaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedShipmentSigma, shipmentSigma),
            getPersistedShipmentSigma(shipmentSigma)
        );
    }

    @Test
    @Transactional
    void fullUpdateShipmentSigmaWithPatch() throws Exception {
        // Initialize the database
        insertedShipmentSigma = shipmentSigmaRepository.saveAndFlush(shipmentSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentSigma using partial update
        ShipmentSigma partialUpdatedShipmentSigma = new ShipmentSigma();
        partialUpdatedShipmentSigma.setId(shipmentSigma.getId());

        partialUpdatedShipmentSigma
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);

        restShipmentSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipmentSigma))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentSigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipmentSigmaUpdatableFieldsEquals(partialUpdatedShipmentSigma, getPersistedShipmentSigma(partialUpdatedShipmentSigma));
    }

    @Test
    @Transactional
    void patchNonExistingShipmentSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentSigma.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shipmentSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipmentSigma))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShipmentSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentSigma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipmentSigma))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShipmentSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentSigma.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentSigmaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(shipmentSigma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShipmentSigma() throws Exception {
        // Initialize the database
        insertedShipmentSigma = shipmentSigmaRepository.saveAndFlush(shipmentSigma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the shipmentSigma
        restShipmentSigmaMockMvc
            .perform(delete(ENTITY_API_URL_ID, shipmentSigma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return shipmentSigmaRepository.count();
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

    protected ShipmentSigma getPersistedShipmentSigma(ShipmentSigma shipmentSigma) {
        return shipmentSigmaRepository.findById(shipmentSigma.getId()).orElseThrow();
    }

    protected void assertPersistedShipmentSigmaToMatchAllProperties(ShipmentSigma expectedShipmentSigma) {
        assertShipmentSigmaAllPropertiesEquals(expectedShipmentSigma, getPersistedShipmentSigma(expectedShipmentSigma));
    }

    protected void assertPersistedShipmentSigmaToMatchUpdatableProperties(ShipmentSigma expectedShipmentSigma) {
        assertShipmentSigmaAllUpdatablePropertiesEquals(expectedShipmentSigma, getPersistedShipmentSigma(expectedShipmentSigma));
    }
}
