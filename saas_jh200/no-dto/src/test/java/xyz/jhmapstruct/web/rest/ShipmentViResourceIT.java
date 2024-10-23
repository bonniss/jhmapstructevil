package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.ShipmentViAsserts.*;
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
import xyz.jhmapstruct.domain.ShipmentVi;
import xyz.jhmapstruct.repository.ShipmentViRepository;

/**
 * Integration tests for the {@link ShipmentViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShipmentViResourceIT {

    private static final String DEFAULT_TRACKING_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TRACKING_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_SHIPPED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SHIPPED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELIVERY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELIVERY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/shipment-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShipmentViRepository shipmentViRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShipmentViMockMvc;

    private ShipmentVi shipmentVi;

    private ShipmentVi insertedShipmentVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShipmentVi createEntity() {
        return new ShipmentVi()
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
    public static ShipmentVi createUpdatedEntity() {
        return new ShipmentVi()
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);
    }

    @BeforeEach
    public void initTest() {
        shipmentVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedShipmentVi != null) {
            shipmentViRepository.delete(insertedShipmentVi);
            insertedShipmentVi = null;
        }
    }

    @Test
    @Transactional
    void createShipmentVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ShipmentVi
        var returnedShipmentVi = om.readValue(
            restShipmentViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ShipmentVi.class
        );

        // Validate the ShipmentVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertShipmentViUpdatableFieldsEquals(returnedShipmentVi, getPersistedShipmentVi(returnedShipmentVi));

        insertedShipmentVi = returnedShipmentVi;
    }

    @Test
    @Transactional
    void createShipmentViWithExistingId() throws Exception {
        // Create the ShipmentVi with an existing ID
        shipmentVi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShipmentViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentVi)))
            .andExpect(status().isBadRequest());

        // Validate the ShipmentVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTrackingNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        shipmentVi.setTrackingNumber(null);

        // Create the ShipmentVi, which fails.

        restShipmentViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllShipmentVis() throws Exception {
        // Initialize the database
        insertedShipmentVi = shipmentViRepository.saveAndFlush(shipmentVi);

        // Get all the shipmentViList
        restShipmentViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));
    }

    @Test
    @Transactional
    void getShipmentVi() throws Exception {
        // Initialize the database
        insertedShipmentVi = shipmentViRepository.saveAndFlush(shipmentVi);

        // Get the shipmentVi
        restShipmentViMockMvc
            .perform(get(ENTITY_API_URL_ID, shipmentVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shipmentVi.getId().intValue()))
            .andExpect(jsonPath("$.trackingNumber").value(DEFAULT_TRACKING_NUMBER))
            .andExpect(jsonPath("$.shippedDate").value(DEFAULT_SHIPPED_DATE.toString()))
            .andExpect(jsonPath("$.deliveryDate").value(DEFAULT_DELIVERY_DATE.toString()));
    }

    @Test
    @Transactional
    void getShipmentVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedShipmentVi = shipmentViRepository.saveAndFlush(shipmentVi);

        Long id = shipmentVi.getId();

        defaultShipmentViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultShipmentViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultShipmentViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllShipmentVisByTrackingNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentVi = shipmentViRepository.saveAndFlush(shipmentVi);

        // Get all the shipmentViList where trackingNumber equals to
        defaultShipmentViFiltering("trackingNumber.equals=" + DEFAULT_TRACKING_NUMBER, "trackingNumber.equals=" + UPDATED_TRACKING_NUMBER);
    }

    @Test
    @Transactional
    void getAllShipmentVisByTrackingNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentVi = shipmentViRepository.saveAndFlush(shipmentVi);

        // Get all the shipmentViList where trackingNumber in
        defaultShipmentViFiltering(
            "trackingNumber.in=" + DEFAULT_TRACKING_NUMBER + "," + UPDATED_TRACKING_NUMBER,
            "trackingNumber.in=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllShipmentVisByTrackingNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentVi = shipmentViRepository.saveAndFlush(shipmentVi);

        // Get all the shipmentViList where trackingNumber is not null
        defaultShipmentViFiltering("trackingNumber.specified=true", "trackingNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentVisByTrackingNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedShipmentVi = shipmentViRepository.saveAndFlush(shipmentVi);

        // Get all the shipmentViList where trackingNumber contains
        defaultShipmentViFiltering(
            "trackingNumber.contains=" + DEFAULT_TRACKING_NUMBER,
            "trackingNumber.contains=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllShipmentVisByTrackingNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedShipmentVi = shipmentViRepository.saveAndFlush(shipmentVi);

        // Get all the shipmentViList where trackingNumber does not contain
        defaultShipmentViFiltering(
            "trackingNumber.doesNotContain=" + UPDATED_TRACKING_NUMBER,
            "trackingNumber.doesNotContain=" + DEFAULT_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllShipmentVisByShippedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentVi = shipmentViRepository.saveAndFlush(shipmentVi);

        // Get all the shipmentViList where shippedDate equals to
        defaultShipmentViFiltering("shippedDate.equals=" + DEFAULT_SHIPPED_DATE, "shippedDate.equals=" + UPDATED_SHIPPED_DATE);
    }

    @Test
    @Transactional
    void getAllShipmentVisByShippedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentVi = shipmentViRepository.saveAndFlush(shipmentVi);

        // Get all the shipmentViList where shippedDate in
        defaultShipmentViFiltering(
            "shippedDate.in=" + DEFAULT_SHIPPED_DATE + "," + UPDATED_SHIPPED_DATE,
            "shippedDate.in=" + UPDATED_SHIPPED_DATE
        );
    }

    @Test
    @Transactional
    void getAllShipmentVisByShippedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentVi = shipmentViRepository.saveAndFlush(shipmentVi);

        // Get all the shipmentViList where shippedDate is not null
        defaultShipmentViFiltering("shippedDate.specified=true", "shippedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentVisByDeliveryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentVi = shipmentViRepository.saveAndFlush(shipmentVi);

        // Get all the shipmentViList where deliveryDate equals to
        defaultShipmentViFiltering("deliveryDate.equals=" + DEFAULT_DELIVERY_DATE, "deliveryDate.equals=" + UPDATED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    void getAllShipmentVisByDeliveryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentVi = shipmentViRepository.saveAndFlush(shipmentVi);

        // Get all the shipmentViList where deliveryDate in
        defaultShipmentViFiltering(
            "deliveryDate.in=" + DEFAULT_DELIVERY_DATE + "," + UPDATED_DELIVERY_DATE,
            "deliveryDate.in=" + UPDATED_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllShipmentVisByDeliveryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentVi = shipmentViRepository.saveAndFlush(shipmentVi);

        // Get all the shipmentViList where deliveryDate is not null
        defaultShipmentViFiltering("deliveryDate.specified=true", "deliveryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentVisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            shipmentViRepository.saveAndFlush(shipmentVi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        shipmentVi.setTenant(tenant);
        shipmentViRepository.saveAndFlush(shipmentVi);
        Long tenantId = tenant.getId();
        // Get all the shipmentViList where tenant equals to tenantId
        defaultShipmentViShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the shipmentViList where tenant equals to (tenantId + 1)
        defaultShipmentViShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultShipmentViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultShipmentViShouldBeFound(shouldBeFound);
        defaultShipmentViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShipmentViShouldBeFound(String filter) throws Exception {
        restShipmentViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));

        // Check, that the count call also returns 1
        restShipmentViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShipmentViShouldNotBeFound(String filter) throws Exception {
        restShipmentViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShipmentViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingShipmentVi() throws Exception {
        // Get the shipmentVi
        restShipmentViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShipmentVi() throws Exception {
        // Initialize the database
        insertedShipmentVi = shipmentViRepository.saveAndFlush(shipmentVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentVi
        ShipmentVi updatedShipmentVi = shipmentViRepository.findById(shipmentVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedShipmentVi are not directly saved in db
        em.detach(updatedShipmentVi);
        updatedShipmentVi.trackingNumber(UPDATED_TRACKING_NUMBER).shippedDate(UPDATED_SHIPPED_DATE).deliveryDate(UPDATED_DELIVERY_DATE);

        restShipmentViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedShipmentVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedShipmentVi))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedShipmentViToMatchAllProperties(updatedShipmentVi);
    }

    @Test
    @Transactional
    void putNonExistingShipmentVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shipmentVi.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShipmentVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShipmentVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShipmentViWithPatch() throws Exception {
        // Initialize the database
        insertedShipmentVi = shipmentViRepository.saveAndFlush(shipmentVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentVi using partial update
        ShipmentVi partialUpdatedShipmentVi = new ShipmentVi();
        partialUpdatedShipmentVi.setId(shipmentVi.getId());

        partialUpdatedShipmentVi.shippedDate(UPDATED_SHIPPED_DATE);

        restShipmentViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipmentVi))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipmentViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedShipmentVi, shipmentVi),
            getPersistedShipmentVi(shipmentVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateShipmentViWithPatch() throws Exception {
        // Initialize the database
        insertedShipmentVi = shipmentViRepository.saveAndFlush(shipmentVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentVi using partial update
        ShipmentVi partialUpdatedShipmentVi = new ShipmentVi();
        partialUpdatedShipmentVi.setId(shipmentVi.getId());

        partialUpdatedShipmentVi
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);

        restShipmentViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipmentVi))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipmentViUpdatableFieldsEquals(partialUpdatedShipmentVi, getPersistedShipmentVi(partialUpdatedShipmentVi));
    }

    @Test
    @Transactional
    void patchNonExistingShipmentVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shipmentVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipmentVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShipmentVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipmentVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShipmentVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(shipmentVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShipmentVi() throws Exception {
        // Initialize the database
        insertedShipmentVi = shipmentViRepository.saveAndFlush(shipmentVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the shipmentVi
        restShipmentViMockMvc
            .perform(delete(ENTITY_API_URL_ID, shipmentVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return shipmentViRepository.count();
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

    protected ShipmentVi getPersistedShipmentVi(ShipmentVi shipmentVi) {
        return shipmentViRepository.findById(shipmentVi.getId()).orElseThrow();
    }

    protected void assertPersistedShipmentViToMatchAllProperties(ShipmentVi expectedShipmentVi) {
        assertShipmentViAllPropertiesEquals(expectedShipmentVi, getPersistedShipmentVi(expectedShipmentVi));
    }

    protected void assertPersistedShipmentViToMatchUpdatableProperties(ShipmentVi expectedShipmentVi) {
        assertShipmentViAllUpdatablePropertiesEquals(expectedShipmentVi, getPersistedShipmentVi(expectedShipmentVi));
    }
}
