package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.ShipmentMiAsserts.*;
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
import xyz.jhmapstruct.domain.ShipmentMi;
import xyz.jhmapstruct.repository.ShipmentMiRepository;

/**
 * Integration tests for the {@link ShipmentMiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShipmentMiResourceIT {

    private static final String DEFAULT_TRACKING_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TRACKING_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_SHIPPED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SHIPPED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELIVERY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELIVERY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/shipment-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShipmentMiRepository shipmentMiRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShipmentMiMockMvc;

    private ShipmentMi shipmentMi;

    private ShipmentMi insertedShipmentMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShipmentMi createEntity() {
        return new ShipmentMi()
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
    public static ShipmentMi createUpdatedEntity() {
        return new ShipmentMi()
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);
    }

    @BeforeEach
    public void initTest() {
        shipmentMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedShipmentMi != null) {
            shipmentMiRepository.delete(insertedShipmentMi);
            insertedShipmentMi = null;
        }
    }

    @Test
    @Transactional
    void createShipmentMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ShipmentMi
        var returnedShipmentMi = om.readValue(
            restShipmentMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentMi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ShipmentMi.class
        );

        // Validate the ShipmentMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertShipmentMiUpdatableFieldsEquals(returnedShipmentMi, getPersistedShipmentMi(returnedShipmentMi));

        insertedShipmentMi = returnedShipmentMi;
    }

    @Test
    @Transactional
    void createShipmentMiWithExistingId() throws Exception {
        // Create the ShipmentMi with an existing ID
        shipmentMi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShipmentMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentMi)))
            .andExpect(status().isBadRequest());

        // Validate the ShipmentMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTrackingNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        shipmentMi.setTrackingNumber(null);

        // Create the ShipmentMi, which fails.

        restShipmentMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllShipmentMis() throws Exception {
        // Initialize the database
        insertedShipmentMi = shipmentMiRepository.saveAndFlush(shipmentMi);

        // Get all the shipmentMiList
        restShipmentMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));
    }

    @Test
    @Transactional
    void getShipmentMi() throws Exception {
        // Initialize the database
        insertedShipmentMi = shipmentMiRepository.saveAndFlush(shipmentMi);

        // Get the shipmentMi
        restShipmentMiMockMvc
            .perform(get(ENTITY_API_URL_ID, shipmentMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shipmentMi.getId().intValue()))
            .andExpect(jsonPath("$.trackingNumber").value(DEFAULT_TRACKING_NUMBER))
            .andExpect(jsonPath("$.shippedDate").value(DEFAULT_SHIPPED_DATE.toString()))
            .andExpect(jsonPath("$.deliveryDate").value(DEFAULT_DELIVERY_DATE.toString()));
    }

    @Test
    @Transactional
    void getShipmentMisByIdFiltering() throws Exception {
        // Initialize the database
        insertedShipmentMi = shipmentMiRepository.saveAndFlush(shipmentMi);

        Long id = shipmentMi.getId();

        defaultShipmentMiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultShipmentMiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultShipmentMiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllShipmentMisByTrackingNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentMi = shipmentMiRepository.saveAndFlush(shipmentMi);

        // Get all the shipmentMiList where trackingNumber equals to
        defaultShipmentMiFiltering("trackingNumber.equals=" + DEFAULT_TRACKING_NUMBER, "trackingNumber.equals=" + UPDATED_TRACKING_NUMBER);
    }

    @Test
    @Transactional
    void getAllShipmentMisByTrackingNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentMi = shipmentMiRepository.saveAndFlush(shipmentMi);

        // Get all the shipmentMiList where trackingNumber in
        defaultShipmentMiFiltering(
            "trackingNumber.in=" + DEFAULT_TRACKING_NUMBER + "," + UPDATED_TRACKING_NUMBER,
            "trackingNumber.in=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllShipmentMisByTrackingNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentMi = shipmentMiRepository.saveAndFlush(shipmentMi);

        // Get all the shipmentMiList where trackingNumber is not null
        defaultShipmentMiFiltering("trackingNumber.specified=true", "trackingNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentMisByTrackingNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedShipmentMi = shipmentMiRepository.saveAndFlush(shipmentMi);

        // Get all the shipmentMiList where trackingNumber contains
        defaultShipmentMiFiltering(
            "trackingNumber.contains=" + DEFAULT_TRACKING_NUMBER,
            "trackingNumber.contains=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllShipmentMisByTrackingNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedShipmentMi = shipmentMiRepository.saveAndFlush(shipmentMi);

        // Get all the shipmentMiList where trackingNumber does not contain
        defaultShipmentMiFiltering(
            "trackingNumber.doesNotContain=" + UPDATED_TRACKING_NUMBER,
            "trackingNumber.doesNotContain=" + DEFAULT_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllShipmentMisByShippedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentMi = shipmentMiRepository.saveAndFlush(shipmentMi);

        // Get all the shipmentMiList where shippedDate equals to
        defaultShipmentMiFiltering("shippedDate.equals=" + DEFAULT_SHIPPED_DATE, "shippedDate.equals=" + UPDATED_SHIPPED_DATE);
    }

    @Test
    @Transactional
    void getAllShipmentMisByShippedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentMi = shipmentMiRepository.saveAndFlush(shipmentMi);

        // Get all the shipmentMiList where shippedDate in
        defaultShipmentMiFiltering(
            "shippedDate.in=" + DEFAULT_SHIPPED_DATE + "," + UPDATED_SHIPPED_DATE,
            "shippedDate.in=" + UPDATED_SHIPPED_DATE
        );
    }

    @Test
    @Transactional
    void getAllShipmentMisByShippedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentMi = shipmentMiRepository.saveAndFlush(shipmentMi);

        // Get all the shipmentMiList where shippedDate is not null
        defaultShipmentMiFiltering("shippedDate.specified=true", "shippedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentMisByDeliveryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentMi = shipmentMiRepository.saveAndFlush(shipmentMi);

        // Get all the shipmentMiList where deliveryDate equals to
        defaultShipmentMiFiltering("deliveryDate.equals=" + DEFAULT_DELIVERY_DATE, "deliveryDate.equals=" + UPDATED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    void getAllShipmentMisByDeliveryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentMi = shipmentMiRepository.saveAndFlush(shipmentMi);

        // Get all the shipmentMiList where deliveryDate in
        defaultShipmentMiFiltering(
            "deliveryDate.in=" + DEFAULT_DELIVERY_DATE + "," + UPDATED_DELIVERY_DATE,
            "deliveryDate.in=" + UPDATED_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllShipmentMisByDeliveryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentMi = shipmentMiRepository.saveAndFlush(shipmentMi);

        // Get all the shipmentMiList where deliveryDate is not null
        defaultShipmentMiFiltering("deliveryDate.specified=true", "deliveryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentMisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            shipmentMiRepository.saveAndFlush(shipmentMi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        shipmentMi.setTenant(tenant);
        shipmentMiRepository.saveAndFlush(shipmentMi);
        Long tenantId = tenant.getId();
        // Get all the shipmentMiList where tenant equals to tenantId
        defaultShipmentMiShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the shipmentMiList where tenant equals to (tenantId + 1)
        defaultShipmentMiShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultShipmentMiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultShipmentMiShouldBeFound(shouldBeFound);
        defaultShipmentMiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShipmentMiShouldBeFound(String filter) throws Exception {
        restShipmentMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));

        // Check, that the count call also returns 1
        restShipmentMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShipmentMiShouldNotBeFound(String filter) throws Exception {
        restShipmentMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShipmentMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingShipmentMi() throws Exception {
        // Get the shipmentMi
        restShipmentMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShipmentMi() throws Exception {
        // Initialize the database
        insertedShipmentMi = shipmentMiRepository.saveAndFlush(shipmentMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentMi
        ShipmentMi updatedShipmentMi = shipmentMiRepository.findById(shipmentMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedShipmentMi are not directly saved in db
        em.detach(updatedShipmentMi);
        updatedShipmentMi.trackingNumber(UPDATED_TRACKING_NUMBER).shippedDate(UPDATED_SHIPPED_DATE).deliveryDate(UPDATED_DELIVERY_DATE);

        restShipmentMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedShipmentMi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedShipmentMi))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedShipmentMiToMatchAllProperties(updatedShipmentMi);
    }

    @Test
    @Transactional
    void putNonExistingShipmentMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentMi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shipmentMi.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShipmentMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShipmentMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentMi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShipmentMiWithPatch() throws Exception {
        // Initialize the database
        insertedShipmentMi = shipmentMiRepository.saveAndFlush(shipmentMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentMi using partial update
        ShipmentMi partialUpdatedShipmentMi = new ShipmentMi();
        partialUpdatedShipmentMi.setId(shipmentMi.getId());

        restShipmentMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipmentMi))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipmentMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedShipmentMi, shipmentMi),
            getPersistedShipmentMi(shipmentMi)
        );
    }

    @Test
    @Transactional
    void fullUpdateShipmentMiWithPatch() throws Exception {
        // Initialize the database
        insertedShipmentMi = shipmentMiRepository.saveAndFlush(shipmentMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentMi using partial update
        ShipmentMi partialUpdatedShipmentMi = new ShipmentMi();
        partialUpdatedShipmentMi.setId(shipmentMi.getId());

        partialUpdatedShipmentMi
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);

        restShipmentMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipmentMi))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipmentMiUpdatableFieldsEquals(partialUpdatedShipmentMi, getPersistedShipmentMi(partialUpdatedShipmentMi));
    }

    @Test
    @Transactional
    void patchNonExistingShipmentMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentMi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shipmentMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipmentMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShipmentMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipmentMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShipmentMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(shipmentMi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShipmentMi() throws Exception {
        // Initialize the database
        insertedShipmentMi = shipmentMiRepository.saveAndFlush(shipmentMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the shipmentMi
        restShipmentMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, shipmentMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return shipmentMiRepository.count();
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

    protected ShipmentMi getPersistedShipmentMi(ShipmentMi shipmentMi) {
        return shipmentMiRepository.findById(shipmentMi.getId()).orElseThrow();
    }

    protected void assertPersistedShipmentMiToMatchAllProperties(ShipmentMi expectedShipmentMi) {
        assertShipmentMiAllPropertiesEquals(expectedShipmentMi, getPersistedShipmentMi(expectedShipmentMi));
    }

    protected void assertPersistedShipmentMiToMatchUpdatableProperties(ShipmentMi expectedShipmentMi) {
        assertShipmentMiAllUpdatablePropertiesEquals(expectedShipmentMi, getPersistedShipmentMi(expectedShipmentMi));
    }
}
