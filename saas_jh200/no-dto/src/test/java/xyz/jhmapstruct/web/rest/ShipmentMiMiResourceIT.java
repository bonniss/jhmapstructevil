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
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.ShipmentMiMi;
import xyz.jhmapstruct.repository.ShipmentMiMiRepository;

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
        var returnedShipmentMiMi = om.readValue(
            restShipmentMiMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentMiMi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ShipmentMiMi.class
        );

        // Validate the ShipmentMiMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertShipmentMiMiUpdatableFieldsEquals(returnedShipmentMiMi, getPersistedShipmentMiMi(returnedShipmentMiMi));

        insertedShipmentMiMi = returnedShipmentMiMi;
    }

    @Test
    @Transactional
    void createShipmentMiMiWithExistingId() throws Exception {
        // Create the ShipmentMiMi with an existing ID
        shipmentMiMi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShipmentMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentMiMi)))
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

        restShipmentMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentMiMi)))
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
    void getShipmentMiMisByIdFiltering() throws Exception {
        // Initialize the database
        insertedShipmentMiMi = shipmentMiMiRepository.saveAndFlush(shipmentMiMi);

        Long id = shipmentMiMi.getId();

        defaultShipmentMiMiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultShipmentMiMiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultShipmentMiMiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllShipmentMiMisByTrackingNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentMiMi = shipmentMiMiRepository.saveAndFlush(shipmentMiMi);

        // Get all the shipmentMiMiList where trackingNumber equals to
        defaultShipmentMiMiFiltering(
            "trackingNumber.equals=" + DEFAULT_TRACKING_NUMBER,
            "trackingNumber.equals=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllShipmentMiMisByTrackingNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentMiMi = shipmentMiMiRepository.saveAndFlush(shipmentMiMi);

        // Get all the shipmentMiMiList where trackingNumber in
        defaultShipmentMiMiFiltering(
            "trackingNumber.in=" + DEFAULT_TRACKING_NUMBER + "," + UPDATED_TRACKING_NUMBER,
            "trackingNumber.in=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllShipmentMiMisByTrackingNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentMiMi = shipmentMiMiRepository.saveAndFlush(shipmentMiMi);

        // Get all the shipmentMiMiList where trackingNumber is not null
        defaultShipmentMiMiFiltering("trackingNumber.specified=true", "trackingNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentMiMisByTrackingNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedShipmentMiMi = shipmentMiMiRepository.saveAndFlush(shipmentMiMi);

        // Get all the shipmentMiMiList where trackingNumber contains
        defaultShipmentMiMiFiltering(
            "trackingNumber.contains=" + DEFAULT_TRACKING_NUMBER,
            "trackingNumber.contains=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllShipmentMiMisByTrackingNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedShipmentMiMi = shipmentMiMiRepository.saveAndFlush(shipmentMiMi);

        // Get all the shipmentMiMiList where trackingNumber does not contain
        defaultShipmentMiMiFiltering(
            "trackingNumber.doesNotContain=" + UPDATED_TRACKING_NUMBER,
            "trackingNumber.doesNotContain=" + DEFAULT_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllShipmentMiMisByShippedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentMiMi = shipmentMiMiRepository.saveAndFlush(shipmentMiMi);

        // Get all the shipmentMiMiList where shippedDate equals to
        defaultShipmentMiMiFiltering("shippedDate.equals=" + DEFAULT_SHIPPED_DATE, "shippedDate.equals=" + UPDATED_SHIPPED_DATE);
    }

    @Test
    @Transactional
    void getAllShipmentMiMisByShippedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentMiMi = shipmentMiMiRepository.saveAndFlush(shipmentMiMi);

        // Get all the shipmentMiMiList where shippedDate in
        defaultShipmentMiMiFiltering(
            "shippedDate.in=" + DEFAULT_SHIPPED_DATE + "," + UPDATED_SHIPPED_DATE,
            "shippedDate.in=" + UPDATED_SHIPPED_DATE
        );
    }

    @Test
    @Transactional
    void getAllShipmentMiMisByShippedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentMiMi = shipmentMiMiRepository.saveAndFlush(shipmentMiMi);

        // Get all the shipmentMiMiList where shippedDate is not null
        defaultShipmentMiMiFiltering("shippedDate.specified=true", "shippedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentMiMisByDeliveryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentMiMi = shipmentMiMiRepository.saveAndFlush(shipmentMiMi);

        // Get all the shipmentMiMiList where deliveryDate equals to
        defaultShipmentMiMiFiltering("deliveryDate.equals=" + DEFAULT_DELIVERY_DATE, "deliveryDate.equals=" + UPDATED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    void getAllShipmentMiMisByDeliveryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentMiMi = shipmentMiMiRepository.saveAndFlush(shipmentMiMi);

        // Get all the shipmentMiMiList where deliveryDate in
        defaultShipmentMiMiFiltering(
            "deliveryDate.in=" + DEFAULT_DELIVERY_DATE + "," + UPDATED_DELIVERY_DATE,
            "deliveryDate.in=" + UPDATED_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllShipmentMiMisByDeliveryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentMiMi = shipmentMiMiRepository.saveAndFlush(shipmentMiMi);

        // Get all the shipmentMiMiList where deliveryDate is not null
        defaultShipmentMiMiFiltering("deliveryDate.specified=true", "deliveryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentMiMisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            shipmentMiMiRepository.saveAndFlush(shipmentMiMi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        shipmentMiMi.setTenant(tenant);
        shipmentMiMiRepository.saveAndFlush(shipmentMiMi);
        Long tenantId = tenant.getId();
        // Get all the shipmentMiMiList where tenant equals to tenantId
        defaultShipmentMiMiShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the shipmentMiMiList where tenant equals to (tenantId + 1)
        defaultShipmentMiMiShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultShipmentMiMiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultShipmentMiMiShouldBeFound(shouldBeFound);
        defaultShipmentMiMiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShipmentMiMiShouldBeFound(String filter) throws Exception {
        restShipmentMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentMiMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));

        // Check, that the count call also returns 1
        restShipmentMiMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShipmentMiMiShouldNotBeFound(String filter) throws Exception {
        restShipmentMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShipmentMiMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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

        restShipmentMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedShipmentMiMi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedShipmentMiMi))
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

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shipmentMiMi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentMiMi))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentMiMi))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentMiMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentMiMi)))
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

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shipmentMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipmentMiMi))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipmentMiMi))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentMiMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(shipmentMiMi)))
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
